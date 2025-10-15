
import java.time.LocalDateTime;

public class Movimentacao {
    private static int contador=1;
    private final int idProduto;
    private final tipoMovimentacao tipo;
    private final int quantidade;
    private final LocalDateTime dataMovimentacao;
    private final Produtos produto;


    public Movimentacao(int quantidade, Produtos produto,tipoMovimentacao tipo,LocalDateTime dataMovimentacao ){
        this.idProduto = contador++;
        this.produto = produto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataMovimentacao = LocalDateTime.now();


    }

    public enum tipoMovimentacao{
        ENTRADA, SAIDA
    }

    public Produtos getProduto(){return produto;}
  
    public int getQuantidade(){return quantidade;}

    public int getIdProduto(){return idProduto;}

    public tipoMovimentacao getTipo(){return tipo;}

    public LocalDateTime getDatamovimentacao(){return dataMovimentacao;}

    public boolean isEntrada(){return this.tipo==tipoMovimentacao.ENTRADA;}

    public boolean isSaida(){return this.tipo==tipoMovimentacao.SAIDA;}



    

    @Override
    public String toString() {
        return "Movimentação: " + tipo + " | Produto: " + produto.getNome() + 
            " | Quantidade: " + quantidade + 
            " | Data: " + dataMovimentacao;
    }

}
