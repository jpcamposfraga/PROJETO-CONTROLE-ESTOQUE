package com.estoque;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CadastroScreen extends Application{
    private DatabaseManager dbManager;
    private Usuario usuarioLogado;
    private Stage primaryStage;

    private boolean senhasIguais(String txtSenha,String txtConfirmSenha){
            if (txtSenha.equals(txtConfirmSenha)){
                return true;
            }
            return false;
        }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        dbManager = DatabaseManager.getInstance();
        
        primaryStage.setTitle("Sistema de Estoque - Cadastro");
        primaryStage.setScene(criarTelaCadastro(primaryStage));
        primaryStage.setWidth(400);
        primaryStage.setHeight(500);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public Scene criarTelaCadastro(Stage stage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");
        
        // Container branco central
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(40));
        container.setMaxWidth(350);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 5);");
        
        // Titulo
        Label titulo = new Label("Sistema de Estoque");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titulo.setTextFill(Color.web("#667eea"));
        
        Label subtitulo = new Label("Crie seu cadastro para continuar");
        subtitulo.setFont(Font.font("Arial", 14));
        subtitulo.setTextFill(Color.GRAY);

        //Funcao do usuario
        ComboBox<String> cbRoles = new ComboBox<>();
        cbRoles.setItems(FXCollections.observableArrayList("ADMIN","USER"));
        cbRoles.setPromptText("Selecione o nível de acesso:");
        cbRoles.getSelectionModel().select("USER");
        VBox layoutRoles = new VBox(10, cbRoles);
        layoutRoles.setStyle("-fx-padding: 10;");

        // Campo nome
        VBox nomeBox = new VBox(5);
        Label lblNome = new Label("Nome");
        lblNome.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome de usuario");
        txtNome.setStyle("-fx-padding: 10; -fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 5; -fx-background-radius: 5;");
        nomeBox.getChildren().addAll(lblNome, txtNome);

        // Campo usuario
        VBox usuarioBox = new VBox(5);
        Label lblUsuario = new Label("Usuario");
        lblUsuario.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Nome de usuario");
        txtUsuario.setStyle("-fx-padding: 10; -fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 5; -fx-background-radius: 5;");
        usuarioBox.getChildren().addAll(lblUsuario, txtUsuario);
        
        // Campo senha
        VBox senhaBox = new VBox(5);
        Label lblSenha = new Label("Crie sua Senha");
        lblSenha.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("Digite sua senha");
        txtSenha.setStyle("-fx-padding: 10; -fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 5; -fx-background-radius: 5;");
        senhaBox.getChildren().addAll(lblSenha, txtSenha);

        // Campo confirmar senha
        VBox confirmSenhaBox = new VBox(5);
        Label lblConfirmSenha = new Label("Confirme a senha:");
        lblConfirmSenha.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        PasswordField txtConfirmSenha = new PasswordField();
        txtConfirmSenha.setPromptText("Digite sua senha novamente");
        txtConfirmSenha.setStyle("-fx-padding: 10; -fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 5; -fx-background-radius: 5;");
        confirmSenhaBox.getChildren().addAll(lblConfirmSenha, txtConfirmSenha);

        
        // Label de erro
        Label lblErro = new Label();
        lblErro.setTextFill(Color.RED);
        lblErro.setVisible(false);
        
        // Botao cadastro
        Button btnCadastro = new Button("CADASTRAR");
        btnCadastro.setPrefWidth(270);
        btnCadastro.setPrefHeight(45);
        btnCadastro.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5; " +
            "-fx-cursor: hand;");
        
        btnCadastro.setOnMouseEntered(e -> 
            btnCadastro.setStyle("-fx-background-color: #5568d3; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5; " +
                "-fx-cursor: hand;"));
        
        btnCadastro.setOnMouseExited(e -> 
            btnCadastro.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5; " +
                "-fx-cursor: hand;"));
        
        btnCadastro.setOnAction(e -> {
            String usuario = txtUsuario.getText();
            String senha = txtSenha.getText();
            String confirmarSenha = txtConfirmSenha.getText();
            
            if (usuario.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
                lblErro.setText("Preencha todos os campos!");
                lblErro.setVisible(true);
                return;
            }

            if (!senhasIguais(confirmarSenha, senha)) {
                lblErro.setText("Senhas não coincidem!");
                lblErro.setVisible(true);
                return;
            }
            
            boolean cadastrado = dbManager.inserirUsuario(txtNome.getText(),txtUsuario.getText(),txtSenha.getText(),cbRoles.getSelectionModel().getSelectedItem());
            
            if (cadastrado) {
                usuarioLogado = dbManager.autenticarUsuario(usuario, senha);
                MainScreen mainScreen = new MainScreen(usuarioLogado,dbManager);
                mainScreen.start(new Stage());
                stage.close();  
            }else{
                lblErro.setText("Erro ao cadastrar. Usuário já existe!");
                lblErro.setVisible(true);
            }
        });
        
        // Enter para cadastro
        txtSenha.setOnAction(e -> btnCadastro.fire());
        
        
        
        container.getChildren().addAll(
            titulo, subtitulo, usuarioBox, senhaBox,confirmSenhaBox,cbRoles, 
            lblErro, btnCadastro
        );
        
        root.getChildren().add(container);
        
        return new Scene(root);
    }
    
}
