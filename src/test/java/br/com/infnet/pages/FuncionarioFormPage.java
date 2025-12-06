package br.com.infnet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class FuncionarioFormPage {

    // Driver utilizado para controlar o navegador
    private final WebDriver driver;

    // Localizador do campo de nome no formulário
    private final By FIELD_NOME = By.id("nome");

    // Localizador do campo de cargo no formulário
    private final By FIELD_CARGO = By.id("cargo");

    // Localizador do botão responsável por enviar o formulário
    private final By BUTTON_SUBMIT = By.cssSelector("button.btn-success");

    // URL base para onde o usuário deve ser redirecionado após o envio do formulário
    private final String BASE_URL = "http://localhost:7000/funcionarios";

    // Construtor que recebe o driver e inicializa a página
    public FuncionarioFormPage(WebDriver driver) {
        this.driver = driver;
    }

    // Preenche o formulário com os valores informados
    public void fillForm(String nome, String cargo) {

        // Aguarda até que o campo de nome esteja presente na página
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(FIELD_NOME));

        // Limpa e preenche o campo de nome
        driver.findElement(FIELD_NOME).clear();
        driver.findElement(FIELD_NOME).sendKeys(nome);

        // Limpa e preenche o campo de cargo
        driver.findElement(FIELD_CARGO).clear();
        driver.findElement(FIELD_CARGO).sendKeys(cargo);
    }

    // Envia o formulário e aguarda o redirecionamento para a lista de funcionários
    public void submitForm() {

        // Clica no botão de submissão
        driver.findElement(BUTTON_SUBMIT).click();

        // Aguarda até que a URL final seja carregada após o envio
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlToBe(BASE_URL));
    }

    // Realiza apenas o clique no botão de enviar, sem esperar redirecionamento
    public void clickSubmitButton() {
        driver.findElement(BUTTON_SUBMIT).click();
    }
}

