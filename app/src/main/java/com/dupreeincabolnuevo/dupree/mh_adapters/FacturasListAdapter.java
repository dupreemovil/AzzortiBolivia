package com.dupreeincabolnuevo.dupree.mh_adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dupreeinca.lib_api_rest.model.dto.response.ItemFacturaDTO;
import com.dupreeincabolnuevo.dupree.R;
import com.dupreeincabolnuevo.dupree.databinding.ItemFacturaBinding;
import com.dupreeincabolnuevo.dupree.mh_holders.FacturasHolder;

import java.util.List;

/**
 * Created by marwuinh@gmail.com on 2/27/19.
 */

public class FacturasListAdapter extends RecyclerView.Adapter<FacturasHolder>{
    private List<ItemFacturaDTO> list;
    private FacturasHolder.Events events;

    public FacturasListAdapter(List<ItemFacturaDTO> list, FacturasHolder.Events events) {
        this.list = list;
        this.events = events;
    }

    @NonNull
    @Override
    public FacturasHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemFacturaBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_factura, viewGroup, false);
        return new FacturasHolder(binding, events);
    }

    @Override
    public void onBindViewHolder(@NonNull FacturasHolder holder, int i) {
        if(list.size() > i) {
            holder.bindData(list.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
}
