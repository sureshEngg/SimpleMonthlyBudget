package Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This class is responsible to set the Font Style of the Text through out the application.
 * Created by ${Sureshsharma} on 3/15/2016.
 */
public class FontUtility {
    public static void setCustomFont(TextView textView, String fontName, Context context) {
        Typeface customTypeface = Typeface.createFromAsset(context.getAssets(), fontName);
        textView.setTypeface(customTypeface);
    }

    public static void setCustomFont(EditText editText, String fontName, Context context) {
        Typeface customTypeface = Typeface.createFromAsset(context.getAssets(), fontName);
        editText.setTypeface(customTypeface);
    }

    public static void setCustomFont(Button editText, String fontName, Context context) {
        Typeface customTypeface = Typeface.createFromAsset(context.getAssets(), fontName);
        editText.setTypeface(customTypeface);
    }
}
