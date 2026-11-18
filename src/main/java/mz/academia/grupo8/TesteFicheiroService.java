
package mz.academia.grupo8;

import mz.academia.grupo8.model.Estudante;
import mz.academia.grupo8.persistence.FicheiroService;

import java.util.ArrayList;
import java.util.List;

public class TesteFicheiroService {

    public static void main(String[] args) {

        FicheiroService ficheiroService = new FicheiroService();

        String caminho = "utilizadores_teste.dat";

        // =====================================
        // 1. CRIAR OBJECTOS
        // =====================================

        Estudante estudante1 = new Estudante(
                "EST001",
                "Ana Manuel",
                "841234567",
                "ana@email.com",
                "Engenharia Informática",
                3
        );

        Estudante estudante2 = new Estudante(
                "EST002",
                "Pedro Carlos",
                "821234567",
                "pedro@email.com",
                "Engenharia Informática",
                2
        );

        List<Estudante> estudantes = new ArrayList<>();

        estudantes.add(estudante1);
        estudantes.add(estudante2);

        // =====================================
        // 2. GUARDAR OBJECTOS
        // =====================================

        System.out.println("=== GUARDAR ===");

        ficheiroService.guardar(caminho, estudantes);

        System.out.println("Dados guardados com sucesso!");

        // =====================================
        // 3. VERIFICAR EXISTÊNCIA
        // =====================================

        System.out.println("\n=== VERIFICAR FICHEIRO ===");

        System.out.println(
                "Ficheiro existe: "
                        + ficheiroService.existe(caminho)
        );

        // =====================================
        // 4. RECUPERAR OBJECTOS
        // =====================================

        System.out.println("\n=== CARREGAR ===");

        Object resultado = ficheiroService.carregar(caminho);

        if (resultado instanceof List<?> lista) {

            System.out.println(
                    "Quantidade recuperada: " + lista.size()
            );

            for (Object objeto : lista) {
                System.out.println(objeto);
                System.out.println("--------------------");
            }

        } else {
            System.out.println("Não foi possível recuperar a lista.");
        }
    }
}
