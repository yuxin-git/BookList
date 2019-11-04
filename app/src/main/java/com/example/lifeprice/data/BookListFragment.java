package com.example.lifeprice.data;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lifeprice.BookListMainActivity;
import com.example.lifeprice.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookListFragment extends Fragment {

    private BookListMainActivity.BooksArrayAdapter theAdapter;
    public BookListFragment(BookListMainActivity.BooksArrayAdapter theAdaper) {
        // Required empty public constructor
        this.theAdapter=theAdaper;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_book_list, container, false);

        ListView listViewSuper= view.findViewById(R.id.list_view_books);
        listViewSuper.setAdapter(theAdapter);
        this.registerForContextMenu(listViewSuper);

        return view;
    }

}
