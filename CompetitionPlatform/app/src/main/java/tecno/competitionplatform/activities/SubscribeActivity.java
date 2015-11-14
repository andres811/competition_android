package tecno.competitionplatform.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tecno.competitionplatform.classes.AlertDialogManager;
import tecno.competitionplatform.classes.RestClient;
import tecno.competitionplatform.classes.ResultHandler;
import tecno.competitionplatform.config.Config;
import tecno.competitionplatform.entities.Country;

public class SubscribeActivity extends Activity {

    private ListCountriesTask mGetCountriesTask = null;
    private ProgressDialog mDialog;
    
    //Subscribe form elements
    private EditText mNameView;
    private EditText mLastnameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordRepeatView;
    private EditText mDobView;
    private Spinner mCountrySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mDialog = new ProgressDialog(SubscribeActivity.this);
        mDialog.setCanceledOnTouchOutside(false);
        mGetCountriesTask = new ListCountriesTask();
        mGetCountriesTask.execute((Void) null);
        
        //getting form elements
        mNameView = (EditText) findViewById(R.id.subscribe_name);
        mLastnameView = (EditText) findViewById(R.id.subscribe_lastname);
        mEmailView = (EditText) findViewById(R.id.subscribe_email);
        mPasswordView = (EditText) findViewById(R.id.subscribe_password);
        mPasswordRepeatView = (EditText) findViewById(R.id.subscribe_repeat_password);
        mDobView = (EditText) findViewById(R.id.subscribe_dob);
        mCountrySpinner = (Spinner) findViewById(R.id.subscribe_country);

        Button mSubscribeButton = (Button) findViewById(R.id.subscribe_button);
        mSubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSubscribe();
            }
        });
    }

    private void attemptSubscribe() {
        cleanErrors();

        // Store values at the time of the subscribe attempt.
        String name = mNameView.getText().toString();
        String lastname = mLastnameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordRepeat = mPasswordRepeatView.getText().toString();
        String dob = mDobView.getText().toString();
        String country = mCountrySpinner.getSelectedItem().toString();

        boolean cancel = false;
        View focusView = null;

        /*
         * VALIDATIONS
         */

        //Country
        if(TextUtils.isEmpty(country)){
            focusView = mPasswordView;
            cancel = true;
        }

        //Date of birth
        if(TextUtils.isEmpty(dob)){
            mDobView.setError(getString(R.string.error_field_required));
            focusView = mDobView;
            cancel = true;
        } else if(!isDobValid(dob, Config.VIEW_DATE_FORMAT)){
            mDobView.setError("Fecha inválida. Usar formato " + Config.VIEW_DATE_FORMAT);
            focusView = mDobView;
            cancel = true;
        }


        //Passwords
        if(TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if(!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if(password != passwordRepeat) {
            mPasswordRepeatView.setError("Las contraseñas no coinciden");
            focusView = mPasswordRepeatView;
            cancel = true;
        }

        //Email
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if(!isEmailValid(email)){
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        //Lastname
        if (TextUtils.isEmpty(lastname)) {
            mLastnameView.setError(getString(R.string.error_field_required));
            focusView = mLastnameView;
            cancel = true;
        }

        //Name
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

    }

    private boolean isDobValid(String dob, String ViewFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(ViewFormat);
        try {
            sdf.parse(dob);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void cleanErrors() {
        mNameView.setError(null);
        mLastnameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPasswordRepeatView.setError(null);
        mDobView.setError(null);
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