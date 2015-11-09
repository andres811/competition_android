package tecno.competitionplatform.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import tecno.competitionplatform.config.Config;

/**
 * Created by Andres on 19/10/2015.
 */
public class RestClient {

    //Request method (GET method by default)
    private String method = "GET";
    //Url
    private URL url = null;
    //request data in json
    private JSONObject requestData = null;
    //Extra headers
    private HashMap<String,String> headers = null;
    //Response code
    private int responseCode;
    //The response can be saved in a JSONObject...
    private JSONObject responseData = null;
    //or a JSONArray, depends of the services response data.
    private JSONArray responseDataArray = null;


    public JSONArray getResponseDataArray() {
        return responseDataArray;
    }

    public void setResponseDataArray(JSONArray responseDataArray) {
        this.responseDataArray = responseDataArray;
    }

    public JSONObject getResponseData() {
        return responseData;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setRequestData(JSONObject requestData) {
        this.requestData = requestData;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setUrl(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    public void setMethod(String method) {
        this.method = method;
    }

    /*
     * Contructors
     */
    public RestClient(String url) throws MalformedURLException {
        this.url = new URL(url);
    }
    public RestClient(String url, String method) throws MalformedURLException {
        this.url = new URL(url);
        this.method = method;
    }
    public RestClient(String url, JSONObject requestData) throws MalformedURLException {
        this.url = new URL(url);
        this.requestData = requestData;
    }
    public RestClient(String url, JSONObject requestData, String method) throws MalformedURLException {
        this.url = new URL(url);
        this.requestData = requestData;
        this.method = method;
    }

    /*
     * Add extra headers
     * (Like Token)
     */
    public void addHeader(String name, String value) {
        if(headers == null) {
            headers = new HashMap<>();
        }

        headers.put(name,value);

    }

    /*
     * Service call
     */
    public void executeRequest() throws Exception {

        if(url == null) {
            throw new Exception("Url is missing.");
        }

        HttpURLConnection conn;
        InputStream inputStream;
        OutputStreamWriter outputStreamWriter;

        //Create connection
        conn = (HttpURLConnection) this.url.openConnection();
        //Set request method
        conn.setRequestMethod(this.method);

        //Set Extra headers
        if(headers != null && headers.size() > 0) {
            for (Map.Entry<String,String> header: headers.entrySet()){
                conn.setRequestProperty(header.getKey(), header.getValue());
            }
        }

        //CASE GET
        if(this.method.equals("GET")){

            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            //Set header data type JSON, by default
            conn.setRequestProperty("Accept", "application/json");


        //CASE POST
        } else if (this.method.equals("POST")) {

            //Default request data type
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            //Set request json data here
            if(this.requestData != null) {
                outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
                outputStreamWriter.write(this.requestData.toString());
                outputStreamWriter.flush();
            }
        }

        //get response code
        this.responseCode = conn.getResponseCode();

        //get response data and saved in responseData or responseDataArray
        inputStream = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String jsonResponse="", temp;
        while (null != ((temp = br.readLine())))
        {
            jsonResponse=jsonResponse + temp;
        }
        if(!jsonResponse.equals("")){
            Object auxJson = new JSONTokener(jsonResponse).nextValue();
            if (auxJson instanceof JSONObject){
                this.responseData = new JSONObject(jsonResponse);
            }
            else if (auxJson instanceof JSONArray) {
                this.responseDataArray  = new JSONArray(auxJson.toString());
            }

        }

        //Destroy connection
        conn.disconnect();

    }

}