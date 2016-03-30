package com.example.overlaphandwriteclient;

import com.gc.materialdesign.views.Slider;
import com.gc.materialdesign.views.Slider.OnValueChangedListener;
import com.gc.materialdesign.views.Switch;
import com.gc.materialdesign.views.Switch.OnCheckListener;
import com.gc.materialdesign.widgets.ColorSelector;
import com.gc.materialdesign.widgets.ColorSelector.OnColorSelectedListener;
import com.hanvon.handwriting.sentence.RecognizerMain;
import com.sogou.handwrite.overlap.gPenTargetType;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SettingActivity extends Activity implements OnClickListener{
    protected static final String TAG = "SettingActivity";
    private TextView modeTextView;
    private TextView versionTextView;
    private Slider widthSlider;
    private Slider timeSlider;
    private Slider speedSlider;
    private Switch realTimeSwitch;
    protected int mColor = Color.BLACK;
    private int mStrokeWidth = Constants.DEFAULT_STROKE_WIDTH;
    private SharedPreferences mPrefs;
    private int mGpenOrHw;
    private int mIsEnglish;
    private int mReconginazeMode;
    private int mReconginazeTarget;
    private int mReconginazeVersion;
    private int mEndWaitTime;
    private int mRecogSpeed;
    private boolean mRealTimeFlag;
    private boolean[] flag;

    private final int POSITION_UPPERCASE_SC = 0;
    private final int POSITION_UPPERCASE_DC = 1;
    private final int POSITION_LOWERCASE_SC = 2;
    private final int POSITION_LOWERCASE_DC = 3;
    private final int POSITION_NUMBER_SC = 4;
    private final int POSITION_NUMBER_DC = 5;
    private final int POSITION_PUNCTUATION_SC = 6;
    private final int POSITION_PUNCTUATION_DC = 7;
    private final int POSITION_SPECIAL_CHAR = 8;
    private final int POSITION_CHINESE_CHAR = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("设置");

        setContentView(R.layout.activity_settings);

        mPrefs = getSharedPreferences(Constants.PREF_SETTING, Context.MODE_PRIVATE);

        initData();
        initView();
    }

    private void initData() {
        mColor = mPrefs.getInt(Constants.SETTING_COLOR, Color.BLACK);
        mStrokeWidth = mPrefs.getInt(Constants.SETTING_WIDTH, Constants.DEFAULT_STROKE_WIDTH);
        mReconginazeTarget = mPrefs.getInt(Constants.SETTING_TARGET, gPenTargetType.GPEN_TR_SPECIAL_DC);
        mIsEnglish = mPrefs.getInt(Constants.CHINESE_OR_ENGLISH, gPenTargetType.NO_ENGLISH);
        mGpenOrHw = mPrefs.getInt(Constants.SETTING_GPEN_OR_HW, gPenTargetType.GPSO);
        if (mGpenOrHw == gPenTargetType.GPSO) {
            mReconginazeMode = mPrefs.getInt(Constants.SETTING_MODE, gPenTargetType.APPROACH_OVERLAP);
            mReconginazeVersion = mPrefs.getInt(Constants.SETTING_VERSION, gPenTargetType.GPEN_VER_MIX);    // 默认简繁体

        } else if (mGpenOrHw == gPenTargetType.HWSO) {
            mReconginazeVersion = mPrefs.getInt(Constants.SETTING_VERSION, RecognizerMain.Language_KR);
            mReconginazeMode = mPrefs.getInt(Constants.SETTING_MODE, RecognizerMain.Mode_CHS_Sentence_Overlap_Free);
        }
        mRealTimeFlag = mPrefs.getBoolean(Constants.SETTING_REAL_TIME, true);
        flag = getFlag(mReconginazeTarget);

        mEndWaitTime = mPrefs.getInt(Constants.SETTING_WATI_TIME, Constants.DEFAULT_MATCH_DELAY);

        mRecogSpeed = mPrefs.getInt(Constants.SETTING_RECOG_SPEED, Constants.DEFAULT_RECOG_SPEED);
    }

    private boolean[] getFlag(int mode) {
        return new boolean[]
                {(mode & gPenTargetType.GPEN_TR_UPPERCASE_SC) != 0,
                        (mode & gPenTargetType.GPEN_TR_UPPERCASE_DC) != 0,
                        (mode & gPenTargetType.GPEN_TR_LOWERCASE_SC) != 0,
                        (mode & gPenTargetType.GPEN_TR_LOWERCASE_DC) != 0,
                        (mode & gPenTargetType.GPEN_TR_NUMBER_SC) != 0,
                        (mode & gPenTargetType.GPEN_TR_NUMBER_DC) != 0,
                        (mode & gPenTargetType.GPEN_TR_PUNCTUATION_SC) != 0,
                        (mode & gPenTargetType.GPEN_TR_PUNCTUATION_DC) != 0,
                        (mode & gPenTargetType.GPEN_TR_SPECIAL_CHAR) != 0,
                        (mode & gPenTargetType.GPEN_TR_CHINESE_CHAR) != 0,
                };
    }

    private void initView() {
        findViewById(R.id.layout_mode).setOnClickListener(this);
        findViewById(R.id.layout_color).setOnClickListener(this);
        findViewById(R.id.layout_version).setOnClickListener(this);

        modeTextView = (TextView) findViewById(R.id.text_mode);
        if (mGpenOrHw == gPenTargetType.GPSO) {
            if (mReconginazeMode == gPenTargetType.APPROACH_NORMAL) {
                modeTextView.setText(R.string.mode_single_word);
            } else if (mReconginazeMode == gPenTargetType.APPROACH_OVERLAP) {
                modeTextView.setText(R.string.mode_overlay);
            } else {
                modeTextView.setText(R.string.mode_raw);
            }
        } else {
            if (mReconginazeMode == RecognizerMain.Mode_CHS_SingleChar) {
                modeTextView.setText(R.string.mode_single_word);
            } else if (mReconginazeMode == RecognizerMain.Mode_CHS_Sentence_Overlap_Free) {
                modeTextView.setText(R.string.mode_overlay);
            } else {
                modeTextView.setText(R.string.mode_raw);
            }
        }

        versionTextView = (TextView) findViewById(R.id.text_version);
        if (mGpenOrHw == gPenTargetType.GPSO) {        // 如果当前是gPen.so
            switch (mReconginazeVersion) {
                case gPenTargetType.GPEN_VER_TRANDITIONAL:
                    versionTextView.setText(R.string.version_tranditional);
                    break;
                case gPenTargetType.GPEN_VER_SIMPLIFIED:       //添加中英文判断
                    if(mIsEnglish == gPenTargetType.IS_ENGLISH)
                        versionTextView.setText(R.string.version_english);
                    else {
                        versionTextView.setText(R.string.version_simplified);
                    }
                    break;
                case gPenTargetType.GPEN_VER_MIX:
                    versionTextView.setText(R.string.version_mixed);
                    break;
                default:
                    versionTextView.setText(R.string.version_mixed);
                    break;
            }
        } else {
            switch (mReconginazeVersion) {
                case RecognizerMain.Language_KR:
                    versionTextView.setText(R.string.version_kr);
                    break;
                case RecognizerMain.Language_JP:
                    versionTextView.setText(R.string.version_jp);
                    break;
                default:
                    versionTextView.setText(R.string.version_kr);
                    break;
            }
        }


        widthSlider = (Slider) findViewById(R.id.width_slider);
        timeSlider = (Slider) findViewById(R.id.time_slider);
        speedSlider = (Slider) findViewById(R.id.speed_slider);

        widthSlider.setValue(mStrokeWidth);
        widthSlider.setOnValueChangedListener(new OnValueChangedListener() {

            @Override
            public void onValueChanged(int value) {
                mStrokeWidth = value;
            }
        });

        timeSlider.setValue(mEndWaitTime);
        timeSlider.setOnValueChangedListener(new OnValueChangedListener() {

            @Override
            public void onValueChanged(int value) {
                mEndWaitTime = value;
            }
        });

        speedSlider.setValue(mRecogSpeed);
        speedSlider.setOnValueChangedListener(new OnValueChangedListener() {

            @Override
            public void onValueChanged(int value) {
                mRecogSpeed = value;
            }
        });

        realTimeSwitch = (Switch) findViewById(R.id.switch_real_time);
        realTimeSwitch.setChecked(mRealTimeFlag);
        realTimeSwitch.setOncheckListener(new OnCheckListener() {

            @Override
            public void onCheck(boolean check) {
                mRealTimeFlag = check;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        Log.e("SettingActivtiy", ":按下返回键，能否带回设置的数据呢");
        saveData();

        Intent i = new Intent();
        i.putExtra(Constants.SETTING_COLOR, mColor);
        i.putExtra(Constants.SETTING_WIDTH, mStrokeWidth);
        i.putExtra(Constants.SETTING_MODE, mReconginazeMode);
        i.putExtra(Constants.SETTING_GPEN_OR_HW, mGpenOrHw);
        i.putExtra(Constants.SETTING_VERSION, mReconginazeVersion);
        i.putExtra(Constants.CHINESE_OR_ENGLISH, mIsEnglish);
        i.putExtra(Constants.SETTING_TARGET, mReconginazeTarget);
        i.putExtra(Constants.SETTING_WATI_TIME, mEndWaitTime);
        i.putExtra(Constants.SETTING_RECOG_SPEED, mRecogSpeed);
        i.putExtra(Constants.SETTING_REAL_TIME, mRealTimeFlag);
        setResult(RESULT_OK, i);
        this.finish();
        super.onBackPressed();
    }

    private void saveData() {
        mPrefs.edit().putBoolean(Constants.SETTING_REAL_TIME, mRealTimeFlag).commit();
        mPrefs.edit().putInt(Constants.SETTING_COLOR, mColor).commit();
        mPrefs.edit().putInt(Constants.SETTING_WIDTH, mStrokeWidth).commit();
        mPrefs.edit().putInt(Constants.SETTING_MODE, mReconginazeMode).commit();
        mPrefs.edit().putInt(Constants.SETTING_VERSION, mReconginazeVersion).commit();
        mPrefs.edit().putInt(Constants.SETTING_GPEN_OR_HW, mGpenOrHw).commit();
        mPrefs.edit().putInt(Constants.CHINESE_OR_ENGLISH, mIsEnglish).commit();
//		mPrefs.edit().putInt(Constants.SETTING_TARGET, mReconginazeTarget).commit();
        mPrefs.edit().putInt(Constants.SETTING_WATI_TIME, mEndWaitTime).commit();
        mPrefs.edit().putInt(Constants.SETTING_RECOG_SPEED, mRecogSpeed).commit();
        Log.e("SettingActivity:", "设置的结果：mRecoginazeVersion="+mReconginazeVersion+
                ", mRegTarget="+mReconginazeTarget+", 英文半角的="+gPenTargetType.GPEN_TR_OTHER_DC1);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_mode:
                setMode();
                break;

            case R.id.layout_color:
                setColor();
                break;

            case R.id.layout_version:
                setVersion();
//                try {
//                    Log.e("SettingActivity:", "myTargetListener="+myTargetListener);
//                    myTargetListener.onTargetChanged(mReconginazeTarget);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }

                break;

            default:
                break;
        }
    }

    private void setTarget() {
        flag = getFlag(mReconginazeTarget);

        new AlertDialog.Builder(this).setTitle("识别范围").setIcon(android.R.drawable.ic_dialog_info)
                .setMultiChoiceItems(R.array.target, flag, new OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        mReconginazeTarget = mReconginazeTarget ^ getTargetNum(which);        // 将原来的mReconfinazeTarget与选择的TargetNum做异或运算，得到新的mReconfinazeTarget
                        mPrefs.edit().putInt(Constants.SETTING_TARGET, mReconginazeTarget).commit();
                    }
                }).setNegativeButton("确定", null).show();
    }

    protected int getTargetNum(int which) {
        int ret = 0;
        switch (which) {
            case POSITION_UPPERCASE_SC:
                ret = gPenTargetType.GPEN_TR_UPPERCASE_SC;
                break;

            case POSITION_UPPERCASE_DC:
                ret = gPenTargetType.GPEN_TR_UPPERCASE_DC;
                break;

            case POSITION_LOWERCASE_SC:
                ret = gPenTargetType.GPEN_TR_LOWERCASE_SC;
                break;

            case POSITION_LOWERCASE_DC:
                ret = gPenTargetType.GPEN_TR_LOWERCASE_DC;
                break;

            case POSITION_NUMBER_SC:
                ret = gPenTargetType.GPEN_TR_NUMBER_SC;
                break;

            case POSITION_NUMBER_DC:
                ret = gPenTargetType.GPEN_TR_NUMBER_DC;
                break;

            case POSITION_PUNCTUATION_SC:
                ret = gPenTargetType.GPEN_TR_PUNCTUATION_SC;
                break;

            case POSITION_PUNCTUATION_DC:
                ret = gPenTargetType.GPEN_TR_PUNCTUATION_DC;
                break;

            case POSITION_SPECIAL_CHAR:
                ret = gPenTargetType.GPEN_TR_SPECIAL_CHAR;
                break;

            case POSITION_CHINESE_CHAR:
                ret = gPenTargetType.GPEN_TR_CHINESE_CHAR;
                break;

            default:
                break;
        }
        return ret;
    }

    private void setColor() {
        ColorSelector colorSelector = new ColorSelector(this, mColor, new OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                mColor = color;
                Log.i("TAG", "onColorSelected:current color is:" + color);
            }

        });
        colorSelector.show();
    }

    private void setVersion() {
        int choicedItemIndex = getVersionPosition();
        new AlertDialog.Builder(this).setTitle("语言选择").setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(
                        new String[]{getString(R.string.version_tranditional), getString(R.string.version_simplified),
                                getString(R.string.version_mixed), getString(R.string.version_english),
                                getString(R.string.version_kr), getString(R.string.version_jp)},
                        choicedItemIndex, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 3 ) {
                                    mReconginazeTarget = gPenTargetType.GPEN_TR_OTHER_DC1;
                                    mPrefs.edit().putInt(Constants.SETTING_TARGET, gPenTargetType.GPEN_TR_OTHER_DC1).commit();
                                } else {
                                    mReconginazeTarget = gPenTargetType.GPEN_TR_SPECIAL_DC;
                                    mPrefs.edit().putInt(Constants.SETTING_TARGET, gPenTargetType.GPEN_TR_SPECIAL_DC).commit();
                                }
                                changeVersion(which);
                                dialog.dismiss();
                            }

                        })
                .setNegativeButton("取消", null).show();
    }

    private void setMode() {
        int choicedItemIndex = getModePosition();
        new AlertDialog.Builder(this).setTitle("识别模式").setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(
                        new String[]{getString(R.string.mode_single_word), getString(R.string.mode_overlay),
                                getString(R.string.mode_raw)},
                        choicedItemIndex, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                changeMode(which);
                                dialog.dismiss();
                            }

                        })
                .setNegativeButton("取消", null).show();
    }

    private int getVersionPosition() {
        if (mGpenOrHw == gPenTargetType.GPSO) {
            switch (mReconginazeVersion) {
                case gPenTargetType.GPEN_VER_TRANDITIONAL:
                    return 0;

                case gPenTargetType.GPEN_VER_SIMPLIFIED:
                    if (mIsEnglish == gPenTargetType.IS_ENGLISH)
                        return 3;
                    else
                        return 1;

                case gPenTargetType.GPEN_VER_MIX:
                    return 2;

                default:
                    break;
            }
        } else {
            switch (mReconginazeVersion) {

                case RecognizerMain.Language_KR:
                    return 4;

                case RecognizerMain.Language_JP:
                    return 5;
                default:
                    break;
            }
        }

        return -1;
    }

    private int getModePosition() {
        if (mGpenOrHw == gPenTargetType.GPSO) {
            switch (mReconginazeMode) {
                case gPenTargetType.APPROACH_NORMAL:
                    return 0;

                case gPenTargetType.APPROACH_OVERLAP:
                    return 1;

                case gPenTargetType.APPROACH_ROW:
                    return 2;
                default:
                    break;
            }
        } else if (mGpenOrHw == gPenTargetType.HWSO) {
            switch (mReconginazeMode) {
                case RecognizerMain.Mode_CHS_SingleChar:
                    return 0;
                case RecognizerMain.Mode_CHS_Sentence_Overlap_Free:
                    return 1;
                case RecognizerMain.Mode_CHS_Sentence:
                    return 2;
                default:
                    break;
            }
        }
        return -1;
    }

    private void changeVersion(int which) {
        switch (which) {
            case 0:
                mGpenOrHw = gPenTargetType.GPSO;
                mIsEnglish = gPenTargetType.NO_ENGLISH;
                mReconginazeVersion = gPenTargetType.GPEN_VER_TRANDITIONAL;
                // 请选择识别范围
                setTarget();
                versionTextView.setText(getString(R.string.version_tranditional));
                break;

            case 1:
                mGpenOrHw = gPenTargetType.GPSO;
                mIsEnglish = gPenTargetType.NO_ENGLISH;
                mReconginazeVersion = gPenTargetType.GPEN_VER_SIMPLIFIED;
                setTarget();
                versionTextView.setText(getString(R.string.version_simplified));
                break;

            case 2:
                mGpenOrHw = gPenTargetType.GPSO;
                mIsEnglish = gPenTargetType.NO_ENGLISH;
                mReconginazeVersion = gPenTargetType.GPEN_VER_MIX;
                setTarget();
                versionTextView.setText(getString(R.string.version_mixed));
                break;

            case 3:
                mGpenOrHw = gPenTargetType.GPSO;
                mIsEnglish = gPenTargetType.IS_ENGLISH;
                mReconginazeVersion = gPenTargetType.GPEN_VER_SIMPLIFIED;    // 英文同样适用中文简体
                versionTextView.setText(getString(R.string.version_english));
                break;
            case 4:
                mGpenOrHw = gPenTargetType.HWSO;
                mReconginazeVersion = RecognizerMain.Language_KR;
                versionTextView.setText(getString(R.string.version_kr));

                break;
            case 5:
                mGpenOrHw = gPenTargetType.HWSO;
                mReconginazeVersion = RecognizerMain.Language_JP;
                versionTextView.setText(getString(R.string.version_jp));
                break;
            default:
                break;
        }

        setOverLap();

    }

    private void setOverLap() {

        mPrefs.edit().putInt(Constants.SETTING_MODE, mGpenOrHw == gPenTargetType.HWSO ? RecognizerMain.Mode_CHS_Sentence_Overlap_Free : gPenTargetType.APPROACH_OVERLAP).commit();
        mReconginazeMode = mGpenOrHw == gPenTargetType.HWSO ? RecognizerMain.Mode_CHS_Sentence_Overlap_Free : gPenTargetType.APPROACH_OVERLAP;
        modeTextView.setText(getString(R.string.mode_overlay));
    }

    private void changeMode(int which) {
        Log.i(TAG, "current mode is:" + which);
        if (mGpenOrHw == gPenTargetType.GPSO) {
            switch (which) {
                case 0:
                    mReconginazeMode = gPenTargetType.APPROACH_NORMAL;
                    modeTextView.setText(getString(R.string.mode_single_word));
                    break;

                case 1:
                    mReconginazeMode = gPenTargetType.APPROACH_OVERLAP;
                    modeTextView.setText(getString(R.string.mode_overlay));
                    break;

                case 2:
                    mReconginazeMode = gPenTargetType.APPROACH_ROW;
                    modeTextView.setText(getString(R.string.mode_raw));
                    break;

                default:
                    break;
            }
        } else if (mGpenOrHw == gPenTargetType.HWSO) {
            switch (which) {
                case 0:
                    mReconginazeMode = RecognizerMain.Mode_CHS_SingleChar;
                    modeTextView.setText(getString(R.string.mode_single_word));
                    break;
                case 1:
                    mReconginazeMode = RecognizerMain.Mode_CHS_Sentence_Overlap_Free;
                    modeTextView.setText(getString(R.string.mode_overlay));
                    break;
                case 2:
                    mReconginazeMode = RecognizerMain.Mode_CHS_Sentence;
                    modeTextView.setText(getString(R.string.mode_raw));
                    break;
            }
        }
    }

//    interface MyTargetListener {
//        void onTargetChanged(int target) throws RemoteException;
//    }
}
