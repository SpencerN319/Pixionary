package sb_3.pixionary.Utilities;

import android.os.AsyncTask;

import okhttp3.WebSocket;
import sb_3.pixionary.Utilities.POJO.GameClasses.Bot;

/**
 * Created by fastn on 4/19/2018.
 */

public class BotTask extends AsyncTask<Void, Void, String> {

    public Bot bot;
    private WebSocket webSocket;


    public BotTask(Bot bot) {
        this.bot = bot;
        this.webSocket = webSocket;
    }

    @Override
    protected String doInBackground(Void...voids) {
        int guessTime = (120 - (8*bot.getDifficulty())) / bot.getWord_list().size();
//        while (!gotCorrect) {
//            try {
//                Thread.sleep(guessTime);
//                String guess = bot.guess();
//                webSocket.send("Bot Says:" + guess);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return null;
    }
}
