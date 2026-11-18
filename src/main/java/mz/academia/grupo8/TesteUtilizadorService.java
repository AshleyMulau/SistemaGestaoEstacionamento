
package mz.academia.grupo8;

import mz.academia.grupo8.model.Estudante;
import mz.academia.grupo8.model.DocenteFuncionario;
import mz.academia.grupo8.model.Utilizador;
import mz.academia.grupo8.service.UtilizadorService;

public class TesteUtilizadorService {

    public static void main(String[] args) {

        UtilizadorService servico = new UtilizadorService();

        // Criar um estudante
        Estudante estudante = new Estudante(
                "EST001",
                "Ana Manuel",
                "841234567",
                "ana@email.com",
                "Engenharia Informática",
                3
        );

        // Criar um docente
        DocenteFuncionario docente = new DocenteFuncionario(
                "DOC001",
                "Carlos Alberto",
                "821234567",
                "carlos@email.com",
                "Departamento de Informática",
                "Docente"
        );

        // Registar utilizadores
        System.out.println("=== REGISTO ===");

        System.out.println("Estudante: "
                + servico.registar(estudante));

        System.out.println("Docente: "
                + servico.registar(docente));

        // Tentar registar código duplicado
        System.out.println("\n=== DUPLICADOS ===");

        Estudante duplicado = new Estudante(
                "EST001",
                "Outro Estudante",
                "871234567",
                "outro@email.com",
                "Matemática",
                2
        );

        System.out.println("Duplicado: "
                + servico.registar(duplicado));

        // Procurar utilizador
        System.out.println("\n=== PESQUISA ===");

        Utilizador encontrado =
                servico.procurarPorCodigo("EST001");

        System.out.println(encontrado);

        // Listar utilizadores
        System.out.println("\n=== LISTAGEM ===");

        for (Utilizador utilizador : servico.listarTodos()) {
            System.out.println("\n" + utilizador);
        }

        // Actualizar utilizador
        System.out.println("\n=== ACTUALIZAÇÃO ===");

        System.out.println("Actualizado: "
                + servico.actualizar(
                "EST001",
                "Ana Manuel Actualizada",
                "849999999",
                "ana.nova@email.com"
        ));

        // Remover utilizador
        System.out.println("\n=== REMOÇÃO ===");

        System.out.println("Removido: "
                + servico.remover("DOC001"));

        System.out.println("Quantidade final: "
                + servico.quantidade());
    }
}
