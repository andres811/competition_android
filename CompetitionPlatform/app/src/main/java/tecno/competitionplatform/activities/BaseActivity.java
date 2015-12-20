package tecno.competitionplatform.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.internal.app.ToolbarActionBar;
import android.support.v7.widget.AppCompatImageView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import tecno.competitionplatform.adapters.ListCountryAdapter;
import tecno.competitionplatform.classes.AlertDialogManager;
import tecno.competitionplatform.classes.RestClient;
import tecno.competitionplatform.classes.ResultHandler;
import tecno.competitionplatform.classes.SessionManager;
import tecno.competitionplatform.config.Config;
import tecno.competitionplatform.entities.Country;
import tecno.competitionplatform.entities.Subscriber;

public class BaseActivity extends Activity {

    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        ActionBar mActionBar = getActionBar();
        //mActionBar.setDisplayShowHomeEnabled(true);
        //mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        //mActionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        LayoutInflater mInflater = LayoutInflater.from(this);

        //View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
       // TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        //mTitleTextView.setText("My Own Title");
/*
        ImageButton imageButton = (ImageButton) mCustomView
                .findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Refresh Clicked!",
                        Toast.LENGTH_LONG).show();
            }
        });

        */


       // mActionBar.setCustomView(mCustomView);
        //mActionBar.setDisplayShowCustomEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if(getSessionManager().isLoggedIn()){
            inflater.inflate(R.menu.account_actions, menu);
            MenuItem labelNameItem = menu.findItem(R.id.action_account_info);
            SpannableString s = new SpannableString(getSessionManager().getUser().getFirstname());
            s.setSpan(new ForegroundColorSpan(Color.BLUE), 0, s.length(), 0);
            labelNameItem.setTitle(s);
        } else {

            //inflater.inflate(R.menu.menu_main, menu);
            //getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

            //setOverflowButtonColor(this , Color.CYAN);

        }


        return super.onCreateOptionsMenu(menu);
    }

    public static void setOverflowButtonColor(final Activity activity, final int color) {
        final String overflowDescription = activity.getString(R.string.abc_action_menu_overflow_description);
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        final ViewTreeObserver viewTreeObserver = decorView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final ArrayList<View> outViews = new ArrayList<View>();
                decorView.findViewsWithText(outViews, overflowDescription,
                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                if (outViews.isEmpty()) {
                    return;
                }
                AppCompatImageView overflow=(AppCompatImageView) outViews.get(0);
                overflow.setColorFilter(color);
                removeOnGlobalLayoutListener(decorView, this);
            }
        });
    }

    public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
        else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_account_logout) {
            getSessionManager().logoutUser();
            return true;
        } else if ((id == R.id.action_account_info)) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected SessionManager getSessionManager(){
        if( mSessionManager == null){
            mSessionManager = new SessionManager(getApplicationContext());
        }
        return mSessionManager;
    }

    /**
     * Represents an asynchronous login task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, ResultHandler<Subscriber>> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;

            //Dummy credentials.
            //mEmail = "despacito@porlaspiedras.com";
            //mPassword = "123123123";
        }

        @Override
        protected ResultHandler<Subscriber> doInBackground(Void... params) {

            JSONObject credentials;
            JSONObject responseData;
            ResultHandler<Subscriber> result = new ResultHandler<>();
            String url = Config.BASE_URL_SERVICES + Config.AUTHENTICATION_SERVICE;

            try {
                credentials = new JSONObject();
                credentials.put("username", mEmail);
                credentials.put("password", mPassword);
                RestClient restClient = new RestClient(url , credentials,"POST");
                restClient.executeRequest();

                switch (restClient.getResponseCode()) {

                    case HttpURLConnection.HTTP_OK:
                        //get json response and save in json response
                        responseData = restClient.getResponseData();
                        break;

                    case HttpURLConnection.HTTP_UNAUTHORIZED:
                        throw new Exception(getString(R.string.error_incorrect_password));

                    case HttpURLConnection.HTTP_INTERNAL_ERROR:
                        throw new Exception("Error del servidor");

                    default:
                        throw new Exception("Ha ocurrido un error");
                }

                //mapping json to entity
                //Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSzz").create();
                //Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
                Gson gson = new Gson();
                Subscriber subscriber = gson.fromJson(responseData.toString(), Subscriber.class);

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

            if (result !=null && result.validateResponse()) {

                Subscriber subscriber = result.getData();
                getSessionManager().createLoginSession(subscriber.getSubscriberId(), subscriber.getFirstname(), subscriber.getEmail(), subscriber.getToken());

                Toast.makeText(BaseActivity.this, "Has iniciado sesión!",
                        Toast.LENGTH_LONG).show();

                finish();
            } else {
                AlertDialogManager.getErrorDialog(BaseActivity.this, "ERROR", result.getException().getMessage(), "Volver", true);
            }
        }

    }

    public class ListCountriesTask extends AsyncTask<Void, Void, ResultHandler<List<Country>>> {

        private final Context mChildContext;
        private Spinner mCountrySpinner;

        public ListCountriesTask(Context childContext, Spinner countrySpinner) {
            this.mCountrySpinner = countrySpinner;
            this.mChildContext = childContext;
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
                /*
                List<String> countryArrayList = new ArrayList<>();
                Spinner countrySelect = (Spinner) findViewById(R.id.subscribe_country);
                for(Country c : result.getData()){countryArrayList.add(c.getName());}

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(SubscribeActivity.this, android.R.layout.simple_spinner_item,countryArrayList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                countrySelect.setAdapter(dataAdapter);
                */

                ListCountryAdapter listCountryAdapter = new ListCountryAdapter(mChildContext, R.layout.country_list_row, result.getData() );

                listCountryAdapter.notifyDataSetChanged();
                mCountrySpinner.setAdapter(listCountryAdapter);
                mCountrySpinner.setSelection(Config.DEFAULT_COUNTRY_SPINNER_VALUE);

            } else {
                AlertDialogManager.getErrorDialog(mChildContext, "Error", result.getException().getMessage(),"Volver", true);
            }
        }

        @Override
        protected void onCancelled() {

            //showProgress(false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
