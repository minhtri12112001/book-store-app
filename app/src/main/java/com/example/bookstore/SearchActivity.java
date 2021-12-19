package com.example.bookstore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.example.bookstore.Adapter.SearchBookDataAdapter;
import com.example.bookstore.Object.Book;
import com.example.bookstore.Object.VNCharacterUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView rv_search_book_list;
    private SearchView searchView;
    private ArrayList<Book> searchBooks;
    private SearchBookDataAdapter searchBookDataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchBooks = new ArrayList<>();
        searchView = findViewById(R.id.sv_search_two);
        searchBookDataAdapter = new SearchBookDataAdapter(SearchActivity.this, searchBooks);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        ///*
        rv_search_book_list = (RecyclerView) findViewById(R.id.rv_search_book_list);
        rv_search_book_list.setAdapter(searchBookDataAdapter);
        rv_search_book_list.setLayoutManager(gridLayoutManager);
        rv_search_book_list.setHasFixedSize(true);

        FirebaseFirestore.getInstance().collection("book")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null){
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){

                            if (doc.getType() == DocumentChange.Type.ADDED){
                                String book_name = VNCharacterUtils.removeAccent(doc.getDocument().getString("book_name").toLowerCase());
                                String book_author = VNCharacterUtils.removeAccent(doc.getDocument().getString("author").toLowerCase());
                                String book_category = VNCharacterUtils.removeAccent(doc.getDocument().getString("category").toLowerCase());

                                if (book_name.contains(VNCharacterUtils.removeAccent(getIntent().getStringExtra("search_text").toLowerCase()))
                                    || book_author.contains(VNCharacterUtils.removeAccent(getIntent().getStringExtra("search_text").toLowerCase()))
                                        || book_category.contains(VNCharacterUtils.removeAccent(getIntent().getStringExtra("search_text").toLowerCase()))
                                ){
                                    Log.d("i_love_you", "3000");
                                    String book_id = doc.getDocument().getId();
                                    Book book = doc.getDocument().toObject(Book.class);
                                    book.setBook_id(book_id);
                                    searchBooks.add(book);
                                    searchBookDataAdapter.notifyDataSetChanged();
                                    Log.d("Document",book.getBook_id());
                                }

                            }
                        }
                    }
                });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String text = s;
                if (text.length() > 0 ){
                    Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                    intent.putExtra("search_text", text);
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}