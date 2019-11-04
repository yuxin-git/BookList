package com.example.lifeprice.data;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.lifeprice.data.model.Shop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ShopLoader {
    public ArrayList<Shop> getShops() {
        return shops;
    }

    private ArrayList<Shop> shops=new ArrayList<>();

    public String download(String urlString)
    {
        try {
            // 调用URL对象的openConnection方法获取HttpURLConnection的实例
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方式，GET或POST
            connection.setRequestMethod("GET");
            // 设置连接超时、读取超时的时间，单位为毫秒（ms）
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // 设置是否使用缓存  默认是true
            connection.setUseCaches(false);
            //设置请求头里面的属性

            // 开始连接
            Log.i("HttpURLConnection.GET","开始连接");
            connection.connect();

            //获取数据
            InputStream inputStream = connection.getInputStream();
            //使用BufferedReader对象读取返回的数据流
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
            if (connection.getResponseCode() == 200) {
                Log.i("HttpURLConnection.GET", "请求成功");
                // 按行读取，存储在StringBuider对象response中
                String line;
                StringBuffer resultData=new StringBuffer();

                while ((line = buffer.readLine()) != null) {
                    resultData.append(line);
                }
                String text=resultData.toString();
                Log.v("out: ",text);
                return(text);
            }else{
                Log.i("HttpURLConnection.GET", "请求失败");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
    public void parseJson(String text)
    {
        try{
            JSONObject jsonObject=new JSONObject(text);
            JSONArray jsonDatas=jsonObject.getJSONArray("shops");
            int length=jsonDatas.length();
            String test;
            for(int i=0;i<length;i++){
                JSONObject shopJson=jsonDatas.getJSONObject(i);
                Shop shop=new Shop();
                shop.setName(shopJson.getString("name"));
                shop.setLatitude(shopJson.getDouble("latitude"));
                shop.setLongitude(shopJson.getDouble("longitude"));
                shop.setMemo(shopJson.getString("memo"));
                shops.add(shop);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void load(final Handler handler, final String url)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String content=download(url);
                parseJson(content);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }
}
