# Willo Academy

Plataforma e-learning premium multirrol (Estudiante + Administrador).

## Stack Tecnológico

| Capa          | Tecnología                                   |
|---------------|----------------------------------------------|
| Backend       | Java 17 + Spring Boot 3.2.5                 |
| Vistas        | Thymeleaf + HTML5 + CSS3 (vanilla)          |
| Seguridad     | Spring Security (BCrypt, roles)              |
| Base de Datos | PostgreSQL (vía JdbcTemplate)                |
| Build         | Maven                                        |

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/willoacademy/
│   │   ├── WilloApplication.java           # Entry point
│   │   ├── config/                         # Spring Config
│   │   │   ├── SecurityConfig.java         #   Seguridad (rutas, roles, bcrypt)
│   │   │   └── WebConfig.java              #   ResourceHandlers estáticos
│   │   ├── core/
│   │   │   ├── persistence/
│   │   │   │   └── BaseRepository.java     #   CRUD genérico con JdbcTemplate
│   │   │   └── security/
│   │   │       ├── AuthGuard.java          #   Helper de autenticación
│   │   │       └── Role.java               #   Enum STUDENT / ADMIN
│   │   ├── shared/
│   │   │   ├── model/User.java             #   Modelo transversal
│   │   │   └── util/
│   │   │       ├── Constants.java
│   │   │       └── Validators.java
│   │   └── features/                       # Módulos por dominio
│   │       ├── auth/                       # Autenticación
│   │       │   ├── AuthController.java
│   │       │   ├── AuthService.java
│   │       │   ├── AuthRepository.java
│   │       │   └── dto/
│   │       │       ├── LoginRequest.java
│   │       │       └── RegisterRequest.java
│   │       ├── catalog/                    # Catálogo de cursos
│   │       │   ├── CatalogController.java
│   │       │   ├── CatalogService.java
│   │       │   ├── CatalogRepository.java
│   │       │   └── dto/
│   │       │       ├── CourseSummary.java
│   │       │       └── CourseDetail.java
│   │       ├── player/                     # Reproductor de lecciones
│   │       │   ├── PlayerController.java
│   │       │   ├── PlayerService.java
│   │       │   ├── PlayerRepository.java
│   │       │   └── dto/
│   │       │       ├── LessonView.java
│   │       │       └── ProgressRequest.java
│   │       └── admin/
│   │           ├── dashboard/              # Dashboard de analíticas
│   │           │   ├── DashboardController.java
│   │           │   ├── DashboardService.java
│   │           │   ├── DashboardRepository.java
│   │           │   └── dto/StatsResponse.java
│   │           └── content/                # Gestión de contenido
│   │               ├── ContentController.java
│   │               ├── ContentService.java
│   │               ├── ContentRepository.java
│   │               └── dto/CourseForm.java
│   │
│   └── resources/
│       ├── application.properties
│       ├── db/migration/
│       │   ├── V1__init.sql                # Schema (6 tablas, índices)
│       │   └── V2__seed.sql                # Datos iniciales
│       ├── static/
│       │   ├── css/app.css                 # Reset + variables + layout global
│       │   ├── auth/login.css
│       │   ├── catalog/list.css
│       │   ├── catalog/detail.css
│       │   ├── player/video.css
│       │   ├── admin/dashboard.css
│       │   └── admin/content-manager.css
│       └── templates/
│           ├── layouts/default.html        # Layout principal (header, sidebar, main)
│           ├── fragments/
│           │   ├── header.html
│           │   └── sidebar.html
│           ├── auth/
│           │   ├── login.html
│           │   └── register.html
│           ├── catalog/
│           │   ├── list.html
│           │   └── detail.html
│           ├── player/
│           │   └── video.html
│           └── admin/
│               ├── dashboard.html
│               └── content-manager.html
│
└── test/java/com/willoacademy/features/    # Tests por módulo
    ├── auth/
    ├── catalog/
    ├── player/
    └── admin/
```

## Arquitectura: MVC con Service Layer

```
HTTP Request
    │
    ▼
┌──────────────────┐
│  Controller      │  @Controller: recibe request, llama al Service,
│  (orquestación)  │  coloca datos en Model, retorna "layouts/default"
└──────┬───────────┘       con contentView="feature/mivista"
       │
       ▼
┌──────────────────┐
│  Service         │  @Service: lógica de negocio, validaciones,
│  (negocio)       │  llama al Repository
└──────┬───────────┘
       │
       ▼
