package paq_tesoreria.ejb;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import framework.aplicacion.TablaGenerica;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Utilitario;

/**
 * Session Bean implementation class ServicioTesoreria
 */
@Stateless
@LocalBean
public class ServicioTesoreria {
	private Utilitario utilitario = new Utilitario();

	@EJB
	private ServicioPresupuesto serv_presupuesto;

	public TablaGenerica getTablaGenericaImpuesto(String activo){
		TablaGenerica tab_impuesto =utilitario.consultar("select a.ide_teimp,detalle_teimp,codigo_teimp,porcentaje_teimp,detalle_tetii " +
				" from tes_impuesto a,tes_tipo_impuesto b where a.ide_tetii= b.ide_tetii" +
				" and a.ide_tetii in ("+activo+") order by codigo_teimp");

		return tab_impuesto;
		
	}
public String getImpuesto (String activo,String grupo,String tipo_impuesto){
		
		String tab_impuesto="select a.ide_teimp,codigo_teimp,porcentaje_teimp,detalle_teimp,detalle_tetii " +
				" from tes_impuesto a,tes_tipo_impuesto b where a.ide_tetii= b.ide_tetii" +
				" and activo_teimp in ("+activo+") ";
				if(grupo.equals("0")){
					tab_impuesto+=" and a.ide_tetii in ("+tipo_impuesto+")";		
				}
				    tab_impuesto+=" order by codigo_teimp";

		return tab_impuesto;
		
	}
public String getImpuestoCalculo (String codigo){
	
	String tab_impuesto="select a.ide_teimp,codigo_teimp,porcentaje_teimp,detalle_teimp,detalle_tetii " +
			" from tes_impuesto a,tes_tipo_impuesto b where a.ide_tetii= b.ide_tetii" +
			" and  a.ide_teimp in ("+codigo+")  order by codigo_teimp";

	return tab_impuesto;
	
}
public String getConsultaRetencion(String comprobante){

	String tab_impuesto="select a.ide_teder,base_imponible_teder,valor_retenido_teder,ide_cocac,ide_gelua,aplica_formula_teast,formula_teast,"
			+" (case when aplica_formula_teast =true then 0 else base_imponible_teder end) as resultado"
			 +" from tes_detalle_retencion a,tes_asiento_tipo b, tes_comprobante_pago c,tes_retencion d,adq_factura e"
			+" where a.ide_teimp =b.ide_teimp"
			+" and a.ide_teret = d.ide_teret"
			 +" and d.ide_adfac = e.ide_adfac"
			 +" and c.ide_adsoc = e.ide_adsoc"
			+" and c.ide_tecpo in ("+comprobante+")";

	return tab_impuesto;
	
}
public String getConsultaTipoConcepto(String activo){
	
	String tab_impuesto="select ide_tetic,(case when codigo_tetic is null then 'S/D' else codigo_tetic end) ||' '||detalle_tetic as detalle from tes_tipo_concepto  where activo_tetic in ("+activo+") order by codigo_tetic";

	return tab_impuesto;
	
}

/*public String getConsultaPagos(){
	
	String tab_pagos="select ruc_tepro, ruc_tepro as ruc, row_number()over(order by ruc_tepro) as referencia,nombre_tepro,codigo_banco_geins,numero_cuenta_tepcb,"
			+" codigo_gttcb,valor_pago_tecpo,codigo_tetic,detalle_tecpo"
			+" from tes_comprobante_pago a"
			+" left join tes_proveedor b on a.ide_tepro=b.ide_tepro"
			+" left join tes_proveedor_cuenta_bancaria c on b.ide_tepro=c.ide_tepro"
			+" left join gen_institucion d on c.ide_geins = d.ide_geins"
			+" left join gth_tipo_cuenta_bancaria e on c.ide_gttcb = e.ide_gttcb"
			+" left join tes_tipo_concepto f on a.ide_tetic = f.ide_tetic where 1=-1"
			+" order by ruc_tepro";

	return tab_pagos;
	
}*/

public String getActualizarConsultaPagos(boolean consulta, String fechaI,String fechaF, String cuenta_transferencia){
	
	String tab_pagos="select distinct referencia as reff, referencia, comprobante_egreso, ruc, nombre_tepro, codigo_banco_geins, numero_cuenta_tepcb, codigo_gttcb, valor_transf as valor_pago_tecpo, codigo_tetic, detalle_tecpo, ide_tecpo from (";
	 tab_pagos+="select row_number()over(order by ruc_tepro) as referencia, comprobante_egreso_tecpo as comprobante_egreso, "
			+" (case when a.ide_geedp is null then prov.ruc_tepro else emp.ruc_emp end) as ruc, "
			+" (case when a.ide_geedp is null then prov.nombre_tepro else emp.NOMBRES end) as nombre_tepro, "
			+" (case when a.ide_geedp is null then prov.codigo_banco_geins else emp.CODIGO_BANCO_GEINS end) as codigo_banco_geins,"
			+" (case when a.ide_geedp is null then prov.numero_cuenta_tepcb else emp.NUMERO_CUENTA_GTCBE end) as numero_cuenta_tepcb,"
			+" (case when a.ide_geedp is null then prov.codigo_gttcb else emp.CODIGO_GTTCB end) as codigo_gttcb,"
			+" valor_pago_tecpo,dmov.valor_transf,codigo_tetic,substring(detalle_tecpo from 1 for 100) as detalle_tecpo, a.ide_tecpo "
			+" from tes_comprobante_pago a"
			+" left join tes_tipo_concepto f on a.ide_tetic = f.ide_tetic"
			+" left join cont_movimiento mov on mov.ide_tecpo=a.ide_tecpo"
			+" left join (select ide_comov, sum(haber_codem) as valor_transf from cont_detalle_movimiento where ide_cocac in ("+cuenta_transferencia+") group by ide_comov) dmov on dmov.ide_comov=mov.ide_comov"
			
			+" left join (select b.ide_tepro, coalesce(ruc_representante_tepro,ruc_tepro) as ruc_tepro,substring(nombre_tepro from 1 for 30) as nombre_tepro, codigo_banco_geins, numero_cuenta_tepcb,codigo_gttcb"
			+" 	 from tes_proveedor b"
			+" 	 left join tes_proveedor_cuenta_bancaria c on b.ide_tepro=c.ide_tepro"
			+" 	 left join gen_institucion d on c.ide_geins = d.ide_geins"
			+" 	 left join gth_tipo_cuenta_bancaria e on c.ide_gttcb = e.ide_gttcb) prov on prov.ide_tepro=a.ide_tepro"
				 
			+" left join (select par.ide_geedp,substring (APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || PRIMER_NOMBRE_GTEMP || ' ' "
			+" 	|| (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP  end)  from 1 for 30) AS NOMBRES,"
			+" 	CODIGO_GTTCB, DOCUMENTO_IDENTIDAD_GTEMP as ruc_emp,NUMERO_CUENTA_GTCBE,CODIGO_BANCO_GEINS "
			+" 	from GEN_EMPLEADOS_DEPARTAMENTO_PAR par "
			+" 	left join GTH_EMPLEADO emp on par.ide_gtemp=emp.ide_gtemp "
			+" 	left join GTH_CUENTA_BANCARIA_EMPLEADO cuenta on cuenta.IDE_GTEMP=emp.IDE_GTEMP  and ACREDITACION_GTCBE=true "
			+" 	left join GEN_INSTITUCION insti on insti.IDE_GEINS=cuenta.IDE_GEINS  "
			+" 	left join GTH_TIPO_CUENTA_BANCARIA tipoc on cuenta.IDE_GTTCB=tipoc.IDE_GTTCB ) emp on emp.ide_geedp=a.ide_geedp ";
				
	if(consulta)
		tab_pagos +=" where fecha_pago_tecpo between '"+fechaI+"' and '"+fechaF+"'";
	else
		tab_pagos +=" where 1=-1";
	
	tab_pagos +=" ) a ";
	tab_pagos +=" order by ruc";

	System.out.println("tab_pagos: "+tab_pagos);
	return tab_pagos;
	
}
public String getConsultaRetencionCalcula(String comprobante,String formula){
	
	String tab_impuesto="select a.ide_teder,base_imponible_teder,valor_retenido_teder,ide_cocac,ide_gelua,aplica_formula_teast,formula_teast,"
			+" (case when aplica_formula_teast =true then "+formula+" else base_imponible_teder end) as resultado"
			+" from tes_detalle_retencion a,tes_asiento_tipo b, tes_comprobante_pago c,tes_retencion d,adq_factura e"
			+" where a.ide_teimp =b.ide_teimp"
			+" and a.ide_teret = d.ide_teret"
			+" and d.ide_adfac = e.ide_adfac"
			+" and c.ide_adsoc = e.ide_adsoc"
			+" and c.ide_tecpo in ("+comprobante+")";

	return tab_impuesto;
	
}

public String getConsulValorPagoContabilidad(String movimiento){
	
	String tab_impuesto=" select ide_comov,sum(haber_codem) as valor from cont_detalle_movimiento where transferencia_codem = true and ide_comov="+movimiento+" group by ide_comov";

	return tab_impuesto;
	
}

public String getSqlDeudaClientes (String codigo){
	
//	String tab_cliente="select row_number() over(order by ide_recli,grupo,ide_fafac) as codigo,ide_fafac,ide_recli,detalle_bogrm,secuencial_fafac,fecha_transaccion_fafac,total_fafac,grupo,interes"
//			+" from ("
//			+" select ide_fafac,ide_recli,detalle_bogrm,secuencial_fafac,total_fafac,1 as grupo, 0 as interes,fecha_transaccion_fafac"
//			+" from ("
//			+" select ide_fafac,ide_fadaf,secuencial_fafac,total_fafac,ide_recli,ide_coest,fecha_transaccion_fafac from fac_factura"
//			+" ) a , ("
//			+" select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, detalle_bogrm" 
//			+" from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm" 
//			+" order by autorizacion_sri_bogrm"
//			+" ) b"
//			+" where a.ide_fadaf = b.ide_fadaf"
//			+" and ide_coest = (select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido')"
//			+" union"
//			+" select ide_fanod,ide_recli,'NOTA DE DEBITO',detalle_fenod,total_fanod,2 as grupo,interes_generado_fanod,fecha_emision_fanod"
//			+" from fac_nota_debito where ide_coest = (select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido')"
//			+" ) a"
//			+" where ide_recli ="+codigo
//			+" order by ide_recli,grupo,ide_fafac";

	/*String tab_cliente=" select row_number() over(order by secuencial_fafac,detalle_bogrm) as codigo,ide_fafac,ide_recli,substring(detalle_bogrm from 1 for 35) as detalle_bogrm,secuencial_fafac,fecha_transaccion_fafac,total_fafac,grupo,interes "
			+" from ( "
			+" select ide_fafac,ide_recli,detalle_bogrm,secuencial_fafac,total_fafac,1 as grupo, 0 as interes,fecha_transaccion_fafac " 
			 +" from ( select ide_fafac,ide_fadaf,secuencial_fafac,total_fafac,f.ide_recli,ide_coest,fecha_transaccion_fafac ,ruc_comercial_recli from fac_factura f, rec_clientes c where f.ide_recli=c.ide_recli ) a , " 
			 +" ( select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, 'FACTURA: ' || detalle_bogrm as detalle_bogrm "
			 +" from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm  "
			 +" order by autorizacion_sri_bogrm ) b "
			 +" where a.ide_fadaf = b.ide_fadaf and ruc_comercial_recli='"+codigo+ "'" 
			 +" and ide_coest in ((select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido'),24) " 
			+" union "
			 +" select ide_fanod,nd.ide_recli,'NOTA DE DEBITO',detalle_fenod,0,2 as grupo,interes_generado_fanod,fecha_emision_fanod " 
			 +" from fac_nota_debito nd, rec_clientes c where nd.ide_recli=c.ide_recli and ide_coest = (select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido') and ruc_comercial_recli='"+codigo+ "'" 
			+" ) a " 
			+" order by secuencial_fafac,detalle_bogrm";*/
	
	String tab_cliente=" select row_number() over(order by secuencial_fafac,detalle_bogrm) as codigo,ide_fafac,ide_recli,substring(detalle_bogrm from 1 for 35) as detalle_bogrm,secuencial_fafac,fecha_transaccion_fafac,total_fafac,grupo,interes,establecimiento" 
			+"  from ( "
			 +" select ide_fafac,ide_recli,detalle_bogrm,secuencial_fafac,total_fafac,1 as grupo, 0 as interes,fecha_transaccion_fafac ,establecimiento "
			  +" from ( select ide_fafac,ide_fadaf,secuencial_fafac,total_fafac,f.ide_recli,ide_coest,fecha_transaccion_fafac ,ruc_comercial_recli,establecimiento_operativo_recli as establecimiento from fac_factura f, rec_clientes c where f.ide_recli=c.ide_recli ) a ,"
			 
			  +" ( select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, 'FACTURA: ' || detalle_bogrm as detalle_bogrm "
			  +" from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm  "
			  +" order by autorizacion_sri_bogrm ) b "
			  +" where a.ide_fadaf = b.ide_fadaf and ruc_comercial_recli='"+codigo+ "'"
			  +" and ide_coest in ((select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido'),24)"  
			 +" union "
			  +" select ide_fanod,nd.ide_recli,'NOTA DE DEBITO',detalle_fenod,0,2 as grupo,interes_generado_fanod,fecha_emision_fanod, establecimiento_operativo_recli as establecimiento"  
			  +" from fac_nota_debito nd, rec_clientes c where nd.ide_recli=c.ide_recli and ide_coest = (select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido') and ruc_comercial_recli='"+codigo+ "'"
			 +" ) a  "
			+"  order by secuencial_fafac,detalle_bogrm";
	
	/*String tab_cliente=" select row_number() over(order by secuencial_fafac,detalle_bogrm) as codigo,ide_fafac,ide_recli,substring(detalle_bogrm from 1 for 35) as detalle_bogrm,secuencial_fafac,fecha_transaccion_fafac,total_fafac,grupo,interes,establecimiento" 
			+"  from ( "
			 +" select ide_fafac,ide_recli,detalle_bogrm,secuencial_fafac,total_fafac,1 as grupo, 0 as interes,fecha_transaccion_fafac ,establecimiento "
			  +" from ( select ide_fafac,ide_fadaf,secuencial_fafac,total_fafac,f.ide_recli,ide_coest,fecha_transaccion_fafac ,ruc_comercial_recli,establecimiento_operativo_recli as establecimiento from fac_factura f, rec_clientes c where f.ide_recli=c.ide_recli ) a ,"
			 
			  +" ( select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, 'FACTURA: ' || detalle_bogrm as detalle_bogrm "
			  +" from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm  "
			  +" order by autorizacion_sri_bogrm ) b "
			  +" where a.ide_fadaf = b.ide_fadaf and ruc_comercial_recli='"+codigo+ "'"
			  +" and ide_coest in ((select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido'),24)"  
			+" union "
				+" select ide_fafac,ide_recli,detalle_bogrm,acta_secuencial_acerp,precio_total_acerp,1 as grupo, 0 as interes,fecha_emision_acerp ,establecimiento "
				+"  from ( select f.ide_fafac,ide_fadaf,acta_secuencial_acerp,precio_total_acerp,f.ide_recli,aer.ide_coest,fecha_emision_acerp ,ruc_comercial_recli,establecimiento_operativo_recli as establecimiento "
				+" from fac_factura f "
				+" join rec_clientes c on c.ide_recli=f.ide_recli"
				+" join fac_acta_entrega_recepcion aer on aer.ide_fafac=f.ide_fafac  ) a ,"
			   +" ( select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, 'ACTA E-R PET: ' || detalle_bogrm as detalle_bogrm "
			   +"  from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm  "
			   +" order by autorizacion_sri_bogrm ) b "
			   +" where a.ide_fadaf = b.ide_fadaf and ruc_comercial_recli='"+codigo+ "'"
			   +" and ide_coest in ((select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido'),24)"
			 +" union "
			  +" select ide_fanod,nd.ide_recli,'NOTA DE DEBITO',detalle_fenod,0,2 as grupo,interes_generado_fanod,fecha_emision_fanod, establecimiento_operativo_recli as establecimiento"  
			  +" from fac_nota_debito nd, rec_clientes c where nd.ide_recli=c.ide_recli and ide_coest = (select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido') and ruc_comercial_recli='"+codigo+ "'"
			 +" ) a  "
			+"  order by secuencial_fafac,detalle_bogrm";
	*/
	return tab_cliente;
	
}

public String getSqlEstadoCuentaCliente (String ruc){
	String tab_cliente= "select ruc_comercial_recli,coalesce(razon_social,razon_social_recli) as razon_social,coalesce(direccion,' ') as direccion,coalesce(email,' ') as email, sum((coalesce(total_fafac,0)-coalesce(valor_cobro,0))+coalesce(interes_generado_fanod,0)) as valor  " +
                   
                    "from rec_clientes cli  " +
                
                    "left join (select distinct ruc_comercial_recli as ruc, razon_social_recli as razon_social, direccion_recld as direccion,email  " +
                    "           from rec_clientes cli  " +
                    "           left join (select ide_recli,direccion_recld || ' ' || coalesce(barrio_recld,'') || ' ' || coalesce(interseccion_recld,'') as direccion_recld   " +
                    "	              from rec_cliente_direccion where activo_recld=true and ide_recld in (select max(ide_recld) from rec_cliente_direccion group by ide_recli)) dir on dir.ide_recli=cli.ide_recli  " +
                    "	   left join (select ide_recli, string_agg(email_recle, ',') as email from rec_cliente_email   " +
                    "                       where notificacion_recle=true and activo_recle=true group by ide_recli ) c on c.ide_recli=cli.ide_recli   " +
                    "	   where cli.ide_recli in (select max(ide_recli) from rec_clientes group by ruc_comercial_recli)) dat on dat.ruc=cli.ruc_comercial_recli  " +
                   
                    "left join fac_factura fac on fac.ide_recli=cli.ide_recli  " +
                 
                    "left join ( select nd.ide_fanod,nd.ide_recli,dnd.ide_fafac,detalle_fenod,interes_generado_fanod,fecha_emision_fanod   " +
                    "   from fac_nota_debito nd, fac_detalle_debito dnd   " +
                    "   where nd.ide_fanod=dnd.ide_fanod and ide_coest = (select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido')   " +
                    "   ) c on c.ide_fafac=fac.ide_fafac and c.ide_recli=fac.ide_recli  " +
                   
                    "left join (select ide_fafac, sum(coalesce(valor_cobro_facob,0)+coalesce(valor_cobro_iva_facob,0)) as valor_cobro from fac_cobro group by ide_fafac) d on d.ide_fafac=fac.ide_fafac  " +
                   
                    "where fac.ide_coest in ((select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido'),24,30)  " +
                    "and ((coalesce(total_fafac,0)-coalesce(valor_cobro,0))+coalesce(interes_generado_fanod,0))>0    " +
                    "and ruc_comercial_recli like '"+ruc+"' " +
                   
                    "group by ruc_comercial_recli,coalesce(razon_social,razon_social_recli),direccion,email  " +
                    "order by ruc_comercial_recli ";
	
	System.out.println("getSqlEstadoCuentaCliente "+tab_cliente);
	return tab_cliente;
}

public String getSqlDeudaClientesAbonos (String codigo){
	
	String tab_cliente=" select row_number() over(order by fecha_transaccion_fafac) as codigo,ide_fafac,ide_recli,ide_fanod, ruc_comercial_recli ,0 as ide_prcon, substring(detalle_bogrm from 1 for 30) as detalle_bogrm,secuencial_fafac,"
			+ " fecha_transaccion_fafac,total_fafac,(case when (total_fafac-valor_cobro)>0 then (total_fafac-valor_cobro-total_fanoc) else 0 end) as valor, iva_cobrado as valor_iva,interes" 
			+ " ,valor_cobro as abonada, establecimiento from ( "
			+ " select fac.ide_fafac,fac.ide_recli,fac.ide_coest,coalesce(ide_fanod,0) as ide_fanod,detalle_bogrm,secuencial_fafac,coalesce(valor_cobro,0) as valor_cobro, "
			+ " case when fac.ide_coest=1 or fac.ide_coest=16 then 0 else total_fafac+coalesce(faer.precio_total_acerp,0) end as total_fafac,  "
			+ " case when fac.ide_coest=1 or fac.ide_coest=16 then 0 else (case when coalesce(iva_cobrado_fafac,false)=false then valor_iva_fafac else 0 end) end as iva_cobrado "
			+ " ,coalesce(interes_generado_fanod,0) as interes,fecha_transaccion_fafac, ruc_comercial_recli, establecimiento_operativo_recli as establecimiento" 
			+ " ,case when valor_cobro is null then false else true end as abonada"
			+ " ,coalesce(total_fanoc,0) as total_fanoc "
			+ " from fac_factura fac"
			
			+" left join (select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, detalle_bogrm as detalle_bogrm" 
			+"   from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm  "
			+"   order by autorizacion_sri_bogrm ) a on a.ide_fadaf = fac.ide_fadaf "
			+" left join rec_clientes cli on cli.ide_recli=fac.ide_recli"
			
			+" left join ( select nd.ide_fanod,nd.ide_recli,dnd.ide_fafac,detalle_fenod,interes_generado_fanod,fecha_emision_fanod" 
			+"   from fac_nota_debito nd, fac_detalle_debito dnd "
			+"   where nd.ide_fanod=dnd.ide_fanod and ide_coest = (select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido')" 
			+"   ) c on c.ide_fafac=fac.ide_fafac and c.ide_recli=fac.ide_recli"
			
			+" left join (select ide_fafac, sum(coalesce(valor_cobro_facob,0)+coalesce(valor_cobro_iva_facob,0)) as valor_cobro from fac_cobro group by ide_fafac) d on d.ide_fafac=fac.ide_fafac"
			+" left join fac_acta_entrega_recepcion faer on faer.ide_fafac=fac.ide_fafac"
			+" left join (SELECT ide_fafac,sum(valor_referencial_fanoc) as valor_referencial_fanoc, sum(iva_fanoc) as iva_fanoc, sum(total_fanoc) as total_fanoc FROM public.fac_nota_credito nc join fac_detalle_ncredito ncd on ncd.ide_fanoc=nc.ide_fanoc where activo_fanoc=true group by ide_fafac) nc on nc.ide_fafac=fac.ide_fafac "
			
			+" where fac.ide_coest in ((select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido'),24,30,1,16) " //and coalesce(autorizada_sri_fafac,false)=true "
			
			+"  ) a  "
			
			//+" where (abs(total_fafac)+abs(interes)+abs(iva_cobrado)+abs(total_fafac-valor_cobro))>0 and (total_fafac-valor_cobro)>=0 "
			+" where ((abs(total_fafac)+abs(interes)+abs(iva_cobrado)+abs(total_fafac-valor_cobro-total_fanoc))>0 and (total_fafac-valor_cobro-total_fanoc)>=0 or interes>0) "
			+ " and ruc_comercial_recli='"+codigo+ "'" 
		    +"  order by fecha_transaccion_fafac;";
	
	System.out.println("getSqlDeudaClientesAbonos "+tab_cliente);
	return tab_cliente;
	
}

public String getSqlDeudaClientesContratos (String codigo){
	
	String tab_cliente=" select row_number() over(order by fecha_inicio_prcon) as codigo,0 as ide_fafac,ide_recli,0 as ide_fanod,'"+codigo+ "' as ruc_comercial_recli ,pc.ide_prcon,substring(observacion_prcon from 1 for 30) as detalle_bogrm,"
			+" numero_contrato_prcon as secuencial_fafac,fecha_inicio_prcon as fecha_transaccion_fafac,0 as total_fafac,(coalesce(monto_prcon,0)-coalesce(valor_cobro,0)) as valor,0 as valor_iva,0 as interes, coalesce(valor_cobro,0) as abonada,'' as establecimiento  "
			+" from pre_contrato pc"
			
			 +" left join (select ide_prcon, sum(valor_cobro_facob) as valor_cobro from fac_cobro group by ide_prcon) d on d.ide_prcon = pc.ide_prcon"
			
			 +" where ide_coest in ((select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido'),24,30) and ide_recli in (select ide_recli from rec_clientes where ruc_comercial_recli like '"+codigo+ "')"
			+" order by fecha_inicio_prcon;";
	
	System.out.println("getSqlDeudaClientesContratos "+tab_cliente);
	return tab_cliente;
	
}

public String getSqlDeudaClientesArchivo (String codigo){
	
	String tab_cliente=" select row_number() over(order by ruc_comercial_recli) as codigo,tipo_id, ruc_comercial_recli as doc_identidad,razon_social_recli, substring(detalle_bogrm from 1 for 30) as detalle_bogrm,sum(total_fafac-valor_cobro) as valor, sum(interes) as interes " 
			+" from ( "
			+" select fac.ide_fafac,fac.ide_recli,fac.ide_coest,coalesce(ide_fanod,0) as ide_fanod,detalle_bogrm,secuencial_fafac,coalesce(valor_cobro,0) as valor_cobro, "
			+" case when fac.ide_coest=1 or fac.ide_coest=16 then 0 else total_fafac+coalesce(faer.precio_total_acerp,0) end as total_fafac,  "
			+" case when fac.ide_coest=1 or fac.ide_coest=16 then 0 else (case when coalesce(iva_cobrado_fafac,false)=false then valor_iva_fafac else 0 end) end as iva_cobrado "
			+" ,coalesce(interes_generado_fanod,0) as interes,fecha_transaccion_fafac,substring(detalle_gttdi from 1 for 1) as tipo_id, ruc_comercial_recli, establecimiento_operativo_recli as establecimiento, razon_social_recli "
			+" ,case when valor_cobro is null then false else true end as abonada "
			 
			+" from fac_factura fac"
			
			+" left join (select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, detalle_bogrm as detalle_bogrm "
			+"   from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm  "
			+"   order by autorizacion_sri_bogrm ) a on a.ide_fadaf = fac.ide_fadaf "
			+" left join rec_clientes cli on cli.ide_recli=fac.ide_recli"
			+" left join gth_tipo_documento_identidad tdi on tdi.ide_gttdi = cli.ide_gttdi"
			
			+" left join ( select nd.ide_fanod,nd.ide_recli,dnd.ide_fafac,detalle_fenod,interes_generado_fanod,fecha_emision_fanod "
			+"   from fac_nota_debito nd, fac_detalle_debito dnd "
			+"   where nd.ide_fanod=dnd.ide_fanod and ide_coest = (select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido') "
			+"   ) c on c.ide_fafac=fac.ide_fafac and c.ide_recli=fac.ide_recli"
			
			+" left join (select ide_fafac, sum(coalesce(valor_cobro_facob,0)+coalesce(valor_cobro_iva_facob,0)) as valor_cobro from fac_cobro group by ide_fafac) d on d.ide_fafac=fac.ide_fafac"
			+" left join fac_acta_entrega_recepcion faer on faer.ide_fafac=fac.ide_fafac"
			
			+" where fac.ide_coest in ((select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido'),24,30,1,16)  "
			
			+"  ) a  "
			
			+" where (abs(interes)+abs(total_fafac-valor_cobro))>0 and (total_fafac-valor_cobro)>=0 and ruc_comercial_recli not like '%9999999999%' "
			+"  group by tipo_id,ruc_comercial_recli, razon_social_recli,detalle_bogrm"
			+"  order by ruc_comercial_recli";
	
		if(codigo.equals("-1"))
			tab_cliente+=" limit 0 ";
	
	System.out.println("getSqlDeudaClientesArchivo "+tab_cliente);
	return tab_cliente;
	
}

//public String getSqlClienteCobro(String ruc, String id_transaccion){
	public String getSqlClienteCobro(String fecha_recaudacion){
	
		/*String tab_cliente_cobro=" select cli.ruc_comercial_recli, coalesce(sum(total_fafac),0) as facturas, coalesce(sum(interes),0) as intereses from fac_factura fac"
		+ " join rec_clientes cli on cli.ide_recli=fac.ide_recli"
		+ " left join (select ruc_comercial_recli, detalle_fenod, sum(interes_generado_fanod) as interes from fac_nota_debito nd "
		+ " 	join rec_clientes cli on cli.ide_recli=nd.ide_recli"
		+ " 	where ruc_comercial_recli like '"+ruc+"' and ide_transaccion_fanod like '"+id_transaccion+"'"
		+ " 	group by ruc_comercial_recli,detalle_fenod) nd on nd.ruc_comercial_recli=cli.ruc_comercial_recli and nd.detalle_fenod=fac.secuencial_fafac"
		+ " where cli.ruc_comercial_recli like '"+ruc+"' and documento_conciliado_fafac like '"+id_transaccion+"'"
		+ " group by cli.ruc_comercial_recli";*/
		
		/*String tab_cliente_cobro="select distinct cli.ruc_comercial_recli,documento_conciliado_fafac, coalesce(sum(total_fafac),0) as facturas, coalesce(sum(interes),0) as intereses from fac_factura fac"
				+ " join rec_clientes cli on cli.ide_recli=fac.ide_recli"
				+ " left join (select ruc_comercial_recli, detalle_fenod,ide_transaccion_fanod, sum(interes_generado_fanod) as interes from fac_nota_debito nd "
				+ " 	join rec_clientes cli on cli.ide_recli=nd.ide_recli"
				+ " 	where ide_transaccion_fanod is not null and fecha_emision_fanod = '"+fecha_recaudacion+"'"
				+ " 	group by ruc_comercial_recli,detalle_fenod,ide_transaccion_fanod) nd on nd.ruc_comercial_recli=cli.ruc_comercial_recli and nd.detalle_fenod=fac.secuencial_fafac"
				+ " where documento_conciliado_fafac is not null and UPPER(documento_bancario_fafac) like '%PACIFICO%'"
				+ "  and fecha_pago_fafac = '"+fecha_recaudacion+"'"
				+ " group by cli.ruc_comercial_recli,documento_conciliado_fafac";*/
		String tab_cliente_cobro="select caja, forma_cobro, sum(cobro) as valor_recaudado, ruc_comercial,razon_social,idTran_cod_aut from ( ";
		
		tab_cliente_cobro+=getReporteCaja("1",fecha_recaudacion,fecha_recaudacion,"","");
		
		tab_cliente_cobro+=" )b where UPPER(caja) like '%PACIFICO%' "
							+" group by caja, forma_cobro, ruc_comercial,razon_social,forma_cobro,idTran_cod_aut"
							+" order by ruc_comercial";
				
		//System.out.println("getSqlClienteCobro "+tab_cliente_cobro);
		return tab_cliente_cobro;
	
	}
	
