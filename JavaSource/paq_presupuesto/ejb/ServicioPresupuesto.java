package paq_presupuesto.ejb;


import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Utilitario;
import framework.aplicacion.TablaGenerica;

/**
 * Session Bean implementation class ServicioPresupuesto
 */
@Stateless

public class ServicioPresupuesto {
	private Utilitario utilitario= new Utilitario();
	@EJB
	private ServicioContabilidad serv_contabilidad;
	@EJB
	private ServicioPresupuesto serv_presupuesto;
	/**
	 * Metodo que devuelve el Catalogo Presupuestario por los años vigentes
	 * @param estado recibe el o los estados true y false, ejemplo: true o false
	 * @param ide_geani recibe el año para filtar el ctalaogo presupuestario 
	 * @return String SQL Clasificador Presupuestario
	 */
	 public String getCatalogoPresupuestarioAnio(String estado,String ide_geani){
		 
		 String tab_presupesto="SELECT a.ide_prcla,codigo_clasificador_prcla,descripcion_clasificador_prcla " +
		 		" FROM pre_clasificador a,cont_vigente b,gen_anio c where a.ide_prcla = b.ide_prcla" +
				" and b.ide_geani= c.ide_geani and b.ide_geani ="+ide_geani +
				" and activo_prcla in ("+estado+")order by codigo_clasificador_prcla";
		 	 return tab_presupesto;
				 
	 }
	
		/**
		 * Metodo que devuelve el Catalogo Presupuestario para de esta manera desplegar en los autocompletar del aplicativo de esta manera no saturamos los combos de consulta
		 * @param estado recibe el o los estados true y false, ejemplo: true o false
		 * @return String SQL Clasificador Presupuestario solo para consulta de autompletar
		 */
	 public String getCatalogoPresupuestario(String activo){
		 
		 String tab_presupesto="SELECT ide_prcla,codigo_clasificador_prcla,descripcion_clasificador_prcla " +
		 		" FROM pre_clasificador where activo_prcla in (" +activo+")"+
					" ORDER BY codigo_clasificador_prcla";
		 return tab_presupesto;
				 
	 }
	 

	 
	 public String getCertificacion(String ide_geani, String activo){

			String tab_certificacion=("select ide_prcer,nro_certificacion_prcer,num_documento_prcer,fecha_prcer, detalle_prcer " +
					"from pre_certificacion where activo_prcer in ("+activo+") and ide_geani in ("+ide_geani+") order by fecha_prcer desc, nro_certificacion_prcer desc ");
			return tab_certificacion;

	 }
	 
	public String getCatalogoPresupuestario2(String activo){
		 
		 String tab_presupesto="SELECT ide_prcla,codigo_clasificador_prcla || ' - ' || descripcion_clasificador_prcla " +
		 		" FROM pre_clasificador where activo_prcla in (" +activo+")"+
					" ORDER BY codigo_clasificador_prcla";
		 return tab_presupesto;
				 
	 }
	 
	 public String getCatalogoPoa(String activo){
		 
		 String tab_presupesto="select a.ide_prfup"
			 	  + " , substring(programa || ' - ' || detalle_programa from 1 for 39) as programa "
			  	  + ", substring(' - ' || proyecto || ' - ' || detalle_proyecto from 1 for 40) as proyecto "
				  + ", substring(' - ' || producto || ' - ' || detalle_producto from 1 for 40) as producto "
				  + ", ' - SUBACTIVIDAD - ' || detalle_subactividad || ' - CODIGO - ' || codigo_subactividad as subactividad "
				  +"from ("+getUbicacionPOA()+") a ; ";
		 if(activo.equals("true"))
		 {
		  tab_presupesto="select a.ide_prfup"
			  + ", producto || ' - ' || detalle_producto || ' - SUBACTIVIDAD - ' || detalle_subactividad || ' - CODIGO - ' || codigo_subactividad as subactividad "
			  +"from ("+getUbicacionPOA()+") a ; ";
		 }
		 
		 System.out.println("getCatalogoPoa "+tab_presupesto);
		 return tab_presupesto;
				 
	 }
	 
	 public TablaGenerica getTablaCatalogoPresupuestario(String ideClasificador){
		 
		 TablaGenerica tab_presupesto=utilitario.consultar("SELECT ide_prcla,codigo_clasificador_prcla,descripcion_clasificador_prcla " +
		 		" FROM pre_clasificador where ide_prcla=" +ideClasificador+
					" ORDER BY codigo_clasificador_prcla");
		 return tab_presupesto;
				 
	 } 
	 public String getFuncionPrograma(){
		String tab_funcion="select ide_prfup,detalle_prfup,codigo_prfup  " +
				"from pre_funcion_programa ";
		return tab_funcion;
		
	}
	 public String getCatalogoContabilizar(String tramite_presupuestario, String tipo_mov_presupuestario){
		String tab_funcion=" select ide_prasp,a.ide_prcla,cue_codigo_cocac,cue_descripcion_cocac, codigo_clasificador_prcla,descripcion_clasificador_prcla ,d.ide_gelua,detalle_gelua,devengado,ide_prmop"
				+" from pre_asociacion_presupuestaria a, pre_clasificador b,cont_catalogo_cuenta c, gen_lugar_aplica d"
				+" where a.ide_prcla = b.ide_prcla and a.ide_cocac= c.ide_cocac and a.ide_gelua = d.ide_gelua"
				+" and b.ide_prcla in ("
				+" select ide_prcla from pre_poa where ide_prpoa in (select ide_prpoa from pre_poa_tramite where ide_prtra in ("+tramite_presupuestario+"))"
				+" )"
				+" and ide_prmop in ("+tipo_mov_presupuestario+")"
				+" order by cue_codigo_cocac";
		System.out.println("getCatalogoContabilizar "+tab_funcion);
		return tab_funcion;
		
	}
	 
	 public String getCatalogoContabilizarDevengados(String comprobante_pago, String tipo_mov_presupuestario){
		/*String tab_funcion=" select ide_prasp,a.ide_prcla,cue_codigo_cocac,cue_descripcion_cocac, codigo_clasificador_prcla,descripcion_clasificador_prcla ,d.ide_gelua,detalle_gelua,devengado,valor_devengar,ide_prmop"
				+" from pre_asociacion_presupuestaria a, pre_clasificador b,cont_catalogo_cuenta c, gen_lugar_aplica d"
				+" where a.ide_prcla = b.ide_prcla and a.ide_cocac= c.ide_cocac and a.ide_gelua = d.ide_gelua"
				+" and b.ide_prcla in ("
				+" select ide_prcla from pre_poa where ide_prpoa in (select ide_prpoa, sum(valor_devengar_tecmp) as valor_devengar from tes_comprobante_poa where ide_tecpo in ("+comprobante_pago+") group by ide_prpoa)"
				+" )"
				+" and ide_prmop in ("+tipo_mov_presupuestario+")"
				+" order by cue_codigo_cocac";*/
		
		
		String tab_funcion=" select ide_prasp,a.ide_prcla,cue_codigo_cocac,cue_descripcion_cocac, codigo_clasificador_prcla,descripcion_clasificador_prcla ,d.ide_gelua,detalle_gelua,devengado,0 as valor_devengar,ide_prmop"
				+" from pre_asociacion_presupuestaria a, pre_clasificador b,cont_catalogo_cuenta c, gen_lugar_aplica d"
				+" where a.ide_prcla = b.ide_prcla and a.ide_cocac= c.ide_cocac and a.ide_gelua = d.ide_gelua"
				+" and b.ide_prcla in ("
				+" select ide_prcla from pre_poa where ide_prpoa in (select ide_prpoa from tes_comprobante_poa where ide_tecpo in ("+comprobante_pago+") )"
				+" )"
				+" and ide_prmop in ("+tipo_mov_presupuestario+")"
				+" order by cue_codigo_cocac";
		
		
		
		System.out.println("getCatalogoContabilizarDevengados "+tab_funcion);
		return tab_funcion;
		
	}
	 
	private TablaGenerica getTablaGenericaFuncionPro(String ideanio){
		TablaGenerica tab_funcion_progra=utilitario.consultar("select ide_prfup,detalle_prfup from pre_funcion_programa " +
				" where ide_prfup in (select ide_prfup from cont_vigente where ide_geani=" +ideanio+
				
				"	order by detalle_prfup");
		return tab_funcion_progra;
		
	}
	public String getTramite (String activo){
		String tab_tramite="select ide_prtra,secuencial_prtra as nro_compromiso,numero_oficio_prtra,fecha_tramite_prtra,observaciones_prtra from pre_tramite where activo_prtra in ("+activo+") " +
				" order by fecha_tramite_prtra desc";
		return tab_tramite;
		
	}
	
	public TablaGenerica getTablaGenericaTramite (String ide_prtra, String ide_adfac ){
		String sql="";
		
		if(ide_adfac.length()>0)
			sql="select comp.ide_prtra,numero_oficio_prtra from pre_tramite comp"
	           +" join adq_factura faC on faC.ide_prtra=comp.ide_prtra where ide_adfac in ("+ide_adfac+") order by numero_oficio_prtra";
		else
			sql="select ide_prtra,numero_oficio_prtra from pre_tramite where ide_prtra in ("+ide_prtra+") order by numero_oficio_prtra";
		
		TablaGenerica tab_tramite=utilitario.consultar(sql);
		return tab_tramite;
	}
	
	public TablaGenerica getTablaGenericaTramite (String ide_prtra){
		String sql="select ide_prtra,numero_oficio_prtra from pre_tramite where ide_prtra in ("+ide_prtra+") order by numero_oficio_prtra";
		
		TablaGenerica tab_tramite=utilitario.consultar(sql);
		return tab_tramite;
	}
	
	public TablaGenerica getTablaGenericaComp (String ide_prtra ){
		TablaGenerica tab_tramite=utilitario.consultar("select distinct b.ide_prpoa,b.ide_prfuf,a.ide_prtra,b.comprometido_prpot,b.ide_prpot,(case when valor_devengado is null then 0 else valor_devengado end) as devengado, "
				+"b.comprometido_prpot - (case when valor_devengado is null then 0 else valor_devengado end) as saldo_devengar,observaciones_prtra "
				+"from pre_tramite a "
				+"left join pre_poa_tramite b on a.ide_prtra = b.ide_prtra "
				+"left join (select b.ide_prpoa,a.ide_prtra,a.ide_prfuf,comp.ide_prpot, sum(devengado_prmen) as valor_devengado " 	
				+"from pre_mensual a, pre_anual b, pre_poa_tramite comp "					
				+"where a.ide_pranu = b.ide_pranu and a.ide_prfuf=comp.ide_prfuf and  a.ide_prtra=comp.ide_prtra and b.ide_prpoa=comp.ide_prpoa "			
				+"group by b.ide_prpoa,a.ide_prtra,a.ide_prfuf,comp.ide_prpot ) c on b.ide_prpot = c.ide_prpot where a.ide_prtra in ("+ide_prtra+")");
		tab_tramite.imprimirSql();
	
		return tab_tramite;
	}
	
