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

    // FAIL EARLY: impede estado inválido já no construtor
    public FuncionarioService() {
        this.rede = null;
        inicializarRegistrosPadrao();
    }

    public FuncionarioService(RedeSimulada rede) {
        if (rede == null) { // fail early
            throw new IllegalArgumentException("Instância de rede não pode ser nula.");
        }
        this.rede = rede;
        inicializarRegistrosPadrao();
    }

    private void inicializarRegistrosPadrao() {
        addFuncionario("Edward Newgate", "Senior");
        addFuncionario("Portgas Ace", "Junior");
    }

    // FAIL EARLY: garante que nome/cargo são válidos
    private void validate(String nome, String cargo) {
        if (nome == null || cargo == null) {
            throw new IllegalArgumentException("Nome ou cargo não podem ser nulos.");
        }
        nome = nome.trim();
        cargo = cargo.trim();

        if (nome.isBlank() || cargo.isBlank()) {
            throw new IllegalArgumentException("Nome e cargo não podem ser vazios.");
        }

        if (nome.length() > 50 || cargo.length() > 50) {
            throw new IllegalArgumentException("Nome ou cargo excedem 50 caracteres.");
        }

        if (nome.contains("  ") || cargo.contains("  ")) {
            throw new IllegalArgumentException("Nome ou cargo contêm espaços inválidos.");
        }
    }

    public void addFuncionario(String nome, String cargo) {
        validate(nome, cargo);
        funcionarios.add(new Funcionario(nextId++, nome.trim(), cargo.trim()));
    }

    public Optional<Funcionario> findById(int id) {
        // fail early
        if (id <= 0) {
            return Optional.empty();
        }

        return funcionarios.stream()
                .filter(f -> f.getId() == id)
                .findFirst();
    }

    public boolean updateFuncionario(int id, String nome, String cargo) {
        validate(nome, cargo);

        Optional<Funcionario> f = findById(id);

        if (f.isEmpty()) {
            return false; // fail gracefully
        }

        Funcionario funcionario = f.get();
        funcionario.setNome(nome.trim());
        funcionario.setCargo(cargo.trim());
        return true;
    }

    public boolean deleteFuncionario(int id) {
        if (id <= 0) {  // fail early
            return false;
        }
        return funcionarios.removeIf(f -> f.getId() == id);
    }

    public List<Funcionario> listar() {
        return Collections.unmodifiableList(funcionarios);
    }
    //test
    // FAIL GRACEFULLY NA REDE
    public String sincronizarComServidor() {

        if (rede == null) {
            return "SEM_REDE"; // graceful
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            Future<String> future = executor.submit(rede::fetchData);

            // timeout curto
            String resposta = future.get(1, TimeUnit.SECONDS);

            if (resposta == null) {
                return "RESPOSTA_INVALIDA"; // graceful
            }

            return resposta;

        } catch (TimeoutException e) {
            return "FALHA_TIMEOUT";  // graceful

        } catch (Exception e) {
            return "FALHA_REDE"; // graceful

        } finally {
            executor.shutdownNow();
        }
    }
}