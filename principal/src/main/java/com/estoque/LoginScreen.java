package com.estoque;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginScreen extends Application {
    private DatabaseManager dbManager;
    private Usuario usuarioLogado;
    
    @Override
    public void start(Stage primaryStage) {
        dbManager = DatabaseManager.getInstance();
        
        primaryStage.setTitle("Sistema de Estoque - Login");
        primaryStage.setScene(criarTelaLogin(primaryStage));
        primaryStage.setWidth(400);
        primaryStage.setHeight(500);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    private Scene criarTelaLogin(Stage stage) {
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
        
        Label subtitulo = new Label("Faca login para continuar");
        subtitulo.setFont(Font.font("Arial", 14));
        subtitulo.setTextFill(Color.GRAY);
        
        // Campo usuario
        VBox usuarioBox = new VBox(5);
        Label lblUsuario = new Label("Usuario");
        lblUsuario.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Digite seu usuario");
        txtUsuario.setStyle("-fx-padding: 10; -fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 5; -fx-background-radius: 5;");
        usuarioBox.getChildren().addAll(lblUsuario, txtUsuario);
        
        // Campo senha
        VBox senhaBox = new VBox(5);
        Label lblSenha = new Label("Senha");
        lblSenha.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("Digite sua senha");
        txtSenha.setStyle("-fx-padding: 10; -fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 5; -fx-background-radius: 5;");
        senhaBox.getChildren().addAll(lblSenha, txtSenha);
        
        // Label de erro
        Label lblErro = new Label();
        lblErro.setTextFill(Color.RED);
        lblErro.setVisible(false);
        
        // Botao login
        Button btnLogin = new Button("ENTRAR");
        btnLogin.setPrefWidth(270);
        btnLogin.setPrefHeight(45);
        btnLogin.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5; " +
            "-fx-cursor: hand;");
        
        btnLogin.setOnMouseEntered(e -> 
            btnLogin.setStyle("-fx-background-color: #5568d3; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5; " +
                "-fx-cursor: hand;"));
        
        btnLogin.setOnMouseExited(e -> 
            btnLogin.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5; " +
                "-fx-cursor: hand;"));
        
        btnLogin.setOnAction(e -> {
            String usuario = txtUsuario.getText();
            String senha = txtSenha.getText();
            
            if (usuario.isEmpty() || senha.isEmpty()) {
                lblErro.setText("Preencha todos os campos!");
                lblErro.setVisible(true);
                return;
            }
            
            usuarioLogado = dbManager.autenticarUsuario(usuario, senha);
            
            if (usuarioLogado != null) {
                MainScreen mainScreen = new MainScreen(usuarioLogado, dbManager);
                mainScreen.start(new Stage());
                stage.close();
            } else {
                lblErro.setText("Usuario ou senha invalidos!");
                lblErro.setVisible(true);
                txtSenha.clear();
            }
        });
        
        // Enter para login
        txtSenha.setOnAction(e -> btnLogin.fire());
        
        // Info usuario padrao
        Label lblInfo = new Label("Usuario padrao: admin / admin123");
        lblInfo.setFont(Font.font("Arial", 10));
        lblInfo.setTextFill(Color.GRAY);
        
        container.getChildren().addAll(
            titulo, subtitulo, usuarioBox, senhaBox, 
            lblErro, btnLogin, lblInfo
        );
        
        root.getChildren().add(container);
        
        return new Scene(root);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}