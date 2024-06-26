package paq_activos.ejb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import framework.aplicacion.TablaGenerica;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Utilitario;

@Stateless
public class ServicioActivos {
	private Utilitario utilitario = new Utilitario();

	public TablaGenerica getTablaGenericaConsultaCustodio(String ide_custodio) {
		TablaGenerica tab_custodio = utilitario
				.consultar("SELECT ide_afcus, ide_afact, ide_geedp, detalle_afcus, fecha_entrega_afcus,"
						+ " fecha_descargo_afcus, numero_acta_afcus, razon_descargo_afcus,"
						+ " cod_barra_afcus, nro_secuencial_afcus, activo_afcus,gen_ide_geedp"
						+ " FROM afi_custodio where ide_afcus in (" + ide_custodio + ") order by ide_afcus;");
		return tab_custodio;
	}

	public String getActaObservacion(String ide_afdoc) {
		String query = "";
		query = "SELECT detalle_afdoc FROM afi_docu WHERE ide_afdoc=" + ide_afdoc;
		return query;
	}

	public String getConsultaPeritoBienes(String ide_geani) {
		String string;
		string = "SELECT afa.ide_afact as codigo_bien, " + "	afa.cod_anterior_afact as codigo_anterior, "
				+ "	bcm.cat_codigo_bocam as item_presupuestario, " + "	atip.detalle_aftia as tipo_activo, "
				+ "	afa.detalle_afact as descripcion_caracteristicas, " + "	afa.cantidad_afact as cantidad, "
				+ "	afa.observaciones_afact as componentes, " + "	afa.serie_afact as serie, "
				+ "	afa.marca_afact as marca, " + "	afa.modelo_afact as modelo, " + "	afa.color_afact as color, "
				+ "	afa.chasis_afact as chasis, " + "	afa.motor_afact as motor, " + "	afa.placa_afact as placa, "
				+ "	est.detalle_afest as estado, " + "	afa.valor_neto_afact as valor_neto, "
				+ "	afa.valor_iva as iva, " + " afa.valor_compra_afact as valor_compra, "

				+ " per.fecha_reavaluo_afper as fecha_reavaluo, " + " per.valor_residual_afper as valor_residual, "
				+ " per.valor_revaluo_comercial_afper as valor_revaluo_comercial, "
				+ " per.valor_realizacion_afper as valor_realizacion "

				+ " FROM afi_perito per " + " JOIN afi_activo afa on afa.ide_afact=per.ide_afact "
				+ "	LEFT JOIN bodt_catalogo_material AS bcm ON afa.ide_bocam = bcm.ide_bocam "
				+ "	LEFT JOIN afi_ubicacion AS ubi ON afa.ide_afubi = ubi.ide_afubi "
				+ "	LEFT JOIN bodt_bodega AS ing ON afa.ide_bobod = ing.ide_bobod "
				+ "	LEFT JOIN tes_proveedor	AS prov ON afa.ide_tepro = prov.ide_tepro "
				+ "	LEFT JOIN afi_tipo_propiedad AS tip ON afa.ide_aftip = tip.ide_aftip "
				+ "	LEFT JOIN afi_tipo_activo AS atip ON afa.ide_aftia = atip.ide_aftia "
				+ "	LEFT JOIN afi_estado AS est ON afa.ide_afest = est.ide_afest "
				+ "	LEFT JOIN afi_docu AS doc ON afa.afi_ultima_acta = doc.ide_afdoc "
				+ "	LEFT JOIN afi_tipo_docu AS tdoc ON doc.ide_aftidoc = tdoc.ide_aftidoc "
				+ "	LEFT JOIN afi_docu AS cons ON afa.afi_ultima_acta_consta = cons.ide_afdoc "
				+ "	LEFT JOIN afi_tipo_docu AS tcons ON cons.ide_aftidoc = tcons.ide_aftidoc "
				+ "	LEFT JOIN (SELECT c.ide_afddc, c.ide_gtemp, c.ide_afdoc, c.recibe_afddc, c.activo_afdda "
				+ "		 FROM afi_doc_detalle_custodio c WHERE c.recibe_afddc=TRUE) AS cust ON afa.afi_ultima_acta = cust.ide_afdoc "
				+ "	LEFT JOIN gth_empleado gem  ON gem.ide_gtemp = cust.ide_gtemp "

				+ "	where per.ide_geani= " + ide_geani

				+ " ORDER BY 1 DESC";

		System.out.println("getConsultaPeritoBienes: " + string);
		return string;
	}

	public String getConsultaCustodiosYBienes() {
		String string;
		string = "SELECT 	afa.ide_afact as bien_codigo, " + "	prov.ruc_tepro as proveedor_ruc, "
				+ "	prov.nombre_tepro as proveedor_nombre, "
				+ "	prov.nom_representante_tepro as proveedor_representante, "
				+ "	ubi.detalle_afubi as bien_ubicacion, " + "	afa.afi_ubicacion_otro as referencia_ubicacion, "
				+ "	tip.detalle_aftip as forma_ingreso, " + "	ing.fecha_ingreso_bobod as ingreso_fecha_ingreso, "
				+ "	ing.fecha_compra_bobod as ingreso_fecha_factura, "
				+ "	ing.numero_ingreso_bobod as ingreso_numero, "
				+ " CASE WHEN ing.numero_ingreso_bobod = 0 THEN afa.nro_factura_afact ELSE ing.num_factura_bobod END AS factura_numero, "
				+ " ing.num_factura_anterior_bobod as factura_anterior,"
				+ " CASE WHEN ing.numero_ingreso_bobod = 0 THEN 'INGRESO ANTIGUO' ELSE ing.descripcion_bobod END AS ingreso_observacion, "
				+ "	ing.novedad_bobod as ingreso_novedad, " + "	afa.ide_bocam as codigo_catalogo, "
				+ "	bcm.cat_codigo_bocam as item_presupuestario, " + "	bcm.descripcion_bocam as nombre_catalogo, "
				+ "	afa.fecha_alta_afact as fecha_alta, " + "	afa.cantidad_afact as cantidad, "
				+ "	afa.cod_anterior_afact as codigo_anterior, "
				+ "	afa.detalle_afact as descripcion_caracteristicas, " + "	afa.observaciones_afact as componentes, "
				+ "	afa.color_afact as color, " + "	afa.marca_afact as marca, " + "	afa.serie_afact as serie, "
				+ "	afa.modelo_afact as modelo, " + "	afa.chasis_afact as chasis, " + "	afa.motor_afact as motor, "
				+ "	afa.placa_afact as placa, " + "	afa.novedad_afact as novedad, "
				+ "	afa.valor_neto_afact as valor_neto, " + "	afa.valor_iva as iva, "
				+ " afa.valor_compra_afact as valor_compra, " + "	afa.fecha_reavaluo_afact as fecha_revaluo, "
				+ "	afa.valor_revaluo_afact as valor_revaluo_comercial, "
				+ "	afa.valor_residual_afact as valor_residual, "
				+ "	afa.valor_realizacion_afact as valor_realizacion, " + "	afa.fecha_baja_afact as fecha_baja, "
				+ "	afa.razon_baja_afact as razon_baja, " + "	atip.detalle_aftia as tipo_activo, "
				// +
				// " case when afa.valor_neto_afact<100 then 'BSC' else 'BLD' end as
				// tipo_activo, "
				+ "	est.detalle_afest as estado, " + "	afa.afi_ultima_acta as ultima_acta_erp, "
				+ "	tdoc.detalle_aftidoc as tipo_ultima_acta, " + "	doc.nro_secuencial_afdoc as numero_ultima_acta, "
				+ "	CASE WHEN afa.afi_ultima_acta IS NOT NULL THEN (SELECT COUNT(h.ide_gtemp) FROM afi_doc_detalle_custodio h WHERE h.recibe_afddc = true AND h.ide_afdoc=afa.afi_ultima_acta) ELSE 0 END AS numero_custodios, "
				+ "	gem.documento_identidad_gtemp as cedula, " + "	gem.primer_nombre_gtemp as primer_nombre, "
				+ "	gem.segundo_nombre_gtemp as segundo_nombre, " + "	gem.apellido_paterno_gtemp as primer_apellido, "
				+ "	gem.apellido_materno_gtemp as segundo_apellido, " + "	doc.fecha_afdoc as fecha_ultima_acta, "
				+ "	doc.detalle_afdoc as observacion_ultima_acta, "
				+ "	afa.afi_ultima_acta_consta as acta_ultima_constacion_erp, "
				+ "	tcons.detalle_aftidoc as tipo_constatacion, "
				+ "	cons.nro_secuencial_afdoc as numero_ultima_constatacion, "
				+ "	cons.fecha_afdoc as fecha_ultima_constatacion, "
				+ "	cons.detalle_afdoc as observacion_ultima_constatacion, "
				// Ticket #714154
				+ " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2015' THEN 'N/A' ELSE"
				+ " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 7)THEN 'SI' ELSE 'NO' END) END AS constatacion_2015,"
				+ " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2016' THEN 'N/A' ELSE"
				+ " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 8)THEN 'SI' ELSE 'NO' END)END AS constatacion_2016,"
				+ " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2017' THEN 'N/A' ELSE"
				+ " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 10)THEN 'SI' ELSE 'NO' END)END AS constatacion_2017,"
				// Ticket #517883 Estimados favor su ayuda en la columna de
				// CONSTATACIÓN 2018, que los bienes que se detalla a
				// continuación aparezca lo siguiente: “NO PROCEDE” a
				// (5551,5552,5553,5554,5555,5557,5558,5559,5560,5561,5562,5563,5564,5565,5566,5567,5568,5569,5570,5571,5572,5573,5574,5575,5576,5577,5578,5579,5580,5581,5582,5584,5592,5593,5481,5480,5479,5478,5477,5476,5475,5474,5473,5472,5471,5470,5469,5468,5467,5466,5458,5457,5456,5455,5453,5452,5451,5420,5417,5304,5303,5166,5165,3123,3122,2718,2717,2716,2715,2710,2697,2142,3561,3539,2494,2052)
				+ "  CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2018' THEN 'N/A' ELSE "
				+ " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 11) THEN 'SI' "
				+ " WHEN afa.ide_afact IN (5551,5552,5553,5554,5555,5557,5558,5559,5560,5561,5562,5563,5564,5565,5566,5567,5568,5569,5570,5571,5572,5573,5574,5575,5576,5577,5578,5579,5580,5581,5582,5584,5592,5593,5481,5480,5479,5478,5477,5476,5475,5474,5473,5472,5471,5470,5469,5468,5467,5466,5458,5457,5456,5455,5453,5452,5451,5420,5417,5304,5303,5166,5165,3123,3122,2718,2717,2716,2715,2710,2697,2142,3561,3539,2494,2052) THEN 'NO PROCEDE' "
				+ " ELSE 'NO' END)END AS constatacion_2018,  "
				+ " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2019' THEN 'N/A' ELSE "
				+ " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 12)THEN 'SI' ELSE 'NO' END)END AS constatacion_2019, "
				+ " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2020' THEN 'N/A' ELSE "
				+ " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 12)THEN 'SI' ELSE 'NO' END)END AS constatacion_2020, "
				+ " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2021' THEN 'N/A' ELSE "
				+ " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 12)THEN 'SI' ELSE 'NO' END)END AS constatacion_2021, "
				+ " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2022' THEN 'N/A' ELSE "
				+ " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 12)THEN 'SI' ELSE 'NO' END)END AS constatacion_2022, "

				+ " CASE WHEN afi_intangible = TRUE THEN 'SI' ELSE 'NO' END AS intangible" + " FROM afi_activo afa "
				+ "	LEFT JOIN bodt_catalogo_material AS bcm ON afa.ide_bocam = bcm.ide_bocam "
				+ "	LEFT JOIN afi_ubicacion AS ubi ON afa.ide_afubi = ubi.ide_afubi "
				+ "	LEFT JOIN bodt_bodega AS ing ON afa.ide_bobod = ing.ide_bobod "
				+ "	LEFT JOIN tes_proveedor	AS prov ON afa.ide_tepro = prov.ide_tepro "
				+ "	LEFT JOIN afi_tipo_propiedad AS tip ON afa.ide_aftip = tip.ide_aftip "
				+ "	LEFT JOIN afi_tipo_activo AS atip ON afa.ide_aftia = atip.ide_aftia "
				+ "	LEFT JOIN afi_estado AS est ON afa.ide_afest = est.ide_afest "
				+ "	LEFT JOIN afi_docu AS doc ON afa.afi_ultima_acta = doc.ide_afdoc "
				+ "	LEFT JOIN afi_tipo_docu AS tdoc ON doc.ide_aftidoc = tdoc.ide_aftidoc "
				+ "	LEFT JOIN afi_docu AS cons ON afa.afi_ultima_acta_consta = cons.ide_afdoc "
				+ "	LEFT JOIN afi_tipo_docu AS tcons ON cons.ide_aftidoc = tcons.ide_aftidoc "
				+ "	LEFT JOIN (SELECT c.ide_afddc, c.ide_gtemp, c.ide_afdoc, c.recibe_afddc, c.activo_afdda "
				+ "		 FROM afi_doc_detalle_custodio c WHERE c.recibe_afddc=TRUE) AS cust ON afa.afi_ultima_acta = cust.ide_afdoc "
				+ "	LEFT JOIN gth_empleado gem  ON gem.ide_gtemp = cust.ide_gtemp " + " ORDER BY 1 DESC";
		return string;
	}