	public String getPoa (String ide_geani,String activo,String presupuesto){
		String tab_poa="select a.ide_prpoa,a.ide_prpoa as codigo,detalle_subactividad,presupuesto_inicial_prpoa,codigo_clasificador_prcla,codigo_subactividad,detalle_programa,descripcion_clasificador_prcla,programa," +
				" detalle_proyecto,proyecto,detalle_producto,producto,detalle_actividad,actividad," +
				" subactividad,fecha_inicio_prpoa,fecha_fin_prpoa,num_resolucion_prpoa," +
				" presupuesto_codificado_prpoa,reforma_prpoa,detalle_geani,detalle_geare" +
				" from pre_poa a left join  gen_anio b on a.ide_geani= b.ide_geani left join pre_clasificador c on a.ide_prcla = c.ide_prcla left join " +
	//			" (select a.ide_prfup,codigo_subactividad,detalle_subactividad,subactividad,detalle_actividad,actividad,detalle_producto,producto,detalle_proyecto," +
	//			" proyecto,detalle_programa ,programa from (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_subactividad,detalle_prfup as detalle_subactividad," +
	//			" detalle_prnfp as subactividad from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =5) a , " +
	//			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad,detalle_prfup as detalle_actividad,detalle_prnfp as actividad" +
	//			" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =4) b, " +
	//			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto,detalle_prfup as detalle_producto,detalle_prnfp as producto" +
	//			" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =3 ) c, " +
	//			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto,detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto" +
	//			" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =2) d, " +
	//			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa,detalle_prfup as detalle_programa,detalle_prnfp as programa" +
	//			" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =1) e where a.pre_ide_prfup = b.ide_prfup" +
	//			" and b.pre_ide_prfup = c.ide_prfup and c.pre_ide_prfup = d.ide_prfup and d.pre_ide_prfup = e.ide_prfup ) f on a.ide_prfup = f.ide_prfup" +
	
				" ("+getUbicacionPOA()+" ) f on a.ide_prfup = f.ide_prfup" +
	
				" left join gen_area g on a.ide_geare=g.ide_geare "
				+ " where a.ide_geani= "+ide_geani+" and coalesce(activo_prpoa,false) in ("+activo+") and coalesce(ejecutado_presupuesto_prpoa,false) in ("+presupuesto+") ";
		
		//if(!ide_geare.contains("-1"))
		//	tab_poa += " and g.ide_geare="+ide_geare;
		
		tab_poa += " order by codigo_subactividad,a.ide_prpoa";
		
		 System.out.println("getPoa: "+tab_poa);
		 return tab_poa;
	}  
	
	public String getPoa (String ide_geani,String activo,String presupuesto,int area,boolean saldo){
		String tab_poa="select a.ide_prpoa,a.ide_prpoa as codigo,detalle_subactividad, coalesce(presupuesto_inicial_prpoa,0) + coalesce(reforma_prpoa,0) as presupuesto_codificado_prpoa,codigo_clasificador_prcla,(coalesce(presupuesto_inicial_prpoa,0) + coalesce(reforma_prpoa,0)) - coalesce(valor_certificado_prpoa,0) as saldo,codigo_subactividad,detalle_programa,descripcion_clasificador_prcla,programa," +
				" detalle_proyecto,proyecto,detalle_producto,producto,detalle_actividad,actividad," +
				" subactividad,fecha_inicio_prpoa,fecha_fin_prpoa,num_resolucion_prpoa," +
				" presupuesto_inicial_prpoa,reforma_prpoa,detalle_geani,detalle_geare" +
				" from pre_poa a left join  gen_anio b on a.ide_geani= b.ide_geani left join pre_clasificador c on a.ide_prcla = c.ide_prcla left join " +

				" ("+getUbicacionPOA()+" ) f on a.ide_prfup = f.ide_prfup" +
	
				" left join gen_area g on a.ide_geare=g.ide_geare where a.ide_geare="+area+" and a.ide_geani= "+ide_geani+" and coalesce(activo_prpoa,false) in ("+activo+") and coalesce(ejecutado_presupuesto_prpoa,false) in ("+presupuesto+") ";
					
		if(saldo)
		{
			tab_poa	+= " and (coalesce(presupuesto_inicial_prpoa,0) + coalesce(reforma_prpoa,0)) - coalesce(valor_certificado_prpoa,0) >0 ";
		}
		tab_poa	+= " order by codigo_subactividad,a.ide_prpoa";
		
		System.out.println("getPoa: "+tab_poa);
		return tab_poa;
	} 
	
	public String cetificacion(String anio){
		String sql="select ide_prcer,nro_certificacion_prcer,detalle_prcer,num_documento_prcer,valor_certificacion_prcer from pre_certificacion where ide_geani in ("+anio+") order by num_documento_prcer";
		return sql;
	}
	public String getPoaNombre (String ide_geani){
		String tab_poa="select ide_prpoa, codigoERP, CODIGO_CLASIFICADOR_PRCLA, area,CODIGO_SUBACTIVIDAD, DETALLE_PROGRAMA, DETALLE_PROYECTO, DETALLE_PRODUCTO, DESCRIPCION_CLASIFICADOR_PRCLA, DETALLE_ACTIVIDAD, DETALLE_SUBACTIVIDAD from ( "
				+" select a.ide_prpoa, cast(a.ide_prpoa as character(10)) as codigoERP,'Partida Presupuestaria:   '|| case when codigo_clasificador_prcla is null then 'N/A' else codigo_clasificador_prcla end as codigo_clasificador_prcla,'	|	Codigo SUB-ACTIVIDAD:	'||codigo_subactividad as codigo_subactividad,'		|	Programa: 	'||detalle_programa as detalle_programa,'	|	Proyecto: 	'||detalle_proyecto as detalle_proyecto,'	|	Producto: 	'||detalle_producto as detalle_producto,"
				+" '	|	Nombre Cuenta Presupuestaria: 	'|| case when descripcion_clasificador_prcla is null then 'N/A' else descripcion_clasificador_prcla end as descripcion_clasificador_prcla,'	|	Actividad:	 '||detalle_actividad as detalle_actividad,'	|	Sub Actividad:	'||detalle_subactividad as detalle_subactividad, '		|	Area: 	' || detalle_geare as area "
				+" from pre_poa a "
				+" left join gen_anio b on a.ide_geani= b.ide_geani "
				+" left join pre_clasificador c on a.ide_prcla = c.ide_prcla "
				+" left join ("+getUbicacionPOA()+" ) f on a.ide_prfup = f.ide_prfup"
				+" left join gen_area g on a.ide_geare=g.ide_geare "
				+" where a.ide_geani in ("+ide_geani+") "
				+" ) b ";
			return tab_poa;
	} 
	
	public String getPartidaNombre (String ide_geani){
		String tab_poa=" select a.ide_prpoa, coalesce(codigo_clasificador_prcla, 'N/A') as codigo_clasificador,"
				+" '	|	'|| coalesce(descripcion_clasificador_prcla, 'N/A') as descripcion_clasificador "
				+" from pre_poa a "
				+" left join gen_anio b on a.ide_geani= b.ide_geani "
				+" left join pre_clasificador c on a.ide_prcla = c.ide_prcla "
				+" where a.ide_geani in ("+ide_geani+") ";
			return tab_poa;
	} 
	
	public String getAreaNombre (String ide_geani){
		String tab_poa=" select a.ide_prpoa, detalle_geare as area "
				+" from pre_poa a "
				+" left join gen_anio b on a.ide_geani= b.ide_geani "
				+" left join gen_area g on a.ide_geare=g.ide_geare "
				+" where a.ide_geani in ("+ide_geani+") ";
			return tab_poa;
	} 
	
	public String getPoaTodos(){
		String tab_poa=("select a.ide_prpoa,codigo_clasificador_prcla,codigo_subactividad,detalle_subactividad,descripcion_clasificador_prcla from pre_poa a" +
				" left join  gen_anio b on a.ide_geani= b.ide_geani left join pre_clasificador c on a.ide_prcla = c.ide_prcla left join " +
				" ( "+getUbicacionPOA()+" ) f on a.ide_prfup = f.ide_prfup" +
				" left join gen_area g on a.ide_geare=g.ide_geare  order by codigo_subactividad,a.ide_prpoa");
	
			return tab_poa;
	}
	
