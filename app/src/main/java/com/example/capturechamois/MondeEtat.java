package com.example.capturechamois;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class MondeEtat extends AppCompatActivity {
    private TextView textViewLatitude;
    private TextView textViewLongtitude;
    private TextView textViewTemp;
    private TextView textViewVitesse_vent;
    private TextView textViewDescription_weather;
    private TextView textResult;
    private double latitude;
    private double longtitude;
    private double temp;
    private double vitesse_vent;
    private String description_weather;
    private String urlapi = "http:\\api.openweathermap.org/data/2.5/weather?q=nancy&appid=317cf47ebb1315bcc3d54f83e716bbd8";
    private Button continuer;
    private static int PHOTO=100;


    /**
     * @param savedInstanceState savedInstancestate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monde_etat);
        textViewLatitude=(TextView) findViewById(R.id.latitude);
        textViewLongtitude=(TextView) findViewById(R.id.longtitude);
        textViewTemp=(TextView) findViewById(R.id.temp);
        textViewVitesse_vent=(TextView) findViewById(R.id.vitesse_vent);
        textViewDescription_weather=(TextView) findViewById(R.id.description_weather);
        textResult=(TextView) findViewById(R.id.result);

        //verifier qu'on a la permisson de geolocalisation
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE},48);
        }

        //Service en charge de la geolocalisation
        final LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        //je utilise NETWORK8PROVIDER
        Location l = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        if(l!=null){
            latitude = l.getLatitude();
            textViewLatitude.setText(String.format("%.2f", latitude));
            longtitude = l.getLongitude();
            textViewLongtitude.setText(String.format("%.2f", longtitude));
            Log.i("Location","obtenirGPS");
        }
        else{
            Log.i("Location","pasGPS");
        }
        TacheMeteo tacheMeteo=new TacheMeteo(this);
        tacheMeteo.execute(urlapi);

        //ajouter buff dans le joueur
        int pourcentage=100;
        if(latitude+longtitude>50){
            pourcentage+=20;
        }else{
            pourcentage-=20;
        }

        if (temp<15) {
            pourcentage-=20;
        }else if(temp>25){
            pourcentage-=20;
        }else{
            pourcentage+=20;
        }

        if(vitesse_vent>2||vitesse_vent<5){
            pourcentage+=20;
        }else {
            pourcentage-=20;
        }

        String buff;
        if(pourcentage>100){
            buff="buff, tu as plus de temps pour capturer le chamois +"+(pourcentage-100)+"%";
        }else {
            buff="debuff, tu as moins de temps pour capturer le chamois -"+(100-pourcentage)+"%";
        }
        String result="A cause du Méteo et Position, tu as le "+buff;
        textResult.setText(result);

        //saubegarde le  buff pour la scene prochaine
        try {
            FileOutputStream fos = openFileOutput("pourcentage.txt", MODE_PRIVATE);
            String inputFileContext = String.format("%d", pourcentage);
            fos.write(inputFileContext.getBytes());
            fos.flush();
            fos.close();
            Log.i("MainActivity", "sauvegarde_photo");
        } catch (IOException e) {
            e.printStackTrace();
        }


        //bouton start
        continuer=(Button) findViewById(R.id.Continuer);
        continuer.setOnClickListener((v)->{
            // on passe a outil de photo
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(i.resolveActivity(getPackageManager())!=null){
                startActivityForResult(i, PHOTO);
            }
            Log.i("MainActivity","photo");



            //indice
            Toast.makeText(MondeEtat.this,"Tu as trouvé un endroit bizarre, prends une photo",Toast.LENGTH_LONG).show();
        });

    }


    /**
     * pour afficher le meteo
     */
    private class TacheMeteo extends AsyncTask<String, Void, JSONObject> {
        private final WeakReference<AppCompatActivity> myActivity;  //reference faible

        public TacheMeteo(AppCompatActivity a){
            myActivity = new WeakReference<>(a);
        }

        @Override
        protected void onPreExecute(){
            Log.i("MeteoActivity","onPreExecute");

        }

        protected JSONObject doInBackground(String... urls) {
            Log.i("MeteoActivity","doInBackground");
            URL url = null;
            HttpURLConnection urlConnection = null;
            JSONObject result = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                result = readStream(in);

            }catch ( IOException | JSONException e){
                e.printStackTrace();
            }finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
            return  result;
        }

        private  JSONObject readStream(InputStream is) throws  IOException, JSONException {
            StringBuilder sb =  new StringBuilder();
            BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
            for(String line= r.readLine(); line != null; line = r.readLine()){
                sb.append(line);
            }
            is.close();;
            return  new JSONObject(sb.toString());
        }

        protected void onProgressUpdate(){
            Log.i("MeteoActivity","onProgressUpdate");
        }

        protected void onPostExecute(JSONObject s){
            Log.i("MeteoActivity","onPostExecute");
            try {
                if(s==null){
                    Log.i("Location","pasMeteo");
                }else {
                    // des information meteo
                    temp = s.getJSONObject("main").getDouble("temp") - 273.15;
                    vitesse_vent = s.getJSONObject("wind").getDouble("speed");
                    JSONObject des=(JSONObject) s.getJSONArray("weather").get(0);
                    description_weather=des.getString("description");
                    textViewTemp.setText(String.format("%.2f", temp));
                    textViewDescription_weather.setText(description_weather);
                    textViewVitesse_vent.setText(String.format("%.2f", vitesse_vent));
                    Log.i("Location","obtenirMeteo");
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * @param requestCode request code
     * @param resultCode   resultcode
     * @param data  data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO && resultCode == RESULT_OK) {
            // on prends le photo comme backgound
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.i("MainActivity", "photoActivity");

            //saubegarde le photo
            try {
                FileOutputStream fos = openFileOutput("image.data", MODE_PRIVATE);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                Log.i("MainActivity", "sauvegarde_photo");
            } catch (IOException e) {
                e.printStackTrace();
            }

            // on passe a scene prochaine
            Intent ic=new Intent(MondeEtat.this,CapturePage.class);
            startActivity(ic);
            Log.i("MainActivity","passer_scene_capture");
        }
    }


}
