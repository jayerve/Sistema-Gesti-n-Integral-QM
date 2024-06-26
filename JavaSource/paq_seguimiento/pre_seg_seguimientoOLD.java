package paq_seguimiento;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import org.primefaces.event.FileUploadEvent;



import paq_comercializacion.ejb.ServicioClientes;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_gestion.ejb.ServicioGestion;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidades.EnvioMail;
import framework.aplicacion.Framework;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Upload;


public class pre_seg_seguimientoOLD extends Pantalla {

	
	private Tabla tab_informe = new Tabla();
	private Tabla tab_asignacion = new Tabla();
	private Tabla tab_recomendacion = new Tabla();
	private Tabla tab_respuesta = new Tabla();
	private Tabla tab_respuesta_historial = new Tabla();
	private Tabla tab_respuesta_adjunto = new Tabla();
	private Tabla tab_respuesta_adjunto_historial = new Tabla();
	private Tabla tab_informe_adjunto = new Tabla();
	private Tabla tab_plan_accion = new Tabla();
	private Framework framework = new Framework();
	private String nombre;
	private Upload upl_archivoFirma=new Upload();

	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioClientes ser_cliente = (ServicioClientes) utilitario.instanciarEJB(ServicioClientes.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	private String carpeta;
	private String control = "Texto";

	String usuario_responsable="",empresa="",empleado="",ide_secar="";	
	boolean bandAccion=false;
	public pre_seg_seguimientoOLD (){
		utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'seg_informe'");
		empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		bandAccion=false;
		String ide_serec="",ide_seinf="";
	 TablaGenerica tab_usuario=utilitario.consultar("SELECT ide_seusu, ide_seemp, ide_secar, nombre_seusu, login_seusu, password_seusu, "
					+ "usu_email, ide_usua, activo_seusu "
					+ "FROM seg_usuario "
					+ "where ide_usua="+utilitario.getVariable("ide_usua")+" and activo_seusu=true ") ;
	 empresa=tab_usuario.getValor("ide_seemp");	
	 usuario_responsable=tab_usuario.getValor("ide_seusu");		 
	 ide_secar=tab_usuario.getValor("ide_secar");
	usuario_responsable=tab_usuario.getValor("ide_seusu");
	 TablaGenerica tab_asignacion;
	 System.out.println("Cargo: "+ide_secar);
	 if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
			}else{
				tab_asignacion=utilitario.consultar("SELECT ide_serec, ide_seinf  "
				 		+ "FROM seg_asignacion  "
				 		+ "where ide_seemp in("+empresa+")  "
				 		+ "group by ide_serec,ide_seinf ");
				tab_asignacion.imprimirSql();
				if (tab_asignacion.getTotalFilas()>0) {
					for (int i = 0; i < tab_asignacion.getTotalFilas(); i++) {
						if (tab_asignacion.getTotalFilas()==1) {
							ide_serec=tab_asignacion.getValor(i,"ide_serec");
							ide_seinf=tab_asignacion.getValor(i,"ide_seinf");
						}else if((tab_asignacion.getTotalFilas()-1)==i){
							ide_serec+=tab_asignacion.getValor(i,"ide_serec");
							ide_seinf+=tab_asignacion.getValor(i,"ide_seinf");	
						}else{
						
						ide_serec+=tab_asignacion.getValor(i,"ide_serec")+",";
						ide_seinf+=tab_asignacion.getValor(i,"ide_seinf")+",";
						}
					}
				}
				
			} 
	 
	
System.out.println("informes_asignadas"+ide_seinf);
System.out.println("recomendaciones_asignadas"+ide_serec);

	 
	 
	 
		Tabulador tab_tabulador = new Tabulador();
	//	tab_tabulador.setStyle("");
		tab_tabulador.setId("tab_tabulador");

		 tab_informe.setId("tab_informe");
	        tab_informe.setTabla("seg_informe", "ide_seinf", 1);
	        
	        tab_informe.getColumna("ide_seinf").setNombreVisual("CODIGO");
	        
	        tab_informe.getColumna("ide_sesui").setCombo("SELECT ide_sesui, descripcion_sesui FROM seg_suscribe_informe");
	        tab_informe.getColumna("ide_sesui").setNombreVisual("SUSCRIBE");
	        tab_informe.getColumna("ide_sesui").setLectura(true);

	        
	        tab_informe.getColumna("ide_seesi").setCombo("SELECT ide_seesi, descripcion_seesi FROM seg_estado_informe");
	        tab_informe.getColumna("ide_seesi").setNombreVisual("ESTADO");
	        tab_informe.getColumna("ide_seesi").setLectura(true);		
	               
	        tab_informe.getColumna("ide_senii").setCombo("SELECT ide_senii, descripcion_senii FROM seg_nivel_informe order by ide_senii desc");
	        tab_informe.getColumna("ide_senii").setNombreVisual("PRIORIDAD");
	        tab_informe.getColumna("ide_senii").setLectura(true);		
	        
	        tab_informe.getColumna("numero_seinf").setNombreVisual("NUMERO");
	        tab_informe.getColumna("numero_seinf").setLongitud(10);

	        tab_informe.getColumna("asunto_seinf").setNombreVisual("ASUNTO");
	        tab_informe.getColumna("asunto_seinf").setAncho(80);
	        tab_informe.getColumna("asunto_seinf").setLongitud(150);
	        tab_informe.getColumna("asunto_seinf").setLectura(true);


	        
	        tab_informe.getColumna("fecha_aprobacion_seinf").setNombreVisual("FECHA APROBACION");
	        tab_informe.getColumna("fecha_aprobacion_seinf").setLectura(true);
	        
	        tab_informe.getColumna("fecha_inicio_seinf").setNombreVisual("FECHA INICIO");
	        tab_informe.getColumna("fecha_inicio_seinf").setLectura(true);
	        
	        tab_informe.getColumna("fecha_fin_seinf").setNombreVisual("FEECHA FIN");
	        tab_informe.getColumna("fecha_fin_seinf").setLectura(true);

	        tab_informe.getColumna("usuario_responsable_seinf").setNombreVisual("USUARIO REGISTRO");		
	        tab_informe.getColumna("usuario_responsable_seinf").setCombo("SELECT  segus.ide_seusu,usua.nick_usua "
	        		+ "FROM seg_usuario segus "
	        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
	        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
	        		+ "order by segus.ide_seusu");
	        tab_informe.getColumna("usuario_responsable_seinf").setAutoCompletar();
	        tab_informe.getColumna("usuario_responsable_seinf").setLectura(true);
	        
	        
	        tab_informe.getColumna("activo_seinf").setCheck();
	        tab_informe.getColumna("activo_seinf").setValorDefecto("true");
	        tab_informe.getColumna("activo_seinf").setNombreVisual("ACTIVO");
	        tab_informe.getColumna("activo_seinf").setLectura(true);

	        tab_informe.getGrid().setColumns(4);
	        tab_informe.setTipoFormulario(true);
	        //tab_informe.setLectura(true);
	        tab_informe.agregarRelacion(tab_informe_adjunto);
	        tab_informe.agregarRelacion(tab_recomendacion);
	       tab_informe.agregarRelacion(tab_plan_accion);
	      
	  	 if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
	  	 
	  	 }else {
		       tab_informe.setCondicion("ide_seinf in("+ide_seinf+")");

		}

	        tab_informe.dibujar();