	public String getUbicacionPOA(){
		/*String tab_poa=(" select a.ide_prfup,codigo_subactividad,detalle_subactividad,subactividad,codigo_actividad,detalle_actividad,actividad,codigo_producto,cod_prod,detalle_producto,producto, "+
				" detalle_actividad_mc,actividad_mc,detalle_producto_mc,producto_mc,codigo_proyecto,cod_pry,detalle_proyecto, proyecto,codigo_programa,detalle_programa ,programa from " +
				
				" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_subactividad," +
				" detalle_prfup as detalle_subactividad, detalle_prnfp as subactividad from pre_funcion_programa a, pre_nivel_funcion_programa b " +
				" where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =7) a " +
				
				" left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad," +
				" detalle_prfup as detalle_actividad, detalle_prnfp as actividad from pre_funcion_programa a, pre_nivel_funcion_programa b " +
				" where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =6) b on a.pre_ide_prfup = b.ide_prfup" +
				
				" left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto," +
				" detalle_prfup as detalle_producto, detalle_prnfp as producto, codigo_pry_prd_prfup as cod_prod from pre_funcion_programa a, pre_nivel_funcion_programa b " +
				" where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =5) c on b.pre_ide_prfup = c.ide_prfup" +
				
				" left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad_mc," +
				" detalle_prfup as detalle_actividad_mc,detalle_prnfp as actividad_mc from pre_funcion_programa a, pre_nivel_funcion_programa b " +
				" where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =4) d on c.pre_ide_prfup = d.ide_prfup " +
				
				" left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto_mc," +
				" detalle_prfup as detalle_producto_mc,detalle_prnfp as producto_mc from pre_funcion_programa a, pre_nivel_funcion_programa b " +
				" where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =3 ) e on d.pre_ide_prfup = e.ide_prfup " +
				
				" left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto," +
				" detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto, codigo_pry_prd_prfup as cod_pry from pre_funcion_programa a, pre_nivel_funcion_programa b " +
				" where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =2) f on e.pre_ide_prfup = f.ide_prfup" +
				
				" left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa," +
				" detalle_prfup as detalle_programa,detalle_prnfp as programa from pre_funcion_programa a, pre_nivel_funcion_programa b " +
				" where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =1) g on f.pre_ide_prfup = g.ide_prfup ");*/
		
		String tab_poa="select a.ide_prfup,codigo_subactividad,detalle_subactividad,subactividad,codigo_actividad,detalle_actividad,actividad,codigo_producto,cod_prod,detalle_producto,producto, " +
				" codigo_actividad_mc,cod_act,detalle_actividad_mc,actividad_mc,codigo_producto_mc,cod_prod_mc,detalle_producto_mc,producto_mc,codigo_obra, detalle_obra_prfup as obra, " +
				" coalesce(pry1.codigo_proyecto,pry2.codigo_proyecto) as codigo_proyecto, " +
				" coalesce(pry1.cod_pry,pry2.cod_pry) as cod_pry,  " +
				" coalesce(pry1.detalle_proyecto,pry2.detalle_proyecto) as detalle_proyecto,  " +
				" coalesce(pry1.proyecto,pry2.proyecto) as proyecto, " +
				" coalesce(pry1.codigo_programa,pry2.codigo_programa) as codigo_programa, " +
				" coalesce(pry1.detalle_programa,pry2.detalle_programa) as detalle_programa, " +
				" coalesce(pry1.programa,pry2.programa) as programa,"+
				" coalesce(pry1.cod_prog,pry2.cod_prog) as cod_prog from  " +
				
				" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_subactividad, " +
				" detalle_prfup as detalle_subactividad, detalle_prnfp as subactividad from pre_funcion_programa a, pre_nivel_funcion_programa b  " +
				" where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =7) a  " +
				
				" left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad, " +
				" detalle_prfup as detalle_actividad, detalle_prnfp as actividad from pre_funcion_programa a, pre_nivel_funcion_programa b  " +
				" where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =6) b on a.pre_ide_prfup = b.ide_prfup " +
				
				" left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto, " +
				" detalle_prfup as detalle_producto, detalle_prnfp as producto, codigo_pry_prd_prfup as cod_prod from pre_funcion_programa a, pre_nivel_funcion_programa b  " +
				" where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =5) c on b.pre_ide_prfup = c.ide_prfup " +
				
				" left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad_mc,codigo_pry_prd_prfup as cod_act, " +
				" detalle_prfup as detalle_actividad_mc,detalle_prnfp as actividad_mc,detalle_obra_prfup"
				//+ ",case when length(codigo_obra_prfup) > 9 then codigo_obra_prfup else codigo_pry_prd_prfup end as codigo_obra "
				+ ",case when length(codigo_obra_prfup) > 0 then codigo_obra_prfup else '' end as codigo_obra "
				+ " from pre_funcion_programa a, pre_nivel_funcion_programa b  " +
				" where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =4) d on c.pre_ide_prfup = d.ide_prfup  " +
				
				" left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto_mc,codigo_pry_prd_prfup as cod_prod_mc, " +
				" detalle_prfup as detalle_producto_mc,detalle_prnfp as producto_mc from pre_funcion_programa a, pre_nivel_funcion_programa b  " +
				" where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =3 ) e on d.pre_ide_prfup = e.ide_prfup  " +
				
				" left join (select pr.ide_prfup, pr.pre_ide_prfup, codigo_proyecto, " +
				"    detalle_proyecto,proyecto, cod_pry,cod_prog,codigo_programa, detalle_programa,programa " +
				" 	  from (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto, " +
				" 	    detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto, codigo_pry_prd_prfup as cod_pry  " +
				" 	    from pre_funcion_programa a, pre_nivel_funcion_programa b  " +
				" 	    where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =2) pr " +
				" 	  left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa, codigo_pry_prd_prfup as cod_prog, " +
				" 	    detalle_prfup as detalle_programa,detalle_prnfp as programa from pre_funcion_programa a, pre_nivel_funcion_programa b  " +
				" 	    where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =1) g on pr.pre_ide_prfup = g.ide_prfup) pry1 on c.pre_ide_prfup = pry1.ide_prfup  " +
				
				" left join (select pr.ide_prfup, pr.pre_ide_prfup, codigo_proyecto, " +
				"    detalle_proyecto,proyecto, cod_pry,cod_prog,codigo_programa, detalle_programa,programa " +
				" 	  from (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto, " +
				" 	    detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto, codigo_pry_prd_prfup as cod_pry  " +
				" 	    from pre_funcion_programa a, pre_nivel_funcion_programa b  " +
				" 	    where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =2) pr " +
				" 	  left join (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa, codigo_pry_prd_prfup as cod_prog, " +
				" 	    detalle_prfup as detalle_programa,detalle_prnfp as programa from pre_funcion_programa a, pre_nivel_funcion_programa b  " +
				" 	    where a.ide_prnfp = b.ide_prnfp and b.nivel_prnfp =1) g on pr.pre_ide_prfup = g.ide_prfup) pry2 on e.pre_ide_prfup = pry2.ide_prfup  ";
	
			return tab_poa;
	}
	
	public String getPoaSaldosFuenteFinanciamiento(String ide_geani,String ide_prfuf,String tipo,String seleccionados, String ide_geare){
		String sql="";
				if(tipo.equals("1")){
					sql+="select * from (";
				}
		      sql+="select row_number() over(order by a.ide_prpoa,b.ide_prfuf) as codigo,a.ide_prpoa,b.ide_prfuf,num_resolucion_prpoa,codigo_clasificador_prcla,descripcion_clasificador_prcla,codigo_subactividad,"
		      		+" detalle_subactividad,detalle_prfuf,presupuesto_inicial_prpoa,presupuesto_codificado_prpoa,reforma_prpoa,(coalesce(presupuesto_inicial_prpoa,0)+coalesce(reforma_prpoa,0))-coalesce(valor_certificado_prpoa,0) as saldo_certificar,"
		      		+" valor_asignado,valor_reformado,valor_saldo_fuente,detalle_geare,"
				+" detalle_proyecto,detalle_producto,detalle_actividad,fecha_inicio_prpoa,fecha_fin_prpoa,"
				+" detalle_geani,detalle_programa"
				+" from ("
				+" select a.ide_prpoa,codigo_clasificador_prcla,codigo_subactividad,detalle_programa,detalle_subactividad,descripcion_clasificador_prcla,programa,"
				+" detalle_proyecto,proyecto,detalle_producto,producto,detalle_actividad,actividad,"
				+" subactividad,fecha_inicio_prpoa,fecha_fin_prpoa,num_resolucion_prpoa,presupuesto_inicial_prpoa,"
				+" presupuesto_codificado_prpoa,reforma_prpoa,valor_certificado_prpoa,detalle_geani,detalle_geare"
				+" from pre_poa a left join  gen_anio b on a.ide_geani= b.ide_geani left join pre_clasificador c on a.ide_prcla = c.ide_prcla left join" 
				
				+" ( "+getUbicacionPOA()+" ) f on a.ide_prfup = f.ide_prfup" 
				
				+" left join gen_area g on a.ide_geare=g.ide_geare where a.activo_prpoa=true and a.ide_geani="+ide_geani;
				if(pckUtilidades.CConversion.CInt(ide_geare)>0)
			    	  sql+=" and a.ide_geare = "+ide_geare;
				
				sql+=" order by codigo_subactividad,a.ide_prpoa" 
				+" ) a left join ("
				+" select a.ide_prpoa,a.ide_prfuf,detalle_prfuf,valor_asignado,(case when valor_reformado is null then 0 else valor_reformado end) as valor_reformado,"
				+" valor_asignado + (case when valor_reformado is null then 0 else valor_reformado end) as valor_saldo_fuente"
				+" from ("
				+" select ide_prpoa,a.ide_prfuf,detalle_prfuf,sum(valor_financiamiento_prpof ) as valor_asignado"
				+" from pre_poa_financiamiento a, pre_fuente_financiamiento b where a.ide_prfuf = b.ide_prfuf group by ide_prpoa,a.ide_prfuf,detalle_prfuf"
				+" ) a left join ("
				+" select ide_prpoa,ide_prfuf,sum(valor_reformado_prprf) as valor_reformado from pre_poa_reforma_fuente group by ide_prpoa,ide_prfuf"
				+" ) b on a.ide_prpoa = b.ide_prpoa and a.ide_prfuf = b.ide_prfuf"
				+" ) b on a.ide_prpoa = b.ide_prpoa "
				+" where b.ide_prfuf ="+ide_prfuf
				+" order by a.ide_prpoa,b.ide_prfuf";
		      if(tipo.equals("1")){
		    	  sql+=" ) a where codigo in ("+seleccionados+")  ";
		      }
		      System.out.println("sql "+sql);
		return sql;	
	}
	public TablaGenerica getTablaGenericaPoa(String ide_prpoa) {
		TablaGenerica tab_poa=utilitario.consultar("select ide_prpoa,b.ide_prcla,b.codigo_clasificador_prcla,b.descripcion_clasificador_prcla,coalesce(presupuesto_inicial_prpoa,0) + coalesce(reforma_prpoa,0) as presupuesto_codificado_prpoa,(coalesce(presupuesto_inicial_prpoa,0) + coalesce(reforma_prpoa,0)) - coalesce(valor_certificado_prpoa,0) as saldo " +
				" from pre_poa a,pre_clasificador b where a.ide_prcla=b.ide_prcla " +
				" and  ide_prpoa in("+ide_prpoa+")order by descripcion_clasificador_prcla");
		return tab_poa;
		
	}
	
