
// ===== CONFIGURACIÓN DE LA API =====
const API_BASE_URL = 'http://localhost:8080/api';

// ===== AUTENTICACIÓN =====
let usuarioActual = null;

// Cargar usuario desde localStorage si está disponible auth.js
if (typeof authUtils !== 'undefined') {
    usuarioActual = authUtils.getUser();
}

// Obtener ID del usuario actual (para pedidos)
function obtenerUsuarioId() {
    return usuarioActual ? usuarioActual.id : null;
}

// Verificar si el usuario es admin
function esAdmin() {
    return usuarioActual && usuarioActual.rol === 'ADMIN';
}

// Actualizar UI según estado de autenticación
function actualizarUIAutenticacion() {
    const userInfoElement = document.getElementById('user-info');
    const loginLinkElement = document.getElementById('login-link');
    const logoutLinkElement = document.getElementById('logout-link');

    if (usuarioActual) {
        // Usuario logueado
        if (userInfoElement) {
            userInfoElement.textContent = `${usuarioActual.nombre} ${usuarioActual.apellido}`;
            userInfoElement.style.display = 'inline';
        }
        if (loginLinkElement) loginLinkElement.style.display = 'none';
        if (logoutLinkElement) logoutLinkElement.style.display = 'inline';
    } else {
        // Usuario no logueado
        if (userInfoElement) userInfoElement.style.display = 'none';
        if (loginLinkElement) loginLinkElement.style.display = 'inline';
        if (logoutLinkElement) logoutLinkElement.style.display = 'none';
    }
}

// Ejecutar al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    actualizarUIAutenticacion();
});

const nav = document.querySelector("#nav");
const abrir = document.querySelector("#abrir");
const cerrar = document.querySelector("#cerrar");

abrir.addEventListener("click", () => {
    nav.classList.add("visible");
});

cerrar.addEventListener("click", () => {
    nav.classList.remove("visible");
});

// manejo del carrito
let carrito = JSON.parse(localStorage.getItem('carrito')) || [];
let productos = [];

// Elementos del DOM
const productosConteiner = document.getElementById('conteiner-items');
const cartBtn = document.getElementById('cart-btn');
const cartModal = document.getElementById('cart-modal');
const cartItems = document.getElementById('cart-items');
const cartTotal = document.getElementById('cart-total');
const checkoutBtn = document.getElementById('checkout-btn');
const cartCount = document.getElementById('cart-count');

// Verificar si estamos en la página de productos
const esProductosPage = productosConteiner !== null;

// ===== FETCH PRODUCTOS DESDE LA API SPRING BOOT =====
async function fetchProductos() {
    // Solo cargar productos si estamos en la página de productos
    if (!esProductosPage) {
        // console.log('ℹ️ No se cargan productos');
        return;
    }

    try {
        // Petición GET a la API REST Java
        const response = await fetch(`${API_BASE_URL}/productos`);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        productos = data;

        // Limpiar carrito de productos que ya no existen
        limpiarCarritoInvalido();

        renderizarProductos();
        console.log('✅ Productos cargados desde la API:', productos.length);
    } catch (error) {
        console.error('❌ Error al cargar productos:', error);
        productosConteiner.innerHTML = `
            <div class="error-message" style="grid-column: 1/-1; text-align: center; padding: 40px;">
                <h3>⚠️ Error al cargar los productos</h3>
                <p>No se pudo conectar con el servidor. Asegúrate de que la API esté corriendo en ${API_BASE_URL}</p>
                <p style="color: #666; font-size: 14px;">Error: ${error.message}</p>
            </div>
        `;
    }
}

// ===== LIMPIAR CARRITO DE PRODUCTOS INVÁLIDOS =====
function limpiarCarritoInvalido() {
    const idsValidos = productos.map(p => p.id);
    const carritoOriginal = carrito.length;

    carrito = carrito.filter(item => idsValidos.includes(item.id));

    if (carrito.length < carritoOriginal) {
        console.log(`🧹 Se eliminaron ${carritoOriginal - carrito.length} productos obsoletos del carrito`);
        actualizarCarrito();
    }
}

