/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asistencia;

import java.util.HashMap;
import java.util.Map;

import org.primefaces.event.SelectEvent;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Consulta;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.ListaSeleccion;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;


public class pre_asi_vacacion extends Pantalla {

	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_tabla2 = new Tabla();
	private Tabla tab_tabla3 = new Tabla();	
	private AutoCompletar aut_empleado=new AutoCompletar();
	private Consulta con_ver_vacaciones = new Consulta();
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	private	ListaSeleccion lis_activo=new ListaSeleccion();
	private SeleccionTabla set_empleado=new SeleccionTabla();
	private Consulta con_ver_ficha_vacacion = new Consulta();
	
	public pre_asi_vacacion() {        
		
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

		Boton bot_ver_vacaciones=new Boton();
		
		bot_ver_vacaciones.setValue("VER VACACIONES");
		bot_ver_vacaciones.setMetodo("verVacaciones");
				
		con_ver_vacaciones.setId("con_ver_vacaciones");
		con_ver_vacaciones.setTitle("VACACIONES DEL EMPLEADO");
		con_ver_vacaciones.setWidth("70%");
		con_ver_vacaciones.setHeight("50%");
		con_ver_vacaciones.setConsulta("SELECT IDE_ASVAC,DIA_ACUMULADO,NRO_DIAS_ADICIONAL,DIA_DESCONTADO,DIA_SOLICITADO, " +
				"DIA_ACUMULADO+NRO_DIAS_ADICIONAL as NRO_TOTALES_VACACIONES, (DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_PENDIENTES " +
				"FROM ( " +
				"SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, " +
				"(case when SUM(DIA_ADICIONAL_ASDEV) is null then 0 else SUM(DIA_ADICIONAL_ASDEV) end) as NRO_DIAS_ADICIONAL, " +
				"(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO, " +
				"(case when SUM(DIA_SOLICITADO_ASDEV) is null then 0 else SUM(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO " +
				"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = TRUE GROUP BY IDE_ASVAC " +
				") a where IDE_ASVAC=-1","IDE_ASVAC");
		con_ver_vacaciones.getTab_consulta_dialogo().setTipoFormulario(true);
		con_ver_vacaciones.getTab_consulta_dialogo().getGrid().setColumns(4);
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("IDE_ASVAC").setNombreVisual("CODIGO");
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_ACUMULADO").setNombreVisual("DIAS ACUMULADOS");
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("NRO_DIAS_ADICIONAL").setNombreVisual("NRO DIAS ADICIONAL");
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_DESCONTADO").setNombreVisual("DIAS DESCONTADOS");
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_SOLICITADO").setNombreVisual("DIAS SOLICITADOS");
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("NRO_TOTALES_VACACIONES").setNombreVisual("NRO TOTAL VACACIONES");
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIAS_PENDIENTES").setNombreVisual("DIAS PENDIENTES");
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("IDE_ASVAC").setEtiqueta();
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_ACUMULADO").setEtiqueta();
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("NRO_DIAS_ADICIONAL").setEtiqueta();
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_DESCONTADO").setEtiqueta();
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_SOLICITADO").setEtiqueta();
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("NRO_TOTALES_VACACIONES").setEtiqueta();
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIAS_PENDIENTES").setEtiqueta();      
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_ACUMULADO").setEstilo("font-size: 17px;font-weight: bold");
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("NRO_DIAS_ADICIONAL").setEstilo("font-size: 17px;font-weight: bold");
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_DESCONTADO").setEstilo("font-size: 17px;font-weight: bold");
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIA_SOLICITADO").setEstilo("font-size: 17px;font-weight: bold");
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("NRO_TOTALES_VACACIONES").setEstilo("font-size: 17px;font-weight: bold");
        con_ver_vacaciones.getTab_consulta_dialogo().getColumna("DIAS_PENDIENTES").setEstilo("font-size: 17px;font-weight: bold");
        con_ver_vacaciones.getTab_consulta_dialogo().setMostrarNumeroRegistros(false);        
        con_ver_vacaciones.getBot_aceptar().setRendered(false);
                
		
		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTabla("ASI_VACACION", "IDE_ASVAC", 1);
		tab_tabla1.getColumna("ACTIVO_ASVAC").setCheck();
		tab_tabla1.getColumna("ACTIVO_ASVAC").setValorDefecto("TRUE");
		tab_tabla1.getColumna("IDE_GTEMP").setVisible(false);
		tab_tabla1.agregarRelacion(tab_tabla2);
		tab_tabla1.setCondicion("IDE_ASVAC=-1");
		tab_tabla1.setLectura(true);
		tab_tabla1.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setMensajeWarn("VACACION");
		pat_panel1.setPanelTabla(tab_tabla1);

		Division div_division = new Division();		
		tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setTabla("ASI_DETALLE_VACACION", "IDE_ASDEV", 2);
		tab_tabla2.getColumna("ACTIVO_ASDEV").setCheck();
		tab_tabla2.getColumna("ACTIVO_ASDEV").setValorDefecto("TRUE");
		tab_tabla2.getColumna("ANULADO_ASDEV").setCheck();
		tab_tabla2.getColumna("IDE_ASESV").setCombo("ASI_ESTADO_VACACION", "IDE_ASESV", "DETALLE_ASESV", "");
		tab_tabla2.setLectura(true);		
		tab_tabla2.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setMensajeWarn("DETALLE VACACIÓN");
		pat_panel2.setPanelTabla(tab_tabla2);
						
		ItemMenu itm_vacaciones=new ItemMenu();
		itm_vacaciones.setValue("Ver Ficha");	
		itm_vacaciones.setIcon("ui-icon-note");
		itm_vacaciones.setMetodo("verFichaVacacion");
		pat_panel2.getMenuTabla().getChildren().add(itm_vacaciones);
		
		tab_tabla3.setId("tab_tabla3");
		tab_tabla3.setSql("SELECT " +
				"(cast(PERIODO as integer) -1) ||' - '|| periodo AS PERIODO,DIA_ACUMULADO,NRO_DIAS_ADICIONAL,DIA_DESCONTADO,DIA_SOLICITADO, " +
				"DIA_ACUMULADO+NRO_DIAS_ADICIONAL as NRO_TOTALES_VACACIONES, (DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_PENDIENTES " +
				" FROM ( " +
				"SELECT IDE_ASVAC,cast (PERIODO as integer) -1 AS ANTERIOR,periodo,SUM(DIA_ACUMULADO) AS DIA_ACUMULADO,SUM(NRO_DIAS_ADICIONAL) AS NRO_DIAS_ADICIONAL, " +
				"SUM(DIA_DESCONTADO) AS DIA_DESCONTADO,SUM(DIA_SOLICITADO) AS DIA_SOLICITADO " +
				"FROM ( " +
				"SELECT IDE_ASVAC, " +
				"TO_CHAR(FECHA_NOVEDAD_ASDEV,'yyyy')AS periodo, " +
				"(case when DIA_ACUMULADO_ASDEV is null then 0 else DIA_ACUMULADO_ASDEV end)AS DIA_ACUMULADO, " +
				"(case when DIA_ADICIONAL_ASDEV is null then 0 else DIA_ADICIONAL_ASDEV end) as NRO_DIAS_ADICIONAL, " +
				"(case when DIA_DESCONTADO_ASDEV is null then 0 else DIA_DESCONTADO_ASDEV end)AS DIA_DESCONTADO, " +
				"(case when DIA_SOLICITADO_ASDEV is null then 0 else DIA_SOLICITADO_ASDEV end)AS DIA_SOLICITADO " +
				"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = TRUE AND IDE_ASVAC=-1 " +
				")a GROUP BY a.periodo,a.IDE_ASVAC " +
				")b ORDER BY PERIODO DESC");
		tab_tabla3.getColumna("DIA_ACUMULADO").setNombreVisual("DIAS ACUMULADOS");
		tab_tabla3.getColumna("NRO_DIAS_ADICIONAL").setNombreVisual("NRO DIAS ADICIONAL");
		tab_tabla3.getColumna("DIA_DESCONTADO").setNombreVisual("DIAS DESCONTADOS");
		tab_tabla3.getColumna("DIA_SOLICITADO").setNombreVisual("DIAS SOLICITADOS");
		tab_tabla3.getColumna("NRO_TOTALES_VACACIONES").setNombreVisual("NRO TOTAL VACACIONES");
		tab_tabla3.getColumna("DIAS_PENDIENTES").setNombreVisual("DIAS PENDIENTES");		
		tab_tabla3.getColumna("DIA_ACUMULADO").alinearDerecha();
		tab_tabla3.getColumna("NRO_DIAS_ADICIONAL").alinearDerecha();
		tab_tabla3.getColumna("DIA_DESCONTADO").alinearDerecha();
		tab_tabla3.getColumna("DIA_SOLICITADO").alinearDerecha();
		tab_tabla3.getColumna("NRO_TOTALES_VACACIONES").alinearDerecha();
		tab_tabla3.getColumna("DIAS_PENDIENTES").alinearDerecha();
		tab_tabla3.setLectura(true);	
		tab_tabla3.dibujar();
		con_ver_vacaciones.getGri_cuerpo().setFooter(tab_tabla3);
		   		
		div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");
		agregarComponente(div_division);
		bar_botones.agregarComponente(new Etiqueta("Empleado"));
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);
		bar_botones.agregarBoton(bot_ver_vacaciones);
		agregarComponente(con_ver_vacaciones);
				
		
		aut_empleado.setId("aut_empleado");
		aut_empleado.setAutoCompletar("select IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP,APELLIDO_PATERNO_GTEMP,APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP from GTH_EMPLEADO");
		aut_empleado.setMetodoChange("filtrarEmpleado");	
		set_empleado.setId("set_empleado");
		set_empleado.setSeleccionTabla("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
				"APELLIDO_PATERNO_GTEMP || ' ' || " +
				"APELLIDO_MATERNO_GTEMP || ' ' || " +
				"PRIMER_NOMBRE_GTEMP || ' ' || " +
				"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
				"from GTH_EMPLEADO ", "IDE_GTEMP");
		set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
		set_empleado.setTitle("SELECCION DE EMPLEADOS");
		agregarComponente(set_empleado);
		
