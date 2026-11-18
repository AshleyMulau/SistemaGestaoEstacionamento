
package mz.academia.grupo8.service;

import mz.academia.grupo8.model.EstadoLugar;
import mz.academia.grupo8.model.Estacionamento;
import mz.academia.grupo8.model.Lugar;
import mz.academia.grupo8.model.TipoLugar;
import mz.academia.grupo8.model.Utilizador;
import mz.academia.grupo8.model.Veiculo;
import mz.academia.grupo8.tad.MinhaHashSet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável pela gestão das entradas,
 * saídas e histórico de estacionamento.
 */
public class EstacionamentoService {

    private final MinhaHashSet<Estacionamento> registos;

    private final UtilizadorService utilizadorService;
    private final VeiculoService veiculoService;
    private final LugarService lugarService;

    public EstacionamentoService(
            UtilizadorService utilizadorService,
            VeiculoService veiculoService,
            LugarService lugarService) {

        if (utilizadorService == null
                || veiculoService == null
                || lugarService == null) {
            throw new IllegalArgumentException(
                    "Os serviços não podem ser nulos."
            );
        }

        this.utilizadorService = utilizadorService;
        this.veiculoService = veiculoService;
        this.lugarService = lugarService;

        registos = new MinhaHashSet<>();


    }


    /**
     * Carrega os registos de estacionamento e sincroniza
     * o estado dos lugares após a recuperação dos dados.
     */
    public void carregarDados(List<Estacionamento> dados) {

        // 1. Limpar os registos anteriores
        registos.limpar();

        // 2. Colocar todos os lugares como livres
        for (Lugar lugar : lugarService.listarTodos()) {
            lugar.setEstado(EstadoLugar.LIVRE);
        }

        // 3. Se não existirem dados, terminar
        if (dados == null) {
            return;
        }

        // 4. Carregar os registos e actualizar os lugares
        for (Estacionamento estacionamento : dados) {

            registos.adicionar(estacionamento);

            // Apenas os estacionamentos activos ocupam lugares
            if (estacionamento.estaActivo()) {

                Lugar lugar = lugarService.procurarPorNumero(
                        estacionamento.getLugar().getNumero()
                );

                if (lugar != null) {
                    lugar.setEstado(EstadoLugar.OCUPADO);
                }
            }
        }
    }


    /**
     * Verifica se um veículo está actualmente estacionado.
     *
     * @param matricula matrícula do veículo
     * @return true se existir um estacionamento activo
     */
    public boolean veiculoEstaEstacionado(String matricula) {

        if (matricula == null || matricula.isBlank()) {
            return false;
        }

        for (Estacionamento estacionamento : registos.listar()) {

            if (estacionamento.estaActivo() &&
                    estacionamento.getVeiculo()
                            .getMatricula()
                            .equalsIgnoreCase(matricula.trim())) {

                return true;
            }
        }

        return false;
    }

    /**
     * Regista a entrada de um veículo no parque.
     *
     * @param codigoRegisto código único do estacionamento
     * @param matricula matrícula do veículo
     * @param codigoProprietario código do proprietário
     * @param numeroLugar número do lugar escolhido
     * @return true se a entrada for registada com sucesso
     */
    public boolean registarEntrada(
            String codigoRegisto,
            String matricula,
            String codigoProprietario,
            int numeroLugar) {

        // 1. Validar os dados recebidos
        if (codigoRegisto == null || codigoRegisto.isBlank()
                || matricula == null || matricula.isBlank()
                || codigoProprietario == null
                || codigoProprietario.isBlank()) {
            return false;
        }

        // 2. Procurar o veículo registado
        Veiculo veiculo =
                veiculoService.procurarPorMatricula(matricula);

        if (veiculo == null) {
            return false;
        }

        // 3. Procurar o proprietário registado
        Utilizador proprietario =
                utilizadorService.procurarPorCodigo(codigoProprietario);

        if (proprietario == null) {
            return false;
        }

        // 4. Confirmar que o proprietário corresponde ao veículo
        if (!veiculo.getProprietario().equals(proprietario)) {
            return false;
        }

        // 5. Procurar o lugar
        Lugar lugar = lugarService.procurarPorNumero(numeroLugar);

        if (lugar == null || !lugar.estaLivre()) {
            return false;
        }

        // 6. Verificar compatibilidade entre utilizador e lugar
        if (proprietario instanceof mz.academia.grupo8.model.Estudante) {
            if (lugar.getTipo() != TipoLugar.ESTUDANTE) {
                return false;
            }
        } else if (proprietario instanceof mz.academia.grupo8.model.DocenteFuncionario) {
            if (lugar.getTipo() != TipoLugar.DOCENTE_FUNCIONARIO) {
                return false;
            }
        } else {
            return false;
        }

        // 7. Verificar se o veículo já está estacionado
        if (existeEstacionamentoActivo(veiculo)) {
            return false;
        }

// 8. Verificar se o código de registo já existe
        if (codigoRegistoExiste(codigoRegisto)) {
            return false;
        }

        // 9. Criar o registo de estacionamento
        Estacionamento estacionamento = new Estacionamento(
                codigoRegisto.trim(),
                veiculo,
                proprietario,
                lugar,
                LocalDateTime.now()
        );

        // 10. Guardar o registo na HashSet
        if (!registos.adicionar(estacionamento)) {
            return false;
        }

        // 11. Marcar o lugar como ocupado
        lugar.setEstado(EstadoLugar.OCUPADO);

        return true;
    }


