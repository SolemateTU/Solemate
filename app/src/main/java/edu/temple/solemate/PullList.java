package edu.temple.solemate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xxnoa_000 on 4/6/2018.
 */

public class PullList extends Activity {

    private ImageView image;
    private TextView sneakerIDTextView;


    private TextView header;
    private TextView dets;
    private TextView shoeTitle;
    private ImageView dets_img;


    JSONArray jarray;
    ListView list;


    protected void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_list);
        jarray = new JSONArray();

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String id_url = "https://3wpql46dsk.execute-api.us-east-1.amazonaws.com/prod/get-user-images";
        final String url = "https://s7.postimg.org/lavivuo23/Solemate.gif";


        final ArrayList<String> names = new ArrayList<String>();
        final ArrayList<String> desc = new ArrayList<String>();
        final ArrayList<String> images = new ArrayList<String>();
        final ArrayList<String> prices = new ArrayList<String>();
        JSONObject temp;

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);

        /*for (int i = 0; i < jarray.length(); i++){

            temp= jarray.optJSONObject(i);

            names.add(temp.optString("shoeTitle"));
            desc.add(temp.optString("shoeDescription"));
            images.add(temp.optString("shoeImage"));
            prices.add(temp.optString("shoePrice"));

        }*/

        // TEST DATA
        final String[] web = {
                "Nike",
                "Addidas",
                "New Balance",
                "Under Armor",
                "Puma",
                "IDK",
                "Crocs"
        } ;
        String[] weeb = {
                "This is a Nike shoe something something something",
                "This is an Addidas shoe something something something",
                "This is a New Balance shoe something something something",
                "This is a Under Armor shoe something something something",
                "This is a Puma shoe something something something",
                "IDK any other shoe types please help",
                "Crocs what are those what are those what are those"
        } ;
        Integer[] imageId = {
                R.drawable.closeicon,
                R.drawable.powerphases,
                R.drawable.powerphases,
                R.drawable.powerphases,
                R.drawable.powerphases,
                R.drawable.powerphases,
                R.drawable.powerphases

        };
        ////////////////////////

        JSONObject postparams=new JSONObject();
        try {
            //postparams.put("img", imageString);
            postparams.put("userID", "charlie");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Array l;



        StringRequest postRequest = new StringRequest(Request.Method.POST, id_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            //need to get something out of this
                            System.out.println(response);
                            ///////////////////////////////////
                            mBottomSheetDialog.cancel();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            // here is params will add to your url using post method
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userID", "charlie");
                //params.put("2ndParamName","valueoF2ndParam");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        //Volley.newRequestQueue(this).add(postRequest);
        System.out.println("The Request: "+postRequest);
        queue.add(postRequest);


        //queue.add(sr);






       /* // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST,
                id_url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        jarray=response;
                        System.out.println(response.toString());
                        // Process the JSON

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred

                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);*/










        System.out.println("MADE IT HERE 1");
        //queue.add(jsonObjReq);
        System.out.println("MADE IT HERE 2");
        final View sheetView = this.getLayoutInflater().inflate(R.layout.pop_up_send, null);
        mBottomSheetDialog.setContentView(sheetView);
        dets = (TextView) sheetView.findViewById(R.id.details);
        dets_img = (ImageView) sheetView.findViewById(R.id.detail_image);

        //sample header, can use this code to place in current data for this single instance
        shoeTitle = (TextView) sheetView.findViewById(R.id.header);
        header= (TextView) sheetView.findViewById(R.id.header);
        shoeTitle.setText("");


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


            }
        });//}






        CustomList2 adapter = new
                CustomList2(PullList.this, names);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(SavedList.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(PullList.this, Details.class);
                intent.putExtra("picture2", images.get(position));

                intent.putExtra("boolean", true);
                System.out.println("Picture: "+images.get(position));
                startActivity(intent);

            }
        });

    }


}