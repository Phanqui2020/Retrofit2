package com.example.retrofit2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.retrofit2.Retrofit2.APIUtils;
import com.example.retrofit2.Retrofit2.DataClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Information extends AppCompatActivity {

    Button btnDelete, btnUpdate, btnOK, btnCancelInfo;
    EditText edtUserInfo, edtPassInfo;
    ImageView imgInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        addControls();
        addEvents();

    }

    private void addEvents() {
        // load information
        Intent intent = getIntent();
        ArrayList<SinhVien> arrayList = intent.getParcelableArrayListExtra("information");
        if(arrayList != null){
            String id = arrayList.get(0).getId();
            String user = arrayList.get(0).getUsername();
            String pass = arrayList.get(0).getPassword();
            String image = arrayList.get(0).getImage();
            edtUserInfo.setText(user);
            edtPassInfo.setText(pass);
            Picasso.get().load(image).into(imgInfo);

        //delete user
        btnDelete.setOnClickListener(v -> {
            String imagePath = image.substring(image.lastIndexOf("/"));
            DataClient dataClient = APIUtils.getData();
            Call<String> call = dataClient.DeleteData(id, imagePath);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if("Success".equalsIgnoreCase(response.body())){
                        Toast.makeText(Information.this,"xóa thành công",Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(Information.this, MainActivity.class);
                        startActivity(intent1);
                    }else {
                        Toast.makeText(Information.this,"xóa không thành công",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        });

        // update user
        btnUpdate.setOnClickListener(v -> {
            edtUserInfo.setEnabled(true);
            edtPassInfo.setEnabled(true);
            btnUpdate.setVisibility(View.INVISIBLE);
            btnDelete.setVisibility(View.INVISIBLE);
            btnOK.setVisibility(View.VISIBLE);
            btnCancelInfo.setVisibility(View.VISIBLE);
        });

        btnOK.setOnClickListener(v ->{
            String useredit = edtUserInfo.getText().toString();
            String passedit = edtPassInfo.getText().toString();
            if(useredit.length() > 0 && passedit.length() > 0){
                DataClient dataClient = APIUtils.getData();
                Call<SinhVien> call = dataClient.UpdateData(id,useredit, passedit, image);
                call.enqueue(new Callback<SinhVien>() {
                    @Override
                    public void onResponse(Call<SinhVien> call, Response<SinhVien> response) {
                        SinhVien sinhvien =  response.body();
                        if (sinhvien != null) {
                            edtUserInfo.setEnabled(false);
                            edtPassInfo.setEnabled(false);
                            btnUpdate.setVisibility(View.VISIBLE);
                            btnDelete.setVisibility(View.VISIBLE);
                            btnOK.setVisibility(View.INVISIBLE);
                            btnCancelInfo.setVisibility(View.INVISIBLE);
                            edtUserInfo.setText(sinhvien.getUsername());
                            edtPassInfo.setText(sinhvien.getPassword());
                            Picasso.get().load(sinhvien.getImage()).into(imgInfo);
                        }
                    }

                    @Override
                    public void onFailure(Call<SinhVien> call, Throwable t) {
                        Log.d("error", t.getMessage());
                    }
                });
            }else {
                Toast.makeText(Information.this, "user hoặc pass ko được để trống", Toast.LENGTH_LONG).show();
            }
        });

        btnCancelInfo.setOnClickListener(v->{
            edtUserInfo.setEnabled(false);
            edtPassInfo.setEnabled(false);
            btnUpdate.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            btnOK.setVisibility(View.INVISIBLE);
            btnCancelInfo.setVisibility(View.INVISIBLE);
            edtUserInfo.setText(user);
            edtPassInfo.setText(pass);
            Picasso.get().load(image).into(imgInfo);
        });
        }
    }

    private void addControls() {
        btnCancelInfo = findViewById(R.id.btnCancelInfo);
        btnOK = findViewById(R.id.btnOK);
        btnDelete = findViewById(R.id.btnDelete);
        edtUserInfo = findViewById(R.id.edtUserInfo);
        edtPassInfo = findViewById(R.id.edtPassInfo);
        imgInfo = findViewById(R.id.imgInfo);
        btnUpdate = findViewById(R.id.btnUpdate);

    }
}
