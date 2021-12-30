package com.example.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "EmailPassword";
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button btn_signup;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String user_id;
    private TextView sign_up_error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sign_up_error = findViewById(R.id.sign_up_error);
        sign_up_error.setVisibility(View.INVISIBLE);
        db = FirebaseFirestore.getInstance();
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
                        /*
                    case R.id.customerSettings:
                        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                         */
                }
                return false;
            }
        });


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        btn_signup = findViewById(R.id.btn_signup);
        LoadingDialog loadingDialog = new LoadingDialog(SignUpActivity.this);
        mAuth = FirebaseAuth.getInstance();
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_confirmPassword = confirmPassword.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    sign_up_error.setText("Vui lòng không để trống email và tài khoản");
                    sign_up_error.setVisibility(View.VISIBLE);
                    //Toast.makeText(SignUpActivity.this, "Empty credentials.", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    sign_up_error.setText("Mật khẩu quá ngắn, vui lòng nhập lại");
                    sign_up_error.setVisibility(View.VISIBLE);
                    //Toast.makeText(SignUpActivity.this, "Password too short.", Toast.LENGTH_SHORT).show();
                } else if((txt_password.equals(txt_confirmPassword)) == false ){
                    sign_up_error.setText("Vui lòng xác nhận đúng mật khẩu đã nhập");
                    sign_up_error.setVisibility(View.VISIBLE);
                    //Toast.makeText(SignUpActivity.this, "Vui lòng nhập đúng lại mật khẩu xác nhận.", Toast.LENGTH_SHORT).show();
                } else {
                    sign_up_error.setVisibility(View.INVISIBLE);
                    createAccount(txt_email, txt_password);
                    loadingDialog.startLoadingDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();
                        }
                    }, 5000);
                }
            }
        });
        TextView btn_sign_in = findViewById(R.id.btn_startSignInActivity);
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    user_id = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("users").document(user_id);
                    Map<String,Object> user = new HashMap<>();
                    user.put("email",mAuth.getCurrentUser().getEmail());
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("msgOnSuccess:", "user profile is created for "+ user_id);
                        }
                    });
                    MainActivity.isLogin = true;
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignUpActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
        // [END create_user_with_email]
    }

    private void reload() {
    }

    private void updateUI(FirebaseUser user) {
    }

}
