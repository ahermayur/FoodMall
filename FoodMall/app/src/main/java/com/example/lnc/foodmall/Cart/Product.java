package com.example.lnc.foodmall.Cart;

import android.graphics.Bitmap;

/**
 * Created by lnc on 14/3/17.
 */

public class Product {
    public int pId;
    public int pPrice;
    public int pCount;
    public String pName;
    public String pDesc;
    public Bitmap pImage;
    public Product(int pId, int pPrice, int pCount, String pName, String pDesc, Bitmap pImage) {
        this.pId = pId;
        this.pPrice = pPrice;
        this.pCount = pCount;
        this.pName = pName;
        this.pImage = pImage;
        this.pDesc = pDesc;
    }

    public Bitmap getpImage() {
        return pImage;
    }

    public void setpImage(Bitmap pImage) {
        this.pImage = pImage;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public int getpPrice() {
        return pPrice;
    }

    public void setpPrice(int pPrice) {
        this.pPrice = pPrice;
    }

    public int getpCount() {
        return pCount;
    }

    public void setpCount(int pCount) {
        this.pCount = pCount;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpDesc() {
        return pDesc;
    }

    public void setpDesc(String pDesc) {
        this.pDesc = pDesc;
    }

}
