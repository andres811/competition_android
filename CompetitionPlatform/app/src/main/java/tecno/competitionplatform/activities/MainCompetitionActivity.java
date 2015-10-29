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

import tecno.competitionplatform.classes.AlertDialogManager;
import tecno.competitionplatform.classes.RestClient;
import tecno.competitionplatform.classes.ResultHandler;
import tecno.competitionplatform.config.Config;
import tecno.competitionplatform.entities.MainCompetition;

public class MainCompetitionActivity extends Activity {

    private ReadMainCompetitionTask mReadMainCompetitionTask = null;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_competition);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        int mainCompetitionId = getIntent().getExtras().getInt("mainCompetitionId");
        mDialog = new ProgressDialog(MainCompetitionActivity.this);
        mReadMainCompetitionTask = new ReadMainCompetitionTask(mainCompetitionId);
        mReadMainCompetitionTask.execute((Void) null);
    }

    public class ReadMainCompetitionTask extends AsyncTask<Void, Void, ResultHandler<MainCompetition>> {

        private final int mainCompetitionId;

        public ReadMainCompetitionTask(int mainCompetitionId) {
            this.mainCompetitionId = mainCompetitionId;
        }

        @Override
        protected void onPreExecute() {

            //process dialog
            mDialog.setMessage("Cargando detalle...");
            mDialog.show();
        }

        @Override
        protected ResultHandler<MainCompetition> doInBackground(Void... params) {

            JSONObject mainCompetitionsJson;
            ResultHandler<MainCompetition> result = new ResultHandler<>();
            String url = Config.BASE_URL_SERVICES + Config.MAINCOMPETITION;
            url = url + "/" + mainCompetitionId;

            try {
                RestClient restClient = new RestClient(url,"GET");
                restClient.executeRequest();
                int responseCode = restClient.getResponseCode();


                switch (responseCode) {

                    case HttpURLConnection.HTTP_OK:
                        //get json response and save in json response
                        mainCompetitionsJson = restClient.getResponseData();
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
                MainCompetition mainCompetition = gson.fromJson(mainCompetitionsJson.toString(), MainCompetition.class);

                //saving data in result
                result.setData(mainCompetition);


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
        protected void onPostExecute(final ResultHandler<MainCompetition> result) {


            mDialog.dismiss();

            if (result !=null && result.validateResponse()) {

                MainCompetition mainCompetition = result.getData();

                //Get layout items to set the content
                TextView txtTitle = (TextView)findViewById(R.id.main_competition_title);
                TextView txtDescription = (TextView)findViewById(R.id.main_competition_description);
                TextView txtStartDate = (TextView)findViewById(R.id.main_competition_start_date);
                TextView txtEndDate = (TextView)findViewById(R.id.main_competition_end_date);
                Button btnListCompetitions = (Button)findViewById(R.id.main_competition_btn_competitions);

                txtTitle.setText(mainCompetition.getName());
                txtDescription.setText(mainCompetition.getDescription());
                //txtStartDate.setText(mainCompetition.getStartDate().toString());
                //txtEndDate.setText(mainCompetition.getEndDate().toString()));

                btnListCompetitions.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainCompetitionActivity.this, CompetitionListActivity.class);
                        intent.putExtra("mainCompetitionId", mainCompetitionId);
                        startActivity(intent);

                    }
                });



            } else {
                AlertDialogManager.getErrorDialog(MainCompetitionActivity.this, "ERROR", result.getException().getMessage(), "Volver", true);
            }
        }

        @Override
        protected void onCancelled() {

            //showProgress(false);
        }
    }

}


