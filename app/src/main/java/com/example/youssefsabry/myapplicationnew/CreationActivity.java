package com.example.youssefsabry.myapplicationnew;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.youssefsabry.myapplicationnew.DataType.MyResponse;
import com.example.youssefsabry.myapplicationnew.Retrofit.APIClient;
import com.example.youssefsabry.myapplicationnew.Retrofit.APIInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreationActivity extends AppCompatActivity {

    APIInterface apiInterface;
    Button guitarButton;
    Button pianoButton;
    Button saxButton;
    Button bassButton;
    Button uploadButton;
    Button saveButton;
    ArrayList<Integer> list;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);
        Intent getLAstIntentData = getIntent();

        String email=getLAstIntentData.getStringExtra("sameh");


        apiInterface = APIClient.getClient().create(APIInterface.class);
        guitarButton=(Button) findViewById(R.id.guitar);
        pianoButton=(Button) findViewById(R.id.piano);
        saxButton=(Button) findViewById(R.id.brass);
        bassButton=(Button) findViewById(R.id.bass);
        uploadButton=(Button) findViewById(R.id.UploadMusic);

        list = new ArrayList<Integer>();

        guitarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add(3);
            }
        });

        pianoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add(0);
            }
        });

        saxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add(6);
            }
        });

        bassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add(24);
            }
        });


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMidi();
            }
        });

    }


    private void uploadMidi() {

        // 1. Create some MidiTracks
        MidiTrack tempoTrack = new MidiTrack();
        MidiTrack noteTrackP = new MidiTrack();
        MidiTrack noteTrackG = new MidiTrack();
        MidiTrack noteTrackS = new MidiTrack();
        MidiTrack noteTrackB = new MidiTrack();

// 2. Add events to the tracks
// Track 0 is the tempo map
        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);

        Tempo tempo = new Tempo();
        tempo. setBpm(400);

        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(tempo);

// Track 1 will have some notes in it
        for(int i = 0; i<list.size(); i++)
        {
            int pitch = 40;
            int velocity = 100;
            long tick = i * 480;
            long duration = 120;

            
            if(list.get(i)==0)
                noteTrackP.insertNote(0, pitch, velocity, tick, duration);

            if(list.get(i)==3)
                noteTrackG.insertNote(3, pitch, velocity, tick, duration);

            if(list.get(i)==6)
                noteTrackS.insertNote(6, pitch, velocity, tick, duration);

            if(list.get(i)==24)
                noteTrackB.insertNote(24, pitch, velocity, tick, duration);
        }

// 3. Create a MidiFile with the tracks we created
        List<MidiTrack> tracks = new ArrayList<MidiTrack>();
        tracks.add(tempoTrack);
        tracks.add(noteTrackP);
        tracks.add(noteTrackG);
        tracks.add(noteTrackS);
        tracks.add(noteTrackB);

        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);

// 4. Write the MIDI data to a file
        File output = new File(Environment.getExternalStorageDirectory().getPath()+"/output.mid");
        try
        {
            midi.writeToFile(output);
            uploadFile(Environment.getExternalStorageDirectory().getPath()+"/output.mid","NewFile");
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }

    private void uploadFile(String fileUri, String desc) {

        //creating a file
        File file = new File(fileUri);

        //creating request body for file
        RequestBody requestFile = RequestBody.create(MediaType.parse("mid"), file);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);

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

        //creating a call and calling the upload image method
        Call<MyResponse> call = api.uploadImage(requestFile, descBody);

        //finally performing the call
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (!response.body().equals("error")) {
                    Toast.makeText(getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}

