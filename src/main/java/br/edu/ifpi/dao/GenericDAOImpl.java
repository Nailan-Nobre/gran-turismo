package br.edu.ifpi.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Implementação base para DAOs usando JPA
 * @param <T> Tipo da entidade
 * @param <ID> Tipo da chave primária
 */
public abstract class GenericDAOImpl<T, ID> implements GenericDAO<T, ID> {
    
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("minhaUnidadeDePersistencia");
    private Class<T> entityClass;
    
    public GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    @Override
    public void salvar(T entidade) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entidade);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao salvar entidade: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public void atualizar(T entidade) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entidade);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao atualizar entidade: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public T buscarPorId(ID id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<T> listarTodos() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            TypedQuery<T> query = em.createQuery(jpql, entityClass);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public void remover(T entidade) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T managedEntity = em.merge(entidade);
            em.remove(managedEntity);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao remover entidade: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public void removerPorId(ID id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T entidade = em.find(entityClass, id);
            if (entidade != null) {
                em.remove(entidade);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao remover entidade por ID: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    /**
     * Método utilitário para fechar o EntityManagerFactory
     */
    public static void fecharEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
