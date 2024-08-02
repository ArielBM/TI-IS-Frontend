package com.jobbi.me.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Header {
    @Id
    private int id;

    @Column(name = "date_insertion")
    private String date_insertion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date_insertion;
    }

    public void setDate(String date) {
        this.date_insertion = date;
    }
}
