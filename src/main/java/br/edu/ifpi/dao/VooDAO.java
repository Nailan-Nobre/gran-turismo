package br.edu.ifpi.dao;

import br.edu.ifpi.Model.Voo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * DAO específico para a entidade Voo
 */
public class VooDAO extends GenericDAOImpl<Voo, Long> {
    
    public VooDAO() {
        super(Voo.class);
    }
    
    /**
     * Busca voos por origem
     */
    public List<Voo> buscarPorOrigem(String origem) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT v FROM Voo v WHERE v.origem LIKE :origem";
            TypedQuery<Voo> query = em.createQuery(jpql, Voo.class);
            query.setParameter("origem", "%" + origem + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca voos por destino
     */
    public List<Voo> buscarPorDestino(String destino) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT v FROM Voo v WHERE v.destinoVoo LIKE :destino";
            TypedQuery<Voo> query = em.createQuery(jpql, Voo.class);
            query.setParameter("destino", "%" + destino + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca voos por origem e destino
     */
    public List<Voo> buscarPorOrigemEDestino(String origem, String destino) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT v FROM Voo v WHERE v.origem LIKE :origem AND v.destinoVoo LIKE :destino";
            TypedQuery<Voo> query = em.createQuery(jpql, Voo.class);
            query.setParameter("origem", "%" + origem + "%");
            query.setParameter("destino", "%" + destino + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca voos por companhia aérea
     */
    public List<Voo> buscarPorCompanhia(String companhia) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT v FROM Voo v WHERE v.companhiaAerea LIKE :companhia";
            TypedQuery<Voo> query = em.createQuery(jpql, Voo.class);
            query.setParameter("companhia", "%" + companhia + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca voos com preço menor ou igual ao valor informado
     */
    public List<Voo> buscarPorPrecoMaximo(double precoMaximo) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT v FROM Voo v WHERE v.precoPassagem <= :precoMaximo ORDER BY v.precoPassagem";
            TypedQuery<Voo> query = em.createQuery(jpql, Voo.class);
            query.setParameter("precoMaximo", precoMaximo);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Lista todas as companhias aéreas únicas
     */
    public List<String> listarCompanhias() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT DISTINCT v.companhiaAerea FROM Voo v ORDER BY v.companhiaAerea";
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
