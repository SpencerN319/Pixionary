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

public class ProfileAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource;
    private String[] dataLabels = { "Username: ", "Password: ", "ID: ", "User Type: ",
             "Score: ", "Games Played: " };

    public ProfileAdapter(Context context, int resource, ArrayList<String> user) {
        super(context, resource, user);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String label = dataLabels[position];
        String value = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView tvLabel = (TextView) convertView.findViewById(R.id.category_profile);
        TextView tvValue = (TextView) convertView.findViewById(R.id.value_profile);

        tvLabel.setText(label);
        tvValue.setText(value);

        return convertView;
    }
}
