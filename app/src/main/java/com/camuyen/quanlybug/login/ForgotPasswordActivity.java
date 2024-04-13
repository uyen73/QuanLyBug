package com.camuyen.quanlybug.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.camuyen.quanlybug.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextView txtBackToLogin;
    Button btnRecoverPassword;
    EditText edtEmailRecover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getWidget();
        addAction();

    }

    private void addAction() {
        txtBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        btnRecoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = edtEmailRecover.getText().toString();
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Complete, Please check your email", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ForgotPasswordActivity.this, "Email không đúng", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void getWidget() {
        txtBackToLogin = findViewById(R.id.txtBackToLogin);
        btnRecoverPassword = findViewById(R.id.btnRecoverPassword);
        edtEmailRecover = findViewById(R.id.edtEmailRecover);
    }
}