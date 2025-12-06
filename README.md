README - Manual de Execução do Projeto PB_tp3

1. Visão Geral
Este projeto contém um sistema simples de gerenciamento de funcionários desenvolvido em Java com Maven, além de testes unitários (JUnit) e testes de interface usando Selenium WebDriver.

2. Pré-requisitos
Para rodar o sistema e os testes, é necessário ter instalado:
- Java JDK 17 ou superior
- Apache Maven 3.8+ 
- Navegador Google Chrome
- ChromeDriver compatível com a versão do seu Chrome
- Ambiente com acesso local a http://localhost:7000 (backend deve estar em execução)

3. Estrutura do Projeto
O projeto segue o padrão Maven:
- src/main/java: código da aplicação
- src/main/resources: arquivos de configuração
- src/test/java: testes unitários e testes utilizando Selenium
- pom.xml: dependências e configuração do Maven

4. Dependências Principais
As dependências já estão configuradas no pom.xml:
- JUnit 5 (testes unitários)
- Selenium WebDriver (testes de interface)
- SparkJava (backend HTTP)
- Jackson (serialização JSON)

5. Como Compilar o Projeto
No terminal, dentro da pasta PB_tp3-master, execute:
    mvn clean install

6. Como Executar o Sistema
Execute o comando:
    mvn exec:java -Dexec.mainClass="br.com.infnet.App"

O servidor iniciará em:
    http://localhost:7000/funcionarios

7. Como Rodar os Testes
Para rodar apenas os testes unitários (JUnit):
    mvn test

Para rodar os testes Selenium, certifique-se de:
- Ter o ChromeDriver no PATH
- Estar executando o servidor (passo 6)

Então execute:
    mvn test -Dtest=FuncionarioWebTest

8. Observações Importantes
- Caso os testes Selenium falhem por tempo, ajuste os waits dentro das classes de Page Object.
- Certifique-se de que nenhuma outra aplicação está usando a porta 7000.
- O HTML das páginas está localizado em src/main/resources/templates.

9. Contato
Em caso de dúvidas, consulte os comentários no código ou a documentação das bibliotecas utilizadas.
