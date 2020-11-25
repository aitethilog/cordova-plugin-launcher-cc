package com.oddmouse.plugins;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

public class Launcher extends CordovaPlugin {

  public static final String IS_SET_AS_LAUNCHER = "isSetAsLauncher";
  public static final String EXIT_LAUNCHER = "exitLauncher";
  public static final String SELECT_LAUNCHER = "selectLauncher";
  public static final String REMOVE_LAUNCHER = "removeLauncher";

  private Activity activity = null;

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    try {
	  if (IS_SET_AS_LAUNCHER.equals(action)) {
        callbackContext.success(Boolean.toString(isSetAsDefaultLauncher()));
        return true;

      } else if (EXIT_LAUNCHER.equals(action)) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		Intent chooser = Intent.createChooser(intent, "Select destination...");
		if (intent.resolveActivity(cordova.getActivity().getPackageManager()) != null) {
			cordova.getActivity().startActivity(chooser);
		}
		
		callbackContext.success();
		return true;

      } else if (SELECT_LAUNCHER.equals(action)) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		cordova.getActivity().startActivity(startMain);
		
		callbackContext.success();
		return true;

      } else if (REMOVE_LAUNCHER.equals(action)) {
		activity = cordova.getActivity();
		PackageManager pkgMngr = activity.getPackageManager();
		String packageName = activity.getPackageName();
		
		if (isSetAsDefaultLauncher()) {
			pkgMngr.clearPackagePreferredActivities(activity.getPackageName());
		}
		else {
			ComponentName cn1 = new ComponentName(packageName, packageName + ".LauncherAlias1");
			ComponentName cn2 = new ComponentName(packageName, packageName + ".LauncherAlias2");
			int dis = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
			if(pkgMngr.getComponentEnabledSetting(cn1) == dis) dis = 3 - dis;
			pkgMngr.setComponentEnabledSetting(cn1, dis, PackageManager.DONT_KILL_APP);
			pkgMngr.setComponentEnabledSetting(cn2, 3 - dis, PackageManager.DONT_KILL_APP);
		}
		
		callbackContext.success();
		return true;
		
	  } else {
        callbackContext.error("The method '" + action + "' does not exist.");
        return false;

      }
    } catch (Exception e) {

      callbackContext.error(e.getMessage());
      return false;

    }
  }

	private Boolean isSetAsDefaultLauncher() {
		String myPackage = cordova.getActivity().getApplicationContext().getPackageName();
        return myPackage.equals(findLauncherPackageName());
	}

    private String findLauncherPackageName() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = this.cordova.getActivity().getPackageManager().resolveActivity(intent, 0);
        return res.activityInfo.packageName;
    }
}
