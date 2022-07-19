package com.rizal.cardbiasa.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable {
    private int id;
    private String title;
    private String isi;
    private String username;

    public Card(int id, String title, String isi, String username) {
        this.id = id;
        this.title = title;
        this.isi = isi;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.isi);
        dest.writeString(this.username);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.title = source.readString();
        this.isi = source.readString();
        this.username = source.readString();
    }

    protected Card(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.isi = in.readString();
        this.username = in.readString();
    }

    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel source) {
            return new Card(source);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
}

