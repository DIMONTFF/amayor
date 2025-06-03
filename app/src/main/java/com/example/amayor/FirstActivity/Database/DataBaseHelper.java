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
    private static final int DATABASE_VERSION = 3;

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
    private static final String COLUMN_IMAGEN = "imagen";

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
            COLUMN_IMAGEN + " BLOB, " +
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
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_PRODUCTO + " ADD COLUMN " + COLUMN_IMAGEN + " BLOB");
        }
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

    public long insertTienda(String usuario, String telefono, String contrasenia, String nombreTienda, TipoTienda tipoTienda) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO_TIENDA, usuario);
        values.put(COLUMN_NUM_TELEFONO_TIENDA, telefono);
        values.put(COLUMN_CONTRASENIA_TIENDA, contrasenia);
        values.put(COLUMN_NOMBRE_TIENDA, nombreTienda);
        values.put(COLUMN_TIPO_TIENDA, tipoTienda.toString());
        long neighbor_id = db.insert(TABLE_TIENDA, null, values);
        db.close();
        return neighbor_id;
    }

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

    public boolean updateCliente(int idCliente, String usuario, String telefono, String direccion) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO_CLIENTE, usuario);
        values.put(COLUMN_NUM_TELEFONO_CLIENTE, telefono);
        values.put(COLUMN_DIRECCION_CLIENTE, direccion);
        int rowsAffected = db.update(TABLE_CLIENTE, values, COLUMN_ID + " = ?", new String[]{String.valueOf(idCliente)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean updateRepartidor(int idRepartidor, String usuario, String telefono) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO_REPARTIDOR, usuario);
        values.put(COLUMN_NUM_TELEFONO_REPARTIDOR, telefono);
        int rowsAffected = db.update(TABLE_REPARTIDOR, values, COLUMN_ID + " = ?", new String[]{String.valueOf(idRepartidor)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean updateTienda(int idTienda, String usuario, String telefono, String nombreTienda, String tipoTienda) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO_TIENDA, usuario);
        values.put(COLUMN_NUM_TELEFONO_TIENDA, telefono);
        values.put(COLUMN_NOMBRE_TIENDA, nombreTienda);
        values.put(COLUMN_TIPO_TIENDA, tipoTienda);
        int rowsAffected = db.update(TABLE_TIENDA, values, COLUMN_ID + " = ?", new String[]{String.valueOf(idTienda)});
        db.close();
        return rowsAffected > 0;
    }

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
                int imagenIndex = cursor.getColumnIndex(COLUMN_IMAGEN);

                do {
                    int id = cursor.getInt(idIndex);
                    String nombre = cursor.getString(nombreIndex);
                    double precio = cursor.getDouble(precioIndex);
                    int tiendaId = cursor.getInt(idTiendaIndex);
                    byte[] imagen = imagenIndex != -1 && !cursor.isNull(imagenIndex) ? cursor.getBlob(imagenIndex) : null;
                    Producto producto = new Producto(id, tiendaId, nombre, precio, imagen);
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
                                    int prodImagenIndex = productoDetailCursor.getColumnIndex(COLUMN_IMAGEN);
                                    int prodId = productoDetailCursor.getInt(prodIdIndex);
                                    String prodNombre = productoDetailCursor.getString(prodNombreIndex);
                                    double prodPrecio = productoDetailCursor.getDouble(prodPrecioIndex);
                                    int prodTiendaId = productoDetailCursor.getInt(prodIdTiendaIndex);
                                    byte[] prodImagen = prodImagenIndex != -1 && !productoDetailCursor.isNull(prodImagenIndex) ? productoDetailCursor.getBlob(prodImagenIndex) : null;
                                    Producto producto = new Producto(prodId, prodTiendaId, prodNombre, prodPrecio, prodImagen);
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
        Cursor clienteCursor = db.query(TABLE_CLIENTE,
                new String[]{COLUMN_USUARIO_CLIENTE, COLUMN_NUM_TELEFONO_CLIENTE, COLUMN_DIRECCION_CLIENTE},
                COLUMN_ID + " = ?", new String[]{String.valueOf(idCliente)}, null, null, null);
        try {
            if (clienteCursor.moveToFirst()) {
                int usuarioIndex = clienteCursor.getColumnIndex(COLUMN_USUARIO_CLIENTE);
                int telefonoIndex = clienteCursor.getColumnIndex(COLUMN_NUM_TELEFONO_CLIENTE);
                int direccionIndex = clienteCursor.getColumnIndex(COLUMN_DIRECCION_CLIENTE);
                if (!clienteCursor.isNull(usuarioIndex)) {
                    details.put("usuario", clienteCursor.getString(usuarioIndex));
                }
                if (!clienteCursor.isNull(telefonoIndex)) {
                    details.put("telefono", clienteCursor.getString(telefonoIndex));
                }
                if (!clienteCursor.isNull(direccionIndex)) {
                    details.put("direccion", clienteCursor.getString(direccionIndex));
                }
            }
        } finally {
            clienteCursor.close();
        }
        db.close();
        return details;
    }

    public ContentValues getTiendaDetails(int tiendaId) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues details = new ContentValues();
        Cursor cursor = db.query(TABLE_TIENDA,
                new String[]{COLUMN_USUARIO_TIENDA, COLUMN_NUM_TELEFONO_TIENDA, COLUMN_CONTRASENIA_TIENDA, COLUMN_NOMBRE_TIENDA, COLUMN_TIPO_TIENDA},
                COLUMN_ID + " = ?", new String[]{String.valueOf(tiendaId)}, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                int usuarioIndex = cursor.getColumnIndex(COLUMN_USUARIO_TIENDA);
                int telefonoIndex = cursor.getColumnIndex(COLUMN_NUM_TELEFONO_TIENDA);
                int contraseniaIndex = cursor.getColumnIndex(COLUMN_CONTRASENIA_TIENDA);
                int nombreIndex = cursor.getColumnIndex(COLUMN_NOMBRE_TIENDA);
                int tipoIndex = cursor.getColumnIndex(COLUMN_TIPO_TIENDA);
                if (!cursor.isNull(usuarioIndex)) {
                    details.put("usuario", cursor.getString(usuarioIndex));
                }
                if (!cursor.isNull(telefonoIndex)) {
                    details.put("telefono", cursor.getString(telefonoIndex));
                }
                if (!cursor.isNull(contraseniaIndex)) {
                    details.put("contrasena", cursor.getString(contraseniaIndex));
                }
                if (!cursor.isNull(nombreIndex)) {
                    details.put("nombre_tienda", cursor.getString(nombreIndex));
                }
                if (!cursor.isNull(tipoIndex)) {
                    details.put("tipo_tienda", cursor.getString(tipoIndex));
                }
            }
        } finally {
            cursor.close();
        }
        db.close();
        return details;
    }

    public ContentValues getRepartidorDetails(int repartidorId) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues details = new ContentValues();
        Cursor cursor = db.query(TABLE_REPARTIDOR,
                new String[]{COLUMN_USUARIO_REPARTIDOR, COLUMN_NUM_TELEFONO_REPARTIDOR, COLUMN_CONTRASENIA_REPARTIDOR},
                COLUMN_ID + " = ?", new String[]{String.valueOf(repartidorId)}, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                int usuarioIndex = cursor.getColumnIndex(COLUMN_USUARIO_REPARTIDOR);
                int telefonoIndex = cursor.getColumnIndex(COLUMN_NUM_TELEFONO_REPARTIDOR);
                int contraseniaIndex = cursor.getColumnIndex(COLUMN_CONTRASENIA_REPARTIDOR);
                if (!cursor.isNull(usuarioIndex)) {
                    details.put("nombre_usuario", cursor.getString(usuarioIndex));
                }
                if (!cursor.isNull(telefonoIndex)) {
                    details.put("telefono", cursor.getString(telefonoIndex));
                }
                if (!cursor.isNull(contraseniaIndex)) {
                    details.put("contrasena", cursor.getString(contraseniaIndex));
                }
            }
        } finally {
            cursor.close();
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

    public List<PedidoConTienda> getPreparedPedidosForRepartidor() {
        List<PedidoConTienda> pedidos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT p." + COLUMN_ID + " AS id_pedido, p." + COLUMN_NOMBRE_CLIENTE + ", p." + COLUMN_DIRECCION_PEDIDO + ", " +
                "p." + COLUMN_IMPORTE + ", p." + COLUMN_ESTADO + ", p." + COLUMN_ID_CLIENTE + ", " +
                "p." + COLUMN_ID_TIENDA_PEDIDO + ", p." + COLUMN_ID_REPARTIDOR + ", t." + COLUMN_NOMBRE_TIENDA +
                " FROM " + TABLE_PEDIDO + " p" +
                " INNER JOIN " + TABLE_TIENDA + " t ON p." + COLUMN_ID_TIENDA_PEDIDO + " = t." + COLUMN_ID +
                " WHERE p." + COLUMN_ESTADO + " IN (?, ?) AND t." + COLUMN_TIPO_TIENDA + " IN (?, ?)";
        String[] selectionArgs = {Estado.PREP.toString(), Estado.ENTREG.toString(), TipoTienda.GENERAL.toString(), TipoTienda.FARMACIA.toString()};
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
                                    int prodImagenIndex = productoDetailCursor.getColumnIndex(COLUMN_IMAGEN);
                                    int prodId = productoDetailCursor.getInt(prodIdIndex);
                                    String prodNombre = productoDetailCursor.getString(prodNombreIndex);
                                    double prodPrecio = productoDetailCursor.getDouble(prodPrecioIndex);
                                    int prodTiendaId = productoDetailCursor.getInt(prodIdTiendaIndex);
                                    byte[] prodImagen = prodImagenIndex != -1 && !productoDetailCursor.isNull(prodImagenIndex) ? productoDetailCursor.getBlob(prodImagenIndex) : null;
                                    Producto producto = new Producto(prodId, prodTiendaId, prodNombre, prodPrecio, prodImagen);
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
            // GENERAL Store
            ContentValues tienda1 = new ContentValues();
            tienda1.put(COLUMN_USUARIO_TIENDA, "lacolonia");
            tienda1.put(COLUMN_NUM_TELEFONO_TIENDA, "504223344");
            tienda1.put(COLUMN_CONTRASENIA_TIENDA, "storepass1");
            tienda1.put(COLUMN_NOMBRE_TIENDA, "Supermercado La Colonia");
            tienda1.put(COLUMN_TIPO_TIENDA, TipoTienda.GENERAL.toString());
            long idTienda1 = db.insert(TABLE_TIENDA, null, tienda1);

            // FARMACIA Store
            ContentValues tienda2 = new ContentValues();
            tienda2.put(COLUMN_USUARIO_TIENDA, "farmaciakielsa");
            tienda2.put(COLUMN_NUM_TELEFONO_TIENDA, "504227788");
            tienda2.put(COLUMN_CONTRASENIA_TIENDA, "pharmpass1");
            tienda2.put(COLUMN_NOMBRE_TIENDA, "Farmacia Kielsa");
            tienda2.put(COLUMN_TIPO_TIENDA, TipoTienda.FARMACIA.toString());
            long idTienda2 = db.insert(TABLE_TIENDA, null, tienda2);

            // OTROS Store (No Products)
            ContentValues tienda3 = new ContentValues();
            tienda3.put(COLUMN_USUARIO_TIENDA, "libreriaguaymuras");
            tienda3.put(COLUMN_NUM_TELEFONO_TIENDA, "504221122");
            tienda3.put(COLUMN_CONTRASENIA_TIENDA, "bookpass1");
            tienda3.put(COLUMN_NOMBRE_TIENDA, "PeluquerÃ­a Guaymuras");
            tienda3.put(COLUMN_TIPO_TIENDA, TipoTienda.OTROS.toString());
            long idTienda3 = db.insert(TABLE_TIENDA, null, tienda3);

            // Products for GENERAL Store (Supermercado La Colonia)
            ContentValues prod1 = new ContentValues();
            prod1.put(COLUMN_NOMBRE_PRODUCTO, "Leche Entera 1L");
            prod1.put(COLUMN_PRECIO, 1.50);
            prod1.put(COLUMN_ID_TIENDA, idTienda1);
            prod1.put(COLUMN_IMAGEN, (byte[]) null);
            long idProd1 = db.insert(TABLE_PRODUCTO, null, prod1);

            ContentValues prod2 = new ContentValues();
            prod2.put(COLUMN_NOMBRE_PRODUCTO, "Pan Integral 500g");
            prod2.put(COLUMN_PRECIO, 2.00);
            prod2.put(COLUMN_ID_TIENDA, idTienda1);
            prod2.put(COLUMN_IMAGEN, (byte[]) null);
            long idProd2 = db.insert(TABLE_PRODUCTO, null, prod2);

            ContentValues prod3 = new ContentValues();
            prod3.put(COLUMN_NOMBRE_PRODUCTO, "Arroz Blanco 1kg");
            prod3.put(COLUMN_PRECIO, 1.20);
            prod3.put(COLUMN_ID_TIENDA, idTienda1);
            prod3.put(COLUMN_IMAGEN, (byte[]) null);
            long idProd3 = db.insert(TABLE_PRODUCTO, null, prod3);

            ContentValues prod4 = new ContentValues();
            prod4.put(COLUMN_NOMBRE_PRODUCTO, "Aceite de Oliva 500ml");
            prod4.put(COLUMN_PRECIO, 5.50);
            prod4.put(COLUMN_ID_TIENDA, idTienda1);
            prod4.put(COLUMN_IMAGEN, (byte[]) null);
            long idProd4 = db.insert(TABLE_PRODUCTO, null, prod4);

            ContentValues prod5 = new ContentValues();
            prod5.put(COLUMN_NOMBRE_PRODUCTO, "Pasta Espagueti 400g");
            prod5.put(COLUMN_PRECIO, 1.80);
            prod5.put(COLUMN_ID_TIENDA, idTienda1);
            prod5.put(COLUMN_IMAGEN, (byte[]) null);
            long idProd5 = db.insert(TABLE_PRODUCTO, null, prod5);

            // Products for FARMACIA Store (Farmacia Kielsa)
            ContentValues prod6 = new ContentValues();
            prod6.put(COLUMN_NOMBRE_PRODUCTO, "Paracetamol 500mg 20 tabletas");
            prod6.put(COLUMN_PRECIO, 3.00);
            prod6.put(COLUMN_ID_TIENDA, idTienda2);
            prod6.put(COLUMN_IMAGEN, (byte[]) null);
            long idProd6 = db.insert(TABLE_PRODUCTO, null, prod6);

            ContentValues prod7 = new ContentValues();
            prod7.put(COLUMN_NOMBRE_PRODUCTO, "Ibuprofeno 400mg 10 tabletas");
            prod7.put(COLUMN_PRECIO, 4.50);
            prod7.put(COLUMN_ID_TIENDA, idTienda2);
            prod7.put(COLUMN_IMAGEN, (byte[]) null);
            long idProd7 = db.insert(TABLE_PRODUCTO, null, prod7);

            ContentValues prod8 = new ContentValues();
            prod8.put(COLUMN_NOMBRE_PRODUCTO, "Jarabe para la Tos 120ml");
            prod8.put(COLUMN_PRECIO, 6.00);
            prod8.put(COLUMN_ID_TIENDA, idTienda2);
            prod8.put(COLUMN_IMAGEN, (byte[]) null);
            long idProd8 = db.insert(TABLE_PRODUCTO, null, prod8);

            ContentValues prod9 = new ContentValues();
            prod9.put(COLUMN_NOMBRE_PRODUCTO, "Alcohol en Gel 250ml");
            prod9.put(COLUMN_PRECIO, 2.50);
            prod9.put(COLUMN_ID_TIENDA, idTienda2);
            prod9.put(COLUMN_IMAGEN, (byte[]) null);
            long idProd9 = db.insert(TABLE_PRODUCTO, null, prod9);

            ContentValues prod10 = new ContentValues();
            prod10.put(COLUMN_NOMBRE_PRODUCTO, "Mascarillas KN95 5 unidades");
            prod10.put(COLUMN_PRECIO, 5.00);
            prod10.put(COLUMN_ID_TIENDA, idTienda2);
            prod10.put(COLUMN_IMAGEN, (byte[]) null);
            long idProd10 = db.insert(TABLE_PRODUCTO, null, prod10);

            // Clientes
            ContentValues cliente1 = new ContentValues();
            cliente1.put(COLUMN_USUARIO_CLIENTE, "ana_lopez");
            cliente1.put(COLUMN_NUM_TELEFONO_CLIENTE, "504330011");
            cliente1.put(COLUMN_CONTRASENIA_CLIENTE, "clientpass1");
            cliente1.put(COLUMN_DIRECCION_CLIENTE, "Col. Kennedy, Tegucigalpa");
            long idCliente1 = db.insert(TABLE_CLIENTE, null, cliente1);

            ContentValues cliente2 = new ContentValues();
            cliente2.put(COLUMN_USUARIO_CLIENTE, "carlos_martinez");
            cliente2.put(COLUMN_NUM_TELEFONO_CLIENTE, "5043311298");
            cliente2.put(COLUMN_CONTRASENIA_CLIENTE, "clientpass2");
            cliente2.put(COLUMN_DIRECCION_CLIENTE, "Barrio El Centro, San Pedro Sula");
            long idCliente2 = db.insert(TABLE_CLIENTE, null, cliente2);

            ContentValues cliente3 = new ContentValues();
            cliente3.put(COLUMN_USUARIO_CLIENTE, "sofia_gomez");
            cliente3.put(COLUMN_NUM_TELEFONO_CLIENTE, "504332233");
            cliente3.put(COLUMN_CONTRASENIA_CLIENTE, "clientpass3");
            cliente3.put(COLUMN_DIRECCION_CLIENTE, "Residencial Los Alpes, Comayagua");
            long idCliente3 = db.insert(TABLE_CLIENTE, null, cliente3);

            // Repartidores
            ContentValues repartidor1 = new ContentValues();
            repartidor1.put(COLUMN_USUARIO_REPARTIDOR, "mario_driver");
            repartidor1.put(COLUMN_NUM_TELEFONO_REPARTIDOR, "504333344");
            repartidor1.put(COLUMN_CONTRASENIA_REPARTIDOR, "driverpass1");
            long idRepartidor1 = db.insert(TABLE_REPARTIDOR, null, repartidor1);

            ContentValues repartidor2 = new ContentValues();
            repartidor2.put(COLUMN_USUARIO_REPARTIDOR, "laura_rider");
            repartidor2.put(COLUMN_NUM_TELEFONO_REPARTIDOR, "504334455");
            repartidor2.put(COLUMN_CONTRASENIA_REPARTIDOR, "driverpass2");
            long idRepartidor2 = db.insert(TABLE_REPARTIDOR, null, repartidor2);

            ContentValues repartidor3 = new ContentValues();
            repartidor3.put(COLUMN_USUARIO_REPARTIDOR, "jose_moto");
            repartidor3.put(COLUMN_NUM_TELEFONO_REPARTIDOR, "504335566");
            repartidor3.put(COLUMN_CONTRASENIA_REPARTIDOR, "driverpass3");
            long idRepartidor3 = db.insert(TABLE_REPARTIDOR, null, repartidor3);

            // Pedidos
            // Pedido 1: Ana LÃ³pez from Supermercado La Colonia
            ContentValues pedido1 = new ContentValues();
            pedido1.put(COLUMN_ID_CLIENTE, idCliente1);
            pedido1.put(COLUMN_ID_TIENDA, idTienda1);
            pedido1.put(COLUMN_NOMBRE_CLIENTE, "Ana LÃ³pez");
            pedido1.put(COLUMN_DIRECCION_PEDIDO, "Col. Kennedy, Tegucigalpa");
            pedido1.put(COLUMN_IMPORTE, 5.50);
            pedido1.put(COLUMN_ESTADO, "PREP");
            long idPedido1 = db.insert(TABLE_PEDIDO, null, pedido1);

            ContentValues pedidoProd1 = new ContentValues();
            pedidoProd1.put(COLUMN_ID_PEDIDO, idPedido1);
            pedidoProd1.put(COLUMN_ID_PRODUCTO, idProd1);
            pedidoProd1.put(COLUMN_CANTIDAD, 2);
            db.insert(TABLE_PEDIDO_PRODUCTO, null, pedidoProd1);

            ContentValues pedidoProd2 = new ContentValues();
            pedidoProd2.put(COLUMN_ID_PEDIDO, idPedido1);
            pedidoProd2.put(COLUMN_ID_PRODUCTO, idProd2);
            pedidoProd2.put(COLUMN_CANTIDAD, 1);
            db.insert(TABLE_PEDIDO_PRODUCTO, null, pedidoProd2);

            // Pedido 2: Carlos MartÃ­nez from Farmacia Kielsa
            ContentValues pedido2 = new ContentValues();
            pedido2.put(COLUMN_ID_CLIENTE, idCliente2);
            pedido2.put(COLUMN_ID_TIENDA, idTienda2);
            pedido2.put(COLUMN_NOMBRE_CLIENTE, "Carlos MartÃ­nez");
            pedido2.put(COLUMN_DIRECCION_PEDIDO, "Barrio El Centro, San Pedro Sula");
            pedido2.put(COLUMN_IMPORTE, 11.00);
            pedido2.put(COLUMN_ESTADO, "ENTREG");
            long idPedido2 = db.insert(TABLE_PEDIDO, null, pedido2);

            ContentValues pedidoProd3 = new ContentValues();
            pedidoProd3.put(COLUMN_ID_PEDIDO, idPedido2);
            pedidoProd3.put(COLUMN_ID_PRODUCTO, idProd6);
            pedidoProd3.put(COLUMN_CANTIDAD, 1);
            db.insert(TABLE_PEDIDO_PRODUCTO, null, pedidoProd3);

            ContentValues pedidoProd4 = new ContentValues();
            pedidoProd4.put(COLUMN_ID_PEDIDO, idPedido2);
            pedidoProd4.put(COLUMN_ID_PRODUCTO, idProd7);
            pedidoProd4.put(COLUMN_CANTIDAD, 2);
            db.insert(TABLE_PEDIDO_PRODUCTO, null, pedidoProd4);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}