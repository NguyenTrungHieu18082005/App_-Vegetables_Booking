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

    // Thêm vào phần khai báo constants
    public static final String COL_P_UNIT    = "unit";
    public static final String COL_P_VISIBLE = "is_visible";

    // ── BƯỚC 2: Thêm constants (sau phần constants products) ─────────
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

    public static final String TABLE_ORDER_ITEMS="order_items";

    public static final String COL_ITEM_ID="id";

    public static final String COL_ORDER_ID="order_id";

    public static final String COL_PRODUCT_ID="product_id";

    public static final String COL_QTY="quantity";

    public static final String COL_PRICE="price";



    // Singleton — dùng chung 1 instance trong toàn app
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

        String createCart =

                "CREATE TABLE " + TABLE_CART + "(" +

                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +

                        "user_id INTEGER NOT NULL," +

                        "product_id INTEGER NOT NULL," +

                        "quantity INTEGER DEFAULT 1," +

                        "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +

                        "FOREIGN KEY(user_id) REFERENCES users(id)," +

                        "FOREIGN KEY(product_id) REFERENCES products(id)" +

                        ")";

        db.execSQL(createCart);

        db.execSQL(

                "CREATE TABLE " +

                        TABLE_ORDER_ITEMS +

                        "(" +

                        COL_ITEM_ID +

                        " INTEGER PRIMARY KEY AUTOINCREMENT," +

                        COL_ORDER_ID +

                        " INTEGER," +

                        COL_PRODUCT_ID +

                        " INTEGER," +

                        COL_QTY +

                        " INTEGER," +

                        COL_PRICE +

                        " REAL" +

                        ")"

        );


    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        db.execSQL(
                "DROP TABLE IF EXISTS cart"
        );

        db.execSQL(
                "DROP TABLE IF EXISTS orders"
        );

        db.execSQL(
                "DROP TABLE IF EXISTS products"
        );

        db.execSQL(
                "DROP TABLE IF EXISTS users"
        );

        onCreate(
                db
        );
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

    public int getUserCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS, null);
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        return count;
    }

    // Thêm sản phẩm
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

    // Sửa cursorToProduct()
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
    // Trong DatabaseHelper.java
    public Product getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("products", null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Product product = null;
        if (cursor != null && cursor.moveToFirst()) {
            product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getString(cursor.getColumnIndexOrThrow("category")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image_url")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("stock")),
                    cursor.getString(cursor.getColumnIndexOrThrow("unit")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("is_visible")) == 1  // ✅ sửa ở đây
            );
            cursor.close();
        }
        return product;
    }


    // Xóa sản phẩm
    public boolean deleteProduct(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("products", "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {

        List<Product> products =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM products WHERE is_visible = 1",
                        null
                );

        if (cursor.moveToFirst()) {

            do {

                Product p =
                        new Product();

                p.setId(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("id")
                        )
                );

                p.setName(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("name")
                        )
                );

                p.setPrice(
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow("price")
                        )
                );

                p.setDescription(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("description")
                        )
                );

                p.setCategory(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("category")
                        )
                );

                p.setImageUrl(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("image_url")
                        )
                );

                p.setStock(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("stock")
                        )
                );

                p.setUnit(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("unit")
                        )
                );

                p.setVisible(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("is_visible")
                        ) == 1
                );

                products.add(p);

            }

            while (
                    cursor.moveToNext()
            );
        }

        cursor.close();

        return products;
    }

    // Tìm kiếm theo tên
    public List<Product> searchProducts(String query) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM products WHERE name LIKE ? ORDER BY name ASC",
                new String[]{"%" + query + "%"}
        );
        while (cursor.moveToNext()) {
            list.add(cursorToProduct(cursor));
        }
        cursor.close();
        return list;
    }

    // Helper

    // Order Admin
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
        // Cập nhật mã đơn #FH-xxxx
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

    private String getCurrentDateTime() {
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }



    //làm tính năng trả dữ liệu Tên ra profile
    public String getUserNameById(long userId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_FULLNAME},
                COL_ID + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        String name = "";

        if (cursor.moveToFirst()) {
            name = cursor.getString(
                    cursor.getColumnIndexOrThrow(COL_FULLNAME)
            );
        }

        cursor.close();

        return name;


    }

    /**
     * Lấy thông tin đầy đủ user theo id (fullname, email, phone)
     * Trả về mảng [fullname, email, phone] hoặc null nếu không tìm thấy
     */
    public String[] getUserById(long userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_FULLNAME, COL_EMAIL, COL_PHONE},
                COL_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null
        );
        String[] result = null;
        if (cursor.moveToFirst()) {
            result = new String[]{
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_FULLNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE))
            };
        }
        cursor.close();
        return result;
    }

    /**
     * Đặt hàng: tạo order + lưu order_items + xóa giỏ hàng
     *
     * @param userId      id người dùng
     * @param cartItems   danh sách sản phẩm trong giỏ
     * @param subtotal    tổng tiền hàng
     * @param total       tổng cuối (subtotal + ship)
     * @return orderId nếu thành công, -1 nếu thất bại
     */
    public long placeOrder(int userId, java.util.List<com.example.btl_ltuddd.model.Cart> cartItems,
                           double subtotal, double total) {
        // Lấy thông tin user
        String[] userInfo = getUserById(userId);
        if (userInfo == null) return -1;

        String customerName  = userInfo[0];
        String customerPhone = (userInfo[2] != null) ? userInfo[2] : "";
        String address       = "";  // Có thể mở rộng sau

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

            // Cập nhật mã đơn #FH-xxxx
            ContentValues codeValues = new ContentValues();
            codeValues.put(COL_O_CODE, "#FH-" + String.format("%04d", orderId));
            db.update(TABLE_ORDERS, codeValues, COL_O_ID + "=?",
                    new String[]{String.valueOf(orderId)});

            // 2. Lưu từng sản phẩm vào order_items
            for (com.example.btl_ltuddd.model.Cart item : cartItems) {
                ContentValues itemValues = new ContentValues();
                itemValues.put(COL_ORDER_ID,   orderId);
                itemValues.put(COL_PRODUCT_ID, item.getProductId());
                itemValues.put(COL_QTY,        item.getQuantity());
                itemValues.put(COL_PRICE,      item.getProductPrice());
                db.insert(TABLE_ORDER_ITEMS, null, itemValues);
            }

            // 3. Xóa giỏ hàng của user
            db.delete("cart", "user_id=?", new String[]{String.valueOf(userId)});

            db.setTransactionSuccessful();
            return orderId;
        } finally {
            db.endTransaction();
        }
    }

    // Hàm thêm vào giỏ hàng

    // Hàm thêm vào giỏ hàng
    public long insertCart(
            int userId,
            int productId
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        // Kiểm tra sản phẩm đã tồn tại trong giỏ chưa
        Cursor cursor =
                db.rawQuery(

                        "SELECT quantity " +
                                "FROM cart " +
                                "WHERE user_id=? " +
                                "AND product_id=?",

                        new String[]{

                                String.valueOf(
                                        userId
                                ),

                                String.valueOf(
                                        productId
                                )
                        }

                );

        if (cursor.moveToFirst()) {

            int quantity =
                    cursor.getInt(0);

            ContentValues values =
                    new ContentValues();

            values.put(
                    "quantity",
                    quantity + 1
            );

            db.update(

                    TABLE_CART,

                    values,

                    "user_id=? AND product_id=?",

                    new String[]{

                            String.valueOf(
                                    userId
                            ),

                            String.valueOf(
                                    productId
                            )

                    }

            );

            cursor.close();

            return 1;
        }

        cursor.close();

        ContentValues values =
                new ContentValues();

        values.put(
                "user_id",
                userId
        );

        values.put(
                "product_id",
                productId
        );

        values.put(
                "quantity",
                1
        );

        return db.insert(

                TABLE_CART,

                null,

                values

        );
    }



    // Lấy sản phẩm trong giỏ hàng
    public List<Product> getCartProducts(int userId) {

        List<Product> list =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        String sql =

                "SELECT " +
                        "p.*, " +
                        "c.quantity " +

                        "FROM cart c " +

                        "INNER JOIN products p " +

                        "ON c.product_id = p.id " +

                        "WHERE c.user_id = ?";

        Cursor cursor =
                db.rawQuery(
                        sql,
                        new String[]{
                                String.valueOf(userId)
                        }
                );

        if (cursor.moveToFirst()) {

            do {

                Product product =
                        new Product();

                product.setId(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("id")
                        )
                );

                product.setName(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("name")
                        )
                );

                product.setPrice(
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow("price")
                        )
                );

                product.setImageUrl(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("image_url")
                        )
                );

                product.setUnit(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("unit")
                        )
                );

                product.setQuantity(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("quantity")
                        )
                );

                list.add(product);

            }

            while (
                    cursor.moveToNext()
            );
        }

        cursor.close();

        return list;
    }


    // Xóa sản phẩm khỏi giỏ
    public boolean removeCart(
            int userId,
            int productId
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        int rows =

                db.delete(

                        TABLE_CART,

                        "user_id=? AND product_id=?",

                        new String[]{

                                String.valueOf(
                                        userId
                                ),

                                String.valueOf(
                                        productId
                                )

                        }

                );

        return rows > 0;

    }



    // Xóa toàn bộ giỏ hàng
    public void clearCart(
            int userId
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        db.delete(

                TABLE_CART,

                "user_id=?",

                new String[]{

                        String.valueOf(
                                userId
                        )

                }

        );

    }

    //Hàm update số lượng
    public void updateCartQuantity(
            int cartId,
            int quantity
    ){

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues cv =
                new ContentValues();

        cv.put(
                "quantity",
                quantity
        );

        db.update(
                "cart",
                cv,
                "id=?",
                new String[]{
                        String.valueOf(cartId)
                }
        );

    }


    public void deleteCart(
            int cartId
    ){

        getWritableDatabase().delete(
                "cart",
                "id=?",
                new String[]{
                        String.valueOf(cartId)
                }
        );

    }

    //Data base trả về list <Cart>
    public List<Cart> getCartItems(
            int userId
    ) {

        List<Cart> list =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(

                        "SELECT " +
                                "c.id," +
                                "c.user_id," +
                                "c.product_id," +
                                "c.quantity," +
                                "p.name," +
                                "p.price," +
                                "p.image_url," +
                                "p.unit " +
                                "FROM cart c " +
                                "INNER JOIN products p " +
                                "ON c.product_id = p.id " +
                                "WHERE c.user_id=?",

                        new String[]{

                                String.valueOf(
                                        userId
                                )

                        }

                );

        if (
                cursor.moveToFirst()
        ) {

            do {

                Cart cart =
                        new Cart();

                cart.setId(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("id")
                        )
                );

                cart.setUserId(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("user_id")
                        )
                );

                cart.setProductId(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("product_id")
                        )
                );

                cart.setQuantity(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("quantity")
                        )
                );

                cart.setProductName(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("name")
                        )
                );

                cart.setProductPrice(
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow("price")
                        )
                );

                cart.setProductImage(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("image_url")
                        )
                );

                cart.setProductUnit(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("unit")
                        )
                );

                list.add(
                        cart
                );

            }

            while (
                    cursor.moveToNext()
            );

        }

        cursor.close();

        return list;
    }

    // Làm với khi khách hàng đặt hàng

}

