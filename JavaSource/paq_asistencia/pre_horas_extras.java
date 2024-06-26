package paq_asistencia;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import com.lowagie.text.pdf.AcroFields.Item;

import paq_anticipos.ejb.ServicioAnticipo;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
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
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;


public class pre_horas_extras extends Pantalla {

	private Tabla tab_permisos=new Tabla();
	private Tabla tab_det_hor_ext=new Tabla();
	private AutoCompletar aut_empleado = new AutoCompletar();
	private Confirmar con_guardar=new Confirmar();
	@EJB
	private ServicioEmpleado ser_empleado=(ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	@EJB
	private ServicioNomina ser_nomina=(ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();

	private Tabla tab_horas_extra=new Tabla();
	private Dialogo dia_horas_extras=new Dialogo();


	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);

	private Etiqueta eti_tot_hor_ext=new Etiqueta();
	private Dialogo dia_anulado=new Dialogo(); 
	private AreaTexto are_tex_razon_anula=new AreaTexto();
	private Texto tex_documento_anula=new Texto();
	private Calendario cal_fecha_anula=new Calendario();

	private Dialogo dia_modifica_horas_extra=new Dialogo(); 
	private Texto tex_modifica_horas_extra=new Texto();

	private Etiqueta eti_fecha_solicitada=new Etiqueta();  
	private Etiqueta eti_hora_solicitada=new Etiqueta();
	private Tabla tab_horas_extra_carrito=new Tabla();
	private Tabla tab_suma_hora_aprobada=new Tabla();
	private Tabla tab_aprobacion_talento_humano=new Tabla();
	public Dialogo dia_aprobacion_talento_humano=new Dialogo();
	private Etiqueta eti_tot_talento_humano=new Etiqueta();

	


	public pre_horas_extras() {


		Boton bot_aprobar_solicitud=new Boton();
		bot_aprobar_solicitud.setValue("APROBAR SOLICITUD");
		bot_aprobar_solicitud.setMetodo("aprobarSolicitud");


		Boton bot_aprobar_horas=new Boton();
		bot_aprobar_horas.setValue("APROBAR HORAS");
		bot_aprobar_horas.setMetodo("aprobarHoras");

		Boton bot_anulado=new Boton();
		bot_anulado.setValue("ANULAR SOLICITUD");
		bot_anulado.setMetodo("anularSolicitud");

		Boton bot_aprobacion_talento_humano=new Boton();
		bot_aprobacion_talento_humano.setValue("APROBACION TALENTO HUMANO");
		bot_aprobacion_talento_humano.setMetodo("aprobacionTalentoHumano");

		
		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
		bar_botones.agregarReporte();
		// autocompletar empleado
		aut_empleado.setId("aut_empleado");
		String str_sql_emp=ser_gestion.getSqlEmpleadosAutocompletar();
		aut_empleado.setAutoCompletar(str_sql_emp);
		aut_empleado.setMetodoChange("filtrarAnticiposEmpleado");

		Etiqueta eti_colaborador=new Etiqueta("Empleado:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);
		bar_botones.agregarBoton(bot_aprobar_solicitud);
		bar_botones.agregarBoton(bot_aprobar_horas);
		bar_botones.agregarBoton(bot_anulado);
		bar_botones.agregarBoton(bot_aprobacion_talento_humano);


		//  PERMISOS (division 1)

		tab_permisos.setId("tab_permisos");
		tab_permisos.setTabla("ASI_PERMISOS_VACACION_HEXT", "IDE_ASPVH", 1);
		tab_permisos.getColumna("TIPO_ASPVH").setValorDefecto("3");// 3 horas extras 
		tab_permisos.getColumna("TIPO_ASPVH").setVisible(false);
		tab_permisos.getColumna("HORA_DESDE_ASPVH").setVisible(false);
		tab_permisos.getColumna("HORA_HASTA_ASPVH").setVisible(false);
		tab_permisos.getColumna("IDE_GTEMP").setVisible(false);
		tab_permisos.getColumna("IDE_GEEDP").setVisible(false);
		tab_permisos.getColumna("NRO_HORAS_ASPVH").setVisible(false);
		tab_permisos.getColumna("NRO_DIAS_ASPVH").setVisible(false);
		tab_permisos.getColumna("FECHA_DESDE_ASPVH").setVisible(false);
		tab_permisos.getColumna("FECHA_HASTA_ASPVH").setVisible(false);
		tab_permisos.getColumna("NRO_DOCUMENTO_ASPVH").setVisible(false);		
		tab_permisos.getColumna("APROBADO_ASPVH").setCheck();
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setValorDefecto(utilitario.getFechaActual());
		tab_permisos.getColumna("ACTIVO_ASPVH").setCheck();
		tab_permisos.getColumna("ACTIVO_ASPVH").setValorDefecto("true");
		//		tab_permisos.getColumna("IDE_ASMOT").setCombo("ASI_MOTIVO", "IDE_ASMOT", "DETALLE_ASMOT", "");
		tab_permisos.getColumna("IDE_ASMOT").setCombo("select IDE_ASMOT,DETALLE_ASMOT from ASI_MOTIVO order by DETALLE_ASMOT");		

		tab_permisos.getColumna("IDE_GEMES").setVisible(false);
		tab_permisos.getColumna("IDE_GEANI").setVisible(false);
		tab_permisos.getColumna("GEN_IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				" EMP.APELLIDO_MATERNO_GTEMP || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				"WHERE EPAR.ACTIVO_GEEDP=true");
		tab_permisos.getColumna("GEN_IDE_GEEDP").setAutoCompletar();
		//		tab_permisos.getColumna("GEN_IDE_GEEDP").setLectura(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setCombo(tab_permisos.getColumna("GEN_IDE_GEEDP").getListaCombo());
		//		tab_permisos.getColumna("GEN_IDE_GEEDP2").setLectura(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setAutoCompletar();
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setCheck();
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setValorDefecto("false");
		tab_permisos.getColumna("REGISTRO_NOVEDAD_ASPVH").setLectura(true);
		//GERENTE DE AREA GEN_IDE_GEEDP3
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setCombo(tab_permisos.getColumna("GEN_IDE_GEEDP").getListaCombo());
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setAutoCompletar();

		tab_permisos.getGrid().setColumns(4);
		tab_permisos.setTipoFormulario(true);
		tab_permisos.agregarRelacion(tab_det_hor_ext);
		tab_permisos.setCondicion("TIPO_ASPVH=3 AND IDE_GEEDP=-1");

		tab_permisos.getColumna("GEN_IDE_GEEDP").setRequerida(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP2").setRequerida(true);
		tab_permisos.getColumna("GEN_IDE_GEEDP3").setRequerida(true);				
		tab_permisos.getColumna("FECHA_SOLICITUD_ASPVH").setRequerida(true);
		tab_permisos.getColumna("IDE_GEEST").setVisible(false);


		tab_permisos.getColumna("ACTIVO_ASPVH").setLectura(true);
		tab_permisos.getColumna("ANULADO_ASPVH").setCheck();
		tab_permisos.getColumna("aprobado_tthh_aspvh").setCheck();
		

		tab_permisos.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_permisos);
		tab_permisos.setHeader("HORAS EXTRAS");

		tab_det_hor_ext.setId("tab_det_hor_ext");
		tab_det_hor_ext.setTabla("ASI_DETALLE_HORAS_EXTRAS", "IDE_ASDHE", 2);		
		tab_det_hor_ext.getColumna("ACTIVO_ASDHE").setCheck();
		tab_det_hor_ext.getColumna("ACTIVO_ASDHE").setValorDefecto("false");
		tab_det_hor_ext.getColumna("REGISTRO_NOVEDAD_ASDHE").setCheck();
		tab_det_hor_ext.getColumna("REGISTRO_NOVEDAD_ASDHE").setValorDefecto("false");
		tab_det_hor_ext.getColumna("REGISTRO_NOVEDAD_ASDHE").setVisible(false);
		tab_det_hor_ext.getColumna("REGISTRO_NOVEDAD_ASDHE").setLectura(true);				
		tab_det_hor_ext.getColumna("HORA_INICIAL_ASDHE").setMetodoChange("calaculahoras");
		tab_det_hor_ext.getColumna("HORA_INICIAL_ASDHE").setMascara("99:99:99");		
		tab_det_hor_ext.getColumna("HORA_FINAL_ASDHE").setMetodoChange("calaculahoras");
		tab_det_hor_ext.getColumna("HORA_FINAL_ASDHE").setMascara("99:99:99");		
		//tab_det_hor_ext.getColumna("NRO_HORAS_ASDHE").setEtiqueta();
		tab_det_hor_ext.getColumna("NRO_HORAS_ASDHE").alinearCentro();
		tab_det_hor_ext.setColumnaSuma("NRO_HORAS_ASDHE");
		tab_det_hor_ext.getColumna("NRO_HORAS_ASDHE").setFormatoNumero(2);

		tab_det_hor_ext.getColumna("HORA_INICIAL_ASDHE").setRequerida(true);
		tab_det_hor_ext.getColumna("HORA_FINAL_ASDHE").setRequerida(true);
		tab_det_hor_ext.getColumna("IDE_ASGRI").setVisible(false);
		tab_det_hor_ext.getColumna("ACTIVO_ASDHE").setLectura(true);
		tab_det_hor_ext.getColumna("FECHA_ASDHE").setRequerida(true);
		tab_det_hor_ext.getColumna("IDE_ASMOT").setCombo("ASI_MOTIVO", "IDE_ASMOT", "DETALLE_ASMOT", "");
		tab_det_hor_ext.getColumna("IDE_ASMOT").setLectura(true);
		tab_det_hor_ext.getColumna("NRO_HORAS_APROBADAS_ASDHE").setLectura(true);
		tab_det_hor_ext.getColumna("APROBADO_ASDHE").setCheck();
		tab_det_hor_ext.getColumna("APROBADO_ASDHE").setLectura(true);
		tab_det_hor_ext.getColumna("NOMINA_ASDHE").setCheck();
		tab_det_hor_ext.getColumna("NOMINA_ASDHE").setLectura(true);

		tab_det_hor_ext.dibujar();

		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_det_hor_ext);		
		//pat_panel2.getMenuTabla().getItem_eliminar().setRendered(false);
		tab_det_hor_ext.setHeader("DETALLE HORAS EXTRAS");

		//  DIVISION DE LA PANTALLA

		Division div_division=new Division();
		div_division.dividir2(pat_panel1,pat_panel2,"40%","H");
		agregarComponente(div_division);


		// confirmacion para guardar datos
		

		tab_horas_extra.setId("tab_horas_extra");
		tab_horas_extra.setSql("SELECT ide_asdhe,fecha_asdhe,hora_inicial_asdhe,hora_final_asdhe,nro_horas_asdhe,(case when nro_horas_aprobadas_asdhe is null then nro_horas_asdhe else nro_horas_aprobadas_asdhe end)as nro_horas_aprobadas,actividades_asdhe,registro_novedad_asdhe,activo_asdhe FROM ASI_DETALLE_HORAS_EXTRAS where ide_aspvh=-1");
		tab_horas_extra.setCampoPrimaria("ide_asdhe");

		tab_horas_extra.getColumna("registro_novedad_asdhe").setCheck();
		tab_horas_extra.getColumna("activo_asdhe").setCheck();
		tab_horas_extra.setTipoSeleccion(true);

		tab_horas_extra.onSelectCheck("seleccionaHoraExtra");
		tab_horas_extra.onUnselectCheck("quitaSeleccionHoraExtra");
		tab_horas_extra.dibujar();

		PanelTabla pat_=new PanelTabla();
		pat_.setPanelTabla(tab_horas_extra);
		pat_.getMenuTabla().getItem_insertar().setRendered(false);
		pat_.getMenuTabla().getItem_eliminar().setRendered(false);
		pat_.getMenuTabla().getItem_guardar().setRendered(false);



		eti_tot_hor_ext.setId("eti_tot_hor_ext");
		eti_tot_hor_ext.setValue("Total Horas Extras Aprobadas: 0.0");
		eti_tot_hor_ext.setStyle("font-size: 14px;color: red;font-weight: bold");

		tab_horas_extra_carrito.setId("tab_horas_extra_carrito");
		tab_horas_extra_carrito.setSql("SELECT ide_asdhe as ide_asdhe_2,fecha_asdhe,hora_inicial_asdhe,hora_final_asdhe,nro_horas_asdhe,(case when nro_horas_aprobadas_asdhe is null then nro_horas_asdhe else nro_horas_aprobadas_asdhe end)as nro_horas_aprobadas,actividades_asdhe,registro_novedad_asdhe,activo_asdhe FROM ASI_DETALLE_HORAS_EXTRAS where ide_aspvh=-1");
		tab_horas_extra_carrito.setCampoPrimaria("ide_asdhe_2");

		tab_horas_extra_carrito.getColumna("registro_novedad_asdhe").setCheck();
		tab_horas_extra_carrito.getColumna("activo_asdhe").setCheck();
		tab_horas_extra_carrito.setTipoSeleccion(true);
		tab_horas_extra_carrito.setColumnaSuma("nro_horas_aprobadas");

		tab_horas_extra_carrito.onSelectCheck("seleccionaHoraExtra2");		
		tab_horas_extra_carrito.dibujar();

		PanelTabla pat_carrito=new PanelTabla();
		pat_carrito.setPanelTabla(tab_horas_extra_carrito);
		pat_carrito.getMenuTabla().getItem_insertar().setRendered(false);
		pat_carrito.getMenuTabla().getItem_eliminar().setRendered(false);
		pat_carrito.getMenuTabla().getItem_guardar().setRendered(false);




		dia_horas_extras.setId("dia_horas_extras");

		Grid gri_matriz=new Grid();
		gri_matriz.setColumns(1);
		gri_matriz.getChildren().add(pat_);
		gri_matriz.getChildren().add(eti_tot_hor_ext);
		gri_matriz.getChildren().add(pat_carrito);
		gri_matriz.setStyle("width:" + (dia_horas_extras.getAnchoPanel() - 5) + "px;height:" + dia_horas_extras.getAltoPanel() + "px;overflow: auto;display: block;");


		dia_horas_extras.setDialogo(gri_matriz);
		dia_horas_extras.setHeight("60%");
		dia_horas_extras.setWidth("96%");
		dia_horas_extras.getBot_aceptar().setMetodo("aceptarHorasExtras");	
		dia_horas_extras.getBot_cancelar().setMetodo("cancelarHorasExtras");

		dia_horas_extras.setTitle("SELECCION DE HORAS EXTRAS");

		dia_horas_extras.setDynamic(false);
		agregarComponente(dia_horas_extras);

		cal_fecha_anula.setId("cal_fecha_anula");

		are_tex_razon_anula.setId("are_tex_razon_anula");
		are_tex_razon_anula.setStyle("width:300px;");

		Grid gri_anular_horas_extra=new Grid();
		gri_anular_horas_extra.setColumns(2);
		gri_anular_horas_extra.getChildren().add(new Etiqueta("RAZON DE ANULACIÓN"));
		gri_anular_horas_extra.getChildren().add(are_tex_razon_anula);
		gri_anular_horas_extra.getChildren().add(new Etiqueta("DOCUMENTO DE ANULACIÓN"));
		gri_anular_horas_extra.getChildren().add(tex_documento_anula);
		gri_anular_horas_extra.getChildren().add(new Etiqueta("FECHA DE ANULACIÓN"));
		gri_anular_horas_extra.getChildren().add(cal_fecha_anula);

		dia_anulado.setId("dia_anulado");
		dia_anulado.setDialogo(gri_anular_horas_extra);
		dia_anulado.setWidth("60%");
		dia_anulado.setHeight("40%");
		dia_anulado.setTitle("ANULACION DE SOLICITUD DE HORAS EXTRAS");
		dia_anulado.getBot_aceptar().setMetodo("aceptarAnulacionHorasExtras");				
		dia_anulado.setDynamic(false);
		gri_anular_horas_extra.setStyle("width:" + (dia_anulado.getAnchoPanel() - 5) + "px;overflow:auto;");
		agregarComponente(dia_anulado);


		//modifica hora extra aprobada

		eti_fecha_solicitada.setId("eti_fecha_solicitada");				
		eti_hora_solicitada.setId("eti_hora_solicitada");
		tex_modifica_horas_extra.setId("tex_modifica_horas_extra");
		tex_modifica_horas_extra.setSoloNumeros();

		Grid gri_modificar_horas_extra=new Grid();
		gri_modificar_horas_extra.setColumns(2);		
		gri_modificar_horas_extra.getChildren().add(new Etiqueta("Fecha Solicitud:"));
		gri_modificar_horas_extra.getChildren().add(eti_fecha_solicitada);
		gri_modificar_horas_extra.getChildren().add(new Etiqueta("Hora Solicitada"));
		gri_modificar_horas_extra.getChildren().add(eti_hora_solicitada);
		gri_modificar_horas_extra.getChildren().add(new Etiqueta("Hora Aprobada:"));
		gri_modificar_horas_extra.getChildren().add(tex_modifica_horas_extra);

		dia_modifica_horas_extra.setId("dia_modifica_horas_extra");
		dia_modifica_horas_extra.setDialogo(gri_modificar_horas_extra);
		dia_modifica_horas_extra.setWidth("30%");
		dia_modifica_horas_extra.setHeight("25%");
		dia_modifica_horas_extra.setTitle("MODIFICA EL NUMERO DE HORAS EXTRAS APROBADAS");
		dia_modifica_horas_extra.getBot_aceptar().setMetodo("aceptarModificarHorasExtras");				
		dia_modifica_horas_extra.setDynamic(false);


		agregarComponente(dia_modifica_horas_extra);
		
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);
		
		
		tab_aprobacion_talento_humano.setId("tab_aprobacion_talento_humano");
		tab_aprobacion_talento_humano.setSql("SELECT ide_asdhe as ide_asdhe_3,ide_asmot,fecha_asdhe,hora_inicial_asdhe,hora_final_asdhe,nro_horas_asdhe,(case when nro_horas_aprobadas_asdhe is null then nro_horas_asdhe else nro_horas_aprobadas_asdhe end)as nro_horas_aprobadas,actividades_asdhe,registro_novedad_asdhe,activo_asdhe,aprobado_asdhe FROM ASI_DETALLE_HORAS_EXTRAS where ide_aspvh=-1");
		tab_aprobacion_talento_humano.setCampoPrimaria("ide_asdhe_3");

		tab_aprobacion_talento_humano.getColumna("registro_novedad_asdhe").setCheck();
		tab_aprobacion_talento_humano.getColumna("activo_asdhe").setCheck();
		tab_aprobacion_talento_humano.getColumna("aprobado_asdhe").setCheck();
		tab_aprobacion_talento_humano.getColumna("ide_asmot").setCombo("select ide_asmot,detalle_asmot from asi_motivo where ide_asmot in("+utilitario.getVariable("p_motivos_horas_extras")+")");
		tab_aprobacion_talento_humano.getColumna("ide_asmot").setRequerida(true);
		tab_aprobacion_talento_humano.getColumna("fecha_asdhe").setLectura(true);
		tab_aprobacion_talento_humano.getColumna("hora_inicial_asdhe").setLectura(true);
		tab_aprobacion_talento_humano.getColumna("hora_final_asdhe").setLectura(true);
		tab_aprobacion_talento_humano.getColumna("nro_horas_asdhe").setLectura(false); 
		tab_aprobacion_talento_humano.getColumna("nro_horas_aprobadas").setLectura(true);
		tab_aprobacion_talento_humano.getColumna("actividades_asdhe").setLectura(true);
		tab_aprobacion_talento_humano.getColumna("registro_novedad_asdhe").setLectura(true);
		tab_aprobacion_talento_humano.getColumna("activo_asdhe").setLectura(true);
		tab_aprobacion_talento_humano.getColumna("aprobado_asdhe").setLectura(true);

		tab_aprobacion_talento_humano.setColumnaSuma("nro_horas_aprobadas");

				
		tab_aprobacion_talento_humano.dibujar();

		PanelTabla pat_talento_humano=new PanelTabla();
		pat_talento_humano.setPanelTabla(tab_aprobacion_talento_humano);
		pat_talento_humano.getMenuTabla().getItem_insertar().setRendered(false);
		pat_talento_humano.getMenuTabla().getItem_eliminar().setRendered(false);
		pat_talento_humano.getMenuTabla().getItem_guardar().setRendered(false);
		
		
		
		eti_tot_talento_humano.setId("eti_tot_talento_humano");
		eti_tot_talento_humano.setValue("Total Horas Extras Aprobadas Talento Humano: 0.0");
		eti_tot_talento_humano.setStyle("font-size: 14px;color: red;font-weight: bold");
		
		dia_aprobacion_talento_humano.setId("dia_aprobacion_talento_humano");

		Grid gri_talento_humano=new Grid();
		gri_talento_humano.setColumns(1);		
		gri_talento_humano.getChildren().add(eti_tot_talento_humano);
		gri_talento_humano.getChildren().add(pat_talento_humano);
		gri_talento_humano.setStyle("width:" + (dia_aprobacion_talento_humano.getAnchoPanel() - 5) + "px;height:" + dia_aprobacion_talento_humano.getAltoPanel() + "px;overflow: auto;display: block;");

		dia_aprobacion_talento_humano.setDialogo(gri_talento_humano);
		dia_aprobacion_talento_humano.setHeight("60%");
		dia_aprobacion_talento_humano.setWidth("96%");
		dia_aprobacion_talento_humano.getBot_aceptar().setMetodo("aceptarAprobacionTalentoHumano");	
		dia_aprobacion_talento_humano.setTitle("APROBACION DE TALENTO HUMANO");
		dia_aprobacion_talento_humano.setDynamic(false);
		agregarComponente(dia_aprobacion_talento_humano);

	}
	/*
	public void sumarHorasExtras(){
		double dou_suma=0;
		for (int i = 0; i < tab_horas_extra_carrito.getListaFilasSeleccionadas().size(); i++) {
			Fila fila=tab_horas_extra_carrito.getListaFilasSeleccionadas().get(i);
			try {
				dou_suma=dou_suma+Double.parseDouble(fila.getCampos()[5]+"");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		eti_tot_hor_ext.setValue("Total Horas Extras Aprobadas: "+utilitario.getFormatoNumero(dou_suma));
		utilitario.addUpdate("eti_tot_hor_ext");
	}
	 */

