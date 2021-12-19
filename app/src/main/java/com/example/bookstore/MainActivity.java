package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.bookstore.Adapter.Top5CheapestCostBookDataAdapter;
import com.example.bookstore.Adapter.Top5PublishDayBookDataAdapter;
import com.example.bookstore.Adapter.Top5TrendingBookDataAdapter;
import com.example.bookstore.Object.Book;
import com.example.bookstore.Object.CartItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv_top_5_cheapest_cost_book, rv_top_5_publish_day_book;
    private RecyclerView rv_top_5_trending_book;
    private Top5PublishDayBookDataAdapter top5PublishDayBookAdapter;
    private Top5CheapestCostBookDataAdapter top5CheapestCostBookDataAdapter;
    private Top5TrendingBookDataAdapter top5TrendingBookDataAdapter;
    private FirebaseFirestore db;
    private ArrayList<Book> top5PublishDayBooks, top5CheapestCostBooks, top5TrendingBooks;
    public static ArrayList<CartItem> cartItems;
    public static FirebaseAuth firebaseAuth;
    public static FirebaseUser user;
    public static boolean isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user != null){
            isLogin = true;
        }
        //Define database source, others books lists
        db = FirebaseFirestore.getInstance();

        top5TrendingBooks = new ArrayList<>();
        top5PublishDayBooks = new ArrayList<Book>();
        top5CheapestCostBooks = new ArrayList<Book>();

        top5PublishDayBookAdapter = new Top5PublishDayBookDataAdapter(MainActivity.this, top5PublishDayBooks);
        top5CheapestCostBookDataAdapter = new Top5CheapestCostBookDataAdapter(MainActivity.this, top5CheapestCostBooks);
        top5TrendingBookDataAdapter = new Top5TrendingBookDataAdapter(MainActivity.this, top5TrendingBooks);

        // Set adapter and layout manager for each books lists recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        // Top 5 trending books
        rv_top_5_trending_book = (RecyclerView) findViewById(R.id.rv_top_5_trending_book);
        rv_top_5_trending_book.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_top_5_trending_book.setHasFixedSize(true);
        rv_top_5_trending_book.setAdapter(top5TrendingBookDataAdapter);

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
        showUserInformation();
        // Define Array for cart items
        if (cartItems != null){
            //loadCartListData();
            //saveCartList();

        }
        else {
            cartItems = new ArrayList<CartItem>();

        }








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
                        if (MainActivity.isLogin == true ){
                            startActivity(new Intent(getApplicationContext(),CustomerSettingsActivity.class));
                            return true;
                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                }
                return false;
            }
        });
    }

    private void showUserInformation(){

        if (user != null) {
            String user_id = user.getUid();
            Log.d("User information","user id: "+ user);
            Log.d ("User information","user id: "+ user_id);
            CallUserAPIFromCloudFireStore(user_id);
        } else {
            return;
        }
    }
    private void CallUserAPIFromCloudFireStore(String user_id){
        FirebaseFirestore.getInstance().collection("users").document(user_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String email = documentSnapshot.getString("email");
                            String fullName = documentSnapshot.getString("fullName");
                            String phoneNumber = documentSnapshot.getString("phoneNumber");
                            Log.d ("User information","email: "+ email);
                            Log.d ("User information","full name: "+ fullName);
                            Log.d ("User information","phone number: "+ phoneNumber);
                        }

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
        //Get top 5 trending books from cloud firestore
        FirebaseFirestore.getInstance().collection("book").orderBy("number_already_sales", Query.Direction.DESCENDING).limit(5)
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
                                top5TrendingBooks.add(book);
                                top5TrendingBookDataAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
    private void saveCartList(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cartItems);
        editor.putString("Cart list", json);
        editor.apply();
    }
    private void loadCartListData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Cart list", null);
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        cartItems = gson.fromJson(json, type);

        if (cartItems == null){
            cartItems = new ArrayList<CartItem>();
        }
    }
}