# Imagen base con JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Crear directorio para la app y las keys
RUN mkdir /app /keys

# Crear un usuario no-root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el JAR generado por Gradle
COPY applications/app-service/build/libs/MS_Solicitudes.jar ./MS_Solicitudes.jar

# Definir variables de entorno para JWT
ENV JWT_PRAGMA_PRIVATE_KEY_PATH=/keys/private.pem
ENV JWT_PRAGMA_PUBLIC_KEY_PATH=/keys/public.pem

# Puerto que expone la app
EXPOSE 8081

# Opciones de JVM
ENV JAVA_OPTS="-Xmx512m -Djava.security.egd=file:/dev/./urandom"

# Copiar el script al contenedor
COPY deployment/wait-for-it.sh /wait-for-it.sh

# Comando para ejecutar la aplicación
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar MS_Solicitudes.jar" ]
