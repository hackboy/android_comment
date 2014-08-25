//My接口扩展于IInterface接口
public interface My extends android.os.IInterface{

	//本地ipc实现stub类，抽象类，实现了com.example.themestyle.My接口
	/** Local-side IPC implementation stub class. */
	//aidl帮我们完成了Binder的复杂部分（通信===》transfact）;
	//该类也实现了My接口，即asBinder方法，还有一个我们需要实现的getProcessId方法。。。。如此这般
	//我们要做的就是实现抽象方法，即专注于我们的功能部分
	public static abstract class Stub extends android.os.Binder implements com.example.themestyle.My{
		//这就是DESCRIPTOR
		private static final java.lang.String DESCRIPTOR = "com.example.themestyle.My";

		//构造该对象时进行attachInterface操作
		public Stub(){
			this.attachInterface(this, DESCRIPTOR);
			}
		//直接得到一个可用的接口
		//将ibinder对象转换为com.example.themestyle.My接口，如有必要生成一个代理对象
		public static com.example.themestyle.My asInterface(android.os.IBinder obj){
			
			if ((obj==null)) {
				return null;
			}

			//iinterface接口对象
			android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);

			if (((iin!=null)&&(iin instanceof com.example.themestyle.My))) {
				return ((com.example.themestyle.My)iin);
			}

			return new com.example.themestyle.My.Stub.Proxy(obj);
		}

		//asbinder方法，直接返回stub对象，这个是my接口应该实现的方法
		@Override 
		public android.os.IBinder asBinder(){
			return this;
		}

		//覆盖binder的ontransact方法，实现binder传输
		@Override 
		public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException{
			switch (code){

			case INTERFACE_TRANSACTION:
				{
				reply.writeString(DESCRIPTOR);
				return true;
				}
			//我们提供的方法，看起如何传输
			case TRANSACTION_getProcessId:
				{
				data.enforceInterface(DESCRIPTOR);
				int _result = this.getProcessId();
				reply.writeNoException();
				reply.writeInt(_result);
				return true;
				}
			}

			return super.onTransact(code, data, reply, flags);

		}

		//实现一个代理类，实现com.example.themestyle.My接口，这个不是binder，只是包含了一个binder对象
		private static class Proxy implements com.example.themestyle.My{
			//一个binder对象，远程对象代理吧
			private android.os.IBinder mRemote;

			Proxy(android.os.IBinder remote){
				mRemote = remote;
			}
			//直接返回这个远程对象？
			@Override 
			public android.os.IBinder asBinder(){
				return mRemote;
			}

			public java.lang.String getInterfaceDescriptor(){
				return DESCRIPTOR;
			}

			//真真的就是一个代理，直接把请求发送给远程对象，操蛋到底谁是代理。。。。
			//mRemote不也是一个代理吗？？？疯了
			@Override 
			public int getProcessId() throws android.os.RemoteException{
				android.os.Parcel _data = android.os.Parcel.obtain();
				android.os.Parcel _reply = android.os.Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_getProcessId, _data, _reply, 0);
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
		//对该类的方法进行编号，这个应该是1
		static final int TRANSACTION_getProcessId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
	}
		//我们要实现的方法，我们要做的是覆盖该方法
		public int getProcessId() throws android.os.RemoteException;
}