# Playlist Manager

Gestor de listas de reproducción construido como **monorepo** con separación estricta de responsabilidades entre un backend REST (Spring Boot 3 + Java 17/21), una aplicación web (Angular Standalone + Reactive Forms), una colección de pruebas para consumo de API (Postman) y un ejercicio de refactorización orientada a objetos.

La especificación funcional y el contrato técnico del sistema están definidos en [`SPECS.md`](SPECS.md).

---

## 1. Estructura del Monorepo

```
playlist-manager/
├── backend/                       # API REST Spring Boot 3 + Java 17/21 (Maven)
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/playlist/manager/
│       │   │   ├── config/security/   # SecurityConfig, RestAuthenticationEntryPoint
│       │   │   ├── controller/        # ListController (endpoints REST)
│       │   │   ├── service/           # ListService (interfaz) + ListServiceImpl
│       │   │   ├── repository/        # ListRepository (Spring Data JPA)
│       │   │   ├── domain/entity/     # Playlist, Song (entidades JPA)
│       │   │   ├── dto/               # records inmutables (request/response/error)
│       │   │   ├── mapper/            # ListMapper (DTO <-> Entidad)
│       │   │   └── exception/         # Excepciones de dominio + GlobalExceptionHandler
│       │   └── resources/application.properties
│       └── test/java/com/playlist/manager/  # Pruebas unitarias y de integración
│
├── frontend/                      # Aplicación web Angular 17 Standalone
│   ├── package.json / angular.json / tsconfig*.json
│   └── src/
│       ├── app/
│       │   ├── core/
│       │   │   ├── models/          # Interfaces tipadas (Playlist, Song, ApiError)
│       │   │   ├── interceptors/    # auth.interceptor, error.interceptor
│       │   │   └── services/        # PlaylistService (HttpClient)
│       │   ├── features/            # list-list, list-create, list-detail
│       │   ├── app.config.ts, app.routes.ts, app.component.ts
│       │   └── environments/
│       ├── index.html, main.ts, styles.css
│
├── postman/                       # Colección y entorno de Postman (v2.1)
│   ├── PlaylistManager.postman_collection.json
│   └── PlaylistManager.postman_environment.json
│
├── ejercicio-1-refactor/          # Ejercicio de refactorización POO (SOLID)
│   ├── Posicion.java
│   ├── Jugador.java
│   ├── Equipo.java
│   ├── EquipoReporte.java
│   └── Main.java
│
├── SPECS.md                       # Requerimientos funcionales y contrato técnico
├── Teorica-Staff-III.md           # Resolución de la evaluación teórica
└── AI-LOG.md                      # Bitácora del proceso de desarrollo asistido por IA
```

### Propósito de cada componente

| Carpeta / archivo | Descripción |
| :--- | :--- |
| `/backend` | API REST `/lists` con Spring Boot 3, Spring Data JPA, Spring Security, H2 y pruebas unitarias. |
| `/frontend` | SPA en Angular 17 con componentes Standalone, formularios reactivos y autenticación Basic integrada. |
| `/postman` | Colección y entorno para probar los endpoints de la API desde un cliente REST. |
| `/ejercicio-1-refactor` | Solución refactorizada (SOLID/POO) de una clase Java legado. |
| `SPECS.md` | Especificación técnica: endpoints, representación JSON y requerimientos no funcionales. |
| `Teorica-Staff-III.md` | Resolución íntegra de la evaluación teórica. |
| `AI-LOG.md` | Bitácora y registro del trabajo asistido por IA. |

---

## 2. Decisiones Técnicas y de Arquitectura Destacadas

- **Separación estricta de capas**: el flujo de una petición recorre `controller → service → repository`, con entidades JPA aisladas del mundo exterior. Nunca se exponen entidades JPA en los controladores; siempre se usan **DTOs inmutables (`record`)** con validación declarativa (`jakarta.validation`).

- **Prevención del problema N+1**: la recuperación de listas con sus canciones usa `@EntityGraph(attributePaths = "canciones")` en `ListRepository` (tanto en `findAll()` como en `findByNombreIgnoreCase`), con `CascadeType.ALL` y `orphanRemoval = true` en la relación `Playlist ↔ Song`.

- **Seguridad Stateless**: Spring Security con **HTTP Basic Auth** (`SessionCreationPolicy.STATELESS`), `BCryptPasswordEncoder` (nunca `{noop}`) y usuarios en memoria con rol `USER`. CORS explícito habilitado para el origen de Angular (`http://localhost:4200`) con métodos `GET, POST, DELETE, OPTIONS` y cabeceras `Authorization, Content-Type`. Los errores `401 Unauthorized` se devuelven como JSON estandarizado (`ErrorResponse`) mediante `RestAuthenticationEntryPoint`, en lugar del reto HTML por defecto.

- **Manejo centralizado de errores**: `GlobalExceptionHandler` (`@RestControllerAdvice`) garantiza respuestas consistentes para `400 Bad Request` (nombre nulo/vacío/blanco o duplicado), `404 Not Found` (lista inexistente) y `500 Internal Server Error`.

