package com.example.retrofit2;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.retrofit2.Retrofit2.APIUtils;
import com.example.retrofit2.Retrofit2.DataClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ImageView imgSignIn;
    EditText edtUserNameSI, edtPassWordSI;
    Button btnSignIn, btnToSignUp;
    String username = "";
    String password = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControl();
        addEvents();
    }

    private void addEvents() {
        // chuyển qua màn hình đăng ký
        btnToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

        // đăng nhập
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtUserNameSI.getText().toString();
                password = edtPassWordSI.getText().toString();

                if(username.length() > 0 && password.length() > 0){
                    DataClient dataClient = APIUtils.getData();
                    Call<List<SinhVien>> call = dataClient.LoginData(username, password);
                    call.enqueue(new Callback<List<SinhVien>>() {
                        @Override
                        public void onResponse(Call<List<SinhVien>> call, Response<List<SinhVien>> response) {
                            ArrayList<SinhVien> sinhvien = (ArrayList<SinhVien>) response.body();
                            if(sinhvien.size() > 0){
                                Intent intent = new Intent(MainActivity.this, Information.class);
                                intent.putExtra("information",sinhvien);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SinhVien>> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Không tồn tại tài khoản",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });



    }

    private void addControl() {
        imgSignIn = findViewById(R.id.imgSignIn);
        edtUserNameSI = findViewById(R.id.edtUserNameSI);
        edtPassWordSI =findViewById(R.id.edtPassWordSI);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnToSignUp = findViewById(R.id.btnToSignUp);
    }
}
