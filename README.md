# Design Home 🛋️

## Descripción del Proyecto
Design Home es una aplicación web de e-commerce especializada en muebles y decoración, ofreciendo una experiencia de compra intuitiva y moderna.

## Características Principales
- 🏠 Navegación responsiva con menú móvil
- 🛒 Carrito de compras completamente funcional
- 📱 Diseño adaptable a diferentes dispositivos
- 🖼️ Catálogo de productos dinámico
- 📍 Múltiples páginas: Inicio, Productos, Contacto, Suscripciones

## Tecnologías Utilizadas
- HTML5
- CSS3
- JavaScript (Vanilla)
- API de productos

## Funcionalidades Técnicas de JavaScript

### Gestión del Carrito
- **Almacenamiento Local**: Utiliza `localStorage` para persistir el carrito de compras
- **Métodos de Carrito**:
  - `addToCart()`: Añade productos al carrito
  - `removerDelCarrito()`: Elimina productos del carrito
  - `actualizarCantidad()`: Modifica la cantidad de productos
  - `actualizarCarrito()`: Actualiza la interfaz y el estado del carrito

### Renderizado Dinámico
- Función `fetchProductos()`: Carga productos desde una API externa
- Función `renderizarProductos()`: Genera dinámicamente la vista de productos

### Interactividad del Menú
- Implementación de apertura/cierre de menú para dispositivos móviles
- Uso de `classList.add()` y `classList.remove()` para manejar la visibilidad

## Características de Implementación
- **Fetch API**: Consumo de API para obtener datos de productos
- **Eventos Dinámicos**: Gestión de eventos con `addEventListener()`
- **Manipulación del DOM**: Actualizaciones dinámicas sin recargar la página
- **Responsive Design**: Adaptación a diferentes tamaños de pantalla

## Páginas
1. **Inicio**: Slider de imágenes
2. **Productos**: Catálogo de productos con función de añadir al carrito
3. **Contacto**: Formulario de contacto con mapa integrado
4. **Suscripciones**: Formulario de newsletter

## Instalación
1. Clonar el repositorio
2. Abrir `index.html` en un navegador
3. No requiere instalación de dependencias (JavaScript Vanilla)

## Requisitos
- Navegador web moderno
- Conexión a internet para cargar productos desde la API

## Contribuciones
Las contribuciones son bienvenidas. Por favor, abrir un issue o enviar un pull request.

## Licencia
© 2024 Design Home - Todos los derechos reservados