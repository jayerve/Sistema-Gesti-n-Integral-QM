package paq_facturacion.ejb;

import java.util.List;

import javax.ejb.Stateless;

import paq_sistema.aplicacion.Utilitario;
import framework.aplicacion.TablaGenerica;


/**
 * Session Bean implementation class ServicioFacturacion
 */
@Stateless

public class ServicioFacturacion {
	private Utilitario utilitario=new Utilitario();


	public String getClientes(String matrizSucursal ){
	    /*String tab_cliente="select ide_recli, ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli, nro_establecimiento_recli,nombre_comercial_recli, direccion_recld, codigo, nro_contrato_recli,activo_recli,MATRIZ_SUCURSAL_RECLI from ("+
	    		" select a.ide_recli, cast(a.ide_recli as character(25)) as codigo,nro_contrato_recli, ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli," +
	            "  nro_establecimiento_recli,nombre_comercial_recli, " +
	            "  direccion_recld,activo_recli,MATRIZ_SUCURSAL_RECLI from rec_clientes a " +
	            " LEFT JOIN rec_cliente_direccion b on a.ide_recli=b.ide_recli and b.activo_recld = true " +
	            " ) a "+
	            " where activo_recli in (true,false) and coalesce(MATRIZ_SUCURSAL_RECLI,0) in ("+matrizSucursal+")  ORDER BY  nombre_comercial_recli " ;*/
		
		String tab_cliente="SELECT cli.ide_recli "
				+" , coalesce(numero_contrato,'0') as numero_contrato "
				+" , coalesce(cli.ruc_comercial_recli,'') as ruc_comercial_recli  "
				+" , coalesce(cli.nro_establecimiento_recli,0) as nro_establecimiento_recli"
				+" , coalesce(cli.razon_social_recli,cli.nombre_comercial_recli) as razon_social_recli "
				+" , coalesce(cli.establecimiento_operativo_recli,'') as establecimiento_operativo_recli "
				+" , coalesce(cliD.direccion_recld,'') as direccion_recld  "
				+" , coalesce(cliE.email_recle,'') as email_recle  "
				+" , coalesce(cli.telefono_factura_recli,'') as telefono_factura_recli  "
				+" , coalesce(dias_geplf,0) as dias_plazo  "
				+" FROM rec_clientes cli  "
				+" left join (select ide_recli,coalesce(barrio_recld || ', ','')  || coalesce(direccion_recld || ' ','') || coalesce(nmro_casa_lote_dpto_recld || ' ','') || coalesce(interseccion_recld,'') as direccion_recld from rec_cliente_direccion where activo_recld=true and ide_recld in (select min(ide_recld) from rec_cliente_direccion" 
				+"     where activo_recld=true group by ide_recli)) cliD on cliD.ide_recli=cli.ide_recli  "
				+" left join (select ide_recli, string_agg(email_recle, ',') as email_recle from rec_cliente_email where notificacion_recle=true and activo_recle=true group by ide_recli ) cliE on cliE.ide_recli=cli.ide_recli"  
				+" left join (select ide_recli, string_agg(numero_contrato_prcon, ',') as numero_contrato from pre_contrato where ide_coest=2 group by ide_recli) d on d.ide_recli=cli.ide_recli "
				+" left join gen_plazo_facturas pf on pf.ide_geplf=cli.ide_geplf  ";
				//+" where coalesce(cli.activo_recli,false) in (true) and cli.nro_establecimiento_recli=1  ";
		
		    if(matrizSucursal.equals("-1"))
				tab_cliente+=" where cli.ide_recli in ( select ide_recli from fac_convenio) ";
						
			tab_cliente+=" order by razon_social_recli  ";
		
	    return tab_cliente;
	}
	
	public String getClientesActivos(String matrizSucursal ){
	    /*String tab_cliente="select ide_recli, ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli, nro_establecimiento_recli,nombre_comercial_recli, direccion_recld, codigo, nro_contrato_recli,activo_recli,MATRIZ_SUCURSAL_RECLI from ("+
	    		" select a.ide_recli, cast(a.ide_recli as character(25)) as codigo,nro_contrato_recli, ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli," +
	            "  nro_establecimiento_recli,nombre_comercial_recli, " +
	            "  direccion_recld,activo_recli,MATRIZ_SUCURSAL_RECLI from rec_clientes a " +
	            " LEFT JOIN rec_cliente_direccion b on a.ide_recli=b.ide_recli and b.activo_recld = true " +
	            " ) a "+
	            " where coalesce(activo_recli,false) = true and coalesce(MATRIZ_SUCURSAL_RECLI,0) in ("+matrizSucursal+")  ORDER BY  ruc_comercial_recli " ;
	    */
	    
		String tab_cliente="SELECT cli.ide_recli "
						+" , coalesce(numero_contrato,'0') as numero_contrato "
						+" , coalesce(cli.ruc_comercial_recli,'') as ruc_comercial_recli  "
						+" , cli.fac_mensual_recli "
						+" , coalesce(cli.nro_establecimiento_recli,0) as nro_establecimiento_recli"
						+" , coalesce(dias_geplf,0) as dias_plazo  "
						+" , coalesce(cli.razon_social_recli,cli.nombre_comercial_recli) as razon_social_recli "
						+" , coalesce(cli.establecimiento_operativo_recli,'') as establecimiento_operativo_recli "
						+" , coalesce(cliD.direccion_recld,'') as direccion_recld  "
						+" , coalesce(cliE.email_recle,'') as email_recle  "
						+" , coalesce(cli.telefono_factura_recli,'') as telefono_factura_recli  "
						
						+" FROM rec_clientes cli  "
						+" left join (select ide_recli,coalesce(barrio_recld || ', ','')  || coalesce(direccion_recld || ' ','') || coalesce(nmro_casa_lote_dpto_recld || ' ','') || coalesce(interseccion_recld,'') as direccion_recld from rec_cliente_direccion where activo_recld=true and ide_recld in (select min(ide_recld) from rec_cliente_direccion" 
						+"     where activo_recld=true group by ide_recli)) cliD on cliD.ide_recli=cli.ide_recli  "
						+" left join (select ide_recli, string_agg(email_recle, ',') as email_recle from rec_cliente_email where notificacion_recle=true and activo_recle=true group by ide_recli ) cliE on cliE.ide_recli=cli.ide_recli"  
						+" left join (select ide_recli, string_agg(numero_contrato_prcon, ',') as numero_contrato from pre_contrato where ide_coest=2 group by ide_recli) d on d.ide_recli=cli.ide_recli "
						+" left join gen_plazo_facturas pf on pf.ide_geplf=cli.ide_geplf  "
						+" where coalesce(cli.activo_recli,false) = true "
						+" order by razon_social_recli  ";
		
	    return tab_cliente;
	}
	
	public String getClientesMatrices(String ide_recli ){

		String tab_cliente="SELECT cli.ide_recli ";
				if(ide_recli.equals("0"))
					tab_cliente+=" , coalesce(cli.ide_recli,'0') as Codigo ";
				
				tab_cliente+=" , cast('Contrato - ' || coalesce(numero_contrato,nro_contrato_recli) as character(25)) as numero_contrato "
				+" , cast(coalesce(cli.ruc_comercial_recli,'') as character(15)) as ruc_comercial_recli  "
				+" , coalesce(cli.nro_establecimiento_recli,0) as nro_establecimiento_recli"
				+" , coalesce(cli.razon_social_recli,cli.nombre_comercial_recli) as razon_social_recli "		
				+" , coalesce(cli.activo_recli,false) as activo_recli ";
		
		if(!ide_recli.equals("0"))
			tab_cliente+=" , coalesce(cli.establecimiento_operativo_recli,'') as establecimiento_operativo_recli "
				+" , coalesce(cliD.direccion_recld,'') as direccion_recld  "
				+" , coalesce(nombre_contacto_reclt, representante_legal_recli) as representante_legal_recli "
				+" , coalesce(cliE.email_recle,'') as email_recle  "
				+" , coalesce(cli.telefono_factura_recli,'') as telefono_factura_recli  "
				+" , coalesce(dias_geplf,0) as dias_plazo  ";
		
		tab_cliente+=" FROM rec_clientes cli  "
				+" left join (select ide_recli,coalesce(barrio_recld || ', ','')  || coalesce(direccion_recld || ' ','') || coalesce(nmro_casa_lote_dpto_recld || ' ','') || coalesce(interseccion_recld,'') as direccion_recld from rec_cliente_direccion where activo_recld=true and ide_recld in (select min(ide_recld) from rec_cliente_direccion" 
				+"     where activo_recld=true group by ide_recli)) cliD on cliD.ide_recli=cli.ide_recli  "
				+" left join (select ide_recli, string_agg(email_recle, ',') as email_recle from rec_cliente_email where notificacion_recle=true and activo_recle=true group by ide_recli ) cliE on cliE.ide_recli=cli.ide_recli"  
				+" left join (select ide_recli, string_agg(numero_contrato_prcon, ',') as numero_contrato from pre_contrato where ide_coest=2 group by ide_recli) d on d.ide_recli=cli.ide_recli "
				+" left join gen_plazo_facturas pf on pf.ide_geplf=cli.ide_geplf  "
				+" left join rec_cliente_telefono ccli on ide_retco=1 and activo_reclt=true and ccli.ide_recli=cli.ide_recli "
				+" where coalesce(cli.activo_recli,false) in (true,false) and cli.nro_establecimiento_recli=1 ";
				
		if(!ide_recli.equals("0"))
				tab_cliente +=" and cli.ide_recli = "+ide_recli;
		
				tab_cliente +=" order by razon_social_recli  ";
		
		System.out.println("getClientesMatrices: "+tab_cliente);
				
	    return tab_cliente;
	}
	
	public String getEstablecimientosActivos(String estado ){
	    /*String tab_cliente="select ide_recli, ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli, nro_establecimiento_recli,nombre_comercial_recli, direccion_recld, codigo, nro_contrato_recli,activo_recli,MATRIZ_SUCURSAL_RECLI from ("+
	    		" select a.ide_recli, cast(a.ide_recli as character(25)) as codigo,nro_contrato_recli, ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli," +
	            "  nro_establecimiento_recli,nombre_comercial_recli, " +
	            "  direccion_recld,activo_recli,MATRIZ_SUCURSAL_RECLI from rec_clientes a " +
	            " LEFT JOIN rec_cliente_direccion b on a.ide_recli=b.ide_recli and b.activo_recld = true " +
	            " ) a "+
	            " where coalesce(activo_recli,false) = true and coalesce(MATRIZ_SUCURSAL_RECLI,0) in ("+matrizSucursal+")  ORDER BY  ruc_comercial_recli " ;
	    */
	    
		String tab_cliente="SELECT ide_reest  "
				 +" , coalesce(cli.ruc_comercial_recli,'') as ruc_comercial_recli   "
				 +" , coalesce(nro_establecimiento_reest,0) as nro_establecimiento_recli "
				 +" , coalesce(direccion_reest,'') as direccion_recld   "
				 +" , coalesce(cli.razon_social_recli,cli.nombre_comercial_recli) as razon_social_recli"
				 +" , coalesce(numero_contrato,'0') as numero_contrato  "
				 +" , coalesce(establecimiento_operativo_reest,'') as establecimiento_operativo_recli  "
				 +" , coalesce(cliE.email_recle,'') as email_recle   "
				 +" , coalesce(cli.telefono_factura_recli,'') as telefono_factura_recli"
				 +" , coalesce(dias_geplf,0) as dias_plazo"
				 +" ,cli.ide_recli"
				 +" , cli.fac_mensual_recli   "
				
				 +" FROM rec_clientes cli   "
				 +" join rec_clientes_establecimiento est on est.ide_recli=cli.ide_recli "
				 +" left join (select ide_recli, string_agg(email_recle, ',') as email_recle from rec_cliente_email where notificacion_recle=true and activo_recle=true group by ide_recli ) cliE on cliE.ide_recli=cli.ide_recli "  
				 +" left join (select ide_recli, string_agg(numero_contrato_prcon, ',') as numero_contrato from pre_contrato where activo_prcon=true group by ide_recli) d on d.ide_recli=cli.ide_recli  "
				 +" left join gen_plazo_facturas pf on pf.ide_geplf=cli.ide_geplf   "
				 +" where coalesce(cli.activo_recli,false) = true "
				 //+ " and coalesce(factura_datos_recli,1)=0 "
				 + " and coalesce(activo_reest,false) = true "
				 //+" and ruc_comercial_recli like '1768054040001' "
				 +" order by razon_social_recli,nro_establecimiento_reest   ";
						
		System.out.println("getEstablecimientosActivos "+tab_cliente);
	    return tab_cliente;
	}
	public String getClientesConciliacion(String matrizSucursal ){
	    String tab_cliente="select a.ide_recli, ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli," +
	            "  nro_establecimiento_recli,nombre_comercial_recli, " +
	            "  direccion_recld from rec_clientes a " +
	            " LEFT JOIN rec_cliente_direccion b on a.ide_recli=b.ide_recli and b.activo_recld = true " +
	            //" where a.activo_recli in (true,false) and coalesce(MATRIZ_SUCURSAL_RECLI,0) in ("+matrizSucursal+")" +
	            " where a.activo_recli in (true,false) " +
	            " ORDER BY  nombre_comercial_recli";
	    return tab_cliente;
	}
	
	public TablaGenerica getTablaBodega (String tabla){
		
		TablaGenerica tab_bodega=utilitario.consultar("select a.ide_recli, a.aplica_mtarifa_recli " +
				"FROM rec_clientes a, tes_tarifas b " +
				"WHERE a.ide_tetar=b.ide_tetar and a.aplica_mtarifa_recli=true and ide_recli=1 and b.ide_tetar=1"+tabla+"'");
		return tab_bodega;
		
		}
	
	public TablaGenerica getTablaGenericaFactura(String codigo){
		
		String sql="SELECT ide_sucu, ide_retip, ide_recli, ide_tetid, 'Facturas anuladas: ' || string_agg(secuencial_fafac, ', ') as observacion_fafac, "
				+ " sum(base_aprobada_fafac) as base_aprobada_fafac,sum(valor_iva_fafac) as valor_iva_fafac,sum(total_fafac) as total_fafac, "
				+ " sum(descuento_fafac) as descuento_fafac,sum(irbpnr_fafac) as irbpnr_fafac "
				+ " FROM fac_factura where ide_fafac in ("+codigo+") "
				+ " group by ide_sucu, ide_retip, ide_recli, ide_tetid;";

		 TablaGenerica tab_cabecera_factura=utilitario.consultar(sql);
		 return tab_cabecera_factura;
		 
	 }
	
