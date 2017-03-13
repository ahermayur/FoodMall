package com.example.lnc.foodmall;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lnc.foodmall.Class.AlertDialogManager;
import com.example.lnc.foodmall.RestAPI.RestAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Mall_Dashboard_Hotels extends AppCompatActivity {
    LinearLayout ll_hotel1,ll_hotel2;
    Context context;
    List<View> list=null;
    public static int size;
    public static int selected_hotel;
    public static int[] hid;
    public static String[] contactno;
    public static String[] email;
    public static float[] rating;
    public static String[] hdesc={"ABC","CDE","FGH","IJK","LMN","OPQ","ABC","CDE","ABC","CDE","FGH","IJK","LMN","OPQ","ABC","CDE"};
    public static String[] hname={"ABC","CDE","FGH","IJK","LMN","OPQ","ABC","CDE","ABC","CDE","FGH","IJK","LMN","OPQ","ABC","CDE"};
    public static Bitmap[] logo;
    private ProgressDialog progress;
    AlertDialogManager alert = new AlertDialogManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_mall_dashboard_hotels);
        context=getApplicationContext();
        progress= new ProgressDialog(this);
        progress.setIndeterminate(true);
        ll_hotel1=(LinearLayout) findViewById(R.id.ll_hotel_1);
        ll_hotel2=(LinearLayout) findViewById(R.id.ll_hotel_2);
        callMethodHotelList();
    }

    private void callMethodHotelList() {
        if (!isNetworkAvailable()) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Network Connection Unavilable !", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle user action
                            callMethodHotelList();
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
            new AsyncGetHotelDetails().execute();
        }
    }

    private boolean isNetworkAvailable() {
        // Using ConnectivityManager to check for Network Connection
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    private void loadData() {
        for(int i=0;i<hname.length;i++) {
            final int position=i;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate( R.layout.custom_dashbard_hotel, null);
            final TextView tv_hotel_name = (TextView) addView.findViewById(R.id.hotelname);
            final ImageView img_hotel_logo = (ImageView) addView.findViewById(R.id.hotellogo);
            final ImageView img_menu = (ImageView) addView.findViewById(R.id.popup_menu);
            img_hotel_logo.setImageBitmap(logo[position]);
            tv_hotel_name.setText(hname[position]);
            img_hotel_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_hotel=position;
                    Intent intent=new Intent(Mall_Dashboard_Hotels.this,Hotel_Product_Dashboard.class);
                    intent.putExtra("hotelid",hid[position]);
                    intent.putExtra("hotelname",hname[position]);
                    startActivity(intent);
                }
            });
            img_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(Mall_Dashboard_Hotels.this,img_menu);
                    popup.getMenuInflater().inflate(R.menu.hotel_dashboard_popup_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId())
                            {
                                case R.id.popup_description:
                                    final Dialog dialog = new Dialog(Mall_Dashboard_Hotels.this);
                                    dialog.setContentView(R.layout.custom_hotel_decription);
                                    dialog.setTitle(" Book Appointment ");
                                    TextView tv_desc_hotel_name=(TextView)dialog.findViewById(R.id.tv_desc_hotel_name);
                                    TextView tv_desc_hotel_desc=(TextView)dialog.findViewById(R.id.tv_desc_hotel_desc);
                                    ImageView imv_hotel_des_logo=(ImageView)dialog.findViewById(R.id.imv_hotel_des_logo);
                                    Button btn_ok = (Button) dialog.findViewById(R.id.btn_desc_cancle);
                                    Button btn_view_menu = (Button) dialog.findViewById(R.id.btn_desc_menu);
                                    tv_desc_hotel_name.setText(hname[position]+"");
                                    tv_desc_hotel_desc.setText(hdesc[position]+"");
                                    imv_hotel_des_logo.setImageBitmap(logo[position]);

                                    context=dialog.getContext();

                                    btn_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    btn_view_menu.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();

                                    break;

                            }

                            Toast.makeText(Mall_Dashboard_Hotels.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                    popup.show();
                }
            });

            final  LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);

            if(i%2==0)
            {
                ll_hotel1.addView(addView,params);
            }
            else
            {
                ll_hotel2.addView(addView,params);
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
    private class AsyncGetHotelDetails extends AsyncTask<Void,Void,Void>{
        RestAPI api =new RestAPI();
        @Override
        protected void onPreExecute() {
            progress.setMessage("Please Wait a Moment...");
            progress.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject object=new JSONObject();

            try {
                object=api.HotelDetaillist();

                JSONArray jsonArray=object.getJSONArray("Value");
                JSONObject jsonObj=null;

                size=jsonArray.length();

                hid=new int[size];
                logo=new Bitmap[size];
                hname=new String[size];
                email=new String[size];
                contactno=new String[size];
                rating=new float[size];
                hdesc=new String[size];

                for(int i=0;i<jsonArray.length();i++)
                {
                    jsonObj=jsonArray.getJSONObject(i);
                    hid[i]=jsonObj.getInt("hotelid");
                    hname[i]=jsonObj.getString("hotelname");
                    email[i]=jsonObj.getString("owneremail");
                    rating[i]= (float) jsonObj.getDouble("rating");
                    contactno[i]=jsonObj.getString("mobileno");
                    hdesc[i]=jsonObj.getString("hoteldescription");
                    logo[i] = StringToBitmap(jsonObj.getString("imagepath"));
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progress.dismiss();
            loadData();
        }
    }
}
