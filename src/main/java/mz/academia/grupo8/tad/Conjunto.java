
package mz.academia.grupo8.tad;

import java.util.List;

/**
 * Interface genérica que define as operações
 * fundamentais de um conjunto.
 *
 * @param <T> tipo dos elementos armazenados
 */
public interface Conjunto<T> {

    /**
     * Adiciona um elemento ao conjunto.
     *
     * @param elemento elemento a adicionar
     * @return true se o elemento foi adicionado;
     *         false se já existia
     */
    boolean adicionar(T elemento);

    /**
     * Remove um elemento do conjunto.
     *
     * @param elemento elemento a remover
     * @return true se foi removido;
     *         false se não existia
     */
    boolean remover(T elemento);

    /**
     * Verifica se um elemento existe no conjunto.
     *
     * @param elemento elemento a procurar
     * @return true se existir; false caso contrário
     */
    boolean contem(T elemento);

    /**
     * Devolve o número de elementos do conjunto.
     *
     * @return quantidade de elementos
     */
    int tamanho();

    /**
     * Verifica se o conjunto está vazio.
     *
     * @return true se estiver vazio
     */
    boolean estaVazio();

    /**
     * Remove todos os elementos do conjunto.
     */
    void limpar();

    /**
     * Devolve uma lista com todos os elementos do conjunto.
     *
     * @return lista dos elementos armazenados
     */
    List<T> listar();
}
