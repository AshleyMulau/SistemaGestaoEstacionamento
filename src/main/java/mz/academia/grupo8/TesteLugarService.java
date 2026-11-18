
package mz.academia.grupo8;

import mz.academia.grupo8.model.EstadoLugar;
import mz.academia.grupo8.model.Lugar;
import mz.academia.grupo8.model.TipoLugar;
import mz.academia.grupo8.service.LugarService;

public class TesteLugarService {

    public static void main(String[] args) {

        LugarService servico = new LugarService();

        // Criar lugares
        Lugar l1 = new Lugar(
                1, "Bloco A", TipoLugar.ESTUDANTE
        );

        Lugar l2 = new Lugar(
                2, "Bloco A", TipoLugar.ESTUDANTE
        );

        Lugar l3 = new Lugar(
                3, "Bloco B", TipoLugar.DOCENTE_FUNCIONARIO
        );

        Lugar l4 = new Lugar(
                4, "Bloco B", TipoLugar.DOCENTE_FUNCIONARIO
        );

        System.out.println("=== REGISTO ===");
        System.out.println("Lugar 1: " + servico.registar(l1));
        System.out.println("Lugar 2: " + servico.registar(l2));
        System.out.println("Lugar 3: " + servico.registar(l3));
        System.out.println("Lugar 4: " + servico.registar(l4));

        System.out.println("\n=== DUPLICADOS ===");

        Lugar duplicado = new Lugar(
                1, "Bloco C", TipoLugar.ESTUDANTE
        );

        System.out.println(
                "Número duplicado: " + servico.registar(duplicado)
        );

        System.out.println("\n=== PESQUISA ===");
        System.out.println(servico.procurarPorNumero(2));

        System.out.println("\n=== TODOS OS LUGARES ===");
        servico.listarTodos().forEach(System.out::println);

        System.out.println("\n=== LUGARES LIVRES ===");
        servico.listarLivres().forEach(System.out::println);

        // Simular ocupação do lugar 3
        l3.setEstado(EstadoLugar.OCUPADO);

        System.out.println("\n=== LUGARES OCUPADOS ===");
        servico.listarOcupados().forEach(System.out::println);

        System.out.println("\n=== CONTAGEM ===");
        System.out.println("Livres: " + servico.contarLivres());
        System.out.println("Ocupados: " + servico.contarOcupados());
        System.out.println("Total: " + servico.quantidade());

        System.out.println("\n=== ACTUALIZAÇÃO ===");

        System.out.println(
                "Lugar livre actualizado: "
                        + servico.actualizar(
                        1,
                        "Bloco C",
                        TipoLugar.ESTUDANTE
                )
        );

        System.out.println(
                "Lugar ocupado actualizado: "
                        + servico.actualizar(
                        3,
                        "Bloco D",
                        TipoLugar.DOCENTE_FUNCIONARIO
                )
        );

        System.out.println("\n=== REMOÇÃO ===");

        System.out.println(
                "Remover lugar livre: " + servico.remover(4)
        );

        System.out.println(
                "Remover lugar ocupado: " + servico.remover(3)
        );

        System.out.println(
                "Quantidade final: " + servico.quantidade()
        );
    }
}
