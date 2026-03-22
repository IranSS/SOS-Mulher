package com.example.sos_mulher.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sos_mulher.R;
import com.example.sos_mulher.models.Alerta;

import java.util.List;

public class AlertaAdapter extends RecyclerView.Adapter<AlertaAdapter.ViewHolder> {

    private List<Alerta> lista;

    public AlertaAdapter(List<Alerta> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alerta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alerta alerta = lista.get(position);

        holder.mensagem.setText(alerta.getMensagem());
        holder.data.setText(alerta.getData());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mensagem, data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mensagem = itemView.findViewById(R.id.text_mensagem);
            data = itemView.findViewById(R.id.text_data);
        }
    }
}
