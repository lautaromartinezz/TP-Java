# TP-Java

## Requisitos

- **JDK 21** — [Descargar Temurin JDK 21 x64 MSI](https://adoptium.net/temurin/releases/?version=21)
- **Apache Tomcat 10.0** — Servidor de aplicaciones

## Docker Setup

```bash
docker run --name javaDB \
  -v /c/Users/TuUsuario/docker-volumes/javaDB:/var/lib/mysql \
  -e MYSQL_ROOT_HOST='%' \
  -e MYSQL_ALLOW_EMPTY_PASSWORD="yes" \
  -e MYSQL_PASSWORD="tpjava" \
  -e MYSQL_USER="tpjava" \
  -e MYSQL_DATABASE='javaDB' \
  -p 3307:3306 \
  -d percona/percona-server
```

Luego ejecutar los archivos `.sql` de la carpeta `db/` para inicializar la base de datos.

## Configuración en Eclipse

1. **Importar el proyecto:** `File > Import > Existing Maven Projects` y seleccionar la carpeta raíz.
2. **Actualizar dependencias:** Click derecho sobre el proyecto `Maven > Update Project`.
3. **Configurar Tomcat 10:** `Window > Preferences > Server > Runtime Environments > Add > Apache Tomcat v10.0`.
4. **Ejecutar:** Click derecho sobre el proyecto `Run As > Run on Server` y seleccionar Tomcat 10.

## Arquitectura

Aplicación web Java (Jakarta EE / Servlet API 6) empaquetada como `.war` y desplegada en Tomcat 10.
Sigue una arquitectura en capas: la vista (JSP) se comunica con los `servlet`, estos con los
`controller` (lógica de negocio) y estos últimos con la capa `data` (acceso a MySQL vía JDBC).

```
vista (JSP)  ->  servlet (HTTP)  ->  controller (negocio)  ->  data (JDBC)  ->  MySQL
```

### Estructura del proyecto

```
TP-Java/
├── db/                          # Scripts SQL
│   ├── init.sql                 # Creación de esquema y tablas
│   └── dump.sql                 # Volcado de datos
├── src/main/java/
│   ├── entities/                # Modelos de dominio (Monster, Puerta, Puesto, Resultado, ...)
│   ├── data/                    # Capa de acceso a datos (JDBC + DbConnector)
│   ├── controller/              # Lógica de negocio (una por entidad)
│   ├── servlet/                 # Controladores HTTP (rutas de la app)
│   └── util/                    # Utilidades (HtmlUtil: escape de HTML/JS)
├── src/main/webapp/
│   ├── css/app.css              # Estilos de la aplicación
│   ├── index.jsp                # Punto de entrada (redirige a /dashboard)
│   ├── monster/                 # Vistas de monstruos (list, form, ranking)
│   ├── puerta/                  # Vistas de puertas (list, form)
│   ├── puesto/                  # Vistas de puestos (list, form)
│   ├── puerta-asignada/         # Vistas de puertas asignadas (list, form)
│   ├── puesto-asignado/         # Vistas de puestos asignados (list, form)
│   ├── resultado/               # Vistas de resultados (list, form)
│   └── WEB-INF/                 # Recursos no accesibles directamente
│       ├── dashboard.jsp        # Panel de control con métricas
│       └── includes/            # Fragmentos compartidos (header, footer)
└── pom.xml                      # Configuración Maven (dependencias y plugins)
```

### Capas

| Capa | Paquete / ruta | Responsabilidad |
|------|----------------|-----------------|
| Vista | `src/main/webapp/**/*.jsp` | Renderizado HTML de listados y formularios |
| Servlet | `src/main/java/servlet` | Manejo de HTTP (doGet/doPost/doPut/doDelete) y ruteo |
| Controller | `src/main/java/controller` | Lógica de negocio y validaciones |
| Data | `src/main/java/data` | Acceso a datos con JDBC |
| Entities | `src/main/java/entities` | Objetos de dominio |
| Util | `src/main/java/util` | Helpers transversales (escape HTML/JS) |

### Rutas HTTP

| Ruta | Entidad | Métodos |
|------|---------|---------|
| `/dashboard` | Panel de control (métricas) | GET |
| `/monster` | Monstruos (+ ranking) | GET, POST, PUT, DELETE |
| `/puerta` | Puertas | GET, POST, PUT, DELETE |
| `/puesto` | Puestos | GET, POST, PUT, DELETE |
| `/puesto-asignado` | Asignación monstruo → puesto | GET, POST, PUT, DELETE |
| `/puerta-asignada` | Asignación puerta → puesto | GET, POST, PUT, DELETE |
| `/resultado` | Resultados de energía | GET, POST, PUT, DELETE |

## TODO / Pendientes

### Autenticación y Autorización

- [ ] Login/Logout con manejo de sesiones
- [ ] Tabla `usuario` (username, password hasheada con bcrypt, rol)
- [ ] `AuthFilter` para proteger rutas
- [ ] Autorización por rol en cada servlet
- [ ] Navegación según rol (mostrar/ocultar opciones del menú)

### Roles

- [ ] **Administrador** (super admin): acceso a todos los listados y ABM de todos los CRUDs
- [ ] **Operador**: asigna monstruos a puestos (`puesto-asignado`) y puertas a puestos (`puerta-asignada`)
- [ ] **Asustador**: ve únicamente sus propias asignaciones (puestos/puertas) y los rankings

### Home / Landing

- [ ] Página de inicio real (hoy `index.jsp` solo redirige a `/dashboard`)
- [ ] Bienvenida + resumen según el rol del usuario

### Modales para ABM

- [ ] Modal de confirmación de borrado (reemplazar `confirm()` nativo)
- [ ] Modales de alta/edición (hoy se usan páginas `form.jsp`)
- [ ] Modal de feedback (éxito/error)

### Casos de uso faltantes

- [ ] Listado complejo: Resultados por rango de fechas
- [ ] CU nivel resumen: ranking jerárquico de monstruos por energía (hoy el ranking es plano)
- [ ] Listado simple de monstruos por energía generada (cubierto por el ranking)

### Documentación de endpoints

- [ ] Documentar cada servlet/ruta, método HTTP y parámetros
