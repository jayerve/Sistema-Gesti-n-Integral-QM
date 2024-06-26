/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.controladores;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.reportes.GenerarReporte;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import paq_gestion.ejb.ServicioEmpleado;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;




@ManagedBean
@ViewScoped
public class ControladorRoles {

    private String strOpcion = "1";
    private List lisRolesPago;
    private Object rolSeleccionado;
    private Utilitario utilitario = new Utilitario();
    @EJB
    private ServicioEmpleado ser_empleado;
    private String strPathReporte;
    private String strPathReporteRoles;
    private String strPathReporteRolesConsolidado;
    private Map parametros = new HashMap();
	StringBuilder str_ide=new StringBuilder();
	StringBuilder str_ide_nrrol=new StringBuilder();
	StringBuilder str_ide_nrrol_sin_auto=new StringBuilder();
	private String ide_nrdtn_consolidado="";
int cont=0;
    private JasperPrint jasperPrint;
    Connection conn;
    @PostConstruct
    public void cargarDatos() {
       	int valor=0,valorRol=0,valorRolAutorizado=0;
        TablaGenerica tab_partida = ser_empleado.getPartidaEmpleado(utilitario.getVariable("IDE_GTEMP"));
        if (tab_partida != null) {
        	if (tab_partida.getTotalFilas()>0) {
         		for (int i = 0; i < tab_partida.getTotalFilas(); i++) 
   			 {
          	  
                str_ide.append(tab_partida.getValor(i, "IDE_GEEDP"));
               // valor++;
                if (tab_partida.getTotalFilas()==1) {
   			}else if (valor<=tab_partida.getTotalFilas()) {
   					valor++;
   					if(valor<(tab_partida.getTotalFilas())){
                    str_ide.append(",");
                    System.out.println("str_ide:  "+str_ide);
   					}
    			}
    
   			 }
         		
         	}
        	
        	boolean ver_rol=false;
        	TablaGenerica tab_area = utilitario.consultar("select * from gen_area where ide_geare in("+tab_partida.getValor("IDE_GEARE")+")");
        	TablaGenerica tabRoles=null;
        	if(tab_area.getValor("IDE_GEARE").equals("10") || tab_area.getValor("IDE_GEARE").equals("9")){
        		tabRoles=utilitario.consultar("select * from nrh_rol  rol  "
        			+ "left join gen_perido_rol gprol on gprol.ide_gepro=rol.ide_gepro "
            			+ "where gprol.tipo_rol=1  and rol.ide_nresr=1 AND GPROL.IDE_GEANI in("+utilitario.getVariable("p_anio_roles")+") "
            			//+ "and gprol.ide_geani in(12,13) "
        			//+ " and (ver_nrrol is null or ver_nrrol=false)  "
            			+ "order by rol.ide_nrrol desc ");
            			//+ "");		
        	}else{
        		tabRoles=utilitario.consultar("select * from nrh_rol  rol  "
            			+ "left join gen_perido_rol gprol on gprol.ide_gepro=rol.ide_gepro "
            			+ "where gprol.tipo_rol=1  and rol.ide_nresr=1 AND GPROL.IDE_GEANI in ("+utilitario.getVariable("p_anio_roles")+") "
            			//+ "where gprol.tipo_rol=0  and rol.ide_nresr=1 AND GPROL.IDE_GEANI in ("+utilitario.getVariable("p_anio_roles")+") AND GPROL.IDE_GEMES>=4 "

            			+ "order by rol.ide_nrrol desc");
            			
        	}
        	
        
        	if (tabRoles.getTotalFilas()>0) {
        		for (int i = 0; i < tabRoles.getTotalFilas(); i++) 
     			 {
        			 if (i==(tabRoles.getTotalFilas()-1)) {
        				 str_ide_nrrol.append(tabRoles.getValor(i, "IDE_NRROL"));
        				}else{
           				 str_ide_nrrol.append(tabRoles.getValor(i, "IDE_NRROL"));	
           				 str_ide_nrrol.append(",");
           					}
        	    			} 			
     			 }else {
    				 str_ide_nrrol.append("-1");
    				 str_ide.append("-1");
				} 
        	
       
        	
     			 
            lisRolesPago = ser_empleado.getRolesEmpleadoListaControlador(str_ide.toString(),utilitario.getVariable("IDE_GTEMP"),str_ide_nrrol.toString());
        }
        strPathReporteRoles = utilitario.getURL()+ "/reportes/reporte" + utilitario.getVariable("IDE_USUA") + ".pdf";
        //strPathReporteRoles = utilitario.getURL()+ "/reportes/reporte" + utilitario.getVariable("IDE_USUA") + "roles.pdf";
        //strPathReporteRolesConsolidado = utilitario.getURL()+ "/reportes/reporte" + utilitario.getVariable("IDE_USUA") + ".pdf";


    }

    public void visualizarRolx() {
        if (rolSeleccionado != null) {
            GenerarReporte ger = new GenerarReporte();
            
            try {
                parametros.put("IDE_GEPRO", Long.parseLong(((Object[]) rolSeleccionado)[4] + ""));
            } catch (Exception e) {
            }
            parametros.put("IDE_NRDTN", Long.parseLong(((Object[]) rolSeleccionado)[3] + ""));
            TablaGenerica tab_partida = ser_empleado.getPartida(utilitario.getVariable("IDE_GTEMP"));
            parametros.put("IDE_GEEDP", Long.parseLong(tab_partida.getValor("IDE_GEEDP")));
            parametros.put("titulo", " BOLETA DE PAGO");
            parametros.put("IDE_NRTIR", utilitario.getVariable("p_nrh_trubro_egreso") + "," + utilitario.getVariable("p_nrh_trubro_ingreso"));
        	parametros.put("par_total_recibir",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_valor_recibir")));
			parametros.put("par_total_ingresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_ingresos")));
			parametros.put("par_total_egresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_egresos")));
			ger.generar(parametros, "/reportes/rep_rol_de_pagos/rep_rol_individual_roles.jasper");
        }        
    }


