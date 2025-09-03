package br.edu.ifpi.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "voos")
public class Voo implements ServicoContratavel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "companhia_aerea", nullable = false, length = 100)
    private String companhiaAerea;
    
    @Column(name = "origem", nullable = false, length = 100)
    private String origem;
    
    @Column(name = "destino_voo", nullable = false, length = 100)
    private String destinoVoo;
    
    @Column(name = "preco_passagem", nullable = false)
    private double precoPassagem;

    // Construtor padrão necessário para JPA
    public Voo() {}

    public Voo(String companhiaAerea, String origem, String destinoVoo, double precoPassagem) {
        this.companhiaAerea = companhiaAerea;
        this.origem = origem;
        this.destinoVoo = destinoVoo;
        this.precoPassagem = precoPassagem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanhiaAerea() {
        return companhiaAerea;
    }

    public void setCompanhiaAerea(String companhiaAerea) {
        this.companhiaAerea = companhiaAerea;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestinoVoo() {
        return destinoVoo;
    }

    public void setDestinoVoo(String destinoVoo) {
        this.destinoVoo = destinoVoo;
    }

    public double getPrecoPassagem() {
        return precoPassagem;
    }

    public void setPrecoPassagem(double precoPassagem) {
        this.precoPassagem = precoPassagem;
    }

    @Override
    public String getDescricaoServico() {
        return "Voo com " + companhiaAerea + " de " + origem + " para " + destinoVoo;
    }

    @Override
    public double getPreco() {
        return precoPassagem;
    }
}
