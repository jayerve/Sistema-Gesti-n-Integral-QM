/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_seguimiento;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;


import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidades.EnvioMail;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


/**
 *
 * @author DELL-USER
 */
public class pre_seg_informe extends Pantalla {
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
    private Tabla tab_informe = new Tabla();
    private Tabla tab_asignacion = new Tabla();
    private Tabla tab_recomendacion = new Tabla();
    private Tabla tab_informe_adjunto = new Tabla();
    String usuario_responsable="";

    public pre_seg_informe() {
    //	notificaciones();
		utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'seg_informe'");

     	 TablaGenerica tab_usuario=utilitario.consultar("SELECT ide_seusu, ide_seemp, ide_secar, nombre_seusu, login_seusu, password_seusu, "
					+ "usu_email, ide_usua, activo_seusu "
					+ "FROM seg_usuario "
					+ "where ide_usua="+utilitario.getVariable("ide_usua")+" and activo_seusu=true and ide_secar=1") ;
 	if (tab_usuario.getTotalFilas()>0) {
 		usuario_responsable=tab_usuario.getValor("ide_seusu");
		}else{
			utilitario.agregarMensajeInfo("No contiene permisos de Acceso a esta Opción", "Contactese con el administrador del Sistema");
			return;
		} 
    	
    	tab_informe.setId("tab_informe");
        tab_informe.setTabla("seg_informe", "ide_seinf", 1);
        tab_informe.getColumna("ide_seinf").setNombreVisual("CODIGO");
        tab_informe.getColumna("ide_sesui").setCombo("SELECT ide_sesui, descripcion_sesui "
        	+ "FROM seg_suscribe_informe");
        tab_informe.getColumna("ide_sesui").setRequerida(true);
        tab_informe.getColumna("ide_sesui").setNombreVisual("SUSCRIBE");
        tab_informe.getColumna("ide_seesi").setCombo("SELECT ide_seesi, descripcion_seesi "
        		+ "FROM seg_estado_informe");
        tab_informe.getColumna("ide_seesi").setRequerida(true);
        tab_informe.getColumna("ide_seesi").setNombreVisual("ESTADO");
        tab_informe.getColumna("ide_senii").setCombo("SELECT ide_senii, descripcion_senii "
        		+ "FROM seg_nivel_informe "
        		+ "order by ide_senii desc");

        
        tab_informe.getColumna("ide_senii").setRequerida(true);
        tab_informe.getColumna("ide_senii").setNombreVisual("PRIORIDAD");
        tab_informe.getColumna("ide_senii").setRequerida(true);		 
        tab_informe.getColumna("numero_seinf").setRequerida(true);
        tab_informe.getColumna("numero_seinf").setNombreVisual("NUMERO");
        tab_informe.getColumna("numero_seinf").setLongitud(10);
        
        tab_informe.getColumna("asunto_seinf").setNombreVisual("ASUNTO");
        tab_informe.getColumna("asunto_seinf").setRequerida(true);

     //   tab_informe.getColumna("asunto_seinf").setAncho(80);
        //tab_informe.getColumna("asunto_seinf").setLongitud(150);
        //tab_informe.getColumna("asunto_seinf").setLectura(false);

        tab_informe.getColumna("fecha_aprobacion_seinf").setRequerida(true);
        tab_informe.getColumna("fecha_aprobacion_seinf").setNombreVisual("FECHA APROBACION");
        tab_informe.getColumna("fecha_inicio_seinf").setRequerida(true);
        tab_informe.getColumna("fecha_inicio_seinf").setNombreVisual("FECHA INICIO");
        tab_informe.getColumna("fecha_fin_seinf").setRequerida(true);
        tab_informe.getColumna("fecha_fin_seinf").setNombreVisual("FEECHA FIN");
        
        tab_informe.getColumna("usuario_responsable_seinf").setNombreVisual("USUARIO REGISTRO");
        tab_informe.getColumna("usuario_responsable_seinf").setCombo("SELECT  segus.ide_seusu,segus.login_seusu "
        		+ "FROM seg_usuario segus "
        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
        		+ "order by segus.ide_seusu");
        tab_informe.getColumna("usuario_responsable_seinf").setAutoCompletar();
        tab_informe.getColumna("usuario_responsable_seinf").setLectura(true);		
        tab_informe.getColumna("activo_seinf").setCheck();
        tab_informe.getColumna("activo_seinf").setValorDefecto("true");
        tab_informe.getColumna("activo_seinf").setNombreVisual("ACTIVO");
        tab_informe.getGrid().setColumns(4);
        tab_informe.setTipoFormulario(true);
        tab_informe.agregarRelacion(tab_informe_adjunto);
        tab_informe.agregarRelacion(tab_recomendacion);
      // tab_informe.agregarRelacion(tab_asignacion);
        tab_informe.dibujar();
        
        
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_informe);
		pat_panel1.setMensajeWarn("1.INFORME");

			
		tab_informe_adjunto.setId("tab_informe_adjunto");
		tab_informe_adjunto.setTabla("seg_adjunto_informe", "ide_seadi", 2);
		tab_informe_adjunto.getColumna("ide_seadi").setNombreVisual("CODIGO");
		tab_informe_adjunto.getColumna("ide_seinf").setVisible(false);
		tab_informe_adjunto.getColumna("activo_seadi").setCheck();
		tab_informe_adjunto.getColumna("activo_seadi").setValorDefecto("true");
		tab_informe_adjunto.getColumna("activo_seadi").setNombreVisual("ACTIVO");
		tab_informe_adjunto.getColumna("fecha_registro_seadi").setNombreVisual("FEC.ADJ");
		tab_informe_adjunto.getColumna("fecha_registro_seadi").setValorDefecto(utilitario.getFechaActual());
		tab_informe_adjunto.getColumna("fecha_registro_seadi").setLectura(true);
		tab_informe_adjunto.getColumna("hora_registro_seadi").setNombreVisual("HORA.ADJ");
		tab_informe_adjunto.getColumna("hora_registro_seadi").setValorDefecto(utilitario.getHoraActual());
		tab_informe_adjunto.getColumna("hora_registro_seadi").setLectura(true);
		tab_informe_adjunto.getColumna("adjunto_seadi").setUpload("seguimiento");
		tab_informe_adjunto.getColumna("adjunto_seadi").setNombreVisual("ADJUNTO");
		tab_informe_adjunto.getColumna("adjunto_seadi").setRequerida(true);
		tab_informe_adjunto.dibujar();
        PanelTabla pat_panel4= new PanelTabla();
        pat_panel4.setPanelTabla(tab_informe_adjunto);
		pat_panel4.setMensajeWarn("2. A D J U N T O");
		
		
	    tab_recomendacion.setId("tab_recomendacion");
        tab_recomendacion.setTabla("seg_recomendacion", "ide_serec", 3);
        tab_recomendacion.getColumna("ide_seinf").setVisible(false);
        tab_recomendacion.getColumna("ide_serec").setNombreVisual("CODIGO");

