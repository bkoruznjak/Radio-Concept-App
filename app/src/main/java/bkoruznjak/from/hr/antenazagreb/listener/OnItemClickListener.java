package bkoruznjak.from.hr.antenazagreb.listener;

import android.view.View;

/**
 * Created by bkoruznjak on 07/07/16.
 */
public interface OnItemClickListener {
    void onItemClick(View view, int position);

    void onItemLongClick(View view, int position);
}