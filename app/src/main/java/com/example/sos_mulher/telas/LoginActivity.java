package com.example.sos_mulher.telas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.sos_mulher.R;
import com.example.sos_mulher.dao.UserDAO;
import com.example.sos_mulher.data.AppDataBase;
import com.example.sos_mulher.models.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        AppDataBase db = Room.databaseBuilder(getApplicationContext(),
                AppDataBase.class, "user").build();
        UserDAO dao = db.userDao();

        Button cadastro = findViewById(R.id.Button_cadastro);
        Button entrar = findViewById(R.id.Button_entrar);

        EditText campoEmail = findViewById(R.id.campo_email);
        EditText campoSenha = findViewById(R.id.campo_senha);

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);

        int userId = prefs.getInt("user_id", -1);

        if(userId != -1){
            Intent in = new Intent(LoginActivity.this, AppActivity.class);
            startActivity(in);
            finish();
        }

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    String email = campoEmail.getText().toString();
                    String senha = campoSenha.getText().toString();
                    User user = dao.findByEmail(email);

                    if(user.equals(null)){
                        return;
                    }
                    else{
                        if(user.getPassword().equals(senha)){

                            SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();

                            editor.putInt("user_id", user.getUid());
                            editor.putString("user_name", user.getName());
                            editor.apply();

                            Intent in = new Intent(LoginActivity.this, AppActivity.class);
                            startActivity(in);
                            finish();
                        }else{
                            campoEmail.setText("");
                            campoSenha.setText("");
                        }
                    }

                    Log.d("EMAIL LOGIN", "onClick: " + String.valueOf(user.getName()));
                }).start();
            }
        });

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, CadasterActivity.class);
                startActivity(in);
            }
        });
    }
}