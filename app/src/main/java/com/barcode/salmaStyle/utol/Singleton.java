package com.barcode.salmaStyle.utol;

public class Singleton {
    private static final Singleton singleton = new Singleton();

    private Singleton() {
    }

    public static Singleton getInstance() {
        return singleton;
    }

    public String language_name = "en";
    public String strPictureName = "";
    public String strClothCode = "";
    public boolean isCoverImage = false;
    public boolean isAddImage = false;

}