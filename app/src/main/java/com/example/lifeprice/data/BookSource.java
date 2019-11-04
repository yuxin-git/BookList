package com.example.lifeprice.data;

import android.content.Context;

import com.example.lifeprice.data.model.Book;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class BookSource {
    Context context;
    ArrayList<Book> books=new ArrayList<Book>();

    public ArrayList<Book> getBooks() {
        return books;
    }
    public BookSource(Context context) {
        this.context = context;
    }

    public void save()
    {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                context.openFileOutput("Serializable.txt",Context.MODE_PRIVATE));
            outputStream.writeObject(books);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Book> load() {      //用于加载
        try {
            ObjectInputStream inputStream = new ObjectInputStream(
                    context.openFileInput("Serializable.txt")
            );
            books = (ArrayList<Book>) inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }
}
