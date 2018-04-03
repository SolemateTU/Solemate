package edu.temple.solemate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
            "\nThis is a Nike shoe something something something",
            "\nThis is an Addidas shoe something something something",
            "\nThis is a New Balance shoe something something something",
            "\nThis is a Under Armor shoe something something something",
            "\nThis is a Puma shoe something something something",
            "\nIDK any other shoe types please help",
            "\nCrocs what are those what are those what are those"
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


    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desc);
        t = (TextView) findViewById(R.id.tv);
        i = (ImageView) findViewById(R.id.image);

        i.setImageResource(R.drawable.powerphases);
        t.setText("grab saved data for details here");


        // code for suggestion list
        CustomList adapter = new
                CustomList(Description.this, web, weeb, imageId);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(Description.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Description.this, Description.class));

            }
        });



    }
}
