package com.example.sos_mulher.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sos_mulher.R;
import com.example.sos_mulher.dao.ContatoDAO;
import com.example.sos_mulher.data.AppDataBase;
import com.example.sos_mulher.models.Contatos;

import java.util.List;

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ViewHolder> {

    private List<Contatos> lista;

    public ContatoAdapter(List<Contatos> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contato_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contatos contato = lista.get(position);

        holder.nome.setText(contato.getName());
        holder.grau_parentesco.setText(contato.getGrau_parentesco());

        // evita bug de reciclagem
        holder.check.setOnCheckedChangeListener(null);

        holder.check.setChecked(contato.getSendMsg());

        holder.check.setOnCheckedChangeListener((buttonView, isChecked) -> {

            contato.setSendMsg(isChecked);

            new Thread(() -> {
                AppDataBase db = AppDataBase.getInstance(buttonView.getContext());
                ContatoDAO dao = db.contatoDAO();

                dao.update(contato); // salva a alteração no banco
            }).start();
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nome, grau_parentesco;
        CheckBox check;

        public ViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.text_nome);
            grau_parentesco = itemView.findViewById(R.id.text_parentesco);
            check = itemView.findViewById(R.id.check_enviar);
        }
    }
}