        tab_recomendacion.getColumna("ide_setir").setCombo("SELECT ide_setir, descripcion_setir "
            	+ "FROM seg_tipo_recomendacion");
        tab_recomendacion.getColumna("ide_setir").setRequerida(true);
        tab_recomendacion.getColumna("ide_setir").setNombreVisual("TIPO");
        tab_recomendacion.getColumna("ide_setir").setAutoCompletar();		
           
            
        tab_recomendacion.getColumna("ide_seesr").setCombo("SELECT ide_seesr, descripcion_seesr  "
        		+ "FROM seg_estado_recomendacion");
        tab_recomendacion.getColumna("ide_seesr").setRequerida(true);
        tab_recomendacion.getColumna("ide_seesr").setNombreVisual("ESTADO");
        tab_recomendacion.getColumna("ide_seesr").setAutoCompletar();	
        
        
        tab_recomendacion.getColumna("numero_serec").setNombreVisual("NUMERO");
        tab_recomendacion.getColumna("numero_serec").setLectura(true);

        tab_recomendacion.getColumna("asunto_serec").setNombreVisual("ASUNTO");
        //tab_recomendacion.getColumna("asunto_serec").setAncho(80);
       tab_recomendacion.getColumna("asunto_serec").setRequerida(true);
       // tab_recomendacion.getColumna("asunto_serec").setLectura(false);

        tab_recomendacion.getColumna("fecha_aprobacion_serec").setNombreVisual("FECHA APROBACION");
        tab_recomendacion.getColumna("fecha_inicio_serec").setNombreVisual("FECHA INICIO");
        tab_recomendacion.getColumna("fecha_fin_serec").setNombreVisual("FEECHA FIN");
        tab_recomendacion.getColumna("activo_serec").setNombreVisual("ACTIVO");
        tab_recomendacion.getColumna("activo_serec").setCheck();
        tab_recomendacion.getColumna("activo_serec").setValorDefecto("true");

        tab_recomendacion.agregarRelacion(tab_asignacion);
        tab_recomendacion.getGrid().setColumns(4);
        tab_recomendacion.setCampoOrden("ide_serec asc");

