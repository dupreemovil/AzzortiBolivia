package com.dupreeincabolnuevo.dupree.mh_adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dupreeinca.lib_api_rest.util.models.ModelList;
import com.dupreeincabolnuevo.dupree.R;
import com.dupreeincabolnuevo.dupree.databinding.ItemReportesBinding;
import com.dupreeincabolnuevo.dupree.mh_holders.ReportesHolder;

import java.util.List;

/**
 * Created by marwuinh@gmail.com on 2/27/19.
 */

public class ReportesListAdapter extends RecyclerView.Adapter<ReportesHolder>{
    private List<ModelList> list;
    private ReportesHolder.Events events;

    public ReportesListAdapter(List<ModelList> list, ReportesHolder.Events events) {
        this.list = list;
        this.events = events;
    }

    @NonNull
    @Override
    public ReportesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemReportesBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_reportes, viewGroup, false);
        return new ReportesHolder(binding, events);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportesHolder holder, int i) {
        if(list.size() > i) {
            holder.bindData(list.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
}
