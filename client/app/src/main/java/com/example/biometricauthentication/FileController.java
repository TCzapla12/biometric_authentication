package com.example.biometricauthentication;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class FileController {
    static String url = "https://192.168.1.37:44360/File/";
    public static ArrayList<FileData>  files = new ArrayList<>();

    public void Show(Context c) throws IOException {
        String reqURL = url + "Show";
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(c);
        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, reqURL, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                JSONArray array = response;
                Type listType = new TypeToken<List<FileData>>() {}.getType();
                files = gson.fromJson(array.toString(), listType);
                for(FileData  f:files)
                {
                    f.Rebase();
                }
                Intent intent = new Intent(c, FileSystemActivity.class);
                c.startActivity(intent);

            }
        }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
        }
    });
        ExampleRequestQueue.add(request);
    }
    void Delete(Context c,String id) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String reqURL = url + "Delete/" + id;
        URL url = new URL(reqURL);
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Accept", "*/*");
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("File deleted");
        } else {
            System.out.println("No file to delete. Server replied HTTP code: " + responseCode);
        }
        connection.disconnect();
    }
    void Upload(String path, String id) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String reqURL = url + "Upload";
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sc.init(null, trustAllCerts, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        String charset = "UTF-8";
        String CRLF = "\r\n";
        File binaryFile = new File(path + "/" + id);
        String boundary = Long.toHexString(System.currentTimeMillis());
        URLConnection connection = new URL(reqURL).openConnection();
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("User-Agent", "Application/1.0.0");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Postman-Token", "142d55f7-d39a-4b8d-875e-01db3a966fff");
        connection.setRequestProperty("Host", "192.168.1.37:44360");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        try (
                OutputStream output = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
        ){
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.copy(binaryFile.toPath(), output);
            }
            output.flush();
            writer.append(CRLF).flush();
            writer.append(CRLF);
            writer.append("--" + boundary + "--").append(CRLF).flush();
        }
        catch (Exception e)
        {e.printStackTrace();}
        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        System.out.println(responseCode);
        }
    void Download(Context c, String id) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String reqURL = url + "Download/" + id;
        URL url = new URL(reqURL);
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = connection.getHeaderField("Content-Disposition");
            if (disposition != null) {
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                fileName = reqURL.substring(reqURL.lastIndexOf("/") + 1,
                        reqURL.length());
            }
            InputStream inputStream = connection.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory(),fileName));
            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        connection.disconnect();
    }
    TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }
    };
    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
}
