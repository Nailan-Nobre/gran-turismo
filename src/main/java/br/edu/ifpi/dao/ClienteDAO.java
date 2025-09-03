package br.edu.ifpi.dao;

import br.edu.ifpi.Model.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * DAO específico para a entidade Cliente
 */
public class ClienteDAO extends GenericDAOImpl<Cliente, Long> {
    
    public ClienteDAO() {
        super(Cliente.class);
    }
    
    /**
     * Busca cliente por CPF
     */
    public Cliente buscarPorCpf(String cpf) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT c FROM Cliente c WHERE c.cpf = :cpf";
            TypedQuery<Cliente> query = em.createQuery(jpql, Cliente.class);
            query.setParameter("cpf", cpf);
            List<Cliente> clientes = query.getResultList();
            return clientes.isEmpty() ? null : clientes.get(0);
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca clientes por nome (busca parcial)
     */
    public List<Cliente> buscarPorNome(String nome) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT c FROM Cliente c WHERE c.nome LIKE :nome";
            TypedQuery<Cliente> query = em.createQuery(jpql, Cliente.class);
            query.setParameter("nome", "%" + nome + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca clientes por email
     */
    public Cliente buscarPorEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT c FROM Cliente c WHERE c.email = :email";
            TypedQuery<Cliente> query = em.createQuery(jpql, Cliente.class);
            query.setParameter("email", email);
            List<Cliente> clientes = query.getResultList();
            return clientes.isEmpty() ? null : clientes.get(0);
        } finally {
            em.close();
        }
    }
    
    /**
     * Verifica se já existe um cliente com o CPF informado
     */
    public boolean existeCpf(String cpf) {
        return buscarPorCpf(cpf) != null;
    }
    
    /**
     * Verifica se já existe um cliente com o email informado
     */
    public boolean existeEmail(String email) {
        return buscarPorEmail(email) != null;
    }
}
