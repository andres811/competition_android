package tecno.competitionplatform.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;

import tecno.competitionplatform.classes.AlertDialogManager;
import tecno.competitionplatform.classes.RestClient;
import tecno.competitionplatform.classes.ResultHandler;
import tecno.competitionplatform.config.Config;
import tecno.competitionplatform.entities.Competition;
import tecno.competitionplatform.entities.Location;

public class CompetitionActivity extends BaseActivity {

    private ReadCompetitionTask mReadCompetitionTask = null;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        int competitionId = getIntent().getExtras().getInt("competitionId");
        mDialog = new ProgressDialog(CompetitionActivity.this);
        mDialog.setCanceledOnTouchOutside(false);
        mReadCompetitionTask = new ReadCompetitionTask(competitionId);
        mReadCompetitionTask.execute((Void) null);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home ) {

        }
        return super.onOptionsItemSelected(item);
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
                String url = Config.BASE_URL_SERVICES + Config.COMPETITION_SERVICE;
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
                    Gson gson = new GsonBuilder().setDateFormat(Config.GSON_DATE_FORMAT).create();
                    // Gson gson = new Gson();
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
                    TextView txtMainCompetition = (TextView)findViewById(R.id.competition_main_competition_name);
                    TextView txtLocationName = (TextView)findViewById(R.id.competition_location_name);
                    TextView txtLocationRegion = (TextView)findViewById(R.id.competition_location_region);
                    TextView txtLocationCapacity = (TextView)findViewById(R.id.competition_location_capacity);
                    TextView txtStartDate = (TextView)findViewById(R.id.competition_start_date);
                    TextView txtStartTime = (TextView)findViewById(R.id.competition_start_time);
                    GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

                    Location location = competition.getLocation();
                    LatLng locationLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                    map.setMyLocationEnabled(true);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 13));

                    map.addMarker(new MarkerOptions()
                            .title(location.getName())
                            .snippet("Capacidad: " + location.getCapacity().toString())
                            .position(locationLatLng));

                    txtTitle.setText(competition.getName());
                    txtDescription.setText(competition.getDescription());
                    txtMainCompetition.setText(competition.getMainCompetition().getName());
                    txtLocationName.setText(location.getName());
                    txtLocationRegion.setText(location.getRegion().getName());
                    txtLocationCapacity.setText(location.getCapacity().toString());

                    //date formatter
                    SimpleDateFormat sdfDate = new SimpleDateFormat(Config.VIEW_DATE_FORMAT);
                    SimpleDateFormat sdfTime= new SimpleDateFormat(Config.VIEW_TIME_FORMAT);


                    txtStartDate.setText(sdfDate.format(competition.getStartDate()).toString());
                    txtStartTime.setText(sdfTime.format(competition.getStartDate()).toString());



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



