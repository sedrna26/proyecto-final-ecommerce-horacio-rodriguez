# 🛒 Sistema E-commerce con Autenticación - Proyecto Final

## 📋 Descripción del Proyecto

Sistema completo de comercio electrónico desarrollado con **Spring Boot 3.2.0 (Java 17)** para el backend y **HTML/CSS/JavaScript** para el frontend. Implementa una API RESTful completa que gestiona productos, usuarios y pedidos con **sistema de autenticación y control de acceso basado en roles** (ADMIN/USUARIO), demostrando conceptos avanzados de programación orientada a objetos, manejo de excepciones, validaciones, seguridad y arquitectura de software.

---

## ✨ Características Principales

### 🔐 Sistema de Autenticación y Autorización
- ✅ **Login y Registro de usuarios** con validación de credenciales
- ✅ **Control de acceso basado en roles** (ADMIN y USUARIO)
- ✅ **Gestión de sesiones** con localStorage
- ✅ **Rutas protegidas** según rol del usuario
- ✅ **Interfaz adaptativa** que muestra funcionalidades según permisos

### 👥 Roles y Permisos

#### **ADMIN** (Administrador)
- ✅ **CRUD completo de productos** (crear, editar, eliminar)
- ✅ **Ver todos los pedidos** del sistema
- ✅ **Gestión de inventario** y control de stock
- ✅ **Acceso a modo administración** desde cualquier página

#### **USUARIO** (Comprador)
- ✅ **Realizar pedidos** y comprar productos
- ✅ **Ver historial propio** de pedidos
- ✅ **Gestión de carrito** de compras
- ✅ **Actualización automática de stock** al confirmar pedido

### Backend (Spring Boot + MySQL)
- ✅ **API RESTful completa** con endpoints para productos, pedidos, usuarios y autenticación
- ✅ **Persistencia de datos** con JPA/Hibernate y MySQL (MariaDB 10.4.32)
- ✅ **Validaciones robustas** con Jakarta Validation
- ✅ **Manejo de excepciones** personalizado y global
- ✅ **Herencia y Polimorfismo** (clases Producto, Bebida, Comida)
- ✅ **Control de stock** en tiempo real con actualización automática
- ✅ **Organización modular** por paquetes (productos, pedidos, usuarios, auth)
- ✅ **CORS configurado** para integración frontend-backend

### Frontend (HTML/CSS/JavaScript)
- 🔐 **Sistema de autenticación** (login.html, register.html)
- 🛒 **Carrito de compras** persistente (localStorage)
- 👤 **Perfil de usuario** con información de sesión
- 🔒 **Protección de rutas** según rol
- 📱 **Diseño responsivo** adaptable a móviles y tablets
- 🔄 **Integración con API** mediante Fetch con manejo de autenticación
- ✅ **Validación de stock** antes de comprar
- 📦 **Gestión de pedidos** con historial filtrado por usuario
- 🎨 **Interfaz moderna** e intuitiva con navegación dinámica
- ⚡ **Actualización dinámica** de UI según estado de autenticación

---

## 🏗️ Arquitectura del Sistema

