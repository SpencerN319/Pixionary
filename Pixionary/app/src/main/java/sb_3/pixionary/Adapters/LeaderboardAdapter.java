package sb_3.pixionary.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.ShortUser;


/**
 * Created by fastn on 3/8/2018.
 */

public class LeaderboardAdapter extends ArrayAdapter<ShortUser> {

    private Context context;
    private int resource;

    public LeaderboardAdapter(Context context, int resource, ArrayList<ShortUser> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //User info we will use
        ShortUser user = getItem(position);
        String username = user.getUsername();
        int score = user.getScore();
        String scoreStr = String.valueOf(score);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView tvUser = (TextView) convertView.findViewById(R.id.username_display);
        TextView tvScore = (TextView) convertView.findViewById(R.id.score_display);

        tvUser.setText(username);
        tvScore.setText(scoreStr);

        return convertView;
    }

}
