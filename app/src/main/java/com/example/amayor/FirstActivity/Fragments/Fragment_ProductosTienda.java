package com.example.amayor.FirstActivity.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.amayor.FirstActivity.Adapters.ProductosTiendaAdapter;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.Objetos.Producto;
import com.example.amayor.R;
import java.util.List;

public class Fragment_ProductosTienda extends Fragment implements Fragment_CrearProductos.OnProductoAddedListener {

    private static final String ARG_TIENDA_ID = "tienda_id";

    private int mTiendaId;
    private RecyclerView recyclerView;
    private ProductosTiendaAdapter adapter;
    private DataBaseHelper dbHelper;
    private List<Producto> productos;

    public Fragment_ProductosTienda() {
        // Required empty public constructor
    }

    public static Fragment_ProductosTienda newInstance(int tiendaId) {
        Fragment_ProductosTienda fragment = new Fragment_ProductosTienda();
        Bundle args = new Bundle();
        args.putInt(ARG_TIENDA_ID, tiendaId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTiendaId = getArguments().getInt(ARG_TIENDA_ID);
            Log.d("ProductosTienda", "Tienda ID recibido: " + mTiendaId);
        }
        dbHelper = new DataBaseHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos_tienda, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerProductosTienda);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize Back Button
        ImageView ivBack = view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
                Toast.makeText(requireContext(), "Volviendo a la pantalla anterior", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "No hay fragmentos anteriores", Toast.LENGTH_SHORT).show();
            }
        });

        // Cargar productos desde la base de datos
        productos = dbHelper.getProductosByTienda(mTiendaId);
        Log.d("ProductosTienda", "Número de productos cargados: " + productos.size());
        for (Producto producto : productos) {
            Log.d("ProductosTienda", "Producto: ID=" + producto.getIdProducto() + ", Nombre=" + producto.getNombre() + ", Precio=" + producto.getPrecio());
        }
        adapter = new ProductosTiendaAdapter(productos, producto -> {
            // Eliminar producto de la base de datos
            Log.d("ProductosTienda", "Eliminando producto: " + producto.getNombre());
            dbHelper.deleteProducto(producto.getIdProducto());
            // Actualizar la lista
            productos.remove(producto);
            adapter.notifyDataSetChanged();
        });
        recyclerView.setAdapter(adapter);

        // Configurar el botón para añadir producto
        ImageView botonAnadirProducto = view.findViewById(R.id.botonAnadirProducto);
        botonAnadirProducto.setOnClickListener(v -> {
            Fragment_CrearProductos dialog = Fragment_CrearProductos.newInstance(mTiendaId);
            dialog.setOnProductoAddedListener(this);
            dialog.show(getParentFragmentManager(), "AddProductoDialog");
        });

        return view;
    }

    @Override
    public void onProductoAdded(Producto producto) {
        productos.add(producto);
        adapter.notifyItemInserted(productos.size() - 1);
        recyclerView.scrollToPosition(productos.size() - 1);
        Log.d("ProductosTienda", "Producto añadido: " + producto.getNombre());
    }
}