	public TablaGenerica getTablaGenericaPoaXAnio(String ide_geani) {
		TablaGenerica tab_poa=utilitario.consultar("select ide_prpoa,b.ide_prcla,b.codigo_clasificador_prcla,b.descripcion_clasificador_prcla,coalesce(presupuesto_codificado_prpoa,0) as presupuesto_codificado_prpoa " +
				" from pre_poa a,pre_clasificador b where a.ide_prcla=b.ide_prcla " +
				" and a.ide_geani in("+ide_geani+") order by descripcion_clasificador_prcla");
		return tab_poa;
		
	}
	public TablaGenerica getTablaGenericaCert(String ide_prpoa) {
		TablaGenerica tab_poa=utilitario.consultar("select b.ide_prpoa,b.ide_prfuf,a.ide_prcer,b.valor_certificado_prpoc,b.ide_prpoc,(case when comprometido is null then 0 else comprometido end) as comprometido,"
				+" b.valor_certificado_prpoc - (case when comprometido is null then 0 else comprometido end) as saldo_comprometer,detalle_prcer,nro_contrato_proceso_prcer "
				+" from pre_certificacion a"
				+" left join pre_poa_certificacion b on a.ide_prcer = b.ide_prcer"
				+" left join (select sum(comprometido_prpot) as comprometido,ide_prpoc from pre_poa_tramite group by ide_prpoc) c on b.ide_prpoc = c.ide_prpoc where a.ide_prcer  in ("+ide_prpoa+")");
		return tab_poa;
		
	}
	public String getSaldoPoa(String ide_prpoa){
		String tab_programa=("select a.ide_prpoa,(valor_financiamiento_prpof + (case when valor_reformado is null then 0 else valor_reformado end )) - ((case when valor_certificado_prpoc is null then 0 else valor_certificado_prpoc end) + (case when valor_liquidado is null then 0 else valor_liquidado end)-(case when valor_liquidado is null then 0 else valor_liquidado end)) as saldo_poa,"
				+" c.ide_prfuf,valor_financiamiento_prpof,valor_certificado_prpoc,valor_reformado,valor_liquidado from pre_poa a left join pre_poa_financiamiento c on a.ide_prpoa=c.ide_prpoa"
				+" left join (select (case when  sum(valor_certificado_prpoc) is null then 0 else sum(valor_certificado_prpoc) end) as valor_certificado_prpoc,ide_prpoa,ide_prfuf from pre_poa_certificacion group by ide_prpoa,ide_prfuf) b on a.ide_prpoa = b.ide_prpoa and b.ide_prfuf = c.ide_prfuf"
					+" left join ( select sum(valor_reformado_prprf) as valor_reformado,ide_prpoa,ide_prfuf from pre_poa_reforma_fuente group by ide_prpoa,ide_prfuf) d on a.ide_prpoa = d.ide_prpoa and d.ide_prfuf=c.ide_prfuf"
					+" left join (select sum(valor_liquidado_prdcl) as valor_liquidado,ide_prpoa,ide_prfuf from  pre_detalle_liquida_certif group by ide_prpoa,ide_prfuf) e on a.ide_prpoa = e.ide_prpoa and b.ide_prfuf=e.ide_prfuf"
					+" where a.ide_prpoa = "+ide_prpoa);
		return tab_programa;
		
	}
	
	public String getPrograma (String activo){
		String tab_programa=("select  ide_prpro,cod_programa_prpro from pre_programa" +
				" where activo_prpro in ("+activo+") order by cod_programa_prpro");
		return tab_programa;
		
	}
	public String getTotalCertificadoPoa (String ide_prcer){
		String tab_certificacion=("select ide_prcer,ide_prpoa,sum(valor_certificado_prpoc)  as total_certificado from pre_poa_certificacion where ide_prcer in ("+ide_prcer+") group by ide_prcer,ide_prpoa");
		return tab_certificacion;
		
	}
	public TablaGenerica getTablaGenericaPrograma(String ide_prpro){
		TablaGenerica tab_programa=utilitario.consultar("select  ide_prpro,cod_programa_prpro from pre_programa where ide_prpro in ("+ide_prpro+")" +
				" order by cod_programa_prpro");
		return tab_programa;
	}
	public String getCertificacion(String activo){
		/*String tab_certificacion=("select ide_prcer,nro_certificacion_prcer,num_documento_prcer,fecha_prcer,substring(detalle_prcer from 1 for 80) as detalle " +
				"from pre_certificacion where activo_prcer in ("+activo+") order by fecha_prcer desc, nro_certificacion_prcer desc ");*/
		
		String tab_certificacion=("select ide_prcer,nro_certificacion_prcer,num_documento_prcer,fecha_prcer, detalle_prcer as detalle " +
				"from pre_certificacion where activo_prcer in ("+activo+") order by fecha_prcer desc, nro_certificacion_prcer desc ");
		return tab_certificacion;

	}
	/**
	 * Metodo que devuelve el POA a ser aprobado para la generacion del Presupuesto Inicial de Gastos
	 * @param estado recibe el o los estados true y false, ejemplo: true o false
	 * @param ide_geani recibe el año para filtar el POA 
	 * @return String SQL POA
	 */
	public String getPoaPorAprobarse(String estado,String ide_geani){
		 
		 String tab_presupesto="select ide_prpoa,a.ide_prcla,a.ide_prfup,presupuesto_inicial_prpoa,codigo_clasificador_prcla,codigo_subactividad,descripcion_clasificador_prcla,"
		+" detalle_subactividad,detalle_actividad,d.ide_geani,detalle_geani"
		+" from pre_poa a"
		+" left join ( "+getUbicacionPOA()+") b on a.ide_prfup = b.ide_prfup"
		+" left join pre_clasificador c on a.ide_prcla = c.ide_prcla" 
		+" left join ( select a.ide_geani,ide_prfup,detalle_geani from cont_vigente a, gen_anio b where  a.ide_geani = b.ide_geani" 
		+" and not ide_prfup is null order by detalle_geani desc"
		  +" ) d on a.ide_prfup = d.ide_prfup"
		+" where activo_prpoa in ("+estado+") and ide_prpro is null and d.ide_geani ="+ide_geani
		+" order by codigo_clasificador_prcla,codigo_subactividad";
	 	 return tab_presupesto;
			 
	}
	/**
	 * Metodo que devuelve el valor del financiamiento inicial por fuente de financiamiento
	 * @param ide_prfuf recibe el codigo de la fuente de financiamiento
	 * @param ide_geani recibe el año para filtar el POA 
	 * @return String SQL Valor inicial fuente de financiamiento
	 */
	public String getInicialFuenteFinanciamiento(String ide_prfuf,String ide_geani){
		String tab_certificacion=("select ide_prfuf,sum(valor_prffi) as valor from pre_fuente_financiamiento_ini where ide_geani="+ide_geani+" and ide_prfuf ="+ide_prfuf+" group by ide_prfuf");
		return tab_certificacion;
		
	}
	/**
	 * Metodo que devuelve el valor del ejecucion por fuente de financiamiento
	 * @param ide_prfuf recibe el codigo de la fuente de financiamiento
	 * @param ide_geani recibe el año para filtar el POA 
	 * @return String SQL Valor ejecutado en el poa por fuente de financiamiento
	 */
	public String getEjecutaFuenteFinanciamiento(String ide_prfuf,String ide_geani,String codigo){
		//System.out.println("entre al codigo "+codigo);
		String tab_certificacion=("select ide_prfuf, sum (valor) as valor from ("
				+" select ide_prfuf, sum(valor_financiamiento_prpof) as valor from pre_poa_financiamiento where ide_prfuf="+ide_prfuf+" and ide_prpoa in ("
				+" 		select ide_prpoa from pre_poa where ide_geani = "+ide_geani+") ");
					if(codigo==null){
						tab_certificacion+=" and 1=1 ";
					}
									
					else{
						tab_certificacion+=" and not ide_prpof ="+codigo;
					}
				tab_certificacion+= " group by ide_prfuf"
					+" 	union"
					+" 	select ide_prfuf, sum(valor_reformado_prprf) as valor from pre_poa_reforma_fuente where ide_prfuf="+ide_prfuf+" and ide_prpoa in ("
						+" 		select ide_prpoa from pre_poa where ide_geani ="+ide_geani+") and aprobado_prprf =true group by ide_prfuf"
						+" ) a group by ide_prfuf";
				
				System.out.println("ejecucion financiamiento "+tab_certificacion);
		return tab_certificacion;
		
	}
	public String saldoFuenteFinanciamiento(String ide_geani){
		String tab_certificacion="select a.ide_prfuf,detalle_prfuf,a.valor, (case when b.valor is null then 0 else b.valor end) as valor_ejecutado,"
				+" a.valor - (case when b.valor is null then 0 else b.valor end) as saldo"
				+" from ("
					+" 	select ide_prfuf,sum(valor_prffi) as valor from pre_fuente_financiamiento_ini where ide_geani="+ide_geani+" group by ide_prfuf"
						+" ) a"
						+" left join ("
							+" 	select ide_prfuf, (case when sum (valor) is null then 0 else sum (valor) end) as valor from ("
								+" 		select ide_prfuf, sum(valor_financiamiento_prpof) as valor from pre_poa_financiamiento where ide_prpoa in ("
									+" 			select ide_prpoa from pre_poa where ide_geani = "+ide_geani+" ) "
									+"			group by ide_prfuf"
										+" union"
										+" select ide_prfuf, sum(valor_reformado_prprf) as valor from pre_poa_reforma_fuente where  ide_prpoa in ("
											+" 	select ide_prpoa from pre_poa where ide_geani ="+ide_geani+" ) and aprobado_prprf =true group by ide_prfuf"
										+" ) a group by ide_prfuf"
								+" ) b on a.ide_prfuf = b.ide_prfuf"
								+" left join pre_fuente_financiamiento c on a.ide_prfuf = c.ide_prfuf"
								+" order by detalle_prfuf";
				return tab_certificacion;
	}
	public void apruebaPoa(String ide_poa,String codigo){
		String sql="insert into pre_programa(ide_prpro,ide_prfup,ide_prcla,cod_programa_prpro,activo_prpro)"
				+" select "+codigo+" as codigo,"
				+" a.ide_prfup,a.ide_prcla,codigo_clasificador_prcla||'.'||codigo_prfup as nuevo_codigo,true"
				+" from ( select a.ide_prfup,a.ide_prcla "
				+" 	from pre_poa a left join pre_programa b on a.ide_prfup = b.ide_prfup and a.ide_prcla=b.ide_prcla"
				+" where b.ide_prfup is null and b.ide_prcla is null"
				+" and ide_prpoa in ("+ide_poa+")"
				+" ) a,pre_funcion_programa b, pre_clasificador c"
				+" where a.ide_prfup = b.ide_prfup and a.ide_prcla = c.ide_prcla order by codigo_clasificador_prcla; ";
		System.out.println("apruebaPoa: "+sql);
			utilitario.getConexion().ejecutarSql(sql);	
	 }
	public String insertaVigente(String codigo,String programa,String anio){
		String sql="insert into cont_vigente (ide_covig,ide_prpro,ide_geani,activo_covig)"
				+" values ("+codigo+","+programa+","+anio+",true  )";
		return utilitario.getConexion().ejecutarSql(sql);
	
	}
	public void insertaFuenteEjecucion(String codigo,String ide_poa){
		String sql="insert into pre_poa_fuente_ejecucion (ide_prpfe,ide_prfuf,ide_prpoa,valor_certificado_prpfe,valor_compromiso_prpfe,valor_devengado_prpfe,activo_prpfe)"
				+" select row_number() over(order by a.ide_prfuf)+"+codigo+" as codigo,a.ide_prfuf,a.ide_prpoa,0,0,0,true "
				+" from pre_poa_financiamiento a"
				+" left join pre_poa_fuente_ejecucion b on a.ide_prfuf=b.ide_prfuf and a.ide_prpoa=b.ide_prpoa"
				+" where b.ide_prfuf is null and b.ide_prpoa is null and a.ide_prpoa ="+ide_poa;
		utilitario.getConexion().ejecutarSql(sql);
	
	}
	