		con_ver_ficha_vacacion.setId("con_ver_ficha_vacacion");
		con_ver_ficha_vacacion.setTitle("FICHA DE VACACIONES");
		con_ver_ficha_vacacion.setWidth("70%");
		con_ver_ficha_vacacion.setHeight("65%");		
		con_ver_ficha_vacacion.setConsulta("SELECT " +
				"a.ide_aspvh, " +
				"h.jefe_inmediato, " +
				"j.jefe_talento_humano, " +
				"l.gerente_area, " +
				"b.detalle_asmot, " +
				"a.fecha_solicitud_aspvh,a.fecha_desde_aspvh,a.fecha_hasta_aspvh,a.detalle_aspvh,a.nro_dias_aspvh, " +
				"d.detalle_gemes,c.detalle_geani,a.nro_documento_aspvh,a.aprobado_aspvh,a.activo_aspvh " +
				"FROM ASI_PERMISOS_VACACION_HEXT a " +
				"LEFT JOIN( " +
				"SELECT ide_asmot,detalle_asmot FROM asi_motivo " +
				")b ON a.ide_asmot=b.ide_asmot " +
				"LEFT JOIN( " +
				"SELECT ide_geani,detalle_geani FROM gen_anio " +
				")c ON c.ide_geani=a.ide_geani " +
				"LEFT JOIN( " +
				"SELECT ide_gemes,detalle_gemes FROM gen_mes " +
				")d ON d.ide_gemes=a.ide_gemes " +
				"INNER JOIN( " +
				"SELECT ide_geedp,ide_gtemp FROM gen_empleados_departamento_par " +
				")g ON g.ide_geedp=a.gen_ide_geedp " +
				"LEFT JOIN( " +
				"SELECT ide_gtemp,apellido_paterno_gtemp ||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as jefe_inmediato FROM gth_empleado " +
				")h ON h.ide_gtemp=g.ide_gtemp " +
				"INNER JOIN( " +
				"SELECT ide_geedp,ide_gtemp FROM gen_empleados_departamento_par " +
				")i ON i.ide_geedp=a.gen_ide_geedp2 " +
				"LEFT JOIN( " +
				"SELECT ide_gtemp,apellido_paterno_gtemp ||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as jefe_talento_humano FROM gth_empleado " +
				")j ON j.ide_gtemp=i.ide_gtemp " +
				"INNER JOIN( " +
				"SELECT ide_geedp,ide_gtemp FROM gen_empleados_departamento_par " +
				")k ON k.ide_geedp=a.gen_ide_geedp3 " +
				"LEFT JOIN( " +
				"SELECT ide_gtemp,apellido_paterno_gtemp ||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as gerente_area FROM gth_empleado " +
				")l ON l.ide_gtemp=k.ide_gtemp " +
				"WHERE a.ide_aspvh in(SELECT ide_aspvh FROM asi_detalle_vacacion where ide_asdev=-1)", "ide_aspvh");
		
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("ide_aspvh").setNombreVisual("CODIGO");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("jefe_inmediato").setNombreVisual("JEFE INMEDIATO");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("jefe_talento_humano").setNombreVisual("JEFE DE TALENTO HUMANO");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("gerente_area").setNombreVisual("GERENTE DE AREA");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("detalle_asmot").setNombreVisual("MOTIVO");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("fecha_solicitud_aspvh").setNombreVisual("FECHA SOLICITUD");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("fecha_desde_aspvh").setNombreVisual("FECHA DESDE");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("fecha_hasta_aspvh").setNombreVisual("FECHA HASTA");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("detalle_aspvh").setNombreVisual("DETALLE");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("nro_dias_aspvh").setNombreVisual("NRO DIAS");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("detalle_gemes").setNombreVisual("MES");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("detalle_geani").setNombreVisual("AÑO");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("nro_documento_aspvh").setNombreVisual("NRO DOCUMENTO");		
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("aprobado_aspvh").setNombreVisual("APROBADO");
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("activo_aspvh").setNombreVisual("ACTIVO");			
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("aprobado_aspvh").setCheck();
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getColumna("activo_aspvh").setCheck();
		con_ver_ficha_vacacion.getBot_aceptar().setRendered(false);
		con_ver_ficha_vacacion.getTab_consulta_dialogo().setTipoFormulario(true);
		con_ver_ficha_vacacion.getTab_consulta_dialogo().getGrid().setColumns(4);
		con_ver_ficha_vacacion.getTab_consulta_dialogo().setMostrarNumeroRegistros(false);
		agregarComponente(con_ver_ficha_vacacion);
		
	}
	
	public void filtrarEmpleado(SelectEvent evt){		
			aut_empleado.onSelect(evt);
			tab_tabla1.setCondicion("IDE_GTEMP="+aut_empleado.getValor());
			tab_tabla1.ejecutarSql();
			tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
		
	}
	public void limpiar() {
		tab_tabla1.limpiar();
		tab_tabla2.limpiar();
		aut_empleado.limpiar();
		utilitario.addUpdate("aut_empleado");// limpia y refresca el autocompletar
	}
	
	public void verVacaciones(){
		if(aut_empleado.getValor()!=null && !aut_empleado.getValor().isEmpty()){
			if(tab_tabla1.getTotalFilas()>0){		
					con_ver_vacaciones.getTab_consulta_dialogo().setSql("SELECT IDE_ASVAC,DIA_ACUMULADO,NRO_DIAS_ADICIONAL,DIA_DESCONTADO,DIA_SOLICITADO, " +
							"DIA_ACUMULADO+NRO_DIAS_ADICIONAL as NRO_TOTALES_VACACIONES, (DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_PENDIENTES " +
							"FROM ( " +
							"SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, " +
							"(case when SUM(DIA_ADICIONAL_ASDEV) is null then 0 else SUM(DIA_ADICIONAL_ASDEV) end) as NRO_DIAS_ADICIONAL, " +
							"(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO, " +
							"(case when SUM(DIA_SOLICITADO_ASDEV) is null then 0 else SUM(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO " +
							"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = TRUE GROUP BY IDE_ASVAC " +
							") a where IDE_ASVAC="+tab_tabla1.getValorSeleccionado());
					con_ver_vacaciones.getTab_consulta_dialogo().ejecutarSql();
					tab_tabla3.setSql("SELECT " +
							"periodo AS PERIODO,DIA_ACUMULADO,NRO_DIAS_ADICIONAL,DIA_DESCONTADO,DIA_SOLICITADO, " +
							"DIA_ACUMULADO+NRO_DIAS_ADICIONAL as NRO_TOTALES_VACACIONES, (DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_PENDIENTES " +
							" FROM ( " +
							"SELECT IDE_ASVAC,cast (PERIODO as integer) -1 AS ANTERIOR,periodo,SUM(DIA_ACUMULADO) AS DIA_ACUMULADO,SUM(NRO_DIAS_ADICIONAL) AS NRO_DIAS_ADICIONAL, " +
							"SUM(DIA_DESCONTADO) AS DIA_DESCONTADO,SUM(DIA_SOLICITADO) AS DIA_SOLICITADO " +
							"FROM ( " +
							"SELECT IDE_ASVAC, " +
							"TO_CHAR(FECHA_NOVEDAD_ASDEV,'yyyy')AS periodo, " +
							"(case when DIA_ACUMULADO_ASDEV is null then 0 else DIA_ACUMULADO_ASDEV end)AS DIA_ACUMULADO, " +
							"(case when DIA_ADICIONAL_ASDEV is null then 0 else DIA_ADICIONAL_ASDEV end) as NRO_DIAS_ADICIONAL, " +
							"(case when DIA_DESCONTADO_ASDEV is null then 0 else DIA_DESCONTADO_ASDEV end)AS DIA_DESCONTADO, " +
							"(case when DIA_SOLICITADO_ASDEV is null then 0 else DIA_SOLICITADO_ASDEV end)AS DIA_SOLICITADO " +
							"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = TRUE AND IDE_ASVAC="+tab_tabla1.getValorSeleccionado()+
							")a GROUP BY a.periodo,a.IDE_ASVAC " +
							")b ORDER BY PERIODO DESC");
																
					tab_tabla3.ejecutarSql();					
					con_ver_vacaciones.dibujar();
			}else{
				utilitario.agregarMensajeInfo("No se puede visualizar los registros", "El Empleado no posee registros ");
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede visualizar los registros", "Debe seleccionar un Empleado");
		}		
	}
	
	public void verFichaVacacion(){		
		if(tab_tabla2.getValor("ide_aspvh")!=null && !tab_tabla2.getValor("ide_aspvh").isEmpty()){
			con_ver_ficha_vacacion.getTab_consulta_dialogo().setSql("SELECT " +
					"a.ide_aspvh, " +
					"h.jefe_inmediato, " +
					"j.jefe_talento_humano, " +
					"l.gerente_area, " +
					"b.detalle_asmot, " +
					"a.fecha_solicitud_aspvh,a.fecha_desde_aspvh,a.fecha_hasta_aspvh,a.detalle_aspvh,a.nro_dias_aspvh, " +
					"d.detalle_gemes,c.detalle_geani,a.nro_documento_aspvh,a.aprobado_aspvh,a.activo_aspvh " +
					"FROM ASI_PERMISOS_VACACION_HEXT a " +
					"LEFT JOIN( " +
					"SELECT ide_asmot,detalle_asmot FROM asi_motivo " +
					")b ON a.ide_asmot=b.ide_asmot " +
					"LEFT JOIN( " +
					"SELECT ide_geani,detalle_geani FROM gen_anio " +
					")c ON c.ide_geani=a.ide_geani " +
					"LEFT JOIN( " +
					"SELECT ide_gemes,detalle_gemes FROM gen_mes " +
					")d ON d.ide_gemes=a.ide_gemes " +
					"INNER JOIN( " +
					"SELECT ide_geedp,ide_gtemp FROM gen_empleados_departamento_par " +
					")g ON g.ide_geedp=a.gen_ide_geedp " +
					"LEFT JOIN( " +
					"SELECT ide_gtemp,apellido_paterno_gtemp ||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as jefe_inmediato FROM gth_empleado " +
					")h ON h.ide_gtemp=g.ide_gtemp " +
					"INNER JOIN( " +
					"SELECT ide_geedp,ide_gtemp FROM gen_empleados_departamento_par " +
					")i ON i.ide_geedp=a.gen_ide_geedp2 " +
					"LEFT JOIN( " +
					"SELECT ide_gtemp,apellido_paterno_gtemp ||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as jefe_talento_humano FROM gth_empleado " +
					")j ON j.ide_gtemp=i.ide_gtemp " +
					"INNER JOIN( " +
					"SELECT ide_geedp,ide_gtemp FROM gen_empleados_departamento_par " +
					")k ON k.ide_geedp=a.gen_ide_geedp3 " +
					"LEFT JOIN( " +
					"SELECT ide_gtemp,apellido_paterno_gtemp ||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as gerente_area FROM gth_empleado " +
					")l ON l.ide_gtemp=k.ide_gtemp " +
					"WHERE a.ide_aspvh in(SELECT ide_aspvh FROM asi_detalle_vacacion where ide_asdev="+tab_tabla2.getValorSeleccionado()+")"); 
			con_ver_ficha_vacacion.getTab_consulta_dialogo().ejecutarSql();	
			con_ver_ficha_vacacion.dibujar();
		}else{
			utilitario.agregarMensajeInfo("No se puede mostrar información", "No posee vacación");
		}
		
	}
	

	@Override
	public void insertar() {
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		tab_tabla1.guardar();
		tab_tabla2.guardar();
		utilitario.getConexion().guardarPantalla();
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
 if (rep_reporte.getReporteSelecionado().equals("Detalle Vacaciones Saldo")){

			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();		
								rep_reporte.cerrar();
								set_empleado.getTab_seleccion().setSql("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
										"APELLIDO_PATERNO_GTEMP || ' ' || " +
										"APELLIDO_MATERNO_GTEMP || ' ' || " +
										"PRIMER_NOMBRE_GTEMP || ' ' || " +
										"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
										"from GTH_EMPLEADO "+
										"ORDER BY NOMBRES ASC");

								set_empleado.getTab_seleccion().ejecutarSql();
								set_empleado.getBot_aceptar().setMetodo("aceptarReporte");

						set_empleado.dibujar();
				}else if(set_empleado.isVisible()){
				System.out.println(""+set_empleado.getSeleccionados());
				
				p_parametros.put("IDE_GTEMP",set_empleado.getSeleccionados());
				p_parametros.put("titulo", "GERENCIA ADMINISTRATIVA DEPARTAMENTO DE TALENTO HUMANO SALDO VACACIONES");
				System.out.println("path "+rep_reporte.getPath());
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
				set_empleado.cerrar();
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

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}

	public Consulta getCon_ver_vacaciones() {
		return con_ver_vacaciones;
	}

	public void setCon_ver_vacaciones(Consulta con_ver_vacaciones) {
		this.con_ver_vacaciones = con_ver_vacaciones;
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

	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}
	
	public Tabla getTab_tabla3() {
		return tab_tabla3;
	}

	public void setTab_tabla3(Tabla tab_tabla3) {
		this.tab_tabla3 = tab_tabla3;
	}
	
	public Consulta getCon_ver_ficha_vacacion() {
		return con_ver_ficha_vacacion;
	}

	public void setCon_ver_ficha_vacacion(Consulta con_ver_ficha_vacacion) {
		this.con_ver_ficha_vacacion = con_ver_ficha_vacacion;
	}		
}
