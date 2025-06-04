-- src/main/resources/schema.sql
CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    email VARCHAR(100)
);

CREATE TABLE producto (
    productoid INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    precio DECIMAL(10,2),
    stock INT
);

CREATE TABLE comprobante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    clienteid INT,
    fecha VARCHAR(100),
    total DECIMAL(10,2),
    cantidad_total INT,
    FOREIGN KEY (clienteid) REFERENCES cliente(id)
);

CREATE TABLE linea_comprobante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    comprobante_id INT,
    productoid INT,
    cantidad INT,
    precio_unitario DECIMAL(10,2),
    FOREIGN KEY (comprobante_id) REFERENCES comprobante(id),
    FOREIGN KEY (productoid) REFERENCES producto(productoid)
);