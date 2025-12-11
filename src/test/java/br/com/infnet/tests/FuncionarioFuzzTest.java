package br.com.infnet.tests;

import br.com.infnet.model.Funcionario;
import br.com.infnet.service.FuncionarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

public class FuncionarioFuzzTest {

    private FuncionarioService service = new FuncionarioService();
    private final Random random = new Random();

    private String randomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 !@#$%&*()-_=+[]{};:,./?"
                + "çÇáéíóúãõâêîôû"              // unicode leve
                + "<>\"'`~";                    // caracteres maliciosos
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    @Test
    public void fuzzTesteNomes() {

        for (int i = 0; i < 200; i++) {

            service = new FuncionarioService();

            int randomLength = random.nextInt(2) == 0 ? random.nextInt(61) : random.nextInt(1000);

            String nomeAleatorio = random.nextInt(10) == 0 ? null : randomString(randomLength);
            String cargoFixado = "CargoValido";

            try {
                service.addFuncionario(nomeAleatorio, cargoFixado);

                List<Funcionario> lista = service.listar();
                Assertions.assertFalse(lista.isEmpty());

                Funcionario ultimo = lista.get(lista.size() - 1);

                Assertions.assertEquals(
                        nomeAleatorio == null ? null : nomeAleatorio.trim(),
                        ultimo.getNome()
                );

                Assertions.assertEquals(cargoFixado, ultimo.getCargo());

            } catch (IllegalArgumentException ex) {
                String msg = ex.getMessage();
                Assertions.assertTrue(
                        msg != null &&
                                (
                                        msg.contains("Nome") ||
                                                msg.contains("cargo") ||
                                                msg.contains("não") ||
                                                msg.contains("excedem") ||
                                                msg.toLowerCase().contains("inválido")
                                ),
                        "Exceção inesperada: " + msg
                );
            } catch (Exception ex) {
                Assertions.fail("Exceção inesperada: " + ex.getClass() + " - " + ex.getMessage());
            }
        }
    }

    @Test
    public void fuzzTesteCargos() {

        for (int i = 0; i < 200; i++) {

            // Reinicializa o serviço
            service = new FuncionarioService();

            // adiciona possibilidade de cargo extremamente longo
            int randomLength = random.nextInt(2) == 0 ? random.nextInt(61) : random.nextInt(1000);

            String nomeFixado = "NomeValido";
            String cargoAleatorio = random.nextInt(10) == 0 ? null : randomString(randomLength);

            try {
                service.addFuncionario(nomeFixado, cargoAleatorio);

                List<Funcionario> lista = service.listar();
                Assertions.assertFalse(lista.isEmpty());

                Funcionario ultimo = lista.get(lista.size() - 1);

                Assertions.assertEquals(nomeFixado, ultimo.getNome());

                Assertions.assertEquals(
                        cargoAleatorio == null ? null : cargoAleatorio.trim(),
                        ultimo.getCargo()
                );

            } catch (IllegalArgumentException ex) {
                String msg = ex.getMessage();
                Assertions.assertTrue(
                        msg != null &&
                                (
                                        msg.contains("Nome") ||
                                                msg.contains("cargo") ||
                                                msg.contains("não") ||
                                                msg.contains("excedem") ||
                                                msg.toLowerCase().contains("inválido")
                                ),
                        "Exceção inesperada: " + msg
                );
            } catch (Exception ex) {
                Assertions.fail("Exceção inesperada: " + ex.getClass() + " - " + ex.getMessage());
            }
        }
    }
}