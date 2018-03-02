package edu.temple.solemate;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by xxnoa_000 on 2/28/2018.
 */

public class Details extends Activity {

    private ImageView image;

    protected void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.savetemp);

        image= (ImageView) findViewById(R.id.imageView3);


        if(!getIntent().getExtras().getBoolean("boolean")){
        File imgFile = (File) getIntent().getExtras().get("picture");

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            image.setImageBitmap(myBitmap);
            image.setRotation(90);

        }

        else{

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            Bitmap myBitmap = BitmapFactory.decodeFile((String)getIntent().getExtras().get("picture2"));

            image.setImageBitmap(myBitmap);

        }


       // }
    }
}
