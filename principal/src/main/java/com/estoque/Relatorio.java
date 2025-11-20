package com.estoque;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe de relatórios simplificada - trabalha com EstoqueService
 * Gera relatórios no console e em arquivos (TXT, CSV, HTML)
 */
public class Relatorio {
    private final EstoqueService service;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final String RELATORIOS_DIR = "relatorios/";
    
    public enum FormatoRelatorio {
        TXT, CSV, HTML
    }
    
    public Relatorio(EstoqueService service) {
        this.service = service;
        criarDiretorioRelatorios();
    }
    
    private void criarDiretorioRelatorios() {
        File dir = new File(RELATORIOS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    // ========== MÉTODOS PARA CONSOLE ==========
    
    public String gerarRelatorioProdutos() {
        List<Produtos> produtos = service.listarTodosProdutos();
        
        if (produtos.isEmpty()) {
            return "Nenhum produto cadastrado no estoque.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
        sb.append("║              RELATÓRIO DE PRODUTOS EM ESTOQUE                  ║\n");
        sb.append("╚════════════════════════════════════════════════════════════════╝\n\n");
        
        for (Produtos p : produtos) {
            sb.append(String.format("ID: %d | %s\n", p.getId(), p.getNome()));
            sb.append(String.format("  Categoria: %s | Fornecedor: %s\n", p.getCategoria(), p.getFornecedor()));
            sb.append(String.format("  Quantidade: %d | Preço Unit.: R$ %.2f | Total: R$ %.2f\n", 
                p.getQuantidade(), p.getPreco(), p.getValorTotal()));
            sb.append(String.format("  Validade: %s\n", p.getDataValidade()));
            sb.append("  " + "-".repeat(60) + "\n");
        }
        
        sb.append(String.format("\nTotal de produtos: %d\n", produtos.size()));
        sb.append(String.format("Valor total do estoque: R$ %.2f\n", service.calcularValorTotalEstoque()));
        
        return sb.toString();
    }
    
    public String gerarRelatorioMovimentacoes() {
        List<Movimentacao> movimentacoes = service.obterHistorico();
        
        if (movimentacoes.isEmpty()) {
            return "Nenhuma movimentação registrada.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
        sb.append("║              RELATÓRIO DE MOVIMENTAÇÕES                        ║\n");
        sb.append("╚════════════════════════════════════════════════════════════════╝\n\n");
        
        int totalEntradas = 0;
        int totalSaidas = 0;
        
        for (Movimentacao m : movimentacoes) {
            sb.append(String.format("[%s] %s\n", 
                m.getDatamovimentacao().format(DATE_FORMAT),
                m.getTipo()));
            sb.append(String.format("  Produto: %s (ID: %d)\n", 
                m.getProduto().getNome(), m.getProduto().getId()));
            sb.append(String.format("  Quantidade: %d\n", m.getQuantidade()));
            sb.append("  " + "-".repeat(60) + "\n");
            
            if (m.isEntrada()) totalEntradas++;
            else totalSaidas++;
        }
        
        sb.append(String.format("\nTotal de movimentações: %d\n", movimentacoes.size()));
        sb.append(String.format("  • Entradas: %d\n", totalEntradas));
        sb.append(String.format("  • Saídas: %d\n", totalSaidas));
        
        return sb.toString();
    }
    
    public String gerarRelatorioResumo() {
        List<Produtos> produtos = service.listarTodosProdutos();
        List<Movimentacao> movimentacoes = service.obterHistorico();
        
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                    RESUMO DO ESTOQUE                           ║\n");
        sb.append("╚════════════════════════════════════════════════════════════════╝\n\n");
        
        sb.append(String.format("📦 Total de produtos cadastrados: %d\n", produtos.size()));
        sb.append(String.format("💰 Valor total em estoque: R$ %.2f\n", service.calcularValorTotalEstoque()));
        sb.append(String.format("📊 Total de movimentações: %d\n\n", movimentacoes.size()));
        
        Produtos maisVendido = produtoMaisVendido();
        if (maisVendido != null) {
            sb.append(String.format("🏆 Produto mais vendido: %s\n", maisVendido.getNome()));
        }
        
        Produtos menosVendido = produtoMenosVendido();
        if (menosVendido != null) {
            sb.append(String.format("📉 Produto menos vendido: %s\n", menosVendido.getNome()));
        }
        
        return sb.toString();
    }
    
    public String relatorioProdutosEstoqueBaixo(int quantidadeMinima) {
        List<Produtos> produtosBaixos = service.produtosComEstoqueBaixo(quantidadeMinima);
        
        if (produtosBaixos.isEmpty()) {
            return String.format("✓ Nenhum produto com estoque abaixo de %d unidades.\n", quantidadeMinima);
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("\n⚠️  ALERTA: PRODUTOS COM ESTOQUE BAIXO\n");
        sb.append("═".repeat(60) + "\n\n");
        
        for (Produtos p : produtosBaixos) {
            sb.append(String.format("• %s (ID: %d)\n", p.getNome(), p.getId()));
            sb.append(String.format("  Quantidade atual: %d | Mínimo: %d\n", 
                p.getQuantidade(), quantidadeMinima));
            sb.append(String.format("  Categoria: %s\n\n", p.getCategoria()));
        }
        
        return sb.toString();
    }
    
    // ========== ANÁLISES ==========
    
    public Produtos produtoMaisVendido() {
        Map<Produtos, Integer> totalSaidas = new HashMap<>();
        
        for (Movimentacao mov : service.obterHistorico()) {
            if (mov.isSaida()) {
                Produtos produto = mov.getProduto();
                totalSaidas.put(produto, totalSaidas.getOrDefault(produto, 0) + mov.getQuantidade());
            }
        }
        
        if (totalSaidas.isEmpty()) return null;
        
        Produtos maisVendido = null;
        int maiorQuantidade = 0;
        
        for (Map.Entry<Produtos, Integer> entry : totalSaidas.entrySet()) {
            if (entry.getValue() > maiorQuantidade) {
                maiorQuantidade = entry.getValue();
                maisVendido = entry.getKey();
            }
        }
        
        return maisVendido;
    }
    
    public Produtos produtoMenosVendido() {
        Map<Produtos, Integer> totalSaidas = new HashMap<>();
        
        for (Movimentacao mov : service.obterHistorico()) {
            if (mov.isSaida()) {
                Produtos produto = mov.getProduto();
                totalSaidas.put(produto, totalSaidas.getOrDefault(produto, 0) + mov.getQuantidade());
            }
        }
        
        if (totalSaidas.isEmpty()) return null;
        
        Produtos menosVendido = null;
        int menorQuantidade = Integer.MAX_VALUE;
        
        for (Map.Entry<Produtos, Integer> entry : totalSaidas.entrySet()) {
            if (entry.getValue() < menorQuantidade && entry.getValue() > 0) {
                menorQuantidade = entry.getValue();
                menosVendido = entry.getKey();
            }
        }
        
        return menosVendido;
    }
    
    // ========== GERAÇÃO DE ARQUIVOS ==========
    
    public String salvarRelatorioCompleto(FormatoRelatorio formato) throws IOException {
        String timestamp = LocalDateTime.now().format(FILE_DATE_FORMAT);
        String nomeArquivo = RELATORIOS_DIR + "relatorio_completo_" + timestamp;
        
        switch (formato) {
            case TXT:
                return salvarRelatorioTXT(nomeArquivo + ".txt");
            case CSV:
                return salvarRelatorioCSV(nomeArquivo + ".csv");
            case HTML:
                return salvarRelatorioHTML(nomeArquivo + ".html");
            default:
                throw new IllegalArgumentException("Formato não suportado");
        }
    }
    
    private String salvarRelatorioTXT(String caminhoArquivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(caminhoArquivo), StandardCharsets.UTF_8))) {
            
            writer.write("╔════════════════════════════════════════════════════════════════╗\n");
            writer.write("║              RELATÓRIO COMPLETO DO ESTOQUE                     ║\n");
            writer.write("║              Gerado em: " + LocalDateTime.now().format(DATE_FORMAT) + "                      ║\n");
            writer.write("╚════════════════════════════════════════════════════════════════╝\n\n");
            
            writer.write(gerarRelatorioResumo());
            writer.write("\n\n");
            writer.write(gerarRelatorioProdutos());
            writer.write("\n\n");
            writer.write(gerarRelatorioMovimentacoes());
            writer.write("\n\n");
            writer.write(relatorioProdutosEstoqueBaixo(5));
            
            writer.write("\n" + "═".repeat(60) + "\n");
            writer.write("Fim do relatório\n");
        }
        
        return caminhoArquivo;
    }
    
    private String salvarRelatorioCSV(String caminhoArquivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(caminhoArquivo), StandardCharsets.UTF_8))) {
            
            writer.write("ID;Nome;Categoria;Fornecedor;Quantidade;Preco_Unitario;Valor_Total;Data_Validade;Data_Cadastro\n");
            
            for (Produtos p : service.listarTodosProdutos()) {
                writer.write(String.format("%d;%s;%s;%s;%d;%.2f;%.2f;%s;%s\n",
                    p.getId(),
                    escaparCSV(p.getNome()),
                    escaparCSV(p.getCategoria()),
                    escaparCSV(p.getFornecedor()),
                    p.getQuantidade(),
                    p.getPreco(),
                    p.getValorTotal(),
                    p.getDataValidade(),
                    p.getDataCadastro().format(DATE_FORMAT)
                ));
            }
            
            writer.write("\nRESUMO\n");
            writer.write("Total de Produtos;" + service.listarTodosProdutos().size() + "\n");
            writer.write("Valor Total Estoque;" + String.format("%.2f", service.calcularValorTotalEstoque()) + "\n");
        }
        
