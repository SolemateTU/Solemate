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

import java.io.File;

/**
 * Created by xxnoa_000 on 2/28/2018.
 */

public class Details extends Activity {

    private ImageView image;

    private TextView header;

    protected void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.savetemp);

        image= (ImageView) findViewById(R.id.imageView3);

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);


        //picture taken
        if(!getIntent().getExtras().getBoolean("boolean")){
        File imgFile = (File) getIntent().getExtras().get("picture");

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            image.setImageBitmap(myBitmap);
            image.setRotation(90);

            //INSERT CODE FOR AWS ENDPOINT HERE

            final View sheetView = this.getLayoutInflater().inflate(R.layout.pop_up_send, null);
            mBottomSheetDialog.setContentView(sheetView);

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

        }

        //picture from internal storage
        else{

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            Bitmap myBitmap = BitmapFactory.decodeFile((String)getIntent().getExtras().get("picture2"));


            image.setImageBitmap(myBitmap);



            final View sheetView = this.getLayoutInflater().inflate(R.layout.pop_up_send, null);
            mBottomSheetDialog.setContentView(sheetView);

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

        }


       // }
    }
}
