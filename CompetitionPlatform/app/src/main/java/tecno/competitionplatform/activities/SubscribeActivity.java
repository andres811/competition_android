package tecno.competitionplatform.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tecno.competitionplatform.classes.AlertDialogManager;
import tecno.competitionplatform.classes.CustomDatePicker;
import tecno.competitionplatform.classes.RestClient;
import tecno.competitionplatform.classes.ResultHandler;
import tecno.competitionplatform.config.Config;
import tecno.competitionplatform.entities.Country;
import tecno.competitionplatform.entities.Subscriber;

public class SubscribeActivity extends BaseActivity {

    private ListCountriesTask mGetCountriesTask = null;
    private UserSubscribeTask mSubscribeTask = null;

    private ProgressDialog mDialog;
    
    //Subscribe form elements
    private EditText mNameView;
    private EditText mLastnameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordRepeatView;
    private EditText mDobView;
    private Spinner mCountrySpinner;
    private DatePickerDialog dobPickerDialog;



    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mDialog = new ProgressDialog(SubscribeActivity.this);
        mDialog.setCanceledOnTouchOutside(false);

        
        //getting form view elements
        mNameView = (EditText) findViewById(R.id.subscribe_name);
        mLastnameView = (EditText) findViewById(R.id.subscribe_lastname);
        mEmailView = (EditText) findViewById(R.id.subscribe_email);
        mPasswordView = (EditText) findViewById(R.id.subscribe_password);
        mPasswordRepeatView = (EditText) findViewById(R.id.subscribe_repeat_password);
        mCountrySpinner = (Spinner) findViewById(R.id.subscribe_country);

        mDobView = (EditText) findViewById(R.id.subscribe_dob);
        mDobView.setInputType(InputType.TYPE_NULL);

        setDateTimeField();

        mGetCountriesTask = new ListCountriesTask(SubscribeActivity.this, mCountrySpinner);
        mGetCountriesTask.execute((Void) null);

        Button mSubscribeButton = (Button) findViewById(R.id.subscribe_button);
        mSubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    attemptSubscribe();
                } catch (ParseException e) {
                    //Only because android ask for it
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     *
     */
    private void setDateTimeField() {


        Calendar newCalendar = Calendar.getInstance();
        dobPickerDialog = new DatePickerDialog(SubscribeActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mDobView.setText(new SimpleDateFormat(Config.VIEW_DATE_FORMAT).format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    /**
     * @param v
     */
    public void onClickDob(View v) {
        dobPickerDialog.show();

        //setting style to buttons.
        Button dobPickerNegativeButton = dobPickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button dobPickerPositiveButton = dobPickerDialog.getButton(DialogInterface.BUTTON_POSITIVE);

        dobPickerNegativeButton.setBackgroundResource(R.drawable.background_button_color);
        dobPickerNegativeButton.setTextColor(this.getResources().getColor(R.color.normal_text));
        dobPickerPositiveButton.setBackgroundResource(R.drawable.background_button_color);
        dobPickerPositiveButton.setTextColor(this.getResources().getColor(R.color.normal_text));
    }

    /**
     * @throws ParseException
     */
    private void attemptSubscribe() throws ParseException {

        cleanErrors();

        // Store values at the time of the subscribe attempt.
        String name = mNameView.getText().toString();
        String lastname = mLastnameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordRepeat = mPasswordRepeatView.getText().toString();
        String dob = mDobView.getText().toString();
        Country country = (Country)mCountrySpinner.getSelectedItem();

        boolean cancel = false;
        View focusView = null;


        /*
         * VALIDATIONS
        */

        //Country
        if(TextUtils.isEmpty(country.getName())){
            focusView = mPasswordView;
            cancel = true;
        }

        //Date of birth
        if(TextUtils.isEmpty(dob)){
            mDobView.setError(getString(R.string.error_field_required));
            focusView = mDobView;
            cancel = true;
        } else if(!isDobValid(dob, Config.VIEW_DATE_FORMAT)){
            mDobView.setError("Fecha inválida. Usar el formato: " + Config.VIEW_DATE_FORMAT);
            focusView = mDobView;
            cancel = true;
        }


        //Passwords
        if(TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            mPasswordRepeatView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if(!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if(!password.equals(passwordRepeat)) {
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

        if (cancel) {
            // There was an error; don't attempt subscribe and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user subscribe attempt.
            //pass the subscriber data in a new subscriber instance
            mSubscribeTask = new UserSubscribeTask(
                    new Subscriber(
                            email,
                            name,
                            lastname,
                            password,
                            new SimpleDateFormat(Config.VIEW_DATE_FORMAT).parse(dob),
                            country
                    )
            );
            mSubscribeTask.execute((Void) null);
        }

    }

    /**
     * @param dob
     * @param viewFormat
     * @return
     */
    private boolean isDobValid(String dob, String viewFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(viewFormat);
        try {
            sdf.parse(dob);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param email
     * @return
     */
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     *
     */
    private void cleanErrors() {
        mNameView.setError(null);
        mLastnameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPasswordRepeatView.setError(null);
        mDobView.setError(null);
    }

    /**
     *
     */
    public class UserSubscribeTask extends AsyncTask<Void, Void, ResultHandler<Subscriber>> {

        Subscriber mSubscriberData = null;

        /**
         * @param subscriber
         */
        public UserSubscribeTask(Subscriber subscriber) {
            this.mSubscriberData = subscriber;
        }

        @Override
        protected void onPreExecute() {

            mDialog.setMessage("Suscribiendo");
            mDialog.show();
        }

        @Override
        protected ResultHandler<Subscriber> doInBackground(Void... params) {

            ResultHandler<Subscriber> result = new ResultHandler<>();

            String url = Config.BASE_URL_SERVICES + Config.SUBSCRIBER_SERVICE;

            try {
                //Create gson with custom date format
                Gson gson = new GsonBuilder().setDateFormat(Config.GSON_DATE_FORMAT).create();

                JSONObject subscriberDataJson = new JSONObject(gson.toJson(mSubscriberData));
                RestClient restClient = new RestClient(url,subscriberDataJson,"POST");

                //Executing request.
                restClient.executeRequest();

                switch (restClient.getResponseCode()) {

                    case HttpURLConnection.HTTP_OK:
                        //Response ok. Break and continue
                        break;

                    case HttpURLConnection.HTTP_NOT_FOUND:
                        throw new Exception("No se qué pasó aquí");

                    case HttpURLConnection.HTTP_INTERNAL_ERROR:
                        throw new Exception("Error del servidor");

                    default:
                        throw new Exception("Error");
                }

                //get json response and save in result
                JSONObject responseSubscribeDataJson = restClient.getResponseData();
                result.setData(gson.fromJson(responseSubscribeDataJson.toString(), Subscriber.class));


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

        /**
         * @param result
         */
        @Override
        protected void onPostExecute(final ResultHandler<Subscriber> result) {

            mDialog.dismiss();

            if (result !=null && result.validateResponse()) {


                Subscriber subscriberData = result.getData();
                UserLoginTask userLoginTask = new UserLoginTask(subscriberData.getEmail(), subscriberData.getPassword());
                userLoginTask.execute((Void) null);


            } else {
                AlertDialogManager.getErrorDialog(SubscribeActivity.this, "Error", result.getException().getMessage(), "Volver", true);
            }
        }
    }
}