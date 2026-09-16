const overlay  = document.getElementById('dialogOverlay');
const dialog   = document.getElementById('dialog');
const closeBtn = document.getElementById('dialogClose');
const editBtn  = document.getElementById('dialogBtnEdit');
const addExBtn = document.getElementById('btnAddExercise');

let currentTreinoId = null;
let pendingDeletes  = [];
let hasChanges      = false;

document.addEventListener('click', (e) => {
    const card = e.target.closest('.card');
    if (!card) return;
    e.preventDefault();
    currentTreinoId = parseInt(card.dataset.id);
    document.getElementById('dialogTitle').textContent = card.dataset.titulo;
    carregarExercicios(currentTreinoId);
    overlay.classList.add('active');
});

async function carregarExercicios(treinoId) {
    const table  = document.querySelector('.exercise-table');
    const addBtn = document.getElementById('btnAddExercise');
    table.querySelectorAll('.exercise-row').forEach((r) => r.remove());
    pendingDeletes = [];
    hasChanges     = false;

    const loadingEl = document.createElement('div');
    loadingEl.classList.add('dialog-loading');
    loadingEl.textContent = 'Carregando exercícios...';
    table.insertBefore(loadingEl, addBtn);

    try {
        const exercicios = await getExercicios(treinoId);
        loadingEl.remove();
        exercicios.forEach((ex) => {
            table.insertBefore(criarLinhaExercicio(ex), addBtn);
        });
    } catch (err) {
        loadingEl.remove();
        console.error('[Dialog] Erro ao carregar exercícios:', err);
    }
}

function criarLinhaExercicio(ex) {
    const div = document.createElement('div');
    div.classList.add('exercise-row');
    div.dataset.id = ex.id;
    div.innerHTML = `
        <div class="cell-name">
            <span class="field-view exercise-name">${ex.nome}</span>
            <input type="text" class="field-edit exercise-input" value="${ex.nome}" maxlength="40" />
        </div>
        <div class="cell-val">
            <span class="field-view exercise-val">${ex.series}</span>
            <input type="number" class="field-edit exercise-input exercise-input--small" value="${ex.series}" min="1" />
        </div>
        <div class="cell-val">
            <span class="field-view exercise-val">${ex.repeticoes}</span>
            <input type="number" class="field-edit exercise-input exercise-input--small" value="${ex.repeticoes}" min="1" />
        </div>
        <input type="number" class="exercise-kg" value="${ex.kg}" min="0" />
        <div class="exercise-row-actions">
            <div class="actions-view">
                <button class="btn-x">✕</button>
                <button class="btn-check">✓</button>
            </div>
            <div class="actions-edit">
                <button class="btn-delete-exercise">Excluir</button>
            </div>
        </div>
    `;

    // Kg sempre editável: salva no banco ao mudar o valor
    div.querySelector('.exercise-kg').addEventListener('change', async () => {
        if (!div.dataset.id) return;
        const cellVals   = div.querySelectorAll('.cell-val');
        const nome       = div.querySelector('.exercise-name').textContent;
        const series     = parseInt(cellVals[0]?.querySelector('.field-view')?.textContent) || 0;
        const repeticoes = parseInt(cellVals[1]?.querySelector('.field-view')?.textContent) || 0;
        const kg         = parseFloat(div.querySelector('.exercise-kg').value) || 0;
        try {
            await updateExercicio(parseInt(div.dataset.id), { nome, series, repeticoes, kg });
            hasChanges = true;
        } catch (err) {
            console.error('[Dialog] Erro ao atualizar kg:', err);
        }
    });

    return div;
}

closeBtn.addEventListener('click', closeDialog);
overlay.addEventListener('click', (e) => {
    if (e.target === overlay) closeDialog();
});
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') closeDialog();
});

function closeDialog() {
    exitEditMode();
    overlay.classList.remove('active');
    if (hasChanges) window.location.reload();
    hasChanges = false;
}

let isEditing = false;

editBtn.addEventListener('click', () => {
    isEditing = !isEditing;

    if (isEditing) {
        dialog.classList.add('edit-mode');
        editBtn.textContent = 'Salvar';
        editBtn.classList.add('saving');
    } else {
        saveAndExitEditMode();
    }
});

