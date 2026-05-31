# Usa Java 17 como base
FROM eclipse-temurin:17-jdk-alpine

# Crea la carpeta de trabajo
WORKDIR /app

# Copia el pom y descarga las dependencias primero
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Da permisos al mvnw
RUN chmod +x mvnw

# Descarga las dependencias
RUN ./mvnw dependency:go-offline -B

# Copia el codigo fuente
COPY src src

# Compila y empaqueta el JAR
RUN ./mvnw package -DskipTests

# Expone el puerto 8080
EXPOSE 8080

# Ejecuta el JAR
CMD ["java", "-jar", "target/Rambed360-0.0.1-SNAPSHOT.jar"]