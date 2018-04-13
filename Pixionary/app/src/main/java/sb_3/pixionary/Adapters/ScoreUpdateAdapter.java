package sb_3.pixionary.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.GameClasses.Player;

/**
 * Created by fastn on 2/19/2018.
 */

public class ScoreUpdateAdapter extends BaseAdapter implements android.widget.ListAdapter {

    private Context context;
    private int resource;
    private ArrayList<Player> userAndScore;

    public ScoreUpdateAdapter(Context context, int resource, ArrayList<Player> userAndScore) {
        this.context = context;
        this.resource = resource;
        this.userAndScore = userAndScore;
    }

    @Override
    public int getCount() {
        return userAndScore.size();
    }

    @Override
    public Object getItem(int position) {
        return userAndScore.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convert, ViewGroup parent) {
        View view;

        if (convert != null) {
           view = new View(context);
        }else {
            view = (View) convert;
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.profile_layout, null);

        TextView tvLabel = (TextView) view.findViewById(R.id.category_profile);
        TextView tvValue = (TextView) view.findViewById(R.id.value_profile);

        tvLabel.setText(userAndScore.get(position).getName());
        tvValue.setText(String.valueOf(userAndScore.get(position).getNumCorrect()));

        return view;
    }
}