	public String getSqlPagosDiferidosPacifico(){
		
		String sql="SELECT ide_faaup, ide_usua, fecha_pago_archivo_faaup, fecha_concilia_faaup, "
				     +"  ruc_cliente_faaup, cliente_faaup, agencia_faaup, valor_registrado_faaup, "
				     +"  valor_archivo_faaup, id_transaccion_faaup, documento_conciliado_faaup, "
				     +"  observacion_faaup, deposito_diferido_faaup, conciliado_faaup, "
				     +"  validado_faaup, activo_faaup"
				 +" FROM fac_auditoria_pagos "
				 +" where deposito_diferido_faaup=true and conciliado_faaup=false and activo_faaup=true";
		//System.out.println("getSqlPagosDiferidosPacifico "+sql);
		return sql;
	}
	
	public String getSqlClienteCobroProdubanco(String fecha_recaudacion, String fecha_recaudacionFin){
		
		String tab_cliente_cobro="select caja, forma_cobro, sum(cobro) as valor_recaudado, ruc_comercial,razon_social,idTran_cod_aut from ( ";
		
		tab_cliente_cobro+=getReporteCaja("1",fecha_recaudacion,fecha_recaudacionFin,"","");
		
		tab_cliente_cobro+=" )b where UPPER(caja) like '%PRODUBANCO%' "
							+" group by caja, forma_cobro, ruc_comercial,razon_social,forma_cobro,idTran_cod_aut"
							+" order by ruc_comercial";
				
		//System.out.println("getSqlClienteCobro "+tab_cliente_cobro);
		return tab_cliente_cobro;
	
	}

public String getSqlTitulosCredito(String codigo){
	
	String tab_titulo="SELECT ide_facon, fecha_titulo_facon,nro_titulo_facon, cli.ruc_comercial_recli, cli.nombre_comercial_recli, cli.razon_social_recli, est.detalle_coest as estado  "
			 +" FROM fac_convenio con "
			 +" left join cont_estado est on est.ide_coest=con.ide_coest "
			 +" left join rec_clientes cli on cli.ide_recli=con.ide_recli "
			 +" where activo_facon=true and titulo_credito_facon=true and ide_geani = "+codigo;			    
	System.out.println("tab_titulo "+tab_titulo);
	return tab_titulo;
	
}

public String getSqlTitulosCreditoCoactiva(String ide_geani){
	
	String tab_titulo="SELECT ide_facon, fecha_titulo_facon,nro_titulo_facon, cli.ruc_comercial_recli, cli.razon_social_recli,descripcion_facon as descripcion,monto_convenio_facon, est.detalle_coest as estado,cli.nombre_comercial_recli  "
			 +" FROM fac_convenio con "
			 +" left join cont_estado est on est.ide_coest=con.ide_coest "
			 +" left join rec_clientes cli on cli.ide_recli=con.ide_recli "
			 +" where titulo_credito_facon=true and con.ide_coest in (2,35) ";
	if (!ide_geani.equals("-1"))
		tab_titulo += " and con.ide_geani="+ide_geani;
	
	tab_titulo += " order by 2 desc ";		
	
	System.out.println("tab_titulo "+tab_titulo);
	return tab_titulo;
	
}

public String getSqlTitulosCredito(String fecha_inicial,String fecha_final){
	
	String tab_titulo="SELECT con.ide_facon, fecha_titulo_facon,nro_titulo_facon, cli.ruc_comercial_recli, cli.razon_social_recli, "
			+" coalesce(nombre_contacto_reclt, representante_legal_recli) as representante_legal_recli, direccion_recld, est.detalle_coest as estado "
			+" ,total_facturas,valor_facturas,valor_interes, valor_facturas+valor_interes as valor_titulo"
			+"   FROM fac_convenio con "
			+"   left join cont_estado est on est.ide_coest=con.ide_coest "
			+"   left join rec_clientes cli on cli.ide_recli=con.ide_recli "
			+"   left join (select ide_recli,coalesce(barrio_recld || ', ','')  || coalesce(direccion_recld || ' ','') || coalesce(nmro_casa_lote_dpto_recld || ' ','') || coalesce(interseccion_recld,'') as direccion_recld"
			+"             from rec_cliente_direccion where activo_recld=true and ide_recld in (select min(ide_recld) from rec_cliente_direccion"
			+"             where activo_recld=true group by ide_recli)) cliD on cliD.ide_recli=cli.ide_recli"			
			+" left join rec_cliente_telefono ccli on ide_retco=1 and activo_reclt=true and ccli.ide_recli=cli.ide_recli"			
			+" left join (SELECT ide_facon, sum(case when ide_fafac is not null then 1 else 0 end) as total_facturas, sum(case when ide_fafac is not null then valor_facof else 0 end) as valor_facturas"
			+" 	, sum(case when ide_fanod is not null then valor_facof else 0 end) as valor_interes"
			+" 	 FROM fac_detalle_convenio group by ide_facon) val on val.ide_facon=con.ide_facon"			
			+" where activo_facon=true and titulo_credito_facon=true and fecha_titulo_facon between '"+fecha_inicial+"' and  '"+fecha_final+"'"
			+" order by fecha_titulo_facon desc";	
	
	System.out.println("getSqlTitulosCredito "+tab_titulo);
	return tab_titulo;
	
}

public String getFacturaClientes(String cliente,String tipo,String fecha_inicial,String fecha_final,String aplica_fecha){
	//tipo 1 todos, 0 cliente
	//System.out.println("chequea gfechas "+aplica_fecha);
	String factura_clientes="select row_number() over(order by razon_social_recli,comprobante_aplicado,fecha_transaccion_fafac) as codigo,ide_fafac,a.ide_recli,detalle_bogrm,ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli"
			+" ,secuencial_fafac,round(total_fafac,2) as total,abono,saldo,grupo,"
			+" fecha_transaccion_fafac,fecha_vencimiento_fafac,a.ide_coest,detalle_coest,fecha_pago_fafac,conciliado_fafac,autorizada,detalle_retip,comprobante_aplicado,comprobante_pago,cheque "
			+" from ("
				+" 	select ide_fafac,ide_recli,detalle_bogrm,secuencial_fafac,total_fafac,abono,(case when a.ide_coest=16 or a.ide_coest=1 then 0 else (total_fafac-abono) end) as saldo,1 as grupo, fecha_transaccion_fafac,fecha_vencimiento_fafac,fecha_pago_fafac,a.ide_coest,conciliado_fafac,"
				+" ide_retip,documento_conciliado_fafac as comprobante_pago, documento_bancario_fafac as cheque, autorizada_sri_fafac as autorizada, secuencial_fafac as comprobante_aplicado "
				+" from ("
					+" 		select fac.ide_fafac,ide_fadaf,secuencial_fafac,total_fafac,coalesce(valor_cobro,0) as abono,ide_recli,ide_coest,fecha_transaccion_fafac,fecha_vencimiento_fafac,fecha_pago_fafac,conciliado_fafac,ide_retip,"
					+" 		documento_conciliado_fafac,documento_bancario_fafac,autorizada_sri_fafac,secuencial_fafac as comprobante_aplicado "
						+" 	from fac_factura fac "
						+" left join (select ide_fafac, sum(coalesce(valor_cobro_facob,0)+coalesce(valor_cobro_iva_facob,0)) as valor_cobro from fac_cobro group by ide_fafac) d on d.ide_fafac = fac.ide_fafac"
							+" ) a , ("
								+" 	select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, detalle_bogrm"
								+" 	from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm" 
								+" 	order by autorizacion_sri_bogrm"
								+" 	) b"
								+" 	where a.ide_fadaf = b.ide_fadaf"
								+" 	union"
								//AWBECERRA
						+" 	select a.ide_fanod,a.ide_recli,'NOTA DE DEBITO',nro_nota_debito_elect_fanod as detalle_fenod,interes_generado_fanod,(case when a.ide_coest=16 then interes_generado_fanod else 0 end) as abono,"
						+" (case when a.ide_coest=16 or a.ide_coest=1 then 0 else interes_generado_fanod end) as saldo,2 as grupo,a.fecha_ingre,fecha_emision_fanod,fecha_pago_fafac as fecha_pago,a.ide_coest,conciliado_fafac as conciliado,"
						+" c.ide_retip ,documento_conciliado_fafac as comprobante_pago, documento_bancario_fafac as cheque, autorizada_sri_fanod as autorizada,detalle_fenod as comprobante_aplicado "
						+" from fac_nota_debito a, fac_detalle_debito b, fac_factura c where a.ide_fanod = b.ide_fanod and b.ide_fafac =c.ide_fafac"
				+" 	) a, rec_clientes b,cont_estado c,rec_tipo d"
					+" where a.ide_recli =b.ide_recli"
				+" 	and a.ide_coest = c.ide_coest"
				+" 	and a.ide_retip= d.ide_retip ";
					if(tipo.equals("0")){
						//factura_clientes +=" and a.ide_recli= "+cliente;
						//factura_clientes +=" and a.ide_recli in (select ide_recli from rec_clientes where ruc_comercial_recli = (select ruc_comercial_recli from rec_clientes where ide_recli="+cliente+")) ";
						factura_clientes +=" and b.ruc_comercial_recli like '"+cliente+"' ";
					}
					if(aplica_fecha.equals("true")){
						factura_clientes +=" and fecha_pago_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' ";
					}
					else
					{
						factura_clientes +=" and fecha_transaccion_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' ";
					}
					factura_clientes +=" order by razon_social_recli,comprobante_aplicado,fecha_transaccion_fafac";
	System.out.println("consulta facturas tesoreria: "+ factura_clientes);
	return factura_clientes;
}

public String comprobantesClientes(String cliente,String fecha_inicial,String fecha_final,String aplica_fecha_pago,String conciliados){

	String comprobantes_clientes="select cli.ide_recli,ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli,detalle_bogrm,secuencial_fafac,total_fafac as total,abono, "
			+" (case when a.ide_coest=16 or a.ide_coest=1 then 0 else (total_fafac-abono) end) as saldo,  "
			+" fecha_transaccion_fafac,fecha_vencimiento_fafac,fecha_pago_fafac,detalle_coest,conciliado_fafac, detalle_retip,documento_conciliado_fafac as comprobante_pago,  "
			+" documento_bancario_fafac as cheque, autorizada_sri_fafac as autorizada, secuencial_fafac as comprobante_aplicado, 0 as tasa_interes, 0 as meses_atraso   ";
			
	if(conciliados.equals("false") && aplica_fecha_pago.equals("true")){
		comprobantes_clientes+=" ,observaciones_facob as observacion_pago ";	
	}
	else
		comprobantes_clientes+=" ,' - ' as observacion_pago ";
	
	comprobantes_clientes+=" from rec_clientes cli ";
	
			if(conciliados.equals("true")){
				comprobantes_clientes+=" join ( select fac.ide_fadaf,fac.ide_recli,secuencial_fafac,total_fafac,coalesce(total_fafac,0) as abono,ide_coest,fecha_transaccion_fafac,fecha_vencimiento_fafac,fecha_pago_fafac,conciliado_fafac,ide_retip, " 		
						+" documento_conciliado_fafac,documento_bancario_fafac,autorizada_sri_fafac,detalle_bogrm  "
						+" from fac_factura fac   "
						+" left join (select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, detalle_bogrm from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm order by autorizacion_sri_bogrm ) b on b.ide_fadaf = fac.ide_fadaf " 
						+" where ide_fafac not in (select ide_fafac from fac_cobro) and ide_coest=16 and coalesce(fecha_conciliado_fafac,fecha_pago_fafac) between '"+fecha_inicial+"' and '"+fecha_final+"' "
						+") a on a.ide_recli=cli.ide_recli ";	
			}
			else
			{
				comprobantes_clientes+=" join ( select fac.ide_fadaf,fac.ide_recli,secuencial_fafac,total_fafac,coalesce(valor_cobro,0) as abono,ide_coest,fecha_transaccion_fafac,fecha_vencimiento_fafac,"+ (aplica_fecha_pago.equals("true")?" fecha_cobro_facob as ":" ")+"fecha_pago_fafac,conciliado_fafac,ide_retip, " 		
						+ (aplica_fecha_pago.equals("true")?" cast(nro_documento_facob as character(25)) as ":" ")+" documento_conciliado_fafac, "
						+ (aplica_fecha_pago.equals("true")?" observaciones_facob ":" '' ")+" as observaciones_facob, "
						+" documento_bancario_fafac,autorizada_sri_fafac,detalle_bogrm  "
						+" from fac_factura fac   ";
				
						if(aplica_fecha_pago.equals("true")){
							comprobantes_clientes += " join (select ide_facob,ide_fafac,nro_documento_facob,fecha_cobro_facob, coalesce(valor_cobro_facob,0)+coalesce(valor_cobro_iva_facob,0) as valor_cobro,observaciones_facob from fac_cobro where fecha_cobro_facob between '"+fecha_inicial+"' and '"+fecha_final+"' ) d on d.ide_fafac = fac.ide_fafac "; 
						}
						else{ 
							comprobantes_clientes += " left join (select ide_fafac, sum(coalesce(valor_cobro_facob,0)+coalesce(valor_cobro_iva_facob,0)) as valor_cobro from fac_cobro group by ide_fafac) d on d.ide_fafac = fac.ide_fafac ";
						}
			
						comprobantes_clientes += " left join (select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, detalle_bogrm from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm order by autorizacion_sri_bogrm ) b on b.ide_fadaf = fac.ide_fadaf " 
						+" ) a on a.ide_recli=cli.ide_recli ";		
			}
	
	
			comprobantes_clientes+=" left join cont_estado est on est.ide_coest=a.ide_coest "
			+" left join rec_tipo tip on tip.ide_retip=a.ide_retip "
			+" where ruc_comercial_recli like '"+cliente+"' ";

			if(!aplica_fecha_pago.equals("true") && conciliados.equals("false")){
				comprobantes_clientes +=" and fecha_transaccion_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' ";
			}
			
			/////////////////////////////////////
			comprobantes_clientes +=" union all "
			
			+" select cli.ide_recli,ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli,detalle_bogrm,nro_nota_debito_elect_fanod,interes_generado_fanod,abono,saldo,  "
			+" fecha_emision_fanod,fecha_emision_fanod as fecha_vencimiento_fafac,fecha_pago,detalle_coest,conciliado, detalle_retip, cast(comprobante_pago as  character(25)) as comprobante_pago,  "
			+" cheque, autorizada, comprobante_aplicado, tasa_interes, meses_atraso  "; 
			
			if(conciliados.equals("false") && aplica_fecha_pago.equals("true")){
				comprobantes_clientes+=" ,observaciones_facob as observacion_pago ";	
			}
			else
				comprobantes_clientes+=" ,' - ' as observacion_pago ";
			
			comprobantes_clientes+=" from rec_clientes cli ";
			
			if(conciliados.equals("true")){
				comprobantes_clientes+=" join (select a.ide_recli,cast('NOTA DE DEBITO' as character(25)) as detalle_bogrm,nro_nota_debito_elect_fanod ,interes_generado_fanod,(case when a.ide_coest=16 then interes_generado_fanod else 0 end) as abono, " 
						+" (case when a.ide_coest=16 or a.ide_coest=1 then 0 else interes_generado_fanod end) as saldo,fecha_emision_fanod,fecha_emision_fanod as fecha_pago,a.ide_coest,(case when a.ide_coest=16 then true else false end) as conciliado,  "
						+" 4 as ide_retip ,documento_cobro_fanod as comprobante_pago, ide_transaccion_fanod as cheque, autorizada_sri_fanod as autorizada,detalle_fenod as comprobante_aplicado, interes_aplicado_faded as tasa_interes, dias_retraso_faded as meses_atraso   "
						+" from fac_nota_debito a "
						+" left join fac_detalle_debito b on b.ide_fanod=a.ide_fanod "
						+" where a.ide_coest=16 and a.ide_fanod not in (select ide_fanod from fac_cobro) and fecha_emision_fanod between '"+fecha_inicial+"' and '"+fecha_final+"' ";		
			}
			else
			{
				comprobantes_clientes+=" join (select a.ide_recli,cast('NOTA DE DEBITO' as character(25)) as detalle_bogrm,nro_nota_debito_elect_fanod ,interes_generado_fanod,(case when a.ide_coest=16 then interes_generado_fanod else 0 end) as abono, " 
				+" (case when a.ide_coest=16 or a.ide_coest=1 then 0 else interes_generado_fanod end) as saldo,fecha_emision_fanod,fecha_cobro_facob as fecha_pago,a.ide_coest,(case when a.ide_coest=16 then true else false end) as conciliado,  "
				+" d.ide_retip ,nro_documento_facob as comprobante_pago,observaciones_facob, cast((documento_cobro || observaciones_facob) as character(100)) as cheque, autorizada_sri_fanod as autorizada,detalle_fenod as comprobante_aplicado, interes_aplicado_faded as tasa_interes, dias_retraso_faded as meses_atraso   "
				+" from fac_nota_debito a "
				+" left join fac_detalle_debito b on b.ide_fanod=a.ide_fanod "
				+" left join fac_cobro d on d.ide_fanod=a.ide_fanod ";
				
				if(aplica_fecha_pago.equals("true")){
					comprobantes_clientes +=" where fecha_cobro_facob between '"+fecha_inicial+"' and '"+fecha_final+"' ";
				}
			}
			comprobantes_clientes +=" ) a on a.ide_recli=cli.ide_recli "
			+" left join cont_estado est on est.ide_coest=a.ide_coest "
			+" left join rec_tipo tip on tip.ide_retip=a.ide_retip "
			+" where ruc_comercial_recli like '"+cliente+"' ";
			
			if(!aplica_fecha_pago.equals("true") && conciliados.equals("false")){
				comprobantes_clientes +=" and fecha_emision_fanod between '"+fecha_inicial+"' and '"+fecha_final+"' ";
			}
			
			////////////////////////////////////
			if(conciliados.equals("false")){
				comprobantes_clientes+=" union all "
				
				+" select cli.ide_recli,ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli,detalle_bogrm,nro_nota_credito_fanoc,total_fanoc,abono,saldo,  "
				+" fecha_fanoc, fecha_vencimiento_fafac,fecha_pago,detalle_coest,conciliado, ' ' as detalle_retip, comprobante_pago,  "
				+" cheque, autorizada, comprobante_aplicado, 0 as tasa_interes, 0 as meses_atraso   "+ ",' - ' as observacion_pago"
				+" from rec_clientes cli "
				+" join ( "
				+" select b.ide_recli,cast('NOTA DE CREDITO' as character(25)) as detalle_bogrm,nro_nota_credito_fanoc, (-1)*total_fanoc as total_fanoc, 0 as abono,  "
				+" 0 as saldo,fecha_fanoc,fecha_vencimiento_fafac, fecha_pago_fafac as fecha_pago,a.ide_coest,false as conciliado,  "
				+" documento_conciliado_fafac as comprobante_pago, documento_bancario_fafac as cheque, autorizada_sri_fanoc as autorizada, secuencial_fafac as comprobante_aplicado   "
				+" from fac_nota_credito a "
				+" left join fac_factura b on b.ide_fafac = a.ide_fafac "
				+" ) a on a.ide_recli=cli.ide_recli "
				+" left join cont_estado est on est.ide_coest=a.ide_coest "
				+" where ruc_comercial_recli like '"+cliente+"' "
				+" and fecha_fanoc between '"+fecha_inicial+"' and '"+fecha_final+"' ";
			}
			
			comprobantes_clientes += " order by 7";
	
			if(cliente.equals("-1"))
				comprobantes_clientes += " limit 0 ";
					
		System.out.println("consulta comprobantesClientes: "+ comprobantes_clientes);
		return comprobantes_clientes;
	}

public String getFacturaRetencion(String ide_prtra, String ide_adfac){
	
	String tab_impuesto="select a.ide_adfac,ide_adsoc,ide_prtra,valor_retenido,detalle_adfac||' FACTURA: '||num_factura_adfac as detalle,ide_tepro,subtotal_adfac,valor_iva_adfac,total_adfac " 
			+" from adq_factura a, ( select ide_adfac,sum(total_ret_teret) as valor_retenido from tes_retencion group by ide_adfac ) b where a.ide_adfac = b.ide_adfac ";
	
	if(ide_adfac.length()>0)
		tab_impuesto+=" and a.ide_adfac ="+ide_adfac;
	else
		tab_impuesto+=" and ide_prtra ="+ide_prtra;
	
	return tab_impuesto;
	
}

public String getFacturaRetencionLiq(String ide_prtra, String ide_adlic){
	
	String tab_impuesto="select a.ide_adlic,ide_prtra,valor_retenido,detalle_adlic||' FACTURA: '||num_liquidacion_adlic as detalle,ide_tepro,subtotal_adlic,total_adlic "
			+" from adq_liquidacion_compra a, ( select ide_adlic,sum(total_ret_teret) as valor_retenido from tes_retencion where coalesce(autorizada_sri_teret,false)=true "
			+" group by ide_adlic ) b "
			+" where a.ide_adlic = b.ide_adlic";
	
	if(ide_adlic.length()>0)
		tab_impuesto+=" and a.ide_adlic ="+ide_adlic;
	else
		tab_impuesto+=" and ide_prtra ="+ide_prtra;
	
	return tab_impuesto;
	
}

public String getFacturaRetencion(String ide_prtra){
	
	String tab_impuesto="select a.ide_adfac,ide_adsoc,ide_prtra,valor_retenido,detalle_adfac||' FACTURA: '||num_factura_adfac as detalle,ide_tepro,subtotal_adfac,valor_iva_adfac,total_adfac " 
			+" from adq_factura a, ( select ide_adfac,sum(total_ret_teret) as valor_retenido from tes_retencion group by ide_adfac ) b where a.ide_adfac = b.ide_adfac ";
	
		tab_impuesto+=" and ide_prtra ="+ide_prtra;
	
	return tab_impuesto;
	
}

public String getCuentaPresupuestariaGastos()
{
	String sql="select a.ide_pranu,(codigo_clasificador_prcla||' - '||codigo_prfup) as codigo_clasificador_prcla,substring(descripcion_clasificador_prcla from 1 for 100) as codigo, coalesce(detalle_prfuf,' ') as fuente "
			+ " from pre_anual a "
			+ " join pre_poa b on a.ide_prpoa = b.ide_prpoa "
			+ " join pre_funcion_programa pfp on pfp.ide_prfup=b.ide_prfup "
			+ " join pre_clasificador c on b.ide_prcla= c.ide_prcla "
			+ " left join pre_fuente_financiamiento ff on ff.ide_prfuf=a.ide_prfuf order by codigo_clasificador_prcla";
	return sql;
}

public String getCuentaPresupuestaria()
{
	String sql=" select a.ide_pranu,codigo_clasificador_prcla,coalesce(detalle_geani,' ') as detalle_geani,substring(descripcion_clasificador_prcla from 1 for 60) as codigo,coalesce(detalle_prfuf,' ') as fuente " 
			+ "  from pre_anual a"
			 + " left join pre_clasificador c on c.ide_prcla= a.ide_prcla"
			 + " left join pre_fuente_financiamiento ff on ff.ide_prfuf=a.ide_prfuf "
			 + " left join gen_anio anio on anio.ide_geani=a.ide_geani"
			 + " order by codigo_clasificador_prcla,detalle_geani";
	return sql;
}

public String getCuentaPresupuestariaMov()
{
	String sql="select a.ide_pranu,(codigo_clasificador_prcla||' - '||codigo_prfup) as codigo_clasificador_prcla,coalesce(detalle_geani,' ') as detalle_geani , substring(descripcion_clasificador_prcla from 1 for 60) as codigo, coalesce(detalle_prfuf,' ') as fuente " 
			+ " from pre_anual a "
			 + " join pre_poa b on a.ide_prpoa = b.ide_prpoa "
			 + " join pre_funcion_programa pfp on pfp.ide_prfup=b.ide_prfup "
			 + " join pre_clasificador c on b.ide_prcla= c.ide_prcla "
			 + " left join pre_fuente_financiamiento ff on ff.ide_prfuf=a.ide_prfuf  "
			 + " left join gen_anio anio on anio.ide_geani=a.ide_geani  ";
	 sql+=" union all  "
			+ " select a.ide_pranu,codigo_clasificador_prcla,coalesce(detalle_geani,' ') as detalle_geani,substring(descripcion_clasificador_prcla from 1 for 60) as codigo,coalesce(detalle_prfuf,' ') as fuente "
			+ " from pre_anual a "
			 + " left join pre_clasificador c on c.ide_prcla= a.ide_prcla " 
			 + " left join pre_fuente_financiamiento ff on ff.ide_prfuf=a.ide_prfuf  "
			 + " left join gen_anio anio on anio.ide_geani=a.ide_geani "
			 + " order by codigo_clasificador_prcla,detalle_geani";
	 
	System.out.println("getCuentaPresupuestariaMov "+sql);
	return sql;	
}

public String getSaldoDevengado(String asiento,String comprobante)
{
String sql="select row_number() over(order by b.ide_pranu) as codigo,b.ide_prfup,a.codigo_clasificador_prcla,b.detalle_prfuf,comprometido_prpot - coalesce(valor_devengado,0) as saldo_compromiso,"
		+ " comprometido_prpot,coalesce(valor_devengado,0) as valor_devengado "
	    +",b.ide_pranu,b.ide_prfuf from ("
	    +" 	select a.ide_cocac,a.ide_prcla,b.ide_tecpo,pagado,devengado,ide_prmop,debe_codem,haber_codem,d.codigo_clasificador_prcla,e.codigo_clasificador_prcla as cuenta_devengado"
		+" 	from pre_asociacion_presupuestaria a,tes_comprobante_pago b,cont_detalle_movimiento c,"
		+" 	(select ide_prcla,codigo_clasificador_prcla from pre_clasificador) d,"
		+" 	(select ide_prcla,codigo_clasificador_prcla from pre_clasificador) e"
		+" 	where b.ide_comov = c.ide_comov"
		+" 	and a.ide_cocac = c.ide_cocac"
		+" 	and a.ide_prcla= d.ide_prcla"
		+" 	and devengado= e.ide_prcla"
		+" 	and ide_prmop = 5"
		+" 	and c.ide_codem ="+asiento
		+" 	) a"
		+" 	left join ("
		+" 			select a.ide_prpoa,ide_tecpo,a.ide_prfuf,detalle_prfuf,codigo_clasificador_prcla,comprometido_prpot,ide_pranu,b.ide_prtra,c.ide_prfup,c.ide_prcla" 
		+" 			from pre_poa_tramite a left join tes_comprobante_pago b on a.ide_prtra = b.ide_prtra  "
		+"	       left join pre_poa c on a.ide_prpoa = c.ide_prpoa "
		+"         left join pre_clasificador d on c.ide_prcla = d.ide_prcla "
		+"         left join pre_fuente_financiamiento e on a.ide_prfuf = e.ide_prfuf 	"
		+"         left join pre_anual f on c.ide_prpoa = f.ide_prpoa "
		+" 			where b.ide_tecpo ="+comprobante
		+" 			) b on a.ide_tecpo = b.ide_tecpo and b.codigo_clasificador_prcla like cuenta_devengado||'%' and a.ide_prcla = b.ide_prcla"
		+" 			left join ("
		+" 					select ide_prpoa,a.ide_prtra,a.ide_prfuf,sum(devengado_prmen) as valor_devengado"
		+" 					from pre_mensual a, pre_anual b"
		+" 					where a.ide_pranu = b.ide_pranu "
		+" 					group by ide_prpoa,a.ide_prtra,a.ide_prfuf"
		+" 					) c on b.ide_prtra = c.ide_prtra and b.ide_prpoa = c.ide_prpoa and b.ide_prfuf = c.ide_prfuf";
System.out.println("getSaldoDevengado "+sql);		
return sql;
		
}
public String getFacturasComprobantes(String condicion){
	String sql="select b.ide_adfac,(case when coalesce(codigo_srsuc,'02') like '02' then false else true end) as aplica_credito,secuencial_prtra as compromiso,"
			+ " num_factura_adfac,fecha_factura_adfac,nombre_tepro,detalle_adfac,total_adfac,valor_iva_adfac "
			+ " from adq_factura b"
			+ " left join tes_proveedor c on c.ide_tepro=b.ide_tepro"
			+ " left join sri_sustento_comprobante ssc on ssc.ide_srsuc=b.ide_srsuc"
			+ " left join pre_tramite comp on comp.ide_prtra=b.ide_prtra"
			+ " where ide_tecpo is null and b.ide_prtra is not null "+condicion+" order by fecha_factura_adfac desc";

	return sql;
}
public String getSaldoFuenteDevengar(String tramites){
	String sql="select ide_prpot, a.ide_prpoa,a.ide_prfuf,a.ide_prtra,comprometido_prpot,(case when valor_devengado is null then 0 else valor_devengado end) as val_devengado,"
			+" comprometido_prpot - (case when valor_devengado is null then 0 else valor_devengado end) as saldo_devengar,detalle_prfuf,codigo_clasificador_prcla,detalle_claificador"
			+" from pre_poa_tramite a"
			+" left join ( select ide_prpoa,a.ide_prtra,a.ide_prfuf,sum(devengado_prmen) as valor_devengado"
				+" 	from pre_mensual a, pre_anual b where a.ide_pranu = b.ide_pranu and not ide_prpoa  is null"
				+" 	group by ide_prpoa,a.ide_prtra,a.ide_prfuf"
				+" 	) b on a.ide_prtra = b.ide_prtra and a.ide_prpoa = b.ide_prpoa and a.ide_prfuf = b.ide_prfuf"
				+" 	left join pre_fuente_financiamiento c on a.ide_prfuf = c.ide_prfuf"
				+" 	left join ( select ide_prpoa,a.ide_prcla,codigo_clasificador_prcla,substring(descripcion_clasificador_prcla from 1 for 100) as detalle_claificador"
				+" 			from pre_poa a , pre_clasificador b where a.ide_prcla = b.ide_prcla ) d on a.ide_prpoa = d.ide_prpoa"
				+" 			where a.ide_prtra in ( "+tramites+") order by a.ide_prtra";
	return sql;
}
public String getPoaResumen(){
	String sql="select ide_prpoa,codigo_clasificador_prcla,substring(descripcion_clasificador_prcla from 1 for 100) as detalle_claificador from pre_poa a , pre_clasificador b where a.ide_prcla = b.ide_prcla ";
	return sql;
}
public String getFacturasComprobante(String comprobante){
	String sql="select ide_adfac,num_factura_adfac,detalle_adfac from adq_factura where ide_tecpo ="+comprobante;
	return sql;	
}
public String getComprobantePoa(String comprobante){
	String sql="select a.ide_tecmp,b.ide_prtra,secuencial_prtra as nro_compromiso,detalle_prfuf,saldo_devengar_tecmp,valor_devengar_tecmp,valor_iva_tecmp,codigo_clasificador_prcla,detalle_claificador,"
			+" (case when aplica_iva_tecmp =true then 'SI APLICA' when aplica_iva_tecmp= false then 'NO APLICA' end) as aplica_iva"
			+" from tes_comprobante_poa a, pre_poa_tramite b,("
				+" 	select ide_prpoa,codigo_clasificador_prcla,substring(descripcion_clasificador_prcla from 1 for 100) as detalle_claificador from pre_poa a , pre_clasificador b where a.ide_prcla = b.ide_prcla" 
				+" 	) c ,pre_fuente_financiamiento d "
				+ " , pre_tramite e  where a.ide_prpot = b.ide_prpot and a.ide_prpoa = c.ide_prpoa and a.ide_prfuf = d.ide_prfuf and e.ide_prtra=b.ide_prtra and ide_tecpo ="+comprobante;
	return sql;		
}
public String getTotalFacturaComprobante(String comprobante){

	String sql="select ide_tecpo, sum(subtotal_adfac) as subtotal,sum(total_adfac) as total,sum(valor_iva_adfac) as valor_iva,sum(base_iva_adfac) as base_iva"
			+" from ("
			+" 		select ide_adfac,subtotal_adfac,total_adfac,valor_iva_adfac,base_iva_adfac,ide_tecpo"
			+" 		from adq_factura where ide_tecpo in ("+comprobante+")"
			+" 		) a group by ide_tecpo";
	return sql;
}
public String getTotalRetencionComprobante(String comprobante){
	
	String sql="select ide_tecpo,sum (valor_retenido) as total_retenido from adq_factura a, ("
			+" select ide_adfac,sum(valor_retenido) as valor_retenido from tes_retencion a,("
			+" 		select ide_teret,sum(valor_retenido_teder) as valor_retenido "
			+" 		from tes_detalle_retencion group by ide_teret"
			+" 		) b where a.ide_teret = b.ide_teret group by ide_adfac"
			+" 	) b where a.ide_adfac= b.ide_adfac group by ide_tecpo having ide_tecpo="+comprobante;
	return sql;
}
public String getSqlConciliacionBancaria(String cuenta_contable,String tipo_conciliacion,String fecha_inicial,String fecha_final,String tipo_seleccionado,String seleccionados){
	String sql="";
	//SQL NO CONCILIADO
	String sql1="select ide_codem,c.ide_cocac,a.ide_comov,mov_fecha_comov,nro_comprobante_comov, (case when conciliado_banco_codem is null then 'NO CONCILIADO' when conciliado_banco_codem = 1 then 'CONCILIADO'"
			+" when conciliado_banco_codem =2 then 'EN TRANSITO' end) as estado"
			+" ,detalle_comov,cue_codigo_cocac,cue_descripcion_cocac,debe_codem,haber_codem,conciliado_banco_codem,fecha_conciliado_banco_codem,ide_tebam,saldo_cocac"
			+" from cont_movimiento a, cont_detalle_movimiento b, cont_catalogo_cuenta c"
			+" where a.ide_comov = b.ide_comov"
			+" and b.ide_cocac = c.ide_cocac and not ide_cotia=4 "
			+" and b.ide_cocac in ( "+cuenta_contable+" )"
			+" and conciliado_banco_codem is null";
			if(tipo_seleccionado.equals("1")){
				sql1 +=" and ide_codem in ("+seleccionados+") ";
			}
			sql1+=" and mov_fecha_comov between '"+fecha_inicial+"' and '"+fecha_final+"'";
			
	// SQL EN TRANSITO
	String sql2="select ide_codem,c.ide_cocac,a.ide_comov,mov_fecha_comov,nro_comprobante_comov,(case when conciliado_banco_codem is null then 'NO CONCILIADO' when conciliado_banco_codem = 1 then 'CONCILIADO'"
			+" when conciliado_banco_codem =2 then 'EN TRANSITO' end) as estado"
			+" ,detalle_comov,cue_codigo_cocac,cue_descripcion_cocac,debe_codem,haber_codem,conciliado_banco_codem,fecha_conciliado_banco_codem,ide_tebam,saldo_cocac"
			+" from cont_movimiento a, cont_detalle_movimiento b, cont_catalogo_cuenta c"
			+" where a.ide_comov = b.ide_comov"
			+" and b.ide_cocac = c.ide_cocac and not ide_cotia=4"
			+" and b.ide_cocac in ( "+cuenta_contable+" )"
			+" and conciliado_banco_codem =2";
			if(tipo_seleccionado.equals("1")){
				sql2 +=" and ide_codem in ("+seleccionados+") ";
			}
			//sql2+=" and mov_fecha_comov <= '"+fecha_final+"'";
			sql2+=" and mov_fecha_comov between '"+fecha_inicial+"' and '"+fecha_final+"'";
	

	if(tipo_conciliacion.equals("1")){
				sql+=sql1;
						
	}
	if(tipo_conciliacion.equals("2")){
				sql+=sql1+" union "+sql2;
	}
	
	sql+=" order by estado, mov_fecha_comov,cue_codigo_cocac";
	
	System.out.println("despliega sql de lo tab "+sql);
	return sql;
}


public String getSqlRevConciliacionBancaria(String cuenta_contable,String tipo_conciliacion,String fecha_inicial,String fecha_final,String tipo_seleccionado,String seleccionados){
	String sql="select ide_codem,c.ide_cocac,(case when conciliado_banco_codem is null then 'NO CONCILIADO' when conciliado_banco_codem = 1 then 'CONCILIADO'"
			+" when conciliado_banco_codem =2 then 'EN TRANSITO' end) as estado"
			+" ,mov_fecha_comov,nro_comprobante_comov,detalle_comov,cue_codigo_cocac,cue_descripcion_cocac,debe_codem,haber_codem,conciliado_banco_codem,fecha_conciliado_banco_codem,ide_tebam,saldo_cocac"
			+" from cont_movimiento a, cont_detalle_movimiento b, cont_catalogo_cuenta c"
			+" where a.ide_comov = b.ide_comov"
			+" and b.ide_cocac = c.ide_cocac and not ide_cotia=4 "
			+" and b.ide_cocac in ( "+cuenta_contable+" )"
			+" and ide_codem in ("+seleccionados+") ";

	
	sql+=" order by estado, mov_fecha_comov,cue_codigo_cocac";
	
	System.out.println("despliega sql de lo tab "+sql);
	return sql;
}

public String getSqlConciliacionBancariaSaldo(String cuenta_contable, String mes, String anio, String tipo){

	String sql="select ide_geani, sum(debe_codem) as debe, sum(haber_codem) as haber, sum(debe_codem)-sum(haber_codem) as saldo from ("
		 +" select ide_codem,c.ide_cocac,a.ide_comov,debe_codem,haber_codem,a.ide_geani,"
		 +" conciliado_banco_codem,fecha_conciliado_banco_codem,ide_tebam,saldo_cocac "
		 +" from cont_movimiento a, cont_detalle_movimiento b, cont_catalogo_cuenta c "
		 +" where a.ide_comov = b.ide_comov and b.ide_cocac = c.ide_cocac and b.ide_cocac in ( "+cuenta_contable+" )" ;
	
	if(tipo.equals("1")){ 
		sql+=" and ide_cotia=4 ";
	}
		 
	sql +=" and extract(month from mov_fecha_comov) <="+mes+" and a.ide_geani="+anio
		 +" ) a group by ide_geani";

	System.out.println("despliega getSqlConciliacionBancariaSaldo "+sql);
	return sql;
}

/**
 * Metodo que devuelve la consulta de los movimientos que pertenecen a un conciliacioon bancaria, o estas pueden tambiene star en transito 
 * @param tipo si es contable o si esta en transito
 * @param codigo clave primaria d ela tabla ide_tebam 
 * @return string SQL modulo estado
 */
public String sqlConciliado(String tipo,String codigo){
	String sql="select ide_codem,c.ide_cocac,a.ide_comov,mov_fecha_comov,nro_comprobante_comov,(case when conciliado_banco_codem is null then 'NO CONCILIADO' when conciliado_banco_codem = 1 then 'CONCILIADO'"
			+" when conciliado_banco_codem =2 then 'EN TRANSITO' end) as estado"
			+" ,detalle_comov,cue_codigo_cocac,cue_descripcion_cocac,debe_codem,haber_codem,conciliado_banco_codem,fecha_conciliado_banco_codem,ide_tebam,saldo_cocac"
			+" from cont_movimiento a, cont_detalle_movimiento b, cont_catalogo_cuenta c"
			+" where a.ide_comov = b.ide_comov"
			+" and b.ide_cocac = c.ide_cocac"
			+" and ide_tebam = "+codigo;
	if(tipo.equals("2")){
		sql+=" and conciliado_banco_codem =2 " ;
	}
	else if(tipo.equals("1")){ 
		sql+=" and conciliado_banco_codem =1";
	}
	System.out.println("sql consulta "+sql);
	return sql;
}

