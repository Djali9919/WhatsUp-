package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    public class Download extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try{
                url = new URL(urls[0]);
                urlConnection  = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while(data!=-1){
                    char info = (char)data;
                    result = result + info;
                    data = reader.read();

                }
                return result;


            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String cloud ="",rain = "";

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonObject1 = jsonObject.getJSONObject("clouds");
                if(jsonObject1.getInt("all")<50){
                    cloud = "Sunny";
                }else if(jsonObject1.getInt("all")>50&&jsonObject1.getInt("all")<80){
                    cloud = "Slightly cloudy";
                }else{
                    cloud = "Cloudy";
                }
                    JSONObject object = jsonObject.getJSONObject("main");
                    if(object.getInt("humidity")>80 && object.getInt("humidity")<100){
                        rain = "Light Rain";
                    }else if(object.getInt("humidity")>100){
                        rain = "Heavy Rain";
                    }
                    textView.setText("Temp. Min. :" + object.getInt("temp_min") + "\n" + "Temp. Max. :" +
                            object.getInt("temp_max") + "\n" + cloud + "\n" + rain);
                    Log.i("Temp. Min. :" ,Integer.toString(object.getInt("temp_min")) );
                    Log.i("Temp. Max. :" ,Integer.toString(object.getInt("temp_max")) );





            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void getWeather(View view){
        Download task = new Download();
        String result = "";
        try{
            result = task.execute("https://openweathermap.org/data/2.5/weather?q="+ editText.getText().toString()+"&appid=439d4b804bc8187953eb36d2a8c26a02").get();
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView2);

    }
}
