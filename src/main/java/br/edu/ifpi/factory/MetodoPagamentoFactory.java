package br.edu.ifpi.factory;

import br.edu.ifpi.Model.MetodoPagamento;
import br.edu.ifpi.Model.CartaoCredito;
import br.edu.ifpi.Model.TransferenciaBancaria;

public class MetodoPagamentoFactory {
    
    public static MetodoPagamento criarMetodoPagamento(String tipo, String... dados) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de pagamento não pode ser nulo");
        }
        
        switch (tipo.toUpperCase()) {
            case "CARTAO":
            case "CARTAO_CREDITO":
                if (dados.length < 2) {
                    throw new IllegalArgumentException(
                        "Dados insuficientes para criar Cartão de Crédito. Necessário: numeroCartao, nomeTitular"
                    );
                }
                return new CartaoCredito(dados[0], dados[1]);
                
            case "PIX":
            case "TRANSFERENCIA":
            case "TRANSFERENCIA_BANCARIA":
                if (dados.length < 1) {
                    throw new IllegalArgumentException(
                        "Dados insuficientes para criar Transferência Bancária. Necessário: chavePix"
                    );
                }
                return new TransferenciaBancaria(dados[0]);
                
            default:
                throw new IllegalArgumentException(
                    "Tipo de pagamento inválido: " + tipo + 
                    ". Tipos válidos: CARTAO, PIX, TRANSFERENCIA"
                );
        }
    }
    
    public static MetodoPagamento criarCartaoCredito(String numeroCartao, String nomeTitular, String validade, String cvv) {
        return new CartaoCredito(numeroCartao, nomeTitular, validade, cvv);
    }
    
    public static MetodoPagamento criarTransferenciaBancaria(String banco, String agencia, String conta, String chavePix) {
        return new TransferenciaBancaria(banco, agencia, conta, chavePix);
    }
    
    public static MetodoPagamento criarTransferenciaPix(String chavePix) {
        return new TransferenciaBancaria(chavePix);
    }
}
