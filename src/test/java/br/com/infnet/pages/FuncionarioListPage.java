package br.com.infnet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
/* testando workflow */
public class FuncionarioListPage {

    private final WebDriver driver;

    // URL da página principal da listagem de funcionários
    private final String BASE_URL = "http://localhost:7000/funcionarios";

    // Localizador do link usado para acessar o formulário de cadastro
    private final By LINK_NOVO_FUNCIONARIO = By.cssSelector("a[href='/funcionarios/new']");

    // Localizador do corpo da tabela onde os funcionários são listados
    private final By TABLE_BODY = By.cssSelector("table tbody");

    // Construtor recebe o WebDriver utilizado para interação com a página
    public FuncionarioListPage(WebDriver driver) {
        this.driver = driver;
    }

    // Navega diretamente para a página de listagem de funcionários
    public void navigateToList() {
        driver.get(BASE_URL);
    }

    // Clica no link responsável por abrir o formulário de cadastro
    public void clickNewFuncionario() {
        driver.findElement(LINK_NOVO_FUNCIONARIO).click();
    }

    // Localiza a linha da tabela correspondente ao funcionário cujo nome foi informado
    public WebElement findFuncionarioRow(String nome) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Aguarda até que exista uma linha contendo o nome informado
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//table//tr[td[contains(normalize-space(), '" + nome + "')]]")
        ));

        // Retorna a linha completa onde o nome foi encontrado
        return driver.findElement(
                By.xpath("//table//tr[td[contains(normalize-space(), '" + nome + "')]]")
        );
    }

    // Clica no botão de edição referente ao funcionário informado
    public void clickEdit(String nome) {

        // Localiza a linha específica do funcionário
        WebElement row = findFuncionarioRow(nome);

        // Dentro dessa linha, clica no botão de edição
        row.findElement(By.cssSelector("a.btn-warning")).click();

        // Aguarda até que a URL seja alterada para uma rota de edição
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains("/edit/"));
    }

    // Executa a ação de deletar o funcionário correspondente ao nome fornecido
    public void deleteFuncionario(String nome) {

        // Localiza a linha onde está o funcionário
        WebElement row = findFuncionarioRow(nome);

        // Dentro da linha, clica no botão de exclusão do formulário
        row.findElement(By.cssSelector("form button.btn-danger")).click();

        // Aguarda o redirecionamento de volta para a listagem após a exclusão
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlToBe(BASE_URL));
    }

    // Retorna todo o texto contido no corpo da tabela
    public String getTableText() {
        return driver.findElement(TABLE_BODY).getText();
    }

    // Verifica se existe uma linha contendo simultaneamente o nome e o cargo indicados
    public boolean isFuncionarioPresent(String nome, String cargo) {
        try {
            String xpath = "//tr[td[contains(.,'" + nome + "')] and td[contains(.,'" + cargo + "')]]";
            return driver.findElement(By.xpath(xpath)).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }
}

