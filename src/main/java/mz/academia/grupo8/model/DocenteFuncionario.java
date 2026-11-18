
package mz.academia.grupo8.model;

/**
 * Representa um docente ou funcionário da Faculdade
 * de Ciências e Tecnologia.
 */
public class DocenteFuncionario extends Utilizador {

    private static final long serialVersionUID = 1L;

    // Dados específicos do docente ou funcionário
    private String departamento;
    private String categoria;

    /**
     * Construtor da classe DocenteFuncionario.
     */
    public DocenteFuncionario(String codigo, String nome,
                              String telefone, String email,
                              String departamento, String categoria) {

        // Inicializa os atributos herdados
        super(codigo, nome, telefone, email);

        setDepartamento(departamento);
        setCategoria(categoria);
    }

    // Getter do departamento
    public String getDepartamento() {
        return departamento;
    }

    // Setter do departamento
    public void setDepartamento(String departamento) {

        if (departamento == null || departamento.isBlank()) {
            throw new IllegalArgumentException(
                    "O departamento é obrigatório."
            );
        }

        this.departamento = departamento.trim();
    }

    // Getter da categoria
    public String getCategoria() {
        return categoria;
    }

    // Setter da categoria
    public void setCategoria(String categoria) {

        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException(
                    "A categoria é obrigatória."
            );
        }

        this.categoria = categoria.trim();
    }

    /**
     * Representação textual do docente ou funcionário.
     */
    @Override
    public String toString() {

        return super.toString()
                + "\nTipo: Docente/Funcionário"
                + "\nDepartamento: " + departamento
                + "\nCategoria: " + categoria;
    }
}
