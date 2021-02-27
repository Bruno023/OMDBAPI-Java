package com.example.omdbapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class InfoPelisActivity extends AppCompatActivity {

    TextView txtDetalles,txtDetalles2;
    ImageView poster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_pelis);

        ObtenerReferencias();

        RecibirDatos();
    }

    private void RecibirDatos() {
        SetearDatos();
    }

    private void ObtenerReferencias() {
        txtDetalles=(TextView) findViewById(R.id.txtInfo);
        txtDetalles2=(TextView) findViewById(R.id.txtInfo2);
        poster=(ImageView) findViewById(R.id.imgPoster);
    }

    private void SetearDatos(String id) {
        TareaAsincronica miTarea=new TareaAsincronica();
        miTarea.setId(id);
        miTarea.execute();
    }

    private class TareaAsincronica extends AsyncTask<Void, Void, String> {
        private String Id;

        public String getId()
        {
            return Id;
        }
        public void setId(String Id)
        {
            this.Id=Id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... parametros) {
            HttpURLConnection miConexion = null;
            URL strAPIUrl;

            BufferedReader responseReader;
            String responseLine, strResultado = "";
            StringBuilder sbResponse;
            try {
                strAPIUrl = new URL("http://www.omdbapi.com/?apikey=eec92515&i="+Id);
                miConexion = (HttpURLConnection) strAPIUrl.openConnection();
                miConexion.setRequestMethod("GET");
                if (miConexion.getResponseCode() == 200) {

                    responseReader = new BufferedReader(new InputStreamReader(miConexion.getInputStream()));
                    sbResponse = new StringBuilder();
                    while ((responseLine = responseReader.readLine()) != null) {
                        sbResponse.append(responseLine);
                    }
                    responseReader.close();
                    strResultado = sbResponse.toString();
                } else {

                }
            } catch (Exception e) {

            } finally {
                if (miConexion != null) {
                    miConexion.disconnect();
                }
            }
            return strResultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            ParseandoElJSON(resultado);
        }
    }

    private void ParseandoElJSON(String resultado) {
        JsonObject objJSONRespuesta;
        String strTitle, strImdbID,strPlot,strLanguage,strCountry,Runtime,Datos,Year;
        String Poster;
        Datos="";


        objJSONRespuesta = JsonParser.parseString(resultado).getAsJsonObject();

        Datos+="Year:" + objJSONRespuesta.get("Year").getAsString() + '\n';
        Datos+="Runtime:" + objJSONRespuesta.get("Runtime").getAsString() + '\n';
        Datos+="Country:" + objJSONRespuesta.get("Country").getAsString() + '\n';
        Datos+="Language:" + objJSONRespuesta.get("Language").getAsString() + '\n';
        Datos+="Plot:" + objJSONRespuesta.get("Plot").getAsString() + '\n';
        strTitle=objJSONRespuesta.get("Title").getAsString();
        Year=objJSONRespuesta.get("Year").getAsString();
        Poster=objJSONRespuesta.get("Poster").getAsString();
        mostrarFoto(Poster);
        txtDetalles.setText(Year+"-"+strTitle);
        txtDetalles2.setText(Datos);


    }

    private void mostrarFoto(String Poster){
        Glide.with(InfoPelisActivity.this)
                .load(Poster)
                .apply(new RequestOptions()
                        .centerCrop()
                        .dontAnimate()
                        .dontTransform().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .thumbnail(.5f)
                .into(poster);
    }
}