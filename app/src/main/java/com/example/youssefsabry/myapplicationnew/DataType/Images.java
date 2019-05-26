package com.example.youssefsabry.myapplicationnew.DataType;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Images {

    @SerializedName("error")
    public String error;
    @SerializedName("images")
    public List<Datum> images;

    public class Datum {

        @SerializedName("id")
        public Integer id;
        @SerializedName("desc")
        public String desc;
        @SerializedName("url")
        public String url;

    }
}
