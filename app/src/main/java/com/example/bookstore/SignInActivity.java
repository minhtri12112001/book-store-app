package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private TextView sign_in_error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.txt_email);
        password = findViewById(R.id.txt_password);
        mAuth = FirebaseAuth.getInstance();
        sign_in_error = findViewById(R.id.sign_in_error);
        sign_in_error.setVisibility(View.INVISIBLE);
        //Bottom navigation function
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
        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    sign_in_error.setText("Mật khẩu hoặc email trống, vui lòng nhập vào");
                    sign_in_error.setVisibility(View.VISIBLE);
                    //Toast.makeText(SignInActivity.this, "Empty credentials.", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.signInWithEmailAndPassword(txt_email,txt_password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    sign_in_error.setVisibility(View.INVISIBLE);
                                    MainActivity.isLogin = true;
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    sign_in_error.setText("Mật khẩu sai, vui lòng đăng nhập lại");
                                    sign_in_error.setVisibility(View.VISIBLE);
                                    //Toast.makeText(SignInActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            });
                }



            }
        });
        TextView btn_sign_up = findViewById(R.id.btn_startSignUpActivity);
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });
    }
}