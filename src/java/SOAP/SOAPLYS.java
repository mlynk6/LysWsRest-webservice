/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOAP;

import com.lys.beans.AccesosDB;
import com.lys.beans.InspeccionesGenCab;
import com.lys.beans.InspeccionesGenDet;
import com.lys.beans.InspeccionesMaqCab;
import com.lys.beans.InspeccionesMaqDet;
import com.lys.beans.Menu;
import com.lys.beans.MenuDB;
import com.lys.beans.Parametros;
import com.lys.beans.SubMenu;
import com.lys.beans.SubMenuBotones;
import com.lys.beans.UsuarioDB;
import com.lys.conection.GetResultSet;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author dvillanueva
 */
@WebService(serviceName = "SOAPLYS")
public class SOAPLYS {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    @WebMethod(operationName = "ListUsers")
    public Parametros[] ListUsers() throws java.lang.Exception {
        List<Parametros> listPar = new ArrayList<Parametros>();
        GetResultSet cresult = new GetResultSet();
        String Sql = "SELECT c_codigousuario,c_nombre FROM  dbo.MTP_USUARIO";
        ResultSet rs = cresult.CreateConection(Sql);//this.CreateConection(Sql);

        while (rs.next()) {

            Parametros par = new Parametros(rs.getString(1), rs.getString(2));
            listPar.add(par);

        }
        Parametros[] l2 = new Parametros[listPar.size()];

        for (int i = 0; i < listPar.size(); i++) {

            l2[i] = listPar.get(i);

        }
//ExceptionBean
        return l2;
    }

