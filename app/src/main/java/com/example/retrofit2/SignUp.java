package com.example.retrofit2;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.retrofit2.Retrofit2.APIUtils;
import com.example.retrofit2.Retrofit2.DataClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    ImageView imgSignUp;
    EditText edtUserNameSU, edtPassWordSU;
    Button btnSignUp, btnCancel;
    int RequestCodeImage =123;
    String Path ="";
    String username = "";
    String password = "";
    String image ="";
    private static final int PERMISSION_REQUEST_CODE = 1;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        addControl();
        addEvents();

        //request storage permission
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        requestPermissions(permissions,PERMISSION_REQUEST_CODE);
    }

    private void addControl() {
        imgSignUp = findViewById(R.id.imgsignUp);
        edtUserNameSU = findViewById(R.id.edtUserNameSU);
        edtPassWordSU =findViewById(R.id.edtPassWordSU);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void addEvents() {

        //lựa chọn hình ảnh từ máy
        imgSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,RequestCodeImage);
            }
        });

        // đưa thông tin đăng ký và file lên server
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtUserNameSU.getText().toString();
                password = edtPassWordSU.getText().toString();
                if(username.length()> 0 && password.length() > 0){
                    File file = new File(Path);
                    String filePath = file.getAbsolutePath();

                    // upload hình trùng tên nhau
                    String[] listFile = filePath.split("\\.");
                    filePath = listFile[0] + System.currentTimeMillis() + "." + listFile[1];

                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file",filePath, requestBody);

                    //tạo kết nối gửi lên và nhận dữ liệu về
                    DataClient dataClient = APIUtils.getData();
                    Call<String> call = dataClient.UploadImage(body);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.body().length() > 0){
                                DataClient insertData = APIUtils.getData();
                                Call<String> insert = insertData.InsertData(username,password,APIUtils.baseUrl + "image/" + response.body());
                                insert.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        Log.d("BBB", response.body());
                                        if(response.body().equals("Success")){
                                            Toast.makeText(SignUp.this,"thêm thành công",Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.d("BBBBB", t.getMessage());

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d( "BBBB: ", t.getMessage());
                            Log.d(String.valueOf(t.fillInStackTrace()), "onFailure: ");

                        }
                    });
                }else {
                    Toast.makeText(SignUp.this, "Xin mời nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // lựa chọn hình ảnh từ máy
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == RequestCodeImage && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            Path = getRealPathFromURI(uri);

            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgSignUp.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    // lấy đường dẫn thực tế của hình ảnh trong máy để đưa lên server
    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
}