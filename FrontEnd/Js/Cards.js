const section = document.querySelector('header section');
const cardAddLink = document.querySelector('a.test');

async function carregarTreinos() {
    const skeletons = [1, 2, 3].map(() => {
        const el = document.createElement('div');
        el.classList.add('card', 'card-skeleton');
        el.innerHTML = `
            <div class="container_card">
                <div class="skeleton-line skeleton-title"></div>
                <div class="skeleton-line skeleton-sub"></div>
                <div class="skeleton-line skeleton-btn"></div>
            </div>
        `;
        section.insertBefore(el, cardAddLink);
        return el;
    });

    try {
        const treinos = await getTreinos();
        skeletons.forEach((s) => s.remove());
        treinos.forEach((treino) => {
            section.insertBefore(criarCard(treino), cardAddLink);
        });
    } catch (err) {
        skeletons.forEach((s) => s.remove());
        console.error('[Cards] Erro ao carregar treinos:', err);
    }
}

function criarCard(treino) {
    const div = document.createElement('div');
    div.classList.add('card');
    div.dataset.id = treino.id;
    div.dataset.titulo = treino.titulo;
    div.innerHTML = `
        <div class="container_card">
            <div class="text_card">
                <h2>${treino.titulo}</h2>
                <p>${treino.totalExercicios} Exercício${treino.totalExercicios !== 1 ? 's' : ''}</p>
            </div>
            <div class="button_card">
                <button>></button>
            </div>
        </div>
    `;
    return div;
}

carregarTreinos();
