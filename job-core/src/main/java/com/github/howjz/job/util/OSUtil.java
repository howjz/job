package com.github.howjz.job.util;

/**
 *  操作系统相关
 * @author fangwk
 * @date 2018/8/28
 */
public class OSUtil {

	public static final String[] OS_PLATFORM = { "Linux", "Windows", "Mac" };

	/**
	 *  获取当前系统是哪个平台
	 *
	 * @return
	 */
	public static String getPlatform() {
		String currentOS = System.getProperty("os.name").toLowerCase();
		if (currentOS.indexOf("windows") != -1) {
			return OS_PLATFORM[1];
		} else if (currentOS.indexOf("mac") != -1) {
			return OS_PLATFORM[2];
		} else {
			return OS_PLATFORM[0];
		}
	}

	/**
	 *  判断是否是linux系统
	 * @return
	 */
	public static boolean isLinux(){
		return OS_PLATFORM[0].equalsIgnoreCase(getPlatform());
	}

	/**
	 *  判断是否是Windows系统
	 * @return
	 */
	public static boolean isWindows(){
		return OS_PLATFORM[1].equalsIgnoreCase(getPlatform());
	}

	/**
	 *  判断是否是MAC系统
	 * @return
	 */
	public static boolean isMAC(){
		return OS_PLATFORM[2].equalsIgnoreCase(getPlatform());
	}

}
