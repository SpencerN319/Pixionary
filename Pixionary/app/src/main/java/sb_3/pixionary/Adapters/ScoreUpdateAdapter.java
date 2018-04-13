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

public class ScoreUpdateAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource;
    private ArrayList<String> userAndScore;

    public ScoreUpdateAdapter(Context context, int resource, ArrayList<String> userAndScore) {
        super(context, resource, userAndScore);
        this.context = context;
        this.resource = resource;
        this.userAndScore = userAndScore;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String label = userAndScore.get(position*2);
        String value = userAndScore.get(position*2+1);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView tvLabel = (TextView) convertView.findViewById(R.id.category_profile);
        TextView tvValue = (TextView) convertView.findViewById(R.id.value_profile);

        tvLabel.setText(label);
        tvValue.setText(value);

        return convertView;
    }
}
