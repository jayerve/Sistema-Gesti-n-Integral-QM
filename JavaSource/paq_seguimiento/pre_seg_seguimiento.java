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

import com.lowagie.text.pdf.ArabicLigaturizer;


import paq_comercializacion.ejb.ServicioClientes;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_gestion.ejb.ServicioGestion;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidades.EnvioMail;
import framework.aplicacion.Framework;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;

import org.apache.poi.hssf.util.HSSFColor.TAN;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

public class pre_seg_seguimiento extends Pantalla {

	private Framework framework = new Framework();
	private Tabla tab_informe = new Tabla();
	private Tabla tab_asignacion = new Tabla();
	private Tabla tab_recomendacion = new Tabla();
	private Tabla tab_respuesta = new Tabla();
	private Tabla tab_respuesta_historial = new Tabla();
	private Tabla tab_respuesta_adjunto = new Tabla();
	private Tabla tab_respuesta_adjunto_historial = new Tabla();
	private Tabla tab_informe_adjunto = new Tabla();
	private Tabla tab_plan_accion = new Tabla();
	private Confirmar con_aprobar_solicitud=new Confirmar();
	private Confirmar con_solicitud_reprogramar=new Confirmar();

	private Dialogo dia_anulado=new Dialogo();
	private Dialogo dia_informacion=new Dialogo();

	private AreaTexto are_tex_razon_anula=new AreaTexto();
	private AreaTexto are_tex_informativa=new AreaTexto();
	private AreaTexto are_tex_informativa_recomendacion=new AreaTexto();


	private Etiqueta eti_informacion = new Etiqueta();
	private Etiqueta eti_informacion_recomendacion = new Etiqueta();

	private Calendario cal_fecha_inicio=new Calendario();
	private Calendario cal_fecha_fin=new Calendario();
	String carpeta="respuesta";
	
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
    int ide_sepla_periocidad=0,ide_sepla_notificacion=0,ide_sepla_notificacion_nuevo=0;
	String usuario_responsable="",empresa="",empleado="",ide_secar="",fecha_inicio_seres="",fecha_fin_seres="",descripcion_seres="",empleado_responsable="";	
	boolean bandAccion=false,bandReproPlanTotal=false,bandReproPlanParcial=false,banderaNotificacion=false;
	public pre_seg_seguimiento (){
		
		if(!pckUtilidades.Utilitario.obtenerIPhost().contains(utilitario.getVariable("p_ip_servidor_seguimiento")))
		{
			utilitario.agregarNotificacionInfo("MENSAJE - AUTORIZACION DE MODULO","Esta pantalla no esta autorizada para usarse en el servidor , favor use el servidor de SISTEMA ERP ");
		return;
		}
		
				
		utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'seg_informe'");
		empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		bandAccion=false;
		String ide_serec="",ide_seinf="",ide_sepla="";
	 TablaGenerica tab_usuario=utilitario.consultar("SELECT usuario.ide_seusu, usuario.ide_seemp, usuario.ide_secar, usuario.nombre_seusu, usuario.login_seusu, usuario.password_seusu, "
					+ "usuario.usu_email, usuario.ide_usua, usuario.activo_seusu,emp.ide_gtemp "
					+ "FROM seg_usuario usuario "
					+ "LEFT JOIN SIS_USUARIO USUA ON usua.ide_usua=usuario.ide_usua "
					+ "LEFT JOIN GTH_EMPLEADO EMP on emp.ide_gtemp=usua.ide_gtemp "					
					+ "where usuario.ide_usua="+utilitario.getVariable("ide_usua")+" and activo_seusu=true ") ;
	 
	 empresa=tab_usuario.getValor("ide_seemp");	
	 usuario_responsable=tab_usuario.getValor("ide_seusu");		 
	 ide_secar=tab_usuario.getValor("ide_secar");
	usuario_responsable=tab_usuario.getValor("ide_seusu");
	if (tab_usuario.getValor("ide_gtemp")== null || tab_usuario.getValor("ide_gtemp").equals("") || tab_usuario.getValor("ide_gtemp").isEmpty()) {
		empleado_responsable="null";
	}else {
		empleado_responsable=tab_usuario.getValor("ide_gtemp");
	}
	
	 TablaGenerica tab_asignacion;
	// System.out.println("Cargo: "+ide_secar);
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
				
			/*	TablaGenerica tab_plan_accion=utilitario.consultar("select ide_sepla,ide_seinf,ide_serec from seg_plan_accion where ide_seinf in("+ide_seinf+") and ide_serec in("+ide_serec+") and activo_sepla=true");
				if (tab_plan_accion.getTotalFilas()>0) {
					for (int i = 0; i < tab_asignacion.getTotalFilas(); i++) {
						if (tab_plan_accion.getTotalFilas()==1) {
							ide_sepla=tab_asignacion.getValor(i,"ide_sepla");
						}else if((tab_asignacion.getTotalFilas()-1)==i){
							ide_sepla=tab_asignacion.getValor(i,"ide_sepla");
						}else{
							ide_sepla+=tab_asignacion.getValor(i,"ide_sepla");
						}
			} 
	 
	
					
					
				}else {
					
				}*/
				
				
			} 
	 
	
//System.out.println("informes_asignadas"+ide_seinf);
//System.out.println("recomendaciones_asignadas"+ide_serec);
	//bar_botones.getBot_eliminar().setRendered(false);
	//bar_botones.getBot_guardar().setRendered(false);
	//bar_botones.getBot_insertar().setRendered(false);
	 

	Boton bot_aprobar_plan=new Boton();
	bot_aprobar_plan.setValue("REPROGRAMACION PARCIAL PLAN ACCION");
	//bot_aprobar_plan.setMetodo("reprogramarPlan");
	bot_aprobar_plan.setMetodo("dibujarReprogramar");
	//bar_botones.agregarBoton(bot_aprobar_plan);
	

	
	Boton bot_reprogramar=new Boton();
	bot_reprogramar.setValue("REPROGRAMACION PLAN ACCION");
	//bot_reprogramar.setMetodo("reprogramarPlanTotal");
	bot_reprogramar.setMetodo("dibujarReprogramarTotal");
	bar_botones.agregarBoton(bot_reprogramar);
	
	cal_fecha_inicio.setId("cal_fecha_inicio");
	cal_fecha_fin.setId("cal_fecha_fin");

	are_tex_razon_anula.setId("are_tex_razon_anula");
	are_tex_razon_anula.setStyle("width:5000px;height:60px;overflow:auto;");

	Grid gri_anular_horas_extra=new Grid();
	gri_anular_horas_extra.setColumns(1);
	gri_anular_horas_extra.getChildren().add(new Etiqueta("RAZON DE RE-PROGRAMACION"));
	gri_anular_horas_extra.getChildren().add(are_tex_razon_anula);
	//gri_anular_horas_extra.getChildren().add(new Etiqueta("FECHA INICIO DE RE-PROGRAMACION"));
	//gri_anular_horas_extra.getChildren().add(cal_fecha_inicio);
	//gri_anular_horas_extra.getChildren().add(new Etiqueta("FECHA FIN DE RE-PROGRAMACION"));
	//gri_anular_horas_extra.getChildren().add(cal_fecha_fin);


	dia_anulado.setId("dia_anulado");
	dia_anulado.setDialogo(gri_anular_horas_extra);
	dia_anulado.setWidth("20%");
	dia_anulado.setHeight("35%");
	dia_anulado.setTitle("RE-PROGRAMACION DE ACTIVIDADES");
	dia_anulado.getBot_aceptar().setMetodo("aceptarReprogramar");
	dia_anulado.getBot_cancelar().setMetodo("cancelarReprogramar");
	dia_anulado.setDynamic(false);
	gri_anular_horas_extra.setStyle("width:" + (dia_anulado.getAnchoPanel() - 5) + "px;overflow:auto;");
	agregarComponente(dia_anulado);
	 
	eti_informacion.setId("eti_informacion");
	eti_informacion.setValue("ASUNTO INFORME");
	eti_informacion.setStyle("font-weight:bold");

	are_tex_informativa.setId("are_tex_informativa");
	are_tex_informativa.setValue("asdsdaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
	are_tex_informativa.setStyle("width:500px;height:60px;overflow:auto;disabled:true;overflow:auto;");
	are_tex_informativa.setDisabled(true);

	Grid gri_informacion=new Grid();
	gri_informacion.setColumns(1);
	gri_informacion.getChildren().add(eti_informacion);
	gri_informacion.getChildren().add(are_tex_informativa);
	//gri_informacion.setStyle("width:" + (dia_informacion.getAnchoPanel() - 5) + "px;overflow:auto;");

	
	eti_informacion_recomendacion.setId("eti_informacion_recomendacion");
	eti_informacion_recomendacion.setValue("ASUNTO RECOMENDACION");
	eti_informacion_recomendacion.setStyle("font-weight:bold");
	
	are_tex_informativa_recomendacion.setId("are_tex_informativa_recomendacion");
	are_tex_informativa_recomendacion.setValue("asdsdaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
	are_tex_informativa_recomendacion.setStyle("width:500px;height:60px;overflow:auto;disabled:true;");
	are_tex_informativa_recomendacion.setDisabled(true);

	Grid gri_informacion_recomendacion=new Grid();
	gri_informacion_recomendacion.setColumns(1);
	gri_informacion_recomendacion.getChildren().add(eti_informacion_recomendacion);
	gri_informacion_recomendacion.getChildren().add(are_tex_informativa_recomendacion);
	
	

	dia_informacion.setId("dia_informacion");
	dia_informacion.setDialogo(gri_informacion);
	dia_informacion.setWidth("40%");
	dia_informacion.setHeight("55%");
	dia_informacion.setTitle("DETALLE DESCRIPCION");
	dia_informacion.getBot_aceptar().setMetodo("aceptarDialogo");
	dia_informacion.getBot_cancelar().setMetodo("aceptarDialogo");
	dia_informacion.setDynamic(false);
	//gri_informacion.setStyle("width:" + (dia_informacion.getAnchoPanel() - 5) + "px;overflow:auto;");
	agregarComponente(dia_informacion);
	 
	con_aprobar_solicitud.setId("con_aprobar_solicitud");
	con_aprobar_solicitud.setMessage("Esta seguro que desea cambiar de estado");
	con_aprobar_solicitud.setTitle("CONFIRMACION CAMBIO DE ESTADO DE ACCION");
	con_aprobar_solicitud.getBot_aceptar().setMetodo("aceptarAprobarSolicitudTalento");
	con_aprobar_solicitud.getBot_cancelar().setMetodo("cancelarAprobarSolicitud");
	agregarComponente(con_aprobar_solicitud);

	
	con_solicitud_reprogramar.setId("con_solicitud_reprogramar");
	con_solicitud_reprogramar.setMessage("Esta seguro que desea aceptar reprogramacion");
	con_solicitud_reprogramar.setTitle("CONFIRMACION CAMBIO DE ESTADO DE ACCION");
	con_solicitud_reprogramar.getBot_aceptar().setMetodo("aceptarReprogramacion");
	con_aprobar_solicitud.getBot_cancelar().setMetodo("cancelarAprobarSolicitud");
	agregarComponente(con_solicitud_reprogramar);
	
	
	 
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
	        tab_informe.getColumna("asunto_seinf").setAncho(120);
	        tab_informe.getColumna("asunto_seinf").setLongitud(150);
	        tab_informe.getColumna("asunto_seinf").setVisible(false);
//	        tab_informe.getColumna("asunto_seinf").setOnClick("desplegarAsunto();");//;OnClick("mostrarDialogo");//MetodoChange("");

	        //tab_informe.getColumna("asunto_seinf").setLectura(true);


	        
	        tab_informe.getColumna("fecha_aprobacion_seinf").setNombreVisual("FECHA APROBACION");
	        tab_informe.getColumna("fecha_aprobacion_seinf").setLectura(true);
	        
	        tab_informe.getColumna("fecha_inicio_seinf").setNombreVisual("FECHA INICIO");
	        tab_informe.getColumna("fecha_inicio_seinf").setLectura(true);
	        
	        tab_informe.getColumna("fecha_fin_seinf").setNombreVisual("FEECHA FIN");
	        tab_informe.getColumna("fecha_fin_seinf").setLectura(true);

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
	        tab_informe.getColumna("activo_seinf").setLectura(true);

	        tab_informe.getGrid().setColumns(4);
	        tab_informe.setTipoFormulario(true);
	        //tab_informe.setLectura(true);
	        tab_informe.agregarRelacion(tab_informe_adjunto);
	        tab_informe.agregarRelacion(tab_recomendacion);
	         //tab_informe.agregarRelacion(tab_respuesta);
	      
	  	 if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
	  	 
	  	 }else {
		       tab_informe.setCondicion("ide_seinf in("+ide_seinf+")");

		}

	        tab_informe.dibujar();

			PanelTabla pat_panel1=new PanelTabla();
			pat_panel1.setPanelTabla(tab_informe);
			//pat_panel1.set(are_tex_informativa);
			pat_panel1.setFooter(gri_informacion);
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
	        tab_recomendacion.getColumna("ide_seesr").setAncho(20);
	        tab_recomendacion.getColumna("ide_seesr").setLongitud(50);
	        
	        tab_recomendacion.getColumna("numero_serec").setNombreVisual("NUMERO");
	        tab_recomendacion.getColumna("asunto_serec").setNombreVisual("ASUNTO");
	        tab_recomendacion.getColumna("asunto_serec").setAncho(160);
	        tab_recomendacion.getColumna("asunto_serec").setLongitud(500);
	        tab_recomendacion.getColumna("asunto_serec").setVisible(false);

	        
	        tab_recomendacion.getColumna("fecha_aprobacion_serec").setNombreVisual("FECHA APROBACION");
	        tab_recomendacion.getColumna("fecha_inicio_serec").setNombreVisual("FECHA INICIO");
	        tab_recomendacion.getColumna("fecha_fin_serec").setNombreVisual("FEECHA FIN");
	        tab_recomendacion.getColumna("activo_serec").setNombreVisual("ACTIVO");
	        tab_recomendacion.setLectura(true);

	   	 if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
		  	 
	  	 }else {
		       tab_recomendacion.setCondicion("ide_serec in("+ide_serec+")");

		}
	        tab_recomendacion.setCampoOrden("ide_serec asc");
	        tab_recomendacion.getGrid().setColumns(4);
	        tab_recomendacion.dibujar();
	        
	        PanelTabla pat_panel3 = new PanelTabla();
	        pat_panel3.setPanelTabla(tab_recomendacion);
	        pat_panel3.setFooter(gri_informacion_recomendacion);
			pat_panel3.setMensajeWarn("3.R E C O M E N D A C I Ó N");
			

			
			
