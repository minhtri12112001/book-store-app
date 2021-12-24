package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.bookstore.Adapter.OrderListDataAdapter;
import com.example.bookstore.Object.CartItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {
    private ArrayList<CartItem> orderItems;
    private OrderListDataAdapter orderListDataAdapter;
    private RecyclerView rv_order_detail;
    private ImageView btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        btn_back = findViewById(R.id.btn_back_userOrder);
        orderItems = new ArrayList<>();
        orderListDataAdapter = new OrderListDataAdapter(this,orderItems);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_order_detail = (RecyclerView) findViewById(R.id.rv_order_detail);
        rv_order_detail.setLayoutManager(layoutManager);
        rv_order_detail.setHasFixedSize(true);
        rv_order_detail.setAdapter(orderListDataAdapter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UserOrderActivity.class));
            }
        });

        CallOrderAPIFromCloudFireStore(getIntent().getStringExtra("order_id"));
    }
    private void CallOrderAPIFromCloudFireStore(String order_id) {
        FirebaseFirestore.getInstance().collection("orders").document(order_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            long total_book_id = documentSnapshot.getLong("total_book_id");
                            for (int i = 0; i < total_book_id; i++){
                                CartItem orderItem = new CartItem(
                                        documentSnapshot.getString("cartItem" + (i+1) +".book_id"),
                                        documentSnapshot.getString("cartItem" + (i+1) +".book_name"),
                                        documentSnapshot.getLong("cartItem" + (i+1) +".cost"),
                                        documentSnapshot.getString("cartItem" + (i+1) +".book_image"),
                                        documentSnapshot.getLong("cartItem" + (i+1) +".total_number")
                                );
                                orderItems.add(orderItem);
                                orderListDataAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });
    }
}