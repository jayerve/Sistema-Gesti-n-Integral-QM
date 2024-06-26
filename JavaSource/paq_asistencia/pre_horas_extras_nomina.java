/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asistencia;


import javax.ejb.EJB;

import jxl.biff.formula.FormulaErrorCode;

import org.primefaces.component.editor.Editor;
import org.primefaces.event.SelectEvent;

import paq_asistencia.ejb.ServicioControl;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.MarcaAgua;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.correo.EnviarCorreo;

/**
 *
 * @author DELL-USER
 */
public class pre_horas_extras_nomina extends Pantalla {


	private Tabla tab_tabla = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();

	private SeleccionCalendario sec_importar=new SeleccionCalendario();

	@EJB
	private ServicioControl ser_control = (ServicioControl) utilitario.instanciarEJB(ServicioControl.class);

	private AutoCompletar aut_empleados=new AutoCompletar();
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);

	private SeleccionTabla sel_tab_periodo = new SeleccionTabla();
	private SeleccionTabla sel_tab_tipo_nomina = new SeleccionTabla();
	private SeleccionTabla sel_tab_empleados = new SeleccionTabla();
	private Dialogo dia_exportar_horas_nomina=new Dialogo();
	private Editor edi_mensajes=new Editor();
	private Confirmar con_guardar=new Confirmar();
	private Dialogo dia_resultado_exportacion=new Dialogo();
	private Editor edi_mensajes_resultado=new Editor();
	
	
	public pre_horas_extras_nomina() {    
		
		bar_botones.quitarBotonsNavegacion();

		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);

		Boton bot_filtrar = new Boton();
		bot_filtrar.setValue("Actualizar");
		bot_filtrar.setMetodo("actualizarMarcaciones");
		bot_filtrar.setIcon("ui-icon-refresh");
		bar_botones.agregarBoton(bot_filtrar);

		bar_botones.agregarSeparador();

		aut_empleados.setId("aut_empleados");
		String str_sql_emp=ser_gestion.getSqlEmpleadosAutocompletar();
		aut_empleados.setAutoCompletar(str_sql_emp);
		aut_empleados.setMetodoChange("seleccionoEmpleado");

		bar_botones.agregarComponente(aut_empleados);

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);

		bar_botones.agregarSeparador();		

		Boton bot_exportar = new Boton();
		bot_exportar.setValue("Exportar Horas Extras a Nomina");
		bot_exportar.setMetodo("exportarHorasExtrasNomnia");
		bot_exportar.setIcon("ui-icon-clock");
		bar_botones.agregarBoton(bot_exportar);

		tab_tabla.setId("tab_tabla");	
		tab_tabla.setSql("SELECT ide_asdhe,d.apellido_paterno_gtemp || ' ' || d.apellido_materno_gtemp || ' ' || d.primer_nombre_gtemp || ' ' || d.segundo_nombre_gtemp as empleado, " +
				"TO_CHAR(a.fecha_asdhe,'YYYY-MM-DD') as fecha_asdhe,a.nro_horas_aprobadas_asdhe,c.detalle_asmot,a.hora_inicial_asdhe,a.hora_final_asdhe,a.actividades_asdhe " +
				"FROM asi_detalle_horas_extras a, asi_permisos_vacacion_hext b, asi_motivo c,gth_empleado d " +
				"where a.ide_aspvh=b.ide_aspvh and c.ide_asmot=a.ide_asmot " +
				"and d.ide_gtemp=b.ide_gtemp and b.ide_gtemp=-1  order by   a.fecha_asdhe,empleado");		

		tab_tabla.getColumna("fecha_asdhe").setValorDefecto(utilitario.getFechaActual());
		tab_tabla.getColumna("fecha_asdhe").setLectura(true);
		tab_tabla.getColumna("hora_inicial_asdhe").setControl("Hora");
		tab_tabla.getColumna("hora_inicial_asdhe").setTipoJava("java.sql.Time");
		tab_tabla.getColumna("hora_inicial_asdhe").setLectura(true);
		tab_tabla.getColumna("hora_final_asdhe").setControl("Hora");
		tab_tabla.getColumna("hora_final_asdhe").setTipoJava("java.sql.Time");
		tab_tabla.getColumna("hora_final_asdhe").setLectura(true);
		tab_tabla.getColumna("empleado").setLectura(true);
		tab_tabla.getColumna("actividades_asdhe").setLectura(true);
		tab_tabla.getColumna("nro_horas_aprobadas_asdhe").setLectura(true);
		tab_tabla.getColumna("detalle_asmot").setLectura(true);
		tab_tabla.setColumnaSuma("nro_horas_aprobadas_asdhe");
		tab_tabla.setEmptyMessage("No se encontraron marcaciones en el rango de fechas seleccionado");
		tab_tabla.setRows(20);		
		tab_tabla.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);
		pat_panel.getMenuTabla().getItem_insertar().setRendered(false);
		pat_panel.getMenuTabla().getItem_guardar().setRendered(false);
		pat_panel.getMenuTabla().getItem_eliminar().setRendered(false);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir1(tab_tabla);
		agregarComponente(div_division);

		sec_importar.setId("sec_importar");
		agregarComponente(sec_importar);
		
		
		//periodo del rol
		sel_tab_periodo.setId("sel_tab_periodo");
		sel_tab_periodo.setSeleccionTabla("SELECT PER.IDE_GEPRO,TIR.DETALLE_NRTIT,ani.detalle_geani,mes.detalle_gemes, " +
				"PER.FECHA_INICIAL_GEPRO,PER.FECHA_FINAL_GEPRO " +
				"FROM GEN_PERIDO_ROL PER " +
				"INNER JOIN NRH_TIPO_ROL TIR ON TIR.IDE_NRTIT=PER.IDE_NRTIT " +
				"INNER JOIN GEN_MES MES ON MES.IDE_GEMES=PER.IDE_GEMES " +
				"INNER JOIN GEN_ANIO ANI ON ANI.IDE_GEANI=PER.IDE_GEANI WHERE ACTIVO_GEPRO=true and PER.IDE_GEPRO=-1 " +
				"ORDER BY mes.IDE_GEMES ASC", "IDE_GEPRO");		
		sel_tab_periodo.getTab_seleccion().getColumna("detalle_geani").setFiltro(true);
		sel_tab_periodo.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
		gru_pantalla.getChildren().add(sel_tab_periodo);
		sel_tab_periodo.setRadio();
		sel_tab_periodo.getBot_aceptar().setMetodo("aceptarPeriodoRol");
		sel_tab_periodo.setDynamic(false);
		agregarComponente(sel_tab_periodo);

		//tipo de nomina
		sel_tab_tipo_nomina.setId("sel_tab_tipo_nomina");
		sel_tab_tipo_nomina.setSeleccionTabla("select " +
				"ROL.IDE_NRROL,TIN.DETALLE_NRTIN, " +
				"TEM.DETALLE_GTTEM," +
				"TIC.DETALLE_GTTCO, " +
				"SUC.NOM_SUCU " +
				"from NRH_ROL ROL " +
				"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
				"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
				"LEFT JOIN GTH_TIPO_CONTRATO TIC ON TIC.IDE_GTTCO=DTN.IDE_GTTCO " +
				"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
				"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
				"where ROL.IDE_GEPRO=-1","IDE_NRROL");
		gru_pantalla.getChildren().add(sel_tab_tipo_nomina);
		sel_tab_tipo_nomina.getBot_aceptar().setMetodo("aceptarTipoNomina");
		sel_tab_tipo_nomina.setDynamic(false);
		agregarComponente(sel_tab_tipo_nomina);
		
		
		//empleados nomina
		
		sel_tab_empleados.setId("sel_tab_empleados");		
		sel_tab_empleados.setSeleccionTabla("SELECT  " +
				"EDP.IDE_GEEDP, " +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP ||' '|| " +
				"EMP.APELLIDO_MATERNO_GTEMP ||' '|| " +
				"EMP.PRIMER_NOMBRE_GTEMP ||' '|| " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES, " +
				"EDP.IDE_SUCU, " +
				"EDP.IDE_GTEMP, " +
				"EDP.IDE_GTTEM " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP " +
				"INNER JOIN NRH_DETALLE_ROL DROL ON DROL.IDE_GEEDP=EDP.IDE_GEEDP  AND DROL.IDE_NRROL in (-1) " +
				"INNER JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EDP.IDE_GTEMP " +
				"GROUP BY EDP.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP , " +
				"EMP.APELLIDO_MATERNO_GTEMP , " +
				"EMP.PRIMER_NOMBRE_GTEMP , " +
				"EMP.SEGUNDO_NOMBRE_GTEMP,EDP.IDE_SUCU,EDP.IDE_GTEMP, " +
				"EDP.IDE_GTTEM  " +
				"ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC","IDE_GEEDP");
		sel_tab_empleados.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
		sel_tab_empleados.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		gru_pantalla.getChildren().add(sel_tab_empleados);
		sel_tab_empleados.getBot_aceptar().setMetodo("aceptarEmpleados");
		sel_tab_empleados.setDynamic(false);
		agregarComponente(sel_tab_empleados);
		
		///dialogo de exportar a nomina las horas extras
		
		dia_exportar_horas_nomina.setId("dia_exportar_horas_nomina");
		dia_exportar_horas_nomina.setTitle("EXPORTAR HORAS EXTRAS A NOMINA");
		dia_exportar_horas_nomina.setWidth("80%");
		dia_exportar_horas_nomina.setHeight("50%");
		dia_exportar_horas_nomina.getBot_aceptar().setRendered(false);
		agregarComponente(dia_exportar_horas_nomina);
		
		Grid gri_matriz=new Grid();
		gri_matriz.setColumns(1);
		
		edi_mensajes.setControls("");
		edi_mensajes.setId("edi_mensajes");
		edi_mensajes.setStyle("overflow:auto;");		
		edi_mensajes.setWidth(dia_exportar_horas_nomina.getAnchoPanel() - 15 );
		edi_mensajes.setDisabled(true);				
		gri_matriz.setFooter(edi_mensajes);
		
		dia_exportar_horas_nomina.setDialogo(gri_matriz);
		
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);

		
		///dialogo de resultado de exportar a nomina 
		
		dia_resultado_exportacion.setId("dia_resultado_exportacion");
		dia_resultado_exportacion.setTitle("RESULTADO DE EXPORTAR HORAS EXTRAS A NOMINA");
		dia_resultado_exportacion.setWidth("80%");
		dia_resultado_exportacion.setHeight("50%");
		dia_resultado_exportacion.getBot_aceptar().setRendered(false);
		agregarComponente(dia_resultado_exportacion);
		
		Grid gri_matriz_resultado=new Grid();
		gri_matriz_resultado.setColumns(1);
		
		edi_mensajes_resultado.setControls("");
		edi_mensajes_resultado.setId("edi_mensajes_resultado");
		edi_mensajes_resultado.setStyle("overflow:auto;");		
		edi_mensajes_resultado.setWidth(dia_resultado_exportacion.getAnchoPanel() - 15 );
		edi_mensajes_resultado.setDisabled(true);				
		gri_matriz_resultado.setFooter(edi_mensajes_resultado);
		
		dia_resultado_exportacion.setDialogo(gri_matriz_resultado);

		
	}

