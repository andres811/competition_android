package tecno.competitionplatform.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;

import tecno.competitionplatform.classes.AlertDialogManager;
import tecno.competitionplatform.classes.RestClient;
import tecno.competitionplatform.classes.ResultHandler;
import tecno.competitionplatform.config.Config;
import tecno.competitionplatform.entities.SessionUser;
import tecno.competitionplatform.entities.Subscriber;

public class AccountActivity extends BaseActivity {

    private ReadSubscriberTask mReadSubscriberTask = null;
    private ProgressDialog mDialog;
    private ImageView mSubscriberImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getActionBar().setDisplayHomeAsUpEnabled(false);

        mSubscriberImage = (ImageView) findViewById(R.id.subscriber_image);

        mDialog = new ProgressDialog(AccountActivity.this);
        mDialog.setCanceledOnTouchOutside(false);

        mReadSubscriberTask = new ReadSubscriberTask();
        mReadSubscriberTask.execute((Void) null);
    }

    public void onClickEditAccountButton(View view) {
        Toast.makeText(AccountActivity.this, "Funcionalidad no disponible",
                Toast.LENGTH_LONG).show();
    }

    public class SetSubscriberImageTask extends AsyncTask<Void, Void, ResultHandler<Bitmap>> {

        public SetSubscriberImageTask() {}

        @Override
        protected void onPreExecute() {}

        @Override
        protected ResultHandler<Bitmap> doInBackground(Void... params) {

            ResultHandler<Bitmap> result = new ResultHandler<>();

            //Get subscriber id and token from the user data session
            Integer subscriberId = getSessionManager().getUser().getUserId();


            //create the service url
            String url = Config.BASE_URL_SERVICES + Config.SUBSCRIBER_IMAGE_SERVICE;
            url += "/" + subscriberId;

            try {
                RestClient restClient = new RestClient(url);

                Bitmap bmp = restClient.downloadImage(url);
                result.setData(bmp);

            } catch (MalformedURLException e) {
                result.setException(e);
                e.printStackTrace();
            } catch (IOException e) {
                result.setData(null);
                e.printStackTrace();;
            }
            return result;
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(final ResultHandler<Bitmap> result) {


            if (result != null && result.validateResponse()) {

                Bitmap bmp = result.getData();
                if (bmp != null) {

                    mSubscriberImage.setBackground(new BitmapDrawable(getResources(), bmp));

                } else {
                    mSubscriberImage.setBackground(getResources().getDrawable(R.drawable.default_subscriber_image));
                }


            } else {
                AlertDialogManager.getErrorDialog(AccountActivity.this, "ERROR", result.getException().getMessage(), "Volver", true);
            }
        }

        @Override
        protected void onCancelled() {

            //showProgress(false);
        }
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

                //setting suscriber image if exist
                new SetSubscriberImageTask().execute((Void) null);


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
