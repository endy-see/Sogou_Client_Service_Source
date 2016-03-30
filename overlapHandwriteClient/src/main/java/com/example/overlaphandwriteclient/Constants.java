package com.example.overlaphandwriteclient;

public interface Constants {
	String PREF_SETTING = "setting_pref";
	int DEFAULT_STROKE_WIDTH = 12;
	int DEFAULT_MATCH_DELAY = 800;// enlarge this delay!!!
	int DEFAULT_RECOG_SPEED = 50; 
	
	/** 画笔颜色 */
	String SETTING_COLOR = "color";

	/** 画笔粗细 */
	String SETTING_WIDTH = "width";

	/** 识别模式 */
	String SETTING_MODE = "mode";

	/** 识别文字繁简体*/
	String SETTING_VERSION = "version";

	/**用的是Gpen还是汉王**/
	String SETTING_GPEN_OR_HW = "gpenorhw";

	/** 识别范围 */
	String SETTING_TARGET = "target";

	/** 抬笔等待时间 */
	String SETTING_WATI_TIME = "time";
	
	/** 识别速度 */
	String SETTING_RECOG_SPEED = "speed";
	
	/** 实时输出结果*/
	String SETTING_REAL_TIME = "realtime";

	String CHINESE_OR_ENGLISH = "ChineseOrEnglish";
	
}
