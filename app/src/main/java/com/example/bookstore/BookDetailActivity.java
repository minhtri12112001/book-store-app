package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BookDetailActivity extends AppCompatActivity {
    private TextView tv_book_name, tv_book_cost;
    private ImageView iv_book_detail_book_image,plus_button,minus_button;
    private EditText sizeno_value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

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
                if(value < 0){
                    value = 0;
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
    }
}