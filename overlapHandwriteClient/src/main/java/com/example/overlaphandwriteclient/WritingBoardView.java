/*********************************************************************************************
 * Project Name: SCUT gPen Overlap Demo
 * Copyright (C), 2015~, SCUT HCII-Lab (http://www.hcii-lab.net/gpen)
 * ANY INDIVIDUAL OR LEGAL ENTITY MAY NOT USE THIS FILE WITHOUT THE AUTHORIZATION BY HCII-LAB.
 * <p/>
 * Model name: Handwriting board view
 * Author: Liquan Qiu
 * Version: 1.0
 * Created: 20150621
 * <p/>
 * Function: Board view for handwriting, recognition engine will be called after
 * finishing writing, results and elapsed time will be displayed .
 *********************************************************************************************/

package com.example.overlaphandwriteclient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.hanvon.handwriting.sentence.HanwangInterface;
import com.hanvon.handwriting.sentence.RecognizerMain;
import com.sogou.handwrite.overlap.IHandwriteService;
import com.sogou.handwrite.overlap.gPenTargetType;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class WritingBoardView extends View implements Runnable, OverlayHandwriteListener {
    public static IHandwriteService mService = null;// add on 2015-07-27
    HanwangInterface mHWInterface = null;

    private static final String REMOTE_SERVICE_ACTION = "com.sogou.handwrite.overlap.HandwriteService";

    private static final String TAG = "WritingBoardView";
    private static final boolean DEBUG = true;
    private static final int POINT_MAX = 2048;

    // just for debug!!! 2015-10-29
    private static final String OUTPUT_DIR = "/sdcard/sogou_handwriting/";
    private static final String OUTPUT_FILE_NAME = "handwrite_client.result";

    private static final int MSG_ON_SHOW_RESULT = 0;
    private static final int MSG_ON_ERROR = 1;
    private static final int MSG_ON_CLEAN_LAST_RESULAT = 2;
    private FileWriter mFileWriter = null;
    private BufferedWriter mBufferedWriter = null;
    private boolean isWriting = false;

    public Paint mPaint;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint;

    private float mX, mY;

    public Handler handler;

    private boolean toClean;
    private boolean cleanLastResult;

    private String candidateString;

    // private Rect rDst;

    private Path mPath;

    // ====the result text view===============================================
    private TextView resultTextView;
    // ========================================================================

    // ====Related to gPen API==================================================
    /** an instance of gPen classifier */
    // private gPenLibHandwrite mypen; // an instance of gPen classifier

    /**
     * points of the handwriting. after each stroke, [-1,0] must be added; after
     * all the points, [-1,-1] must be added
     */
    private int[] points;

    /** total count of the "points" buf */
    private int countPoint;

    /** current count of the "points" buf unsend*/
    private int currentPoint;

    private float theRecognizeTime;

    private Context mContext;

    private int[] alpha;
    private int alpha_kinds;

    private ArrayList<Path> mPathHistory;

    private SettingActivity settingActivity;

//    @Override
//    public void onTargetChanged(int target) throws RemoteException {
//        if(settingActivity == null) {
//            settingActivity = new SettingActivity();
//            settingActivity.setTargetChangedListener(this);
//        }
//        Log.e("WritingBoardView:", "onTargetChanged:看哪个target先传过来：这里是1号");
//        mService.setTargetAndMode(target, mReconginazeMode);
//    }

    private enum WriteStatus {
        TOUCH_DOWN, TOUCH_MOVE, TOUCH_UP, CLEAR_CANVAS
    }

    private WriteStatus curStatus;

    private float mStrokeWidth = Constants.DEFAULT_STROKE_WIDTH;

    public static SharedPreferences mPref;

    private int mColor;

    private int mEndWaitTime;

    private int mReconginazeSpeed;

    private int mGpenOrHw;

    private int mReconginazeVersion;

    private int mReconginazeTarget;

    private int mReconginazeMode;

    private boolean mRealTimeFlag;

    private PrecessTask precessTask;

    // get explicit Intent, 2015-09-10
    private Intent getExplicitIntent(Context mContext, Intent mIntent) {
        Log.e("getExplicitIntent", "在getExplicitIntent中");
        if (mContext == null || mIntent == null) {
            return null;
        }

        // get all PackageManagers and find the unique service
        PackageManager mPackageManager = mContext.getPackageManager();
        List<ResolveInfo> mInfoList = mPackageManager.queryIntentServices(mIntent, 0);
        if (mInfoList == null || mInfoList.size() != 1) {
            Log.e("getExplicitIntent", "mIntent=" + mIntent);
            return null;
        }

        try {
            ResolveInfo mResolveInfo = mInfoList.get(0);
            String mPackageName = mResolveInfo.serviceInfo.packageName;
            Log.e("getExplicitIntent", ":mPackageName=" + mPackageName);
            String mClassName = mResolveInfo.serviceInfo.name;
            ComponentName mComponentName = new ComponentName(mPackageName, mClassName);
            Intent targetIntent = new Intent(mIntent);
            targetIntent.setComponent(mComponentName);
            boolean yes = targetIntent == null ? false : true;
            Log.e("getExplicitIntent", "targetIntent==null?" + yes);
            return targetIntent;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    public WritingBoardView(Context c, AttributeSet attr) {
        super(c, attr);

        mContext = c;

        mPref = mContext.getSharedPreferences(Constants.PREF_SETTING, Context.MODE_PRIVATE);
        mStrokeWidth = mPref.getInt(Constants.SETTING_WIDTH, Constants.DEFAULT_STROKE_WIDTH);
        mColor = mPref.getInt(Constants.SETTING_COLOR, Color.BLACK);
        mGpenOrHw = mPref.getInt(Constants.SETTING_GPEN_OR_HW, gPenTargetType.GPSO);
        if (mGpenOrHw == gPenTargetType.GPSO) {
            mReconginazeVersion = mPref.getInt(Constants.SETTING_VERSION, gPenTargetType.GPEN_VER_MIX);
            mReconginazeMode = mPref.getInt(Constants.SETTING_MODE, gPenTargetType.APPROACH_OVERLAP);
        } else if (mGpenOrHw == gPenTargetType.HWSO) {
            mReconginazeVersion = mPref.getInt(Constants.SETTING_VERSION, RecognizerMain.Language_KR);
            mReconginazeMode = mPref.getInt(Constants.SETTING_MODE, RecognizerMain.Mode_CHS_Sentence_Overlap_Free);
        }
        mReconginazeTarget = mPref.getInt(Constants.SETTING_TARGET, gPenTargetType.GPEN_TR_SPECIAL_DC);
        mEndWaitTime = mPref.getInt(Constants.SETTING_WATI_TIME, Constants.DEFAULT_MATCH_DELAY);
        mReconginazeSpeed = mPref.getInt(Constants.SETTING_RECOG_SPEED, Constants.DEFAULT_RECOG_SPEED);
        mRealTimeFlag = mPref.getBoolean(Constants.SETTING_REAL_TIME, true);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_ON_SHOW_RESULT:
                        showResult();
                        break;

                    case MSG_ON_CLEAN_LAST_RESULAT:
                        //clean last result on first point down
                        resultTextView.setText("");
                        break;

                    case MSG_ON_ERROR:
                        //error code
                        String errorMsg = "";
                        switch (msg.arg1) {
                            case gPenTargetType.AHR_INIT_ERROR:
                                errorMsg = "初始化识别引擎错误";
                                break;

                            case gPenTargetType.AHR_CONF_ERROR:
                                errorMsg = "配置识别模式错误";
                                break;

                            case gPenTargetType.AHR_RESET_ERROR:
                                errorMsg = "重置缓冲区错误";
                                break;

                            case gPenTargetType.AHR_SPLIT_ERROR:
                            case gPenTargetType.AHR_RECOG_ERROR:
                                errorMsg = "识别错误";
                                break;

                            default:
                                break;
                        }
                        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };

        mPaint = new Paint();

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setColor(0xFF000000);
        mBitmapPaint.setTextSize(20);

        countPoint = 0;
        currentPoint = 0;

        points = new int[POINT_MAX << 1];

        mPath = new Path();

        candidateString = "";
        cleanLastResult = false;

        alpha = getResources().getIntArray(R.array.alpha);
        alpha_kinds = alpha.length;
        mPathHistory = new ArrayList<>();

        if (DEBUG) {
            int returnValue = openOutputFile(OUTPUT_DIR, OUTPUT_FILE_NAME);
            if (returnValue < 0) {
                Toast.makeText(mContext, "Cannot store logs.", Toast.LENGTH_SHORT).show();
            }
        }

        if (mService == null) {
            Intent eIntent = new Intent();
            eIntent.setAction(REMOTE_SERVICE_ACTION);
            Intent mIntent = getExplicitIntent(c, eIntent);
            if (mIntent == null) {
                Toast.makeText(mContext, "Service does not exist.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                if (c.startService(mIntent) == null) {
                    printf("startService failed.");
                    return;
                }

                if (!c.bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE)) {
                    printf("bindService failed.");
                    return;
                }

            } catch (SecurityException e) {
                e.printStackTrace();
                printf("SecurityException occurred while calling startService or bindService.");
                return;
            }
        }
        try {
            mHWInterface = new HanwangInterface();
        } catch (Exception e) {
            Log.e(TAG, "初始化HanwangInterface失败！");
            e.printStackTrace();
        }
        Log.e(TAG, ":构造函数初始化1<<:version="+mReconginazeVersion+", target="+mReconginazeTarget+", mode="+mReconginazeMode);
        precessTask = new PrecessTask(this);
        precessTask.setDisplayRealTime(mRealTimeFlag);
        if (mHWInterface != null) {
            precessTask.setHwInterface(mHWInterface);
            precessTask.setContext(getContext());
        }
        new Thread(precessTask).start();
    }

    public void setTextView(TextView tmpResultTextView, TextView tmpScoreTextView) {
        resultTextView = tmpResultTextView;
    }

    public final void run() {
        if (toClean) {
            pushEndPoint(-1, -1);
            setData(true);
        }
    }

    /**
     * show the result, include the candidate result and the execution time.
     */
    private final void showResult() {
        if (candidateString != null && candidateString.length() > 0) {
            resultTextView.setText(candidateString + "\n");
        }

        candidateString = "";
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawColor(Color.WHITE);
        }

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        if (curStatus == WriteStatus.CLEAR_CANVAS) {
            canvas.drawColor(Color.WHITE);
            mPath.reset();
            mPathHistory.clear();
        } else {
            int mStrokeNum = mPathHistory.size();
            if (mStrokeNum > alpha_kinds) {
                mStrokeNum = alpha_kinds; // 10+ kinds
            }
            // printf("-->mStrokeNum:" + mStrokeNum);
            for (int i = mPathHistory.size() - mStrokeNum; i < mPathHistory.size(); i++) {
                // mPaint.setColor(alpha[alpha_kinds - (mPathHistory.size() -
                // i)]);
                // change color to alpha, 2015-12-31
                mPaint.setAlpha(alpha[alpha_kinds - (mPathHistory.size() - i)]);
                canvas.drawPath(mPathHistory.get(i), mPaint);
            }

            if (curStatus == WriteStatus.TOUCH_DOWN || curStatus == WriteStatus.TOUCH_UP) {

            } else if (curStatus == WriteStatus.TOUCH_MOVE) {
                mPaint.setAlpha(alpha[alpha_kinds - 1]);
                canvas.drawPath(mPath, mPaint);
            }
        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        float _x = event.getX();
        float _y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                curStatus = WriteStatus.TOUCH_DOWN;
                if (cleanLastResult) {
                    handler.obtainMessage(MSG_ON_CLEAN_LAST_RESULAT).sendToTarget();
                    cleanLastResult = false;
                }
                toClean = false;
                mPath = new Path();
                pushPoint((int) _x - 2, (int) _y - 2);
                mX = _x;
                mY = _y;
                mPath.moveTo(_x, _y);
                handler.removeCallbacks(this);
                setData(false);
                break;
            case MotionEvent.ACTION_UP:
                curStatus = WriteStatus.TOUCH_UP;
                pushPoint((int) _x, (int) _y);
                setData(false);
                pushEndPoint(-1, 0); // ***(-1,0) is added here***
                mPathHistory.add(mPath);
                toClean = true;
                handler.postDelayed(this, mEndWaitTime);
                setData(false);
                break;
            case MotionEvent.ACTION_MOVE:
                curStatus = WriteStatus.TOUCH_MOVE;
                pushPoint((int) _x, (int) _y);
                setData(false);
                mPath.quadTo(mX, mY, (_x + mX) / 2, (_y + mY) / 2);
                mX = _x;
                mY = _y;
                break;
        }
        invalidate();

        return true;
    }

    /**
     * 将数据传输至处理线程中进行处理
     * 仅在end为true或者累计到一定值时才传输
     * @param end
     */
    private void setData(boolean end) {
        mGpenOrHw = mPref.getInt(Constants.SETTING_GPEN_OR_HW, gPenTargetType.GPSO);

//        Log.i("LJC", "currentPoint is:" + currentPoint + ", countPoint is:" + (countPoint << 1));
        if (end) {
            int[] point = Arrays.copyOfRange(points, currentPoint, countPoint << 1);
            currentPoint = (countPoint << 1);
            Message msg = new Message();
            msg.what = mGpenOrHw == gPenTargetType.GPSO ? 0 : 100;
            msg.obj = point;
            msg.arg1 = 1; //means to clean board
            precessTask.getHandler().sendMessage(msg);
        } else {

            if(mGpenOrHw == gPenTargetType.GPSO) {
                Log.e("<<", "msg.what===0");
                if (countPoint > 0 && countPoint % 100 == 0) {
                    int[] point = Arrays.copyOfRange(points, currentPoint, countPoint << 1);
                    currentPoint = (countPoint << 1);
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = point;
                    msg.arg1 = 0; //means not to clean board
                    precessTask.getHandler().sendMessage(msg);
                }
            }else {
                Log.e("<<", "msg.what===100");
                if (countPoint > 0 && countPoint % 100 == 0) {
                    int[] point = Arrays.copyOfRange(points, currentPoint, countPoint << 1);
                    currentPoint = (countPoint << 1);
                    Message msg = new Message();
                    //汉王
                    msg.what = 100;
                    msg.obj = point;
                    msg.arg1 = 0; //means not to clean board
                    precessTask.getHandler().sendMessage(msg);
                }
            }
        }
    }

    private final void pushPoint(int _x, int _y) {
        if (countPoint + 2 < POINT_MAX) {
            int os = countPoint << 1;
            points[os + 0] = (short) _x;
            points[os + 1] = (short) _y;
            countPoint++;
        }
    }

    private final void pushEndPoint(int _x, int _y) {
        if (countPoint + 1 < POINT_MAX) {
            int os = countPoint << 1;
            points[os + 0] = (short) _x;
            points[os + 1] = (short) _y;
            countPoint++;
        }
    }

    public final void clean() {
        curStatus = WriteStatus.CLEAR_CANVAS;
        currentPoint = 0;
        countPoint = 0;

        if (mGpenOrHw == gPenTargetType.GPSO) {
            try {
                printf("clean result");
                int ret = mService.setTargetAndMode(mReconginazeTarget, mReconginazeMode);
                if (ret != 0) {
                    sendErrorMsg(gPenTargetType.AHR_CONF_ERROR);
                }

                ret = mService.resetResult();
                if (ret != 0) {
                    sendErrorMsg(gPenTargetType.AHR_RESET_ERROR);
                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        postInvalidate();
    }

    public void releaseClassifier() {
        int returnValue;

        if (mService != null) {
            try {
                returnValue = mService.destroyEngine();
                printf("destroyEngine, return:" + returnValue + ".");

            } catch (RemoteException e) {
                e.printStackTrace();
                printf("RemoteException occurred while calling destroyEngine.");
            }

            mContext.unbindService(mConnection);
        }

        mService = null;

        if (DEBUG) {
            while (isWriting == true) {
                // just wait, 2015-10-29
            }
            closeOutputFile();
        }
    }

    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IHandwriteService.Stub.asInterface(service);
            int returnValue = 0;

            try {
                returnValue = mService.initializeEngine();
                if (returnValue != 0) {
                    sendErrorMsg(gPenTargetType.AHR_INIT_ERROR);
                    mService = null;
                    return;
                }

                returnValue = mService.setVersion(mReconginazeVersion);
                printf("setVersion, return:" + returnValue + ".");
                if (returnValue != 0) {
                    mService = null;
                    return;
                }

                returnValue = mService.setRecogSpeed(mReconginazeSpeed);
                printf("setRecogSpeed, return:" + returnValue + ".");
                if (returnValue != 0) {
                    mService = null;
                    return;
                }

                returnValue = mService.setTargetAndMode(mReconginazeTarget, mReconginazeMode);
                printf("setTargetAndMode, return:" + returnValue + ".");
                if (returnValue != 0) {
                    sendErrorMsg(gPenTargetType.AHR_CONF_ERROR);
                    mService = null;
                    return;
                }

                precessTask.setHandWriteService(mService);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("<<", "mService = null;--->2");

                printf("RemoteException occurred while calling initializeEngine or setVersion.");
                mService = null;
                return;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("<<", "onServiceDisconnected");
            releaseClassifier();
        }
    };

    private void printf(String str) {
        if (DEBUG) {
            Log.d(TAG, str);
            final String mContent = str + "\n";
            new Thread() {
                public void run() {
                    writeOutputFile(mContent);
                }
            }.start();
        }
    }

    // just for debug!!! 2015-10-29
    private int openOutputFile(String outputDir, String outputFileName) {
        if (outputDir == null || outputDir.length() == 0 || outputFileName == null || outputFileName.length() == 0) {
            return -1;
        }

        File mFile = new File(outputDir);
        if (mFile.isDirectory() == false) {
            if (mFile.mkdirs() == false) {
                return -1;
            }
        }

        String outputFilePath = outputDir + "/" + outputFileName;

        try {
            mFileWriter = new FileWriter(outputFilePath, true);
            mBufferedWriter = new BufferedWriter(mFileWriter);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    private int closeOutputFile() {
        try {
            if (mBufferedWriter != null) {
                mBufferedWriter.close();
                mBufferedWriter = null;
            }

            if (mFileWriter != null) {
                mFileWriter.close();
                mFileWriter = null;
            }
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    private synchronized int writeOutputFile(String mContent) {
        isWriting = true;
        if (mContent == null || mContent.length() == 0 || mBufferedWriter == null) {
            isWriting = false;
            return -1;
        }

        try {
            mBufferedWriter.write(mContent);
            mBufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            isWriting = false;
            return -2;
        }

        isWriting = false;
        return 0;
    }

    public void setPaintColor(int color) {
        mPaint.setColor(color);
    }

    public void setPaintStrokeWidth(int width) {
        mStrokeWidth = width;
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    public void setReconginazeMode(int mode) {
        mReconginazeMode = mode;
    }

    public void setReconginazeTarget(int target) {
        mReconginazeTarget = target;
        try {
            mService.setTargetAndMode(target, mReconginazeMode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setWaitTime(int time) {
        mEndWaitTime = time;
    }

    public void setReconginazeSpeed(int speed) {
        mReconginazeSpeed = speed;
        if (mService != null) {
            try {
                int returnValue = mService.setRecogSpeed(speed);
                printf("setReconginazeSpeed,returnValue:" + returnValue);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void setReconginazeVersion(int version) {
        mReconginazeVersion = version;
        if (mGpenOrHw == gPenTargetType.GPSO && mService != null) {

            try {
                int returnValue = mService.setVersion(version);
                Log.e(TAG, ":setReconginazeVersion方法初始化2<<:version="+mReconginazeVersion);

                printf("setReconginazeVersion,returnValue:" + returnValue);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResult(String result, boolean clear) {
        candidateString = result;
        if (clear) {
            cleanLastResult = true;
            clean();
        }
        handler.obtainMessage(MSG_ON_SHOW_RESULT).sendToTarget();
    }

    @Override
    public void onError(int errorCode) {
        sendErrorMsg(errorCode);
    }

    private void sendErrorMsg(int errorCode) {
        Message msg = new Message();
        msg.what = MSG_ON_ERROR;
        msg.arg1 = errorCode;
        handler.sendMessage(msg);
    }

    public void setDisPlayMode(boolean realTime) {
        Log.e(TAG, "realTime=" + realTime);
        mRealTimeFlag = realTime;
        precessTask.setDisplayRealTime(mRealTimeFlag);
    }

}