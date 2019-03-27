package com.example.gameshare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText username, fullname, email, password ;
    Button register ;
    TextView txt_login;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.login);
        txt_login = findViewById(R.id.txt_login);

        auth = FirebaseAuth.getInstance();

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Please Wait");
                pd.show();

                String str_username = username.getText().toString();
                String str_fullname = fullname.getText().toString();
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();

                if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_password) || TextUtils.isEmpty(str_fullname) || TextUtils.isEmpty(str_email)) {
                    Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                } else if (str_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "password too short", Toast.LENGTH_SHORT).show();
                } else {
                    register(str_username,str_fullname, str_email,str_password);

                }

            }
        });

    }

        private void register (final String username, final String fullname, String email, String password)
        {
            auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                String userid = firebaseUser.getUid();

                                reference = FirebaseDatabase.getInstance().getReference().child(" ").child(userid);

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("id",userid);
                                hashMap.put("username",username);
                                hashMap.put("fullname",fullname);
                                hashMap.put("bio","");
                                hashMap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/gameshare-3ada0.appspot.com/o/avatar-1577909_960_720.png?alt=media&token=868374d1-e2cb-4de0-9b4f-457425fc47ee");
                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            pd.dismiss();
                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                            else
                            {
                                pd.dismiss();
                                Toast.makeText(RegisterActivity.this,"You cant register with this email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
