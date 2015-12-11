package tecno.competitionplatform.classes;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import tecno.competitionplatform.config.Config;

/**
 * Created by Andres on 19/11/2015.
 */
public class Helper {

    private static Helper instance = null;

    protected Helper() {
        // Exists only to defeat instantiation.
    }
    public static Helper getInstance() {
        if(instance == null) {
            instance = new Helper();
        }
        return instance;
    }

    public String getPreviewString(String string) {
        if(string.length() > Config.PREVIEW_STRING_LENGTH ){
            return string.substring(0, Config.PREVIEW_STRING_LENGTH) + "...";
        }
        return string;

    }


    //Fix the list view height when its inside a scroll layout
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 40;
        listView.setLayoutParams(params);
    }

}
