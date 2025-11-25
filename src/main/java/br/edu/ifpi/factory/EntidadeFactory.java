package br.edu.ifpi.factory;

import br.edu.ifpi.Model.Cliente;
import br.edu.ifpi.Model.Destino;
import br.edu.ifpi.util.Validador;

public class EntidadeFactory {
    
    public static Cliente criarCliente(String nome, String email, String cpf) {
        validarString(nome, "Nome");
        
        if (!Validador.validarEmail(email)) {
            throw new IllegalArgumentException("Email inválido: " + email);
        }
        
        if (!Validador.validarCPF(cpf)) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }
        
        return new Cliente(nome, email, cpf);
    }
    
    public static Cliente criarCliente(String nome, String email, String cpf, String senha) {
        validarString(nome, "Nome");
        validarString(senha, "Senha");
        
        if (!Validador.validarEmail(email)) {
            throw new IllegalArgumentException("Email inválido: " + email);
        }
        
        if (!Validador.validarCPF(cpf)) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }
        
        if (senha.length() < 6) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres");
        }
        
        return new Cliente(nome, email, cpf, senha);
    }
    
    public static Destino criarDestino(String nome, String pais) {
        validarString(nome, "Nome do destino");
        validarString(pais, "País");
        
        return new Destino(nome, pais);
    }
    
    private static void validarString(String valor, String nomeCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(nomeCampo + " não pode ser nulo ou vazio");
        }
    }
}
