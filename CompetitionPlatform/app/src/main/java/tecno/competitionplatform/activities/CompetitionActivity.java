package tecno.competitionplatform.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import tecno.competitionplatform.activities.R;
import tecno.competitionplatform.classes.AlertDialogManager;
import tecno.competitionplatform.classes.RestClient;
import tecno.competitionplatform.classes.ResultHandler;
import tecno.competitionplatform.config.Config;
import tecno.competitionplatform.entities.Competition;
import tecno.competitionplatform.entities.MainCompetition;

public class CompetitionActivity extends Activity {

    private ReadCompetitionTask mReadCompetitionTask = null;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        int competitionId = getIntent().getExtras().getInt("competitionId");
        mDialog = new ProgressDialog(CompetitionActivity.this);
        mReadCompetitionTask = new ReadCompetitionTask(competitionId);
        mReadCompetitionTask.execute((Void) null);
    }

    public class ReadCompetitionTask extends AsyncTask<Void, Void, ResultHandler<Competition>> {

            private final int competitionId;

            public ReadCompetitionTask(int mainCompetitionId) {
                this.competitionId = mainCompetitionId;
            }

            @Override
            protected void onPreExecute() {

                //process dialog
                mDialog.setMessage("Cargando detalle...");
                mDialog.show();
            }

            @Override
            protected ResultHandler<Competition> doInBackground(Void... params) {

                JSONObject competitionJson;
                ResultHandler<Competition> result = new ResultHandler<>();
                String url = Config.BASE_URL_SERVICES + Config.COMPETITION;
                url = url + "/" + competitionId;

                try {
                    RestClient restClient = new RestClient(url,"GET");
                    restClient.executeRequest();
                    
                    switch (restClient.getResponseCode()) {

                        case HttpURLConnection.HTTP_OK:
                            //get json response and save in json response
                            competitionJson = restClient.getResponseData();
                            break;

                        case HttpURLConnection.HTTP_NOT_FOUND:
                            throw new Exception("No hay datos");

                        case HttpURLConnection.HTTP_INTERNAL_ERROR:
                            throw new Exception("Error del servidor");

                        default:
                            throw new Exception("Error");
                    }

                    //mapping json to entity
                    Gson gson = new Gson();
                    Competition competition = gson.fromJson(competitionJson.toString(), Competition.class);

                    //saving data in result
                    result.setData(competition);


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
            protected void onPostExecute(final ResultHandler<Competition> result) {


                mDialog.dismiss();

                if (result !=null && result.validateResponse()) {

                    Competition competition = result.getData();

                    //Get layout items to set the content
                    TextView txtTitle = (TextView)findViewById(R.id.competition_title);
                    TextView txtDescription = (TextView)findViewById(R.id.competition_description);
                    TextView txtStartDate = (TextView)findViewById(R.id.competition_start_date);
                    TextView txtEndDate = (TextView)findViewById(R.id.competition_end_date);

                    txtTitle.setText(competition.getName());
                    txtDescription.setText(competition.getDescription());
                    //txtStartDate.setText(mainCompetition.getStartDate().toString());
                    //txtEndDate.setText(mainCompetition.getEndDate().toString()));



                } else {
                    AlertDialogManager.getErrorDialog(CompetitionActivity.this, "ERROR", result.getException().getMessage(), "Volver", true);
                }
            }

            @Override
            protected void onCancelled() {

                //showProgress(false);
            }
        }

    }