			/*tab_plan_accion.setId("tab_plan_accion");
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

			//tab_plan_accion.agregarRelacion(tab_respuesta);
			
			
			tab_plan_accion.dibujar();
	        PanelTabla pat_panel4= new PanelTabla();
	        pat_panel4.setPanelTabla(tab_plan_accion);
			pat_panel4.setMensajeWarn("4. PLAN_ACCION");
			*/
			
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
				tab_respuesta.getColumna("ide_setre").setMetodoChange("validarTipoAccion");
				
			}else {
				tab_respuesta.getColumna("ide_setre").setCombo("SELECT ide_setre, descripcion_setre "
						+ "FROM seg_tipo_respuesta");//+ "FROM seg_tipo_respuesta where ide_setre=1 "); 
				tab_respuesta.getColumna("ide_setre").setLectura(true);
			}
			tab_respuesta.getColumna("ide_setre").setOrden(2);
		
			tab_respuesta.getColumna("descripcion_seres").setNombreVisual("ACTIVIDADES_PROGRAMADAS");
			tab_respuesta.getColumna("descripcion_seres").setRequerida(true);
			tab_respuesta.getColumna("descripcion_seres").setOrden(3);
			tab_respuesta.getColumna("medio_verificacion_seres").setNombreVisual("MEDIO_VERIFICACION");
			tab_respuesta.getColumna("medio_verificacion_seres").setRequerida(true);
			tab_respuesta.getColumna("medio_verificacion_seres").setOrden(3);
			tab_respuesta.getColumna("fecha_desde_seres").setNombreVisual("FECHA.INICIO");
			tab_respuesta.getColumna("fecha_desde_seres").setRequerida(true);		
			tab_respuesta.getColumna("fecha_desde_seres").setOrden(4);
			tab_respuesta.getColumna("fecha_hasta_seres").setNombreVisual("FECHA.FIN");
			tab_respuesta.getColumna("fecha_hasta_seres").setRequerida(true);
			tab_respuesta.getColumna("fecha_hasta_seres").setOrden(5);
			tab_respuesta.getColumna("mecanismo_reporte_seres").setUpload(carpeta);
			tab_respuesta.getColumna("mecanismo_reporte_seres").setNombreVisual("ADJUNTO");			
			tab_respuesta.getColumna("mecanismo_reporte_seres").setColumnaNombreArchivo("nombre_archivo_seres");
			
			//tab_respuesta.getColumna("mecanismo_reporte_seres").setRequerida(true);
			
		//	tab_respuesta.getColumna("mecanismo_reporte_seres").setMetodoChange(metodo);("validarEstadoAsignacion");

			tab_respuesta.getColumna("mecanismo_reporte_seres").setOrden(6);
		
			tab_respuesta.getColumna("nombre_archivo_seres").setLectura(true);
			tab_respuesta.getColumna("nombre_archivo_seres").setNombreVisual("NOMBRE_ADJUNTO");
			tab_respuesta.getColumna("nombre_archivo_seres").setValorDefecto("sin nombre");
			tab_respuesta.getColumna("nombre_archivo_seres").setOrden(7);

			
			
			tab_respuesta.getColumna("ide_seemp").setNombreVisual("AREA");		
	        tab_respuesta.getColumna("ide_seemp").setCombo("SELECT ide_seemp,descripcion_seemp "
	        		+ "FROM seg_empresa where  activo_seemp=true");
			tab_respuesta.getColumna("ide_seemp").setLectura(true);	
			tab_respuesta.getColumna("ide_seemp").setVisible(false);
			tab_respuesta.getColumna("ide_seemp").setOrden(8);
			tab_respuesta.getColumna("ide_gtemp_responsable").setNombreVisual("USUARIO_RESPONSABLE");		
	        tab_respuesta.getColumna("ide_gtemp_responsable").setCombo("SELECT  emp.ide_gtemp, "
	        		+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
	        		+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
	        		+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
	        		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS  "
	        		+ "FROM gth_empleado emp  "
	        		//+ "where emp.activo_gtemp=true  "
	        		+ "order by APELLIDO_PATERNO_GTEMP,APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP");
	        tab_respuesta.getColumna("ide_gtemp_responsable").setAutoCompletar();
			tab_respuesta.getColumna("ide_gtemp_responsable").setOrden(9);
			tab_respuesta.getColumna("hora_seres").setNombreVisual("HORA");
			tab_respuesta.getColumna("hora_seres").setValorDefecto(utilitario.getHoraActual());
			tab_respuesta.getColumna("hora_seres").setLectura(true);
			tab_respuesta.getColumna("hora_seres").setOrden(10);
			tab_respuesta.getColumna("fecha_seres").setNombreVisual("FEC_REG_ACTIVIDAD");
			tab_respuesta.getColumna("fecha_seres").setValorDefecto(utilitario.getFechaActual());
			tab_respuesta.getColumna("fecha_seres").setLectura(true);
			tab_respuesta.getColumna("fecha_seres").setOrden(11);
			tab_respuesta.getColumna("ide_seusu").setNombreVisual("USUARIO REGISTRO");		
	        tab_respuesta.getColumna("ide_seusu").setCombo("SELECT  segus.ide_seusu,segus.login_seusu "
	        		+ "FROM seg_usuario segus "
	        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
	        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
	        		+ "order by segus.ide_seusu");
	        tab_respuesta.getColumna("ide_seusu").setAutoCompletar();
	        tab_respuesta.getColumna("ide_seusu").setLectura(true);
			tab_respuesta.getColumna("ide_seusu").setOrden(12);
			tab_respuesta.getColumna("ide_seinf").setVisible(false);
			tab_respuesta.getColumna("ide_sepla").setVisible(false);
			tab_respuesta.getColumna("observacion_seres").setOrden(13);
			tab_respuesta.getColumna("activo_seres").setCheck();
			tab_respuesta.getColumna("activo_seres").setValorDefecto("true");
			tab_respuesta.getColumna("activo_seres").setNombreVisual("ACTIVO");
			tab_respuesta.getColumna("activo_seres").setOrden(14);
			tab_respuesta.getColumna("activo_seres").setLectura(true);
			tab_respuesta.getColumna("reprogramacion_seres").setNombreVisual("RE-PROGRAMACION");
			tab_respuesta.getColumna("reprogramacion_seres").setLectura(true);
			tab_respuesta.getColumna("reprogramacion_seres").setOrden(15);

			//tab_respuesta.getColumna("fecha_inicio_reprogramacion_seres").setNombreVisual("FEC_INICIO_REPRO");
		//	tab_respuesta.getColumna("fecha_inicio_reprogramacion_seres").setLectura(true);
			//tab_respuesta.getColumna("fecha_inicio_reprogramacion_seres").setOrden(15);

			//tab_respuesta.getColumna("fecha_fin_reprogramacion_seres").setNombreVisual("FEC_FIN_REPRO");
			//tab_respuesta.getColumna("fecha_fin_reprogramacion_seres").setLectura(true);
			//tab_respuesta.getColumna("fecha_fin_reprogramacion_seres").setOrden(16);

			tab_respuesta.getColumna("descripcion_repro_seres").setNombreVisual("DESCRIP-REPRO");
			tab_respuesta.getColumna("descripcion_repro_seres").setLectura(true);
			tab_respuesta.getColumna("descripcion_repro_seres").setOrden(16);

			
			
			
			
			
			if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
				
				
				
				tab_respuesta.setCondicion("IDE_SEINF="+tab_informe.getValor("IDE_SEINF")+""
						+ " AND IDE_SEREC="+tab_recomendacion.getValor("IDE_SEREC")+" and  ide_sepla in (select ide_sepla from seg_plan_accion where activo_sepla=true)  and activo_seres=true");
				//tab_respuesta.ejecutarSql();		
			}else {
				 TablaGenerica tabRespuesta=utilitario.consultar("select * "
					 		+ "from seg_respuesta  "
					 		+"WHERE IDE_SEINF="+tab_informe.getValor("IDE_SEINF")+" "
							+ " AND IDE_SEREC="+tab_recomendacion.getValor("IDE_SEREC")+" and  ide_sepla in (select ide_sepla from seg_plan_accion where activo_sepla=true)   ");
					 
					 
					if (tabRespuesta.getTotalFilas()<=0) {
						tab_respuesta.setCondicion("IDE_SEREC=-1");
					}else {
						tab_respuesta.setCondicion("IDE_SEINF="+tab_informe.getValor("IDE_SEINF")+""
							+ " AND IDE_SEREC="+tab_recomendacion.getValor("IDE_SEREC")+" and  ide_sepla in (select ide_sepla from seg_plan_accion where activo_sepla=true)  and activo_seres=true");
						
					}
			}
			
			if (ide_secar.equals("1") || empresa.equals("1") || empresa.equals("2")) {
				//tab_respuesta.getColumna("reprogramacion_seres").setLectura(false);
				tab_respuesta.getColumna("reprogramacion_seres").setNombreVisual("REPROGRAMAR");
				tab_respuesta.getColumna("reprogramacion_seres").setLectura(true);
				tab_respuesta.getColumna("aceptar_repro_seres").setNombreVisual("ACEPTAR_REPROGRAMAR");
				tab_respuesta.getColumna("aceptar_repro_seres").setValorDefecto("false");
				tab_respuesta.getColumna("aceptar_repro_seres").setMetodoChange("validarReprogramacion");
				tab_respuesta.getColumna("aceptar_repro_seres").setOrden(17);
				tab_respuesta.getColumna("aceptar_repro_seres").setLectura(true);

			}else {
				tab_respuesta.getColumna("reprogramacion_seres").setLectura(true);
				tab_respuesta.getColumna("reprogramacion_seres").setCheck();
				tab_respuesta.getColumna("reprogramacion_seres").setValorDefecto("false");
				tab_respuesta.getColumna("reprogramacion_seres").setNombreVisual("REPROGRAMAR");
				tab_respuesta.getColumna("reprogramacion_seres").setMetodoChange("validarTipoAccion");
				tab_respuesta.getColumna("aceptar_repro_seres").setVisible(false);
				tab_respuesta.getColumna("aceptar_repro_seres").setValorDefecto("false");
				tab_respuesta.getColumna("aceptar_repro_seres").setOrden(17);
			}
			tab_respuesta.getColumna("mecanismo_reporte_seres_enlace").setVisible(false);
			tab_respuesta.getColumna("ide_seres_anterior").setVisible(false);
			tab_respuesta.setCampoOrden("fecha_desde_seres asc,fecha_hasta_seres asc");
			tab_respuesta.dibujar();
	        //PanelTabla pat_panel5= new PanelTabla();
	       // pat_panel5.setPanelTabla(tab_respuesta);
			PanelTabla pat_panel_detalle = new PanelTabla();

			pat_panel_detalle.setMensajeWarn("5. ACCIONES");

			
			

			ItemMenu enviar_plan_accion = new ItemMenu();
			enviar_plan_accion.setValue("Enviar Plan Accion");
			enviar_plan_accion.setMetodo("enviarPlan");
			enviar_plan_accion.setIcon("ui-icon-mail-closed");

			ItemMenu enviar_plan_notificacion_rechazo = new ItemMenu();
			enviar_plan_notificacion_rechazo.setValue("Enviar Accion Estado Rechazo");
			enviar_plan_notificacion_rechazo.setMetodo("enviarAccionRechazo");
			enviar_plan_notificacion_rechazo.setIcon("ui-icon-mail-closed");

			pat_panel_detalle.setPanelTabla(tab_respuesta);
			pat_panel_detalle.getMenuTabla().getChildren().add(enviar_plan_accion);
			pat_panel_detalle.getMenuTabla().getChildren().add(enviar_plan_notificacion_rechazo);

			//pat_panel_detalle.getMenuTabla().getItem_guardar().setRendered(false);
			//pat_panel_detalle.getMenuTabla().getItem_excel().setRendered(false);
			//pat_panel_detalle.getMenuTabla().getItem_buscar().setRendered(false);
			pat_panel_detalle.getMenuTabla().getItem_formato().setRendered(true);
			// pat_panel_detalle.getMenuTabla().getItem_eliminar().setRendered(false);
			//pat_panel_detalle.getMenuTabla().getItem_importar().setRendered(false);
			//pat_panel_detalle.getMenuTabla().getItem_insertar().setRendered(false);
			//pat_panel_detalle.getMenuTabla().getItem_actualizar().setRendered(false);
		
			
			tab_respuesta_historial.setId("tab_respuesta_historial");
			tab_respuesta_historial.setIdCompleto("tab_tabulador:tab_respuesta_historial");
			tab_respuesta_historial.setTabla("seg_respuesta", "ide_seres", 7);
			tab_respuesta_historial.getColumna("ide_setre").setCombo("SELECT ide_setre, descripcion_setre "
					+ "FROM seg_tipo_respuesta");
			tab_respuesta_historial.getColumna("ide_setre").setNombreVisual("TIPO");

		tab_respuesta_historial.getColumna("descripcion_seres").setNombreVisual("ACCIONES HISTORICAS");
		tab_respuesta_historial.getColumna("descripcion_seres").setRequerida(true);
		
		tab_respuesta_historial.getColumna("ide_seemp").setNombreVisual("AREA");		
		tab_respuesta_historial.getColumna("ide_seemp").setCombo("SELECT ide_seemp,descripcion_seemp "
        		+ "FROM seg_empresa where  activo_seemp=true");

		tab_respuesta_historial.getColumna("ide_seusu").setNombreVisual("USUARIO REGISTRO");		
		tab_respuesta_historial.getColumna("ide_seusu").setCombo("SELECT  segus.ide_seusu,segus.login_seusu "
        		+ "FROM seg_usuario segus "
        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
        		+ "order by segus.ide_seusu");
		
		

		tab_respuesta_historial.getColumna("ide_gtemp_responsable").setVisible(false);
		tab_respuesta_historial.getColumna("medio_verificacion_seres").setVisible(false);
		tab_respuesta_historial.getColumna("fecha_desde_seres").setVisible(false);
		tab_respuesta_historial.getColumna("fecha_hasta_seres").setVisible(false);
		tab_respuesta_historial.getColumna("mecanismo_reporte_seres").setVisible(false);
		tab_respuesta_historial.getColumna("ide_serec").setVisible(false);
		tab_respuesta_historial.getColumna("ide_seinf").setVisible(false);
		tab_respuesta_historial.getColumna("ide_sepla").setVisible(false);
		tab_respuesta_historial.getColumna("observacion_seres").setVisible(false);
		tab_respuesta_historial.getColumna("activo_seres").setVisible(false);
		tab_respuesta_historial.getColumna("reprogramacion_seres").setVisible(false);
		tab_respuesta_historial.getColumna("descripcion_repro_seres").setVisible(false);
		tab_respuesta_historial.getColumna("reprogramacion_seres").setVisible(false);
		tab_respuesta_historial.getColumna("nombre_archivo_seres").setVisible(false);
		tab_respuesta_historial.getColumna("ide_seres_anterior").setVisible(false);
		tab_respuesta_historial.getColumna("mecanismo_reporte_seres_enlace").setVisible(false);

		
			
			/*tab_respuesta_historial.getColumna("ide_seres").setNombreVisual("CODIGO");
			tab_respuesta_historial.getColumna("ide_seres").setOrden(1);
			tab_respuesta_historial.getColumna("ide_serec").setVisible(false);
			
			tab_respuesta.getColumna("ide_setre").setCombo("SELECT ide_setre, descripcion_setre "
						+ "FROM seg_tipo_respuesta where ide_setre=1 "); 
			tab_respuesta_historial.getColumna("descripcion_seres").setNombreVisual("ACCIONES HISTORICAS");
			tab_respuesta_historial.getColumna("descripcion_seres").setRequerida(true);
			tab_respuesta_historial.getColumna("descripcion_seres").setOrden(3);
			
			tab_respuesta_historial.getColumna("medio_verificacion_seres").setVisible(false);

			tab_respuesta_historial.getColumna("fecha_desde_seres").setVisible(false);
			tab_respuesta_historial.getColumna("fecha_hasta_seres").setVisible(false);
			tab_respuesta_historial.getColumna("mecanismo_reporte_seres").setVisible(false);
			tab_respuesta_historial.getColumna("ide_seemp").setNombreVisual("AREA");		
			tab_respuesta_historial.getColumna("ide_seemp").setCombo("SELECT ide_seemp,descripcion_seemp "
	        		+ "FROM seg_empresa");
			tab_respuesta_historial.getColumna("ide_gtemp_responsable").setVisible(false);

			tab_respuesta_historial.getColumna("hora_seres").setNombreVisual("HORA");
			tab_respuesta_historial.getColumna("fecha_seres").setNombreVisual("FEC_REG_ACTIVIDAD");
			
			tab_respuesta_historial.getColumna("ide_usua").setNombreVisual("USUARIO REGISTRO");		
			tab_respuesta_historial.getColumna("ide_usua").setCombo("SELECT  segus.ide_seusu,usua.nick_usua "
	        		+ "FROM seg_usuario segus "
	        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
	        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
	        		+ "order by segus.ide_seusu");
			tab_respuesta_historial.getColumna("ide_seinf").setVisible(false);
			tab_respuesta_historial.getColumna("ide_sepla").setVisible(false);
			tab_respuesta_historial.getColumna("observacion_seres").setVisible(false);

			tab_respuesta_historial.getColumna("activo_seres").setCheck();
			tab_respuesta_historial.getColumna("activo_seres").setNombreVisual("ACTIVO");

			
			 TablaGenerica tabRespuestaHistorial=utilitario.consultar("select * "
				 		+ "from seg_respuesta  "
				 		+"WHERE IDE_SEINF="+tab_informe.getValor("IDE_SEINF")+" "
						+ " AND IDE_SEREC="+tab_recomendacion.getValor("IDE_SEREC")+" and ide_sepla is null");
				 
				 
				if (tabRespuestaHistorial.getTotalFilas()<=0) {
					tab_respuesta_historial.setCondicion("IDE_SEREC=-1");
				}else {
					tab_respuesta_historial.setCondicion("IDE_SEINF="+tab_informe.getValor("IDE_SEINF")+" and "
							+ "IDE_SEREC="+tab_recomendacion.getValor("IDE_SEREC")+" and ide_sepla is null");			 		
				}*/
			tab_respuesta_historial.setCondicion("ide_seinf="+tab_informe.getValor("IDE_SEINF")+" AND IDE_SEREC="+tab_recomendacion.getValor("IDE_SEREC")+" AND IDE_SEPLA IS  NULL");
			
			
		tab_respuesta_historial.agregarRelacion(tab_respuesta_adjunto_historial);
			tab_respuesta_historial.setLectura(true);
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
			tab_tabulador.agregarTab("2.RECOMENDACIÓN", pat_panel3);
			//tab_tabulador.agregarTab("4.PLAN.ACCION", pat_panel4);
			tab_tabulador.agregarTab("3.ACCIÓN", pat_panel_detalle);
			//tab_tabulador.agregarTab("6.ACCIÓN.ADJUNTO", pat_panel6);
			tab_tabulador.agregarTab("4.HISTORIAL DE ACCIONES", pat_panel7);
			tab_tabulador.agregarTab("5.ACCIÓN.ADJUNTO HISTORIAL", pat_panel8);

			
		
			Division div_division = new Division();
			div_division.setId("div_division");
			div_division.dividir2(pat_panel1,pat_panel2,"50%","V");
			//agregarComponente(div_division);
			Division div_division2=new Division();
			div_division2.setId("div_division2");
			div_division2.dividir2(div_division,tab_tabulador,"50%","H");
			agregarComponente(div_division2);
			
			actualizarInformativo();
			actualizarInformativoRecomendacion();
	}
	
	
	@Override
	public void insertar() {
		int ide_sepla=0,ide_sepla_=0;
		boolean insertar=false,sin_plan_accion=false;
		ide_sepla_periocidad=0;
	//	Si no existe en la recomendacion en la ultima evaluacion en estado de proceso
		TablaGenerica tab_detalle_vacacion=utilitario.consultar("SELECT ide_sedee, ide_seeva, ide_seinf, ide_serec, ide_seesr, asunto_sedee, "
				+ "activo_sedee, ide_gtemp "
				+ "FROM seg_detalle_evaluacion "
				+ "where ide_seeva >=4 "
				+ "and ide_seesr not in (2) and ide_serec="+tab_recomendacion.getValorSeleccionado());
		tab_detalle_vacacion.imprimirSql();
		if (tab_detalle_vacacion.getTotalFilas()>0) {
			
		}		else {
			//Si no existe dentro de la evaluacion cerrada
			TablaGenerica tab_detalle_vacacion_temp=utilitario.consultar("SELECT ide_sedee, ide_seeva, ide_seinf, ide_serec, ide_seesr, asunto_sedee, "
					+ "activo_sedee, ide_gtemp "
					+ "FROM seg_detalle_evaluacion "
					+ "where ide_seeva >=4 "
					+ "and ide_serec="+tab_recomendacion.getValorSeleccionado());
			
			if (tab_detalle_vacacion_temp.getTotalFilas()==0) {
				//Si la recomendacion se encentra en estado de proceso  y no se encuentra en la evaluacion
				TablaGenerica tab_recomendacion_=utilitario.consultar("SELECT ide_serec, numero_serec, activo_serec "
						+ "FROM seg_recomendacion "
						+ "where ide_seesr=1 "
						+ "and ide_serec="+tab_recomendacion.getValorSeleccionado());
				
				if (tab_recomendacion_.getTotalFilas()>0) {
					
				}else {
					utilitario.agregarMensajeInfo("No se puede realizar la accion solicitada", "Contactese con el administrador");
					return;	
				}
				
			}else {
				
			
			
			TablaGenerica tab_evaluacion = utilitario.consultar("select numero_seeva, asunto_seeva from seg_evaluacion where ide_seeva in("+tab_detalle_vacacion_temp.getValor("ide_seeva")+")");
			String etiqueta="Nro. Informme: "+tab_evaluacion.getValor("numero_seeva")+"  Asunto: "+tab_evaluacion.getValor("asunto_seeva");
			utilitario.agregarMensajeInfo("La Recomendación se encuentra en estado de Cumplida", "Mediante ealuacion:  "+etiqueta);
			return;
		}		
		}
		
		
	
		if(tab_respuesta.isFocus()){
		
			if (tab_recomendacion.getTotalFilas()>0) {	
				boolean bandPlanAplicado=false;
				//validacion de registro de avances con el plan de accion en estado cumplida y unica vez
				
				
			  	 if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
				tab_respuesta.insertar();
				tab_respuesta.setValor("ide_seusu", usuario_responsable);	
				tab_respuesta.setValor("ide_serec", tab_recomendacion.getValorSeleccionado());	
				tab_respuesta.setValor("ide_seinf",  tab_informe.getValorSeleccionado());
				tab_respuesta.setValor("ide_seemp", empresa);	
				tab_respuesta.setValor("activo_seres", ""+true);	
				ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
					ide_sepla_notificacion=ide_sepla;
					if (ide_sepla==0) {//preguntar
						insertarTablaPlanAccion(usuario_responsable, tab_recomendacion.getValor("IDE_SEREC"), utilitario.getFechaActual(), null, 2, "", tab_informe.getValor("ide_seinf"), null, "PLAN DE ACCION");
					 ide_sepla_=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
						ide_sepla_notificacion=ide_sepla_;
					tab_respuesta.setValor("ide_sepla",""+ ide_sepla_);	

				}else {
					tab_respuesta.setValor("ide_sepla", ""+ide_sepla);	
				}

			  	}else {//si es un usuario comun 
				ide_sepla_notificacion=0;
			  	//Consulto si el plan de accion se encuentra en estado de aplicada y la periodicidad es unica ve ide_seper=6	
				TablaGenerica tab_plan_aprobado=utilitario.consultar("SELECT ide_sepla, ide_seusu, ide_serec, fecha_registro_sepla, fecha_aprobacion_sepla, "
						+ "porcentaje_sepla, ide_seesp, activo_sepla,ide_seinf,notificacion_sepla, "
						+ "ide_seper, asunto_sepla, envio_plan, ide_seemp, observacion_sepla "
						+ "FROM seg_plan_accion "
						+ "where ide_serec="+tab_recomendacion.getValor("IDE_SEREC")+" and ide_seinf="+tab_recomendacion.getValor("IDE_SEINF")+" and ide_seesp=1 and ide_seper=6 "
								+ "and activo_sepla=true "
						+ "order by ide_sepla desc limit 1");
				
				if (tab_plan_aprobado.getTotalFilas()>0) {
					insertar=false;
					ide_sepla_periocidad=Integer.parseInt(tab_plan_aprobado.getValor("ide_sepla"));
					tab_respuesta.insertar();
					tab_respuesta.setValor("ide_seusu", usuario_responsable);	
					tab_respuesta.setValor("ide_serec", tab_recomendacion.getValorSeleccionado());	
					tab_respuesta.setValor("ide_sepla", ""+ide_sepla_periocidad);	
					tab_respuesta.setValor("ide_seinf",  tab_informe.getValorSeleccionado());
					tab_respuesta.setValor("ide_seemp", empresa);	
					tab_respuesta.setValor("activo_seres", ""+true);	
					ide_sepla_notificacion=ide_sepla_periocidad;
				  	 if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
				  	 }else {
							tab_respuesta.setValor("ide_setre",  ""+1);

			}
				
					
				}else {
					int cont_aplicadas=0,cont_aprobadas=0,cont_notificaciones=0,cont_avance=0,cont_notificaciones_rechazo=0;

					//Consulto si el plan de accion se encuentra en estado de ejecucion 
					TablaGenerica tab_plan_aplicado=utilitario.consultar("SELECT ide_sepla, ide_seusu, ide_serec, fecha_registro_sepla, fecha_aprobacion_sepla, "
							+ "porcentaje_sepla, ide_seesp, activo_sepla,ide_seinf,notificacion_sepla, "
							+ "ide_seper, asunto_sepla, envio_plan, ide_seemp, observacion_sepla "
							+ "FROM seg_plan_accion "
							+ "where ide_serec="+tab_recomendacion.getValor("IDE_SEREC")+" and ide_seinf="+tab_recomendacion.getValor("IDE_SEINF")+" and ide_seesp=2 and activo_sepla=true "
							+ "order by ide_sepla desc limit 1");
					
					if (tab_plan_aplicado.getTotalFilas()>0) {
						
							TablaGenerica tab_respuestas_plan_ejecucion=utilitario.consultar("select ide_seres,ide_serec,ide_seinf,ide_sepla,ide_setre "
									+ "from seg_respuesta "
									+ "where ide_sepla="+tab_plan_aplicado.getValor("ide_sepla")+" and activo_seres=true "
									+ "order by ide_seres asc");
									
					if (tab_respuestas_plan_ejecucion.getTotalFilas()==0) {
					tab_respuesta.insertar();
					tab_respuesta.setValor("ide_seusu", usuario_responsable);	
					tab_respuesta.setValor("ide_serec", tab_recomendacion.getValorSeleccionado());	
					tab_respuesta.setValor("ide_sepla", tab_plan_aplicado.getValor("ide_sepla"));	
					tab_respuesta.setValor("ide_seinf",  tab_informe.getValorSeleccionado());
					tab_respuesta.setValor("ide_seemp", empresa);	
					tab_respuesta.setValor("activo_seres", ""+true);	
					ide_sepla_notificacion=Integer.parseInt(tab_plan_aplicado.getValor("ide_sepla"));

					if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
			}else{
						tab_respuesta.setValor("ide_setre",  ""+1);
			}
					}//else si contiene acciones ingresadas
					else if(tab_respuestas_plan_ejecucion.getTotalFilas()>0){
						 cont_aplicadas=0;cont_aprobadas=0;cont_notificaciones=0;cont_avance=0;cont_notificaciones_rechazo=0;
						for (int i = 0; i < tab_respuestas_plan_ejecucion.getTotalFilas(); i++) {
							if (tab_respuestas_plan_ejecucion.getValor(i,"ide_setre").equals("1")) {// si es avance
								cont_avance++;
								cont_aplicadas=0;
								cont_aprobadas=0;
								cont_notificaciones=0;
								cont_notificaciones_rechazo=0;
								//i=tab_respuestas_plan_ejecucion.getTotalFilas();
								if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
								}else {
									tab_respuesta.setValor("ide_setre",  ""+1);
								}
							
							}
						else if (tab_respuestas_plan_ejecucion.getValor(i,"ide_setre").equals("5")) {//APLICADA
								cont_aplicadas++;
							}else if (tab_respuestas_plan_ejecucion.getValor(i,"ide_setre").equals("6")) {//APROBADA
								cont_aprobadas++;
							}else if (tab_respuestas_plan_ejecucion.getValor(i,"ide_setre").equals("3")) {//  NOTIFICACION
								cont_notificaciones++;
							}else if (tab_respuestas_plan_ejecucion.getValor(i,"ide_setre").equals("4")) {//NOTIFICACION DE RECHAZO
							cont_notificaciones_rechazo++;	
							}					
						}//for
					}
						int sumatoria_aplicadas=0;
						if (cont_avance==0 && cont_notificaciones>=0 && tab_respuestas_plan_ejecucion.getTotalFilas()!=0 && cont_aprobadas==0 && cont_aplicadas==0) {
							tab_respuesta.insertar();
							tab_respuesta.setValor("ide_seusu", usuario_responsable);	
							tab_respuesta.setValor("ide_serec", tab_recomendacion.getValorSeleccionado());	
							tab_respuesta.setValor("ide_sepla", tab_plan_aplicado.getValor("ide_sepla"));	
							tab_respuesta.setValor("ide_seinf",  tab_informe.getValorSeleccionado());
							tab_respuesta.setValor("ide_seemp", empresa);	
							tab_respuesta.setValor("activo_seres", ""+true);
							ide_sepla_notificacion=Integer.parseInt(tab_plan_aplicado.getValor("ide_sepla"));

							if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
							}else {
								tab_respuesta.setValor("ide_setre",  ""+1);
							}
						}else if (cont_avance>0 && cont_notificaciones>=0 && cont_aprobadas>=0) {
									tab_respuesta.insertar();
									tab_respuesta.setValor("ide_seusu", usuario_responsable);	
									tab_respuesta.setValor("ide_serec", tab_recomendacion.getValorSeleccionado());	
									tab_respuesta.setValor("ide_sepla", tab_plan_aplicado.getValor("ide_sepla"));	
									tab_respuesta.setValor("ide_seinf",  tab_informe.getValorSeleccionado());
									tab_respuesta.setValor("ide_seemp", empresa);	
									tab_respuesta.setValor("ide_sepla", tab_plan_aplicado.getValor("ide_sepla"));	
									tab_respuesta.setValor("activo_seres", ""+true);	
									ide_sepla_notificacion=Integer.parseInt(tab_plan_aplicado.getValor("ide_sepla"));

									if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
									}else {
										tab_respuesta.setValor("ide_setre",  ""+1);
									}
								}
						else if (cont_avance==0 && cont_notificaciones>=0 && cont_aprobadas>0) {
							utilitario.agregarMensaje("No se puede registrar acciones nuevas", "Revise el estado de sus actividades");
							tab_respuesta.actualizar();
						}else if (cont_avance==0 && cont_notificaciones>=0 && cont_aprobadas==0 && tab_respuestas_plan_ejecucion.getTotalFilas()!=0) {
							utilitario.agregarMensaje("No se puede registrar acciones nuevas", "Revise el estado de sus actividades");
							tab_respuesta.actualizar();
							
						}/*else if (cont_avance==0 && cont_notificaciones>=0 && cont_aprobadas==0 && tab_respuestas_plan_ejecucion.getTotalFilas()==0) {
							tab_respuesta.insertar();
							tab_respuesta.setValor("ide_seusu", usuario_responsable);	
							tab_respuesta.setValor("ide_serec", tab_recomendacion.getValorSeleccionado());	
							tab_respuesta.setValor("ide_sepla", tab_plan_aplicado.getValor("ide_sepla"));	
							tab_respuesta.setValor("ide_seinf",  tab_informe.getValorSeleccionado());
							tab_respuesta.setValor("ide_seemp", empresa);	
							tab_respuesta.setValor("activo_seres", ""+true);
							ide_sepla_notificacion=Integer.parseInt(tab_plan_aplicado.getValor("ide_sepla"));

							if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
							}else {
								tab_respuesta.setValor("ide_setre",  ""+1);
						}	
							
							
						}	*/
						else {
								//	System.out.println("ide_sepla "+tab_plan_aplicado.getValor("ide_sepla")+" en estado aprobado");
									/*utilitario.agregarMensaje("No se puede registrar acciones nuevas", "Revise el estado de sus actividades");
									tab_respuesta.actualizar();
									cont_aplicadas=0;
									cont_aprobadas=0;
									cont_notificaciones=0;
									cont_notificaciones_rechazo=0;
									cont_avance=0;
									return;*/
								}
								
						/*if (bandPlanAplicado) {
							
						}
						
						int sumatoria_resp=cont_aplicadas+cont_aprobadas+cont_notificaciones+cont_notificaciones_rechazo;
						if (bandPlanAplicado) {
							
						}
						if (sumatoria_resp!=0) {
							System.out.println("ide_sepla "+tab_plan_aplicado.getValor("ide_sepla")+" en estado aprobado");
							utilitario.agregarMensaje("No se puede registrar acciones nuevas", "Revise el estado de sus actividades");
							tab_respuesta.actualizar();
							sumatoria_resp=0;
							cont_aplicadas=0;
							cont_aprobadas=0;
							cont_notificaciones=0;
							cont_notificaciones_rechazo=0;
							cont_avance=0;
							return;
						}else {
							tab_respuesta.insertar();
							tab_respuesta.setValor("ide_seusu", usuario_responsable);	
							tab_respuesta.setValor("ide_serec", tab_recomendacion.getValorSeleccionado());	
							tab_respuesta.setValor("ide_sepla", tab_plan_aplicado.getValor("ide_sepla"));	
							tab_respuesta.setValor("ide_seinf",  tab_informe.getValorSeleccionado());
							tab_respuesta.setValor("ide_seemp", empresa);	
							tab_respuesta.setValor("activo_seres", ""+true);	
						}*/
						
		
		
				}//else de inserccion
				else{//Si el plan se encuentra en estado de aplicado y su periocidad es no es unica vez
					TablaGenerica tab_plan_aplicado_activo=utilitario.consultar("SELECT pla.ide_sepla, pla.ide_seusu, pla.ide_serec, pla.fecha_registro_sepla, pla.fecha_aprobacion_sepla, "
							+ "pla.porcentaje_sepla, pla.ide_seesp, pla.activo_sepla,pla.ide_seinf,pla.ide_seemp,pla.notificacion_sepla, "
							+ "pla.ide_seper, pla.asunto_sepla, pla.envio_plan, pla.ide_seemp, pla.observacion_sepla,peri.valor_seper,pla.fecha_inicio_control_periocidad_sepla "
							+ "FROM seg_plan_accion pla "
							+ "left join seg_periocidad peri on pla.ide_seper=peri.ide_seper "
							+ "where pla.ide_serec="+tab_recomendacion.getValor("IDE_SEREC")+" and pla.ide_seinf="+tab_recomendacion.getValor("IDE_SEINF")+" and pla.ide_seesp=1  and pla.ide_seper!=6 and activo_sepla=true "
							+ " order by ide_sepla desc limit 1");
				
					if (tab_plan_aplicado_activo.getTotalFilas()>0) {
					   TablaGenerica tab_respuestas=utilitario.consultar("select * from seg_respuesta where ide_sepla="+tab_plan_aplicado_activo.getValor("ide_sepla")
					   		+ "order by ide_seres asc" );
					   if (tab_respuestas.getTotalFilas()>0) {
				//		   int cont_aplicadas=0,cont_aprobadas=0,cont_notificaciones=0,cont_avance=0,cont_notificaciones_rechazo=0;
					for (int j = 0; j < tab_respuestas.getTotalFilas(); j++) {
						   if (tab_respuestas.getValor(j,"ide_setre").equals("1")) {
							cont_avance=0;
							cont_aplicadas=0;
							cont_aprobadas=0;
							cont_notificaciones=0;
							cont_notificaciones_rechazo=0;
							j=tab_respuestas.getTotalFilas();
							utilitario.agregarMensaje("No se puede registrar plan de accion contiene actividades en estado de Avance", "Pongase en contacto con el administrador del Sistema");
						   break;
						   }
						   else if (tab_respuestas.getValor(j,"ide_setre").equals("5")) {//APLICADA
								cont_aplicadas++;
							}else if (tab_respuestas.getValor(j,"ide_setre").equals("6")) {//APROBADA
								cont_aprobadas++;
								cont_avance=0;
								cont_aplicadas=0;
								cont_aprobadas=0;
								cont_notificaciones=0;
								cont_notificaciones_rechazo=0;
								j=tab_respuestas.getTotalFilas();
								utilitario.agregarMensaje("No se puede registrar plan de accion contiene actividades en esta de Aprobado", "Pongase en contacto con el administrador del Sistema");
								break;
							}else if (tab_respuestas.getValor(j,"ide_setre").equals("3")) {//  NOTIFICACION
								cont_notificaciones++;
							}else if (tab_respuestas.getValor(j,"ide_setre").equals("4")) {//NOTIFICACION DE RECHAZO
							cont_notificaciones_rechazo++;	
							}
					   }
					
					int sumatoria_resp=cont_aplicadas+cont_notificaciones+cont_notificaciones_rechazo;
					if (sumatoria_resp==tab_respuestas.getTotalFilas()) {
						String ide_seemp="",fecha_inicio_control_periocidad_sepla="",observacion_sepla="",ide_seper="";
						ide_seemp=tab_plan_aplicado_activo.getValor("ide_seemp");
						fecha_inicio_control_periocidad_sepla=tab_plan_aplicado_activo.getValor("fecha_inicio_control_periocidad_sepla");
						observacion_sepla=tab_plan_aplicado_activo.getValor("observacion_sepla");
						ide_seper=tab_plan_aplicado_activo.getValor("ide_seper");
						
						String fechaActual=utilitario.getFechaActual();
						
						if (fecha_inicio_control_periocidad_sepla==null ||  fecha_inicio_control_periocidad_sepla.isEmpty() || fecha_inicio_control_periocidad_sepla.isEmpty()) {
							utilitario.agregarMensaje("No se ha registrado fecha de cumplimiento en el plan de accion", "Pongase en contacto con el administrador");
							tab_respuesta.actualizar();
							return;
						}
						
						
						if (fechaActual.compareTo(fecha_inicio_control_periocidad_sepla)>=0) {
		
							  String ide_sepla_anterios="";
							  ide_sepla_anterios=tab_plan_aplicado_activo.getValor("ide_sepla");
							  utilitario.getConexion().ejecutarSql("update seg_plan_accion set activo_sepla=false where ide_sepla=" + tab_plan_aplicado_activo.getValor("ide_sepla"));
							  //TablaGenerica tab_respuestas_=utilitario.consultar("select * from seg_respuesta where ide_sepla="+tab_plan_aplicado_activo.getValor("ide_sepla")+" "
							//	   		+ "order by fecha_desde_seres asc,fecha_hasta_seres asc" );
							 // for (int i = 0; i < tab_respuestas_.getTotalFilas(); i++) {
							  utilitario.getConexion().ejecutarSql("update seg_respuesta set activo_seres=false where ide_seres=" + tab_plan_aplicado_activo.getValor("ide_sepla"));
								//}  
							  
							 int valor_periodicidad=0,dias_sumataria=0;
							 valor_periodicidad=Integer.parseInt(tab_plan_aplicado_activo.getValor("valor_seper"));
							  dias_sumataria=valor_periodicidad*30;
							  String nueva_Fecha="";
							  nueva_Fecha=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha_inicio_control_periocidad_sepla), dias_sumataria));
							  
							  insertarTablaPlanAccionPeriocidad(usuario_responsable, tab_recomendacion.getValor("IDE_SEREC"), utilitario.getFechaActual(), null, 2, "", tab_informe.getValor("ide_seinf"), ide_seper, "PLAN DE ACCION"
									  ,Integer.parseInt(ide_seemp),nueva_Fecha,observacion_sepla);
							  ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
							  insertarTablaRespuesta(ide_sepla_anterios, valor_periodicidad,ide_sepla);
							  actualizarTablas();
							
						}else {

							utilitario.agregarMensaje("No se puede registrar actividades", "Fecha de cumplimiento en el plan de accion es menor a fecha actual");
								tab_respuesta.actualizar();
							return;
							
						}
						
						
					/*	System.out.println("ide_sepla "+tab_plan_aplicado_activo.getValor("ide_sepla")+" en estado aplicado");
						utilitario.agregarMensaje("No se puede registrar plan de accion contiene actividades en esta de Avance", "Pongase en contacto con el administrador del Sistema");*/

					}
	
	
	
					  }
					   
					   
					}else{
		
		
					ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
					if (ide_sepla!=0) {//si existe
						utilitario.agregarMensajeInfo("No se puede ingresar", "El Plan de Accion invalido");
						return;
					}
					else {
						tab_respuesta.insertar();
						tab_respuesta.setValor("ide_seusu", usuario_responsable);	
						tab_respuesta.setValor("ide_serec", tab_recomendacion.getValorSeleccionado());	
						tab_respuesta.setValor("ide_seinf",  tab_informe.getValorSeleccionado());
	  	 if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
						}else {
							tab_respuesta.setValor("ide_setre",  ""+1);
	  		 
						}
						tab_respuesta.setValor("ide_seemp", empresa);	
						tab_respuesta.setValor("activo_seres", ""+true);
						insertarTablaPlanAccion(usuario_responsable, tab_recomendacion.getValor("IDE_SEREC"), utilitario.getFechaActual(), null, 2, "", tab_informe.getValor("ide_seinf"), null, "PLAN DE ACCION");
						ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
						 ide_sepla_=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
						tab_respuesta.setValor("ide_sepla",""+ ide_sepla_);	
						}
					}
				}
				}//Else 
			  	}
			}else {//Sin recomendaciones
				utilitario.agregarMensajeInfo("No se puede ingresar", "El Plan de Accion no contiene recomendaciones asignadas");
				return;
			} 
				
	  	 }

	  	 
	}
	  	 	  	 
	@Override
	public void guardar() {

		TablaGenerica tab_actualizar_campos=null;
		boolean band=false,bandInsercion=false;
		int ide_sepla=0,ide_sepla_=0;
		String mecanismo_control_seres="",nombre_mecanismo_seres="";
	//	if (tab_respuesta.isFocus()) {
			if (tab_respuesta.guardar() || tab_informe.guardar() ||  tab_recomendacion.guardar() || tab_informe_adjunto.guardar()) {
				//Bandera para conocer si es nuevo registro
				bandInsercion=false;
				 TablaGenerica tab_consulta =utilitario.consultar("select * from seg_respuesta where ide_seres="+tab_respuesta.getValor("ide_seres"));
				 TablaGenerica tab_consulta_inicio = null;
				 	boolean actualizar=false;
				 	if (tab_consulta.getTotalFilas()==0 ) {
				 		actualizar=false;
						 tab_consulta_inicio =utilitario.consultar("select * from seg_respuesta where ide_sepla="+ide_sepla_notificacion+" order by ide_seres asc");
		}else {
						actualizar=true;
					 	tab_consulta_inicio =utilitario.consultar("select * from seg_respuesta where ide_sepla="+tab_respuesta.getValor("ide_sepla")+" order by ide_seres asc");
					 	ide_sepla_notificacion=Integer.parseInt(tab_respuesta.getValor("ide_sepla"));
					}
			
				 	if (actualizar==true) {
						if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
							if (tab_respuesta.getValor("ide_setre").equals("1") || tab_respuesta.getValor("ide_setre").equals("3") 
								|| tab_respuesta.getValor("ide_setre").equals("8") ||  tab_respuesta.getValor("ide_setre").equals("5") 
								||  tab_respuesta.getValor("ide_setre").equals("4")) {
																							
								tab_respuesta.guardar();	
								
								if(estadoRespuesta==3){
								int ide_seres=0;
								ide_seres=Integer.parseInt(tab_respuesta.getValorSeleccionado());
								
								if(ide_sepla_notificacion==ide_sepla_notificacion_nuevo){
									
								}else {
						
									
									TablaGenerica tab_consulta_notificaciones = utilitario.consultar("select * from seg_respuesta "
											+ "where ide_sepla="+ide_sepla_notificacion_nuevo);
									int cont_aplicadas=0,cont_aprobadas=0,cont_notificaciones=0,cont_avance=0,cont_notificaciones_rechazo=0;
										for (int i = 0; i < tab_consulta_notificaciones.getTotalFilas(); i++) {
											if (tab_consulta_notificaciones.getValor(i,"ide_setre").equals("1")) {// si es avance
												cont_avance++;
												i = tab_consulta_notificaciones.getTotalFilas();								
											}
										else if (tab_consulta_notificaciones.getValor(i,"ide_setre").equals("5")) {//APLICADA
												cont_aplicadas++;
												i = tab_consulta_notificaciones.getTotalFilas();								

											}else if (tab_consulta_notificaciones.getValor(i,"ide_setre").equals("6")) {//APROBADA
												cont_aprobadas++;
												i = tab_consulta_notificaciones.getTotalFilas();								

											}else if (tab_consulta_notificaciones.getValor(i,"ide_setre").equals("3")) {//  NOTIFICACION
												cont_notificaciones++;
												//i = tab_consulta_notificaciones.getTotalFilas();								

											}else if (tab_consulta_notificaciones.getValor(i,"ide_setre").equals("4")) {//NOTIFICACION DE RECHAZO
											cont_notificaciones_rechazo++;	
											i = tab_consulta_notificaciones.getTotalFilas();								

											}					
										}//for
									
									
									if (cont_avance>0 || cont_aplicadas>0 || cont_aprobadas>0 || cont_notificaciones_rechazo>0 ) {
											System.out.println("IDE_SEPLA ANTERIOR: "+ide_sepla_notificacion +"  IDE_SEPLA DUPLICADO: "+ide_sepla_notificacion_nuevo);
									}else {
										
									
								utilitario.getConexion().ejecutarSql("update seg_respuesta set ide_sepla="+ide_sepla_notificacion+" where ide_seres=" + ide_seres);
								utilitario.getConexion().ejecutarSql("delete from seg_plan_accion where ide_sepla="+ide_sepla_notificacion_nuevo);

								}
									
									
								}

								

									
								}//si no es una 
								
								
								guardarPantalla();
							}else if (tab_respuesta.getValor("ide_setre").equals("6")) {
			
								if (tab_respuesta.getValor("mecanismo_reporte_seres")==null || tab_respuesta.getValor("mecanismo_reporte_seres").equals("") || tab_respuesta.getValor("mecanismo_reporte_seres").isEmpty()) {
									utilitario.agregarMensajeInfo("Sin mecanismo de reporte", "Recuerde que no se ha registrado un adjunto");
								//	return;
			}					
						 		/*TablaGenerica  tab_respuesta_=utilitario.consultar("select * from seg_respuesta where ide_seres="+tab_respuesta.getValor("ide_seres"));
						 		tab_respuesta.setValor("descripcion_seres",tab_respuesta_.getValor("descripcion_seres") );	
								tab_respuesta.setValor("fecha_desde_seres", tab_respuesta_.getValor("fecha_desde_seres"));	
								tab_respuesta.setValor("fecha_desde_seres",  tab_respuesta_.getValor("fecha_hasta_seres"));
								tab_respuesta.setValor("medio_verificacion_seres", tab_respuesta_.getValor("medio_verificacion_seres"));	
								tab_respuesta.setValor("ide_gtemp_responsable",tab_respuesta_.getValor("ide_gtemp_responsable") );	
								tab_respuesta.setValor("descripcion_repro_seres", tab_respuesta_.getValor("descripcion_repro_seres"));	*/
								guardarPantalla();
							}/*else {
									tab_respuesta.actualizar();
								utilitario.addUpdate("tab_respuesta");
								utilitario.agregarMensaje("No se puede actualizar el registro", "Por favor contactese con el Administrador");
								return;
							}*/
					}else{
							if (tab_respuesta.getValor("ide_setre").equals("1")) {
								utilitario.getConexion().ejecutarSql("update seg_plan_accion set  notificacion_sepla=false   where ide_sepla="+ide_sepla_notificacion);
								guardarPantalla();
							}else if (tab_respuesta.getValor("ide_setre").equals("6")) {
								if (tab_respuesta.getValor("mecanismo_reporte_seres")==null || tab_respuesta.getValor("mecanismo_reporte_seres").equals("") || tab_respuesta.getValor("mecanismo_reporte_seres").isEmpty()) {
									tab_respuesta.actualizar();
									utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar un archivo adjunto");
						return;
								} else {
									utilitario.getConexion().ejecutarSql("update seg_respuesta set mecanismo_reporte_seres='"+tab_respuesta.getValor("mecanismo_reporte_seres")+"' where ide_seres="+tab_respuesta.getValor("ide_seres"));
									utilitario.agregarMensaje("Se guardo Correctamente", "Registro actualizado correctamente");	
					}
						 		/*TablaGenerica  tab_respuesta_=utilitario.consultar("select * from seg_respuesta where ide_seres="+tab_respuesta.getValor("ide_seres"));
						 		tab_respuesta.setValor("descripcion_seres",tab_respuesta_.getValor("descripcion_seres") );	
								tab_respuesta.setValor("fecha_desde_seres", tab_respuesta_.getValor("fecha_desde_seres"));	
								tab_respuesta.setValor("fecha_desde_seres",  tab_respuesta_.getValor("fecha_hasta_seres"));
								tab_respuesta.setValor("medio_verificacion_seres", tab_respuesta_.getValor("medio_verificacion_seres"));	
								tab_respuesta.setValor("ide_gtemp_responsable",tab_respuesta_.getValor("ide_gtemp_responsable") );	
								tab_respuesta.setValor("descripcion_repro_seres", tab_respuesta_.getValor("descripcion_repro_seres"));	
								guardarPantalla();*/
							}else if (tab_respuesta.getValor("ide_setre").equals("4") ||  tab_respuesta.getValor("ide_setre").equals("8")) {
								//tab_respuesta.actualizar();
								//utilitario.agregarMensaje("No se puede realizar esta acción", "Pongase en contacto con el administrador"); 
								//return;
								if (tab_respuesta.getValor("ide_setre").equals("8")) {
								if (tab_respuesta.getValor("mecanismo_reporte_seres")==null || tab_respuesta.getValor("mecanismo_reporte_seres").equals("") || tab_respuesta.getValor("mecanismo_reporte_seres").isEmpty()) {
									utilitario.agregarMensajeInfo("Sin mecanismo de reporte", "Recuerde que no se ha registrado un adjunto");
								return;
								}else {
									utilitario.getConexion().ejecutarSql("update seg_respuesta set mecanismo_reporte_seres='"+tab_respuesta.getValor("mecanismo_reporte_seres")+"' where ide_seres="+tab_respuesta.getValor("ide_seres"));
									utilitario.agregarMensaje("Se guardo Correctamente", "Registro actualizado correctamente");	
								} 
								}else {
									guardarPantalla();
								} 
							}
		
							else {
								tab_respuesta.actualizar();
								utilitario.addUpdate("tab_respuesta");
								utilitario.agregarMensaje("No se puede actualizar el registro", "Por favor contactese con el Administrador");
								return;
			
							}
			
			}
			
		
					}else {
						
		
		
		  	 if (tab_respuesta.getValor("fecha_desde_seres")==null || tab_respuesta.getValor("fecha_desde_seres").isEmpty() || tab_respuesta.getValor("fecha_desde_seres").equals("") ) {
					utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar fecha desde");
		  		 return;
				}
	  		 	
		  	 if (tab_respuesta.getValor("fecha_hasta_seres")==null || tab_respuesta.getValor("fecha_hasta_seres").isEmpty() || tab_respuesta.getValor("fecha_hasta_seres").equals("") ) {
					utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar fecha hasta");
		  		 return;
			}
	  		 	
	  		 	if (tab_respuesta.getValor("fecha_desde_seres").equals(tab_respuesta.getValor("fecha_hasta_seres"))) {
					
				}else{
	  			if (utilitario.isFechaMayor(utilitario.DeStringADate(tab_respuesta.getValor("fecha_hasta_seres")),utilitario.DeStringADate(tab_respuesta.getValor("fecha_desde_seres")))==false) {
					utilitario.agregarMensajeInfo("No se puede guardar fechas invalidas", "Fecha desde mayor a fecha hasta");
					return;
				}	
				}
	  		 	

		 if (tab_respuesta.getValor("ide_setre").equals("1") || tab_respuesta.getValor("ide_setre").equals("3")) {	//Si es una accion de tipo avance
			 if (tab_respuesta.getValor("ide_setre").equals("1")){
				 tab_respuesta.setValor("mecanismo_reporte_seres", null);
				 tab_respuesta.setValor("nombre_archivo_seres", null);
				 utilitario.addUpdateTabla(tab_respuesta, "mecanismo_reporte_seres,nombre_archivo_seres", "");
				 bandInsercion=true;		 
			 }else {
				
				 
			
				 
				 
			 }
	  		
		 }else{
	  		if (tab_respuesta.getValor("mecanismo_reporte_seres")==null || tab_respuesta.getValor("mecanismo_reporte_seres").equals("") || tab_respuesta.getValor("mecanismo_reporte_seres").isEmpty()) {
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar un archivo adjunto");
				return;
			} 
	  	 }
	  	 
		
				
		if (validarEstadoAsignacion()) {
			utilitario.agregarMensajeInfo("No se puede puede actualizar","El tipo de accion no permite realizar la accion requerida" );
			tab_respuesta.actualizar();
			utilitario.addUpdate("tab_tabulador:tab_respuesta,tab_respuesta");
			band=false;
			return;
		}


      		
		if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
      		
	  	if (tab_respuesta.getValor("ide_setre").equals("3")){
	  	try {
		estadoRespuesta=3;
		 ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
	     TablaGenerica tab_empleado=utilitario.consultar("SELECT * FROM  "
								+ "SEG_ASIGNACION ASI "
								+ "left join seg_empresa emp on emp.ide_seemp=asi.ide_seemp "
								+ "left join seg_usuario usu on usu.ide_seemp=asi.ide_seemp "
								+ "left join sis_usuario usua on usua.ide_usua=usu.ide_usua "
								+ "WHERE asi.IDE_seinf="+tab_informe.getValor("ide_seinf")+" and asi.ide_serec="+tab_recomendacion.getValor("ide_serec")+" and usu.activo_seusu=true");
                		
						if (tab_empleado.getTotalFilas()>0) {
							for (int i = 0; i < tab_empleado.getTotalFilas(); i++) {
							//EnviarCorreoAccion(""+ide_sepla, tab_informe.getValor("numero_seinf"), tab_recomendacion.getValor("numero_serec"), "Nro.: "+tab_respuesta.getValor("IDE_SERES")+" Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tab_empleado.getValor(i,"ide_gtemp"), 3,"","");
							}
							utilitario.agregarMensajeInfo("NOTIFICACION REMITIDA", "SE HA INFORMADO AL RESPONSABLE MEDIANTE CORREO ELECTRONICO");
                	
                	}else {
							utilitario.agregarMensajeInfo("NO SE PUDO ENVIAR NOTIFICACION", "NO EXISTEN CORREO ELECTRONICOS VALIDOS");
							return;
						}
						//tab_respuesta.guardar();	
                		guardarPantalla();    
						
						int ide_seres=0,cont_aplicadas=0,cont_aprobadas=0,cont_notificaciones=0,cont_avance=0,cont_notificaciones_rechazo=0;

						ide_seres=Integer.parseInt(tab_respuesta.getValorSeleccionado());						
							TablaGenerica tab_placcion = utilitario.consultar("select * from seg_plan_accion "
									+ "where ide_serec="+tab_recomendacion.getValor("IDE_SEREC")+" AND IDE_SEINF="+tab_informe.getValor("ide_seinf")+" "
											+ "and ide_sepla not in("+ide_sepla_notificacion+") "
											+ "order by ide_sepla desc ");
							
							if (tab_placcion.getTotalFilas()==0) {
								TablaGenerica tab_placcion_temp = utilitario.consultar("select * from seg_respuesta "
										+ "where ide_serec="+tab_recomendacion.getValor("IDE_SEREC")+" AND IDE_SEINF="+tab_informe.getValor("ide_seinf")+" "
												+ "and ide_sepla  in("+ide_sepla_notificacion+") "
												+ "order by ide_sepla desc ");
								
								cont_aplicadas=0;cont_aprobadas=0;cont_notificaciones=0;cont_avance=0;cont_notificaciones_rechazo=0;
								for (int i = 0; i < tab_placcion_temp.getTotalFilas(); i++) {
									if (tab_placcion_temp.getValor(i,"ide_setre").equals("1")) {// si es avance
										cont_avance++;
										//i = tab_placcion.getTotalFilas();								
									}
								else if (tab_placcion_temp.getValor(i,"ide_setre").equals("5")) {//APLICADA
										cont_aplicadas++;
										//i = tab_placcion.getTotalFilas();								

									}else if (tab_placcion_temp.getValor(i,"ide_setre").equals("6")) {//APROBADA
										cont_aprobadas++;
										//i = tab_placcion.getTotalFilas();								

									}else if (tab_placcion_temp.getValor(i,"ide_setre").equals("3")) {//  NOTIFICACION
										cont_notificaciones++;
										//i = tab_consulta_notificaciones.getTotalFilas();								

									}else if (tab_placcion_temp.getValor(i,"ide_setre").equals("4")) {//NOTIFICACION DE RECHAZO
									cont_notificaciones_rechazo++;	
									//i = tab_placcion.getTotalFilas();								

									}					
								}
								
								if (cont_avance>0 || cont_aplicadas>0 || cont_aprobadas>0 || cont_notificaciones_rechazo>0 ) {
									
								}else if (cont_avance==0 && cont_aplicadas==0 && cont_aprobadas==0 && cont_notificaciones_rechazo==0  &&  cont_notificaciones>=0 ) {
        						utilitario.getConexion().ejecutarSql("update seg_plan_accion set notificacion_sepla=true "
											+ "where ide_sepla="+ide_sepla_notificacion);
								}

							}else {
								
							}
							
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	  				}else {
	  				//	utilitario.agregarMensajeInfo("No se puede ingresar", "Unicamente se puede registrar Notificaciones");
					//	return;
						guardarPantalla();

					}
    	}else {
			
	  		 if (tab_respuesta.getValor("ide_setre").equals("6")) {

			}
                		guardarPantalla();  
                		if (bandInsercion==true) {
                			/*TablaGenerica tab_actualizaRespuesta= utilitario.consultar("select * from ")	
                			utilitario.getConexion().ejecutarSql("update seg_respuesta set mecanismo_reporte_seres=null,nombre_archivo_seres=null where ide_sepla=" + tab_respuesta.getValor("ide_sepla")+" and ide_setre=1 and activo_seres=true");
                			 */
			}

		try {
		} catch (Exception e) {
			//System.out.println("Error envio Correo electronico accion: "+tab_plan_accion.getValor("ide_sepla")+","+ tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf")+", "+tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec")+","+
			//tab_respuesta.getValor("ide_seres")+" Asunto: "+tab_respuesta.getValor("descripcion_seres"));
			}
			}
		
	  	 }
    /*	if (band==true ) {
        	   }else{
        		   guardarPantalla();        		
        	}*/
				 	int totalFilasInicio=0,totalFilasFin=0;
				 	TablaGenerica tab_consulta_fin =null;
				 	if (actualizar=false) {
					tab_consulta_fin=utilitario.consultar("select * from seg_respuesta where ide_sepla="+ide_sepla_notificacion+" order by ide_seres asc");
				 	totalFilasInicio=tab_consulta_inicio.getTotalFilas();
				 	totalFilasFin=tab_consulta_fin.getTotalFilas();
				 	}else {
				 	tab_consulta_fin=utilitario.consultar("select * from seg_respuesta where ide_sepla="+tab_respuesta.getValor("ide_sepla")+" order by ide_seres asc");
				 	totalFilasInicio=tab_consulta_inicio.getTotalFilas();
				 	totalFilasFin=tab_consulta_fin.getTotalFilas();	
					}
			 		

				 	
				 if (totalFilasInicio==0 && totalFilasFin==0) {
					return;
				}else	if (totalFilasInicio==0 && totalFilasFin>0) {
						for (int i = 0; i < tab_consulta_fin.getTotalFilas(); i++) {
							if (tab_consulta_fin.getValor(i,"mecanismo_reporte_seres")==null || tab_consulta_fin.getValor(i,"mecanismo_reporte_seres").isEmpty() || tab_consulta_fin.getValor(i,"mecanismo_reporte_seres").equals("")) {
								
							}else {
								insertarHistorialMecanismo(empleado, utilitario.getFechaActual(), utilitario.getHoraActual(), Integer.parseInt(tab_consulta_fin.getValor(i,"ide_seres")),tab_consulta_fin.getValor(i, "mecanismo_reporte_seres"));
							}
						}
						return;
					}else if ((totalFilasInicio==totalFilasFin) && totalFilasInicio>0 && totalFilasFin>0) {
						
						
						for (int i = 0; i < tab_consulta_inicio.getTotalFilas(); i++) {
							if (tab_consulta_inicio.getValor(i,"mecanismo_reporte_seres")==null || tab_consulta_inicio.getValor(i,"mecanismo_reporte_seres").isEmpty() || tab_consulta_inicio.getValor(i,"mecanismo_reporte_seres").equals("")) {
								
								if (tab_consulta_fin.getValor(i,"mecanismo_reporte_seres")==null || tab_consulta_fin.getValor(i,"mecanismo_reporte_seres").isEmpty() || tab_consulta_fin.getValor(i,"mecanismo_reporte_seres").equals("")) {
									
								}else {
									insertarHistorialMecanismo(empleado, utilitario.getFechaActual(), utilitario.getHoraActual(), Integer.parseInt(tab_consulta_fin.getValor(i,"ide_seres")),tab_consulta_fin.getValor(i, "mecanismo_reporte_seres"));
								}
								
								
							}else {
								
							if (tab_consulta_inicio.getValor(i,"mecanismo_reporte_seres").equals(tab_consulta_fin.getValor(i,"mecanismo_reporte_seres"))) {
								
							}else {
								insertarHistorialMecanismo(empleado, utilitario.getFechaActual(), utilitario.getHoraActual(), Integer.parseInt(tab_consulta_fin.getValor(i,"ide_seres")),tab_consulta_fin.getValor(i, "mecanismo_reporte_seres"));
							}
							
			}
    	}
    	
						
						return;
					}else if (totalFilasInicio>totalFilasFin) {
				
						guardarPantalla();
						/*for (int i = 0; i < (tab_consulta_inicio.getTotalFilas()-1); i++) {
							if (tab_consulta_inicio.getValor(i,"mecanismo_reporte_seres")==null || tab_consulta_inicio.getValor(i,"mecanismo_reporte_seres").isEmpty() || tab_consulta_inicio.getValor(i,"mecanismo_reporte_seres").equals("")) {
								
								if (tab_consulta_fin.getValor(i,"mecanismo_reporte_seres")==null || tab_consulta_fin.getValor(i,"mecanismo_reporte_seres").isEmpty() || tab_consulta_fin.getValor(i,"mecanismo_reporte_seres").equals("")) {
									
								}else {
									insertarHistorialMecanismo(empleado, utilitario.getFechaActual(), utilitario.getHoraActual(), Integer.parseInt(tab_consulta_fin.getValor(i,"ide_seres")),tab_consulta_fin.getValor(i, "mecanismo_reporte_seres"));
								}
								
								
							}else {
								
							if (tab_consulta_inicio.getValor(i,"mecanismo_reporte_seres").equals(tab_consulta_fin.getValor(i,"mecanismo_reporte_seres"))) {
								
							}else {
								insertarHistorialMecanismo(empleado, utilitario.getFechaActual(), utilitario.getHoraActual(), Integer.parseInt(tab_consulta_fin.getValor(i,"ide_seres")),tab_consulta_fin.getValor(i, "mecanismo_reporte_seres"));
							}
							
							}
						}
						if (tab_consulta_inicio.getValor(tab_consulta_inicio.getTotalFilas(),"mecanismo_reporte_seres")==null || tab_consulta_inicio.getValor(tab_consulta_inicio.getTotalFilas(),"mecanismo_reporte_seres").isEmpty() || tab_consulta_inicio.getValor(tab_consulta_inicio.getTotalFilas(),"mecanismo_reporte_seres").equals("")) {
						
						}else {
							insertarHistorialMecanismo(empleado, utilitario.getFechaActual(), utilitario.getHoraActual(), Integer.parseInt(tab_consulta_inicio.getValor(tab_consulta_inicio.getTotalFilas(),"ide_seres")),tab_consulta_inicio.getValor(tab_consulta_inicio.getTotalFilas(), "mecanismo_reporte_seres"));
						}
						
						
						
				 	return;*/
		
				 	
			}//ELSE GUARDAR RESPUESTA
				 
					else  if ((totalFilasInicio<totalFilasFin)) {
						for (int i = 0; i < (tab_consulta_fin.getTotalFilas()-1); i++) {
							if (tab_consulta_fin.getValor(i,"mecanismo_reporte_seres")==null || tab_consulta_fin.getValor(i,"mecanismo_reporte_seres").isEmpty() || tab_consulta_fin.getValor(i,"mecanismo_reporte_seres").equals("")) {
								
								if (tab_consulta_inicio.getValor(i,"mecanismo_reporte_seres")==null || tab_consulta_inicio.getValor(i,"mecanismo_reporte_seres").isEmpty() || tab_consulta_inicio.getValor(i,"mecanismo_reporte_seres").equals("")) {
									
								}else {
									insertarHistorialMecanismo(empleado, utilitario.getFechaActual(), utilitario.getHoraActual(), Integer.parseInt(tab_consulta_fin.getValor(i,"ide_seres")),tab_consulta_fin.getValor(i, "mecanismo_reporte_seres"));
								}
								
								
							}else {
								
							if (tab_consulta_inicio.getValor(i,"mecanismo_reporte_seres").equals(tab_consulta_fin.getValor(i,"mecanismo_reporte_seres"))) {
								
							}else {
								insertarHistorialMecanismo(empleado, utilitario.getFechaActual(), utilitario.getHoraActual(), Integer.parseInt(tab_consulta_fin.getValor(i,"ide_seres")),tab_consulta_fin.getValor(i, "mecanismo_reporte_seres"));
							}
							
							}
						}
						if (tab_consulta_fin.getValor(tab_consulta_fin.getTotalFilas() - 1,"mecanismo_reporte_seres")==null || tab_consulta_fin.getValor(tab_consulta_fin.getTotalFilas() - 1 ,"mecanismo_reporte_seres").isEmpty() || tab_consulta_fin.getValor(tab_consulta_fin.getTotalFilas() - 1,"mecanismo_reporte_seres").equals("")) {
						
						}else {
							insertarHistorialMecanismo(empleado, utilitario.getFechaActual(), utilitario.getHoraActual(), Integer.parseInt(tab_consulta_fin.getValor(tab_consulta_fin.getTotalFilas() -1,"ide_seres")),tab_consulta_fin.getValor(tab_consulta_fin.getTotalFilas()-1, "mecanismo_reporte_seres"));
						}
						
						
						
					 	return;

					}
    	}
	//	}
		/*if(tab_respuesta_adjunto.isFocus()){
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
		}*/

	}



	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
		if (tab_respuesta.isFocus()) {
			if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
		 		if (tab_respuesta.getValor("ide_setre").equals("1") ){
		 			utilitario.getConexion().ejecutarSql("delete from seg_respuesta where ide_seres=" + tab_respuesta.getValor("ide_seres"));
		 			tab_respuesta.actualizar();
		 			
		 		}else {

		 		}

 	 }else {
		if (tab_respuesta.getValor("ide_setre").equals("1") ){
			//if(tab_respuesta.getValor("mecanismo_reporte_seres").isEmpty() || tab_respuesta.getValor("mecanismo_reporte_seres")==null || tab_respuesta.getValor("mecanismo_reporte_seres").equals("")) {
 			utilitario.getConexion().ejecutarSql("delete from seg_respuesta where ide_seres=" +tab_respuesta.getValor("ide_seres"));
 			utilitario.agregarNotificacion("Acción Eliminada Correctamente", "","");

 			tab_respuesta.actualizar();
			//}else {
		//		utilitario.agregarNotificacionInfo("No se puede realizar esta accion", "Contactese con el Administrador");
		//		return;
		//	} 

	}else {
		utilitario.agregarNotificacionInfo("No se puede realizar esta accion", "Contactese con el Administrador");
		return;
	} 
 	}
		/*if (tab_respuesta.getValor("ide_setre").equals("1") ){
			if(tab_respuesta.getValor("mecanismo_reporte_seres").isEmpty() || tab_respuesta.getValor("mecanismo_reporte_seres")==null || tab_respuesta.getValor("mecanismo_reporte_seres").equals("")) {
		utilitario.getTablaisFocus().eliminar();
			}else {
				utilitario.agregarNotificacionInfo("No se puede realizar esta accion", "Contactese con el Administrador");
				return;
			} 
		}else {
			utilitario.agregarNotificacionInfo("No se puede realizar esta accion", "Contactese con el Administrador");
			return;*/
		}
			
		
		
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

	  	 try {
			if (ide_secar.equals("1") && !ide_secar.isEmpty()) {
				tab_respuesta.setCondicion("IDE_SEINF="+tab_informe.getValor("IDE_SEINF")+""
						+ " AND IDE_SEREC="+tab_recomendacion.getValor("IDE_SEREC")+" and  ide_sepla in (select ide_sepla from seg_plan_accion where activo_sepla=true) and activo_seres=true");
				tab_respuesta_historial.setCondicion("ide_serec="+tab_recomendacion.getValor("ide_serec")+" and ide_sepla is null" );
				tab_respuesta.ejecutarSql();
				tab_respuesta_historial.ejecutarSql();
				utilitario.addUpdate("tab_tabulador,tab_tabulador:tab_respuesta,tab_tabulador:tab_respuesta_historial");
				actualizarInformativo();
				actualizarInformativoRecomendacion();
			 }else {
				 TablaGenerica tabRespuesta=utilitario.consultar("select * "
				 		+ "from seg_respuesta  "
				 		+"WHERE IDE_SEINF="+tab_informe.getValor("IDE_SEINF")+" "
						+ " AND IDE_SEREC="+tab_recomendacion.getValor("IDE_SEREC")+" and  ide_sepla in (select ide_sepla from seg_plan_accion where activo_sepla=true) ");
				 
				 
				if (tabRespuesta.getTotalFilas()<=0) {
					tab_respuesta.setCondicion("IDE_SEREC=-1");
				}else {
					tab_respuesta.setCondicion("IDE_SEINF="+tab_informe.getValor("IDE_SEINF")+""
						+ " AND IDE_SEREC="+tab_recomendacion.getValor("IDE_SEREC")+" and  ide_sepla in (select ide_sepla from seg_plan_accion where activo_sepla=true) and activo_seres=true");
				}
				tab_respuesta.ejecutarSql();
				
				 TablaGenerica tabRespuestaHistorial=utilitario.consultar("select * "
					 		+ "from seg_respuesta  "
					 		+"WHERE IDE_SEINF="+tab_informe.getValor("IDE_SEINF")+" "
							+ " AND IDE_SEREC="+tab_recomendacion.getValor("IDE_SEREC")+" and ide_sepla is null");
					 
					 
					if (tabRespuestaHistorial.getTotalFilas()<=0) {
						tab_respuesta_historial.setCondicion("IDE_SEREC=-1");
					}else {
								tab_respuesta_historial.setCondicion("IDE_SEINF="+tab_informe.getValor("IDE_SEINF")+" and "
								+ "IDE_SEREC="+tab_recomendacion.getValor("IDE_SEREC")+" and ide_sepla is null");					
				 		
					}
				tab_respuesta_historial.ejecutarSql();
				utilitario.addUpdate("tab_tabulador,tab_respuesta,tab_tabulador:tab_respuesta,tab_respuesta_historial,tab_tabulador:tab_respuesta_historial");
				actualizarInformativo();
				actualizarInformativoRecomendacion();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
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
	//System.out.println("Actualizar estado de accion: "+tab_plan_accion.getValor("ide_sepla")+","+ tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf")+", "+tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec")+","+
		//	tab_respuesta.getValor("ide_seres")+" Asunto: "+tab_respuesta.getValor("descripcion_seres"));
	//return;s
	//EnviarCorreoAccion(tab_plan_accion.getValor("ide_sepla"), tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf"), tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec"),
//			" Asunto: "+tab_respuesta.getValor("descripcion_seres"),empleado,1,"","");
	
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


public Tabla getTab_respuesta_adjunto_historial() {
	return tab_respuesta_adjunto_historial;
}


public void setTab_respuesta_adjunto_historial(
		Tabla tab_respuesta_adjunto_historial) {
	this.tab_respuesta_adjunto_historial = tab_respuesta_adjunto_historial;
}



public Tabla getTab_respuesta_historial() {
	return tab_respuesta_historial;
}


public void setTab_respuesta_historial(Tabla tab_respuesta_historial) {
	this.tab_respuesta_historial = tab_respuesta_historial;
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



public String emailLinkEncuestaCorreo(String strNombreEmpleado ,String ide_sepla,String informe, String recomendacion, String acciones,String ide_gtemp, int tipo_rol,String fecha_repro,String descripcion_repro) {
	 
	String html="";
	 /*="<p>Estimado "+strNombreEmpleado+ ", "
             + "</p>\n"
             + "<p>&nbsp;</p>\n";*/
    
	if (tipo_rol==1) {
	//Tipo rol cambio estado accion
		html+="<p> Notificamos mediante la presente que se ha cambiado el estado del Plan de Accion para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" .</p>\n"
	             + "<p>&nbsp;</p>\n" 
	             + "<p>Recuerde el cumplimiento del plan de accion debe cumplirse en las fechas  ingresadas </p>\n"
	             + "<p>&nbsp;</p>\n"
	             + "<p>Saludos cordiales,</p>\n";
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
	             + "</tr>\n"
	             + "</tbody>\n"
	             + "</table>";
	}
	else if (tipo_rol==2) {
		//ENVIO PLAN DE ACCION
        html+="<p>Estimado Delegado(a) del Seguimiento de Recomendaci&oacuten </p>\n"
      	+ "En atenci&oacuten a la notificaci&oacuten y disposici&oacuten de cumplimiento de la recomendaci&oacuten N."+recomendacion+" , me permito informar que se ha ingresado el plan de acci&oacuten para dar cumplimiento a la recomendaci&oacuten, solicito la aceptaci&oacuten del mismo. "
        + "<p>&nbsp;</p>\n" 
        + "<p>Saludos cordiales,</p>\n";

        html+="<table style=\"height: 144px;\" width=\"571\">\n"
                + "<tbody>\n"
                + "<tr>\n"
                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
                + "<td width=\"476\">\n"
                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>"+strNombreEmpleado+"</strong></p>\n"
                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
                + "</tr>\n"
                + "</tbody>\n"
                + "</table>";
        //System.out.println("2 :"+html);
	}
	
	
	
	
	else if (tipo_rol==3) {
	//Notificacion
		
        html+="<p>En el artículo 8 de la Ley Orgánica de la Contraloría General del Estado en el artículo No. 9 establece las Recomendaciones de auditoría. - "
        		+ "Las recomendaciones de auditoría, una vez comunicadas a las instituciones del Estado y a sus servidores, deben ser aplicadas de manera inmediata y con el "
        		+ "carácter de obligatorio; serán objeto de seguimiento y su inobservancia será sancionada por la Contraloría General del Estado. </p>\n"
        		+ "<p>Con Resolución Administrativa No. EMGIRS EP- GGE-CJU-2020-019 en su Art.1 Delegar al/la Gerente de Desarrollo Organizacional, o quien  "
        		+ "cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y en representación de la Gerente General de la Empresa Pública Metropolitana "
        		+ "de Gestión Integral de Residuos Sólidos EMGIRS-EP, realice el seguimiento mensual de las recomendaciones de auditoría realizadas mediante los diferentes "
        		+ "exámenes especiales o auditorías, e informe a la Gerencia de su seguimiento y cumplimiento.</p>\n"
        		+ "<p>Con los antecedentes expuestos y dando cumplimiento a la delegación dada a la Gerencia de Desarrollo Organizacional, pongo en conocimiento "
        		+ "(Memorando y/o informe de seguimiento y control de las recomendaciones emitidas por la Contraloría General del Estado o Auditoria Interna) para la aplicación "
        		+ "de las acciones según correspondan. </p>\n";
		
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
                + "</tr>\n"
                + "</tbody>\n"
                + "</table>";
///System.out.println("NOTIFICACION 1:" +html);

	}	
	else if (tipo_rol==4) {
	//Notificacion de Rechazo
		html+="<p>Estimado(a) "+strNombreEmpleado+", </p>\n"
		+ "<p> El 27 de agosto de 2020, la Gerente General de la EMGIRS EP, mediante resoluci&oacuten administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute: "
		+ "Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y en representaci&oacuten "
		+ "de la Gerente General de la Empresa P&uacuteblica Metropolitana de Gesti&oacuten Integral de Residuos S&oacutelidos EMGIRS-EP, realice el seguimiento mensual de las recomendaciones de "
		+ "auditor&iacutea realizadas mediante los diferentes ex&aacutemenes especiales o auditor&iacuteas, e informe a la gerencia de su seguimiento y cumplimiento. </p>\n"
		+ "<p>La Gerencia de Desarrollo Organizacional en funci&oacuten a lo que establece el Procedimiento de Seguimiento y Control de Recomendaciones emitidas por la Contralor&iacutea General del Estado vigente, "
		+ "se da la conformidad del plan de acci&oacuten y se recuerda lo siguiente: El Responsable de la Recomendaci&oacuten y el Servidor designado son responsables de:  </p>\n"
		+ "<p>&nbsp;a	Dar estricto cumplimiento a lo establecido en la recomendaci&oacuten a su cargo.  </p>"
		+ "<p>&nbsp;b	El registro del avance de la documentaci&oacuten que respalde el cumplimiento de la recomendaci&oacuten, de acuerdo a los tiempos establecido en el Plan.  </p>"
		+ "<p>&nbsp;c	Deber&aacuten validar y verificar la pertinencia de la informaci&oacuten previo al registrado en el sistema de recomendaciones.  </p>"
		+ "<p>&nbsp;d	Revisar y mantener actualizado la informaci&oacuten de las recomendaciones que se encuentran cargada en el sistema.  </p>"
		+ "<p>&nbsp;e	Llevar el control de las recomendaciones y su respectivo archivo f&iacutesico y/o digital.  </p>"
		+ "<p>&nbsp;f	Reportar el avance y el cumplimiento de la recomendaci&oacuten a la M&aacutexima Autoridad o su delegado. </p>\n"
		+ "<p>Con este antecedente, y en calidad de Delegado del Seguimiento al Cumplimiento de las Recomendaciones se "
		+ "rechaza el (plan de acci&oacuten o medio de verificaci&oacuten presentado) de la recomendaci&oacuten No. "+tab_recomendacion.getValor("numero_serec")+" del Informe No. "+tab_informe.getValor("numero_seinf")+" presentado y solicito realizar los "
		+ "ajustes pertinentes conforme a (colocar la informaci&oacuten registrada en el campo de observación) en un plazo de 2 días h&aacutebiles. </p>\n";
		 html+="<table style=\"height: 144px;\" width=\"571\">\n"
	             + "<tbody>\n"
	             + "<tr>\n"
	             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	             + "<td width=\"476\">\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	             + "</tr>\n"
	             + "</tbody>\n"
	             + "</table>";
	
		// System.out.println("notificacion de rechazo"+html);
		 
		 
		 
		 
		 
	}else if (tipo_rol==5) {
	//Plan de accion
		html+="<p> Estimado(a) responsable del cumplimiento de la recomendaci&oacuten:</p>\n"
  		             + "<p>&nbsp;</p>\n" 
	             + "<p>Como Delegado de la Gerencia General y en funci&oacuten a lo que establece el Procedimiento interno de Seguimiento y Control de "
	             + "Recomendaciones emitidas por la Contralor&iacutea General del Estado , se da la conformidad del plan de acci&oacuten y se recuerda lo siguiente: </p>\n"
		         + "</p>\n"
		         +"<p> El Responsable de la Recomendaci&oacuten y el Servidor designado son responsables de: </p>\n"
		         +"<p><strong>Fase de elaboraci&oacuten del plan de acci&oacuten </strong></p>\n"
		         + "<p>a)	Dar estricto cumplimiento a lo establecido en la recomendaci&oacuten a su cargo. "
		         + "<p>b)	Establecer un plan de acci&oacuten por cada recomendaci&oacuten en el sistema e informar mediante memorando al delegado de la M&aacutexima Autoridad conocer y aceptar la responsabilidad de cumplimiento de dicho plan. "
		         +"<p><strong>Fase de reporte al cumplimiento al plan de acci&oacuten </strong></p>\n"
		         + "<p>c)	Deber&aacuten validar y verificar la pertinencia de la informaci&oacuten previo al registrado en el sistema de recomendaciones.  "
		         + "<p>d)	El registro del avance de la documentaci&oacuten que respalde el cumplimiento de la recomendaci&oacuten, de acuerdo a los tiempos establecido en el Plan.  "
		         + "<p>e)	Revisar y mantener actualizado la informaci&oacuten de las recomendaciones que se encuentran cargada en el sistema.  "
		         + "<p>f)	Llevar el control de las recomendaciones y su respectivo archivo f&iacutesico y/o digital.  "
		         + "<p>g)	Reportar el avance y el cumplimiento de la recomendaci&oacuten a la M&aacutexima Autoridad o su delegado. "
		         + "</p>\n"
		         + "<p>Con este antecedente, y en calidad de Delegado(a) del Seguimiento al Cumplimiento de las Recomendaciones se aplica el plan de acci&oacuten para la recomendacion No. "+tab_recomendacion.getValor("numero_serec")+"  del informe No.  "+tab_informe.getValor("numero_seinf")+""
		         + "dispongo a Usted la ejecuci&oacuten de las actividades y reporte de los avances establecidos en el plan de acci&oacuten conforme a los tiempos establecidos.  </p>\n"
  		             + "<p>&nbsp;</p>\n"
  		             + "<p>Saludos cordiales,</p>\n";
		 html+="<table style=\"height: 144px;\" width=\"571\">\n"
	             + "<tbody>\n"
	             + "<tr>\n"
	             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	             + "<td width=\"476\">\n"
	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	             + "</tr>\n"
	             + "</tbody>\n"
	             + "</table>";
			
		 
	}else if (tipo_rol==6) {
//ESTADO APROBADA
		html+="<p> Estimado(a):  "+strNombreEmpleado+"</p>\n"
			+ "<p>El 27 de agosto de 2020, la Gerente General de la EMGIRS EP, mediante resoluci&oacuten administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute: "
			+ "Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre "
			+ "y en representaci&iocuten de la Gerente General de la Empresa P&uacuteblica Metropolitana de Gesti&oacuten Integral de Residuos S&oacutelidos EMGIRS-EP, realice el seguimiento "
			+ "mensual de las recomendaciones de auditor&iacutea realizadas mediante los diferentes ex&aacutemenes especiales o auditor&iacuteas, e informe a la gerencia de "
			+ "su seguimiento y cumplimiento. </p>\n"
			+ "La Gerencia de Desarrollo Organizacional en funci&oacuten a lo que establece el Procedimiento de Seguimiento y Control de Recomendaciones "
			+ "emitidas por la Contralor&iacutea General del Estado vigente, se da la conformidad del plan de acci&oacuten y se recuerda lo siguiente: </p>\n"
			+ "<p>El Responsable de la Recomendaci&oacuten y el Servidor designado son responsables de:  </p>"
			+ "<p>&nbsp;a	Dar estricto cumplimiento a lo establecido en la recomendaci&oacuten a su cargo. </p> "
			+ "<p>&nbsp;b	El registro del avance de la documentaci&oacuten que respalde el cumplimiento de la recomendaci&oacuten, de acuerdo a los tiempos establecido en el Plan. </p> "
			+ "<p>&nbsp;c	Deber&aacuten validar y verificar la pertinencia de la informaci&oacuten previo al registrado en el sistema de recomendaciones. </p> "
			+ "<p>&nbsp;d	Revisar y mantener actualizado la informaci&oacuten de las recomendaciones que se encuentran cargada en el sistema. </p> "
			+ "<p>&nbsp;e	Llevar el control de las recomendaciones y su respectivo archivo f&iacutesico y/o digital.</p> "
			+ "<p>&nbsp;f	Reportar el avance y el cumplimiento de la recomendaci&oacuten a la M&aacutexima Autoridad o su delegado.” </p>\n"

			+ "<p>Con este antecedente, y en calidad de Delegado del Seguimiento al Cumplimiento de las Recomendaciones se acepta y dispongo a Usted la ejecuci&oacuten de las actividades "
			+ "y reporte de los avances establecidos en el plan de acci&oacuten  de la Recomendaci&oacuten No. "+tab_recomendacion.getValor("numero_serec")+"  del informe No.  "+tab_informe.getValor("numero_seinf")+"conforme a los tiempos establecidos.  </p> "
        + "<p>&nbsp;</p>\n"
        + "<p>Saludos cordiales,</p>\n";
        html+="<table style=\"height: 144px;\" width=\"571\">\n"
                + "<tbody>\n"
                + "<tr>\n"
                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
                + "<td width=\"476\">\n"
                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
                + "</tr>\n"
                + "</tbody>\n"
                + "</table>";
   // System.out.println("aprobado "+html);
	}
	else if (tipo_rol==7) {
		//Reprogramcion de actividad Administrador
				//Plan de accion INCUMPLIDA
				html+="<p>Estimado Delegado(a) del Seguimiento de Recomendaci&oacuten </p>\n"
		        		+"<p>En atención a la notificación y disposición de cumplimiento de la recomendación N. "+tab_recomendacion.getValor("numero_serec")+"  "+tab_recomendacion.getValor("asunto_serec")+", "
		        		+ "me permito informar que se ha ingresado la reprogramación del plan de acción para dar cumplimiento a la recomendación por "+descripcion_repro+" con fecha "+fecha_repro+", "
		        		+ "solicito la aceptación del mismo</p>\n"
		        		+ "<p>&nbsp;</p>\n"; 
		   	        
	        html+="<table style=\"height: 144px;\" width=\"571\">\n"
	                + "<tbody>\n"
	                + "<tr>\n"
	                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	                + "<td width=\"476\">\n"
			             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>"+strNombreEmpleado+"</strong></p>\n"
	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	                + "</tr>\n"
	                + "</tbody>\n"
	                + "</table>";
			
		}
	
	
	else if (tipo_rol==8) {
		//Reprogramcion de actividad Administrador
        html+="<p>Estimado(a) "+strNombreEmpleado+",  </p>\n"
        		+"<p>El 27 de agosto de 2020, la Gerente General de la EMGIRS EP, mediante resoluci&oacute;n administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute;: "
        		+ "Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y "
        		+ "en representaci&oacute;n de la Gerente General de la Empresa P&uacute;blica Metropolitana de Gesti&oacute;n Integral de Residuos S&oacute;lidos EMGIRS-EP, realice el seguimiento "
        		+ "mensual de las recomendaciones de auditor&iacute;a realizadas mediante los diferentes ex&aacute;menes especiales o auditor&iacute;as, e informe a la gerencia de su seguimiento y cumplimiento. </p>\n"
        		+ "<p>La Gerencia de Desarrollo Organizacional en funci&oacute;n a lo que establece el Procedimiento de Seguimiento y Control de Recomendaciones emitidas por la Contralor&iacute;a General "
        		+ "del Estado vigente, me permito informar que la recomendaci&oacute;n se encuentra con estado INCUMPLIDO, por lo que se solicita dispongo a Usted la ejecuci&oacute;n de las actividades "
        		+ "y reporte de los avances establecidos en el plan de acci&oacute;n conforme a los tiempos establecidos o en su defecto realizar la solicitud de reprogramaci&oacute;n.</p>\n";
	        html+="<table style=\"height: 144px;\" width=\"571\">\n"
	                + "<tbody>\n"
	                + "<tr>\n"
	                + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
	                + "<td width=\"476\">\n"
	             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
	                + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
	                + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
	                + "</tr>\n"
	                + "</tbody>\n"
	                + "</table>";
		}
	
	else if (tipo_rol==9) {
		//Reprogramcion de actividad Administrador
		html+="<p> Notifico mediante la presente que se ha remitido el plan de accion corregido  para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" .</p>\n"
	             + "<p>&nbsp;</p>\n" 
	             + "<p>&nbsp;</p>\n"
	             + "<p>Saludos cordiales,</p>\n";
  	html+="<table style=\"height: 144px;\" width=\"571\">\n"
                 + "<tbody>\n"
                 + "<tr>\n"
                 + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
                 + "<td width=\"476\">\n"
                 + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>"+strNombreEmpleado+"</strong></p>\n"
                 + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
                 + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
                 + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
                 + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
                 + "</td>\n"
                 + "</tr>\n"
                 + "</tbody>\n"
                 + "</table>";
	

		}
	
	else if (tipo_rol==11) {
		//Plan de accion Reprogramado Accion 
	        html+="<p>Notificamos mediante la presente que se ha reprogramado la "+acciones+" para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
	       + "<p>&nbsp;</p>\n" 
	        + "<p>Recuerde que debe registrar la nueva accion</p>\n"
	        + "<p>&nbsp;</p>\n"
	        + "<p>Saludos cordiales,</p>\n";
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
	                + "</tr>\n"
	                + "</tbody>\n"
	                + "</table>";
		}

	
	else if (tipo_rol==12) {
		//Plan de accion EN ESTADO DE APLICADA
	        html+="<p>Notificamos mediante la presente que el plan de accion se encuentra en estado de aplicada para "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
	       + "<p>&nbsp;</p>\n" 
	        + "<p>Recuerde que debe registrar la nueva accion</p>\n"
	        + "<p>&nbsp;</p>\n"
	        + "<p>Saludos cordiales,</p>\n";
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
	                + "</tr>\n"
	                + "</tbody>\n"
	                + "</table>";
		}
	
	else if (tipo_rol==13) {
		//Plan de accion EN ESTADO DE REPROGRAMADO
	        html+="<p>Notificamos mediante la presente que se ha reprogramado todo el plan de accion para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
	       + "<p>&nbsp;</p>\n" 
	        + "<p>Recuerde que debe registrar la nueva accion</p>\n"
	        + "<p>&nbsp;</p>\n"
	        + "<p>Saludos cordiales,</p>\n";
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
             + "</tr>\n"
             + "</tbody>\n"
             + "</table>";
		}
	
	   
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
		   String ide_sepla,String informe, String recomendacion, String acciones,String ide_gtemp, int tipo_correo,String fecha_repro,String descripcion_repro) throws Exception
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
           String htmlText ="";
              	 /*="<p>Estimado "+strNombreEmpleado+ ", "
                           + "</p>\n"
                           + "<p>&nbsp;</p>\n";*/
                  
           if (tipo_correo==1) {
        		//Tipo rol cambio estado accion
        			htmlText+="<p> Notificamos mediante la presente que se ha cambiado el estado del Plan de Accion para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" .</p>\n"
        		             + "<p>&nbsp;</p>\n" 
        		             + "<p>Recuerde el cumplimiento del plan de accion debe cumplirse en las fechas  ingresadas </p>\n"
        		             + "<p>&nbsp;</p>\n"
        		             + "<p>Saludos cordiales,</p>\n";
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
        		}
        		else if (tipo_correo==2) {
        		//ENVIAR PLAN DE ACCION
        	        htmlText+="<p>Estimado Delegado(a) del Seguimiento de Recomendaci&oacuten </p>\n"
        	              	+ "En atenci&oacuten a la notificaci&oacuten y disposici&oacuten de cumplimiento de la recomendaci&oacuten N."+recomendacion+" , me permito informar que se ha ingresado el plan de acci&oacuten para dar cumplimiento a la recomendaci&oacuten, solicito la aceptaci&oacuten del mismo. "
        	        + "<p>&nbsp;</p>\n" 
        	        + "<p>Saludos cordiales,</p>\n";
        	                
        	    	htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
        	                   + "<tbody>\n"
        	                   + "<tr>\n"
        	                   + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
        	                   + "<td width=\"476\">\n"
        	                        + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>"+strNombreEmpleado+"</strong></p>\n"
        	                   + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
        	                   + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
        	                   + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
        	                   + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
        	                   + "</tr>\n"
        	                   + "</tbody>\n"
        	                   + "</table>";
        	               // System.out.println("2 :"+htmlText);
        		}
        		else if (tipo_correo==3) {
        		//Notificacion
        			
        	        htmlText+="<p>En el art&iacute;culo 8 de la Ley Org&aacute;nica de la Contralor&iacute;a General del Estado en el art&iacute;culo No. 9 establece las Recomendaciones de auditor&iacute;a. - "
        	        		+ "Las recomendaciones de auditor&iacute;a, una vez comunicadas a las instituciones del Estado y a sus servidores, deben ser aplicadas de manera inmediata y con el "
        	        		+ "car&aacute;cter de obligatorio; ser&aacute;n objeto de seguimiento y su inobservancia ser&aacute; sancionada por la Contralor&iacute;a General del Estado. </p>\n"
        	        		+ "<p>Con Resoluci&oacute;n Administrativa No. EMGIRS EP- GGE-CJU-2020-019 en su Art.1 Delegar al/la Gerente de Desarrollo Organizacional, o quien  "
        	        		+ "cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y en representaci&oacute;n de la Gerente General de la Empresa P&uacute;blica Metropolitana "
        	        		+ "de Gesti&oacute;n Integral de Residuos S&oacute;lidos EMGIRS-EP, realice el seguimiento mensual de las recomendaciones de auditor&iacute;a realizadas mediante los diferentes "
        	        		+ "ex&aacute;menes especiales o auditor&iacute;as, e informe a la Gerencia de su seguimiento y cumplimiento.</p>\n"
        	        		+ "<p>Con los antecedentes expuestos y dando cumplimiento a la delegaci&oacute;n dada a la Gerencia de Desarrollo Organizacional, pongo en conocimiento "
        	        		+ "(Memorando y/o informe de seguimiento y control de las recomendaciones emitidas por la Contralor&iacute;a General del Estado o Auditoria Interna) para la aplicaci&oacute;n "
        	        		+ "de las acciones seg&uacute;n correspondan. </p>\n";
        			
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
        		                   + "</tr>\n"
        		                   + "</tbody>\n"
        		                   + "</table>";
        	        //System.out.println("NOTIFICACION 1:" +htmlText);
        		}	
        		else if (tipo_correo==4) {
        		//Notificacion de Rechazo
        			htmlText+="<p>Estimado(a) "+strNombreEmpleado+", </p>\n"
        					+ "<p> El 27 de agosto de 2020, la Gerente General de la EMGIRS EP, mediante resoluci&oacuten administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute: "
        					+ "Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y en representaci&oacuten "
        					+ "de la Gerente General de la Empresa P&uacuteblica Metropolitana de Gesti&oacuten Integral de Residuos S&oacutelidos EMGIRS-EP, realice el seguimiento mensual de las recomendaciones de "
        					+ "auditor&iacutea realizadas mediante los diferentes ex&aacutemenes especiales o auditor&iacuteas, e informe a la gerencia de su seguimiento y cumplimiento. </p>\n"
        					+ "<p>La Gerencia de Desarrollo Organizacional en funci&oacuten a lo que establece el Procedimiento de Seguimiento y Control de Recomendaciones emitidas por la Contralor&iacutea General del Estado vigente, "
        					+ "se da la conformidad del plan de acci&oacuten y se recuerda lo siguiente: El Responsable de la Recomendaci&oacuten y el Servidor designado son responsables de:  </p>\n"
        					+ "<p>&nbsp;a	Dar estricto cumplimiento a lo establecido en la recomendaci&oacuten a su cargo.  </p>"
        					+ "<p>&nbsp;b	El registro del avance de la documentaci&oacuten que respalde el cumplimiento de la recomendaci&oacuten, de acuerdo a los tiempos establecido en el Plan.  </p>"
        					+ "<p>&nbsp;c	Deber&aacuten validar y verificar la pertinencia de la informaci&oacuten previo al registrado en el sistema de recomendaciones.  </p>"
        					+ "<p>&nbsp;d	Revisar y mantener actualizado la informaci&oacuten de las recomendaciones que se encuentran cargada en el sistema.  </p>"
        					+ "<p>&nbsp;e	Llevar el control de las recomendaciones y su respectivo archivo f&iacutesico y/o digital.  </p>"
        					+ "<p>&nbsp;f	Reportar el avance y el cumplimiento de la recomendaci&oacuten a la M&aacutexima Autoridad o su delegado. </p>\n"
        					+ "<p>Con este antecedente, y en calidad de Delegado del Seguimiento al Cumplimiento de las Recomendaciones se "
        					+ "rechaza el (plan de acci&oacuten o medio de verificaci&oacuten presentado) de la recomendaci&oacuten No. "+tab_recomendacion.getValor("numero_serec")+" del Informe No. "+tab_informe.getValor("numero_seinf")+" presentado y solicito realizar los "
        					+ "ajustes pertinentes conforme a (colocar la informaci&oacuten registrada en el campo de observación) en un plazo de 2 días h&aacutebiles. </p>\n";
        					 
        			
        	    	htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
        	                   + "<tbody>\n"
        	                   + "<tr>\n"
        	                   + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
        	                   + "<td width=\"476\">\n"
        				             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
        	                   + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
        	                   + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
        	                   + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
        	                   + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
        	                   + "</tr>\n"
        	                   + "</tbody>\n"
        	                   + "</table>";
        				
        			 //System.out.println("notificacion de rechazo"+htmlText);

        					 

           		//Notificacion de rechazo
        		}else if (tipo_correo==5) {
            		//Plan de accion
        			htmlText+="<p> Notificamos mediante la presente que se ha cambiado el estado de la accion:  "+acciones+" para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" .</p>\n"
       		             + "<p>&nbsp;</p>\n" 
       		             + "<p>Recuerde el cumplimiento del plan de accion debe cumplirse en las fechas  ingresadas </p>\n"
       		             + "<p>&nbsp;</p>\n"
       		             + "<p>Saludos cordiales,</p>\n";
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
            		}  
           		//Aplicada
        		else if (tipo_correo==6) {
//APROBADA
        			htmlText+="<p> Estimado(a):  "+strNombreEmpleado+"</p>\n"
            				+ "<p>El 27 de agosto de 2020, la Gerente General de la EMGIRS EP, mediante resoluci&oacuten administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute: "
            				+ "Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre "
            				+ "y en representaci&iocuten de la Gerente General de la Empresa P&uacuteblica Metropolitana de Gesti&oacuten Integral de Residuos S&oacutelidos EMGIRS-EP, realice el seguimiento "
            				+ "mensual de las recomendaciones de auditor&iacutea realizadas mediante los diferentes ex&aacutemenes especiales o auditor&iacuteas, e informe a la gerencia de "
            				+ "su seguimiento y cumplimiento. </p>\n"
            				+ "La Gerencia de Desarrollo Organizacional en funci&oacuten a lo que establece el Procedimiento de Seguimiento y Control de Recomendaciones "
            				+ "emitidas por la Contralor&iacutea General del Estado vigente, se da la conformidad del plan de acci&oacuten y se recuerda lo siguiente: </p>\n"
            				+ "<p>El Responsable de la Recomendaci&oacuten y el Servidor designado son responsables de:  </p>"
            				+ "<p>&nbsp;a	Dar estricto cumplimiento a lo establecido en la recomendaci&oacuten a su cargo. </p> "
            				+ "<p>&nbsp;b	El registro del avance de la documentaci&oacuten que respalde el cumplimiento de la recomendaci&oacuten, de acuerdo a los tiempos establecido en el Plan. </p> "
            				+ "<p>&nbsp;c	Deber&aacuten validar y verificar la pertinencia de la informaci&oacuten previo al registrado en el sistema de recomendaciones. </p> "
            				+ "<p>&nbsp;d	Revisar y mantener actualizado la informaci&oacuten de las recomendaciones que se encuentran cargada en el sistema. </p> "
            				+ "<p>&nbsp;e	Llevar el control de las recomendaciones y su respectivo archivo f&iacutesico y/o digital.</p> "
            				+ "<p>&nbsp;f	Reportar el avance y el cumplimiento de la recomendaci&oacuten a la M&aacutexima Autoridad o su delegado.” </p>\n"
        			    			
            				+ "<p>Con este antecedente, y en calidad de Delegado del Seguimiento al Cumplimiento de las Recomendaciones se acepta y dispongo a Usted la ejecuci&oacuten de las actividades "
            				+ "y reporte de los avances establecidos en el plan de acci&oacuten  de la Recomendaci&oacuten No. "+tab_recomendacion.getValor("numero_serec")+"  del informe No.  "+tab_informe.getValor("numero_seinf")+"conforme a los tiempos establecidos.  </p> "
       		             + "<p>&nbsp;</p>\n"
       		             + "<p>Saludos cordiales,</p>\n";
        	    	htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
        	                   + "<tbody>\n"
        	                   + "<tr>\n"
        	                   + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
        	                   + "<td width=\"476\">\n"
            	                + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
        	                   + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
        	                   + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
        	                   + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
        	                   + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
        	                   + "</tr>\n"
        	                   + "</tbody>\n"
        	                   + "</table>";
            	   // System.out.println("aprobado "+htmlText);
            	    }else if (tipo_correo==7) {
            		//Reprogramacion de actividad
        			htmlText+="<p>Estimado Delegado(a) del Seguimiento de Recomendaci&oacuten </p>\n"
    		        		+"<p>En atención a la notificación y disposición de cumplimiento de la recomendación N. "+tab_recomendacion.getValor("numero_serec")+"  "+tab_recomendacion.getValor("asunto_serec")+", "
    		        		+ "me permito informar que se ha ingresado la reprogramación del plan de acción para dar cumplimiento a la recomendación por "+descripcion_repro+" con fecha "+fecha_repro+", "
    		        		+ "solicito la aceptación del mismo</p>\n"
    		        		+ "<p>&nbsp;</p>\n"; 
    		   	        
        	    	htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
        	                   + "<tbody>\n"
        	                   + "<tr>\n"
        	                   + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
        	                   + "<td width=\"476\">\n"
    			             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>"+strNombreEmpleado+"</strong></p>\n"
        	                   + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
        	                   + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
        	                   + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
        	                   + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
        	                   + "</tr>\n"
        	                   + "</tbody>\n"
        	                   + "</table>";
            		} 
        		else if (tipo_correo==8) {
        			//Reprogramcion de actividad Administrador
        		/*	htmlText+="<p>Notificamos mediante la presente se informa que se ha imcumplido con la accion "+acciones+" para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
        		       + "<p>&nbsp;</p>\n" 
        		        + "<p>Recuerde que debe registrar la documentacion necesaria para el cumplimiento del Plan de Accion </p>\n"
        		        + "<p>&nbsp;</p>\n"
        		        + "<p>Saludos cordiales,</p>\n";
        			
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
        	                   + "</table>";*/
        	    	
        	    	
        			   htmlText+="<p>Estimado(a) "+strNombreEmpleado+",  </p>\n"
        		        		+"<p>El 27 de agosto de 2020, la Gerente General de la EMGIRS EP, mediante resoluci&oacute;n administrativa No. EMGIRS-EP-GGE-CJU-2020-019 resolvi&oacute;: "
        		        		+ "Art. 1 Delegar al/la Gerente de Desarrollo Organizacional, o quien cumpla sus funciones en caso de encargo o subrogaciones, para que a nombre y "
        		        		+ "en representaci&oacute;n de la Gerente General de la Empresa P&uacute;blica Metropolitana de Gesti&oacute;n Integral de Residuos S&oacute;lidos EMGIRS-EP, realice el seguimiento "
        		        		+ "mensual de las recomendaciones de auditor&iacute;a realizadas mediante los diferentes ex&aacute;menes especiales o auditor&iacute;as, e informe a la gerencia de su seguimiento y cumplimiento. </p>\n"
        		        		+ "<p>La Gerencia de Desarrollo Organizacional en funci&oacute;n a lo que establece el Procedimiento de Seguimiento y Control de Recomendaciones emitidas por la Contralor&iacute;a General "
        		        		+ "del Estado vigente, me permito informar que la recomendaci&oacute;n se encuentra con estado INCUMPLIDO, por lo que se solicita dispongo a Usted la ejecuci&oacute;n de las actividades "
        		        		+ "y reporte de los avances establecidos en el plan de acci&oacute;n conforme a los tiempos establecidos o en su defecto realizar la solicitud de reprogramaci&oacute;n.</p>\n";
        			    htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
        			             + "<tbody>\n"
        			             + "<tr>\n"
        			             + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
        			             + "<td width=\"476\">\n"
        			             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>DELEGADO DEL SEGUIMIENTO DE RECOMENDACIONES</strong></p>\n"
        			             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
        			             + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
        			             + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
        			             + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
        			             + "</tr>\n"
        			             + "</tbody>\n"
        	                   + "</table>";
        	    	
        	    	
        			}
           
         		else if (tipo_correo==9) {
            		//Plan de accion
        			htmlText+="<p> Notifico mediante la presente que se ha remitido el plan de accion corregido  para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" .</p>\n"
       		             + "<p>&nbsp;</p>\n" 
       		             + "<p>&nbsp;</p>\n"
       		             + "<p>Saludos cordiales,</p>\n";
        	    	htmlText+="<table style=\"height: 144px;\" width=\"571\">\n"
        	                   + "<tbody>\n"
        	                   + "<tr>\n"
        	                   + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"http://emgirs.gob.ec/images/Logfinal.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
        	                   + "<td width=\"476\">\n"
        	                   + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>"+strNombreEmpleado+"</strong></p>\n"
        	                   + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
        	                   + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Av. Amazonas N51-84</strong></p>\n"
        	                   + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
        	                   + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
        	                   + "</td>\n"
        	                   + "</tr>\n"
        	                   + "</tbody>\n"
        	                   + "</table>";
        			}
           
        		else if (tipo_correo==11) {
        			//Plan de accion Reprogramado Accion 
        			htmlText+="<p>Notificamos mediante la presente que se ha reprogramado la "+acciones+" para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
        		       + "<p>&nbsp;</p>\n" 
        		        + "<p>Recuerde que debe registrar la nueva accion</p>\n"
        		        + "<p>&nbsp;</p>\n"
        		        + "<p>Saludos cordiales,</p>\n";
        			
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
        			}

        		
        		else if (tipo_correo==12) {
        			//Plan de accion EN ESTADO DE APLICADA
        			htmlText+="<p>Notificamos mediante la presente que el plan de accion se encuentra en estado de aplicada para. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
        		       + "<p>&nbsp;</p>\n" 
        		        + "<p>Recuerde que debe registrar la nueva accion</p>\n"
        		        + "<p>&nbsp;</p>\n"
        		        + "<p>Saludos cordiales,</p>\n";
        			
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
        			}
        		
        		else if (tipo_correo==13) {
        			//Plan de accion EN ESTADO DE REPROGRAMADO
        			htmlText+="<p>Notificamos mediante la presente que se ha reprogramado todo el plan de accion para la recomendación Nro. "+recomendacion+"  Del informe Nro. "+informe+" </p>\n"
        		       + "<p>&nbsp;</p>\n" 
        		        + "<p>Recuerde que debe registrar la nueva accion</p>\n"
        		        + "<p>&nbsp;</p>\n"
        		        + "<p>Saludos cordiales,</p>\n";
                   
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

        			}
                   
          

           
            //String htmlText = cuerpo;
            messageBodyPart.setContent(htmlText, "text/html");
            multiParte.addBodyPart(messageBodyPart);
            	            
         // second part (the image)
           /* messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource("D:/soporteTecnico.jpg");
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");
            multiParte.addBodyPart(messageBodyPart);*/

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

   ///2 registro de nuevo plan de accion
   public void EnviarCorreoAccion(String ide_sepla,String informe, String recomendacion, String acciones,String ide_gtemp,int tipo_rol, String fecha, String descripcion) throws Exception{
		try {
		TablaGenerica tab_correo= utilitario.consultar("select ide_gtemp,detalle_gtcor from gth_correo where ide_gtemp="+ide_gtemp);
		String correo=tab_correo.getValor("detalle_gtcor");//"juan.ayerve@emgirs.gob.ec";
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
			
			//util.EnviaMailInterno(envMail, correo_envio, "SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP", emailLinkEncuestaCorreo(strNombreEmpleado,ide_sepla, informe,  recomendacion,  acciones, ide_gtemp,tipo_rol,fecha,descripcion), null);
			String str_mail=correo_envio;
			envMail.setAsunto("SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP");
			envMail.setCuerpoHtml(emailLinkEncuestaCorreo(strNombreEmpleado,ide_sepla, informe,  recomendacion,  acciones, ide_gtemp,tipo_rol,fecha,descripcion));
			envMail.setPara(str_mail);
			pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
			
			if(obj.getRespuesta())
			{
				utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : email: " + str_mail);
			}
			else
				utilitario.agregarMensajeError("Correo no enviado a : email: " + str_mail , " msjError: " + obj.getDescripcion());
			
			//no usar EnviaMailInterno
			//EnviaMailInternoAccion(envMail, correo, "SISTEMA DE SEGUIMIENTO Y RECOMENDACIONES EMGIRS-EP", emailLinkEncuestaCorreo(strNombreEmpleado,ide_sepla,informe, recomendacion, acciones, ide_gtemp,tipo_rol,fecha,descripcion), null,strNombreEmpleado,
			//ide_sepla, informe,  recomendacion,  acciones+" con estado:"+descripcion_setre, ide_gtemp,tipo_rol,fecha,descripcion);
			System.out.println("Enviando Correo.........");

			} catch (Exception e) {
			System.out.println("Error en el envio de correo"+e.getMessage());
				}
			} catch (Exception e) {
				e.printStackTrace();
				utilitario.agregarMensajeError("Ha ocurrido un error al aprobar la solicitud", "");
			}

				} 		
   
   
   

public void insertarTablaPlanAccion(
		 String ide_seusu,
		 String ide_serec,
		 String fecha_registro_sepla,
		 String fecha_aprobacion_sepla,
		 int ide_seesp,
		 String activo_sepla,
		 String ide_seinf,
		 String ide_seper,
		 String asunto_sepla


		 
		 ){

		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("seg_plan_accion", "ide_sepla"));
		String codigo=tab_codigo.getValor("codigo");
		
		

		utilitario.getConexion().ejecutarSql("INSERT INTO seg_plan_accion(" 
					+ "ide_sepla, "
					+ "ide_seusu, "
			  		+ "ide_serec, "
			  		+ "fecha_registro_sepla, "
			  		+ "fecha_aprobacion_sepla, "
			  		+ "porcentaje_sepla, "
			  		+ "ide_seesp, "
			  		+ "activo_sepla, "
			  		+ "ide_seinf, "
			  		+ "ide_seper, "
			  		+ "asunto_sepla,"
			  		+ "fecha_ingre, "
			  		+ "hora_ingre, "
			  		+ "usuario_ingre "
			  		+ ") "  

			  		+" values( " +codigo + ", "
			  		+ ""+ ide_seusu+", "
			  		+ ""+ide_serec+", "
			  		+ "'"+fecha_registro_sepla+"', "
			  		+ ""+fecha_aprobacion_sepla+", "
			  		+ "0.00, "
			  		+ ""+ide_seesp+", "
			  		+ "true, "
			  		+ ""+ide_seinf+", "
			  		+ ""+null+", "
	    	  		+ "'"+asunto_sepla+"', "
	    	  		+ " NOW(), NOW()," 
	    	  		+ "'" +utilitario.getVariable("NICK") + "');"); 
	 
		

}



public void insertarTablaPlanAccionPeriocidad(
		 String ide_seusu,
		 String ide_serec,
		 String fecha_registro_sepla,
		 String fecha_aprobacion_sepla,
		 int ide_seesp,
		 String activo_sepla,
		 String ide_seinf,
		 String ide_seper,
		 String asunto_sepla,
		 int ide_seemp,
		 String fecha_inicio_control_periocidad_sepla,
		 String observacion_sepla
		){

	
		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("seg_plan_accion", "ide_sepla"));
		String codigo=tab_codigo.getValor("codigo");
		

		utilitario.getConexion().ejecutarSql("INSERT INTO seg_plan_accion(" 
					+ "ide_sepla, "
					+ "ide_seusu, "
			  		+ "ide_serec, "
			  		+ "fecha_registro_sepla, "
			  		+ "fecha_aprobacion_sepla, "
			  		+ "porcentaje_sepla, "
			  		+ "ide_seesp, "
			  		+ "activo_sepla, "
			  		+ "ide_seinf, "
			  		+ "ide_seper, "
			  		+ "asunto_sepla,"
			  		+ "envio_plan, "
			  		+ "ide_seemp, "
			  		+ "observacion_sepla,"
			  		+ "descripcion_repro_sepla, "
			  		+ "fecha_repro_sepla, "
			  		+ "aceptar_repro_sepla, "
			  		+ "reprogramacion_parcial_sepla, "
			  		+ "reprogramacion_total_sepla, "
			  		+ "fecha_inicio_control_periocidad_sepla,"
			  		+ "fecha_ingre, "
			  		+ "hora_ingre, "
			  		+ "usuario_ingre "
			  		+ ") "  

			  		+" values( " +codigo + ", "
			  		+ ""+ ide_seusu+", "
			  		+ ""+ide_serec+", "
			  		+ "'"+utilitario.getFechaActual()+"', "
			  		+ ""+null+", "
			  		+ "0.00, "
			  		+ ""+ide_seesp+", "
			  		+ "true, "
			  		+ ""+ide_seinf+", "
			  		+ ""+ide_seper+", "
			  		+ "'"+asunto_sepla+"',"
			  		+ "false ,  "
			  		+ ""+ide_seemp+",  "
			  		+ "'"+observacion_sepla+"',  "
			  		+ ""+null+",  "
			  		+ ""+null+",  "
			  		+ "false ,  "
			  		+ "false ,  "
			  		+ "false ,  "
			  		+ "'"+fecha_inicio_control_periocidad_sepla+"',"
			  		+ " NOW(), NOW()," 
	    	  		+ "'" +utilitario.getVariable("NICK") + "');"); 
	 
		

}




