package com.dupreeinca.lib_api_rest.model.dto.response;

import java.util.List;

public class PedidoDigitadoDTO {

    private String status;
    private boolean valid;
    private ListaPedidosDigitados result;
    private int code;

    private List<RaiseDTO> raise;

    public String getStatus() {
        return status;
    }

    public boolean isValid() {
        return valid;
    }

    public ListaPedidosDigitados getResult() {
        return result;
    }

    public int getCode() {
        return code;
    }

    public List<RaiseDTO> getRaise() {
        return raise;
    }

}