/*	public void reportBuilder() throws JRException {
        try {
            Class.forName("org.postgresql.Driver");
            //conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sampu","postgres","cat2014pg");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sampu","postgres","Belkin32");
        } catch (SQLException ex) {
            //Logger.getLogger(IniciarReporte.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
           // Logger.getLogger(IniciarReporte.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    	TablaGenerica tab_roles=utilitario.consultar("select * from gen_perido_rol where ide_gepro="+Integer.parseInt(((Object[]) rolSeleccionado)[4] + "")+" and tipo_rol not in (0,4,6)");
		String report="";
		if (tab_roles.getTotalFilas()>0) {
		
        TablaGenerica tab_sucursal=utilitario.consultar("select ide_usua,ide_sucu from SIS_USUARIO_SUCURSAL where ide_usua="+utilitario.getVariable("ide_usua"));
        parametros.put("IDE_GEPRO", pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[4] + ""));
        parametros.put("IDE_NRDTN", pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[3] + ""));
        TablaGenerica tab_partida = ser_empleado.getPartida(utilitario.getVariable("IDE_GTEMP"));
        parametros.put("IDE_GEEDP", pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[6] + ""));
        parametros.put("titulo", " BOLETA DE PAGO");
        parametros.put("IDE_NRTIR", utilitario.getVariable("p_nrh_trubro_egreso") + "," + utilitario.getVariable("p_nrh_trubro_ingreso"));
    	parametros.put("par_total_recibir",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_valor_recibir")));
		parametros.put("par_total_ingresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_ingresos")));
		parametros.put("par_total_egresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_egresos")));
        parametros.put("ide_empr", Integer.valueOf(pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_empr"))));
        parametros.put("ide_sucu", Integer.valueOf(pckUtilidades.CConversion.CInt(tab_sucursal.getValor("ide_sucu"))));
        parametros.put("usuario", Integer.valueOf(pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua"))));
	    parametros.put("SUBREPORT_DIR", getURL());
        parametros.put("REPORT_CONNECTION", conn);

		System.out.println("enter a imprimir cc");

			//strPathReporte=null;
			String usuario=utilitario.getVariable("IDE_USUA");
			strPathReporteRoles = utilitario.getURL()+ "/reportes/reporte" + usuario + "roles.pdf";
	        report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/rep_rol_de_pagos/rep_rol_individual_roles.jasper");
				System.out.println("report  "+report);
				System.out.println("getURL()  "+getURL());
				System.out.println("conn  "+conn.toString());
				System.out.println("parametros  "+parametros.toString());


				//jasperPrint = JasperFillManager.fillReport(report, parametros,conn);
	            //GenerarReporte ger = new GenerarReporte();
				//ger.generar(parametros, "/reportes/rep_rol_de_pagos/rep_rol_individual_roles.jasper");
				jasperPrint = JasperFillManager.fillReport(report, parametros,conn);


				
			
		}else {
			 TablaGenerica tab_sucursal=utilitario.consultar("select ide_usua,ide_sucu from SIS_USUARIO_SUCURSAL where ide_usua="+utilitario.getVariable("ide_usua"));
		        parametros.put("IDE_GEPRO", pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[4] + ""));
		        parametros.put("IDE_NRDTN", pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[3] + ""));
		        TablaGenerica tab_partida = ser_empleado.getPartida(utilitario.getVariable("IDE_GTEMP"));
		        parametros.put("IDE_GEEDP", pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[6] + ""));
		        parametros.put("titulo", " BOLETA DE PAGO");
		        parametros.put("IDE_NRTIR", utilitario.getVariable("p_nrh_trubro_egreso") + "," + utilitario.getVariable("p_nrh_trubro_ingreso"));
		    	parametros.put("par_total_recibir",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_valor_recibir")));
				parametros.put("par_total_ingresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_ingresos")));
				parametros.put("par_total_egresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_egresos")));
		        parametros.put("ide_empr", Integer.valueOf(pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_empr"))));
		        parametros.put("ide_sucu", Integer.valueOf(pckUtilidades.CConversion.CInt(tab_sucursal.getValor("ide_sucu"))));
		        parametros.put("usuario", Integer.valueOf(pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua"))));
			    parametros.put("SUBREPORT_DIR", getURL());
		        parametros.put("REPORT_CONNECTION", conn);
			    strPathReporteRoles = utilitario.getURL()+ "/reportes/reporte" + utilitario.getVariable("IDE_USUA") + ".pdf";
			   	report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/rep_rol_de_pagos/rep_rol_individual.jasper");	
				System.out.println("report  "+report);
				System.out.println("getURL()  "+getURL());
				System.out.println("conn  "+conn.toString());
				System.out.println("parametros  "+parametros.toString());
				jasperPrint = JasperFillManager.fillReport(report, parametros,conn);
			
		}
}*/
    
    public void reportBuilder() throws JRException {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sampu","postgres","Htics@2024");
            //conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sampu","postgres","Belkin32");
        } catch (SQLException ex) {
            //Logger.getLogger(IniciarReporte.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
           // Logger.getLogger(IniciarReporte.class.getName()).log(Level.SEVERE, null, ex);
        }
		
        /*TablaGenerica tab_sucursal=utilitario.consultar("select ide_usua,ide_sucu from SIS_USUARIO_SUCURSAL where ide_usua="+utilitario.getVariable("ide_usua"));
        parametros.put("IDE_GEPRO", Long.parseLong(((Object[]) rolSeleccionado)[4] + ""));
        parametros.put("IDE_NRDTN", Long.parseLong(((Object[]) rolSeleccionado)[3] + ""));
        TablaGenerica tab_partida = ser_empleado.getPartida(utilitario.getVariable("IDE_GTEMP"));
        parametros.put("IDE_GEEDP", Long.parseLong(((Object[]) rolSeleccionado)[6] + ""));
        parametros.put("titulo", " BOLETA DE PAGO");
        parametros.put("IDE_NRTIR", utilitario.getVariable("p_nrh_trubro_egreso") + "," + utilitario.getVariable("p_nrh_trubro_ingreso"));
    	parametros.put("par_total_recibir",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_valor_recibir")));
		parametros.put("par_total_ingresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_ingresos")));
		parametros.put("par_total_egresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_egresos")));
        parametros.put("ide_empr", Integer.valueOf(pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_empr"))));
        parametros.put("ide_sucu", Integer.valueOf(pckUtilidades.CConversion.CInt(tab_sucursal.getValor("ide_sucu"))));
        parametros.put("usuario", Integer.valueOf(pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua"))));
	//    parametros.put("SUBREPORT_DIR", x);
        parametros.put("REPORT_CONNECTION", conn);
*/
	
	/* TablaGenerica tab_sucursal=utilitario.consultar("select ide_usua,ide_sucu from SIS_USUARIO_SUCURSAL where ide_usua="+utilitario.getVariable("ide_usua"));
	        parametros.put("IDE_GEPRO", pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[4] + ""));
	        parametros.put("IDE_NRDTN", pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[3] + ""));
	        TablaGenerica tab_partida = ser_empleado.getPartida(utilitario.getVariable("IDE_GTEMP"));
	        parametros.put("IDE_GEEDP", pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[6] + ""));
	        parametros.put("titulo", " BOLETA DE PAGO");
	        parametros.put("IDE_NRTIR", utilitario.getVariable("p_nrh_trubro_egreso") + "," + utilitario.getVariable("p_nrh_trubro_ingreso"));
	    	parametros.put("par_total_recibir",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_valor_recibir")));
			parametros.put("par_total_ingresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_ingresos")));
			parametros.put("par_total_egresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_egresos")));
	        parametros.put("ide_empr", Integer.valueOf(pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_empr"))));
	        parametros.put("ide_sucu", Integer.valueOf(pckUtilidades.CConversion.CInt(tab_sucursal.getValor("ide_sucu"))));
	        parametros.put("usuario", Integer.valueOf(pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua"))));
		    parametros.put("SUBREPORT_DIR", getURL());
	        parametros.put("REPORT_CONNECTION", conn);
       
        System.out.println("enter a imprimir cc");
	*/
        
        
        
        String ide_gttem="";
        TablaGenerica tab_partida1 = ser_empleado.getPartidaTipoRoles(((Object[]) rolSeleccionado)[6].toString());
        //tab_partida1.getValor("");
    
        TablaGenerica tab_periodo=utilitario.consultar("select * from gen_perido_rol where ide_gepro="+pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[4] + ""));
        String  ide_gepro_consolidado=""+pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[4] + "");
        double valorDecimoCuarto,valorDecimoTercero=0.0,valorFondosReserva=0.0,valorIessPersonal=0.0,valorIessPatronal=0.0,valorIessPersonalBase=0.0,
        		nro_horas_suplementarias_50=0.0;
        int mes =0,mes_aux=0,anio=0,anio_aux=0;
        String fecha_inicial_gepro="",ide_gepro="",ide_nrdtn="",ide_gepro_extra="",ide_nrdtn_extra="",ide_gepro_fondos="",ide_nrdtn_fondos="",ide_gepro_normal="",ide_nrdtn_normal="",ide_nrdtn_consolidado="",ide_gepro_alimentacion="";
        mes=pckUtilidades.CConversion.CInt(tab_periodo.getValor("ide_gemes"));
        mes_aux=pckUtilidades.CConversion.CInt(tab_periodo.getValor("ide_gemes"));

        anio_aux=utilitario.getAnio(tab_periodo.getValor("fecha_inicial_gepro"));
       
        //Valido el mes de rol
        //if (mes_aux==12) {
        	//si es 12 le sumo uno
		//	mes=1;
			//al anio le sumo uno
		 // anio=anio_aux+1;
        //}else {
		//	mes=mes+1;
		//	anio=anio_aux;
	//	}
        //Consulto el nuevo a�o si existe un cambio       
        //TablaGenerica tab_anio =utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anio+"%'");
        //Asigno el valor del anio
        
        anio=pckUtilidades.CConversion.CInt(tab_periodo.getValor("ide_geani"));
        
        ide_gepro=ide_gepro_consolidado+",";
        ide_gepro_normal=ide_gepro_consolidado;
        
                
        //Rol tipo HORAS EXTRA
        TablaGenerica tab_periodo_roles=utilitario.consultar("select ide_gepro,ide_gemes from gen_perido_rol "
       // + "where ide_geani="+anio+"  and ide_gemes="+mes+" and tipo_rol in(9) order by tipo_rol desc ");
 		+ "where ide_geani="+anio+"  and ide_gemes="+mes_aux+" and tipo_rol in(9,12,13) order by tipo_rol desc ");
        
      //Rol tipo FONDOS DE RESERVA
        TablaGenerica tab_anio_fondos =utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anio_aux+"%'");
        TablaGenerica tab_fondos_reserva=utilitario.consultar("select ide_gepro,ide_gemes from gen_perido_rol "
        		+ "where ide_geani="+anio+"  and ide_gemes="+mes_aux+" and tipo_rol in(7) order by tipo_rol desc ");
        
        TablaGenerica tab_alimentacion=utilitario.consultar("select ide_gepro,ide_gemes from gen_perido_rol "
        		//+ "where ide_geani="+anio+"  and ide_gemes="+mes_aux+" and tipo_rol in(11) order by tipo_rol desc ");
        		+ "where ide_geani="+anio+"  and ide_gemes="+mes_aux+" and tipo_rol in(11) order by tipo_rol desc ");
        
        
        
        
		//ide_gepro="";
		boolean estado=false;
		
		if (mes_aux==8 && anio_aux==2019) {
			        		ide_gepro+=tab_fondos_reserva.getValor("IDE_GEPRO");	
		}else if(mes_aux<8 && anio_aux<=2019){ 
		ide_gepro=ide_gepro_consolidado;
		}else if(anio_aux<2019){
		}else{
			if (tab_periodo_roles.getValor("IDE_GEPRO")==null || tab_periodo_roles.getValor("IDE_GEPRO").equals("") || tab_periodo_roles.getValor("IDE_GEPRO").isEmpty()) {				     
			//Si no contiene un periodo de horas extra
			}else{
				for (int i = 0; i < tab_periodo_roles.getTotalFilas(); i++) {
					if(i==(tab_periodo_roles.getTotalFilas()-1)) {
						ide_gepro+=tab_periodo_roles.getValor(i,"IDE_GEPRO")+",";			
					}else{
					ide_gepro+=tab_periodo_roles.getValor(i,"IDE_GEPRO")+",";			

				}
				}
				//Si contiene horas extra le a�ade
			}
			
			
			if (tab_fondos_reserva.getValor("IDE_GEPRO")==null || tab_fondos_reserva.getValor("IDE_GEPRO").equals("") || tab_fondos_reserva.getValor("IDE_GEPRO").isEmpty()) {				     
    
				} else {
		   		ide_gepro_fondos=tab_fondos_reserva.getValor("IDE_GEPRO");
		   		ide_gepro+=ide_gepro_fondos;   
					}
			
			if (tab_alimentacion.getValor("IDE_GEPRO")==null || tab_alimentacion.getValor("IDE_GEPRO").equals("") || tab_alimentacion.getValor("IDE_GEPRO").isEmpty()) {				     
				
			} else {
				char ultimo_caracter;
				ultimo_caracter = ide_gepro.charAt(ide_gepro.length()-1);
				if (Character.toString(ultimo_caracter).equals(",")) {
					ide_gepro=ide_gepro.substring(0, ide_gepro.length() - 1);
				}else {
					
				}
				ide_gepro_alimentacion=tab_alimentacion.getValor("IDE_GEPRO");
				ide_gepro+=","+ide_gepro_alimentacion;   
				}
			
			
			

			char ultimo_caracter;
					ultimo_caracter = ide_gepro.charAt(ide_gepro.length()-1);
					if (Character.toString(ultimo_caracter).equals(",")) {
						ide_gepro=ide_gepro.substring(0, ide_gepro.length() - 1);
					}else {
						
					}
		}
        
        
        
		//---------------------------------------------------------------------------------------------------------
		ide_nrdtn_normal=""+pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[3] + "");
        
		ide_nrdtn=ide_nrdtn_normal+",";
        
		
        TablaGenerica tab_detalle_tipo_nomina=utilitario.consultar("SELECT ide_nrdtn, ide_nrtin, ide_gttem, ide_gttco, ide_sucu, ide_nrtit, "
        		+ "activo_nrdtn  "
        		+ "FROM nrh_detalle_tipo_nomina  "
		        		+ "where ide_nrtin in(7,9,11,12,13) and  "
        		+ "ide_gttem in(SELECT ide_gttem FROM nrh_detalle_tipo_nomina where ide_nrdtn in("+ide_nrdtn_normal+"))"
        				+ " order by ide_nrdtn desc");
        
        					        
        
        
        if (mes_aux==8 && anio_aux==2019) {
        for (int i = 0; i < tab_detalle_tipo_nomina.getTotalFilas(); i++) {
        	if (i==(tab_detalle_tipo_nomina.getTotalFilas()-1)) {
        		ide_nrdtn+=tab_detalle_tipo_nomina.getValor(i,"IDE_NRDTN");
        		ide_nrdtn_fondos=tab_detalle_tipo_nomina.getValor(i,"IDE_NRDTN");
			}else{
	      //  	if (i==0) {

			//	ide_nrdtn+=tab_detalle_tipo_nomina.getValor(0,"IDE_NRDTN")+",";}else{}
				//ide_nrdtn_extra=tab_detalle_tipo_nomina.getValor(i,"IDE_NRDTN");
        	}
		}
        
				}else if(mes_aux<8 && anio_aux<=2019){ 
					ide_nrdtn=ide_nrdtn_normal;	        
		}else if(anio_aux<2019){
					ide_nrdtn=ide_nrdtn_normal;
		}else{
	        for (int i = 0; i < tab_detalle_tipo_nomina.getTotalFilas(); i++) {
	        	if (i==(tab_detalle_tipo_nomina.getTotalFilas()-1)) {
	        		ide_nrdtn+=tab_detalle_tipo_nomina.getValor(i,"IDE_NRDTN");
	        		ide_nrdtn_fondos=tab_detalle_tipo_nomina.getValor(i,"IDE_NRDTN");
				}else{										
					ide_nrdtn+=tab_detalle_tipo_nomina.getValor(i,"IDE_NRDTN")+",";
					ide_nrdtn_extra=tab_detalle_tipo_nomina.getValor(i,"IDE_NRDTN");
	        	}
			}					        	
        	
        	
        }
        
        if (tab_detalle_tipo_nomina.getTotalFilas()==0) {
        	ide_nrdtn=""+pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[3] + "");

		}
        
				TablaGenerica tab_rol=utilitario.consultar("SELECT ide_geedp, ide_gtemp from "
						+ "gen_empleados_departamento_par  where ide_gtemp in("+utilitario.getVariable("IDE_GTEMP")+") "
								+ "order by ide_geedp asc ");
				String ide_geedp="";
				
			     int ide_geedp_nomina=0;
			     
				for (int i = 0; i < tab_rol.getTotalFilas(); i++) {
						if (tab_rol.getValor(i,"IDE_GEEDP").equals((((Object[]) rolSeleccionado)[6] + "").toString())) {
							if (i==(tab_rol.getTotalFilas()-1)) {
								
							}else{
							ide_geedp+=tab_rol.getValor(i,"IDE_GEEDP");
							i=tab_rol.getTotalFilas();
							break;}
						}
						
				   	if (i==(tab_rol.getTotalFilas()-1)) {
		        		ide_geedp+=tab_rol.getValor(i,"IDE_GEEDP");
		        		}else{										
						ide_geedp+=tab_rol.getValor(i,"IDE_GEEDP")+",";
		        	}	
				}

				/*for (int i = 0; i < tab_rol.getTotalFilas(); i++) {
				   	if (i==(tab_rol.getTotalFilas()-1)) {
		        		ide_geedp+=tab_rol.getValor(i,"IDE_GEEDP");
		        		}else{										
						ide_geedp+=tab_rol.getValor(i,"IDE_GEEDP")+",";
		        	}	
				}*/
				  System.out.println("IDE_GEEDP"+ide_geedp);

        
        
        
        TablaGenerica tab_sucursal=utilitario.consultar("select ide_usua,ide_sucu from SIS_USUARIO_SUCURSAL where ide_usua="+utilitario.getVariable("ide_usua"));
        //parametros.put("IDE_GEPRO", pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[4] + ""));
        
       //parametros.put("IDE_GEPRO", Long.parseLong(((Object[]) rolSeleccionado)[4] + ""));
        
        parametros.put("IDE_GEPRO", ide_gepro);
        parametros.put("IDE_NRDTN", ide_nrdtn);
        parametros.put("IDE_GEEDP", ide_geedp);
        parametros.put("titulo", " BOLETA DE PAGO");
        parametros.put("IDE_NRTIR", "0,1,2,3");
        parametros.put("fecha_desde", tab_periodo.getValor("fecha_inicial_gepro"));
        parametros.put("fecha_hasta", tab_periodo.getValor("fecha_final_gepro"));
      	parametros.put("etiqueta_rol",utilitario.getVariable("p_nrh_rubro_etiqueta_rol"));
        TablaGenerica tab_mes1=utilitario.consultar("select * from gen_mes where ide_gemes="+utilitario.getMes(tab_periodo.getValor("fecha_final_gepro")));
        parametros.put("mes", tab_mes1.getValor("detalle_gemes"));
		parametros.put("ide_empr", Integer.valueOf(pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_empr"))));
        parametros.put("ide_sucu", Integer.valueOf(pckUtilidades.CConversion.CInt(tab_sucursal.getValor("ide_sucu"))));
        parametros.put("usuario", Integer.valueOf(pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua"))));
	    parametros.put("SUBREPORT_DIR", getURL());
        parametros.put("REPORT_CONNECTION", conn);
		String report="";
		report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/rep_rol_de_pagos//rep_rol_individual_consolidado_cambio_tot.jasper");
		System.out.println("report  "+report);
		System.out.println("getURL()  "+getURL());
		System.out.println("conn  "+conn.toString());
		System.out.println("parametros  "+parametros.toString());
		jasperPrint = JasperFillManager.fillReport(report, parametros,conn);
		
		
	}
    
    
    public void reportBuilder2() throws JRException {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sampu","postgres","Htics@2024");
        } catch (SQLException ex) {
            //Logger.getLogger(IniciarReporte.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
           // Logger.getLogger(IniciarReporte.class.getName()).log(Level.SEVERE, null, ex);
        }
		
        /*TablaGenerica tab_sucursal=utilitario.consultar("select ide_usua,ide_sucu from SIS_USUARIO_SUCURSAL where ide_usua="+utilitario.getVariable("ide_usua"));
        parametros.put("IDE_GEPRO", Long.parseLong(((Object[]) rolSeleccionado)[4] + ""));
        parametros.put("IDE_NRDTN", Long.parseLong(((Object[]) rolSeleccionado)[3] + ""));
        TablaGenerica tab_partida = ser_empleado.getPartida(utilitario.getVariable("IDE_GTEMP"));
        parametros.put("IDE_GEEDP", Long.parseLong(((Object[]) rolSeleccionado)[6] + ""));
        parametros.put("titulo", " BOLETA DE PAGO");
        parametros.put("IDE_NRTIR", utilitario.getVariable("p_nrh_trubro_egreso") + "," + utilitario.getVariable("p_nrh_trubro_ingreso"));
    	parametros.put("par_total_recibir",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_valor_recibir")));
		parametros.put("par_total_ingresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_ingresos")));
		parametros.put("par_total_egresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_egresos")));
        parametros.put("ide_empr", Integer.valueOf(pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_empr"))));
        parametros.put("ide_sucu", Integer.valueOf(pckUtilidades.CConversion.CInt(tab_sucursal.getValor("ide_sucu"))));
        parametros.put("usuario", Integer.valueOf(pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua"))));
	//    parametros.put("SUBREPORT_DIR", x);
        parametros.put("REPORT_CONNECTION", conn);
*/
	
	/* TablaGenerica tab_sucursal=utilitario.consultar("select ide_usua,ide_sucu from SIS_USUARIO_SUCURSAL where ide_usua="+utilitario.getVariable("ide_usua"));
	        parametros.put("IDE_GEPRO", pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[4] + ""));
	        parametros.put("IDE_NRDTN", pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[3] + ""));
	        TablaGenerica tab_partida = ser_empleado.getPartida(utilitario.getVariable("IDE_GTEMP"));
	        parametros.put("IDE_GEEDP", pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[6] + ""));
	        parametros.put("titulo", " BOLETA DE PAGO");
	        parametros.put("IDE_NRTIR", utilitario.getVariable("p_nrh_trubro_egreso") + "," + utilitario.getVariable("p_nrh_trubro_ingreso"));
	    	parametros.put("par_total_recibir",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_valor_recibir")));
			parametros.put("par_total_ingresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_ingresos")));
			parametros.put("par_total_egresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_egresos")));
	        parametros.put("ide_empr", Integer.valueOf(pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_empr"))));
	        parametros.put("ide_sucu", Integer.valueOf(pckUtilidades.CConversion.CInt(tab_sucursal.getValor("ide_sucu"))));
	        parametros.put("usuario", Integer.valueOf(pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua"))));
	    parametros.put("SUBREPORT_DIR", getURL());
        parametros.put("REPORT_CONNECTION", conn);

		System.out.println("enter a imprimir cc");
	*/
        
        
        
        String ide_gttem="";
        TablaGenerica tab_partida1 = ser_empleado.getPartidaTipoRoles(((Object[]) rolSeleccionado)[6].toString());
        tab_partida1.getValor("");
    
        TablaGenerica tab_periodo=utilitario.consultar("select * from gen_perido_rol where ide_gepro="+pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[4] + ""));
        double valorDecimoCuarto,valorDecimoTercero=0.0,valorFondosReserva=0.0,valorIessPersonal=0.0,valorIessPatronal=0.0,valorIessPersonalBase=0.0,
        		nro_horas_suplementarias_50=0.0;
        int mes =0,mes_aux=0,anio=0,anio_aux=0;
        String fecha_inicial_gepro="",ide_gepro="",ide_nrdtn="",ide_gepro_extra="",ide_nrdtn_extra="",ide_gepro_fondos="",ide_nrdtn_fondos="",ide_gepro_normal="",ide_nrdtn_normal="";
        mes=pckUtilidades.CConversion.CInt(tab_periodo.getValor("ide_gemes"));
        anio_aux=utilitario.getAnio(tab_periodo.getValor("fecha_inicial_gepro"));
       
        if (mes==12) {
			mes=1;
		  anio=anio_aux+1;
        }else {
			mes=mes+1;
			anio=anio_aux;
		}
               
        TablaGenerica tab_anio =utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+anio+"%'");
        anio=pckUtilidades.CConversion.CInt(tab_periodo.getValor("ide_geani"));
        
        ide_gepro=((Object[]) rolSeleccionado)[4].toString()+",";
        ide_gepro_normal=((Object[]) rolSeleccionado)[4].toString();
        
        TablaGenerica tab_periodo_roles=utilitario.consultar("select ide_gepro,ide_gemes from gen_perido_rol "
        		+ "where ide_geani="+anio+"  and ide_gemes="+mes+" and tipo_rol in(7,9) order by tipo_rol desc ");
        
       
        /**
         * Valor ide_gepro de horas_extas
         */
        for (int i = 0; i < tab_periodo_roles.getTotalFilas(); i++) {
        	if (i==(tab_periodo_roles.getTotalFilas()-1)) {
        		ide_gepro+=tab_periodo_roles.getValor(i,"IDE_GEPRO");	
        		ide_gepro_fondos=tab_periodo_roles.getValor(i,"IDE_GEPRO");
			}else{
			ide_gepro+=tab_periodo_roles.getValor(i,"IDE_GEPRO")+",";
			ide_gepro_extra=tab_periodo_roles.getValor(i,"IDE_GEPRO");
			}
		}
//---------------------------------------------------------------------------------------------------------
        ide_nrdtn=((Object[]) rolSeleccionado)[3].toString()+",";
        ide_nrdtn_normal=((Object[]) rolSeleccionado)[3].toString();
        TablaGenerica tab_detalle_tipo_nomina=utilitario.consultar("SELECT ide_nrdtn, ide_nrtin, ide_gttem, ide_gttco, ide_sucu, ide_nrtit, "
        		+ "activo_nrdtn  "
        		+ "FROM nrh_detalle_tipo_nomina  "
        		+ "where ide_nrtin in(7,9) and  "
        		+ "ide_gttem in(SELECT ide_gttem FROM nrh_detalle_tipo_nomina where ide_nrdtn in("+pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[3] + "")+"))"
        				+ " order by ide_nrdtn desc");
        
        
        for (int i = 0; i < tab_detalle_tipo_nomina.getTotalFilas(); i++) {
        	if (i==(tab_detalle_tipo_nomina.getTotalFilas()-1)) {
        		ide_nrdtn+=tab_detalle_tipo_nomina.getValor(i,"IDE_NRDTN");
        		ide_nrdtn_fondos=tab_detalle_tipo_nomina.getValor(i,"IDE_NRDTN");
			}else{
				ide_nrdtn+=tab_detalle_tipo_nomina.getValor(i,"IDE_NRDTN")+",";
				ide_nrdtn_extra=tab_detalle_tipo_nomina.getValor(i,"IDE_NRDTN");
        	}
		}
        
