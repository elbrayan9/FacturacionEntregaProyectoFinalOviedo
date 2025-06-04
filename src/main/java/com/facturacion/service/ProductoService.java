// src/main/java/com/facturacion/service/ProductoService.java
package com.facturacion.service;

import com.facturacion.model.Producto;
import com.facturacion.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Producto findById(Integer id) {
        return productoRepository.findById(id).orElse(null);
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto update(Integer id, Producto productoDetails) {
        Producto producto = findById(id);
        if (producto != null) {
            producto.setNombre(productoDetails.getNombre());
            producto.setPrecio(productoDetails.getPrecio());
            producto.setStock(productoDetails.getStock());
            return productoRepository.save(producto);
        }
        return null;
    }

    public void delete(Integer id) {
        productoRepository.deleteById(id);
    }
}