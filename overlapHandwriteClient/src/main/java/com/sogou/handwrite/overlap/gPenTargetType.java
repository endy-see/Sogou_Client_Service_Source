/*********************************************************************************************
 * Project Name: SCUT gPen Overlap Demo
 * Copyright (C), 2015~, SCUT HCII-Lab (http://www.hcii-lab.net/gpen)
 * ANY INDIVIDUAL OR LEGAL ENTITY MAY NOT USE THIS FILE WITHOUT THE AUTHORIZATION BY HCII-LAB.
 * 
 *      Model name: gPenTargetType
 *          Author: Liquan Qiu
 *         Version: 1.0
 *         Created: 20150621
 *
 *        Function: 
 *                  
 *     
 *********************************************************************************************/

package com.sogou.handwrite.overlap;

// this class is unused in this service!!!!!!!1
public class gPenTargetType {
	public static final int GPSO = 0;			// 0表示要使用gPen.so，1：要使用hw.so
	public static final int HWSO = 1;

	public static final int NO_ENGLISH = 0;		// 非英语
	public static final int IS_ENGLISH = 1;		// 英语

	// ------------------------识别方法---------------------------------------------
	// Recognize in any rotation
	public static final int APPROACH_NORMAL = 1;
	public static final int APPROACH_ROTATION = 2;
	// 重叠手写模式
	public static final int APPROACH_OVERLAP = 3;
	
	//短句输入
	public static final int APPROACH_ROW = 4;
	
	// ------------------------------------------------------------------------------------

	public static final int GPEN_VER_TRANDITIONAL = 1; // output Chinese
														// Traditional
	public static final int GPEN_VER_SIMPLIFIED = 2; // output Chinese
														// Simplified
	public static final int GPEN_VER_MIX = 3; // output Chinese Traditional and
												// Simplified
//	public static final int GPEN_VER_ENGLISH = 2; 	//output English


	
	//----------------------------错误码--------------------------------------
	public final static int AHR_INIT_ERROR = -1;
	public final static int AHR_CONF_ERROR = -2;
	public final static int AHR_RESET_ERROR = -3;
	public final static int AHR_SPLIT_ERROR = -4;
	public final static int AHR_RECOG_ERROR = -5;
	
	// ----Target
	// ranges--------------------------------------------------------------------
	public static final int GPEN_TR_UPPERCASE_SC = 0x00000001; // 全角大写英文字母
	public static final int GPEN_TR_UPPERCASE_DC = 0x00000002; // 半角大写英文字母
	public static final int GPEN_TR_LOWERCASE_SC = 0x00000004; // 全角小写英文字母
	public static final int GPEN_TR_LOWERCASE_DC = 0x00000008; // 半角小写英文字母
	public static final int GPEN_TR_NUMBER_SC = 0x00000010; // 全角数字
	public static final int GPEN_TR_NUMBER_DC = 0x00000020; // 半角数字
	public static final int GPEN_TR_PUNCTUATION_SC = 0x00000040; // 全角标点符号
	public static final int GPEN_TR_PUNCTUATION_DC = 0x00000080; // 半角标点符号
	public static final int GPEN_TR_GESTURE = 0x00000100; // 手势
	public static final int GPEN_TR_SPECIAL_CHAR = 0x00000200; // special
																// characters
	public static final int GPEN_TR_CHINESE_CHAR = 0x00000400; // Chinese
																// characters(Simplified
																// and
																// Traditional)

	public static final int GPEN_OVERLAP = 0x00100000; // 重叠手写模式
	public static final int GPEN_TEXTLINE = 0x00200000; // 文本行模式（不支持）
	public static final int GPEN_FREE = 0x00400000; // 自由书写模式（不支持）

	public static final int GPEN_TR_SPECIAL_DC = GPEN_TR_NUMBER_DC		// 半角组合
			| GPEN_TR_CHINESE_CHAR | GPEN_TR_UPPERCASE_DC
			| GPEN_TR_LOWERCASE_DC | GPEN_TR_PUNCTUATION_DC;

	public static final int GPEN_TR_ALLDC = GPEN_TR_NUMBER_DC			// 半角组合（含中文字符、含手势）
			| GPEN_TR_CHINESE_CHAR | GPEN_TR_GESTURE | GPEN_TR_UPPERCASE_DC
			| GPEN_TR_LOWERCASE_DC | GPEN_TR_PUNCTUATION_DC;

	public static final int GPEN_TR_SPECIAL_SC = GPEN_TR_NUMBER_SC		// 全角组合
			| GPEN_TR_CHINESE_CHAR | GPEN_TR_UPPERCASE_SC
			| GPEN_TR_LOWERCASE_SC | GPEN_TR_PUNCTUATION_SC;
	
	public static final int GPEN_TR_ALLSC = GPEN_TR_NUMBER_SC			// 全角组合（含手势）
			| GPEN_TR_CHINESE_CHAR | GPEN_TR_GESTURE | GPEN_TR_UPPERCASE_SC
			| GPEN_TR_LOWERCASE_SC | GPEN_TR_PUNCTUATION_SC;

	public static final int GPEN_TR_DEFAULT = GPEN_TR_NUMBER_SC			// 默认：全角数字、中文字符、手势
			| GPEN_TR_CHINESE_CHAR | GPEN_TR_GESTURE;

//	public static final int GPEN_TR_OTHER_DC = GPEN_TR_NUMBER_DC		// 其他半角组合（不含中文字符、含手势）
//			| GPEN_TR_GESTURE | GPEN_TR_UPPERCASE_DC | GPEN_TR_LOWERCASE_DC
//			| GPEN_TR_PUNCTUATION_DC;
	
	public static final int GPEN_TR_OTHER_DC = GPEN_TR_NUMBER_DC		// 其他半角组合（不含中文字符、含手势）
			| GPEN_TR_UPPERCASE_DC | GPEN_TR_LOWERCASE_DC
			| GPEN_TR_PUNCTUATION_DC;

	public static final int GPEN_TR_OTHER_DC1 = GPEN_TR_UPPERCASE_DC
			| GPEN_TR_LOWERCASE_DC | GPEN_TR_NUMBER_DC;

	public static final int GPEN_TR_OTHER_SC = GPEN_TR_NUMBER_SC		// 其他全角组合（不含中文字符、含手势）
			| GPEN_TR_GESTURE | GPEN_TR_UPPERCASE_SC | GPEN_TR_LOWERCASE_SC
			| GPEN_TR_PUNCTUATION_SC;
	// ------------------------------------------------------------------------------------
}