//	public void aceptarEliminar(){
//		//		String str_elimino=ser_control.eliminarResumen(sec_importar.getFecha1String(), sec_importar.getFecha2String());
//		//		if(str_elimino.isEmpty()){
//		//			utilitario.agregarMensaje("Se Elimino Correctamente", "");
//		//			tab_tabla.ejecutarSql();	
//		//		}
//		//		sec_importar.cerrar();
//
//	}

	public void aceptarImportar(){
		//		int int_total=ser_control.resumirAsistencia(sec_importar.getFecha1String(), sec_importar.getFecha2String());		
		//		sec_importar.cerrar();
		//		if(int_total>0){
		//			utilitario.agregarMensaje("Se Guardo Correctamente", "Se importaron "+int_total+" registros");
		//			tab_tabla.ejecutarSql();	
		//		}
		//		else{
		//			utilitario.agregarMensajeInfo("No se encontraron marcaciones en el rango de fechas seleccionado", "");
		//		}
	}

	public void exportarHorasExtrasNomnia(){
		if(tab_tabla.getTotalFilas()>0){			
			sel_tab_periodo.getTab_seleccion().setSql("SELECT PER.IDE_GEPRO,TIR.DETALLE_NRTIT,ani.detalle_geani,mes.detalle_gemes, " +
					"PER.FECHA_INICIAL_GEPRO,PER.FECHA_FINAL_GEPRO " +
					"FROM GEN_PERIDO_ROL PER " +
					"INNER JOIN NRH_TIPO_ROL TIR ON TIR.IDE_NRTIT=PER.IDE_NRTIT " +
					"INNER JOIN GEN_MES MES ON MES.IDE_GEMES=PER.IDE_GEMES " +
					"INNER JOIN GEN_ANIO ANI ON ANI.IDE_GEANI=PER.IDE_GEANI WHERE ACTIVO_GEPRO=true " +
					"ORDER BY mes.IDE_GEMES ASC");
			sel_tab_periodo.getTab_seleccion().ejecutarSql();
			sel_tab_periodo.dibujar();
		}else{
			utilitario.agregarMensajeInfo("No se Puede Exportar", "Debe tener Registros en la pantalla");
		}
	}

	public void aceptarPeriodoRol(){
		if(sel_tab_periodo.getValorSeleccionado()!=null && !sel_tab_periodo.getValorSeleccionado().isEmpty()){
			sel_tab_periodo.cerrar();
			sel_tab_tipo_nomina.getTab_seleccion().setSql("select " +
					"ROL.IDE_NRROL,TIN.DETALLE_NRTIN, " +
					"TEM.DETALLE_GTTEM," +
					"TIC.DETALLE_GTTCO, " +
					"SUC.NOM_SUCU " +
					"from NRH_ROL ROL " +
					"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
					"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
					"LEFT JOIN GTH_TIPO_CONTRATO TIC ON TIC.IDE_GTTCO=DTN.IDE_GTTCO " +
					"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
					"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
					"where ROL.IDE_GEPRO ="+sel_tab_periodo.getValorSeleccionado() +" and ROL.ide_nresr=2");
			sel_tab_tipo_nomina.getTab_seleccion().ejecutarSql();
			sel_tab_tipo_nomina.dibujar();			
		}else{
			utilitario.agregarMensajeInfo("No se Puede Exportar", "Debe Seleccionar un Periodo");
		}	
	}
	
	public void aceptarTipoNomina(){
		if(sel_tab_tipo_nomina.getSeleccionados()!=null && !sel_tab_tipo_nomina.getSeleccionados().isEmpty()){
			sel_tab_empleados.getTab_seleccion().setSql("SELECT  " +
					"EDP.IDE_GEEDP, " +
					"EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
					"EMP.APELLIDO_PATERNO_GTEMP ||' '|| " +
					"EMP.APELLIDO_MATERNO_GTEMP ||' '|| " +
					"EMP.PRIMER_NOMBRE_GTEMP ||' '|| " +
					"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES, " +
					"EDP.IDE_SUCU, " +
					"EDP.IDE_GTEMP, " +
					"EDP.IDE_GTTEM " +
					"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP " +
					"INNER JOIN NRH_DETALLE_ROL DROL ON DROL.IDE_GEEDP=EDP.IDE_GEEDP  AND DROL.IDE_NRROL in ("+sel_tab_tipo_nomina.getSeleccionados()+") " +
					"INNER JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EDP.IDE_GTEMP " +
					"GROUP BY EDP.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP , " +
					"EMP.APELLIDO_MATERNO_GTEMP , " +
					"EMP.PRIMER_NOMBRE_GTEMP , " +
					"EMP.SEGUNDO_NOMBRE_GTEMP,EDP.IDE_SUCU,EDP.IDE_GTEMP, " +
					"EDP.IDE_GTTEM " +
					"ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC");
			sel_tab_empleados.getTab_seleccion().ejecutarSql();
			sel_tab_empleados.dibujar();						
			sel_tab_tipo_nomina.cerrar();			
		}else{
			utilitario.agregarMensajeInfo("No se Puede Exportar", "Debe Seleccionar al menos un Tipo de Nomina");
		}
	}
	
	public void aceptarEmpleados(){
		StringBuilder str_horas_extras_nomina=new StringBuilder();
		StringBuilder str_resultado=new StringBuilder();
		if(sel_tab_empleados.getSeleccionados()!=null && !sel_tab_empleados.getSeleccionados().isEmpty()){			
			dia_exportar_horas_nomina.cerrar();			
			TablaGenerica tab_exportar_horas_nomina=utilitario.consultar("select a.ide_asdhe,a.empleado,a.fecha_asdhe,a.nro_horas_aprobadas_asdhe,a.detalle_asmot,a.detalle_nrrub, " +
					"a.hora_inicial_asdhe,a.hora_final_asdhe,a.actividades_asdhe, " +
					"b.ide_asdhe as ide_asdhe_b,b.empleado as empleado_b,b.fecha_asdhe as fecha_asdhe_b,b.nro_horas_aprobadas_asdhe as nro_horas_aprobadas_asdhe_b, " +
					"b.hora_inicial_asdhe as hora_inicial_asdhe_b,b.hora_final_asdhe as hora_final_asdhe_b,b.actividades_asdhe as actividades_asdhe_b " +
					"from ( " +
					"SELECT ide_asdhe,d.apellido_paterno_gtemp || ' ' || d.apellido_materno_gtemp || ' ' || d.primer_nombre_gtemp || ' ' || d.segundo_nombre_gtemp as empleado, " +
					"TO_CHAR(a.fecha_asdhe,'YYYY-MM-DD') as fecha_asdhe,a.nro_horas_aprobadas_asdhe,c.detalle_asmot,e.detalle_nrrub,a.hora_inicial_asdhe,a.hora_final_asdhe,a.actividades_asdhe " +
					"FROM asi_detalle_horas_extras a, asi_permisos_vacacion_hext b, asi_motivo c,gth_empleado d,nrh_rubro e " +
					"where a.ide_aspvh=b.ide_aspvh and c.ide_asmot=a.ide_asmot and b.ide_gtemp = d.ide_gtemp and e.ide_nrrub=c.ide_nrrub " +
					"and b.ide_geedp in ("+sel_tab_empleados.getSeleccionados()+")  order by   a.fecha_asdhe,empleado " +
					")a " +
					"left join ( " +
					"SELECT ide_asdhe,d.apellido_paterno_gtemp || ' ' || d.apellido_materno_gtemp || ' ' || d.primer_nombre_gtemp || ' ' || d.segundo_nombre_gtemp as empleado, " +
					"TO_CHAR(a.fecha_asdhe,'YYYY-MM-DD') as fecha_asdhe,a.nro_horas_aprobadas_asdhe,c.detalle_asmot,a.hora_inicial_asdhe,a.hora_final_asdhe,a.actividades_asdhe, " +
					"c.ide_nrrub,b.ide_geedp " +
					"FROM asi_detalle_horas_extras a, asi_permisos_vacacion_hext b, asi_motivo c,gth_empleado d, gen_empleados_departamento_par e,nrh_detalle_rol f, " +
					"(select ide_nrder, ide_nrrub from nrh_detalle_rubro) g " +
					"where a.ide_aspvh=b.ide_aspvh and c.ide_asmot=a.ide_asmot " +
					"and d.ide_gtemp=b.ide_gtemp " +
					"and e.ide_geedp=b.ide_geedp " +
					"and f.ide_geedp=e.ide_geedp " +
					"and f.ide_nrder = g.ide_nrder " +
					"and c.ide_nrrub = g.ide_nrrub " +
					"and f.ide_nrrol in ("+sel_tab_tipo_nomina.getSeleccionados()+") " +
					"and f.ide_geedp in ("+sel_tab_empleados.getSeleccionados()+") " +
					"order by   a.fecha_asdhe,empleado " +
					")b on b.ide_asdhe=a.ide_asdhe " +
					"where b.ide_asdhe is null");			
			
			if(tab_exportar_horas_nomina.getTotalFilas()>0){
				for (int i = 0; i < tab_exportar_horas_nomina.getTotalFilas(); i++) {
					str_horas_extras_nomina.append(getFormatoError("El empleado : "+tab_exportar_horas_nomina.getValor(i, "empleado")+" A la fecha: "+tab_exportar_horas_nomina.getValor(i, "fecha_asdhe")+" con el numero de horas extras aprobadas: "+tab_exportar_horas_nomina.getValor(i, "nro_horas_aprobadas_asdhe")+" al rubro: "+tab_exportar_horas_nomina.getValor(i, "detalle_nrrub")+" No tiene configurado horas extras en su respectiva nomina" ));
				}
				str_resultado.append(str_horas_extras_nomina);
				edi_mensajes.setValue("NOVEDADES AL EXPORTAR HORAS EXTRAS "+str_resultado);
				dia_exportar_horas_nomina.dibujar();	
			}else{		
				con_guardar.setMessage("Esta Seguro de Subir las Horas Extras de los Empleados Seleccionados entre las Fechas "+cal_fecha_inicial.getFecha()+" al "+cal_fecha_final.getFecha());
				con_guardar.setTitle("CONFIRMACION PARA SUBIR HORAS EXTRAS");
				con_guardar.getBot_aceptar().setMetodo("aceptarSubirHorasExtras");
				con_guardar.dibujar();				
				utilitario.addUpdate("con_guardar");
			}			
			
		}else{
			utilitario.agregarMensajeInfo("No se Puede Exportar", "Debe Seleccionar al menos un Empleado");
		}
	}
	private String getFormatoError(String mensaje){
		return "<div><font color='#ff0000'><strong>*&nbsp;</strong>"+mensaje+"</font></div>";	
	}
	
	private String getFormatoCorrecto(String mensaje){
		return "<div><font color='#00CC00'><strong>*&nbsp;</strong>"+mensaje+"</font></div>";	
	}
	
	public void aceptarSubirHorasExtras(){	
		StringBuilder str_exportar_horas_extras_nomina=new StringBuilder();
		StringBuilder str_resultado_nomina=new StringBuilder();		
		TablaGenerica tab_subir_horas_extras=utilitario.consultar("SELECT ide_asdhe,d.apellido_paterno_gtemp || ' ' || d.apellido_materno_gtemp || ' ' || d.primer_nombre_gtemp || ' ' || d.segundo_nombre_gtemp as empleado, " +
				"TO_CHAR(a.fecha_asdhe,'YYYY-MM-DD') as fecha_asdhe,a.nro_horas_aprobadas_asdhe,c.detalle_asmot,a.hora_inicial_asdhe,a.hora_final_asdhe,a.actividades_asdhe, " +
				"c.ide_nrrub,b.ide_geedp,f.ide_nrrol,detalle_nrrub " +
				"FROM asi_detalle_horas_extras a, asi_permisos_vacacion_hext b, asi_motivo c,gth_empleado d, gen_empleados_departamento_par e,nrh_detalle_rol f, " +
				"(select ide_nrder, a.ide_nrrub,detalle_nrrub from nrh_detalle_rubro a , nrh_rubro b where a.ide_nrrub=b.ide_nrrub) g " +
				"where a.ide_aspvh=b.ide_aspvh and c.ide_asmot=a.ide_asmot " +
				"and d.ide_gtemp=b.ide_gtemp " +
				"and e.ide_geedp=b.ide_geedp " +
				"and f.ide_geedp=e.ide_geedp " +
				"and f.ide_nrder = g.ide_nrder " +
				"and c.ide_nrrub = g.ide_nrrub " +
				"and f.ide_nrrol in ("+sel_tab_tipo_nomina.getSeleccionados()+") " +
				"and f.ide_geedp in ("+sel_tab_empleados.getSeleccionados()+") " +
				"and fecha_asdhe between to_date('"+cal_fecha_inicial.getFecha()+"','yyyy-mm-dd') and to_date('"+cal_fecha_final.getFecha()+"','yyyy-mm-dd') " +
				"order by   a.fecha_asdhe,empleado");
		if (tab_subir_horas_extras.getTotalFilas()>0) {
			Tabla tab_valida_nomina=new Tabla();
			tab_valida_nomina.setTabla("ASI_VALIDA_NOMINA", "IDE_ASVNO", -1);
			tab_valida_nomina.setCondicion("IDE_ASVNO=-1");
			tab_valida_nomina.ejecutarSql();
			for (int i = 0; i < tab_subir_horas_extras.getTotalFilas(); i++) {
				tab_valida_nomina.insertar();
				tab_valida_nomina.setValor("IDE_ASDHE", tab_subir_horas_extras.getValor(i,"IDE_ASDHE"));
				tab_valida_nomina.setValor("IDE_NRROL", tab_subir_horas_extras.getValor(i,"IDE_NRROL"));
				tab_valida_nomina.setValor("IDE_GEEDP", tab_subir_horas_extras.getValor(i,"IDE_GEEDP"));
				tab_valida_nomina.setValor("FECHA_ACTUAL_ASVNO", utilitario.getFechaActual());
			}
			tab_valida_nomina.guardar();
			
			Tabla tab_horas_extras_nomina=utilitario.consultarTabla("select ide_nrdro,b.val from ( " +
					"select a.ide_nrdro,a.ide_nrrol,a.ide_nrder,a.ide_geedp, " +
					"sum(a.nro_horas_aprobadas_asdhe) as val " +
					"from ( " +
					"SELECT ide_asdhe,d.apellido_paterno_gtemp || ' ' || d.apellido_materno_gtemp || ' ' || d.primer_nombre_gtemp || ' ' || d.segundo_nombre_gtemp as empleado, " +
					"TO_CHAR(a.fecha_asdhe,'YYYY-MM-DD') as fecha_asdhe,a.nro_horas_aprobadas_asdhe,c.detalle_asmot,a.hora_inicial_asdhe,a.hora_final_asdhe,a.actividades_asdhe, " +
					"c.ide_nrrub,b.ide_geedp,f.ide_nrrol,f.ide_nrdro,g.ide_nrder " +
					"FROM asi_detalle_horas_extras a, asi_permisos_vacacion_hext b, asi_motivo c,gth_empleado d, gen_empleados_departamento_par e,nrh_detalle_rol f, " +
					"(select ide_nrder, ide_nrrub from nrh_detalle_rubro) g " +
					"where a.ide_aspvh=b.ide_aspvh and c.ide_asmot=a.ide_asmot " +
					"and d.ide_gtemp=b.ide_gtemp " +
					"and e.ide_geedp=b.ide_geedp " +
					"and f.ide_geedp=e.ide_geedp " +
					"and f.ide_nrder = g.ide_nrder " +
					"and c.ide_nrrub = g.ide_nrrub " +
					"and f.ide_nrrol in ("+sel_tab_tipo_nomina.getSeleccionados()+") " +
					"and f.ide_geedp in ("+sel_tab_empleados.getSeleccionados()+") " +
					"and fecha_asdhe between to_date('"+cal_fecha_inicial.getFecha()+"','yyyy-mm-dd') and to_date('"+cal_fecha_final.getFecha()+"','yyyy-mm-dd') " +
					"order by   a.fecha_asdhe,empleado " +
					")a group by a.ide_nrdro,a.ide_nrrol,a.ide_nrder,a.ide_geedp " +
					")b ");
			if(tab_horas_extras_nomina.getTotalFilas()>0){
				for (int i = 0; i < tab_horas_extras_nomina.getTotalFilas(); i++) {
					utilitario.getConexion().agregarSqlPantalla("update nrh_detalle_rol set valor_nrdro="+tab_horas_extras_nomina.getValor(i, "val")+" where ide_nrdro="+tab_horas_extras_nomina.getValor(i, "ide_nrdro"));
				}
			}			
			utilitario.getConexion().agregarSql("update NRH_ROL set ESTADO_CALCULADO_NRROL=0 where IDE_NRROL in ("+sel_tab_tipo_nomina.getSeleccionados()+")");
			guardarPantalla();
			con_guardar.cerrar();
			sel_tab_empleados.cerrar();
			if(tab_subir_horas_extras.getTotalFilas()>0){
				for (int i = 0; i < tab_subir_horas_extras.getTotalFilas(); i++) {
					str_exportar_horas_extras_nomina.append(getFormatoCorrecto("Empleado : "+tab_subir_horas_extras.getValor(i, "empleado")+" A la fecha: "+tab_subir_horas_extras.getValor(i, "fecha_asdhe")+" registrado con el numero de horas extras aprobadas: "+tab_subir_horas_extras.getValor(i, "nro_horas_aprobadas_asdhe")+" al rubro: "+tab_subir_horas_extras.getValor(i, "detalle_nrrub") ));
				}
				str_resultado_nomina.append(str_exportar_horas_extras_nomina);
				edi_mensajes_resultado.setValue("REGISTRO DE HORAS EXTRAS EXPORTADAS"+str_resultado_nomina);
				dia_resultado_exportacion.dibujar();
			}			
		}
	}
	
	
	public void limpiar(){
		aut_empleados.limpiar();
		utilitario.addUpdate("aut_empleados");
		actualizarMarcaciones();
	}

	public void actualizarMarcaciones(){		
		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(), cal_fecha_final.getFecha())) {			
			if(aut_empleados.getValor()!=null){				
				tab_tabla.setSql("SELECT ide_asdhe,d.apellido_paterno_gtemp || ' ' || d.apellido_materno_gtemp || ' ' || d.primer_nombre_gtemp || ' ' || d.segundo_nombre_gtemp as empleado, " +
						"TO_CHAR(a.fecha_asdhe,'YYYY-MM-DD') as fecha_asdhe,a.nro_horas_aprobadas_asdhe,c.detalle_asmot,a.hora_inicial_asdhe,a.hora_final_asdhe,a.actividades_asdhe " +
						"FROM asi_detalle_horas_extras a, asi_permisos_vacacion_hext b, asi_motivo c,gth_empleado d " +
						"where a.ide_aspvh=b.ide_aspvh and c.ide_asmot=a.ide_asmot " +
						"and d.ide_gtemp=b.ide_gtemp and activo_asdhe=true and aprobado_asdhe=TRUE and nomina_asdhe !=TRUE and d.IDE_GTEMP="+aut_empleados.getValor()+" AND TO_CHAR(a.fecha_asdhe,'YYYY-MM-DD') BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'   order by   a.fecha_asdhe,empleado");
			}
			else{				
				tab_tabla.setSql("SELECT ide_asdhe,d.apellido_paterno_gtemp || ' ' || d.apellido_materno_gtemp || ' ' || d.primer_nombre_gtemp || ' ' || d.segundo_nombre_gtemp as empleado, " +
						"TO_CHAR(a.fecha_asdhe,'YYYY-MM-DD') as fecha_asdhe,a.nro_horas_aprobadas_asdhe,c.detalle_asmot,a.hora_inicial_asdhe,a.hora_final_asdhe,a.actividades_asdhe " +
						"FROM asi_detalle_horas_extras a, asi_permisos_vacacion_hext b, asi_motivo c,gth_empleado d " +
						"where a.ide_aspvh=b.ide_aspvh and c.ide_asmot=a.ide_asmot " +
						"and d.ide_gtemp=b.ide_gtemp and activo_asdhe=true and aprobado_asdhe=TRUE and TO_CHAR(a.fecha_asdhe,'YYYY-MM-DD') BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'  and nomina_asdhe !=true order by   a.fecha_asdhe,empleado");
				}
			tab_tabla.ejecutarSql();			
		}
		else {			
			utilitario.agregarMensajeInfo("Rangos de fechas no válidos","");
		}		
	}

	public void seleccionoEmpleado(SelectEvent evt){
		aut_empleados.onSelect(evt);
		actualizarMarcaciones();	
	}
	
	@Override
	public void insertar() {

	}

	@Override
	public void guardar() {

	}

	@Override
	public void eliminar() {

	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

	public SeleccionCalendario getSec_importar() {
		return sec_importar;
	}

	public void setSec_importar(SeleccionCalendario sec_importar) {
		this.sec_importar = sec_importar;
	}

	public AutoCompletar getAut_empleados() {
		return aut_empleados;
	}

	public void setAut_empleados(AutoCompletar aut_empleados) {
		this.aut_empleados = aut_empleados;
	}

	public SeleccionTabla getSel_tab_periodo() {
		return sel_tab_periodo;
	}

	public void setSel_tab_periodo(SeleccionTabla sel_tab_periodo) {
		this.sel_tab_periodo = sel_tab_periodo;
	}

	public SeleccionTabla getSel_tab_tipo_nomina() {
		return sel_tab_tipo_nomina;
	}

	public void setSel_tab_tipo_nomina(SeleccionTabla sel_tab_tipo_nomina) {
		this.sel_tab_tipo_nomina = sel_tab_tipo_nomina;
	}

	public SeleccionTabla getSel_tab_empleados() {
		return sel_tab_empleados;
	}

	public void setSel_tab_empleados(SeleccionTabla sel_tab_empleados) {
		this.sel_tab_empleados = sel_tab_empleados;
	}

	public Dialogo getDia_exportar_horas_nomina() {
		return dia_exportar_horas_nomina;
	}

	public void setDia_exportar_horas_nomina(Dialogo dia_exportar_horas_nomina) {
		this.dia_exportar_horas_nomina = dia_exportar_horas_nomina;
	}

	public Dialogo getDia_resultado_exportacion() {
		return dia_resultado_exportacion;
	}

	public void setDia_resultado_exportacion(Dialogo dia_resultado_exportacion) {
		this.dia_resultado_exportacion = dia_resultado_exportacion;
	}
			
}
