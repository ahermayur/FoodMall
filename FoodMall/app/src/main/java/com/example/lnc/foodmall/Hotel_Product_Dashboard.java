package com.example.lnc.foodmall;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lnc.foodmall.RestAPI.RestAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class Hotel_Product_Dashboard extends AppCompatActivity {
    LinearLayout ll_categoery;
    Context context;
    int hotel_id;
    private ProgressDialog progress;
    String hotel_name;
    Bitmap logo;

    int cat_id[];
    String cat_name[];
    Bitmap cat_icon[];
    int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_product_dashboard);
        context=this;
        progress= new ProgressDialog(this);
        progress.setIndeterminate(true);
        ll_categoery = (LinearLayout)findViewById(R.id.ll_category);
        Bundle extras = getIntent().getExtras();
        hotel_id=extras.getInt("hotelid");
        hotel_name=extras.getString("hotelname");
        callMethodCatList();
    }
    private boolean isNetworkAvailable() {
        // Using ConnectivityManager to check for Network Connection
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    private void callMethodCatList() {
        if (!isNetworkAvailable()) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Network Connection Unavilable !", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle user action
                            callMethodCatList();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
        else
        {
            new asyncCategoery().execute(hotel_id);
        }
    }

    private class asyncCategoery extends AsyncTask<Object,Void,Void>{
        RestAPI api =new RestAPI();
        Boolean result=false;
        @Override
        protected void onPreExecute() {
            progress.setMessage("Please Wait a Moment...");
            progress.show();
        }
        @Override
        protected Void doInBackground(Object... params) {
            JSONObject object=new JSONObject();

            try {
                object=api.Category(hotel_id);
                result=object.getBoolean("Successful");
                JSONArray jsonArray=object.getJSONArray("Value");
                JSONObject jsonObj=null;

                size=jsonArray.length();

                cat_id=new int[size];
                cat_name=new String[size];
                cat_icon=new Bitmap[size];

                for(int i=0;i<jsonArray.length();i++)
                {
                    jsonObj=jsonArray.getJSONObject(i);
                    cat_id[i]=jsonObj.getInt("CategoryID");
                    cat_name[i]=jsonObj.getString("CategoryName");
                    cat_icon[i] = StringToBitmap(jsonObj.getString("Category_Image"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progress.dismiss();
            if (result==true) {
                loadData();
            }
            else
            {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Handle user action
                                callMethodCatList();
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            }
        }
    }
    private Bitmap StringToBitmap(String photo) {

        try {
            byte[] encodeByte = Base64.decode(photo, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch (Exception e){
            e.getMessage();
        }
        return null;
    }
    private void loadData() {
        for(int i=0;i<cat_id.length;i++)
        {
            final int no=i;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 20, 30, 20);
            LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.custom_category, null);
            final TextView tv_cat_name = (TextView)addView.findViewById(R.id.tv_cat_title);
            final CircleImageView img_cat_icon=(CircleImageView) addView.findViewById(R.id.img_cat_icon);
            tv_cat_name.setText(cat_name[i]);
            img_cat_icon.setImageBitmap(cat_icon[i]);
            addView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Category "+no,Toast.LENGTH_LONG).show();
                }
            });
            ll_categoery.addView(addView,layoutParams);
        }
    }
}
