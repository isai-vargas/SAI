package com.example.sai.Data;

import com.example.sai.Model.BodegaModel;
import com.example.sai.Model.ClienteModel;
import com.example.sai.Model.EmpresaModel;
import com.example.sai.Model.LoginResponseModel;
import com.example.sai.Model.MenuItemModel;
import com.example.sai.Model.SerieModel;
import com.example.sai.Model.SucursalModel;
import com.example.sai.Model.UsuarioEmpresaModel;
import com.example.sai.Service.UsuarioEmpresaService;

import java.util.List;

public class DatosGlobalesSingleton {

    private static DatosGlobalesSingleton instance;
    //definicon de modelos
    private EmpresaModel empresaModel;
    private UsuarioEmpresaModel usuarioEmpresaModel;
    private LoginResponseModel loginResponseModel;
    private SucursalModel sucursalModel;
    private BodegaModel bodegaModel;
    private SerieModel serieModel;
    private ClienteModel clienteModel;


    private List<MenuItemModel> menuItemModelList;



    // Constructor privado para evitar instanciación directa
    private DatosGlobalesSingleton() {
    }

    // Método para obtener la única instancia de la clase
    public static synchronized DatosGlobalesSingleton getInstance() {
        if (instance == null) {
            instance = new DatosGlobalesSingleton();
        }
        return instance;
    }

    // Métodos de acceso a los modelos a guardar o extraer datos

    public EmpresaModel getEmpresaModel()
    {
        return empresaModel;
    }

    public void setEmpresaModel(EmpresaModel empresaModel)
    {
        this.empresaModel = empresaModel;
    }

    public UsuarioEmpresaModel getUsuarioEmpresaModel()
    {
        return usuarioEmpresaModel;
    }

    public void setUsuarioEmpresaModel(UsuarioEmpresaModel usuarioEmpresaModel)
    {
        this.usuarioEmpresaModel = usuarioEmpresaModel;
    }

    public LoginResponseModel getLoginResponseModel()
    {
        return loginResponseModel;
    }

    public  void setLoginResponseModel (LoginResponseModel loginResponseModel)
    {
        this.loginResponseModel = loginResponseModel;
    }


    public SucursalModel getSucursalModel()
    {
        return sucursalModel;
    }

    public  void setSucursalModel (SucursalModel sucursalModel)
    {
        this.sucursalModel = sucursalModel;
    }

    public BodegaModel getBodegaModel()
    {
        return bodegaModel;
    }

    public void setBodegaModel (BodegaModel bodegaModel)
    {
        this.bodegaModel = bodegaModel;
    }


    public SerieModel getSerieModel()
    {
        return serieModel;
    }

    public void  setSerieModel (SerieModel serieModel)
    {
        this.serieModel = serieModel;
    }

    //limpiar serie
    public  void clearSerieModel(){
        this.serieModel = null;
    }


    public ClienteModel getClienteModel()
    {
        return clienteModel;
    }

    public void setClienteModel(ClienteModel clienteModel)
    {
        this.clienteModel = clienteModel;
    }

    // Método para limpiar el clienteModel
    public void clearClienteModel()
    {
        this.clienteModel = null;
    }

    public List<MenuItemModel> getMenuItemModelList()
    {
        return menuItemModelList;
    }
    public void setMenuItemModelList (List<MenuItemModel> menuItemModelList)
    {
        this.menuItemModelList = menuItemModelList;
    }




}
