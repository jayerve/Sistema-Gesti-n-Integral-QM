package paq_asistencia_permisos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import paq_adquisicion.ejb.ServicioAdquisicion;
import paq_asistencia.ejb.ServicioAsistencia;
import paq_bodega.ejb.ServicioBodega;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidades.EnvioMail;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.reportes.GenerarReporte;
public class pre_asistencia_aprueba_jefe extends Pantalla{

	@EJB

	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioAsistencia ser_asistencia = (ServicioAsistencia) utilitario.instanciarEJB(ServicioAsistencia.class);
	
	@EJB
	private ServicioEmpleado ser_empleado = (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);

	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	
	@EJB
	private ServicioAdquisicion ser_Adquisicion=(ServicioAdquisicion) utilitario.instanciarEJB(ServicioAdquisicion.class);
	
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	
	
	
	private SeleccionTabla set_tab_empleado=new SeleccionTabla();
	private AutoCompletar aut_empleado = new AutoCompletar();
	private Tabla tab_consulta=new Tabla();
	private Combo com_rubros = new Combo();

	private SeleccionCalendario sec_rango=new SeleccionCalendario();
	private Check che_todos_emp=new Check();
	private SeleccionTabla set_det_tip_nomina=new SeleccionTabla();
	private Map p_parametros=new HashMap();
	private SeleccionTabla sel_tab_empleados = new SeleccionTabla();
	private Confirmar con_guardar=new Confirmar();



	public pre_asistencia_aprueba_jefe(){
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);

		
		
		Etiqueta eti_colaborador=new Etiqueta("OPCIONES JEFE INMEDIATO: ");
		bar_botones.agregarComponente(eti_colaborador);
		
		
	

		// boton consultar
		Boton bot_consultar = new Boton();
		bot_consultar.setIcon("ui-icon-calculator");
		bot_consultar.setMetodo("consultar");
		bot_consultar.setValue("CONSULTAR");
		bot_consultar.setTitle("Consultar");
		bar_botones.agregarBoton(bot_consultar);
		
	
		//btn apobacion de  solicitud
		Boton bot_aprobar_solicitud=new Boton();
		bot_aprobar_solicitud.setValue("APROBAR SOLICITUD");
		bot_aprobar_solicitud.setMetodo("aprobarSolicitud");
		bar_botones.agregarBoton(bot_aprobar_solicitud);

		
		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
		
		Boton bot_enviar=new Boton();
		bot_enviar.setMetodo("seleccionarEnviar");
		bot_enviar.setValue("Enviar E-mail");
		bot_enviar.setIcon("ui-icon-mail-closed");
		//bar_botones.agregarBoton(bot_enviar);
		
		Boton bot_buscar=new Boton();
		bot_buscar.setIcon("ui-icon-person");
		bot_buscar.setValue("Buscar Solicitud de Compras");
		bot_buscar.setMetodo("importarSolicitudCompra");
		//bar_botones.agregarBoton(bot_buscar);
		
		
		set_tab_empleado.setId("set_tab_empleado");
		set_tab_empleado.setSeleccionTabla(ser_empleado.getSQLEmpleadosActivos(),"ide_geedp");
		//set_tab_empleado.getTab_seleccion().getColumna("").setNombreVisual("Nombre Proveedor");
		//set_tab_empleado.getTab_seleccion().getColumna("RUC_TEPRO").setNombreVisual("Ruc Proveedor");
		//set_tab_empleado.getTab_seleccion().getColumna("NOMBRE_TEPRO").setFiltro(true);
		//set_tab_empleado.getTab_seleccion().getColumna("RUC_TEPRO").setFiltro(true);
		set_tab_empleado.setTitle("Seleccione Proveedor");
		set_tab_empleado.getBot_aceptar().setMetodo("aceptarProveedor");
		set_tab_empleado.setRadio();
		agregarComponente(set_tab_empleado);
		

		/*
		 
		bot_anulado.setValue("ANULAR SOLICITUD");
		bot_anulado.setMetodo("anularSolicitud");

		Boton bot_aprobacion_talento_humano=new Boton();
		bot_aprobacion_talento_humano.setValue("APROBACION TALENTO HUMANO");
		bot_aprobacion_talento_humano.setMetodo("aprobacionTalentoHumano");
		
		Boton bot_aplica_vacacion=new Boton();
		bot_aplica_vacacion.setValue("APLICA VACACION");
		bot_aplica_vacacion.setMetodo("aplicaVacacion");

		Boton bot_deshacer_aplica_vacacion=new Boton();
		bot_deshacer_aplica_vacacion.setValue("DESHACER APLICA VACACION");
		bot_deshacer_aplica_vacacion.setMetodo("deshacerAplicaVacacion");
*/
		// boton visualizar_pdf
		//Boton bot_visualizar_pdf = new Boton();
		//bot_visualizar_pdf.setIcon("ui-icon-calculator");
		//bot_visualizar_pdf.setMetodo("descargarArchivoAdjunto");
		//bot_visualizar_pdf.setAjax(false);
		//bot_visualizar_pdf.setValue("Descargar");
		//bot_visualizar_pdf.setTitle("Descargar");
		