//--------------------------------------------------------------------------------------------------------------------------------------------------

        
        //NORMAL
       /**
        * 46  acumula fondos de reserva
		  288  remuneracion
		  330  acumula decimo
        */
        double acumula_fondos=0.0,acumula_decimo=0.0,remuneracion=0.0,decimo_tercer=0.0,impuesto_a_la_renta=0.0,valor_h_extra=0.0,
        		valor_fondos_reserva=0.0,anticipo_rmu=0.0,prestamo_quirografaria=0.0,prestamo_hipotecario=0.0,dias_imp=0.0,fondos_reserva_base=0.0,comite_empresa=0.0;
        TablaGenerica tab_nomina_normal=utilitario.consultar("select * from nrh_detalle_rol  drol  "
        		+ "left join nrh_rol rol on rol.ide_nrrol=drol.ide_nrrol "
        		+ "left join nrh_detalle_rubro drub on drub.ide_nrder=drol.ide_nrder "
        		+ "left join nrh_rubro rub on rub.ide_nrrub=drub.ide_nrrub "
        		+ "where "
        		+ "drol.ide_geedp in("+pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[6] + "")+")  and drub.ide_nrdtn in("+pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[3] + "")+") "
        		+ "and rol.ide_gepro in("+ide_gepro_normal+") and rub.ide_nrrub in(330,46,288,42,216,215,38,274,375)");
       
     /**
      * OBTENGO LOS TRES RUBROS NECESARIOS
      */
        if (tab_nomina_normal.getTotalFilas()>0) {
        	for (int j = 0; j < tab_nomina_normal.getTotalFilas(); j++) {
			if (tab_nomina_normal.getValor(j,"IDE_NRRUB").equals("46")) {
				acumula_fondos=Double.parseDouble(tab_nomina_normal.getValor(j,"VALOR_NRDRO"));
			}else if (tab_nomina_normal.getValor(j,"IDE_NRRUB").equals("288")) {
				remuneracion=Double.parseDouble(tab_nomina_normal.getValor(j,"VALOR_NRDRO"));
			}else if (tab_nomina_normal.getValor(j,"IDE_NRRUB").equals("330")){
				acumula_decimo=Double.parseDouble(tab_nomina_normal.getValor(j,"VALOR_NRDRO"));
			}else if (tab_nomina_normal.getValor(j,"IDE_NRRUB").equals("38")){
				anticipo_rmu=Double.parseDouble(tab_nomina_normal.getValor(j,"VALOR_NRDRO"));
			}else if (tab_nomina_normal.getValor(j,"IDE_NRRUB").equals("216")){
				prestamo_quirografaria=Double.parseDouble(tab_nomina_normal.getValor(j,"VALOR_NRDRO"));
			}else if (tab_nomina_normal.getValor(j,"IDE_NRRUB").equals("215")){
				prestamo_hipotecario=Double.parseDouble(tab_nomina_normal.getValor(j,"VALOR_NRDRO"));
			}else if (tab_nomina_normal.getValor(j,"IDE_NRRUB").equals("42")){
				impuesto_a_la_renta=Double.parseDouble(tab_nomina_normal.getValor(j,"VALOR_NRDRO"));
			}else if (tab_nomina_normal.getValor(j,"IDE_NRRUB").equals("274")){
				dias_imp=Double.parseDouble(tab_nomina_normal.getValor(j,"VALOR_NRDRO"));
			}else if (tab_nomina_normal.getValor(j,"IDE_NRRUB").equals("375")){
			 comite_empresa=Double.parseDouble(tab_nomina_normal.getValor(j,"VALOR_NRDRO"));
			}
			else {
				
			}
		}
        }
        
        
        valorIessPersonalBase=Double.parseDouble(utilitario.getFormatoNumero((Double.parseDouble(tab_partida1.getValor("RMU_GEEDP"))*0.1145),2));
       
     //INGRESOS
        //Horas extra
        TablaGenerica tab_horas_extra=utilitario.consultar("select * from nrh_detalle_rol  drol  "
        		+ "left join nrh_rol rol on rol.ide_nrrol=drol.ide_nrrol "
        		+ "left join nrh_detalle_rubro drub on drub.ide_nrder=drol.ide_nrder "
        		+ "left join nrh_rubro rub on rub.ide_nrrub=drub.ide_nrrub "
        		+ "where "
        		+ "drol.ide_geedp in("+pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[6] + "")+")  and drub.ide_nrdtn in("+ide_nrdtn_extra+") "
        		+ "and rol.ide_gepro in("+ide_gepro_extra+") and rub.ide_nrrub in(131,44,245)");
        
        double iess_personal_extra=0;
        if (tab_horas_extra.getTotalFilas()>0) {
        	for (int j = 0; j < tab_horas_extra.getTotalFilas(); j++) {
			if (tab_horas_extra.getValor(j,"IDE_NRRUB").equals("131")) {
				valor_h_extra=Double.parseDouble(tab_horas_extra.getValor(j,"VALOR_NRDRO"));
			}else 
			if (tab_horas_extra.getValor(j,"IDE_NRRUB").equals("44")) {
				iess_personal_extra=Double.parseDouble(tab_horas_extra.getValor(j,"VALOR_NRDRO"));
			}else 
		    if (tab_horas_extra.getValor(j,"IDE_NRRUB").equals("245")) {
					nro_horas_suplementarias_50=Double.parseDouble(tab_horas_extra.getValor(j,"VALOR_NRDRO"));
				}
			else {
     		}
        	}
        	}
        
        
        
        
        
