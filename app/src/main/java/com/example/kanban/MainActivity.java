package com.example.kanban;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  ArrayList<Produto> listaProdutos = new ArrayList<>();
  RecyclerView recycler;
  EditText nomeProduto, categoriaProduto, precoProduto, search;
  Button cadastrar;
  int productId;

  Adaptador adapter;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportActionBar().hide();
    nomeProduto = findViewById(R.id.nomeProduto);
    categoriaProduto = findViewById(R.id.categoriaProduto);
    precoProduto = findViewById(R.id.precoProduto);
    recycler = findViewById(R.id.rv);
    cadastrar = findViewById(R.id.cadastrar);
    recycler.setHasFixedSize(true);
    recycler.setLayoutManager(new LinearLayoutManager(this));
    adapter = new Adaptador(this, listaProdutos, new Adaptador.OnItemClickListener() {
      @Override
      public void onItemClick(Produto p) {
        Toast.makeText(MainActivity.this, p.getNome(), Toast.LENGTH_SHORT).show();
      }
    });
    recycler.setAdapter(adapter);
    cadastrar.setOnClickListener(click -> {
      cadastrarProduto();
      search.setText(" ");
      search.setText("");
    });
    search = findViewById(R.id.search);
    search.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String textoFiltro = charSequence.toString().toLowerCase();
        adapter.filtrar(textoFiltro);
      }

      @Override
      public void afterTextChanged(Editable editable) {
      }
    });
  }

  public boolean cadastrarProduto() {
    if (nomeProduto.getText().toString().equals("") || categoriaProduto.getText().toString().equals("") || precoProduto.getText().toString().equals("")) {
      Toast.makeText(this, "Insira todos os campos para criar o produto!", Toast.LENGTH_SHORT).show();
      return false;
    }
    productId++;
    Produto p1 = new Produto(nomeProduto.getText().toString(), categoriaProduto.getText().toString(), Float.parseFloat(precoProduto.getText().toString()), productId);
    listaProdutos.add(p1);
    nomeProduto.setText("");
    categoriaProduto.setText("");
    precoProduto.setText("");
    return true;
  }

}