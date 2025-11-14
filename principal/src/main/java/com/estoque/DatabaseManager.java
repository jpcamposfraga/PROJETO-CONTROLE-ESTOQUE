package com.estoque;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static DatabaseManager instance;
    private static final String DB_URL = "jdbc:sqlite:estoque.db";
    private Connection conexao;
    
    private DatabaseManager() {
        conectar();
        criarTabelas();
    }

    public static synchronized DatabaseManager getInstance(){
        if(instance==null){
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    private void conectar() {
        try {
            Class.forName("org.sqlite.JDBC");
            conexao = DriverManager.getConnection(DB_URL);
            System.out.println("Conectado ao banco de dados");
        } catch (Exception e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
    }
    
    private void criarTabelas() {
        try {
            Statement stmt = conexao.createStatement();
            
            String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "usuario TEXT UNIQUE NOT NULL," +
                "senha TEXT NOT NULL," +
                "tipo TEXT NOT NULL," +
                "ativo INTEGER DEFAULT 1," +
                "data_cadastro TEXT NOT NULL)";
            stmt.execute(sqlUsuarios);
            
            String sqlProdutos = "CREATE TABLE IF NOT EXISTS produtos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "categoria TEXT NOT NULL," +
                "fornecedor TEXT," +
                "descricao TEXT," +
                "preco REAL NOT NULL," +
                "quantidade INTEGER DEFAULT 0," +
                "unidade_medida TEXT," +
                "data_validade TEXT," +
                "data_cadastro TEXT NOT NULL)";
            stmt.execute(sqlProdutos);
            
            String sqlMovimentacoes = "CREATE TABLE IF NOT EXISTS movimentacoes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "produto_id INTEGER NOT NULL," +
                "tipo TEXT NOT NULL," +
                "quantidade INTEGER NOT NULL," +
                "usuario_id INTEGER," +
                "data_movimentacao TEXT NOT NULL," +
                "FOREIGN KEY(produto_id) REFERENCES produtos(id)," +
                "FOREIGN KEY(usuario_id) REFERENCES usuarios(id))";
            stmt.execute(sqlMovimentacoes);
            
            criarUsuarioPadrao();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }
    
    private void criarUsuarioPadrao() {
        try {
            String sql = "INSERT OR IGNORE INTO usuarios (nome, usuario, senha, tipo, data_cadastro) " +
                "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, "Administrador");
            pstmt.setString(2, "admin");
            pstmt.setString(3, "admin123");
            pstmt.setString(4, "ADMIN");
            pstmt.setString(5, LocalDateTime.now().toString());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            // Usuario ja existe
        }
    }
    
    public boolean inserirProduto(Produtos produto) {
        String sql = "INSERT INTO produtos (nome, categoria, fornecedor, descricao, preco, " +
            "quantidade, unidade_medida, data_validade, data_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, produto.getNome());
            pstmt.setString(2, produto.getCategoria());
            pstmt.setString(3, produto.getFornecedor());
            pstmt.setString(4, produto.getDescricao());
            pstmt.setDouble(5, produto.getPreco());
            pstmt.setInt(6, produto.getQuantidade());
            pstmt.setString(7, produto.getUnidadeMedida());
            pstmt.setString(8, produto.getDataValidade().toString());
            pstmt.setString(9, produto.getDataCadastro().toString());
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir produto: " + e.getMessage());
            return false;
        }
    }
    
    public List<Produtos> listarTodosProdutos() {
        List<Produtos> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos ORDER BY nome";
        
        try {
            
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Produtos p = new Produtos(
                    rs.getInt("id"),
                    rs.getInt("quantidade"),
                    LocalDate.parse(rs.getString("data_validade")),
                    LocalDateTime.parse(rs.getString("data_cadastro")),
                    rs.getString("unidade_medida"),
                    rs.getString("nome"),
                    rs.getString("categoria"),
                    rs.getString("fornecedor"),
                    rs.getString("descricao"),
                    rs.getDouble("preco")
                );
                produtos.add(p);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }
        
        return produtos;
    }
    
    public boolean atualizarProduto(int id, String nome, String categoria, double preco, int quantidade) {
        String sql = "UPDATE produtos SET nome = ?, categoria = ?, preco = ?, quantidade = ? WHERE id = ?";
        
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, nome);
            pstmt.setString(2, categoria);
            pstmt.setDouble(3, preco);
            pstmt.setInt(4, quantidade);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
            return false;
        }
    }
    
public boolean removerProduto(int id) {
        String sql = "DELETE FROM produtos WHERE id = ?";
        
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover produto: " + e.getMessage());
            return false;
        }
    }
    
    public Produtos buscarProdutoPorId(int id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Produtos p = new Produtos(
                    rs.getInt("id"),
                    rs.getInt("quantidade"),
                    LocalDate.parse(rs.getString("data_validade")),
                    LocalDateTime.parse(rs.getString("data_cadastro")),
                    rs.getString("unidade_medida"),
                    rs.getString("nome"),
                    rs.getString("categoria"),
                    rs.getString("fornecedor"),
                    rs.getString("descricao"),
                    rs.getDouble("preco")
                );
                rs.close();
                pstmt.close();
                return p;
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto: " + e.getMessage());
        }
        
        return null;
    }

    public Produtos buscarProdutoPorNome(String nome){
        String sql = "SELECT * FROM produtos WHERE nome = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, nome);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Produtos p = new Produtos(
                    rs.getInt("id"),
                    rs.getInt("quantidade"),
                    LocalDate.parse(rs.getString("data_validade")),
                    LocalDateTime.parse(rs.getString("data_cadastro")),
                    rs.getString("unidade_medida"),
                    rs.getString("nome"),
                    rs.getString("categoria"),
                    rs.getString("fornecedor"),
                    rs.getString("descricao"),
                    rs.getDouble("preco")
                );
                rs.close();
                pstmt.close();
                return p;
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto: " + e.getMessage());
        }
        
        return null;

    }
    
    public boolean inserirMovimentacao(Movimentacao mov, int usuarioId) {
        String sql = "INSERT INTO movimentacoes (produto_id, tipo, quantidade, usuario_id, data_movimentacao) " +
            "VALUES (?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setInt(1, mov.getProduto().getId());
            pstmt.setString(2, mov.getTipo().toString());
            pstmt.setInt(3, mov.getQuantidade());
            pstmt.setInt(4, usuarioId);
            pstmt.setString(5, mov.getDatamovimentacao().toString());
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir movimentacao: " + e.getMessage());
            return false;
        }
    }
    
    public List<Movimentacao> listarMovimentacoes() {
    List<Movimentacao> movimentacoes = new ArrayList<>();
    
    // CORREÇÃO: Usar aliases explícitos para evitar conflito de nomes
    String sql = "SELECT " +
        "m.id as mov_id, " +
        "m.produto_id, " +
        "m.tipo, " +
        "m.quantidade as mov_quantidade, " +  // ALIAS para quantidade da movimentação
        "m.usuario_id, " +
        "m.data_movimentacao, " +
        "p.id as prod_id, " +
        "p.nome, " +
        "p.categoria, " +
        "p.preco, " +
        "p.quantidade as prod_quantidade, " +  // ALIAS para quantidade do produto
        "p.fornecedor, " +
        "p.descricao, " +
        "p.unidade_medida, " +
        "p.data_validade, " +
        "p.data_cadastro " +
        "FROM movimentacoes m " +
        "JOIN produtos p ON m.produto_id = p.id " +
        "ORDER BY m.data_movimentacao DESC";
    
    try {
        Statement stmt = conexao.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            // Criar objeto Produto usando os aliases corretos
            Produtos p = new Produtos(
                rs.getInt("prod_id"),              // ID do produto
                rs.getInt("prod_quantidade"),      // Quantidade em estoque
                LocalDate.parse(rs.getString("data_validade")),
                LocalDateTime.parse(rs.getString("data_cadastro")),
                rs.getString("unidade_medida"),
                rs.getString("nome"),
                rs.getString("categoria"),
                rs.getString("fornecedor"),
                rs.getString("descricao"),
                rs.getDouble("preco")
            );
            
            // Criar objeto Movimentacao usando o alias correto
            Movimentacao mov = new Movimentacao(
                rs.getInt("mov_quantidade"),       // Quantidade da movimentação
                p,
                Movimentacao.tipoMovimentacao.valueOf(rs.getString("tipo")),
                LocalDateTime.parse(rs.getString("data_movimentacao")),
                rs.getInt("produto_id")
            );
            
            movimentacoes.add(mov);
        }
        
        rs.close();
        stmt.close();
    } catch (SQLException e) {
        System.err.println("Erro ao listar movimentacoes: " + e.getMessage());
        e.printStackTrace();  // Adicionar stacktrace para debug
    } catch (Exception e) {
        System.err.println("Erro ao processar movimentacao: " + e.getMessage());
        e.printStackTrace();
    }
    
    return movimentacoes;
}
    
    public boolean inserirUsuario(String nome, String usuario, String senha, String tipo) {
        String sql = "INSERT INTO usuarios (nome, usuario, senha, tipo, data_cadastro) VALUES (?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, nome);
            pstmt.setString(2, usuario);
            pstmt.setString(3, senha);
            pstmt.setString(4, tipo);
            pstmt.setString(5, LocalDateTime.now().toString());
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuario: " + e.getMessage());
            return false;
        }
    }
    
    public Usuario autenticarUsuario(String usuario, String senha) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND senha = ? AND ativo = 1";
        
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, usuario);
            pstmt.setString(2, senha);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Usuario user = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("usuario"),
                    rs.getString("tipo")
                );
                rs.close();
                pstmt.close();
                return user;
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar usuario: " + e.getMessage());
        }
        
        return null;
    }
    
    public void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("Conexao fechada");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexao: " + e.getMessage());
        }
    }
}