#Define la imagen base sobre la cual se construirá esta imagen personalizada.
FROM eclipse-temurin:24-jdk-alpine
# FROM eclipse-temurin:17-jdk-alpine
# Define el directorio de trabajo dentro del contenedor.
VOLUME /tmp
# El argumento JAR_FILE se utiliza para especificar el archivo JAR que se copiará al contenedor.
ARG JAR_FILE=target/*.jar
# Copia el archivo JAR de la aplicación al contenedor.
COPY ${JAR_FILE} libro.jar
# Informa que el contenedor usa el puerto 8080 para aceptar conexiones.
EXPOSE 8080
#Define el comando principal que se ejecuta cuando arranca el contenedor.
#En este caso: inicia tu app Spring Boot con java -jar /libro.jar
ENTRYPOINT ["java","-jar","/libro.jar"]