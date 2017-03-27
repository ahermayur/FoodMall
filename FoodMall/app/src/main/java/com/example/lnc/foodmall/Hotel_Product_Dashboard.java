package com.example.lnc.foodmall;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lnc.foodmall.Cart.Cart_Activity;
import com.example.lnc.foodmall.Cart.Product;
import com.example.lnc.foodmall.RestAPI.RestAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Hotel_Product_Dashboard extends AppCompatActivity {
    LinearLayout ll_categoery,ll_product;
    Context context;
    public static int hotel_id;
    private ProgressDialog progress;
    public static String hotel_name;
    Bitmap logo;
    int cart_count;
    int cart_price;
    int cat_id[];
    String cat_name[];
    Bitmap cat_icon[];
    int prod_id[];
    int prod_price[];
    String prod_name[];
    String prod_desc[];
    Bitmap prod_image[];
    TextView tv_price,tv_cart_count,tv_title;
    ImageView icon_logo;
    int size_prod;
    int size;
    int top,bottom,left,right;
    public static ArrayList<Product> product=new ArrayList();
    public static ArrayList<Integer> pid_index=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_product_dashboard);
        context=this;
        progress= new ProgressDialog(this);
        progress.setIndeterminate(true);
        ll_categoery = (LinearLayout)findViewById(R.id.ll_category);
        ll_product = (LinearLayout)findViewById(R.id.ll_product);
        try {

            Bundle extras = getIntent().getExtras();
            hotel_id = extras.getInt("hotelid");
            hotel_name = extras.getString("hotelname");
        }
        catch(Exception e)
        {
            hotel_id=3;
            hotel_name="Logic N Concepts";
        }
    /*
        hotel_id=3;
        hotel_name="Logic N Concepts";
*/
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view =getSupportActionBar().getCustomView();
        tv_price=(TextView)view.findViewById(R.id.action_bar_price);
        tv_cart_count=(TextView)view.findViewById(R.id.action_bar_cart);
        tv_title=(TextView)view.findViewById(R.id.action_bar_title);
        icon_logo=(ImageView)view.findViewById(R.id.action_bar_icon);
        tv_cart_count.setText(product.size()+"");
        tv_title.setText(hotel_name);
        icon_logo.setImageResource(R.drawable.foodmalllogo);
        top=tv_cart_count.getTop();
        bottom=tv_cart_count.getBottom();
        right=tv_cart_count.getRight();
        left=tv_cart_count.getLeft();
        callMethodCatList();
    }
    public void updateCart()
    {
        tv_cart_count.setText(product.size()+"");
        doBounceAnimation(tv_cart_count);
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
            final int catid=cat_id[i];
            tv_cat_name.setText(cat_name[i]);
            img_cat_icon.setImageBitmap(cat_icon[i]);
            addView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll_product.removeAllViews();
                    new AsyncGetProduct().execute(catid);
                }
            });
            ll_categoery.addView(addView,layoutParams);
        }
        try {
            new AsyncGetProduct().execute(cat_id[0]);
        }
        catch(Exception e)
        {

        }
    }
    void doBounceAnimation(View targetView) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationY", -100f, 25,30);
        animator.setInterpolator(new BounceInterpolator());
        animator.setStartDelay(500);
        animator.setDuration(1500);
        animator.start();
    }
    private class AsyncGetProduct extends AsyncTask<Object,Void,Void>{

        RestAPI api =new RestAPI();
        Boolean result=false;
        @Override
        protected void onPreExecute() {
            progress.setMessage("Please Wait Getting Products...");
            progress.show();
        }
        @Override
        protected Void doInBackground(Object... params) {
            JSONObject object=new JSONObject();
            try {
                object=api.Product((Integer) params[0]);
                result=object.getBoolean("Successful");
                JSONArray jsonArray=object.getJSONArray("Value");
                JSONObject jsonObj=null;

                size_prod=jsonArray.length();

                prod_id=new int[size_prod];
                prod_price=new int[size_prod];
                prod_name=new String[size_prod];
                prod_desc=new String[size_prod];
                prod_image=new Bitmap[size_prod];

                for(int i=0;i<jsonArray.length();i++)
                {
                    jsonObj=jsonArray.getJSONObject(i);
                    prod_id[i]=jsonObj.getInt("ProductID");
                    prod_price[i]=Integer.parseInt(jsonObj.getString("Product_Price"));
                    prod_name[i]=jsonObj.getString("Product_Name");
                    prod_desc[i]=jsonObj.getString("Product_Description");
                    prod_image[i] = StringToBitmap(jsonObj.getString("Image_Path"));
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
                loadData1();
            }
        }
    }

    private void loadData1() {
        for(int i=0;i<prod_id.length;i++)
        {

            final int no=i;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 0, 10);
            LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.custom_product, null);
            final TextView tv_product_name = (TextView)addView.findViewById(R.id.tv_product_name);
            final TextView tv_product_price = (TextView)addView.findViewById(R.id.tv_product_price);
            final TextView tv_product_count = (TextView)addView.findViewById(R.id.tv_product_count);
            final TextView tv_product_details = (TextView)addView.findViewById(R.id.tv_product_details);
            final ImageView img_product_icon=(ImageView) addView.findViewById(R.id.imv_product_logo);
            final ImageView img_product_add_remove=(ImageView) addView.findViewById(R.id.imv_product_add_remove);
            final ImageView count_minus=(ImageView) addView.findViewById(R.id.imv_product_count_minus);
            final ImageView count_plus=(ImageView) addView.findViewById(R.id.imv_product_count_plus);
            int product_count=searchProduct(prod_id[i]);
            img_product_add_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int ind=pid_index.indexOf(prod_id[no]);
                    if (img_product_add_remove.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.cartadd).getConstantState())
                    {
                        product.add( new Product(prod_id[no],prod_price[no],1,prod_name[no],prod_desc[no],prod_image[no]));
                        pid_index.add(prod_id[no]);
                        count_minus.setEnabled(true);
                        tv_product_count.setText("" + 1);
                        img_product_add_remove.setImageResource(R.drawable.cartremove);
                        RotateAnimation rotate = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF,
                                0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(500);
                        img_product_add_remove.startAnimation(rotate);
                    }
                    else
                    {
                        tv_product_count.setText("" + 0);
                        product.remove(ind);
                        pid_index.remove(ind);
                        count_minus.setEnabled(false);
                        img_product_add_remove.setImageResource(R.drawable.cartadd);
                        RotateAnimation rotate = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF,
                                0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(500);
                        img_product_add_remove.startAnimation(rotate);
                    }
                    updateCart();
                }
            });
            tv_product_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String details=tv_product_details.getText().toString();
                    if(details.equalsIgnoreCase("See Details"))
                    {
                        tv_product_details.setText(prod_desc[no]);
                        tv_product_details.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.up,0);
                    }
                    else
                    {
                        tv_product_details.setText("See Details");
                        tv_product_details.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.down,0);
                    }
                }
            });
            count_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count=1+Integer.parseInt(tv_product_count.getText().toString());
                    int ind=pid_index.indexOf(prod_id[no]);
                    if(count==1)
                    {
                        product.add( new Product(prod_id[no],prod_price[no],1,prod_name[no],prod_desc[no],prod_image[no]));
                        pid_index.add(prod_id[no]);
                        count_minus.setEnabled(true);
                        img_product_add_remove.setImageResource(R.drawable.cartremove);
                        RotateAnimation rotate = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF,
                                0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(500);
                        img_product_add_remove.startAnimation(rotate);
                    }
                    else
                    {
                        product.set(ind,new Product(product.get(ind).getpId(),product.get(ind).getpPrice(),count,product.get(ind).getpName(),product.get(ind).getpDesc(),product.get(ind).getpImage()));
                    }
                    tv_product_count.setText(""+count);


                    updateCart();
                }
            });
            count_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count=Integer.parseInt(tv_product_count.getText().toString())-1;
                    int ind=pid_index.indexOf(prod_id[no]);
                    if(count>0) {
                        tv_product_count.setText("" + count);
                        product.set(ind,new Product(product.get(ind).getpId(),product.get(ind).getpPrice(),count,product.get(ind).getpName(),product.get(ind).getpDesc(),product.get(ind).getpImage()));
                    }
                    else
                    {
                        if(count==0)
                        {
                            tv_product_count.setText("" + count);
                            product.remove(ind);
                            pid_index.remove(ind);
                            count_minus.setEnabled(false);
                            img_product_add_remove.setImageResource(R.drawable.cartadd);
                            RotateAnimation rotate = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF,
                                    0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
                            rotate.setDuration(500);
                            img_product_add_remove.startAnimation(rotate);
                        }
                    }
                    updateCart();
                }
            });
            final int prodid=prod_id[i];
            tv_product_name.setText(prod_name[i]);
            tv_product_price.setText(prod_price[i]+"");

            tv_product_count.setText(""+product_count);

            img_product_icon.setImageBitmap(prod_image[i]);
            addView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
            ll_product.addView(addView,layoutParams);
        }
    }
    public int searchProduct(int i)
    {
        int c=0;
        for(int j=0;j<product.size();j++)
        {
            if(i==product.get(j).getpId())
            {
                c=product.get(j).getpCount();
                break;
            }
        }
        return c;
    }
    public void openCart(View view)
    {
        Intent intent=new Intent(Hotel_Product_Dashboard.this,Cart_Activity.class);
        startActivity(intent);
        finish();
    }
}
