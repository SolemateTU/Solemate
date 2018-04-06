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
import android.widget.Button;
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
    private Button recognitionCallButton;
    private TextView sneakerIDTextView;


    private TextView header;
    private TextView dets;
    private TextView shoeTitle;
    private ImageView dets_img;

    protected void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.savetemp);

        Bitmap myBitmap;
        final String[] tosave = new String[1];
        final String[] Name = new String[1];
        final String[] Details = new String[1];
        final String[] Price = new String[1];


        //initiating view objects
        image = (ImageView) findViewById(R.id.imageView3);

        //final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://s7.postimg.org/lavivuo23/Solemate.gif";
        final String id_url = "https://3wpql46dsk.execute-api.us-east-1.amazonaws.com/prod/identification-function";


        final Intent intent= new Intent(Details.this, Description.class);

        //picture taken
        if(!getIntent().getExtras().getBoolean("boolean")){
        File imgFile = (File) getIntent().getExtras().get("picture");

            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            image.setImageBitmap(myBitmap);
            image.setRotation(90);


        }

        //picture from internal storage
        else{

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            myBitmap = BitmapFactory.decodeFile((String) getIntent().getExtras().get("picture2"));

            image.setImageBitmap(myBitmap);

        }


        final boolean[] gtg = {false};


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
                            JSONArray jarray;
                            if (readFromFile().equals("")){
                                jarray = new JSONArray();
                            }
                            else{
                                System.out.println("You sure that's a null output?");
                                jarray = new JSONArray(readFromFile());
                            }
                            String test;
                            shoeTitle.setText(response.getString("shoeTitle"));
                            Name[0] =response.getString("shoeTitle");
                            Price[0]=response.getString("shoePrice");
                            Details[0]=response.getString("shoeDescription");
                            dets.setText(response.getString("shoePrice"));
                            byte[] decodedString = Base64.decode(response.getString("shoeImage"), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            dets_img.setImageBitmap(decodedByte);
                            tosave[0] = response.getString("shoeImage");
                            jarray.put(response);
                            writeToFile(jarray.toString());
                            System.out.println("Title After wait: "+Name[0]);
                            System.out.println("Current length of array: "+jarray.length());
                            gtg[0] =true;



                            intent.putExtra("name", Name[0]);
                            intent.putExtra("price", Price[0]);
                            intent.putExtra("details", Details[0]);
                            intent.putExtra("image", tosave[0]);



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


        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        final View sheetView = this.getLayoutInflater().inflate(R.layout.pop_up_send, null);
        mBottomSheetDialog.setContentView(sheetView);
        dets = (TextView) sheetView.findViewById(R.id.details);
        dets_img = (ImageView) sheetView.findViewById(R.id.detail_image);

        //sample header, can use this code to place in current data for this single instance
        shoeTitle = (TextView) sheetView.findViewById(R.id.header);
        header= (TextView) sheetView.findViewById(R.id.header);
        shoeTitle.setText("Loading");


        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget((ImageView) sheetView.findViewById(R.id.detail_image));
        Glide.with(this).load(url).into(imageViewTarget);


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
        //if (Name[0]!="null"){
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to description;
                //startActivity(new Intent(Details.this, Description.class));
                //System.out.println("Is it Null? "+Name[0]);

                if (gtg[0]){
                startActivity(intent);}

            }
        });//}

        queue.add(jsonObjReq);
        System.out.println("Title: "+Name[0]);
        System.out.println("Description: "+Details[0]);
        System.out.println("Price: "+Price[0]);
    }

    private void writeToFile(String data) {
        try {
            System.out.println("DATA BEING WROTE: "+data);
            File mFolder = new File("/data/user/0/edu.temple.solemate/files");
            File imgFile = new File(mFolder.getAbsolutePath() + "/config.txt");

            FileOutputStream fOut = new FileOutputStream(imgFile, false);/// may have to delete false
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            //myOutWriter.write(data);

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

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        System.out.println("DATA BEING READ: "+ret);
        return ret;
    }

}
