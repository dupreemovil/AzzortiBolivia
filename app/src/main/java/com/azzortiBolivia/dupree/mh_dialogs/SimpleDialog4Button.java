package com.azzortiBolivia.dupree.mh_dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
public class SimpleDialog4Button extends DialogFragment {
    private final String TAG = SimpleDialog4Button.class.getName();

    private String titulo="";
    private String message="";

    public SimpleDialog4Button() {
    }

    private String titleAprobar="";
    private String titleRechazar="";
    public void loadData(String titulo, String message, String titleAprobar, String titleRechazar) {
        this.titulo = titulo;
        this.message = message;
        this.titleAprobar = titleAprobar;
        this.titleRechazar = titleRechazar;
    }

    public void setListener(ListenerResult listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return create4ButtonDialog();
    }
    /**
     * Crea un diálogo de alerta sencillo
     * @return Nuevo diálogo
     */

    public AlertDialog create4ButtonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] options = {"Cancelar", "Editar", "Rechazar", "Aprobar"};//@@
        builder.setTitle(message);
        //builder.setMessage(message);

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null)
                    listener.result(options[which]);
                switch (which) {
                    case 0: // Cancel
                        Log.i("@@--", "Opcion 0 ");
                        break;
                    case 1: // Editar
                        Log.i("@@--", "Opcion 1 ");
                        break;
                    case 2: // aprobar
                        Log.i("@@--", "Opcion 2 ");
                        break;
                    case 3: // Rechazar
                        Log.i("@@--", "Opcion 3 ");
                        break;
                }
                dismiss();
            }
        });
        return builder.create();
    }

    private ListenerResult listener;
    public interface ListenerResult {
        void result(String status);
    }
}