package com.example.Genio.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnFechar = (Button) findViewById(R.id.btnFechar);
        btnFechar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        Button btnJogarEasy = (Button) findViewById(R.id.btnJogarEasy);
        btnJogarEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                comecaJogo(0);
            }
        });

        Button btnJogarHard = (Button) findViewById(R.id.btnJogarHard);
        btnJogarHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                comecaJogo(1);
            }
        });
    }

    private void comecaJogo(int _ehHard)
    {
        Intent it = new Intent(MainActivity.this, JogoActivity.class);
        it.putExtra("ehHard", _ehHard+"");
        startActivity(it);
    }
}
