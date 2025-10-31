package br.edu.ifpi.factory;

import br.edu.ifpi.Model.ServicoContratavel;
import br.edu.ifpi.Model.Voo;
import br.edu.ifpi.Model.Hospedagem;
import br.edu.ifpi.Model.Passeio;

/**
 * Factory Method para criação de diferentes serviços contratáveis.
 * Encapsula a lógica de criação de objetos ServicoContratavel.
 */
public class ServicoContratavelFactory {
    
    /**
     * Tipos de serviços disponíveis
     */
    public enum TipoServico {
        VOO,
        HOSPEDAGEM,
        PASSEIO
    }
    
    /**
     * Cria um serviço contratável baseado no tipo
     * 
     * @param tipo Tipo do serviço
     * @param parametros Parâmetros específicos de cada serviço
     * @return Instância de ServicoContratavel
     */
    public static ServicoContratavel criarServico(TipoServico tipo, Object... parametros) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de serviço não pode ser nulo");
        }
        
        switch (tipo) {
            case VOO:
                return criarVoo(parametros);
            case HOSPEDAGEM:
                return criarHospedagem(parametros);
            case PASSEIO:
                return criarPasseio(parametros);
            default:
                throw new IllegalArgumentException("Tipo de serviço desconhecido: " + tipo);
        }
    }
    
    /**
     * Cria um serviço de Voo
     * 
     * @param parametros [companhiaAerea, origem, destino, precoPassagem]
     */
    private static Voo criarVoo(Object... parametros) {
        if (parametros.length < 4) {
            throw new IllegalArgumentException(
                "Dados insuficientes para criar Voo. Necessário: companhiaAerea, origem, destino, precoPassagem"
            );
        }
        
        String companhiaAerea = (String) parametros[0];
        String origem = (String) parametros[1];
        String destino = (String) parametros[2];
        double precoPassagem = ((Number) parametros[3]).doubleValue();
        
        return new Voo(companhiaAerea, origem, destino, precoPassagem);
    }
    
    /**
     * Cria um serviço de Hospedagem
     * 
     * @param parametros [nomeHotel, diarias, precoPorNoite]
     */
    private static Hospedagem criarHospedagem(Object... parametros) {
        if (parametros.length < 3) {
            throw new IllegalArgumentException(
                "Dados insuficientes para criar Hospedagem. Necessário: nomeHotel, diarias, precoPorNoite"
            );
        }
        
        String nomeHotel = (String) parametros[0];
        int diarias = ((Number) parametros[1]).intValue();
        double precoPorNoite = ((Number) parametros[2]).doubleValue();
        
        return new Hospedagem(nomeHotel, diarias, precoPorNoite);
    }
    
    /**
     * Cria um serviço de Passeio
     * 
     * @param parametros [nomePasseio, precoPasseio]
     */
    private static Passeio criarPasseio(Object... parametros) {
        if (parametros.length < 2) {
            throw new IllegalArgumentException(
                "Dados insuficientes para criar Passeio. Necessário: nomePasseio, precoPasseio"
            );
        }
        
        String nomePasseio = (String) parametros[0];
        double precoPasseio = ((Number) parametros[1]).doubleValue();
        
        return new Passeio(nomePasseio, precoPasseio);
    }
    
    // Métodos auxiliares para criar serviços específicos de forma mais intuitiva
    
    /**
     * Cria um Voo com validações
     */
    public static Voo criarVoo(String companhiaAerea, String origem, String destino, double precoPassagem) {
        validarString(companhiaAerea, "Companhia Aérea");
        validarString(origem, "Origem");
        validarString(destino, "Destino");
        validarPreco(precoPassagem, "Preço da Passagem");
        
        return new Voo(companhiaAerea, origem, destino, precoPassagem);
    }
    
    /**
     * Cria uma Hospedagem com validações
     */
    public static Hospedagem criarHospedagem(String nomeHotel, int diarias, double precoPorNoite) {
        validarString(nomeHotel, "Nome do Hotel");
        
        if (diarias <= 0) {
            throw new IllegalArgumentException("Número de diárias deve ser maior que zero");
        }
        
        validarPreco(precoPorNoite, "Preço por Noite");
        
        return new Hospedagem(nomeHotel, diarias, precoPorNoite);
    }
    
    /**
     * Cria um Passeio com validações
     */
    public static Passeio criarPasseio(String nomePasseio, double precoPasseio) {
        validarString(nomePasseio, "Nome do Passeio");
        validarPreco(precoPasseio, "Preço do Passeio");
        
        return new Passeio(nomePasseio, precoPasseio);
    }
    
    // Métodos de validação auxiliares
    
    private static void validarString(String valor, String nomeCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(nomeCampo + " não pode ser nulo ou vazio");
        }
    }
    
    private static void validarPreco(double preco, String nomeCampo) {
        if (preco < 0) {
            throw new IllegalArgumentException(nomeCampo + " não pode ser negativo");
        }
    }
}
