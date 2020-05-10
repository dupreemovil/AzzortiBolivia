package com.azzortiBolivia.dupree.mh_fragments_menu.incorporaciones.Preinscripcion;


import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.image.lib_image.util.PermissionCamera;
import com.dupreeinca.lib_api_rest.controller.InscripcionController;
import com.dupreeinca.lib_api_rest.controller.UploadFileController;
import com.dupreeinca.lib_api_rest.model.base.TTError;
import com.dupreeinca.lib_api_rest.model.base.TTResultListener;
import com.dupreeinca.lib_api_rest.model.dto.request.Identy;
import com.dupreeinca.lib_api_rest.model.dto.request.PosiblesNuevas;
import com.dupreeinca.lib_api_rest.model.dto.request.Register;
import com.dupreeinca.lib_api_rest.model.dto.response.GenericDTO;
import com.dupreeinca.lib_api_rest.model.view.Profile;
import com.dupreeinca.lib_api_rest.util.models.ModelList;
import com.azzortiBolivia.dupree.R;
import com.azzortiBolivia.dupree.databinding.FragmentNewPreinsciptionBinding;
import com.azzortiBolivia.dupree.files.LoadJsonFile;
import com.azzortiBolivia.dupree.files.ManagerFiles;
import com.azzortiBolivia.dupree.mh_CRUD.CRUDPosibles_Nuevas;
import com.azzortiBolivia.dupree.mh_adapters.IncorporacionPagerAdapter;
import com.azzortiBolivia.dupree.mh_dialogs.ListString;
import com.azzortiBolivia.dupree.mh_dialogs.SingleListDialog;
import com.azzortiBolivia.dupree.mh_fragments_menu.incorporaciones.IncorporacionesVPages;
import com.azzortiBolivia.dupree.mh_utilities.PinchZoomImageView;
import com.azzortiBolivia.dupree.mh_utilities.Validate;
import com.azzortiBolivia.dupree.mh_utilities.dialogs.DialogListOption;
import com.azzortiBolivia.dupree.mh_utilities.mPreferences;
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
public class PreinsciptionFragment extends BaseFragment implements PermissionCamera.Events{

    private static final int PDF417_REQUEST_CODE = 1;
    public static final String TAG = PreinsciptionFragment.class.getName();
    private InscripcionController inscripcionController;
    private UploadFileController uploadFileController;
    private PermissionCamera camera;

    private FragmentNewPreinsciptionBinding binding;

    private List<ModelList> listTipoDoc;
    private LoadJsonFile jsonFile;
    private PosiblesNuevas posiblesNuevas;

    public PreinsciptionFragment() {
        // Required empty public constructor
    }

    public void loadData(){
    }

    public void loadData(PosiblesNuevas posiblesNuevas){
        this.posiblesNuevas=posiblesNuevas;
    }

    public static PreinsciptionFragment newInstance() {
        Bundle args = new Bundle();

        PreinsciptionFragment fragment = new PreinsciptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    boolean imge_sended=false;

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_new_preinsciption;
    }

