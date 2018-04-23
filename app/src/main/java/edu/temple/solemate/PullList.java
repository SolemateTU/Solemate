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
    private TextView price;
    private TextView shoeTitle;
    private ImageView dets_img;


    JSONArray jarray;
    JSONObject r;
    ListView list;


    protected void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_list);
        jarray = new JSONArray();
        r= new JSONObject();

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String id_url = "https://3wpql46dsk.execute-api.us-east-1.amazonaws.com/prod/get-user-images";
        final String url = "https://s17.postimg.cc/w2zg7k1u7/Solemate.gif";


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

        // build json object
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("userID", "charlie");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // JSONArray request
        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.POST, id_url, postparams, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // heres an example of iterating over each image and printing the contents
                        // create array of image labels
                        JSONArray keys = response.names();
                        jarray= response.names();
                        r=response;
                        for(int i = 0; i<keys.length(); i++) {
                            try {
                                // print individual image string
                                System.out.println(response.get(keys.getString(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        for(int i = 0; i<jarray.length(); i++) {
                            try {
                                names.add((String) r.get(jarray.getString(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        System.out.println("Size of Names now: "+names.size());
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
                                intent.putExtra("picture2", names.get(position));

                                intent.putExtra("boolean", true);
                                intent.putExtra("boolean2", true);
                                System.out.println("Picture: "+names.get(position));
                                startActivity(intent);

                            }
                        });
                        mBottomSheetDialog.cancel();
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }
                ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // prevent request timeout
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // make request
        queue.add(postRequest);

        System.out.println("MADE IT HERE 1");
        //queue.add(jsonObjReq);
        System.out.println("MADE IT HERE 2");
        final View sheetView = this.getLayoutInflater().inflate(R.layout.pop_up_send, null);
        mBottomSheetDialog.setContentView(sheetView);
        price = (TextView) sheetView.findViewById(R.id.price);
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



        // make names array here




    }
    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(PullList.this, DetectorActivity.class);
        startActivity(startMain);
    }


}