public void insertarTablaRespuesta(String ide_sepla_anterior, int sumaDias,int ide_sepla){

		

		TablaGenerica tab_empleados=utilitario.consultar("SELECT "
					+ " ide_seres, "
					+ "ide_serec, "
					+ "descripcion_seres, "
					+ "hora_seres, "
				+ "ide_seusu, "
					+ "ide_seemp, "
					+ "ide_setre, "
					+ "activo_seres, "
					+ "fecha_seres,"
					+ "ide_sepla, "
					+ "ide_seinf, "
					+ "fecha_desde_seres, "
					+ "fecha_hasta_seres, "
					+ "medio_verificacion_seres,  "
					+ "ide_gtemp_responsable, "
					+ "mecanismo_reporte_seres, "
					+ "observacion_seres, "
				+ "reprogramacion_seres, "
				+ "descripcion_repro_seres, "
				+ "aceptar_repro_seres, "
				+ "nombre_archivo_seres, "
				+ "ide_seres_anterior,"
				+ "mecanismo_reporte_seres_enlace "
				+ "FROM seg_respuesta "
				+ "where ide_sepla="+ide_sepla_anterior+" and ide_setre in(5,3) "
				+ "order by fecha_desde_seres asc, fecha_hasta_seres asc");
		

		String ide_serec="",descripcion_seres="",ide_seusu="",hora_seres="",ide_seemp="",ide_seinf="",medio_verificacion_seres="",ide_gtemp_responsable="",mecanismo_reporte_seres_enlace="";
		for (int i = 0; i < tab_empleados.getTotalFilas(); i++) {
	
		 ide_serec=tab_empleados.getValor(i,"ide_serec"); 
		 descripcion_seres=tab_empleados.getValor(i,"descripcion_seres");
		hora_seres=utilitario.getHoraActual();
		//ide_seusu=tab_empleados.getValor(i,"ide_seusu");
		ide_seusu=usuario_responsable;

		ide_seemp=tab_empleados.getValor(i,"ide_seemp");
		String ide_setre="";
		boolean insertar=false;
		if (tab_empleados.getValor(i,"ide_setre").equals("5")) {
			ide_setre="6";
			insertar=true;
		}else if (tab_empleados.getValor(i,"ide_setre").equals("3")){
			ide_setre="3";
			insertar=true;
		}else {
			insertar=false;
		}
		
		String activo_seres="true";
		String fecha_seres=utilitario.getFechaActual();
		int ide_sepla_=ide_sepla;
		ide_seinf=tab_empleados.getValor(i,"ide_seinf");
		String fecha_desde_nueva="",fecha_hasta_nueva="";
		fecha_desde_nueva=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(tab_respuesta.getValor(i,"fecha_desde_seres")), sumaDias));
		fecha_hasta_nueva=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(tab_respuesta.getValor(i,"fecha_hasta_seres")), sumaDias));
		medio_verificacion_seres=tab_empleados.getValor(i,"medio_verificacion_seres");
		ide_gtemp_responsable=tab_empleados.getValor(i,"ide_gtemp_responsable");
		
		if (ide_gtemp_responsable== null || ide_gtemp_responsable.equals("") || ide_gtemp_responsable.isEmpty()) {
			
		}else {
		
			ide_gtemp_responsable=empleado_responsable;
		}
		
		String mecanismo_reporte_seres=tab_empleados.getValor(i,"mecanismo_reporte_seres");
		String nombre_archivo_seres=tab_empleados.getValor(i,"nombre_archivo_seres");

		mecanismo_reporte_seres_enlace=tab_empleados.getValor(i,"mecanismo_reporte_seres_enlace");
		String observacion_seres=null;
		boolean reprogramacion_seres=false;
		String descripcion_repro_seres=null;
		boolean aceptar_repro_seres=false;
		String ide_seres_anterior=null; 

		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("seg_respuesta", "ide_seres"));
		String codigo=tab_codigo.getValor("codigo");
		
		if (insertar==true) {
		utilitario.getConexion().ejecutarSql("INSERT INTO seg_respuesta(" 
		+ " ide_seres, "
		+ "ide_serec, "
		+ "descripcion_seres, "
		+ "hora_seres, "
		+ "ide_seusu, "
		+ "ide_seemp, "
		+ "ide_setre, "
		+ "activo_seres, "
		+ "fecha_seres,"
		+ "ide_sepla, "
		+ "ide_seinf, "
		+ "fecha_desde_seres, "
		+ "fecha_hasta_seres, "
		+ "medio_verificacion_seres,  "
		+ "ide_gtemp_responsable, "
		+ "mecanismo_reporte_seres, "
		+ "observacion_seres, "
		+ "reprogramacion_seres, "
		+ "descripcion_repro_seres, "
		+ "aceptar_repro_seres, "
		+ "nombre_archivo_seres, "
					+ "ide_seres_anterior,"
					+ "mecanismo_reporte_seres_enlace,"
					+ "fecha_ingre, "
			  		+ "hora_ingre, "
			  		+ "usuario_ingre "
					+ ") "
			  		+" values( " +codigo + ", "
			  		+ ""+ ide_serec+", "
  		+ "'"+descripcion_seres+"', "
  		+ "'"+utilitario.getHoraActual()+"', "
  		+ ""+ide_seusu+", "
			  		+ ""+ide_seemp+", "	  		
			  		+ ""+ide_setre+", "
			  		+ "true, "
  		+ "'"+utilitario.getFechaActual()+"', "
			  		+ ""+ide_sepla+", "
			  		+ ""+ide_seinf+", "
  		+ "'"+fecha_desde_nueva+"', "
  		+ "'"+fecha_hasta_nueva+"', "
  		+ "'"+medio_verificacion_seres+"', "
			  		+ ""+null+", "
			  		+ "'"+mecanismo_reporte_seres+"', "
  		+ "null, "
  		+ "false, "
  		+ "null, "
  		+ "false, "
			  		+ "'"+nombre_archivo_seres+"', "
  		+ "null, "
	  				+ "'"+mecanismo_reporte_seres_enlace+"',"
	  				+ " NOW(), NOW()," 
	    	  		+ "'" +utilitario.getVariable("NICK") + "');"); 

		}else {
			
		}
	

		
}

		


	 

}
   

