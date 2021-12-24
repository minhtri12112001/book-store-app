package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.bookstore.Adapter.OrderListDataAdapter;
import com.example.bookstore.Adapter.UserOrderAdapter;
import com.example.bookstore.Object.CartItem;
import com.example.bookstore.Object.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserOrderActivity extends AppCompatActivity {
    private ArrayList<Order> orders;
    private UserOrderAdapter userOrderAdapter;
    private RecyclerView rv_user_order;
    private ImageView btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);
        btn_back = findViewById(R.id.btn_back_customerSetting);
        orders = new ArrayList<>();
        userOrderAdapter = new UserOrderAdapter(this,orders);
        rv_user_order = findViewById(R.id.rv_user_order);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rv_user_order = (RecyclerView) findViewById(R.id.rv_user_order);
        rv_user_order.setLayoutManager(layoutManager);
        rv_user_order.setHasFixedSize(true);
        rv_user_order.setAdapter(userOrderAdapter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CustomerSettingsActivity.class));
            }
        });

        getOrderListOfUserFromCloudFirestore();
    }

    private void getOrderListOfUserFromCloudFirestore() {
        FirebaseFirestore.getInstance().collection("orders").whereEqualTo("user_id",MainActivity.user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document :task.getResult()){
                        Order order = new Order();
                        order.setOrder_id(document.getId());
                        Log.d("Order_id",document.getId());
                        order.setOrder_number(document.getString("order_number"));
                        order.setAddress(document.getString("address"));
                        order.setPaid(document.getBoolean("isPaid"));
                        orders.add(order);
                        userOrderAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}