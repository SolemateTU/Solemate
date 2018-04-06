package edu.temple.solemate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xxnoa_000 on 4/1/2018.
 */

public class Description extends Activity {

    private TextView t;
    private ImageView i;

    // for suggestion list fake data
    ListView list;
    String[] web = {
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

    private String fakeimg;
    ////////////////////////


    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desc);
        t = (TextView) findViewById(R.id.tv);
        i = (ImageView) findViewById(R.id.image);

        //ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> desc = new ArrayList<String>();
        ArrayList<String> images = new ArrayList<String>();
        List<String> temp = Arrays.asList(web);
        ArrayList<String> names = new ArrayList<String>(temp);

        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String details = getIntent().getStringExtra("details");
        String img = getIntent().getStringExtra("image");
        System.out.println("IMAGE: "+img);

        fakeimg=img;

        byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        i.setImageBitmap(decodedByte);

        //i.setImageResource(R.drawable.powerphases);
        t.setText(name+" "+details+" "+ price);


        // code for suggestion list
        CustomList adapter = new
                CustomList(Description.this, names, desc, images, false, web, weeb, imageId);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(Description.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(Description.this, Description.class));
                Intent intent= new Intent(Description.this, Description.class);
                intent.putExtra("name", web[position]);
                intent.putExtra("details", weeb[position]);
                intent.putExtra("price", "priceless");
                intent.putExtra("image", fakeimg);
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
}
