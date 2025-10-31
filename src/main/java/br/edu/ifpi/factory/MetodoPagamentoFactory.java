package br.edu.ifpi.factory;

import br.edu.ifpi.Model.MetodoPagamento;
import br.edu.ifpi.Model.CartaoCredito;
import br.edu.ifpi.Model.TransferenciaBancaria;

/**
 * Factory Method para criação de diferentes métodos de pagamento.
 * Encapsula a lógica de criação de objetos MetodoPagamento.
 */
public class MetodoPagamentoFactory {
    
    /**
     * Cria um método de pagamento baseado no tipo informado
     * 
     * @param tipo Tipo do método de pagamento ("CARTAO" ou "PIX")
     * @param dados Array com os dados necessários para criar o método
     *              - Para CARTAO: [numeroCartao, nomeTitular]
     *              - Para PIX: [chavePix]
     * @return Instância de MetodoPagamento
     * @throws IllegalArgumentException se o tipo for inválido ou dados insuficientes
     */
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
    
    /**
     * Cria um Cartão de Crédito
     */
    public static MetodoPagamento criarCartaoCredito(String numeroCartao, String nomeTitular) {
        return new CartaoCredito(numeroCartao, nomeTitular);
    }
    
    /**
     * Cria uma Transferência Bancária (PIX)
     */
    public static MetodoPagamento criarTransferenciaPix(String chavePix) {
        return new TransferenciaBancaria(chavePix);
    }
}
