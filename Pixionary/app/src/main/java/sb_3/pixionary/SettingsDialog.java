package sb_3.pixionary;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import SaveData.UserDataDBHandler;

/**
 * Created by fastn on 2/16/2018.
 */

public class SettingsDialog extends Activity implements View.OnClickListener {

    private TextView title;
    private Button[] buttons  = new Button[5];

    /**
     * Creates the buttons for the dialog and makes them all clickable.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings_dialog);
        title = (TextView) findViewById(R.id.settings_title);
        buttons[0] = (Button) findViewById(R.id.button_profile);
        buttons[1] = (Button) findViewById(R.id.button_leaderboard);
        buttons[2] = (Button) findViewById(R.id.button_blank);
        buttons[3] = (Button) findViewById(R.id.bt_DeleteAccount);
        buttons[4] = (Button) findViewById(R.id.button_logout);
        for (Button button: buttons) {
            button.setOnClickListener(this);
        }
        //Disables unused buttons for now.
        //buttons[2].setEnabled(false);
        //buttons[3].setEnabled(false);
    }

    @Override
    public void onClick(View v) {

        Class selectedActivity = null;
        int i = 0;

        switch (v.getId()) {
            case R.id.button_profile:
                i = 1;
                selectedActivity = ProfileActivity.class;
                break;
            case R.id.button_leaderboard:
                i = 2;
                selectedActivity = LeaderboardActivity.class;
                break;
            case R.id.button_blank:
                i = 3;
                //Start something here.
                break;
            case R.id.bt_DeleteAccount:
                i = 4;
                selectedActivity = DeleteAccount.class;
                //Start something here.
                break;
            case R.id.button_logout:
                i = 5;
                logOut();
                break;
        }
        if (i != 5 && i != 0) {
            nextActivity(selectedActivity);
        }
        finish();
    }

    private void logOut() {
        UserDataDBHandler db = new UserDataDBHandler(this);
        db.deleteOne(0);
        boolean set = true;
        Intent retIntent = getIntent();
        retIntent.putExtra("logout", set);
        setResult(MainMenuActivity.SETTINGS_REQUEST_ID, retIntent);
    }

    private void nextActivity(Class selected) {
        Intent intent = new Intent(this, selected);
        startActivity(intent);
    }

}
