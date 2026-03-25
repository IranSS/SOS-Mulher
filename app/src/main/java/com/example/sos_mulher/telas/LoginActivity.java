package com.example.sos_mulher.telas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sos_mulher.R;
import com.example.sos_mulher.dao.UserDAO;
import com.example.sos_mulher.data.AppDataBase;
import com.example.sos_mulher.models.User;

public class LoginActivity extends AppCompatActivity {

    private static final String PREF_NAME = "user_session";
    private static final String KEY_ID = "user_id";
    private static final String KEY_NAME = "user_name";

    private EditText campoEmail, campoSenha;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        userDAO = AppDataBase.getInstance(this).userDao();

        campoEmail = findViewById(R.id.campo_email);
        campoSenha = findViewById(R.id.campo_senha);

        Button entrar = findViewById(R.id.Button_entrar);
        Button cadastro = findViewById(R.id.Button_cadastro);

        verificarSessao();

        entrar.setOnClickListener(v -> fazerLogin());
        cadastro.setOnClickListener(v ->
                startActivity(new Intent(this, CadasterActivity.class))
        );
    }

    private void verificarSessao() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int userId = prefs.getInt(KEY_ID, -1);

        if (userId != -1) {
            startActivity(new Intent(this, AppActivity.class));
            finish();
        }
    }

    private void fazerLogin() {

        String email = campoEmail.getText().toString().trim();
        String senha = campoSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {

            User user = userDAO.findByEmail(email);

            runOnUiThread(() -> {

                if (user == null) {
                    Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!user.getPassword().equals(senha)) {
                    Toast.makeText(this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                    campoSenha.setText("");
                    return;
                }

                salvarSessao(user);

                Toast.makeText(this, "Login realizado!", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(this, AppActivity.class));
                finish();
            });

        }).start();
    }

    private void salvarSessao(User user) {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        prefs.edit()
                .putInt(KEY_ID, user.getUid())
                .putString(KEY_NAME, user.getName())
                .apply();
    }
}