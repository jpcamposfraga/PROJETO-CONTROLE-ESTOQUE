package com.estoque;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Estoque {
    private final List<Produtos> listadeprodutos;
    private LocalDateTime ultimaAtualizacao;
    private final List<Movimentacao> historicodeMov;


    public Estoque(){
        this.listadeprodutos = new ArrayList<>();
        this.ultimaAtualizacao = LocalDateTime.now();
        this.historicodeMov = new ArrayList<>();
    }
    



    public void registrarMovimentacao(Movimentacao mov){
        Produtos produto = mov.getProduto();

        Produtos produtoNoEstoque = buscarProdutoporId(produto.getId());

        if (produtoNoEstoque != null) {
            if (mov.isEntrada()) {
                // Se for entrada, aumenta a quantidade.
                produtoNoEstoque.setQuantidade(produtoNoEstoque.getQuantidade() + mov.getQuantidade());
            } else if (mov.isSaida()) {
                // Se for saída, verifica se tem estoque suficiente antes de diminuir.
                if (produtoNoEstoque.getQuantidade() >= mov.getQuantidade()) {
                    produtoNoEstoque.setQuantidade(produtoNoEstoque.getQuantidade() - mov.getQuantidade());
                } else {
                    System.out.println("ERRO: Saída não registrada. Quantidade insuficiente para o produto ID: " + produto.getId());
                    return; // Sai do método se houver erro de estoque
                }
            }
            
            
            historicodeMov.add(mov); 
            this.ultimaAtualizacao = LocalDateTime.now();
        } else {
            System.out.println("ERRO: Movimentação não registrada. Produto ID " + produto.getId() + " não encontrado no estoque.");
        }  
    }

    public List<Movimentacao> gethistoricodeMov(){
        return this.historicodeMov;
    }

    public List<Produtos> listarProdutos(){
        return new ArrayList<>(listadeprodutos);
    }


    public LocalDateTime ultimaAtualizacao(){
        return ultimaAtualizacao;
    }

    public void adicionarProduto(Produtos produto){
        listadeprodutos.add(produto);
    } 

    public boolean removerProdutoPorId(int id){
        return listadeprodutos.removeIf(p-> p.getId()==id);
    }

    public Produtos buscarProdutoporId(int id){
        for (Produtos p : listadeprodutos){
            if (p.getId()==id){
                return p;
            }
        }
        return null;
    }


    
    public List<Produtos> getListadeprodutos(){
        return listadeprodutos;
        
    }    
    
}
