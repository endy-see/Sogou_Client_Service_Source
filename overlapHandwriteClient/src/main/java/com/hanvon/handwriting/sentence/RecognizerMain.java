package com.hanvon.handwriting.sentence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;
import android.util.Log;

/**
 * 手写识别库调用流程；
 * 	<p>1.设置识别字典SetRecogDic</p>
 * 	<p>2.创建识别句柄CreateRecHandle</p>
 * 	<p>3.设置识别范围SetRecogRange</p>
 * 	<p>4.识别Recognize</p>
 * 	<p>5.获取识别结果GetResult</p>
 * 	<p>6.释放识别句柄ReleaseRecHandle</p>
 * 	<p>7.释放识别字典DeleteRecogDic</p>
 * @author hanvon
 *
 */
public class RecognizerMain {

    private String TAG = "RecogizerMain";


    // rec language
    public static final int Language_CHS = 1;
    public static final int Language_JP = 2;
    public static final int Language_KR = 3;
    // rec mode
    public static final int Mode_CHS_SingleChar = 1; // 中文单字
    public static final int Mode_CHS_Sentence = 2; // 中文短语
    public static final int Mode_CHS_Sentence_Overlap_Free = 4; // 自由书写

    // rec range
    public static final int Range_CHS = 1;// chinese only
    public static final int Range_Eng = 2;// english only
    public static final int Range_Num = 4;// number only
    public static final int Range_Punc = 8;// punctuation only
    public static final int Range_All = 15;// all

    private long handle_rec;
    private int mode;

    private static RecognizerMain mInstance;

//    public static synchronized RecognizerMain getInstance() {其实是没啥用的
//        if (mInstance == null) {
//            mInstance = new RecognizerMain();
//        }
//        return mInstance;
//    }

    public static RecognizerMain Create(int mode, int language) {
        RecognizerMain rec = new RecognizerMain();
        rec.handle_rec = CreateRecHandle(mode, language);
        rec.mode = mode;
        return rec;
    }

    /**
     * 销毁识别对象
     *
     * @param senRec
     *            需要销毁的对象
     */
    public static void Destroy(RecognizerMain senRec) {
        ReleaseRecHandle(senRec.handle_rec);
        senRec.handle_rec = 0;
    }

    /**
     * 按指定格式返回叠写识别结果，返回结果条数
     */
    public int getOverlayRecognition(short[] inputPoints,
                                     List<String> candidateResults) {
        if (inputPoints == null || candidateResults == null
                || inputPoints.length == 0) {
            return -1;
        }

        Log.e("RecognizerMain:", "Recognize(handle_rec, inputPoints) = "+Recognize(handle_rec, inputPoints));
        if (Recognize(handle_rec, inputPoints) != 0) {
            return -1;
        }

        try {
            String[] candidateString = GetResult(handle_rec);

            int returnValue = 0;
            int realLength = 0;

            if (candidateString == null) {
                return -1;
            } else if ((returnValue = candidateString.length) == 0) {
                return 0;
            }

            for (int i = 0; i < returnValue; i++) {
                if (!candidateString[i].equals("")) {
                    candidateResults.add(candidateString[i]);
                    realLength++;
                }
            }

            return realLength;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -4;
    }

    /**
     * 暂时没有用上！！！
     * 获取识别结果
     *
     * @param trace
     *            待识别的笔迹
     * @param nRecType
     *            识别方式 1 识别所有字符； 2 识别除最后一个字符外的所有字符，优点是因为最后一个字还是书写，
     *            对其进行识别是没有意义的，不对其进行识别能减少运算，提高识别效率。缺点是最后一个字符不被识别或识别不正确。
     * @return 识别结果
     */
//    public RecResult recognize(TraceData trace, int nRecType) {
//
//        if (handle_rec == 0)
//            return null;
//        synchronized (this) {
//            short[] pts = null;
//            switch (mode) {
//                case Mode_CHS_SingleChar:
//                    // case Mode_ENG_WORD:
//                {
//                    pts = trace.GetTraceDataPts(true);
//                }
//                break;
//                case Mode_CHS_Sentence:
//                case Mode_CHS_Sentence_Overlap_Free:
//                    // case Mode_CHS_ENG_AUTO:
//                {
//                    pts = trace.GetTraceDataPts(nRecType == RecType_Total);
//
//                }
//                break;
//            }
//
//            if (Recognize(handle_rec, pts) != 0)
//                return null;
//            RecResult recResult = new RecResult();
//            recResult.setCandStr(GetResult(handle_rec));
//            // recResult.setCandLang(nativeGetResultLang(handle_rec));
//            // if(mode!=Mode_CHS_SingleChar)
//            // recResult.setCandSeg(nativeGetResultSeg(handle_rec));
//            return recResult;
//        }
//    }

    /**
     * 设置识别范围
     *
     * @param range
     *            识别范围 1 中文； 2 英文； 3 数字； 4 标点； 5全部
     * @return 状态值
     */
    public int setRecogRange(int range) {
        if (handle_rec == 0)
            return 0;
        synchronized (this) {
            int nRet = SetRecogRange(handle_rec, range);
            return nRet;
        }
    }

    /**
     * 获得识别模式
     *
     * @return 识别模式
     */
    public int getRecogMode() {
        if (handle_rec == 0)
            return 0;
        synchronized (this) {
            int nRet = GetRecogMode(handle_rec);
            return nRet;
        }
    }
    /**
     * 设置识别模式
     * @param mode	识别模式
     * @param language	识别语言
     * @return
     */


    public int setRecogMode(int mode, int language) {
        if (handle_rec == 0) {
            handle_rec = CreateRecHandle(mode, language);
            if (0 != handle_rec) {
                this.mode = mode;
                return mode;
            } else {
                return 0;
            }
        }
        ReleaseRecHandle(handle_rec);
        handle_rec = CreateRecHandle(mode, language);
        this.mode = mode;

        return mode;
    }

    /**
     * 设置系统字典
     *
     * @param dicfn
     *            字典路径
     * @return
     */
    public static boolean SetRecDic(String dicfn) {
        boolean nRet = SetRecogDic(dicfn);
        return nRet;
    }



    /**=========================手写识别native函数===============================*/
    /**
     * 设置手写识别字典
     * @param 识别字典的全路径
     * @return
     */
    private static native boolean SetRecogDic(String fn);

    /**
     * 删除手写识别字典
     */
    private static native void DeleteRecogDic();

    /**
     * 创建识别句柄
     * @param mode	识别模式
     * @param language	识别语言
     * @return 识别句柄ID
     */
    private static native long CreateRecHandle(int mode, int language);

    /**
     * 释放识别句柄
     * @param handle 识别句柄ID
     */
    private static native void ReleaseRecHandle(long handle);

    /**
     * 识别
     * @param handle	识别句柄ID
     * @param nPoint	笔迹点数组xy
     * @return
     */
    private static native int Recognize(long handle, short[] nPoint);

    /**
     * 获得候选词列表
     * @param handle	识别句柄ID
     * @return	识别结果数组
     */
    private static native String[] GetResult(long handle);

    /**
     * 设置识别范围
     * @param handle	识别句柄ID
     * @param range_value	识别范围值
     * @return
     */
    private static  native int SetRecogRange(long handle, int range_value);

    /**
     * 获取识别模式
     * @param handle	识别句柄ID
     * @return
     */
    private static native int GetRecogMode(long handle);

    static {
        System.loadLibrary("HwSentenceRec");
    }

}