			PanelTabla pat_panel1=new PanelTabla();
			pat_panel1.setPanelTabla(tab_informe);
			pat_panel1.setMensajeWarn("1.I N F O R M E  ");

			
			
			tab_informe_adjunto.setId("tab_informe_adjunto");
			tab_informe_adjunto.setTabla("seg_adjunto_informe", "ide_seadi", 2);
			tab_informe_adjunto.getColumna("ide_seadi").setNombreVisual("CODIGO");

			tab_informe_adjunto.getColumna("ide_seinf").setVisible(false);
			tab_informe_adjunto.getColumna("activo_seadi").setCheck();
			tab_informe_adjunto.getColumna("activo_seadi").setValorDefecto("true");
			tab_informe_adjunto.getColumna("activo_seadi").setNombreVisual("ACTIVO");
			tab_informe_adjunto.getColumna("fecha_registro_seadi").setNombreVisual("FEC.ADJ");
			tab_informe_adjunto.getColumna("hora_registro_seadi").setNombreVisual("HORA.ADJ");
			tab_informe_adjunto.getColumna("adjunto_seadi").setUpload("seguimiento");
			tab_informe_adjunto.getColumna("adjunto_seadi").setNombreVisual("ADJUNTO");
			tab_informe_adjunto.setLectura(true);
			tab_informe_adjunto.dibujar();
	        PanelTabla pat_panel2= new PanelTabla();
	        pat_panel2.setPanelTabla(tab_informe_adjunto);
			pat_panel2.setMensajeWarn("2. A D J U N T O");
			
			
	        tab_recomendacion.setId("tab_recomendacion");
	        tab_recomendacion.setIdCompleto("tab_tabulador:tab_recomendacion");
	        tab_recomendacion.setTabla("seg_recomendacion", "ide_serec", 3);
	        tab_recomendacion.getColumna("ide_serec").setNombreVisual("CODIGO");
	        tab_recomendacion.getColumna("ide_seinf").setVisible(false);     
	        tab_recomendacion.getColumna("ide_setir").setCombo("SELECT ide_setir, descripcion_setir "
	            	+ "FROM seg_tipo_recomendacion");
	        tab_recomendacion.getColumna("ide_setir").setRequerida(true);
	        tab_recomendacion.getColumna("ide_setir").setNombreVisual("TIPO");
	        tab_recomendacion.getColumna("ide_setir").setRequerida(true);		
            tab_recomendacion.getColumna("ide_seesr").setCombo("SELECT ide_seesr, descripcion_seesr  "
	        		+ "FROM seg_estado_recomendacion");
	        tab_recomendacion.getColumna("ide_seesr").setRequerida(true);
	        tab_recomendacion.getColumna("ide_seesr").setNombreVisual("ESTADO");
	        tab_recomendacion.getColumna("ide_seesr").setRequerida(true);	
	        tab_recomendacion.getColumna("numero_serec").setNombreVisual("NUMERO");
	        tab_recomendacion.getColumna("asunto_serec").setNombreVisual("ASUNTO");
	        tab_recomendacion.getColumna("asunto_serec").setAncho(80);
	        tab_recomendacion.getColumna("asunto_serec").setLongitud(150);
	        tab_recomendacion.getColumna("fecha_aprobacion_serec").setNombreVisual("FECHA APROBACION");
	        tab_recomendacion.getColumna("fecha_inicio_serec").setNombreVisual("FECHA INICIO");
	        tab_recomendacion.getColumna("fecha_fin_serec").setNombreVisual("FEECHA FIN");
	        tab_recomendacion.getColumna("activo_serec").setNombreVisual("ACTIVO");
	        tab_recomendacion.setLectura(true);
	        tab_recomendacion.agregarRelacion(tab_plan_accion);
	       	tab_recomendacion.agregarRelacion(tab_respuesta);
	        tab_recomendacion.agregarRelacion(tab_respuesta_historial);
	   	 if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
		  	 
	  	 }else {
		       tab_recomendacion.setCondicion("ide_serec in("+ide_serec+")");

		}

	        tab_recomendacion.getGrid().setColumns(4);
	        tab_recomendacion.dibujar();
	        
	        PanelTabla pat_panel3 = new PanelTabla();
	        pat_panel3.setPanelTabla(tab_recomendacion);
			pat_panel3.setMensajeWarn("3.R E C O M E N D A C I Ó N");
			

			
			
			tab_plan_accion.setId("tab_plan_accion");
			tab_plan_accion.setIdCompleto("tab_tabulador:tab_plan_accion");
			tab_plan_accion.setTabla("seg_plan_accion", "ide_sepla", 4);
			tab_plan_accion.getColumna("ide_sepla").setNombreVisual("CODIGO");

			tab_plan_accion.getColumna("ide_seusu").setVisible(false);
			tab_plan_accion.getColumna("ide_serec").setVisible(false);
			tab_plan_accion.getColumna("ide_sepla").setOrden(1);
			tab_plan_accion.getColumna("fecha_registro_sepla").setOrden(3);
			tab_plan_accion.getColumna("fecha_registro_sepla").setNombreVisual("FECHA INICIO");
			tab_plan_accion.getColumna("fecha_registro_sepla").setValorDefecto(utilitario.getFechaActual());
			tab_plan_accion.getColumna("fecha_registro_sepla").setLectura(true);
			tab_plan_accion.getColumna("fecha_aprobacion_sepla").setNombreVisual("FECHA APROBACION");
			tab_plan_accion.getColumna("fecha_aprobacion_sepla").setOrden(4);
			tab_plan_accion.getColumna("porcentaje_sepla").setNombreVisual("% AVANCE");
			tab_plan_accion.getColumna("porcentaje_sepla").setFormatoNumero(0);
			tab_plan_accion.getColumna("porcentaje_sepla").setLectura(true);
			tab_plan_accion.getColumna("porcentaje_sepla").setOrden(5);
			tab_plan_accion.getColumna("ide_seesp").setCombo("SELECT ide_seesp, descripcion_seesp  "
	        		+ "FROM seg_estado_plan_accion");
			tab_plan_accion.getColumna("ide_seesp").setRequerida(true);
			tab_plan_accion.getColumna("ide_seesp").setNombreVisual("ESTADO");
			tab_plan_accion.getColumna("ide_seesp").setOrden(6);
			tab_plan_accion.getColumna("ide_seper").setNombreVisual("PERIOCIDAD MESES");
			
			//tab_plan_accion.getColumna("ide_seper").setCombo(lista);
			
			tab_plan_accion.getColumna("ide_seper").setCombo("SELECT ide_seper, descripcion_seper "
					+ "FROM seg_periocidad");

			

			
			
			tab_plan_accion.getColumna("ide_seper").setRequerida(true);
			tab_plan_accion.getColumna("ide_seper").setOrden(7);
			tab_plan_accion.getColumna("asunto_sepla").setNombreVisual("ASUNTO");
			tab_plan_accion.getColumna("asunto_sepla").setOrden(2);

			
			tab_plan_accion.getColumna("activo_sepla").setCheck();
			tab_plan_accion.getColumna("activo_sepla").setValorDefecto("true");
			tab_plan_accion.getColumna("activo_sepla").setNombreVisual("ACTIVO");
			tab_plan_accion.getColumna("activo_sepla").setOrden(8);

			tab_plan_accion.agregarRelacion(tab_respuesta);
			
			
			tab_plan_accion.dibujar();
	        PanelTabla pat_panel4= new PanelTabla();
	        pat_panel4.setPanelTabla(tab_plan_accion);
			pat_panel4.setMensajeWarn("4. PLAN_ACCION");
			
			
			tab_respuesta.setId("tab_respuesta");
			tab_respuesta.setIdCompleto("tab_tabulador:tab_respuesta");
			tab_respuesta.setTabla("seg_respuesta", "ide_seres", 5);
			tab_respuesta.getColumna("ide_seres").setNombreVisual("CODIGO");
			tab_respuesta.getColumna("ide_seres").setOrden(1);
			tab_respuesta.getColumna("ide_serec").setVisible(false);

			tab_respuesta.getColumna("ide_setre").setNombreVisual("TIPO");
			if (ide_secar.equals("1") || empresa.equals("1") || empresa.equals("2")) {
				tab_respuesta.getColumna("ide_setre").setCombo("SELECT ide_setre, descripcion_setre "
						+ "FROM seg_tipo_respuesta"); 
				tab_respuesta.getColumna("ide_setre").setLectura(false);
			}else {
				tab_respuesta.getColumna("ide_setre").setCombo("SELECT ide_setre, descripcion_setre "
						+ "FROM seg_tipo_respuesta where ide_setre=1 "); 
				tab_respuesta.getColumna("ide_setre").setLectura(true);

			}
			tab_respuesta.getColumna("ide_setre").setOrden(2);

			
			
			tab_respuesta.getColumna("ide_setre").setMetodoChange("cambioEstadoAccion");
			
			
			
			tab_respuesta.getColumna("descripcion_seres").setNombreVisual("ACTIVIDADES_PROGRAMADAS");
			tab_respuesta.getColumna("descripcion_seres").setOrden(3);
			
			tab_respuesta.getColumna("medio_verificacion_seres").setNombreVisual("MEDIO_VERIFICACION");
			tab_respuesta.getColumna("medio_verificacion_seres").setOrden(3);
			
			
			tab_respuesta.getColumna("fecha_desde_seres").setNombreVisual("FECHA.INICIO");
			tab_respuesta.getColumna("fecha_desde_seres").setRequerida(true);		
			tab_respuesta.getColumna("fecha_desde_seres").setOrden(4);
			tab_respuesta.getColumna("fecha_hasta_seres").setNombreVisual("FECHA.FIN");
			tab_respuesta.getColumna("fecha_hasta_seres").setRequerida(true);
			tab_respuesta.getColumna("fecha_hasta_seres").setOrden(5);
	
			
			
			tab_respuesta.getColumna("ide_seemp").setNombreVisual("AREA");		
	        tab_respuesta.getColumna("ide_seemp").setCombo("SELECT ide_seemp,descripcion_seemp "
	        		+ "FROM seg_empresa where  activo_seemp=true");
			tab_respuesta.getColumna("ide_seemp").setLectura(true);		
			tab_respuesta.getColumna("ide_seemp").setOrden(6);

			
			
			tab_respuesta.getColumna("ide_gtemp_responsable").setNombreVisual("USUARIO_RESPONSABLE");		
	        tab_respuesta.getColumna("ide_gtemp_responsable").setCombo("SELECT  emp.ide_gtemp, "
	        		+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
	        		+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
	        		+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
	        		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS  "
	        		+ "FROM gth_empleado emp  "
	        		+ "where emp.activo_gtemp=true  "
	        		+ "order by APELLIDO_PATERNO_GTEMP,APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP");
	        
	        tab_respuesta.getColumna("ide_gtemp_responsable").setAutoCompletar();
			tab_respuesta.getColumna("ide_gtemp_responsable").setOrden(7);


		

			
			tab_respuesta.getColumna("hora_seres").setNombreVisual("HORA");
			tab_respuesta.getColumna("hora_seres").setValorDefecto(utilitario.getHoraActual());
			tab_respuesta.getColumna("hora_seres").setLectura(true);
			tab_respuesta.getColumna("hora_seres").setOrden(8);

			tab_respuesta.getColumna("fecha_seres").setNombreVisual("FEC_REG_ACTIVIDAD");
			tab_respuesta.getColumna("fecha_seres").setValorDefecto(utilitario.getFechaActual());
			tab_respuesta.getColumna("fecha_seres").setLectura(true);
			tab_respuesta.getColumna("fecha_seres").setOrden(9);

		
			
			tab_respuesta.getColumna("ide_usua").setNombreVisual("USUARIO REGISTRO");		
	        tab_respuesta.getColumna("ide_usua").setCombo("SELECT  segus.ide_seusu,usua.nick_usua "
	        		+ "FROM seg_usuario segus "
	        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
	        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
	        		+ "order by segus.ide_seusu");
	        tab_respuesta.getColumna("ide_usua").setAutoCompletar();
	        tab_respuesta.getColumna("ide_usua").setVisible(false);



	
			tab_respuesta.getColumna("ide_seinf").setVisible(false);
			tab_respuesta.getColumna("ide_sepla").setVisible(false);

	

			tab_respuesta.getColumna("activo_seres").setCheck();
			tab_respuesta.getColumna("activo_seres").setValorDefecto("true");
			tab_respuesta.getColumna("activo_seres").setNombreVisual("ACTIVO");
			tab_respuesta.getColumna("activo_seres").setOrden(10);

			

			


			
			
			
	
	        
			
			
			tab_respuesta.agregarRelacion(tab_respuesta_adjunto);
			tab_respuesta.setCondicion("ide_sepla=-1");

			tab_respuesta.dibujar();
	        PanelTabla pat_panel5= new PanelTabla();
	        pat_panel5.setPanelTabla(tab_respuesta);
			pat_panel5.setMensajeWarn("5. ACCIONES");

			
		

			
			tab_respuesta_adjunto.setId("tab_respuesta_adjunto");
			tab_respuesta_adjunto.setIdCompleto("tab_tabulador:tab_respuesta_adjunto");
			tab_respuesta_adjunto.setTabla("seg_adjunto_respuesta", "ide_seadr", 6);
			tab_respuesta_adjunto.getColumna("ide_seadr").setNombreVisual("CODIGO");
			tab_respuesta_adjunto.getColumna("ide_seres").setVisible(false);
			tab_respuesta_adjunto.getColumna("adjunto_seadr").setUpload("seguimiento");
			tab_respuesta_adjunto.getColumna("adjunto_seadr").setNombreVisual("ADJUNTO");
			tab_respuesta_adjunto.getColumna("fecha_registro_seadr").setNombreVisual("FECHA.REGISTRO");
			tab_respuesta_adjunto.getColumna("fecha_registro_seadr").setValorDefecto(utilitario.getFechaActual());
			tab_respuesta_adjunto.getColumna("fecha_registro_seadr").setLectura(true);

			tab_respuesta_adjunto.getColumna("hora_registro_seadr").setNombreVisual("HORA.REGISTRO");
			tab_respuesta_adjunto.getColumna("hora_registro_seadr").setLectura(true);
			tab_respuesta_adjunto.getColumna("hora_registro_seadr").setValorDefecto(utilitario.getHoraActual());

			tab_respuesta_adjunto.getColumna("activo_seadr").setCheck();
			tab_respuesta_adjunto.getColumna("activo_seadr").setValorDefecto("true");
			tab_respuesta_adjunto.getColumna("activo_seadr").setNombreVisual("ACTIVO");
			
			
			
			
			
			//tab_respuesta_adjunto.setLectura(true);
			tab_respuesta_adjunto.dibujar();
	        PanelTabla pat_panel6= new PanelTabla();
	        pat_panel6.setPanelTabla(tab_respuesta_adjunto);
			pat_panel6.setMensajeWarn("6.RESP.ADJUNTO");

			
			
			tab_respuesta_historial.setId("tab_respuesta_historial");
			tab_respuesta_historial.setIdCompleto("tab_tabulador:tab_respuesta_historial");
			tab_respuesta_historial.setTabla("seg_respuesta", "ide_seres", 7);
			tab_respuesta_historial.getColumna("ide_seres").setNombreVisual("CODIGO");
			tab_respuesta_historial.getColumna("ide_serec").setVisible(false);
			tab_respuesta_historial.getColumna("descripcion_seres").setNombreVisual("DESCRIPCIÓN");
			tab_respuesta_historial.getColumna("hora_seres").setNombreVisual("HORA");
			tab_respuesta_historial.getColumna("hora_seres").setValorDefecto(utilitario.getHoraActual());
			tab_respuesta_historial.getColumna("hora_seres").setLectura(true);
		
			
			tab_respuesta_historial.getColumna("ide_usua").setNombreVisual("USUARIO REGISTRO");		
			tab_respuesta_historial.getColumna("ide_usua").setCombo("SELECT  segus.ide_seusu,usua.nick_usua "
	        		+ "FROM seg_usuario segus "
	        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
	        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
	        		+ "order by segus.ide_seusu");
			tab_respuesta_historial.getColumna("ide_usua").setAutoCompletar();			
			
			tab_respuesta_historial.getColumna("ide_seemp").setNombreVisual("EMPRESA");		
			tab_respuesta_historial.getColumna("ide_seemp").setCombo("SELECT ide_seemp,descripcion_seemp "
	        		+ "FROM seg_empresa where  activo_seemp=true");
			tab_respuesta_historial.getColumna("ide_seemp").setAutoCompletar();			

			
			tab_respuesta_historial.getColumna("ide_setre").setNombreVisual("TIPO");
			tab_respuesta_historial.getColumna("ide_setre").setCombo("SELECT ide_setre, descripcion_setre "
					+ "FROM seg_tipo_respuesta"); 

			tab_respuesta_historial.getColumna("ide_seinf").setVisible(false);
			tab_respuesta_historial.getColumna("ide_sepla").setVisible(false);

			tab_respuesta_historial.getColumna("activo_seres").setCheck();
			tab_respuesta_historial.getColumna("activo_seres").setValorDefecto("true");
			tab_respuesta_historial.getColumna("activo_seres").setNombreVisual("ACTIVO");
			tab_respuesta_historial.getColumna("fecha_seres").setNombreVisual("FECHA APROBACION");
			tab_respuesta_historial.getColumna("fecha_seres").setValorDefecto(utilitario.getFechaActual());
			tab_respuesta_historial.getColumna("fecha_seres").setLectura(true);
			tab_respuesta_historial.setLectura(true);
			tab_respuesta_historial.agregarRelacion(tab_respuesta_adjunto_historial);
			tab_respuesta_historial.setCondicion("ide_sepla is null");
			tab_respuesta_historial.dibujar();
	        PanelTabla pat_panel7= new PanelTabla();
	        pat_panel7.setPanelTabla(tab_respuesta_historial);
			pat_panel7.setMensajeWarn("7. HISTORIAL ACCIONES");

			
			tab_respuesta_adjunto_historial.setId("tab_respuesta_adjunto_historial");
			tab_respuesta_adjunto_historial.setIdCompleto("tab_tabulador:tab_respuesta_adjunto_historial");
			tab_respuesta_adjunto_historial.setTabla("seg_adjunto_respuesta", "ide_seadr", 8);
			tab_respuesta_adjunto_historial.getColumna("ide_seadr").setNombreVisual("CODIGO");
			tab_respuesta_adjunto_historial.getColumna("ide_seres").setVisible(false);
			tab_respuesta_adjunto_historial.getColumna("adjunto_seadr").setUpload("seguimiento");
			tab_respuesta_adjunto_historial.getColumna("adjunto_seadr").setNombreVisual("ADJUNTO");
			tab_respuesta_adjunto_historial.getColumna("fecha_registro_seadr").setNombreVisual("FECHA.REGISTRO");
			tab_respuesta_adjunto_historial.getColumna("fecha_registro_seadr").setValorDefecto(utilitario.getFechaActual());
			tab_respuesta_adjunto_historial.getColumna("fecha_registro_seadr").setLectura(true);
			tab_respuesta_adjunto_historial.getColumna("hora_registro_seadr").setNombreVisual("HORA.REGISTRO");
			tab_respuesta_adjunto_historial.getColumna("hora_registro_seadr").setLectura(true);
			tab_respuesta_adjunto_historial.getColumna("hora_registro_seadr").setValorDefecto(utilitario.getHoraActual());
			tab_respuesta_adjunto_historial.getColumna("activo_seadr").setCheck();
			tab_respuesta_adjunto_historial.getColumna("activo_seadr").setValorDefecto("true");
			tab_respuesta_adjunto_historial.getColumna("activo_seadr").setNombreVisual("ACTIVO");
			tab_respuesta_adjunto_historial.setLectura(true);
			tab_respuesta_adjunto_historial.dibujar();
	        PanelTabla pat_panel8= new PanelTabla();
	        pat_panel8.setPanelTabla(tab_respuesta_adjunto_historial);
			pat_panel8.setMensajeWarn("8.RESP ADJUNTO HISTORIAL");
 
			//Tabuladores de tablas
			tab_tabulador.agregarTab("3.RECOMENDACIÓN", pat_panel3);
			tab_tabulador.agregarTab("4.PLAN.ACCION", pat_panel4);
			tab_tabulador.agregarTab("5.ACCIÓN", pat_panel5);
			tab_tabulador.agregarTab("6.ACCIÓN.ADJUNTO", pat_panel6);
			tab_tabulador.agregarTab("7.HISTORIAL DE ACCIONES", pat_panel7);
			tab_tabulador.agregarTab("8.ACCIÓN.ADJUNTO HISTORIAL", pat_panel8);

			
		
			Division div_division = new Division();
			div_division.setId("div_division");
			div_division.dividir2(pat_panel1,pat_panel2,"50%","V");
			//agregarComponente(div_division);
			Division div_division2=new Division();
			div_division2.setId("div_division2");
			div_division2.dividir2(div_division,tab_tabulador,"50%","H");
			agregarComponente(div_division2);
	}
	
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
		TablaGenerica tab=utilitario.consultar("SELECT ide_serec, ide_seinf, ide_setir, ide_seesr, asunto_serec, numero_serec, "
				+ "fecha_inicio_serec, fecha_fin_serec, fecha_aprobacion_serec,  "
				+ "activo_serec "
				+ "FROM seg_recomendacion where ide_seesr in (2,3) and ide_serec="+tab_recomendacion.getValorSeleccionado());
		tab.imprimirSql();
		if (tab.getTotalFilas()>0) {
			utilitario.agregarMensajeInfo("No se puede realizar esta acción", "La Recomendación se encuentra en estado de Cumplida");
			return;
		}else {
			
		}
		
		if(tab_plan_accion.isFocus()){
			//Validacion 
			tab_plan_accion.insertar();		
		   	tab_plan_accion.setValor("ide_seusu", usuario_responsable);
		   	tab_plan_accion.setValor("ide_serec",  tab_recomendacion.getValorSeleccionado());
		   	tab_plan_accion.setValor("ide_seinf",  tab_informe.getValorSeleccionado());


		}else if(tab_respuesta.isFocus()){
			if (tab_plan_accion.getTotalFilas()>0) {	
				tab_respuesta.insertar();
				tab_respuesta.setValor("ide_usua", usuario_responsable);	
				tab_respuesta.setValor("ide_serec", tab_recomendacion.getValorSeleccionado());	
				tab_respuesta.setValor("ide_sepla", tab_plan_accion.getValorSeleccionado());	
				tab_respuesta.setValor("ide_seinf",  tab_informe.getValorSeleccionado());

			/*TablaGenerica tab_empresa=utilitario.consultar("SELECT ide_seemp, gen_ide_seemp, descripcion_seemp, "
					+ "sigla_seemp, ems_subarea,ems_mostrar "
					+ "FROM seg_empresa "
					+ "where ide_seemp="+empresa);*/

			tab_respuesta.setValor("ide_seemp", empresa);	



			}
				
			}else if(tab_respuesta_adjunto.isFocus()){
			//	if (tab_respuesta.getTotalFilas()>0) {
					tab_respuesta_adjunto.insertar();
				//}
				
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "No existe datos del Empleado Departamento");
			}
		
		
	}

	
	
	
	@Override
	public void guardar() {
	
		if (tab_plan_accion.isFocus()) {
			if (tab_plan_accion.guardar()) {
	        	guardarPantalla();    	
	        if ( tab_plan_accion.getValor("ide_sepla")!=null ) {
				
			} else {	
	        	try {
					EnviarCorreoAccion(tab_plan_accion.getValor("ide_sepla"), tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf"), tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec"),
							" Asunto: "+tab_respuesta.getValor("descripcion_seres"),empleado,4);
				
					System.out.println("Plan de accion ingresado "+tab_plan_accion.getValor("ide_sepla")+","+ tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf")+", "+tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec")+","+
		        			tab_respuesta.getValor("ide_seres")+" Asunto: "+tab_respuesta.getValor("descripcion_seres"));

	        	} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	System.out.println("Accion ingresada "+tab_plan_accion.getValor("ide_sepla")+","+ tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf")+", "+tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec")+","+
	        			tab_respuesta.getValor("ide_seres")+" Asunto: "+tab_respuesta.getValor("descripcion_seres"));

	    	}
			}
		}
	  	
		if (tab_respuesta.isFocus()) {
			 TablaGenerica tab_consulta =utilitario.consultar("select ide_seres,ide_sepla from seg_respuesta where ide_seres="+tab_respuesta.getValor("ide_seres"));
	  		 	System.out.println("Registro de nueva accion "+tab_respuesta.getValor("ide_seres"));
	  		 	boolean actualizar=false;
	  		 	if (tab_respuesta.getTotalFilas()>0 ) {
	  		 		actualizar=false;
	  		 	}else {
					actualizar=true;
				}
			if (tab_respuesta.guardar()) {
	  		
      	if (actualizar==true ) {
                	guardarPantalla();    	
        			updateAvanceCumplimiento(1);	
    	}else {
			
		try {
			
	EnviarCorreoAccion(tab_plan_accion.getValor("ide_sepla"), tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf"), tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec"),
			" Asunto: "+tab_respuesta.getValor("descripcion_seres"),empleado,2);
	System.out.println("Accion ingresada "+tab_plan_accion.getValor("ide_sepla")+","+ tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf")+", "+tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec")+","+
			tab_respuesta.getValor("ide_seres")+" Asunto: "+tab_respuesta.getValor("descripcion_seres"));

		} catch (Exception e) {
			System.out.println("Error envio Correo electronico accion: "+tab_plan_accion.getValor("ide_sepla")+","+ tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf")+", "+tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec")+","+
			tab_respuesta.getValor("ide_seres")+" Asunto: "+tab_respuesta.getValor("descripcion_seres"));
			}
	  	 }
	
        	guardarPantalla();    	
			}
    	}
    	
		if(tab_respuesta_adjunto.isFocus()){
	  	 if (tab_respuesta_adjunto.guardar()) {
        	guardarPantalla();    	
try {
	EnviarCorreoAccion(tab_plan_accion.getValor("ide_sepla"), tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf"), tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec"),
	        			" Asunto: "+tab_respuesta.getValor("descripcion_seres"),empleado,3);
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
        	
	  	 
	  	 
	  	 }
		}

	}



	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}



	public ServicioGestion getSer_gestion() {
		return ser_gestion;
	}

	public void setSer_gestion(ServicioGestion ser_gestion) {
		this.ser_gestion = ser_gestion;
	}

	public ServicioFacturacion getSer_facturacion() {
		return ser_facturacion;
	}

	public void setSer_facturacion(ServicioFacturacion ser_facturacion) {
		this.ser_facturacion = ser_facturacion;
	}

	public ServicioClientes getSer_cliente() {
		return ser_cliente;
	}

	public void setSer_cliente(ServicioClientes ser_cliente) {
		this.ser_cliente = ser_cliente;
	}

	public ServicioSeguridad getSer_seguridad() {
		return ser_seguridad;
	}

	public void setSer_seguridad(ServicioSeguridad ser_seguridad) {
		this.ser_seguridad = ser_seguridad;
	}
	

	public ServicioContabilidad getSer_contabilidad() {
		return ser_contabilidad;
	}

	public void setSer_contabilidad(ServicioContabilidad ser_contabilidad) {
		this.ser_contabilidad = ser_contabilidad;
	}
	
	
		
	public void actualizarTablas(){

		tab_respuesta.setCondicion("and IDE_SEINF="+tab_informe.getValor("IDE_SEINF")+""
				+ " AND IDE_SEREC="+tab_recomendacion.getValor("IDE_SEREC"));
		tab_respuesta.ejecutarSql();		
		tab_respuesta_adjunto.setCondicion("IDE_SERES="+tab_respuesta.getValor("IDE_SERES"));
		tab_respuesta_historial.setCondicion("ide_serec="+tab_recomendacion.getValor("ide_serec")+"and ide_sepla is null" );
		tab_respuesta_adjunto_historial.setCondicion("ide_seres="+tab_respuesta_historial.getValor("ide_seres"));
		tab_respuesta.ejecutarSql();
		tab_respuesta_adjunto.ejecutarSql();
		tab_respuesta_historial.ejecutarSql();
		tab_respuesta_adjunto_historial.ejecutarSql();
		utilitario.addUpdate("tab_tabulador,tab_tabulador:tab_respuesta,tab_tabulador:tab_respuesta_adjunto,tab_tabulador:tab_respuesta_historial,tab_tabulador:tab_respuesta_adjunto_historial");
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


	
	

public void updateAvanceCumplimiento(int tipo_correo){
	//tab_plan_accion.modificar(evt); 

	String ide_recomendacion_seleccionada="",ide_plan_accion="";
	ide_recomendacion_seleccionada=tab_recomendacion.getValorSeleccionado();
	ide_plan_accion=tab_plan_accion.getValorSeleccionado();

	System.out.println(ide_recomendacion_seleccionada+" :: "+ide_plan_accion);
	
	
	  TablaGenerica tab_avance=utilitario.consultar("SELECT  ide_serec,count(ide_serec) as avance "
      		+ "FROM seg_respuesta "
      		+ "where ide_serec="+ide_recomendacion_seleccionada+" and ide_sepla="+ide_plan_accion+" "
      		+ "group by ide_serec ");
    	  
      int num_respuestas=Integer.parseInt(tab_avance.getValor("avance"));
      		
      TablaGenerica tab_avance_aprobadas=utilitario.consultar("SELECT  ide_serec,count(ide_serec) as avance "
      		+ "FROM seg_respuesta "
      		+ "where ide_serec="+tab_recomendacion.getValorSeleccionado()+" and ide_sepla="+ide_plan_accion+" "
      		+ " and ide_setre=5 " 
      		+ " group by ide_serec ");
      
      tab_avance_aprobadas.imprimirSql();
      int num_respuestas_aprobadas=0;
      if (tab_avance_aprobadas.getTotalFilas()>0) {
       num_respuestas_aprobadas=Integer.parseInt(tab_avance_aprobadas.getValor("avance"));
      }else {
    	  num_respuestas_aprobadas=0;
	}
      

    	  double valorAvance=(num_respuestas_aprobadas*100/num_respuestas);
    	  
utilitario.getConexion().ejecutarSql("update seg_plan_accion set porcentaje_sepla="+utilitario.getFormatoNumero(valorAvance,2)+" where ide_sepla=" + ide_plan_accion);
utilitario.addUpdateTabla(tab_plan_accion, "porcentaje_sepla", "");
utilitario.addUpdate("tab_tabulador:tab_plan_accion");


try {
	System.out.println("Actualizar estado de accion: "+tab_plan_accion.getValor("ide_sepla")+","+ tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf")+", "+tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec")+","+
			tab_respuesta.getValor("ide_seres")+" Asunto: "+tab_respuesta.getValor("descripcion_seres"));
	//return;s
	EnviarCorreoAccion(tab_plan_accion.getValor("ide_sepla"), tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf"), tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec"),
			" Asunto: "+tab_respuesta.getValor("descripcion_seres"),empleado,1);
	
	bandAccion=false;
} catch (Exception e) {
	// TODO Auto-generated catch block
	System.out.println("Correo Cambio de estado accion inválido");
}


}


