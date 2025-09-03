package br.edu.ifpi.dao;

import java.util.List;

/**
 * Interface genérica para operações CRUD
 * @param <T> Tipo da entidade
 * @param <ID> Tipo da chave primária
 */
public interface GenericDAO<T, ID> {
    
    /**
     * Salva uma nova entidade
     */
    void salvar(T entidade);
    
    /**
     * Atualiza uma entidade existente
     */
    void atualizar(T entidade);
    
    /**
     * Busca uma entidade por ID
     */
    T buscarPorId(ID id);
    
    /**
     * Lista todas as entidades
     */
    List<T> listarTodos();
    
    /**
     * Remove uma entidade
     */
    void remover(T entidade);
    
    /**
     * Remove uma entidade por ID
     */
    void removerPorId(ID id);
}
