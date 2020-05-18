package com.dupreeinca.lib_api_rest.model.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PedidoDigitadoDTO {

    @SerializedName("datos")
    private List<PedidoDigitado> result;
//    private ListaPedidosDigitados result;
    @SerializedName("lider")
    private String status;
    private int code;

    private boolean valid;


    private List<RaiseDTO> raise;

    public String getStatus() {
        return status;
    }

    public boolean isValid() {
        return valid;
    }

    public List<PedidoDigitado> getResult() {
        return result;
    }

    public int getCode() {
        return code;
    }
    public List<RaiseDTO> getRaise() {
        return raise;
    }

}
