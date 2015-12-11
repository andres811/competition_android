package tecno.competitionplatform.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import tecno.competitionplatform.adapters.ListMainCompetitionAdapter;
import tecno.competitionplatform.adapters.ListPostAdapter;
import tecno.competitionplatform.classes.AlertDialogManager;
import tecno.competitionplatform.classes.Helper;
import tecno.competitionplatform.classes.RestClient;
import tecno.competitionplatform.classes.ResultHandler;
import tecno.competitionplatform.config.Config;
import tecno.competitionplatform.entities.MainCompetition;
import tecno.competitionplatform.entities.Post;

public class MainCompetitionActivity extends BaseActivity {

    //async tasks
    private ReadMainCompetitionTask mReadMainCompetitionTask = null;
    private ListPostsTask mlistPostsTask = null;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_competition);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        int mainCompetitionId = getIntent().getExtras().getInt("mainCompetitionId");
        mDialog = new ProgressDialog(MainCompetitionActivity.this);
        mDialog.setCanceledOnTouchOutside(false);
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
            mDialog.setMessage("Cargando detalle del evento...");
            mDialog.show();
        }

        @Override
        protected ResultHandler<MainCompetition> doInBackground(Void... params) {

            JSONObject mainCompetitionsJson;
            ResultHandler<MainCompetition> result = new ResultHandler<>();
            String url = Config.BASE_URL_SERVICES + Config.MAINCOMPETITION_SERVICE;
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

                //date format gson config
                Gson gson = new GsonBuilder().setDateFormat(Config.GSON_DATE_FORMAT).create();

                //mapping json to entity
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


            if (result !=null && result.validateResponse()) {

                MainCompetition mainCompetition = result.getData();

                //Get layout items to set the content
                TextView txtTitle = (TextView)findViewById(R.id.main_competition_title);
                TextView txtDescription = (TextView)findViewById(R.id.main_competition_description);
                TextView txtStartDate = (TextView)findViewById(R.id.main_competition_start_date);
                TextView txtEndDate = (TextView)findViewById(R.id.main_competition_end_date);
                Button btnListCompetitions = (Button)findViewById(R.id.main_competition_btn_competitions);

                //date formatter
                SimpleDateFormat sdf = new SimpleDateFormat(Config.VIEW_DATE_FORMAT);

                //setting data in the view
                txtTitle.setText(mainCompetition.getName());
                txtDescription.setText(mainCompetition.getDescription());
                txtStartDate.setText(sdf.format(mainCompetition.getStartDate()).toString());
                txtEndDate.setText(sdf.format(mainCompetition.getEndDate()).toString());





                btnListCompetitions.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        /*
                        Intent intent = new Intent(MainCompetitionActivity.this, CompetitionListActivity.class);
                        intent.putExtra("mainCompetitionId", mainCompetitionId);
                        startActivity(intent);
                        */

                        Intent intent = new Intent(MainCompetitionActivity.this, CompetitionListActivity.class);
                        intent.putExtra("mainCompetitionId", mainCompetitionId);
                        startActivityForResult(intent, 1);


                    }
                });


                //now lets list the posts
                mlistPostsTask = new ListPostsTask(mainCompetitionId);
                mlistPostsTask.execute((Void) null);


            } else {
                AlertDialogManager.getErrorDialog(MainCompetitionActivity.this, "ERROR", result.getException().getMessage(), "Volver", true);
            }
        }

        @Override
        protected void onCancelled() {

            //showProgress(false);
        }
    }

    public class ListPostsTask extends AsyncTask<Void, Void, ResultHandler<List<Post>>> {

        private final int mainCompetitionId;

        public ListPostsTask(int mainCompetitionId) {
            this.mainCompetitionId = mainCompetitionId;
        }

        @Override
        protected void onPreExecute() {

            //process dialog
            mDialog.setMessage("Cargando noticias...");

        }

        @Override
        protected ResultHandler<List<Post>> doInBackground(Void... params) {

            JSONArray postJsonArray;
            ResultHandler<List<Post>> result = new ResultHandler<>();
            String url = Config.BASE_URL_SERVICES + Config.POST_BY_MAIN_COMPETITION_SERVICE;
            url = url + "/" + mainCompetitionId;

            try {
                RestClient restClient = new RestClient(url);
                restClient.executeRequest();

                switch (restClient.getResponseCode()) {

                    case HttpURLConnection.HTTP_OK:
                        //get json response and save in json response
                        postJsonArray = restClient.getResponseDataArray();

                        //date format gson config
                        Gson gson = new GsonBuilder().setDateFormat(Config.GSON_DATE_FORMAT).create();

                        //mapping jsonarray to entity list
                        List<Post> postList = gson.fromJson(postJsonArray.toString(), new TypeToken<List<Post>>() {}.getType());

                        //saving data in result
                        result.setData(postList);

                        break;

                    case HttpURLConnection.HTTP_NOT_FOUND:
                        //Do nothing!
                        break;

                    case HttpURLConnection.HTTP_INTERNAL_ERROR:
                        throw new Exception("Error del servidor");

                    default:
                        throw new Exception("Error");
                }




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
        protected void onPostExecute(final ResultHandler<List<Post>> result) {


            mDialog.dismiss();

            if (result !=null && result.validateResponse()) {

                if(result.getData() != null) {

                    findViewById(R.id.last_posts_title).setVisibility(View.VISIBLE);

                    List<Post> postList = result.getData();

                    Collections.reverse(postList);


                    ListView postListView = (ListView) findViewById(R.id.main_competition_posts_list_view);

                    //get data from the table by the ListAdapter
                    ListPostAdapter listPostAdapter = new ListPostAdapter(MainCompetitionActivity.this, R.layout.post_list_row, postList);

                    //listPostAdapter.notifyDataSetChanged();
                    postListView.setAdapter(listPostAdapter);

                    // the magic
                    Helper.setListViewHeightBasedOnChildren(postListView);

                }




            } else {
                AlertDialogManager.getErrorDialog(MainCompetitionActivity.this, "ERROR", result.getException().getMessage(), "Volver", true);
            }
        }

        @Override
        protected void onCancelled() {

            //showProgress(false);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String stredittext=data.getStringExtra("edittextvalue");
            }
        }

    }
}



