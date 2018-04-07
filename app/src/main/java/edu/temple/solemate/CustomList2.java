package edu.temple.solemate;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by xxnoa_000 on 4/6/2018.
 */

public class CustomList2 extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> web;
    private   ArrayList<String> weeb;
    private   String[] weebfake;
    private boolean mode;

    public CustomList2(Activity context,
                      ArrayList<String> web) {
        super(context, R.layout.list_image_only, web);
        this.context = context;
        this.web = web;


    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_image_only, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

            byte[] decodedString = Base64.decode(web.get(position), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);

        return rowView;
    }
}