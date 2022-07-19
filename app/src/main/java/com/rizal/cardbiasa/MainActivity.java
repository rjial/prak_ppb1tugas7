package com.rizal.cardbiasa;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.rizal.cardbiasa.adapter.database.CardAdapter;
import com.rizal.cardbiasa.helper.SwipeToDeleteCallback;
import com.rizal.cardbiasa.model.Card;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "CARD_BIASA" ;
    public static final String name = "nameKey";
    public static final String pass = "passwordKey";
    private String username;
    private TextView txtUsernamePerson;
    private Button btnLogout, btnTambah;
    private List<Card> cardList;
    SharedPreferences sharedPreferences;
    private com.rizal.cardbiasa.adapter.recyclerview.CardAdapter recyCardAdapter;
    private RecyclerView recyCard;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        txtUsernamePerson = (TextView) findViewById(R.id.txtUsernamePerson);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnTambah = (Button) findViewById(R.id.btnTambah);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        this.initApp(sharedPreferences);
        recyCard = (RecyclerView) findViewById(R.id.rcyCard);
        recyCard.setLayoutManager(new LinearLayoutManager(this));
        if (recyCardAdapter == null) {
            recyCardAdapter = new com.rizal.cardbiasa.adapter.recyclerview.CardAdapter(cardList, activityResultLauncher);
        } else {
            recyCardAdapter.notifyDataSetChanged();
        }
        recyCard.setAdapter(recyCardAdapter);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TambahActivity.class);
                intent.putExtra(name, username);
//                startActivity(intent);
                activityResultLauncher.launch(intent);
            }
        });
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Card item = recyCardAdapter.getAllCard().get(position);
                recyCardAdapter.removeItem(position);
                CardAdapter cardDB = new CardAdapter(MainActivity.this);
                cardDB.open();
                cardDB.removeCard(item.getId(), username);
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Card telah dihapus dari list", Snackbar.LENGTH_LONG);
                snackbar.show();
//                cardDB.close();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyCard);
    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.d("BACK", "BACK");
                        CardAdapter cardDB = new CardAdapter(MainActivity.this);
                        cardDB.open();
                        Intent intent = result.getData();
                        cardList = cardDB.getAllCards(username);
                        for(Card card: cardList) {
                            Log.d("CARD " + card.getId(), card.getIsi() + " " + card.getTitle());
                        }
                        recyCardAdapter = new com.rizal.cardbiasa.adapter.recyclerview.CardAdapter(cardList, activityResultLauncher);
                        cardDB.close();
                        recyCard.setAdapter(recyCardAdapter);
                    }
                }
            });

    @Override
    protected void onResume() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        this.initApp(sharedPreferences);
        super.onResume();

    }
    private void initApp(SharedPreferences sharedPreferences) {
        if (sharedPreferences.contains(name)) {
            if (sharedPreferences.contains(pass)) {
                username = sharedPreferences.getString(name, "");
                CardAdapter cardAdapter = new CardAdapter(MainActivity.this);
                cardAdapter.open();
                cardList = cardAdapter.getAllCards(username);
                cardAdapter.close();
//                Log.d("LIST", cardList.toString());
                txtUsernamePerson.setText(username);
//                recyCardAdapter = new com.rizal.cardbiasa.adapter.recyclerview.CardAdapter(cardList);
            }
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
    }
}