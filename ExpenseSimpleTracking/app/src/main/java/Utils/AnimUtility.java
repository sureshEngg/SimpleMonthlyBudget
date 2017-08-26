package Utils;

import android.app.Activity;

import suresh.expensesimpletracking.R;


/**
 * Activity navigation animation.
 * Created by sureshsharma on 2/23/2016.
 */
public class AnimUtility {
    public static void forwardNavigation(Activity context) {
        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void backwardNavigation(Activity context) {
        context.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static void forwardPopNavigation(Activity context) {
        context.overridePendingTransition(R.anim.slide_in_up, R.anim.exit_no_anim);
    }

    public static void backwardPopNavigation(Activity context) {
        context.overridePendingTransition(0, R.anim.slide_down);
    }
}
