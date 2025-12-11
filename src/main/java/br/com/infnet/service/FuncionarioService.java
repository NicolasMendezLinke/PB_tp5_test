package br.com.infnet.service;

import br.com.infnet.model.Funcionario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// imports para concorrência / timeout
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FuncionarioService {

    private final List<Funcionario> funcionarios = new ArrayList<>();
    private int nextId = 1;

    private final RedeSimulada rede;

    public FuncionarioService() {
        this.rede = null; // mantém compatibilidade
        addFuncionario("Edward Newgate", "Senior");
        addFuncionario("Portgas Ace", "Junior");
    }

    public FuncionarioService(RedeSimulada rede) {
        this.rede = rede;
        addFuncionario("Edward Newgate", "Senior");
        addFuncionario("Portgas Ace", "Junior");
    }

    public void addFuncionario(String nome, String cargo) {
        validate(nome, cargo);
        funcionarios.add(new Funcionario(nextId++, nome.trim(), cargo.trim()));
    }

    public Optional<Funcionario> findById(int id) {
        return funcionarios.stream()
                .filter(f -> f.getId() == id)
                .findFirst();
    }

    public boolean updateFuncionario(int id, String nome, String cargo) {
        validate(nome, cargo);

        Optional<Funcionario> f = findById(id);
        if (f.isEmpty()) {
            return false;
        }

        Funcionario funcionario = f.get();
        funcionario.setNome(nome.trim());
        funcionario.setCargo(cargo.trim());
        return true;
    }

    public boolean deleteFuncionario(int id) {
        return funcionarios.removeIf(f -> f.getId() == id);
    }

    public List<Funcionario> listar() {
        return Collections.unmodifiableList(funcionarios);
    }

    private void validate(String nome, String cargo) {
        if (nome == null || nome.isBlank() || cargo == null || cargo.isBlank()) {
            throw new IllegalArgumentException("Nome e cargo não podem ser vazios.");
        }
        if (nome.length() > 50 || cargo.length() > 50) {
            throw new IllegalArgumentException("Nome ou cargo excedem 50 caracteres.");
        }
    }

    public String sincronizarComServidor() {
        if (rede == null) {
            return "SEM_REDE";
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            Future<String> future = executor.submit(rede::fetchData);

            // timeout de 1 segundo
            String resposta = future.get(1, TimeUnit.SECONDS);

            if (resposta == null) {
                return "FALHA";
            }

            return resposta;

        } catch (TimeoutException e) {
            // caso a chamada exceda 1s
            return "FALHA_TIMEOUT";

        } catch (Exception e) {
            // outras falhas (RuntimeException simulada, etc.)
            return "FALHA";

        } finally {
            // tenta encerrar a thread do executor
            executor.shutdownNow();
        }
    }
}


