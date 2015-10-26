package tecno.competitionplatform.activities;

import android.os.Bundle;
import android.app.Activity;

import tecno.competitionplatform.activities.R;

public class CompetitionListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
