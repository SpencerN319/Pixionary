package sb_3.pixionary.gameplay;

import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import java.util.ArrayList;

import sb_3.pixionary.Adapters.ScoreUpdateAdapter;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.GameClasses.Player;

public class PointUpdateActivity extends Activity {

    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_update);
        this.setFinishOnTouchOutside(false);
        activity = this;

        ArrayList<String> userAndScore = getIntent().getStringArrayListExtra("USERSANDSCORES");
        ArrayList<Player> scoreUpdate = new ArrayList<>();
        for (int i = 0; i < userAndScore.size()/2; i++) {
            scoreUpdate.add(new Player(userAndScore.get(i*2), Integer.parseInt(userAndScore.get(i*2+1))));
        }

        ListView scoresListView = (ListView) findViewById(R.id.list_user_score);
        ScoreUpdateAdapter scoreUpdateAdapter = new ScoreUpdateAdapter(this, R.layout.profile_layout, scoreUpdate);
        scoresListView.setAdapter(scoreUpdateAdapter);

        waitDisplay();

    }

    private void waitDisplay() {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 3000);
    }
}
