package tecno.competitionplatform.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSessionManager().isLoggedIn()) {
            findViewById(R.id.btn_login).setVisibility(View.INVISIBLE);
            findViewById(R.id.btn_subscribe).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }



    public void btnLoginClick(View v) {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void btnListMainCompetitions(View v) {
        Intent intent = new Intent(getBaseContext(), MainCompetitionListActivity.class);
        startActivity(intent);
    }
}
