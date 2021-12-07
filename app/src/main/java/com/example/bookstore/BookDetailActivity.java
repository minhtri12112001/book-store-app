package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;

public class BookDetailActivity extends AppCompatActivity {
    private TextView tv_book_name, tv_book_cost, buy_now_button;
    private ImageView iv_book_detail_book_image,plus_button,minus_button;
    private EditText sizeno_value;
    private Button cart_pay_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        buy_now_button = findViewById(R.id.btn_buy_now);
        tv_book_name = findViewById(R.id.tv_book_detail_book_name);
        tv_book_cost = findViewById(R.id.tv_book_detail_book_cost);
        iv_book_detail_book_image = findViewById(R.id.iv_book_detail_book_image);
        plus_button = findViewById(R.id.plus);
        minus_button = findViewById(R.id.minus);
        sizeno_value = findViewById(R.id.sizeno);
        //Set content for each book detail activity
        tv_book_name.setText(getIntent().getStringExtra("book_name"));
        String book_image = getIntent().getStringExtra("book_image");
        Glide.with(this)
                .load(book_image)
                .into(iv_book_detail_book_image);
        Double book_cost = getIntent().getDoubleExtra("book_cost", 0.0);
        String string_book_cost = Double.toString(book_cost);
        tv_book_cost.setText(string_book_cost + " đ");

        TextView buttonAddToCart = findViewById(R.id.buttonAddToCart);
        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        BookDetailActivity.this, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.layout_bottom_sheet,
                                (LinearLayout)findViewById(R.id.bottomSheetContainer)
                        );
                bottomSheetView.findViewById(R.id.btn_continue).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetView.findViewById(R.id.btn_cart_pay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(BookDetailActivity.this, CartActivity.class);
                        startActivity(intent);
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        //Plus and minus function
        plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer value = Integer.parseInt(sizeno_value.getText().toString());
                value = value + 1;
                sizeno_value.setText(value.toString());
            }
        });
        minus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer value = Integer.parseInt(sizeno_value.getText().toString());
                value = value - 1;
                if(value < 1){
                    value = 1;
                }
                sizeno_value.setText(value.toString());
            }
        });
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
        //set buy now button
        buy_now_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookDetailActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }
}