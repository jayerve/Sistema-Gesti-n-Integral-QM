package paq_contabilidad.ejb;

import javax.ejb.Stateless;

import framework.aplicacion.TablaGenerica;
import paq_precontractual.ejb.ServicioGeneralAdmPrecon;
import paq_sistema.aplicacion.Utilitario;
@Stateless

public class ServicioContabilidad {
	
	private Utilitario utilitario=new Utilitario();
	private ServicioGeneralAdmPrecon ser_generalAdm = (ServicioGeneralAdmPrecon ) utilitario.instanciarEJB(ServicioGeneralAdmPrecon.class);
	
	public String getVigente (String codigo){
		
		String tab_vigente="SELECT tc.constraint_name, " +
				"tc.table_name, kcu.column_name," +
				" ccu.table_name AS foreign_table_name," +
				"ccu.column_name AS foreign_column_name " +
				"FROM information_schema.table_constraints AS tc " +
				"JOIN information_schema.key_column_usage AS kcu " +
				"ON tc.constraint_name = kcu.constraint_name " +
				"JOIN information_schema.constraint_column_usage AS ccu " +
				"ON ccu.constraint_name = tc.constraint_name " +
				"WHERE constraint_type = 'FOREIGN KEY' AND tc.table_name='cont_vigente'" +"codigo";
				
		return tab_vigente;
		
	}
	public TablaGenerica getTablaVigente (String tabla){
		
		TablaGenerica tab_vigente=utilitario.consultar("SELECT tc.constraint_name, " +
				"tc.table_name, kcu.column_name, " +
				" ccu.table_name AS foreign_table_name, " +
				"ccu.column_name AS foreign_column_name " +
				"FROM information_schema.table_constraints AS tc " +
				"JOIN information_schema.key_column_usage AS kcu " +
				"ON tc.constraint_name = kcu.constraint_name " +
				"JOIN information_schema.constraint_column_usage AS ccu " +
				"ON ccu.constraint_name = tc.constraint_name " +
				"WHERE constraint_type = 'FOREIGN KEY' AND tc.table_name='"+tabla+"'");
		return tab_vigente;
		
		}
	
	public String getAnio (String activo, String bloqueado ){
		String tab_anio= "select ide_geani,detalle_geani from gen_anio " +
				" where activo_geani in ("+activo+") " +
				" and bloqueado_geani in ("+bloqueado+") " +
				" order by detalle_geani desc";
		return tab_anio;
	
		}
	public String getMes (String estado ){
		String tab_anio= "select ide_gemes,detalle_gemes from gen_mes where activo_gemes in ("+estado+") order by ide_gemes";
		return tab_anio;
	
		}
	public TablaGenerica getTablaAnio (String activo, String bloqueado ){
		TablaGenerica tab_anio= utilitario.consultar("select ide_geani,detalle_geani from gen_anio " +
				" where activo_geani in ("+activo+") " +
				" and bloqueado_geani in ("+bloqueado+") " +
				" order by detalle_geani desc");
		return tab_anio;
		}
	public String getAnioDetalle (String activo, String bloqueado ){
		String tab_anio= "select ide_geani,detalle_geani, " +
				" CASE WHEN activo_geani = true THEN 'Activo' ELSE 'Inactivo' END AS activo_geani," +
				" CASE WHEN bloqueado_geani = true THEN 'Bloqueado' ELSE 'Habilitado' END AS bloqueado_geani" +
				" from gen_anio where activo_geani in("+activo+")" +
				//" and bloqueado_geani in ("+bloqueado+")" +
				" and bloqueado_geani in (false)" +
				" order by detalle_geani desc" ;
				
		return tab_anio;
	
		}
	public String getListaNombreAsientoTipo(){
		String tab_asiento= "select a.ide_conac,detalle_conac as descripcion_asiento,detalle_coest as comprobantes_estado,consolidado_conac,individual_conac,activo_conac from cont_nombre_asiento_contable a "
				+ " left join cont_estado est on est.ide_coest=a.ide_coest " ;
		return tab_asiento;
	}
	public String getNombreAsientoContable (String modulo, String estado ){
		String tab_asiento= "select a.ide_conac,ide_gemod,detalle_conac as descripcion_asiento,detalle_coest as comprobantes_estado,consolidado_conac,individual_conac,activo_conac, a.ide_coest from cont_nombre_asiento_contable a "
				+ " left join cont_estado est on est.ide_coest=a.ide_coest where ide_gemod in ( "+modulo+") and activo_conac in ("+estado+")" ;
		return tab_asiento;
	}
	public String getNombreAsientoContable_corto (String modulo, String estado ){
		String tab_asiento= "select a.ide_conac,detalle_conac as descripcion_asiento, detalle_coest as comprobantes_estado "
				+ " from cont_nombre_asiento_contable a "
				+ " left join cont_estado est on est.ide_coest=a.ide_coest where ide_gemod in ( "+modulo+") and activo_conac in ("+estado+")" ;
		return tab_asiento;
	}
	public TablaGenerica getTablaAnioDetalle (String activo, String bloqueado ){
		TablaGenerica tab_anio= utilitario.consultar("select ide_geani,detalle_geani," +
				" CASE WHEN activo_geani = true THEN 'Activo' ELSE 'Inactivo' END AS activo_geani," +
				" CASE WHEN bloqueado_geani = true THEN 'Bloqueado' ELSE 'Habilitado' END AS bloqueado_geani" +
				" from gen_anio where activo_geani in("+activo+")  " +
				" and bloqueado_geani in ("+bloqueado+")" +
				" order by detalle_geani desc");
		return tab_anio;
		}
	/**
	 * Metodo que devuelve los Estados clasificados por modulo 
	 * @param estado recibe el o los estados true y false, ejemplo: true o false
	 * @param modulo recibe el codigo del Modulo por el cual desea clasificar el estado 
	 * @return string SQL modulo estado
	 */
	public TablaGenerica getTablaModuloEstados (String estado,String modulo){
		
		TablaGenerica tab_estados=utilitario.consultar("SELECT a.ide_coest,detalle_coest,detalle_gemod "
				+"FROM gen_modulo_estado a,cont_estado b, gen_modulo c "
				+"WHERE a.ide_coest= b.ide_coest AND a.ide_gemod = c.ide_gemod "
				+"AND activo_gemoe in ("+estado+") AND a.ide_gemod ="+modulo+" order by detalle_coest");
			return tab_estados;
	}
	
	public String getModuloEstados (String estado,String modulo){
		String consultaEstados="SELECT a.ide_coest,detalle_coest,detalle_gemod "
	+"FROM gen_modulo_estado a,cont_estado b, gen_modulo c "
	+"WHERE a.ide_coest= b.ide_coest AND a.ide_gemod = c.ide_gemod "
	+"AND activo_gemoe in ("+estado+") AND a.ide_gemod ="+modulo+" order by detalle_coest";
		return consultaEstados;
	}
	/**
	 * Metodo que devuelve los Parametros generales requeridos por modulo
	 * @param estado recibe el o los estados true y false, ejemplo: true o false
	 * @param modulo recibe el codigo del modulo 
	 * @return string SQL gen_modulo
	 */
	public String getModuloParametros (String estado,String modulo){
		String consultaEstados="select ide_copag,detalle_copag from cont_parametros_general" 
	+" where ide_copag in (select ide_copag from cont_parametro_modulo where ide_gemod in ("+modulo+") and activo_copam in ("+estado+") )";
		System.out.println("modulo parametro"+consultaEstados);
		return consultaEstados;
	}
	/**
	 * Metodo que devuelve los tipos de convenios 
	 * @param estado recibe el o los estados true y false, ejemplo: true o false
	 * @param modulo recibe el codigo del de los tipos de convenios 
	 * @return string SQL tipo de convenio
	 */
	public TablaGenerica getTablaTipoConvenio (String activo,String detalle){
		TablaGenerica tab_tipo_convenio= utilitario.consultar("Select ide_cotie ,detalle_cotie " +
				" from cont_tipo_convenio" +
				"  where activo_cotie in ( true, false) order by detalle_cotie");
		
			return tab_tipo_convenio;
	}
	
	public String getTipoConvenio (String activo, String detalle){
		String tipo_convenio ="Select ide_cotie ,detalle_cotie " +
				" from cont_tipo_convenio" +
				"  where activo_cotie in ( true, false) order by detalle_cotie";
			
		return tipo_convenio ;
	
	}
	public TablaGenerica getTablaInstitucion ( String activo, String detalle){
		TablaGenerica tab_institucion= utilitario.consultar("Select ide_geins,detalle_geins" +
				" from gen_institucion" +
				" where activo_geins in (true, false) order by detalle_geins");
		
		return tab_institucion;	
		
		}
	public String getInstitucion (String activo, String detalle){
		String institucion ="Select ide_geins,detalle_geins "+
				" from gen_institucion" +
				" where activo_geins in (true, false)  order by detalle_geins";
		
		return institucion;
	}
	/**
	 * Metodo que devuelve la Cuenta Cantable
	 * @param estado recibe el o los estados true y false, ejemplo: true o false
	 * @param modulo recibe las cuentas contables activas o pasivas 
	 * @return TABLA GENERICA SQL Cuenta Contable 
	 */
	
	public TablaGenerica getTablaCuentaContable ( String activo){
	TablaGenerica tab_cuenta_contable= utilitario.consultar("select ide_cocac,cue_codigo_cocac,cue_descripcion_cocac" +
			"  from cont_catalogo_cuenta " +
			"  WHERE activo_cocac IN ("+activo+")order by cue_codigo_cocac ");
	
	return tab_cuenta_contable;	
}

	public String getCuentaContable (String activo, String ide_cocac){
		String cuentacontable ="select ide_cocac,cue_codigo_cocac,cue_descripcion_cocac" +
				"  from cont_catalogo_cuenta " +
				"  WHERE activo_cocac IN ("+activo+") and ide_cocac="+ide_cocac+" order by cue_codigo_cocac ";
		return cuentacontable;
	}
	
	public String getCuentaContable (String activo){
		String cuentacontable ="select ide_cocac,cue_codigo_cocac,cue_descripcion_cocac" +
				"  from cont_catalogo_cuenta " +
				"  WHERE activo_cocac IN ("+activo+")order by cue_codigo_cocac ";
		return cuentacontable;
	}
	
	public String getCuentaContable (Integer codigoClasificacion){
	  return "SELECT ccc.ide_cocac, ccc.cue_codigo_cocac,ccc.cue_descripcion_cocac " +
	      " FROM cont_catalogo_cuenta ccc " + 
	      " left join  pre_asociacion_presupuestaria pap on pap.ide_cocac = ccc.ide_cocac " +
	      " left join pre_clasificador pc on pap.ide_prcla = pc.ide_prcla " +
	      " WHERE pap.ide_prcla = "+ codigoClasificacion +" and activo_cocac = true order by cue_codigo_cocac";
	}
	
	public String getPreClasificacion(){
	  return "SELECT ide_prcla,codigo_clasificador_prcla, descripcion_clasificador_prcla FROM pre_clasificador " + 
	         "WHERE activo_prcla = true";
	}
	
	public String getBalanceComprobacion(){
		String cuentacontable ="SELECT ide_balance, cue_descripcionb "+
		  "FROM cont_balance_general where ide_cuenta is null order by 2 ";
		return cuentacontable;
	}
	 
	
	/**
	 * Metodo que devuelve la Cuenta Cantable por ide_cocac 
	 * @param activo recibe el o los estados true y false, ejemplo: true o false
	 * @param ide_cuenta recibe los ide de las cuentas contables 
	 * @return String SQL Cuenta Contable 
	 */
	public String getCuentaContableCodigo (String estado,String ide_activo){
		String cuentacontable ="select ide_cocac,cue_codigo_cocac,cue_descripcion_cocac, replace(cue_codigo_cocac,'.','') as cue_codigo_remplazado" +
				"  from cont_catalogo_cuenta " +
				"  WHERE activo_cocac IN ("+estado+") and ide_cocac in ("+ide_activo+") order by cue_codigo_cocac ";
		return cuentacontable;
	}
	
	/**
	 * Metodo que devuelve la Cuenta Cantable por los años vigentes
	 * @param estado recibe el o los estados true y false, ejemplo: true o false
	 * @param ide_geani recibe el año de las cuentas contables 
	 * @return TablaGenerica Cuenta Contable 
	 */
	public TablaGenerica getTablaCatalogoCuentaAnio (String estado,String ide_geani){
		
		TablaGenerica tab_catalogo_cuenta_anio=utilitario.consultar("select a.ide_cocac,cue_codigo_cocac,cue_descripcion_cocac," +
				"detalle_geani" +
				"  from cont_catalogo_cuenta a,cont_vigente b,gen_anio c "+
				" where a.ide_cocac = b.ide_cocac" +
				" and b.ide_geani= c.ide_geani and b.ide_geani =("+ide_geani+")" +
				" and activo_cocac in ("+estado+")order by cue_codigo_cocac");
		return tab_catalogo_cuenta_anio;
		
		
	}
	/**
	 * Metodo que devuelve la Cuenta Cantable por los años vigentes
	 * @param estado recibe el o los estados true y false, ejemplo: true o false
	 * @param ide_geani recibe el año de las cuentas contables 
	 * @return String Cuenta Contable 
	 */
	public String getCatalogoCuentaAnio (String estado,String ide_geani){
		
		String tab_catalogo_cuenta_anio="select a.ide_cocac,cue_codigo_cocac,cue_descripcion_cocac," +
				"detalle_geani" +
				"  from cont_catalogo_cuenta a,cont_vigente b,gen_anio c "+
				" where a.ide_cocac = b.ide_cocac" +
				" and b.ide_geani= c.ide_geani and b.ide_geani =("+ide_geani+")" +
				" and activo_cocac in ("+estado+")order by cue_codigo_cocac";
		return tab_catalogo_cuenta_anio;
		
		
	}
	/**
	 * Metodo que devuelve la Cuenta Cantable por los años vigentes
	 * @param estado recibe el o los estados true y false, ejemplo: true o false
	 * @param ide_geani recibe el año de las cuentas contables 
	 * @return String SQL Cuenta Contable 
	 */
	public String servicioCatalogoCuentaAnio (String estado, String ide_geani){
		
		String catalogo_cuenta_anio="select a.ide_cocac,cue_codigo_cocac,cue_descripcion_cocac,detalle_geani " +
				" from cont_catalogo_cuenta a,cont_vigente b,gen_anio c where a.ide_cocac = b.ide_cocac" +
				" and b.ide_geani= c.ide_geani and b.ide_geani ="+ide_geani +
				" and activo_cocac in ("+estado+")order by cue_codigo_cocac";
		return catalogo_cuenta_anio;
	}
	
	/**
	 * Metodo que devuelve la Cuenta Cantable para cargar en los combos
	 * @return String SQL Cuenta Contable 
	 */
	public String servicioCatalogoCuentaCombo (){
		
		String catalogo_cuenta_anio="select a.ide_cocac,cue_codigo_cocac,cue_descripcion_cocac " +
				" from cont_catalogo_cuenta a order by cue_codigo_cocac";
		return catalogo_cuenta_anio;
	}
	public String getInventario(String ide_geani,String carga,String material){
		
		String tab_inventario="SELECT ide_boinv,codigo_bomat,detalle_bomat, detalle_geani,ingreso_material_boinv,egreso_material_boinv,existencia_inicial_boinv,costo_anterior_boinv," +
				"costo_actual_boinv,fecha_ingr_articulo_boinv,costo_inicial_boinv,activo_boinv " +
				" FROM bodt_inventario a, gen_anio b ,bodt_material c WHERE a.ide_geani=b.ide_geani " +
				" AND a.ide_bomat = c.ide_bomat AND a.ide_geani IN ("+ide_geani+") ";
				if (carga.equals("0")){
					tab_inventario +=" and a.ide_bomat= "+material;
				}
				    tab_inventario += " ORDER by ide_boinv";	
	
		return tab_inventario;
	}
	
	/**
	 * Metodo que devuelve la Cuenta Cantable por realizaqr transacciones filtra cuentas contables por año fiscal vigente
	 * @return String SQL Cuenta Contable para realizar transacciones
	 */
	public String servicioCatalogoCuentasTransaccion(String aplica_anio){
		
		String catalogo_cuenta_anio="select ide_cocac,cue_codigo_cocac,substring (cue_descripcion_cocac from 1 for 100) as cuenta from cont_catalogo_cuenta  where not ide_cocac in ("
				+" select con_ide_cocac from cont_catalogo_cuenta where not con_ide_cocac is null group by con_ide_cocac )" 
				+" and ide_cocac in ( select ide_cocac from cont_vigente where not ide_cocac is null ";
		if (aplica_anio.equals("0")){
			catalogo_cuenta_anio+= " and ide_geani in (select ide_geani from gen_anio where activo_geani = true limit 1)";
		}
		catalogo_cuenta_anio+= " )"
				+" order by cue_codigo_cocac";
		return catalogo_cuenta_anio;
	}
	/**
	 * Metodo que devuelve el ide maximo de una tabla
	 * @return String SQL Codigo maximo de los ide primarios de de las tablas
	 */
	public String servicioCodigoMaximo(String tabla,String ide_primario){
		
		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
		return maximo;
	}
	/**
	 * Metodo que devuelve el numero secuencial, por modulo
	 * @param modulo recibe el modulo al cual pertenece el secuencial
	 * @return String nro secuencial por modulo
	 */
	
