package fr.corentin.pendu.manager;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.corentin.pendu.utils.Link;

/**
 * Class {@link ChargeWebPageManager}
 * @author Corentin Dupont
 * @version For project Info0306
 */

public class ChargeWebPageManager extends AsyncTask<String, Void, String> {

    @SuppressLint("StaticFieldLeak")
    private final WebView webView;
    private final ConnectivityManager connectivityManager;

    public ChargeWebPageManager(WebView webView, ConnectivityManager connectivityManager) {
        this.webView = webView;
        this.connectivityManager = connectivityManager;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "URL invalide !!";
        }
    }

    private String getNetworkName() {
        NetworkInfo networkInfo = this.connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) return networkInfo.getTypeName();
        return "";
    }

    public void loadNetwork(View v) {


        if (!getNetworkName().equals("")) {
            String lang = PreferencesManager.getSavedLanguage(webView.getContext(), "pref");
            if(lang.equals("fr")){
                this.execute(Link.RULES_FR.getLink());
            } else  {
                this.execute(Link.RULES_EN.getLink());
            }
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);

            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = conn.getInputStream();
                int response = conn.getResponseCode();
                Log.d("HTTP response", "The response is: " + response);
                String contentAsString = readIt(is);
                String contentAsString2 = readIt(is, len);
                return contentAsString;
            }

            conn.disconnect();

        } finally {if (is != null) is.close();}

        return null;

    }

    public String readIt(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        return out.toString();
    }

    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        this.webView.loadDataWithBaseURL(null, result, "text/html", "UTF-8", null);
    }
}
