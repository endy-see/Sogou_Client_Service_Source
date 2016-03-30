package com.example.overlaphandwriteclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hanvon.handwriting.sentence.HanwangInterface;
import com.hanvon.handwriting.sentence.RecognizerMain;
import com.sogou.handwrite.overlap.IHandwriteService;
import com.sogou.handwrite.overlap.gPenTargetType;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;


public class PrecessTask implements Runnable {


    private static final int MSG_PROCESS_HANDWRITE_DATA = 0;
    private static final int MSG_ON_STOP = 1;
    private static final int MSG_PROCESS_HW_DATA = 100;

    private Handler mHandler;
    private OverlayHandwriteListener mListener;
    private IHandwriteService mService;
    private boolean isThreadRunning;
    private boolean mDisplayRealTime = true;

    private HanwangInterface hwInterf = null;
    private Context mContext;

    public PrecessTask(OverlayHandwriteListener listener) {
        this.mListener = listener;
        isThreadRunning = false;
    }

    public void setHandWriteService(IHandwriteService service) {
        this.mService = service;
    }

    public void setHwInterface(HanwangInterface hw_intf) {
        this.hwInterf = hw_intf;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void run() {
        isThreadRunning = true;
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    handleLocalMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Looper.loop();
    }

    private void handleLocalMessage(Message msg) {
        if (!isThreadRunning) {
            return;
        }
        Log.e("PrecessTask", "-->" + msg.what);

        switch (msg.what) {
            case MSG_PROCESS_HANDWRITE_DATA:
                try {
                    int[] points = (int[]) msg.obj;
                    processHandWrite(points, msg.arg1 == 1);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case MSG_ON_STOP:
                break;

            //汉王 处理韩日文
            case MSG_PROCESS_HW_DATA:
                try {
                    int[] points = (int[]) msg.obj;
                    int mode = WritingBoardView.mPref.getInt(Constants.SETTING_MODE, RecognizerMain.Mode_CHS_Sentence);
                    int version = WritingBoardView.mPref.getInt(Constants.SETTING_VERSION, RecognizerMain.Language_KR);
                    processHw(points, msg.arg1 == 1, hwInterf, mContext, mode, version);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    private void processHandWrite(int[] points, boolean needToFlush) {
        if (mService != null) {
            try {
                int index = 0;
                while (index < points.length) {
                    int[] tmp = Arrays.copyOfRange(points, index, index + 2);
                    int ret = mService.processHandwrite(tmp);
                    if (ret != 0) {
                        mListener.onError(gPenTargetType.AHR_RECOG_ERROR);
                        return;
                    }
                    index += 2;
                }

                if (needToFlush || mDisplayRealTime) {
                    String candidateString = "";
                    List<String> resultArrayList = mService.getResult();
                    if (resultArrayList != null && resultArrayList.size() > 0 && !isEmpty(resultArrayList)) {
                        for (int i = 0; i < resultArrayList.size(); i++) {
                            if (i == resultArrayList.size() - 1) {
                                candidateString += resultArrayList.get(i);
                            } else {
                                candidateString += resultArrayList.get(i) + ", ";
                            }
                        }
                    }
                    mListener.onResult(candidateString, needToFlush);
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    }


    private void processHw(int[] points, boolean needToFlush, HanwangInterface mHWInterface, Context mContext, int mode, int version) {
        int initEngine = mHWInterface.initializeHanwangEngine(mContext,
                mode, version, RecognizerMain.Range_All);
        if (initEngine != 0) {
            Log.e("WritingBoardView", "初始化引擎失败！！！");
        }
        // 韩文、日文
        List<String> resultArrayList = new ArrayList<String>();
        short[] hwTmp = IntToShort(points);

        try {
            hwInterf.getOverlayRecognition(hwTmp, resultArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (needToFlush || mDisplayRealTime) {
            String candidateString = "";
            if (resultArrayList.size() > 0) {
                for (int i = 0; i < resultArrayList.size(); i++) {
                    if (i == resultArrayList.size() - 1) {
                        candidateString += resultArrayList.get(i);
                    } else {
                        candidateString += resultArrayList.get(i) + ", ";
                    }
                }

            } else {
                Log.e("PrecessTask:", "processHw:resultArrayList.size()=" + resultArrayList.size());
            }
            mListener.onResult(candidateString, needToFlush);
        }

    }

    public short[] IntToShort(int[] inputPoints) {
        int len = inputPoints.length;
        short[] tmpShort = new short[len];

        for (int i = 0; i < len; i++) {
            tmpShort[i] = (short) inputPoints[i];
        }

        return tmpShort;
    }

    private boolean isEmpty(List<String> result) {
        boolean ret = true;

        for (int i = 0; i < result.size(); i++) {
            if (!TextUtils.isEmpty(result.get(i).trim())) {
                ret = false;
                break;
            }
        }

        return ret;
    }

    public void setDisplayRealTime(boolean mRealTimeFlag) {
        mDisplayRealTime = mRealTimeFlag;
    }

}