	public String empleadoResponsable(String modulo, String ide_gtemp){
		String resultado="0";	
		TablaGenerica valor_empleado=utilitario.consultar(ser_generalAdm.servicioDepartamento("true",ide_gtemp));
		//valor_empleado.imprimirSql();
		if (valor_empleado.getTotalFilas()>0){
			TablaGenerica valor_resultado=utilitario.consultar("select ide_gemod,ide_geedp from gen_modulo_adjudicador where coalesce(activo_gemoa,false)=true and ide_gemod="+modulo+" and ide_geedp="+valor_empleado.getValor("ide_geedp"));
			resultado =	pckUtilidades.CConversion.CInt(valor_resultado.getValor("ide_geedp"))+"";
		}
		return resultado.equals("0")?null:resultado;
	}
	
	public String numeroSecuencial(String modulo){
		String resultado="1";
		Integer nuevo_valor=0;
		
		TablaGenerica valor_resultado=utilitario.consultar("select ide_gemod,numero_secuencial_gemos from gen_modulo_secuencial where ide_gemod="+modulo);
		if(valor_resultado.getTotalFilas()>0)
			nuevo_valor=pckUtilidades.CConversion.CInt(valor_resultado.getValor("numero_secuencial_gemos"))+1;
		
		resultado =	nuevo_valor+"";
		return resultado;
	}
	
	public String guardaSecuencial(String secuencial_vigente,String modulo){
		String mensaje="Actualizado Secuencial";
		double nuevo_valor=pckUtilidades.CConversion.CDbl_2(secuencial_vigente);
		utilitario.getConexion().ejecutarSql("update gen_modulo_secuencial set numero_secuencial_gemos="+nuevo_valor+" where ide_gemod="+modulo);
		System.out.println("update gen_modulo_secuencial set numero_secuencial_gemos="+nuevo_valor+" where ide_gemod="+modulo);
		return mensaje;
	}
	/**
	 * Metodo que permite generar el balance inicial
	 * @param ide_geani recibe el año fiscal para la generaciòn del balance inicial
	 */
	public void generarBalanceInicial(String ide_geani,String ide_movimiento){
		TablaGenerica anio = utilitario.consultar("select * from gen_anio where ide_geani ="+ide_geani);
		utilitario.getConexion().ejecutarSql("delete from cont_balance_inicial where ide_geani="+ide_geani);
		utilitario.getConexion().ejecutarSql("insert into cont_balance_inicial (ide_cobai,ide_cocac,ide_geani,valor_debe_cobai,valor_haber_cobai,valor_descripcion_cobai,activo_cobai)"
	+" select row_number() over(order by a.ide_cocac) + (select (case when max(ide_cobai) is null then 0 else max(ide_cobai) end) as codigo from cont_balance_inicial) as codigo, a.ide_cocac,"
	+" ide_geani,0 as debe,0 as haber,'BALANCE INICIAL '||"+anio.getValor("detalle_geani")+" as detalle,false from cont_catalogo_cuenta a, cont_vigente b where a.ide_cocac= b.ide_cocac and ide_geani ="+ide_geani+" group by a.ide_cocac,ide_geani");
		
		TablaGenerica actualiza_cuenta=utilitario.consultar(sumaDebeHaberCuentaContable(ide_movimiento));
		for (int i=0;i<actualiza_cuenta.getTotalFilas();i++){
		utilitario.getConexion().ejecutarSql(updateBalanceInicial(actualiza_cuenta.getValor(i,"debe"),actualiza_cuenta.getValor(i,"haber"),actualiza_cuenta.getValor(i,"ide_cocac"),ide_geani));
		}
		
		TablaGenerica nivel_maximo = utilitario.consultar(nivelMaximoCuentaContable());
		Integer contador = pckUtilidades.CConversion.CInt(nivel_maximo.getValor("nivel"));
		while (contador>1){
			TablaGenerica suma_balance=utilitario.consultar("select con_ide_cocac,sum(valor_debe_cobai) as debe,sum(valor_haber_cobai) as haber from ("
					+" select  a.ide_cocac,ide_geani,valor_debe_cobai,valor_haber_cobai,con_ide_cocac" 
					+" from cont_balance_inicial a, cont_catalogo_cuenta b where a.ide_cocac= b.ide_cocac and  ide_geani="+ide_geani+" and nivel_cocac="+contador
					+" ) a group by con_ide_cocac");
			for(int i=0;i<suma_balance.getTotalFilas();i++){
				utilitario.getConexion().ejecutarSql(updateBalanceInicial(suma_balance.getValor(i,"debe"), suma_balance.getValor(i,"debe"), suma_balance.getValor(i, "con_ide_cocac"), ide_geani));
			}
		   -- contador ; 
		}
		
	}
	
	/**
	 * Metodo que devuelve el sql para actualizar el balance inicial
	 * @param valor_debe recibe el valor al debe que va a ser actualizado
	 * @param valor_haber recibe el valor al haber que va a ser actualizado
	 * @param ide_cocac recibe ide de la cuenta contable a ser actualizado
	 * @param ide_geani recibe el año fiscal
	 * @return String sql para actualizar balance inicial
	 */
	public String updateBalanceInicial(String valor_debe,String valor_haber,String ide_cocac,String ide_geani){
		    String sql="update cont_balance_inicial"
					+" set valor_debe_cobai ="+valor_debe+","
					+" valor_haber_cobai ="+valor_haber+" where ide_cocac ="+ide_cocac+" and ide_geani="+ide_geani; 
			return sql;
	}
	
	
	/**
	 * Metodo que devuelve el sql para actualizar el balance de comprobacion
	 * @param valor_debe recibe el valor al debe que va a ser actualizado
	 * @param valor_haber recibe el valor al haber que va a ser actualizado
	 * @param ide_cocac recibe ide de la cuenta contable a ser actualizado
	 * @param ide_geani recibe el año fiscal
	 * @param ide_gemes recibe el mes
	 * @return String sql para actualizar balance comprobacion
	 */
	public String updateBalanceComprobacion(String valor_debe,String valor_haber,String ide_cocac,String ide_geani,String ide_gemes){
		    String sql="update cont_balance_comprobacion"
					+" set debe"+ide_gemes+"_cobac ="+valor_debe+","
					+" haber"+ide_gemes+"_cobac ="+valor_haber+" where ide_cocac ="+ide_cocac+" and ide_geani="+ide_geani; 
		    //System.out.println("actualizo balance "+sql);
		    return sql;
	}
	/**
	 * Metodo que devuelve la suma del debe y el haber por cuenta contable y movimientos contables
	 * @param ide_movimiento recibe el codigo del asiento contable
	 * @return String suma debe y haber por cuenta contable
	 */
	public String sumaDebeHaberCuentaContable(String ide_movimiento){
		    String sql="select ide_cocac,sum (debe_codem) as debe,sum(haber_codem) as haber from ("
	+" select ide_cocac,debe_codem,haber_codem from cont_detalle_movimiento where ide_comov in ("+ide_movimiento+")"
	+" ) a group by ide_cocac"; 
		    System.out.println("imprimo para mayorizar "+sql);
			return sql;
	}
	
	/**
	 * Metodo que devuelve el codigo maximo del nivel de cuenta contable
	 * @return String nivel maximo
	 */
	public String nivelMaximoCuentaContable(){
		    String sql="select 1 as codigo, max(nivel_cocac) as nivel from cont_catalogo_cuenta"; 
			return sql;
	}
	/**
	 * Metodo que devuelve los periodos contables por año vigente
	 * @param ide_geani recibe el codigo del año fiscal
	 * @return String sql de los movimientos contables
	 */
	public String getMovimientosContables(String ide_geani,String ide_cotia,String ide_cotim){
		    String sql="select ide_comov,nro_comprobante_comov,mov_fecha_comov,detalle_comov,ide_geani,ide_gemes from cont_movimiento where ide_geani ="+ide_geani;
		    
		    if(!ide_cotia.equals("-1") && !ide_cotim.equals("-1"))
		    	sql+=" and ide_cotia in ("+ide_cotia+") and ide_cotim in ("+ide_cotim+") "; 
		    
		    sql+=" order by nro_comprobante_comov desc,mov_fecha_comov desc"; 
			return sql;
	}
	/**
	 * Metodo que devuelve los asientos contables con las respectivas sumas al debe y al haber
	 * @return String sql de los movimientos contables
	 */
	public String getMovimientosContablesSumaDebeHaber(String ide_geani,String ide_gemes, String estado,String ide_cotia){
		    String sql="select a.ide_comov,nro_comprobante_comov,mov_fecha_comov,detalle_comov,"
			+" (case when debe is  null then 0 else debe end) as debe,(case when haber is null then 0 else haber end) as haber,"
			+" (case when debe is  null then 0 else debe end) - (case when haber is null then 0 else haber end) as diferencia,"
			+" (case when b.ide_comov is null then 0 else 1 end ) as detalle_asiento, responsable, aprobado "
			+" from cont_movimiento a"
			+" left join ( select sum(debe_codem) as debe, sum(haber_codem) as haber,ide_comov" 
			+"            from cont_detalle_movimiento group by ide_comov  ) b on  a.ide_comov = b.ide_comov"
			+" left join (select distinct ide_gtemp, coalesce(nom_usua,'') as responsable from sis_usuario where ide_gtemp is not null) us on us.ide_gtemp=a.ide_gtemp"
			+" left join (SELECT distinct ide_geedp, coalesce(nom_usua,'') as aprobado FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR JOIN sis_usuario us ON us.IDE_GTEMP=EPAR.IDE_GTEMP) ap on ap.ide_geedp=a.ide_geedp"
			+" where  ide_geani = "+ide_geani+" and ide_gemes in ("+ide_gemes+") and activo_comov in ("+estado+") and not ide_cotia= "+ide_cotia
			+" order by nro_comprobante_comov desc,mov_fecha_comov desc"; 
		    System.out.println("cargando para mayorizar "+sql);
			return sql;
	}
	
	public String getMovimientosContablesSumaDebeHaberIngresoGasto(String ide_geani,String ide_gemes, String estado,String condicion){
	    String sql="select a.ide_comov,nro_comprobante_comov,mov_fecha_comov,detalle_comov,"
		+" (case when debe is  null then 0 else debe end) as debe,(case when haber is null then 0 else haber end) as haber,"
		+" (case when debe is  null then 0 else debe end) - (case when haber is null then 0 else haber end) as diferencia,"
		+" (case when b.ide_comov is null then 0 else 1 end ) as detalle_asiento, responsable, aprobado "
		+" from cont_movimiento a"
		+" left join ( select sum(debe_codem) as debe, sum(haber_codem) as haber,ide_comov" 
		+"            from cont_detalle_movimiento group by ide_comov  ) b on  a.ide_comov = b.ide_comov"
		+" left join (select distinct ide_gtemp, coalesce(nom_usua,'') as responsable from sis_usuario where ide_gtemp is not null) us on us.ide_gtemp=a.ide_gtemp"
		+" left join (SELECT distinct ide_geedp, coalesce(nom_usua,'') as aprobado FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR JOIN sis_usuario us ON us.IDE_GTEMP=EPAR.IDE_GTEMP) ap on ap.ide_geedp=a.ide_geedp"
		+" where  ide_geani = "+ide_geani+" and ide_gemes in ("+ide_gemes+") and ap.ide_geedp "+(estado.equals("true")?"is not null":"is null")+" "+condicion
		+" order by nro_comprobante_comov desc,mov_fecha_comov desc"; 
	    System.out.println("cargando para aprobar "+sql);
		return sql;
	}
	/**
	 * Metodo que devuelve los asientos contables con las respectivas sumas al debe y al haber por fechas
	 * @return String sql de los movimientos contables
	 */
	public String getMovimientosContablesSumaDebeHaberFecha(String fecha_inicial, String fecha_final, String ide_conac){
		    /*String sql="select ide_cocac,sum(debe) as debe,sum(haber) as haber"
		    		+" from cont_movimiento a"
		    		+" left join ( select (case when debe_codem is null then 0 else debe_codem end) as debe," 
		    			+" 	(case when haber_codem is null then 0 else haber_codem end) as haber,ide_comov,ide_cocac"
		    			+" 	from cont_detalle_movimiento ) b on  a.ide_comov = b.ide_comov"
		    			+" 	where  ide_geani = 8 and not ide_cocac is null and not ide_cotia= 4 and mov_fecha_comov between '2016-01-01' and '2016-01-31'"
		    			+" 	group by ide_cocac"; 
		    */
		
			String sql="select mov.ide_comov, mov_fecha_comov, detalle_conac, detalle_comov,  debe,  haber, debe-haber as diferencia_asiento, "
					+" coalesce(coalesce(neto_contabilizado,neto_contabilizado_a)+coalesce(iva_contabilizado,iva_contabilizado_a),0)+coalesce(interes_contabilizado,0) as contabilizado, " 
					+" (debe - (coalesce(coalesce(neto_contabilizado,neto_contabilizado_a)+coalesce(iva_contabilizado,iva_contabilizado_a),0)+(coalesce(interes_contabilizado,0)*2))) as diferencia "
					+" from cont_movimiento mov"
					+" left join cont_nombre_asiento_contable conac on conac.ide_conac=mov.ide_conac"
					+" left join ( select ide_comov, sum(coalesce(debe_codem ,0 )) as debe, "
					+" 	      sum(coalesce(haber_codem ,0 )) as haber"
					+" 	      from cont_detalle_movimiento "
					+" 	      group by ide_comov"
					+" 	    ) b on  b.ide_comov = mov.ide_comov"
		
					/*+" left join (select cfa.ide_comov,cfa.ide_conac, sum(coalesce(valor_cobro_facob,base_aprobada_fafac)) as neto_contabilizado, sum(coalesce(valor_cobro_iva_facob,valor_iva_fafac)) as iva_contabilizado"
					+" 		from cont_factura_asiento cfa"
					+" 		join fac_factura fac on fac.ide_fafac=cfa.ide_fafac"
					+" 		left join fac_cobro fc on fc.ide_facob = cfa.ide_facob"
					+" 		group by cfa.ide_comov,cfa.ide_conac"
					+" 		) faco on faco.ide_comov=mov.ide_comov and faco.ide_conac=mov.ide_conac "; */
					
					+" left join (select cfa.ide_comov,cfa.ide_conac, sum(case when ide_gemod=34 then -1*coalesce(valor_referencial_fanoc,0) else coalesce(valor_cobro_facob,base_aprobada_fafac) end) as neto_contabilizado,  "
					+ " sum(case when ide_gemod=34 then -1*coalesce(iva_fanoc,0) else coalesce(valor_cobro_iva_facob,valor_iva_fafac) end) as iva_contabilizado  "
					+ "	 from cont_factura_asiento cfa  "
					+ "	 join cont_nombre_asiento_contable cas on cas.ide_conac=cfa.ide_conac "
					+ "	 join fac_factura fac on fac.ide_fafac=cfa.ide_fafac  "
					+ "	 left join fac_cobro fc on fc.ide_facob = cfa.ide_facob  "
					+ "	 left join fac_nota_credito nc on nc.ide_fafac=fac.ide_fafac "
					+ "	 group by cfa.ide_comov,cfa.ide_conac ,ide_gemod"
					+" 		) faco on faco.ide_comov=mov.ide_comov and faco.ide_conac=mov.ide_conac "; 
			
			sql+=" left join ( select nda.ide_comov,nda.ide_conac, sum(coalesce(interes_generado_fanod,0)) as interes_contabilizado  "
					+ "	from cont_nota_debito_asiento nda 	 "
					+ "	join fac_nota_debito nd on nd.ide_fanod=nda.ide_fanod  "
					+ "	group by nda.ide_comov,nda.ide_conac  "
					+ " ) naco on naco.ide_comov=mov.ide_comov and naco.ide_conac=mov.ide_conac   "; 
			
			     sql+=" left join (select caa.ide_comov,caa.ide_conac, sum(coalesce(valor_cobro_facob)) as neto_contabilizado_a, sum(coalesce(valor_cobro_iva_facob)) as iva_contabilizado_a "
					+" from cont_anticipo_asiento caa "
					+" left join fac_cobro fc on fc.ide_facob = caa.ide_facob 	"
					+" group by caa.ide_comov,caa.ide_conac 	"
					+" ) aco on aco.ide_comov=mov.ide_comov and aco.ide_conac=mov.ide_conac  "; 
			
			sql+=" where mov_fecha_comov between '"+fecha_inicial+"' and '"+fecha_final+"' ";
			
			if( pckUtilidades.CConversion.CInt(ide_conac)>0 )
				sql+=" and mov.ide_conac = "+ide_conac;
			else
				sql+=" and mov.ide_conac in (select ide_conac from cont_nombre_asiento_contable where ide_gemod in (1,33,34)) ";
			
			if( fecha_inicial.equals("1900-01-01") )
				sql+=" limit 0 ";
			else 
				sql+=" order by  mov_fecha_comov, detalle_conac";
		
		    System.out.println("getMovimientosContablesSumaDebeHaberFecha "+sql);
			return sql;
	}
		