- **Codificación segura de URLs**: el header `Location` del `201 Created` en `POST /lists` se construye con `UriComponentsBuilder`, soportando correctamente nombres con espacios/caracteres especiales; en el frontend se aplica `encodeURIComponent(name)` en `PlaylistService`.

---

## 3. Requisitos y Guía de Ejecución

### Requisitos previos

- **Backend**: JDK 17+ y Maven 3.8+.
- **Frontend**: Node.js 18+ y npm.
- **Postman**: cliente de escritorio para importar la colección.

### 3.1 Backend (`/backend`)

Compilar y ejecutar las pruebas:

```bash
cd backend
mvn clean test
```

Arrancar el servidor (escucha en `http://localhost:8080`):

```bash
mvn spring-boot:run
```

**Credenciales HTTP Basic por defecto**: usuario `user` / contraseña `playlist`.

**Variables de entorno configurables** (con valores por defecto seguros):

| Variable | Default | Descripción |
| :--- | :--- | :--- |
| `BASIC_USER` | `user` | Usuario HTTP Basic. |
| `BASIC_PASSWORD` | `playlist` | Contraseña HTTP Basic. |
| `DATASOURCE_USERNAME` | `sa` | Usuario de la base de datos H2. |
| `DATASOURCE_PASSWORD` | *(vacío)* | Contraseña de la base de datos H2. |

La base de datos H2 se configura en memoria (`jdbc:h2:mem:playlistdb`). Las credenciales se externalizan por variables de entorno para no versionar secretos en `application.properties`.

### 3.2 Frontend (`/frontend`)

Instalar dependencias:

```bash
cd frontend
npm install
```

Levantar el servidor de desarrollo (en `http://localhost:4200`):

```bash
npm start
# o bien
ng serve
```

Las credenciales y la URL de la API se configuran en `src/environments/environment.ts` (`apiUrl`, `basicUser`, `basicPassword`); edítalas ahí para probar el escenario de credenciales inválidas (`401`).

### 3.3 Postman (`/postman`)

1. Abre Postman e importa ambos archivos:
   - `PlaylistManager.postman_collection.json`
   - `PlaylistManager.postman_environment.json`
2. Selecciona el entorno **Playlist Manager - Local**.
3. Con el backend arrancado, ejecuta las peticiones. La colección valida automáticamente estados (`201`, `400`, `200`, `404`, `204`) y el header `Location`.

### 3.4 Ejercicio 1 Refactor (`/ejercicio-1-refactor`)

Compilar y ejecutar:

```bash
cd ejercicio-1-refactor
javac *.java
java Equipo
# o bien
java Main
```

Ambos puntos de entrada producen la misma salida por consola. La solución aplica SOLID (SRP/OCP), inmutabilidad (`List.copyOf`, `record Jugador`) y validación de invariantes de dominio en el constructor de `Equipo` (`titulos >= 0`, control de nulos/blancos).

---

## 4. Autoría IA

- **Asistente utilizado**: **Innti** (en VS Code / Kilo Code), operando bajo dirección técnica incremental por fases.
- **Dirección Humana, Arquitectura y Auditoría (~20% escritura/ajuste directo + 100% diseño y verificación)**:
  - Definición de la arquitectura del monorepo, contratos de capas, invariantes de dominio y reglas de negocio.
  - Resolución íntegra de la evaluación teórica (`Teorica-Staff-III.md`) y redacción crítica de la bitácora (`AI-LOG.md`).
  - **Ejercicio 1 (Refactor POO)**: Revisión crítica de la primera propuesta de la IA, rechazando el uso de `record` sin validaciones de estado para `Equipo` y exigiendo encapsulamiento POO con validación de invariantes en el constructor (`titulos >= 0`, control de nulos/blancos) y retrocompatibilidad del punto de entrada `Equipo.main`.
  - **Ejercicio 2 (Testing y Cobertura)**: Auditoría de la suite inicial generada por la IA, detectando la desactivación de filtros de seguridad (`addFilters = false`) y la falta de casos borde; dirección y especificación de +14 pruebas adicionales (`SecurityIntegrationTest` con filtros activos, manejo de duplicados en controlador, decodificación de `%20` en ruta, `canciones = null`, y endpoints `GET /lists` y `DELETE /lists/{listName}`).
  - **Ejercicio 3 (Seguridad H2)**: Evaluación de riesgos de seguridad en la configuración sugerida para H2 (descarte de `spring.h2.console.enabled=true` en el perfil base, externalización mediante variables de entorno y hashing de credenciales con `BCryptPasswordEncoder`).
- **Generación Asistida por IA (~80% implementación de código y scaffolding)**:
  - Generación guiada del scaffolding de Spring Boot (`/backend`: entidades JPA, repositorio, DTOs, mapper, servicio, controlador, `SecurityConfig` y `GlobalExceptionHandler`).
  - Generación de la suite base de pruebas unitarias y codificación de los escenarios de prueba adicionales especificados tras la auditoría.
  - Generación de la colección y entorno de Postman v2.1 (`/postman`) con scripts de validación.
  - Implementación de los componentes Standalone, formularios reactivos (`FormArray`) e interceptores HTTP en Angular 17 (`/frontend`).
