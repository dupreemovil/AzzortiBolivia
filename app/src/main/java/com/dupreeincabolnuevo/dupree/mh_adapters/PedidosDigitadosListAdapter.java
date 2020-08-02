package com.dupreeincabolnuevo.dupree.mh_adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;

import com.dupreeincabolnuevo.dupree.R;
import com.dupreeincabolnuevo.dupree.databinding.ItemPedidosDigitadosBinding;
import com.dupreeincabolnuevo.dupree.mh_holders.PedidosDigitadosHolder;
import com.dupreeinca.lib_api_rest.model.dto.response.PedidoDigitado;

import java.util.List;

public class PedidosDigitadosListAdapter extends RecyclerView.Adapter<PedidosDigitadosHolder> {

    private List<PedidoDigitado> list, listFilter;
    private PedidosDigitadosHolder.Events events;

    public PedidosDigitadosListAdapter(List<PedidoDigitado> list, List<PedidoDigitado> listFilter, PedidosDigitadosHolder.Events events){
        this.list = list;
        this.listFilter = listFilter;
        this.events=events;

        //para filtrar
        mFilter = new CustomFilter(PedidosDigitadosListAdapter.this);
    }

    @NonNull
    @Override
    public PedidosDigitadosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        ItemPedidosDigitadosBinding binding = DataBindingUtil.inflate(inflate, R.layout.item_pedidos_digitados, parent, false);
        return new PedidosDigitadosHolder(binding, events);
    }



    @Override
    public void onBindViewHolder(@NonNull PedidosDigitadosHolder holder, int position) {
        if(listFilter.size()>position){
            holder.bindData(listFilter.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return (null != listFilter ? listFilter.size() : 0);
    }

    /**
     * FILTER
     */
    private CustomFilter mFilter;

    public CustomFilter getmFilter() {
        return mFilter;
    }

    public class CustomFilter extends Filter {
        private PedidosDigitadosListAdapter mAdapter;
        private CustomFilter(PedidosDigitadosListAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            listFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                listFilter.addAll(list);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final PedidoDigitado mWords : list) {
                    if (String.valueOf(mWords.getNume_iden()).toLowerCase().contains(filterPattern)) {
                        listFilter.add(mWords);
                    }
                }
            }
            System.out.println("Count Number " + listFilter.size());
            results.values = listFilter;
            results.count = listFilter.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

}
