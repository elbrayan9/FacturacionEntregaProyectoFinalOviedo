# API Facturación - Segunda Entrega

Este proyecto implementa un sistema de facturación básico utilizando Spring Boot, JPA y H2 (base de datos en memoria).

## 🚀 Descripción del Proyecto

Este sistema de facturación permite administrar clientes, productos y generar comprobantes de venta. Se ha desarrollado siguiendo una arquitectura de tres capas: `Controller` (para manejar las solicitudes REST), `Service` (para la lógica de negocio y validaciones) y `Repository` (para el acceso a datos mediante JPA). La base de datos utilizada es H2 en memoria, lo que significa que no requiere instalación adicional y los datos se reinician con cada ejecución de la aplicación.

## 📦 Estructura del Proyecto

El proyecto está organizado de la siguiente manera:

* `src/main/java/com/facturacion/`:
    * `controller/`: Contiene los controladores REST para gestionar las solicitudes HTTP.
    * `service/`: Contiene la lógica de negocio y las validaciones.
    * `repository/`: Interfaces para el acceso a datos con Spring Data JPA.
    * `model/`: Clases de las entidades de datos (Cliente, Producto, Comprobante, LineaComprobante).
* `src/main/resources/`:
    * `application.properties`: Archivo de configuración de Spring Boot.
    * `schema.sql`: Script SQL para la creación de tablas de la base de datos al iniciar la aplicación.
    * `data.sql`: Script SQL para la inserción de datos iniciales en la base de datos.
* `pom.xml`: Archivo de configuración de Maven para la gestión de dependencias y la compilación.
* `run_project.sh`: Script para compilar y ejecutar la aplicación en Linux/Mac.
* `README.md`: Este archivo de documentación de la API.
* `Facturacion_Postman_Collection.json`: Colección de Postman para probar los endpoints de la API.

## 🧰 Requisitos

Para compilar y ejecutar este proyecto, necesitas tener instalados los siguientes elementos:

* **Java Development Kit (JDK) 17** o superior.
* **Apache Maven** (versión 3.x o superior), accesible desde tu terminal (`mvn -v`).

## ⚙️ Instrucciones para Compilar y Ejecutar

Sigue estos pasos para poner en marcha la aplicación:

1.  **Clona o descarga el proyecto.**
2.  **Abre tu terminal** y navega hasta el directorio raíz del proyecto (donde se encuentra el archivo `pom.xml`).
    ```bash
    cd /ruta/a/tu/proyecto/FacturacionEntregaProyectoFinalOviedo
    ```
3.  **Compila el proyecto:** Este comando generará el archivo `.jar` ejecutable dentro de la carpeta `target/`.
    ```bash
    mvn clean package
    ```
    * _Nota: Si la compilación es exitosa, verás un mensaje de `BUILD SUCCESS` al final._
4.  **Ejecuta la aplicación:**
    ```bash
    java -jar target/FacturacionSegundaEntregaOviedo-1.0-SNAPSHOT.jar
    ```
    * La aplicación se iniciará y estará disponible en `http://localhost:8080/`. Deberías ver logs en la consola indicando que Spring Boot está arrancando.

## 🛠 Base de Datos H2 (En Memoria)

El proyecto utiliza una base de datos H2 en memoria, lo que facilita su configuración y uso para desarrollo y pruebas.

### Acceso a la Consola H2

Puedes acceder a la consola web de H2 para visualizar las tablas y los datos directamente:

* **URL de la Consola:** `http://localhost:8080/h2-console`
* **JDBC URL:** `jdbc:h2:mem:facturaciondb`
* **Usuario:** `sa`
* **Contraseña:** (dejar vacío)

## 🧪 Endpoints Disponibles (API REST)

La API está disponible en la `Base URL`: `http://localhost:8080/`.

### Endpoints de Clientes

* `GET /clientes` → Listar todos los clientes.
* `POST /clientes` → Crear un nuevo cliente.
* `GET /clientes/{id}` → Buscar cliente por ID.
* `PUT /clientes/{id}` → Actualizar cliente existente.
* `DELETE /clientes/{id}` → Eliminar cliente.

### Endpoints de Productos

* `GET /productos` → Listar todos los productos.
* `POST /productos` → Crear un nuevo producto.
* `GET /productos/{id}` → Buscar producto por ID.
* `PUT /productos/{id}` → Actualizar producto existente.
* `DELETE /productos/{id}` → Eliminar producto.

### Endpoints de Comprobantes

* `GET /comprobantes` → Listar todos los comprobantes.
* `POST /comprobantes` → Crear un nuevo comprobante.
* `GET /comprobantes/{id}` → Buscar comprobante por ID.

## 📝 Ejemplos de Solicitudes (Postman)

Puedes importar el archivo `Facturacion_Postman_Collection.json` en Postman para tener preconfiguradas las solicitudes de prueba.

### **Crear Comprobante (POST /comprobantes)**

Este endpoint es el más importante para la lógica de negocio. Soporta las siguientes validaciones y funcionalidades:

* Verificación de existencia de `cliente`.
* Verificación de existencia de `productos` en cada línea.
* Validación de `stock` disponible para cada producto.
* Reducción automática del `stock` de los productos vendidos.
* Captura el precio del producto al momento de la venta (`precioUnitario` en `linea_comprobante`), asegurando que cambios futuros en el precio del producto no afecten comprobantes ya emitidos.
* La fecha del comprobante se obtiene de un servicio externo (`http://worldclockapi.com/`). Si falla, se usa la fecha local.
* Calcula el `total` y la `cantidadTotal` del comprobante.
* Devuelve respuestas claras: `201 Created` para éxito, `400 Bad Request` con mensajes de error para validaciones fallidas.

**Ejemplo de Body para POST /comprobantes:**

```json
{
  "cliente": {
    "id": 1
  },
  "lineas": [
    {
      "cantidad": 1,
      "producto": {
        "productoid": 1
      }
    },
    {
      "cantidad": 2,
      "producto": {
        "productoid": 2
      }
    }
  ]
}