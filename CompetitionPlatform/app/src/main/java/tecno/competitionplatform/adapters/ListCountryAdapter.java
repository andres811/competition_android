package tecno.competitionplatform.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import tecno.competitionplatform.activities.CompetitionActivity;
import tecno.competitionplatform.activities.R;
import tecno.competitionplatform.entities.Competition;
import tecno.competitionplatform.entities.Country;

/**
 * Created by Andres on 14/11/2015.
 */
public class ListCountryAdapter extends ArrayAdapter<Country> {

    final private Context context;

    public ListCountryAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public ListCountryAdapter(final Context context, int resource, List<Country> items) {
        super(context, resource, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.competition_list_row, null);
        }

        final Country c = getItem(position);

        if (c != null) {
            TextView name = (TextView) v.findViewById(R.id.onlist_competition_title);
            TextView description = (TextView) v.findViewById(R.id.onlist_competition_description);
            TextView btn = (Button) v.findViewById(R.id.btn_competition);


            if (name != null) {
                name.setText(c.getName());
            }

            if (description != null) {
                description.setText(c.getName());
            }

            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CompetitionActivity.class);
                    intent.putExtra("competitionId", c.getName());
                    context.startActivity(intent);

                }
            });

        }

        return v;
    }

}
