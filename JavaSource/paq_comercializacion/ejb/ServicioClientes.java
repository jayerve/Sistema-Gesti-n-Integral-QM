package paq_comercializacion.ejb;

import java.util.List;

import javax.ejb.Stateless;

import paq_sistema.aplicacion.Utilitario;
import framework.aplicacion.TablaGenerica;


/**
 * Session Bean implementation class ServicioCliente
 */
@Stateless

public class ServicioClientes {
	private Utilitario utilitario=new Utilitario();

	
	public ServicioClientes() {
		// TODO Auto-generated constructor stub
	}

	
	//busca clientes por ruc y 
	public String getClientesRuc(String ruc ){
	    String tab_cliente="select a.ide_recli, ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli," +
	            "  nro_establecimiento_recli, nombre_comercial_recli, direccion_recld from rec_clientes a " +
	            " LEFT JOIN rec_cliente_direccion b on a.ide_recli=b.ide_recli where ruc_comercial_recli in ("+utilitario.generarComillaSimple(ruc)+")" +
	                " and activo_recli = true ORDER BY  nombre_comercial_recli";
	    return tab_cliente;
	}
	
	// busca clientes por ruc y por Matriz o Sucursal
	public String getClientesRucMoS(String ruc, String tipo ){
	    String tab_cliente="select a.ide_recli, ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli," +
	            "  nro_establecimiento_recli, nombre_comercial_recli, direccion_recld from rec_clientes a " +
	            " LEFT JOIN rec_cliente_direccion b on a.ide_recli=b.ide_recli where ruc_comercial_recli in ("+utilitario.generarComillaSimple(ruc)+")" +
	            " and matriz_sucursal_recli = "+tipo+" and activo_recli = true ORDER BY  nombre_comercial_recli";
	    return tab_cliente;
	}
	
	// busca clientes por ide_recli y por Sucursal
	public TablaGenerica getTablaGenericaSucursal(String ide_recli){
		
	    String sql="select a.ide_recli, ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli," +
	            "  nro_establecimiento_recli,nombre_comercial_recli, " +
	            "  direccion_recld from rec_clientes a " +
	            " LEFT JOIN rec_cliente_direccion b on a.ide_recli=b.ide_recli where a.rec_ide_recli in ("+ide_recli+")" +
	                " and matriz_sucursal_recli=0 ORDER BY  nombre_comercial_recli";

		 TablaGenerica tab_cliente_sucursal =utilitario.consultar(sql);
		 return tab_cliente_sucursal;
		 
	 }	
	
	public TablaGenerica getTablaGenericaClienteRuc(String ruc){
		
	    String sql="select a.ide_recli, ruc_comercial_recli,razon_social_recli,nombre_comercial_recli," +
	            "  establecimiento_operativo_recli,nro_establecimiento_recli, " +
	            "  direccion_recld from rec_clientes a " +
	            " LEFT JOIN rec_cliente_direccion b on a.ide_recli=b.ide_recli where ruc_comercial_recli in ("+utilitario.generarComillaSimple(ruc)+")" +
	                " ORDER BY  nombre_comercial_recli";

		 TablaGenerica tab_cliente_ruc =utilitario.consultar(sql);
		 return tab_cliente_ruc;
		 
	 }	
	
	public TablaGenerica getTablaGenericaCliente(String ide_recli){
		
	    String sql="select a.ide_recli, ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli," +
	            "  nro_establecimiento_recli,nombre_comercial_recli, " +
	            "  direccion_recld from rec_clientes a " +
	            " LEFT JOIN rec_cliente_direccion b on a.ide_recli=b.ide_recli where a.ide_recli in ("+ide_recli+")" +
	                " ORDER BY  nombre_comercial_recli";

		 TablaGenerica tab_cliente_ruc =utilitario.consultar(sql);
		 return tab_cliente_ruc;
		 
	 }	

	public String getClientes(String matrizSucursal ){
	    String tab_cliente="select a.ide_recli, ruc_comercial_recli,razon_social_recli,establecimiento_operativo_recli," +
	            "  nro_establecimiento_recli,nombre_comercial_recli, " +
	            "  direccion_recld from rec_clientes a " +
	            " LEFT JOIN rec_cliente_direccion b on a.ide_recli=b.ide_recli where MATRIZ_SUCURSAL_RECLI in ("+matrizSucursal+")" +
	                " and activo_recli = true ORDER BY  nombre_comercial_recli";
	    return tab_cliente;
	}

	public void borrarClienteMatriz(String ide_recli){
		String sql ="update rec_clientes set activo_recli = false"
					+ " where ide_recli in ("+ide_recli+" ) and matriz_sucursal_recli = 1 and activo_recli = true;";
		utilitario.getConexion().ejecutarSql(sql);
		System.out.println("actualizarCliente: "+sql);
	}

	public void borrarClienteSucursal(String ide_recli){
		String sql ="update rec_clientes set activo_recli = false"
					+ " where ide_recli in ("+ide_recli+" ) and matriz_sucursal_recli = 0 and activo_recli = true;";
		utilitario.getConexion().ejecutarSql(sql);
		System.out.println("actualizarCliente: "+sql);
	}

	// actualizo el campo rec_ide_recli con el ide_recli de la matriz que queda 
	public void actualizoSucursal(String ide_recli, long rec_ide_recli){
		String sql ="update rec_clientes set rec_ide_recli = "+rec_ide_recli+
					" where ide_recli in ("+ide_recli+" ) and matriz_sucursal_recli = 0 and activo_recli = true;";
		utilitario.getConexion().ejecutarSql(sql);
		System.out.println("actualizarCliente: "+sql);
	}

	
	//busca clientes por id_recli
	public String getTodoCliente(int ide_recli ){
	    String tab_cliente="select * from rec_clientes " +
	            "  where ide_recli in ("+ide_recli+")" +
	                " and activo_recli = true ";
	    return tab_cliente;
	}

	//maximo de la matriz
		public int getMaxMatriz(String ide_recli ){
		    String sql="select max(ide_recli)as ide_recli from rec_clientes " +
		            "  where ide_recli in ("+ide_recli+")" +
		                " and activo_recli = true ";
		    List tab_consulta = utilitario.getConexion().consultar(sql);
		    
//		    long max_ide_recli= Long.parseLong(tab_consulta.get(0)+""); 
		    int max_ide_recli= Integer.parseInt(tab_consulta.get(0)+"");
    		return max_ide_recli; 
		}

		
	// busca todos los clientes por ide_recli
	public TablaGenerica getTodoCliente(String ide_recli){
		
	    String sql="select * from rec_clientes where activo_recli = true "+
	            " and ide_recli in ("+ide_recli+")" +
	                " ORDER BY  nombre_comercial_recli";
		 TablaGenerica tab_cliente_id =utilitario.consultar(sql);
		 
		 System.out.println("el sql " +sql);
		 return tab_cliente_id;
		 
	 }	

