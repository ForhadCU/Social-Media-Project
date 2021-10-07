package com.example.allinoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView textView_alreadyReg;
    private Button button_reg;
    private EditText editText_name, editText_email, editText_pass;
    private TextInputLayout textInputLayout_nameError, textInputLayout_emailError,  textInputLayout_passError;
    private boolean isNameValid, isEmailValid, isPassValid;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private String status = "offline";
    private String currentUid;

    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestoreDB.collection("USERS");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Sing Up");

        toolbar = findViewById(R.id.appBarID);
        setSupportActionBar(toolbar);
        //add BackUp button on ActionBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //set NavigationBar and statusBar color
        if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }//End

        mAuth = FirebaseAuth.getInstance();

        textView_alreadyReg = findViewById(R.id.tv_alreadyRegID);
        button_reg = findViewById(R.id.btn_registerID);
        editText_name = findViewById(R.id.eTxt_nameRegID);
        editText_email = findViewById(R.id.eTxt_emailRegID);
        editText_pass = findViewById(R.id.eTxt_passwordRegID);
        textInputLayout_nameError = findViewById(R.id.nameError);
        textInputLayout_emailError = findViewById(R.id.emailError);
        textInputLayout_passError = findViewById(R.id.passError);


        button_reg.setOnClickListener(this);
        textView_alreadyReg.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_alreadyRegID:
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.btn_registerID:
                String name = editText_name.getText().toString().trim();
                String email = editText_email.getText().toString().trim();
                String pass = editText_pass.getText().toString().trim();

                boolean getValidation = mSetValidation(name, email, pass);
                if (getValidation)
                    mSignUp(name, email, pass);
                break;
        }
    }

    private void mSignUp(final String name, final String email, final String pass) {
        progressDialog.setMessage("Signing Up....");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    //save user data into Firestore
                    currentUid = mAuth.getCurrentUser().getUid();
                    mSaveUserToFirestore(name, email, pass);
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void mSaveUserToFirestore(String name, String email, String pass) {
        User_data_handler user_data_handler = new User_data_handler(name, email, pass, status);

//        collectionReference.add(user_data_handler);
        firestoreDB.collection("USERS").document(currentUid).set(user_data_handler);
    }

    private boolean mSetValidation(String name, String email, String pass) {
        //name validity
        if (TextUtils.isEmpty(name))
        {
            textInputLayout_nameError.setError(getResources().getString(R.string.name_error));
            isNameValid = false;
        }
        else{
            isNameValid = true;
            textInputLayout_nameError.setErrorEnabled(false);
        }

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
        else {
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
            isPassValid = true;
            textInputLayout_passError.setErrorEnabled(false);
        }

        //return result true
        if (isNameValid && isEmailValid && isPassValid)
        {
            return true;
        }
        else
            return false;

    }

    //add BackUp button on ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }//END
}
