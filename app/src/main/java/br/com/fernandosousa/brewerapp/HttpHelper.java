package br.com.fernandosousa.brewerapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
/*
* Créditos - Ricardo Lecheta - Exempls Livro Android - Android Utils
* https://github.com/livroandroid/AndroidUtils/blob/master/app/src/main/java/livroandroid/lib/utils/HttpHelper.java
* */
public class HttpHelper {
    private final String TAG = "Http";
    public final int TIMEOUT_MILLIS = 15000;
    public boolean LOG_ON = false;
    private String contentType;
    private String charsetToEncode;

    public String doGet(String url) throws IOException {
        return doGet(url, null, "UTF-8");
    }

    public String doGet(String url, Map<String, String> params, String charset) throws IOException {
        String queryString = getQueryString(params);
        if (queryString != null && queryString.trim().length() > 0) {
            url += "?" + queryString;
        }

        if (LOG_ON) {
            Log.d(TAG, ">> Http.doGet: " + url);
        }

        URL u = new URL(url);
        HttpURLConnection conn = null;
        String s = null;
        try {
            conn = (HttpURLConnection) u.openConnection();
            if (contentType != null) {
                conn.setRequestProperty("Content-Type", contentType);
            }
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIMEOUT_MILLIS);
            conn.setReadTimeout(TIMEOUT_MILLIS);
            conn.connect();
            InputStream in = null;
            int status = conn.getResponseCode();
            if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
                Log.d(TAG, "Error code: " + status);
                in = conn.getErrorStream();
            } else {
                in = conn.getInputStream();
            }
            s = toString(in, charset);
            if (LOG_ON) {
                Log.d(TAG, "<< Http.doGet: " + s);
            }
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

    public String doDelete(String url) throws IOException {
        return doDelete(url, null, "UTF-8");
    }

    public String doDelete(String url, Map<String, String> params, String charset) throws IOException {
        String queryString = getQueryString(params);
        if (queryString != null && queryString.trim().length() > 0) {
            url += "?" + queryString;
        }

        if (LOG_ON) {
            Log.d(TAG, ">> Http.doDelete: " + url);
        }

        URL u = new URL(url);
        HttpURLConnection conn = null;
        String s = null;
        try {
            conn = (HttpURLConnection) u.openConnection();
            if (contentType != null) {
                conn.setRequestProperty("Content-Type", contentType);
            }
            conn.setRequestMethod("DELETE");
            conn.setConnectTimeout(TIMEOUT_MILLIS);
            conn.setReadTimeout(TIMEOUT_MILLIS);

            conn.connect();
            InputStream in = conn.getInputStream();
            s = toString(in, charset);
            if (LOG_ON) {
                Log.d(TAG, "<< Http.doGet: " + s);
            }
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

    public String doPost(String url, Map<String, String> params, String charset) throws IOException {
        String queryString = getQueryString(params);
        byte[] bytes = params != null ? queryString.getBytes(charset) : null;
        if (LOG_ON) {
            Log.d(TAG, "Http.doPost: " + url + "?" + params);
        }
        return doPost(url, bytes, charset);
    }

    public String doPost(String url, byte[] params, String charset) throws IOException {
        if (LOG_ON) {
            Log.d(TAG, ">> Http.doPost: " + url);
        }

        URL u = new URL(url);
        HttpURLConnection conn = null;
        String s = null;
        try {
            conn = (HttpURLConnection) u.openConnection();
            if (contentType != null) {
                conn.setRequestProperty("Content-Type", contentType);
            }
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(TIMEOUT_MILLIS);
            conn.setReadTimeout(TIMEOUT_MILLIS);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            if (params != null) {
                OutputStream out = conn.getOutputStream();
                out.write(params);
                out.flush();
                out.close();
            }
            InputStream in = null;
            int status = conn.getResponseCode();
            if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
                Log.d(TAG, "Error code: " + status);
                in = conn.getErrorStream();
            } else {
                in = conn.getInputStream();
            }
            s = toString(in, charset);
            if (LOG_ON) {
                Log.d(TAG, "<< Http.doPost: " + s);
            }
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

    public Bitmap doGetBitmap(String url) throws IOException {
        if (LOG_ON) {
            Log.d(TAG, ">> Http.doGet: " + url);
        }
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(TIMEOUT_MILLIS);
        conn.setReadTimeout(TIMEOUT_MILLIS);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.connect();
        InputStream in = conn.getInputStream();
        byte[] bytes = toBytes(in);
        if (LOG_ON) {
            Log.d(TAG, "<< Http.doGet: " + bytes);
        }
        in.close();
        conn.disconnect();
        Bitmap bitmap = null;
        if (bytes != null) {
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return bitmap;
    }

    /**
     * Retorna a QueryString para 'GET'
     */
    public String getQueryString(Map<String, String> params) throws IOException {
        if (params == null || params.size() == 0) {
            return null;
        }
        String urlParams = null;
        for (String chave : params.keySet()) {
            Object objValor = params.get(chave);
            if (objValor != null) {
                String valor = objValor.toString();
                if (charsetToEncode != null) {
                    valor = URLEncoder.encode(valor, charsetToEncode);
                }
                urlParams = urlParams == null ? "" : urlParams + "&";
                urlParams += chave + "=" + valor;
            }
        }
        return urlParams;

    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setCharsetToEncode(String encode) {
        this.charsetToEncode = encode;
    }


    public static String toString(InputStream in, String charset) throws IOException {
        byte[] bytes = toBytes(in);
        String texto = new String(bytes, charset);
        return texto;
    }

    /**
     * Converte a InputStream para bytes[]
     *
     * @param in
     * @return
     */
    public static byte[] toBytes(InputStream in) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            byte[] bytes = bos.toByteArray();
            return bytes;
        } catch (Exception e) {
            Log.e("IOUtils", e.getMessage(), e);
            return null;
        } finally {
            try {
                bos.close();
                in.close();
            } catch (IOException e) {
                Log.e("IOUtils", e.getMessage(), e);
            }
        }
    }


}
