package com.example.omdbapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText edtBusqueda;
    Button btnBuscar;
    ListView List;
    private ArrayList<String> Peliculas=new ArrayList<String>();
    private ArrayList<Pelicula> Peliculas2=new ArrayList<Pelicula>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ObtenerReferencias();

        SetearListeners();
    }

    private void SetearListeners() {
        btnBuscar.setOnClickListener(Click);

    }

    private void ObtenerReferencias() {
        edtBusqueda = (EditText) findViewById(R.id.edtBusqueda);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        List=(ListView) findViewById(R.id.ListView);
    }

    private void Buscar(String strTexto) {
        TareaAsincronica miTarea=new TareaAsincronica();
        miTarea.setTexto(strTexto);
        miTarea.execute();
    }

    View.OnClickListener Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String Texto = edtBusqueda.getText().toString();
            Buscar(Texto);
        }
    };

    private class TareaAsincronica extends AsyncTask<Void, Void, String> {
        private String Texto;

        public String getTexto()
        {
            return Texto;
        }
        public void setTexto(String Texto)
        {
            this.Texto=Texto;
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
                strAPIUrl = new URL("http://www.omdbapi.com/?apikey=your_api_key&s="+Texto);
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
        JsonObject objJSONRespuesta, objJSONPelicula;
        JsonArray searchJSONArray;
        String strTitle, imbdid,Year;
        Peliculas.clear();
        objJSONRespuesta = JsonParser.parseString(resultado).getAsJsonObject();
        searchJSONArray = objJSONRespuesta.getAsJsonArray("Search");

        for (int i = 0; i < searchJSONArray.size(); i++) {
            objJSONPelicula = searchJSONArray.get(i).getAsJsonObject();
            strTitle = objJSONPelicula.get("Title").getAsString();
            imbdid = objJSONPelicula.get("imdbID").getAsString();
            Year = objJSONPelicula.get("Year").getAsString();

            Peliculas.add(Year+"-"+strTitle);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Peliculas);
            List.setAdapter(adapter);


        }
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Toast.makeText(MainActivity.this, "Has pulsado: "+ Peliculas.get(position), Toast.LENGTH_LONG).show();
                IniciarActivity();

            }
        });
    }

    private void IniciarActivity() {
        Intent miIntent=new Intent(this, InfoPelisActivity.class);
        startActivity(miIntent);
    }

}