// ===== RENDERIZAR PRODUCTOS =====
function renderizarProductos() {
    if (!esProductosPage || !productosConteiner) {
        return; // No hacer nada si no estamos en productos.html
    }

    if (!productos || productos.length === 0) {
        productosConteiner.innerHTML = '<p style="grid-column: 1/-1; text-align: center;">No hay productos disponibles</p>';
        return;
    }

    // Usar la función de renderizado con filtros si existe
    if (typeof renderizarProductosFiltrados === 'function') {
        productosFiltrados = productos;
        renderizarProductosFiltrados();
        return;
    }

    // Renderizado simple original (fallback)
    productosConteiner.innerHTML = productos.map(producto => `
        <div class="item">
            <figure>
               <img src="${producto.urlImagen || 'data:image/svg+xml,%3Csvg xmlns=%27http://www.w3.org/2000/svg%27 width=%27300%27 height=%27200%27%3E%3Crect fill=%27%23ddd%27 width=%27300%27 height=%27200%27/%3E%3Ctext fill=%27%23666%27 font-family=%27Arial%27 font-size=%2720%27 x=%2750%25%27 y=%2750%25%27 text-anchor=%27middle%27 dy=%27.3em%27%3E${producto.nombre}%3C/text%3E%3C/svg%3E'}" 
     alt="${producto.nombre}"
     onerror="this.onerror=null; this.src='data:image/svg+xml,%3Csvg xmlns=%27http://www.w3.org/2000/svg%27 width=%27300%27 height=%27200%27%3E%3Crect fill=%27%23f0f0f0%27 width=%27300%27 height=%27200%27/%3E%3Ctext fill=%27%23999%27 font-family=%27Arial%27 font-size=%2718%27 x=%2750%25%27 y=%2750%25%27 text-anchor=%27middle%27 dy=%27.3em%27%3EImagen no disponible%3C/text%3E%3C/svg%3E';">
            </figure>
            <div class="info-product">
                <h2>${producto.nombre}</h2>
                <p style="font-size: 14px; color: #666; margin: 8px 0;">${producto.descripcion || ''}</p>
                <div style="display: flex; justify-content: space-between; align-items: center; margin: 10px 0;">
                    <span style="font-size: 12px; background: #e0e0e0; padding: 4px 8px; border-radius: 4px;">
                        ${producto.categoria}
                    </span>
                    <span style="font-size: 12px; color: ${producto.stock > 10 ? '#4CAF50' : '#ff9800'};">
                        Stock: ${producto.stock}
                    </span>
                </div>
                <div class="price">$${producto.precio.toFixed(2)}</div>
                <button onclick="addToCart(${producto.id})" 
                        ${producto.stock === 0 ? 'disabled' : ''}
                        style="${producto.stock === 0 ? 'opacity: 0.5; cursor: not-allowed;' : ''}">
                    ${producto.stock === 0 ? 'Sin Stock' : 'Añadir al carrito'}
                </button>
            </div>
        </div>
    `).join('');
}

// ===== AÑADIR PRODUCTO AL CARRITO =====
function addToCart(productoId) {
    const producto = productos.find(p => p.id === productoId);

    if (!producto) {
        alert('❌ Producto no encontrado');
        return;
    }

    if (producto.stock === 0) {
        alert('❌ Producto sin stock disponible');
        return;
    }

    const itemExist = carrito.find(item => item.id === productoId);

    if (itemExist) {
        // Verifica que no se supere el stock disponible
        if (itemExist.cantidad >= producto.stock) {
            alert(`⚠️ Solo hay ${producto.stock} unidades disponibles de ${producto.nombre}`);
            return;
        }
        itemExist.cantidad += 1;
    } else {
        carrito.push({
            id: producto.id,
            nombre: producto.nombre,
            imagen: producto.urlImagen || 'https://placehold.co/300x200?text=Producto', // <--- Aquí
            precio: producto.precio,
            cantidad: 1,
            stockDisponible: producto.stock
        });
    }

    actualizarCarrito();

    // Mostrar notificación
    mostrarNotificacion(`✅ ${producto.nombre} agregado al carrito`);
}

