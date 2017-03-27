package com.example.lnc.foodmall.Cart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lnc.foodmall.R;
import com.example.lnc.foodmall.Hotel_Product_Dashboard;

import java.util.ArrayList;

public class Cart_Activity extends AppCompatActivity {
    public static ArrayList<Product> product=Hotel_Product_Dashboard.product;
    public static ArrayList<Integer> pid_index=Hotel_Product_Dashboard.pid_index;
    ListView lv_cart_items;
    Context context;
    static Snackbar snackbar;
    int total_prise=0;
    TextView empty;
    CAdapter1 cAdapter1;
    int top,bottom,left,right;
    TextView tv_price,tv_cart_count,tv_title;
    ImageView icon_logo;
    ImageView img_emptycart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        try {

            Bundle extras = getIntent().getExtras();
            Hotel_Product_Dashboard.hotel_id = extras.getInt("hotelid");
            Hotel_Product_Dashboard.hotel_name = extras.getString("hotelname");
        }
        catch(Exception e)
        {
            Hotel_Product_Dashboard.hotel_id=3;
            Hotel_Product_Dashboard.hotel_name="Logic N Concepts";
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
        img_emptycart=(ImageView)findViewById(R.id.iv_drawable);
        tv_cart_count.setText(product.size()+"");
        if(product.size()==0)
        {
            img_emptycart.setVisibility(View.VISIBLE);
            doBounceAnimation1(img_emptycart);
        }
        tv_title.setText(Hotel_Product_Dashboard.hotel_name);
        icon_logo.setImageResource(R.drawable.foodmalllogo);
        top=tv_cart_count.getTop();
        bottom=tv_cart_count.getBottom();
        right=tv_cart_count.getRight();
        left=tv_cart_count.getLeft();
        lv_cart_items=(ListView)findViewById(R.id.lv_cart_items);
        empty=(TextView)findViewById(R.id.tv_empty_cart);
        context=getApplicationContext();
        cAdapter1=new CAdapter1(context);
        lv_cart_items.setAdapter(cAdapter1);
        cAdapter1.notifyDataSetChanged();
        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        Cart_Activity.this);

                alert.setTitle("Empty Cart");
                alert.setIcon(R.drawable.warning);
                alert.setMessage("Are you sure, Do you want empty this Cart ?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TOD O Auto-generated method stub

                        product.clear();
                        pid_index.clear();
                        Hotel_Product_Dashboard.pid_index.clear();
                        Hotel_Product_Dashboard.product.clear();
                        cAdapter1.notifyDataSetChanged();
                        cAdapter1.notifyDataSetInvalidated();
                        showSnakBar();
                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });

                alert.show();

            }
        });
        showSnakBar();
    }
    private void showSnakBar() {

        total_prise=0;
        for(int i=0;i<product.size();i++)
        {
            total_prise=total_prise+(product.get(i).getpPrice()*product.get(i).getpCount());
        }

        snackbar = Snackbar.make(findViewById(android.R.id.content), "Total Amount : "+total_prise, Snackbar.LENGTH_INDEFINITE)
                .setAction("Order Now", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle user action

                    }
                });
        snackbar.setActionTextColor(Color.CYAN);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.GREEN);
        snackbar.show();
    }
    public void updateCart()
    {
        tv_cart_count.setText(product.size()+"");
        doBounceAnimation(tv_cart_count);
    }
    void doBounceAnimation1(View targetView) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationY", -100f, 25,30);
        animator.setInterpolator(new BounceInterpolator());
        animator.setStartDelay(500);
        animator.setDuration(1500);
        animator.setRepeatCount(animator.INFINITE);
        animator.start();
    }
    void doBounceAnimation(View targetView) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationY", -100f, 25,30);
        animator.setInterpolator(new BounceInterpolator());
        animator.setStartDelay(500);
        animator.setDuration(1500);
        animator.start();
    }
    private class CAdapter1 extends BaseAdapter {
        Context context;

        LayoutInflater inflater=null;

        public CAdapter1(Context context) {
            this.context=context;
            inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return product.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class Holder
        {
            int pid;
            TextView tv_product_name;
            TextView tv_product_price;
            TextView tv_product_count;
            TextView tv_product_details;
            ImageView img_product_icon;
            ImageView count_minus;
            ImageView count_plus;
            ImageView img_product_add_remove;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View rows = inflater.inflate(R.layout.custom_product, null);
            final CAdapter1.Holder holder=new CAdapter1.Holder();

            holder.tv_product_name = (TextView)rows.findViewById(R.id.tv_product_name);
            holder.tv_product_price = (TextView)rows.findViewById(R.id.tv_product_price);
            holder.tv_product_count = (TextView)rows.findViewById(R.id.tv_product_count);
            holder.tv_product_details = (TextView)rows.findViewById(R.id.tv_product_details);
            holder.img_product_icon=(ImageView) rows.findViewById(R.id.imv_product_logo);
            holder.count_minus=(ImageView) rows.findViewById(R.id.imv_product_count_minus);
            holder.count_plus=(ImageView) rows.findViewById(R.id.imv_product_count_plus);
            holder.img_product_add_remove=(ImageView) rows.findViewById(R.id.imv_product_add_remove);
            holder.tv_product_name.setText(product.get(position).getpName());
            holder.tv_product_price.setText(""+product.get(position).getpPrice());
            holder.tv_product_count.setText(""+product.get(position).getpCount());
            holder.tv_product_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String details=holder.tv_product_details.getText().toString();
                    if(details.equalsIgnoreCase("See Details"))
                    {
                        holder.tv_product_details.setText(product.get(position).getpDesc());
                        holder.tv_product_details.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.up,0);
                    }
                    else
                    {
                        holder.tv_product_details.setText("See Details");
                        holder.tv_product_details.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.down,0);
                    }
                }
            });
            holder.img_product_add_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int ind=pid_index.indexOf(pid_index.get(position));
                    final int count=Integer.parseInt(holder.tv_product_count.getText().toString())-1;
                    if (holder.img_product_add_remove.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.cartremove).getConstantState())
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(
                                Cart_Activity.this);

                        alert.setTitle("Delete");
                        alert.setIcon(R.drawable.warning);
                        alert.setMessage("Do you want delete this item?");
                        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TOD O Auto-generated method stub

                                holder.tv_product_count.setText("" + count);
                                product.remove(ind);