public void insertarTablaRespuestaReprogramacion(String ide_sepla_anterior,int ide_sepla){


	
	TablaGenerica tab_empleados=utilitario.consultar("SELECT "
			+ "ide_seres, "
			+ "ide_serec, "
			+ "descripcion_seres, "
			+ "hora_seres, "
			+ "ide_seusu, "
			+ "ide_seemp, "
			+ "ide_setre, "
			+ "activo_seres, "
			+ "fecha_seres, "
			+ "ide_sepla, "
			+ "ide_seinf, "
			+ "fecha_desde_seres, "
			+ "fecha_hasta_seres, "
			+ "medio_verificacion_seres, "
			+ "ide_gtemp_responsable, "
			+ "mecanismo_reporte_seres, "
			+ "observacion_seres, "
			+ "reprogramacion_seres, "
			+ "descripcion_repro_seres, "
			+ "aceptar_repro_seres, "
			+ "nombre_archivo_seres, "
			+ "ide_seres_anterior, "
			+ "mecanismo_reporte_seres_enlace "
			+ "FROM seg_respuesta "
			+ "where ide_sepla="+ide_sepla_anterior+" and ide_setre in(5,3,6) "
			+ "order by fecha_desde_seres asc, fecha_hasta_seres asc");
	

	String ide_serec="",descripcion_seres="",ide_seusu="",hora_seres="",ide_seemp="",ide_seinf="",medio_verificacion_seres="",ide_gtemp_responsable="",mecanismo_reporte_seres_enlace="";
	for (int i = 0; i < tab_empleados.getTotalFilas(); i++) {

	 ide_serec=tab_empleados.getValor(i,"ide_serec"); 
	 descripcion_seres=tab_empleados.getValor(i,"descripcion_seres");
	hora_seres=utilitario.getHoraActual();
	ide_seusu=usuario_responsable;
	ide_seemp=empresa;
	String ide_setre="";
	boolean insertar=false;
	if (tab_empleados.getValor(i,"ide_setre").equals("5") || tab_empleados.getValor(i,"ide_setre").equals("6")) {
		ide_setre="1";
		insertar=true;
	}else if (tab_empleados.getValor(i,"ide_setre").equals("3")){
		ide_setre="3";
		insertar=true;
	}else {
		insertar=false;
	}
	
	String activo_seres="true";
	String fecha_seres=utilitario.getFechaActual();
	int ide_sepla_=ide_sepla;
	ide_seinf=tab_empleados.getValor(i,"ide_seinf");
	medio_verificacion_seres=tab_empleados.getValor(i,"medio_verificacion_seres");
	ide_gtemp_responsable=tab_empleados.getValor(i,"ide_gtemp_responsable");
	
	if (ide_gtemp_responsable== null || ide_gtemp_responsable.equals("") || ide_gtemp_responsable.isEmpty()) {
		
	}else {
		ide_gtemp_responsable=empleado_responsable;
	} 
	String mecanismo_reporte_seres=tab_empleados.getValor(i,"mecanismo_reporte_seres");
	mecanismo_reporte_seres_enlace=tab_empleados.getValor(i,"mecanismo_reporte_seres_enlace");
	String observacion_seres="";
	boolean reprogramacion_seres=false;
	String descripcion_repro_seres="";
	boolean aceptar_repro_seres=false;
	String nombre_archivo_seres=tab_empleados.getValor(i,"nombre_archivo_seres");
	String ide_seres_anterior=null; 
	TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("seg_respuesta", "ide_seres"));
	String codigo=tab_codigo.getValor("codigo");
	
	if (insertar==true) {
		String sql="INSERT INTO seg_respuesta(" 
				+ " ide_seres, "
				+ "ide_serec, "
				+ "descripcion_seres, "
				+ "hora_seres, "
				+ "ide_seusu, "
				+ "ide_seemp, "
				+ "ide_setre, "
				+ "activo_seres, "
				+ "fecha_seres,"
				+ "ide_sepla, "
				+ "ide_seinf, "
				+ "fecha_desde_seres, "
				+ "fecha_hasta_seres, "
				+ "medio_verificacion_seres,  "
				+ "ide_gtemp_responsable, "
				+ "mecanismo_reporte_seres, "
				+ "observacion_seres, "
				+ "reprogramacion_seres, "
				+ "descripcion_repro_seres, "
				+ "aceptar_repro_seres, "
				+ "nombre_archivo_seres, "
				+ "ide_seres_anterior, "
				+ "mecanismo_reporte_seres_enlace, "
				+ "fecha_ingre, "
		  		+ "hora_ingre, "
		  		+ "usuario_ingre "
				+ ") "
		  		+" values( " +codigo + ", "
		  		+ ""+ ide_serec+", "
		  		+ "'"+descripcion_seres+"', "
		  		+ "'"+utilitario.getHoraActual()+"', "
		  		+ ""+ide_seusu+", "
		  		+ ""+ide_seemp+", "	  		
		  		+ ""+ide_setre+", "
		  		+ "true, "
		  		+ "'"+utilitario.getFechaActual()+"', "
		  		+ ""+ide_sepla+", "
		  		+ ""+ide_seinf+", ";
		  		if (ide_setre.equals("3")) {
		  		sql+="'"+utilitario.getFechaActual()+"', "
				+ "'"+utilitario.getFechaActual()+"', ";
				}else {
					sql+="null, "
					+ "null, ";
				}
		  		
		  		sql+="'"+medio_verificacion_seres+"', "
		  		+ ""+null+", "
		  		+ "'"+mecanismo_reporte_seres+"', "
		  		+ "'"+null+"', "
		  		+ "false, "
		  		+ "'"+null+"', "
		  		+ "false, "
		  		+ "'"+nombre_archivo_seres+"', "
		  		+ "null, "
		  		+ "'"+mecanismo_reporte_seres_enlace+"'," 
		  		+ " NOW(), NOW()," 
    	  		+ "'" +utilitario.getVariable("NICK") + "');"; 
		  		
		  		utilitario.getConexion().ejecutarSql(sql);

	}else {
		
	}


	
}

	


 

}



