package com.example.notebook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class wordContent_fragment extends AppCompatActivity {
    public static void actionStart(Context context,String wordEn){
        Intent intent=new Intent(context,wordContent_fragment.class);
        intent.putExtra("wordEn",wordEn);
        context.startActivity(intent);
    }
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.word_content);
        Button like=findViewById(R.id.like_button);
        Button dislike=findViewById(R.id.dislisk_button);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RightFragment rightFragment=(RightFragment)getSupportFragmentManager().findFragmentById(R.id.word_content);
                rightFragment.save();
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("tiashi","delete");
                RightFragment rightFragment=(RightFragment)getSupportFragmentManager().findFragmentById(R.id.word_content);
                rightFragment.delete();
            }
        });
        String wordEn=getIntent().getStringExtra("wordEn");
        RightFragment rightFragment=(RightFragment)getSupportFragmentManager().findFragmentById(R.id.word_content);
        rightFragment.refresh(wordEn);
    }
}
