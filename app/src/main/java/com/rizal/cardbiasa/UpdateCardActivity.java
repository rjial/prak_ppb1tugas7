package com.rizal.cardbiasa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.rizal.cardbiasa.adapter.database.CardAdapter;
import com.rizal.cardbiasa.model.Card;

public class UpdateCardActivity extends AppCompatActivity {

    public static final String name = "nameKey";
    private Button btnUpdateCard;
    private TextInputEditText txtUpdateIsi, txtUpdateTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_card);
        getSupportActionBar().setTitle("Update Card");
        Intent intent = getIntent();
        int id = intent.getIntExtra("ID_CARD", 0);
        String username = intent.getStringExtra(name);
        CardAdapter cardDB = new CardAdapter(UpdateCardActivity.this);
        cardDB.open();
        Card card = cardDB.getCard(String.valueOf(id));
        btnUpdateCard = (Button) findViewById(R.id.btnUpdateCard);
        txtUpdateIsi = (TextInputEditText) findViewById(R.id.txtUpdateIsi);
        txtUpdateTitle = (TextInputEditText) findViewById(R.id.txtUpdateTitle);
        txtUpdateIsi.setText(card.getIsi());
        txtUpdateTitle.setText(card.getTitle());
        CardAdapter cardAdapter = new CardAdapter(UpdateCardActivity.this);
        cardAdapter.open();
        btnUpdateCard.setOnClickListener(i -> {
            if (TextUtils.isEmpty(txtUpdateTitle.getText())) {
                txtUpdateTitle.setError("Isikan judul");
            } else if(TextUtils.isEmpty(txtUpdateIsi.getText())) {
                txtUpdateIsi.setError("Isikan field isi!");
            } else {
                String title = txtUpdateTitle.getText().toString();
                String isi = txtUpdateIsi.getText().toString();
                long update = cardDB.updateCard(id, title, isi,username);
                Log.d("UPDATE", String.valueOf(update));
                if (update > 0) {
                    cardDB.close();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(this, "Gagal Update", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
}