	public TablaGenerica getTablaGenericaDetFactura(String codigo){
		 
		 TablaGenerica tab_cabecera_factura=utilitario.consultar("SELECT ide_bomat, cantidad_fadef, valor_fadef, total_fadef, descuento_fadef,irbpnr_fadef, observacion_fadef,secuencial_fafac, secuencial_fafac || ' ' || coalesce(observacion_fadef,'') as observacion "
		 		+ " FROM fac_detalle_factura fd "
		 		+ " left join fac_factura f on f.ide_fafac=fd.ide_fafac "
		 		+ " where fd.ide_fafac in ("+codigo+");");
		 return tab_cabecera_factura;
		 
	 }
	
	public String getFacturas(String numeroFactura ){
	    String tab_cliente="SELECT ide_bomat, codigo_bomat, detalle_bomat FROM bodt_material b WHERE codigo_bomat is not null " +
				"and ide_bogrm in(select c.ide_bogrm from fac_usuario_lugar  a " +
				"inner join fac_lugar b on a.ide_falug=b.ide_falug and a.ide_usua=" +utilitario.getVariable("ide_usua")+
				" inner join fac_venta_lugar c on c.ide_falug=b.ide_falug " +
				"inner join  bodt_grupo_material d on c.ide_bogrm=d.ide_bogrm ) order by detalle_bomat";
	    return tab_cliente;
	}

	public String getFacturasAnuladas(String establecimiento ){
		 String tab_facturas="SELECT fac.ide_fafac,fecha_transaccion_fafac as fecha_emision, "	
			+" secuencial_fafac as secuencial,cli.ruc_comercial_recli as ruc, "	
			+" cli.razon_social_recli as cliente, base_aprobada_fafac as sub_total, valor_iva_fafac as iva,  total_fafac as total, "		
			+" fecha_vencimiento_fafac as fecha_vencimiento, est.detalle_coest as estado, autorizada_sri_fafac as autorizada_sri "	
			+" FROM fac_factura fac "	
			+" join cont_estado est on est.ide_coest=fac.ide_coest "	
			+" join fac_datos_factura facd on facd.ide_fadaf=fac.ide_fadaf "	
			+" join bodt_grupo_material bgm on bgm.ide_bogrm = facd.ide_bogrm "	
			+" join rec_clientes cli on cli.ide_recli=fac.ide_recli ";
		 tab_facturas+=" where fac.ide_coest=1 and fac.ide_fadaf="+establecimiento+" order by 1;";
			 	
		 //System.out.println("getFacturasAnuladas "+tab_facturas);
	    return tab_facturas;
	}

	public ServicioFacturacion() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Este servicio retorna los datos de la factura
	 * @param grupos recibe 1 para mostrar todos los materiales y
	 * @param grupoMaterial recibe 0 para mostrar solo por grupos de acuerdo los ide enviados en el parámetro 
	 * @return retorna un string con el siguiente contenido (codigo datos factura, autorización 
	 */
	public String getDatosFactura(String grupos,String grupoMaterial){
		String tab_datos_factura="select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, detalle_bogrm " +
		" from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm ";
		if(grupos.equals("0")){
			tab_datos_factura +=" and b.ide_bogrm in ("+grupoMaterial+") ";
		}
		tab_datos_factura += " order by autorizacion_sri_bogrm";
		//System.out.println("datos factura");
		return tab_datos_factura;
	}
	
	public boolean getDatosFacturaModificar(String ide_fadaf){
		boolean bloqueado=false;
		List list_sql1 = utilitario.getConexion().consultar("select bloqueado_fadaf from fac_datos_factura where ide_fadaf= "+ide_fadaf);	
		try{
			if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
				System.out.println("getDatosFacturaModificar: "+list_sql1.get(0).toString());
				bloqueado=Boolean.parseBoolean(list_sql1.get(0).toString());
				System.out.println("getDatosFacturaModificar: "+bloqueado);
			}
		}
		catch(Exception ex)
		{System.out.println("getDatosFacturaModificar Error: "+ex.getMessage());}
		
		return !bloqueado;
		
	}
	
	public String getDatosSucursales(){
		String tab_datos_sucursales="select ide_bogrm,autorizacion_sri_bogrm,detalle_bogrm "
                + "from bodt_grupo_material where autorizacion_sri_bogrm is not null "
                + "order by autorizacion_sri_bogrm";

		return tab_datos_sucursales;
	}
	
	 public String getCabeceraFactura(String grupos,String cliente){
		 String tab_cabecera_factura="select ide_fafac,secuencial_fafac,fecha_transaccion_fafac,fecha_vencimiento_fafac,"
		 		+ "fecha_pago_fafac,detalle_bogrm,base_aprobada_fafac,valor_iva_fafac,total_fafac" +
		 		" from fac_datos_factura a, bodt_grupo_material b,fac_factura c " +
		 		" where a.ide_bogrm = b.ide_bogrm and a.ide_fadaf=c.ide_fadaf" ;
		 		if(grupos.equals("0")){
		 			tab_cabecera_factura+=" and c.ide_recli in("+cliente+") and coalesce(autorizada_sri_fafac,false)=true ";		
		 		}
		 		if(grupos.equals("1")){
		 			tab_cabecera_factura+=" and a.ide_fadaf=-1 ";		
		 		}
		 		if(grupos.equals("2")){
		 			tab_cabecera_factura+=" and a.ide_fadaf in("+cliente+") and c.ide_coest in (2,24,30) and coalesce(autorizada_sri_fafac,false)=true ";		
		 		}
		 tab_cabecera_factura+=" order by secuencial_fafac desc ";
		 return tab_cabecera_factura;
	 }
	 
	 public String getCabeceraNotaCredito(){
		 String tab_cabecera_notaCredito="SELECT ide_fanoc, fecha_fanoc,nro_nota_credito_fanoc, valor_referencial_fanoc, " +
				 "	iva_fanoc, total_fanoc, autorizada_sri_fanoc, detalle_fanoc    " +
				 "	FROM fac_nota_credito " ;
		 tab_cabecera_notaCredito+=" where coalesce(autorizada_sri_fanoc,false) = false ";
		 //tab_cabecera_notaCredito+=" and extract(year from fecha_fanoc)=2021 and extract(month from fecha_fanoc)=6  ";
		 tab_cabecera_notaCredito+=" order by ide_fanoc desc ";
		 return tab_cabecera_notaCredito;
	 }
	 
	 public String getComprobantes(String grupos,String filtro,String fecha_inicial,String fecha_final, boolean aplFechaPago, boolean aplFechaConciliado, boolean aplFechaAbono, String tipoComp, String tipoEst){
		 
		 String tab_comprobantes="SELECT ROW_NUMBER() OVER (ORDER BY tipo_documento) AS codigo,tipo_documento,sucursal,estado,fecha_emision,secuencial,ruc,cliente,base_cero,sub_total,iva,total,valor_cobro, "
		 		+ " (case when tipo_documento like 'NOTA CREDITO' then 0.00 else (case when coalesce(valor_cobro,0)=0 and estado like 'Pagado' then total else valor_cobro end)end) as valor_cancelado, "
		 		+ " (total - coalesce((case when tipo_documento like 'NOTA CREDITO' then 0.00 else (case when coalesce(valor_cobro,0)=0 and estado like 'Pagado' then total else valor_cobro end)end),0)) as saldo,  "
		 		+ " fecha_vencimiento,fecha_pago,fecha_conciliado,fecha_anulado,comprobante_aplicado,fecha_documento_apl,observacion,transaccion_bp,autorizada_sri " //,contabilizado,recaudado "
		 		+ " from (";

		 String tab_facturas="SELECT distinct cast('FACTURA' as character(25)) as tipo_documento, detalle_bogrm as sucursal, (case when est.ide_coest=24 and coalesce(valor_cobro,0)>0 then 'Abonada ' || est.detalle_coest else est.detalle_coest end ) as estado,  " +
			"	fecha_transaccion_fafac as fecha_emision, " +
			"	secuencial_fafac as secuencial,cli.ruc_comercial_recli as ruc, cli.razon_social_recli as cliente,coalesce(base_cero_fafac,0) as base_cero, base_aprobada_fafac as sub_total, valor_iva_fafac as iva, total_fafac as total,valor_cobro,  " +
			"	fecha_vencimiento_fafac as fecha_vencimiento, "+(aplFechaAbono?"fecha_cobro_facob":"fecha_pago_fafac")+" as fecha_pago, fecha_conciliado_fafac as fecha_conciliado,fecha_anulado_fafac as fecha_anulado, secuencial_fafac as comprobante_aplicado, fecha_transaccion_fafac as fecha_documento_apl, coalesce(documento_bancario_fafac,' ') || ' - ' || coalesce(observacion_fafac,' ') as observacion, documento_conciliado_fafac as transaccion_bp "
			//+ ",fasi.ide_comov as contabilizado, fasir.ide_comov  as recaudado, autorizada_sri_fafac as autorizada_sri " +
			+ ", autorizada_sri_fafac as autorizada_sri " +
			"	FROM fac_factura fac " +
			"	left join cont_estado est on est.ide_coest=fac.ide_coest " +
			"	left join fac_datos_factura facd on facd.ide_fadaf=fac.ide_fadaf " +
			"	left join bodt_grupo_material bgm on bgm.ide_bogrm = facd.ide_bogrm "+
			"   left join rec_clientes cli on cli.ide_recli=fac.ide_recli ";
		    tab_facturas += "   left join (select ide_fafac"+(aplFechaAbono?",fecha_cobro_facob":"")+" ,sum(coalesce(valor_cobro_facob,0)+coalesce(valor_cobro_iva_facob,0)) as valor_cobro from fac_cobro group by ide_fafac"+(aplFechaAbono?",fecha_cobro_facob":"")+") fc on fc.ide_fafac = fac.ide_fafac  ";

			/*tab_facturas += " left join cont_factura_asiento fasi on fasi.ide_fafac=fac.ide_fafac and fasi.ide_coest=2 and fasi.ide_conac in (select ide_conac from cont_nombre_asiento_contable where ide_gemod=1) "
					     + " left join cont_factura_asiento fasir on fasir.ide_fafac=fac.ide_fafac and fasir.ide_coest=16 ";*/
			
			if(grupos.equals("1")){
				tab_facturas+=" where bgm.ide_bogrm in("+filtro+") ";
	 			if(aplFechaPago)
	 				tab_facturas+=" and fecha_pago_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' ";
	 			else
	 				if(aplFechaConciliado)
	 					tab_facturas+=" and fecha_conciliado_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' ";
		 			else
		 				if(aplFechaAbono)
		 					tab_facturas+=" and fecha_cobro_facob between '"+fecha_inicial+"' and '"+fecha_final+"' ";
		 				else
		 					tab_facturas+=" and fecha_transaccion_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' ";
	 		}
			
			if(pckUtilidades.CConversion.CInt(tipoEst)>0)
			{
				tab_facturas+=" and fac.ide_coest = "+tipoEst;
			}
			
	
			String tab_notaCredito = "SELECT distinct cast('NOTA CREDITO' as character(25)) as tipo_documento, detalle_bogrm as sucursal, " +
			"	est.detalle_coest as estado, fecha_fanoc as fecha_emision, " +
			"	nro_nota_credito_fanoc as secuencial,cli.ruc_comercial_recli as ruc, cli.razon_social_recli as cliente,0 as base_cero, valor_referencial_fanoc as sub_total, iva_fanoc as iva, total_fanoc as total, 0.00 as valor_cobro, " +
			"	cast('1900-01-01' as date) as fecha_vencimiento,cast('1900-01-01' as date) as fecha_pago,cast('1900-01-01' as date) as fecha_conciliado,fecha_anulado_fafac as fecha_anulado,fac.secuencial_fafac as comprobante_aplicado, fecha_transaccion_fafac as fecha_documento_apl, detalle_fanoc as observacion, 'N/A' as transaccion_bp "
			//+ ",fasi.ide_comov as contabilizado, fasir.ide_comov  as recaudado, autorizada_sri_fanoc as autorizada_sri " +
			+ ",autorizada_sri_fanoc as autorizada_sri " +
			"	FROM fac_nota_credito nc " +
			"	left join bodt_grupo_material bgm on bgm.autorizacion_sri_bogrm = substring(nro_nota_credito_fanoc from 1 for 3) " +
			"	left join cont_estado est on est.ide_coest=nc.ide_coest "+
			"   left join fac_factura fac on fac.ide_fafac=nc.ide_fafac "+
            "   left join rec_clientes cli on cli.ide_recli=fac.ide_recli " ;
        	//+ " left join cont_factura_asiento fasi on fasi.ide_fafac=fac.ide_fafac and fasi.ide_coest=2 and fasi.ide_conac in (select ide_conac from cont_nombre_asiento_contable where ide_gemod=1) "
			//+ " left join cont_factura_asiento fasir on fasir.ide_fafac=fac.ide_fafac and fasir.ide_coest=16 ";
			if(grupos.equals("1")){
				tab_notaCredito+=" where bgm.ide_bogrm in("+filtro+")";		
				tab_notaCredito+=" and fecha_fanoc between '"+fecha_inicial+"' and '"+fecha_final+"' ";
				
				if(aplFechaAbono)
					tab_notaCredito+=" and nc.ide_fanoc=-1 ";
	 		}	
			
			if(pckUtilidades.CConversion.CInt(tipoEst)>0)
			{
				tab_notaCredito+=" and nc.ide_coest = "+tipoEst;
			}
			

			String tab_notaDebito = "SELECT distinct cast('NOTA DEBITO' as character(25)) as tipo_documento, detalle_bogrm as sucursal, " +
			"	est.detalle_coest as estado, fecha_emision_fanod as fecha_emision, " +
			"	nro_nota_debito_elect_fanod as secuencial,cli.ruc_comercial_recli as ruc, cli.razon_social_recli as cliente,0 as base_cero, interes_generado_fanod as sub_total, 0 as iva, interes_generado_fanod as total, valor_cobro, " +
			"	cast('1900-01-01' as date) as fecha_vencimiento,"+(aplFechaAbono?"fecha_cobro_facob":"nd.fecha_actua")+" as fecha_pago,cast('1900-01-01' as date) as fecha_conciliado,fecha_anulado_fafac as fecha_anulado,detalle_fenod as comprobante_aplicado, fecha_transaccion_fafac as fecha_documento_apl, (case when nd.usuario_actua like 'Banco del Pacifico' then nd.usuario_actua else ide_transaccion_fanod end) as observacion "
			//+ ", ide_transaccion_fanod as transaccion_bp ,(case when nasi.ide_coest=2 then nasi.ide_comov else 0 end) as contabilizado, (case when nasi.ide_coest=16 then nasi.ide_comov else 0 end) as recaudado, autorizada_sri_fanod as autorizada_sri" +
			+ ", ide_transaccion_fanod as transaccion_bp , autorizada_sri_fanod as autorizada_sri" +
			"	FROM fac_nota_debito nd " +
			"	left join bodt_grupo_material bgm on bgm.autorizacion_sri_bogrm = (case when nro_nota_debito_elect_fanod is not null then substring(nro_nota_debito_elect_fanod from 1 for 3) else substring(detalle_fenod from 1 for 3) end) " +
			"	left join cont_estado est on est.ide_coest=nd.ide_coest "+
			"	left join fac_detalle_debito dnd on dnd.ide_fanod=nd.ide_fanod "+
			"	left join fac_factura fac on fac.ide_fafac=dnd.ide_fafac "+
		    "   left join rec_clientes cli on cli.ide_recli=nd.ide_recli ";
			
			tab_notaDebito += "   left join (select ide_fanod"+(aplFechaAbono?",fecha_cobro_facob":"")+",sum(coalesce(valor_cobro_interes_facob,0)) as valor_cobro from fac_cobro group by ide_fanod"+(aplFechaAbono?",fecha_cobro_facob":"")+") fc on fc.ide_fanod = nd.ide_fanod  ";

			//tab_notaDebito += "left join cont_nota_debito_asiento nasi on nasi.ide_fanod=nd.ide_fanod ";
			
			if(grupos.equals("1")){
				tab_notaDebito+=" where bgm.ide_bogrm in("+filtro+")";		
	 			
	 			if(aplFechaPago || aplFechaConciliado)
	 				tab_notaDebito+=" and nd.fecha_actua between '"+fecha_inicial+"' and '"+fecha_final+"' ";
	 			else
	 				if(aplFechaAbono)
	 					tab_notaDebito+=" and fecha_cobro_facob between '"+fecha_inicial+"' and '"+fecha_final+"' ";
	 				else
	 					tab_notaDebito+=" and fecha_emision_fanod between '"+fecha_inicial+"' and '"+fecha_final+"' ";
	 		}	
			
			if(pckUtilidades.CConversion.CInt(tipoEst)>0)
			{
				tab_notaDebito+=" and nd.ide_coest = "+tipoEst;
			}
			
			/////////////////////

			if(tipoComp.equals("0")){ //todos
				tab_comprobantes += tab_facturas+" union all  "+tab_notaCredito+" union all  "+tab_notaDebito;
			}
			
			if(tipoComp.equals("1")){ //facturas
				tab_comprobantes += tab_facturas;
			}
			
			if(tipoComp.equals("2")){ //notas credito
				tab_comprobantes += tab_notaCredito;
			}
			
			if(tipoComp.equals("3")){ //notas debito
				tab_comprobantes += tab_notaDebito;
			}
			
			tab_comprobantes+=" ) a ";	
			
	 		if(grupos.equals("0")){
	 			tab_comprobantes+=" limit 0 ";		
	 		}
	 		else
	 			tab_comprobantes+=" order by ruc,comprobante_aplicado,fecha_emision ";
	 		
		 
		 System.out.println("tab_comprobantes "+tab_comprobantes);
		 return tab_comprobantes;
	 }
	 
