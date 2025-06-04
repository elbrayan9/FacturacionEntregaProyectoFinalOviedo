// src/main/java/com/facturacion/service/ComprobanteService.java
package com.facturacion.service;

import com.facturacion.model.Cliente;
import com.facturacion.model.Comprobante;
import com.facturacion.model.LineaComprobante;
import com.facturacion.model.Producto;
import com.facturacion.repository.ClienteRepository;
import com.facturacion.repository.ComprobanteRepository;
import com.facturacion.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap; // Para mantener el orden de inserción de los mensajes de error

@Service
public class ComprobanteService {

    private final ComprobanteRepository comprobanteRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final RestTemplate restTemplate;

    public ComprobanteService(ComprobanteRepository comprobanteRepository, ClienteRepository clienteRepository, ProductoRepository productoRepository) {
        this.comprobanteRepository = comprobanteRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.restTemplate = new RestTemplate();
    }

    @Transactional
    public Map<String, Object> createComprobante(Comprobante comprobanteRequest) {
        Map<String, Object> response = new LinkedHashMap<>();
        List<String> errors = new ArrayList<>();
        BigDecimal totalComprobante = BigDecimal.ZERO;
        Integer cantidadTotalComprobante = 0;

        // 1. Validar Cliente Existente
        if (comprobanteRequest.getCliente() == null || comprobanteRequest.getCliente().getId() == null) {
            errors.add("El cliente es requerido.");
        } else {
            Cliente cliente = clienteRepository.findById(comprobanteRequest.getCliente().getId()).orElse(null);
            if (cliente == null) {
                errors.add("Cliente con ID " + comprobanteRequest.getCliente().getId() + " no encontrado.");
            } else {
                comprobanteRequest.setCliente(cliente); // Asignar el cliente completo
            }
        }

        // Validaciones de Líneas de Comprobante
        if (comprobanteRequest.getLineas() == null || comprobanteRequest.getLineas().isEmpty()) {
            errors.add("El comprobante debe tener al menos una línea de producto.");
        } else {
            List<LineaComprobante> lineasValidadas = new ArrayList<>();
            for (LineaComprobante lineaRequest : comprobanteRequest.getLineas()) {
                if (lineaRequest.getProducto() == null || lineaRequest.getProducto().getProductoid() == null) {
                    errors.add("Producto es requerido en una línea de comprobante.");
                    continue;
                }
                if (lineaRequest.getCantidad() == null || lineaRequest.getCantidad() <= 0) {
                    errors.add("La cantidad del producto con ID " + lineaRequest.getProducto().getProductoid() + " debe ser mayor a 0.");
                    continue;
                }

                Producto producto = productoRepository.findById(lineaRequest.getProducto().getProductoid()).orElse(null);
                if (producto == null) {
                    errors.add("Producto con ID " + lineaRequest.getProducto().getProductoid() + " no encontrado.");
                    continue;
                }

                if (producto.getStock() < lineaRequest.getCantidad()) {
                    errors.add("Stock insuficiente para el producto " + producto.getNombre() + ". Stock disponible: " + producto.getStock() + ", Solicitado: " + lineaRequest.getCantidad());
                    continue;
                }

                // Si las validaciones pasan, se prepara la línea del comprobante
                LineaComprobante lineaComprobante = new LineaComprobante();
                lineaComprobante.setProducto(producto);
                lineaComprobante.setCantidad(lineaRequest.getCantidad());
                lineaComprobante.setPrecioUnitario(producto.getPrecio()); // Almacenar el precio actual del producto

                lineasValidadas.add(lineaComprobante);

                // Reducir el stock del producto
                producto.setStock(producto.getStock() - lineaRequest.getCantidad());
                productoRepository.save(producto); // Actualizar el stock en la base de datos

                totalComprobante = totalComprobante.add(producto.getPrecio().multiply(BigDecimal.valueOf(lineaRequest.getCantidad())));
                cantidadTotalComprobante += lineaRequest.getCantidad();
            }
            comprobanteRequest.setLineas(lineasValidadas); // Asignar las líneas validadas y completas
        }

        if (!errors.isEmpty()) {
            response.put("status", "error");
            response.put("messages", errors);
            return response;
        }

        // Obtener fecha del servicio externo o local
        String fechaComprobante = obtenerFechaActual();
        comprobanteRequest.setFecha(fechaComprobante);
        comprobanteRequest.setTotal(totalComprobante);
        comprobanteRequest.setCantidadTotal(cantidadTotalComprobante);

        // Guardar el comprobante y sus líneas
        Comprobante savedComprobante = comprobanteRepository.save(comprobanteRequest);
        for (LineaComprobante linea : savedComprobante.getLineas()) {
            linea.setComprobante(savedComprobante); // Establecer la referencia al comprobante
        }
        comprobanteRepository.save(savedComprobante); // Volver a guardar para actualizar las relaciones si es necesario

        response.put("status", "success");
        response.put("message", "Comprobante creado exitosamente.");
        response.put("comprobante", savedComprobante);
        return response;
    }

    private String obtenerFechaActual() {
        String fecha = null;
        try {
            // Intentar obtener la fecha del servicio externo
            String apiUrl = "http://worldclockapi.com/api/json/utc/now";
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                // El campo "currentDateTime" en la respuesta del API es algo como "2023-06-03T14:30Z"
                // Queremos formatearlo a algo más legible si es necesario, o usarlo directamente.
                // Para este ejemplo, usaremos el formato tal cual viene.
                fecha = root.path("currentDateTime").asText();
                if (fecha.isEmpty()) {
                    fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener la fecha del servicio externo: " + e.getMessage());
            // Si el servicio falla, calcular la fecha usando la clase Date de Java
            fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return fecha;
    }

    public List<Comprobante> findAllComprobantes() {
        return comprobanteRepository.findAll();
    }

    public Comprobante findComprobanteById(Integer id) {
        return comprobanteRepository.findById(id).orElse(null);
    }
}