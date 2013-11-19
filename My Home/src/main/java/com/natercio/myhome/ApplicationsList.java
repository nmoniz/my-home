package com.natercio.myhome;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 10/13/13
 * Time: 3:53 PM
 */

public class ApplicationsList extends ArrayList<ApplicationInfo> {

    private PackageManager packageManager;

    /////////////////////
    //  PUBLIC METHODS //
    /////////////////////

    public ApplicationsList(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    public void sort(Comparator<ApplicationInfo> comparator) {
        Collections.sort(this, comparator);
    }

    /**
     * Loads all installed apps into this ArrayList. This method will clear this ArrayList.
     */
    public void loadAll() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final List<ResolveInfo> appsList = packageManager.queryIntentActivities(mainIntent, 0);

        if (appsList != null) {
            this.clear();

            for (ResolveInfo resolveInfo : appsList) {
                ApplicationInfo appInfo = new ApplicationInfo(packageManager, resolveInfo);

                this.add(appInfo);
            }
        }
    }

    /**
     * Loads the specified apps into this ArrayList. This method may result in an unsorted list
     */
    public void load(String packageName) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final List<ResolveInfo> appsList = packageManager.queryIntentActivities(mainIntent, 0);

        if (appsList != null) {
            for (ResolveInfo resolveInfo : appsList) {
                if (resolveInfo.activityInfo.packageName.equals(packageName)) {
                    ApplicationInfo appInfo = new ApplicationInfo(packageManager, resolveInfo);
                    this.add(appInfo);
                    break;
                }
            }
        }
    }

    /////////////////////
    // PRIVATE METHODS //
    /////////////////////

    public static final class TitleAscComparator implements Comparator<ApplicationInfo> {

        @Override
        public int compare(ApplicationInfo a, ApplicationInfo b) {
            return a.getTitle().toLowerCase().compareTo(b.getTitle().toLowerCase());
        }
    }

    public static final class TitleDescComparator implements Comparator<ApplicationInfo> {

        @Override
        public int compare(ApplicationInfo a, ApplicationInfo b) {
            return b.getTitle().toLowerCase().compareTo(a.getTitle().toLowerCase());
        }
    }
}