// ===== MOSTRAR NOTIFICACIÓN =====
function mostrarNotificacion(mensaje) {
    const notif = document.createElement('div');
    notif.textContent = mensaje;
    notif.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: #4CAF50;
        color: white;
        padding: 15px 25px;
        border-radius: 5px;
        z-index: 10000;
        animation: slideIn 0.3s ease-out;
    `;
    document.body.appendChild(notif);

    setTimeout(() => {
        notif.style.animation = 'slideOut 0.3s ease-out';
        setTimeout(() => notif.remove(), 300);
    }, 2000);
}

// Eliminar producto del carrito
function removerDelCarrito(productoId) {
    carrito = carrito.filter(item => item.id !== productoId);
    actualizarCarrito();
}

// actualizar cantidad de productos
function actualizarCantidad(productoId, nuevaCantidad) {
    if (nuevaCantidad < 1) return;

    const item = carrito.find(item => item.id === productoId);
    if (!item) return;

    // Verifica que no se supere el stock disponible
    if (nuevaCantidad > item.stockDisponible) {
        alert(`⚠️ Solo hay ${item.stockDisponible} unidades disponibles`);
        return;
    }

    item.cantidad = nuevaCantidad;
    actualizarCarrito();
}

// actualizar carrito
function actualizarCarrito() {
    // guardar en el localStorage
    localStorage.setItem('carrito', JSON.stringify(carrito));

    // actualizar contador del carrito
    cartCount.textContent = carrito.reduce((total, item) => total + item.cantidad, 0);

    // renderizado de productos del carrito
    if (cartItems) {
        if (carrito.length === 0) {
            cartItems.innerHTML = '<p style="text-align: center; padding: 20px; color: #666;">🛒 El carrito está vacío</p>';
        } else {
            cartItems.innerHTML = carrito.map(item => `
                <div class="cart-item">
                    <img src="${item.imagen}" 
                         alt="${item.nombre}"
                         onerror="this.onerror=null; this.src='data:image/svg+xml,%3Csvg xmlns=%27http://www.w3.org/2000/svg%27 width=%27100%27 height=%27100%27%3E%3Crect fill=%27%23f0f0f0%27 width=%27100%27 height=%27100%27/%3E%3Ctext fill=%27%23999%27 font-family=%27Arial%27 font-size=%2712%27 x=%2750%25%27 y=%2750%25%27 text-anchor=%27middle%27 dy=%27.3em%27%3ESin imagen%3C/text%3E%3C/svg%3E';">
                    <div class="cart-item-details">
                        <h6>${item.nombre}</h6>
                        <div class="quantity-controls">
                            <button onclick="actualizarCantidad(${item.id}, ${item.cantidad - 1})">-</button>
                            <span>${item.cantidad}</span>
                            <button onclick="actualizarCantidad(${item.id}, ${item.cantidad + 1})">+</button>
                        </div>
                        <small style="color: #666;">Stock disponible: ${item.stockDisponible}</small>
                    </div>
                    <div class="cart-item-price">
                        <div>$${(item.precio * item.cantidad).toFixed(2)}</div>
                        <button onclick="removerDelCarrito(${item.id})">Eliminar</button>
                    </div>
                </div>
            `).join('');
        }
    }

    // actualizar total del carrito
    if (cartTotal) {
        const total = carrito.reduce((sum, item) => sum + (item.precio * item.cantidad), 0);
        cartTotal.innerHTML = `<h5>Total: $${total.toFixed(2)}</h5>`;
    }
}


if (cartBtn && cartModal) {
    cartBtn.addEventListener('click', () => {
        cartModal.style.display = cartModal.style.display === 'block' ? 'none' : 'block';
        actualizarCarrito();
    });


    window.addEventListener('click', (event) => {
        if (event.target === cartModal) {
            cartModal.style.display = 'none';
        }
    });

    const closeBtn = document.querySelector('.close-btn');
    if (closeBtn) {
        closeBtn.addEventListener('click', () => {
            cartModal.style.display = 'none';
        });
    }
}

// ===== REALIZAR PEDIDO (CHECKOUT) =====
if (checkoutBtn) {
    checkoutBtn.addEventListener('click', async () => {
        if (carrito.length === 0) {
            alert('⚠️ El carrito está vacío');
            return;
        }

        // Deshabilitar botón mientras se procesa
        checkoutBtn.disabled = true;
        checkoutBtn.textContent = 'Procesando...';

        try {
            // Verificar que el usuario esté logueado
            const userId = obtenerUsuarioId();
            if (!userId) {
                alert('⚠️ Debes iniciar sesión para realizar un pedido');
                window.location.href = 'login.html';
                return;
            }

            // Preparar datos del pedido
            const productosMap = {};
            carrito.forEach(item => {
                productosMap[item.id] = item.cantidad;
            });

            const pedidoData = {
                usuarioId: userId,
                productos: productosMap
            };

            console.log('📤 Enviando pedido:', pedidoData);

            // Enviar POST a la API
            const response = await fetch(`${API_BASE_URL}/pedidos`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(pedidoData)
            });

            // Verificar si la respuesta es JSON
            const contentType = response.headers.get('content-type');
            if (!contentType || !contentType.includes('application/json')) {
                const textResponse = await response.text();
                console.error('❌ Respuesta no es JSON:', textResponse);
                throw new Error('El servidor no devolvió una respuesta JSON válida');
            }

            const responseData = await response.json();

            if (!response.ok) {
                throw new Error(responseData.mensaje || responseData.error || 'Error al crear el pedido');
            }

            console.log('✅ Pedido creado:', responseData);

            // Mostrar mensaje de éxito
            alert(`✅ ¡Pedido #${responseData.id} realizado con éxito!\n\nTotal: $${responseData.total.toFixed(2)}\nEstado: ${responseData.estado}`);

            // Limpiar carrito
            carrito = [];
            actualizarCarrito();

            // Cerrar modal
            cartModal.style.display = 'none';

            // Recargar productos para actualizar stock
            await fetchProductos();

        } catch (error) {
            console.error('❌ Error al realizar pedido:', error);
            alert(`❌ Error al realizar el pedido:\n${error.message}`);
        } finally {
            checkoutBtn.disabled = false;
            checkoutBtn.textContent = 'Finalizar Compra';
        }
    });
}

