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

import tecno.competitionplatform.config.Config;

/**
 * Created by Andres on 19/10/2015.
 */
public class RestClient {


    private String method = "GET";

    private URL url = null;

    private JSONObject requestData = null;

    private JSONObject responseData = null;

    private JSONArray responseDataArray = null;

    private int responseCode;


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


    //Constructors

    public RestClient(URL url, JSONObject requestData, String method) {
        this.url = url;
        this.requestData = requestData;
        this.method = method;
    }

    public RestClient(URL url, JSONObject requestData) {
        this.url = url;
        this.requestData = requestData;
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
    public RestClient(String url, String method) throws MalformedURLException {
        this.url = new URL(url);
        this.method = method;
    }


    public RestClient(){
    }


    public void AddHeader(String name, String value)
    {
        //todo
    }

    /*
    Web services call
     */
    public void executeRequest() throws Exception {
        if(url == null) {
            throw new Exception("Url is missing.");
        }

        HttpURLConnection conn = null;
        InputStream inputStream;
        OutputStreamWriter outputStreamWriter;


        conn = (HttpURLConnection) this.url.openConnection();
        conn.setRequestMethod(this.method);


        // conn.getHeaderField("Accept: application/json");
        if(this.method.equals("GET")){

            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Accept", "application/json");

        } else if (this.method.equals("POST")) {

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //Set request json data if needed
            if(this.requestData != null) {
                outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
                outputStreamWriter.write(this.requestData.toString());
                outputStreamWriter.flush();
            }
        }
        //set response code
        this.responseCode = conn.getResponseCode();

        //get response data
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

        conn.disconnect();

    }

}