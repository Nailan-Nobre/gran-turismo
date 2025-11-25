package br.edu.ifpi.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente extends Pessoa {
    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;
    
    @Column(name = "telefone", length = 20)
    private String telefone;
    
    @Column(name = "senha", nullable = false, length = 255)
    private String senha;
    
    // Campos para armazenar método de pagamento
    @Column(name = "tipo_pagamento", length = 50)
    private String tipoPagamento; // "CARTAO" ou "PIX"
    
    @Column(name = "numero_cartao", length = 19)
    private String numeroCartao;
    
    @Column(name = "nome_titular", length = 255)
    private String nomeTitular;
    
    @Column(name = "validade_cartao", length = 5)
    private String validadeCartao;
    
    @Column(name = "cvv", length = 4)
    private String cvv;
    
    @Column(name = "banco", length = 100)
    private String banco;
    
    @Column(name = "agencia", length = 20)
    private String agencia;
    
    @Column(name = "conta", length = 20)
    private String conta;
    
    @Column(name = "chave_pix", length = 255)
    private String chavePix;

    public Cliente() {}

    public Cliente(String nome, String email, String cpf) {
        super(nome, email);
        this.cpf = cpf;
    }
    
    public Cliente(String nome, String email, String cpf, String senha) {
        super(nome, email);
        this.cpf = cpf;
        this.senha = senha;
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
    
    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    // Getters e Setters para método de pagamento
    public String getTipoPagamento() {
        return tipoPagamento;
    }
    
    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
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
    
    public String getValidadeCartao() {
        return validadeCartao;
    }
    
    public void setValidadeCartao(String validadeCartao) {
        this.validadeCartao = validadeCartao;
    }
    
    public String getCvv() {
        return cvv;
    }
    
    public void setCvv(String cvv) {
        this.cvv = cvv;
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
    
    /**
     * Verifica se o cliente já tem método de pagamento configurado
     */
    public boolean temPagamentoConfigurado() {
        return tipoPagamento != null && !tipoPagamento.isEmpty();
    }
    
    /**
     * Salva os dados do método de pagamento no cliente
     */
    public void salvarMetodoPagamento(MetodoPagamento pagamento) {
        if (pagamento instanceof CartaoCredito) {
            CartaoCredito cartao = (CartaoCredito) pagamento;
            this.tipoPagamento = "CARTAO";
            this.numeroCartao = cartao.getNumeroCartao();
            this.nomeTitular = cartao.getNomeTitular();
            this.validadeCartao = cartao.getValidade();
            this.cvv = cartao.getCvv();
            // Limpa dados de PIX
            this.banco = null;
            this.agencia = null;
            this.conta = null;
            this.chavePix = null;
        } else if (pagamento instanceof TransferenciaBancaria) {
            TransferenciaBancaria pix = (TransferenciaBancaria) pagamento;
            this.tipoPagamento = "PIX";
            this.banco = pix.getBanco();
            this.agencia = pix.getAgencia();
            this.conta = pix.getConta();
            this.chavePix = pix.getChavePix();
            // Limpa dados de cartão
            this.numeroCartao = null;
            this.nomeTitular = null;
            this.validadeCartao = null;
            this.cvv = null;
        }
    }
    
    /**
     * Recupera o método de pagamento salvo
     */
    public MetodoPagamento recuperarMetodoPagamento() {
        if (tipoPagamento == null || tipoPagamento.isEmpty()) {
            return null;
        }
        
        if ("CARTAO".equals(tipoPagamento)) {
            return new CartaoCredito(numeroCartao, nomeTitular, validadeCartao, cvv);
        } else if ("PIX".equals(tipoPagamento)) {
            return new TransferenciaBancaria(banco, agencia, conta, chavePix);
        }
        
        return null;
    }
}
