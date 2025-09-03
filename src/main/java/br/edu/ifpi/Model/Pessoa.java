package br.edu.ifpi.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "pessoas")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    
    @Column(name = "email", nullable = false, length = 150)
    private String email;

    // Construtor padrão necessário para JPA
    public Pessoa() {}

    // Métodos get e set
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Pessoa(String nome, String email){
        this.nome = nome;
        this.email = email;
    }

    public void exibirInfo(){
        System.out.println("Nome: "+ nome);
        System.out.println("Email: "+ email);
    }
}