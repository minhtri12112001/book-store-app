package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bookstore.Adapter.CartItemDataAdapter;
import com.example.bookstore.Adapter.OrderListDataAdapter;
import com.example.bookstore.Object.CartItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ConfirmOrderActivity extends AppCompatActivity {
    static ArrayList<CartItem> orderItems;
    static ArrayList<Long> totalNumberOfEachBooks;
    static ArrayList<Long> totalNumberAlreadySales;
    private RecyclerView rv_order_list;
    private OrderListDataAdapter orderListDataAdapter;
    TextView tv_order_total_cost;
    Button btn_confirm_order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        tv_order_total_cost = findViewById(R.id.tv_order_total_cost);
        CallOrderAPIFromCloudFireStore(DeliveryInformationActivity.order_id);
        btn_confirm_order = findViewById(R.id.btn_confirm_order);
        orderItems = new ArrayList<>();
        totalNumberOfEachBooks = new ArrayList<>();
        totalNumberAlreadySales = new ArrayList<>();
        orderListDataAdapter = new OrderListDataAdapter(ConfirmOrderActivity.this, orderItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_order_list = (RecyclerView) findViewById(R.id.rv_order_list);
        rv_order_list.setLayoutManager(layoutManager);
        rv_order_list.setHasFixedSize(true);
        rv_order_list.setAdapter(orderListDataAdapter);
        //Log.d("Order item", orderItems.get(0).getBook_id());
        finishOrder(DeliveryInformationActivity.order_id);

    }

    private void finishOrder(String order_id){
        btn_confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int random = new Random().nextInt(50000 - 10000) + 10000;
                Map<String, Object> order = new HashMap<>();
                order.put("order_number","#ST" + random);
                FirebaseFirestore.getInstance().collection("orders").document(order_id).update(order);
                Intent intent = new Intent(getApplicationContext(), FinishPaymentActivity.class);
                intent.putExtra("order_number","#ST" + random);
                startActivity(intent);

            }
        });
    }
    private void showCost() {
        long total_cost = 0;
        for (int i = 0; i < orderItems.size(); i++ ){
            total_cost = total_cost + (orderItems.get(i).getTotal_number() * orderItems.get(i).getCost());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            Log.d("total_cost", String.valueOf(total_cost));
            tv_order_total_cost.setText(decimalFormat.format(total_cost) + " đ");
        }
    }

    private void CallOrderAPIFromCloudFireStore(String order_id) {
        FirebaseFirestore.getInstance().collection("orders").document(order_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            long total_book_id = documentSnapshot.getLong("total_book_id");
                            for (int i = 0; i < total_book_id; i++){
                                //Log.d("Order_item" + i, documentSnapshot.getString("cartItem" + (i+1) +".book_id"));
                                //Log.d("Order_item" + i, documentSnapshot.getString("cartItem" + (i+1) +".book_name"));
                                /*
                                orderItems.add(new CartItem(
                                        documentSnapshot.getString("cartItem" + (i+1) +".book_id"),
                                        documentSnapshot.getString("cartItem" + (i+1) +".book_id"),
                                        documentSnapshot.getLong("cartItem" + (i+1) +".cost"),
                                        documentSnapshot.getString("cartItem" + (i+1) +".book_image"),
                                        documentSnapshot.getLong("cartItem" + (i+1) +".total_number")
                                ));

                                 */
                                CartItem orderItem = new CartItem(
                                        documentSnapshot.getString("cartItem" + (i+1) +".book_id"),
                                        documentSnapshot.getString("cartItem" + (i+1) +".book_name"),
                                        documentSnapshot.getLong("cartItem" + (i+1) +".cost"),
                                        documentSnapshot.getString("cartItem" + (i+1) +".book_image"),
                                        documentSnapshot.getLong("cartItem" + (i+1) +".total_number")
                                );
                                FirebaseFirestore.getInstance().collection("book").document(orderItem.getBook_id()).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                Long total_number = new Long(documentSnapshot.getLong("total_number"));
                                                Long number_already_sales = new Long(0);
                                                if (documentSnapshot.getLong("number_already_sale") != null) {
                                                    number_already_sales = new Long(documentSnapshot.getLong("number_already_sales"));
                                                }
                                                totalNumberAlreadySales.add(number_already_sales);
                                                Log.d("already_sale",String.valueOf(number_already_sales));
                                                totalNumberOfEachBooks.add(total_number);
                                                Log.d("current_number", String.valueOf(total_number));
                                            }
                                        });
                                orderItems.add(orderItem);
                                Log.d("Order item", orderItems.get(i).getBook_id());
                                Log.d("Order item", orderItem.getBook_id());
                                Log.d("Order item", orderItem.getBook_image());
                                Log.d("Order item", orderItem.getBook_name());
                                Log.d("Order item", String.valueOf(orderItem.getCost()));
                                Log.d("Order item", String.valueOf(orderItem.getTotal_number()));
                                orderListDataAdapter.notifyDataSetChanged();
                            }
                            showCost();
                        }

                    }
                });
    }
}