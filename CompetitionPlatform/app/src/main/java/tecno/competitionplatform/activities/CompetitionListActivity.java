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

import tecno.competitionplatform.activities.R;
import tecno.competitionplatform.adapters.ListCompetitionAdapter;
import tecno.competitionplatform.adapters.ListMainCompetitionAdapter;
import tecno.competitionplatform.classes.AlertDialogManager;
import tecno.competitionplatform.classes.RestClient;
import tecno.competitionplatform.classes.ResultHandler;
import tecno.competitionplatform.config.Config;
import tecno.competitionplatform.entities.Competition;


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
import tecno.competitionplatform.entities.Competition;


public class CompetitionListActivity extends Activity {

    private ListCompetitionsTask mListCompetitionsTask = null;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        int mainCompetitionId = getIntent().getExtras().getInt("mainCompetitionId");
        mDialog = new ProgressDialog(CompetitionListActivity.this);
        mListCompetitionsTask = new ListCompetitionsTask(0,1000);
        mListCompetitionsTask.execute((Void) null);
    }

    public class ListCompetitionsTask extends AsyncTask<Void, Void, ResultHandler<List<Competition>>> {

        private final int from;
        private final int to;

        public ListCompetitionsTask(int from, int to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected void onPreExecute() {

            mDialog.setMessage("Cargando Competencias...");
            mDialog.show();

        }

        @Override
        protected ResultHandler<List<Competition>> doInBackground(Void... params) {

            JSONArray competitionsJson;
            ResultHandler<List<Competition>> result = new ResultHandler<>();
            String url = Config.BASE_URL_SERVICES + Config.COMPETITION;
            url = url + "/" + from + "/" + to;

            try {
                RestClient restClient = new RestClient(url,"GET");
                restClient.executeRequest();
                int responseCode = restClient.getResponseCode();


                switch (responseCode) {

                    case HttpURLConnection.HTTP_OK:
                        //get json response and save in json response
                        competitionsJson = restClient.getResponseDataArray();
                        break;

                    case HttpURLConnection.HTTP_NOT_FOUND:
                        throw new Exception("No hay competencias");

                    case HttpURLConnection.HTTP_INTERNAL_ERROR:
                        throw new Exception("Error del servidor");

                    default:
                        throw new Exception("Error");
                }

                //mapping json to entity
                Gson gson = new Gson();
                List<Competition> competitionList = gson.fromJson(competitionsJson.toString(), new TypeToken<List<Competition>>(){}.getType());

                //saving data in result
                result.setData(competitionList);


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
        protected void onPostExecute(final ResultHandler<List<Competition>> result) {
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

                List<Competition> competitionList = result.getData();

                ListView competitionListView = (ListView) findViewById(R.id.main_competition_list_view);

                // get data from the table by the ListAdapter
                ListCompetitionAdapter listCompetitionAdapter = new ListCompetitionAdapter(CompetitionListActivity.this, R.layout.competition_list_row, competitionList);

                listCompetitionAdapter.notifyDataSetChanged();
                competitionListView.setAdapter(listCompetitionAdapter);

                //finish();

            } else {
                AlertDialogManager.getErrorDialog(CompetitionListActivity.this, "Error", result.getException().getMessage(), "Volver", true);
            }
        }

        @Override
        protected void onCancelled() {

            //showProgress(false);
        }
    }

}
