package com.example.sos_mulher.telas;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.sos_mulher.R;
import com.example.sos_mulher.dao.UserDAO;
import com.example.sos_mulher.data.AppDataBase;
import com.example.sos_mulher.models.User;

import java.util.List;

public class CadasterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadaster);

        // usar database e seus metodos
        AppDataBase db = AppDataBase.getInstance(this);
        UserDAO userDAO = db.userDao();

        Button cadastrar = findViewById(R.id.Button_cadastro);
        ImageButton backButton = findViewById(R.id.Button_back);

        //campos
        EditText name = findViewById(R.id.inputText_nome);
        EditText cpf = findViewById(R.id.inputText_cpf);
        EditText phone = findViewById(R.id.inputText_telefone);
        EditText email = findViewById(R.id.inputText_email);
        EditText pass = findViewById(R.id.inputText_senha);
        EditText pass2 = findViewById(R.id.inputText_confirme_senha);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User u = new User();
                if(pass.getText().toString().equals(pass2.getText().toString())){
                    u.setPassword(pass.getText().toString());
                }
                else{
                    Log.d("CADASTRO", "Falha ao cadastrar usuário, senhas não são compativeis.");
                    return;
                }
                u.setName(name.getText().toString());
                u.setCpf(cpf.getText().toString());
                u.setPhone(phone.getText().toString());
                u.setEmail(email.getText().toString());

                new Thread(() -> {
                    List<User> users = userDAO.getAll();
                    Log.d("DATABASE", "Usuarios: " + users.size());
                }).start();

                new Thread(() -> {
                    userDAO.insert(u);
                    Log.d("CADASTRO", "Usuário cadastrado com sucesso!");
                }).start();
                finish();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}