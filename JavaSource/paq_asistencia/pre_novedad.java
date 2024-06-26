/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asistencia;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import org.primefaces.component.dialog.Dialog;

import paq_asistencia.ejb.ServicioAsistencia;
import paq_asistencia.ejb.ServicioControl;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
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

/**
 * @author HP-USER
 *
 */
public class pre_novedad extends Pantalla {

	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_tabla2 = new Tabla();	
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	private SeleccionCalendario sel_cal = new SeleccionCalendario();
	private SeleccionTabla set_empleado_novedad=new SeleccionTabla();

	private SeleccionCalendario sec_importar=new SeleccionCalendario();

	
	@EJB
	private ServicioControl ser_control = (ServicioControl) utilitario.instanciarEJB(ServicioControl.class);
	@EJB
	private ServicioAsistencia ser_asistencia = (ServicioAsistencia) utilitario.instanciarEJB(ServicioAsistencia.class);
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	
	private Dialogo dia_justificacion=new Dialogo();
	private Tabla tab_asi_novedad_just=new Tabla();
	private Grid gri_justificacion=new Grid();
	

	
	public pre_novedad() {
		

		tab_asi_novedad_just.setId("tab_asi_novedad_just");
		tab_asi_novedad_just.setTabla("ASI_NOVEDAD_JUSTIFICACION", "IDE_ASNOJ", 3);
		tab_asi_novedad_just.getColumna("IDE_ASNOD").setVisible(false);
		tab_asi_novedad_just.getColumna("FECHA_ASNOJ").setValorDefecto(utilitario.getFechaActual());
		tab_asi_novedad_just.getColumna("ARCHIVO_ASNOJ").setUpload("justificaciones");
		tab_asi_novedad_just.getColumna("ACTIVO_ASNOJ").setLectura(true);
		tab_asi_novedad_just.getColumna("ACTIVO_ASNOJ").setCheck();
		tab_asi_novedad_just.getColumna("ACTIVO_ASNOJ").setValorDefecto("true");
		tab_asi_novedad_just.setCondicion("IDE_ASNOJ=-1");
		tab_asi_novedad_just.setTipoFormulario(false);
		tab_asi_novedad_just.setMostrarNumeroRegistros(false);
		tab_asi_novedad_just.dibujar();
		
		
		PanelTabla pat_tab_j=new PanelTabla();
		pat_tab_j.setPanelTabla(tab_asi_novedad_just);
		
		gri_justificacion=new Grid();
		dia_justificacion.setId("dia_justificacion");
		gri_justificacion.getChildren().add(pat_tab_j);
		
		gri_justificacion.setStyle("width:" + (dia_justificacion.getAnchoPanel() - 5) + "px; height:" + dia_justificacion.getAltoPanel() + "px;overflow:auto;display:block;");
		dia_justificacion.setDialogo(gri_justificacion);
		dia_justificacion.getBot_aceptar().setMetodo("aceptarDatosJustificacion");
		
		agregarComponente(dia_justificacion);
		
		bar_botones.quitarBotonsNavegacion();
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);

		Boton bot_filtrar = new Boton();
		bot_filtrar.setValue("Actualizar");
		bot_filtrar.setMetodo("aceptarRangoFechas");
		bot_filtrar.setIcon("ui-icon-refresh");
		bar_botones.agregarBoton(bot_filtrar);
		
		bar_botones.agregarSeparador();
		
		Boton bot_importar = new Boton();
		bot_importar.setValue("Importar Novedades Asistencia");
		bot_importar.setMetodo("importar");
		bot_importar.setIcon("ui-icon-clock");
		bar_botones.agregarBoton(bot_importar);
		
//		Boton bot_exportar = new Boton();
//		bot_exportar.setValue("Exportar Novedades Asistencia");
//		bot_exportar.setMetodo("exportarAsistencia");
//		bot_exportar.setIcon("ui-icon-clock");
//		bar_botones.agregarBoton(bot_exportar);

		Boton bot_justificar = new Boton();
		bot_justificar.setValue("Justificar");
		bot_justificar.setMetodo("justificarAsistencia");
		bot_justificar.setIcon("ui-icon-clock");
		bar_botones.agregarBoton(bot_justificar);

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
		bar_botones.agregarReporte();
		
