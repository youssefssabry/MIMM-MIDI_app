package com.example.youssefsabry.myapplicationnew;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.youssefsabry.myapplicationnew.DataType.CredCheck;
import com.example.youssefsabry.myapplicationnew.DataType.Images;
import com.example.youssefsabry.myapplicationnew.DataType.MyResponse;
import com.example.youssefsabry.myapplicationnew.Retrofit.APIInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button loginButton;
    EditText EmailField, PasswordField;
    String EmailNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton=(Button)findViewById(R.id.submitButton);
        EmailField=(EditText)findViewById(R.id.email);
        PasswordField=(EditText)findViewById(R.id.password);

        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitButton:
                makeLogin();
                break;

                default:
                    break;

        }
    }

    private void makeLogin() {
        String EmailNew=EmailField.getText().toString();
        String Password=PasswordField.getText().toString();

        //The gson builder
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //creating our api
        APIInterface api = retrofit.create(APIInterface.class);
        Call<CredCheck> call = api.checkCred(EmailNew,Password);
        call.enqueue(new Callback<CredCheck>() {
            @Override
            public void onResponse(Call<CredCheck> call, Response<CredCheck> response) {

                if(response.code()==200) {

                    CredCheck resource = response.body();

                    if(resource.message){
                        EnterTheProgram();
                    }else{

                        Toast.makeText(MainActivity.this,"You are not Allowed to login",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"Error in Connection",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CredCheck> call, Throwable t) {

                Toast.makeText(MainActivity.this,"Error in Connection",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void EnterTheProgram() {

        Intent openNewActivity=new Intent(MainActivity.this,RunningActivity.class);
        openNewActivity.putExtra("wael",EmailNew);
        startActivity(openNewActivity);
    }
}
