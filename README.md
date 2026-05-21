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
