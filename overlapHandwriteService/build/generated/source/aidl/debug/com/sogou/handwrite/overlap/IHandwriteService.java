/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\AndroidStudio\\sogou_overlap_handwrite_client_v1.5_new\\overlapHandwriteService\\src\\main\\aidl\\com\\sogou\\handwrite\\overlap\\IHandwriteService.aidl
 */
package com.sogou.handwrite.overlap;
public interface IHandwriteService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.sogou.handwrite.overlap.IHandwriteService
{
private static final java.lang.String DESCRIPTOR = "com.sogou.handwrite.overlap.IHandwriteService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.sogou.handwrite.overlap.IHandwriteService interface,
 * generating a proxy if needed.
 */
public static com.sogou.handwrite.overlap.IHandwriteService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.sogou.handwrite.overlap.IHandwriteService))) {
return ((com.sogou.handwrite.overlap.IHandwriteService)iin);
}
return new com.sogou.handwrite.overlap.IHandwriteService.Stub.Proxy(obj);
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
case TRANSACTION_initializeEngine:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.initializeEngine();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setVersion:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.setVersion(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_processHandwrite:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
_arg0 = data.createIntArray();
int _result = this.processHandwrite(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setTargetAndMode:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _result = this.setTargetAndMode(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
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
case TRANSACTION_getOriginResult:
{
data.enforceInterface(DESCRIPTOR);
byte[] _result = this.getOriginResult();
reply.writeNoException();
reply.writeByteArray(_result);
return true;
}
case TRANSACTION_clearResult:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.clearResult();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_resetResult:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.resetResult();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_destroyEngine:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.destroyEngine();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setRecogSpeed:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.setRecogSpeed(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.sogou.handwrite.overlap.IHandwriteService
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
@Override public int initializeEngine() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_initializeEngine, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int setVersion(int version) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(version);
mRemote.transact(Stub.TRANSACTION_setVersion, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int processHandwrite(int[] strokePoints) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeIntArray(strokePoints);
mRemote.transact(Stub.TRANSACTION_processHandwrite, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int setTargetAndMode(int target, int mode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(target);
_data.writeInt(mode);
mRemote.transact(Stub.TRANSACTION_setTargetAndMode, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
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
@Override public byte[] getOriginResult() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
byte[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getOriginResult, _data, _reply, 0);
_reply.readException();
_result = _reply.createByteArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int clearResult() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_clearResult, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int resetResult() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_resetResult, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int destroyEngine() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_destroyEngine, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int setRecogSpeed(int speed) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(speed);
mRemote.transact(Stub.TRANSACTION_setRecogSpeed, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_initializeEngine = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setVersion = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_processHandwrite = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_setTargetAndMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getOriginResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_clearResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_resetResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_destroyEngine = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_setRecogSpeed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
}
public int initializeEngine() throws android.os.RemoteException;
public int setVersion(int version) throws android.os.RemoteException;
public int processHandwrite(int[] strokePoints) throws android.os.RemoteException;
public int setTargetAndMode(int target, int mode) throws android.os.RemoteException;
public java.util.List<java.lang.String> getResult() throws android.os.RemoteException;
public byte[] getOriginResult() throws android.os.RemoteException;
public int clearResult() throws android.os.RemoteException;
public int resetResult() throws android.os.RemoteException;
public int destroyEngine() throws android.os.RemoteException;
public int setRecogSpeed(int speed) throws android.os.RemoteException;
}
