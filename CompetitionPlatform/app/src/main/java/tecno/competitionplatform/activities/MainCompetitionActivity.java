package tecno.competitionplatform.activities;

import android.os.Bundle;
import android.app.Activity;

import tecno.competitionplatform.activities.R;

public class MainCompetitionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_competition);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        int mainCompetitionId = getIntent().getExtras().getInt("mainCompetitionId");
    }

}
