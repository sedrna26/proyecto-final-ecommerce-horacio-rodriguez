let slides;
let slide;
let prevBtn;
let nextBtn;
let index = 0;
let intervalId = null;

function iniciarSlider() {
    // Limpiar intervalo anterior si existe
    if (intervalId) {
        clearInterval(intervalId);
    }

    // Reinicializar elementos
    slides = document.querySelector('.slides');
    slide = document.querySelectorAll('.slide');
    prevBtn = document.querySelector('.prev');
    nextBtn = document.querySelector('.next');
    index = 0;

    console.log('🔄 Iniciando slider con', slide.length, 'slides');

    // Si no hay slides, no hacer nada
    if (!slide || slide.length === 0) {
        console.log('⚠️ No se encontraron slides');
        return;
    }

    function updateSlide() {
        slides.style.transform = `translateX(-${index * 100}%)`;
    }

    function nextSlide() {
        index = (index === slide.length - 1) ? 0 : index + 1;
        updateSlide();
    }

    function prevSlide() {
        index = (index === 0) ? slide.length - 1 : index - 1;
        updateSlide();
    }

    // Remover listeners antiguos y agregar nuevos
    if (prevBtn && nextBtn) {
        prevBtn.removeEventListener('click', prevSlide);
        nextBtn.removeEventListener('click', nextSlide);
        prevBtn.addEventListener('click', prevSlide);
        nextBtn.addEventListener('click', nextSlide);
    }

    // Función para cambiar de diapositiva automáticamente cada 6 segundos
    function autoSlide() {
        intervalId = setInterval(() => {
            nextSlide();
        }, 6000);
    }

    // Iniciar transición automática
    autoSlide();

    // Asegurar que el primer slide esté visible
    updateSlide();
}

// Inicializar el slider cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    iniciarSlider();
});