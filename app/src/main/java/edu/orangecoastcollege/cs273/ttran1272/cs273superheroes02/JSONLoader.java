package edu.orangecoastcollege.cs273.ttran1272.cs273superheroes02;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ttran1272 on 10/24/2017.
 */

public class JSONLoader {
    /**
     *  Loads JSON data from a file in the assets directory
     *  @param context The activity from which the data is loaded
     *  @throws java.io.IOException If there is an error reading from the JSON file
     */

    public static List<Superhero> loadJSONFromAsset(Context context) throws IOException {

        List<Superhero> allSuperheroesList = new ArrayList<>();
        InputStream is = context.getAssets().open("cs273superheroes.json");
        int size = is.available();

        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        String json = new String(buffer, "UTF-8");

        try {
            JSONObject jsonRootObject = new JSONObject(json);
            JSONArray allSuperheroesJSON = jsonRootObject.getJSONArray("cs273superheroes");

            int length = allSuperheroesJSON.length();

            // Loop through all the superheros in the JSON data, create a Superhero object for each
            // and add the object to the allSuperheroesList
            for (int i = 0; i < length; i++){
                JSONObject superheroObject = allSuperheroesJSON.getJSONObject(i);

                String userName = superheroObject.getString("Username");
                String name = superheroObject.getString("Name");
                String superPower = superheroObject.getString("Superpower");
                String oneThing = superheroObject.getString("OneThing");

                Superhero superhero = new Superhero(userName, name, superPower, oneThing);

                allSuperheroesList.add(superhero);

            }


        } catch (JSONException e){
            Log.e("CS273 Superheroes", e.getMessage());
        }

        return allSuperheroesList;
    }
}
