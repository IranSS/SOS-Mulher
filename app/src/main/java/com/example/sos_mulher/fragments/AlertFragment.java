package com.example.sos_mulher.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sos_mulher.R;
import com.example.sos_mulher.dao.ContatoDAO;
import com.example.sos_mulher.data.AppDataBase;
import com.example.sos_mulher.models.Alerta;
import com.example.sos_mulher.models.Contatos;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class AlertFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert, container, false);

        Button enviar = view.findViewById(R.id.btn_sos);


        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        enviar.setOnClickListener(v -> {

            FusedLocationProviderClient fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(requireContext());

            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getContext(), "Permissão de localização necessária", Toast.LENGTH_SHORT).show();
                return;
            }

            fusedLocationClient.getCurrentLocation(
                            com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                            null
                    )
                    .addOnSuccessListener(location -> {

                        if (location != null) {

                            double lat = location.getLatitude();
                            double lng = location.getLongitude();

                            buscarContatosEEnviar(lat, lng);

                        } else {
                            Toast.makeText(getContext(), "Não foi possível obter localização", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        return view;
    }
    private void enviarEmail(String[] destinatarios, double lat, double lng) {

        //evitar crash
        if (!isAdded()) return;

        String assunto = "🚨 SOS - Preciso de ajuda!";

        String mensagem = "🚨 SOS! Estou em perigo!\n\n" +
                "Preciso de ajuda urgente.\n\n" +
                "📍 Minha localização:\n" +
                "https://www.google.com/maps?q=" + lat + "," + lng;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_EMAIL, destinatarios);
        intent.putExtra(Intent.EXTRA_SUBJECT, assunto);
        intent.putExtra(Intent.EXTRA_TEXT, mensagem);

        startActivity(Intent.createChooser(intent, "Enviar SOS..."));
    }
    private void buscarContatosEEnviar(double lat, double lng) {

        new Thread(() -> {

            AppDataBase db = AppDataBase.getInstance(getContext());
            ContatoDAO contatoDAO = db.contatoDAO();

            List<Contatos> lista = contatoDAO.getContatosParaEnviar();

            ArrayList<String> emails = new ArrayList<>();

            for (Contatos c : lista) {
                emails.add(c.getEmail());
            }

            String[] destinatarios = emails.toArray(new String[0]);

            requireActivity().runOnUiThread(() -> {

                if (!isAdded()) return;

                if (destinatarios.length > 0) {
                    enviarEmail(destinatarios, lat, lng);
                    salvarAlerta();
                } else {
                    Toast.makeText(getContext(), "Nenhum contato selecionado", Toast.LENGTH_SHORT).show();
                }
            });

        }).start();
    }
    private void salvarAlerta() {

        new Thread(() -> {

            AppDataBase db = AppDataBase.getInstance(getContext());

            Alerta alerta = new Alerta();

            alerta.setMensagem("Alerta enviado com sucesso");

            String data = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                    .format(new java.util.Date());

            alerta.setData(data);

            db.alertaDAO().insert(alerta);

        }).start();
    }
}