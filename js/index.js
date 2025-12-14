/**
 * Script para cargar contenido dinámico en index.html
 * - Carrusel con productos de la base de datos
 * - Sección "Más vendidos" con productos más pedidos
 */

// SVG para imagen de respaldo
const imagenPorDefecto = `data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='400' height='300' viewBox='0 0 400 300'%3E%3Crect fill='%23e0e0e0' width='400' height='300'/%3E%3Ctext x='50%25' y='50%25' dominant-baseline='middle' text-anchor='middle' font-family='Arial, sans-serif' font-size='18' fill='%23999'%3EImagen no disponible%3C/text%3E%3C/svg%3E`;

/**
 * Carga productos para el carrusel
 */
async function cargarCarrusel() {
    try {
        const response = await fetch(`${API_BASE_URL}/productos`);
        if (!response.ok) throw new Error('Error al cargar productos');

        const productos = await response.json();

        // Tomar los primeros 5 productos para el carrusel
        const productosCarrusel = productos.slice(0, 5);

        const slidesContainer = document.querySelector('.slides');
        slidesContainer.innerHTML = ''; // Limpiar slides existentes

        productosCarrusel.forEach(producto => {
            const slide = document.createElement('div');
            slide.className = 'slide';

            const img = document.createElement('img');
            img.src = producto.urlImagen || imagenPorDefecto;
            img.alt = producto.nombre;
            img.onerror = function () {
                this.onerror = null;
                this.src = imagenPorDefecto;
            };

            slide.appendChild(img);
            slidesContainer.appendChild(slide);
        });

        console.log('✅ Carrusel cargado con', productosCarrusel.length, 'productos');

        // Reiniciar el slider después de cargar nuevas imágenes
        if (typeof iniciarSlider === 'function') {
            setTimeout(() => {
                iniciarSlider();
                console.log('✅ Slider reiniciado');
            }, 100);
        }

    } catch (error) {
        console.error('Error al cargar el carrusel:', error);
        // Mantener slides por defecto si hay error
    }
}

/**
 * Carga los productos más vendidos
 */
async function cargarMasVendidos() {
    try {
        const response = await fetch(`${API_BASE_URL}/productos/mas-vendidos`);
        if (!response.ok) throw new Error('Error al cargar productos más vendidos');

        const productos = await response.json();

        const contenedor = document.querySelector('.productos-vendidos');
        if (!contenedor) {
            console.error('No se encontró el contenedor de productos más vendidos');
            return;
        }

        contenedor.innerHTML = ''; // Limpiar productos existentes

        // Si no hay productos, mostrar mensaje
        if (productos.length === 0) {
            contenedor.innerHTML = '<p style="text-align: center; width: 100%; color: #666;">Aún no hay productos vendidos</p>';
            return;
        }

        productos.forEach(producto => {
            const item = document.createElement('div');
            item.className = 'item';

            item.innerHTML = `
                <figure>
                    <img src="${producto.urlImagen || imagenPorDefecto}" 
                         alt="${producto.nombre}"
                         onerror="this.onerror=null; this.src='${imagenPorDefecto}';">
                </figure>
                <div class="info-product">
                    <h2>${producto.nombre}</h2>
                    <div class="price">$${producto.precio.toLocaleString('es-AR')}</div>
                </div>
            `;

            contenedor.appendChild(item);
        });

    } catch (error) {
        console.error('Error al cargar productos más vendidos:', error);
        // Mantener productos por defecto si hay error
    }
}

/**
 * Inicializa la página
 */
document.addEventListener('DOMContentLoaded', async () => {
    await cargarCarrusel();
    await cargarMasVendidos();
});
