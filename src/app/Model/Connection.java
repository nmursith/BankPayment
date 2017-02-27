package app.Model;



import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class Connection {

    private String baseURL = "http://localhost:3000/sms";
    //private String baseURL = "http://192.168.43.204/API";
    private static Connection conn;


    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public synchronized static Connection getInstance() {
        if(conn== null) {
            conn = new Connection();
        }

        return conn;
    }

    public URI getURI(String file) throws URISyntaxException {
        if (baseURL.charAt(baseURL.length() - 1) != '/') {
            return new URI(baseURL + "/" + file);
        }
        return new URI(baseURL + file);
    }

    public String get(String file, HashMap<String, String> para) throws URISyntaxException, IOException {

        //HttpParams paras = new BasicHttpParams();

        if (para != null) {
            file += "?";
            Iterator<String> keys = para.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                file += (key + "=" + para.get(key) + "&");
                //paras.setParameter(key, para.get(key));
            }
        }
        URI uri = getURI(file);


        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(uri);
        //get.setURI(uri);
        //get.setParams(paras);

        HttpResponse response = client.execute(get);//IO exception
        InputStream stream = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line, data = "";
        while ((line = reader.readLine()) != null) {
            data += line;
        }
        return data;
    }

    public String post(String file, HashMap<String, String> para) throws URISyntaxException, IOException {


        List<NameValuePair> paras = new ArrayList<>();

        if (para != null) {

            Iterator<String> keys = para.keySet().iterator();

            while (keys.hasNext()) {
                String key = keys.next();
                paras.add(new BasicNameValuePair(key, para.get(key)));
            }
        }
        URI uri = new URI(baseURL);//getURI(file);


        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(uri);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        post.setEntity(new UrlEncodedFormEntity(paras));

        HttpResponse response = client.execute(post);//IO exception
        InputStream stream = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line, data = "";
        while ((line = reader.readLine()) != null) {
            data += line;
        }
        return data;
    }

    public void sendPost() throws Exception {

        String url = baseURL;
        URL obj = new URL(url);
        //HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        //add reuqest header
        con.setRequestMethod("POST");
        //con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "Text=Testingn  ngg&phone=+94719356519";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

}
