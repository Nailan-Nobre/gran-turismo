package br.edu.ifpi.dao;

import br.edu.ifpi.Model.Destino;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * DAO específico para a entidade Destino
 */
public class DestinoDAO extends GenericDAOImpl<Destino, Long> {
    
    public DestinoDAO() {
        super(Destino.class);
    }
    
    /**
     * Busca destinos por país
     */
    public List<Destino> buscarPorPais(String pais) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT d FROM Destino d WHERE d.pais LIKE :pais";
            TypedQuery<Destino> query = em.createQuery(jpql, Destino.class);
            query.setParameter("pais", "%" + pais + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca destinos por nome da cidade
     */
    public List<Destino> buscarPorNome(String nome) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT d FROM Destino d WHERE d.nome LIKE :nome";
            TypedQuery<Destino> query = em.createQuery(jpql, Destino.class);
            query.setParameter("nome", "%" + nome + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca destino específico por nome e país
     */
    public Destino buscarPorNomeEPais(String nome, String pais) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT d FROM Destino d WHERE d.nome = :nome AND d.pais = :pais";
            TypedQuery<Destino> query = em.createQuery(jpql, Destino.class);
            query.setParameter("nome", nome);
            query.setParameter("pais", pais);
            List<Destino> destinos = query.getResultList();
            return destinos.isEmpty() ? null : destinos.get(0);
        } finally {
            em.close();
        }
    }
    
    /**
     * Lista todos os países únicos
     */
    public List<String> listarPaises() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT DISTINCT d.pais FROM Destino d ORDER BY d.pais";
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
