package com.dupreeinca.lib_api_rest.model.dto.response;

import com.dupreeinca.lib_api_rest.model.dto.request.Index;
import java.util.List;

public class ListaPedidosDigitados {

    private String nume_iden;
    private String nomb_terc;
    private String acti_usua;
    private List<PedidoDigitado> table;
    private Index paginator;
    private String asesora;

    public String getNume_iden() {
        return nume_iden;
    }

    public String getNomb_terc() {
        return nomb_terc;
    }

    public String getActi_usua() {
        return acti_usua;
    }

    public List<PedidoDigitado> getTable() {
        return table;
    }

    public Index getPaginator() {
        return paginator;
    }

    public String getAsesora() {
        return asesora;
    }

}


