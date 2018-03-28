package sb_3.pixionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import sb_3.pixionary.AdminSettingsDialog.ViewUsers;


/**
 * Created by spencern319 on 3/23/18.
 */

public class AdminSettings extends Activity implements View.OnClickListener {

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
        setContentView(R.layout.admin_settings_dialog);
        title = (TextView) findViewById(R.id.settings_title);
        buttons[0] = (Button) findViewById(R.id.bt_ViewUsers);
        buttons[1] = (Button) findViewById(R.id.bt_DeleteUser);
        buttons[2] = (Button) findViewById(R.id.bt_AddImage);
        buttons[3] = (Button) findViewById(R.id.bt_RemoveImage);
        buttons[4] = (Button) findViewById(R.id.bt_RemoveCategory);
        for (Button button: buttons) {
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        Class selectedActivity = null;
        int i = 0;

        switch (v.getId()) {
            case R.id.bt_ViewUsers:
                i = 1;
                selectedActivity = ViewUsers.class;
                break;
            case R.id.bt_DeleteUser:
                i = 2;
                //TODO start delete user
                break;
            case R.id.bt_AddImage:
                i = 3;
                //TODO start add image
                break;
            case R.id.bt_RemoveImage:
                i = 4;
                //TODO start remove image
                break;
            case R.id.bt_RemoveCategory:
                i = 5;
                //TODO start remove category
                break;
        }
        if (i != 0) {
            nextActivity(selectedActivity);
        }
        finish();
    }


    private void nextActivity(Class selected) {
        Intent intent = new Intent(this, selected);
        startActivity(intent);
    }

}

