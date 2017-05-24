package com.example.u15190.genius;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onStop(){
        super.onStop();
        Alerta("Parou a aplicação");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Alerta("Reiniciou a aplicação");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Alerta("Janela Destruida....");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnFechar = (Button) findViewById(R.id.btnFechar);
        btnFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnTela2 = (Button) findViewById(R.id.btnJogar);
        btnTela2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(MainActivity.this, JogoActivity.class);
                startActivity(it);
                //finish();

            }
        });

    }

    private void Alerta(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}
