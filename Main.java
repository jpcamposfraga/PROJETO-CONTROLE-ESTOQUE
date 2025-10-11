
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Estoque meuEstoque = new Estoque();
        Produtos videogame = new Produtos(10, 0, LocalDate.of(2030, 01, 01),
        LocalDateTime.now(), " ", "Video-Game PS5", "Eletronicos", "Sony", "Videogame de última geração", 4999.99);

        Produtos televisao = new Produtos(5, 1, LocalDate.of(2031, 02, 02), LocalDateTime.now(),
        " ", "Televião 72 polegadas", "Eletronicos", "Philco", "televisao OLED 72 polegadas", 2999.99);
        meuEstoque.adicionarProduto(televisao);
        meuEstoque.adicionarProduto(videogame);
        Relatorio relatorioGerador = new Relatorio();
        relatorioGerador.gerarRelatorioProdutos(meuEstoque);
    }
}
