package com.example.kanban;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MyViewHolder> {
  Context context;
  Adaptador.OnItemClickListener listener;
  List<Produto> listaOriginal;
  List<Produto> listaProdutos;

  public Adaptador(Context context, ArrayList<Produto> listaProdutos, OnItemClickListener listener) {
    this.context = context;
    this.listener = listener;
    this.listaOriginal = listaProdutos;
    this.listaProdutos = new ArrayList<>(listaProdutos);
  }

  public void filtrar(String textoFiltro) {
    List<Produto> produtosFiltrados = listaOriginal.stream()
            .filter(produto -> produto.getNome().toLowerCase().contains(textoFiltro))
            .collect(Collectors.toList());
    if (textoFiltro.isEmpty()) {
      listaProdutos.clear();
      listaProdutos.addAll(listaOriginal);
    } else {
      listaProdutos.clear();
      listaProdutos.addAll(produtosFiltrados);
    }
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public Adaptador.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(context).inflate(R.layout.componente_produto, parent, false);
    return new Adaptador.MyViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull Adaptador.MyViewHolder holder, int position) {
    Produto p = listaProdutos.get(position);
    holder.nome.setText(p.getNome());
    holder.categoria.setText(p.getCategoria());
    holder.preco.setText("R$ " + p.getPreco());
    holder.remove.setOnClickListener(view -> {
      listaProdutos.remove(position);
      listaOriginal.remove(position);
      notifyDataSetChanged();
    });
    holder.edit.setOnClickListener(view -> {
      AlertDialog.Builder alert = new AlertDialog.Builder(context);
      LinearLayout layout = new LinearLayout(context);
      layout.setOrientation(LinearLayout.VERTICAL);
      layout.setGravity(Gravity.CENTER);
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
              LinearLayout.LayoutParams.MATCH_PARENT,
              LinearLayout.LayoutParams.WRAP_CONTENT
      );
      params.setMargins(32, 0, 32, 0); // adicionando margem aos lados

      final EditText editText1 = new EditText(context);
      final EditText editText2 = new EditText(context);
      final EditText editText3 = new EditText(context);

      editText1.setLayoutParams(params);
      editText2.setLayoutParams(params);
      editText3.setLayoutParams(params);

      editText1.setText(p.getNome());
      editText2.setText(p.getCategoria());

      editText3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

      editText3.setText(p.getPreco() + "");

      layout.addView(editText1);
      layout.addView(editText2);
      layout.addView(editText3);

      alert.setTitle("Editar");
      alert.setMessage("Insira as informações do produto");

      alert.setView(layout);

      alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
          if (editText1.getText().toString().equals("") || editText2.getText().toString().equals("") || editText3.getText().toString().equals("")) {
            Toast.makeText(context, "Insira informação em todos os campos", Toast.LENGTH_SHORT).show();
          } else {
            p.setNome(editText1.getText().toString());
            p.setCategoria(editText2.getText().toString());
            p.setPreco(Float.parseFloat(editText3.getText().toString()));
            notifyDataSetChanged();
          }
        }
      });
      alert.show();
    });
  }

  @Override
  public int getItemCount() {
    return listaProdutos.size();
  }

  public interface OnItemClickListener {
    void onItemClick(Produto p);
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView nome, categoria, preco;
    Button edit, remove;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      nome = itemView.findViewById(R.id.componenteNome);
      categoria = itemView.findViewById(R.id.componenteCategoria);
      preco = itemView.findViewById(R.id.componentePreco);
      edit = itemView.findViewById(R.id.componenteEdit);
      remove = itemView.findViewById(R.id.componenteDelete);
    }
  }
}
