package com.example.amayor.FirstActivity.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombreProducto;
        public TextView tvPrecioProducto;
        public ImageView ivProductoImagen;
        public Button btnEliminarProducto;

        public ProductoViewHolder(View itemView) {
            super(itemView);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecioProducto = itemView.findViewById(R.id.tvPrecioProducto);
            ivProductoImagen = itemView.findViewById(R.id.ivProductoImagen);
            btnEliminarProducto = itemView.findViewById(R.id.btnEliminarProducto);
        }
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
        holder.tvNombreProducto.setText(producto.getNombre());
        holder.tvPrecioProducto.setText(String.format("$%.2f", producto.getPrecio()));

        // Set product image
        if (producto.getImagen() != null) {
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(producto.getImagen(), 0, producto.getImagen().length);
                holder.ivProductoImagen.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.e("ProductosTiendaAdapter", "Error decoding image for product: " + producto.getNombre(), e);
                holder.ivProductoImagen.setImageResource(R.drawable.placeholder);
            }
        } else {
            holder.ivProductoImagen.setImageResource(R.drawable.placeholder);
        }

        // Set click listener for Delete button
        holder.btnEliminarProducto.setOnClickListener(v -> {
            if (eliminarClickListener != null) {
                eliminarClickListener.onEliminarClick(producto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }
}