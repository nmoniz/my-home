package com.natercio.myhome.utils;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 10/21/13
 * Time: 5:07 PM
 */
public class ORMLiteMyHomeDBConfigUtil extends OrmLiteConfigUtil {


    public static void main(String args[]) throws Exception {
        writeConfigFile("ormlite_config.txt");
    }
}
