package edu.temple.solemate;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by xxnoa_000 on 2/28/2018.
 */

public class Details extends Activity {

    private ImageView image;
    private ImageView dets_img;
    private TextView dets;
    private TextView shoeTitle;

    // urls
    final String url = "https://s17.postimg.cc/w2zg7k1u7/Solemate.gif";
    final String details_url = "https://3wpql46dsk.execute-api.us-east-1.amazonaws.com/prod/identification-function";
    final String identification_url = "https://1hoad2m4ka.execute-api.us-east-1.amazonaws.com/dev2";

    private RequestQueue queue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savetemp);

        Bitmap myBitmap = null;

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        final View sheetView = this.getLayoutInflater().inflate(R.layout.pop_up_send, null);
        mBottomSheetDialog.setContentView(sheetView);

        //initiating view objects
        image = (ImageView) findViewById(R.id.imageView3);
        dets = (TextView) sheetView.findViewById(R.id.details);
        dets_img = (ImageView) sheetView.findViewById(R.id.detail_image);
        shoeTitle = (TextView) sheetView.findViewById(R.id.header);

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        final Intent intent = new Intent(Details.this, Description.class);
        final boolean[] gtg = {false};

        // get bitmap and encode as base64
        myBitmap = display_image(myBitmap);
        String imageString = bitmapToBase64(myBitmap);

        // build request body
        JSONObject postParams = new JSONObject();
        try {
            postParams.put("img", imageString);
            postParams.put("userID", "test_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // build identification model request
        JsonObjectRequest identificationRequest = buildIdentificationRequest(intent, gtg, postParams, imageString);

        // sample header for pop-up view
        // can use this code to place in current data for this single instance
        shoeTitle.setText("Loading");
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(dets_img);
        Glide.with(this).load(url).into(imageViewTarget);

        // register cancel and display callbacks
        LinearLayout cancel = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_delete);
        LinearLayout description = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_edit);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel code here;
                mBottomSheetDialog.cancel();
            }
        });

        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to description;
                if (gtg[0]) {
                    startActivity(intent);
                }

            }
        });

        // display details pop-up
        mBottomSheetDialog.show();

        // make ID request
        System.out.println("+++++MAKING ID REQUEST+++++");
        queue.add(identificationRequest);
    }

    private Bitmap display_image(Bitmap myBitmap) {
        //picture taken
        if (!getIntent().getExtras().getBoolean("boolean")) {
            //File imgFile = (File) getIntent().getExtras().get("picture");
            //myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            myBitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
            image.setImageBitmap(myBitmap);
            //image.setImageBitmap(myBitmap);
            image.setRotation(90);
        }
        //picture from internal storage
        else {
            if ((boolean) getIntent().getExtras().get("boolean2")) {
                byte[] decodedString = Base64.decode((String) getIntent().getExtras().get("picture2"), Base64.DEFAULT);
                myBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image.setImageBitmap(myBitmap);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                myBitmap = BitmapFactory.decodeFile((String) getIntent().getExtras().get("picture2"));
                image.setImageBitmap(myBitmap);
            }
        }
        return myBitmap;
    }

    private String bitmapToBase64(Bitmap myBitmap) {
        //bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return imageString;
    }

    private Bitmap base64ToBitmap(String imageString) {
        byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;
    }

    private JsonObjectRequest buildIdentificationRequest(final Intent intent, final boolean[] gtg, final JSONObject postParams, final String imageString){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, identification_url, postParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("++++++++++++ID REQUEST RETURN++++++++++++");
                        try {
                            String shoeID = response.getString("shoeID").replace("_Stock","");
                            shoeID = shoeID.replace("_stock", "");
                            System.out.println(shoeID);
                            //shoeTitle.setText(shoeID.replace("_", " "));
                            postParams.put("shoeID", shoeID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // build and make request to get shoe details
                        JsonObjectRequest detailsRequest = buildDetailsRequest(intent, gtg, postParams);
                        // make details request
                        queue.add(detailsRequest);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("++++++++++++ID REQUEST FAIL++++++++++++");
                        System.out.println(error);
                        shoeTitle.setText("Could not identify image");
                        //Failure Callback
                    }
                }) {

            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        return jsonObjReq;
    }

    private JsonObjectRequest buildDetailsRequest(final Intent intent, final boolean[] gtg, JSONObject postParams) {
        final String[] tosave = new String[1];
        final String[] Name = new String[1];
        final String[] Details = new String[1];
        final String[] Price = new String[1];

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, details_url, postParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("++++++++++++REC REQUEST RETURN++++++++++++");
                        try {
                            // write response to phone storage
                            JSONArray jarray;
                            if (readFromFile().equals("")) {
                                jarray = new JSONArray();
                            } else {
                                System.out.println("You sure that's a null output?");
                                jarray = new JSONArray(readFromFile());
                            }
                            jarray.put(response);
                            writeToFile(jarray.toString());

                            System.out.println("+++++++++FDHFJDKSHF+++++++++");
                            System.out.println(response.toString());

                            // display details in pop up
                            shoeTitle.setText(response.getString("shoeTitle"));
                            dets.setText(response.getString("shoePrice"));

                            Bitmap imageBitmap = base64ToBitmap(response.getString("shoeImage"));
                            dets_img.setImageBitmap(imageBitmap);

                            // add details to intent
                            Name[0] = response.getString("shoeTitle");
                            Price[0] = response.getString("shoePrice");
                            Details[0] = response.getString("shoeDescription");
                            tosave[0] = response.getString("shoeImage");

                            intent.putExtra("name", Name[0]);
                            intent.putExtra("price", Price[0]);
                            intent.putExtra("details", Details[0]);
                            intent.putExtra("image", tosave[0]);

                            gtg[0] = true;

                            System.out.println("Title After wait: " + Name[0]);
                            System.out.println("Current length of array: " + jarray.length());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("++++++++++++REC REQUEST FAIL++++++++++++");
                        //Failure Callback
                    }
                }) {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        return jsonObjReq;
    }

    private void writeToFile(String data) {
        try {
            System.out.println("DATA BEING WROTE: " + data);
            File mFolder = new File("/data/user/0/edu.temple.solemate/files");
            File imgFile = new File(mFolder.getAbsolutePath() + "/config.txt");

            FileOutputStream fOut = new FileOutputStream(imgFile, false);/// may have to delete false
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);

            myOutWriter.close();

            fOut.flush();
            fOut.close();

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = getApplicationContext().openFileInput("config.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        System.out.println("DATA BEING READ: " + ret);
        return ret;
    }

}
