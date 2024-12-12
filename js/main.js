
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

// Fetch Productos desde la API
async function fetchProductos() {
    try {
        const response = await fetch('https://furniture-api.fly.dev/v1/products?category=sofa&limit=12');
        const data = await response.json();
        productos = data.data;
        renderizarProductos();
    } catch (error) {
        console.error('Error fetching productos:', error);
        productosConteiner.innerHTML = '<p class="text-center">Error al cargar los productos. Por favor, intente más tarde nuevamente.</p>';
    }
}

// renderizar productos
function renderizarProductos() {
    productosConteiner.innerHTML = productos.map(producto => `
        <div class="item">
            <figure>
                <img src="${producto.image_path}" alt="imagen_${producto.name}">
            </figure>
            <div class="info-product">
                <h2>${producto.name}</h2>
                <div class="price">$${producto.price.toFixed(2)}</div>
                <button onclick="addToCart('${producto.id}')">Añadir al carrito</button>
            </div>
        </div>
    `).join('');
}

// funcion para añadir productos al carrito
function addToCart(productoId) {
    const producto = productos.find(p => p.id === productoId);
    const itemExist = carrito.find(item => item.id === productoId);

    if (itemExist) {
        itemExist.cantidad += 1;
    } else {
        carrito.push({
            id: producto.id,
            nombre: producto.name,
            imagen: producto.image_path,
            precio: producto.price,
            cantidad: 1
        });
    }
    actualizarCarrito();
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
    if (item) {
        item.cantidad = nuevaCantidad;
        actualizarCarrito();
    }
}

// actualizar carrito
function actualizarCarrito() {
    // guardar en el localStorage
    localStorage.setItem('carrito', JSON.stringify(carrito));

    // actualizar contador del carrito
    cartCount.textContent = carrito.reduce((total, item) => total + item.cantidad, 0);

    // renderizado de productos del carrito
    if (cartItems) {
        cartItems.innerHTML = carrito.map(item => `
            <div class="cart-item">
                <img src="${item.imagen}" alt="${item.nombre}">
                <div class="cart-item-details">
                    <h6>${item.nombre}</h6>
                    <div class="quantity-controls">
                        <button onclick="actualizarCantidad('${item.id}', ${item.cantidad - 1})">-</button>
                        <span>${item.cantidad}</span>
                        <button onclick="actualizarCantidad('${item.id}', ${item.cantidad + 1})">+</button>
                    </div>
                </div>
                <div class="cart-item-price">
                    <div>$${(item.precio * item.cantidad).toFixed(2)}</div>
                    <button onclick="removerDelCarrito('${item.id}')">Eliminar</button>
                </div>
            </div>
        `).join('');
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


if (checkoutBtn) {
    checkoutBtn.addEventListener('click', () => {
        alert('Muchas gracias por su compra!');
        carrito = [];
        actualizarCarrito();
        cartModal.style.display = 'none';
    });
}


fetchProductos();
actualizarCarrito();