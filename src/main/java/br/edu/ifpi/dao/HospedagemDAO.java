package br.edu.ifpi.dao;

import br.edu.ifpi.Model.Hospedagem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * DAO específico para a entidade Hospedagem
 */
public class HospedagemDAO extends GenericDAOImpl<Hospedagem, Long> {
    
    public HospedagemDAO() {
        super(Hospedagem.class);
    }
    
    /**
     * Busca hospedagens por nome do hotel
     */
    public List<Hospedagem> buscarPorNomeHotel(String nomeHotel) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT h FROM Hospedagem h WHERE h.nomeHotel LIKE :nomeHotel";
            TypedQuery<Hospedagem> query = em.createQuery(jpql, Hospedagem.class);
            query.setParameter("nomeHotel", "%" + nomeHotel + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca hospedagens com preço por noite menor ou igual ao valor informado
     */
    public List<Hospedagem> buscarPorPrecoMaximo(double precoMaximo) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT h FROM Hospedagem h WHERE h.precoPorNoite <= :precoMaximo ORDER BY h.precoPorNoite";
            TypedQuery<Hospedagem> query = em.createQuery(jpql, Hospedagem.class);
            query.setParameter("precoMaximo", precoMaximo);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca hospedagens por número de diárias
     */
    public List<Hospedagem> buscarPorDiarias(int diarias) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT h FROM Hospedagem h WHERE h.diarias = :diarias";
            TypedQuery<Hospedagem> query = em.createQuery(jpql, Hospedagem.class);
            query.setParameter("diarias", diarias);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca hospedagens com custo total menor ou igual ao valor informado
     */
    public List<Hospedagem> buscarPorCustoTotalMaximo(double custoMaximo) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT h FROM Hospedagem h WHERE (h.diarias * h.precoPorNoite) <= :custoMaximo ORDER BY (h.diarias * h.precoPorNoite)";
            TypedQuery<Hospedagem> query = em.createQuery(jpql, Hospedagem.class);
            query.setParameter("custoMaximo", custoMaximo);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Lista todos os nomes de hotéis únicos
     */
    public List<String> listarHoteis() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT DISTINCT h.nomeHotel FROM Hospedagem h ORDER BY h.nomeHotel";
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca hospedagens mais baratas (ordenadas por preço por noite)
     */
    public List<Hospedagem> buscarMaisBaratas(int limite) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT h FROM Hospedagem h ORDER BY h.precoPorNoite ASC";
            TypedQuery<Hospedagem> query = em.createQuery(jpql, Hospedagem.class);
            query.setMaxResults(limite);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
