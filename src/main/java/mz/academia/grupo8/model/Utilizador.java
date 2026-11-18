
package mz.academia.grupo8.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe abstracta que representa um utilizador
 * do parque de estacionamento.
 *
 * Pode ser um estudante ou um docente/funcionário.
 */
public abstract class Utilizador implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos comuns a todos os utilizadores
    private final String codigo;
    private String nome;
    private String telefone;
    private String email;

    /**
     * Construtor da classe Utilizador.
     */
    public Utilizador(String codigo, String nome,
                      String telefone, String email) {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "O código do utilizador é obrigatório."
            );
        }

        this.codigo = codigo.trim();
        setNome(nome);
        setTelefone(telefone);
        setEmail(email);
    }

    // Métodos getters

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    // Métodos setters

    public void setNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "O nome é obrigatório."
            );
        }

        this.nome = nome.trim();
    }

    public void setTelefone(String telefone) {

        if (telefone == null || telefone.isBlank()) {
            throw new IllegalArgumentException(
                    "O telefone é obrigatório."
            );
        }

        this.telefone = telefone.trim();
    }

    public void setEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "O email é obrigatório."
            );
        }

        this.email = email.trim();
    }

    /**
     * Compara dois utilizadores através do código
     * e do tipo concreto da classe.
     */
    @Override
    public boolean equals(Object objeto) {

        if (this == objeto) {
            return true;
        }

        if (objeto == null || getClass() != objeto.getClass()) {
            return false;
        }

        Utilizador outro = (Utilizador) objeto;

        return codigo.equals(outro.codigo);
    }

    /**
     * Gera o código hash do utilizador.
     */
    @Override
    public int hashCode() {

        return Objects.hash(getClass(), codigo);
    }

    /**
     * Representação textual do utilizador.
     */
    @Override
    public String toString() {

        return "Código: " + codigo
                + "\nNome: " + nome
                + "\nTelefone: " + telefone
                + "\nEmail: " + email;
    }
}
