package com.example.capturechamois;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.apache.http.util.EncodingUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class CapturePage extends AppCompatActivity {
    private ImageView chamois;
    private ImageView fond;
    private TextView  temps;
    private static int tempsOriginal=10;
    private AtomicReference<Date> date;
    private AtomicReference<Boolean> TaperStart;
    private boolean captureSuccess;
    private int pourcent;
    private int ColorChamois;


    /**
     * @param savedInstanceState saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_page);
        date=new AtomicReference<>(new Date());
        TaperStart= new AtomicReference<>(false);
        captureSuccess=false;
        pourcent=100;
        this.ColorChamois=0;

        try {
            // prend le photo comme fond
            FileInputStream fis = openFileInput("image.data");
            Bitmap bm = BitmapFactory.decodeStream(fis);
            fond=(ImageView) findViewById(R.id.Fond) ;
            fond.setImageBitmap(bm);

            // prend le buff
            FileInputStream pourcentage = openFileInput("pourcentage.txt");
            byte[] buffer = new byte[3];
            pourcentage.read(buffer);
            String str=EncodingUtils.getString(buffer, "UTF-8");
            pourcent = Integer.parseInt(str);
            temps =(TextView) findViewById(R.id.Roulette);
            String indice="Le chamois est apparu, tu as "+tempsOriginal*pourcent/100+"s pour l'attraper\n commences le rattrapage une fois que tu appuies sur button start";
            temps.setText(indice);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //bouton start
        Button start=(Button) findViewById(R.id.start_rattrapage);
        start.setOnClickListener((v)->{
            if(!TaperStart.get()) {
                Calendar calendar = new GregorianCalendar();
                date.set(calendar.getTime());
                TaperStart.set(true);
                //affiche l’heure
                Toast.makeText(CapturePage.this, "Time Actuel : " + date, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(CapturePage.this, "Devez taper bouton Capturer d'abord\nTu ne peut pas taper start dexux fois", Toast.LENGTH_SHORT).show();

            }
        });

        //bouton stop
        Button stop=(Button) findViewById(R.id.Capture);
        stop.setOnClickListener((v)->{
            Calendar calendar = new GregorianCalendar();
            Date time1=date.get();
            if(!TaperStart.get()){
                Toast.makeText(CapturePage.this, "Devez taper start d'abord\nTu ne peut pas taper capture dexux fois", Toast.LENGTH_SHORT).show();
            }
            else {
                TaperStart.set(false);
                Date time2 = calendar.getTime();
                //affiche l’heure
                if((time2.getTime()-time1.getTime())/1000<tempsOriginal*pourcent/100){
                    captureSuccess=true;
                    Toast.makeText(CapturePage.this, "time passé(s) : " + (time2.getTime() - time1.getTime()) / 1000 + "," + (time2.getTime() - time1.getTime()) % 1000+"\n Tu as capturé le chamois", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(CapturePage.this, "time passé(s) : " + (time2.getTime() - time1.getTime()) / 1000 + "," + (time2.getTime() - time1.getTime()) % 1000+"\n Tu as perdu la track du chamois", Toast.LENGTH_SHORT).show();
                }


                // on passe a scene prochaine
                Intent ic=new Intent(CapturePage.this,Captures.class);
                startActivity(ic);
                Log.i("CapturePage","passer_a_la_etape_prochain");
            }
        });

        // choisir le chamois random
        Random random = new Random();
        ColorChamois=random.nextInt(4);
        chamois=(ImageView) findViewById(R.id.Chamois_bleu);
        switch (ColorChamois) {
            case 0:
                chamois=(ImageView) findViewById(R.id.Chamois);
                break;
            case 1:
                chamois=(ImageView) findViewById(R.id.Chamois_bleu);
                break;
            case 2:
                chamois=(ImageView) findViewById(R.id.Chamois_brun);
                break;
            case 3:
                chamois=(ImageView) findViewById(R.id.Chamois_rouge);
                break;
        }
        chamois.setVisibility(View.VISIBLE);
    }
}
