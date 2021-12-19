package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DeliveryInformationActivity extends AppCompatActivity {
    Button btn_check_deliveryInformation;
    EditText et_deliveryEmail, et_deliveryFullName, et_deliveryPhoneNumber, et_deliveryAddress;
    static String order_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_information);
        btn_check_deliveryInformation = findViewById(R.id.btn_check_deliveryInformation);
        et_deliveryEmail = findViewById(R.id.et_deliveryEmail);
        et_deliveryFullName = findViewById(R.id.et_deliveryFullName);
        et_deliveryPhoneNumber = findViewById(R.id.et_deliveryPhoneNumber);
        et_deliveryAddress = findViewById(R.id.et_deliveryAddress);
        String user_id = MainActivity.user.getUid();
        CallUserAPIFromCloudFireStore(user_id);
        btn_check_deliveryInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateOrderDataToCloudFireStore();
                startActivity(new Intent(getApplicationContext(),ConfirmOrderActivity.class));
            }
        });
    }

    private void CreateOrderDataToCloudFireStore() {
        String new_fullName = et_deliveryFullName.getText().toString();
        String new_email = et_deliveryEmail.getText().toString();
        String new_phoneNumber = et_deliveryPhoneNumber.getText().toString();
        String new_address = et_deliveryAddress.getText().toString();
        String user_id = MainActivity.user.getUid();
        Map<String, Object> order = new HashMap<>();
        order.put("user_id", user_id);
        order.put("email", new_email);
        order.put("fullName", new_fullName);
        order.put("phoneNumber", new_phoneNumber);
        order.put("address", new_address);
        order.put("total_book_id",MainActivity.cartItems.size());
        Log.d("user_id: ", user_id);
        for (int i = 0; i < MainActivity.cartItems.size(); i++) {
            //dorder.put("book_id "+ (i + 1), MainActivity.cartItems.get(i).getBook_id());
            order.put("cartItem" + (i + 1), MainActivity.cartItems.get(i));
        }
        //Log.d("order", FirebaseFirestore.getInstance().collection("orders").document().getId());
        order_id = FirebaseFirestore.getInstance().collection("orders").document().getId();
        FirebaseFirestore.getInstance().collection("orders").document(order_id).set(order);
    }

    private void CallUserAPIFromCloudFireStore(String user_id) {
        FirebaseFirestore.getInstance().collection("users").document(user_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String email = documentSnapshot.getString("email");
                            String fullName = documentSnapshot.getString("fullName");
                            String phoneNumber = documentSnapshot.getString("phoneNumber");
                            String address = documentSnapshot.getString("address");
                            Log.d("User information", "email: " + email);
                            Log.d("User information", "full name: " + fullName);
                            Log.d("User information", "phone number: " + phoneNumber);
                            et_deliveryFullName.setText(fullName);
                            et_deliveryEmail.setText(email);
                            et_deliveryPhoneNumber.setText(phoneNumber);
                            et_deliveryAddress.setText(address);
                        }

                    }
                });
    }
}