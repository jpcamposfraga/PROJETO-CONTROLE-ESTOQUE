
import java.time.LocalDateTime;

public class Movimentacao {
    private int idProduto;
    private tipoMovimentacao tipo;
    private int quantidade;
    private LocalDateTime dataMovimentacao;
    private Produtos produto;


    public Movimentacao(int idProduto, Produtos produto,tipoMovimentacao tipo,int quantidade,LocalDateTime dataMovimentacao ){
        this.idProduto = idProduto;
        this.produto = produto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataMovimentacao = LocalDateTime.now();


    }

    public enum tipoMovimentacao{
        ENTRADA, SAIDA
    }

    public Produtos getProduto(){
        return produto;

    }

    public boolean isEntrada(){
        return this.tipo==tipoMovimentacao.ENTRADA;
    }

    public boolean isSaida(){
        return this.tipo==tipoMovimentacao.SAIDA;
    }

    public int getQuantidade(){
        return quantidade;
    }

    @Override
    public String toString() {
        return "Movimentação: " + tipo + " | Produto: " + produto.getNome() + 
            " | Quantidade: " + quantidade + 
            " | Data: " + dataMovimentacao;
    }

}