	String sql="";
	public void trigActualizaReformaFuente(String ide_prpoa){
		 sql="delete from pre_poa_reforma  where ide_prpoa= "+ide_prpoa+";"
					+" insert into pre_poa_reforma (ide_prpor,ide_prpoa,valor_reformado_prpor,resolucion_prpor,activo_prpor,fecha_prpor,saldo_actual_prpor)"
					+" select row_number()over(order by ide_prpoa,fecha_prprf) + (select (case when max(ide_prpor) is null then 0 else max(ide_prpor) end) as maximo from pre_poa_reforma) as codigo,"
					+" ide_prpoa,valor_reformado,resolucion_prprf,estado,fecha_prprf,saldo"
					+" from ("
					+" select ide_prpoa,sum(valor_reformado_prprf) as valor_reformado,resolucion_prprf,true as estado,fecha_prprf,sum(saldo_actual_prprf) as saldo"
					+" from pre_poa_reforma_fuente a where activo_prprf=true"
					+" group by ide_prpoa,resolucion_prprf,fecha_prprf having ide_prpoa="+ide_prpoa+" order by ide_prpoa" 
					+" ) a"
					+" order by ide_prpoa,fecha_prprf;";
		utilitario.getConexion().ejecutarSql(sql);
	
	}
	public void trigActualizaReforma(String ide_prpoa){
		 sql="update pre_poa set reforma_prpoa =valor_reformado"
				 +" from ( select ide_prpoa,sum(valor_reformado_prpor) as valor_reformado from pre_poa_reforma a group by ide_prpoa having ide_prpoa="+ide_prpoa
				 +" 		 ) a where a.ide_prpoa = pre_poa.ide_prpoa; update pre_poa set presupuesto_codificado_prpoa=presupuesto_inicial_prpoa+reforma_prpoa where ide_prpoa="+ide_prpoa;
		utilitario.getConexion().ejecutarSql(sql);
	
	}
	public void trigValidaFuenteEjecucion(String ide_prpoa,String ide_prfuf){
		 sql="insert into pre_poa_fuente_ejecucion (ide_prpfe,ide_prpoa,ide_prfuf,valor_certificado_prpfe,valor_compromiso_prpfe,valor_devengado_prpfe,activo_prpfe)"
				 +" select row_number()over (order by ide_prfuf) +(select ( case when max (ide_prpfe) is null then 0 else max (ide_prpfe) end) as codigo from pre_poa_fuente_ejecucion) as codigo,"
				 +" ide_prpoa,ide_prfuf,0,0,0,true from ( select a.ide_prpoa,a.ide_prfuf from pre_poa_financiamiento a left join pre_poa_fuente_ejecucion b on a.ide_prpoa = b.ide_prpoa and a.ide_prfuf = b.ide_prfuf"
				 +" where a.ide_prpoa = "+ide_prpoa+" and a.ide_prfuf= "+ide_prfuf+" and b.ide_prfuf is null ) a";
		utilitario.getConexion().ejecutarSql(sql);
	
	}
	
	public void trigEjecutaCertificacion(String ide_prpoa,String ide_prfuf){
		 /*sql="update pre_poa_fuente_ejecucion set valor_certificado_prpfe=valor from ("
				 +"  select ide_prpoa,sum(valor_certificado_prpoc) as valor,activo_prpoc,ide_prfuf from pre_poa_certificacion where activo_prpoc = true and ide_prpoa="+ide_prpoa+" and ide_prfuf ="+ide_prfuf+" group by ide_prpoa,activo_prpoc,ide_prfuf"
			 +" ) a where a.ide_prpoa = pre_poa_fuente_ejecucion.ide_prpoa  and a.ide_prfuf = pre_poa_fuente_ejecucion.ide_prfuf; ";
		 utilitario.getConexion().ejecutarSql(sql);*/
		sql="update pre_poa_fuente_ejecucion set valor_certificado_prpfe=valor from ("
				 +"  select ide_prpoa,sum(valor_certificado_prpoc) as valor,ide_prfuf from pre_poa_certificacion where ide_prpoa="+ide_prpoa+" and ide_prfuf ="+ide_prfuf+" group by ide_prpoa,ide_prfuf"
			 +" ) a where a.ide_prpoa = pre_poa_fuente_ejecucion.ide_prpoa and a.ide_prfuf = pre_poa_fuente_ejecucion.ide_prfuf; ";
		 utilitario.getConexion().ejecutarSql(sql);
	}
	public void trigActualizaCertificadoPoa(String ide_prpoa){
		 sql="update pre_poa set valor_certificado_prpoa =valor from ("
				 +" select sum(valor_certificado_prpfe) as valor,ide_prpoa from pre_poa_fuente_ejecucion where ide_prpoa="+ide_prpoa+" group by ide_prpoa) a where a.ide_prpoa=pre_poa.ide_prpoa";
		utilitario.getConexion().ejecutarSql(sql);
	
	}
	public void trigCertificacionPreMensual(String ide_prcer){
		 sql="delete from pre_mensual where ide_prcer = "+ide_prcer+";"
				 +" INSERT INTO pre_mensual(ide_prmen, ide_pranu,fecha_ejecucion_prmen, comprobante_prmen, devengado_prmen, cobrado_prmen,"
				 +" 		 cobradoc_prmen, pagado_prmen, comprometido_prmen, valor_anticipo_prmen, activo_prmen,  certificado_prmen, ide_prfuf,ide_prcer)"
		 +" select row_number() over(order by ide_pranu) + (select (case when max(ide_prmen) is null then 0 else max(ide_prmen) end) as codigo from pre_mensual ) as codigo,"
		 +" b.ide_pranu,fecha_prcer,num_documento_prcer,0,0,0,0,0,0,true,valor_certificado_prpoc,a.ide_prfuf,a.ide_prcer"
		 +" from pre_poa_certificacion a, pre_anual b, pre_certificacion c"
		 +" where a.ide_prpoa = b.ide_prpoa and a.ide_prcer = c.ide_prcer and a.ide_prcer = "+ide_prcer+";";
		utilitario.getConexion().ejecutarSql(sql);
	
	}
	public void trigEjecutaCompromiso(String ide_prpoa,String ide_prfuf){
		 sql="update pre_poa_fuente_ejecucion set valor_compromiso_prpfe=valor from ("
				 +"  select ide_prpoa,sum(comprometido_prpot) as valor,ide_prfuf from pre_poa_tramite where activo_prpot = true and ide_prpoa="+ide_prpoa+" and ide_prfuf ="+ide_prfuf+" group by ide_prpoa,ide_prfuf"
			 +" ) a where a.ide_prpoa = pre_poa_fuente_ejecucion.ide_prpoa  and a.ide_prfuf = pre_poa_fuente_ejecucion.ide_prfuf; ";
		utilitario.getConexion().ejecutarSql(sql);
	}
	
	///ecevallos
	public void trigDeleteDetalleCompromiso(String ide_prpoa,String ide_prfuf){
		 //sql="update pre_poa_fuente_ejecucion set  valor_compromiso_prpfe = 0.00 where activo_prpfe = true and ide_prpoa="+ide_prpoa+" and ide_prfuf ="+ide_prfuf+";";
	        sql="";
		utilitario.getConexion().ejecutarSql(sql);
	 System.out.println(sql);
	}
	
	public void trigDeleteDetalleCertificado(String ide_prpoa,String ide_prfuf){
		//sql="update pre_poa_fuente_ejecucion set  valor_certificado_prpfe = 0.00 where activo_prpfe = true and ide_prpoa="+ide_prpoa+" and ide_prfuf ="+ide_prfuf+";";
	        sql="";
		utilitario.getConexion().ejecutarSql(sql);
	System.out.println(sql);
	}
	///
	
	public void trigActualizaCompromisoPoa(String ide_prpoa){
		 sql="update pre_poa set valor_compromiso_prpoa =valor from ("
				 +" select sum(valor_compromiso_prpfe) as valor,ide_prpoa from pre_poa_fuente_ejecucion where ide_prpoa="+ide_prpoa+" group by ide_prpoa) a where a.ide_prpoa=pre_poa.ide_prpoa";
		utilitario.getConexion().ejecutarSql(sql);
	
	}
	public void trigCompromisoPreMensual(String ide_prtra){
		 sql="delete from pre_mensual where ide_codem is null and ide_prtra in ("+ide_prtra+");"
				 +" INSERT INTO pre_mensual(ide_prmen, ide_pranu, ide_prtra,ide_comov, ide_codem, fecha_ejecucion_prmen, comprobante_prmen, devengado_prmen, cobrado_prmen," 
				 +"             cobradoc_prmen, pagado_prmen, comprometido_prmen, valor_anticipo_prmen, "
				 +"             activo_prmen,  certificado_prmen, ide_prfuf,ide_prcer)"
				 +" select row_number()over(order by a.ide_prtra)   +(select (case when max(ide_prmen) is null then 0 else max(ide_prmen) end) as codigo from pre_mensual)  as codigo,"
				 +" b.ide_pranu,a.ide_prtra,null,null,fecha_tramite_prtra,numero_oficio_prtra,0,0,0,0,comprometido_prpot,0,true,0,a.ide_prfuf,null"
				 +" from   pre_poa_tramite a,pre_anual b,pre_tramite c"
				 +" where a.ide_prpoa =b.ide_prpoa"
				 +" and a.ide_prtra =c.ide_prtra"
				 +" and a.ide_prtra ="+ide_prtra+";";
		utilitario.getConexion().ejecutarSql(sql);
	
	}