	public String getReportePagosCliente(String fecha_inicial,String fecha_final,String ruc)
	{
		String sql="select row_number() over(order by ruc) as codigo, * from ( ";
		
		sql+="select ruc_comercial as ruc ,razon_social as nombre,caja as origen_caja, forma_cobro, fecha_cobro_facob as fecha_pago,nro_documento_facob as nro_documento,sum(cobro) as monto,string_agg(documento||':'||secuencial, ',') as observaciones"+
             " from ( ";
		sql+=getReporteCaja("1",fecha_inicial,fecha_final,"0",ruc);
		
		sql+=") rpt "+
		     " group by ruc_comercial,razon_social,caja,forma_cobro,fecha_cobro_facob,nro_documento_facob ";
		
		sql+=" union all ";
		
		sql+=" SELECT ruc_comercial_recli as ruc,razon_social_recli as nombre,cast('CONCILIACION' as character (15)) as origen_caja, cast(coalesce(documento_conciliado_fafac,'Otros') as character(10)) as forma_cobro, coalesce(fecha_pago_fafac,fecha_transaccion_fafac) as fecha_pago, 0 as nro_documento,coalesce(case when cobro_caja>0 then cobro_caja else null end ,total_fafac )as monto, string_agg('FACTURA:'||secuencial_fafac||'-'||coalesce(documento_bancario_fafac,'N/A'), ',') as observaciones "+  
				" FROM fac_factura fac "+
				" join rec_clientes cli on cli.ide_recli=fac.ide_recli "+
				" left join (select ide_fafac,sum(coalesce(valor_cobro_facob,0) + coalesce(valor_cobro_iva_facob,0)) as cobro_caja from fac_cobro group by ide_fafac) co on co.ide_fafac=fac.ide_fafac "+ 
				" where ruc_comercial_recli like '"+ruc+"' "+
				" and ide_coest=16 "+
				" and coalesce(co.cobro_caja,0)=0 "+
				" and coalesce(fecha_pago_fafac,fecha_transaccion_fafac) between '"+fecha_inicial+"' and '"+fecha_final+"' "+
				" group by ruc_comercial_recli,razon_social_recli,documento_conciliado_fafac, "+
				" fecha_pago,monto";
		
		sql+=" ) a order by fecha_pago";
		
		System.out.println("getReportePagosCliente: "+sql);
		return sql;
	
	}

