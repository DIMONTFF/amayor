package com.example.amayor.FirstActivity.Adapters;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amayor.FirstActivity.Fragments.Fragment_ProductoListaTienda;
import com.example.amayor.Objetos.Tienda;
import com.example.amayor.R;
import java.util.List;

public class TiendaAdapter extends RecyclerView.Adapter<TiendaAdapter.TiendaViewHolder> {

    private List<Tienda> tiendas;
    private FragmentActivity activity;

    public TiendaAdapter(List<Tienda> tiendas, FragmentActivity activity) {
        this.tiendas = tiendas;
        this.activity = activity;
    }

    public static class TiendaViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombreTienda;
        public TextView tvTipoTienda;
        public TextView tvNumTelef;

        public TiendaViewHolder(View itemView) {
            super(itemView);
            tvNombreTienda = itemView.findViewById(R.id.tvNombreTienda);
            tvTipoTienda = itemView.findViewById(R.id.tvTipoTienda);
            tvNumTelef = itemView.findViewById(R.id.tvNumTelef);
        }
    }

    @NonNull
    @Override
    public TiendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tienda, parent, false);
        return new TiendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TiendaViewHolder holder, int position) {
        Tienda tienda = tiendas.get(position);
        holder.tvNombreTienda.setText(tienda.getNombreTienda());
        holder.tvTipoTienda.setText(tienda.getTipoTienda().toString());
        holder.tvNumTelef.setText(tienda.getNumTelef());

        // Set text color based on store name
        String nombreTienda = tienda.getNombreTienda().toLowerCase();
        if (nombreTienda.contains("pescadería")) {
            holder.tvNombreTienda.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.blue));
        } else if (nombreTienda.contains("carnicería")) {
            holder.tvNombreTienda.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
        } else if (nombreTienda.contains("frutería")) {
            holder.tvNombreTienda.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
        } else {
            holder.tvNombreTienda.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.orange));
        }

        // Click listener to show store products
        holder.itemView.setOnClickListener(v -> {
            Fragment_ProductoListaTienda fragment = new Fragment_ProductoListaTienda();
            Bundle args = new Bundle();
            args.putInt("idTienda", tienda.getIdPersona());
            fragment.setArguments(args);

            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return tiendas.size();
    }
}
