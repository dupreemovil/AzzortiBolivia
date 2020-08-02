package com.dupreeincabolnuevo.dupree.mh_fragments_menu;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.dupreeincabolnuevo.dupree.R;


import com.dupreeincabolnuevo.dupree.databinding.FragmentPedidoDigitadoBinding;
import com.dupreeincabolnuevo.dupree.mh_adapters.PedidosDigitadosListAdapter;
import com.dupreeincabolnuevo.dupree.mh_holders.PedidosDigitadosHolder;
import com.dupreeincabolnuevo.dupree.view.fragment.BaseFragment;
import com.dupreeinca.lib_api_rest.controller.ReportesController;
import com.dupreeinca.lib_api_rest.model.base.TTError;
import com.dupreeinca.lib_api_rest.model.base.TTResultListener;
import com.dupreeinca.lib_api_rest.model.dto.request.Identy;
import com.dupreeinca.lib_api_rest.model.dto.response.PedidoDigitado;
import com.dupreeinca.lib_api_rest.model.dto.response.PedidoDigitadoDTO;
import com.dupreeinca.lib_api_rest.model.view.Profile;

import java.util.ArrayList;
import java.util.List;

public class PedidosDigitadosFragment extends BaseFragment implements PedidosDigitadosHolder.Events{
    private final String TAG = PedidosDigitadosFragment.class.getName();
    private FragmentPedidoDigitadoBinding  binding;
    private ReportesController reportesController;

    private List<PedidoDigitado> list, listFilter;
    private PedidosDigitadosListAdapter adapter_pedido_digitado;

    public PedidosDigitadosFragment() {
        // Required empty public constructor
    }

    private Profile perfil;
    public void loadData(Profile perfil){
        this.perfil=perfil;
    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_pedido_digitado;
    }

    @Override
    protected void initViews(ViewDataBinding view) {
        binding = (FragmentPedidoDigitadoBinding) view;

        binding.cardViewBackGround.setVisibility(View.INVISIBLE);
        binding.tvNombreAsesora.setText("");
        binding.recycler.setLayoutManager(new GridLayoutManager(getActivity(),1));
        binding.recycler.setHasFixedSize(true);

        list = new ArrayList<>();
        listFilter = new ArrayList<>();
        //incentivoRefs = getResult();

        listFilter.addAll(list);

        adapter_pedido_digitado = new PedidosDigitadosListAdapter(list, listFilter, this);
        binding.recycler.setAdapter(adapter_pedido_digitado);

    }

    @Override
    protected void onLoadedView() {
        reportesController = new ReportesController(getContext());
        checkPedidosDigitados();
    }


    public void checkPedidosDigitados(){

        if(list.size()<1){//OJO REFREZCAR CON PULL REFRESH
            showProgress();
            String s=perfil.getValor();
            reportesController.getPedidosDigitados(new Identy(  ""), new TTResultListener<PedidoDigitadoDTO>() {
                @Override
                public void success(PedidoDigitadoDTO result) {
                    dismissProgress();
                    addPedidosDigitados(result);
                    updateView(result);

                }
                @Override
                public void error(TTError error) {
                    dismissProgress();
                    checkSession(error);
                }
            });
        }
    }

    private void addPedidosDigitados(PedidoDigitadoDTO  pedidosDigitados){
        list.clear();
        listFilter.clear();

        if(pedidosDigitados!=null) {
            list.addAll(pedidosDigitados.getResult());
            listFilter.addAll(pedidosDigitados.getResult());
            adapter_pedido_digitado.notifyDataSetChanged();
        }
    }

    private void updateView(PedidoDigitadoDTO listaACargar){
        list.clear();
        listFilter.clear();
        list.addAll(listaACargar.getResult());
        listFilter.addAll(listaACargar.getResult());

        binding.cardViewBackGround.setVisibility(View.VISIBLE);
        binding.tvNombreAsesora.setText(listaACargar.getStatus());

        adapter_pedido_digitado.notifyDataSetChanged();
    }

    public void searchNewIdenty(String cedula){
        showProgress();
        reportesController.getPedidosDigitados(new Identy(cedula), new TTResultListener<PedidoDigitadoDTO>() {
            @Override
            public void success(PedidoDigitadoDTO result) {
                dismissProgress();
                updateView(result);
            }

            @Override
            public void error(TTError error) {
                dismissProgress();
                checkSession(error);
            }
        });
    }

    @Override
    public void onClickRoot(PedidoDigitado dataRow, int row) {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_search, menu);
//        final MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setQueryHint(getString(R.string.cedula_asesora));
//
//        EditText txtSearch = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        txtSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Log.e(TAG, "onCreateOptionsMenu() -> onQueryTextSubmit() -> " + query);
//                searchMyQuery(query);
//                searchView.clearFocus();
//                return false;//habilita el serach del teclado
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Log.e(TAG, "onCreateOptionsMenu() -> onQueryTextChange() -> " + newText);
//                searchViewTextChange(newText);
//                return false;
//            }
//        });
//
//        searchView.setIconified(true);//inicialmente oculto

//        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchMyQuery(String query){
        searchNewIdenty(query);
    }

    public void searchViewTextChange(String newText) {

    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach()");
        setHasOptionsMenu(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach()");
        setHasOptionsMenu(false);
    }
}

