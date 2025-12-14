/**
 * Script para la página de pedidos
 * Carga y muestra el historial de pedidos del usuario desde la base de datos
 */

// Verificar si estamos en la página de pedidos
const pedidosContainer = document.getElementById('pedidos-list');
const esPedidosPage = pedidosContainer !== null;

// SVG para imagen de respaldo
const imagenPorDefecto = `data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='60' height='60'%3E%3Crect fill='%23e0e0e0' width='60' height='60'/%3E%3Ctext x='50%25' y='50%25' dominant-baseline='middle' text-anchor='middle' font-family='Arial, sans-serif' font-size='12' fill='%23999'%3EN/A%3C/text%3E%3C/svg%3E`;

/**
 * Retorna el color según el estado del pedido
 */
function getEstadoClass(estado) {
    const estados = {
        'PENDIENTE': 'estado-PENDIENTE',
        'CONFIRMADO': 'estado-CONFIRMADO',
        'ENVIADO': 'estado-ENVIADO',
        'ENTREGADO': 'estado-ENTREGADO',
        'CANCELADO': 'estado-CANCELADO'
    };
    return estados[estado] || 'estado-PENDIENTE';
}

/**
 * Formatea la fecha a formato legible
 */
function formatearFecha(fechaString) {
    const fecha = new Date(fechaString);
    const opciones = {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    };
    return fecha.toLocaleDateString('es-ES', opciones);
}

/**
 * Carga los pedidos del usuario desde la API
 * Si el usuario es admin, carga todos los pedidos
 */
async function cargarPedidos() {
    if (!esPedidosPage) return;

    // Verificar autenticación
    const userId = obtenerUsuarioId();
    if (!userId) {
        pedidosContainer.innerHTML = `
            <div class="error-message" style="text-align: center; padding: 40px; background: #fee; border-radius: 8px; color: #c33;">
                <i class="fa-solid fa-lock" style="font-size: 48px; margin-bottom: 20px;"></i>
                <h3>Acceso Restringido</h3>
                <p>Debes iniciar sesión para ver tus pedidos</p>
                <a href="login.html" style="display: inline-block; margin-top: 20px; padding: 10px 20px; background: #667eea; color: white; text-decoration: none; border-radius: 8px;">Iniciar Sesión</a>
            </div>
        `;
        return;
    }

    try {
        // Si es admin, cargar todos los pedidos, si no, solo los del usuario
        const endpoint = esAdmin()
            ? `${API_BASE_URL}/pedidos`
            : `${API_BASE_URL}/pedidos/usuario/${userId}`;

        const response = await fetch(endpoint);

        if (!response.ok) {
            throw new Error(`Error ${response.status}: ${response.statusText}`);
        }

        const pedidos = await response.json();
        // console.log('📦 Pedidos cargados:', pedidos);

        mostrarPedidos(pedidos);

    } catch (error) {
        console.error('❌ Error al cargar pedidos:', error);
        mostrarError(error.message);
    }
}

/**
 * Muestra los pedidos en el DOM
 */
function mostrarPedidos(pedidos) {
    if (!pedidosContainer) return;

    // Si no hay pedidos
    if (!pedidos || pedidos.length === 0) {
        pedidosContainer.innerHTML = `
            <div class="no-pedidos">
                <i class="fa-solid fa-box-open"></i>
                <h2>No tienes pedidos aún</h2>
                <p>Cuando realices tu primera compra, aparecerá aquí.</p>
                <a href="productos.html">
                    <i class="fa-solid fa-shopping-bag"></i> Ir a productos
                </a>
            </div>
        `;
        return;
    }

    // Ordenar pedidos por fecha (más recientes primero)
    pedidos.sort((a, b) => new Date(b.fechaPedido) - new Date(a.fechaPedido));

    // Renderizar pedidos
    pedidosContainer.innerHTML = pedidos.map(pedido => `
        <div class="pedido-card">
            <div class="pedido-header">
                <div>
                    <div class="pedido-id">Pedido #${pedido.id}</div>
                    ${esAdmin() ? `
                        <div style="font-size: 14px; color: #666; margin-top: 5px;">
                            <i class="fa-solid fa-user"></i> 
                            ${pedido.usuario ? `${pedido.usuario.nombre} ${pedido.usuario.apellido} (${pedido.usuario.email})` : 'Usuario desconocido'}
                        </div>
                    ` : ''}
                    <div class="pedido-fecha">
                        <i class="fa-regular fa-calendar"></i> 
                        ${formatearFecha(pedido.fechaPedido)}
                    </div>
                </div>
                <span class="pedido-estado ${getEstadoClass(pedido.estado)}">
                    ${pedido.estado}
                </span>
            </div>

            <div class="pedido-lineas">
                ${pedido.lineas.map(linea => `
                    <div class="linea-item">
                        <img src="${linea.producto.urlImagen || imagenPorDefecto}" 
                             alt="${linea.producto.nombre}"
                             onerror="this.onerror=null; this.src='${imagenPorDefecto}';">
                        <div class="linea-info">
                            <div class="linea-nombre">${linea.producto.nombre}</div>
                            <div class="linea-cantidad">
                                Cantidad: ${linea.cantidad} × $${linea.precioUnitario.toLocaleString('es-AR')}
                            </div>
                        </div>
                        <div class="linea-precio">
                            $${(linea.cantidad * linea.precioUnitario).toLocaleString('es-AR')}
                        </div>
                    </div>
                `).join('')}
            </div>

            <div class="pedido-total">
                Total: $${pedido.total.toLocaleString('es-AR')}
            </div>
        </div>
    `).join('');
}

/**
 * Muestra un mensaje de error
 */
function mostrarError(mensaje) {
    if (!pedidosContainer) return;

    pedidosContainer.innerHTML = `
        <div class="error-message">
            <h3><i class="fa-solid fa-triangle-exclamation"></i> Error al cargar pedidos</h3>
            <p>No se pudieron cargar los pedidos. ${mensaje}</p>
            <p style="font-size: 14px; margin-top: 10px;">
                Asegúrate de que el servidor esté ejecutándose en ${API_BASE_URL}
            </p>
            <button onclick="cargarPedidos()" style="margin-top: 15px; padding: 10px 20px; background: #721c24; color: white; border: none; border-radius: 4px; cursor: pointer;">
                <i class="fa-solid fa-rotate-right"></i> Reintentar
            </button>
        </div>
    `;
}

/**
 * Inicializar cuando el DOM esté listo
 */
if (esPedidosPage) {
    document.addEventListener('DOMContentLoaded', () => {
        // Actualizar título si es admin
        const titulo = document.querySelector('.pedidos-container h1');
        if (titulo && esAdmin()) {
            titulo.innerHTML = '<i class="fa-solid fa-boxes-stacked"></i> Todos los Pedidos (Admin)';
        }

        cargarPedidos();
    });
}

