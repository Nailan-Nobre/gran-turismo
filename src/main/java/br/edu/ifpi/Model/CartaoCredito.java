package br.edu.ifpi.Model;

public class CartaoCredito implements MetodoPagamento {
    private String numeroCartao;
    private String nomeTitular;

    public CartaoCredito(String numeroCartao, String nomeTitular) {
        this.numeroCartao = numeroCartao;
        this.nomeTitular = nomeTitular;
    }

    public boolean processarPagamento(double valor) {
        return true; // Simula aprovação
    }

    public String getDescricao() {
        return "Cartão de Crédito: " + numeroCartao + " (Titular: " + nomeTitular + ")";
    }
    
    // Getters e Setters
    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }
}