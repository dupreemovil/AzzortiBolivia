package com.dupreeinca.lib_api_rest.model.dto.response;

import com.google.gson.annotations.SerializedName;

public class PedidoDigitado {
    @SerializedName("nume_iden")
    private String nume_iden;
    @SerializedName("nomb_terc")
    private String nomb_terc;
    @SerializedName("acti_usua")
    private String acti_usua;


    public PedidoDigitado(String nume_iden, String nomb_terc,  String acti_usua) {
        this.nume_iden = nume_iden;
        this.nomb_terc = nomb_terc;
        this.acti_usua = acti_usua;
    }

    public String getNume_iden() {
        return nume_iden;
    }

    public String getNomb_terc() {
        return nomb_terc;
    }

    public String getActi_usua() {
        return acti_usua;
    }
}