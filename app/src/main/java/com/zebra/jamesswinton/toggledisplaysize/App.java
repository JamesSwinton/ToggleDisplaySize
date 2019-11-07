package com.zebra.jamesswinton.toggledisplaysize;

import android.app.Application;

public class App extends Application {

  // Constants
  public static final String CUSTOM_PROFILE = "CustomKeyMappings";
  public static final String CUSTOM_PROFILE_XML =
                "<wap-provisioningdoc>\n"
              + "    <characteristic type=\"Profile\">\n"
              + "      <parm name=\"ProfileName\" value=\"CustomKeyMappings\"/>\n"
              + "      <characteristic type=\"DisplayMgr\" version=\"9.2\" >\n"
              + "        <parm name=\"FontSize\" value=\"1.15\"/>\n"
              + "        <parm name=\"DisplaySize\" value=\"SMALL\"/>\n"
              + "      </characteristic>\n"
              + "    </characteristic>\n"
              + "</wap-provisioningdoc>";

  public static final String DEFAULT_PROFILE = "DefaultKeyMappings";
  public static final String DEFAULT_PROFILE_XML =
            "<wap-provisioningdoc>\n"
          + "    <characteristic type=\"Profile\">\n"
          + "      <parm name=\"ProfileName\" value=\"DefaultKeyMappings\"/>\n"
          + "      <characteristic type=\"DisplayMgr\" version=\"9.2\" >\n"
          + "        <parm name=\"FontSize\" value=\"1.0\"/>\n"
          + "        <parm name=\"DisplaySize\" value=\"DEFAULT\"/>\n"
          + "      </characteristic>\n"
          + "    </characteristic>\n"
          + "</wap-provisioningdoc>";
}
