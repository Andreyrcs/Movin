// ================================================================
// api.js — Camada de comunicação com o backend (Movin)
//
// Como usar:
//   1. Troque BASE_URL pela URL real do seu backend.
//   2. Em cada função, remova o bloco "Mock" e descomente o
//      bloco "Backend real" logo abaixo.
//   3. Se o backend usar autenticação (JWT, cookie, etc.), adicione
//      os headers necessários nos objetos de configuração do fetch.
// ================================================================

const BASE_URL = 'http://localhost:3000/api'; // ← trocar pela URL do backend

// ── TREINOS (Cards da home) ──────────────────────────────────────

/**
 * Retorna todos os treinos do usuário.
 * GET /api/treinos
 * @returns {Promise<Array<{id, titulo, totalExercicios}>>}
 */
async function getTreinos() {
    // Mock — remover quando o backend estiver pronto
    return [
        { id: 1, titulo: 'Segunda', totalExercicios: 3 },
    ];

    /* Backend real:
    const res = await fetch(`${BASE_URL}/treinos`);
    if (!res.ok) throw new Error('Erro ao buscar treinos');
    return res.json();
    */
}

/**
 * Cria um novo treino (chamado ao salvar em NovoTreino.html).
 * POST /api/treinos
 * @param {{ titulo: string, exercicios: Array }} data
 * @returns {Promise<{id, titulo, totalExercicios}>}
 */
async function createTreino(data) {
    console.log('[API] createTreino →', data);
    // Mock
    return { id: Date.now(), titulo: data.titulo, totalExercicios: data.exercicios.length };

    /* Backend real:
    const res = await fetch(`${BASE_URL}/treinos`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error('Erro ao criar treino');
    return res.json();
    */
}

/**
 * Atualiza o título de um treino.
 * PUT /api/treinos/:id
 * @param {number} id
 * @param {{ titulo: string }} data
 */
async function updateTreino(id, data) {
    console.log('[API] updateTreino →', id, data);
    // Mock
    return { id, ...data };

    /* Backend real:
    const res = await fetch(`${BASE_URL}/treinos/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error('Erro ao atualizar treino');
    return res.json();
    */
}

/**
 * Remove um treino e todos os seus exercícios.
 * DELETE /api/treinos/:id
 * @param {number} id
 */
async function deleteTreino(id) {
    console.log('[API] deleteTreino →', id);
    // Mock
    return { success: true };

    /* Backend real:
    const res = await fetch(`${BASE_URL}/treinos/${id}`, { method: 'DELETE' });
    if (!res.ok) throw new Error('Erro ao deletar treino');
    return res.json();
    */
}

// ── EXERCÍCIOS (Linhas dentro do dialog) ────────────────────────

/**
 * Retorna os exercícios de um treino (chamado ao abrir o dialog).
 * GET /api/treinos/:treinoId/exercicios
 * @param {number} treinoId
 * @returns {Promise<Array<{id, nome, series, repeticoes, kg}>>}
 */
async function getExercicios(treinoId) {
    console.log('[API] getExercicios → treino', treinoId);
    // Mock — substitua quando o backend responder esses dados
    return [
        { id: 1, nome: 'Press Inclinado', series: 3, repeticoes: 10, kg: 60 },
        { id: 2, nome: 'Rosca Direta',    series: 3, repeticoes: 10, kg: 60 },
        { id: 3, nome: 'Puxada Frontal',  series: 3, repeticoes: 12, kg: 55 },
    ];

    /* Backend real:
    const res = await fetch(`${BASE_URL}/treinos/${treinoId}/exercicios`);
    if (!res.ok) throw new Error('Erro ao buscar exercícios');
    return res.json();
    */
}

/**
 * Cria um exercício dentro de um treino.
 * POST /api/treinos/:treinoId/exercicios
 * @param {number} treinoId
 * @param {{ nome: string, series: number, repeticoes: number, kg: number }} data
 */
async function createExercicio(treinoId, data) {
    console.log('[API] createExercicio → treino', treinoId, data);
    // Mock
    return { id: Date.now(), ...data };

    /* Backend real:
    const res = await fetch(`${BASE_URL}/treinos/${treinoId}/exercicios`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error('Erro ao criar exercício');
    return res.json();
    */
}

/**
 * Atualiza um exercício (chamado ao salvar no modo edição do dialog).
 * PUT /api/exercicios/:id
 * @param {number} id
 * @param {{ nome?: string, series?: number, repeticoes?: number, kg?: number }} data
 */
async function updateExercicio(id, data) {
    console.log('[API] updateExercicio →', id, data);
    // Mock
    return { id, ...data };

    /* Backend real:
    const res = await fetch(`${BASE_URL}/exercicios/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error('Erro ao atualizar exercício');
    return res.json();
    */
}

/**
 * Remove um exercício (chamado ao clicar em "Excluir" no modo edição).
 * DELETE /api/exercicios/:id
 * @param {number} id
 */
async function deleteExercicio(id) {
    console.log('[API] deleteExercicio →', id);
    // Mock
    return { success: true };

    /* Backend real:
    const res = await fetch(`${BASE_URL}/exercicios/${id}`, { method: 'DELETE' });
    if (!res.ok) throw new Error('Erro ao deletar exercício');
    return res.json();
    */
}
