package com.example.overlaphandwriteclient;

public interface OverlayHandwriteListener {
	void onResult(String result, boolean clear);
	void onError(int errorCode);
}
