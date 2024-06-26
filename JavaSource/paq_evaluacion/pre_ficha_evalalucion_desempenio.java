package paq_evaluacion;

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

import paq_anticipos.ejb.ServicioAnticipo;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import framework.correo.EnviarCorreo;
 

public class pre_ficha_evalalucion_desempenio extends Pantalla {

	private Tabla tab_desempenio=new Tabla();
	private Tabla tab_evaluadores=new Tabla();
	private Tabla actividad_puesto=new Tabla();
	private Tabla tab_competencia_gestion=new Tabla();
	private Tabla tab_competencia_institucional=new Tabla();
	private Tabla tab_competencia_tecnica=new Tabla();


	private AutoCompletar aut_empleado = new AutoCompletar();
	private Confirmar con_guardar=new Confirmar();
	@EJB
	private ServicioEmpleado ser_empleado=(ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	@EJB
	private ServicioNomina ser_nomina=(ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();	
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	private SeleccionCalendario sel_cal_fechas=new SeleccionCalendario();
	private SeleccionTabla sel_tab_grupo_ocupacional=new SeleccionTabla();
	private SeleccionTabla sel_tab_cargo_funcional=new SeleccionTabla();

	private Dialogo dia_actividad_puesto=new Dialogo();
	private Tabla tab_detalle_competencia=new Tabla();

	private Grid gri_activ_puesto=new Grid();

	private Dialogo dia_modificar_competencias=new Dialogo();
	private Etiqueta eti_indicador=new Etiqueta();
	private Etiqueta eti_peso=new Etiqueta();
	private Texto tex_indicador=new Texto();
	private Texto tex_peso=new Texto();
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	
	public pre_ficha_evalalucion_desempenio() {


		sel_tab_grupo_ocupacional.setId("sel_tab_grupo_ocupacional");
		sel_tab_grupo_ocupacional.setSeleccionTabla("SELECT IDE_GEGRO,DETALLE_GEGRO FROM  GEN_GRUPO_OCUPACIONAL WHERE IDE_GEGRO=-1", "IDE_GEGRO");
		sel_tab_grupo_ocupacional.getTab_seleccion().getColumna("DETALLE_GEGRO").setFiltro(true);
		sel_tab_grupo_ocupacional.setTitle("SELECCION DE GRUPO OCUPACIONAL");

		sel_tab_grupo_ocupacional.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tab_grupo_ocupacional);

		sel_tab_cargo_funcional.setId("sel_tab_cargo_funcional");
		sel_tab_cargo_funcional.setSeleccionTabla("SELECT IDE_GECAF,DETALLE_GECAF FROM GEN_CARGO_FUNCIONAL WHERE IDE_GECAF=-1", "IDE_GECAF");
		sel_tab_cargo_funcional.getTab_seleccion().getColumna("DETALLE_GECAF").setFiltro(true);
		sel_tab_cargo_funcional.setTitle("SELECCION DE CARGO FUNCIONAL");

		sel_tab_cargo_funcional.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tab_cargo_funcional);

		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarReporte();
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);

		// autocompletar empleado
		aut_empleado.setId("aut_empleado");
		String str_sql_emp=ser_gestion.getSqlEmpleadosAutocompletar();
		aut_empleado.setAutoCompletar(str_sql_emp);
		aut_empleado.setMetodoChange("filtrarAnticiposEmpleado");
		
		Etiqueta eti_colaborador=new Etiqueta("Empleado:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);


		Boton bot_enviar=new Boton();
		bot_enviar.setMetodo("enviarCorreo");
		bot_enviar.setValue("Enviar E-mail");
		bot_enviar.setIcon("ui-icon-mail-closed");
		bar_botones.agregarBoton(bot_enviar);

		Boton bot_reactivar=new Boton();
		bot_reactivar.setMetodo("reativarEvaluacion");
		bot_reactivar.setValue("REACTIVAR EVALUACION");		
		bar_botones.agregarBoton(bot_reactivar);

		tab_desempenio.setId("tab_desempenio");
		tab_desempenio.setTabla("EVL_DESEMPENIO", "IDE_EVDES", 1);
		tab_desempenio.getColumna("ACTIVO_EVDES").setCheck();
		tab_desempenio.getColumna("ACTIVO_EVDES").setValorDefecto("true");
		tab_desempenio.getColumna("ACTIVO_EVDES").setLectura(true);
		tab_desempenio.getColumna("IDE_GEEDP").setVisible(false);
		tab_desempenio.getColumna("IDE_GTEMP").setVisible(false);
		tab_desempenio.setCampoOrden("IDE_EVDES DESC");		
		tab_desempenio.agregarRelacion(tab_evaluadores);		
		tab_desempenio.setCondicion("IDE_GTEMP=-1");		
		tab_desempenio.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_desempenio);

		Tabulador tab_tabulador=new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_evaluadores.setId("tab_evaluadores");		
		tab_evaluadores.setTabla("EVL_EVALUADORES", "IDE_EVEVA", 2);
		tab_evaluadores.getColumna("ACTIVO_EVEVA").setCheck();
		tab_evaluadores.getColumna("ACTIVO_EVEVA").setValorDefecto("true");
		tab_evaluadores.getColumna("ACTIVO_EVEVA").setLectura(true);
		tab_evaluadores.getColumna("IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
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
		tab_evaluadores.getColumna("IDE_GEEDP").setAutoCompletar();
		tab_evaluadores.setColumnaSuma("POR_PESO_EVEVA");
		tab_evaluadores.getColumna("POR_PESO_EVEVA").setRequerida(true);
		tab_evaluadores.getColumna("POR_PESO_EVEVA").setMetodoChange("validarCero");	
		tab_evaluadores.getColumna("fecha_evaluacion_eveva").setLectura(true);
		tab_evaluadores.agregarRelacion(actividad_puesto);
		tab_evaluadores.agregarRelacion(tab_competencia_gestion);
		tab_evaluadores.agregarRelacion(tab_competencia_institucional);
		tab_evaluadores.agregarRelacion(tab_competencia_tecnica);
		tab_evaluadores.setRecuperarLectura(true);
		
		tab_evaluadores.dibujar();

		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_evaluadores);

		ItemMenu itm_eva=new ItemMenu();
		itm_eva.setIcon("ui-contact");
		itm_eva.setMetodo("modificarEvaluador");
		itm_eva.setValue("Modificar");
		pat_panel2.getMenuTabla().getChildren().add(itm_eva);
	
		
		
		actividad_puesto.setId("actividad_puesto");
		actividad_puesto.setIdCompleto("tab_tabulador:actividad_puesto");
		actividad_puesto.setTabla("EVL_ACTIVIDAD_PUESTO", "IDE_EVACP", 3);
		actividad_puesto.getColumna("ACTIVO_EVACP").setCheck();
		actividad_puesto.getColumna("ACTIVO_EVACP").setValorDefecto("true");
		actividad_puesto.getColumna("ACTIVO_EVACP").setLectura(true);
		//		actividad_puesto.getColumna("IDE_CMDEC").setCombo("SELECT ide_cmdec,detalle_cmdec as detalle_cmdec from cmp_detalle_competencia where ide_cmfac " +
		//				"in (select ide_cmfac from cmp_factor_competencia where ide_evfae="+utilitario.getVariable("p_factor_actividad_puesto")+") " +
		//				"and ide_gegca in ( select ide_gegca from gen_grupo_cargo_area a,gen_empleados_departamento_par b " +
		//				"where a.ide_gegro = b.ide_gegro " +
		//				"and a.ide_gecaf=b.ide_gecaf and a.ide_gedep=b.ide_gedep and IDE_GEEDP=-1)");
		actividad_puesto.getColumna("IDE_CMDEC").setCombo("SELECT ide_cmdec,detalle_cmdec as detalle_cmdec from cmp_detalle_competencia");
		actividad_puesto.getColumna("IDE_CMDEC").setLectura(true);
		actividad_puesto.getColumna("IDE_EVFAE").setValorDefecto(utilitario.getVariable("p_factor_actividad_puesto"));
		actividad_puesto.getColumna("IDE_EVFAE").setVisible(false);
		actividad_puesto.getColumna("CUMPLIDO_EVACP").setLectura(true);
		actividad_puesto.getColumna("POR_CUMPLIDO_EVACP").setLectura(true);
		actividad_puesto.getColumna("NIVEL_CUMPLIDO_EVACP").setLectura(true);
		actividad_puesto.setColumnaSuma("meta_evacp");
		//actividad_puesto.getColumna("meta_evacp").setRequerida(true);
		actividad_puesto.getColumna("meta_evacp").setMetodoChange("validarCeroMeta");
		actividad_puesto.setRecuperarLectura(true);
		actividad_puesto.dibujar();

		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(actividad_puesto);
		
		ItemMenu itm_mc=new ItemMenu();
		itm_mc.setIcon("ui-contact");
		itm_mc.setMetodo("modificarActividadPuesto");
		itm_mc.setValue("Modificar");
		pat_panel3.getMenuTabla().getChildren().add(itm_mc);
		