public void cambioEstadoAccion(AjaxBehaviorEvent evt){
	tab_respuesta.modificar(evt); 
	bandAccion=true;
}




public Tabla getTab_informe() {
	return tab_informe;
}


public void setTab_informe(Tabla tab_informe) {
	this.tab_informe = tab_informe;
}


public Tabla getTab_asignacion() {
	return tab_asignacion;
}


public void setTab_asignacion(Tabla tab_asignacion) {
	this.tab_asignacion = tab_asignacion;
}


public Tabla getTab_recomendacion() {
	return tab_recomendacion;
}


public void setTab_recomendacion(Tabla tab_recomendacion) {
	this.tab_recomendacion = tab_recomendacion;
}


public Tabla getTab_respuesta() {
	return tab_respuesta;
}


public void setTab_respuesta(Tabla tab_respuesta) {
	this.tab_respuesta = tab_respuesta;
}


public Tabla getTab_respuesta_adjunto() {
	return tab_respuesta_adjunto;
}


public void setTab_respuesta_adjunto(Tabla tab_respuesta_adjunto) {
	this.tab_respuesta_adjunto = tab_respuesta_adjunto;
}


public Tabla getTab_informe_adjunto() {
	return tab_informe_adjunto;
}


public void setTab_informe_adjunto(Tabla tab_informe_adjunto) {
	this.tab_informe_adjunto = tab_informe_adjunto;
}


