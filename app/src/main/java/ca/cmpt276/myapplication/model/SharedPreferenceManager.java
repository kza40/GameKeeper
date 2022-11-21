package ca.cmpt276.myapplication.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ca.cmpt276.myapplication.ThemeSetting;

/**
 * The SharedPreferenceManager class provides a model for saving game configurations and games.
 */

public class SharedPreferenceManager {

    SharedPreferences mPrefs;
    private static SharedPreferenceManager sharedPreferenceManager;
    private final String PREFERENCES_FILE_NAME = "CONFIG_MANAGER_FILE";
    private final String MY_CONFIG_MANAGER ="MY_CONFIG_MANAGER";

    public SharedPreferenceManager(Context context)
    {
        mPrefs=context.getSharedPreferences(PREFERENCES_FILE_NAME, MODE_PRIVATE);
    }

    public void updateConfigManager(ConfigManager configManager)
    {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson= getGson();
        String json = gson.toJson(configManager);
        prefsEditor.putString(MY_CONFIG_MANAGER, json);
        prefsEditor.commit();
    }

    @NonNull
    private Gson getGson()
    {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public ConfigManager getConfigManager()
    {
        Gson gson=getGson();
        ConfigManager configManager;
        String json = mPrefs.getString(MY_CONFIG_MANAGER,"");
        if(!json.equals(""))
        {
            configManager=gson.fromJson(json, ConfigManager.class);
            if(configManager.getTheme()==null)
                configManager.setTheme(ThemeSetting.THEME_STAR_WARS);
            ConfigManager.setInstance(configManager);
        }
        else
        {
            configManager=ConfigManager.getInstance();
        }
        return configManager;
    }

}