	 public String getClientesFactura(String estado){
		 String tab_cabecera_factura="select ide_fafac,secuencial_fafac,detalle_coest,ruc_comercial_recli,coalesce(razon_social_recli,nombre_comercial_recli) as razon_social_recli,total_fafac "
				 +" from fac_factura a "
				 + " join rec_clientes b on b.ide_recli = a.ide_recli "
				 + " left join cont_estado est on est.ide_coest=a.ide_coest"
				 +" where  a.ide_coest in ("+estado+")";		

		 return tab_cabecera_factura;
	 }
	 
	 public TablaGenerica getTablaGenericaFacturaCabecera(String codigo){
		 
		 TablaGenerica tab_cabecera_factura=utilitario.consultar("select c.ide_fafac,secuencial_fafac,factura_fisica_fafac,fecha_transaccion_fafac,detalle_bogrm,base_aprobada_fafac,"
				+" valor_iva_fafac,total_fafac,descuento_fafac,irbpnr_fafac"
				+" ,fecha_vencimiento_fafac, fecha_pago_fafac,ide_recli "
				+" ,coalesce(valor_cobro,0) as valor_cobro, coalesce(valor_cobro_iva,0) as valor_cobro_iva"
				+" from fac_datos_factura a "
				+" join bodt_grupo_material b on a.ide_bogrm = b.ide_bogrm"
				+" join fac_factura c on a.ide_fadaf=c.ide_fadaf"
				+" left join (select ide_fafac, sum(coalesce(valor_cobro_facob,0)) as valor_cobro, sum(coalesce(valor_cobro_iva_facob,0)) as valor_cobro_iva from fac_cobro group by ide_fafac) d on d.ide_fafac=c.ide_fafac  " +
		 		" where  c.ide_fafac in ("+codigo+") order by secuencial_fafac");
		 return tab_cabecera_factura;
		 
	 }
	 
	 public String getFacturasNotasContabilizadas(String ide_comov){
			String tab_datos_factura="select row_number() over(order by comprobante) as codigo, comprobante, id, secuencial, fecha_emision,estado, subtotal, iva, valor_contabilizado, observacion, nro_comprobante_cobro "
				+" from ("
				+" SELECT distinct CAST (varchar 'FACTURA' AS text)  as comprobante, cfa.ide_cofaa as id, secuencial_fafac as secuencial, fecha_transaccion_fafac as fecha_emision, "
				//+" c.detalle_coest as estado, base_aprobada_fafac as subtotal, valor_iva_fafac as iva, coalesce(valor_cobro_facob,base_aprobada_fafac) as valor_contabilizado, documento_bancario_fafac as observacion, documento_conciliado_fafac as nro_comprobante_cobro  "
				+" c.detalle_coest as estado, base_aprobada_fafac as subtotal, valor_iva_fafac as iva, coalesce(valor_cobro_facob,base_aprobada_fafac) as valor_contabilizado, documento_bancario_fafac as observacion, coalesce(cast(nro_documento_facob as character(25)),'') || ' - ' || coalesce(cast(cod_autorizacion_facob as character(25)),'')  as nro_comprobante_cobro  "
				+"  FROM cont_factura_asiento cfa"
				+"  join fac_factura fac on fac.ide_fafac=cfa.ide_fafac "
				+"  left join cont_estado c on c.ide_coest = fac.ide_coest "
				+"  left join fac_cobro fc on fc.ide_facob = cfa.ide_facob "
				+" where cfa.ide_comov="+ide_comov //+" and cfa.ide_conac in (select ide_conac from cont_nombre_asiento_contable where ide_gemod in (1)) "
				+" union all"
				+" SELECT distinct CAST (varchar 'NOTA CREDITO' AS text)  as comprobante, cfa.ide_cofaa as id, nro_nota_credito_fanoc as secuencial, fecha_fanoc as fecha_emision, "
				+ " c.detalle_coest as estado, valor_referencial_fanoc as subtotal, iva_fanoc as iva,valor_referencial_fanoc as valor_contabilizado, detalle_fanoc as observacion, '' as nro_comprobante_cobro  "
				+"   FROM cont_factura_asiento cfa"
				+"   join fac_nota_credito nc on nc.ide_fafac=cfa.ide_fafac"
				+"  left join cont_estado c on c.ide_coest = nc.ide_coest "
				+" where cfa.ide_comov="+ide_comov+" and cfa.ide_conac in (select ide_conac from cont_nombre_asiento_contable where ide_gemod in (34)) "
				+" union all"
				+" SELECT distinct CAST (varchar 'NOTA DEBITO' AS text)  as comprobante, nda.ide_conda as id, nro_nota_debito_elect_fanod as secuencial, fecha_emision_fanod as fecha_emision, "
				+ " c.detalle_coest as estado, interes_generado_fanod as subtotal, 0 as iva, interes_generado_fanod as valor_contabilizado, ide_transaccion_fanod as observacion, coalesce(cast(documento_cobro_fanod as character(25)),'') || ' - ' || coalesce(cast(nro_documento_facob as character(25)),'') as nro_comprobante_cobro "
				+" FROM cont_nota_debito_asiento nda"
				+" join fac_nota_debito nd on nd.ide_fanod=nda.ide_fanod"
				+"  left join cont_estado c on c.ide_coest = nd.ide_coest "
				+"  left join fac_cobro fc on fc.ide_fanod = nd.ide_fanod "
				+" where nda.ide_comov="+ide_comov
				+" union all"
				+" SELECT distinct CAST (varchar 'ANTICIPO CONTRATO' AS text) as comprobante, cat.ide_coana as id, lpad(numero_contrato_prcon,4,'0') as secuencial, fecha_cobro_facob as fecha_emision," 
				+" c.detalle_coest as estado, coalesce(valor_cobro_facob,0) as subtotal, coalesce(valor_cobro_iva_facob,0) as iva,coalesce(valor_cobro_facob,0) as valor_contabilizado, observaciones_facob as observacion, cast(nro_documento_facob as character varying(10)) as nro_comprobante_cobro" 
				+" FROM cont_anticipo_asiento cat"
				+" join fac_cobro fc on fc.ide_facob=cat.ide_facob"
				+" join pre_contrato crt on crt.ide_prcon=fc.ide_prcon"
				+" left join cont_estado c on c.ide_coest = crt.ide_coest" 
				+" where cat.ide_comov="+ide_comov
				+" union all"
				+" SELECT distinct CAST (varchar 'ACTA E-R PET' AS text)  as comprobante, cfa.ide_cofaa as id, acta_secuencial_acerp as secuencial, fecha_emision_acerp as fecha_emision, "
				+ " c.detalle_coest as estado, precio_total_acerp as subtotal, 0 as iva, precio_total_acerp as valor_contabilizado, observacion_acerp as observacion, '' as nro_comprobante_cobro  "
				+" FROM cont_factura_asiento cfa"
				+" join fac_acta_entrega_recepcion aer on aer.ide_fafac=cfa.ide_fafac"
				+"  left join cont_estado c on c.ide_coest = aer.ide_coest "
				+" where cfa.ide_comov="+ide_comov //+" and cfa.ide_conac in (select ide_conac from cont_nombre_asiento_contable where ide_gemod in (1)) "
				+" ) a ";
			if(ide_comov.equals("-1"))
				tab_datos_factura +=" limit 0";
			
			System.out.println("getFacturasNotasContabilizadas: "+tab_datos_factura);
			return tab_datos_factura;
		}
	 
