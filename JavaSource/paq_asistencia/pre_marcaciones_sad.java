/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asistencia;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_marcaciones_sad extends Pantalla {


	private Tabla tab_tabla = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	public pre_marcaciones_sad() {      
		bar_botones.limpiar();
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);

		Boton bot_filtrar = new Boton();
		bot_filtrar.setValue("Actualizar");
		bot_filtrar.setMetodo("actualizarMarcaciones");
		bot_filtrar.setIcon("ui-icon-refresh");
		bar_botones.agregarBoton(bot_filtrar);

		tab_tabla.setId("tab_tabla");
		tab_tabla.setCampoPrimaria("IDE_GTEMP");		
		tab_tabla.setSql("SELECT distinct EMP.IDE_GTEMP,emp.documento_identidad_gtemp,  "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
				+ "sucu.nom_sucu AS SUCURSAL, AREA.DETALLE_GEARE AS AREA,DEPA.DETALLE_GEDEP AS DEPARTAMENTO,rel.detalle_corel AS LUGAR_MARCACION,con.ide_persona_cobim AS CODIGO_MARCACION,  "
				+ "TO_CHAR(con.FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,"
				+ "TO_CHAR(con.FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM  "
				+ "from CON_BIOMETRICO_MARCACIONES  con  "
				+ "left join GTH_EMPLEADO emp on emp.TARJETA_MARCACION_GTEMP=trim(IDE_PERSONA_COBIM)  "
				+ "left join con_reloj rel on rel.ide_corel=con.ide_corel  "
				+ "left join gen_empleados_departamento_par edp on edp.ide_gtemp=emp.ide_gtemp  "
				+ "left join sis_sucursal sucu on sucu.ide_sucu=edp.ide_sucu   "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EDP.IDE_GEDEP   "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EDP.IDE_GEARE "
				+ "WHERE TO_CHAR(con.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'"
				+ "and  EDP.ACTIVO_GEEDP =TRUE  "
				+ "order by EMP.IDE_GTEMP,NOMBRES_APELLIDOS ASC,FECHAM asc,HORAM asc");
			//,TO_CHAR(con.FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM 
		//HORAM asc
		//tab_tabla.getColumna("LUGAR_MARCACION").setFiltro(true);
		//tab_tabla.getColumna("documento_identidad_gtemp").setFiltro(true);
		//tab_tabla.getColumna("NOMBRES_APELLIDOS").setFiltro(true);
		//tab_tabla.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		//tab_tabla.getColumna("LUGAR_MARCACION").setFiltro(true);
		tab_tabla.setNumeroTabla(1);				
		tab_tabla.setLectura(true);
		tab_tabla.setRows(20);
		tab_tabla.dibujar();		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
	}

	public void actualizarMarcaciones(){
		if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
		//	tab_tabla.setSql("SELECT BIO.IDE_COBIM,BIO.EVENTO_RELOJ_COBIM,IDE_PERSONA_COBIM,(SELECT APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || PRIMER_NOMBRE_GTEMP || ' ' || (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP end) FROM GTH_EMPLEADO WHERE TARJETA_MARCACION_GTEMP=trim(IDE_PERSONA_COBIM)) as NOMBRES,DETALLE_COREL,TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM FROM CON_BIOMETRICO_MARCACIONES BIO INNER JOIN CON_RELOJ RELOJ ON BIO.IDE_COREL=RELOJ.IDE_COREL  WHERE TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'");	
		//	tab_tabla.ejecutarSql();
			tab_tabla.setSql("SELECT distinct EMP.IDE_GTEMP,emp.documento_identidad_gtemp,  "
					+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
					+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
					+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
					+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
					+ "sucu.nom_sucu AS SUCURSAL, AREA.DETALLE_GEARE AS AREA,DEPA.DETALLE_GEDEP AS DEPARTAMENTO,rel.detalle_corel AS LUGAR_MARCACION,con.ide_persona_cobim AS CODIGO_MARCACION,  "
					+ "TO_CHAR(con.FECHA_EVENTO_COBIM,'YYYY-MM-DD') AS FECHAM,  "
					+ "TO_CHAR(con.FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM  "
					+ "from CON_BIOMETRICO_MARCACIONES  con  "
					+ "left join GTH_EMPLEADO emp on emp.TARJETA_MARCACION_GTEMP=trim(IDE_PERSONA_COBIM)  "
					+ "left join con_reloj rel on rel.ide_corel=con.ide_corel  "
					+ "left join gen_empleados_departamento_par edp on edp.ide_gtemp=emp.ide_gtemp  "
					+ "left join sis_sucursal sucu on sucu.ide_sucu=edp.ide_sucu   "
					+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EDP.IDE_GEDEP   "
					+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EDP.IDE_GEARE "
					+ "WHERE TO_CHAR(con.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'"
					+ "and  EDP.ACTIVO_GEEDP =TRUE  "
					+ "order by EMP.IDE_GTEMP,NOMBRES_APELLIDOS ASC,FECHAM  asc,HORAM asc");
			//,HORAM asc
				tab_tabla.ejecutarSql();

		}
		else {
			utilitario.agregarMensajeInfo("Filtros no válidos",
					"Debe ingresar los fitros de rangos de fechas");
		}		
	}

	@Override
	public void insertar() {
		tab_tabla.insertar();
	}

	@Override
	public void guardar() {
		tab_tabla.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		tab_tabla.eliminar();
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}


	
	
}
