package com.example.sos_mulher.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sos_mulher.Adapter.ContatoAdapter;
import com.example.sos_mulher.R;
import com.example.sos_mulher.dao.ContatoDAO;
import com.example.sos_mulher.data.AppDataBase;
import com.example.sos_mulher.models.Contatos;

import java.util.ArrayList;
import java.util.List;

public class ContatosFragment extends Fragment {

    private AppDataBase db;
    private ContatoDAO contatoDAO;
    private RecyclerView recyclerView;
    private View rootView;
    private ContatoAdapter adapter;
    private List<Contatos> listaContatos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_contatos, container, false);

        // Inicializa banco UMA VEZ
        db = AppDataBase.getInstance(getContext());
        contatoDAO = db.contatoDAO();

        // Views
        Spinner spinner = rootView.findViewById(R.id.selecionar_relacao);
        EditText nome_contato = rootView.findViewById(R.id.definir_nome_contato);
        EditText email_contato = rootView.findViewById(R.id.definir_email_contato);
        Button definir_contato = rootView.findViewById(R.id.definir_contato);
        CheckBox enviar_email = rootView.findViewById(R.id.definir_envio_email);

        recyclerView = rootView.findViewById(R.id.recycler_contatos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.opcoes_relacoes,
                R.drawable.spinner_item
        );
        adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Botão salvar
        definir_contato.setOnClickListener(v -> {

            Contatos contato = new Contatos();
            contato.setEmail(email_contato.getText().toString());
            contato.setName(nome_contato.getText().toString());
            contato.setGrau_parentesco(spinner.getSelectedItem().toString());
            contato.setSendMsg(enviar_email.isChecked());

            new Thread(() -> {
                contatoDAO.insert(contato);

                requireActivity().runOnUiThread(() -> {
                    carregarContatos();
                });
            }).start();

            // limpa campos
            email_contato.setText("");
            nome_contato.setText("");
            enviar_email.setChecked(false);

            Log.d("Adicionado", "Contato adicionado: " + contato.returnAllInfos());
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        carregarContatos();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void carregarContatos() {

        new Thread(() -> {
            List<Contatos> novaLista = contatoDAO.getAll();

            requireActivity().runOnUiThread(() -> {

                if (adapter == null) {
                    listaContatos = new ArrayList<>(novaLista);
                    adapter = new ContatoAdapter(listaContatos);
                    recyclerView.setAdapter(adapter);
                } else {
                    listaContatos.clear();
                    listaContatos.addAll(novaLista);
                    adapter.notifyDataSetChanged();
                }
            });
        }).start();
    }
    ItemTouchHelper.SimpleCallback callback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView,
                                      @NonNull RecyclerView.ViewHolder viewHolder,
                                      @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                    int position = viewHolder.getAdapterPosition();
                    Contatos contato = listaContatos.get(position);

                    // REMOVE DO BANCO
                    new Thread(() -> {
                        contatoDAO.delete(contato);
                    }).start();

                    // REMOVE DA LISTA
                    listaContatos.remove(position);
                    adapter.notifyItemRemoved(position);
                }
            };
}