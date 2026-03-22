package com.example.sos_mulher.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sos_mulher.Adapter.AlertaAdapter;
import com.example.sos_mulher.R;
import com.example.sos_mulher.dao.UserDAO;
import com.example.sos_mulher.data.AppDataBase;
import com.example.sos_mulher.models.Alerta;
import com.example.sos_mulher.models.User;

import java.util.List;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView userName = view.findViewById(R.id.user_name);
        Bundle bundle = getArguments();

        RecyclerView recyclerAlertas = view.findViewById(R.id.recycler_alertas);
        recyclerAlertas.setLayoutManager(new LinearLayoutManager(getContext()));

        new Thread(() -> {

            AppDataBase db = AppDataBase.getInstance(getContext());
            List<Alerta> lista = db.alertaDAO().getUltimosAlertas();

            requireActivity().runOnUiThread(() -> {
                recyclerAlertas.setAdapter(new AlertaAdapter(lista));
            });

        }).start();

        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String userName2 = prefs.getString("user_name", "Usuário");
        userName.setText(userName2);

        return view;
    }
}