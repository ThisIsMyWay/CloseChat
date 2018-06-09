package com.closechat.closechat.imageshare;

public class Constants {

    /*
   Logging flag
  */
    public static final boolean LOGGING = true;

    /*
      Your imgur client id. You need this to upload to imgur.
      More here: https://api.imgur.com/
     */
    public static final String MY_IMGUR_CLIENT_ID = "fe4bf1df5615810";
    public static final String MY_IMGUR_CLIENT_SECRET = "280bde1a931e9759fbd1d4ee1ee0cd16218cfe04";

    /*
      Redirect URL for android.
     */
    public static final String MY_IMGUR_REDIRECT_URL = "http://android";

    /*
      Client Auth
     */
    public static String getClientAuth() {
        return "Client-ID " + MY_IMGUR_CLIENT_ID;
    }
}
