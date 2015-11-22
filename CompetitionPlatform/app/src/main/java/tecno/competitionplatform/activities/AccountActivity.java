package tecno.competitionplatform.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;

import tecno.competitionplatform.classes.AlertDialogManager;
import tecno.competitionplatform.classes.RestClient;
import tecno.competitionplatform.classes.ResultHandler;
import tecno.competitionplatform.config.Config;
import tecno.competitionplatform.entities.MainCompetition;
import tecno.competitionplatform.entities.SessionUser;
import tecno.competitionplatform.entities.Subscriber;

public class AccountActivity extends BaseActivity {

    private ReadSubscriberTask mReadSubscriberTask = null;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getActionBar().setDisplayHomeAsUpEnabled(false);

        mDialog = new ProgressDialog(AccountActivity.this);
        mDialog.setCanceledOnTouchOutside(false);

        mReadSubscriberTask = new ReadSubscriberTask();
        mReadSubscriberTask.execute((Void) null);
    }

    public class ReadSubscriberTask extends AsyncTask<Void, Void, ResultHandler<Subscriber>> {


        public ReadSubscriberTask() {}

        @Override
        protected void onPreExecute() {

            //process dialog
            mDialog.setMessage("Cargando detalle...");
            mDialog.show();
        }

        @Override
        protected ResultHandler<Subscriber> doInBackground(Void... params) {

            JSONObject subscriberJson;
            ResultHandler<Subscriber> result = new ResultHandler<>();

            //Get subscriber id and token from the user data session
            SessionUser sessionUserData = getSessionManager().getUser();
            Integer subscriberId = sessionUserData.getUserId();
            String token = sessionUserData.getToken();

            //create the service url
            String url = Config.BASE_URL_SERVICES + Config.SUBSCRIBER_SERVICE;
            url += "/" + subscriberId;

            try {
                RestClient restClient = new RestClient(url);

                //Add token in header
                restClient.addHeader("Authorization", "oauth " + token);

                restClient.executeRequest();
                int responseCode = restClient.getResponseCode();


                switch (responseCode) {

                    case HttpURLConnection.HTTP_OK:
                        //get json response and save in json response
                        subscriberJson = restClient.getResponseData();
                        break;

                    case HttpURLConnection.HTTP_UNAUTHORIZED:
                        //token expired
                        //todo: logout here?
                        throw new Exception("Su sesión ha expirado");

                    case HttpURLConnection.HTTP_NOT_FOUND:
                        throw new Exception("No hay datos");

                    case HttpURLConnection.HTTP_INTERNAL_ERROR:
                        throw new Exception("Error del servidor");

                    default:
                        throw new Exception("Error");
                }


                //date format gson config
                Gson gson = new GsonBuilder().setDateFormat(Config.GSON_DATE_FORMAT).create();

                //mapping json to entity
                Subscriber subscriber = gson.fromJson(subscriberJson.toString(), Subscriber.class);

                //saving data in result
                result.setData(subscriber);

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
        protected void onPostExecute(final ResultHandler<Subscriber> result) {


            mDialog.dismiss();

            if (result !=null && result.validateResponse()) {

                Subscriber subscriber = result.getData();

                //Get layout items to set with the subscriber data
                TextView txtName = (TextView)findViewById(R.id.account_name);
                TextView txtLastname = (TextView)findViewById(R.id.account_lastname);
                TextView txtDob = (TextView)findViewById(R.id.account_dob);
                TextView txtEmail = (TextView)findViewById(R.id.account_email);
                TextView txtCountry = (TextView)findViewById(R.id.account_country);

                //date formatter
                SimpleDateFormat sdf = new SimpleDateFormat(Config.VIEW_DATE_FORMAT);

                //setting data in the view
                txtName.setText(subscriber.getFirstname());
                txtLastname.setText(subscriber.getLastname());
                txtDob.setText(sdf.format(subscriber.getDob()).toString());
                txtEmail.setText(subscriber.getEmail());
                txtCountry.setText(subscriber.getCountry().getName());


            } else {
                AlertDialogManager.getErrorDialog(AccountActivity.this, "ERROR", result.getException().getMessage(), "Volver", true);
            }
        }

        @Override
        protected void onCancelled() {

            //showProgress(false);
        }
    }
}
