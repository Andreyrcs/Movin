package org.movin.model;

public class Exercicio {

    private int id;
    private String nome;
    private int series;
    private int repeticoes;
    private double kg;

    public Exercicio() {
    }

    public Exercicio(String nome, int series, int repeticoes, double kg) {
        this.nome = nome;
        this.series = series;
        this.repeticoes = repeticoes;
        this.kg = kg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(int repeticoes) {
        this.repeticoes = repeticoes;
    }

    public double getKg() {
        return kg;
    }

    public void setKg(double kg) {
        this.kg = kg;
    }
}