public void insertarHistorialMecanismo(
		 String ide_gtemp,
		 String fecha_sehmc,
		 String hora_sehmc,
		 int  ide_seres,
		 String mecanismo_seres_historial_sehmc
		 ){

		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("seg_historial_mecanismo_control", "ide_sehmc"));
		String codigo=tab_codigo.getValor("codigo");
		

		utilitario.getConexion().ejecutarSql("INSERT INTO seg_historial_mecanismo_control(" 
					+ "ide_sehmc, "
					+ "ide_gtemp, "
			  		+ "fecha_sehmc, "
			  		+ "hora_sehmc, "
			  		+ "ide_seres, "
			  		+ "activo_sehmc, "
			  		+ "mecanismo_seres_historial_sehmc,"
			  		+ "fecha_ingre, "
			  		+ "hora_ingre, "
			  		+ "usuario_ingre "
			  		+ ") "  
			  		+" values( " +codigo + ", "
			  		+ ""+ ide_gtemp+", "
			  		+ "'"+fecha_sehmc+"', "
			  		+ "'"+hora_sehmc+"', "
			  		+ ""+ide_seres+", "
     		  		+ ""+true+", "
	    	  		+ "'"+mecanismo_seres_historial_sehmc+"',"
	    	  		+ " NOW(), NOW()," 
	    	  		+ "'" +utilitario.getVariable("NICK") + "');"); 
		
		

}


