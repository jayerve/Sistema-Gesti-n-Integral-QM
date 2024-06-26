package paq_juridico.ejb;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Utilitario;

/**
 * Session Bean implementation class ServicioBodega
 */

@Stateless
@LocalBean

public class ServicioJuridico {

	/**
	 * Default constructor. 
	 */
	private Utilitario utilitario=new Utilitario();
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion) utilitario.instanciarEJB(ServicioFacturacion.class);

	public String getSqlCoactivas(String cliente,String tipo,String fecha_inicial,String fecha_final){
		
		String tab_coactivas="SELECT jco.ide_jucoa as codigo,nro_juicio_jucoa as nro_proceso, razon_social_recli, ruc_comercial_recli, nro_titulo_facon, fecha_titulo_facon,cuantia_jucoa, coalesce(monto_total,0)+coalesce(interes_generado,0) as monto_obligacion, "
				+ "cuantia_jucoa as monto_conInteres, coalesce(monto_pagado,0)+coalesce(interes_pagado,0) as monto_entregado,coalesce(monto_pendiente,0)+coalesce(interes_pendiente,0) as monto_pendiente, "
				+ "coalesce(monto_anulado,0)+coalesce(interes_anulado,0) as monto_anulado,coalesce(interes_pendiente,0) as intereses_corte, "
				+ "detalle_juepr as estado_actual_proceso,observacion_jucoa, dias_vencidos_jucoa "
				+ "FROM jur_coactiva jco  "
				+ "left join rec_clientes cli on cli.ide_recli=jco.ide_recli  "
				+ "left join jur_etapa_procesal etp on etp.ide_juepr=jco.ide_juepr   "
				+ "left join fac_convenio ft on ft.ide_facon=jco.ide_facon "
				+ "left join (select ide_facon, sum(total_fafac) as monto_total "
				+ "		, sum(case when ide_coest=1 then total_fafac else 0 end) as monto_anulado "
				+ "		, sum(case when ide_coest in (2,24) then total_fafac else 0 end) as monto_pendiente "
				+ "		, sum(case when ide_coest in (16,30) then total_fafac else 0 end) as monto_pagado "
				+ "		from fac_detalle_convenio fdc "
				+ "		join fac_factura fac on fac.ide_fafac=fdc.ide_fafac  "
				+ "		group by ide_facon) mc on mc.ide_facon=ft.ide_facon "
				+ " "
				+ "left join (select ide_facon, sum(interes_generado_fanod) as interes_generado, sum(case when ide_coest=1 then interes_generado_fanod else 0 end) as interes_anulado "
				+ "		, sum(case when ide_coest=2 then interes_generado_fanod else 0 end) as interes_pendiente  "
				+ "		, sum(case when ide_coest=16 then interes_generado_fanod else 0 end) as interes_pagado  "
				+ "		from fac_detalle_convenio fdc "
				+ "		join fac_nota_debito nd on nd.ide_fanod=fdc.ide_fanod  "
				+ "		group by ide_facon) ig on ig.ide_facon=ft.ide_facon "
				+ "where coalesce(activo_jucoa,false) in (true,false)   ";	
					
		if(tipo.equals("0")){
			tab_coactivas +=" and jco.ide_recli= "+cliente;
		}
		
		if(tipo.equals("1")){
			tab_coactivas +=" and fecha_jucoa between '"+fecha_inicial+"' and '"+fecha_final+"' ";
		}
		
		tab_coactivas +=" order by fecha_jucoa ";
		
		System.out.println("tab_coactivas "+tab_coactivas);
		return tab_coactivas;
		
	}
	
	public String getCarteraClientes(String cliente,String tipo){
		//tipo 1 todos, 0 cliente
		
		String tab_cliente=" select row_number() over(order by secuencial_fafac,detalle_bogrm) as codigo,ide_fafac,ide_recli,ruc_comercial_recli,razon_social_recli,establecimiento,substring(detalle_bogrm from 1 for 35)::varchar(60) as detalle_bogrm,secuencial_fafac,fecha_vencimiento_fafac,total_fafac,grupo,interes" 
				+"  from ( "
				 +" select ide_fafac,ide_recli,ruc_comercial_recli,razon_social_recli,detalle_bogrm,secuencial_fafac,total_fafac,1 as grupo, 0 as interes,fecha_vencimiento_fafac ,establecimiento "
				  +" from ( select ide_fafac,ide_fadaf,secuencial_fafac,total_fafac,f.ide_recli,ide_coest,fecha_vencimiento_fafac ,ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli as establecimiento from fac_factura f, rec_clientes c where f.ide_recli=c.ide_recli ) a ,"
				 
				  +" ( select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, 'FACTURA: ' || detalle_bogrm as detalle_bogrm "
				  +" from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm  "
				  +" order by autorizacion_sri_bogrm ) b "
				  +" where a.ide_fadaf = b.ide_fadaf ";
		
		if(tipo.equals("0"))
			tab_cliente += " and ruc_comercial_recli='"+cliente+ "'";
		
		tab_cliente += " and ide_coest in ((select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido'),24)"  
				 +" union "
				  +" select ide_fanod,nd.ide_recli,ruc_comercial_recli,razon_social_recli,'NOTA DE DEBITO',detalle_fenod,0,2 as grupo,interes_generado_fanod,fecha_emision_fanod, establecimiento_operativo_recli as establecimiento"  
				  +" from fac_nota_debito nd, rec_clientes c where nd.ide_recli=c.ide_recli and ide_coest = (select cast(valor_para as integer) from sis_parametros where nom_para ='p_factura_emitido') ";
		
		if(tipo.equals("0"))
			tab_cliente += " and ruc_comercial_recli='"+cliente+ "'";
			
		
		tab_cliente += " ) a  "
				+"  order by secuencial_fafac,detalle_bogrm";
			
		if(tipo.equals("1"))
		{
			tab_cliente = "select row_number() over(order by ruc_comercial_recli,razon_social_recli) as codigo,0 as ide_fafac,0 as ide_recli,ruc_comercial_recli,razon_social_recli,'' as establecimiento,'' as detalle_bogrm,'' as secuencial_fafac,'' as fecha_vencimiento_fafac,sum(total_fafac) as total_fafac,'' as grupo, sum(interes) as interes from (" + tab_cliente;
			tab_cliente += " ) b group by ruc_comercial_recli,razon_social_recli order by ruc_comercial_recli,razon_social_recli ";
		}

		System.out.println("consulta getCarteraClientes: "+ tab_cliente);
		return tab_cliente;
	}
	
	public String getNotificacionClientes(){

		String tab_notificacion="select distinct ruc_comercial_recli as codigo, ruc_comercial_recli,razon_social_recli,sum(saldo) as saldo,sum(interes) as interes,sum(total) as total"
				+ ",noti1.nro_notificacion_junot as nro_1,noti1.notificacion_junot as notificacion_1 "
		        + ",noti2.nro_notificacion_junot as nro_2,noti2.notificacion_junot as notificacion_2 "
		        + ",noti3.nro_notificacion_junot as nro_3,noti3.notificacion_junot as notificacion_3 ";
		
		tab_notificacion+=" from (";
		
		tab_notificacion+=ser_facturacion.getComprobantesContabilizados(0,0);
		
		tab_notificacion+=" ) aa "
				+ " left join jur_notificacion noti1 on noti1.ruc_junot=aa.ruc_comercial_recli and noti1.tipo_notificacion_junot=1 "
				+ " left join jur_notificacion noti2 on noti2.ruc_junot=aa.ruc_comercial_recli and noti2.tipo_notificacion_junot=2 "
				+ " left join jur_notificacion noti3 on noti3.ruc_junot=aa.ruc_comercial_recli and noti3.tipo_notificacion_junot=3 "
				+ " where dias_retraso>80 "
				+ " group by ruc_comercial_recli,razon_social_recli"
				+ " ,noti1.nro_notificacion_junot,noti1.notificacion_junot "
				+ " ,noti2.nro_notificacion_junot,noti2.notificacion_junot "
				+ " ,noti3.nro_notificacion_junot,noti3.notificacion_junot "
				+ " order by ruc_comercial_recli ";

		System.out.println("consulta getNotificacionClientes: "+ tab_notificacion);
		return tab_notificacion;
	}
	
	public String getNotificacionClientesComprobantes(String ruc)
	{
		String tab_notificacion="select *  ";
		
		tab_notificacion+=" from (";
		
		tab_notificacion+=ser_facturacion.getComprobantesContabilizados(0,0);
		
		tab_notificacion+=" ) aa where dias_retraso>80 and ruc_comercial_recli like '"+ruc+"' "
				+ "order by ruc_comercial_recli ";

		System.out.println("consulta getNotificacionClientes: "+ tab_notificacion);
		return tab_notificacion;
	}

}
