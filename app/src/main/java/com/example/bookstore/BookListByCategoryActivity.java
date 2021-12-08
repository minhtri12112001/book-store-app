package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bookstore.Adapter.BookListByCategoryDataAdapter;
import com.example.bookstore.Adapter.Top5PublishDayBookDataAdapter;
import com.example.bookstore.Object.Book;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BookListByCategoryActivity extends AppCompatActivity {
    private RecyclerView rv_book_list_by_category;
    private BookListByCategoryDataAdapter bookListByCategoryDataAdapter;
    private ArrayList<Book> books;
    private FirebaseFirestore db;
    private TextView tv_category_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_by_category);

        tv_category_name = (TextView) findViewById(R.id.tv_category_name);
        tv_category_name.setText(getIntent().getStringExtra("category_name"));
        db = FirebaseFirestore.getInstance();
        books = new ArrayList<Book>();
        bookListByCategoryDataAdapter = new BookListByCategoryDataAdapter(BookListByCategoryActivity.this, books);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        ///*
        rv_book_list_by_category = (RecyclerView) findViewById(R.id.rv_book_list_by_category);
        rv_book_list_by_category.setAdapter(bookListByCategoryDataAdapter);
        rv_book_list_by_category.setLayoutManager(gridLayoutManager);
        rv_book_list_by_category.setHasFixedSize(true);
        //rv_book_list_by_category.setAdapter(bookListByCategoryDataAdapter);
        //*/
        CallBookAPIFromCloudFireStore();

        //Bottom navigation function
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.category:
                        startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(), CartActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.customerSettings:
                        startActivity(new Intent(getApplicationContext(), CustomerSettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
    private void CallBookAPIFromCloudFireStore(){

        FirebaseFirestore.getInstance().collection("book").whereEqualTo("id_category",getIntent().getStringExtra("category_id"))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                String book_id = doc.getDocument().getId();
                                Book book = doc.getDocument().toObject(Book.class);
                                book.setBook_id(book_id);
                                Log.d("Book List ID",book.getBook_id());
                                books.add(book);
                                bookListByCategoryDataAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}