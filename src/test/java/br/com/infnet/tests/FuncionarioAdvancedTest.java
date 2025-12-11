package br.com.infnet.tests;

import br.com.infnet.service.FuncionarioService;
import br.com.infnet.service.RedeSimulada;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeoutException;

import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.*;

public class FuncionarioAdvancedTest {

    @Test
    void testSimulacaoFalhaDeRede() {
        RedeSimulada redeMock = Mockito.mock(RedeSimulada.class);

        Mockito.when(redeMock.fetchData())
                .thenThrow(new RuntimeException("Falha de rede"));

        FuncionarioService service = new FuncionarioService(redeMock);

        String resultado = service.sincronizarComServidor();

        assertEquals("FALHA_REDE", resultado); // atualizado

        Mockito.verify(redeMock).fetchData();
    }


    @Test
    void testTimeoutSimulado() {
        RedeSimulada redeMock = Mockito.mock(RedeSimulada.class);

        Mockito.when(redeMock.fetchData()).thenAnswer(invocation -> {
            Thread.sleep(2000);
            return "OK";
        });

        FuncionarioService service = new FuncionarioService(redeMock);

        assertEquals("FALHA_TIMEOUT", service.sincronizarComServidor());
    }


    @Test
    void testEntradaInvalida() {
        RedeSimulada redeMock = Mockito.mock(RedeSimulada.class);

        Mockito.when(redeMock.fetchData()).thenReturn(null);

        FuncionarioService service = new FuncionarioService(redeMock);

        assertEquals("RESPOSTA_INVALIDA", service.sincronizarComServidor()); // atualizado
    }
}