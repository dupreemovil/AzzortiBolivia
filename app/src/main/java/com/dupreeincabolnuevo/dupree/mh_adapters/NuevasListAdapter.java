package com.dupreeincabolnuevo.dupree.mh_adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dupreeinca.lib_api_rest.model.dto.response.realm.Posibles_Nuevas;
import com.dupreeincabolnuevo.dupree.R;
import com.dupreeincabolnuevo.dupree.databinding.ItemListNuevasBinding;
import com.dupreeincabolnuevo.dupree.databinding.ItemListPreinscripBinding;
import com.dupreeincabolnuevo.dupree.mh_holders.ListNuevasHolder;

import java.util.List;

public class NuevasListAdapter extends RecyclerView.Adapter<ListNuevasHolder> {

    private List<Posibles_Nuevas> list;
    private ListNuevasHolder.Events events;
    public NuevasListAdapter(List<Posibles_Nuevas> list, ListNuevasHolder.Events events){
        this.list = list;
        this.events = events;
    }

    @NonNull
    @Override
    public ListNuevasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        ItemListNuevasBinding binding= DataBindingUtil.inflate(inflate, R.layout.item_list_nuevas, parent, false);
        return new ListNuevasHolder(binding, events);
    }

    @Override
    public void onBindViewHolder(@NonNull ListNuevasHolder holder, int position) {
        if(list.size() > position) {
            holder.bindData(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }
}
