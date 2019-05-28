package com.example.youssefsabry.myapplicationnew;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.youssefsabry.myapplicationnew.DataType.Images;
import com.example.youssefsabry.myapplicationnew.DataType.MyResponse;
import com.example.youssefsabry.myapplicationnew.Retrofit.APIClient;
import com.example.youssefsabry.myapplicationnew.Retrofit.APIInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RunningActivity extends AppCompatActivity {

    Button getDataFromServerButton;
    Button makeMusicButton;
    APIInterface apiInterface;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        Intent getLAstIntentData=getIntent();
        String dataLast=getLAstIntentData.getStringExtra("email");

        final String useremail = dataLast;

        apiInterface = APIClient.getClient().create(APIInterface.class);


        makeMusicButton=(Button) findViewById(R.id.MakeMusic);
        getDataFromServerButton=(Button)findViewById(R.id.getDataButton);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        getDataFromServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                Call<Images> call = api.doGetImageList();
                call.enqueue(new Callback<Images>() {
                    @Override
                    public void onResponse(Call<Images> call, Response<Images> response) {
                        if(response.code()==200) {
                            Toast.makeText(getApplicationContext(), "Files Retrieved...", Toast.LENGTH_LONG).show();

                            String displayResponse = "";

                            Images resource = response.body();

                            List<Images.Datum> datumList = resource.images;
                            for (Images.Datum datum : datumList) {
                                displayResponse += datum.id + " " + datum.url + "\n";
                                final Button bt=new Button(RunningActivity.this);
                                bt.setText(datum.url);
                                bt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getApplicationContext(), bt.getText(), Toast.LENGTH_LONG).show();

                                    }
                                });


                                mAdapter = new MyAdapter(datumList,RunningActivity.this);
                                recyclerView.setAdapter(mAdapter);

                            }

                        }else{
                            Toast.makeText(getApplicationContext(), "Error in Retrieving...", Toast.LENGTH_LONG).show();


                        }
                    }

                    @Override
                    public void onFailure(Call<Images> call, Throwable t) {

                    }
                });
            }
        });


        makeMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goMakeMusicActivity=new Intent(RunningActivity.this,CreationActivity.class);
                goMakeMusicActivity.putExtra("email",useremail);

                startActivity(goMakeMusicActivity);
            }
        });







//
//    public void sendNotification() {
//
//        //Get an instance of NotificationManager//
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.ic_launcher_background)
//                        .setContentTitle("Midi Sound Pro")
//                        .setContentText("File Uploaded Succesfully ");
//
//
//        // Gets an instance of the NotificationManager service//
//
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                mNotificationManager.notify(001, mBuilder.build());
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
//            //the image URI
//            Uri selectedImage = data.getData();
//
//            Log.v(TAG,"selectedImage: "+selectedImage.getPath().toString());
//            uploadFile(selectedImage,"New File ");
//        }
//    }



}
}