	public boolean validarRuc(String ide_gttdi,String documento_identidad) {

		String str_sql="Select * from rec_clientes where nro_establecimiento_recli=1 and ruc_comercial_recli="+ utilitario.generarComillaSimple(documento_identidad) + " and activo_recli=true "; 
	
		System.out.println("validarRuc "+str_sql);
		//Asi se hacen consultas a la BDD
		TablaGenerica tab_consulta=utilitario.consultar(str_sql);

		//Preguntamos si la tabla no esta vacia es decir que si retorno un resultado la consulta
		if ( !tab_consulta.isEmpty()) {
			//Obtenemos el valor del campo y lo almacenamos en un String
			//String str_aplica_valor_ruc= tab_consulta.getValor("ruc_comercial_recli");
//			utilitario.agregarMensajeInfo("Atencion",
//					"El numero de RUC/C.I. ingresado, ya EXISTE");
			if(tab_consulta.getTotalFilas()>1)
				return true; //
		}
		return false;  //retorna false
	}
	
// valida si existe un número de establecimiento repetido	
	public boolean validarSucursalEstablecimiento(String ruc_comercial_recli, int int_nro_establecimiento) {

		String str_sql="Select * from rec_clientes where ruc_comercial_recli = " + utilitario.generarComillaSimple(ruc_comercial_recli) +" and" 
				+ " nro_establecimiento_recli="+int_nro_establecimiento + " and activo_recli=true"; 
	
		//Asi se hacen consultas a la BDD
		TablaGenerica tab_consulta=utilitario.consultar(str_sql);

		//Preguntamos si la tabla no esta vacia es decir que si retorno un resultado la consulta
		if ( !tab_consulta.isEmpty()) {
			//Obtenemos el valor del campo y lo almacenamos en un String
			// String str_nro_establecimiento= tab_consulta.getValor("nro_establecimiento_recli");
			if(tab_consulta.getTotalFilas()>1)
			{
			    //utilitario.agregarMensajeInfo("Atención", "El numero Sucursal, ya EXISTE");
				return true; //
			}
		}
		return false;  //retorna false
	}
	
/**
 * Metodo para validar el establecimiento 
 * @param valor
 * @param parseInt
 * @return
 */

	public boolean validarNroEstablecimiento(String ide_recli, String nro_establecimiento) {

	String str_sql="Select * from rec_clientes_establecimiento where ide_recli = "+ide_recli+ 
			 " and nro_establecimiento_reest="+nro_establecimiento + " and activo_reest=true"; 

	//Asi se hacen consultas a la BDD
	TablaGenerica tab_consulta=utilitario.consultar(str_sql);

	//Preguntamos si la tabla no esta vacia es decir que si retorno un resultado la consulta
	if ( tab_consulta.isEmpty()==false) {
		//Obtenemos el valor del campo y lo almacenamos en un String
		 String str_nro_establecimiento= tab_consulta.getValor("nro_establecimiento_reest");
		utilitario.agregarMensajeInfo("Atención","El numero de Establecimiento, ya EXISTE");
			return true; //
	}
	return false;  //retorna false
}

