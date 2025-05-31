package com.example.amayor.FirstActivity.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amayor.Objetos.Pedido;
import com.example.amayor.Objetos.Estado;
import com.example.amayor.R;
import java.util.List;

public class PedidosTiendaAdapter extends RecyclerView.Adapter<PedidosTiendaAdapter.PedidoViewHolder> {

    private List<Pedido> pedidos;
    private OnEstadoCambiadoListener estadoCambiadoListener;

    public interface OnEstadoCambiadoListener {
        void onEstadoCambiado(Pedido pedido, Estado nuevoEstado);
    }

    public PedidosTiendaAdapter(List<Pedido> pedidos, OnEstadoCambiadoListener listener) {
        this.pedidos = pedidos;
        this.estadoCambiadoListener = listener;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido_tienda, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        holder.tvIdPedido.setText("Pedido #" + pedido.getIdPedido());
        holder.tvNombreCliente.setText(pedido.getNombreCliente() != null ? pedido.getNombreCliente() : "Sin nombre");
        holder.tvImporte.setText(String.format("$%.2f", pedido.getImporte()));
        holder.tvEstado.setText(pedido.getEstado() != null ? formatEstado(pedido.getEstado()) : "Sin estado");

        // Configurar el botón según el estado
        if (pedido.getEstado() == Estado.NOPREP) {
            holder.btnCambiarEstado.setText("Marcar como Preparado");
            holder.btnCambiarEstado.setOnClickListener(v -> {
                if (estadoCambiadoListener != null) {
                    estadoCambiadoListener.onEstadoCambiado(pedido, Estado.PREP);
                }
            });
        } else if (pedido.getEstado() == Estado.PREP) {
            holder.btnCambiarEstado.setText("Marcar como No Preparado");
            holder.btnCambiarEstado.setOnClickListener(v -> {
                if (estadoCambiadoListener != null) {
                    estadoCambiadoListener.onEstadoCambiado(pedido, Estado.NOPREP);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    // Formatear el estado para mostrarlo de manera amigable
    private String formatEstado(Estado estado) {
        switch (estado) {
            case NOPREP:
                return "No preparado";
            case PREP:
                return "Preparado";
            case NOENTREG:
                return "No entregado";
            case ENTREG:
                return "Entregado";
            default:
                return estado.toString();
        }
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdPedido, tvNombreCliente, tvImporte, tvEstado;
        Button btnCambiarEstado;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdPedido = itemView.findViewById(R.id.tvIdPedido);
            tvNombreCliente = itemView.findViewById(R.id.tvNombreCliente);
            tvImporte = itemView.findViewById(R.id.tvImporte);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            btnCambiarEstado = itemView.findViewById(R.id.btnCambiarEstado);
        }
    }
}
