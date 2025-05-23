
CREATE TABLE cliente (
    clienteid INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE producto (
    productoid INT PRIMARY KEY,
    nombre VARCHAR(100),
    precio DECIMAL(10,2),
    stock INT
);

CREATE TABLE comprobante (
    id SERIAL PRIMARY KEY,
    clienteid INT,
    fecha VARCHAR(100),
    total DECIMAL(10,2),
    cantidad_total INT,
    FOREIGN KEY (clienteid) REFERENCES cliente(clienteid)
);

CREATE TABLE linea_comprobante (
    id SERIAL PRIMARY KEY,
    comprobante_id INT,
    productoid INT,
    cantidad INT,
    precio_unitario DECIMAL(10,2),
    FOREIGN KEY (comprobante_id) REFERENCES comprobante(id),
    FOREIGN KEY (productoid) REFERENCES producto(productoid)
);
