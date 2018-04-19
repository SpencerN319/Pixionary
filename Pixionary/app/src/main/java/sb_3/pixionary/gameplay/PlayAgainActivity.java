package sb_3.pixionary.gameplay;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import sb_3.pixionary.R;

public class PlayAgainActivity extends Activity {

    private TextView timeWait;
    private TextView winnerTV;
    private Button playAgainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_play_again);
        this.setFinishOnTouchOutside(false);

        timeWait = (TextView) findViewById(R.id.tv_time_left);
        winnerTV = (TextView) findViewById(R.id.tv_winner);
        playAgainBtn = (Button) findViewById(R.id.btn_play_again);

        String response = getIntent().getStringExtra("response");
        winnerTV.setText(response);

        playAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Figure out what needs to happen to play again.

            }
        });
    }

    @Override
    public void onBackPressed() {
        //DO nothing.
    }

    private void waitDisplay() {
        new CountDownTimer(10000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeWait.setText((Integer.toString((int)millisUntilFinished/1000 + 1)));
            }

            @Override
            public void onFinish() {
                timeWait.setText("Press OK to continue");
            }
        }.start();
    }
}
