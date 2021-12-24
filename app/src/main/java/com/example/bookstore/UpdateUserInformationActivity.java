package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateUserInformationActivity extends AppCompatActivity {
    private EditText et_userFullName,et_userEmail,et_userPhoneNumber,et_userAddress;
    private ImageView btn_userInformation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_information);
        et_userFullName = findViewById(R.id.et_userFullName);
        et_userEmail = findViewById(R.id.et_userEmail);
        et_userPhoneNumber = findViewById(R.id.et_userPhoneNumber);
        et_userAddress = findViewById(R.id.et_userAddress);
        btn_userInformation = findViewById(R.id.btn_userInformation);
        btn_userInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CustomerSettingsActivity.class));
            }
        });


        Button btn_update_userInformation = findViewById(R.id.btn_update_userInformation);
        btn_update_userInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUserDataToCloudFireStore(MainActivity.user.getUid());
            }
        });
        CallUserAPIFromCloudFireStore(MainActivity.user.getUid());
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