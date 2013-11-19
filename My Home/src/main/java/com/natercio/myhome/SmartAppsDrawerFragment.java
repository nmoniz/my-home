package com.natercio.myhome;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.natercio.myhome.db.Event;
import com.natercio.myhome.profiler.GetFrequentTask;
import com.natercio.myhome.profiler.HandleEventTask;
import com.natercio.myhome.profiler.ProfilerService;
import com.natercio.myhome.utils.MyHomeLog;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 8/10/13
 * Time: 10:18 AM
 */
public class SmartAppsDrawerFragment extends Fragment {

    //private static final int RECENT_SORTING =       0b1;
    //private static final int CONTEXT_SORTING =      0b01;
    //private static final int NEWEST_SORTING =       0b001;
    private static final int FREQUENT_SORTING =     0b0001;
    private static final int NAME_SORTING =         0b00001;

    private int sortings;

    private GridLayout appsGrid;
    private ApplicationsList installedApps;
    private ApplicationsList frequentApps;

    private View rootView;
    private LayoutInflater inflater;

    private ProfilerService profilerService;
    private LocalProfilerServiceConnection serviceConnection = new LocalProfilerServiceConnection();

    private int colWidth;

    private int colsPerCard;
    private int colsPerItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.smart_apps_layout, container, false);

        this.inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        sortings = 0b00011;
        
        installedApps = new ApplicationsList(getActivity().getPackageManager());
        installedApps.loadAll();
        installedApps.sort(new ApplicationsList.TitleAscComparator());

        setupAppsGrid();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().bindService(new Intent(getActivity(), ProfilerService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        if (serviceConnection.isBounded())
            getActivity().unbindService(serviceConnection);

        super.onStop();
    }

    public void packageAdded(String packageName) {
        installedApps.load(packageName);
        installedApps.sort(new ApplicationsList.TitleAscComparator());

        bindApps();
    }


    public void packageRemoved(String packageName) {
        for (int i = 0; i < installedApps.size(); i++) {
            if (installedApps.get(i).getPackageName().equals(packageName)) {
                installedApps.remove(i);
                break;
            }
        }

        bindApps();
    }

    /////////////////////
    // PRIVATE METHODS //
    /////////////////////

    private void setupAppsGrid() {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();

        int cardCols = metrics.widthPixels / (int) (90 * metrics.scaledDensity);
        int itemCols = metrics.widthPixels / (int) (80 * metrics.scaledDensity);

        appsGrid = (GridLayout) rootView.findViewById(R.id.app_layout_container);
        appsGrid.setColumnCount(cardCols * itemCols);

        colWidth = metrics.widthPixels / appsGrid.getColumnCount();
        colsPerCard = appsGrid.getColumnCount() / cardCols;
        colsPerItem = appsGrid.getColumnCount() / itemCols;
    }

    private void loadFrequentApps(int count) {
        frequentApps = new ApplicationsList(getActivity().getPackageManager());

        GetFrequentTask getFrequentTask = new GetFrequentTask(getActivity(), count);
        profilerService.doTaskAndWait(getFrequentTask);

        for (String appTitle: getFrequentTask.getFrequent()) {
            for (ApplicationInfo app : installedApps) {
                if (app.getTitle().equals(appTitle)) {
                    frequentApps.add(app);
                    break;
                }
            }
        }

        frequentApps.sort(new ApplicationsList.TitleAscComparator());
    }

    private void bindApps() {
        appsGrid.removeAllViews();

        if ((FREQUENT_SORTING & sortings) == FREQUENT_SORTING) {
            bindFrequent();
        }

        if ((NAME_SORTING & sortings) == NAME_SORTING) {
            bindRest();
        }
    }

    private void bindRest() {
        //TODO: replace shitty code
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();

        int rowsPerItem = (int) (Math.ceil(120 * metrics.scaledDensity / colWidth));

        //add label to grid layout
        addAppSortingLabel("The other apps");

        //add the rest of the apps to grid layout
        for (ApplicationInfo app : installedApps) {
            if (frequentApps.contains(app))
                continue;

            View item;

            item = inflateAppItem(app, colsPerItem, rowsPerItem);

            item.setOnClickListener(new AppLauncher(app));
            appsGrid.addView(item);
        }
    }

    private void bindFrequent() {
        //add label to grid layout
        addAppSortingLabel(getString(R.string.sorting_frequent));

        //add frequently used apps to grid layout
        int itemCounter = 0;

        for (ApplicationInfo app : frequentApps) {
            View item;

            item = inflateAppCard(app, colsPerCard, colsPerCard);

            item.setOnClickListener(new AppLauncher(app));
            appsGrid.addView(item);

            itemCounter = (itemCounter + 1) % (frequentApps.size());

            if (itemCounter == 0) {
                break;
            }
        }
    }

    private void addAppSortingLabel(String label) {
        TextView groupLabelTextView = (TextView) inflater.inflate(R.layout.smart_apps_label, appsGrid, false);
        groupLabelTextView.setText(label);

        GridLayout.Spec rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, GridLayout.CENTER);
        GridLayout.Spec colSpec = GridLayout.spec(GridLayout.UNDEFINED, appsGrid.getColumnCount(), GridLayout.CENTER);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec);
        params.width = appsGrid.getColumnCount() * colWidth;

        groupLabelTextView.setLayoutParams(params);

        appsGrid.addView(groupLabelTextView);
    }

    private View inflateAppCard(ApplicationInfo app, int colSpan, int rowSpan) {
        GridLayout.Spec rowSpec;
        GridLayout.Spec colSpec;
        GridLayout.LayoutParams params;

        View item =  inflater.inflate(R.layout.app_card, appsGrid, false);

        ImageView img = (ImageView) item.findViewById(R.id.app_card_icon);
        img.setImageDrawable(app.getBestIcon());

        TextView txt = (TextView) item.findViewById(R.id.app_card_title);
        txt.setText(app.getTitle());

        rowSpec = GridLayout.spec(GridLayout.UNDEFINED, rowSpan, GridLayout.CENTER);
        colSpec = GridLayout.spec(GridLayout.UNDEFINED, colSpan, GridLayout.CENTER);

        params = new GridLayout.LayoutParams(rowSpec, colSpec);
        params.height = rowSpan * colWidth;
        params.width = colSpan * colWidth;

        item.setLayoutParams(params);

        return item;
    }

    private View inflateAppItem(ApplicationInfo app, int colSpan, int rowSpan) {
        GridLayout.Spec rowSpec;
        GridLayout.Spec colSpec;
        GridLayout.LayoutParams params;

        View item =  inflater.inflate(R.layout.app_item, appsGrid, false);

        ImageView img = (ImageView) item.findViewById(R.id.app_item_icon);
        img.setImageDrawable(app.getStandardIcon());

        TextView txt = (TextView) item.findViewById(R.id.app_item_title);
        txt.setText(app.getTitle());

        rowSpec = GridLayout.spec(GridLayout.UNDEFINED, rowSpan, GridLayout.CENTER);
        colSpec = GridLayout.spec(GridLayout.UNDEFINED, colSpan, GridLayout.CENTER);

        params = new GridLayout.LayoutParams(rowSpec, colSpec);
        params.height = rowSpan * colWidth;
        params.width = colSpan * colWidth;

        item.setLayoutParams(params);

        return item;
    }

    ///////////////////////
    // PROTECTED CLASSES //
    ///////////////////////

    protected final class AppLauncher implements View.OnClickListener {

        ApplicationInfo applicationInfo;

        public AppLauncher(ApplicationInfo info) {
            this.applicationInfo = info;
        }

        @Override
        public void onClick(View view) {
            startActivity(applicationInfo.getIntent());

            Event event = new Event();
            event.setAction("started");
            event.setWhat(applicationInfo.getTitle());

            profilerService.doTask(new HandleEventTask(getActivity(), event));
        }
    }

    private final class LocalProfilerServiceConnection implements ServiceConnection {

        private boolean bounded;

        private boolean isBounded() {
            return bounded;
        }

        private void setBounded(boolean bounded) {
            this.bounded = bounded;
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            setBounded(true);

            ProfilerService.ServiceBinder binder = (ProfilerService.ServiceBinder) iBinder;
            profilerService = binder.getService();

            loadFrequentApps(appsGrid.getColumnCount() / colsPerCard);

            bindApps();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            setBounded(false);
        }
    }
}
