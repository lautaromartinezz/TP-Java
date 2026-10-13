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

## Estructura del proyecto

```
src/main/java/
├── entities/       # Modelos de dominio
├── data/           # Capa de acceso a datos
├── controller/     # Lógica de negocio
└── servlet/        # Servlets HTTP
```

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
