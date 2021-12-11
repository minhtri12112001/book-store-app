package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookstore.Adapter.CartItemDataAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {
    private TextView tv_cart_item_book_name, tv_cart_item_total_cost;
    private ImageView iv_cart_item_image;
    public static CartItemDataAdapter cartItemDataAdapter;
    static RecyclerView rv_cart_list;
    static TextView tv_thong_bao;
    public  static TextView tv_cart_total_cost;
    static ImageView iv_empty_cart;
    static Button btn_continued_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        tv_thong_bao = (TextView) findViewById(R.id.textviewthongbao);
        iv_empty_cart = (ImageView) findViewById(R.id.emptycart);
        btn_continued_buy = (Button) findViewById(R.id.btn_continued_buy);
        tv_cart_total_cost = (TextView) findViewById(R.id.tv_cart_total_cost);

        cartItemDataAdapter = new CartItemDataAdapter(CartActivity.this, MainActivity.cartItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_cart_list = (RecyclerView) findViewById(R.id.rv_cart_list);
        rv_cart_list.setLayoutManager(layoutManager);
        rv_cart_list.setHasFixedSize(true);
        rv_cart_list.setAdapter(cartItemDataAdapter);


        checkData();

        sumTotalCost();


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
                        startActivity(new Intent(getApplicationContext(), CustomerSettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    public static void sumTotalCost() {
        long total_cost = 0;
        for (int i = 0; i < MainActivity.cartItems.size(); i++){
            total_cost = total_cost + (MainActivity.cartItems.get(i).getCost() * MainActivity.cartItems.get(i).getTotal_number());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            tv_cart_total_cost.setText(decimalFormat.format(total_cost) + " đ");
            cartItemDataAdapter.notifyDataSetChanged();

        }
    }

    public static void checkData() {
        if (MainActivity.cartItems.size() <= 0){

            tv_thong_bao.setVisibility(View.VISIBLE);
            iv_empty_cart.setVisibility(View.VISIBLE);
            btn_continued_buy.setVisibility(View.VISIBLE);
            rv_cart_list.setVisibility(View.INVISIBLE);
            tv_cart_total_cost.setText("0 đ");
            cartItemDataAdapter.notifyDataSetChanged();
        }
        else {
            tv_thong_bao.setVisibility(View.INVISIBLE);
            iv_empty_cart.setVisibility(View.INVISIBLE);
            btn_continued_buy.setVisibility(View.INVISIBLE);
            rv_cart_list.setVisibility(View.VISIBLE);
            cartItemDataAdapter.notifyDataSetChanged();
        }
    }
}