public String servicioCodigoMaximo(String tabla,String ide_primario){
		
		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
		return maximo;
	}



public int retornaIdeSepla(String ide_serec, String ide_seinf){
	int retornaValor=0;
	TablaGenerica tab_plan_accion=utilitario.consultar("SELECT ide_sepla, ide_seusu, ide_serec, fecha_registro_sepla, fecha_aprobacion_sepla, "
			+ "porcentaje_sepla, ide_seesp, activo_sepla, "
			+ "ide_seinf,ide_seper, asunto_sepla "
			+ "FROM seg_plan_accion "
			+ "where ide_serec="+ide_serec+" and ide_seinf="+ide_seinf+" and activo_sepla=true");
	
	if (tab_plan_accion.getTotalFilas()>0){
		retornaValor=Integer.parseInt(tab_plan_accion.getValor("IDE_SEPLA"));
	}else {
		retornaValor=0;
	}
	
	return retornaValor; 
}

public int retornaIdeSeplaNotificacion(String ide_serec, String ide_seinf){
	int retornaValor=0;
	TablaGenerica tab_plan_accion=utilitario.consultar("SELECT ide_sepla, ide_seusu, ide_serec, fecha_registro_sepla, fecha_aprobacion_sepla, "
			+ "porcentaje_sepla, ide_seesp, activo_sepla, "
			+ "ide_seinf,ide_seper, asunto_sepla "
			+ "FROM seg_plan_accion "
			+ "where ide_serec="+ide_serec+" and ide_seinf="+ide_seinf+" order by ide_sepla desc limit 1");
	
	if (tab_plan_accion.getTotalFilas()>0){
		retornaValor=Integer.parseInt(tab_plan_accion.getValor("IDE_SEPLA"));
	}else {
		retornaValor=0;
	}
	
	return retornaValor; 
}


public int retornaPlanesAccion(String ide_serec, String ide_seinf, int estadoPlan){
	int retornaValor=0;
	TablaGenerica tab_plan_accion=null;
	if (estadoPlan==1) {
		 tab_plan_accion=utilitario.consultar("SELECT ide_sepla, ide_seusu, ide_serec, fecha_registro_sepla, fecha_aprobacion_sepla, "
				+ "porcentaje_sepla, ide_seesp, activo_sepla, "
				+ "ide_seinf,ide_seper, asunto_sepla "
				+ "FROM seg_plan_accion "
				+ "where ide_serec="+ide_serec+" and ide_seinf="+ide_seinf+" and activo_sepla=true and ide_seesp=1");
		 if (tab_plan_accion.getTotalFilas()>0){
				retornaValor=Integer.parseInt(tab_plan_accion.getValor("IDE_SEPLA"));
			}else {
				retornaValor=0;
			}
		 
	}else if (estadoPlan==2) {
		 tab_plan_accion=utilitario.consultar("SELECT ide_sepla, ide_seusu, ide_serec, fecha_registro_sepla, fecha_aprobacion_sepla, "
				+ "porcentaje_sepla, ide_seesp, activo_sepla, "
				+ "ide_seinf,ide_seper, asunto_sepla "
				+ "FROM seg_plan_accion "
				+ "where ide_serec="+ide_serec+" and ide_seinf="+ide_seinf+" and activo_sepla=true and ide_seesp=2");
	}else if (estadoPlan==4) {
		 tab_plan_accion=utilitario.consultar("SELECT ide_sepla, ide_seusu, ide_serec, fecha_registro_sepla, fecha_aprobacion_sepla, "
				+ "porcentaje_sepla, ide_seesp, activo_sepla, "
				+ "ide_seinf,ide_seper, asunto_sepla "
				+ "FROM seg_plan_accion "
				+ "where ide_serec="+ide_serec+" and ide_seinf="+ide_seinf+" and activo_sepla=true and ide_seesp=5");
	}else {
		retornaValor=0;
	}
	 
	return retornaValor;
}

private int estadoRespuesta;
public void validarTipoAccion(AjaxBehaviorEvent evt){
	estadoRespuesta=0;
	String ide_setre="",fecha_desde_seres="",fecha_hasta_seres="",mecanismo_reporte_seres="",fecha_actual="";
	tab_respuesta.modificar(evt);
	if (tab_respuesta.getValor("ide_setre")==null || tab_respuesta.getValor("ide_setre").equals("")) {
		estadoRespuesta=0;

	}else {
		
		
		TablaGenerica tab_respuestaAnterior=utilitario.consultar("SELECT ide_seres, ide_serec, descripcion_seres, hora_seres, ide_seusu,  "
				+ "ide_seemp, ide_setre, activo_seres  "
				+ "fecha_seres,ide_sepla, ide_seinf, fecha_desde_seres, fecha_hasta_seres, medio_verificacion_seres,  "
				+ "ide_gtemp_responsable, mecanismo_reporte_seres, observacion_seres,  "
				+ "reprogramacion_seres, descripcion_repro_seres, aceptar_repro_seres, "
				+ "nombre_archivo_seres, ide_seres_anterior  "
				+ "FROM seg_respuesta  "
				+ "where ide_seres="+tab_respuesta.getValor("ide_seres"));
		
		ide_setre=tab_respuestaAnterior.getValor("ide_setre");
		fecha_desde_seres=tab_respuestaAnterior.getValor("fecha_desde_seres");
		fecha_hasta_seres=tab_respuestaAnterior.getValor("fecha_hasta_seres");
		mecanismo_reporte_seres=tab_respuestaAnterior.getValor("mecanismo_reporte_seres");
		
		
		//Notificacion de Rechazo
		if (tab_respuesta.getValor("ide_setre").equals("4")){
			if (tab_respuesta.getValor("observacion_seres")==null || tab_respuesta.getValor("observacion_seres").isEmpty() || tab_respuesta.getValor("observacion_seres").equals("")) {
				utilitario.agregarMensajeInfo("No se puede guardar", "Registre una observacion y vuelva a intentarlo");
				return;
			}else {
				
			}
			
			estadoRespuesta=4;
			con_aprobar_solicitud.dibujar();
		}
		
		/*else if (tab_respuesta.getValor("ide_setre").equals("3")){
			estadoRespuesta=3;
			if (tab_respuesta.getValor("ide_seres")==null || tab_respuesta.getValor("ide_seres").isEmpty() || tab_respuesta.getValor("ide_seres").equals("")) {
				tab_respuesta.setValor("ide_setre",""+1);
				utilitario.addUpdateTabla(tab_respuesta, "ide_setre", "");
				utilitario.agregarMensaje("No se puede realizar la accion solicitada", "Debe Guardar la accion para continuar");
				return;
			}
			
			con_aprobar_solicitud.dibujar();
		}*/
		else  
		//Aplicada
		if (tab_respuesta.getValor("ide_setre").equals("5") && (tab_respuesta.getValor("reprogramacion_seres")==null ||  tab_respuesta.getValor("reprogramacion_seres").equals("false") || tab_respuesta.getValor("reprogramacion_seres").isEmpty())){
			if (ide_setre.equals("1")) {
				fecha_actual=utilitario.getFechaActual();
				
				
				if (fecha_hasta_seres.compareTo(fecha_actual)>0) {
					utilitario.agregarMensaje("No se puede realizar la accion requerida", "Estado de recomendacion en estado de Avance Fecha Futura");	
					tab_respuesta.actualizar();
					return;
					}
				
				if (mecanismo_reporte_seres==null || mecanismo_reporte_seres.equals("") || mecanismo_reporte_seres.isEmpty()) {
					utilitario.agregarMensaje("No se puede realizar la accion requerida", "Estado de recomendacion en estado de Avance sin documento Adjunto");	
					tab_respuesta.actualizar();
					return;
				}else {
					
				}	
					
	
					
			
			}else {
				if (ide_setre.equals("8")) {
					if (tab_respuesta.getValor("mecanismo_reporte_seres")==null || tab_respuesta.getValor("mecanismo_reporte_seres").equals("") || tab_respuesta.getValor("mecanismo_reporte_seres").isEmpty()) {
						utilitario.agregarMensaje("No se actualizar", "Accion sin documento Adjunto");	
						tab_respuesta.actualizar();
						return;
					}else {
						
					}	
			}
			}
			estadoRespuesta=5;
			con_aprobar_solicitud.dibujar();
		}else if (tab_respuesta.getValor("ide_setre").equals("6")){
			estadoRespuesta=6;
			con_aprobar_solicitud.dibujar();
		}else if (tab_respuesta.getValor("ide_setre").equals("7")){
			//REPROGRAMCION ADMINISTRADOR
			estadoRespuesta=7;
			con_aprobar_solicitud.dibujar();
		}else if (tab_respuesta.getValor("ide_setre").equals("5")){
			//REPROGRAMCION ADMINISTRADOR
			estadoRespuesta=5;
			con_aprobar_solicitud.dibujar();	
		}else if (tab_respuesta.getValor("ide_setre").equals("8")){
			//REPROGRAMCION ADMINISTRADOR
			
			TablaGenerica tab_resp_anterior=utilitario.consultar("select * from seg_respuesta where ide_seres="+tab_respuesta.getValor("ide_seres"));
			if (tab_resp_anterior.getTotalFilas()>0) {
				if (tab_resp_anterior.getValor("ide_setre").equals("5")) {
					utilitario.agregarMensajeInfo("No se puede realizar la transaccion seleccioanda", "Tipo recomendacion no puede cambiar de estado");
					return;
				}
			}
			estadoRespuesta=8;
			con_aprobar_solicitud.dibujar();
			
//		}else if (tab_respuesta.getValor("reprogramacion_seres").equals("true") && !tab_respuesta.getValor("ide_setre").equals("5")){
		}else if (tab_respuesta.getValor("reprogramacion_seres")!=null &&    tab_respuesta.getValor("reprogramacion_seres").equals("true") ){
			if (tab_respuesta.getValor("ide_seres")==null || tab_respuesta.getValor("ide_seres").equals("") || tab_respuesta.getValor("ide_seres").isEmpty()) {
				utilitario.agregarMensajeInfo("No se puede realizar esta accion", "No se ha ingresado el Avance");
				tab_respuesta.setValor("reprogramacion_seres", "false");

				utilitario.addUpdateTabla(tab_respuesta, "reprogramacion_seres", "");
				return ;
			}
			//REPROGRAMACION
			estadoRespuesta=11;
			dia_anulado.dibujar();
			are_tex_razon_anula.setValue("");
			cal_fecha_inicio.setValue(null);
			cal_fecha_fin.setValue(null);
			utilitario.addUpdate("are_tex_razon_anula,cal_fecha_inicio,cal_fecha_fin");
		
		}else if (tab_respuesta.getValor("ide_setre").equals("3")){
			estadoRespuesta=3;
		}
		else {
			estadoRespuesta=0;

		}
			
	}
				
}

	

	
public void mostrarDialogo(AjaxBehaviorEvent evt){
	String ide_setre="",fecha_desde_seres="",fecha_hasta_seres="",mecanismo_reporte_seres="",fecha_actual="";
	tab_informe.modificar(evt);
	if (tab_informe.getValor("asunto_seinf")==null || tab_informe.getValor("asunto_seinf").equals("") || tab_informe.getValor("asunto_seinf").isEmpty()) {
		utilitario.agregarMensaje("No se puede visualizar", "No existe asunto");
		are_tex_informativa.setValue(null);
		utilitario.addUpdate("dia_informacion");
	}else {
	
		are_tex_informativa.setValue(tab_informe.getValor("asunto_seinf"));
		dia_informacion.dibujar();
	}
	
}	


public Confirmar getCon_aprobar_solicitud() {
	return con_aprobar_solicitud;
}


public void setCon_aprobar_solicitud(Confirmar con_aprobar_solicitud) {
	this.con_aprobar_solicitud = con_aprobar_solicitud;
}



public Confirmar getCon_solicitud_reprogramar() {
	return con_solicitud_reprogramar;
}


public void setCon_solicitud_reprogramar(Confirmar con_solicitud_reprogramar) {
	this.con_solicitud_reprogramar = con_solicitud_reprogramar;
}


public void aceptarAprobarSolicitudTalento(){
int ide_sepla=0,ide_sepla_=0;	
	try {//NOTIFICACION DE RECHAZO

		TablaGenerica tabEmpleado=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,emp.ide_gtemp "
	        		+ "FROM seg_usuario segus "
	        		+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
	        		+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
	        		+ "where segus.ide_seusu="+tab_respuesta.getValor("ide_seusu")
	        		+ " order by segus.ide_seusu");
		tabEmpleado.imprimirSql();
		
		if (estadoRespuesta==4) {//RECHAZO
			tab_respuesta.guardar();
			
		ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
			
	     TablaGenerica tab_empleado=utilitario.consultar("SELECT * FROM  "
					+ "SEG_ASIGNACION ASI "
					+ "left join seg_empresa emp on emp.ide_seemp=asi.ide_seemp "
					+ "left join seg_usuario usu on usu.ide_seemp=asi.ide_seemp "
					+ "left join sis_usuario usua on usua.ide_usua=usu.ide_usua "
					+ "WHERE asi.IDE_seinf="+tab_informe.getValor("ide_seinf")+" and asi.ide_serec="+tab_recomendacion.getValor("ide_serec")+" and usu.activo_seusu=true");

			if (tab_empleado.getTotalFilas()>0) {
				for (int i = 0; i < tab_empleado.getTotalFilas(); i++) {
	//	EnviarCorreoAccion(""+ide_sepla, tab_informe.getValor("numero_seinf"), tab_recomendacion.getValor("numero_serec"), "Nro.: "+tab_respuesta.getValor("IDE_SERES")+" Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" debido a :  "+tab_respuesta.getValor("observacion_seres")+" ", tab_empleado.getValor("ide_gtemp"), 4,"","");
				}
		guardarPantalla();
		utilitario.agregarMensajeInfo("ACCION HA CAMBIADO DE ESTADO A RECHAZADA", "SE HA INFORMADO AL RESPONSABLE MEDIANTE CORREO ELECTRONICO");
			}
		//NOTIFICACION DE APLICADA
		}else if (estadoRespuesta==5) {
			//CAMBIO DE ESTADO APLICADO POR ADMINISTRADOR
			tab_respuesta.guardar();
			
			guardarPantalla();
			
			ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
			//EnviarCorreoAccion(""+ide_sepla, tab_informe.getValor("numero_seinf"), tab_recomendacion.getValor("numero_serec"), "Nro.: "+tab_respuesta.getValor("IDE_SERES")+" Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tabEmpleado.getValor("ide_gtemp"), 5,"","");
				
			utilitario.agregarMensajeInfo("ACCION HA CAMBIADO DE ESTADO A APLICADA", "SE HA INFORMADO AL RESPONSABLE MEDIANTE CORREO ELECTRONICO");
			//tab_respuesta.guardar();
			
			//guardarPantalla();
			String mensajeCorreo=tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec");
			actualizaPlanAccionAplicada(tab_informe.getValor("ide_seinf"), tab_recomendacion.getValor("ide_serec"), ""+ide_sepla,tabEmpleado.getValor("ide_gtemp"),mensajeCorreo,5);
			
			}/*else if (estadoRespuesta==3) {//NOTIFICACION
				tab_respuesta.guardar();	
				guardarPantalla();
			ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
			TablaGenerica tab_empleado=utilitario.consultar("SELECT * FROM  "
					+ "SEG_ASIGNACION ASI "
					+ "left join seg_empresa emp on emp.ide_seemp=asi.ide_seemp "
					+ "left join seg_usuario usu on usu.ide_seemp=asi.ide_seemp "
					+ "left join sis_usuario usua on usua.ide_usua=usu.ide_usua "
					+ "WHERE asi.IDE_seinf="+tab_informe.getValor("ide_seinf")+" and asi.ide_serec="+tab_recomendacion.getValor("ide_serec")+" and usu.activo_seusu=true");
				
			if (tab_empleado.getTotalFilas()>0) {
				for (int i = 0; i < tab_empleado.getTotalFilas(); i++) {
				EnviarCorreoAccion(""+ide_sepla, tab_informe.getValor("numero_seinf"), tab_recomendacion.getValor("numero_serec"), "Nro.: "+tab_respuesta.getValor("IDE_SERES")+" Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tab_empleado.getValor(i,"ide_gtemp"), 3);
				}
				utilitario.agregarMensajeInfo("NOTIFICACION REMITIDA", "SE HA INFORMADO AL RESPONSABLE MEDIANTE CORREO ELECTRONICO");

			}else {
				utilitario.agregarMensajeInfo("NO SE PUDO ENVIAR NOTIFICACION", "NO EXISTEN CORREO ELECTRONICOS VALIDOS");
				return;
			}
			
				
			}*/
			else if (estadoRespuesta==6) {//CAMBIO DE ESTADO APROBADA
				tab_respuesta.guardar();	
				guardarPantalla();
			ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));

			TablaGenerica tab_empleado=utilitario.consultar("SELECT * FROM  "
					+ "SEG_ASIGNACION ASI "
					+ "left join seg_empresa emp on emp.ide_seemp=asi.ide_seemp "
					+ "left join seg_usuario usu on usu.ide_seemp=asi.ide_seemp "
					+ "left join sis_usuario usua on usua.ide_usua=usu.ide_usua "
					+ "WHERE asi.IDE_seinf="+tab_informe.getValor("ide_seinf")+" and asi.ide_serec="+tab_recomendacion.getValor("ide_serec")+" and usu.activo_seusu=true");

			if (tab_empleado.getTotalFilas()>0) {
				for (int i = 0; i < tab_empleado.getTotalFilas(); i++) {
				//	EnviarCorreoAccion(""+ide_sepla, tab_informe.getValor("numero_seinf"), tab_recomendacion.getValor("numero_serec"), "Nro.: "+tab_respuesta.getValor("IDE_SERES")+" Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tab_empleado.getValor(i,"ide_gtemp"), 6,"","");			
				}

				
			utilitario.agregarMensajeInfo("ACCION HA CAMBIADO DE ESTADO APROBADA", "SE HA INFORMADO AL RESPONSABLE MEDIANTE CORREO ELECTRONICO");
			}
			}else if (estadoRespuesta==7) {
			//Reprogramacion
			tab_respuesta.guardar();	
			guardarPantalla();
			ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
		//	EnviarCorreoAccion(""+ide_sepla, tab_informe.getValor("numero_seinf"), tab_recomendacion.getValor("numero_serec"), "Nro.: "+tab_respuesta.getValor("IDE_SERES")+" Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tabEmpleado.getValor("ide_gtemp"), 7,"","");
			actualizaPlanAccion(tab_informe.getValor("ide_seinf"), tab_recomendacion.getValor("ide_serec"), ""+ide_sepla,tabEmpleado.getValor("ide_gtemp"),"",7);
				
			utilitario.agregarMensajeInfo("ACCION HA CAMBIADO DE ESTADO APROBADA", "SE HA INFORMADO AL RESPONSABLE MEDIANTE CORREO ELECTRONICO");
			}
		
			else if (estadoRespuesta==8) {
				//Reprogramacion
				tab_respuesta.guardar();	
				guardarPantalla();
				ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
				//EnviarCorreoAccion(""+ide_sepla, tab_informe.getValor("numero_seinf"), tab_recomendacion.getValor("numero_serec"), "Nro.: "+tab_respuesta.getValor("IDE_SERES")+" Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tabEmpleado.getValor("ide_gtemp"), 8,"","");
				actualizaPlanAccion(tab_informe.getValor("ide_seinf"), tab_recomendacion.getValor("ide_serec"), ""+ide_sepla,tabEmpleado.getValor("ide_gtemp"),"",8);
					
				utilitario.agregarMensajeInfo("ACCION HA CAMBIADO DE ESTADO A INCUMPLIDA", "SE HA INFORMADO AL RESPONSABLE MEDIANTE CORREO ELECTRONICO");
				}
		
			
			else if (estadoRespuesta==11){
				//REPROGRAMACION
			tab_respuesta.guardar();	
			guardarPantalla();
			ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
			//EnviarCorreoAccion(""+ide_sepla, tab_informe.getValor("numero_seinf"), tab_recomendacion.getValor("numero_serec"), "Accion Nro.: "+tab_respuesta.getValor("IDE_SERES")+" Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tabEmpleado.getValor("ide_gtemp"), 11,"","");

		}
	
			
			con_aprobar_solicitud.cerrar();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		System.out.println("ERROR CAMBIO DE ESTADO DE RESPUESTA");
	}
	
}


