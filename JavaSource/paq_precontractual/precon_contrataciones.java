package paq_precontractual;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.component.password.Password;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.OutputLink;
import framework.componentes.Panel;
import framework.componentes.PanelAcordion;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import framework.componentes.TextoEditor;
import framework.componentes.Upload;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
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
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidades.EnvioMail;

public class precon_contrataciones extends Pantalla{
	
	@EJB
	private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioEtapaProcedimiento ser_EtapaProcedimiento = (ServicioEtapaProcedimiento) utilitario.instanciarEJB(ServicioEtapaProcedimiento.class);
	@EJB
	private ServicioRequisito ser_Requisito = (ServicioRequisito) utilitario.instanciarEJB(ServicioRequisito.class);
	@EJB
	private ServicioSeguimiento ser_Seguimiento = (ServicioSeguimiento) utilitario.instanciarEJB(ServicioSeguimiento.class);
	@EJB
	private ServicioGeneralAdmPrecon ser_generalAdm = (ServicioGeneralAdmPrecon ) utilitario.instanciarEJB(ServicioGeneralAdmPrecon.class);
	@EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
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
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
    private ServicioFUIxls ser_fui_xls = (ServicioFUIxls) utilitario.instanciarEJB(ServicioFUIxls.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	
	private Tabla tab_precontractual=new Tabla();
	private Tabla tab_documento_requisito=new Tabla();
	private Tabla tab_documento_requisito_secundario=new Tabla();
	private Tabla tab_seguimiento=new Tabla();
	private Tabla tab_partida_poa=new Tabla();
	private Tabla tab_pre_pac=new Tabla();
	private Tabla tab_responsable_prec=new Tabla();
	
	private SeleccionTabla set_pac = new SeleccionTabla();
	private SeleccionTabla set_lineas_poa=new SeleccionTabla();
	private SeleccionTabla set_empleado=new SeleccionTabla();
	
	private Check che_firma = new Check();
	private Radio rad_si_no = new Radio();
	private Dialogo dia_aprobar = new Dialogo();
	private Dialogo dia_solicitud = new Dialogo();
	private Dialogo dia_firma_electronica = new Dialogo();
	private Dialogo dia_anexo = new Dialogo();
	private Grid gri_aprobar = new Grid();
	
	private Upload upl_archivoFirma=new Upload();
	private Confirmar con_completar=new Confirmar();
	private Upload upl_anexo=new Upload();
	
	private Combo com_anio=new Combo();
	Combo com_actividadesR=new Combo();
	Etiqueta eti_descripcion = new Etiqueta();
	Etiqueta eti_notas = new Etiqueta();
	Etiqueta eti_mensaje = new Etiqueta();
	OutputLink bot_plantilla=new OutputLink();
	OutputLink bot_manuales=new OutputLink();
	AreaTexto are_txt_notas = new AreaTexto();
	TextoEditor are_txt_edit = new TextoEditor();
	private Password pas_clave_firma = new Password();
	
	public static String p_sec_precontractual;
	public static String p_precon_automatico;
	public static String par_sec_precon_sol;
	public static String par_precon_tecnico;
	
	public boolean cumpleRequisitos;
	public boolean cumpleAprobacion;
	public boolean enviarMailFirmado=false;
	public boolean requiereAprobacion=false;
	public boolean actividadOpcional=false;
	public String tipoContratacion="NA";
	
	private PanelMenu pam_menu = new PanelMenu();
	private Panel pan_opcion = new Panel();
	private int int_opcion = 0;// sirve para identificar la opcion que se encuentra dibujada en pantalla
	private Efecto efecto = new Efecto();
	private Division div_division = new Division();
	String priv_ide_prpre="-1";
	String carpeta="contrataciones";
	private File fil_pdf;
	private String archivo="";
	private String empleado;
	private static boolean tecnico=false;
	List perfilUsuarioConectado;
	
	public precon_contrataciones(){
		
		//bar_botones.limpiar();
		
		/*if(!pckUtilidades.Utilitario.obtenerIPhost().contains("SRV103ERP02"))
		{
			utilitario.agregarNotificacionInfo("MENSAJE - AUTORIZACION DE MODULO","Esta pantalla no esta autorizada para usarse en el servidor actual (IP:"+pckUtilidades.Utilitario.obtenerIPhost()+"), favor use el servidor ERP desde la pagina servicios emgirs");
			utilitario.agregarMensajeInfo("Permisos", "Contacte con el adminsitrador... o use el servidor ERP...");
			return;
		}*/
		
		empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		System.out.println("empleado: "+empleado);
		
		bar_botones.getBot_insertar().setRendered(false);
		bar_botones.getBot_eliminar().setRendered(false);
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		p_sec_precontractual = utilitario.getVariable("p_sec_precontractual");
		p_precon_automatico = utilitario.getVariable("p_precon_automatico");
		par_sec_precon_sol = utilitario.getVariable("p_sec_precon_sol");
		par_precon_tecnico = utilitario.getVariable("p_sec_precon_tecnico");
		
		if(pckUtilidades.CConversion.CInt(p_precon_automatico)==0)
			utilitario.agregarNotificacionInfo("MENSAJE - DE MODULO","El sistema esta parametrizado para ser utilizado de manera manual (Modo Carga de Documentacion).");
		
		Boton bot_nuevo = new Boton();
		bot_nuevo.setValue("1) Nuevo Proceso");
		bot_nuevo.setMetodo("insertarProceso");
		bot_nuevo.setIcon("ui-icon-document");
		bar_botones.agregarBoton(bot_nuevo);
		
		Boton bot_cargar_req = new Boton();
		bot_cargar_req.setValue("2) Subir Requisito");
		bot_cargar_req.setMetodo("insertarRequisito");
		bot_cargar_req.setIcon("ui-icon-plusthick");
		bar_botones.agregarBoton(bot_cargar_req);
		
		Boton bot_cargar_req_sec = new Boton();
		bot_cargar_req_sec.setValue("3) Subir Anexos/Certificados");
		bot_cargar_req_sec.setMetodo("insertarRequisitoSecundario");
		bot_cargar_req_sec.setIcon("ui-icon-plusthick");
		bar_botones.agregarBoton(bot_cargar_req_sec);
		
		Boton bot_enviar = new Boton();
		bot_enviar.setValue("4) Siguiente Tarea");
		bot_enviar.setMetodo("enviarEtapaSiguiente");
		bot_enviar.setIcon("ui-icon-arrowstop-1-e");
		bar_botones.agregarBoton(bot_enviar);
		
		Boton bot_excel=new Boton();
		bot_excel.setIcon("ui-icon-calculator");
  		bot_excel.setValue("GENERAR FUI");
  		bot_excel.setAjax(false);
  		bot_excel.setMetodo("exportarExcel");
  		bar_botones.agregarBoton(bot_excel); 
  		
  		Boton bot_generar= new Boton();
  		bot_generar.setIcon("ui-icon-folder-collapsed");
		bot_generar.setValue("Exportar Expediente");
		bot_generar.setMetodo("generarArchivo");
		bot_generar.setAjax(false);
		bar_botones.agregarBoton(bot_generar);
		
		perfilUsuarioConectado=utilitario.getConexion().consultar(ser_generalAdm.getPerfilConectado(utilitario.getVariable("IDE_PERF")));
		if(validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR") || validarPerfil(perfilUsuarioConectado,"AVANZADO")){	
			//bara el boton empleado 
			Boton bot_empleado=new Boton();
			bot_empleado.setIcon("ui-icon-person");
			bot_empleado.setValue("Cambiar Responsable");
			bot_empleado.setMetodo("importarEmpleado");
			bar_botones.agregarBoton(bot_empleado);

			Boton bot_reasignar=new Boton();
			bot_reasignar.setIcon("ui-icon-person");
			bot_reasignar.setValue("Reasignar Proceso");
			bot_reasignar.setMetodo("reasignar");
			bar_botones.agregarBoton(bot_reasignar);
		}
		
		Boton bot_devolver=new Boton();
		bot_devolver.setIcon("ui-icon-transfer-e-w");
		bot_devolver.setValue("Saltar Tarea / Devolver");
		bot_devolver.setMetodo("devolver");
		bar_botones.agregarBoton(bot_devolver);
		
		set_empleado.setId("set_empleado");
		set_empleado.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true"),"ide_geedp");
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("nombres_apellidos").setFiltro(true);
		set_empleado.setTitle("Seleccione un Empleado");
		set_empleado.getBot_aceptar().setMetodo("aceptarEmpleado");
		set_empleado.setRadio();
		agregarComponente(set_empleado);
		
		con_completar.setId("con_completar");
		con_completar.setMessage("¿Esta Seguro de Completar la tarea?, recuerde que en este proceso no hay reverso");
		con_completar.setTitle("Confirmación");
		con_completar.getBot_aceptar().setMetodo("enviarEtapaSiguiente");
		agregarComponente(con_completar);
		
		
		upl_archivoFirma.setId("upl_archivoFirma");
		upl_archivoFirma.setMetodo("firmarArchivo");
		upl_archivoFirma.setAuto(false);
		upl_archivoFirma.setMultiple(false);
		upl_archivoFirma.setMode("single");
		//upl_archivoFirma.setAllowTypes("/(\\.|\\/)(p12)$/");
		upl_archivoFirma.setUploadLabel("FIRMAR ELECTRONICAMENTE");
		agregarComponente(upl_archivoFirma);
		
		pas_clave_firma.setId("pas_clave_firma");
		pas_clave_firma.setFeedback(false);
		pas_clave_firma.setRequired(false);
		pas_clave_firma.setRequiredMessage("Debe ingresar la clave");
		pas_clave_firma.setValue("");
		pas_clave_firma.setStyle("width: 99%;");
		
		Boton bot_cargar_clave = new Boton();
		bot_cargar_clave.setValue("Ingresar Clave");
		bot_cargar_clave.setMetodo("setClave");
		bot_cargar_clave.setIcon("ui-icon-arrowstop-1-e");
		
		che_firma.setId("che_firma");
		che_firma.setValue("false");
		che_firma.setDisabled(true);
		
		Grid gri_firma=new Grid();
		gri_firma.setWidth("100%");
		gri_firma.setColumns(2);
		gri_firma.getChildren().add(new Etiqueta("Clave:"));
		gri_firma.getChildren().add(pas_clave_firma);
		
		gri_firma.getChildren().add(new Etiqueta("¿Se ingreso la clave?"));
		gri_firma.getChildren().add(che_firma);
		gri_firma.getChildren().add(new Etiqueta(""));
		gri_firma.getChildren().add(bot_cargar_clave);
		gri_firma.getChildren().add(new Etiqueta("Archivo:"));
		gri_firma.getChildren().add(upl_archivoFirma);
		
		dia_firma_electronica.setId("dia_firma_electronica");
		dia_firma_electronica.setTitle("Cargue su Archivo de Firma Electronica");
		dia_firma_electronica.setWidth("40%");
		dia_firma_electronica.setHeight("30%");
		dia_firma_electronica.setDialogo(gri_firma);
		dia_firma_electronica.setDynamic(false);
		dia_firma_electronica.getBot_aceptar().setRendered(false);
		//dia_firma_electronica.getBot_aceptar().setMetodo("firmarArchivo");
       	
       	agregarComponente(dia_firma_electronica);
       	
       	upl_anexo.setId("upl_anexo");
       	upl_anexo.setMetodo("cargarAnexo");
       	upl_anexo.setAuto(false);
       	upl_anexo.setMultiple(false);
       	upl_anexo.setMode("single");
		//upl_archivoFirma.setAllowTypes("/(\\.|\\/)(p12)$/");
       	//upl_anexo.setUploadLabel("FIRMAR ELECTRONICAMENTE");
		agregarComponente(upl_anexo);

		dia_anexo.setId("dia_anexo");
		dia_anexo.setTitle("Cargue su Anexo");
		dia_anexo.setWidth("40%");
		dia_anexo.setHeight("30%");
		dia_anexo.setDialogo(upl_anexo);
		dia_anexo.setDynamic(false);
		dia_anexo.getBot_aceptar().setRendered(false);
		//dia_firma_electronica.getBot_aceptar().setMetodo("firmarArchivo");      	
       	agregarComponente(dia_anexo);
		
       	//dia_firma_electronica.dibujar();
       	
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
		
		PanelAcordion pac_acordion=new PanelAcordion();
		pac_acordion.setId("pac_acordion");	
		pac_acordion.setRendered(true);
		pac_acordion.setActiveIndex("0,1");
		eti_descripcion.setId("eti_descripcion");
		eti_descripcion.setStyle("display: block;font-weight: bold;text-align:justify;");
		eti_descripcion.setValue("");
		
		eti_notas.setId("eti_notas");
		eti_notas.setStyle("display: block;font-weight: bold;text-align:justify;");
		eti_notas.setValue("");
		
		bot_plantilla.setId("bot_plantilla");
		bot_plantilla.setTarget("_blank");
		bot_plantilla.setValue("https://servicios.emgirs.gob.ec/");
		bot_plantilla.setStyle("display: inline-block; margin: 0 0 -8px 0;");
		bot_plantilla.setTitle("PLANTILLA");

		bot_manuales.setId("bot_manuales");
		bot_manuales.setTarget("_blank");
		bot_manuales.setValue(utilitario.getVariable("p_web_files_manuales"));
		bot_manuales.setStyle("display: inline-block; margin: 0 0 -8px 0;");
		bot_manuales.getChildren().add(new Etiqueta("LINK"));

		pac_acordion.agregarPanel("INFORMACIÓN DE TAREAS", eti_descripcion);
		pac_acordion.agregarPanel("PLANTILLAS", bot_plantilla);
		pac_acordion.agregarPanel("MANUALES", bot_manuales);
		pac_acordion.agregarPanel("INFORMACIÓN DE NOTAS", eti_notas);
		
		gri_lateral.getChildren().add(pac_acordion);
		
		dibujarProcesos(0);

		div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(gri_lateral, pan_opcion, "20%", "V");
		div_division.getDivision1().setCollapsible(true);
		div_division.getDivision1().setHeader("MENU DE OPCIONES");
		agregarComponente(div_division);
		
		rad_si_no.setId("rad_si_no");
		rad_si_no.setRadio(utilitario.getListaSiNo());
		
		eti_mensaje.setId("eti_mensaje");
		eti_mensaje.setStyle("display: block;font-weight: bold;text-align:justify;");
		eti_mensaje.setValue("");
		
		are_txt_notas.setId("are_txt_notas");
		are_txt_notas.setAutoResize(true);
		are_txt_notas.setStyle("width:350px; height:70px");
		are_txt_notas.setValue("");
		
		//Ventana emergente seleccionar el pac
		set_pac.setId("set_pac");
		set_pac.setTitle("Seleccione Un Proceso de Contratación (PAC)");
		set_pac.setSeleccionTabla(ser_presupuesto.getLineasPac("-1","-1",false,"-1"),"ide_prpac");
		set_pac.getTab_seleccion().getColumna("descripcion").setFiltroContenido();	
		set_pac.getBot_aceptar().setMetodo("aceptarActividad");
		set_pac.getBot_cancelar().setMetodo("cancelarActividad");
		set_pac.setFooter("Un proceso sin responsable no podra ser iniciado...");
		set_pac.setRadio();
		agregarComponente(set_pac);
		
		set_lineas_poa.setId("set_lineas_poa");
		set_lineas_poa.setTitle("Seleccione las lineas POA que Certificará");
		set_lineas_poa.setSeleccionTabla(ser_presupuesto.getPoa("-1","true","false",0,true),"ide_prpoa");
		set_lineas_poa.getTab_seleccion().getColumna("detalle_subactividad").setFiltroContenido();
		set_lineas_poa.getTab_seleccion().getColumna("codigo").setFiltroContenido();
		set_lineas_poa.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltroContenido();
		set_lineas_poa.setFooter("Estas lineas seran enviadas a planificación para su Certificación.");
		set_lineas_poa.getBot_aceptar().setMetodo("enviarEtapaSiguiente");
		agregarComponente(set_lineas_poa);
		
		gri_aprobar.setId("gri_aprobar");
		gri_aprobar.setColumns(2);
		
		com_actividadesR.setId("com_actividadesR");
		//com_actividadesR.setCombo("");

		dia_aprobar.setId("dia_aprobar");
		dia_aprobar.setTitle("¿Cuenta con Aprobación?");
		dia_aprobar.setWidth("45%");
		dia_aprobar.setHeight("35%");
		dia_aprobar.setDialogo(gri_aprobar);	
		dia_aprobar.getBot_aceptar().setMetodo("aprobar");
		agregarComponente(dia_aprobar);
		
		are_txt_edit.setId("are_txt_edit");
		are_txt_edit.setHeight(450);
		are_txt_edit.setWidth(880);
		are_txt_edit.setValue("");
       	
		dia_solicitud.setId("dia_solicitud");
		dia_solicitud.setTitle("Solicitud");
		dia_solicitud.setWidth("70%");
		dia_solicitud.setHeight("80%");
		dia_solicitud.setDialogo(new Etiqueta("Ingrese el detalle de la solicitud..."));
		dia_solicitud.setDialogo(are_txt_edit);	
		dia_solicitud.getBot_aceptar().setMetodo("enviarEtapaSiguiente");
		
       	agregarComponente(dia_solicitud);
       	
	}
	
	public void seleccionaElAnio (){   	
    	//cargarInformacionDependiendoAreaYUsuario();
		dibujarProcesos(1);
	}
	
	/**
	 * forma el menu de la parte derecha con las opciones de la ficha del proceso
	 */
	private void contruirMenu() {
		pam_menu.setWidgetVar("100%");

		// SUB MENU 1
		Submenu sum_proceso = new Submenu();
		sum_proceso.setLabel("PROCESOS DE CONTRATACION");
		pam_menu.getChildren().add(sum_proceso);

		// ITEM 1 : OPCION 0
		ItemMenu itm_mis_procesos = new ItemMenu();
		itm_mis_procesos.setValue("MIS PROCESOS");
		itm_mis_procesos.setIcon("ui-icon-person");
		itm_mis_procesos.setMetodo("dibujarMisProcesos");
		itm_mis_procesos.setUpdate("pan_opcion");
		sum_proceso.getChildren().add(itm_mis_procesos);
		
		// ITEM : OPCION 
		ItemMenu itm_procesos_asignados = new ItemMenu();
		itm_procesos_asignados.setValue("PROCESOS RECIBIDOS");
		itm_procesos_asignados.setIcon("ui-icon-person");
		itm_procesos_asignados.setMetodo("dibujarProcesosRecibidos");
		itm_procesos_asignados.setUpdate("pan_opcion");
		sum_proceso.getChildren().add(itm_procesos_asignados);
		
		// ITEM : OPCION 
		ItemMenu itm_archivos_p = new ItemMenu();
		itm_archivos_p.setValue("PROCESOS ENVIADOS");
		itm_archivos_p.setIcon("ui-icon-person");
		itm_archivos_p.setMetodo("dibujarProcesosEnviados");
		itm_archivos_p.setUpdate("pan_opcion");
		sum_proceso.getChildren().add(itm_archivos_p);

		
		// SUB MENU
		Submenu sum_seguimiento = new Submenu();
		sum_seguimiento.setLabel("REPORTES");
		pam_menu.getChildren().add(sum_seguimiento);


	}
	
	private void limpiarPanel(){		
		pan_opcion.getChildren().clear();
		pan_opcion.getChildren().add(efecto);
	}
	

	public void dibujarMisProcesos() 
	{	
		dibujarProcesos(1);
		validarTareas(false);
	}
	
	public void dibujarProcesosRecibidos() 
	{	
		dibujarProcesos(2);
		validarTareas(false);
	}
	
	public void dibujarProcesosEnviados() 
	{	
		dibujarProcesos(3);
		validarTareas(false);
	}
	
	public void dibujarProcesos(int tipo) 
	{		
		int_opcion=tipo;
		limpiarPanel();
		//List perfilUsuarioConectado=utilitario.getConexion().consultar(ser_generalAdm.getPerfilConectado(utilitario.getVariable("IDE_PERF")));
		
		tab_precontractual = new Tabla();
		tab_precontractual.setId("tab_precontractual");
		//tab_precontractual.setHeader("MIS PROCESOS");
		tab_precontractual.setTabla("precon_precontractual","ide_prpre",1);
		//tab_precontractual.setTipoFormulario(true);
		//tab_precontractual.getGrid().setColumns(4);
		//tab_precontractual.setMostrarNumeroRegistros(false);
		tab_precontractual.setValidarInsertar(true);
			
		tab_precontractual.getColumna("ide_prpre").setNombreVisual("Código");
		tab_precontractual.getColumna("ide_prpre").setOrden(1);
		tab_precontractual.getColumna("ide_prpre").setLongitud(8);
		tab_precontractual.getColumna("codigo_prpre").setNombreVisual("Código Proceso");
		tab_precontractual.getColumna("codigo_prpre").setOrden(2);
		tab_precontractual.getColumna("codigo_prpre").setLongitud(15);
		tab_precontractual.getColumna("codigo_prpre").setLectura(true);
		tab_precontractual.getColumna("codigo_prpre").setEstilo("width:200px");
		//tab_precontractual.getColumna("fecha_prpre");
		tab_precontractual.getColumna("fecha_prpre").setNombreVisual("Fecha de Registro");
		tab_precontractual.getColumna("fecha_prpre").setOrden(3);
		tab_precontractual.getColumna("fecha_prpre").setLectura(true);
		//tab_precontractual.getColumna("hora_prpre");
		tab_precontractual.getColumna("hora_prpre").setVisible(false);
		tab_precontractual.getColumna("ide_prpac").setVisible(false);
				
		tab_precontractual.getColumna("ide_pretp").setCombo(ser_EtapaProcedimiento.getProcedimientos());
		tab_precontractual.getColumna("ide_pretp").setNombreVisual("Procedimiento");
		tab_precontractual.getColumna("ide_pretp").setLectura(true);
		tab_precontractual.getColumna("ide_pretp").setOrden(5);
		tab_precontractual.getColumna("ide_pretp").setEstilo("width:300px");
		tab_precontractual.getColumna("descripcion_prpre").setNombreVisual("Descripción (NOMBRE DEL PROCESO)");
		tab_precontractual.getColumna("descripcion_prpre").setOrden(4);
		tab_precontractual.getColumna("descripcion_prpre").setRequerida(true);
		
	    tab_precontractual.getColumna("estado_proceso_prpre").setCombo(utilitario.getListaEstadoProceso());
	    tab_precontractual.getColumna("estado_proceso_prpre").setNombreVisual("Estado");
	    tab_precontractual.getColumna("estado_proceso_prpre").setOrden(6);
	    tab_precontractual.getColumna("estado_proceso_prpre").setLectura(true);

	    tab_precontractual.getColumna("ide_geare").setCombo("gen_area","ide_geare","detalle_geare","");
	    tab_precontractual.getColumna("ide_geare").setLectura(true);
	    
	    //tab_precontractual.getColumna("monto_prpre");
		tab_precontractual.getColumna("monto_prpre").setNombreVisual("Monto");
		tab_precontractual.getColumna("monto_prpre").setOrden(7);
		tab_precontractual.getColumna("monto_prpre").setLectura(true);
		//tab_precontractual.getColumna("ide_actual_preta");
		tab_precontractual.getColumna("ide_actual_preta").setVisible(false);
		tab_precontractual.getColumna("actividad_actual_prpre").setNombreVisual("Actividad Actual");
		tab_precontractual.getColumna("actividad_actual_prpre").setOrden(8);
		tab_precontractual.getColumna("actividad_actual_prpre").setLectura(true);
		tab_precontractual.getColumna("actividad_actual_prpre").setEstilo("width:300px");
		//tab_precontractual.getColumna("ide_actual_geedp");
		
		tab_precontractual.getColumna("ide_actual_geedp").setVisible(false);		
		tab_precontractual.getColumna("responsable_actual_prpre");
		tab_precontractual.getColumna("responsable_actual_prpre").setNombreVisual("Responsable Actual");
		tab_precontractual.getColumna("responsable_actual_prpre").setOrden(9);
		tab_precontractual.getColumna("responsable_actual_prpre").setLectura(true);
		tab_precontractual.getColumna("departamento_actual_prpre").setNombreVisual("Departamento Actual");
		tab_precontractual.getColumna("departamento_actual_prpre").setOrden(10);
		tab_precontractual.getColumna("departamento_actual_prpre").setLectura(true);
		tab_precontractual.getColumna("departamento_actual_prpre").setEstilo("width:300px");
		tab_precontractual.getColumna("ide_usuario_iniciop_prpre");
		tab_precontractual.getColumna("ide_usuario_iniciop_prpre").setVisible(false);
		tab_precontractual.getColumna("ide_preta").setCombo("precon_etapa", "ide_preta","descripcion_preta", "");
		tab_precontractual.getColumna("ide_preta").setNombreVisual("Actividad a Enviar");
		tab_precontractual.getColumna("ide_preta").setRequerida(false);
		tab_precontractual.getColumna("ide_preta").setLectura(true);
		tab_precontractual.getColumna("ide_preta").setOrden(11);
		tab_precontractual.getColumna("ide_preta").setEstilo("width:300px");
		//tab_precontractual.getColumna("observacion_prpre");
		tab_precontractual.getColumna("observacion_prpre").setNombreVisual("Observación");
		tab_precontractual.getColumna("observacion_prpre").setOrden(12);
		tab_precontractual.getColumna("ide_tepro").setCombo(ser_generalAdm.getProveedores());
		tab_precontractual.getColumna("ide_tepro").setAutoCompletar();
		tab_precontractual.getColumna("ide_tepro").setOrden(13);
		tab_precontractual.getColumna("ide_tepro").setNombreVisual("Proveedor");
		tab_precontractual.getColumna("ide_tepro").setLectura(true);

	    
	    tab_precontractual.getColumna("termino_especificacion_prpre").setCombo(utilitario.getListaTipoProcesoContracion(false));
	    tab_precontractual.getColumna("termino_especificacion_prpre").setNombreVisual("¿Defina si es un Bien o Servicio?");
	    tab_precontractual.getColumna("termino_especificacion_prpre").setOrden(5);
	    tab_precontractual.getColumna("termino_especificacion_prpre").setValorDefecto("NA");
	    tab_precontractual.getColumna("termino_especificacion_prpre").setLectura(true);

	    tab_precontractual.getColumna("aprueba_proyecto_prpre").setCombo(utilitario.getListaSiNo());
	    tab_precontractual.getColumna("aprueba_proyecto_prpre").setNombreVisual("¿Aprueba?");
	    tab_precontractual.getColumna("aprueba_proyecto_prpre").setOrden(14);
	    tab_precontractual.getColumna("aprueba_proyecto_prpre").setLectura(true);

		tab_precontractual.getColumna("activo_prpre").setNombreVisual("ACTIVO");
		tab_precontractual.getColumna("activo_prpre").setValorDefecto("true");
		tab_precontractual.getColumna("activo_prpre").setLectura(true);
		tab_precontractual.getColumna("activo_prpre").setVisible(true);
		tab_precontractual.getColumna("activo_prpre").setOrden(15);

		tab_precontractual.getColumna("aprueba_informetm_prpre").setVisible(false);
		tab_precontractual.getColumna("es_favorable_informej_prpre").setVisible(false);
		tab_precontractual.getColumna("cumple_prpre").setVisible(false);
		tab_precontractual.getColumna("manifestacion_interes_prpre").setVisible(false);
		tab_precontractual.getColumna("es_superior_prpre").setVisible(false);
		tab_precontractual.getColumna("convalida_recadjudicar_prpre").setVisible(false);
		tab_precontractual.getColumna("se_recibe_oferta_prpre").setVisible(false);
		tab_precontractual.getColumna("necesario_reaperturar_prpre").setVisible(false);
		tab_precontractual.getColumna("recomienda_adjudicar_prpre").setVisible(false);
		tab_precontractual.getColumna("existe_mas_oferta_prpre").setVisible(false);
		tab_precontractual.getColumna("convalidacion_satisfac_prpre").setVisible(false);
		tab_precontractual.getColumna("error_forma_prpre").setVisible(false);
		tab_precontractual.getColumna("es_viable_prpre").setVisible(false);
		tab_precontractual.getColumna("comision_tecnica_prpre").setVisible(false);
		tab_precontractual.getColumna("esta_ok_prpre").setVisible(false);
		tab_precontractual.getColumna("solicitar_alcance_prpre").setVisible(false);
		tab_precontractual.getColumna("es_proceso_infima_prpre").setVisible(false);
		tab_precontractual.getColumna("es_favorable_informej_prpre").setLectura(true);
		tab_precontractual.getColumna("consta_pac_prpre").setVisible(false);
		tab_precontractual.getColumna("consta_poa_prpre").setVisible(false);
		//tab_precontractual.getColumna("aprueba_proyecto_prpre").setVisible(false);
		tab_precontractual.getColumna("se_encuentra_catalogoe_prpre").setVisible(false);
		//tab_precontractual.getColumna("termino_especificacion_prpre").setVisible(false);
				
		/*if(int_opcion==2)
		{			
			tab_precontractual.getColumna("activo_prpre").setLectura(true);
			tab_precontractual.getColumna("aprueba_informetm_prpre").setVisible(false);
			tab_precontractual.getColumna("cumple_prpre").setVisible(false);
		}	*/	
		tab_precontractual.getColumna("descripcion_prpre").setLectura(true);
		if(int_opcion==3)
		{		
			tab_precontractual.getColumna("descripcion_prpre").setLectura(true);
			tab_precontractual.getColumna("observacion_prpre").setLectura(true);
			tab_precontractual.getColumna("ide_tepro").setLectura(true);
		}
		
		/*if(empleado.equals("22 BORRAR"))
		{
			tab_precontractual.getColumna("ide_preta").setLectura(false);
			tab_precontractual.getColumna("departamento_actual_prpre").setLectura(false);
			tab_precontractual.getColumna("responsable_actual_prpre").setLectura(false);
			tab_precontractual.getColumna("ide_actual_geedp").setVisible(true);
			tab_precontractual.getColumna("ide_actual_preta").setVisible(true);
			tab_precontractual.getColumna("actividad_actual_prpre").setLectura(false);
			tab_precontractual.getColumna("ide_actual_geedp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
			tab_precontractual.getColumna("ide_actual_geedp").setAutoCompletar();
			
			tab_precontractual.getColumna("ide_actual_preta").setCombo("precon_etapa", "ide_preta","descripcion_preta", "");
			tab_precontractual.getColumna("ide_pretp").setLectura(false);
			tab_precontractual.getColumna("termino_especificacion_prpre").setLectura(false);
			tab_precontractual.getColumna("estado_proceso_prpre").setLectura(false);
			tab_precontractual.getColumna("ide_usuario_iniciop_prpre").setVisible(true);
		}*/
	    
	    if(validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR") || validarPerfil(perfilUsuarioConectado,"AVANZADO"))
	    {
	    	tab_precontractual.getColumna("estado_proceso_prpre").setLectura(false);
	    	tab_precontractual.getColumna("ide_pretp").setLectura(false);
	    	tab_precontractual.getColumna("termino_especificacion_prpre").setLectura(false);
	    	tab_precontractual.getColumna("descripcion_prpre").setLectura(false);
	    }

		tab_precontractual.setCondicion("ide_prpre=-1");
		tab_precontractual.onSelect("seleccionarProceso");
		tab_precontractual.agregarRelacion(tab_documento_requisito);
		tab_precontractual.agregarRelacion(tab_seguimiento);		
		tab_precontractual.dibujar();		
		
        PanelTabla pat_precontractual=new PanelTabla();
        pat_precontractual.setPanelTabla(tab_precontractual);
 
        Tabulador tab_Tabulador=new Tabulador();
		tab_Tabulador.setId("tab_tabulador");
		
		// detalle documento requisito
		tab_documento_requisito.setTipoFormulario(true);  //formulario 
		tab_documento_requisito.getGrid().setColumns(2); //hacer  columnas

		tab_documento_requisito.setId("tab_documento_requisito");
		tab_documento_requisito.setIdCompleto("tab_tabulador:tab_documento_requisito");
		tab_documento_requisito.setTabla("precon_documento_requisito","ide_prdoc", 2);
		tab_documento_requisito.setHeader("Sitras, Solicitudes, etc...");
		tab_documento_requisito.getColumna("con_ide_prdoc").setVisible(false);
		tab_documento_requisito.getColumna("ide_prdoc").setNombreVisual("Código");
		tab_documento_requisito.getColumna("ide_prreq").setCombo("precon_requisito","ide_prreq","descripcion_prreq","");
		tab_documento_requisito.getColumna("ide_prreq").setNombreVisual("Requisito Actividad");
		tab_documento_requisito.getColumna("ide_prreq").setLectura(true);
		tab_documento_requisito.getColumna("ide_prtip").setCombo("precon_tipologia","ide_prtip","descripcion_prtip","");
		tab_documento_requisito.getColumna("ide_prtip").setValorDefecto("6");
		tab_documento_requisito.getColumna("ide_prtip").setNombreVisual("Tipología");
		tab_documento_requisito.getColumna("ide_prtip").setOrden(4);
		//tab_documento_requisito.getColumna("ide_prreq").setLongitud_control(10);
		//tab_documento_requisito.getColumna("fecha_presenta_prdoc").setLongitud(40);
		tab_documento_requisito.getColumna("fecha_presenta_prdoc").setNombreVisual("Fecha Documento");
		//tab_documento_requisito.getColumna("fecha_presenta_prdoc").setLectura(true);
	    tab_documento_requisito.getColumna("presenta_prdoc").setCombo(utilitario.getListaSiNo());
		tab_documento_requisito.getColumna("presenta_prdoc").setNombreVisual("Subió el Archivo?");
		tab_documento_requisito.getColumna("presenta_prdoc").setLectura(true);
		tab_documento_requisito.getColumna("documentoadjunto_prdoc").setNombreVisual("Anexo Principal");
		tab_documento_requisito.getColumna("documentoadjunto_prdoc").setUpload(carpeta);
		tab_documento_requisito.getColumna("descripcion_prdoc").setNombreVisual("Link Portal Archivo Anexo SERCOP");
		tab_documento_requisito.getColumna("descripcion_prdoc").setComentario("Se registra el link donde se subio el archivo del portal de la SERCOP.");
		tab_documento_requisito.getColumna("NRO_DOCUMENTO_PRDOC").setRequerida(true);
		tab_documento_requisito.getColumna("ASUNTO_PRDOC").setRequerida(true);
		tab_documento_requisito.getColumna("DE_NOMBRE_PRDOC").setRequerida(true);
		tab_documento_requisito.getColumna("PARA_NOMBRE_PRDOC").setRequerida(true);
		tab_documento_requisito.getColumna("DE_NOMBRE_PRDOC").setValorDefecto("N/A");
		tab_documento_requisito.getColumna("PARA_NOMBRE_PRDOC").setValorDefecto("N/A");
		tab_documento_requisito.getColumna("NRO_FOJAS_PRDOC").setRequerida(true);
		tab_documento_requisito.getColumna("anexo_prdoc").setVisible(false);

		if(validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR")){
			tab_documento_requisito.getColumna("descripcion_prdoc").setVisible(true);
			tab_documento_requisito.getColumna("presenta_prdoc").setLectura(false);
		}else{
			tab_documento_requisito.getColumna("descripcion_prdoc").setVisible(false);
		}
		tab_documento_requisito.getColumna("etapa_prdoc");
		tab_documento_requisito.getColumna("etapa_prdoc").setNombreVisual("Actividad");
		tab_documento_requisito.getColumna("etapa_prdoc").setLectura(true);
		tab_documento_requisito.getColumna("activo_prdoc").setNombreVisual("Activo");
		tab_documento_requisito.getColumna("activo_prdoc").setValorDefecto("true");
		tab_documento_requisito.getColumna("activo_prdoc").setLectura(true);
		tab_documento_requisito.getColumna("activo_prdoc").setOrden(11);
		tab_documento_requisito.getColumna("original_prdoc").setValorDefecto("true");
		tab_documento_requisito.getColumna("original_prdoc").setNombreVisual("original/copia");
		tab_documento_requisito.getColumna("electronico_prdoc").setValorDefecto("true");
		tab_documento_requisito.getColumna("detalle_anexos_prdoc").setVisible(false);
		tab_documento_requisito.getColumna("electronico_prdoc").setVisible(false);
		if(int_opcion==3)
		{
			tab_documento_requisito.getColumna("documentoadjunto_prdoc").setLectura(true);	

		}
		else
		{
			tab_documento_requisito.getColumna("documentoadjunto_prdoc").setLectura(false);		
				
		}
		tab_documento_requisito.setCampoForanea("ide_prpre");
		tab_documento_requisito.setCondicion("con_ide_prdoc is null");
		tab_documento_requisito.setCampoOrden("ide_prdoc desc");
		tab_documento_requisito.dibujar();
		
		PanelTabla pat_documento_requisito = new PanelTabla();
		pat_documento_requisito.setPanelTabla(tab_documento_requisito);
		
		//if(validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR") || validarPerfil(perfilUsuarioConectado,"AVANZADO"))
		//{
			ItemMenu itm_requisito_borra = new ItemMenu();
			itm_requisito_borra.setValue("Re-Generar");
			itm_requisito_borra.setMetodo("requisitoBorrar");
			itm_requisito_borra.setIcon("ui-icon-close");
			pat_documento_requisito.getMenuTabla().getChildren().add(itm_requisito_borra);
			
			ItemMenu itm_borra1 = new ItemMenu();
			itm_borra1.setValue("Borrar");
			itm_borra1.setMetodo("borrarArchivo");
			itm_borra1.setIcon("ui-icon-trash");
			pat_documento_requisito.getMenuTabla().getChildren().add(itm_borra1);
		
		//}
		
		tab_documento_requisito_secundario.setId("tab_documento_requisito_secundario");
		tab_documento_requisito_secundario.setIdCompleto("tab_tabulador:tab_documento_requisito_secundario");
		tab_documento_requisito_secundario.setTabla("precon_documento_requisito","ide_prdoc", 4);
		tab_documento_requisito_secundario.setTipoFormulario(true);  //formulario 
		tab_documento_requisito_secundario.getGrid().setColumns(2); //hacer  columnas
		tab_documento_requisito_secundario.setHeader("Anexos, Alcances, Certificados, etc...");
		tab_documento_requisito_secundario.getColumna("ide_prreq").setCombo("precon_requisito","ide_prreq","descripcion_prreq","");
		tab_documento_requisito_secundario.getColumna("ide_prreq").setNombreVisual("Requisito Actividad");
		tab_documento_requisito_secundario.getColumna("ide_prreq").setVisible(false);
		tab_documento_requisito_secundario.getColumna("IDE_PRDOC").setNombreVisual("Código");		
		tab_documento_requisito_secundario.getColumna("etapa_prdoc").setNombreVisual("Actividad");		
		tab_documento_requisito_secundario.getColumna("fecha_presenta_prdoc").setNombreVisual("Fecha Documento");
		tab_documento_requisito_secundario.getColumna("fecha_presenta_prdoc").setValorDefecto(utilitario.getFechaActual());
		tab_documento_requisito_secundario.getColumna("descripcion_prdoc").setNombreVisual("Nombre Documento");
		tab_documento_requisito_secundario.getColumna("descripcion_prdoc").setValorDefecto("sin nombre");
		tab_documento_requisito_secundario.getColumna("descripcion_prdoc").setEtiqueta();
		tab_documento_requisito_secundario.getColumna("NRO_DOCUMENTO_PRDOC").setVisible(false);
		tab_documento_requisito_secundario.getColumna("documentoadjunto_prdoc").setNombreVisual("Anexo");
		tab_documento_requisito_secundario.getColumna("documentoadjunto_prdoc").setUpload(carpeta);
		tab_documento_requisito_secundario.getColumna("documentoadjunto_prdoc").setColumnaNombreArchivo("descripcion_prdoc");		
		tab_documento_requisito_secundario.getColumna("presenta_prdoc").setCombo(utilitario.getListaSiNo());
		tab_documento_requisito_secundario.getColumna("presenta_prdoc").setNombreVisual("Subió el Archivo?");
		tab_documento_requisito_secundario.getColumna("presenta_prdoc").setValorDefecto("SI");
		tab_documento_requisito_secundario.getColumna("presenta_prdoc").setLectura(true);
		tab_documento_requisito_secundario.getColumna("activo_prdoc").setValorDefecto("true");
		tab_documento_requisito_secundario.getColumna("activo_prdoc").setLectura(true);
		tab_documento_requisito_secundario.getColumna("NRO_FOJAS_PRDOC").setNombreVisual("numero de archivos/fojas");		
		tab_documento_requisito_secundario.getColumna("original_prdoc").setNombreVisual("original/copia");
		tab_documento_requisito_secundario.getColumna("original_prdoc").setValorDefecto("true");
		//tab_documento_requisito_secundario.getColumna("electronico_prdoc").setValorDefecto("true");
		
		tab_documento_requisito_secundario.getColumna("ide_prtip").setVisible(false);
		tab_documento_requisito_secundario.getColumna("etapa_prdoc").setVisible(false);
		tab_documento_requisito_secundario.getColumna("electronico_prdoc").setVisible(false);
		tab_documento_requisito_secundario.getColumna("ASUNTO_PRDOC").setVisible(false);
		tab_documento_requisito_secundario.getColumna("DE_NOMBRE_PRDOC").setVisible(false);
		tab_documento_requisito_secundario.getColumna("PARA_NOMBRE_PRDOC").setVisible(false);
		tab_documento_requisito_secundario.getColumna("ide_prpre").setVisible(false);
		tab_documento_requisito_secundario.getColumna("con_ide_prdoc").setVisible(false);
		tab_documento_requisito_secundario.getColumna("anexo_prdoc").setNombreVisual("Alcance");
		tab_documento_requisito_secundario.getColumna("DETALLE_ANEXOS_PRDOC").setNombreVisual("Observaciones");
		
		tab_documento_requisito_secundario.getColumna("IDE_PRDOC").setOrden(1);
		tab_documento_requisito_secundario.getColumna("fecha_presenta_prdoc").setOrden(2);
		tab_documento_requisito_secundario.getColumna("presenta_prdoc").setOrden(3);
		tab_documento_requisito_secundario.getColumna("documentoadjunto_prdoc").setOrden(4);
		tab_documento_requisito_secundario.getColumna("descripcion_prdoc").setOrden(5);
		tab_documento_requisito_secundario.getColumna("original_prdoc").setOrden(6);
		tab_documento_requisito_secundario.getColumna("anexo_prdoc").setOrden(7);
		tab_documento_requisito_secundario.getColumna("NRO_FOJAS_PRDOC").setOrden(8);
		tab_documento_requisito_secundario.getColumna("DETALLE_ANEXOS_PRDOC").setOrden(9);
		tab_documento_requisito_secundario.getColumna("activo_prdoc").setOrden(10);
		
		tab_documento_requisito_secundario.setCondicion("con_ide_prdoc=-1");
		tab_documento_requisito_secundario.dibujar();
		
		PanelTabla pat_documento_requisito_secundario = new PanelTabla();
		pat_documento_requisito_secundario.setPanelTabla(tab_documento_requisito_secundario);
		
		//if(validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR") || validarPerfil(perfilUsuarioConectado,"AVANZADO"))
		//{
		ItemMenu itm_borra = new ItemMenu();
		itm_borra.setValue("Borrar");
		itm_borra.setMetodo("borrarAnexo");
		itm_borra.setIcon("ui-icon-trash");
		pat_documento_requisito_secundario.getMenuTabla().getChildren().add(itm_borra);

		//}
		
		// detalle seguimiento
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
		tab_seguimiento.getColumna("departamento_prseg").setNombreVisual("DEPARTAMENTO RESPONSABLE");
		tab_seguimiento.getColumna("etapa_fin_prseg").setNombreVisual("ACTIVIDAD FIN");
		tab_seguimiento.getColumna("responsable_asignado_prseg").setNombreVisual("RESPONSABLE ASIGNADO");
		tab_seguimiento.getColumna("departamento_asignado_prseg").setNombreVisual("DEPARTAMENTO ASIGNADO");
		tab_seguimiento.getColumna("observacion_prseg").setNombreVisual("OBSERVACIÓN");
		tab_seguimiento.getColumna("notas_prseg").setNombreVisual("NOTAS");
		tab_seguimiento.getColumna("estado_actividad_prseg").setNombreVisual("ESTADO ACTIVIDAD");
		tab_seguimiento.getColumna("estado_procedimiento_prseg").setNombreVisual("ESTADO PROCEDIMIENTO");	
		tab_seguimiento.getColumna("activo_prseg").setLongitud(40);
		tab_seguimiento.getColumna("activo_prseg").setNombreVisual("ACTIVO");
		tab_seguimiento.getColumna("ide_preta_prseg").setVisible(false);
		/*tab_seguimiento.getColumna("fecha_asignacion_prseg").setLectura(true);
		tab_seguimiento.getColumna("hora_asignacion_prseg").setLectura(true);
		tab_seguimiento.getColumna("fecha_cambio_prseg").setLectura(true);
		tab_seguimiento.getColumna("hora_cambio_prseg").setLectura(true);
		tab_seguimiento.getColumna("etapa_inicio_prseg").setLectura(true);
		tab_seguimiento.getColumna("responsable_prseg").setLectura(true);
		tab_seguimiento.getColumna("departamento_prseg").setLectura(true);
		tab_seguimiento.getColumna("etapa_fin_prseg").setLectura(true);
		tab_seguimiento.getColumna("responsable_asignado_prseg").setLectura(true);
		tab_seguimiento.getColumna("departamento_asignado_prseg").setLectura(true);
		tab_seguimiento.getColumna("observacion_prseg").setLectura(true);
		tab_seguimiento.getColumna("notas_prseg").setLectura(true);
		tab_seguimiento.getColumna("estado_actividad_prseg").setLectura(true);
		tab_seguimiento.getColumna("estado_procedimiento_prseg").setLectura(true);*/
		//tab_seguimiento.getColumna("activo_prseg").setLectura(true);
		//tab_seguimiento.setLectura(true);
		tab_seguimiento.dibujar();
		//tab_seguimiento.setLectura(false);
		PanelTabla pat_seguimiento = new PanelTabla();
		pat_seguimiento.setPanelTabla(tab_seguimiento);
		
		
		tab_partida_poa.setId("tab_partida_poa");
		tab_partida_poa.setIdCompleto("tab_tabulador:tab_partida_poa");
		tab_partida_poa.setTabla("pre_partida_pac","ide_prpap", 4);
		//tab_partida_poa.setTipoFormulario(true);  //formulario 
		//tab_partida_poa.getGrid().setColumns(4); //hacer  columnas
		tab_partida_poa.getColumna("ide_prcla").setCombo("select ide_prcla,codigo_clasificador_prcla,descripcion_clasificador_prcla from pre_clasificador order by codigo_clasificador_prcla");
		tab_partida_poa.getColumna("ide_prcla").setAutoCompletar();
		tab_partida_poa.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoaTodos());
		tab_partida_poa.getColumna("ide_prpoa").setAutoCompletar();
		tab_partida_poa.getColumna("ide_prpac").setVisible(false);
		tab_partida_poa.getColumna("ide_prpoa").setLectura(true);
		tab_partida_poa.getColumna("ide_prcla").setLectura(true);
		tab_partida_poa.getColumna("portal_prpap").setLectura(true);
		tab_partida_poa.getColumna("anio_prpap").setLectura(true);
		tab_partida_poa.getColumna("certificado_poa_prpap").setLectura(true);
		tab_partida_poa.setCondicion("ide_prpac=-1");
		tab_partida_poa.dibujar();
		PanelTabla pat_partida_poa = new PanelTabla();
		pat_partida_poa.setPanelTabla(tab_partida_poa);
		
		tab_pre_pac.setId("tab_pre_pac");
		tab_pre_pac.setIdCompleto("tab_tabulador:tab_pre_pac");
		tab_pre_pac.setTabla("pre_pac","ide_prpac", 5);
		tab_pre_pac.setHeader("PAC");
		tab_pre_pac.setTipoFormulario(true);  //formulario 
		tab_pre_pac.getGrid().setColumns(4); //hacer  columnas
		tab_pre_pac.getColumna("ide_copec").setCombo("cont_periodo_cuatrimestre","ide_copec","detalle_copec","");
		tab_pre_pac.getColumna("ide_cotio").setCombo("cont_tipo_compra","ide_cotio","detalle_cotio","");
		tab_pre_pac.getColumna("ide_coest").setCombo("cont_estado", "ide_coest","detalle_coest","");
		tab_pre_pac.getColumna("ide_bounm").setCombo("bodt_unidad_medida","ide_bounm","detalle_bounm","");
		tab_pre_pac.getColumna("tipo_prod_prpac").setCombo(utilitario.getListaTipoProducto());	
		tab_pre_pac.getColumna("tregimen_prpac").setCombo(utilitario.getListaTipoRegimen());	
		tab_pre_pac.getColumna("ide_adtic").setCombo("adq_tipo_contratacion","ide_adtic", "detalle_adtic","");
		tab_pre_pac.getColumna("ide_geare").setCombo("gen_area","ide_geare","detalle_geare","");
		tab_pre_pac.setLectura(true);
		tab_pre_pac.setCondicion("ide_prpac=-1");
		tab_pre_pac.dibujar();
		PanelTabla pat_pre_pac = new PanelTabla();
		pat_pre_pac.setPanelTabla(tab_pre_pac);
		
		//RESPONSABLE PAC
		tab_responsable_prec.setId("tab_responsable_prec");
		//tab_responsable_prec.setHeader("RESPONSABLES DE CONTRATACION");
		tab_responsable_prec.setIdCompleto("tab_tabulador:tab_responsable_prec");
		tab_responsable_prec.setTabla("pre_responsable_contratacion","ide_prrec",6);
		tab_responsable_prec.setHeader("RESPONSABLE TECNICO - PAC");
		tab_responsable_prec.getColumna("ide_prcop").setVisible(false);
		tab_responsable_prec.getColumna("ide_prpac").setVisible(false);
		tab_responsable_prec.getColumna("IDE_GTEMP").setVisible(false);
		tab_responsable_prec.getColumna("IDE_GEEDP").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_responsable_prec.getColumna("IDE_GEEDP").setAutoCompletar();
		tab_responsable_prec.getColumna("IDE_GEEDP").setLectura(true);
		tab_responsable_prec.setCondicion("ide_prcop is null and ide_prpac=-1");
		tab_responsable_prec.getColumna("activo_prrec").setValorDefecto("true");
		tab_responsable_prec.dibujar();
		PanelTabla pat_panel_resp = new PanelTabla();
		pat_panel_resp.setPanelTabla(tab_responsable_prec);
		

		Division div_division_rq = new Division();
		div_division_rq.setId("div_division_rq");
		div_division_rq.dividir2(pat_documento_requisito, pat_documento_requisito_secundario, "60%", "V");
		//div_division_rq.getDivision1().setCollapsible(true);
		
		//tab_Tabulador.setDynamic(true);
		//tab_Tabulador.setTransient(true);
		tab_Tabulador.agregarTab("REQUISITOS", div_division_rq);
		tab_Tabulador.agregarTab("PARTIDAS/POA", pat_partida_poa);
		tab_Tabulador.agregarTab("VER PAC", pat_pre_pac);
		tab_Tabulador.agregarTab("VER TECNICO DESIGNADO", pat_panel_resp);
		tab_Tabulador.agregarTab("SEGUIMIENTO", pat_seguimiento);
		

		/*PanelAcordion pac_acordion2=new PanelAcordion();
		pac_acordion2.setId("pac_acordion2");	
		pac_acordion2.setRendered(true);
		pac_acordion2.setActiveIndex("0");
		pac_acordion2.agregarPanel("CONTRATACIONES", pat_precontractual);
		//pac_acordion2.agregarPanel("DOCUMENTACIÓN", tab_Tabulador);
		*/
		 
		Division div_division2 = new Division();
		//div_division2.dividir1(pac_acordion2);
		//div_division2.dividir2(pac_acordion2,tab_Tabulador,"40%","h");
		div_division2.dividir2(pat_precontractual,tab_Tabulador,"30%","h");
		
		if(int_opcion>0)
			cargarInformacionDependiendoAreaYUsuario();

		mostrarCamposDeAcuerdoActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
		if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre"))!=0){
			tab_documento_requisito.setSql(ser_docrquisto.getDocumentoRequisito(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre"))));
			//tab_documento_requisito.ejecutarSql();
			tab_documento_requisito.dibujar();
			if(tab_documento_requisito.getTotalFilas()>0)
			{
				tab_documento_requisito_secundario.setCondicion("con_ide_prdoc="+tab_documento_requisito.getValor("ide_prdoc"));
				tab_documento_requisito_secundario.ejecutarSql();
			}
			
			tab_partida_poa.setCondicion("ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
			tab_partida_poa.ejecutarSql();
			tab_pre_pac.setCondicion("ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
			tab_pre_pac.ejecutarSql();
			tab_responsable_prec.setCondicion("ide_prcop is null and ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
			tab_responsable_prec.ejecutarSql();
			
			tab_seguimiento.setSql(ser_Seguimiento.getSeguimientoPorPrecontractual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre"))));
			//tab_seguimiento.setLectura(true);
			tab_seguimiento.dibujar();
			//tab_seguimiento.setLectura(false);
		}
		
		if(tab_precontractual.getValor("ide_prpre")!=null){
			mostrarNotificacionActividad(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			
		}
		
		String titulo="MIS PROCESOS ";
		
		if(int_opcion==2)
			titulo+="RECIBIDOS";
		
		if(int_opcion==3)
			titulo+="ENVIADOS";
		
		pan_opcion.setTitle(titulo);
		pan_opcion.getChildren().add(div_division2);

	}
	
	public void seleccionarProceso(SelectEvent evt) {
		tab_precontractual.seleccionarFila(evt);
		seleccionarProceso(tab_precontractual.getValorSeleccionado());
	}
	
	public void seleccionarProceso(String getValorSeleccionado ) 
	{
		tab_documento_requisito.ejecutarValorForanea(getValorSeleccionado);
		tab_seguimiento.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
		
		/*if(tab_documento_requisito.getTotalFilas()>0)
		{
			tab_documento_requisito_secundario.setCondicion("con_ide_prdoc="+tab_documento_requisito.getValor("ide_prdoc"));
			tab_documento_requisito_secundario.ejecutarSql();
		}*/
		tab_partida_poa.setCondicion("ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
		tab_partida_poa.ejecutarSql();
		tab_pre_pac.setCondicion("ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
		tab_pre_pac.ejecutarSql();
		tab_responsable_prec.setCondicion("ide_prcop is null and ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
		tab_responsable_prec.ejecutarSql();

		validarTareas(true);

	}
	
	public void seleccionarProceso(AjaxBehaviorEvent evt) {

	}
	
	public void importarEmpleado(){
		if (tab_pre_pac.isEmpty()) {
			utilitario.agregarMensajeInfo("No existe PAC", "");
			return;
		}
							
		set_empleado.getTab_seleccion().setSql(ser_nomina.servicioEmpleadoContrato("true"));
		set_empleado.getBot_aceptar().setMetodo("aceptarEmpleado");
		set_empleado.getTab_seleccion().ejecutarSql();
		set_empleado.dibujar();
	}
	
	public void aceptarEmpleado(){
		//String str_seleccionados=set_empleado.getSeleccionados();
		String str_seleccionados=set_empleado.getValorSeleccionado();
		if(str_seleccionados!=null){
			//Inserto los empleados seleccionados en la tabla de resposable d econtratacion 
			TablaGenerica tab_empleado_responsable = ser_nomina.ideEmpleadoContrato(str_seleccionados,"true");	
			//tab_empleado_responsable.imprimirSql();
				
			for(int i=0;i<tab_responsable_prec.getTotalFilas();i++){	
				tab_responsable_prec.setValor(i,"activo_prrec", "false");	
				tab_responsable_prec.modificar(i);//para que haga el update
			}
			
			for(int i=0;i<tab_empleado_responsable.getTotalFilas();i++){
				tab_responsable_prec.insertar();
				tab_responsable_prec.setValor("IDE_GEEDP", tab_empleado_responsable.getValor(i, "IDE_GEEDP"));			
				tab_responsable_prec.setValor("IDE_GTEMP", tab_empleado_responsable.getValor(i, "IDE_GTEMP"));	
				tab_responsable_prec.setValor("ide_prpac", tab_precontractual.getValor("ide_prpac"));	
				tab_precontractual.setValor("responsable_actual_prpre",pckUtilidades.CConversion.CStr(tab_empleado_responsable.getValor(i, "PRIMER_NOMBRE_GTEMP"))+" "+pckUtilidades.CConversion.CStr(tab_empleado_responsable.getValor(i, "APELLIDO_PATERNO_GTEMP")));
				tab_precontractual.setValor("ide_actual_geedp",pckUtilidades.CConversion.CStr(tab_empleado_responsable.getValor(i, "ide_gtemp")));
				tab_precontractual.setValor("ide_usuario_iniciop_prpre",pckUtilidades.CConversion.CStr(tab_empleado_responsable.getValor(i, "IDE_GTEMP")));
				tab_precontractual.modificar(tab_precontractual.getFilaActual());
			}
			set_empleado.cerrar();
			tab_precontractual.guardar();
			tab_responsable_prec.guardar();
			guardarPantalla();
			/*
			if(tab_documento_requisito.getTotalFilas()>0)
				generarActualizacionDesignacionTecnico(tab_precontractual.getValor("ide_prpac"));
			else
				generarDesignacionTecnico(tab_precontractual.getValor("ide_prpac"));
		    */
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
	}
	
	public void exportarExcel(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		if(tab_documento_requisito.getTotalFilas()<1){
			utilitario.agregarMensajeInfo("Sin registros", "");
			//return;			
		}
		if (tab_pre_pac.isEmpty()) {
			utilitario.agregarMensajeInfo("No existe PAC", "");
			return;
		}

		ser_fui_xls.exportarFUI(tab_precontractual.getValor("ide_prpre"), tab_precontractual.getValorArreglo(tab_precontractual.getFilaActual(), "ide_pretp", 1), 
								tab_precontractual.getValor("descripcion_prpre"),
								tab_precontractual.getValor("responsable_actual_prpre"),
								tab_precontractual.getValor("departamento_actual_prpre"),
								false);
		System.out.println("FUI Exportado exitosamente...");
	}
	
	public void generarArchivo(){	
		try {
			
			if(tab_precontractual.getTotalFilas()<=0)
			{
				utilitario.agregarMensajeInfo("Selecione un Proceso", "");
				return;
			}
			
			if (tab_pre_pac.isEmpty()) {
				utilitario.agregarMensajeInfo("No existe PAC", "");
				return;
			}
			
			TablaGenerica tab_archivos=utilitario.consultar("SELECT ide_prdoc,nro_documento_prdoc, descripcion_prreq, documentoadjunto_prdoc, descripcion_prdoc, con_ide_prdoc "
															+" FROM precon_documento_requisito pdr "
															+" left join precon_requisito pr on pr.ide_prreq=pdr.ide_prreq "
															+" where activo_prdoc=true and ide_prpre= "+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre"))
															+" order by 1 ");
			
			if(tab_archivos.getTotalFilas()<=0)
			{
				utilitario.agregarMensajeInfo("SIN ARCHIVOS DEL PROCESO", "");
				return;
			}
			
			String fileName = tab_precontractual.getValor("descripcion_prpre");
			fileName=pckUtilidades.CConversion.CStr(fileName);
			fileName=pckUtilidades.Utilitario.quitarCaracteresSpeciales(fileName);
			List<File> anexosPrincipales=new ArrayList();
			int item=1;
			for(int i=0;i<tab_archivos.getTotalFilas();i++){
				
				if(pckUtilidades.CConversion.CStr(tab_archivos.getValor(i, "documentoadjunto_prdoc")).length()>4)
				{
					String ruta=utilitario.getPropiedad("rutaDownload")+(tab_archivos.getValor(i, "documentoadjunto_prdoc"));	
					//String nombre=tab_archivos.getValor(i, "etapa_prdoc");s 
					String nombre="";
					if(pckUtilidades.CConversion.CInt(tab_archivos.getValor(i, "con_ide_prdoc"))>0)	
						nombre=pckUtilidades.CConversion.CStr(tab_archivos.getValor(i, "nro_documento_prdoc")) + " - "+pckUtilidades.CConversion.CStr(tab_archivos.getValor(i, "descripcion_prdoc"));
					else
						nombre=pckUtilidades.CConversion.CStr(tab_archivos.getValor(i, "nro_documento_prdoc")) + " - "+pckUtilidades.CConversion.CStr(tab_archivos.getValor(i, "descripcion_prreq"));
					
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
	
	public void devolver()
	{
		if (tab_pre_pac.isEmpty()) {
			utilitario.agregarMensajeInfo("No existe PAC", "");
			return;
		}
		
		priv_ide_prpre=pckUtilidades.CConversion.CStr(tab_precontractual.getValor("ide_prpre"));
		tipoContratacion=pckUtilidades.CConversion.CStr(tab_precontractual.getValor("termino_especificacion_prpre"));
		
		rad_si_no.setValue("NO");
		are_txt_notas.setValue("");
		gri_aprobar.getChildren().clear();
		gri_aprobar.getChildren().add(new Etiqueta("Seleccione una opción: "));
		gri_aprobar.getChildren().add(rad_si_no);		
		gri_aprobar.getChildren().add(new Etiqueta("Ingrese alguna observación: "));
		gri_aprobar.getChildren().add(are_txt_notas);	
		
		dia_aprobar.setTitle("¿ESTA COMPLETA LA DOCUMENTACIÓN/ACTIVIDAD?");
		gri_aprobar.getChildren().add(new Etiqueta("En el caso de que no cumpla con los requisitos, Seleccione la actividad a devolver: "));
		com_actividadesR.setCombo("SELECT ide_prrut, descripcion_preta from precon_ruta pr "
								+" join (select ide_pretp,descripcion_preta from precon_etapa_procedimiento pep,precon_etapa pet where activo_pretp=true and pep.ide_preta=pet.ide_preta) pe on pe.ide_pretp=pr.pre_ide_pretp "
								+" where coalesce(activo_prrut,false)=true and pr.ide_pretp="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))
								+" order by 1");
		
		gri_aprobar.getChildren().add(com_actividadesR);	

		//utilitario.addUpdate("dia_aprobar");					
		dia_aprobar.dibujar();
	}
	
	public void reasignar()
	{
		if (tab_pre_pac.isEmpty()) {
			utilitario.agregarMensajeInfo("No existe PAC", "");
			return;
		}
							
		set_empleado.getTab_seleccion().setSql(ser_nomina.servicioEmpleadoContrato("true"));
		set_empleado.getBot_aceptar().setMetodo("reasignarEmpleado");
		set_empleado.getTab_seleccion().ejecutarSql();
		set_empleado.dibujar();
	}
	
	public void reasignarEmpleado()
	{
		String str_seleccionados=set_empleado.getValorSeleccionado();
		if(str_seleccionados!=null)
		{
			TablaGenerica tab_empleado_responsable = ser_nomina.ideEmpleadoContrato(str_seleccionados,"true");	
			//tab_empleado_responsable.imprimirSql();				
			for(int i=0;i<tab_empleado_responsable.getTotalFilas();i++){					
				tab_precontractual.setValor("responsable_actual_prpre",pckUtilidades.CConversion.CStr(tab_empleado_responsable.getValor(i, "PRIMER_NOMBRE_GTEMP"))+" "+pckUtilidades.CConversion.CStr(tab_empleado_responsable.getValor(i, "APELLIDO_PATERNO_GTEMP")));
				tab_precontractual.setValor("ide_actual_geedp",pckUtilidades.CConversion.CStr(tab_empleado_responsable.getValor(i, "ide_gtemp")));
				tab_precontractual.setValor("estado_proceso_prpre","ENVIADO");
				//tab_precontractual.setValor("ide_usuario_iniciop_prpre",pckUtilidades.CConversion.CStr(tab_empleado_responsable.getValor(i, "IDE_GTEMP")));
				tab_precontractual.modificar(tab_precontractual.getFilaActual());
			}
			set_empleado.cerrar();
			tab_precontractual.guardar();
			guardarPantalla();			
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
	}
	
	/**
	 * Carga la actividad actual seleccionada en el popup
	 */
	public void aceptarActividad(){

		if(set_pac.getValorSeleccionado()!=null){
			
			TablaGenerica tab_pac=utilitario.consultar("Select * from pre_pac where ide_prpac=" + pckUtilidades.CConversion.CInt(set_pac.getValorSeleccionado()));
			//tab_pac.imprimirSql();
			TablaGenerica tab_tipo_compra=utilitario.consultar("Select ide_cotio as codigo,ide_cotio,tipo_cotio from cont_tipo_compra where ide_cotio=" + pckUtilidades.CConversion.CInt(tab_pac.getValor("ide_cotio")));
			//tab_tipo_compra.imprimirSql();
			TablaGenerica tab_procedimiento=utilitario.consultar("Select ppr.ide_prpro as codigo, ppr.ide_prpro,ide_adtic,ide_pretp,descripcion_preta,descripcion_prpro,descripcion_prfas "
																+" from precon_etapa_procedimiento pep "
																+" join precon_procedimiento ppr on ppr.ide_prpro=pep.ide_prpro "
																+" join precon_fase pfa on pfa.ide_prfas=ppr.ide_prfas "
																+" join precon_etapa pet on pep.ide_preta=pet.ide_preta "
																+" where pet.iniciosegumiento_preta='SI' and pep.activo_pretp=true and ide_adtic=" + pckUtilidades.CConversion.CInt(tab_pac.getValor("ide_adtic")));
			TablaGenerica tab_responsable = utilitario.consultar(ser_precontractual.responsablePAC(pckUtilidades.CConversion.CInt(set_pac.getValorSeleccionado())));
			tab_responsable.imprimirSql();						
			if(tab_responsable.getTotalFilas()!=1)
			{
				utilitario.agregarMensajeInfo("El proceso no tiene un responsable activo...", "");
				return;
			}
			
			tab_precontractual.insertar();
			tab_precontractual.setValor("responsable_actual_prpre",pckUtilidades.CConversion.CStr(tab_responsable.getValor("NOMBRES_APELLIDOS")));
			tab_precontractual.setValor("ide_actual_geedp",pckUtilidades.CConversion.CStr(tab_responsable.getValor("ide_gtemp")));
			tab_precontractual.setValor("ide_usuario_iniciop_prpre",pckUtilidades.CConversion.CStr(tab_responsable.getValor("ide_gtemp")));
			tab_precontractual.setValor("ide_geare",pckUtilidades.CConversion.CStr(tab_pac.getValor("ide_geare")));
			tab_precontractual.setValor("ide_prpac", pckUtilidades.CConversion.CStr(tab_pac.getValor("ide_prpac")));
			tab_precontractual.setValor("ide_pretp", pckUtilidades.CConversion.CStr(tab_procedimiento.getValor("ide_prpro")));
			tab_precontractual.setValor("termino_especificacion_prpre", pckUtilidades.CConversion.CStr(tab_tipo_compra.getValor("tipo_cotio")));
			tab_precontractual.setValor("descripcion_prpre", pckUtilidades.CConversion.CStr(tab_pac.getValor("descripcion_prpac")));	
			tab_precontractual.setValor("monto_prpre", pckUtilidades.CConversion.CStr(tab_pac.getValor("valor_total_prpac")));
			tipoContratacion=pckUtilidades.CConversion.CStr(tab_precontractual.getValor("termino_especificacion_prpre"));
			//tab_precontractual.modificar(tab_precontractual.getFilaActual());//para que haga el update	
			
			TablaGenerica tab_etapa_procedimiento=utilitario.consultar("SELECT ide_pretp, ide_prpro, ide_preta, ide_geare FROM precon_etapa_procedimiento where ide_pretp="+pckUtilidades.CConversion.CInt(tab_procedimiento.getValor("ide_pretp")));
			//tab_etapa_procedimiento.imprimirSql();
			cargarEtapaInicial(pckUtilidades.CConversion.CInt(tab_etapa_procedimiento.getValor("ide_prpro")),pckUtilidades.CConversion.CInt(tab_etapa_procedimiento.getValor("ide_preta")));
			cargarInfoActividad();
			set_pac.cerrar();
			tab_precontractual.guardar();
			guardarPantalla();
			
			//tab_precontractual.setFilaActual(tab_precontractual.getFilaActual());
			seleccionarProceso(tab_precontractual.getValorSeleccionado());
			/*
			if(pckUtilidades.CConversion.CInt(p_precon_automatico)==1)
				if(tab_responsable_prec.getTotalFilas()>0)
					generarDesignacionTecnico(set_pac.getValorSeleccionado());
				else
					utilitario.agregarNotificacionInfo("No se puede generar la designación", "No existe un responsable para el proceso de contratación, favor utilice el boton agregar responsable...");
			*/
		}else{
			utilitario.agregarMensajeInfo("Debe seleccionar una actividad.", "");
		}

	}
	
	/**
	 * Cancela el poppup de seleccion de actividad actual
	 */
	public void cancelarActividad(){
		tab_precontractual.ejecutarSql();
		set_pac.cerrar();
	}

	public void insertarProceso()
	{
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;
		}
		
		if(int_opcion==3)
		{
			utilitario.agregarNotificacionInfo("Registro No Editable", "Proceso en bandeja de enviados... Si el problema persiste contactese con el Administrador...");
			return;
		}
		
		String ide_gtempxx=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		String responsable_actual=ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(ide_gtempxx));
		TablaGenerica tab_responsable= utilitario.consultar(responsable_actual);
		String departamento_actual=ser_generalAdm.servicioDepartamento("true", ide_gtempxx);
		TablaGenerica tab_departamento= utilitario.consultar(departamento_actual);
		String codigo_secuencia=ser_EtapaProcedimiento.getProcedimientos();
		TablaGenerica tab_codigo_secuencia=utilitario.consultar(codigo_secuencia);		
		tab_precontractual.getColumna("ide_actual_geedp").setValorDefecto(tab_responsable.getValor("ide_gtemp"));
		tab_precontractual.getColumna("ide_usuario_iniciop_prpre").setValorDefecto(tab_responsable.getValor("ide_gtemp"));
		tab_precontractual.getColumna("responsable_actual_prpre").setValorDefecto(tab_responsable.getValor("empleado"));
		tab_precontractual.getColumna("departamento_actual_prpre").setValorDefecto(tab_departamento.getValor("DETALLE_GEDEP"));
		tab_precontractual.getColumna("fecha_prpre").setValorDefecto(utilitario.getFechaActual());
		tab_precontractual.getColumna("hora_prpre").setValorDefecto(utilitario.getHoraActual());
		tab_precontractual.getColumna("ide_pretp").setValorDefecto("1");
		tab_precontractual.getColumna("codigo_prpre").setValorDefecto(tab_codigo_secuencia.getValor("codigo_secuencia_prpro")+"-"+ser_precontractual.numeroSecuencial(p_sec_precontractual));
		tab_precontractual.getColumna("estado_proceso_prpre").setValorDefecto("REGISTRADO");

		//set_tipo_actividad.getTab_seleccion().setSql(ser_EtapaProcedimiento.getEtapaInicioSeguimientoParalelo("SI"));		
		set_pac.getTab_seleccion().setSql(ser_presupuesto.getLineasPac(com_anio.getValue().toString(),tab_departamento.getValor("IDE_GEARE"),tecnico,empleado));
		set_pac.getTab_seleccion().ejecutarSql();
		set_pac.dibujar();
		
		tab_documento_requisito_secundario.setCondicion("con_ide_prdoc=-1");
		tab_documento_requisito_secundario.ejecutarSql();
			
		//cargarEtapaInicial(1,pckUtilidades.CConversion.CInt(set_tipo_actividad.getValorSeleccionado()));
		//cargarEtapaInicial(1,1);
	}
	
	public void insertarRequisito()
	{
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;
		}
		
		if(int_opcion==3)
		{
			utilitario.agregarNotificacionInfo("Registro No Editable", "Proceso en bandeja de enviados... Si el problema persiste contactese con el Administrador...");
			return;
		}
		
		utilitario.agregarMensaje("Cargar Archivo", "Utilice el boton Cargar Archivo");
		
		//List perfilUsuarioConectado=utilitario.getConexion().consultar(ser_generalAdm.getPerfilConectado(utilitario.getVariable("IDE_PERF")));
		
		String requisitoNulo="";
		String ide_noRegistrado="";
		Integer contadorRequisito=0;
		TablaGenerica tab_etapa_req = null;
		tab_documento_requisito.getColumna("DE_NOMBRE_PRDOC").setValorDefecto(tab_precontractual.getValor("responsable_actual_prpre"));
		String ser_etapa_requisitos=ser_Requisito.getRequisitoPorEtapa(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
		TablaGenerica tab_requisitoPrecontractual=utilitario.consultar(ser_etapa_requisitos);
		if(tab_documento_requisito.getValor("ide_prreq") != null){
			for(int i = 0; i < tab_requisitoPrecontractual.getTotalFilas(); i++){
				tab_documento_requisito.setValor("ide_prreq", tab_requisitoPrecontractual.getValor(i, "ide_prreq"));
				tab_documento_requisito.setValor("fecha_presenta_prdoc", utilitario.getFechaActual());
				String ser_etapa_req=ser_etapa_requisito.noRegistradoEnRequisitoPrecontractualYEtapa(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre")), pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")), pckUtilidades.CConversion.CInt(tab_documento_requisito.getValor("ide_prreq")));
				tab_etapa_req=utilitario.consultar(ser_etapa_req);
				if(!tab_etapa_req.isEmpty()){
					contadorRequisito=contadorRequisito+1;
				}else{
					ide_noRegistrado=tab_requisitoPrecontractual.getValor(i, "ide_prreq");
				}
				
			}
			if(contadorRequisito==tab_requisitoPrecontractual.getTotalFilas()){
				tab_documento_requisito.getColumna("ide_prreq").setValorDefecto("Null");
				requisitoNulo="NULO";
			}else{
				tab_documento_requisito.getColumna("ide_prreq").setValorDefecto(ide_noRegistrado);
			}
			if(requisitoNulo!="NULO"){
				utilitario.addUpdateTabla(tab_documento_requisito, "ide_prreq", "");
				tab_documento_requisito.getColumna("fecha_presenta_prdoc").setValorDefecto(utilitario.getFechaActual());
				String etapa_actual=cargarEtapaActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
				tab_documento_requisito.getColumna("etapa_prdoc").setValorDefecto(etapa_actual);
				if(validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR") || validarPerfil(perfilUsuarioConectado,"AVANZADO")){
					tab_documento_requisito.getColumna("fecha_presenta_prdoc").setLectura(false);
				}
				tab_documento_requisito.insertar();
				//invocar el panel de cargar archivo
				
				tab_documento_requisito_secundario.setCondicion("con_ide_prdoc=-1");
				tab_documento_requisito_secundario.ejecutarSql();
				
			}
			else
				tab_documento_requisito.modificar(tab_documento_requisito.getFilaActual());
			
		}else{
			String ser_prreq=ser_Requisito.getRequisitoPorEtapa(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			TablaGenerica tab_prreq=utilitario.consultar(ser_prreq);
			tab_documento_requisito.getColumna("ide_prreq").setValorDefecto(tab_prreq.getValor("ide_prreq"));
			tab_documento_requisito.getColumna("fecha_presenta_prdoc").setValorDefecto(utilitario.getFechaActual());
			if(validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR") || validarPerfil(perfilUsuarioConectado,"AVANZADO")){
				tab_documento_requisito.getColumna("fecha_presenta_prdoc").setLectura(false);
			}
			String etapa_actual=cargarEtapaActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			tab_documento_requisito.getColumna("etapa_prdoc").setValorDefecto(etapa_actual);
			tab_documento_requisito.insertar();
		}
	}
	
	public void insertarRequisitoSecundario()
	{
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;
		}
		
		if(int_opcion==3)
		{
			utilitario.agregarNotificacionInfo("Registro No Editable", "Proceso en bandeja de enviados... Si el problema persiste contactese con el Administrador...");
			return;
		}
		
		if(tab_documento_requisito.getTotalFilas()<1)
		{
			utilitario.agregarNotificacionInfo("No se puede insertar", "Seleccione un Requisito antes de continuar...");
			return;
		}
		
		if(pckUtilidades.CConversion.CInt(tab_documento_requisito.getValor("ide_prdoc"))<1)
		{
			utilitario.agregarNotificacionInfo("No se puede insertar", "Guarde el Requisito antes de continuar...");
			return;
		}
		
		int ide_actual_preta=pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"));
		TablaGenerica tab_precon_documento_requisito = utilitario.consultar("SELECT ide_prdoc as codigo,ide_prdoc, ide_prpre, ide_prreq, etapa_prdoc FROM precon_documento_requisito "
				+ " where ide_prpre="+tab_precontractual.getValor("ide_prpre")+" and con_ide_prdoc is null and ide_prreq in (SELECT ide_prreq FROM precon_requisito where ide_preta="+ide_actual_preta+")");
		//tab_precon_documento_requisito.imprimirSql();
		if(tab_precon_documento_requisito.getTotalFilas()<1)
		{
			utilitario.agregarNotificacionInfo("No se puede insertar", "Cargue el requisito previo de cargar el anexo...");
			return;
		}
		
		if(pckUtilidades.CConversion.CInt(tab_documento_requisito.getValor("ide_prdoc"))!= pckUtilidades.CConversion.CInt(tab_precon_documento_requisito.getValor("ide_prdoc")))
		{
			utilitario.agregarNotificacionInfo("No se puede insertar", "Ubiquese en el requisito solicitado: "+tab_precon_documento_requisito.getValor("etapa_prdoc"));
			return;
		}
		
		utilitario.agregarMensaje("Cargar Archivo", "Utilice el boton Cargar Archivo");
		
		//tab_documento_requisito.fin();
		
		tab_documento_requisito_secundario.insertar();
		tab_documento_requisito_secundario.setValor("con_ide_prdoc", tab_documento_requisito.getValor("ide_prdoc"));
		tab_documento_requisito_secundario.setValor("ide_prpre", tab_documento_requisito.getValor("ide_prpre"));
		tab_documento_requisito_secundario.setValor("ide_prreq", tab_documento_requisito.getValor("ide_prreq"));
		tab_documento_requisito_secundario.setValor("etapa_prdoc", tab_documento_requisito.getValor("etapa_prdoc"));
		//tab_documento_requisito_secundario.setValor("descripcion_prdoc", "sin nombre");

	}
	
	public void cargarInfoActividad()
	{
		TablaGenerica tab_tareas= utilitario.consultar(ser_etapa.getEtapaTareas(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))));
		//tab_tareas.imprimirSql();
		
		eti_descripcion.setValue("N/A");
		eti_notas.setValue("N/A");
		bot_plantilla.setValue("N/A");
		bot_plantilla.getChildren().clear();
		
		if (tab_tareas.getTotalFilas()>0){
			eti_descripcion.setValue(tab_tareas.getValor("tarea_preta"));
			eti_notas.setValue(tab_tareas.getValor("notas_preta"));
			
			if(pckUtilidades.CConversion.CStr(tab_tareas.getValor("plantilla_preta")).length()>4)
			{
				bot_plantilla.getChildren().add(new Etiqueta(tab_tareas.getValor("descripcion_preta")));
				bot_plantilla.setValue(utilitario.getPropiedad("rutaDownload")+tab_tareas.getValor("plantilla_preta"));
				
			}
		}
		
		utilitario.addUpdate("div_division,eti_descripcion,eti_notas,bot_plantilla");
	}
	
	/**
	 * Metodo que permite cargar la informacion de acuerdo al usuario conectado o asignado a la actividad
	 */
	public void cargarInformacionDependiendoAreaYUsuario(){
		try {

			String sqlAnio="-1";
			tecnico=false;
			if(com_anio.getValue()!=null){
				sqlAnio=" extract(year from fecha_prpre)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") and ";				
			}
			else{
				utilitario.agregarMensajeInfo("Selecione un año", "");
				return;
			}
			
			//List perfilUsuarioConectado=utilitario.getConexion().consultar(ser_generalAdm.getPerfilConectado(utilitario.getVariable("IDE_PERF")));
						
			if(validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR DE LA APLICACION PRECONTRACTUAL") || validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR")){
				System.out.println("cargarInformacionDependiendoAreaYUsuario: ADMINISTRADOR DE LA APLICACION PRECONTRACTUAL");
				if(validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR"))
					tab_precontractual.setCondicion(sqlAnio+"1=1");
				else
					tab_precontractual.setCondicion(sqlAnio+"activo_prpre=true");
				
				tab_precontractual.ejecutarSql();
				tab_documento_requisito.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
				if(tab_documento_requisito.getTotalFilas()>0)
				{
					tab_documento_requisito_secundario.setCondicion("con_ide_prdoc="+tab_documento_requisito.getValor("ide_prdoc"));
					tab_documento_requisito_secundario.ejecutarSql();
				}
				tab_partida_poa.setCondicion("ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
				tab_partida_poa.ejecutarSql();
				tab_pre_pac.setCondicion("ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
				tab_pre_pac.ejecutarSql();
				tab_responsable_prec.setCondicion("ide_prcop is null and ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
				tab_responsable_prec.ejecutarSql();
				tab_seguimiento.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
				utilitario.addUpdate("tab_precontractual");
			}else 
			if(validarPerfil(perfilUsuarioConectado,"PRECONTRACTUAL AVANZADO")){
				System.out.println("cargarInformacionDependiendoAreaYUsuario: PRECONTRACTUAL AVANZADO");
				String ide_gtempxx=pckUtilidades.CConversion.CInt(ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp"))+"";
				TablaGenerica tab_empleado=utilitario.consultar(ser_nomina.servicioEmpleadoContratoCodigo("true",ide_gtempxx));
				String ide_geare="0";
				if(tab_empleado.getTotalFilas()>0)
					ide_geare=pckUtilidades.CConversion.CInt(tab_empleado.getValor("ide_geare"))+"";
				
				if(int_opcion==1)
					tab_precontractual.setCondicion(sqlAnio+"activo_prpre=true and ide_geare="+ide_geare);
				if(int_opcion==2)
					tab_precontractual.setCondicion(sqlAnio+"activo_prpre=true and estado_proceso_prpre like 'ENVIADO' and ide_actual_geedp="+ide_gtempxx);
				if(int_opcion==3)
					tab_precontractual.setCondicion(sqlAnio+"activo_prpre=true and estado_proceso_prpre like 'ENVIADO' and ide_usuario_iniciop_prpre="+ide_gtempxx+" and ide_geare="+ide_geare);
				
				tab_precontractual.ejecutarSql();
				//tab_precontractual.imprimirSql();
				tab_documento_requisito.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
				if(tab_documento_requisito.getTotalFilas()>0)
				{
					tab_documento_requisito_secundario.setCondicion("con_ide_prdoc="+tab_documento_requisito.getValor("ide_prdoc"));
					tab_documento_requisito_secundario.ejecutarSql();
				}
				tab_partida_poa.setCondicion("ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
				tab_partida_poa.ejecutarSql();
				tab_pre_pac.setCondicion("ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
				tab_pre_pac.ejecutarSql();
				tab_responsable_prec.setCondicion("ide_prcop is null and ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
				tab_responsable_prec.ejecutarSql();
				tab_seguimiento.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
				utilitario.addUpdate("tab_precontractual");
			}
			else
			{
				System.out.println("cargarInformacionDependiendoAreaYUsuario: AREA REQUIRIENTE");
				String ide_gtempxx=pckUtilidades.CConversion.CInt(ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp"))+"";
				tecnico=true;
				if(int_opcion==1)
					tab_precontractual.setCondicion(sqlAnio+"activo_prpre=true and estado_proceso_prpre in ('REGISTRADO','EN PROCESO') and ide_usuario_iniciop_prpre="+ide_gtempxx+" and coalesce(ide_actual_geedp,"+ide_gtempxx+")="+ide_gtempxx+" ");
				if(int_opcion==2)
					tab_precontractual.setCondicion(sqlAnio+"activo_prpre=true and estado_proceso_prpre like 'ENVIADO' and ide_actual_geedp="+ide_gtempxx+"");
				if(int_opcion==3)
					tab_precontractual.setCondicion(sqlAnio+"activo_prpre=true and estado_proceso_prpre like 'ENVIADO' and ide_usuario_iniciop_prpre="+ide_gtempxx+"");
				
				tab_precontractual.ejecutarSql();
				
				if(tab_precontractual.getTotalFilas()>0)
				{
					tab_precontractual.setFilaActual(pckUtilidades.CConversion.CStr(tab_precontractual.getValor(0,"ide_prpre")));
					tab_precontractual.setFocus();
				}
				
				//tab_precontractual.imprimirSql();
				tab_documento_requisito.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
				if(tab_documento_requisito.getTotalFilas()>0)
				{
					tab_documento_requisito_secundario.setCondicion("con_ide_prdoc="+tab_documento_requisito.getValor("ide_prdoc"));
					tab_documento_requisito_secundario.ejecutarSql();
				}
				tab_partida_poa.setCondicion("ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
				tab_partida_poa.ejecutarSql();
				tab_pre_pac.setCondicion("ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
				tab_pre_pac.ejecutarSql();
				tab_responsable_prec.setCondicion("ide_prcop is null and ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
				tab_responsable_prec.ejecutarSql();
				tab_seguimiento.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
				utilitario.addUpdate("tab_precontractual");
			}
			
			cargarInfoActividad();
			
		} catch (Exception e) {
			System.out.println("Error cargarInformacionDependiendoAreaYUsuario: "+e);// TODO: handle exception
		}
		
	}
	/**
	 * Metodo que permite cargar la actividad para iniciar la fase preparatoria
	 */
	public void cargarEtapaInicial(Integer ide_prpro,Integer ide_preta){
		String ser_etapa_inicial=ser_EtapaProcedimiento.getEtapaInicioSeguimiento("SI",ide_preta);
		TablaGenerica tab_etapa_inicial= utilitario.consultar(ser_etapa_inicial);
		//tab_etapa_inicial.imprimirSql();
		//tab_precontractual.getColumna("ide_actual_preta").setValorDefecto(tab_etapa_inicial.getValor("ide_preta"));
		//tab_precontractual.getColumna("actividad_actual_prpre").setValorDefecto(tab_etapa_inicial.getValor("descripcion_preta"));
		tab_precontractual.setValor("ide_actual_preta", tab_etapa_inicial.getValor("ide_preta"));
		tab_precontractual.setValor("actividad_actual_prpre", tab_etapa_inicial.getValor("descripcion_preta"));
		//utilitario.addUpdateTabla(tab_precontractual,"ide_actual_preta","");
		//utilitario.addUpdateTabla(tab_precontractual,"actividad_actual_prpre","");
		mostrarCamposDeAcuerdoActividadActual(pckUtilidades.CConversion.CInt(tab_etapa_inicial.getValor("ide_preta")));
		
		String ide_etapa_enviar=cargarEtapaSiguienteAEnviar(ide_prpro,pckUtilidades.CConversion.CInt(tab_etapa_inicial.getValor("ide_preta")));
		//tab_precontractual.getColumna("ide_preta").setValorDefecto(ide_etapa_enviar);
		tab_precontractual.setValor("ide_preta", ide_etapa_enviar);
		//utilitario.addUpdateTabla(tab_precontractual,"ide_preta","");
		//tab_precontractual.insertar();
		//tab_precontractual.setValor("ide_pretp", ide_prpro+"");
		
		mostrarNotificacionActividad(ide_preta);
	}

	/**
	 * Metodo que permite cargar la siguiente actividad
	 * @param ide_procedimiento 
	 * @param ide_actividad_actual
	 */
	public String cargarEtapaSiguienteAEnviar(Integer ide_procedimiento,Integer ide_actividad_actual) {
		String ide_etapa_actual="";
		//Validar segun tipo de contratacion y monto para habilitar las actividades especiales de requisitos

		String ser_etapa_siguiente_enviar=ser_EtapaProcedimiento.getEtapaSiguiente(ide_procedimiento, ide_actividad_actual, tipoContratacion);
		TablaGenerica tab_ide_etapa_actual =utilitario.consultar(ser_etapa_siguiente_enviar);
		//tab_ide_etapa_actual.imprimirSql();
		ide_etapa_actual=tab_ide_etapa_actual.getValor("ide_preta");
		return ide_etapa_actual;
	}
	/**
	 * Metodo que permite cargar la actividad actual en el proceso
	 * @return
	 */
	public String cargarEtapaActual(Integer ide_preta){
		String etapa_actual="";
		String ser_etapa_actual=ser_etapa.getEtapa(ide_preta);
		TablaGenerica tab_etapa_actual=utilitario.consultar(ser_etapa_actual);
		etapa_actual=tab_etapa_actual.getValor("descripcion_preta");
		requiereAprobacion=pckUtilidades.CConversion.CBol(tab_etapa_actual.getValor("requiere_aprobacion_preta"));
		actividadOpcional=pckUtilidades.CConversion.CBol(tab_etapa_actual.getValor("opcional_preta"));
		return etapa_actual;
	}
	/**
	 * Metodo que verifica los requisitos necesarios de la actividad en proceso
	 * @param ide_prpre
	 * @param ide_preta
	 * @return
	 */
	public boolean verificarRequisitoActividadActual(Integer ide_prpre,Integer ide_preta, boolean opcional){
		if(!opcional)
		{
			Integer contadorNo=0;
			String ser_requisito=ser_Requisito.getRequisitoPorEtapa(ide_preta);
			String ser_etapa_requisitos=ser_etapa_requisito.getContarEtapaRequisitoPorPrecontractual(ide_prpre,ide_preta);
			List listRequisito=utilitario.getConexion().consultar(ser_requisito);
			List listRequisitoPrecontractual=utilitario.getConexion().consultar(ser_etapa_requisitos);
			if(listRequisitoPrecontractual.size()>=listRequisito.size()){
				TablaGenerica tab_etapa_presenta = utilitario.consultar(ser_etapa_requisitos);
				for(int i = 0; i < tab_etapa_presenta.getTotalFilas(); i++){
					tab_documento_requisito.setValor("presenta_prdoc", tab_etapa_presenta.getValor(i, "presenta_prdoc"));
					if(tab_documento_requisito.getValor("presenta_prdoc").equals("NO")){
						contadorNo=contadorNo+1;
					}
				}
				if(contadorNo>0){
					cumpleRequisitos=false;
				}else{
					cumpleRequisitos=true;
				}
			}else{
				cumpleRequisitos=false;
			}
			return cumpleRequisitos;
		}
		return true;
	}
	
	/**
	 * Metodo que permite enviar la actividad siguiente
	 */
	public void enviarEtapaSiguiente(){
		boolean finalizado=false;
		if(tab_precontractual.getValor("ide_prpre")!=null)
		{
			if(con_completar.isVisible())
			{
				con_completar.cerrar();
				priv_ide_prpre=pckUtilidades.CConversion.CStr(tab_precontractual.getValor("ide_prpre"));
				tipoContratacion=pckUtilidades.CConversion.CStr(tab_precontractual.getValor("termino_especificacion_prpre"));
				TablaGenerica tab_cargaEtapa=utilitario.consultar(ser_etapa.getEtapa(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))));
				//tab_cargaEtapa.imprimirSql();
				System.out.println("enviarEtapaSiguiente ide_actual_preta: "+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
				System.out.println("enviarEtapaSiguiente ide_preta: "+pckUtilidades.CConversion.CInt(tab_cargaEtapa.getValor("ide_preta")));
				enviarMailFirmado=false;
				
				if(verificarRequisitoActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre")),
                        pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")),
                        pckUtilidades.CConversion.CBol(tab_cargaEtapa.getValor("opcional_preta"))))
				{
					p_precon_automatico="0";
				}
				else
				{
					p_precon_automatico = utilitario.getVariable("p_precon_automatico");
					enviarMailFirmado=true;
					if(pckUtilidades.CConversion.CInt(tab_cargaEtapa.getValor("ide_preta"))==120) //120 solicitud poa
						if(set_lineas_poa.isVisible())
						{
							String str_seleccionados=set_lineas_poa.getSeleccionados();
							System.out.println("str_seleccionados: "+str_seleccionados);
							if (str_seleccionados!=null)
							{
								TablaGenerica tab_lineas_poa =utilitario.consultar("select ide_prpoa,activo_prpoa,ide_prcla,ide_prfup,presupuesto_inicial_prpoa from pre_poa where ide_prpoa in ("+str_seleccionados+")");
								TablaGenerica tab_lineas_poa_pac =utilitario.consultar("select ide_prpap,ide_prcla,ide_prpoa from pre_partida_pac where ide_prpoa in ("+str_seleccionados+") and ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));							
								
								String sql="update pre_partida_pac set activo_prpap=false, certificado_poa_prpap=false where ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac"));
								utilitario.getConexion().ejecutarSql(sql);
								System.out.println("actualizo false en pre_partida_pac: "+sql);
								
								boolean existe=false;
								for(int i=0;i<tab_lineas_poa.getTotalFilas();i++)
								{
									existe=false;
									for(int j=0;j<tab_lineas_poa_pac.getTotalFilas();j++)
									{
										if(pckUtilidades.CConversion.CInt(tab_lineas_poa.getValor(i,"ide_prpoa"))==pckUtilidades.CConversion.CInt(tab_lineas_poa_pac.getValor(j,"ide_prpoa")))
											existe=true;
									}
									
									if(!existe)
									{
										TablaGenerica tab_maximo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("pre_partida_pac", "ide_prpap"));
										String maximo_pre_partida_pac=tab_maximo.getValor("codigo");
										sql="insert into pre_partida_pac (ide_prpap,valor_prpap,activo_prpap,certificado_poa_prpap,ide_prcla,ide_prpoa,ide_prpac,fecha_ingre,hora_ingre) ";
										sql+=" values ( "+maximo_pre_partida_pac+",0,true,true,"+pckUtilidades.CConversion.CInt(tab_lineas_poa.getValor(i,"ide_prcla"))
														+","+pckUtilidades.CConversion.CInt(tab_lineas_poa.getValor(i,"ide_prpoa"))+","+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac"))+",now(),now() ); ";
										
										utilitario.getConexion().ejecutarSql(sql);
										System.out.println("inserto en pre_partida_pac: "+sql);
										
									}
									else
									{
										sql="update pre_partida_pac set activo_prpap=true, certificado_poa_prpap=true where ide_prpoa="+pckUtilidades.CConversion.CInt(tab_lineas_poa.getValor(i,"ide_prpoa"))+" and ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac"));
										utilitario.getConexion().ejecutarSql(sql);
										System.out.println("actualizo en pre_partida_pac: "+sql);
									}
								}
								
								generarSolicitudPOA(tab_precontractual.getValor("ide_prpac"), tab_precontractual.getValor("ide_pretp"), tab_precontractual.getValor("ide_preta"));
								set_lineas_poa.cerrar();
							}
							else
								utilitario.agregarMensajeInfo("Seleccione almenos una linea del POA.", "");
						}
						else
						{
							if(pckUtilidades.CConversion.CStr(tab_cargaEtapa.getValor("bien_servicio_preta")).contains("PG"))
							{
								set_lineas_poa.getTab_seleccion().setSql(ser_presupuesto.getPoa(com_anio.getValue().toString(),"true","true,false",pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_geare")), true));
								set_lineas_poa.getTab_seleccion().ejecutarSql();
								set_lineas_poa.dibujar();
								return;
							}
						}
					
				}
				
				System.out.println("p_precon_automatico: "+p_precon_automatico);
				if(pckUtilidades.CConversion.CInt(p_precon_automatico)==1) //valida si existe archivo cargado no entra
				{

					if(pckUtilidades.CConversion.CInt(tab_cargaEtapa.getValor("ide_preta"))==115 ) //115 Solicitar Stock de Bodega
						if(dia_solicitud.isVisible())
						{
							
							if(pckUtilidades.CConversion.CStr(are_txt_edit.getValue()).contains("font") && !pckUtilidades.CConversion.CStr(are_txt_edit.getValue()).contains("Times New Roman"))
							{
								System.out.println(pckUtilidades.CConversion.CStr(are_txt_edit.getValue()));
								utilitario.agregarMensajeInfo("No se puede continuar", "Favor quitar el estilo de fuente del texto...");
								return;
							}
							
							boolean exito=generarSolicitudStockBodega(tab_precontractual.getValor("ide_prpac"), tab_precontractual.getValor("ide_pretp"), tab_precontractual.getValor("ide_preta"), are_txt_edit.getValue()+"");
							if(!exito)
							{
								utilitario.agregarMensajeInfo("No se puede continuar", "Favor quitar el formato del texto");
								return;
							}
							dia_solicitud.cerrar();
						}
						else
						{
							dia_solicitud.setTitle("Solicitud de Stock de Bodega");
							dia_solicitud.setFooter("Especifique los items...");
							are_txt_edit.setValue("");	
							dia_solicitud.dibujar();
							return;
							
						}
					
					if(pckUtilidades.CConversion.CInt(tab_cargaEtapa.getValor("ide_preta"))==116 ) //116 Emitir Verificación Stock de bienes
						if(dia_solicitud.isVisible())
						{
							
							if(pckUtilidades.CConversion.CStr(are_txt_edit.getValue()).contains("font") && !pckUtilidades.CConversion.CStr(are_txt_edit.getValue()).contains("Times New Roman"))
							{
								System.out.println(pckUtilidades.CConversion.CStr(are_txt_edit.getValue()));
								utilitario.agregarMensajeInfo("No se puede continuar", "Favor quitar el estilo de fuente del texto...");
								return;
							}
							
							boolean exito=generarVerificacionStockBodega(tab_precontractual.getValor("ide_prpac"), tab_precontractual.getValor("ide_pretp"), tab_precontractual.getValor("ide_preta"), are_txt_edit.getValue()+"");
							if(!exito)
							{
								utilitario.agregarMensajeInfo("No se puede continuar", "Favor quitar el formato del texto");
								return;
							}
							dia_solicitud.cerrar();
						}
						else
						{
							dia_solicitud.setTitle("Verificación de Stock de Bodega");
							dia_solicitud.setFooter("Especifique su respuesta...");
							are_txt_edit.setValue("En atención al requerimiento; y, toda vez que la Unidad de Bienes verificó la existencia de los "
									+ "BIENES/EXISTENCIAS en la bodega institucional, me permito poner en su conocimiento que, "
									+ "actualmente no se cuenta con stock de los items requeridos.");	
							dia_solicitud.dibujar();
							return;
							
						}
					
					if(pckUtilidades.CConversion.CInt(tab_cargaEtapa.getValor("ide_preta"))==5 ) //5 Solicitar la Certificación productos normalizados en Catálogo Electrónico
					{
						
						if(pckUtilidades.CConversion.CInt(tab_pre_pac.getValor("cpc_prpac"))<1)
						{
							utilitario.agregarMensajeInfo("No se puede continuar", "El proceso no cuenta con un CPC");
							return;
						}
						
						if(pckUtilidades.CConversion.CStr(tab_pre_pac.getValor("denominacion_prpac")).length()<=4)
						{
							utilitario.agregarMensajeInfo("No se puede continuar", "El proceso no cuenta con la denomicacion del CPC");
							return;
						}
						
						if(dia_solicitud.isVisible())
						{
							
							if(pckUtilidades.CConversion.CStr(are_txt_edit.getValue()).contains("font") && !pckUtilidades.CConversion.CStr(are_txt_edit.getValue()).contains("Times New Roman"))
							{
								System.out.println(pckUtilidades.CConversion.CStr(are_txt_edit.getValue()));
								utilitario.agregarMensajeInfo("No se puede continuar", "Favor quitar el estilo de fuente del texto...");
								return;
							}
							boolean exito=generarSolicitudNormalizados(tab_precontractual.getValor("ide_prpac"), tab_precontractual.getValor("ide_pretp"), tab_precontractual.getValor("ide_preta"), are_txt_edit.getValue()+"");
							if(!exito)
							{
								utilitario.agregarMensajeInfo("No se puede continuar", "Favor quitar el formato del texto");
								return;
							}
							dia_solicitud.cerrar();
						}
						else
						{
							dia_solicitud.setTitle("Solicitud de Certificación productos normalizados en Catálogo Electrónico");
							dia_solicitud.setFooter("Especifique los items y sus CPCs...");
							are_txt_edit.setValue("");	
							dia_solicitud.dibujar();
							return;
							
						}
						
					}
					
					if(pckUtilidades.CConversion.CInt(tab_cargaEtapa.getValor("ide_preta"))==7 ) //7 Emitir Certificación de productos normalizados en Catálogo Electrónico
					{
						enviarMailFirmado=true;
						generarCertificacionNormalizados(tab_precontractual.getValor("ide_prpac"), tab_precontractual.getValor("ide_pretp"), tab_precontractual.getValor("ide_preta"),"certificacion_1.pdf");
					}
					
					if(pckUtilidades.CConversion.CInt(tab_cargaEtapa.getValor("ide_preta"))==20 ) //20 Solicitar Certificación Presupuestaria
					{
						if(dia_solicitud.isVisible())
						{
							
							if(pckUtilidades.CConversion.CStr(are_txt_edit.getValue()).contains("font") && !pckUtilidades.CConversion.CStr(are_txt_edit.getValue()).contains("Times New Roman"))
							{
								System.out.println(pckUtilidades.CConversion.CStr(are_txt_edit.getValue()));
								utilitario.agregarMensajeInfo("No se puede continuar", "Favor quitar el estilo de fuente del texto...");
								return;
							}
							boolean exito=generarSolicitudCertificacionPresupuestaria(tab_precontractual.getValor("ide_prpac"), tab_precontractual.getValor("ide_pretp"), tab_precontractual.getValor("ide_preta"), are_txt_edit.getValue()+"");
							if(!exito)
							{
								utilitario.agregarMensajeInfo("No se puede continuar", "Favor quitar el formato del texto");
								return;
							}
							dia_solicitud.cerrar();
						}
						else
						{
							dia_solicitud.setTitle("Solicitud de Certificación Presupuestaria");
							dia_solicitud.setFooter("Especifique su justificación...");
							are_txt_edit.setValue("Justificación:");	
							dia_solicitud.dibujar();
							return;
							
						}
						
					}
					
					if(pckUtilidades.CConversion.CInt(tab_cargaEtapa.getValor("ide_preta"))==117 ) //117 Solicitar Certificado de no Duplicidad
					{
						generarSolicitudNoDuplicidadConsultoria(tab_precontractual.getValor("ide_prpac"), tab_precontractual.getValor("ide_pretp"), tab_precontractual.getValor("ide_preta"));
					}
					
					if(pckUtilidades.CConversion.CInt(tab_cargaEtapa.getValor("ide_preta"))==118 ) //118 Emitir Certificado de no Duplicidad
					{
						enviarMailFirmado=true;
						generarCertificacionNoDuplicidadConsultoria(tab_precontractual.getValor("ide_prpac"), tab_precontractual.getValor("ide_pretp"), tab_precontractual.getValor("ide_preta"),"");
					}
	
					if(pckUtilidades.CConversion.CInt(tab_cargaEtapa.getValor("ide_preta"))==22 ) //22 Solicitar Certificación PAC
					{
						generarSolicitudPAC(tab_precontractual.getValor("ide_prpac"), tab_precontractual.getValor("ide_pretp"), tab_precontractual.getValor("ide_preta"));
					}
					
					if(pckUtilidades.CConversion.CInt(tab_cargaEtapa.getValor("ide_preta"))==23 ) //23 Emitir Certificación PAC
					{
						enviarMailFirmado=true;
						generarCertificacionPAC(tab_precontractual.getValor("ide_prpac"), tab_precontractual.getValor("ide_pretp"), tab_precontractual.getValor("ide_preta"),"certificacion_1.pdf");
					}
					
					if(pckUtilidades.CConversion.CInt(tab_cargaEtapa.getValor("ide_preta"))==141 ) //141 Solicitar verificación PAC
					{
						generarSolicitudVerificacionPAC(tab_precontractual.getValor("ide_prpac"), tab_precontractual.getValor("ide_pretp"), tab_precontractual.getValor("ide_preta"));
					}
					
					/*if(pckUtilidades.CConversion.CInt(tab_cargaEtapa.getValor("ide_preta"))==11 ) //11 Convocar mesa de trabajo y enviar a las áreas para revisión
					{
						if(dia_solicitud.isVisible())
						{
							//generarSolicitudConvocarMesa(tab_precontractual.getValor("ide_prpac"), tab_precontractual.getValor("ide_pretp"), tab_precontractual.getValor("ide_preta"), are_txt_solicitud.getValue()+"");
							dia_solicitud.cerrar();
						}
						else
						{
							dia_solicitud.setTitle("Solicitud de convocatoria a mesa de trabajo");
							dia_solicitud.setFooter("Lugar, fecha y hora de reunión");
							are_txt_solicitud.setValue("");	
							dia_solicitud.dibujar();
							return;
							
						}
					}*/
				
				}
				
				if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==119)
				{
					if(tab_documento_requisito_secundario.getTotalFilas()<3)
					{
						utilitario.agregarMensajeInfo("El estudio de mercado debe de contar con almenos 3 cotizaciones.", "Favor cargue las cotizaciones con el boton 'Subir Alcances/Certificados'... ");
						//return;
					}
				}
				
				guardar();
				if(tab_precontractual.getValor("estado_proceso_prpre").equals("REGISTRADO") || tab_precontractual.getValor("estado_proceso_prpre").equals("EN PROCESO") || tab_precontractual.getValor("estado_proceso_prpre").equals("ENVIADO")){
					finalizado=true;
				}
				
				if(tab_precontractual.getValor("estado_proceso_prpre").contains("NO APROBADO") || tab_precontractual.getValor("estado_proceso_prpre").contains("FINALIZADO"))
				{
					utilitario.agregarNotificacionInfo("ESTADO DEL PROCESO: ",tab_precontractual.getValor("estado_proceso_prpre"));			
					return;
				}
				
				if(!finalizado){
					utilitario.agregarMensajeInfo("El proceso ya ha finalizado, no se puede modificar.", "");
				}else {	
					
					rad_si_no.setValue("SI");	
					are_txt_notas.setValue("");
					eti_mensaje.setValue("");
					utilitario.addUpdate("rad_si_no,eti_mensaje,are_txt_notas");	
		
					if(pckUtilidades.CConversion.CBol(tab_cargaEtapa.getValor("requiere_aprobacion_preta")))
					{
						TablaGenerica tab_cargaRuta=utilitario.consultar("SELECT ide_prrut, pre_ide_pretp, pre_ide_pretp2, coalesce(direccion_prrut,'ENVIAR') as direccion_prrut from precon_ruta where coalesce(activo_prrut,false)=true and pre_ide_pretp in (select ide_pretp from precon_etapa_procedimiento where ide_preta="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))+") and ide_pretp="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")));
						tab_cargaRuta.imprimirSql(); //validar las tareas que pueden tener retornos

						gri_aprobar.getChildren().clear();
						gri_aprobar.getChildren().add(new Etiqueta("Seleccione una opción: "));
						gri_aprobar.getChildren().add(rad_si_no);		
						gri_aprobar.getChildren().add(new Etiqueta("Ingrese alguna observación: "));
						gri_aprobar.getChildren().add(are_txt_notas);	
						
						if(tab_cargaRuta.getTotalFilas()>0)
						{
							//gri_aprobar.getChildren().add(new Etiqueta("En el caso de que no cumpla con los requisitos, : "));
							dia_aprobar.setTitle("¿ESTA COMPLETA LA DOCUMENTACIÓN/ACTIVIDAD?");
							gri_aprobar.getChildren().add(new Etiqueta("En el caso de que no cumpla con los requisitos, Seleccione la actividad a devolver: "));
							com_actividadesR.setCombo("SELECT ide_prrut, descripcion_preta from precon_ruta pr "
													+" join precon_etapa pe on pe.ide_preta=pr.pre_ide_pretp "
													+" where coalesce(activo_prrut,false)=true and ide_pretp="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp"))
													+" order by 2");
							
							gri_aprobar.getChildren().add(com_actividadesR);	
							
						}

						utilitario.addUpdate("dia_aprobar");	
						//eti_mensaje.setValue(tab_cargaEtapa.getValor("descripcion_preta"));
						//utilitario.addUpdate("eti_mensaje");					
						dia_aprobar.dibujar();
						return;
					}
					else
					{
						guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(0, pckUtilidades.CConversion.CBol(tab_cargaEtapa.getValor("opcional_preta")));	
						guardarPantalla();
						boolean validarFocus=false;
						if(tab_precontractual.getTotalFilas()>0)
						{							
							for(int i = 0; i < tab_precontractual.getTotalFilas(); i++){
								if(pckUtilidades.CConversion.CStr(tab_precontractual.getValor(i,"ide_prpre")).equals(priv_ide_prpre))
								{
									validarFocus=true;
									break;
								}
							}
							//priv_ide_prpre=pckUtilidades.CConversion.CStr(tab_precontractual.getValor(0,"ide_prpre"));
						}
						
						if(!validarFocus)
							return;						
						
						tab_precontractual.setFilaActual(priv_ide_prpre);
						tab_precontractual.setFocus();
						utilitario.addUpdate("tab_precontractual");
						tab_documento_requisito.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
						if(tab_documento_requisito.getTotalFilas()>0)
						{
							tab_documento_requisito_secundario.setCondicion("con_ide_prdoc="+tab_documento_requisito.getValor("ide_prdoc"));
							tab_documento_requisito_secundario.ejecutarSql();
						}
						tab_partida_poa.setCondicion("ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
						tab_partida_poa.ejecutarSql();
						tab_pre_pac.setCondicion("ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
						tab_pre_pac.ejecutarSql();
						tab_responsable_prec.setCondicion("ide_prcop is null and ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
						tab_responsable_prec.ejecutarSql();
						tab_seguimiento.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
						validarTareas(true);
					}
							
				}

			}
			else
			{
				if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==24)
					con_completar.setMessage("¿Esta Seguro de Completar la tarea?, recuerde que en este proceso no hay reverso. Adicionalmente, favor revisar que todo el expediente este correctamente cargado al Sistema previo a solicitar el Informe Respectivo.");
				else
					if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==25)
						con_completar.setMessage("¿Esta Seguro de Completar la tarea?, recuerde que en este proceso no hay reverso. Adicionalmente, favor revisar que todo el expediente este correctamente cargado al Sistema previo a emitir el Informe Respectivo.");
					else
						con_completar.setMessage("¿Esta Seguro de Completar la tarea?, recuerde que en este proceso no hay reverso");
				
				con_completar.dibujar();
				utilitario.addUpdate("con_completar");				
			}
			
		}else{
			utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que todavía no hay un registro guardado.", "");
		}
	}
	
	public void aprobar()
	{		
		if(rad_si_no.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Tipo", "");
			return;			
		}
		
		if(pckUtilidades.CConversion.CStr(rad_si_no.getValue()).contains("SI"))
			guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(1, true);	
		else
		{
			if(pckUtilidades.CConversion.CInt(com_actividadesR.getValue())>0)
			{
				TablaGenerica precon_ruta = utilitario.consultar("Select ide_prrut,pe.ide_preta as pre_ide_pretp from precon_ruta pr "
																+ " join (select ide_pretp,pet.ide_preta, descripcion_preta from precon_etapa_procedimiento pep,precon_etapa pet where activo_pretp=true and pep.ide_preta=pet.ide_preta) pe on pe.ide_pretp=pr.pre_ide_pretp "
																+ " where ide_prrut="+pckUtilidades.CConversion.CInt(com_actividadesR.getValue()));
				
				guardarInformacionSeguimientoConValidacionRequisitoEnActividadDevuelta(3,pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")),
						pckUtilidades.CConversion.CInt(precon_ruta.getValor("pre_ide_pretp")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")),true);
						
			}
			else
				guardarSeguimiento(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_preta")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")),2);	
		}
			
		guardarPantalla();
		tab_precontractual.setFilaActual(priv_ide_prpre);
		tab_documento_requisito.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
		if(tab_documento_requisito.getTotalFilas()>0)
		{
			tab_documento_requisito_secundario.setCondicion("con_ide_prdoc="+tab_documento_requisito.getValor("ide_prdoc"));
			tab_documento_requisito_secundario.ejecutarSql();
		}
		tab_partida_poa.setCondicion("ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
		tab_partida_poa.ejecutarSql();
		tab_pre_pac.setCondicion("ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
		tab_pre_pac.ejecutarSql();
		tab_responsable_prec.setCondicion("ide_prcop is null and ide_prpac="+pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpac")));
		tab_responsable_prec.ejecutarSql();
		tab_seguimiento.ejecutarValorForanea(tab_precontractual.getValorSeleccionado());
		tab_precontractual.setFocus();
		validarTareas(true);
		dia_aprobar.cerrar();
		
	}

	/**
	 * Metodo que guarda la información de seguimiento de ese proceso validando si en esa actividad
	 * tiene que subir documentos y cumple con los mismos en actividad actual
	 */
	public void guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual(int tipoAprobacion, boolean actividadOpcional){
		int ide_actual_preta=pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"));
		if(verificarRequisitoActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre")),ide_actual_preta,actividadOpcional))
		{			
			//enviarMailFirmado=false;
			//enviar_correo=pckUtilidades.CConversion.CInt(p_precon_automatico)==1;
			//primero guardamos los datos actuales antes de enviar a la siguiente actividad
			guardarSeguimiento(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_preta")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")), tipoAprobacion);
	
			//Cargamos los datos nuevos de la etapa siguiente
			tab_precontractual.setValor("ide_actual_preta", tab_precontractual.getValor("ide_preta"));
			String descripcion_etapa_actual=cargarEtapaActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_preta")));
			tab_precontractual.setValor("actividad_actual_prpre", descripcion_etapa_actual);
			tab_precontractual.setValor("estado_proceso_prpre","EN PROCESO");
			tab_precontractual.setValor("fecha_prpre", utilitario.getFechaActual());
			tab_precontractual.setValor("hora_prpre", utilitario.getHoraActual());
			tab_precontractual.setValor("observacion_prpre", " ");
			
			String ide_geedp_enviar=null;
			if(pckUtilidades.CConversion.CInt(p_precon_automatico)==1 || ide_actual_preta==120 || ide_actual_preta==121)	//120 solicitud poa //121 emision poa
				ide_geedp_enviar=cargarIdUsuario(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_preta")));
			
			if(ide_geedp_enviar==null){
				//insertamos el usuario que inicio el proceso
				String ide_gtemp=tab_precontractual.getValor("ide_usuario_iniciop_prpre");
				String ide_departamento=ser_generalAdm.servicioDepartamento("true",ide_gtemp);
				TablaGenerica tab_ide_departamento= utilitario.consultar(ide_departamento);
				String responsable_actual=ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(ide_gtemp));
				TablaGenerica tab_responsable= utilitario.consultar(responsable_actual);
				tab_precontractual.setValor("ide_actual_geedp",ide_gtemp);
				tab_precontractual.setValor("responsable_actual_prpre",tab_responsable.getValor("empleado"));//RESPONSABLE ASIGNADO");
				tab_precontractual.setValor("departamento_actual_prpre",tab_ide_departamento.getValor("DETALLE_GEDEP"));//DEPARTAMENTO ASIGNADO");
			}else{
				String ide_departamento=ser_generalAdm.servicioDepartamentoYIdEmpleado(ide_geedp_enviar);
				TablaGenerica tab_ide_departamento= utilitario.consultar(ide_departamento);
				tab_ide_departamento.imprimirSql();
				String responsable_actual=ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(tab_ide_departamento.getValor("IDE_GTEMP")));
				TablaGenerica tab_responsable= utilitario.consultar(responsable_actual);
				tab_responsable.imprimirSql();
				tab_precontractual.setValor("estado_proceso_prpre","ENVIADO"); //enviar_correo=true;
				tab_precontractual.setValor("ide_actual_geedp",tab_ide_departamento.getValor("IDE_GTEMP"));
				tab_precontractual.setValor("responsable_actual_prpre",tab_responsable.getValor("empleado"));//RESPONSABLE ASIGNADO");
				tab_precontractual.setValor("departamento_actual_prpre",tab_ide_departamento.getValor("DETALLE_GEDEP"));//DEPARTAMENTO ASIGNADO");
			}
			tab_precontractual.modificar(tab_precontractual.getFilaActual());
			tipoContratacion=tipoContratacion.length()<1?"NA":tipoContratacion;
			//System.out.println("tipoContratacion: "+tipoContratacion);
			String ser_etapa_siguiente_enviar=cargarEtapaSiguienteAEnviar(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")),pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			tab_precontractual.setValor("ide_preta",ser_etapa_siguiente_enviar);
			tab_precontractual.guardar();
			tab_documento_requisito_secundario.guardar();
			guardarPantalla();
			utilitario.addUpdate("tab_precontractual");
			//ENVIO CORREO
			if(!enviarMailFirmado){	
				System.out.println("enviarMailActividad: guardarInformacionSeguimientoConValidacionRequisitoEnActividadActual");
				/*enviarMailActividad(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_geedp")),tab_precontractual.getValor("codigo_prpre"),tab_precontractual.getValor("descripcion_prpre"),
						pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")), tab_precontractual.getValor("actividad_actual_prpre"),tab_precontractual.getValor("fecha_prpre"),
						tab_precontractual.getValor("hora_prpre"),pckUtilidades.CConversion.CStr(tab_documento_requisito.getValor("etapa_prdoc")),
						pckUtilidades.CConversion.CStr(tab_documento_requisito.getValor("documentoadjunto_prdoc")));			*/
			}
			cargarInformacionDependiendoAreaYUsuario();
			if(tab_precontractual.getValor("ide_prpre")!=null){
				mostrarNotificacionActividad(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no cumple con los requisitos solicitados en la actividad actual, o no subió el archivo anexo.", "");
		}
	}
	
	/**
	 * Metodo que guarda la información de seguimiento de ese proceso validando si en esa actividad
	 * tiene que subir documentos y cumple con los mismos en actividad actual
	 */
	public void guardarInformacionSeguimientoConValidacionRequisitoEnActividadDevuelta(int tipoAprobacion, int ide_prpro, int ide_preta_retro, 
			                                                       int ide_actual_preta, boolean actividadOpcional){
		if(verificarRequisitoActividadActual(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_prpre")), ide_actual_preta, actividadOpcional))
		{			
			//enviarMailFirmado=false;
			//enviar_correo=pckUtilidades.CConversion.CInt(p_precon_automatico)==1;
			//primero guardamos los datos actuales antes de enviar a la siguiente actividad
			guardarSeguimiento(ide_preta_retro,ide_prpro,tipoAprobacion);
	
			//Cargamos los datos nuevos de la etapa siguiente
			tab_precontractual.setValor("ide_actual_preta", ide_preta_retro+"");
			String descripcion_etapa_actual=cargarEtapaActual(ide_preta_retro);
			tab_precontractual.setValor("actividad_actual_prpre", descripcion_etapa_actual);
			tab_precontractual.setValor("estado_proceso_prpre","EN PROCESO");
			tab_precontractual.setValor("fecha_prpre", utilitario.getFechaActual());
			tab_precontractual.setValor("hora_prpre", utilitario.getHoraActual());
			tab_precontractual.setValor("observacion_prpre", " ");
			
			String ide_geedp_enviar=null;
			if(pckUtilidades.CConversion.CInt(p_precon_automatico)==1 || ide_actual_preta==120 || ide_actual_preta==121)	//120 solicitud poa //121 emision poa
			   ide_geedp_enviar=cargarIdUsuario(ide_prpro, ide_preta_retro);
			
			if(ide_geedp_enviar==null){
				//insertamos el usuario que inicio el proceso
				String ide_gtemp=tab_precontractual.getValor("ide_usuario_iniciop_prpre");
				String ide_departamento=ser_generalAdm.servicioDepartamento("true",ide_gtemp);
				TablaGenerica tab_ide_departamento= utilitario.consultar(ide_departamento);
				String responsable_actual=ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(ide_gtemp));
				TablaGenerica tab_responsable= utilitario.consultar(responsable_actual);
				tab_precontractual.setValor("ide_actual_geedp",ide_gtemp);
				tab_precontractual.setValor("responsable_actual_prpre",tab_responsable.getValor("empleado"));//RESPONSABLE ASIGNADO");
				tab_precontractual.setValor("departamento_actual_prpre",tab_ide_departamento.getValor("DETALLE_GEDEP"));//DEPARTAMENTO ASIGNADO");
			}else{
				String ide_departamento=ser_generalAdm.servicioDepartamentoYIdEmpleado(ide_geedp_enviar);
				TablaGenerica tab_ide_departamento= utilitario.consultar(ide_departamento);
				String responsable_actual=ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(tab_ide_departamento.getValor("IDE_GTEMP")));
				TablaGenerica tab_responsable= utilitario.consultar(responsable_actual);
				tab_precontractual.setValor("estado_proceso_prpre","ENVIADO"); //enviar_correo=true;
				tab_precontractual.setValor("ide_actual_geedp",tab_ide_departamento.getValor("IDE_GTEMP"));
				tab_precontractual.setValor("responsable_actual_prpre",tab_responsable.getValor("empleado"));//RESPONSABLE ASIGNADO");
				tab_precontractual.setValor("departamento_actual_prpre",tab_ide_departamento.getValor("DETALLE_GEDEP"));//DEPARTAMENTO ASIGNADO");
			}
			tab_precontractual.modificar(tab_precontractual.getFilaActual());
			tipoContratacion=tipoContratacion.length()<1?"NA":tipoContratacion;
			//System.out.println("tipoContratacion: "+tipoContratacion);
			String ser_etapa_siguiente_enviar=cargarEtapaSiguienteAEnviar(ide_prpro,ide_preta_retro); //ide_actual_preta
			tab_precontractual.setValor("ide_preta",ser_etapa_siguiente_enviar);
			
			if(tipoAprobacion==3)
			{
				tab_documento_requisito.actualizar();
				TablaGenerica tab_precon_documento_requisito = utilitario.consultar("SELECT ide_prdoc as codigo,ide_prdoc, ide_prpre, ide_prreq FROM precon_documento_requisito "
						+ " where ide_prpre="+tab_precontractual.getValor("ide_prpre")+" and con_ide_prdoc is null and ide_prreq in (SELECT ide_prreq FROM precon_requisito where ide_preta="+ide_preta_retro+")");

				if(tab_precon_documento_requisito.getTotalFilas()>0 && tab_documento_requisito.getTotalFilas()>0)
				{
					tab_documento_requisito.setFilaActual(pckUtilidades.CConversion.CStr(tab_precon_documento_requisito.getValor("ide_prdoc")));
					tab_documento_requisito.setValor("presenta_prdoc","NO");
					tab_documento_requisito.modificar(tab_documento_requisito.getFilaActual());
				}
			}			
			
			tab_precontractual.guardar();
			tab_documento_requisito.guardar();
			tab_documento_requisito_secundario.guardar();
			guardarPantalla();
			utilitario.addUpdate("tab_precontractual");
			//ENVIO CORREO
			if(!enviarMailFirmado){			
			/*	enviarMailActividad(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_geedp")),tab_precontractual.getValor("codigo_prpre"),tab_precontractual.getValor("descripcion_prpre"),
						pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")), tab_precontractual.getValor("actividad_actual_prpre"),tab_precontractual.getValor("fecha_prpre"),
						tab_precontractual.getValor("hora_prpre"),pckUtilidades.CConversion.CStr(tab_documento_requisito.getValor("etapa_prdoc")),
						pckUtilidades.CConversion.CStr(tab_documento_requisito.getValor("documentoadjunto_prdoc")));*/
			}
			cargarInformacionDependiendoAreaYUsuario();
			/*if(tab_precontractual.getValor("ide_prpre")!=null){
				mostrarNotificacionActividad(ide_preta_retro); //ide_actual_preta
			}*/
		}else{
			utilitario.agregarMensajeInfo("No se puede enviar a la siguiente actividad, ya que no cumple con los requisitos solicitados en la actividad actual, o no subió el archivo anexo.", "");
		}
	}

	/**
	 * Metodo que permite devolver el id usuario en esa etapa 
	 * @param ide_etapaEnviar
	 * @return
	 */
	public String cargarIdUsuario(Integer ide_prpro,Integer ide_etapaEnviar){
		String ide_geedp="";
		String sr_ide_geedp=ser_precontractual.cargarInformacionUsuarioEtapaAEnviar(ide_prpro,ide_etapaEnviar);
		TablaGenerica tab_ide_geedp=utilitario.consultar(sr_ide_geedp);
		//tab_ide_geedp.imprimirSql();
		ide_geedp=tab_ide_geedp.getValor("ide_geedp");
		return ide_geedp;
				
	}
	/**
	 * Metodo que muestra campos en opcion lectura de acuerdo a la actividad actual
	 * @param ide_actual_preta
	 */
	public void mostrarCamposDeAcuerdoActividadActual(Integer ide_actual_preta){
		//tab_precontractual.getColumna("descripcion_prpre").setLectura(true);
		tab_precontractual.getColumna("aprueba_informetm_prpre").setLectura(true);
		tab_precontractual.getColumna("se_encuentra_catalogoe_prpre").setLectura(true);
		tab_precontractual.getColumna("termino_especificacion_prpre").setLectura(true);
		tab_precontractual.getColumna("monto_prpre").setLectura(true);
		tab_precontractual.getColumna("consta_poa_prpre").setLectura(true);
		tab_precontractual.getColumna("consta_pac_prpre").setLectura(true);
		tab_precontractual.getColumna("aprueba_proyecto_prpre").setLectura(false);
		tab_precontractual.getColumna("es_favorable_informej_prpre").setLectura(true);
		tab_precontractual.getColumna("esta_ok_prpre").setLectura(true);
		tab_precontractual.getColumna("es_superior_prpre").setLectura(true);
		tab_precontractual.getColumna("manifestacion_interes_prpre").setLectura(true);
		tab_precontractual.getColumna("cumple_prpre").setLectura(true);
		tab_precontractual.getColumna("comision_tecnica_prpre").setLectura(true);
		tab_precontractual.getColumna("es_viable_prpre").setLectura(true);
		tab_precontractual.getColumna("error_forma_prpre").setLectura(true);
		tab_precontractual.getColumna("recomienda_adjudicar_prpre").setLectura(true);
		tab_precontractual.getColumna("necesario_reaperturar_prpre").setLectura(true);
		tab_precontractual.getColumna("convalidacion_satisfac_prpre").setLectura(true);
		tab_precontractual.getColumna("existe_mas_oferta_prpre").setLectura(true);
		tab_precontractual.getColumna("se_recibe_oferta_prpre").setLectura(true);
		tab_precontractual.getColumna("convalida_recadjudicar_prpre").setLectura(true);
		/*switch (ide_actual_preta) {
			case 1: tab_precontractual.getColumna("descripcion_prpre").setLectura(false);
	        		break;
			case 3: tab_precontractual.getColumna("aprueba_informetm_prpre").setLectura(false);
	        		break;
			case 4: tab_precontractual.getColumna("descripcion_prpre").setLectura(false);
    				break;
		    case 6:	tab_precontractual.getColumna("se_encuentra_catalogoe_prpre").setLectura(false);
		    		break;
		}*/
	}

	/**
	 * Metodo que muestra notificación de las tareas que debe hacer en la actividad actual
	 * @param ide_actual_preta
	 */
	public void mostrarNotificacionActividad(Integer ide_actual_preta){
		
		if(ide_actual_preta>1)
		{
			if(tab_precontractual.getValor("estado_proceso_prpre").contains("NO APROBADO") || tab_precontractual.getValor("estado_proceso_prpre").contains("FINALIZADO"))
			{
				utilitario.agregarNotificacionInfo("ESTADO DEL PROCESO: ",tab_precontractual.getValor("estado_proceso_prpre"));			
				return;
			}
		}
		
		String concatenacion="";
		String etapa_actual=cargarEtapaActual(ide_actual_preta);
		String ser_etapa_requisitos=ser_etapa_requisito.getEtapaConRequisito(ide_actual_preta);
		TablaGenerica tab_etapa_presenta = utilitario.consultar(ser_etapa_requisitos);
		for(int i = 0; i < tab_etapa_presenta.getTotalFilas(); i++){
			String requisito=tab_etapa_presenta.getValor(i, "descripcion_prreq");
			concatenacion=concatenacion+requisito+" -- ";
		}

		concatenacion+="RESPONSABLE: "+tab_precontractual.getValor("responsable_actual_prpre");
		
		if(tab_etapa_presenta.getTotalFilas()!=0){
			utilitario.agregarNotificacionInfo("ACTIVIDAD ACTUAL: "+etapa_actual+" -- "+(requiereAprobacion?"REQUIERE APROBACION":"NO REQUIERE APROBACION") +" -- "+(actividadOpcional?"OPCIONAL":"REQUERIDA") ,"REQUISITOS A SUBIR: "+concatenacion);
		}else{
			utilitario.agregarNotificacionInfo("ACTIVIDAD ACTUAL: "+etapa_actual+" -- "+(requiereAprobacion?"REQUIERE APROBACION":"NO REQUIERE APROBACION") +" -- "+(actividadOpcional?"OPCIONAL":"REQUERIDA"),"REQUISITOS A SUBIR: No tiene que subir ningún requisito");
		}
		
	}

	/**
	 * Metodo que permite guardar el seguimiento, de cada actividad que se realice en el proceso
	 */
	public void guardarSeguimiento(Integer ide_preta, int ide_prpro, int tipoAprobacion){
		String actividad="";
		tab_seguimiento.insertar();
		tab_seguimiento.setValor("ide_prpre",tab_precontractual.getValor("ide_prpre"));//ID PRECONTRACTUAL
		tab_seguimiento.setValor("fecha_asignacion_prseg",tab_precontractual.getValor("fecha_prpre"));//FECHA ASIGNACIÓN");
		tab_seguimiento.setValor("hora_asignacion_prseg",tab_precontractual.getValor("hora_prpre"));//HORA ASIGNACIÓN");
		tab_seguimiento.setValor("fecha_cambio_prseg",utilitario.getFechaActual());//FECHA CAMBIO");
		tab_seguimiento.setValor("hora_cambio_prseg",utilitario.getHoraActual());//HORA CAMBIO");
		tab_seguimiento.setValor("etapa_inicio_prseg",tab_precontractual.getValor("actividad_actual_prpre"));//("ACTIVIDAD INICIO");
		tab_seguimiento.setValor("responsable_prseg",tab_precontractual.getValor("responsable_actual_prpre"));//"RESPONSABLE");
		tab_seguimiento.setValor("departamento_prseg",tab_precontractual.getValor("departamento_actual_prpre"));//DEPARTAMENTO ACTUAL
		tab_seguimiento.setValor("ide_preta_prseg",tab_precontractual.getValor("ide_actual_preta"));//ID ACTIVIDAD INICIO 
		String etapa_final=cargarEtapaSiguientesAntesdeEnviar(ide_preta);
		tab_seguimiento.setValor("etapa_fin_prseg",etapa_final);//ACTIVIDAD FIN");
		String ide_geedp_enviar=cargarIdUsuario(ide_prpro, ide_preta);
		//Si es igual a null quiere decir que no tiene la etapa un usuario asignado 
		if(ide_geedp_enviar==null){
			String ide_gtemp=tab_precontractual.getValor("ide_usuario_iniciop_prpre");
			String ide_departamento=ser_generalAdm.servicioDepartamento("true",ide_gtemp);
			TablaGenerica tab_ide_departamento= utilitario.consultar(ide_departamento);
			String responsable_actual=ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(ide_gtemp));
			TablaGenerica tab_responsable= utilitario.consultar(responsable_actual);
			tab_seguimiento.setValor("responsable_asignado_prseg",tab_responsable.getValor("empleado"));//RESPONSABLE ASIGNADO");
			tab_seguimiento.setValor("departamento_asignado_prseg",tab_ide_departamento.getValor("DETALLE_GEDEP"));//DEPARTAMENTO ASIGNADO");
			tab_seguimiento.setValor("notas_prseg",pckUtilidades.CConversion.CStr(are_txt_notas.getValue()));//NOTAS DEL APROBADOR
		}else{
			String ide_geedp_enviar_serial=ide_geedp_enviar;
			String ide_departamento=ser_generalAdm.servicioDepartamentoYIdEmpleado(ide_geedp_enviar_serial);
			TablaGenerica tab_ide_departamento= utilitario.consultar(ide_departamento);
			String responsable_actual=ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(tab_ide_departamento.getValor("IDE_GTEMP")));
			TablaGenerica tab_responsable= utilitario.consultar(responsable_actual);
			tab_seguimiento.setValor("responsable_asignado_prseg",tab_responsable.getValor("empleado"));//RESPONSABLE ASIGNADO");
			tab_seguimiento.setValor("departamento_asignado_prseg",tab_ide_departamento.getValor("DETALLE_GEDEP"));//DEPARTAMENTO ASIGNADO");
			tab_seguimiento.setValor("notas_prseg",pckUtilidades.CConversion.CStr(are_txt_notas.getValue()));//NOTAS DEL APROBADOR
		}
		
		if(tipoAprobacion==0)
		{
			tab_seguimiento.setValor("observacion_prseg",tab_precontractual.getValor("observacion_prpre"));//"OBSERVACION");
			tab_seguimiento.setValor("estado_actividad_prseg",tab_precontractual.getValor("estado_proceso_prpre"));//ESTADO ACTIVIDAD");
		}
		if(tipoAprobacion==1)
		{
			actividad="SE APROBO CORRECTAMENTE: "+cargarEtapaSiguientesAntesdeEnviar(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			String ide_gtempxx=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
			TablaGenerica tab_responsable_aprob=utilitario.consultar(ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(ide_gtempxx)));
			actividad+=" | APROBADO POR: "+tab_responsable_aprob.getValor("empleado");
			tab_seguimiento.setValor("observacion_prseg",actividad);//"OBSERVACION");
			tab_seguimiento.setValor("estado_actividad_prseg","APROBADO");//ESTADO ACTIVIDAD");
		}
		if(tipoAprobacion==2)
		{
			actividad="FINALIZA EL PROCESO YA QUE NO SE APROBO: "+cargarEtapaSiguientesAntesdeEnviar(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			String ide_gtempxx=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
			TablaGenerica tab_responsable_aprob=utilitario.consultar(ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(ide_gtempxx)));
			actividad+=" | NO APROBADO POR: "+tab_responsable_aprob.getValor("empleado");
			tab_seguimiento.setValor("observacion_prseg",actividad);//"OBSERVACION");
			tab_seguimiento.setValor("estado_actividad_prseg","NO APROBADO");//ESTADO ACTIVIDAD");
		}
		
		if(tipoAprobacion==3)
		{
			actividad="SE DEVUELVE EL PROCESO YA QUE NO SE APROBO: "+cargarEtapaSiguientesAntesdeEnviar(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta")));
			String ide_gtempxx=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
			TablaGenerica tab_responsable_aprob=utilitario.consultar(ser_generalAdm.servicioEmpleados("true", pckUtilidades.CConversion.CInt(ide_gtempxx)));
			actividad+=" | DEVUELTO POR: "+tab_responsable_aprob.getValor("empleado");
			tab_seguimiento.setValor("observacion_prseg",actividad);//"OBSERVACION");
			tab_seguimiento.setValor("estado_actividad_prseg",tab_precontractual.getValor("estado_proceso_prpre"));//ESTADO ACTIVIDAD");
		}

		String estadoProcedimiento=cargarEstadoProcedimiento(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")));
		tab_seguimiento.setValor("estado_procedimiento_prseg",estadoProcedimiento);//ESTADO PROCEDIMIENTO");	
		tab_seguimiento.setValor("activo_prseg","true");
		tab_seguimiento.guardar();
		guardarPantalla();
		//tab_seguimiento.ejecutarSql();
		/*tab_seguimiento.setLectura(true);
		tab_seguimiento.dibujar();
		tab_seguimiento.setLectura(false);*/
		
		if(tipoAprobacion==2)
			cambiarEstadoProceso("NO APROBADO",actividad);
		
		//utilitario.addUpdate("tab_seguimiento");
		
	}
	/**
	 * Metodo que permite guardar el seguimiento, de finalización del proceso
	 */
	public void guardarSeguimientoFin(String observacion){
		tab_seguimiento.insertar();
		tab_seguimiento.setValor("ide_prpre",tab_precontractual.getValor("ide_prpre"));//ID PRECONTRACTUAL
		tab_seguimiento.setValor("fecha_asignacion_prseg",tab_precontractual.getValor("fecha_prpre"));//FECHA ASIGNACIÓN");
		tab_seguimiento.setValor("hora_asignacion_prseg",tab_precontractual.getValor("hora_prpre"));//HORA ASIGNACIÓN");
		tab_seguimiento.setValor("fecha_cambio_prseg",utilitario.getFechaActual());//FECHA CAMBIO");
		tab_seguimiento.setValor("hora_cambio_prseg",utilitario.getHoraActual());//HORA CAMBIO");
		tab_seguimiento.setValor("etapa_inicio_prseg",tab_precontractual.getValor("actividad_actual_prpre"));//("ACTIVIDAD INICIO");
		tab_seguimiento.setValor("responsable_prseg",tab_precontractual.getValor("responsable_actual_prpre"));//"RESPONSABLE");
		tab_seguimiento.setValor("departamento_prseg",tab_precontractual.getValor("departamento_actual_prpre"));//DEPARTAMENTO ACTUAL
		tab_seguimiento.setValor("ide_preta_prseg",tab_precontractual.getValor("ide_actual_preta"));//ID ACTIVIDAD INICIO 
		tab_seguimiento.setValor("observacion_prseg",observacion);//"OBSERVACION");
		tab_seguimiento.setValor("estado_actividad_prseg",tab_precontractual.getValor("estado_proceso_prpre"));//ESTADO ACTIVIDAD");
		String estadoProcedimiento=cargarEstadoProcedimiento(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_pretp")));
		tab_seguimiento.setValor("estado_procedimiento_prseg",estadoProcedimiento);//ESTADO PROCEDIMIENTO");	
		tab_seguimiento.setValor("activo_prseg","true");
		tab_seguimiento.guardar();
		//guardarPantalla();
		//tab_seguimiento.ejecutarSql();
		/*tab_seguimiento.setLectura(true);
		tab_seguimiento.dibujar();
		tab_seguimiento.setLectura(false);*/
		//utilitario.addUpdate("tab_seguimiento");
	}
	/**
	 * Metodo que permite cargar el estado del procedimiento
	 * @return
	 */
	public String cargarEstadoProcedimiento(Integer ide_pretp){
		String estado_procedimiento="";
		String ser_estadoProcedimiento=ser_EtapaProcedimiento.getProcedimientos(ide_pretp);
		TablaGenerica tab_estadoProcedimiento=utilitario.consultar(ser_estadoProcedimiento);
		estado_procedimiento=tab_estadoProcedimiento.getValor("descripcion");
		return estado_procedimiento;
	}
	/**
	 * Metodo que permite cargar la actividad antes de enviar de acuerdo a su id
	 * @param ide_preta
	 * @return
	 */
	public String cargarEtapaSiguientesAntesdeEnviar(Integer ide_preta){
		String etapa;
		String ser_cargaEtapa=ser_etapa.getEtapa(ide_preta);
		TablaGenerica tab_cargaEtapa=utilitario.consultar(ser_cargaEtapa);
		//tab_cargaEtapa.imprimirSql();
		etapa=tab_cargaEtapa.getValor("descripcion_preta");
		return etapa;
	}
	/**
	 * Metodo que permite cambiar el estado del proceso y guardar su seguimiento
	 * @param estado FINALIZADO
	 * @param mensajeObservacion que se guarda en el seguimiento
	 */
	public void cambiarEstadoProceso(String estado, String mensajeObservacion){
		tab_precontractual.setValor("estado_proceso_prpre",estado);
		tab_precontractual.modificar(tab_precontractual.getFilaActual());
		tab_precontractual.guardar();
		guardarPantalla();
		utilitario.addUpdate("tab_precontractual");
		guardarSeguimientoFin(mensajeObservacion);
		utilitario.agregarMensajeInfo("ESTADO DEL PROCESO: "+estado, mensajeObservacion);
	}
	/**
	 * Metodo que permite realizar un insert a la tabla pre_contrato, para empezar con la fase contractual
	 * @param tipocontrato
	 * @param objeto_contrato
	 * @param monto
	 * @param ide_proveedor
	 */
	public void cargarDatosEnContrato(String tipocontrato,String objeto_contrato,Double monto,Integer ide_proveedor){
		Integer max_ide_prcon=0;
		Integer ide_tipocontrato=0;
		TablaGenerica tab_max_ide=utilitario.consultar("select 1 as id, max(ide_prcon) as max_ide from pre_contrato;");
		max_ide_prcon=pckUtilidades.CConversion.CInt(tab_max_ide.getValor("max_ide"))+1;
		if(tipocontrato.equals("TR")){
			ide_tipocontrato=4;
		}else{
			ide_tipocontrato=5;
		}
		if(ide_proveedor==0){
			ide_proveedor=null;
		}
		String insert_contrato=ser_generalAdm.getInsertContrato(max_ide_prcon,ide_tipocontrato, objeto_contrato, monto, ide_proveedor);
		utilitario.getConexion().ejecutarSql(insert_contrato);
	}
	/**
	 * Metodo que carga la fase actual segun en id del procedimiento
	 * @param ide_pretp
	 * @return
	 */
	public String cargarFaseActual(Integer ide_prpre){
		String fase_actual="";
		String ser_fase_actual=ser_fase.getFase(ide_prpre);
		TablaGenerica tab_fase_actual=utilitario.consultar(ser_fase_actual);
		fase_actual=tab_fase_actual.getValor("descripcion_prfas");
		return fase_actual;
	}
	
	public boolean validarPerfil(List perfilUsuarioConectado, String strPerfil)
	{		
		for(Object perfil : perfilUsuarioConectado)
		{
			if(perfil.toString().toUpperCase().contains(strPerfil.toUpperCase()))
				return true;		
		}		
		return false;
	}
	
	public void validarTareas(boolean cargar)
	{
		try
		{
			if(tab_precontractual.isFocus()){
				int ide_actual_preta=pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"));
				if(ide_actual_preta>0)
				{
					if(cargar)
						cargarInfoActividad();
					mostrarCamposDeAcuerdoActividadActual(ide_actual_preta);
					mostrarNotificacionActividad(ide_actual_preta);
					tipoContratacion=pckUtilidades.CConversion.CStr(tab_precontractual.getValor("termino_especificacion_prpre"));
					//tab_documento_requisito.actualizar();
					
					if(pckUtilidades.CConversion.CInt(tab_documento_requisito.getTotalFilas())==0)
					{
						tab_documento_requisito.setCondicion("ide_prpre="+tab_precontractual.getValor("ide_prpre"));
						tab_documento_requisito.ejecutarSql();
					}
					
					TablaGenerica tab_precon_documento_requisito = utilitario.consultar("SELECT ide_prdoc as codigo,ide_prdoc, ide_prpre, ide_prreq FROM precon_documento_requisito "
							+ " where ide_prpre="+tab_precontractual.getValor("ide_prpre")+" and con_ide_prdoc is null and ide_prreq in (SELECT ide_prreq FROM precon_requisito where ide_preta="+ide_actual_preta+")");
					//tab_precon_documento_requisito.imprimirSql();					
					
					if(tab_precon_documento_requisito.getTotalFilas()>0 && tab_documento_requisito.getTotalFilas()>0)
					{
						tab_documento_requisito.setFilaActual(pckUtilidades.CConversion.CStr(tab_precon_documento_requisito.getValor("ide_prdoc")));
						tab_documento_requisito.setFocus();
						tab_documento_requisito_secundario.setCondicion("con_ide_prdoc="+tab_precon_documento_requisito.getValor("ide_prdoc"));
						tab_documento_requisito_secundario.ejecutarSql();
						//utilitario.addUpdate("tab_documento_requisito,tab_documento_requisito_secundario");
					}
					else
					{
						if(pckUtilidades.CConversion.CInt(tab_precon_documento_requisito.getTotalFilas())==0 && !(validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR") || validarPerfil(perfilUsuarioConectado,"AVANZADO")) && int_opcion<3)
						{
							tab_documento_requisito.setCondicion("ide_prdoc=-1");
							tab_documento_requisito.ejecutarSql();
							tab_documento_requisito_secundario.setCondicion("con_ide_prdoc=-1");
							tab_documento_requisito_secundario.ejecutarSql();
						}
						else
							if(tab_documento_requisito.getTotalFilas()>0)
							{
								tab_documento_requisito_secundario.setCondicion("con_ide_prdoc="+tab_documento_requisito.getValor("ide_prdoc"));
								tab_documento_requisito_secundario.ejecutarSql();
							}
							else
							{						
								tab_documento_requisito_secundario.setCondicion("con_ide_prdoc=-1");
								tab_documento_requisito_secundario.ejecutarSql();
							}
					}
				}
				else
					utilitario.agregarNotificacionInfo("ACTIVIDAD ACTUAL: N/A ","Cierre esta notificacion en la parte superior derecha...");
			}
			else
				if(tab_documento_requisito.isFocus()){
					if(tab_documento_requisito.getTotalFilas()>0)
					{
						tab_documento_requisito_secundario.setCondicion("con_ide_prdoc="+tab_documento_requisito.getValor("ide_prdoc"));
						tab_documento_requisito_secundario.ejecutarSql();
					}
					else
					{
						tab_documento_requisito_secundario.setCondicion("con_ide_prdoc=-1");
						tab_documento_requisito_secundario.ejecutarSql();
					}
				}
		}
		catch(Exception ex)
		{
			System.out.println("Error validarTareas "+ex.getMessage());
		}
	}
	
	public void enviarMailActividad(Integer ide_usuario,String codigo_proceso,String proceso_contratacion,Integer procedimiento_contratacion,String actividad,String fecha_asignacion,String hora_asignacion
			,String nombreAdjunto, String rutaAdjunto){
		String concatenacionFechaHoraAsignacionActividad=fecha_asignacion+" - "+hora_asignacion;
		
		String procedimientoContratacion="";
		String ser_etapa_proce=ser_EtapaProcedimiento.getProcedimientos(procedimiento_contratacion);
		TablaGenerica tab_etapa_actual=utilitario.consultar(ser_etapa_proce);
		procedimientoContratacion=tab_etapa_actual.getValor("descripcion");
				
		TablaGenerica tab_correos= utilitario.consultar("select nom_usua, mail_usua from sis_usuario where activo_usua=true and ide_gtemp='"+ide_usuario+"'");
		
		TablaGenerica tab_correo_envio= utilitario.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
				+ "clave_corr from sis_correo where ide_corr=3"); 
		String smtp_correo=tab_correo_envio.getValor("smtp_corr"); 
		String puertoEnvio=tab_correo_envio.getValor("puerto_corr"); 
		String correo_envio=tab_correo_envio.getValor("correo_corr");
		String usuario_envio=tab_correo_envio.getValor("usuario_corr"); 
		String clave_correo=tab_correo_envio.getValor("clave_corr");
		
		String str_asunto = "CONTROL DE SEGUIMIENTO CONTRATACIÓN";
		String str_mail = "";
		String str_mensaje = "";
		String str_nombre_adjunto = "";
			
		EnvioMail envMail = new EnvioMail(smtp_correo,puertoEnvio,correo_envio,usuario_envio,clave_correo);
		
		for (int i = 0; i < tab_correos.getTotalFilas(); i++) {
			
			try {
				
				File anexo;
				
				if(pckUtilidades.CConversion.CStr(rutaAdjunto).length()>4)
				{
					String ruta=utilitario.getPropiedad("rutaDownload")+(rutaAdjunto);			
					str_nombre_adjunto=pckUtilidades.Utilitario.quitarCaracteresSpeciales(nombreAdjunto);
					anexo=utilitario.descargarArchivo(ruta, str_nombre_adjunto,rutaAdjunto);
				}
				else
					anexo=null;				
				
				//if(tab_correos.getValor(i,"mail_usua")!=null)
					
				//util.EnviaMailInterno(envMail, tab_correos.getValor(i,"mail_usua"), 
						         // "alex.becerra@emgirs.gob.ec", 
				//"CONTROL DE SEGUIMIENTO CONTRATACIÓN",
				//emailNotificacionCambioActividad(tab_correos.getValor("nom_usua"),codigo_proceso,proceso_contratacion,procedimientoContratacion,actividad,concatenacionFechaHoraAsignacionActividad),anexo);
			
				str_mail="juan.ayerve@quitohonesto.gob.ec";//tab_correos.getValor(i,"mail_usua");
				System.out.println("envMail0: "+str_mail);
				str_mensaje=emailNotificacionCambioActividad(tab_correos.getValor("nom_usua"),codigo_proceso,proceso_contratacion,procedimientoContratacion,actividad,concatenacionFechaHoraAsignacionActividad);
				//str_mail="alex.becerra@emgirs.gob.ec";
				envMail.setAsunto(str_asunto);
				envMail.setCuerpoHtml(str_mensaje);
				envMail.setPara(str_mail);
				//envMail.setCopia(envMail.getCorreoEnvio());
				if(anexo!=null)
				{
					envMail.setNombreAdjunto(str_nombre_adjunto);
					envMail.setAdjuntoArray64(pckUtilidades.Utilitario.fileConvertToArray64(anexo));
				}
				pckEntidades.MensajeRetorno obj= pckUtilidades.consumoServiciosCore.enviarMail(envMail);
				
				if(obj.getRespuesta())
				{
					utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : email: " + str_mail);
				}
				else
					utilitario.agregarMensajeError("Correo no enviado a : email: " + str_mail , " msjError: " + obj.getDescripcion());
			
			}catch (Exception e) {
				System.out.println("Error en el envío de correo"+e.getMessage());
			}
			
			System.out.println("envMail: "+str_mail);
			//System.out.println("envMail: "+emailNotificacionCambioActividad(tab_correos.getValor("nom_usua"),codigo_proceso,proceso_contratacion,procedimientoContratacion,actividad,concatenacionFechaHoraAsignacionActividad));
		}
	}
	
	//CUERPO DEL MENSAJE A ENVIAR AL EMPLEADO ASIGNADO
	public String emailNotificacionCambioActividad(String nombreEmpleado,String codigo_proceso,String proceso_contratacion,String procedimiento_contratacion,String actividad,String fechaHora_asignacion) {
		      String html = "<p>Estimado(a), "
		              + "</p>\n"
		              + "<p>"+nombreEmpleado+"</p>"
		              + "<p>&nbsp;</p>\n"
		              + "<p>El Proceso "+procedimiento_contratacion+" de Contratación: "+codigo_proceso.toLowerCase()+" "+proceso_contratacion+", se le asignado, para que realice la Actividad: "+actividad.toLowerCase()+". Con Fecha y Hora de Asignacion: "+fechaHora_asignacion+"</p>\n"
		              + "<p>&nbsp;</p>\n"
		              + "<p>Saludos cordiales,</p>\n"
		              + "<table style=\"height: 144px;\" width=\"571\">\n"
		              + "<tbody>\n"
		              + "<tr>\n"
		              + "<td style=\"font-size: 0.8em; border-right: solid;\"><img src=\"https://facturacion.emgirs.gob.ec/addocument_website/client_files/logo-emgirs.png\" alt=\"\" width=\"211\" height=\"102\" /></td>\n"
		              + "<td width=\"476\">\n"
		              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>EMPRESA P&Uacute;BLICA METROPOLITANA DE GESTI&Oacute;N INTEGRAL DE RESIDUOS&nbsp;S&Oacute;LIDOS</strong></p>\n"
		              + "<p style=\"font-size: 0.8em; text-align: left; padding: 0px; margin: 0px;\"><strong>Pje: OE3G - N51-84 y Av. Río Amazonas</strong></p>\n"
		              + "PBX: (02) 3930-600&nbsp; ext. 2505\n"
		              + "<p style=\"font-size: 0.8em; padding: 0px; margin: 0px;\"><a href=\"http://www.emgirs.gob.ec/\">www.emgirs.gob.ec</a></p>\n"
		              + "</td>\n"
		              + "</tr>\n"
		              + "</tbody>\n"
		              + "</table>";
		      return html;
	}
	
	public void generarDesignacionTecnico(String ide_prpac)
	{
		insertarRequisito();

		String secuencial_doc=generarNroDocumento(par_precon_tecnico);

		archivo="tecnico_pac_"+ide_prpac+"_"+secuencial_doc;
		String str_path = utilitario.getPropiedad("rutaUpload") + "/" + carpeta;
		File path = new File(str_path);
		path.mkdirs();
		
		tab_documento_requisito.setValor("documentoadjunto_prdoc", "/upload/"+carpeta+"/"+archivo+".pdf" );
		tab_documento_requisito.setValor("presenta_prdoc","SI");
		tab_documento_requisito.setValor("nro_documento_prdoc",secuencial_doc);
		
		tab_documento_requisito.setValor("asunto_prdoc", "N/A");
		tab_documento_requisito.setValor("de_nombre_prdoc", "N/A");
		tab_documento_requisito.setValor("para_nombre_prdoc", "N/A");
		tab_documento_requisito.setValor("nro_fojas_prdoc", "1");
		tab_documento_requisito.setValor("detalle_anexos_prdoc", "N/A");
		
		Map p_parametros = new HashMap();
		p_parametros.put("ide_prpac", pckUtilidades.CConversion.CInt(ide_prpac));
		p_parametros.put("nro_documento",secuencial_doc);
		
		fil_pdf = generarPDF(p_parametros,"/reportes/rep_contratacion/rep_designacion_tecnico.jasper",archivo);
		pckUtilidades.Utilitario.copiarArchivo(fil_pdf, utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+archivo+".pdf");
		tab_documento_requisito.guardar();
		
		guardarPantalla();
		
		firmar(archivo);

	}
	
	public void generarActualizacionDesignacionTecnico(String ide_prpac)
	{		
		tab_documento_requisito.fin();
		insertarRequisitoSecundario();
		
		String secuencial_doc=generarNroDocumento(par_precon_tecnico);

		archivo="tecnico_pac_"+ide_prpac+"_"+secuencial_doc+"_alcance_"+tab_responsable_prec.getTotalFilas();
		String str_path = utilitario.getPropiedad("rutaUpload") + "/" + carpeta;
		File path = new File(str_path);
		path.mkdirs();
		
		tab_documento_requisito_secundario.setValor("descripcion_prdoc",archivo);
		tab_documento_requisito_secundario.setValor("documentoadjunto_prdoc", "/upload/"+carpeta+"/"+archivo+".pdf" );
		tab_documento_requisito_secundario.setValor("presenta_prdoc","SI");
		tab_documento_requisito_secundario.setValor("nro_documento_prdoc",secuencial_doc);
		
		tab_documento_requisito_secundario.setValor("asunto_prdoc", "N/A");
		tab_documento_requisito_secundario.setValor("de_nombre_prdoc", "N/A");
		tab_documento_requisito_secundario.setValor("para_nombre_prdoc", "N/A");
		tab_documento_requisito_secundario.setValor("nro_fojas_prdoc", "1");
		tab_documento_requisito_secundario.setValor("detalle_anexos_prdoc", "N/A");
		
		Map p_parametros = new HashMap();
		p_parametros.put("ide_prpac", pckUtilidades.CConversion.CInt(ide_prpac));
		p_parametros.put("nro_documento",secuencial_doc);
		
		fil_pdf = generarPDF(p_parametros,"/reportes/rep_contratacion/rep_designacion_tecnico.jasper",archivo);
		pckUtilidades.Utilitario.copiarArchivo(fil_pdf, utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+archivo+".pdf");
		tab_documento_requisito_secundario.guardar();
		
		guardarPantalla();
		utilitario.agregarMensaje("Guardado Correctamente", "...");	
		
		firmar(archivo);
		
	}
	
	public boolean generarSolicitudStockBodega(String ide_prpac, String ide_prpro, String ide_preta, String solicitud)
	{		
		insertarRequisito();
		TablaGenerica tab_datos_usuario=utilitario.consultar(ser_precontractual.cargarDatosUsuarioEtapaAEnviar(pckUtilidades.CConversion.CInt(ide_prpro), pckUtilidades.CConversion.CInt(ide_preta)));
		if(tab_datos_usuario.getTotalFilas()>0)
		{

			String secuencial_doc=generarNroDocumento(par_sec_precon_sol);
			
			archivo="solicitud_stockbodega_"+ide_prpac+"_"+secuencial_doc;
			String str_path = utilitario.getPropiedad("rutaUpload") + "/" + carpeta;
			File path = new File(str_path);
			path.mkdirs();
			
			tab_documento_requisito.setValor("documentoadjunto_prdoc", "/upload/"+carpeta+"/"+archivo+".pdf" );
			tab_documento_requisito.setValor("presenta_prdoc","SI");
			tab_documento_requisito.setValor("nro_documento_prdoc",secuencial_doc);
			
			tab_documento_requisito.setValor("asunto_prdoc", "N/A");
			tab_documento_requisito.setValor("de_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("para_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("nro_fojas_prdoc", "1");
			tab_documento_requisito.setValor("detalle_anexos_prdoc", "N/A");
			
			Map p_parametros = new HashMap();
			p_parametros.put("ide_prpac", pckUtilidades.CConversion.CInt(ide_prpac));
			p_parametros.put("detalle", pckUtilidades.CConversion.CStr(solicitud));
			p_parametros.put("para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("nombres_apellidos")));
			p_parametros.put("pie_para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("detalle_geare")));
			p_parametros.put("nro_documento",secuencial_doc);
			
			fil_pdf = generarPDF(p_parametros,"/reportes/rep_contratacion/rep_stock_bodega.jasper",archivo);
			if(fil_pdf==null)
			{
				utilitario.agregarMensajeInfo("Error al generar la solicitud...", "Favor intente nuevamente..");
				return false;
			}
			pckUtilidades.Utilitario.copiarArchivo(fil_pdf, utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+archivo+".pdf");
		}
		else
			utilitario.agregarMensajeInfo("No configurado el responsable para la emision de la solicitud...", "Favor carge el documento manualmente...");	
		
		tab_documento_requisito.guardar();
		
		guardarPantalla();
		
		firmar(archivo);
		return true;
	}
	
	public boolean generarVerificacionStockBodega(String ide_prpac, String ide_prpro, String ide_preta, String solicitud)
	{		
		insertarRequisito();
		TablaGenerica tab_datos_usuario=utilitario.consultar(ser_precontractual.responsablePAC(pckUtilidades.CConversion.CInt(ide_prpac)));
		if(tab_datos_usuario.getTotalFilas()>0)
		{

			String secuencial_doc=generarNroDocumento(par_sec_precon_sol);
			
			archivo="verificacion_stockbodega_"+ide_prpac+"_"+secuencial_doc;
			String str_path = utilitario.getPropiedad("rutaUpload") + "/" + carpeta;
			File path = new File(str_path);
			path.mkdirs();
			
			tab_documento_requisito.setValor("documentoadjunto_prdoc", "/upload/"+carpeta+"/"+archivo+".pdf" );
			tab_documento_requisito.setValor("presenta_prdoc","SI");
			tab_documento_requisito.setValor("nro_documento_prdoc",secuencial_doc);
			
			tab_documento_requisito.setValor("asunto_prdoc", "N/A");
			tab_documento_requisito.setValor("de_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("para_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("nro_fojas_prdoc", "1");
			tab_documento_requisito.setValor("detalle_anexos_prdoc", "N/A");
			
			Map p_parametros = new HashMap();
			p_parametros.put("ide_prpac", pckUtilidades.CConversion.CInt(ide_prpac));
			p_parametros.put("detalle", pckUtilidades.CConversion.CStr(solicitud));
			p_parametros.put("para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("nombres_apellidos")));
			p_parametros.put("pie_para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("detalle_geare")));
			p_parametros.put("nro_documento",secuencial_doc);
			
			fil_pdf = generarPDF(p_parametros,"/reportes/rep_contratacion/rep_stock_bodega_resp.jasper",archivo);
			if(fil_pdf==null)
			{
				utilitario.agregarMensajeInfo("Error al generar la Respuesta...", "Favor intente nuevamente..");
				return false;
			}
			pckUtilidades.Utilitario.copiarArchivo(fil_pdf, utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+archivo+".pdf");
		}
		else
		{
			System.out.println("No configurado el responsable para la emision de la Respuesta...");
			utilitario.agregarMensajeInfo("No configurado el responsable para la emision de la Respuesta...", "Favor carge el documento manualmente...");	
		}
		tab_documento_requisito.guardar();
		
		guardarPantalla();
		
		firmar(archivo);
		return true;
	}
	
	public boolean generarSolicitudNoDuplicidadConsultoria(String ide_prpac, String ide_prpro, String ide_preta)
	{		
		insertarRequisito();

		TablaGenerica tab_datos_usuario=utilitario.consultar(ser_precontractual.cargarDatosUsuarioEtapaAEnviar(pckUtilidades.CConversion.CInt(ide_prpro), pckUtilidades.CConversion.CInt(ide_preta)));
		if(tab_datos_usuario.getTotalFilas()>0)
		{
			String secuencial_doc=generarNroDocumento(par_sec_precon_sol);
			
			archivo="solicitud_noduplicidad_consultoria_"+ide_prpac+"_"+secuencial_doc;
			String str_path = utilitario.getPropiedad("rutaUpload") + "/" + carpeta;
			File path = new File(str_path);
			path.mkdirs();
			
			tab_documento_requisito.setValor("documentoadjunto_prdoc", "/upload/"+carpeta+"/"+archivo+".pdf" );
			tab_documento_requisito.setValor("presenta_prdoc","SI");
			tab_documento_requisito.setValor("nro_documento_prdoc",secuencial_doc);
			
			tab_documento_requisito.setValor("asunto_prdoc", "N/A");
			tab_documento_requisito.setValor("de_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("para_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("nro_fojas_prdoc", "1");
			tab_documento_requisito.setValor("detalle_anexos_prdoc", "N/A");
			
			Map p_parametros = new HashMap();
			p_parametros.put("ide_prpac", pckUtilidades.CConversion.CInt(ide_prpac));
			p_parametros.put("para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("nombres_apellidos")));
			p_parametros.put("pie_para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("detalle_geare")));
			p_parametros.put("nro_documento",secuencial_doc);
			
			fil_pdf = generarPDF(p_parametros,"/reportes/rep_contratacion/rep_solicitud_noduplicidad_consultoria.jasper",archivo);
			if(fil_pdf==null)
			{
				utilitario.agregarMensajeInfo("Error al generar la solicitud...", "Favor intente nuevamente..");
				return false;
			}
			pckUtilidades.Utilitario.copiarArchivo(fil_pdf, utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+archivo+".pdf");
		}
		else
			utilitario.agregarMensajeInfo("No configurado el responsable para la emision de la solicitud...", "Favor carge el documento manualmente...");
		
		tab_documento_requisito.guardar();
		
		guardarPantalla();
		
		firmar(archivo);
		return true;
	}
	
	public boolean generarCertificacionNoDuplicidadConsultoria(String ide_prpac, String ide_prpro, String ide_preta, String solicitud)
	{		
		insertarRequisito();
				
		TablaGenerica tab_datos_usuario=utilitario.consultar(ser_precontractual.responsablePAC(pckUtilidades.CConversion.CInt(ide_prpac)));
		if(tab_datos_usuario.getTotalFilas()>0)
		{

			String secuencial_doc=generarNroDocumento(par_sec_precon_sol);
			
			archivo="CertNoDupConsultoria_"+ide_prpac+"_"+secuencial_doc;
			String str_path = utilitario.getPropiedad("rutaUpload") + "/" + carpeta;
			File path = new File(str_path);
			path.mkdirs();
			
			tab_documento_requisito.setValor("documentoadjunto_prdoc", "/upload/"+carpeta+"/"+archivo+".pdf" );
			tab_documento_requisito.setValor("presenta_prdoc","SI");
			tab_documento_requisito.setValor("nro_documento_prdoc",secuencial_doc);
			
			tab_documento_requisito.setValor("asunto_prdoc", "N/A");
			tab_documento_requisito.setValor("de_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("para_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("nro_fojas_prdoc", "1");
			tab_documento_requisito.setValor("detalle_anexos_prdoc", "N/A");
			
			Map p_parametros = new HashMap();
			p_parametros.put("ide_prpac", pckUtilidades.CConversion.CInt(ide_prpac));
			//p_parametros.put("detalle", pckUtilidades.CConversion.CStr(solicitud));
			p_parametros.put("para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("nombres_apellidos")));
			p_parametros.put("pie_para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("detalle_geare")));
			p_parametros.put("nro_documento",secuencial_doc);
			
			fil_pdf = generarPDF(p_parametros,"/reportes/rep_contratacion/rep_solicitud_noduplicidad_consultoria_resp.jasper",archivo);
			if(fil_pdf==null)
			{
				utilitario.agregarMensajeInfo("Error al generar la Respuesta...", "Favor intente nuevamente..");
				return false;
			}
			pckUtilidades.Utilitario.copiarArchivo(fil_pdf, utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+archivo+".pdf");
		}
		else
		{
			System.out.println("No configurado el responsable para la emision de la Respuesta...");
			utilitario.agregarMensajeInfo("No configurado el responsable para la emision de la Respuesta...", "Favor carge el documento manualmente...");	
		}
		tab_documento_requisito.guardar();
		
		guardarPantalla();
		//tab_documento_requisito.fin();
		insertarRequisitoSecundario();
		
		dia_anexo.dibujar();//

		return true;
	}
	
	public boolean generarSolicitudNormalizados(String ide_prpac, String ide_prpro, String ide_preta, String solicitud)
	{		
		insertarRequisito();

		TablaGenerica tab_datos_usuario=utilitario.consultar(ser_precontractual.cargarDatosUsuarioEtapaAEnviar(pckUtilidades.CConversion.CInt(ide_prpro), pckUtilidades.CConversion.CInt(ide_preta)));
		if(tab_datos_usuario.getTotalFilas()>0)
		{
			String secuencial_doc=generarNroDocumento(par_sec_precon_sol);
			
			archivo="solicitud_normalizados_"+ide_prpac+"_"+secuencial_doc;
			String str_path = utilitario.getPropiedad("rutaUpload") + "/" + carpeta;
			File path = new File(str_path);
			path.mkdirs();
			
			tab_documento_requisito.setValor("documentoadjunto_prdoc", "/upload/"+carpeta+"/"+archivo+".pdf" );
			tab_documento_requisito.setValor("presenta_prdoc","SI");
			tab_documento_requisito.setValor("nro_documento_prdoc",secuencial_doc);
			
			tab_documento_requisito.setValor("asunto_prdoc", "N/A");
			tab_documento_requisito.setValor("de_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("para_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("nro_fojas_prdoc", "1");
			tab_documento_requisito.setValor("detalle_anexos_prdoc", "N/A");
			
			Map p_parametros = new HashMap();
			p_parametros.put("ide_prpac", pckUtilidades.CConversion.CInt(ide_prpac));
			p_parametros.put("para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("nombres_apellidos")));
			p_parametros.put("pie_para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("detalle_geare")));
			p_parametros.put("detalle", pckUtilidades.CConversion.CStr(solicitud));
			p_parametros.put("nro_documento",secuencial_doc);
			
			fil_pdf = generarPDF(p_parametros,"/reportes/rep_contratacion/rep_solicitud_normalizados.jasper",archivo);
			if(fil_pdf==null)
			{
				utilitario.agregarMensajeInfo("Error al generar la solicitud...", "Favor intente nuevamente..");
				return false;
			}
			pckUtilidades.Utilitario.copiarArchivo(fil_pdf, utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+archivo+".pdf");
		}
		else
			utilitario.agregarMensajeInfo("No configurado el responsable para la emision de la solicitud...", "Favor carge el documento manualmente...");
		
		tab_documento_requisito.guardar();
		
		guardarPantalla();
		
		firmar(archivo);
		return true;
	}
	
	public boolean generarCertificacionNormalizados(String ide_prpac, String ide_prpro, String ide_preta, String solicitud)
	{		
		insertarRequisito();
				
		TablaGenerica tab_datos_usuario=utilitario.consultar(ser_precontractual.responsablePAC(pckUtilidades.CConversion.CInt(ide_prpac)));
		if(tab_datos_usuario.getTotalFilas()>0)
		{

			String secuencial_doc=generarNroDocumento(par_sec_precon_sol);
			
			archivo="CertNormalizados_"+ide_prpac+"_"+secuencial_doc;
			String str_path = utilitario.getPropiedad("rutaUpload") + "/" + carpeta;
			File path = new File(str_path);
			path.mkdirs();
			
			tab_documento_requisito.setValor("documentoadjunto_prdoc", "/upload/"+carpeta+"/"+archivo+".pdf" );
			tab_documento_requisito.setValor("presenta_prdoc","SI");
			tab_documento_requisito.setValor("nro_documento_prdoc",secuencial_doc);
			
			tab_documento_requisito.setValor("asunto_prdoc", "N/A");
			tab_documento_requisito.setValor("de_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("para_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("nro_fojas_prdoc", "1");
			tab_documento_requisito.setValor("detalle_anexos_prdoc", "N/A");
			
			Map p_parametros = new HashMap();
			p_parametros.put("ide_prpac", pckUtilidades.CConversion.CInt(ide_prpac));
			p_parametros.put("anexo", pckUtilidades.CConversion.CStr(solicitud));
			p_parametros.put("para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("nombres_apellidos")));
			p_parametros.put("pie_para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("detalle_geare")));
			p_parametros.put("nro_documento",secuencial_doc);
			
			fil_pdf = generarPDF(p_parametros,"/reportes/rep_contratacion/rep_solicitud_normalizados_resp.jasper",archivo);
			if(fil_pdf==null)
			{
				utilitario.agregarMensajeInfo("Error al generar la Respuesta...", "Favor intente nuevamente..");
				return false;
			}
			pckUtilidades.Utilitario.copiarArchivo(fil_pdf, utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+archivo+".pdf");
		}
		else
		{
			System.out.println("No configurado el responsable para la emision de la Respuesta...");
			utilitario.agregarMensajeInfo("No configurado el responsable para la emision de la Respuesta...", "Favor carge el documento manualmente...");	
		}
		tab_documento_requisito.guardar();
		
		guardarPantalla();
		//tab_documento_requisito.fin();
		insertarRequisitoSecundario();
		
		dia_anexo.dibujar();//

		return true;
	}
	
	public boolean generarSolicitudPOA(String ide_prpac, String ide_prpro, String ide_preta)
	{		
		insertarRequisito();

		TablaGenerica tab_datos_usuario=utilitario.consultar(ser_precontractual.cargarDatosUsuarioEtapaAEnviar(pckUtilidades.CConversion.CInt(ide_prpro), pckUtilidades.CConversion.CInt(ide_preta)));
		if(tab_datos_usuario.getTotalFilas()>0)
		{
			String secuencial_doc=generarNroDocumento(par_sec_precon_sol);
			
			archivo="solicitud_poa_"+ide_prpac+"_"+secuencial_doc;
			String str_path = utilitario.getPropiedad("rutaUpload") + "/" + carpeta;
			File path = new File(str_path);
			path.mkdirs();
			
			tab_documento_requisito.setValor("documentoadjunto_prdoc", "/upload/"+carpeta+"/"+archivo+".pdf" );
			tab_documento_requisito.setValor("presenta_prdoc","SI");
			tab_documento_requisito.setValor("nro_documento_prdoc",secuencial_doc);
			
			tab_documento_requisito.setValor("asunto_prdoc", "N/A");
			tab_documento_requisito.setValor("de_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("para_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("nro_fojas_prdoc", "1");
			tab_documento_requisito.setValor("detalle_anexos_prdoc", "N/A");
			
			Map p_parametros = new HashMap();
			p_parametros.put("ide_prpac", pckUtilidades.CConversion.CInt(ide_prpac));
			p_parametros.put("para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("nombres_apellidos")));
			p_parametros.put("pie_para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("detalle_geare")));
			p_parametros.put("nro_documento",secuencial_doc);
			
			fil_pdf = generarPDF(p_parametros,"/reportes/rep_contratacion/rep_solicitud_poa.jasper",archivo);
			if(fil_pdf==null)
			{
				utilitario.agregarMensajeInfo("Error al generar la solicitud...", "Favor intente nuevamente..");
				return false;
			}
			pckUtilidades.Utilitario.copiarArchivo(fil_pdf, utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+archivo+".pdf");
		}
		else
			utilitario.agregarMensajeInfo("No configurado el responsable para la emision de la solicitud...", "Favor carge el documento manualmente...");
		
		tab_documento_requisito.guardar();
		
		guardarPantalla();
		
		firmar(archivo);
		return true;
	}
	
	public boolean generarSolicitudPAC(String ide_prpac, String ide_prpro, String ide_preta)
	{		
		insertarRequisito();

		TablaGenerica tab_datos_usuario=utilitario.consultar(ser_precontractual.cargarDatosUsuarioEtapaAEnviar(pckUtilidades.CConversion.CInt(ide_prpro), pckUtilidades.CConversion.CInt(ide_preta)));
		if(tab_datos_usuario.getTotalFilas()>0)
		{
			String secuencial_doc=generarNroDocumento(par_sec_precon_sol);
			
			archivo="solicitud_pac_"+ide_prpac+"_"+secuencial_doc;
			String str_path = utilitario.getPropiedad("rutaUpload") + "/" + carpeta;
			File path = new File(str_path);
			path.mkdirs();
			
			tab_documento_requisito.setValor("documentoadjunto_prdoc", "/upload/"+carpeta+"/"+archivo+".pdf" );
			tab_documento_requisito.setValor("presenta_prdoc","SI");
			tab_documento_requisito.setValor("nro_documento_prdoc",secuencial_doc);
			
			tab_documento_requisito.setValor("asunto_prdoc", "N/A");
			tab_documento_requisito.setValor("de_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("para_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("nro_fojas_prdoc", "1");
			tab_documento_requisito.setValor("detalle_anexos_prdoc", "N/A");
			
			Map p_parametros = new HashMap();
			p_parametros.put("ide_prpac", pckUtilidades.CConversion.CInt(ide_prpac));
			p_parametros.put("para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("nombres_apellidos")));
			p_parametros.put("pie_para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("detalle_geare")));
			p_parametros.put("nro_documento",secuencial_doc);
			
			fil_pdf = generarPDF(p_parametros,"/reportes/rep_contratacion/rep_solicitud_pac.jasper",archivo);
			if(fil_pdf==null)
			{
				utilitario.agregarMensajeInfo("Error al generar la solicitud...", "Favor intente nuevamente..");
				return false;
			}
			pckUtilidades.Utilitario.copiarArchivo(fil_pdf, utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+archivo+".pdf");
		}
		else
			utilitario.agregarMensajeInfo("No configurado el responsable para la emision de la solicitud...", "Favor carge el documento manualmente...");
		
		tab_documento_requisito.guardar();
		
		guardarPantalla();
		
		firmar(archivo);
		return true;
	}
	
	public boolean generarCertificacionPAC(String ide_prpac, String ide_prpro, String ide_preta, String solicitud)
	{		
		insertarRequisito();
				
		TablaGenerica tab_datos_usuario=utilitario.consultar(ser_precontractual.responsablePAC(pckUtilidades.CConversion.CInt(ide_prpac)));
		if(tab_datos_usuario.getTotalFilas()>0)
		{

			String secuencial_doc=generarNroDocumento(par_sec_precon_sol);
			
			archivo="CertificacionPAC_"+ide_prpac+"_"+secuencial_doc;
			String str_path = utilitario.getPropiedad("rutaUpload") + "/" + carpeta;
			File path = new File(str_path);
			path.mkdirs();
			
			tab_documento_requisito.setValor("documentoadjunto_prdoc", "/upload/"+carpeta+"/"+archivo+".pdf" );
			tab_documento_requisito.setValor("presenta_prdoc","SI");
			tab_documento_requisito.setValor("nro_documento_prdoc",secuencial_doc);
			
			tab_documento_requisito.setValor("asunto_prdoc", "N/A");
			tab_documento_requisito.setValor("de_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("para_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("nro_fojas_prdoc", "1");
			tab_documento_requisito.setValor("detalle_anexos_prdoc", "N/A");
			
			Map p_parametros = new HashMap();
			p_parametros.put("ide_prpac", pckUtilidades.CConversion.CInt(ide_prpac));
			p_parametros.put("anexo", pckUtilidades.CConversion.CStr(solicitud));
			p_parametros.put("para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("nombres_apellidos")));
			p_parametros.put("pie_para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("detalle_geare")));
			p_parametros.put("nro_documento",secuencial_doc);
			
			fil_pdf = generarPDF(p_parametros,"/reportes/rep_contratacion/rep_solicitud_pac_resp.jasper",archivo);
			if(fil_pdf==null)
			{
				utilitario.agregarMensajeInfo("Error al generar la Respuesta...", "Favor intente nuevamente..");
				return false;
		    	}
			pckUtilidades.Utilitario.copiarArchivo(fil_pdf, utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+archivo+".pdf");
		}
		else
		{
			System.out.println("No configurado el responsable para la emision de la Respuesta...");
			utilitario.agregarMensajeInfo("No configurado el responsable para la emision de la Respuesta...", "Favor carge el documento manualmente...");	
		}
		tab_documento_requisito.guardar();
		
		guardarPantalla();
		//tab_documento_requisito.fin();
		insertarRequisitoSecundario();
		
		dia_anexo.dibujar();//

		return true;
	}
	
	public boolean generarSolicitudCertificacionPresupuestaria(String ide_prpac, String ide_prpro, String ide_preta, String solicitud)
	{		
		insertarRequisito();

		TablaGenerica tab_datos_usuario=utilitario.consultar(ser_precontractual.cargarDatosUsuarioEtapaAEnviar(pckUtilidades.CConversion.CInt(ide_prpro), pckUtilidades.CConversion.CInt(ide_preta)));
		if(tab_datos_usuario.getTotalFilas()>0)
		{
			String secuencial_doc=generarNroDocumento(par_sec_precon_sol);
			
			archivo="solicitud_certificacion_pres_"+ide_prpac+"_"+secuencial_doc;
			String str_path = utilitario.getPropiedad("rutaUpload") + "/" + carpeta;
			File path = new File(str_path);
			path.mkdirs();
			
			tab_documento_requisito.setValor("documentoadjunto_prdoc", "/upload/"+carpeta+"/"+archivo+".pdf" );
			tab_documento_requisito.setValor("presenta_prdoc","SI");
			tab_documento_requisito.setValor("nro_documento_prdoc",secuencial_doc);
			
			tab_documento_requisito.setValor("asunto_prdoc", "N/A");
			tab_documento_requisito.setValor("de_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("para_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("nro_fojas_prdoc", "1");
			tab_documento_requisito.setValor("detalle_anexos_prdoc", "N/A");
			
			Map p_parametros = new HashMap();
			p_parametros.put("ide_prpac", pckUtilidades.CConversion.CInt(ide_prpac));
			p_parametros.put("para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("nombres_apellidos")));
			p_parametros.put("pie_para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("detalle_geare")));
			p_parametros.put("detalle", pckUtilidades.CConversion.CStr(solicitud));
			p_parametros.put("nro_documento",secuencial_doc);
			
			fil_pdf = generarPDF(p_parametros,"/reportes/rep_contratacion/rep_solicitud_certificacion_pres.jasper",archivo);
			if(fil_pdf==null)
			{
				utilitario.agregarMensajeInfo("Error al generar la solicitud...", "Favor intente nuevamente..");
				return false;
			}
			pckUtilidades.Utilitario.copiarArchivo(fil_pdf, utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+archivo+".pdf");
		}
		else
			utilitario.agregarMensajeInfo("No configurado el responsable para la emision de la solicitud...", "Favor carge el documento manualmente...");
		
		tab_documento_requisito.guardar();
		
		guardarPantalla();
		
		firmar(archivo);
		return true;
	}
	
	public boolean generarSolicitudVerificacionPAC(String ide_prpac, String ide_prpro, String ide_preta)
	{		
		insertarRequisito();

		TablaGenerica tab_datos_usuario=utilitario.consultar(ser_precontractual.cargarDatosUsuarioEtapaAEnviar(pckUtilidades.CConversion.CInt(ide_prpro), pckUtilidades.CConversion.CInt(ide_preta)));
		if(tab_datos_usuario.getTotalFilas()>0)
		{
			String secuencial_doc=generarNroDocumento(par_sec_precon_sol);
			archivo="verificacion_pac_"+ide_prpac+"_"+secuencial_doc;
			String str_path = utilitario.getPropiedad("rutaUpload") + "/" + carpeta;
			File path = new File(str_path);
			path.mkdirs();
			
			tab_documento_requisito.setValor("documentoadjunto_prdoc", "/upload/"+carpeta+"/"+archivo+".pdf" );
			tab_documento_requisito.setValor("presenta_prdoc","SI");
			tab_documento_requisito.setValor("nro_documento_prdoc",secuencial_doc);
			
			tab_documento_requisito.setValor("asunto_prdoc", "N/A");
			tab_documento_requisito.setValor("de_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("para_nombre_prdoc", "N/A");
			tab_documento_requisito.setValor("nro_fojas_prdoc", "1");
			tab_documento_requisito.setValor("detalle_anexos_prdoc", "N/A");
			
			Map p_parametros = new HashMap();
			p_parametros.put("ide_prpac", pckUtilidades.CConversion.CInt(ide_prpac));
			p_parametros.put("para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("nombres_apellidos")));
			p_parametros.put("pie_para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("detalle_geare")));
			p_parametros.put("nro_documento",secuencial_doc);
			
			fil_pdf = generarPDF(p_parametros,"/reportes/rep_contratacion/rep_verificacion_pac.jasper",archivo);
			if(fil_pdf==null)
			{
				utilitario.agregarMensajeInfo("Error al generar la solicitud...", "Favor intente nuevamente..");
				return false;
			}
			pckUtilidades.Utilitario.copiarArchivo(fil_pdf, utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+archivo+".pdf");
		}
		else
			utilitario.agregarMensajeInfo("No configurado el responsable para la emision de la solicitud...", "Favor carge el documento manualmente...");
		
		tab_documento_requisito.guardar();
		
		guardarPantalla();
		
		firmar(archivo);
		return true;
	}
	
	/*public void generarSolicitudConvocarMesa(String ide_prpac, String ide_prpro, String ide_preta, String solicitud)
	{		
		insertarRequisito();

		TablaGenerica tab_datos_usuario=utilitario.consultar(ser_precontractual.cargarDatosUsuarioEtapaAEnviar(pckUtilidades.CConversion.CInt(ide_prpro), pckUtilidades.CConversion.CInt(ide_preta)));
		if(tab_datos_usuario.getTotalFilas()>0)
		{
			archivo="Convocar_mesa_"+ide_prpac;
			String str_path = utilitario.getPropiedad("rutaUpload") + "/" + carpeta;
			File path = new File(str_path);
			path.mkdirs();
			
			tab_documento_requisito.setValor("documentoadjunto_prdoc", "/upload/"+carpeta+"/"+archivo+".pdf" );
			tab_documento_requisito.setValor("presenta_prdoc","SI");
			
			Map p_parametros = new HashMap();
			p_parametros.put("ide_prpac", pckUtilidades.CConversion.CInt(ide_prpac));
			p_parametros.put("para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("nombres_apellidos")));
			p_parametros.put("pie_para", pckUtilidades.CConversion.CStr(tab_datos_usuario.getValor("detalle_geare")));
			p_parametros.put("detalle", pckUtilidades.CConversion.CStr(solicitud));
			
			fil_pdf = generarPDF(p_parametros,"/reportes/rep_contratacion/rep_convocar_mesa.jasper",archivo);
			pckUtilidades.Utilitario.copiarArchivo(fil_pdf, utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+archivo+".pdf");
		}
		else
			utilitario.agregarMensajeInfo("No configurado el responsable para la emision de la solicitud...", "Favor carge el documento manualmente...");
		
		tab_documento_requisito.guardar();
		
		guardarPantalla();
		
		pas_clave_firma.setValue("");
		utilitario.addUpdate("pas_clave_firma");
		dia_firma_electronica.setFooter("Archivo Origen a Firmar: "+archivo);
		dia_firma_electronica.dibujar();
	}*/
	
	public void firmar(String archivo)
	{
		System.out.println("Firmando el archivo: "+archivo);
		enviarMailFirmado=true;
		pas_clave_firma.setValue("");
		che_firma.setValue("false");
		utilitario.addUpdate("pas_clave_firma,che_firma");
		dia_firma_electronica.setFooter("Archivo Origen a Firmar: "+archivo);
		dia_firma_electronica.dibujar();
	}
	
	public String generarNroDocumento(String modulo)
	{
		String abreviaturaArea="NA";
		String abreviaturaPare="NA";
		
		TablaGenerica tab_departamento= utilitario.consultar(ser_generalAdm.servicioDepartamento("true", empleado));
		if(tab_departamento.getTotalFilas()>0)
		{
			abreviaturaArea=pckUtilidades.CConversion.CStr(tab_departamento.getValor("abreviatura_geare"));
			abreviaturaPare=pckUtilidades.CConversion.CStr(tab_departamento.getValor("gabrev"));
		}
		
		TablaGenerica tab_anio = utilitario.consultar("SELECT ide_geani, detalle_geani FROM gen_anio where ide_geani="+com_anio.getValue());				
		String anio=tab_anio.getValor("detalle_geani");
		//String secuencial_doc=tipo.toUpperCase()+"-EMGIRS-EP-"+abreviaturaPare.toUpperCase()+"-"+abreviaturaArea.toUpperCase()+"-"+anio+"-"+ pckUtilidades.Utilitario.padLeft(ser_contabilidad.numeroSecuencial(modulo), 4) +"-ERP";
		String secuencial_doc="EMGIRS-EP-"+abreviaturaPare.toUpperCase()+"-"+abreviaturaArea.toUpperCase()+"-"+anio+"-"+ pckUtilidades.Utilitario.padLeft(ser_contabilidad.numeroSecuencial(modulo), 4) +"-ERP";
		utilitario.agregarMensaje("Guardando secuencial ", "Nro: "+secuencial_doc);
		System.out.println("Guardando secuencial Nro: "+secuencial_doc);
		ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(modulo), modulo);
		
		return secuencial_doc;
	}
	
	public File generarPDF(Map parametros, String reporte, String nombre) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			JasperPrint jasperPrint = null;
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

				try {
					parametros.put("SUBREPORT_DIR", utilitario.getURL());
				} catch (Exception e) {
				}

				jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, utilitario.getConexion().getConnection());

			} catch (Exception e) {
				System.out.println("Error ejecutar generar: " + e.getMessage());
			}

			JRExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			File fil_reporte = new File(ec.getRealPath("/reportes/" + nombre + ".pdf"));
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, fil_reporte);
			exporter.exportReport();
			return fil_reporte;
		} catch (Exception ex) {
			System.out.println("Error generar: " + ex.getMessage());
			ex.printStackTrace();
		}
		return null;
	}
	
	public void setClave()
	{		
		//System.out.println("set strClave: "+pckUtilidades.CConversion.CStr(txt_clave_firma.getValue()));
		che_firma.setValue("true");
		utilitario.addUpdate("pas_clave_firma,che_firma");
		utilitario.agregarMensajeInfo("Clave Firma", "Su clave ha sido ingresada correctamente...");
	}
	
	public void firmarArchivo(FileUploadEvent evt){
		try
		{
			if(fil_pdf == null)
			{
				utilitario.agregarMensaje("No se pudo firmar", "No se cargo correctante el archivo origen");
				//dia_firma_electronica.cerrar();
				return;
			}
			
			String strClave=pckUtilidades.CConversion.CStr(pas_clave_firma.getValue());
			//System.out.println("strClave: "+strClave);
			if(strClave.length()<4)
			{
				utilitario.agregarMensaje("No se pudo firmar", "No se ingreso la clave de la firma electronica");
				utilitario.agregarMensaje("No se pudo firmar", "Presione el Boton ingresar clave...");
				//dia_firma_electronica.cerrar();
				return;
			}
			
			if(!che_firma.getValue().toString().equalsIgnoreCase("true")){
				utilitario.agregarMensaje("No se pudo firmar", "No se ingreso la clave de la firma electronica");
				utilitario.agregarMensaje("No se pudo firmar", "Presione el Boton ingresar clave...");
				return;	
			}
			
			if(evt.getFile().getInputstream()==null)
			{
				utilitario.agregarMensaje("No se pudo firmar", "No se cargo la firma electronica");
				System.out.println("Error No se pudo firmar: No se cargo la firma electronica: "+evt.getFile().getFileName());
				//dia_firma_electronica.cerrar();
				return;
			}
			
			String archivoSing64 = pckUtilidades.consumoServiciosCore.firmarAPI_ERP(pckUtilidades.Utilitario.inputStreamConvertToArray64(evt.getFile().getInputstream()), strClave, pckUtilidades.Utilitario.fileConvertToArray64(fil_pdf));
			
			if(archivoSing64.length()<5)
			{
				utilitario.agregarMensaje("No se pudo firmar", "No se firmo correctamente");
				//dia_firma_electronica.cerrar();
				return;
			}
			
			File filSing=pckUtilidades.Utilitario.array64ConvertToFile(archivoSing64);
			
			if(filSing==null)
			{
				utilitario.agregarMensajeError("No se pudo firmar", "No se firmo correctamente, favor revise su archivo de firma electronica o su clave esta inconrrecta...");
				//dia_firma_electronica.cerrar();
				return;
			}
			
			String ruta=utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+archivo+".pdf"; 
			
			pckUtilidades.Utilitario.copiarArchivo(filSing, ruta);
			
			TablaGenerica tab_Gprecontractual = utilitario.consultar("SELECT * FROM precon_precontractual where ide_prpre="+priv_ide_prpre);
			
			/*enviarMailActividad(pckUtilidades.CConversion.CInt(tab_Gprecontractual.getValor("ide_actual_geedp")),tab_Gprecontractual.getValor("codigo_prpre"),tab_Gprecontractual.getValor("descripcion_prpre"),
					pckUtilidades.CConversion.CInt(tab_Gprecontractual.getValor("ide_pretp")), tab_Gprecontractual.getValor("actividad_actual_prpre"),tab_Gprecontractual.getValor("fecha_prpre"),
					tab_Gprecontractual.getValor("hora_prpre"),archivo+".pdf","/upload/"+carpeta+"/"+archivo+".pdf");
	*/
			utilitario.agregarMensaje("Firmado Correctamente", "El archivo: "+archivo);
			System.out.println("Se firmo Correctamente El archivo: "+archivo);
		}
		catch(Exception ex)
		{
			utilitario.agregarMensaje("No se pudo firmar", ex.getMessage());
			System.out.println("Error firmarArchivo: "+ex.getMessage());
			return;
		}
		
		dia_firma_electronica.cerrar();
	}
	
	public void requisitoBorrar() 
	{	
		tab_documento_requisito.setValor("presenta_prdoc","NO");
		tab_documento_requisito.modificar(tab_documento_requisito.getFilaActual());
		tab_documento_requisito.guardar();
		utilitario.agregarMensaje("Guardado Correctamente", "Requisito listo para ser regenerado...");
		guardarPantalla();
	}
	
	public void borrarArchivo()
	{
		if(tab_documento_requisito.isFocus())
		{
			tab_documento_requisito.eliminar();
			utilitario.agregarMensaje("Administrador", "Recuerde dar clic en guardar para eliminar definitivamente...");
		}
	}
	
	public void borrarAnexo()
	{
		if(tab_documento_requisito_secundario.isFocus())
		{
			tab_documento_requisito_secundario.eliminar();
			utilitario.agregarMensaje("Administrador", "Recuerde dar clic en guardar para eliminar definitivamente...");
		}
	}
	
	public void cargarAnexo(FileUploadEvent evt){
		try
		{
			
			if(evt.getFile().getInputstream()==null)
			{
				utilitario.agregarMensaje("No se pudo cargar", "No se cargo el anexo");
				System.out.println("Error No se pudo cargar: No se cargo el anexo: "+evt.getFile().getFileName());
				return;
			}

			String nombreAnexo = evt.getFile().getFileName();
			nombreAnexo=nombreAnexo.replace(' ', '_');
			System.out.println("nombreAnexo: "+nombreAnexo);
			
			File fil_anexo = pckUtilidades.Utilitario.array64ConvertToFile(pckUtilidades.Utilitario.inputStreamConvertToArray64(evt.getFile().getInputstream()));
			
			tab_documento_requisito_secundario.setValor("descripcion_prdoc",nombreAnexo);
			tab_documento_requisito_secundario.setValor("documentoadjunto_prdoc", "/upload/"+carpeta+"/"+nombreAnexo );
			tab_documento_requisito_secundario.setValor("presenta_prdoc","SI");			
			
			tab_documento_requisito_secundario.modificar(tab_documento_requisito_secundario.getFilaActual());//para que haga el update
			tab_documento_requisito_secundario.guardar();
			guardarPantalla();
			
			pckUtilidades.Utilitario.copiarArchivo(fil_anexo, utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+nombreAnexo);

			utilitario.agregarMensaje("Anexo/Certificado", "Se cargo correctamente el archivo: "+nombreAnexo);
			System.out.println("Se cargo correctamente el archivo: "+nombreAnexo);
			
			dia_anexo.cerrar();
			firmar(archivo);			
		}
		catch(Exception ex)
		{
			utilitario.agregarMensaje("No guardar el anexo", ex.getMessage());
			System.out.println("Error No guardar el anexo: "+ex.getMessage());
			return;
		}
	
	}
	
	@Override
	public void insertar() {
		
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede eliminar", "Debe Seleccionar un Año");
			return;
		}
		if(int_opcion==3)
		{
			utilitario.agregarNotificacionInfo("Registro No Editable", "Proceso en bandeja de enviados... Si el problema persiste contactese con el Administrador...");
			return;
		}
		/*if(tab_precontractual.isFocus()){
			insertarProceso();
		}else if(tab_documento_requisito.isFocus()){
			insertarRequisito();
		}*/
	}

	@Override
	public void guardar() 
	{
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede guardar", "Debe Seleccionar un Año");
			return;
		}
		
		if(int_opcion==3)
		{
			utilitario.agregarNotificacionInfo("Registro No Editable", "Proceso en bandeja de enviados... Si el problema persiste contactese con el Administrador...");
			return;
		}
		
		//List perfilUsuarioConectado=utilitario.getConexion().consultar(ser_generalAdm.getPerfilConectado(utilitario.getVariable("IDE_PERF")));
		boolean finalizado=false;
		/*if(pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==8 || pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"))==9){
			if(tab_precontractual.getValor("se_encuentra_catalogoe_prpre")!=null && tab_precontractual.getValor("aprueba_informetm_prpre")==null){
				utilitario.agregarMensajeInfo("El proceso no puede guardar ya tiene que unirse, es actividad paralela.", "");
			}
		}*/
		if(tab_precontractual.getValor("estado_proceso_prpre").equals("REGISTRADO") || tab_precontractual.getValor("estado_proceso_prpre").equals("EN PROCESO") || tab_precontractual.getValor("estado_proceso_prpre").equals("ENVIADO")){
			finalizado=true;
		}
		if(tab_precontractual.getValor("estado_proceso_prpre").contains("NO APROBADO") || tab_precontractual.getValor("estado_proceso_prpre").contains("FINALIZADO"))
		{
			utilitario.agregarNotificacionInfo("ESTADO DEL PROCESO: ",tab_precontractual.getValor("estado_proceso_prpre"));			
			return;
		}
		if(!finalizado){
			utilitario.agregarMensajeInfo("El proceso ya ha finalizado, no se puede modificar.", "");
		}else{
			if(tab_precontractual.guardar()){
				if(tab_precontractual.isFilaInsertada()){
					ser_precontractual.guardaSecuencial(ser_precontractual.numeroSecuencial(p_sec_precontractual), p_sec_precontractual);
				}
				//validar que no pueda modificar el registro anterior	
				if(!validarPerfil(perfilUsuarioConectado,"ADMINISTRADOR")){
					int ide_actual_preta=pckUtilidades.CConversion.CInt(tab_precontractual.getValor("ide_actual_preta"));
					TablaGenerica tab_precon_documento_requisito = utilitario.consultar("SELECT ide_prdoc as codigo,ide_prdoc, ide_prpre, ide_prreq, etapa_prdoc FROM precon_documento_requisito "
							+ " where ide_prpre="+tab_precontractual.getValor("ide_prpre")+" and con_ide_prdoc is null and ide_prreq in (SELECT ide_prreq FROM precon_requisito where ide_preta="+ide_actual_preta+")");
					//tab_precon_documento_requisito.imprimirSql();
					if(pckUtilidades.CConversion.CInt(tab_documento_requisito.getValor("ide_prdoc"))!= pckUtilidades.CConversion.CInt(tab_precon_documento_requisito.getValor("ide_prdoc")))
					{
						tab_documento_requisito.ejecutarSql();
						if(tab_documento_requisito.getTotalFilas()>0)
						{
							tab_documento_requisito_secundario.setCondicion("con_ide_prdoc="+tab_documento_requisito.getValor("ide_prdoc"));
							tab_documento_requisito_secundario.ejecutarSql();
						}
						utilitario.agregarNotificacionInfo("No se puede guardar el adjunto", "Ubiquese en el requisito solicitado: "+tab_precon_documento_requisito.getValor("etapa_prdoc"));
						return;
					}
				}
				
				
				if(tab_documento_requisito.isFocus()){
					if(tab_documento_requisito.getTotalFilas()>0)
					{						
						if(tab_documento_requisito.getValor("documentoadjunto_prdoc")==null){
							tab_documento_requisito.setValor("presenta_prdoc","NO");
						}else{
							tab_documento_requisito.setValor("presenta_prdoc","SI");
						}
						tab_documento_requisito.guardar();
					}
					else
						utilitario.agregarMensaje("No Guardado", "Agrege un registro antes de continuar...");
				}
				
				//if(tab_documento_requisito_secundario.isFocus()){
					if(tab_documento_requisito_secundario.getTotalFilas()>0)
					{
						if(tab_documento_requisito_secundario.getValor("documentoadjunto_prdoc")==null){
							tab_documento_requisito_secundario.setValor("presenta_prdoc","NO");
						}else{
							tab_documento_requisito_secundario.setValor("presenta_prdoc","SI");
						}
					}
					else
						utilitario.agregarMensaje("Guardando sin (ANEXOS)", "Agrege un registro para de continuar (OPCIONAL)...");
				//}
				tab_documento_requisito.guardar();	
				tab_documento_requisito_secundario.guardar();
				tab_responsable_prec.guardar();
			}
			
			utilitario.agregarMensaje("Guardado Correctamente", "Presione en el boton Completar Tarea para continuar...");
			guardarPantalla();
		}
		
	}
	
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede eliminar", "Debe Seleccionar un Año");
			return;
		}
		if(int_opcion==3)
		{
			utilitario.agregarNotificacionInfo("Registro No Editable", "Proceso en bandeja de enviados... Si el problema persiste contactese con el Administrador...");
			return;
		}
		//utilitario.getTablaisFocus().eliminar();
	}
	
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		validarTareas(true);
	}
	
	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		validarTareas(true);
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		validarTareas(true);
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		validarTareas(true);
	}
	
	@Override
	public void actualizar(){
		super.actualizar();
		validarTareas(true);
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

	public Tabla getTab_seguimiento() {
		return tab_seguimiento;
	}

	public void setTab_seguimiento(Tabla tab_seguimiento) {
		this.tab_seguimiento = tab_seguimiento;
	}

	public ServicioEtapaProcedimiento getSer_EtapaProcedimiento() {
		return ser_EtapaProcedimiento;
	}

	public void setSer_EtapaProcedimiento(
			ServicioEtapaProcedimiento ser_EtapaProcedimiento) {
		this.ser_EtapaProcedimiento = ser_EtapaProcedimiento;
	}

	public ServicioRequisito getSer_Requisito() {
		return ser_Requisito;
	}

	public void setSer_Requisito(ServicioRequisito ser_Requisito) {
		this.ser_Requisito = ser_Requisito;
	}

	public ServicioSeguimiento getSer_Seguimiento() {
		return ser_Seguimiento;
	}

	public void setSer_Seguimiento(ServicioSeguimiento ser_Seguimiento) {
		this.ser_Seguimiento = ser_Seguimiento;
	}

	public ServicioGeneralAdmPrecon getSer_generalAdm() {
		return ser_generalAdm;
	}

	public void setSer_generalAdm(ServicioGeneralAdmPrecon ser_generalAdm) {
		this.ser_generalAdm = ser_generalAdm;
	}

	public ServicioSeguridad getSer_seguridad() {
		return ser_seguridad;
	}

	public void setSer_seguridad(ServicioSeguridad ser_seguridad) {
		this.ser_seguridad = ser_seguridad;
	}

	public ServicioPrecontractual getSer_precontractual() {
		return ser_precontractual;
	}

	public void setSer_precontractual(ServicioPrecontractual ser_precontractual) {
		this.ser_precontractual = ser_precontractual;
	}

	public ServicioEtapaRequisito getSer_etapa_requisito() {
		return ser_etapa_requisito;
	}

	public void setSer_etapa_requisito(ServicioEtapaRequisito ser_etapa_requisito) {
		this.ser_etapa_requisito = ser_etapa_requisito;
	}

	public ServicioEtapa getSer_etapa() {
		return ser_etapa;
	}

	public void setSer_etapa(ServicioEtapa ser_etapa) {
		this.ser_etapa = ser_etapa;
	}

	public ServicioRuta getSer_ruta() {
		return ser_ruta;
	}

	public void setSer_ruta(ServicioRuta ser_ruta) {
		this.ser_ruta = ser_ruta;
	}

	public ServicioProcedimiento getSer_procedimiento() {
		return ser_procedimiento;
	}

	public void setSer_procedimiento(ServicioProcedimiento ser_procedimiento) {
		this.ser_procedimiento = ser_procedimiento;
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

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public Efecto getEfecto() {
		return efecto;
	}

	public void setEfecto(Efecto efecto) {
		this.efecto = efecto;
	}

	public Division getDiv_division() {
		return div_division;
	}

	public void setDiv_division(Division div_division) {
		this.div_division = div_division;
	}

	public Radio getRad_si_no() {
		return rad_si_no;
	}

	public void setRad_si_no(Radio rad_si_no) {
		this.rad_si_no = rad_si_no;
	}

	public Dialogo getDia_aprobar() {
		return dia_aprobar;
	}

	public void setDia_aprobar(Dialogo dia_aprobar) {
		this.dia_aprobar = dia_aprobar;
	}

	public SeleccionTabla getSet_pac() {
		return set_pac;
	}

	public void setSet_pac(SeleccionTabla set_pac) {
		this.set_pac = set_pac;
	}

	public OutputLink getBot_plantilla() {
		return bot_plantilla;
	}

	public void setBot_plantilla(OutputLink bot_plantilla) {
		this.bot_plantilla = bot_plantilla;
	}

	public OutputLink getBot_manuales() {
		return bot_manuales;
	}

	public void setBot_manuales(OutputLink bot_manuales) {
		this.bot_manuales = bot_manuales;
	}

	public Tabla getTab_documento_requisito_secundario() {
		return tab_documento_requisito_secundario;
	}

	public void setTab_documento_requisito_secundario(
			Tabla tab_documento_requisito_secundario) {
		this.tab_documento_requisito_secundario = tab_documento_requisito_secundario;
	}


	public Combo getCom_actividadesR() {
		return com_actividadesR;
	}

	public void setCom_actividadesR(Combo com_actividadesR) {
		this.com_actividadesR = com_actividadesR;
	}

	public Tabla getTab_partida_poa() {
		return tab_partida_poa;
	}

	public void setTab_partida_poa(Tabla tab_partida_poa) {
		this.tab_partida_poa = tab_partida_poa;
	}

	public Tabla getTab_pre_pac() {
		return tab_pre_pac;
	}

	public void setTab_pre_pac(Tabla tab_pre_pac) {
		this.tab_pre_pac = tab_pre_pac;
	}

	public SeleccionTabla getSet_lineas_poa() {
		return set_lineas_poa;
	}

	public void setSet_lineas_poa(SeleccionTabla set_lineas_poa) {
		this.set_lineas_poa = set_lineas_poa;
	}

	public Tabla getTab_responsable_prec() {
		return tab_responsable_prec;
	}

	public void setTab_responsable_prec(Tabla tab_responsable_prec) {
		this.tab_responsable_prec = tab_responsable_prec;
	}

	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}

	public Dialogo getDia_solicitud() {
		return dia_solicitud;
	}

	public void setDia_solicitud(Dialogo dia_solicitud) {
		this.dia_solicitud = dia_solicitud;
	}

	public Dialogo getDia_firma_electronica() {
		return dia_firma_electronica;
	}

	public void setDia_firma_electronica(Dialogo dia_firma_electronica) {
		this.dia_firma_electronica = dia_firma_electronica;
	}

	public Upload getUpl_archivoFirma() {
		return upl_archivoFirma;
	}

	public void setUpl_archivoFirma(Upload upl_archivoFirma) {
		this.upl_archivoFirma = upl_archivoFirma;
	}

	public Password getPas_clave_firma() {
		return pas_clave_firma;
	}

	public void setPas_clave_firma(Password pas_clave_firma) {
		this.pas_clave_firma = pas_clave_firma;
	}

	public Check getChe_firma() {
		return che_firma;
	}

	public void setChe_firma(Check che_firma) {
		this.che_firma = che_firma;
	}


	
}
