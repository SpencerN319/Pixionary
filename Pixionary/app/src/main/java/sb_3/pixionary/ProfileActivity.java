package sb_3.pixionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import SaveData.UserDataDBHandler;
import sb_3.pixionary.Adapters.ProfileAdapter;
import sb_3.pixionary.Utilities.POJO.User;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ListView profile_list = (ListView) findViewById(R.id.list_profile);

        UserDataDBHandler db = new UserDataDBHandler(this);
        User user = db.getUser("0");

        ArrayList<String> userdata = new ArrayList<>();
        userdata.add(user.getUsername());
        userdata.add(user.getPassword());
        userdata.add(user.getId());
        userdata.add(user.getUserType());
        userdata.add(Integer.toString(user.getGamesPlayed()));
        userdata.add(Integer.toString(user.getScore()));
        userdata.add(Integer.toString(user.getCategoryCount()));
        userdata.add(Integer.toString(user.getImageCount()));

        ProfileAdapter adapter = new ProfileAdapter(this, R.layout.profile_layout, userdata);
        profile_list.setAdapter(adapter);
    }
}
