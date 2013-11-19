package com.natercio.myhome;

import android.app.Activity;
import android.app.Fragment;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 10/3/13
 * Time: 4:09 PM
 */
public class WidgetDrawerFragment extends Fragment {


    static final int APPWIDGET_HOST_ID = 2037;

    private static final int REQUEST_CREATE_APPWIDGET = 5;
    private static final int REQUEST_PICK_APPWIDGET = 9;

    private AppWidgetManager appWidgetManager;
    private AppWidgetHost appWidgetHost;

    private View rootView;
    private GridLayout mainLayout;

    private ArrayList<AppWidgetHostView> widgetHostViews = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();

        rootView = inflater.inflate(R.layout.widgets_layout, container, false);
        rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                selectWidget();

                return true;
            }
        });

        mainLayout = (GridLayout) rootView.findViewById(R.id.widgets_container_layout);
        mainLayout.setColumnCount((int) Math.ceil(metrics.widthPixels / (80 * metrics.scaledDensity)));
        mainLayout.setRowCount((int) Math.ceil(metrics.heightPixels / (80 * metrics.scaledDensity)));

        appWidgetManager = AppWidgetManager.getInstance(getActivity());

        appWidgetHost = new AppWidgetHost(getActivity(), APPWIDGET_HOST_ID);
        appWidgetHost.startListening();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        for (AppWidgetHostView view : widgetHostViews)
            mainLayout.addView(view);
    }

    @Override
    public void onStop() {
        mainLayout.removeAllViews();

        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PICK_APPWIDGET) {
                configureWidget(data);
            } else if (requestCode == REQUEST_CREATE_APPWIDGET) {
                createWidget(data);
            }
        } else if (resultCode == Activity.RESULT_CANCELED && data != null) {
            int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            if (appWidgetId != -1) {
                appWidgetHost.deleteAppWidgetId(appWidgetId);
            }
        }
    }

    public void selectWidget() {
        int appWidgetId = this.appWidgetHost.allocateAppWidgetId();
        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        addEmptyData(pickIntent);
        startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);
    }

    public void createWidget(Intent data) {
        Bundle extras = data.getExtras();

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();

        int cellHeight = mainLayout.getMeasuredHeight() / mainLayout.getRowCount();
        int cellWidth = mainLayout.getMeasuredWidth() / mainLayout.getColumnCount();

        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId);

        int rowSpan = (int) Math.ceil((float) appWidgetInfo.minResizeHeight * metrics.scaledDensity / cellHeight);
        int colSpan = (int) Math.ceil((float) appWidgetInfo.minResizeWidth * metrics.scaledDensity / cellWidth);

        GridLayout.Spec rowSpec = GridLayout.spec(GridLayout.UNDEFINED, rowSpan, GridLayout.CENTER);
        GridLayout.Spec colSpec = GridLayout.spec(GridLayout.UNDEFINED, colSpan, GridLayout.CENTER);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec);
        params.height = rowSpan * cellHeight;
        params.width = colSpan * cellWidth;

        AppWidgetHostView hostView = appWidgetHost.createView(getActivity(), appWidgetId, appWidgetInfo);
        hostView.setAppWidget(appWidgetId, appWidgetInfo);
        hostView.setLayoutParams(params);

        widgetHostViews.add(hostView);

        mainLayout.addView(hostView);
    }

    private void configureWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId);
        if (appWidgetInfo.configure != null) {
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(appWidgetInfo.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
        } else {
            createWidget(data);
        }
    }

    private void addEmptyData(Intent pickIntent) {
        ArrayList<AppWidgetProviderInfo> customInfo = new ArrayList<>();
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
        ArrayList<Bundle> customExtras = new ArrayList<>();
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);
    }

}