// ===== VARIABLES PARA ADMINISTRACIÓN =====
let modoAdmin = false;
let productosFiltrados = [];

// ===== BÚSQUEDA Y FILTRADO =====
if (esProductosPage) {
    const searchInput = document.getElementById('search-input');
    const categoryFilter = document.getElementById('category-filter');

    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const termino = e.target.value.toLowerCase();
            const categoriaSeleccionada = categoryFilter?.value || '';

            productosFiltrados = productos.filter(p => {
                const coincideNombre = p.nombre.toLowerCase().includes(termino) ||
                    (p.descripcion && p.descripcion.toLowerCase().includes(termino));
                const coincideCategoria = !categoriaSeleccionada || p.categoria === categoriaSeleccionada;
                return coincideNombre && coincideCategoria;
            });

            renderizarProductosFiltrados();
        });
    }

    if (categoryFilter) {
        categoryFilter.addEventListener('change', (e) => {
            const categoria = e.target.value;
            const termino = searchInput?.value.toLowerCase() || '';

            productosFiltrados = productos.filter(p => {
                const coincideNombre = p.nombre.toLowerCase().includes(termino) ||
                    (p.descripcion && p.descripcion.toLowerCase().includes(termino));
                const coincideCategoria = !categoria || p.categoria === categoria;
                return coincideNombre && coincideCategoria;
            });

            renderizarProductosFiltrados();
        });
    }
}

