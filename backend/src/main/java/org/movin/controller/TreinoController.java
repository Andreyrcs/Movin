package org.movin.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.movin.dao.ExercicioDAO;
import org.movin.dao.TreinoDAO;
import org.movin.model.Exercicio;
import org.movin.model.Treino;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TreinoController implements HttpHandler {

    private TreinoDAO treinoDAO = new TreinoDAO();
    private ExercicioDAO exercicioDAO = new ExercicioDAO();
    private Gson gson = new Gson();

    @Override
    public void handle(HttpExchange requisicao) {
        adicionarCors(requisicao);

        try {
            String metodo = requisicao.getRequestMethod();
            String caminho = requisicao.getRequestURI().getPath();

            if (metodo.equals("OPTIONS")) {
                requisicao.sendResponseHeaders(204, -1);
                return;
            }

            if (metodo.equals("GET") && caminho.equals("/api/treinos")) {
                listarTreinos(requisicao);

            } else if (metodo.equals("POST") && caminho.equals("/api/treinos")) {
                criarTreino(requisicao);

            } else if (metodo.equals("GET") && caminho.endsWith("/exercicios")) {
                listarExercicios(requisicao);

            } else if (metodo.equals("POST") && caminho.endsWith("/exercicios")) {
                criarExercicio(requisicao);

            } else if (metodo.equals("PUT")) {
                atualizarTreino(requisicao);

            } else if (metodo.equals("DELETE")) {
                excluirTreino(requisicao);

            } else {
                enviarResposta(
                        requisicao,
                        405,
                        "{\"erro\":\"Metodo nao permitido\"}"
                );
            }

        } catch (Exception erro) {
            System.out.println("Erro no TreinoController.");
            System.out.println(erro.getMessage());

            enviarResposta(
                    requisicao,
                    400,
                    "{\"erro\":\"Dados invalidos\"}"
            );
        }
    }

    private void listarTreinos(HttpExchange requisicao) {
        List<Treino> treinos = treinoDAO.listar();

        JsonArray resposta = new JsonArray();

        for (Treino treino : treinos) {
            JsonObject item = new JsonObject();

            item.addProperty("id", treino.getId());
            item.addProperty("titulo", treino.getTitulo());
            item.addProperty(
                    "totalExercicios",
                    treinoDAO.contarExercicios(treino.getId())
            );

            resposta.add(item);
        }

        enviarResposta(
                requisicao,
                200,
                gson.toJson(resposta)
        );
    }

    private void criarTreino(HttpExchange requisicao) {
        try {
            String json = new String(
                    requisicao.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            Treino treino = gson.fromJson(json, Treino.class);

            boolean salvouTreino = treinoDAO.salvar(treino);

            if (!salvouTreino) {
                enviarResposta(
                        requisicao,
                        500,
                        "{\"erro\":\"Erro ao salvar treino\"}"
                );
                return;
            }

            int totalExercicios = 0;

            if (treino.getExercicios() != null) {

                for (Exercicio exercicio : treino.getExercicios()) {

                    boolean salvouExercicio =
                            exercicioDAO.salvar(
                                    treino.getId(),
                                    exercicio
                            );

                    if (salvouExercicio) {
                        totalExercicios++;
                    }
                }
            }

            JsonObject resposta = new JsonObject();

            resposta.addProperty("id", treino.getId());
            resposta.addProperty("titulo", treino.getTitulo());
            resposta.addProperty(
                    "totalExercicios",
                    totalExercicios
            );

            enviarResposta(
                    requisicao,
                    201,
                    gson.toJson(resposta)
            );

        } catch (Exception erro) {
            enviarResposta(
                    requisicao,
                    400,
                    "{\"erro\":\"Dados invalidos\"}"
            );
        }
    }

    private void atualizarTreino(HttpExchange requisicao) {
        try {
            int id = pegarId(requisicao);

            String json = new String(
                    requisicao.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            Treino treino = gson.fromJson(json, Treino.class);

            treino.setId(id);

            boolean atualizou = treinoDAO.atualizar(treino);

            if (atualizou) {
                JsonObject resposta = new JsonObject();

                resposta.addProperty("id", treino.getId());
                resposta.addProperty(
                        "titulo",
                        treino.getTitulo()
                );

                enviarResposta(
                        requisicao,
                        200,
                        gson.toJson(resposta)
                );

            } else {
                enviarResposta(
                        requisicao,
                        404,
                        "{\"erro\":\"Treino nao encontrado\"}"
                );
            }

        } catch (Exception erro) {
            enviarResposta(
                    requisicao,
                    400,
                    "{\"erro\":\"Dados invalidos\"}"
            );
        }
    }

    private void excluirTreino(HttpExchange requisicao) {
        try {
            int id = pegarId(requisicao);

            boolean excluiu = treinoDAO.excluir(id);

            if (excluiu) {
                enviarResposta(
                        requisicao,
                        200,
                        "{\"success\":true}"
                );

            } else {
                enviarResposta(
                        requisicao,
                        404,
                        "{\"erro\":\"Treino nao encontrado\"}"
                );
            }

        } catch (Exception erro) {
            enviarResposta(
                    requisicao,
                    400,
                    "{\"erro\":\"ID invalido\"}"
            );
        }
    }

    private void listarExercicios(HttpExchange requisicao) {
        try {
            int idTreino = pegarId(requisicao);

            List<Exercicio> exercicios =
                    exercicioDAO.listarPorTreino(idTreino);

            enviarResposta(
                    requisicao,
                    200,
                    gson.toJson(exercicios)
            );

        } catch (Exception erro) {
            enviarResposta(
                    requisicao,
                    400,
                    "{\"erro\":\"ID invalido\"}"
            );
        }
    }

    private void criarExercicio(HttpExchange requisicao) {
        try {
            int idTreino = pegarId(requisicao);

            String json = new String(
                    requisicao.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            Exercicio exercicio =
                    gson.fromJson(json, Exercicio.class);

            boolean salvou =
                    exercicioDAO.salvar(
                            idTreino,
                            exercicio
                    );

            if (salvou) {
                enviarResposta(
                        requisicao,
                        201,
                        gson.toJson(exercicio)
                );

            } else {
                enviarResposta(
                        requisicao,
                        500,
                        "{\"erro\":\"Erro ao salvar exercicio\"}"
                );
            }

        } catch (Exception erro) {
            enviarResposta(
                    requisicao,
                    400,
                    "{\"erro\":\"Dados invalidos\"}"
            );
        }
    }

    private int pegarId(HttpExchange requisicao) {
        String caminho =
                requisicao.getRequestURI().getPath();

        String[] partes = caminho.split("/");

        return Integer.parseInt(partes[3]);
    }

    private void adicionarCors(HttpExchange requisicao) {
        requisicao.getResponseHeaders().set(
                "Access-Control-Allow-Origin",
                "*"
        );

        requisicao.getResponseHeaders().set(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS"
        );

        requisicao.getResponseHeaders().set(
                "Access-Control-Allow-Headers",
                "Content-Type"
        );
    }

    private void enviarResposta(
            HttpExchange requisicao,
            int status,
            String resposta
    ) {
        try {
            byte[] dados =
                    resposta.getBytes(StandardCharsets.UTF_8);

            requisicao.getResponseHeaders().set(
                    "Content-Type",
                    "application/json; charset=UTF-8"
            );

            requisicao.sendResponseHeaders(
                    status,
                    dados.length
            );

            OutputStream saida =
                    requisicao.getResponseBody();

            saida.write(dados);
            saida.close();

        } catch (Exception erro) {
            System.out.println(
                    "Erro ao enviar resposta."
            );
        }
    }
}