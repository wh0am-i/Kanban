package com.example.kanban;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    cadastrar.setOnClickListener(click -> {
      cadastrarProduto();
      search.setText(" ");
      search.setText("");
    });
    search = findViewById(R.id.search);
  }

  public boolean cadastrarProduto() {
    if (nomeProduto.getText().toString().equals("") || categoriaProduto.getText().toString().equals("") || precoProduto.getText().toString().equals("")) {
      Toast.makeText(this, "Insira todos os campos para criar o produto!", Toast.LENGTH_SHORT).show();
      return false;
    }
    if(listaProdutos.isEmpty()){
      productId++;
    } else {
      int ultimaPosicao = listaProdutos.size()-1;
      System.out.println(ultimaPosicao);
      productId = listaProdutos.get(ultimaPosicao).getId();
      productId++;
    }
    Produto p1 = new Produto(nomeProduto.getText().toString(), categoriaProduto.getText().toString(), Float.parseFloat(precoProduto.getText().toString()), productId);
    p1.salvar();
    nomeProduto.setText("");
    categoriaProduto.setText("");
    precoProduto.setText("");
    return true;
  }

  @Override
  protected void onResume() {
    super.onResume();
    carrega();
  }

  public void carrega() {
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    reference.child("Produtos").addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        listaProdutos.clear();
        for (DataSnapshot ds : snapshot.getChildren()) {
          Produto p = (Produto) ds.getValue(Produto.class);
          listaProdutos.add(p);
        }
        adapter = new Adaptador(MainActivity.this, listaProdutos, new Adaptador.OnItemClickListener() {
          @Override
          public void onItemClick(Produto p) {
            Toast.makeText(MainActivity.this, p.getNome(), Toast.LENGTH_SHORT).show();
          }
        });
        recycler.setAdapter(adapter);
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
        adapter.notifyDataSetChanged();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

}