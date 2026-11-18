
package mz.academia.grupo8.service;

import mz.academia.grupo8.model.EstadoLugar;
import mz.academia.grupo8.model.Lugar;
import mz.academia.grupo8.model.TipoLugar;
import mz.academia.grupo8.tad.MinhaHashSet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela gestão dos lugares
 * do parque de estacionamento.
 */
public class LugarService {

    private final MinhaHashSet<Lugar> lugares;

    public LugarService() {
        lugares = new MinhaHashSet<>();
    }

    // Registar um novo lugar
    public boolean registar(Lugar lugar) {
        if (lugar == null) {
            throw new IllegalArgumentException(
                    "O lugar não pode ser nulo."
            );
        }

        if (procurarPorNumero(lugar.getNumero()) != null) {
            return false;
        }

        return lugares.adicionar(lugar);
    }

    /**
     * Carrega os lugares recuperados do ficheiro
     * para a estrutura MinhaHashSet.
     */
    public void carregarDados(List<Lugar> dados) {

        lugares.limpar();

        if (dados == null) {
            return;
        }

        for (Lugar lugar : dados) {
            lugares.adicionar(lugar);
        }
    }

    /**
     * Devolve uma cópia da lista de lugares.
     */
    public List<Lugar> obterDados() {
        return new ArrayList<>(lugares.listar());
    }


    // Procurar lugar pelo número
    public Lugar procurarPorNumero(int numero) {
        if (numero <= 0) {
            return null;
        }

        for (Lugar lugar : lugares.listar()) {
            if (lugar.getNumero() == numero) {
                return lugar;
            }
        }

        return null;
    }

    // Actualizar localização e tipo do lugar
    public boolean actualizar(int numero,
                              String localizacao,
                              TipoLugar tipo) {

        Lugar lugar = procurarPorNumero(numero);

        if (lugar == null) {
            return false;
        }

        if (lugar.getEstado() == EstadoLugar.OCUPADO) {
            return false;
        }

        lugar.setLocalizacao(localizacao);
        lugar.setTipo(tipo);

        return true;
    }

    // Remover lugar pelo número
    public boolean remover(int numero) {
        Lugar lugar = procurarPorNumero(numero);

        if (lugar == null) {
            return false;
        }

        if (lugar.getEstado() == EstadoLugar.OCUPADO) {
            return false;
        }

        return lugares.remover(lugar);
    }

    // Listar todos os lugares
    public List<Lugar> listarTodos() {
        return lugares.listar()
                .stream()
                .sorted((a, b) ->
                        Integer.compare(
                                a.getNumero(),
                                b.getNumero()
                        ))
                .collect(Collectors.toList());
    }

    // Listar lugares livres
    public List<Lugar> listarLivres() {
        return lugares.listar()
                .stream()
                .filter(Lugar::estaLivre)
                .sorted((a, b) ->
                        Integer.compare(
                                a.getNumero(),
                                b.getNumero()
                        ))
                .collect(Collectors.toList());
    }

    // Listar lugares ocupados
    public List<Lugar> listarOcupados() {
        return lugares.listar()
                .stream()
                .filter(lugar ->
                        lugar.getEstado() == EstadoLugar.OCUPADO)
                .sorted((a, b) ->
                        Integer.compare(
                                a.getNumero(),
                                b.getNumero()
                        ))
                .collect(Collectors.toList());
    }

    // Contar lugares livres
    public long contarLivres() {
        return lugares.listar()
                .stream()
                .filter(Lugar::estaLivre)
                .count();
    }

    // Contar lugares ocupados
    public long contarOcupados() {
        return lugares.listar()
                .stream()
                .filter(lugar ->
                        lugar.getEstado() == EstadoLugar.OCUPADO)
                .count();
    }

    // Quantidade total de lugares
    public int quantidade() {
        return lugares.tamanho();
    }

    // Verificar se não existem lugares
    public boolean estaVazio() {
        return lugares.estaVazio();
    }
}
