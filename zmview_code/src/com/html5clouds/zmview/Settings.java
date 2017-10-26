package com.html5clouds.zmview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class Settings extends SherlockPreferenceActivity
  implements SharedPreferences.OnSharedPreferenceChangeListener
{
  protected SharedPreferences prefs;
  int setting_options = 0;
  EditTextPreference zmhostPref;
  EditTextPreference zmnamePref;
  EditTextPreference zmuserPref;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    addPreferencesFromResource(R.xml.settings);
    this.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    this.zmnamePref = ((EditTextPreference)findPreference("zm_name"));
    this.zmhostPref = ((EditTextPreference)findPreference("zm_host"));
    this.zmuserPref = ((EditTextPreference)findPreference("zm_user"));
    if (getIntent().getExtras() != null)
      this.setting_options = getIntent().getExtras().getInt("setting_options");
    if (this.prefs.getString("zm_name", null) != null)
      this.zmnamePref.setSummary("URL: " + this.prefs.getString("zm_name", null));
    if (this.prefs.getString("zm_host", null) != null)
      this.zmhostPref.setSummary("URL: " + this.prefs.getString("zm_host", null));
    if (this.prefs.getString("zm_user", null) != null)
      this.zmuserPref.setSummary("Username: " + this.prefs.getString("zm_user", null));
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    paramMenu.add("Save").setIcon(2130837634).setShowAsAction(5);
    return super.onCreateOptionsMenu(paramMenu);
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    this.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    this.prefs.edit().putBoolean("config", true).commit();
    if (this.setting_options == 0)
      startActivity(new Intent(this, ZmViewActivity.class));
    if (this.setting_options == 1)
      setResult(1, new Intent());
    finish();
    if (this.setting_options == 2)
      setResult(2, new Intent());
    finish();
    return true;
  }

  protected void onPause()
  {
    super.onPause();
    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
  }

  protected void onResume()
  {
    super.onResume();
    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
  }

  public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString)
  {
    if (paramString.equals("zm_host"))
      this.zmhostPref.setSummary("URL: " + this.prefs.getString("zm_host", ""));
    if (paramString.equals("zm_user"))
      this.zmuserPref.setSummary("Username: " + this.prefs.getString("zm_user", ""));
    if (paramString.equals("zm_name"))
      this.zmnamePref.setSummary("Name: " + this.prefs.getString("zm_name", "unknown"));
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.Settings
 * JD-Core Version:    0.6.2
 */