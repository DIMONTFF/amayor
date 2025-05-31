package com.example.amayor.FirstActivity.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amayor.FirstActivity.Adapters.ProductoAdapter;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.Objetos.Producto;
import com.example.amayor.R;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_ProductoListaTienda extends Fragment implements ProductoAdapter.OnAddToCartListener {

    private Map<Producto, Integer> carrito;
    private static final String TAG = "ProductoListFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producto_lista_tienda, container, false);

        carrito = new HashMap<>();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get idTienda from arguments
        int idTienda = getArguments() != null ? getArguments().getInt("idTienda", -1) : -1;

        // Fetch Productos from database
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());
        List<Producto> productos = dbHelper.getProductosByTienda(idTienda);

        // Set adapter
        ProductoAdapter adapter = new ProductoAdapter(productos, this);
        recyclerView.setAdapter(adapter);

        // Set click listener for cart button
        ImageView botonVerCarrito = view.findViewById(R.id.botonVerCarrito);
        botonVerCarrito.setOnClickListener(v -> {
            Fragment_Carrito dialog = Fragment_Carrito.newInstance(new HashMap<>(carrito));
            dialog.show(getParentFragmentManager(), "CarritoDialog");
        });

        return view;
    }

    @Override
    public void onAddToCart(Producto producto) {
        carrito.merge(producto, 1, Integer::sum);
        Toast.makeText(getContext(),producto.getNombre() + " añadido al carrito", Toast.LENGTH_SHORT).show();
        StringBuilder cartLog = new StringBuilder("Carrito:\n");
        for (Map.Entry<Producto, Integer> entry : carrito.entrySet()) {
            cartLog.append(entry.getKey().getNombre())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }
        Log.d(TAG, cartLog.toString());
    }
}