		tab_competencia_gestion.setId("tab_competencia_gestion");
		tab_competencia_gestion.setIdCompleto("tab_tabulador:tab_competencia_gestion");
		tab_competencia_gestion.setTabla("EVL_OTRA_COMPETENCIA", "IDE_EVOTC", 4);
		tab_competencia_gestion.getColumna("ACTIVO_EVOTC").setCheck();
		tab_competencia_gestion.getColumna("ACTIVO_EVOTC").setValorDefecto("true");
		tab_competencia_gestion.getColumna("ACTIVO_EVOTC").setLectura(true);
		tab_competencia_gestion.getColumna("IDE_CMDEC").setCombo("SELECT ide_cmdec,detalle_cmdec as detalle_cmdec from cmp_detalle_competencia");
		tab_competencia_gestion.getColumna("IDE_CMDEC").setLectura(true);

		//		tab_competencia_gestion.getColumna("IDE_CMDEC").setCombo("SELECT ide_cmdec,substr(detalle_cmdec,1,100) as detalle_cmdec from cmp_detalle_competencia where ide_cmfac " +
		//				"in (select ide_cmfac from cmp_factor_competencia where ide_evfae="+utilitario.getVariable("p_competencias_gestion")+") " +
		//				"and ide_gegca in ( select ide_gegca from gen_grupo_cargo_area a,gen_empleados_departamento_par b " +
		//				"where a.ide_gegro = b.ide_gegro " +
		//				"and a.ide_gecaf=b.ide_gecaf and a.ide_gedep=b.ide_gedep and IDE_GEEDP=-1)");		
		tab_competencia_gestion.getColumna("IDE_EVFAE").setValorDefecto(utilitario.getVariable("p_competencias_gestion"));
		tab_competencia_gestion.getColumna("IDE_EVFAE").setVisible(false);
		tab_competencia_gestion.getColumna("IDE_EVNID").setLectura(true);
		tab_competencia_gestion.getColumna("IDE_EVNID").setCombo("EVL_NIVEL_DESARROLLO", "IDE_EVNID", "DETALLE_EVNID", "");
		tab_competencia_gestion.getColumna("IDE_EVREL").setCombo("EVL_RELEVANCIA", "IDE_EVREL", "DETALLE_EVREL", "");		
		tab_competencia_gestion.getColumna("IDE_EVREL").setLectura(true);
		tab_competencia_gestion.getColumna("NIVEL_CUMPLIDO_EVOTC").setLectura(true);		
		tab_competencia_gestion.setCondicion("IDE_EVFAE="+utilitario.getVariable("p_competencias_gestion"));
		tab_competencia_gestion.dibujar();


		PanelTabla pat_panel4=new PanelTabla();
		pat_panel4.setPanelTabla(tab_competencia_gestion);


		tab_competencia_institucional.setId("tab_competencia_institucional");
		tab_competencia_institucional.setIdCompleto("tab_tabulador:tab_competencia_institucional");
		tab_competencia_institucional.setTabla("EVL_OTRA_COMPETENCIA", "IDE_EVOTC", 5);

		tab_competencia_institucional.getColumna("ACTIVO_EVOTC").setCheck();
		tab_competencia_institucional.getColumna("ACTIVO_EVOTC").setValorDefecto("true");
		tab_competencia_institucional.getColumna("ACTIVO_EVOTC").setLectura(true);
		tab_competencia_institucional.getColumna("IDE_CMDEC").setCombo("SELECT ide_cmdec,detalle_cmdec as detalle_cmdec from cmp_detalle_competencia");
tab_competencia_institucional.getColumna("IDE_CMDEC").setLectura(true);

		//		tab_competencia_institucional.getColumna("IDE_CMDEC").setCombo("SELECT ide_cmdec,substr(detalle_cmdec,1,100) as detalle_cmdec from cmp_detalle_competencia where ide_cmfac " +
		//				"in (select ide_cmfac from cmp_factor_competencia where ide_evfae="+utilitario.getVariable("p_competencias_instituciones")+") " +
		//				"and ide_gegca in ( select ide_gegca from gen_grupo_cargo_area a,gen_empleados_departamento_par b " +
		//				"where a.ide_gegro = b.ide_gegro " +
		//				"and a.ide_gecaf=b.ide_gecaf and a.ide_gedep=b.ide_gedep and IDE_GEEDP=-1)");		
		tab_competencia_institucional.getColumna("IDE_EVFAE").setValorDefecto(utilitario.getVariable("p_competencias_instituciones"));
		tab_competencia_institucional.getColumna("IDE_EVFAE").setVisible(false);
		tab_competencia_institucional.setCondicion("IDE_EVFAE="+utilitario.getVariable("p_competencias_instituciones"));
		tab_competencia_institucional.getColumna("IDE_EVNID").setLectura(true);
		tab_competencia_institucional.getColumna("IDE_EVNID").setCombo("EVL_NIVEL_DESARROLLO", "IDE_EVNID", "DETALLE_EVNID", "");		
		tab_competencia_institucional.getColumna("IDE_EVREL").setCombo("EVL_RELEVANCIA", "IDE_EVREL", "DETALLE_EVREL", "");		
		tab_competencia_institucional.getColumna("IDE_EVREL").setLectura(true);
		tab_competencia_institucional.getColumna("NIVEL_CUMPLIDO_EVOTC").setLectura(true);		
		tab_competencia_institucional.dibujar();


		PanelTabla pat_panel5=new PanelTabla();
		pat_panel5.setPanelTabla(tab_competencia_institucional);


		tab_competencia_tecnica.setId("tab_competencia_tecnica");
		tab_competencia_tecnica.setIdCompleto("tab_tabulador:tab_competencia_tecnica");
		tab_competencia_tecnica.setTabla("EVL_OTRA_COMPETENCIA", "IDE_EVOTC", 6);
		tab_competencia_tecnica.getColumna("ACTIVO_EVOTC").setCheck();
		tab_competencia_tecnica.getColumna("ACTIVO_EVOTC").setValorDefecto("true");
		tab_competencia_tecnica.getColumna("ACTIVO_EVOTC").setLectura(true);
		tab_competencia_tecnica.getColumna("IDE_CMDEC").setCombo("SELECT ide_cmdec,detalle_cmdec as detalle_cmdec from cmp_detalle_competencia");
		tab_competencia_tecnica.getColumna("IDE_CMDEC").setLectura(true);

		//		tab_competencia_tecnica.getColumna("IDE_CMDEC").setCombo("SELECT ide_cmdec,substr(detalle_cmdec,1,100) as detalle_cmdec from cmp_detalle_competencia where ide_cmfac " +
		//				"in (select ide_cmfac from cmp_factor_competencia where ide_evfae="+utilitario.getVariable("p_competencias_tecnicas")+") " +
		//				"and ide_gegca in ( select ide_gegca from gen_grupo_cargo_area a,gen_empleados_departamento_par b " +
		//				"where a.ide_gegro = b.ide_gegro " +
		//				"and a.ide_gecaf=b.ide_gecaf and a.ide_gedep=b.ide_gedep and IDE_GEEDP=-1)");		
		tab_competencia_tecnica.getColumna("IDE_EVFAE").setValorDefecto(utilitario.getVariable("p_competencias_tecnicas"));
		tab_competencia_tecnica.getColumna("IDE_EVFAE").setVisible(false);
		tab_competencia_tecnica.setCondicion("IDE_EVFAE="+utilitario.getVariable("p_competencias_tecnicas"));
		tab_competencia_tecnica.getColumna("IDE_EVNID").setLectura(true);
		tab_competencia_tecnica.getColumna("IDE_EVNID").setCombo("EVL_NIVEL_DESARROLLO", "IDE_EVNID", "DETALLE_EVNID", "");		
		tab_competencia_tecnica.getColumna("IDE_EVREL").setCombo("EVL_RELEVANCIA", "IDE_EVREL", "DETALLE_EVREL", "");		
		tab_competencia_tecnica.getColumna("IDE_EVREL").setLectura(true);
		tab_competencia_tecnica.getColumna("NIVEL_CUMPLIDO_EVOTC").setLectura(true);		
		tab_competencia_tecnica.dibujar();


		PanelTabla pat_panel6=new PanelTabla();
		pat_panel6.setPanelTabla(tab_competencia_tecnica);

		tab_tabulador.agregarTab("ACTIVIDAD PUESTO", pat_panel3);
		tab_tabulador.agregarTab("COMPETENCIAS GESTIÓN", pat_panel4);
		tab_tabulador.agregarTab("COMPETENCIAS INSTITUCIONALES", pat_panel5);
		tab_tabulador.agregarTab("COMPETENCIAS TECNICAS", pat_panel6);


		//  DIVISION DE LA PANTALLA
		Division div_division=new Division();
		div_division.dividir3(pat_panel1,pat_panel2,tab_tabulador,"30%","30%","H");
		agregarComponente(div_division);

