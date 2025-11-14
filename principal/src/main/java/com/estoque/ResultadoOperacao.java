package com.estoque;
public class ResultadoOperacao {
    private final boolean sucesso;
    private final String mensagem;
    private final Object dados;
    
    private ResultadoOperacao(boolean sucesso, String mensagem, Object dados) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.dados = dados;
    }
    
    public static ResultadoOperacao sucesso(String mensagem, Object dados) {
        return new ResultadoOperacao(true, mensagem, dados);
    }
    
    public static ResultadoOperacao sucesso(String mensagem) {
        return new ResultadoOperacao(true, mensagem, null);
    }
    
    public static ResultadoOperacao erro(String mensagem) {
        return new ResultadoOperacao(false, mensagem, null);
    }
    
    public boolean isSucesso() {
        return sucesso;
    }
    
    public String getMensagem() {
        return mensagem;
    }
    
    public Object getDados() {
        return dados;
    }
    
    @Override
    public String toString() {
        return (sucesso ? "✓ " : "✗ ") + mensagem;
    }
}