	public void actualizarSaldosReforma(String ide_prpoa,String valor_reformado,String nro_resolucion,String fecha,String saldo,String estado){
		
		if(estado.equals("true")){
			//System.out.println("entrea a insertar true ");
	
		// Consulto codigo maximo de la cabecera de la tabla de reformas
		TablaGenerica tab_maximo =utilitario.consultar(serv_contabilidad.servicioCodigoMaximo("pre_poa_reforma", "ide_prpor"));
		String maximo_cont_movimiento=tab_maximo.getValor("codigo");
		
		String sql_actualiza_saldos="insert into pre_poa_reforma (ide_prpor,ide_prpoa,valor_reformado_prpor,resolucion_prpor,activo_prpor,fecha_prpor,saldo_actual_prpor)"
				+" values("+maximo_cont_movimiento+","+ide_prpoa+","+valor_reformado+",'"+nro_resolucion+"',true,'"+fecha+"',"+saldo+")";
	
		//System.out.println("sql_actualiza_saldos "+sql_actualiza_saldos);
		
		utilitario.getConexion().ejecutarSql(sql_actualiza_saldos);
		
		String sql_actualiza_saldos_cabecera="update pre_poa"
				+" set reforma_prpoa=valor_reformado"
				+" from ( select sum(valor_reformado_prpor) as valor_reformado,ide_prpoa from pre_poa_reforma group by ide_prpoa) a"
				+" where a.ide_prpoa=pre_poa.ide_prpoa and a.ide_prpoa ="+ide_prpoa+"; "
				+" update pre_poa"
				+" set presupuesto_codificado_prpoa=presupuesto_inicial_prpoa +reforma_prpoa"
				+" where ide_prpoa ="+ide_prpoa+";";
		//System.out.println("sql_actualiza_saldos_cabecera "+sql_actualiza_saldos_cabecera);
		
		utilitario.getConexion().ejecutarSql(sql_actualiza_saldos_cabecera);
		}
	}

	public int validoSecuencialCompromiso(String ide_geani, String secuencial_prtra){
		int secuencial=0; 
		
		String sql = "SELECT ide_prtra, secuencial_prtra FROM pre_tramite where ide_geani="+ide_geani+" and secuencial_prtra="+secuencial_prtra;
	
		List list_sql1 = utilitario.getConexion().consultar(sql);	
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
			secuencial=pckUtilidades.CConversion.CInt(list_sql1.get(1));
		}
		
		System.out.println("secuencial: "+secuencial);
		