public void actualizaPlanAccion(String ide_seinf, String ide_serec, String ide_sepla,String ide_gtemp,String mensaje_correo, int tipo){
	
	try {
		/*TablaGenerica tabActualizaPlanAccion=utilitario.consultar(" SELECT ide_serec,ide_setre "
				+ " FROM seg_respuesta "
				+ " where ide_setre="+tipo+" and ide_serec="+ide_serec+" and ide_sepla="+ide_sepla
				+ " group by ide_serec,ide_setre");*/
		
		TablaGenerica tabActualizaPlanAccion=utilitario.consultar("SELECT ide_serec,ide_setre FROM seg_respuesta  "
				+ "where ide_serec="+ide_serec+" and ide_sepla="+ide_sepla+"  AND reprogramacion_seres=false "
				+ "group by ide_serec,ide_setre");
		
		if (tabActualizaPlanAccion.getTotalFilas()==1) {
			if (tabActualizaPlanAccion.getValor("IDE_SEREC")==null || tabActualizaPlanAccion.getValor("IDE_SEREC").isEmpty() ) {
				
			}else {
				
				/*if (tipo==5) {
					// APLICADA
				//
		String mensaje="";
		mensaje="PLA DE ACCION Nro."+ide_sepla+" del Informe: "+ide_seinf+" de la Recomendacion: "+ide_serec;
		utilitario.getConexion().ejecutarSql("update seg_plan_accion set porcentaje_sepla=100.00,activo_sepla=true,ide_seesp=1,asunto_sepla='"+mensaje+"',fecha_aprobacion_sepla='"+utilitario.getFechaActual()+"' where ide_sepla=" + ide_sepla);
	
	

		utilitario.agregarMensajeInfo("PLAN DE ACCION HA SIDO APLICADO CORRECTAMENTE", "RECOMENDACION ASIGNADA EN ESTADO DE CUMPLIDA");
			EnviarCorreoAccion(ide_sepla, tab_informe.getValor("numero_seinf"), mensaje, "", ide_gtemp, 12,"","");
				}else */if (tipo==7){
					//CUANDO ES REPROGRAMACION
					String mensaje="";
					mensaje="PLA DE ACCION Nro."+ide_sepla+" del Informe: "+ide_seinf+" de la Recomendacion: "+ide_serec;
					utilitario.getConexion().ejecutarSql("update seg_plan_accion set porcentaje_sepla=0.00,activo_sepla=true,ide_seesp=4,asunto_sepla='"+mensaje+"',fecha_aprobacion_sepla='"+utilitario.getFechaActual()+"' where ide_sepla=" + ide_sepla);
					//System.out.println("PLA DE ACCION Nro."+ide_sepla+" del Informe: "+ide_seinf+" de la Recomendacion: "+ide_serec+" HA SIDO REPROGRAMADO");
					
					//utilitario.getConexion().ejecutarSql("update seg_recomendacion set ide_seesr=2 where ide_serec=" + ide_serec);

					utilitario.agregarMensajeInfo("PLAN DE ACCION HA SIDO APLICADO CORRECTAMENTE", "RECOMENDACION ASIGNADA EN ESTADO DE CUMPLIDA");
					//	EnviarCorreoAccion(ide_sepla, tab_informe.getValor("numero_seinf"), mensaje, "", ide_gtemp, 13,"","");

				}
			}
		}else {
		//	System.out.println("Sin actualizar plan de accion ");
			//Actualizar al plan de accion a ejecucion
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		System.out.println("ERROR EN PLAN DE ACCION APLICDO");
	}
	
	
	
	
	
	
}






public void actualizaPlanAccionAplicada(String ide_seinf, String ide_serec, String ide_sepla,String ide_gtemp,String mensaje_correo, int tipo){
	
	try {
		/*TablaGenerica tabActualizaPlanAccion=utilitario.consultar(" SELECT ide_serec,ide_setre "
				+ " FROM seg_respuesta "
				+ " where ide_setre="+tipo+" and ide_serec="+ide_serec+" and ide_sepla="+ide_sepla
				+ " group by ide_serec,ide_setre");*/
		
		TablaGenerica tabActualizaPlanAccion=utilitario.consultar("SELECT ide_serec,ide_setre FROM seg_respuesta  "
				+ "where ide_serec="+ide_serec+" and ide_sepla="+ide_sepla+"   AND (reprogramacion_seres=false or reprogramacion_seres is null) "
				+ "group by ide_serec,ide_setre");
		
		if (tabActualizaPlanAccion.getTotalFilas()==1) {
			if (tabActualizaPlanAccion.getValor("IDE_SEREC")==null || tabActualizaPlanAccion.getValor("IDE_SEREC").isEmpty() ) {
				
			}else {
				
				if (tipo==5) {
					// APLICADA
				//
		String mensaje="";
		mensaje="PLA DE ACCION Nro."+ide_sepla+" del Informe: "+ide_seinf+" de la Recomendacion: "+ide_serec;
		utilitario.getConexion().ejecutarSql("update seg_plan_accion set porcentaje_sepla=100.00,activo_sepla=true,ide_seesp=1,asunto_sepla='"+mensaje+"',fecha_aprobacion_sepla='"+utilitario.getFechaActual()+"' where ide_sepla=" + ide_sepla);
	// Eliminar validacion de actualizacion del plan de accion en caso de que todas las actividades se encuentren como aplicadas
	

		utilitario.agregarMensajeInfo("PLAN DE ACCION HA SIDO APLICADO CORRECTAMENTE", "RECOMENDACION ASIGNADA EN ESTADO DE CUMPLIDA");
		//	EnviarCorreoAccion(ide_sepla, tab_informe.getValor("numero_seinf"), mensaje, "", ide_gtemp, 12,"","");
				}/*else if (tipo==7){
					//CUANDO ES REPROGRAMACION
					String mensaje="";
					mensaje="PLA DE ACCION Nro."+ide_sepla+" del Informe: "+ide_seinf+" de la Recomendacion: "+ide_serec;
					utilitario.getConexion().ejecutarSql("update seg_plan_accion set porcentaje_sepla=0.00,activo_sepla=true,ide_seesp=4,asunto_sepla='"+mensaje+"',fecha_aprobacion_sepla='"+utilitario.getFechaActual()+"' where ide_sepla=" + ide_sepla);
					System.out.println("PLA DE ACCION Nro."+ide_sepla+" del Informe: "+ide_seinf+" de la Recomendacion: "+ide_serec+" HA SIDO REPROGRAMADO");
					
					//utilitario.getConexion().ejecutarSql("update seg_recomendacion set ide_seesr=2 where ide_serec=" + ide_serec);

					utilitario.agregarMensajeInfo("PLAN DE ACCION HA SIDO APLICADO CORRECTAMENTE", "RECOMENDACION ASIGNADA EN ESTADO DE CUMPLIDA");
						EnviarCorreoAccion(ide_sepla, tab_informe.getValor("numero_seinf"), mensaje, "", ide_gtemp, 13,"","");

				}*/
			}
		}else {
			
	/*		TablaGenerica tabRespuesta=utilitario.consultar("SELECT ide_serec,ide_setre FROM seg_respuesta  "
					+ "where ide_serec="+ide_serec+" and ide_seres="+tab_respuesta.getValor("ide_seres")+ " "
					+ "group by ide_serec,ide_setre");
			
			*/
			
			
			//System.out.println("Sin actualizar plan de accion ");
			//Actualizar al plan de accion a ejecucion
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		System.out.println("ERROR EN PLAN DE ACCION APLICDO");
	}
		
}


public void dibujarReprogramar(){
	bandReproPlanParcial=true;
	bandReproPlanTotal=false;
	dia_anulado.dibujar();
	are_tex_razon_anula.setValue("");
	utilitario.addUpdate("are_tex_razon_anula");


	
	
}

public void dibujarReprogramarTotal(){
	bandReproPlanParcial=false;
	bandReproPlanTotal=true;
	dia_anulado.dibujar();
	are_tex_razon_anula.setValue("");
	utilitario.addUpdate("are_tex_razon_anula");


}





public void aceptarReprogramar(){
	String fecha_repro_sepla="";
	if (are_tex_razon_anula.getValue()!=null && !are_tex_razon_anula.getValue().toString().isEmpty()) {
		
		/*if (cal_fecha_inicio.getValue()==null || cal_fecha_inicio.isEmpty(true)) {
			utilitario.agregarMensajeInfo("Fecha Inicio Reprogramacion", "Debe ingresar una fecha valida");
			return;
		}
		
		if (cal_fecha_fin.getValue()==null || cal_fecha_fin.isEmpty(true)) {
			utilitario.agregarMensajeInfo("Fecha Fin Reprogramacion", "Debe ingresar una fecha valida");
			return;
		}
		
		
		if (utilitario.isFechaMayor(utilitario.DeStringADate(cal_fecha_inicio.getFecha()), utilitario.DeStringADate(cal_fecha_fin.getFecha()))) {
			utilitario.agregarMensajeInfo("Fecha Inicio no puede ser menor a Fecha Fin", "Debe ingresar una fecha valida");
			return;
		}
		
		if (utilitario.isFechaMayor(utilitario.DeStringADate(tab_respuesta.getValor("fecha_hasta_seres")),utilitario.DeStringADate(cal_fecha_inicio.getFecha()))) {
			utilitario.agregarMensajeInfo("Fecha Desde no puede ser menor a Fecha Fin de Plan Anterior", "Debe ingresar una fecha valida");
			return;
		}
		
		if (utilitario.isFechaMayor(utilitario.DeStringADate(tab_respuesta.getValor("fecha_hasta_seres")),utilitario.DeStringADate(cal_fecha_fin.getFecha()))) {
			utilitario.agregarMensajeInfo("Fecha Hasta no puede ser menor a Fecha Fin de Plan Anterior", "Debe ingresar una fecha valida");
			return;
		}*/
		
			/*	utilitario.getConexion().agregarSqlPantalla("update seg_respuesta set  "
						+ "descripcion_repro_seres='"+are_tex_razon_anula.getValue().toString()+"',"
						+ "fecha_inicio_reprogramacion_seres='"+cal_fecha_inicio.getFecha()+"',"
						+ "fecha_fin_reprogramacion_seres='"+cal_fecha_fin.getFecha()+"' "
						+ "reprogramacion_seres=true where ide_seres="+tab_respuesta.getValor("ide_seres"));	
			
				guardarPantalla();*/
		
				//fecha_inicio_seres=cal_fecha_inicio.getFecha();
				//fecha_fin_seres=cal_fecha_fin.getFecha();
				descripcion_seres=are_tex_razon_anula.getValue().toString();
				//fecha_repro_sepla=utilitario.getFechaActual();
				
				if (bandReproPlanTotal) {
					reprogramarPlanTotal(descripcion_seres);	
				}else if (bandReproPlanParcial) {
					reprogramarPlan(descripcion_seres);
					
				}else {
					utilitario.agregarMensajeInfo("Debe escoger una opcion de Reprogramacion", "No se ha seleccionado una accion valida");
					return;
				}
	
				
			/*	if (bandReproPlanParcial==true) {
					reprogramarPlan(descripcion_seres);
				}else if (bandReproPlanTotal) {
					reprogramarPlanTotal(descripcion_seres);
				}*/
				
	
					}
				/*TablaGenerica tab_empleados=utilitario.consultar("SELECT ide_seres, ide_serec, descripcion_seres, hora_seres, ide_seusu, "
						+ "ide_seemp, ide_setre, activo_seres, fecha_seres, "
						+ "ide_sepla, ide_seinf, fecha_desde_seres, fecha_hasta_seres, medio_verificacion_seres, "
						+ "ide_gtemp_responsable, mecanismo_reporte_seres, observacion_seres, "
						+ "reprogramacion_seres, descripcion_repro_seres "
						+ "FROM seg_respuesta "
						+ "where ide_seres="+tab_respuesta.getValor("ide_seres"));

				copiarFila(fecha_inicio_seres, fecha_fin_seres,descripcion_seres,tab_empleados.getValor("ide_seres"));
    			utilitario.getConexion().ejecutarSql("update seg_respuesta set activo_seres =false,reprogramacion_seres=true where ide_seres in("+tab_empleados.getValor("ide_seres")+")");
    			
    			
				dia_anulado.cerrar();
				

				int ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));

				TablaGenerica tabEmpleado=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,emp.ide_gtemp "
						+ "FROM seg_usuario segus "
						+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
						+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
						+ "where segus.ide_seusu="+tab_respuesta.getValor("ide_seusu")
						+ " order by segus.ide_seusu");
				tabEmpleado.imprimirSql();

				
				try {
					EnviarCorreoAccion(""+ide_sepla, tab_recomendacion.getValor("IDE_SEINF"), tab_recomendacion.getValor("IDE_SEREC"),
					"Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tabEmpleado.getValor("ide_gtemp"), 7);
					//EnviarCorreo(Integer.parseInt(tab_permisos.getValor("ide_aspvh")));	
					guardarPantalla();
					utilitario.addUpdate("tab_tabulador:tab_respuesta,tab_respuesta");
					tab_respuesta.actualizar();
					utilitario.addUpdate("tab_respuesta,dia_anulado");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Correo Reprogramacion no enviado");				
					}
				

	} else {
		utilitario.agregarMensajeInfo("No se puede anular la solicitud", "Debe ingresar una Razon para anular  los Permisos");
	}	*/
	
}




public void aceptarDialogo(){
	dia_informacion.cerrar();
	utilitario.addUpdate("dia_informacion");
	
}




public Dialogo getDia_anulado() {
	return dia_anulado;
}


public void setDia_anulado(Dialogo dia_anulado) {
	this.dia_anulado = dia_anulado;
}

/**
 * 	
 * @param empleado
 * @param numIngresos
 * Metodo para copiar las filas de acuerdo al empleado y numero de repeticioe
 */

public void copiarFila(String fecha_inicio_seres,String fecha_fin_seres,String descripcion_seres,String ide_seres){ 	

	TablaGenerica tab_empleados=utilitario.consultar("SELECT ide_seres, ide_serec, descripcion_seres, hora_seres, ide_seusu, "
			+ "ide_seemp, ide_setre, activo_seres, fecha_seres, "
			+ "ide_sepla, ide_seinf, fecha_desde_seres, fecha_hasta_seres, medio_verificacion_seres, "
			+ "ide_gtemp_responsable, mecanismo_reporte_seres, observacion_seres, "
			+ "reprogramacion_seres, descripcion_repro_seres,nombre_archivo_seres "
			+ "FROM seg_respuesta "
			+ "where ide_seres="+ide_seres);

	tab_respuesta.setLectura(false);

	tab_respuesta.insertar();
	tab_respuesta.setValor("ide_serec",tab_empleados.getValor("ide_serec"));
	tab_respuesta.setValor("descripcion_seres",tab_empleados.getValor("descripcion_seres"));
	tab_respuesta.setValor("hora_seres",utilitario.getHoraActual());
	tab_respuesta.setValor("ide_seusu",tab_empleados.getValor("ide_seusu"));
	tab_respuesta.setValor("ide_seemp",tab_empleados.getValor("ide_seemp"));
	tab_respuesta.setValor("ide_setre","7");
	tab_respuesta.setValor("activo_seres","true");
	tab_respuesta.setValor("fecha_seres",utilitario.getFechaActual());
	tab_respuesta.setValor("ide_sepla",tab_empleados.getValor("ide_sepla"));
	tab_respuesta.setValor("ide_seinf",tab_empleados.getValor("ide_seinf"));
	tab_respuesta.setValor("fecha_desde_seres",fecha_inicio_seres);
	tab_respuesta.setValor("fecha_hasta_seres",fecha_fin_seres);
	tab_respuesta.setValor("medio_verificacion_seres",tab_empleados.getValor("medio_verificacion_seres"));
	tab_respuesta.setValor("ide_gtemp_responsable",tab_empleados.getValor("ide_gtemp_responsable"));
	tab_respuesta.setValor("observacion_seres",tab_empleados.getValor("observacion_seres"));
	//tab_respuesta.setValor("mecanismo_reporte_seres",tab_empleados.getValor("mecanismo_reporte_seres"));
	tab_respuesta.setValor("reprogramacion_seres","false");
	tab_respuesta.setValor("descripcion_repro_seres",descripcion_seres);
	//tab_respuesta.setValor("nombre_archivo_seres",tab_empleados.getValor("nombre_archivo_seres"));
	tab_respuesta.setValor("ide_seres_anterior",ide_seres);
	tab_respuesta.guardar();
	guardarPantalla();
}


public void copiarFilaRespuesta(String ide_sepla_anterior, int sumaDias,int ide_sepla){ 	

	TablaGenerica tab_empleados=utilitario.consultar("SELECT "
			+ "ide_seres, "
			+ "ide_serec, "
			+ "descripcion_seres, "
			+ "hora_seres, "
			+ "ide_seusu, "
			+ "ide_seemp, "
			+ "ide_setre, "
			+ "activo_seres, "
			+ "fecha_seres, "
			+ "ide_sepla, "
			+ "ide_seinf, "
			+ "fecha_desde_seres, "
			+ "fecha_hasta_seres, "
			+ "medio_verificacion_seres, "
			+ "ide_gtemp_responsable, "
			+ "mecanismo_reporte_seres, "
			+ "observacion_seres, "
			+ "reprogramacion_seres, "
			+ "descripcion_repro_seres, "
			+ "aceptar_repro_seres, "
			+ "nombre_archivo_seres, "
			+ "ide_seres_anterior "
			+ "FROM seg_respuesta "
			+ "where ide_sepla="+ide_sepla_anterior+" and ide_setre=5 "
			+ "order by fecha_desde_seres asc, fecha_hasta_seres asc");
	
	tab_respuesta.setLectura(false);

for (int i = 0; i < tab_empleados.getTotalFilas(); i++) {
	
	tab_respuesta.insertar();
	tab_respuesta.setValor("ide_serec",tab_empleados.getValor(i,"ide_serec"));
	tab_respuesta.setValor("descripcion_seres",tab_empleados.getValor(i,"descripcion_seres"));
	tab_respuesta.setValor("hora_seres",utilitario.getHoraActual());
	tab_respuesta.setValor("ide_seusu",tab_empleados.getValor(i,"ide_seusu"));
	tab_respuesta.setValor("ide_seemp",tab_empleados.getValor(i,"ide_seemp"));
	tab_respuesta.setValor("ide_setre","6");
	tab_respuesta.setValor("activo_seres","true");
	tab_respuesta.setValor("fecha_seres",utilitario.getFechaActual());
	tab_respuesta.setValor("ide_sepla",""+ide_sepla);
	tab_respuesta.setValor("ide_seinf",tab_empleados.getValor("ide_seinf"));
	
	String fecha_desde_nueva="",fecha_hasta_nueva="";
	fecha_desde_nueva=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(tab_respuesta.getValor(i,"fecha_desde_seres")), sumaDias));
	fecha_hasta_nueva=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(tab_respuesta.getValor(i,"fecha_hasta_seres")), sumaDias));

	tab_respuesta.setValor("fecha_desde_seres",fecha_desde_nueva);
	tab_respuesta.setValor("fecha_hasta_seres",fecha_hasta_nueva);
	tab_respuesta.setValor("medio_verificacion_seres",tab_empleados.getValor(i,"medio_verificacion_seres"));
	tab_respuesta.setValor("ide_gtemp_responsable",tab_empleados.getValor(i,"ide_gtemp_responsable"));
	tab_respuesta.setValor("mecanismo_reporte_seres",null);
	tab_respuesta.setValor("observacion_seres",null);
	tab_respuesta.setValor("reprogramacion_seres","false");
	tab_respuesta.setValor("descripcion_repro_seres",null);
	tab_respuesta.setValor("aceptar_repro_seres","false");
	tab_respuesta.setValor("nombre_archivo_seres",null);
	tab_respuesta.setValor("ide_seres_anterior",null);
	
	
	
	tab_respuesta.guardar();
	
	guardarPantalla();
	}
}



public void enviarAccionRechazo(){		
String ide_seinf="",ide_serec="";
ide_seinf=tab_informe.getValor("ide_seinf");
ide_serec=tab_recomendacion.getValor("ide_serec");
int ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
if (ide_sepla==0) {
	utilitario.agregarMensajeInfo("No se puede enviar Acciones de Rechazo", "No contiene un Plan de Accion Activo");
	return;
}

try {

	TablaGenerica tab_empleados=utilitario.consultar("SELECT ide_seres, ide_serec, descripcion_seres, hora_seres, ide_seusu, "
			+ "ide_seemp, ide_setre, activo_seres, fecha_seres, "
			+ "ide_sepla, ide_seinf, fecha_desde_seres, fecha_hasta_seres, medio_verificacion_seres, "
			+ "ide_gtemp_responsable, mecanismo_reporte_seres, observacion_seres, "
			+ "reprogramacion_seres, descripcion_repro_seres "
			+ "FROM seg_respuesta "
			+ "where ide_serec="+ide_serec+" and ide_seinf="+ide_seinf+" and ide_sepla="+ide_sepla+" and ide_setre=4" );

			//+ "where ide_serec="+ide_serec+" and ide_seinf="+ide_seinf+"  and activo_seres=true and ide_sepla is not null" );

	if(tab_empleados.getTotalFilas()>0){	
	     TablaGenerica tab_empleado=utilitario.consultar("SELECT * FROM  "
					+ "SEG_ASIGNACION ASI "
					+ "left join seg_empresa emp on emp.ide_seemp=asi.ide_seemp "
					+ "left join seg_usuario usu on usu.ide_seemp=asi.ide_seemp "
					+ "left join sis_usuario usua on usua.ide_usua=usu.ide_usua "
					+ "WHERE asi.IDE_seinf="+tab_informe.getValor("ide_seinf")+" and asi.ide_serec="+tab_recomendacion.getValor("ide_serec")+" and usu.activo_seusu=true");

			if (tab_empleado.getTotalFilas()>0) {
				for (int i = 0; i < tab_empleado.getTotalFilas(); i++) {
	
		//if (tab_plan_accion.getValor("envio_plan")==null || tab_plan_accion.getValor("envio_plan").equals("") || tab_plan_accion.getValor("envio_plan").isEmpty() || tab_plan_accion.getValor("envio_plan").equals("false") ) {
			//for (int i = 0; i < tab_empleados.getTotalFilas(); i++) {
		//EnviarCorreoAccion(""+tab_empleados.getValor("ide_sepla"), tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf"), tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec"),
	//" Asunto: "+tab_respuesta.getValor("descripcion_seres"),tab_empleado.getValor(i,"ide_gtemp"),9,"","");
				}
		
			//	EnviarCorreoAccion(tab_empleados.getValor(i,"ide_sepla"), tab_empleados.getValor(i,"ide_seinf"), tab_empleados.getValor(i,"ide_serec"),tab_empleados.getValor(i,"descripcion_seres") ,empleado , 2);	
				}
				//utilitario.getConexion().ejecutarSql("update seg_plan_accion set envio_plan=true where ide_sepla=" + tab_empleados.getValor("ide_sepla"));
			
		}else {
		utilitario.agregarMensajeInfo("No se puede enviar el Plan de Accion", "Ya ha sido enviado el plan de accion pongase en contacto con el Administrador");
		return;
		}
	//}
	
	

	
	
} catch (Exception e) {
	// TODO Auto-generated catch block
System.out.println("Error al enviar plan");

}


}




