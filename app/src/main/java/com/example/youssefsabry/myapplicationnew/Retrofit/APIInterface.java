package com.example.youssefsabry.myapplicationnew.Retrofit;

import com.example.youssefsabry.myapplicationnew.DataType.CredCheck;
import com.example.youssefsabry.myapplicationnew.DataType.Images;
import com.example.youssefsabry.myapplicationnew.DataType.MyResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {

    //the base URL for our API
    String BASE_URL = "http://192.168.0.10/ImageUploadApi/";

    //this is our multipart request
    @Multipart
    @POST("Api.php?apicall=upload")
  Call<MyResponse> uploadImage(@Part("image\"; filename=\"myfile.mid\" ") RequestBody file, @Part("desc") RequestBody desc);


    @GET("Api.php?apicall=getallimages")
    Call<Images> doGetImageList();

    @FormUrlEncoded
    @POST("Api.php?apicall=checkCred")
    Call<CredCheck> checkCred(@Field("email") String email, @Field("password") String passowrd);
}
