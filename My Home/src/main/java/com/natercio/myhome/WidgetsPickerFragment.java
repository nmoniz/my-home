package com.natercio.myhome;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 8/8/13
 * Time: 3:01 PM
 */
public class WidgetsPickerFragment extends Fragment {

    private ListView widgetsListView = null;
    private ArrayList<AppWidgetProviderInfo> installedWidgets;
    private WidgetsAdapter widgetsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.widgets_picker_layout, container, false);

        widgetsListView = (ListView) rootView.findViewById(R.id.widgets_list);

        loadWidgets(true);

        bindWidgets();

        return rootView;
    }


    /////////////////////
    // PRIVATE METHODS //
    /////////////////////

    private void loadWidgets(boolean isLaunching) {
        if (isLaunching && installedWidgets != null) {
            return;
        }

        AppWidgetManager widgetManager = AppWidgetManager.getInstance(getActivity());

        final List<AppWidgetProviderInfo> widgetsList = widgetManager.getInstalledProviders();

        //TODO: sort widgets

        if (widgetsList != null) {
            final int count = widgetsList.size();

            if (installedWidgets == null) {
                installedWidgets = new ArrayList<AppWidgetProviderInfo>(count);
            }

            installedWidgets.clear();


            for (int i = 0; i < count; i++) {
                installedWidgets.add(widgetsList.get(i));
            }
        }
    }

    private void bindWidgets() {
        widgetsAdapter = new WidgetsAdapter(this.getActivity(), installedWidgets);

        widgetsListView.setAdapter(widgetsAdapter);

        //TODO: setup listeners
    }

    /////////////////////
    // PRIVATE CLASSES //
    /////////////////////

    private class WidgetsAdapter extends ArrayAdapter<AppWidgetProviderInfo> {

        private ArrayList<AppWidgetProviderInfo> widgets;

        private WidgetsAdapter(Context context, ArrayList<AppWidgetProviderInfo> objects) {
            super(context, 0, objects);

            widgets = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final AppWidgetProviderInfo widget = widgets.get(position);

            View view = convertView;

            if (convertView == null) {
                final LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.widget_item, null);
            }

            if (widget != null) {
                TextView nameTextView = (TextView) view.findViewById(R.id.widget_item_title);

                if (nameTextView != null)
                    nameTextView.setText(widget.label);

                TextView sizeTextView = (TextView) view.findViewById(R.id.widget_item_size);


                if (sizeTextView != null)
                    sizeTextView.setText(widget.minWidth + " x " + widget.minHeight);

                ImageView previewImageView = (ImageView) view.findViewById(R.id.widget_item_preview);

                if (previewImageView != null) {
                    int resouceToLoadId = (widget.previewImage != 0) ? widget.previewImage : widget.icon;

                    try {
                        previewImageView.setImageDrawable(getContext().getPackageManager().getResourcesForApplication(widget.provider.getPackageName()).getDrawable(resouceToLoadId));
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            return view;
        }
    }

}