	/**
	 * Metodo que permite generar el balance de comprobaciòn
	 * @param ide_geani recibe el año fiscal para la generaciòn del balance inicial
	 * @param ide_geani recibe el año fiscal para la generaciòn del balance inicial
	 */
	
	 
	public void generarBalanceComprobacion(String ide_geani,String ide_gemes,String ide_cotia){
		TablaGenerica anio = utilitario.consultar("select * from gen_anio where ide_geani ="+ide_geani);
		utilitario.getConexion().ejecutarSql("delete from cont_balance_comprobacion where ide_geani="+ide_geani+"; ");
		
		String inserta_balance_comprobacion="insert into cont_balance_comprobacion (ide_cobac,ide_cocac,ide_geani,debe1_cobac,haber1_cobac,debe2_cobac,haber2_cobac,debe3_cobac,haber3_cobac"
				+" ,debe4_cobac,haber4_cobac,debe5_cobac,haber5_cobac,debe6_cobac,haber6_cobac,debe7_cobac,haber7_cobac,debe8_cobac,haber8_cobac,debe9_cobac,haber9_cobac"
				+" ,debe10_cobac,haber10_cobac,debe11_cobac,haber11_cobac,debe12_cobac,haber12_cobac)"
				+" select row_number() over(order by a.ide_cocac) + (select (case when max(ide_cobac) is null then 0 else max(ide_cobac) end) as codigo from cont_balance_comprobacion) as codigo,"
				+" a.ide_cocac,a.ide_geani,0 as debe1, 0 as haber1,0 as debe2, 0 as haber2,0 as debe3, 0 as haber3,0 as debe4, 0 as haber4,0 as debe5, 0 as haber5"
				+" ,0 as debe6, 0 as haber6,0 as debe7, 0 as haber7,0 as debe8, 0 as haber8,0 as debe9, 0 as haber9,0 as debe10, 0 as haber10"
				+" ,0 as debe11, 0 as haber11,0 as debe12, 0 as haber12"
				+" from ( select a.ide_cocac,ide_geani "
				+" from cont_catalogo_cuenta a, cont_vigente b where a.ide_cocac= b.ide_cocac" 
				+" and ide_geani ="+ide_geani+" group by a.ide_cocac,ide_geani ) a"
				+" left join cont_balance_comprobacion b on a.ide_cocac = b.ide_cocac and a.ide_geani = b.ide_geani"
				+" where b.ide_cocac is null;";
				
		utilitario.getConexion().ejecutarSql(inserta_balance_comprobacion);
		TablaGenerica consulta_movimiento =utilitario.consultar("select * from cont_movimiento where ide_gemes ="+ide_gemes+" and ide_geani="+ide_geani+" and not ide_cotia in ("+ide_cotia+")");
		TablaGenerica consulta_cuadre_asientos=utilitario.consultar(getMovimientosContablesSumaDebeHaber(ide_geani, ide_gemes, "false", ide_cotia));
		consulta_cuadre_asientos.imprimirSql();
		double debe=0;
		double haber=0;
		
		
		for (int i=0;i<consulta_cuadre_asientos.getTotalFilas();i++){
			// tabla generica permite consultar las sumas de 
	
			debe=pckUtilidades.CConversion.CDbl_2(consulta_cuadre_asientos.getValor(i,"debe"));
		    haber=pckUtilidades.CConversion.CDbl_2(consulta_cuadre_asientos.getValor(i,"haber"));
		    
			if(debe==haber){
				//System.out.println("entro actualizar los asientos "+consulta_cuadre_asientos.getValor(i, "ide_comov"));
			    utilitario.getConexion().ejecutarSql("update cont_movimiento set activo_comov=true where ide_geedp is not null and ide_comov ="+consulta_cuadre_asientos.getValor(i,"ide_comov"));	
			}			
		}
		
		//
		TablaGenerica consulta_cuentas_actualizar_balance = utilitario.consultar(sumaDebeHaberCuentaContable("select ide_comov from cont_movimiento where ide_gemes ="+ide_gemes+" and ide_geani="+ide_geani+" and not ide_cotia in ("+ide_cotia+") and activo_comov=true "));
		consulta_cuentas_actualizar_balance.imprimirSql();
		for (int j=0;j<consulta_cuentas_actualizar_balance.getTotalFilas();j++){
			utilitario.getConexion().ejecutarSql(updateBalanceComprobacion(consulta_cuentas_actualizar_balance.getValor(j,"debe"), consulta_cuentas_actualizar_balance.getValor(j, "haber"), consulta_cuentas_actualizar_balance.getValor(j,"ide_cocac"), ide_geani, ide_gemes));
		}
		
		TablaGenerica nivel_maximo = utilitario.consultar(nivelMaximoCuentaContable());
		Integer contador = pckUtilidades.CConversion.CInt(nivel_maximo.getValor("nivel"));
		while (contador>1){
			/* TablaGenerica suma_balance=utilitario.consultar("select con_ide_cocac,sum(debe) as debe,sum(haber) as haber from ("
	+" select  a.ide_cocac,ide_geani,debe"+ide_gemes+"_cobac as debe,haber"+ide_gemes+"_cobac as haber,con_ide_cocac"
	+" from cont_balance_comprobacion a, cont_catalogo_cuenta b where a.ide_cocac= b.ide_cocac and  ide_geani="+ide_geani+" and nivel_cocac="+contador
	+" ) a group by con_ide_cocac");
			
			System.out.println("va siguiente sistem ");
			suma_balance.imprimirSql();
			for(int k=0;k<suma_balance.getTotalFilas();k++){
				utilitario.getConexion().ejecutarSql(updateBalanceComprobacion(suma_balance.getValor(k,"debe"), suma_balance.getValor(k, "haber"), suma_balance.getValor(k,"ide_cocac"), ide_geani, ide_gemes));
			}*/
			utilitario.getConexion().ejecutarSql("update cont_balance_comprobacion"
	+" set debe"+ide_gemes+"_cobac =debe,"
	+" haber"+ide_gemes+"_cobac =haber "
	+" from ("
	+" select con_ide_cocac,sum(debe) as debe,sum(haber) as haber from ("
	+" select  a.ide_cocac,ide_geani,debe"+ide_gemes+"_cobac as debe,haber"+ide_gemes+"_cobac as haber,con_ide_cocac"
	+" from cont_balance_comprobacion a, cont_catalogo_cuenta b where a.ide_cocac= b.ide_cocac and  ide_geani="+ide_geani+" and nivel_cocac="+contador
	+" ) a group by con_ide_cocac"
	+" )  a"
	+" where cont_balance_comprobacion.ide_cocac = a.con_ide_cocac");
		   -- contador ; 
		}
		
	}
	
	public void desmayorizaAsientos(String ide_geani,String ide_gemes){
		utilitario.getConexion().ejecutarSql("update cont_movimiento set activo_comov=false where ide_geani="+ide_geani+" and ide_gemes ="+ide_gemes);
	}
	
	public void aprobarAsientos(String ide_geani, String ide_gemes, String ide_comov, String ide_geedp){
		String sql="update cont_movimiento set ide_geedp="+ide_geedp+", fecha_actua=now(), hora_actua=now() where ide_gtemp is not null and ide_geani="+ide_geani+" and ide_gemes ="+ide_gemes+" and ide_comov in ("+ide_comov+");";
		System.out.println("aprobarAsientos "+sql);
		utilitario.getConexion().ejecutarSql(sql);
		
		sql="update tes_comprobante_pago set aprobado_tecpo=true, fecha_actua=now(), hora_actua=now() where ide_comov in ("+ide_comov+") and ide_geani = "+ide_geani;
		System.out.println("aprobarComprobantesPago "+sql);
		utilitario.getConexion().ejecutarSql(sql);
	}
	
	public void desAprobarAsientos(String ide_geani, String ide_gemes, String ide_comov){
		String sql="update cont_movimiento set ide_geedp=null, fecha_actua=now(), hora_actua=now() where activo_comov=false and ide_geani="+ide_geani+" and ide_gemes ="+ide_gemes+" and ide_comov in ("+ide_comov+");";
		System.out.println("desaprobarAsientos "+sql);
		utilitario.getConexion().ejecutarSql(sql);
		
		sql="update tes_comprobante_pago set aprobado_tecpo=false, fecha_actua=now(), hora_actua=now() where ide_comov in ( select ide_comov from cont_movimiento where ide_geedp is null and activo_comov=false and ide_comov in ("+ide_comov+")) and ide_geani = "+ide_geani;
		System.out.println("desaprobarComprobantesPago "+sql);
		utilitario.getConexion().ejecutarSql(sql);
	}
	
	/**
	 * Metodo que devuelve el Mayor Analitico
	 * @param fecha_inicial
	 * @para fecha_final
	 * @return String sql de los movimientos contables
	 */
	public String getMayorAnalitico(String fecha_inicial,String fecha_final,String ide_cocac, String anio){

		/*String sql="select a.ide_comov,mov_fecha_comov,detalle_comov,nro_comprobante_comov,"
				+" cue_codigo_cocac,cue_descripcion_cocac,debe_codem,haber_codem,detalle_gemes,fecha_ejecucion_prmen,devengado_prmen,cobrado_prmen,pagado_prmen,estado"
				+" from ("
				+" 	 select ide_comov,mov_fecha_comov,detalle_comov,nro_comprobante_comov,detalle_gemes,"
				+" 	 (case when activo_comov = false then 'NO MAYORIZADO' else 'MAYORIZADO' end) as estado"
				+" 	 from cont_movimiento a, gen_mes b where a.ide_gemes = b.ide_gemes"
				+" ) a"
				+" left join ("
				+" 	 select a.ide_comov,debe_codem,haber_codem,cue_codigo_cocac,cue_descripcion_cocac,b.ide_cocac "
				+" 	 ,fecha_ejecucion_prmen,devengado_prmen,cobrado_prmen,pagado_prmen"
				+" 	 from cont_detalle_movimiento a"
				+" 	 left join cont_catalogo_cuenta b on b.ide_cocac = a.ide_cocac"
				+" 	 left join (select ide_codem,fecha_ejecucion_prmen,sum(coalesce(devengado_prmen,0)) as devengado_prmen"
				+" 	   ,sum(coalesce(cobrado_prmen,0)) as cobrado_prmen,sum(coalesce(pagado_prmen,0)) as pagado_prmen"
				+" 	   from pre_mensual m "
				+" left join pre_anual a on a.ide_pranu=m.ide_pranu where ide_codem is not null "//+" and ide_geani="+anio
				+" group by ide_codem,fecha_ejecucion_prmen) ejc on ejc.ide_codem=a.ide_codem"
				+" ) b on a.ide_comov = b.ide_comov"
				+" where mov_fecha_comov between '"+fecha_inicial+"' and '"+fecha_final+"'"
				+" and ide_cocac IN ("+ide_cocac+")"
				+" order by cue_codigo_cocac,mov_fecha_comov,nro_comprobante_comov";*/
		
		String sql = "select ide_comov,ide_codem,mov_fecha_comov,tipo_movimiento,detalle_comov,asiento_tipo,nro_comprobante_comov, cue_codigo_cocac,cue_descripcion_cocac,debe_codem,haber_codem, "+
					      " sum(debe_codem-haber_codem) OVER (PARTITION BY cue_codigo_cocac ORDER BY mov_fecha_comov) AS saldo  "+
					      " ,detalle_gemes,fecha_ejecucion_prmen,devengado_prmen,cobrado_prmen,pagado_prmen,estado "+
					  " from ( ";		

		    sql+="select a.ide_comov,b.ide_codem,mov_fecha_comov,tipo_movimiento,detalle_comov,asiento_tipo,nro_comprobante_comov,"
				+" cue_codigo_cocac,cue_descripcion_cocac,debe_codem,haber_codem,detalle_gemes,fecha_ejecucion_prmen,devengado_prmen,cobrado_prmen,pagado_prmen,estado"
				+" from ("
				+" 	 select ide_comov,mov_fecha_comov,detalle_comov,detalle_cotim as tipo_movimiento,detalle_conac as asiento_tipo,nro_comprobante_comov,detalle_gemes,"
				+" 	 (case when activo_comov = false then 'NO MAYORIZADO' else 'MAYORIZADO' end) as estado"
				+" 	 FROM cont_movimiento a "
				+ "  left join gen_mes b on b.ide_gemes = a.ide_gemes "
				+ "  left join cont_tipo_movimiento tm on tm.ide_cotim=a.ide_cotim "
				+ "  left join cont_nombre_asiento_contable nac on nac.ide_conac=a.ide_conac "
				+" ) a"
				+" left join ("
				+" 	 select a.ide_comov,a.ide_codem,debe_codem,haber_codem,cue_codigo_cocac,cue_descripcion_cocac,b.ide_cocac "
				+" 	 ,fecha_ejecucion_prmen,devengado_prmen,cobrado_prmen,pagado_prmen"
				+" 	 from cont_detalle_movimiento a"
				+" 	 left join cont_catalogo_cuenta b on b.ide_cocac = a.ide_cocac"
				+" 	 left join (select ide_codem,fecha_ejecucion_prmen,sum(coalesce(devengado_prmen,0)) as devengado_prmen"
				+" 	   ,sum(coalesce(cobrado_prmen,0)) as cobrado_prmen,sum(coalesce(pagado_prmen,0)) as pagado_prmen"
				+" 	   from pre_mensual m "
				+" left join pre_anual a on a.ide_pranu=m.ide_pranu where ide_codem is not null "//+" and ide_geani="+anio
				+" group by ide_codem,fecha_ejecucion_prmen) ejc on ejc.ide_codem=a.ide_codem"
				+" ) b on a.ide_comov = b.ide_comov"
				+" where mov_fecha_comov between '"+fecha_inicial+"' and '"+fecha_final+"'"
				+" and ide_cocac IN ("+ide_cocac+")"
				+" order by cue_codigo_cocac,mov_fecha_comov,nro_comprobante_comov ";
		    
		    sql += " ) mayor "+
					" order by mov_fecha_comov ";
		
		System.out.println("getMayorAnalitico: "+sql);
			return sql;
	}
	
	public String getMayorAnalitico_detallado(String fecha_inicial,String fecha_final,String ide_cocac, String anio)
	{
	    
		String sql="select a.ide_comov,mov_fecha_comov,detalle_comov,nro_comprobante_comov,"
				+" cue_codigo_cocac,cue_descripcion_cocac,debe_codem,haber_codem,detalle_gemes,detalle,fecha_ejecucion_prmen,devengado_prmen,cobrado_prmen,pagado_prmen,estado"
				+" from ("
				+" 	 select ide_comov,mov_fecha_comov,detalle_comov,nro_comprobante_comov,detalle_gemes,"
				+" 	 (case when activo_comov = false then 'NO MAYORIZADO' else 'MAYORIZADO' end) as estado"
				+" 	 from cont_movimiento a, gen_mes b where a.ide_gemes = b.ide_gemes"
				+" ) a"
				
				+" left join ("
				+" 	 select a.ide_comov,debe_codem,haber_codem,cue_codigo_cocac,cue_descripcion_cocac,b.ide_cocac "
				+" 	 , detalle,fecha_ejecucion_prmen,devengado_prmen,cobrado_prmen,pagado_prmen"
				+" 	 from cont_detalle_movimiento a"
				+" 	 left join cont_catalogo_cuenta b on b.ide_cocac = a.ide_cocac"
				+" 	 left join (select ide_codem,fecha_ejecucion_prmen,coalesce(dpa.detalle,g_dpa.detalle) as detalle,sum(coalesce(devengado_prmen,0)) as devengado_prmen"
				+" 	   ,sum(coalesce(cobrado_prmen,0)) as cobrado_prmen,sum(coalesce(pagado_prmen,0)) as pagado_prmen"
				+" 	   from pre_mensual m "
				+" left join pre_anual a on a.ide_pranu=m.ide_pranu "
				+" left join (select a.ide_pranu,codigo_clasificador_prcla||'-'||coalesce(detalle_geani,' ') ||'-'|| substring(descripcion_clasificador_prcla from 1 for 60) ||'-'|| coalesce(detalle_prfuf,' ') as detalle" 
					+" from pre_anual a "
				    +" left join pre_clasificador c on c.ide_prcla= a.ide_prcla "
				    +" left join pre_fuente_financiamiento ff on ff.ide_prfuf=a.ide_prfuf  "
				    +" left join gen_anio anio on anio.ide_geani=a.ide_geani "
				    +" order by codigo_clasificador_prcla,detalle_geani) dpa on dpa.ide_pranu=a.ide_pranu"
				    
				+" left join (select a.ide_pranu,ff.ide_prfuf,cast('(POA: ' as character(10)) || cast(a.ide_prpoa as character(10)) ||')-'|| codigo_clasificador_prcla||'-'||coalesce(detalle_geani,' ') ||'-'|| substring(descripcion_clasificador_prcla from 1 for 60) ||'-'|| coalesce(detalle_prfuf,' ') ||'-'|| coalesce(detalle_prtge,' ')  as detalle" 
				+" from pre_anual a "
				+" left join pre_poa p on p.ide_prpoa= a.ide_prpoa "
				+" left join pre_tipo_gestion tg on tg.ide_prtge = p.ide_prtge " 
				+" left join pre_clasificador c on c.ide_prcla= p.ide_prcla "
				+" left join pre_poa_financiamiento pf on pf.ide_prpoa= a.ide_prpoa "
				+" left join pre_fuente_financiamiento ff on ff.ide_prfuf=pf.ide_prfuf  "
				+" left join gen_anio anio on anio.ide_geani=a.ide_geani "
				+" order by codigo_clasificador_prcla,detalle_geani) g_dpa on g_dpa.ide_pranu=a.ide_pranu and g_dpa.ide_prfuf=m.ide_prfuf"
	
				+" where ide_codem is not null "//+" and ide_geani="+anio
				+" group by ide_codem,fecha_ejecucion_prmen,dpa.detalle,g_dpa.detalle) ejc on ejc.ide_codem=a.ide_codem"
				+" ) b on a.ide_comov = b.ide_comov"
				
				+" where mov_fecha_comov between '"+fecha_inicial+"' and '"+fecha_final+"'"
				+" and ide_cocac IN ("+ide_cocac+")"
				+" order by cue_codigo_cocac,mov_fecha_comov,nro_comprobante_comov";
		
		System.out.println("getMayorAnalitico_detallado: "+sql);
			return sql;
	}
	