        return caminhoArquivo;
    }
    
    public String salvarRelatorioHTML(String caminhoArquivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(caminhoArquivo), StandardCharsets.UTF_8))) {
            
            writer.write("<!DOCTYPE html>\n<html lang='pt-BR'>\n<head>\n");
            writer.write("  <meta charset='UTF-8'>\n");
            writer.write("  <title>Relatório de Estoque</title>\n");
            writer.write("  <style>\n");
            writer.write("    body { font-family: Arial; margin: 20px; background: #f5f5f5; }\n");
            writer.write("    .container { max-width: 1200px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n");
            writer.write("    h1 { color: #2c3e50; border-bottom: 3px solid #3498db; padding-bottom: 10px; }\n");
            writer.write("    h2 { color: #34495e; margin-top: 30px; }\n");
            writer.write("    table { width: 100%; border-collapse: collapse; margin: 20px 0; }\n");
            writer.write("    th { background: #3498db; color: white; padding: 12px; text-align: left; }\n");
            writer.write("    td { padding: 10px; border-bottom: 1px solid #ddd; }\n");
            writer.write("    tr:hover { background: #f8f9fa; }\n");
            writer.write("    .resumo { background: #e8f4f8; padding: 20px; border-radius: 8px; margin: 20px 0; }\n");
            writer.write("    .resumo p { margin: 10px 0; font-size: 16px; }\n");
            writer.write("    .entrada { color: #28a745; font-weight: bold; }\n");
            writer.write("    .saida { color: #dc3545; font-weight: bold; }\n");
            writer.write("  </style>\n");
            writer.write("</head>\n<body>\n<div class='container'>\n");
            
            writer.write("  <h1>📊 Relatório Completo de Estoque</h1>\n");
            writer.write("  <p>Gerado em: " + LocalDateTime.now().format(DATE_FORMAT) + "</p>\n");
            
            // Resumo
            writer.write("  <div class='resumo'>\n");
            writer.write("    <h2>Resumo</h2>\n");
            writer.write("    <p>📦 Total de produtos: " + service.listarTodosProdutos().size() + "</p>\n");
            writer.write("    <p>💰 Valor total em estoque: R$ " + String.format("%.2f", service.calcularValorTotalEstoque()) + "</p>\n");
            writer.write("    <p>📊 Total de movimentações: " + service.obterHistorico().size() + "</p>\n");
            writer.write("    <p>📉 Produtos com estoque baixo: " + service.produtosComEstoqueBaixo(10).size() + "<p>\n");
            writer.write("    <p>⏳ Produtos com Validade Próxima:" + service.produtosComValidadeProx().size() + "</p>\n");
            
            Produtos maisVendido = produtoMaisVendido();
            if (maisVendido != null) {
                writer.write("    <p>🏆 Produto mais vendido: " + escaparHTML(maisVendido.getNome()) + "</p>\n");
            }
            writer.write("  </div>\n");
            
            // Tabela de produtos
            writer.write("  <h2>Produtos em Estoque</h2>\n");
            writer.write("  <table>\n");
            writer.write("    <tr><th>ID</th><th>Nome</th><th>Categoria</th><th>Fornecedor</th><th>Quantidade</th><th>Preço Unit.</th><th>Valor Total</th></tr>\n");
            
            for (Produtos p : service.listarTodosProdutos()) {
                writer.write("    <tr>\n");
                writer.write("      <td>" + p.getId() + "</td>\n");
                writer.write("      <td>" + escaparHTML(p.getNome()) + "</td>\n");
                writer.write("      <td>" + escaparHTML(p.getCategoria()) + "</td>\n");
                writer.write("      <td>" + escaparHTML(p.getFornecedor()) + "</td>\n");
                writer.write("      <td>" + p.getQuantidade() + "</td>\n");
                writer.write("      <td>R$ " + String.format("%.2f", p.getPreco()) + "</td>\n");
                writer.write("      <td>R$ " + String.format("%.2f", p.getValorTotal()) + "</td>\n");
                writer.write("    </tr>\n");
            }
            writer.write("  </table>\n");
            
            // Movimentações
            if (!service.obterHistorico().isEmpty()) {
                writer.write("  <h2>Histórico de Movimentações</h2>\n");
                writer.write("  <table>\n");
                writer.write("    <tr><th>Data</th><th>Tipo</th><th>Produto</th><th>Quantidade</th></tr>\n");
                
                for (Movimentacao m : service.obterHistorico()) {
                    String classeCSS = m.isEntrada() ? "entrada" : "saida";
                    writer.write("    <tr>\n");
                    writer.write("      <td>" + m.getDatamovimentacao().format(DATE_FORMAT) + "</td>\n");
                    writer.write("      <td class='" + classeCSS + "'>" + m.getTipo() + "</td>\n");
                    writer.write("      <td>" + escaparHTML(m.getProduto().getNome()) + "</td>\n");
                    writer.write("      <td>" + m.getQuantidade() + "</td>\n");
                    writer.write("    </tr>\n");
                }
                writer.write("  </table>\n");

            }
            //estoque baixo
            writer.write("  <h2>Produtos com Estoque Baixo</h2>\n");
            writer.write("  <table>\n");
            writer.write("    <tr><th>ID</th><th>Nome</th><th>Categoria</th><th>Fornecedor</th><th>Quantidade</th></tr>\n");
            for (Produtos p : service.produtosComEstoqueBaixo(10)) {
                writer.write("    <tr>\n");
                writer.write("      <td>" + p.getId() + "</td>\n");
                writer.write("      <td>" + escaparHTML(p.getNome()) + "</td>\n");
                writer.write("      <td>" + escaparHTML(p.getCategoria()) + "</td>\n");
                writer.write("      <td>" + escaparHTML(p.getFornecedor()) + "</td>\n");
                writer.write("      <td>" + p.getQuantidade() + "</td>\n");
                
                writer.write("    </tr>\n");
            }
            writer.write("  </table>\n");

            //data de validade proxima
            writer.write("  <h2>Produtos com Validade Próxima</h2>\n");
            writer.write("  <table>\n");
            writer.write("    <tr><th>ID</th><th>Nome</th><th>Categoria</th><th>Data de Validade</th></tr>\n");
            for (Produtos p : service.produtosComValidadeProx()) {
                writer.write("    <tr>\n");
                writer.write("      <td>" + p.getId() + "</td>\n");
                writer.write("      <td>" + escaparHTML(p.getNome()) + "</td>\n");
                writer.write("      <td>" + escaparHTML(p.getCategoria()) + "</td>\n");
                writer.write("      <td>" + p.getDataValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</td>\n");
                writer.write("    </tr>\n");
            }
            writer.write("  </table>\n");


            
            writer.write("</div>\n</body>\n</html>");
        }
        
        return caminhoArquivo;
    }
    
    private String escaparCSV(String texto) {
        if (texto == null) return "";
        if (texto.contains(";") || texto.contains("\"")) {
            return "\"" + texto.replace("\"", "\"\"") + "\"";
        }
        return texto;
    }
    
    private String escaparHTML(String texto) {
        if (texto == null) return "";
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}