//                            Hotel_Product_Dashboard.product.remove(ind);
                                pid_index.remove(ind);
//                            Hotel_Product_Dashboard.pid_index.remove(ind);
                                holder.count_minus.setEnabled(false);
                                cAdapter1.notifyDataSetChanged();
                                cAdapter1.notifyDataSetInvalidated();
                                updateCart();
                                showSnakBar();
                            }
                        });
                        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        });

                        alert.show();
                    }
                }
            });
            holder.count_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count=1+Integer.parseInt(holder.tv_product_count.getText().toString());
                    int ind=pid_index.indexOf(pid_index.get(position));
                    product.set(ind,new Product(product.get(ind).getpId(),product.get(ind).getpPrice(),count,product.get(ind).getpName(),product.get(ind).getpDesc(),product.get(ind).getpImage()));
    //                Hotel_Product_Dashboard.product.set(ind,new Product(product.get(ind).getpId(),product.get(ind).getpPrice(),count,product.get(ind).getpName(),product.get(ind).getpDesc(),product.get(ind).getpImage()));
                    holder.tv_product_count.setText(""+count);
                    showSnakBar();
                    updateCart();
                }
            });

            holder.count_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int count=Integer.parseInt(holder.tv_product_count.getText().toString())-1;
                    final int ind=pid_index.indexOf(pid_index.get(position));
                    if(count>0) {
                        holder.tv_product_count.setText("" + count);
                        product.set(ind,new Product(product.get(ind).getpId(),product.get(ind).getpPrice(),count,product.get(ind).getpName(),product.get(ind).getpDesc(),product.get(ind).getpImage()));
                        updateCart();
                     }
                    else
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(
                                Cart_Activity.this);

                        alert.setTitle("Delete");
                        alert.setIcon(R.drawable.warning);
                        alert.setMessage("Do you want delete this item?");
                        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TOD O Auto-generated method stub

                                holder.tv_product_count.setText("" + count);
                                product.remove(ind);
//                            Hotel_Product_Dashboard.product.remove(ind);
                                pid_index.remove(ind);
//                            Hotel_Product_Dashboard.pid_index.remove(ind);
                                holder.count_minus.setEnabled(false);
                                cAdapter1.notifyDataSetChanged();
                                cAdapter1.notifyDataSetInvalidated();
                                updateCart();
                            }
                        });
                        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        });

                        alert.show();

                    }
                    showSnakBar();
                }
            });
            holder.img_product_add_remove.setImageResource(R.drawable.cartremove);
            holder.img_product_icon.setImageBitmap(product.get(position).getpImage());
            holder.pid=product.get(position).getpId();
            rows.setTag(rows);
            return rows;
        }
    }

    public void openProducts(View view)
    {
        Intent intent=new Intent(Cart_Activity.this,Hotel_Product_Dashboard.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Cart_Activity.this,Hotel_Product_Dashboard.class);
        startActivity(intent);
        finish();
    }
}
