package edu.temple.solemate;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;


/**
 * Created by xxnoa_000 on 2/28/2018.
 */

public class Details extends Activity {

    private ImageView image;
    private Button recognitionCallButton;
    private TextView sneakerIDTextView;


    private TextView header;
    private TextView dets;

    protected void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.savetemp);

        //initiating view objects
        image = (ImageView) findViewById(R.id.imageView3);
        recognitionCallButton = (Button) findViewById(R.id.AWSRecogCallButton);
        sneakerIDTextView = (TextView) findViewById(R.id.sneakerIDTextView);

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://3wpql46dsk.execute-api.us-east-1.amazonaws.com/prod/identification-function";


        final View sheetView = this.getLayoutInflater().inflate(R.layout.pop_up_send, null);
        mBottomSheetDialog.setContentView(sheetView);

        // httpMethod: "POST"
        // body: "sample body"

        // listener for RecognitionCallButton

        dets= (TextView) sheetView.findViewById(R.id.details);


        //place to do post call, and save response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        sneakerIDTextView.setText(response);
                        dets.setText(response);
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sneakerIDTextView.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);
        ///////////////

        recognitionCallButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                sneakerIDTextView.setText(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sneakerIDTextView.setText("That didn't work!");
                    }
                });
                queue.add(stringRequest);
            }
        });

        //picture taken
        if(!getIntent().getExtras().getBoolean("boolean")){
        File imgFile = (File) getIntent().getExtras().get("picture");

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            image.setImageBitmap(myBitmap);
            image.setRotation(90);


        }

        //picture from internal storage
        else{

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            Bitmap myBitmap = BitmapFactory.decodeFile((String) getIntent().getExtras().get("picture2"));


            image.setImageBitmap(myBitmap);
            image.setRotation(90);




        }



        //sample header, can use this code to place in current data for this single instance
        header= (TextView) sheetView.findViewById(R.id.header);
        header.setText("Yeezy Powerphase");




        mBottomSheetDialog.show();
        LinearLayout cancel = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_delete);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel code here;
                mBottomSheetDialog.cancel();
            }
        });
        LinearLayout description = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_edit);
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to description;
                startActivity(new Intent(Details.this, Description.class));
            }
        });
    }
}