		//bar_botones.agregarBoton(bot_visualizar_pdf);
		
		
	
		

		tab_consulta.setId("tab_consulta");
		tab_consulta.setSql(ser_asistencia.getConsultaAsistenciaLote("1900-01-01", "1900-01-01"));
//		tab_consulta.getColumna("detalle_asmot").setVisible(true);
    	tab_consulta.getColumna("detalle_asmot").setNombreVisual("Motivo");
		tab_consulta.getColumna("apellido_paterno_gtemp").setNombreVisual("Apellido");
		tab_consulta.getColumna("apellido_materno_gtemp").setVisible(false);
		tab_consulta.getColumna("primer_nombre_gtemp").setVisible(true);
		tab_consulta.getColumna("primer_nombre_gtemp").setNombreVisual("Nombre");
	    tab_consulta.getColumna("segundo_nombre_gtemp").setVisible(false);
	    tab_consulta.getColumna("fecha_solicitud_aspvh").setNombreVisual("Fecha Solicitud");

	    tab_consulta.getColumna("fecha_desde_aspvh").setNombreVisual("Fecha Desde");
	    tab_consulta.getColumna("fecha_hasta_aspvh").setNombreVisual("Fecha Hasta");
	    tab_consulta.getColumna("hora_desde_aspvh").setNombreVisual("Hora Desde");
	    tab_consulta.getColumna("hora_hasta_aspvh").setNombreVisual("Hora Hasta");
	    
	    
	    tab_consulta.getColumna("detalle_aspvh").setNombreVisual("Detalle");
	    tab_consulta.getColumna("nro_dias_aspvh").setNombreVisual("# Dias");
	    tab_consulta.getColumna("nro_horas_aspvh").setNombreVisual("# Horas");
		

		
		

		
	

		tab_consulta.setCampoPrimaria("ide_aspvh");
		tab_consulta.setLectura(true);
		tab_consulta.setTipoSeleccion(true);
		tab_consulta.setNumeroTabla(1);
		
		tab_consulta.dibujar();
		tab_consulta.setRows(20);

		PanelTabla pat_tab=new PanelTabla();
		pat_tab.setPanelTabla(tab_consulta);

		Division div1=new Division();
		div1.dividir1(pat_tab);


		agregarComponente(div1);

		sec_rango.setId("sec_rango");
		sec_rango.setTitle("Seleccione un Rango de Fechas");

		agregarComponente(sec_rango);
		
