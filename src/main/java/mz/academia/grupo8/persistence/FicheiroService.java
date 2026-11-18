
package mz.academia.grupo8.persistence;

import java.io.*;

/**
 * Serviço responsável pela leitura e escrita
 * de objectos em ficheiros.
 */
public class FicheiroService {

    /**
     * Guarda um objecto num ficheiro.
     *
     * @param caminho caminho do ficheiro
     * @param objeto objecto a guardar
     */
    public void guardar(String caminho, Object objeto) {

        if (caminho == null || caminho.isBlank()) {
            throw new IllegalArgumentException(
                    "O caminho do ficheiro é obrigatório."
            );
        }

        try (ObjectOutputStream saida =
                     new ObjectOutputStream(
                             new FileOutputStream(caminho))) {

            saida.writeObject(objeto);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao guardar os dados: " + e.getMessage(), e
            );
        }
    }

    /**
     * Carrega um objecto de um ficheiro.
     *
     * @param caminho caminho do ficheiro
     * @return objecto recuperado
     */
    public Object carregar(String caminho) {

        if (caminho == null || caminho.isBlank()) {
            throw new IllegalArgumentException(
                    "O caminho do ficheiro é obrigatório."
            );
        }

        File ficheiro = new File(caminho);

        // Se o ficheiro ainda não existir, não há dados guardados
        if (!ficheiro.exists()) {
            return null;
        }

        try (ObjectInputStream entrada =
                     new ObjectInputStream(
                             new FileInputStream(ficheiro))) {

            return entrada.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(
                    "Erro ao carregar os dados: " + e.getMessage(), e
            );
        }
    }

    /**
     * Verifica se determinado ficheiro existe.
     *
     * @param caminho caminho do ficheiro
     * @return true se existir
     */
    public boolean existe(String caminho) {
        return new File(caminho).exists();
    }
}
