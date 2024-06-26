package paq_precontractual;


import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import framework.aplicacion.TablaGenerica;
import framework.componentes.*;

import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.event.SelectEvent;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_precontractual.ejb.ServicioDocumentoRequisito;
import paq_precontractual.ejb.ServicioEtapa;
import paq_precontractual.ejb.ServicioEtapaProcedimiento;
import paq_precontractual.ejb.ServicioEtapaRequisito;
import paq_precontractual.ejb.ServicioFUIxls;
import paq_precontractual.ejb.ServicioFase;
import paq_precontractual.ejb.ServicioGeneralAdmPrecon;
import paq_precontractual.ejb.ServicioPrecontractual;
import paq_precontractual.ejb.ServicioProcedimiento;
import paq_precontractual.ejb.ServicioRequisito;
import paq_precontractual.ejb.ServicioRuta;
import paq_precontractual.ejb.ServicioSeguimiento;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;


public class precon_proceso extends Pantalla { //Expediente del Proceso

	private SeleccionTabla set_pbuscar=new SeleccionTabla();
	private Tabla tab_precontractual = new Tabla();
	private Tabla tab_documento_requisito = new Tabla();
	private Tabla tab_seguimiento=new Tabla();

	private PanelMenu pam_menu = new PanelMenu();
	private Panel pan_opcion = new Panel();
	private int int_opcion = 0;// sirve para identificar la opcion que se encuentra dibujada en pantalla
	private Efecto efecto = new Efecto();
	private Division div_division = new Division();
	
	Etiqueta eti_proceso = new Etiqueta();

	String ide_gtempxx="0";
	String ide_prpre="0";
	