```
proyecto-final-ecommerce-horacio-rodriguez/
│
├── backend/                           # API REST Spring Boot
│   ├── src/main/java/com/techlab/
│   │   ├── productos/                 # Gestión de productos
│   │   │   ├── Producto.java         # Entidad base (herencia)
│   │   │   ├── Bebida.java           # Especialización producto
│   │   │   ├── Comida.java           # Especialización producto
│   │   │   ├── ProductoRepository.java
│   │   │   ├── ProductoService.java
│   │   │   └── ProductoController.java
│   │   │
│   │   ├── pedidos/                   # Gestión de pedidos
│   │   │   ├── Pedido.java
│   │   │   ├── LineaPedido.java
│   │   │   ├── EstadoPedido.java     # Enum estados
│   │   │   ├── PedidoRepository.java
│   │   │   ├── PedidoService.java
│   │   │   └── PedidoController.java
│   │   │
│   │   ├── usuarios/                  # Gestión de usuarios y auth
│   │   │   ├── Usuario.java          # Entidad con rol y password
│   │   │   ├── RolUsuario.java       # Enum (ADMIN/USUARIO)
│   │   │   ├── UsuarioRepository.java
│   │   │   ├── UsuarioService.java
│   │   │   ├── UsuarioController.java
│   │   │   └── AuthController.java   # Login/Register/Logout
│   │   │
│   │   ├── excepciones/               # Excepciones personalizadas
│   │   │   ├── RecursoNoEncontradoException.java
│   │   │   ├── StockInsuficienteException.java
│   │   │   ├── ValidacionException.java
│   │   │   └── ManejadorExcepcionesGlobal.java
│   │   │
│   │   ├── config/                    # Configuración
│   │   │   └── WebConfig.java        # CORS y configuración web
│   │   │
│   │   └── EcommerceApplication.java  # Main Spring Boot
│   │
│   ├── src/main/resources/
│   │   └── application.properties     # Config DB y servidor
│   │
│   ├── actualizar_bd_autenticacion.sql # Script SQL para auth
│   ├── pom.xml                        # Dependencias Maven
│   └── README.md
│
├── frontend/                          # Aplicación web cliente
│   ├── index.html                     # Página principal
│   ├── productos.html                 # Catálogo de productos
│   ├── carrito.html                   # Carrito de compras
│   ├── pedidos.html                   # Historial de pedidos
│   ├── login.html                     # 🔐 Inicio de sesión
│   ├── register.html                  # 🔐 Registro de usuarios
│   ├── admin.html                     # 👨‍💼 Panel administrador
│   │
│   ├── css/
│   │   └── style.css                  # Estilos globales
│   │
│   ├── js/
│   │   ├── main.js                    # Lógica principal + auth
│   │   ├── auth.js                    # 🔐 Módulo autenticación
│   │   ├── carrito.js                 # Gestión carrito
│   │   ├── pedidos.js                 # Gestión pedidos
│   │   ├── admin.js                   # Panel admin (CRUD)
│   │   ├── index.js                   # Página inicio
│   │   └── slider.js                  # Carrusel imágenes
│   │
│   └── img/                           # Recursos gráficos
│       └── slider/
│
└── README.md                          # Este archivo
```

---

## 🎯 Conceptos Técnicos Implementados

### 1. ✅ Tipos de Datos y Variables
- **Primitivos**: `int`, `double`, `boolean`, `long`
- **Referencia**: `String`, `LocalDateTime`, `BigDecimal`
- Variables de instancia, locales y parámetros

### 2. ✅ Operadores
- **Aritméticos**: `+`, `-`, `*`, `/` (cálculos de totales y precios)
- **Relacionales**: `<`, `>`, `<=`, `>=`, `==`, `!=` (validaciones de stock, comparaciones)
- **Lógicos**: `&&`, `||`, `!` (condiciones compuestas en autenticación y permisos)
- **Asignación**: `=`, `+=`, `-=` (actualización de stock)

### 3. ✅ Colecciones (Java Collections Framework)
- `List<Producto>`: lista de productos activos
- `List<LineaPedido>`: líneas de pedido con productos y cantidades
- `List<Pedido>`: historial de pedidos por usuario
- `Map<String, Object>`: respuestas JSON dinámicas
- `Set<>`: eliminación de duplicados en consultas

### 4. ✅ POO y Colaboración de Clases
- **Entidades JPA**: `Producto`, `Usuario`, `Pedido`, `LineaPedido`
- **Servicios de negocio**: `ProductoService`, `PedidoService`, `UsuarioService`
- **Controladores REST**: `ProductoController`, `PedidoController`, `UsuarioController`, `AuthController`
- **Repositorios JPA**: `ProductoRepository`, `PedidoRepository`, `UsuarioRepository`
- **DTOs implícitos**: Transferencia de datos vía JSON
- **Patrón MVC**: Separación de responsabilidades (Modelo-Vista-Controlador)

### 5. ✅ Herencia y Polimorfismo
```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_producto")
public abstract class Producto {
    // Atributos comunes
}

@Entity
@DiscriminatorValue("BEBIDA")
public class Bebida extends Producto {
    private Double volumenLitros;
    private String tipoBebida;
    private Boolean tieneGas;
}

@Entity
@DiscriminatorValue("COMIDA")
public class Comida extends Producto {
    private LocalDate fechaVencimiento;
    private Double pesoKg;
    private Boolean requiereRefrigeracion;
}
```
**Polimorfismo en acción**: Un `List<Producto>` puede contener `Bebida` y `Comida`

### 6. ✅ Excepciones y Manejo de Errores
- **Personalizadas**:
  - `StockInsuficienteException`: validación de inventario
  - `RecursoNoEncontradoException`: entidades no encontradas (404)
  - `ValidacionException`: errores de validación de datos
  - `CredencialesInvalidasException`: login fallido