		// confirmacion para reactivar evaluacion
		con_guardar.setId("con_guardar");
		con_guardar.setMessage("ESTA SEGURO DE REACTIVAR: LA SIGUIENTE EVALUACION: PERIODO DE EVALUACION= DEL "+tab_desempenio.getValor("fecha_desde_evdes")+" AL"+tab_desempenio.getValor("fecha_hasta_evdes") +" " +
				"EVALUADOR: "+tab_evaluadores.getValor("IDE_GEEDP"));
		con_guardar.setTitle("CONFIRMACION DE CALIFICACIÓN");
		con_guardar.getBot_aceptar().setMetodo("validarResultados");

		agregarComponente(con_guardar);

		sel_cal_fechas.setId("sel_cal_fechas");
		sel_cal_fechas.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_cal_fechas);


		// PARA AGREGAR ACTIVIDAD PUESTO
		//		Boton bot_agregar_act_p = new Boton();
		//		bot_agregar_act_p.setValue("Agregar Actividad Puesto");
		//		bot_agregar_act_p.setMetodo("agregarActividadPuesto");
		//		bot_agregar_act_p.setIcon("ui-icon-refresh");
		//		bar_botones.agregarBoton(bot_agregar_act_p);



		tab_detalle_competencia.setId("tab_detalle_competencia");
		tab_detalle_competencia.setTabla("CMP_DETALLE_COMPETENCIA", "IDE_CMDEC", 10);
		tab_detalle_competencia.getColumna("DETALLE_CMDEC").setNombreVisual("DETALLE COMPETENCIAS");
		tab_detalle_competencia.setCondicion("IDE_CMDEC =-1");
		tab_detalle_competencia.setTipoSeleccion(true);
		
		tab_detalle_competencia.getColumna("IDE_GEGCA").setVisible(false);
		tab_detalle_competencia.getColumna("IDE_CMFAC").setVisible(false);
		tab_detalle_competencia.getColumna("ACTIVO_CMDEC").setVisible(false);
		
		tab_detalle_competencia.dibujar();


		dia_actividad_puesto.setId("dia_actividad_puesto");
		dia_actividad_puesto.setTitle("ACTIVIDAD PUESTO");
		gri_activ_puesto.setColumns(2);
		gri_activ_puesto.getChildren().add(new Etiqueta("Actividad Puesto"));
		gri_activ_puesto.getChildren().add(tab_detalle_competencia);


		gri_activ_puesto.setStyle("width:" + (dia_actividad_puesto.getAnchoPanel() - 5) + "px; height:" + dia_actividad_puesto.getAltoPanel() + "px;overflow:auto;display:block;");
		dia_actividad_puesto.setDialogo(gri_activ_puesto);
		dia_actividad_puesto.getBot_aceptar().setMetodo("aceptarDialogoActividadPuesto");

		agregarComponente(dia_actividad_puesto);


		eti_indicador.setValue("Indicador");
		eti_peso.setValue("Peso");
		tex_peso.setSoloNumeros();
		
		dia_modificar_competencias.setId("dia_modificar_competencias");
		Grid gri_mc=new Grid();
		gri_mc.setColumns(2);
		gri_mc.getChildren().add(eti_indicador);
		gri_mc.getChildren().add(tex_indicador);
		gri_mc.getChildren().add(eti_peso);
		gri_mc.getChildren().add(tex_peso);
		gri_mc.setStyle("width:" + (dia_modificar_competencias.getAnchoPanel() - 5) + "px; height:" + dia_modificar_competencias.getAltoPanel() + "px;overflow:auto;display:block;");
		dia_modificar_competencias.setDialogo(gri_mc);
		dia_modificar_competencias.getBot_aceptar().setMetodo("aceptarModificarActividadPuesto");
		dia_modificar_competencias.setWidth("25%");
		dia_modificar_competencias.setHeight("25%");
		agregarComponente(dia_modificar_competencias);


	}

	
	public void modificarEvaluador(){
		if (!validarAccionCompetencias("EVL_ACTIVIDAD_PUESTO", tab_evaluadores.getValorSeleccionado(), "ACTIVO_EVACP",utilitario.getVariable("p_factor_actividad_puesto"))){
			utilitario.agregarMensajeInfo("No se puede modificar el Evaluador","La Competencia ya se encuentra calificada");
			return;
		}
tab_evaluadores.getFilas().get(tab_evaluadores.getFilaActual()).setLectura(false)	;
utilitario.addUpdate("tab_evaluadores");
		
	}
	
	public Dialogo getDia_modificar_competencias() {
		return dia_modificar_competencias;
	}



	public void setDia_modificar_competencias(Dialogo dia_modificar_competencias) {
		this.dia_modificar_competencias = dia_modificar_competencias;
	}



	public void aceptarModificarActividadPuesto(){
		
		double tot_peso=actividad_puesto.getSumaColumna("META_EVACP");
		double peso_mod=0;
		try {
			peso_mod=Double.parseDouble(actividad_puesto.getValor(actividad_puesto.getFilaActual(), "META_EVACP"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("tot peso "+tot_peso);
		System.out.println("peso a modi "+peso_mod);
		tot_peso=tot_peso-peso_mod;
		if (tex_peso.getValue()==null || tex_peso.getValue().toString().isEmpty()){
			utilitario.agregarMensajeInfo("El peso no puede ser nula o vacia", "");
			return;
	
		
		}
		if (tot_peso+Double.parseDouble(tex_peso.getValue()+"")>100){
			utilitario.agregarMensajeInfo("el total de peso sobrepasa de 100%", "Valor maximo "+(100-tot_peso));
			return;
		}
		
		utilitario.agregarMensajeInfo("Atencion para acentar el cambio debe guardar la actividad de puesto","");
		actividad_puesto.modificar(actividad_puesto.getFilaActual());
		actividad_puesto.setValor(actividad_puesto.getFilaActual(),"indicador_evacp", tex_indicador.getValue()+"");
		actividad_puesto.setValor(actividad_puesto.getFilaActual(),"meta_evacp", tex_peso.getValue()+"");
		utilitario.addUpdateTabla(actividad_puesto, "indicador_evacp,meta_evacp", "tab_tabulador:actividad_puesto");
		dia_modificar_competencias.cerrar();
	}

	public void modificarActividadPuesto(){
		if (!validarAccionCompetencias("EVL_ACTIVIDAD_PUESTO", tab_evaluadores.getValorSeleccionado(), "ACTIVO_EVACP",utilitario.getVariable("p_factor_actividad_puesto"))){
			utilitario.agregarMensajeInfo("No se puede modificar la actividad de puesto","La Competencia ya se encuentra evaluada");
			return;
		}
		tex_indicador.setValue(actividad_puesto.getValor("indicador_evacp"));
		tex_peso.setValue(actividad_puesto.getValor("meta_evacp"));
		dia_modificar_competencias.dibujar();
		
	}
	public void aceptarDialogoActividadPuesto(){
		if (tab_detalle_competencia.getListaFilasSeleccionadas().size()==0){
			utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos un detalle de competencia");
			return;
		}

		if (actividad_puesto.isFocus()){
			for (int i = 0; i < tab_detalle_competencia.getListaFilasSeleccionadas().size(); i++) {
				actividad_puesto.insertar();
				actividad_puesto.setValor("IDE_CMDEC",tab_detalle_competencia.getListaFilasSeleccionadas().get(i).getRowKey());
			}
		}else if (tab_competencia_gestion.isFocus()){
			for (int i = 0; i < tab_detalle_competencia.getListaFilasSeleccionadas().size(); i++) {
				tab_competencia_gestion.insertar();
				tab_competencia_gestion.setValor("IDE_CMDEC",tab_detalle_competencia.getListaFilasSeleccionadas().get(i).getRowKey());
			}
		}else if (tab_competencia_institucional.isFocus()){
			for (int i = 0; i < tab_detalle_competencia.getListaFilasSeleccionadas().size(); i++) {
				tab_competencia_institucional.insertar();
				tab_competencia_institucional.setValor("IDE_CMDEC",tab_detalle_competencia.getListaFilasSeleccionadas().get(i).getRowKey());
			}
		}
		else if (tab_competencia_tecnica.isFocus()){
			for (int i = 0; i < tab_detalle_competencia.getListaFilasSeleccionadas().size(); i++) {
				tab_competencia_tecnica.insertar();
				tab_competencia_tecnica.setValor("IDE_CMDEC",tab_detalle_competencia.getListaFilasSeleccionadas().get(i).getRowKey());
			}
		}

		dia_actividad_puesto.cerrar();

	}

	public void agregarActividadPuesto(){
		if (aut_empleado.getValor()==null){
			utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado");
			return;
		}

		if (tab_evaluadores.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede agregar una actividad de puesto ", "No existe un evaluador");
			return;
		}


		if (actividad_puesto.isFocus()){


			if (!validarAccionCompetencias("EVL_ACTIVIDAD_PUESTO", tab_evaluadores.getValorSeleccionado(), "ACTIVO_EVACP",utilitario.getVariable("p_factor_actividad_puesto"))){
				utilitario.agregarMensajeInfo("No se puede insertar una actividad de puesto","La Competencia ya se encuentra evaluada");
				return;
			}
			String ide_cmdec="";
			for (int i = 0; i < actividad_puesto.getTotalFilas(); i++) {
				if (actividad_puesto.getValor(i, "IDE_CMDEC")!=null && !actividad_puesto.getValor(i, "IDE_CMDEC").isEmpty()){
					ide_cmdec+=actividad_puesto.getValor(i, "IDE_CMDEC")+",";
				}
			}
			try {
				ide_cmdec=ide_cmdec.substring(0,ide_cmdec.length()-1);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (ide_cmdec.isEmpty()){
				ide_cmdec="-1";
			}
			
			tab_detalle_competencia.setCondicion("ACTIVO_CMDEC=TRUE and ide_cmdec not in ("+ide_cmdec+") AND ide_cmfac " +
					"in (select ide_cmfac from cmp_factor_competencia where ide_evfae="+utilitario.getVariable("p_factor_actividad_puesto")+") " +
					"and ide_gegca in ( select ide_gegca from gen_grupo_cargo_area a,gen_empleados_departamento_par b " +
					"where a.ide_gegro = b.ide_gegro " +
					"and a.ide_gecaf=b.ide_gecaf and a.ide_gedep=b.ide_gedep and IDE_GEEDP="+ide_geedp_activo+")");
			tab_detalle_competencia.getColumna("IDE_CMDEC").setNombreVisual("DETALLE COMPETENCIAS");
			tab_detalle_competencia.ejecutarSql();
			System.out.println("det comp "+tab_detalle_competencia.getSql());
			if (tab_detalle_competencia.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No existen actividades por insertar para el empleado seleccionado","");
				return;
			}
		}else if (tab_competencia_gestion.isFocus()){

			if (!validarAccionCompetencias("EVL_OTRA_COMPETENCIA", tab_evaluadores.getValorSeleccionado(), "ACTIVO_EVOTC",utilitario.getVariable("p_competencias_gestion"))){
				utilitario.agregarMensajeInfo("No se puede insertar una competencia de gestion","La Competencia ya se encuentra evaluada");
				return;
			}


			String ide_cmdec="";
			for (int i = 0; i < tab_competencia_gestion.getTotalFilas(); i++) {
				if (tab_competencia_gestion.getValor(i, "IDE_CMDEC")!=null && !tab_competencia_gestion.getValor(i, "IDE_CMDEC").isEmpty()){
					ide_cmdec+=tab_competencia_gestion.getValor(i, "IDE_CMDEC")+",";
				}
			}
			try {
				ide_cmdec=ide_cmdec.substring(0,ide_cmdec.length()-1);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (ide_cmdec.isEmpty()){
				ide_cmdec="-1";
			}

			//tab_detalle_competencia.getColumna("IDE_CMDEC").setNombreVisual("DETALLE COMPETENCIAS");
			tab_detalle_competencia.setCondicion("ACTIVO_CMDEC=TRUE and ide_cmdec not in ("+ide_cmdec+") AND ide_cmfac " +
					"in (select ide_cmfac from cmp_factor_competencia where ide_evfae="+utilitario.getVariable("p_competencias_gestion")+") " +
					"and ide_gegca in ( select ide_gegca from gen_grupo_cargo_area a,gen_empleados_departamento_par b " +
					"where a.ide_gegro = b.ide_gegro " +
					"and a.ide_gecaf=b.ide_gecaf and a.ide_gedep=b.ide_gedep and IDE_GEEDP="+ide_geedp_activo+")");
			tab_detalle_competencia.ejecutarSql();
			if (tab_detalle_competencia.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No existen competencias de gestion por insertar para el empleado seleccionado","");
				return;
			}


		}else if (tab_competencia_institucional.isFocus()){

			if (!validarAccionCompetencias("EVL_OTRA_COMPETENCIA", tab_evaluadores.getValorSeleccionado(), "ACTIVO_EVOTC",utilitario.getVariable("p_competencias_instituciones"))){
				utilitario.agregarMensajeInfo("No se puede insertar una competencia de gestion","La Competencia ya se encuentra evaluada");
				return;
			}


			String ide_cmdec="";
			for (int i = 0; i < tab_competencia_institucional.getTotalFilas(); i++) {
				if (tab_competencia_institucional.getValor(i, "IDE_CMDEC")!=null && !tab_competencia_institucional.getValor(i, "IDE_CMDEC").isEmpty()){
					ide_cmdec+=tab_competencia_institucional.getValor(i, "IDE_CMDEC")+",";
				}
			}
			try {
				ide_cmdec=ide_cmdec.substring(0,ide_cmdec.length()-1);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (ide_cmdec.isEmpty()){
				ide_cmdec="-1";
			}

			
			tab_detalle_competencia.setCondicion("ACTIVO_CMDEC=TRUE and ide_cmdec not in ("+ide_cmdec+") AND ide_cmfac " +
					"in (select ide_cmfac from cmp_factor_competencia where ide_evfae="+utilitario.getVariable("p_competencias_instituciones")+") " +
					"and ide_gegca in ( select ide_gegca from gen_grupo_cargo_area a,gen_empleados_departamento_par b " +
					"where a.ide_gegro = b.ide_gegro " +
					"and a.ide_gecaf=b.ide_gecaf and a.ide_gedep=b.ide_gedep and IDE_GEEDP="+ide_geedp_activo+")");
			tab_detalle_competencia.ejecutarSql();
			if (tab_detalle_competencia.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No existen competencias de gestion por insertar para el empleado seleccionado","");
				return;
			}

		}
		else if (tab_competencia_tecnica.isFocus()){

			if (!validarAccionCompetencias("EVL_OTRA_COMPETENCIA", tab_evaluadores.getValorSeleccionado(), "ACTIVO_EVOTC",utilitario.getVariable("p_competencias_tecnicas"))){
				utilitario.agregarMensajeInfo("No se puede insertar una competencia de gestion","La Competencia ya se encuentra evaluada");
				return;
			}


			String ide_cmdec="";
			for (int i = 0; i < tab_competencia_tecnica.getTotalFilas(); i++) {
				if (tab_competencia_tecnica.getValor(i, "IDE_CMDEC")!=null && !tab_competencia_tecnica.getValor(i, "IDE_CMDEC").isEmpty()){
					ide_cmdec+=tab_competencia_tecnica.getValor(i, "IDE_CMDEC")+",";
				}
			}
			try {
				ide_cmdec=ide_cmdec.substring(0,ide_cmdec.length()-1);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (ide_cmdec.isEmpty()){
				ide_cmdec="-1";
			}

			
			tab_detalle_competencia.setCondicion("ACTIVO_CMDEC=TRUE and ide_cmdec not in ("+ide_cmdec+") AND ide_cmfac " +
					"in (select ide_cmfac from cmp_factor_competencia where ide_evfae="+utilitario.getVariable("p_competencias_tecnicas")+") " +
					"and ide_gegca in ( select ide_gegca from gen_grupo_cargo_area a,gen_empleados_departamento_par b " +
					"where a.ide_gegro = b.ide_gegro " +
					"and a.ide_gecaf=b.ide_gecaf and a.ide_gedep=b.ide_gedep and IDE_GEEDP="+ide_geedp_activo+")");
			tab_detalle_competencia.ejecutarSql();
			if (tab_detalle_competencia.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No existen competencias de gestion por insertar para el empleado seleccionado","");
				return;
			}

		}


		dia_actividad_puesto.dibujar();
	}

	public void activarFilaActividadPuesto(int fila){

	}


public boolean ValidarCompetencias(String IDE_EVEVA){
	
	System.out.println("BABYBULI1"+tab_evaluadores.getValor("IDE_EVEVA"));
	System.out.println("BABYBULI"+tab_evaluadores.getValorSeleccionado());
	ide_geedp_activo=ser_gestion.getIdeContratoActivo(aut_empleado.getValor());
	TablaGenerica tab_competencias= utilitario.consultar("SELECT * FROM (SELECT GRU.IDE_EVGRF,FAC.IDE_EVFAE,FAC.DETALLE_EVFAE,EDP.IDE_GEEDP, " +
			"EDP.IDE_GEGRO,GRO.DETALLE_GEGRO,CAF.DETALLE_GECAF,GRU.PESO_POR_EVGRF  FROM " +
			"GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP " +
			"left join GEN_GRUPO_OCUPACIONAL GRO ON GRO.IDE_GEGRO=EDP.IDE_GEGRO " +
			"left join GEN_CARGO_FUNCIONAL CAF ON CAF.IDE_GECAF=EDP.IDE_GECAF " +
			"LEFT JOIN EVL_GRUPO_FACTOR GRU ON GRU.IDE_GEGRO=EDP.IDE_GEGRO " +
			"LEFT JOIN EVL_FACTOR_EVALUACION FAC ON FAC.IDE_EVFAE=GRU.IDE_EVFAE " +
			"WHERE EDP.IDE_GEEDP="+ide_geedp_activo+" " +
			")c " +
			"LEFT JOIN " +
			"((SELECT A.IDE_EVEVA,A.IDE_EVFAE FROM EVL_OTRA_COMPETENCIA A " +
			"GROUP BY  A.IDE_EVEVA,A.IDE_EVFAE " +
			"HAVING A.IDE_EVEVA ="+tab_evaluadores.getValor("IDE_EVEVA")+" " +
			") " +
			"UNION ( " +
			"SELECT B.IDE_EVEVA,B.IDE_EVFAE FROM  EVL_ACTIVIDAD_PUESTO  B " +
			"GROUP BY B.IDE_EVEVA,B.IDE_EVFAE " +
			"HAVING B.IDE_EVEVA="+tab_evaluadores.getValor("IDE_EVEVA")+") " +
			"ORDER BY IDE_EVEVA) " +
			"b on b.IDE_EVFAE=c.IDE_EVFAE " +
			"where b.ide_evfae is null"); 
	tab_competencias.imprimirSql();
	
	if(tab_competencias.getTotalFilas()>0){
		for (int i = 0; i < tab_competencias.getTotalFilas(); i++)  {
			
	
	String competencia=	tab_competencias.getValor(i,"DETALLE_EVFAE");
	
		utilitario.agregarMensajeInfo("DEBE INGRESAR COMPETENCIA",competencia);
		return 	true;
		}
		
	}else {
		utilitario.agregarMensajeInfo("CORRECTA ASIGNACION DE COMPETENCIAS","");
	//return;
	}
	
return false;
}

	public boolean validarAccionCompetencias(String nombre_tabla,String ide_eveva,String campo_activo,String ide_evfae)
	{
		TablaGenerica tab_competencia = utilitario.consultar("select * from "+nombre_tabla+" " +
				"WHERE ide_evfae="+ide_evfae+" and IDE_EVEVA="+ide_eveva);
		System.out.println("tab comp "+tab_competencia.getSql());
		for (int i = 0; i < tab_competencia.getTotalFilas(); i++) {
			if(tab_competencia.getValor(i,campo_activo)==null 
					|| tab_competencia.getValor(i,campo_activo).isEmpty()
					|| tab_competencia.getValor(i,campo_activo).equalsIgnoreCase("false")
					||tab_competencia.getValor(i,campo_activo).equalsIgnoreCase("0")){
				return false;
			}
		}
		return true;
	}



	public Tabla getActividad_puesto() {
		return actividad_puesto;
	}

	public void setActividad_puesto(Tabla actividad_puesto) {
		this.actividad_puesto = actividad_puesto;
	}

	public Dialogo getDia_actividad_puesto() {
		return dia_actividad_puesto;
	}

	public void setDia_actividad_puesto(Dialogo dia_actividad_puesto) {
		this.dia_actividad_puesto = dia_actividad_puesto;
	}

	public Tabla getTab_detalle_competencia() {
		return tab_detalle_competencia;
	}

	public void setTab_detalle_competencia(Tabla tab_detalle_competencia) {
		this.tab_detalle_competencia = tab_detalle_competencia;
	}

	public void enviarCorreo(){
		EnviarCorreo env_enviar = new EnviarCorreo();
		//Enviar a cada uno de los evaluadores
		String str_asunto="EVALUACIÓN DE DESEMPEÑO";

		String ide_geedp=tab_evaluadores.getStringColumna("ide_geedp");
		if(ide_geedp!=null && ide_geedp.isEmpty()==false){
			TablaGenerica tab_correos= ser_empleado.getCorreoEmpleadoPepartamentoPartida(ide_geedp);

			StringBuilder str_mensaje=new StringBuilder();				
			str_mensaje.append("<p style='font-size: 18px;font-weight: bold'>").append("EVALUACIÓN DE DESEMPEÑO").append("</p>").append(" \n");
			str_mensaje.append("<p>Para su conocimiento, se le informa que tiene que evaluar el desepeño del siguiente empleado : <strong>").append(((Object[])aut_empleado.getValue())[2]).append(" </strong>");			
			for(int i=0;i<tab_correos.getTotalFilas();i++){
				String str_mail=tab_correos.getValor(i, "detalle_gtcor");
				System.out.println(str_mail+"  ");
				if(str_mail==null || str_mail.isEmpty()){
					utilitario.agregarMensajeError("El evaluador "+tab_correos.getValor(i, "EMPLEADO")+" no tiene configurado un correo para notificaciones", "");
					continue;
				}
				String str_msj= env_enviar.agregarCorreo(str_mail, str_asunto, str_mensaje.toString(), null);
				if(str_msj.isEmpty()==false){
					//Fallo el envio de coorreo
					//str_resultado.append(getFormatoError("No se puede enviar el correo a "+str_mail+" motivo: "+str_msj));
					utilitario.agregarMensajeError("No se pudo enviar a "+str_mail,str_msj);
				}

			}
			String str=env_enviar.enviarTodos();
			if(str.trim().isEmpty()){
				utilitario.agregarMensaje("Se envio correctamente", "");
			}
			else{
				utilitario.agregarMensajeError("No se pudo enviar",str);
			}

		}
		else{
			utilitario.agregarMensajeInfo("No existen avaluadores", "No se puede enviar el correo electrónico");
		}

	}

	String ide_geedp_activo="";

	public void filtrarAnticiposEmpleado(SelectEvent evt){
		aut_empleado.onSelect(evt);
		ide_geedp_activo=ser_gestion.getIdeContratoActivo(aut_empleado.getValor());
		tab_desempenio.setCondicion("IDE_GTEMP="+aut_empleado.getValor());
		tab_desempenio.ejecutarSql();
		
		
		
		tab_evaluadores.getColumna("IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
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
				"WHERE EPAR.ACTIVO_GEEDP=true and EPAR.IDE_GEEDP!="+ide_geedp_activo);
		tab_evaluadores.ejecutarValorForanea(tab_desempenio.getValorSeleccionado());

		System.out.println("IDE GEEDP ACTIVO "+ide_geedp_activo);
		actividad_puesto.getColumna("IDE_CMDEC").setCombo("SELECT ide_cmdec,detalle_cmdec as detalle_cmdec from cmp_detalle_competencia where ide_cmfac " +
				"in (select ide_cmfac from cmp_factor_competencia where ide_evfae="+utilitario.getVariable("p_factor_actividad_puesto")+") " +
				"and ide_gegca in ( select ide_gegca from gen_grupo_cargo_area a,gen_empleados_departamento_par b " +
				"where a.ide_gegro = b.ide_gegro " +
				"and a.ide_gecaf=b.ide_gecaf and a.ide_gedep=b.ide_gedep and IDE_GEEDP="+ide_geedp_activo+")");
		actividad_puesto.ejecutarSql();

		//		actividad_puesto.ejecutarValorForanea(tab_evaluadores.getValorSeleccionado());
		//		actividad_puesto.actualizarCombos();




		tab_competencia_gestion.getColumna("IDE_CMDEC").setCombo("SELECT ide_cmdec,detalle_cmdec as detalle_cmdec from cmp_detalle_competencia where ide_cmfac " +
				"in (select ide_cmfac from cmp_factor_competencia where ide_evfae="+utilitario.getVariable("p_competencias_gestion")+") " +
				"and ide_gegca in ( select ide_gegca from gen_grupo_cargo_area a,gen_empleados_departamento_par b " +
				"where a.ide_gegro = b.ide_gegro " +
				"and a.ide_gecaf=b.ide_gecaf and a.ide_gedep=b.ide_gedep and IDE_GEEDP="+ide_geedp_activo+")");
		tab_competencia_gestion.ejecutarSql();
		tab_competencia_institucional.getColumna("IDE_CMDEC").setCombo("SELECT ide_cmdec,substr(detalle_cmdec,1,100) as detalle_cmdec from cmp_detalle_competencia where ide_cmfac " +
				"in (select ide_cmfac from cmp_factor_competencia where ide_evfae="+utilitario.getVariable("p_competencias_instituciones")+") " +
				"and ide_gegca in ( select ide_gegca from gen_grupo_cargo_area a,gen_empleados_departamento_par b " +
				"where a.ide_gegro = b.ide_gegro " +
				"and a.ide_gecaf=b.ide_gecaf and a.ide_gedep=b.ide_gedep and IDE_GEEDP="+ide_geedp_activo+")");
		tab_competencia_institucional.ejecutarSql();
		tab_competencia_tecnica.getColumna("IDE_CMDEC").setCombo("SELECT ide_cmdec,substr(detalle_cmdec,1,100) as detalle_cmdec from cmp_detalle_competencia where ide_cmfac " +
				"in (select ide_cmfac from cmp_factor_competencia where ide_evfae="+utilitario.getVariable("p_competencias_tecnicas")+") " +
				"and ide_gegca in ( select ide_gegca from gen_grupo_cargo_area a,gen_empleados_departamento_par b " +
				"where a.ide_gegro = b.ide_gegro " +
				"and a.ide_gecaf=b.ide_gecaf and a.ide_gedep=b.ide_gedep and IDE_GEEDP="+ide_geedp_activo+")");
		tab_competencia_tecnica.ejecutarSql();
	}

	public void validarCero(AjaxBehaviorEvent evt){
		tab_evaluadores.modificar(evt);
		if(tab_evaluadores.getValor("POR_PESO_EVEVA")!=null){
			double dou_valor=0;
			try {
				dou_valor=Double.parseDouble(tab_evaluadores.getValor("POR_PESO_EVEVA"));

			} catch (Exception e) {
				// TODO: handle exception
			}	
			if(dou_valor<=0){
				tab_evaluadores.setValor("POR_PESO_EVEVA", "");
				utilitario.addUpdateTabla(tab_evaluadores, "POR_PESO_EVEVA", "");
				utilitario.agregarMensajeInfo("El campo PESO EVALUADOR no puede menor o igual a 0", "");
			}
			else{
				tab_evaluadores.sumarColumnas();
				utilitario.addUpdate("tab_evaluadores");
			}
		}
	}

	public void validarCeroMeta(AjaxBehaviorEvent evt){
		actividad_puesto.modificar(evt);
		if(actividad_puesto.getValor("meta_evacp")!=null){
			double dou_valor=0;
			try {
				dou_valor=Double.parseDouble(actividad_puesto.getValor("meta_evacp"));

			} catch (Exception e) {
				// TODO: handle exception
			}	
			if(dou_valor<=0){
				actividad_puesto.setValor("meta_evacp", "");
				utilitario.addUpdateTabla(actividad_puesto, "meta_evacp", "");
				utilitario.agregarMensajeInfo("El Campo Peso en Actividad Puesto  no puede menor o igual a 0", "");
			}
			else{
				actividad_puesto.sumarColumnas();
				utilitario.addUpdate("tab_tabulador:actividad_puesto");
			}
		}
	}


	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_desempenio.limpiar();
		tab_evaluadores.limpiar();
		actividad_puesto.limpiar();
		tab_competencia_gestion.limpiar();		
		tab_competencia_institucional.limpiar();
		tab_competencia_tecnica.limpiar();
		ide_geedp_activo="";
		aut_empleado.limpiar();
		aut_empleado.limpiar();
		utilitario.addUpdate("aut_empleado");// limpia y refresca el autocompletar
		tab_evaluadores.sumarColumnas();
	}



	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_desempenio.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (ide_geedp_activo!=null && !ide_geedp_activo.isEmpty()){		
					tab_desempenio.insertar();
					tab_desempenio.setValor("IDE_GEEDP",ide_geedp_activo);
					tab_desempenio.setValor("IDE_GTEMP", aut_empleado.getValor());
					TablaGenerica tab_titulo_profesional=utilitario.consultar("SELECT a.IDE_GTEMP,b.IDE_GTEDE,(case when b.DETALLE_GTTTP is null then 'SIN TITULO' else b.DETALLE_GTTTP end) as titulo " +
							"FROM GTH_EMPLEADO a " +
							"left join " +
							"(SELECT a.IDE_GTEDE,a.IDE_GTEMP,b.DETALLE_GTTTP FROM GTH_EDUCACION_EMPLEADO a, GTH_TIPO_TITULO_PROFESIONAL b " +
							"where a.IDE_GTTTP=b.IDE_GTTTP and b.ACTIVO_GTTTP=TRUE LIMIT 1) b on a.IDE_GTEMP=b.IDE_GTEMP where a.IDE_GTEMP="+aut_empleado.getValor());
					tab_desempenio.setValor("TITULO_PROFESIONAL_EVDES", tab_titulo_profesional.getValor("titulo"));
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "El contrato del empleado no esta activo");
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado");
			}
		}
		else if (tab_evaluadores.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (tab_desempenio.getTotalFilas()>0){					
					if (!validarAccionCompetencias("EVL_ACTIVIDAD_PUESTO", tab_evaluadores.getValorSeleccionado(), "ACTIVO_EVACP",utilitario.getVariable("p_factor_actividad_puesto"))){
						utilitario.agregarMensajeInfo("No se puede insertar el Evaluador","La Competencia ya se encuentra calificada");
						return;
					}

					
					tab_evaluadores.insertar();


				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe insertar un Desempeño");				
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado");
			}			
		}
		else if (actividad_puesto.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (tab_evaluadores.getTotalFilas()>0){
					agregarActividadPuesto();
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe insertar un Evaluador");				
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado");
			}			
		}	
		else if (tab_competencia_gestion.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (tab_evaluadores.getTotalFilas()>0){
					agregarActividadPuesto();
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe insertar un Evaluador");				
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado");
			}			
		}
		else if (tab_competencia_institucional.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (tab_evaluadores.getTotalFilas()>0){
					agregarActividadPuesto();
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe insertar un Evaluador");				
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado");
			}			
		}
		else if (tab_competencia_tecnica.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (tab_evaluadores.getTotalFilas()>0){
					agregarActividadPuesto();
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe insertar un Evaluador");				
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado");
			}			
		}
	}

	@Override
	public void guardar() {		
		if (aut_empleado.getValor()!=null){		
			if (tab_desempenio.getValor("fecha_desde_evdes")==null ||  tab_desempenio.getValor("fecha_desde_evdes").isEmpty()){
				utilitario.agregarMensajeError("No se puede guardar", "Debe seleccionar una fecha inicio");
				return;
			}
			if (tab_desempenio.getValor("fecha_hasta_evdes")==null ||  tab_desempenio.getValor("fecha_hasta_evdes").isEmpty()){
				utilitario.agregarMensajeError("No se puede guardar", "Debe seleccionar una fecha hasta");
				return;
			}
			if (tab_desempenio.getTotalFilas()==0) {
				utilitario.agregarMensajeError("No se puede guardar","Debe ingresar periodo a efectuar Evaluacion");
				return;
			}
			if (utilitario.isFechaMenor(utilitario.getFecha(tab_desempenio.getValor("fecha_hasta_evdes")), utilitario.getFecha(tab_desempenio.getValor("fecha_desde_evdes")))){
				utilitario.agregarMensajeError("No se puede guardar", "La fecha desempenio hasta no puede ser menor que la fecha desde");
				return;
			}		
			
		

		
			
		
	

			
			
		/*	for (int i = 0; i < tab_evaluadores.getTotalFilas(); i++) {
				if (tab_evaluadores.getValor("fecha_eveva")==null || tab_evaluadores.getValor("fecha_eveva").isEmpty()) {
					utilitario.agregarMensajeError("No se puede guardar", "La fecha maxima de  evaluacion no puede ser nula o vacia");
					return;
					
				}
				
				
				if (tab_evaluadores.getValor(i,"POR_PESO_EVEVA")==null || tab_evaluadores.getValor(i,"POR_PESO_EVEVA").isEmpty()){
					utilitario.agregarMensajeInfo("No se puede guardar Evaluadores de puesto", "El peso de la actividad no puede ser nula o vacia");
					return;
				}
				
				
				if (utilitario.isFechaMenor(utilitario.getFecha(tab_evaluadores.getValor("fecha_eveva")), utilitario.getFecha(tab_desempenio.getValor("fecha_desde_evdes")))){
					utilitario.agregarMensajeError("No se puede guardar", "La fecha maxima evaluacion no puede ser menor que la fecha desde");
					return;
				}
					if (tab_evaluadores.getValor(i,"POR_PESO_EVEVA")==null || tab_evaluadores.getValor(i,"POR_PESO_EVEVA").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar Evaluadores de puesto", "El peso de la actividad no puede ser nula o vacia");
				return;
			}

			}*/

			if (tab_evaluadores.getTotalFilas()>0){
				
			
			
			if (tab_evaluadores.getValor("IDE_GEEDP")==null || tab_evaluadores.getValor("IDE_GEEDP").isEmpty()) {
				utilitario.agregarMensajeError("No se puede guardar","Debe seleccionar persona que efectuara Evaluacion");
				return;
			}
			if(tab_evaluadores.getSumaColumna("POR_PESO_EVEVA")!=100.00){
				utilitario.agregarMensajeError("La suma de las columna de la peso debe ser igual a 100", "");
				return;
			}
			
				
			
			if (tab_evaluadores.getValor("fecha_eveva")==null || tab_evaluadores.getValor("fecha_eveva").isEmpty()) {
				utilitario.agregarMensajeError("No se puede guardar", "La fecha maxima de  evaluacion no puede ser nula o vacia");
				return;
				
			}
					
				
			
		
			
			}
	
			else {
				utilitario.agregarMensajeError("No se puede guardar", "Debe escoger un Evaluador");
				return;
			}
			
			

		

			if (actividad_puesto.getTotalFilas()>0){
		
			if(actividad_puesto.getSumaColumna("meta_evacp")!=100.00){
				utilitario.agregarMensajeError("La suma de las columna de la peso debe ser igual a 100", "");
				return;
			}
			
			for (int i = 0; i < actividad_puesto.getTotalFilas(); i++) {
				if (actividad_puesto.getValor(i,"meta_evacp")==null || actividad_puesto.getValor(i,"meta_evacp").isEmpty()){
					utilitario.agregarMensajeInfo("No se puede guardar la actividad de puesto", "El peso de la actividad no puede ser nula o vacia");
					return;
				}
			}
			}
		
	
			if (tab_desempenio.guardar()){	
			
				
		if(tab_evaluadores.guardar()){						
		
					if (actividad_puesto.guardar()){
						
						if(tab_competencia_gestion.guardar()){						
						
							if(tab_competencia_institucional.guardar()){
						
								if(tab_competencia_tecnica.guardar()){									
									//ValidarCompetencias();
									guardarPantalla();	
									tab_evaluadores.getFilas().get(tab_evaluadores.getFilaActual()).setLectura(true)	;
									tab_evaluadores.sumarColumnas();
								}
							}
						}
					}
				}
			}
		}
	
		else{
			utilitario.agregarMensajeInfo("No se puede guardar ", "Debe seleccionar un Empleado");
		
		}

	}



	@Override
	public void eliminar() {
		if (aut_empleado.getValor()!=null){			
			if (tab_desempenio.isFocus()){	
				tab_desempenio.eliminar();							
			}else if(tab_evaluadores.isFocus()){
				if (!validarAccionCompetencias("EVL_ACTIVIDAD_PUESTO", tab_evaluadores.getValorSeleccionado(), "ACTIVO_EVACP",utilitario.getVariable("p_factor_actividad_puesto"))){
					utilitario.agregarMensajeInfo("No se puede eliminar el Evaluador","La Competencia ya se encuentra calificada");
					return;
				}
				if (actividad_puesto.getTotalFilas()>0) {
					utilitario.agregarMensajeInfo("No se puede eliminar el Evaluador","Primero Eliminar Actividades de Puesto");
					return;
			
				}
				if (tab_competencia_gestion.getTotalFilas()>0) {
					utilitario.agregarMensajeInfo("No se puede eliminar el Evaluador","Primero Eliminar Competencia de Gestion");
					return;
			
				}
				if (tab_competencia_institucional.getTotalFilas()>0) {
					utilitario.agregarMensajeInfo("No se puede eliminar el Evaluador","Primero Eliminar Competencias Institucionales");
					return;
			
				}
				if (tab_competencia_tecnica.getTotalFilas()>0) {
					utilitario.agregarMensajeInfo("No se puede eliminar el Evaluador","Primero Eliminar Competencias Tecnicas");
					return;
			
				}
				
				if (utilitario.consultar("select * from EVL_RESULTADO where IDE_EVEVA="+tab_evaluadores.getValorSeleccionado()).getTotalFilas()>0) {
					utilitario.agregarMensajeInfo("No se puede eliminar el Evaluador","Eliminar Resultados existentes");
					return;
					
				}
				
				
				utilitario.getConexion().agregarSqlPantalla("delete from EVL_EVALUADORES where IDE_EVEVA="+tab_evaluadores.getValorSeleccionado());
				utilitario.getConexion().guardarPantalla();
				tab_evaluadores.ejecutarValorForanea(tab_desempenio.getValorSeleccionado());

				tab_evaluadores.sumarColumnas();
			}
			else if(actividad_puesto.isFocus()){

				if (actividad_puesto.isFilaInsertada()){
					actividad_puesto.eliminar();
				}else{
					if (actividad_puesto.getTotalFilas()>0){
						if (!validarAccionCompetencias("EVL_ACTIVIDAD_PUESTO", tab_evaluadores.getValorSeleccionado(), "ACTIVO_EVACP",utilitario.getVariable("p_factor_actividad_puesto"))){
							utilitario.agregarMensajeInfo("No se puede insertar una actividad de puesto","La Competencia ya se encuentra evaluada");
							return;
						}

						utilitario.getConexion().agregarSqlPantalla("delete from EVL_ACTIVIDAD_PUESTO WHERE IDE_EVACP="+actividad_puesto.getValorSeleccionado());

						utilitario.getConexion().guardarPantalla();
						actividad_puesto.ejecutarValorForanea(tab_evaluadores.getValorSeleccionado());
					}
				}
			}
			else if(tab_evaluadores.isFocus()){
				tab_evaluadores.eliminar();
			}
			else if(tab_competencia_gestion.isFocus()){

				if (tab_competencia_gestion.isFilaInsertada()){
					tab_competencia_gestion.eliminar();
				}else{
					if (tab_competencia_gestion.getTotalFilas()>0){
						if (!validarAccionCompetencias("EVL_OTRA_COMPETENCIA", tab_evaluadores.getValorSeleccionado(), "ACTIVO_EVOTC",utilitario.getVariable("p_competencias_gestion"))){
							utilitario.agregarMensajeInfo("No se puede insertar una competencia de gestion","La Competencia ya se encuentra evaluada");
							return;
						}

						utilitario.getConexion().agregarSqlPantalla("delete from EVL_OTRA_COMPETENCIA WHERE IDE_EVOTC="+tab_competencia_gestion.getValorSeleccionado());

						utilitario.getConexion().guardarPantalla();
						tab_competencia_gestion.ejecutarValorForanea(tab_evaluadores.getValorSeleccionado());
					}
				}
				
				
				
			}
			else if(tab_competencia_institucional.isFocus()){
				

				if (tab_competencia_institucional.isFilaInsertada()){
					tab_competencia_institucional.eliminar();
				}else{
					if (tab_competencia_institucional.getTotalFilas()>0){
						if (!validarAccionCompetencias("EVL_OTRA_COMPETENCIA", tab_evaluadores.getValorSeleccionado(), "ACTIVO_EVOTC",utilitario.getVariable("p_competencias_instituciones"))){
							utilitario.agregarMensajeInfo("No se puede insertar una competencia de gestion","La Competencia ya se encuentra evaluada");
							return;
						}

						utilitario.getConexion().agregarSqlPantalla("delete from EVL_OTRA_COMPETENCIA WHERE IDE_EVOTC="+tab_competencia_institucional.getValorSeleccionado());

						utilitario.getConexion().guardarPantalla();
						tab_competencia_institucional.ejecutarValorForanea(tab_evaluadores.getValorSeleccionado());
					}
				}
					

			}
			else if(tab_competencia_tecnica.isFocus()){
				

				if (tab_competencia_tecnica.isFilaInsertada()){
					tab_competencia_tecnica.eliminar();
				}else{
					if (tab_competencia_tecnica.getTotalFilas()>0){
						if (!validarAccionCompetencias("EVL_OTRA_COMPETENCIA", tab_evaluadores.getValorSeleccionado(), "ACTIVO_EVOTC",utilitario.getVariable("p_competencias_tecnicas"))){
							utilitario.agregarMensajeInfo("No se puede insertar una competencia de gestion","La Competencia ya se encuentra evaluada");
							return;
						}

						utilitario.getConexion().agregarSqlPantalla("delete from EVL_OTRA_COMPETENCIA WHERE IDE_EVOTC="+tab_competencia_tecnica.getValorSeleccionado());

						utilitario.getConexion().guardarPantalla();
						tab_competencia_tecnica.ejecutarValorForanea(tab_evaluadores.getValorSeleccionado());
					}
				}
									
				
				
						}
		}else{
			utilitario.agregarMensajeInfo("No se puede guardar el Anticipo", "Debe seleccionar un Empleado");
		}
	}
	public void reativarEvaluacion(){

		String ide_geedp=tab_evaluadores.getStringColumna("ide_geedp");
		if(ide_geedp!=null && ide_geedp.isEmpty()==false){
			if(tab_evaluadores.getValor("ACTIVO_EVEVA").equals("false")){
				con_guardar.dibujar();
				if(con_guardar.isVisible()){			
					TablaGenerica tab_ide_geedp=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
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
							"WHERE EPAR.ACTIVO_GEEDP=true and EPAR.IDE_GEEDP="+tab_evaluadores.getValor("IDE_GEEDP"));

					con_guardar.setMessage("ESTA SEGURO DE REACTIVAR: LA SIGUIENTE EVALUACION: PERIODO DE EVALUACION= DEL "+tab_desempenio.getValor("fecha_desde_evdes")+" AL "+tab_desempenio.getValor("fecha_hasta_evdes") +" " +
							"EVALUADOR: "+tab_ide_geedp.getValor("NOMBRES_APELLIDOS"));
					con_guardar.setTitle("CONFIRMACION DE CALIFICACIÓN");
					con_guardar.getBot_aceptar().setMetodo("validarResultados");		
					utilitario.addUpdate("con_guardar");			
				}	
			}else{
				utilitario.agregarMensajeInfo("No se puede reactivar", "El evaluador debe de estar inactivo");
			}			
		}else{
			utilitario.agregarMensajeInfo("No se puede Reactivar la Evaluacion", "Debe seleccionar un Empleado");
		}
	}


	public void validarResultados() {
		con_guardar.cerrar();
		utilitario.getConexion().agregarSqlPantalla("update EVL_DESEMPENIO set activo_evdes=1 where ide_evdes="+tab_desempenio.getValorSeleccionado());
		utilitario.getConexion().agregarSqlPantalla("update EVL_EVALUADORES set activo_eveva=1 where  ide_eveva="+tab_evaluadores.getValorSeleccionado());
		utilitario.getConexion().agregarSqlPantalla("update EVL_ACTIVIDAD_PUESTO set activo_evacp=1 where ide_eveva="+tab_evaluadores.getValorSeleccionado());
		utilitario.getConexion().agregarSqlPantalla("update EVL_OTRA_COMPETENCIA set activo_evotc=1 where ide_eveva="+tab_evaluadores.getValorSeleccionado());
		utilitario.getConexion().agregarSqlPantalla("DELETE from EVL_RESULTADO WHERE IDE_EVEVA="+tab_evaluadores.getValorSeleccionado());

		guardarPantalla();
		tab_desempenio.ejecutarSql();
		tab_evaluadores.ejecutarSql();
		actividad_puesto.ejecutarSql();
		tab_competencia_gestion.ejecutarSql();
		tab_competencia_institucional.ejecutarSql();
		tab_competencia_tecnica.ejecutarSql();

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

	public Tabla gettab_desempenio() {
		return tab_desempenio;
	}

	public void settab_desempenio(Tabla tab_desempenio) {
		this.tab_desempenio = tab_desempenio;
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

	public Tabla gettab_evaluadores() {
		return tab_evaluadores;
	}

	public void settab_evaluadores(Tabla tab_evaluadores) {
		this.tab_evaluadores = tab_evaluadores;
	}

	public Tabla getactividad_puesto() {
		return actividad_puesto;
	}

	public void setactividad_puesto(Tabla actividad_puesto) {
		this.actividad_puesto = actividad_puesto;
	}

	public Tabla getTab_competencia_gestion() {
		return tab_competencia_gestion;
	}

	public void setTab_competencia_gestion(Tabla tab_competencia_gestion) {
		this.tab_competencia_gestion = tab_competencia_gestion;
	}

	public Tabla getTab_competencia_institucional() {
		return tab_competencia_institucional;
	}

	public void setTab_competencia_institucional(Tabla tab_competencia_institucional) {
		this.tab_competencia_institucional = tab_competencia_institucional;
	}

	public Tabla getTab_competencia_tecnica() {
		return tab_competencia_tecnica;
	}

	public void setTab_competencia_tecnica(Tabla tab_competencia_tecnica) {
		this.tab_competencia_tecnica = tab_competencia_tecnica;
	}

	public Tabla getTab_desempenio() {
		return tab_desempenio;
	}

	public void setTab_desempenio(Tabla tab_desempenio) {
		this.tab_desempenio = tab_desempenio;
	}

	public Tabla getTab_evaluadores() {
		return tab_evaluadores;
	}

	public void setTab_evaluadores(Tabla tab_evaluadores) {
		this.tab_evaluadores = tab_evaluadores;
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}


	public SeleccionCalendario getSel_cal_fechas() {
		return sel_cal_fechas;
	}

	public void setSel_cal_fechas(SeleccionCalendario sel_cal_fechas) {
		this.sel_cal_fechas = sel_cal_fechas;
	}

	public SeleccionTabla getSel_tab_grupo_ocupacional() {
		return sel_tab_grupo_ocupacional;
	}

	public void setSel_tab_grupo_ocupacional(
			SeleccionTabla sel_tab_grupo_ocupacional) {
		this.sel_tab_grupo_ocupacional = sel_tab_grupo_ocupacional;
	}

	public SeleccionTabla getSel_tab_cargo_funcional() {
		return sel_tab_cargo_funcional;
	}

	public void setSel_tab_cargo_funcional(SeleccionTabla sel_tab_cargo_funcional) {
		this.sel_tab_cargo_funcional = sel_tab_cargo_funcional;
	}

	@Override
	public void aceptarReporte() {
		if(rep_reporte.getNombre().equals("Detalle Resultado Evaluacion")){
			if(rep_reporte.isVisible()){
				if(tab_desempenio.getValorSeleccionado()==null){
					utilitario.agregarMensajeInfo("No existe un registro seleccionado para generar el reporte", "");
					return;
				}
				Map map_parametros=new HashMap();
				map_parametros.put("ide_evdes", pckUtilidades.CConversion.CInt(tab_desempenio.getValorSeleccionado()));
				map_parametros.put("titulo", "DETALLE RESULTADO EVALUACIÓN");
				sef_reporte.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();
			}

		}else


			if(rep_reporte.getReporteSelecionado().equals("Detalle Evaluacion X Grupo")){

				if(rep_reporte.isVisible()){
					p_parametros =new HashMap();
					rep_reporte.cerrar();
					sel_cal_fechas.setFecha1(null);
					sel_cal_fechas.setFecha2(null);					
					sel_cal_fechas.dibujar();		
				}else if(sel_cal_fechas.isVisible()){
					if(sel_cal_fechas.isFechasValidas()){
						p_parametros.put("fecha_inicio", sel_cal_fechas.getFecha1String());
						p_parametros.put("fecha_fin", sel_cal_fechas.getFecha2String());					
						System.out.println(""+sel_cal_fechas.getFecha1String());
						System.out.println(""+sel_cal_fechas.getFecha2String());				

						sel_tab_grupo_ocupacional.getTab_seleccion().setSql("SELECT IDE_GEGRO,DETALLE_GEGRO FROM  GEN_GRUPO_OCUPACIONAL");
						sel_tab_grupo_ocupacional.getTab_seleccion().ejecutarSql();
						sel_tab_grupo_ocupacional.getBot_aceptar().setMetodo("aceptarReporte");
						sel_cal_fechas.cerrar();
						sel_tab_grupo_ocupacional.dibujar();
					}else{
						utilitario.agregarMensajeInfo("No se puede continuar", "La fecha final es menor a la fecha inicial");
					}
				}else if(sel_tab_grupo_ocupacional.isVisible()){
					if(sel_tab_grupo_ocupacional.getSeleccionados()!=null && !sel_tab_grupo_ocupacional.getSeleccionados().isEmpty()){
						//System.out.println(""+sel_tab_cargo_funcional.getSeleccionados());
						p_parametros.put("ide_gegro",sel_tab_grupo_ocupacional.getSeleccionados());
						sel_tab_cargo_funcional.getTab_seleccion().setSql("select CAF.IDE_GECAF,CAF.DETALLE_GECAF from GEN_CARGO_FUNCIONAL CAF " +
								"LEFT JOIN GEN_GRUPO_CARGO GCAF ON GCAF.IDE_GECAF=CAF.IDE_GECAF " +
								"WHERE GCAF.IDE_GEGRO in ("+sel_tab_grupo_ocupacional.getSeleccionados()+") " +
								"GROUP BY CAF.IDE_GECAF,CAF.DETALLE_GECAF " +
								"ORDER BY CAF.DETALLE_GECAF ASC");
						sel_tab_cargo_funcional.getTab_seleccion().ejecutarSql();
						sel_tab_cargo_funcional.getBot_aceptar().setMetodo("aceptarReporte");
						sel_tab_grupo_ocupacional.cerrar();
						sel_tab_cargo_funcional.dibujar();
						System.out.println("valor de ide_gecaf"+sel_tab_cargo_funcional.getValorSeleccionado());
					}else{
						utilitario.agregarMensajeInfo("No se puede continuar", "Debe selccionar al menos un Cargo Funcional");
					}					
				}else if(sel_tab_cargo_funcional.isVisible()){
					if(sel_tab_cargo_funcional.getSeleccionados()!=null && !sel_tab_cargo_funcional.getSeleccionados().isEmpty()){
						//System.out.println(""+sel_tab_grupo_ocupacional.getSeleccionados());
						p_parametros.put("ide_gecaf",sel_tab_cargo_funcional.getSeleccionados());

						p_parametros.put("titulo","EVALUACION X GRUPO");
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
						sel_tab_cargo_funcional.cerrar();
						System.out.println("valor de ide_gegro"+sel_tab_grupo_ocupacional.getSeleccionados());
						sef_reporte.dibujar();		

					}else{
						utilitario.agregarMensajeInfo("No se puede continuar", "Debe selccionar al menos un Cargo Funcional");
					}					
				}

			}





	}




}


