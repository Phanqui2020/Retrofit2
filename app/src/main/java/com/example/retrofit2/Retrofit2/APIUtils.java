package com.example.retrofit2.Retrofit2;

import retrofit2.Retrofit;

//cung cấp đường dẫn
public class APIUtils {
    public static final String baseUrl = "http://192.168.1.17/quanlysinhvien/";

    // gửi và nhận dữ liệu về được chứa trong dataclient
    public static DataClient getData(){
        return RetrofitClient.getClient(baseUrl).create(DataClient.class);
    }
}
