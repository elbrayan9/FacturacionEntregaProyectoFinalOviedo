
#!/bin/bash
echo "Compilando el proyecto..."
mvn clean package

echo "Ejecutando la aplicación..."
java -jar target/FacturacionSegundaEntregaOviedo-1.0-SNAPSHOT.jar
