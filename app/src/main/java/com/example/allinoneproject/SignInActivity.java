package com.example.allinoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private long backPressedTime = 0;
    private TextView textView_signUp;
    private Button button_signIN;
    private EditText editText_email, editText_pass;
    private TextInputLayout textInputLayout_emailError, textInputLayout_passError;
    private Boolean isEmailValid, isPassValid;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //set NavigationBar and statusBar color
        if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }//End
        mAuth = FirebaseAuth.getInstance();

        textView_signUp = findViewById(R.id.tv_signUpID);
        button_signIN = findViewById(R.id.btn_loginID);
        editText_email = findViewById(R.id.eTxt_emailID);
        editText_pass = findViewById(R.id.eTxt_passwordID);
        textInputLayout_passError = findViewById(R.id.passError);
        textInputLayout_emailError = findViewById(R.id.emailError);

        textView_signUp.setOnClickListener(this);
        button_signIN.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
    }

    //BackPressed Process with Toast
    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000)
        {
            backPressedTime = t;
            Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        else {
            super.onBackPressed();
        }
    }//END

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_signUpID:
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.btn_loginID:
                String email = editText_email.getText().toString().trim();
                String pass = editText_pass.getText().toString().trim();

                boolean getValidation = mSetValidation(email, pass);
                if (getValidation)
                    mSignIN(email, pass);
                break;
        }
    }

    private void mSignIN(String email, String pass) {
        progressDialog.setMessage("Signing in..");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean mSetValidation(String email, String pass) {
        //email validity
        if (TextUtils.isEmpty(email))
        {
            textInputLayout_emailError.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            textInputLayout_emailError.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        }
        else
        {
            isEmailValid = true;
            textInputLayout_emailError.setErrorEnabled(false);
        }

        //pass validity
        if (TextUtils.isEmpty(pass))
        {
            textInputLayout_passError.setError(getResources().getString(R.string.password_error));
            isPassValid = false;
        }
        else if (pass.length() < 6)
        {
            textInputLayout_passError.setError(getResources().getString(R.string.error_invalid_password));
            isPassValid = false;
        }
        else {
            textInputLayout_passError.setErrorEnabled(false);
            isPassValid = true;
        }

        //return result true
        if (isEmailValid && isPassValid)
            return true;
        else
            return false;
    }
}
