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

/**
 * Created by Andres on 28/10/2015.
 */
public class ListCompetitionAdapter extends ArrayAdapter<Competition> {

    final private Context context;

    public ListCompetitionAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public ListCompetitionAdapter(final Context context, int resource, List<Competition> items) {
        super(context, resource, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.main_competition_list_row, null);
        }

        final Competition c = getItem(position);

        if (c != null) {
            TextView name = (TextView) v.findViewById(R.id.onlist_competition_title);
            TextView description = (TextView) v.findViewById(R.id.onlist_competition_description);
            TextView btn = (Button) v.findViewById(R.id.btn_competition);


            if (name != null) {
                name.setText(c.getName());
            }

            if (description != null) {
                description.setText(c.getDescription());
            }

            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CompetitionActivity.class);
                    intent.putExtra("competitionId", c.getCompetitionId());
                    context.startActivity(intent);

                }
            });

        }

        return v;
    }

}