# 📦 Cómo generar el archivo .jar ejecutable

Este proyecto está preparado para ser compilado y ejecutado como un `.jar` usando Maven.

---

## 🧰 Requisitos
- Tener **Java 17** o superior instalado
- Tener **Apache Maven** instalado y accesible desde la terminal (`mvn -v`)

---

## ⚙️ Instrucciones para compilar y ejecutar

### 1. Abrir la terminal en el directorio raíz del proyecto
Ejemplo:
```bash
cd FacturacionEntregaProyectoFinalOviedo_COMPLETO
```

### 2. Ejecutar el siguiente comando:
```bash
mvn clean package
```
Esto generará un archivo `.jar` dentro de la carpeta `target/`.

### 3. Ejecutar el .jar generado:
```bash
java -jar target/FacturacionEntregaProyectoFinalOviedo.jar
```

---

## 🧪 Probar con Postman
1. Abrí Postman.
2. Importá el archivo `Facturacion_Postman_Collection.json` incluido.
3. Ejecutá la petición `Crear Comprobante` contra:
```http
http://localhost:8080/comprobantes
```

---

## 🛠 Base de datos
Podés crear las tablas necesarias ejecutando el archivo `Facturacion_Creacion_Tabla.sql` en una base compatible como H2, MySQL o PostgreSQL (ajustando si hace falta).

---

✅ ¡Con esto ya podés correr y probar el sistema de comprobantes completo!