	 	/**
		 * Este servicio retorna los datos de la factura para ser contabilizados, y a su ves consulta los asientos
		 * @param tipo_emision recibe 1 para mostrar las facturas de tipo emision,2 para mostrar facturas tipo recaudacion, 0 para consultar facturas en asientos contables 
		 * @param fecha_inicial si tipo_emision=1, fecha iniciail corresponde a fecha transacccion iniicial, si tipo_emision=2 corresponde a fecha de pago de la factura. 
		 * @return retorna un string con el siguiente contenido: codigo de la factura, numero del asiento contable, nombre del asiento contable, ruc del proveedor, nombre del proveedor, numero de factura y los respectivos valores de la factura
		 */
		public String getDatosFacturaContabilidad(//String tipo_emision,
				String fecha_inicial,String fecha_final, //String estado,
				String grupo_material,String anio_anterior,String ide_coest){
			System.out.println("datos factura contabilizar estado: "+ide_coest);
			
			String tab_datos_factura=
					"select distinct a.ide_fafac,secuencial_fafac,ruc_comercial_recli,razon_social_recli,base_aprobada_fafac,"
					+" valor_iva_fafac,total_fafac, coalesce(valor_cobro,0) as abono, estado_f, observacion || ' - ' || coalesce(cliente_pago,' ') as observacion,fecha_transaccion_fafac,coalesce(fecha_cobro_facob, fecha_pago_fafac) as fecha_cobro_facob,detalle_bogrm,td.detalle_tetid as tipo,"
					+ (ide_coest.equals("30")?" coalesce(nro_documento,'') || ' - ' || coalesce(cod_autorizacion,'') as ": "" )+" nro_comp_caja,autorizada_sri_fafac as autorizada "
					+" from ("

						+" select ide_fafac,secuencial_fafac,ruc_comercial_recli,razon_social_recli,base_aprobada_fafac,valor_iva_fafac,(base_aprobada_fafac+valor_iva_fafac) as total_fafac,factura_fisica_fafac,fecha_transaccion_fafac, " 
						+" detalle_bogrm,fecha_pago_fafac,b.ide_bogrm,c.ide_tetid,autorizada_sri_fafac, coalesce(documento_bancario_fafac,' ') || ' - ' || coalesce(observacion_fafac,' ') as observacion, "
					 	+(ide_coest.equals("30")?"":" coalesce(documento_conciliado_fafac,' ') as nro_comp_caja,")+"c.ide_coest, detalle_coest as estado_f "
					 	+" from fac_datos_factura a, bodt_grupo_material b, fac_factura c, rec_clientes d, cont_estado e "
					 	+" where a.ide_bogrm = b.ide_bogrm and a.ide_fadaf=c.ide_fadaf and c.ide_recli = d.ide_recli and c.ide_coest=e.ide_coest "
						+" 	) a"
						+ (ide_coest.equals("30")?"":" 	left join cont_factura_asiento b on a.ide_fafac = b.ide_fafac and b.ide_coest in ("+ide_coest + (ide_coest.equals("30")?",16":"") + ") and b.ide_conac in (select ide_conac from cont_nombre_asiento_contable where ide_gemod in (1))")
						+ (ide_coest.equals("30")?"":" 	left join cont_estado c on b.ide_coest = c.ide_coest ")
						+ (ide_coest.equals("30")?"":" 	left join cont_nombre_asiento_contable d on b.ide_conac = d.ide_conac and a.ide_bogrm=d.ide_bogrm")
						+" left join tes_tipo_documento td on td.ide_tetid=a.ide_tetid "
					    + (ide_coest.equals("30")?"":"left") +" join (select ide_fafac,fecha_cobro_facob, sum(coalesce(valor_cobro_facob,0)+coalesce(valor_cobro_iva_facob,0)) as valor_cobro, string_agg(cliente_pago_facob, ', ') as cliente_pago,"
																		+ " string_agg(cast(nro_documento_facob as character(25)), ', ') as nro_documento, string_agg(cast(cod_autorizacion_facob as character(25)), ', ') as cod_autorizacion "
																		+ " from fac_cobro where ide_facob not in (select ide_facob from cont_factura_asiento where ide_facob>0) and ide_fafac>0 group by ide_fafac,fecha_cobro_facob) fc on fc.ide_fafac = a.ide_fafac  ";
						
						tab_datos_factura += " where ((a.ide_tetid=4 and coalesce(autorizada_sri_fafac,false)=true) or (a.ide_tetid=3)) ";
						
						if(ide_coest.equals("2")){//emitido
							tab_datos_factura +=" and fecha_transaccion_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' and b.ide_comov is null and a.ide_bogrm="+grupo_material+" ";
						}
						
						if(ide_coest.equals("16")){//pagado
							tab_datos_factura +=" and coalesce(valor_cobro,0)=0 and fecha_pago_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' and b.ide_comov is null and a.ide_bogrm="+grupo_material+" and extract(year from fecha_transaccion_fafac) "+anio_anterior	;
						}
						
						if(ide_coest.equals("30")){//abonos
							tab_datos_factura +=" and a.ide_coest in (16,24,"+ide_coest +") and a.ide_bogrm="+grupo_material ;
							tab_datos_factura +=" and fc.fecha_cobro_facob between '"+fecha_inicial+"' and '"+fecha_final+"' ";
							tab_datos_factura +=" and coalesce(valor_cobro,0) > 0 ";
							tab_datos_factura +=" and extract(year from fecha_transaccion_fafac) "+anio_anterior;
						}
						
						tab_datos_factura += " order by secuencial_fafac ";
						
						if(ide_coest.equals("0")){
							tab_datos_factura +=" limit 0 "	;
						}
						
			System.out.println("datos factura contabilizar: "+tab_datos_factura);
			return tab_datos_factura;
		}
	 	/**
		 * Este servicio retorna los datos de las notas de debito para ser contabilizados, y a su ves consulta los asientos
		 * @param tipo_emision recibe 1 para mostrar las notas de debito de tipo emision,2 para mostrar notas de debito tipo recaudacion, 0 para consultar notas de debito en asientos contables 
		 * @param fecha_inicial si tipo_emision=1, fecha iniciail corresponde a fecha transacccion iniicial, si tipo_emision=2 corresponde a fecha de pago de la notas de debito. 
		 * @return retorna un string con el siguiente contenido: codigo de la notas de debito, numero del asiento contable, nombre del asiento contable, ruc del proveedor, nombre del proveedor, numero de factura y los respectivos valores de la factura
		 */
		public String getDatosNotaDebitoContabilidad(String tipo_emision,String fecha_inicial,String fecha_final,String ide_bogrm){
			/*String tab_datos_factura="select a.ide_fafac,b.ide_comov,b.ide_coest,b.ide_conac,detalle_coest,detalle_conac,secuencial_fafac,ruc_comercial_recli,razon_social_recli,base_aprobada_fafac,"
					+" valor_iva_fafac,total_fafac,factura_fisica_fafac,fecha_transaccion_fafac,observacion,detalle_bogrm,interes_generado_fanod,fecha_emision_fanod,a.ide_fanod,fecha_pago_fafac,nro_comp_caja,autorizada_sri_fanod as autorizada"
					+" from ("
						+" 	select c.ide_fafac,secuencial_fafac,ruc_comercial_recli,razon_social_recli,base_aprobada_fafac,valor_iva_fafac,total_fafac,factura_fisica_fafac,fecha_transaccion_fafac,"
						+ " detalle_bogrm,fecha_pago_fafac,interes_generado_fanod,fecha_emision_fanod,ide_fanod,autorizada_sri_fanod, observacion, documento_conciliado_fafac as nro_comp_caja "
						+" 	from fac_datos_factura a, bodt_grupo_material b,fac_factura c,rec_clientes d,(select a.ide_fanod,interes_generado_fanod,fecha_emision_fanod,ide_fafac,autorizada_sri_fanod,(case when a.usuario_actua like 'Banco del Pacifico' then a.usuario_actua else ide_transaccion_fanod end) as observacion "
							+" 		from fac_nota_debito a, fac_detalle_debito b"
							+" 		where a.ide_fanod=b.ide_fanod and a.ide_coest=16) e "
							+" 		where a.ide_bogrm = b.ide_bogrm and a.ide_fadaf=c.ide_fadaf and c.ide_recli = d.ide_recli and c.ide_fafac = e.ide_fafac"
							+" ) a"
							+" left join cont_nota_debito_asiento b on a.ide_fanod = b.ide_fanod"
							+" left join cont_estado c on b.ide_coest = c.ide_coest "
							+" left join cont_nombre_asiento_contable d on b.ide_conac = d.ide_conac";
							//if(tipo_emision.equals("1")){
								tab_datos_factura +=" where fecha_emision_fanod between '"+fecha_inicial+"' and '"+fecha_final+"' and b.ide_comov is null "	;
							//}
							//if(tipo_emision.equals("2")){
							///	tab_datos_factura +=" where fecha_pago_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' and b.ide_comov is null "	;
							//}
							tab_datos_factura += " and coalesce(autorizada_sri_fanod,false)=true order by secuencial_fafac";
							System.out.println("datos nota debito contabilizar: "+tab_datos_factura);
							return tab_datos_factura;*/

			/*String tab_datos_notas_debito="select a.ide_fafac,b.ide_comov,b.ide_coest,b.ide_conac,detalle_coest,detalle_conac,fecha_emision_fanod,nro_nota_debito_elect_fanod,ruc_comercial_recli,razon_social_recli,interes_generado_fanod,fecha_transaccion_fafac,secuencial_fafac"
					+" ,observacion,detalle_bogrm,a.ide_fanod,fecha_pago_fafac,nro_comp_caja,autorizada_sri_fanod as autorizada"
					+"  from ("
					+"   select c.ide_fafac,secuencial_fafac,a.ide_bogrm,nro_nota_debito_elect_fanod,ruc_comercial_recli,razon_social_recli,base_aprobada_fafac,valor_iva_fafac,total_fafac,factura_fisica_fafac,fecha_transaccion_fafac,"
					+"   detalle_bogrm,fecha_pago_fafac,interes_generado_fanod,fecha_emision_fanod,ide_fanod,autorizada_sri_fanod, observacion, documento_conciliado_fafac as nro_comp_caja "
					+" 	from fac_datos_factura a, bodt_grupo_material b,fac_factura c,rec_clientes d,"
					+" 	(select a.ide_fanod,interes_generado_fanod,fecha_emision_fanod, nro_nota_debito_elect_fanod,ide_fafac,autorizada_sri_fanod"
					//+" 	,(case when a.usuario_actua like 'Banco%' then a.usuario_actua else ide_transaccion_fanod end) as observacion "
					+" ,(case when length(a.usuario_actua)>6 then a.usuario_actua else 'Caja Matriz' end) as observacion "
					+" 		from fac_nota_debito a, fac_detalle_debito b"
					+" 		where a.ide_fanod=b.ide_fanod and a.ide_coest=16) e "
					+" 		where a.ide_bogrm = b.ide_bogrm and a.ide_fadaf=c.ide_fadaf and c.ide_recli = d.ide_recli and c.ide_fafac = e.ide_fafac"
					+" 	 ) a"
					+" 	 left join cont_nota_debito_asiento b on a.ide_fanod = b.ide_fanod"
				    +" 	 left join cont_estado c on b.ide_coest = c.ide_coest "
					+" 	 left join cont_nombre_asiento_contable d on b.ide_conac = d.ide_conac"
					+" 	where fecha_emision_fanod between '"+fecha_inicial+"' and '"+fecha_final+"' and b.ide_comov is null and a.ide_bogrm="+ide_bogrm
					+" 	and coalesce(autorizada_sri_fanod,false)=true order by secuencial_fafac";*/
			
			
			String tab_datos_notas_debito="select a.ide_fanod,b.ide_comov,b.ide_coest,b.ide_conac,detalle_coest,detalle_conac,fecha_emision_fanod,nro_nota_debito_elect_fanod,ruc_comercial_recli,razon_social_recli,interes_generado_fanod,fecha_transaccion_fafac,secuencial_fafac"
					+" ,observacion,detalle_bogrm,fecha_pago_fafac,nro_comp_caja,autorizada_sri_fanod as autorizada"
					+"  from ("
					+"   select c.ide_fafac,secuencial_fafac,a.ide_bogrm,nro_nota_debito_elect_fanod,ruc_comercial_recli,razon_social_recli,base_aprobada_fafac,valor_iva_fafac,total_fafac,factura_fisica_fafac,fecha_transaccion_fafac,"
					+"   detalle_bogrm,fecha_pago_fafac,interes_generado_fanod,fecha_emision_fanod,ide_fanod,autorizada_sri_fanod, observacion, documento_conciliado_fafac as nro_comp_caja "
					+" 	from fac_datos_factura a, bodt_grupo_material b,fac_factura c,rec_clientes d,"
					+" 	(select a.ide_fanod,interes_generado_fanod,fecha_emision_fanod, nro_nota_debito_elect_fanod,ide_fafac,autorizada_sri_fanod"
					+" ,(case when length(a.usuario_actua)>6 then a.usuario_actua else 'Caja Matriz' end) as observacion "
					+" 		from fac_nota_debito a, fac_detalle_debito b"
					+" 		where a.ide_fanod=b.ide_fanod and a.ide_coest=16) e "
					+" 		where a.ide_bogrm = b.ide_bogrm and a.ide_fadaf=c.ide_fadaf and c.ide_recli = d.ide_recli and c.ide_fafac = e.ide_fafac"
					+" 	 ) a"
					+" 	 left join cont_nota_debito_asiento b on a.ide_fanod = b.ide_fanod"
				    +" 	 left join cont_estado c on b.ide_coest = c.ide_coest "
					+" 	 left join cont_nombre_asiento_contable d on b.ide_conac = d.ide_conac"
					+" 	where fecha_emision_fanod between '"+fecha_inicial+"' and '"+fecha_final+"' and b.ide_comov is null and a.ide_bogrm="+ide_bogrm
					+" 	and coalesce(autorizada_sri_fanod,false)=true order by secuencial_fafac";
			
			System.out.println("datos nota debito contabilizar: "+tab_datos_notas_debito);
			return tab_datos_notas_debito;
		}
	 	/**
		 * Este servicio retorna los datos de las notas de credito para ser contabilizados, y a su ves consulta los asientos
		 * @param tipo_emision recibe 1 para mostrar las notas de credito de tipo emision,2 para mostrar notas de credito tipo recaudacion, 0 para consultar notas de credito en asientos contables 
		 * @param fecha_inicial si tipo_emision=1, fecha iniciail corresponde a fecha transacccion iniicial, si tipo_emision=2 corresponde a fecha de pago de la notas de debito. 
		 * @return retorna un string con el siguiente contenido: codigo de la notas de credito, numero del asiento contable, nombre del asiento contable, ruc del proveedor, nombre del proveedor, numero de factura y los respectivos valores de la factura
		 */
		public String getDatosNotaCreditoContabilidad(String fecha_inicial,String fecha_final,String estado,String grupo_material){
			String tab_datos_factura="select a.ide_fafac,b.ide_comov,b.ide_coest,b.ide_conac,detalle_coest,detalle_conac,secuencial_fafac,ruc_comercial_recli,razon_social_recli,base_aprobada_fafac,"
					+" valor_iva_fafac,total_fafac,factura_fisica_fafac,fecha_transaccion_fafac,detalle_bogrm,fecha_fanoc,a.ide_fanoc,fecha_pago_fafac,autorizada_sri_fanoc as autorizada"
					+" from ("
						+" 	select c.ide_fafac,secuencial_fafac,ruc_comercial_recli,razon_social_recli,base_aprobada_fafac,valor_iva_fafac,total_fafac,factura_fisica_fafac,fecha_transaccion_fafac,"
						+ " detalle_bogrm,fecha_pago_fafac,fecha_fanoc,ide_fanoc,e.ide_comov,b.ide_bogrm,autorizada_sri_fanoc,nro_nota_credito_fanoc "
						+" 	from fac_datos_factura a, bodt_grupo_material b,fac_factura c,rec_clientes d,(select ide_fafac,fecha_fanoc,ide_fanoc,ide_comov,autorizada_sri_fanoc,nro_nota_credito_fanoc from fac_nota_credito where ide_coest=2) e "
						+" 	where a.ide_bogrm = b.ide_bogrm and a.ide_fadaf=c.ide_fadaf and c.ide_recli = d.ide_recli and c.ide_fafac = e.ide_fafac"
						+" 	) a"
						+" 	left join cont_factura_asiento b on a.ide_fafac = b.ide_fafac and b.ide_coest ="+estado
						+" 	left join cont_estado c on b.ide_coest = c.ide_coest "
						+" 	left join cont_nombre_asiento_contable d on b.ide_conac = d.ide_conac";
						if(estado.equals("2")){//emitido
							tab_datos_factura +=" where fecha_fanoc between '"+fecha_inicial+"' and '"+fecha_final+"' and a.ide_comov is null and a.ide_bogrm="+grupo_material+" "	;
						}
						if(estado.equals("16")){//pagado
							tab_datos_factura +=" where fecha_pago_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' and not a.ide_comov is null and a.ide_bogrm="+grupo_material	;
						}
						tab_datos_factura += " and ((substring(nro_nota_credito_fanoc from 5 for 3) like '011' and coalesce(autorizada_sri_fanoc,false)=true) "+
											" or (substring(nro_nota_credito_fanoc from 5 for 3) not like '011' and coalesce(autorizada_sri_fanoc,false)=false)) order by secuencial_fafac ";
						
						if(estado.equals("0")){//
							tab_datos_factura +=" limit 0 "	;
						}
						
							System.out.println("datos nota credito contabilizar"+tab_datos_factura);
							return tab_datos_factura;
		}
	
