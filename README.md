# API Facturación - Segunda Entrega

## Base URL
http://localhost:8080/

## Endpoints

### Clientes
- `GET /clientes` → Listar todos los clientes
- `POST /clientes` → Crear nuevo cliente
- `GET /clientes/{id}` → Buscar cliente por ID
- `PUT /clientes/{id}` → Actualizar cliente
- `DELETE /clientes/{id}` → Eliminar cliente

## Acceso a la Consola H2
http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:facturaciondb`
- Usuario: `sa`
- Contraseña: *(vacío)*