package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.bookstore.Adapter.Top5CheapestCostBookDataAdapter;
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

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv_top_5_cheapest_cost_book, rv_top_5_publish_day_book;
    private RecyclerView rv_book_menu;
    private Top5PublishDayBookDataAdapter top5PublishDayBookAdapter;
    private Top5CheapestCostBookDataAdapter top5CheapestCostBookDataAdapter;
    private FirebaseFirestore db;
    private ArrayList<Book> books, top5PublishDayBooks, top5CheapestCostBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if( task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()){
                        Log.d("Document",doc.getData().toString());
                        //Log.d("Document",doc.getString("cost"));
                    }
                    else {
                        Log.d("Document","No data");
                    }
                }
            }
        });*/


        // Get book's data from Cloud Firestore (Firebase)
        /*List<Book> books = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("book").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()){
                            books.add(new Book(
                                    doc.getString("book_name"),
                                    doc.getString("book_image"),
                                    doc.getDouble("cost"),
                                    doc.getDouble("total_number")
                            ));

                            Log.d("Document", doc.getId() + "=>" + book_name);
                        }
                    }
                });*/

        //Define database source, others books lists
        db = FirebaseFirestore.getInstance();
        top5PublishDayBooks = new ArrayList<Book>();
        top5CheapestCostBooks = new ArrayList<Book>();
        //books = new ArrayList<Book>();
        //BookAdapter = new Top5PublishDayBookDataAdapter(MainActivity.this, books);
        top5PublishDayBookAdapter = new Top5PublishDayBookDataAdapter(MainActivity.this, top5PublishDayBooks);
        top5CheapestCostBookDataAdapter = new Top5CheapestCostBookDataAdapter(MainActivity.this, top5CheapestCostBooks);


        // Set adapter and layout manager for each books lists recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        // Top 5 publish day books
        rv_top_5_publish_day_book = (RecyclerView) findViewById(R.id.rv_top_5_publish_day_book);
        rv_top_5_publish_day_book.setLayoutManager(layoutManager);
        rv_top_5_publish_day_book.setHasFixedSize(true);
        rv_top_5_publish_day_book.setAdapter(top5PublishDayBookAdapter);

        // Top 5 cheapest cost books
        rv_top_5_cheapest_cost_book = (RecyclerView) findViewById(R.id.rv_top_5_cheapest_cost_book);
        rv_top_5_cheapest_cost_book.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_top_5_cheapest_cost_book.setHasFixedSize(true);
        rv_top_5_cheapest_cost_book.setAdapter(top5CheapestCostBookDataAdapter);





        CallBookAPIFromCloudFireStore();





        //Define bottom navigation view 's function
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
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

        ImageView book_image = (ImageView) findViewById(R.id.book_detail);
        book_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BookDetailActivity.class));
            }
        });
    }
    private void CallBookAPIFromCloudFireStore(){

        //Get top 5 publish day books from cloud firestore
        FirebaseFirestore.getInstance().collection("book").orderBy("publish_day", Query.Direction.DESCENDING).limit(5)
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
                                //Log.d("Document",book_id);
                                Book book = doc.getDocument().toObject(Book.class);
                                book.setBook_id(book_id);
                                Log.d("Document",book.getBook_id());
                                top5PublishDayBooks.add(book);
                                top5PublishDayBookAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        //Get top 5 cheapest cost books from cloud firestore
        FirebaseFirestore.getInstance().collection("book").orderBy("cost", Query.Direction.ASCENDING).limit(5)
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
                                Log.d("Document",book.getBook_id());
                                top5CheapestCostBooks.add(book);
                                top5CheapestCostBookDataAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}