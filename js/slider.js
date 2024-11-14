const slides = document.querySelector('.slides');
const slide = document.querySelectorAll('.slide');
const prevBtn = document.querySelector('.prev');
const nextBtn = document.querySelector('.next');

let index = 0;

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

prevBtn.addEventListener('click', prevSlide);
nextBtn.addEventListener('click', nextSlide);

// Función para cambiar de diapositiva automáticamente cada 3 segundos
function autoSlide() {
    setInterval(() => {
        nextSlide();
    }, 6000); // Cambiar de diapositiva cada 6 segundos (6000 milisegundos)
}

autoSlide(); // Llamar a la función para iniciar la transición automática