/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\hlw\\ScLumin\\src\\com\\hbcmcc\\sclumin\\service\\ScLuminServiceAIDL.aidl
 */
package com.hbcmcc.sclumin.service;
public interface ScLuminServiceAIDL extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.hbcmcc.sclumin.service.ScLuminServiceAIDL
{
private static final java.lang.String DESCRIPTOR = "com.hbcmcc.sclumin.service.ScLuminServiceAIDL";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.hbcmcc.sclumin.service.ScLuminServiceAIDL interface,
 * generating a proxy if needed.
 */
public static com.hbcmcc.sclumin.service.ScLuminServiceAIDL asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.hbcmcc.sclumin.service.ScLuminServiceAIDL))) {
return ((com.hbcmcc.sclumin.service.ScLuminServiceAIDL)iin);
}
return new com.hbcmcc.sclumin.service.ScLuminServiceAIDL.Stub.Proxy(obj);
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
case TRANSACTION_startService:
{
data.enforceInterface(DESCRIPTOR);
this.startService();
reply.writeNoException();
return true;
}
case TRANSACTION_stopService:
{
data.enforceInterface(DESCRIPTOR);
this.stopService();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.hbcmcc.sclumin.service.ScLuminServiceAIDL
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
@Override public void startService() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startService, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stopService() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopService, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_startService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_stopService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void startService() throws android.os.RemoteException;
public void stopService() throws android.os.RemoteException;
}
