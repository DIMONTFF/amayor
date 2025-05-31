package com.example.amayor.FirstActivity.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.amayor.Objetos.Estado;
import com.example.amayor.Objetos.Pedido;
import com.example.amayor.Objetos.Producto;
import com.example.amayor.Objetos.Tienda;
import com.example.amayor.Objetos.TipoTienda;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "aunclick.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_TIENDA = "tienda";
    private static final String TABLE_PRODUCTO = "producto";
    private static final String TABLE_CLIENTE = "cliente";
    private static final String TABLE_REPARTIDOR = "repartidor";
    private static final String TABLE_PEDIDO = "pedido";
    private static final String TABLE_PEDIDO_PRODUCTO = "pedido_producto";

    // Common column names
    private static final String COLUMN_ID = "_id";

    // Tienda columns
    private static final String COLUMN_USUARIO_TIENDA = "usuario";
    private static final String COLUMN_NUM_TELEFONO_TIENDA = "num_telefono";
    private static final String COLUMN_CONTRASENIA_TIENDA = "contrasenia";
    private static final String COLUMN_NOMBRE_TIENDA = "nombre_tienda";
    private static final String COLUMN_TIPO_TIENDA = "tipo_tienda";

    // Producto columns
    private static final String COLUMN_NOMBRE_PRODUCTO = "nombre";
    private static final String COLUMN_PRECIO = "precio";
    private static final String COLUMN_ID_TIENDA = "id_tienda";

    // Cliente columns
    private static final String COLUMN_USUARIO_CLIENTE = "usuario";
    private static final String COLUMN_NUM_TELEFONO_CLIENTE = "num_telefono";
    private static final String COLUMN_CONTRASENIA_CLIENTE = "contrasenia";
    private static final String COLUMN_DIRECCION_CLIENTE = "direccion";

    // Repartidor columns
    private static final String COLUMN_USUARIO_REPARTIDOR = "usuario";
    private static final String COLUMN_NUM_TELEFONO_REPARTIDOR = "num_telefono";
    private static final String COLUMN_CONTRASENIA_REPARTIDOR = "contrasenia";

    // Pedido columns
    private static final String COLUMN_NOMBRE_CLIENTE = "nombre_cliente";
    private static final String COLUMN_DIRECCION_PEDIDO = "direccion";
    private static final String COLUMN_IMPORTE = "importe";
    private static final String COLUMN_ESTADO = "estado";
    private static final String COLUMN_ID_CLIENTE = "id_cliente";
    private static final String COLUMN_ID_TIENDA_PEDIDO = "id_tienda";
    private static final String COLUMN_ID_REPARTIDOR = "id_repartidor";

    // Pedido_Producto columns
    private static final String COLUMN_ID_PEDIDO = "id_pedido";
    private static final String COLUMN_ID_PRODUCTO = "id_producto";
    private static final String COLUMN_CANTIDAD = "cantidad";

    // Create table statements
    private static final String CREATE_TABLE_TIENDA = "CREATE TABLE " + TABLE_TIENDA + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USUARIO_TIENDA + " TEXT NOT NULL UNIQUE, " +
            COLUMN_NUM_TELEFONO_TIENDA + " TEXT NOT NULL, " +
            COLUMN_CONTRASENIA_TIENDA + " TEXT NOT NULL, " +
            COLUMN_NOMBRE_TIENDA + " TEXT NOT NULL, " +
            COLUMN_TIPO_TIENDA + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_PRODUCTO = "CREATE TABLE " + TABLE_PRODUCTO + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NOMBRE_PRODUCTO + " TEXT NOT NULL, " +
            COLUMN_PRECIO + " REAL NOT NULL, " +
            COLUMN_ID_TIENDA + " INTEGER NOT NULL, " +
            "FOREIGN KEY (" + COLUMN_ID_TIENDA + ") REFERENCES " + TABLE_TIENDA + "(" + COLUMN_ID + "));";

    private static final String CREATE_TABLE_CLIENTE = "CREATE TABLE " + TABLE_CLIENTE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USUARIO_CLIENTE + " TEXT NOT NULL UNIQUE, " +
            COLUMN_NUM_TELEFONO_CLIENTE + " TEXT NOT NULL, " +
            COLUMN_CONTRASENIA_CLIENTE + " TEXT NOT NULL, " +
            COLUMN_DIRECCION_CLIENTE + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_REPARTIDOR = "CREATE TABLE " + TABLE_REPARTIDOR + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USUARIO_REPARTIDOR + " TEXT NOT NULL UNIQUE, " +
            COLUMN_NUM_TELEFONO_REPARTIDOR + " TEXT NOT NULL, " +
            COLUMN_CONTRASENIA_REPARTIDOR + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_PEDIDO = "CREATE TABLE " + TABLE_PEDIDO + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NOMBRE_CLIENTE + " TEXT NOT NULL, " +
            COLUMN_DIRECCION_PEDIDO + " TEXT NOT NULL, " +
            COLUMN_IMPORTE + " REAL NOT NULL, " +
            COLUMN_ESTADO + " TEXT NOT NULL, " +
            COLUMN_ID_CLIENTE + " INTEGER NOT NULL, " +
            COLUMN_ID_TIENDA_PEDIDO + " INTEGER NOT NULL, " +
            COLUMN_ID_REPARTIDOR + " INTEGER, " +
            "FOREIGN KEY (" + COLUMN_ID_CLIENTE + ") REFERENCES " + TABLE_CLIENTE + "(" + COLUMN_ID + "), " +
            "FOREIGN KEY (" + COLUMN_ID_TIENDA_PEDIDO + ") REFERENCES " + TABLE_TIENDA + "(" + COLUMN_ID + "), " +
            "FOREIGN KEY (" + COLUMN_ID_REPARTIDOR + ") REFERENCES " + TABLE_REPARTIDOR + "(" + COLUMN_ID + "));";

    private static final String CREATE_TABLE_PEDIDO_PRODUCTO = "CREATE TABLE " + TABLE_PEDIDO_PRODUCTO + " (" +
            COLUMN_ID_PEDIDO + " INTEGER NOT NULL, " +
            COLUMN_ID_PRODUCTO + " INTEGER NOT NULL, " +
            COLUMN_CANTIDAD + " INTEGER NOT NULL, " +
            "PRIMARY KEY (" + COLUMN_ID_PEDIDO + ", " + COLUMN_ID_PRODUCTO + "), " +
            "FOREIGN KEY (" + COLUMN_ID_PEDIDO + ") REFERENCES " + TABLE_PEDIDO + "(" + COLUMN_ID + "), " +
            "FOREIGN KEY (" + COLUMN_ID_PRODUCTO + ") REFERENCES " + TABLE_PRODUCTO + "(" + COLUMN_ID + "));";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TIENDA);
        db.execSQL(CREATE_TABLE_PRODUCTO);
        db.execSQL(CREATE_TABLE_CLIENTE);
        db.execSQL(CREATE_TABLE_REPARTIDOR);
        db.execSQL(CREATE_TABLE_PEDIDO);
        db.execSQL(CREATE_TABLE_PEDIDO_PRODUCTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEDIDO_PRODUCTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEDIDO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPARTIDOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIENDA);
        onCreate(db);
    }

    public void deleteAllData() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("DELETE FROM " + TABLE_PEDIDO_PRODUCTO);
            db.execSQL("DELETE FROM " + TABLE_PEDIDO);
            db.execSQL("DELETE FROM " + TABLE_PRODUCTO);
            db.execSQL("DELETE FROM " + TABLE_CLIENTE);
            db.execSQL("DELETE FROM " + TABLE_REPARTIDOR);
            db.execSQL("DELETE FROM " + TABLE_TIENDA);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void resetAutoIncrement() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("DELETE FROM sqlite_sequence");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Validate Cliente login and return ID
    public int validateCliente(String usuario, String contrasenia) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_USUARIO_CLIENTE + " = ? AND " + COLUMN_CONTRASENIA_CLIENTE + " = ?";
        String[] selectionArgs = {usuario, contrasenia};
        Cursor cursor = db.query(TABLE_CLIENTE, new String[]{COLUMN_ID}, selection, selectionArgs, null, null, null);
        int id = -1;
        try {
            if (cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            }
        } finally {
            cursor.close();
            db.close();
        }
        return id;
    }

    // Validate Tienda login and return ID and tipo_tienda
    public Tienda validateTienda(String usuario, String contrasenia) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_USUARIO_TIENDA + " = ? AND " + COLUMN_CONTRASENIA_TIENDA + " = ?";
        String[] selectionArgs = {usuario, contrasenia};
        Cursor cursor = db.query(TABLE_TIENDA, null, selection, selectionArgs, null, null, null);
        Tienda tienda = null;
        try {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String user = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USUARIO_TIENDA));
                String telefono = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NUM_TELEFONO_TIENDA));
                String pass = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTRASENIA_TIENDA));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE_TIENDA));
                String tipo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPO_TIENDA));
                tienda = new Tienda(id, user, telefono, pass, nombre, TipoTienda.valueOf(tipo));
            }
        } finally {
            cursor.close();
            db.close();
        }
        return tienda;
    }

    // Validate Repartidor login and return ID
    public int validateRepartidor(String usuario, String contrasenia) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_USUARIO_REPARTIDOR + " = ? AND " + COLUMN_CONTRASENIA_REPARTIDOR + " = ?";
        String[] selectionArgs = {usuario, contrasenia};
        Cursor cursor = db.query(TABLE_REPARTIDOR, new String[]{COLUMN_ID}, selection, selectionArgs, null, null, null);
        int id = -1;
        try {
            if (cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            }
        } finally {
            cursor.close();
            db.close();
        }
        return id;
    }

    // Check if usuario exists in cliente table
    public boolean usuarioExistsCliente(String usuario) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_USUARIO_CLIENTE + " = ?";
        String[] selectionArgs = {usuario};
        Cursor cursor = db.query(TABLE_CLIENTE, new String[]{COLUMN_ID}, selection, selectionArgs, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Check if usuario exists in tienda table
    public boolean usuarioExistsTienda(String usuario) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_USUARIO_TIENDA + " = ?";
        String[] selectionArgs = {usuario};
        Cursor cursor = db.query(TABLE_TIENDA, new String[]{COLUMN_ID}, selection, selectionArgs, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Check if usuario exists in repartidor table
    public boolean usuarioExistsRepartidor(String usuario) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_USUARIO_REPARTIDOR + " = ?";
        String[] selectionArgs = {usuario};
        Cursor cursor = db.query(TABLE_REPARTIDOR, new String[]{COLUMN_ID}, selection, selectionArgs, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Insert a new Cliente
    public long insertCliente(String usuario, String telefono, String contrasenia, String direccion) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO_CLIENTE, usuario);
        values.put(COLUMN_NUM_TELEFONO_CLIENTE, telefono);
        values.put(COLUMN_CONTRASENIA_CLIENTE, contrasenia);
        values.put(COLUMN_DIRECCION_CLIENTE, direccion);
        long id = db.insert(TABLE_CLIENTE, null, values);
        db.close();
        return id;
    }

    // Insert a new Tienda
    public long insertTienda(String usuario, String telefono, String contrasenia, String nombreTienda, TipoTienda tipoTienda) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO_TIENDA, usuario);
        values.put(COLUMN_NUM_TELEFONO_TIENDA, telefono);
        values.put(COLUMN_CONTRASENIA_TIENDA, contrasenia);
        values.put(COLUMN_NOMBRE_TIENDA, nombreTienda);
        values.put(COLUMN_TIPO_TIENDA, tipoTienda.toString());
        long id = db.insert(TABLE_TIENDA, null, values);
        db.close();
        return id;
    }

    // Insert a new Repartidor
    public long insertRepartidor(String usuario, String telefono, String contrasenia) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO_REPARTIDOR, usuario);
        values.put(COLUMN_NUM_TELEFONO_REPARTIDOR, telefono);
        values.put(COLUMN_CONTRASENIA_REPARTIDOR, contrasenia);
        long id = db.insert(TABLE_REPARTIDOR, null, values);
        db.close();
        return id;
    }

    // Fetch all Tiendas
    public List<Tienda> getAllTiendas() {
        List<Tienda> tiendas = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_TIENDA, null, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int usuarioIndex = cursor.getColumnIndex(COLUMN_USUARIO_TIENDA);
                int telefonoIndex = cursor.getColumnIndex(COLUMN_NUM_TELEFONO_TIENDA);
                int contraseniaIndex = cursor.getColumnIndex(COLUMN_CONTRASENIA_TIENDA);
                int nombreIndex = cursor.getColumnIndex(COLUMN_NOMBRE_TIENDA);
                int tipoIndex = cursor.getColumnIndex(COLUMN_TIPO_TIENDA);

                do {
                    int id = cursor.getInt(idIndex);
                    String usuario = cursor.getString(usuarioIndex);
                    String telefono = cursor.getString(telefonoIndex);
                    String contrasenia = cursor.getString(contraseniaIndex);
                    String nombre = cursor.getString(nombreIndex);
                    String tipo = cursor.getString(tipoIndex);
                    Tienda tienda = new Tienda(id, usuario, telefono, contrasenia, nombre, TipoTienda.valueOf(tipo));
                    tiendas.add(tienda);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }
        return tiendas;
    }

    // Fetch Productos by id_tienda
    public List<Producto> getProductosByTienda(int idTienda) {
        List<Producto> productos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_ID_TIENDA + " = ?";
        String[] selectionArgs = {String.valueOf(idTienda)};
        Cursor cursor = db.query(TABLE_PRODUCTO, null, selection, selectionArgs, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int nombreIndex = cursor.getColumnIndex(COLUMN_NOMBRE_PRODUCTO);
                int precioIndex = cursor.getColumnIndex(COLUMN_PRECIO);
                int idTiendaIndex = cursor.getColumnIndex(COLUMN_ID_TIENDA);

                do {
                    int id = cursor.getInt(idIndex);
                    String nombre = cursor.getString(nombreIndex);
                    double precio = cursor.getDouble(precioIndex);
                    int tiendaId = cursor.getInt(idTiendaIndex);
                    Producto producto = new Producto(id, tiendaId, nombre, precio);
                    productos.add(producto);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }
        return productos;
    }

    public boolean updatePedidoEstado(int idPedido, Estado nuevoEstado) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ESTADO, nuevoEstado.toString());
        int rowsAffected = db.update(TABLE_PEDIDO, values, COLUMN_ID + " = ?", new String[]{String.valueOf(idPedido)});
        db.close();
        return rowsAffected > 0;
    }

    // Delete a Producto by id
    public void deleteProducto(int idProducto) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_PEDIDO_PRODUCTO, COLUMN_ID_PRODUCTO + " = ?", new String[]{String.valueOf(idProducto)});
            db.delete(TABLE_PRODUCTO, COLUMN_ID + " = ?", new String[]{String.valueOf(idProducto)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Delete a Pedido by id
    public boolean deletePedido(int idPedido) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_PEDIDO_PRODUCTO, COLUMN_ID_PEDIDO + " = ?", new String[]{String.valueOf(idPedido)});
            int rowsAffected = db.delete(TABLE_PEDIDO, COLUMN_ID + " = ?", new String[]{String.valueOf(idPedido)});
            db.setTransactionSuccessful();
            return rowsAffected > 0;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public List<Pedido> getPedidosByTiendaAndEstado(int idTienda) {
        List<Pedido> pedidos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_ID_TIENDA_PEDIDO + " = ? AND " + COLUMN_ESTADO + " IN (?, ?)";
        String[] selectionArgs = {String.valueOf(idTienda), Estado.PREP.toString(), Estado.NOPREP.toString()};
        Cursor cursor = db.query(TABLE_PEDIDO, null, selection, selectionArgs, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int nombreClienteIndex = cursor.getColumnIndex(COLUMN_NOMBRE_CLIENTE);
                int direccionIndex = cursor.getColumnIndex(COLUMN_DIRECCION_PEDIDO);
                int importeIndex = cursor.getColumnIndex(COLUMN_IMPORTE);
                int estadoIndex = cursor.getColumnIndex(COLUMN_ESTADO);
                int idClienteIndex = cursor.getColumnIndex(COLUMN_ID_CLIENTE);
                int idTiendaIndex = cursor.getColumnIndex(COLUMN_ID_TIENDA_PEDIDO);
                int idRepartidorIndex = cursor.getColumnIndex(COLUMN_ID_REPARTIDOR);

                do {
                    int id = cursor.getInt(idIndex);
                    String nombreCliente = cursor.getString(nombreClienteIndex);
                    String direccion = cursor.getString(direccionIndex);
                    double importe = cursor.getDouble(importeIndex);
                    String estadoStr = cursor.getString(estadoIndex);
                    int idCliente = cursor.getInt(idClienteIndex);
                    int tiendaId = cursor.getInt(idTiendaIndex);
                    Integer idRepartidor = cursor.isNull(idRepartidorIndex) ? null : cursor.getInt(idRepartidorIndex);
                    Estado estado = Estado.valueOf(estadoStr);

                    LinkedHashMap<Producto, Integer> descripcion = new LinkedHashMap<>();
                    String productoSelection = COLUMN_ID_PEDIDO + " = ?";
                    String[] productoSelectionArgs = {String.valueOf(id)};
                    Cursor productoCursor = db.query(TABLE_PEDIDO_PRODUCTO, new String[]{COLUMN_ID_PRODUCTO, COLUMN_CANTIDAD}, productoSelection, productoSelectionArgs, null, null, null);
                    try {
                        if (productoCursor.moveToFirst()) {
                            int idProductoIndex = productoCursor.getColumnIndex(COLUMN_ID_PRODUCTO);
                            int cantidadIndex = productoCursor.getColumnIndex(COLUMN_CANTIDAD);
                            do {
                                int idProducto = productoCursor.getInt(idProductoIndex);
                                int cantidad = productoCursor.getInt(cantidadIndex);
                                String productoDetailSelection = COLUMN_ID + " = ?";
                                String[] productoDetailArgs = {String.valueOf(idProducto)};
                                Cursor productoDetailCursor = db.query(TABLE_PRODUCTO, null, productoDetailSelection, productoDetailArgs, null, null, null);
                                if (productoDetailCursor.moveToFirst()) {
                                    int prodIdIndex = productoDetailCursor.getColumnIndex(COLUMN_ID);
                                    int prodNombreIndex = productoDetailCursor.getColumnIndex(COLUMN_NOMBRE_PRODUCTO);
                                    int prodPrecioIndex = productoDetailCursor.getColumnIndex(COLUMN_PRECIO);
                                    int prodIdTiendaIndex = productoDetailCursor.getColumnIndex(COLUMN_ID_TIENDA);
                                    int prodId = productoDetailCursor.getInt(prodIdIndex);
                                    String prodNombre = productoDetailCursor.getString(prodNombreIndex);
                                    double prodPrecio = productoDetailCursor.getDouble(prodPrecioIndex);
                                    int prodTiendaId = productoDetailCursor.getInt(prodIdTiendaIndex);
                                    Producto producto = new Producto(prodId, prodTiendaId, prodNombre, prodPrecio);
                                    descripcion.put(producto, cantidad);
                                }
                                productoDetailCursor.close();
                            } while (productoCursor.moveToNext());
                        }
                    } finally {
                        productoCursor.close();
                    }

                    Pedido pedido = new Pedido(id, idCliente, tiendaId, idRepartidor, nombreCliente, direccion, importe, descripcion, estado);
                    pedidos.add(pedido);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }
        return pedidos;
    }

    public List<Pedido> getCitasByTienda(int idTienda) {
        List<Pedido> citas = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_ID_TIENDA_PEDIDO + " = ? AND " + COLUMN_IMPORTE + " = ? AND " + COLUMN_ESTADO + " IN (?, ?)";
        String[] selectionArgs = {String.valueOf(idTienda), "0.0", Estado.PREP.toString(), Estado.NOPREP.toString()};
        Cursor cursor = db.query(TABLE_PEDIDO, null, selection, selectionArgs, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int nombreClienteIndex = cursor.getColumnIndex(COLUMN_NOMBRE_CLIENTE);
                int direccionIndex = cursor.getColumnIndex(COLUMN_DIRECCION_PEDIDO);
                int importeIndex = cursor.getColumnIndex(COLUMN_IMPORTE);
                int estadoIndex = cursor.getColumnIndex(COLUMN_ESTADO);
                int idClienteIndex = cursor.getColumnIndex(COLUMN_ID_CLIENTE);
                int idTiendaIndex = cursor.getColumnIndex(COLUMN_ID_TIENDA_PEDIDO);
                int idRepartidorIndex = cursor.getColumnIndex(COLUMN_ID_REPARTIDOR);

                do {
                    int id = cursor.getInt(idIndex);
                    String nombreCliente = cursor.getString(nombreClienteIndex);
                    String direccion = cursor.getString(direccionIndex);
                    double importe = cursor.getDouble(importeIndex);
                    String estadoStr = cursor.getString(estadoIndex);
                    int idCliente = cursor.getInt(idClienteIndex);
                    int tiendaId = cursor.getInt(idTiendaIndex);
                    Integer idRepartidor = cursor.isNull(idRepartidorIndex) ? null : cursor.getInt(idRepartidorIndex);
                    Estado estado = Estado.valueOf(estadoStr);
                    LinkedHashMap<Producto, Integer> descripcion = new LinkedHashMap<>();
                    Pedido cita = new Pedido(id, idCliente, tiendaId, idRepartidor, nombreCliente, direccion, importe, descripcion, estado);
                    citas.add(cita);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }
        return citas;
    }

    public long insertPedidoCita(Pedido pedido) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CLIENTE, pedido.getIdCliente());
        values.put(COLUMN_ID_TIENDA_PEDIDO, pedido.getIdTienda());
        if (pedido.getIdRepartidor() != null) {
            values.put(COLUMN_ID_REPARTIDOR, pedido.getIdRepartidor());
        } else {
            values.putNull(COLUMN_ID_REPARTIDOR);
        }
        values.put(COLUMN_NOMBRE_CLIENTE, pedido.getNombreCliente());
        values.put(COLUMN_DIRECCION_PEDIDO, pedido.getDireccion());
        values.put(COLUMN_IMPORTE, pedido.getImporte());
        values.put(COLUMN_ESTADO, pedido.getEstado().toString());
        long pedidoId = db.insert(TABLE_PEDIDO, null, values);
        db.close();
        return pedidoId;
    }

    public static class ClienteInfo {
        public String nombre;
        public String direccion;

        public ClienteInfo(String nombre, String direccion) {
            this.nombre = nombre;
            this.direccion = direccion;
        }
    }

    public ClienteInfo getClienteInfoById(int idCliente) {
        SQLiteDatabase db = getReadableDatabase();
        String nombre = null;
        String direccion = null;
        Cursor clienteCursor = db.query(TABLE_CLIENTE, new String[]{COLUMN_USUARIO_CLIENTE, COLUMN_DIRECCION_CLIENTE},
                COLUMN_ID + " = ?", new String[]{String.valueOf(idCliente)}, null, null, null);
        try {
            if (clienteCursor.moveToFirst()) {
                int usuarioIndex = clienteCursor.getColumnIndex(COLUMN_USUARIO_CLIENTE);
                int direccionIndex = clienteCursor.getColumnIndex(COLUMN_DIRECCION_CLIENTE);
                nombre = clienteCursor.isNull(usuarioIndex) ? null : clienteCursor.getString(usuarioIndex);
                direccion = clienteCursor.isNull(direccionIndex) ? null : clienteCursor.getString(direccionIndex);
            }
        } finally {
            clienteCursor.close();
        }
        db.close();
        return new ClienteInfo(nombre, direccion);
    }

    public ContentValues getClienteDetails(int idCliente) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues details = new ContentValues();
        Cursor clienteCursor = db.query(TABLE_CLIENTE, new String[]{COLUMN_USUARIO_CLIENTE, COLUMN_DIRECCION_CLIENTE},
                COLUMN_ID + " = ?", new String[]{String.valueOf(idCliente)}, null, null, null);
        try {
            if (clienteCursor.moveToFirst()) {
                int usuarioIndex = clienteCursor.getColumnIndex(COLUMN_USUARIO_CLIENTE);
                int direccionIndex = clienteCursor.getColumnIndex(COLUMN_DIRECCION_CLIENTE);
                if (!clienteCursor.isNull(usuarioIndex)) {
                    details.put(COLUMN_USUARIO_CLIENTE, clienteCursor.getString(usuarioIndex));
                }
                if (!clienteCursor.isNull(direccionIndex)) {
                    details.put(COLUMN_DIRECCION_CLIENTE, clienteCursor.getString(direccionIndex));
                }
            }
        } finally {
            clienteCursor.close();
        }
        db.close();
        return details;
    }

    public long insertPedido(int idCliente, int idTienda, String nombreCliente, String direccion, double importe, String estado, Map<Producto, Integer> productos) {
        SQLiteDatabase db = getWritableDatabase();
        long pedidoId = -1;
        db.beginTransaction();
        try {
            ContentValues pedidoValues = new ContentValues();
            pedidoValues.put(COLUMN_ID_CLIENTE, idCliente);
            pedidoValues.put(COLUMN_ID_TIENDA_PEDIDO, idTienda);
            pedidoValues.put(COLUMN_NOMBRE_CLIENTE, nombreCliente);
            pedidoValues.put(COLUMN_DIRECCION_PEDIDO, direccion);
            pedidoValues.put(COLUMN_IMPORTE, importe);
            pedidoValues.put(COLUMN_ESTADO, estado);
            pedidoId = db.insertOrThrow(TABLE_PEDIDO, null, pedidoValues);

            for (Map.Entry<Producto, Integer> entry : productos.entrySet()) {
                ContentValues productoValues = new ContentValues();
                productoValues.put(COLUMN_ID_PEDIDO, pedidoId);
                productoValues.put(COLUMN_ID_PRODUCTO, entry.getKey().getIdProducto());
                productoValues.put(COLUMN_CANTIDAD, entry.getValue());
                db.insertOrThrow(TABLE_PEDIDO_PRODUCTO, null, productoValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            pedidoId = -1;
        } finally {
            db.endTransaction();
            db.close();
        }
        return pedidoId;
    }

    // Wrapper class to hold Pedido and nombre_tienda
    public static class PedidoConTienda {
        private final Pedido pedido;
        private final String nombreTienda;

        public PedidoConTienda(Pedido pedido, String nombreTienda) {
            this.pedido = pedido;
            this.nombreTienda = nombreTienda;
        }

        public Pedido getPedido() {
            return pedido;
        }

        public String getNombreTienda() {
            return nombreTienda;
        }
    }

    // Fetch prepared pedidos from GENERAL or FARMACIA tiendas for repartidor
    public List<PedidoConTienda> getPreparedPedidosForRepartidor() {
        List<PedidoConTienda> pedidos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT p." + COLUMN_ID + " AS id_pedido, p." + COLUMN_NOMBRE_CLIENTE + ", p." + COLUMN_DIRECCION_PEDIDO + ", " +
                "p." + COLUMN_IMPORTE + ", p." + COLUMN_ESTADO + ", p." + COLUMN_ID_CLIENTE + ", " +
                "p." + COLUMN_ID_TIENDA_PEDIDO + ", p." + COLUMN_ID_REPARTIDOR + ", t." + COLUMN_NOMBRE_TIENDA +
                " FROM " + TABLE_PEDIDO + " p" +
                " INNER JOIN " + TABLE_TIENDA + " t ON p." + COLUMN_ID_TIENDA_PEDIDO + " = t." + COLUMN_ID +
                " WHERE p." + COLUMN_ESTADO + " = ? AND t." + COLUMN_TIPO_TIENDA + " IN (?, ?)";
        String[] selectionArgs = {Estado.PREP.toString(), TipoTienda.GENERAL.toString(), TipoTienda.FARMACIA.toString()};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        try {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id_pedido");
                int nombreClienteIndex = cursor.getColumnIndex(COLUMN_NOMBRE_CLIENTE);
                int direccionIndex = cursor.getColumnIndex(COLUMN_DIRECCION_PEDIDO);
                int importeIndex = cursor.getColumnIndex(COLUMN_IMPORTE);
                int estadoIndex = cursor.getColumnIndex(COLUMN_ESTADO);
                int idClienteIndex = cursor.getColumnIndex(COLUMN_ID_CLIENTE);
                int idTiendaIndex = cursor.getColumnIndex(COLUMN_ID_TIENDA_PEDIDO);
                int idRepartidorIndex = cursor.getColumnIndex(COLUMN_ID_REPARTIDOR);
                int nombreTiendaIndex = cursor.getColumnIndex(COLUMN_NOMBRE_TIENDA);

                do {
                    int id = cursor.getInt(idIndex);
                    String nombreCliente = cursor.getString(nombreClienteIndex);
                    String direccion = cursor.getString(direccionIndex);
                    double importe = cursor.getDouble(importeIndex);
                    String estadoStr = cursor.getString(estadoIndex);
                    int idCliente = cursor.getInt(idClienteIndex);
                    int tiendaId = cursor.getInt(idTiendaIndex);
                    Integer idRepartidor = cursor.isNull(idRepartidorIndex) ? null : cursor.getInt(idRepartidorIndex);
                    String nombreTienda = cursor.getString(nombreTiendaIndex);
                    Estado estado = Estado.valueOf(estadoStr);

                    LinkedHashMap<Producto, Integer> descripcion = new LinkedHashMap<>();
                    String productoSelection = COLUMN_ID_PEDIDO + " = ?";
                    String[] productoSelectionArgs = {String.valueOf(id)};
                    Cursor productoCursor = db.query(TABLE_PEDIDO_PRODUCTO, new String[]{COLUMN_ID_PRODUCTO, COLUMN_CANTIDAD}, productoSelection, productoSelectionArgs, null, null, null);
                    try {
                        if (productoCursor.moveToFirst()) {
                            int idProductoIndex = productoCursor.getColumnIndex(COLUMN_ID_PRODUCTO);
                            int cantidadIndex = productoCursor.getColumnIndex(COLUMN_CANTIDAD);
                            do {
                                int idProducto = productoCursor.getInt(idProductoIndex);
                                int cantidad = productoCursor.getInt(cantidadIndex);
                                String productoDetailSelection = COLUMN_ID + " = ?";
                                String[] productoDetailArgs = {String.valueOf(idProducto)};
                                Cursor productoDetailCursor = db.query(TABLE_PRODUCTO, null, productoDetailSelection, productoDetailArgs, null, null, null);
                                if (productoDetailCursor.moveToFirst()) {
                                    int prodIdIndex = productoDetailCursor.getColumnIndex(COLUMN_ID);
                                    int prodNombreIndex = productoDetailCursor.getColumnIndex(COLUMN_NOMBRE_PRODUCTO);
                                    int prodPrecioIndex = productoDetailCursor.getColumnIndex(COLUMN_PRECIO);
                                    int prodIdTiendaIndex = productoDetailCursor.getColumnIndex(COLUMN_ID_TIENDA);
                                    int prodId = productoDetailCursor.getInt(prodIdIndex);
                                    String prodNombre = productoDetailCursor.getString(prodNombreIndex);
                                    double prodPrecio = productoDetailCursor.getDouble(prodPrecioIndex);
                                    int prodTiendaId = productoDetailCursor.getInt(prodIdTiendaIndex);
                                    Producto producto = new Producto(prodId, prodTiendaId, prodNombre, prodPrecio);
                                    descripcion.put(producto, cantidad);
                                }
                                productoDetailCursor.close();
                            } while (productoCursor.moveToNext());
                        }
                    } finally {
                        productoCursor.close();
                    }

                    Pedido pedido = new Pedido(id, idCliente, tiendaId, idRepartidor, nombreCliente, direccion, importe, descripcion, estado);
                    pedidos.add(new PedidoConTienda(pedido, nombreTienda));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }
        return pedidos;
    }

    public void insertSampleData() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues tienda1 = new ContentValues();
            tienda1.put(COLUMN_USUARIO_TIENDA, "store1");
            tienda1.put(COLUMN_NUM_TELEFONO_TIENDA, "123456789");
            tienda1.put(COLUMN_CONTRASENIA_TIENDA, "pass1");
            tienda1.put(COLUMN_NOMBRE_TIENDA, "SuperMart");
            tienda1.put(COLUMN_TIPO_TIENDA, TipoTienda.GENERAL.toString());
            long idTienda1 = db.insert(TABLE_TIENDA, null, tienda1);

            ContentValues tienda2 = new ContentValues();
            tienda2.put(COLUMN_USUARIO_TIENDA, "store2");
            tienda2.put(COLUMN_NUM_TELEFONO_TIENDA, "987654321");
            tienda2.put(COLUMN_CONTRASENIA_TIENDA, "pass2");
            tienda2.put(COLUMN_NOMBRE_TIENDA, "FarmaVida");
            tienda2.put(COLUMN_TIPO_TIENDA, TipoTienda.FARMACIA.toString());
            long idTienda2 = db.insert(TABLE_TIENDA, null, tienda2);

            ContentValues tienda3 = new ContentValues();
            tienda3.put(COLUMN_USUARIO_TIENDA, "store3");
            tienda3.put(COLUMN_NUM_TELEFONO_TIENDA, "555555555");
            tienda3.put(COLUMN_CONTRASENIA_TIENDA, "pass3");
            tienda3.put(COLUMN_NOMBRE_TIENDA, "Miscelanea");
            tienda3.put(COLUMN_TIPO_TIENDA, TipoTienda.OTROS.toString());
            long idTienda3 = db.insert(TABLE_TIENDA, null, tienda3);

            ContentValues producto1 = new ContentValues();
            producto1.put(COLUMN_NOMBRE_PRODUCTO, "Leche");
            producto1.put(COLUMN_PRECIO, 2.50);
            producto1.put(COLUMN_ID_TIENDA, idTienda1);
            long idProducto1 = db.insert(TABLE_PRODUCTO, null, producto1);

            ContentValues producto2 = new ContentValues();
            producto2.put(COLUMN_NOMBRE_PRODUCTO, "Pan");
            producto2.put(COLUMN_PRECIO, 1.20);
            producto2.put(COLUMN_ID_TIENDA, idTienda1);
            long idProducto2 = db.insert(TABLE_PRODUCTO, null, producto2);

            ContentValues producto3 = new ContentValues();
            producto3.put(COLUMN_NOMBRE_PRODUCTO, "Paracetamol");
            producto3.put(COLUMN_PRECIO, 5.00);
            producto3.put(COLUMN_ID_TIENDA, idTienda2);
            long idProducto3 = db.insert(TABLE_PRODUCTO, null, producto3);

            ContentValues producto4 = new ContentValues();
            producto4.put(COLUMN_NOMBRE_PRODUCTO, "Jabón");
            producto4.put(COLUMN_PRECIO, 3.00);
            producto4.put(COLUMN_ID_TIENDA, idTienda2);
            long idProducto4 = db.insert(TABLE_PRODUCTO, null, producto4);

            ContentValues producto5 = new ContentValues();
            producto5.put(COLUMN_NOMBRE_PRODUCTO, "Cuaderno");
            producto5.put(COLUMN_PRECIO, 4.00);
            producto5.put(COLUMN_ID_TIENDA, idTienda3);
            db.insert(TABLE_PRODUCTO, null, producto5);

            ContentValues cliente = new ContentValues();
            cliente.put(COLUMN_USUARIO_CLIENTE, "juan");
            cliente.put(COLUMN_NUM_TELEFONO_CLIENTE, "123456789");
            cliente.put(COLUMN_CONTRASENIA_CLIENTE, "pass123");
            cliente.put(COLUMN_DIRECCION_CLIENTE, "Calle 123");
            long idCliente = db.insert(TABLE_CLIENTE, null, cliente);

            ContentValues repartidor = new ContentValues();
            repartidor.put(COLUMN_USUARIO_REPARTIDOR, "repartidor1");
            repartidor.put(COLUMN_NUM_TELEFONO_REPARTIDOR, "111222333");
            repartidor.put(COLUMN_CONTRASENIA_REPARTIDOR, "passrep1");
            db.insert(TABLE_REPARTIDOR, null, repartidor);

            ContentValues pedido = new ContentValues();
            pedido.put(COLUMN_ID_CLIENTE, idCliente);
            pedido.put(COLUMN_ID_TIENDA_PEDIDO, idTienda1);
            pedido.put(COLUMN_NOMBRE_CLIENTE, "juan");
            pedido.put(COLUMN_DIRECCION_PEDIDO, "Calle 123");
            pedido.put(COLUMN_IMPORTE, 6.20);
            pedido.put(COLUMN_ESTADO, Estado.PREP.toString());
            long idPedido = db.insert(TABLE_PEDIDO, null, pedido);

            ContentValues pedidoProducto1 = new ContentValues();
            pedidoProducto1.put(COLUMN_ID_PEDIDO, idPedido);
            pedidoProducto1.put(COLUMN_ID_PRODUCTO, idProducto1);
            pedidoProducto1.put(COLUMN_CANTIDAD, 2);
            db.insert(TABLE_PEDIDO_PRODUCTO, null, pedidoProducto1);

            ContentValues pedidoProducto2 = new ContentValues();
            pedidoProducto2.put(COLUMN_ID_PEDIDO, idPedido);
            pedidoProducto2.put(COLUMN_ID_PRODUCTO, idProducto2);
            pedidoProducto2.put(COLUMN_CANTIDAD, 1);
            db.insert(TABLE_PEDIDO_PRODUCTO, null, pedidoProducto2);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}