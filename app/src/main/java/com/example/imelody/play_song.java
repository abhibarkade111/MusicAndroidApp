package com.example.imelody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class play_song extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    TextView textView;
    ImageView play,prev,next;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateSeek;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView = findViewById(R.id.textView);
        play = findViewById(R.id.play);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) (bundle.getParcelableArrayList("songlist"));
        textContent = intent.getStringExtra("currentSong");

        textView.setText(textContent);
        textView.setSelected(true);
        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        Drawable mIcon= ContextCompat.getDrawable(this, R.drawable.pause);
        Drawable mIcon1= ContextCompat.getDrawable(this, R.drawable.play);
        Drawable mIcon2= ContextCompat.getDrawable(this, R.drawable.previous);
        Drawable mIcon3= ContextCompat.getDrawable(this, R.drawable.next);


        mIcon.setColorFilter(ContextCompat.getColor(this, R.color.purple_500), PorterDuff.Mode.MULTIPLY);
        mIcon1.setColorFilter(ContextCompat.getColor(this, R.color.purple_500), PorterDuff.Mode.MULTIPLY);

        mIcon2.setColorFilter(ContextCompat.getColor(this, R.color.purple_500), PorterDuff.Mode.MULTIPLY);
        mIcon3.setColorFilter(ContextCompat.getColor(this, R.color.purple_500), PorterDuff.Mode.MULTIPLY);

        play.setImageDrawable(mIcon);
        prev.setImageDrawable(mIcon2);
        next.setImageDrawable(mIcon3);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateSeek = new Thread(){
            @Override
            public void run() {
               int currentPosition = 0;
               try {
                   while (currentPosition<mediaPlayer.getDuration()){
                       currentPosition = mediaPlayer.getCurrentPosition();
                       seekBar.setProgress(currentPosition);
                       sleep(800);
                   }

               }
               catch (Exception e){
                   e.printStackTrace();
               }
            }
        };
        updateSeek.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    play.setImageDrawable(mIcon1);
                    mediaPlayer.pause();
                }
                else {
                    play.setImageDrawable(mIcon);
                    mediaPlayer.start();
                }

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                seekBar.setProgress(0);

                if(position!=0){
                    position-=1;
                }
                else {
                    position = songs.size()-1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                textContent = songs.get(position).getName().toString();
                textView.setText(textContent);
                play.setImageDrawable(mIcon);


            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                seekBar.setProgress(0);


                if(position!=songs.size()-1){
                    position+=1;
                }
                else {
                    position = 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                textContent = songs.get(position).getName().toString();
                textView.setText(textContent);
                play.setImageDrawable(mIcon);

            }
        });







    }
}