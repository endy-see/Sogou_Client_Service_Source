package com.sogou.handwrite.overlap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.hciilab.recognization.lib.gPenLibHandwrite;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class HandwriteService extends Service {
	private static final String TAG = "HandwriteService";
	private static final boolean DEBUG = true;

	// 相关文件名字
	private static final String SO_NAME = "libgpen_api_so.so";// 单字识别引擎
	private static final String LANGUAGE_FILENAME = "char_bigram.dic";// 语言模型文件
//	private static final String LANGUAGE_FILENAME = "HW_REC_KR.bin";// 语言模型文件


	// just for debug!!! 2015-10-29
	private static final String OUTPUT_DIR = "/sdcard/sogou_handwriting/";
	private static final String OUTPUT_FILE_NAME = "handwrite_service.result";
	private FileWriter mFileWriter = null;
	private BufferedWriter mBufferedWriter = null;
	private boolean isWriting = false;

	private gPenLibHandwrite mLib = null;

	@Override
	public void onCreate() {
		if (DEBUG) {
			int returnValue = openOutputFile(OUTPUT_DIR, OUTPUT_FILE_NAME);
			if (returnValue < 0) {
				Toast.makeText(HandwriteService.this, "Cannot store logs.", Toast.LENGTH_SHORT).show();
			}
		}
		printf("onCreate");
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		printf("onStart, startId:" + startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int returnValue = super.onStartCommand(intent, flags, startId);
		printf("onStartCommand, flags:" + flags + ", startId:" + startId + ", return:" + returnValue);
		return returnValue;
	}

	@Override
	public IBinder onBind(Intent intent) {
		printf("onBind, mBinder:" + mBinder);
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		boolean returnValue = super.onUnbind(intent);
		printf("onUnbind, return:" + returnValue);
		return returnValue;
	}

	@Override
	public void onRebind(Intent intent) {
		printf("onRebind");
		super.onRebind(intent);
	}

	@Override
	public void onDestroy() {
		printf("onDestroy");
		super.onDestroy();
		if (DEBUG) {
			while (isWriting == true) {
				// just wait, 2015-10-29
			}
			closeOutputFile();
		}
	}

	private final IHandwriteService.Stub mBinder = new IHandwriteService.Stub() {

		@Override
		public int initializeEngine() throws RemoteException {
			mLib = gPenLibHandwrite.getInstance();
			Log.e("***", "开始初始化分类器：");
			int returnValue = initializeClassifier(mLib);
			Log.e("***", "开始初始化分类器："+returnValue);

			printf("initializeEngine, mLib:" + mLib + ", return:" + returnValue);
			return returnValue;
		}

		@Override
		public int setVersion(int version) throws RemoteException {
			if (mLib == null) {
				printf("setVersion, mLib is null.");
				return -1;
			}

			int returnValue = mLib.iSetVersion(version);
			printf("setVersion, return:" + returnValue);
			return returnValue;
		}

//		@Override
//		public int processHandwrite(int[] strokePoints, int pointsAmount, int targetType, int handwriteMode)
//				throws RemoteException {
//			if (mLib == null) {
//				printf("processHandwrite, mLib is null.");
//				return -1;
//			}
//
//			int returnValue = mLib.processHandwrite(strokePoints, pointsAmount, targetType, handwriteMode);
//			printf("processHandwrite, pointsAmount:" + pointsAmount + ", return:" + returnValue);
//			return returnValue;
//		}
		
		@Override
		public int processHandwrite(int[] strokePoints) throws RemoteException {
			if(mLib == null) {
				printf("processHandwrite, mLib is null.");
				return -1;
			}
			
			int returnValue = mLib.processHandwrite(strokePoints);
			return returnValue;
		}

		@Override
		public List<String> getResult() throws RemoteException {
			if (mLib == null) {
				printf("getResult, mLib is null.");
				return null;
			}

			printf("getResult, no need return value.");
			return mLib.getAllShowResult();
		}

		@Override
		public int clearResult() throws RemoteException {
			if (mLib == null) {
				printf("clearResult, mLib is null.");
				return -1;
			}

			int returnValue = mLib.iClear();
			printf("clearResult, return:" + returnValue);
			return returnValue;
		}

		@Override
		public int destroyEngine() throws RemoteException {
			if (mLib == null) {
				printf("destroyEngine, mLib is null.");
				return -1;
			}

			int returnValue = mLib.iReleaseClassifier();
			printf("destroyEngine, return:" + returnValue);
			return returnValue;
		}

		@Override
		public int setRecogSpeed(int speed) throws RemoteException {
			if (mLib == null) {
				printf("setRecogSpeed, mLib is null.");
				return -1;
			}
			int returnValue = mLib.iSetRecogSpeed(speed);
			printf("setRecogSpeed, return:" + returnValue);
			return returnValue;
		}

		@Override
		public int setTargetAndMode(int target, int mode) throws RemoteException {
			if (mLib == null) {
				printf("setTargetAndMode, mLib is null.");
				return -1;
			}
			int returnValue = mLib.setTargetAndMode(target, mode);
			printf("setTargetAndMode, return:" + returnValue);
			return returnValue;
		}

		@Override
		public byte[] getOriginResult() throws RemoteException {
			if (mLib == null) {
				printf("getOriginReslut, mLib is null.");
				return null;
			}
			return mLib.getAllOriginalResult();
		}

		@Override
		public int resetResult() throws RemoteException {
			if (mLib == null) {
				printf("resetResult, mLib is null.");
				return -1;
			}
			int returnValue = mLib.iReset();
			printf("resetResult, return:" + returnValue);
			return returnValue;
		}

	};

	/**
	 * 从raw中直接复制文件.
	 * 
	 * @param context
	 *            上下文
	 * @param dirName
	 *            目录
	 * @param fileName
	 *            文件名字
	 * @param rawID
	 *            raw资源id
	 * @return 是否成功
	 */
	private boolean copyFromRaw(Context context, String dirName, String fileName, int rawID) {
		try {
			// set the file path for the resource file
			String wordlibFilename = dirName + "/" + fileName;
			File dir = new File(dirName);

			// if the dir in the path not exit, create one
			if (!dir.exists())
				dir.mkdir();

			// if the resource file not exit, create one
			if (!(new File(wordlibFilename)).exists()) {
				// copy the "word_lib.dic" in res/raw to RESOURCEFILEPATH
				InputStream is = context.getResources().openRawResource(rawID);
				FileOutputStream fos = new FileOutputStream(wordlibFilename);
				byte[] buffer = new byte[8192];
				int count = 0;

				try {
					// copying . . .
					while ((count = is.read(buffer)) > 0) {
						fos.write(buffer, 0, count);
					}
				} catch (Exception e) {
					e.printStackTrace();
					printf("Exception occurred when calling copyFromRaw read.");
					return false;

				} finally {
					fos.close();
					is.close();
				}
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			printf("Exception occurred when calling copyFromRaw.");
			return false;
		}
	}

	private int initializeClassifier(gPenLibHandwrite mLib) {
		if (mLib == null) {
			Log.e("langFileName=", "初始化分类器*********************");

			printf("initializeClassifier, mLib is null.");
			return -1;
		}
		Log.e("langFileName=", "*********************");

		Context mContext = getApplication();
		String soName = mContext.getFilesDir().getParentFile().getPath() + "/lib/" + SO_NAME;
		/**
		 * 获取软件主目录/data/data/packagename/files
		 */
		String languageFileName = mContext.getFilesDir().getPath() + "/" + LANGUAGE_FILENAME;
		File file = new File(languageFileName);
		if (!file.exists()) // 如果语言模型文件不存在，则复制
		{
			boolean ret = copyFromRaw(mContext, mContext.getFilesDir().getPath(), LANGUAGE_FILENAME, R.raw.char_bigram);
			printf("copyFromRaw, return:" + ret);
		}

		int returnValue = mLib.iSetClassifier(soName, languageFileName);
		printf("iSetClassifier, soName[" + soName + "], languageFileName[" + languageFileName + "], return:"
				+ returnValue);
		return returnValue;
	}

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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			isWriting = false;
			return -2;
		}

		isWriting = false;
		return 0;
	}
}
