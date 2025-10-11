import java.util.List;

public class Relatorio {
    public void gerarRelatorioProdutos(Estoque estoque){
        List <Produtos> listadeprodutos = estoque.getListadeprodutos();

        if (listadeprodutos.isEmpty()){
            return;
        }

        for (Produtos p : listadeprodutos) {
            System.out.println(p);
        }
    }

    public void gerarRelatorioMovimentacoes(Estoque estoque){
        List <Movimentacao> historicodeMov = estoque.gethistoricodeMov();
        for (Movimentacao m : historicodeMov) {
            System.out.println(m);
        }
    }
}
