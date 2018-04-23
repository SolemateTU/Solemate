package edu.temple.solemate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by xxnoa_000 on 2/28/2018.
 */

public class SavedList extends Activity {


    JSONArray jarray;
    ListView list;


    protected void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_list);

        if (readFromFile().equals("")){
            jarray = new JSONArray();
            System.out.println("Saved List: There's nothing here.");
        }
        else{
            try {
                jarray = new JSONArray(readFromFile());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //String[] names, desc, price;

        final ArrayList<String> names = new ArrayList<String>();
        final ArrayList<String> desc = new ArrayList<String>();
        final ArrayList<String> images = new ArrayList<String>();
        final ArrayList<String> prices = new ArrayList<String>();
        JSONObject temp;

        for (int i = 0; i < jarray.length(); i++){

            temp= jarray.optJSONObject(i);

            names.add(temp.optString("shoeTitle"));
            desc.add(temp.optString("shoeDescription"));
            images.add(temp.optString("shoeImage"));
            prices.add(temp.optString("lowestPrice"));

        }

        // TEST DATA
        final String[] web = {
                "Nike",
                "Addidas",
                "New Balance",
                "Under Armor",
                "Puma",
                "IDK",
                "Crocs"
        } ;
        String[] weeb = {
                "This is a Nike shoe something something something",
                "This is an Addidas shoe something something something",
                "This is a New Balance shoe something something something",
                "This is a Under Armor shoe something something something",
                "This is a Puma shoe something something something",
                "IDK any other shoe types please help",
                "Crocs what are those what are those what are those"
        } ;
        Integer[] imageId = {
                R.drawable.closeicon,
                R.drawable.powerphases,
                R.drawable.powerphases,
                R.drawable.powerphases,
                R.drawable.powerphases,
                R.drawable.powerphases,
                R.drawable.powerphases

        };
        ////////////////////////

        CustomList adapter = new
                CustomList(SavedList.this, names, prices, desc, images);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(SavedList.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(SavedList.this, Description.class);
                intent.putExtra("name", names.get(position));
                intent.putExtra("details", desc.get(position));
                intent.putExtra("price", prices.get(position));
                intent.putExtra("image", images.get(position));

                System.out.println("Name: "+names.get(position));
                System.out.println("Details: "+desc.get(position));
                System.out.println("Price: "+prices.get(position));
                System.out.println("Desc: "+images.get(position));
                startActivity(intent);

            }
        });

    }





    public String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = getApplicationContext().openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        System.out.println("DATA BEING READ: "+ret);
        return ret;
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(SavedList.this, DetectorActivity.class);
        startActivity(startMain);
    }
}