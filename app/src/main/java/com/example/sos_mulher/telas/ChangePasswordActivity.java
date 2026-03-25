package com.example.sos_mulher.telas;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sos_mulher.R;
import com.example.sos_mulher.dao.UserDAO;
import com.example.sos_mulher.data.AppDataBase;
import com.example.sos_mulher.models.User;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String PREF_NAME = "user_session";
    private static final String KEY_ID = "user_id";

    private EditText senhaAtual, novaSenha, confirmarSenha;
    private Button btnSalvar;

    private UserDAO userDAO;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        senhaAtual = findViewById(R.id.senha_atual);
        novaSenha = findViewById(R.id.nova_senha);
        confirmarSenha = findViewById(R.id.confirmar_senha);
        btnSalvar = findViewById(R.id.btn_salvar_senha);

        userDAO = AppDataBase.getInstance(this).userDao();

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        userId = prefs.getInt(KEY_ID, -1);

        if (userId == -1) {
            Toast.makeText(this, "Sessão inválida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnSalvar.setOnClickListener(v -> validarSenha());
    }

    private void validarSenha() {

        String senhaAtualDigitada = senhaAtual.getText().toString();

        new Thread(() -> {

            User user = userDAO.getUserById(userId);

            runOnUiThread(() -> {

                if (user == null) {
                    Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!user.getPassword().equals(senhaAtualDigitada)) {
                    Toast.makeText(this, "Senha atual incorreta", Toast.LENGTH_SHORT).show();
                } else {
                    novaSenha.setEnabled(true);
                    confirmarSenha.setEnabled(true);
                    Toast.makeText(this, "Agora pode alterar", Toast.LENGTH_SHORT).show();

                    btnSalvar.setOnClickListener(v -> alterarSenha());
                }
            });

        }).start();
    }

    private void alterarSenha() {

        String nova = novaSenha.getText().toString();
        String confirmar = confirmarSenha.getText().toString();

        if (!nova.equals(confirmar)) {
            Toast.makeText(this, "Senhas não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            userDAO.updateSenhaById(userId, nova);

            runOnUiThread(() -> {
                Toast.makeText(this, "Senha atualizada!", Toast.LENGTH_SHORT).show();
                finish();
            });

        }).start();
    }
}