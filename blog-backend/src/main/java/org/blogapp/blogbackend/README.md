# BlogApp - Plataforma de Blogging Full-Stack

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-17+-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-blue?style=for-the-badge&logo=docker&logoColor=white)

Este proyecto es una aplicación web full-stack desarrollada como un ejercicio práctico para consolidar y demostrar conocimientos en tecnologías modernas. El objetivo principal fue construir una plataforma de blogging funcional desde cero, implementando un backend con **Spring Boot**, un frontend con **Angular**, autenticación segura mediante **JWT**, y persistencia de datos con **MySQL**.

## ✨ Características Principales

- **Autenticación y Autorización:** Sistema de registro e inicio de sesión seguro utilizando **JWT (JSON Web Tokens)**.
- **Gestión de Usuarios:** Los usuarios pueden registrarse y obtener un perfil.
- **CRUD de Posts:** Los usuarios autenticados pueden crear, leer, actualizar y eliminar sus propias publicaciones.
- **Visualización Pública:** Los posts son visibles para cualquier visitante, pero solo los autores pueden modificarlos.
- **Perfiles de Usuario:** Páginas de perfil que muestran la información y las publicaciones de un usuario.
- **Subida de Archivos:** Funcionalidad para subir imágenes de perfil y de banner.
- **Documentación de API:** La API REST está documentada con **Swagger/OpenAPI** para facilitar su exploración y prueba.

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3**
- **Spring Security** (para la gestión de seguridad y JWT)
- **Spring Data JPA** (con Hibernate)
- **MySQL** como base de datos relacional.
- **Maven** para la gestión de dependencias.
- **jjwt** para la creación y validación de tokens JWT.

### Frontend
- **Angular 17**
- **TypeScript**
- **HTML5 & CSS3**

### Base de Datos y Despliegue
- **Docker & Docker Compose** para orquestar la base de datos MySQL.
- **Swagger/OpenAPI 3** para la documentación de la API.

## 🚀 Cómo Empezar (Configuración Local)

Sigue estos pasos para levantar el proyecto en tu máquina local.

### Prerrequisitos

Asegúrate de tener instalado lo siguiente:
- **JDK 17** o superior.
- **Maven 3.8** o superior.
- **Node.js 18** o superior (con npm).
- **Docker** y **Docker Compose**.

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/blog-app.git
cd blog-app
```

### 2. Configurar la Base de Datos (MySQL con Docker)

El proyecto está configurado para conectarse a una base de datos MySQL. Puedes levantar una instancia fácilmente con Docker.

1.  Navega a la carpeta del backend:
    ```bash
    cd blog-backend
    ```
2.  Ejecuta el siguiente comando para iniciar el contenedor de MySQL:
    ```bash
    docker run --name blog-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=blog_db -d mysql:8.0
    ```
    Esto creará una base de datos llamada `blog_db` con el usuario `root` y la contraseña `root`.

3.  **Importante:** Asegúrate de que tu archivo `src/main/resources/application.properties` en el backend tenga la configuración correcta para la base de datos:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/blog_db
    spring.datasource.username=root
    spring.datasource.password=root
    spring.jpa.hibernate.ddl-auto=update
    ```

### 3. Levantar el Backend (Spring Boot)

1.  Desde la carpeta `blog-backend`, compila y ejecuta la aplicación:
    ```bash
    mvn spring-boot:run
    ```
2.  El servidor se iniciará en `http://localhost:8080`.

### 4. Levantar el Frontend (Angular)

1.  Abre una nueva terminal y navega a la carpeta del frontend:
    ```bash
    cd blog-frontend
    ```
2.  Instala las dependencias:
    ```bash
    npm install
    ```
3.  Inicia el servidor de desarrollo de Angular:
    ```bash
    ng serve
    ```
4.  Abre tu navegador y ve a `http://localhost:4200`.

¡Y listo! La aplicación debería estar funcionando completamente en tu entorno local.

## 📖 Documentación de la API

Una vez que el backend esté en ejecución, puedes acceder a la documentación interactiva de la API generada por Swagger en la siguiente URL:

**http://localhost:8080/swagger-ui/index.html**

Desde allí, puedes probar todos los endpoints de la API, incluyendo los que requieren autenticación (recuerda usar el prefijo `Bearer ` antes del token).

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo `LICENSE` para más detalles.