	public void seleccionaHoraExtra(SelectEvent evt){
		tab_horas_extra.seleccionarFila(evt);
		//	sumarHorasExtras();		
		TablaGenerica tab_horas_aprobadas=utilitario.consultar("SELECT ide_asdhe,fecha_asdhe,hora_inicial_asdhe,hora_final_asdhe,nro_horas_asdhe,(case when nro_horas_aprobadas_asdhe is null then nro_horas_asdhe else nro_horas_aprobadas_asdhe end) as nro_horas_aprobadas,actividades_asdhe,registro_novedad_asdhe,activo_asdhe FROM ASI_DETALLE_HORAS_EXTRAS where activo_asdhe=false and ide_aspvh="+tab_permisos.getValorSeleccionado()+" and ide_asdhe="+tab_horas_extra.getValorSeleccionado());
		String str_fecha_solicitada="";
		String str_nro_horas_solicitadas="";
		if (tab_horas_aprobadas.getTotalFilas()>0) {			
			str_fecha_solicitada=tab_horas_aprobadas.getValor("fecha_asdhe");
			eti_fecha_solicitada.setValue(str_fecha_solicitada);
			str_nro_horas_solicitadas=tab_horas_aprobadas.getValor("nro_horas_asdhe");
			eti_hora_solicitada.setValue(str_nro_horas_solicitadas);
			tex_modifica_horas_extra.setValue(tab_horas_aprobadas.getValor("nro_horas_aprobadas"));
			utilitario.addUpdate("tex_modifica_horas_extra,eti_fecha_solicitada,eti_hora_solicitada");
		}else{
			eti_fecha_solicitada.setValue("");
			eti_hora_solicitada.setValue("0.00");
		}		
		dia_modifica_horas_extra.dibujar();
	}
	public void quitaSeleccionHoraExtra(UnselectEvent evt){		
		utilitario.addUpdate("tab_horas_extra");
		//sumarHorasExtras();
	}

