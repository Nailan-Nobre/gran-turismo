package br.edu.ifpi.factory;

import br.edu.ifpi.dao.*;

/**
 * Factory para criação e gerenciamento de DAOs
 * Implementa o padrão Singleton para garantir instâncias únicas de cada DAO
 */
public class DAOFactory {
    
    // Instância única da factory
    private static volatile DAOFactory instance;
    
    private static ClienteDAO clienteDAO;
    private static DestinoDAO destinoDAO;
    private static VooDAO vooDAO;
    private static HospedagemDAO hospedagemDAO;
    
    /**
     * Construtor privado para prevenir instanciação externa
     */
    private DAOFactory() {
        // Construtor vazio - DAOs são criados sob demanda
    }
    
    /**
     * Método estático para obter a instância única da factory
     * Implementação thread-safe usando Double-Checked Locking
     * @return A instância única de DAOFactory
     */
    public static DAOFactory getInstance() {
        if (instance == null) {
            synchronized (DAOFactory.class) {
                if (instance == null) {
                    instance = new DAOFactory();
                }
            }
        }
        return instance;
    }
    
    public enum TipoDAO {
        CLIENTE,
        DESTINO,
        VOO,
        HOSPEDAGEM
    }
    
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
    
    public static ClienteDAO obterClienteDAO() {
        if (clienteDAO == null) {
            clienteDAO = new ClienteDAO();
        }
        return clienteDAO;
    }
    
    public static ClienteDAO getClienteDAO() {
        return obterClienteDAO();
    }
    
    public static DestinoDAO obterDestinoDAO() {
        if (destinoDAO == null) {
            destinoDAO = new DestinoDAO();
        }
        return destinoDAO;
    }
    
    public static DestinoDAO getDestinoDAO() {
        return obterDestinoDAO();
    }
    
    public static VooDAO obterVooDAO() {
        if (vooDAO == null) {
            vooDAO = new VooDAO();
        }
        return vooDAO;
    }
    
    public static VooDAO getVooDAO() {
        return obterVooDAO();
    }
    
    public static HospedagemDAO obterHospedagemDAO() {
        if (hospedagemDAO == null) {
            hospedagemDAO = new HospedagemDAO();
        }
        return hospedagemDAO;
    }
    
    public static HospedagemDAO getHospedagemDAO() {
        return obterHospedagemDAO();
    }
    
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
    
    public static void limparInstancias() {
        clienteDAO = null;
        destinoDAO = null;
        vooDAO = null;
        hospedagemDAO = null;
    }
    
    public static void fecharConexoes() {
        GenericDAOImpl.fecharEntityManagerFactory();
    }
}
