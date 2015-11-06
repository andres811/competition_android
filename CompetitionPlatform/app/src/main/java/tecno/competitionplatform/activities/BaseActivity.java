package tecno.competitionplatform.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import tecno.competitionplatform.classes.SessionManager;

public class BaseActivity extends Activity {

    private SessionManager _sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        //mActionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        LayoutInflater mInflater = LayoutInflater.from(this);

        //View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
       // TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        //mTitleTextView.setText("My Own Title");
/*
        ImageButton imageButton = (ImageButton) mCustomView
                .findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Refresh Clicked!",
                        Toast.LENGTH_LONG).show();
            }
        });

        */


       // mActionBar.setCustomView(mCustomView);
        //mActionBar.setDisplayShowCustomEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if(getSessionManager().isLoggedIn()){
            inflater.inflate(R.menu.account_actions, menu);
        } else {

        }


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            getSessionManager().logoutUser();
            return true;
        }

        return false;
    }

    protected SessionManager getSessionManager(){
        if( _sessionManager == null){
            _sessionManager = new SessionManager(getApplicationContext());
        }
        return _sessionManager;
    }

}
