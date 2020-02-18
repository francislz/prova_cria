### Teste de desenvolvimento CRIA

Bibliotecas usadas:

* Java 8
* JUnit 4.12
* Gradle 5.42

> Código desenvolvido na IDE IntelliJ IDEA

#### Como executar o projeto

O projeto pode ser executado pela propria IDE

> **OBS:** em caso de algum erro de dependência importe as dependências do gradle na IDE.

A compilação pode ser feita via gradle. Para isso basta entrar com o seguinte comando no terminal (na raiz do projeto):

```shell script
./gradlew build
```
#### Observações

* Não achei que faz sentido criar testes para as classes Carro, Locadora e Categoria visto que essas possuem apenas getters e setters.
* Os dados foram todos salvos em arquivos e a alteração dos arquivos retornara em exceções (que forão tratadas) e mau funcionamento do algoritmo.
* O sistema permite que o usuário forneça o caminho do arquivo de **INPUT** nas variáveis de argumento da função main, mas caso isso não seja feito o arquivo padrão está na raiz do projeto e se chama ___Entrada.txt___.
* O Gradle só foi usado para gerenciar as dependências do projeto, as tasks de execução não funcionam.
* Em um dos exemplos de entrada foram fornecidas três datas, por isso os dias fornecidos não foram tratados como intervalos e sim como os dias de aluguel de forma individual.

