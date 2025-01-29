# Usa una imagen ligera para el runtime de Java
FROM eclipse-temurin:21-jre

# Establece el directorio de trabajo
WORKDIR /app

# Copia tu archivo JAR al contenedor
COPY target/mySongSetBackend-0.0.1-SNAPSHOT.jar app.jar


# Expone el puerto del backend
EXPOSE 8080

# Ejecuta la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