//FONDOS DE RESERVA
        TablaGenerica tab_fondos=utilitario.consultar("select drol.ide_geedp,sum(drol.valor_nrdro) as valor_nrdro   "
        		+ "from nrh_detalle_rol  drol  "
        		+ "left join nrh_rol rol on rol.ide_nrrol=drol.ide_nrrol "
        		+ "left join nrh_detalle_rubro drub on drub.ide_nrder=drol.ide_nrder "
        		+ "left join nrh_rubro rub on rub.ide_nrrub=drub.ide_nrrub "
        		+ "where "
        		+ "drol.ide_geedp in("+pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[6] + "")+")  and drub.ide_nrdtn in("+ide_nrdtn_fondos+") "
        		+ "and rol.ide_gepro in("+ide_gepro_fondos+") and rub.ide_nrrub in(29,136) "
        		+ "group by drol.ide_geedp");
        
  
        if (tab_fondos.getValor("valor_nrdro")==null || tab_fondos.getValor("valor_nrdro").isEmpty() || tab_fondos.getValor("valor_nrdro").equals("") ) {
			valor_fondos_reserva=0.0;
		}else{
        valor_fondos_reserva=Double.parseDouble(tab_fondos.getValor("valor_nrdro"));
		}
        

        
        	  
 
 
 
 ///DECIMO TERCER Y DECIMO CUARTO SUELDO
 TablaGenerica tab_decimo_cuarto=utilitario.consultar("select drol.ide_geedp,sum(drol.valor_nrdro) as valor_nrdro from nrh_detalle_rol  drol   "
        	  		+ "left join nrh_rol rol on rol.ide_nrrol=drol.ide_nrrol   "
        	  		+ "left join nrh_detalle_rubro drub on drub.ide_nrder=drol.ide_nrder   "
        	  		+ "left join nrh_rubro rub on rub.ide_nrrub=drub.ide_nrrub   "
        	  		+ "where   "
        	  		+ "drol.ide_geedp in("+pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[6] + "")+")  and drub.ide_nrdtn in("+pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[3] + "")+")   "
        	  		+ "and rol.ide_gepro in("+ide_gepro_normal+") and rub.ide_nrrub in(333,121) "
        	  		+ "group by drol.ide_geedp ");
        	 
        	  Double valorDecimoT=0.0;
        	  valorIessPersonal=Double.parseDouble(utilitario.getFormatoNumero((remuneracion*0.1145),2));
        	  valorDecimoCuarto=Double.parseDouble(tab_decimo_cuarto.getValor("valor_nrdro"));

        	  //valorIessPatronal=remuneracion*0.*0.1115;
  
        	  //----------------------------------------------------------------------------------
        	  
        	  
        	  
        	  ///DECIMO TERCER Y DECIMO CUARTO SUELDO
        	  TablaGenerica tab_decimo_tercero=utilitario.consultar("select drol.ide_geedp,sum(drol.valor_nrdro) as valor_nrdro from nrh_detalle_rol  drol   "
        	         	  		+ "left join nrh_rol rol on rol.ide_nrrol=drol.ide_nrrol   "
        	         	  		+ "left join nrh_detalle_rubro drub on drub.ide_nrder=drol.ide_nrder   "
        	         	  		+ "left join nrh_rubro rub on rub.ide_nrrub=drub.ide_nrrub   "
        	         	  		+ "where   "
        	         	  		+ "drol.ide_geedp in("+pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[6] + "")+")  and drub.ide_nrdtn in("+ide_nrdtn_extra+")   "
        	         	  		+ "and rol.ide_gepro in("+ide_gepro_extra+") and rub.ide_nrrub in(334) "
        	         	  		+ "group by drol.ide_geedp ");
        	  
        	  
        	  Double valorDecimoTExtra=0.0;
        	  valorDecimoTExtra=Double.parseDouble(tab_decimo_tercero.getValor("valor_nrdro"));
        	  
        	  
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        Double sub_total_ingresos=0.0,sub_total_egresos=0.0,fondos_de_reserva=0.0,fondos_de_reserva_egres0=0.0;
              
        	  if ((int)acumula_fondos==1) {
        		  sub_total_ingresos=remuneracion+valor_fondos_reserva+valor_h_extra;
        		  parametros.put("fondos_reserva", valor_fondos_reserva);
      	         parametros.put("fondos_reserva_egresos", valor_fondos_reserva);

        		  sub_total_egresos=Double.parseDouble(utilitario.getFormatoNumero((valorIessPersonal+valor_fondos_reserva+prestamo_hipotecario+prestamo_quirografaria+anticipo_rmu+iess_personal_extra+comite_empresa),2));
        	 		}else {
        	 			parametros.put("fondos_reserva",0.0);
        	 			parametros.put("fondos_reserva_egresos", valor_fondos_reserva);
        	 		sub_total_ingresos=Double.parseDouble(utilitario.getFormatoNumero((remuneracion+valor_fondos_reserva+valor_h_extra),2));
              		 sub_total_egresos=Double.parseDouble(utilitario.getFormatoNumero((valorIessPersonal+prestamo_hipotecario+prestamo_quirografaria+anticipo_rmu+iess_personal_extra+comite_empresa),2));
              		  }
        	  
           	  if ((int)acumula_decimo==1) {
           	  //sub_total_ingresos=sub_total_ingresos+valorDecimoT+valorDecimoCuarto;
       		  //sub_total_egresos=sub_total_egresos+valorDecimoT+valorDecimoCuarto+iess_personal_extra;
           		 double decimoT=0.0; 
           		 
           		decimoT=Double.parseDouble(utilitario.getFormatoNumero((((remuneracion/12)/30)*dias_imp),2));
            	 	 
           			parametros.put("decimo13", 0.0);
	           		parametros.put("provision13",decimoT);
	           		parametros.put("provision14",valorDecimoCuarto);
				
  	 		}else {
  	 			double decimoT=0.0; 
          		 
           		decimoT=Double.parseDouble(utilitario.getFormatoNumero((((remuneracion/12)/30)*dias_imp),2));
            	 	 
           			parametros.put("provision13",decimoT);
	           		parametros.put("decimo13", decimoT);
	           		parametros.put("provision14",0.0);

  	 		 sub_total_ingresos=sub_total_ingresos+decimoT+valorDecimoCuarto+valorDecimoTExtra;
   	 	 		}


        	  
        	  
        	  
        	  
        TablaGenerica tab_sucursal=utilitario.consultar("select ide_usua,ide_sucu from SIS_USUARIO_SUCURSAL where ide_usua="+utilitario.getVariable("ide_usua"));
        //parametros.put("IDE_GEPRO", pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[4] + ""));
        
       //parametros.put("IDE_GEPRO", Long.parseLong(((Object[]) rolSeleccionado)[4] + ""));
        
        parametros.put("IDE_GEPRO", ide_gepro);
        parametros.put("IDE_NRDTN", ide_nrdtn);
        parametros.put("IDE_GEEDP", pckUtilidades.CConversion.CInt(((Object[]) rolSeleccionado)[6] + ""));
        parametros.put("titulo", " BOLETA DE PAGO");
        parametros.put("IDE_NRTIR", utilitario.getVariable("p_nrh_trubro_egreso") + "," + utilitario.getVariable("p_nrh_trubro_ingreso"));

        parametros.put("IDE_NRTIR_INGRESOS", "0,3");
        parametros.put("IDE_NRTIR_EGRESOS", "1,2");
        parametros.put("iess_personal_base", valorIessPersonal);

        parametros.put("rmu",pckUtilidades.CConversion.CDbl_2(tab_partida1.getValor("RMU_GEEDP")));

        
        parametros.put("ide_gepro_normal",((Object[]) rolSeleccionado)[4].toString());
        parametros.put("ide_gepro_horas_extra", ide_gepro_extra);
        parametros.put("ide_gepro_fondos_reserva",ide_gepro_fondos);
        
		//parametros.put("par_total_ingresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_ingresos")));
		//parametros.put("par_total_egresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_egresos")));
        
		parametros.put("par_total",pckUtilidades.CConversion.CDbl_2(utilitario.getFormatoNumero((sub_total_ingresos-sub_total_egresos),2)));
        parametros.put("fecha_desde", tab_periodo.getValor("fecha_inicial_gepro"));
        parametros.put("fecha_hasta", tab_periodo.getValor("fecha_final_gepro"));
        TablaGenerica tab_mes=utilitario.consultar("select * from gen_mes where ide_gemes="+utilitario.getMes(tab_periodo.getValor("fecha_final_gepro")));
        parametros.put("mes", tab_mes.getValor("detalle_gemes"));


        parametros.put("IDE_GEPRO", ide_gepro);

		
		
		parametros.put("ide_empr", Integer.valueOf(pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_empr"))));
        parametros.put("ide_sucu", Integer.valueOf(pckUtilidades.CConversion.CInt(tab_sucursal.getValor("ide_sucu"))));
        parametros.put("usuario", Integer.valueOf(pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua"))));
	    parametros.put("SUBREPORT_DIR", getURL());
        parametros.put("REPORT_CONNECTION", conn);
   
    System.out.println("enter a imprimir cc");

        
        
        
        
        
        
        
        
        
		
		//TablaGenerica tab_roles=utilitario.consultar("select * from gen_perido_rol where ide_gepro="+Integer.parseInt(((Object[]) rolSeleccionado)[4] + "")+" and tipo_rol not in (0,4,6)");
		String report="";
		//if (tab_roles.getTotalFilas()>0) {
			 //report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/rep_rol_de_pagos/rep_rol_individual_roles.jasper");
		report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/rep_rol_de_pagos/rep_rol_individual_consolidado.jasper");
		//}else {
		  //   report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/rep_rol_de_pagos/rep_rol_individual.jasper");	
		//}
		System.out.println("report  "+report);
		System.out.println("getURL()  "+getURL());
		System.out.println("conn  "+conn.toString());
		System.out.println("parametros  "+parametros.toString());


		jasperPrint = JasperFillManager.fillReport(report, parametros,conn);
	}


	public void visualizarRol() throws JRException,IOException {

		 try
		    {
		      FacesContext fc = FacesContext.getCurrentInstance();
		      ExternalContext ec = fc.getExternalContext();
		      //Reporte llama a este metodo
				reportBuilder();
				System.out.println("enter a imprimir veamos que pasa");
		      JRExporter exporter = new JRPdfExporter();
		      exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		      
		  	//TablaGenerica tab_roles=utilitario.consultar("select * from gen_perido_rol where ide_gepro="+Integer.parseInt(((Object[]) rolSeleccionado)[4] + "")+" and tipo_rol not in (0,4,6)");
			//String report="";
			//if (tab_roles.getTotalFilas()>0) {
			//	String relativeWebPath=FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporte" + utilitario.getVariable("IDE_USUA")+"1"+ ".pdf"); 
				//File fil_reporte = new File(relativeWebPath);
			//	strPathReporteRoles = utilitario.getURL()+ "/reportes/reporte" + utilitario.getVariable("IDE_USUA") + "roles.pdf";
			//	String usuario=utilitario.getVariable("IDE_USUA");
				//System.out.println("usuario "+usuario);
		//	   File fil_reporte1 = new File(ec.getRealPath("/reportes/reporte" + usuario+ "roles.pdf"));
		 //     exporter.setParameter(JRExporterParameter.OUTPUT_FILE, fil_reporte1);
		 //     exporter.exportReport();
				///reportes/rep_asistencia/rep_detalle_vacacion.jasper
			
			//}else {
				//strPathReporteRoles = utilitario.getURL()+ "/reportes/reporte" + utilitario.getVariable("IDE_USUA") + ".pdf";
			//File fil_reporte = new File(ec.getRealPath("/reportes/reporte" + utilitario.getVariable("IDE_USUA") + "roles.pdf"));
			    File fil_reporte = new File(ec.getRealPath("/reportes/reporte" + utilitario.getVariable("IDE_USUA") + ".pdf"));
		      exporter.setParameter(JRExporterParameter.OUTPUT_FILE, fil_reporte);
		      exporter.exportReport();
			//}
			
			
		      
		    }
		    catch (Exception ex)
		    {
		      System.out.println("error" + ex.getMessage());
		      ex.printStackTrace();
		    }
	}
    
	

	
    public List getLisRolesPago() {
        return lisRolesPago;
    }

    public void setLisRolesPago(List lisRolesPago) {
        this.lisRolesPago = lisRolesPago;
    }

    public String getStrOpcion() {
        return strOpcion;
    }

    public void setStrOpcion(String strOpcion) {
        this.strOpcion = strOpcion;
    }

    public Object getRolSeleccionado() {
        return rolSeleccionado;
    }

    public void setRolSeleccionado(Object rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }

    public String getStrPathReporte() {
        return strPathReporte;
    }

    public void setStrPathReporte(String strPathReporte) {
        this.strPathReporte = strPathReporte;
    }

	public Map getParametros() {
		return parametros;
	}

	public void setParametros(Map parametros) {
		this.parametros = parametros;
	}
	  public String getURL()
	  {
	    ExternalContext iecx = FacesContext.getCurrentInstance().getExternalContext();
	    HttpServletRequest request = (HttpServletRequest)iecx.getRequest();
	    String path = request.getRequestURL() + "";
	    path = path.substring(0, path.lastIndexOf("/"));
	    if (path.indexOf("portal") > 0) {
	      path = path.substring(0, path.lastIndexOf("/"));
	    }
	    return path;
	  }
	  public Conexion getConexion()
	  {
	    Conexion conexion = (Conexion)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CONEXION");
	    return conexion;
	  }

	public String getStrPathReporteRoles() {
		return strPathReporteRoles;
	}

	public void setStrPathReporteRoles(String strPathReporteRoles) {
		this.strPathReporteRoles = strPathReporteRoles;
	}
	  
	public String getStrPathReporteRolesConsolidado() {
		return strPathReporteRolesConsolidado;
	}

	public void setStrPathReporteRolesConsolidado(
			String strPathReporteRolesConsolidado) {
		this.strPathReporteRolesConsolidado = strPathReporteRolesConsolidado;
	}
	  
	public String getIde_nrdtn_consolidado() {
		return ide_nrdtn_consolidado;
	}

	public void setIde_nrdtn_consolidado(String ide_nrdtn_consolidado) {
		this.ide_nrdtn_consolidado = ide_nrdtn_consolidado;
	}
	  
	  
	  
	  
}
