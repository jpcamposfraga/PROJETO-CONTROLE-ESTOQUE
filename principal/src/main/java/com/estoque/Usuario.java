package com.estoque;
public class Usuario {
    private int id;
    private String nome;
    private String usuario;
    private String tipo;
    private String senha;
    
    
    public Usuario(String nome, String usuario, String tipo,String senha) {
        
        this.nome = nome;
        this.usuario = usuario;
        this.tipo = tipo;
        this.senha = senha;
        
    }

    public Usuario(String nome, String usuario, String tipo) {
        
        this.nome = nome;
        this.usuario = usuario;
        this.tipo = tipo;
        
        
    }

    public Usuario(int id,String nome, String usuario, String tipo,String senha) {
        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
        this.tipo = tipo;
        this.senha = senha;
        
    }

    public Usuario(int id,String nome, String usuario, String tipo) {
        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
        this.tipo = tipo;
        this.id=id;
        
    }

    public String getSenha(){
        return senha;
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

    public void setSenha(String senha){
        this.senha =senha;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setUsuario(String usuario){
        this.usuario = usuario;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
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