package com.example.amayor.FirstActivity.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.Objetos.Estado;
import com.example.amayor.Objetos.Pedido;
import com.example.amayor.Objetos.Producto;
import com.example.amayor.R;
import java.util.List;
import java.util.Map;

public class PedidosRepartidorAdapter extends RecyclerView.Adapter<PedidosRepartidorAdapter.PedidoViewHolder> {

    private List<DataBaseHelper.PedidoConTienda> pedidos;
    private FragmentActivity activity;
    private DataBaseHelper dbHelper;

    public PedidosRepartidorAdapter(List<DataBaseHelper.PedidoConTienda> pedidos, FragmentActivity activity) {
        this.pedidos = pedidos;
        this.activity = activity;
        this.dbHelper = new DataBaseHelper(activity.getApplicationContext());
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido_repartidor, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        DataBaseHelper.PedidoConTienda pedidoConTienda = pedidos.get(position);
        Pedido pedido = pedidoConTienda.getPedido();
        holder.tvIdPedidoValue.setText("#" + pedido.getIdPedido());
        holder.tvNombreClienteValue.setText(pedido.getNombreCliente() != null ? pedido.getNombreCliente() : "Sin nombre");
        holder.tvDireccionValue.setText(pedido.getDireccion() != null ? pedido.getDireccion() : "Sin dirección");
        holder.tvNombreTiendaValue.setText(pedidoConTienda.getNombreTienda() != null ? pedidoConTienda.getNombreTienda() : "Sin tienda");

        // Format products list
        StringBuilder productosStr = new StringBuilder();
        for (Map.Entry<Producto, Integer> entry : pedido.getDescripcion().entrySet()) {
            if (productosStr.length() > 0) {
                productosStr.append(", ");
            }
            productosStr.append(entry.getKey().getNombre()).append(" x").append(entry.getValue());
        }
        holder.tvProductosValue.setText(productosStr.length() > 0 ? productosStr.toString() : "Sin productos");

        // Format importe
        holder.tvImporteValue.setText(String.format("$%.2f", pedido.getImporte()));

        // Format estado
        String estadoStr;
        switch (pedido.getEstado()) {
            case NOPREP:
                estadoStr = "No Preparado";
                holder.btnCambiarEstado.setVisibility(View.GONE);
                break;
            case PREP:
                estadoStr = "No Entregado";
                holder.btnCambiarEstado.setVisibility(View.VISIBLE);
                holder.btnCambiarEstado.setText("Marcar como Entregado");
                break;
            case NOENTREG:
                estadoStr = "No Entregado";
                holder.btnCambiarEstado.setVisibility(View.VISIBLE);
                holder.btnCambiarEstado.setText("Marcar como Entregado");
                break;
            case ENTREG:
                estadoStr = "Entregado";
                holder.btnCambiarEstado.setVisibility(View.VISIBLE);
                holder.btnCambiarEstado.setText("Marcar como No Entregado");
                break;
            default:
                estadoStr = "Desconocido";
                holder.btnCambiarEstado.setVisibility(View.GONE);
        }
        holder.tvEstadoValue.setText(estadoStr);

        // Set button click listener
        holder.btnCambiarEstado.setOnClickListener(v -> {
            Estado nuevoEstado;
            if (pedido.getEstado() == Estado.PREP || pedido.getEstado() == Estado.NOENTREG) {
                nuevoEstado = Estado.ENTREG;
            } else {
                nuevoEstado = Estado.NOENTREG;
            }
            boolean success = dbHelper.updatePedidoEstado(pedido.getIdPedido(), nuevoEstado);
            if (success) {
                pedido.setEstado(nuevoEstado);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdPedidoValue, tvNombreClienteValue, tvDireccionValue, tvNombreTiendaValue, tvProductosValue, tvImporteValue, tvEstadoValue;
        Button btnCambiarEstado;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdPedidoValue = itemView.findViewById(R.id.tvIdPedidoValue);
            tvNombreClienteValue = itemView.findViewById(R.id.tvNombreClienteValue);
            tvDireccionValue = itemView.findViewById(R.id.tvDireccionValue);
            tvNombreTiendaValue = itemView.findViewById(R.id.tvNombreTiendaValue);
            tvProductosValue = itemView.findViewById(R.id.tvProductosValue);
            tvImporteValue = itemView.findViewById(R.id.tvImporteValue);
            tvEstadoValue = itemView.findViewById(R.id.tvEstadoValue);
            btnCambiarEstado = itemView.findViewById(R.id.btnCambiarEstado);
        }
    }
}