package edu.temple.solemate;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by xxnoa_000 on 2/28/2018.
 */

public class Details extends Activity {

    private ImageView image;
    private TextView dets;
    private TextView shoeTitle;
    private ImageView dets_img;

    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savetemp);

        Bitmap myBitmap;
        image = (ImageView) findViewById(R.id.imageView3);

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        // API Gateway endpoint URL
        final String id_url = "https://3wpql46dsk.execute-api.us-east-1.amazonaws.com/prod/identification-function";


        // display image in image view and perform any necessary formatting
        if(!getIntent().getExtras().getBoolean("boolean")){ // picture taken
            File imgFile = (File) getIntent().getExtras().get("picture");
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            image.setImageBitmap(myBitmap);
            image.setRotation(90);
        }
        else { //picture from internal storage
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            myBitmap = BitmapFactory.decodeFile((String) getIntent().getExtras().get("picture2"));

            image.setImageBitmap(myBitmap);
        }

        //bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);

        // build json object
        JSONObject postparams=new JSONObject();
        try {
            postparams.put("img", imageString);
            postparams.put("userID", "test_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                id_url, postparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //System.out.println(response);
                            // display details in pop up
                            shoeTitle.setText(response.getString("shoeTitle"));
                            dets.setText(response.getString("shoePrice"));
                            byte[] decodedString = Base64.decode(response.getString("shoeImage"), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            dets_img.setImageBitmap(decodedByte);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Failure Callback
                    }
                })
        {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        try {
            System.out.println(jsonObjReq.getHeaders());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }


        // construct pop-up
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        final View sheetView = this.getLayoutInflater().inflate(R.layout.pop_up_send, null);
        mBottomSheetDialog.setContentView(sheetView);
        dets = (TextView) sheetView.findViewById(R.id.details);
        dets_img = (ImageView) sheetView.findViewById(R.id.detail_image);


        // label pop-up
        shoeTitle = (TextView) sheetView.findViewById(R.id.header);
        shoeTitle.setText("Yeezy Powerphase");

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

        // make request to AWS
        queue.add(jsonObjReq);
    }
}
