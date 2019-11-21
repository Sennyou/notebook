package com.example.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.math.*;
import org.litepal.crud.LitePalSupport;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.security.MessageDigest;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //IntentFilter intentFilter;
    //NetworkChange networkChange;

    protected void onStart(){
        super.onStart();
        int or;
        or=getResources().getConfiguration().orientation;
        if(or==1) {
            LeftFragment leftFragment = (LeftFragment) getSupportFragmentManager().findFragmentById(R.id.wordrec);
            leftFragment.refresh();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int or;
        or=getResources().getConfiguration().orientation;
        if(or==1){
           setContentView(R.layout.word_rec);

        }
        else {
            setContentView(R.layout.activity_main);
            Button like=findViewById(R.id.like_button);
            Button dislike=findViewById(R.id.dislisk_button);
            Button play=findViewById(R.id.fayin);
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RightFragment rightFragment=(RightFragment)getSupportFragmentManager().findFragmentById(R.id.right);
                    rightFragment.save();
                }
            });
            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("tiashi","delete");
                    RightFragment rightFragment=(RightFragment)getSupportFragmentManager().findFragmentById(R.id.right);
                    rightFragment.delete();
                }
            });
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RightFragment rightFragment=(RightFragment)getSupportFragmentManager().findFragmentById(R.id.right);
                    rightFragment.play();
                }
            });
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }

       // LitePal.getDatabase();
       // word word=new word();
       // word.setEn("ball");
       // word.setZh("球");
      //  word.save();

        //textView = findViewById(R.id.text);

      // send();
    }


    @Override
    public void onClick(View view) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("HELP");
        dialog.setMessage("这真的很有帮助！");
        dialog.setCancelable(false);
        dialog.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
        return true;
    }
}