		// confirmacion para guardar datos
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);

	}


	public  void aceptarProveedor(){
		String str_seleccionado = set_tab_empleado.getValorSeleccionado();
		TablaGenerica tab_proveedor=ser_bodega.getTablaProveedor(str_seleccionado);
		if (str_seleccionado!=null){			
				}
		set_tab_empleado.cerrar();
		
	}
	public void importarSolicitudCompra(){

		set_tab_empleado.getTab_seleccion().setSql("Select * from gth_empleado");
		set_tab_empleado.getTab_seleccion().ejecutarSql();
		set_tab_empleado.dibujar();

	}
	
	
	
	 public File generar(Map parametros, String reporte,String IDE_EMPL) {     
         try {           
                 FacesContext fc = FacesContext.getCurrentInstance();    
                 ExternalContext ec = fc.getExternalContext();  
                 JasperPrint jasperPrint=null;
                 try {
                         
                         InputStream fis = ec.getResourceAsStream(reporte);
                         JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fis);
                         
                         try {
                         parametros.put("ide_empr", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_empr")));
                         } catch (Exception e) {
                         }
                         try {
                         parametros.put("ide_sucu", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_sucu")));
                         } catch (Exception e) {
                         }

                         try {
                         parametros.put("usuario", pckUtilidades.CConversion.CInt(utilitario.getVariable("ide_usua")));
                         } catch (Exception e) {
                         }

                         parametros.put("SUBREPORT_DIR", utilitario.getURL());
                         
                         jasperPrint = JasperFillManager.fillReport(
                         jasperReport, parametros, utilitario.getConexion().getConnection());
                         
                         } catch (Exception e) {
                         System.out.println("error ejecutar" + e.getMessage());
                         }
                 
	                 JRExporter exporter = new JRPdfExporter();         
	                 exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);  
                 File fil_reporte = new File(ec.getRealPath("/reportes/rol_"+IDE_EMPL+ ".pdf"));          
	                 exporter.setParameter(JRExporterParameter.OUTPUT_FILE, fil_reporte);        
	                 exporter.exportReport();                        
	                 return fil_reporte;     
                 } catch (Exception ex) {         
                         System.out.println("error" + ex.getMessage());        
                         ex.printStackTrace();     
                         }    
         return null;    
         }

	
	/**
	Metodo para envio de correo mail
	**/
	
	
	public void seleccionarEnviar(){                                
            
                        //Busco los correos de los empleados seleccionados
                        StringBuilder str_ide=new StringBuilder();
                    //    int int_num_col_idegtemp=sel_tab_empleados.getTab_seleccion().getNumeroColumna("IDE_GTEMP");

                 //       for (Fila filaActual:sel_tab_empleados.getTab_seleccion().getSeleccionados()) {
                 //               if(str_ide.toString().isEmpty()==false){
                 //                       str_ide.append(",");
                 //               }
                   //             str_ide.append(filaActual.getCampos()[int_num_col_idegtemp]);
                       // }
                        TablaGenerica tab_correos =ser_empleado.getCorreoEmpleados("22");                                         
                      //  StringBuilder str_resultado=new StringBuilder(getFormatoInformacion("TOTAL DE EMPLEADOS PARA ENVIAR CORREO ELECTRÓNICO"));
                        //EnviarCorreo env_enviar = new EnviarCorreo();
                        
                 
                        p_parametros.put("IDE_GEEDP", "3");            
                        p_parametros.put("titulo", " BOLETA DE PAGO");
                        p_parametros.put("IDE_NRTIR",utilitario.getVariable("p_nrh_trubro_egreso")+","+utilitario.getVariable("p_nrh_trubro_ingreso"));
                        p_parametros.put("par_total_recibir",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_valor_recibir")));
                        p_parametros.put("par_total_ingresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_ingresos")));
                        p_parametros.put("par_total_egresos",pckUtilidades.CConversion.CInt(utilitario.getVariable("p_nrh_rubro_total_egresos")));                                                    
                        
                        
                        File fil_rol=generar(p_parametros, "/reportes/rep_rol_de_pagos/rep_n_rol_pagos.jasper","22");
                        List<File> lis_file = new ArrayList<File>();
                        lis_file.add(fil_rol);
                        
                        
                        String str_asunto="PERMISO DE PERSONAL";
                
                                                String str_mensaje="Fecha Generación: "+utilitario.getFechaLarga(utilitario.getFechaActual())+" Hora : "+utilitario.getHoraActual()+" "+
                                                "Funcionario(a):  Para su conocimiento, le adjuntamos un archivo pdf con el detalle del rol de pago generado por el Sistema de Gestíon de Talento Humano.";
                        //TablaGenerica tab_correo = utilitario.consultar("select ide_corr,correo_corr,clave_corr from sis_correo limit 1");
                        TablaGenerica tab_correo_envio = utilitario.consultar("select co.ide_corr,smtp_corr, puerto_corr, usuario_corr, correo_corr,clave_corr,plantilla_cpla from sis_correo co " 
                        		+ " left join sis_correo_plantilla cop on cop.ide_corr=co.ide_corr "
                				+ " where activo_cpla = true and activo_corr = true and co.ide_corr=1");
                        EnvioMail envMail = new EnvioMail(tab_correo_envio.getValor("smtp_corr"), tab_correo_envio.getValor("puerto_corr"), tab_correo_envio.getValor("correo_corr"), tab_correo_envio.getValor("usuario_corr"), tab_correo_envio.getValor("clave_corr"));
                                                
                                                
                                                String str_mail="juan.ayerve@emgirs.gob.ec";
                                                String str_clave="emgirs2017";
                        //utilitario.EnviaEmail(tab_correo.getValor("correo_corr"), tab_correo.getValor("clave_corr"), str_mail, str_asunto, str_mensaje,fil_rol);
                        envMail.setAsunto(str_asunto);
            			envMail.setCuerpoHtml(str_mensaje);
            			envMail.setPara(str_mail);
            			if(fil_rol!=null)
            			{
            				envMail.setNombreAdjunto("rol_pagos.pdf");
            				envMail.setAdjuntoArray64(pckUtilidades.Utilitario.fileConvertToArray64(fil_rol));
            			}
                        pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
                        if(obj.getRespuesta())
            			{
            				utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : email: " + str_mail);
            			}
            			else
            				utilitario.agregarMensajeError("Correo no enviado a : email: " + str_mail , " msjError: " + obj.getDescripcion());
                                                //utilitario.EnviaEmail(str_mail, str_clave, str_mail, str_asunto, str_mensaje,null);
                                               
                                               // tab_correos.getFilas().remove(j);

                                             //   break;
                                        //}
                                //}


                       // }
                 //       sel_tab_empleados.cerrar();
                        /*	
                        //String str_mensaje=env_enviar.enviarTodos();
                        System.out.println("DFJ****   "+str_mensaje);
                        if(str_mensaje.isEmpty()==false){
                                str_resultado.append(getFormatoError(str_mensaje));
                        }
						*/
                //        edi_msj.setValue(str_resultado);
                //        dia_resumen.dibujar();

             //   }else{
              //          utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
               // }                                       

        //}

}

	public void aceptarAprobarSolicitud(){
		
		String str_seleccionados=tab_consulta.getFilasSeleccionadas();

	
		for(int i=0;i<tab_consulta.getTotalFilas();i++){
			
			utilitario.getConexion().ejecutarSql("update asi_permisos_vacacion_hext set aprobado_aspvh=true,"
			+" aprobado_tthh_aspvh= false, anulado_aspvh=false where ide_aspvh in("+str_seleccionados+")"); 
			guardarPantalla();
			
			
}
		con_guardar.cerrar();
		utilitario.addUpdate("tab_consulta");
		
utilitario.agregarMensaje("Aprobación de Permisos ", "Se ha guardado correctamente");
	
	}
	
	
	/**
	 * Metodo para la aprobacion de solicitud por parte de jefe inmediato
	 */
	public void aprobarSolicitud(){	
		//System.out.println("Datos de tabla"+tab_consulta.getValor("IDE_GTEMP"));
		
		
		//dentro de esta variable guardamos los campos seleccionados o solicitudes para ser aprobadas 
		String str_seleccionados=tab_consulta.getFilasSeleccionadas();
		 // numero de objetos seleccionados de la lista tab_consulta.getSeleccionados().length
		//me devuelve u vector
		//System.out.println("Seleccionados: "+tab_consulta.getSeleccionados());
		//me devuelve los elementos seleccionados en la lista de permisos para ser aprobados
		//System.out.println tab_consulta.getSeleccionados().length("str_seleccionados: "+str_seleccionados);	
        System.out.println("str_seleccionados: "+str_seleccionados);
		if(str_seleccionados!=null && !str_seleccionados.isEmpty()){
			if(tab_consulta.getTotalFilas()>0){
					con_guardar.setMessage("Esta Seguro de Aprobar La Solicitud de Permiso");
					con_guardar.setTitle("CONFIRMACION APROBACIÓN DE SOLICITUD DE PERMISO");
					con_guardar.getBot_aceptar().setMetodo("aceptarAprobarSolicitud");
					con_guardar.dibujar();
					utilitario.addUpdate("con_guardar");
				}else{
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "El Empleado no Tiene Solicitudes");
				}		
				
				
				
				

				}else {
					utilitario.agregarMensajeInfo("Recuerde debe Consultar Permisos", "Aun no se han seleccionado datos");

		              }
		
			
		/*if(aut_empleado.getValor()!=null && !aut_empleado.getValor().isEmpty()){
			if(tab_permisos.getTotalFilas()>0){
				if(tab_permisos.getValor("anulado_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud se encuentra Anulada");
					return;
				}
				if(tab_permisos.getValor("aprobado_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud se encuentra Aprobada");
					return;
				}
				con_guardar.setMessage("Esta Seguro de Aprobar La Solicitud de Permiso");
				con_guardar.setTitle("CONFIRMACION APROBACION DE SOLICITUD DE PERMISO");
				con_guardar.getBot_aceptar().setMetodo("aceptarAprobarSolicitud");
				con_guardar.dibujar();
				utilitario.addUpdate("con_guardar");
			}else{
				utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "El Empleado no Tiene Solicitudes");
			}		
		}else{
			utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Debe seleccionar un Empleado");
		}
		*/
	}

    public void downloadFile(byte[] fileBytes, String nameFile) throws IOException{
    	
    	String filePath = "C://"+nameFile+".pdf";
    	
    	if (escribirArchivo(fileBytes, filePath)){
    	
	        FacesContext context = FacesContext.getCurrentInstance();  
	        HttpServletResponse response = (HttpServletResponse) context  
	                             .getExternalContext().getResponse();  
	        File file = new File(filePath);  
	        if (!file.exists()) {  
	          response.sendError(HttpServletResponse.SC_NOT_FOUND);  
	          return;  
	         }  
	        response.reset();  
	        response.setBufferSize(1024);  
	        response.setContentType("application/octet-stream");  
	        response.setHeader("Content-Length", String.valueOf(file.length()));  
	        //response.setHeader("Content-Disposition", "attachment;filename=\""  
	          //     + file.getName() + "\"");
	        response.setHeader("Content-Disposition", "inline;filename=\""  
	                + nameFile + ".pdf\"");
	        
	        BufferedInputStream input = null;  
	        BufferedOutputStream output = null;  
	
	        try 
	        {  
	            input = new BufferedInputStream(new FileInputStream(file),  
	                        1024);  
	            output = new BufferedOutputStream(response.getOutputStream(),  
	                            1024);  
	            byte[] buffer = new byte[1024];  
	            int length;  
	            while ((length = input.read(buffer)) > 0) {  
	                output.write(buffer, 0, length);  
	            }  
	        } finally 
	        {  
	            input.close();  
	            output.close();  
	        }  
	        context.responseComplete();
    	}
    }
    



    public void descargarArchivoAdjunto(){
		try {

			if (tab_consulta.getSeleccionados().length != 0){
				
				for (Fila fila:tab_consulta.getSeleccionados()) {
					if ((byte[])fila.getCampos()[30] == null || ((byte[])fila.getCampos()[30]).length == 0){
						utilitario.agregarMensajeInfo("No se puede descargar el archivo", "No existe archivo adjunto.");
						return;
					}
						downloadFile((byte[])tab_consulta.getSeleccionados()[0].getCampos()[30], "archivo_permiso_adjunto");
				}
				
			}else{
				utilitario.agregarMensajeInfo("No se puede descargar", "Debe seleccionar al menos un registro.");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			utilitario.agregarMensajeError("Error al descargar archivo", "ocurrio un error al descargar el archivo adjunto");
		}


	}
	
    public boolean escribirArchivo(byte[] fileBytes, String archivoDestino){ 
    	   boolean correcto = false; 
    	   try { 
    	     OutputStream out = new FileOutputStream(archivoDestino); 
    	     out.write(fileBytes); 
    	     out.close();         
    	     correcto = true; 
    	     System.out.println("archivo creado");
    	   } catch (Exception e) { 
    	     e.printStackTrace(); 
    	   }         
    	     return correcto;

    	}
    
    
    
    
    
    
    

	
	public void limpiar(){
		tab_consulta.setSql(ser_asistencia.getConsultaAsistenciaLote("1900-01-01", "1900-01-01"));
		tab_consulta.ejecutarSql();		
	}

	String str_fecha1="";
	String str_fecha2="";
	
	public void consultar(){
		


		if (!sec_rango.isVisible()){
			sec_rango.setFecha1(null);
			sec_rango.setFecha2(null);
			str_fecha1="";
			str_fecha2="";
			sec_rango.getBot_aceptar().setMetodo("consultar");
			sec_rango.dibujar();
			utilitario.addUpdate("sec_rango");
		}else{

			if (!sec_rango.isFechasValidas()){
				str_fecha1="";
				str_fecha2="";
				utilitario.agregarMensajeInfo("No se puede consultar", "fechas incorrectas");
				return;
			}



		//	System.out.println("che "+che_todos_emp.getValue());

			str_fecha1=sec_rango.getFecha1String();
			str_fecha2=sec_rango.getFecha2String();
			
			TablaGenerica ide_geedp = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
			
			tab_consulta.setSql(ser_asistencia.getConsultaAsistenciaJefeInmediatoLote(str_fecha1, str_fecha2, Integer.parseInt(ide_geedp.getValor(0,"ide_geedp"))));
			
			tab_consulta.ejecutarSql();
			utilitario.addUpdate("tab_consulta");


			sec_rango.cerrar();
			
			if (tab_consulta.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No existen transacciones registradas ", "");
			}
		}
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub

	}

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}

	public Tabla getTab_consulta() {
		return tab_consulta;
	}

	public void setTab_consulta(Tabla tab_consulta) {
		this.tab_consulta = tab_consulta;
	}


	public SeleccionCalendario getSec_rango() {
		return sec_rango;
	}


	public void setSec_rango(SeleccionCalendario sec_rango) {
		this.sec_rango = sec_rango;
	}


	public SeleccionTabla getSet_solicitud() {
		return set_tab_empleado;
	}


	public void setSet_solicitud(SeleccionTabla set_solicitud) {
		this.set_tab_empleado = set_solicitud;
	}


	public Confirmar getCon_guardar() {
		return con_guardar;
	}


	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}



}
