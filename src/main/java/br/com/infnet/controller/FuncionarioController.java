package br.com.infnet.controller;

import br.com.infnet.model.Funcionario;
import br.com.infnet.service.FuncionarioService;
import br.com.infnet.view.FuncionarioView;
import io.javalin.Javalin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FuncionarioController {

    private static final int MAX_LENGTH = 50;
    private final FuncionarioService service = new FuncionarioService();

    public FuncionarioController(Javalin app) {

        // Listar Funcionários
        app.get("/funcionarios", ctx ->
                ctx.html(FuncionarioView.renderList(service.listar()))
        );

        // Formulário Novo Funcionário
        app.get("/funcionarios/new", ctx ->
                ctx.html(FuncionarioView.renderForm(new HashMap<>()))
        );

        // Criar Funcionário
        app.post("/funcionarios", ctx -> {
            String nome = ctx.formParam("nome");
            String cargo = ctx.formParam("cargo");

            if (!validateInputs(ctx, nome, cargo)) return;

            try {
                service.addFuncionario(nome, cargo);
                ctx.redirect("/funcionarios");
            } catch (IllegalArgumentException e) {
                ctx.status(400).result(e.getMessage());
            }
        });

        // Editar Funcionário (carregar formulário)
        app.get("/funcionarios/edit/{id}", ctx -> {

            int id = ctx.pathParamAsClass("id", Integer.class).get();
            Optional<Funcionario> f = service.findById(id);

            if (f.isEmpty()) {
                ctx.status(404).result("Funcionário não encontrado");
                return;
            }

            Funcionario funcionario = f.get();
            Map<String, Object> model = new HashMap<>();
            model.put("id", funcionario.getId());
            model.put("nome", funcionario.getNome());
            model.put("cargo", funcionario.getCargo());

            ctx.html(FuncionarioView.renderForm(model));
        });

        // Atualizar Funcionário
        app.post("/funcionarios/edit/{id}", ctx -> {

            int id = ctx.pathParamAsClass("id", Integer.class).get();
            String nome = ctx.formParam("nome");
            String cargo = ctx.formParam("cargo");

            if (!validateInputs(ctx, nome, cargo)) return;

            try {
                boolean ok = service.updateFuncionario(id, nome, cargo);

                if (!ok) {
                    ctx.status(404).result("Funcionário não encontrado");
                    return;
                }

                ctx.redirect("/funcionarios");

            } catch (IllegalArgumentException e) {
                ctx.status(400).result(e.getMessage());
            }
        });

        // Deletar Funcionário
        app.post("/funcionarios/delete/{id}", ctx -> {
            int id = ctx.pathParamAsClass("id", Integer.class).get();

            if (!service.deleteFuncionario(id)) {
                ctx.status(404).result("Funcionário não encontrado");
                return;
            }

            ctx.redirect("/funcionarios");
        });
    }

    // Extração de repetição da validação (Fail Early)
    private boolean validateInputs(io.javalin.http.Context ctx, String nome, String cargo) {

        if (nome == null || nome.trim().isEmpty() || cargo == null || cargo.trim().isEmpty()) {
            ctx.status(400).result("Erro: Nome e Cargo são obrigatórios.");
            return false;
        }

        if (nome.length() > MAX_LENGTH || cargo.length() > MAX_LENGTH) {
            ctx.status(400).result("Erro: O limite de " + MAX_LENGTH + " caracteres foi excedido.");
            return false;
        }

        return true;
    }
}