	public String getConsultaHistoricoActasCustodios(String custodio) {
		String query;
		query = "";
		query += "SELECT ";
		query += "	adoc.ide_afdoc as codigo_acta_erp,";
		query += "	CASE WHEN recibe_afddc THEN 'RECIBE' ELSE 'ENTREGA' END  as accion,";
		query += "	detalle_aftidoc as acta_tipo,";
		query += "	n.detalle_geani as acta_anio,";
		query += "	fecha_afdoc as acta_fecha,";
		query += "	nro_secuencial_afdoc as acta_numero,";
		query += "	detalle_afdoc as acta_observacion,";
		query += "  (SELECT nom_usua FROM sis_usuario WHERE nick_usua = adoc.usuario_ingre) as usuario_que_realizo,";
		query += "	(SELECT count(ide_afact) FROM afi_doc_detalle_activo act WHERE act.ide_afdoc=adoc.ide_afdoc) as numero_bienes,";
		query += "	(SELECT array_to_string(array(SELECT ide_afact FROM afi_doc_detalle_activo act WHERE act.ide_afdoc=adoc.ide_afdoc ORDER BY act.ide_afact),', ')) as CODIGO_BIENES";
		query += "  FROM afi_doc_detalle_custodio addc";
		query += "  LEFT JOIN afi_docu adoc ON addc.ide_afdoc=adoc.ide_afdoc";
		query += "  LEFT JOIN afi_tipo_docu atid ON adoc.ide_aftidoc=atid.ide_aftidoc";
		query += "	LEFT JOIN gen_anio n ON adoc.ide_geani = n.ide_geani";
		query += "  WHERE addc.ide_gtemp IN (" + custodio + ") order by adoc.fecha_afdoc ;";
		return query;
	}

	public String getConsultaHistoricoActasActivo(String activos) {
		String query;
		query = "";
		query += "SELECT ";
		query += "	act.ide_afact,";
		query += "	CASE WHEN act.afi_ultima_acta=a.ide_afdoc THEN 'ACTA '||act.afi_ultima_acta WHEN act.afi_ultima_acta_consta=a.ide_afdoc  THEN 'CONSTATACION '||act.afi_ultima_acta_consta ELSE ' ' END as acta_activa,";
		query += "	a.ide_afdoc as acta_codigo_erp,";
		query += "	c.detalle_aftidoc as acta_tipo,";
		query += "	n.detalle_geani as acta_anio,";
		query += "	b.fecha_afdoc as acta_fecha,";
		query += "	b.nro_secuencial_afdoc as acta_numero,";
		query += "	b.detalle_afdoc as acta_observacion,";
		query += "  (SELECT nom_usua FROM sis_usuario WHERE nick_usua = b.usuario_ingre) as usuario_que_elaboro,";
		query += "	(SELECT array_to_string(array(";
		query += "		SELECT tmpemp.documento_identidad_gtemp || ' ' || tmpemp.primer_nombre_gtemp || ' ' || tmpemp.segundo_nombre_gtemp || ' '|| tmpemp.apellido_paterno_gtemp || ' ' || tmpemp.apellido_materno_gtemp as nombrecompleto";
		query += "		FROM gth_empleado tmpemp WHERE ide_gtemp IN (SELECT tmp.ide_gtemp FROM afi_doc_detalle_custodio tmp WHERE tmp.ide_afdoc = b.ide_afdoc AND tmp.recibe_afddc =false)";
		query += "	),', ')) as acta_custodios_entregan,";
		query += "	(SELECT array_to_string(array(";
		query += "		SELECT tmpemp.documento_identidad_gtemp || ' ' || tmpemp.primer_nombre_gtemp || ' ' || tmpemp.segundo_nombre_gtemp || ' '|| tmpemp.apellido_paterno_gtemp || ' ' || tmpemp.apellido_materno_gtemp as nombrecompleto";
		query += "		FROM gth_empleado tmpemp WHERE ide_gtemp IN (SELECT tmp.ide_gtemp FROM afi_doc_detalle_custodio tmp WHERE tmp.ide_afdoc = b.ide_afdoc AND tmp.recibe_afddc =true)";
		query += "	),', ')) as acta_custodios_reciben";
		query += "  FROM";
		query += "	afi_activo act";
		query += "	LEFT JOIN bodt_catalogo_material AS bcm ON act.ide_bocam = bcm.ide_bocam";
		query += "	LEFT JOIN afi_doc_detalle_activo a ON act.ide_afact =a.ide_afact";
		query += "	LEFT JOIN afi_docu b ON a.ide_afdoc=b.ide_afdoc";
		query += "	LEFT JOIN afi_tipo_docu c ON b.ide_aftidoc = c.ide_aftidoc";
		query += "	LEFT JOIN gen_anio n ON b.ide_geani = n.ide_geani";
		query += "  WHERE act.ide_afact in (" + activos + ")";
		query += "  ORDER BY b.fecha_afdoc;";
		return query;
	}

	public String getDescripcionActivosCombo() {
		String query;
		query = "";
		query += "SELECT ";
		query += "	act.ide_afact,";
		query += "	act.ide_afact AS codigo,";
		// query += " bcm.cat_codigo_bocam as item_presupuestario,";
		query += "	bcm.descripcion_bocam as nombre_catalogo,";

		query += "  CASE WHEN  char_length(act.detalle_afact) > 35 then substring(act.detalle_afact from 1 for 35)||'...' ELSE act.detalle_afact END as detalle_afact,";
		// query += "	act.cod_anterior_afact as codigo_anterior,";
		// query += " act.color_afact as color,";
		query += "	act.marca_afact as marca,";
		query += "	act.serie_afact as serie,";
		query += "	act.modelo_afact as modelo ";

		// query += " act.chasis_afact as chasis,";
		// query += " act.motor_afact as motor";
		query += "  FROM ";
		query += "	afi_activo act";
		query += "	LEFT JOIN bodt_catalogo_material AS bcm ON act.ide_bocam = bcm.ide_bocam ORDER BY 1";
		return query;
	}

	public String getConsultaCustodiosYBienesSinBajas() {
		String string;
		string = "WITH perito AS ( ";
		string += "   SELECT  ";
		string += "    DISTINCT ON (ide_afact) * ";
		string += "   FROM afi_perito  ";
		string += "   ORDER BY afi_perito.ide_afact, afi_perito.fecha_reavaluo_afper DESC NULLS LAST ";
		string += ") ";
		string += " SELECT ";
		string += " afa.ide_afact as codigo,   ";
		string += " afa.cod_anterior_afact as codigo_anterior,  ";
		string += " prov.ruc_tepro as proveedor_ruc,   ";
		string += " prov.nombre_tepro as proveedor_nombre,   ";
		string += " prov.nom_representante_tepro as proveedor_representante,   ";
		string += " ing.fecha_ingreso_bobod as ingreso_fecha_ingreso,    ";
		string += " afa.fecha_alta_afact as fecha_alta, ";
		string += " CASE WHEN ing.numero_ingreso_bobod = 0 THEN afa.nro_factura_afact ELSE ing.num_factura_bobod END AS factura_numero, ";
		string += " ing.fecha_compra_bobod as ingreso_fecha_factura, ";
		string += " ing.num_factura_anterior_bobod as factura_anterior,";
		string += " tip.detalle_aftip as forma_ingreso, ";
		string += " ing.numero_ingreso_bobod as ingreso_numero,";
		string += " ing.novedad_bobod as ingreso_novedad,";
		string += " CASE WHEN ing.numero_ingreso_bobod = 0 THEN 'INGRESO ANTIGUO' ELSE ing.descripcion_bobod END AS ingreso_observacion,";
		string += " afa.ide_bocam as codigo_catalogo,  ";
		string += " bcm.descripcion_bocam as nombre_catalogo,";
		string += " bcm.cat_codigo_bocam as item_presupuestario,  ";
		string += " afi_poa.detalle_prtge as detalle_prtge,  ";
		string += " afi_poa.cod_prog || ' ' || afi_poa.detalle_programa as programa,  ";
		string += " afi_poa.detalle_proyecto || ' ' || afi_poa.cod_pry as proyecto,  ";
		string += " atip.detalle_aftia as tipo_activo,  ";
		string += " afa.detalle_afact as descripcion_caracteristicas,";
		string += " afa.cantidad_afact as cantidad,      ";
		string += " afa.observaciones_afact as componentes,   ";
		string += " afa.serie_afact as serie,   ";
		string += " afa.marca_afact as marca,   ";
		string += " afa.modelo_afact as modelo,   ";
		string += " afa.color_afact as color,   ";
		string += " afa.chasis_afact as chasis,   ";
		string += " afa.motor_afact as motor,   ";
		string += " afa.placa_afact as placa,  ";
		string += " est.detalle_afest as estado,   ";
		string += " ubi.detalle_afubi as ubicacion, ";
		string += " afa.afi_ubicacion_otro as referencia_ubicacion,";
		string += " CASE WHEN afa.afi_ultima_acta IS NOT NULL THEN (SELECT COUNT(h.ide_gtemp) FROM afi_doc_detalle_custodio h WHERE h.recibe_afddc = true AND h.ide_afdoc=afa.afi_ultima_acta) ELSE 0 END AS numero_custodios,";
		string += " array_to_string(array(  ";
		string += " SELECT documento_identidad_gtemp ||' '|| primer_nombre_gtemp||' '|| segundo_nombre_gtemp||' '||       apellido_paterno_gtemp||' '|| apellido_materno_gtemp       FROM afi_doc_detalle_custodio addc  ";
		string += " LEFT JOIN gth_empleado gem  ON gem.ide_gtemp=addc.ide_gtemp       where  addc.recibe_afddc = true and addc.ide_afdoc=doc.ide_afdoc and addc.ide_afdoc = afa.afi_ultima_acta       ), ', ') AS nombres_del_o_los_custodios,  ";
		string += " afa.valor_neto_afact as valor_sin_impuestos,";
		string += " afa.valor_iva as iva,";
		string += " afa.valor_inc_iva_afact as valor_con_impuestos,";
		string += " afa.valor_compra_afact as valor_compra,";
		string += " afa.fecha_reavaluo_afact as fecha_revaluo_perito,   ";
		string += " afa.valor_residual_afact as valor_residual_perito,  ";
		string += " afa.valor_revaluo_afact as valor_revaluo_comercial_perito,   ";
		string += " afa.valor_realizacion_afact as valor_realizacion_perito,";
		string += " afa.vida_util_afact as vida_util_afact, ";
		string += " doc.fecha_afdoc as fecha_ultima_acta,       ";
		string += " doc.nro_secuencial_afdoc as numero_ultima_acta,";
		string += " afa.afi_ultima_acta as ultima_acta_erp,   ";
		string += " tdoc.detalle_aftidoc as tipo_ultima_acta,     ";
		string += " doc.detalle_afdoc as observacion_ultima_acta, ";
		string += " afa.novedad_afact as novedad_del_bien,   ";
		string += " afa.razon_baja_afact as razon_baja,  ";
		string += " afa.fecha_baja_afact as fecha_baja,   ";
		// Ticket #714154
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2015' THEN 'N/A' ELSE";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 7)THEN 'SI' ELSE 'NO' END) END AS constatacion_2015,";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2016' THEN 'N/A' ELSE";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 8)THEN 'SI' ELSE 'NO' END)END AS constatacion_2016,";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2017' THEN 'N/A' ELSE";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 10)THEN 'SI' ELSE 'NO' END)END AS constatacion_2017,";
		// Ticket #517883 Estimados favor su ayuda en la columna de CONSTATACIÓN
		// 2018, que los bienes que se detalla a continuación aparezca lo
		// siguiente: “NO PROCEDE” a
		// (5551,5552,5553,5554,5555,5557,5558,5559,5560,5561,5562,5563,5564,5565,5566,5567,5568,5569,5570,5571,5572,5573,5574,5575,5576,5577,5578,5579,5580,5581,5582,5584,5592,5593,5481,5480,5479,5478,5477,5476,5475,5474,5473,5472,5471,5470,5469,5468,5467,5466,5458,5457,5456,5455,5453,5452,5451,5420,5417,5304,5303,5166,5165,3123,3122,2718,2717,2716,2715,2710,2697,2142,3561,3539,2494,2052)
		string += "  CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2018' THEN 'N/A' ELSE ";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 11) THEN 'SI' ";
		string += "  WHEN afa.ide_afact IN (5551,5552,5553,5554,5555,5557,5558,5559,5560,5561,5562,5563,5564,5565,5566,5567,5568,5569,5570,5571,5572,5573,5574,5575,5576,5577,5578,5579,5580,5581,5582,5584,5592,5593,5481,5480,5479,5478,5477,5476,5475,5474,5473,5472,5471,5470,5469,5468,5467,5466,5458,5457,5456,5455,5453,5452,5451,5420,5417,5304,5303,5166,5165,3123,3122,2718,2717,2716,2715,2710,2697,2142,3561,3539,2494,2052) THEN 'NO PROCEDE'";
		string += " ELSE 'NO' END)END AS constatacion_2018,  ";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2019' THEN 'N/A' ELSE ";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 12)THEN 'SI' ELSE 'NO' END)END AS constatacion_2019, ";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2020' THEN 'N/A' ELSE ";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 12)THEN 'SI' ELSE 'NO' END)END AS constatacion_2020, ";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2021' THEN 'N/A' ELSE ";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 12)THEN 'SI' ELSE 'NO' END)END AS constatacion_2021, ";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2022' THEN 'N/A' ELSE ";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 12)THEN 'SI' ELSE 'NO' END)END AS constatacion_2022, ";

