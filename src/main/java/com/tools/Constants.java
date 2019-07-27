package com.tools;

import java.io.File;

public class Constants {
    public static String EXCEL_DIR = System.getProperties().getProperty("user.home")
            + File.separator + "tools" +
            File.separator +  "excel" +
            File.separator;

    public static String BSPORT_HOST_EN = "https://bsportsfan.com";
    public static String BSPORT_HOST_CN = "https://cn.bsportsfan.com";

    public static int HTTP_TIMEOUT = 9000;

    public static final String HOLDS_TO_LOVE = "holds to love";
    public static final String HOLDS_TO = "holds to";
    public static final String BREAKS_TO = "breaks to";
    public static final String BREAKS_TO_LOVE = "breaks to love";

}