	public String getReporteCaja(String tipo,String fecha_inicial,String fecha_final, String nro_documento, String ruc){
		
		String sql="";
		
			sql=" select documento,caja,sucursal,forma_cobro,NOMBRES as recaudador,fecha_cobro_facob,nro_documento_facob,cobro,cliente_pago_facob,cheque_transf,secuencial," +
					" fecha_emision,ruc_comercial_recli as ruc_comercial,razon_social_recli as razon_social,establecimiento_operativo_recli as establecimiento_operativo, observacion, cod_autorizacion_facob as idTran_cod_aut" +
					" from (" +
					" SELECT ide_facob,'FACTURA' as documento, ca.detalle_tecaj as caja, sis.nom_sucu as sucursal, rt.detalle_retip as forma_cobro, " +
					" cast(substring (coalesce(APELLIDO_PATERNO_GTEMP,'') || ' ' || coalesce(PRIMER_NOMBRE_GTEMP,'') from 1 for 30) as character varying(30)) AS NOMBRES," +
					" fecha_cobro_facob,nro_documento_facob, coalesce(valor_cobro_facob,0) + coalesce(valor_cobro_iva_facob,0) as cobro," +
					" cliente_pago_facob,documento_cobro as cheque_transf,secuencial_fafac as secuencial,fecha_transaccion_fafac as fecha_emision,ruc_comercial_recli,razon_social_recli,"
					+ "establecimiento_operativo_recli, observaciones_facob as observacion,cod_autorizacion_facob " +
					" FROM fac_cobro fc" +
					" left join tes_caja ca on ca.ide_tecaj=fc.ide_tecaj" +
					" left join sis_sucursal sis on sis.ide_sucu=fc.ide_sucu" +
					" left join rec_tipo rt on rt.ide_retip=fc.ide_retip" +
					" left join gth_empleado ge on ge.ide_gtemp=fc.ide_gtemp" +
					" join fac_factura fac on fac.ide_fafac=fc.ide_fafac" +
					" join rec_clientes cli on cli.ide_recli=fac.ide_recli " +
					"" +
					" union all" +
					"" +
					" SELECT ide_facob,'NOTA DEBITO' as documento, ca.detalle_tecaj as caja, sis.nom_sucu as sucursal, rt.detalle_retip as forma_cobro, " +
					" cast(substring (coalesce(APELLIDO_PATERNO_GTEMP,'') || ' ' || coalesce(PRIMER_NOMBRE_GTEMP,'') from 1 for 30) as character varying(30)) AS NOMBRES," +
					" fecha_cobro_facob,nro_documento_facob, coalesce(valor_cobro_interes_facob,0) as cobro," +
					" cliente_pago_facob,documento_cobro as cheque_transf,nro_nota_debito_elect_fanod as secuencial,fecha_emision_fanod as fecha_emision,ruc_comercial_recli,razon_social_recli,"
					+ "establecimiento_operativo_recli, observaciones_facob as observacion,cod_autorizacion_facob " +
					" FROM fac_cobro fc" +
					" left join tes_caja ca on ca.ide_tecaj=fc.ide_tecaj" +
					" left join sis_sucursal sis on sis.ide_sucu=fc.ide_sucu" +
					" left join rec_tipo rt on rt.ide_retip=fc.ide_retip" +
					" left join gth_empleado ge on ge.ide_gtemp=fc.ide_gtemp" +
					" join fac_nota_debito nd on nd.ide_fanod=fc.ide_fanod  " +
					" join rec_clientes cli on cli.ide_recli=nd.ide_recli " +
					"" +
					" union all" +
					"" +
					" SELECT ide_facob,'CONTRATO' as documento, ca.detalle_tecaj as caja, sis.nom_sucu as sucursal, rt.detalle_retip as forma_cobro, " +
					" cast(substring (coalesce(APELLIDO_PATERNO_GTEMP,'') || ' ' || coalesce(PRIMER_NOMBRE_GTEMP,'') from 1 for 30) as character varying(30)) AS NOMBRES," +
					" fecha_cobro_facob,nro_documento_facob, coalesce(valor_cobro_facob,0) + coalesce(valor_cobro_iva_facob,0) as cobro," +
					" cliente_pago_facob,documento_cobro as cheque_transf,lpad(numero_contrato_prcon,5,'0') as secuencial,fecha_firma_prcon as fecha_emision,ruc_comercial_recli,razon_social_recli,"
					+ "establecimiento_operativo_recli, observaciones_facob as observacion,cod_autorizacion_facob " +
					" FROM fac_cobro fc" +
					" left join tes_caja ca on ca.ide_tecaj=fc.ide_tecaj" +
					" left join sis_sucursal sis on sis.ide_sucu=fc.ide_sucu" +
					" left join rec_tipo rt on rt.ide_retip=fc.ide_retip" +
					" left join gth_empleado ge on ge.ide_gtemp=fc.ide_gtemp" +
					" join pre_contrato crt on crt.ide_prcon=fc.ide_prcon  " +
					" join rec_clientes cli on cli.ide_recli=crt.ide_recli " +
					"" +
					" ) a ";
		
			if(tipo.equals("1")){ 
				sql+=" where fecha_cobro_facob between '"+fecha_inicial+"' and '"+fecha_final+"'";
				if(ruc.length()>0)
					sql+=" and ruc_comercial_recli like '"+ruc+"' ";
			}
			
			if(tipo.equals("2")){ 
				sql+=" where nro_documento_facob = "+nro_documento;
			}
			
			if(tipo.equals("-1")){ 
				sql+=" limit 0";
			}
			
		System.out.println("getReporteCaja: "+sql);
		return sql;
		
	}
	
public String getReporteConciliacionPich(String tipo,String fecha_inicial,String fecha_final, String nro_documento, String ruc){
		
		String sql="";
		
			sql=" SELECT ide_faauc, nom_usua, fecha_pago_archivo_faauc, fecha_hora_concilia_faauc, "
			      +" contrapartida_faauc, ruc_cliente_faauc, cliente_faauc, valor_archivo_faauc, "
			      +"  valor_deuda_faauc, interes_incluido_deuda_faauc, valor_vuelto_faauc, "
			      +" documento_conciliado_faauc, referencia_faauc, cod_autorizacion_faauc, "
			      +" observacion_faauc, conciliado_faauc, validado_faauc, activo_faauc, "
			      +" f.usuario_ingre, f.fecha_ingre as fecha_ingreso, f.hora_ingre as hora_ingreso, f.usuario_actua, f.fecha_actua, "
			      +" f.hora_actua "
			  +" FROM fac_auditoria_conciliacion f"
			  +" left join sis_usuario s on s.ide_usua=f.ide_usua  ";
		
			if(tipo.equals("1")){ 
				sql+=" where fecha_pago_archivo_faauc between '"+fecha_inicial+"' and '"+fecha_final+"'";
				if(ruc.length()>0)
					sql+=" and ruc_cliente_faauc like '"+ruc+"' ";
			}
			
			if(tipo.equals("2")){ 
				sql+=" where documento_conciliado_faauc like '%"+nro_documento+"' ";
			}
			
			if(tipo.equals("-1")){ 
				sql+=" limit 0";
			}
			
		System.out.println("getReporteConciliacionPich: "+sql);
		return sql;
		
	}
	
