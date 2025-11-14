package com.estoque;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Produtos {
<<<<<<< HEAD:principal/src/main/java/com/estoque/Produtos.java
    
=======
    private static int contador =1;
>>>>>>> bbe135daed218b01db81f5abe32b0b9e71df5899:Produtos.java
    private int quantidade;
    private String nome;
    private double preco;
    private  int id;
    private String categoria;
    private String fornecedor;
    private LocalDate dataValidade;
    private String descricao;
    private LocalDateTime dataCadastro;
    private String unidadeMedida;

<<<<<<< HEAD:principal/src/main/java/com/estoque/Produtos.java
    public Produtos(int id,int quantidade,LocalDate dataValidade,LocalDateTime dataCadastro, String unidadeMedida, String nome, String categoria,String fornecedor,
=======
    public Produtos(int quantidade,LocalDate dataValidade,LocalDateTime dataCadastro, String unidadeMedida, String nome, String categoria,String fornecedor,
>>>>>>> bbe135daed218b01db81f5abe32b0b9e71df5899:Produtos.java
    String descricao,double preco){

        this.quantidade = quantidade;
        this.id = contador++;
        this.dataValidade = dataValidade;
        this.dataCadastro = dataCadastro;
        this.unidadeMedida = unidadeMedida;
        this.nome = nome;
        this.categoria = categoria;
        this.fornecedor = fornecedor;
        this.descricao = descricao;
        this.preco = preco;

    }

    public Produtos(int quantidade, LocalDate dataValidade, LocalDateTime dataCadastro, 
                        String unidadeMedida, String nome, String categoria, String fornecedor, 
                        String descricao, double preco) {
            this(0, quantidade, dataValidade, dataCadastro, 
                unidadeMedida, nome, categoria, fornecedor, 
                descricao, preco); // Chama o construtor principal passando '0' para o ID
        }


    public void setId(int id){
        this.id = id;
    }

    public double getValorTotal(){
        return preco * quantidade;
    }

    public int getQuantidade(){
        return quantidade;
    }

    public int getId(){
        return id;
    }

    public LocalDate getDataValidade(){
        return dataValidade;
    }

    public LocalDateTime getDataCadastro(){
        return dataCadastro;
    }

    public String getUnidadeMedida(){
        return unidadeMedida;
    }

    public String getNome(){
        return nome;
    }

    public String getCategoria(){
        return categoria;
    }

    public String getFornecedor(){
        return fornecedor;
    }

    public String getDescricao(){
        return descricao;
    }

    public double getPreco(){
        return preco;
    }

////////////////////////////////////////////////////////////////


    public void setQuantidade(int quantidade) {
    this.quantidade = quantidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
        
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }


    @Override
    public String toString() {
        return "Produto {" +
            "ID=" + id +
            ", Nome='" + nome + '\'' +
            ", Categoria='" + categoria + '\'' +
            ", Quantidade=" + quantidade +
            ", Preço=" + preco +
            ", Fornecedor='" + fornecedor + '\'' +
            ", UnidadeMedida=" + unidadeMedida +
            ", DataCadastro=" + dataCadastro +
            ", DataValidade=" + dataValidade +
            ", Descrição='" + descricao + '\'' +
            '}';
    }


}
