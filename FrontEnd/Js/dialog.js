const overlay  = document.getElementById('dialogOverlay');
const dialog   = document.getElementById('dialog');
const closeBtn = document.getElementById('dialogClose');
const editBtn  = document.getElementById('dialogBtnEdit');
const addExBtn = document.getElementById('btnAddExercise');

// ── Abrir dialog ao clicar no card ────────────────────
document.querySelectorAll('.card').forEach(card => {
    card.addEventListener('click', () => {
        overlay.classList.add('active');
    });
});

// ── Fechar ────────────────────────────────────────────
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
}

// ── Modo edição ───────────────────────────────────────
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

function saveAndExitEditMode() {
    // Sincroniza spans com os valores dos inputs ao salvar
    document.querySelectorAll('.exercise-row').forEach(row => {
        const nameSpan  = row.querySelector('.cell-name .field-view');
        const nameInput = row.querySelector('.cell-name .field-edit');
        if (nameSpan && nameInput) nameSpan.textContent = nameInput.value;

        const cellVals = row.querySelectorAll('.cell-val');
        cellVals.forEach(cell => {
            const span  = cell.querySelector('.field-view');
            const input = cell.querySelector('.field-edit');
            if (span && input) span.textContent = input.value;
        });
    });

    exitEditMode();
}

function exitEditMode() {
    isEditing = false;
    dialog.classList.remove('edit-mode');
    editBtn.textContent = 'Editar';
    editBtn.classList.remove('saving');
}

// ── Toggle ✕ / ✓ por linha (radio behavior) ──────────
document.addEventListener('click', (e) => {
    const isX     = e.target.classList.contains('btn-x');
    const isCheck = e.target.classList.contains('btn-check');
    if (!isX && !isCheck) return;

    const row     = e.target.closest('.exercise-row');
    const btnX    = row.querySelector('.btn-x');
    const btnCheck = row.querySelector('.btn-check');

    if (isX) {
        const alreadyActive = btnX.classList.contains('active');
        btnX.classList.toggle('active', !alreadyActive);
        btnCheck.classList.remove('active');
    } else {
        const alreadyActive = btnCheck.classList.contains('active');
        btnCheck.classList.toggle('active', !alreadyActive);
        btnX.classList.remove('active');
    }
});

// ── Excluir linha ─────────────────────────────────────
document.addEventListener('click', (e) => {
    if (!e.target.classList.contains('btn-delete-exercise')) return;
    e.target.closest('.exercise-row').remove();
});

// ── Adicionar novo exercício ──────────────────────────
addExBtn.addEventListener('click', () => {
    const table  = document.querySelector('.exercise-table');
    const newRow = document.createElement('div');
    newRow.classList.add('exercise-row');
    newRow.innerHTML = `
        <div class="cell-name">
            <span class="field-view exercise-name">Novo Exercício</span>
            <input type="text" class="field-edit exercise-input" value="Novo Exercício" />
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
    // Foca no input do nome do novo exercício
    newRow.querySelector('.exercise-input').focus();
});
