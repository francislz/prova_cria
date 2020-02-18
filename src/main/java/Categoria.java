public class Categoria {
    private String descricao;
    private int limiteDePessoas;

    public Categoria(String descricao, int limiteDePessoas) {
        this.descricao = descricao;
        this.limiteDePessoas = limiteDePessoas;
    }

    public Categoria() {
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getLimiteDePessoas() {
        return limiteDePessoas;
    }

    public void setLimiteDePessoas(int limiteDePessoas) {
        this.limiteDePessoas = limiteDePessoas;
    }
}
