
package mz.academia.grupo8;

import mz.academia.grupo8.tad.MinhaHashSet;

public class TesteHashSet {

    public static void main(String[] args) {

        // Criar um conjunto de números inteiros
        MinhaHashSet<Integer> conjunto = new MinhaHashSet<>();

        System.out.println("=== TESTE DA NOSSA HASHSET ===");

        // TESTE 1: Adicionar elementos
        System.out.println("\n1. ADICIONAR ELEMENTOS");

        System.out.println(conjunto.adicionar(10));
        System.out.println(conjunto.adicionar(20));
        System.out.println(conjunto.adicionar(30));

        System.out.println("Tamanho: " + conjunto.tamanho());

        // TESTE 2: Impedir elementos duplicados
        System.out.println("\n2. TESTAR DUPLICADOS");

        System.out.println("Adicionar 10 novamente: "
                + conjunto.adicionar(10));

        System.out.println("Tamanho: " + conjunto.tamanho());

        // TESTE 3: Procurar elementos
        System.out.println("\n3. PROCURAR ELEMENTOS");

        System.out.println("Contém 20? "
                + conjunto.contem(20));

        System.out.println("Contém 50? "
                + conjunto.contem(50));

        // TESTE 4: Remover elementos
        System.out.println("\n4. REMOVER ELEMENTOS");

        System.out.println("Remover 20: "
                + conjunto.remover(20));

        System.out.println("Contém 20? "
                + conjunto.contem(20));

        System.out.println("Tamanho: " + conjunto.tamanho());

        // TESTE 5: Redimensionamento
        System.out.println("\n5. TESTAR REDIMENSIONAMENTO");

        for (int i = 1; i <= 30; i++) {
            conjunto.adicionar(i);
        }

        System.out.println("Tamanho após inserir mais elementos: "
                + conjunto.tamanho());

        // TESTE 6: Limpar conjunto
        System.out.println("\n6. LIMPAR CONJUNTO");

        conjunto.limpar();

        System.out.println("Está vazio? "
                + conjunto.estaVazio());

        System.out.println("Tamanho final: "
                + conjunto.tamanho());

        System.out.println("\n=== FIM DOS TESTES ===");


// TESTE 6: Colisões
        System.out.println("\n6. TESTAR COLISOES");

        MinhaHashSet<Integer> colisoes = new MinhaHashSet<>();

        colisoes.adicionar(1);
        colisoes.adicionar(17);

        System.out.println("Contém 1? " + colisoes.contem(1));
        System.out.println("Contém 17? " + colisoes.contem(17));

        colisoes.remover(1);

        System.out.println("Contém 1 após remoção? "
                + colisoes.contem(1));

        System.out.println("Contém 17 após remoção? "
                + colisoes.contem(17));

        System.out.println("Tamanho: " + colisoes.tamanho());

    }
}
