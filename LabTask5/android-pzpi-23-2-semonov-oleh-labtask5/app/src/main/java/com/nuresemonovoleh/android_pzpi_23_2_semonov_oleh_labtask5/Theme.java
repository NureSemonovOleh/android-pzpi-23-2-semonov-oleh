package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_labtask5;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Theme {
    private static final String PREFS_NAME = "theme";
    private static final String KEY_THEME = "key_theme";
    private static final String KEY_FONT_SIZE = "key_font_size";

    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    public static final int THEME_SYSTEM = 2;

    public static final int FONT_SIZE_SMALL = 0;
    public static final int FONT_SIZE_MEDIUM = 1;
    public static final int FONT_SIZE_BIG = 2;

    public static void applyTheme(Context context){
        int theme = getSavedTheme(context);
        int fontSize = getSavedFontSize(context);

        switch (theme) {
            case THEME_LIGHT:
                if (fontSize == FONT_SIZE_SMALL) {
                    context.setTheme(R.style.AppTheme_Light_Small);
                } else if (fontSize == FONT_SIZE_MEDIUM) {
                    context.setTheme(R.style.AppTheme_Light_Medium);
                } else if (fontSize == FONT_SIZE_BIG) {
                    context.setTheme(R.style.AppTheme_Light_Big);
                }
                break;

            case THEME_DARK:
                if (fontSize == FONT_SIZE_SMALL) {
                    context.setTheme(R.style.AppTheme_Dark_Small);
                } else if (fontSize == FONT_SIZE_MEDIUM) {
                    context.setTheme(R.style.AppTheme_Dark_Medium);
                } else if (fontSize == FONT_SIZE_BIG) {
                    context.setTheme(R.style.AppTheme_Dark_Big);
                }
                break;

            case THEME_SYSTEM:
                int currentNight = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (currentNight == Configuration.UI_MODE_NIGHT_YES) {
                    if (fontSize == FONT_SIZE_SMALL) {
                        context.setTheme(R.style.AppTheme_Dark_Small);
                    } else if (fontSize == FONT_SIZE_MEDIUM) {
                        context.setTheme(R.style.AppTheme_Dark_Medium);
                    } else if (fontSize == FONT_SIZE_BIG) {
                        context.setTheme(R.style.AppTheme_Dark_Big);
                    }
                } else {
                    if (fontSize == FONT_SIZE_SMALL) {
                        context.setTheme(R.style.AppTheme_Light_Small);
                    } else if (fontSize == FONT_SIZE_MEDIUM) {
                        context.setTheme(R.style.AppTheme_Light_Medium);
                    } else if (fontSize == FONT_SIZE_BIG) {
                        context.setTheme(R.style.AppTheme_Light_Big);
                    }
                }
                break;


        }
    }
    public static void saveTheme(Context context, int theme){
        context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)
                .edit()
                .putInt(KEY_THEME, theme)
                .apply();
    }
    public static int getSavedTheme(Context context){
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getInt(KEY_THEME, THEME_SYSTEM);
    }


    public static void saveFontSize(Context context, int fontSize){
        context.getSharedPreferences(PREFS_NAME,context.MODE_PRIVATE)
                .edit()
                .putInt(KEY_FONT_SIZE, fontSize)
                .apply();
    }
    public static int getSavedFontSize(Context context){
        return context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE)
                .getInt(KEY_FONT_SIZE, FONT_SIZE_MEDIUM);

    }





}
