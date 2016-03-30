/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\AndroidStudio\\sogou_overlap_handwrite_client_v1.5_new\\overlapHandwriteClient\\src\\main\\aidl\\com\\sogou\\HanwangService\\IHanwangService.aidl
 */
package com.sogou.HanwangService;
// Declare any non-default types here with import statements

public interface IHanwangService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.sogou.HanwangService.IHanwangService
{
private static final java.lang.String DESCRIPTOR = "com.sogou.HanwangService.IHanwangService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.sogou.HanwangService.IHanwangService interface,
 * generating a proxy if needed.
 */
public static com.sogou.HanwangService.IHanwangService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.sogou.HanwangService.IHanwangService))) {
return ((com.sogou.HanwangService.IHanwangService)iin);
}
return new com.sogou.HanwangService.IHanwangService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_basicTypes:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
long _arg1;
_arg1 = data.readLong();
boolean _arg2;
_arg2 = (0!=data.readInt());
float _arg3;
_arg3 = data.readFloat();
double _arg4;
_arg4 = data.readDouble();
java.lang.String _arg5;
_arg5 = data.readString();
this.basicTypes(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
reply.writeNoException();
return true;
}
case TRANSACTION_initializeHanwangEngine:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _result = this.initializeHanwangEngine(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_destroyHanwangEngine:
{
data.enforceInterface(DESCRIPTOR);
this.destroyHanwangEngine();
reply.writeNoException();
return true;
}
case TRANSACTION_getOverlayRecognition:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
_arg0 = data.createIntArray();
java.util.List<java.lang.String> _arg1;
_arg1 = new java.util.ArrayList<java.lang.String>();
int _result = this.getOverlayRecognition(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
reply.writeStringList(_arg1);
return true;
}
case TRANSACTION_getSingleRecognition:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
_arg0 = data.createIntArray();
char[] _arg1;
int _arg1_length = data.readInt();
if ((_arg1_length<0)) {
_arg1 = null;
}
else {
_arg1 = new char[_arg1_length];
}
int _result = this.getSingleRecognition(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
reply.writeCharArray(_arg1);
return true;
}
case TRANSACTION_getResult:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<java.lang.String> _result = this.getResult();
reply.writeNoException();
reply.writeStringList(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.sogou.HanwangService.IHanwangService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
@Override public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, java.lang.String aString) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(anInt);
_data.writeLong(aLong);
_data.writeInt(((aBoolean)?(1):(0)));
_data.writeFloat(aFloat);
_data.writeDouble(aDouble);
_data.writeString(aString);
mRemote.transact(Stub.TRANSACTION_basicTypes, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int initializeHanwangEngine(int mode, int range) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(mode);
_data.writeInt(range);
mRemote.transact(Stub.TRANSACTION_initializeHanwangEngine, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void destroyHanwangEngine() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_destroyHanwangEngine, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int getOverlayRecognition(int[] inputPoints, java.util.List<java.lang.String> candidateResults) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeIntArray(inputPoints);
mRemote.transact(Stub.TRANSACTION_getOverlayRecognition, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readStringList(candidateResults);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getSingleRecognition(int[] inputPoints, char[] candidateResults) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeIntArray(inputPoints);
if ((candidateResults==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(candidateResults.length);
}
mRemote.transact(Stub.TRANSACTION_getSingleRecognition, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readCharArray(candidateResults);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.util.List<java.lang.String> getResult() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<java.lang.String> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getResult, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArrayList();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_basicTypes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_initializeHanwangEngine = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_destroyHanwangEngine = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getOverlayRecognition = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getSingleRecognition = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, java.lang.String aString) throws android.os.RemoteException;
public int initializeHanwangEngine(int mode, int range) throws android.os.RemoteException;
public void destroyHanwangEngine() throws android.os.RemoteException;
public int getOverlayRecognition(int[] inputPoints, java.util.List<java.lang.String> candidateResults) throws android.os.RemoteException;
public int getSingleRecognition(int[] inputPoints, char[] candidateResults) throws android.os.RemoteException;
public java.util.List<java.lang.String> getResult() throws android.os.RemoteException;
}