- **Estándar Java**: `NumberFormatException`, `IllegalArgumentException`
- **Manejo global**: `@RestControllerAdvice` con `@ExceptionHandler`
- **Try-catch**: En controladores y servicios para operaciones críticas

### 7. ✅ Paquetes/Módulos (Organización)
```
com.techlab
├── productos       # Módulo de productos
├── pedidos         # Módulo de pedidos
├── usuarios        # Módulo de usuarios y autenticación
├── excepciones     # Excepciones personalizadas
└── config          # Configuración (CORS, Web)
```

### 8. ✅ Enumeraciones (Enum)
```java
public enum EstadoPedido {
    PENDIENTE, PROCESANDO, ENVIADO, ENTREGADO, CANCELADO
}

public enum RolUsuario {
    ADMIN, USUARIO
}
```

### 9. ✅ Validaciones (Jakarta Validation)
```java
@NotBlank(message = "El nombre es obligatorio")
private String nombre;

@Min(value = 0, message = "El precio no puede ser negativo")
private Double precio;

@Email(message = "Email debe ser válido")
private String email;
```

### 10. ✅ Persistencia de Datos (JPA/Hibernate)
- **Anotaciones**: `@Entity`, `@Id`, `@GeneratedValue`, `@Column`, `@ManyToOne`, `@OneToMany`
- **Relaciones**: 
  - Pedido → Usuario (ManyToOne)
  - Pedido → LineaPedido (OneToMany)
  - LineaPedido → Producto (ManyToOne)
- **Herencia JPA**: `@Inheritance`, `@DiscriminatorColumn`
- **Consultas JPQL**: Métodos de consulta derivados en repositorios

---

## 🔌 API REST Endpoints

### 🔐 Autenticación (AuthController)
| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|---------|
| POST | `/api/auth/login` | Iniciar sesión (retorna usuario con rol) | Público |
| POST | `/api/auth/register` | Registrar nuevo usuario | Público |
| GET | `/api/auth/verify/{userId}` | Verificar usuario por ID | Público |
| POST | `/api/auth/logout` | Cerrar sesión | Autenticado |

**Request Login**:
```json
{
  "email": "juan.perez@email.com",
  "password": "pass123"
}
```

**Response Login (200 OK)**:
```json
{
  "id": 1,
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan.perez@email.com",
  "rol": "ADMIN",
  "telefono": "555-1234",
  "direccion": "Calle Principal 123",
  "activo": true,
  "fechaRegistro": "2024-01-15T10:30:00"
}
```

### 📦 Productos (ProductoController)
| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|---------|
| GET | `/api/productos` | Listar productos activos | Público |
| GET | `/api/productos/{id}` | Obtener producto por ID | Público |
| GET | `/api/productos/mas-vendidos` | Top 3 productos más vendidos | Público |
| POST | `/api/productos` | Crear nuevo producto | **ADMIN** |
| PUT | `/api/productos/{id}` | Actualizar producto | **ADMIN** |
| DELETE | `/api/productos/{id}` | Eliminar (desactivar) producto | **ADMIN** |

**Request POST/PUT Producto**:
```json
{
  "nombre": "Laptop Dell XPS 15",
  "descripcion": "Laptop profesional",
  "precio": 1299.99,
  "stock": 15,
  "categoria": "Electrónica",
  "urlImagen": "https://..."
}
```

### 🛒 Pedidos (PedidoController)
| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|---------|
| GET | `/api/pedidos` | Listar **todos** los pedidos | **ADMIN** |
| GET | `/api/pedidos/usuario/{id}` | Pedidos de un usuario específico | Autenticado |
| POST | `/api/pedidos` | Crear nuevo pedido (disminuye stock) | Autenticado |
| POST | `/api/pedidos/{id}/cancelar` | Cancelar pedido | Autenticado |

**Request POST Pedido**:
```json
{
  "usuarioId": 1,
  "observaciones": "Entregar en horario de oficina",
  "lineasPedido": [
    {
      "productoId": 44,
      "cantidad": 1,
      "precioUnitario": 1299.99
    },
    {
      "productoId": 45,
      "cantidad": 2,
      "precioUnitario": 99.99
    }
  ]
}
```

**Response POST Pedido (201 Created)**:
```json
{
  "id": 4,
  "usuario": "Juan Pérez",
  "fechaPedido": "2025-12-12T18:38:01",
  "estado": "PENDIENTE",
  "total": 1499.97,
  "observaciones": "Entregar en horario de oficina",
  "cantidadProductos": 2,
  "lineasPedido": [...]
}
```

