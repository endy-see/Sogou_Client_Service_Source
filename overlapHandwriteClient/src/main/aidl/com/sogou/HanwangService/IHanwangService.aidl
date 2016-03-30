// IHanwangService.aidl
package com.sogou.HanwangService;

// Declare any non-default types here with import statements

interface IHanwangService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    int initializeHanwangEngine(int mode, int range);

    void destroyHanwangEngine();

    int getOverlayRecognition(in int[] inputPoints, out List<String> candidateResults);

    int getSingleRecognition(in int[] inputPoints, out char[] candidateResults);

    List<String> getResult();
}
