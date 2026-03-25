package com.example.sos_mulher.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sos_mulher.R;
import com.example.sos_mulher.dao.ContatoDAO;
import com.example.sos_mulher.data.AppDataBase;
import com.example.sos_mulher.models.Alerta;
import com.example.sos_mulher.models.Contatos;
import com.example.sos_mulher.service.SosService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlertFragment extends Fragment {

    private static final String TAG = "SOS_DEBUG";
    private static final int REQUEST_LOCATION_PERMISSION = 1001;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_alert, container, false);
        Button btnSos = view.findViewById(R.id.btn_sos);
        ImageButton Button_back_alert = view.findViewById(R.id.Button_back_alert);

        Button_back_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment novoFragment = new HomeFragment();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, novoFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        Context context = getContext();

        if (context != null) {
            fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(context);
        }

        if (btnSos != null) {
            btnSos.setOnClickListener(v -> verificarPermissao());
        }

        return view;
    }

    /* ===================== PERMISSÃO ===================== */

    private void verificarPermissao() {
        Context context = getContext();
        if (context == null) return;

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION
            );
        } else {
            obterLocalizacaoEEnviar();
        }
    }

    /* ===================== LOCALIZAÇÃO ===================== */

    private void obterLocalizacaoEEnviar() {
        Context context = getContext();
        if (context == null || fusedLocationClient == null) return;

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient
                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(location -> {
                    if (!isAdded() || location == null) return;

                    double lat = location.getLatitude();
                    double lng = location.getLongitude();

                    Log.d(TAG, "Localização: " + lat + ", " + lng);
                    buscarContatosEEnviar(lat, lng);
                })
                .addOnFailureListener(e ->
                        Log.e(TAG, "Erro ao obter localização", e)
                );
    }

    /* ===================== BUSCAR CONTATOS ===================== */

    private void buscarContatosEEnviar(double lat, double lng) {

        new Thread(() -> {
            Context context = getContext();
            if (context == null) return;

            AppDataBase db = AppDataBase.getInstance(context);
            ContatoDAO contatoDAO = db.contatoDAO();

            List<Contatos> contatos = contatoDAO.getContatosParaEnviar();
            ArrayList<String> emails = new ArrayList<>();

            for (Contatos c : contatos) {
                if (c.getSendMsg()) {
                    emails.add(c.getEmail());
                }
            }

            String[] destinatarios = emails.toArray(new String[0]);

            Activity activity = getActivity();
            if (activity == null) return;

            activity.runOnUiThread(() -> {
                if (!isAdded()) return;

                if (destinatarios.length > 0) {
                    enviarMensagemSeguro(destinatarios, lat, lng);
                    salvarAlerta();
                } else {
                    Toast.makeText(
                            context,
                            "Nenhum contato selecionado",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        }).start();
    }

    /* ===================== MENSAGEM / CLIPBOARD ===================== */

    private void enviarMensagemSeguro(String[] destinatarios, double lat, double lng) {

        Context context = getContext();
        if (context == null || destinatarios.length == 0) return;

        String mensagem =
                "🚨 SOS! Estou em perigo!\n\n" +
                        "📍 Minha localização:\n" +
                        "https://www.google.com/maps?q=" + lat + "," + lng;

        // INTENT EXCLUSIVO PARA EMAIL
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(android.net.Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, destinatarios);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "🚨 ALERTA DE EMERGÊNCIA");
        emailIntent.putExtra(Intent.EXTRA_TEXT, mensagem);

        try {
            startActivity(emailIntent);
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(context, "Nenhum app de e-mail instalado", Toast.LENGTH_LONG).show();
        }
    }

    /* ===================== SALVAR ALERTA ===================== */

    private void salvarAlerta() {
        new Thread(() -> {
            Context context = getContext();
            // Inicia o foreground service
//            ContextCompat.startForegroundService(
//                    context,
//                    new Intent(context, SosService.class)
//            );
            if (context == null) return;

            AppDataBase db = AppDataBase.getInstance(context);

            Alerta alerta = new Alerta();
            alerta.setMensagem("Alerta enviado com sucesso");
            alerta.setData(
                    new SimpleDateFormat("dd/MM/yyyy HH:mm")
                            .format(new Date())
            );

            db.alertaDAO().insert(alerta);
            Log.d(TAG, "Alerta salvo");
        }).start();
    }

    /* ===================== CALLBACK PERMISSÃO ===================== */

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                obterLocalizacaoEEnviar();
            } else {
                Context context = getContext();
                if (context != null) {
                    Toast.makeText(
                            context,
                            "Permissão de localização negada",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        }
    }
}