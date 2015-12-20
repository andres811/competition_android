package tecno.competitionplatform.activities;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tecno.competitionplatform.config.Config;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        ((EditText) findViewById(R.id.setting_url_base)).setText(Config.BASE_URL_SERVICES);
        ((EditText) findViewById(R.id.setting_secured_url_base)).setText(Config.BASE_URL_SECURE_SERVICES);
    }

    public void OnClickSaveSettings(View view) {
        String newUrl =((EditText)findViewById(R.id.setting_url_base)).getText().toString();
        String newSecuredUrl =((EditText)findViewById(R.id.setting_secured_url_base)).getText().toString();
        Config.BASE_URL_SERVICES = newUrl;
        Config.BASE_URL_SECURE_SERVICES = newSecuredUrl;
        Toast.makeText(this.getApplicationContext(), "Configuración guardada.",
                Toast.LENGTH_LONG).show();
    }
}
