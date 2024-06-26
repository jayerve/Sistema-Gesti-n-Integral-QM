package paq_adquisicion.ejb;

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
public class ServicioAdquisicion {
	private Utilitario utilitario = new Utilitario();
	
	public TablaGenerica getRetencionesDeFactura(String ide_adfac) {
		TablaGenerica tg_retenciones = utilitario.consultar(String.format(
				"SELECT ide_teret, ide_adfac, num_retencion_teret FROM tes_retencion WHERE ide_adfac = %s AND ide_coest != 1",
				ide_adfac));
		return tg_retenciones;
	}

	public String getSolicitudCompra(String activo) {

		String tab_solicitud = "select a.ide_adsoc,a.ide_tepro,nombre_tepro,detalle_adsoc,nro_solicitud_adsoc,total_adfac,valor_iva_adfac,num_factura_adfac" + " from adq_solicitud_compra a,adq_factura b ,tes_proveedor c"
				+ " where  a.ide_adsoc=b.ide_adsoc and a.ide_tepro=c.ide_tepro and activo_adsoc in (" + activo + ") order by detalle_adsoc";
		return tab_solicitud;

	}

	public String getSolicitud(String ide_adsoc) {

		String tab_solicitud = "select a.ide_adsoc,a.ide_tepro,nombre_tepro,detalle_adsoc,nro_solicitud_adsoc,total_adfac,num_factura_adfac" + " from adq_solicitud_compra a,adq_factura b ,tes_proveedor c"
				+ " where  a.ide_adsoc=b.ide_adsoc and a.ide_tepro=c.ide_tepro and a.ide_adsoc in (" + ide_adsoc + ") order by detalle_adsoc";

		return tab_solicitud;

	}

	public TablaGenerica getTablaGenericaSolicitud(String ide_adsoc) {
		TablaGenerica tab_solicitud = utilitario.consultar("select a.ide_adsoc,a.ide_tepro,a.ide_prtra,nombre_tepro,detalle_adsoc,nro_solicitud_adsoc,total_adfac,valor_iva_adfac,num_factura_adfac,subtotal_adfac"
				+ " from adq_solicitud_compra a,adq_factura b ,tes_proveedor c " + " where  a.ide_adsoc=b.ide_adsoc and a.ide_tepro=c.ide_tepro and a.ide_adsoc in (" + ide_adsoc + ") order by detalle_adsoc");

		return tab_solicitud;

	}

	public String getFacturasCompra(String ide_prcon) {

		String tab_facturasC = "select b.ide_adfac, (case when coalesce(codigo_srsuc,'02') like '02' then false else true end) as aplica_credito,secuencial_prtra as compromiso,comprobante_egreso_tecpo,numero_documento_ingeg as nro_ingreso_existencia,"
				+ " nro_solicitud_adsoc,fecha_factura_adfac,num_factura_adfac,nombre_tepro, total_adfac,valor_iva_adfac"
				+ " from adq_factura b "
				+ " left join adq_solicitud_compra a on a.ide_adsoc=b.ide_adsoc"
				+ " left join tes_proveedor c on a.ide_tepro=c.ide_tepro"
				+ " left join sri_sustento_comprobante ssc on ssc.ide_srsuc=b.ide_srsuc"
				+ " left join pre_tramite comp on comp.ide_prtra=b.ide_prtra"
				+ " left join bodt_ingreso_egreso big on big.ide_adfac=b.ide_adfac"
				+ " left join tes_comprobante_pago tcp on tcp.ide_tecpo=b.ide_tecpo" 
				+ " where activo_adfac in (true) and comp.ide_prcon=" +ide_prcon
				+ " order by fecha_factura_adfac desc";
		return tab_facturasC;

	}
	