	 	/**
		 * Este servicio retorna los datos de la factura para ser insertados en contabilidad.
		 * @param ide_facturas recibe los ides de las facturas para ser insertados en contabilidad 
		 * @param lugra_aplica recibe el parametro del lugar que aplica, ejemplo aplica debe, aplica_haber 
		 * @param ide_conac recibe el codigo del nombre del asiento a contabilizar 
		 * @param individual_grupal recibe 1 si es grupal 0 si es individual 
		 * @return retorna un string con el siguiente contenido: codigo de la factura, numero del asiento contable, nombre del asiento contable, ruc del proveedor, nombre del proveedor, numero de factura y los respectivos valores de la factura
		 */
	 /*public String getFacturasInsertaContabilidad(String ide_facturas,String lugar_aplica,String ide_conac,String individual_grupal){
		
		 String str_factura_contabilidad="";
				 if(individual_grupal.equals("1")){
					 str_factura_contabilidad +="select ide_cocac,ide_gelua,sum(valor_asiento) as valor_asiento,detalle_bogrm, 'De las Facturas ('||textcat_all(secuencial_fafac || ', ') ||')' as secuencial_fafac from ( ";
				 }

				 str_factura_contabilidad +="select ide_fafac,a.ide_bogrm,ide_cocac,ide_gelua,ide_coest,ide_conac,tipo_iva_coast,secuencial_fafac,base_aprobada_fafac,valor_iva_fafac,"
						 +" (case when tipo_iva_coast is true then valor_iva_fafac when base_imponible_coast is true then base_aprobada_fafac when valor_total_coast is true then total_fafac else 0 end) * (case when factor_menos_coast is true then -1 else 1 end) as valor_asiento,detalle_bogrm"
						 +" from ("
						 +" select ide_bogrm,ide_cocac,ide_gelua,ide_coest,ide_conac,tipo_iva_coast,activo_coast,base_imponible_coast,valor_total_coast,factor_menos_coast "
						 +" from cont_asiento_tipo where ide_gelua in (select cast(valor_para as integer) as valor from sis_parametros where nom_para ='"+lugar_aplica+"')"
						 +" and activo_coast = true"
						 +" ) a, ("
					     +" select a.ide_fafac,b.ide_bogrm,secuencial_fafac,base_aprobada_fafac as base_aprobada_fafac,valor_iva_fafac,(base_aprobada_fafac+valor_iva_fafac) as total_fafac,detalle_bogrm"
					  // +" select a.ide_fafac,b.ide_bogrm,secuencial_fafac,base_aprobada_fafac as base_aprobada_fafac,valor_iva_fafac, total_fafac ,detalle_bogrm"
						 +" from fac_factura a, fac_datos_factura b,  bodt_grupo_material c"
						 +" where a.ide_fadaf = b.ide_fadaf"
						 +" and b.ide_bogrm = c.ide_bogrm"
						 +" and a.ide_fafac in ("+ide_facturas+")"
						 +" ) b"
						 +" where a.ide_bogrm=b.ide_bogrm and ide_conac ="+ide_conac;
				 
				 if(individual_grupal.equals("1")){
					 str_factura_contabilidad +=" ) a group by ide_cocac,ide_gelua,detalle_bogrm";
				 }
				 System.out.println("getFacturasInsertaContabilidad ingrese a factura asientos "+str_factura_contabilidad);
		 return str_factura_contabilidad;
	 }*/
		public String getFacturasInsertaContabilidad(String ide_facturas,String lugar_aplica,String ide_conac,String individual_grupal, String ide_coest, String ide_facob){
			
			 String str_factura_contabilidad="";
					 if(individual_grupal.equals("1")){
						 str_factura_contabilidad +="select ide_cocac,ide_gelua,sum(case when valor_asiento=0 then valor_acta else valor_asiento end) as valor_asiento,detalle_bogrm, 'De las Facturas ('||textcat_all(secuencial_fafac || ', ') ||')' as secuencial_fafac from ( ";
					 }

					 if(ide_coest.contains("30")) // abonos
					 {
						 str_factura_contabilidad +=" select ide_fafac,a.ide_bogrm,ide_cocac,ide_gelua,ide_coest,ide_conac,tipo_iva_coast,secuencial_fafac,valor_cobro,valor_cobro_iva, "
								 +"  (case when tipo_iva_coast is true then valor_cobro_iva when base_imponible_coast is true then valor_cobro when valor_total_coast is true then abono else 0 end) * (case when factor_menos_coast is true then -1 else 1 end) as valor_asiento,valor_acta,detalle_bogrm" 
								 +"  from ( select ide_bogrm,ide_cocac,ide_gelua,ide_coest,ide_conac,tipo_iva_coast,activo_coast,base_imponible_coast,valor_total_coast,factor_menos_coast  from cont_asiento_tipo "
								 +" 	where ide_gelua in (select cast(valor_para as integer) as valor from sis_parametros where nom_para ='"+lugar_aplica+"') and activo_coast = true "
								 +"     ) a, "
								 +"  ( select a.ide_fafac,b.ide_bogrm,secuencial_fafac,base_aprobada_fafac as base_aprobada_fafac,valor_iva_fafac,(base_aprobada_fafac+valor_iva_fafac) as total_fafac,"
								 +"    coalesce(precio_total_acerp,0) as valor_acta, coalesce(valor_cobro_facob,0) as valor_cobro, coalesce(valor_cobro_iva_facob,0) as valor_cobro_iva, "
								 +"    coalesce(valor_cobro_facob,0) + coalesce(valor_cobro_iva_facob,0) as abono, detalle_bogrm "
								 +"    from fac_factura a "
								 +"    join fac_datos_factura b on b.ide_fadaf=a.ide_fadaf "
								 +"    join bodt_grupo_material c on c.ide_bogrm=b.ide_bogrm "
								 +"   join fac_cobro fc on fc.ide_fafac = a.ide_fafac  "
								 +"   left join fac_acta_entrega_recepcion d on d.ide_fafac=a.ide_fafac "
								 +"  where a.ide_fadaf = b.ide_fadaf and b.ide_bogrm = c.ide_bogrm and a.ide_fafac in ("+ide_facturas+") and ide_facob in ("+ide_facob+")"
								 +"  ) b "
								 +"   where a.ide_bogrm=b.ide_bogrm and ide_conac ="+ide_conac;	
					 
					 }
					 else
					 {
						 str_factura_contabilidad +="select ide_fafac,a.ide_bogrm,ide_cocac,ide_gelua,ide_coest,ide_conac,tipo_iva_coast,secuencial_fafac,base_aprobada_fafac,valor_iva_fafac,"
								 +" (case when tipo_iva_coast is true then valor_iva_fafac when base_imponible_coast is true then base_aprobada_fafac when valor_total_coast is true then total_fafac else 0 end) * (case when factor_menos_coast is true then -1 else 1 end) as valor_asiento,valor_acta,detalle_bogrm"
								 +" from ("
								 +" select ide_bogrm,ide_cocac,ide_gelua,ide_coest,ide_conac,tipo_iva_coast,activo_coast,base_imponible_coast,valor_total_coast,factor_menos_coast "
								 +" from cont_asiento_tipo where ide_gelua in (select cast(valor_para as integer) as valor from sis_parametros where nom_para ='"+lugar_aplica+"')"
								 +" and activo_coast = true"
								 +" ) a, ("
							     +" select a.ide_fafac,b.ide_bogrm,secuencial_fafac,base_aprobada_fafac as base_aprobada_fafac,valor_iva_fafac,(base_aprobada_fafac+valor_iva_fafac) as total_fafac,coalesce(precio_total_acerp,0) as valor_acta,detalle_bogrm"
								 +" from fac_factura a"
								 +" join fac_datos_factura b on b.ide_fadaf=a.ide_fadaf"
								 +" join bodt_grupo_material c on c.ide_bogrm=b.ide_bogrm"
								 +" left join fac_acta_entrega_recepcion d on d.ide_fafac=a.ide_fafac"
								 +" where a.ide_fadaf = b.ide_fadaf"
								 +" and b.ide_bogrm = c.ide_bogrm"
								 +" and a.ide_fafac in ("+ide_facturas+")"
								 +" ) b"
								 +" where a.ide_bogrm=b.ide_bogrm and ide_conac ="+ide_conac;
					 }
					 
					 if(individual_grupal.equals("1")){
						 str_factura_contabilidad +=" ) a group by ide_cocac,ide_gelua,detalle_bogrm";
					 }
					 System.out.println("getFacturasInsertaContabilidad ingrese a factura asientos "+str_factura_contabilidad);
			 return str_factura_contabilidad;
		 }
	 
	 	/**
			 * Este servicio retorna los datos de las notas de credito para ser insertados en contabilidad.
			 * @param ide_facturas recibe los ides de las facturas para ser insertados en contabilidad 
			 * @param lugra_aplica recibe el parametro del lugar que aplica, ejemplo aplica debe, aplica_haber 
			 * @param ide_conac recibe el codigo del nombre del asiento a contabilizar 
			 * @param individual_grupal recibe 1 si es grupal 0 si es individual 
			 * @return retorna un string con el siguiente contenido: codigo de la factura, numero del asiento contable, nombre del asiento contable, ruc del proveedor, nombre del proveedor, numero de factura y los respectivos valores de la factura
			 */
		 public String getFacturasInsertaContabilidadNotaDebito(String ide_fanod,String lugar_aplica,String ide_conac,String individual_grupal){
			
			 String str_factura_contabilidad="";
					 if(individual_grupal.equals("1")){
						 str_factura_contabilidad +="select ide_cocac,ide_gelua,sum(valor_asiento) as valor_asiento,sum(interes) as interes ,detalle_bogrm, 'De las Facturas ('||textcat_all(secuencial_fafac || ', ') ||')' as secuencial_fafac from ( ";
					 }
					 str_factura_contabilidad +="select ide_fafac,a.ide_bogrm,ide_cocac,ide_gelua,a.ide_coest,a.ide_conac,tipo_iva_coast,secuencial_fafac,base_aprobada_fafac,valor_iva_fafac,"
							 +" (case when tipo_iva_coast is true then valor_iva_fafac else base_aprobada_fafac end) as valor_asiento,detalle_bogrm,interes_generado_fanod as interes"
							 +" from ("
							 +"  select ide_bogrm,ide_cocac,ide_gelua,ide_coest,ide_conac,tipo_iva_coast,activo_coast"
							 +" from cont_asiento_tipo "
							 +" where ide_gelua in (select cast(valor_para as integer) as valor from sis_parametros where nom_para ='"+lugar_aplica+"')"
								+" and activo_coast = true"
									+"  ) a, ("
										+" 	 select a.ide_fafac,b.ide_comov,a.ide_bogrm,b.ide_coest,b.ide_conac,detalle_coest,detalle_conac,secuencial_fafac,ruc_comercial_recli,razon_social_recli,base_aprobada_fafac,"
										+" 	 valor_iva_fafac,total_fafac,factura_fisica_fafac,fecha_transaccion_fafac,detalle_bogrm,interes_generado_fanod,fecha_emision_fanod,a.ide_fanod,fecha_pago_fafac"
										+" 	 from ("
											+" 		 select c.ide_fafac,secuencial_fafac,a.ide_bogrm,ruc_comercial_recli,razon_social_recli,base_aprobada_fafac,valor_iva_fafac,total_fafac,factura_fisica_fafac,fecha_transaccion_fafac,detalle_bogrm,fecha_pago_fafac,interes_generado_fanod,fecha_emision_fanod,ide_fanod"
											+" 		 from fac_datos_factura a, bodt_grupo_material b,fac_factura c,rec_clientes d,(select a.ide_fanod,interes_generado_fanod,fecha_emision_fanod,ide_fafac"
											+" 				 from fac_nota_debito a, fac_detalle_debito b"
											+" 				 where a.ide_fanod=b.ide_fanod ) e "
											+" 				 where a.ide_bogrm = b.ide_bogrm and a.ide_fadaf=c.ide_fadaf and c.ide_recli = d.ide_recli and c.ide_fafac = e.ide_fafac"
											+" 		 ) a"
											+" 		 left join cont_nota_debito_asiento b on a.ide_fanod = b.ide_fanod"
											+" 		 left join cont_estado c on b.ide_coest = c.ide_coest "
											+" 		 left join cont_nombre_asiento_contable d on b.ide_conac = d.ide_conac"
											+"       where a.ide_fanod in ("+ide_fanod+")"
											+" 		 order by secuencial_fafac"
											+" ) b"
											+"  where a.ide_bogrm = b.ide_bogrm and a.ide_conac ="+ide_conac;
					 if(individual_grupal.equals("1")){
						 str_factura_contabilidad +=" ) a group by ide_cocac,ide_gelua,detalle_bogrm";
					 }
					 
			 System.out.println("getFacturasInsertaContabilidadNotaDebito: "+str_factura_contabilidad);
			 return str_factura_contabilidad;
		 }	 
		 	/**
			 * Este servicio retorna los datos de la factura para ser insertados en contabilidad.
			 * @param ide_facturas recibe los ides de las facturas para ser insertados en contabilidad 
			 * @param lugra_aplica recibe el parametro del lugar que aplica, ejemplo aplica debe, aplica_haber 
			 * @param ide_conac recibe el codigo del nombre del asiento a contabilizar 
			 * @param individual_grupal recibe 1 si es grupal 0 si es individual 
			 * @return retorna un string con el siguiente contenido: codigo de la factura, numero del asiento contable, nombre del asiento contable, ruc del proveedor, nombre del proveedor, numero de factura y los respectivos valores de la factura
			 */
		 public String getFacturasInsertaContabilidadNotaCredito(String ide_facturas,String lugar_aplica,String ide_conac,String individual_grupal){
			
			 String str_factura_contabilidad="";
					 if(individual_grupal.equals("1")){
						 str_factura_contabilidad +="select ide_cocac,ide_gelua,sum(valor_asiento) as valor_asiento,detalle_bogrm, 'De las Facturas ('||textcat_all(secuencial_fafac || ', ') ||')' as secuencial_fafac from ( ";
					 }
					 str_factura_contabilidad +="select ide_fafac,a.ide_bogrm,ide_cocac,ide_gelua,a.ide_coest,a.ide_conac,tipo_iva_coast,secuencial_fafac,base_aprobada_fafac,valor_iva_fafac,"
							 +" (case when tipo_iva_coast is true then valor_iva_fafac else base_aprobada_fafac end) as valor_asiento,detalle_bogrm"
							 +" from (				  "
								+" 	 select ide_bogrm,ide_cocac,ide_gelua,ide_coest,ide_conac,tipo_iva_coast,activo_coast"
								+" 	 from cont_asiento_tipo "
								+" 	 where ide_gelua in (select cast(valor_para as integer) as valor from sis_parametros where nom_para ='"+lugar_aplica+"')"
								+" 	 and activo_coast = true"
								+" 	 ) a, ("
									+" 		 select a.ide_fafac,b.ide_comov,b.ide_coest,ide_bogrm,b.ide_conac,detalle_coest,detalle_conac,secuencial_fafac,ruc_comercial_recli,razon_social_recli,base_aprobada_fafac,"
									+" 		 valor_iva_fafac,total_fafac,factura_fisica_fafac,fecha_transaccion_fafac,detalle_bogrm,fecha_fanoc,a.ide_fanoc,fecha_pago_fafac"
									+" 		 from ("
									+" 				 select c.ide_fafac,secuencial_fafac,a.ide_bogrm,ruc_comercial_recli,razon_social_recli,base_aprobada_fafac,valor_iva_fafac,total_fafac,factura_fisica_fafac,fecha_transaccion_fafac,detalle_bogrm,fecha_pago_fafac,fecha_fanoc,ide_fanoc"
									+" 				 from fac_datos_factura a, bodt_grupo_material b,fac_factura c,rec_clientes d,(select ide_fafac,fecha_fanoc,ide_fanoc from fac_nota_credito ) e "
									+" 				 where a.ide_bogrm = b.ide_bogrm and a.ide_fadaf=c.ide_fadaf and c.ide_recli = d.ide_recli and c.ide_fafac = e.ide_fafac "
									+" 				 ) a"
									+" 				 left join cont_factura_asiento b on a.ide_fafac = b.ide_fafac"
									+" 				 left join cont_estado c on b.ide_coest = c.ide_coest "
									+" 				 left join cont_nombre_asiento_contable d on b.ide_conac = d.ide_conac"
									+" 				 where a.ide_fafac in ("+ide_facturas+")"
									+" 		 order by secuencial_fafac"
									+" 		 ) b"
									+" 		 where a.ide_bogrm = b.ide_bogrm and a.ide_conac ="+ide_conac;
					 if(individual_grupal.equals("1")){
						 str_factura_contabilidad +=" ) a group by ide_cocac,ide_gelua,detalle_bogrm";
					 }
					 
			 return str_factura_contabilidad;
		 }
	 public TablaGenerica getTablaGenericaFacturasVencidas(String codigo){
		 
		 TablaGenerica tab_cabecera_factura=utilitario.consultar("select ide_fafac,ide_recli,fecha_transaccion_fafac,to_date(to_char(now(), 'YYYY/MM/DD'), 'YYYY/MM/DD')  - fecha_transaccion_fafac  as dias_emitido" 
				 +" from fac_factura where ide_coest = 2"
				 +" and to_date(to_char(now(), 'YYYY/MM/DD'), 'YYYY/MM/DD')  - fecha_transaccion_fafac >6 and ide_recli in ("+codigo+") "
				 +" order by ide_recli");
		 return tab_cabecera_factura;
		 
	 } 
	 public String getDatosBasicosClientes(String matrizSucursal ){
		    String tab_cliente="select distinct ruc_comercial_recli as ide_recli, ruc_comercial_recli, nombre_comercial_recli, razon_social_recli from rec_clientes"
		    				   +" ORDER BY ruc_comercial_recli;";
		    return tab_cliente;
		}
	 public String getDatosBasicosClientesLite(String matrizSucursal ){
		    String tab_cliente="select distinct ruc_comercial_recli as ide_recli, ruc_comercial_recli, razon_social_recli, coalesce(nombre_contacto_reclt, representante_legal_recli) as representante_legal_recli, estado_recli as publico, activo_recli as activo, direccion_recld"
								+" from rec_clientes cli "
								+" left join (select ide_recli,coalesce(barrio_recld || ', ','')  || coalesce(direccion_recld || ' ','') || coalesce(nmro_casa_lote_dpto_recld || ' ','') || coalesce(interseccion_recld,'') as direccion_recld" 
								+"            from rec_cliente_direccion where activo_recld=true and ide_recld in (select min(ide_recld) from rec_cliente_direccion"
								+"            where activo_recld=true group by ide_recli)) cliD on cliD.ide_recli=cli.ide_recli "
								+" left join rec_cliente_telefono ccli on ide_retco=1 and activo_reclt=true and ccli.ide_recli=cli.ide_recli "
								+" where coalesce(nro_establecimiento_recli,1)=1 and ruc_comercial_recli is not null"
								+" ORDER BY ruc_comercial_recli, ide_recli desc;";
		    return tab_cliente;
		}
	 public String getClientesDatosBasicos(String matrizSucursal ){
		    String tab_cliente="select a.ide_recli, (ruc_comercial_recli || ' - Est ' || cast(nro_establecimiento_recli as character(3))) as ruc_comercial_recli, razon_social_recli, establecimiento_operativo_recli,activo_recli from rec_clientes a " +
		           // " LEFT JOIN rec_cliente_direccion b on a.ide_recli=b.ide_recli where MATRIZ_SUCURSAL_RECLI in ("+matrizSucursal+")" +
		                " ORDER BY razon_social_recli,establecimiento_operativo_recli";
		    return tab_cliente;
		}
	 