	@EJB
	private ServicioEtapaProcedimiento ser_EtapaProcedimiento = (ServicioEtapaProcedimiento) utilitario.instanciarEJB(ServicioEtapaProcedimiento.class);
	@EJB
	private ServicioRequisito ser_Requisito = (ServicioRequisito) utilitario.instanciarEJB(ServicioRequisito.class);
	@EJB
	private ServicioSeguimiento ser_Seguimiento = (ServicioSeguimiento) utilitario.instanciarEJB(ServicioSeguimiento.class);
	@EJB
	private ServicioGeneralAdmPrecon ser_generalAdm = (ServicioGeneralAdmPrecon ) utilitario.instanciarEJB(ServicioGeneralAdmPrecon.class);	
	@EJB
    private ServicioPrecontractual ser_precontractual = (ServicioPrecontractual) utilitario.instanciarEJB(ServicioPrecontractual.class);
	@EJB
    private ServicioEtapaRequisito ser_etapa_requisito = (ServicioEtapaRequisito) utilitario.instanciarEJB(ServicioEtapaRequisito.class);
	@EJB
    private ServicioEtapa ser_etapa = (ServicioEtapa) utilitario.instanciarEJB(ServicioEtapa.class);
	@EJB
    private ServicioRuta ser_ruta = (ServicioRuta) utilitario.instanciarEJB(ServicioRuta.class);
	@EJB
    private ServicioProcedimiento ser_procedimiento = (ServicioProcedimiento) utilitario.instanciarEJB(ServicioProcedimiento.class);
	@EJB
    private ServicioDocumentoRequisito ser_docrquisto = (ServicioDocumentoRequisito) utilitario.instanciarEJB(ServicioDocumentoRequisito.class);
	@EJB
    private ServicioFase ser_fase = (ServicioFase) utilitario.instanciarEJB(ServicioFase.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
    private ServicioFUIxls ser_fui_xls = (ServicioFUIxls) utilitario.instanciarEJB(ServicioFUIxls.class);

	public precon_proceso() {

		bar_botones.limpiar();
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		
		/*if(!pckUtilidades.Utilitario.obtenerIPhost().contains("SRV103ERP02"))
		{
			utilitario.agregarNotificacionInfo("MENSAJE - AUTORIZACION DE MODULO","Esta pantalla no esta autorizada para usarse en el servidor actual (IP:"+pckUtilidades.Utilitario.obtenerIPhost()+"), favor use el servidor ERP desde la pagina servicios emgirs");
			utilitario.agregarMensajeInfo("Permisos", "Contacte con el adminsitrador... o use el servidor ERP...");
			return;
		}*/
		
	    //boton buscar 
		Boton bot_buscar=new Boton();
		bot_buscar.setIcon("ui-icon-search");
		bot_buscar.setValue("Buscar Proceso");
		bot_buscar.setMetodo("filtrarProceso");
		bar_botones.agregarComponente(new Etiqueta("Seleccione un Proceso:"));
		bar_botones.agregarBoton(bot_buscar);
		bar_botones.agregarBoton(bot_limpiar);
		
		Boton bot_generar= new Boton();
		bot_generar.setValue("Exportar Expediente");
		bot_generar.setMetodo("generarArchivo");
		bot_generar.setAjax(false);
		bar_botones.agregarBoton(bot_generar);
		
		Boton bot_excel=new Boton();
		bot_excel.setIcon("ui-icon-calculator");
  		bot_excel.setValue("GENERAR FUI");
  		bot_excel.setAjax(false);
  		bot_excel.setMetodo("exportarExcel");
  		bar_botones.agregarBoton(bot_excel); 
  		
  		set_pbuscar.setId("set_pbuscar");
  		set_pbuscar.setSeleccionTabla(ser_precontractual.getProcesoExpediente(),"ide_prpre");
  		set_pbuscar.setTitle("Seleccione Un Proceso de Contratación");
  		set_pbuscar.getTab_seleccion().getColumna("descripcion_prpre").setFiltroContenido();
  		set_pbuscar.setRadio();
		set_pbuscar.getBot_aceptar().setMetodo("filtrarProceso");
		agregarComponente(set_pbuscar);
		
		eti_proceso.setId("eti_proceso");
		eti_proceso.setStyle("display: block;font-weight: bold;text-align:justify;");
		eti_proceso.setValue("");

		contruirMenu();

		pan_opcion.setId("pan_opcion");
		pan_opcion.setTransient(true);

		efecto.setType("drop");
		efecto.setSpeed(150);
		efecto.setPropiedad("mode", "'show'");
		efecto.setEvent("load");
		pan_opcion.getChildren().add(efecto);
		
		Grid gri_lateral = new Grid();
		gri_lateral.setWidth("100%");
		gri_lateral.getChildren().add(pam_menu);
		gri_lateral.getChildren().add(new Etiqueta(" "));
		gri_lateral.getChildren().add(new Etiqueta("PROCESO:"));
		gri_lateral.getChildren().add(new Etiqueta(" "));
		gri_lateral.getChildren().add(eti_proceso);

		dibujarDatosProceso();

		div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(gri_lateral, pan_opcion, "20%", "V");
		div_division.getDivision1().setCollapsible(true);
		div_division.getDivision1().setHeader("MENU DE OPCIONES");
		agregarComponente(div_division);		

	}
	
	/**
	 * forma el menu de la parte derecha con las opciones de la ficha del proceso
	 */
	private void contruirMenu() {
		pam_menu.setWidgetVar("100%");

		// SUB MENU 1
		Submenu sum_proceso = new Submenu();
		sum_proceso.setLabel("PROCESO");
		pam_menu.getChildren().add(sum_proceso);

		// ITEM 1 : OPCION 0
		ItemMenu itm_datos_empl = new ItemMenu();
		itm_datos_empl.setValue("DATOS DEL PROCESO");
		itm_datos_empl.setIcon("ui-icon-person");
		itm_datos_empl.setMetodo("dibujarDatosProceso");
		itm_datos_empl.setUpdate("pan_opcion");
		sum_proceso.getChildren().add(itm_datos_empl);
		
		// ITEM : OPCION 
		ItemMenu itm_archivos_p = new ItemMenu();
		itm_archivos_p.setValue("ARCHIVOS DEL PROCESO");
		itm_archivos_p.setIcon("ui-icon-person");
		itm_archivos_p.setMetodo("dibujarArchivosProceso");
		itm_archivos_p.setUpdate("pan_opcion");
		sum_proceso.getChildren().add(itm_archivos_p);

		// SUB MENU 
		Submenu sum_datos_preparatoria = new Submenu();
		sum_datos_preparatoria.setLabel("FASE PREPARATORIA");
		pam_menu.getChildren().add(sum_datos_preparatoria);
		
		ItemMenu itm_archivos_preparatoria = new ItemMenu();
		itm_archivos_preparatoria.setValue("ARCHIVOS PREPARATORIA");
		itm_archivos_preparatoria.setIcon("ui-icon-person");
		itm_archivos_preparatoria.setMetodo("dibujarArchivosPreparatoria");
		itm_archivos_preparatoria.setUpdate("pan_opcion");
		sum_datos_preparatoria.getChildren().add(itm_archivos_preparatoria);
		
		// SUB MENU 
		Submenu sum_datos_precontractual = new Submenu();
		sum_datos_precontractual.setLabel("FASE PRECONTRACTUAL");
		pam_menu.getChildren().add(sum_datos_precontractual);

		ItemMenu itm_archivos_precontractual = new ItemMenu();
		itm_archivos_precontractual.setValue("ARCHIVOS PRECONTRACTUAL");
		itm_archivos_precontractual.setIcon("ui-icon-person");
		itm_archivos_precontractual.setMetodo("dibujarArchivosPrecontractual");
		itm_archivos_precontractual.setUpdate("pan_opcion");
		sum_datos_precontractual.getChildren().add(itm_archivos_precontractual);
		
		// SUB MENU
		Submenu sum_datos_contractual = new Submenu();
		sum_datos_contractual.setLabel("FASE CONTRACTUAL");
		pam_menu.getChildren().add(sum_datos_contractual);
		
		// SUB MENU
		Submenu sum_datos_ejecucion = new Submenu();
		sum_datos_ejecucion.setLabel("EJECUCIÓN");
		pam_menu.getChildren().add(sum_datos_ejecucion);


		// SUB MENU
		Submenu sum_seguimiento = new Submenu();
		sum_seguimiento.setLabel("SEGUIMIENTO");
		pam_menu.getChildren().add(sum_seguimiento);
		// ITEM : OPCION 
		ItemMenu itm_datos_seguimiento = new ItemMenu();
		itm_datos_seguimiento.setValue("SEGUIMIENTO");
		itm_datos_seguimiento.setIcon("ui-icon-person");
		itm_datos_seguimiento.setMetodo("dibujarDatosSeguimiento");
		itm_datos_seguimiento.setUpdate("pan_opcion");
		sum_seguimiento.getChildren().add(itm_datos_seguimiento);

	}
	
	
	public void filtrarProceso() 
	{
		if(set_pbuscar.isVisible())
		{
			ide_prpre = set_pbuscar.getValorSeleccionado();		
			set_pbuscar.cerrar();
			
			TablaGenerica tab_pprocesos=utilitario.consultar("select ide_prpre, descripcion_prpre from precon_precontractual where ide_prpre="+ide_prpre);
			if(tab_pprocesos.getTotalFilas()<=0)
			{
				utilitario.agregarMensajeInfo("NO SE ENCONTRO EL PROCESO", "");
				return;
			}
			
			eti_proceso.setValue(pckUtilidades.CConversion.CStr(tab_pprocesos.getValor( "descripcion_prpre")));
			
			dibujarDatosProceso();
			dibujarArchivosProceso();
			utilitario.addUpdate("pan_opcion,eti_proceso");
		}
		else
		{
			set_pbuscar.getTab_seleccion().ejecutarSql();
			set_pbuscar.dibujar();
		}
	}
	
	private void limpiarPanel(){		
		pan_opcion.getChildren().clear();
		pan_opcion.getChildren().add(efecto);
	}
	
	public void limpiar() {

		eti_proceso.setValue("");
		tab_precontractual.limpiar();
		tab_documento_requisito.limpiar();		
		int_opcion=0;
		utilitario.addUpdate("eti_proceso");// limpia y refresca el autocompletar
	}
	
	public void dibujarDatosProceso() 
	{		
		int_opcion=0;
		limpiarPanel();
		tab_precontractual = new Tabla();
		
		tab_precontractual.setId("tab_precontractual");
		tab_precontractual.setTabla("precon_precontractual","ide_prpre",1);
		tab_precontractual.setCondicion("ide_prpre=" + ide_prpre);
		tab_precontractual.setTipoFormulario(true);
		tab_precontractual.getGrid().setColumns(4);
		tab_precontractual.setMostrarNumeroRegistros(false);
		tab_precontractual.setValidarInsertar(true);
		
		tab_precontractual.getColumna("ide_prpre").setNombreVisual("Código");
		tab_precontractual.getColumna("ide_prpre").setOrden(1);
		tab_precontractual.getColumna("codigo_prpre").setNombreVisual("Número de Registro");
		tab_precontractual.getColumna("codigo_prpre").setOrden(2);
		tab_precontractual.getColumna("codigo_prpre").setLectura(false);
		tab_precontractual.getColumna("codigo_prpre").setEstilo("width:200px");
		tab_precontractual.getColumna("fecha_prpre").setNombreVisual("Fecha de Registro");
		tab_precontractual.getColumna("fecha_prpre").setOrden(3);
		tab_precontractual.getColumna("fecha_prpre").setLectura(false);
		tab_precontractual.getColumna("hora_prpre").setVisible(false);
		tab_precontractual.getColumna("ide_pretp").setCombo(ser_EtapaProcedimiento.getProcedimientos());
		tab_precontractual.getColumna("ide_pretp").setNombreVisual("Procedimiento");
		tab_precontractual.getColumna("ide_pretp").setLectura(false);
		tab_precontractual.getColumna("ide_pretp").setOrden(4);
		tab_precontractual.getColumna("ide_pretp").setEstilo("width:300px");
		tab_precontractual.getColumna("descripcion_prpre").setNombreVisual("Descripción");
		tab_precontractual.getColumna("descripcion_prpre").setOrden(5);
		tab_precontractual.getColumna("descripcion_prpre").setRequerida(true);		
	    tab_precontractual.getColumna("estado_proceso_prpre").setCombo(utilitario.getListaEstadoProceso());
	    tab_precontractual.getColumna("estado_proceso_prpre").setNombreVisual("Estado");
	    tab_precontractual.getColumna("estado_proceso_prpre").setOrden(6);
	    tab_precontractual.getColumna("estado_proceso_prpre").setLectura(false);
		tab_precontractual.getColumna("monto_prpre").setNombreVisual("Monto");
		tab_precontractual.getColumna("monto_prpre").setOrden(7);
		tab_precontractual.getColumna("monto_prpre").setLectura(false);
		tab_precontractual.getColumna("ide_actual_preta").setVisible(false);
		tab_precontractual.getColumna("actividad_actual_prpre").setNombreVisual("Actividad Actual");
		tab_precontractual.getColumna("actividad_actual_prpre").setOrden(8);
		tab_precontractual.getColumna("actividad_actual_prpre").setLectura(false);
		tab_precontractual.getColumna("actividad_actual_prpre").setEstilo("width:300px");
		tab_precontractual.getColumna("ide_actual_geedp").setVisible(false);
		tab_precontractual.getColumna("responsable_actual_prpre");
		tab_precontractual.getColumna("responsable_actual_prpre").setNombreVisual("Responsable Actual");
		tab_precontractual.getColumna("responsable_actual_prpre").setOrden(9);
		tab_precontractual.getColumna("responsable_actual_prpre").setLectura(false);
		tab_precontractual.getColumna("departamento_actual_prpre").setNombreVisual("Departamento Actual");
		tab_precontractual.getColumna("departamento_actual_prpre").setOrden(10);
		tab_precontractual.getColumna("departamento_actual_prpre").setLectura(false);
		tab_precontractual.getColumna("departamento_actual_prpre").setEstilo("width:150px");
		tab_precontractual.getColumna("ide_preta").setCombo("precon_etapa", "ide_preta","descripcion_preta", "activo_preta=true");
		tab_precontractual.getColumna("ide_preta").setNombreVisual("Actividad a Enviar");
		tab_precontractual.getColumna("ide_preta").setRequerida(false);
		tab_precontractual.getColumna("ide_preta").setLectura(false);
		tab_precontractual.getColumna("ide_preta").setOrden(11);
		tab_precontractual.getColumna("ide_preta").setEstilo("width:300px");
		tab_precontractual.getColumna("observacion_prpre").setNombreVisual("Observación");
		tab_precontractual.getColumna("observacion_prpre").setOrden(12);
		tab_precontractual.getColumna("ide_tepro").setCombo(ser_generalAdm.getProveedores());
		tab_precontractual.getColumna("ide_tepro").setAutoCompletar();
		tab_precontractual.getColumna("ide_tepro").setOrden(13);
		tab_precontractual.getColumna("ide_tepro").setNombreVisual("Proveedor");
	    tab_precontractual.getColumna("aprueba_informetm_prpre").setCombo(utilitario.getListaSiNo());
	    tab_precontractual.getColumna("aprueba_informetm_prpre").setNombreVisual("¿Aprueba Informe Técnico Motivado?");
	    tab_precontractual.getColumna("aprueba_informetm_prpre").setOrden(14);
	    tab_precontractual.getColumna("aprueba_informetm_prpre").setLectura(false);  
	    tab_precontractual.getColumna("termino_especificacion_prpre").setCombo(utilitario.getListaTipoProcesoContracion(false));
	    tab_precontractual.getColumna("termino_especificacion_prpre").setNombreVisual("¿Defina si es un Bien o Servicio?");
	    tab_precontractual.getColumna("termino_especificacion_prpre").setOrden(15);
	    tab_precontractual.getColumna("termino_especificacion_prpre").setLectura(false);
	    tab_precontractual.getColumna("se_encuentra_catalogoe_prpre").setCombo(utilitario.getListaSiNo());
	    tab_precontractual.getColumna("se_encuentra_catalogoe_prpre").setNombreVisual("¿Bien o Servicio se encuentra en Catálogo Electrónico?");
	    tab_precontractual.getColumna("se_encuentra_catalogoe_prpre").setOrden(16);
	    tab_precontractual.getColumna("se_encuentra_catalogoe_prpre").setLectura(false);
	    tab_precontractual.getColumna("aprueba_proyecto_prpre").setCombo(utilitario.getListaSiNo());
	    tab_precontractual.getColumna("aprueba_proyecto_prpre").setNombreVisual("¿Aprueba?");
	    tab_precontractual.getColumna("aprueba_proyecto_prpre").setOrden(17);
	    tab_precontractual.getColumna("aprueba_proyecto_prpre").setLectura(false);
	    tab_precontractual.getColumna("consta_poa_prpre").setCombo(utilitario.getListaSiNo());
	    tab_precontractual.getColumna("consta_poa_prpre").setNombreVisual("¿Consta en POA?");
	    tab_precontractual.getColumna("consta_poa_prpre").setOrden(18);
	    tab_precontractual.getColumna("consta_poa_prpre").setLectura(false);
	    tab_precontractual.getColumna("consta_pac_prpre").setCombo(utilitario.getListaSiNo());
	    tab_precontractual.getColumna("consta_pac_prpre").setNombreVisual("¿Consta en PAC?");
	    tab_precontractual.getColumna("consta_pac_prpre").setOrden(19);
	    tab_precontractual.getColumna("consta_pac_prpre").setLectura(false);
	    tab_precontractual.getColumna("es_favorable_informej_prpre").setCombo(utilitario.getListaSiNo());
	    tab_precontractual.getColumna("es_favorable_informej_prpre").setNombreVisual("¿Es Favorable?");
	    tab_precontractual.getColumna("es_favorable_informej_prpre").setOrden(20);
	    tab_precontractual.getColumna("es_favorable_informej_prpre").setLectura(false);
		tab_precontractual.getColumna("activo_prpre").setNombreVisual("ACTIVO");
		tab_precontractual.getColumna("activo_prpre").setValorDefecto("true");
		tab_precontractual.getColumna("activo_prpre").setLectura(false);
		tab_precontractual.getColumna("activo_prpre").setVisible(false);
		tab_precontractual.getColumna("activo_prpre").setOrden(21);
		tab_precontractual.getColumna("ide_usuario_iniciop_prpre");
		tab_precontractual.getColumna("ide_usuario_iniciop_prpre").setVisible(false);
		tab_precontractual.getColumna("es_proceso_infima_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("es_proceso_infima_prpre").setNombreVisual("¿Es Proceso de Ínfimas?");
		tab_precontractual.getColumna("es_proceso_infima_prpre").setLectura(false);
		tab_precontractual.getColumna("solicitar_alcance_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("solicitar_alcance_prpre").setNombreVisual("¿Se necesita solicitar alcance?");
		tab_precontractual.getColumna("solicitar_alcance_prpre").setLectura(false);
		tab_precontractual.getColumna("esta_ok_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("esta_ok_prpre").setNombreVisual("¿Esta OK?");
		tab_precontractual.getColumna("esta_ok_prpre").setLectura(false);
		tab_precontractual.getColumna("comision_tecnica_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("comision_tecnica_prpre").setNombreVisual("¿Requiere Comisión Técnica?");
		tab_precontractual.getColumna("comision_tecnica_prpre").setLectura(false);
		tab_precontractual.getColumna("es_viable_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("es_viable_prpre").setNombreVisual("¿Viable?");
		tab_precontractual.getColumna("es_viable_prpre").setLectura(false);
		tab_precontractual.getColumna("error_forma_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("error_forma_prpre").setNombreVisual("¿Existe Errores de Forma?");
		tab_precontractual.getColumna("error_forma_prpre").setLectura(false);
		tab_precontractual.getColumna("convalidacion_satisfac_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("convalidacion_satisfac_prpre").setNombreVisual("¿Convalidación Satisfactoria?");
		tab_precontractual.getColumna("convalidacion_satisfac_prpre").setLectura(false);
		tab_precontractual.getColumna("existe_mas_oferta_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("existe_mas_oferta_prpre").setNombreVisual("¿Existe más de una oferta?");
		tab_precontractual.getColumna("existe_mas_oferta_prpre").setLectura(false);
		tab_precontractual.getColumna("recomienda_adjudicar_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("recomienda_adjudicar_prpre").setNombreVisual("¿Recomienda adjudicar?");
		tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(false);
		tab_precontractual.getColumna("necesario_reaperturar_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("necesario_reaperturar_prpre").setNombreVisual("¿Es necesario reaperturar?");
		tab_precontractual.getColumna("necesario_reaperturar_prpre").setLectura(false);
		tab_precontractual.getColumna("se_recibe_oferta_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("se_recibe_oferta_prpre").setNombreVisual("¿Se recibe la oferta?");
		tab_precontractual.getColumna("se_recibe_oferta_prpre").setLectura(false);
		tab_precontractual.getColumna("convalida_recadjudicar_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("convalida_recadjudicar_prpre").setNombreVisual("¿Convalidación Satisfactoria y recomienda adjudicar?");
		tab_precontractual.getColumna("convalida_recadjudicar_prpre").setLectura(false);
		List lista_monto = new ArrayList();
		    Object fila_monto[] = {
		           "SI", "150.000 - SI"
		    };
		    Object fila_monto1[] = {
		         "NO", "150.000 - NO"
		    };
		    lista_monto.add(fila_monto);
		    lista_monto.add(fila_monto1);
		tab_precontractual.getColumna("es_superior_prpre").setCombo(lista_monto);
		tab_precontractual.getColumna("es_superior_prpre").setNombreVisual("¿Es Superior a?");
		tab_precontractual.getColumna("es_superior_prpre").setLectura(false);
		tab_precontractual.getColumna("manifestacion_interes_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("manifestacion_interes_prpre").setNombreVisual("¿Existen manifestaciones de interés?");
		tab_precontractual.getColumna("manifestacion_interes_prpre").setLectura(false);
		tab_precontractual.getColumna("cumple_prpre").setCombo(utilitario.getListaSiNo());
		tab_precontractual.getColumna("cumple_prpre").setNombreVisual("¿Cumple?");
		tab_precontractual.getColumna("cumple_prpre").setLectura(false);
		
		tab_precontractual.dibujar();
		
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setPanelTabla(tab_precontractual);
		pan_opcion.setTitle("DATOS DEL PROCESO");
		pan_opcion.getChildren().add(pat_panel1);
		
	}
	
	public void dibujarArchivosProceso(){
		dibujarArchivos(0);
	}
	
	public void dibujarArchivosPreparatoria(){
		dibujarArchivos(1);
	}
	
	public void dibujarArchivosPrecontractual(){
		dibujarArchivos(2);
	}
	
	
	public void dibujarArchivos(int opcion) {
		if (pckUtilidades.CConversion.CInt(ide_prpre)>0) {
			
			limpiarPanel();			
			tab_documento_requisito = new Tabla();
			
			// detalle documento requisito
			tab_documento_requisito.setId("tab_documento_requisito");
			tab_documento_requisito.setTabla("precon_documento_requisito","ide_prdoc", 2);
			tab_documento_requisito.setCondicion("presenta_prdoc like 'SI' and ide_prpre=" + ide_prpre);
			
			tab_documento_requisito.getColumna("ide_prpre").setVisible(false);
			tab_documento_requisito.getColumna("ide_prdoc").setNombreVisual("Código");
			tab_documento_requisito.getColumna("ide_prreq").setCombo(ser_Requisito.getRequisitoActividad(opcion));
			tab_documento_requisito.getColumna("ide_prreq").setLongitud(350);
			tab_documento_requisito.getColumna("ide_prreq").setNombreVisual("Tipo Documento/Requisito");	
			tab_documento_requisito.getColumna("ide_prreq").setLectura(true);
			tab_documento_requisito.getColumna("fecha_presenta_prdoc").setLongitud(20);
			tab_documento_requisito.getColumna("fecha_presenta_prdoc").setNombreVisual("Fecha Documento");	
			tab_documento_requisito.getColumna("fecha_presenta_prdoc").setLectura(true);
		    tab_documento_requisito.getColumna("presenta_prdoc").setLongitud_control(20);
		    tab_documento_requisito.getColumna("presenta_prdoc").setCombo(utilitario.getListaSiNo());
			tab_documento_requisito.getColumna("presenta_prdoc").setNombreVisual("Subió el Archivo?");	
			tab_documento_requisito.getColumna("presenta_prdoc").setLectura(true);
			tab_documento_requisito.getColumna("documentoadjunto_prdoc").setNombreVisual("Anexo Principal");
			tab_documento_requisito.getColumna("documentoadjunto_prdoc").setUpload("contrataciones");
			tab_documento_requisito.getColumna("documentoadjunto_prdoc").setLectura(true);
			tab_documento_requisito.getColumna("descripcion_prdoc").setNombreVisual("Nombre Documento");
			tab_documento_requisito.getColumna("descripcion_prdoc").setComentario("Se registra el link donde se subio el archivo del portal de la SERCOP.");
			tab_documento_requisito.getColumna("descripcion_prdoc").setLectura(true);
			tab_documento_requisito.getColumna("etapa_prdoc");
			tab_documento_requisito.getColumna("etapa_prdoc").setNombreVisual("Archivo/Actividad");	
			tab_documento_requisito.getColumna("etapa_prdoc").setLectura(true);
			tab_documento_requisito.getColumna("activo_prdoc").setNombreVisual("Activo");
			tab_documento_requisito.getColumna("activo_prdoc").setValorDefecto("true");
			tab_documento_requisito.getColumna("activo_prdoc").setLectura(true);
			
			tab_documento_requisito.getColumna("nro_documento_prdoc").setLectura(true);
			//tab_documento_requisito.getColumna("asunto_prdoc").setLectura(true);
			tab_documento_requisito.getColumna("de_nombre_prdoc").setLectura(true);
			tab_documento_requisito.getColumna("para_nombre_prdoc").setLectura(true);
			
			tab_documento_requisito.getColumna("original_prdoc").setLectura(true);
			tab_documento_requisito.getColumna("nro_fojas_prdoc").setLectura(true);
			//tab_documento_requisito.getColumna("detalle_anexos_prdoc").setLectura(true);
			tab_documento_requisito.getColumna("electronico_prdoc").setLectura(true);
			tab_documento_requisito.getColumna("con_ide_prdoc").setVisible(false);
			
			
			if(opcion>0)
			{
				tab_documento_requisito.setTipoFormulario(true);  //formulario 
				tab_documento_requisito.getGrid().setColumns(2); //hacer  columnas
				//tab_documento_requisito.getColumna("ide_prreq").setLectura(true);
				//tab_documento_requisito.getColumna("fecha_presenta_prdoc").setLectura(true);
				//tab_documento_requisito.getColumna("presenta_prdoc").setLectura(true);
				//tab_documento_requisito.getColumna("etapa_prdoc").setLectura(true);
				//tab_documento_requisito.getColumna("activo_prdoc").setLectura(true);				
				tab_documento_requisito.getColumna("documentoadjunto_prdoc").setLectura(false);
				tab_documento_requisito.setCondicion("ide_prpre=" + ide_prpre +" and ide_prreq in ( select ide_prreq from ("+ser_Requisito.getRequisitoActividad(opcion)+") a ) ");
				tab_documento_requisito.dibujar();
			}
			else
			{
				//tab_documento_requisito.setLectura(false);
				tab_documento_requisito.dibujar();
				//tab_documento_requisito.setLectura(false);
			}
			
			//tab_documento_requisito.imprimirSql();
			
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_documento_requisito);
			pan_opcion.setTitle("ARCHIVOS");
			pan_opcion.getChildren().add(pat_panel1);
			
			int_opcion = opcion > 0 ? opcion : 1;

		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Proceso...");
			limpiar();
		}
	}
	
	public void dibujarDatosSeguimiento() {
		if (pckUtilidades.CConversion.CInt(ide_prpre)>0) {
			int_opcion=-1;
			limpiarPanel();			
			tab_seguimiento = new Tabla();
			
			tab_seguimiento.setId("tab_seguimiento");
			tab_seguimiento.setIdCompleto("tab_tabulador:tab_seguimiento");
			tab_seguimiento.setTabla("precon_seguimiento","ide_prseg", 3);
			tab_seguimiento.setSql(ser_Seguimiento.getSeguimientoPorPrecontractual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre"))));
			tab_seguimiento.getColumna("ide_prseg").setLongitud(10);
			tab_seguimiento.getColumna("ide_prseg").setNombreVisual("CÓDIGO");
			tab_seguimiento.getColumna("fecha_asignacion_prseg").setLongitud(40);
			tab_seguimiento.getColumna("fecha_asignacion_prseg").setNombreVisual("FECHA ASIGNACIÓN");
			tab_seguimiento.getColumna("hora_asignacion_prseg").setLongitud(40);
			tab_seguimiento.getColumna("hora_asignacion_prseg").setNombreVisual("HORA ASIGNACIÓN");
			tab_seguimiento.getColumna("fecha_cambio_prseg").setLongitud(40);
			tab_seguimiento.getColumna("fecha_cambio_prseg").setNombreVisual("FECHA CAMBIO");
			tab_seguimiento.getColumna("hora_cambio_prseg").setLongitud(40);
			tab_seguimiento.getColumna("hora_cambio_prseg").setNombreVisual("HORA CAMBIO");
			tab_seguimiento.getColumna("etapa_inicio_prseg").setNombreVisual("ACTIVIDAD INICIO");
			tab_seguimiento.getColumna("responsable_prseg").setNombreVisual("RESPONSABLE");
			tab_seguimiento.getColumna("departamento_prseg").setNombreVisual("DEPARTAMENTO");
			tab_seguimiento.getColumna("etapa_fin_prseg").setNombreVisual("ACTIVIDAD FIN");
			tab_seguimiento.getColumna("responsable_asignado_prseg").setNombreVisual("RESPONSABLE ASIGNADO");
			tab_seguimiento.getColumna("departamento_asignado_prseg").setNombreVisual("DEPARTAMENTO");
			tab_seguimiento.getColumna("observacion_prseg").setNombreVisual("OBSERVACIÓN");
			tab_seguimiento.getColumna("estado_actividad_prseg").setNombreVisual("ESTADO ACTIVIDAD");
			tab_seguimiento.getColumna("estado_procedimiento_prseg").setNombreVisual("ESTADO PROCEDIMIENTO");	
			tab_seguimiento.getColumna("activo_prseg").setLongitud(40);
			tab_seguimiento.getColumna("activo_prseg").setNombreVisual("ACTIVO");
			tab_seguimiento.getColumna("ide_preta_prseg").setVisible(false);
			tab_seguimiento.setLectura(true);
			tab_seguimiento.dibujar();
			tab_seguimiento.setLectura(false);
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_seguimiento);
			pan_opcion.setTitle("SEGUIMIENTO");
			pan_opcion.getChildren().add(pat_panel1);

		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Proceso...");
			limpiar();
		}
	}
	
	public void generarArchivo(){	
		try {
			
			if(tab_precontractual.getTotalFilas()<=0)
			{
				utilitario.agregarMensajeInfo("Selecione un Proceso", "");
				return;
			}
			
			if(tab_documento_requisito.getTotalFilas()<=0)
			{
				utilitario.agregarMensajeInfo("SIN ARCHIVOS DEL PROCESO", "");
				return;
			}
			
			String fileName = pckUtilidades.CConversion.CStr(tab_precontractual.getValor("descripcion_prpre"));
			fileName=pckUtilidades.Utilitario.quitarCaracteresSpeciales(fileName);
			List<File> anexosPrincipales=new ArrayList();
			int item=1;
			for(int i=0;i<tab_documento_requisito.getTotalFilas();i++){
				
				if(pckUtilidades.CConversion.CStr(tab_documento_requisito.getValor(i, "documentoadjunto_prdoc")).length()>4)
				{
					String ruta=utilitario.getPropiedad("rutaDownload")+(tab_documento_requisito.getValor(i, "documentoadjunto_prdoc"));	
					//String nombre=tab_documento_requisito.getValor(i, "etapa_prdoc");s 
					String nombre="";
					if(pckUtilidades.CConversion.CInt(tab_documento_requisito.getValor(i, "con_ide_prdoc"))>0)	
						nombre=pckUtilidades.CConversion.CStr(tab_documento_requisito.getValor(i, "nro_documento_prdoc")) + " - "+pckUtilidades.CConversion.CStr(tab_documento_requisito.getValor(i, "descripcion_prdoc"));
					else
						nombre=pckUtilidades.CConversion.CStr(tab_documento_requisito.getValor(i, "nro_documento_prdoc")) + " - "+pckUtilidades.CConversion.CStr(tab_documento_requisito.getValorArreglo(i, "ide_prreq", 1));
					
					if(nombre.length()<4)
					{
						nombre="SinNombre"+i;
					}
					
					try{
						File anexoFil=utilitario.descargarArchivo(ruta, item+"_" +pckUtilidades.Utilitario.quitarCaracteresSpeciales(nombre), ruta.substring(ruta.length()-5, ruta.length()));
						if(anexoFil!=null)
							anexosPrincipales.add(anexoFil);
						}catch(Exception e){}
					item++;
				}
			}

			utilitario.crearArchivoZIP(anexosPrincipales, fileName.concat(".zip"));		
			
			
		} catch (Exception e) {
			// TODO: handle exception
			utilitario.crearError("PROCESOS", "generarArchivo", e);
		}
	}
	
	public void exportarExcel(){
		if(tab_precontractual.getTotalFilas()<=0)
		{
			utilitario.agregarMensajeInfo("Selecione un Proceso", "");
			return;
		}
		
		if(tab_documento_requisito.getTotalFilas()<=0)
		{
			utilitario.agregarMensajeInfo("SIN ARCHIVOS DEL PROCESO", "");
			return;
		}

		ser_fui_xls.exportarFUI(tab_precontractual.getValor("ide_prpre"), tab_precontractual.getValorArreglo(tab_precontractual.getFilaActual(), "ide_pretp", 1), 
								tab_precontractual.getValor("descripcion_prpre"),
								tab_precontractual.getValor("responsable_actual_prpre"),
								tab_precontractual.getValor("departamento_actual_prpre"),
								true);
		System.out.println("FUI Exportado exitosamente...");
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		//utilitario.getTablaisFocus().insertar();
		/*switch (int_opcion)
		{
			case 0:
				tab_precontractual.insertar();
				break;
			case 1:
				tab_documento_requisito.insertar();
				break;
			default:
				tab_precontractual.insertar();
				break;
		}*/
		
		if(int_opcion>0)
			tab_documento_requisito.insertar();
		else
			tab_precontractual.insertar();
	}
	
	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		{
			List perfilUsuarioConectado=utilitario.getConexion().consultar(ser_generalAdm.getPerfilConectado(utilitario.getVariable("ide_usua")));
			boolean finalizado=false;
			if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==8 || pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==9){
				if(tab_precontractual.getValor("se_encuentra_catalogoe_prpre")!=null && tab_precontractual.getValor("aprueba_informetm_prpre")==null){
					utilitario.agregarMensajeInfo("El proceso no puede guardar ya tiene que unirse, es actividad paralela.", "");
				}
			}
			if(tab_precontractual.getValor("estado_proceso_prpre").equals("REGISTRADO") || tab_precontractual.getValor("estado_proceso_prpre").equals("EN PROCESO") 
					|| tab_precontractual.getValor("estado_proceso_prpre").equals("ENVIADO")){
				finalizado=true;
			}
			if(!finalizado){
				utilitario.agregarMensajeInfo("El proceso ya ha finalizado, no se puede modificar.", "");
			}else{
				if(tab_precontractual.guardar()){
					/*if(tab_precontractual.isFilaInsertada()){
						ser_precontractual.guardaSecuencial(ser_precontractual.numeroSecuencial(p_sec_precontractual), p_sec_precontractual);
					}*/
					if(tab_documento_requisito.isFocus()){
						if(perfilUsuarioConectado.get(0).toString().equals("ADMINISTRADOR DE LA APLICACION PRECONTRACTUAL")){
							if(tab_documento_requisito.getValor("documentoadjunto_prdoc")==null && tab_documento_requisito.getValor("descripcion_prdoc")==null){
								tab_documento_requisito.setValor("presenta_prdoc","NO");
							}else{
								tab_documento_requisito.setValor("presenta_prdoc","SI");
							}
						}else{
							if(tab_documento_requisito.getValor("documentoadjunto_prdoc")==null){
								tab_documento_requisito.setValor("presenta_prdoc","NO");
							}else{
								tab_documento_requisito.setValor("presenta_prdoc","SI");
							}
						}
						tab_documento_requisito.guardar();
					}
				}
				guardarPantalla();
			}
		}
	}
	
	
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_precontractual() {
		return tab_precontractual;
	}

	public void setTab_precontractual(Tabla tab_precontractual) {
		this.tab_precontractual = tab_precontractual;
	}

	public Tabla getTab_documento_requisito() {
		return tab_documento_requisito;
	}

	public void setTab_documento_requisito(Tabla tab_documento_requisito) {
		this.tab_documento_requisito = tab_documento_requisito;
	}

	public SeleccionTabla getSet_pbuscar() {
		return set_pbuscar;
	}

	public void setSet_pbuscar(SeleccionTabla set_pbuscar) {
		this.set_pbuscar = set_pbuscar;
	}

	public PanelMenu getPam_menu() {
		return pam_menu;
	}

	public void setPam_menu(PanelMenu pam_menu) {
		this.pam_menu = pam_menu;
	}

	public Panel getPan_opcion() {
		return pan_opcion;
	}

	public void setPan_opcion(Panel pan_opcion) {
		this.pan_opcion = pan_opcion;
	}

	public Efecto getEfecto() {
		return efecto;
	}

	public void setEfecto(Efecto efecto) {
		this.efecto = efecto;
	}

	public Tabla getTab_seguimiento() {
		return tab_seguimiento;
	}

	public void setTab_seguimiento(Tabla tab_seguimiento) {
		this.tab_seguimiento = tab_seguimiento;
	}

	public Etiqueta getEti_proceso() {
		return eti_proceso;
	}

	public void setEti_proceso(Etiqueta eti_proceso) {
		this.eti_proceso = eti_proceso;
	}
	
	
	

}
