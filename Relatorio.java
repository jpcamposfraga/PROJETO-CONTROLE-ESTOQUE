import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Produtos produtoMaisVendido(List<Movimentacao> movimentacao){
        Map <Produtos, Integer> totalSaidas = new HashMap<>();
//pra cada movimentacao em uma lista de mov, ele vai ver se o tipo e saida 
//e se for pegar o produto e a qtd e botar num map o total de saidas, junto com o produto e somar com a qtd
        for (Movimentacao mov : movimentacao) {
            if(mov.getTipo()==Movimentacao.tipoMovimentacao.SAIDA){
                Produtos produto = mov.getProduto();
                int quantidade = mov.getQuantidade();
                totalSaidas.put(produto, totalSaidas.getOrDefault(produto, 0)+quantidade);
            }
            
        }

        Produtos maisVendido = null;
        int maiorQuantidade = 0;
//aqui ele verifica qual e o maior valor do map de totalSaidas (no caso o mais vendido),
//e retorna esse valor, ele tbm pega a qtd
        for (Map.Entry<Produtos, Integer> entry : totalSaidas.entrySet()) {
            if (entry.getValue()>maiorQuantidade){
                maiorQuantidade = entry.getValue();
                maisVendido = entry.getKey();
            }
            
        }
        return maisVendido;
    }


    public static Produtos produtoMenosVendido(List<Movimentacao> movimentacao){
        Map <Produtos, Integer> totalSaidas = new HashMap<>();

        for (Movimentacao mov : movimentacao) {
            if(mov.getTipo()==Movimentacao.tipoMovimentacao.SAIDA){
                Produtos produto = mov.getProduto();
                int quantidade = mov.getQuantidade();
                totalSaidas.put(produto, totalSaidas.getOrDefault(produto, 0)+quantidade);
            }
        }


        Produtos menosVendido = null;
        int menorQuantidade = Integer.MAX_VALUE;

        for (Map.Entry<Produtos, Integer> entry: totalSaidas.entrySet()){
            if (entry.getValue()<menorQuantidade && entry.getValue()>0) {
                menorQuantidade = entry.getValue();
                menosVendido = entry.getKey();
            }
        }
        return menosVendido;
    }


    public static int totalDeMovimentacoes(List<Movimentacao> historicodeMov){
        int totaldeEntradas = 0;
        int totaldeSaidas = 0;
        for (Movimentacao mov : historicodeMov) {
            if (mov.isEntrada()){totaldeEntradas++;}
            else if (mov.isSaida()) {totaldeSaidas++;}
        }
        System.out.println("Total de entradas: "+ totaldeEntradas);
        System.out.println("Total de saidas: "+ totaldeSaidas);
        System.out.println("Total de entradas e saidas"+ (totaldeEntradas+totaldeSaidas));
        return totaldeEntradas+totaldeSaidas;

    }
    

    public double valorTotalEstoque(Estoque estoque){
        List <Produtos> listadeProdutos = estoque.getListadeprodutos();
        double valorTotal = 0;
        for (Produtos produto : listadeProdutos) {
            valorTotal += produto.getPreco()*produto.getQuantidade();
        }
        System.out.println("Valor total R$: "+valorTotal);
        return valorTotal;
    }


}
