package com.example.amayor.FirstActivity.Fragments;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.Objetos.Producto;
import com.example.amayor.R;

public class Fragment_CrearProductos extends DialogFragment {

    private static final String ARG_TIENDA_ID = "tienda_id";
    private EditText editTextNombre, editTextPrecio;
    private Button buttonAnadir, buttonCancelar;
    private DataBaseHelper dbHelper;
    private OnProductoAddedListener productoAddedListener;

    public interface OnProductoAddedListener {
        void onProductoAdded(Producto producto);
    }

    public static Fragment_CrearProductos newInstance(int tiendaId) {
        Fragment_CrearProductos fragment = new Fragment_CrearProductos();
        Bundle args = new Bundle();
        args.putInt(ARG_TIENDA_ID, tiendaId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DataBaseHelper(requireContext());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_crear_productos, null);

        // Initialize views
        editTextNombre = view.findViewById(R.id.editTextNombreProducto);
        editTextPrecio = view.findViewById(R.id.editTextPrecioProducto);
        buttonAnadir = view.findViewById(R.id.buttonAnadirProducto);
        buttonCancelar = view.findViewById(R.id.buttonCancelar);

        // Set dialog title
        builder.setView(view)
                .setTitle("Añadir Nuevo Producto");

        // Handle Añadir button
        buttonAnadir.setOnClickListener(v -> {
            String nombre = editTextNombre.getText().toString().trim();
            String precioStr = editTextPrecio.getText().toString().trim();

            // Validate inputs
            if (nombre.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, ingrese el nombre del producto", Toast.LENGTH_SHORT).show();
                return;
            }
            if (precioStr.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, ingrese el precio del producto", Toast.LENGTH_SHORT).show();
                return;
            }

            double precio;
            try {
                precio = Double.parseDouble(precioStr);
                if (precio < 0) {
                    Toast.makeText(requireContext(), "El precio no puede ser negativo", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Por favor, ingrese un precio válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert product into database
            int tiendaId = getArguments().getInt(ARG_TIENDA_ID);
            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("precio", precio);
            values.put("id_tienda", tiendaId);
            long productoId = dbHelper.getWritableDatabase().insert("producto", null, values);

            if (productoId != -1) {
                // Create Producto object
                Producto nuevoProducto = new Producto((int) productoId, tiendaId, nombre, precio);
                // Notify listener
                if (productoAddedListener != null) {
                    productoAddedListener.onProductoAdded(nuevoProducto);
                }
                Toast.makeText(requireContext(), "Producto añadido exitosamente", Toast.LENGTH_SHORT).show();
                dismiss();
            } else {
                Toast.makeText(requireContext(), "Error al añadir el producto", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Cancelar button
        buttonCancelar.setOnClickListener(v -> dismiss());

        return builder.create();
    }

    public void setOnProductoAddedListener(OnProductoAddedListener listener) {
        this.productoAddedListener = listener;
    }
}