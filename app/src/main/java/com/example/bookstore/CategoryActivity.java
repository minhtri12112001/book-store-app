package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.bookstore.Adapter.BookCategoryDataAdapter;
import com.example.bookstore.Object.Category;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView rv_book_category_list;
    private BookCategoryDataAdapter bookCategoryDataAdapter;
    private FirebaseFirestore db;
    private ArrayList<Category> bookCategories;
    //private HashSet<String> bookCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        db = FirebaseFirestore.getInstance();
        bookCategories = new ArrayList<Category>();
        bookCategoryDataAdapter = new BookCategoryDataAdapter(CategoryActivity.this,bookCategories);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        rv_book_category_list = (RecyclerView) findViewById(R.id.rv_book_category_list);
        rv_book_category_list.setLayoutManager(gridLayoutManager);
        rv_book_category_list.setHasFixedSize(true);
        rv_book_category_list.setAdapter(bookCategoryDataAdapter);

        CallBookAPIFromCloudFireStore();


        //Bottom navigation function
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.category);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.customerSettings:
                        if (MainActivity.isLogin == true ){
                            startActivity(new Intent(getApplicationContext(),CustomerSettingsActivity.class));
                            return true;
                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(), CartActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
    private void CallBookAPIFromCloudFireStore(){

        //Get distinct book categories list
        FirebaseFirestore.getInstance().collection("category")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null){
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){

                            if (doc.getType() == DocumentChange.Type.ADDED){
                                String category_id = doc.getDocument().getId();
                                //Log.d("Document",book_id);
                                Category category = doc.getDocument().toObject(Category.class);
                                category.setCategory_id(category_id);
                                //Log.d("Document",book.getBook_id());
                                bookCategories.add(category);
                                bookCategoryDataAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

    }
}