function renderizarProductosFiltrados() {
    const productosAMostrar = productosFiltrados.length > 0 ||
        document.getElementById('search-input')?.value ||
        document.getElementById('category-filter')?.value
        ? productosFiltrados
        : productos;

    if (!productosConteiner) return;

    if (productosAMostrar.length === 0) {
        productosConteiner.innerHTML = '<p style="grid-column: 1/-1; text-align: center; padding: 40px; color: #666;">No se encontraron productos</p>';
        return;
    }

    productosConteiner.innerHTML = productosAMostrar.map(producto => {
        const botonesAdmin = modoAdmin ? `
            <div style="display: flex; gap: 8px; margin-top: 10px;">
                <button onclick="editarProducto(${producto.id})" style="flex: 1; padding: 8px; background: #ffc107; color: #000; border: none; border-radius: 4px; cursor: pointer; font-size: 13px; font-weight: 600;">
                    <i class="fa-solid fa-edit"></i> Editar
                </button>
                <button onclick="eliminarProducto(${producto.id})" style="flex: 1; padding: 8px; background: #dc3545; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 13px; font-weight: 600;">
                    <i class="fa-solid fa-trash"></i> Eliminar
                </button>
            </div>
        ` : '';

        return `
            <div class="item">
                <figure>
                   <img src="${producto.urlImagen || 'data:image/svg+xml,%3Csvg xmlns=%27http://www.w3.org/2000/svg%27 width=%27300%27 height=%27200%27%3E%3Crect fill=%27%23ddd%27 width=%27300%27 height=%27200%27/%3E%3Ctext fill=%27%23666%27 font-family=%27Arial%27 font-size=%2720%27 x=%2750%25%27 y=%2750%25%27 text-anchor=%27middle%27 dy=%27.3em%27%3E${producto.nombre}%3C/text%3E%3C/svg%3E'}" 
         alt="${producto.nombre}"
         onerror="this.onerror=null; this.src='data:image/svg+xml,%3Csvg xmlns=%27http://www.w3.org/2000/svg%27 width=%27300%27 height=%27200%27%3E%3Crect fill=%27%23f0f0f0%27 width=%27300%27 height=%27200%27/%3E%3Ctext fill=%27%23999%27 font-family=%27Arial%27 font-size=%2718%27 x=%2750%25%27 y=%2750%25%27 text-anchor=%27middle%27 dy=%27.3em%27%3EImagen no disponible%3C/text%3E%3C/svg%3E';">
                </figure>
                <div class="info-product">
                    <h2>${producto.nombre}</h2>
                    <p style="font-size: 14px; color: #666; margin: 8px 0;">${producto.descripcion || ''}</p>
                    <p style="font-size: 12px; color: #999; margin: 4px 0;">
                        <i class="fa-solid fa-tag"></i> ${producto.categoria || 'Sin categoría'}
                    </p>
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 10px;">
                        <div class="price">$${producto.precio.toLocaleString('es-AR')}</div>
                        <div style="font-size: 13px; color: ${producto.stock > 10 ? '#28a745' : producto.stock > 0 ? '#ffc107' : '#dc3545'}; font-weight: 600;">
                            <i class="fa-solid fa-boxes-stacked"></i> ${producto.stock} en stock
                        </div>
                    </div>
                    ${!modoAdmin ? `<button class="btn-add-cart" data-id="${producto.id}">Agregar al carrito</button>` : ''}
                    ${botonesAdmin}
                </div>
            </div>
        `;
    }).join('');

    // Re-agregar event listeners a los botones
    if (!modoAdmin) {
        document.querySelectorAll('.btn-add-cart').forEach(btn => {
            btn.addEventListener('click', () => {
                const id = parseInt(btn.dataset.id);
                addToCart(id);
            });
        });
    }
}

// ===== MODO ADMINISTRACIÓN =====
if (esProductosPage) {
    const btnAdminMode = document.getElementById('btn-admin-mode');
    const btnAddProduct = document.getElementById('btn-add-product');

    // Mostrar/ocultar botón de admin según el rol del usuario
    if (btnAdminMode) {
        if (esAdmin()) {
            btnAdminMode.style.display = 'flex';
        } else {
            btnAdminMode.style.display = 'none';
            // Ocultar también el botón de agregar producto
            if (btnAddProduct) {
                btnAddProduct.style.display = 'none';
            }
        }

        btnAdminMode.addEventListener('click', () => {
            modoAdmin = !modoAdmin;

            if (modoAdmin) {
                btnAdminMode.style.background = '#dc3545';
                btnAdminMode.innerHTML = '<i class="fa-solid fa-shield-halved"></i><span>Salir Admin</span>';
                btnAddProduct.style.display = 'flex';
            } else {
                btnAdminMode.style.background = '#28a745';
                btnAdminMode.innerHTML = '<i class="fa-solid fa-shield-halved"></i><span>Modo Admin</span>';
                btnAddProduct.style.display = 'none';
            }

            renderizarProductosFiltrados();
        });
    }

    if (btnAddProduct) {
        btnAddProduct.addEventListener('click', () => {
            abrirModalProducto();
        });
    }
}

