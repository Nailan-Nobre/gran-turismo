package br.edu.ifpi.util;

/**
 * Classe utilitária para aplicar cores ANSI no terminal
 * Fornece constantes para formatação de texto colorido
 */
public class Cores {
    
    // Reset
    public static final String RESET = "\033[0m";
    
    // Cores básicas
    public static final String PRETO = "\033[0;30m";
    public static final String VERMELHO = "\033[0;31m";
    public static final String VERDE = "\033[0;32m";
    public static final String AMARELO = "\033[0;33m";
    public static final String AZUL = "\033[0;34m";
    public static final String ROXO = "\033[0;35m";
    public static final String CIANO = "\033[0;36m";
    public static final String BRANCO = "\033[0;37m";
    
    // Cores brilhantes
    public static final String PRETO_BRILHANTE = "\033[0;90m";
    public static final String VERMELHO_BRILHANTE = "\033[0;91m";
    public static final String VERDE_BRILHANTE = "\033[0;92m";
    public static final String AMARELO_BRILHANTE = "\033[0;93m";
    public static final String AZUL_BRILHANTE = "\033[0;94m";
    public static final String ROXO_BRILHANTE = "\033[0;95m";
    public static final String CIANO_BRILHANTE = "\033[0;96m";
    public static final String BRANCO_BRILHANTE = "\033[0;97m";
    
    // Cores em negrito
    public static final String VERMELHO_NEGRITO = "\033[1;31m";
    public static final String VERDE_NEGRITO = "\033[1;32m";
    public static final String AMARELO_NEGRITO = "\033[1;33m";
    public static final String AZUL_NEGRITO = "\033[1;34m";
    public static final String ROXO_NEGRITO = "\033[1;35m";
    public static final String CIANO_NEGRITO = "\033[1;36m";
    public static final String BRANCO_NEGRITO = "\033[1;37m";
    
    // Fundos
    public static final String FUNDO_PRETO = "\033[40m";
    public static final String FUNDO_VERMELHO = "\033[41m";
    public static final String FUNDO_VERDE = "\033[42m";
    public static final String FUNDO_AMARELO = "\033[43m";
    public static final String FUNDO_AZUL = "\033[44m";
    public static final String FUNDO_ROXO = "\033[45m";
    public static final String FUNDO_CIANO = "\033[46m";
    public static final String FUNDO_BRANCO = "\033[47m";
    
    // Estilos
    public static final String NEGRITO = "\033[1m";
    public static final String SUBLINHADO = "\033[4m";
    public static final String INVERTIDO = "\033[7m";
    
    // Métodos utilitários para facilitar o uso
    
    public static String sucesso(String texto) {
        return VERDE_BRILHANTE + texto + RESET;
    }
    
    public static String erro(String texto) {
        return VERMELHO_BRILHANTE + texto + RESET;
    }
    
    public static String aviso(String texto) {
        return AMARELO_BRILHANTE + texto + RESET;
    }
    
    public static String info(String texto) {
        return CIANO_BRILHANTE + texto + RESET;
    }
    
    public static String titulo(String texto) {
        return AZUL_NEGRITO + texto + RESET;
    }
    
    public static String destaque(String texto) {
        return ROXO_BRILHANTE + texto + RESET;
    }
    
    public static String input(String texto) {
        return AMARELO + texto + RESET;
    }
    
    public static String valor(String texto) {
        return VERDE_NEGRITO + texto + RESET;
    }
    
    public static String separador(String simbolo, int tamanho) {
        return PRETO_BRILHANTE + simbolo.repeat(tamanho) + RESET;
    }
    
    public static String cabecalho(String texto, int largura) {
        return AZUL_NEGRITO + FUNDO_BRANCO + centralizarTexto(texto, largura) + RESET;
    }
    
    private static String centralizarTexto(String texto, int largura) {
        int espacos = (largura - texto.length()) / 2;
        return " ".repeat(Math.max(0, espacos)) + texto + " ".repeat(Math.max(0, espacos));
    }
}
