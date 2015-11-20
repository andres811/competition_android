package tecno.competitionplatform.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;

import tecno.competitionplatform.adapters.ListCompetitionAdapter;
import tecno.competitionplatform.classes.AlertDialogManager;
import tecno.competitionplatform.classes.RestClient;
import tecno.competitionplatform.classes.ResultHandler;
import tecno.competitionplatform.config.Config;
import tecno.competitionplatform.entities.Competition;
import tecno.competitionplatform.entities.MainCompetition;


public class CompetitionListActivity extends BaseActivity {

    private ListCompetitionsTask mListCompetitionsTask = null;
    private ProgressDialog mDialog;
    private Integer mMainCompetitionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_list);

        getActionBar().setDisplayHomeAsUpEnabled(false);
        mMainCompetitionId = getIntent().getExtras().getInt("mainCompetitionId");

        Intent intent = new Intent(CompetitionListActivity.this, MainCompetitionActivity.class);
        intent.putExtra("mainCompetitionId", mMainCompetitionId);
        setResult(RESULT_OK, intent);
        mDialog = new ProgressDialog(CompetitionListActivity.this);
        mDialog.setCanceledOnTouchOutside(false);
        mListCompetitionsTask = new ListCompetitionsTask(mMainCompetitionId,0,1000);
        mListCompetitionsTask.execute((Void) null);
    }


    public class ListCompetitionsTask extends AsyncTask<Void, Void, ResultHandler<List<Competition>>> {

        private final int mcId;
        private final int from;
        private final int to;

        public ListCompetitionsTask(int mcId, int from, int to) {
            this.mcId = mcId;
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
            String url = Config.BASE_URL_SERVICES + Config.COMPETITION_SERVICE;
            url = url + "/" + mcId + "/" + from + "/" + to;

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
                        throw new Exception("No hay competencias para este evento");

                    case HttpURLConnection.HTTP_INTERNAL_ERROR:
                        throw new Exception("Error del servidor");

                    default:
                        throw new Exception("Error");
                }

                //mapping json to entity
                Gson gson = new GsonBuilder().setDateFormat(Config.GSON_DATE_FORMAT).create();
                // Gson gson = new Gson();
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


                List<Competition> competitionList = result.getData();

                ListView competitionListView = (ListView) findViewById(R.id.competition_list_view);

                // get data from the table by the ListAdapter
                ListCompetitionAdapter listCompetitionAdapter = new ListCompetitionAdapter(CompetitionListActivity.this, R.layout.competition_list_row, competitionList);

                listCompetitionAdapter.notifyDataSetChanged();
                competitionListView.setAdapter(listCompetitionAdapter);

                //finish();

            } else {
                AlertDialogManager.getErrorDialog(CompetitionListActivity.this, "Error", result.getException().getMessage(), "Volver", true);
            }
        }
    }



}
