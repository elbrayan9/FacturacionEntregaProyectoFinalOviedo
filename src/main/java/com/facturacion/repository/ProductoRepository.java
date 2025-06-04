// src/main/java/com/facturacion/repository/ProductoRepository.java
package com.facturacion.repository;

import com.facturacion.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {}