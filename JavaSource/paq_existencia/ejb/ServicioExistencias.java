package paq_existencia.ejb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import framework.aplicacion.TablaGenerica;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Utilitario;

@Stateless
public class ServicioExistencias {
	private Utilitario utilitario = new Utilitario();

	public TablaGenerica getTablaGenericaConsultaCustodio(String ide_custodio) {
		TablaGenerica tab_custodio = utilitario
				.consultar("SELECT ide_afcus, ide_afact, ide_geedp, detalle_afcus, fecha_entrega_afcus, fecha_descargo_afcus, numero_acta_afcus, razon_descargo_afcus, cod_barra_afcus, nro_secuencial_afcus, activo_afcus,gen_ide_geedp FROM afi_custodio where ide_afcus in ("
						+ ide_custodio + ") order by ide_afcus;");

		return tab_custodio;
	}

	public String getCompras(String activo) {
		String tab_compra = "SELECT * FROM (select a.ide_adfac,a.ide_tepro,num_factura_adfac,a.detalle_adfac,b.nombre_tepro,(SELECT COUNT(*) FROM adq_detalle_factura c WHERE c.ide_adqdft= ";
		tab_compra += "2";// Existencias es 2
		tab_compra += " AND c.procesado_adfac=FALSE AND c.ide_adfac=a.ide_adfac) as items from adq_factura a LEFT JOIN	tes_proveedor b ON a.ide_tepro=b.ide_tepro) f WHERE items >0;";
		return tab_compra;
	}

	public String getComprasCodigo(String ide_adsoc) {
		System.out.println("getComprasCodigo: " + ide_adsoc);
		String tab_compra = "select a.ide_adfac,fecha_factura_adfac,a.ide_tepro,num_factura_adfac,detalle_adfac,nombre_tepro from adq_factura a,tes_proveedor b  where a.ide_tepro=b.ide_tepro and  a.ide_adfac in (" + ide_adsoc + ") order by detalle_adfac";
		return tab_compra;
	}

	public String getDetalleComprasCodigo(String ide_adsoc) {
		System.out.println("getDetalleComprasCodigo: " + ide_adsoc);
		String tab_compra = "SELECT ide_addef, descripcion_addef, ide_adfac, ide_bomat, valor_unitario_addef, cantidad_addef,        valor_total_addef, aplica_iva_addef, activo_addef, usuario_ingre,        fecha_ingre, hora_ingre, usuario_actua, fecha_actua, hora_actua,        recibido_addef, marca_addef, serie_addef, color_addef, por_descuento_addef,        valor_descuento_addef, modelo_addef, aplica_iva_adfac  FROM adq_detalle_factura  WHERE ide_adfac in ("
				+ ide_adsoc + ");";
		return tab_compra;
	}

	public String getDetalleExistencia() {
		return " SELECT 	ide_exist, " + " b.cat_codigo_bocam, " + " CASE WHEN b.con_ide_bocam IS NULL THEN ' ' ELSE (SELECT  descripcion_bocam FROM bodt_catalogo_material c WHERE c.ide_bocam=b.con_ide_bocam)END AS familia, " + " b.descripcion_bocam , "
				+ " marca_exist as marca, " + " modelo_exist as modelo, " + " color_exist as color , " + " dimension_exist as dimensiones, " + " fecha_caducidad_exist,  " + " ps.descripcion_bnpeli as peli_salud, "
				+ " pf.descripcion_bnpeli as peli_inflamable, " + " pi.descripcion_bnpeli as peli_inestable, " + " riesgo_especifico_exist, " + " e.detalle_bounm "
				+ " FROM bodt_existencia a LEFT JOIN bodt_catalogo_material b ON a.ide_bocam = b.ide_bocam " + " LEFT JOIN bodt_nivel_peligrosidad ps ON a.ide_bnpeli_salud = ps.ide_bnpeli "
				+ " LEFT JOIN bodt_nivel_peligrosidad pf ON a.ide_bnpeli_inflama = pf.ide_bnpeli " + " LEFT JOIN bodt_nivel_peligrosidad pi ON a.ide_bnpeli_inesta = pi.ide_bnpeli " + " LEFT JOIN bodt_bodega_ubicacion d ON a.ide_boubi=d.ide_boubi "
				+ " LEFT JOIN bodt_unidad_medida e ON a.ide_bounm=e.ide_bounm;";
	}

	public String getExistencias() {
		return " SELECT 	ide_exist, " + " d.detalle_boubi as bodega,  " + " b.cat_codigo_bocam as item_presupuestario, "
				+ " CASE WHEN b.con_ide_bocam IS NULL THEN ' ' ELSE (SELECT  descripcion_bocam FROM bodt_catalogo_material c WHERE c.ide_bocam=b.con_ide_bocam)END AS familia, " + " b.descripcion_bocam , " + " exist_cantidad_saldo, "
				+ " marca_exist as marca, " + " modelo_exist as modelo, " + " color_exist as color , " + " dimension_exist as dimensiones, " + " fecha_caducidad_exist, " + " ps.descripcion_bnpeli as peli_salud, "
				+ " pf.descripcion_bnpeli as peli_inflamable, " + " pi.descripcion_bnpeli as peli_inestable, " + " riesgo_especifico_exist, " + " e.detalle_bounm as presentacion"
				+ " FROM bodt_existencia a LEFT JOIN bodt_catalogo_material b ON a.ide_bocam = b.ide_bocam " + " LEFT JOIN bodt_nivel_peligrosidad ps ON a.ide_bnpeli_salud = ps.ide_bnpeli "
				+ " LEFT JOIN bodt_nivel_peligrosidad pf ON a.ide_bnpeli_inflama = pf.ide_bnpeli " + " LEFT JOIN bodt_nivel_peligrosidad pi ON a.ide_bnpeli_inesta = pi.ide_bnpeli " + " LEFT JOIN bodt_bodega_ubicacion d ON a.ide_boubi=d.ide_boubi "
				+ " LEFT JOIN bodt_unidad_medida e ON a.ide_bounm=e.ide_bounm " + " WHERE exist_cantidad_saldo >0;";
	}

	public String getExistencias(String ide_boubi) {
		return " SELECT 	ide_exist, " + " d.detalle_boubi as bodega,  " + " b.cat_codigo_bocam as item_presupuestario, "
				+ " CASE WHEN b.con_ide_bocam IS NULL THEN ' ' ELSE (SELECT  descripcion_bocam FROM bodt_catalogo_material c WHERE c.ide_bocam=b.con_ide_bocam)END AS familia, " + " b.descripcion_bocam , " + " exist_cantidad_saldo, "
				+ " marca_exist as marca, " + " modelo_exist as modelo, " + " color_exist as color , " + " dimension_exist as dimensiones, " + " fecha_caducidad_exist, " + " ps.descripcion_bnpeli as peli_salud, "
				+ " pf.descripcion_bnpeli as peli_inflamable, " + " pi.descripcion_bnpeli as peli_inestable, " + " riesgo_especifico_exist, " + " e.detalle_bounm as presentacion"
				+ " FROM bodt_existencia a LEFT JOIN bodt_catalogo_material b ON a.ide_bocam = b.ide_bocam " + " LEFT JOIN bodt_nivel_peligrosidad ps ON a.ide_bnpeli_salud = ps.ide_bnpeli "
				+ " LEFT JOIN bodt_nivel_peligrosidad pf ON a.ide_bnpeli_inflama = pf.ide_bnpeli " + " LEFT JOIN bodt_nivel_peligrosidad pi ON a.ide_bnpeli_inesta = pi.ide_bnpeli " + " LEFT JOIN bodt_bodega_ubicacion d ON a.ide_boubi=d.ide_boubi "
				+ " LEFT JOIN bodt_unidad_medida e ON a.ide_bounm=e.ide_bounm " + " WHERE exist_cantidad_saldo >0 AND a.ide_boubi = " + ide_boubi + ";";
	}