public Tabla getTab_plan_accion() {
	return tab_plan_accion;
}


public void setTab_plan_accion(Tabla tab_plan_accion) {
	this.tab_plan_accion = tab_plan_accion;
}


public String getUsuario_responsable() {
	return usuario_responsable;
}


public void setUsuario_responsable(String usuario_responsable) {
	this.usuario_responsable = usuario_responsable;
}


public String getEmpresa() {
	return empresa;
}


public void setEmpresa(String empresa) {
	this.empresa = empresa;
}


public Tabla getTab_respuesta_historial() {
	return tab_respuesta_historial;
}


public void setTab_respuesta_historial(Tabla tab_respuesta_historial) {
	this.tab_respuesta_historial = tab_respuesta_historial;
}


public Tabla getTab_respuesta_adjunto_historial() {
	return tab_respuesta_adjunto_historial;
}


public void setTab_respuesta_adjunto_historial(
		Tabla tab_respuesta_adjunto_historial) {
	this.tab_respuesta_adjunto_historial = tab_respuesta_adjunto_historial;
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
    String html ="<p>Estimado, "
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



public String emailLinkEncuestaCorreo(String strNombreEmpleado ,String ide_sepla,String informe, String recomendacion, String acciones,String ide_gtemp, int tipo_rol) {
	 
	String html
	 ="<p>Estimado "+strNombreEmpleado+ ", "
             + "</p>\n"
             + "<p>&nbsp;</p>\n";
    
	if (tipo_rol==1) {
	//Tipo rol cambio estado accion
		html+="<p> Notificamos mediante la presente que se ha cambiado el estado del Plan de Accion para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" .</p>\n"
	             + "<p>&nbsp;</p>\n" 
	             + "<p>Recuerde el cumplimiento del plan de accion debe cumplirse en las fechas  ingresadas </p>\n"
	             + "<p>&nbsp;</p>\n"
	             + "<p>Saludos cordiales,</p>\n";
	}
	else if (tipo_rol==2) {
	//Tipo rol registro de nueva accion	
        html+="<p>Notificamos mediante la presente que se ha registrado la siguiente accion : <strong>"+acciones+" .</strong> en el Plan de Accion Nro.:"+ide_sepla+" De la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
        + "<p>&nbsp;</p>\n" 
        + "<p>Recuerde que debe registrar la documentacion que valide la accion registrada.</p>\n"
        + "<p>&nbsp;</p>\n"
        + "<p>Saludos cordiales,</p>\n";

	}
	else if (tipo_rol==3) {
	//Tipo rol adjunto accion
		
        html+="<p>Notificamos mediante la presente que se ha registrado la documentacion de la accion : <strong>"+acciones+" .</strong> del Plan de Accion Nro.:"+ide_sepla+" De la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
       + "<p>&nbsp;</p>\n" 
        + "<p>Recuerde que debe registrar las acciones que realizara para el cumplimiento del Plan de Accion </p>\n"
        + "<p>&nbsp;</p>\n"
        + "<p>Saludos cordiales,</p>\n";

	}	
	else if (tipo_rol==4) {
	//Plan de accion
		html+="<p>Notificamos mediante la presente que se ha ingresado el Plan de Accion Nro.:"+ide_sepla+"  De la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" .</p>\n"
	             + "<p>&nbsp;</p>\n" 
	             + "<p>Recuerde que debe registrar las acciones que realizara para el cumplimiento del Plan de Accion en un lapso de 5 dias habiles</p>\n"
	             + "<p>&nbsp;</p>\n"
	             + "<p>Saludos cordiales,</p>\n";
	}

	
	    html+="<table style=\"height: 144px;\" width=\"571\">\n"
             + "<tbody>\n"
             + "<tr>\n"
             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
             + "<td width=\"476\">\n"
             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>PLANIFICACIÓN DE PROCESOS Y PROYECTOS</strong></p>\n"
             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
             + "</td>\n"             + "<p>Notificamos mediante la presente que se ha ingresado al Plan de Accion Nro.:"+ide_sepla+" la siguiente accion: <strong>"+acciones+" .</strong></p>\n"

             + "</tr>\n"
             + "</tbody>\n"
             + "</table>";
        return html;
}


public String emailLinkEncuestaCorreoAccion(String strNombreEmpleado ,String ide_sepla,String informe, String recomendacion, String acciones,String ide_gtemp) {
	 String html ="<p>Estimado "+strNombreEmpleado+ ", "
            + "</p>\n"
            + "<p>&nbsp;</p>\n"
            + "<p>Notificamos mediante la presente que se ha ingresado al Plan de Accion Nro.:"+ide_sepla+" la siguiente accion: <strong>"+acciones+" .</strong></p>\n"

            + "<p>Notificamos mediante la presente que se ha ingresado la documentacion para  al Plan de Accion Nro.:"+ide_sepla+" la siguiente accion: <strong>"+acciones+" .</strong></p>\n"
            + "<p> De la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" .</p>\n"
            + "<p>&nbsp;</p>\n" 
            + "<p>Recuerde el cumplimiento del plan de accion debe cumplirse en las fechas  ingresadas </p>\n"
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
   

   
   public String EnviaMailInterno(EnvioMail enviaMail, String mailReceptor, String asunto, String cuerpo, File filearchivo,String strNombreEmpleado ,
		   String ide_sepla,String informe, String recomendacion, String acciones,String ide_gtemp) throws Exception
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
           String htmlText 
           ="<p>Estimado "+strNombreEmpleado+ ", "
                   + "</p>\n"
                   + "<p>&nbsp;</p>\n"
                   + "<p>Notificamos mediante la presente que se ha ingresado el plan de accion:<strong> "+acciones+" .</strong></p>\n"
                   + "<p> De la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" .</p>\n"
                   + "<p>&nbsp;</p>\n" 
                   + "<p>Recuerde el cumplimiento del plan de accion debe cumplirse en las fechas  ingresadas </p>\n"
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

           
            //String htmlText = cuerpo;
            messageBodyPart.setContent(htmlText, "text/html");
            multiParte.addBodyPart(messageBodyPart);
            	            
         // second part (the image)
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource("D:/soporteTecnico.jpg");
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");
            multiParte.addBodyPart(messageBodyPart);

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

   
   public String EnviaMailInternoAccion(EnvioMail enviaMail, String mailReceptor, String asunto, String cuerpo, File filearchivo,String strNombreEmpleado ,
		   String ide_sepla,String informe, String recomendacion, String acciones,String ide_gtemp, int tipo_correo) throws Exception
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
           String htmlText 
              	 ="<p>Estimado "+strNombreEmpleado+ ", "
                           + "</p>\n"
                           + "<p>&nbsp;</p>\n";
                  
           if (tipo_correo==1) {
        		//Tipo rol cambio estado accion
        			htmlText+="<p> Notificamos mediante la presente que se ha cambiado el estado del Plan de Accion para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" .</p>\n"
        		             + "<p>&nbsp;</p>\n" 
        		             + "<p>Recuerde el cumplimiento del plan de accion debe cumplirse en las fechas  ingresadas </p>\n"
        		             + "<p>&nbsp;</p>\n"
        		             + "<p>Saludos cordiales,</p>\n";
        		}
        		else if (tipo_correo==2) {
        		//Tipo rol registro de nueva accion	
        	        htmlText+="<p>Notificamos mediante la presente que se ha registrado la siguiente accion : <strong>"+acciones+" .</strong> en el Plan de Accion Nro.:"+ide_sepla+" De la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
        	        + "<p>&nbsp;</p>\n" 
        	        + "<p>Recuerde que debe registrar la documentacion que valide la accion registrada.</p>\n"
        	        + "<p>&nbsp;</p>\n"
        	        + "<p>Saludos cordiales,</p>\n";

        		}
        		else if (tipo_correo==3) {
        		//Tipo rol adjunto accion
        			
        	        htmlText+="<p>Notificamos mediante la presente que se ha registrado la documentacion de la accion : <strong>"+acciones+" .</strong> del Plan de Accion Nro.:"+ide_sepla+" De la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
        	       + "<p>&nbsp;</p>\n" 
        	        + "<p>Recuerde que debe registrar las acciones que realizara para el cumplimiento del Plan de Accion </p>\n"
        	        + "<p>&nbsp;</p>\n"
        	        + "<p>Saludos cordiales,</p>\n";

        		}	
        		else if (tipo_correo==4) {
        		//Plan de accion
        			htmlText+="<p>Notificamos mediante la presente que se ha ingresado el Plan de Accion Nro.:"+ide_sepla+"  De la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" .</p>\n"
        		             + "<p>&nbsp;</p>\n" 
        		             + "<p>Recuerde que debe registrar las acciones que realizara para el cumplimiento del Plan de Accion en un lapso de 5 dias habiles</p>\n"
        		             + "<p>&nbsp;</p>\n"
        		             + "<p>Saludos cordiales,</p>\n";
        		}


                   
                   
              	htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
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

           
            //String htmlText = cuerpo;
            messageBodyPart.setContent(htmlText, "text/html");
            multiParte.addBodyPart(messageBodyPart);
            	            
         // second part (the image)
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource("D:/soporteTecnico.jpg");
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");
            multiParte.addBodyPart(messageBodyPart);

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


/*
   public void EnviarCorreo(String ide_sepla,String informe, String recomendacion, String acciones,String ide_gtemp,int tipo_correo) throws Exception{
		try {
		TablaGenerica tab_correo= utilitario.consultar("select ide_gtemp,detalle_gtcor from gth_correo where ide_gtemp="+ide_gtemp);
		String correo="juan.ayerve@emgirs.gob.ec";//tab_correo.getValor("detalle_gtcor");
		TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
				+ "clave_corr from sis_correo where ide_corr=2"); 
		String smtp_correo="mail.emgirs.gob.ec"; 
		String puertoEnvio="25"; 
		String correo_envio="srecomendaciones@emgirs.gob.ec";
		String usuario_envio="srecomendaciones"; 
		String clave_correo="R3comendaciones.2022";
				
		//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
		EnvioMail envMail = new EnvioMail(smtp_correo,puertoEnvio,correo_envio,usuario_envio,clave_correo);
				String strNombreEmpleado="";
				strNombreEmpleado = obtenerNombresApellidosEmpleadoCorreo(ide_gtemp);				
		try {
			
			if (tipo_correo==1) {
				//util.EnviaMailInterno(envMail, correo_envio, "SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP", emailLinkEncuestaCorreo(strNombreEmpleado,ide_sepla, informe,  recomendacion,  acciones, ide_gtemp,tipo_correo), null);
				EnviaMailInterno(envMail, correo, "SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP", emailLinkEncuestaCorreo(strNombreEmpleado,ide_sepla,informe, recomendacion, acciones, ide_gtemp,tipo_correo), null,strNombreEmpleado,
				ide_sepla, informe,  recomendacion,  acciones, ide_gtemp);
					
			}else if(tipo_correo==2){
				
			//util.EnviaMailInterno(envMail, correo_envio, "SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP", emailLinkEncuestaCorreo(strNombreEmpleado,ide_sepla, informe,  recomendacion,  acciones, ide_gtemp), null);
			EnviaMailInterno(envMail, correo, "SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP", emailLinkEncuestaCorreo(strNombreEmpleado,ide_sepla,informe, recomendacion, acciones, ide_gtemp), null,strNombreEmpleado,
			ide_sepla, informe,  recomendacion,  acciones, ide_gtemp);
			}
			
			System.out.println("Enviando Correo.........");

			} catch (Exception e) {
			System.out.println("Error en el envio de correo"+e.getMessage());
				}
			} catch (Exception e) {
				e.printStackTrace();
				utilitario.agregarMensajeError("Ha ocurrido un error al aprobar la solicitud", "");
			}

				} 	
  */ 
   
   public void EnviarCorreoAccion(String ide_sepla,String informe, String recomendacion, String acciones,String ide_gtemp,int tipo_rol) throws Exception{
		try {
		TablaGenerica tab_correo= utilitario.consultar("select ide_gtemp,detalle_gtcor from gth_correo where ide_gtemp="+ide_gtemp);
		String correo="tania.vilca@emgirs.gob.ec";//tab_correo.getValor("detalle_gtcor");
		TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
				+ "clave_corr from sis_correo where ide_corr=2"); 
		String smtp_correo="mail.emgirs.gob.ec"; 
		String puertoEnvio="25"; 
		String correo_envio="srecomendaciones@emgirs.gob.ec";
		String usuario_envio="srecomendaciones"; 
		String clave_correo="R3comendaciones.2022";
				
		//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
		EnvioMail envMail = new EnvioMail(smtp_correo,puertoEnvio,correo_envio,usuario_envio,clave_correo);
				String strNombreEmpleado="";
				strNombreEmpleado = obtenerNombresApellidosEmpleadoCorreo(ide_gtemp);				
		try {

			TablaGenerica tab_tipo_respuesta=utilitario.consultar("SELECT ide_setre, descripcion_setre "
						+ "FROM seg_tipo_respuesta where ide_setre="+tab_respuesta.getValor("ide_setre"));
			String descripcion_setre=tab_tipo_respuesta.getValor("descripcion_setre");
			
			//util.EnviaMailInterno(envMail, correo_envio, "SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP", emailLinkEncuestaCorreo(strNombreEmpleado,ide_sepla, informe,  recomendacion,  acciones+" con estado:"+descripcion_setre, ide_gtemp,tipo_rol), null);
			String str_mail=correo_envio;
			envMail.setAsunto("SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP");
			envMail.setCuerpoHtml(emailLinkEncuestaCorreo(strNombreEmpleado,ide_sepla, informe,  recomendacion,  acciones+" con estado:"+descripcion_setre, ide_gtemp,tipo_rol));
			envMail.setPara(str_mail);
			pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
			
			if(obj.getRespuesta())
			{
				utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : email: " + str_mail);
			}
			else
				utilitario.agregarMensajeError("Correo no enviado a : email: " + str_mail , " msjError: " + obj.getDescripcion());
			//no usar EnviaMailInterno
			EnviaMailInternoAccion(envMail, correo, "SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP", emailLinkEncuestaCorreo(strNombreEmpleado,ide_sepla,informe, recomendacion, acciones+" con estado:"+descripcion_setre, ide_gtemp,tipo_rol), null,strNombreEmpleado,
			ide_sepla, informe,  recomendacion,  acciones+" con estado:"+descripcion_setre, ide_gtemp,tipo_rol);
			System.out.println("Enviando Correo.........");

			} catch (Exception e) {
			System.out.println("Error en el envio de correo"+e.getMessage());
				}
			} catch (Exception e) {
				e.printStackTrace();
				utilitario.agregarMensajeError("Ha ocurrido un error al aprobar la solicitud", "");
			}

				} 		
   
   
   

	public void subirArchivo(FileUploadEvent event) {
		/*     */     try {
		/* 477 */       String str_nombre = this.framework.getVariable("IDE_USUA") + this.framework.getFechaActual().replace("-", "") + this.framework.getHoraActual().replace(":", "") + event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf("."), event.getFile().getFileName().length());
		/* 478 */       str_nombre = str_nombre.toLowerCase();
		/* 479 */       String str_path = this.framework.getPropiedad("rutaUpload") + "/" + this.carpeta;
		/* 480 */       File path = new File(str_path);
		/* 481 */       path.mkdirs();
		/* 482 */       File result = new File(str_path + "/" + str_nombre);
		/*     */       
		/* 484 */       javax.faces.context.ExternalContext extContext = javax.faces.context.FacesContext.getCurrentInstance().getExternalContext();
		/* 485 */       str_path = extContext.getRealPath("/upload/" + this.carpeta);
		/* 486 */       path = new File(str_path);
		/* 487 */       path.mkdirs();
		/* 488 */       File result1 = new File(str_path + "/" + str_nombre);
		/*     */       try {
		/* 490 */         Tabla tab_formulario = this.framework.getTablaisFocus();
		/* 491 */         tab_formulario.setValor(getNombre(), "/upload/" + this.carpeta + "/" + str_nombre);
		/* 492 */         if (!tab_formulario.isFilaInsertada()) {
		/* 493 */           tab_formulario.modificar(tab_formulario.getFilaActual());
		/*     */         }
		/*     */       }
		/*     */       catch (Exception e) {}
		/* 497 */       int BUFFER_SIZE = 6124;
		/*     */       try {
		/* 499 */         FileOutputStream fileOutputStream = new FileOutputStream(result);
		/* 500 */         byte[] buffer = new byte[BUFFER_SIZE];
		/*     */         
		/* 502 */         InputStream inputStream = event.getFile().getInputstream();
		/*     */         for (;;) {
		/* 504 */           int bulk = inputStream.read(buffer);
		/* 505 */           if (bulk < 0) {
		/*     */             break;
		/*     */           }
		/* 508 */           fileOutputStream.write(buffer, 0, bulk);
		/* 509 */           fileOutputStream.flush();
		/*     */         }
		/* 511 */         fileOutputStream.close();
		/* 512 */         inputStream.close();
		/*     */       } catch (IOException e) {
		/* 514 */         System.out.println(e.getMessage());
		/*     */       }
		/*     */       try {
		/* 517 */         FileOutputStream fileOutputStream = new FileOutputStream(result1);
		/* 518 */         byte[] buffer = new byte[BUFFER_SIZE];
		/*     */         
		/* 520 */         InputStream inputStream = event.getFile().getInputstream();
		/*     */         for (;;) {
		/* 522 */           int bulk = inputStream.read(buffer);
		/* 523 */           if (bulk < 0) {
		/*     */             break;
		/*     */           }
		/* 526 */           fileOutputStream.write(buffer, 0, bulk);
		/* 527 */           fileOutputStream.flush();
		/*     */         }
		/* 529 */         fileOutputStream.close();
		/* 530 */         inputStream.close();
		/*     */       } catch (IOException e) {
		/* 532 */         System.out.println(e.getMessage());
		/*     */       }
		/*     */     } catch (Exception ex) {
		/* 535 */       System.out.println(ex.getMessage());
		/*     */     }
		/*     */     try
		/*     */     {
		/* 539 */       this.framework.getSeleccionArchivo().cerrar();
		/*     */     }
		/*     */     catch (Exception e) {}
		/*     */   }


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getControl() {
		return control;
	}


	public void setControl(String control) {
		this.control = control;
	}

	
	   public void setUpload(String carpeta) {
		  this.carpeta = carpeta;
		  setControl("Upload");
	   }
	
}

