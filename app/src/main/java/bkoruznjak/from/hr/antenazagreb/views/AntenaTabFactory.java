package bkoruznjak.from.hr.antenazagreb.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import bkoruznjak.from.hr.antenazagreb.R;

/**
 * Created by bkoruznjak on 16/07/16.
 */
public class AntenaTabFactory {

    private Context mContext;

    public AntenaTabFactory(Context context) {
        this.mContext = context;
    }

    public LinearLayout generateCustomTab(String tabTitle, Drawable tabIcon) {
        LinearLayout customTabLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.antena_tab_layout, null);
        TextView newsTabText = (TextView) customTabLayout.findViewById(R.id.tabName);
        newsTabText.setText(tabTitle);
        newsTabText.setTextColor(mContext.getResources().getColor(R.color.md_grey_100));
        ImageView newsTabIcon = (ImageView) customTabLayout.findViewById(R.id.tabIcon);
        newsTabIcon.setImageDrawable(tabIcon);
        return customTabLayout;
    }
}