	public void seleccionaHoraExtra2(SelectEvent evt){
		tab_horas_extra_carrito.seleccionarFila(evt);
		con_guardar.setMessage("Esta Seguro de Cancelar la Hora Extra Seleccionada");
		con_guardar.setTitle("CONFIRMACION CANCELACION HORA EXTRA ");
		con_guardar.getBot_aceptar().setMetodo("aceptarCancelarHoraExtra");
		con_guardar.dibujar();
		utilitario.addUpdate("con_guardar");
	}
	
	
	public void aceptarCancelarHoraExtra(){
		utilitario.getConexion().agregarSqlPantalla("update ASI_DETALLE_HORAS_EXTRAS set nro_horas_aprobadas_asdhe=nro_horas_asdhe,aprobado_asdhe=false where ide_asdhe="+tab_horas_extra_carrito.getValorSeleccionado());
		tab_horas_extra_carrito.guardar();
		guardarPantalla();
		con_guardar.cerrar();
		tab_suma_hora_aprobada.setSql("select ide_aspvh,(case when sum (nro_horas_aprobadas_asdhe) is null then 0 else sum (nro_horas_aprobadas_asdhe) end)  as hora_aprobado from asi_detalle_horas_extras where ide_aspvh="+tab_permisos.getValorSeleccionado()+" and aprobado_asdhe=true group by ide_aspvh");
		tab_suma_hora_aprobada.ejecutarSql();
		String valor_sumado=tab_suma_hora_aprobada.getValor("hora_aprobado");
		if(tab_suma_hora_aprobada.getTotalFilas()>0){
			eti_tot_hor_ext.setValue("Total Horas Extras Aprobadas: "+valor_sumado);			
		}else{
			eti_tot_hor_ext.setValue("Total Horas Extras Aprobadas: 0.00");
		}

		utilitario.addUpdate("eti_tot_hor_ext");		
		tab_horas_extra_carrito.ejecutarSql();
		tab_horas_extra.ejecutarSql();		
		tab_det_hor_ext.ejecutarSql();		
	}