// ===== MODAL DE PRODUCTO =====
function abrirModalProducto(producto = null) {
    const modal = document.getElementById('product-modal');
    const title = document.getElementById('product-modal-title');
    const form = document.getElementById('product-form');

    if (!modal || !form) return;

    title.textContent = producto ? 'Editar Producto' : 'Agregar Producto';

    if (producto) {
        document.getElementById('product-id').value = producto.id;
        document.getElementById('product-nombre').value = producto.nombre;
        document.getElementById('product-descripcion').value = producto.descripcion || '';
        document.getElementById('product-precio').value = producto.precio;
        document.getElementById('product-stock').value = producto.stock;
        document.getElementById('product-categoria').value = producto.categoria;
        document.getElementById('product-imagen').value = producto.urlImagen || '';
    } else {
        form.reset();
        document.getElementById('product-id').value = '';
    }

    modal.style.display = 'flex';
}

function cerrarModalProducto() {
    const modal = document.getElementById('product-modal');
    if (modal) {
        modal.style.display = 'none';
        document.getElementById('product-form').reset();
    }
}

if (esProductosPage) {
    const closeProductModal = document.querySelector('.close-product-modal');
    const cancelProductBtn = document.querySelector('.cancel-product-btn');
    const productForm = document.getElementById('product-form');

    if (closeProductModal) {
        closeProductModal.addEventListener('click', cerrarModalProducto);
    }

    if (cancelProductBtn) {
        cancelProductBtn.addEventListener('click', cerrarModalProducto);
    }

    if (productForm) {
        productForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await guardarProducto();
        });
    }
}

// ===== GUARDAR PRODUCTO =====
async function guardarProducto() {
    const id = document.getElementById('product-id').value;
    const producto = {
        nombre: document.getElementById('product-nombre').value,
        descripcion: document.getElementById('product-descripcion').value,
        precio: parseFloat(document.getElementById('product-precio').value),
        stock: parseInt(document.getElementById('product-stock').value),
        categoria: document.getElementById('product-categoria').value,
        urlImagen: document.getElementById('product-imagen').value || null,
        activo: true
    };

    try {
        const url = id ? `${API_BASE_URL}/productos/${id}` : `${API_BASE_URL}/productos`;
        const method = id ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(producto)
        });

        if (!response.ok) {
            throw new Error('Error al guardar el producto');
        }

        const productoGuardado = await response.json();
        console.log('✅ Producto guardado:', productoGuardado);

        alert(`✅ Producto ${id ? 'actualizado' : 'creado'} exitosamente`);

        cerrarModalProducto();
        await fetchProductos();

    } catch (error) {
        console.error('❌ Error al guardar producto:', error);
        alert(`❌ Error al guardar el producto: ${error.message}`);
    }
}

// ===== EDITAR PRODUCTO =====
window.editarProducto = async function (id) {
    const producto = productos.find(p => p.id === id);
    if (producto) {
        abrirModalProducto(producto);
    }
}

// ===== ELIMINAR PRODUCTO =====
window.eliminarProducto = async function (id) {
    const producto = productos.find(p => p.id === id);

    if (!confirm(`¿Estás seguro de eliminar "${producto.nombre}"?\nEsta acción no se puede deshacer.`)) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/productos/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Error al eliminar el producto');
        }

        alert('✅ Producto eliminado exitosamente');
        await fetchProductos();

    } catch (error) {
        console.error('❌ Error al eliminar producto:', error);
        alert(`❌ Error al eliminar el producto: ${error.message}`);
    }
}

fetchProductos();
actualizarCarrito();