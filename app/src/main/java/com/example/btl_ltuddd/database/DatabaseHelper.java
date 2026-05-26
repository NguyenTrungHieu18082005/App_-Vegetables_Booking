package com.example.btl_ltuddd.database;

// app/src/main/java/com/example/btl_ltuddd/database/DatabaseHelper.java
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.btl_ltuddd.client.dashboard.ProductAdapter;
import com.example.btl_ltuddd.model.Cart;
import com.example.btl_ltuddd.model.Order;
import com.example.btl_ltuddd.model.Product;
import com.example.btl_ltuddd.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    ProductAdapter adapter;
    List<Product> productList;
    DatabaseHelper dbHelper;

    public static final String DB_NAME    = "hoaquasach.db";
    private static final int    DB_VERSION = 3;

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

    public static final String COL_P_UNIT    = "unit";
    public static final String COL_P_VISIBLE = "is_visible";

    public static final String TABLE_ORDERS     = "orders";
    public static final String COL_O_ID         = "id";
    public static final String COL_O_CODE       = "order_code";
    public static final String COL_O_CUST_NAME  = "customer_name";
    public static final String COL_O_CUST_PHONE = "customer_phone";
    public static final String COL_O_TOTAL      = "total_amount";
    public static final String COL_O_STATUS     = "status";
    public static final String COL_O_CREATED    = "created_at";
    public static final String COL_O_ADDRESS    = "address";

    private static final String TABLE_CART = "cart";

    public static final String TABLE_ORDER_ITEMS = "order_items";
    public static final String COL_ITEM_ID       = "id";
    public static final String COL_ORDER_ID      = "order_id";
    public static final String COL_PRODUCT_ID    = "product_id";
    public static final String COL_QTY           = "quantity";
    public static final String COL_PRICE         = "price";

    // Singleton
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
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
                + COL_P_STOCK    + " INTEGER DEFAULT 0, "
                + COL_P_UNIT     + " TEXT, "
                + COL_P_VISIBLE  + " INTEGER DEFAULT 1"
                + ")");

        db.execSQL("CREATE TABLE " + TABLE_ORDERS + " ("
                + COL_O_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_O_CODE       + " TEXT, "
                + COL_O_CUST_NAME  + " TEXT NOT NULL, "
                + COL_O_CUST_PHONE + " TEXT, "
                + COL_O_TOTAL      + " REAL NOT NULL, "
                + COL_O_STATUS     + " TEXT DEFAULT 'pending', "
                + COL_O_CREATED    + " TEXT, "
                + COL_O_ADDRESS    + " TEXT"
                + ")");

        db.execSQL("CREATE TABLE " + TABLE_CART + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER NOT NULL,"
                + "product_id INTEGER NOT NULL,"
                + "quantity INTEGER DEFAULT 1,"
                + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(user_id) REFERENCES users(id),"
                + "FOREIGN KEY(product_id) REFERENCES products(id)"
                + ")");

        db.execSQL("CREATE TABLE " + TABLE_ORDER_ITEMS + "("
                + COL_ITEM_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_ORDER_ID   + " INTEGER,"
                + COL_PRODUCT_ID + " INTEGER,"
                + COL_QTY        + " INTEGER,"
                + COL_PRICE      + " REAL"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cart");
        db.execSQL("DROP TABLE IF EXISTS order_items");
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS users");
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
        cv.put(COL_PASSWORD, password);
        try {
            return db.insertOrThrow(TABLE_USERS, null, cv);
        } catch (Exception e) {
            return -1;
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

    /** Lấy User object theo id — trả null nếu không tìm thấy */
    public User getUserById(long userId) {
        SQLiteDatabase db = getReadableDatabase();  // FIX: khai báo db đúng cách
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_ID, COL_FULLNAME, COL_EMAIL, COL_PHONE},
                COL_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null
        );
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_FULLNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)),
                    null  // không trả password ra ngoài
            );
        }
        cursor.close();
        return user;
    }

    /** Trả về tên user theo id (dùng cho profile) */
    public String getUserNameById(long userId) {
        User user = getUserById(userId);
        return user != null ? user.getFullname() : "";
    }

    public int getUserCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS, null);
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        return count;
    }

    // ─── PRODUCT METHODS ──────────────────────────────────────────

    public int getLowStockCount(int threshold) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_PRODUCTS + " WHERE " + COL_P_STOCK + " <= ?",
                new String[]{String.valueOf(threshold)});
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        return count;
    }

    public int getProductCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PRODUCTS, null);
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        return count;
    }

    public long addProduct(String name, double price, String description,
                           String category, String imageUrl, int stock,
                           String unit, boolean isVisible) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_P_NAME,     name);
        values.put(COL_P_PRICE,    price);
        values.put(COL_P_DESC,     description);
        values.put(COL_P_CATEGORY, category);
        values.put(COL_P_IMAGE,    imageUrl);
        values.put(COL_P_STOCK,    stock);
        values.put(COL_P_UNIT,     unit);
        values.put(COL_P_VISIBLE,  isVisible ? 1 : 0);
        return db.insert(TABLE_PRODUCTS, null, values);
    }

    public int updateProduct(int id, String name, double price, String description,
                             String category, String imageUrl, int stock,
                             String unit, boolean isVisible) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_P_NAME,     name);
        values.put(COL_P_PRICE,    price);
        values.put(COL_P_DESC,     description);
        values.put(COL_P_CATEGORY, category);
        values.put(COL_P_IMAGE,    imageUrl);
        values.put(COL_P_STOCK,    stock);
        values.put(COL_P_UNIT,     unit);
        values.put(COL_P_VISIBLE,  isVisible ? 1 : 0);
        return db.update(TABLE_PRODUCTS, values,
                COL_P_ID + "=?", new String[]{String.valueOf(id)});
    }

    private Product cursorToProduct(Cursor cursor) {
        return new Product(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_P_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_P_NAME)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_P_PRICE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_P_DESC)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_P_CATEGORY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_P_IMAGE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_P_STOCK)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_P_UNIT)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_P_VISIBLE)) == 1
        );
    }

    public Product getProductById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        Product product = null;
        if (cursor != null && cursor.moveToFirst()) {
            product = cursorToProduct(cursor);
            cursor.close();
        }
        return product;
    }

    public boolean deleteProduct(int id) {
        return getWritableDatabase().delete(TABLE_PRODUCTS,
                "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM products WHERE is_visible = 1", null);
        if (cursor.moveToFirst()) {
            do {
                products.add(cursorToProduct(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }

    public List<Product> searchProducts(String query) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM products WHERE name LIKE ? ORDER BY name ASC",
                new String[]{"%" + query + "%"});
        while (cursor.moveToNext()) {
            list.add(cursorToProduct(cursor));
        }
        cursor.close();
        return list;
    }

    // ─── ORDER METHODS ──────────────────────────────────────────

    public long addOrder(String customerName, String customerPhone,
                         double totalAmount, String address) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_O_CUST_NAME,  customerName);
        values.put(COL_O_CUST_PHONE, customerPhone);
        values.put(COL_O_TOTAL,      totalAmount);
        values.put(COL_O_STATUS,     "pending");
        values.put(COL_O_ADDRESS,    address);
        values.put(COL_O_CREATED,    getCurrentDateTime());
        long id = db.insert(TABLE_ORDERS, null, values);
        ContentValues cv2 = new ContentValues();
        cv2.put(COL_O_CODE, "#FH-" + String.format("%04d", id));
        db.update(TABLE_ORDERS, cv2, COL_O_ID + "=?", new String[]{String.valueOf(id)});
        return id;
    }

    public int updateOrderStatus(int id, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_O_STATUS, status);
        return db.update(TABLE_ORDERS, values,
                COL_O_ID + "=?", new String[]{String.valueOf(id)});
    }

    public boolean deleteOrder(int id) {
        return getWritableDatabase().delete(TABLE_ORDERS,
                COL_O_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public List<Order> getAllOrders() {
        return queryOrders("SELECT * FROM " + TABLE_ORDERS
                + " ORDER BY " + COL_O_ID + " DESC", null);
    }

    public List<Order> getOrdersByStatus(String status) {
        return queryOrders("SELECT * FROM " + TABLE_ORDERS
                + " WHERE " + COL_O_STATUS + "=?"
                + " ORDER BY " + COL_O_ID + " DESC", new String[]{status});
    }

    public List<Order> searchOrders(String query) {
        String like = "%" + query + "%";
        return queryOrders("SELECT * FROM " + TABLE_ORDERS
                + " WHERE " + COL_O_CODE + " LIKE ? OR "
                + COL_O_CUST_NAME + " LIKE ?"
                + " ORDER BY " + COL_O_ID + " DESC", new String[]{like, like});
    }

    public int getOrderCount()         { return orderCountByStatus(null); }
    public int getPendingOrderCount()  { return orderCountByStatus("pending"); }
    public int getShippingOrderCount() { return orderCountByStatus("shipping"); }
    public int getDoneOrderCount()     { return orderCountByStatus("done"); }

    private int orderCountByStatus(String status) {
        SQLiteDatabase db = getReadableDatabase();
        String sql  = status == null
                ? "SELECT COUNT(*) FROM " + TABLE_ORDERS
                : "SELECT COUNT(*) FROM " + TABLE_ORDERS + " WHERE status=?";
        String[] args = status == null ? null : new String[]{status};
        Cursor c = db.rawQuery(sql, args);
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        return count;
    }

    private List<Order> queryOrders(String sql, String[] args) {
        List<Order> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(sql, args);
        while (c.moveToNext()) {
            list.add(new Order(
                    c.getInt(c.getColumnIndexOrThrow(COL_O_ID)),
                    c.getString(c.getColumnIndexOrThrow(COL_O_CODE)),
                    c.getString(c.getColumnIndexOrThrow(COL_O_CUST_NAME)),
                    c.getString(c.getColumnIndexOrThrow(COL_O_CUST_PHONE)),
                    c.getDouble(c.getColumnIndexOrThrow(COL_O_TOTAL)),
                    c.getString(c.getColumnIndexOrThrow(COL_O_STATUS)),
                    c.getString(c.getColumnIndexOrThrow(COL_O_CREATED)),
                    c.getString(c.getColumnIndexOrThrow(COL_O_ADDRESS))
            ));
        }
        c.close();
        return list;
    }

    /**
     * Đặt hàng: tạo order + lưu order_items + xóa giỏ hàng (1 transaction)
     */
    public long placeOrder(int userId, List<Cart> cartItems, double subtotal, double total) {
        // FIX: dùng User object thay vì String[]
        User user = getUserById(userId);
        if (user == null) return -1;

        String customerName  = user.getFullname();
        String customerPhone = user.getPhone() != null ? user.getPhone() : "";
        String address       = "";

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // 1. Tạo đơn hàng
            ContentValues orderValues = new ContentValues();
            orderValues.put(COL_O_CUST_NAME,  customerName);
            orderValues.put(COL_O_CUST_PHONE, customerPhone);
            orderValues.put(COL_O_TOTAL,      total);
            orderValues.put(COL_O_STATUS,     "pending");
            orderValues.put(COL_O_ADDRESS,    address);
            orderValues.put(COL_O_CREATED,    getCurrentDateTime());

            long orderId = db.insert(TABLE_ORDERS, null, orderValues);
            if (orderId == -1) return -1;

            // Cập nhật mã đơn
            ContentValues codeValues = new ContentValues();
            codeValues.put(COL_O_CODE, "#FH-" + String.format("%04d", orderId));
            db.update(TABLE_ORDERS, codeValues, COL_O_ID + "=?",
                    new String[]{String.valueOf(orderId)});

            // 2. Lưu từng sản phẩm vào order_items
            for (Cart item : cartItems) {
                ContentValues itemValues = new ContentValues();
                itemValues.put(COL_ORDER_ID,   orderId);
                itemValues.put(COL_PRODUCT_ID, item.getProductId());
                itemValues.put(COL_QTY,        item.getQuantity());
                itemValues.put(COL_PRICE,      item.getProductPrice());
                db.insert(TABLE_ORDER_ITEMS, null, itemValues);
            }

            // 3. Xóa giỏ hàng
            db.delete(TABLE_CART, "user_id=?", new String[]{String.valueOf(userId)});

            db.setTransactionSuccessful();
            return orderId;
        } finally {
            db.endTransaction();
        }
    }

    private String getCurrentDateTime() {
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }

    // ─── CART METHODS ──────────────────────────────────────────

    public long insertCart(int userId, int productId) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT quantity FROM cart WHERE user_id=? AND product_id=?",
                new String[]{String.valueOf(userId), String.valueOf(productId)});
        if (cursor.moveToFirst()) {
            int quantity = cursor.getInt(0);
            ContentValues values = new ContentValues();
            values.put("quantity", quantity + 1);
            db.update(TABLE_CART, values,
                    "user_id=? AND product_id=?",
                    new String[]{String.valueOf(userId), String.valueOf(productId)});
            cursor.close();
            return 1;
        }
        cursor.close();
        ContentValues values = new ContentValues();
        values.put("user_id",    userId);
        values.put("product_id", productId);
        values.put("quantity",   1);
        return db.insert(TABLE_CART, null, values);
    }

    public List<Product> getCartProducts(int userId) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT p.*, c.quantity FROM cart c "
                        + "INNER JOIN products p ON c.product_id = p.id "
                        + "WHERE c.user_id = ?",
                new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow("image_url")));
                product.setUnit(cursor.getString(cursor.getColumnIndexOrThrow("unit")));
                product.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                list.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean removeCart(int userId, int productId) {
        return getWritableDatabase().delete(TABLE_CART,
                "user_id=? AND product_id=?",
                new String[]{String.valueOf(userId), String.valueOf(productId)}) > 0;
    }

    public void clearCart(int userId) {
        getWritableDatabase().delete(TABLE_CART,
                "user_id=?", new String[]{String.valueOf(userId)});
    }

    public void updateCartQuantity(int cartId, int quantity) {
        ContentValues cv = new ContentValues();
        cv.put("quantity", quantity);
        getWritableDatabase().update("cart", cv, "id=?",
                new String[]{String.valueOf(cartId)});
    }

    public void deleteCart(int cartId) {
        getWritableDatabase().delete("cart", "id=?",
                new String[]{String.valueOf(cartId)});
    }

    public List<Cart> getCartItems(int userId) {
        List<Cart> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT c.id, c.user_id, c.product_id, c.quantity, "
                        + "p.name, p.price, p.image_url, p.unit "
                        + "FROM cart c "
                        + "INNER JOIN products p ON c.product_id = p.id "
                        + "WHERE c.user_id=?",
                new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                Cart cart = new Cart();
                cart.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                cart.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                cart.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow("product_id")));
                cart.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                cart.setProductName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                cart.setProductPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                cart.setProductImage(cursor.getString(cursor.getColumnIndexOrThrow("image_url")));
                cart.setProductUnit(cursor.getString(cursor.getColumnIndexOrThrow("unit")));
                list.add(cart);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}