	public String getKardexExistencia(String ide_bocam, String ide_geani) {
		String sql = "";
		sql += " SELECT 	b.ide_geani,";
		sql += " 	e.ide_bocam,";
		sql += " 	d.cat_codigo_bocam || ' '||d.descripcion_bocam as item_catalogo,";
		sql += " 	f.detalle_bounm as medida,";
		sql += " 	g.detalle_bounm as presentacion,";
		sql += " 	e.ide_exist,";
		sql += " 	a.exist_cantidad,";
		sql += " 	c.detalle_aftidoc,";
		sql += " 	b.fecha_ingre ||' '||b.hora_ingre as registro_fecha";
		sql += " FROM";
		sql += " 	afi_doc_detalle_existencia a";
		sql += " 	LEFT JOIN bodt_existencia e ON a.ide_exist=e.ide_exist";
		sql += " 	LEFT JOIN afi_docu b ON a.ide_afdoc=b.ide_afdoc";
		sql += " 	LEFT JOIN afi_tipo_docu c ON b.ide_aftidoc=c.ide_aftidoc";
		sql += " 	LEFT JOIN bodt_catalogo_material d ON e.ide_bocam=d.ide_bocam";
		sql += " 	LEFT JOIN bodt_unidad_medida f ON d.ide_bounm=f.ide_bounm";
		sql += " 	LEFT JOIN bodt_unidad_medida g ON e.ide_bounm=g.ide_bounm";
		sql += " WHERE b.ide_geani=" + ide_geani;
		if (ide_bocam != null && !ide_bocam.isEmpty()) {
			sql += " AND e.ide_bocam IN (" + ide_bocam + ")";
		}
		sql += " UNION ALL";
		sql += " SELECT 	b1.ide_geani,";
		sql += " 	a1.ide_bocam,";
		sql += " 	d.cat_codigo_bocam || ' '||d.descripcion_bocam as item_catalogo,";
		sql += " 	f.detalle_bounm as medida,";
		sql += " 	g.detalle_bounm as presentacion,";
		sql += " 	a1.ide_exist,";
		sql += " 	a1.exist_cantidad,";
		sql += " 	CASE WHEN b1.es_ingreso_por_cierre THEN 'Ingreso por Cierre' ELSE 'Acta de Ingreso' END AS detalle_aftidoc,";
		sql += " 	b1.fecha_ingre ||' '||b1.hora_ingre as registro_fecha";
		sql += " FROM";
		sql += " 	bodt_existencia a1";
		sql += " 	LEFT JOIN bodt_bodega b1 ON a1.ide_bobod=b1.ide_bobod";
		sql += " 	LEFT JOIN bodt_catalogo_material d ON a1.ide_bocam=d.ide_bocam";
		sql += " 	LEFT JOIN bodt_unidad_medida f ON d.ide_bounm=f.ide_bounm";
		sql += " 	LEFT JOIN bodt_unidad_medida g ON a1.ide_bounm=g.ide_bounm";
		sql += " WHERE b1.ide_geani=" + ide_geani;
		if (ide_bocam != null && !ide_bocam.isEmpty()) {
			sql += " AND a1.ide_bocam IN (" + ide_bocam + ")";
		}
		sql += " ORDER BY ide_bocam,ide_exist,registro_fecha;";
		return sql;
	}