	public boolean validarMatrizEstablecimiento(String ide_recli, String nro_sucursal) {

		String str_sql="Select * from rec_clientes_establecimiento where ide_recli = "+ide_recli+ 
				 " and matriz_sucursal_reest="+nro_sucursal+" and activo_reest=true"; 

		//Asi se hacen consultas a la BDD
		TablaGenerica tab_consulta=utilitario.consultar(str_sql);

		//Preguntamos si la tabla no esta vacia es decir que si retorno un resultado la consulta
		if ( tab_consulta.isEmpty()==false) {
			//Obtenemos el valor del campo y lo almacenamos en un String
			 String str_matriz= tab_consulta.getValor("matriz_sucursal_reest");
			 if (str_matriz.equals("1")){
			utilitario.agregarMensajeInfo("Atención","Existe una Matriz");
				return true; //}
		}
		return false;  //retorna false
	}
		
		return false;
	}
	
	
	public TablaGenerica ConsultaCliente(){
		TablaGenerica tab_cliente=utilitario.consultar("select ide_recli, razon_social_recli,ruc_comercial_recli,"
						+ "CASE activo_recli when 'true' then 'ACTIVO'   else 'INACTIVO'  end as activo_recli"
						+ "from rec_clientes where activo_recli=true and matriz_sucursal_recli=1 order by ide_recli;");
	    return tab_cliente;		
	}
	
	
//--------------------------------------	
/**
 * 
 * @param cliente
 * @param tipo
 * @param fecha_inicial
 * @param fecha_final
 * @param aplica_fecha
 * @return
 */
	public String getConsultaClientes(String cliente,String tipo,String fecha_inicial,String fecha_final,String aplica_fecha){
		//tipo 1 todos, 0 cliente
		//System.out.println("chequea gfechas "+aplica_fecha);
		String factura_clientes="select a.ide_recli, a.ruc_comercial_recli, f1.fecha_transaccion_fafac, f1.total_fafac,"
								+"a.nombre_comercial_recli AS nombre_comercial,   a.nombre_comercial_recli,"
								+"COALESCE(a.razon_social_recli, a.nombre_comercial_recli) AS nombre_comercial_factura,"
								+"a.codigo_zona_recli,    COALESCE(a.direccion_recli, ''::character varying) AS direccion_recli,"
								+"COALESCE(a.telefono_factura_recli, ''::character varying) AS telefono_factura, COALESCE(a.email_recli, ''::character varying) AS email,"
								+"pre_con.numero_contrato_prcon, pre_con.fecha_inicio_prcon, b.detalle_bogrm, b.ide_bogrm, a.activo_recli "  
								+"from fac_factura f1 "
								+"LEFT JOIN ( SELECT mc.ide_recli,mc.telefono_factura_recli,  mc.razon_social_recli,      mc.ruc_comercial_recli"   
                                +",b_1.numero_contrato_prcon, b_1.fecha_inicio_prcon  "
                                +" FROM rec_clientes mc "
                                +" JOIN ( SELECT pre_contrato.ide_recli,  pre_contrato.numero_contrato_prcon,"
                                +"pre_contrato.fecha_inicio_prcon "
                                +"FROM pre_contrato "
                                +"WHERE pre_contrato.activo_prcon = true) b_1 ON b_1.ide_recli = mc.ide_recli "
                                +"WHERE mc.matriz_sucursal_recli = 1 AND mc.activo_recli = true ) pre_con ON pre_con.ide_recli = f1.ide_recli "
                                +"left join ( select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, detalle_bogrm, a.ide_bogrm "	
                    			+"from fac_datos_factura a, bodt_grupo_material b	where a.ide_bogrm = b.ide_bogrm " 	
                    			+"order by autorizacion_sri_bogrm ) b ON f1.ide_fadaf = b.ide_fadaf " 
                                +" left join rec_clientes a on a.ide_recli=f1.ide_recli  "
                                +" where f1.ide_fafac in (select max(ide_fafac) from fac_factura group by ide_recli) ";
								if(aplica_fecha.equals("true")){
									factura_clientes +=" and pre_con.fecha_inicio_prcon between '"+fecha_inicial+"' and '"+fecha_final+"' ";
								}
								else
								{
//									factura_clientes +=" and f1.fecha_transaccion_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' ";
								}
								
								if (tipo.equals("0")){
									factura_clientes += " ";
								}else{
									factura_clientes += " and b.ide_bogrm in ("+tipo+") ";
								}
									
						factura_clientes +=" order by a.ide_recli, a.nombre_comercial_recli";
		//System.out.println("consulta facturas tesoreria: "+ factura_clientes);
		return factura_clientes;
	}
/**
 * metodo para buscar clientes con contrato y la ultima factura
 * CC 02 06 2017
 * @param cliente
 * @param punto_venta
 * @param fecha_inicial
 * @param fecha_final
 * @param aplica_fecha
 * @param aplica_fec_prcon
 * @return
 */
	/*public String getClientesFacturasContratos(String cliente, String punto_venta, String fecha_inicial,String fecha_final,String aplica_fecha, String aplica_fec_prcon){
		
		String factura_clientes=" select cli.ide_recli, cli.fac_mensual_recli as FACTURA_MENSUAL, CASE cli.estado_recli WHEN 'true' THEN 'PUBLICO' WHEN 'false' THEN 'PRIVADO' ELSE 'OTRO' END AS TIPO," 
				+"CASE ide_retic WHEN 1 THEN 'PERSONA NATURAL'   WHEN 2 THEN 'PERSONA JURIDICA'   ELSE 'SOCIEDADES' END AS TIPO_CONTRIBUYENTE, "
				+"cli.ruc_comercial_recli, cli.razon_social_recli, cli.representante_legal_recli, cli.nombre_comercial_recli, gen.detalle_gedip AS PARROQUIA, " 
				+"cli.direccion_recli,cli.telefono_factura_recli,cli.email_recli, tia.detalle_retia AS FRECUENCIA, rcli.detalle_reclr AS RUTA, cli.estimado_desecho_recl, "
				+"mat.detalle_bogrm, tem.detalle_tipcli, prec.numero_contrato_prcon, prec.fecha_inicio_prcon, fac.ide_fafac,fac.fecha_transaccion_fafac,ROUND(fac.total_fafac,2) AS VALOR_FACTURA, "
				+"CASE cli.activo_recli WHEN 'true' THEN 'ACTIVO'  WHEN 'false' THEN 'INACTIVO'   ELSE 'OTRO' END AS ESTADO, cli.ide_bogrm  "
				+"from rec_clientes cli "
				+"LEFT JOIN gen_division_politica gen ON gen.ide_gedip = cli.ide_gedip "
				+"LEFT JOIN rec_cliente_ruta rcli ON rcli.ide_reclr = cli.ide_reclr "
				+"LEFT JOIN rec_tipo_asistencia tia ON cli.ide_retia = tia.ide_retia "
				+"left join bodt_grupo_material mat on cli.ide_bogrm = mat.ide_bogrm "
				+"LEFT JOIN rec_tipo_cliente_emgirs tem ON tem.ide_tipcli=cli.ide_tipcli "
				+"LEFT JOIN pre_contrato prec ON prec.ide_recli=cli.ide_recli "
				+"left join (select ide_recli,max(ide_fafac) as ide_fafac from fac_factura fac "
				+"group by  ide_recli ) frec on frec.ide_recli=cli.ide_recli "
				+"left join fac_factura fac on fac.ide_fafac=frec.ide_fafac where cli.activo_recli in ('true', 'false')  ";
				if(aplica_fecha.equals("true")){
					factura_clientes +=" and fecha_pago_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' ";
					}
				if(aplica_fec_prcon.equals("true")){
					factura_clientes +=" and fecha_inicio_prcon between '"+fecha_inicial+"' and '"+fecha_final+"' ";	
					
				}
				if (punto_venta.equals("0")){
					factura_clientes += " ";
				}else{
					factura_clientes += " and cli.ide_bogrm in ("+punto_venta+") ";
				}
	return factura_clientes;
}*/
	
