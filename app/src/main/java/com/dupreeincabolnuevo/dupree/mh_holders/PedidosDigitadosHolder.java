package com.dupreeincabolnuevo.dupree.mh_holders;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dupreeincabolnuevo.dupree.BaseAPP;
import com.dupreeincabolnuevo.dupree.R;
import com.dupreeincabolnuevo.dupree.databinding.ItemPedidosDigitadosBinding;
import com.dupreeinca.lib_api_rest.model.dto.response.PedidoDigitado;

public class PedidosDigitadosHolder extends RecyclerView.ViewHolder {

    private ItemPedidosDigitadosBinding binding;
    private Events events;

    public PedidosDigitadosHolder(@NonNull ItemPedidosDigitadosBinding binding, Events events) {
        super(binding.getRoot());
        this.binding = binding;
        this.events = events;
    }

    public void bindData(final PedidoDigitado item) {
        int position = getAdapterPosition();
        Resources resources = BaseAPP.getContext().getResources();

        binding.tvName.setText("".concat(String.valueOf(item.getNomb_terc())));
        binding.tvCedula.setText("".concat(String.valueOf(item.getNume_iden())));
        binding.tvObserv.setText("".concat(item.getActi_usua()));
        binding.cardViewBackGround.setCardBackgroundColor(position%2!=0 ? resources.getColor(R.color.transp_Accent) : resources.getColor(R.color.transp_azulDupree));


        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(events != null)
                    events.onClickRoot(item, getAdapterPosition());
            }
        });
    }

    public interface Events{
        void onClickRoot(PedidoDigitado dataRow, int row);
    }

}
