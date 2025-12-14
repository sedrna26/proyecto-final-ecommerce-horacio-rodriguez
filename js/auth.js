const API_URL = 'http://localhost:8080/api';

// =============== UTILIDADES DE SESIÓN ===============

function getUser() {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
}

function setUser(user) {
    localStorage.setItem('user', JSON.stringify(user));
}

function clearUser() {
    localStorage.removeItem('user');
}

function isLoggedIn() {
    return getUser() !== null;
}

function isAdmin() {
    const user = getUser();
    return user && user.rol === 'ADMIN';
}

function logout() {
    const user = getUser();

    if (user) {
        // Llamar al endpoint de logout
        fetch(`${API_URL}/auth/logout`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(() => {
            clearUser();
            window.location.href = 'login.html';
        }).catch(() => {
            // Logout local aunque falle el servidor
            clearUser();
            window.location.href = 'login.html';
        });
    } else {
        window.location.href = 'login.html';
    }
}

// =============== MANEJO DE LOGIN ===============

if (document.getElementById('login-form')) {
    const loginForm = document.getElementById('login-form');
    const btnLogin = document.getElementById('btn-login');
    const errorMessage = document.getElementById('error-message');
    const successMessage = document.getElementById('success-message');

    // Redirigir si ya está logueado
    if (isLoggedIn()) {
        window.location.href = 'index.html';
    }

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;

        // Validaciones básicas
        if (!email || !password) {
            mostrarError('Por favor completa todos los campos');
            return;
        }

        // Deshabilitar botón durante la petición
        btnLogin.disabled = true;
        btnLogin.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Iniciando sesión...';

        try {
            const response = await fetch(`${API_URL}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password })
            });

            const data = await response.json();

            if (response.ok) {
                // Login exitoso
                setUser(data);
                mostrarExito('¡Bienvenido! Redirigiendo...');

                setTimeout(() => {
                    window.location.href = 'index.html';
                }, 1000);
            } else {
                // Error de autenticación
                mostrarError(data.mensaje || 'Error al iniciar sesión');
                btnLogin.disabled = false;
                btnLogin.innerHTML = '<i class="fa-solid fa-right-to-bracket"></i> Iniciar Sesión';
            }
        } catch (error) {
            console.error('Error:', error);
            mostrarError('Error de conexión. Verifica que el servidor esté activo.');
            btnLogin.disabled = false;
            btnLogin.innerHTML = '<i class="fa-solid fa-right-to-bracket"></i> Iniciar Sesión';
        }
    });

    function mostrarError(mensaje) {
        errorMessage.textContent = mensaje;
        errorMessage.style.display = 'block';
        successMessage.style.display = 'none';
    }

    function mostrarExito(mensaje) {
        successMessage.textContent = mensaje;
        successMessage.style.display = 'block';
        errorMessage.style.display = 'none';
    }
}

// =============== MANEJO DE REGISTRO ===============

if (document.getElementById('register-form')) {
    const registerForm = document.getElementById('register-form');
    const btnRegister = document.getElementById('btn-register');
    const errorMessage = document.getElementById('error-message');
    const successMessage = document.getElementById('success-message');

    // Redirigir si ya está logueado
    if (isLoggedIn()) {
        window.location.href = 'index.html';
    }

    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const nombre = document.getElementById('nombre').value.trim();
        const apellido = document.getElementById('apellido').value.trim();
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        const telefono = document.getElementById('telefono').value.trim() || null;
        const direccion = document.getElementById('direccion').value.trim() || null;

        // Validaciones
        if (!nombre || !apellido || !email || !password) {
            mostrarError('Por favor completa todos los campos obligatorios');
            return;
        }

        if (password.length < 6) {
            mostrarError('La contraseña debe tener al menos 6 caracteres');
            return;
        }

        // Validar email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            mostrarError('Por favor ingresa un email válido');
            return;
        }

        // Deshabilitar botón
        btnRegister.disabled = true;
        btnRegister.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Registrando...';

        try {
            const response = await fetch(`${API_URL}/auth/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    nombre,
                    apellido,
                    email,
                    password,
                    telefono,
                    direccion
                })
            });

            const data = await response.json();

            if (response.ok) {
                // Registro exitoso - auto login
                setUser(data);
                mostrarExito('¡Registro exitoso! Redirigiendo...');

                setTimeout(() => {
                    window.location.href = 'index.html';
                }, 1500);
            } else {
                // Error en el registro
                mostrarError(data.mensaje || 'Error al registrar usuario');
                btnRegister.disabled = false;
                btnRegister.innerHTML = '<i class="fa-solid fa-user-plus"></i> Registrarse';
            }
        } catch (error) {
            console.error('Error:', error);
            mostrarError('Error de conexión. Verifica que el servidor esté activo.');
            btnRegister.disabled = false;
            btnRegister.innerHTML = '<i class="fa-solid fa-user-plus"></i> Registrarse';
        }
    });

    function mostrarError(mensaje) {
        errorMessage.textContent = mensaje;
        errorMessage.style.display = 'block';
        successMessage.style.display = 'none';
    }

    function mostrarExito(mensaje) {
        successMessage.textContent = mensaje;
        successMessage.style.display = 'block';
        errorMessage.style.display = 'none';
    }
}

// Exportar funciones para uso en otros archivos
window.authUtils = {
    getUser,
    setUser,
    clearUser,
    isLoggedIn,
    isAdmin,
    logout
};
