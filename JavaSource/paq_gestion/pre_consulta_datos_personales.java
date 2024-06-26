/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gestion;

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
public class pre_consulta_datos_personales extends Pantalla {


	private Tabla tab_tabla = new Tabla();
	public pre_consulta_datos_personales() {      
		bar_botones.limpiar();
	

		tab_tabla.setId("tab_tabla");
		tab_tabla.setCampoPrimaria("IDE_GTEMP");		
		tab_tabla.setSql("SELECT "
				+ "EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
				+ " EMP.APELLIDO_PATERNO_GTEMP ||'  '|| EMP.APELLIDO_MATERNO_GTEMP ||'  '|| EMP.PRIMER_NOMBRE_GTEMP ||'  '|| EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES, "
				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
				+ "DEPA.DETALLE_GEDEP, "
				+ "DP.IDE_GEDIP,  "
				+ "CASE WHEN DP3.DETALLE_GEDIP  IS NULL THEN DP2.DETALLE_GEDIP ELSE DP3.DETALLE_GEDIP END AS PAIS, "
				+ "CASE WHEN DP3.DETALLE_GEDIP IS NULL THEN DP1.DETALLE_GEDIP ELSE DP2.DETALLE_GEDIP END AS PROVINCIA, "
				+ "CASE WHEN  DP3.DETALLE_GEDIP IS NULL THEN TDP.DETALLE_GEDIP ELSE DP1.DETALLE_GEDIP END AS CANTON,   "
				+ "TDP.DETALLE_GEDIP AS CIUDAD, "
				+ "CASE WHEN DP.DETALLE_GEDIP =TDP.DETALLE_GEDIP THEN null else  DP.DETALLE_GEDIP END AS PARROQUIA,  "
				+ "DIR.DETALLE_GTDIR AS DIRECCION, "
				+ "DIR.REFERENCIA_GTDIR AS REFERENCIA,  "
				+ "CASE WHEN DIR.utililiza_recorrido_gtdir = TRUE THEN 'SI' else 'NO' end AS UTILIZA_RECORRIDO,  "
				+ "EC.DETALLE_GTESC AS ESTADO_CIVIL, "
				+ "CON.IDE_GTCON,CON.DOCUMENTO_IDENTIDAD_GTCON,  "
				+ "CON.APELLIDO_PATERNO_GTCON ||'  '|| CON.APELLIDO_MATERNO_GTCON  "
				+ "||'  '|| CON.PRIMER_NOMBRE_GTCON ||'  '|| CON.SEGUNDO_NOMBRE_GTCON AS NOMBRE_CONYUGE, "
				+ "array_to_string(ARRAY( "
				+ "SELECT numero_telefono_gttel "
				+ "FROM gth_TELEFONO TELF "
				+ "WHERE EPAR.ide_gtemp= TELF.ide_gtemp "
				+ "AND EPAR.ACTIVO_GEEDP=TRUE  "
				+ "),',','*') AS TELEFONOS, "
				+ "split_part(array_to_string( "
				+ "ARRAY( "
				+ "SELECT DETALLE_GTCOR "
				+ "FROM gth_CORREO g  "
				+ "WHERE ACTIVO_GTCOR=TRUE and   "
				+ "EPAR.ide_gtemp= g.ide_gtemp  "
				+ "AND EPAR.ACTIVO_GEEDP=TRUE "
				+ "),',','*'),',',2) AS CORREO_INSTITUCIONAL, "
				+ "split_part(array_to_string( "
				+ "ARRAY( "
				+ "SELECT DETALLE_GTCOR "
				+ "FROM gth_CORREO g "
				+ "WHERE ACTIVO_GTCOR=FALSE and  "
				+ "EPAR.ide_gtemp= g.ide_gtemp  "
				+ "AND EPAR.ACTIVO_GEEDP=TRUE  "
				+ "),',','*'),',',1) AS CORREO_PERSONAL, "
				+ "case when (SELECT count(ide_gtcaf) as cargas  "
				+ "FROM gth_cargas_familiares car "
				+ "where EPAR.ide_gtemp= car.ide_gtemp AND EPAR.ACTIVO_GEEDP=TRUE "
				+ "group by ide_gtemp) is null then 0 else (SELECT count(ide_gtcaf) as cargas "
				+ "FROM gth_cargas_familiares car "
				+ "where EPAR.ide_gtemp= car.ide_gtemp AND EPAR.ACTIVO_GEEDP=TRUE  "
				+ "group by ide_gtemp) end AS CARGAS "
				+ "from GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
				+ "LEFT JOIN GTH_ESTADO_CIVIL EC ON EMP.IDE_GTESC=EC.IDE_GTESC  "
				+ "LEFT JOIN GTH_NACIONALIDAD NAC ON EMP.IDE_GTNAC=NAC.IDE_GTNAC  "
				+ "LEFT JOIN GTH_TIPO_DOCUMENTO_IDENTIDAD TDI ON EMP.IDE_GTTDI=TDI.IDE_GTTDI  "
				+ "LEFT JOIN GTH_DIRECCION DIR ON DIR.IDE_GTEMP=EMP.IDE_GTEMP AND dir.ide_gtdir in (  select max(ide_gtdir) as ide_gtdir from GTH_DIRECCION group by ide_gtemp ) "
				+ "LEFT JOIN gth_conyuge CON  ON CON.IDE_GTEMP=EMP.IDE_GTEMP  "
				+ "LEFT JOIN GEN_DIVISION_POLITICA DP ON DP.IDE_GEDIP=DIR.IDE_GEDIP  "
				+ "LEFT JOIN GEN_DIVISION_POLITICA TDP ON TDP.IDE_GEDIP=DP.GEN_IDE_GEDIP  "
				+ "LEFT JOIN GEN_DIVISION_POLITICA DP1 ON DP1.IDE_GEDIP=TDP.GEN_IDE_GEDIP  "
				+ "LEFT JOIN GEN_DIVISION_POLITICA DP2 ON DP2.IDE_GEDIP=DP1.GEN_IDE_GEDIP  "
				+ "LEFT JOIN GEN_DIVISION_POLITICA DP3 ON DP3.IDE_GEDIP=DP2.GEN_IDE_GEDIP  "
				+ "where EPAR.ACTIVO_GEEDP=TRUE "
				+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP");
		
		tab_tabla.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		tab_tabla.getColumna("NOMBRES").setFiltro(true);
		tab_tabla.getColumna("PARROQUIA").setFiltro(true);
		tab_tabla.getColumna("PROVINCIA").setFiltro(true);
		tab_tabla.getColumna("CANTON").setFiltro(true);
		tab_tabla.getColumna("CIUDAD").setFiltro(true);
		tab_tabla.getColumna("PAIS").setFiltro(true);

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
