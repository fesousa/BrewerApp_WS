package br.com.fernandosousa.brewerapp;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*

Creditos: Ricardo Lecheta - Exemplos do livro android
*/
public class HttpHelper {

    private static final String TAG = "WS";
    public static String doGet(String url) throws IOException {
        Log.d(TAG, ">> Http.doGet: " + url);
        URL u = new URL(url);
        HttpURLConnection conn = null;
        String s = null;
        try {
            conn = (HttpURLConnection) u.openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.connect();
            InputStream in = null;
            int status = conn.getResponseCode();
            if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
                Log.d(TAG, "Error code: " + status);
                in = conn.getErrorStream();
            } else {
                in = conn.getInputStream();
            }

            byte[] bytes = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    bos.write(buffer, 0, len);
                }
                bytes = bos.toByteArray();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                return null;
            } finally {
                try {
                    bos.close();
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
            s = new String(bytes, "UTF-8");
            Log.d(TAG, "<< Http.doGet: " + s);
            in.close();
        } catch (IOException e) {
            throw e;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return s;
    }
}
