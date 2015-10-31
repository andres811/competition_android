package tecno.competitionplatform.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;

import tecno.competitionplatform.adapters.ListMainCompetitionAdapter;
import tecno.competitionplatform.classes.AlertDialogManager;
import tecno.competitionplatform.classes.RestClient;
import tecno.competitionplatform.classes.ResultHandler;
import tecno.competitionplatform.config.Config;
import tecno.competitionplatform.entities.MainCompetition;

public class MainCompetitionListActivity extends Activity {

    private ListMainCompetitionTask mListMainCompetitionTask = null;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_competition_list);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //mProgressView = findViewById(R.id.loading_progress);
        //showProgress(true);

        mDialog = new ProgressDialog(MainCompetitionListActivity.this);
        mListMainCompetitionTask = new ListMainCompetitionTask(0,10);
        mListMainCompetitionTask.execute((Void) null);

    }

/*
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
*/

    public class ListMainCompetitionTask extends AsyncTask<Void, Void, ResultHandler<List<MainCompetition>>> {

        private final int from;
        private final int to;

        public ListMainCompetitionTask(int from, int to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected void onPreExecute() {

            mDialog.setMessage("Cargando Eventos...");
            mDialog.show();

        }

        @Override
        protected ResultHandler<List<MainCompetition>> doInBackground(Void... params) {

            JSONArray mainCompetitionsJson;
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

                    case HttpURLConnection.HTTP_NOT_FOUND:
                        throw new Exception("No hay eventos");

                    case HttpURLConnection.HTTP_INTERNAL_ERROR:
                        throw new Exception("Error del servidor");

                    default:
                        throw new Exception("Error");
                }

                //mapping json to entity
               // Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
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
            mDialog.dismiss();

            if (result !=null && result.validateResponse()) {
                /*

                ArrayList<Button> buttonArrayList = new ArrayList<>();
                ArrayAdapter<Button> adapter;
                ListView listView = (ListView)findViewById(R.id.list_buttons);
                adapter=new ArrayAdapter<>(MainCompetitionListActivity.this,
                        android.R.layout.simple_list_item_1,
                        buttonArrayList);
                listView.setAdapter(adapter);


                for (MainCompetition mc: mainCompetitionList) {
                    Button btn = new Button(MainCompetitionListActivity.this);
                    btn.setText(mc.getName());
                    buttonArrayList.add(btn);
                }
                adapter.notifyDataSetChanged();
                */

                List<MainCompetition> mainCompetitionList = result.getData();

                ListView mainCompetitionListView = (ListView) findViewById(R.id.main_competition_list_view);

                // get data from the table by the ListAdapter
                ListMainCompetitionAdapter listMainCompetitionAdapter = new ListMainCompetitionAdapter(MainCompetitionListActivity.this, R.layout.main_competition_list_row, mainCompetitionList);

                listMainCompetitionAdapter.notifyDataSetChanged();
                mainCompetitionListView.setAdapter(listMainCompetitionAdapter);

                //finish();

            } else {
                AlertDialogManager.getErrorDialog(MainCompetitionListActivity.this, "Error", result.getException().getMessage(),"Volver", true);
            }
        }

        @Override
        protected void onCancelled() {

            //showProgress(false);
        }
    }

}
