package com.example.capturechamois;

import android.content.Intent;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button start;
    private Button capture;


    /**
     * @param savedInstanceState savedInstancestate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bouton start
        start=(Button) findViewById(R.id.bouton_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on passe a scene prochaine
                Intent ic=new Intent(MainActivity.this,MondeEtat.class);
                startActivity(ic);
                Log.i("MainActivity","passer_a_la_etape_prochain");
            }


        });

        //bouton capture
        capture=(Button) findViewById(R.id.bouton_capture);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on passe a scene prochaine
                Intent ic=new Intent(MainActivity.this,Captures.class);
                startActivity(ic);
                Log.i("MainActivity","passer_a_la_etape_prochain");
            }


        });
    }


    /**
     * @param menu menu
     * @return boolean
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    /**
     * @param item item
     * @return boolean
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_quitter) {
            //quitter
            finish();
            Log.i("Menu", "quitter");
            return true;
        }
        if (itemId == R.id.action_à_propos) {
            //afficher infomation
            Toast.makeText(MainActivity.this,"Auteur : CAI Yunfan Version 0.03 Beta",Toast.LENGTH_SHORT).show();
            Log.i("Menu", "A propos");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
