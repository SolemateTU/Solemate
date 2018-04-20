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
    private final ArrayList<String> shoeIDs;
    private ArrayList<String> imgStrings;
    private final ArrayList<String> shoeDescriptions;

    public CustomList(Activity context,
                      ArrayList<String> shoeIDs,   ArrayList<String> shoeDescriptions,   ArrayList<String> imgStrings) {
        super(context, R.layout.list_single, shoeIDs);
        this.context = context;

        this.shoeIDs = shoeIDs;
        this.shoeDescriptions = shoeDescriptions;
        this.imgStrings = imgStrings;


    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView txtDetails = (TextView) rowView.findViewById(R.id.txt2);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

        if (shoeIDs.size() != 0) {

            txtTitle.setText(shoeIDs.get(position));
            txtDetails.setText(shoeDescriptions.get(position));

            byte[] decodedString = Base64.decode(imgStrings.get(position), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        }

        return rowView;
    }

    @Override
    public int getCount() {
        return shoeIDs!=null ? shoeIDs.size() : 0;
    }
}