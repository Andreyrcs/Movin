package org.movin;

import com.sun.net.httpserver.HttpServer;
import org.movin.controller.ExercicioController;
import org.movin.controller.TreinoController;

import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) {

        try {
            HttpServer servidor =
                    HttpServer.create(
                            new InetSocketAddress(3000),
                            0
                    );

            servidor.createContext(
                    "/api/treinos",
                    new TreinoController()
            );

            servidor.createContext(
                    "/api/exercicios",
                    new ExercicioController()
            );

            servidor.start();

            System.out.println(
                    "Servidor iniciado na porta 3000."
            );

        } catch (Exception erro) {
            System.out.println(
                    "Erro ao iniciar servidor."
            );

            System.out.println(
                    erro.getMessage()
            );
        }
    }
}