		sel_cal.setId("sel_cal");
		sel_cal.setMultiple(true);
		sel_cal.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_cal);
		
		set_empleado_novedad.setId("set_empleado_novedad");
		set_empleado_novedad.setSeleccionTabla("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP AS DOCUMENTO, " +
				"APELLIDO_PATERNO_GTEMP || ' ' || " +
				"APELLIDO_MATERNO_GTEMP || ' ' || " +
				"PRIMER_NOMBRE_GTEMP || ' ' || " +
				"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
				"from GTH_EMPLEADO ", "IDE_GTEMP");
		set_empleado_novedad.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
		set_empleado_novedad.getTab_seleccion().getColumna("DOCUMENTO").setFiltro(true);
		set_empleado_novedad.getBot_aceptar().setMetodo("aceptarReporte");
		set_empleado_novedad.setTitle("SELECCION DE EMPLEADOS");
		agregarComponente(set_empleado_novedad);

		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTabla("ASI_NOVEDAD", "IDE_ASNOV", 1);
		tab_tabla1.agregarRelacion(tab_tabla2);
		tab_tabla1.getColumna("FECHA_INICIO_ASNOV").setValorDefecto(utilitario.getFechaActual());
		tab_tabla1.getColumna("FECHA_FIN_ASNOV").setValorDefecto(utilitario.getFechaActual());
		tab_tabla1.getColumna("ACTIVO_ASNOV").setCheck();
		tab_tabla1.getColumna("IDE_USUA").setValorDefecto(utilitario.getVariable("IDE_USUA"));
		tab_tabla1.getColumna("IDE_USUA").setCombo("SIS_USUARIO","IDE_USUA","NOM_USUA","");
		tab_tabla1.getColumna("IDE_USUA").setAutoCompletar();
		tab_tabla1.getColumna("IDE_USUA").setLectura(true);
		tab_tabla1.setCondicion("FECHA_INICIO_ASNOV BETWEEN "+utilitario.getFormatoFechaSQL(cal_fecha_inicial.getFecha())+" AND "+utilitario.getFormatoFechaSQL(cal_fecha_final.getFecha() ));
		tab_tabla1.setRows(5);
		tab_tabla1.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla1);

		tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setTabla("ASI_NOVEDAD_DETALLE", "IDE_ASNOD", 2);

		tab_tabla2.getColumna("IDE_ASMOT").setCombo("ASI_MOTIVO","IDE_ASMOT","DETALLE_ASMOT","ACTIVO_ASMOT=true");
		tab_tabla2.getColumna("IDE_GTEMP").setCombo("GTH_EMPLEADO","IDE_GTEMP","DOCUMENTO_IDENTIDAD_GTEMP,APELLIDO_PATERNO_GTEMP || APELLIDO_MATERNO_GTEMP || PRIMER_NOMBRE_GTEMP || SEGUNDO_NOMBRE_GTEMP","ACTIVO_GTEMP=true");
		tab_tabla2.getColumna("IDE_GTEMP").setAutoCompletar();
		tab_tabla2.getColumna("ACTIVO_ASNOD").setCheck();
		tab_tabla2.getColumna("NOMINA_ASNOD").setCheck();
		tab_tabla2.getColumna("APROBADO_ASNOD").setCheck();
		tab_tabla2.getColumna("VACACIONES_ASNOD").setCheck();
		
		tab_tabla2.getColumna("IDE_GTEMP").setLectura(true);
		tab_tabla2.getColumna("IDE_GTEMP").setFiltroContenido();
		tab_tabla2.getColumna("NRO_HORAS_ASNOD").setLectura(true);
		tab_tabla2.getColumna("NRO_HORAS_APROBADO_ASNOD").setLectura(true);
		tab_tabla2.setRows(10);
		tab_tabla2.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_tabla2);
		
		
		ItemMenu itm_ver_justificacion=new ItemMenu();
		itm_ver_justificacion.setValue("Ver Justificacion");
		itm_ver_justificacion.setIcon("ui-icon-contact");
		itm_ver_justificacion.setMetodo("verJustificacion");

		pat_panel2.getMenuTabla().getChildren().add(itm_ver_justificacion);

		
		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel1, pat_panel2, "30%", "H");
		agregarComponente(div_division);
		
		
		
		sec_importar.setId("sec_importar");
		sec_importar.setTitle("IMPORTAR RESUMEN ASISTENCIA");		
		sec_importar.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sec_importar.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sec_importar.getBot_aceptar().setMetodo("aceptarImportar");
	    agregarComponente(sec_importar);
	}

	public void verJustificacion(){
		
		
		if (ser_asistencia.getNovedadJustificacion("IDE_ASNOD", tab_tabla2.getValorSeleccionado()).getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No existe justificacion", "");
			return;
		}
		
		tab_asi_novedad_just.setCondicion("IDE_ASNOJ="+ser_asistencia.getNovedadJustificacion("IDE_ASNOD", tab_tabla2.getValorSeleccionado()).getValor("IDE_ASNOJ"));
		tab_asi_novedad_just.ejecutarSql();
		
		
		dia_justificacion.setTitle("JUSTIFICACION DE NOVEDAD: "+tab_tabla2.getValorArreglo(tab_tabla2.getFilaActual(),"IDE_GTEMP", 1)+" "+tab_tabla2.getValorArreglo(tab_tabla2.getFilaActual(),"IDE_GTEMP", 2));
		dia_justificacion.setDialogo(gri_justificacion);
		dia_justificacion.getBot_aceptar().setRendered(false);
		dia_justificacion.dibujar();
		utilitario.addUpdate("dia_justificacion");
	}
	
	public Dialogo getDia_justificacion() {
		return dia_justificacion;
	}


	public void setDia_justificacion(Dialogo dia_justificacion) {
		this.dia_justificacion = dia_justificacion;
	}


	public Tabla getTab_asi_novedad_just() {
		return tab_asi_novedad_just;
	}


	public void setTab_asi_novedad_just(Tabla tab_asi_novedad_just) {
		this.tab_asi_novedad_just = tab_asi_novedad_just;
	}


	public void aceptarDatosJustificacion(){
		
		if (tab_asi_novedad_just.guardar()){
			tab_tabla2.guardar();
			guardarPantalla();
			dia_justificacion.cerrar();
		}
	}
	
	public void justificarAsistencia(){
		if (tab_tabla2.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede justificar","No existe novedades por justificar ");
			return;
		}
		
		tab_asi_novedad_just.setCondicion("IDE_ASNOJ="+ser_asistencia.getNovedadJustificacion("IDE_ASNOD", tab_tabla2.getValorSeleccionado()).getValor("IDE_ASNOJ"));		
		tab_asi_novedad_just.ejecutarSql();
		tab_asi_novedad_just.insertar();
		dia_justificacion.setTitle("JUSTIFICACION DE NOVEDAD: "+tab_tabla2.getValorArreglo(tab_tabla2.getFilaActual(),"IDE_GTEMP", 1)+" "+tab_tabla2.getValorArreglo(tab_tabla2.getFilaActual(),"IDE_GTEMP", 2));
		dia_justificacion.dibujar();

		utilitario.addUpdate("dia_justificacion");

		
		
	}

	public void aceptarImportar(){
		if(sec_importar.isFechasValidas()){
		String str_msj=	ser_control.importarNovedadesMarcacion(sec_importar.getFecha1String(), sec_importar.getFecha2String());

		if(str_msj.isEmpty()){
			utilitario.agregarMensajeInfo("No se encontraron registros en el rango de fechas seleccionadas", "");
		}
		else if(str_msj.startsWith("Se")){
			utilitario.agregarMensaje("Se guardo Correctamente", str_msj);
		}
		else{
			utilitario.agregarMensajeError("No se pudo Guardar", str_msj);	
		}
			sec_importar.cerrar();	
			tab_tabla1.ejecutarSql();
			tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
			
		}
		else{
			utilitario.agregarMensajeInfo("Rango de fechas no válidas", "");
		}
	}
	
	// exportar marcaciones
	public void exportarAsistencia(){
		System.out.println("Exportando marcaciones");
		utilitario.getConexion().agregarSqlPantalla(" update nrh_detalle_rol"
+" set valor_nrdro = ("
+" select sum (nro_horas_aprobado_asnod) as horas "
+" from ("
+" select a.ide_asnov,fecha_inicio_asnov,fecha_fin_asnov,b.ide_gtemp,b.ide_asmot,nro_horas_aprobado_asnod,aprobado_asnod,"
+" nomina_asnod,aplica_vacaciones_asmot,d.ide_nrder,ide_nrdro,e.ide_geedp"
+" from asi_novedad a, asi_novedad_detalle b,asi_motivo c,nrh_detalle_rubro d,gen_empleados_departamento_par e,nrh_rol f,"
+" nrh_detalle_rol g"
+" where a.ide_asnov = b.ide_asnov"
+" and b.ide_asmot = c.ide_asmot"
+" and c.ide_nrrub = d.ide_nrrub"
+" and b.ide_gtemp= e.ide_gtemp "
+" and f.ide_nrrol = g.ide_nrrol"
+" and e.ide_geedp = g.ide_geedp"
+" and d.ide_nrder = g.ide_nrder"
+" and ide_nresr =2  and nro_horas_aprobado_asnod >0"
+" and fecha_inicio_asnov between to_date('"+sec_importar.getFecha1String()+"','yyyy-mm-dd') and to_date('"+sec_importar.getFecha2String()+"','yyyy-mm-dd')"
+" and aprobado_asnod=true and nomina_asnod=false"
+" ) a group by ide_nrdro,ide_geedp"
+" )"
+" where ide_nrdro in ("
+" select ide_nrdro "
+" from ("
+" select a.ide_asnov,fecha_inicio_asnov,fecha_fin_asnov,b.ide_gtemp,b.ide_asmot,nro_horas_aprobado_asnod,aprobado_asnod,"
+" nomina_asnod,aplica_vacaciones_asmot,d.ide_nrder,ide_nrdro,e.ide_geedp"
+" from asi_novedad a, asi_novedad_detalle b,asi_motivo c,nrh_detalle_rubro d,gen_empleados_departamento_par e,nrh_rol f,"
+" nrh_detalle_rol g"
+" where a.ide_asnov = b.ide_asnov"
+" and b.ide_asmot = c.ide_asmot"
+" and c.ide_nrrub = d.ide_nrrub"
+" and b.ide_gtemp= e.ide_gtemp "
+" and f.ide_nrrol = g.ide_nrrol"
+" and e.ide_geedp = g.ide_geedp"
+" and d.ide_nrder = g.ide_nrder"
+" and ide_nresr =2  and nro_horas_aprobado_asnod >0"
+" and fecha_inicio_asnov between to_date('"+sec_importar.getFecha1String()+"','yyyy-mm-dd') and to_date('"+sec_importar.getFecha2String()+"','yyyy-mm-dd')"
+" and aprobado_asnod=true and nomina_asnod=false"
+" ) a group by ide_nrdro,ide_geedp ); ");
		utilitario.getConexion().agregarSqlPantalla(" update asi_novedad_detalle" +
				" set nomina_asnod =true" +
				" where ide_asnod  in (" +
				" select ide_asnod " +
				" from (" +
				" select a.ide_asnov,fecha_inicio_asnov,fecha_fin_asnov,b.ide_gtemp,b.ide_asmot,nro_horas_aprobado_asnod,aprobado_asnod," +
				" nomina_asnod,aplica_vacaciones_asmot,d.ide_nrder,ide_nrdro,e.ide_geedp" +
				" from asi_novedad a, asi_novedad_detalle b,asi_motivo c,nrh_detalle_rubro d,gen_empleados_departamento_par e,nrh_rol f," +
				" nrh_detalle_rol g" +
				" where a.ide_asnov = b.ide_asnov" +
				" and b.ide_asmot = c.ide_asmot" +
				" and c.ide_nrrub = d.ide_nrrub" +
				" and b.ide_gtemp= e.ide_gtemp" +
				" and f.ide_nrrol = g.ide_nrrol" +
				" and e.ide_geedp = g.ide_geedp" +
				" and d.ide_nrder = g.ide_nrder" +
				" and ide_nresr =2  and nro_horas_aprobado_asnod >0" +
				" and fecha_inicio_asnov between to_date('"+sec_importar.getFecha1String()+"','yyyy-mm-dd') and to_date('"+sec_importar.getFecha2String()+"','yyyy-mm-dd')" +
				" and aprobado_asnod=true and nomina_asnod=false" +
				" ) a group by ide_asnod ); " );
						guardarPantalla();
			tab_tabla1.ejecutarSql();
		
	}

	/**
	 * Importa novedades en un rango de fechas de resumen de asistencia
	 */
	public void importar(){
		sec_importar.dibujar();
	}
	
	


	public void aceptarRangoFechas(){
		
		tab_tabla1.setCondicion("FECHA_INICIO_ASNOV BETWEEN "+utilitario.getFormatoFechaSQL(cal_fecha_inicial.getFecha())+" AND "+utilitario.getFormatoFechaSQL(cal_fecha_final.getFecha() ));
		tab_tabla1.ejecutarSql();
		tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
	
	}

	@Override
	public void insertar() {
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		tab_tabla1.guardar();
		tab_tabla2.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		utilitario.getTablaisFocus().eliminar();
	}
	
	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
	rep_reporte.dibujar();
	}


	@Override
	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Detalle De Novedades")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();				
				rep_reporte.cerrar();	
				sel_cal.setFecha1(null);
				sel_cal.setFecha2(null);
				sel_cal.dibujar();				
			} else if (sel_cal.isVisible()) {
				if(sel_cal.isFechasValidas()){
					p_parametros.put("FECHA_INICIO",sel_cal.getFecha1String());
					p_parametros.put("FECHA_FIN",sel_cal.getFecha2String());
					System.out.println("fecha inicio 1:"+sel_cal.getFecha1String());
					System.out.println("fecha fin 2:"+sel_cal.getFecha2String());
					set_empleado_novedad.getTab_seleccion().setSql("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP AS DOCUMENTO, " +
							"APELLIDO_PATERNO_GTEMP || ' ' || " +
							"APELLIDO_MATERNO_GTEMP || ' ' || " +
							"PRIMER_NOMBRE_GTEMP || ' ' || " +
							"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
							"from GTH_EMPLEADO "+
							"ORDER BY NOMBRES ASC");	
					set_empleado_novedad.getTab_seleccion().ejecutarSql();
					set_empleado_novedad.getBot_aceptar().setMetodo("aceptarReporte");
					sel_cal.cerrar();
					set_empleado_novedad.dibujar();
				}else{
					utilitario.agregarMensajeInfo("Fechas no Validas", "La fecha final debe ser mayor a la fecha inicial");
				}
			}
			else if(set_empleado_novedad.isVisible()){
				System.out.println(""+set_empleado_novedad.getSeleccionados());				
				p_parametros.put("IDE_GTEMP",set_empleado_novedad.getSeleccionados());
				p_parametros.put("titulo", "DETALLE DE NOVEDADES");
				System.out.println("path "+rep_reporte.getPath());
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
				set_empleado_novedad.cerrar();
				sef_reporte.dibujar();
			}
		}	
	}


	public Tabla getTab_tabla1() {
		return tab_tabla1;
	}

	public void setTab_tabla1(Tabla tab_tabla1) {
		this.tab_tabla1 = tab_tabla1;
	}

	public Tabla getTab_tabla2() {
		return tab_tabla2;
	}

	public void setTab_tabla2(Tabla tab_tabla2) {
		this.tab_tabla2 = tab_tabla2;
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

	public SeleccionCalendario getSel_cal() {
		return sel_cal;
	}

	public void setSel_cal(SeleccionCalendario sel_cal) {
		this.sel_cal = sel_cal;
	}

	public SeleccionTabla getSet_empleado_novedad() {
		return set_empleado_novedad;
	}

	public void setSet_empleado_novedad(SeleccionTabla set_empleado_novedad) {
		this.set_empleado_novedad = set_empleado_novedad;
	}
	
}