	public void limpiarAcceso(String tabla){
		String sql="delete from sis_bloqueo where tabla_bloq ilike '"+tabla+"';";
		utilitario.getConexion().ejecutarSql(sql);
	}
	/**
	 * Metodo que devuelve el sql para consultar lso anticpio pendientes de contabilizar de lso empleados
	 * @return String sql para contar anticpios pendientes
	 */
	public String getConsultaAnticipos(){
		    String sql="select a.ide_nrant as codigo,documento_identidad_gtemp,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp,detalle_nrmoa,fecha_solicitud_nrant,monto_aprobado_nrant "
					+" from nrh_anticipo a "
					+"  left join tes_comprobante_pago b on a.ide_nrant = b.ide_nrant "
					+"  left join nrh_motivo_anticipo c on a.ide_nrmoa = c.ide_nrmoa "
					+"  left join gth_empleado d on a.ide_gtemp = d.ide_gtemp "
					+"  where aprobado_nrant =true and a.activo_nrant=true  "
					+"  and b.ide_nrant is null "
					+" order by apellido_paterno_gtemp";
			return sql;
	}
	
	public String getConsultaOtrosAnticiposEmpleados(){
	    String sql="select ide_gtemp as codigo,documento_identidad_gtemp,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp,'' as detalle_nrmoa,now() as fecha_solicitud_nrant,0 as monto_aprobado_nrant"
	    		+"  from  gth_empleado "
				+"  where activo_gtemp=true "
				+" order by apellido_paterno_gtemp ";
		return sql;
}
	
	public String getTransferencias(String fecha_inicial, String fecha_final){
		
		String sql="select cat.ide_cocac as codigo, extract(month from DATE '"+fecha_final+"') as periodo, coalesce(cue_codigo_cocac,'000.00') || '.00' as cuenta,  "
				+ " cast('9999999999996' as varchar) as  ruc_recep, (select identificacion_empr from sis_empresa where ide_empr=0) as ruc_otorgante, deudor, acreedor, 0 as cuenta_monetaria  "
				+ " from cont_catalogo_cuenta cat "
				+ " left join ( select ide_cocac, mov_fecha_comov, sum(debe_codem) as deudor, sum(haber_codem) as acreedor from cont_detalle_movimiento dmov "
				    + " left join cont_movimiento mov on mov.ide_comov=dmov.ide_comov "
				    + " group by ide_cocac, mov_fecha_comov) dmov on dmov.ide_cocac=cat.ide_cocac "
				+ " where cue_codigo_cocac like '113.28%' and mov_fecha_comov between '"+fecha_inicial+"' and '"+fecha_final+"'";
	
		System.out.println("trans: "+sql);
		 return sql;
	}

	public TablaGenerica getTablaValidaAsientoApertura(String ide_geani){
		
		String sql=" SELECT ide_comov, mov_fecha_comov, detalle_comov, nro_comprobante_comov FROM cont_movimiento where ide_cotia=4 and ide_geani="+ide_geani;
	
		return utilitario.consultar(sql);
	}
	
	public TablaGenerica getTablaValidaAsientoCierre(String ide_geani){
			
			String sql=" SELECT ide_comov, mov_fecha_comov, detalle_comov, nro_comprobante_comov FROM cont_movimiento where ide_cotia=2 and ide_geani="+ide_geani;
		
			return utilitario.consultar(sql);
		}
		
	public TablaGenerica getTablaDetalleAsientoApertura(){
		
		String sql="";
		
		/*sql=" SELECT cat.ide_cocac, bal.ide_cocac, cue_codigo_cocac, debe_saldo_cobac, haber_saldo_cobac"
				+ " FROM cont_balance_comprobacion bal"
				+ " left join cont_catalogo_cuenta cat on cat.ide_cocac = bal.ide_cocac"
				+ " where extract(month from fecha_final_cobac)=12 and extract(year from fecha_final_cobac)<extract(year from now()) "
				+ " and coalesce(cat.apertura_cierre_cocac,0) in (1,3) "
				+ " and (abs(debe_saldo_cobac) + abs(haber_saldo_cobac))>0";
		
		sql += " union ";*/ //codigo anterior
				
		sql	+= " select cat.ide_cocac, cat.ide_cocac, cue_codigo_cocac, case when ac.ide_gelua = 1 then abs(valor) else 0 end as debe_saldo," 
				+ " case when ac.ide_gelua = 2 then abs(valor) else 0 end as haber_saldo"
				+ " FROM cont_balance_general bal"
				+ " join cont_asiento_apertura_cierre ac on ac.ide_cocac=bal.ide_cuenta"
				+ " join cont_catalogo_cuenta cat on cat.ide_cocac = ac.con_ide_cocac"
				+ " where periodo_final=12 and extract(year from fecha_final)<extract(year from now())" 
				+ " and coalesce(ac.apertura_cierre_asiac,0) in (1,3) "
				+ " and abs(valor)>0"
				+ " order by 1"; //generar todos los balances antes de generar el asiento de apertura de todo el año
	
		return utilitario.consultar(sql);
	}

	public TablaGenerica getTablaDetalleAsientoCierre(){
		
		String sql="";
		
		
		/*sql+=" SELECT ide_cobac, bal.ide_cocac, cue_codigo_cocac, debe_saldo_cobac as debe_saldo, haber_saldo_cobac as haber_saldo"
				+ " FROM cont_balance_comprobacion bal"
				+ " left join cont_catalogo_cuenta cat on cat.ide_cocac = bal.ide_cocac"
				+ " where extract(month from fecha_final_cobac)=12 and extract(year from fecha_final_cobac)<extract(year from now()) "
				+ " and coalesce(cat.apertura_cierre_cocac,0) in (2,3) "
				+ " and (abs(debe_saldo_cobac) + abs(haber_saldo_cobac))>0"
				+ " order by 1";
	
		sql += " union ";*/ // codigo antiguo
		
		sql	+= " select cat.ide_cocac, cat.ide_cocac, cue_codigo_cocac, case when ac.ide_gelua = 1 then abs(valor) else 0 end as debe_saldo," 
				+ " case when ac.ide_gelua = 2 then abs(valor) else 0 end as haber_saldo"
				+ " FROM cont_balance_general bal"
				+ " join cont_asiento_apertura_cierre ac on ac.ide_cocac=bal.ide_cuenta"
				+ " join cont_catalogo_cuenta cat on cat.ide_cocac = ac.con_ide_cocac"
				+ " where periodo_final=12 and extract(year from fecha_final)<=extract(year from now())" 
				+ " and coalesce(ac.apertura_cierre_asiac,0) in (2,3) "
				+ " and abs(valor)>0"
				+ " order by 1";
	
		return utilitario.consultar(sql);
	}

	public TablaGenerica getTablaAsientoAperturaArchivoPlano(String ide_comov){
		
		String sql=" SELECT periodo, (case when length(cuenta) < 7 then cuenta || '.00' else cuenta end) as cuenta, sum(debe_codem) as debe_codem, sum(haber_codem) as haber_codem  FROM"
				+ " (SELECT 1 as periodo, cue_codigo_cocac || '.00' as cuenta, debe_codem, haber_codem "
				+ " FROM cont_detalle_movimiento dmov"
				+ " left join cont_catalogo_cuenta cat on cat.ide_cocac =dmov.ide_cocac"
				+ " where ide_comov="+ide_comov+" and nivel_cocac=4"
				
				+ " union all"
				
				+ " SELECT 1 as periodo, (case when cat2.sigef_cocac=true then cat2.cue_codigo_cocac else cat.cue_codigo_cocac end) as cuenta, debe_codem, haber_codem "
				+ " FROM cont_detalle_movimiento dmov"
				+ " left join cont_catalogo_cuenta cat on cat.ide_cocac =dmov.ide_cocac "
				+ " left join cont_catalogo_cuenta cat2 on cat2.ide_cocac = cat.con_ide_cocac"
				+ " where ide_comov="+ide_comov+" and cat.nivel_cocac=5"
				
				+ " union all"
				
				+ " SELECT 1 as periodo,(case when cat3.sigef_cocac=true then cat3.cue_codigo_cocac else cat2.cue_codigo_cocac end) as cuenta, "
				+ " sum(debe_codem) as debe_codem, sum(haber_codem) as haber_codem "
				+ " FROM cont_detalle_movimiento dmov"
				+ " left join cont_catalogo_cuenta cat on cat.ide_cocac =dmov.ide_cocac"
				+ " left join cont_catalogo_cuenta cat2 on cat2.ide_cocac =cat.con_ide_cocac "
				+ " left join cont_catalogo_cuenta cat3 on cat3.ide_cocac =cat2.con_ide_cocac"
				+ " where ide_comov="+ide_comov+" and cat.nivel_cocac=6"
				+ " group by cuenta"
				
				+ " union all"
				
				+ " SELECT 1 as periodo,cat3.cue_codigo_cocac as cuenta, sum(debe_codem) as debe_codem, sum(haber_codem) as haber_codem "
				+ " FROM cont_detalle_movimiento dmov"
				+ " left join cont_catalogo_cuenta cat on cat.ide_cocac =dmov.ide_cocac"
				+ " left join cont_catalogo_cuenta cat2 on cat2.ide_cocac =cat.con_ide_cocac"
				+ " left join cont_catalogo_cuenta cat3 on cat3.ide_cocac =cat2.con_ide_cocac"
				+ " where ide_comov="+ide_comov+" and cat.nivel_cocac=7"
				+ " group by cuenta"
				
				+ " union all"
				
				+ " SELECT 1 as periodo,cat4.cue_codigo_cocac as cuenta, sum(debe_codem) as debe_codem, sum(haber_codem) as haber_codem "
				+ " FROM cont_detalle_movimiento dmov"
				+ " left join cont_catalogo_cuenta cat on cat.ide_cocac =dmov.ide_cocac"
				+ " left join cont_catalogo_cuenta cat2 on cat2.ide_cocac =cat.con_ide_cocac"
				+ " left join cont_catalogo_cuenta cat3 on cat3.ide_cocac =cat2.con_ide_cocac"
				+ " left join cont_catalogo_cuenta cat4 on cat4.ide_cocac =cat3.con_ide_cocac"
				+ " where ide_comov="+ide_comov+" and cat.nivel_cocac=8"
				+ " group by cuenta"
				
				+ " ) a"
				
				+ " group by periodo, cuenta"
				+ " order by 2 ";
	
		 return utilitario.consultar(sql);
	}

	public TablaGenerica getTablaBalanceComprobacionArchivoPlano(String anio, String nivel1, String nivel2){
		
		String sql="";
			/*="select bal.ide_cobac,bal.ide_cocac, extract(month from fecha_final_cobac) as periodo, "
				+" (case when nivel_cocac =4 then cue_codigo_cocac || '.00' else '000.00.00' end) as cuenta  "
				+" ,substring(cue_descripcion_cocac from 1 for 100)  as descripcion_cuenta "
				+" ,debe_inicial_cobac,haber_inicial_cobac,debe_periodo_cobac,haber_periodo_cobac, "
				+" debe_acumulado_cobac,haber_acumulado_cobac,debe_saldo_cobac,haber_saldo_cobac,fecha_inicial_cobac,fecha_final_cobac "
				+" from cont_balance_comprobacion bal "
				+" left join cont_catalogo_cuenta cat on cat.ide_cocac = bal.ide_cocac "
				+" where ide_geani="+anio+" and sigef_cocac=true and cat.nivel_cocac = "+nivel1 // --nivel 1
				+" and (abs(debe_inicial_cobac) + abs(haber_inicial_cobac) + abs(debe_acumulado_cobac) + abs(haber_acumulado_cobac) + abs(debe_periodo_cobac) + abs(haber_periodo_cobac) + abs(debe_saldo_cobac) + abs(haber_saldo_cobac))>0 "
				
				+" union all "
				
				+" select bal.ide_cobac,bal.ide_cocac, extract(month from fecha_final_cobac) as periodo, "
				+" (case when cat.nivel_cocac = 5 then cat.cue_codigo_cocac else '000.00.00' end) as cuenta "
				+" ,substring(cat.cue_descripcion_cocac from 1 for 100)  as descripcion_cuenta "
				+" ,debe_inicial_cobac,haber_inicial_cobac,debe_periodo_cobac,haber_periodo_cobac, "
				+" debe_acumulado_cobac,haber_acumulado_cobac,debe_saldo_cobac,haber_saldo_cobac,fecha_inicial_cobac,fecha_final_cobac "
				+" from cont_balance_comprobacion bal "
				+" left join cont_catalogo_cuenta cat on cat.ide_cocac = bal.ide_cocac "
				+" left join cont_catalogo_cuenta pcat on pcat.ide_cocac=cat.con_ide_cocac "
				+" where  ide_geani="+anio+" and coalesce(pcat.sigef_cocac,false)=false and cat.nivel_cocac = "+nivel2 //--nivel 2
				+" and (abs(debe_inicial_cobac) + abs(haber_inicial_cobac) + abs(debe_acumulado_cobac) + abs(haber_acumulado_cobac) + abs(debe_periodo_cobac) + abs(haber_periodo_cobac) + abs(debe_saldo_cobac) + abs(haber_saldo_cobac))>0 "
				
				+" order by cuenta ";*/
		
		sql+="select ide_cobac, ide_cocac, periodo, cuenta, descripcion_cuenta"
				+" ,case when cuenta like '212.40%' then 0.00 else debe_inicial_cobac end as debe_inicial_cobac"
				+" ,case when cuenta like '212.40%' then haber_inicial_cobac-debe_inicial_cobac else haber_inicial_cobac end as haber_inicial_cobac"
				+" ,debe_periodo_cobac"
				+" ,haber_periodo_cobac "
				+" ,case when cuenta like '212.40%' then debe_acumulado_cobac-debe_inicial_cobac else debe_acumulado_cobac end as debe_acumulado_cobac"
				+" ,case when cuenta like '212.40%' then haber_acumulado_cobac-debe_inicial_cobac else haber_acumulado_cobac end as haber_acumulado_cobac"
				+" ,case when debe_inicial_cobac=0 and debe_saldo_cobac<0 and (cuenta like '141.99%' or cuenta like '145.99%' or cuenta like '151.98%' or cuenta like '152.98%' or cuenta like '126.99%' ) then 0.00 else debe_saldo_cobac end as debe_saldo_cobac"
				+" ,case when debe_inicial_cobac=0 and debe_saldo_cobac<0 and (cuenta like '141.99%' or cuenta like '145.99%' or cuenta like '151.98%' or cuenta like '152.98%' or cuenta like '126.99%' ) then abs(debe_saldo_cobac) else haber_saldo_cobac end as haber_saldo_cobac"
				+" ,fecha_final_cobac"
				+" from ( ";
		
			sql+="select bal.ide_cobac,bal.ide_cocac, extract(month from fecha_final_cobac) as periodo, "
				+" (case when nivel_cocac =4 then cue_codigo_cocac || '.00' else '000.00.00' end) as cuenta  "
				+" ,substring(cue_descripcion_cocac from 1 for 100)  as descripcion_cuenta "
				+"  ,debe_inicial_cobac"
				+"  ,haber_inicial_cobac"
				+"  ,debe_acumulado_cobac + debe_periodo_cobac - debe_inicial_cobac as debe_periodo_cobac"
				+"  ,haber_acumulado_cobac + haber_periodo_cobac - haber_inicial_cobac as haber_periodo_cobac "
				+"  ,debe_periodo_cobac+debe_acumulado_cobac as debe_acumulado_cobac"
				+"  ,haber_periodo_cobac+ haber_acumulado_cobac as haber_acumulado_cobac"
				+"  ,debe_saldo_cobac,haber_saldo_cobac,fecha_final_cobac "
				+" from cont_balance_comprobacion bal "
				+" left join cont_catalogo_cuenta cat on cat.ide_cocac = bal.ide_cocac "
				+" where ide_geani="+anio+" and sigef_cocac=true and cat.nivel_cocac = "+nivel1 // --nivel 1
				+" and (abs(debe_inicial_cobac) + abs(haber_inicial_cobac) + abs(debe_acumulado_cobac) + abs(haber_acumulado_cobac) + abs(debe_periodo_cobac) + abs(haber_periodo_cobac) + abs(debe_saldo_cobac) + abs(haber_saldo_cobac))>0 "
				
				+" union all "
				
				+" select bal.ide_cobac,bal.ide_cocac, extract(month from fecha_final_cobac) as periodo, "
				+" (case when cat.nivel_cocac = 5 then cat.cue_codigo_cocac else '000.00.00' end) as cuenta "
				+" ,substring(cat.cue_descripcion_cocac from 1 for 100)  as descripcion_cuenta "
				+"  ,debe_inicial_cobac"
				+"  ,haber_inicial_cobac"
				+"  ,debe_acumulado_cobac + debe_periodo_cobac - debe_inicial_cobac as debe_periodo_cobac"
				+"  ,haber_acumulado_cobac + haber_periodo_cobac - haber_inicial_cobac as haber_periodo_cobac "
				+"  ,debe_periodo_cobac+debe_acumulado_cobac as debe_acumulado_cobac"
				+"  ,haber_periodo_cobac+ haber_acumulado_cobac as haber_acumulado_cobac"
				+"  ,debe_saldo_cobac,haber_saldo_cobac,fecha_final_cobac "
				+" from cont_balance_comprobacion bal "
				+" left join cont_catalogo_cuenta cat on cat.ide_cocac = bal.ide_cocac "
				+" left join cont_catalogo_cuenta pcat on pcat.ide_cocac=cat.con_ide_cocac "
				+" where  ide_geani="+anio+" and coalesce(pcat.sigef_cocac,false)=false and cat.nivel_cocac = "+nivel2 //--nivel 2
				+" and (abs(debe_inicial_cobac) + abs(haber_inicial_cobac) + abs(debe_acumulado_cobac) + abs(haber_acumulado_cobac) + abs(debe_periodo_cobac) + abs(haber_periodo_cobac) + abs(debe_saldo_cobac) + abs(haber_saldo_cobac))>0 "
				
				+" order by cuenta ";
			
			sql+=")a";
	
			System.out.println("getTablaBalanceComprobacionArchivoPlano: "+sql);
	
		 return utilitario.consultar(sql);
	}
	
