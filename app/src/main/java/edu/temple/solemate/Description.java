package edu.temple.solemate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xxnoa_000 on 4/1/2018.
 */

public class Description extends Activity {

    private TextView t;
    private ImageView i;

    // for suggestion list fake data
    ListView list;

    ArrayList<String> shoeIDs = new ArrayList<String>();
    ArrayList<String> shoeDescriptions = new ArrayList<String>();
    ArrayList<String> imgStrings = new ArrayList<String>();


    ////////////////////////

    private RequestQueue queue;

    final String recommendation_url = "http://eb-rec-flask-dev.us-east-1.elasticbeanstalk.com";
    final String details_url = "https://3wpql46dsk.execute-api.us-east-1.amazonaws.com/prod/identification-function";

    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desc);
        t = (TextView) findViewById(R.id.tv);
        i = (ImageView) findViewById(R.id.image);

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        // display details info about identified shoe
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String details = getIntent().getStringExtra("details");
        String img = getIntent().getStringExtra("image");

        byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        i.setImageBitmap(decodedByte);

        t.setText(name + " " + details + " " + price);

        JsonObjectRequest recommendation_request = buildRecommendationRequest();
        queue.add(recommendation_request);

        // set list click listener
        list = (ListView)findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(Description.this, "You Clicked at " + shoeIDs.get(position), Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(Description.this, Description.class));
                Intent intent= new Intent(Description.this, Description.class);
                intent.putExtra("name", shoeIDs.get(position));
                intent.putExtra("details", shoeDescriptions.get(position));
                intent.putExtra("price", shoeDescriptions.get(position));
                intent.putExtra("image", imgStrings.get(position));
                startActivity(intent);

            }
        });
    }


    private JsonObjectRequest buildDetailsRequest(String shoeID, final int index) {
        // add shoeID to json request body
        JSONObject postParams = new JSONObject();
        try {
            postParams.put("shoeID", shoeID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, details_url, postParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        // update arrayAdapter lists with shoe details
                        try {
                            shoeIDs.add(index, response.getString("shoeTitle"));
                            shoeDescriptions.add(index, response.getString("lowestPrice"));
                            imgStrings.add(index, response.getString("shoeImage"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // create arraylist adapter and render the list with the updated shoe details
                        CustomList adapter = new
                                CustomList(Description.this, shoeIDs, shoeDescriptions, imgStrings);
                        list=(ListView)findViewById(R.id.list);
                        list.setAdapter(adapter);
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

        // increase timeout limit
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        return jsonObjReq;
    }

    private JsonObjectRequest buildRecommendationRequest() {
        // add shoeID to json request body
        JSONObject postParams = new JSONObject();
        try {
            postParams.put("img", getIntent().getStringExtra("image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, recommendation_url, postParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        // build list with returned shoe IDs
                        String[] ids = new String[3];
                        try {
                            ids[0] = response.getString("shoeID-1").replace("_stock","").toLowerCase();
                            ids[1] = response.getString("shoeID-2").replace("_stock","").toLowerCase();
                            ids[2] = response.getString("shoeID-3").replace("_stock","").toLowerCase();
                            System.out.println(ids[0]);
                            System.out.println(ids[1]);
                            System.out.println(ids[2]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // get details for each required shoe from AWS
                        for (int i = 0; i < 3; i++){
                            JsonObjectRequest req = buildDetailsRequest(ids[i], i);
                            queue.add(req);
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

        // increase timeout limit
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        return jsonObjReq;
    }
//
//    public String readFromFile() {
//
//        String ret = "";
//
//        try {
//            InputStream inputStream = getApplicationContext().openFileInput("config.txt");
//
//            if ( inputStream != null ) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String receiveString = "";
//                StringBuilder stringBuilder = new StringBuilder();
//
//                while ( (receiveString = bufferedReader.readLine()) != null ) {
//                    stringBuilder.append(receiveString);
//                }
//
//                inputStream.close();
//                ret = stringBuilder.toString();
//            }
//        }
//        catch (FileNotFoundException e) {
//            Log.e("login activity", "File not found: " + e.toString());
//        } catch (IOException e) {
//            Log.e("login activity", "Can not read file: " + e.toString());
//        }
//
//        System.out.println("DATA BEING READ: "+ret);
//        return ret;
//    }
}
