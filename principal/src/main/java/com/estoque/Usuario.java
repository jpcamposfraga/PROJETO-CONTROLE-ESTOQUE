package com.estoque;
public class Usuario {
    private int id;
    private String nome;
    private String usuario;
    private String tipo;
    
    public Usuario(int id, String nome, String usuario, String tipo) {
        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
        this.tipo = tipo;
    }
    
    public int getId() {
        return id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getUsuario() {
        return usuario;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public boolean isAdmin() {
        return "ADMIN".equals(tipo);
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + id +
            ", nome='" + nome + '\'' +
            ", usuario='" + usuario + '\'' +
            ", tipo='" + tipo + '\'' +
            '}';
    }
}