package tecno.competitionplatform.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
            v = vi.inflate(R.layout.country_list_row, null);
        }

        final Country c = getItem(position);

        if (c != null) {
            TextView nameView = (TextView) v.findViewById(R.id.onlist_country_name);
            TextView idView = (TextView) v.findViewById(R.id.onlist_country_id);


            if (nameView != null) {
                nameView.setText(c.getName());
            }

            if (idView != null) {
                idView.setText(c.getCountryId().toString());
            }

        }

        return v;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        //return  inflater.inflate( R.layout.country_list_row, parent, false);
        //return super.getDropDownView(position, convertView, parent);
        return createViewFromResource(inflater, position, convertView, parent, R.layout.country_list_row);
    }


    private View createViewFromResource(LayoutInflater inflater, int position, View convertView,
                                        ViewGroup parent, int resource) {
        View view;
        TextView text;
        int mFieldId = R.id.onlist_country_name;

        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        text = (TextView) view.findViewById(mFieldId);


        Country item = getItem(position);

        text.setText(item.getName());

        return view;
    }

}
