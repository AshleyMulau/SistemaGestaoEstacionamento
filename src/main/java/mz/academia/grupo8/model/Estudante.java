
package mz.academia.grupo8.model;

/**
 * Representa um estudante da Faculdade de Ciências
 * e Tecnologia autorizado a utilizar o estacionamento.
 */
public class Estudante extends Utilizador {

    private static final long serialVersionUID = 1L;

    // Dados específicos do estudante
    private String curso;
    private int anoAcademico;

    /**
     * Construtor da classe Estudante.
     */
    public Estudante(String codigo, String nome,
                     String telefone, String email,
                     String curso, int anoAcademico) {

        // Inicializa os atributos herdados
        super(codigo, nome, telefone, email);

        setCurso(curso);
        setAnoAcademico(anoAcademico);
    }

    // Getter do curso
    public String getCurso() {
        return curso;
    }

    // Setter do curso
    public void setCurso(String curso) {

        if (curso == null || curso.isBlank()) {
            throw new IllegalArgumentException(
                    "O curso é obrigatório."
            );
        }

        this.curso = curso.trim();
    }

    // Getter do ano académico
    public int getAnoAcademico() {
        return anoAcademico;
    }

    // Setter do ano académico
    public void setAnoAcademico(int anoAcademico) {

        if (anoAcademico < 1 || anoAcademico > 7) {
            throw new IllegalArgumentException(
                    "O ano académico deve estar entre 1 e 7."
            );
        }

        this.anoAcademico = anoAcademico;
    }

    /**
     * Representação textual do estudante.
     */
    @Override
    public String toString() {

        return super.toString()
                + "\nTipo: Estudante"
                + "\nCurso: " + curso
                + "\nAno Académico: " + anoAcademico;
    }
}
