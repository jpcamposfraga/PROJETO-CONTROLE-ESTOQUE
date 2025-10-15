
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Estoque meuEstoque = new Estoque();
        Produtos videogame = new Produtos(0, LocalDate.of(2030, 01, 01),
        LocalDateTime.now(), " ", "Video-Game PS5", "Eletronicos", "Sony", "Videogame de última geração", 4999.99);

        Movimentacao mov1 = new Movimentacao(10, videogame,Movimentacao.tipoMovimentacao.ENTRADA, LocalDateTime.now());

        Produtos televisao = new Produtos(0, LocalDate.of(2031, 02, 02), LocalDateTime.now(),
        " ", "Televisão 72 polegadas", "Eletronicos", "Philco", "televisao OLED 72 polegadas", 2999.99);

        Movimentacao mov2 = new Movimentacao(5, televisao, Movimentacao.tipoMovimentacao.ENTRADA, LocalDateTime.now());

        meuEstoque.adicionarProduto(televisao);
        meuEstoque.adicionarProduto(videogame);
        meuEstoque.registrarMovimentacao(mov1);
        meuEstoque.registrarMovimentacao(mov2);
        Relatorio relatorioGerador = new Relatorio();
        relatorioGerador.valorTotalEstoque(meuEstoque);
        relatorioGerador.gerarRelatorioProdutos(meuEstoque);


    }
}
