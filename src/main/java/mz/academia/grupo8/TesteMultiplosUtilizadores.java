package mz.academia.grupo8;

import mz.academia.grupo8.model.Estudante;
import mz.academia.grupo8.model.Utilizador;
import mz.academia.grupo8.service.UtilizadorService;

public class TesteMultiplosUtilizadores {

    public static void main(String[] args) {

        UtilizadorService service = new UtilizadorService();

        System.out.println("=== REGISTAR UTILIZADORES ===");

        System.out.println(service.registar(
                new Estudante("E001", "Maria", "841111111",
                        "maria@gmail.com", "Informática", 3)));

        System.out.println(service.registar(
                new Estudante("E002", "Ana", "842222222",
                        "ana@gmail.com", "Informática", 2)));

        System.out.println(service.registar(
                new Estudante("E003", "João", "843333333",
                        "joao@gmail.com", "Informática", 1)));

        System.out.println(service.registar(
                new Estudante("E004", "Pedro", "844444444",
                        "pedro@gmail.com", "Informática", 3)));

        System.out.println("\n=== RESULTADO ===");

        System.out.println(
                "Quantidade: " + service.quantidade()
        );

        for (Utilizador u : service.listarTodos()) {
            System.out.println(u.getCodigo() + " - " + u.getNome());
        }
    }
}