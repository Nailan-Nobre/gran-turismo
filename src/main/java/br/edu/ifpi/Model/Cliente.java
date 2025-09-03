package br.edu.ifpi.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente extends Pessoa {
    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    // Construtor padrão necessário para JPA
    public Cliente() {}

    public Cliente(String nome, String email, String cpf) {
        super(nome, email);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