	public String getActiciposRec(String tipo,String fecha_inicial,String fecha_final){
		
		String sql="";
		
			sql = "  SELECT fc.ide_facob,cast(lpad(numero_contrato_prcon,4,'0') as character varying(25)) as secuencial,cast('CONTRATO' as character varying(30)) as tipo_documento, "
				 +" fecha_cobro_facob,nro_documento_facob as num_recibo_caja, ruc_comercial_recli,cast(substring(razon_social_recli from 1 for 100) as character varying(100)) as razon_social_recli,"
				 +" coalesce(valor_cobro_facob,0) + coalesce(valor_cobro_iva_facob,0) as valor_cobro, "
				 +" rt.detalle_retip as forma_cobro,  documento_cobro as cheque_transf,"
				 +" establecimiento_operativo_recli, observaciones_facob as observacion "
				 
				 +" FROM fac_cobro fc "
				 +" left join rec_tipo rt on rt.ide_retip=fc.ide_retip "
				 +" left join cont_anticipo_asiento caa on caa.ide_facob=fc.ide_facob"
				 +" join pre_contrato crt on crt.ide_prcon=fc.ide_prcon   "
				 +" join rec_clientes cli on cli.ide_recli=crt.ide_recli "
				
				+" where caa.ide_coana is null " ;
		
			if(tipo.equals("1")){ 
				sql+=" and fecha_cobro_facob between '"+fecha_inicial+"' and '"+fecha_final+"'";
			}
			
			if(tipo.equals("-1")){ 
				sql+=" limit 0";
			}
			
		System.out.println("getReporteCaja: "+sql);
		return sql;
		
	}
	
