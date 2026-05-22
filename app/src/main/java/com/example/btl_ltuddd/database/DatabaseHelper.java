package com.example.btl_ltuddd.database;

// app/src/main/java/com/example/btl_ltuddd/database/DatabaseHelper.java
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "hoaquasach.db";
    private static final int    DB_VERSION = 1;

    // Bảng users
    public static final String TABLE_USERS = "users";
    public static final String COL_ID       = "id";
    public static final String COL_FULLNAME = "fullname";
    public static final String COL_PHONE    = "phone";
    public static final String COL_EMAIL    = "email";
    public static final String COL_PASSWORD = "password";

    // Bảng products
    public static final String TABLE_PRODUCTS = "products";
    public static final String COL_P_ID       = "id";
    public static final String COL_P_NAME     = "name";
    public static final String COL_P_PRICE    = "price";
    public static final String COL_P_DESC     = "description";
    public static final String COL_P_CATEGORY = "category";
    public static final String COL_P_IMAGE    = "image_url";
    public static final String COL_P_STOCK    = "stock";

    private static DatabaseHelper instance;

    // Singleton — dùng chung 1 instance trong toàn app
    public static synchronized DatabaseHelper getInstance(Context ctx) {
        if (instance == null)
            instance = new DatabaseHelper(ctx.getApplicationContext());
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " ("
                + COL_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_FULLNAME + " TEXT NOT NULL, "
                + COL_PHONE    + " TEXT, "
                + COL_EMAIL    + " TEXT UNIQUE NOT NULL, "
                + COL_PASSWORD + " TEXT NOT NULL"
                + ")");

        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " ("
                + COL_P_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_P_NAME     + " TEXT NOT NULL, "
                + COL_P_PRICE    + " REAL NOT NULL, "
                + COL_P_DESC     + " TEXT, "
                + COL_P_CATEGORY + " TEXT, "
                + COL_P_IMAGE    + " TEXT, "
                + COL_P_STOCK    + " INTEGER DEFAULT 0"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    // ─── USER METHODS ──────────────────────────────────────────

    /** Đăng ký → trả về id nếu thành công, -1 nếu email đã tồn tại */
    public long registerUser(String fullName, String phone, String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_FULLNAME, fullName);
        cv.put(COL_PHONE,    phone);
        cv.put(COL_EMAIL,    email);
        cv.put(COL_PASSWORD, password); // TODO: hash password (BCrypt/SHA-256) trước khi lưu
        try {
            return db.insertOrThrow(TABLE_USERS, null, cv);
        } catch (Exception e) {
            return -1; // Email đã tồn tại (UNIQUE constraint)
        }
    }

    /** Đăng nhập → trả về user id nếu đúng, -1 nếu sai */
    public long loginUser(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS,
                new String[]{COL_ID},
                COL_EMAIL + "=? AND " + COL_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);
        long userId = -1;
        if (c.moveToFirst()) userId = c.getLong(0);
        c.close();
        return userId;
    }

    /** Kiểm tra email đã tồn tại chưa */
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, new String[]{COL_ID},
                COL_EMAIL + "=?", new String[]{email},
                null, null, null);
        boolean exists = c.getCount() > 0;
        c.close();
        return exists;
    }
}