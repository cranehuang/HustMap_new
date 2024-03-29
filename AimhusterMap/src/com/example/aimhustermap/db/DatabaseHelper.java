package com.example.aimhustermap.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import com.example.aimhustermap.util.CipherUtil;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	
	private Activity activity ;
	private static String KEYSTRING = "chenkaijialaoshi" ;
	private static String HUST = "hust.db";

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		activity = (Activity)context;
		File file = new File(DatabaseHust.DB_PATH + "/" +DatabaseHust.DB_NAME);

		//如果不存在数据库，则从asset导入
		if(!file.exists()){
			includeDB();
			
			SecretKey key = CipherUtil.generateKeyByAES(KEYSTRING);
			try {
				CipherUtil.decryptByAES(DatabaseHust.DB_PATH + "/" + HUST,
						DatabaseHust.DB_PATH + "/" + DatabaseHust.DB_NAME, key);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("jiemi");
			}
			finally{
				File fileHust = new File(DatabaseHust.DB_PATH + "/" + HUST );
				fileHust.delete();
			}
			
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		File file = new File(DatabaseHust.DB_PATH + "/" +DatabaseHust.DB_NAME);

		//如果不存在数据库，则从asset导入
		if(!file.exists()){
			includeDB();
			
			SecretKey key = CipherUtil.generateKeyByAES(KEYSTRING);
			try {
				CipherUtil.decryptByAES(DatabaseHust.DB_PATH + "/" +DatabaseHust.DB_NAME,
						DatabaseHust.DB_PATH + "/" +DatabaseHust.DB_NAME, key);
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally{
				File fileHust = new File(DatabaseHust.DB_PATH + "/" + HUST );
				fileHust.delete();
			}
//			
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		File file = new File(DatabaseHust.DB_PATH+"/"+DatabaseHust.DB_NAME);
		file.delete();
		onCreate(db);
		
	}
	
	/*
	 * 
	 * 如果数据库文件不在，则从asset中导入到指定文件夹中
	 * 
	 * */
	private boolean includeDB(){
		
		boolean b = false;
		System.out.println("include method");
		// 检查 SQLite 数据库文件是否存在
				if ((new File(DatabaseHust.DB_PATH +"/"+ HUST)).exists() == false) {
					// 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在
					File f = new File(DatabaseHust.DB_PATH);
					// 如 database 目录不存在，新建该目录
					if (!f.exists()) {
						f.mkdir();
						System.out.println("f---->"+f.mkdir());
					}
					System.out.println("jixu----->>>>");
					
					try {
						// 得到 assets 目录下我们实现准备好的 SQLite 数据库作为输入流
						InputStream is = activity.getBaseContext().getAssets().open(HUST);
						// 输出流
						OutputStream os = new FileOutputStream(DatabaseHust.DB_PATH + "/" + HUST);

						// 文件写入
						byte[] buffer = new byte[1024];
						int length;
						while ((length = is.read(buffer)) > 0) {
							os.write(buffer, 0, length);
						}

						// 关闭文件流
						os.flush();
						os.close();
						is.close();
						b = true;
					} catch (Exception e) {
						System.out.println("catch------>>>>>>>>>>");
						e.printStackTrace();
						b = false;
					}
				}
				
				return b;
		
	}


}
