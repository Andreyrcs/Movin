package org.movin.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.movin.dao.ExercicioDAO;
import org.movin.model.Exercicio;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ExercicioController implements HttpHandler {

    private ExercicioDAO exercicioDAO = new ExercicioDAO();
    private Gson gson = new Gson();

    @Override
    public void handle(HttpExchange requisicao) {
        adicionarCors(requisicao);

        try {
            String metodo = requisicao.getRequestMethod();

            if (metodo.equals("OPTIONS")) {
                requisicao.sendResponseHeaders(204, -1);
                return;
            }

            if (metodo.equals("PUT")) {
                atualizarExercicio(requisicao);

            } else if (metodo.equals("DELETE")) {
                excluirExercicio(requisicao);

            } else {
                enviarResposta(
                        requisicao,
                        405,
                        "{\"erro\":\"Metodo nao permitido\"}"
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

    private void atualizarExercicio(HttpExchange requisicao) {
        try {
            int id = pegarId(requisicao);

            Exercicio exercicio =
                    exercicioDAO.buscarPorId(id);

            if (exercicio == null) {
                enviarResposta(
                        requisicao,
                        404,
                        "{\"erro\":\"Exercicio nao encontrado\"}"
                );
                return;
            }

            String json = new String(
                    requisicao.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            JsonObject dados =
                    gson.fromJson(json, JsonObject.class);

            if (dados.has("nome")) {
                exercicio.setNome(
                        dados.get("nome").getAsString()
                );
            }

            if (dados.has("series")) {
                exercicio.setSeries(
                        dados.get("series").getAsInt()
                );
            }

            if (dados.has("repeticoes")) {
                exercicio.setRepeticoes(
                        dados.get("repeticoes").getAsInt()
                );
            }

            if (dados.has("kg")) {
                exercicio.setKg(
                        dados.get("kg").getAsDouble()
                );
            }

            boolean atualizou =
                    exercicioDAO.atualizar(exercicio);

            if (atualizou) {
                enviarResposta(
                        requisicao,
                        200,
                        gson.toJson(exercicio)
                );

            } else {
                enviarResposta(
                        requisicao,
                        500,
                        "{\"erro\":\"Erro ao atualizar exercicio\"}"
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

    private void excluirExercicio(HttpExchange requisicao) {
        try {
            int id = pegarId(requisicao);

            boolean excluiu =
                    exercicioDAO.excluir(id);

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
                        "{\"erro\":\"Exercicio nao encontrado\"}"
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