		return secuencial;
	}
	
	public int maximoSecuencialCompromiso(String ide_geani){
		int secuencial=0; 
		
		String sql = "SELECT max(secuencial_prtra) FROM pre_tramite where ide_geani="+ide_geani;
	
		List list_sql1 = utilitario.getConexion().consultar(sql);	
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
			secuencial=pckUtilidades.CConversion.CInt(list_sql1.get(0));
		}
		
		System.out.println("secuencialMax: "+secuencial);
		
		return secuencial;
	}
	
	public String getReformasPac(String ide_geani, String ide_copec, String aplica_portal)
	{
		String sql="";
		
		sql="select row_number() over(order by pac.ide_prpac) as codigo"
	    +" ,pac.ide_prpac, detalle_coest as estado, detalle_geani as anio, detalle_copec as cuatrimestre ,cpc_prpac"
	    +" ,pc.codigo_clasificador_prcla as partida,detalle_cotio as tipo_compra, tipo_prod_prpac, tregimen_prpac"
	    +" ,detalle_adtic as procedimiento, cat_electronico_prpac, descripcion_prpac as descripcion ,cantidad_prpac"
	    +" ,detalle_bounm as unidad_medida, valor_unitario_prpac, valor_total_prpac"
	    +" ,(coalesce(codificado,0.00) + coalesce(reforma,0.00)) as presupuesto_codificado,ppac.ide_prpoa"
	   
		+" FROM pre_pac pac "
		+" left join cont_estado est on est.ide_coest=pac.ide_coest "
		+" left join gen_anio anio on anio.ide_geani=pac.ide_geani "
		+" left join cont_periodo_cuatrimestre cua on cua.ide_copec=pac.ide_copec "
		+" left join cont_tipo_compra tc on tc.ide_cotio=pac.ide_cotio "
		+" left join bodt_unidad_medida um on um.ide_bounm=pac.ide_bounm "
		+" left join adq_tipo_contratacion tcon on tcon.ide_adtic=pac.ide_adtic "
		+" join pre_partida_pac ppac on ppac.ide_prpac=pac.ide_prpac "
		+" left join pre_clasificador pc on pc.ide_prcla = ppac.ide_prcla "
		
		+" left join (select sum(coalesce(valor_financiamiento_prpof,0)) as codificado, ide_prpoa from pre_poa_financiamiento  pf  "
		+" 	  group by ide_prpoa) pf on pf.ide_prpoa = ppac.ide_prpoa "
		+" left join (select sum(coalesce(valor_reformado_prprf,0)) as reforma, ide_prpoa from pre_poa_reforma_fuente  rf   "
		+" 	  group by ide_prpoa) rf on rf.ide_prpoa = ppac.ide_prpoa "
		
		+" where pac.ide_geani="+ide_geani+" and pac.ide_copec="+ide_copec+" and coalesce(portal_prpap,false)  in ("+aplica_portal+")";
		
		System.out.println("getReformasPac "+sql);
		return sql;
	}
	
	public String getLineasPac(String ide_geani, String ide_geare, boolean tecnico, String ide_gtemp)
	{
		String sql="";
		
		sql="select distinct pac.ide_prpac, detalle_copec as cuatrimestre, descripcion_prpac as descripcion "
				+" ,cpc_prpac,detalle_cotio as tipo_compra "
				+" ,detalle_adtic as procedimiento, valor_total_prpac, NOMBRES_APELLIDOS as responsable "
				+" FROM pre_pac pac   "
				+" left join cont_periodo_cuatrimestre cua on cua.ide_copec=pac.ide_copec  "
				+" left join cont_tipo_compra tc on tc.ide_cotio=pac.ide_cotio  "
				+" left join adq_tipo_contratacion tcon on tcon.ide_adtic=pac.ide_adtic  "
				+" left join pre_responsable_contratacion rpac on rpac.ide_prpac=pac.ide_prpac and rpac.activo_prrec=true "
				+" left join (SELECT IDE_GTEMP, DOCUMENTO_IDENTIDAD_GTEMP, coalesce(PRIMER_NOMBRE_GTEMP,'') || ' ' || coalesce(SEGUNDO_NOMBRE_GTEMP,'') || ' ' || coalesce(APELLIDO_PATERNO_GTEMP,'') || ' ' || coalesce(APELLIDO_MATERNO_GTEMP,'') AS NOMBRES_APELLIDOS " 
				+"		FROM GTH_EMPLEADO EMP ) erpac on erpac.ide_gtemp=rpac.ide_gtemp "
				//+" join pre_partida_pac ppac on ppac.ide_prpac=pac.ide_prpac  "
				//+" left join pre_clasificador pc on pc.ide_prcla = ppac.ide_prcla  "
		
		+" where pac.ide_geani="+ide_geani+" and coalesce(activo_prpac,false)=true and pac.ide_prpac not in (SELECT distinct ide_prpac FROM precon_precontractual) ";
		
		if(tecnico)
			sql+=" and rpac.ide_gtemp="+ide_gtemp;
		else
			sql+=" and pac.ide_geare="+ide_geare;
		
		System.out.println("getLineasPac "+sql);
		return sql;
	}
	
	
		/**
		 * Metodo que devuelve el Catalogo Presupuestario por los años vigentes de TTHH
		 * @param estado recibe el o los estados true y false, ejemplo: true o false
		 * @param ide_geani recibe el año para filtar el ctalaogo presupuestario 
		 * @return String SQL Clasificador Presupuestario
		 */
	public String getCatalogoPresupuestarioAnioTTHH(String estado,String ide_geani){
		 
		 String tab_presupesto="SELECT a.ide_prcla,codigo_clasificador_prcla,descripcion_clasificador_prcla " +
		 		" FROM pre_clasificador a,cont_vigente b,gen_anio c where a.ide_prcla = b.ide_prcla" +
				" and b.ide_geani= c.ide_geani and b.ide_geani ="+ide_geani +
				" and activo_prcla in ("+estado+") "
				+ "and a.ide_prcla in(1070,1071,1088,701,702,752)"
				+ "order by codigo_clasificador_prcla";
		 	 return tab_presupesto;
				 
	}
	 
	 
	public String getTecho_futura(String ide_geani)
	{
		String sql="SELECT inversion_prtef, nro_resolucion_prtef, valor_prtef, coalesce(certificado,0) as valor_certificado "
				+" FROM public.pre_techo_futuras ptf "
				+" left join (SELECT cf.ide_geani,inversion_prgre, sum(valor_certificado_prpcf) as certificado "
				+" 	FROM pre_certificacion_futura cf "
				+" 	join pre_poa_certificacion_futura dcf on dcf.ide_prcef=cf.ide_prcef "
				+" 	join pre_poa p on p.ide_prpoa=dcf.ide_prpoa "
				+" 	join (SELECT pc.ide_prcla, pc3.codigo_clasificador_prcla || ' ' || pc3.descripcion_clasificador_prcla as tipo_gasto, coalesce(inversion_prgre,false) as inversion_prgre  "
				+" 		FROM public.pre_clasificador pc  "
				+" 		left join pre_clasificador pc1 on pc1.ide_prcla=pc.pre_ide_prcla  "
				+" 		left join pre_clasificador pc2 on pc2.ide_prcla=pc1.pre_ide_prcla  "
				+" 		left join pre_clasificador pc3 on pc3.ide_prcla=pc2.pre_ide_prcla "
				+" 		left join pre_grupo_economico ge on ge.ide_prgre=pc3.ide_prgre) pc on pc.ide_prcla=p.ide_prcla "
				+" 	group by cf.ide_geani,inversion_prgre) c on c.ide_geani=ptf.ide_geani and c.inversion_prgre=ptf.inversion_prtef "
				
				+" where activo_prtef=true and ptf.ide_geani="+ide_geani
				+" order by inversion_prtef";
		
		
		System.out.println("getTecho_futura "+sql);
		return sql;
	}
	
	public String getRptCertificaciones(String ide_geani){
		String sql=("select a.*, certificado - comprometido as saldo_por_comprometer" +
					" from (" +
					" SELECT poa.ide_prpoa,cla.ide_prcla, fecha_prcer, nro_certificacion_prcer, detalle_prcer, nro_contrato_proceso_prcer,detalle_geare as area," +
					" codigo_clasificador_prcla,descripcion_clasificador_prcla,detalle_programa,detalle_proyecto,detalle_actividad_mc,detalle_subactividad,detalle_prfuf," +
					" sum(coalesce(valor_certificado_prpoc,0)) as certificado," +
					" sum(coalesce(comprometido,0)) as comprometido " +
					"  FROM pre_certificacion cer" +
					" join pre_poa_certificacion cerd on cerd.ide_prcer=cer.ide_prcer" +
					" join pre_poa poa on poa.ide_prpoa=cerd.ide_prpoa" +
					" join pre_clasificador cla on cla.ide_prcla=poa.ide_prcla" +
					" join pre_fuente_financiamiento ff on ff.ide_prfuf=cerd.ide_prfuf" +
					" join gen_area area on area.ide_geare=poa.ide_geare " +
					" left join ( select comp.ide_prcer,compd.ide_prfuf,compd.ide_prpoa,sum(comprometido_prpot) as comprometido " +
					"           from pre_tramite comp" +
					"	   join pre_poa_tramite compd on compd.ide_prtra=comp.ide_prtra " +
					"	   group by comp.ide_prcer,compd.ide_prfuf,compd.ide_prpoa" +
					"          ) comp on comp.ide_prcer=cer.ide_prcer and comp.ide_prpoa = poa.ide_prpoa and comp.ide_prfuf=ff.ide_prfuf" +
					
					"  left join ( " +serv_presupuesto.getUbicacionPOA()+" ) upoa on upoa.ide_prfup = poa.ide_prfup  " +
									
					" where cer.ide_geani=" +ide_geani+
					" group by poa.ide_prpoa,cla.ide_prcla, fecha_prcer, nro_certificacion_prcer, detalle_prcer, nro_contrato_proceso_prcer,detalle_geare," +
					" codigo_clasificador_prcla,descripcion_clasificador_prcla,detalle_programa,detalle_proyecto,detalle_actividad_mc,detalle_subactividad,detalle_prfuf " +
					" order by nro_certificacion_prcer, codigo_clasificador_prcla" +
					" ) a");
		System.out.println("getRptCertificaciones: "+sql);
			return sql;
	}
	
	public String getRptCertificaciones(String ide_geani,String fecha_inicial,String fecha_final){
		String sql=("select a.*, certificado - comprometido as saldo_por_comprometer" +
					" from (" +
					" SELECT poa.ide_prpoa,cla.ide_prcla, fecha_prcer, nro_certificacion_prcer, detalle_prcer, nro_contrato_proceso_prcer,detalle_geare as area," +
					" codigo_clasificador_prcla,descripcion_clasificador_prcla,detalle_programa,detalle_proyecto,detalle_actividad_mc,detalle_subactividad,detalle_prfuf," +
					" sum(coalesce(valor_certificado_prpoc,0)) as certificado," +
					" sum(coalesce(comprometido,0)) as comprometido " +
					"  FROM pre_certificacion cer" +
					" join pre_poa_certificacion cerd on cerd.ide_prcer=cer.ide_prcer" +
					" join pre_poa poa on poa.ide_prpoa=cerd.ide_prpoa" +
					" join pre_clasificador cla on cla.ide_prcla=poa.ide_prcla" +
					" join pre_fuente_financiamiento ff on ff.ide_prfuf=cerd.ide_prfuf" +
					" join gen_area area on area.ide_geare=poa.ide_geare " +
					" left join ( select comp.ide_prcer,compd.ide_prfuf,compd.ide_prpoa,sum(comprometido_prpot) as comprometido " +
					"           from pre_tramite comp" +
					"	   join pre_poa_tramite compd on compd.ide_prtra=comp.ide_prtra " +
					"	   where fecha_tramite_prtra between '"+fecha_inicial+"' and '"+fecha_final+"'" +
					"	   group by comp.ide_prcer,compd.ide_prfuf,compd.ide_prpoa" +
					"          ) comp on comp.ide_prcer=cer.ide_prcer and comp.ide_prpoa = poa.ide_prpoa and comp.ide_prfuf=ff.ide_prfuf" +
					
					"  left join ( " +serv_presupuesto.getUbicacionPOA()+" ) upoa on upoa.ide_prfup = poa.ide_prfup  " +
									
					" where cer.ide_geani=" +ide_geani+ " and fecha_prcer between '"+fecha_inicial+"' and '"+fecha_final+"'"+
					" group by poa.ide_prpoa,cla.ide_prcla, fecha_prcer, nro_certificacion_prcer, detalle_prcer, nro_contrato_proceso_prcer,detalle_geare," +
					" codigo_clasificador_prcla,descripcion_clasificador_prcla,detalle_programa,detalle_proyecto,detalle_actividad_mc,detalle_subactividad,detalle_prfuf " +
					" order by nro_certificacion_prcer, codigo_clasificador_prcla" +
					" ) a");
		System.out.println("getRptCertificaciones fechas: "+sql);
			return sql;
	}
	
	public String getRptCompromisos(String ide_geani){
		String sql=("select a.*, comprometido-devengado as saldo_por_devengar " +
				" from (" +
				" SELECT poa.ide_prpoa,cla.ide_prcla, fecha_tramite_prtra as fecha_compromiso, secuencial_prtra as compromiso,nro_certificacion_prcer as certificacion,detalle_copag as tipo, numero_oficio_prtra,observaciones_prtra as detalle, nro_contrato_proceso_prtra,detalle_geare as area," +
				" ruc_tepro as ruc_proveedor,nombre_tepro as nombre_proveedor,codigo_clasificador_prcla as partida,descripcion_clasificador_prcla,detalle_programa,detalle_proyecto,detalle_actividad_mc,detalle_subactividad,detalle_prfuf as fuente" +
				" ,sum(comprometido_prpot) as comprometido , sum(coalesce(devengado,0)) as devengado" +
				"  FROM pre_tramite comp" +
				"  join cont_parametros_general tip on tip.ide_copag=comp.ide_copag"+
				" join pre_certificacion cert on cert.ide_prcer=comp.ide_prcer " +
				" join pre_poa_tramite compd on compd.ide_prtra=comp.ide_prtra " +
				" join pre_poa poa on poa.ide_prpoa=compd.ide_prpoa" +
				" join pre_clasificador cla on cla.ide_prcla=poa.ide_prcla" +
				" join pre_fuente_financiamiento ff on ff.ide_prfuf=compd.ide_prfuf" +
				" left join gen_area area on area.ide_geare=poa.ide_geare " +
				" left join tes_proveedor pro on pro.ide_tepro=comp.ide_tepro" +
				" left join (select sum(devengado_prmen) as devengado, ide_prpoa, pm.ide_prfuf,pm.ide_prtra " +
				"		from pre_mensual pm " +
				"		join pre_anual pa on pa.ide_pranu=pm.ide_pranu" +
				"		where ide_prpoa>0 and abs(devengado_prmen)>0 and pm.ide_prtra is not null " +
				"		group by ide_prpoa,pm.ide_prfuf,pm.ide_prtra) men on men.ide_prfuf=ff.ide_prfuf and men.ide_prpoa=poa.ide_prpoa and men.ide_prtra=comp.ide_prtra" +
				
				"  left join ( " +serv_presupuesto.getUbicacionPOA()+" ) ab on ab.ide_prfup=poa.ide_prfup  " +
				
				" where comp.ide_geani=" +ide_geani+
				" group by poa.ide_prpoa,cla.ide_prcla, fecha_tramite_prtra, secuencial_prtra,nro_certificacion_prcer,detalle_copag, numero_oficio_prtra,observaciones_prtra, nro_contrato_proceso_prtra,detalle_geare," +
				" ruc_tepro,nombre_tepro,codigo_clasificador_prcla,descripcion_clasificador_prcla,detalle_programa,detalle_proyecto,detalle_actividad_mc,detalle_subactividad,detalle_prfuf "+
				" order by secuencial_prtra, codigo_clasificador_prcla" +
				" ) a ");
	System.out.println("getRptCompromisos: "+sql);
			return sql;
	}
	
	public String getRptCompromisos(String ide_geani,String fecha_inicial,String fecha_final){
		String sql=("select a.*, comprometido-devengado as saldo_por_devengar " +
				" from (" +
				" SELECT poa.ide_prpoa,cla.ide_prcla, fecha_tramite_prtra as fecha_compromiso, secuencial_prtra as compromiso,nro_certificacion_prcer as certificacion,detalle_copag as tipo, numero_oficio_prtra,observaciones_prtra as detalle, nro_contrato_proceso_prtra,detalle_geare as area," +
				" ruc_tepro as ruc_proveedor,nombre_tepro as nombre_proveedor,codigo_clasificador_prcla as partida,descripcion_clasificador_prcla,detalle_programa,detalle_proyecto,detalle_actividad_mc,detalle_subactividad,detalle_prfuf as fuente" +
				" ,sum(coalesce(dlc.valor_comprometido_prdlc, comprometido_prpot)) as comprometido , sum(coalesce(devengado,0)) as devengado" +
				"  FROM pre_tramite comp" +
				"  join cont_parametros_general tip on tip.ide_copag=comp.ide_copag"+
				" join pre_certificacion cert on cert.ide_prcer=comp.ide_prcer " +
				" join pre_poa_tramite compd on compd.ide_prtra=comp.ide_prtra " +
				" join pre_poa poa on poa.ide_prpoa=compd.ide_prpoa" +
				" join pre_clasificador cla on cla.ide_prcla=poa.ide_prcla" +
				" join pre_fuente_financiamiento ff on ff.ide_prfuf=compd.ide_prfuf" +
				" left join gen_area area on area.ide_geare=poa.ide_geare " +
				" left join tes_proveedor pro on pro.ide_tepro=comp.ide_tepro" +
				" left join (select lc.ide_prtra,lc.fecha_prlic,dlc.ide_prpoa,dlc.ide_prfuf,valor_comprometido_prdlc,valor_liberado_prdlc   " +
				"		from pre_libera_compromiso lc  " +
				"		join pre_detalle_libera_compro dlc on dlc.ide_prlic=lc.ide_prlic  " +
				"		where fecha_prlic > '"+fecha_final+"') dlc on dlc.ide_prtra=comp.ide_prtra and dlc.ide_prpoa=poa.ide_prpoa and dlc.ide_prfuf=ff.ide_prfuf " +
				" left join (select sum(devengado_prmen) as devengado, ide_prpoa, pm.ide_prfuf,pm.ide_prtra " +
				"		from pre_mensual pm " +
				"		join pre_anual pa on pa.ide_pranu=pm.ide_pranu" +
				"		where ide_prpoa>0 and abs(devengado_prmen)>0 and pm.ide_prtra is not null and fecha_ejecucion_prmen between '"+fecha_inicial+"' and '"+fecha_final+"'" +
				"		group by ide_prpoa,pm.ide_prfuf,pm.ide_prtra) men on men.ide_prfuf=ff.ide_prfuf and men.ide_prpoa=poa.ide_prpoa and men.ide_prtra=comp.ide_prtra" +
				
				"  left join ( " +serv_presupuesto.getUbicacionPOA()+" ) ab on ab.ide_prfup=poa.ide_prfup  " +
				
				" where comp.ide_geani=" +ide_geani+ " and fecha_tramite_prtra between '"+fecha_inicial+"' and '"+fecha_final+"'"+
				" group by poa.ide_prpoa,cla.ide_prcla, fecha_tramite_prtra, secuencial_prtra,nro_certificacion_prcer,detalle_copag, numero_oficio_prtra,observaciones_prtra, nro_contrato_proceso_prtra,detalle_geare," +
				" ruc_tepro,nombre_tepro,codigo_clasificador_prcla,descripcion_clasificador_prcla,detalle_programa,detalle_proyecto,detalle_actividad_mc,detalle_subactividad,detalle_prfuf "+
				" order by secuencial_prtra, codigo_clasificador_prcla" +
				" ) a ");
	System.out.println("getRptCompromisos fechas: "+sql);
			return sql;
	}
	
	public String getRptDevengados(String ide_geani, String ide_prcla, String fuente){
		String sql= " SELECT " +
				" cla.ide_prcla, secuencial_prtra as compromiso, nro_contrato_proceso_prtra,fecha_ejecucion_prmen," +
				" codigo_clasificador_prcla as partida,descripcion_clasificador_prcla,detalle_prfuf as fuente, " +
				" mov.ide_comov as codigo_movimiento,mov_fecha_comov,cue_codigo_cocac as cuenta_contable," +
				" cue_descripcion_cocac as descripcion_contable,comprobante_egreso_tecpo,fecha_pago_tecpo," +
				" ruc_tepro as ruc_proveedor,nombre_tepro as nombre_proveedor," +
				" devengado_prmen,pagado_prmen" +

				" FROM pre_mensual pm  " +
				" join pre_fuente_financiamiento ff on ff.ide_prfuf=pm.ide_prfuf   " +
				" join cont_movimiento mov on mov.ide_comov=pm.ide_comov " +
				" join pre_tramite comp on comp.ide_prtra=pm.ide_prtra " +
				" join pre_anual pa on pa.ide_pranu=pm.ide_pranu" +
				" left join tes_comprobante_pago cp on cp.ide_comov=mov.ide_comov and cp.ide_tecpo=pm.ide_tecpo" +
				" left join tes_proveedor pro on pro.ide_tepro=cp.ide_tepro  " +
				" join pre_poa poa on poa.ide_prpoa=pa.ide_prpoa  " +
				" join pre_clasificador cla on cla.ide_prcla=poa.ide_prcla  " +
				" join cont_detalle_movimiento dmov on dmov.ide_codem=pm.ide_codem and dmov.ide_comov=mov.ide_comov" +
				" join cont_catalogo_cuenta cc on cc.ide_cocac=dmov.ide_cocac " +
 
				" where comp.ide_geani=" +ide_geani+" and cla.ide_prcla=" +ide_prcla;
		
		if(fuente.length()>1)
			if(!fuente.toUpperCase().equals("CONSOLIDADO"))
				sql+=  " and detalle_prfuf like '"+fuente+"' ";
		
		sql+=  " order by pm.ide_comov ";
				
	System.out.println("getRptDevengados: "+sql);
			return sql;
	}
	
	public String getRptDevengadosIngreso(String ide_geani, String ide_prcla){
		String sql=( " SELECT " +
				" cla.ide_prcla, fecha_ejecucion_prmen," +
				" codigo_clasificador_prcla as partida,descripcion_clasificador_prcla,detalle_prfuf as fuente, " +
				" mov.ide_comov as codigo_movimiento,mov_fecha_comov,cue_codigo_cocac as cuenta_contable," +
				" cue_descripcion_cocac as descripcion_contable,detalle_comov as descripcion_movimiento," +
				" devengado_prmen,cobrado_prmen" +
			
				" FROM pre_mensual pm " +
			
				" join pre_anual pa on pa.ide_pranu=pm.ide_pranu" +
				" join cont_detalle_movimiento dmov on dmov.ide_codem=pm.ide_codem" +
				" left join cont_catalogo_cuenta cc on cc.ide_cocac=dmov.ide_cocac" +
				" left join cont_movimiento mov on mov.ide_comov=pm.ide_comov" +
				" join pre_clasificador cla on cla.ide_prcla=pa.ide_prcla " +
				" join pre_fuente_financiamiento ff on ff.ide_prfuf=pm.ide_prfuf  "+
				" where (abs(devengado_prmen)+abs(cobrado_prmen))>0 and pa.ide_geani=" +ide_geani+" and cla.ide_prcla=" +ide_prcla
				);
	//System.out.println("getRptDevengados: "+sql);
			return sql;
	}
	
	
	public String getRptCertificacionesFuturas(String ide_geani){
		String sql=("SELECT fecha_prcef,nro_certificacion_prcef as secuencial_futura,nro_certificacion_prcer,detalle_coest, num_resolucion_prcef,  detalle_prcef,  nro_contrato_proceso_prcef" +
				",detalle_geare as area,codigo_clasificador_prcla,descripcion_clasificador_prcla,detalle_programa,detalle_proyecto,detalle_actividad_mc,detalle_subactividad,detalle_prfuf as fuente" +
				",anio_prpcf,valor_certificado_prpcf,fecha_modificacion_prpcf" +
			
				" FROM pre_certificacion_futura cf" +
				" left join cont_estado est on est.ide_coest=cf.ide_coest" +
				" left join pre_certificacion cer on cer.ide_prcer=cf.ide_prcer" +
				" join pre_poa_certificacion_futura pcf on pcf.ide_prcef=cf.ide_prcef" +
				" join pre_poa poa on poa.ide_prpoa=pcf.ide_prpoa" +
				" join pre_clasificador cla on cla.ide_prcla=poa.ide_prcla" +
				" join pre_fuente_financiamiento ff on ff.ide_prfuf=pcf.ide_prfuf" +
				" join gen_area area on area.ide_geare=cf.ide_geare" +
				" left join ( " +serv_presupuesto.getUbicacionPOA()+" ) upoa on upoa.ide_prfup = poa.ide_prfup  " +
				
				" where activo_prcef=true and cf.ide_geani=" +ide_geani+
				" order by nro_certificacion_prcef, codigo_clasificador_prcla,anio_prpcf");
		System.out.println("getRptCertificacionesFuturas: "+sql);
			return sql;
	}
	
	public String getRptEjecucionPresupuestoContratos(String ide_prcon){
		String sql=("select a.*, comprometido-devengado as saldo_por_devengar " +
				" from (" +
				" SELECT poa.ide_prpoa,fecha_tramite_prtra as fecha_compromiso, secuencial_prtra as compromiso,nro_certificacion_prcer as certificacion, numero_oficio_prtra,observaciones_prtra as detalle," +
				" codigo_clasificador_prcla as partida,descripcion_clasificador_prcla,detalle_subactividad,detalle_prfuf as fuente" +
				" ,sum(comprometido_prpot) as comprometido , sum(coalesce(devengado,0)) as devengado" +
				"  FROM pre_tramite comp" +
				"  join cont_parametros_general tip on tip.ide_copag=comp.ide_copag"+
				" join pre_certificacion cert on cert.ide_prcer=comp.ide_prcer " +
				" join pre_poa_tramite compd on compd.ide_prtra=comp.ide_prtra " +
				" join pre_poa poa on poa.ide_prpoa=compd.ide_prpoa" +
				" join pre_clasificador cla on cla.ide_prcla=poa.ide_prcla" +
				" join pre_fuente_financiamiento ff on ff.ide_prfuf=compd.ide_prfuf" +
				" left join (select sum(devengado_prmen) as devengado, ide_prpoa, pm.ide_prfuf,pm.ide_prtra " +
				"		from pre_mensual pm " +
				"		join pre_anual pa on pa.ide_pranu=pm.ide_pranu" +
				"		where ide_prpoa>0 and abs(devengado_prmen)>0 and pm.ide_prtra is not null " +
				"		group by ide_prpoa,pm.ide_prfuf,pm.ide_prtra) men on men.ide_prfuf=ff.ide_prfuf and men.ide_prpoa=poa.ide_prpoa and men.ide_prtra=comp.ide_prtra" +
				
				"  left join ( " +serv_presupuesto.getUbicacionPOA()+" ) ab on ab.ide_prfup=poa.ide_prfup  " +
				
				" where comp.ide_prcon=" +ide_prcon+
				" group by poa.ide_prpoa, fecha_tramite_prtra, secuencial_prtra,nro_certificacion_prcer,numero_oficio_prtra,observaciones_prtra," +
				" codigo_clasificador_prcla,descripcion_clasificador_prcla,detalle_subactividad,detalle_prfuf "+
				" order by secuencial_prtra, codigo_clasificador_prcla" +
				" ) a ");
	System.out.println("getRptEjecucionPresupuestoContratos: "+sql);
			return sql;
	}
}