┌──────────────────┐
│  Repository      │  @Repository: queries SQL con PreparedStatement
│  (persistencia)  │  (nunca concatenación de strings)
└──────┬───────────┘
       │
       ▼
┌──────────────────┐
│  PostgreSQL      │  JdbcTemplate con ? parametrizados
└──────────────────┘
       │
       ▼
┌──────────────────┐
│  Thymeleaf       │  Layout envuelve: header + sidebar + {contentView}
│  (vista)         │  + CSS específico del feature
└──────────────────┘
       │
       ▼
HTTP Response (HTML)
```

### Flujo de una petición (ej. catálogo)

```
GET /catalog?id=3
  → CatalogController.list()
    → model.addAttribute("contentView", "catalog/list")
    → model.addAttribute("courses", catalogService.search(...))
    → return "layouts/default"

Thymeleaf:
  → layouts/default.html
    → th:replace="fragments/header :: header"
    → th:replace="fragments/sidebar :: sidebar"
    → th:include="catalog/list"     ← contenido dinámico
```

## Principios Aplicados

### Single Responsibility (SRP)

Cada archivo cumple **una sola función**:

| Archivo            | Responsabilidad                          |
|--------------------|------------------------------------------|
| `*Controller.java` | Orquestar request/response, no contiene lógica de negocio |
| `*Service.java`    | Lógica de negocio, validaciones          |
| `*Repository.java` | Solo queries SQL parametrizadas          |
| `*.html`           | Template Thymeleaf (vista)               |
| `*.css`            | Estilos encapsulados del feature         |
| `dto/*.java`       | Objetos de transferencia (forma, respuesta) |

### Feature-Driven

El código se agrupa por **dominio de negocio** (`auth/`, `catalog/`, `player/`, `admin/`), no por tipo de archivo. Cada feature contiene su propio Controller, Service, Repository, DTOs, template HTML y CSS.

### Dependency Inversion

Los controladores reciben sus dependencias por **constructor injection** (Spring DI). Ningún controller instancia directamente un Service o Repository.

### Seguridad (Prevención de Inyección SQL)

Todas las consultas SQL usan **JdbcTemplate con `?` parametrizados**:

```java
// ✅ Correcto
jdbc.query("SELECT * FROM users WHERE email = ?", rowMapper(), email);

// ❌ Prohibido
jdbc.query("SELECT * FROM users WHERE email = '" + email + "'");
```

### Gestión de Roles

- `SecurityConfig.java` define reglas de acceso por ruta
- `Role.java` enum centraliza los roles (`STUDENT`, `ADMIN`)
- Las vistas condicionan UI según rol vía Thymeleaf + Spring Security:
  ```html
  <li sec:authorize="hasRole('ADMIN')">
    <a th:href="@{/admin/dashboard}">Dashboard</a>
  </li>
  ```

## Convenciones para Desarrolladores

### Nomenclatura de Archivos

- **Java**: `PascalCase` (ej. `CatalogController.java`)
- **HTML/CSS**: `kebab-case` (ej. `content-manager.html`, `dashboard.css`)
- **DTOs**: nombre descriptivo + Request/Response (ej. `LoginRequest.java`, `StatsResponse.java`)

### Reglas por Feature

1. **No importar archivos CSS** dentro de los templates HTML — todas las hojas de estilo se cargan globalmente desde `layouts/default.html`
2. **No concatenar strings en SQL** — siempre usar `?` del JdbcTemplate
3. **No instanciar servicios manualmente** — usar inyección por constructor
4. **No escribir lógica de negocio en controladores** — delegar a Service
5. **No escribir lógica de red o BD en vistas** — los templates solo muestran datos del Model

### Añadir un Nuevo Feature

1. Crear carpeta `src/main/java/com/willoacademy/features/mi-feature/`
2. Crear Controller, Service, Repository, DTOs
3. Crear template `src/main/resources/templates/mi-feature/vista.html` (solo HTML, sin `<html>/<head>/<body>`)
4. Crear CSS en `src/main/resources/static/mi-feature/vista.css`
5. Agregar `<link>` al CSS en `layouts/default.html`
6. Registrar rutas en `SecurityConfig.java` si requiere protección especial

## Ejecución Local

```bash
# 1. Levantar PostgreSQL (docker)
docker run -d --name willo-pg -e POSTGRES_DB=willoacademy \
  -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 postgres:16

# 2. Ejecutar migraciones (o copiar SQL a la BD)
#    Los archivos están en src/main/resources/db/migration/

# 3. Iniciar la app
./mvnw spring-boot:run

# 4. Abrir en navegador
open http://localhost:8080
```