	public String getExistenciasEnBodega(String anio) {
		String activo = " SELECT 	ide_bosto,	" + " n.detalle_geani as año,	" + " bbs.ide_geani,	" + " detalle_boubi as bodega,	" + " bbs.ide_bocam as codigo_catalogo,	" + " bcm.cat_codigo_bocam as item_presupuestario,	"
				+ " pbcm.descripcion_bocam as familia,	" + " bcm.descripcion_bocam as nombre_catalogo,	" + " med.detalle_bounm as unidad_medida,	" + " pres.detalle_bounm as presentacion,	" + " cantidad_ingreso_bosto,	" + " cantidad_egreso_bosto,	"
				+ " saldo_existencia_bosto,	" + " fecha_ultima_transaccion_bosto,	" + " saldo_inicial_bosto,	" + " fecha_saldo_inicial,	" + " costo_inicial_bosto, 		" + " costo_anterior_bosto, 		" + " costo_actual_bosto,	" + " activo_bosto	" + " FROM 		"
				+ " bodt_bodega_stock bbs	" + " LEFT JOIN bodt_catalogo_material AS bcm ON bbs.ide_bocam = bcm.ide_bocam	" + " LEFT JOIN bodt_catalogo_material AS pbcm ON bcm.con_ide_bocam = pbcm.ide_bocam	"
				+ " LEFT JOIN gen_anio AS n ON bbs.ide_geani = n.ide_geani	" + " LEFT JOIN bodt_bodega_ubicacion AS ubi ON bbs.ide_boubi=ubi.ide_boubi	" + " LEFT JOIN bodt_unidad_medida AS med ON bcm.ide_bounm  = med.ide_bounm	"
				+ " LEFT JOIN bodt_unidad_medida AS pres ON bbs.ide_bounm  = pres.ide_bounm		" + " WHERE bbs.ide_geani =	" + anio + " ORDER BY bodega;";
		return activo;
	}

	/**
	 * 
	 * @param ide_boubi
	 * @param ide_bocam
	 * @param ide_geani
	 * @param ide_bounm
	 * @return
	 */
	public String getSaldoItem(String ide_boubi, String ide_bocam, String ide_geani, String ide_bounm) {
		String query = "SELECT ide_bosto, ide_geani, ide_bocam, cantidad_ingreso_bosto, cantidad_egreso_bosto, saldo_existencia_bosto, fecha_ultima_transaccion_bosto, saldo_inicial_bosto, fecha_saldo_inicial, costo_inicial_bosto, costo_anterior_bosto, costo_actual_bosto, ide_boubi, ide_bounm"
				+ " FROM bodt_bodega_stock WHERE ide_boubi= " + ide_boubi + " AND ide_bocam= " + ide_bocam + " AND ide_geani= " + ide_geani + " AND ide_bounm= " + ide_bounm + ";";
		return query;
	}

	public TablaGenerica getSaldoItem(String ide_exist) {
		TablaGenerica tab_saldo = utilitario.consultar("SELECT ide_exist, exist_cantidad_saldo FROM bodt_existencia WHERE ide_exist =" + ide_exist + ";");
		return tab_saldo;
	}

	/**
	 * 
	 * Sirve para actualizar el saldo en bodega cuando se realiza un ingreso
	 * 
	 * @param ide_boubi
	 *            Bodega de la cual se quiere actualizar el item
	 * @param ide_bocam
	 *            Codigo del catalogo de bienes y existencias
	 * @param ide_geani
	 *            Año en el cual se va a realizar la operacion
	 * @param ide_bounm
	 *            Presentación del item
	 * @param saldo_existencia_bosto
	 * @param cantidad_ingreso_bosto
	 * @param costo_anterior_bosto
	 * @param costo_actual_bosto
	 * @return Query con el Update requerido
	 */
	public String updateSaldoItem(String ide_boubi, String ide_bocam, String ide_geani, String ide_bounm, String saldo_existencia_bosto, String cantidad_ingreso_bosto, String costo_anterior_bosto, String costo_actual_bosto) {
		String query = " UPDATE bodt_bodega_stock " + " SET cantidad_ingreso_bosto=" + cantidad_ingreso_bosto + "," + " saldo_existencia_bosto= " + saldo_existencia_bosto + "," + " fecha_ultima_transaccion_bosto=NOW()," + " costo_anterior_bosto="
				+ costo_anterior_bosto + "," + " costo_actual_bosto=" + costo_actual_bosto + " " + " WHERE ide_boubi= " + ide_boubi + " AND ide_bocam= " + ide_bocam + " AND ide_geani= " + ide_geani + " AND ide_bounm= " + ide_bounm + ";";
		return query;
	}

