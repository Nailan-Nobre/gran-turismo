package br.edu.ifpi.Model;

public class TransferenciaBancaria implements MetodoPagamento {
    private String banco;
    private String agencia;
    private String conta;
    private String chavePix;

    public TransferenciaBancaria(String chavePix) {
        this.chavePix = chavePix;
    }
    
    public TransferenciaBancaria(String banco, String agencia, String conta, String chavePix) {
        this.banco = banco;
        this.agencia = agencia;
        this.conta = conta;
        this.chavePix = chavePix;
    }

    public boolean processarPagamento(double valor) {
        return true;
    }

    public String obterDescricao() {
        if (banco != null) {
            return "PIX - " + banco + " (Ag: " + agencia + " / Cc: " + conta + ") - Chave: " + chavePix;
        }
        return "Transferência PIX: " + chavePix;
    }
    
    public String getDescricao() {
        return obterDescricao();
    }
    
    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }
}
