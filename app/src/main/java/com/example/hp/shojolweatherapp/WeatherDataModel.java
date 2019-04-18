package com.example.hp.shojolweatherapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.PriorityQueue;

/**
 * Created by hp on 8/1/2018.
 */

public class WeatherDataModel {
    // Member variables that hold our relevant weather inforomation.
    private String Temperature;
    private String City;
    private String iconName;
    private int condition;





    // Create a WeatherDataModel from a JSON.
    // We will call this instead of the standard constructor.
    public static WeatherDataModel fromJson(JSONObject jsonObject){

        WeatherDataModel weatherData = new WeatherDataModel();
        try {
            weatherData.City = jsonObject.getString("name");
            weatherData.condition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherData.iconName = updateWeatherIcon(weatherData.condition);
            double tempResult = jsonObject.getJSONObject("main").getDouble("temp")-273.5;
            int roundedValue =(int) Math.rint(tempResult);
            weatherData.Temperature = Integer.toString(roundedValue);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return weatherData;

    }

    // Get the weather image name from OpenWeatherMap's condition (marked by a number code)
    private static String updateWeatherIcon(int condition) {

        if (condition >= 0 && condition < 300) {
                return "tstorm1";
} else if (condition >= 300 && condition < 500) {
        return "light_rain";
        } else if (condition >= 500 && condition < 600) {
        return "shower3";
        } else if (condition >= 600 && condition <= 700) {
        return "snow4";
        } else if (condition >= 701 && condition <= 771) {
        return "fog";
        } else if (condition >= 772 && condition < 800) {
        return "tstorm3";
        } else if (condition == 800) {
        return "sunny";
        } else if (condition >= 801 && condition <= 804) {
        return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
        return "tstorm3";
        } else if (condition == 903) {
        return "snow5";
        } else if (condition == 904) {
        return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
        return "tstorm3";
        }

        return "dunno";
        }

    // Getter methods for temperature, city, and icon name:
    public String getTemperature() {
        return Temperature + "°";
    }

    public String getCity() {
        return City;
    }

    public String getIconName() {
        return iconName;
    }
}
