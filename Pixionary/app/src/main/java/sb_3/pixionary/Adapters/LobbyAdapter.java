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

/**
 * Created by fastn on 2/19/2018.
 */

public class LobbyAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource;
    private ArrayList<String> players;

    public LobbyAdapter(Context context, int resource, ArrayList<String> players) {
        super(context, resource, players);
        this.context = context;
        this.resource = resource;
        this.players = players;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.player_name);
        tvName.setText(players.get(position));

        return convertView;
    }
}