		string += " cons.fecha_afdoc as fecha_ultima_constatacion,       ";
		string += " afa.afi_ultima_acta_consta as acta_ultima_constacion_erp,  ";
		string += " tcons.detalle_aftidoc as tipo_acta_constatacion,   ";
		string += " cons.nro_secuencial_afdoc as numero_ultima_constatacion,";
		string += " cons.detalle_afdoc as observacion_ultima_constatacion  ";
		string += " ,CASE WHEN afi_intangible = TRUE THEN 'SI' ELSE 'NO' END AS intangible";
		// Ticket #174553
		string += " , afa.valor_depreciacion_afact, valor_en_libros,";
		string += " fecha_reavaluo_afper, edad_afper, vida_util_afper, vida_util_restante_afper, factor_afper, valor_revaluo_comercial_afper, valor_realizacion_afper, valor_residual_afper ";

		string += " FROM afi_activo afa       LEFT JOIN bodt_catalogo_material AS bcm ON afa.ide_bocam = bcm.ide_bocam       LEFT JOIN afi_ubicacion AS ubi ON afa.ide_afubi = ubi.ide_afubi  ";
		string += " LEFT JOIN bodt_bodega AS ing ON afa.ide_bobod = ing.ide_bobod        LEFT JOIN tes_proveedor	AS prov ON afa.ide_tepro = prov.ide_tepro       LEFT JOIN afi_tipo_propiedad AS tip ON afa.ide_aftip = tip.ide_aftip  ";
		string += " LEFT JOIN afi_tipo_activo AS atip ON afa.ide_aftia = atip.ide_aftia       LEFT JOIN afi_estado AS est ON afa.ide_afest = est.ide_afest       LEFT JOIN afi_docu AS doc ON afa.afi_ultima_acta = doc.ide_afdoc  ";
		string += " LEFT JOIN afi_tipo_docu AS tdoc ON doc.ide_aftidoc = tdoc.ide_aftidoc       LEFT JOIN afi_docu AS cons ON afa.afi_ultima_acta_consta = cons.ide_afdoc       LEFT JOIN afi_tipo_docu AS tcons ON cons.ide_aftidoc = tcons.ide_aftidoc  ";
		string += " LEFT JOIN perito ON afa.ide_afact = perito.ide_afact ";
		string += " LEFT JOIN afi_poa ON afa.ide_afact = afi_poa.ide_afact ";
		string += " WHERE tdoc.saca_del_inventario_empresarial is not TRUE";
		string += " ORDER BY 1 DESC";
		return string;
	}

	public String getHerramientasEstado() {
		String query = "";
		query += " SELECT ";
		query += " 	ide_afact as codigo,";
		query += " 	b.cat_codigo_bocam as item_presupuestario,";
		query += " 	b.descripcion_bocam as nombre_catalogo,";
		query += " 	c.detalle_boubi as bodega, ";
		query += " 	case when afi_prestado then 'PRESTADO' else 'EN BODEGA' end as accion , ";
		query += " 	detalle_afact as descripcion_caracteristicas,";
		query += " 	observaciones_afact as componentes,";
		query += " 	color_afact as color,  ";
		query += " 	marca_afact as marca, ";
		query += " 	serie_afact as serie, ";
		query += " 	modelo_afact as modelo ";
		query += " FROM afi_activo a ";
		query += " 	left join bodt_catalogo_material b ON a.ide_bocam = b.ide_bocam ";
		query += " 	LEFT JOIN bodt_bodega_ubicacion c on a.ide_boubi=c.ide_boubi ";
		query += " WHERE afi_prestado in (true,false) order by 4, 5";
		return query;
	}

	public TablaGenerica getCampo(String ide_afact) {
		TablaGenerica tab_activo = utilitario.consultar(
				"SELECT ide_afact,ide_afubi, afi_ubicacion_otro FROM afi_activo WHERE ide_afact=" + ide_afact + ";");
		return tab_activo;
	}

	public TablaGenerica getDetalleCustodiosActa(String ide_afdoc) {
		TablaGenerica tab_activo = utilitario.consultar(
				"SELECT ide_afddc, ide_gtemp, ide_afdoc, recibe_afddc, activo_afdda, usuario_ingre, fecha_ingre, hora_ingre, usuario_actua, fecha_actua, hora_actua, ide_empre_res FROM afi_doc_detalle_custodio WHERE ide_afdoc ="
						+ ide_afdoc + ";");
		return tab_activo;
	}

	public TablaGenerica getDetalleActivoActa(String ide_afdoc) {
		TablaGenerica tab_activo = utilitario.consultar(
				"SELECT ide_afdda, ide_afact, ide_afdoc, activo_afdda, usuario_ingre, fecha_ingre, hora_ingre, usuario_actua, fecha_actua, hora_actua,  ide_afubi, afi_ubicacion_otro FROM afi_doc_detalle_activo WHERE ide_afdoc ="
						+ ide_afdoc + ";");
		return tab_activo;
	}

	public boolean generaActaEntregaRecepcionDeConstatacion(String ide_afdoc, String secuencial) {
		System.out.println("generaActaEntregaRecepcionDeConstatacion " + ide_afdoc);
		// CABECERA
		String query = "INSERT INTO afi_docu( ide_afdoc, ide_aftidoc, ide_geani, ide_gtemp, ide_gtemp_aut, nro_secuencial_afdoc, detalle_afdoc, fecha_afdoc, activo_afdoc, usuario_ingre, fecha_ingre, hora_ingre, usuario_actua, fecha_actua, hora_actua, ide_boubi, aprobacion_adm, ide_gtemp_jef_aut, rec_ide_afdoc)"
				+ "SELECT (SELECT MAX(ide_afdoc)+1 FROM afi_docu), 15 AS ide_aftidoc, ide_geani, ide_gtemp, ide_gtemp_aut,"
				+ secuencial + "AS nro_secuencial_afdoc, 'GENERADO POR CONSTATACIÓN CÓDIGO ERP " + ide_afdoc
				+ "' AS detalle_afdoc, fecha_afdoc, activo_afdoc,   usuario_ingre, fecha_ingre, hora_ingre, usuario_actua, fecha_actua, hora_actua, ide_boubi, aprobacion_adm, ide_gtemp_jef_aut,"
				+ ide_afdoc + " AS rec_ide_afdoc FROM afi_docu WHERE ide_afdoc= " + ide_afdoc + ";";

		// System.out.println(query);
		utilitario.getConexion().ejecutarSql(query);
		TablaGenerica tabActa = this.getActaEntregaRecepcionGenerada(ide_afdoc);
		// System.out.println("tabActa.getTotalFilas()" +
		// tabActa.getTotalFilas());
		// DETALLE ACTIVOS
		TablaGenerica tab_det_activos = this.getDetalleActivoActa(ide_afdoc);
		String queryActivos = "";
		String queryCustodios = "";
		for (int i = 0; i < tab_det_activos.getTotalFilas(); i++) {
			tab_det_activos.setFilaActual(i);
			queryActivos = "INSERT INTO afi_doc_detalle_activo(";
			queryActivos += " ide_afdda, ";
			queryActivos += " ide_afact, ";
			queryActivos += " ide_afdoc, ";
			queryActivos += " activo_afdda,";
			queryActivos += " usuario_ingre, ";
			queryActivos += " fecha_ingre, ";
			queryActivos += " hora_ingre, ";
			queryActivos += " usuario_actua,";
			queryActivos += " fecha_actua, ";
			queryActivos += " hora_actua, ";
			queryActivos += " ide_afubi, ";
			queryActivos += " afi_ubicacion_otro)";
			queryActivos += " VALUES ((SELECT MAX(ide_afdda)+1 FROM afi_doc_detalle_activo),";
			queryActivos += tab_det_activos.getValor("ide_afact") + ",";
			queryActivos += tabActa.getValor("ide_afdoc") + ",";
			queryActivos += tab_det_activos.getValor("activo_afdda") + ",";
			queryActivos += "'" + tab_det_activos.getValor("usuario_ingre") + "',";
			queryActivos += "'" + tab_det_activos.getValor("fecha_ingre") + "',";
			queryActivos += "'" + tab_det_activos.getValor("hora_ingre") + "',";
			queryActivos += "'" + tab_det_activos.getValor("usuario_actua") + "',";
			queryActivos += (tab_det_activos.getValor("fecha_actua") == null) ? "null,"
					: "'" + tab_det_activos.getValor("fecha_actua") + "',";
			queryActivos += (tab_det_activos.getValor("hora_actua") == null) ? "null,"
					: "'" + tab_det_activos.getValor("hora_actua") + "',";
			queryActivos += tab_det_activos.getValor("ide_afubi") + ",";
			queryActivos += (tab_det_activos.getValor("afi_ubicacion_otro") == null) ? "''"
					: "'" + tab_det_activos.getValor("afi_ubicacion_otro") + "'";
			queryActivos += " );";
			// System.out.println(queryActivos);
			utilitario.getConexion().ejecutarSql(queryActivos);
		}

		// DETALLE CUSTODIOS
		TablaGenerica tab_det_custodios = this.getDetalleCustodiosActa(ide_afdoc);
		for (int j = 0; j < tab_det_custodios.getTotalFilas(); j++) {
			tab_det_custodios.setFilaActual(j);
			queryCustodios = "";
			queryCustodios += " INSERT INTO afi_doc_detalle_custodio(";
			queryCustodios += " ide_afddc, ";
			queryCustodios += " ide_gtemp, ";
			queryCustodios += " ide_afdoc, ";
			queryCustodios += " recibe_afddc,";
			queryCustodios += " activo_afdda, ";
			queryCustodios += " usuario_ingre,";
			queryCustodios += " fecha_ingre, ";
			queryCustodios += " hora_ingre, ";
			queryCustodios += " usuario_actua,";
			queryCustodios += " fecha_actua, ";
			queryCustodios += " hora_actua, ide_empre_res)";
			queryCustodios += " VALUES ((SELECT MAX(ide_afddc)+1 FROM afi_doc_detalle_custodio),";
			queryCustodios += tab_det_custodios.getValor("ide_gtemp") + ",";
			queryCustodios += tabActa.getValor("ide_afdoc") + ",";
			queryCustodios += tab_det_custodios.getValor("recibe_afddc") + ",";
			queryCustodios += tab_det_custodios.getValor("activo_afdda") + ",";
			queryCustodios += "'" + tab_det_custodios.getValor("usuario_ingre") + "',";
			queryCustodios += "'" + tab_det_custodios.getValor("fecha_ingre") + "',";
			queryCustodios += "'" + tab_det_custodios.getValor("hora_ingre") + "',";
			queryCustodios += "'" + tab_det_custodios.getValor("usuario_actua") + "',";
			queryCustodios += (tab_det_custodios.getValor("fecha_actua") == null) ? "null,"
					: "'" + tab_det_custodios.getValor("fecha_actua") + "',";
			queryCustodios += (tab_det_custodios.getValor("hora_actua") == null) ? "null,"
					: "'" + tab_det_custodios.getValor("hora_actua") + "',";
			queryCustodios += tab_det_custodios.getValor("ide_empre_res");
			queryCustodios += " );";
			System.out.println(queryCustodios);
			utilitario.getConexion().ejecutarSql(queryCustodios);
		}

		return true;
	}

	public void actualizarActaERGenerada(String ide_afdoc) {
		utilitario.getConexion()
				.ejecutarSql("UPDATE afi_activo SET afi_ultima_acta=" + ide_afdoc
						+ " WHERE ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo WHERE ide_afdoc="
						+ ide_afdoc + ")");
	}

	public TablaGenerica getActaEntregaRecepcionGenerada(String ide_afdoc) {
		TablaGenerica tab_activo = utilitario.consultar(
				"SELECT ide_afdoc, ide_aftidoc, nro_secuencial_afdoc, detalle_afdoc, fecha_afdoc, activo_afdoc, ide_boubi, aprobacion_adm, ide_gtemp_jef_aut, rec_ide_afdoc  FROM afi_docu WHERE rec_ide_afdoc ="
						+ ide_afdoc + " ;");
		return tab_activo;
	}

	public String getTipoActas() {
		return "SELECT ide_aftidoc,ide_aftidoc as identificador, detalle_aftidoc  FROM afi_tipo_docu;";
	}

	public String getUbicaciones() {
		return "SELECT ide_afubi,detalle_afubi FROM afi_ubicacion";
	}

	public String updateUbicacionBienesDeActa(String ide_afdoc, String ide_afubi) {
		return "UPDATE afi_activo SET ide_afubi=" + ide_afubi
				+ " WHERE ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo WHERE ide_afdoc IN(" + ide_afdoc
				+ "));";

	}

	public TablaGenerica getAreaActa(String ide_docu) {
		String sql = "";
		sql += " SELECT ";
		sql += " 	DISTINCT emp.ide_gtemp,";
		sql += " 	doc.ide_geani,";
		sql += " 	emp.apellido_paterno_gtemp,";
		sql += " 	(case when emp.apellido_materno_gtemp is null then '' else emp.apellido_materno_gtemp end) as apellido_materno_gtemp,";
		sql += " 	emp.primer_nombre_gtemp,";
		sql += " 	(case when emp.segundo_nombre_gtemp is null then '' else emp.segundo_nombre_gtemp end) as segundo_nombre_gtemp,";
		sql += " 	emp.documento_identidad_gtemp,";
		sql += " 	(select count(*) as numCust from afi_doc_detalle_custodio where activo_afdda = TRUE AND recibe_afddc = FALSE AND ide_afdoc = "
				+ ide_docu + "),";
		sql += " 	cargo.detalle_gecaf,";
		sql += " 	cargo.ide_geare,";
		sql += " 	cargo.detalle_geare";
		sql += " FROM";
		sql += " afi_docu doc LEFT JOIN afi_doc_detalle_custodio cusdoc ON doc.ide_afdoc = cusdoc.ide_afdoc";
		sql += " 	     LEFT JOIN gth_empleado emp ON emp.ide_gtemp = cusdoc.ide_gtemp";
		sql += " 	     LEFT JOIN (select  ide_gtemp,";
		sql += " 				detalle_gecaf,";
		sql += " 				b.ide_geare,";
		sql += " 				detalle_geare";
		sql += " 			from 	gen_empleados_departamento_par a";
		sql += " 				left join gen_area b on a.ide_geare = b.ide_geare";
		sql += " 				left join gen_cargo_funcional c on a.ide_gecaf = c.ide_gecaf";
		sql += " 			where coalesce(a.activo_geedp,false) in (true,false) and ide_geedp in (select max(ide_geedp) as ide_geedp from gen_empleados_departamento_par group by ide_gtemp )) cargo";
		sql += " 			ON cargo.ide_gtemp = emp.ide_gtemp";
		sql += " ";
		sql += " WHERE";
		sql += " cusdoc.recibe_afddc = TRUE AND";
		sql += " doc.ide_afdoc = " + ide_docu + "";
		sql += " ORDER BY ide_gtemp DESC;";
		TablaGenerica tab_area = utilitario.consultar(sql);
		return tab_area;
	}

	public String getConsultaCustodiosYBienesUnicoRegitro(String ide_aftidoc) {
		String string;
		string = " SELECT ";
		string += " afa.ide_afact as codigo,   ";
		string += " afa.cod_anterior_afact as codigo_anterior,  ";
		string += " prov.ruc_tepro as proveedor_ruc,   ";
		string += " prov.nombre_tepro as proveedor_nombre,   ";
		string += " prov.nom_representante_tepro as proveedor_representante,   ";
		string += " ing.fecha_ingreso_bobod as ingreso_fecha_ingreso,    ";
		string += " afa.fecha_alta_afact as fecha_alta, ";
		string += " CASE WHEN ing.numero_ingreso_bobod = 0 THEN afa.nro_factura_afact ELSE ing.num_factura_bobod END AS factura_numero, ";
		string += " ing.fecha_compra_bobod as ingreso_fecha_factura, ";
		string += " ing.num_factura_anterior_bobod as factura_anterior,";
		string += " tip.detalle_aftip as forma_ingreso, ";
		string += " ing.numero_ingreso_bobod as ingreso_numero,";
		string += " ing.novedad_bobod as ingreso_novedad,";
		string += " CASE WHEN ing.numero_ingreso_bobod = 0 THEN 'INGRESO ANTIGUO' ELSE ing.descripcion_bobod END AS ingreso_observacion,";
		string += " afa.ide_bocam as codigo_catalogo,  ";
		string += " bcm.descripcion_bocam as nombre_catalogo,";
		string += " bcm.cat_codigo_bocam as item_presupuestario,  ";
		string += " atip.detalle_aftia as tipo_activo,  ";
		string += " afa.detalle_afact as descripcion_caracteristicas,";
		string += " afa.cantidad_afact as cantidad,      ";
		string += " afa.observaciones_afact as componentes,   ";
		string += " afa.serie_afact as serie,   ";
		string += " afa.marca_afact as marca,   ";
		string += " afa.modelo_afact as modelo,   ";
		string += " afa.color_afact as color,   ";
		string += " afa.chasis_afact as chasis,   ";
		string += " afa.motor_afact as motor,   ";
		string += " afa.placa_afact as placa,  ";
		string += " est.detalle_afest as estado,   ";
		string += " ubi.detalle_afubi as ubicacion, ";
		string += " afa.afi_ubicacion_otro as referencia_ubicacion,";
		string += " CASE WHEN afa.afi_ultima_acta IS NOT NULL THEN (SELECT COUNT(h.ide_gtemp) FROM afi_doc_detalle_custodio h WHERE h.recibe_afddc = true AND h.ide_afdoc=afa.afi_ultima_acta) ELSE 0 END AS numero_custodios,";
		string += " array_to_string(array(  ";
		string += " SELECT documento_identidad_gtemp ||' '|| primer_nombre_gtemp||' '|| segundo_nombre_gtemp||' '||       apellido_paterno_gtemp||' '|| apellido_materno_gtemp       FROM afi_doc_detalle_custodio addc  ";
		string += " LEFT JOIN gth_empleado gem  ON gem.ide_gtemp=addc.ide_gtemp       where  addc.recibe_afddc = true and addc.ide_afdoc=doc.ide_afdoc and addc.ide_afdoc = afa.afi_ultima_acta       ), ', ') AS nombres_del_o_los_custodios,  ";
		string += " afa.valor_neto_afact as valor_sin_impuestos,";
		string += " afa.valor_iva as iva,";
		string += " afa.valor_compra_afact as valor_con_impuestos,";
		string += " afa.fecha_reavaluo_afact as fecha_revaluo_perito,   ";
		string += " afa.valor_residual_afact as valor_residual_perito,  ";
		string += " afa.valor_revaluo_afact as valor_revaluo_comercial_perito,   ";
		string += " afa.valor_realizacion_afact as valor_realizacion_perito,";
		string += " doc.fecha_afdoc as fecha_ultima_acta,       ";
		string += " doc.nro_secuencial_afdoc as numero_ultima_acta,";
		string += " afa.afi_ultima_acta as ultima_acta_erp,   ";
		string += " tdoc.detalle_aftidoc as tipo_ultima_acta,     ";
		string += " doc.detalle_afdoc as observacion_ultima_acta, ";
		string += " afa.novedad_afact as novedad_del_bien,   ";
		string += " afa.razon_baja_afact as razon_baja,  ";
		string += " afa.fecha_baja_afact as fecha_baja,   ";

		// Ticket #714154
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2015' THEN 'N/A' ELSE";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 7)THEN 'SI' ELSE 'NO' END) END AS constatacion_2015,";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2016' THEN 'N/A' ELSE";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 8)THEN 'SI' ELSE 'NO' END)END AS constatacion_2016,";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2017' THEN 'N/A' ELSE";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 10)THEN 'SI' ELSE 'NO' END)END AS constatacion_2017,";
		// Ticket #517883 Estimados favor su ayuda en la columna de CONSTATACIÓN
		// 2018, que los bienes que se detalla a continuación aparezca lo
		// siguiente: “NO PROCEDE” a
		// (5551,5552,5553,5554,5555,5557,5558,5559,5560,5561,5562,5563,5564,5565,5566,5567,5568,5569,5570,5571,5572,5573,5574,5575,5576,5577,5578,5579,5580,5581,5582,5584,5592,5593,5481,5480,5479,5478,5477,5476,5475,5474,5473,5472,5471,5470,5469,5468,5467,5466,5458,5457,5456,5455,5453,5452,5451,5420,5417,5304,5303,5166,5165,3123,3122,2718,2717,2716,2715,2710,2697,2142,3561,3539,2494,2052)
		string += "  CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2018' THEN 'N/A' ELSE ";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 11) THEN 'SI' ";
		string += "  WHEN afa.ide_afact IN (5551,5552,5553,5554,5555,5557,5558,5559,5560,5561,5562,5563,5564,5565,5566,5567,5568,5569,5570,5571,5572,5573,5574,5575,5576,5577,5578,5579,5580,5581,5582,5584,5592,5593,5481,5480,5479,5478,5477,5476,5475,5474,5473,5472,5471,5470,5469,5468,5467,5466,5458,5457,5456,5455,5453,5452,5451,5420,5417,5304,5303,5166,5165,3123,3122,2718,2717,2716,2715,2710,2697,2142,3561,3539,2494,2052) THEN 'NO PROCEDE'";
		string += " ELSE 'NO' END)END AS constatacion_2018,  ";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2019' THEN 'N/A' ELSE ";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 12)THEN 'SI' ELSE 'NO' END)END AS constatacion_2019, ";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2020' THEN 'N/A' ELSE ";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 12)THEN 'SI' ELSE 'NO' END)END AS constatacion_2020, ";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2021' THEN 'N/A' ELSE ";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 12)THEN 'SI' ELSE 'NO' END)END AS constatacion_2021, ";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2022' THEN 'N/A' ELSE ";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 12)THEN 'SI' ELSE 'NO' END)END AS constatacion_2022, ";

		string += " cons.fecha_afdoc as fecha_ultima_constatacion,       ";
		string += " afa.afi_ultima_acta_consta as acta_ultima_constacion_erp,  ";
		string += " tcons.detalle_aftidoc as tipo_acta_constatacion,   ";
		string += " cons.nro_secuencial_afdoc as numero_ultima_constatacion,";
		string += " cons.detalle_afdoc as observacion_ultima_constatacion  ";
		string += " ,CASE WHEN afi_intangible = TRUE THEN 'SI' ELSE 'NO' END AS intangible";
		string += " FROM afi_activo afa       LEFT JOIN bodt_catalogo_material AS bcm ON afa.ide_bocam = bcm.ide_bocam       LEFT JOIN afi_ubicacion AS ubi ON afa.ide_afubi = ubi.ide_afubi  ";
		string += " LEFT JOIN bodt_bodega AS ing ON afa.ide_bobod = ing.ide_bobod        LEFT JOIN tes_proveedor	AS prov ON afa.ide_tepro = prov.ide_tepro       LEFT JOIN afi_tipo_propiedad AS tip ON afa.ide_aftip = tip.ide_aftip  ";
		string += " LEFT JOIN afi_tipo_activo AS atip ON afa.ide_aftia = atip.ide_aftia       LEFT JOIN afi_estado AS est ON afa.ide_afest = est.ide_afest       LEFT JOIN afi_docu AS doc ON afa.afi_ultima_acta = doc.ide_afdoc  ";
		string += " LEFT JOIN afi_tipo_docu AS tdoc ON doc.ide_aftidoc = tdoc.ide_aftidoc       LEFT JOIN afi_docu AS cons ON afa.afi_ultima_acta_consta = cons.ide_afdoc       LEFT JOIN afi_tipo_docu AS tcons ON cons.ide_aftidoc = tcons.ide_aftidoc  ";
		string += " WHERE doc.ide_aftidoc IN (" + ide_aftidoc + ") ORDER BY 1 DESC";
		return string;
	}

	public String getConsultaCustodiosYBienesUnicoRegitro() {
		String string;
		string = " SELECT ";
		string += " afa.ide_afact as codigo,   ";
		string += " afa.cod_anterior_afact as codigo_anterior,  ";
		string += " prov.ruc_tepro as proveedor_ruc,   ";
		string += " prov.nombre_tepro as proveedor_nombre,   ";
		string += " prov.nom_representante_tepro as proveedor_representante,   ";
		string += " ing.fecha_ingreso_bobod as ingreso_fecha_ingreso,    ";
		string += " afa.fecha_alta_afact as fecha_alta, ";
		string += " CASE WHEN ing.numero_ingreso_bobod = 0 THEN afa.nro_factura_afact ELSE ing.num_factura_bobod END AS factura_numero, ";
		string += " ing.fecha_compra_bobod as ingreso_fecha_factura, ";
		string += " ing.num_factura_anterior_bobod as factura_anterior,";
		string += " tip.detalle_aftip as forma_ingreso, ";
		string += " ing.numero_ingreso_bobod as ingreso_numero,";
		string += " ing.novedad_bobod as ingreso_novedad,";
		string += " CASE WHEN ing.numero_ingreso_bobod = 0 THEN 'INGRESO ANTIGUO' ELSE ing.descripcion_bobod END AS ingreso_observacion,";
		string += " afa.ide_bocam as codigo_catalogo,  ";
		string += " bcm.descripcion_bocam as nombre_catalogo,";
		string += " bcm.cat_codigo_bocam as item_presupuestario,  ";
		string += " atip.detalle_aftia as tipo_activo,  ";
		string += " afa.detalle_afact as descripcion_caracteristicas,";
		string += " afa.cantidad_afact as cantidad,      ";
		string += " afa.observaciones_afact as componentes,   ";
		string += " afa.serie_afact as serie,   ";
		string += " afa.marca_afact as marca,   ";
		string += " afa.modelo_afact as modelo,   ";
		string += " afa.color_afact as color,   ";
		string += " afa.chasis_afact as chasis,   ";
		string += " afa.motor_afact as motor,   ";
		string += " afa.placa_afact as placa,  ";
		string += " est.detalle_afest as estado,   ";
		string += " ubi.detalle_afubi as ubicacion, ";
		string += " afa.afi_ubicacion_otro as referencia_ubicacion,";
		string += " CASE WHEN afa.afi_ultima_acta IS NOT NULL THEN (SELECT COUNT(h.ide_gtemp) FROM afi_doc_detalle_custodio h WHERE h.recibe_afddc = true AND h.ide_afdoc=afa.afi_ultima_acta) ELSE 0 END AS numero_custodios,";
		string += " array_to_string(array(  ";
		string += " SELECT documento_identidad_gtemp ||' '|| primer_nombre_gtemp||' '|| segundo_nombre_gtemp||' '||       apellido_paterno_gtemp||' '|| apellido_materno_gtemp       FROM afi_doc_detalle_custodio addc  ";
		string += " LEFT JOIN gth_empleado gem  ON gem.ide_gtemp=addc.ide_gtemp       where  addc.recibe_afddc = true and addc.ide_afdoc=doc.ide_afdoc and addc.ide_afdoc = afa.afi_ultima_acta       ), ', ') AS nombres_del_o_los_custodios,  ";
		string += " afa.valor_neto_afact as valor_sin_impuestos,";
		string += " afa.valor_iva as iva,";
		string += " afa.valor_compra_afact as valor_con_impuestos,";
		string += " afa.fecha_reavaluo_afact as fecha_revaluo_perito,   ";
		string += " afa.valor_residual_afact as valor_residual_perito,  ";
		string += " afa.valor_revaluo_afact as valor_revaluo_comercial_perito,   ";
		string += " afa.valor_realizacion_afact as valor_realizacion_perito,";
		string += " doc.fecha_afdoc as fecha_ultima_acta,       ";
		string += " doc.nro_secuencial_afdoc as numero_ultima_acta,";
		string += " afa.afi_ultima_acta as ultima_acta_erp,   ";
		string += " tdoc.detalle_aftidoc as tipo_ultima_acta,     ";
		string += " doc.detalle_afdoc as observacion_ultima_acta, ";
		string += " afa.novedad_afact as novedad_del_bien,   ";
		string += " afa.razon_baja_afact as razon_baja,  ";
		string += " afa.fecha_baja_afact as fecha_baja,   ";
		// Ticket #714154
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2015' THEN 'N/A' ELSE";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 7)THEN 'SI' ELSE 'NO' END) END AS constatacion_2015,";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2016' THEN 'N/A' ELSE";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 8)THEN 'SI' ELSE 'NO' END)END AS constatacion_2016,";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2017' THEN 'N/A' ELSE";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 10)THEN 'SI' ELSE 'NO' END)END AS constatacion_2017,";
		// Ticket #517883 Estimados favor su ayuda en la columna de CONSTATACIÓN
		// 2018, que los bienes que se detalla a continuación aparezca lo
		// siguiente: “NO PROCEDE” a
		// (5551,5552,5553,5554,5555,5557,5558,5559,5560,5561,5562,5563,5564,5565,5566,5567,5568,5569,5570,5571,5572,5573,5574,5575,5576,5577,5578,5579,5580,5581,5582,5584,5592,5593,5481,5480,5479,5478,5477,5476,5475,5474,5473,5472,5471,5470,5469,5468,5467,5466,5458,5457,5456,5455,5453,5452,5451,5420,5417,5304,5303,5166,5165,3123,3122,2718,2717,2716,2715,2710,2697,2142,3561,3539,2494,2052)
		string += "  CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2018' THEN 'N/A' ELSE ";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 11) THEN 'SI' ";
		string += "  WHEN afa.ide_afact IN (5551,5552,5553,5554,5555,5557,5558,5559,5560,5561,5562,5563,5564,5565,5566,5567,5568,5569,5570,5571,5572,5573,5574,5575,5576,5577,5578,5579,5580,5581,5582,5584,5592,5593,5481,5480,5479,5478,5477,5476,5475,5474,5473,5472,5471,5470,5469,5468,5467,5466,5458,5457,5456,5455,5453,5452,5451,5420,5417,5304,5303,5166,5165,3123,3122,2718,2717,2716,2715,2710,2697,2142,3561,3539,2494,2052) THEN 'NO PROCEDE'";
		string += " ELSE 'NO' END)END AS constatacion_2018,  ";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2019' THEN 'N/A' ELSE ";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 12)THEN 'SI' ELSE 'NO' END)END AS constatacion_2019, ";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2020' THEN 'N/A' ELSE ";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 12)THEN 'SI' ELSE 'NO' END)END AS constatacion_2020, ";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2021' THEN 'N/A' ELSE ";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 12)THEN 'SI' ELSE 'NO' END)END AS constatacion_2021, ";
		string += " CASE WHEN EXTRACT(YEAR FROM afa.fecha_alta_afact) > '2022' THEN 'N/A' ELSE ";
		string += " (CASE WHEN afa.ide_afact IN (SELECT ide_afact FROM afi_doc_detalle_activo da LEFT JOIN afi_docu d ON da.ide_afdoc = d.ide_afdoc WHERE d.ide_aftidoc=3 AND d.ide_geani = 12)THEN 'SI' ELSE 'NO' END)END AS constatacion_2022, ";

		string += " cons.fecha_afdoc as fecha_ultima_constatacion,       ";
		string += " afa.afi_ultima_acta_consta as acta_ultima_constacion_erp,  ";
		string += " tcons.detalle_aftidoc as tipo_acta_constatacion,   ";
		string += " cons.nro_secuencial_afdoc as numero_ultima_constatacion,";
		string += " cons.detalle_afdoc as observacion_ultima_constatacion  ";
		string += " ,CASE WHEN afi_intangible = TRUE THEN 'SI' ELSE 'NO' END AS intangible";
		string += " FROM afi_activo afa       LEFT JOIN bodt_catalogo_material AS bcm ON afa.ide_bocam = bcm.ide_bocam       LEFT JOIN afi_ubicacion AS ubi ON afa.ide_afubi = ubi.ide_afubi  ";
		string += " LEFT JOIN bodt_bodega AS ing ON afa.ide_bobod = ing.ide_bobod        LEFT JOIN tes_proveedor	AS prov ON afa.ide_tepro = prov.ide_tepro       LEFT JOIN afi_tipo_propiedad AS tip ON afa.ide_aftip = tip.ide_aftip  ";
		string += " LEFT JOIN afi_tipo_activo AS atip ON afa.ide_aftia = atip.ide_aftia       LEFT JOIN afi_estado AS est ON afa.ide_afest = est.ide_afest       LEFT JOIN afi_docu AS doc ON afa.afi_ultima_acta = doc.ide_afdoc  ";
		string += " LEFT JOIN afi_tipo_docu AS tdoc ON doc.ide_aftidoc = tdoc.ide_aftidoc       LEFT JOIN afi_docu AS cons ON afa.afi_ultima_acta_consta = cons.ide_afdoc       LEFT JOIN afi_tipo_docu AS tcons ON cons.ide_aftidoc = tcons.ide_aftidoc  ";
		string += " ORDER BY 1 DESC";
		return string;
	}

	public String getEmpresaResponsables() {
		return "SELECT 	afr.ide_empre_res, afr.identificacion_empre_responsable, afr.detalle_empre_responsable, afr.cargo_empre_responsable, afe.detalle_empre, afe.identificacion_empre FROM afi_empresa_responsable afr LEFT JOIN afi_empresa afe ON afr.ide_empre = afe.ide_empre";
	}
	
	public String getDescripcionHerramienta() {
		return "select ide_afher,codigo_afher as codigo,descripcion_afher as detalle_afact, marca_afher as marca_afact, color_afher as color_afact,ide_afest,serie_afher as serie_afact from afi_herramienta;";
	}

	public String getDescripcionActivo() {
		return "SELECT ide_afact, ide_afact as codigo,CASE WHEN a.ide_bocam IS NULL THEN nac.detalle_afnoa ELSE b.descripcion_bocam END AS descripcion_bocam, detalle_afact,observaciones_afact,color_afact,  marca_afact, serie_afact, modelo_afact,  chasis_afact, motor_afact,cod_anterior_afact FROM afi_activo a LEFT JOIN bodt_catalogo_material b ON a.ide_bocam = b.ide_bocam LEFT JOIN afi_nombre_activo nac ON a.ide_afnoa=nac.ide_afnoa";
	}

	public String getActivosPorCustodio(String ide_gtemp) {
		return "SELECT act.ide_afact, act.ide_afact as identificador_activo,CASE WHEN act.ide_bocam IS NULL THEN nac.detalle_afnoa ELSE bcm.descripcion_bocam END AS descripcion_bocam, detalle_afact,observaciones_afact,color_afact,  marca_afact, serie_afact, modelo_afact,  chasis_afact, motor_afact,cod_anterior_afact FROM 	afi_docu AS doc LEFT JOIN afi_doc_detalle_custodio AS doccus ON doc.ide_afdoc = doccus.ide_afdoc,  afi_activo AS act LEFT JOIN afi_doc_detalle_activo AS docact ON act.ide_afact = docact.ide_afact LEFT JOIN bodt_catalogo_material bcm ON act.ide_bocam=bcm.ide_bocam LEFT JOIN afi_nombre_activo nac ON act.ide_afnoa=nac.ide_afnoa WHERE   doccus.ide_gtemp in ("
				+ ide_gtemp
				+ ")  AND docact.ide_afdoc=doc.ide_afdoc AND act.afi_ultima_acta =  doc.ide_afdoc AND doccus.recibe_afddc IN (true)";
	}

	/**
	 * M&eacute;todo para consultar los activos vinculados a un custodio con la
	 * peculiaridad que excluye los bienes que se encuentran ya constatados en el
	 * mismo año
	 * 
	 * @param ide_gtemp
	 * @param com_anio
	 * @return
	 */
	public String getActivosPorCustodio(String ide_gtemp, String com_anio) {
		return "SELECT act.ide_afact, act.ide_afact as identificador_activo,CASE WHEN act.ide_bocam IS NULL THEN nac.detalle_afnoa ELSE bcm.descripcion_bocam END AS descripcion_bocam, detalle_afact,observaciones_afact,color_afact,  marca_afact, serie_afact, modelo_afact,  chasis_afact, motor_afact,cod_anterior_afact FROM 	afi_docu AS doc LEFT JOIN afi_doc_detalle_custodio AS doccus ON doc.ide_afdoc = doccus.ide_afdoc,  afi_activo AS act LEFT JOIN afi_doc_detalle_activo AS docact ON act.ide_afact = docact.ide_afact LEFT JOIN bodt_catalogo_material bcm ON act.ide_bocam=bcm.ide_bocam LEFT JOIN afi_nombre_activo nac ON act.ide_afnoa=nac.ide_afnoa WHERE   doccus.ide_gtemp in ("
				+ ide_gtemp
				+ ")  AND docact.ide_afdoc=doc.ide_afdoc AND act.afi_ultima_acta =  doc.ide_afdoc AND doccus.recibe_afddc IN (true)"
				+ " AND act.ide_afact NOT IN (SELECT act.ide_afact FROM 	afi_docu AS doc 	LEFT JOIN afi_doc_detalle_custodio AS doccus ON doc.ide_afdoc = doccus.ide_afdoc,  afi_activo AS act 	LEFT JOIN afi_doc_detalle_activo AS docact ON act.ide_afact = docact.ide_afact WHERE   doccus.ide_gtemp in ("
				+ ide_gtemp
				+ ")  AND docact.ide_afdoc=doc.ide_afdoc AND  act.afi_ultima_acta_consta =  doc.ide_afdoc AND  doc.ide_geani = "
				+ com_anio + " AND doccus.recibe_afddc IN (true))";
	}

	public String getConsultaActivosPorCustodio(String ide_gtemp) {
		return "SELECT act.ide_afact as codigo,cod_anterior_afact as codigo_anterior,bcm.descripcion_bocam as nombre_catalogo,bcm.cat_codigo_bocam as item_presupuestario,  cantidad_afact as cantidad,detalle_afact as descripcion_caracteristicas,observaciones_afact as componentes,   serie_afact as serie, marca_afact as marca, modelo_afact as modelo,  color_afact as color,  chasis_afact as chasis, motor_afact as motor, placa_afact as placa,detalle_afest as estado,detalle_afubi as ubicacion,doc.nro_secuencial_afdoc as numero_ultima_acta,act.afi_ultima_acta as ultima_acta_erp,   tdoc.detalle_aftidoc as tipo_ultima_acta    FROM 	afi_docu AS doc LEFT JOIN afi_doc_detalle_custodio AS doccus ON doc.ide_afdoc = doccus.ide_afdoc LEFT JOIN afi_tipo_docu AS tdoc ON doc.ide_aftidoc = tdoc.ide_aftidoc ,  afi_activo AS act LEFT JOIN afi_doc_detalle_activo AS docact ON act.ide_afact = docact.ide_afact LEFT JOIN bodt_catalogo_material bcm ON act.ide_bocam=bcm.ide_bocam LEFT JOIN afi_nombre_activo nac ON act.ide_afnoa=nac.ide_afnoa LEFT JOIN afi_estado AS est ON act.ide_afest = est.ide_afest LEFT JOIN afi_ubicacion AS ubi ON act.ide_afubi = ubi.ide_afubi WHERE   doccus.ide_gtemp in ("
				+ ide_gtemp
				+ ")  AND docact.ide_afdoc=doc.ide_afdoc AND act.afi_ultima_acta =  doc.ide_afdoc AND doccus.recibe_afddc IN (true)";
	}

	public String getActivosCodigo(String ide_activo) {
		String tab_custodio = "select * from afi_activo where ide_afact in (" + ide_activo + ")";
		// System.out.println("imprimor getActivosCodigo "+tab_custodio);
		return tab_custodio;
	}

	public String getCustodioCodigo(String ide_activo) {
		String tab_custodio = "select * from afi_custodio where ide_afact in (" + ide_activo + ")";
		return tab_custodio;
	}

	public String getCustodioSecuencial(String ide_activo) {
		String tab_custodio = "select ide_afact,max(nro_secuencial_afcus) as nro_secuencial_afcus, activo_afcus"
				+ " from ("
				+ " select a.ide_afact,(case when nro_secuencial_afcus is null then 0 else nro_secuencial_afcus end) as nro_secuencial_afcus,"
				+ " (case when activo_afcus is null then true else activo_afcus end) as activo_afcus"
				+ " from afi_activo a" + " left join afi_custodio b on a.ide_afact = b.ide_afact where a.ide_afact ="
				+ ide_activo + " ) a group by ide_afact, activo_afcus having activo_afcus = true";
		return tab_custodio;
	}

	public String getActivosEnBodega() {
		String activo = "SELECT ide_afact , cod_anterior_afact, ide_afact as identificador_activo, detalle_afact,observaciones_afact,color_afact,  marca_afact, serie_afact, modelo_afact,  chasis_afact, motor_afact FROM afi_activo "
				+ "WHERE ide_afact NOT IN (SELECT afidoc.ide_afact FROM afi_docu doc LEFT JOIN  afi_doc_detalle_activo afidoc ON doc.ide_afdoc = afidoc.ide_afdoc WHERE afidoc.ide_afact IS NOT NULL) "
				+ "ORDER BY ide_afact DESC;";
		return activo;
	}
	
	public String getActualizarDepreciacionSinPerito(String fecha_corte) {
		System.out.println("fecha_corte: " + fecha_corte);
		return "WITH param AS ( " +
				"	SELECT  " +
				"	date_trunc('month', '"+fecha_corte+"'::date)::date AS fecha_inicio, " +
				"	'"+fecha_corte+"'::date AS fecha_calculo, " +
				"	360.0 AS dias_por_anio " +
				"), " +
				"calculo_inicial as ( " +
				"  SELECT  " +
				"       ide_afact, activo.ide_bocam, catalogo.cat_codigo_bocam, detalle_afact, fecha_alta_afact, valor_compra_afact, vida_util_afact,  " +
				"       valor_compra_afact*0.1 as valor_residual,  " +
				"       (SELECT fecha_calculo FROM param)::date - fecha_alta_afact::date as dias_transcurridos, " +
				"       vida_util_afact - (((SELECT fecha_calculo FROM param)::date - fecha_alta_afact::date) / (SELECT dias_por_anio FROM param)) as tiempo_restante, " +
				"       valor_compra_afact - valor_compra_afact*0.1 as valor_a_depreciar, " +
				"       (valor_compra_afact - valor_compra_afact*0.1 ) / (NULLIF(vida_util_afact,0) * 12) as mensual, " +
				"       (valor_compra_afact - valor_compra_afact*0.1 ) / (NULLIF(vida_util_afact,0) * (SELECT dias_por_anio FROM param)) as diario " +
				"        " +
				"  FROM afi_activo as activo " +
				"  LEFT JOIN bodt_catalogo_material as catalogo ON catalogo.ide_bocam = activo.ide_bocam " +
				"  WHERE catalogo.cat_codigo_bocam LIKE '%84.%' " +
				" " +
				"), " +
				"calculo_depreciacion AS ( " +
				"SELECT  " +
				"*, " +
				"CASE WHEN vida_util_afact - (((SELECT fecha_calculo FROM param)::date - fecha_alta_afact::date) / (SELECT dias_por_anio FROM param)) <= 0 THEN valor_compra_afact * 0.9  " +
				"       ELSE (valor_compra_afact - valor_compra_afact*0.1 ) / (NULLIF(vida_util_afact,0) * (SELECT dias_por_anio FROM param)) * ((SELECT fecha_calculo FROM param)::date - fecha_alta_afact::date )  END as dep_acumulado " +
				"FROM calculo_inicial " +
				"), " +
				"activos as ( " +
				"SELECT  " +
				"       activo.ide_afact, activo.ide_bocam, catalogo.cat_codigo_bocam, catalogo.descripcion_bocam,  " +
				"regexp_replace(activo.detalle_afact, E'[\\n\\r]+', ' ', 'g' ) as detalle_afact,  " +
				"       activo.fecha_alta_afact, (SELECT fecha_calculo FROM param) as fecha_corte, " +
				"       activo.valor_compra_afact, activo.vida_util_afact, " +
				"       depreciacion.valor_residual,  depreciacion.dias_transcurridos, depreciacion.tiempo_restante, depreciacion.valor_a_depreciar,  " +
				"       depreciacion.mensual, depreciacion.diario,  " +
				"       COALESCE(depreciacion.dep_acumulado, 0) as dep_acumulado, activo.valor_compra_afact - COALESCE(dep_acumulado,0)  as valor_en_libros, " +
				"       (SELECT saca_del_inventario_empresarial  FROM afi_doc_detalle_activo as doc_detalle   " +
				"	LEFT JOIN afi_docu as doc ON doc.ide_afdoc = doc_detalle.ide_afdoc " +
				"	LEFT JOIN afi_tipo_docu as tipo_doc ON tipo_doc.ide_aftidoc = doc.ide_aftidoc " +
				"	WHERE doc_detalle.ide_afact = activo.ide_afact ORDER BY fecha_afdoc DESC LIMIT 1) as saca_del_inventario_empresarial " +
				"  FROM afi_activo as activo " +
				"  LEFT JOIN bodt_catalogo_material as catalogo ON catalogo.ide_bocam = activo.ide_bocam " +
				"  LEFT JOIN calculo_depreciacion as depreciacion ON activo.ide_afact = depreciacion.ide_afact " +
				"   " +
				"  ORDER BY catalogo.cat_codigo_bocam, activo.ide_bocam, activo.ide_afact " +
				"), " +
				"resultado AS( " +
				"SELECT  " +
				"activos.*, " +
				"descripcion_clasificador_prcla, " +
				"CASE WHEN fecha_corte-(SELECT fecha_alta_afact FROM param) <= 30 THEN fecha_corte-(SELECT fecha_alta_afact FROM param) ELSE 30 END AS dias_periodo, " +
				"CASE WHEN fecha_corte-(SELECT fecha_alta_afact FROM param) <= 30 THEN fecha_corte-(SELECT fecha_alta_afact FROM param) ELSE 30 END * activos.diario AS depreciacion_periodo " +
				"FROM activos  " +
				"LEFT JOIN pre_clasificador AS clasificador ON clasificador.codigo_clasificador_prcla = activos.cat_codigo_bocam " +
				"WHERE saca_del_inventario_empresarial=false AND cat_codigo_bocam LIKE '%84.%'  " +
				"AND fecha_alta_afact <= (SELECT fecha_calculo FROM param)::date " +
				" " +
				") " +
				" " +
				"UPDATE afi_activo  " +
				"SET fecha_calculo_afact = resultado.fecha_corte,  " +
				"fecha_alta_afact_temp = resultado.fecha_alta_afact, " +
				"vida_util_afact_temp = resultado.vida_util_afact, " +
				"valor_compra_afact_temp = resultado.valor_compra_afact, " +
				"valor_residual_afact = resultado.valor_residual, " +
				"depreciacion_acumulada_afact = resultado.dep_acumulado, " +
				"valor_depre_mes_afact = resultado.mensual, " +
				"valor_depre_dia_afact = resultado.diario, " +
				"valor_en_libros = resultado.valor_en_libros " +
				"FROM resultado " +
				"WHERE afi_activo.ide_afact = resultado.ide_afact " +
				"RETURNING resultado.*;  " 
 ;
	}

	public String getActivoDepreciacionSinPerito(String fecha_corte) {
		System.out.println("fecha_corte: " + fecha_corte);
		return "WITH param AS ( " +
				"	SELECT  " +
				"	date_trunc('month', '"+fecha_corte+"'::date)::date AS fecha_inicio, " +
				"	'"+fecha_corte+"'::date AS fecha_calculo, " +
				"	360.0 AS dias_por_anio " +
				"), " +
				"calculo_inicial as ( " +
				"  SELECT  " +
				"       ide_afact, activo.ide_bocam, catalogo.cat_codigo_bocam, detalle_afact, fecha_alta_afact, valor_compra_afact, vida_util_afact,  " +
				"       valor_compra_afact*0.1 as valor_residual,  " +
				"       (SELECT fecha_calculo FROM param)::date - fecha_alta_afact::date as dias_transcurridos, " +
				"       vida_util_afact - (((SELECT fecha_calculo FROM param)::date - fecha_alta_afact::date) / (SELECT dias_por_anio FROM param)) as tiempo_restante, " +
				"       valor_compra_afact - valor_compra_afact*0.1 as valor_a_depreciar, " +
				"       (valor_compra_afact - valor_compra_afact*0.1 ) / (NULLIF(vida_util_afact,0) * 12) as mensual, " +
				"       (valor_compra_afact - valor_compra_afact*0.1 ) / (NULLIF(vida_util_afact,0) * (SELECT dias_por_anio FROM param)) as diario " +
				"        " +
				"  FROM afi_activo as activo " +
				"  LEFT JOIN bodt_catalogo_material as catalogo ON catalogo.ide_bocam = activo.ide_bocam " +
				"  WHERE catalogo.cat_codigo_bocam LIKE '%84.%' " +
				" " +
				"), " +
				"calculo_depreciacion AS ( " +
				"SELECT  " +
				"*, " +
				"CASE WHEN vida_util_afact - (((SELECT fecha_calculo FROM param)::date - fecha_alta_afact::date) / (SELECT dias_por_anio FROM param)) <= 0 THEN valor_compra_afact * 0.9  " +
				"       ELSE (valor_compra_afact - valor_compra_afact*0.1 ) / (NULLIF(vida_util_afact,0) * (SELECT dias_por_anio FROM param)) * ((SELECT fecha_calculo FROM param)::date - fecha_alta_afact::date )  END as dep_acumulado " +
				"FROM calculo_inicial " +
				"), " +
				"activos as ( " +
				"SELECT  " +
				"       activo.ide_afact, activo.ide_bocam, catalogo.cat_codigo_bocam, catalogo.descripcion_bocam,  " +
				"regexp_replace(activo.detalle_afact, E'[\\n\\r]+', ' ', 'g' ) as detalle_afact,  " +
				"       activo.fecha_alta_afact, (SELECT fecha_calculo FROM param) as fecha_corte, " +
				"       activo.valor_compra_afact, activo.vida_util_afact, " +
				"       depreciacion.valor_residual,  depreciacion.dias_transcurridos, depreciacion.tiempo_restante, depreciacion.valor_a_depreciar,  " +
				"       depreciacion.mensual, depreciacion.diario,  " +
				"       COALESCE(depreciacion.dep_acumulado, 0) as dep_acumulado, activo.valor_compra_afact - COALESCE(dep_acumulado,0)  as valor_en_libros, " +
				"       (SELECT saca_del_inventario_empresarial  FROM afi_doc_detalle_activo as doc_detalle   " +
				"	LEFT JOIN afi_docu as doc ON doc.ide_afdoc = doc_detalle.ide_afdoc " +
				"	LEFT JOIN afi_tipo_docu as tipo_doc ON tipo_doc.ide_aftidoc = doc.ide_aftidoc " +
				"	WHERE doc_detalle.ide_afact = activo.ide_afact ORDER BY fecha_afdoc DESC LIMIT 1) as saca_del_inventario_empresarial " +
				"  FROM afi_activo as activo " +
				"  LEFT JOIN bodt_catalogo_material as catalogo ON catalogo.ide_bocam = activo.ide_bocam " +
				"  LEFT JOIN calculo_depreciacion as depreciacion ON activo.ide_afact = depreciacion.ide_afact " +
				"   " +
				"  ORDER BY catalogo.cat_codigo_bocam, activo.ide_bocam, activo.ide_afact " +
				"), " +
				"resultado AS( " +
				"SELECT  " +
				"activos.*, " +
				"descripcion_clasificador_prcla, " +
				"CASE WHEN fecha_corte-(SELECT fecha_alta_afact FROM param) <= 30 THEN fecha_corte-(SELECT fecha_alta_afact FROM param) ELSE 30 END AS dias_periodo, " +
				"CASE WHEN fecha_corte-(SELECT fecha_alta_afact FROM param) <= 30 THEN fecha_corte-(SELECT fecha_alta_afact FROM param) ELSE 30 END * activos.diario AS depreciacion_periodo " +
				"FROM activos  " +
				"LEFT JOIN pre_clasificador AS clasificador ON clasificador.codigo_clasificador_prcla = activos.cat_codigo_bocam " +
				"WHERE saca_del_inventario_empresarial=false AND cat_codigo_bocam LIKE '%84.%'  " +
				"AND fecha_alta_afact <= (SELECT fecha_calculo FROM param)::date " +
				"ORDER BY fecha_alta_afact DESC " +
				") " +
				"SELECT * FROM resultado "  ;
	}

	public String getActivosDepreciacion(String where) {
		String activo = "";
		activo += "SELECT ";
		activo += "   afa.ide_afact as ide_afact,";
		activo += "   afa.cod_anterior_afact as codigo_anterior,";
		activo += "   bcm.cat_codigo_bocam as item_presupuestario,";
		activo += "   bcm.descripcion_bocam as nombre_catalogo,";
		activo += "   atip.detalle_aftia as tipo_activo,";
		activo += "   afa.detalle_afact as descripcion_caracteristicas,";
		activo += "   afa.cantidad_afact as cantidad,";
		activo += "   afa.observaciones_afact as componentes,";
		activo += "   afa.serie_afact as serie,";
		activo += "   afa.marca_afact as marca,";
		activo += "   afa.modelo_afact as modelo,";
		activo += "   afa.color_afact as color,";
		activo += "   afa.chasis_afact as chasis,";
		activo += "   afa.motor_afact as motor,";
		activo += "   afa.placa_afact as placa,";
		activo += "   est.detalle_afest as estado,";
		activo += "   afa.fecha_alta_afact as fecha_alta_original,";
		activo += "   COALESCE((SELECT b.fecha_reavaluo_afper FROM afi_perito b WHERE (afa.fecha_calculo_afact::date - b.fecha_reavaluo_afper::date) >= 0 AND b.ide_afact = afa.ide_afact ORDER BY b.fecha_reavaluo_afper DESC LIMIT 1),afa.fecha_alta_afact) as fecha_alta_calculo,";
		activo += "   afa.fecha_calculo_afact as fecha_calculo,";
		activo += "   afa.valor_compra_afact as valor_compra,";
		activo += "   afa.valor_compra_afact_temp as valor_realizacion,";
		activo += "   afa.vida_util_afact as vida_util,";
		activo += "   afa.vida_util_afact_temp as vida_util_realizacion,";
		activo += "   afa.valor_depre_mes_afact as val_dep_mensual,";
		activo += "   afa.valor_depre_dia_afact as val_dep_diaria,";
		activo += "   afa.valor_depreciacion_afact as valor_depreciacion,";
		activo += "   afa.valor_residual_afact as valor_residual,";
		activo += "   afa.acta_codigo_temp as acta_codigo,";
		activo += "   (SELECT fecha_afdoc FROM afi_docu dco WHERE dco.ide_afdoc = afa.acta_codigo_temp) as acta_fecha,";
		activo += "   afa.acta_tipo_temp as acta_tipo,";
		activo += "   tip.detalle_aftip as forma_ingreso, ";
		activo += "   CASE WHEN COALESCE((SELECT b.ide_afact FROM afi_perito b WHERE  (afa.fecha_calculo_afact::date - b.fecha_reavaluo_afper::date) >= 0 AND b.ide_afact = afa.ide_afact ORDER BY b.fecha_reavaluo_afper DESC LIMIT 1),-1) = -1 THEN 'NO' ELSE 'SI' END AS info_de_perito,";
		activo += "   CASE WHEN afi_intangible = TRUE THEN 'SI' ELSE 'NO' END AS intangible";
		activo += "	  FROM afi_activo afa ";
		activo += "   LEFT JOIN bodt_catalogo_material AS bcm ON afa.ide_bocam = bcm.ide_bocam";
		activo += "   LEFT JOIN afi_tipo_activo AS atip ON afa.ide_aftia = atip.ide_aftia";
		activo += "   LEFT JOIN afi_tipo_propiedad AS tip ON afa.ide_aftip = tip.ide_aftip";
		activo += "   LEFT JOIN afi_estado AS est ON afa.ide_afest = est.ide_afest WHERE ";
		activo += where;
		activo += "   ORDER BY ide_afact DESC;";
		System.out.println(activo);
		return activo;
	}

	/**
	 * Genera una consulta en la cual se muestra la lista de activos que concuerdan
	 * en su columna afi_ultima_acta el tipo de acta que se quiere seleccionar
	 * 
	 * @param tipoActa lista del tipo de actas eje "16,10"
	 * @return
	 */
	public String getActivosPorTipoUltimaActa(String tipoActa) {
		String activo = "SELECT 	ide_afact, cod_anterior_afact, ide_afact as identificador_activo, detalle_afact,observaciones_afact,color_afact,  marca_afact, serie_afact, modelo_afact,  chasis_afact, motor_afact FROM afi_activo WHERE afi_ultima_acta IN (SELECT ide_afdoc FROM afi_docu WHERE ide_aftidoc IN ("
				+ tipoActa + ")) ORDER BY ide_afact DESC;";
		return activo;
	}

	public String getIngresosConBienesSinEntregar() {
		return "SELECT DISTINCT a.ide_bobod,b.ide_bobod as identificador_ingreso,  numero_ingreso_bobod as ingreso_numero "
				+ " FROM afi_activo a LEFT JOIN bodt_bodega b ON a.ide_bobod=b.ide_bobod "
				+ " WHERE ide_afact NOT IN (SELECT afidoc.ide_afact FROM afi_docu doc LEFT JOIN  afi_doc_detalle_activo afidoc ON doc.ide_afdoc = afidoc.ide_afdoc WHERE doc.ide_aftidoc = 1 AND afidoc.ide_afact IS NOT NULL) ORDER BY ide_bobod DESC;";
	}

	public String getActivosEnActaIngreso(String ide_bobod) {
		String activo = "SELECT ide_afact , cod_anterior_afact, ide_afact as identificador_activo, detalle_afact,observaciones_afact,color_afact,  marca_afact, serie_afact, modelo_afact,  chasis_afact, motor_afact FROM afi_activo "
				+ "WHERE ide_bobod IN (" + ide_bobod
				+ ") AND ide_afact NOT IN (SELECT afidoc.ide_afact FROM afi_docu doc LEFT JOIN  afi_doc_detalle_activo afidoc ON doc.ide_afdoc = afidoc.ide_afdoc WHERE afidoc.ide_afact IS NOT NULL) "
				+ "ORDER BY ide_afact DESC;";
		return activo;
	}

	public String getHerramientasNuevaTabla() {
		String activo = "select ide_afher,codigo_afher as identificador_activo,descripcion_afher as detalle_afact, marca_afher as marca_afact, color_afher as color_afact,ide_afest,serie_afher as serie_afact from afi_herramienta where prestado_afher=false order by ide_afher asc;";
		return activo;
	}
	public String getHerramientasEnBodega() {
		String activo = "SELECT ide_afact, ide_afact as identificador_activo,detalle_afact,b.descripcion_bocam,c.detalle_boubi,observaciones_afact,color_afact,  marca_afact, ide_afest,  serie_afact, modelo_afact FROM afi_activo a left join bodt_catalogo_material b ON a.ide_bocam = b.ide_bocam LEFT JOIN bodt_bodega_ubicacion c on a.ide_boubi=c.ide_boubi WHERE afi_prestado=false;";
		return activo;
	}

	public String getHerramientasPrestadas() {
		String activo = "SELECT ide_afact, ide_afact as identificador_activo,b.descripcion_bocam,c.detalle_boubi, detalle_afact,observaciones_afact,color_afact,  marca_afact, serie_afact, modelo_afact FROM afi_activo a left join bodt_catalogo_material b ON a.ide_bocam = b.ide_bocam LEFT JOIN bodt_bodega_ubicacion c on a.ide_boubi=c.ide_boubi WHERE afi_prestado=true;";
		return activo;
	}
	public String getHerramientasNuevaTablaPrestadas() {
		String activo = "select ide_afher,codigo_afher as identificador_activo,descripcion_afher as detalle_afact, marca_afher as marca_afact, color_afher as color_afact,ide_afest,serie_afher as serie_afact from afi_herramienta where prestado_afher=true;";
		return activo;
	}

	public String getHerramientasPorBodega(String bodega) {
		String activo;
		if (bodega != "") {
			activo = "SELECT ide_afact, ide_afact as identificador_activo, b.cat_codigo_bocam, b.descripcion_bocam,c.detalle_boubi, case when afi_prestado then 'PRESTADO' else 'EN BODEGA' end as afi_prestado , detalle_afact,observaciones_afact,color_afact,  marca_afact, serie_afact, modelo_afact FROM afi_activo a left join bodt_catalogo_material b ON a.ide_bocam = b.ide_bocam LEFT JOIN bodt_bodega_ubicacion c on a.ide_boubi=c.ide_boubi WHERE afi_prestado in (true,false) and a.ide_boubi="
					+ bodega + ";";
		} else {
			activo = "SELECT ide_afact, ide_afact as identificador_activo, b.cat_codigo_bocam, b.descripcion_bocam,c.detalle_boubi, case when afi_prestado then 'PRESTADO' else 'EN BODEGA' end as afi_prestado , detalle_afact,observaciones_afact,color_afact,  marca_afact, serie_afact, modelo_afact FROM afi_activo a left join bodt_catalogo_material b ON a.ide_bocam = b.ide_bocam LEFT JOIN bodt_bodega_ubicacion c on a.ide_boubi=c.ide_boubi WHERE afi_prestado in (true,false);";
		}
		return activo;
	}

	public String getDatosAseguradora() {
		String tab_datos = "select ide_tease,detalle_tease " + "from tes_aseguradora where activo_tease = true "
				+ "order by detalle_tease";

		return tab_datos;
	}
	
	
	public String obtenerHerramientasPorNumeroDeActaTipoPrestamo(String numeroDeActa){

		String tabla_datos= "select  her.ide_afher, afidoc.ide_afdda as identificador_acta_detalle, her.ide_afher as identificador_activo,descripcion_afher,color_afher,  marca_afher, serie_afher " 
				+"FROM afi_herramienta her inner join afi_doc_detalle_activo afidoc on afidoc.ide_afher = her.ide_afher inner join  afi_docu  on afi_docu.ide_afdoc = afidoc.ide_afdoc "
				+"WHERE afidoc.ide_afher IS NOT null and  afi_docu.ide_afdoc="+numeroDeActa+" and afi_docu.ide_aftidoc=4 and her.prestado_afher= true "
				+"ORDER BY ide_afher DESC;";                     
        return tabla_datos;
	}
	public String obtenerActivosPorNumeroDeActaTipoPrestamo(String numeroDeActa){

		String tabla_datos= "select  aa.ide_afact, afidoc.ide_afdda as identificador_acta_detalle, cod_anterior_afact, aa.ide_afact as identificador_activo, detalle_afact,observaciones_afact,color_afact,  marca_afact, serie_afact, modelo_afact,  chasis_afact, motor_afact " 
				+"FROM afi_activo aa inner join afi_doc_detalle_activo afidoc on afidoc.ide_afact = aa.ide_afact inner join  afi_docu    on afi_docu.ide_afdoc = afidoc.ide_afdoc "
				+"WHERE afidoc.ide_afact IS NOT null and  afi_docu.ide_afdoc="+numeroDeActa+" and afi_docu.ide_aftidoc=4 and aa.afi_prestado= true "
				+"ORDER BY ide_afact DESC;";                     
        return tabla_datos;
	}
	