        tab_recomendacion.dibujar();
        
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_recomendacion);
		pat_panel2.setMensajeWarn("3.R E C O M E N D A C I Ó N");

         
        tab_asignacion.setId("tab_asignacion");
        tab_asignacion.setTabla("seg_asignacion", "ide_seasi", 4);
        tab_asignacion.getColumna("ide_seasi").setNombreVisual("CODIGO");
        tab_asignacion.getColumna("ide_serec").setVisible(false);
        tab_asignacion.getColumna("ide_seusu").setVisible(false);
        tab_asignacion.getColumna("ide_seinf").setVisible(false);
        tab_asignacion.getColumna("ide_periodo_nuevo_seasi").setVisible(false);
        tab_asignacion.getColumna("ide_seemp").setNombreVisual("AREA");
        tab_asignacion.getColumna("ide_seemp").setCombo("SELECT ide_seemp, descripcion_seemp "
        		+ "FROM seg_empresa where  activo_seemp=true");
        tab_asignacion.getColumna("ide_seemp").setMetodoChange("validarAsignacion");
        
        tab_asignacion.getColumna("ide_seemp").setRequerida(true);
        tab_asignacion.dibujar();
        PanelTabla pat_panel3= new PanelTabla();
        pat_panel3.setPanelTabla(tab_asignacion);
		pat_panel3.setMensajeWarn("4. A R E A");
		
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1,pat_panel4,"50%","V");

        Division div_division2 = new Division();
        div_division2.setId("div_division2");
        div_division2.dividir2(pat_panel2, pat_panel3, "50%",  "V");

        
        Division div_division3 = new Division();
        div_division3.setId("div_division3");
        div_division3.dividir2(div_division, div_division2,"40%", "H");
        agregarComponente(div_division3);
        

    }

    @Override
    public void insertar() {
		// TODO Auto-generated method stub
    	  	
		if(tab_informe.isFocus()){
			tab_informe.insertar();				
		   	tab_informe.setValor("usuario_responsable_seinf", usuario_responsable);
  	 
		}else if(tab_informe_adjunto.isFocus()){
			if (tab_informe.getTotalFilas()>0) {	
				tab_informe_adjunto.insertar();}
			}else if(tab_recomendacion.isFocus()){
				if (tab_informe.getTotalFilas()>0) {
					tab_recomendacion.insertar();
					TablaGenerica tab_num_rec=utilitario.consultar("SELECT count(numero_serec) as num_rec1,count(numero_serec) as num_rec  "
							+ "FROM seg_recomendacion "
							+ "where ide_seinf="+tab_informe.getValor("ide_seinf")+"");
			        tab_recomendacion.getColumna("numero_serec").setLectura(false);
					tab_recomendacion.setValor("numero_serec", ""+(Integer.parseInt(tab_num_rec.getValor("num_rec1"))+1));
			        tab_recomendacion.getColumna("numero_serec").setLectura(true);

				}
			}else if(tab_asignacion.isFocus()){
				if (tab_informe.getTotalFilas()>0 && tab_recomendacion.getTotalFilas()>0) {
					tab_asignacion.insertar();
					tab_asignacion.setValor("ide_serec", tab_recomendacion.getValor("ide_serec"));
					tab_asignacion.setValor("ide_seinf", tab_informe.getValor("ide_seinf"));
					tab_asignacion.setValor("ide_periodo_nuevo_seasi", "2");
				}
				else{
					utilitario.agregarMensajeInfo("No se puede insertar asignacion", "Debe Registrar el 1.Informe y la 2.Recomendacion");
				return;
				}
				
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe Registrar el 1. informe, 2.recomendacion y 3.asignacion");
			return;
			}
		}    

    @Override
    public void guardar() {
               
    	if (tab_informe.guardar()) {
        	guardarPantalla();    	
    	}
    	
    	if (tab_informe_adjunto.guardar()) {
        	guardarPantalla();    	
    	}
    	
    	if (tab_recomendacion.guardar()) {
        	guardarPantalla();    	
    	}
    	
        if (tab_asignacion.guardar()) {
  	
			
  		  
  		  
        	
	    	  if(tab_asignacion.isFilaInsertada() || tab_asignacion.isFilaModificada()){	
	    		  TablaGenerica tab_empresa_usuario=utilitario.consultar("SELECT ide_seusu, ide_seemp, ide_secar, nombre_seusu, login_seusu, password_seusu, "
	    					+ "usu_email, ide_usua, activo_seusu "
	    					+ "FROM seg_usuario "
	    					+ "where ide_seemp="+tab_asignacion.getValor("ide_seemp")+" and activo_seusu=true");
	    		  
	    		  if (tab_empresa_usuario.getTotalFilas()>0) {
	    				tab_asignacion.setValor("ide_seusu", tab_empresa_usuario.getValor("ide_seusu"));
	    			}else {
	    				utilitario.agregarMensaje("No se puede asignar","Debe crear el usuario");
	    				return;
	    			}
	    		  
	    		  
	    		  
	    		  
	    				guardarPantalla();
	    				TablaGenerica tab_usuario=null;
	    				for (int i = 0; i < tab_empresa_usuario.getTotalFilas(); i++) {
		    				 tab_usuario= utilitario.consultar("select ide_usua,ide_gtemp from sis_usuario where ide_usua="+tab_empresa_usuario.getValor(i,"ide_usua")+" order by ide_usua desc limit 1");   				
						    				
		            	
	    				try {
						    //ENVIO  DE ASIGNACION DE RECOMENDACION TIPO 1 PARA REGISTRO DE ASIGNACION DE RECOMENDACIONES
	    			//		EnviarCorreo(tab_asignacion.getValor("ide_seasi"), tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf"), tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec"), tab_usuario.getValor("ide_gtemp"),1);

	    				
	    				} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    				}
	    	  		}else{
	            	guardarPantalla();    	
	    	  		}
	    	  }
    }

    @Override
    public void eliminar() {
		if (tab_informe.isFocus()) {
			TablaGenerica tab_rec=utilitario.consultar("SELECT ide_serec, ide_seinf, ide_setir, ide_seesr, asunto_serec, numero_serec, "
					+ "fecha_inicio_serec, fecha_fin_serec, fecha_aprobacion_serec, "
					+ "activo_serec "
					+ "FROM seg_recomendacion "
					+ "where ide_seinf="+tab_informe.getValorSeleccionado());
			if (tab_rec.getTotalFilas()==0) {
				tab_informe.eliminar();

			}else {
				utilitario.agregarMensajeInfo("No se puede realizar esta acción", "El informe seleccionado contiene recomendaciones asignadas");
				return;
			}	
		}else if (tab_informe_adjunto.isFocus()) {
			tab_informe_adjunto.eliminar();

		
			
							
		}else if (tab_recomendacion.isFocus()) {
			
				TablaGenerica tab_asignadas=utilitario.consultar("SELECT asi.ide_seasi, asi.ide_serec, asi.ide_seusu, asi.ide_seemp, asi.ide_seinf "
					+ "FROM seg_asignacion asi "
					+ "left join seg_recomendacion reco on asi.ide_serec=reco.ide_serec "
					+ "left join seg_respuesta res on reco.ide_serec=res.ide_serec "
					//+ "where asi.ide_serec=1 "
					+ "where asi.ide_serec="+tab_recomendacion.getValorSeleccionado());
			if (tab_asignadas.getTotalFilas()==0) {
				tab_recomendacion.eliminar();

			}else {
				utilitario.agregarMensajeInfo("No se puede realizar esta acción", "La recomendación seleccionada contiene areas asignadas");
				return;
			
			}	
			
		}else if (tab_asignacion.isFocus()) {
			
			try {
				TablaGenerica tab_recomendaciones_asignadas=utilitario.consultar("SELECT ide_seres, ide_serec, descripcion_seres, hora_seres,ide_seemp, ide_setre, activo_seres "
						+ "FROM seg_respuesta "
						+ "where ide_serec="+tab_recomendacion.getValorSeleccionado()+" and ide_seinf="+tab_informe.getValor("ide_seinf")+" and ide_sepla is not null and activo_seres=true");
				if (tab_recomendaciones_asignadas.getTotalFilas()==0) {
					
					  TablaGenerica tab_empresa_usuario=utilitario.consultar("SELECT ide_seusu, ide_seemp, ide_secar, nombre_seusu, login_seusu, password_seusu, "
								+ "usu_email, ide_usua, activo_seusu "
								+ "FROM seg_usuario "
								+ "where ide_seemp="+tab_asignacion.getValor("ide_seemp")+" and activo_seusu=true");
					  
					  if (tab_empresa_usuario.getTotalFilas()>0) {
						  //TIPO 2 PARA REGISTRO DE ASIGNACION DE RECOMENDACIONES
						  EnviarCorreo(tab_asignacion.getValor("ide_seemp"), tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf"), 
						  tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec"), ser_seguridad.getUsuario(tab_empresa_usuario.getValor("IDE_USUA")).getValor("ide_gtemp"),2);

						}else {
							utilitario.agregarMensaje("No se puede asignar","Debe crear el usuario");
							return;
						}
					  

					
					tab_asignacion.eliminar();
					
					
					
				}else {
					utilitario.agregarMensajeInfo("No se puede realizar esta acción", "Tiene avances realizados para el informe y recomendación seleccionada");
					return;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("");			
				}
			
			
		}

    }
    
 
    

	
	public void EnviarCorreo(String ide_seasi,String informe, String recomendacion,String ide_gtemp,int tipoRol) throws Exception{
		String strNombreEmpleado="";

		try {
		TablaGenerica tab_correo= utilitario.consultar("select ide_gtemp,detalle_gtcor from gth_correo where ide_gtemp="+ide_gtemp);
		String correo=tab_correo.getValor("detalle_gtcor");
		TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
				+ "clave_corr from sis_correo where ide_corr=2"); 
		String smtp_correo="mail.emgirs.gob.ec"; 
		String puertoEnvio="25"; 
		String correo_envio="srecomendaciones@emgirs.gob.ec";
		String usuario_envio="srecomendaciones"; 
		String clave_correo="R3comendaciones.2022";
		strNombreEmpleado = obtenerNombresApellidosEmpleadoCorreo(ide_gtemp);				
			 	
		//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
		EnvioMail envMail = new EnvioMail(smtp_correo,puertoEnvio,correo_envio,usuario_envio,clave_correo);
		try {
			
			
			//util.EnviaMailInterno(envMail, correo_envio, "SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP", emailLinkEncuestaCorreo(strNombreEmpleado,informe,recomendacion, tipoRol), null);
			String str_mail=correo_envio;
			envMail.setAsunto("SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP");
			envMail.setCuerpoHtml(emailLinkEncuestaCorreo(strNombreEmpleado,informe,recomendacion, tipoRol));
			envMail.setPara(str_mail);
			pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
			
			if(obj.getRespuesta())
			{
				utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : email: " + str_mail);
			}
			else
				utilitario.agregarMensajeError("Correo no enviado a : email: " + str_mail , " msjError: " + obj.getDescripcion());
			//no usar EnviaMailInterno
			EnviaMailInterno(envMail, correo, "SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP", emailLinkEncuestaCorreo(strNombreEmpleado,informe,recomendacion, tipoRol), null,strNombreEmpleado,informe,recomendacion,tipoRol);
			System.out.println("Enviando Correo a: "+strNombreEmpleado);

			} catch (Exception e) {
			System.out.println("Error en el envio de correo"+e.getMessage());
				}
			} catch (Exception e) {
			//	e.printStackTrace();
				utilitario.agregarMensajeError("Ha ocurrido un error al envio de correo de asignacion funcionari: ", ""+strNombreEmpleado);
			}

				} 		
	
	
	

	
	public String obtenerNombresApellidosEmpleadoCorreo(String empleado){
		String retornoValor="";
	
		TablaGenerica tabEmpleado=utilitario.consultar("SELECT EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
				"FROM GTH_EMPLEADO EMP  " +
				" WHERE EMP.IDE_GTEMP="+empleado);
		
		return retornoValor=tabEmpleado.getValor("NOMBRES_APELLIDOS");

	}


	public String emailNotificacionAprobado(String strNombreEmpleado ,String informe,String recomendacion) {
        String html ="<p>Estimado(a) "+strNombreEmpleado+", "
                + "</p>\n"
                + "<p>&nbsp;</p>\n"
                + "<p>Notificamos mediante la presente que se le ha asignado la recomendación Nro. "+recomendacion+"  .</p>\n"
                + "<p>&nbsp;</p>\n"
                + "<p>Del informe Nro. "+informe+" .</p>\n"
                + "<p>&nbsp;</p>\n" 
                + "<p>Recuerde que debe ingresar el plan de acción para la recomendación asignada en un lapso de 5 días hábiles </p>\n"
                + "<p>&nbsp;</p>\n"
                + "<p>Saludos cordiales,</p>\n"
                + "<table style=\"height: 144px;\" width=\"571\">\n"
                + "<tbody>\n"
                + "<tr>\n"
                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
                + "<td width=\"476\">\n"
                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
                + "</td>\n"
                + "</tr>\n"
                + "</tbody>\n"
                + "</table>";
        return html;
    }


	
	public String emailLinkEncuestaCorreo(String strNombreEmpleado ,String informe,String recomendacion,int tipoRol) {
        String html ="";
        
        if (tipoRol==1) {
	
        html ="<p>Estimado(a) "+strNombreEmpleado+", "
	                + "</p>\n"
	                + "<p>&nbsp;</p>\n"
	                + "<p>La Gerencia General de la EMGIRS EP, mediante resoluci&oacuten administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute: <strong>Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y en representaci&oacuten de la Gerente General de la Empresa P&uacuteblica Metropolitana de Gesti&oacuten Integral de Residuos S&oacutelidos EMGIRS-EP, realice el seguimiento mensual de las recomendaciones de auditor&iacutea realizadas mediante los diferentes ex&iacutemenes especiales o auditor&iacuteas, e informe a la gerencia de su seguimiento y cumplimiento</strong>. .</p>\n"
	                + "<p>&nbsp;</p>\n" 
	                + "<p>En funci&oacuten a la delegaci&oacuten, la Gerencia de Desarrollo Organizacional ha realizado la difusi&oacuten del Procedimiento interno de Seguimiento y Control de Recomendaciones emitidas por la Contralor&iacutea General del Estado en el que establece: </p>\n"
	                + "<p><strong>El Responsable de la Recomendaci&oacuten y el Servidor designado son responsables de: </strong> </p>\n"
	                + "<p>&nbsp;&nbsp;a)	Dar estricto cumplimiento a lo establecido en la recomendaci&oacuten a su cargo</p>\n"
	                + "<p>&nbsp;&nbsp;b)	Establecer un plan de acci&oacuten por cada recomendaci&oacuten en el sistema e informar mediante memorando al delegado de la M&iacutexima Autoridad conocer y aceptar la responsabilidad de cumplimiento de dicho plan. </p>\n"
	                + "<p><strong>El Responsable de la Recomendaci&oacuten y el Servidor designado son responsables de: </strong> </p>\n"
	                + "<p>&nbsp;&nbsp;c)	Deber&iacuten validar y verificar la pertinencia de la informaci&oacuten previo al registrado en el sistema de recomendaciones </p>\n"
	                + "<p>&nbsp;&nbsp;d)	El registro del avance de la documentaci&oacuten que respalde el cumplimiento de la recomendaci&oacuten, de acuerdo a los tiempos establecido en el Plan.  </p>\n"
	                + "<p>&nbsp;&nbsp;e)	Revisar y mantener actualizado la informaci&oacuten de las recomendaciones que se encuentran cargada en el sistema.  </p>\n"
	                + "<p>&nbsp;&nbsp;f)	Llevar el control de las recomendaciones y su respectivo archivo f&iacutesico y/o digital.   </p>\n"
	                + "<p>&nbsp;&nbsp;g)	Reportar el avance y el cumplimiento de la recomendaci&oacuten a la M&iacutexima Autoridad o su delegado </p>\n"
	                + "<p>&nbsp;</p>\n"
	                + "<p>Con el fin de dar cumplimiento oportuno a las recomendaciones emitidas por la Contralor&iacutea General del Estado, esta Gerencia ha realizado la revisi&oacuten del informe: "+informe+", en el que se&ntildeala:</p>\n"
	                 + "<p>Recomendaci&oacuten No. "+recomendacion+" <p>\n "
	                + "<p>Con este antecedente, y en calidad de Delegado del Seguimiento al Cumplimiento de las Recomendaciones dispongo a Usted el cumplimiento de la recomendaci&oacuten y en un termin&oacute de 5 d&iacuteas se ingrese al Sistema de Recomendaciones el respectivo plan de acci&oacuten para la revisi&oacuten y aceptaci&oacuten por parte de esta Gerencia. ,</p>\n"
	                + "<table style=\"height: 144px;\" width=\"571\">\n"
	                + "<tbody>\n"
	                + "<tr>\n"
	                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	                + "<td width=\"476\">\n"
	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	                + "</td>\n"
	                + "</tr>\n"
	                + "</tbody>\n"
	                + "</table>";
        
        //System.out.println("PRUEBA CORREO1: "+html);
        }else if (tipoRol==2) {
        	 html ="<p>Estimado(a) "+strNombreEmpleado+", "
 	                + "</p>\n"
 	                + "<p>&nbsp;</p>\n"
		            + "<p>Notificamos mediante la presente que se ha eliminado la asignacion de la recomendación Nro. "+recomendacion+" , del informe Nro. "+informe+" .</p>\n"
 	                + "<p>&nbsp;</p>\n"
 	                + "<p>Saludos cordiales,</p>\n"
 	                + "<table style=\"height: 144px;\" width=\"571\">\n"
 	                + "<tbody>\n"
 	                + "<tr>\n"
 	                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
 	                + "<td width=\"476\">\n"
 	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
 	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
 	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
 	                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
 	                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
 	                + "</td>\n"
 	                + "</tr>\n"
 	                + "</tbody>\n"
 	                + "</table>";
        	 //System.out.println("PRUEBA CORREO1: "+html);
		}else if (tipoRol==3) {
			html ="<p>La Gerencia General de la EMGIRS EP, mediante resoluci&oacuten administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute: Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y en representaci&oacuten de la Gerente General de la Empresa P&uacuteblica Metropolitana de Gesti&oacuten Integral de Residuos S&oacutelidos EMGIRS-EP, realice el seguimiento mensual de las recomendaciones de "
	       	 		+ "auditoría realizadas mediante los diferentes exámenes especiales o auditorías, e informe a la gerencia de su seguimiento y cumplimiento."
	                + "</p>\n"
		            + "<p>Como delegado de Gerencia General con el fin de cumplir con lo establecido en el Procedimiento interno de Seguimiento y Control de Recomendaciones notifica que la recomendaci&oacuten "+tab_recomendacion.getValor("numero_serec")+" del informe "+tab_informe.getValor("numero_seinf")+" fue reasignada a  "+strNombreEmpleado+"   .</p>\n"
	                + "<p>&nbsp;</p>\n"
	                + "<table style=\"height: 144px;\" width=\"571\">\n"
	                + "<tbody>\n"
	                + "<tr>\n"
	                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	                + "<td width=\"476\">\n"
	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	                + "</td>\n"
	                + "</tr>\n"
	                + "</tbody>\n"
	                + "</table>";
				//System.out.println("PRUEBA CORREO1: "+html);
		}
	        return html;
	}
	
	
    public void autentificadorParametersSMTP(String usuarioEnvio, String claveCorreo) {
		          usuarioEnvio = usuarioEnvio;
		          claveCorreo = claveCorreo;
		        }
		        
	   

	   private class autentificadorParametersSMTP extends Authenticator {
	 	      private String usuarioEnvio;
	 	      private String claveCorreo;
	 	       public autentificadorParametersSMTP(String usuarioEnvio, String claveCorreo) {
	 	         this.usuarioEnvio = usuarioEnvio;
	 	         this.claveCorreo = claveCorreo;
	 	      }
	   }
	   

	   
	   public String EnviaMailInterno(EnvioMail enviaMail, String mailReceptor, String asunto, String cuerpo, File filearchivo,String strNombreEmpleado ,String informe,String recomendacion,int tipo_rol) throws Exception
	      {
	       String mensaje = "";
	        
	        Properties props = new Properties();
	        props.put("mail.smtp.user", enviaMail.getUsuarioEnvio());
	        props.put("mail.smtp.host", enviaMail.getServidorSMTP());
	        props.put("mail.smtp.port", enviaMail.getPuertoEnvio());
	        props.put("mail.smtp.socketFactory.fallback", "false");
	        props.put("mail.smtp.auth", "false");
	        props.put("mail.smtp.starttls.enable", Boolean.valueOf(false));
	        props.put("mail.smtp.socketFactory.port", enviaMail.getPuertoEnvio());
	        props.put("mail.smtp.ssl.trust", enviaMail.getServidorSMTP());
	        try
	        {
	         Authenticator auth = new autentificadorParametersSMTP(enviaMail.getUsuarioEnvio(), enviaMail.getClaveCorreo());
	         Session session = Session.getInstance(props, auth);
	          BodyPart texto = new MimeBodyPart();
	          MimeMultipart multiParte = new MimeMultipart("related");
	           BodyPart messageBodyPart = new MimeBodyPart();
	           String htmlText="";
	           
	           if (tipo_rol==1) {
		           htmlText="<p>Estimado(a) "+strNombreEmpleado+", "
	   		                + "</p>\n"
	   		                + "<p>&nbsp;</p>\n"
	   	                + "<p>La Gerencia General de la EMGIRS EP, mediante resoluci&oacuten administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute: <strong>Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y en representaci&oacuten de la Gerente General de la Empresa P&uacuteblica Metropolitana de Gesti&oacuten Integral de Residuos S&oacutelidos EMGIRS-EP, realice el seguimiento mensual de las recomendaciones de auditor&iacutea realizadas mediante los diferentes ex&iacutemenes especiales o auditor&iacuteas, e informe a la gerencia de su seguimiento y cumplimiento</strong>. .</p>\n"
	   	                + "<p>&nbsp;</p>\n" 
			                + "<p>En funci&oacuten a la delegaci&oacuten, la Gerencia de Desarrollo Organizacional ha realizado la difusi&oacuten del Procedimiento interno de Seguimiento y Control de Recomendaciones emitidas por la Contralor&iacutea General del Estado en el que establece: </p>\n"
			                + "<p><strong>El Responsable de la Recomendaci&oacuten y el Servidor designado son responsables de: </strong> </p>\n"
			                + "<p>&nbsp;&nbsp;a)	Dar estricto cumplimiento a lo establecido en la recomendaci&oacuten a su cargo</p>\n"
			                + "<p>&nbsp;&nbsp;b)	Establecer un plan de acci&oacuten por cada recomendaci&oacuten en el sistema e informar mediante memorando al delegado de la M&iacutexima Autoridad conocer y aceptar la responsabilidad de cumplimiento de dicho plan. </p>\n"
			                + "<p><strong>El Responsable de la Recomendaci&oacuten y el Servidor designado son responsables de: </strong> </p>\n"
			                + "<p>&nbsp;&nbsp;c)	Deber&iacuten validar y verificar la pertinencia de la informaci&oacuten previo al registrado en el sistema de recomendaciones </p>\n"
			                + "<p>&nbsp;&nbsp;d)	El registro del avance de la documentaci&oacuten que respalde el cumplimiento de la recomendaci&oacuten, de acuerdo a los tiempos establecido en el Plan.  </p>\n"
			                + "<p>&nbsp;&nbsp;e)	Revisar y mantener actualizado la informaci&oacuten de las recomendaciones que se encuentran cargada en el sistema.  </p>\n"
			                + "<p>&nbsp;&nbsp;f)	Llevar el control de las recomendaciones y su respectivo archivo f&iacutesico y/o digital.   </p>\n"
			                + "<p>&nbsp;&nbsp;g)	Reportar el avance y el cumplimiento de la recomendaci&oacuten a la M&iacutexima Autoridad o su delegado </p>\n"
	   		                + "<p>&nbsp;</p>\n"
			                + "<p>Con el fin de dar cumplimiento oportuno a las recomendaciones emitidas por la Contralor&iacutea General del Estado, esta Gerencia ha realizado la revisi&oacuten del informe: "+informe+", en el que se&ntildeala:</p>\n"
	   	                 + "<p>Recomendaci&oacuten No. "+recomendacion+" <p>\n "
			                + "<p>Con este antecedente, y en calidad de Delegado del Seguimiento al Cumplimiento de las Recomendaciones dispongo a Usted el cumplimiento de la recomendaci&oacuten y en un termin&oacute de 5 d&iacuteas se ingrese al Sistema de Recomendaciones el respectivo plan de acci&oacuten para la revisi&oacuten y aceptaci&oacuten por parte de esta Gerencia. ,</p>\n"
	   		                + "<table style=\"height: 144px;\" width=\"571\">\n"
	   		                + "<tbody>\n"
	   		                + "<tr>\n"
	   		                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	   		                + "<td width=\"476\">\n"
			                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
	   		                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	   		                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	   		                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	   		                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	   		                + "</td>\n"
	   		                + "</tr>\n"
	   		                + "</tbody>\n"
	   		                + "</table>";
				 //System.out.println("PRUEBA CORREO1: "+htmlText);
		           
	           }else if (tipo_rol==2) {

		           htmlText="<p>Estimado(a) "+strNombreEmpleado+", "
		   		                + "</p>\n"
		   		                + "<p>&nbsp;</p>\n"
		   		                + "<p>Notificamos mediante la presente que se ha eliminado la asignacion de la recomendación Nro. "+recomendacion+", del informe Nro. "+informe+" .</p>\n"
		   		                + "<p>&nbsp;</p>\n" 
		   		                + "<p>Saludos cordiales,</p>\n"
		   		                + "<table style=\"height: 144px;\" width=\"571\">\n"
		   		                + "<tbody>\n"
		   		                + "<tr>\n"
		   		                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
		   		                + "<td width=\"476\">\n"
		   		                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
		   		                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
		   		                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
		   		                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
		   		                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
		   		                + "</td>\n"
		   		                + "</tr>\n"
		   		                + "</tbody>\n"
		   		                + "</table>";
		           System.out.println("PRUEBA CORREO2: "+htmlText);
			}else if (tipo_rol==3) {
				htmlText ="<p>La Gerencia General de la EMGIRS EP, mediante resoluci&oacuten administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute: Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y en representaci&oacuten de la Gerente General de la Empresa P&uacuteblica Metropolitana de Gesti&oacuten Integral de Residuos S&oacutelidos EMGIRS-EP, realice el seguimiento mensual de las recomendaciones de "
		       	 		+ "auditoría realizadas mediante los diferentes exámenes especiales o auditorías, e informe a la gerencia de su seguimiento y cumplimiento."
		                + "</p>\n"
			            + "<p>Como delegado de Gerencia General con el fin de cumplir con lo establecido en el Procedimiento interno de Seguimiento y Control de Recomendaciones notifica que la recomendaci&oacuten "+tab_recomendacion.getValor("numero_serec")+" del informe "+tab_informe.getValor("numero_seinf")+" fue reasignada a  "+strNombreEmpleado+"   .</p>\n"
			                + "</p>\n"
		                + "<p>El Responsable de la Recomendaci&oacuten y el Servidor designado son responsables de: </p>\n"
			            + "<p>&nbsp;&nbspa)	Dar estricto cumplimiento a lo establecido en la recomendaci&oacuten a su cargo. </p>\n"
			            + "<p>&nbsp;&nbspb)	El registro del avance de la documentaci&oacuten que respalde el cumplimiento de la recomendaci&oacuten, de acuerdo a los tiempos establecido en el Plan.  </p>\n"
			            + "<p>&nbsp;&nbspc)	Revisar y mantener actualizado la informaci&oacuten de las recomendaciones que se encuentran cargada en el sistema.  </p>\n"
			            + "<p>&nbsp;&nbspd)	Llevar el control de las recomendaciones y su respectivo archivo f&iacutesico y/o digital.   </p>\n"
			            + "<p>&nbsp;&nbspe)	Reportar el avance y el cumplimiento de la recomendaci&oacuten a la M&aacutexima Autoridad o su delegado.”  </p>\n"
			                + "<p>&nbsp;</p>\n"
			                + "<table style=\"height: 144px;\" width=\"571\">\n"
			                + "<tbody>\n"
			                + "<tr>\n"
			                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
			                + "<td width=\"476\">\n"
		                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
			                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
			                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
			                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
			                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
			                + "</td>\n"
			                + "</tr>\n"
		                + "</tbody>\n"
		                + "</table>";
				System.out.println("PRUEBA CORREO3: "+htmlText);
			}
	           
	            //String htmlText = cuerpo;
	            messageBodyPart.setContent(htmlText, "text/html");
	            multiParte.addBodyPart(messageBodyPart);
	            	            
	         // second part (the image)
	          /*  messageBodyPart = new MimeBodyPart();
	            DataSource fds = new FileDataSource("D:/soporteTecnico.jpg");
	            messageBodyPart.setDataHandler(new DataHandler(fds));
	            messageBodyPart.setHeader("Content-ID", "<image>");
	            multiParte.addBodyPart(messageBodyPart);
*/
	            MimeMessage message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(enviaMail.getCorreoEnvio()));
	            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailReceptor));
	            message.setSubject(asunto);
	            message.setContent(multiParte);
	          
	            Transport.send(message);
	            System.out.println("Correo enviado exitosamente a: " + mailReceptor);
	        }
	        catch (Exception mex) {
	          System.out.println("Error EnviaEmail: " + mex);
	          mensaje = "Error EnviaEmail: " + mex;
	        }
	        return mensaje;
	      }

	   
	   
	
	
	
	public void validarEliminacion(){
		TablaGenerica tab_consulta=utilitario.consultar("select "
				+ "inf.ide_seinf,susinf.descripcion_sesui,inf.numero_seinf,inf.fecha_aprobacion_seinf,inf.fecha_inicio_seinf,inf.fecha_fin_seinf, "
				+ "rec.numero_serec,rec.asunto_serec, resp.descripcion_seres , emp.descripcion_seemp,  "
				+ "estrec.descripcion_seesr,resp.fecha_seres,usu.nombre_seusu,"
				+ "cast (extract( YEAR from resp.fecha_seres)as integer) as anio,extract( MONTH from resp.fecha_seres) as mes,extract( DAY from resp.fecha_seres) as dia "
				+ "FROM  "
				+ "seg_respuesta resp "
				+ "LEFT JOIN seg_usuario usu on usu.ide_seusu= resp.ide_seusu "
				+ "LEFT JOIN seg_empresa emp ON  emp.ide_seemp = resp.ide_geare "
				+ "LEFT JOIN seg_recomendacion rec ON resp.ide_serec = rec.ide_serec "
				+ "LEFT JOIN seg_informe inf ON rec.ide_seinf = inf.ide_seinf  "
				+ "LEFT JOIN   seg_asignacion asi  ON  resp.ide_serec = rec.ide_serec and  emp.ide_seemp=asi.ide_seemp and  rec.ide_seinf=asi.ide_seinf "
				+ "LEFT JOIN seg_estado_recomendacion estrec ON estrec.ide_seesr = rec.ide_seesr "
				+ "LEFT JOIN seg_suscribe_informe susinf ON inf.ide_sesui = susinf.ide_sesui "
				+ "LEFT JOIN seg_estado_informe estinf ON inf.ide_seesi = estinf.ide_seesi "
				+ "LEFT JOIN seg_tipo_recomendacion tiprec ON rec.ide_setir = tiprec.ide_setir "
				+ "LEFT JOIN seg_tipo_respuesta tresp on tresp.ide_setre = resp.ide_setre " 
				+ "group by inf.ide_seinf,susinf.descripcion_sesui,inf.numero_seinf,inf.fecha_aprobacion_seinf,inf.fecha_inicio_seinf,inf.fecha_fin_seinf, "
				+ "rec.numero_serec,rec.asunto_serec, resp.descripcion_seres , emp.descripcion_seemp, "
				+ "tiprec.descripcion_setir,estrec.descripcion_seesr,resp.fecha_seres,usu.nombre_seusu "
				+ "ORDER BY  inf.ide_seinf");
	}



	public Tabla getTab_asignacion() {
		return tab_asignacion;
	}

	public void setTab_asignacion(Tabla tab_asignacion) {
		this.tab_asignacion = tab_asignacion;
	}


	public Tabla getTab_informe_adjunto() {
		return tab_informe_adjunto;
	}

	public Tabla getTab_informe() {
		return tab_informe;
	}

	public void setTab_informe(Tabla tab_informe) {
		this.tab_informe = tab_informe;
	}

	public Tabla getTab_recomendacion() {
		return tab_recomendacion;
	}

	public void setTab_recomendacion(Tabla tab_recomendacion) {
		this.tab_recomendacion = tab_recomendacion;
	}

	public void setTab_informe_adjunto(Tabla tab_informe_adjunto) {
		this.tab_informe_adjunto = tab_informe_adjunto;
	}
	
	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		try {
			// TODO Auto-generated method stub
			super.siguiente();
			actualizarTablas();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error: "+e);
		}
	}


