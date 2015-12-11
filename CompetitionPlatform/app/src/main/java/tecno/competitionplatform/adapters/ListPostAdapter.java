package tecno.competitionplatform.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import tecno.competitionplatform.activities.CompetitionActivity;
import tecno.competitionplatform.activities.R;
import tecno.competitionplatform.classes.Helper;
import tecno.competitionplatform.config.Config;
import tecno.competitionplatform.entities.Competition;
import tecno.competitionplatform.entities.Post;

/**
 * Created by Andres on 10/12/2015.
 */
public class ListPostAdapter extends ArrayAdapter<Post>

{

    final private Context context;

    public ListPostAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public ListPostAdapter(final Context context, int resource, List<Post> items) {
        super(context, resource, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.post_list_row, null);
        }

        final Post p = getItem(position);

        if (p != null) {

            TextView title = (TextView) v.findViewById(R.id.onlist_post_title);
            TextView createdDate = (TextView) v.findViewById(R.id.onlist_post_date);
            TextView description = (TextView) v.findViewById(R.id.onlist_post_content);



            if (title != null) {
                title.setText(p.getTitle());
            }

            if (createdDate != null) {
                createdDate.setText("Escrito el " + new SimpleDateFormat(Config.VIEW_DATE_FORMAT).format(p.getCreatedDate()));
            }

            if (description != null) {
                description.setText(Helper.getInstance().getPreviewString(p.getDescription()));
            }



        }

        return v;
    }
}