async function saveAndExitEditMode() {
    hasChanges = true;

    // 1. Excluir exercícios marcados para deleção
    for (const id of pendingDeletes) {
        try {
            await deleteExercicio(id);
        } catch (err) {
            console.error('[Dialog] Erro ao excluir exercício:', err);
        }
    }
    pendingDeletes = [];

    // 2. Salvar / criar exercícios restantes
    const rows = document.querySelectorAll('.exercise-row');

    for (const row of rows) {
        const nameInput = row.querySelector('.cell-name .field-edit');
        const nameSpan  = row.querySelector('.cell-name .field-view');
        const cellVals  = row.querySelectorAll('.cell-val');
        const kgInput   = row.querySelector('.exercise-kg');

        const nome       = nameInput?.value.trim() ?? '';
        const series     = parseInt(cellVals[0]?.querySelector('.field-edit')?.value) || 0;
        const repeticoes = parseInt(cellVals[1]?.querySelector('.field-edit')?.value) || 0;
        const kg         = parseFloat(kgInput?.value) || 0;

        // Atualiza spans visuais
        if (nameSpan && nameInput) nameSpan.textContent = nome;
        cellVals.forEach((cell) => {
            const span  = cell.querySelector('.field-view');
            const input = cell.querySelector('.field-edit');
            if (span && input) span.textContent = input.value;
        });

        try {
            if (row.dataset.id) {
                await updateExercicio(parseInt(row.dataset.id), { nome, series, repeticoes, kg });
            } else {
                const novo = await createExercicio(currentTreinoId, { nome, series, repeticoes, kg });
                row.dataset.id = novo.id;
            }
        } catch (err) {
            console.error('[Dialog] Erro ao salvar exercício:', err);
        }
    }

    exitEditMode();
}

function exitEditMode() {
    isEditing = false;
    dialog.classList.remove('edit-mode');
    editBtn.textContent = 'Editar';
    editBtn.classList.remove('saving');
}

document.addEventListener('click', (e) => {
    const isX     = e.target.classList.contains('btn-x');
    const isCheck = e.target.classList.contains('btn-check');
    if (!isX && !isCheck) return;

    const row      = e.target.closest('.exercise-row');
    const btnX     = row.querySelector('.btn-x');
    const btnCheck = row.querySelector('.btn-check');

    if (isX) {
        btnX.classList.toggle('active', !btnX.classList.contains('active'));
        btnCheck.classList.remove('active');
    } else {
        btnCheck.classList.toggle('active', !btnCheck.classList.contains('active'));
        btnX.classList.remove('active');
    }
});

// Excluir: marca ID para deleção no banco e remove visualmente
document.addEventListener('click', (e) => {
    if (!e.target.classList.contains('btn-delete-exercise')) return;
    const row = e.target.closest('.exercise-row');
    if (row.dataset.id) {
        pendingDeletes.push(parseInt(row.dataset.id));
        hasChanges = true;
    }
    row.remove();
});

addExBtn.addEventListener('click', () => {
    const table  = document.querySelector('.exercise-table');
    const newRow = document.createElement('div');
    newRow.classList.add('exercise-row');
    newRow.innerHTML = `
        <div class="cell-name">
            <span class="field-view exercise-name">Novo Exercício</span>
            <input type="text" class="field-edit exercise-input" value="Novo Exercício" maxlength="40" />
        </div>
        <div class="cell-val">
            <span class="field-view exercise-val">3</span>
            <input type="number" class="field-edit exercise-input exercise-input--small" value="3" min="1" />
        </div>
        <div class="cell-val">
            <span class="field-view exercise-val">10</span>
            <input type="number" class="field-edit exercise-input exercise-input--small" value="10" min="1" />
        </div>
        <input type="number" class="exercise-kg" value="0" min="0" />
        <div class="exercise-row-actions">
            <div class="actions-view">
                <button class="btn-x">✕</button>
                <button class="btn-check">✓</button>
            </div>
            <div class="actions-edit">
                <button class="btn-delete-exercise">Excluir</button>
            </div>
        </div>
    `;
    table.insertBefore(newRow, addExBtn);
    newRow.querySelector('.exercise-input').focus();
});
