
package mz.academia.grupo8;

import mz.academia.grupo8.model.Estudante;
import mz.academia.grupo8.model.DocenteFuncionario;
import mz.academia.grupo8.model.Veiculo;
import mz.academia.grupo8.service.VeiculoService;

public class TesteVeiculoService {

    public static void main(String[] args) {

        VeiculoService servico = new VeiculoService();

        // Criar utilizadores
        Estudante estudante = new Estudante(
                "EST001",
                "Ana Manuel",
                "841234567",
                "ana@email.com",
                "Engenharia Informática",
                3
        );

        DocenteFuncionario docente = new DocenteFuncionario(
                "DOC001",
                "Carlos Alberto",
                "821234567",
                "carlos@email.com",
                "Departamento de Informática",
                "Docente"
        );

        // Criar veículos
        Veiculo v1 = new Veiculo(
                "ABC-123-MC",
                "Toyota",
                "Corolla",
                "Branco",
                estudante
        );

        Veiculo v2 = new Veiculo(
                "XYZ-456-MC",
                "Honda",
                "Civic",
                "Preto",
                docente
        );

        Veiculo v3 = new Veiculo(
                "DEF-789-MC",
                "Nissan",
                "Note",
                "Prata",
                estudante
        );

        System.out.println("=== REGISTO ===");
        System.out.println("Veículo 1: " + servico.registar(v1));
        System.out.println("Veículo 2: " + servico.registar(v2));
        System.out.println("Veículo 3: " + servico.registar(v3));

        System.out.println("\n=== DUPLICADOS ===");

        Veiculo duplicado = new Veiculo(
                "ABC-123-MC",
                "Ford",
                "Fiesta",
                "Vermelho",
                estudante
        );

        System.out.println(
                "Matrícula duplicada: " + servico.registar(duplicado)
        );

        System.out.println("\n=== PESQUISA ===");
        System.out.println(
                servico.procurarPorMatricula("ABC-123-MC")
        );

        System.out.println("\n=== TODOS OS VEÍCULOS ===");
        servico.listarTodos().forEach(System.out::println);

        System.out.println("\n=== VEÍCULOS DE ESTUDANTES ===");
        servico.listarVeiculosEstudantes()
                .forEach(System.out::println);

        System.out.println("\n=== VEÍCULOS DE DOCENTES ===");
        servico.listarVeiculosDocentes()
                .forEach(System.out::println);

        System.out.println("\n=== ACTUALIZAÇÃO ===");

        System.out.println(
                "Actualizado: " + servico.actualizar(
                        "ABC-123-MC",
                        "Toyota",
                        "Corolla Cross",
                        "Azul",
                        estudante
                )
        );

        System.out.println(
                servico.procurarPorMatricula("ABC-123-MC")
        );

        System.out.println("\n=== REMOÇÃO ===");

        System.out.println(
                "Removido: " + servico.remover("XYZ-456-MC")
        );

        System.out.println(
                "Quantidade final: " + servico.quantidade()
        );
    }
}