	public void generarBalComprobacion(String anio,String fecha_inicial,String fecha_final,String tipo_asiento){
		String sql_borra_tabla="delete from cont_balance_comprobacion;";
		utilitario.getConexion().ejecutarSql(sql_borra_tabla);
		// inserto balance de comprobacion
		String sql="insert into cont_balance_comprobacion (ide_cobac,ide_cocac,ide_geani,debe1_cobac,haber1_cobac,debe2_cobac,haber2_cobac,debe3_cobac,haber3_cobac"
				+" ,debe4_cobac,haber4_cobac,debe5_cobac,haber5_cobac,debe6_cobac,haber6_cobac,debe7_cobac,haber7_cobac,debe8_cobac,haber8_cobac,debe9_cobac,haber9_cobac"
				+" ,debe10_cobac,haber10_cobac,debe11_cobac,haber11_cobac,debe12_cobac,haber12_cobac,debe_inicial_cobac,haber_inicial_cobac,debe_acumulado_cobac,haber_acumulado_cobac"
				+" ,debe_periodo_cobac,haber_periodo_cobac,debe_saldo_cobac,haber_saldo_cobac,fecha_inicial_cobac,fecha_final_cobac)"
				+" select row_number() over(order by a.ide_cocac) + (select (case when max(ide_cobac) is null then 0 else max(ide_cobac) end) as codigo from cont_balance_comprobacion) as codigo,"
				+" a.ide_cocac,a.ide_geani,0 as debe1, 0 as haber1,0 as debe2, 0 as haber2,0 as debe3, 0 as haber3,0 as debe4, 0 as haber4,0 as debe5, 0 as haber5"
				+" ,0 as debe6, 0 as haber6,0 as debe7, 0 as haber7,0 as debe8, 0 as haber8,0 as debe9, 0 as haber9,0 as debe10, 0 as haber10"
				+" ,0 as debe11, 0 as haber11,0 as debe12, 0 as haber12,0 as debe_inicial_cobac,0 as haber_inicial_cobac,0 as debe_acumulado_cobac,0 as haber_acumulado_cobac"
				+" ,0 as debe_periodo_cobac,0 as haber_periodo_cobac,0 as debe_saldo_cobac,0 as haber_saldo_cobac,'"+fecha_inicial+"' as fecha_inicial_cobac,'"+fecha_final+"' as fecha_final_cobac"
				+" from ( select a.ide_cocac,ide_geani "
					+" 	from cont_catalogo_cuenta a, cont_vigente b where a.ide_cocac= b.ide_cocac"
					+" 	and ide_geani ="+anio+" group by a.ide_cocac,ide_geani ) a"
					+" 	left join cont_balance_comprobacion b on a.ide_cocac = b.ide_cocac and a.ide_geani = b.ide_geani"
					+" 	where b.ide_cocac is null;";
		//System.out.println("imprimir sql insert "+sql);
		utilitario.getConexion().ejecutarSql(sql);
		
		// actualizo bance inicial
		String sql_inicial ="update cont_balance_comprobacion"
				+" set debe_inicial_cobac = debe ,"
				+" haber_inicial_cobac=haber"
				+" from ( "
					+" 	select ide_cocac,sum(debe) as debe,sum(haber) as haber"
					+" 	from cont_movimiento a"
					+" 	left join ( select (case when debe_codem is null then 0 else debe_codem end) as debe," 
					+" 			(case when haber_codem is null then 0 else haber_codem end) as haber,ide_comov,ide_cocac"
					+" 			from cont_detalle_movimiento ) b on  a.ide_comov = b.ide_comov"
					+" 			where  ide_geani = "+anio+"  and  ide_cotia= "+tipo_asiento+" and not ide_cocac is null"
					+" 			group by ide_cocac"
					+" 	) a"
					+" 	where a.ide_cocac = cont_balance_comprobacion.ide_cocac";
		
		utilitario.getConexion().ejecutarSql(sql_inicial);
	
		String var_sql_periodo=" and mov_fecha_comov between '"+fecha_inicial+"' and '"+fecha_final+"' and activo_comov=true ";
		String var_sql_acumulado=" and 1 !=1 ";
		
		TablaGenerica tab_mes_actual=utilitario.consultar("select 1 as codigo, 2 as resultado from tes_caja where not extract(month from cast('"+fecha_inicial+"' as date)) =extract(month from cast('"+fecha_final+"' as date))");
		if(tab_mes_actual.getTotalFilas()>0){
			var_sql_periodo=" and activo_comov=true  and mov_fecha_comov between cast (date_trunc('month',cast('"+fecha_final+"' as date))  as date) and '"+fecha_final+"'";
			var_sql_acumulado=" and 1=1";
		}
		// actualizo ejecutado periodo
		String sql_periodo="update cont_balance_comprobacion"
				+" 	set debe_periodo_cobac = debe ,"
				+" 	haber_periodo_cobac = haber"
				+" 	from ( "
					+" 		select ide_cocac,round(sum(debe),2) as debe,round(sum(haber),2) as haber"
					+" 		from cont_movimiento a"
					+" 		left join ( select (case when debe_codem is null then 0 else debe_codem end) as debe," 
					+" 				(case when haber_codem is null then 0 else haber_codem end) as haber,ide_comov,ide_cocac"
					+" 				from cont_detalle_movimiento ) b on  a.ide_comov = b.ide_comov"
					+" 				where  ide_geani = "+anio+" and not ide_cocac is null and not ide_cotia= "+tipo_asiento+var_sql_periodo
					+" 				group by ide_cocac"
					+" 		) a"
					+" 		where a.ide_cocac = cont_balance_comprobacion.ide_cocac";
		
		//System.out.println("sql_periodo "+sql_periodo);
		utilitario.getConexion().ejecutarSql(sql_periodo);
		
		// actualizo ejecutado acumulado
		String sql_acumulado="update cont_balance_comprobacion"
				+" set debe_acumulado_cobac = debe ,"
				+" haber_acumulado_cobac = haber"
				+" from ( "
					+"	select ide_cocac,round(sum(debe),2) as debe,round(sum(haber),2) as haber"
					+"	from cont_movimiento a"
					+"	left join ( select (case when debe_codem is null then 0 else debe_codem end) as debe," 
					+"			(case when haber_codem is null then 0 else haber_codem end) as haber,ide_comov,ide_cocac"
					+"			from cont_detalle_movimiento ) b on  a.ide_comov = b.ide_comov"
					+"			where  ide_geani = "+anio+" and not ide_cocac is null and not ide_cotia= "+tipo_asiento+" and activo_comov=true and mov_fecha_comov between '"+fecha_inicial+"' and cast (  extract (year from cast('"+fecha_final+"' as date))||'-'|| extract (month from cast('"+fecha_final+"' as date)) -1 ||'-'||extract (day from (select date_trunc('month',cast((cast('"+fecha_final+"' as date) - interval '1 month') as date)) +'1month' ::interval -'1sec' ::interval))  as date)"
					+"	group by ide_cocac"
					+"	) a where a.ide_cocac = cont_balance_comprobacion.ide_cocac "+var_sql_acumulado;
		//System.out.println("sql_acumulado "+sql_acumulado);
		utilitario.getConexion().ejecutarSql(sql_acumulado);
		
		///////////////////////////////////////////////////
		/*String sqlActualiza="select ide_cobac,"
				+" case when cue_codigo_cocac like '212.40%' then 0.00 else debe_inicial_cobac end as debe_inicial_cobac, "
				+" case when cue_codigo_cocac like '212.40%' then haber_inicial_cobac-debe_inicial_cobac else haber_inicial_cobac end as haber_inicial_cobac,"
				+" case when cue_codigo_cocac like '212.40%' then debe_acumulado_cobac-debe_inicial_cobac else debe_acumulado_cobac end as debe_acumulado_cobac,"
				+" case when cue_codigo_cocac like '212.40%' then haber_acumulado_cobac-debe_inicial_cobac else haber_acumulado_cobac end as haber_acumulado_cobac,"
				+" case when debe_inicial_cobac=0 and debe_saldo_cobac<0 and (cue_codigo_cocac like '141.99%' or cue_codigo_cocac like '145.99%' or cue_codigo_cocac like '151.98%' or cue_codigo_cocac like '152.98%' or cue_codigo_cocac like '126.99%' ) then 0.00 else debe_saldo_cobac end as debe_saldo_cobac,"
				+" case when debe_inicial_cobac=0 and debe_saldo_cobac<0 and (cue_codigo_cocac like '141.99%' or cue_codigo_cocac like '145.99%' or cue_codigo_cocac like '151.98%' or cue_codigo_cocac like '152.98%' or cue_codigo_cocac like '126.99%' ) then abs(debe_saldo_cobac) else haber_saldo_cobac end as haber_saldo_cobac"
				+" from cont_balance_comprobacion bc"
				+" left join cont_catalogo_cuenta cat on cat.ide_cocac = bc.ide_cocac"
				+" where cue_codigo_cocac like '212.40%' or cue_codigo_cocac like '141.99%' or cue_codigo_cocac like '145.99%' or cue_codigo_cocac like '151.98%' or cue_codigo_cocac like '152.98%' or cue_codigo_cocac like '126.99%'"
				+" and (abs(debe_inicial_cobac) + abs(haber_inicial_cobac) + abs(debe_acumulado_cobac) + abs(haber_acumulado_cobac) + abs(debe_periodo_cobac) + abs(haber_periodo_cobac) + abs(debe_saldo_cobac) + abs(haber_saldo_cobac))>0 ";
		
		String sqlUpdate="";
		
		TablaGenerica tab_balance=utilitario.consultar(sqlActualiza);
		
		for(int i=0;i <tab_balance.getTotalFilas();i++){
			
			sqlUpdate="update cont_balance_comprobacion set debe_inicial_cobac="+tab_balance.getValor(i, "debe_inicial_cobac")+
			" ,haber_inicial_cobac="+tab_balance.getValor(i, "haber_inicial_cobac")+
			" ,debe_acumulado_cobac="+tab_balance.getValor(i, "debe_acumulado_cobac")+
			" ,haber_acumulado_cobac="+tab_balance.getValor(i, "haber_acumulado_cobac")+
			" ,debe_saldo_cobac="+tab_balance.getValor(i, "debe_saldo_cobac")+
			" ,haber_saldo_cobac="+ tab_balance.getValor(i, "haber_saldo_cobac")+
			" where ide_cobac="+tab_balance.getValor(i, "ide_cobac");
			utilitario.getConexion().ejecutarSql(sqlUpdate);
		}*/
		///////////////////////////////////////////////
		
		
		TablaGenerica tab_nivel=utilitario.consultar("select 1 as codigo, max(nivel_cocac) as maximo from cont_catalogo_cuenta");
		int int_nivel=pckUtilidades.CConversion.CInt(tab_nivel.getValor("maximo"));
		
		for(int i=0;i <int_nivel;i++){
			
			int valor=int_nivel-i;
			String sql_actualiza_niveles="update cont_balance_comprobacion"
					+" set debe_inicial_cobac = (case when abs(debe_inicial)>0 then debe_inicial else debe_inicial_cobac end),"
					+" haber_inicial_cobac= (case when abs(haber_inicial)>0 then haber_inicial else haber_inicial_cobac end),"
					+" debe_acumulado_cobac= (case when abs(debe_acumulado)>0 then debe_acumulado else debe_acumulado_cobac end),"
					+" haber_acumulado_cobac= (case when abs(haber_acumulado)>0 then haber_acumulado else haber_acumulado_cobac end),"
					+" debe_periodo_cobac= (case when abs(debe_periodo)>0 then debe_periodo else debe_periodo_cobac end),"
					+" haber_periodo_cobac= (case when abs(haber_periodo)>0 then haber_periodo else haber_periodo_cobac end)"
					+" from ("
						+"	select con_ide_cocac,round(sum(debe_inicial_cobac),2) as debe_inicial,round(sum(haber_inicial_cobac),2) as haber_inicial,"
						+"	round(sum(debe_acumulado_cobac),2) as debe_acumulado, round(sum(haber_acumulado_cobac),2) as haber_acumulado,round(sum(debe_periodo_cobac),2) as debe_periodo,round(sum(haber_periodo_cobac),2) as haber_periodo"
						+"	from  cont_catalogo_cuenta a ,cont_balance_comprobacion b"
						+"	where a.ide_cocac = b.ide_cocac and nivel_cocac ="+valor+" group by con_ide_cocac"
						+"	) a where a.con_ide_cocac = cont_balance_comprobacion.ide_cocac";
			//System.out.println("sql_actualiza_niveles "+sql_actualiza_niveles);
	
			utilitario.getConexion().ejecutarSql(sql_actualiza_niveles);
			
			String sql_acumulado_total="update cont_balance_comprobacion"
					+" set debe_acumulado_cobac=debe_inicial_cobac+debe_acumulado_cobac,"
					+" haber_acumulado_cobac=haber_inicial_cobac+haber_acumulado_cobac where ide_cocac in (select ide_cocac from cont_catalogo_cuenta where nivel_cocac ="+valor+")";
			utilitario.getConexion().ejecutarSql(sql_acumulado_total);
			
			String sql_saldo="update cont_balance_comprobacion"
					+" set debe_saldo_cobac =saldo_deudor,"
					+" haber_saldo_cobac = saldo_acreedor"
					+" from ("
						+" select a.ide_cocac,(case when saldo_cocac =1 then (debe_acumulado_cobac +debe_periodo_cobac) - (haber_acumulado_cobac + haber_periodo_cobac)" 
						+" when saldo_cocac =3 then (debe_acumulado_cobac +debe_periodo_cobac) else 0 end) as saldo_deudor, "
						+" (case when saldo_cocac =2 then (haber_acumulado_cobac + haber_periodo_cobac) - (debe_acumulado_cobac +debe_periodo_cobac)" 
						+" when saldo_cocac =3 then (haber_acumulado_cobac + haber_periodo_cobac) else 0 end) as saldo_acreedor "
						+" from cont_balance_comprobacion a, cont_catalogo_cuenta b where a.ide_cocac=b.ide_cocac"
						+" and nivel_cocac ="+valor+"	) a where a.ide_cocac = cont_balance_comprobacion.ide_cocac";
			//System.out.println("sql_saldo "+sql_saldo);
			utilitario.getConexion().ejecutarSql(sql_saldo);
			
			}
		/*
		String sql_acumulado_total="update cont_balance_comprobacion"
				+" set debe_acumulado_cobac=debe_inicial_cobac+debe_acumulado_cobac,"
				+" haber_acumulado_cobac=haber_inicial_cobac+haber_acumulado_cobac";
		utilitario.getConexion().ejecutarSql(sql_acumulado_total);
		*/
		
			///////////////////////////////////////////////////
			String sqlActualiza="select ide_cobac,"
			+" case when cue_codigo_cocac like '212.40%' then 0.00 else debe_inicial_cobac end as debe_inicial_cobac, "
			+" case when cue_codigo_cocac like '212.40%' then haber_inicial_cobac-debe_inicial_cobac else haber_inicial_cobac end as haber_inicial_cobac,"
			+" case when cue_codigo_cocac like '212.40%' then debe_acumulado_cobac-debe_inicial_cobac else debe_acumulado_cobac end as debe_acumulado_cobac,"
			+" case when cue_codigo_cocac like '212.40%' then haber_acumulado_cobac-debe_inicial_cobac else haber_acumulado_cobac end as haber_acumulado_cobac,"
			+" case when debe_inicial_cobac=0 and debe_saldo_cobac<0 and (cue_codigo_cocac like '141.99%' or cue_codigo_cocac like '145.99%' or cue_codigo_cocac like '151.98%' or cue_codigo_cocac like '152.98%' or cue_codigo_cocac like '126.99%' ) then 0.00 else debe_saldo_cobac end as debe_saldo_cobac,"
			+" case when debe_inicial_cobac=0 and debe_saldo_cobac<0 and (cue_codigo_cocac like '141.99%' or cue_codigo_cocac like '145.99%' or cue_codigo_cocac like '151.98%' or cue_codigo_cocac like '152.98%' or cue_codigo_cocac like '126.99%' ) then abs(debe_saldo_cobac) else haber_saldo_cobac end as haber_saldo_cobac"
			+" from cont_balance_comprobacion bc"
			+" left join cont_catalogo_cuenta cat on cat.ide_cocac = bc.ide_cocac"
			+" where cue_codigo_cocac like '212.40%' or cue_codigo_cocac like '141.99%' or cue_codigo_cocac like '145.99%' or cue_codigo_cocac like '151.98%' or cue_codigo_cocac like '152.98%' or cue_codigo_cocac like '126.99%'"
			+" and (abs(debe_inicial_cobac) + abs(haber_inicial_cobac) + abs(debe_acumulado_cobac) + abs(haber_acumulado_cobac) + abs(debe_periodo_cobac) + abs(haber_periodo_cobac) + abs(debe_saldo_cobac) + abs(haber_saldo_cobac))>0 ";
			
			String sqlUpdate="";
			
			TablaGenerica tab_balance=utilitario.consultar(sqlActualiza);
			
			for(int i=0;i <tab_balance.getTotalFilas();i++){
			
			sqlUpdate="update cont_balance_comprobacion set debe_inicial_cobac="+tab_balance.getValor(i, "debe_inicial_cobac")+
			" ,haber_inicial_cobac="+tab_balance.getValor(i, "haber_inicial_cobac")+
			" ,debe_acumulado_cobac="+tab_balance.getValor(i, "debe_acumulado_cobac")+
			" ,haber_acumulado_cobac="+tab_balance.getValor(i, "haber_acumulado_cobac")+
			" ,debe_saldo_cobac="+tab_balance.getValor(i, "debe_saldo_cobac")+
			" ,haber_saldo_cobac="+ tab_balance.getValor(i, "haber_saldo_cobac")+
			" where ide_cobac="+tab_balance.getValor(i, "ide_cobac");
			utilitario.getConexion().ejecutarSql(sqlUpdate);
			}
			///////////////////////////////////////////////
	
			
		}