	public String getFacturasCompra(String activo, String anio) {

		String tab_facturasC = "select b.ide_adfac, (case when coalesce(codigo_srsuc,'02') like '02' then false else true end) as aplica_credito,secuencial_prtra as nro_compromiso,retenciones,notas_creditos"
				+ " ,fecha_factura_adfac as fecha_factura,num_factura_adfac as num_factura,nombre_tepro as proveedor, total_adfac as valor_total,valor_iva_adfac as valor_iva"
				+ "  from adq_factura b "
				+ "  join tes_proveedor c on c.ide_tepro=b.ide_tepro"
				+ "  left join sri_sustento_comprobante ssc on ssc.ide_srsuc=b.ide_srsuc"
				+ "  left join pre_tramite comp on comp.ide_prtra=b.ide_prtra " 
				+ "  left join (select count(ide_teret) as retenciones, ide_adfac from tes_retencion group by ide_adfac) rt on rt.ide_adfac=b.ide_adfac"
				+ "  left join (select count(ide_adncr) as notas_creditos, ide_adfac from adq_nota_credito group by ide_adfac) nc on nc.ide_adfac=b.ide_adfac"

				+ " where activo_adfac in ("+activo+") " 
				+ " and extract(year from fecha_factura_adfac)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+anio+") "
				+ " order by fecha_factura_adfac desc";
		return tab_facturasC;

	}

	public String actualizarNumeroRetencion(String ide_fanod) {
		String numeroRet = "";
		String sql = "update tes_retencion set num_retencion_teret = case when (length(num_retencion_teret)<6 or num_retencion_teret is null) and ide_coest=2 then "
				+ " '001-011-' || lpad(''||(SELECT max(cast(coalesce(substring(num_retencion_teret from 9 for length(num_retencion_teret)), '0') as integer))+1 FROM tes_retencion where num_retencion_teret like '001-011%'),9,'0') else "
				+ "  num_retencion_teret end  " + " where ide_teret=" + ide_fanod + ";";

		utilitario.getConexion().ejecutarSql(sql);

		List list_sql1 = utilitario.getConexion().consultar("SELECT num_retencion_teret FROM tes_retencion where ide_teret=" + ide_fanod + " limit 1");
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null) {
			numeroRet = String.valueOf(list_sql1.get(0));
		}

		System.out.println("actualizarNumeroRetencion: " + numeroRet);

		return numeroRet;
	}

	public boolean compruebaRetencion(String estb, String pto, String secuencial) {
		boolean existe = false;

		List list_sql1 = utilitario.getConexion().consultar(
				"SELECT ide_teret FROM tes_retencion where substring(num_retencion_teret from 1 for 3) like '" + estb + "' " + " and substring(num_retencion_teret from 5 for 3) like '" + pto + "' "
						+ " and substring(num_retencion_teret from 9 for 9) like '" + secuencial + "'; ");
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null) {
			existe = true;
		}

		return existe;
	}

	public boolean compruebaFacCompra(String estb, String pto, String secuencial, String anio, String ide_tepro) {
		boolean existe = false;

		List list_sql1 = utilitario.getConexion().consultar(
				"SELECT ide_adfac FROM adq_factura where ide_tepro='"+ide_tepro+"' and extract(year from fecha_factura_adfac)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+anio+") and substring(num_factura_adfac from 1 for 3) like '" + estb + "' " + " and substring(num_factura_adfac from 5 for 3) like '" + pto + "' " + " and substring(num_factura_adfac from 9 for 9) like '"+ secuencial + "'; ");
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null) {
			existe = true;
		}

		return existe;
	}

	public String getCompras(String activo) {
		String tab_compra = "select a.ide_adsoc,a.ide_tepro,nro_solicitud_adsoc,detalle_adsoc,nombre_tepro from adq_solicitud_compra a,tes_proveedor b" + " where a.ide_tepro=b.ide_tepro and activo_adsoc in (" + activo + ") order by detalle_adsoc";
		return tab_compra;
	}

	public String getComprasCodigo(String ide_adsoc) {
		String tab_compra = "select a.ide_adsoc,a.ide_tepro,nro_solicitud_adsoc,detalle_adsoc,nombre_tepro from adq_solicitud_compra a,tes_proveedor b" + " where a.ide_tepro=b.ide_tepro and  a.ide_adsoc in (" + ide_adsoc + ") order by detalle_adsoc";
		return tab_compra;
	}
	
	public String getFacturaComprasCodigo(String ide_adfac) {
		String tab_compra = "select ide_adfac, ide_adsoc, num_factura_adfac, fecha_factura_adfac, "
          +" detalle_adfac, valor_descuento_adfac, subtotal_adfac, total_adfac, "
          +" valor_iva_adfac, base_iva_adfac, activo_adfac,  "
          +" porcent_desc_adfac, aplica_descuento_adfac, ide_tepro, ide_prtra, "
          +" nro_autorizacion_sri_adq, base_noiva_adfac, ide_tecpo, ide_srsuc from adq_factura " 
          +" where ide_adfac =" + ide_adfac;
		return tab_compra;
	}

	public String getComprasCombo(String activo) {
		String tab_compra = "select a.ide_adsoc,(case when nro_solicitud_adsoc is null then 'S/N' else nro_solicitud_adsoc end) as nro_solicitud_adsoc ,nombre_tepro,fecha_solicitud_adsoc from adq_solicitud_compra a,tes_proveedor b"
				+ " where a.ide_tepro=b.ide_tepro and activo_adsoc in (" + activo + ") order by detalle_adsoc";
		return tab_compra;
	}

	public String getTramite(String activo) {
		String tab_estado = "SELECT a.ide_prtra,a.ide_prtra as nro_compromiso,fecha_tramite_prtra,numero_oficio_prtra,total_compromiso_prtra " + " FROM pre_tramite a, cont_estado b" + " WHERE a.ide_coest=b.ide_coest and activo_prtra in (" + activo + ") "
				+ " order by numero_oficio_prtra";
		return tab_estado;

	}

	public TablaGenerica getTablaGenericaTramite(String ide_prtra) {
		TablaGenerica tab_estado = utilitario.consultar("SELECT a.ide_prtra,fecha_tramite_prtra,numero_oficio_prtra,total_compromiso_prtra " + " FROM pre_tramite a, cont_estado b" + " WHERE a.ide_coest=b.ide_coest and a.ide_prtra in (" + ide_prtra + ") "
				+ " order by numero_oficio_prtra");
		return tab_estado;

	}

	public TablaGenerica getTablaGenericaTramitePOA(String ide_prtra) {
		TablaGenerica tab_estado = utilitario.consultar("select ide_prpot,ide_prpoa,ide_prtra,comprometido_prpot,observaciones_prpot from pre_poa_tramite where ide_prtra in (" + ide_prtra + ")");
		return tab_estado;

	}

	public boolean compruebaLiqCompra(String estb, String pto, String secuencial) {
		boolean existe = false;

		List list_sql1 = utilitario.getConexion().consultar(
				"SELECT ide_adlic FROM adq_liquidacion_compra where substring(num_liquidacion_adlic from 1 for 3) like '" + estb + "' " + " and substring(num_liquidacion_adlic from 5 for 3) like '" + pto + "' " + " and substring(num_liquidacion_adlic from 9 for 9) like '"
						+ secuencial + "'; ");
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null) {
			existe = true;
		}

		return existe;
	}
	
	public void actualizarNumeroLiquidacionCompra(String ide_adlic){
		String sql ="update adq_liquidacion_compra set num_liquidacion_adlic='001-011-' || "
					+ " lpad(''||(select coalesce(MAX(cast(substring(num_liquidacion_adlic from 11 for 7) as integer)),0)+1 FROM adq_liquidacion_compra),9,'0')"
					+ " where ide_adlic="+ide_adlic+" and (length(num_liquidacion_adlic)<1 or num_liquidacion_adlic is null);";
		
		utilitario.getConexion().ejecutarSql(sql);
		System.out.println("actualizarNumeroLiquidacionCompra: "+sql);
	}

	public String getLiquidacionCompra(String activo) {

		String tab_facturasC = "select a.ide_adlic,secuencial_prtra as compromiso,num_liquidacion_adlic,fecha_adlic,nombre_tepro, total_adlic"
				+ " from adq_liquidacion_compra a "
				+ " left join tes_proveedor b on b.ide_tepro=a.ide_tepro"
				+ " left join pre_tramite c on c.ide_prtra=a.ide_prtra  "
				+ " where a.ide_coest=2 and activo_adlic in (true) and coalesce(autorizada_sri_adlic,false)=true  "
				+ " order by fecha_adlic desc";
		return tab_facturasC;

	}
	
	public TablaGenerica getTablaGenericaLiquidacionCompra(String ide_adlic) {
		TablaGenerica tab_solicitud = utilitario.consultar("select * from adq_liquidacion_compra where ide_adlic in (" + ide_adlic + ") ");

		return tab_solicitud;

	}

}
