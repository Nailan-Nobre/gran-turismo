package br.edu.ifpi;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class TesteConexao {
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            System.out.println("Tentando conectar ao banco de dados...");
            
            // Criar EntityManagerFactory
            emf = Persistence.createEntityManagerFactory("minhaUnidadeDePersistencia");
            
            // Criar EntityManager
            em = emf.createEntityManager();
            
            System.out.println("✅ Conexão com o banco de dados estabelecida com sucesso!");
            
            // Teste simples - executar uma query
            em.createNativeQuery("SELECT 1").getSingleResult();
            System.out.println("✅ Query de teste executada com sucesso!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao conectar com o banco de dados:");
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }
}