	public String getClientesFacturasContratos(String cliente, String punto_venta, String fecha_inicial,String fecha_final,String aplica_fecha, String aplica_fec_prcon){
		
		String factura_clientes=" ";
		
		if(aplica_fecha.equals("true")){
			factura_clientes +=" select TIPO,ruc_comercial_recli,SERVICIO_FACTURADO,SUBTOTAL_ACUMULADO,IVA_ACUMULADO,TOTAL_ACUMULADO,INTERES_ACUMULADO,TOTAL_VENTA_SIN_IVA,KG_M3,NUMERO_CONTRATO,TIPO_CONTRATO,FECHA_FACTURA,VALOR_FACTURA,TOTAL_FACTURAS from ( ";
			}
		
		factura_clientes+=" select distinct CASE when cli.estado_recli = true THEN 'PUBLICO' else 'PRIVADO' END AS TIPO, "
			+" cli.ruc_comercial_recli, "
			+" mat.detalle_bogrm as servicio_facturado, "
			+" ROUND(coalesce(frec.sub_acum,0) - coalesce(ncrec.sub_acum,0),2) AS SUBTOTAL_ACUMULADO,"
			+" ROUND(coalesce(frec.iva_acum,0) - coalesce(ncrec.iva_acum,0),2) AS IVA_ACUMULADO,"
			+" ROUND(coalesce(frec.valor_acum,0) - coalesce(ncrec.valor_acum,0),2) AS TOTAL_ACUMULADO,"
			+" ROUND(coalesce(ndrec.sub_acum,0),2) AS INTERES_ACUMULADO,"
			+" ROUND(ROUND(coalesce(frec.sub_acum,0) - coalesce(ncrec.sub_acum,0),2)+ROUND(coalesce(ndrec.sub_acum,0),2),2) AS TOTAL_VENTA_SIN_IVA,"
			
			+" ROUND(case mat.autorizacion_sri_bogrm "
			+"  when '001' then ROUND(frec.sub_acum - coalesce(ncrec.sub_acum,0),2)/1.5 "
			+"  when '002' then ROUND(frec.sub_acum - coalesce(ncrec.sub_acum,0),2)/15.22"
			+"  when '003' then ROUND(frec.sub_acum - coalesce(ncrec.sub_acum,0),2)/0.57"
			+"  when '004' then ROUND(frec.sub_acum - coalesce(ncrec.sub_acum,0),2)/25.83"
			+"  when '005' then ROUND(frec.sub_acum - coalesce(ncrec.sub_acum,0),2)/25.83"
			
			+"  when '006' then ROUND(frec.sub_acum - coalesce(ncrec.sub_acum,0),2)/0.57"
			+"  when '007' then ROUND(frec.sub_acum - coalesce(ncrec.sub_acum,0),2)/0.57"
			+"  when '008' then ROUND(frec.sub_acum - coalesce(ncrec.sub_acum,0),2)/0.57"
			+"  when '010' then ROUND(frec.sub_acum - coalesce(ncrec.sub_acum,0),2)/0.57"
			+"  when '011' then ROUND(frec.sub_acum - coalesce(ncrec.sub_acum,0),2)/0.57"
			      
			+"  else 0 end, 2) as kg_m3,"
			
			+" coalesce(numero_contrato, nro_contrato_recli) as numero_contrato, "
			+" servicio as tipo_contrato, "
			+" fac.fecha_transaccion_fafac as fecha_factura,ROUND(fac.base_aprobada_fafac,2) AS VALOR_FACTURA,"
			+" facturas as total_facturas"
			
			//+" ,mat.autorizacion_sri_bogrm"
			
			+" from rec_clientes cli  "
			
			+" left join (select ide_recli, string_agg(email_recle, ',') as email_recle from rec_cliente_email where notificacion_recle=true and activo_recle=true group by ide_recli ) c on c.ide_recli=cli.ide_recli  "
			+" left join (select ide_recli, string_agg(numero_contrato_prcon, ',') as numero_contrato, string_agg(detalle_prtsc, ',') as servicio from pre_contrato c left join pre_tipo_servicio_contrato s on s.ide_prtsc=c.ide_prtsc where ide_coest in (2,31) group by ide_recli) d on d.ide_recli=cli.ide_recli"    
			
			+" left join (select ruc_comercial_recli, max(cli.ide_recli) as ide_recli, max(ide_fafac) as ide_fafac, "
			+" 	sum(base_aprobada_fafac) as sub_acum, sum(valor_iva_fafac) as iva_acum,"
			+"         sum(total_fafac) as valor_acum , count(fac.ide_fafac) as facturas "
			+" 	from fac_factura fac "
			+" 	join rec_clientes cli on cli.ide_recli=fac.ide_recli "
			+" 	where fac.ide_coest in (1,2,16,24,29,30) and coalesce(activo_fafac,false) in (true,false) "
			+" 	    and ((fac.ide_tetid=4 and coalesce(autorizada_sri_fafac,false)=true) or (fac.ide_tetid=3 and fac.ide_coest not in (1)))";
		
		if(aplica_fecha.equals("true")){
			factura_clientes +=" 	    and fecha_transaccion_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' ";
		}		
			
		factura_clientes +=" 	group by  ruc_comercial_recli ) frec on frec.ide_recli=cli.ide_recli  "
			
			+" left join (select ruc_comercial_recli, max(cli.ide_recli) as ide_recli, sum(valor_referencial_fanoc) as sub_acum, sum(iva_fanoc) as iva_acum,"
			+"         sum(total_fanoc) as valor_acum , count(nc.ide_fanoc) as notasC  		"
			+" 	from fac_nota_credito nc "
			+" 	join fac_factura fac on fac.ide_fafac=nc.ide_fafac	"
			+" 	join rec_clientes cli on cli.ide_recli=fac.ide_recli "
			+" 	where nc.ide_coest in (2) and ((substring(nro_nota_credito_fanoc from 5 for 3) like '011' and coalesce(autorizada_sri_fanoc,false)=true) "
			+" 	       or (substring(nro_nota_credito_fanoc from 5 for 3) not like '011' and coalesce(autorizada_sri_fanoc,false)=false)) ";
		
		if(aplica_fecha.equals("true")){
			factura_clientes +=" 	       and fecha_fanoc between '"+fecha_inicial+"' and '"+fecha_final+"' ";
		}	
			
		factura_clientes +=" 	group by  ruc_comercial_recli ) ncrec on ncrec.ide_recli=cli.ide_recli "
			
			+" left join (select ruc_comercial_recli, max(cli.ide_recli) as ide_recli, sum(interes_generado_fanod) as sub_acum, count(nd.ide_fanod) as notasD "
			+" 	from fac_nota_debito nd "
			+" 	join rec_clientes cli on cli.ide_recli=nd.ide_recli "
			+" 	where ide_coest in (16) and autorizada_sri_fanod=true ";
		
		if(aplica_fecha.equals("true")){
			factura_clientes +=" and fecha_emision_fanod between '"+fecha_inicial+"' and '"+fecha_final+"' ";
		}	
			
		factura_clientes +=" 	group by  ruc_comercial_recli ) ndrec on ndrec.ide_recli=cli.ide_recli" +			
					
			////////////	
				
		" left join fac_factura fac on fac.ide_fafac=frec.ide_fafac  " +
		" left join fac_datos_factura fd on fd.ide_fadaf=fac.ide_fadaf " +
		" left join bodt_grupo_material mat on mat.ide_bogrm = fd.ide_bogrm  " +
		
		" where coalesce(cli.activo_recli,false) in (true, false)  ";


		if(aplica_fec_prcon.equals("true")){
			//factura_clientes +=" and fecha_inicio_prcon between '"+fecha_inicial+"' and '"+fecha_final+"' ";	
			factura_clientes += " ";
		}
		
		if (punto_venta.equals("0")){
			factura_clientes += " ";
		}else{
			factura_clientes += " and mat.ide_bogrm in ("+punto_venta+") ";
		}

		factura_clientes += " order by cli.ruc_comercial_recli ";
				
		if(aplica_fecha.equals("true")){
			factura_clientes +=" ) a where (abs(SUBTOTAL_ACUMULADO)+abs(INTERES_ACUMULADO))>0 ";
		}
				
		System.out.println("factura_clientes "+factura_clientes);		
				
		return factura_clientes;
	}
	
/**
 * Metodo para buscar los tipos de servicios
 * @return
 */
	public String getDatosTiposServicios(){
		String tab_datos_clientes="select ide_tipcli,detalle_tipcli "
                + "from rec_tipo_cliente_emgirs where ide_tipcli is not null "
                + "order by detalle_tipcli";

		return tab_datos_clientes;
	}	
	
	
	public String getClientesAvCorp(){
		
		/*String factura_clientes="select a.ide_recli as codigo, coalesce(numero_contrato,'0') as numero_contrato,    " +
				"coalesce(nro_establecimiento_recli,1) as nro_establecimiento,      " +
				"coalesce(ruc_comercial_recli,'') as ruc_comercial_factura,    " +
				"coalesce(razon_social_recli,nombre_comercial_recli) as nombre_comercial_factura,       " +
				"coalesce(establecimiento_operativo_recli,coalesce(razon_social_recli,nombre_comercial_recli)) as establecimiento_operativo,     " +
				"coalesce(representante_legal_recli,'') as representante_legal,     " +
				"coalesce(telefono_factura_recli,'') as telefono_factura,         " +
				"coalesce(direccion_recld,'') as direccion_factura,        " +
				"coalesce(abreviatura_reclr,'') as abreviatura_factura,       " +
				"a.ide_reclr as id_abreviatura_ruta,        " +
				"ide_retic as id_tipo_contribuyente,       " +
				"coalesce(email_recle,'') as email,       " +
				"coalesce(num_generador_desecho_recli,'') as num_generador_desecho,      " +
				"fecha_emision_generador_recli as fecha_generador_desecho,       " +
				"coalesce(fac_mensual_recli,false) as mensualizado      " +
				"   " +
				"from rec_clientes a         " +
				"left join rec_cliente_ruta rcli on rcli.ide_reclr=a.ide_reclr	      " +
				"join (select ide_recli,coalesce(barrio_recld || ', ','')  || coalesce(direccion_recld || ' ','') || coalesce(nmro_casa_lote_dpto_recld || ' ','') || coalesce(interseccion_recld,'') as direccion_recld from rec_cliente_direccion where activo_recld=true and ide_recld in (select min(ide_recld) from rec_cliente_direccion " +
				"	    where activo_recld=true group by ide_recli)) b on b.ide_recli=a.ide_recli     " +
				"left join (select ide_recli, string_agg(email_recle, ',') as email_recle from rec_cliente_email where notificacion_recle=true and activo_recle=true group by ide_recli ) c on c.ide_recli=a.ide_recli     " +
				"left join (select ide_recli, string_agg(numero_contrato_prcon, ',') as numero_contrato from pre_contrato where ide_coest=31 group by ide_recli) d on d.ide_recli=a.ide_recli      " +
				"   " +
				"where a.ruc_comercial_recli is not null and nro_establecimiento_recli=1 and a.activo_recli=true     " +
				"   " +
				"union all   " +
				"   " +
				"select a.ide_recli as codigo, coalesce(numero_contrato,'0') as numero_contrato,    " +
				"coalesce(nro_establecimiento_reest,2) as nro_establecimiento,      " +
				"coalesce(ruc_comercial_recli,'') as ruc_comercial_factura,    " +
				"coalesce(razon_social_recli,nombre_comercial_recli) as nombre_comercial_factura,       " +
				"coalesce(establecimiento_operativo_reest,coalesce(razon_social_recli,nombre_comercial_recli)) as establecimiento_operativo,     " +
				"coalesce(representante_legal_recli,'') as representante_legal,     " +
				"coalesce(telefono_reest,coalesce(telefono_factura_recli,'')) as telefono_factura,         " +
				"coalesce(direccion_operativa_reest,'') as direccion_factura,        " +
				"coalesce(abreviatura_reclr,'') as abreviatura_factura,       " +
				"rcli.ide_reclr as id_abreviatura_ruta,        " +
				"ide_retic as id_tipo_contribuyente,       " +
				"coalesce(email_reest,coalesce(email_recle,'')) as email,       " +
				"coalesce(num_generador_desecho_recli,'') as num_generador_desecho,      " +
				"fecha_emision_generador_recli as fecha_generador_desecho,       " +
				"coalesce(fac_mensual_recli,false) as mensualizado      " +
				"   " +
				"from rec_clientes_establecimiento a         " +
				"join rec_clientes b on b.ide_recli=a.ide_recli   " +
				"left join rec_cliente_ruta rcli on rcli.ide_reclr=a.ide_reclr	      " +
				"left join (select ide_recli, string_agg(email_recle, ',') as email_recle from rec_cliente_email where notificacion_recle=true and activo_recle=true group by ide_recli ) c on c.ide_recli=b.ide_recli     " +
				"left join (select ide_recli, string_agg(numero_contrato_prcon, ',') as numero_contrato from pre_contrato where ide_coest=31 group by ide_recli) d on d.ide_recli=b.ide_recli      " +
				"   " +
				"where a.nro_establecimiento_reest>1 and a.activo_reest=true   " +
				"and b.ruc_comercial_recli is not null and nro_establecimiento_recli=1 and b.activo_recli=true     " +
				"     " +
				"order by ruc_comercial_factura, nro_establecimiento";*/
	
		/*String sql="select distinct codigo,cast(nro_contrato as character(25)) as nro_contrato,nro_establecimiento,ruc_comercial_factura," +
                " nombre_comercial_factura,establecimiento_operativo,representante_legal,telefono_factura," +
                " cast(direccion_factura as character(250)) as direccion_factura,abreviatura_factura" +
                " ,id_abreviatura_ruta,id_tipo_contribuyente, " +
                " cast(email as character(100)) as email,num_generador_desecho,fecha_generador_desecho,mensualizado,especiales " +
                "" +
                " from ( ";
            
    
		   sql+= " select a.ide_recli as codigo, coalesce(numero_contrato,'0') as nro_contrato,     " +
		    "coalesce(nro_establecimiento_recli,1) as nro_establecimiento,       " +
		    "coalesce(ruc_comercial_recli,'') as ruc_comercial_factura,     " +
		    "coalesce(razon_social_recli,nombre_comercial_recli) as nombre_comercial_factura,        " +
		    "coalesce(establecimiento_operativo_recli,coalesce(razon_social_recli,nombre_comercial_recli)) as establecimiento_operativo,      " +
		    "coalesce(representante_legal_recli,'') as representante_legal,      " +
		    "coalesce(telefono_factura_recli,'') as telefono_factura,          " +
		    "coalesce(direccion_recld,'') as direccion_factura,         " +
		    "coalesce(abreviatura_reclr,'') as abreviatura_factura,        " +
		    "a.ide_reclr as id_abreviatura_ruta,         " +
		    "ide_retic as id_tipo_contribuyente,        " +
		    "coalesce(email_recle,'') as email,        " +
		    "coalesce(num_generador_desecho_recli,'') as num_generador_desecho,       " +
		    "fecha_emision_generador_recli as fecha_generador_desecho,        " +
		    "coalesce(fac_mensual_recli,false) as mensualizado,       " +
		    "coalesce(especiales_recli,false) as especiales     " +
		    "from rec_clientes a          " +
		    "left join rec_cliente_ruta rcli on rcli.ide_reclr=a.ide_reclr	       " +

		    "     join (select ide_recli,(case when barrio_recld='SN' then '' else coalesce(barrio_recld||' ','') end)   " +
		    "				|| coalesce(direccion_recld,'')    " +
		    "				|| (case when nmro_casa_lote_dpto_recld='SN' then '' else coalesce(' '||nmro_casa_lote_dpto_recld,'') end)  " +
		    "				|| (case when interseccion_recld='SN' then '' else coalesce(' y '||interseccion_recld,'') end)   " +
		    "				as direccion_recld from rec_cliente_direccion where activo_recld=true and ide_recld in (select min(ide_recld) from rec_cliente_direccion    " +
		    "	      where activo_recld=true group by ide_recli)) b on b.ide_recli=a.ide_recli    " +       
		           
		    "left join (select ide_recli, string_agg(email_recle, ',') as email_recle from rec_cliente_email where notificacion_recle=true and activo_recle=true group by ide_recli ) c on c.ide_recli=a.ide_recli      " +
		    "left join (select ide_recli, string_agg(numero_contrato_prcon, ',') as numero_contrato from pre_contrato where ide_coest in (31,2) group by ide_recli) d on d.ide_recli=a.ide_recli       " +
		    "    " +

		    "where a.ruc_comercial_recli is not null and a.activo_recli=true      " +
		    " " +
		    " " +
		    "    " +
		    "union all    " +
		    "    " +
		    "select a.ide_recli as codigo, coalesce(numero_contrato,'0') as nro_contrato,     " +
		    "coalesce(nro_establecimiento_reest,2) as nro_establecimiento,       " +
		    "coalesce(ruc_comercial_recli,'') as ruc_comercial_factura,     " +
		    "coalesce(razon_social_recli,nombre_comercial_recli) as nombre_comercial_factura,        " +
		    "coalesce(establecimiento_operativo_reest,coalesce(razon_social_recli,nombre_comercial_recli)) as establecimiento_operativo,      " +
		    "coalesce(representante_legal_recli,'') as representante_legal,      " +
		    "coalesce(telefono_reest,coalesce(telefono_factura_recli,'')) as telefono_factura,          " +
		    "coalesce(direccion_reest,direccion_operativa_reest) as direccion_factura,         " +
		    "coalesce(abreviatura_reclr,'') as abreviatura_factura,        " +
		    "rcli.ide_reclr as id_abreviatura_ruta,         " +
		    "ide_retic as id_tipo_contribuyente,        " +
		    "coalesce(email_reest,coalesce(email_recle,'')) as email,        " +
		    "coalesce(num_generador_desecho_recli,'') as num_generador_desecho,       " +
		    "fecha_emision_generador_recli as fecha_generador_desecho,        " +
		    "coalesce(fac_mensual_recli,false) as mensualizado,       " +
		    "coalesce(especiales_recli,false) as especiales     " +
		    "from rec_clientes_establecimiento a          " +
		    "join rec_clientes b on b.ide_recli=a.ide_recli    " +
		    "left join rec_cliente_ruta rcli on rcli.ide_reclr=a.ide_reclr	       " +
		    "left join (select ide_recli, string_agg(email_recle, ',') as email_recle from rec_cliente_email where notificacion_recle=true and activo_recle=true group by ide_recli ) c on c.ide_recli=b.ide_recli      " +
		    "left join (select ide_recli, string_agg(numero_contrato_prcon, ',') as numero_contrato from pre_contrato where ide_coest=31 group by ide_recli) d on d.ide_recli=b.ide_recli       " +
		    "    " +
		    "where a.nro_establecimiento_reest>1 and a.activo_reest=true    " +

		     "and b.ruc_comercial_recli is not null and b.activo_recli=true      " ;
		    
		   
		   sql+=" union all ";
		   
		   sql+=" select ide_recli, nro_contrato,  " +
		        "     coalesce(nro_establecimiento_recli,1) as nro_establecimiento,  " +
		        "     coalesce(ruc_comercial_recli,'') as ruc_comercial_factura,  " +
		        "     coalesce(razon_social_recli,nombre_comercial_recli) as nombre_comercial_factura,     " +
		        "     coalesce(establecimiento_operativo_recli,coalesce(razon_social_recli,nombre_comercial_recli)) as establecimiento_operativo,   " +
		        "     coalesce(representante_legal,'') as representante_legal,      " +
		        "     coalesce(telefono_factura_recli,'') as telefono_factura,             " +
		        "     coalesce(direccion_recld,'') as direccion_factura,      " +
		        "     coalesce(abreviatura_reclr,'') as abreviatura_factura,     " +
		        "     ide_reclr as id_abreviatura_ruta,      " +
		        "     ide_retic as id_tipo_contribuyente,     " +
		        "     coalesce(email_recle,'') as email,     " +
		        "     coalesce(num_generador_desecho_recli,'') as num_generador_desecho,    " +
		        "     fecha_emision_generador_recli as fecha_generador_desecho,     " +
		        "     coalesce(fac_mensual_recli,false) as mensualizado,    " +
		        "     coalesce(especiales_recli,false) as especiales    " +
		        " " +
		        "     from ( select a.ide_recli,a.telefono_factura_recli,a.nombre_comercial_recli,(case when mat.ruc_comercial_recli like '1760003410001' then mat.razon_social_recli else a.razon_social_recli end) as razon_social_recli    " +
		        "     ,a.establecimiento_operativo_recli,a.matriz_sucursal_recli,(case when mat.ruc_comercial_recli like '1760003410001' then mat.ruc_comercial_recli else a.ruc_comercial_recli end) as ruc_comercial_recli    " +
		        "     ,abreviatura_reclr,b.direccion_recld,a.nro_establecimiento_recli, a.factura_datos_recli,a.ide_reclr,a.ide_retic,c.email_recle,a.fac_mensual_recli    " +
		        "     ,coalesce(a.num_generador_desecho_recli,mat.num_generador_desecho_recli) as  num_generador_desecho_recli    " +
		        "     ,coalesce(a.fecha_emision_generador_recli,mat.fecha_emision_generador_recli) as  fecha_emision_generador_recli " +
		        "     ,coalesce(a.nro_contrato_recli,'0') as nro_contrato " +
		        "     ,coalesce(a.representante_legal_recli,mat.representante_legal_recli) as representante_legal " +
		        "     ,especiales_recli  " +
		        "     from rec_clientes a       " +
		        "     join rec_cliente_ruta rcli on rcli.ide_reclr=a.ide_reclr	    " +
		        "     join (select ide_recli,(case when barrio_recld='SN' then '' else coalesce(barrio_recld||' ','') end)   " +
		        "				|| coalesce(direccion_recld,'')    " +
		        "				|| (case when nmro_casa_lote_dpto_recld='SN' then '' else coalesce(' '||nmro_casa_lote_dpto_recld,'') end)  " +
		        "				|| (case when interseccion_recld='SN' then '' else coalesce(' y '||interseccion_recld,'') end)   " +
		        "				as direccion_recld from rec_cliente_direccion where activo_recld=true and ide_recld in (select min(ide_recld) from rec_cliente_direccion    " +
		        "	      where activo_recld=true group by ide_recli)) b on b.ide_recli=a.ide_recli    " +
		        "     left join (select ide_recli, string_agg(email_recle, ',') as email_recle from rec_cliente_email where notificacion_recle=true and activo_recle=true group by ide_recli ) c on c.ide_recli=a.ide_recli      " +
		        "     left join ( select mc.ide_recli, mc.telefono_factura_recli, mc.razon_social_recli, mc.ruc_comercial_recli, direccion_recld, email_recle, num_generador_desecho_recli, fecha_emision_generador_recli,representante_legal_recli from rec_clientes mc    " +
		        "	    join (select ide_recli,direccion_recld || ' ' || coalesce(barrio_recld,'') || ' ' || coalesce(interseccion_recld,'') as direccion_recld from rec_cliente_direccion where activo_recld=true and ide_recld in (select min(ide_recld) from rec_cliente_direccion   " +
		        "			  where activo_recld=true group by ide_recli)) b on b.ide_recli=mc.ide_recli   " +
		        "	    left join (select ide_recli, string_agg(email_recle, ',') as email_recle from rec_cliente_email where notificacion_recle=true and activo_recle=true group by ide_recli ) c on c.ide_recli=mc.ide_recli     " +
		        "	    where mc.matriz_sucursal_recli=1 and mc.activo_recli=true and mc.rec_ide_recli is null and mc.ruc_comercial_recli is not null ) mat on mat.ide_recli=a.rec_ide_recli    " +
		        "     where a.ruc_comercial_recli is not null and a.activo_recli=true     " +
		        "     ) a     ";
		   
		   sql+= " ) cli ";
		   
		   sql+= " order by ruc_comercial_factura, nro_establecimiento";*/
		
		String sql="select distinct codigo,cast(nro_contrato as character(70)) as nro_contrato,nro_establecimiento,ruc_comercial_factura, " +
                "nombre_comercial_factura,establecimiento_operativo,representante_legal,telefono_factura, " +
                "cast(direccion_factura as character(250)) as direccion_factura,abreviatura_factura " +
                ",id_abreviatura_ruta,id_tipo_contribuyente,  " +
                "cast(email as character(100)) as email,num_generador_desecho,fecha_generador_desecho,mensualizado,especiales  " +
                " " +
                "from (  " +
                " " +
                "  select a.ide_recli as codigo, coalesce(numero_contrato,'0') as nro_contrato,      " +
                "        coalesce(nro_establecimiento_recli,1) as nro_establecimiento,        " +
                "        coalesce(ruc_comercial_recli,'') as ruc_comercial_factura,      " +
                "        coalesce(razon_social_recli,nombre_comercial_recli) as nombre_comercial_factura,         " +
                "        coalesce(establecimiento_operativo_recli,coalesce(razon_social_recli,nombre_comercial_recli)) as establecimiento_operativo,       " +
                "        coalesce(representante_legal_recli,'') as representante_legal,       " +
                "        coalesce(telefono_factura_recli,'') as telefono_factura,           " +
                "        coalesce(direccion_recld,'') as direccion_factura,          " +
                "        coalesce(abreviatura_reclr,'') as abreviatura_factura,         " +
                "        a.ide_reclr as id_abreviatura_ruta,          " +
                "        ide_retic as id_tipo_contribuyente,         " +
                "        coalesce(email_recle,'') as email,         " +
                "        coalesce(num_generador_desecho_recli,'') as num_generador_desecho,        " +
                "        fecha_emision_generador_recli as fecha_generador_desecho,         " +
                "        coalesce(fac_mensual_recli,false) as mensualizado,        " +
                "        coalesce(especiales_recli,false) as especiales      " +
                "        from rec_clientes a           " +
                "        left join rec_cliente_ruta rcli on rcli.ide_reclr=a.ide_reclr	        " +
                "        join (select ide_recli,(case when barrio_recld='SN' then '' else coalesce(barrio_recld||' ','') end)    " +
                "        				|| coalesce(direccion_recld,'')     " +
                "        				|| (case when nmro_casa_lote_dpto_recld='SN' then '' else coalesce(' '||nmro_casa_lote_dpto_recld,'') end)   " +
                "        				|| (case when interseccion_recld='SN' then '' else coalesce(' y '||interseccion_recld,'') end)    " +
                "        				as direccion_recld from rec_cliente_direccion where activo_recld=true and ide_recld in (select min(ide_recld) from rec_cliente_direccion     " +
                "        	      where activo_recld=true group by ide_recli)) b on b.ide_recli=a.ide_recli            " +
                "                " +
                "        left join (select ide_recli, string_agg(email_recle, ',') as email_recle from rec_cliente_email where notificacion_recle=true and activo_recle=true group by ide_recli ) c on c.ide_recli=a.ide_recli       " +
                "        left join (select ide_recli, string_agg(numero_contrato_prcon, ',') || ' | ' || string_agg(cast(fecha_fin_prcon as character(25)), ',') as numero_contrato from pre_contrato where ide_prtsc=1 and ide_coest in (31,2) and coalesce(activo_prcon,false)=true group by ide_recli) d on d.ide_recli=a.ide_recli         " +
                "             " +
                "        where a.ruc_comercial_recli is not null and a.activo_recli=true  " +
                "        and (1 = (case when substring(ruc_comercial_recli from 3 for 1) like '6' then 1 else 0 end) or d.ide_recli is not null) " +
                " " +
                "  union all  " +
                " " +
                "  select a.ide_recli as codigo, coalesce(numero_contrato,'0') as nro_contrato,      " +
                "        coalesce(nro_establecimiento_reest,2) as nro_establecimiento,        " +
                "        coalesce(ruc_comercial_recli,'') as ruc_comercial_factura,      " +
                "        coalesce(razon_social_recli,nombre_comercial_recli) as nombre_comercial_factura,         " +
                "        coalesce(establecimiento_operativo_reest,coalesce(razon_social_recli,nombre_comercial_recli)) as establecimiento_operativo,       " +
                "        coalesce(representante_legal_recli,'') as representante_legal,       " +
                "        coalesce(telefono_reest,coalesce(telefono_factura_recli,'')) as telefono_factura,           " +
                "        coalesce(direccion_reest,direccion_operativa_reest) as direccion_factura,          " +
                "        coalesce(abreviatura_reclr,'') as abreviatura_factura,         " +
                "        rcli.ide_reclr as id_abreviatura_ruta,          " +
                "        ide_retic as id_tipo_contribuyente,         " +
                "        coalesce(email_reest,coalesce(email_recle,'')) as email,         " +
                "        coalesce(num_generador_desecho_recli,'') as num_generador_desecho,        " +
                "        fecha_emision_generador_recli as fecha_generador_desecho,         " +
                "        coalesce(fac_mensual_recli,false) as mensualizado,        " +
                "        coalesce(especiales_recli,false) as especiales      " +
                "        from rec_clientes_establecimiento a           " +
                "        join rec_clientes b on b.ide_recli=a.ide_recli     " +
                "        left join rec_cliente_ruta rcli on rcli.ide_reclr=a.ide_reclr	        " +
                "        left join (select ide_recli, string_agg(email_recle, ',') as email_recle from rec_cliente_email where notificacion_recle=true and activo_recle=true group by ide_recli ) c on c.ide_recli=b.ide_recli       " +
                "        left join (select ide_recli, string_agg(numero_contrato_prcon, ',') || ' | ' || string_agg(cast(fecha_fin_prcon as character(25)), ',') as numero_contrato from pre_contrato where ide_prtsc=1 and ide_coest in (31,2) and coalesce(activo_prcon,false)=true group by ide_recli) d on d.ide_recli=b.ide_recli        " +
                "             " +
                "        where a.nro_establecimiento_reest>1 and a.activo_reest=true    " +
                "        and b.ruc_comercial_recli is not null and b.activo_recli=true  " +
                "        and (1 = (case when substring(b.ruc_comercial_recli from 3 for 1) like '6' then 1 else 0 end) or d.ide_recli is not null) " +
                " " +
                ") cli " +
                " " +
                "order by ruc_comercial_factura, nro_establecimiento "; //Memorando Nro. EMGIRS-EP-GGE-GOP-UC-2021-0314-M
	
		System.out.println("getClientesRutas "+sql);
		return sql;
	}
	