public String obtenerHerramientasPorIdSolicitante(String idSolicitante){
		
		String tabla_datos= "her.ide_afher, her.ide_afher as identificador_activo, descripcion_afher as descripcion_caracteristicas,serie_afher as serie, marca_afher as marca, color_afher as color,detalle_afest as estado,detalle_afubi as ubicacion,doc.nro_secuencial_afdoc as numero_ultima_acta,tdoc.detalle_aftidoc as tipo_ultima_acta"    
				+"FROM 	afi_docu AS doc "  
				+"LEFT JOIN afi_doc_detalle_custodio AS doccus ON doc.ide_afdoc = doccus.ide_afdoc"  
				+"LEFT JOIN afi_tipo_docu AS tdoc ON doc.ide_aftidoc = tdoc.ide_aftidoc ,  afi_herramienta AS her"  
				+"LEFT JOIN afi_doc_detalle_activo AS docact ON her.ide_afher = docact.ide_afher"  
				+"LEFT JOIN afi_estado AS est ON act.ide_afest = est.ide_afest "  
				+"LEFT JOIN afi_ubicacion AS ubi ON act.ide_afubi = ubi.ide_afubi WHERE   doccus.ide_gtemp in (" + idSolicitante + ") "   
				+"AND docact.ide_afdoc=doc.ide_afdoc AND her.acta_afher =  doc.ide_afdoc AND doccus.recibe_afddc IN (true) and doc.ide_aftidoc =4; "; 
		return tabla_datos;
	}
	public String obtenerActivosPorIdSolicitante(String idSolicitante){
		
		System.out.println("INGRESA A LA CONSULTA" );

		String tabla_datos= "SELECT act.ide_afact, act.ide_afact as identificador_activo, bcm.descripcion_bocam as nombre_catalogo,bcm.cat_codigo_bocam as item_presupuestario,  cantidad_afact as cantidad,detalle_afact as descripcion_caracteristicas,observaciones_afact as componentes,   serie_afact as serie, marca_afact as marca, modelo_afact as modelo,  color_afact as color,  chasis_afact as chasis, motor_afact as motor, placa_afact as placa,detalle_afest as estado,detalle_afubi as ubicacion,doc.nro_secuencial_afdoc as numero_ultima_acta,act.afi_ultima_acta as ultima_acta_erp,   tdoc.detalle_aftidoc as tipo_ultima_acta "    
				+"FROM 	afi_docu AS doc "  
				+"LEFT JOIN afi_doc_detalle_custodio AS doccus ON doc.ide_afdoc = doccus.ide_afdoc "  
				+"LEFT JOIN afi_tipo_docu AS tdoc ON doc.ide_aftidoc = tdoc.ide_aftidoc ,  afi_activo AS act "  
				+"LEFT JOIN afi_doc_detalle_activo AS docact ON act.ide_afact = docact.ide_afact "  
				+"LEFT JOIN bodt_catalogo_material bcm ON act.ide_bocam=bcm.ide_bocam "  
				+"LEFT JOIN afi_nombre_activo nac ON act.ide_afnoa=nac.ide_afnoa "  
				+"LEFT JOIN afi_estado AS est ON act.ide_afest = est.ide_afest "  
				+"LEFT JOIN afi_ubicacion AS ubi ON act.ide_afubi = ubi.ide_afubi WHERE   doccus.ide_gtemp in (" + idSolicitante + ") "   
				+"AND docact.ide_afdoc=doc.ide_afdoc AND act.afi_ultima_acta =  doc.ide_afdoc AND doccus.recibe_afddc IN (true) and doc.ide_aftidoc =4; "; 
		
		System.out.println("  _____________ DEVUELVE RESULATDOS_________________    " +  tabla_datos );
        return tabla_datos;
	}
	
	

	public String getSegurosActivos(String grupos, String aseguradora, String fecha_inicial, String fecha_final) {

		String tab_seguros = "SELECT s.numero_poliza_afseg as numero, s.detalle_afseg as detalle, s.monto_asegurado_afseg, s.porcentaje_asegurable_afseg, "
				+ "s.prima_neta_afseg, observacion_afseg, s.fecha_inicio_afseg, fecha_vigencia_afseg, e.detalle_coest as estado,ase.detalle_tease as aseguradora, "
				+ "CASE WHEN fecha_vigencia_afseg > current_date THEN 'VENCIDO' " + "ELSE 'VIGENTE' END as alerta "
				+ "FROM AFI_SEGURO S " + "LEFT JOIN tes_aseguradora ASE ON ase.ide_tease= s.ide_tease "
				+ "left join cont_estado e ON e.ide_coest=s.ide_coest " + "where s.activo_afseg=true ";
		if (grupos.equals("1")) {
			tab_seguros += " and ase.ide_tease ='" + aseguradora + "'";
		}
		if (grupos.equals("2")) {
			tab_seguros += " and fecha_vigencia_afseg between '" + fecha_inicial + "' and '" + fecha_final + "' ";
		}
		return tab_seguros;
	}

}