	public String getRptAnticiposContabilizados(String ide_geani){
		String sql=(" SELECT distinct CAST (varchar 'ANTICIPO CONTRATO' AS text) as tipo_comprobante, ruc_comercial_recli, lpad(numero_contrato_prcon,4,'0') as contrato, fecha_cobro_facob as fecha_emision,  " +
				" c.detalle_coest as estado_contrato, coalesce(valor_cobro_facob,0) as subtotal, coalesce(valor_cobro_iva_facob,0) as iva,coalesce(valor_cobro_facob,0) as valor_contabilizado, observaciones_facob as observacion,  " +
				" coalesce(cast(nro_documento_facob as character varying(10)),cliente_pago_facob) as nro_comprobante_cobro " +
				" ,cat.ide_comov as codigo_movimiento, mov.mov_fecha_comov as fecha_movimiento, mov.detalle_comov as detalle_movimiento " +
				" FROM cont_anticipo_asiento cat " +
				" join cont_movimiento mov on mov.ide_comov=cat.ide_comov " +
				" join fac_cobro fc on fc.ide_facob=cat.ide_facob " +
				" join pre_contrato crt on crt.ide_prcon=fc.ide_prcon " +
				" join rec_clientes cli on cli.ide_recli=crt.ide_recli " +
				" left join cont_estado c on c.ide_coest = crt.ide_coest  " +
				" where ide_geani=" +ide_geani+
				" order by cat.ide_comov desc");
		System.out.println("getRptAnticiposContabilizados: "+sql);
			return sql;
	}
	
