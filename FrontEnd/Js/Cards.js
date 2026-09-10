// Configuração equivalente às props do componente React original
// const card = document.getElementById('tiltCard');
// const glow = document.getElementById('spotlightGlow');

// const tiltLimit = parseFloat(card.dataset.tiltLimit); // ângulo máximo de tilt
// const scale = parseFloat(card.dataset.scale); // escala no hover
// const perspective = parseFloat(card.dataset.perspective); // distância da perspectiva
// const effect = card.dataset.effect; // "gravitate" ou "evade"
// const dir = effect === 'evade' ? -1 : 1;

// function resetTransform() {
//     card.style.transform = `perspective(${perspective}px) rotateX(0deg) rotateY(0deg) scale3d(1, 1, 1)`;
// }

// card.addEventListener('pointermove', (e) => {
//     const rect = card.getBoundingClientRect();
//     const px = (e.clientX - rect.left) / rect.width;
//     const py = (e.clientY - rect.top) / rect.height;

//     const xRot = (py - 0.5) * (tiltLimit * 2) * dir;
//     const yRot = (px - 0.5) * -(tiltLimit * 2) * dir;

//     card.style.transform = `perspective(${perspective}px) rotateX(${xRot}deg) rotateY(${yRot}deg) scale3d(${scale}, ${scale}, ${scale})`;

//     glow.style.left = `${px * 100}%`;
//     glow.style.top = `${py * 100}%`;
// });

// card.addEventListener('pointerleave', resetTransform);

// resetTransform();
