package com.example.dell.whatstheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    Button whatsTheWeather;
    final String apikey ="&appid=b0e5aeac5338e0cc0063fd89e0fb0633";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (EditText) findViewById(R.id.city_name);
        whatsTheWeather = (Button) findViewById(R.id.find_weather);

        whatsTheWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Value of city name",cityName.getText().toString());

                String nameOfCity = cityName.getText().toString();

                DownloadTask task = new DownloadTask();
                task.execute("http://api.openweathermap.org/data/2.5/weather?q="+nameOfCity+apikey);
            }
        });


    }

    public class DownloadTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;

            HttpURLConnection httpURLConnection;

            try {
                url = new URL(urls[0]);
                httpURLConnection =(HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data!=-1){
                    char currrent = (char) data;
                    result+= currrent;
                    data = inputStreamReader.read();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                JSONArray weatherArray = new JSONArray(jsonObject.getString("weather"));

                for(int i=0;i<weatherArray.length();i++){

                    JSONObject weatherPart = weatherArray.getJSONObject(i);
                    weatherPart.getString("main");
                    Log.i("main report",weatherPart.getString("main"));
                    weatherPart.getString("description");
                    Log.i("description report",weatherPart.getString("description"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            //Log.i("URL",result);
        }
    }
}
