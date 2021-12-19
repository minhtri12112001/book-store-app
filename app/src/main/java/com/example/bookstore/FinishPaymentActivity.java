package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class FinishPaymentActivity extends AppCompatActivity {
    TextView tv_order_number;
    ImageView btn_cancel;
    static long currentTotalNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_payment);
        tv_order_number = findViewById(R.id.tv_order_number);
        btn_cancel = findViewById(R.id.btn_cancel);
        Log.d("userFinish", "11111111");
        updateTotalNumberOfEachBook();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                MainActivity.cartItems = null;
                ConfirmOrderActivity.orderItems = null;
            }
        });
        tv_order_number.setText("Số đơn hàng: " + getIntent().getStringExtra("order_number"));
    }

    private void updateTotalNumberOfEachBook() {
        for (int i = 0; i < ConfirmOrderActivity.orderItems.size(); i++){
            String book_id = ConfirmOrderActivity.orderItems.get(i).getBook_id();
            Log.d("current_number", String.valueOf(ConfirmOrderActivity.totalNumberOfEachBooks.get(i)));
            FirebaseFirestore.getInstance().collection("book").document(book_id)
                    .update("total_number",ConfirmOrderActivity.totalNumberOfEachBooks.get(i) - ConfirmOrderActivity.orderItems.get(i).getTotal_number());

            FirebaseFirestore.getInstance().collection("book").document(book_id)
                    .update("number_already_sales",ConfirmOrderActivity.totalNumberAlreadySales.get(i) + ConfirmOrderActivity.orderItems.get(i).getTotal_number());
        }
    }
}