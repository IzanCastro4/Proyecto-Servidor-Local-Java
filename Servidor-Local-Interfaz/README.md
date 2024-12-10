# Sistema de Biblioteca Distribuida

Sistema cliente/servidor para gestión de biblioteca utilizando Java RMI.

## Requisitos

- Java 11 o superior
- Maven 3.6.0 o superior
- Puerto 1099 disponible para RMI

## Tecnologías

- Java RMI
- Swing con FlatLaf
- Maven
- SLF4J y Logback

## Instalación

```bash
git clone [url-repositorio]
cd sistema-biblioteca
mvn clean package
```

## Configuración

Editar `src/main/resources/config.properties`:

```properties
puerto=1099
host=localhost
ruta.archivo=data/biblioteca.txt
```

## Ejecución

### Servidor
```bash
java -jar target/servidor-jar-with-dependencies.jar
```

### Cliente
```bash
java -jar target/cliente-jar-with-dependencies.jar
```

## Funcionalidades

- Gestión CRUD de libros
- Búsqueda por título, autor y categoría
- Interfaz gráfica moderna
- Reconexión automática
- Logs detallados

## Estructura del Proyecto

```
sistema-biblioteca/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/biblioteca/
│       │       ├── modelo/
│       │       ├── vista/
│       │       ├── controlador/
│       │       ├── dao/
│       │       ├── interfaces/
│       │       └── util/
│       └── resources/
└── data/
```

## Arquitectura

- **Modelo**: Representa los libros y datos
- **Vista**: Interfaces gráficas cliente/servidor
- **Controlador**: Lógica de negocio y comunicación
- **DAO**: Acceso a datos en archivo txt
- **RMI**: Comunicación distribuida

## Uso

1. Iniciar servidor
2. Conectar cliente
3. Realizar operaciones:
   - Agregar libros
   - Buscar por criterios
   - Actualizar información
   - Eliminar registros

## Desarrollo

```bash
# Compilar
mvn compile

# Ejecutar tests
mvn test

# Generar documentación
mvn javadoc:javadoc
```

## Características

- Interfaz moderna con FlatLaf
- Persistencia en archivo txt
- Manejo de errores robusto
- Logging detallado
- Operaciones concurrentes seguras

## Contacto

[Tu nombre]
[Tu email]

## Licencia

MIT License
