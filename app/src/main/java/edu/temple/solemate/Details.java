package edu.temple.solemate;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.view.View;
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
    private TextView header;
    private TextView dets;

    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savetemp);

        image = (ImageView) findViewById(R.id.imageView3);

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        // API Gateway endpoint URL
        final String url = "https://3wpql46dsk.execute-api.us-east-1.amazonaws.com/prod/Recommend_Function/";

        // build request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dets.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // make request to AWS
        queue.add(stringRequest);

        // display image in image view and perform any necessary formatting
        if(!getIntent().getExtras().getBoolean("boolean")){ // picture taken
            File imgFile = (File) getIntent().getExtras().get("picture");
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            image.setImageBitmap(myBitmap);
            image.setRotation(90);
        }
        else { //picture from internal storage
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            Bitmap myBitmap = BitmapFactory.decodeFile((String) getIntent().getExtras().get("picture2"));

            image.setImageBitmap(myBitmap);
        }

        // construct pop-up
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        final View sheetView = this.getLayoutInflater().inflate(R.layout.pop_up_send, null);
        mBottomSheetDialog.setContentView(sheetView);
        dets = (TextView) sheetView.findViewById(R.id.details);

        // label pop-up
        header = (TextView) sheetView.findViewById(R.id.header);
        header.setText("Yeezy Powerphase");

        // display pop-up
        mBottomSheetDialog.show();
        LinearLayout cancel = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_delete);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel code here;
                mBottomSheetDialog.cancel();
            }
        });
    }
}
