package br.com.infnet.model;

public class Funcionario {

    // Identificador único do funcionário
    private int id;

    // Nome do funcionário
    private String nome;

    // Cargo ocupacional do funcionário
    private String cargo;

    // Construtor responsável por inicializar o objeto com id, nome e cargo
    public Funcionario(int id, String nome, String cargo) {
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
    }

    // Retorna o identificador do funcionário
    public int getId() {
        return id;
    }

    // Define ou altera o identificador do funcionário
    public void setId(int id) {
        this.id = id;
    }

    // Retorna o nome do funcionário
    public String getNome() {
        return nome;
    }

    // Define ou altera o nome do funcionário
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Retorna o cargo ocupado pelo funcionário
    public String getCargo() {
        return cargo;
    }

    // Define ou altera o cargo ocupado pelo funcionário
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

}