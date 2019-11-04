package com.example.lifeprice.data.model;

import java.io.Serializable;

public class Book implements Serializable {
    private String name;
    private double price;
    private int pictureId;

    public Book(String name,int pictureId) {
        this.name = name;

        this.pictureId = pictureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

}
