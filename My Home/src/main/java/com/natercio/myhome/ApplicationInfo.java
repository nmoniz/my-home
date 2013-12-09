/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.natercio.myhome;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.natercio.myhome.utils.MyHomeLog;

/**
 * Represents an app in AllAppsView.
 */
public class ApplicationInfo {

    private String title = "";

    private String packageName = "";

    private String className = "";

    private Drawable bestIcon = null;

    private Drawable standardIcon = null;

    /**
     * The intent used to start the application.
     */
    private Intent intent;

    /**
     * The time at which the app was first installed.
     */
    private long firstInstallTime;

    ComponentName componentName;

    static final int DOWNLOADED_FLAG = 1;
    static final int UPDATED_SYSTEM_APP_FLAG = 2;

    int flags = 0;

    /**
     * Must not hold the Context.
     */
    public ApplicationInfo(PackageManager manager, ResolveInfo info) {
        final String packageName = info.activityInfo.applicationInfo.packageName;

        this.title = info.loadLabel(manager).toString();

        this.packageName = info.activityInfo.packageName;

        this.className = info.activityInfo.name;

        this.componentName = new ComponentName(packageName, this.className);
        this.setIntent(componentName,
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        try {
            int appFlags = manager.getApplicationInfo(packageName, 0).flags;
            if ((appFlags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) == 0) {
                flags |= DOWNLOADED_FLAG;

                if ((appFlags & android.content.pm.ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                    flags |= UPDATED_SYSTEM_APP_FLAG;
                }
            }
            firstInstallTime = manager.getPackageInfo(packageName, 0).firstInstallTime;
        } catch (NameNotFoundException e) {
            MyHomeLog.LogError(this.getClass().getName(), "PackageManager.getApplicationInfo failed for " + packageName);
        }

        standardIcon = info.activityInfo.loadIcon(manager);

        try {
            bestIcon = manager.getResourcesForApplication(info.activityInfo.packageName).getDrawableForDensity(info.activityInfo.applicationInfo.icon, DisplayMetrics.DENSITY_XHIGH);
        } catch (NameNotFoundException | Resources.NotFoundException e) {
            bestIcon = standardIcon;
        }
    }

    public ApplicationInfo(ApplicationInfo info) {
        componentName = info.componentName;
        title = info.title.toString();
        intent = new Intent(info.intent);
        flags = info.flags;
        firstInstallTime = info.firstInstallTime;
    }

    public String getTitle() {
        return title;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public Drawable getBestIcon() {
        return bestIcon;
    }

    public Drawable getStandardIcon() {
        return standardIcon;
    }

    public Intent getIntent() {
        return intent;
    }

    /**
     * Creates the application intent based on a component name and various launch flags.
     *
     * @param className the class name of the component representing the intent
     * @param launchFlags the launch flags
     */
    final void setIntent(ComponentName className, int launchFlags) {
        intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(className);
        intent.setFlags(launchFlags);
    }

    long getFirstInstallTime() {
        return firstInstallTime;
    }

    @Override
    public String toString() {
        return "ApplicationInfo(title=" + title.toString() + ")";
    }

}
