package com.android.helpers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
	private static String DB_PATH = "/data/data/com.android.shaastra/databases/";
	private static String DB_NAME = "shaastra.db";
	private static String TAG = "Databasehelper";
	public static String EVENT_DETAILS_TABLE_NAME = "eventDetails";
	public static String EVENT_CATEGORY_TABLE_NAME = "eventCategory";
	public static String VENUE_TABLE_NAME = "eventVenues";
	public static String COORDINATOR_TABLE_NAME = "coordList";
	private static String ID = "_id";

	private SQLiteDatabase myDatabase;
	private final Context myContext;

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version)
	{
		super(context, name, factory, version);
		this.myContext = context;
	}

	public DatabaseHelper(Context context)
	{
		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}

	public void createDataBase() throws IOException
	{
		boolean dbExist = checkDataBase();
		if (dbExist)
		{
		} else
		{
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try
			{
				copyDataBase();
			} catch (IOException e)
			{
				throw new Error("Error copying database " + e.toString());
			}
		}
	}

	private boolean checkDataBase()
	{
		SQLiteDatabase checkDB = null;
		try
		{
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e)
		{
			Log.e("database", e.toString());
		}

		if (checkDB != null)
		{
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transferring byte stream.
	 * */
	private void copyDataBase() throws IOException
	{
		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0)
		{
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException
	{
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDatabase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);

	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS events");
		onCreate(db);
	}

	public synchronized void close()
	{
		if (myDatabase != null)
			myDatabase.close();

		super.close();
	}

	public Cursor fetchDescription(long eventId) throws SQLException
	{
		String[] columns = new String[] { ID,
				"eventName", "introduction", "format", "prize", "venueID",
				"time" 
				};
		Cursor mCursor =
		// order matters here
		myDatabase.query(EVENT_DETAILS_TABLE_NAME, columns, ID + "=" + eventId, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor fetchCordDetails(String name) throws SQLException
	{
		Cursor mCursor;
		if (name != null)
		{
			mCursor = myDatabase.query("coordList", new String[] { ID,
					"coordName", "phone", "eventName" }, "eventName= ?",
					new String[] { name }, null, null, null);
			if (mCursor != null)
			{
				mCursor.moveToFirst();
			}
		} else
		{
			mCursor = myDatabase.query("coordList", new String[] { ID,
					"coordName", "phone", "eventName" }, null, null, null, null,
					null);
			if (mCursor != null)
			{
				mCursor.moveToFirst();
			}
		}
		return mCursor;
	}

	public Cursor fetchAllCords() throws SQLException
	{
		Cursor mCursor;

		mCursor = myDatabase.query("coordList", new String[] { ID, "coordName",
				"phone", "eventName" }, null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchCords(String search) throws SQLException
	{
		Cursor mCursor;
		mCursor = myDatabase.query("coordList", new String[] { ID, "coordName",
				"phone", "eventName" }, "coordName like ? or eventName like ?",
				new String[] { "%" + search + "%", "%" + search + "%" }, null,
				null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	/* Queries the category table and gets the list of categories in order */
	public String[] getCategoryList() throws SQLException
	{
		Cursor mCursor;
		mCursor = myDatabase.query(EVENT_CATEGORY_TABLE_NAME, new String[]{"eventCategoryName"}, null, null, null, null, null);
		
		ArrayList<String> a = new ArrayList<String>();
		while(mCursor.moveToNext())
		{
			a.add(mCursor.getString(0));
		}
		mCursor.close();
		String[] b = new String[a.size()];
		for(int i = 0; i < a.size(); i++)
		{
			b[i] = a.get(i);
		}
		
		/* Some other side thingees for faster querying */
		Global.categoryCount = a.size();
		
		return b;
		
	}
	public Cursor getEventsForCategory(int category)
	{
		Cursor mCursor;
		mCursor = myDatabase.query(EVENT_DETAILS_TABLE_NAME, new String[]{ID, "eventName"}, "eventCategoryID = " + category, null, null, null, null);
		return mCursor;
	}
	public int sizeOfCategories() throws SQLException
	{
		Cursor mCursor;
		mCursor = myDatabase.query("eventDetails",
				new String[] { "eventCategoryID" }, null, null, null, null,
				null);
		HashSet<Integer> h = new HashSet<Integer>();
		while (mCursor.moveToNext())
		{
			h.add(mCursor.getInt(0));
		}
		mCursor.close();
		return h.size();
	}
	public Cursor getLocation(int id)
	{
		Cursor mCursor;
		mCursor = myDatabase.query(VENUE_TABLE_NAME,
				new String[] { "venueName", "venueLat", "venueLong" }, ID + "=" + Integer.toString(id), null, null, null,
				null);
		mCursor.moveToFirst();
		return mCursor;
	}

}
