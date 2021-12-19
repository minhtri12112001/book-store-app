package com.example.bookstore;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookstore.Adapter.CartItemDataAdapter;
import com.example.bookstore.Object.CartItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    private TextView tv_cart_item_book_name, tv_cart_item_total_cost;
    private ImageView iv_cart_item_image;
    public static CartItemDataAdapter cartItemDataAdapter;
    static RecyclerView rv_cart_list;
    static TextView tv_thong_bao;
    private FirebaseFirestore db;
    public static TextView tv_cart_total_cost;
    static ImageView iv_empty_cart;
    static Button btn_continued_buy, btn_thanhtoan;
    static long total_cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        tv_thong_bao = (TextView) findViewById(R.id.textviewthongbao);
        iv_empty_cart = (ImageView) findViewById(R.id.emptycart);
        btn_continued_buy = (Button) findViewById(R.id.btn_continued_buy);
        btn_thanhtoan = findViewById(R.id.btn_thanhtoan);
        tv_cart_total_cost = (TextView) findViewById(R.id.tv_cart_total_cost);
        db = FirebaseFirestore.getInstance();
        cartItemDataAdapter = new CartItemDataAdapter(CartActivity.this, MainActivity.cartItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_cart_list = (RecyclerView) findViewById(R.id.rv_cart_list);
        rv_cart_list.setLayoutManager(layoutManager);
        rv_cart_list.setHasFixedSize(true);
        rv_cart_list.setAdapter(cartItemDataAdapter);


        checkData();

        sumTotalCost();
        btn_continued_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        /*
        if (MainActivity.cartItems == null){
            btn_thanhtoan.setClickable(false);
            Log.d("Cannot click", "true");
        }

         */
        /*
        if (MainActivity.cartItems != null){
            btn_thanhtoan.setClickable(true);
        }
        */
        Log.d("User", String.valueOf(MainActivity.cartItems));
        if ((MainActivity.isLogin == false) || (MainActivity.cartItems.size() == 0) ) {
            Log.d("User", "error");
            btn_thanhtoan.setEnabled(false);

        } else {
            btn_thanhtoan.setEnabled(true);
        }
        btn_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DeliveryInformationActivity.class));
            }
        });

        //Bottom navigation function
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.cart);
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
                    case R.id.customerSettings:
                        if (MainActivity.isLogin == true) {
                            startActivity(new Intent(getApplicationContext(), CustomerSettingsActivity.class));
                            return true;
                        } else {
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                }
                return false;
            }
        });
    }

    public static void sumTotalCost() {
        //long total_cost = 0;
        total_cost = 0;
        for (int i = 0; i < MainActivity.cartItems.size(); i++) {
            total_cost = total_cost + (MainActivity.cartItems.get(i).getCost() * MainActivity.cartItems.get(i).getTotal_number());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            tv_cart_total_cost.setText(decimalFormat.format(total_cost) + " đ");
            cartItemDataAdapter.notifyDataSetChanged();

        }
    }

    public static void createOrder() {
        if (MainActivity.user != null) {
            if (MainActivity.cartItems != null) {
                String user_id = MainActivity.user.getUid();
                Map<String, Object> order = new HashMap<>();
                order.put("user_id", user_id);
                Log.d("user_id: ", user_id);
                for (int i = 0; i < MainActivity.cartItems.size(); i++) {
                    order.put("book_id" + i, MainActivity.cartItems.get(i).getBook_id());
                }
                FirebaseFirestore.getInstance().collection("orders").add(order);
            }

        }
    }

    public static void checkData() {
        if (MainActivity.cartItems.size() <= 0) {

            tv_thong_bao.setVisibility(View.VISIBLE);
            iv_empty_cart.setVisibility(View.VISIBLE);
            btn_continued_buy.setVisibility(View.VISIBLE);
            rv_cart_list.setVisibility(View.INVISIBLE);
            tv_cart_total_cost.setText("0 đ");
            cartItemDataAdapter.notifyDataSetChanged();
        } else {
            tv_thong_bao.setVisibility(View.INVISIBLE);
            iv_empty_cart.setVisibility(View.INVISIBLE);
            btn_continued_buy.setVisibility(View.INVISIBLE);
            rv_cart_list.setVisibility(View.VISIBLE);
            cartItemDataAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.cartItems);
        editor.putString("Cart list", json);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.cartItems);
        editor.putString("Cart list", json);
        editor.apply();
    }
}