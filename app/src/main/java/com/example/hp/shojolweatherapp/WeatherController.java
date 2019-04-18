package com.example.hp.shojolweatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class WeatherController extends AppCompatActivity {

    final int REQUEST_CODE= 123;

    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    //App id to use open weather data
    final String APP_ID = "e72ca729af228beabd5d20e3b7749713";
    //Time between location updates (5000 miliseconds or 5 seconds)
    long MIN_TIME = 5000;
    //Distance between location updates (1000m or km)
    float MIN_DISTANCE = 1000;

    //TODO: Set LOCATION_PROVIDER here:
    String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;


    //Member variables:
    TextView cityLabel;
    ImageView weatherImage;
    TextView temperatureLabel;

    //TODO:Declare location manager and location listener here:

    LocationManager locationManager;
    LocationListener locationListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_controller);

        //Linking the elements in the layout to java code
        cityLabel = (TextView) findViewById(R.id.locationTV);
        weatherImage = (ImageView) findViewById(R.id.weatherSymbolIV);
        temperatureLabel = (TextView) findViewById(R.id.tempTV);
        ImageButton changeCityButton = (ImageButton) findViewById(R.id.changeCityButton);


        //TODO: Add an onclickListener to the cahnge city button here:
        changeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherController.this,ChangeCityControlller.class);
                startActivity(intent);
            }
        });

    }

    //TODO:Add onResume() here:
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("shojolweatherapp", "onResume() is called");
        Intent myintent = getIntent();
        String city = myintent.getStringExtra("city");
        if(city!=null) {

            getWeatherForNewCity(city);

        }else{

            getWeatherForCurrentLocatioin();
            Log.d("shojolweatherapp", "Getting weather for current location");
        }





    }


    //TODO:Add gerWeatherForNewCity(String) here:
    private void getWeatherForNewCity(String city){
        RequestParams params = new RequestParams();
        params.put("q",city);
        params.put("appid",APP_ID);
        letsDoSomeNetworking(params);
    }


    //TODO: Add getWeatherForCurrentLocation() here:
    private void getWeatherForCurrentLocatioin() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d("shojolweatherapp", "onLocationChanged() callback recived");
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());
               // Log.v("shojolweatherapp", "IN ON LOCATION CHANGE, lat=" + latitude + ", lon=" + longitude);
                Log.d("shojolweatherapp","latitude is: "+latitude);
                Log.d("shojolweatherapp","longitude is: "+longitude);
                Log.d("shojolweatherapp","shojol");
                RequestParams params = new RequestParams();
                params.put("lat",latitude);
                params.put("lon",longitude);
                params.put("appid",APP_ID);
                letsDoSomeNetworking(params);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Log.d("shojolweatherapp", "onProviderDisabled() callback recived");
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation

            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE);
            return;
        }

       // locationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE){

            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                Log.d("shojolweatherapp","onRequestPermission() recived and permission granted");
                getWeatherForCurrentLocatioin();
            }
        }else{
            Log.d("shojolweatherapp","permission denied");
        }
    }
    //TODO: Add letsDoSomeNetworking(RequestParams params) here:
    private void letsDoSomeNetworking(RequestParams params){

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL, params , new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){

                Log.d("shojolweatherapp","Sucess! JSON: "+response.toString());
               WeatherDataModel weatherData = WeatherDataModel.fromJson(response);
               updateUI(weatherData);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers,Throwable e, JSONObject response){
                Log.e("shojolweatherapp","Fail "+e.toString());
                Log.d("shojolweatherapp","statusCode is: "+statusCode);
                Toast.makeText(WeatherController.this,"Request Failed",Toast.LENGTH_SHORT).show();
            }
        });

    }




    //TODO: Add umdateUI() here:
    private void updateUI(WeatherDataModel weather){
       cityLabel.setText(weather.getCity());
       temperatureLabel.setText((weather.getTemperature()));
       int resourceId = getResources().getIdentifier(weather.getIconName(),"drawable",getPackageName());
       weatherImage.setImageResource(resourceId);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);

        }
    }
}
