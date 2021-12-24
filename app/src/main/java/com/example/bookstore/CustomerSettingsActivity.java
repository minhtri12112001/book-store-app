package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.bookstore.TinyDB;
import java.util.HashMap;
import java.util.Map;

public class CustomerSettingsActivity extends AppCompatActivity {
    private TextView tv_more_order, btn_startUpdateUserInformationActivity;
    private TextView tv_userFullName,tv_userEmail, tv_userPhoneNumber, tv_userAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_settings);


        tv_more_order = findViewById(R.id.tv_more_order);

        tv_userFullName = findViewById(R.id.tv_userFullName);
        tv_userEmail = findViewById(R.id.tv_userEmail);
        tv_userPhoneNumber = findViewById(R.id.tv_userPhoneNumber);
        tv_userAddress = findViewById(R.id.tv_userAddress);
        btn_startUpdateUserInformationActivity = findViewById(R.id.btn_startUpdateUserInformationActivity);
        String user_id = MainActivity.user.getUid();


        startUserOrderActivity();


        CallUserAPIFromCloudFireStore(user_id);

        btn_startUpdateUserInformationActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UpdateUserInformationActivity.class));
            }
        });

        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                MainActivity.isLogin = false;
                startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                finish();
            }
        });
        //Define bottom navigation view 's function
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.customerSettings);
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
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private void startUserOrderActivity() {
        tv_more_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UserOrderActivity.class));
            }
        });
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
                            String address = documentSnapshot.getString("address");
                            Log.d ("User information","email: "+ email);
                            Log.d ("User information","full name: "+ fullName);
                            Log.d ("User information","phone number: "+ phoneNumber);
                            tv_userFullName.setText(fullName);
                            tv_userEmail.setText(email);
                            tv_userPhoneNumber.setText(phoneNumber);
                            tv_userAddress.setText(address);
                        }

                    }
                });
    }

}