	 public String getClientesDatosBasicos(){
		    String tab_cliente="select distinct ruc_comercial_recli as codigo, ruc_comercial_recli, razon_social_recli, establecimiento_operativo_recli from rec_clientes a ORDER BY razon_social_recli";
		    return tab_cliente;
		}
	 
	 /**Busca clientes con factura para validar documento de Pagos Banco Pacifico
		 * @param campo cedula del campo al que se realizara la busqueda
		 * @param valor factuta de la busqueda
		 * @return
		 */
		public TablaGenerica getDatosClienteFactura(String ide_factura){
			return utilitario.consultar("select orden,secuencial,fecha_transaccion_fafac, ruc_comercial_recli,rpad,base_aprobada_fafac, valor_iva_fafac, total_fafac,detalle_bogrm,doc_identidad,"
			+" repeat ('0',(15 - length (tot_sin_punto)))||tot_sin_punto as valor_sin_punto,repeat ('0',(15 - length (secuencial)))||secuencial as factura_sin_punto,repeat ('0',(13 - length (ruc_comercial_recli)))||ruc_comercial_recli as ruc_sin_punto,repeat ('0',(7 - length (nuevo_iva)))||nuevo_iva as iva_sin_punto"
			+" from ("
			+" SELECT row_number() over( order by secuencial_fafac) as orden,replace (secuencial_fafac,'-','') as secuencial,fecha_transaccion_fafac, ruc_comercial_recli,rpad(razon_social_recli,30,' ') as rpad,base_aprobada_fafac, valor_iva_fafac, total_fafac,"
			+" detalle_bogrm,replace(round(total_fafac,2)||'','.','') as tot_sin_punto, ( case when ide_gttdi = 1  then 'P' when ide_gttdi= 2 then 'R' when ide_gttdi= 3 then 'C' end) as doc_identidad,replace((round(valor_iva_fafac,2))||'','.','') as nuevo_iva "
			+" FROM fac_factura fac"
			+" join rec_clientes cli on cli.ide_recli=fac.ide_recli"
			+" join (select ide_fadaf, detalle_bogrm from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm) b on fac.ide_fadaf = b.ide_fadaf"
			+" where ide_fafac="+ide_factura 
			
			+" order by secuencial_fafac"
			+" ) a");
		}
		
		public String actualizarNumeroNotaDebito(String ide_fanod){
			String numeroNDelectronica="";
			String sql ="update fac_nota_debito set nro_nota_debito_fanod=case when (length(nro_nota_debito_fanod)<1 or nro_nota_debito_fanod is null) then "
			+"cast((select max(cast(nro_nota_debito_fanod as integer))+1 from fac_nota_debito) as character varying) else nro_nota_debito_fanod end "
			+", nro_nota_debito_elect_fanod=case when (length(nro_nota_debito_elect_fanod)<6 or nro_nota_debito_elect_fanod is null) and ide_coest=16 then "
			+" '001-011-' || lpad(''||(SELECT max(cast(coalesce(substring(nro_nota_debito_elect_fanod from 9 for length(nro_nota_debito_elect_fanod)), '0') as integer))+1 FROM fac_nota_debito),9,'0') else "
			+" nro_nota_debito_elect_fanod end "
			+"where ide_fanod="+ide_fanod+";";
			
			utilitario.getConexion().ejecutarSql(sql);
			
			List list_sql1 = utilitario.getConexion().consultar("SELECT nro_nota_debito_elect_fanod FROM fac_nota_debito where ide_fanod="+ide_fanod+" limit 1");	
			if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
				numeroNDelectronica=String.valueOf(list_sql1.get(0));
			}
			
			//System.out.println("actualizarNumeroNotaDebitoELECT: "+numeroNDelectronica);
			
