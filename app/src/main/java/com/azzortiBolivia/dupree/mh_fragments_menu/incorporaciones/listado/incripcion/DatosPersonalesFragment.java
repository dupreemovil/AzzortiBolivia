package com.azzortiBolivia.dupree.mh_fragments_menu.incorporaciones.listado.incripcion;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.azzortiBolivia.dupree.mh_dialogs.ListString;
import com.azzortiBolivia.dupree.mh_dialogs.SingleListDialog;
import com.image.lib_image.util.PermissionCamera;
import com.dupreeinca.lib_api_rest.controller.InscripcionController;
import com.dupreeinca.lib_api_rest.controller.UploadFileController;
import com.dupreeinca.lib_api_rest.enums.EnumFormatDireccion;
import com.dupreeinca.lib_api_rest.model.base.TTError;
import com.dupreeinca.lib_api_rest.model.base.TTResultListener;
import com.dupreeinca.lib_api_rest.model.dto.request.Identy;
import com.dupreeinca.lib_api_rest.model.dto.request.InscriptionDTO;
import com.dupreeinca.lib_api_rest.model.dto.request.ValidateRef;
import com.dupreeinca.lib_api_rest.model.dto.response.GenericDTO;
import com.dupreeinca.lib_api_rest.util.models.ModelList;
import com.azzortiBolivia.dupree.R;
import com.azzortiBolivia.dupree.databinding.FragmentDatosPersonalesBinding;
import com.azzortiBolivia.dupree.files.LoadJsonFile;
import com.azzortiBolivia.dupree.files.ManagerFiles;
import com.azzortiBolivia.dupree.mh_dialogs.DateDialog;
import com.azzortiBolivia.dupree.mh_fragments_menu.incorporaciones.listado.Incorp_ListPre_Fragment;
import com.azzortiBolivia.dupree.mh_utilities.PinchZoomImageView;
import com.azzortiBolivia.dupree.mh_utilities.dialogs.DialogListOption;
import com.azzortiBolivia.dupree.model_view.DataAsesora;
import com.azzortiBolivia.dupree.mh_utilities.Validate;
import com.azzortiBolivia.dupree.view.activity.BaseActivityListener;
import com.azzortiBolivia.dupree.view.fragment.BaseFragment;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatosPersonalesFragment extends BaseFragment implements View.OnClickListener, PermissionCamera.Events {

    public static String TAG = DatosPersonalesFragment.class.getName();
    private FragmentDatosPersonalesBinding binding;
    private InscriptionDTO model;
    private InscripcionController inscripController;

    private LoadJsonFile jsonFile;
    private List<ModelList> listDirSend;
    List<String> listP;
    private List<ModelList> listSexo=null;
    private DataAsesora data;
    private PermissionCamera camera;
    private UploadFileController uploadFileController;
    //String urlImage="";
    boolean imge_sended=false;
    private static final int PDF417_REQUEST_CODE = 1;
    private EditText txtNameIncrip;
    private View view;
    private String estado;
    private List<ModelList> listCIExtension=null;

    public DatosPersonalesFragment() {

    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_datos_personales;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new InscriptionDTO();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            estado = bundle.getString("estado", estado);
            data = bundle.getParcelable(TAG);
            bundle.putParcelable(DatosPersonalesFragment.TAG, data);
            isOnCreate = true;
        }else{
            onBack();
        }
    }

    @Override
    protected void initViews(ViewDataBinding view) {
        binding = (FragmentDatosPersonalesBinding) view;
        binding.setCallback(this);
        binding.setModel(model);
        if(estado.equalsIgnoreCase("RECHAZADO")){
            binding.txtNameIncrip.setEnabled(true);
            binding.txtNameIncrip.setFocusableInTouchMode(true);
            binding.imgVolante.setClickable(false);
            binding.imgVolante.setEnabled(false);
            binding.imgVolante.setOnClickListener(null);
            binding.imgVolante.setVisibility(View.GONE);
            binding.linearImgVolante.setVisibility(View.GONE);
            binding.layoutTxtNamePre.setVisibility(View.VISIBLE);
            binding.layoutTxtLastnamePre.setVisibility(View.VISIBLE);
            binding.layoutTxtNameIncrip.setVisibility(View.GONE);

        }else{
            binding.imgVolante.setClickable(false);
            binding.imgVolante.setEnabled(false);
            binding.imgVolante.setOnClickListener(null);
            binding.imgVolante.setVisibility(View.GONE);
            binding.linearImgVolante.setVisibility(View.GONE);
            binding.layoutTxtNamePre.setVisibility(View.GONE);
            binding.layoutTxtLastnamePre.setVisibility(View.GONE);
            binding.layoutTxtNameIncrip.setVisibility(View.VISIBLE);
        }
        uploadFileController = new UploadFileController(getContext());
      /*  binding.imgVolante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelected = IMG_CED_FRT;
                showMenuImage();
//                permissionImage();
            }
        });*/
    }

    ////IMAGE
    private void showMenuImage(){
        DialogListOption dialogListOption = new DialogListOption(getActivity());
        List<ModelList> list = new ArrayList<>();
        list.add(new ModelList(0, getString(R.string.camara)));
        list.add(new ModelList(1, getString(R.string.galeria)));

        dialogListOption.showDialog(getString(R.string.seleccionar), list);
        dialogListOption.setDialogSelectModel(new DialogListOption.DialogSelectModel() {
            @Override
            public void onModelSelected(ModelList item) {
                switch (item.getId()) {
                    case 0:

                        camera.checkPermissionCamera();
                        break;
                    case 1:
                        camera.checkPermissionGalery();
                        break;
                }
            }
        });
    }

    @Override
    protected void onLoadedView() {
        camera = new PermissionCamera(this, this);
        inscripController = new InscripcionController(getContext());
        jsonFile = new LoadJsonFile(getContext());

        listDirSend = jsonFile.getParentezcos(ManagerFiles.DIR_SEND.getKey());
        listP = Arrays.asList(getResources().getStringArray(R.array.parentescoOptions));
        listCIExtension=getExtensions();
        listSexo=getSexs();
        if(isOnCreate){
            isOnCreate = false;
            loadData(data);
        }
    }

    public List<ModelList> getExtensions(){
        List<ModelList> result = new ArrayList<>();

        result.add(new ModelList(1, "BN"));
        result.add(new ModelList(2, "CB"));
        result.add(new ModelList(3, "CH"));
        result.add(new ModelList(4, "EX"));
        result.add(new ModelList(5, "LP"));
        result.add(new ModelList(6, "OR"));
        result.add(new ModelList(7, "PN"));
        result.add(new ModelList(8, "PT"));
        result.add(new ModelList(9, "TJ"));
        result.add(new ModelList(10, "SC"));
        return result;
    }
    public List<ModelList> getSexs(){
        List<ModelList> result = new ArrayList<>();

        result.add(new ModelList(1, "Femenino"));
        result.add(new ModelList(2, "Masculino"));

        return result;
    }

    public void loadData(DataAsesora data){
        clearData();
        model.setReferenciado_hint(getResources().getString(R.string.referido));
        model.setCedula(data.getCedula().isEmpty() ? Incorp_ListPre_Fragment.identySelected : data.getCedula());
        model.setNombre(data.getNombre().isEmpty() ? Incorp_ListPre_Fragment.nameSelected : data.getNombre());//se redunda xq a veces no llega

        model.setLetra2("Femenino");
        model.setFormato_direccion(data.getFormato_direccion());
        model.setModeEdit(data.isModeEdit());

        if(data.isModeEdit()){
            showProgress();
            inscripController.getInscripcion(new Identy(data.getCedula()), new TTResultListener<InscriptionDTO>() {
                @Override
                public void success(InscriptionDTO result) {
//                    Log.e(TAG, new Gson().toJson(result));
                    dismissProgress();
                    model.loadDataInit(result, listDirSend, listP);
                    model.setRefValidated(true);
                }

                @Override
                public void error(TTError error) {
                    dismissProgress();
                    checkSession(error);
                }
            });
        }
    }

    private void clearData(){
        model.clearData();
        model.setReferenciado_hint(getResources().getString(R.string.referido));
        setRefValidated(false);
    }
    @Override
    //Boton Lupa
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgB_Searchref://
                if(!model.getReferenciado_por().isEmpty()) {
                    showProgress();
                    inscripController.validateReferido(new ValidateRef(
                            model.getReferenciado_por(),
                            dataStore.getTokenSesion()), new TTResultListener<GenericDTO>() {
                        @Override
                        public void success(GenericDTO result) {
                            dismissProgress();
                            model.setReferenciado_hint(result.getResult());
                            setRefValidated(true);
                        }

                        @Override
                        public void error(TTError error) {
                            dismissProgress();
                            checkSession(error);
                        }
                    });
                }
                break;
            case R.id.txtCIExtension:
                if(listCIExtension!=null){
                    String titleToShow=getString(R.string.extension);
                    String sm=model.getLetra1();
                    int i=v.getId();
                    showList(i, titleToShow, listCIExtension, sm);
                }
                break;

            case R.id.txtSexo://
                if(listSexo!=null){
                    String s=getString(R.string.sexo);
                    String sm=model.getLetra2();
                    int i=v.getId();
                    showList(i, s, listSexo, sm);
                }
                break;
            case R.id.imgB_CleanRef://
                setRefValidated(false);
                break;
            case R.id.txtDateBird://
                showDate();
                break;
            case R.id.btnContinuar:
                if(validate()) {
                    nextPage();
                }
                break;
            case R.id.imgVolante:
                //imageSelected = IMG_CED_FRT;
                showMenuImage();
                break;
        }
    }

    public void showList(int id, String title, List<ModelList> data, String itemSelected){
        SingleListDialog dialog = new SingleListDialog();
        Log.i("@@--", "Showing list"+data.toString());
        dialog.loadData(title, data, itemSelected, new SingleListDialog.ListenerResponse() {
            @Override
            public void result(ModelList item) {
                switch(id){
                    case R.id.txtSexo:
                        binding.txtSexo.setError(null);
                        model.setLetra2(item.getName());
                        break;
                    case R.id.txtCIExtension:
                        binding.txtCIExtension.setError(null);
                        model.setLetra1(item.getName());
                        break;
                }
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(),"mDialog");
    }

    private void clearSexo(){
        listSexo=null;
        model.setLetra2("");
    }


    public boolean validate() {
        Log.e(TAG,"validateRegister() -> init()");
        Validate valid = new Validate();
        //datos personales
        if(model.getNombre().isEmpty()){
            msgToast("Nombre de asesora... Verifique");
            valid.setLoginError(getString(R.string.campo_requerido),binding.txtNameIncrip);
            return false;
        } else if(model.getCedula().isEmpty()){
            msgToast("Cêdula de asesora... Verifique");
            valid.setLoginError(getString(R.string.campo_requerido),binding.txtIdentyCard);
            return false;
        } else if(model.getReferenciado_por().isEmpty()){
            msgToast("Cêdula de referido... Verifique");
            valid.setLoginError(getString(R.string.campo_requerido),binding.txtIdentyCardRef);
            return false;
        } else if(!model.isRefValidated()){
            msgToast("Debe validar la cédula del referido... Verifique");
            valid.setLoginError(getString(R.string.deba_validar),binding.txtIdentyCardRef);
            return false;
        } else if (model.getLetra3().isEmpty()) {
            msgToast("Debe digitar un sector un sector ...");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), binding.txtSector);
            return false;
        } else if (model.getLetra1().isEmpty()) {
            msgToast("Seleccione una Extencion para el CI...");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), binding.txtCIExtension);
            return false;
        } else if (model.getNacimiento().isEmpty()) {
            msgToast("Seleccione fecha de nacimiento...");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), binding.txtDateBird);
            return false;
        } else if (model.getZona_seccion().isEmpty()) {
            msgToast("Zona sección... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), binding.txtZone);
            return false;
        } else if (model.getZona_seccion().length() != 3 && model.getZona_seccion().length() != 6) {
            msgToast("Zona sección... Verifique");
            valid.setLoginError("Debe ser un valor de 3 o 6 números", binding.txtZone);
            return false;
        }
        return true;
    }

    public void nextPage(){
        Bundle bundle = new Bundle();

        if(data.getFormato_direccion().equals(EnumFormatDireccion.FORMATO_1.getKey())) {
            bundle.putParcelable(DirFormato1Fragment.TAG, model);
            baseActivityListener.replaceFragmentWithBackStack(DirFormato1Fragment.class, true, bundle);
        } else if (data.getFormato_direccion().equals(EnumFormatDireccion.FORMATO_2.getKey())){
            bundle.putParcelable(DirFormato2Fragment.TAG, model);
            baseActivityListener.replaceFragmentWithBackStack(DirFormato2Fragment.class, true, bundle);
        } else if (data.getFormato_direccion().equals(EnumFormatDireccion.FORMATO_3.getKey())){
            bundle.putParcelable(DirFormato3Fragment.TAG, model);
            baseActivityListener.replaceFragmentWithBackStack(DirFormato3Fragment.class, true, bundle);
        }  else if (data.getFormato_direccion().equals(EnumFormatDireccion.FORMATO_4.getKey())){//@@

            bundle.putParcelable(DirFormato4Fragment.TAG, model);
            baseActivityListener.replaceFragmentWithBackStack(DirFormato4Fragment.class, true, bundle);
        }
    }

    public void setRefValidated(boolean refValidated) {
        binding.txtIdentyCardRef.setError(null);
        model.setReferenciado_por(refValidated ? model.getReferenciado_por() : "");
        model.setReferenciado_hint(refValidated ? model.getReferenciado_hint() : getString(R.string.referido));
//        model.setNacimiento("");
//        model.setZona_seccion("");

        model.setRefValidated(refValidated);
    }

    public void showDate(){
        DateDialog newFragment = new DateDialog();
        newFragment.setData(new DateDialog.ListenerResponse() {
            @Override
            public void result(String date) {
                binding.txtDateBird.setError(null);
                model.setNacimiento(date);
            }
        });
        newFragment.show(getFragmentManager(), "datePicker");
    }

    BaseActivityListener baseActivityListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivityListener) {
            baseActivityListener = (BaseActivityListener) context;
        } else
            throw new RuntimeException(context.toString().concat(" is not OnInteractionActivity"));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        baseActivityListener = null;
    }

    File fileList;
    @Override
    public void imageFile(File imageFile) {
        Log.e(TAG, "imageFile: "+new Gson().toJson(imageFile));
        this.fileList=imageFile;
        sendImageMultiPart(fileList);
    }


    private void sendImageMultiPart(File file){
        showProgress();
        uploadFileController.uploadImage(file, new TTResultListener<GenericDTO>() {
            @Override
            public void success(GenericDTO result) {
                dismissProgress();

                imge_sended = true;
                urlImage = result.getResult();
                controlImage(true);

                imageProfile(urlImage);

                model.setImg_terminos(urlImage);
            }

            @Override
            public void error(TTError error) {
                dismissProgress();
                checkSession(error);
            }
        });
    }


    private void updateFile(File file, int indexFile){
        Log.e(TAG,"File: "+String.valueOf(indexFile)+", "+file.getAbsolutePath());
        switch (indexFile){
           /* case IMG_CED_FRT:
                imageProfile(file.getAbsolutePath());
                model.setImg_terminos(file.getAbsolutePath());
                break;*/
            /*case IMG_CED_ADV:
                model.setCedula_adverso(file.getAbsolutePath());
                break;
            case IMG_PAG_FRT:
                model.setPagare_frontal(file.getAbsolutePath());
                break;
            case IMG_PAG_ADV:
                model.setPagare_adverso(file.getAbsolutePath());
                break;*/
        }
    }

    private void controlImage(boolean control){
        binding.txtIdentyCard.setEnabled(control);
        binding.txtNameIncrip.setEnabled(control);
        binding.imgBSearchref.setEnabled(control);
        binding.imgBCleanRef.setEnabled(control);
    }

   /* ImageLoader img;
    private void imageProfile(String url){
        Log.i(TAG, "imageProfile: "+url);
        img = ImageLoader.getInstance();
        img.init(PinchZoomImageView.configurarImageLoader(getActivity()));
        //img.clearMemoryCache();
        //img.clearDiskCache();
       // img.displayImage(url, binding.imgVolante);
        img.displayImage(url, binding.imgVolante, new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build());
    }*/

    ImageLoader img;
    private void imageProfile(String url){
        Log.i(TAG, "imageProfile: "+url);
        img = ImageLoader.getInstance();
        img.init(PinchZoomImageView.configurarImageLoader(getActivity()));
        //img.clearMemoryCache();
        //img.clearDiskCache();
        img.displayImage(url, binding.imgVolante);
    }
    String urlImage="";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Entra", "Entra a activity result lee documento");

        try
        {
            if(requestCode == PDF417_REQUEST_CODE)
            {
                if (!data.getStringExtra("number").contains("Fallo"))
                {
                    //devolver a la forma
                    binding.txtIdentyCard.setText(data.getStringExtra("number"));
                    binding.txtNameIncrip.setText(data.getStringExtra("name"));
                    //binding.txtLastname.setText(data.getStringExtra("lastname"));
                    //binding.imgBSearchref.callOnClick();
                    msgToast("Lectura correcta del Documento");
                }
                else
                {
                    msgToast("Error al leer cedula, intente nuevamente");
                }
            }
            else
            {
                super.onActivityResult(requestCode, resultCode, data);
                camera.onActivityResult(requestCode, resultCode, data, false);
            }
        }
        catch (Exception e){
            msgToast("Imposible leer documento");
        }
    }
}
