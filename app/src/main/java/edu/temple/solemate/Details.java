package edu.temple.solemate;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;


/**
 * Created by xxnoa_000 on 2/28/2018.
 */

public class Details extends Activity {

    private ImageView image;
    private Button recognitionCallButton;
    private TextView sneakerIDTextView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.savetemp);

        image = (ImageView) findViewById(R.id.imageView3);
        recognitionCallButton = (Button) findViewById(R.id.AWSRecogCallButton);
        sneakerIDTextView = (TextView) findViewById(R.id.sneakerIDTextView);

        if (!getIntent().getExtras().getBoolean("boolean")) {
            File imgFile = (File) getIntent().getExtras().get("picture");

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            image.setImageBitmap(myBitmap);
            image.setRotation(90);

            // Instantiate the RequestQueue.
            final RequestQueue queue = Volley.newRequestQueue(this);
            final String url = "https://3wpql46dsk.execute-api.us-east-1.amazonaws.com/prod/Recommend_Function/";

            // listener for RecognitionCallButton
            recognitionCallButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Create request to sent to AWS
                    // Volley docs: https://developer.android.com/training/volley/request.html
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    sneakerIDTextView.setText("Response: " + response.toString());
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // The message was sent in the error response
                                    try {
                                        // convert response byte array to string
                                        String str = new String(error.networkResponse.data, "UTF-8");
                                        sneakerIDTextView.setText(str);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                        sneakerIDTextView.setText("Recommendation Failed :(");
                                    }
                                }
                            });
                    // make request by adding request to request queue
                    queue.add(jsonObjectRequest);
                }
            });


        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            Bitmap myBitmap = BitmapFactory.decodeFile((String) getIntent().getExtras().get("picture2"));

            image.setImageBitmap(myBitmap);

        }


        // }
    }
}
