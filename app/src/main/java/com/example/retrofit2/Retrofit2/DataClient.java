package com.example.retrofit2.Retrofit2;


import com.example.retrofit2.SinhVien;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

//quản lý các request/response cho/từ server
public interface DataClient {
    //gửi 1 file ko phải dạng chuỗi
    @Multipart
    //gửi dữ liệu lên (gửi tới file có thể xử lý)
    @POST("upload.php")
    // tạo function
    // sau khi xử lý dữ liệu server sẽ trả về
    // 1 dũ liệu bất kỳ (dữ liệu kiểu chuỗi)
    // Chọn file gửi hình ảnh lên server @Part
    // Chọn loại dữ liệu gửi lên MultipartBody.Part
    // lắng nghe và gửi dữ liệu lên server
    Call<String> UploadImage(@Part MultipartBody.Part image);

    // gửi dữ liêu dạng chuỗi
    //<

    /**
     * <String> là dữ liệu nhận về từ server
     * @Field dùng để truyền dữ liệu lên cho server
     *
     * @return
     */
    @FormUrlEncoded
    @POST("insert.php")
    Call<String> InsertData(@Field("username") String username
                            ,@Field("password") String password
                            ,@Field("image") String image);


    @FormUrlEncoded
    @POST("login.php")
    Call<List<SinhVien>> LoginData(@Field("username") String username,
                                    @Field("password") String password);

    /***
     * @query nối thêm 1 chuỗi ( khóa, dữ liệu) vào đường dẫn
     *
     * @return
     */
    @GET("delete.php")
    Call<String> DeleteData(@Query("id") String id,
                            @Query("image") String image);

    @GET("update.php")
    Call<SinhVien> UpdateData(@Query("id") String id,
                            @Query("username") String username,
                            @Query("password") String password,
                            @Query("image") String image);


}