    /**
     * Regista a saída de um veículo do parque.
     *
     * @param matricula matrícula do veículo
     * @return true se a saída for registada com sucesso
     */
    public boolean registarSaida(String matricula) {

        if (matricula == null || matricula.isBlank()) {
            return false;
        }

        // Procurar o veículo
        Veiculo veiculo =
                veiculoService.procurarPorMatricula(matricula);

        if (veiculo == null) {
            return false;
        }

        // Procurar o estacionamento activo
        for (Estacionamento registo : registos.listar()) {

            if (registo.estaActivo()
                    && registo.getVeiculo().equals(veiculo)) {

                // Registar data e hora da saída
                registo.registarSaida(LocalDateTime.now());

                // Libertar o lugar
                registo.getLugar().setEstado(EstadoLugar.LIVRE);

                return true;
            }
        }

        // Veículo não possui estacionamento activo
        return false;
    }


    /**
     * Lista todos os estacionamentos actualmente activos.
     *
     * @return lista de estacionamentos activos
     */
    public List<Estacionamento> listarActivos() {

        return registos.listar()
                .stream()
                .filter(Estacionamento::estaActivo)
                .collect(java.util.stream.Collectors.toList());
    }


    /**
     * Lista todo o histórico de estacionamento.
     *
     * @return lista de todos os registos
     */
    public List<Estacionamento> listarHistorico() {

        return registos.listar();
    }



    /**
     * Consulta os estacionamentos de determinado utilizador.
     *
     * @param codigoProprietario código do utilizador
     * @return lista de estacionamentos do utilizador
     */
    public List<Estacionamento> consultarPorUtilizador(
            String codigoProprietario) {

        if (codigoProprietario == null
                || codigoProprietario.isBlank()) {
            return List.of();
        }

        return registos.listar()
                .stream()
                .filter(registo ->
                        registo.getProprietario()
                                .getCodigo()
                                .equalsIgnoreCase(
                                        codigoProprietario.trim()
                                )
                )
                .collect(java.util.stream.Collectors.toList());
    }



    /**
     * Conta os estacionamentos actualmente activos.
     *
     * @return quantidade de veículos estacionados
     */
    public long contarActivos() {

        return registos.listar()
                .stream()
                .filter(Estacionamento::estaActivo)
                .count();
    }



    /**
     * Devolve a quantidade de lugares livres.
     *
     * @return quantidade de lugares livres
     */
    public long contarLugaresLivres() {

        return lugarService.contarLivres();
    }



    /**
     * Devolve a quantidade de lugares ocupados.
     *
     * @return quantidade de lugares ocupados
     */
    public long contarLugaresOcupados() {

        return lugarService.contarOcupados();
    }



    /**
     * Devolve a quantidade total de registos históricos.
     *
     * @return quantidade total de registos
     */
    public int quantidadeRegistos() {

        return registos.tamanho();
    }


    /**
     * Devolve uma cópia de todos os registos de estacionamento,
     * incluindo os activos e os já terminados.
     */
    public List<Estacionamento> obterDados() {
        return new ArrayList<>(registos.listar());
    }

    /**
     * Verifica se não existem registos de estacionamento.
     *
     * @return true se não existirem registos
     */
    public boolean historicoVazio() {

        return registos.estaVazio();
    }

    public boolean utilizadorEstaEstacionado(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return false;
        }

        for (Estacionamento estacionamento : registos.listar()) {
            if (estacionamento.estaActivo()
                    && estacionamento.getProprietario().getCodigo()
                    .equalsIgnoreCase(codigo.trim())) {
                return true;
            }
        }

        return false;
    }


    /**
     * Verifica se já existe um registo com determinado código.
     */
    private boolean codigoRegistoExiste(String codigo) {

        for (Estacionamento registo : registos.listar()) {

            if (registo.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Verifica se um veículo já possui estacionamento activo.
     */
    private boolean existeEstacionamentoActivo(Veiculo veiculo) {

        for (Estacionamento registo : registos.listar()) {

            if (registo.estaActivo()
                    && registo.getVeiculo().equals(veiculo)) {
                return true;
            }
        }

        return false;
    }



}
