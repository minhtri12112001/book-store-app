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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.bookstore.TinyDB;
import java.util.HashMap;
import java.util.Map;

public class CustomerSettingsActivity extends AppCompatActivity {
    private EditText et_userFullName,et_userEmail,et_userPhoneNumber,et_userAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_settings);

        et_userFullName = findViewById(R.id.et_userFullName);
        et_userEmail = findViewById(R.id.et_userEmail);
        et_userPhoneNumber = findViewById(R.id.et_userPhoneNumber);
        et_userAddress = findViewById(R.id.et_userAddress);
        String user_id = MainActivity.user.getUid();

        Button btn_update_userInformation = findViewById(R.id.btn_update_userInformation);
        btn_update_userInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUserDataToCloudFireStore(user_id);
            }
        });
        CallUserAPIFromCloudFireStore(user_id);

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

    private void UpdateUserDataToCloudFireStore(String user_id) {
        String new_fullName = et_userFullName.getText().toString();
        String new_email = et_userEmail.getText().toString();
        String new_phoneNumber = et_userPhoneNumber.getText().toString();
        String new_address = et_userAddress.getText().toString();
        Map<String, Object> userInformation = new HashMap<>();
        userInformation.put("fullName",new_fullName);
        userInformation.put("email", new_email);
        userInformation.put("phoneNumber", new_phoneNumber);
        userInformation.put("address", new_address);
        FirebaseFirestore.getInstance().collection("users").document(user_id)
                .update(userInformation);


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
                            et_userFullName.setText(fullName);
                            et_userEmail.setText(email);
                            et_userPhoneNumber.setText(phoneNumber);
                            et_userAddress.setText(address);
                        }

                    }
                });
    }

}