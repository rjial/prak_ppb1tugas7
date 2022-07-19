package com.rizal.cardbiasa.adapter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rizal.cardbiasa.model.Card;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_ISI = "isi";
    public static final String KEY_USERNAME = "username";
    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "cards.db";
    private static final String DATABASE_TABLE = "cards";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " ("+ KEY_ROWID +" integer primary key autoincrement, "
                    + KEY_TITLE + " text not null, "
                    + KEY_ISI + " text not null, "
                    + KEY_USERNAME + " text not null);";
    private Context context = null;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public CardAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME,null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(sqLiteDatabase);
        }

    }
    public void open() throws SQLException {
        db = DBHelper.getWritableDatabase();
    }
    public void close() {
        DBHelper.close();
    }
    public List<Card> getAllCards(String username) {
        List<Card> cardList = new ArrayList<Card>();
        String queryAllCard = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_USERNAME + " = '" + username + "'";
        Cursor cursor = db.rawQuery(queryAllCard, null);
        if (cursor.moveToFirst()) {
            do {
                Card card = new Card(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                cardList.add(card);
            } while (cursor.moveToNext());
        }
        return cardList;
    }
    public Card getCard(String id) {
        String queryCard = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_ROWID + " = " + id;
        Cursor cursor =  db.rawQuery(queryCard, null);
        if (cursor != null) cursor.moveToFirst();
        Card card = new Card(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return card;
    }
    public void removeCard(int id, String username) {
        String queryCard = "DELETE FROM " + DATABASE_TABLE + " WHERE " + KEY_ROWID + " = " + String.valueOf(id) + " AND " + KEY_USERNAME + " = '" + username + "'";
        db.execSQL(queryCard);
        db.close();
    }
    public long addCard(String title, String isi, String username) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, title);
        contentValues.put(KEY_ISI, isi);
        contentValues.put(KEY_USERNAME, username);
        return db.insert(DATABASE_TABLE, null, contentValues);
    }
    public long updateCard(int id, String title, String isi, String username) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, title);
        contentValues.put(KEY_ISI, isi);
        return db.update(DATABASE_TABLE, contentValues, KEY_ROWID + "= ?", new String[]{String.valueOf(id)});
    }
}
