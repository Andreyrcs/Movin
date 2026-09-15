package org.movin.model;

import java.util.List;

public class Treino {

    private int id;
    private String titulo;
    private List<Exercicio> exercicios;

    public Treino() {
    }

    public Treino(String titulo) {
        this.titulo = titulo;
    }

    public Treino(int id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Exercicio> getExercicios() {
        return exercicios;
    }

    public void setExercicios(List<Exercicio> exercicios) {
        this.exercicios = exercicios;
    }
}