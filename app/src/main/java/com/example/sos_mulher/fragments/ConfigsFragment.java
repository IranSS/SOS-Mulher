package com.example.sos_mulher.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.sos_mulher.R;
import com.example.sos_mulher.telas.AppActivity;
import com.example.sos_mulher.telas.ChangePasswordActivity;
import com.example.sos_mulher.telas.LoginActivity;
import com.google.android.material.snackbar.Snackbar;

public class ConfigsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_configs, container, false);

        Button logout = view.findViewById(R.id.button_logout);
        Switch switch1 = view.findViewById(R.id.active_1);
        Switch switch2 = view.findViewById(R.id.active_2);

        LinearLayout change_password = view.findViewById(R.id.change_password);
        LinearLayout contatos = view.findViewById(R.id.contacts);
        LinearLayout local = view.findViewById(R.id.localReal);
        LinearLayout suporte = view.findViewById(R.id.suporte);

        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences prefs = requireActivity()
                        .getSharedPreferences("user_session", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = prefs.edit();
                editor.clear(); // limpa sessão
                editor.apply();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

                requireActivity().finish();
            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(in);
            }
        });
        contatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Funcionalidade em desenvolvimento 🚧", Toast.LENGTH_SHORT).show();
            }
        });
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Funcionalidade em desenvolvimento 🚧", Toast.LENGTH_SHORT).show();
            }
        });
        suporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Funcionalidade em desenvolvimento 🚧", Toast.LENGTH_SHORT).show();
            }
        });
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(getContext(), "Funcionalidade em desenvolvimento 🚧", Toast.LENGTH_SHORT).show();
        });
        switch2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(getContext(), "Funcionalidade em desenvolvimento 🚧", Toast.LENGTH_SHORT).show();
        });

        // Inflate the layout for this fragment
        return view;
    }
}