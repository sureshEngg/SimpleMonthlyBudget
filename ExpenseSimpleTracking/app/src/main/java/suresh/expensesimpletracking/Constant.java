package suresh.expensesimpletracking;

import android.graphics.Bitmap;

public class Constant {
	
	//public static final String URL = "http://172.16.16.4/takeaways/";
	//public static final String URL = "http://site4demo.com/takeaways/";
	public static final String URL = "http://takeout.01power.net/";
	
	//public static final String SERVER_IMAGE_PATH = "http://172.16.16.4/takeaways/sites/default/files/";
	//public static final String SERVER_IMAGE_PATH = "http://site4demo.com/takeaways/sites/default/files/";
	public static final String SERVER_IMAGE_PATH = "http://takeout.01power.net/sites/default/files/";

	public static final boolean DEVELOPMENT = false;
	public static Object latlong;
	
	public static final int FAVORITE = 101;
	public static final int LIKE = 102;
	public static final int COMMENT = 103;
	public static final int PHOTOT = 104;
	protected static final int REGISTRATION = 0;

	public static final int COUPAN = 105;
	//public static final String FACEBOOK_APPID = "336715673121893";

	//public static final String FACEBOOK_APPID = "478687865542922";
	//public static final String ACCESS_TOKEN = "ba98e4549c6743ae6c53d9a1aa57777b";
	public static final String FACEBOOK_APPID = "185378221623923";
	public static final String ACCESS_TOKEN = "89c5eeaaa3360264d5534b05fb293539";
	
	public static final String FB_PIC_URL = "http://graph.facebook.com/";

	public static final int ADD_RESTAURANT = 1000;

	public static final int REFRESH_COUPAN = 500;
	public static final int GET_COUPAN = 501;

	public static final int STARTUP = 10001;

	public static final String DATABASENAME = "BUDGET.db";
	public static final String DATABASELOCATION = "/data/data/suresh.expensesimpletracking/databases/"+DATABASENAME;

	public static final String BUDGET_TABLE = "CREATE Table budget_table (id TEXT, title TEXT, price TEXT, date TEXT, month TEXT);";

	public static final String TRIP_BUDGET_TABLE = "CREATE Table trip_budget_table (id TEXT, member_id TEXT, member_name TEXT, title TEXT, price TEXT, date TEXT);";
	
	public static final String REQUIRED_TABLE = "CREATE Table required_table (id TEXT, title TEXT);";
	
	public static final String MEMBER_TABLE = "CREATE Table member_table (id TEXT, name TEXT, contact TEXT, amount TEXT);";
	
	public static final String ALBUM_TABLE = "CREATE Table photos_table (id TEXT, path TEXT, user_id TEXT, restaurant_id, retaurant_name);";

	public static int CURRENT_FLAG = -1;

	public static Bitmap proPicBitmap = null;

	public static int requiredWidth = 480;

	public static int requiredHeight = 800;
}
