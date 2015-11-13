package tecno.competitionplatform.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import tecno.competitionplatform.classes.AlertDialogManager;
import tecno.competitionplatform.classes.RestClient;
import tecno.competitionplatform.classes.ResultHandler;
import tecno.competitionplatform.config.Config;
import tecno.competitionplatform.entities.Country;

public class SubscribeActivity extends Activity {

    private ListCountriesTask mGetCountriesTask = null;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mDialog = new ProgressDialog(SubscribeActivity.this);
        mDialog.setCanceledOnTouchOutside(false);
        mGetCountriesTask = new ListCountriesTask();
        mGetCountriesTask.execute((Void) null);
    }

    public class ListCountriesTask extends AsyncTask<Void, Void, ResultHandler<List<Country>>> {

        public ListCountriesTask() {

        }

        @Override
        protected void onPreExecute() {

            //mDialog.setMessage("Cargando");
            //mDialog.show();

        }

        @Override
        protected ResultHandler<List<Country>> doInBackground(Void... params) {

            JSONArray countryListJson;
            ResultHandler<List<Country>> result = new ResultHandler<>();
            String url = Config.BASE_URL_SERVICES + Config.COUNTRY_SERVICE;

            try {
                RestClient restClient = new RestClient(url,"GET");
                restClient.executeRequest();
                int responseCode = restClient.getResponseCode();

                switch (responseCode) {

                    case HttpURLConnection.HTTP_OK:
                        //get json response and save in json response
                        countryListJson = restClient.getResponseDataArray();
                        break;

                    case HttpURLConnection.HTTP_NOT_FOUND:
                        throw new Exception("No se qué pasó aquí");

                    case HttpURLConnection.HTTP_INTERNAL_ERROR:
                        throw new Exception("Error del servidor");

                    default:
                        throw new Exception("Error");
                }

                //mapping json to entity
                Gson gson = new Gson();
                List<Country> countryList = gson.fromJson(
                        countryListJson.toString(),
                        new TypeToken<List<Country>>() {}.getType()
                );

                //saving data in result
                result.setData(countryList);


            } catch (JSONException e) {
                e.printStackTrace();
                result.setException(e);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                result.setException(e);
            } catch (Exception e) {
                e.printStackTrace();
                result.setException(e);
            }

            return result;
        }

        @Override
        protected void onPostExecute(final ResultHandler<List<Country>> result) {
            //mDialog.dismiss();

            if (result !=null && result.validateResponse()) {

                List<String> countryArrayList = new ArrayList<>();
                Spinner countrySelect = (Spinner) findViewById(R.id.subscribe_country);
                for(Country c : result.getData()){countryArrayList.add(c.getName());}

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(SubscribeActivity.this, android.R.layout.simple_spinner_item,countryArrayList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                countrySelect.setAdapter(dataAdapter);
                //dataAdapter.notifyDataSetChanged();
                //finish();

            } else {
                AlertDialogManager.getErrorDialog(SubscribeActivity.this, "Error", result.getException().getMessage(),"Volver", true);
            }
        }

        @Override
        protected void onCancelled() {

            //showProgress(false);
        }
    }

}