### 👥 Usuarios (UsuarioController)
| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|---------|
| GET | `/api/usuarios` | Listar todos los usuarios | **ADMIN** |
| GET | `/api/usuarios/{id}` | Obtener usuario por ID | Autenticado |
| POST | `/api/usuarios` | Crear nuevo usuario | **ADMIN** |
| PUT | `/api/usuarios/{id}` | Actualizar usuario | Autenticado |
| DELETE | `/api/usuarios/{id}` | Desactivar usuario | **ADMIN** |

---

## 🚀 Instalación y Ejecución

### 📋 Prerequisitos
- ☕ **Java 17** o superior ([Descargar JDK](https://www.oracle.com/java/technologies/javase-downloads.html))
- 📦 **Maven 3.6+** ([Descargar Maven](https://maven.apache.org/download.cgi))
- 🗄️ **MySQL 8.0** o **MariaDB 10.4+** ([Descargar MySQL](https://dev.mysql.com/downloads/))
- 🌐 **Navegador web** moderno (Chrome, Firefox, Edge)
- 💻 **Visual Studio Code** (recomendado con Live Server extension)

---

### ⚙️ Configuración del Backend

#### 1️⃣ Crear base de datos
Ejecutar en MySQL/MariaDB:
```sql
CREATE DATABASE ecommerce_db;
USE ecommerce_db;
```

#### 2️⃣ Ejecutar script de tablas y autenticación
```bash
mysql -u root -p ecommerce_db < backend/actualizar_bd_autenticacion.sql
```

Este script crea:
- ✅ Tabla `productos` (con herencia de Bebida y Comida)
- ✅ Tabla `usuarios` (con campos `password` y `rol`)
- ✅ Tabla `pedidos` y `lineas_pedido`
- ✅ Usuarios de prueba con roles ADMIN y USUARIO

#### 3️⃣ Configurar credenciales de la BD
Editar `backend/src/main/resources/application.properties`:

```properties
# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD_AQUI

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Puerto del servidor
server.port=8080
```

#### 4️⃣ Compilar y ejecutar backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

✅ **Backend corriendo en**: `http://localhost:8080`

---

### 🌐 Configuración del Frontend

#### 1️⃣ Verificar configuración de API
Abrir `js/main.js` y confirmar:
```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

#### 2️⃣ Iniciar frontend

**Opción A - Live Server (VS Code)**:
1. Instalar extensión "Live Server" en VS Code
2. Click derecho en `index.html` → "Open with Live Server"
3. Se abre automáticamente en `http://localhost:5500`

**Opción B - Abrir directamente**:
1. Doble click en `index.html`
2. Se abre en navegador predeterminado

✅ **Frontend corriendo en**: `http://localhost:5500` (o puerto de Live Server)

---

## 🔐 Usuarios de Prueba

El sistema incluye usuarios precargados para testing:

### 👨‍💼 Usuario Administrador
```
Email: juan.perez@email.com
Password: pass123
Rol: ADMIN
```
**Permisos**: CRUD de productos, ver todos los pedidos, gestión completa

### 👨‍💼 Usuario Administrador 2
```
Email: admin@ecommerce.com
Password: admin123
Rol: ADMIN
```

### 👤 Usuario Comprador
```
Email: maria.gonzalez@email.com
Password: maria456
Rol: USUARIO
```
**Permisos**: Realizar pedidos, ver historial propio

### 👤 Usuario Comprador 2
```
Email: carlos.rodriguez@email.com
Password: carlos789
Rol: USUARIO
```

---

## 🎯 Flujo de Uso del Sistema

### Para Usuarios Compradores (USUARIO)

1. **🏠 Página de Inicio** (`index.html`)
   - Ver catálogo de productos
   - Click en "Iniciar Sesión" en la navegación

2. **🔐 Login** (`login.html`)
   - Ingresar email y password
   - Click "Iniciar Sesión"
   - Redirección automática a inicio

3. **🛍️ Explorar Productos** (`productos.html`)
   - Ver todos los productos disponibles
   - Filtrar por categoría
   - Verificar stock disponible

4. **🛒 Agregar al Carrito**
   - Click en "Agregar al Carrito"
   - Ver contador de carrito actualizado
   - Continuar comprando o ir al carrito

5. **💳 Confirmar Pedido** (`carrito.html`)
   - Revisar productos seleccionados
   - Ajustar cantidades si es necesario
   - Click "Confirmar Pedido"
   - Stock se reduce automáticamente

6. **📦 Ver Historial** (`pedidos.html`)
   - Ver solo pedidos propios
   - Ver estado: PENDIENTE, PROCESANDO, ENVIADO, ENTREGADO

### Para Administradores (ADMIN)

1. **🔐 Login como Admin**
   - Usar credenciales de admin
   - Ver botón "Modo Admin" en navegación

2. **👨‍💼 Panel de Administración** (`admin.html`)
   - Ver todos los productos
   - **Crear**: Click "Agregar Producto" → Llenar formulario
   - **Editar**: Click "Editar" en producto → Modificar datos
   - **Eliminar**: Click "Eliminar" → Confirmar (desactiva producto)

3. **📊 Gestión de Pedidos** (`pedidos.html`)
   - Ver **todos** los pedidos del sistema
   - Filtrar por estado
   - Ver detalles de cada pedido con usuario

4. **📈 Análisis**
   - Ver productos más vendidos
   - Monitorear stock bajo
   - Gestionar inventario

---

## 📊 Ejemplos de Peticiones API

### 🔐 Autenticación

**Login**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan.perez@email.com",
    "password": "pass123"
  }'
```

**Registro**:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Carlos",
    "apellido": "López",
    "email": "carlos@example.com",
    "password": "carlos123",
    "telefono": "555-9999",
    "direccion": "Av. Libertador 456"
  }'
```

### 📦 Productos

**Listar productos activos**:
```bash
curl http://localhost:8080/api/productos
```

**Crear producto (ADMIN)**:
```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Mouse Gamer RGB",
    "descripcion": "Mouse óptico con 7 botones programables",
    "precio": 49.99,
    "stock": 30,
    "categoria": "Accesorios",
    "urlImagen": "https://example.com/mouse.jpg"
  }'
```

**Actualizar producto (ADMIN)**:
```bash
curl -X PUT http://localhost:8080/api/productos/45 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Mouse Logitech MX Master 3",
    "precio": 89.99,
    "stock": 25
  }'
```

### 🛒 Pedidos

**Crear pedido**:
```bash
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 1,
    "observaciones": "Entregar en horario de oficina",
    "lineasPedido": [
      {
        "productoId": 44,
        "cantidad": 1,
        "precioUnitario": 1299.99
      }
    ]
  }'
```

**Listar pedidos de usuario**:
```bash
curl http://localhost:8080/api/pedidos/usuario/1
```

**Listar todos los pedidos (ADMIN)**:
```bash
curl http://localhost:8080/api/pedidos
```

---

## 🛠️ Tecnologías Utilizadas

### Backend
- ☕ **Java 17** - Lenguaje de programación
- 🍃 **Spring Boot 3.2.0** - Framework principal
- 🗄️ **Spring Data JPA** - Capa de persistencia
- 🔧 **Hibernate** - ORM (Object-Relational Mapping)
- 🗃️ **MySQL/MariaDB** - Base de datos relacional
- ✅ **Jakarta Validation** - Validación de datos
- 📦 **Maven** - Gestión de dependencias

### Frontend
- 🌐 **HTML5** - Estructura de páginas
- 🎨 **CSS3** - Estilos y diseño responsivo
- ⚡ **JavaScript (ES6+)** - Lógica del cliente (Vanilla JS)
- 🔄 **Fetch API** - Comunicación con backend
- 💾 **localStorage** - Persistencia de carrito y sesión

### Herramientas de Desarrollo
- 💻 **Visual Studio Code** - IDE principal
- 🔴 **Live Server** - Servidor de desarrollo frontend
- 📮 **Postman** - Testing de API REST
- 🐬 **MySQL Workbench** - Administración de BD
- 🔍 **Chrome DevTools** - Debugging frontend

---

## 📚 Documentación Adicional

### Estructura de la Base de Datos

**Tabla: usuarios**
```sql
- id (PK, AUTO_INCREMENT)
- nombre VARCHAR(100)
- apellido VARCHAR(100)
- email VARCHAR(255) UNIQUE
- password VARCHAR(255)
- rol ENUM('ADMIN', 'USUARIO')
- telefono VARCHAR(20)
- direccion VARCHAR(255)
- activo BOOLEAN DEFAULT TRUE
- fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
```

**Tabla: productos** (Herencia: Bebida, Comida)
```sql
- id (PK, AUTO_INCREMENT)
- tipo_producto VARCHAR(31) [BEBIDA, COMIDA]
- nombre VARCHAR(255)
- descripcion TEXT
- precio DECIMAL(10,2)
- stock INT
- categoria VARCHAR(100)
- url_imagen VARCHAR(500)
- activo BOOLEAN DEFAULT TRUE
-- Campos de Bebida:
- volumen_litros DOUBLE
- tipo_bebida VARCHAR(50)
- tiene_gas BOOLEAN
-- Campos de Comida:
- fecha_vencimiento DATE
- peso_kg DOUBLE
- requiere_refrigeracion BOOLEAN
```

**Tabla: pedidos**
```sql
- id (PK, AUTO_INCREMENT)
- usuario_id (FK → usuarios)
- fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP
- estado ENUM('PENDIENTE', 'PROCESANDO', 'ENVIADO', 'ENTREGADO', 'CANCELADO')
- total DECIMAL(10,2)
- observaciones TEXT
```

**Tabla: lineas_pedido**
```sql
- id (PK, AUTO_INCREMENT)
- pedido_id (FK → pedidos)
- producto_id (FK → productos)
- cantidad INT
- precio_unitario DECIMAL(10,2)
```

---

## 🐛 Solución de Problemas Comunes

### ❌ Error: "Connection refused" en frontend
**Solución**: Verificar que el backend esté corriendo en `http://localhost:8080`
```bash
cd backend
mvn spring-boot:run
```

### ❌ Error: "Access denied for user 'root'@'localhost'"
**Solución**: Verificar credenciales en `application.properties` y permisos de MySQL

### ❌ Error: "Port 8080 already in use"
**Solución**: Matar proceso en puerto 8080
```bash
# Windows (PowerShell)
Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess | Stop-Process -Force

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

### ❌ No se cargan los productos en el frontend
**Solución**: 
1. Verificar que el backend esté corriendo
2. Abrir consola del navegador (F12) y revisar errores
3. Verificar configuración de CORS en `WebConfig.java`
4. Confirmar que `API_BASE_URL` en `main.js` apunte a `http://localhost:8080/api`

### ❌ Error al crear pedido: "Stock insuficiente"
**Solución**: 
1. Verificar stock disponible en la BD
2. Reducir cantidad en el carrito
3. Como admin, aumentar stock del producto

---

## 🤝 Contribuciones

Este es un proyecto educativo desarrollado para el curso de **Desarrollo de Software II**.

### Autor
**Horacio Rodriguez**  
Instituto de Educación Superior  
Diciembre 2025

---

## 📄 Licencia

Este proyecto fue desarrollado con fines educativos para el curso de Back-End / Java - Talento Tech.

---

## 📞 Contacto y Soporte

Para consultas sobre el proyecto:
- 📧 sedrna26@gmail.com
- 🧑 Horacio Andres Rodriguez
- 📚 Curso: Back-End / Java - Talento Tech

---

## 🎓 Conceptos Académicos Demostrados

Este proyecto implementa y demuestra los siguientes conceptos del curso:

✅ **Programación Orientada a Objetos**
- Clases, objetos, atributos y métodos
- Encapsulamiento con modificadores de acceso
- Herencia (Producto → Bebida/Comida)
- Polimorfismo (List<Producto> con diferentes tipos)
- Abstracción con interfaces y clases abstractas

✅ **Manejo de Excepciones**
- Try-catch-finally
- Excepciones personalizadas
- Propagación y manejo global de excepciones

✅ **Colecciones y Estructuras de Datos**
- List, Set, Map
- Iteración con foreach y streams
- Operaciones CRUD sobre colecciones

✅ **Persistencia de Datos**
- ORM con JPA/Hibernate
- Relaciones entre entidades (OneToMany, ManyToOne)
- Consultas JPQL y métodos derivados

✅ **Arquitectura de Software**
- Patrón MVC (Model-View-Controller)
- Separación en capas (Controller, Service, Repository)
- API RESTful con JSON
- CORS y configuración web

✅ **Autenticación y Seguridad**
- Sistema de login y registro
- Control de acceso basado en roles
- Validación de credenciales
- Gestión de sesiones

✅ **Desarrollo Full Stack**
- Backend con Spring Boot
- Frontend con JavaScript vanilla
- Integración frontend-backend con API REST
- Persistencia en navegador (localStorage)

---

**🎉 Sistema E-commerce completo con autenticación y control de acceso por roles**
