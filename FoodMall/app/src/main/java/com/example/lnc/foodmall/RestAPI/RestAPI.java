 /* JSON API for android appliation */
package com.example.lnc.foodmall.RestAPI;

 import org.json.JSONArray;
 import org.json.JSONObject;

 import java.io.IOException;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.io.OutputStreamWriter;
 import java.io.UnsupportedEncodingException;
 import java.lang.reflect.Method;
 import java.lang.reflect.Modifier;
 import java.net.HttpURLConnection;
 import java.net.URL;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.Locale;
 import java.util.Map;

 public class RestAPI {
     private final String urlString = "http://192.168.1.111/Food%20Mall%20System/Food%20Mall%20System/Handler1.ashx";

     private static String convertStreamToUTF8String(InputStream stream) throws IOException {
         String result = "";
         StringBuilder sb = new StringBuilder();
         try {
             InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
             char[] buffer = new char[4096];
             int readedChars = 0;
             while (readedChars != -1) {
                 readedChars = reader.read(buffer);
                 if (readedChars > 0)
                     sb.append(buffer, 0, readedChars);
             }
             result = sb.toString();
         } catch (UnsupportedEncodingException e) {
             e.printStackTrace();
         }
         return result;
     }


     private String load(String contents) throws IOException {
         URL url = new URL(urlString);
         HttpURLConnection conn = (HttpURLConnection)url.openConnection();
         conn.setRequestMethod("POST");
         conn.setConnectTimeout(60000);
         conn.setDoOutput(true);
         conn.setDoInput(true);
         OutputStreamWriter w = new OutputStreamWriter(conn.getOutputStream());
         w.write(contents);
         w.flush();
         InputStream istream = conn.getInputStream();
         String result = convertStreamToUTF8String(istream);
         return result;
     }


     private Object mapObject(Object o) {
         Object finalValue = null;
         if (o.getClass() == String.class) {
             finalValue = o;
         }
         else if (Number.class.isInstance(o)) {
             finalValue = String.valueOf(o);
         } else if (Date.class.isInstance(o)) {
             SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss", new Locale("en", "USA"));
             finalValue = sdf.format((Date)o);
         }
         else if (Collection.class.isInstance(o)) {
             Collection<?> col = (Collection<?>) o;
             JSONArray jarray = new JSONArray();
             for (Object item : col) {
                 jarray.put(mapObject(item));
             }
             finalValue = jarray;
         } else {
             Map<String, Object> map = new HashMap<String, Object>();
             Method[] methods = o.getClass().getMethods();
             for (Method method : methods) {
                 if (method.getDeclaringClass() == o.getClass()
                         && method.getModifiers() == Modifier.PUBLIC
                         && method.getName().startsWith("get")) {
                     String key = method.getName().substring(3);
                     try {
                         Object obj = method.invoke(o, null);
                         Object value = mapObject(obj);
                         map.put(key, value);
                         finalValue = new JSONObject(map);
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
             }

         }
         return finalValue;
     }

     public JSONObject HotelDetaillist() throws Exception {
         JSONObject result = null;
         JSONObject o = new JSONObject();
         JSONObject p = new JSONObject();
         o.put("interface","RestAPI");
         o.put("method", "HotelDetaillist");
         o.put("parameters", p);
         String s = o.toString();
         String r = load(s);
         result = new JSONObject(r);
         return result;
     }

     public JSONObject Category(int hid) throws Exception {
         JSONObject result = null;
         JSONObject o = new JSONObject();
         JSONObject p = new JSONObject();
         o.put("interface","RestAPI");
         o.put("method", "Category");
         p.put("hid",mapObject(hid));
         o.put("parameters", p);
         String s = o.toString();
         String r = load(s);
         result = new JSONObject(r);
         return result;
     }
     public JSONObject Product(int cid) throws Exception {
         JSONObject result = null;
         JSONObject o = new JSONObject();
         JSONObject p = new JSONObject();
         o.put("interface","RestAPI");
         o.put("method", "Product");
         p.put("cid",mapObject(cid));
         o.put("parameters", p);
         String s = o.toString();
         String r = load(s);
         result = new JSONObject(r);
         return result;
     }

 }