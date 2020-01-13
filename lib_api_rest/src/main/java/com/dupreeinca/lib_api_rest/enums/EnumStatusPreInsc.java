package com.dupreeinca.lib_api_rest.enums;

/**
 * Created by marwuinh@gmail.com on 2/28/19.
 */

public enum EnumStatusPreInsc {
    AUTORIZADO("AUTORIZADO"),
    RECHAZADO("RECHAZADO"),
    PENDIENTE("PENDIENTE");

    private String key;

    EnumStatusPreInsc(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
