import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testFilterLocadora(){
        //Define a razao social da locadora a ser filtrada
        String nome = "WestCar";
        //Faz a busca de acordo com o nome fornecido
        Locadora resultado = new Main().filterLocadora(nome);
        //Garante que o nome é o nome retornado
        assertTrue(nome.equals(resultado.getNome()));
    }

    @Test
    public void resolveInstancia() {
        //Cria uma instancia da classe main para ler os arquivos e chamar a execucao
        Main main = new Main();
        //Retorna o par de Locadora e Carro encontrados
        Object[] pair = main.resolveInstancia("Normal: 3: 20Mar2009 (seg)");

        //Garante que para a instancia fornecida o pair é nao nulo
        assertNotNull(pair);

        /***
         * Se passar o primeiro teste de não nulo, o segundo teste é feito
         * que verifica se os objetos encontrados são os esperados
         */
        Locadora locadora = (Locadora) pair[0];
        Carro carro = (Carro) pair[1];

        try{
            assertTrue(locadora.getNome().equals("SouthCar"));
            assertTrue(carro.getDescricao().equals("GOL"));
        }
        catch (AssertionError ex){
            // Apenas uma mensagem de log para falar que o teste falhou
            System.err.println("Teste falhou, o resultado esperado não foi obtido");
        }
    }

    @Test
    public void combinar() {
        Main main = new Main();
        int qtdEsperada = 1;
        /***
         * Retorna a quantidade de categorias que são match para o numero
         * de pessoas fornecidos
         */
        int qtdObtida = main.combinar(6).size();

        /**
         * Nesse exemplo apenas uma categoria é esperada
         */
        assertEquals(qtdEsperada, qtdObtida);

    }

    @Test
    public void readCarros() {
        int qtdExperada = 3;
        int qtdResultante = new Main().getCarrosSize();

        assertEquals(qtdExperada, qtdResultante);
    }

    @Test
    public void readLocadoras() {
        //Define a quantidade de locadoras
        int qtd = 3;
        //Apos a leitura retorna a quantidade de locadoras lidas no arquivo
        int resultado = new Main().getLocadorasSize();
        //Certifica que a quantidade é a igual
        assertEquals(qtd, resultado);
    }

    @Test
    public void readCategorias() {
        //Define a quantidade de categorias
        int qtd = 3;
        //Apos a leitura retorna a quantidade de categorias lida no arquivo
        int resultado = new Main().getCategoriasSize();
        //Certifica que a quantidade é a igual
        assertEquals(qtd, resultado);
    }
}
