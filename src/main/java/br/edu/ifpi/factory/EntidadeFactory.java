package br.edu.ifpi.factory;

import br.edu.ifpi.Model.Cliente;
import br.edu.ifpi.Model.Destino;

/**
 * Factory Method para criação de entidades do modelo.
 * Centraliza a criação de objetos de domínio com validações.
 */
public class EntidadeFactory {
    
    /**
     * Cria um Cliente com validações
     * 
     * @param nome Nome do cliente
     * @param email Email do cliente
     * @param cpf CPF do cliente (11 dígitos)
     * @return Nova instância de Cliente
     */
    public static Cliente criarCliente(String nome, String email, String cpf) {
        validarString(nome, "Nome");
        validarEmail(email);
        validarCPF(cpf);
        
        return new Cliente(nome, email, cpf);
    }
    
    /**
     * Cria um Destino com validações
     * 
     * @param nome Nome da cidade
     * @param pais Nome do país
     * @return Nova instância de Destino
     */
    public static Destino criarDestino(String nome, String pais) {
        validarString(nome, "Nome do destino");
        validarString(pais, "País");
        
        return new Destino(nome, pais);
    }
    
    // Métodos de validação
    
    private static void validarString(String valor, String nomeCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(nomeCampo + " não pode ser nulo ou vazio");
        }
    }
    
    private static void validarEmail(String email) {
        validarString(email, "Email");
        
        // Validação básica de email
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Email inválido: " + email);
        }
    }
    
    private static void validarCPF(String cpf) {
        if (cpf == null) {
            throw new IllegalArgumentException("CPF não pode ser nulo");
        }
        
        // Remove caracteres não numéricos
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        
        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException(
                "CPF deve conter 11 dígitos. Valor fornecido: " + cpf
            );
        }
        
        // Verifica se todos os dígitos são iguais (CPF inválido)
        if (cpfLimpo.matches("(\\d)\\1{10}")) {
            throw new IllegalArgumentException("CPF inválido: todos os dígitos são iguais");
        }
    }
}
