package sb_3.pixionary.gameplay;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import sb_3.pixionary.Adapters.ScoreUpdateAdapter;
import sb_3.pixionary.R;

public class PointUpdateActivity extends AppCompatActivity {

    private ArrayList<String> userAndScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_update);

        //TODO Add something to declare the ArrayList.

        ListView scoresListView = (ListView) findViewById(R.id.list_user_score);
        ScoreUpdateAdapter scoreUpdateAdapter = new ScoreUpdateAdapter(this, R.layout.profile_layout, userAndScore);
        scoresListView.setAdapter(scoreUpdateAdapter);

        waitDisplay();

    }

    private void waitDisplay() {
        new CountDownTimer(3000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                //DO NOTHING
            }

            @Override
            public void onFinish() {
                finish();
            }
        };
    }
}
