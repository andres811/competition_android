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

import tecno.competitionplatform.activities.MainCompetitionActivity;
import tecno.competitionplatform.activities.R;
import tecno.competitionplatform.classes.Helper;
import tecno.competitionplatform.entities.MainCompetition;

/**
 * Created by Andres on 28/10/2015.
 */
public class ListMainCompetitionAdapter extends ArrayAdapter<MainCompetition> {

    final private Context context;

    public ListMainCompetitionAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public ListMainCompetitionAdapter(final Context context, int resource, List<MainCompetition> items) {
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

        final MainCompetition mc = getItem(position);

        if (mc != null) {
            TextView name = (TextView) v.findViewById(R.id.onlist_main_competition_title);
            TextView description = (TextView) v.findViewById(R.id.onlist_main_competition_description);
            TextView btn = (Button) v.findViewById(R.id.btn_main_competition);


            if (name != null) {
                name.setText(mc.getName());
            }

            if (description != null) {
                description.setText(Helper.getInstance().getPreviewString(mc.getDescription()));
            }

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MainCompetitionActivity.class);
                    intent.putExtra("mainCompetitionId", mc.getMainCompetitionId());
                    context.startActivity(intent);

                }
            });

        }

        return v;
    }

}