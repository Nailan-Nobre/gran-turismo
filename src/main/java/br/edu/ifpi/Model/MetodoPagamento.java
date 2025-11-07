package br.edu.ifpi.Model;

public interface MetodoPagamento {
    boolean processarPagamento(double valor);
    String getDescricao();
    String obterDescricao();
}
