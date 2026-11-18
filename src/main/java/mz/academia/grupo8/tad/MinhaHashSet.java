
package mz.academia.grupo8.tad;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação própria de um conjunto genérico
 * utilizando uma tabela de dispersão (Hash Table).
 *
 * As colisões são resolvidas através de encadeamento.
 *
 * @param <T> tipo dos elementos armazenados
 */
public class MinhaHashSet<T> implements Conjunto<T> {

    // Capacidade inicial da tabela
    private static final int CAPACIDADE_INICIAL = 16;

    // Factor de carga máximo antes do redimensionamento
    private static final double FACTOR_CARGA = 0.75;

    // Tabela que armazena as listas de elementos
    private LinkedList<T>[] tabela;

    // Número actual de elementos
    private int tamanho;

    /**
     * Construtor da classe.
     */
    @SuppressWarnings("unchecked")
    public MinhaHashSet() {

        tabela = new LinkedList[CAPACIDADE_INICIAL];
        tamanho = 0;
    }

    /**
     * Calcula a posição de um elemento na tabela.
     */
    private int calcularIndice(T elemento) {

        int hash = elemento.hashCode();

        return (hash & 0x7fffffff) % tabela.length;
    }

    /**
     * Devolve uma lista com todos os elementos do conjunto.
     */
    @Override
    public List<T> listar() {

        List<T> elementos = new ArrayList<>();

        for (LinkedList<T> lista : tabela) {

            if (lista != null) {
                elementos.addAll(lista);
            }
        }

        return elementos;
    }

    /**
     * Adiciona um elemento ao conjunto.
     */
    @Override
    public boolean adicionar(T elemento) {

        if (elemento == null) {
            throw new IllegalArgumentException(
                    "Não é permitido adicionar elementos nulos."
            );
        }

        if (contem(elemento)) {
            return false;
        }

        // Verifica se é necessário aumentar a tabela
        if ((double) (tamanho + 1) / tabela.length > FACTOR_CARGA) {
            redimensionar();
        }

        int indice = calcularIndice(elemento);

        // Cria uma lista na posição, se ainda não existir
        if (tabela[indice] == null) {
            tabela[indice] = new LinkedList<>();
        }

        tabela[indice].add(elemento);
        tamanho++;

        return true;
    }

    /**
     * Remove um elemento do conjunto.
     */
    @Override
    public boolean remover(T elemento) {

        if (elemento == null) {
            return false;
        }

        int indice = calcularIndice(elemento);

        if (tabela[indice] != null) {

            boolean removido = tabela[indice].remove(elemento);

            if (removido) {
                tamanho--;
                return true;
            }
        }

        return false;
    }

    /**
     * Verifica se um elemento existe no conjunto.
     */
    @Override
    public boolean contem(T elemento) {

        if (elemento == null) {
            return false;
        }

        int indice = calcularIndice(elemento);

        return tabela[indice] != null
                && tabela[indice].contains(elemento);
    }

    /**
     * Devolve a quantidade de elementos.
     */
    @Override
    public int tamanho() {

        return tamanho;
    }

    /**
     * Verifica se o conjunto está vazio.
     */
    @Override
    public boolean estaVazio() {

        return tamanho == 0;
    }

    /**
     * Remove todos os elementos.
     */
    @Override
    public void limpar() {

        for (int i = 0; i < tabela.length; i++) {
            tabela[i] = null;
        }

        tamanho = 0;
    }

    /**
     * Aumenta a capacidade da tabela e redistribui
     * os elementos pelas novas posições.
     */
    @SuppressWarnings("unchecked")
    private void redimensionar() {

        LinkedList<T>[] tabelaAntiga = tabela;

        tabela = new LinkedList[tabelaAntiga.length * 2];

        tamanho = 0;

        for (LinkedList<T> lista : tabelaAntiga) {

            if (lista != null) {

                for (T elemento : lista) {
                    adicionar(elemento);
                }
            }
        }
    }
}
