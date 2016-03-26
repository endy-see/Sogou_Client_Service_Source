package com.sogou.handwrite.overlap;

interface IHandwriteService {
	int initializeEngine();
	
	int setVersion(int version);
	
	int processHandwrite(in int[] strokePoints);
	
	int setTargetAndMode(int target, int mode);
	
	List<String> getResult();
	
	byte[] getOriginResult();
	
	int clearResult();
	
	int resetResult();
	
	int destroyEngine();
	
	int setRecogSpeed(int speed);
}