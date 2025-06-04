// src/main/java/com/facturacion/controller/ComprobanteController.java
package com.facturacion.controller;

import com.facturacion.model.Comprobante;
import com.facturacion.service.ComprobanteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comprobantes")
public class ComprobanteController {

    private final ComprobanteService comprobanteService;

    public ComprobanteController(ComprobanteService comprobanteService) {
        this.comprobanteService = comprobanteService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createComprobante(@RequestBody Comprobante comprobanteRequest) {
        Map<String, Object> result = comprobanteService.createComprobante(comprobanteRequest);
        
        if ("error".equals(result.get("status"))) {
            // Si hay errores de validación, devolver un 400 Bad Request
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } else {
            // Si la creación fue exitosa, devolver un 201 Created
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
    }

    @GetMapping
    public List<Comprobante> getAllComprobantes() {
        return comprobanteService.findAllComprobantes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comprobante> getComprobanteById(@PathVariable Integer id) {
        Comprobante comprobante = comprobanteService.findComprobanteById(id);
        if (comprobante != null) {
            return new ResponseEntity<>(comprobante, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}