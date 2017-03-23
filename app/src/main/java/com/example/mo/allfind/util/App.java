package com.example.mo.allfind.util;

import android.app.Application;


import com.example.mo.allfind.model.User;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.Map;

public class App extends Application {

	public static Map<String, Long> map;

	public static User user;

	public static final String URL = "http://115.159.193.237:8888/SXSY/";

	@Override
	public void onCreate() {
		super.onCreate();
		//开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
		Config.DEBUG = true;
		UMShareAPI.get(this);
	}
	{
		PlatformConfig.setQQZone("1106051290","r5XVAb86CXCPAgQt");
		PlatformConfig.setWeixin("wx967daebe835fbeac","5bb696d9ccd75a38c8a0bfe0675559b3");
		PlatformConfig.setSinaWeibo("1466345172", "7f13a5c935d09332f97af2f46f28f9c4","http://com.zyf.disanfanglogin");
	}
}