public void enviarPlan(){		
String ide_seinf="",ide_serec="";

ide_seinf=tab_informe.getValor("ide_seinf");
ide_serec=tab_recomendacion.getValor("ide_serec");


try {
	int ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));

		/*TablaGenerica tab_plan_accion=utilitario.consultar("SELECT ide_sepla, envio_plan "
			+ "FROM seg_plan_accion "
			+ "where ide_sepla="+tab_empleados.getValor("ide_sepla")+" " );
	
	if (tab_plan_accion.getTotalFilas()>0) {*/
		//if (tab_plan_accion.getValor("envio_plan")==null || tab_plan_accion.getValor("envio_plan").equals("") || tab_plan_accion.getValor("envio_plan").isEmpty() || tab_plan_accion.getValor("envio_plan").equals("false") ) {
	//		for (int i = 0; i < tab_empleados.getTotalFilas(); i++) {

		TablaGenerica tab_resp_reprogramacion=utilitario.consultar("select * from seg_plan_accion where ide_sepla="+ide_sepla+" and reprogramacion_parcial_sepla=true and activo_sepla=true");

		if (tab_resp_reprogramacion.getTotalFilas()>0) {
		//	EnviarCorreoAccion(""+ide_sepla, tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf"), tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec"),
		//			" Asunto: "+tab_respuesta.getValor("descripcion_seres"),empleado,2,"","");
			
		}else {
			TablaGenerica tab_resp_reprogramacion_total=utilitario.consultar("select * from seg_plan_accion where ide_seinf="+tab_informe.getValor("ide_seinf")+" and "
					+ "ide_serec="+tab_recomendacion.getValor("ide_serec")+" and ide_sepla is not null  order by ide_sepla desc");
			
			if (tab_resp_reprogramacion_total.getTotalFilas()>0) {
				if (tab_resp_reprogramacion_total.getTotalFilas()==1) {
	TablaGenerica tab_empleados=utilitario.consultar("SELECT ide_seres, ide_serec, descripcion_seres, hora_seres, ide_seusu, "
			+ "ide_seemp, ide_setre, activo_seres, fecha_seres, "
			+ "ide_sepla, ide_seinf, fecha_desde_seres, fecha_hasta_seres, medio_verificacion_seres, "
			+ "ide_gtemp_responsable, mecanismo_reporte_seres, observacion_seres, "
			+ "reprogramacion_seres, descripcion_repro_seres "
			+ "FROM seg_respuesta "
							+ "where ide_serec="+ide_serec+" and ide_seinf="+ide_seinf+" and ide_sepla="+ide_sepla+"  and activo_seres=true " );

			//+ "where ide_serec="+ide_serec+" and ide_seinf="+ide_seinf+"  and activo_seres=true and ide_sepla is not null" );

	if(tab_empleados.getTotalFilas()>0){	
						 TablaGenerica tab_empleado=utilitario.consultar("SELECT * FROM  "
									+ "SEG_ASIGNACION ASI "
									+ "left join seg_empresa emp on emp.ide_seemp=asi.ide_seemp "
									+ "left join seg_usuario usu on usu.ide_seemp=asi.ide_seemp "
									+ "left join sis_usuario usua on usua.ide_usua=usu.ide_usua "
									+ "WHERE asi.IDE_seinf="+tab_informe.getValor("ide_seinf")+" and asi.ide_serec="+tab_recomendacion.getValor("ide_serec")+" and usu.activo_seusu=true");
	
					/*TablaGenerica tabEmpleado=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,emp.ide_gtemp "
								+ "FROM seg_usuario segus "
								+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
								+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
								+ "where segus.ide_seusu="+tab_respuesta.getValor("ide_seusu")
								+ " order by segus.ide_seusu");*/
						for (int i = 0; i < tab_empleado.getTotalFilas(); i++) {
					//	EnviarCorreoAccion(""+tab_empleados.getValor("ide_sepla"), tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf"), tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec"),
					//			" Asunto: "+tab_respuesta.getValor("descripcion_seres"),tab_empleado.getValor(i,"ide_gtemp"),2,"","");
						
						}
					}else {
						utilitario.agregarMensajeInfo("No se puede enviar el Plan de Accion", "No existen acciones AVANCE o APROBADAS");
						return;
						}				
					
				}else {
					if(tab_resp_reprogramacion_total.getValor(1,"reprogramacion_total_sepla")==null || tab_resp_reprogramacion_total.getValor(1,"reprogramacion_total_sepla").isEmpty()){
						TablaGenerica tab_empleados=utilitario.consultar("SELECT ide_seres, ide_serec, descripcion_seres, hora_seres, ide_seusu, "
								+ "ide_seemp, ide_setre, activo_seres, fecha_seres, "
								+ "ide_sepla, ide_seinf, fecha_desde_seres, fecha_hasta_seres, medio_verificacion_seres, "
								+ "ide_gtemp_responsable, mecanismo_reporte_seres, observacion_seres, "
								+ "reprogramacion_seres, descripcion_repro_seres "
								+ "FROM seg_respuesta "
								+ "where ide_serec="+ide_serec+" and ide_seinf="+ide_seinf+" and ide_sepla="+ide_sepla+" and activo_seres=true  " );
		
								//+ "where ide_serec="+ide_serec+" and ide_seinf="+ide_seinf+"  and activo_seres=true and ide_sepla is not null" );
				
						if(tab_empleados.getTotalFilas()>0){	
							TablaGenerica tab_empleado=utilitario.consultar("SELECT * FROM  "
									+ "SEG_ASIGNACION ASI "
									+ "left join seg_empresa emp on emp.ide_seemp=asi.ide_seemp "
									+ "left join seg_usuario usu on usu.ide_seemp=asi.ide_seemp "
									+ "left join sis_usuario usua on usua.ide_usua=usu.ide_usua "
									+ "WHERE asi.IDE_seinf="+tab_informe.getValor("ide_seinf")+" and asi.ide_serec="+tab_recomendacion.getValor("ide_serec")+" and usu.activo_seusu=true");

					/*TablaGenerica tabEmpleado=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,emp.ide_gtemp "
								+ "FROM seg_usuario segus "
								+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
								+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
								+ "where segus.ide_seusu="+tab_respuesta.getValor("ide_seusu")
								+ " order by segus.ide_seusu");*/
						for (int i = 0; i < tab_empleado.getTotalFilas(); i++) {
			
						//	EnviarCorreoAccion(""+tab_empleados.getValor("ide_sepla"), tab_informe.getValor("numero_seinf")+" Asunto: "+tab_informe.getValor("asunto_seinf"), tab_recomendacion.getValor("numero_serec")+" Asunto: "+tab_recomendacion.getValor("asunto_serec"),
						//			" Asunto: "+tab_respuesta.getValor("descripcion_seres"),tab_empleado.getValor(i,"ide_gtemp"),2,"","");
						}
		}else {
		utilitario.agregarMensajeInfo("No se puede enviar el Plan de Accion", "No existen acciones AVANCE o APROBADAS");
		return;
		}
						
					}else {
						if(tab_resp_reprogramacion_total.getValor(1,"reprogramacion_total_sepla").equals("true")){
							//CORREO
						}
					
					}
				}
			}else {
				

				
				
			}
		}
		
				
			//	EnviarCorreoAccion(tab_empleados.getValor(i,"ide_sepla"), tab_empleados.getValor(i,"ide_seinf"), tab_empleados.getValor(i,"ide_serec"),tab_empleados.getValor(i,"descripcion_seres") ,empleado , 2);	
			//	}
				//utilitario.getConexion().ejecutarSql("update seg_plan_accion set envio_plan=true where ide_sepla=" + tab_empleados.getValor("ide_sepla"));
			

	//}
	
	
	

	
} catch (Exception e) {
	// TODO Auto-generated catch block
System.out.println("Error al enviar plan");

}


}


public boolean validarEstadoAsignacion(){
	boolean retornaValor=false;
	//tab_respuesta.modificar(evt);
	if (tab_respuesta.getValor("ide_setre")==null || tab_respuesta.getValor("ide_setre").isEmpty() || tab_respuesta.getValor("ide_setre").equals("")) {
		retornaValor=true;
	}else if (tab_respuesta.getValor("ide_setre").equals("6")) {
		retornaValor= false;
	}else if(tab_respuesta.getValor("ide_setre").equals("1")){
		retornaValor= false;
	}else if(tab_respuesta.getValor("ide_setre").equals("3")){
			retornaValor= false;
	}else if(tab_respuesta.getValor("ide_setre").equals("4")){
		retornaValor= false;
	}else if(tab_respuesta.getValor("ide_setre").equals("5")){
		retornaValor= false;
	}else if(tab_respuesta.getValor("ide_setre").equals("7")){
		retornaValor= false;
	}else if(tab_respuesta.getValor("ide_setre").equals("8")){
		retornaValor= false;
	}
	else{
		retornaValor=true;
	}
	return retornaValor;
}


public void validarReprogramacion(AjaxBehaviorEvent evt){
	
	if (!tab_respuesta.getValor("ide_setre").equals("7") && (tab_respuesta.getValor("ide_seres_anterior")==null || tab_respuesta.getValor("ide_seres_anterior").isEmpty()) ) {
		tab_respuesta.setValor("aceptar_repro_seres","false");
		utilitario.addUpdateTabla(tab_respuesta, "aceptar_repro_seres", "");
		utilitario.agregarMensajeInfo("No se puede realizar esta accionr", "No se ha solicitado reprogramacion de accion");
		return ;
	}
	con_solicitud_reprogramar.dibujar();
	
	
}

public void aceptarReprogramacion(){
	//tab_respuesta.modificar(evt);
	try {
		utilitario.getConexion().ejecutarSql("update seg_respuesta  set aceptar_repro_seres=true,ide_setre=6 where ide_seres="+tab_respuesta.getValor("ide_seres"));
	 TablaGenerica tab_usuario=utilitario.consultar("SELECT susua.ide_seusu, susua.ide_seemp, susua.ide_secar, susua.nombre_seusu, susua.login_seusu, susua.password_seusu, "
	 		+ "susua.usu_email, susua.ide_usua, susua.activo_seusu ,usua.ide_gtemp "
	 		+ "FROM seg_usuario susua  "
	 		+ "left join sis_usuario usua on usua.ide_usua=susua.ide_usua "
			+ "where susua.ide_seusu="+tab_respuesta.getValor("ide_seusu")+" and susua.activo_seusu=true ") ;
	
//	EnviarCorreoAccion(tab_respuesta.getValor("ide_seres"), tab_respuesta.getValor("ide_seinf"), tab_respuesta.getValor("ide_serec"), 
//		"Descripcion: "+tab_respuesta.getValor("descripcion_seres")+", Medio de Verificacion: "+tab_respuesta.getValor("medio_verificacion_seres")+", Fecha desde: "+tab_respuesta.getValor("fecha_desde_seres")+", Fecha hasta: "+tab_respuesta.getValor("fecha_hasta_seres"), tab_usuario.getValor("IDE_GTEMP"), 6,"","");
		tab_respuesta.actualizar();
	} catch (Exception e) {
		// TODO Auto-generated catch block
System.out.println("ERROR AL ENVIAR CORREO DE REPROGRAMACION");	}
	


	
}



public void validarAccionInvalida(){
	int cont=0;
	TablaGenerica tab_empleados=utilitario.consultar("SELECT ide_seres, ide_serec, descripcion_seres, hora_seres, ide_seusu, "
			+ "ide_seemp, ide_setre, activo_seres, fecha_seres, "
			+ "ide_sepla, ide_seinf, fecha_desde_seres, fecha_hasta_seres, medio_verificacion_seres, "
			+ "ide_gtemp_responsable, mecanismo_reporte_seres, observacion_seres, "
			+ "reprogramacion_seres, descripcion_repro_seres "
			+ "FROM seg_respuesta "
			+ "where ide_serec="+tab_recomendacion.getValor("IDE_SEREC")+" and ide_seinf="+tab_informe.getValor("IDE_SEINF")+" and ide_sepla is not null" );
String  fecha=utilitario.getFechaActual();

for (int i = 0; i < tab_empleados.getTotalFilas(); i++) {
if (utilitario.isFechaMayor(utilitario.DeStringADate(fecha), utilitario.DeStringADate(tab_empleados.getValor(i,"fecha_hasta_seres")))) {
	utilitario.getConexion().ejecutarSql("update seg_respuesta  set ide_setre=8 where ide_seres="+tab_empleados.getValor(i,"ide_seres"));
cont++;
}	
}
if (cont>0) {
	utilitario.agregarMensajeInfo("Se han actualizado las recomendaciones", "Estado actualizado a Incumplido");
	tab_respuesta.actualizar();
}	
	
	
}

public void cancelarReprogramar(){
	tab_respuesta.actualizar();
	dia_anulado.cerrar();
	utilitario.addUpdate("tab_tabulador:tab_respuesta,tab_respuesta,dia_anulado");
}

public void cancelarAprobarSolicitud(){
	tab_respuesta.actualizar();
	con_aprobar_solicitud.cerrar();
	con_solicitud_reprogramar.cerrar();
	utilitario.addUpdate("tab_tabulador:tab_respuesta,tab_respuesta");
}


@Override
public void aceptarBuscar() {
	// TODO Auto-generated method stub
	super.aceptarBuscar();
	actualizarTablas();

}
//public void reprogramarPlan(String descripcion_seres){
public void reprogramarPlan(String descripcion_seres){
	int ide_sepla=0;
	bandReproPlanTotal=false;
	bandReproPlanParcial=true;
	ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
	
	
	
	TablaGenerica tab_recomendaciones_plan=utilitario.consultar("select ide_seres,ide_serec,ide_seinf,fecha_desde_seres,fecha_hasta_seres,"
			+ "mecanismo_reporte_seres,ide_setre from seg_respuesta where ide_sepla in("+ide_sepla+") and ide_setre in(1,6,8) ");
	
	if (tab_recomendaciones_plan.getTotalFilas()>0) {
	for (int i = 0; i < tab_recomendaciones_plan.getTotalFilas(); i++) {
		if (tab_recomendaciones_plan.getValor(i,"ide_setre").equals("6")) {
			if (tab_recomendaciones_plan.getValor(i,"mecanismo_reporte_seres")==null || tab_recomendaciones_plan.getValor(i,"mecanismo_reporte_seres").isEmpty() || tab_recomendaciones_plan.getValor(i,"mecanismo_reporte_seres").equals("")) {
				utilitario.getConexion().ejecutarSql("update seg_respuesta  set activo_seres=false,reprogramacion_seres=true where ide_sepla="+ide_sepla+" and "
						+ "ide_seres="+tab_recomendaciones_plan.getValor(i,"IDE_SERES"));
			}
		}else {
			utilitario.getConexion().ejecutarSql("update seg_respuesta  set activo_seres=false,reprogramacion_seres=true where ide_sepla="+ide_sepla+" and "
					+ "ide_seres="+tab_recomendaciones_plan.getValor(i,"IDE_SERES"));;
		}

	}	
	
	
	//if (tab_recomendaciones_plan.getTotalFilas()>0) {
		utilitario.getConexion().ejecutarSql("update seg_plan_accion  set descripcion_repro_sepla='"+descripcion_seres+"',reprogramacion_parcial_sepla=true,fecha_repro_sepla='"+utilitario.getFechaActual()+"' where ide_sepla="+ide_sepla);
	/*	for (int i = 0; i < tab_recomendaciones_plan.getTotalFilas(); i++) {			
			utilitario.getConexion().ejecutarSql("update seg_respuesta  set activo_seres=false,reprogramacion_seres=true where ide_seres="+tab_recomendaciones_plan.getValor(i,"ide_seres")+"");*/
		tab_respuesta.actualizar();
		//utilitario.addUpdate("tab_respuesta");
		//utilitario.agregarMensajeInfo("Se ha Reprogramado-Parcialmente","Por favor registre las nuevas actividades");
		//}
		
		try {
			 TablaGenerica tab_empleado=utilitario.consultar("SELECT * FROM  "
						+ "SEG_ASIGNACION ASI "
						+ "left join seg_empresa emp on emp.ide_seemp=asi.ide_seemp "
						+ "left join seg_usuario usu on usu.ide_seemp=asi.ide_seemp "
						+ "left join sis_usuario usua on usua.ide_usua=usu.ide_usua "
						+ "WHERE asi.IDE_seinf="+tab_informe.getValor("ide_seinf")+" and asi.ide_serec="+tab_recomendacion.getValor("ide_serec")+" and usu.activo_seusu=true");
			
		/*TablaGenerica tabEmpleado=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,emp.ide_gtemp "
					+ "FROM seg_usuario segus "
					+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
					+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
					+ "where segus.ide_seusu="+tab_respuesta.getValor("ide_seusu")
					+ " order by segus.ide_seusu");*/
			for (int i = 0; i < tab_empleado.getTotalFilas(); i++) {
				//	EnviarCorreoAccion(""+ide_sepla, tab_recomendacion.getValor("IDE_SEINF"), tab_recomendacion.getValor("IDE_SEREC"),
				//		"Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tab_empleado.getValor(i,"ide_gtemp"), 7,utilitario.getFechaActual(),descripcion_seres);
			
			}
		//	EnviarCorreoAccion(""+ide_sepla, tab_recomendacion.getValor("IDE_SEINF"), tab_recomendacion.getValor("IDE_SEREC"),
			//		"Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tabEmpleado.getValor("ide_gtemp"), 7);
			
			dia_anulado.cerrar();
			utilitario.addUpdate("tab_respuesta,dia_anulado");
			utilitario.agregarMensajeInfo("Reprogramacion Parcial realizada con exito","");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//System.out.println("Correo REPROGRAMACION PARCIAL Y PLAN. "+ide_sepla);
		}
		
		}else {
			utilitario.agregarMensajeInfo("No se puede realizar la accion solicitada","No contiene acciones a reprogramar");
			return;
		}
	
}



public void reprogramarPlanTotal(String descripcion_seres){
	int ide_sepla=0;
	bandReproPlanTotal=true;
	bandReproPlanParcial=false;
	int contRegistrosInvalidos=0;
	ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
	
	TablaGenerica tab_rec=utilitario.consultar("select ide_seres,ide_serec,ide_seinf,fecha_desde_seres,fecha_hasta_seres,"
			+ "mecanismo_reporte_seres,ide_setre from seg_respuesta where ide_sepla in("+ide_sepla+")  ");
	
	
	
	
	if (tab_rec.getTotalFilas()<=0) {
		utilitario.agregarMensajeInfo("No se han encontrado registro validos","No se puede realizar la accion requerida");
		dia_anulado.cerrar();
		utilitario.addUpdate("dia_anulado");
		return;
	}else {	}
	
	
	
/*	TablaGenerica tab_recomendaciones_plan=utilitario.consultar("select ide_seres,ide_serec,ide_seinf,fecha_desde_seres,fecha_hasta_seres,"
			+ "mecanismo_reporte_seres,ide_setre from seg_respuesta where ide_sepla in("+ide_sepla+") and ide_setre  in(1,8,6)  ");
	//and ide_setre in(1,8)
	if (tab_recomendaciones_plan.getTotalFilas()>0) {*/

		utilitario.getConexion().ejecutarSql("update seg_plan_accion  set descripcion_repro_sepla='"+descripcion_seres+"',activo_sepla=false,reprogramacion_total_sepla=true,fecha_repro_sepla='"+utilitario.getFechaActual()+"' where ide_sepla="+ide_sepla);
		utilitario.getConexion().ejecutarSql("update seg_respuesta  set activo_seres=false,reprogramacion_seres=true where ide_sepla="+ide_sepla+" and "
				+ "ide_serec="+tab_recomendacion.getValor("IDE_SEREC")+" and ide_seinf="+tab_recomendacion.getValor("IDE_SEINF"));
		//utilitario.addUpdate("tab_respuesta");
		//utilitario.agregarMensajeInfo("Se ha Reprogramado todo el Plan de Accion","Por favor registre las nuevas actividades");
	 
	  String nueva_Fecha="",ide_sepla_anterior="";
	  ide_sepla_anterior=""+ide_sepla;	
		insertarTablaPlanAccion(usuario_responsable, tab_recomendacion.getValor("IDE_SEREC"), utilitario.getFechaActual(), null, 2, "", tab_informe.getValor("ide_seinf"), null, "PLAN DE ACCION");
		ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
		  insertarTablaRespuestaReprogramacion(ide_sepla_anterior,ide_sepla);

/*
	 int valor_periodicidad=0,dias_sumataria=0;
	 valor_periodicidad=Integer.parseInt(tab_plan_aplicado_activo.getValor("valor_seper"));
	  dias_sumataria=valor_periodicidad*30;
	  String nueva_Fecha="",ide_sepla_anterior="";
	  ide_sepla_anterior=tab_respuesta.getValor("ide_sepla");	   
	  insertarTablaPlanAccionPeriocidad(usuario_responsable, tab_recomendacion.getValor("IDE_SEREC"), utilitario.getFechaActual(), null, 2, "", tab_informe.getValor("ide_seinf"), ide_seper, "PLAN DE ACCION"
			  ,Integer.parseInt(ide_seemp),nueva_Fecha,observacion_sepla);
	  ide_sepla=retornaIdeSepla(tab_recomendacion.getValor("IDE_SEREC"),tab_recomendacion.getValor("IDE_SEINF"));
	  insertarTablaRespuestaReprogramacion(ide_sepla_anterior,ide_sepla);
	  actualizarTablas();
	  tab_respuesta.actualizar();
*/
	 
	 
			 tab_respuesta.actualizar();

	 
		try {
			 TablaGenerica tab_empleado=utilitario.consultar("SELECT * FROM  "
						+ "SEG_ASIGNACION ASI "
						+ "left join seg_empresa emp on emp.ide_seemp=asi.ide_seemp "
						+ "left join seg_usuario usu on usu.ide_seemp=asi.ide_seemp "
						+ "left join sis_usuario usua on usua.ide_usua=usu.ide_usua "
						+ "WHERE asi.IDE_seinf="+tab_informe.getValor("ide_seinf")+" and asi.ide_serec="+tab_recomendacion.getValor("ide_serec")+" and usu.activo_seusu=true");
			
		/*TablaGenerica tabEmpleado=utilitario.consultar("SELECT  segus.ide_seusu,usua.nick_usua,emp.ide_gtemp "
					+ "FROM seg_usuario segus "
					+ "left join sis_usuario usua on usua.ide_usua=segus.ide_usua "
					+ "left join gth_empleado emp on emp.ide_gtemp=usua.ide_gtemp "
					+ "where segus.ide_seusu="+tab_respuesta.getValor("ide_seusu")
					+ " order by segus.ide_seusu");*/
			for (int i = 0; i < tab_empleado.getTotalFilas(); i++) {
			//EnviarCorreoAccion(""+ide_sepla, tab_recomendacion.getValor("IDE_SEINF"), tab_recomendacion.getValor("IDE_SEREC"),
			//			"Descripcion: "+tab_respuesta.getValor("descripcion_seres")+" ", tab_empleado.getValor(i,"ide_gtemp"), 7,utilitario.getFechaActual(),descripcion_seres);
			
			}
			dia_anulado.cerrar();
			utilitario.addUpdate("tab_respuesta,dia_anulado");
			utilitario.agregarMensajeInfo("Reprogramacion Total realizada con exito","");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//System.out.println("Correo REPROGRAMACION PARCIAL Y PLAN. "+ide_sepla);

		}
		/*}else {
			utilitario.agregarMensajeInfo("No se puede realizar la accion solicitada","No contiene acciones a reprogramar");
			dia_anulado.cerrar();
			utilitario.addUpdate("dia_anulado");
			return;
		}*/
	
}


public void opcionReprogramar(){
	if (bandReproPlanTotal) {
		dia_anulado.dibujar();
		are_tex_razon_anula.setValue("");
		utilitario.addUpdate("are_tex_razon_anula");
		
	}else if(bandReproPlanParcial){
		dia_anulado.dibujar();
		are_tex_razon_anula.setValue("");
		utilitario.addUpdate("are_tex_razon_anula");
	}
	
}
 

public int getIde_sepla_periocidad() {
	return ide_sepla_periocidad;
}
public void setIde_sepla_periocidad(int ide_sepla_periocidad) {
	this.ide_sepla_periocidad = ide_sepla_periocidad;
}


public boolean isBandReproPlanTotal() {
	return bandReproPlanTotal;
}


public void setBandReproPlanTotal(boolean bandReproPlanTotal) {
	this.bandReproPlanTotal = bandReproPlanTotal;
}


public boolean isBandReproPlanParcial() {
	return bandReproPlanParcial;
}


public void setBandReproPlanParcial(boolean bandReproPlanParcial) {
	this.bandReproPlanParcial = bandReproPlanParcial;
}

public void desplegarAsunto(){
	estadoRespuesta=0;
	String ide_setre="",fecha_desde_seres="",fecha_hasta_seres="",mecanismo_reporte_seres="",fecha_actual="";
	//tab_informe.modificar(evt);
	if (tab_informe.getValor("ide_setre")==null || tab_respuesta.getValor("ide_setre").equals("")) {
		estadoRespuesta=0;

	}else {
		
	}
}	


public void actualizarInformativo(){
	String ide_seinf="";
	TablaGenerica tab_informativo=utilitario.consultar("select ide_seinf,asunto_seinf from seg_informe where ide_seinf="+tab_informe.getValor("ide_seinf"));
	are_tex_informativa.setValue(tab_informativo.getValor("asunto_seinf"));
	utilitario.addUpdate("are_tex_informativa");
	
}

public void actualizarInformativoRecomendacion(){
	String ide_seinf="";
	TablaGenerica tab_informativo_recomendacion=utilitario.consultar("select ide_seinf,asunto_serec from seg_recomendacion where ide_serec="+tab_recomendacion.getValor("ide_serec"));
	are_tex_informativa_recomendacion.setValue(tab_informativo_recomendacion.getValor("asunto_serec"));
	utilitario.addUpdate("are_tex_informativa_recomendacion");
	
}




}
	
