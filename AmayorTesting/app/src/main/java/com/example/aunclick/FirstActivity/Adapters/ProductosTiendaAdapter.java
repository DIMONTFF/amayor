package com.example.amayor.FirstActivity.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amayor.Objetos.Producto;
import com.example.amayor.R;
import java.util.List;
import android.util.Log;

public class ProductosTiendaAdapter extends RecyclerView.Adapter<ProductosTiendaAdapter.ProductoViewHolder> {

    private List<Producto> productos;
    private OnProductoEliminarClickListener eliminarClickListener;

    public interface OnProductoEliminarClickListener {
        void onEliminarClick(Producto producto);
    }

    public ProductosTiendaAdapter(List<Producto> productos, OnProductoEliminarClickListener listener) {
        this.productos = productos;
        this.eliminarClickListener = listener;
        Log.d("ProductosAdapter", "Productos recibidos: " + productos.size());
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto_tienda, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);
        Log.d("ProductosAdapter", "Binding producto: Nombre=" + producto.getNombre() + ", Precio=" + producto.getPrecio());
        holder.tvNombreProducto.setText(producto.getNombre() != null ? producto.getNombre() : "Sin nombre");
        holder.tvPrecio.setText(producto.getPrecio() >= 0 ? String.format("$%.2f", producto.getPrecio()) : "Sin precio");

        // Set click listener for Eliminar button
        holder.btnEliminar.setOnClickListener(v -> {
            if (eliminarClickListener != null) {
                eliminarClickListener.onEliminarClick(producto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreProducto;
        TextView tvPrecio;
        Button btnEliminar;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            btnEliminar = itemView.findViewById(R.id.btnEliminarProducto);
            Log.d("ProductosAdapter", "ViewHolder creado, tvNombreProducto=" + (tvNombreProducto != null) + ", tvPrecio=" + (tvPrecio != null));
        }
    }
}