    @Override
    protected void initViews(ViewDataBinding view) {
        onAttachFragment(getParentFragment());

        binding = (FragmentNewPreinsciptionBinding) view;

        inscripcionController = new InscripcionController(getContext());
        uploadFileController = new UploadFileController(getContext());
        jsonFile = new LoadJsonFile(getContext());
        //Datos personales
        binding.txtSpnTypeId.setOnClickListener(mListenerClick);
        binding.imgBSearchref.setOnClickListener(mListenerClick);
        binding.imgBCleanRef.setOnClickListener(mListenerClick);
        binding.imgVolante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenuImage();
//                permissionImage();
            }
        });
        binding.btCamera.setOnClickListener(mListenerClick);
        binding.btnRegister.setOnClickListener(mListenerClick);
    }

    @Override
    protected void onLoadedView() {
        camera = new PermissionCamera(this, this);

        listTipoDoc = jsonFile.getParentezcos(ManagerFiles.TIPO_DOC.getKey());

        setRefValidated(false);

        clearAllData();

        controlImage(true);

        if(posiblesNuevas!=null)
        {
            binding.txtSpnTypeId.setText(posiblesNuevas.getTipo_docu());
            binding.txtIdenty.setText(posiblesNuevas.getNume_iden());
            //binding.imgBSearchref.callOnClick();
            binding.txtName.setText(posiblesNuevas.getNomb_terc());
            binding.txtLastname.setText(posiblesNuevas.getApel_terc());
        }
    }


    View.OnClickListener mListenerClick =
            new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Validate validate = new Validate();
                    switch (view.getId()){
                        case R.id.imgB_Searchref:
                            binding.txtIdenty.setError(null);

                            if(binding.txtIdenty.getText().toString().isEmpty() || binding.txtIdenty.getText().toString().length() < 6){
                                msgToast(getString(R.string.mas_de_seis_caracteres));
                                validate.setLoginError(getString(R.string.campo_requerido), binding.txtIdenty);
                            } else if(!binding.txtIdenty.getText().toString().isEmpty()) {
                                cifin(new Identy(binding.txtIdenty.getText().toString()));
                            }
                            break;
                        case R.id.imgB_CleanRef:
                            setRefValidated(false);
                            break;
                        case R.id.txtSpnTypeId:
                            showList(ListString.BROACAST_REG_TYPE_DOC, getString(R.string.tipo_de_documento), listTipoDoc, binding.txtSpnTypeId.getText().toString());
                            break;
                        case R.id.btnRegister:
                            register();
                            break;
                        case R.id.bt_Camera:
                           /* if(binding.txtSpnTypeId.getText().toString().contains("dula")){
                                Toast.makeText(getContext(),"Activando Camara",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), BarcodeCaptureActivity.class);
                                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
                                startActivityForResult(intent,1);
                                Log.e("EN FRAGMENT:", "Lector de Cedula");
                            }else{*/
                                msgToast("Habilitado solo para Cedula");
                            //}
                            break;
                    }

                }
            };

    private void cifin(Identy data){
        showProgress();
        inscripcionController.validateCentralRiesgo(data, new TTResultListener<GenericDTO>() {
            @Override
            public void success(GenericDTO result) {
                dismissProgress();

                String name = result.getResult();
                if(name!=null) {
                    binding.txtInputRiesgo.setHint(name);//
                    setRefValidated(!name.equals("RECHAZADO"));
                    if(name.equals("RECHAZADO"))
                        showSnackBarDuration(name+" por centrales de riesgo", 5000);
                }
            }

            @Override
            public void error(TTError error) {
                dismissProgress();
                checkSession(error);
            }
        });
    }

    public void showList(String tag, String title, List<ModelList> data, String itemSelected){
        SingleListDialog dialog = new SingleListDialog();
        dialog.loadData(title, data, itemSelected, new SingleListDialog.ListenerResponse() {
            @Override
            public void result(ModelList item) {
                Log.i(TAG, tag);
                switch(tag){
                    case ListString.BROACAST_REG_TYPE_DOC:
                        binding.txtSpnTypeId.setError(null);
                        binding.txtSpnTypeId.setText(item.getName());
                        break;
                }
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(),"mDialog");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG,"onDestroyView()");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG,"onResume()");
        //setSelectedItem(oldItem);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy()");
    }

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


    public void register(){
        if(ValidateRegister()){
            String jsonPerfil = mPreferences.getJSON_TypePerfil(getActivity());

            if(jsonPerfil!=null){
                Profile perfil = new Gson().fromJson(jsonPerfil, Profile.class);
                if ((perfil != null) && (!perfil.getPerfil().equals(Profile.ADESORA))) {
                    sendDataRegister(urlImage);
                } else {
                    //searchTermins();
                }
            } else{
                //searchTermins();
            }
        }
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
            }

            @Override
            public void error(TTError error) {
                dismissProgress();
                checkSession(error);
            }
        });
    }

    public Boolean ValidateRegister()
    {
        Validate valid=new Validate();
        //datos personales
        if (binding.txtSpnTypeId.getText().toString().isEmpty())
        {
            msgToast("Seleccione tipo de documento...");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), binding.txtSpnTypeId);
            return false;
        }
       /* else if (valid.isValidInteger(binding.txtIdenty.getText().toString()))
        {
            msgToast("Cédula invalida...");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), binding.txtIdenty);
            return false;
        }*/
        else if(!isRefValidated()){
            msgToast("Debe validar la cédula... Verifique");
            valid.setLoginError(getString(R.string.deba_validar),binding.txtIdenty);
            return false;
        }
        else if (binding.txtName.getText().toString().isEmpty())
        {
            msgToast("Nombre invalido...");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), binding.txtName);
            return false;
        }
        else if (binding.txtLastname.getText().toString().isEmpty())
        {
            msgToast("Apellido invalido...");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), binding.txtLastname);
            return false;
        }

        return true;
    }

    public Boolean ValidateTermins() {
        //if(pathImage==null) {
        if(urlImage == null) {
            msgToast("Debe seleccionar un volante");
            return false;
        }
        return  true;
    }

    public Register obtainDataUser(String pathImage)
    {
        return new Register(
                String.valueOf(Arrays.asList(getResources().getStringArray(R.array.typeIdenty)).indexOf(binding.txtSpnTypeId.getText().toString())),
                binding.txtIdenty.getText().toString(),
                binding.txtName.getText().toString(),
                binding.txtLastname.getText().toString(),
                pathImage

        );
    }

    private void sendDataRegister(String urlImage){
        showProgress();
        Register register = obtainDataUser(urlImage);
        inscripcionController.postPreinscripcion(register, new TTResultListener<GenericDTO>() {
            @Override
            public void success(GenericDTO result) {
                dismissProgress();

                String data = result.getResult();
                if(data != null)
                    msgToast(data);

                clearAllData();
                controlImage(true);

                if (posiblesNuevas != null){
                    CRUDPosibles_Nuevas.borrariD(posiblesNuevas.getId());
                }else{
                    CRUDPosibles_Nuevas.borrarNueva(register.getCedula());
                }

                viewParent.gotoPage(IncorporacionPagerAdapter.PAGE_LIST_PRE);
            }

            @Override
            public void error(TTError error) {
                dismissProgress();
                checkSession(error);
            }
        });
    }

    private void controlImage(boolean control){
        binding.txtIdenty.setEnabled(control);
        binding.txtSpnTypeId.setEnabled(control);
        binding.imgBSearchref.setEnabled(control);
        binding.imgBCleanRef.setEnabled(control);
    }

    private void clearAllData(){
        imge_sended = false;
        urlImage=null;

        controlImage(true)
;

        binding.txtSpnTypeId.setText("");
        binding.txtIdenty.setText("");
        binding.txtInputRiesgo.setHint(getResources().getString(R.string.cedula));
        fileList = null;
        binding.imgVolante.setImageResource(R.drawable.ph_add_image2);

        binding.txtName.setText("");
        binding.txtLastname.setText("");

        setRefValidated(false);
    }

    private boolean refValidated=true;

    public boolean isRefValidated() {
        return refValidated;
    }

    public void setRefValidated(boolean refValidated) {
        this.refValidated = refValidated;

        binding.btnRegister.setEnabled(refValidated);
        binding.btnRegister.setBackground(refValidated ? getResources().getDrawable(R.drawable.rounded_background_blue) : getResources().getDrawable(R.drawable.rounded_background_gray));


        fileList=null;

        binding.txtIdenty.setEnabled(!refValidated);
        binding.txtIdenty.setError(null);

        binding.imgBSearchref.setVisibility(refValidated ? View.VISIBLE : View.VISIBLE);
        binding.imgBCleanRef.setVisibility(!refValidated ? View.GONE : View.VISIBLE);
        binding.txtIdenty.setText(refValidated ? binding.txtIdenty.getText().toString() : "");
        binding.txtName.setText(refValidated ? binding.txtName.getText().toString() : "");
        binding.txtLastname.setText(refValidated ? binding.txtLastname.getText().toString() : "");


        binding.txtInputRiesgo.setHint(refValidated ? binding.txtInputRiesgo.getHint() : getString(R.string.cedula));

        //habilitando campos debajo
        binding.txtName.setEnabled(refValidated);
        binding.txtLastname.setEnabled(refValidated);

    }

    //Parent Fragment
    IncorporacionesVPages viewParent;
    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof IncorporacionesVPages) {
            viewParent = (IncorporacionesVPages) childFragment;
        } else {
            //throw new RuntimeException(childFragment.toString().concat(" is not OnInteractionActivity"));
            Log.e(TAG, "is not OnInteractionActivity");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        viewParent = null;
//        menuListener = null;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        camera.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

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
                    binding.txtIdenty.setText(data.getStringExtra("number"));
                    binding.txtName.setText(data.getStringExtra("name"));
                    binding.txtLastname.setText(data.getStringExtra("lastname"));
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

    File fileList;
    @Override
    public void imageFile(File imageFile) {
        Log.e(TAG, "imageFile: "+new Gson().toJson(imageFile));
        this.fileList=imageFile;
        sendImageMultiPart(fileList);
    }
}



