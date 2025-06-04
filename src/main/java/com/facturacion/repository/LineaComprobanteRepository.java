// src/main/java/com/facturacion/repository/LineaComprobanteRepository.java
package com.facturacion.repository;

import com.facturacion.model.LineaComprobante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineaComprobanteRepository extends JpaRepository<LineaComprobante, Integer> {}