package com.estoque;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainScreen extends Application {
    private Usuario usuario;
    private final DatabaseManager dbManager;
    private EstoqueService service;
    private BorderPane root;
    private TableView<Produtos> tabelaProdutos;
    private Produtos produtos;
    private TableView<Movimentacao> tabelaMovimentacoes;
    private TableView<Usuario> tabelaUsuarios;
    
    public MainScreen(Usuario usuario, DatabaseManager dbManager) {
        this.usuario = usuario;
        this.dbManager = dbManager.getInstance();
        this.service = new EstoqueService();
    
    }


    private void carregardadosBanco(){
        List<Produtos> produtosdoBanco = dbManager.listarTodosProdutos();
        for(Produtos p:produtosdoBanco){
            service.getEstoque().adicionarProduto(p);
        }

        List<Movimentacao> movimentacaos = dbManager.listarMovimentacoes();
        for (Movimentacao m:movimentacaos){
            service.getEstoque().gethistoricodeMov().add(m);
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Estoque - " + usuario.getNome());
        
        root = new BorderPane();
        root.setTop(criarBarraSuperior());
        root.setLeft(criarMenuLateral());
        root.setCenter(criarDashboard());
        
        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
    
    private HBox criarBarraSuperior() {
        HBox barra = new HBox(20);
        barra.setPadding(new Insets(15, 20, 15, 20));
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setStyle("-fx-background-color: #2c3e50;");
        
        Label titulo = new Label("SISTEMA DE CONTROLE DE ESTOQUE");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titulo.setTextFill(Color.WHITE);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label lblUsuario = new Label("Usuario: " + usuario.getNome());
        lblUsuario.setTextFill(Color.WHITE);
        lblUsuario.setFont(Font.font("Arial", 14));
        
        Label lblTipo = new Label("[" + usuario.getTipo() + "]");
        lblTipo.setTextFill(Color.web("#3498db"));
        lblTipo.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        Button btnSair = new Button("Sair");
        btnSair.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 20;");
        btnSair.setOnAction(e -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
            new LoginScreen().start(new Stage());
        });
        
        barra.getChildren().addAll(titulo, spacer, lblUsuario, lblTipo, btnSair);
        return barra;
    }
    
    private VBox criarMenuLateral() {
    VBox menu = new VBox(10);
    menu.setPadding(new Insets(20));
    menu.setPrefWidth(220);
    menu.setStyle("-fx-background-color: #34495e;");
    
    Button btnDashboard = criarBotaoMenu("Dashboard", "#3498db");
    Button btnProdutos = criarBotaoMenu("Produtos", "#2ecc71");
    Button btnMovimentacoes = criarBotaoMenu("Movimentacoes", "#f39c12");
    Button btnRelatorios = criarBotaoMenu("Relatorios", "#9b59b6");
    Button btnUsuarios = criarBotaoMenu("Usuários", "#ece90bff");
    
    
    btnDashboard.setOnAction(e -> {
        root.setCenter(criarDashboard());
    });
    
    btnProdutos.setOnAction(e -> {
        root.setCenter(criarTelaProdutos());
        // A tabela já é atualizada dentro de criarTelaProdutos
    });
    
    
    btnMovimentacoes.setOnAction(e -> {
        VBox telaMovimentacoes = criarTelaMovimentacoes();
        root.setCenter(telaMovimentacoes);
        // Força atualização imediata dos dados
        atualizarTabelaMovimentacoes();
    });
    
    btnRelatorios.setOnAction(e -> {
        root.setCenter(criarTelaRelatorios());
    });


    btnUsuarios.setOnAction(e->{
       
        VBox telaUsuarios = criarTelaUsuarios();
        root.setCenter(telaUsuarios);});
        atualizarTabelaUsuarios();
    if (usuario.getTipo().equals("ADMIN")){
        menu.getChildren().addAll(btnDashboard, btnProdutos, btnMovimentacoes, btnRelatorios,btnUsuarios);
    }else{

        menu.getChildren().addAll(btnDashboard, btnProdutos, btnMovimentacoes, btnRelatorios);
    }
    
    
    return menu;
    }

    private void atualizarTodasTabelas() {
    
    
    if (tabelaProdutos != null) {
        
        atualizarTabelaProdutos();
    } else {
        
    }
    
    if (tabelaMovimentacoes != null) {
        
        atualizarTabelaMovimentacoes();
    } else {
        
    }
}


    
    private Button criarBotaoMenu(String texto, String cor) {
        Button btn = new Button(texto);
        btn.setPrefWidth(180);
        btn.setPrefHeight(50);
        btn.setStyle("-fx-background-color: " + cor + "; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; " +
            "-fx-cursor: hand;");
        
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: derive(" + cor + ", -20%); -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; " +
            "-fx-cursor: hand;"));
        
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: " + cor + "; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; " +
            "-fx-cursor: hand;"));
        
        return btn;
    }
    
    private VBox criarDashboard() {
        VBox dashboard = new VBox(20);
        dashboard.setPadding(new Insets(30));
        dashboard.setStyle("-fx-background-color: #ecf0f1;");
        
        Label titulo = new Label("Dashboard");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        
        // Cards de resumo
        HBox cards = new HBox(20);
        cards.setAlignment(Pos.CENTER);
        
        List<Produtos> produtos = dbManager.listarTodosProdutos();
        int totalProdutos = produtos.size();
        int estoqueBaixo = 0;
        int validadeProx =0;
        double valorTotal = 0;
      
        LocalDate hoje = LocalDate.now();
        
        for (Produtos p : produtos) {
            if (p.getQuantidade() < 10) estoqueBaixo++;
            valorTotal += p.getValorTotal();
        }

        for (Produtos p: produtos){
            LocalDate validade = p.getDataValidade();
            if (ChronoUnit.DAYS.between(hoje, validade)<=15 && ChronoUnit.DAYS.between(hoje, validade)>=0) validadeProx++;

        }
        
        
        cards.getChildren().addAll(
            criarCard("Total de Produtos", String.valueOf(totalProdutos), "#3498db"),
            criarCard("Estoque Baixo", String.valueOf(estoqueBaixo), "#e74c3c"),
            criarCard("Vencimento Próximo", String.valueOf(validadeProx), "#e74c3c"),
            criarCard("Valor Total", String.format("R$ %.2f", valorTotal), "#2ecc71")
        );
        
        // Tabela de produtos com estoque baixo
        Label lblEstoqueBaixo = new Label("Produtos com Estoque Baixo");
        lblEstoqueBaixo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        TableView<Produtos> tabelaBaixo = criarTabelaProdutos();
        tabelaBaixo.setPrefHeight(300);
        
        List<Produtos> produtosBaixos = produtos.stream()
            .filter(p -> p.getQuantidade() < 10)
            .toList();
        tabelaBaixo.getItems().addAll(produtosBaixos);
        
        dashboard.getChildren().addAll(titulo, cards, lblEstoqueBaixo, tabelaBaixo);



        //tabela de produtos com data de validade proxima
        Label lblDataValidadeProx = new Label("Produtos com Validade Próxima");
        lblDataValidadeProx.setFont(Font.font("Arial", FontWeight.BOLD,18));

        TableView<Produtos> tabelaValidade = criarTabelaProdutos();
        tabelaValidade.setPrefHeight(300);

        
        List<Produtos> produtosValidade = produtos.stream().filter(p->{LocalDate validade = p.getDataValidade(); long diasrestantes = ChronoUnit.DAYS.between(hoje, validade);
        return diasrestantes <=15 && diasrestantes>=0;}).collect(Collectors.toList());
        tabelaValidade.getItems().addAll(produtosValidade);


        dashboard.getChildren().addAll(lblDataValidadeProx, tabelaValidade);
        
        return dashboard;
    }
    
    private VBox criarCard(String titulo, String valor, String cor) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setPrefWidth(280);
        card.setPrefHeight(150);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        
        Label lblTitulo = new Label(titulo);
        lblTitulo.setFont(Font.font("Arial", 14));
        lblTitulo.setTextFill(Color.GRAY);
        
        Label lblValor = new Label(valor);
        lblValor.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        lblValor.setTextFill(Color.web(cor));
        
        card.getChildren().addAll(lblTitulo, lblValor);
        
        return card;
    }
    
    private VBox criarTelaProdutos() {
        VBox tela = new VBox(20);
        tela.setPadding(new Insets(30));
        tela.setStyle("-fx-background-color: #ecf0f1;");
        
        Label titulo = new Label("Gerenciar Produtos");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        
        // Barra de acoes
        HBox barraAcoes = new HBox(15);
        barraAcoes.setAlignment(Pos.CENTER_LEFT);
        
        Button btnAdicionar = new Button("+ Novo Produto");
        btnAdicionar.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand;");
        btnAdicionar.setOnAction(e -> abrirDialogNovoProduto());
        
        Button btnEditar = new Button("Editar");
        btnEditar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand;");
        btnEditar.setOnAction(e -> editarProdutoSelecionado());
        
        Button btnRemover = new Button("Remover");
        btnRemover.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand;");
        btnRemover.setOnAction(e -> removerProdutoSelecionado());
        
        Button btnAtualizar = new Button("Atualizar");
        btnAtualizar.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand;");
        btnAtualizar.setOnAction(e -> atualizarTabelaProdutos());
        
        TextField txtBusca = new TextField();
        txtBusca.setPromptText("Buscar produto por nome...");
        txtBusca.setPrefWidth(300);
        txtBusca.setStyle("-fx-padding: 10;");

        Button btnProcurar = new Button("Procurar");
        btnProcurar.setStyle("-fx-background-color: #f09e07ff; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand;");


        btnProcurar.setOnAction(e -> {
    try {
        String nome = txtBusca.getText();
        Produtos p = dbManager.buscarProdutoPorNome(nome);

        if (p != null) {
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Produto encontrado");
            alert.setHeaderText("Detalhes do produto");
            alert.setContentText(
                "Nome: " + p.getNome() + "\n" +
                "Categoria: " + p.getCategoria() + "\n" +
                "Preço: R$ " + p.getPreco() + "\n" +
                "Quantidade: " + p.getQuantidade()
            );
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Produto não encontrado");
            alert.setHeaderText(null);
            alert.setContentText("Nenhum produto encontrado com o nome: " + nome);
            alert.showAndWait();
        }

    } catch (NumberFormatException ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Nome inválido");
        alert.setContentText("Digite um nome válido no campo de busca.");
        alert.showAndWait();
    }
});
        
        barraAcoes.getChildren().addAll(btnAdicionar, btnEditar, btnRemover, btnAtualizar, txtBusca,btnProcurar);
        
        // Tabela de produtos
        tabelaProdutos = criarTabelaProdutos();
        atualizarTabelaProdutos();
        
        tela.getChildren().addAll(titulo, barraAcoes, tabelaProdutos);
        VBox.setVgrow(tabelaProdutos, Priority.ALWAYS);
        
        return tela;
    }
    
    private TableView<Produtos> criarTabelaProdutos() {
        TableView<Produtos> tabela = new TableView<>();
        
        TableColumn<Produtos, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);
        
        TableColumn<Produtos, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(250);
        
        TableColumn<Produtos, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colCategoria.setPrefWidth(150);
        
        TableColumn<Produtos, Integer> colQuantidade = new TableColumn<>("Quantidade");
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colQuantidade.setPrefWidth(100);
        
        TableColumn<Produtos, Double> colPreco = new TableColumn<>("Preco");
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colPreco.setPrefWidth(100);
        
        TableColumn<Produtos, String> colFornecedor = new TableColumn<>("Fornecedor");
        colFornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));
        colFornecedor.setPrefWidth(150);

        TableColumn<Produtos, String> colDataValidade = new TableColumn<>("Data de Validade");
        colDataValidade.setCellValueFactory(new PropertyValueFactory<>("dataValidade"));
        colDataValidade.setPrefWidth(200);
        
        tabela.getColumns().addAll(colId, colNome, colCategoria, colQuantidade, colPreco, colFornecedor, colDataValidade);
        
        return tabela;
    }
    
    private void atualizarTabelaProdutos() {
        if (tabelaProdutos != null) {
            tabelaProdutos.getItems().clear();
            tabelaProdutos.getItems().addAll(dbManager.listarTodosProdutos());
        }
    }
    
    private void abrirDialogNovoProduto() {
        Dialog<Produtos> dialog = new Dialog<>();
        dialog.setTitle("Novo Produto");
        dialog.setHeaderText("Cadastrar novo produto no estoque");
        
        ButtonType btnSalvar = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnSalvar, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField txtNome = new TextField();
        TextField txtCategoria = new TextField();
        TextField txtFornecedor = new TextField();
        TextField txtDescricao = new TextField();
        TextField txtPreco = new TextField();
        TextField txtQuantidade = new TextField();
        DatePicker dataPicker = new DatePicker();
        
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("Categoria:"), 0, 1);
        grid.add(txtCategoria, 1, 1);
        grid.add(new Label("Fornecedor:"), 0, 2);
        grid.add(txtFornecedor, 1, 2);
        grid.add(new Label("Descricao:"), 0, 3);
        grid.add(txtDescricao, 1, 3);
        grid.add(new Label("Preco:"), 0, 4);
        grid.add(txtPreco, 1, 4);
        grid.add(new Label("Quantidade:"), 0, 5);
        grid.add(txtQuantidade, 1, 5);
        grid.add(new Label("Validade:"), 0, 6);
        grid.add(dataPicker, 1, 6);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnSalvar) {
                try {
                    Produtos produto = new Produtos(
                        Integer.parseInt(txtQuantidade.getText()),
                        dataPicker.getValue(),
                        LocalDateTime.now(),
                        "unidade",
                        txtNome.getText(),
                        txtCategoria.getText(),
                        txtFornecedor.getText(),
                        txtDescricao.getText(),
                        Double.parseDouble(txtPreco.getText())
                    );
                    return produto;
                } catch (Exception e) {
                    mostrarAlerta("Erro", "Dados invalidos!");
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(produto -> {
            if (dbManager.inserirProduto(produto)) {
                mostrarAlerta("Sucesso", "Produto cadastrado com sucesso!");
                atualizarTabelaProdutos();
            } else {
                mostrarAlerta("Erro", "Erro ao cadastrar produto!");
            }
        });
    }
    
    private void editarProdutoSelecionado() {
        Produtos selecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Aviso", "Selecione um produto para editar!");
            return;
        }
        /////////////// 
        Dialog<Produtos> dialog = new Dialog<>();
        dialog.setTitle("Editar Produto");
        dialog.setHeaderText("Editar produto no estoque");
        
        ButtonType btnSalvar = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnSalvar, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        

        

        TextField txtNome = new TextField(selecionado.getNome());
        TextField txtCategoria = new TextField(selecionado.getCategoria());
        TextField txtFornecedor = new TextField(selecionado.getFornecedor());
        TextField txtDescricao = new TextField(selecionado.getDescricao());
        TextField txtPreco = new TextField(String.valueOf(selecionado.getPreco()));
        TextField txtQuantidade = new TextField(String.valueOf(selecionado.getQuantidade()));
        DatePicker dataPicker = new DatePicker(selecionado.getDataValidade());

        

        
        
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("Categoria:"), 0, 1);
        grid.add(txtCategoria, 1, 1);
        grid.add(new Label("Fornecedor:"), 0, 2);
        grid.add(txtFornecedor, 1, 2);
        grid.add(new Label("Descricao:"), 0, 3);
        grid.add(txtDescricao, 1, 3);
        grid.add(new Label("Preco:"), 0, 4);
        grid.add(txtPreco, 1, 4);
        grid.add(new Label("Quantidade:"), 0, 5);
        grid.add(txtQuantidade, 1, 5);
        grid.add(new Label("Validade:"), 0, 6);
        grid.add(dataPicker, 1, 6);
        
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnSalvar) {
                try {
                    double preco = Double.parseDouble(txtPreco.getText().trim());
                    int quantidade = Integer.parseInt(txtQuantidade.getText().trim());
                    
                    selecionado.setNome(txtNome.getText());
                    selecionado.setCategoria(txtCategoria.getText());
                    selecionado.setFornecedor(txtFornecedor.getText());
                    selecionado.setDescricao(txtDescricao.getText());
                    selecionado.setPreco(preco);
                    selecionado.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
                    selecionado.setDataValidade(dataPicker.getValue());
                    return selecionado;
                } catch (Exception e) {
                    mostrarAlerta("Erro", "Dados invalidos!");
                    return null;
                }
            }
            return null;
        });
        
    dialog.showAndWait().ifPresent(produtoEditado -> {
        boolean atualizado = false;
        // Preferencial: usar método de update no dbManager
        try {
            atualizado = dbManager.atualizarProduto(selecionado.getId(),selecionado.getNome(),selecionado.getCategoria(),selecionado.getPreco(),selecionado.getQuantidade());
        } catch (NoSuchMethodError | UnsupportedOperationException e) {
            
            atualizado = dbManager.inserirProduto(produtoEditado);
        }

        if (atualizado) {
            mostrarAlerta("Sucesso", "Produto atualizado com sucesso!");
            atualizarTabelaProdutos();
        } else {
            mostrarAlerta("Erro", "Erro ao atualizar produto!");
        }
    });
        //mostrarAlerta("Info", "Funcao em desenvolvimento");
        
        
        
    }
    
    private void removerProdutoSelecionado() {
        Produtos selecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Aviso", "Selecione um produto para remover!");
            return;
        }
        
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Remocao");
        confirmacao.setHeaderText("Deseja realmente remover este produto?");
        confirmacao.setContentText(selecionado.getNome());
        
        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                if (dbManager.removerProduto(selecionado.getId())) {
                    mostrarAlerta("Sucesso", "Produto removido com sucesso!");
                    atualizarTabelaProdutos();
                } else {
                    mostrarAlerta("Erro", "Erro ao remover produto!");
                }
            }
        });
    }

    private VBox criarTelaUsuarios(){
        VBox tela = new VBox(20);
        tela.setPadding(new Insets(30));
        tela.setStyle("-fx-background-color: #ecf0f1;");
        
        Label titulo = new Label("Usuários:");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        HBox barraAcoes = new HBox(15);
        
        Button btnAddUsuario = new Button("Adicionar Usuário");
        btnAddUsuario.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand;");
        btnAddUsuario.setOnAction(e->abrirDialogNovoUsuario());
        
        Button btnRemoverUsuario = new Button("Remover Usuário");
        btnRemoverUsuario.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand;");
        btnRemoverUsuario.setOnAction(e -> removerUsuarioSelecionado());
        
        Button btnEditarUsuario = new Button("Editar Usuário");
        btnEditarUsuario.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand;");
        btnEditarUsuario.setOnAction(e -> editarUsuarioSelecionado());
        
        barraAcoes.getChildren().addAll(btnAddUsuario,btnRemoverUsuario,btnEditarUsuario);
        
        tabelaUsuarios = criarTabelaUsuarios();
        atualizarTabelaUsuarios();
        
        
        tela.getChildren().addAll(titulo, barraAcoes, tabelaUsuarios);
        VBox.setVgrow(tabelaUsuarios, Priority.ALWAYS);

        return tela;
        
    }

    private void atualizarTabelaUsuarios() {
    if (tabelaUsuarios != null) {
        tabelaUsuarios.getItems().clear();
        tabelaUsuarios.getItems().addAll(dbManager.listarUsuarios());
        }
    }

    

    private TableView<Usuario> criarTabelaUsuarios() {
        TableView<Usuario> tabela = new TableView<>();
        
        TableColumn<Usuario, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);
        
        TableColumn<Usuario, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(100);

        TableColumn<Usuario, String> colUsuario = new TableColumn<>("Usuario");
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colUsuario.setPrefWidth(100);
        
        TableColumn<Usuario, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colTipo.setPrefWidth(250);
        
        
        
        
        
        tabela.getColumns().addAll(colId, colNome,colUsuario,colTipo);
        
        return tabela;
    }

    private void abrirDialogNovoUsuario() {
        Dialog<Usuario> dialog = new Dialog<>();
        dialog.setTitle("Novo Usuário");
        dialog.setHeaderText("Cadastrar Novo Usuário no Sistema");
        
        ButtonType btnSalvar = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnSalvar, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField txtNome = new TextField();
        TextField txtUsuario = new TextField();
        TextField txtSenha = new TextField();
        
        ComboBox<String> cbRoles = new ComboBox<>();
        cbRoles.setItems(FXCollections.observableArrayList("ADMIN","USER"));
        cbRoles.setPromptText("Selecione o nível de acesso:");
        cbRoles.getSelectionModel().select("USER");
        VBox layoutRoles = new VBox(10, cbRoles);
        layoutRoles.setStyle("-fx-padding: 10;");
        
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("Usuário:"), 0, 1);
        grid.add(txtUsuario, 1, 1);
        grid.add(new Label("Tipo:"), 0, 3);
        grid.add(cbRoles, 1, 3);
        grid.add(new Label("Senha:"), 0, 2);
        grid.add(txtSenha, 1, 2);
        
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnSalvar) {
                try {
                    Usuario user = new Usuario(txtNome.getText(),txtUsuario.getText(),cbRoles.getValue(),txtSenha.getText());
                    return user;
                } catch (Exception e) {
                    mostrarAlerta("Erro", "Dados invalidos!");
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(user -> {
            if (dbManager.inserirUsuario(user.getNome(),user.getUsuario(),user.getTipo(),user.getSenha())) {
                mostrarAlerta("Sucesso", "Usuário cadastrado com sucesso!");
                atualizarTabelaUsuarios();
            } else {
                mostrarAlerta("Erro", "Erro ao cadastrar usuário!");
            }
        });
    }

    private void editarUsuarioSelecionado(){
        Usuario selecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Aviso", "Selecione um usuário para editar!");
            return;
        }
        /////////////// 
        Dialog<Usuario> dialog = new Dialog<>();
        dialog.setTitle("Editar Usuário");
        dialog.setHeaderText("Editar informações do usuário");
        
        ButtonType btnSalvar = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnSalvar, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        

        

        TextField txtNome = new TextField(selecionado.getNome());
        TextField txtUsuario = new TextField(selecionado.getUsuario());
        ComboBox<String> cbRoles = new ComboBox<>();
        cbRoles.setItems(FXCollections.observableArrayList("ADMIN","USER"));
        cbRoles.setPromptText("Selecione o nível de acesso:");
        cbRoles.getSelectionModel().select("USER");
        VBox layoutRoles = new VBox(10, cbRoles);
        layoutRoles.setStyle("-fx-padding: 10;");
        TextField txtSenha = new TextField(selecionado.getSenha());
        

        

        
        
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("Usuário:"), 0, 1);
        grid.add(txtUsuario, 1, 1);
        grid.add(new Label("Tipo:"), 0, 2);
        grid.add(cbRoles, 1, 2);
        grid.add(new Label("Senha:"), 0, 3);
        grid.add(txtSenha, 1, 3);
        
        
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnSalvar) {
                try {
                    
                    
                    selecionado.setNome(txtNome.getText());
                    selecionado.setUsuario(txtUsuario.getText());
                    selecionado.setTipo(cbRoles.getValue());
                    selecionado.setSenha(txtSenha.getText());
                    
                    return selecionado;
                    
                } catch (Exception e) {
                    mostrarAlerta("Erro", "Dados invalidos!");
                    return null;
                }
            }
            return null;
        });
        
    dialog.showAndWait().ifPresent(userEditado -> {
        boolean atualizado = false;
        // Preferencial: usar método de update no dbManager
        try {
            atualizado = dbManager.atualizarUsuario(selecionado.getId(),selecionado.getNome(),selecionado.getUsuario(),selecionado.getTipo(),selecionado.getSenha());
        } catch (NoSuchMethodError | UnsupportedOperationException e) {
            
            atualizado = dbManager.inserirUsuario(selecionado.getNome(),selecionado.getUsuario(),selecionado.getTipo(),selecionado.getSenha());
        }

        if (atualizado) {
            mostrarAlerta("Sucesso", "Usuário atualizado com sucesso!");
            atualizarTabelaUsuarios();
        } else {
            mostrarAlerta("Erro", "Erro ao atualizar usuário!");
        }
    });
        //mostrarAlerta("Info", "Funcao em desenvolvimento");
        
        
        
    }
    

    private void removerUsuarioSelecionado(){
        Usuario selecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Aviso", "Selecione um usuário para remover!");
            return;
        }
        
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Remocao");
        confirmacao.setHeaderText("Deseja realmente remover este usuário?");
        confirmacao.setContentText(selecionado.getNome());
        
        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                if (dbManager.removerUsuario(selecionado.getId())) {
                    mostrarAlerta("Sucesso", "Usuário removido com sucesso!");
                    atualizarTabelaUsuarios();
                } else {
                    mostrarAlerta("Erro", "Erro ao remover usuário!");
                }
            }
        });
    }
    
    
    
    private VBox criarTelaMovimentacoes() {
        VBox tela = new VBox(20);
        tela.setPadding(new Insets(30));
        tela.setStyle("-fx-background-color: #ecf0f1;");
        
        Label titulo = new Label("Movimentacoes de Estoque");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        
        HBox barraAcoes = new HBox(15);
        
        Button btnEntrada = new Button("+ Entrada");
        btnEntrada.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand;");
        btnEntrada.setOnAction(e -> abrirDialogoMovimentacao(Movimentacao.tipoMovimentacao.ENTRADA));
        
        Button btnSaida = new Button("- Saida");
        btnSaida.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand;");
        btnSaida.setOnAction(e -> abrirDialogoMovimentacao(Movimentacao.tipoMovimentacao.SAIDA));
        
        Button btnAtualizar = new Button("Atualizar");
        btnAtualizar.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand;");
        btnAtualizar.setOnAction(e -> atualizarTabelaMovimentacoes());
        
        barraAcoes.getChildren().addAll(btnEntrada, btnSaida, btnAtualizar);
        
        tabelaMovimentacoes = criarTabelaMovimentacoes();
        atualizarTabelaMovimentacoes();
        
        tela.getChildren().addAll(titulo, barraAcoes, tabelaMovimentacoes);
        VBox.setVgrow(tabelaMovimentacoes, Priority.ALWAYS);
        
        return tela;
    }


    private TableView<Movimentacao> criarTabelaMovimentacoes() {
        TableView<Movimentacao> tabela = new TableView<>();
        
        TableColumn<Movimentacao, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idProduto"));
        colId.setPrefWidth(50);
        
        TableColumn<Movimentacao, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colTipo.setPrefWidth(100);
        
        TableColumn<Movimentacao, String> colProduto = new TableColumn<>("Produto");
        colProduto.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getProduto().getNome()
            )
        );
        colProduto.setPrefWidth(250);
        
        TableColumn<Movimentacao, Integer> colQuantidade = new TableColumn<>("Quantidade");
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colQuantidade.setPrefWidth(100);
        
        TableColumn<Movimentacao, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDatamovimentacao().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                )
            )
        );
        colData.setPrefWidth(150);
        
        tabela.getColumns().addAll(colId, colTipo, colProduto, colQuantidade, colData);
        
        return tabela;
    }
    
    private void atualizarTabelaMovimentacoes() {
        if (tabelaMovimentacoes != null) {
            tabelaMovimentacoes.getItems().clear();
            tabelaMovimentacoes.getItems().addAll(dbManager.listarMovimentacoes());
        }
    }
    
    private void abrirDialogoMovimentacao(Movimentacao.tipoMovimentacao tipo) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(tipo == Movimentacao.tipoMovimentacao.ENTRADA ? 
                       "Registrar Entrada" : "Registrar Saida");
        dialog.setHeaderText("Selecione o produto e a quantidade");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        // ComboBox com produtos
        ComboBox<Produtos> cbProdutos = new ComboBox<>();
        cbProdutos.getItems().addAll(dbManager.listarTodosProdutos());
        cbProdutos.setConverter(new javafx.util.StringConverter<Produtos>() {
            @Override
            public String toString(Produtos produto) {
                return produto != null ? produto.getNome() + " (Estoque: " + produto.getQuantidade() + ")" : "";
            }
            
            @Override
            public Produtos fromString(String string) {
                return null;
            }
        });
        cbProdutos.setPrefWidth(300);
        
        // Campo de quantidade
        TextField tfQuantidade = new TextField();
        tfQuantidade.setPromptText("Digite a quantidade");
        
        grid.add(new Label("Produto:"), 0, 0);
        grid.add(cbProdutos, 1, 0);
        grid.add(new Label("Quantidade:"), 0, 1);
        grid.add(tfQuantidade, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Processar resultado
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Produtos produtoSelecionado = cbProdutos.getValue();
                    int quantidade = Integer.parseInt(tfQuantidade.getText().trim());
                    
                    if (produtoSelecionado == null) {
                        mostrarAlerta("Erro", "Selecione um produto!");
                        return;
                    }
                    
                    if (quantidade <= 0) {
                        mostrarAlerta("Erro", "Quantidade deve ser maior que zero!");
                        return;
                    }
                    
                    // Validar estoque para SAIDA
                    if (tipo == Movimentacao.tipoMovimentacao.SAIDA) {
                        if (produtoSelecionado.getQuantidade() < quantidade) {
                            mostrarAlerta("Erro", 
                                "Estoque insuficiente!\n" +
                                "Disponivel: " + produtoSelecionado.getQuantidade() + "\n" +
                                "Solicitado: " + quantidade);
                            return;
                        }
                    }
                    
                    // Criar movimentacao
                    Movimentacao mov = new Movimentacao(
                        quantidade,
                        produtoSelecionado,
                        tipo,
                        LocalDateTime.now(),
                        produtoSelecionado.getId()
                    );
                    
                    // Registrar movimentacao no banco
                    if (dbManager.inserirMovimentacao(mov, usuario.getId())) {
                        // Atualizar quantidade do produto
                        int novaQuantidade;
                        if (tipo == Movimentacao.tipoMovimentacao.ENTRADA) {
                            novaQuantidade = produtoSelecionado.getQuantidade() + quantidade;
                        } else {
                            novaQuantidade = produtoSelecionado.getQuantidade() - quantidade;
                        }
                        
                        // Atualizar produto no banco
                        dbManager.atualizarProduto(
                            produtoSelecionado.getId(),
                            produtoSelecionado.getNome(),
                            produtoSelecionado.getCategoria(),
                            produtoSelecionado.getPreco(),
                            novaQuantidade
                        );
                        
                        // Atualizar produto local
                        produtoSelecionado.setQuantidade(novaQuantidade);

                        // Mostrar mensagem de sucesso
                        mostrarAlerta("Sucesso", 
                            tipo.toString() + " registrada com sucesso!\n" +
                            "Produto: " + produtoSelecionado.getNome() + "\n" +
                            "Quantidade: " + quantidade + "\n" +
                            "Novo estoque: " + novaQuantidade);

                        // Atualizar todas as tabelas
                        atualizarTodasTabelas();
                    } else {
                        mostrarAlerta("Erro", "Erro ao registrar movimentacao!");
                    }
                    
                } catch (NumberFormatException e) {
                    mostrarAlerta("Erro", "Quantidade invalida! Digite apenas numeros.");
                } catch (Exception e) {
                    mostrarAlerta("Erro", "Erro ao processar movimentacao: " + e.getMessage());
                }
            }
        });
    }

    
    
    private VBox criarTelaRelatorios() {
        VBox tela = new VBox(20);
        tela.setPadding(new Insets(30));
        tela.setStyle("-fx-background-color: #ecf0f1;");
        
        Label titulo = new Label("Relatorios");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        
        VBox botoesRelatorios = new VBox(15);
        
        Button btnRelatorioCompleto = new Button("Gerar Relatorio Completo (HTML)");
        btnRelatorioCompleto.setPrefWidth(300);
        btnRelatorioCompleto.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-padding: 15 30; -fx-font-size: 14px; -fx-cursor: hand;");
        btnRelatorioCompleto.setOnAction(e -> gerarRelatorioHTML());
        
        Button btnRelatorioProdutos = new Button("Relatorio de Produtos");
        btnRelatorioProdutos.setPrefWidth(300);
        btnRelatorioProdutos.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-padding: 15 30; -fx-font-size: 14px; -fx-cursor: hand;");
        btnRelatorioProdutos.setOnAction(e -> mostrarRelatorioProdutos());
        
        Button btnRelatorioMovimentacoes = new Button("Relatorio de Movimentacoes");
        btnRelatorioMovimentacoes.setPrefWidth(300);
        btnRelatorioMovimentacoes.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-padding: 15 30; -fx-font-size: 14px; -fx-cursor: hand;");
        btnRelatorioMovimentacoes.setOnAction(e -> mostrarRelatorioMovimentacoes());
        
        Button btnRelatorioEstoqueBaixo = new Button("Produtos com Estoque Baixo");
        btnRelatorioEstoqueBaixo.setPrefWidth(300);
        btnRelatorioEstoqueBaixo.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-padding: 15 30; -fx-font-size: 14px; -fx-cursor: hand;");
        btnRelatorioEstoqueBaixo.setOnAction(e -> mostrarRelatorioEstoqueBaixo());
        
        botoesRelatorios.getChildren().addAll(
            btnRelatorioCompleto,
            btnRelatorioProdutos,
            btnRelatorioMovimentacoes,
            btnRelatorioEstoqueBaixo
        );
        
        tela.getChildren().addAll(titulo, botoesRelatorios);
        
        return tela;
    }

    private void gerarRelatorioHTML() {
    try {
        
        service.getEstoque().getListadeprodutos().clear();
        service.getEstoque().gethistoricodeMov().clear();
        carregardadosBanco();
        
        
        String dirRelatorios = "relatorios";
        File dir = new File(dirRelatorios);
        if (!dir.exists()) {
            boolean criado = dir.mkdirs();
            System.out.println("Diretório criado: " + criado);
        }
        
        
        Relatorio relatorio = new Relatorio(service);
        String arquivo = relatorio.salvarRelatorioCompleto(Relatorio.FormatoRelatorio.HTML);
        
        
        File arquivoGerado = new File(arquivo);
        String caminhoAbsoluto = arquivoGerado.getAbsolutePath();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Relatório Gerado");
        alert.setHeaderText("Sucesso!");
        alert.setContentText(
            "Relatório gerado com sucesso!\n\n" +
            "Arquivo: " + caminhoAbsoluto + "\n\n" +
            "Abra o arquivo no navegador para visualizar.\n" +
            "Total de produtos: " + service.listarTodosProdutos().size() + "\n" +
            "Total de movimentações: " + service.obterHistorico().size()
        );
        alert.showAndWait();
        
    } catch (Exception e) {
        mostrarAlerta("Erro", "Erro ao gerar relatório: " + e.getMessage());
        e.printStackTrace(); // Para debug no console
    }
}

    private void mostrarRelatorioProdutos() {

        service.getEstoque().getListadeprodutos().clear();
        carregardadosBanco();
        Relatorio relatorio = new Relatorio(service);
        String conteudo = relatorio.gerarRelatorioProdutos();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Relatorio de Produtos");
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.getDialogPane().setPrefWidth(800);
        alert.showAndWait();
    }
    
    private void mostrarRelatorioMovimentacoes() {
        service.getEstoque().gethistoricodeMov().clear();
        carregardadosBanco();
        Relatorio relatorio = new Relatorio(service);
        String conteudo = relatorio.gerarRelatorioMovimentacoes();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Relatorio de Movimentacoes");
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.getDialogPane().setPrefWidth(800);
        alert.showAndWait();
    }
    
    private void mostrarRelatorioEstoqueBaixo() {
        service.getEstoque().getListadeprodutos().clear();
        carregardadosBanco();
        Relatorio relatorio = new Relatorio(service);
        String conteudo = relatorio.relatorioProdutosEstoqueBaixo(10);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Produtos com Estoque Baixo");
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.getDialogPane().setPrefWidth(800);
        alert.showAndWait();
    }
    
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
