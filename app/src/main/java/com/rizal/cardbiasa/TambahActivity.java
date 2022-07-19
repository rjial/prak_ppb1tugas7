package com.rizal.cardbiasa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.rizal.cardbiasa.adapter.database.CardAdapter;
import com.rizal.cardbiasa.model.Card;

public class TambahActivity extends AppCompatActivity {
    public static final String name = "nameKey";
    private Button btnTambahCard;
    private TextInputEditText txtTambahIsi, txtTambahTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);
        getSupportActionBar().setTitle("Tambah Card");
        Intent intent = getIntent();
        String username = intent.getStringExtra(name);
        btnTambahCard = (Button) findViewById(R.id.btnTambahCard);
        txtTambahIsi = (TextInputEditText) findViewById(R.id.txtTambahIsi);
        txtTambahTitle = (TextInputEditText) findViewById(R.id.txtTambahTitle);
        CardAdapter cardAdapter = new CardAdapter(TambahActivity.this);
        cardAdapter.open();
        btnTambahCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(txtTambahTitle.getText())) {
                    txtTambahTitle.setError("Isikan judul");
                } else if(TextUtils.isEmpty(txtTambahIsi.getText())) {
                    txtTambahIsi.setError("Isikan field isi!");
                } else {
                    String title = txtTambahTitle.getText().toString();
                    String isi = txtTambahIsi.getText().toString();
//                cardAdapter.addCard(title, isi, username);
//                Card card = new Card(-1, title, isi, username);
                    CardAdapter cardDB = new CardAdapter(TambahActivity.this);
                    cardDB.open();
                    cardDB.addCard(title, isi, username);
                    cardDB.close();
                    setResult(RESULT_OK, intent);
                    finish();

                }
            }
        });
    }
}