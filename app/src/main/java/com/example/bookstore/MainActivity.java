package com.example.bookstore;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.Object.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv_top_10_trending_book;
    private RecyclerView rv_book_menu;
    private RecyclerDataAdapter BookAdapter;
    private FirebaseFirestore db;
    private ArrayList<Book> books;
    private ImageView iv_book_image;

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
        db = FirebaseFirestore.getInstance();
        books = new ArrayList<Book>();
        iv_book_image = findViewById(R.id.iv_book_image);
        BookAdapter = new RecyclerDataAdapter(MainActivity.this, books);
        //books.add(new Book("sapiens","firebase.com",50000,10));

        rv_book_menu = (RecyclerView) findViewById(R.id.rv_book_menu);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_book_menu.setLayoutManager(layoutManager);
        rv_book_menu.setHasFixedSize(true);
        rv_book_menu.setAdapter(BookAdapter);

        //rv_top_10_trending_book = (RecyclerView) findViewById(R.id.rv_top_10_trending_book);
        //rv_top_10_trending_book.setLayoutManager(layoutManager);
        //rv_top_10_trending_book.setHasFixedSize(true);
        //rv_book_menu.setAdapter(BookAdapter);
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

                                books.add(doc.getDocument().toObject(Book.class));
                                BookAdapter.notifyDataSetChanged();
                            }


                        }
                    }
                });
    }
}