package br.edu.ifpi.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente extends Pessoa {
    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;
    
    @Column(name = "telefone", length = 20)
    private String telefone;

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
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
