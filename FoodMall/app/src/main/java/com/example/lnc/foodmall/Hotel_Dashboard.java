package com.example.lnc.foodmall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AnalogClock;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Hotel_Dashboard extends AppCompatActivity {
    EditText editText;
    TabHost tabHost;
    ListView lv_cat1,lv_cat2,lv_cat3,lv_cat4,lv_cat5;
    Context context;
    FrameLayout frameLayout;
    int[] id={1,2,3,4,5};
    int[] price={100,234,4444,222,444};
    String[] product_name={"ABC","CDE","EFG","GHI","JKL"};
    String[] cat_name={"ABC","CDE","EFG","GHI","JKL"};
    String[] product_desc={"ABC","CDE","EFG","GHI","JKL"};
    CAdapter[] adapter=new CAdapter[5];
    int[] count={0,0,0,0,0};

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_dashboard);
        context=this;
        frameLayout=(FrameLayout)findViewById(android.R.id.tabcontent);
        lv_cat1=(ListView)findViewById(R.id.lv_cat1);
        lv_cat2=(ListView)findViewById(R.id.lv_cat2);
        lv_cat3=(ListView)findViewById(R.id.lv_cat3);
        lv_cat4=(ListView)findViewById(R.id.lv_cat4);
        lv_cat5=(ListView)findViewById(R.id.lv_cat5);
        adapter[0]=new CAdapter(context,id,product_name,price,product_desc);
        adapter[1]=new CAdapter(context,id,product_name,price,product_desc);
        adapter[2]=new CAdapter(context,id,product_name,price,product_desc);
        adapter[3]=new CAdapter(context,id,product_name,price,product_desc);
        adapter[4]=new CAdapter(context,id,product_name,price,product_desc);

        TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();


            final TabHost.TabSpec spec1 = tabHost.newTabSpec(cat_name[0]);
            spec1.setContent(R.id.tab1);
            spec1.setIndicator(cat_name[0]);

            TabHost.TabSpec spec2 = tabHost.newTabSpec("Tab 2");
            spec2.setIndicator("Tab 2");
            ListView lv = new ListView(context);
            lv.setAdapter(new CAdapter(context, id, product_name, price, product_desc));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(Hotel_Dashboard.this, "" + position, Toast.LENGTH_LONG).show();
                }
            });
            spec2.setContent(new TabHost.TabContentFactory() {
                public View createTabContent(String tag) {
                    lv_cat2.setAdapter(adapter[2]);
                    return lv_cat2;
                }
            });


            TabHost.TabSpec spec3 = tabHost.newTabSpec("Tab 3");
            spec3.setIndicator("Tab 1");
            spec3.setContent(R.id.tab3);


            tabHost.addTab(spec1);
            tabHost.addTab(spec2);
            tabHost.addTab(spec3);
        lv_cat1.setAdapter(new CAdapter(context,id,product_name,price,product_desc));

    }

    private class CAdapter extends BaseAdapter {
        Context context;
        int[] id;
        String[] product_name;
        String[] product_desc;
        int[] price;
        LayoutInflater inflater=null;

        public CAdapter(Context context, int[] id, String[] product_name, int[] price, String[] product_desc) {
            this.context = context;
            this.id = id;
            this.product_name = product_name;
            this.product_desc = product_desc;
            this.price = price;
            inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return id.length;
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
            TextView tv_product_name;
            TextView tv_product_price;
            ImageView imv_product_remove;
            ImageView imv_product_add;
            TextView tv_product_count;
            TextView tv_product_details;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View rows = inflater.inflate(R.layout.custom_product, null);
            final CAdapter.Holder holder=new CAdapter.Holder();
            holder.tv_product_name = (TextView) rows.findViewById(R.id.tv_product_name);
            holder.tv_product_price = (TextView) rows.findViewById(R.id.tv_product_price);
            holder.tv_product_count = (TextView) rows.findViewById(R.id.tv_product_count);
            holder.imv_product_remove = (ImageView) rows.findViewById(R.id.imv_product_remove);
            holder.imv_product_add = (ImageView) rows.findViewById(R.id.imv_product_add);
            holder.tv_product_details = (TextView) rows.findViewById(R.id.tv_product_details);
            holder.tv_product_name.setText(product_name[position]);
            holder.tv_product_price.setText(price[position]+"");
            holder.tv_product_count.setText(count[position]+"");
            holder.imv_product_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   if(count[position]!=0) {
                        count[position] = count[position] + 1;
                        holder.tv_product_count.setText(count[position]+"");
                 //   }
                }
            });
            holder.imv_product_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(count[position]!=0) {
                        count[position] = count[position] - 1;
                        holder.tv_product_count.setText(count[position]+"");
                    }
                }
            });
            rows.setTag(rows);
            return rows;
        }
    }
}
