package br.edu.ifpi.util;

import java.util.regex.Pattern;

public class Validador {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern TELEFONE_PATTERN = Pattern.compile(
        "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$"
    );
    
    private static final Pattern NUMERO_CARTAO_PATTERN = Pattern.compile(
        "^\\d{13,19}$"
    );
    
    private static final Pattern CVV_PATTERN = Pattern.compile(
        "^\\d{3,4}$"
    );
    
    private static final Pattern VALIDADE_PATTERN = Pattern.compile(
        "^(0[1-9]|1[0-2])/\\d{2}$"
    );
    
    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    public static boolean validarCPF(String cpf) {
        if (cpf == null) {
            return false;
        }
        
        cpf = cpf.replaceAll("[^0-9]", "");
        
        if (cpf.length() != 11) {
            return false;
        }
        
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        try {
            int[] numeros = new int[11];
            for (int i = 0; i < 11; i++) {
                numeros[i] = Integer.parseInt(String.valueOf(cpf.charAt(i)));
            }
            
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += numeros[i] * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) {
                primeiroDigito = 0;
            }
            if (numeros[9] != primeiroDigito) {
                return false;
            }
            
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += numeros[i] * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) {
                segundoDigito = 0;
            }
            
            return numeros[10] == segundoDigito;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean validarTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }
        return TELEFONE_PATTERN.matcher(telefone.trim()).matches();
    }
    
    public static boolean validarNumeroCartao(String numeroCartao) {
        if (numeroCartao == null) {
            return false;
        }
        String numero = numeroCartao.replaceAll("[^0-9]", "");
        return NUMERO_CARTAO_PATTERN.matcher(numero).matches();
    }
    
    public static boolean validarCVV(String cvv) {
        if (cvv == null) {
            return false;
        }
        return CVV_PATTERN.matcher(cvv.trim()).matches();
    }
    
    public static boolean validarValidade(String validade) {
        if (validade == null) {
            return false;
        }
        if (!VALIDADE_PATTERN.matcher(validade.trim()).matches()) {
            return false;
        }
        
        try {
            String[] partes = validade.split("/");
            int mes = Integer.parseInt(partes[0]);
            int ano = Integer.parseInt(partes[1]) + 2000;
            
            int anoAtual = java.time.Year.now().getValue();
            int mesAtual = java.time.MonthDay.now().getMonthValue();
            
            if (ano < anoAtual) {
                return false;
            }
            if (ano == anoAtual && mes < mesAtual) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static String formatarCPF(String cpf) {
        if (cpf == null) {
            return "";
        }
        String numeros = cpf.replaceAll("[^0-9]", "");
        if (numeros.length() == 11) {
            return numeros.substring(0, 3) + "." + 
                   numeros.substring(3, 6) + "." + 
                   numeros.substring(6, 9) + "-" + 
                   numeros.substring(9, 11);
        }
        return cpf;
    }
    
    public static String formatarTelefone(String telefone) {
        if (telefone == null) {
            return "";
        }
        String numeros = telefone.replaceAll("[^0-9]", "");
        if (numeros.length() == 11) {
            return "(" + numeros.substring(0, 2) + ") " + 
                   numeros.substring(2, 7) + "-" + 
                   numeros.substring(7, 11);
        } else if (numeros.length() == 10) {
            return "(" + numeros.substring(0, 2) + ") " + 
                   numeros.substring(2, 6) + "-" + 
                   numeros.substring(6, 10);
        }
        return telefone;
    }
    
    public static String limparNumeros(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replaceAll("[^0-9]", "");
    }
}
