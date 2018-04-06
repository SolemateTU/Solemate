package edu.temple.solemate;

/**
 * Created by xxnoa_000 on 4/1/2018.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final   ArrayList<String> web;
    private   ArrayList<String> weeb;
    private final   ArrayList<String> imageId;
    private final String[] webfake;
    private   String[] weebfake;
    private  final Integer[] imageIdfake;
    private boolean mode;

    public CustomList(Activity context,
                      ArrayList<String> web,   ArrayList<String> weeb,   ArrayList<String> imageId, boolean real, String[] fakename, String[] fakedesc, Integer[] fakeimage) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;
        this.weeb = weeb;
        this.imageId = imageId;

        this.webfake= fakename;
        this.weebfake= fakedesc;
        this.imageIdfake= fakeimage;

        mode= real;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView txtDetails = (TextView) rowView.findViewById(R.id.txt2);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

        if (mode) {
            txtTitle.setText(web.get(position));
            txtDetails.setText(weeb.get(position));

            byte[] decodedString = Base64.decode(imageId.get(position), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        }
        else {
            txtTitle.setText(webfake[position]);
            txtDetails.setText(weebfake[position]);
            imageView.setImageResource(imageIdfake[position]);
        }
        return rowView;
    }
}