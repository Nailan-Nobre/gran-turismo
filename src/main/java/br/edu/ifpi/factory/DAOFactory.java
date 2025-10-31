package br.edu.ifpi.factory;

import br.edu.ifpi.dao.*;

/**
 * Factory Method para criação de DAOs.
 * Centraliza a criação de objetos DAO, facilitando a manutenção
 * e permitindo fácil extensão para novos tipos de DAO.
 */
public class DAOFactory {
    
    // Instâncias singleton dos DAOs para reutilização
    private static ClienteDAO clienteDAO;
    private static DestinoDAO destinoDAO;
    private static VooDAO vooDAO;
    private static HospedagemDAO hospedagemDAO;
    
    /**
     * Tipos de DAO disponíveis
     */
    public enum TipoDAO {
        CLIENTE,
        DESTINO,
        VOO,
        HOSPEDAGEM
    }
    
    /**
     * Obtém uma instância de DAO baseado no tipo
     * Implementa padrão Singleton para cada DAO
     * 
     * @param tipo Tipo do DAO desejado
     * @return Instância do DAO
     */
    public static GenericDAO<?, Long> obterDAO(TipoDAO tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de DAO não pode ser nulo");
        }
        
        switch (tipo) {
            case CLIENTE:
                return obterClienteDAO();
            case DESTINO:
                return obterDestinoDAO();
            case VOO:
                return obterVooDAO();
            case HOSPEDAGEM:
                return obterHospedagemDAO();
            default:
                throw new IllegalArgumentException("Tipo de DAO desconhecido: " + tipo);
        }
    }
    
    /**
     * Obtém instância única do ClienteDAO
     */
    public static ClienteDAO obterClienteDAO() {
        if (clienteDAO == null) {
            clienteDAO = new ClienteDAO();
        }
        return clienteDAO;
    }
    
    /**
     * Obtém instância única do DestinoDAO
     */
    public static DestinoDAO obterDestinoDAO() {
        if (destinoDAO == null) {
            destinoDAO = new DestinoDAO();
        }
        return destinoDAO;
    }
    
    /**
     * Obtém instância única do VooDAO
     */
    public static VooDAO obterVooDAO() {
        if (vooDAO == null) {
            vooDAO = new VooDAO();
        }
        return vooDAO;
    }
    
    /**
     * Obtém instância única do HospedagemDAO
     */
    public static HospedagemDAO obterHospedagemDAO() {
        if (hospedagemDAO == null) {
            hospedagemDAO = new HospedagemDAO();
        }
        return hospedagemDAO;
    }
    
    /**
     * Cria uma nova instância de DAO (sem singleton)
     * Útil quando é necessária uma nova instância
     * 
     * @param tipo Tipo do DAO
     * @return Nova instância do DAO
     */
    public static GenericDAO<?, Long> criarNovoDAO(TipoDAO tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de DAO não pode ser nulo");
        }
        
        switch (tipo) {
            case CLIENTE:
                return new ClienteDAO();
            case DESTINO:
                return new DestinoDAO();
            case VOO:
                return new VooDAO();
            case HOSPEDAGEM:
                return new HospedagemDAO();
            default:
                throw new IllegalArgumentException("Tipo de DAO desconhecido: " + tipo);
        }
    }
    
    /**
     * Limpa todas as instâncias singleton
     * Útil para testes ou reinicialização
     */
    public static void limparInstancias() {
        clienteDAO = null;
        destinoDAO = null;
        vooDAO = null;
        hospedagemDAO = null;
    }
    
    /**
     * Fecha o EntityManagerFactory de todos os DAOs
     * Deve ser chamado ao finalizar a aplicação
     */
    public static void fecharConexoes() {
        GenericDAOImpl.fecharEntityManagerFactory();
    }
}
