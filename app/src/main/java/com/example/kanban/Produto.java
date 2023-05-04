package com.example.kanban;

public class Produto {
  String nome, categoria;
  float preco;
  int id;

  public Produto(String nome, String categoria, float preco, int id) {
    this.nome = nome;
    this.categoria = categoria;
    this.preco = preco;
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public float getPreco() {
    return preco;
  }

  public void setPreco(float preco) {
    this.preco = preco;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
