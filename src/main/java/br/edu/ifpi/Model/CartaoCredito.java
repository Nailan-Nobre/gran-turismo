package br.edu.ifpi.Model;

public class CartaoCredito implements MetodoPagamento {
    private String numeroCartao;
    private String nomeTitular;
    private String validade;
    private String cvv;

    public CartaoCredito(String numeroCartao, String nomeTitular) {
        this.numeroCartao = numeroCartao;
        this.nomeTitular = nomeTitular;
    }
    
    public CartaoCredito(String numeroCartao, String nomeTitular, String validade, String cvv) {
        this.numeroCartao = numeroCartao;
        this.nomeTitular = nomeTitular;
        this.validade = validade;
        this.cvv = cvv;
    }

    public boolean processarPagamento(double valor) {
        return true;
    }

    public String obterDescricao() {
        return "Cartão de Crédito: **** **** **** " + numeroCartao.substring(Math.max(0, numeroCartao.length() - 4)) + 
               " (Titular: " + nomeTitular + ")";
    }
    
    public String getDescricao() {
        return obterDescricao();
    }
    
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
    
    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}