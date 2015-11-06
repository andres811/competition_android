package tecno.competitionplatform.classes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.content.IntentCompat;

import java.util.HashMap;

import tecno.competitionplatform.activities.LoginActivity;
import tecno.competitionplatform.activities.MainActivity;
import tecno.competitionplatform.entities.Subscriber;

/**
 * Created by Andres on 20/10/2015.
 */
public class SessionManager {
    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private Editor editor;

    // Context
    private Context _context;

    // Shared pref mode
    private int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // is logged in flag
    private static final String IS_LOGIN = "isLoggedIn";

    // User name (
    private static final String KEY_NAME = "name";

    // Email address
    private static final String KEY_EMAIL = "email";

    // User id
    private static final String ID = "id";

    // session token
    private static final String TOKEN = "token";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(Integer id, String name, String email, String token){
        // Storing user id
        editor.putInt(this.ID, id);

        // Storing login value as TRUE
        editor.putBoolean(this.IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing token in pref
        editor.putString(TOKEN, token);

        // commit changes
        editor.commit();

        // redirect him to Main Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

        _context.startActivity(i);

    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     *
    public HashMap<String, String> getUser(){
        HashMap<String, String> user = new HashMap<>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user id
        user.put(ID, pref.getString(ID, null));

        // return user
        return user;
    }
*/
    public Subscriber getUser() {
        return new Subscriber(pref.getInt(ID,0),pref.getString(KEY_EMAIL, null), pref.getString(KEY_NAME, null), pref.getString(TOKEN, null));
    }
    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Main Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}