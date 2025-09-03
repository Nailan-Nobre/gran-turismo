package br.edu.ifpi.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "hospedagens")
public class Hospedagem implements ServicoContratavel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nome_hotel", nullable = false, length = 100)
    private String nomeHotel;
    
    @Column(name = "diarias", nullable = false)
    private int diarias;
    
    @Column(name = "preco_por_noite", nullable = false)
    private double precoPorNoite;

    // Construtor padrão necessário para JPA
    public Hospedagem() {}

    public Hospedagem(String nomeHotel, int diarias, double precoPorNoite) {
        this.nomeHotel = nomeHotel;
        this.diarias = diarias;
        this.precoPorNoite = precoPorNoite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeHotel() {
        return nomeHotel;
    }

    public void setNomeHotel(String nomeHotel) {
        this.nomeHotel = nomeHotel;
    }

    public int getDiarias() {
        return diarias;
    }

    public void setDiarias(int diarias) {
        this.diarias = diarias;
    }

    public double getPrecoPorNoite() {
        return precoPorNoite;
    }

    public void setPrecoPorNoite(double precoPorNoite) {
        this.precoPorNoite = precoPorNoite;
    }

    @Override
    public String getDescricaoServico() {
        return "Hospedagem em " + nomeHotel + " por " + diarias + " noites";
    }

    @Override
    public double getPreco() {
        return diarias * precoPorNoite;
    }
}
