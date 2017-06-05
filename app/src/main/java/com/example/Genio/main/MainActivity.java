package com.example.Genius.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        Button btnFechar = (Button) findViewById(R.id.btnFechar);
        btnFechar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish(); // finalizando
            }
        });

        final Button btnJogarEasy = (Button) findViewById(R.id.btnJogarEasy);
        btnJogarEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                comecaJogo(0);
            }
        });

        final Button btnJogarHard = (Button) findViewById(R.id.btnJogarHard);
        btnJogarHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                comecaJogo(1);
            }
        });

        final ImageView img = (ImageView) findViewById(R.id.imagemGenius);
        img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                btnJogarEasy.setText("Pedro Pezoa");
                btnJogarHard.setText("Daniel Campioni");
            }
        });
    }

    private void comecaJogo(int _ehHard)
    {
        try {
            Intent it = new Intent(MainActivity.this, JogoActivity.class);
            it.putExtra("ehHard", _ehHard + "");
            startActivity(it);
        }
        catch (Exception e)
        {
        }
    }
}