	public String getClientesNotificados(String ide_regec, String ruc_comercial_recli){
		
		String sql="SELECT ide_recln, nombre_regec, nro_recln,tipo_recln, rcn.ruc_comercial_recli,razon_social_recli, fecha_notificacion_recln, "
			        +" detalle_tipo_recln, observacion_recln as correo_notificado, activo_recln "
			        +" FROM rec_cliente_notificaciones rcn "
					+" left join rec_gestor_comercial rgc on rgc.ide_regec=rcn.ide_regec "
					+" left join (select ruc_comercial_recli,razon_social_recli from rec_clientes cli " 
							+" where ide_recli in (select max(ide_recli) from rec_clientes where ruc_comercial_recli=cli.ruc_comercial_recli) "
							+" group by ruc_comercial_recli,razon_social_recli ) cli on cli.ruc_comercial_recli=rcn.ruc_comercial_recli "
					+ " where rcn.ide_regec="+ide_regec +" or rcn.ruc_comercial_recli like '"+ruc_comercial_recli+"'";
		
		System.out.println("getClientesNotificados: "+sql);
		return sql;
		
	}
	
public String getClientesGestores(String ide_regec){
		
		String sql="select distinct codigo,cast(nro_contrato as character(25)) as nro_contrato,nro_establecimiento,ruc_comercial_factura," +
                " nombre_comercial_factura,establecimiento_operativo,representante_legal,telefono_factura," +
                " cast(direccion_factura as character(250)) as direccion_factura,abreviatura_factura" +
                " ,id_abreviatura_ruta,id_tipo_contribuyente, " +
                " cast(email as character(100)) as email,num_generador_desecho,fecha_generador_desecho,mensualizado,especiales,ide_regec " +
                "" +
                " from ( ";
            
    
		   sql+= " select a.ide_recli as codigo, coalesce(numero_contrato,'0') as nro_contrato,     " +
		    "coalesce(nro_establecimiento_recli,1) as nro_establecimiento,       " +
		    "coalesce(ruc_comercial_recli,'') as ruc_comercial_factura,     " +
		    "coalesce(razon_social_recli,nombre_comercial_recli) as nombre_comercial_factura,        " +
		    "coalesce(establecimiento_operativo_recli,coalesce(razon_social_recli,nombre_comercial_recli)) as establecimiento_operativo,      " +
		    "coalesce(representante_legal_recli,'') as representante_legal,      " +
		    "coalesce(telefono_factura_recli,'') as telefono_factura,          " +
		    "coalesce(direccion_recld,'') as direccion_factura,         " +
		    "coalesce(abreviatura_reclr,'') as abreviatura_factura,        " +
		    "a.ide_reclr as id_abreviatura_ruta,         " +
		    "ide_retic as id_tipo_contribuyente,        " +
		    "coalesce(email_recle,'') as email,        " +
		    "coalesce(num_generador_desecho_recli,'') as num_generador_desecho,       " +
		    "fecha_emision_generador_recli as fecha_generador_desecho,        " +
		    "coalesce(fac_mensual_recli,false) as mensualizado,       " +
		    "coalesce(especiales_recli,false) as especiales, ide_regec    " +
		    "from rec_clientes a          " +
		    "left join rec_cliente_ruta rcli on rcli.ide_reclr=a.ide_reclr	       " +

		    "     join (select ide_recli,(case when barrio_recld='SN' then '' else coalesce(barrio_recld||' ','') end)   " +
		    "				|| coalesce(direccion_recld,'')    " +
		    "				|| (case when nmro_casa_lote_dpto_recld='SN' then '' else coalesce(' '||nmro_casa_lote_dpto_recld,'') end)  " +
		    "				|| (case when interseccion_recld='SN' then '' else coalesce(' y '||interseccion_recld,'') end)   " +
		    "				as direccion_recld from rec_cliente_direccion where activo_recld=true and ide_recld in (select min(ide_recld) from rec_cliente_direccion    " +
		    "	      where activo_recld=true group by ide_recli)) b on b.ide_recli=a.ide_recli    " +       
		           
		    "left join (select ide_recli, string_agg(email_recle, ',') as email_recle from rec_cliente_email where notificacion_recle=true and activo_recle=true group by ide_recli ) c on c.ide_recli=a.ide_recli      " +
		    "left join (select ide_recli, string_agg(numero_contrato_prcon, ',') as numero_contrato from pre_contrato where ide_coest in (31,2) group by ide_recli) d on d.ide_recli=a.ide_recli       " +
		    "    " +

		    "where a.ruc_comercial_recli is not null and a.activo_recli=true and coalesce(ide_regec,0)="+ide_regec ;
		    
		   
		   sql+= " ) cli ";
		   
		   sql+= " order by ruc_comercial_factura, nro_establecimiento";
	
		System.out.println("getClientesGestores "+sql);
		return sql;
	}
	
}