	/**
	 * 
	 * Sirve para actualizar el saldo en bodega cuando se realiza un egreso
	 * 
	 * @param ide_boubi
	 *            Bodega de la cual se quiere actualizar el item
	 * @param ide_bocam
	 *            Codigo del catalogo de bienes y existencias
	 * @param ide_geani
	 *            Año en el cual se va a realizar la operacion
	 * @param ide_bounm
	 *            Presentación del item
	 * @param saldo_existencia_bosto
	 * @param cantidad_egreso_bosto
	 * @return Query con el Update requerido
	 */
	public String updateSaldoItem(String ide_boubi, String ide_bocam, String ide_geani, String ide_bounm, String cantidad_egreso_bosto, String saldo_existencia_bosto) {
		String query = " UPDATE bodt_bodega_stock " + " SET cantidad_egreso_bosto=" + cantidad_egreso_bosto + "," + " saldo_existencia_bosto= " + saldo_existencia_bosto + "," + " fecha_ultima_transaccion_bosto=NOW() WHERE ide_boubi= " + ide_boubi
				+ " AND ide_bocam= " + ide_bocam + " AND ide_geani= " + ide_geani + " AND ide_bounm= " + ide_bounm + ";";
		return query;
	}

	public String unidadPresentacion(String ide_exist) {
		System.out.println("unidadPresentacion() --> ide_exist:" + ide_exist);
		String ide_bounm = "";
		TablaGenerica tab_unid = utilitario.consultar("SELECT ide_exist, ide_bounm FROM bodt_existencia WHERE ide_exist= " + ide_exist);
		ide_bounm = tab_unid.getValor("ide_bounm");
		System.out.println("unidadPresentacion() --> return ide_bounm:" + ide_bounm);
		return ide_bounm;
	}

	/**
	 * Cuando no existe el registro en las condiciones especificadas se requiere
	 * el insert
	 * 
	 * @param ide_boubi
	 *            Bodega de la cual se quiere insertar el item
	 * @param ide_bocam
	 *            Codigo del catalogo de bienes y existencias
	 * @param ide_geani
	 *            Año en el cual se va a realizar la operacion
	 * @param ide_bounm
	 *            Presentación del item
	 * @param cantidad
	 * @param valorPPP
	 * @return Query con el Insert requerido
	 */
	public String insertSaldoItem(String ide_boubi, String ide_bocam, String ide_geani, String ide_bounm, String cantidad, String valorPPP) {
		String query = "INSERT INTO bodt_bodega_stock( ide_bosto, ide_geani, ide_bocam, cantidad_ingreso_bosto, cantidad_egreso_bosto, saldo_existencia_bosto, fecha_ultima_transaccion_bosto, saldo_inicial_bosto, fecha_saldo_inicial, costo_inicial_bosto, costo_anterior_bosto, costo_actual_bosto, activo_bosto, ide_boubi, ide_bounm)  VALUES ( ";
		query += " (SELECT MAX(ide_bosto)+1 AS ide_bosto FROM bodt_bodega_stock), ";// ide_bosto
		query += " " + ide_geani + ", ";// ide_geani
		query += " " + ide_bocam + ", ";// ide_bocam
		query += " 0, ";// cantidad_ingreso_bosto
		query += " 0, ";// cantidad_egreso_bosto
		query += " " + cantidad + ", ";// saldo_existencia_bosto
		query += " NOW(), ";// fecha_ultima_transaccion_bosto
		query += " " + cantidad + ", ";// saldo_inicial_bosto
		query += " NOW(), ";// fecha_saldo_inicial
		query += " " + valorPPP + ", ";// costo_inicial_bosto
		query += " 0, ";// costo_anterior_bosto
		query += " " + valorPPP + ", ";// costo_actual_bosto
		query += " TRUE, ";// activo_bosto
		query += " " + ide_boubi + ", ";// ide_boubi
		query += " " + ide_bounm + ");";// ide_bounm
		return query;
	}

}
