package com.html5clouds.zmview;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Base64;
import java.util.Map;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class ObscuredSharedPreferences
  implements SharedPreferences
{
  private static final char[] SEKRIT = { 88, 49, 67, 35, 38 };
  protected static final String UTF8 = "utf-8";
  protected Context context;
  protected SharedPreferences delegate;

  public ObscuredSharedPreferences(Context paramContext, SharedPreferences paramSharedPreferences)
  {
    this.delegate = paramSharedPreferences;
    this.context = paramContext;
  }

  public boolean contains(String paramString)
  {
    return this.delegate.contains(paramString);
  }

  protected String decrypt(String paramString)
  {
    try
    {
      byte[] arrayOfByte1 = { 125, 96, 67, 95, 2, -23, -32, -82 };
      byte[] arrayOfByte2;
      if (paramString != null)
        arrayOfByte2 = Base64.decode(paramString, 2);
      //while (true)
      else
      {
        SecretKey localSecretKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(SEKRIT));
        Cipher localCipher = Cipher.getInstance("PBEWithMD5AndDES");
        localCipher.init(2, localSecretKey, new PBEParameterSpec(arrayOfByte1, 20));
        arrayOfByte2 = new byte[0];
        try
        {
          String str1 = new String(localCipher.doFinal(arrayOfByte2), "utf-8");
          return str1;
          //arrayOfByte2 = new byte[0];
        }
        catch (Exception localException2)
        { 
          localCipher.init(2, localSecretKey, new PBEParameterSpec(Settings.Secure.getString(this.context.getContentResolver(), "android_id").getBytes("utf-8"), 20));
          try
          {
            String str2 = new String(localCipher.doFinal(arrayOfByte2), "utf-8");
            return str2;
          }
          catch (Exception localException3)
          {
            return null;
          }
        }
      }
      return null;
    }
    catch (Exception localException1)
    {
      throw new RuntimeException(localException1);
    }
  }

  public Editor edit()
  {
    return new Editor();
  }

  protected String encrypt(String paramString)
  {
    try
    {
      byte[] arrayOfByte1 = { 125, 96, 67, 95, 2, -23, -32, -82 };
      if (paramString != null);
      for (byte[] arrayOfByte2 = paramString.getBytes("utf-8"); ; arrayOfByte2 = new byte[0])
      {
        SecretKey localSecretKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(SEKRIT));
        Cipher localCipher = Cipher.getInstance("PBEWithMD5AndDES");
        localCipher.init(1, localSecretKey, new PBEParameterSpec(arrayOfByte1, 20));
        return new String(Base64.encode(localCipher.doFinal(arrayOfByte2), 2), "utf-8");
      }
    }
    catch (Exception localException)
    {
      throw new RuntimeException(localException);
    }
  }

  public Map<String, ?> getAll()
  {
    throw new UnsupportedOperationException();
  }

  public boolean getBoolean(String paramString, boolean paramBoolean)
  {
    String str = this.delegate.getString(paramString, null);
    if ((str != null) && (decrypt(str) != null))
      paramBoolean = Boolean.parseBoolean(decrypt(str));
    return paramBoolean;
  }

  public float getFloat(String paramString, float paramFloat)
  {
    String str = this.delegate.getString(paramString, null);
    if ((str != null) && (decrypt(str) != null))
      paramFloat = Float.parseFloat(decrypt(str));
    return paramFloat;
  }

  public int getInt(String paramString, int paramInt)
  {
    String str = this.delegate.getString(paramString, null);
    if ((str != null) && (decrypt(str) != null))
      paramInt = Integer.parseInt(decrypt(str));
    return paramInt;
  }

  public long getLong(String paramString, long paramLong)
  {
    String str = this.delegate.getString(paramString, null);
    if ((str != null) && (decrypt(str) != null))
      paramLong = Long.parseLong(decrypt(str));
    return paramLong;
  }

  public String getString(String paramString1, String paramString2)
  {
    String str = this.delegate.getString(paramString1, null);
    if ((str != null) && (decrypt(str) != null))
      paramString2 = decrypt(str);
    return paramString2;
  }

  public Set<String> getStringSet(String paramString, Set<String> paramSet)
  {
    return null;
  }

  public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener)
  {
    this.delegate.registerOnSharedPreferenceChangeListener(paramOnSharedPreferenceChangeListener);
  }

  public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener)
  {
    this.delegate.unregisterOnSharedPreferenceChangeListener(paramOnSharedPreferenceChangeListener);
  }

  public class Editor
    implements SharedPreferences.Editor
  {
    protected SharedPreferences.Editor delegate = ObscuredSharedPreferences.this.delegate.edit();

    public Editor()
    {
    }

    public void apply()
    {
    }

    public Editor clear()
    {
      this.delegate.clear();
      return this;
    }

    public boolean commit()
    {
      return this.delegate.commit();
    }

    public Editor putBoolean(String paramString, boolean paramBoolean)
    {
      this.delegate.putString(paramString, ObscuredSharedPreferences.this.encrypt(Boolean.toString(paramBoolean)));
      return this;
    }

    public Editor putFloat(String paramString, float paramFloat)
    {
      this.delegate.putString(paramString, ObscuredSharedPreferences.this.encrypt(Float.toString(paramFloat)));
      return this;
    }

    public Editor putInt(String paramString, int paramInt)
    {
      this.delegate.putString(paramString, ObscuredSharedPreferences.this.encrypt(Integer.toString(paramInt)));
      return this;
    }

    public Editor putLong(String paramString, long paramLong)
    {
      this.delegate.putString(paramString, ObscuredSharedPreferences.this.encrypt(Long.toString(paramLong)));
      return this;
    }

    public Editor putString(String paramString1, String paramString2)
    {
      this.delegate.putString(paramString1, ObscuredSharedPreferences.this.encrypt(paramString2));
      return this;
    }

    public SharedPreferences.Editor putStringSet(String paramString, Set<String> paramSet)
    {
      return null;
    }

    public Editor remove(String paramString)
    {
      this.delegate.remove(paramString);
      return this;
    }
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.ObscuredSharedPreferences
 * JD-Core Version:    0.6.2
 */