	public void aprobarSolicitud(){	
		if(aut_empleado.getValor()!=null && !aut_empleado.getValor().isEmpty()){
			if(tab_permisos.getTotalFilas()>0){
				if(tab_permisos.getValor("anulado_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud se encuentra Anulada");
					return;
				}
				if(tab_permisos.getValor("aprobado_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud se encuentra Aprobada");
					return;
				}
				con_guardar.setMessage("Esta Seguro de Aprobar La Solicitud de Horas Extras");
				con_guardar.setTitle("CONFIRMACION APROBACION DE SOLICITUD HORAS EXTRAS");
				con_guardar.getBot_aceptar().setMetodo("aceptarAprobarSolicitud");
				con_guardar.dibujar();
				utilitario.addUpdate("con_guardar");
			}else{
				utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "El Empleado no Tiene Solicitudes");
			}		
		}else{
			utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Debe seleccionar un Empleado");
		}
	}


	public void aceptarAprobarSolicitud(){		
		
		
		utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set aprobado_aspvh=true where TIPO_ASPVH=3 and ide_aspvh="+tab_permisos.getValorSeleccionado());
		guardarPantalla();
		con_guardar.cerrar();	
		String str_ide_anterior=tab_permisos.getValorSeleccionado();
		
		
		tab_permisos.setCondicion("TIPO_ASPVH=3 AND IDE_GTEMP="+aut_empleado.getValor());
		tab_permisos.ejecutarSql();
		if(str_ide_anterior!=null && !str_ide_anterior.isEmpty()){
			tab_permisos.setFilaActual(str_ide_anterior);
		}
		
		tab_det_hor_ext.ejecutarValorForanea(tab_permisos.getValorSeleccionado());	

		
	}

	public void aprobarHoras(){
		if(aut_empleado.getValor()!=null && !aut_empleado.getValor().isEmpty()){
			if(tab_permisos.getTotalFilas()>0){				
				if(tab_permisos.getValor("anulado_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud se encuentra Anulada");
					return;
				}
				if (tab_permisos.getValor("aprobado_aspvh").equalsIgnoreCase("true")) {
					TablaGenerica tab_horas_extras=utilitario.consultar("SELECT ide_asdhe,fecha_asdhe,hora_inicial_asdhe,hora_final_asdhe,nro_horas_asdhe,actividades_asdhe,registro_novedad_asdhe,activo_asdhe FROM ASI_DETALLE_HORAS_EXTRAS where activo_asdhe=false and ide_aspvh="+tab_permisos.getValorSeleccionado());
					if(tab_horas_extras.getTotalFilas()>0){
						tab_horas_extra.setSql("SELECT ide_asdhe,fecha_asdhe,hora_inicial_asdhe,hora_final_asdhe,nro_horas_asdhe,(case when nro_horas_aprobadas_asdhe is null then nro_horas_asdhe else nro_horas_aprobadas_asdhe end)as nro_horas_aprobadas,actividades_asdhe,registro_novedad_asdhe,activo_asdhe FROM ASI_DETALLE_HORAS_EXTRAS where (case when aprobado_asdhe is null then false else aprobado_asdhe end) !=true and ide_aspvh="+tab_permisos.getValorSeleccionado());
						tab_horas_extra.ejecutarSql();
						tab_suma_hora_aprobada.setSql("select ide_aspvh,(case when sum (nro_horas_aprobadas_asdhe) is null then 0 else sum (nro_horas_aprobadas_asdhe) end)  as hora_aprobado from asi_detalle_horas_extras where ide_aspvh="+tab_permisos.getValorSeleccionado()+" and aprobado_asdhe=true group by ide_aspvh");
						tab_suma_hora_aprobada.ejecutarSql();
						String valor_sumado=tab_suma_hora_aprobada.getValor("hora_aprobado");
						if(tab_suma_hora_aprobada.getTotalFilas()>0){
							eti_tot_hor_ext.setValue("Total Horas Extras Aprobadas: "+valor_sumado);			
						}else{
							eti_tot_hor_ext.setValue("Total Horas Extras Aprobadas: 0.00");
						}
						tab_horas_extra_carrito.setSql("SELECT ide_asdhe as ide_asdhe_2,fecha_asdhe,hora_inicial_asdhe,hora_final_asdhe,nro_horas_asdhe,(case when nro_horas_aprobadas_asdhe is null then nro_horas_asdhe else nro_horas_aprobadas_asdhe end)as nro_horas_aprobadas,actividades_asdhe,registro_novedad_asdhe,activo_asdhe FROM ASI_DETALLE_HORAS_EXTRAS where aprobado_asdhe=true and ide_aspvh="+tab_permisos.getValorSeleccionado());
						tab_horas_extra_carrito.ejecutarSql();						
						dia_horas_extras.dibujar();						
					}else{
						utilitario.agregarMensajeInfo("No se puede Aprobar las Horas Extra ", "El Empleado no Tiene Horas Extras Disponibles para Aprobar");
					}					
				} else{
					utilitario.agregarMensajeInfo("No se puede Aprobar las Horas Extra ", "El Empleado no Tiene Aprobada la Solicitud");					
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede Aprobar las Horas Extra ", "El Empleado no Tiene Solicitudes");
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede Aprobar las Horas Extra ", "Debe seleccionar un Empleado");
		}
	}

	public void aceptarHorasExtras(){		
		if(tab_horas_extra_carrito.getTotalFilas()>0){
			con_guardar.setMessage("Esta Seguro de Aprobar las Horas Extras");
			con_guardar.setTitle("CONFIRMACION APROBACION DE HORAS EXTRAS");
			con_guardar.getBot_aceptar().setMetodo("aceptarAprobarHoras");			
			con_guardar.dibujar();
			dia_horas_extras.cerrar();
			utilitario.addUpdate("con_guardar");
		}else{			
			dia_horas_extras.cerrar();
		}
		
		
	}
	public void cancelarHorasExtras(){
		utilitario.getConexion().agregarSqlPantalla("update ASI_DETALLE_HORAS_EXTRAS set aprobado_asdhe=false where ide_aspvh in ("+tab_permisos.getValorSeleccionado()+") and aprobado_asdhe=true");
		tab_horas_extra_carrito.guardar();
		guardarPantalla();			
		tab_det_hor_ext.ejecutarValorForanea(tab_permisos.getValorSeleccionado());
		dia_horas_extras.cerrar();
	}


	public void aceptarAprobarHoras(){		
		con_guardar.cerrar();
		

		utilitario.getConexion().agregarSqlPantalla("update ASI_DETALLE_HORAS_EXTRAS set activo_asdhe=true,nomina_asdhe=false where ide_aspvh="+tab_permisos.getValorSeleccionado()+" and APROBADO_ASDHE=true");
		guardarPantalla();
		con_guardar.cerrar();	
		tab_det_hor_ext.ejecutarValorForanea(tab_permisos.getValorSeleccionado());
	}

	public void anularSolicitud(){		
		if(aut_empleado.getValue()!=null && !aut_empleado.getValue().toString().isEmpty()){
			if(tab_permisos.getTotalFilas()>0){
				if(tab_permisos.getValor("anulado_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud se encuentra Anulada");
					return;
				}				
				TablaGenerica tab_anular_horas_extra=utilitario.consultar("select * from asi_detalle_horas_extras where nomina_asdhe=true and ide_aspvh="+tab_permisos.getValorSeleccionado());
				if(tab_anular_horas_extra.getTotalFilas()>0){
					utilitario.agregarMensajeInfo("No se puede Anular la Solicitud", "Ya se Encuentra registrada en nomina");
				}else{
					dia_anulado.dibujar();	
				}		

			}else{
				utilitario.agregarMensajeInfo("No se puede Aprobar las Horas Extra ", "El Empleado no Tiene Solicitudes");
			}			
		}else{
			utilitario.agregarMensajeInfo("No se puede Anular las Horas Extra ", "Debe seleccionar un Empleado");
		}
	}

	public void aceptarAnulacionHorasExtras(){
		if (are_tex_razon_anula.getValue()!=null && !are_tex_razon_anula.getValue().toString().isEmpty()) {
			if(tex_documento_anula.getValue()!=null && !tex_documento_anula.getValue().toString().isEmpty()){
				if (cal_fecha_anula.getValue()!=null && !cal_fecha_anula.getValue().toString().isEmpty()) {
					tab_permisos.setValor("razon_anula_aspvh",are_tex_razon_anula.getValue().toString());
					tab_permisos.setValor("documento_anula_aspvh",tex_documento_anula.getValue().toString());
					tab_permisos.setValor("fecha_anula_aspvh", cal_fecha_anula.getFecha());
					tab_permisos.modificar(tab_permisos.getFilaActual());
					tab_permisos.guardar();
					utilitario.getConexion().agregarSqlPantalla("update ASI_PERMISOS_VACACION_HEXT set anulado_aspvh=true where ide_aspvh="+tab_permisos.getValorSeleccionado());					
					guardarPantalla();
					dia_anulado.cerrar();
					tab_permisos.ejecutarSql();
				} else {
					utilitario.agregarMensajeInfo("No se puede anular la solicitud", "Debe seleccionar una Fecha para para anular las horas extras");
				}	
			}else{
				utilitario.agregarMensajeInfo("No se puede anular la solicitud", "Debe Ingresar El Documento de Anulación para anular las horas extras");
			}					
		} else {
			utilitario.agregarMensajeInfo("No se puede anular la solicitud", "Debe ingresar una Razon para anular las horas extras");
		}	
	}



	public void aceptarModificarHorasExtras(){
		if(tex_modifica_horas_extra.getValue().toString()!=null && !tex_modifica_horas_extra.getValue().toString().isEmpty()){
			double dou_nro_hora_solicitada=Double.parseDouble(eti_hora_solicitada.getValue().toString());
			double dou_nro_hora_aprobada=Double.parseDouble(tex_modifica_horas_extra.getValue().toString());
			if(dou_nro_hora_aprobada>dou_nro_hora_solicitada){
				utilitario.agregarMensajeInfo("NO SE PUEDE APROBAR LA HORA", "EL NUMERO DE HORA POR APROBAR SUPERA LA HORA SOLICITADA");
				return;
			}
			utilitario.getConexion().agregarSqlPantalla("update ASI_DETALLE_HORAS_EXTRAS set nro_horas_aprobadas_asdhe="+tex_modifica_horas_extra.getValue()+",aprobado_asdhe=true where ide_asdhe="+tab_horas_extra.getValorSeleccionado());
			//utilitario.getConexion().agregarSqlPantalla("update ASI_DETALLE_HORAS_EXTRAS set  where  ide_asdhe="+tab_horas_extra.getValorSeleccionado());
			tab_det_hor_ext.guardar();
			guardarPantalla();
			dia_modifica_horas_extra.cerrar();
			tab_suma_hora_aprobada.setSql("select ide_aspvh,(case when sum (nro_horas_aprobadas_asdhe) is null then 0 else sum (nro_horas_aprobadas_asdhe) end)  as hora_aprobado from asi_detalle_horas_extras where ide_aspvh="+tab_permisos.getValorSeleccionado()+" and aprobado_asdhe=true group by ide_aspvh");
			tab_suma_hora_aprobada.ejecutarSql();
			eti_tot_hor_ext.setValue("Total Horas Extras Aprobadas: "+tab_suma_hora_aprobada.getValor("hora_aprobado"));
			utilitario.addUpdate("eti_tot_hor_ext");
			tab_horas_extra.ejecutarSql();
			tab_horas_extra_carrito.ejecutarSql();
			tab_det_hor_ext.ejecutarSql();
			tab_suma_hora_aprobada.ejecutarSql();

		}else{
			utilitario.agregarMensajeInfo("No se puede modificar las horas extras", "Debe ingresar el valor para modificar");
		}
	}
	
	public void aprobacionTalentoHumano(){
		if(aut_empleado.getValor()!=null && !aut_empleado.getValor().isEmpty()){
			if(tab_permisos.getTotalFilas()>0){
				if(tab_permisos.getValor("anulado_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud se encuentra Anulada");
					return;
				}
				if(tab_permisos.getValor("aprobado_aspvh").equalsIgnoreCase("false")){
					utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Solicitud no se encuentra Aprobada");
					return;
				}
				if(tab_permisos.getValor("aprobado_tthh_aspvh").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede continuar", "Ya se encuentra aprobado por Talento Humano");
					return;
				}

				tab_aprobacion_talento_humano.setSql("SELECT ide_asdhe as ide_asdhe_3,ide_asmot,fecha_asdhe,hora_inicial_asdhe,hora_final_asdhe,nro_horas_asdhe, " +
						"(case when nro_horas_aprobadas_asdhe is null then nro_horas_asdhe else nro_horas_aprobadas_asdhe end)as nro_horas_aprobadas, " +
						"actividades_asdhe,registro_novedad_asdhe,activo_asdhe,aprobado_asdhe " +
						"FROM ASI_DETALLE_HORAS_EXTRAS where ide_aspvh="+tab_permisos.getValorSeleccionado()+" and activo_asdhe= true and aprobado_asdhe=true");
				tab_aprobacion_talento_humano.ejecutarSql();
				tab_suma_hora_aprobada.setSql("select ide_aspvh,(case when sum (nro_horas_aprobadas_asdhe) is null then 0 else sum (nro_horas_aprobadas_asdhe) end)  as hora_aprobado from asi_detalle_horas_extras where ide_aspvh="+tab_permisos.getValorSeleccionado()+" and aprobado_asdhe=true group by ide_aspvh");
				tab_suma_hora_aprobada.ejecutarSql();
				String valor_sumado=tab_suma_hora_aprobada.getValor("hora_aprobado");
				if(tab_suma_hora_aprobada.getTotalFilas()>0){
					eti_tot_talento_humano.setValue("Total Horas Extras Aprobadas Talento Humano: "+valor_sumado);			
				}else{
					eti_tot_talento_humano.setValue("Total Horas Extras Aprobadas Talento Humano: 0.00");
				}
				utilitario.addUpdate("eti_tot_talento_humano");
				dia_aprobacion_talento_humano.dibujar();
			}else{
				utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "El Empleado no Tiene Solicitudes");
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede Aprobar la Solicitud ", "Debe seleccionar un Empleado");
		}
	}
	
	public void aceptarAprobacionTalentoHumano(){			
		int int_acu1=0;
		if(tab_aprobacion_talento_humano.getTotalFilas()>0){				
			for (int i = 0; i < tab_aprobacion_talento_humano.getTotalFilas(); i++) {				
				if(tab_aprobacion_talento_humano.getValor(i, "ide_asmot")==null){
					int_acu1=int_acu1+1;	
				}			
			}	
			if(int_acu1>0){				
				utilitario.agregarMensajeInfo("No se puede Continuar", "Existen "+int_acu1+" Registros sin Motivo");
				return;
			}
			con_guardar.setMessage("Esta Seguro de Aprobar las Horas Extras de Talento Humano");
			con_guardar.setTitle("CONFIRMACION APROBACION DE HORAS EXTRAS POR TALENTO HUMANO");
			con_guardar.getBot_aceptar().setMetodo("aceptarAprobarTalentoHumano");			
			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");//aprobado_tthh
			//tab_aprobacion_talento_humano.guardar();
			//guardarPantalla()
		}		
	}
	
	public void aceptarAprobarTalentoHumano(){
		for (int i = 0; i < tab_aprobacion_talento_humano.getTotalFilas(); i++) {			
			utilitario.getConexion().agregarSqlPantalla("update ASI_DETALLE_HORAS_EXTRAS set ide_asmot="+tab_aprobacion_talento_humano.getValor(i, "ide_asmot")+" where ide_asdhe="+tab_aprobacion_talento_humano.getValor(i, tab_aprobacion_talento_humano.getCampoPrimaria()));
		}
		utilitario.getConexion().agregarSqlPantalla("update asi_permisos_vacacion_hext set aprobado_tthh_aspvh=1 where ide_aspvh="+tab_permisos.getValorSeleccionado());
		guardarPantalla();
		System.out.println("si ingresa al aceptarAprobarTalentoHumano ");		
		dia_aprobacion_talento_humano.cerrar();
		con_guardar.cerrar();
		tab_det_hor_ext.ejecutarSql();
		tab_permisos.ejecutarSql();
	}

	String ide_geedp_activo="";
	public void filtrarAnticiposEmpleado(SelectEvent evt){
		aut_empleado.onSelect(evt);
		ide_geedp_activo=ser_gestion.getIdeContratoActivo(aut_empleado.getValor());
		tab_permisos.setCondicion("TIPO_ASPVH=3 AND IDE_GTEMP="+aut_empleado.getValor());
		tab_permisos.ejecutarSql();
		tab_det_hor_ext.ejecutarValorForanea(tab_permisos.getValorSeleccionado());	
	}

	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_permisos.limpiar();
		ide_geedp_activo="";
		aut_empleado.limpiar();
		aut_empleado.limpiar();
		utilitario.addUpdate("aut_empleado");// limpia y refresca el autocompletar
	}



	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_permisos.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (ide_geedp_activo!=null && !ide_geedp_activo.isEmpty()){		
					tab_permisos.insertar();
					tab_permisos.setValor("IDE_GEEDP",ide_geedp_activo);
					tab_permisos.setValor("IDE_GTEMP", aut_empleado.getValor());
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "El contrato del empleado no esta activo");
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado que solicita la Horas Extras");
			}
		}else if (tab_det_hor_ext.isFocus()){
			if(tab_permisos.getValor("aprobado_aspvh").equalsIgnoreCase("false")){
				tab_det_hor_ext.insertar();	
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "La solicitud ya se encuentra Aprobada");
			}			
		}
	}
	//	public void calculoHoras(String str_hora_inicial , String str_hora_final){
	//		try {
	//
	//			System.out.println("hora inicial"+str_hora_inicial);
	//			System.out.println("hora inicial"+str_hora_final);
	//			Date hora_inicial= utilitario.getHora(utilitario.getFormatoHora(str_hora_inicial));
	//			Date hora_final= utilitario.getHora(utilitario.getFormatoHora(str_hora_final));
	//			int total_segundos_hora_inicial=(hora_inicial.getHours()*3600)+(hora_inicial.getMinutes()*60) + hora_inicial.getSeconds();
	//			int total_segundos_hora_final=(hora_final.getHours()*3600)+(hora_final.getMinutes()*60)+hora_final.getSeconds();
	//
	//			System.out.println("tot_seg hi "+total_segundos_hora_inicial);
	//			System.out.println("tot_seg hf "+total_segundos_hora_final);
	//			int total_diferencia_segundo=total_segundos_hora_final-total_segundos_hora_inicial;
	//			System.out.println("tot_seg "+total_diferencia_segundo);
	//
	//			int total_horas=total_diferencia_segundo/3600;
	//			int nuevo_valor=total_diferencia_segundo-(total_horas*3600);
	//			int total_minutos=nuevo_valor/60;
	//			int total_segundos=nuevo_valor-(total_minutos*60);
	//			System.out.println("total_horas"+total_horas);
	//			System.out.println("total_minutos"+total_minutos);
	//			System.out.println("total_segundos"+total_segundos);
	//			double total_diferencia_segundos=((total_horas*3600)+(total_minutos*60)+total_segundos);
	//			double total_diferencia_horas=total_diferencia_segundos/3600;
	//			System.out.println("total diferencia horas "+total_diferencia_horas);
	//			tab_det_hor_ext.setValor(tab_det_hor_ext.getFilaActual(),"NRO_HORAS_ASDHE",total_diferencia_horas+"");
	//			utilitario.addUpdateTabla(tab_det_hor_ext,"NRO_HORAS_ASDHE", total_diferencia_horas+"");
	//		} catch (Exception e) {
	//			// TODO: handle exception
	//			tab_det_hor_ext.setValor(tab_det_hor_ext.getFilaActual(),"NRO_HORAS_ASDHE","");
	//			utilitario.addUpdateTabla(tab_det_hor_ext,"NRO_HORAS_ASDHE", "");
	//
	//		}
	//
	//	}
	//	public  void calaculahoras(AjaxBehaviorEvent evt){
	//		
	//			tab_det_hor_ext.modificar(evt);		
	//			if(tab_det_hor_ext.getValor("HORA_FINAL_ASDHE")!=null && tab_det_hor_ext.getValor("HORA_INICIAL_ASDHE")!=null){
	//				if (!isHoraMayor(utilitario.getFormatoHora(tab_det_hor_ext.getValor("HORA_INICIAL_ASDHE")),utilitario.getFormatoHora(tab_det_hor_ext.getValor("HORA_FINAL_ASDHE")))) {
	//					calculoHoras(tab_det_hor_ext.getValor("HORA_INICIAL_ASDHE"), tab_det_hor_ext.getValor("HORA_FINAL_ASDHE"));
	//				//	tab_det_hor_ext.setColumnaSuma("NRO_HORAS_ASDHE");
	//				}else {
	//					utilitario.agregarMensajeInfo("HORA INICIAL NO PUEDE SER  MENOR A HORA FINAL", "");
	//				}	
	//			}		
	//	}

	public void calculoHoras(String str_hora_inicial , String str_hora_final){
		try {
			System.out.println("hora inicial"+str_hora_inicial);
			System.out.println("hora inicial"+str_hora_final);
			Date hora_inicial= utilitario.getHora(utilitario.getFormatoHora(str_hora_inicial));
			Date hora_final= utilitario.getHora(utilitario.getFormatoHora(str_hora_final));
			int total_segundos_hora_inicial=(hora_inicial.getHours()*3600)+(hora_inicial.getMinutes()*60) + hora_inicial.getSeconds();
			int total_segundos_hora_final=(hora_final.getHours()*3600)+(hora_final.getMinutes()*60)+hora_final.getSeconds();

			System.out.println("tot_seg hi "+total_segundos_hora_inicial);
			System.out.println("tot_seg hf "+total_segundos_hora_final);
			int total_diferencia_segundo=total_segundos_hora_final-total_segundos_hora_inicial;
			System.out.println("tot_seg "+total_diferencia_segundo);

			int total_horas=total_diferencia_segundo/3600;
			int nuevo_valor=total_diferencia_segundo-(total_horas*3600);
			int total_minutos=nuevo_valor/60;
			int total_segundos=nuevo_valor-(total_minutos*60);
			System.out.println("total_horas"+total_horas);
			System.out.println("total_minutos"+total_minutos);
			System.out.println("total_segundos"+total_segundos);
			double total_diferencia_segundos=((total_horas*3600)+(total_minutos*60)+total_segundos);
			double total_diferencia_horas=total_diferencia_segundos/3600;
			System.out.println("total diferencia horas "+total_diferencia_horas);
			tab_det_hor_ext.setValor(tab_det_hor_ext.getFilaActual(),"NRO_HORAS_ASDHE",total_diferencia_horas+"");
			utilitario.addUpdateTabla(tab_det_hor_ext,"NRO_HORAS_ASDHE", total_diferencia_horas+"");
		} catch (Exception e) {
			// TODO: handle exception
			tab_det_hor_ext.setValor(tab_det_hor_ext.getFilaActual(),"NRO_HORAS_ASDHE","");
			utilitario.addUpdateTabla(tab_det_hor_ext,"NRO_HORAS_ASDHE", "");
		}
	}

	public  void calaculahoras(AjaxBehaviorEvent evt){
		tab_det_hor_ext.modificar(evt);		
		System.out.println("HORA INICIAL -calaculahoras: "+tab_det_hor_ext.getValor("HORA_INICIAL_ASDHE"));
		System.out.println("HORA FINAL -calaculahoras: "+tab_det_hor_ext.getValor("HORA_FINAL_ASDHE"));
		if(tab_det_hor_ext.getValor("HORA_INICIAL_ASDHE")!=null && !tab_det_hor_ext.getValor("HORA_INICIAL_ASDHE").isEmpty()
				&& tab_det_hor_ext.getValor("HORA_FINAL_ASDHE")!=null && !tab_det_hor_ext.getValor("HORA_FINAL_ASDHE").isEmpty()){
			if (!isHoraMayor(tab_det_hor_ext.getValor("HORA_INICIAL_ASDHE"),tab_det_hor_ext.getValor("HORA_FINAL_ASDHE"))) {
				calculoHoras(tab_det_hor_ext.getValor("HORA_INICIAL_ASDHE"), tab_det_hor_ext.getValor("HORA_FINAL_ASDHE"));
				System.out.println(" HORA MAYOR calaculahoras: "+isHoraMayor(tab_det_hor_ext.getValor("HORA_INICIAL_ASDHE"),tab_det_hor_ext.getValor("HORA_FINAL_ASDHE")));
			}else {				
				System.out.println("else IS HORA MAYOR calaculahoras: "+isHoraMayor(tab_det_hor_ext.getValor("HORA_INICIAL_ASDHE"),tab_det_hor_ext.getValor("HORA_FINAL_ASDHE")));
				//utilitario.agregarMensajeInfo("HORA INICIAL NO PUEDE SER  MENOR A HORA FINAL", "");
				tab_det_hor_ext.setValor("HORA_FINAL_ASDHE","");
				utilitario.addUpdateTabla(tab_det_hor_ext, "HORA_FINAL_ASDHE", "");
				utilitario.ejecutarJavaScript("alert('HORA INICIAL NO PUEDE SER  MENOR A HORA FINAL');");
			}	
		}
	}

	public  void calaculahoras(SelectEvent evt){
		tab_det_hor_ext.modificar(evt);
		if (!isHoraMayor(tab_det_hor_ext.getValor("HORA_INICIAL_ASDHE"),tab_det_hor_ext.getValor("HORA_FINAL_ASDHE"))) {
			calculoHoras(tab_det_hor_ext.getValor("HORA_INICIAL_ASDHE"), tab_det_hor_ext.getValor("HORA_FINAL_ASDHE"));
			tab_det_hor_ext.setColumnaSuma("NRO_HORAS_ASDHE");
		}
		else {
			utilitario.agregarMensajeInfo("HORA INICIAL NO PUEDE SER  MENOR A HORA FINAL", "");

		}
	}


	//	public boolean isHoraMayor(String hora_ini,String hora_fin){
	//		try {
	//			DateFormat dateFormat = new SimpleDateFormat ("hh:mm:ss");
	//
	//			String hora1 = hora_ini;
	//			String hora2 = hora_fin;
	//
	//
	//			Date date1, date2, dateNueva;
	//			date1 = dateFormat.parse(hora1);
	//			date2 = dateFormat.parse(hora2);
	//
	//			if (date1.compareTo(date2) > 0){ 
	//				return true;
	//			}
	//
	//		} catch (ParseException parseException){
	//			parseException.printStackTrace();
	//		}
	//		return false;
	//	}
	public boolean isHoraMayor(String hora_ini,String hora_fin){
		try {
			DateFormat dateFormat = new  SimpleDateFormat ("hh:mm:ss");

			String hora1 = utilitario.getFormatoHora(hora_ini);
			String hora2 = utilitario.getFormatoHora(hora_fin);

			int int_hora1=pckUtilidades.CConversion.CInt(hora1.replaceAll(":", ""));
			int int_hora2=pckUtilidades.CConversion.CInt(hora2.replaceAll(":", ""));


			if(int_hora1>int_hora2){
				return true;
			}

		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}


	public boolean validarSolicitudVacaciones(){
		if (tab_permisos.getValor("GEN_IDE_GEEDP")==null || tab_permisos.getValor("GEN_IDE_GEEDP").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar Jefe Inmediato");
			return false;
		}
		if (tab_permisos.getValor("GEN_IDE_GEEDP2")==null || tab_permisos.getValor("GEN_IDE_GEEDP2").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Jefe de Talento Humano");
			return false;
		}
		if (tab_permisos.getValor("GEN_IDE_GEEDP3")==null || tab_permisos.getValor("GEN_IDE_GEEDP3").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Gerente de Area");
			return false;
		}
		if (tab_det_hor_ext.getValor("HORA_INICIAL_ASDHE")==null || tab_det_hor_ext.getValor("HORA_INICIAL_ASDHE").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Hora Inicial");
			return false;
		}		
		if (tab_det_hor_ext.getValor("HORA_FINAL_ASDHE")==null || tab_det_hor_ext.getValor("HORA_FINAL_ASDHE").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Hora Final");
			return false;
		}
		if (isHoraMayor(tab_det_hor_ext.getValor("HORA_INICIAL_ASDHE"),tab_det_hor_ext.getValor("HORA_FINAL_ASDHE"))) {
			utilitario.agregarMensajeInfo("HORA INICIAL NO PUEDE SER  MENOR A HORA FINAL", "");
			return false;
		}
		if (tab_det_hor_ext.getValor("NRO_HORAS_ASDHE")==null || tab_det_hor_ext.getValor("NRO_HORAS_ASDHE").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Número de Horas");
			return false;
		}
		if (tab_det_hor_ext.getValor("ACTIVIDADES_ASDHE")==null || tab_det_hor_ext.getValor("ACTIVIDADES_ASDHE").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar las Actividades");
			return false;
		}
		return true;
	}


	@Override
	public void guardar() {
		// TODO Auto-generated method stub

		if (aut_empleado.getValor()!=null){
			if (validarSolicitudVacaciones()){
				if (tab_permisos.guardar()){
					if (tab_det_hor_ext.guardar()){
						guardarPantalla();
					}
				}
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede guardar el Anticipo", "Debe seleccionar un Empleado");
		}

	}


	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if(tab_permisos.isFocus()){
			tab_permisos.eliminar();
		}
		if(tab_det_hor_ext.isFocus()){
			tab_det_hor_ext.eliminar();
		}
	}

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}

	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

	public Tabla getTab_permisos() {
		return tab_permisos;
	}

	public void setTab_permisos(Tabla tab_permisos) {
		this.tab_permisos = tab_permisos;
	}

	public Tabla getTab_det_hor_ext() {
		return tab_det_hor_ext;
	}

	public void setTab_det_hor_ext(Tabla tab_det_hor_ext) {
		this.tab_det_hor_ext = tab_det_hor_ext;
	}

	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}

	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}



	public Tabla getTab_horas_extra() {
		return tab_horas_extra;
	}

	public void setTab_horas_extra(Tabla tab_horas_extra) {
		this.tab_horas_extra = tab_horas_extra;
	}

	public Dialogo getDia_horas_extras() {
		return dia_horas_extras;
	}

	public void setDia_horas_extras(Dialogo dia_horas_extras) {
		this.dia_horas_extras = dia_horas_extras;
	}

	public Dialogo getDia_anulado() {
		return dia_anulado;
	}

	public void setDia_anulado(Dialogo dia_anulado) {
		this.dia_anulado = dia_anulado;
	}


	public Dialogo getDia_modifica_horas_extra() {
		return dia_modifica_horas_extra;
	}

	public void setDia_modifica_horas_extra(Dialogo dia_modifica_horas_extra) {
		this.dia_modifica_horas_extra = dia_modifica_horas_extra;
	}

	public Tabla getTab_horas_extra_carrito() {
		return tab_horas_extra_carrito;
	}

	public void setTab_horas_extra_carrito(Tabla tab_horas_extra_carrito) {
		this.tab_horas_extra_carrito = tab_horas_extra_carrito;
	}
	
	public Tabla getTab_suma_hora_aprobada() {
		return tab_suma_hora_aprobada;
	}

	public void setTab_suma_hora_aprobada(Tabla tab_suma_hora_aprobada) {
		this.tab_suma_hora_aprobada = tab_suma_hora_aprobada;
	}

	public Tabla getTab_aprobacion_talento_humano() {
		return tab_aprobacion_talento_humano;
	}

	public void setTab_aprobacion_talento_humano(Tabla tab_aprobacion_talento_humano) {
		this.tab_aprobacion_talento_humano = tab_aprobacion_talento_humano;
	}

	public Dialogo getDia_aprobacion_talento_humano() {
		return dia_aprobacion_talento_humano;
	}

	public void setDia_aprobacion_talento_humano(
			Dialogo dia_aprobacion_talento_humano) {
		this.dia_aprobacion_talento_humano = dia_aprobacion_talento_humano;
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Horas Extras")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();		
				rep_reporte.cerrar();	
				//System.out.println("p_parametro "+ tab_permisos.getSql());
				//System.out.println("IDE_GTEMP"+ser_nomina.getEmpleadoDepartamento(aut_empleado.getValor()).getValor("ide_gtemp"));
				p_parametros.put("IDE_GEEDP",pckUtilidades.CConversion.CInt(ide_geedp_activo));
				p_parametros.put("titulo","BIESS GERENCIA ADMINISTRATIVA - FINANCIERA DEPARTAMENTO DE TALENTO HUMANO HORAS EXTRAS");
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());				
				sef_reporte.dibujar();
			}
			//}else{
			//utilitario.agregarMensajeInfo("No se puede continuar", "No contiene registro de permisos");
			//}	
		}
	}
}
