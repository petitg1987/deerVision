package com.urchin.release.mgt.utils;

public class AppVersionUtils {

    public static int compareVersion(String version1, String version2) {
        String[] arr1 = version1.split("\\.");
        String[] arr2 = version2.split("\\.");

        if (arr1.length < arr2.length) {
            return -1;
        }
        if (arr1.length > arr2.length) {
            return 1;
        }

        for (int i = 0; i < arr1.length; i++) {
            if(Integer.parseInt(arr1[i]) < Integer.parseInt(arr2[i])) {
                return -1;
            }
            if(Integer.parseInt(arr1[i]) > Integer.parseInt(arr2[i])) {
                return 1;
            }
        }

        return 0;
    }

}