public void actualizarTablas(){
	tab_asignacion.setCondicion("IDE_SEINF="+tab_informe.getValor("IDE_SEINF")+"and ide_serec="+tab_recomendacion.getValor("IDE_SEREC"));
	tab_asignacion.ejecutarSql();
	utilitario.addUpdate("tab_asignacion");
}

@Override
public void atras() {
	// TODO Auto-generated method stub
	try {
		// TODO Auto-generated method stub
		super.atras();
		actualizarTablas();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("error: "+e);
	}
}



@Override
public void inicio() {
	try {
		super.inicio();
		actualizarTablas();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("error: "+e);
	}
}


@Override
public void fin() {
	// TODO Auto-generated method stub
	try {
		super.fin();
		actualizarTablas();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("error: "+e);
	}
}


@Override
public void actualizar() {
	// TODO Auto-generated method stub
	try {
		super.actualizar();
		actualizarTablas();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("error: "+e);
	}
}


public void validarAsignacion(AjaxBehaviorEvent evt){
	tab_asignacion.modificar(evt); 
	TablaGenerica tab_asignacion_temp=utilitario.consultar("select  ide_seasi, ide_serec, ide_seusu, ide_seemp, ide_seinf, ide_periodo_nuevo_seasi "
		  		+ "FROM seg_asignacion "
		  		+ "where ide_serec ="+tab_recomendacion.getValorSeleccionado() +" "
		  		+ "and ide_seinf="+tab_informe.getValorSeleccionado()+" "
		  		+ "and ide_seemp="+tab_asignacion.getValor("ide_seemp"));
	
	if (tab_asignacion.getValor("ide_seasi")==null || tab_asignacion.getValor("ide_seasi").isEmpty() || tab_asignacion.getValor("ide_seasi").equals("")) {
		 if (tab_asignacion_temp.getTotalFilas()>0) {
				utilitario.agregarMensaje("No se puede asignar","La area seleccionada ya se encuentra ingresada");
				tab_asignacion.setValor("ide_seemp", "");
				utilitario.addUpdateTabla(tab_asignacion, "ide_seemp", "");
				return;
			}
	}else {
		if (tab_asignacion_temp.getTotalFilas()>0) {
			TablaGenerica tab_asignacion_temp_=utilitario.consultar("select  ide_seasi, ide_serec, ide_seusu, ide_seemp, ide_seinf, ide_periodo_nuevo_seasi "
			  		+ "FROM seg_asignacion "
			  		+ "where ide_serec ="+tab_recomendacion.getValorSeleccionado() +" "
			  		+ "and ide_seinf="+tab_informe.getValorSeleccionado()+" "
			  		+ "and ide_seasi="+tab_asignacion.getValor("ide_seasi"));
			
			tab_asignacion.setValor("ide_seemp", tab_asignacion_temp_.getValor("ide_seemp"));
			utilitario.addUpdateTabla(tab_asignacion, "ide_seemp", "");
			utilitario.agregarMensaje("No se puede asignar","La area seleccionada ya se encuentra ingresada");
			return;
			
		}else {
			try {
				
				TablaGenerica tab_asignacion_temp_=utilitario.consultar("select  ide_seasi, ide_serec, ide_seusu, ide_seemp, ide_seinf, ide_periodo_nuevo_seasi "
				  		+ "FROM seg_asignacion "
				  		+ "where ide_serec ="+tab_recomendacion.getValorSeleccionado() +" "
				  		+ "and ide_seinf="+tab_informe.getValorSeleccionado()+" "
				  		+ "and ide_seasi="+tab_asignacion.getValor("ide_seasi"));
				
				String ide_seempAnterior=tab_asignacion_temp_.getValor("ide_seemp");
				String ide_seempNuevo=tab_asignacion.getValor("ide_seemp");

				TablaGenerica TabUsuarioAnterior=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,usua.ide_gtemp "
        		+ "FROM seg_usuario segus "
        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
        		+ "where ide_seemp="+ide_seempAnterior+" and segus.activo_seusu=true "
        		+ "order by segus.ide_seusu");
        						
        		
				TablaGenerica TabUsuario=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,usua.ide_gtemp "
		        		+ "FROM seg_usuario segus "
		        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
		        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
		        		+ "where ide_seemp="+ide_seempNuevo+" and segus.activo_seusu=true "
		        		+ "order by segus.ide_seusu");
				EnviarCorreo(tab_asignacion.getValor("ide_seasi"), tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf"), tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec"),  TabUsuario.getValor("ide_gtemp"),3);
				EnviarCorreo(tab_asignacion.getValor("ide_seasi"), tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf"), tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec"), TabUsuario.getValor("ide_gtemp"),1);
				tab_asignacion.guardar();
				guardarPantalla();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Error en actualizacion de asignacion de recomendaciones");			}
		}	
		
	
	
	}
	
	
	
	
}


 



 
}