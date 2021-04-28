package com.example.newerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView country,temperature,countryName,temperatureF,time,countryZone,currentWeather,windD,windS,windDegree,humidity,forecastWeather,forecastD;
    TextView tempTommorowC,tempTommorrowF,humidityT,Tphases,Tsunrise,Tsunset,Tmoonrise,Tmoonset,TchanceRain,TchanceSnow;
    EditText search;
    ImageView button,setImage;
    String Countries,tDate,tWeather,tTempC,tTempF,tHumidity;
    String phases,sunriseT,sunsetT,moonriseT,moonsetT,TwillRain,Twillsnow;
    Uri myuri;
    double lat,lng;
    LocationManager locationManager;
    String latitude, longitude;
    private static final int REQUEST_LOCATION = 1;

    Geocoder currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        country = findViewById(R.id.muhCountry);
        countryName = findViewById(R.id.countryName);
        temperature = findViewById(R.id.muhtemperature);
        search = findViewById(R.id.searchCountry);
        button = findViewById(R.id.muhButtons);
        temperatureF = findViewById(R.id.temperatureF);
        time = findViewById(R.id.time);
        countryZone = findViewById(R.id.countryZone);
        currentWeather = findViewById(R.id.currentWeather);
        windD = findViewById(R.id.windDirection);
        windS = findViewById(R.id.windSpeed);
        windDegree = findViewById(R.id.windDegree);
        humidity = findViewById(R.id.currenthumidity);
        //
        forecastWeather = findViewById(R.id.forecastWeather);
        tempTommorowC = findViewById(R.id.tempCtomorrow);
        tempTommorrowF = findViewById(R.id.temFtommorrow);
        humidityT = findViewById(R.id.humidityT);
        //
        Tphases = findViewById(R.id.moonPhase);
        Tsunrise = findViewById(R.id.sunrise);
        Tsunset = findViewById(R.id.sunset);
        Tmoonrise = findViewById(R.id.moonrise);
        Tmoonset = findViewById(R.id.moonset);
        TchanceRain = findViewById(R.id.chancesOfRain);
        TchanceSnow = findViewById(R.id.chancesOfsnow);
        getLocation();
        fetchdata(Countries);
        countryName.setText(Countries);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Countries = search.getText().toString();
                toUpperCaseLetter(Countries);
                countryName.setText(Countries);
                fetchdata(Countries);

            }
        });




    }
    public void toUpperCaseLetter(String countries){
        String Cap = countries.substring(0,1);
        String rem = countries.substring(1,countries.length());
        String First = Cap.toUpperCase();
        Countries = First+rem;
    }
    public void getLocation(){
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(provider);
            if (locationGPS != null) {
                 lat = locationGPS.getLatitude();
                 lng = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(lng);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }

        }
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                Countries = addresses.get(0).getLocality();
            }
        }catch(IOException ex) {
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
        }

    }
    private void fetchdata(String countries)
    {

        // Create a String request
        // using Volley Library
        final String url = "https://api.weatherapi.com/v1/forecast.json?key=20cc9a9b0a4243b4be970612211704&q="+countries+"&days=1&aqi=no&alerts=no";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {

                        // Handle the JSON object and
                        // handle it inside try and catch
                        try {

                            // Creating object of JSONObject
                            JSONObject jsonObject = new JSONObject(response);
                            country.setText("Region: "+jsonObject.getJSONObject("location").getString("region"));
                            currentWeather.setText("Currently "+jsonObject.getJSONObject("current").getJSONObject("condition").getString("text"));
                            humidity.setText("Current Humidity: "+jsonObject.getJSONObject("current").getString("humidity"));
                            temperature.setText("Current°C: "+jsonObject.getJSONObject("current").getString("temp_c"));
                            temperatureF.setText("Current°F: "+jsonObject.getJSONObject("current").getString("temp_f"));
                            time.setText("Current Time: "+jsonObject.getJSONObject("location").getString("localtime"));
                            countryZone.setText("Current Zone: "+jsonObject.getJSONObject("location").getString("tz_id"));
                            windD.setText("Direction: "+jsonObject.getJSONObject("current").getString("wind_dir"));
                            windS.setText("Speed: "+jsonObject.getJSONObject("current").getString("wind_kph")+" Kph");
                            windDegree.setText("Degree: "+jsonObject.getJSONObject("current").getString("wind_degree")+" °");

                            JSONArray jsonArray = jsonObject.getJSONObject("forecast").getJSONArray("forecastday");
                            for(int i = 0;i<jsonArray.length();i++){
                                jsonObject = jsonArray.getJSONObject(i);
                                tWeather = jsonObject.getJSONObject("day").getJSONObject("condition").getString("text");
                                tDate = jsonObject.getString("date");
                                tTempC = jsonObject.getJSONObject("day").getString("avgtemp_c");
                                tTempF = jsonObject.getJSONObject("day").getString("avgtemp_f");
                                tHumidity = jsonObject.getJSONObject("day").getString("avghumidity");

                                phases = jsonObject.getJSONObject("astro").getString("moon_phase");
                                sunriseT = jsonObject.getJSONObject("astro").getString("sunrise");
                                sunsetT = jsonObject.getJSONObject("astro").getString("sunset");
                                moonriseT = jsonObject.getJSONObject("astro").getString("moonrise");
                                moonsetT = jsonObject.getJSONObject("astro").getString("moonset");
                                TwillRain = jsonObject.getJSONObject("day").getString("daily_chance_of_rain");
                                Twillsnow = jsonObject.getJSONObject("day").getString("daily_chance_of_snow");

                            }
                            forecastWeather.setText(tWeather+" later");
                            tempTommorrowF.setText("Avg daily °F: "+tTempF);
                            tempTommorowC.setText("Avg daily °C: "+tTempC);
                            TchanceRain.setText("Chances of Rain "+TwillRain+" %");
                            TchanceSnow.setText("Chances of Snow "+Twillsnow+" %");
                            humidityT.setText("Humidity: "+tHumidity);
                            //myuri = Uri.parse(uriS);
                            Tphases.setText("Moon Phases "+phases);
                            Tsunrise.setText("Sunsrise: "+sunriseT);
                            Tsunset.setText("Sunset: "+sunsetT);
                            Tmoonrise.setText("moonrise: "+moonriseT);
                            Tmoonset.setText("moonset: "+moonsetT);


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(
                                MainActivity.this,
                                error.getMessage(),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
        requestQueue.getCache().clear();
    }
}

