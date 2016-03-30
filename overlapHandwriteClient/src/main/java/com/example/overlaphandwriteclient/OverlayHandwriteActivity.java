package com.example.overlaphandwriteclient;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;

import com.hanvon.handwriting.sentence.RecognizerMain;
import com.sogou.handwrite.overlap.gPenTargetType;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;

public class OverlayHandwriteActivity extends Activity {
	private static final int REQUEST_CODE_SETTING = 0;
	private WritingBoardView writeView;
	TextView scoreTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_overlay_handwrite);
		TextView resultTextView = (TextView) findViewById(R.id.textview);
		resultTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
		scoreTextView = (TextView) findViewById(R.id.scoretextview);
		writeView = (WritingBoardView) findViewById(R.id.writingview);
		writeView.setTextView(resultTextView, scoreTextView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.overlay_handwrite, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			settings();
			break;

		default:
			break;
		}
		
		return true;
	}
	
	private void settings() {
		Intent i = new Intent(this, SettingActivity.class);
		startActivityForResult(i, REQUEST_CODE_SETTING);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE_SETTING:
			if (resultCode == RESULT_OK) {
				changeSettings(data);
			}
			break;

		default:
			break;
		} 
	}

	private void changeSettings(Intent data) {
		if (data != null) {
			int color = data.getIntExtra(Constants.SETTING_COLOR, Color.BLACK);
			writeView.setPaintColor(color);
			
			int strokeWidth = data.getIntExtra(Constants.SETTING_WIDTH, Constants.DEFAULT_STROKE_WIDTH);
			writeView.setPaintStrokeWidth(strokeWidth);

			int gpenorhw = data.getIntExtra(Constants.SETTING_GPEN_OR_HW, gPenTargetType.GPSO);

			int version = -1;
			int mode = -1;
			int target = -1;

			if(gpenorhw == gPenTargetType.GPSO) {
				version = data.getIntExtra(Constants.SETTING_VERSION, gPenTargetType.GPEN_VER_MIX);
				mode = data.getIntExtra(Constants.SETTING_MODE, gPenTargetType.GPEN_OVERLAP);
				target = data.getIntExtra(Constants.SETTING_TARGET, gPenTargetType.GPEN_TR_SPECIAL_DC);

			} else if(gpenorhw == gPenTargetType.HWSO) {
				version = data.getIntExtra(Constants.SETTING_VERSION, RecognizerMain.Language_KR);
				mode = data.getIntExtra(Constants.SETTING_MODE, RecognizerMain.Mode_CHS_Sentence_Overlap_Free);
			}

			writeView.setReconginazeVersion(version);
			writeView.setReconginazeTarget(target);
			writeView.setReconginazeMode(mode);

			int time = data.getIntExtra(Constants.SETTING_WATI_TIME, Constants.DEFAULT_MATCH_DELAY);
			writeView.setWaitTime(time);
			
			int speed = data.getIntExtra(Constants.SETTING_RECOG_SPEED, Constants.DEFAULT_RECOG_SPEED);
			writeView.setReconginazeSpeed(speed);
			
			boolean realTime = data.getBooleanExtra(Constants.SETTING_REAL_TIME, true);
			writeView.setDisPlayMode(realTime);

		}
		
	}

	protected void onDestroy() {
		super.onDestroy();
		if (writeView != null) {
			writeView.releaseClassifier();
		}
	}

}
