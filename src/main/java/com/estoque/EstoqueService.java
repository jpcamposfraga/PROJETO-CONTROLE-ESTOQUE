package com.estoque;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Camada de serviço - contém a lógica de negócio
 * Preparado para integração com GUI
 */
public class EstoqueService {
    private final Estoque estoque;
    
    public EstoqueService() {
        this.estoque = new Estoque();
    }
    
    /**
     * Adiciona produto ao estoque com validações
     */
    public ResultadoOperacao adicionarProduto(Produtos produto) {
        try {
            // Validações
            if (produto == null) {
                return ResultadoOperacao.erro("Produto não pode ser nulo");
            }
            
            if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
                return ResultadoOperacao.erro("Nome do produto é obrigatório");
            }
            
            if (produto.getPreco() <= 0) {
                return ResultadoOperacao.erro("Preço deve ser maior que zero");
            }
            
            if (produto.getQuantidade() < 0) {
                return ResultadoOperacao.erro("Quantidade não pode ser negativa");
            }
            
            estoque.adicionarProduto(produto);
            return ResultadoOperacao.sucesso("Produto adicionado com sucesso", produto);
            
        } catch (Exception e) {
            return ResultadoOperacao.erro("Erro ao adicionar produto: " + e.getMessage());
        }
    }
    
    /**
     * Registra movimentação com validações completas
     */
    public ResultadoOperacao registrarMovimentacao(Movimentacao mov) {
        try {
            if (mov == null) {
                return ResultadoOperacao.erro("Movimentação não pode ser nula");
            }
            
            if (mov.getQuantidade() <= 0) {
                return ResultadoOperacao.erro("Quantidade deve ser maior que zero");
            }
            
            Produtos produto = mov.getProduto();
            Produtos produtoEstoque = estoque.buscarProdutoporId(produto.getId());
            
            if (produtoEstoque == null) {
                return ResultadoOperacao.erro("Produto ID " + produto.getId() + " não encontrado");
            }
            
            // Validação específica para saída
            if (mov.isSaida() && produtoEstoque.getQuantidade() < mov.getQuantidade()) {
                return ResultadoOperacao.erro(
                    String.format("Estoque insuficiente. Disponível: %d, Solicitado: %d", 
                    produtoEstoque.getQuantidade(), mov.getQuantidade())
                );
            }
            
            estoque.registrarMovimentacao(mov);
            return ResultadoOperacao.sucesso(
                "Movimentação registrada com sucesso. Novo estoque: " + produtoEstoque.getQuantidade(),
                mov
            );
            
        } catch (Exception e) {
            return ResultadoOperacao.erro("Erro ao registrar movimentação: " + e.getMessage());
        }
    }
    
    /**
     * Busca produto por ID retornando Optional
     */
    public Optional<Produtos> buscarProduto(int id) {
        return Optional.ofNullable(estoque.buscarProdutoporId(id));
    }
    
    /**
     * Lista todos os produtos
     */
    public List<Produtos> listarTodosProdutos() {
        return new ArrayList<>(estoque.listarProdutos());
    }
    
    /**
     * 
     */
    public List<Produtos> buscarPorCategoria(String categoria) {
        List<Produtos> resultado = new ArrayList<>();
        for (Produtos p : estoque.listarProdutos()) {
            if (p.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.add(p);
            }
        }
        return resultado;
    }
    
    /**
     * Busca produtos com estoque baixo
     */
    public List<Produtos> produtosComEstoqueBaixo(int quantidadeMinima) {
        List<Produtos> resultado = new ArrayList<>();
        for (Produtos p : estoque.listarProdutos()) {
            if (p.getQuantidade() <= quantidadeMinima) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<Produtos> produtosComValidadeProx(){
        
        List<Produtos> resultado = new ArrayList<>();
        for (Produtos p: estoque.listarProdutos()){
            LocalDate validade = p.getDataValidade();
            if (ChronoUnit.DAYS.between(LocalDate.now(), validade)<=15 && ChronoUnit.DAYS.between(LocalDate.now(), validade)>=0){
                resultado.add(p);

            }
            
        }
        return resultado;
        
    }
    
    /**
     * Remove produto por ID
     */
    public ResultadoOperacao removerProduto(int id) {
        Produtos produto = estoque.buscarProdutoporId(id);
        if (produto == null) {
            return ResultadoOperacao.erro("Produto não encontrado");
        }
        
        if (produto.getQuantidade() > 0) {
            return ResultadoOperacao.erro(
                "Não é possível remover produto com estoque. Quantidade atual: " + produto.getQuantidade()
            );
        }
        
        boolean removido = estoque.removerProdutoPorId(id);
        if (removido) {
            return ResultadoOperacao.sucesso("Produto removido com sucesso", produto);
        }
        return ResultadoOperacao.erro("Erro ao remover produto");
    }
    
    /**
     * Calcula valor total do estoque (MÉTODO FALTANTE)
     */
    public double calcularValorTotalEstoque() {
        double total = 0;
        for (Produtos p : estoque.listarProdutos()) {
            total += p.getValorTotal();
        }
        return total;
    }
    
    /**
     * Retorna histórico de movimentações (MÉTODO FALTANTE)
     */
    public List<Movimentacao> obterHistorico() {
        return new ArrayList<>(estoque.gethistoricodeMov());
    }
    
    /**
     * Atualiza informações do produto (MÉTODO FALTANTE)
     */
    public ResultadoOperacao atualizarProduto(int id, String nome, double preco, String categoria) {
        Produtos produto = estoque.buscarProdutoporId(id);
        if (produto == null) {
            return ResultadoOperacao.erro("Produto não encontrado");
        }
        
        if (nome != null && !nome.trim().isEmpty()) {
            produto.setNome(nome);
        }
        if (preco > 0) {
            produto.setPreco((float) preco);
        }
        if (categoria != null && !categoria.trim().isEmpty()) {
            produto.setCategoria(categoria);
        }
        
        return ResultadoOperacao.sucesso("Produto atualizado com sucesso", produto);
    }
    
    /**
     * Retorna referência ao estoque (para compatibilidade com Relatorio atual)
     */
    public Estoque getEstoque() {
        return estoque;
    }
}