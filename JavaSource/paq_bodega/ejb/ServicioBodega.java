package paq_bodega.ejb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import framework.aplicacion.TablaGenerica;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Utilitario;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidades.EnvioMail;

/**
 * Session Bean implementation class ServicioBodega
 */

@Stateless
@LocalBean
public class ServicioBodega {

	/**
	 * Default constructor.
	 */
	private Utilitario utilitario = new Utilitario();
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario
			.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);

	public final Integer TRANSACCION_INGRESO = 1;
	public final Integer TRANSACCION_EGRESO = 2;
	public final Integer TRANSACCION_TRANSFERENCIA_INGRESO = 3;
	public final Integer TRANSACCION_TRANSFERENCIA_EGRESO = 4;
	public final Integer TRANSACCION_EGRESO_BAJA = 6;
	public final Integer ESTADO_PREDETERMINADO = 8;

	public void trigRegistraInventarioIngreso(String ide_geani, String ide_bocam, String ide_boubi) {
		// Inserta un registro en bodt_bodega_inventario con todo en 0.00
		// System.out.println("trigRegistraInventarioIngreso");
		String usuario = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("nick_usua");
		String fecha = utilitario.getFechaActual();
		String hora = utilitario.getHoraActual();
		TablaGenerica tab_maximo = utilitario
				.consultar(ser_contabilidad.servicioCodigoMaximo("bodt_bodega_inventario", "ide_boinv"));
		String ide_boinv = tab_maximo.getValor("codigo");
		String sql = String.format("INSERT INTO bodt_bodega_inventario("
				+ "            ide_boinv, ide_geani, ide_bocam, ide_boubi, descripcion_boinv, "
				+ "            cantidad_inicial_boinv, costo_inicial_boinv, cantidad_ingreso_boinv, "
				+ "            costo_ingreso_boinv, cantidad_egreso_boinv, costo_egreso_boinv, "
				+ "            costo_inicial_inc_iva_boinv, costo_ingreso_inc_iva_boinv, costo_egreso_inc_iva_boinv, costo_saldo_inc_iva_boinv,"
				+ "            cantidad_saldo_boinv, costo_saldo_boinv, usuario_ingre, fecha_ingre, hora_ingre, "
				+ "            usuario_actua, fecha_actua, hora_actua, activo_boinv,"
				+ "            valor_existencia_inicial_boinv, valor_existencia_inicial_sin_iva_boinv, valor_existencia_inicial_inc_iva_boinv, "
				+ "            costo_inicial_sin_iva_boinv, costo_ingreso_sin_iva_boinv, costo_egreso_sin_iva_boinv, costo_saldo_sin_iva_boinv, "
				+ "			 cantidad_egreso_baja_boinv, costo_egreso_baja_boinv" + " ) "
				+ "    VALUES ( %s, %s, %s, %s, %s, " + "            0, 0, 0, " + "            0, 0, 0, "
				+ "            0, 0, 0, 0, " + "            0, 0, '%s', '%s', '%s', '%s', '%s', '%s', %s,"
				+ "          0, 0, 0," + "          0, 0, 0, 0," + "			 0, 0);", ide_boinv, ide_geani, ide_bocam,
				ide_boubi, null, usuario, fecha, hora, usuario, fecha, hora, Boolean.TRUE.toString());
		utilitario.getConexion().ejecutarSql(sql);

	}

	public TablaGenerica getFamiliasDeCatalogoPorCertificacion(String ide_prcer) {
		String sql = String.format("WITH detalle_certificacion AS ( " + "SELECT  "
				+ "    pre_certificacion.ide_prcer, pre_certificacion.valor_certificacion_prcer,  "
				+ "    pre_clasificador.ide_prcla, pre_clasificador.codigo_clasificador_prcla ,  "
				+ "    SUM(pre_poa_certificacion.valor_certificado_prpoc) as valor_certificado  "
				+ "FROM pre_certificacion  "
				+ "LEFT JOIN pre_poa_certificacion ON pre_certificacion.ide_prcer = pre_poa_certificacion.ide_prcer  "
				+ "LEFT JOIN pre_poa ON pre_poa.ide_prpoa=pre_poa_certificacion.ide_prpoa  "
				+ "LEFT JOIN pre_clasificador ON pre_poa.ide_prcla=pre_clasificador.ide_prcla  "
				+ "WHERE pre_certificacion.ide_prcer =  %s " + "GROUP BY 1,2,3,4  " + ") "
				+ "SELECT catalogo.ide_bocam,   " + "catalogo.cat_codigo_bocam || ' ' || catalogo.descripcion_bocam  "
				+ "FROM bodt_catalogo_material as catalogo  "
				+ "LEFT JOIN pre_clasificador ON pre_clasificador.ide_prcla = catalogo.ide_prcla "
				+ "WHERE catalogo.con_ide_bocam = 2 AND catalogo.ide_prcla IN (SELECT ide_prcla FROM detalle_certificacion) "
				+ "ORDER BY 2 ", ide_prcer);
		return utilitario.consultar(sql);

	}

	public TablaGenerica getDetalleCertificacionPresupuestaria(String ide_prcer) {
		String sql = String.format("SELECT "
				+ "    pre_certificacion.ide_prcer, pre_certificacion.valor_certificacion_prcer, "
				+ "    pre_clasificador.ide_prcla, pre_clasificador.codigo_clasificador_prcla , "
				+ "    SUM(pre_poa_certificacion.valor_certificado_prpoc) as valor_certificado "
				+ "FROM pre_certificacion "
				+ "LEFT JOIN pre_poa_certificacion ON pre_certificacion.ide_prcer = pre_poa_certificacion.ide_prcer "
				+ "LEFT JOIN pre_poa ON pre_poa.ide_prpoa=pre_poa_certificacion.ide_prpoa "
				+ "LEFT JOIN pre_clasificador ON pre_poa.ide_prcla=pre_clasificador.ide_prcla "
				+ "WHERE pre_certificacion.ide_prcer = %s " + "GROUP BY 1,2,3,4 ",
				// param
				ide_prcer);
		return utilitario.consultar(sql);
	}

	/**
	 * Verifica si ya esta registrado un ide_afact (código de activo) en la tabla
	 * afi_poa
	 * 
	 * @param ide_afact
	 * @return
	 */
	public boolean existeActivoEnAfiPoa(String ide_afact) {
		String sql = String.format("SELECT ide_afact, COUNT(*) FROM afi_poa WHERE ide_afact = %s GROUP BY 1",
				ide_afact);
		// System.out.println(sql);
		TablaGenerica tg = utilitario.consultar(sql);
		if (tg.getTotalFilas() > 0) {
			return true;
		}
		return false;
	}

	public TablaGenerica getResumenPoaDeIdePrpoc(String ide_prpoc) {
		String sql = String.format("WITH resumen_funcion_programa AS ( " + "select a.ide_prfup, "
				+ "       codigo_subactividad, " + "       detalle_subactividad, " + "       subactividad, "
				+ "       codigo_actividad, " + "       detalle_actividad, " + "       actividad, "
				+ "       codigo_producto, " + "       cod_prod, " + "       detalle_producto, " + "       producto, "
				+ "       codigo_actividad_mc, " + "       cod_act, " + "       detalle_actividad_mc, "
				+ "       actividad_mc, " + "       codigo_producto_mc, " + "       cod_prod_mc, "
				+ "       detalle_producto_mc, " + "       producto_mc, " + "       codigo_obra, "
				+ "       detalle_producto_mc as obra, "
				+ "       coalesce(pry1.codigo_proyecto,pry2.codigo_proyecto) as codigo_proyecto, "
				+ "       coalesce(pry1.cod_pry,pry2.cod_pry) as cod_pry, "
				+ "       coalesce(pry1.detalle_proyecto,pry2.detalle_proyecto) as detalle_proyecto, "
				+ "       coalesce(pry1.proyecto,pry2.proyecto) as proyecto, "
				+ "       coalesce(pry1.codigo_programa,pry2.codigo_programa) as codigo_programa, "
				+ "       coalesce(pry1.detalle_programa,pry2.detalle_programa) as detalle_programa, "
				+ "       coalesce(pry1.programa,pry2.programa) as programa, "
				+ "       coalesce(pry1.cod_prog,pry2.cod_prog) as cod_prog " + "from " + "    (select ide_prfup, "
				+ "            pre_ide_prfup, " + "            codigo_prfup as codigo_subactividad, "
				+ "            detalle_prfup as detalle_subactividad, " + "            detalle_prnfp as subactividad "
				+ "     from pre_funcion_programa a, " + "          pre_nivel_funcion_programa b "
				+ "     where a.ide_prnfp = b.ide_prnfp " + "         and b.nivel_prnfp =7) a " + "left join "
				+ "    (select ide_prfup, " + "            pre_ide_prfup, "
				+ "            codigo_prfup as codigo_actividad, " + "            detalle_prfup as detalle_actividad, "
				+ "            detalle_prnfp as actividad " + "     from pre_funcion_programa a, "
				+ "          pre_nivel_funcion_programa b " + "     where a.ide_prnfp = b.ide_prnfp "
				+ "         and b.nivel_prnfp =6) b on a.pre_ide_prfup = b.ide_prfup " + "left join "
				+ "    (select ide_prfup, " + "            pre_ide_prfup, "
				+ "            codigo_prfup as codigo_producto, " + "            detalle_prfup as detalle_producto, "
				+ "            detalle_prnfp as producto, " + "            codigo_pry_prd_prfup as cod_prod "
				+ "     from pre_funcion_programa a, " + "          pre_nivel_funcion_programa b "
				+ "     where a.ide_prnfp = b.ide_prnfp "
				+ "         and b.nivel_prnfp =5) c on b.pre_ide_prfup = c.ide_prfup " + "left join "
				+ "    (select ide_prfup, " + "            pre_ide_prfup, "
				+ "            codigo_prfup as codigo_actividad_mc, " + "            codigo_pry_prd_prfup as cod_act, "
				+ "            detalle_prfup as detalle_actividad_mc, " + "            detalle_prnfp as actividad_mc "
				+ "     from pre_funcion_programa a, " + "          pre_nivel_funcion_programa b "
				+ "     where a.ide_prnfp = b.ide_prnfp "
				+ "         and b.nivel_prnfp =4) d on c.pre_ide_prfup = d.ide_prfup " + "left join "
				+ "    (select ide_prfup, " + "            pre_ide_prfup, "
				+ "            codigo_prfup as codigo_producto_mc, "
				+ "            codigo_pry_prd_prfup as cod_prod_mc, "
				+ "            detalle_prfup as detalle_producto_mc, " + "            detalle_prnfp as producto_mc, "
				+ "            case " + "                when length(codigo_obra_prfup) > 9 then codigo_obra_prfup "
				+ "                else codigo_pry_prd_prfup " + "            end as codigo_obra "
				+ "     from pre_funcion_programa a, " + "          pre_nivel_funcion_programa b "
				+ "     where a.ide_prnfp = b.ide_prnfp "
				+ "         and b.nivel_prnfp =3 ) e on d.pre_ide_prfup = e.ide_prfup " + "left join "
				+ "    (select pr.ide_prfup, " + "            pr.pre_ide_prfup, " + "            codigo_proyecto, "
				+ "            detalle_proyecto, " + "            proyecto, " + "            cod_pry, "
				+ "            cod_prog, " + "            codigo_programa, " + "            detalle_programa, "
				+ "            programa   " + "     from " + "         (select ide_prfup, "
				+ "                 pre_ide_prfup, "
				+ "                 codigo_prfup as codigo_proyecto,   detalle_prfup as detalle_proyecto, "
				+ "                                                   detalle_prnfp as proyecto, "
				+ "                                                   codigo_pry_prd_prfup as cod_pry   "
				+ "          from pre_funcion_programa a, " + "               pre_nivel_funcion_programa b   "
				+ "          where a.ide_prnfp = b.ide_prnfp " + "              and b.nivel_prnfp =2) pr   "
				+ "     left join " + "         (select ide_prfup, " + "                 pre_ide_prfup, "
				+ "                 codigo_prfup as codigo_programa, "
				+ "                 codigo_pry_prd_prfup as cod_prog,   detalle_prfup as detalle_programa, "
				+ "                                                    detalle_prnfp as programa "
				+ "          from pre_funcion_programa a, " + "               pre_nivel_funcion_programa b   "
				+ "          where a.ide_prnfp = b.ide_prnfp "
				+ "              and b.nivel_prnfp =1) g on pr.pre_ide_prfup = g.ide_prfup) pry1 on c.pre_ide_prfup = pry1.ide_prfup "
				+ "left join " + "    (select pr.ide_prfup, " + "            pr.pre_ide_prfup, "
				+ "            codigo_proyecto, " + "            detalle_proyecto, " + "            proyecto, "
				+ "            cod_pry, " + "            cod_prog, " + "            codigo_programa, "
				+ "            detalle_programa, " + "            programa   " + "     from "
				+ "         (select ide_prfup, " + "                 pre_ide_prfup, "
				+ "                 codigo_prfup as codigo_proyecto,   detalle_prfup as detalle_proyecto, "
				+ "                                                   detalle_prnfp as proyecto, "
				+ "                                                   codigo_pry_prd_prfup as cod_pry   "
				+ "          from pre_funcion_programa a, " + "               pre_nivel_funcion_programa b   "
				+ "          where a.ide_prnfp = b.ide_prnfp " + "              and b.nivel_prnfp =2) pr   "
				+ "     left join " + "         (select ide_prfup, " + "                 pre_ide_prfup, "
				+ "                 codigo_prfup as codigo_programa, "
				+ "                 codigo_pry_prd_prfup as cod_prog,   detalle_prfup as detalle_programa, "
				+ "                                                    detalle_prnfp as programa "
				+ "          from pre_funcion_programa a, " + "               pre_nivel_funcion_programa b   "
				+ "          where a.ide_prnfp = b.ide_prnfp "
				+ "              and b.nivel_prnfp =1) g on pr.pre_ide_prfup = g.ide_prfup) pry2 on e.pre_ide_prfup = pry2.ide_prfup  "
				+ "              ) " + "           " + "SELECT  "
				+ " pre_poa_certificacion.ide_prpoc, pre_poa.ide_prpoa, pre_certificacion.ide_geani, pre_certificacion.ide_prcer, valor_certificado_prpoc, pre_funcion_programa.ide_prfup,  "
				+ "  pre_clasificador.ide_prcla, "
				+ " cod_prog, codigo_programa, detalle_programa, cod_pry, codigo_proyecto, detalle_proyecto, codigo_actividad, detalle_actividad, codigo_subactividad, detalle_subactividad, "
				+ " pre_clasificador.codigo_clasificador_prcla, descripcion_clasificador_prcla, pre_tipo_gestion.detalle_prtge "
				+ "FROM pre_poa_certificacion "
				+ "LEFT JOIN pre_poa ON pre_poa.ide_prpoa = pre_poa_certificacion.ide_prpoa "
				+ "LEFT JOIN pre_funcion_programa ON pre_funcion_programa.ide_prfup = pre_poa.ide_prfup "
				+ "LEFT JOIN resumen_funcion_programa ON resumen_funcion_programa.ide_prfup = pre_poa.ide_prfup "
				+ "LEFT JOIN pre_clasificador ON pre_clasificador.ide_prcla = pre_poa.ide_prcla  "
				+ "LEFT JOIN pre_certificacion ON pre_certificacion.ide_prcer = pre_poa_certificacion.ide_prcer "
				+ "LEFT JOIN pre_tipo_gestion ON pre_tipo_gestion.ide_prtge = pre_poa.ide_prtge "
				+ "WHERE pre_poa_certificacion.ide_prpoc = %s ", ide_prpoc);
		//System.out.println(sql);
		return utilitario.consultar(sql);
	}

	public void registrarActivoEnAfiPoa(String ide_afact, String ide_prpoc) {
		if (existeActivoEnAfiPoa(ide_afact)) {
			System.out.println(String.format("Activo ide_afact: %s ya registrado en afi_poa", ide_afact));
			return;
		}
		System.out.println("existeActivoEnAfiPoa: false");
		TablaGenerica tg = getResumenPoaDeIdePrpoc(ide_prpoc);
		String detalle_prtge = tg.getValor("detalle_prtge");
		String cod_prog = tg.getValor("cod_prog");
		String detalle_programa = tg.getValor("detalle_programa");
		String cod_pry = tg.getValor("cod_pry");
		String detalle_proyecto = tg.getValor("detalle_proyecto");
		String codigo_actividad = tg.getValor("codigo_actividad");
		String detalle_actividad = tg.getValor("detalle_actividad");
		String codigo_subactividad = tg.getValor("codigo_subactividad");
		String detalle_subactividad = tg.getValor("detalle_subactividad");
		String codigo_clasificador_prcla = tg.getValor("codigo_clasificador_prcla");
		String descripcion_clasificador_prcla = tg.getValor("descripcion_clasificador_prcla");

		String sql_insert = String.format("INSERT INTO afi_poa( "
				+ "            ide_afpoa, ide_afact, ide_prpoc, detalle_prtge, cod_prog, detalle_programa, "
				+ "            cod_pry, detalle_proyecto, codigo_actividad, detalle_actividad,  "
				+ "            codigo_subactividad, detalle_subactividad, codigo_clasificador_prcla,  "
				+ "            descripcion_clasificador_prcla) "
				+ "    VALUES ((SELECT COALESCE(MAX(ide_afpoa), 0) + 1 FROM afi_poa), %s, %s, '%s', '%s', '%s', "
				+ "            '%s', '%s', '%s', '%s', " + "            '%s', '%s', '%s', " + "            '%s');",
				ide_afact, ide_prpoc, detalle_prtge, cod_prog, detalle_programa, cod_pry, detalle_proyecto,
				codigo_actividad, detalle_actividad, codigo_subactividad, detalle_subactividad,
				codigo_clasificador_prcla, descripcion_clasificador_prcla);
		utilitario.getConexion().ejecutarSql(sql_insert);
		System.out.println(
				String.format("Registrando activo ide_afact: %s con ide_prpoc: %s en afi_poa", ide_afact, ide_prpoc));
	}

	public String getSqlClasificadorPresupuestarioDeCertificacion(String ide_prcer) {
		return String.format("WITH detalle_certificacion AS " + "    (SELECT pre_certificacion.ide_prcer, "
				+ "            pre_certificacion.valor_certificacion_prcer, "
				+ "            pre_clasificador.ide_prcla, "
				+ "            pre_clasificador.codigo_clasificador_prcla, "
				+ "            descripcion_clasificador_prcla, "
				+ "            SUM(pre_poa_certificacion.valor_certificado_prpoc) as valor_certificado "
				+ "     FROM pre_certificacion "
				+ "     LEFT JOIN pre_poa_certificacion ON pre_certificacion.ide_prcer = pre_poa_certificacion.ide_prcer "
				+ "     LEFT JOIN pre_poa ON pre_poa.ide_prpoa=pre_poa_certificacion.ide_prpoa "
				+ "     LEFT JOIN pre_clasificador ON pre_poa.ide_prcla=pre_clasificador.ide_prcla "
				+ "     WHERE pre_certificacion.ide_prcer = %s " + "     GROUP BY 1,2,3,4,5) "
				+ "SELECT detalle_certificacion.ide_prcla, "
				+ "       codigo_clasificador_prcla || ' ' || descripcion_clasificador_prcla as descripcion "
				+ "FROM detalle_certificacion  ", ide_prcer);
	}

	public String getSqlEgresosPorSolicitante(String ide_geani, String ide_gtemp) {
		return String.format(
				"SELECT detalle.ide_inegd, ide_boubi, fecha_ingeg, numero_documento_ingeg,  detalle.ide_bocam, egreso.ide_ingeg, detalle.costo_unitario_inegd, cantidad_inegd, "
						+ "detalle.subtotal_inegd,  total_inegd, ide_gtemp_solicitante "
						+ "  FROM bodt_ingreso_egreso_det as detalle "
						+ "  LEFT JOIN bodt_ingreso_egreso as egreso ON egreso.ide_ingeg = detalle.ide_ingeg "
						+ "  LEFT JOIN gth_empleado as empleado ON empleado.ide_gtemp = egreso.ide_gtemp_solicitante "
						+ "  WHERE egreso.ide_inttr = 2 AND egreso.ide_geani = %s AND empleado.ide_gtemp = %s",
				ide_geani, ide_gtemp);
	}

	public String getSqlEgresosPorItemCatalogo(String ide_geani, String ide_bocam) {
		return String.format(
				"SELECT detalle.ide_inegd, ide_boubi, fecha_ingeg, numero_documento_ingeg,  detalle.ide_bocam, egreso.ide_ingeg, detalle.costo_unitario_inegd, cantidad_inegd, "
						+ "detalle.subtotal_inegd,  total_inegd, ide_gtemp_solicitante "
						+ "  FROM bodt_ingreso_egreso_det as detalle "
						+ "  LEFT JOIN bodt_ingreso_egreso as egreso ON egreso.ide_ingeg = detalle.ide_ingeg "
						+ "  LEFT JOIN gth_empleado as empleado ON empleado.ide_gtemp = egreso.ide_gtemp_solicitante "
						+ "  WHERE egreso.ide_inttr = 2 AND egreso.ide_geani = %s AND detalle.ide_bocam = %s",
				ide_geani, ide_bocam);
	}

	public void trigTransferencia(String ide_ingeg, String ide_inttr_t_egreso, String ide_inttr_t_ingreso) {
		// System.out.println("trigTransferencia");

		String sql = String.format("WITH param AS ( " + "    SELECT " + "    %s as ide_inttr_t_ingreso, "
				+ "    %s as ide_inttr_t_egreso, " + "    %s as ide_ingeg " + "     " + "), nuevo_pk AS ( "
				+ "   SELECT 1 AS ide,(CASE WHEN max(ide_ingeg) IS NULL THEN 0 ELSE max(ide_ingeg) END) + 1 AS codigo FROM bodt_ingreso_egreso "
				+ "), cabecera_transferencia_ingreso AS ( "
				+ "    SELECT (SELECT ide_inttr_t_ingreso FROM param) as ide_inttr, ide_adfac, ide_adfac_antigua, ide_boubi as ide_boubi_transferencia,  "
				+ "       ide_geani, ide_gtemp, ide_gtemp_jefe_solicitante, ide_gtemp_solicitante,  "
				+ "       ide_prcer, fecha_ingeg, observacion_ingeg, activo_ingeg, subtotal_ingeg,  "
				+ "       valor_iva_ingeg, total_ingeg, usuario_ingre, fecha_ingre, hora_ingre,  "
				+ "       usuario_actua, fecha_actua, hora_actua, numero_documento_ingeg,  "
				+ "       ide_boubi_transferencia as ide_boubi, ide_ingeg_ref " + "    FROM bodt_ingreso_egreso "
				+ "    WHERE ide_ingeg = (SELECT ide_ingeg FROM param)  "
				+ "	AND ide_inttr = (SELECT ide_inttr_t_egreso FROM param) " + "), insert_cabecera_ingreso AS ( "
				+ "   INSERT INTO bodt_ingreso_egreso( " + "	    ide_ingeg, "
				+ "            ide_inttr, ide_adfac, ide_adfac_antigua, ide_boubi,  "
				+ "            ide_geani, ide_gtemp, ide_gtemp_jefe_solicitante, ide_gtemp_solicitante,  "
				+ "            ide_prcer, fecha_ingeg, observacion_ingeg, activo_ingeg, subtotal_ingeg,  "
				+ "            valor_iva_ingeg, total_ingeg, usuario_ingre, fecha_ingre, hora_ingre,  "
				+ "            usuario_actua, fecha_actua, hora_actua, numero_documento_ingeg,  "
				+ "            ide_boubi_transferencia,  ide_ingeg_ref) " + "    SELECT  "
				+ "       (SELECT codigo FROM nuevo_pk), "
				+ "       ide_inttr, ide_adfac, ide_adfac_antigua, ide_boubi,  "
				+ "       ide_geani, ide_gtemp, ide_gtemp_jefe_solicitante, ide_gtemp_solicitante,  "
				+ "       ide_prcer, fecha_ingeg, observacion_ingeg, activo_ingeg, subtotal_ingeg,  "
				+ "       valor_iva_ingeg, total_ingeg, usuario_ingre, fecha_ingre, hora_ingre,  "
				+ "       usuario_actua, fecha_actua, hora_actua, numero_documento_ingeg,  "
				+ "       ide_boubi_transferencia, (SELECT ide_ingeg FROM param) as ide_ingeg_ref "
				+ "     FROM cabecera_transferencia_ingreso  "
				+ "     WHERE ide_inttr = (SELECT ide_inttr_t_ingreso FROM param) " + "), update_cabecera_egreso AS ( "
				+ "    UPDATE bodt_ingreso_egreso SET ide_ingeg_ref = (SELECT codigo FROM nuevo_pk) WHERE ide_ingeg = (SELECT ide_ingeg FROM param)  "
				+ "), nuevo_pk_detalle AS ( "
				+ "   SELECT 1 AS ide,(CASE WHEN max(ide_inegd) IS NULL THEN 0 ELSE max(ide_inegd) END) + 1 AS codigo FROM bodt_ingreso_egreso_det "
				+ ") " + "   INSERT INTO bodt_ingreso_egreso_det( "
				+ "            ide_inegd, ide_bocam, ide_ingeg, ide_inttr, ide_addef, ide_afest,  "
				+ "            costo_unitario_inegd, costo_unitario_inc_iva_inegd, subtotal_inegd, valor_iva_inegd, cantidad_inegd,  "
				+ "            aplica_iva_inegd, total_inegd, usuario_ingre, fecha_ingre, hora_ingre,  "
				+ "            usuario_actua, fecha_actua, hora_actua, marca_inegd, modelo_inegd,  "
				+ "            color_inegd, fecha_vencimiento_inegd, peligro_salud_inegd, peligro_inflamabilidad_inegd,  "
				+ "            peligro_reactividad_inegd, manejo_especial_inegd, saldo_disponible_inegd,  "
				+ "            ide_bounm, ide_bounm_presentacion, valor_existencia_inegd) "
				+ "    SELECT (SELECT codigo FROM nuevo_pk_detalle) +ROW_NUMBER() OVER (ORDER BY ide_inegd) -1 , ide_bocam, (SELECT codigo FROM nuevo_pk) AS ide_ingeg, (SELECT ide_inttr_t_ingreso FROM param) as ide_inttr, ide_addef, ide_afest,  "
				+ "       costo_unitario_inegd, costo_unitario_inc_iva_inegd, subtotal_inegd, valor_iva_inegd, cantidad_inegd,  "
				+ "       aplica_iva_inegd, total_inegd, usuario_ingre, fecha_ingre, hora_ingre,  "
				+ "       usuario_actua, fecha_actua, hora_actua, marca_inegd, modelo_inegd,  "
				+ "       color_inegd, fecha_vencimiento_inegd, peligro_salud_inegd, peligro_inflamabilidad_inegd,  "
				+ "       peligro_reactividad_inegd, manejo_especial_inegd, saldo_disponible_inegd,  "
				+ "       ide_bounm, ide_bounm_presentacion, valor_existencia_inegd "
				+ "   FROM bodt_ingreso_egreso_det  "
				+ "   WHERE ide_ingeg = (SELECT ide_ingeg FROM param) AND ide_inttr = (SELECT ide_inttr_t_egreso FROM param) ",
				ide_inttr_t_ingreso, ide_inttr_t_egreso, ide_ingeg);
		utilitario.getConexion().ejecutarSql(sql);

	}

	public String getSqlDescripcionPoaCertificacion(String ide_prcer) {
		return "SELECT ide_prpoc, codigo_clasificador_prcla || ' ' || descripcion_clasificador_prcla "
				+ "FROM pre_poa_certificacion  "
				+ "LEFT JOIN pre_poa ON pre_poa.ide_prpoa = pre_poa_certificacion.ide_prpoa "
				+ "LEFT JOIN pre_clasificador ON pre_clasificador.ide_prcla = pre_poa.ide_prcla " + "WHERE ide_prcer = "
				+ ide_prcer;
	}

	public String getSqlDescripcionPoaCertificacionPorAnio(String ide_geani) {
		return "SELECT ide_prpoc, codigo_clasificador_prcla || ' ' || descripcion_clasificador_prcla "
				+ "FROM pre_poa_certificacion  "
				+ "LEFT JOIN pre_poa ON pre_poa.ide_prpoa = pre_poa_certificacion.ide_prpoa "
				+ "LEFT JOIN pre_clasificador ON pre_clasificador.ide_prcla = pre_poa.ide_prcla "
				+ "LEFT JOIN pre_certificacion ON pre_poa_certificacion.ide_prcer = pre_certificacion.ide_prcer "
				+ "WHERE pre_certificacion.ide_geani = " + ide_geani;
	}

	public TablaGenerica getClasificadorDeDetalleCertificacion(String ide_prpoc) {
		String sql = "SELECT ide_prpoc, ide_prcla " + "FROM pre_poa_certificacion  "
				+ "LEFT JOIN pre_poa ON pre_poa.ide_prpoa = pre_poa_certificacion.ide_prpoa " + "WHERE ide_prpoc="
				+ ide_prpoc;

		return utilitario.consultar(sql);
	}

	/**
	 * Actualizar el valor de existencia de una transacción, se actualizar el valor
	 * de total de acuerdo al nuevo valor de existencia
	 * 
	 * @param ide_inegd
	 * @param valor_existencia_inegd
	 */
	public void actualizarValorExistencia(String ide_inegd, String valor_existencia_inegd) {
		String sql = String.format(
				"UPDATE bodt_ingreso_egreso_det SET valor_existencia_inegd = %s, total_inegd = cantidad_inegd * %s WHERE ide_inegd = %s",
				// param
				valor_existencia_inegd, valor_existencia_inegd, ide_inegd);
		utilitario.getConexion().ejecutarSql(sql);
	}

	/**
	 * Recalcular los valores de la cabecera de una transacción
	 * 
	 * @param ide_ingeg
	 */
	public void recalcularCabeceraDeTransaccion(String ide_ingeg) {
		String sql = String.format("UPDATE bodt_ingreso_egreso  "
				+ "  SET subtotal_ingeg = (SELECT SUM(subtotal_inegd) FROM bodt_ingreso_egreso_det WHERE ide_ingeg = %s ),  "
				+ "  valor_iva_ingeg = (SELECT SUM(valor_iva_inegd) FROM bodt_ingreso_egreso_det WHERE ide_ingeg = %s ),  "
				+ "  total_ingeg =   (SELECT SUM(total_inegd) FROM bodt_ingreso_egreso_det WHERE ide_ingeg = %s ) "
				+ "  WHERE ide_ingeg = %s ",
				// param
				ide_ingeg, ide_ingeg, ide_ingeg, ide_ingeg);
		utilitario.getConexion().ejecutarSql(sql);
	}

	/**
	 * Actualiza la tabla de resumen con el pmp calculado hasta determinada
	 * transacción
	 * 
	 * @param ide_bocam
	 * @param ide_inegd
	 */
	public void actualizarPmpDeItemHastaTransaccion(String ide_bocam, String ide_inegd) {
		String sql = String.format("WITH param AS ( " + "    SELECT  " + "    14 as ide_geani,  "
				+ "    %s as ide_bocam, " + "    1 as ide_inttr_ingreso, " + "    2 as ide_inttr_egreso, "
				+ "    %s as ide_inegd_limite " + "), inicial AS ( " + "SELECT  " + " ide_geani, " + " ide_bocam, "
				+ " COUNT(ide_boubi), " + " SUM(cantidad_inicial_boinv) AS cantidad, "
				+ " COALESCE( SUM(cantidad_inicial_boinv*valor_existencia_inicial_sin_iva_boinv), 0) AS subtotal, "
				+ " COALESCE( SUM(cantidad_inicial_boinv*valor_existencia_inicial_inc_iva_boinv), 0) AS total, "
				+ " COALESCE( SUM(cantidad_inicial_boinv*valor_existencia_inicial_boinv), 0) AS total_existencias "
				+ "FROM bodt_bodega_inventario " + "WHERE  " + "    ide_geani=(SELECT ide_geani FROM param) "
				+ "    AND ide_bocam=(SELECT ide_bocam FROM param) " + "GROUP BY ide_geani, ide_bocam " + "),  "
				+ "ingresos AS ( " + " SELECT " + "  bodt_ingreso_egreso.ide_geani, "
				+ "  bodt_ingreso_egreso_det.ide_bocam, " + "  COUNT(bodt_ingreso_egreso.ide_geani), "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd) AS cantidad, "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inegd), 0) AS subtotal, "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inc_iva_inegd), 0) AS total, "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.valor_existencia_inegd), 0) AS total_existencias "
				+ " FROM bodt_ingreso_egreso_det "
				+ " JOIN bodt_ingreso_egreso ON bodt_ingreso_egreso.ide_ingeg = bodt_ingreso_egreso_det.ide_ingeg "
				+ " WHERE  " + "    bodt_ingreso_egreso.ide_geani=(SELECT ide_geani FROM param) "
				+ "    AND bodt_ingreso_egreso_det.ide_bocam=(SELECT ide_bocam FROM param) "
				+ "    AND bodt_ingreso_egreso.ide_inttr = (SELECT ide_inttr_ingreso FROM param) "
				+ "    AND bodt_ingreso_egreso.activo_ingeg = true "
				+ "    AND bodt_ingreso_egreso_det.ide_inegd <= (SELECT ide_inegd_limite FROM param) "
				+ " GROUP BY bodt_ingreso_egreso.ide_geani, " + "  bodt_ingreso_egreso_det.ide_bocam " + "), "
				+ "egresos AS ( " + " SELECT " + "  bodt_ingreso_egreso.ide_geani, "
				+ "  bodt_ingreso_egreso_det.ide_bocam, " + "  count(bodt_ingreso_egreso.ide_geani), "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd) AS cantidad, "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inegd), 0) AS subtotal, "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inc_iva_inegd), 0) AS total, "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.valor_existencia_inegd), 0) AS total_existencias "
				+ " FROM bodt_ingreso_egreso_det "
				+ " JOIN bodt_ingreso_egreso ON bodt_ingreso_egreso.ide_ingeg = bodt_ingreso_egreso_det.ide_ingeg "
				+ " WHERE  " + "    bodt_ingreso_egreso.ide_geani=(SELECT ide_geani FROM param) "
				+ "    AND bodt_ingreso_egreso_det.ide_bocam=(SELECT ide_bocam FROM param) "
				+ "    AND bodt_ingreso_egreso.ide_inttr = (SELECT ide_inttr_egreso FROM param) "
				+ "    AND bodt_ingreso_egreso.activo_ingeg = true "
				+ "    AND bodt_ingreso_egreso_det.ide_inegd <= (SELECT ide_inegd_limite FROM param) "
				+ " GROUP BY bodt_ingreso_egreso.ide_geani, " + "  bodt_ingreso_egreso_det.ide_bocam " + "), "
				+ "resumen AS ( " + "SELECT  " + " " + "COALESCE(inicial.cantidad, 0) as inicial_cantidad, "
				+ "COALESCE(inicial.subtotal, 0) as inicial_subtotal, "
				+ "COALESCE(inicial.total, 0) as inicial_total, "
				+ "COALESCE(ingresos.cantidad, 0) as ingresos_cantidad, "
				+ "COALESCE(ingresos.subtotal, 0) as ingresos_subtotal, "
				+ "COALESCE(ingresos.total, 0) as ingresos_total, "
				+ "COALESCE(ingresos.total_existencias, 0) as ingresos_total_existencias, "
				+ "COALESCE(egresos.cantidad, 0) as egresos_cantidad, "
				+ "COALESCE(egresos.subtotal, 0) as egresos_subtotal, "
				+ "COALESCE(egresos.total, 0) as egresos_total, "
				+ "COALESCE ( (COALESCE(inicial.subtotal, 0) + COALESCE(ingresos.subtotal, 0) - COALESCE(egresos.subtotal, 0)) / NULLIF(COALESCE(inicial.cantidad, 0) + COALESCE(ingresos.cantidad, 0) - COALESCE(egresos.cantidad, 0),0),0) AS pmp, "
				+ "COALESCE ( (COALESCE(inicial.total, 0) + COALESCE(ingresos.total, 0) - COALESCE(egresos.total, 0)) / NULLIF(COALESCE(inicial.cantidad, 0) + COALESCE(ingresos.cantidad, 0) - COALESCE(egresos.cantidad, 0),0),0) AS pmp_inc_iva, "
				+ "COALESCE ( (COALESCE(inicial.total_existencias, 0) + COALESCE(ingresos.total_existencias, 0) - COALESCE(egresos.total_existencias, 0)) / NULLIF(COALESCE(inicial.cantidad, 0) + COALESCE(ingresos.cantidad, 0) - COALESCE(egresos.cantidad, 0),0),0) AS pmp_existencia "
				+ "FROM  " + "inicial "
				+ "FULL OUTER JOIN ingresos ON ingresos.ide_geani = inicial.ide_geani AND ingresos.ide_bocam = inicial.ide_bocam "
				+ "FULL OUTER JOIN egresos ON egresos.ide_geani = inicial.ide_geani AND egresos.ide_bocam = inicial.ide_bocam "
				+ ") " + " " + "UPDATE bodt_inventario_resumen " + "SET " + "costo_medio_unidad_inres = resumen.pmp, "
				+ "costo_medio_unidad_inc_iva_inres = resumen.pmp_inc_iva, "
				+ "pmp_existencia_inres = resumen.pmp_existencia, " + "usuario_actua = '', " + "fecha_actua = null, "
				+ "hora_actua = null " + "FROM resumen " + "WHERE  " + "ide_geani = (SELECT ide_geani FROM param) "
				+ "AND ide_bocam = (SELECT ide_bocam FROM param); ",
				// param
				ide_bocam, ide_inegd);
		utilitario.getConexion().ejecutarSql(sql);
	}

	public void trigRegistraInventarioResumen(String ide_geani, String ide_bocam) {
		String usuario = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("nick_usua");
		String fecha = utilitario.getFechaActual();
		String hora = utilitario.getHoraActual();
		TablaGenerica tab_maximo = utilitario
				.consultar(ser_contabilidad.servicioCodigoMaximo("bodt_inventario_resumen", "ide_inres"));
		String ide_inres = tab_maximo.getValor("codigo");
		String sql = String.format("INSERT INTO bodt_inventario_resumen( "
				+ "            ide_inres, ide_bocam, ide_geani,  "
				+ "            costo_medio_unidad_inres, costo_medio_unidad_inc_iva_inres, pmp_existencia_inres,"
				+ "            usuario_ingre, fecha_ingre, hora_ingre,  "
				+ "            usuario_actua, fecha_actua, hora_actua) " + "    VALUES (%s ,%s, %s, "
				+ "    		0, 0, 0, " + "    		'%s', '%s', '%s', " + "    		'%s', '%s', '%s'); ", ide_inres,
				ide_bocam, ide_geani, usuario, fecha, hora, usuario, fecha, hora);
		utilitario.getConexion().ejecutarSql(sql);
	}

	public void trigActualizarEgresosInventario(String ide_geani, String ide_bocam, String ide_boubi) {
		String usuario = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("nick_usua");
		String fecha = utilitario.getFechaActual();
		String hora = utilitario.getHoraActual();
		String sql = String.format("WITH  param AS ( " + "    SELECT  " + "    %s as ide_geani,  "
				+ "    %s as ide_bocam, " + "    %s as ide_boubi, " + "    %s as ide_inttr, " + "    %s as ide_inttr_t "
				+ "), " + "egresos AS ( " + " SELECT " + "  bodt_ingreso_egreso.ide_geani, "
				+ "  bodt_ingreso_egreso_det.ide_bocam, " + "  bodt_ingreso_egreso.ide_boubi, "
				+ "  count(bodt_ingreso_egreso.ide_geani), "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd) AS cantidad, "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inegd) AS costo_acumulado_ingreso, "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inc_iva_inegd) AS costo_acumulado_ingreso_inc_iva, "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.valor_existencia_inegd) AS costo_acumulado_ingreso_valor_existencia "
				+ " FROM bodt_ingreso_egreso_det  "
				+ " JOIN bodt_ingreso_egreso ON bodt_ingreso_egreso.ide_ingeg = bodt_ingreso_egreso_det.ide_ingeg  "
				+ " WHERE   " + "    bodt_ingreso_egreso.ide_geani = (SELECT ide_geani FROM param) "
				+ "    AND bodt_ingreso_egreso_det.ide_bocam= (SELECT ide_bocam FROM param) "
				+ "    AND bodt_ingreso_egreso.ide_inttr = (SELECT ide_inttr FROM param) "
				+ "    AND bodt_ingreso_egreso.ide_boubi = (SELECT ide_boubi FROM param) "
				+ "    AND bodt_ingreso_egreso.activo_ingeg = true  " + " GROUP BY bodt_ingreso_egreso.ide_geani,  "
				+ "  bodt_ingreso_egreso_det.ide_bocam, bodt_ingreso_egreso.ide_boubi " + "), " + "egresos_t AS ( "
				+ " SELECT " + "  bodt_ingreso_egreso.ide_geani, " + "  bodt_ingreso_egreso_det.ide_bocam, "
				+ "  bodt_ingreso_egreso.ide_boubi, " + "  count(bodt_ingreso_egreso.ide_geani), "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd) AS cantidad, "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inegd) AS costo_acumulado_ingreso, "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inc_iva_inegd) AS costo_acumulado_ingreso_inc_iva, "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.valor_existencia_inegd) AS costo_acumulado_ingreso_valor_existencia "
				+ " FROM bodt_ingreso_egreso_det  "
				+ " JOIN bodt_ingreso_egreso ON bodt_ingreso_egreso.ide_ingeg = bodt_ingreso_egreso_det.ide_ingeg  "
				+ " WHERE   " + "    bodt_ingreso_egreso.ide_geani = (SELECT ide_geani FROM param) "
				+ "    AND bodt_ingreso_egreso_det.ide_bocam= (SELECT ide_bocam FROM param) "
				+ "    AND bodt_ingreso_egreso.ide_inttr = (SELECT ide_inttr_t FROM param) "
				+ "    AND bodt_ingreso_egreso.ide_boubi = (SELECT ide_boubi FROM param) "
				+ "    AND bodt_ingreso_egreso.activo_ingeg = true  " + " GROUP BY bodt_ingreso_egreso.ide_geani,  "
				+ "  bodt_ingreso_egreso_det.ide_bocam, bodt_ingreso_egreso.ide_boubi " + "),  " + "egresos_baja AS ( "
				+ " SELECT " + "  bodt_ingreso_egreso.ide_geani, " + "  bodt_ingreso_egreso_det.ide_bocam, "
				+ "  bodt_ingreso_egreso.ide_boubi, " + "  count(bodt_ingreso_egreso.ide_geani), "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd) AS cantidad, "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.valor_existencia_inegd) AS costo_acumulado_egreso_valor_existencia "
				+ " FROM bodt_ingreso_egreso_det  "
				+ " JOIN bodt_ingreso_egreso ON bodt_ingreso_egreso.ide_ingeg = bodt_ingreso_egreso_det.ide_ingeg  "
				+ " WHERE   " + "    bodt_ingreso_egreso.ide_geani = (SELECT ide_geani FROM param) "
				+ "    AND bodt_ingreso_egreso_det.ide_bocam= (SELECT ide_bocam FROM param) "
				+ "    AND bodt_ingreso_egreso.ide_inttr = 6 "
				+ "    AND bodt_ingreso_egreso.ide_boubi = (SELECT ide_boubi FROM param) "
				+ "    AND bodt_ingreso_egreso.activo_ingeg = true  " + " GROUP BY bodt_ingreso_egreso.ide_geani,  "
				+ "  bodt_ingreso_egreso_det.ide_bocam, bodt_ingreso_egreso.ide_boubi " + ")  " + "  "
				+ "UPDATE bodt_bodega_inventario  " + "SET  "
				+ "cantidad_egreso_boinv = COALESCE(egresos.cantidad, 0), "
				+ "costo_egreso_sin_iva_boinv = COALESCE(egresos.costo_acumulado_ingreso,0),  "
				+ "costo_egreso_inc_iva_boinv = COALESCE(egresos.costo_acumulado_ingreso_inc_iva,0), "
				+ "costo_egreso_boinv = COALESCE(egresos.costo_acumulado_ingreso_valor_existencia,0) ,  " + " "
				+ "cantidad_egreso_t_boinv = COALESCE(egresos_t.cantidad, 0), "
				+ "costo_egreso_t_sin_iva_boinv = COALESCE(egresos_t.costo_acumulado_ingreso,0),  "
				+ "costo_egreso_t_inc_iva_boinv = COALESCE(egresos_t.costo_acumulado_ingreso_inc_iva,0), "
				+ "costo_egreso_t_boinv = COALESCE(egresos_t.costo_acumulado_ingreso_valor_existencia,0) ,  " + " "
				+ "cantidad_egreso_baja_boinv = COALESCE(egresos_baja.cantidad, 0), "
				+ "costo_egreso_baja_boinv = COALESCE(egresos_baja.costo_acumulado_egreso_valor_existencia,0),  " +

				"usuario_actua = '%s',  " + "fecha_actua = '%s',  " + "hora_actua = '%s' " + "FROM egresos  "
				+ "FULL OUTER JOIN egresos_t ON egresos.ide_bocam = egresos_t.ide_bocam AND egresos.ide_boubi = egresos_t.ide_boubi AND egresos.ide_geani = egresos_t.ide_geani "
				+ "FULL OUTER JOIN egresos_baja ON egresos.ide_bocam = egresos_baja.ide_bocam AND egresos.ide_boubi = egresos_baja.ide_boubi AND egresos.ide_geani = egresos_baja.ide_geani "
				+ "FULL OUTER JOIN param ON param.ide_geani = egresos.ide_geani AND egresos.ide_bocam = param.ide_bocam AND egresos.ide_boubi = param.ide_boubi "
				+ "WHERE   " + "bodt_bodega_inventario.ide_geani = (SELECT ide_geani FROM param) "
				+ "AND bodt_bodega_inventario.ide_bocam = (SELECT ide_bocam FROM param) "
				+ "AND bodt_bodega_inventario.ide_boubi = (SELECT ide_boubi FROM param) ",
				// Param
				ide_geani, ide_bocam, ide_boubi, this.TRANSACCION_EGRESO, this.TRANSACCION_TRANSFERENCIA_EGRESO,
				// Update
				usuario, fecha, hora);

		utilitario.getConexion().ejecutarSql(sql);

	}

	public void trigActualizarIngresosInventario(String ide_geani, String ide_bocam, String ide_boubi) {
		// System.out.println("trigActualizarIngresosInventario");
		String usuario = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("nick_usua");
		String fecha = utilitario.getFechaActual();
		String hora = utilitario.getHoraActual();
		String sql = String.format("WITH param AS ( " + "    SELECT " + "    %s as ide_geani, "
				+ "    %s as ide_bocam, " + "    %s as ide_boubi, " + "    %s as ide_inttr, " + "    %s as ide_inttr_t "
				+ "), " + "ingresos AS ( " + " SELECT " + "  bodt_ingreso_egreso.ide_geani, "
				+ "  bodt_ingreso_egreso_det.ide_bocam, " + "  bodt_ingreso_egreso.ide_boubi, "
				+ "  count(bodt_ingreso_egreso.ide_geani), "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd) AS cantidad, "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inegd) AS costo_acumulado_ingreso, "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inc_iva_inegd) AS costo_acumulado_ingreso_inc_iva, "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.valor_existencia_inegd) AS costo_acumulado_ingreso_valor_existencia "
				+ " FROM bodt_ingreso_egreso_det "
				+ " JOIN bodt_ingreso_egreso ON bodt_ingreso_egreso.ide_ingeg = bodt_ingreso_egreso_det.ide_ingeg "
				+ " WHERE  " + "    bodt_ingreso_egreso.ide_geani= (SELECT ide_geani FROM param) "
				+ "    AND bodt_ingreso_egreso_det.ide_bocam = (SELECT ide_bocam FROM param) "
				+ "    AND bodt_ingreso_egreso.ide_inttr = (SELECT ide_inttr FROM param) "
				+ "    AND bodt_ingreso_egreso.ide_boubi = (SELECT ide_boubi FROM param) "
				+ "    AND bodt_ingreso_egreso.activo_ingeg = true " + " GROUP BY bodt_ingreso_egreso.ide_geani, "
				+ "  bodt_ingreso_egreso_det.ide_bocam, bodt_ingreso_egreso.ide_boubi " + "), " + "ingresos_t AS ( "
				+ " SELECT " + "  bodt_ingreso_egreso.ide_geani, " + "  bodt_ingreso_egreso_det.ide_bocam, "
				+ "  bodt_ingreso_egreso.ide_boubi, " + "  count(bodt_ingreso_egreso.ide_geani), "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd) AS cantidad, "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inegd) AS costo_acumulado_ingreso, "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inc_iva_inegd) AS costo_acumulado_ingreso_inc_iva, "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.valor_existencia_inegd) AS costo_acumulado_ingreso_valor_existencia "
				+ " FROM bodt_ingreso_egreso_det "
				+ " JOIN bodt_ingreso_egreso ON bodt_ingreso_egreso.ide_ingeg = bodt_ingreso_egreso_det.ide_ingeg "
				+ " WHERE  " + "    bodt_ingreso_egreso.ide_geani= (SELECT ide_geani FROM param) "
				+ "    AND bodt_ingreso_egreso_det.ide_bocam = (SELECT ide_bocam FROM param) "
				+ "    AND bodt_ingreso_egreso.ide_inttr = (SELECT ide_inttr_t FROM param) "
				+ "    AND bodt_ingreso_egreso.ide_boubi = (SELECT ide_boubi FROM param) "
				+ "    AND bodt_ingreso_egreso.activo_ingeg = true " + " GROUP BY bodt_ingreso_egreso.ide_geani, "
				+ "  bodt_ingreso_egreso_det.ide_bocam, bodt_ingreso_egreso.ide_boubi " + ") " + " "
				+ "UPDATE bodt_bodega_inventario " + "SET "
				+ "cantidad_ingreso_boinv = COALESCE(ingresos.cantidad, 0), "
				+ "costo_ingreso_sin_iva_boinv = COALESCE(ingresos.costo_acumulado_ingreso,0) ,  "
				+ "costo_ingreso_inc_iva_boinv = COALESCE(ingresos.costo_acumulado_ingreso_inc_iva,0),  "
				+ "costo_ingreso_boinv = COALESCE(ingresos.costo_acumulado_ingreso_valor_existencia,0) ,  " + " "
				+ "cantidad_ingreso_t_boinv = COALESCE(ingresos_t.cantidad, 0), "
				+ "costo_ingreso_t_sin_iva_boinv = COALESCE(ingresos_t.costo_acumulado_ingreso,0) ,  "
				+ "costo_ingreso_t_inc_iva_boinv = COALESCE(ingresos_t.costo_acumulado_ingreso_inc_iva,0),  "
				+ "costo_ingreso_t_boinv = COALESCE(ingresos_t.costo_acumulado_ingreso_valor_existencia,0) ,  "
				+ "usuario_actua = '%s', " + "fecha_actua = '%s', " + "hora_actua = '%s' " + "FROM ingresos "
				+ "FULL OUTER JOIN ingresos_t ON ingresos.ide_bocam = ingresos_t.ide_bocam AND ingresos.ide_boubi = ingresos_t.ide_boubi AND ingresos.ide_geani = ingresos_t.ide_geani "
				+ "FULL OUTER JOIN param ON param.ide_geani = ingresos.ide_geani AND ingresos.ide_bocam = param.ide_bocam AND ingresos.ide_boubi = param.ide_boubi "
				+ "WHERE  " + "bodt_bodega_inventario.ide_geani = (SELECT ide_geani FROM param) "
				+ "AND bodt_bodega_inventario.ide_bocam = (SELECT ide_bocam FROM param) "
				+ "AND bodt_bodega_inventario.ide_boubi = (SELECT ide_boubi FROM param)  ",
				// param
				ide_geani, ide_bocam, ide_boubi, this.TRANSACCION_INGRESO, this.TRANSACCION_TRANSFERENCIA_INGRESO,

				// Update
				usuario, fecha, hora);

		utilitario.getConexion().ejecutarSql(sql);

	}

	public void trigActualizarSaldoInventario(String ide_geani, String ide_bocam, String ide_boubi) {
		// System.out.println("trigActualizarSaldoInventario");
		String usuario = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("nick_usua");
		String fecha = utilitario.getFechaActual();
		String hora = utilitario.getHoraActual();
		String sql = String.format("WITH param AS ( " + "    SELECT " + "    %s as ide_geani, "
				+ "    %s as ide_bocam, " + "    %s as ide_boubi, " + "    %s as ide_inttr_ingreso, "
				+ "    %s as ide_inttr_egreso, " + "    %s as ide_inttr_t_ingreso, " + "    %s as ide_inttr_t_egreso "
				+ "),inicial AS (  " + "SELECT   " + " ide_geani,  " + " ide_bocam,  " + " ide_boubi, "
				+ " SUM(cantidad_inicial_boinv) AS cantidad,  "
				+ " COALESCE( SUM(costo_inicial_sin_iva_boinv), 0) AS costo_inicial_sin_iva,  "
				+ " COALESCE( SUM(valor_existencia_inicial_inc_iva_boinv), 0) AS costo_inicial_inc_iva,  "
				+ " COALESCE( SUM(costo_inicial_boinv), 0) AS costo_inicial " + "FROM bodt_bodega_inventario  "
				+ "WHERE   " + "    ide_geani=(SELECT ide_geani FROM param)  "
				+ "    AND ide_bocam=(SELECT ide_bocam FROM param)  "
				+ "    AND ide_boubi=(SELECT ide_boubi FROM param)  " + "GROUP BY ide_geani, ide_bocam , ide_boubi "
				+ ") " + "UPDATE bodt_bodega_inventario " + "SET "
				+ "cantidad_saldo_boinv = COALESCE(inicial.cantidad, 0) + COALESCE(cantidad_ingreso_boinv, 0) + COALESCE(cantidad_ingreso_t_boinv, 0) - COALESCE(cantidad_egreso_boinv, 0) - COALESCE(cantidad_egreso_t_boinv, 0) - COALESCE(cantidad_egreso_baja_boinv, 0),  "
				+ "costo_saldo_sin_iva_boinv = COALESCE(inicial.costo_inicial_sin_iva, 0) + COALESCE(costo_ingreso_sin_iva_boinv, 0) + COALESCE(costo_ingreso_t_sin_iva_boinv, 0) - COALESCE(costo_egreso_sin_iva_boinv, 0) - COALESCE(costo_egreso_t_sin_iva_boinv, 0),  "
				+ "costo_saldo_inc_iva_boinv = COALESCE(inicial.costo_inicial_inc_iva, 0) + COALESCE(costo_ingreso_inc_iva_boinv, 0) + COALESCE(costo_ingreso_t_inc_iva_boinv, 0) - COALESCE(costo_egreso_inc_iva_boinv, 0) - COALESCE(costo_egreso_t_inc_iva_boinv, 0),  "
				+ "costo_saldo_boinv = COALESCE(inicial.costo_inicial, 0) + COALESCE(costo_ingreso_boinv, 0) + COALESCE(costo_ingreso_t_boinv, 0) - COALESCE(costo_egreso_boinv, 0) - COALESCE(costo_egreso_t_boinv, 0) - COALESCE(costo_egreso_baja_boinv, 0),  "
				+ "usuario_actua = '%s', " + "fecha_actua = '%s', " + "hora_actua = '%s' " + "FROM inicial "
				+ "FULL OUTER JOIN param ON inicial.ide_geani = param.ide_geani AND inicial.ide_boubi=param.ide_boubi AND inicial.ide_bocam = param.ide_bocam "
				+ "WHERE  " + "bodt_bodega_inventario.ide_geani = (SELECT ide_geani FROM param) "
				+ "AND bodt_bodega_inventario.ide_bocam = (SELECT ide_bocam FROM param) "
				+ "AND bodt_bodega_inventario.ide_boubi = (SELECT ide_boubi FROM param)  ",
				// Params
				ide_geani, ide_bocam, ide_boubi, this.TRANSACCION_INGRESO, this.TRANSACCION_EGRESO,
				this.TRANSACCION_TRANSFERENCIA_INGRESO, this.TRANSACCION_TRANSFERENCIA_EGRESO,

				// Update
				usuario, fecha, hora);

		utilitario.getConexion().ejecutarSql(sql);

	}

	public void trigActualizarCostoMedioPonderado(String ide_geani, String ide_bocam) {
		// System.out.println("trigActualizarCostoMedioPonderado");
		String usuario = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("nick_usua");
		String fecha = utilitario.getFechaActual();
		String hora = utilitario.getHoraActual();
		String sql = String.format("WITH param AS (  " + "    SELECT   " + "    %s as ide_geani,   "
				+ "    %s as ide_bocam,  " + "    %s as ide_inttr_ingreso,  " + "    %s as ide_inttr_egreso  " + "),  "
				+ "limite AS ( " + " SELECT MAX(ide_inegd) as ide_inegd_limite "
				+ " FROM bodt_ingreso_egreso_det as detalle "
				+ " LEFT JOIN bodt_ingreso_egreso as ingreso ON ingreso.ide_ingeg = detalle.ide_ingeg "
				+ " WHERE detalle.ide_bocam = (SELECT ide_bocam FROM param)  AND detalle.ide_inttr=1 AND ingreso.ide_geani =(SELECT ide_geani FROM param) AND ingreso.activo_ingeg = true "
				+ "), " + "inicial AS (  " + "SELECT   " + " ide_geani,  " + " ide_bocam,  " + " COUNT(ide_boubi),  "
				+ " SUM(cantidad_inicial_boinv) AS cantidad,  "
				+ " COALESCE( SUM(cantidad_inicial_boinv*valor_existencia_inicial_sin_iva_boinv), 0) AS subtotal,  "
				+ " COALESCE( SUM(cantidad_inicial_boinv*valor_existencia_inicial_inc_iva_boinv), 0) AS total,  "
				+ " COALESCE( SUM(cantidad_inicial_boinv*valor_existencia_inicial_boinv), 0) AS total_existencias  "
				+ "FROM bodt_bodega_inventario  " + "WHERE   " + "    ide_geani=(SELECT ide_geani FROM param)  "
				+ "    AND ide_bocam=(SELECT ide_bocam FROM param)  " + "GROUP BY ide_geani, ide_bocam  " + "),   "
				+ "ingresos AS (  " + " SELECT  " + "  bodt_ingreso_egreso.ide_geani,  "
				+ "  bodt_ingreso_egreso_det.ide_bocam,  " + "  COUNT(bodt_ingreso_egreso.ide_geani),  "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd) AS cantidad,  "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inegd), 0) AS subtotal,  "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inc_iva_inegd), 0) AS total,  "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.valor_existencia_inegd), 0) AS total_existencias  "
				+ " FROM bodt_ingreso_egreso_det  "
				+ " JOIN bodt_ingreso_egreso ON bodt_ingreso_egreso.ide_ingeg = bodt_ingreso_egreso_det.ide_ingeg  "
				+ " WHERE   " + "    bodt_ingreso_egreso.ide_geani=(SELECT ide_geani FROM param)  "
				+ "    AND bodt_ingreso_egreso_det.ide_bocam=(SELECT ide_bocam FROM param)  "
				+ "    AND bodt_ingreso_egreso.ide_inttr = (SELECT ide_inttr_ingreso FROM param)  "
				+ "    AND bodt_ingreso_egreso.activo_ingeg = true  "
				+ "    AND bodt_ingreso_egreso_det.ide_inegd <= (SELECT ide_inegd_limite FROM limite) "
				+ " GROUP BY bodt_ingreso_egreso.ide_geani,  " + "  bodt_ingreso_egreso_det.ide_bocam  " + "),  "
				+ "egresos AS (  " + " SELECT  " + "  bodt_ingreso_egreso.ide_geani,  "
				+ "  bodt_ingreso_egreso_det.ide_bocam,  " + "  count(bodt_ingreso_egreso.ide_geani),  "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd) AS cantidad,  "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inegd), 0) AS subtotal,  "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inc_iva_inegd), 0) AS total,  "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.valor_existencia_inegd), 0) AS total_existencias  "
				+ " FROM bodt_ingreso_egreso_det  "
				+ " JOIN bodt_ingreso_egreso ON bodt_ingreso_egreso.ide_ingeg = bodt_ingreso_egreso_det.ide_ingeg  "
				+ " WHERE   " + "    bodt_ingreso_egreso.ide_geani=(SELECT ide_geani FROM param)  "
				+ "    AND bodt_ingreso_egreso_det.ide_bocam=(SELECT ide_bocam FROM param)  "
				+ "    AND bodt_ingreso_egreso.ide_inttr = (SELECT ide_inttr_egreso FROM param)  "
				+ "    AND bodt_ingreso_egreso.activo_ingeg = true  "
				+ "    AND bodt_ingreso_egreso_det.ide_inegd <= (SELECT ide_inegd_limite FROM limite) "
				+ " GROUP BY bodt_ingreso_egreso.ide_geani,  " + "  bodt_ingreso_egreso_det.ide_bocam  " + "),  "
				+ "resumen AS (  " + "SELECT   " + "  " + "COALESCE(inicial.cantidad, 0) as inicial_cantidad,  "
				+ "COALESCE(inicial.subtotal, 0) as inicial_subtotal,  "
				+ "COALESCE(inicial.total, 0) as inicial_total,  "
				+ "COALESCE(ingresos.cantidad, 0) as ingresos_cantidad,  "
				+ "COALESCE(ingresos.subtotal, 0) as ingresos_subtotal,  "
				+ "COALESCE(ingresos.total, 0) as ingresos_total,  "
				+ "COALESCE(egresos.cantidad, 0) as egresos_cantidad,  "
				+ "COALESCE(egresos.subtotal, 0) as egresos_subtotal,  "
				+ "COALESCE(egresos.total, 0) as egresos_total,  "
				+ "COALESCE ( (COALESCE(inicial.subtotal, 0) + COALESCE(ingresos.subtotal, 0) - COALESCE(egresos.subtotal, 0)) / NULLIF(COALESCE(inicial.cantidad, 0) + COALESCE(ingresos.cantidad, 0) - COALESCE(egresos.cantidad, 0),0),0) AS pmp,  "
				+ "COALESCE ( (COALESCE(inicial.total, 0) + COALESCE(ingresos.total, 0) - COALESCE(egresos.total, 0)) / NULLIF(COALESCE(inicial.cantidad, 0) + COALESCE(ingresos.cantidad, 0) - COALESCE(egresos.cantidad, 0),0),0) AS pmp_inc_iva,  "
				+ "COALESCE ( (COALESCE(inicial.total_existencias, 0) + COALESCE(ingresos.total_existencias, 0) - COALESCE(egresos.total_existencias, 0)) / NULLIF(COALESCE(inicial.cantidad, 0) + COALESCE(ingresos.cantidad, 0) - COALESCE(egresos.cantidad, 0),0),0) AS pmp_existencia  "
				+ "FROM   " + "inicial  "
				+ "FULL OUTER JOIN ingresos ON ingresos.ide_geani = inicial.ide_geani AND ingresos.ide_bocam = inicial.ide_bocam  "
				+ "FULL OUTER JOIN egresos ON egresos.ide_geani = inicial.ide_geani AND egresos.ide_bocam = inicial.ide_bocam  "
				+ ")  " + " " + "  " + "UPDATE bodt_inventario_resumen  " + "SET  "
				+ "costo_medio_unidad_inres = resumen.pmp,  "
				+ "costo_medio_unidad_inc_iva_inres = resumen.pmp_inc_iva,  "
				+ "pmp_existencia_inres = resumen.pmp_existencia,  " + "usuario_actua = '%s',  "
				+ "fecha_actua = '%s',  " + "hora_actua = '%s'  " + "FROM resumen  " + "WHERE   "
				+ "ide_geani = (SELECT ide_geani FROM param)  " + "AND ide_bocam = (SELECT ide_bocam FROM param); ",
				// params
				ide_geani, ide_bocam, this.TRANSACCION_INGRESO, this.TRANSACCION_EGRESO,
				// update
				usuario, fecha, hora);

		utilitario.getConexion().ejecutarSql(sql);

	}

	/**
	 * Obtiene un listado de los item de catalogo (ide_bocam) que tienen
	 * transacciones posterior a una fecha
	 * 
	 * @param fecha_ingeg
	 * @return
	 */
	public TablaGenerica itemsCatalogoTransaccionesDespuesDe(String fecha_ingeg) {
		String sql = "SELECT ide_bocam, ide_bocam as nunca FROM bodt_ingreso_egreso_det as detalle "
				+ " LEFT JOIN bodt_ingreso_egreso as ingreso ON ingreso.ide_ingeg = detalle.ide_ingeg "
				+ " WHERE fecha_ingeg >= '" + fecha_ingeg + "' GROUP BY 1 ORDER BY 1";
		System.out.println(sql);
		TablaGenerica tg = utilitario.consultar(sql);
		return tg;
	}

	public TablaGenerica itemsCatalogoPorAnio(String ide_geani) {
		String sql = String.format("SELECT ide_geani, ide_bocam, ide_boubi FROM bodt_ingreso_egreso_det as detalle "
				+ "LEFT JOIN bodt_ingreso_egreso as ingreso ON ingreso.ide_ingeg = detalle.ide_ingeg "
				+ "WHERE ide_geani = %s GROUP BY 1,2,3 ORDER BY 1,2,3 ", ide_geani);
		TablaGenerica tg = utilitario.consultar(sql);
		return tg;
	}

	/**
	 * Obtiene las transacciones de un ítem después de una fecha
	 * 
	 * @param fecha_ingeg
	 * @param ide_bocam
	 * @return
	 */
	public TablaGenerica transaccionesDeItemDespuesDe(String ide_bocam, String fecha_ingeg) {
		String sql = String.format("SELECT * FROM bodt_ingreso_egreso_det  "
				+ "LEFT JOIN bodt_ingreso_egreso ON bodt_ingreso_egreso.ide_ingeg = bodt_ingreso_egreso_det.ide_ingeg "
				+ " WHERE bodt_ingreso_egreso_det.ide_ingeg >= (SELECT ide_ingeg FROM bodt_ingreso_egreso WHERE fecha_ingeg >= '%s' ORDER BY 1 LIMIT 1)  "
				+ " AND ide_bocam = %s ORDER BY 1 ", fecha_ingeg, ide_bocam);
		TablaGenerica tg = utilitario.consultar(sql);
		return tg;
	}

	/**
	 * obtiene el PMP de un item (ide_bocam) hasta un ide_inegd Solo utilizar en
	 * ingresos
	 * 
	 * @param ide_bocam
	 * @param ide_inegd
	 * @return
	 */
	public TablaGenerica pmpDeItemHastaTransaccion(String ide_bocam, String ide_inegd) {
		String sql = String.format("WITH param AS ( " + "    SELECT  " + "    14 as ide_geani,  "
				+ "    %s as ide_bocam, " + "    1 as ide_inttr_ingreso, " + "    2 as ide_inttr_egreso, "
				+ "    %s as ide_inegd_limite " + "), inicial AS ( " + "SELECT  " + " ide_geani, " + " ide_bocam, "
				+ " COUNT(ide_boubi), " + " SUM(cantidad_inicial_boinv) AS cantidad, "
				+ " COALESCE( SUM(cantidad_inicial_boinv*valor_existencia_inicial_sin_iva_boinv), 0) AS subtotal, "
				+ " COALESCE( SUM(cantidad_inicial_boinv*valor_existencia_inicial_inc_iva_boinv), 0) AS total, "
				+ " COALESCE( SUM(cantidad_inicial_boinv*valor_existencia_inicial_boinv), 0) AS total_existencias "
				+ "FROM bodt_bodega_inventario " + "WHERE  " + "    ide_geani=(SELECT ide_geani FROM param) "
				+ "    AND ide_bocam=(SELECT ide_bocam FROM param) " + "GROUP BY ide_geani, ide_bocam " + "),  "
				+ "ingresos AS ( " + " SELECT " + "  bodt_ingreso_egreso.ide_geani, "
				+ "  bodt_ingreso_egreso_det.ide_bocam, " + "  COUNT(bodt_ingreso_egreso.ide_geani), "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd) AS cantidad, "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inegd), 0) AS subtotal, "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inc_iva_inegd), 0) AS total, "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.valor_existencia_inegd), 0) AS total_existencias "
				+ " FROM bodt_ingreso_egreso_det "
				+ " JOIN bodt_ingreso_egreso ON bodt_ingreso_egreso.ide_ingeg = bodt_ingreso_egreso_det.ide_ingeg "
				+ " WHERE  " + "    bodt_ingreso_egreso.ide_geani=(SELECT ide_geani FROM param) "
				+ "    AND bodt_ingreso_egreso_det.ide_bocam=(SELECT ide_bocam FROM param) "
				+ "    AND bodt_ingreso_egreso.ide_inttr = (SELECT ide_inttr_ingreso FROM param) "
				+ "    AND bodt_ingreso_egreso.activo_ingeg = true "
				+ "    AND bodt_ingreso_egreso_det.ide_inegd <= (SELECT ide_inegd_limite FROM param) "
				+ " GROUP BY bodt_ingreso_egreso.ide_geani, " + "  bodt_ingreso_egreso_det.ide_bocam " + "), "
				+ "egresos AS ( " + " SELECT " + "  bodt_ingreso_egreso.ide_geani, "
				+ "  bodt_ingreso_egreso_det.ide_bocam, " + "  count(bodt_ingreso_egreso.ide_geani), "
				+ "  SUM (bodt_ingreso_egreso_det.cantidad_inegd) AS cantidad, "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inegd), 0) AS subtotal, "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.costo_unitario_inc_iva_inegd), 0) AS total, "
				+ "  COALESCE (SUM (bodt_ingreso_egreso_det.cantidad_inegd * bodt_ingreso_egreso_det.valor_existencia_inegd), 0) AS total_existencias "
				+ " FROM bodt_ingreso_egreso_det "
				+ " JOIN bodt_ingreso_egreso ON bodt_ingreso_egreso.ide_ingeg = bodt_ingreso_egreso_det.ide_ingeg "
				+ " WHERE  " + "    bodt_ingreso_egreso.ide_geani=(SELECT ide_geani FROM param) "
				+ "    AND bodt_ingreso_egreso_det.ide_bocam=(SELECT ide_bocam FROM param) "
				+ "    AND bodt_ingreso_egreso.ide_inttr IN (2, 6) "
				+ "    AND bodt_ingreso_egreso.activo_ingeg = true "
				+ "    AND bodt_ingreso_egreso_det.ide_inegd <= (SELECT ide_inegd_limite FROM param) "
				+ " GROUP BY bodt_ingreso_egreso.ide_geani, " + "  bodt_ingreso_egreso_det.ide_bocam " + "), "
				+ "resumen AS ( " + "SELECT  " + " " + "COALESCE(inicial.cantidad, 0) as inicial_cantidad, "
				+ "COALESCE(inicial.subtotal, 0) as inicial_subtotal, "
				+ "COALESCE(inicial.total, 0) as inicial_total, "
				+ "COALESCE(ingresos.cantidad, 0) as ingresos_cantidad, "
				+ "COALESCE(ingresos.subtotal, 0) as ingresos_subtotal, "
				+ "COALESCE(ingresos.total, 0) as ingresos_total, "
				+ "COALESCE(ingresos.total_existencias, 0) as ingresos_total_existencias, "
				+ "COALESCE(egresos.cantidad, 0) as egresos_cantidad, "
				+ "COALESCE(egresos.subtotal, 0) as egresos_subtotal, "
				+ "COALESCE(egresos.total, 0) as egresos_total, "
				+ "COALESCE ( (COALESCE(inicial.subtotal, 0) + COALESCE(ingresos.subtotal, 0) - COALESCE(egresos.subtotal, 0)) / NULLIF(COALESCE(inicial.cantidad, 0) + COALESCE(ingresos.cantidad, 0) - COALESCE(egresos.cantidad, 0),0),0) AS pmp, "
				+ "COALESCE ( (COALESCE(inicial.total, 0) + COALESCE(ingresos.total, 0) - COALESCE(egresos.total, 0)) / NULLIF(COALESCE(inicial.cantidad, 0) + COALESCE(ingresos.cantidad, 0) - COALESCE(egresos.cantidad, 0),0),0) AS pmp_inc_iva, "
				+ "COALESCE ( (COALESCE(inicial.total_existencias, 0) + COALESCE(ingresos.total_existencias, 0) - COALESCE(egresos.total_existencias, 0)) / NULLIF(COALESCE(inicial.cantidad, 0) + COALESCE(ingresos.cantidad, 0) - COALESCE(egresos.cantidad, 0),0),0) AS pmp_existencia "
				+ "FROM  " + "inicial "
				+ "FULL OUTER JOIN ingresos ON ingresos.ide_geani = inicial.ide_geani AND ingresos.ide_bocam = inicial.ide_bocam "
				+ "FULL OUTER JOIN egresos ON egresos.ide_geani = inicial.ide_geani AND egresos.ide_bocam = inicial.ide_bocam "
				+ ") " + "SELECT * FROM resumen ", ide_bocam, ide_inegd);
		TablaGenerica tg = utilitario.consultar(sql);
		return tg;
	}

	public void triggerIngreso(String ide_geani, String ide_bocam, String ide_boubi, Boolean updatePMP) {
		TablaGenerica tg_inventario = this.getBodegaInventario(ide_geani, ide_bocam, ide_boubi);

		// Registrar (si no existe) item en la tabla de inventarios con valores 0
		if (tg_inventario.getTotalFilas() == 0) {
			System.out.println(String.format("No existe en el inventario, insertando (%s, %s, %s)", ide_geani,
					ide_bocam, ide_boubi));
			this.trigRegistraInventarioIngreso(ide_geani, ide_bocam, ide_boubi);
		} else if (tg_inventario.getTotalFilas() == 1) {
			System.out.println(String.format("Existe en el inventario, actualizando (%s, %s, %s)", ide_geani, ide_bocam,
					ide_boubi));
		} else {
			System.out.println(String.format("ERROR: totalFilas %s", tg_inventario.getTotalFilas()));
		}

		// Registrar (si no existe) item en la tabla de resumen con valores 0
		TablaGenerica tg_resumen = this.getInventarioResumen(ide_geani, ide_bocam);
		if (tg_resumen.getTotalFilas() == 0) {
			System.out.println(
					String.format("No existe en el resumen, insertando (%s, %s, %s)", ide_geani, ide_bocam, ide_boubi));
			this.trigRegistraInventarioResumen(ide_geani, ide_bocam);
		}

		// Actualizar los ingresos en la tabla de inventarios
		this.trigActualizarIngresosInventario(ide_geani, ide_bocam, ide_boubi);

		// Actualizar los ingresos en la tabla de inventarios
		this.trigActualizarEgresosInventario(ide_geani, ide_bocam, ide_boubi);

		// Actualizar costo medio ponderado
		// Se actualiza cuando se produce un ingreso
		if (updatePMP) {
			this.trigActualizarCostoMedioPonderado(ide_geani, ide_bocam);
		}

		// Actualizar saldos en la tabla inventarios
		this.trigActualizarSaldoInventario(ide_geani, ide_bocam, ide_boubi);

		if (!updatePMP) {
			// Es un egreso
			this.enviarAlertas(ide_bocam, ide_geani, ide_boubi);
		}

	}

	public TablaGenerica getCatalogoPorId(String ide_bocam) {
		String sql = "SELECT ide_bocam, con_ide_bocam, cat_codigo_bocam, descripcion_bocam,  "
				+ "       nivel_bocam, grupo_nivel_bocam, activo_bocam, usuario_ingre,  "
				+ "       fecha_ingre, hora_ingre, usuario_actua, fecha_actua, hora_actua,  "
				+ "       ide_prcla, aplica_aprobacion, aplica_al_gasto, ide_bounm, vida_util_bocam,  "
				+ "       ide_bounm_presentacion, peligro_salud_bocam, peligro_inflamabilidad_bocam,  "
				+ "       peligro_reactividad_bocam, manejo_especial_bocam " + "  FROM bodt_catalogo_material "
				+ "  WHERE ide_bocam =  " + ide_bocam;
		TablaGenerica tg = utilitario.consultar(sql);
		return tg;
	}

	public String getSqlIngresos(String ide_geani, String ide_boubi) {
		return String.format("SELECT ide_ingeg, " + "   numero_documento_ingeg, "
				+ "   observacion_ingeg, fecha_ingeg, activo_ingeg " + "  FROM bodt_ingreso_egreso"
				+ "  WHERE ide_boubi = %s AND ide_geani = %s AND ide_inttr = 1 AND activo_ingeg = true ORDER BY 1 DESC",
				ide_boubi, ide_geani);
	}

	public String getSqlItemsDeIngreso(String ide_ingeg) {
		return String.format(
				"SELECT ide_ingeg, " + "   numero_documento_ingeg, "
						+ "   observacion_ingeg, fecha_ingeg, activo_ingeg " + "  FROM bodt_ingreso_egreso"
						+ "  WHERE ide_boubi = %s AND ide_geani = %s AND ide_inttr = 1 AND activo_ingeg = true ",
				ide_ingeg);
	}

	public TablaGenerica getBodegaInventario(String ide_geani, String ide_bocam, String ide_boubi) {
		TablaGenerica tg = utilitario.consultar("SELECT  " + "* " + " FROM bodt_bodega_inventario "
				+ " WHERE ide_geani = " + ide_geani + " AND ide_bocam= " + ide_bocam + " AND ide_boubi = " + ide_boubi);
		return tg;
	}

	public TablaGenerica getInventarioResumen(String ide_geani, String ide_bocam) {
		TablaGenerica tg = utilitario.consultar("SELECT  * FROM bodt_inventario_resumen " + " WHERE ide_geani = "
				+ ide_geani + " AND ide_bocam= " + ide_bocam);
		return tg;
	}

	public TablaGenerica getAlertasDeExistencias(String ide_bocam, String ide_geani, String ide_boubi) {
		TablaGenerica tg = utilitario.consultar(String.format("SELECT  "
				+ "    inventario.ide_bocam, inventario.ide_geani, inventario.ide_boubi, inventario.cantidad_saldo_boinv, "
				+ "     catalogo.ide_prcla, ide_boalr, usuario_notificacion.ide_gtemp, cantidad_minima_stock_boalr, detalle_gtcor, "
				+ "catalogo.descripcion_bocam, catalogo.cat_codigo_bocam,  detalle_boubi "
				+ "FROM bodt_bodega_inventario as inventario  "
				+ "LEFT JOIN bodt_catalogo_material AS catalogo ON catalogo.ide_bocam = inventario.ide_bocam "
				+ "LEFT JOIN bodt_bodega_ubicacion AS bodega ON inventario.ide_boubi = bodega.ide_boubi "
				+ "INNER JOIN bodt_alerta_regla AS regla ON regla.ide_prcla = catalogo.ide_prcla "
				+ "INNER JOIN bodt_alerta_usuario AS usuario_notificacion ON usuario_notificacion.activo_boalu = TRUE "
				+ "INNER JOIN gth_correo AS correo ON correo.ide_gtemp = usuario_notificacion.ide_gtemp "
				+ "WHERE inventario.ide_bocam = %s AND inventario.ide_geani = %s AND inventario.ide_boubi = %s "
				+ "    AND regla.activo_boalr = TRUE AND activo_gtcor = TRUE AND cantidad_saldo_boinv < regla.cantidad_minima_stock_boalr ",
				ide_bocam, ide_geani, ide_boubi));
		return tg;
	}

	public void enviarAlertas(String ide_bocam, String ide_geani, String ide_boubi) {
		TablaGenerica tg = getAlertasDeExistencias(ide_bocam, ide_geani, ide_boubi);
		System.out.println("ServicioBodega:enviarAlertas");
		System.out.println(tg.getTotalFilas());
		List<String> correos = new ArrayList<String>();

		for (int i = 0; i < tg.getTotalFilas(); i++) {
			correos.add(tg.getValor(i, "detalle_gtcor"));
		}

		Long cantidadActual = 0L;
		String cantidadTexto = tg.getValor("cantidad_saldo_boinv");
		String descripcion_bocam = tg.getValor("descripcion_bocam");
		String partida = tg.getValor("cat_codigo_bocam");
		String limite = tg.getValor("cantidad_minima_stock_boalr");
		String bodega = tg.getValor("detalle_boubi");
		try {

			if (cantidadTexto != null) {
				Double cantidad = Double.parseDouble(cantidadTexto);
				cantidadActual = cantidad.longValue();
			}

		} catch (NumberFormatException e) {
			System.out.println("Error al convertir la cantidad actual: " + tg.getValor("cantidad_saldo_boinv"));
			e.printStackTrace();
		}

		TablaGenerica tab_correo_envio = utilitario
				.consultar("SELECT ide_corr, smtp_corr, puerto_corr, usuario_corr, correo_corr, "
						+ "clave_corr from sis_correo where ide_corr=3");
		String smtp_correo = tab_correo_envio.getValor("smtp_corr");
		String puertoEnvio = tab_correo_envio.getValor("puerto_corr");
		String correo_envio = tab_correo_envio.getValor("correo_corr");
		String usuario_envio = tab_correo_envio.getValor("usuario_corr");
		String clave_correo = tab_correo_envio.getValor("clave_corr");

		EnvioMail envMail = new EnvioMail(smtp_correo, puertoEnvio, correo_envio, usuario_envio, clave_correo);

		for (int i = 0; i < correos.size(); i++) {
			String correo = correos.get(i);
			try {
				System.out.println("Enviando correo a: " + correo);
				envMail.setAsunto("Notificación de existencias en bodega");
				envMail.setCuerpoHtml(
						cuerpoDeCorreoAlerta(bodega, descripcion_bocam, partida, cantidadActual.toString(), limite));
				envMail.setPara(correo);
				pckEntidades.MensajeRetorno obj = pckUtilidades.consumoServiciosCore.enviarMail(envMail);
				System.out.println(obj.getDescripcion());
			} catch (Exception e) {
				System.out.println("Error enviando correo a: " + correo);
				System.out.println(e.getMessage());
			}
		}

	}

	public String cuerpoDeCorreoAlerta(String bodega, String descripcionItem, String partida, String cantidadActual,
			String limite) {
		return String.format(
				"Estimado/a, <br> " + "El stock en <strong> %s </strong> de <strong> %s %s </strong> "
						+ "es inferior al límite establecido %s. <br>" + "<strong>Cantidad actual: %s </strong>",
				bodega, partida, descripcionItem, limite, cantidadActual);
	}

	public String getSqlInventarioActualPorBodegaConSaldo(String ide_geani, String ide_boubi) {
		return String.format(
				"SELECT " + "inventario.ide_boinv, inventario.cantidad_saldo_boinv , catalogo.cat_codigo_bocam, "
						+ "catalogo.descripcion_bocam, inventario.ide_boubi,  "
						+ "resumen.costo_medio_unidad_inres, inventario.ide_bocam " + "FROM  "
						+ "bodt_bodega_inventario AS inventario " + "LEFT JOIN bodt_inventario_resumen AS resumen "
						+ "ON inventario.ide_geani = resumen.ide_geani AND  "
						+ "   inventario.ide_bocam = resumen.ide_bocam "
						+ "LEFT JOIN bodt_catalogo_material AS catalogo "
						+ "ON inventario.ide_bocam = catalogo.ide_bocam " + "WHERE  " + "inventario.ide_geani = %s "
						+ "AND inventario.ide_boubi = %s  " + "AND inventario.cantidad_saldo_boinv >= 0;",
				ide_geani, ide_boubi);
	}
	
	public String getSqlInventarioActualPorBodegaConSaldoQuimico(String ide_geani, String ide_boubi) {
		
		// + "inventario.ide_geani = %s "
		return String.format(
				"SELECT " + "inventario.ide_boinv, inventario.cantidad_saldo_boinv , catalogo.cat_codigo_bocam, "
						+ "catalogo.descripcion_bocam, inventario.ide_boubi,  "
						+ "resumen.costo_medio_unidad_inres, inventario.ide_bocam " + "FROM  "
						+ "bodt_bodega_inventario AS inventario " + "LEFT JOIN bodt_inventario_resumen AS resumen "
						+ "ON inventario.ide_geani = resumen.ide_geani AND  "
						+ "   inventario.ide_bocam = resumen.ide_bocam "
						+ "LEFT JOIN bodt_catalogo_material AS catalogo "
						+ "ON inventario.ide_bocam = catalogo.ide_bocam " + "WHERE  " 
						+ " inventario.ide_boubi = %s  " + "AND inventario.cantidad_saldo_boinv >= 0 and  catalogo.tipo ='QUIMICO';",
			 ide_boubi);
		
		
	}
	
	
	
	
	

	public String getSqlInventarioActual(String ide_geani, String ide_boubi) {
		String sql=String.format("SELECT  "
				+ "inventario.ide_boinv, inventario.ide_bocam, catalogo.cat_codigo_bocam, catalogo.descripcion_bocam, inventario.ide_boubi,   "
				+ "inventario.cantidad_inicial_boinv, inventario.costo_inicial_boinv,   "
				+ "inventario.cantidad_ingreso_boinv, inventario.costo_ingreso_boinv,  "
				+ "inventario.cantidad_ingreso_t_boinv, inventario.costo_ingreso_t_boinv,  "
				+ "inventario.cantidad_egreso_boinv, inventario.costo_egreso_boinv,  "
				+ "inventario.cantidad_egreso_t_boinv, inventario.costo_egreso_t_boinv,  "
				+ "inventario.cantidad_saldo_boinv, inventario.costo_saldo_boinv,  " + "resumen.pmp_existencia_inres "
				+ "FROM   " + "bodt_bodega_inventario AS inventario  "
				+ "LEFT JOIN bodt_inventario_resumen AS resumen  "
				+ "ON inventario.ide_geani = resumen.ide_geani AND   " + "   inventario.ide_bocam = resumen.ide_bocam  "
				+ "LEFT JOIN bodt_catalogo_material AS catalogo  " + "ON inventario.ide_bocam = catalogo.ide_bocam  "
				+ "WHERE   " + "inventario.ide_geani = %s " + "AND inventario.ide_boubi = %s ", ide_geani, ide_boubi);
		System.out.println("getSqlInventarioActual "+sql);
		return sql;
	}

	public String getResumenMensual(String ide_geani, String mes) {
		return String.format("WITH param AS ( " + "    SELECT " + "    %s as ide_geani, "
				+ "    1 as ide_inttr_ingreso, " + "    2 as ide_inttr_egreso, " + "    3 as ide_inttr_t_ingreso, "
				+ "    4 as ide_inttr_t_egreso, " + "    %s as mes " + "), " + " " + "inicial AS ( " + "SELECT "
				+ " ide_geani, " + " ide_bocam, " + " SUM(cantidad_inicial_boinv) AS inicial_cantidad, "
				+ " COALESCE( SUM(cantidad_inicial_boinv*valor_existencia_inicial_boinv) / NULLIF(SUM(cantidad_inicial_boinv),0),0) AS inicial_pmp, "
				+ " COALESCE( SUM(cantidad_inicial_boinv*valor_existencia_inicial_inc_iva_boinv) / NULLIF(SUM(cantidad_inicial_boinv),0),0) AS inicial_pmp_inc_iva, "
				+ " COALESCE( SUM(cantidad_inicial_boinv)* SUM(cantidad_inicial_boinv*valor_existencia_inicial_boinv) / NULLIF(SUM(cantidad_inicial_boinv),0),0) as inicial_subtotal, "
				+ " COALESCE( SUM(cantidad_inicial_boinv)* SUM(cantidad_inicial_boinv*valor_existencia_inicial_boinv) / NULLIF(SUM(cantidad_inicial_boinv),0),0) as inicial_total "
				+ "FROM bodt_bodega_inventario " + "WHERE " + "    ide_geani = (SELECT ide_geani FROM param) "
				+ "GROUP BY 1, 2 " + "ORDER BY ide_bocam " + "), " + "ingresos_previo AS ( " + " " + "SELECT "
				+ "transaccion.ide_geani, detalle.ide_bocam, " + "detalle.ide_inttr, "
				+ "SUM(detalle.cantidad_inegd) AS ingresos_cantidad, "
				+ "SUM(detalle.subtotal_inegd) AS ingresos_subtotal, " + "SUM(detalle.total_inegd) as ingresos_total "
				+ "FROM " + "bodt_ingreso_egreso_det as detalle "
				+ "JOIN bodt_ingreso_egreso as transaccion ON detalle.ide_ingeg = transaccion.ide_ingeg " + "WHERE "
				+ "transaccion.ide_geani = (SELECT ide_geani FROM param) "
				+ "AND detalle.ide_bocam = (SELECT ide_bocam FROM param) "
				+ "AND detalle.ide_inttr = (SELECT ide_inttr_ingreso FROM param) "
				+ "AND transaccion.activo_ingeg = true "
				+ "AND EXTRACT(MONTH FROM fecha_ingeg) < (SELECT mes FROM param) " + "GROUP BY 1, 2, 3 " + "), "
				+ "egresos_previo AS ( " + " " + "SELECT " + "transaccion.ide_geani, detalle.ide_bocam, "
				+ "detalle.ide_inttr, " + "SUM(detalle.cantidad_inegd) AS egresos_cantidad, "
				+ "SUM(detalle.subtotal_inegd) AS egresos_subtotal, " + "SUM(detalle.total_inegd) as egresos_total "
				+ "FROM " + "bodt_ingreso_egreso_det as detalle "
				+ "JOIN bodt_ingreso_egreso as transaccion ON detalle.ide_ingeg = transaccion.ide_ingeg " + "WHERE "
				+ "transaccion.ide_geani = (SELECT ide_geani FROM param) "
				+ "AND detalle.ide_bocam = (SELECT ide_bocam FROM param) "
				+ "AND detalle.ide_inttr = (SELECT ide_inttr_egreso FROM param) "
				+ "AND transaccion.activo_ingeg = true "
				+ "AND EXTRACT(MONTH FROM fecha_ingeg) < (SELECT mes FROM param) " + "GROUP BY 1, 2, 3 " + "), "
				+ "resumen_previo AS ( " + " " + "SELECT "
				+ "COALESCE(inicial.ide_geani, COALESCE(ingresos_previo.ide_geani, COALESCE(egresos_previo.ide_geani, -1))) as ide_geani, "
				+ "COALESCE(inicial.ide_bocam, COALESCE(ingresos_previo.ide_bocam, COALESCE(egresos_previo.ide_bocam, -1))) as ide_bocam, "
				+ "COALESCE(inicial.inicial_cantidad,0) + COALESCE(ingresos_previo.ingresos_cantidad,0) - COALESCE(egresos_previo.egresos_cantidad,0) as previo_cantidad, "
				+ "COALESCE(inicial.inicial_subtotal,0) + COALESCE(ingresos_previo.ingresos_subtotal,0) - COALESCE(egresos_previo.egresos_subtotal,0) as previo_subtotal, "
				+ "COALESCE(inicial.inicial_total,0) + COALESCE(ingresos_previo.ingresos_total,0) - COALESCE(egresos_previo.egresos_total,0) as previo_total "
				+ " " + "FROM inicial "
				+ "FULL OUTER JOIN ingresos_previo ON inicial.ide_geani=ingresos_previo.ide_geani AND inicial.ide_bocam=ingresos_previo.ide_bocam "
				+ "FULL OUTER JOIN egresos_previo ON inicial.ide_geani=egresos_previo.ide_geani AND inicial.ide_bocam=egresos_previo.ide_bocam "
				+ "), " + "ingresos AS ( " + " " + "SELECT " + "transaccion.ide_geani, detalle.ide_bocam, "
				+ "detalle.ide_inttr, " + "SUM(detalle.cantidad_inegd) AS ingresos_cantidad, "
				+ "SUM(detalle.subtotal_inegd) AS ingresos_subtotal, " + "SUM(detalle.total_inegd) as ingresos_total "
				+ "FROM " + "bodt_ingreso_egreso_det as detalle "
				+ "JOIN bodt_ingreso_egreso as transaccion ON detalle.ide_ingeg = transaccion.ide_ingeg " + "WHERE "
				+ "transaccion.ide_geani = (SELECT ide_geani FROM param) "
				+ "AND detalle.ide_bocam = (SELECT ide_bocam FROM param) "
				+ "AND detalle.ide_inttr = (SELECT ide_inttr_ingreso FROM param) "
				+ "AND transaccion.activo_ingeg = true "
				+ "AND EXTRACT(MONTH FROM fecha_ingeg) = (SELECT mes FROM param) " + "GROUP BY 1, 2, 3 " + "), "
				+ "ingresos_t AS ( " + " " + "SELECT " + "transaccion.ide_geani, detalle.ide_bocam, "
				+ "detalle.ide_inttr, " + "SUM(detalle.cantidad_inegd) AS ingresos_t_cantidad, "
				+ "SUM(detalle.subtotal_inegd) AS ingresos_t_subtotal, "
				+ "SUM(detalle.total_inegd) as ingresos_t_total " + "FROM " + "bodt_ingreso_egreso_det as detalle "
				+ "JOIN bodt_ingreso_egreso as transaccion ON detalle.ide_ingeg = transaccion.ide_ingeg " + "WHERE "
				+ "transaccion.ide_geani = (SELECT ide_geani FROM param) "
				+ "AND detalle.ide_bocam = (SELECT ide_bocam FROM param) "
				+ "AND detalle.ide_inttr = (SELECT ide_inttr_t_ingreso FROM param) "
				+ "AND transaccion.activo_ingeg = true "
				+ "AND EXTRACT(MONTH FROM fecha_ingeg) = (SELECT mes FROM param) " + "GROUP BY 1, 2, 3 " + "), "
				+ "egresos AS ( " + " " + "SELECT " + "transaccion.ide_geani, detalle.ide_bocam,  "
				+ "detalle.ide_inttr, " + "SUM(detalle.cantidad_inegd) AS egresos_cantidad, "
				+ "SUM(detalle.subtotal_inegd) AS egresos_subtotal, " + "SUM(detalle.total_inegd) as egresos_total "
				+ " " + "FROM " + "bodt_ingreso_egreso_det as detalle "
				+ "JOIN bodt_ingreso_egreso as transaccion ON detalle.ide_ingeg = transaccion.ide_ingeg " + "WHERE "
				+ "transaccion.ide_geani = (SELECT ide_geani FROM param) "
				+ "AND detalle.ide_bocam = (SELECT ide_bocam FROM param) "
				+ "AND detalle.ide_inttr = (SELECT ide_inttr_egreso FROM param) "
				+ "AND transaccion.activo_ingeg = true "
				+ "AND EXTRACT(MONTH FROM fecha_ingeg) = (SELECT mes FROM param) " + "GROUP BY 1, 2, 3 " + "), "
				+ "egresos_t AS ( " + " " + "SELECT " + "transaccion.ide_geani, detalle.ide_bocam,  "
				+ "detalle.ide_inttr, " + "SUM(detalle.cantidad_inegd) AS egresos_t_cantidad, "
				+ "SUM(detalle.subtotal_inegd) AS egresos_t_subtotal, " + "SUM(detalle.total_inegd) as egresos_total "
				+ "FROM " + "bodt_ingreso_egreso_det as detalle "
				+ "JOIN bodt_ingreso_egreso as transaccion ON detalle.ide_ingeg = transaccion.ide_ingeg " + "WHERE "
				+ "transaccion.ide_geani = (SELECT ide_geani FROM param) "
				+ "AND detalle.ide_bocam = (SELECT ide_bocam FROM param) "
				+ "AND detalle.ide_inttr = (SELECT ide_inttr_t_egreso FROM param) "
				+ "AND transaccion.activo_ingeg = true "
				+ "AND EXTRACT(MONTH FROM fecha_ingeg) = (SELECT mes FROM param) " + "GROUP BY 1, 2, 3 " + "), "
				+ "saldo AS ( " + "SELECT " + "COALESCE(resumen_previo.ide_geani, -1) as ide_geani, "
				+ "COALESCE(resumen_previo.ide_bocam, -1) as ide_bocam, "
				+ "COALESCE(resumen_previo.previo_cantidad,0) + COALESCE(ingresos.ingresos_cantidad,0) - COALESCE(egresos.egresos_cantidad,0) as saldo_cantidad, "
				+ "COALESCE(resumen_previo.previo_subtotal,0) + COALESCE(ingresos.ingresos_subtotal,0) - COALESCE(egresos.egresos_subtotal,0) as saldo_subtotal, "
				+ "COALESCE(resumen_previo.previo_total,0) + COALESCE(ingresos.ingresos_total,0) - COALESCE(egresos.egresos_total,0) as saldo_total "
				+ " " + "FROM resumen_previo "
				+ "FULL OUTER JOIN ingresos ON resumen_previo.ide_geani=ingresos.ide_geani AND resumen_previo.ide_bocam=ingresos.ide_bocam "
				+ "FULL OUTER JOIN egresos ON resumen_previo.ide_geani=egresos.ide_geani AND resumen_previo.ide_bocam=egresos.ide_bocam "
				+ " " + "), " + "resumen AS( " + "SELECT "
				+ "COALESCE(resumen_previo.ide_geani, COALESCE(ingresos.ide_geani, COALESCE(egresos.ide_geani, COALESCE(ingresos_t.ide_geani, COALESCE(egresos_t.ide_geani, COALESCE(saldo.ide_geani, -1)))))) as ide_geani, "
				+ "COALESCE(resumen_previo.ide_bocam, COALESCE(ingresos.ide_bocam, COALESCE(egresos.ide_bocam, COALESCE(ingresos_t.ide_bocam, COALESCE(egresos_t.ide_bocam, COALESCE(saldo.ide_bocam, -1)))))) as ide_bocam, "
				+ "resumen_previo.previo_cantidad, resumen_previo.previo_subtotal, resumen_previo.previo_total, "
				+ "ingresos.ingresos_cantidad, ingresos.ingresos_subtotal, ingresos.ingresos_total, "
				+ "egresos.egresos_cantidad, egresos.egresos_subtotal, egresos.egresos_total, "
				+ "saldo.saldo_cantidad, saldo.saldo_subtotal, saldo.saldo_total " + "FROM resumen_previo "
				+ "FULL OUTER JOIN ingresos ON resumen_previo.ide_geani=ingresos.ide_geani AND resumen_previo.ide_bocam=ingresos.ide_bocam "
				+ "FULL OUTER JOIN egresos ON resumen_previo.ide_geani=egresos.ide_geani AND resumen_previo.ide_bocam=egresos.ide_bocam "
				+ "FULL OUTER JOIN ingresos_t ON resumen_previo.ide_geani=ingresos_t.ide_geani AND resumen_previo.ide_bocam=ingresos_t.ide_bocam "
				+ "FULL OUTER JOIN egresos_t ON resumen_previo.ide_geani=egresos_t.ide_geani AND resumen_previo.ide_bocam=egresos_t.ide_bocam "
				+ "FULL OUTER JOIN saldo ON resumen_previo.ide_geani=saldo.ide_geani AND resumen_previo.ide_bocam=saldo.ide_bocam "
				+ ") " + " " + "SELECT " + "resumen.ide_geani, " + "resumen.ide_bocam, "
				+ "ROUND(COALESCE(resumen.previo_cantidad, 0), 2) AS inicial_cantidad, "
				+ "ROUND(COALESCE(resumen.previo_subtotal, 0), 2) AS inicial_subtotal, "
				+ "ROUND(COALESCE(resumen.previo_total, 0), 2) AS inicial_total, "
				+ "ROUND(COALESCE(resumen.ingresos_cantidad, 0), 2) AS ingresos_cantidad, "
				+ "ROUND(COALESCE(resumen.ingresos_subtotal, 0), 2) AS ingresos_subtotal, "
				+ "ROUND(COALESCE(resumen.ingresos_total, 0), 2) AS ingresos_total, "
				+ "ROUND(COALESCE(resumen.egresos_cantidad, 0), 2) AS egresos_cantidad, "
				+ "ROUND(COALESCE(resumen.egresos_subtotal, 0), 2) AS egresos_subtotal, "
				+ "ROUND(COALESCE(resumen.egresos_total, 0), 2) AS egresos_total, "
				+ "ROUND(COALESCE(resumen.saldo_cantidad, 0), 2) AS saldo_cantidad, "
				+ "ROUND(COALESCE(resumen.saldo_subtotal, 0), 2) AS saldo_subtotal, "
				+ "ROUND(COALESCE(resumen.saldo_total, 0), 2) AS saldo_total, "
				+ "bodt_catalogo_material.descripcion_bocam, " + "padre.ide_bocam as padre_ide_bocam, "
				+ "padre.descripcion_bocam as padre_descripcion_bocam, " + "padre.cat_codigo_bocam " + "FROM resumen "
				+ "LEFT JOIN bodt_catalogo_material ON bodt_catalogo_material.ide_bocam = resumen.ide_bocam "
				+ "LEFT JOIN bodt_catalogo_material AS padre ON padre.ide_bocam = bodt_catalogo_material.con_ide_bocam "
				+ "ORDER BY padre_ide_bocam, bodt_catalogo_material.descripcion_bocam ", ide_geani, mes);
	}

	public String getEstimacion() {
		return "WITH resumen AS( " + "SELECT  " + "	ide_bocam,  egreso.ide_inttr,   "
				+ "       EXTRACT(YEAR FROM fecha_ingeg) as year, EXTRACT(MONTH FROM fecha_ingeg) as month, SUM(cantidad_inegd) as y "
				+ "  FROM bodt_ingreso_egreso_det as detalle "
				+ "  LEFT JOIN bodt_ingreso_egreso as egreso ON egreso.ide_ingeg = detalle.ide_ingeg  "
				+ "  WHERE egreso.ide_inttr = 2 " + "  GROUP BY 1, 2, 3, 4 " + "  ORDER BY year, month " + "), "
				+ "resumen_index AS ( " + "SELECT ROW_NUMBER () OVER (PARTITION BY ide_bocam) as x,  * FROM resumen "
				+ "), " + "calculo_inicial AS ( " + "SELECT *, x*y as xy, x*x as xx FROM resumen_index " + "), "
				+ "calculo_variables AS ( " + "SELECT  " + "ide_bocam, COUNT(*) as n,  "
				+ "SUM(xy) as s_xy, SUM(x) as s_x, SUM(y) as s_y,  " + "SUM(xx) as s_xx, SUM(x) * SUM(x) as s_x_2 "
				+ "FROM calculo_inicial " + "GROUP BY ide_bocam " + "), " + "calculo_a AS ( "
				+ "SELECT ide_bocam, s_y, s_x, n, "
				+ "COALESCE((n*s_xy-(s_x*s_y))/(NULLIF(n*s_xx - s_x_2, 0)), 0) as a " + "FROM calculo_variables  "
				+ "), " + "calculo_b AS( " + "SELECT  " + "calculo_a.ide_bocam, descripcion_bocam, n,  a,   "
				+ "COALESCE((s_y-a*s_x)/NULLIF(n,0), 0) as b FROM calculo_a "
				+ "LEFT JOIN bodt_catalogo_material as catalogo ON catalogo.ide_bocam = calculo_a.ide_bocam " + "), "
				+ "calculo_prediccion AS ( " + "SELECT  " + "*, a*(n+1)+b as y_1, a*(n+12)+b as y_12 "
				+ "FROM calculo_b " + ") " + "SELECT * FROM calculo_prediccion ";
	}

	/**
	 * Obtener los empleados activospara egreso de existencias TODO: Validar cuando
	 * uno de los nombres o apellidos es null
	 * 
	 * @return
	 */
	public String getEmpleadosActivos() {
		return "WITH empleado_area AS( " + "SELECT  " + "accion.ide_gtemp, area.detalle_geare "
				+ "FROM gen_empleados_departamento_par AS accion "
				+ "LEFT JOIN gen_area AS area ON area.ide_geare = accion.ide_geare "
				+ "WHERE accion.activo_geedp = true " + "ORDER BY 1 " + ") " + " " + "SELECT  " + "empleado.ide_gtemp, "
				+ "COALESCE(primer_nombre_gtemp, '') || ' '  || COALESCE(segundo_nombre_gtemp, '') || ' '  || COALESCE(apellido_paterno_gtemp, '') || ' '  || COALESCE(apellido_materno_gtemp, '') AS nombre, "
				+ "detalle_geare " + "FROM gth_empleado AS empleado "
				+ "LEFT JOIN empleado_area  ON empleado.ide_gtemp=empleado_area.ide_gtemp "
				+ "WHERE activo_gtemp = true " + "ORDER BY 1 ";
	}

	public String getMes(String ide_gemes) {
		String tab_anio = "select ide_gemes,detalle_gemes from gen_mes where ide_gemes in (" + ide_gemes
				+ ") order by ide_gemes";
		return tab_anio;

	}

	/**
	 * Obtener las facturas y su proveedor. Utilizado para asociar a un ingreso o
	 * egreso
	 * 
	 * @return
	 */
	public String getFacturas() {
		return "SELECT "
				+ "factura.ide_adfac, factura.num_factura_adfac, factura.fecha_factura_adfac, factura.detalle_adfac, "
				+ "proveedor.nombre_tepro, proveedor.ruc_tepro " + "FROM adq_factura AS factura "
				+ "JOIN tes_proveedor AS proveedor ON proveedor.ide_tepro=factura.ide_tepro ORDER BY factura.ide_adfac  DESC";
		/* return "SELECT ide_adfac, num_factura_adfac FROM adq_factura;"; */
	}

	public String getCatalogoExistencias() {
		/*
		 * return "WITH RECURSIVE catalogo AS ("+
		 * "        SELECT ide_bocam, con_ide_bocam, cat_codigo_bocam, descripcion_bocam, activo_bocam"
		 * + "        FROM bodt_catalogo_material"+ "        WHERE ide_bocam = 2"+
		 * "    UNION ALL"+
		 * "        SELECT e.ide_bocam, e.con_ide_bocam, e.cat_codigo_bocam, e.descripcion_bocam, e.activo_bocam"
		 * + "        FROM bodt_catalogo_material e"+
		 * "        JOIN catalogo ON e.con_ide_bocam = catalogo.ide_bocam"+ ")"+
		 * "SELECT ide_bocam, cat_codigo_bocam, descripcion_bocam FROM catalogo WHERE activo_bocam=true;"
		 * ;
		 */
		return "WITH RECURSIVE catalogo AS ( "
				+ "        SELECT ide_bocam, con_ide_bocam, cat_codigo_bocam, descripcion_bocam, activo_bocam "
				+ "        FROM bodt_catalogo_material " + "        WHERE ide_bocam = 2 " + "    UNION ALL "
				+ "        SELECT e.ide_bocam, e.con_ide_bocam, e.cat_codigo_bocam, e.descripcion_bocam, e.activo_bocam "
				+ "        FROM bodt_catalogo_material e "
				+ "        JOIN catalogo ON e.con_ide_bocam = catalogo.ide_bocam " + "), " + "conteo_hijos AS ( "
				+ "SELECT con_ide_bocam, COUNT(*) as cantidad_hijos " + "FROM catalogo   " + "GROUP BY 1 " + ") " + " "
				+ "SELECT catalogo.ide_bocam, catalogo.cat_codigo_bocam || ' '  || catalogo.descripcion_bocam, conteo_hijos.cantidad_hijos "
				+ "FROM catalogo  " + "LEFT JOIN conteo_hijos ON catalogo.ide_bocam = conteo_hijos.con_ide_bocam "
				+ "WHERE activo_bocam = true AND conteo_hijos.cantidad_hijos IS NULL ";
	}

	public String getCalalogoExistencias1() {
		return "SELECT catalogo.ide_bocam,  " + "catalogo.cat_codigo_bocam || ' ' || catalogo.descripcion_bocam "
				+ "FROM bodt_catalogo_material as catalogo "
				+ "LEFT JOIN bodt_catalogo_material as padre ON catalogo.con_ide_bocam = padre.ide_bocam "
				+ "LEFT JOIN bodt_catalogo_material as abuelo ON padre.con_ide_bocam = abuelo.ide_bocam "
				+ "WHERE abuelo.ide_bocam = 2 ";
	}

	public String getCalalogoExistenciasItems() {
		return "SELECT catalogo.ide_bocam,  " + " catalogo.descripcion_bocam "
				+ "FROM bodt_catalogo_material as catalogo "
				+ "LEFT JOIN bodt_catalogo_material as padre ON catalogo.con_ide_bocam = padre.ide_bocam "
				+ "LEFT JOIN bodt_catalogo_material as abuelo ON padre.con_ide_bocam = abuelo.ide_bocam "
				+ "WHERE abuelo.ide_bocam = 2 ";
	}

	public String getCatalogoExistenciasItemsParaBusqueda() {
		return "WITH cat AS (" + "SELECT catalogo.ide_bocam,  " + " catalogo.cat_codigo_bocam, "
				+ " catalogo.descripcion_bocam " + "FROM bodt_catalogo_material as catalogo "
				+ "LEFT JOIN bodt_catalogo_material as padre ON catalogo.con_ide_bocam = padre.ide_bocam "
				+ "LEFT JOIN bodt_catalogo_material as abuelo ON padre.con_ide_bocam = abuelo.ide_bocam "
				+ "WHERE abuelo.ide_bocam = 2 " + ") "
				+ "SELECT ide_bocam, cat_codigo_bocam,  descripcion_bocam FROM cat ";
	}

	public String getFacturaDetalle(String factura_ide_adfac) {
		return "SELECT  "
				+ "ide_addef, ide_adfac, descripcion_addef, cantidad_addef, valor_unitario_addef, aplica_iva_adfac, valor_total_addef "
				+ "FROM adq_detalle_factura  " + "WHERE ide_adfac=" + factura_ide_adfac;
	}

	public TablaGenerica getTablaGenericaCatalogoDuplicado(String descripcion_bocam, String ide_prcla) {
		TablaGenerica tg = utilitario.consultar(String.format("SELECT catalogo.ide_bocam,  "
				+ " catalogo.descripcion_bocam " + "FROM bodt_catalogo_material as catalogo "
				+ "WHERE descripcion_bocam ilike '%s' AND ide_prcla = %s", descripcion_bocam, ide_prcla));
		return tg;

	}

	public TablaGenerica getTablaGenericaCatalogo(String ide_bocam, String ide_boubi, String ide_geani) {
		TablaGenerica tg = utilitario.consultar(String.format("SELECT  DISTINCT "
				+ "bodt_catalogo_material.ide_bocam, cat_codigo_bocam, descripcion_bocam, ide_bounm, ide_bounm_presentacion,  "
				+ "vida_util_bocam, " + "peligro_salud_bocam, peligro_inflamabilidad_bocam, peligro_reactividad_bocam, "
				+ " manejo_especial_bocam, costo_medio_unidad_inres, costo_medio_unidad_inc_iva_inres, cantidad_saldo_boinv, pmp_existencia_inres  "
				+ "FROM bodt_catalogo_material "
				+ "LEFT JOIN bodt_inventario_resumen ON bodt_catalogo_material.ide_bocam = bodt_inventario_resumen.ide_bocam  "
				+ "LEFT JOIN bodt_bodega_inventario ON bodt_bodega_inventario.ide_bocam = bodt_catalogo_material.ide_bocam "
				+ "WHERE bodt_catalogo_material.ide_bocam IN (%s) AND ide_boubi = %s AND bodt_inventario_resumen.ide_geani = %s AND bodt_bodega_inventario.ide_geani = %s ",
				ide_bocam, ide_boubi, ide_geani, ide_geani));
		// System.out.println(tg.getSql());
		return tg;

	}

	public TablaGenerica getTablaGenericaImportarIngreso(String ide_ingeg, String ide_boubi, String ide_geani) {
		TablaGenerica tg = utilitario.consultar(String.format(
				"SELECT  detalle.ide_bocam,  detalle.cantidad_inegd,  detalle.ide_addef,  ingreso.ide_ingeg,  ingreso.ide_adfac,  catalogo.cat_codigo_bocam, catalogo.descripcion_bocam,  catalogo.ide_bounm, catalogo.ide_bounm_presentacion, catalogo.vida_util_bocam, catalogo.peligro_salud_bocam, catalogo.peligro_inflamabilidad_bocam, catalogo.peligro_reactividad_bocam, catalogo.manejo_especial_bocam, resumen.costo_medio_unidad_inres, resumen.costo_medio_unidad_inc_iva_inres, resumen.pmp_existencia_inres, inventario.cantidad_saldo_boinv  "
						+ "FROM bodt_ingreso_egreso_det AS detalle  "
						+ "LEFT JOIN bodt_ingreso_egreso AS ingreso ON ingreso.ide_ingeg = detalle.ide_ingeg  "
						+ "LEFT JOIN bodt_catalogo_material as catalogo ON catalogo.ide_bocam = detalle.ide_bocam  "
						+ "LEFT JOIN bodt_bodega_inventario as inventario ON inventario.ide_bocam = detalle.ide_bocam AND inventario.ide_boubi = ingreso.ide_boubi AND inventario.ide_geani = ingreso.ide_geani "
						+ "LEFT JOIN bodt_inventario_resumen as resumen ON resumen.ide_bocam = detalle.ide_bocam AND resumen.ide_geani = ingreso.ide_geani "
						+ "WHERE ingreso.ide_ingeg = %s AND  ingreso.ide_boubi = %s AND  ingreso.ide_geani = %s AND ingreso.activo_ingeg = true   ",
				ide_ingeg, ide_boubi, ide_geani));
		// System.out.println(tg.getSql());
		return tg;

	}

	public TablaGenerica getTablaGenericaCabeceraIngreso(String ide_ingeg) {
		TablaGenerica tg = utilitario.consultar(String.format(
				"SELECT ide_ingeg, ide_inttr, ide_adfac, ide_adfac_antigua, ide_boubi, "
						+ "       ide_geani, ide_gtemp, ide_gtemp_jefe_solicitante, ide_gtemp_solicitante, "
						+ "       ide_prcer, fecha_ingeg, observacion_ingeg, activo_ingeg, subtotal_ingeg, "
						+ "       valor_iva_ingeg, total_ingeg, usuario_ingre, fecha_ingre, hora_ingre, "
						+ "       usuario_actua, fecha_actua, hora_actua, numero_documento_ingeg, "
						+ "       ide_boubi_transferencia, ide_ingeg_ref, soporte_descripcion, "
						+ "       numero_proceso_ingeg " + "  FROM bodt_ingreso_egreso " + "  WHERE ide_ingeg = %s",
				ide_ingeg));
		// System.out.println(tg.getSql());
		return tg;

	}

	public TablaGenerica getTablaGenericaPreClasificacionPorId(String ide_prcla) {
		TablaGenerica tg = utilitario.consultar(
				"SELECT ide_prcla,codigo_clasificador_prcla, descripcion_clasificador_prcla FROM pre_clasificador "
						+ "WHERE activo_prcla = true AND ide_prcla = " + ide_prcla);
		return tg;
	}

	public TablaGenerica getTablaGenericaNombreEmpleadoPorIdUsuario(String ide_usua) {
		TablaGenerica tg = utilitario.consultar(
				"SELECT sis_usuario.ide_gtemp, apellido_paterno_gtemp || ' ' || apellido_materno_gtemp || ' ' || primer_nombre_gtemp || ' ' || segundo_nombre_gtemp  as nombre "
						+ "  FROM gth_empleado "
						+ "  LEFT JOIN sis_usuario ON sis_usuario.ide_gtemp = gth_empleado.ide_gtemp "
						+ "  WHERE sis_usuario.ide_usua = " + ide_usua);
		return tg;
	}

	public TablaGenerica getTablaGenericaFacturaDetalle(String ide_addef) {
		TablaGenerica tg = utilitario.consultar("SELECT  "
				+ "ide_addef, ide_adfac, descripcion_addef, cantidad_addef, valor_unitario_addef, aplica_iva_adfac, valor_total_addef "
				+ "FROM adq_detalle_factura  " + "WHERE ide_addef IN (" + ide_addef + ")");
		return tg;

	}

	public TablaGenerica getTablaGenericaCatalogoDeFacturaDetalle(String ide_addef) {
		TablaGenerica tg = utilitario.consultar(" SELECT catalogo.ide_bocam,  "
				+ " catalogo.cat_codigo_bocam || ' ' || catalogo.descripcion_bocam as texto, "
				+ " catalogo.ide_bounm, catalogo.ide_bounm_presentacion, catalogo.peligro_salud_bocam, "
				+ " catalogo.peligro_inflamabilidad_bocam, catalogo.peligro_reactividad_bocam, catalogo.manejo_especial_bocam "
				+ " FROM bodt_catalogo_material as catalogo "
				+ " LEFT JOIN bodt_catalogo_material as padre ON catalogo.con_ide_bocam = padre.ide_bocam "
				+ " LEFT JOIN bodt_catalogo_material as abuelo ON padre.con_ide_bocam = abuelo.ide_bocam "
				+ " LEFT JOIN bodt_catalogo_material as bisabuelo ON abuelo.con_ide_bocam = bisabuelo.ide_bocam "
				+ " WHERE (abuelo.ide_bocam = 2 OR bisabuelo.ide_bocam = 2) AND catalogo.descripcion_bocam ILIKE (SELECT descripcion_addef FROM adq_detalle_factura WHERE ide_addef = "
				+ ide_addef + " ) " + " ORDER BY catalogo.ide_bocam DESC "

		);
		return tg;

	}

	public TablaGenerica getTablaGenericaCatalogoDeFacturaDetalleYCertificacion(String ide_addef, String ide_prcer) {
		TablaGenerica tg = utilitario.consultar(String.format("WITH detalle_certificacion AS ( " + "SELECT  "
				+ "    pre_certificacion.ide_prcer, pre_certificacion.valor_certificacion_prcer,  "
				+ "    pre_clasificador.ide_prcla, pre_clasificador.codigo_clasificador_prcla ,  "
				+ "    SUM(pre_poa_certificacion.valor_certificado_prpoc) as valor_certificado  "
				+ "FROM pre_certificacion  "
				+ "LEFT JOIN pre_poa_certificacion ON pre_certificacion.ide_prcer = pre_poa_certificacion.ide_prcer  "
				+ "LEFT JOIN pre_poa ON pre_poa.ide_prpoa=pre_poa_certificacion.ide_prpoa  "
				+ "LEFT JOIN pre_clasificador ON pre_poa.ide_prcla=pre_clasificador.ide_prcla  "
				+ "WHERE pre_certificacion.ide_prcer =  %s " + "GROUP BY 1,2,3,4  " + ") "
				+ "SELECT catalogo.ide_bocam,   "
				+ "catalogo.cat_codigo_bocam || ' ' || catalogo.descripcion_bocam as texto,  "
				+ "catalogo.ide_bounm, catalogo.ide_bounm_presentacion, catalogo.peligro_salud_bocam,  "
				+ "catalogo.peligro_inflamabilidad_bocam, catalogo.peligro_reactividad_bocam, catalogo.manejo_especial_bocam  "
				+ "FROM bodt_catalogo_material as catalogo  "
				+ "LEFT JOIN bodt_catalogo_material as padre ON catalogo.con_ide_bocam = padre.ide_bocam  "
				+ "LEFT JOIN bodt_catalogo_material as abuelo ON padre.con_ide_bocam = abuelo.ide_bocam  "
				+ "LEFT JOIN bodt_catalogo_material as bisabuelo ON abuelo.con_ide_bocam = bisabuelo.ide_bocam  "
				+ "WHERE (abuelo.ide_bocam = 2 OR bisabuelo.ide_bocam = 2) AND catalogo.descripcion_bocam ILIKE (SELECT descripcion_addef FROM adq_detalle_factura WHERE ide_addef = %s )  "
				+ "AND catalogo.ide_prcla IN (SELECT ide_prcla FROM detalle_certificacion) "
				+ "ORDER BY catalogo.ide_bocam DESC  ", ide_prcer, ide_addef));
		return tg;

	}

	public TablaGenerica getTablaInventario(String codigo) {
		TablaGenerica tab_inventario = utilitario.consultar("select ide_bomat, codigo_bomat,detalle_bomat "
				+ " from bodt_material " + " WHERE ide_bomat in (" + codigo + ") " + " ORDER BY detalle_bomat");
		return tab_inventario;

	}

	public String getMaterialBodega(String ide_bomat) {

		String tab_inventario = "select ide_bomat, codigo_bomat,detalle_bomat " + " from bodt_material "
				+ " WHERE ide_bomat in (" + ide_bomat + ") " + " ORDER BY detalle_bomat";
		// System.out.println("llega como parametro "+ide);
		return tab_inventario;

	}

	// TODO TERMINAR LA CONSULTA
	public TablaGenerica getUbicacionBodega(String ide_boubi) {
		String sql = "SELECT ide_boubi, detalle_boubi, direccion_boubi, activo_boubi, con_ide_boubi, ide_gtemp, ide_afubi FROM bodt_bodega_ubicacion WHERE ide_boubi="
				+ ide_boubi + " ;";
		TablaGenerica tab_area = utilitario.consultar(sql);
		return tab_area;
	}

	public String getPeligrosidad(String tipo) {
		String query = "";
		query += "SELECT ide_bnpeli, descripcion_bnpeli  FROM bodt_nivel_peligrosidad  WHERE tipo_bnpeli IN (" + tipo
				+ ")";
		return query;
	}

	public String getCatalagoBodega(String activo) {
		String query = "";
		query += " SELECT ";
		query += " 	hijo.ide_bocam,";
		query += " 	hijo.ide_bocam as codigo_catalogo,";
		query += " 	hijo.cat_codigo_bocam as codigo,";
		query += " 	hijo.descripcion_bocam,";
		query += " 	padre.descripcion_bocam,";
		query += " 	abuelo.descripcion_bocam,";
		query += " 	tatarabuelo.descripcion_bocam";
		query += " FROM";
		query += " 	bodt_catalogo_material hijo ";
		query += " 	LEFT JOIN bodt_catalogo_material padre ON hijo.con_ide_bocam=padre.ide_bocam";
		query += " 	LEFT JOIN bodt_catalogo_material abuelo ON padre.con_ide_bocam=abuelo.ide_bocam";
		query += " 	LEFT JOIN bodt_catalogo_material tatarabuelo ON abuelo.con_ide_bocam=tatarabuelo.ide_bocam";
		query += " 	WHERE hijo.ide_bocam NOT IN (2,3) AND hijo.activo_bocam = " + activo;
		query += " ORDER BY hijo.ide_bocam;";
		return query;

	}

	public String getCatalagoBodegaDeCertificacion(String ide_prcer) {
		String query = " SELECT hijo.ide_bocam, " + "        hijo.ide_bocam as codigo_catalogo, "
				+ "        hijo.cat_codigo_bocam as codigo, " + "        hijo.descripcion_bocam, "
				+ "        padre.descripcion_bocam, " + "        abuelo.descripcion_bocam, "
				+ "        tatarabuelo.descripcion_bocam " + " FROM bodt_catalogo_material hijo "
				+ " LEFT JOIN bodt_catalogo_material padre ON hijo.con_ide_bocam=padre.ide_bocam "
				+ " LEFT JOIN bodt_catalogo_material abuelo ON padre.con_ide_bocam=abuelo.ide_bocam "
				+ " LEFT JOIN bodt_catalogo_material tatarabuelo ON abuelo.con_ide_bocam=tatarabuelo.ide_bocam "
				+ " WHERE hijo.ide_bocam NOT IN (2,3) " + "     AND hijo.activo_bocam = TRUE "
				+ "     AND hijo.ide_prcla IN " + "         (SELECT ide_prcla " + "          FROM pre_poa "
				+ "          LEFT JOIN pre_poa_certificacion ON pre_poa_certificacion.ide_prpoa = pre_poa.ide_prpoa "
				+ "          WHERE ide_prcer = " + ide_prcer + " ) " + " ORDER BY hijo.ide_bocam;";
		return query;

	}

	public String getCatalagoBodegaConcat(String activo) {
		String query = "";
		query += " SELECT ";
		query += " 	hijo.ide_bocam,";
		query += " 	hijo.ide_bocam as codigo_catalogo,";
		query += " 	hijo.cat_codigo_bocam as codigo,";
		query += " 	hijo.descripcion_bocam,";
		query += " 	padre.descripcion_bocam,";
		query += " 	abuelo.descripcion_bocam,";
		query += " 	tatarabuelo.descripcion_bocam";
		query += " FROM";
		query += " 	bodt_catalogo_material hijo ";
		query += " 	LEFT JOIN bodt_catalogo_material padre ON hijo.con_ide_bocam=padre.ide_bocam";
		query += " 	LEFT JOIN bodt_catalogo_material abuelo ON padre.con_ide_bocam=abuelo.ide_bocam";
		query += " 	LEFT JOIN bodt_catalogo_material tatarabuelo ON abuelo.con_ide_bocam=tatarabuelo.ide_bocam";
		query += " 	WHERE hijo.ide_bocam NOT IN (2,3) AND hijo.activo_bocam = " + activo;
		query += " ORDER BY hijo.ide_bocam;";
		return query;

	}

	public String getBodegas() {
		return "SELECT ide_boubi, detalle_boubi, direccion_boubi  FROM bodt_bodega_ubicacion WHERE activo_boubi=true;";
	}

	public String getBodega(String ide_boubi) {
		return "SELECT ide_boubi, detalle_boubi, direccion_boubi, ide_gtemp  FROM bodt_bodega_ubicacion WHERE ide_boubi="
				+ ide_boubi;
	}

	public TablaGenerica getTablaGenericaBodega(String ide_boubi) {
		TablaGenerica tg = utilitario.consultar(getBodega(ide_boubi));
		return tg;
	}

	public String getMedidas() {
		return "SELECT ide_bounm,detalle_bounm FROM bodt_unidad_medida;";
	}

	public String getCatalagoBodega(String activo, String items) {
		// items = "'73.14.06','73.14.11'"
		String query = "";
		query += " SELECT ";
		query += " 	hijo.ide_bocam,";
		query += " 	hijo.ide_bocam as codigo_catalogo,";
		query += " 	hijo.cat_codigo_bocam as codigo,";
		query += " 	hijo.descripcion_bocam,";
		query += " 	padre.descripcion_bocam,";
		query += " 	abuelo.descripcion_bocam,";
		query += " 	tatarabuelo.descripcion_bocam";
		query += " FROM";
		query += " 	bodt_catalogo_material hijo ";
		query += " 	LEFT JOIN bodt_catalogo_material padre ON hijo.con_ide_bocam=padre.ide_bocam";
		query += " 	LEFT JOIN bodt_catalogo_material abuelo ON padre.con_ide_bocam=abuelo.ide_bocam";
		query += " 	LEFT JOIN bodt_catalogo_material tatarabuelo ON abuelo.con_ide_bocam=tatarabuelo.ide_bocam";
		query += " 	WHERE hijo.ide_bocam NOT IN (2,3) AND hijo.activo_bocam = " + activo
				+ " AND hijo.cat_codigo_bocam IN (" + items + ")";
		query += " ORDER BY hijo.ide_bocam;";
		return query;
	}

	// TODO VALIDACION DEL REGISTRO DE SALDO CON EL REGISTRO DE INGRESO O EGRESO
	public void cuadrarIngresoEgreso() {

		TablaGenerica tab_novedadImportar = utilitario
				.consultar("select IDE_ASNOV,FECHA_INICIO_ASNOV,FECHA_FIN_ASNOV from asi_novedad where "
						+ "importacion_asnov=true");
		// String fechaComparacionInicio = "";
		// String fechaComparacionFin = "";
		// boolean banderaInicio = false, banderaFin = false;
		//
		// for (int i = 0; i < tab_novedadImportar.getTotalFilas(); i++) {
		// fechaComparacionInicio = tab_novedadImportar.getValor(i,
		// "FECHA_INICIO_ASNOV");
		// if (fechaComparacionInicio.equals(fechaIni)) {
		// banderaInicio = true;
		// }
		// if (fechaComparacionInicio.equals(fechaFin)) {
		// banderaInicio = true;
		// }
		// fechaComparacionFin = tab_novedadImportar.getValor(i,
		// "FECHA_FIN_ASNOV");
		// if (fechaComparacionFin.equals(fechaFin)) {
		// banderaFin = true;
		// }
		// if (fechaComparacionFin.equals(fechaIni)) {
		// banderaFin = true;
		// }
		// }
		// if (banderaInicio == true || banderaFin == true) {
		// utilitario.agregarMensajeInfo("Marcaciones importadas para este rango de
		// fechas",
		// "Datos importados Desde " + fechaIni + " hasta " + fechaFin);
		// return;
		// }
		// // Ver si tabla ya contiene las importaciones para ese rango de
		// // fecha
		// TablaGenerica tab_novedadImportarMarcacionBruto =
		// utilitario.consultar("select * from con_biometrico_marcaciones where
		// fecha_evento_cobim between "
		// + "'" + fechaIni + "' and '" + fechaFin + "'");
		// if (tab_novedadImportarMarcacionBruto.getTotalFilas() <= 0) {
		// utilitario.agregarMensajeInfo("No existen registro de marcaciones ",
		// "Datos no encontrados desde " + fechaIni + " hasta " + fechaFin);
		// return;
		// }
		// // Ver si tabla ya contiene las importaciones para ese rango de
		// // fecha
		// TablaGenerica tab_novedadImportarMarcacionExistentes =
		// utilitario.consultar("select * from con_biometrico_marcaciones_resumen where
		// fecha_evento_cobmr between "
		// + "'" + fechaIni + "' and '" + fechaFin + "'");
		// // Si esxisten
		// if (tab_novedadImportarMarcacionExistentes.getTotalFilas() > 0) {
		// utilitario.agregarMensajeInfo("Marcaciones importadas para este rango de
		// fechas",
		// "Datos importados Desde " + fechaIni + " hasta " + fechaFin);
		// return;
		// // tab_novedad.ejecutarSql();
		// // utilitario.addUpdate("tab_novedad");
		// }
		// // si no existen
		// else {
		// // inserto
		// int ide_usua =
		// Integer.parseInt(utilitario.getVariable("IDE_USUA").toString());
		// String fecha_inicio_asnov = fechaIni;
		// String fecha_fin_asnov = fechaFin;
		// String observacion_asnov = "Marcaciones importadas desde " +
		// fecha_inicio_asnov + " hasta " + fecha_fin_asnov + "";
		// String fecha_asnov = utilitario.getFechaActual();
		// // inserto datos
		// /**
		// * Obtengo el me y anio que se realiza la consulta
		// */
		// int mes = utilitario.getMes(fecha_inicio_asnov);
		// int anioTemp = utilitario.getAnio(fecha_inicio_asnov);
		// TablaGenerica getAnio =
		// utilitario.consultar("select ide_geani,detalle_geani from gen_anio where
		// detalle_geani like '%"
		// + anioTemp + "%'");
		// int anio =
		// pckUtilidades.CConversion.CInt(getAnio.getValor("ide_geani"));
		// // inserto marcaciones para rango de fecha seleccionado
		// getMarcacionesEmpleado(fechaIni, fechaFin, mes, anio, "");
		// tab_resumen_marcaciones.setCondicion("FECHA_EVENTO_COBMR BETWEEN '" +
		// fechaIni + "' AND '" + fechaFin + "' ");
		// tab_resumen_marcaciones.ejecutarSql();
		// insertarTablaNovedad(ide_usua, fecha_inicio_asnov, fecha_fin_asnov,
		// observacion_asnov, true, fecha_asnov, true, false);
		// tab_novedad.ejecutarSql();
		// utilitario.getConexion().ejecutarSql(
		// "update con_biometrico_marcaciones_resumen set " +
		// "recargonocturno25_cobmr=null, recargonocturno100_cobmr=null,
		// horafinextra_cobmr=null, novedad_cobmr=false "
		// + "where fecha_evento_cobmr between '" + fechaIni + "' and '" +
		// fechaFin
		// + "'");
		// utilitario.getConexion().ejecutarSql(
		// "update con_biometrico_marcaciones_resumen set " +
		// " novedad_cobmr =true where horainicioband_cobmr like '%OK%' and
		// horafinband_cobmr like '%OK%' "
		// + " and fecha_evento_cobmr between '" + fechaIni + "' and '" +
		// fechaFin
		// + "' and novedad_cobmr =false");
		// utilitario.getConexion().ejecutarSql(
		// "update con_biometrico_marcaciones_resumen set " +
		// " novedad_cobmr =true where horainicioband_cobmr like '%ATRASADO%' and
		// horafinband_cobmr like '%OK%' "
		// + " and fecha_evento_cobmr between '" + fechaIni + "' and '" +
		// fechaFin
		// + "' and novedad_cobmr =false");
		// utilitario.getConexion().ejecutarSql(
		// "update con_biometrico_marcaciones_resumen set " +
		// " novedad_cobmr =true where horainicioband_cobmr like '%OK%' and
		// horafinband_cobmr like '%ANTICIPADO%' "
		// + " and fecha_evento_cobmr between '" + fechaIni + "' and '" +
		// fechaFin
		// + "' and novedad_cobmr =false");
		// // /EXTRA Y FERIADO
		// utilitario.getConexion().ejecutarSql(
		// "update con_biometrico_marcaciones_resumen set " +
		// " novedad_cobmr =true where horainicioband_cobmr like '%EXTRA%' and
		// horafinband_cobmr like '%EXTRA%' "
		// + " and fecha_evento_cobmr between '" + fechaIni + "' and '" +
		// fechaFin
		// + "' and novedad_cobmr =false");
		// utilitario.getConexion().ejecutarSql(
		// "update con_biometrico_marcaciones_resumen set " +
		// " novedad_cobmr =true where horainicioband_cobmr like '%FERIADO%' and
		// horafinband_cobmr like '%FERIADO%' "
		// + " and fecha_evento_cobmr between '" + fechaIni + "' and '" +
		// fechaFin
		// + "' and novedad_cobmr =false");
		// }

	}

	/**
	 * Este servicio retorna los materiales existentes en el cálogo de materiales
	 * 
	 * @param activo
	 * @return
	 */
	public String getInventario(String grupo, String activo, String ide_botip) {

		String tab_inventario = "select ide_bomat, codigo_bomat,detalle_bomat,detalle_bogrm "
				+ "from bodt_material a,bodt_grupo_material b" + " WHERE activo_bomat in (" + activo
				+ ") AND a.ide_bogrm = b.ide_bogrm ";
		if (grupo.equals("0")) {
			tab_inventario += " AND a.ide_bogrm = b.ide_bogrm AND a.ide_botip in(" + ide_botip + ") ";
		}

		tab_inventario += "ORDER BY detalle_bogrm,detalle_bomat";
		// System.out.println("material servicio "+tab_inventario);
		return tab_inventario;

	}

	/**
	 * Este servicio retorna los datos del inventario por material y año
	 * 
	 * @param material = Recibe el ide del material a consultar
	 * @param anio     = Recibe año del inventario del material a conultar.
	 * @return String
	 */
	public String getDatosInventario(String material, String anio) {

		String tab_inventario = "select ide_boinv,ide_geani,ingreso_material_boinv,egreso_material_boinv,existencia_inicial_boinv,costo_anterior_boinv,costo_actual_boinv,"
				+ " fecha_ingr_articulo_boinv,costo_inicial_boinv,ide_bomat,(case when existencia_inicial_boinv is null then 0 else existencia_inicial_boinv end) +  (case when ingreso_material_boinv is null then 0 else  ingreso_material_boinv end) - (case when egreso_material_boinv is null then 0 else egreso_material_boinv end) as existencia_actual from bodt_inventario where ide_geani ="
				+ anio + " and ide_bomat =" + material;
		return tab_inventario;

	}

	/**
	 * Este servicio retorna los datos del inventario por año
	 * 
	 * @param anio = Recibe año del inventario del material a conultar.
	 * @return String
	 */
	public String getDatosInventarioAnio(String anio) {

		String tab_inventario = "select ide_boinv,ide_geani,ingreso_material_boinv,egreso_material_boinv,existencia_inicial_boinv,costo_anterior_boinv,costo_actual_boinv,"
				+ " fecha_ingr_articulo_boinv,costo_inicial_boinv,a.ide_bomat,(case when existencia_inicial_boinv is null then 0 else existencia_inicial_boinv end) +  (case when ingreso_material_boinv is null then 0 else  ingreso_material_boinv end) - (case when egreso_material_boinv is null then 0 else egreso_material_boinv end) as existencia_actual,codigo_bomat,detalle_bomat from bodt_inventario a, bodt_material b where a.ide_bomat = b.ide_bomat and ide_geani ="
				+ anio;
		return tab_inventario;

	}

	/**
	 * Este servicio retorna los datos del inventario por clave principal de la
	 * tabla bodt_inventario
	 * 
	 * @param ide_boinv = Recibe el ide del material a consultar
	 * @return String
	 */
	public String getDatosInventarioPrincipal(String ide_boinv) {

		String tab_inventario = "select ide_boinv,ide_geani,ingreso_material_boinv,egreso_material_boinv,existencia_inicial_boinv,costo_anterior_boinv,costo_actual_boinv,"
				+ " fecha_ingr_articulo_boinv,costo_inicial_boinv,ide_bomat,(case when existencia_inicial_boinv is null then 0 else existencia_inicial_boinv end) +  (case when ingreso_material_boinv is null then 0 else  ingreso_material_boinv end) - (case when egreso_material_boinv is null then 0 else egreso_material_boinv end) as existencia_actual from bodt_inventario where ide_boinv in ("
				+ ide_boinv + "); ";
		return tab_inventario;

	}

	public TablaGenerica getTablaProveedor(String ide_tepro) {
		TablaGenerica tab_proveedor = utilitario.consultar("select ide_tepro,nombre_tepro,ruc_tepro "
				+ " from tes_proveedor where ide_tepro in (" + ide_tepro + ")" + " order by nombre_tepro");
		return tab_proveedor;
	}

	public TablaGenerica getTablaProveedorPorRuc(String ruc) {
		TablaGenerica tab_proveedor = utilitario
				.consultar("select ide_tepro,nombre_tepro,ruc_tepro from tes_proveedor where ruc_tepro = '" + ruc
						+ "' order by nombre_tepro");
		return tab_proveedor;
	}

	public String getProveedor(String activo) {
		String tab_proveedor = "select ide_tepro,nombre_tepro,ruc_tepro  from tes_proveedor where activo_tepro in ("
				+ activo + ") order by nombre_tepro";
		return tab_proveedor;

	}

	/**
	 * Metodo que permite obtener el inventario con su descripcion de material
	 * 
	 * @return String SQL, codigo del inventario, nombre del material, codigo del
	 *         material.
	 */
	public String getInventarioMaterial() {
		String tab_proveedor = "select ide_boinv,codigo_bomat,detalle_bomat from bodt_inventario a, bodt_material b"
				+ " where a.ide_bomat = b.ide_bomat order by codigo_bomat";
		return tab_proveedor;

	}

	/**
	 * Metodo que permite crear matriz para generaciuon de Kardex de Bodega
	 * 
	 * @return Boolean, true si se creo la matriz correctamente, false si no se creo
	 */
	public Boolean matrizKardexInventarios(String codigo_materiales) {
		System.out.println("kardex " + codigo_materiales);
		Boolean resultado = false;
		double val_ingreso = 0;
		double val_egreso = 0;
		TablaGenerica inventario = utilitario.consultar(getDatosInventarioPrincipal(codigo_materiales));
		String sql_insert = "";
		utilitario.getConexion().ejecutarSql("delete from rep_bodt_kardex; ");
		for (int i = 0; i < inventario.getTotalFilas(); i++) {
			TablaGenerica total_ingresos = utilitario
					.consultar(numeroMatrizKardex("bodt_bodega", inventario.getValor(i, "ide_boinv")));
			TablaGenerica total_egresos = utilitario
					.consultar(numeroMatrizKardex("bodt_egreso", inventario.getValor(i, "ide_boinv")));
			val_ingreso = Double.parseDouble(total_ingresos.getValor("contador"));
			val_egreso = Double.parseDouble(total_egresos.getValor("contador"));

			if (val_ingreso < val_egreso) {
				TablaGenerica inventario_egreso = utilitario
						.consultar(datosMatrizKardex("bodt_egreso", inventario.getValor(i, "ide_boinv")));
				TablaGenerica inventario_ingreso = utilitario
						.consultar(datosMatrizKardex("bodt_bodega", inventario.getValor(i, "ide_boinv")));
				for (int j = 0; j < inventario_egreso.getTotalFilas(); j++) {
					sql_insert = "insert into rep_bodt_kardex (codigo_rebok,ide_boinv,codigo_ingreso_rebok,codigo_egreso_rebok) values ("
							+ j + "," + inventario.getValor(i, "ide_boinv") + ",null,"
							+ inventario_egreso.getValor(j, "ide_boegr") + ");";
					utilitario.getConexion().ejecutarSql(sql_insert);

				}
				for (int k = 0; k < inventario_ingreso.getTotalFilas(); k++) {
					String sql_update = "update rep_bodt_kardex set codigo_ingreso_rebok="
							+ inventario_ingreso.getValor(k, "ide_bobod") + " where ide_boinv="
							+ inventario.getValor(i, "ide_boinv") + " and codigo_rebok=" + k;
					utilitario.getConexion().ejecutarSql(sql_update);

				}
			} else {
				TablaGenerica inventario_ingreso = utilitario
						.consultar(datosMatrizKardex("bodt_bodega", inventario.getValor(i, "ide_boinv")));
				TablaGenerica inventario_egreso = utilitario
						.consultar(datosMatrizKardex("bodt_egreso", inventario.getValor(i, "ide_boinv")));

				for (int j = 0; j < inventario_ingreso.getTotalFilas(); j++) {
					sql_insert = "insert into rep_bodt_kardex (codigo_rebok,ide_boinv,codigo_ingreso_rebok,codigo_egreso_rebok) values ("
							+ j + "," + inventario.getValor(i, "ide_boinv") + ","
							+ inventario_ingreso.getValor(j, "ide_bobod") + ",null);";
					utilitario.getConexion().ejecutarSql(sql_insert);

				}
				for (int k = 0; k < inventario_egreso.getTotalFilas(); k++) {
					String sql_update = "update rep_bodt_kardex set codigo_egreso_rebok="
							+ inventario_ingreso.getValor(k, "ide_boegr") + " where ide_boinv="
							+ inventario.getValor(i, "ide_boinv") + " and codigo_rebok=" + k;
					utilitario.getConexion().ejecutarSql(sql_update);

				}

			}

		}
		return resultado;
	}

	public void blcybld() {
		String sql_update = "";
		sql_update = "UPDATE afi_activo SET ide_aftia=1 WHERE ide_bocam IN (SELECT ide_bocam FROM bodt_catalogo_material WHERE cat_codigo_bocam like '84.01.03');";
		utilitario.getConexion().ejecutarSql(sql_update);
		sql_update = "UPDATE afi_activo SET ide_aftia=1 WHERE ide_bocam IN (SELECT ide_bocam FROM bodt_catalogo_material WHERE cat_codigo_bocam like '84.01.04');";
		utilitario.getConexion().ejecutarSql(sql_update);
		sql_update = "UPDATE afi_activo SET ide_aftia=1 WHERE ide_bocam IN (SELECT ide_bocam FROM bodt_catalogo_material WHERE cat_codigo_bocam like '84.01.07');";
		utilitario.getConexion().ejecutarSql(sql_update);
		sql_update = "UPDATE afi_activo SET ide_aftia=1 WHERE ide_bocam IN (SELECT ide_bocam FROM bodt_catalogo_material WHERE cat_codigo_bocam like '84.01.11');";
		utilitario.getConexion().ejecutarSql(sql_update);
		sql_update = "UPDATE afi_activo SET ide_aftia=2 WHERE ide_bocam IN (SELECT ide_bocam FROM bodt_catalogo_material WHERE cat_codigo_bocam like '53.14.03');";
		utilitario.getConexion().ejecutarSql(sql_update);
		sql_update = "UPDATE afi_activo SET ide_aftia=2 WHERE ide_bocam IN (SELECT ide_bocam FROM bodt_catalogo_material WHERE cat_codigo_bocam like '53.14.04');";
		utilitario.getConexion().ejecutarSql(sql_update);
		sql_update = "UPDATE afi_activo SET ide_aftia=2 WHERE ide_bocam IN (SELECT ide_bocam FROM bodt_catalogo_material WHERE cat_codigo_bocam like '53.14.06');";
		utilitario.getConexion().ejecutarSql(sql_update);
		sql_update = "UPDATE afi_activo SET ide_aftia=2 WHERE ide_bocam IN (SELECT ide_bocam FROM bodt_catalogo_material WHERE cat_codigo_bocam like '53.14.07');";
		utilitario.getConexion().ejecutarSql(sql_update);
		sql_update = "UPDATE afi_activo SET ide_aftia=2 WHERE ide_bocam IN (SELECT ide_bocam FROM bodt_catalogo_material WHERE cat_codigo_bocam like '53.14.11');";
		utilitario.getConexion().ejecutarSql(sql_update);
		sql_update = "UPDATE afi_activo SET ide_aftia=2 WHERE ide_bocam IN (SELECT ide_bocam FROM bodt_catalogo_material WHERE cat_codigo_bocam like '73.14.03');";
		utilitario.getConexion().ejecutarSql(sql_update);
		sql_update = "UPDATE afi_activo SET ide_aftia=2 WHERE ide_bocam IN (SELECT ide_bocam FROM bodt_catalogo_material WHERE cat_codigo_bocam like '73.14.04');";
		utilitario.getConexion().ejecutarSql(sql_update);
		sql_update = "UPDATE afi_activo SET ide_aftia=2 WHERE ide_bocam IN (SELECT ide_bocam FROM bodt_catalogo_material WHERE cat_codigo_bocam like '73.14.06');";
		utilitario.getConexion().ejecutarSql(sql_update);
		sql_update = "UPDATE afi_activo SET ide_aftia=2 WHERE ide_bocam IN (SELECT ide_bocam FROM bodt_catalogo_material WHERE cat_codigo_bocam like '73.14.11');";
		utilitario.getConexion().ejecutarSql(sql_update);
		sql_update = "UPDATE afi_activo SET ide_aftia=2 WHERE ide_bocam IN (SELECT ide_bocam FROM bodt_catalogo_material WHERE cat_codigo_bocam like '73.14.07');";
		utilitario.getConexion().ejecutarSql(sql_update);
		System.out.println("BLC y BLD");
	}

	public String datosMatrizKardex(String tabla, String codigo) {
		String sql = "Select * from " + tabla + " where ide_boinv=" + codigo;
		System.out.println("DATOS MATRIZ " + sql);
		return sql;
	}

	public String numeroMatrizKardex(String tabla, String codigo) {
		String sql = "select a.ide_boinv,(case when contador is null then 0 else contador end) as contador"
				+ " from bodt_inventario a" + " left join (" + " select count(ide_boinv) as contador,ide_boinv"
				+ " from " + tabla + " where not ide_boinv is null" + " group by ide_boinv"
				+ " ) b on a.ide_boinv = b.ide_boinv" + " where a.ide_boinv=" + codigo;
		System.out.println("nunero MATRIZ " + sql);

		return sql;
	}

	public double getResultadoStock(String ide_bomat, String ide_geani) {
		double stock;
		TablaGenerica sql = utilitario.consultar(
				"select 1 as codigo, inicial-egreso as stock from (select ingreso_material_boinv,egreso_material_boinv,"
						+ " (CASE WHEN ingreso_material_boinv is null THEN 0 ELSE ingreso_material_boinv end) as inicial,"
						+ " (CASE WHEN egreso_material_boinv is null THEN 0 ELSE egreso_material_boinv end)as egreso"
						+ " from bodt_inventario" + " where ide_bomat in (" + ide_bomat + ") and ide_geani in ("
						+ ide_geani + ")) a");

		stock = Double.parseDouble(sql.getValor("stock").toString());
		return stock;

	}

	/**
	 * Metodo que permite registrar de forma automatica la cantidad de material
	 * ingresados al stock de inventarios
	 * 
	 * @param material         recibe el codigo del material de bodega
	 * @param anio             recibe el anio fiscal en el que esea ingresra al
	 *                         inventario el material.
	 * @param cantidad_ingreso recibe la cantidad de material a ser ingresado a los
	 *                         inventarios.
	 * @param valor_total      recibe el valor total del material a ser ingresado a
	 *                         los inventarios.
	 * @return boolean True = si se registro con exito en el inventario False= si
	 *         existio algun error al actualizar inventarios.
	 */
	public boolean registraInventarioIngresos(String material, String anio, String cantidad_ingreso,
			String valor_total) {
		boolean confirma = false;
		double stock = 0;
		TablaGenerica datos_inventario = utilitario.consultar(getDatosInventario(material, anio));
		double costo_actual = Double.parseDouble(datos_inventario.getValor("costo_actual_boinv"));
		stock = getResultadoStock(material, anio);
		// Permite conocer el valor actual de mi stock
		double valor_stock_inventario = costo_actual * stock;
		// Cantidad actual ingresada a inventarios desde el formulario
		double cantidad_actual = Double.parseDouble(cantidad_ingreso);
		// Valor total actual ingresado para inventarios desde el formulario
		double valor_actual = Double.parseDouble(valor_total);
		// Permite suma stock existen mas el numero actual de ingreso de
		// materiales;
		double total_nueva_existencia = stock + cantidad_actual;
		// Permite Sumar Valores para depues determinar mi costo vigente por
		// producto
		double valor_nueva_existencia = valor_stock_inventario + valor_actual;
		// Determino el valor indivual nuevo del producto
		double valor_nuevo_individual = valor_nueva_existencia / total_nueva_existencia;

		String sql_actualiza_inventarios = "update bodt_inventario set ingreso_material_boinv=ingreso_material_boinv+"
				+ cantidad_actual + ",costo_anterior_boinv=costo_actual_boinv, costo_actual_boinv="
				+ valor_nuevo_individual + " where ide_bomat=" + material + " and ide_geani=" + anio + ";";

		String resultado_upadte = "";
		resultado_upadte = utilitario.getConexion().ejecutarSql(sql_actualiza_inventarios);
		if (resultado_upadte.isEmpty()) {
			confirma = true;
		}
		return confirma;
	}

	/**
	 * Metodo que permite registrar de forma automatica la cantidad de material
	 * egresado
	 * 
	 * @param material        recibe el codigo del material de bodega
	 * @param cantidad_egreso recibe la cantidad de material a ser egresado a los
	 *                        inventarios.
	 * @return boolean True = si se registro con exito en el inventario False= si
	 *         existio algun error al actualizar inventarios.
	 */
	public boolean registraInventarioEgresos(String material, String cantidad_egreso) {
		boolean confirma = false;
		// Sql actualiza los egresos en inventarios
		String sql_actualiza_inventarios = "update bodt_inventario set egreso_material_boinv = egreso_material_boinv + "
				+ cantidad_egreso + " where ide_boinv =" + material + ";";

		String resultado_upadte = "";
		resultado_upadte = utilitario.getConexion().ejecutarSql(sql_actualiza_inventarios);
		if (resultado_upadte.isEmpty()) {
			confirma = true;
		}
		return confirma;
	}

	public String getSolicitud(String activo) {
		String tab_solicitud = "select ide_adsoc,detalle_adsoc,nro_solicitud_adsoc,valor_adsoc,nombre_tepro,ruc_tepro "
				+ " from adq_solicitud_compra a , tes_proveedor b"
				+ " where a.ide_tepro=b.ide_tepro and activo_adsoc in(" + activo + ") order by nro_solicitud_adsoc";
		return tab_solicitud;
	}

	public String getSolicitudFactura(String activo) {
		String tab_solicitud = "select c.ide_adfac,(case when num_factura_adfac is null then 'S/D' else num_factura_adfac end) as num_factura_adfac,fecha_factura_adfac,nro_solicitud_adsoc,valor_adsoc,ruc_tepro,nombre_tepro,detalle_adsoc"
				+ " from adq_solicitud_compra a , tes_proveedor b, adq_factura c"
				+ " where a.ide_tepro=b.ide_tepro and a.ide_adsoc = c.ide_adsoc and activo_adfac in(" + activo
				+ ") order by nro_solicitud_adsoc";
		return tab_solicitud;
	}

	public String getEgresoBodegaActivos(String ide_geani) {
		String tab_solicitud = "select ide_bocoe,numero_egreso_bocoe,fecha_egreso_bocoe,uso_bocoe"
				+ " from bodt_concepto_egreso where activo_bocoe = true and ide_bocoe  in ("
				+ " select ide_bocoe from bodt_egreso where ide_bobod in ("
				+ " select ide_bobod from bodt_bodega where tipo_ingreso_bobod=2 and ide_geani = " + ide_geani
				+ " ) group by ide_bocoe ) order by numero_egreso_bocoe desc";
		return tab_solicitud;
	}

	public String getMaterialesPorEgreso(String ide_bocoe, String estado) {
		String tab_solicitud = "select c.ide_boegr,ide_bocoe,documento_egreso_boegr,codigo_bomat,detalle_bomat,fecha_egreso_boegr,"
				+ " cantidad_egreso_boegr,fecha_compra_bobod,num_factura_bobod,descripcion_bobod,marca_bobod,"
				+ " modelo_bobod,serie_bobod,color_bobod, b.ide_tepro,nombre_tepro"
				+ " from bodt_material a,bodt_bodega b,bodt_egreso c,tes_proveedor d"
				+ " where a.ide_bomat = b.ide_bomat" + " and b.ide_bobod = c.ide_bobod"
				+ " and b.ide_tepro = d.ide_tepro" + " and ide_bocoe in ( " + ide_bocoe + " ) "
				+ " and activo_boegr in (" + estado + " )" + " order by detalle_bomat";
		return tab_solicitud;
	}

	public String getMaterialesEgresoCodigo(String ide_boegr, String estado) {
		String tab_solicitud = "select c.ide_boegr,ide_bocoe,documento_egreso_boegr,codigo_bomat,detalle_bomat,fecha_egreso_boegr,"
				+ " cantidad_egreso_boegr,fecha_compra_bobod,num_factura_bobod,descripcion_bobod,marca_bobod,"
				+ " modelo_bobod,serie_bobod,color_bobod, b.ide_tepro,nombre_tepro,valor_unitario_bobod,valor_total_bobod"
				+ " from bodt_material a,bodt_bodega b,bodt_egreso c,tes_proveedor d"
				+ " where a.ide_bomat = b.ide_bomat" + " and b.ide_bobod = c.ide_bobod"
				+ " and b.ide_tepro = d.ide_tepro" + " and c.ide_boegr in ( " + ide_boegr + " ) "
				+ " order by detalle_bomat";
		return tab_solicitud;
	}

	public String getEstadoFactura(String ide_solicitud_compra, String estado) {
		String tab_solicitud = "select ide_adfac,ide_adsoc,activo_adfac from adq_factura where ide_adsoc in ("
				+ ide_solicitud_compra + ") and activo_adfac in (" + estado + ")";
		return tab_solicitud;
	}

	public TablaGenerica getTablaGenericaSolicitudCompra(String ide_addef) {
		TablaGenerica tab_solicitud_comp = utilitario.consultar(
				"select b.ide_addef,b.ide_adfac,a.ide_tepro, a.ide_adsoc,detalle_adsoc,num_factura_adfac,valor_adsoc,nro_solicitud_adsoc,valor_total_addef,valor_unitario_addef,cantidad_addef,"
						+ " codigo_bomat,detalle_bomat,d.ide_bomat,fecha_factura_adfac,marca_addef,serie_addef,color_addef,modelo_addef from adq_solicitud_compra a,adq_detalle_factura b,adq_factura c , bodt_material d"
						+ " where a.ide_adsoc=c.ide_adsoc and b.ide_adfac=c.ide_adfac  and d.ide_bomat=b.ide_bomat and  b.ide_addef in ("
						+ ide_addef + ") order by codigo_bomat");
		return tab_solicitud_comp;
	}

	public TablaGenerica getTablaGenericaValidaInventario(String ide_addef, String anio) {
		TablaGenerica tab_solicitud_comp = utilitario
				.consultar("select ide_addef,a.ide_bomat from adq_detalle_factura a"
						+ " left join bodt_inventario b on  a.ide_bomat= b.ide_bomat and b.ide_geani =" + anio
						+ " where b.ide_bomat is null and ide_addef in (" + ide_addef + ")");
		return tab_solicitud_comp;
	}

	public TablaGenerica getTablaGenericaMaterial(String ide_bomat) {
		TablaGenerica tab_solicitud_comp = utilitario
				.consultar("select ide_bomat,detalle_bomat,codigo_bomat from bodt_material where ide_bomat in ("
						+ ide_bomat + ")");
		return tab_solicitud_comp;
	}

	public String getEgresoSolicitud() {
		String tab_solicitud = "select a.ide_adsoc,num_factura_bobod as numero_factura,numero_ingreso_bobod as ingreso_bodega,num_doc_bobod as documento_ingreso_bodega,detalle_adsoc as detalle_compra,"
				+ " nro_solicitud_adsoc as numero_solicitud_compra,valor_adsoc as valor_compra,nombre_tepro as proveedor,ruc_tepro as ruc_proveedor"
				+ " from adq_solicitud_compra a , tes_proveedor b,("
				+ " select ide_adsoc,num_factura_bobod,num_doc_bobod,numero_ingreso_bobod from bodt_bodega where activo_bobod =true  group by ide_adsoc,num_doc_bobod,num_factura_bobod,numero_ingreso_bobod"
				+ " ) c where a.ide_tepro=b.ide_tepro and a.ide_adsoc = c.ide_adsoc order by num_doc_bobod";
		return tab_solicitud;
	}

	public TablaGenerica getEgresoSolicitudBodega(String compra) {
		TablaGenerica tab_solicitud = utilitario.consultar(
				"select ide_bobod,ide_bomat,cantidad_ingreso_bobod,ide_adsoc from bodt_bodega where ide_adsoc ="
						+ compra);
		return tab_solicitud;
	}

	public String getMaterialBodegaCompras() {
		String tab_solicitud = "select ide_bobod,detalle_bomat, codigo_bomat from bodt_bodega a,bodt_material b where a.ide_bomat = b.ide_bomat and not ide_adsoc is null";
		return tab_solicitud;
	}

	public String getCodigoMaterial(String cod_material) {
		String tab_solicitud = "";
		return tab_solicitud;
	}
}