	public String getRptAuxiliarAnticiposContabilizados(String ide_geani){
		String sql=(" select * from auxiliar_anticipo_escombrera where extract(year from fecha_emision)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+ide_geani+")" +
				" order by fecha_emision ");
		System.out.println("getRptAuxiliarAnticiposContabilizados: "+sql);
			return sql;
	}
	
	public String getAnticiposInsertaContabilidad(String ide_facob,String lugar_aplica,String ide_conac,String individual_grupal, String ide_coest){
		
		 String str_factura_contabilidad="";
		 if(individual_grupal.equals("1")){
			 str_factura_contabilidad +="select ide_cocac,ide_gelua,sum(valor_asiento) as valor_asiento,detalle_bogrm, 'De los contratos ('||textcat_all(secuencial || ', ') ||')' as secuencial from ( ";
		 }

		 str_factura_contabilidad +=" select ide_facob,ide_cocac,ide_gelua,ide_coest,ide_conac,tipo_iva_coast,secuencial,valor_cobro, "
				+"  (case when tipo_iva_coast is true then valor_cobro_iva when base_imponible_coast is true then valor_cobro when valor_total_coast is true then (valor_cobro_iva+valor_cobro) else 0 end) * (case when factor_menos_coast is true then -1 else 1 end) as valor_asiento, detalle_bogrm" 
				+"  from ( select cat.ide_bogrm,ide_cocac,ide_gelua,ide_coest,ide_conac,tipo_iva_coast,activo_coast,base_imponible_coast,valor_total_coast,factor_menos_coast, detalle_bogrm"
				+"   from cont_asiento_tipo cat"
				+"   left join bodt_grupo_material c on cat.ide_bogrm=c.ide_bogrm" 
				+"   where ide_gelua in (select cast(valor_para as integer) as valor from sis_parametros where nom_para = '"+lugar_aplica+"') and activo_coast = true" 
				+"  ) a, "
				+"  ( select ide_facob,fecha_cobro_facob,nro_documento_facob as num_recibo_caja,lpad(numero_contrato_prcon,4,'0') as secuencial,"
				+"  coalesce(valor_cobro_facob,0) as valor_cobro, coalesce(valor_cobro_iva_facob,0) as valor_cobro_iva"
				+"  from fac_cobro fc "
				+"  join pre_contrato crt on crt.ide_prcon=fc.ide_prcon" 
				+"  where ide_facob in ("+ide_facob+")"
				+"  ) b "
				+"  where a.ide_bogrm=92 and ide_conac ="+ide_conac;	
		 
		 
		 if(individual_grupal.equals("1")){
			 str_factura_contabilidad +=" ) a group by ide_cocac,ide_gelua,detalle_bogrm";
		 }
		 
		 System.out.println("getAnticiposInsertaContabilidad ingrese a anticipos contratos asientos "+str_factura_contabilidad);
		 return str_factura_contabilidad;
	 }
	
	public String obtenerCaja(){
		String caja="";
		List list_sql1 = utilitario.getConexion().consultar("select ide_tecaj from fac_usuario_lugar where ide_usua ="+utilitario.getVariable("IDE_USUA")+" and activo_fausl=true and recauda_fausl =true limit 1");	
		//System.out.println(" fff "+list_sql1);
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
			caja=String.valueOf(list_sql1.get(0));
		}
		return caja;
	}


    public String getCierreCajaDet(String tipo,String fecha_inicial,String fecha_final, String nro_documento){
		
		String sql="select ide_facob,documento,forma_cobro,fecha_cobro_facob,nro_documento_facob,cobro,cliente_pago_facob,cheque_transf,secuencial,"+
				 " fecha_emision,ruc_comercial_recli as ruc_comercial,razon_social_recli as razon_social,establecimiento_operativo_recli as establecimiento_operativo, observacion, ide_tecca "+
				 " from ( ";
		
		String sql_f=" SELECT ide_facob,'FACTURA' as documento, rt.detalle_retip as forma_cobro, "+
					" fecha_cobro_facob,nro_documento_facob, coalesce(valor_cobro_facob,0)  + coalesce(valor_cobro_iva_facob,0) as cobro,"+
					" cliente_pago_facob,documento_cobro as cheque_transf,secuencial_fafac as secuencial,fecha_transaccion_fafac as fecha_emision,ruc_comercial_recli,razon_social_recli,"+
					" establecimiento_operativo_recli, observaciones_facob as observacion, ide_tecca"+
					" FROM fac_cobro fc"+
					" left join rec_tipo rt on rt.ide_retip=fc.ide_retip"+
					" join fac_factura fac on fac.ide_fafac=fc.ide_fafac"+
					" join rec_clientes cli on cli.ide_recli=fac.ide_recli ";
		
		String sql_nd=" SELECT ide_facob,'NOTA DEBITO' as documento, rt.detalle_retip as forma_cobro,  "+
					" fecha_cobro_facob,nro_documento_facob, coalesce(valor_cobro_interes_facob,0) as cobro, "+
					" cliente_pago_facob,documento_cobro as cheque_transf,nro_nota_debito_elect_fanod as secuencial,fecha_emision_fanod as fecha_emision,ruc_comercial_recli,razon_social_recli, "+
					" establecimiento_operativo_recli, observaciones_facob as observacion, ide_tecca  "+
					" FROM fac_cobro fc "+
					" left join rec_tipo rt on rt.ide_retip=fc.ide_retip "+
					" join fac_nota_debito nd on nd.ide_fanod=fc.ide_fanod "+
					" join rec_clientes cli on cli.ide_recli=nd.ide_recli";
		
		String sql_c=" SELECT ide_facob,'CONTRATO' as documento, rt.detalle_retip as forma_cobro,  "+
				" fecha_cobro_facob,nro_documento_facob, coalesce(valor_cobro_facob,0) + coalesce(valor_cobro_iva_facob,0) as cobro, "+
				" cliente_pago_facob,documento_cobro as cheque_transf,lpad(numero_contrato_prcon,4,'0') as secuencial,fecha_firma_prcon as fecha_emision,ruc_comercial_recli,razon_social_recli, "+
				" establecimiento_operativo_recli, observaciones_facob as observacion, ide_tecca  "+
				" FROM fac_cobro fc "+
				" left join rec_tipo rt on rt.ide_retip=fc.ide_retip "+
				" join pre_contrato crt on crt.ide_prcon=fc.ide_prcon "+
				" join rec_clientes cli on cli.ide_recli=crt.ide_recli ";
				
		
			/*if(tipo.equals("1")){ 
				sql+=" where fecha_cobro_facob between '"+fecha_inicial+"' and '"+fecha_final+"'";
			}*/
		
			if(tipo.equals("-1")){ //todos
				sql+=sql_f +" union all "+ sql_nd +" union all "+ sql_c;
			}
			
			if(tipo.equals("1")){ //facturas
				sql+=sql_f;
			}
			
			if(tipo.equals("2")){ //notas debito
				sql+=sql_nd;
			}
			
			if(tipo.equals("3")){ //contratos
				sql+=sql_c;
			}
			
			sql+=" ) a ";
			
			if( pckUtilidades.CConversion.CInt(nro_documento)!=0)
			{
				sql+=" where ide_tecca="+nro_documento;
			}
			
			if(tipo.equals("-1")){ 
				sql+=" limit 0";
			}
			
		//System.out.println("getCierreCajaDet: "+sql);
		return sql;
		
	}
	

    public String comprobantesPagos(String fecha_inicial,String fecha_final){

	    String comprobantes_pagos="select ejercicio,entidad,no_registro,no_original,clase_registro,tcp,ruc,descripcion,estado,solicitud_pago,pagado_total,monto_gasto,fecha_registro,nombre_beneficiario"
			+" ,ide_prpoa,detalle_programa,"
			+" detalle_proyecto,"
			+" detalle_actividad,"
			+" detalle_subactividad,"
			+" codigo_clasificador_prcla, descripcion_clasificador_prcla,"
			+" codigo_fuente_prfuf, "
			+" fuente,"
			+" sum(devengado_prmen) as devengado,"
			+" sum(pagado_prmen) as pagado,"
			+" subtotal_tecpo,"
			+" valor_iva_tecpo,"
			+" valor_retencion_tecpo,"
			+" valor_pago_tecpo"
			
			+" from (";
			
			
			
			comprobantes_pagos+=" SELECT distinct extract(year from fecha_tecpo) as ejercicio, "
					+" cast('EMGIRS-EP' as character(25)) as entidad,"
					+" cop.ide_tecpo as no_registro,"
					+" comprobante_egreso_tecpo as no_original,"
					+" detalle_tetic as clase_registro,"
					+" cop.ide_comov as tcp,"
					+" case when cop.ide_tepro is null then ruc_emp else ruc_tepro end ruc,"
					+" detalle_tecpo as descripcion,"
					+" detalle_coest as estado, "
					+" 'N' as solicitud_pago,"
					+" 'N' as pagado_total,"
					+" valor_compra_tecpo as monto_gasto,"
					+" fecha_tecpo as fecha_registro,"
					+" case when cop.ide_tepro is null then NOMBRES else nombre_tepro end nombre_beneficiario,"
					+" po.ide_prpoa,"
					+" detalle_programa,"
					+" detalle_proyecto,"
					+" detalle_actividad,"
					+" detalle_subactividad,"
					+" codigo_clasificador_prcla, descripcion_clasificador_prcla,"
					+" codigo_fuente_prfuf, "
					+" detalle_prfuf as fuente,"
					+" devengado_prmen,"
					+" pagado_prmen,"
					+" subtotal_tecpo,"
					+" valor_iva_tecpo,"
					+" valor_retencion_tecpo,"
					+" valor_pago_tecpo"
					
					+" FROM public.tes_comprobante_pago cop"
					
					+" left join tes_tipo_concepto tp on tp.ide_tetic=cop.ide_tetic"
					+" left join cont_estado est on est.ide_coest=cop.ide_coest"
					
					+" left join (select b.ide_tepro, coalesce(ruc_representante_tepro,ruc_tepro) as ruc_tepro,substring(nombre_tepro from 1 for 30) as nombre_tepro, codigo_banco_geins, numero_cuenta_tepcb,codigo_gttcb"
					+" 		 from tes_proveedor b"
					+" 		 left join tes_proveedor_cuenta_bancaria c on b.ide_tepro=c.ide_tepro"
					+" 		 left join gen_institucion d on c.ide_geins = d.ide_geins"
					+" 		 left join gth_tipo_cuenta_bancaria e on c.ide_gttcb = e.ide_gttcb) prov on prov.ide_tepro=cop.ide_tepro"
					
					+" left join (select par.ide_geedp,substring (APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || PRIMER_NOMBRE_GTEMP || ' '" 
					+" 		|| (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP  end)  from 1 for 30) AS NOMBRES,"
					+" 		CODIGO_GTTCB, DOCUMENTO_IDENTIDAD_GTEMP as ruc_emp,NUMERO_CUENTA_GTCBE,CODIGO_BANCO_GEINS "
					+" 		from GEN_EMPLEADOS_DEPARTAMENTO_PAR par "
					+" 		left join GTH_EMPLEADO emp on par.ide_gtemp=emp.ide_gtemp" 
					+" 		left join GTH_CUENTA_BANCARIA_EMPLEADO cuenta on cuenta.IDE_GTEMP=emp.IDE_GTEMP  and ACREDITACION_GTCBE=true" 
					+" 		left join GEN_INSTITUCION insti on insti.IDE_GEINS=cuenta.IDE_GEINS  "
					+" 		left join GTH_TIPO_CUENTA_BANCARIA tipoc on cuenta.IDE_GTTCB=tipoc.IDE_GTTCB ) emp on emp.ide_geedp=cop.ide_geedp"
					
					+" left join cont_detalle_movimiento dmov on dmov.ide_comov=cop.ide_comov"
					+" join pre_mensual m on m.ide_codem=dmov.ide_codem"
					+" join pre_anual pa on pa.ide_pranu=m.ide_pranu"
					+" join pre_poa po on po.ide_prpoa=pa.ide_prpoa"
					
					+" left join (SELECT pc.ide_prcla, pc.codigo_clasificador_prcla, pc.descripcion_clasificador_prcla, "
					+" 		pc2.codigo_clasificador_prcla || ' ' || pc2.descripcion_clasificador_prcla as grupo_gasto, "
					+" 		pc3.codigo_clasificador_prcla || ' ' || pc3.descripcion_clasificador_prcla as tipo_gasto "
					+" 		FROM public.pre_clasificador pc "
					+" 		left join pre_clasificador pc1 on pc1.ide_prcla=pc.pre_ide_prcla "
					+" 		left join pre_clasificador pc2 on pc2.ide_prcla=pc1.pre_ide_prcla "
					+" 		left join pre_clasificador pc3 on pc3.ide_prcla=pc2.pre_ide_prcla) pc on pc.ide_prcla = po.ide_prcla   "
							
					+" join (" +serv_presupuesto.getUbicacionPOA()+") poa on poa.ide_prfup=po.ide_prfup "
					
					+" left join (select sum(valor_financiamiento_prpof) as codificado, ide_prpoa, pf.ide_prfuf,codigo_fuente_prfuf,detalle_prfuf from pre_poa_financiamiento  pf"     
					+" 		  join pre_fuente_financiamiento ff on ff.ide_prfuf=pf.ide_prfuf    "
					+" 		  group by ide_prpoa, pf.ide_prfuf,codigo_fuente_prfuf,detalle_prfuf) pf on pf.ide_prpoa = po.ide_prpoa" 
					
					
					+" where cop.fecha_tecpo between '"+fecha_inicial+"' and '"+fecha_final+"' "
					+" ORDER BY cop.ide_tecpo ";
					
			comprobantes_pagos+=") a"

			+" group by ejercicio,entidad,no_registro,no_original,clase_registro,tcp,ruc,descripcion,estado,solicitud_pago,pagado_total,monto_gasto,fecha_registro,nombre_beneficiario"
			+" ,ide_prpoa,detalle_programa,"
			+" detalle_proyecto,"
			+" detalle_actividad,"
			+" detalle_subactividad,"
			+" codigo_clasificador_prcla, descripcion_clasificador_prcla,"
			+" codigo_fuente_prfuf, "
			+" fuente,"
			+" subtotal_tecpo,"
			+" valor_iva_tecpo,"
			+" valor_retencion_tecpo,"
			+" valor_pago_tecpo";
					
		System.out.println("consulta comprobantesPagos: "+ comprobantes_pagos);
		return comprobantes_pagos;
	}
    
