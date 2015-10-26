package tecno.competitionplatform.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;

import tecno.competitionplatform.activities.R;
import tecno.competitionplatform.classes.RestClient;
import tecno.competitionplatform.classes.ResultHandler;
import tecno.competitionplatform.classes.SessionManager;
import tecno.competitionplatform.config.Config;
import tecno.competitionplatform.entities.MainCompetition;
import tecno.competitionplatform.entities.Subscriber;

public class MainCompetitionListActivity extends Activity {

    private ListMainCompetition mListMainCompetitionTask = null;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressView = findViewById(R.id.loading_progress);
        showProgress(true);
        mListMainCompetitionTask = new ListMainCompetition(0,10);
        mListMainCompetitionTask.execute((Void) null);

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        }
    }


    public class ListMainCompetition extends AsyncTask<Void, Void, ResultHandler<List<MainCompetition>>> {

        private final int from;
        private final int to;

        public ListMainCompetition(int from, int to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected void onPreExecute() {

            ProgressDialog spinner  = new ProgressDialog(MainCompetitionListActivity.this);
            spinner.setMessage("Forza Roma");
            spinner.show();

        }

        @Override
        protected ResultHandler<List<MainCompetition>> doInBackground(Void... params) {

            JSONArray mainCompetitionsJson = null;
            ResultHandler<List<MainCompetition>> result = new ResultHandler<>();
            String url = Config.BASE_URL_SERVICES + Config.MAINCOMPETITION;
            url = url + "/" + from + "/" + to;

            try {
                RestClient restClient = new RestClient(url,"GET");
                restClient.executeRequest();
                int responseCode = restClient.getResponseCode();


                switch (responseCode) {

                    case HttpURLConnection.HTTP_OK:
                        //get json response and save in json response
                        mainCompetitionsJson = restClient.getResponseDataArray();
                        break;

                    case HttpURLConnection.HTTP_NO_CONTENT:
                        throw new Exception("No hay eventos");

                    case HttpURLConnection.HTTP_INTERNAL_ERROR:
                        throw new Exception("Error del servidor");

                    default:
                        throw new Exception("Error");
                }

                //mapping json to entity
                Gson gson = new Gson();
                List<MainCompetition> mainCompetitionList = gson.fromJson(mainCompetitionsJson.toString(), new TypeToken<List<MainCompetition>>(){}.getType());

                //saving data in result
                result.setData(mainCompetitionList);


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
        protected void onPostExecute(final ResultHandler<List<MainCompetition>> result) {

            //showProgress(false);

            if (result !=null && result.isResultValid()) {

                //TODO

                finish();
            } else {

            }
        }

        @Override
        protected void onCancelled() {

            //showProgress(false);
        }
    }

}