    @WebMethod(operationName = "ListaMenu")
    public Menu[] ListaMenu(@WebParam(name = "CodUsuario", targetNamespace = "http://SOAP/") String CodUsuario) throws java.lang.Exception {
        //TODO write your implementation code here:
        String msj = ActualizarMenuAcceso();
        Menu[] ListMenu;

        //-------------------------
        List<Menu> list = new ArrayList<Menu>();

        String query = "EXEC dbo.SP_LISTAR_MENU '" + CodUsuario + "'";

        GetResultSet cresult = new GetResultSet();
        ResultSet rs = cresult.CreateConection(query);
        while (rs.next()) {

            Menu men = new Menu(rs.getString(1), rs.getString(2), rs.getString(3));
            list.add(men);

        }

        ListMenu = new Menu[list.size()];
        for (int i = 0; i < list.size(); i++) {

            ListMenu[i] = list.get(i);

        }

        return ListMenu;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "ListSubMenu")
    public List<SubMenu> ListSubMenu(@WebParam(name = "CodUsuario", targetNamespace = "http://SOAP/") String CodUsuario) throws Exception {
        //TODO write your implementation code here:
        List<SubMenu> listSub = new ArrayList<SubMenu>();

        String query = "EXEC dbo.SP_LISTAR_SUBMENU '" + CodUsuario + "'";
        GetResultSet cresult = new GetResultSet();
        ResultSet rs = cresult.CreateConection(query);
        while (rs.next()) {

            SubMenu smenu = new SubMenu(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
            listSub.add(smenu);

        }

        return listSub;
    }

    /**    
     * Web service operation
     */
    private String ActualizarMenuAcceso() throws Exception {

        String msj = "OK";
        String sqlAccesos = "EXEC SP_SINCRONIZAR_ACCESOS";
        String sqlMenu = "EXEC SP_SINCRONIZAR_MENU";

        GetResultSet Acceso = new GetResultSet();
        msj = Acceso.ExecProcedure(sqlAccesos);
        GetResultSet Menus = new GetResultSet();
       msj= Menus.ExecProcedure(sqlMenu);

        return msj;

    }

    

    @WebMethod(operationName = "ListSubMenuBotones")
    public List<SubMenuBotones> ListSubMenuBotones(@WebParam(name = "CodPadre", targetNamespace = "http://SOAP/") String CodPadre, @WebParam(name = "CodSubMenu", targetNamespace = "http://SOAP/") String CodSubMenu, @WebParam(name = "CodUsuario", targetNamespace = "http://SOAP/") String CodUsuario) throws Exception {
         
        List<SubMenuBotones> listSBotones = new ArrayList<SubMenuBotones>();

        String query = "EXEC dbo.SP_LISTAR_SUBMENU_ITEMS '" + CodPadre + "' , '" + CodSubMenu + "' , '" + CodUsuario + "'";
        GetResultSet cresult = new GetResultSet();
        ResultSet rs = cresult.CreateConection(query);
        while (rs.next()) {

            SubMenuBotones smenu = new SubMenuBotones(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
            listSBotones.add(smenu);

        }
        return listSBotones;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "GetDataMenu")
    public List<MenuDB> GetDataMenu() throws Exception {
          String msj = ActualizarMenuAcceso();
        //TODO write your implementation code here:
        List<MenuDB> dataMenu = new ArrayList<MenuDB>();
         List<MenuDB> vartest = new ArrayList<MenuDB>();
        String query = "SELECT * FROM  dbo.MTP_MENUS";
        GetResultSet cresult = new GetResultSet();
        ResultSet rs = cresult.CreateConection(query);
        
        
        while  (rs.next()){
        
            MenuDB mn = new MenuDB ();
            mn.setAppCodigo(rs.getString(1));
            mn.setNivel1(rs.getString(2));
            mn.setNivel2(rs.getString(3));
            mn.setNivel3(rs.getString(4));
            mn.setNivel4(rs.getString(5));
            mn.setNivel5(rs.getString(6));
            mn.setNombreMenu(rs.getString(7));
            mn.setDescripcion(rs.getString(8));
            dataMenu.add(mn);
        
        }
        
        
        return dataMenu;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "GetDataAccesos")
    public List<AccesosDB> GetDataAccesos() throws Exception {
        //TODO write your implementation code here:
        List<AccesosDB> dataAcceso = new ArrayList<AccesosDB>();
        String query = "SELECT * FROM  dbo.MTP_ACCESO";
        GetResultSet cresult = new GetResultSet();
        ResultSet rs = cresult.CreateConection(query);
        while (rs.next()){
            
            AccesosDB ac = new AccesosDB();
            
            ac.setUsuario(rs.getString(1));
            ac.setAppCodigo(rs.getString(2));
            ac.setNivelAcc(rs.getString(3));
            ac.setAcceso(rs.getString(4));
            ac.setNuevo(rs.getString(5));
            ac.setModificar(rs.getString(6));
            ac.setEliminar(rs.getString(7));
            ac.setOtros(rs.getString(8));
            
            dataAcceso.add(ac);
        
        }
        
       
        
        return dataAcceso;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "saveImage")
    public String saveImage(@WebParam(name = "imgeByte",targetNamespace = "http://SOAP/") byte[] imgeByte, @WebParam(name = "fileName",targetNamespace = "http://SOAP/") String fileName) throws Exception {
        
        String  msg= "";
         String filePathServer="";
        String extension = fileName.substring(fileName.length()-3);
        if (extension.toUpperCase().equals("JPG")){
        
              filePathServer =  File.separator+File.separator+"IBSERVER_1"+File.separator+"Servidor de Archivos"+File.separator+"Fotos_Tablet"+File.separator + fileName;
        }
        else {
         filePathServer =  File.separator+File.separator+"IBSERVER_1"+File.separator+"Servidor de Archivos"+File.separator+"Fotos_Tablet"+File.separator + fileName+".jpg";
       
        }
        
        
       
             try {
            FileOutputStream fos = new FileOutputStream(filePathServer);
            BufferedOutputStream outputStream = new BufferedOutputStream(fos);
            outputStream.write(imgeByte);
            outputStream.close();
             
            msg="Received file: " + filePathServer;
            System.out.println("Received file: " + filePathServer);
             
        } catch (Exception ex) {
            System.err.println(ex);
            msg= ex.getMessage();
        //    throw new WebServiceException(ex);
        }
        return msg;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "GetInspeccionesMaqCab")
    public ArrayList<InspeccionesMaqCab> GetInspeccionesMaqCab(@WebParam(name = "Usuario",targetNamespace = "http://SOAP/") String Usuario) throws Exception {
        //TODO write your implementation code here:
        ArrayList<InspeccionesMaqCab> result  = new ArrayList<InspeccionesMaqCab>();
         String query = "SELECT * FROM dbo.MTp_INSPECCIONMAQUINA_CAB WHERE c_usuarioInspeccion ='"+Usuario+"'";
        GetResultSet cresult = new GetResultSet();
        ResultSet rs = cresult.CreateConection(query);
        
        while (rs.next()){
        
            InspeccionesMaqCab insp = new InspeccionesMaqCab ();
            insp.setC_compania(rs.getString(1));
            insp.setN_correlativo(rs.getString(2));
            insp.setC_maquina(rs.getString(3));
            insp.setC_condicionmaquina(rs.getString(4));
            insp.setC_comentario(rs.getString(5));
            insp.setC_estado(rs.getString(6));
            insp.setD_fechaInicioInspeccion(rs.getString(7));
            insp.setD_fechaFinInspeccion(rs.getString(8));
            insp.setC_periodoinspeccion(rs.getString(9));
            insp.setC_usuarioInspeccion(rs.getString(10));
            insp.setC_usuarioenvio(rs.getString(11));
            insp.setD_fechaEnvio(rs.getString(12));
            insp.setC_ultimousuario(rs.getString(13));
            insp.setD_ultimafechamodificacion(rs.getString(14));
            result.add(insp);
            
            
        
        }
        return result;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "GetInspeccionesMaqDet")
    public ArrayList<InspeccionesMaqDet> GetInspeccionesMaqDet(@WebParam(name = "Usuario",targetNamespace = "http://SOAP/") String Usuario)  throws Exception {
        //TODO write your implementation code here:
        String concat_correlativos="";
        String selecCorrelativos = "SELECT n_correlativo FROM dbo.MTp_INSPECCIONMAQUINA_CAB WHERE c_usuarioInspeccion ='"+Usuario+"'";
        GetResultSet cresultCorrelativos = new GetResultSet();
        ResultSet rscr = cresultCorrelativos.CreateConection(selecCorrelativos);
        while (rscr.next()){
           concat_correlativos= concat_correlativos+ rscr.getString(1)+",";
        }
        concat_correlativos= concat_correlativos.substring(0, concat_correlativos.length()-1);
        ArrayList<InspeccionesMaqDet> result = new ArrayList<InspeccionesMaqDet> ();
        String query = "SELECT * FROM  dbo.MTP_INSPECCIONMAQUINA_DET where  n_correlativo in ("+concat_correlativos +")";
        GetResultSet cresult = new GetResultSet();
        ResultSet rs = cresult.CreateConection(query);
        
        while (rs.next()){
        InspeccionesMaqDet inpd = new InspeccionesMaqDet();
        
        inpd.setC_compania(rs.getString(1));
        inpd.setN_correlativo(rs.getString(2));
        inpd.setN_linea(rs.getString(3));
        inpd.setC_inpeccion(rs.getString(4));
        inpd.setC_tipoinspeccion(rs.getString(5));
        inpd.setN_porcentajeminimo(rs.getString(6));
        inpd.setN_porcentajemaximo(rs.getString(7));
        inpd.setN_pocentajeinspeccion(rs.getString(8));
        inpd.setC_estado(rs.getString(9));
        inpd.setC_comentario(rs.getString(10));
        inpd.setC_rutafoto(rs.getString(11));
        inpd.setC_ultimousuario(rs.getString(12));
        inpd.setD_ultimafechamodificacion(rs.getString(13));
        
        result.add(inpd);
        }
        
        return result;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "GetInspeccionesGenCab")
    public ArrayList<InspeccionesGenCab> GetInspeccionesGenCab(@WebParam(name = "Usuario",targetNamespace = "http://SOAP/") String Usuario) throws Exception {
        //TODO write your implementation code here:
        ArrayList<InspeccionesGenCab> result = new  ArrayList<InspeccionesGenCab>();
          String query = "SELECT * FROM  dbo.MTP_INSPECCIONGENERAL_CAB WHERE c_usuarioinspeccion = '"+Usuario+"'";
        GetResultSet cresult = new GetResultSet();
        ResultSet rs = cresult.CreateConection(query);
        while(rs.next()){
        
            InspeccionesGenCab insp = new InspeccionesGenCab();
            insp.setC_compania(rs.getString(1));
            insp.setN_correlativo(rs.getString(2));
            insp.setC_tipoinspeccion(rs.getString(3));
            insp.setC_maquina(rs.getString(4));
            insp.setC_centrocosto(rs.getString(5));
            insp.setC_comentario(rs.getString(6));
            insp.setC_usuarioinspeccion(rs.getString(7));
            insp.setD_fechainspeccion(rs.getString(8));
            insp.setC_estado(rs.getString(9));
            insp.setC_usuarioenvio(rs.getString(10));
            insp.setD_fechaenvio(rs.getString(11));
            insp.setC_ultimousuario(rs.getString(12));
            insp.setD_ultimafechamodificacion(rs.getString(13));
            result.add(insp);
            
            
        }
        return result;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "GetInspeccionesGenDet")
    public  ArrayList<InspeccionesGenDet> GetInspeccionesGenDet(@WebParam(name = "Usuario",targetNamespace = "http://SOAP/") String Usuario) throws Exception {
        //TODO write your implementation code here:
         String concat_correlativos="";
        String selecCorrelativos = "SELECT  n_correlativo FROM  dbo.MTP_INSPECCIONGENERAL_CAB WHERE c_usuarioinspeccion = '"+Usuario+"'";
        GetResultSet cresultCorrelativos = new GetResultSet();
        ResultSet rscr = cresultCorrelativos.CreateConection(selecCorrelativos);
        while (rscr.next()){
           concat_correlativos= concat_correlativos+ rscr.getString(1)+",";
        }
        concat_correlativos= concat_correlativos.substring(0, concat_correlativos.length()-1);
       ArrayList<InspeccionesGenDet> result = new ArrayList<InspeccionesGenDet> ();
        String query = "SELECT * FROM  dbo.MTP_INSPECCIONGENERAL_DET where  n_correlativo in ("+concat_correlativos +")";
        GetResultSet cresult = new GetResultSet();
        ResultSet rs = cresult.CreateConection(query);
        
        while (rs.next()){
        InspeccionesGenDet inpd = new InspeccionesGenDet();
        
        inpd.setC_compania(rs.getString(1));
        inpd.setN_correlativo(rs.getString(2));
        inpd.setN_linea(rs.getString(3));
        inpd.setC_comentario(rs.getString(4));
        inpd.setC_rutafoto(rs.getString(5));
        inpd.setC_ultimousuario(rs.getString(6));
        inpd.setD_ultimafechamodificacion(rs.getString(7));
        inpd.setC_tiporevisiong(rs.getString(8));
        inpd.setC_flagadictipo(rs.getString(9));
         
        result.add(inpd);
        }
        
        return result;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "GetUsuarios")
    public ArrayList<UsuarioDB> GetUsuarios() throws Exception {
        ArrayList<UsuarioDB> listUsers = new ArrayList<UsuarioDB> ();
        String query = "SELECT * FROM  dbo.MTP_USUARIO";
        GetResultSet cresult = new GetResultSet();
        ResultSet rs = cresult.CreateConection(query);
        while (rs.next()){
         
            UsuarioDB us = new UsuarioDB();
            us.setCodigoUsuario(rs.getString(1));
            us.setNombre(rs.getString(2));
            us.setClave(rs.getString(3));
            us.setNroPersona(rs.getString(4));
            us.setEstado(rs.getString(5));
            us.setFlagmantto(rs.getString(6));
            listUsers.add(us);
        
        }
        
        
        
        return listUsers;
    }

}