public String getComprobantesCUR(String ide_facur){
		
		String sql="select ide_facob,documento,forma_cobro,fecha_cobro_facob,nro_documento_facob,cobro,secuencial,"+
				 " fecha_emision,ruc_comercial_recli as ruc_comercial,razon_social_recli as razon_social,establecimiento_operativo_recli as establecimiento_operativo, observacion, ide_facur "+
				 " from ( ";
		
		String sql_f=" SELECT ide_facob,'FACTURA' as documento, rt.detalle_retip as forma_cobro, "+
					" fecha_cobro_facob,nro_documento_facob, coalesce(valor_cobro_facob,0)  + coalesce(valor_cobro_iva_facob,0) as cobro,"+
					" cliente_pago_facob,documento_cobro as cheque_transf,secuencial_fafac as secuencial,fecha_transaccion_fafac as fecha_emision,ruc_comercial_recli,razon_social_recli,"+
					" establecimiento_operativo_recli, observaciones_facob as observacion, ide_facur"+
					" FROM fac_cobro fc"+
					" left join rec_tipo rt on rt.ide_retip=fc.ide_retip"+
					" join fac_factura fac on fac.ide_fafac=fc.ide_fafac"+
					" join rec_clientes cli on cli.ide_recli=fac.ide_recli ";
		
		String sql_nd=" SELECT ide_facob,'NOTA DEBITO' as documento, rt.detalle_retip as forma_cobro,  "+
					" fecha_cobro_facob,nro_documento_facob, coalesce(valor_cobro_interes_facob,0) as cobro, "+
					" cliente_pago_facob,documento_cobro as cheque_transf,nro_nota_debito_elect_fanod as secuencial,fecha_emision_fanod as fecha_emision,ruc_comercial_recli,razon_social_recli, "+
					" establecimiento_operativo_recli, observaciones_facob as observacion, ide_facur  "+
					" FROM fac_cobro fc "+
					" left join rec_tipo rt on rt.ide_retip=fc.ide_retip "+
					" join fac_nota_debito nd on nd.ide_fanod=fc.ide_fanod "+
					" join rec_clientes cli on cli.ide_recli=nd.ide_recli";
		
			sql+=sql_f +" union all "+ sql_nd;				
			sql+=" ) a ";
			
			
		    sql+=" where ide_facur="+ide_facur;
			
			
		System.out.println("getComprobantesCUR: "+sql);
		return sql;
		
	}
    

}