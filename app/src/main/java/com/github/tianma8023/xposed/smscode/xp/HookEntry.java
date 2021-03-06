package com.github.tianma8023.xposed.smscode.xp;

import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookEntry implements IXposedHookLoadPackage {

    private List<IHook> mHookList;
    {
        mHookList = new ArrayList<>();
        mHookList.add(new SmsHandlerHook()); // InBoundsSmsHandler Hook
        mHookList.add(new ModuleUtilsHook()); // ModuleUtils Hook
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            // Don't hook Wechat on Android 9+.
            // Because wechat will crash when open wallet for unknown reason.
            mHookList.add(new DonateWechatHook()); // Wechat donate Hook
        }
        mHookList.add(new PermissionGranterHook()); // PackageManagerService Hook
//        mHookList.add(new NotificationManagerHook()); // NotificationManager Hook
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        for(IHook hook : mHookList) {
            hook.onLoadPackage(lpparam);
        }
    }
}
