// src/main/java/com/facturacion/repository/ComprobanteRepository.java
package com.facturacion.repository;

import com.facturacion.model.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComprobanteRepository extends JpaRepository<Comprobante, Integer> {}