			return numeroNDelectronica;
		}
		
		public void actualizarNumeroNotaCredito(String ide_fanoc){
			String sql ="update fac_nota_credito set nro_nota_credito_fanoc='001-011-' || "
						+ " lpad(''||(select MAX(cast(substring(nro_nota_credito_fanoc from 11 for 7) as integer))+1  FROM fac_nota_credito),9,'0')"
						+ " where ide_fanoc="+ide_fanoc+" and (length(nro_nota_credito_fanoc)<1 or nro_nota_credito_fanoc is null);";
			
			utilitario.getConexion().ejecutarSql(sql);
			System.out.println("actualizarNumeroNotaCredito: "+sql);
		}
		
		public int obtenerDiasPlazo(String ide_recli){
			int numero_dias=0; 
			String sql = "select coalesce(dias_geplf,0) as dias from rec_clientes cli " +
	                     "left join gen_plazo_facturas pf on pf.ide_geplf=cli.ide_geplf " +
	                     "where ide_recli="+ide_recli;
			

			List list_sql1 = utilitario.getConexion().consultar(sql);	
			if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
				numero_dias=pckUtilidades.CConversion.CInt(list_sql1.get(0));
			}
			
			if(numero_dias==0)
				numero_dias=pckUtilidades.CConversion.CInt(utilitario.getVariable("p_dias_calculo_interes_mora_nd"));
			
			System.out.println("dias: "+numero_dias);
			
			return numero_dias;
		}
		
		public String getComprobantesContabilizados(String grupos,String filtro,String fecha_inicial,String fecha_final, String tipoComp, String aplica_fecha){
			 
			 String tab_comprobantes="SELECT ROW_NUMBER() OVER (ORDER BY tipo_documento,fecha_emision) AS codigo,tipo_documento,sucursal,ruc,cliente,fecha_emision,fecha_pago,fecha_conciliado,secuencial,autorizada_sri,estado,neto,iva,valor_cobrado,valor_cobrado_iva,neto_contabilizado_emision,iva_contabilizado_emision,neto_contabilizado_recaudado,iva_contabilizado_recaudado "
			 		+ " from (";
			 
			 String tab_facturas="SELECT distinct cast('FACTURA' as character(25)) as tipo_documento,detalle_bogrm as sucursal,cli.ruc_comercial_recli as ruc, cli.razon_social_recli as cliente, fecha_transaccion_fafac as fecha_emision,fecha_pago_fafac as fecha_pago, fecha_conciliado_fafac as fecha_conciliado, secuencial_fafac as secuencial, "
					 +" autorizada_sri_fafac as autorizada_sri, c.detalle_coest as estado, base_aprobada_fafac as neto, valor_iva_fafac as iva, coalesce((case when valor_cobro<=0 then (case when fac.ide_coest=16 then base_aprobada_fafac else 0 end) else valor_cobro end),(case when fac.ide_coest=16 then base_aprobada_fafac else 0 end)) as valor_cobrado, coalesce((case when valor_cobro_iva<=0 then (case when fac.ide_coest=16 then valor_iva_fafac else 0 end) else valor_cobro_iva end),(case when fac.ide_coest=16 then valor_iva_fafac else 0 end)) as valor_cobrado_iva"
					 +" ,coalesce(neto_contabilizado_emision,0) as neto_contabilizado_emision, coalesce(iva_contabilizado_emision,0) as iva_contabilizado_emision, coalesce(neto_contabilizado,0) as neto_contabilizado_recaudado, coalesce(iva_contabilizado,0) as iva_contabilizado_recaudado"
					 
					 +" FROM fac_factura fac "
					 +" left join rec_clientes cli on cli.ide_recli=fac.ide_recli "
					 +" left join fac_datos_factura facd on facd.ide_fadaf=fac.ide_fadaf "
					 +" left join bodt_grupo_material bgm on bgm.ide_bogrm = facd.ide_bogrm "
					 +" left join cont_estado c on c.ide_coest = fac.ide_coest  "
					 +" left join (select ide_fafac,sum(coalesce(valor_cobro_facob,0)) as valor_cobro, sum(coalesce(valor_cobro_iva_facob,0)) as valor_cobro_iva from fac_cobro group by ide_fafac) fc on fc.ide_fafac = fac.ide_fafac "
					
					+" left join (select fac.ide_fafac, sum(base_aprobada_fafac) as neto_contabilizado_emision, sum(valor_iva_fafac) as iva_contabilizado_emision "
					+"            FROM cont_factura_asiento cfa "
					+" 	   join fac_factura fac on fac.ide_fafac=cfa.ide_fafac  "
					+" 	   where cfa.ide_coest=2 and cfa.ide_conac in (select ide_conac from cont_nombre_asiento_contable where ide_gemod in (1))"
					+" 	   group by fac.ide_fafac"
					+"           ) fasie on fasie.ide_fafac=fac.ide_fafac "
					
					+" left join (select fac.ide_fafac, sum(coalesce(valor_cobro_facob,base_aprobada_fafac)) as neto_contabilizado, sum(coalesce(valor_cobro_iva_facob,valor_iva_fafac)) as iva_contabilizado "
					+"            FROM cont_factura_asiento cfa "
					+" 	         join fac_factura fac on fac.ide_fafac=cfa.ide_fafac  "
					+"           left join fac_cobro fc on fc.ide_facob = cfa.ide_facob "
					+" 	   where cfa.ide_coest in (16,30,24)"
					+" 	   group by fac.ide_fafac"
					+"          ) fasir on fasir.ide_fafac=fac.ide_fafac ";
					
			 if(aplica_fecha.equals("true"))
				 tab_facturas+="  where fecha_pago_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' ";
			 else
				 tab_facturas+="  where fecha_transaccion_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' ";

			 
			 if(pckUtilidades.CConversion.CInt(filtro)>0)
			 {
				 tab_facturas+=" and bgm.ide_bogrm in ("+filtro+") ";
			 }

			String tab_notaCredito = "SELECT distinct cast('NOTA CREDITO' as character(25)) as tipo_documento,detalle_bogrm as sucursal,cli.ruc_comercial_recli as ruc, cli.razon_social_recli as cliente, fecha_fanoc as fecha_emision,cast('1900-01-01' as date) as fecha_pago, cast('1900-01-01' as date) as fecha_conciliado, nro_nota_credito_fanoc as secuencial, "
					+" autorizada_sri_fanoc as autorizada_sri,c.detalle_coest as estado, valor_referencial_fanoc as neto, iva_fanoc as iva, 0 as valor_cobrado, 0 as valor_cobrado_iva"
					+" ,coalesce(neto_contabilizado_emision,0) as neto_contabilizado_emision, coalesce(iva_contabilizado_emision,0) as iva_contabilizado_emision, 0 as neto_contabilizado_recaudado, 0 as iva_contabilizado_recaudado"
 
					+" FROM fac_nota_credito nc"
					+" join fac_factura fac on fac.ide_fafac=nc.ide_fafac"
					+" left join rec_clientes cli on cli.ide_recli=fac.ide_recli "
					+" left join fac_datos_factura facd on facd.ide_fadaf=fac.ide_fadaf "
					+" left join bodt_grupo_material bgm on bgm.ide_bogrm = facd.ide_bogrm "
					+" left join cont_estado c on c.ide_coest = nc.ide_coest  "
					
					+" left join (select nc.ide_fanoc, sum(valor_referencial_fanoc) as neto_contabilizado_emision, sum(iva_fanoc) as iva_contabilizado_emision "
					+"            FROM cont_factura_asiento cfa "
					+" 	   join fac_nota_credito nc on nc.ide_fafac=cfa.ide_fafac  "
					+" 	   where cfa.ide_coest=2 and cfa.ide_conac in (select ide_conac from cont_nombre_asiento_contable where ide_gemod in (34))"
					+" 	   group by nc.ide_fanoc"
					+"           ) fasie on fasie.ide_fanoc=nc.ide_fanoc"
					+" where fecha_fanoc between '"+fecha_inicial+"' and '"+fecha_final+"' ";
			
			//if(aplica_fecha.equals("true"))
			//	tab_notaCredito+=" where fecha_fanoc between '"+fecha_inicial+"' and '"+fecha_final+"' ";
				
			if(pckUtilidades.CConversion.CInt(filtro)>0)
			{
				tab_notaCredito+=" and bgm.ide_bogrm in ("+filtro+") ";
			}

			String tab_notaDebito = "SELECT distinct cast('NOTA DEBITO' as character(25)) as tipo_documento,detalle_bogrm as sucursal,cli.ruc_comercial_recli as ruc, cli.razon_social_recli as cliente, fecha_emision_fanod as fecha_emision,cast('1900-01-01' as date) as fecha_pago, cast('1900-01-01' as date) as fecha_conciliado, nro_nota_debito_elect_fanod as secuencial," 
					+" autorizada_sri_fanod as autorizada_sri,c.detalle_coest as estado, interes_generado_fanod as neto, 0 as iva, coalesce(valor_cobro,(case when nd.ide_coest=16 then interes_generado_fanod else 0 end)) as valor_cobrado, 0 as valor_cobrado_iva"
					//+" ,coalesce(neto_contabilizado_emision,0) as neto_contabilizado_emision, coalesce(iva_contabilizado_emision,0) as iva_contabilizado_emision, coalesce(neto_contabilizado_recaudado,0) as neto_contabilizado_recaudado, coalesce(iva_contabilizado_recaudado,0) as iva_contabilizado_recaudado"
					+" ,coalesce(neto_contabilizado_recaudado,0) as neto_contabilizado_emision, coalesce(iva_contabilizado_recaudado,0) as iva_contabilizado_emision, coalesce(neto_contabilizado_recaudado,0) as neto_contabilizado_recaudado, coalesce(iva_contabilizado_recaudado,0) as iva_contabilizado_recaudado"
 
					+" FROM fac_nota_debito nd"
					+" join fac_detalle_debito dnd on dnd.ide_fanod=nd.ide_fanod"
					+" join fac_factura fac on fac.ide_fafac=dnd.ide_fafac"
					+" left join rec_clientes cli on cli.ide_recli=nd.ide_recli "
					+" left join fac_datos_factura facd on facd.ide_fadaf=fac.ide_fadaf "
					+" left join bodt_grupo_material bgm on bgm.ide_bogrm = facd.ide_bogrm "
					+" left join cont_estado c on c.ide_coest = nd.ide_coest  "
					+" left join (select ide_fanod,sum(coalesce(valor_cobro_interes_facob,0)) as valor_cobro, 0 as valor_cobro_iva from fac_cobro group by ide_fanod) fc on fc.ide_fanod = nd.ide_fanod" 
					
					+" left join (select nd.ide_fanod, sum(interes_generado_fanod) as neto_contabilizado_emision, 0 as iva_contabilizado_emision "
					+"            FROM cont_nota_debito_asiento nda "
					+" 	   join fac_nota_debito nd on nd.ide_fanod=nda.ide_fanod  "
					+" 	   where nda.ide_coest=2"
					+" 	   group by nd.ide_fanod"
					+"           ) fasie on fasie.ide_fanod=nd.ide_fanod"
					
					+" left join (select nd.ide_fanod, sum(coalesce(valor_cobro_interes_facob,interes_generado_fanod)) as neto_contabilizado_recaudado, 0 as iva_contabilizado_recaudado "
					+"            FROM cont_nota_debito_asiento nda "
					+" 	   join fac_nota_debito nd on nd.ide_fanod=nda.ide_fanod  "
					+"            left join fac_cobro fc on fc.ide_fanod = nda.ide_fanod "
					+" 	   where nda.ide_coest in (16)"
					+" 	   group by nd.ide_fanod"
					+"           ) fasir on fasir.ide_fanod=nd.ide_fanod "
					+" where fecha_emision_fanod between '"+fecha_inicial+"' and '"+fecha_final+"' ";
			
			//if(aplica_fecha.equals("true"))
			//	tab_notaDebito+=" where fecha_emision_fanod between '"+fecha_inicial+"' and '"+fecha_final+"' "; 
						 		
			if(pckUtilidades.CConversion.CInt(filtro)>0)
			 {
				tab_notaDebito+=" and bgm.ide_bogrm in ("+filtro+") ";
			 }
				
				
			/////////////////////
		
			if(tipoComp.equals("0")){ //todos
				tab_comprobantes += tab_facturas +" union all  "+tab_notaCredito +" union all  "+tab_notaDebito;
			}
			
			if(tipoComp.equals("1")){ //facturas
				tab_comprobantes += tab_facturas;
			}
			
			if(tipoComp.equals("2")){ //notas credito
				tab_comprobantes += tab_notaCredito;
			}
			
			
			if(tipoComp.equals("3")){ //notas debito
				tab_comprobantes += tab_notaDebito;
			}
		
			
			tab_comprobantes+=" ) a ";		
				
	 		if(grupos.equals("0")){
	 			tab_comprobantes+=" limit 0 ";		
	 		}
	 		else
	 			tab_comprobantes+=" order by tipo_documento,fecha_emision ";
	 		
			 
		 System.out.println("getComprobantesContabilizados "+tab_comprobantes);
		 return tab_comprobantes;
	 }
		
		public String getComprobantesContabilizados_detallado(String grupos,String filtro,String ide_comov,String fecha_inicial,String fecha_final, String tipoComp, String aplica_fecha){
			 
			 String tab_comprobantes="SELECT ROW_NUMBER() OVER (ORDER BY tipo_documento,mov_fecha_comov) AS codigo,tipo_documento,sucursal,ide_comov,mov_fecha_comov,detalle_conac,ruc,cliente,fecha_emision,secuencial,fecha_pago,estado,neto,iva,neto_contabilizado,(iva_contabilizado+neto_contabilizado) as valor_contabilizado, "
			 		+ " observacion, nro_comprobante_cobro, autorizada_sri, debe, codigo_cuenta_debe, cuenta_debe, haber, codigo_cuenta_haber, cuenta_haber "
			 		+ " from (";
			 
			 String tab_facturas=" SELECT distinct cast('FACTURA' as character(25)) as tipo_documento,detalle_bogrm as sucursal,cfa.ide_comov, mov_fecha_comov, conac.detalle_conac, cli.ruc_comercial_recli as ruc, cli.razon_social_recli as cliente, fecha_transaccion_fafac as fecha_emision, secuencial_fafac as secuencial,  "
								+" coalesce(fecha_cobro_facob,fecha_pago_fafac) as fecha_pago, c.detalle_coest as estado, base_aprobada_fafac as neto, valor_iva_fafac as iva, coalesce(valor_cobro_facob,base_aprobada_fafac) as neto_contabilizado, coalesce(valor_cobro_iva_facob,valor_iva_fafac) as iva_contabilizado, "
								+" coalesce(documento_bancario_fafac,' ') || ' - ' || coalesce(observacion_fafac,' ') as observacion, coalesce(cast(nro_documento_facob as character(25)),documento_conciliado_fafac) as nro_comprobante_cobro, autorizada_sri_fafac as autorizada_sri,"
								+" debe, codigo_cuenta_debe, cuenta_debe, haber, codigo_cuenta_haber, cuenta_haber "
								+" FROM cont_factura_asiento cfa "
								+" join fac_factura fac on fac.ide_fafac=cfa.ide_fafac  "
								+" left join rec_clientes cli on cli.ide_recli=fac.ide_recli "
								+" left join fac_datos_factura facd on facd.ide_fadaf=fac.ide_fadaf "
								+" left join bodt_grupo_material bgm on bgm.ide_bogrm = facd.ide_bogrm "
								+" left join cont_estado c on c.ide_coest = fac.ide_coest  "
								+" left join fac_cobro fc on fc.ide_facob = cfa.ide_facob "
								+" left join cont_movimiento mov on mov.ide_comov=cfa.ide_comov "
								+" left join cont_nombre_asiento_contable conac on conac.ide_conac=mov.ide_conac "
								+" left join (select ide_comov,debe, '| ' || textcat_all(codigo_cuenta_debe || ' | ') as codigo_cuenta_debe, '| ' || textcat_all(cuenta_debe || ' | ') as cuenta_debe "
								+"            from (select dmov.ide_comov,cast((case when dmov.ide_gelua=1 then 'DEBE' else '' end) as character(25)) as debe,"
								+"             cue_codigo_cocac as codigo_cuenta_debe, cue_descripcion_cocac as cuenta_debe"
								+"             from cont_detalle_movimiento dmov  "
								+"             left join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac"
								+"             where dmov.ide_gelua=1"
								+"            ) a group by ide_comov,debe"
								+"           ) ddmov on ddmov.ide_comov=mov.ide_comov "
								+" left join (select ide_comov,haber, '| ' || textcat_all(codigo_cuenta_haber || ' | ') as codigo_cuenta_haber, '| ' || textcat_all(cuenta_haber || ' | ') as cuenta_haber "
								+"            from (select dmov.ide_comov,cast((case when dmov.ide_gelua=2 then 'HABER' else '' end) as character(25)) as haber,"
								+" 	     cue_codigo_cocac as codigo_cuenta_haber, cue_descripcion_cocac as cuenta_haber"
								+"             from cont_detalle_movimiento dmov  "
								+" 	     left join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac"
								+"             where dmov.ide_gelua=2"
								+"             ) a group by ide_comov,haber"
								+"           ) hdmov on hdmov.ide_comov=mov.ide_comov ";

								//+" where coalesce(mov_fecha_comov,fecha_transaccion_fafac) between '"+fecha_inicial+"' and '"+fecha_final+"' ";
								
			 
								 if(pckUtilidades.CConversion.CInt(ide_comov)>0 ){
									 tab_facturas+=" where mov.ide_comov = "+ide_comov;		
							 		}
								 else
								 {
									 if(aplica_fecha.equals("true"))
										 tab_facturas+="  where coalesce(fecha_cobro_facob,fecha_pago_fafac) between '"+fecha_inicial+"' and '"+fecha_final+"' ";
									 else
										 tab_facturas+="  where fecha_transaccion_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' ";
									 //tab_facturas+=" where fecha_transaccion_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' ";
								 }
			 
			 if(pckUtilidades.CConversion.CInt(filtro)>0)
			 {
				 tab_facturas+=" and bgm.ide_bogrm in ("+filtro+") ";
			 }

			String tab_notaCredito = "SELECT distinct cast('NOTA CREDITO' as character(25)) as tipo_documento,detalle_bogrm as sucursal,cfa.ide_comov, mov_fecha_comov, conac.detalle_conac, cli.ruc_comercial_recli as ruc, cli.razon_social_recli as cliente, fecha_fanoc as fecha_emision, nro_nota_credito_fanoc as secuencial,   "
					+" cast('1900-01-01' as date) as fecha_pago, c.detalle_coest as estado,valor_referencial_fanoc as neto, iva_fanoc as iva, "
					+" valor_referencial_fanoc as neto_contabilizado, iva_fanoc as iva_contabilizado, detalle_fanoc as observacion, '' as nro_comprobante_cobro , autorizada_sri_fanoc as autorizada_sri, "
					+" debe, codigo_cuenta_debe, cuenta_debe, haber, codigo_cuenta_haber, cuenta_haber  "
					+" FROM cont_factura_asiento cfa  "
					+" join fac_nota_credito nc on nc.ide_fafac=cfa.ide_fafac "
					+" left join cont_estado c on c.ide_coest = nc.ide_coest   "
				    +" join fac_factura fac on fac.ide_fafac=nc.ide_fafac   "
					+" left join rec_clientes cli on cli.ide_recli=fac.ide_recli  "
					+" left join fac_datos_factura facd on facd.ide_fadaf=fac.ide_fadaf  "
					+" left join bodt_grupo_material bgm on bgm.ide_bogrm = facd.ide_bogrm  "
					+" left join cont_movimiento mov on mov.ide_comov=cfa.ide_comov  "
					+" left join cont_nombre_asiento_contable conac on conac.ide_conac=mov.ide_conac " 
					+" left join (select ide_comov,debe, '| ' || textcat_all(codigo_cuenta_debe || ' | ') as codigo_cuenta_debe, '| ' || textcat_all(cuenta_debe || ' | ') as cuenta_debe "
					+"            from (select dmov.ide_comov,cast((case when dmov.ide_gelua=1 then 'DEBE' else '' end) as character(25)) as debe,"
					+"             cue_codigo_cocac as codigo_cuenta_debe, cue_descripcion_cocac as cuenta_debe"
					+"             from cont_detalle_movimiento dmov  "
					+"             left join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac"
					+"             where dmov.ide_gelua=1"
					+"            ) a group by ide_comov,debe"
					+"           ) ddmov on ddmov.ide_comov=mov.ide_comov "
					+" left join (select ide_comov,haber, '| ' || textcat_all(codigo_cuenta_haber || ' | ') as codigo_cuenta_haber, '| ' || textcat_all(cuenta_haber || ' | ') as cuenta_haber "
					+"            from (select dmov.ide_comov,cast((case when dmov.ide_gelua=2 then 'HABER' else '' end) as character(25)) as haber,"
					+" 	     cue_codigo_cocac as codigo_cuenta_haber, cue_descripcion_cocac as cuenta_haber"
					+"             from cont_detalle_movimiento dmov  "
					+" 	     left join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac"
					+"             where dmov.ide_gelua=2"
					+"             ) a group by ide_comov,haber"
					+"           ) hdmov on hdmov.ide_comov=mov.ide_comov";
					//+" where coalesce(mov_fecha_comov,fecha_fanoc) between '"+fecha_inicial+"' and '"+fecha_final+"' and cfa.ide_conac in (select ide_conac from cont_nombre_asiento_contable where ide_gemod in (34))  ";
					//+" where fecha_fanoc between '"+fecha_inicial+"' and '"+fecha_final+"' and cfa.ide_conac in (select ide_conac from cont_nombre_asiento_contable where ide_gemod in (34))  ";
					if(aplica_fecha.equals("true"))
						tab_notaCredito+=" where fecha_fanoc between '"+fecha_inicial+"' and '"+fecha_final+"' ";
				
			if(pckUtilidades.CConversion.CInt(filtro)>0)
			{
				tab_notaCredito+=" and bgm.ide_bogrm in ("+filtro+") ";
			}

			String tab_notaDebito = "SELECT distinct cast('NOTA DEBITO' as character(25)) as tipo_documento,detalle_bogrm as sucursal,nda.ide_comov, mov_fecha_comov, conac.detalle_conac, cli.ruc_comercial_recli as ruc, cli.razon_social_recli as cliente, fecha_emision_fanod as fecha_emision, nro_nota_debito_elect_fanod as secuencial,   "
					+" fecha_emision_fanod as fecha_pago, c.detalle_coest as estado,interes_generado_fanod as neto, 0 as iva," 
					+" coalesce(interes_generado_fanod) as neto_contabilizado, 0 as iva_contabilizado, ide_transaccion_fanod as observacion, documento_cobro_fanod as nro_comprobante_cobro , autorizada_sri_fanod as autorizada_sri, "
					+" debe, codigo_cuenta_debe, cuenta_debe, haber, codigo_cuenta_haber, cuenta_haber "
					+" FROM cont_nota_debito_asiento nda  "
					+" join fac_nota_debito nd on nd.ide_fanod=nda.ide_fanod "
					+" left join cont_estado c on c.ide_coest = nd.ide_coest   "
					+" join fac_detalle_debito dnd on dnd.ide_fanod=nd.ide_fanod "
					+" join fac_factura fac on fac.ide_fafac=dnd.ide_fafac  "
					+" left join rec_clientes cli on cli.ide_recli=nd.ide_recli  "
					+" left join fac_datos_factura facd on facd.ide_fadaf=fac.ide_fadaf  "
					+" left join bodt_grupo_material bgm on bgm.ide_bogrm = facd.ide_bogrm  "
					+" left join cont_movimiento mov on mov.ide_comov=nda.ide_comov  "
					+" left join cont_nombre_asiento_contable conac on conac.ide_conac=mov.ide_conac  "
					+" left join (select ide_comov,debe, '| ' || textcat_all(codigo_cuenta_debe || ' | ') as codigo_cuenta_debe, '| ' || textcat_all(cuenta_debe || ' | ') as cuenta_debe "
					+"            from (select dmov.ide_comov,cast((case when dmov.ide_gelua=1 then 'DEBE' else '' end) as character(25)) as debe,"
					+"             cue_codigo_cocac as codigo_cuenta_debe, cue_descripcion_cocac as cuenta_debe"
					+"             from cont_detalle_movimiento dmov  "
					+"             left join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac"
					+"             where dmov.ide_gelua=1"
					+"            ) a group by ide_comov,debe"
					+"           ) ddmov on ddmov.ide_comov=mov.ide_comov "
					+" left join (select ide_comov,haber, '| ' || textcat_all(codigo_cuenta_haber || ' | ') as codigo_cuenta_haber, '| ' || textcat_all(cuenta_haber || ' | ') as cuenta_haber "
					+"            from (select dmov.ide_comov,cast((case when dmov.ide_gelua=2 then 'HABER' else '' end) as character(25)) as haber,"
					+" 	     cue_codigo_cocac as codigo_cuenta_haber, cue_descripcion_cocac as cuenta_haber"
					+"             from cont_detalle_movimiento dmov  "
					+" 	     left join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac"
					+"             where dmov.ide_gelua=2"
					+"             ) a group by ide_comov,haber"
					+"           ) hdmov on hdmov.ide_comov=mov.ide_comov";
					//+" where coalesce(mov_fecha_comov,fecha_emision_fanod) between '"+fecha_inicial+"' and '"+fecha_final+"' ";
					//+" where fecha_emision_fanod between '"+fecha_inicial+"' and '"+fecha_final+"' ";
			
			if(aplica_fecha.equals("true"))
				tab_notaDebito+=" where fecha_emision_fanod between '"+fecha_inicial+"' and '"+fecha_final+"' "; 
						 		
			if(pckUtilidades.CConversion.CInt(filtro)>0)
			 {
				tab_notaDebito+=" and bgm.ide_bogrm in ("+filtro+") ";
			 }
				
				
			/////////////////////
		
			if(tipoComp.equals("0")){ //todos
				tab_comprobantes += tab_facturas +" union all  "+tab_notaCredito+" union all  "+tab_notaDebito;
			}
			
			if(tipoComp.equals("1")){ //facturas
				tab_comprobantes += tab_facturas;
			}
			
			if(tipoComp.equals("2")){ //notas credito
				tab_comprobantes += tab_notaCredito;
			}
			
			if(tipoComp.equals("3")){ //notas debito
				tab_comprobantes += tab_notaDebito;
			}
		
			
			tab_comprobantes+=" ) a ";	
			
			if(pckUtilidades.CConversion.CInt(ide_comov)>0 ){
	 			tab_comprobantes+=" where ide_comov = "+ide_comov;		
	 		}
				
	 		if(grupos.equals("0")){
	 			tab_comprobantes+=" limit 0 ";		
	 		}
	 		else
	 			tab_comprobantes+=" order by tipo_documento,mov_fecha_comov ";
	 		
			 
		 System.out.println("getComprobantesContabilizados_detallado "+tab_comprobantes);
		 return tab_comprobantes;
	 }
		
	public String getDeudaCliente(int sucursal, int anio, String ruc)
	{
		String sql="select ruc_comercial_recli, sum(total) as total from ( ";
		
		sql+=getComprobantesContabilizados(sucursal, anio);
		
		sql+=" ) a where ruc_comercial_recli like '"+ruc+"' and (current_date - date(fecha_vencimiento_fafac))>80 "
				+ " group by ruc_comercial_recli ";
		
		System.out.println("getDeudaCliente "+sql);
		
		return sql;
	}
		
	public String getComprobantesContabilizados(int sucursal, int anio)
	{
		
		/*String sql="select detalle_bogrm,detalle_coest,secuencial_fafac,ruc_comercial_recli,razon_social_recli,coalesce(total_fafac,0)-coalesce(valor_cobro,0) as saldo," +
			" coalesce(interes_generado_fanod,0) as interes," +
			" (coalesce(total_fafac,0)-coalesce(valor_cobro,0)) + coalesce(interes_generado_fanod,0) as total," +
			" fecha_transaccion_fafac,fecha_vencimiento_fafac, current_date - date(fecha_vencimiento_fafac) as dias_retraso" +
			" from rec_clientes cli" +
			" left join fac_factura fac on fac.ide_recli=cli.ide_recli" +
			" left join ( select nd.ide_fanod,nd.ide_recli,dnd.ide_fafac,detalle_fenod,interes_generado_fanod,fecha_emision_fanod" +
			"   from fac_nota_debito nd, fac_detalle_debito dnd" +
			"   where nd.ide_fanod=dnd.ide_fanod and ide_coest = (select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido')" +
			"   ) c on c.ide_fafac=fac.ide_fafac and c.ide_recli=fac.ide_recli" +
			" left join (select ide_fafac, sum(coalesce(valor_cobro_facob,0)+coalesce(valor_cobro_iva_facob,0)) as valor_cobro from fac_cobro group by ide_fafac) d on d.ide_fafac=fac.ide_fafac" +
			" left join cont_estado est on est.ide_coest=fac.ide_coest" +
			" left join (select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, detalle_bogrm from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm order by autorizacion_sri_bogrm ) b on b.ide_fadaf = fac.ide_fadaf" +
			" where fac.ide_coest not in (1,16) ";*/
		
		String sql="select row_number() over(order by detalle_bogrm,secuencial_fafac) as codigo,extract(year from fecha_transaccion_fafac) as anio, CASE when cli.estado_recli = true THEN 'PUBLICO' else 'PRIVADO' END AS TIPO, "
				+ " ruc_comercial_recli,razon_social_recli,detalle_bogrm as servicio_facturado,secuencial_fafac,detalle_coest as estado, "
				+ " coalesce(total_fafac,0)-coalesce(valor_cobro,0) as saldo, "
				+ " coalesce(interes_generado_fanod,0) as interes, "
				+ " (coalesce(total_fafac,0)-coalesce(valor_cobro,0)) + coalesce(interes_generado_fanod,0) as total, "
				+ " current_date - date(fecha_vencimiento_fafac) as dias_retraso, "
				+ " "
				+ " case  "
				+ " when ((coalesce(total_fafac,0)-coalesce(valor_cobro,0)) + coalesce(interes_generado_fanod,0))>=10001 then 'Categoría A) Mayor a $10.001' "
				+ " when ((coalesce(total_fafac,0)-coalesce(valor_cobro,0)) + coalesce(interes_generado_fanod,0)) between 5001 and 10000 then 'Categoría B) Entre $5.001 a $10.000' "
				+ " when ((coalesce(total_fafac,0)-coalesce(valor_cobro,0)) + coalesce(interes_generado_fanod,0)) between 500 and 5000 then 'Categoría C) Entre $500 a $5.000' "
				+ " when ((coalesce(total_fafac,0)-coalesce(valor_cobro,0)) + coalesce(interes_generado_fanod,0)) < 500 then 'Categoría D) Menores a $500' "
				+ " end as categoria, "
				+ " "
				+ " email_recle as correo_electronico,  "
				+ " nombre_contacto,coalesce(telefono_factura_recli,'') as telefono_factura,direccion_recld, "
				+ " fecha_transaccion_fafac,fecha_vencimiento_fafac "
				+ " from rec_clientes cli "
				+ " left join (select ide_recli,(case when barrio_recld='SN' then '' else coalesce(barrio_recld||' ','') end)    "
				+ "		|| coalesce(direccion_recld,'')     "
				+ "		|| (case when nmro_casa_lote_dpto_recld='SN' then '' else coalesce(' '||nmro_casa_lote_dpto_recld,'') end)   "
				+ "		|| (case when interseccion_recld='SN' then '' else coalesce(' y '||interseccion_recld,'') end)    "
				+ "		as direccion_recld from rec_cliente_direccion where activo_recld=true and ide_recld in (select min(ide_recld) from rec_cliente_direccion     "
				+ "	where activo_recld=true group by ide_recli)) dir on dir.ide_recli=cli.ide_recli  "
				+ " "
				+ " left join (select ide_recli, string_agg(email_recle, ',') as email_recle from rec_cliente_email where notificacion_recle=true and activo_recle=true group by ide_recli ) em on em.ide_recli=cli.ide_recli    "
				+ " left join (select ide_recli, string_agg(nombre_contacto_reclt, ',') ||' - '|| string_agg(telefono_reclt, ',') as nombre_contacto from rec_cliente_telefono where notificacion_reclt=true and activo_reclt=true group by ide_recli ) ct on ct.ide_recli=cli.ide_recli    "
				+ " "
				+ " left join fac_factura fac on fac.ide_recli=cli.ide_recli "
				+ " left join ( select nd.ide_fanod,nd.ide_recli,dnd.ide_fafac,detalle_fenod,interes_generado_fanod,fecha_emision_fanod "
				+ "   from fac_nota_debito nd, fac_detalle_debito dnd "
				+ "   where nd.ide_fanod=dnd.ide_fanod and ide_coest = (select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido') "
				+ "   ) c on c.ide_fafac=fac.ide_fafac and c.ide_recli=fac.ide_recli "
				+ " left join (select ide_fafac, sum(coalesce(valor_cobro_facob,0)+coalesce(valor_cobro_iva_facob,0)) as valor_cobro from fac_cobro group by ide_fafac) d on d.ide_fafac=fac.ide_fafac "
				+ " left join cont_estado est on est.ide_coest=fac.ide_coest "
				+ " left join (select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, detalle_bogrm from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm order by autorizacion_sri_bogrm ) b on b.ide_fadaf = fac.ide_fadaf "
				+ " where fac.ide_coest not in (1,16)  ";
			
		if(anio>0)
			sql+=" and extract(year from fecha_transaccion_fafac)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+anio+")";
		
		if(anio<0)
			sql+=" and cli.ide_recli=-1";
		
		if(sucursal>0)
			sql+=" and fac.ide_fadaf="+sucursal;
		sql+=" order by detalle_bogrm,secuencial_fafac";
		
		System.out.println("getComprobantesContabilizados por cobrar "+sql);
		return sql;
	}
				
		
}
