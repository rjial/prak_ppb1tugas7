package com.rizal.cardbiasa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.rizal.cardbiasa.adapter.database.UserAdapter;

public class LoginActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "CARD_BIASA" ;
    public static final String name = "nameKey";
    public static final String pass = "passwordKey";
    private TextInputEditText txtUsername, txtPassword;
    private Button btnLogin, btnRegister, btnExit;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        txtUsername = (TextInputEditText) findViewById(R.id.edtUsername);
        txtPassword = (TextInputEditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                try {
                    if (username.length() > 0 && password.length() > 0) {
                        UserAdapter dbUser = new UserAdapter(LoginActivity.this);
                        dbUser.open();
                        if (dbUser.Login(username, password)) {
                            Toast.makeText(LoginActivity.this, "Successfully Logged In", Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(name, username);
                            editor.putString(pass, password);
                            editor.commit();
                            Intent kela = new Intent(LoginActivity.this, MainActivity.class);
//                            kela.putExtra("USERNAME", username);
                            finish();
                            startActivity(kela);
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid Username/Password", Toast.LENGTH_LONG).show();
                        }
                        dbUser.close();
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                try {
                    UserAdapter dbaUser = new UserAdapter(LoginActivity.this);
                    dbaUser.open();
                    if (dbaUser.Register(username, password)) {
                        Toast.makeText(LoginActivity.this, "Create Data", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Tambah Username/Password", Toast.LENGTH_LONG).show();
                    }
                    dbaUser.close();
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
    }
}