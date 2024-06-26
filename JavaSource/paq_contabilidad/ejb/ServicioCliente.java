package paq_contabilidad.ejb;

import java.util.List;

import javax.ejb.Stateless;

import paq_sistema.aplicacion.Utilitario;
import pckEntidades.Clientes;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;


/**
 * Session Bean implementation class ServicioCliente
 */
@Stateless

public class ServicioCliente {
	private Utilitario utilitario=new Utilitario();

	
	public ServicioCliente() {
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

		String str_sql="Select * from rec_clientes where matriz_sucursal_recli=1 and ruc_comercial_recli="+ utilitario.generarComillaSimple(documento_identidad) + " and activo_recli=true "; 
	
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
	public String getClientesFacturasContratos(String cliente, String punto_venta, String fecha_inicial,String fecha_final,String aplica_fecha, String aplica_fec_prcon){
		
		String factura_clientes=" select distinct cli.ide_recli, cli.fac_mensual_recli as FACTURA_MENSUAL, CASE cli.estado_recli WHEN 'true' THEN 'PUBLICO' WHEN 'false' THEN 'PRIVADO' ELSE 'OTRO' END AS TIPO,"
					+" CASE ide_retic WHEN 1 THEN 'PERSONA NATURAL'   WHEN 2 THEN 'PERSONA JURIDICA'   ELSE 'SOCIEDADES' END AS TIPO_CONTRIBUYENTE, "
					+" cli.ruc_comercial_recli, cli.razon_social_recli, cli.representante_legal_recli, cli.nombre_comercial_recli, gen.detalle_gedip AS PARROQUIA," 
					+" cli.direccion_recli,cli.telefono_factura_recli,cli.email_recli, tia.detalle_retia AS FRECUENCIA, rcli.detalle_reclr AS RUTA, "
					+" cli.estimado_desecho_recl, mat.detalle_bogrm, tem.detalle_tipcli, prec.numero_contrato_prcon, prec.fecha_inicio_prcon, "
					+" fac.ide_fafac,fac.fecha_transaccion_fafac,ROUND(fac.total_fafac,2) AS VALOR_FACTURA, "
					+" CASE cli.activo_recli WHEN 'true' THEN 'ACTIVO'  WHEN 'false' THEN 'INACTIVO'   ELSE 'OTRO' END AS ESTADO, " 
					+" cli.ide_bogrm  "
					+" from rec_clientes cli "
					+" LEFT JOIN gen_division_politica gen ON gen.ide_gedip = cli.ide_gedip "
					+" LEFT JOIN rec_cliente_ruta rcli ON rcli.ide_reclr = cli.ide_reclr "
					+" LEFT JOIN rec_tipo_asistencia tia ON cli.ide_retia = tia.ide_retia "
					+" left join bodt_grupo_material mat on cli.ide_bogrm = mat.ide_bogrm "
					+" LEFT JOIN rec_tipo_cliente_emgirs tem ON tem.ide_tipcli=cli.ide_tipcli "
					+" LEFT JOIN pre_contrato prec ON prec.ide_recli=cli.ide_recli "
					+" left join (select ruc_comercial_recli, max(cli.ide_recli) as ide_recli, max(ide_fafac) as ide_fafac "
					+" 		from fac_factura fac"
					+" 		join rec_clientes cli on cli.ide_recli=fac.ide_recli"
					+" 		group by  ruc_comercial_recli  ) frec on frec.ide_recli=cli.ide_recli "
					+" left join fac_factura fac on fac.ide_fafac=frec.ide_fafac  "
					+" where cli.activo_recli in ('true', 'false') ";
				if(aplica_fecha.equals("true")){
					factura_clientes +=" and fecha_transaccion_fafac between '"+fecha_inicial+"' and '"+fecha_final+"' ";
					}
				if(aplica_fec_prcon.equals("true")){
					factura_clientes +=" and fecha_inicio_prcon between '"+fecha_inicial+"' and '"+fecha_final+"' ";	
					
				}
				if (punto_venta.equals("0")){
					factura_clientes += " ";
				}else{
					factura_clientes += " and cli.ide_bogrm in ("+punto_venta+") ";
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
		
		
	
		String sql="select distinct codigo,cast(nro_contrato as character(70)) as nro_contrato,nro_establecimiento,ruc_comercial_factura,   "
				+ "                 nombre_comercial_factura,establecimiento_operativo,representante_legal,telefono_factura,   "
				+ "                 cast(direccion_factura as character(250)) as direccion_factura,abreviatura_factura   "
				+ "                 ,id_abreviatura_ruta,id_tipo_contribuyente,    "
				+ "                 cast(email as character(100)) as email,num_generador_desecho,fecha_generador_desecho,mensualizado,especiales    "
				+ "                   "
				+ "                 from (    "
				+ "                   "
				+ "                  select a.ide_recli as codigo, coalesce(numero_contrato,'0') as nro_contrato,              "
				+ "                        coalesce(nro_establecimiento_reest,coalesce(nro_establecimiento_recli,1)) as nro_establecimiento, "
				+ "                        coalesce(ruc_comercial_recli,'') as ruc_comercial_factura,        "
				+ "                        coalesce(razon_social_recli,nombre_comercial_recli) as nombre_comercial_factura,           "
				+ "                        coalesce(case when length(establecimiento_operativo_reest)>4 then establecimiento_operativo_reest else null end,coalesce(razon_social_recli,nombre_comercial_recli)) as establecimiento_operativo,         "
				+ "                        coalesce(representante_legal_recli,'') as representante_legal,         "
				+ "                        coalesce(case when length(telefono_reest)>4 then telefono_reest else null end,coalesce(telefono_factura_recli,'')) as telefono_factura,                   "
				+ "                        coalesce(direccion_reest,coalesce(direccion_recld,'')) as direccion_factura,  "
				+ "                        coalesce(abreviatura_reclr,'') as abreviatura_factura,           "
				+ "                        a.ide_reclr as id_abreviatura_ruta,            "
				+ "                        ide_retic as id_tipo_contribuyente,              "
				+ "                        coalesce(case when length(email_reest)>4 then email_reest else null end,coalesce(email_recle,'')) as email,       "
				+ "                        coalesce(num_generador_desecho_recli,'') as num_generador_desecho,          "
				+ "                        fecha_emision_generador_recli as fecha_generador_desecho,           "
				+ "                        coalesce(fac_mensual_recli,false) as mensualizado,          "
				+ "                        coalesce(especiales_recli,false) as especiales        "
				+ "                        from rec_clientes a             "
				+ "                        left join rec_cliente_ruta rcli on rcli.ide_reclr=a.ide_reclr	   "
				+ " "
				+ "                        left join (select ide_recli,(case when barrio_recld='SN' then '' else coalesce(barrio_recld||' ','') end)      "
				+ "                        				|| coalesce(direccion_recld,'')       "
				+ "                        				|| (case when nmro_casa_lote_dpto_recld='SN' then '' else coalesce(' '||nmro_casa_lote_dpto_recld,'') end)     "
				+ "                        				|| (case when interseccion_recld='SN' then '' else coalesce(' y '||interseccion_recld,'') end)      "
				+ "                        				as direccion_recld from rec_cliente_direccion where activo_recld=true and ide_recld in (select min(ide_recld) from rec_cliente_direccion       "
				+ "                        	      where activo_recld=true group by ide_recli)) b on b.ide_recli=a.ide_recli "
				+ "                       "
				+ "                        left join rec_clientes_establecimiento est on est.ide_recli=a.ide_recli and est.activo_reest=true and est.ide_reest in (select min(ide_reest) FROM rec_clientes_establecimiento where activo_reest=true and length(direccion_reest)>4 and coalesce(nro_establecimiento_reest,0)>0 group by ide_recli,nro_establecimiento_reest)   "
				+ "                                 "
				+ "                        left join (select ide_recli, string_agg(email_recle, ',') as email_recle from rec_cliente_email where notificacion_recle=true and activo_recle=true group by ide_recli ) c on c.ide_recli=a.ide_recli         "
				+ "                        left join (select ide_recli, string_agg(numero_contrato_prcon, ',') || ' | ' || string_agg(cast(fecha_fin_prcon as character(25)), ',') as numero_contrato from pre_contrato where ide_prtsc=1 and ide_coest in (31) and coalesce(activo_prcon,false)=true group by ide_recli) d on d.ide_recli=a.ide_recli           "
				+ "                               "
				+ "                        where a.ruc_comercial_recli is not null and a.activo_recli=true     "
				+ "                        and (1 = (case when substring(ruc_comercial_recli from 3 for 1) like '6' then 1 else 0 end) or d.ide_recli is not null)   "
				+ "                   "
				+ "                 ) cli   "
				+ "                   "
				+ "                 order by ruc_comercial_factura, nro_establecimiento ;";
	  
		   System.out.println("getClientesAvCorp "+sql);
		return sql;
	}
	
}