	public String getCedGastoIngreso(String sqlGastos, String sqlIngresos, String tipo){
		
		String sql="";
		
		String sqlG="select periodo,'G' as tipo_cedula,codigo_clasificador_prcla,descripcion_clasificador_prcla,'000' as funcion, "
				+" sum(presupuesto_inicial_prpoa) as inicial, "
				+" sum(reforma_prpoa) as reforma, "
				+" sum(presupuesto_codificado_prpoa) as codificado, "
				+" sum(comprometido) as compromiso, "
				+" sum(devengado) as devengado, "
				+" sum(pagado) as pago, "
				+" 0.00 as cobrado,"
				+" sum(presupuesto_codificado_prpoa-comprometido) as saldo_comp, "
				+" sum(comprometido-devengado) as saldo_dev,"
				+" 0.00 as saldo_x_dev from ( "+sqlGastos+" ) a  "
				+" where length(codigo_clasificador_prcla)>0 "
				+" group by periodo,codigo_clasificador_prcla,descripcion_clasificador_prcla ";
				//+" order by codigo_clasificador_prcla";
		
		String sqlI="select periodo,'I' as tipo_cedula,codigo_clasificador_prcla,descripcion_clasificador_prcla,'000' as funcion, "
				+" sum(inicial) as inicial, "
				+" sum(reforma) as reforma, "
				+" sum(codificado) as codificado, "
				+" 0.00 as compromiso, "
				+" sum(devengado) as devengado, "
				+" 0.00 as pago, "
				+" sum(cobrado) as cobrado,"
				+" 0.00 as saldo_comp, "
				+" 0.00 as saldo_dev,"
				+" sum(codificado-devengado) as saldo_x_dev from ( "+sqlIngresos+" ) a  "
				+" where length(codigo_clasificador_prcla)>0 "
				+" group by periodo,codigo_clasificador_prcla,descripcion_clasificador_prcla ";
				//+" order by codigo_clasificador_prcla";
		
		
		if(tipo.equals("G"))
			sql=sqlG;
		
		if(tipo.equals("I"))
			sql=sqlI;
		
		if(tipo.equals("T")){
			sql=sqlI;
			sql+=" union ";
			sql+=sqlG;
		}
		
		sql+=" order by codigo_clasificador_prcla";
		System.out.println("getCedGastoIngreso "+sql);
		
		return sql;
		
	}
	
public String getTrxReciprocas(String fecha_inicial,String fecha_final){
		
		String sql="";
		
		String sql112_05="select periodo,rucEntidad,cta_mayor_pagar_cobrar,sum(flujo_deudor) as flujo_deudor, sum(flujo_acreedor) as flujo_acreedor,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i, sum(flujo_deudor2) as flujo_deudor2,  "
				+ "sum(flujo_acreedor2) as flujo_acreedor2, "
				+ "max(nro_transaccion) as nro_transaccion, max(nro_referenci) as nro_referenci,fecha_apro,fecha_venc "
				+ " "
				+ "from ( "
				//+ "select distinct "
				+ "select "
				+ "extract(month from mov.mov_fecha_comov) as periodo, cast('1768158410001' as character(25)) as rucEntidad, dmovi.cue_codigo_cocac as cta_mayor_pagar_cobrar, "
				+ "dmovi.haber_codem as flujo_deudor, "
				+ "dmovi.debe_codem as flujo_acreedor, "
				+ " "
				+ "(case when cp.ide_geedp is null then prov.ruc_tepro else '9999999999999' end) as ruc,  "
				+ "(case when cp.ide_geedp is null then prov.nombre_tepro else 'VARIOS CLIENTES' end) as nombre_tepro "
				+ ",cast('' as character(25)) as codigo_clasificador_prcla "
				+ ",cast('111.06.00' as character(25)) as cta_mayor_gasto_i "
				+ ",0 as flujo_deudor2 "
				+ ",coalesce(dmovi.haber_codem,0) as flujo_acreedor2 "
				+ ",coalesce(cast(comprobante_egreso_tecpo as bigint),mov.ide_comov) as nro_transaccion "
				+ ",coalesce(cast(comprobante_egreso_tecpo as bigint),mov.ide_comov) as nro_referenci "
				+ ",cast('"+fecha_inicial+"' as character(25)) as fecha_apro, cast('"+fecha_final+"' as character(25)) as fecha_venc "
				+ " "
				+ "from cont_movimiento mov "
				+ "left join tes_comprobante_pago cp on cp.ide_tecpo=mov.ide_tecpo "
				+ "left join (select b.ide_tepro, coalesce(ruc_representante_tepro,ruc_tepro) as ruc_tepro,substring(nombre_tepro from 1 for 30) as nombre_tepro from tes_proveedor b ) prov on prov.ide_tepro=cp.ide_tepro "
				+ " "
				+ "join (select dmov.ide_comov,ide_gelua, cue_codigo_cocac, dmov.debe_codem, dmov.haber_codem "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and cue_codigo_cocac like '112.05%' "
				+ "	where sigef_cocac=true and nivel_cocac=4 "
				+ "	) dmovi on dmovi.ide_comov=mov.ide_comov "
				+ " "
				+ "where mov.mov_fecha_comov between '"+fecha_inicial+"' and  '"+fecha_final+"' "
				+ " "
				+ ") a  "
				+ "group by periodo,rucEntidad,cta_mayor_pagar_cobrar,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i,fecha_apro,fecha_venc ";

		String sql212_05="select periodo,rucEntidad,cta_mayor_pagar_cobrar,sum(flujo_deudor) as flujo_deudor, sum(flujo_acreedor) as flujo_acreedor,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i, sum(flujo_deudor2) as flujo_deudor2,  "
				+ "sum(flujo_acreedor2) as flujo_acreedor2, "
				+ "max(nro_transaccion) as nro_transaccion, max(nro_referenci) as nro_referenci,fecha_apro,fecha_venc "
				+ " "
				+ "from ( "
				//+ "select distinct "
				+ "select "
				+ "extract(month from mov.mov_fecha_comov) as periodo, cast('1768158410001' as character(25)) as rucEntidad,  "
				+ "cast('212.05.00' as character(25)) as cta_mayor_pagar_cobrar, "
				+ "0 as flujo_deudor, "
				+ "dmovi.haber_codem as flujo_acreedor, "
				+ "cast('9999999999999' as character(25)) as ruc,  "
				+ "cast('VARIOS CLIENTES' as character(25)) as nombre_tepro "
				+ ",cast('' as character(25)) as codigo_clasificador_prcla "
				+ ",cast('111.06.00' as character(25)) as cta_mayor_gasto_i "
				+ ",dmovi.haber_codem as flujo_deudor2 "
				+ ",0 as flujo_acreedor2 "
				+ ",mov.ide_comov as nro_transaccion, mov.ide_comov as nro_referenci "
				+ ",cast('"+fecha_inicial+"' as character(25)) as fecha_apro, cast('"+fecha_final+"' as character(25)) as fecha_venc "
				+ " "
				+ "from cont_movimiento mov "
				+ "join (select dmov.ide_comov,ide_gelua, cue_codigo_cocac, dmov.debe_codem, dmov.haber_codem  "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and cue_codigo_cocac like '212.05%' "
				+ "	where nivel_cocac>4 "
				+ "	) dmovi on dmovi.ide_comov=mov.ide_comov "
				+ " "
				+ "where mov.mov_fecha_comov between '"+fecha_inicial+"' and  '"+fecha_final+"' "
				+ " "
				+ ") a  "
				+ "group by periodo,rucEntidad,cta_mayor_pagar_cobrar,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i,fecha_apro,fecha_venc";
		
		String sql113_19="select periodo,rucEntidad,cta_mayor_pagar_cobrar,sum(flujo_deudor) as flujo_deudor, sum(flujo_acreedor) as flujo_acreedor,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i, sum(flujo_deudor2) as flujo_deudor2,  "
				+ "sum(flujo_acreedor2) as flujo_acreedor2, "
				+ "max(nro_transaccion) as nro_transaccion, max(nro_referenci) as nro_referenci,fecha_apro,fecha_venc "
				+ " "
				+ "from ( "
				//+ "select distinct "
				+ "select "
				+ "extract(month from mov.mov_fecha_comov) as periodo, cast('1768158410001' as character(25)) as rucEntidad, dmovi.cue_codigo_cocac as cta_mayor_pagar_cobrar, "
				+ "dmovi.debe_codem as flujo_deudor, "
				+ "dmovi.haber_codem as flujo_acreedor, "
				+ " "
				+ "coalesce((case when cp.ide_geedp is null then prov.ruc_tepro else emp.ruc_emp end),'9999999999999') as ruc,  "
				+ "coalesce((case when cp.ide_geedp is null then prov.nombre_tepro else emp.NOMBRES end),'VARIOS CLIENTES') as nombre_tepro "
				+ ",(case when coalesce(devengado_prmen,0) >0 then dmovi.codigo_clasificador_prcla else '' end) as codigo_clasificador_prcla "
				+ ",(case when coalesce(devengado_prmen,0)>0 then '111.06.00' else dmovii.cue_codigo_cocac end) as cta_mayor_gasto_i "
				+ ",coalesce(dmovii.haber_codem,0) as flujo_deudor2 "
				+ ",coalesce(devengado_prmen,0) as flujo_acreedor2 "
				+ ",coalesce(cast(comprobante_egreso_tecpo as bigint),mov.ide_comov) as nro_transaccion,coalesce(cast(comprobante_egreso_tecpo as bigint),mov.ide_comov) as nro_referenci "
				+ ",cast('"+fecha_inicial+"' as character(25)) as fecha_apro, cast('"+fecha_final+"' as character(25)) as fecha_venc "
				+ " "
				+ "from cont_movimiento mov "
				+ "left join tes_comprobante_pago cp on cp.ide_tecpo=mov.ide_tecpo "
				+ "left join (select b.ide_tepro, coalesce(ruc_representante_tepro,ruc_tepro) as ruc_tepro,substring(nombre_tepro from 1 for 30) as nombre_tepro from tes_proveedor b ) prov on prov.ide_tepro=cp.ide_tepro "
				+ " "
				+ "left join (select par.ide_geedp,substring (APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || PRIMER_NOMBRE_GTEMP || ' '  "
				+ "	|| (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP  end)  from 1 for 30) AS NOMBRES, DOCUMENTO_IDENTIDAD_GTEMP as ruc_emp   "
				+ "	from GEN_EMPLEADOS_DEPARTAMENTO_PAR par  "
				+ "	left join GTH_EMPLEADO emp on par.ide_gtemp=emp.ide_gtemp ) emp on emp.ide_geedp=cp.ide_geedp "
				+ "	 "
				+ " join (select dmov.ide_comov,ide_gelua, cue_codigo_cocac, dmov.debe_codem, dmov.haber_codem, codigo_clasificador_prcla, devengado_prmen "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and cue_codigo_cocac like '113.19%' "
				+ "	left join ( select ide_codem, pcl.codigo_clasificador_prcla,sum(devengado_prmen) as devengado_prmen from pre_mensual pmes "
				+ "		    left join pre_anual panio on panio.ide_pranu=pmes.ide_pranu "
				+ "		    left join pre_clasificador pcl on pcl.ide_prcla=panio.ide_prcla   "
				+ "		    where coalesce(ide_codem,0)>0  "
				+ "			group by ide_codem, pcl.codigo_clasificador_prcla "
				+ "		    ) pmesi on pmesi.ide_codem=dmov.ide_codem  "//--ingresos
				+ "	where sigef_cocac=true and nivel_cocac=4 "
				+ "	) dmovi on dmovi.ide_comov=mov.ide_comov "
				+ " "
				+ "left join (select dmov.ide_comov,ide_gelua, cue_codigo_cocac, dmov.debe_codem, dmov.haber_codem "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and (cue_codigo_cocac like '625%') "
				+ "	) dmovii on dmovii.ide_comov=mov.ide_comov and dmovii.ide_gelua=dmovi.ide_gelua and dmovii.haber_codem=dmovi.haber_codem "
				+ " "
				+ "where mov.mov_fecha_comov between '"+fecha_inicial+"' and  '"+fecha_final+"' "
				+ " "
				+ ") a  "
				+ "group by periodo,rucEntidad,cta_mayor_pagar_cobrar,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i,fecha_apro,fecha_venc ";
		
		
		String sql113_97="select periodo,rucEntidad,cta_mayor_pagar_cobrar,sum(flujo_deudor) as flujo_deudor, sum(flujo_acreedor) as flujo_acreedor,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i, sum(flujo_deudor2) as flujo_deudor2,  "
				+ "sum(flujo_acreedor2) as flujo_acreedor2, "
				+ "max(nro_transaccion) as nro_transaccion, max(nro_referenci) as nro_referenci,fecha_apro,fecha_venc "
				+ " "
				+ "from ( "
				//+ "select distinct "
				+ "select "
				+ "extract(month from mov.mov_fecha_comov) as periodo, cast('1768158410001' as character(25)) as rucEntidad, dmovi.cue_codigo_cocac as cta_mayor_pagar_cobrar, "
				+ "dmovi.debe_codem as flujo_deudor, "
				+ "dmovi.haber_codem as flujo_acreedor, "
				+ " "
				+ "coalesce((case when cp.ide_geedp is null then prov.ruc_tepro else emp.ruc_emp end),'9999999999999') as ruc,  "
				+ "coalesce((case when cp.ide_geedp is null then prov.nombre_tepro else emp.NOMBRES end),'VARIOS CLIENTES') as nombre_tepro "
				+ ",(case when coalesce(devengado_prmen,0) >0 then dmovi.codigo_clasificador_prcla else '' end) as codigo_clasificador_prcla "
				+ ",coalesce(dmovi3.cue_codigo_cocac,dmovii.cue_codigo_cocac) as cta_mayor_gasto_i "
				+ ",coalesce(dmovii.haber_codem,0) as flujo_deudor2 "
				+ ",coalesce(devengado_prmen,0) as flujo_acreedor2 "
				+ ",coalesce(cast(comprobante_egreso_tecpo as bigint),mov.ide_comov) as nro_transaccion,coalesce(cast(comprobante_egreso_tecpo as bigint),mov.ide_comov) as nro_referenci "
				+ ",cast('"+fecha_inicial+"' as character(25)) as fecha_apro, cast('"+fecha_final+"' as character(25)) as fecha_venc "
				+ " "
				+ "from cont_movimiento mov "
				+ "left join tes_comprobante_pago cp on cp.ide_tecpo=mov.ide_tecpo "
				+ "left join (select b.ide_tepro, coalesce(ruc_representante_tepro,ruc_tepro) as ruc_tepro,substring(nombre_tepro from 1 for 30) as nombre_tepro from tes_proveedor b ) prov on prov.ide_tepro=cp.ide_tepro "
				+ "left join (select par.ide_geedp,substring (APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || PRIMER_NOMBRE_GTEMP || ' '  "
				+ "	|| (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP  end)  from 1 for 30) AS NOMBRES, DOCUMENTO_IDENTIDAD_GTEMP as ruc_emp   "
				+ "	from GEN_EMPLEADOS_DEPARTAMENTO_PAR par  "
				+ "	left join GTH_EMPLEADO emp on par.ide_gtemp=emp.ide_gtemp ) emp on emp.ide_geedp=cp.ide_geedp "
				+ "	 "
				+ " join (select dmov.ide_comov,ide_gelua, cue_codigo_cocac, dmov.debe_codem, dmov.haber_codem, codigo_clasificador_prcla, devengado_prmen "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and cue_codigo_cocac like '113.97%' "
				+ "	left join ( select ide_codem, pcl.codigo_clasificador_prcla,sum(devengado_prmen) as devengado_prmen from pre_mensual pmes "
				+ "		    left join pre_anual panio on panio.ide_pranu=pmes.ide_pranu "
				+ "		    left join pre_clasificador pcl on pcl.ide_prcla=panio.ide_prcla   "
				+ "		    where coalesce(ide_codem,0)>0  "
				+ "			group by ide_codem, pcl.codigo_clasificador_prcla "
				+ "		    ) pmesi on pmesi.ide_codem=dmov.ide_codem  "//--ingresos
				+ "	where sigef_cocac=true and nivel_cocac=4 "
				+ "	) dmovi on dmovi.ide_comov=mov.ide_comov "
				+ " "
				+ " "
				+ "left join (select distinct dmov.ide_comov,ide_gelua, cue_codigo_cocac "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and (cue_codigo_cocac like '111.06%') "
				+ "	where coalesce(haber_codem,0)>0   "
				+ "	) dmovi3 on dmovi3.ide_comov=mov.ide_comov and dmovi3.ide_gelua<>dmovi.ide_gelua "
				+ " "
				+ "left join (select dmov.ide_comov,ide_gelua, cue_codigo_cocac, dmov.debe_codem, dmov.haber_codem "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and cue_codigo_cocac like '124%' "
				
				+ "	) dmovii on dmovii.ide_comov=mov.ide_comov and dmovii.ide_gelua=dmovi.ide_gelua "
				+ " "
				+ "where mov.mov_fecha_comov between '"+fecha_inicial+"' and  '"+fecha_final+"' "
				+ " "
				+ ") a  "
				+ "group by periodo,rucEntidad,cta_mayor_pagar_cobrar,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i,fecha_apro,fecha_venc ";
		
		String sql113_28="select periodo,rucEntidad,cta_mayor_pagar_cobrar,sum(flujo_deudor) as flujo_deudor, sum(flujo_acreedor) as flujo_acreedor,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i, sum(flujo_deudor2) as flujo_deudor2,  "
				+ "sum(flujo_acreedor2) as flujo_acreedor2, "
				+ "max(nro_transaccion) as nro_transaccion, max(nro_referenci) as nro_referenci,fecha_apro,fecha_venc "
				+ " "
				+ "from ( "
				//+ "select distinct "
				+ "select "
				+ "extract(month from mov.mov_fecha_comov) as periodo, cast('1768158410001' as character(25)) as rucEntidad, dmovi.cue_codigo_cocac as cta_mayor_pagar_cobrar, "
				+ "dmovi.debe_codem as flujo_deudor, "
				+ "dmovi.haber_codem as flujo_acreedor, "
				+ " "
				+ "coalesce((case when cp.ide_geedp is null then prov.ruc_tepro else emp.ruc_emp end),'9999999999999') as ruc,  "
				+ "coalesce((case when cp.ide_geedp is null then prov.nombre_tepro else emp.NOMBRES end),'VARIOS CLIENTES') as nombre_tepro "
				+ ",(case when coalesce(devengado_prmen,0) >0 then dmovi.codigo_clasificador_prcla else '' end) as codigo_clasificador_prcla "
				+ ",coalesce(dmovi3.cue_codigo_cocac,dmovii.cue_codigo_cocac) as cta_mayor_gasto_i "
				+ ",coalesce(dmovii.haber_codem,0) as flujo_deudor2 "
				+ ",coalesce(devengado_prmen,0) as flujo_acreedor2 "
				+ ",coalesce(cast(comprobante_egreso_tecpo as bigint),mov.ide_comov) as nro_transaccion,coalesce(cast(comprobante_egreso_tecpo as bigint),mov.ide_comov) as nro_referenci "
				+ ",cast('"+fecha_inicial+"' as character(25)) as fecha_apro, cast('"+fecha_final+"' as character(25)) as fecha_venc "
				+ " "
				+ "from cont_movimiento mov "
				+ "left join tes_comprobante_pago cp on cp.ide_tecpo=mov.ide_tecpo "
				+ "left join (select b.ide_tepro, coalesce(ruc_representante_tepro,ruc_tepro) as ruc_tepro,substring(nombre_tepro from 1 for 30) as nombre_tepro from tes_proveedor b ) prov on prov.ide_tepro=cp.ide_tepro "
				+ "left join (select par.ide_geedp,substring (APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || PRIMER_NOMBRE_GTEMP || ' '  "
				+ "	|| (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP  end)  from 1 for 30) AS NOMBRES, DOCUMENTO_IDENTIDAD_GTEMP as ruc_emp   "
				+ "	from GEN_EMPLEADOS_DEPARTAMENTO_PAR par  "
				+ "	left join GTH_EMPLEADO emp on par.ide_gtemp=emp.ide_gtemp ) emp on emp.ide_geedp=cp.ide_geedp "
				+ " "
				+ " join (select dmov.ide_comov,ide_gelua, cue_codigo_cocac, dmov.debe_codem, dmov.haber_codem, codigo_clasificador_prcla, devengado_prmen "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and cue_codigo_cocac like '113.28%' "
				+ "	left join ( select ide_codem, pcl.codigo_clasificador_prcla,sum(devengado_prmen) as devengado_prmen from pre_mensual pmes "
				+ "		    left join pre_anual panio on panio.ide_pranu=pmes.ide_pranu "
				+ "		    left join pre_clasificador pcl on pcl.ide_prcla=panio.ide_prcla   "
				+ "		    where coalesce(ide_codem,0)>0  "
				+ "			group by ide_codem, pcl.codigo_clasificador_prcla "
				+ "		    ) pmesi on pmesi.ide_codem=dmov.ide_codem  "//--ingresos
				+ "	where sigef_cocac=true and nivel_cocac=4 "
				+ "	) dmovi on dmovi.ide_comov=mov.ide_comov "
				+ " "
				+ "left join (select distinct dmov.ide_comov,ide_gelua, cue_codigo_cocac "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and (cue_codigo_cocac like '111.06%') "
				+ "	where coalesce(debe_codem,0)>0  "
				+ "	) dmovi3 on dmovi3.ide_comov=mov.ide_comov and dmovi3.ide_gelua=dmovi.ide_gelua "
				+ " "
				+ "left join (select dmov.ide_comov,ide_gelua, cue_codigo_cocac, dmov.debe_codem, dmov.haber_codem "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and (cue_codigo_cocac like '623%' or cue_codigo_cocac like '626%') "
				+ "	) dmovii on dmovii.ide_comov=mov.ide_comov and dmovii.ide_gelua=dmovi.ide_gelua "
				+ " "
				+ "where mov.mov_fecha_comov between '"+fecha_inicial+"' and  '"+fecha_final+"' "
				+ " "
				+ ") a  "
				+ "group by periodo,rucEntidad,cta_mayor_pagar_cobrar,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i,fecha_apro,fecha_venc ";
		
		String sql113_13="select periodo,rucEntidad,cta_mayor_pagar_cobrar,sum(flujo_deudor) as flujo_deudor, sum(flujo_acreedor) as flujo_acreedor,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i, sum(flujo_deudor2) as flujo_deudor2,  "
				+ "sum(flujo_acreedor2) as flujo_acreedor2, "
				+ "max(nro_transaccion) as nro_transaccion, max(nro_referenci) as nro_referenci,fecha_apro,fecha_venc "
				+ " "
				+ "from ( "
				//+ "select distinct "
				+ "select "
				+ "extract(month from mov.mov_fecha_comov) as periodo, cast('1768158410001' as character(25)) as rucEntidad, dmovi.cue_codigo_cocac as cta_mayor_pagar_cobrar, "
				+ "dmovi.debe_codem as flujo_deudor, "
				+ "dmovi.haber_codem as flujo_acreedor, "
				+ " "
				+ "coalesce((case when cp.ide_geedp is null then prov.ruc_tepro else emp.ruc_emp end),'9999999999999') as ruc,  "
				+ "coalesce((case when cp.ide_geedp is null then prov.nombre_tepro else emp.NOMBRES end),'VARIOS CLIENTES') as nombre_tepro "
				+ ",(case when coalesce(devengado_prmen,0) >0 then dmovi.codigo_clasificador_prcla else '' end) as codigo_clasificador_prcla "
				+ ",(case when coalesce(devengado_prmen,0)>0 then coalesce(dmovi3.cue_codigo_cocac,coalesce(dmovii.cue_codigo_cocac,'111.06.00')) else '623.01.00' end) as cta_mayor_gasto_i "
				+ ",coalesce(dmovii.haber_codem,coalesce(dmovi.haber_codem,0)) as flujo_deudor2 "
				+ ",coalesce(devengado_prmen,0) as flujo_acreedor2 "
				+ ",coalesce(cast(comprobante_egreso_tecpo as bigint),mov.ide_comov) as nro_transaccion,coalesce(cast(comprobante_egreso_tecpo as bigint),mov.ide_comov) as nro_referenci "
				+ ",cast('"+fecha_inicial+"' as character(25)) as fecha_apro, cast('"+fecha_final+"' as character(25)) as fecha_venc "
				+ " "
				+ "from cont_movimiento mov "
				+ "left join tes_comprobante_pago cp on cp.ide_tecpo=mov.ide_tecpo "
				+ "left join (select b.ide_tepro, coalesce(ruc_representante_tepro,ruc_tepro) as ruc_tepro,substring(nombre_tepro from 1 for 30) as nombre_tepro from tes_proveedor b ) prov on prov.ide_tepro=cp.ide_tepro "
				+ "left join (select par.ide_geedp,substring (APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || PRIMER_NOMBRE_GTEMP || ' '  "
				+ "	|| (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP  end)  from 1 for 30) AS NOMBRES, DOCUMENTO_IDENTIDAD_GTEMP as ruc_emp   "
				+ "	from GEN_EMPLEADOS_DEPARTAMENTO_PAR par  "
				+ "	left join GTH_EMPLEADO emp on par.ide_gtemp=emp.ide_gtemp ) emp on emp.ide_geedp=cp.ide_geedp "
				+ " "
				+ " join (select dmov.ide_comov,ide_gelua, cue_codigo_cocac, dmov.debe_codem, dmov.haber_codem, codigo_clasificador_prcla, devengado_prmen "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and cue_codigo_cocac like '113.13%' "
				+ "	left join ( select ide_codem, pcl.codigo_clasificador_prcla,sum(devengado_prmen) as devengado_prmen from pre_mensual pmes "
				+ "		    left join pre_anual panio on panio.ide_pranu=pmes.ide_pranu "
				+ "		    left join pre_clasificador pcl on pcl.ide_prcla=panio.ide_prcla   "
				+ "		    where coalesce(ide_codem,0)>0  "
				+ "			group by ide_codem, pcl.codigo_clasificador_prcla "
				+ "		    ) pmesi on pmesi.ide_codem=dmov.ide_codem  "//--ingresos
				+ "	where sigef_cocac=true and nivel_cocac=4 "
				+ "	) dmovi on dmovi.ide_comov=mov.ide_comov "
				+ " "
				+ "left join (select dmov.ide_comov,ide_gelua,(case when length(cue_codigo_cocac)>10 then substring(cue_codigo_cocac from 0 for 10) else cue_codigo_cocac end) as cue_codigo_cocac "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and (cue_codigo_cocac like '111.%') "
				+ "	where coalesce(debe_codem,0)>0  "
				+ "	) dmovi3 on dmovi3.ide_comov=mov.ide_comov and dmovi3.ide_gelua=dmovi.ide_gelua "
				+ " "
				+ "left join (select dmov.ide_comov,ide_gelua, cue_codigo_cocac, dmov.debe_codem, dmov.haber_codem "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and (cue_codigo_cocac like '623%') "
				+ "	) dmovii on dmovii.ide_comov=mov.ide_comov and dmovii.ide_gelua=dmovi.ide_gelua "
				+ " "
				+ "where mov.mov_fecha_comov between '"+fecha_inicial+"' and  '"+fecha_final+"' "
				+ " "
				+ ") a  "
				+ "group by periodo,rucEntidad,cta_mayor_pagar_cobrar,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i,fecha_apro,fecha_venc ";
		
		String sql113_14="select periodo,rucEntidad,cta_mayor_pagar_cobrar,sum(flujo_deudor) as flujo_deudor, sum(flujo_acreedor) as flujo_acreedor,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i, sum(flujo_deudor2) as flujo_deudor2,  "
				+ "sum(flujo_acreedor2) as flujo_acreedor2, "
				+ "max(nro_transaccion) as nro_transaccion, max(nro_referenci) as nro_referenci,fecha_apro,fecha_venc "
				+ " "
				+ "from ( "
				//+ "select distinct "
				+ "select "
				+ "extract(month from mov.mov_fecha_comov) as periodo, cast('1768158410001' as character(25)) as rucEntidad,  "
				+ "cast('113.14.00' as character(25)) as cta_mayor_pagar_cobrar, "
				+ "dmovi.debe_codem as flujo_deudor, "
				+ "dmovi.haber_codem as flujo_acreedor, "
				+ "cast('9999999999999' as character(25)) as ruc,  "
				+ "cast('VARIOS CLIENTES' as character(25)) as nombre_tepro "
				+ ",codigo_clasificador_prcla "
				+ ",(case when codigo_clasificador_prcla is null then '624.03.99' else '111.06.00' end) as cta_mayor_gasto_i "
				+ ",(case when codigo_clasificador_prcla is null then dmovi.haber_codem else 0 end) as flujo_deudor2 "
				+ ",coalesce(devengado_prmen,0) as flujo_acreedor2 "
				+ ",mov.ide_comov as nro_transaccion, mov.ide_comov as nro_referenci "
				+ ",cast('"+fecha_inicial+"' as character(25)) as fecha_apro, cast('"+fecha_final+"' as character(25)) as fecha_venc "
				+ " "
				+ "from cont_movimiento mov "
				+ "join (select dmov.ide_comov,ide_gelua, cue_codigo_cocac, dmov.debe_codem, dmov.haber_codem, codigo_clasificador_prcla, devengado_prmen "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and cue_codigo_cocac like '113.14%' "
				+ "	left join ( select ide_codem, pcl.codigo_clasificador_prcla,sum(devengado_prmen) as devengado_prmen from pre_mensual pmes "
				+ "		    left join pre_anual panio on panio.ide_pranu=pmes.ide_pranu "
				+ "		    left join pre_clasificador pcl on pcl.ide_prcla=panio.ide_prcla   "
				+ "		    where coalesce(ide_codem,0)>0 and coalesce(devengado_prmen,0)<>0 "
				+ "			group by ide_codem, pcl.codigo_clasificador_prcla "
				+ "		    ) pmesi on pmesi.ide_codem=dmov.ide_codem  "//--ingresos
				+ "	where nivel_cocac>4 "
				+ "	) dmovi on dmovi.ide_comov=mov.ide_comov "
				+ " "
				+ "where mov.mov_fecha_comov between '"+fecha_inicial+"' and  '"+fecha_final+"' "
				+ " "
				+ ") a  "
				+ "group by periodo,rucEntidad,cta_mayor_pagar_cobrar,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i,fecha_apro,fecha_venc ";
		
		String sql113_98="select periodo,rucEntidad,cta_mayor_pagar_cobrar,sum(flujo_deudor) as flujo_deudor, sum(flujo_acreedor) as flujo_acreedor,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i, sum(flujo_deudor2) as flujo_deudor2,  "
				+ "sum(flujo_acreedor2) as flujo_acreedor2, "
				+ "max(nro_transaccion) as nro_transaccion, max(nro_referenci) as nro_referenci,fecha_apro,fecha_venc "
				+ " "
				+ "from ( "
				//+ "select distinct "
				+ "select "
				+ "extract(month from mov.mov_fecha_comov) as periodo, cast('1768158410001' as character(25)) as rucEntidad,  "
				+ "cast('113.98.00' as character(25)) as cta_mayor_pagar_cobrar, "
				+ "dmovi.debe_codem as flujo_deudor, "
				+ "dmovi.haber_codem as flujo_acreedor, "
				+ "cast('9999999999999' as character(25)) as ruc,  "
				+ "cast('VARIOS CLIENTES' as character(25)) as nombre_tepro "
				+ ",codigo_clasificador_prcla "
				+ ",(case when codigo_clasificador_prcla is null then '124.98.01' else '111.06.00' end) as cta_mayor_gasto_i "
				+ ",(case when codigo_clasificador_prcla is null then dmovi.haber_codem else 0 end) as flujo_deudor2 "
				+ ",coalesce(devengado_prmen,0) as flujo_acreedor2 "
				+ ",mov.ide_comov as nro_transaccion, mov.ide_comov as nro_referenci "
				+ ",cast('"+fecha_inicial+"' as character(25)) as fecha_apro, cast('"+fecha_final+"' as character(25)) as fecha_venc "
				+ " "
				+ "from cont_movimiento mov "
				+ "join (select dmov.ide_comov,ide_gelua, cue_codigo_cocac, dmov.debe_codem, dmov.haber_codem, codigo_clasificador_prcla, devengado_prmen "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and cue_codigo_cocac like '113.98%' "
				+ "	left join ( select ide_codem, pcl.codigo_clasificador_prcla,sum(devengado_prmen) as devengado_prmen from pre_mensual pmes "
				+ "		    left join pre_anual panio on panio.ide_pranu=pmes.ide_pranu "
				+ "		    left join pre_clasificador pcl on pcl.ide_prcla=panio.ide_prcla   "
				+ "		    where coalesce(ide_codem,0)>0 and coalesce(devengado_prmen,0)<>0 "
				+ "			group by ide_codem, pcl.codigo_clasificador_prcla "
				+ "		    ) pmesi on pmesi.ide_codem=dmov.ide_codem  "//--ingresos
				+ "	where nivel_cocac>4 "
				+ "	) dmovi on dmovi.ide_comov=mov.ide_comov "
				+ " "
				+ "where mov.mov_fecha_comov between '"+fecha_inicial+"' and  '"+fecha_final+"' "
				+ " "
				+ ") a  "
				+ "group by periodo,rucEntidad,cta_mayor_pagar_cobrar,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i,fecha_apro,fecha_venc ";
		
		
		////////////////////////////////GASTOS
		
		String sql213_53=" select periodo,rucEntidad,cta_mayor_pagar_cobrar,sum(flujo_deudor) as flujo_deudor, sum(flujo_acreedor) as flujo_acreedor,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i, sum(flujo_deudor2) as flujo_deudor2,  "
				+ "sum(flujo_acreedor2) as flujo_acreedor2, "
				+ "max(nro_transaccion) as nro_transaccion, max(nro_referenci) as nro_referenci,fecha_apro,fecha_venc "
				+ " "
				+ "from ( "
				+ " "
				+ "select "
				+ "extract(month from mov.mov_fecha_comov) as periodo, cast('1768158410001' as character(25)) as rucEntidad, dmovi.cue_codigo_cocac as cta_mayor_pagar_cobrar, "
				+ "dmovi.haber_codem as flujo_deudor, "
				+ "dmovi.debe_codem as flujo_acreedor, "
				+ " "
				+ "coalesce((case when cp.ide_geedp is null then prov.ruc_tepro else emp.ruc_emp end),'9999999999999') as ruc,  "
				+ "coalesce((case when cp.ide_geedp is null then prov.nombre_tepro else emp.NOMBRES end),'VARIOS CLIENTES') as nombre_tepro "
				+ ",dmovii.codigo_clasificador_prcla "
				+ ",coalesce(dmovi1.cue_codigo_cocac,coalesce(dmovii.cue_codigo_cocac,'111.06.00')) as cta_mayor_gasto_i "
				+ ",coalesce(dmovii.devengado_prmen,0) as flujo_deudor2 "
				+ ",coalesce(dmovi.haber_codem,0) as flujo_acreedor2 "
				+ ",coalesce(cast(comprobante_egreso_tecpo as bigint),mov.ide_comov) as nro_transaccion,coalesce(cast(comprobante_egreso_tecpo as bigint),mov.ide_comov) as nro_referenci "
				+ ",cast('"+fecha_inicial+"' as character(25)) as fecha_apro, cast('"+fecha_final+"' as character(25)) as fecha_venc "
				+ " "
				+ "from cont_movimiento mov "
				+ "left join tes_comprobante_pago cp on cp.ide_tecpo=mov.ide_tecpo "
				+ "left join (select b.ide_tepro, coalesce(ruc_representante_tepro,ruc_tepro) as ruc_tepro,substring(nombre_tepro from 1 for 30) as nombre_tepro from tes_proveedor b ) prov on prov.ide_tepro=cp.ide_tepro "
				+ " "
				+ "left join (select par.ide_geedp,substring (APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || PRIMER_NOMBRE_GTEMP || ' '  "
				+ "	|| (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP  end)  from 1 for 30) AS NOMBRES,DOCUMENTO_IDENTIDAD_GTEMP as ruc_emp "
				+ "	from GEN_EMPLEADOS_DEPARTAMENTO_PAR par  "
				+ "	left join GTH_EMPLEADO emp on par.ide_gtemp=emp.ide_gtemp ) emp on emp.ide_geedp=cp.ide_geedp "
				+ " "
				+ " join (select dmov.ide_comov,ide_gelua, cue_codigo_cocac, dmov.debe_codem, dmov.haber_codem "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and (cue_codigo_cocac like '213.53%' or cue_codigo_cocac like '213.56%' or cue_codigo_cocac like '213.57%' or cue_codigo_cocac like '213.58%'  "
				+ "	                                                                   or cue_codigo_cocac like '213.73%' or cue_codigo_cocac like '213.75%' or cue_codigo_cocac like '213.77%' or cue_codigo_cocac like '213.78%'  "
				+ "	                                                                   or cue_codigo_cocac like '213.84%' or cue_codigo_cocac like '213.85%' or cue_codigo_cocac like '213.96%' or cue_codigo_cocac like '213.99%') "
				+ "	where sigef_cocac=true and nivel_cocac=4 "
				+ "	) dmovi on dmovi.ide_comov=mov.ide_comov "
				+ " "
				+ "left join (select dmov.ide_comov,ide_gelua, (case when length(cue_codigo_cocac)>10 then substring(cue_codigo_cocac from 0 for 10) else cue_codigo_cocac end) as cue_codigo_cocac,  "
				+ "         dmov.debe_codem, dmov.haber_codem, codigo_clasificador_prcla, coalesce(devengado_prmen,0) as devengado_prmen "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and (cue_codigo_cocac like '131%' or cue_codigo_cocac like '134%' or cue_codigo_cocac like '141%' or cue_codigo_cocac like '142%' "
				+ "                                                                    or cue_codigo_cocac like '152%' or cue_codigo_cocac like '223%' or cue_codigo_cocac like '634%' or cue_codigo_cocac like '635%' or cue_codigo_cocac like '636%') "
				+ "	left join ( select ide_codem, codigo_clasificador_prcla,sum(devengado_prmen) as devengado_prmen from pre_mensual pmes "
				+ "		    left join pre_anual panio on panio.ide_pranu=pmes.ide_pranu "
				+ "		    left join pre_poa poa on poa.ide_prpoa=panio.ide_prpoa "
				+ "		    left join pre_clasificador pcl on pcl.ide_prcla=poa.ide_prcla  "
				+ "		    where coalesce(ide_codem,0)>0 "
				+ "		    group by ide_codem, codigo_clasificador_prcla "
				+ "		    ) pmesi on pmesi.ide_codem=dmov.ide_codem  " //--gastos
				+ "	where codigo_clasificador_prcla is not null  "
				+ "	) dmovii on dmovii.ide_comov=mov.ide_comov and dmovii.ide_gelua=dmovi.ide_gelua and dmovii.debe_codem=dmovi.debe_codem "
				+ " "
				+ "left join (select distinct dmov.ide_comov,ide_gelua, cast('111.06.00' as character(25)) as cue_codigo_cocac "
				+ "	from cont_detalle_movimiento dmov  "
				+ "	join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac and (cue_codigo_cocac like '111.06%') "
				+ "	where coalesce(haber_codem,0)>0  "
				+ "	) dmovi1 on dmovi1.ide_comov=mov.ide_comov and dmovi1.ide_gelua=dmovi.ide_gelua "
				+ " "
				+ "where mov.mov_fecha_comov between '"+fecha_inicial+"' and  '"+fecha_final+"' "
				
				+ ") a  "
			
				+ "group by periodo,rucEntidad,cta_mayor_pagar_cobrar,ruc,nombre_tepro,codigo_clasificador_prcla,cta_mayor_gasto_i,fecha_apro,fecha_venc  ";
		
		//////////////////////////////////
		
			sql=sql112_05;
			sql+=" union all ";
			sql+=sql212_05;
			sql+=" union all ";
			sql+=sql113_19;
			sql+=" union all ";
			sql+=sql113_97;
			sql+=" union all ";
			sql+=sql113_28;
			sql+=" union all ";
			sql+=sql113_13;
			sql+=" union all ";
			sql+=sql113_14;
			sql+=" union all ";
			sql+=sql113_98;
			
			sql+=" union all ";
			sql+=sql213_53;
		
		
		sql+=" order by 3,6";
		System.out.println("getTrxReciprocas "+sql);
		
		return sql;
		
	}
	
	
	////
	public String niif_patrimonio(String ide_geani, String mesI, String mesF){
		
		TablaGenerica tab_anio =utilitario.consultar("select ide_geani, detalle_geani from gen_anio where ide_geani="+ide_geani);
		int anio=pckUtilidades.CConversion.CInt(tab_anio.getValor("detalle_geani"));
		
		String niff_patrimonio="select rubro, sum(saldo_inicial) as saldo_inicial, sum(incremento) as incremento, sum(decremento) as decremento, sum(saldo_inicial+incremento-decremento) as saldo_final,fecha_inicial,fecha_final from "
				+" (select 'Capital' as rubro, haber_codem as saldo_inicial, 0.00 as incremento, 0.00 as decremento, mov_fecha_comov as fecha_inicial, mov_fecha_comov as fecha_final "
				 +" from cont_movimiento mov"
				 +" left join cont_detalle_movimiento dmov on dmov.ide_comov=mov.ide_comov"
				 +" left join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac"
				 +" where ide_cotia=4 and "
				 +" extract(year from mov_fecha_comov)="+anio+" and extract(month from mov_fecha_comov) between "+mesI+" and "+mesF+" and cue_codigo_cocac in ('611.07') "
				 +" union all "
				 +" select 'Capital Empresa Publica' as rubro, haber_codem as saldo_inicial, 0.00 as incremento, debe_codem as decremento, mov_fecha_comov as fecha_inicial, mov_fecha_comov as fecha_final "
				 +" from cont_movimiento mov"
				 +" left join cont_detalle_movimiento dmov on dmov.ide_comov=mov.ide_comov"
				 +" left join cont_catalogo_cuenta cat on cat.ide_cocac=dmov.ide_cocac"
				 +" where ide_cotia in (1,5) and "
				 +" extract(year from mov_fecha_comov)="+anio+" and extract(month from mov_fecha_comov) between "+mesI+" and "+mesF+" and cue_codigo_cocac like '611.07'"
				 +" union all "
				 +" SELECT 'Ajuste a resultados anteriores' as rubro, 0.00 as saldo_inicial, (haber_acumulado_cobac + haber_periodo_cobac - haber_inicial_cobac) as incremento, (debe_acumulado_cobac + debe_periodo_cobac - debe_inicial_cobac) as decremento, fecha_inicial_cobac as fecha_inicial, fecha_final_cobac as fecha_final"
				 +" FROM cont_balance_comprobacion bal"
				 +"   left join cont_catalogo_cuenta cat on cat.ide_cocac=bal.ide_cocac"
				 +" where cue_codigo_cocac like '618.01' and extract(year from fecha_inicial_cobac)="+anio+" and extract(year from fecha_final_cobac)="+anio+""
				 +" union all  "

				+" SELECT 'Resultados Ejercicios Anteriores' as rubro, 0.00 as saldo_inicial, (haber_inicial_cobac) as incremento, (debe_inicial_cobac) as decremento, fecha_inicial_cobac as fecha_inicial, fecha_final_cobac as fecha_final "
				+" FROM cont_balance_comprobacion bal   "
				+" left join cont_catalogo_cuenta cat on cat.ide_cocac=bal.ide_cocac" 
				+" where cue_codigo_cocac like '618.01' and extract(year from fecha_inicial_cobac)="+anio+" and extract(year from fecha_final_cobac)="+anio+""

				 + "union all "
				 +" SELECT 'Resultado del Ejercicio' as rubro, "
				 +"     0.00 as saldo_inicial, (case when valor>0 then valor else 0.00 end) as incremento,"
				 +"     (case when valor<0 then abs(valor) else 0.00 end) as decremento, fecha_inicial as fecha_inicial, fecha_final as fecha_final"
				 +" FROM cont_balance_general where cue_codigob like '618.03'"
				 +" and extract(year from fecha_inicial)="+anio+" and extract(year from fecha_final)="+anio
				 		
				 +" union all "
				 +"  SELECT 'Donaciones recibidas en Bienes' as rubro, "
				 +"     0.00 as saldo_inicial, (case when valor>0 then valor else 0.00 end) as incremento,"
				 +"     (case when valor<0 then abs(valor) else 0.00 end) as decremento, fecha_inicial as fecha_inicial, fecha_final as fecha_final"
				 +"  FROM cont_balance_general where cue_codigob like '611.99'"
				 +"  and extract(year from fecha_inicial)="+anio+" and extract(year from fecha_final)="+anio
				
				 +"  union all"
				 +"  SELECT 'Actualización de Activos' as rubro, "
				 +"      0.00 as saldo_inicial, (case when valor>0 then valor else 0.00 end) as incremento,"
				 +"      (case when valor<0 then abs(valor) else 0.00 end) as decremento, fecha_inicial as fecha_inicial, fecha_final as fecha_final"
				 +"  FROM cont_balance_general where cue_codigob like '629.51'"
				 +"  and extract(year from fecha_inicial)="+anio+" and extract(year from fecha_final)="+anio
  
		 		 +" ) a where (abs(saldo_inicial) + abs(incremento) + abs(decremento))>0 "
		 		 +" group by rubro, fecha_inicial,fecha_final "
		 		 +" order by rubro ";
						
		System.out.println("niff_patrimonio "+niff_patrimonio);
		
		return niff_patrimonio;
	}
	
	public String getCatalogoNiif_bg(){
		String cuentacontable ="SELECT cn1.ide_cocan, coalesce(cn1.cat_codigo_cocan,'') ||' '|| coalesce(cn1.descripcion_cocan,'') as descripcion"
				+" FROM cont_catalogo_niif cn1"
				+" left join cont_catalogo_niif cn2 on cn2.ide_cocan=cn1.con_ide_cocan"
				+" where cn1.activo_cocan=true and cn2.nivel_cocan=3 and cn1.nivel_cocan=4";
		return cuentacontable;
	}
	
	public String getCatalogoNiif_er(){
		String cuentacontable ="SELECT cn1.ide_cocan, coalesce(cn1.cat_codigo_cocan,'') ||' '|| coalesce(cn1.descripcion_cocan,'') as descripcion"
				+" FROM cont_catalogo_niif cn1"
				+" left join cont_catalogo_niif cn2 on cn2.ide_cocan=cn1.con_ide_cocan"
				+" where cn1.activo_cocan=true and cn2.nivel_cocan=2 and cn1.nivel_cocan=4";
		return cuentacontable;
	}

	
	public String getRptComprobantesPago(String ide_geani){
		String sql=("SELECT fecha_tecpo as fecha, comprobante_egreso_tecpo as numero_comprobante_egreso, detalle_coest as estado, (case when nombre_tepro is not null then nombre_tepro else NOMBRES_APELLIDOS end) as beneficiario, " +
			" detalle_tecpo as detalle_pago, " +
			" sum(devengado_prmen) as devengado, sum(pagado_prmen) as pagado" +
			" FROM public.tes_comprobante_pago tcp" +
			" left join tes_proveedor tp on tp.ide_tepro=tcp.ide_tepro" +
			" left join (SELECT EPAR.IDE_GEEDP, EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
			"         EMP.APELLIDO_PATERNO_GTEMP || ' ' || (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)  || ' ' ||EMP.PRIMER_NOMBRE_GTEMP || ' ' || (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) " +
			"	 AS NOMBRES_APELLIDOS, DEPA.DETALLE_GEDEP, EPAR.activo_geedp FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
			"	 LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
			"	 LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE) emp on emp.IDE_GEEDP=tcp.ide_geedp" +
			" join cont_estado ce on ce.ide_coest=tcp.ide_coest" +
			" join cont_detalle_movimiento cdm on cdm.ide_comov=tcp.ide_comov" +
			" join pre_mensual pm on pm.ide_codem=cdm.ide_codem" +
			" join pre_anual pa on pa.ide_pranu = pm.ide_pranu" +
			" join pre_poa pp on pp.ide_prpoa=pa.ide_prpoa" +
			" join pre_clasificador pc on pc.ide_prcla=pp.ide_prcla" +
			" where tcp.ide_geani= " +ide_geani+
			" group by fecha_tecpo, comprobante_egreso_tecpo, detalle_coest, nombre_tepro,NOMBRES_APELLIDOS ,detalle_tecpo" +
			" order by fecha_tecpo");
	
			return sql;
	}
	
}

