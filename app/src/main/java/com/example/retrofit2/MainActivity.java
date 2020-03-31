package com.example.retrofit2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView imgSignIn;
    EditText edtUserNameSI, edtPassWordSI;
    Button btnSignIn, btnToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControl();
        addEvents();
    }

    private void addEvents() {
        

    }

    private void addControl() {
        imgSignIn = findViewById(R.id.imgSignIn);
        edtUserNameSI = findViewById(R.id.edtUserNameSI);
        edtPassWordSI =findViewById(R.id.edtPassWordSI);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnToSignUp = findViewById(R.id.btnToSignUp);
    }
}
