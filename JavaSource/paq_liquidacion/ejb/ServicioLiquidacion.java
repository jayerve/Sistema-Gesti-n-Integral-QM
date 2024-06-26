package paq_liquidacion.ejb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;



import paq_anticipos.ejb.ServicioAnticipo;
import paq_asistencia.ejb.ServicioAsistencia;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_sistema.aplicacion.Utilitario;
import framework.aplicacion.Parametro;
import framework.aplicacion.TablaGenerica;
//import com.sun.xml.internal.bind.marshaller.NioEscapeHandler;
import framework.componentes.Consulta;

 
/**
 * @author 
 *
 */
@Stateless

public class ServicioLiquidacion {
	
	public final static String P_NRH_RUBRO_IMPORTADO = "2";
	public final static String P_NRH_RUBRO_FORMULA = "0";
	public final static String P_NRH_RUBRO_TECLADO = "1";
	public final static String P_NRH_RUBRO_CONSTANTE = "3";

	public static String P_NRH_RUBRO_REGION="";
	public static String P_NRH_RUBRO_DESC_VALORES_LIQUIDAR="";
	public static String P_NRH_RUBRO_REMUNERACION_UNIFICADA="";
	public static String P_NRH_RUBRO_RMU_HONORARIOS="";
	public static String P_NRH_RUBRO_ACUMULA_FONDOS="";
	public static String P_NRH_RUBRO_DIAS_TRABAJADOS="";
	public static String P_NRH_RUBRO_DIAS_ANTIGUEDAD="";
	public static String P_NRH_RUBRO_DIAS_FONDOS_RESERVA="";
	public static String P_NRH_RUBRO_DIAS_PERIODO="";
	public static String P_NRH_RUBRO_RMU_CARGO_SUBROGANTE="";
	public static String P_NRH_RUBRO_DIAS_SUBROGADOS="";
	public static String P_NRH_RUBRO_DIAS_AJUSTE_SUELDO="";
	public static String P_NRH_RUBRO_AJUSTE_SUELDO="";
	public static String P_NRH_RUBRO_DIAS_PEND_VACACION="";
	public static String P_NRH_ACUMULA_DECIMOS="";
	public static String P_NRH_BASE_IMPONIBLE_MES_ANTERIOR="";
	public static String P_NRH_RUBRO_IMPONIBLE_MES_ANTERIOR="";
	public static String P_NRH_RUBRO_FONRESERV_ACUM_ANT="";
	public static String P_NRH_RUBRO_FONRESERV_NOM_ANT="";
	public static String P_NRH_RUBRO_FONRESERV_ACUM_PAGO="";
	public static String P_NRH_RUBRO_FONRESERV_NOM_PAGO="";
	private static String ide_rol_amortizacion="";

	private boolean vacacionesNegativo=false;
	private Double vacacionesNegativoValor=0.0;

	private Utilitario utilitario = new Utilitario();


	private TablaGenerica tab_deta_rubros_rol;


	@EJB
	private ServicioEmpleado serv_empleado;

	@EJB
	private ServicioGestion serv_gestion;


	@EJB
	private ServicioAnticipo serv_anticipo;
	@EJB
	private ServicioLiquidacion serv_nomina;

	@EJB
	private ServicioAsistencia serv_asistencia;
	
	public String validarAccionPersonalPermitida(String ide_geedp,String fecha_accion){
		
		String fecha_fin=utilitario.getAnio(fecha_accion)+"-12-31";
		
		String ide_gepro=getPeriodosRol(fecha_accion, fecha_fin);
		
		if (ide_gepro==null || ide_gepro.isEmpty()){
			ide_gepro="-1";
		}
		TablaGenerica tab_drol=utilitario.consultar("select * from NRH_DETALLE_ROL where ide_nrrol in ( " +
				"select IDE_NRROL from NRH_ROL where " +
				"ide_gepro in ( " +
				"select IDE_GEPRO from GEN_PERIDO_ROL where " +
				"ide_gepro in ("+ide_gepro+") "+
//				"to_date('"+fecha_accion+"','yyyy-mm-dd') " +
//				"BETWEEN FECHA_INICIAL_GEPRO and FECHA_FINAL_GEPRO " +
				") " +
				") and IDE_GEEDP in ("+ide_geedp+")");
		
		System.out.println("tab de rol "+tab_drol.getSql());
		
		if (tab_drol.getTotalFilas()>0){
			TablaGenerica tab_per=utilitario.consultar("select IDE_GEPRO,DETALLE_GEMES,DETALLE_GEANI from GEN_PERIDO_ROL pro " +
					"inner join GEN_MES mes on MES.ide_gemes=PRO.IDE_GEMES " +
					"inner join GEN_ANIO ani on ani.ide_geani=PRO.IDE_GEANI " +
					"where IDE_GEPRO IN ( " +
					""+ide_gepro+" " +
					") ORDER by mes.ide_gemes DESC ");
			String mes=tab_per.getValor("DETALLE_GEMES");
			String anio=tab_per.getValor("DETALLE_GEANI");		
			String str="No se puede generar la accion en la fecha "+fecha_accion+", " +
					"Debido a que el empleado seleccionado consta en la " +
					"nomina "+getTipoEmpleado(getDetalleTipoNomina(getRol(tab_drol.getValor("ide_nrrol")).getValor("ide_nrdtn")).getValor("ide_gttem")).getValor("detalle_gttem")+" " +
					"del  "+mes+"-"+anio+", "+
					"Si desea realizar la accion debe quitar al empleado de la nomina mensionada";
			return str;
		}
		
		return "Accion Valida";
	}

	public TablaGenerica getTablaBalanceIngresosEgresos(String ide_nrrol){
		TablaGenerica tab_cuadre=utilitario.consultar("select " +
				"a.ide_geedp as ide_contrato, " +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP ||' '|| (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) ||' '|| EMP.PRIMER_NOMBRE_GTEMP ||' '|| (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES, " +
				"total_ingresos, " +
				"total_egresos, " +
				"diferencia " +
				"from ( " +
				"select a.ide_geedp,debe as total_ingresos,haber as total_egresos,debe -haber as diferencia " +
				"from ( " +
				"select sum ( haber ) as haber,ide_geedp " +
				"from ( " +
				"select rua.IDE_GEARE,SUC.IDE_SUCU,ARE.DETALLE_GEARE,CUC.IDE_GECUC,RUB.IDE_NRRUB,RUA.IDE_GELUA,RUB.DETALLE_NRRUB, " +
				"CASE WHEN IDE_GELUA=1 THEN SUM (DRO.VALOR_NRDRO) ELSE 0 END AS DEBE , " +
				"CASE WHEN IDE_GELUA=2 THEN SUM (DRO.VALOR_NRDRO) ELSE 0 END AS HABER, ROL.IDE_NRDTN , " +
				"dro.ide_geedp " + 
				"from NRH_ROL ROL INNER JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
				"INNER JOIN NRH_TIPO_NOMINA TIN ON DTN.IDE_NRTIN=TIN.IDE_NRTIN " +
				"INNER JOIN NRH_DETALLE_ROL DRO ON DRO.IDE_NRROL=ROL.IDE_NRROL " +
				"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"INNER JOIN NRH_RUBRO_ASIENTO RUA ON RUA.IDE_NRRUB=RUB.IDE_NRRUB " +
				"INNER JOIN GEN_CUENTA_CONTABLE CUC ON CUC.IDE_GECUC=RUA.IDE_GECUC " +
				"INNER JOIN GEN_TIPO_ASIENTO TIA ON TIA.IDE_GETIA=RUA.IDE_GETIA and TIA.IDE_NRTIN=TIN.IDE_NRTIN " +
				"AND DTN.IDE_NRTIN=TIA.IDE_NRTIN INNER JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GEEDP=DRO.IDE_GEEDP " +
				"INNER JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU " +
				"INNER JOIN GEN_AREA ARE ON rua.ide_geare=are.ide_geare " +
				"WHERE ROL.IDE_NRROL in ("+ide_nrrol+") AND RUA.IDE_GETIA=1 and todos_nrrua=FALSE " +
				//				"and dro.ide_geedp in (54,403) " +
				"GROUP BY rua.IDE_GEARE,SUC.IDE_SUCU,SUC.NOM_SUCU,ARE.DETALLE_GEARE,RUB.IDE_NRRUB,RUA.IDE_GELUA,RUB.DETALLE_NRRUB, " +
				"CUC.IDE_GECUC,ROL.IDE_NRDTN,dro.ide_geedp " +
				"having SUM (DRO.VALOR_NRDRO) !=0 " +
				"ORDER BY RUB.IDE_NRRUB asc " +
				") a group by ide_geedp " +
				") a " +
				"left join ( " +
				"select sum ( debe ) as debe ,ide_geedp " +
				"from ( " +
				"select EDP.IDE_GEARE,SUC.IDE_SUCU,ARE.DETALLE_GEARE,CUC.IDE_GECUC,RUB.IDE_NRRUB,RUA.IDE_GELUA,RUB.DETALLE_NRRUB, " +
				"CASE WHEN IDE_GELUA=1 THEN SUM (DRO.VALOR_NRDRO) ELSE 0 END AS DEBE , " +
				"CASE WHEN IDE_GELUA=2 THEN SUM (DRO.VALOR_NRDRO) ELSE 0 END AS HABER, ROL.IDE_NRDTN , " +
				"dro.ide_geedp " +
				"from NRH_ROL ROL INNER JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
				"INNER JOIN NRH_TIPO_NOMINA TIN ON DTN.IDE_NRTIN=TIN.IDE_NRTIN INNER JOIN NRH_DETALLE_ROL DRO ON DRO.IDE_NRROL=ROL.IDE_NRROL " +
				"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"INNER JOIN NRH_RUBRO_ASIENTO RUA ON RUA.IDE_NRRUB=RUB.IDE_NRRUB " +
				"INNER JOIN GEN_CUENTA_CONTABLE CUC ON CUC.IDE_GECUC=RUA.IDE_GECUC " +
				"INNER JOIN GEN_TIPO_ASIENTO TIA ON TIA.IDE_GETIA=RUA.IDE_GETIA and TIA.IDE_NRTIN=TIN.IDE_NRTIN " +
				"AND DTN.IDE_NRTIN=TIA.IDE_NRTIN INNER JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GEEDP=DRO.IDE_GEEDP " +
				"INNER JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU INNER JOIN GEN_AREA ARE ON ARE.IDE_GEARE=EDP.IDE_GEARE " +
				"WHERE ROL.IDE_NRROL in ("+ide_nrrol+") AND RUA.IDE_GETIA=1 and todos_nrrua=TRUE " +
				//				"and dro.ide_geedp in (54,403) " +
				"GROUP BY EDP.IDE_GEARE,SUC.IDE_SUCU,SUC.NOM_SUCU,ARE.DETALLE_GEARE,RUB.IDE_NRRUB,RUA.IDE_GELUA,RUB.DETALLE_NRRUB, " +
				"CUC.IDE_GECUC,ROL.IDE_NRDTN, dro.ide_geedp " +
				"having SUM (DRO.VALOR_NRDRO) !=0 ORDER BY SUC.NOM_SUCU ASC,ARE.DETALLE_GEARE ASC " +
				") a group by ide_geedp " +
				") b on a.ide_geedp = b.idE_geedp " +
				"where debe != haber "+ 
				")a "+ 
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR edp on edp.ide_geedp=a.ide_geedp "+
				"inner join GTH_EMPLEADO emp on emp.ide_gtemp=edp.ide_gtemp "+
				"order by nombres ");
		System.out.println("cuadre "+tab_cuadre.getSql());
		return tab_cuadre;
	}

	public TablaGenerica getSriDeducibles(String ide_srimr){
		TablaGenerica tab_deducibles=utilitario.consultar("select a.ide_srded,a.detalle_srded,a.FRACCION_BASICA_SRDED, " +
				""+getFraccionBasicaDesgravadaImpuestoRenta(ide_srimr)+" * a.FRACCION_BASICA_SRDED as valor_maximo " +
				"from ( " +
				"select IDE_SRDED,detalle_srded,FRACCION_BASICA_SRDED from SRI_DEDUCIBLES " +
				"where IDE_SRIMR="+ide_srimr+")a ");
		//System.out.println("decucibles ");
		tab_deducibles.imprimirSql();
		
		return tab_deducibles;
	}

	public double getTotalGastosPersonales(String ide_srded,String ide_gtemp){
		try {
			return getSriDeduciblesEmpleado(ide_srded, ide_gtemp).getSumaColumna("VALOR_DEDUCIBLE_SRDEE");
		} catch (Exception e) {
			return 0;
			// TODO: handle exception
		}
	}
	public TablaGenerica getSriDeduciblesEmpleado(String ide_srded,String ide_gtemp){
		TablaGenerica tab_sri_de=utilitario.consultar("select * from SRI_DEDUCIBLES_EMPLEADO where IDE_SRDED="+ide_srded+" and IDE_GTEMP="+ide_gtemp);
		return tab_sri_de;
	}

	public double getTotalIngresosGravados(String ide_srimr,String ide_gtemp){
		// obtengo el total de ingresos proyectado y mensual del empleado
		TablaGenerica tab_det_proy_ing=getSriDetalleProyeccionIngresos(getSriProyeccionIngresos(ide_srimr, ide_gtemp).getValor("IDE_SRPRI"));

		try {
			return pckUtilidades.CConversion.CDbl_2(tab_det_proy_ing.getSumaColumna("VALOR_SRDPI")+"");	
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}


	public double getValorVecesFraccionBasicaDesgravada(String ide_srimr){
		double dou_porcentaje_veces=0;
		try {
			dou_porcentaje_veces=pckUtilidades.CConversion.CDbl_2(utilitario.getVariable("p_num_veces_vmgd"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		double dou_fraccion_basica_desgravada=getFraccionBasicaDesgravadaImpuestoRenta(ide_srimr);

		try {
			return dou_porcentaje_veces*dou_fraccion_basica_desgravada;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}

	public double getFraccionBasicaDesgravadaImpuestoRenta(String ide_srimr){
		TablaGenerica tab_det_sri_imp_ren=utilitario.consultar("select IDE_SRIMR," +
				"min(EXCESO_HASTA_SRDIR) as fraccion_basica_desgravada " +
				"from SRI_DETALLE_IMPUESTO_RENTA " +
				"where IDE_SRIMR="+ide_srimr+" GROUP BY IDE_SRIMR");
		try {
			return pckUtilidades.CConversion.CDbl_2(tab_det_sri_imp_ren.getValor("fraccion_basica_desgravada"));	
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}



	public TablaGenerica getTipoNominaContable(String ide_cnmoc){
		TablaGenerica tab_tip_nom=utilitario.consultar("select * from GTH_TIPO_EMPLEADO where IDE_GTTEM in ( " +
				"select IDE_GTTEM from NRH_DETALLE_TIPO_NOMINA where IDE_NRDTN in ( " +
				"select IDE_NRDTN from NRH_ROL where IDE_CNMOC="+ide_cnmoc+"))");
		return tab_tip_nom;
	}

	public String getSqlComboPeriodoRol(){
		String sql_combo_gepro="SELECT PER.IDE_GEPRO, " +
		"mes.detalle_gemes ||' '|| " + 
		"ani.detalle_geani ||' '|| " +
		"(case when TIR.DETALLE_NRTIT is null then '' else TIR.DETALLE_NRTIT end)||' '|| " + 
		"(case when PER.FECHA_INICIAL_GEPRO is null then '1900-01-01' else PER.FECHA_INICIAL_GEPRO END)||'  '|| " +
		"(case when PER.FECHA_FINAL_GEPRO is null then '1900-01-01' else PER.FECHA_FINAL_GEPRO end )||' '|| " +
		"(CASE WHEN PER.DETALLE_PERIODO_GEPRO IS NULL THEN '' ELSE PER.DETALLE_PERIODO_GEPRO END) as detalle_periodo " +  
		"FROM GEN_PERIDO_ROL PER " + 
		"INNER JOIN NRH_TIPO_ROL TIR ON TIR.IDE_NRTIT=PER.IDE_NRTIT " + 
		"INNER JOIN GEN_MES MES ON MES.IDE_GEMES=PER.IDE_GEMES " + 
		"INNER JOIN GEN_ANIO ANI ON ANI.IDE_GEANI=PER.IDE_GEANI " + 
		"WHERE ACTIVO_GEPRO=true AND ES_LIQUIDACION_GEPRO=FALSE ORDER BY ani.IDE_GEANI desc, mes.IDE_GEMES desc "; 


		//System.out.println("imprimiendo combo de periodos "+sql_combo_gepro);
		return sql_combo_gepro;
	}

	
	
	public String getSqlComboPeriodoRolLiquidacion(){
		String sql_combo_gepro="SELECT PER.IDE_GEPRO, " +
		"mes.detalle_gemes ||' '|| " + 
		"ani.detalle_geani ||' '|| " +
		"(case when TIR.DETALLE_NRTIT is null then '' else TIR.DETALLE_NRTIT end)||' '|| " + 
		"(case when PER.FECHA_INICIAL_GEPRO is null then '1900-01-01' else PER.FECHA_INICIAL_GEPRO END)||'  '|| " +
		"(case when PER.FECHA_FINAL_GEPRO is null then '1900-01-01' else PER.FECHA_FINAL_GEPRO end )||' '|| " +
		"(CASE WHEN PER.DETALLE_PERIODO_GEPRO IS NULL THEN '' ELSE PER.DETALLE_PERIODO_GEPRO END) as detalle_periodo " +  
		"FROM GEN_PERIDO_ROL PER " + 
		"INNER JOIN NRH_TIPO_ROL TIR ON TIR.IDE_NRTIT=PER.IDE_NRTIT " + 
		"INNER JOIN GEN_MES MES ON MES.IDE_GEMES=PER.IDE_GEMES " + 
		"INNER JOIN GEN_ANIO ANI ON ANI.IDE_GEANI=PER.IDE_GEANI " + 
		"WHERE ACTIVO_GEPRO=true AND ES_LIQUIDACION_GEPRO=TRUE ORDER BY ani.IDE_GEANI desc, mes.IDE_GEMES desc "; 


		//System.out.println("imprimiendo combo de periodos "+sql_combo_gepro);
		return sql_combo_gepro;
	}


	public String getSqlSeleccionTablaPeriodoRol(){
		String sql_combo_gepro="SELECT PER.IDE_GEPRO, " +
				"mes.detalle_gemes, ani.detalle_geani ," +
				"TIR.DETALLE_NRTIT ||' '|| CASE WHEN PER.DETALLE_PERIODO_GEPRO IS NULL THEN NULL ELSE PER.DETALLE_PERIODO_GEPRO END as DETALLE_NRTIT,  " +
				"case when (PER.FECHA_INICIAL_GEPRO) is null then NULL else (PER.FECHA_INICIAL_GEPRO) END as fecha_ini, " +
				"case when (PER.FECHA_FINAL_GEPRO) is null then NULL else (PER.FECHA_FINAL_GEPRO) end as fecha_fin  " +
				"FROM GEN_PERIDO_ROL PER " +
				"INNER JOIN NRH_TIPO_ROL TIR ON TIR.IDE_NRTIT=PER.IDE_NRTIT " +
				"INNER JOIN GEN_MES MES ON MES.IDE_GEMES=PER.IDE_GEMES " +
				"INNER JOIN GEN_ANIO ANI ON ANI.IDE_GEANI=PER.IDE_GEANI "+ 
				"WHERE ACTIVO_GEPRO=true AND PER.ES_LIQUIDACION_GEPRO=TRUE "+
				"ORDER BY mes.IDE_GEMES ASC";
		//System.out.println("SM sql sql_combo_gepro  "+sql_combo_gepro);
		return sql_combo_gepro;
	}
	
	

	
	public String getSqlSeleccionTablaPeriodoRolLiquidacion(){
		String sql_combo_gepro="SELECT PER.IDE_GEPRO, " +
				"mes.detalle_gemes, ani.detalle_geani ," +
				"TIR.DETALLE_NRTIT ||' '|| CASE WHEN PER.DETALLE_PERIODO_GEPRO IS NULL THEN NULL ELSE PER.DETALLE_PERIODO_GEPRO END as DETALLE_NRTIT,  " +
				"case when (PER.FECHA_INICIAL_GEPRO) is null then NULL else (PER.FECHA_INICIAL_GEPRO) END as fecha_ini, " +
				"case when (PER.FECHA_FINAL_GEPRO) is null then NULL else (PER.FECHA_FINAL_GEPRO) end as fecha_fin  " +
				"FROM GEN_PERIDO_ROL PER " +
				"INNER JOIN NRH_TIPO_ROL TIR ON TIR.IDE_NRTIT=PER.IDE_NRTIT " +
				"INNER JOIN GEN_MES MES ON MES.IDE_GEMES=PER.IDE_GEMES " +
				"INNER JOIN GEN_ANIO ANI ON ANI.IDE_GEANI=PER.IDE_GEANI WHERE ACTIVO_GEPRO=true "+
				"ORDER BY mes.IDE_GEMES ASC";
		//System.out.println("SM sql sql_combo_gepro  "+sql_combo_gepro);
		return sql_combo_gepro;
	}

	public TablaGenerica getTipoEmpleadoContable(String ide_cnmoc){
		TablaGenerica tab_tip_nom=utilitario.consultar("select * from NRH_TIPO_NOMINA where IDE_NRTIN in ( " +
				"select IDE_NRTIN from NRH_DETALLE_TIPO_NOMINA where IDE_NRDTN in ( " +
				"select IDE_NRDTN from NRH_ROL where IDE_CNMOC="+ide_cnmoc+"))");
		return tab_tip_nom;
	}


	int band=0;
	public boolean importarValoresRubro(List lis_importa,String ide_nrrol,String ide_nrdtn,String ide_nrder,String fecha_ini_gepro,String fecha_fin_gepro,List lis_importa1,int band){
		
		String str_sql=getSqlEmpleadosTipoNomina(ide_nrdtn,fecha_fin_gepro);

		utilitario.getConexion().agregarSql("update  NRH_AMORTIZACION set ACTIVO_NRAMO=false " +
				"where FECHA_VENCIMIENTO_NRAMO " +
				"BETWEEN to_date ('"+fecha_ini_gepro+"','yyyy-mm-dd') and to_date ('"+fecha_fin_gepro+"','yyyy-mm-dd') " +
				"and IDE_NRANI in (select ide_nrani from NRH_ANTICIPO_INTERES " +
				"where IDE_NRANT in (select ide_nrant from NRH_ANTICIPO " +
				"where IDE_GTEMP in (select emp.ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM " +
				"and EDP.IDE_SUCU=DTN.IDE_SUCU " +
				"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP " +
				"WHERE DTN.IDE_NRDTN IN ("+ide_nrdtn+"))))");

		//Recupera los empleados de la nomina

		TablaGenerica tab_emp_dep=utilitario.consultar(str_sql);
		//Recorre la tabla de empleados y compara con la lista obtenida del archivo xls
 
		cargarRubrosRolVacia();

	if (band==2) {
		for (int j = 0; j < tab_emp_dep.getTotalFilas(); j++) {	
			String str_documento=tab_emp_dep.getValor(j, "DOCUMENTO_IDENTIDAD_GTEMP");
			String str_valor=null;
			for (int k = 0; k < lis_importa1.size(); k++) {						
				//busco el valor
				if(str_documento.equalsIgnoreCase(((String[])lis_importa1.get(k))[0])){
					str_valor=((String[])lis_importa1.get(k))[1];
					lis_importa1.remove(k);
					break;
				}
			}			


			if(str_valor!=null){
				//Cargo el rol del empleado seleccionado
				utilitario.getConexion().agregarSql("UPDATE NRH_DETALLE_ROL set VALOR_NRDRO="+str_valor+" " +
						"where IDE_NRROL="+ide_nrrol+" " +
						"and IDE_NRDER="+ide_nrder+" " +
						"and IDE_GEEDP="+tab_emp_dep.getValor(j,"ide_geedp"));
				
				System.out.println("UPDATE NRH_DETALLE_ROL set VALOR_NRDRO="+str_valor+" " +
						"where IDE_NRROL="+ide_nrrol+" " +
						"and IDE_NRDER="+ide_nrder+" " +
						"and IDE_GEEDP="+tab_emp_dep.getValor(j,"ide_geedp"));
				
			}
		}

	
	}else{
		
		for (int j = 0; j < tab_emp_dep.getTotalFilas(); j++) {	
			String str_documento=tab_emp_dep.getValor(j, "DOCUMENTO_IDENTIDAD_GTEMP");
			String str_valor=null;
			for (int k = 0; k < lis_importa.size(); k++) {						
				//busco el valor
				if(str_documento.equalsIgnoreCase(((String[])lis_importa.get(k))[0])){
					str_valor=((String[])lis_importa.get(k))[1];
					lis_importa.remove(k);
					break;
				}
			}			


			if(str_valor!=null){
				//Cargo el rol del empleado seleccionado
				utilitario.getConexion().agregarSql("UPDATE NRH_DETALLE_ROL set VALOR_NRDRO="+str_valor+" " +
						"where IDE_NRROL="+ide_nrrol+" " +
						"and IDE_NRDER="+ide_nrder+" " +
						"and IDE_GEEDP="+tab_emp_dep.getValor(j,"ide_geedp"));
				
				System.out.println("UPDATE NRH_DETALLE_ROL set VALOR_NRDRO="+str_valor+" " +
						"where IDE_NRROL="+ide_nrrol+" " +
						"and IDE_NRDER="+ide_nrder+" " +
						"and IDE_GEEDP="+tab_emp_dep.getValor(j,"ide_geedp"));
				
			}
		}
		//band=0;
		
	}
		utilitario.getConexion().agregarSql("update NRH_ROL set ESTADO_CALCULADO_NRROL=0 where IDE_NRROL="+ide_nrrol);

		String str_msg=utilitario.getConexion().ejecutarListaSql();
		if (str_msg.isEmpty()){
			return true;
		}
		return false;
	}




	public boolean cerrarNomina(String ide_nrrol,String ide_nrdtn,String ide_gepro,String fecha_rol){
		// obtengo el tipo de nomina 
		String IDE_NRTIN="";
		try {
			IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");	
		} catch (Exception e) {
			// TODO: handle exception
		}
		TablaGenerica tab_per=getPeriodoRol(ide_gepro);
		String fecha_ini=tab_per.getValor("FECHA_INICIAL_GEPRO");
		String fecha_fin=tab_per.getValor("FECHA_FINAL_GEPRO");

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			// si el tipo de nomina es liquidacion actualizo el campo ejecutó liquidacion de la tabla gen empleado departamento par
			utilitario.getConexion().agregarSql("UPDATE GEN_EMPLEADOS_DEPARTAMENTO_PAR set EJECUTO_LIQUIDACION_GEEDP=1 " +
					"where IDE_GEEDP in (select IDE_GEEDP from NRH_DETALLE_ROL where IDE_NRROL="+ide_nrrol+")");
		}

		utilitario.getConexion().agregarSqlPantalla("update NRH_AMORTIZACION set ACTIVO_NRAMO=true, " +
				"fecha_cancelado_nramo=to_date('"+fecha_rol+"','yyyy-mm-dd') " +
				"where ide_nrrol in ("+ide_nrrol+") ");

		//LIQUIDAR ANTICIPOS awbecerra

		// actualiza la bandera NOMINA_ASDHE_, para saber que ya fue pasada y cerrada las horas extras
		utilitario.getConexion().agregarSqlPantalla("update ASI_DETALLE_HORAS_EXTRAS set NOMINA_ASDHE=true " +
				"where IDE_ASDHE in (select IDE_ASDHE from ASI_VALIDA_NOMINA where IDE_NRROL="+ide_nrrol+")");

		//		// actualizo el estado de la nomina a cerrada
		//		utilitario.getConexion().agregarSqlPantalla("update NRH_ROL set IDE_NRESR="+utilitario.getVariable("p_nrh_estado_nomina_cerrada")+" where IDE_NRROL="+ide_nrrol);


		String str_cierre=utilitario.getConexion().ejecutarListaSql();
		if (str_cierre.isEmpty()){
			System.out.println("cierre de nomina "+ide_nrrol);
			System.out.println("sql ejecutados "+str_cierre);
			return true;
		}
		return false;
	}

	/**
	 * @param ide_nrrol :ide del rol
	 * @return booleano, true  si el total a recibir de todos los empleados del rol recibido es mayor o igual a cero;
	 * 					 false si el total a recibir de al menos un empleado del rol enviado es menor a cero 
	 */
	public boolean isTotalRecibirPermitido(String ide_nrrol){
		TablaGenerica tab_det_rol=utilitario.consultar("select * from ( " +
				"select * from NRH_DETALLE_ROL " +
				"where IDE_NRROL="+ide_nrrol+" " +
				"and IDE_NRDER in (select IDE_NRDER " +
				"from NRH_DETALLE_RUBRO where IDE_NRRUB in ("+utilitario.getVariable("p_nrh_rubro_valor_recibir")+")))a " +
				"where a.valor_nrdro<0");
		if (tab_det_rol.getTotalFilas()>0){
			return false;
		}
		return true;
	}

	public TablaGenerica getTipoRol(String ide_nrtit){
		TablaGenerica tab_tip_rol= utilitario.consultar("select * from NRH_TIPO_ROL where IDE_NRTIT="+ide_nrtit);
		return tab_tip_rol;
	}


	/**Metodo que retorna la tabla rubros con los campos codigo y detalle de rubro (ordenados por orden de impresion)
	 * @param IDE_NRDTN : tipo de nomina : si envio null o vacio retorna todos los rubros ordenados; 
	 * @param IDE_NRRUB : rubros: si envio null o vacio retorna todos los rubros ordenados que pertenecen al tipo de nomina 
	 * @return TablaGenerica : 
	 */
	public TablaGenerica getRubrosOrden(String IDE_NRDTN,String IDE_NRRUB){
		String str_rub="";

		String sql="select IDE_NRDER,IDE_NRRUB,ORDEN_IMPRIME_NRDER from NRH_DETALLE_RUBRO " +
				"where imprime_nrder=true ";
		if (IDE_NRDTN!=null && !IDE_NRDTN.isEmpty()){
			sql+=" and IDE_NRDTN in ("+IDE_NRDTN+") ";

			if (IDE_NRRUB!=null && !IDE_NRRUB.isEmpty()){
				sql+=" and IDE_NRRUB in ( "+IDE_NRRUB+") ";
			}
			sql+=" ORDER BY IDE_NRRUB,ORDEN_IMPRIME_NRDER desc";
		}

		TablaGenerica tab_rubros1=utilitario.consultar(sql);


		String ide_nrder="";
		String ide_nrrub_ant="";

		for (int i = 0; i < tab_rubros1.getTotalFilas(); i++) {
			if (i==0){
				ide_nrrub_ant=tab_rubros1.getValor(0, "IDE_NRRUB");
				ide_nrder+=tab_rubros1.getValor(0, "IDE_NRDER")+",";
			}else{
				String ide_nrrub=tab_rubros1.getValor(i, "IDE_NRRUB");
				if (!ide_nrrub.equalsIgnoreCase(ide_nrrub_ant)){
					ide_nrder+=tab_rubros1.getValor(i, "IDE_NRDER")+",";
				}
				ide_nrrub_ant=tab_rubros1.getValor(i, "IDE_NRRUB");
			}

		}

		try {
			ide_nrder=ide_nrder.substring(0,ide_nrder.length()-1);
		} catch (Exception e) {
			// TODO: handle exception
		}

		TablaGenerica tab_rubros=utilitario.consultar("select DER.IDE_NRRUB,DETALLE_NRRUB from NRH_DETALLE_RUBRO DER " +
				"LEFT JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"WHERE DER.IDE_NRDER IN ("+ide_nrder+") " +
				"ORDER BY ORDEN_IMPRIME_NRDER");
		return tab_rubros;
	}


	public String getSqlNominaGlobalAreas(String IDE_NRDTN,String IDE_GEPRO,String IDE_NRRUB){
		String str_sql="SELECT SUCU.IDE_SUCU, sucu.nom_sucu as sucursal, area.ide_geare,area.detalle_geare as area, dep.ide_gedep,dep.detalle_gedep as departamento, DETALLE_NRRUB as rubros, rub.ide_nrrub, sum (DETA.VALOR_NRDRO) AS MONTO , " +
				"c.TOTAL_DEP, " +
				"e.total_emp_dep, " +
				"d.ide_gttco, " +
				"d.detalle_gttco, " +
				"d.tot_emp_tip_con " +
				"FROM NRH_DETALLE_ROL DETA " +
				"LEFT JOIN NRH_ROL ROL ON ROL.IDE_NRROL=DETA.IDE_NRROL " +
				"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN = DETATIPONO.IDE_NRDTN " +
				"LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP " +
				"LEFT JOIN GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO " +
				"INNER JOIN GTH_TIPO_EMPLEADO TIPOEMP ON TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM " +
				//				"AND TIPOEMP.IDE_GTTEM=DETATIPONO.IDE_GTTEM " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=PAR.IDE_SUCU " +
				"LEFT JOIN NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER " +
				"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=PAR.IDE_GEDEP LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR LEFT JOIN GEN_PERIDO_ROL PERIODO ON PERIODO.IDE_GEPRO=ROL.IDE_GEPRO LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI " +
				"left join ( " +
				"SELECT SUCU.IDE_SUCU,area.ide_geare,DEP.IDE_GEDEP, sum (DETA.VALOR_NRDRO) AS TOTAL_DEP FROM NRH_DETALLE_ROL DETA LEFT JOIN NRH_ROL ROL ON ROL.IDE_NRROL=DETA.IDE_NRROL LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN = DETATIPONO.IDE_NRDTN LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP LEFT JOIN GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO LEFT JOIN GTH_TIPO_EMPLEADO TIPOEMP ON TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=PAR.IDE_SUCU LEFT JOIN NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=PAR.IDE_GEDEP LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR LEFT JOIN GEN_PERIDO_ROL PERIODO ON PERIODO.IDE_GEPRO=ROL.IDE_GEPRO LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI " +
				"WHERE detatipono.ide_nrdtn IN ("+IDE_NRDTN+") " +
				"AND ROL.IDE_GEPRO IN ("+IDE_GEPRO+") " +
				"AND RUB.IDE_NRRUB IN ("+IDE_NRRUB+") GROUP BY SUCU.IDE_SUCU,area.ide_geare,DEP.IDE_GEDEP " +
				")c on c.ide_sucu=sucu.ide_sucu and area.ide_geare=c.ide_geare and dep.ide_gedep=c.ide_gedep " +
				"left join ( " +
				"SELECT a.ide_sucu,a.ide_geare,a.ide_gedep,a.ide_gttco,a.detalle_gttco,count(a.ide_geedp) as tot_emp_tip_con FROM ( " +
				"select EDP.IDE_SUCU,EDP.IDE_GEARE,EDP.IDE_GEDEP,TCO.IDE_GTTCO,TCO.DETALLE_GTTCO,DRO.IDE_GEEDP from NRH_DETALLE_ROL DRO INNER JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON DRO.IDE_GEEDP=EDP.IDE_GEEDP INNER JOIN GTH_TIPO_CONTRATO TCO ON TCO.IDE_GTTCO=EDP.IDE_GTTCO " +
				"WHERE DRO.IDE_NRROL IN (SELECT IDE_NRROL FROM NRH_ROL " +
				"WHERE IDE_NRDTN IN ("+IDE_NRDTN+") " +
				"AND IDE_GEPRO IN ("+IDE_GEPRO+")) " +
				"GROUP BY EDP.IDE_SUCU,EDP.IDE_GEARE,EDP.IDE_GEDEP,DRO.IDE_GEEDP,TCO.IDE_GTTCO,TCO.DETALLE_GTTCO " +
				") a " +
				"group by a.ide_sucu,a.ide_gttco,a.ide_geare,a.ide_gedep,a.detalle_gttco " +
				"order by a.ide_sucu,a.ide_geare,a.ide_gedep,a.detalle_gttco " +
				")d on d.ide_sucu=sucu.ide_sucu and area.ide_geare=d.ide_geare and dep.ide_gedep=d.ide_gedep " +
				"left join ( " +
				"SELECT a.ide_sucu,a.ide_geare,a.ide_gedep,count(a.ide_geedp) as total_emp_dep FROM ( " +
				"select EDP.IDE_SUCU,EDP.IDE_GEARE,EDP.IDE_GEDEP,TCO.IDE_GTTCO,TCO.DETALLE_GTTCO,DRO.IDE_GEEDP from NRH_DETALLE_ROL DRO INNER JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON DRO.IDE_GEEDP=EDP.IDE_GEEDP INNER JOIN GTH_TIPO_CONTRATO TCO ON TCO.IDE_GTTCO=EDP.IDE_GTTCO " +
				"WHERE DRO.IDE_NRROL IN (SELECT IDE_NRROL FROM NRH_ROL " +
				"WHERE IDE_NRDTN IN ("+IDE_NRDTN+") " +
				"AND IDE_GEPRO IN ("+IDE_GEPRO+")) " +
				"GROUP BY EDP.IDE_SUCU,EDP.IDE_GEARE,EDP.IDE_GEDEP,DRO.IDE_GEEDP,TCO.IDE_GTTCO,TCO.DETALLE_GTTCO " +
				") a " +
				"group by a.ide_sucu,a.ide_geare,a.ide_gedep " +
				"order by ide_sucu " +
				")e on e.ide_sucu=sucu.ide_sucu and area.ide_geare=e.ide_geare and dep.ide_gedep=e.ide_gedep " +
				"and d.ide_sucu=e.ide_sucu and e.ide_geare=d.ide_geare and e.ide_gedep=d.ide_gedep " +
				"WHERE detatipono.ide_nrdtn IN ("+IDE_NRDTN+") " +
				"AND ROL.IDE_GEPRO IN ("+IDE_GEPRO+") " +
				"AND RUB.IDE_NRRUB IN ("+IDE_NRRUB+") " +
				"GROUP BY SUCU.IDE_SUCU,sucu.nom_sucu, area.ide_geare,area.detalle_geare,dep.ide_gedep, dep.detalle_gedep ,  rub.ide_nrrub,DETALLE_NRRUB, " +

				"c.TOTAL_DEP,e.total_emp_dep,d.ide_gttco,d.detalle_gttco,d.tot_emp_tip_con " +
				"HAVING sum (DETA.VALOR_NRDRO)!=0 " +
				"order by sucursal DESC,area.detalle_geare,dep.detalle_gedep,detalle_nrrub,d.detalle_gttco "; 
		return str_sql;
	}

	//	public String getSqlNominaGlobalAreas1(String IDE_NRDTN,String IDE_GEPRO,String IDE_GEDEP,String DETALLE_GTTCO){
	//		String str_sql="SELECT SUCU.IDE_SUCU, sucu.nom_sucu as sucursal, area.detalle_geare as area, dep.ide_gedep,dep.detalle_gedep as departamento, " +
	//				" sum (DETA.VALOR_NRDRO) AS MONTO , " +
	//				"c.TOTAL_DEP,d.total_emp_dep,d.ide_gttco,d.detalle_gttco,d.tot_emp_tip_con " +
	//				"FROM NRH_DETALLE_ROL DETA " +
	//				"LEFT JOIN NRH_ROL ROL ON ROL.IDE_NRROL=DETA.IDE_NRROL LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN = DETATIPONO.IDE_NRDTN LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP LEFT JOIN GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO LEFT JOIN GTH_TIPO_EMPLEADO TIPOEMP ON TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=PAR.IDE_SUCU LEFT JOIN NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=PAR.IDE_GEDEP LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR LEFT JOIN GEN_PERIDO_ROL PERIODO ON PERIODO.IDE_GEPRO=ROL.IDE_GEPRO LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI " +
	//				"left join ( SELECT SUCU.IDE_SUCU,area.ide_geare,DEP.IDE_GEDEP, sum (DETA.VALOR_NRDRO) AS TOTAL_DEP FROM NRH_DETALLE_ROL DETA LEFT JOIN NRH_ROL ROL ON ROL.IDE_NRROL=DETA.IDE_NRROL LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN = DETATIPONO.IDE_NRDTN LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP LEFT JOIN GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO LEFT JOIN GTH_TIPO_EMPLEADO TIPOEMP ON TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=PAR.IDE_SUCU LEFT JOIN NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=PAR.IDE_GEDEP LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR LEFT JOIN GEN_PERIDO_ROL PERIODO ON PERIODO.IDE_GEPRO=ROL.IDE_GEPRO LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI " +
	//				"WHERE detatipono.ide_nrdtn IN ("+IDE_NRDTN+") " +
	//				"AND ROL.IDE_GEPRO IN ("+IDE_GEPRO+") " +
	//				"GROUP BY SUCU.IDE_SUCU,area.ide_geare,DEP.IDE_GEDEP " +
	//				")c on c.ide_sucu=sucu.ide_sucu and area.ide_geare=c.ide_geare and dep.ide_gedep=c.ide_gedep " +
	//				"left join ( select a.ide_gedep,a.total_emp_dep,b.ide_gttco,b.DETALLE_GTTCO,b.tot_emp_tip_con from ( " +
	//				"SELECT  A.IDE_GEDEP,COUNT(A.IDE_GEEDP) as TOTAL_EMP_DEP FROM ( " +
	//				"select EDP.IDE_SUCU,EDP.IDE_GEARE,EDP.IDE_GEDEP,DRO.IDE_GEEDP from NRH_DETALLE_ROL DRO " +
	//				"INNER JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON DRO.IDE_GEEDP=EDP.IDE_GEEDP " +
	//				"INNER JOIN GTH_TIPO_CONTRATO TCO ON TCO.IDE_GTTCO=EDP.IDE_GTTCO " +
	//				"WHERE DRO.IDE_NRROL IN (SELECT IDE_NRROL FROM NRH_ROL " +
	//				"WHERE IDE_NRDTN IN ("+IDE_NRDTN+") AND IDE_GEPRO IN ("+IDE_GEPRO+")) " +
	//				"GROUP BY EDP.IDE_SUCU,EDP.IDE_GEARE,EDP.IDE_GEDEP,DRO.IDE_GEEDP)A " +
	//				"GROUP BY A.IDE_GEDEP) a " +
	//				"left join ( " +
	//				"SELECT a.ide_gedep,a.ide_gttco,a.detalle_gttco,count(a.ide_geedp) as tot_emp_tip_con FROM ( " +
	//				"select EDP.IDE_SUCU,EDP.IDE_GEARE,EDP.IDE_GEDEP,TCO.IDE_GTTCO,TCO.DETALLE_GTTCO,DRO.IDE_GEEDP from NRH_DETALLE_ROL DRO " +
	//				"INNER JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON DRO.IDE_GEEDP=EDP.IDE_GEEDP " +
	//				"INNER JOIN GTH_TIPO_CONTRATO TCO ON TCO.IDE_GTTCO=EDP.IDE_GTTCO " +
	//				"WHERE DRO.IDE_NRROL IN (SELECT IDE_NRROL FROM NRH_ROL " +
	//				"WHERE IDE_NRDTN IN ("+IDE_NRDTN+") AND IDE_GEPRO IN ("+IDE_GEPRO+")) " +
	//				"GROUP BY EDP.IDE_SUCU,EDP.IDE_GEARE,EDP.IDE_GEDEP,DRO.IDE_GEEDP,TCO.IDE_GTTCO,TCO.DETALLE_GTTCO) a " +
	//				"group by a.ide_gttco,a.ide_gedep,a.detalle_gttco " +
	//				")b on a.ide_gedep=b.IDE_GEDEP " +
	//				"order by a.ide_gedep " +
	//				")d on dep.ide_gedep=d.ide_gedep " +
	//				"WHERE detatipono.ide_nrdtn IN ("+IDE_NRDTN+") " +
	//				"AND ROL.IDE_GEPRO IN ("+IDE_GEPRO+") " +
	//				"and dep.ide_gedep in ("+IDE_GEDEP+") " +
	//				"and d.detalle_gttco like ('"+DETALLE_GTTCO+"') "+
	//				"GROUP BY SUCU.IDE_SUCU,sucu.nom_sucu, area.detalle_geare,dep.ide_gedep, " +
	//				"dep.detalle_gedep , c.TOTAL_DEP,d.total_emp_dep, " +
	//				"d.ide_gttco,d.detalle_gttco,d.tot_emp_tip_con " +
	//				"HAVING sum (DETA.VALOR_NRDRO)>0 " +
	//				"order by sucursal DESC,area.detalle_geare,dep.detalle_gedep  ";
	//		return str_sql;
	//	}


	public String getSqlEmpleadosAgregarNomina(String ide_nrrol,String ide_nrdtn){


		String IDE_NRTIN="";
		try {
			IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");	
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (ide_nrdtn==null || ide_nrdtn.isEmpty()){
			ide_nrdtn="-1";
		}

		String sql="select ";

		sql+="a.IDE_GEEDP," +
				"a.DOCUMENTO_IDENTIDAD_GTEMP," +
				"a.NOMBRES, " +
				"a.IDE_SUCU, " +
				"a.IDE_GTEMP, " +
				"a.IDE_GTTEM, "+
				"a.FECHA_GEEDP, "+	
				"a.RMU_GEEDP, "+
				"a.AJUSTE_SUELDO_GEEDP, " +
				"a.FECHA_AJUSTE_GEEDP, "+
				"a.fecha_ingreso_grupo_gtemp, "+//awbecerra
				"a.FECHA_INGRESO_GTEMP, "+
				"a.ACUMULA_FONDOS_GEEDP, "+
				"a.IDE_GEREG, "+
				"a.FECHA_ENCARGO_GEEDP,a.SUELDO_SUBROGA_GEEDP,a.FECHA_ENCARGO_FIN_GEEDP,b.dias_vacacion,a.DISCAPACITADO_GTEMP  ";

		sql+="from " +
				"( ";
		sql+="select " +
				"EDP.IDE_GEEDP," +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP," +
				"EMP.APELLIDO_PATERNO_GTEMP ||' '|| " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) ||' '|| " +
				"EMP.PRIMER_NOMBRE_GTEMP ||' '|| " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES, " +
				"EDP.IDE_SUCU, " +
				"EMP.IDE_GTEMP, " +
				"EMP.DISCAPACITADO_GTEMP, "+
				"EDP.IDE_GTTEM, emp.fecha_ingreso_grupo_gtemp, ";

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="EDP.FECHA_LIQUIDACION_GEEDP AS FECHA_GEEDP, ";	
		}else{
			sql+="EDP.FECHA_GEEDP, ";
		}

		sql+="EDP.RMU_GEEDP, "+
				"EDP.AJUSTE_SUELDO_GEEDP, " +
				"EDP.FECHA_AJUSTE_GEEDP, "+
				"EMP.FECHA_INGRESO_GTEMP, "+
				"EDP.ACUMULA_FONDOS_GEEDP, "+
				"DIP.IDE_GEREG, "+
				"EDP.FECHA_ENCARGO_GEEDP,EDP.SUELDO_SUBROGA_GEEDP,EDP.FECHA_ENCARGO_FIN_GEEDP "+
				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM " +
				"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP "+
				"inner join SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU " +
				"LEFT JOIN GEN_DIVISION_POLITICA DIP ON DIP.IDE_GEDIP=SUC.IDE_GEDIP "+
				"WHERE DTN.IDE_NRDTN="+ide_nrdtn+" " +
				"AND EDP.ACTIVO_GEEDP=TRUE "+
				"AND EDP.IDE_GEEDP NOT IN (select IDE_GEEDP FROM NRH_DETALLE_ROL WHERE IDE_NRROL IN ( " +
				"select IDE_NRROL from NRH_ROL WHERE IDE_NRROL="+ide_nrrol+" ) " +
				"GROUP BY IDE_GEEDP) "+

				" ";

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="AND EDP.LIQUIDACION_GEEDP=1 "+
					"and (EDP.EJECUTO_LIQUIDACION_GEEDP IS NULL OR EDP.EJECUTO_LIQUIDACION_GEEDP!=1) ";

		}else{
			sql+="AND (EDP.LIQUIDACION_GEEDP IS NULL OR EDP.LIQUIDACION_GEEDP!=1) ";
		}

		sql+="ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC,EMP.APELLIDO_MATERNO_GTEMP ASC ";

		sql+=")a " +
				"left join " +
				"( " +
				"SELECT IDE_ASVAC,IDE_GTEMP,DIAS_VACACION FROM ( " +
				"SELECT a.IDE_ASVAC,edp.IDE_GTEMP, " +
				"(DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_VACACION " +
				"FROM ( " +
				"SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, " +
				"(case when SUM(DIA_ADICIONAL_ASDEV) is null then 0 else SUM(DIA_ADICIONAL_ASDEV) end) as NRO_DIAS_ADICIONAL, " +
				"(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO, " +
				"(case when SUM(DIA_SOLICITADO_ASDEV) is null then 0 else SUM(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO " +
				"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = TRUE GROUP BY IDE_ASVAC " +
				") a " +
				"inner join ASI_VACACION vac on VAC.IDE_ASVAC=a.IDE_ASVAC " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR edp on edp.ide_gtemp=vac.ide_gtemp  and edp.activo_geedp=TRUE and vac.ACTIVO_ASVAC=TRUE " +
				") a " +
				"GROUP BY IDE_ASVAC,IDE_GTEMP,DIAS_VACACION  " +
				")b  " +
				"on a.ide_gtemp=b.ide_gtemp ";

		System.out.println("empleados agregar nomina recibida "+ide_nrdtn+" sql empleados "+sql);
		return sql;
	}

	public String getSqlEmpleadosAgregarNominaLiquidacion(String ide_nrrol,String ide_nrdtn,String fecha_liquidacion){


		String IDE_NRTIN="";
		try {
			IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");	
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (ide_nrdtn==null || ide_nrdtn.isEmpty()){
			ide_nrdtn="-1";
		}

		String sql="select ";

		sql+="a.IDE_GEEDP," +
				"a.DOCUMENTO_IDENTIDAD_GTEMP," +
				"a.NOMBRES, " +
				"a.IDE_SUCU, " +
				"a.IDE_GTEMP, " +
				"a.IDE_GTTEM, "+
				"a.FECHA_GEEDP, "+	
				"a.RMU_GEEDP, "+
				"a.AJUSTE_SUELDO_GEEDP, " +
				"a.FECHA_AJUSTE_GEEDP, "+
				"a.fecha_ingreso_grupo_gtemp, "+//awbecerra
				"a.FECHA_INGRESO_GTEMP, "+
				"a.ACUMULA_FONDOS_GEEDP, "+
				"a.IDE_GEREG, "+
				"a.FECHA_ENCARGO_GEEDP,a.SUELDO_SUBROGA_GEEDP,a.FECHA_ENCARGO_FIN_GEEDP,b.dias_vacacion,a.DISCAPACITADO_GTEMP  ";

		sql+="from " +
				"( ";
		sql+="select " +
				"EDP.IDE_GEEDP," +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP," +
				"EMP.APELLIDO_PATERNO_GTEMP ||' '|| " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)||' '|| " +
				"EMP.PRIMER_NOMBRE_GTEMP ||' '|| " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end ) AS NOMBRES, " +
				"EDP.IDE_SUCU, " +
				"EMP.IDE_GTEMP, " +
				"EMP.DISCAPACITADO_GTEMP, "+
				"EDP.IDE_GTTEM, emp.fecha_ingreso_grupo_gtemp, ";

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="EDP.FECHA_LIQUIDACION_GEEDP AS FECHA_GEEDP, ";	
		}else{
			sql+="EDP.FECHA_GEEDP, ";
		}

		sql+="EDP.RMU_GEEDP, "+
				"EDP.AJUSTE_SUELDO_GEEDP, " +
				"EDP.FECHA_AJUSTE_GEEDP, "+
				"EMP.FECHA_INGRESO_GTEMP, "+
				"EDP.ACUMULA_FONDOS_GEEDP, "+
				"DIP.IDE_GEREG, "+
				"EDP.FECHA_ENCARGO_GEEDP,EDP.SUELDO_SUBROGA_GEEDP,EDP.FECHA_ENCARGO_FIN_GEEDP "+
				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM " +
				"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP "+
				"inner join SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU " +
				"LEFT JOIN GEN_DIVISION_POLITICA DIP ON DIP.IDE_GEDIP=SUC.IDE_GEDIP "+
				"WHERE DTN.IDE_NRDTN="+ide_nrdtn+" " +

				"AND EDP.IDE_GEEDP NOT IN (select IDE_GEEDP FROM NRH_DETALLE_ROL WHERE IDE_NRROL IN ( " +
				"select IDE_NRROL from NRH_ROL WHERE IDE_NRROL="+ide_nrrol+" ) " +
				"GROUP BY IDE_GEEDP) "+

				" ";

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="AND EDP.LIQUIDACION_GEEDP=1 "+
					"and (EDP.EJECUTO_LIQUIDACION_GEEDP IS NULL OR EDP.EJECUTO_LIQUIDACION_GEEDP!=1) " +
					"and EDP.FECHA_LIQUIDACION_GEEDP=to_date('"+fecha_liquidacion+"','yyyy-mm-dd') ";

		}else{
			sql+="AND (EDP.LIQUIDACION_GEEDP IS NULL OR EDP.LIQUIDACION_GEEDP!=1) ";
		}

		sql+="ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC,EMP.APELLIDO_MATERNO_GTEMP ASC ";

		sql+=")a " +
				"left join " +
				"( " +
				"SELECT IDE_ASVAC,IDE_GTEMP,DIAS_VACACION FROM ( " +
				"SELECT a.IDE_ASVAC,edp.IDE_GTEMP, " +
				"(DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_VACACION " +
				"FROM ( " +
				"SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, " +
				"(case when SUM(DIA_ADICIONAL_ASDEV) is null then 0 else SUM(DIA_ADICIONAL_ASDEV) end) as NRO_DIAS_ADICIONAL, " +
				"(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO, " +
				"(case when SUM(DIA_SOLICITADO_ASDEV) is null then 0 else SUM(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO " +
				"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = TRUE GROUP BY IDE_ASVAC " +
				") a " +
				"inner join ASI_VACACION vac on VAC.IDE_ASVAC=a.IDE_ASVAC " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR edp on edp.ide_gtemp=vac.ide_gtemp  " +
				"and vac.ACTIVO_ASVAC=TRUE " +
				") a " +
				"GROUP BY IDE_ASVAC,IDE_GTEMP,DIAS_VACACION  " +
				")b  " +
				"on a.ide_gtemp=b.ide_gtemp ";

		System.out.println("empleados agregar nomina LIQUIDACION recibida "+ide_nrdtn+" sql empleados "+sql);
		return sql;

	}

	public boolean eliminarEmpleadoRol(String ide_nrrol,String ide_geedp){
		if (utilitario.getConexion().ejecutarSql("DELETE FROM NRH_DETALLE_ROL WHERE IDE_NRROL IN ( " +
				"select IDE_NRROL from NRH_ROL WHERE IDE_NRROL="+ide_nrrol+" ) " +
				"AND IDE_GEEDP="+ide_geedp+"").isEmpty()){
			String IDE_NRTIN=getDetalleTipoNomina(getRol(ide_nrrol).getValor("IDE_NRDTN")).getValor("IDE_NRTIN");
			if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){

				utilitario.getConexion().ejecutarSql("UPDATE GEN_EMPLEADOS_DEPARTAMENTO_PAR " +
						"set EJECUTO_LIQUIDACION_GEEDP=0 " +
						"where IDE_GEEDP="+ide_geedp);

			}			
			return true;
		}
		return false;
	}


	public String getSqlConsultaValorRubroPeriodo(String IDE_GEEDP,String IDE_NRRUB,String fecha_ini,String fecha_fin){
		String str_sql="";



		if (IDE_GEEDP==null || IDE_GEEDP.isEmpty()){
			IDE_GEEDP="-1";
		}

		if (IDE_NRRUB==null || IDE_NRRUB.isEmpty()){
			IDE_NRRUB="-1";
		}
		String ide_gepro="";

		if (fecha_ini==null || fecha_ini.isEmpty()
				|| fecha_fin==null || fecha_fin.isEmpty()){
			ide_gepro="-1";
		}

		if (ide_gepro.isEmpty()){
			ide_gepro=getPeriodosRol(fecha_ini, fecha_fin);
			if (ide_gepro.isEmpty()){
				ide_gepro="-1";
			}
		}
		str_sql="select a.ide_nrrol, emp.documento_identidad_gtemp as identificacion, " +
				"emp.apellido_paterno_gtemp ||' '|| " +
				"(case when emp.apellido_materno_gtemp is null then '' else emp.apellido_materno_gtemp end) ||' '|| " +
				"emp.primer_nombre_gtemp ||' '|| " +
				"(case when emp.segundo_nombre_gtemp is null then '' else emp.segundo_nombre_gtemp end) as nombres, " +
				"a.tipo_nomina, "+
				"ani.detalle_geani ||' '||mes.detalle_gemes as periodo, " +
				"a.detalle_nrrub, " +
				"a.valor " +
				"from ( " +
				"select dro.ide_nrrol,detalle_nrtin||' '|| detalle_gttem as tipo_nomina,dro.ide_geedp,ide_gemes,ide_geani,detalle_nrrub,VALOR_NRDRO as valor " +
				"from NRH_DETALLE_ROL DRO " +
				"inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL " +
				"INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO " +
				"inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"inner join nrh_detalle_tipo_nomina dtn on dtn.ide_nrdtn=rol.ide_nrdtn " +
				"inner join nrh_tipo_nomina tin on tin.ide_nrtin=dtn.ide_nrtin " +
				"inner join gth_tipo_empleado tem on tem.ide_gttem=dtn.ide_gttem "+
				"where DRO.IDE_GEEDP " +
				"in ( select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in ( select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR " +
				"where IDE_GEEDP="+IDE_GEEDP+") ) " +
				"AND RUB.IDE_NRRUB in ("+IDE_NRRUB+") " +
				"and PRO.IDE_GEPRO IN ("+ide_gepro+") " +
				"order by  ide_geani asc,ide_gemes asc " +
				")a " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR edp on edp.ide_geedp=a.ide_geedp " +
				"inner join gth_empleado emp on emp.ide_gtemp=edp.ide_gtemp " +
				"inner join gen_mes mes on mes.ide_gemes=a.ide_gemes " +
				"inner join gen_anio ani on ani.ide_geani=a.ide_geani " +
				"order by nombres,ani.ide_geani,mes.ide_gemes "; 


		return str_sql;
	}

	public String getSqlConsultaValorRubroPeriodo(String IDE_NRRUB,String fecha_ini,String fecha_fin){
		String str_sql="";


		if (IDE_NRRUB==null || IDE_NRRUB.isEmpty()){
			IDE_NRRUB="-1";
		}


		String ide_gepro="";

		if (fecha_ini==null || fecha_ini.isEmpty()
				|| fecha_fin==null || fecha_fin.isEmpty()){
			ide_gepro="-1";
		}

		if (ide_gepro.isEmpty()){
			ide_gepro=getPeriodosRol(fecha_ini, fecha_fin);
			if (ide_gepro.isEmpty()){
				ide_gepro="-1";
			}
		}
		str_sql="select a.ide_nrrol, emp.documento_identidad_gtemp as identificacion," +
				"emp.apellido_paterno_gtemp ||' '|| " +
				"(case when emp.apellido_materno_gtemp is null then '' else emp.apellido_materno_gtemp end) ||' '|| " +
				"emp.primer_nombre_gtemp ||' '|| " +
				"(case when emp.segundo_nombre_gtemp is null then '' else emp.segundo_nombre_gtemp end) as nombres, " +
				"a.tipo_nomina, "+
				"ani.detalle_geani ||' '||mes.detalle_gemes as periodo, " +
				"a.detalle_nrrub, " +
				"a.valor " +
				"from ( " +
				"select dro.ide_nrrol,detalle_nrtin||' '|| detalle_gttem as tipo_nomina,dro.ide_geedp,ide_gemes,ide_geani,detalle_nrrub,VALOR_NRDRO as valor " +
				"from NRH_DETALLE_ROL DRO " +
				"inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL " +
				"INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO " +
				"inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"inner join nrh_detalle_tipo_nomina dtn on dtn.ide_nrdtn=rol.ide_nrdtn " +
				"inner join nrh_tipo_nomina tin on tin.ide_nrtin=dtn.ide_nrtin " +
				"inner join gth_tipo_empleado tem on tem.ide_gttem=dtn.ide_gttem "+
				"where RUB.IDE_NRRUB in ("+IDE_NRRUB+") " +
				"and PRO.IDE_GEPRO IN ("+ide_gepro+") " +
				"order by  ide_geani asc,ide_gemes asc " +
				")a " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR edp on edp.ide_geedp=a.ide_geedp " +
				"inner join gth_empleado emp on emp.ide_gtemp=edp.ide_gtemp " +
				"inner join gen_mes mes on mes.ide_gemes=a.ide_gemes " +
				"inner join gen_anio ani on ani.ide_geani=a.ide_geani " +
				"order by nombres,ani.ide_geani,mes.ide_gemes "; 


		return str_sql;
	}

	public double getSumatoriaRubro(String IDE_GEEDP,String IDE_NRDER,String fecha_ini,String fecha_fin){
		double dou_sumatoria=0;
		System.out.println("getSumatoriaRubro IDE_GEEDP: "+IDE_GEEDP);
		if (fecha_ini!=null && !fecha_ini.isEmpty()
				&& fecha_fin!=null && !fecha_fin.isEmpty()
				&& IDE_GEEDP!=null && !IDE_GEEDP.isEmpty()
				&& IDE_NRDER!=null && !IDE_NRDER.isEmpty()){

			TablaGenerica tab_det_rub=new TablaGenerica();
			String str_sql="";

			//			System.out.println("fecha ini "+fecha_ini);
			//			System.out.println("fecha fin "+fecha_fin);
			String ide_gepro=getPeriodosRol(fecha_ini, fecha_fin);

			if (ide_gepro!=null){
				try{
					if (ide_gepro.isEmpty()){
						ide_gepro="-1";
					}

					str_sql="select '0' as ide,SUM(VALOR_NRDRO) as sumatoria_rubro from NRH_DETALLE_ROL DRO " +
							"inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL " +
							"INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO " +
							"inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER " +
							"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
							"inner join nrh_detalle_tipo_nomina dtn on dtn.ide_nrdtn=rol.ide_nrdtn and dtn.ide_nrtin="+utilitario.getVariable("p_nrh_tipo_nomina_normal")+" "+
							"where DRO.IDE_GEEDP in ( " +
							"select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in ( " +
							"select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+IDE_GEEDP+") " +
							") " +
							"AND RUB.IDE_NRRUB in (select IDE_NRRUB from NRH_DETALLE_RUBRO where IDE_NRDER="+IDE_NRDER+") " +
							"and PRO.IDE_GEPRO IN ("+ide_gepro+") ";
					System.out.println("getSumatoriaRubro str_sql: "+str_sql);

					tab_det_rub=utilitario.consultar(str_sql);
					System.out.println("getSumatoriaRubro tab_det_rub filas: "+tab_det_rub.getTotalFilas());

					//					if (IDE_GEEDP.equalsIgnoreCase("458")){
					//					System.out.println("sql sumatoria "+tab_det_rub.getSql());	
					//					}
					//					
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("error al ejecutar sql "+str_sql);
				}


				if (tab_det_rub.getTotalFilas()>0){
					if (tab_det_rub.getValor("sumatoria_rubro")!=null && !tab_det_rub.getValor("sumatoria_rubro").isEmpty()){
						try {
							return pckUtilidades.CConversion.CDbl_2(tab_det_rub.getValor("sumatoria_rubro"));	
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			}
		}
		return dou_sumatoria;
	}

	public boolean validarFechas(String fecha_inicial,String fecha_final){
		int mes_ini=0;
		int mes_fin=0;
		int anio_ini=0;
		int anio_fin=0;
		try {
			mes_ini=utilitario.getMes(fecha_inicial);
			mes_fin=utilitario.getMes(fecha_final);
			anio_ini=utilitario.getAnio(fecha_inicial);
			anio_fin=utilitario.getAnio(fecha_final);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (mes_ini!=0 && mes_fin!=0 && anio_ini!=0 && anio_fin!=0){
			if (anio_fin>=anio_ini){
				if (anio_ini==anio_fin){
					if (mes_fin>=mes_ini){
						return true;
					}
				}else{
					return true;
				}

			}
		}

		return false;
	}

	public String getPeriodosRol(String fecha_inicial,String fecha_final){

		int mes_ini=0;
		int mes_fin=0;
		int anio_ini=0;
		int anio_fin=0;
		try {
			mes_ini=utilitario.getMes(fecha_inicial);
			mes_fin=utilitario.getMes(fecha_final);
			anio_ini=utilitario.getAnio(fecha_inicial);
			anio_fin=utilitario.getAnio(fecha_final);
		} catch (Exception e) {
			// TODO: handle exception
		}
		int ide_gemes_fin_aux=mes_fin;
		int ide_gemes_ini_aux=1;
		int ide_geani_aux=anio_fin;
		if (validarFechas(fecha_inicial, fecha_final)){
			int diferencia_anios=anio_fin-anio_ini;
			TablaGenerica tab_gen_per=new TablaGenerica();
			String str_ide_gepro="";
			if (diferencia_anios==0){
				str_ide_gepro="";
				tab_gen_per=utilitario.consultar("select * from GEN_PERIDO_ROL " +
						"where ((IDE_GEMES BETWEEN "+mes_ini+" and "+mes_fin+") " +
						"and IDE_GEANI=(SELECT IDE_GEANI FROM GEN_ANIO WHERE lower (DETALLE_GEANI) like '"+anio_ini+"'))");
				tab_gen_per.imprimirSql();
				for (int j = 0; j < tab_gen_per.getTotalFilas(); j++) {
					str_ide_gepro+=tab_gen_per.getValor(j, "IDE_GEPRO")+",";
				}
			}else{
				str_ide_gepro="";
				for (int i = 0; i <= diferencia_anios; i++) {
					if (i==0){
						ide_gemes_ini_aux=mes_ini;
					}
					if (diferencia_anios==i){
						ide_gemes_fin_aux=mes_fin;
						ide_geani_aux=anio_fin;
						ide_gemes_ini_aux=1;
					}else{
						if (i==0){
							ide_geani_aux=ide_geani_aux-diferencia_anios;
						}else if (i>0){
							ide_gemes_ini_aux=1;
							ide_geani_aux+=1;
						}
						ide_gemes_fin_aux=12;
					}

					tab_gen_per=utilitario.consultar("select * from GEN_PERIDO_ROL " +
							"where ((IDE_GEMES BETWEEN "+ide_gemes_ini_aux+" and "+ide_gemes_fin_aux+") " +
							"and IDE_GEANI=(SELECT IDE_GEANI FROM GEN_ANIO WHERE lower (DETALLE_GEANI) like '"+ide_geani_aux+"'))");
					tab_gen_per.imprimirSql();
					for (int j = 0; j < tab_gen_per.getTotalFilas(); j++) {
						str_ide_gepro+=tab_gen_per.getValor(j, "IDE_GEPRO")+",";
					}
				}
			}
			
			
			if (!str_ide_gepro.isEmpty()){
				str_ide_gepro=str_ide_gepro.substring(0, str_ide_gepro.length()-1);
			}
//			System.out.println("fecha ini "+fecha_inicial+" fecha fin "+fecha_final+" ide_gepro resultado "+str_ide_gepro);
			return str_ide_gepro;
		}

		return null;
	}

	public double getSumatoriaRubro1(String IDE_GEEDP,String IDE_NRRUB,String fecha_ini,String fecha_fin){
		double dou_sumatoria=0;
		if (fecha_ini!=null && !fecha_ini.isEmpty()
				&& fecha_fin!=null && !fecha_fin.isEmpty()
				&& IDE_GEEDP!=null && !IDE_GEEDP.isEmpty()
				&& IDE_NRRUB!=null && !IDE_NRRUB.isEmpty()){

			String str_sql="";


			String ide_gepro=getPeriodosRol(fecha_ini, fecha_fin);

			if (ide_gepro!=null){
				try{
					str_sql="select ide_geedp,SUM(VALOR_NRDRO) as sumatoria_rubro from NRH_DETALLE_ROL DRO " +
							"inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL " +
							"INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO " +
							"inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER " +
							"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
							"where DRO.IDE_GEEDP in ( " +
							"select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in ( " +
							"select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+IDE_GEEDP+") and ide_gttem !=3 " +
							") " +
							"AND RUB.IDE_NRRUB in ("+IDE_NRRUB+") " +
							"and PRO.IDE_GEPRO IN ("+ide_gepro+") " +
							"group by ide_geedp";

					//System.out.println("QUERY luis "+str_sql);
					//System.out.println("QUERY luis IDE_NRRUB "+IDE_NRRUB);

					TablaGenerica tab_det_rub2=utilitario.consultar(str_sql);
					if (tab_det_rub2.getTotalFilas()>0)
					{
						if(IDE_NRRUB.equals("42") || IDE_NRRUB.equals("44")
								|| IDE_NRRUB.equals(utilitario.getVariable("p_nrh_rubro_proviciones_d4")) 
								|| IDE_NRRUB.equals(utilitario.getVariable("p_nrh_rubro_proviciones_d3"))
								|| IDE_NRRUB.equals(utilitario.getVariable("p_nrh_rubro_dias_trabajados"))
								|| IDE_NRRUB.equals(utilitario.getVariable("p_nrh_rubro_decimo_cuarto_rol"))
								|| IDE_NRRUB.equals(utilitario.getVariable("p_nrh_rubro_decimo_tercer_rol")))//corrigiendo problema del calculo del impuesto a la renta, D3, D4 por cambios de contratos
						{
							for (int i = 0; i < tab_det_rub2.getTotalFilas(); i++) 
							{
								if (tab_det_rub2.getValor(i,"sumatoria_rubro")!=null && !tab_det_rub2.getValor(i,"sumatoria_rubro").isEmpty()){
									try {
										dou_sumatoria+=pckUtilidades.CConversion.CDbl_2(tab_det_rub2.getValor(i,"sumatoria_rubro"));
									} catch (Exception e) {
										// TODO: handle exception
									}
								}
							}
						}
						else if (tab_det_rub2.getValor("sumatoria_rubro")!=null && !tab_det_rub2.getValor("sumatoria_rubro").isEmpty())
						{
							try {
									//System.out.println("QUERY luis return "+pckUtilidades.CConversion.CDbl_2(tab_det_rub2.getValor("sumatoria_rubro")));
								return pckUtilidades.CConversion.CDbl_2(tab_det_rub2.getValor("sumatoria_rubro"));	
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}
					

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("error al ejecutar sql "+str_sql);
				}
			}
		}

		//System.out.println("QUERY luis dou_sumatoria "+dou_sumatoria);

		return dou_sumatoria;
	}

	
	
	public double getSumatoriaRubro1Liquidacion(String IDE_GEEDP,String IDE_NRRUB,String fecha_ini,String fecha_fin, int tipoDecimo){

		double dou_sumatoria=0;
		if (fecha_ini!=null && !fecha_ini.isEmpty()
				&& fecha_fin!=null && !fecha_fin.isEmpty()
				&& IDE_GEEDP!=null && !IDE_GEEDP.isEmpty()
				&& IDE_NRRUB!=null && !IDE_NRRUB.isEmpty()){

			String str_sql="";

			TablaGenerica tab_empleado=utilitario.consultar("select ide_geedp, ide_gtemp, ide_gepgc, ide_gegro, ide_gecaf, ide_sucu, "
					+ "ide_gedep, ide_geare, ide_gttem, ide_gttco, ide_gttsi, ide_gtgre,  "
					+ "gen_ide_gegro, gen_ide_gecaf, ide_getiv, ide_gecae, ide_geded,  "
					+ "fecha_geedp, fecha_finctr_geedp, rmu_geedp, ajuste_sueldo_geedp, "
					+ "fecha_encargo_geedp, fecha_ajuste_geedp, fecha_liquidacion_geedp, "
					+ "liquidacion_geedp, fecha_encargo_fin_geedp, sueldo_subroga_geedp, "
					+ "ejecuto_liquidacion_geedp, observacion_geedp, usuario_ingre, "
					+ "fecha_ingre, usuario_actua, fecha_actua, hora_ingre, hora_actua,  "
					+ "activo_geedp, linea_supervicion_geedp, acumula_fondos_geedp,  "
					+ "       control_asistencia_geedp, encargado_subrogado_geedp, valor_liquidacion_geedp "
					+ "from gen_empleados_departamento_par  "
					+ "where ide_geedp="+IDE_GEEDP+"");
			
			
			
			TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_empleado.getValor("ide_gtemp")+") "
					+ "order by ide_geedp asc");
			String ide_geedp_empleado="";
			//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
			//variable guarda la fecha de salida
			String fecha_finTemp="";
			fecha_finTemp=tab_empleado.getValor("fecha_finctr_geedp");
			//variable cuenta el numero de acciones
			int contAccionInvalida=0,contForAccionInvalida=0;
			StringBuilder str_ide_geedp = new StringBuilder();

			for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
				if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(IDE_GEEDP)==true) {
					String valorUltimoElemento="";
					valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
					if(valorUltimoElemento.equals(",")==true){
						str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
					}else {
					}

				i=tabEmpleados.getTotalFilas();
				}else{
						//Voy anidando los ides de la accion
						str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
		               // valor++;
		                if (tabEmpleados.getTotalFilas()==1) {
		   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
		   				contForAccionInvalida++;
		   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
		   					str_ide_geedp.append(",");
		                   // System.out.println("str_ide:  "+str_ide_empleado);
		   					}
		    			}	 	
				}
			}
			
			
			//Si no contiene acciones de personal luego de la liquidacion
			if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
				str_ide_geedp.append(IDE_GEEDP);
			}
			
			
			
			
			
			String fecha_iniTemp="";
					//retornaAccionpersonal(tab_empleado.getValor("IDE_GTEMP"), "23");
			//if (fecha_iniTemp==null || fecha_iniTemp.equals("") || fecha_iniTemp.isEmpty()) {
				String a="";
				a=utilitario.getVariable("p_nrh_acciones_personal");
				String[] listaAccionesEmpleado;
				listaAccionesEmpleado=a.split(",");
				for (int i = 0; i < listaAccionesEmpleado.length; i++) {
					fecha_iniTemp=retornaAccionpersonal(tab_empleado.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
					if (!fecha_iniTemp.equals("")) {
						i=listaAccionesEmpleado.length;
					}
				}
				
			//}
			
	
				
			String fecha_comaracion=utilitario.getAnio(fecha_ini)+"-01-01";

			String fecha_comparacionCuarto=(utilitario.getAnio(fecha_ini)-1)+"-08-01";
			String fecha_comparacionCuartoRango=utilitario.getAnio(fecha_ini)+"-08-01";
			String fecha_calculoFinInicio=utilitario.getAnio(fecha_ini)+"-"+(utilitario.getMes(fecha_fin)-1)+"-01";
			String mes=utilitario.getUltimoDiaMesFecha(fecha_calculoFinInicio);
			String fecha=utilitario.getAnio(fecha_finTemp)+"-"+(utilitario.getMes(fecha_fin)-1)+"-"+utilitario.getDia(mes);
			
			fecha_fin=fecha;
			System.out.println("DECIMOS AJUSTE");
			System.out.println("FECHA_INICIO"+fecha_iniTemp);
			System.out.println("FECHA_FIN"+fecha_finTemp);
			if (tipoDecimo==3) {
				if (fecha_iniTemp.compareTo(fecha_comaracion)<=0) {
					fecha_ini=fecha_comaracion;
					fecha_fin=utilitario.getAnio(fecha_ini)+"-"+(utilitario.getMes(fecha_finTemp))+"-"+utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp));

			}else {
					fecha_ini=fecha_iniTemp;
					//fecha_ini=fecha_finTemp;
					fecha_fin=utilitario.getAnio(fecha_finTemp)+"-"+(utilitario.getMes(fecha_finTemp))+"-"+utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp));

				}		
			}else if(tipoDecimo==4){
				System.out.println("DECIMO CUARTO");
				int mesTemp=0;
				if ((utilitario.getMes(fecha_finTemp)<=8)){
					fecha_ini=(utilitario.getAnio(fecha_finTemp)-1)+"-08-01";
					fecha_fin=utilitario.getAnio(fecha_finTemp)+"-"+(utilitario.getMes(fecha_finTemp))+"-"+utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp));
				}else if((utilitario.getMes(fecha_finTemp)>8)){
					fecha_ini=utilitario.getAnio(fecha_finTemp)+"-08-01";
					//fecha_fin=fecha_finTemp;
					fecha_fin=utilitario.getAnio(fecha_finTemp)+"-"+(utilitario.getMes(fecha_finTemp))+"-"+utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp));

				}				
				System.out.println("Fecha Ini"+fecha_ini);
			}
		
			
			
			String ide_gepro=getPeriodosRol(fecha_ini, fecha_fin);
			double valor_prov=0.0,rmuAnterior=0.0,rmuActual=0.0,valorAnteriorDecimo=0.0,valorActualDecimo=0.0,valorNuevo=0.0;
			
			rmuActual=retornaValorRubro("276");
			valorActualDecimo=rmuActual/12;
			rmuAnterior=retornaValorRubro("284");
			valorAnteriorDecimo=rmuAnterior/12;
			if (ide_gepro!=null){
				try{
					str_sql="select ide_geedp,SUM(VALOR_NRDRO) as sumatoria_rubro from NRH_DETALLE_ROL DRO " +
							"inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL " +
							"INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO " +
							"inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER " +
							"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
							"where DRO.IDE_GEEDP in ( " +
							"select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in ( " +
							"select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+IDE_GEEDP+") and ide_gttem !=3 " +
							") " +
							"AND RUB.IDE_NRRUB in ("+IDE_NRRUB+") " +
							"and PRO.IDE_GEPRO IN ("+ide_gepro+") AND DER.IDE_NRDTN IN(4,2) " +
							"group by ide_geedp";

					//System.out.println("QUERY luis "+str_sql);
					TablaGenerica tab_det_rub2=utilitario.consultar(str_sql);
					if (tab_det_rub2.getTotalFilas()>0)
					{

							for (int i = 0; i < tab_det_rub2.getTotalFilas(); i++) 
							{
								if (tab_det_rub2.getValor(i,"sumatoria_rubro")!=null && !tab_det_rub2.getValor(i,"sumatoria_rubro").isEmpty()){
									try {
										dou_sumatoria+=pckUtilidades.CConversion.CDbl_2(tab_det_rub2.getValor(i,"sumatoria_rubro"));
									} catch (Exception e) {
										// TODO: handle exception
									}
								}
							}
						
					}
					

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("error al ejecutar sql "+str_sql);
				}
			}
		}

		//System.out.println("QUERY luis dou_sumatoria "+dou_sumatoria);

		return dou_sumatoria;
	}

	
	

	public TablaGenerica getDetalleRol(String IDE_NRROL,String  IDE_GEEDP,String IDE_NRRUB){
		TablaGenerica tab_det_rol=utilitario.consultar("select * from NRH_DETALLE_ROL " +
				"where IDE_NRROL="+IDE_NRROL+" " +
				"AND IDE_GEEDP="+IDE_GEEDP+" " +
				"and IDE_NRDER in (select IDE_NRDER from NRH_DETALLE_RUBRO where IDE_NRRUB in ("+IDE_NRRUB+"))");
		return tab_det_rol;
	}


	public TablaGenerica getDetalleRol(String IDE_NRROL){
		TablaGenerica tab_det_rol=utilitario.consultar("select IDE_NRDRO,DIP.ide_gereg,IDE_NRROL,dro.IDE_GEEDP,DRO.IDE_NRDER,VALOR_NRDRO," +
				"DER.IDE_NRRUB,ORDEN_NRDER,RUB.IDE_NRFOC,DER.FORMULA_NRDER,DER.FECHA_INICIAL_NRDER,DER.FECHA_FINAL_NRDER,DER.FECHA_PAGO_NRDER,decimo_nrrub, " +
				"EMP.APELLIDO_PATERNO_GTEMP ||' '||(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) ||' '|| EMP.PRIMER_NOMBRE_GTEMP ||' '|| (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES "+
				"from NRH_DETALLE_ROL DRO " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR edp on EDP.ide_geedp=DRO.IDE_GEEDP " +
				"inner join GTH_EMPLEADO emp on emp.ide_gtemp=edp.ide_gtemp "+
				"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
				"inner join SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU " +
				"LEFT JOIN GEN_DIVISION_POLITICA DIP ON DIP.IDE_GEDIP=SUC.IDE_GEDIP "+
				"where DER.ACTIVO_NRDER=true and DRO.IDE_NRROL="+IDE_NRROL+"  "+
				"order by NOMBRES,der.ORDEN_NRDER desc ");
		return tab_det_rol;
	}

	public TablaGenerica getRubrosFaltantes(String IDE_NRROL){

		String ide_nrdtn=getRol(IDE_NRROL).getValor("IDE_NRDTN");
		String sql="select " +
				"NULL ide_nrdro, " +
				""+IDE_NRROL+" ide_nrrol, " +
				"null ide_geedp, " +
				"DER.IDE_NRDER, " +
				"0 valor_nrdro, " +
				"FOC.IDE_NRFOC, " +
				"RUB.IDE_NRRUB, " +
				"DER.FORMULA_NRDER, " +
				"DER.FECHA_INICIAL_NRDER, " +
				"DER.FECHA_FINAL_NRDER, " +
				"DER.FECHA_PAGO_NRDER, " +
				"decimo_nrrub, " +
				"der.ORDEN_NRDER " +
				"from NRH_DETALLE_RUBRO der " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
				"where DER.IDE_NRDER not in " +
				"( " +
				"select a.IDE_NRDER from ( "+
				"select IDE_NRDRO,IDE_NRROL,dro.IDE_GEEDP,DRO.IDE_NRDER,VALOR_NRDRO," +
				"DER.IDE_NRRUB,RUB.IDE_NRFOC,DER.FORMULA_NRDER,DER.FECHA_INICIAL_NRDER,DER.FECHA_FINAL_NRDER,DER.FECHA_PAGO_NRDER " +
				"from NRH_DETALLE_ROL DRO " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR edp on EDP.ide_geedp=DRO.IDE_GEEDP " +
				"inner join GTH_EMPLEADO emp on emp.ide_gtemp=edp.ide_gtemp "+
				"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
				"where DER.ACTIVO_NRDER=true and DRO.IDE_NRROL="+IDE_NRROL+"  "+
				"order by EMP.APELLIDO_PATERNO_GTEMP ASC,EMP.APELLIDO_MATERNO_GTEMP ASC,der.ORDEN_NRDER DESC "+
				")a " +
				") " +
				"and ACTIVO_NRDER=true " +
				"and ide_nrdtn="+ide_nrdtn;


		TablaGenerica tab_det_rol=utilitario.consultar(sql);
		return tab_det_rol;
	}



	//	public TablaGenerica getRubrosRol(String IDE_NRROL,String  IDE_GEEDP){
	//		
	//		String ide_nrdtn=getRol(IDE_NRROL).getValor("IDE_NRDTN");
	//		String sql="select * from " +
	//				"( " +
	//				"select " +
	//				"NULL ide_nrdro, " +
	//				""+IDE_NRROL+" ide_nrrol, " +
	//				""+IDE_GEEDP+" ide_geedp, " +
	//				"DER.IDE_NRDER, " +
	//				"0 valor_nrdro, " +
	//				"FOC.IDE_NRFOC, " +
	//				"RUB.IDE_NRRUB, " +
	//				"DER.FORMULA_NRDER, " +
	//				"DER.FECHA_INICIAL_NRDER, " +
	//				"DER.FECHA_FINAL_NRDER, " +
	//				"DER.FECHA_PAGO_NRDER, " +
	//				"der.ORDEN_NRDER " +
	//				"from NRH_DETALLE_RUBRO der " +
	//				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
	//				"INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
	//				"where DER.IDE_NRDER not in " +
	//				"( " +
	//				"select a.IDE_NRDER from ( "+
	//				"select IDE_NRDRO,IDE_NRROL,IDE_GEEDP,DRO.IDE_NRDER,VALOR_NRDRO," +
	//				"DER.IDE_NRRUB,RUB.IDE_NRFOC,DER.FORMULA_NRDER,DER.FECHA_INICIAL_NRDER,DER.FECHA_FINAL_NRDER,DER.FECHA_PAGO_NRDER " +
	//				"from NRH_DETALLE_ROL DRO " +
	//				"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER " +
	//				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
	//				"INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
	//				"where DER.ACTIVO_NRDER=1 and DRO.IDE_NRROL="+IDE_NRROL+" and DRO.IDE_GEEDP="+IDE_GEEDP+" "+
	//				")a " +
	//				") " +
	//				"and ACTIVO_NRDER=1 " +
	//				"and ide_nrdtn="+ide_nrdtn+" " +
	//				"UNION "+	
	//				"select IDE_NRDRO,IDE_NRROL,IDE_GEEDP,DRO.IDE_NRDER,VALOR_NRDRO," +
	//				"DER.IDE_NRRUB,RUB.IDE_NRFOC,DER.FORMULA_NRDER,DER.FECHA_INICIAL_NRDER,DER.FECHA_FINAL_NRDER,DER.FECHA_PAGO_NRDER,DER.ORDEN_NRDER  " +
	//				"from NRH_DETALLE_ROL DRO " +
	//				"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER " +
	//				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
	//				"INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
	//				"where DER.ACTIVO_NRDER=1 and DRO.IDE_NRROL="+IDE_NRROL+" and DRO.IDE_GEEDP="+IDE_GEEDP+" " +
	//				")a " +
	//				"ORDER BY a.ORDEN_NRDER DESC";
	//		
	//		
	//		TablaGenerica tab_det_rol=utilitario.consultar(sql);
	//		return tab_det_rol;
	//	}
	//

	public TablaGenerica getRubrosFaltantes(String IDE_NRROL,String  IDE_GEEDP){

		String ide_nrdtn=getRol(IDE_NRROL).getValor("IDE_NRDTN");
		String sql="select " +
				"NULL ide_nrdro, " +
				""+IDE_NRROL+" ide_nrrol, " +
				""+IDE_GEEDP+" ide_geedp, " +
				"DER.IDE_NRDER, " +
				"0 valor_nrdro, " +
				"FOC.IDE_NRFOC, " +
				"RUB.IDE_NRRUB, " +
				"DER.FORMULA_NRDER, " +
				"DER.FECHA_INICIAL_NRDER, " +
				"DER.FECHA_FINAL_NRDER, " +
				"DER.FECHA_PAGO_NRDER, " +
				"der.ORDEN_NRDER " +
				"from NRH_DETALLE_RUBRO der " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
				"where DER.IDE_NRDER not in " +
				"( " +
				"select a.IDE_NRDER from ( "+
				"select IDE_NRDRO,IDE_NRROL,IDE_GEEDP,DRO.IDE_NRDER,VALOR_NRDRO," +
				"DER.IDE_NRRUB,RUB.IDE_NRFOC,DER.FORMULA_NRDER,DER.FECHA_INICIAL_NRDER,DER.FECHA_FINAL_NRDER,DER.FECHA_PAGO_NRDER " +
				"from NRH_DETALLE_ROL DRO " +
				"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
				"where DER.ACTIVO_NRDER=true and DRO.IDE_NRROL="+IDE_NRROL+" and DRO.IDE_GEEDP="+IDE_GEEDP+" "+
				")a " +
				") " +
				"and ACTIVO_NRDER=true " +
				"and ide_nrdtn="+ide_nrdtn;


		TablaGenerica tab_det_rol=utilitario.consultar(sql);
		return tab_det_rol;
	}

	public TablaGenerica getDetalleRol(String IDE_NRROL,String  IDE_GEEDP){
		TablaGenerica tab_det_rol=utilitario.consultar("select IDE_NRDRO,IDE_NRROL,IDE_GEEDP,DRO.IDE_NRDER,VALOR_NRDRO," +
				"DER.IDE_NRRUB,RUB.IDE_NRFOC,ORDEN_NRDER,DER.FORMULA_NRDER,DER.FECHA_INICIAL_NRDER,DER.FECHA_FINAL_NRDER,DER.FECHA_PAGO_NRDER,decimo_nrrub " +
				"from NRH_DETALLE_ROL DRO " +
				"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
				"where DER.ACTIVO_NRDER=true and DRO.IDE_NRROL="+IDE_NRROL+" and DRO.IDE_GEEDP="+IDE_GEEDP+" "+
				"order by der.ORDEN_NRDER desc ");
		return tab_det_rol;
	}

	/**
	 * Metodo que retorna un Rol generado  
	 * @param IDE_NRDTN  : DETALLE TIPO NOMINA
	 * @param IDE_GEPRO  : PERIODO ROL
	 * @return TablaGenerica     : resultado
	 */
	public TablaGenerica getRol(String IDE_NRROL){
		if (IDE_NRROL!=null && !IDE_NRROL.isEmpty()){
			TablaGenerica tab_rol=utilitario.consultar("select * from NRH_ROL " +
					"where IDE_NRROL ="+IDE_NRROL);
			return tab_rol;
		}
		return null;
	}

	public TablaGenerica getTipoEmpleado(String IDE_GTTEM){
		System.out.println("sn c_parametro IDE_GTTEM...  "+IDE_GTTEM);
		
		if (IDE_GTTEM!=null && !IDE_GTTEM.isEmpty()){
			TablaGenerica tab_tip_emp=utilitario.consultar("SELECT * FROM GTH_TIPO_EMPLEADO WHERE IDE_GTTEM="+IDE_GTTEM);
			System.out.println("sn sql tab_tip_emp...  "+tab_tip_emp.getSql());
			
			return tab_tip_emp;
		}
		return null;
	}


	public TablaGenerica getTipoNomina(String IDE_NRTIN){
       System.out.println(" recivo parametro de entrada getTipoNomina...  " + IDE_NRTIN);
		if (IDE_NRTIN != null && !IDE_NRTIN.isEmpty()){
			TablaGenerica tab_tn=utilitario.consultar("select * from NRH_TIPO_NOMINA where IDE_NRTIN="+IDE_NRTIN);
			// System.out.println(" ejecuto sql.DDDDD.. " + tab_tn.getSql());
			return tab_tn;
		}
		return null;
	}

	/**
	 * Metodo que retorna un Rol generado  
	 * @param IDE_NRDTN  : DETALLE TIPO NOMINA
	 * @param IDE_GEPRO  : PERIODO ROL
	 * @return TablaGenerica     : resultado
	 */
	public TablaGenerica getRol(String IDE_NRDTN,String IDE_GEPRO){
		if (IDE_NRDTN!=null && !IDE_NRDTN.isEmpty()
				&& IDE_GEPRO!=null && !IDE_GEPRO.isEmpty()){
			TablaGenerica tab_rol=utilitario.consultar("select * from NRH_ROL " +
					"where IDE_NRDTN ="+IDE_NRDTN+" and IDE_GEPRO="+IDE_GEPRO+"");
			return tab_rol;
		}
		return null;
	}

	/**Metodo que retorna true si existe una nomina generada 
	 * @param IDE_NRDTN
	 * @param IDE_GEPRO
	 * @return boolean : resultado
	 */
	public boolean isNominaGenerada(String IDE_NRDTN,String IDE_GEPRO){
		if (getRol(IDE_NRDTN, IDE_GEPRO).getTotalFilas()>0){
			return true;
		}
		return false;
	}
	
	/** 
	 * @param IDE_GEEDP  : EMPLEADO DEPARTAMENTO
	 * @return String   resultado
	 * Metodo que devuelve el Decimo Tercer Sueldo de un empleado a una fecha determinada 
	 */
	public String getDecimoTercerSueldoOK(String IDE_GEEDP, String fecha_ini_dec, String fecha_fin_dec){
		double dou_decimo_tercer_sueldo=0;

		dou_decimo_tercer_sueldo=getSumatoriaRubro1(IDE_GEEDP, utilitario.getVariable("p_nrh_rubro_proviciones_d3"), fecha_ini_dec, fecha_fin_dec);
		//System.out.println("getDecimoTercerSueldoOK "+IDE_GEEDP +" "+ fecha_ini_dec+" - "+ fecha_fin_dec);
		//System.out.println("getDecimoTercerSueldoOK "+IDE_GEEDP +" "+utilitario.getFormatoNumero(dou_decimo_tercer_sueldo));
		return utilitario.getFormatoNumero(dou_decimo_tercer_sueldo);
		
	}
	
	public String getDecimoTercerSueldoPagado(String IDE_GEEDP, String fecha_ini_dec, String fecha_fin_dec){
		double dou_decimo_tercer_sueldo=0;

		dou_decimo_tercer_sueldo=getSumatoriaRubro1(IDE_GEEDP, utilitario.getVariable("p_nrh_rubro_decimo_tercer_rol"), fecha_ini_dec, fecha_fin_dec);

		//System.out.println("getDecimoTercerSueldoOK "+IDE_GEEDP +" "+ fecha_ini_dec+" - "+ fecha_fin_dec);
		//System.out.println("getDecimoTercerSueldoOK "+IDE_GEEDP +" "+utilitario.getFormatoNumero(dou_decimo_tercer_sueldo));
		return utilitario.getFormatoNumero(dou_decimo_tercer_sueldo);
		
	}

	public String getDecimoCuartoSueldoPagadoLiquidacion(String IDE_GEEDP, String fecha_ini_dec, String fecha_fin_dec,int tipoDecimo){
		double dou_decimo_cuarto_sueldo=0;

		dou_decimo_cuarto_sueldo=getSumatoriaRubro1AjusteLiquidacionDecimoC(IDE_GEEDP, "121,371", fecha_ini_dec, fecha_fin_dec,4);
		//dou_decimo_cuarto_sueldo=getSumatoriaRubro1AjusteLiquidacionDecimoC(IDE_GEEDP, "121,371,333,373", fecha_ini_dec, fecha_fin_dec,4);

		System.out.println("getDecimoCuartoSueldoPagadoLiquidacion "+dou_decimo_cuarto_sueldo);
		//System.out.println("getDecimoTercerSueldoOK "+IDE_GEEDP +" "+utilitario.getFormatoNumero(dou_decimo_tercer_sueldo));
		return utilitario.getFormatoNumero(dou_decimo_cuarto_sueldo);
	}
	
	
	public String getBaseImponibleGenerada(String IDE_GEEDP, String fecha_ini_dec, String fecha_fin_dec,int tipoDecimo){
		double dou_base_imponible_generada=0;

		dou_base_imponible_generada=getSumatoriaRubro1AjusteLiquidacionDecimoC(IDE_GEEDP, utilitario.getVariable("p_valor_base_imponible"), fecha_ini_dec, fecha_fin_dec,11);
		System.out.println("getBaseImponibleSueldoPagadoLiquidacion "+dou_base_imponible_generada);
		//System.out.println("getDecimoTercerSueldoOK "+IDE_GEEDP +" "+utilitario.getFormatoNumero(dou_decimo_tercer_sueldo));
		return utilitario.getFormatoNumero(dou_base_imponible_generada);
	}
	
	/**
	 * Metodo devuelve el valor Pagado por el rubro Provision 13 hasta la fecha de liquidacion
	 * @param IDE_GEEDP
	 * @param fecha_ini_dec
	 * @param fecha_fin_dec
	 * @param tipoDecimo
	 * @return
	 */
	public String getDecimoTercerSueldoPagadoLiquidacion(String IDE_GEEDP, String fecha_ini_dec, String fecha_fin_dec,int tipoDecimo){
		double dou_decimo_tercer_sueldo=0;

		dou_decimo_tercer_sueldo=getSumatoriaRubro1Liquidacion(IDE_GEEDP, "125", fecha_ini_dec, fecha_fin_dec,tipoDecimo);
		//dou_decimo_tercer_sueldo=getSumatoriaRubro1Liquidacion(IDE_GEEDP, "125,334", fecha_ini_dec, fecha_fin_dec,tipoDecimo);
		System.out.println("getDecimoTercerSueldoPagadoLiquidacion "+dou_decimo_tercer_sueldo);
		//System.out.println("getDecimoTercerSueldoOK "+IDE_GEEDP +" "+utilitario.getFormatoNumero(dou_decimo_tercer_sueldo));
		return utilitario.getFormatoNumero(dou_decimo_tercer_sueldo);
		
	}
	
	
	//////////////////////INFORMACION DE CODIGO DE TRABAJO 
	public String getDiasLaboradosPendientesCT(String IDE_GEEDP, String fecha_ini_dec, String fecha_fin_dec,int tipoDecimo){
		double dou_base_dias_laborados_pendientes=0;

		dou_base_dias_laborados_pendientes=getSumatoriaRubro1AjusteLiquidacionDecimoC(IDE_GEEDP, utilitario.getVariable("p_valor_base_imponible"), fecha_ini_dec, fecha_fin_dec,11);
		System.out.println("getBaseImponibleSueldoPagadoLiquidacion "+dou_base_dias_laborados_pendientes);
		//System.out.println("getDecimoTercerSueldoOK "+IDE_GEEDP +" "+utilitario.getFormatoNumero(dou_decimo_tercer_sueldo));
		return utilitario.getFormatoNumero(dou_base_dias_laborados_pendientes);
	}
	
	
	
	
	
	/**
	 * Metodo devuelve el nro de dias acumulados a vacacion
	 * @param IDE_GEEDP
	 * @param fecha_ini_dec
	 * @param fecha_fin_dec
	 * @param tipoDecimo
	 * @return
	 */
	public String getDiasVacacionesAcumuladasCT(String IDE_GEEDP, String fecha_ini_dec, String fecha_fin_dec,int tipoDecimo,String ide_gttem,String ide_geedp,int dias_exceso,boolean ejecuta_rmu){
		TablaGenerica tab_periodo_vacacion_liquidacion= new TablaGenerica();
		String valor="";
		TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,rmu_geedp,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+IDE_GEEDP);
		TablaGenerica tab_periodo_vacacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
				+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
				+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac,"
				+ " fecha_liquidacion_asvac "
				+ " FROM asi_vacacion "
				+ "where ide_gtemp="+tab_emp.getValor("IDE_GTEMP"));
   
		if (tab_periodo_vacacion.getTotalFilas()>0) {
		TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
					+ "order by ide_geedp asc");
			String ide_geedp_empleado="";
			//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
			//variable guarda la fecha de salida
			String fecha_finTemp="";
			fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
			//variable cuenta el numero de acciones
			int contAccionInvalida=0,contForAccionInvalida=0;
			StringBuilder str_ide_geedp = new StringBuilder();

			for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
				if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(IDE_GEEDP)==true) {		
				String valorUltimoElemento="";
				valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
				if(valorUltimoElemento.equals(",")==true){
					str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
				}else {
				}

					
				i=tabEmpleados.getTotalFilas();
				}else{
						//Voy anidando los ides de la accion
						str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
		               // valor++;
		                if (tabEmpleados.getTotalFilas()==1) {
		   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
		   				contForAccionInvalida++;
		   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
		   					str_ide_geedp.append(",");
		                   // System.out.println("str_ide:  "+str_ide_empleado);
		   					}
		    			}	 	
				}
			}
			
			
			//Si no contiene acciones de personal luego de la liquidacion
			if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
				str_ide_geedp.append(IDE_GEEDP);
			}
			
			
			
			
			
			String fecha_iniTemp="";
					//retornaAccionpersonal(tab_empleado.getValor("IDE_GTEMP"), "23");
			//if (fecha_iniTemp==null || fecha_iniTemp.equals("") || fecha_iniTemp.isEmpty()) {
				String a="";
				a=utilitario.getVariable("p_nrh_acciones_personal");
				String[] listaAccionesEmpleado;
				listaAccionesEmpleado=a.split(",");
				for (int i = 0; i < listaAccionesEmpleado.length; i++) {
					fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
					if (!fecha_iniTemp.equals("")) {
						i=listaAccionesEmpleado.length;
					}
				}

			
			
			
		 
		String fecha_inicio="",fecha_fin="";
		//String fecha_iniTemp="";
		//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
		/*		String a="";
		a=utilitario.getVariable("p_nrh_acciones_personal");
		String[] listaAccionesEmpleado;
		listaAccionesEmpleado=a.split(",");
		for (int i = 0; i < listaAccionesEmpleado.length; i++) {
			fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString());
			if (!fecha_iniTemp.equals("")) {
				i=listaAccionesEmpleado.length;
			}
		}
	*/	
		
				
		
		System.out.println("empleado_vacacion :"+tab_emp.getValor("IDE_GTEMP"));
		//String fecha_finTemp=retornaAccionpersonalLiquidacion(tab_emp.getValor("IDE_GTEMP"));
		String ide_asvac="";
		if (tab_periodo_vacacion.getTotalFilas()>0) {
			for (int i = 0; i < tab_periodo_vacacion.getTotalFilas(); i++) {
				fecha_inicio=tab_periodo_vacacion.getValor(i,"fecha_ingreso_asvac");
				fecha_fin=tab_periodo_vacacion.getValor(i,"fecha_finiquito_asvac");
				if (fecha_iniTemp.compareTo(fecha_inicio)==0) {
					//if (fecha_finTemp.compareTo(fecha_fin)==0) {
						ide_asvac=tab_periodo_vacacion.getValor(i,"ide_asvac");
						i=tab_periodo_vacacion.getTotalFilas();

					//}
				}
			}
			
				
		if (ide_asvac==null || ide_asvac.equals("") || ide_asvac.isEmpty()) {
			tab_periodo_vacacion = new TablaGenerica();
			tab_periodo_vacacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
					+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
					+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac,fecha_liquidacion_asvac "
					+ " FROM asi_vacacion "
					+ "where ide_gtemp="+tab_emp.getValor("IDE_GTEMP")+" AND fecha_liquidacion_asvac is not null");	
			
			
			
			boolean band=false;
			if (tab_periodo_vacacion.getTotalFilas()>0) {
			for (int i = 0; i < tab_periodo_vacacion.getTotalFilas(); i++) {
				for (int j = 0; j < tabEmpleados.getTotalFilas(); j++) {
					if (tabEmpleados.getValor(j, "IDE_GEEDP").compareTo(tab_periodo_vacacion.getValor(i,"ide_geedp_liquidacion"))==0) {		
					band=true;
					ide_asvac=tab_periodo_vacacion.getValor(i,"ide_asvac");
					i=tab_periodo_vacacion.getTotalFilas();
					j=tabEmpleados.getTotalFilas();
					}else {
						band=false;
					}
					}
				}
			}else {
				
			}
			
			
			
			/*if (tab_periodo_vacacion.getTotalFilas()>0) {
			for (int i = 0; i < tab_periodo_vacacion.getTotalFilas(); i++) {
				if (fecha_iniTemp.compareTo(tab_periodo_vacacion.getValor(i,"fecha_liquidacion_asvac"))==0) {
						ide_asvac=tab_periodo_vacacion.getValor(i,"ide_asvac");
						i=tab_periodo_vacacion.getTotalFilas();
						}
				}
			}else {
				
			}*/ 
				
			
		}else {
			///////////////////////////////////////////////
			
			//Para este caso si ya contiene un ide de vacacion pero se ingreso la fecha erroneaente
			boolean band=false;
			if (tab_periodo_vacacion.getTotalFilas()>0) {
				
				
				tab_periodo_vacacion = new TablaGenerica();
				tab_periodo_vacacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
						+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
						+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac,fecha_liquidacion_asvac,ide_geedp_liquidacion "
						+ " FROM asi_vacacion "
						+ "where ide_gtemp="+tab_emp.getValor("IDE_GTEMP")+" AND fecha_liquidacion_asvac is not null");	
				
				if (tab_periodo_vacacion.getTotalFilas()==0) {
					
				}else{				
			for (int i = 0; i < tab_periodo_vacacion.getTotalFilas(); i++) {
				for (int j = 0; j < tabEmpleados.getTotalFilas(); j++) {
					if (tabEmpleados.getValor(j, "IDE_GEEDP").compareTo(tab_periodo_vacacion.getValor(i,"ide_geedp_liquidacion"))==0) {		
					band=true;
					ide_asvac=tab_periodo_vacacion.getValor(i,"ide_asvac");
					i=tab_periodo_vacacion.getTotalFilas();
					j=tabEmpleados.getTotalFilas();
					}else {
						band=false;
					}
					}
				}
			
			}
			}else {}
				
			
		}
		

		
		}

		tab_periodo_vacacion_liquidacion = new TablaGenerica();
		if (ide_asvac==null || ide_asvac.equals("") || ide_asvac.isEmpty()) {
			tab_periodo_vacacion_liquidacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
					+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
					+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac "
					+ " FROM asi_vacacion "
					+ "where  ide_gtemp="+tab_emp.getValor("IDE_GTEMP") 
					+ " and activo_asvac=false "
					+ "order by ide_asvac desc "
					+ "limit 1");
					//+ "ide_asvac="+ide_asvac)
		}else {
			tab_periodo_vacacion_liquidacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
					+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
					+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac "
					+ " FROM asi_vacacion "
					+ "where  ide_asvac="+ide_asvac 
					+ " and ide_gtemp="+tab_emp.getValor("IDE_GTEMP") 
					+ " order by ide_asvac desc "
					+ "limit 1");
					//+ "ide_asvac="+ide_asvac)
		}

		
		try {
			//LOEP
			if (ide_gttem.equals("2")) {
			//return valor=serv_asistencia.getDatosEmpleado(tab_emp.getValor("IDE_GTEMP"),Integer.parseInt(tab_emp.getValor("IDE_GTTEM")),tab_periodo_vacacion_liquidacion.getValor("IDE_ASVAC"));
			}else if (ide_gttem.equals("1")) {
				
			    int valorTemp=0;
				
			//Anterior	
			//valor=serv_asistencia.getDatosEmpleadoCodigoTrabajo(tab_emp.getValor("IDE_GTEMP"),Integer.parseInt(tab_emp.getValor("IDE_GTTEM")),tab_periodo_vacacion_liquidacion.getValor("IDE_ASVAC"),tab_periodo_vacacion_liquidacion.getValor("fecha_ingreso_asvac"),tab_periodo_vacacion_liquidacion.getValor("fecha_finiquito_asvac"));

				//Nuevo
				
				
				
				
				int bandSalidaAjuste=0;
				System.out.println("p_nrh_dias_exceso_liquidacion");
				//return getDiasVacacionesAcumuladas(ide_geedp,fecha_inicial_gepro,fecha_final_gepro,4);
				TablaGenerica tab_emp1=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+ide_geedp);
				tabEmpleados=null;
				tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
						+ "order by ide_geedp asc");
				ide_geedp_empleado="";
				//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
				//variable guarda la fecha de salida
				fecha_finTemp="";
				fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
				//variable cuenta el numero de acciones
				contAccionInvalida=0;contForAccionInvalida=0;
				str_ide_geedp=new StringBuilder();

				for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
					if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(IDE_GEEDP)==true) {
						String valorUltimoElemento="";
						valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
						if(valorUltimoElemento.equals(",")==true){
							str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
						}else {
						}

					i=tabEmpleados.getTotalFilas();
					}else{
							//Voy anidando los ides de la accion
							str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
			               // valor++;
			                if (tabEmpleados.getTotalFilas()==1) {
			   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
			   				contForAccionInvalida++;
			   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
			   					str_ide_geedp.append(",");
			                   // System.out.println("str_ide:  "+str_ide_empleado);
			   					}
			    			}	 	
					}
				}
				
				
				//Si no contiene acciones de personal luego de la liquidacion
				if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
					str_ide_geedp.append(IDE_GEEDP);
				}
				
					a="";
					a=utilitario.getVariable("p_nrh_acciones_personal");
					String[] listaAccionesEmpleado1;
					listaAccionesEmpleado1=a.split(",");
					for (int i = 0; i < listaAccionesEmpleado1.length; i++) {
						fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado1[i].toString(),str_ide_geedp.toString());
						if (!fecha_iniTemp.equals("")) {
							i=listaAccionesEmpleado1.length;
						}
					}


				ide_geedp_empleado=IDE_GEEDP;
				System.out.println("fecha_finTemp"+fecha_finTemp);
				String fecha_fin_validacion="";
				if (utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp))>=31) {
					fecha_fin_validacion=utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-30";
					fecha_finTemp=utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-30";
				}else {
					fecha_fin_validacion=utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-"+utilitario.getDia(fecha_finTemp);
				}
					
				System.out.println("fecha_finTemp"+fecha_fin_validacion);
				
				
				TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_finTemp)+"%' ");
				
				
				TablaGenerica tab_liquidacion = utilitario.consultar("SELECT ide_gepro, ide_nrtit, ide_gemes, ide_geani, fecha_inicial_gepro,  "
						+ "fecha_final_gepro, detalle_periodo_gepro,  "
						+ "activo_gepro,gen_ide_gepro  "
						+ "FROM gen_perido_rol "
						+ "where ide_geani="+tabAnio.getValor("IDE_GEANI")+" and ide_gemes="+utilitario.getMes(fecha_finTemp)
						+ " order by ide_gepro desc ");


				TablaGenerica tab_periodo_rol = utilitario.consultar("select * from nrh_rol where ide_gepro="+tab_liquidacion.getValor("IDE_GEPRO")+" "
						+ "and ide_nrdtn IN(2,4) AND ide_nresr=1 "
						+ " order by ide_nrrol asc");
				if (tab_periodo_rol.getTotalFilas()==0) {
					valor=""+0;
				}else {
				
					System.out.println("INGRESO A EXCESO");
				int cont=0;
				StringBuilder str = new StringBuilder();
				for (int i = 0; i < tab_periodo_rol.getTotalFilas(); i++) {
				  
				  		//Voy anidando los ides de la accion
				          	    str.append(tab_periodo_rol.getValor(i, "IDE_NRROL"));
				               // valor++;
				                if (tab_periodo_rol.getTotalFilas()==1) {
				   			}else if (cont<=tab_periodo_rol.getTotalFilas()) {
				   					cont++;
				   					if(cont<(tab_periodo_rol.getTotalFilas())){
				                    str.append(",");
				                   // System.out.println("str_ide:  "+str_ide_empleado);
				   					}
				    			}
				   			 		         		
				}
				
				
				TablaGenerica tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
						+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
						+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
						+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
						+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
						+ "where DRO.IDE_GEEDP in ( "
						+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
						+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp+") and ide_gttem !=3  "
						+ ")  "
						+ "and rol.ide_nrrol IN ("+str.toString()+")");  
				
				/*TablaGenerica tab_detalle_rol = utilitario.consultar("select * from nrh_detalle_rol where ide_nrrol in("+str.toString()+") "
						+ "and ide_geedp in("+ide_geedp_empleado+")"); */ 
		
			 
			    if (tab_detalle_rol.getTotalFilas()==0) {
			    	valor=""+0;
			    	bandSalidaAjuste=0;
				}else {
					valorTemp=(int)(utilitario.getDiferenciaFechas360(utilitario.getFecha(fecha_finTemp), utilitario.getFecha(fecha_fin_validacion))-1);
					 if (valorTemp<=0.0 || valorTemp<=0) {
						valor=""+0;
						bandSalidaAjuste=1;
					}else {
						valorTemp=(int)(utilitario.getDiferenciaFechas360(utilitario.getFecha(fecha_finTemp), utilitario.getFecha(fecha_fin_validacion))-1);
						//String valorNuevo=""+valorTemp;
						bandSalidaAjuste=1;
					}
						
						

				}
			    
			 /*   valorTemp=utilitario.getDiferenciaFechas360(utilitario.getFecha(fecha_finTemp), utilitario.getFecha(fecha_fin_validacion))-1;
				if (valorTemp<=0.0) {
					valor=""+0.0;
				}else {
				}
				*/
				
				}
						


				double valorVacaciones=0.0;
				valorVacaciones=Double.parseDouble(serv_asistencia.getDiasLaboradosPendientesCTVacaciones(tab_emp.getValor("IDE_GTEMP"),Integer.parseInt(tab_emp.getValor("IDE_GTTEM")),ide_asvac,tab_periodo_vacacion_liquidacion.getValor("fecha_ingreso_asvac"),tab_periodo_vacacion_liquidacion.getValor("fecha_finiquito_asvac"),bandSalidaAjuste,bandSalidaAjuste,valorTemp,tab_emp.getValor("RMU_GEEDP"),dias_exceso,ejecuta_rmu));
				vacacionesNegativoValor=0.0;
				vacacionesNegativoValor=valorVacaciones;

				if (valorVacaciones>0.0) {
					return valor=serv_asistencia.getDiasLaboradosPendientesCTVacaciones(tab_emp.getValor("IDE_GTEMP"),Integer.parseInt(tab_emp.getValor("IDE_GTTEM")),ide_asvac,tab_periodo_vacacion_liquidacion.getValor("fecha_ingreso_asvac"),tab_periodo_vacacion_liquidacion.getValor("fecha_finiquito_asvac"),bandSalidaAjuste,bandSalidaAjuste,valorTemp,tab_emp.getValor("RMU_GEEDP"),dias_exceso,ejecuta_rmu);					
				}else {
					
					return ""+0.0;
				}
	
				
				
				
				//return valor=serv_asistencia.getDiasLaboradosUltimoPeriodoVacacionCT(tab_emp.getValor("IDE_GTEMP"),Integer.parseInt(tab_emp.getValor("IDE_GTTEM")),tab_periodo_vacacion_liquidacion.getValor("IDE_ASVAC"),tab_periodo_vacacion_liquidacion.getValor("fecha_ingreso_asvac"),tab_periodo_vacacion_liquidacion.getValor("fecha_finiquito_asvac"),bandSalidaAjuste,valorTemp);			
				
			}
			

			
			
			} catch (Exception e) {
				System.out.println("error verVacaciones()");					}
		}else {
			valor="0";
		}
		return utilitario.getFormatoNumero(valor);
	
	}
		
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	/**
	 * Metodo devuelve el nro de dias acumulados a vacacion
	 * @param IDE_GEEDP
	 * @param fecha_ini_dec
	 * @param fecha_fin_dec
	 * @param tipoDecimo
	 * @return
	 */
	public String getDiasVacacionesAcumuladasValorCancelar(String IDE_GEEDP, String fecha_ini_dec, String fecha_fin_dec,int tipoDecimo,String ide_gttem,String ide_geedp,int dias_exceso,boolean ejecuta_rmu,boolean ide_geame){
		
		String valor="";
		TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,rmu_geedp,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+IDE_GEEDP);
		TablaGenerica tab_periodo_vacacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
				+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
				+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac "
				+ " FROM asi_vacacion "
				+ "where ide_gtemp="+tab_emp.getValor("IDE_GTEMP")+" AND ACTIVO_ASVAC=false "
				+ "order by ide_asvac desc");

		if (tab_periodo_vacacion.getTotalFilas()>0) {
			

			String fecha_inicio="",fecha_fin="";
			String fecha_iniTemp="";
			//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
			String a="";
			
			TablaGenerica tab_emp1=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+ide_geedp);
			TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
					+ "order by ide_geedp asc");
			String ide_geedp_empleado="";
			//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
			//variable guarda la fecha de salida
			String fecha_finTemp="";
			fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
			//variable cuenta el numero de acciones
			int contAccionInvalida=0,contForAccionInvalida=0;
			StringBuilder str_ide_geedp=new StringBuilder();

			for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
				if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(IDE_GEEDP)==true) {
					String valorUltimoElemento="";
					valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
					if(valorUltimoElemento.equals(",")==true){
						str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
					}else {
					}

				i=tabEmpleados.getTotalFilas();
				}else{
						//Voy anidando los ides de la accion
						str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
		               // valor++;
		                if (tabEmpleados.getTotalFilas()==1) {
		   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
		   				contForAccionInvalida++;
		   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
		   					str_ide_geedp.append(",");
		                   // System.out.println("str_ide:  "+str_ide_empleado);
		   					}
		    			}	 	
				}
			}
			
			
			//Si no contiene acciones de personal luego de la liquidacion
			if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
				str_ide_geedp.append(IDE_GEEDP);
			}
			
				a=utilitario.getVariable("p_nrh_acciones_personal");
				String[] listaAccionesEmpleado;
				listaAccionesEmpleado=a.split(",");
				for (int i = 0; i < listaAccionesEmpleado.length; i++) {
					fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
					if (!fecha_iniTemp.equals("")) {
						i=listaAccionesEmpleado.length;
					}
				}
			
			
		 System.out.println("empleado_vacacion :"+tab_emp.getValor("IDE_GTEMP"));
		String ide_asvac="";
		if (tab_periodo_vacacion.getTotalFilas()>0) {
			for (int i = 0; i < tab_periodo_vacacion.getTotalFilas(); i++) {
				fecha_inicio=tab_periodo_vacacion.getValor(i,"fecha_ingreso_asvac");
				fecha_fin=tab_periodo_vacacion.getValor(i,"fecha_finiquito_asvac");
				if (fecha_iniTemp.compareTo(fecha_inicio)==0) {
					//if (fecha_finTemp.compareTo(fecha_fin)==0) {
						ide_asvac=tab_periodo_vacacion.getValor(i,"ide_asvac");
						i=tab_periodo_vacacion.getTotalFilas();
					//}
				}System.out.println();
			}
			
			
			
			
			if (ide_asvac==null || ide_asvac.equals("") || ide_asvac.isEmpty()) {
				tab_periodo_vacacion = new TablaGenerica();
				tab_periodo_vacacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
						+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
						+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac,fecha_liquidacion_asvac "
						+ " FROM asi_vacacion "
						+ "where ide_gtemp="+tab_emp.getValor("IDE_GTEMP")+" AND fecha_liquidacion_asvac is not null");	
				
				
				if (tab_periodo_vacacion.getTotalFilas()>0) {
				for (int i = 0; i < tab_periodo_vacacion.getTotalFilas(); i++) {
					if (fecha_iniTemp.compareTo(tab_periodo_vacacion.getValor(i,"fecha_liquidacion_asvac"))==0) {
							ide_asvac=tab_periodo_vacacion.getValor(i,"ide_asvac");
							i=tab_periodo_vacacion.getTotalFilas();
							}
					}
				}else {
					
				} 
					
				
			}else {
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				tab_periodo_vacacion = new TablaGenerica();
				tab_periodo_vacacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
						+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
						+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac,fecha_liquidacion_asvac "
						+ " FROM asi_vacacion "
						+ "where ide_gtemp="+tab_emp.getValor("IDE_GTEMP")+" AND fecha_liquidacion_asvac is not null");	
				
				
				if (tab_periodo_vacacion.getTotalFilas()>0) {
				for (int i = 0; i < tab_periodo_vacacion.getTotalFilas(); i++) {
					if (fecha_iniTemp.compareTo(tab_periodo_vacacion.getValor(i,"fecha_liquidacion_asvac"))==0) {
							ide_asvac=tab_periodo_vacacion.getValor(i,"ide_asvac");
							i=tab_periodo_vacacion.getTotalFilas();
							}
					}
				}else {
					
				} 
				
				
			}
			
			
			
			
			
			
			
		}

		TablaGenerica tab_periodo_vacacion_liquidacion;
		if (ide_asvac==null || ide_asvac.equals("") || ide_asvac.isEmpty()) {
			tab_periodo_vacacion_liquidacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
					+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
					+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac "
					+ " FROM asi_vacacion "
					+ "where  ide_gtemp="+tab_emp.getValor("IDE_GTEMP") 
					+ " and activo_asvac=false "
					+ "order by ide_asvac desc "
					+ "limit 1");
					//+ "ide_asvac="+ide_asvac)
		}else {
			tab_periodo_vacacion_liquidacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
					+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
					+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac "
					+ " FROM asi_vacacion "
					+ "where  ide_asvac="+ide_asvac 
					//+ " and activo_asvac=false "
					+ " order by ide_asvac desc "
					+ "limit 1");
					//+ "ide_asvac="+ide_asvac)
		}
		
		
	
		
		try {
			//LOEP
			if (ide_gttem.equals("2")) {
				valor="";
				valor=serv_asistencia.getDatosEmpleado(tab_emp.getValor("IDE_GTEMP"),Integer.parseInt(tab_emp.getValor("IDE_GTTEM")),tab_periodo_vacacion_liquidacion.getValor("IDE_ASVAC"),tab_emp.getValor("rmu_geedp"),tab_periodo_vacacion_liquidacion.getValor("fecha_ingreso_asvac"),tab_periodo_vacacion_liquidacion.getValor("fecha_finiquito_asvac"),dias_exceso,true,true);
			double totalVacacionesXEmpleado=0.0,valorDiaRemuneracion=0.0;
			String string = "";
			boolean banderaCero=false,banderaCero1=false;
			int nuevoValorRedondeoDecimal=0;
			double decimales=0.0;	
			//valorDiaRemuneracion=Double.parseDouble(utilitario.getFormatoNumero((Double.parseDouble(tab_emp.getValor("rmu_geedp"))/30),2));
			valorDiaRemuneracion=Double.parseDouble(tab_emp.getValor("rmu_geedp"))/30;
			double total=0.0;
	//				total=valorDiaRemuneracion*Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(valor),2));
					total=valorDiaRemuneracion * Double.parseDouble(valor);			
			//string= String.valueOf(valorDiaRemuneracion);
			//String[] parts = string.split(Pattern.quote("."));
		    //String part1 = parts[0]; // 004
			//String part2 = parts[1]; // 034556
			//String obtenerDecimales=getDecimales(valorDiaRemuneracion);
			//boolean bandera=false;
			//if (obtenerDecimales.substring(0,1).equals("0")) {
			//	bandera=true;
			//}else {
			//	bandera=false;
			//}
			//decimales=Double.parseDouble(getDecimales(valorDiaRemuneracion));
			//String nuevoValorDecimales="";
			//String retornaValor="";
			//if (bandera==true) {
			//	nuevoValorDecimales=""+part1+".0"+(int)decimales;
			//}else{
			//		nuevoValorDecimales=""+part1+"."+(int)decimales;
			//	}
			
			
			//totalVacacionesXEmpleado=Double.parseDouble(nuevoValorDecimales)* Double.parseDouble(valor);
			//int decimalesValorTotal=0;
			//decimalesValorTotal=Integer.parseInt(getDecimales(totalVacacionesXEmpleado));
			//String stringTemp= String.valueOf(totalVacacionesXEmpleado);
			//String[] partsResultado = stringTemp.split(Pattern.quote("."));
		    //String partResutado1 = partsResultado[0]; // 004
			//String partResultado2 = partsResultado[1]; // 034556
			


			//return ""+partResutado1+".0"+decimalesValorTotal;
			//}else {
			double valorVacaciones=0.0;
			vacacionesNegativoValor=0.0;
			valorVacaciones=total;
			vacacionesNegativoValor=valorVacaciones;
			if (valorVacaciones>0.0 || valorVacaciones>0) {
				if (ide_geame==true) {
					return ""+0.0;
				}else {
					return utilitario.getFormatoNumero(valorVacaciones);
						
				}
				
			}else {
				return ""+0.0;
			}

			
			}else if (ide_gttem.equals("1")) {
				return ""+0.0;
			
			}
			

			
			
			} catch (Exception e) {
				System.out.println("error verVacaciones()");					}
		}else {
			valor="0.0";
		}
		return utilitario.getFormatoNumero(valor);
	
	}
		

	
	
	
	
	
	
	/**
	 * Metodo devuelve el nro de dias acumulados a vacacion
	 * @param IDE_GEEDP
	 * @param fecha_ini_dec
	 * @param fecha_fin_dec
	 * @param tipoDecimo
	 * @return
	 */
	
	public String getDiasVacacionesAcumuladas(String IDE_GEEDP, String fecha_ini_dec, String fecha_fin_dec,int tipoDecimo,String ide_gttem,String ide_geedp){
		
		String valor="";
		TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+IDE_GEEDP);
		TablaGenerica tab_periodo_vacacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
				+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
				+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac "
				+ " FROM asi_vacacion "
				+ "where ide_gtemp="+tab_emp.getValor("IDE_GTEMP")+" AND ACTIVO_ASVAC=false");

		if (tab_periodo_vacacion.getTotalFilas()>0) {
			
	
		 
		String fecha_inicio="",fecha_fin="";
		String fecha_iniTemp="";
		
		
		
		TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
				+ "order by ide_geedp asc");
		String ide_geedp_empleado="";
		//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
		//variable guarda la fecha de salida
		String fecha_finTemp="";
		fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
		//variable cuenta el numero de acciones
		int contAccionInvalida=0,contForAccionInvalida=0;
		StringBuilder str_ide_geedp = new StringBuilder();

		for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
			if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(IDE_GEEDP)==true) {
				String valorUltimoElemento="";
				valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
				if(valorUltimoElemento.equals(",")==true){
					str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
				}else {
				}
			i=tabEmpleados.getTotalFilas();
			}else{
					//Voy anidando los ides de la accion
					str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
	               // valor++;
	                if (tabEmpleados.getTotalFilas()==1) {
	   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
	   				contForAccionInvalida++;
	   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
	   					str_ide_geedp.append(",");
	                   // System.out.println("str_ide:  "+str_ide_empleado);
	   					}
	    			}	 	
			}
		}
		
		
		//Si no contiene acciones de personal luego de la liquidacion
		if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
			str_ide_geedp.append(IDE_GEEDP);
		}

		
		//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
		String a="";
		a=utilitario.getVariable("p_nrh_acciones_personal");
		String[] listaAccionesEmpleado;
		listaAccionesEmpleado=a.split(",");
		for (int i = 0; i < listaAccionesEmpleado.length; i++) {
			fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
			if (!fecha_iniTemp.equals("")) {
				i=listaAccionesEmpleado.length;
			}
		}
		
		
		
		
		System.out.println("empleado_vacacion :"+tab_emp.getValor("IDE_GTEMP"));
		String ide_asvac="";
		if (tab_periodo_vacacion.getTotalFilas()>0) {
			for (int i = 0; i < tab_periodo_vacacion.getTotalFilas(); i++) {
				fecha_inicio=tab_periodo_vacacion.getValor(i,"fecha_ingreso_asvac");
				fecha_fin=tab_periodo_vacacion.getValor(i,"fecha_finiquito_asvac");
				if (fecha_iniTemp.compareTo(fecha_inicio)==0) {
					//if (fecha_finTemp.compareTo(fecha_fin)==0) {
						ide_asvac=tab_periodo_vacacion.getValor(i,"ide_asvac");
						i=tab_periodo_vacacion.getTotalFilas();

					}
				}
				
				
				
				if (ide_asvac==null || ide_asvac.equals("") || ide_asvac.isEmpty()) {
					tab_periodo_vacacion= new TablaGenerica();
					tab_periodo_vacacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
							+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
							+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac,fecha_liquidacion_asvac,ide_geedp_liquidacion "
							+ " FROM asi_vacacion "
							+ "where ide_gtemp="+tab_emp.getValor("IDE_GTEMP")+" AND fecha_liquidacion_asvac is not null");	
					 
					
					
					boolean band=false;
					if (tab_periodo_vacacion.getTotalFilas()>0) {
					for (int i = 0; i < tab_periodo_vacacion.getTotalFilas(); i++) {
						for (int j = 0; j < tabEmpleados.getTotalFilas(); j++) {
							if (tabEmpleados.getValor(j, "IDE_GEEDP").compareTo(tab_periodo_vacacion.getValor(i,"ide_geedp_liquidacion"))==0) {		
							band=true;
							ide_asvac=tab_periodo_vacacion.getValor(i,"ide_asvac");
							i=tab_periodo_vacacion.getTotalFilas();
							j=tabEmpleados.getTotalFilas();
							}else {
								band=false;
							}
							}
						}
					}else {
						
					} 
					
					 
					
					
					/*if (tab_periodo_vacacion.getTotalFilas()>0) {
					for (int y = 0; y< tab_periodo_vacacion.getTotalFilas(); y++) {
						if (IDE_GEEDP.compareTo(tab_periodo_vacacion.getValor(y,"ide_geedp_liquidacion"))==0) {
								ide_asvac=tab_periodo_vacacion.getValor(y,"ide_asvac");
								y=tab_periodo_vacacion.getTotalFilas();
								}
						}
					}else {
						
					} */
						
					
				}else {
					
					tab_periodo_vacacion= new TablaGenerica();
					tab_periodo_vacacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
							+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
							+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac,fecha_liquidacion_asvac,ide_geedp_liquidacion "
							+ " FROM asi_vacacion "
							+ "where ide_gtemp="+tab_emp.getValor("IDE_GTEMP")+" AND fecha_liquidacion_asvac is not null");	
					 
			
					boolean band=false;
					if (tab_periodo_vacacion.getTotalFilas()>0) {
					for (int i = 0; i < tab_periodo_vacacion.getTotalFilas(); i++) {
						for (int j = 0; j < tabEmpleados.getTotalFilas(); j++) {
							if (tabEmpleados.getValor(j, "IDE_GEEDP").compareTo(tab_periodo_vacacion.getValor(i,"ide_geedp_liquidacion"))==0) {		
							band=true;
							ide_asvac=tab_periodo_vacacion.getValor(i,"ide_asvac");
							i=tab_periodo_vacacion.getTotalFilas();
							j=tabEmpleados.getTotalFilas();
							}else {
								band=false;
							}
							}
						}
					}else {
						
					} 
					
					
					
					
				}
				
				
				
			
			
		}else {
			
		}

 

	 
		try {
			//LOEP
			//if (ide_gttem.equals("2")) {
			double valorVacaciones=0;
			valorVacaciones=Double.parseDouble(serv_asistencia.getDatosEmpleado(tab_emp.getValor("IDE_GTEMP"),Integer.parseInt(tab_emp.getValor("IDE_GTTEM")),ide_asvac,"","","",0,false,false));
			if (valorVacaciones>0.0) {
				vacacionesNegativo=false;
			}
			else {
				vacacionesNegativo=true;
			}
			return valor=serv_asistencia.getDatosEmpleado(tab_emp.getValor("IDE_GTEMP"),Integer.parseInt(tab_emp.getValor("IDE_GTTEM")),ide_asvac,"","","",0,false,false);
			//}else if (ide_gttem.equals("1")) {
			//	return ""+0.0;
			
			//}
			

			
			} catch (Exception e) {
				System.out.println("error verVacaciones()");					}
		}else {
			valor="0.0";
		}
		if (valor.equals("") || valor.isEmpty()) {
			valor=""+0.0;
		}
		return utilitario.getFormatoNumero(valor);
	
	}
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/**
 * Metodo devuelve el valor total generdo por : (la base imponible generada hasta la fecha de liquidacion)/[30]*[DIAS PENDIENTES]
 * En el caso de que se le pago el en nomina normal el (26 de Noviembre lo correspondiente a 30 dias generando un desfase de 4 dias, 
 * Por lo cual se calcula el valor total en un proceso y no formula 	
 * @param ide_geedp
 * @param ide_gepro
 * @return
 */
	
public String getEstadoLiquidacionEmpleado(String ide_geedp,String ide_gepro){

	boolean dias_exceso=false;
	int valorBandera=0;
	/*dias_exceso=diasExcesoLiquidacion(ide_geedp, ide_gepro);
	if (dias_exceso==false){
		valorBandera=0;
		return ""+valorBandera;
	}
	else if (dias_exceso==true){
		valorBandera=1;
	}
	*/	
	int valorBanderaEjecutaRol=0;
	int valorDiasExceso=0;
	String valor="";
	//return getDiasVacacionesAcumuladas(ide_geedp,fecha_inicial_gepro,fecha_final_gepro,4);
	TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+ide_geedp);
	String fecha_iniTemp="";
	//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
	String a="";
	
	
	
	TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
			+ "order by ide_geedp asc");
	String ide_geedp_empleado="";
	//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
	//variable guarda la fecha de salida
	String fecha_finTemp="";
	fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
	//variable cuenta el numero de acciones
	int contAccionInvalida=0,contForAccionInvalida=0;
	StringBuilder str_ide_geedp = new StringBuilder();

	for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
		if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(ide_geedp)==true) {
			String valorUltimoElemento="";
			valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
			if(valorUltimoElemento.equals(",")==true){
				str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
			}else {
			}

		i=tabEmpleados.getTotalFilas();
		}else{
				//Voy anidando los ides de la accion
				str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
               // valor++;
                if (tabEmpleados.getTotalFilas()==1) {
   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
   				contForAccionInvalida++;
   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
   					str_ide_geedp.append(",");
                   // System.out.println("str_ide:  "+str_ide_empleado);
   					}
    			}	 	
		}
	}
	
	
	//Si no contiene acciones de personal luego de la liquidacion
	if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
		str_ide_geedp.append(ide_geedp);
	}

	
	//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
	
	a=utilitario.getVariable("p_nrh_acciones_personal");
	String[] listaAccionesEmpleado;
	listaAccionesEmpleado=a.split(",");
	for (int i = 0; i < listaAccionesEmpleado.length; i++) {
		fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
		if (!fecha_iniTemp.equals("")) {
			i=listaAccionesEmpleado.length;
		}
	}
	
	
	ide_geedp_empleado=ide_geedp;
	System.out.println("fecha_finTemp"+fecha_finTemp);
	String fecha_fin_validacion="";
	if (utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp))>=31) {
		fecha_fin_validacion=utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-30";
	}else {
		fecha_fin_validacion=utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-"+utilitario.getDia(fecha_finTemp);
	}
		
	System.out.println("fecha_finTemp"+fecha_fin_validacion);
	
	
	TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_finTemp)+"%' ");
	
	
	TablaGenerica tab_liquidacion = utilitario.consultar("SELECT ide_gepro, ide_nrtit, ide_gemes, ide_geani, fecha_inicial_gepro,  "
			+ "fecha_final_gepro, detalle_periodo_gepro,  "
			+ "activo_gepro,gen_ide_gepro  "
			+ "FROM gen_perido_rol "
			+ "where ide_geani="+tabAnio.getValor("IDE_GEANI")+" and ide_gemes="+utilitario.getMes(fecha_finTemp)
			+ " order by ide_gepro desc ");


	TablaGenerica tab_periodo_rol = utilitario.consultar("select * from nrh_rol where ide_gepro="+tab_liquidacion.getValor("IDE_GEPRO")+" "
			+ "and ide_nrdtn IN(2,4) AND ide_nresr=1 "
			+ " order by ide_nrrol asc");
	if (tab_periodo_rol.getTotalFilas()==0) {
		valor=""+0;
	}else {
		System.out.println("INGRESO A EXCESO");
	int cont=0;
	StringBuilder str = new StringBuilder();
	for (int i = 0; i < tab_periodo_rol.getTotalFilas(); i++) {
	  
	  		//Voy anidando los ides de la accion
	          	    str.append(tab_periodo_rol.getValor(i, "IDE_NRROL"));
	               // valor++;
	                if (tab_periodo_rol.getTotalFilas()==1) {
	   			}else if (cont<=tab_periodo_rol.getTotalFilas()) {
	   					cont++;
	   					if(cont<(tab_periodo_rol.getTotalFilas())){
	                    str.append(",");
	                   // System.out.println("str_ide:  "+str_ide_empleado);
	   					}
	    			}
	   			 		         		
	}
	
	
	TablaGenerica tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
			+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
			+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
			+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
			+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
			+ "where DRO.IDE_GEEDP in ( "
			+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
			+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp+") and ide_gttem !=3  "
			+ ")  "
			+ "and rol.ide_nrrol IN ("+str.toString()+")");  
	
	/*TablaGenerica tab_detalle_rol = utilitario.consultar("select * from nrh_detalle_rol where ide_nrrol in("+str.toString()+") "
			+ "and ide_geedp in("+ide_geedp_empleado+")"); */ 
    double valorTemp=0;
 
    if (tab_detalle_rol.getTotalFilas()==0) {
    	valor=""+0;
    	valorBanderaEjecutaRol=0;
	}else {
		valorTemp=(utilitario.getDiferenciaFechas360(utilitario.getFecha(fecha_finTemp), utilitario.getFecha(fecha_fin_validacion))-1);
		 if (valorTemp<=0.0 || valorTemp<=0) {
			valorDiasExceso=0;
			valorBanderaEjecutaRol=0;
		}else {
			valorDiasExceso = (int) valorTemp;
			valorBanderaEjecutaRol=1;
		}
			
			

	}
    
 /*   valorTemp=utilitario.getDiferenciaFechas360(utilitario.getFecha(fecha_finTemp), utilitario.getFecha(fecha_fin_validacion))-1;
	if (valorTemp<=0.0) {
		valor=""+0.0;
	}else {
	}
	*/
	
	}

	return ""+valorBanderaEjecutaRol+","+valorDiasExceso;
	
	
	
}	
	
	
	
	

/** 
	 * @param IDE_GEEDP  : EMPLEADO DEPARTAMENTO
	 * @return String   resultado
	 * Metodo que devuelve el Decimo Tercer Sueldo de un empleado a una fecha determinada 
	 */
	public String getDecimoTercerSueldo(String IDE_GEEDP,String fecha_calculo){
		double dou_remuneracion_base=0;

		try {
			dou_remuneracion_base=pckUtilidades.CConversion.CDbl_2(getRemuneracionEmpleado(IDE_GEEDP));
		} catch (Exception e) {
			// TODO: handle exception
		}
		double dou_decimo_tercer_sueldo=0;

		String str_ide_nrdtn=getIdeDetalleTipoNomina(IDE_GEEDP);

		String ide_gereg=serv_empleado.getRegionEmpleado(getEmpleadoDepartamento(IDE_GEEDP).getValor("IDE_GTEMP")).getValor("IDE_GEREG");

		String fecha_ini_dec=getFechaInicioDecimoTercerSueldo(str_ide_nrdtn,ide_gereg);
		String fecha_fin_dec=getFechaFinalDecimoTercerSueldo(str_ide_nrdtn,ide_gereg);


		dou_decimo_tercer_sueldo=getSumatoriaRubro1(IDE_GEEDP, utilitario.getVariable("p_nrh_rubro_proviciones_d3"), fecha_ini_dec, fecha_fin_dec);

		if (dou_decimo_tercer_sueldo>dou_remuneracion_base){
			dou_decimo_tercer_sueldo=dou_remuneracion_base;
		}
		return utilitario.getFormatoNumero(dou_decimo_tercer_sueldo);

	}

	public boolean isVacacionesNegativo() {
	return vacacionesNegativo;
}

public void setVacacionesNegativo(boolean vacacionesNegativo) {
	this.vacacionesNegativo = vacacionesNegativo;
}

public Double getVacacionesNegativoValor() {
	return vacacionesNegativoValor;
}

public void setVacacionesNegativoValor(Double vacacionesNegativoValor) {
	this.vacacionesNegativoValor = vacacionesNegativoValor;
}

	/**
	 * Metodo devuelve el valor generado por el nro de dias acumulados a vacacion
	 * @param IDE_GEEDP
	 * @param fecha_ini_dec
	 * @param fecha_fin_dec
	 * @param tipoDecimo
	 * @return
	 */
	public String getValorDiasVacacionesAcumuladas(String IDE_GEEDP, String fecha_ini_dec, String fecha_fin_dec,int tipoDecimo){
			
			String valor="",fecha_iniTemp="",fecha_inicio="",fecha_fin="";
			
			TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+IDE_GEEDP);
			TablaGenerica tab_periodo_vacacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
					+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
					+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac "
					+ " FROM asi_vacacion "
					+ "where ide_gtemp="+tab_emp.getValor("IDE_GTEMP")+" AND ACTIVO_ASVAC=false");
	
			
			TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
					+ "order by ide_geedp asc");
			String ide_geedp_empleado="";
			//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
			//variable guarda la fecha de salida
			String fecha_finTemp="";
			fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
			//variable cuenta el numero de acciones
			int contAccionInvalida=0,contForAccionInvalida=0;
			StringBuilder str_ide_geedp = new StringBuilder();

			for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
				if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(IDE_GEEDP)==true) {
					String valorUltimoElemento="";
					valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
					if(valorUltimoElemento.equals(",")==true){
						str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
					}else {
					}

				i=tabEmpleados.getTotalFilas();
				}else{
						//Voy anidando los ides de la accion
						str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
		               // valor++;
		                if (tabEmpleados.getTotalFilas()==1) {
		   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
		   				contForAccionInvalida++;
		   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
		   					str_ide_geedp.append(",");
		                   // System.out.println("str_ide:  "+str_ide_empleado);
		   					}
		    			}	 	
				}
			}
			
			
			//Si no contiene acciones de personal luego de la liquidacion
			if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
				str_ide_geedp.append(IDE_GEEDP);
			}

			
			//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
			String a="";
			a=utilitario.getVariable("p_nrh_acciones_personal");
			String[] listaAccionesEmpleado;
			listaAccionesEmpleado=a.split(",");
			for (int i = 0; i < listaAccionesEmpleado.length; i++) {
				fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
				if (!fecha_iniTemp.equals("")) {
					i=listaAccionesEmpleado.length;
				}
			}
			System.out.println("empleado_vacacion :"+tab_emp.getValor("IDE_GTEMP"));
			String ide_asvac="";
			if (tab_periodo_vacacion.getTotalFilas()>0) {
				for (int i = 0; i < tab_periodo_vacacion.getTotalFilas(); i++) {
					fecha_inicio=tab_periodo_vacacion.getValor(i,"fecha_ingreso_asvac");
					fecha_fin=tab_periodo_vacacion.getValor(i,"fecha_finiquito_asvac");
					if (fecha_iniTemp.compareTo(fecha_inicio)==0) {
							ide_asvac=tab_periodo_vacacion.getValor(i,"ide_asvac");
							i=tab_periodo_vacacion.getTotalFilas();
					}
				}
				
				
				if (ide_asvac==null || ide_asvac.equals("") || ide_asvac.isEmpty()) {
					
					tab_periodo_vacacion= new TablaGenerica();
					tab_periodo_vacacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
							+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
							+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac,fecha_liquidacion_asvac,ide_geedp_liquidacion "
							+ " FROM asi_vacacion "
							+ "where ide_gtemp="+tab_emp.getValor("IDE_GTEMP")+" AND fecha_liquidacion_asvac is not null");	
					
					
					
					
					
					boolean band=false;
					if (tab_periodo_vacacion.getTotalFilas()>0) {
					for (int i = 0; i < tab_periodo_vacacion.getTotalFilas(); i++) {
						for (int j = 0; j < tabEmpleados.getTotalFilas(); j++) {
							if (tabEmpleados.getValor(j, "IDE_GEEDP").compareTo(tab_periodo_vacacion.getValor(i,"ide_geedp_liquidacion"))==0) {		
							band=true;
							ide_asvac=tab_periodo_vacacion.getValor(i,"ide_asvac");
							i=tab_periodo_vacacion.getTotalFilas();
							j=tabEmpleados.getTotalFilas();
							}else {
								band=false;
							}
							}
						}
					}else {
						
					} 
					
					
					
					
					
					
					
					/*if (tab_periodo_vacacion.getTotalFilas()>0) {
					for (int i = 0; i < tab_periodo_vacacion.getTotalFilas(); i++) {
						if (IDE_GEEDP.compareTo(tab_periodo_vacacion.getValor(i,"ide_geedp_liquidacion"))==0) {
								ide_asvac=tab_periodo_vacacion.getValor(i,"ide_asvac");
								i=tab_periodo_vacacion.getTotalFilas();
								}
						}
					}else {
						
					} */
						
					
				}else {
					
				}				
				
			}


			
			try {
				valor=serv_asistencia.getDatosEmpleado(tab_emp.getValor("IDE_GTEMP"),Integer.parseInt(tab_emp.getValor("IDE_GTTEM")),ide_asvac,"","","",0,false,false);
	
				} catch (Exception e) {
					System.out.println("error verVacaciones()");					}
			return utilitario.getFormatoNumero(valor);
		
		}

	/**
	 * Busca el anio activo y retorna la fila  
	 * @param IDE_GEANI
	 * @return resultado: tabla
	 */
	public TablaGenerica getAnioActivo(){
		TablaGenerica tab_anio=utilitario.consultar("SELECT * from GEN_ANIO where ACTIVO_GEANI = true");
		return tab_anio;
	}

	/**
	 * Retorna la fila del ide del anio recibido  
	 * @param IDE_GEANI
	 * @return resultado: tabla
	 */
	public TablaGenerica getAnio(String IDE_GEANI){
		TablaGenerica tab_anio=utilitario.consultar("SELECT * from GEN_ANIO where IDE_GEANI = "+IDE_GEANI);
		return tab_anio;
	}


	/**
	 * Busca un periodo de rol
	 * @param IDE_GEPRO
	 * @return resultado TablaGenerica
	 */
	public TablaGenerica getPeriodoRol(String IDE_GEPRO){
		TablaGenerica tab_per_rol=utilitario.consultar("SELECT * from GEN_PERIDO_ROL where IDE_GEPRO in ("+IDE_GEPRO+") and ACTIVO_GEPRO=TRUE");
		return tab_per_rol;
	}

	/**
	 * Busca un periodo de rol
	 * @param IDE_GEPRO
	 * @return resultado TablaGenerica
	 */
	public TablaGenerica getPeriodoRol(String IDE_GEMES,String IDE_GEANI){
		TablaGenerica tab_per_rol=utilitario.consultar("SELECT * FROM GEN_PERIDO_ROL WHERE IDE_GEMES="+IDE_GEMES+" AND IDE_GEANI="+IDE_GEANI+" and ACTIVO_GEPRO=TRUE");
		return tab_per_rol;
	}
	

	public String getDiasAcumulados(String IDE_GEEDP, String fecha_ini_dec, String fecha_fin_dec, String fecha_ingreso_emgirs){ //awbecerra
		
		System.out.println("getDiasAcumulados fecha_contrato: "+fecha_ingreso_emgirs );
		
		double dias=360;
		double valor_dias=0;
		double valor_dias_trabajados=0;
		double diasAntiguedad=pckUtilidades.CConversion.CDbl_2(utilitario.getDiferenciaFechas360(utilitario.getFecha(fecha_ingreso_emgirs), utilitario.getFecha(fecha_fin_dec)));
		valor_dias_trabajados=getSumatoriaRubro1(IDE_GEEDP, utilitario.getVariable("p_nrh_rubro_dias_trabajados"), fecha_ini_dec, fecha_fin_dec);
		System.out.println("getDiasAcumulados diasAntiguedad: "+diasAntiguedad +" valor_dias_acumulado_perdiodo:" +valor_dias_trabajados);
		
		if((diasAntiguedad==valor_dias_trabajados) || (diasAntiguedad>dias && valor_dias_trabajados==dias))
        {
            valor_dias=valor_dias_trabajados;
        }
        else if (diasAntiguedad>valor_dias_trabajados && diasAntiguedad<=dias)
        {
            valor_dias=diasAntiguedad;
        }
        else if (diasAntiguedad>dias && valor_dias_trabajados<dias)
        {
            valor_dias=dias;
        }
		
		System.out.println("getDiasAcumulados "+IDE_GEEDP +" "+utilitario.getFormatoNumero(valor_dias));
		return utilitario.getFormatoNumero(valor_dias);
	}


	/** 
	 * @param IDE_GEEDP  : EMPLEADO DEPARTAMENTO
	 * @return String   resultado
	 * Metodo que devuelve el Decimo Cuarto Sueldo de un empleado a una fecha determinada 
	 */
	public String getDecimoCuartoSueldoOK(String IDE_GEEDP, String fecha_ini_dec, String fecha_fin_dec){
		double valor_decimo_cuarto_sueldo=0;

		valor_decimo_cuarto_sueldo=getSumatoriaRubro1(IDE_GEEDP, utilitario.getVariable("p_nrh_rubro_proviciones_d4"), fecha_ini_dec, fecha_fin_dec);

		System.out.println("getDecimoCuartoSueldo "+IDE_GEEDP +" "+ fecha_ini_dec+" - "+ fecha_fin_dec);
		System.out.println("getDecimoCuartoSueldo "+IDE_GEEDP +" "+utilitario.getFormatoNumero(valor_decimo_cuarto_sueldo));
		return utilitario.getFormatoNumero(valor_decimo_cuarto_sueldo);
	}

	public String getDecimoCuartoSueldoPagado(String IDE_GEEDP, String fecha_ini_dec, String fecha_fin_dec){
		double valor_decimo_cuarto_sueldo=0;

		valor_decimo_cuarto_sueldo=getSumatoriaRubro1(IDE_GEEDP, utilitario.getVariable("p_nrh_rubro_decimo_cuarto_rol"), fecha_ini_dec, fecha_fin_dec);

		System.out.println("getDecimoCuartoSueldoPagado "+IDE_GEEDP +" "+ fecha_ini_dec+" - "+ fecha_fin_dec);
		System.out.println("getDecimoCuartoSueldoPagado "+IDE_GEEDP +" "+utilitario.getFormatoNumero(valor_decimo_cuarto_sueldo));
		return utilitario.getFormatoNumero(valor_decimo_cuarto_sueldo);
	}
	
	/** 
	 * @param IDE_GEEDP  : EMPLEADO DEPARTAMENTO
	 * @return String   resultado
	 * Metodo que devuelve el Decimo Cuarto Sueldo de un empleado a una fecha determinada 
	 */
	public String getDecimoCuartoSueldo(String IDE_GEEDP,String fecha_calculo){
		double valor_decimo_cuarto_sueldo=0;
		double SBUV=0;
		try {
			SBUV=pckUtilidades.CConversion.CDbl_2(getSalarioBasicoUnificado(utilitario.getFechaActual()));	
		} catch (Exception e) {
			// TODO: handle exception
		}

		String str_ide_nrdtn=getIdeDetalleTipoNomina(IDE_GEEDP);

		String ide_gereg=serv_empleado.getRegionEmpleado(getEmpleadoDepartamento(IDE_GEEDP).getValor("IDE_GTEMP")).getValor("IDE_GEREG");
		String fecha_ini_dec=getFechaInicioDecimoCuartoSueldo(str_ide_nrdtn,ide_gereg);
		String fecha_fin_dec=getFechaFinalDecimoCuartoSueldo(str_ide_nrdtn,ide_gereg);
		valor_decimo_cuarto_sueldo=getSumatoriaRubro1(IDE_GEEDP, utilitario.getVariable("p_nrh_rubro_proviciones_d4"), fecha_ini_dec, fecha_fin_dec);

		if (valor_decimo_cuarto_sueldo>SBUV){
			valor_decimo_cuarto_sueldo=SBUV;
		}
		return utilitario.getFormatoNumero(valor_decimo_cuarto_sueldo);
	}

	/**
	 * Metodo que devuelve la remuneracion del empleado
	 * @param IDE_GEEDP  : EMPLEADO DEPARTAMENTO
	 * @return String   resultado	 * 
	 */
	public String getRemuneracionEmpleado(String IDE_GEEDP){
		return utilitario.getFormatoNumero(getEmpleadoDepartamento(IDE_GEEDP).getValor("RMU_GEEDP"));
	}


	/**
	 * Retorna el salario Bï¿½sico a una determinada fecha
	 * @param fecha
	 * @return
	 */
	public String getSalarioBasicoUnificado(String fecha){
		TablaGenerica tab_salario=utilitario.consultar("SELECT * FROM NRH_SALARIO_BASICO WHERE  "+utilitario.getFormatoFechaSQL(fecha)+" BETWEEN FECHA_INICIO_NRSAB and FECHA_FIN_NRSAB  and ACTIVO_NRSAB=true");
		if(!tab_salario.isEmpty()){			
			return tab_salario.getValor("VALOR_SALARIO_NRSAB");
		}
		return null;
	}


	public String getFormatoFechaRubro(String str_fecha){
		int anio_actual=utilitario.getAnio(utilitario.getFechaActual());
		if (str_fecha!=null && !str_fecha.isEmpty()){
			if (str_fecha.indexOf("/")!=-1){
				String num_anios=str_fecha.substring(str_fecha.indexOf("/")+1, str_fecha.length());
				String fecha=str_fecha.substring(0,str_fecha.indexOf("/"));								
				int int_num_anios_fec_pago=0;	
				try {
					int_num_anios_fec_pago=pckUtilidades.CConversion.CInt(num_anios);
				} catch (Exception e) {
					// TODO: handle exception
				}
				fecha=(anio_actual+int_num_anios_fec_pago)+"-"+fecha;
				str_fecha=fecha;
			}else{
				String fecha=str_fecha;
				fecha=anio_actual+"-"+fecha;
				str_fecha=fecha;
			}

			return str_fecha;
		}
		return null;
	}

	public String getFechaInicioDecimoCuartoSueldo(String IDE_NRDTN,String IDE_GEREG){
		String str_fecha="";
		try {
			str_fecha=getDetalleRubro(IDE_NRDTN,utilitario.getVariable("p_nrh_rubro_decimo_cuarto_rol"),IDE_GEREG).getValor("FECHA_INICIAL_NRDER");		} catch (Exception e) {
				// TODO: handle exception
			}
		if (str_fecha==null || str_fecha.isEmpty() ){
			try {
				str_fecha= getDetalleRubro(IDE_NRDTN,utilitario.getVariable("p_nrh_rubro_decimo_cuarto_rol")).getValor("FECHA_FINAL_NRDER");		
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		str_fecha=getFormatoFechaRubro(str_fecha);
		return str_fecha;
	}
	public String getFechaFinalDecimoCuartoSueldo(String IDE_NRDTN,String IDE_GEREG){
		String str_fecha="";
		try {
			str_fecha=getDetalleRubro(IDE_NRDTN,utilitario.getVariable("p_nrh_rubro_decimo_cuarto_rol"),IDE_GEREG).getValor("FECHA_FINAL_NRDER");
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (str_fecha==null || str_fecha.isEmpty() ){
			try {
				str_fecha= getDetalleRubro(IDE_NRDTN,utilitario.getVariable("p_nrh_rubro_decimo_cuarto_rol")).getValor("FECHA_FINAL_NRDER");		
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		str_fecha=getFormatoFechaRubro(str_fecha);
		return str_fecha;
	}
	public String getFechaPagoDecimoCuartoSueldo(String IDE_NRDTN,String IDE_GEREG){
		String str_fecha="";
		try {
			str_fecha=getDetalleRubro(IDE_NRDTN,utilitario.getVariable("p_nrh_rubro_decimo_cuarto_rol"),IDE_GEREG).getValor("FECHA_PAGO_NRDER");
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (str_fecha==null || str_fecha.isEmpty() ){
			try {
				str_fecha= getDetalleRubro(IDE_NRDTN,utilitario.getVariable("p_nrh_rubro_decimo_cuarto_rol")).getValor("FECHA_FINAL_NRDER");		
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		str_fecha=getFormatoFechaRubro(str_fecha);
		return str_fecha;
	}


	public String getFechaInicioDecimoTercerSueldo(String IDE_NRDTN,String IDE_GEREG){
		String str_fecha=""; 
		try {
			str_fecha=getDetalleRubro(IDE_NRDTN,utilitario.getVariable("p_nrh_rubro_decimo_tercer_rol"),IDE_GEREG).getValor("FECHA_INICIAL_NRDER");	
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (str_fecha==null || str_fecha.isEmpty() ){
			try {
				str_fecha= getDetalleRubro(IDE_NRDTN,utilitario.getVariable("p_nrh_rubro_decimo_tercer_rol")).getValor("FECHA_INICIAL_NRDER");		
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		str_fecha=getFormatoFechaRubro(str_fecha);
		return str_fecha;
	}

	public String getFechaFinalDecimoTercerSueldo(String IDE_NRDTN,String IDE_GEREG){


		String str_fecha=""; 
		try {
			str_fecha=getDetalleRubro(IDE_NRDTN,utilitario.getVariable("p_nrh_rubro_decimo_tercer_rol"),IDE_GEREG).getValor("FECHA_FINAL_NRDER");
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (str_fecha==null || str_fecha.isEmpty() ){
			try {
				str_fecha= getDetalleRubro(IDE_NRDTN,utilitario.getVariable("p_nrh_rubro_decimo_tercer_rol")).getValor("FECHA_FINAL_NRDER");		
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		str_fecha=getFormatoFechaRubro(str_fecha);
		return str_fecha;
	}
	public String getFechaPagoDecimoTercerSueldo(String IDE_NRDTN,String IDE_GEREG){
		String str_fecha=""; 
		try {
			str_fecha=getDetalleRubro(IDE_NRDTN,utilitario.getVariable("p_nrh_rubro_decimo_tercer_rol"),IDE_GEREG).getValor("FECHA_PAGO_NRDER");		
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (str_fecha==null || str_fecha.isEmpty() ){
			try {
				str_fecha= getDetalleRubro(IDE_NRDTN,utilitario.getVariable("p_nrh_rubro_decimo_tercer_rol")).getValor("FECHA_PAGO_NRDER");		
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		str_fecha=getFormatoFechaRubro(str_fecha);
		return str_fecha;
	}



	private String getFechaContratoEmpleado(String IDE_GEEDP){
		return getEmpleadoDepartamento(IDE_GEEDP).getValor("FECHA_GEEDP");
	}



	/**
	 * @param IDE_GEEDP : IDE EMPLEADO DEPARTAMENTO PARTIDA
	 * @param fecha_fin : fecha de calculo
	 * @return resultado: String 
	 * Metodo que retorna el total de dias laborados de un empleado desde su fecha de contratacion hasta la fecha de calculo
	 */
	public String getTotalDiasLaborados(String fecha_inicio,String fecha_calculo){
		//fECHA DE INGRESO 

		try {
			if (fecha_inicio!=null){
				return ""+utilitario.getDiferenciasDeFechas(utilitario.getFecha(fecha_inicio), utilitario.getFecha(fecha_calculo));
			}		
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * @param IDE_GEEDP : IDE EMPLEADO DEPARTAMENTO PARTIDA
	 * @param fecha_fin : fecha de calculo
	 * @return resultado: String 
	 * Metodo que retorna el total de dias laborados de un empleado desde su fecha de contratacion hasta la fecha de calculo
	 */
	public String getTotalDiasAntiguedadEmp(String ide_geedp,String fecha_calculo){
		//fECHA DE INGRESO 

		try {
			if (ide_geedp!=null){
				String fecha_inicio=serv_empleado.getEmpleado(serv_nomina.getEmpleadoDepartamento(ide_geedp).getValor("IDE_GTEMP")).getValor("FECHA_INGRESO_GTEMP");
				return ""+utilitario.getDiferenciasDeFechas(utilitario.getFecha(fecha_inicio), utilitario.getFecha(fecha_calculo));
			}		
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}


	/**
	 * @param IDE_GEEDP : IDE DEL EMPLEADO DEPARTAMENTO
	 * @return TablaGenerica resultado 
	 * Metodo que retorna una TablaGenerica con los ingresos y egresos de un empleado 
	 */
	public TablaGenerica getTablaIngresosEgresosEmpleado(String IDE_GEEDP){
		TablaGenerica tab_Ingresos_egresos_emp=utilitario.consultar(getSqlIngresosEgresosEmpleado(IDE_GEEDP));
		return tab_Ingresos_egresos_emp;
	}

	/**
	 * @param IDE_GEEDP : IDE DEL EMPLEADO DEPARTAMENTO
	 * @return String resultado 
	 * Metodo que retorna el Sql de los ingresos y egresos de un empleado 
	 */
	public String getSqlIngresosEgresosEmpleado(String IDE_GTEMP){

				String sql="SELECT A.IDE_NRROL, A.DETALLE_NRTIT,A.DETALLE_GEANI,A.DETALLE_GEMES,A.TOT_INGRESOS,B.TOT_EGRESOS,(A.TOT_INGRESOS-B.TOT_EGRESOS) AS TOTAL  "
						+ "FROM (select DER.IDE_NRROL,TIR.DETALLE_NRTIT,ANI.DETALLE_GEANI,MES.DETALLE_GEMES,DER.VALOR_NRDRO AS TOT_INGRESOS  "
						+ "from NRH_ROL ROL INNER JOIN NRH_DETALLE_ROL DER ON DER.IDE_NRROL=ROL.IDE_NRROL "
						+ "INNER JOIN gen_empleados_departamento_par gedp ON gedp.ide_geedp=DER.ide_geedp "
						+ "INNER JOIN NRH_DETALLE_RUBRO DRU ON DRU.IDE_NRDER=DER.IDE_NRDER "
						+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DRU.IDE_NRRUB "
						+ "LEFT JOIN GEN_PERIDO_ROL PER ON PER.IDE_GEPRO=ROL.IDE_GEPRO "
						+ "LEFT JOIN NRH_TIPO_ROL TIR ON TIR.IDE_NRTIT=PER.IDE_NRTIT  "
						+ "LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PER.IDE_GEMES "
						+ "LEFT JOIN GEN_ANIO ANI ON ANI.IDE_GEANI=PER.IDE_GEANI "
						+ "where gedp.ide_gtemp="+IDE_GTEMP+" AND RUB.IDE_NRRUB IN (288) ORDER BY DER.IDE_NRROL ASC) A, "
						+ "(select DER.IDE_NRROL,TIR.DETALLE_NRTIT,ANI.DETALLE_GEANI,MES.DETALLE_GEMES,DER.VALOR_NRDRO AS TOT_EGRESOS "
						+ "from NRH_ROL ROL "
						+ "INNER JOIN NRH_DETALLE_ROL DER ON DER.IDE_NRROL=ROL.IDE_NRROL "
						+ "INNER JOIN gen_empleados_departamento_par gedp ON gedp.ide_geedp=DER.ide_geedp "
						+ "INNER JOIN NRH_DETALLE_RUBRO DRU ON DRU.IDE_NRDER=DER.IDE_NRDER "
						+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DRU.IDE_NRRUB "
						+ "LEFT JOIN GEN_PERIDO_ROL PER ON PER.IDE_GEPRO=ROL.IDE_GEPRO "
						+ "LEFT JOIN NRH_TIPO_ROL TIR ON TIR.IDE_NRTIT=PER.IDE_NRTIT "
						+ "LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PER.IDE_GEMES LEFT JOIN GEN_ANIO ANI ON ANI.IDE_GEANI=PER.IDE_GEANI  "
						+ "where gedp.ide_gtemp="+IDE_GTEMP
						+ " AND RUB.IDE_NRRUB IN (255) ORDER BY DER.IDE_NRROL ASC) B WHERE A.IDE_NRROL=B.IDE_NRROL ";
		return sql;
		
	}
		
		

		
		
		
		
		
		
		/*String sql="SELECT A.IDE_NRROL, " +
				"A.DETALLE_NRTIT," +
				"A.DETALLE_GEANI," +
				"A.DETALLE_GEMES," +
				"A.TOT_INGRESOS," +
				"B.TOT_EGRESOS " +
				"FROM " +
				"(select DER.IDE_NRROL,TIR.DETALLE_NRTIT,ANI.DETALLE_GEANI,MES.DETALLE_GEMES,DER.VALOR_NRDRO AS TOT_INGRESOS " +
				"from NRH_ROL ROL " +
				"INNER JOIN NRH_DETALLE_ROL DER ON DER.IDE_NRROL=ROL.IDE_NRROL "+
			    "INNER JOIN gen_empleados_departamento_par gedp ON gedp.ide_geedp=DER.ide_geedp " +//ABECERRA
				"INNER JOIN NRH_DETALLE_RUBRO DRU ON DRU.IDE_NRDER=DER.IDE_NRDER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DRU.IDE_NRRUB " +
				"LEFT JOIN GEN_PERIDO_ROL PER ON PER.IDE_GEPRO=ROL.IDE_GEPRO " +
				"LEFT JOIN NRH_TIPO_ROL TIR ON TIR.IDE_NRTIT=PER.IDE_NRTIT " +
				"LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PER.IDE_GEMES " +
				"LEFT JOIN GEN_ANIO ANI ON ANI.IDE_GEANI=PER.IDE_GEANI " +
				//"where DER.ide_geedp="+IDE_GEEDP+" " +
				"where gedp.ide_gtemp="+IDE_GEEDP+" " +//ABECERRA filtra por empleado no por accion de personal
				//total ingresos
				"AND RUB.IDE_NRRUB IN ("+utilitario.getVariable("p_nrh_rubro_total_ingresos")+") " +
				"ORDER BY DER.IDE_NRROL ASC) A, "+
				"(select DER.IDE_NRROL,TIR.DETALLE_NRTIT,ANI.DETALLE_GEANI,MES.DETALLE_GEMES,DER.VALOR_NRDRO AS TOT_EGRESOS " +
				"from NRH_ROL ROL " +
				"INNER JOIN NRH_DETALLE_ROL DER ON DER.IDE_NRROL=ROL.IDE_NRROL " +
				"INNER JOIN gen_empleados_departamento_par gedp ON gedp.ide_geedp=DER.ide_geedp " +//ABECERRA
				"INNER JOIN NRH_DETALLE_RUBRO DRU ON DRU.IDE_NRDER=DER.IDE_NRDER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DRU.IDE_NRRUB " +
				"LEFT JOIN GEN_PERIDO_ROL PER ON PER.IDE_GEPRO=ROL.IDE_GEPRO " +
				"LEFT JOIN NRH_TIPO_ROL TIR ON TIR.IDE_NRTIT=PER.IDE_NRTIT " +
				"LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PER.IDE_GEMES " +
				"LEFT JOIN GEN_ANIO ANI ON ANI.IDE_GEANI=PER.IDE_GEANI " +
				//"where DER.ide_geedp="+IDE_GEEDP+" " +
				"where gedp.ide_gtemp="+IDE_GEEDP+" " +//ABECERRA
				// total egresos
				"AND RUB.IDE_NRRUB IN ("+utilitario.getVariable("p_nrh_rubro_total_egresos")+") " +
				"ORDER BY DER.IDE_NRROL ASC) B " +
				"WHERE A.IDE_NRROL=B.IDE_NRROL";
System.out.println("getSqlIngresosEgresosEmpleado: "+sql);
		return sql;
	}*/

	//	public TablaGenerica getSriDetalleImpuestoRenta(String IDE_SRIMR,double base_imponible_proy){
	//		TablaGenerica tab_detalle_imp_renta=utilitario.consultar("select * from SRI_DETALLE_IMPUESTO_RENTA where IDE_SRIMR="+IDE_SRIMR+" " +
	//				"and "+base_imponible_proy+" BETWEEN FRACCION_BASICA_SRDIR and EXCESO_HASTA_SRDIR");
	//
	//		return tab_detalle_imp_renta;
	//	}

	public TablaGenerica getSriDetalleImpuestoRenta(String IDE_SRIMR,double base_imponible_proy,int factor_multiplicador_discapacitados){
		TablaGenerica tab_detalle_imp_renta=utilitario.consultar("SELECT * FROM ( " +
				"select IDE_SRDIR,IDE_SRIMR, " +
				"FRACCION_BASICA_SRDIR * "+factor_multiplicador_discapacitados+" as FRACCION_BASICA_SRDIR , " +
				"EXCESO_HASTA_SRDIR * "+factor_multiplicador_discapacitados+" as EXCESO_HASTA_SRDIR, " +
				"IMP_FRACCION_SRDIR , " +
				"IMP_FRACCION_EXCEDENTE_SRDIR " +
				"from SRI_DETALLE_IMPUESTO_RENTA where IDE_SRIMR="+IDE_SRIMR+" " +
				")a " +
				"where "+base_imponible_proy+" BETWEEN a.FRACCION_BASICA_SRDIR and a.EXCESO_HASTA_SRDIR	");
		return tab_detalle_imp_renta;
	}

	public TablaGenerica getSriDetalleImpuestoRenta(String IDE_SRIMR){
		TablaGenerica tab_detalle_imp_renta=utilitario.consultar("select * from SRI_DETALLE_IMPUESTO_RENTA where IDE_SRIMR="+IDE_SRIMR);

		return tab_detalle_imp_renta;
	}

	public TablaGenerica getSriImpuestoRenta(String FECHA_ROL){
		TablaGenerica tab_imp_renta=new TablaGenerica();
		tab_imp_renta=utilitario.consultar("select * from SRI_IMPUESTO_RENTA " +
				"where TO_DATE('"+FECHA_ROL+"', 'yyyy-mm-dd') " +
				"BETWEEN FECHA_INICIO_SRIMR and FECHA_FIN_SRIMR ");
		return tab_imp_renta;
	}



	
	
	
	public void calcularRentaEmpleados(String IDE_NRROL){

		if (IDE_NRROL==null || IDE_NRROL.isEmpty()){
			return;
		}

		// obtenemos el ide de tipo de nomina y el ide de periodo de rol
		TablaGenerica tab_rol=getRol(IDE_NRROL);
		String ide_nrdtn=tab_rol.getValor("IDE_NRDTN");
		String ide_gepro=tab_rol.getValor("IDE_GEPRO");

		// obtenemos el mes y la fecha de inicio de periodo de rol
		TablaGenerica tab_periodo_rol=getPeriodoRol(ide_gepro);
		String ide_gemes=tab_periodo_rol.getValor("IDE_GEMES");
		String fecha_ini_gepro=tab_periodo_rol.getValor("FECHA_INICIAL_GEPRO");

		// obtenemos el ide_srimr de la tabla sri impuesto a la renta de acuerdo a la fecha de inicio de periodo
		String ide_srimr=getSriImpuestoRenta(fecha_ini_gepro).getValor("IDE_SRIMR");

		// validamos que exista configuarcion de detalles de la tabla del sri para retenciones
		if (getSriDetalleImpuestoRenta(ide_srimr).getTotalFilas()==0){
			return;
		}


		// actualiza la tabla proyeccion de ingresos de los empleados 
		registrarProyeccionIngresosEmpleados(IDE_NRROL);


		// CONFIGURAMOS FECHA INICIO DE CALCULO Y FECHA FIN DE CALCULO DE ACUMULADOS PARA CALCULO DE
		// APORTACIONES acumulado Y TOTAL RETENIDO acumulado 

		// obtenemos el año
		String anio=getAnio(tab_periodo_rol.getValor("IDE_GEANI")).getValor("DETALLE_GEANI");

		// fecha inicio de calculo(aa/mm/dd), siempre inicia (1 de enero del anio del periodo) 
		String str_fecha_ini_calculo=anio+"-1-1";

		String str_fecha_fin_calculo="";
		if (pckUtilidades.CConversion.CInt(ide_gemes)==12){
			// fecha fin de calculo(aa/mm/dd), si el mes es 12 o Dicimebre entonces (31 de diciembre del anio del periodo)
			str_fecha_fin_calculo=anio+"-12-31";
		}else{
			// si el mes es menor que 12 entonces la fecha fin de calculo va hasta el mes 
			// y dia de la fecha de inicio de periodo de rol
			str_fecha_fin_calculo=anio+"-"+(utilitario.getMes(fecha_ini_gepro)-1)+"-"+utilitario.getDia(fecha_ini_gepro);
		}

		// obtenemos la tabla de los empleados del rol con el rubro de aportaciones y total a recibir del mes del periodo 
		String sql_emp_renta="select * from ( "+  
				""+getTablaAportacionesTotalRecibir(IDE_NRROL).getSql()+" " +
				")a where a.imp_renta_mensual<=0 ";
		
		TablaGenerica tab_aportaciones_empleados=utilitario.consultar(sql_emp_renta);

		System.out.println("tab emp a retener "+tab_aportaciones_empleados.getSql());
		System.out.println("tab emp a retener filas "+tab_aportaciones_empleados.getTotalFilas());
		

		String ide_geedp="";
		String discapacitado_gtemp="";
		String ide_gtemp="";
		String edad="";
		double porcentaje_beneficio_aplica=100;
		double porcentaje_discapacidad=0;

		// recorremos la tabla de los empleados con sus aportaciones y total a recibir del mes del periodo de rol
		// para calcular la renta mensual de empleado en empleado
		for (int i = 0; i < tab_aportaciones_empleados.getTotalFilas(); i++) {
			ide_geedp=tab_aportaciones_empleados.getValor(i, "IDE_GEEDP");
			double dou_tot_recibir=0;
			double dou_tot_egresos=0;
			double dou_irm=0;
			if (ide_geedp.equalsIgnoreCase("822")){
				System.out.println("edad "+tab_aportaciones_empleados.getValor(i,"EDAD"));
				
			}
			try {
				dou_tot_recibir=pckUtilidades.CConversion.CDbl_2(tab_aportaciones_empleados.getValor(i, "TOT_RECIBIR"));
				dou_tot_egresos=pckUtilidades.CConversion.CDbl_2(tab_aportaciones_empleados.getValor(i, "TOT_EGRESOS"));
				dou_irm=pckUtilidades.CConversion.CDbl_2(tab_aportaciones_empleados.getValor(i, "IMP_RENTA_MENSUAL"));
			} catch (Exception e) {
				// TODO: handle exception
			}

			BigDecimal big_irm=new BigDecimal(dou_irm);
			big_irm=big_irm.setScale(2, RoundingMode.HALF_UP);

			if (ide_geedp.equalsIgnoreCase("822")){
				System.out.println("t. rec antes "+dou_tot_recibir);
				System.out.println("t. egr antes "+dou_tot_egresos);
				System.out.println("imp antes "+dou_irm);
				System.out.println("ide_geedp "+ide_geedp);
			}

			if (dou_irm<=0){
				// ACTUALIZO EL TOTAL A RECIBIR
				dou_tot_recibir=dou_tot_recibir+pckUtilidades.CConversion.CDbl_2(big_irm+"");

				// ACTUALIZO EL TOTAL EGRESOS
				dou_tot_egresos=dou_tot_egresos-pckUtilidades.CConversion.CDbl_2(big_irm+"");

				// PROCESO DE CALCULO DE RENTA DE EMPLEADO

				discapacitado_gtemp=tab_aportaciones_empleados.getValor(i, "DISCAPACITADO_GTEMP");
				ide_gtemp=tab_aportaciones_empleados.getValor(i, "IDE_GTEMP");
				edad=tab_aportaciones_empleados.getValor(i, "EDAD");
				porcentaje_discapacidad=0;
				porcentaje_beneficio_aplica=100;
				try {
					porcentaje_discapacidad=pckUtilidades.CConversion.CDbl_2(tab_aportaciones_empleados.getValor(i, "PORCENTAJE_GTDIE"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				double dou_seguro_social_proyeccion=0;
				try {
					dou_seguro_social_proyeccion=pckUtilidades.CConversion.CDbl_2(tab_aportaciones_empleados.getValor(i, "seguro_social_con_rmu"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				//  1.- CALCULO DE LA BASE IMPONIBLE 1
				double dou_base_imponible=0;

				// obtengo el total de ingresos proyectado y mensual del empleado
				TablaGenerica tab_det_proy_ing=getSriDetalleProyeccionIngresos(getSriProyeccionIngresos(ide_srimr, ide_gtemp).getValor("IDE_SRPRI"));
				double dou_tot_ing_proy=0;
				double dou_tot_ing_mensual=0;

 
				for (int j = 0; j < tab_det_proy_ing.getTotalFilas(); j++) {
					if (ide_gemes.equalsIgnoreCase(tab_det_proy_ing.getValor(j, "IDE_GEMES"))){
						try {
							dou_tot_ing_mensual=pckUtilidades.CConversion.CDbl_2(tab_det_proy_ing.getValor(j, "VALOR_SRDPI"));
						} catch (Exception e) {
							// TODO: handle exception
						}
					}else{
						try {
							dou_tot_ing_proy=dou_tot_ing_proy+pckUtilidades.CConversion.CDbl_2(tab_det_proy_ing.getValor(j, "VALOR_SRDPI"));
						} catch (Exception e) {
							// TODO: handle exception
							dou_tot_ing_proy=dou_tot_ing_proy+0;
						}
					}
				}
				// calculo la base imponible anual
				dou_base_imponible=dou_tot_ing_mensual+dou_tot_ing_proy;
				
				// 2.- CALCULO DEDUCCION IESS
				double dou_deduccion_iess=0;

				// obtengo el aporte personal del mes
				double dou_aporte_personal_del_mes=pckUtilidades.CConversion.CDbl_2(tab_aportaciones_empleados.getValor(i, "VALOR_NRDRO"));

				// obtengo el total de aportes acumulado
				double dou_tot_aportes_acumul=getSumatoriaRubro1(ide_geedp,utilitario.getVariable("p_nrh_rubro_aportes_personales"), str_fecha_ini_calculo,str_fecha_fin_calculo);

				// calculo de proyeccion de aportes 
				double dou_proyeccion_aportes=dou_seguro_social_proyeccion*(12-pckUtilidades.CConversion.CInt(ide_gemes));

				// FORMULA DEDUCCION IESS (aportes_acumu + aporte del mes + proyeccion de aportes)
				if (ide_geedp.equalsIgnoreCase("822")){
					System.out.println("dou_tot_aportes_acumul "+dou_tot_aportes_acumul);
					System.out.println("dou_aporte_personal_del_mes "+dou_aporte_personal_del_mes);
					System.out.println("dou_proyeccion_aportes "+dou_proyeccion_aportes);
				}
				dou_deduccion_iess=dou_tot_aportes_acumul+dou_aporte_personal_del_mes+dou_proyeccion_aportes;
				System.out.println("dou_deduccion_iess "+dou_deduccion_iess);
				// 3.- CALCULO DEDUCCION DE GASTOS
				double dou_deduccion_gastos=0;
				TablaGenerica tab_deducibles=utilitario.consultar("select '0' as ide,sum(VALOR_DEDUCIBLE_SRDEE) as total_deducibles " +
						"from SRI_DEDUCIBLES_EMPLEADO " +
						"where IDE_GTEMP="+ide_gtemp+" " +
						"and IDE_SRDED in (select IDE_SRDED from SRI_DEDUCIBLES where IDE_SRIMR ="+ide_srimr+")");
				if (tab_deducibles.getTotalFilas()>0){
					try {
						dou_deduccion_gastos=pckUtilidades.CConversion.CDbl_2(tab_deducibles.getValor("total_deducibles"));
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

				System.out.println("dou_deduccion_gastos "+dou_deduccion_gastos);
				
				// verificao si el empleado es de tercera edad o discapacitado y aplico el factor multiplicador
				double int_edad=0;
				try {
					int_edad=pckUtilidades.CConversion.CDbl_2(edad);
					
				} catch (Exception e) {
					// TODO: handle exception
						System.out.println("entro exception "+int_edad+ " error "+e );
				}
				double dou_deduccion_gasto_discapacitado=0;
				
					if ((discapacitado_gtemp!=null && !discapacitado_gtemp.isEmpty() 
						&& discapacitado_gtemp.equalsIgnoreCase("true")) || int_edad>=65){
					// CALCULO DE DEDUCCION DE GASTOS PARA DISCAPACITADO
						
					int factor_multi_disc=1;
					try {
						factor_multi_disc=pckUtilidades.CConversion.CInt(utilitario.getVariable("p_factor_multiplicador_renta_discapacitados"));	
					} catch (Exception e) {
						// TODO: handle exception
					}

					// cojo el minimo excedente de la tabla de retenciones del sri 

					double dou_fraccion_basica_desgravada=getFraccionBasicaDesgravadaImpuestoRenta(ide_srimr);
					// multiplico el minimo excedente por el factor multiplicador de los discapacitados
					try {
						dou_deduccion_gasto_discapacitado=dou_fraccion_basica_desgravada*factor_multi_disc;
					} catch (Exception e) {
						// TODO: handle exception
					}
					/*
					if (ide_geedp.equalsIgnoreCase("93")){

									System.out.println("ide geedp "+ide_geedp);
									System.out.println("porcentaje discapacidad "+porcentaje_discapacidad);
									System.out.println("edad "+edad);
									System.out.println("minimo excedente * factor multiplicador "+dou_deduccion_gasto_discapacitado);
						}
					*/
					TablaGenerica tab_btd=utilitario.consultar("select * from SRI_BENEFICIO_TRIBUTARIO_DISC " +
							"where "+porcentaje_discapacidad+" BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD");	
					/*
							if (ide_geedp.equalsIgnoreCase("93")){

								System.out.println("sql porcentaje de discapacidad  ");
								tab_btd.imprimirSql();
							}
							*/

					if (tab_btd.getTotalFilas()>0){
						try {
							porcentaje_beneficio_aplica=pckUtilidades.CConversion.CDbl_2(tab_btd.getValor("PORCENTAJE_APLICA_SRBTD"));	
						} catch (Exception e) {
							// TODO: handle exception
							System.out.println("entre exception porcentaje beneficio "+e);
						}
					}
						//			System.out.println("porcentaje beneficio aplica "+porcentaje_beneficio_aplica);

					dou_deduccion_gasto_discapacitado=(dou_deduccion_gasto_discapacitado*porcentaje_beneficio_aplica)/100;
					//System.out.println("beneficio dou_deduccion_gastos "+dou_deduccion_gastos);
					//System.out.println("beneficio dou_deduccion_gasto_discapacitado "+dou_deduccion_gasto_discapacitado);

					// sumo a la deduccion de gastos mas la deduccion de gastos de discapacitado o tercera edad segun la ley 
					dou_deduccion_gastos=dou_deduccion_gastos+dou_deduccion_gasto_discapacitado;
					//				System.out.println("beneficio tributario "+dou_deduccion_gasto_discapacitado);
				}

					
				//ESTA SI ME SIRVE	
				// 4.- CALCULO DE BASE DE CALCULO FINAL (base imponible menos deduccion iess menos deduccion gastos)
				double dou_base_calculo_final=0;
				dou_base_calculo_final=dou_base_imponible-dou_deduccion_iess-dou_deduccion_gastos;


				// 5.- verifico en la tabla del sri (a que fila corresponde nuestro base calculo final)
				TablaGenerica tab_det_imp_renta=getSriDetalleImpuestoRenta(ide_srimr, dou_base_calculo_final,1);
				double dou_fraccion_basica=0;
				double dou_por_frac_exc=0;
				double dou_imp_fraccion=0;
				try {
					dou_fraccion_basica=pckUtilidades.CConversion.CDbl_2(tab_det_imp_renta.getValor("FRACCION_BASICA_SRDIR"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					dou_por_frac_exc=pckUtilidades.CConversion.CDbl_2(tab_det_imp_renta.getValor("IMP_FRACCION_EXCEDENTE_SRDIR"));
				} catch (Exception e) {
					// TODO: handle exceptionoim
				}
				try {
					dou_imp_fraccion=pckUtilidades.CConversion.CDbl_2(tab_det_imp_renta.getValor("IMP_FRACCION_SRDIR"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				// 6.- CALCULO LA DIF DE SUELDO BASICO
				double dou_dif_sueldo_basico=dou_base_calculo_final-dou_fraccion_basica;

				// 7.- CALCULO PORCENTAJE DEL EXCEDENTE
				double dou_imp_exc=(dou_dif_sueldo_basico*dou_por_frac_exc)/100;

				// 8.- CALCULOD EL IMPUESTO ANUAL
				double dou_imp_renta_anual=dou_imp_exc+dou_imp_fraccion;

				// 9.- CALCULO DEL IMPUESTO MENSUAL

				// obtengo el total retenido acumulado
				double dou_tot_ret_acum=getSumatoriaRubro1(ide_geedp,utilitario.getVariable("p_nrh_rubro_impuesto_renta_mensual"), str_fecha_ini_calculo,str_fecha_fin_calculo);
				
				// calcula el valor de rentencion mensual
				double dou_imp_renta_mensual=0;
				if (pckUtilidades.CConversion.CInt(ide_gemes)<12){
					dou_imp_renta_mensual=(dou_imp_renta_anual-dou_tot_ret_acum)/((12-pckUtilidades.CConversion.CInt(ide_gemes))+1);
				}else{
					dou_imp_renta_mensual=(dou_imp_renta_anual-dou_tot_ret_acum)/1;
				}

				if (ide_geedp.equalsIgnoreCase("822")){
					System.out.println("IDE GEEDP EMPLEADO ***** "+ide_geedp);
					System.out.println("edad ***** "+edad);

					System.out.println("total ing proyectado "+dou_tot_ing_proy);
					System.out.println("total ing mensual "+dou_tot_ing_mensual);
					System.out.println("base imponible "+dou_base_imponible);
					System.out.println("aportes acum "+dou_tot_aportes_acumul);
					System.out.println("deduccion iess "+dou_deduccion_iess);
					System.out.println("deduccion gastos "+dou_deduccion_gastos);
					System.out.println("deduccion gastos discapacitado "+dou_deduccion_gasto_discapacitado);

					System.out.println("base calculo final impuesto renta "+dou_base_calculo_final);
					System.out.println("excedente "+dou_dif_sueldo_basico);
					System.out.println("porcentaje "+dou_imp_exc);
					System.out.println("IMPUESTO ANUAL "+dou_imp_renta_anual);
					System.out.println("RETENIDO ACUMULADO "+dou_tot_ret_acum);
					System.out.println("IMPUESTO RENTA MENSUAL "+dou_imp_renta_mensual);
				}

				if (dou_imp_renta_mensual<0){
					dou_imp_renta_mensual=0;
				}

				BigDecimal big_irm1=new BigDecimal(dou_imp_renta_mensual);
				big_irm1=big_irm1.setScale(2, RoundingMode.HALF_UP);

				//ACTUALIZO EL RUBRO IMPUESTO A LA RENTA PROY EN EL DETALLE DE ROL DEL EMPLEADO 

				utilitario.getConexion().agregarSqlPantalla("update NRH_DETALLE_ROL set valor_nrdro="+big_irm1+" where IDE_NRROL="+IDE_NRROL+" and IDE_GEEDP="+ide_geedp+" " +
						"and IDE_NRDER =  (select IDE_NRDER from NRH_DETALLE_RUBRO where IDE_NRDTN="+ide_nrdtn+" and IDE_NRRUB="+utilitario.getVariable("p_nrh_rubro_impuesto_renta_mensual")+")");

				if (ide_geedp.equalsIgnoreCase("123")){
					System.out.println("tot recibir antes "+dou_tot_recibir);
				}

				// ACTUALIZO EL TOTAL A RECIBIR
				dou_tot_recibir=dou_tot_recibir-pckUtilidades.CConversion.CDbl_2(big_irm1+"");

				BigDecimal big_tot_recibir1=new BigDecimal(dou_tot_recibir);
				big_tot_recibir1=big_tot_recibir1.setScale(2, RoundingMode.HALF_UP);

				if (ide_geedp.equalsIgnoreCase("123")){
					System.out.println("tot recibir despues "+dou_tot_recibir);
				}

				utilitario.getConexion().agregarSqlPantalla("update NRH_DETALLE_ROL set valor_nrdro="+big_tot_recibir1+" where IDE_NRROL="+IDE_NRROL+" and IDE_GEEDP="+ide_geedp+" " +
						"and IDE_NRDER =  (select IDE_NRDER from NRH_DETALLE_RUBRO where IDE_NRDTN="+ide_nrdtn+" and IDE_NRRUB="+utilitario.getVariable("p_nrh_rubro_valor_recibir")+")");

				if (ide_geedp.equalsIgnoreCase("123")){
					System.out.println("tot egersos antes "+dou_tot_egresos);
				}

				// ACTUALIZO EL TOTAL EGRESOS
				dou_tot_egresos=dou_tot_egresos+pckUtilidades.CConversion.CDbl_2(big_irm1+"");
				BigDecimal big_tot_egresos1=new BigDecimal(dou_tot_egresos);
				big_tot_egresos1=big_tot_egresos1.setScale(2, RoundingMode.HALF_UP);

				if (ide_geedp.equalsIgnoreCase("123")){
					System.out.println("tot egresos despues "+dou_tot_egresos);
				}
				utilitario.getConexion().agregarSqlPantalla("update NRH_DETALLE_ROL set valor_nrdro="+big_tot_egresos1+" where IDE_NRROL="+IDE_NRROL+" and IDE_GEEDP="+ide_geedp+" " +
						"and IDE_NRDER =  (select IDE_NRDER from NRH_DETALLE_RUBRO where IDE_NRDTN="+ide_nrdtn+" and IDE_NRRUB="+utilitario.getVariable("p_nrh_rubro_total_egresos")+")");
			}

		}

		if (utilitario.getConexion().ejecutarListaSql().isEmpty()){
			utilitario.agregarMensaje("se guardo correctamente", "");
			utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_RENTA_NRROL=1 where IDE_NRROL="+IDE_NRROL);
		}


	}
	
	
	
	
	
	
	
	
	public void calcularRentaEmpleadosLiquidacion(String IDE_NRROL){

		if (IDE_NRROL==null || IDE_NRROL.isEmpty()){
			return;
		}

		// obtenemos el ide de tipo de nomina y el ide de periodo de rol
		TablaGenerica tab_rol=getRol(IDE_NRROL);
		String ide_nrdtn=tab_rol.getValor("IDE_NRDTN");
		String ide_gepro=tab_rol.getValor("IDE_GEPRO");

		// obtenemos el mes y la fecha de inicio de periodo de rol
		TablaGenerica tab_periodo_rol=getPeriodoRol(ide_gepro);
		String ide_gemes=tab_periodo_rol.getValor("IDE_GEMES");
		String fecha_ini_gepro=tab_periodo_rol.getValor("FECHA_INICIAL_GEPRO");

		// obtenemos el ide_srimr de la tabla sri impuesto a la renta de acuerdo a la fecha de inicio de periodo
		String ide_srimr=getSriImpuestoRenta(fecha_ini_gepro).getValor("IDE_SRIMR");

		// validamos que exista configuarcion de detalles de la tabla del sri para retenciones
		if (getSriDetalleImpuestoRenta(ide_srimr).getTotalFilas()==0){
			return;
		}


		// actualiza la tabla proyeccion de ingresos de los empleados 
		registrarProyeccionIngresosEmpleados(IDE_NRROL);


		// CONFIGURAMOS FECHA INICIO DE CALCULO Y FECHA FIN DE CALCULO DE ACUMULADOS PARA CALCULO DE
		// APORTACIONES acumulado Y TOTAL RETENIDO acumulado 

		// obtenemos el año
		String anio=getAnio(tab_periodo_rol.getValor("IDE_GEANI")).getValor("DETALLE_GEANI");

		// fecha inicio de calculo(aa/mm/dd), siempre inicia (1 de enero del anio del periodo) 
		String str_fecha_ini_calculo=anio+"-1-1";

		String str_fecha_fin_calculo="";
		if (pckUtilidades.CConversion.CInt(ide_gemes)==12){
			// fecha fin de calculo(aa/mm/dd), si el mes es 12 o Dicimebre entonces (31 de diciembre del anio del periodo)
			str_fecha_fin_calculo=anio+"-12-31";
		}else{
			// si el mes es menor que 12 entonces la fecha fin de calculo va hasta el mes 
			// y dia de la fecha de inicio de periodo de rol
			str_fecha_fin_calculo=anio+"-"+(utilitario.getMes(fecha_ini_gepro)-1)+"-"+utilitario.getDia(fecha_ini_gepro);
		}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		TablaGenerica tab_empleados_departamento=utilitario.consultar(getSqlEmpleadosRolLiquidacionImpuestoRenta(IDE_NRROL));
	
		for (int  z= 0; z < tab_empleados_departamento.getTotalFilas(); z++) {
			
		String ide_geedp ="";
		ide_geedp=tab_empleados_departamento.getValor(z,"IDE_GEEDP");
		
		TablaGenerica tabEmpleadosDepartamento=utilitario.consultar("select * from gen_empleados_departamento_par where ide_geedp in("+ide_geedp+")");
		TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tabEmpleadosDepartamento.getValor("ide_gtemp")+") "
				+ "order by ide_geedp desc");
		String ide_geedp_empleado="";
		//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
		//variable guarda la fecha de salida
		String fecha_finTemp="";
		fecha_finTemp=tabEmpleadosDepartamento.getValor("fecha_finctr_geedp");
		//variable cuenta el numero de acciones
		int contAccionInvalida=0,contForAccionInvalida=0;
		StringBuilder str_ide_geedp = new StringBuilder();

		for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
			if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(ide_geedp)) {
				contAccionInvalida++;
			}
						
				if (contAccionInvalida>0) {
					
					if (contAccionInvalida>1) {
						
					
					//Voy anidando los ides de la accion
					str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
	               // valor++;
	                if (tabEmpleados.getTotalFilas()==1) {
	   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
	   				contForAccionInvalida++;
	   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
	   					str_ide_geedp.append(",");
	                   // System.out.println("str_ide:  "+str_ide_empleado);
	   					}
	    			}
	   			 	
				}
				contAccionInvalida++;
			}
				if (i==(tabEmpleados.getTotalFilas()-1)) {
					String valorUltimoElemento="";
					valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
					if(valorUltimoElemento.equals(",")==true){
						str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));						}				 
		}
		
		}
		
		//Si no contiene acciones de personal luego de la liquidacion
		if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
			str_ide_geedp.append(ide_geedp);
		}
		

		TablaGenerica tabAccionesEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_geedp not in("+str_ide_geedp.toString()+")  "
				+ "and ide_gtemp in("+tabEmpleadosDepartamento.getValor("ide_gtemp")+")");

	
		
		int cont=0;
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < tabAccionesEmpleados.getTotalFilas(); i++) {
		  
		  		//Voy anidando los ides de la accion
		          	    str.append(tabAccionesEmpleados.getValor(i, "IDE_GEEDP"));
		               // valor++;
		                if (tabAccionesEmpleados.getTotalFilas()==1) {
		   			}else if (cont<=tabAccionesEmpleados.getTotalFilas()) {
		   					cont++;
		   					if(cont<(tabAccionesEmpleados.getTotalFilas())){
		                    str.append(",");
		                   // System.out.println("str_ide:  "+str_ide_empleado);
		   					}
		    			}
		   			 		         		
		}
		int mes=utilitario.getMes(fecha_finTemp);
		TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_finTemp)+"%' ");
		String anioConsulta=tabAnio.getValor("IDE_GEANI");
		//TablaGenerica rol=utilitario.consultar("select * from gen_perido_rol where ide_gepro="+ide_gepro);
		//String ide_mes="",ide_geani="";
		//ide_mes=rol.getValor("IDE_GEMES");
		//ide_geani=rol.getValor("IDE_GEANI");
		TablaGenerica tab_detalle_rol=null;
		int fondo=0;
		
		TablaGenerica tabLiquidacion=utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
				+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
				+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
				+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
				+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
				+ "where DRO.IDE_GEEDP in ("+str.toString()+") "
				+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+mes+" "
				//+ "and RUB.ide_nrrub in("+utilitario.getVariable("p_nrh_rubro_fondreser_acum_ante")+") "
				//+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) "
				+ "order by RUB.ide_nrrub asc");
		boolean ejecutoRol=false;
		if (tabLiquidacion.getTotalFilas()>0) {
			ejecutoRol=true;
		}else {
			ejecutoRol=false;
		}
		
		
		tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
				+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
				+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
				+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
				+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
				+ "where DRO.IDE_GEEDP in ( "
				+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
				+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str_ide_geedp.toString()+")) and ide_gttem !=3  "
				+ ")  "
				+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+mes+" "
				+ "and RUB.ide_nrrub in("+utilitario.getVariable("p_nrh_rubro_fondreser_acum_ante")+") "
				+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) "
				+ "order by RUB.ide_nrrub asc");

		if (tab_detalle_rol.getTotalFilas()>0) {
	   // si no contiene valores
			/*int valor=0;
			if (tab_detalle_rol.getValor("valor_nrdro")==null || tab_detalle_rol.getValor("valor_nrdro").equals("") || tab_detalle_rol.getValor("valor_nrdro").isEmpty()){
			}else{	
			valor=Integer.parseInt(tab_detalle_rol.getValor("valor_nrdro"));}
			System.out.println("Empleados coh ");*/
			}else {
			//Consulto el siguiente mes
			int sumaMes=0,restaMes=0;
			//Metodo para busqueda de remuneracion en rol de siguiente mes
			if (mes==1) {
				mes=12;
				restaMes=0;
				tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+(utilitario.getAnio(fecha_finTemp)-1)+"%'");
				anio=tabAnio.getValor("IDE_GEANI");
				}else {
					mes=utilitario.getMes(fecha_finTemp);
					restaMes=1;
					tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+(utilitario.getAnio(fecha_finTemp))+"%'");
					anio=tabAnio.getValor("IDE_GEANI");
					mes=mes-restaMes;
				}
			
		 tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
					+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
					+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
					+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
					+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
					+ "where DRO.IDE_GEEDP in ( "
					+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
					+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str_ide_geedp.toString()+")) and ide_gttem !=3  "
					+ ")  "
					+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+(mes)+" "
					+ "and RUB.ide_nrrub in("+utilitario.getVariable("p_nrh_rubro_impuesto_renta_mensual")+") "
					+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) "
					+ "order by RUB.ide_nrrub asc");

			if (tab_detalle_rol.getTotalFilas()>0) {
				double valorIngresos=0.0;
				int valor=0;
				int diferenciaDias=0;
				Double  total=0.0;
				String fecha_inicio="";
				if (tab_detalle_rol.getValor("valor_nrdro")==null || tab_detalle_rol.getValor("valor_nrdro").equals("") || tab_detalle_rol.getValor("valor_nrdro").isEmpty()){
				valorIngresos=0.0;
				}else{
    				valorIngresos=Double.parseDouble(utilitario.getFormatoNumero((Double.parseDouble(tab_detalle_rol.getValor("valor_nrdro"))/30),2));
					fecha_inicio=utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-01";
					if (utilitario.getDia(fecha_finTemp)==31) {
						diferenciaDias=getDias360(fecha_inicio, utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-30");	
					}else if (utilitario.getDia(fecha_finTemp)==29 && utilitario.getMes(fecha_finTemp)==2) {
						diferenciaDias=getDias360(fecha_inicio, utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-30");	
					}else{
						diferenciaDias=getDias360(fecha_inicio, fecha_finTemp);}
				}
				   total=valorIngresos*diferenciaDias;
				
				   
				   tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
							+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
							+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
							+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
							+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
							+ "where DRO.IDE_GEEDP in ("+tab_empleados_departamento.getValor(z,"IDE_GEEDP")+")  "
							+ "and RUB.ide_nrrub in("+utilitario.getVariable("p_nrh_rubro_impuesto_renta_mensual")+") "
							+ " AND ROL.IDE_NRROL IN("+IDE_NRROL+")  "
							+ "order by RUB.ide_nrrub asc");
				   
				   
				   utilitario.getConexion().ejecutarSql("UPDATE NRH_DETALLE_ROL set VALOR_NRDRO="+utilitario.getFormatoNumero(total, 2)+" where IDE_NRDRO="+tab_detalle_rol.getValor("IDE_NRDRO")+" AND IDE_NRROL IN("+IDE_NRROL+")");
					System.out.println("ide:     "+tab_detalle_rol.getValor("IDE_NRDRO")+"   actualizados: "+ tab_empleados_departamento.getValor(z,"IDE_GEEDP") +"  valor:   "+total);	
		
			}else {}
				//Si no contiene valores generadoscontiene 
			}

			}	//FOR
		utilitario.getConexion().guardarPantalla();
		if (utilitario.getConexion().ejecutarListaSql().isEmpty()){
			utilitario.agregarMensaje("se guardo correctamente", "");
			utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_RENTA_NRROL=1 where IDE_NRROL="+IDE_NRROL);
		}
	}


	
	
	
	
	
	
	
	
	
	
	

	public TablaGenerica getTablaAportacionesTotalRecibir(String IDE_NRROL){

		TablaGenerica tab_det_rol=new TablaGenerica();

		String sql="SELECT " +
				"a.IDE_NRDRO, " +
				"a.IDE_NRROL, " +
				"a.IDE_GEEDP, " +
				"a.IDE_GTEMP, " +
				"a.IDE_NRDER, " +
				"a.VALOR_NRDRO, " +
				"a.DISCAPACITADO_GTEMP , " +
				"b.tot_recibir, " +
				"c.tot_egresos, "+
				"a.edad, "+
				"d.seguro_social_con_rmu, "+
				"e.PORCENTAJE_GTDIE, "+
				"f.imp_renta_mensual "+
				" from ( "+
				"select DROL.IDE_NRDRO, " +
				"DROL.IDE_NRROL, " +
				"DROL.IDE_GEEDP, " +
				"EMP.IDE_GTEMP, "+
				"DROL.IDE_NRDER, " +
				"DROL.VALOR_NRDRO, " +
				"EMP.DISCAPACITADO_GTEMP, " +
				"FLOOR(((extract(year from now())-extract(year from fecha_nacimiento_gtemp))* 372 + "+
				"(extract(month from now()) - extract(month from fecha_nacimiento_gtemp))*31 + "+  
				"(extract (day from now())-extract(day from fecha_nacimiento_gtemp)))/372) as edad "+
				"from NRH_DETALLE_ROL DROL " +
				"inner join NRH_DETALLE_RUBRO DRUB on DRUB.IDE_NRDER=DROL.IDE_NRDER " +
				"inner join NRH_RUBRO RUB on rub.ide_nrrub=drub.ide_nrrub " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GEEDP=DROL.IDE_GEEDP " +
				"INNER JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EDP.IDE_GTEMP " +
				"WHERE  RUB.IDE_NRRUB="+utilitario.getVariable("p_nrh_rubro_aportes_personales")+" " +
				"AND DROL.IDE_NRROL="+IDE_NRROL+" "+
				")a " +
				"left join " +
				"( " +
				"select " +
				"DROL.IDE_GEEDP, " +
				"DROL.VALOR_NRDRO as tot_recibir "+
				"from NRH_DETALLE_ROL DROL " +
				"inner join NRH_DETALLE_RUBRO DRUB on DRUB.IDE_NRDER=DROL.IDE_NRDER " +
				"inner join NRH_RUBRO RUB on rub.ide_nrrub=drub.ide_nrrub " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GEEDP=DROL.IDE_GEEDP " +
				"INNER JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EDP.IDE_GTEMP " +
				"WHERE  RUB.IDE_NRRUB="+utilitario.getVariable("p_nrh_rubro_valor_recibir")+" " +
				"AND DROL.IDE_NRROL="+IDE_NRROL+" "+
				")b "+
				"on a.ide_geedp=b.ide_geedp "+
				"left join " +
				"( " +
				"select " +
				"DROL.IDE_GEEDP, " +
				"DROL.VALOR_NRDRO as tot_egresos "+
				"from NRH_DETALLE_ROL DROL " +
				"inner join NRH_DETALLE_RUBRO DRUB on DRUB.IDE_NRDER=DROL.IDE_NRDER " +
				"inner join NRH_RUBRO RUB on rub.ide_nrrub=drub.ide_nrrub " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GEEDP=DROL.IDE_GEEDP " +
				"INNER JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EDP.IDE_GTEMP " +
				"WHERE  RUB.IDE_NRRUB="+utilitario.getVariable("p_nrh_rubro_total_egresos")+" " +
				"AND DROL.IDE_NRROL="+IDE_NRROL+" "+
				")c "+
				"on a.ide_geedp=c.ide_geedp and b.ide_geedp=c.ide_geedp "+
				"left join  " +
				"( " +
				"select  DROL.IDE_NRROL,DROL.IDE_GEEDP, " +
				"DROL.VALOR_NRDRO as seguro_social_con_rmu " +
				"from NRH_DETALLE_ROL DROL,NRH_DETALLE_RUBRO DRUB,NRH_RUBRO RUB,GEN_EMPLEADOS_DEPARTAMENTO_PAR edp,GTH_EMPLEADO emp " +
				"WHERE DRUB.IDE_NRDER=DROL.IDE_NRDER " +
				"AND RUB.IDE_NRRUB=DRUB.IDE_NRRUB " +
				"and EDP.IDE_GEEDP=DROL.IDE_GEEDP " +
				"and EDP.IDE_GTEMP=EMP.IDE_GTEMP " +
				"AND RUB.IDE_NRRUB="+utilitario.getVariable("p_nrh_rubro_seguro_social_con_rmu")+" " +
				"AND DROL.IDE_NRROL="+IDE_NRROL+" " +
				"order by IDE_GEEDP " +
				")d on a.ide_geedp=d.ide_geedp " +
				"and b.ide_geedp=d.ide_geedp " +
				"and c.ide_geedp=d.ide_geedp  "+
				"left join (" +
				"select edp.ide_geedp,PORCENTAJE_GTDIE from GTH_DISCAPACIDAD_EMPLEADO die " +
				"inner join GTH_EMPLEADO emp on DIE.IDE_GTEMP=EMP.IDE_GTEMP " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR edp on edp.ide_gtemp=emp.ide_gtemp " +
				"where DISCAPACITADO_GTEMP=TRUE " +
				")e on a.ide_geedp=e.ide_geedp "+
				"left join  ( " +
				"select  DROL.IDE_NRROL,DROL.IDE_GEEDP, DROL.VALOR_NRDRO as imp_renta_mensual " +
				"from NRH_DETALLE_ROL DROL,NRH_DETALLE_RUBRO DRUB,NRH_RUBRO RUB,GEN_EMPLEADOS_DEPARTAMENTO_PAR edp,GTH_EMPLEADO emp WHERE DRUB.IDE_NRDER=DROL.IDE_NRDER AND RUB.IDE_NRRUB=DRUB.IDE_NRRUB and EDP.IDE_GEEDP=DROL.IDE_GEEDP and EDP.IDE_GTEMP=EMP.IDE_GTEMP " +
				"AND RUB.IDE_NRRUB="+utilitario.getVariable("p_nrh_rubro_impuesto_renta_mensual")+" " +
				"AND DROL.IDE_NRROL="+IDE_NRROL+" order by IDE_GEEDP " +
				")f on a.ide_geedp=f.ide_geedp and b.ide_geedp=f.ide_geedp and c.ide_geedp=f.ide_geedp " ;

		return tab_det_rol=utilitario.consultar(sql);
	}



	/** 
	 * metodo que retorna el detalle del rol de todos los empleados que pertenecen a un rol , y de un rubro en especifico, ademas devuelve una columna con el campo discapacitado_gtemp   
	 * 
	 * @param IDE_NRROL : IDE TABLA ROL
	 * @return  resultado: TablaGenerica
	 */
	public TablaGenerica getRubrosDetalleRol(String IDE_NRROL,String IDE_NRRUB){
		TablaGenerica tab_det_rol=new TablaGenerica();
		//		tab_det_rol=utilitario.consultar("select DROL.IDE_NRDRO, " +
		//				"DROL.IDE_NRROL," +
		//				"DROL.IDE_GEEDP," +
		//				"DROL.IDE_NRDER," +
		//				"DROL.VALOR_NRDRO " +
		//				"from NRH_DETALLE_ROL DROL,NRH_DETALLE_RUBRO DRUB,NRH_RUBRO RUB " +
		//				"WHERE DRUB.IDE_NRDER=DROL.IDE_NRDER " +
		//				"AND RUB.IDE_NRRUB=DRUB.IDE_NRRUB AND RUB.IDE_NRRUB="+IDE_NRRUB+" " +
		//				"AND DROL.IDE_NRROL="+IDE_NRROL);

		tab_det_rol=utilitario.consultar("select DROL.IDE_NRDRO, " +
				"DROL.IDE_NRROL, " +
				"DROL.IDE_GEEDP, " +
				"EMP.IDE_GTEMP, "+
				"DROL.IDE_NRDER, " +
				"DROL.VALOR_NRDRO, " +
				"EMP.DISCAPACITADO_GTEMP " +
				"from NRH_DETALLE_ROL DROL " +
				"inner join NRH_DETALLE_RUBRO DRUB on DRUB.IDE_NRDER=DROL.IDE_NRDER " +
				"inner join NRH_RUBRO RUB on rub.ide_nrrub=drub.ide_nrrub " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GEEDP=DROL.IDE_GEEDP " +
				"INNER JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EDP.IDE_GTEMP " +
				"WHERE  RUB.IDE_NRRUB="+IDE_NRRUB+" " +
				"AND DROL.IDE_NRROL="+IDE_NRROL+"");
		return tab_det_rol;
	}

	public TablaGenerica getRubrosDetalleRol(String IDE_NRROL,String IDE_NRRUB,String IDE_GEEDP){
		TablaGenerica tab_det_rol=new TablaGenerica();
		tab_det_rol=utilitario.consultar("select DROL.IDE_NRDRO, " +
				"DROL.IDE_NRROL," +
				"DROL.IDE_GEEDP," +
				"DROL.IDE_NRDER," +
				"DROL.VALOR_NRDRO " +
				"from NRH_DETALLE_ROL DROL,NRH_DETALLE_RUBRO DRUB,NRH_RUBRO RUB " +
				"WHERE DRUB.IDE_NRDER=DROL.IDE_NRDER " +
				"AND RUB.IDE_NRRUB=DRUB.IDE_NRRUB AND RUB.IDE_NRRUB="+IDE_NRRUB+" " +
				"AND DROL.IDE_NRROL="+IDE_NRROL+" AND DROL.IDE_GEEDP="+IDE_GEEDP);
		return tab_det_rol;
	}


	public void registrarProyeccionIngresosEmpleados(String IDE_NRROL){

		TablaGenerica tab_cab_rol=utilitario.consultar("SELECT * FROM NRH_ROL where IDE_NRROL="+IDE_NRROL);				
		TablaGenerica tab_periodo_rol=getPeriodoRol(tab_cab_rol.getValor("IDE_GEPRO"));
		String fecha_ini_cab_rol_pago=tab_periodo_rol.getValor("FECHA_INICIAL_GEPRO");
		String fecha_iniTemp="",a="";
          
		TablaGenerica tab_tot_ingresos_empleados =utilitario.consultar("select a.IDE_NRDRO, " +
				"a.IDE_NRROL,a.IDE_GEEDP,a.ide_gtemp,a.IDE_NRDER,a.VALOR_NRDRO ,b.RMU " +
				"from " +
				"( " +
				"select DROL.IDE_NRDRO, DROL.IDE_NRROL,DROL.IDE_GEEDP,EMP.IDE_GTEMP,DROL.IDE_NRDER,DROL.VALOR_NRDRO " +
				"from NRH_DETALLE_ROL DROL,NRH_DETALLE_RUBRO DRUB,NRH_RUBRO RUB,GEN_EMPLEADOS_DEPARTAMENTO_PAR edp,GTH_EMPLEADO emp " +
				"WHERE DRUB.IDE_NRDER=DROL.IDE_NRDER AND RUB.IDE_NRRUB=DRUB.IDE_NRRUB " +
				"and EDP.IDE_GEEDP=DROL.IDE_GEEDP and EDP.IDE_GTEMP=EMP.IDE_GTEMP " +
				"AND RUB.IDE_NRRUB="+utilitario.getVariable("p_nrh_rubro_subtotal_iess")+" AND DROL.IDE_NRROL="+IDE_NRROL+" " +
				"order by IDE_GEEDP " +
				")a " +
				"left join " +
				"( " +
				"select DROL.IDE_NRDRO, DROL.IDE_NRROL,DROL.IDE_GEEDP,EMP.IDE_GTEMP,DROL.IDE_NRDER,EDP.RMU_GEEDP as rmu " +
				"from NRH_DETALLE_ROL DROL,NRH_DETALLE_RUBRO DRUB,NRH_RUBRO RUB,GEN_EMPLEADOS_DEPARTAMENTO_PAR edp,GTH_EMPLEADO emp " +
				"WHERE DRUB.IDE_NRDER=DROL.IDE_NRDER AND RUB.IDE_NRRUB=DRUB.IDE_NRRUB " +
				"and EDP.IDE_GEEDP=DROL.IDE_GEEDP and EDP.IDE_GTEMP=EMP.IDE_GTEMP " +
				"AND RUB.IDE_NRRUB="+utilitario.getVariable("p_nrh_rubro_remuneracion_unificada")+" AND DROL.IDE_NRROL="+IDE_NRROL+" " +
				"order by IDE_GEEDP " +
				")b on a.ide_geedp=b.ide_geedp and a.ide_nrrol=b.IDE_NRROL");

		System.out.println("sql proyeccion ingresos "+tab_tot_ingresos_empleados.getSql());	
		String IDE_SRPRI="";
		String str_ide_gemes=tab_periodo_rol.getValor("IDE_GEMES");
		int int_mes_rol=1;
		try {
			int_mes_rol=pckUtilidades.CConversion.CInt(str_ide_gemes);
		} catch (Exception e) {
			// TODO: handle exception
		}
		TablaGenerica tab_sri_imp_ren=getSriImpuestoRenta(fecha_ini_cab_rol_pago);

		if (tab_sri_imp_ren!=null && tab_sri_imp_ren.getTotalFilas()>0){

			String IDE_SRIMR=tab_sri_imp_ren.getValor("IDE_SRIMR");
			TablaGenerica tab_cab_proy_ing=new TablaGenerica();	
			tab_cab_proy_ing.setTabla("SRI_PROYECCION_INGRES", "IDE_SRPRI", -1);
			tab_cab_proy_ing.setCondicion("IDE_SRPRI=-1");
			tab_cab_proy_ing.ejecutarSql();

			TablaGenerica tab_det_proy_ing=new TablaGenerica();
			tab_det_proy_ing.setTabla("SRI_DETALLE_PROYECCCION_INGRES", "IDE_SRDPI", -1);
			tab_det_proy_ing.setCondicion("IDE_SRDPI=-1");
			tab_det_proy_ing.ejecutarSql();

			System.out.println("proyeccion tot ingresos "+tab_tot_ingresos_empleados.getSql());

			for (int i = 0; i < tab_tot_ingresos_empleados.getTotalFilas(); i++) {
				TablaGenerica tab_proy_ing=getSriProyeccionIngresos(IDE_SRIMR, tab_tot_ingresos_empleados.getValor(i, "IDE_GTEMP"));
				tab_cab_proy_ing.setCondicion("IDE_SRPRI=-1");
				tab_cab_proy_ing.ejecutarSql();

				tab_det_proy_ing.setCondicion("IDE_SRDPI=-1");
				tab_det_proy_ing.ejecutarSql();

				if (tab_proy_ing==null || tab_proy_ing.getTotalFilas()==0){
					tab_cab_proy_ing.insertar();
					tab_cab_proy_ing.setValor("IDE_SRIMR", IDE_SRIMR);
					tab_cab_proy_ing.setValor("IDE_GEEDP", tab_tot_ingresos_empleados.getValor(i,"IDE_GEEDP"));
					tab_cab_proy_ing.setValor("IDE_GTEMP", tab_tot_ingresos_empleados.getValor(i, "IDE_GTEMP"));
					tab_cab_proy_ing.setValor("ACTIVO_SRPRI", "true");
					tab_cab_proy_ing.guardar();
					IDE_SRPRI=tab_cab_proy_ing.getValor("IDE_SRPRI");
				}else{
					IDE_SRPRI=tab_proy_ing.getValor("IDE_SRPRI");
				}
				TablaGenerica tab_det_sri_proy=getSriDetalleProyeccionIngresos(IDE_SRPRI);
				if (tab_det_sri_proy.getTotalFilas()>0){
					int int_meses_generados=tab_det_sri_proy.getTotalFilas();
					int int_meses_faltan=12-int_meses_generados;
					for (int j = 1; j <= int_meses_faltan; j++) {
						tab_det_proy_ing.insertar();
						tab_det_proy_ing.setValor("IDE_SRPRI", IDE_SRPRI);
						tab_det_proy_ing.setValor("VALOR_SRDPI",utilitario.getFormatoNumero(tab_tot_ingresos_empleados.getValor(i, "RMU"),2));
						tab_det_proy_ing.setValor("IDE_GEMES", (int_meses_generados+j)+"");
					}
					tab_det_proy_ing.guardar();
					utilitario.getConexion().ejecutarListaSql();

					TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+tab_tot_ingresos_empleados.getValor(i,"IDE_GEEDP"));
					String fecha_finTemp="";
					fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
					int_mes_rol=utilitario.getMes(fecha_finTemp);
					
					utilitario.getConexion().agregarSql("UPDATE SRI_DETALLE_PROYECCCION_INGRES SET VALOR_SRDPI="+utilitario.getFormatoNumero(tab_tot_ingresos_empleados.getValor(i,"VALOR_NRDRO"),2)+" " +
							"WHERE IDE_SRPRI="+IDE_SRPRI+" AND IDE_GEMES="+int_mes_rol);
					

					if (int_mes_rol<12){
						for (int j = int_mes_rol+1; j <= 12; j++) {
							utilitario.getConexion().agregarSql("UPDATE SRI_DETALLE_PROYECCCION_INGRES " +
									"SET VALOR_SRDPI=0.0 " +
									"WHERE IDE_SRPRI="+IDE_SRPRI+" AND IDE_GEMES="+j);
						}
					}
					utilitario.getConexion().ejecutarListaSql();

				}else{
					
					TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+tab_tot_ingresos_empleados.getValor(i,"IDE_GEEDP"));
					String fecha_finTemp="";
					fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
					int_mes_rol=utilitario.getMes(fecha_finTemp);
					
					for (int j = 1; j < int_mes_rol; j++) {
						tab_det_proy_ing.insertar();
						tab_det_proy_ing.setValor("IDE_SRPRI", IDE_SRPRI);
						tab_det_proy_ing.setValor("VALOR_SRDPI",utilitario.getFormatoNumero(0, 2));
						tab_det_proy_ing.setValor("IDE_GEMES", j+"");
					}
					for (int j = int_mes_rol; j <= 12; j++) {
						tab_det_proy_ing.insertar();
						tab_det_proy_ing.setValor("IDE_SRPRI", IDE_SRPRI);
						String num_mes=String.valueOf(j);
						if (num_mes.equals(str_ide_gemes)){
							tab_det_proy_ing.setValor("VALOR_SRDPI", utilitario.getFormatoNumero(tab_tot_ingresos_empleados.getValor(i,"VALOR_NRDRO"), 2));
						}else{
							//tab_det_proy_ing.setValor("VALOR_SRDPI",utilitario.getFormatoNumero(tab_tot_ingresos_empleados.getValor(i, "RMU"),2));
							tab_det_proy_ing.setValor("VALOR_SRDPI",utilitario.getFormatoNumero(0.00,2));
						}
						tab_det_proy_ing.setValor("IDE_GEMES", j+"");
					}
					tab_det_proy_ing.guardar();
					//					utilitario.getConexion().guardarPantalla();
					utilitario.getConexion().ejecutarListaSql();


				}

			}
			//			utilitario.getConexion().guardarPantalla();
			utilitario.getConexion().ejecutarListaSql();

		} else{
			utilitario.agregarMensajeInfo("No se puede calcular el impuesto a la renta ","no se encuentra configurado en el modulo SRI");
		}

	}


	public void registrarProyeccionIngresosEmpleadosInicial(String ide_gtemp){
boolean banderaActualizarIngresos=false;
		TablaGenerica tab_tot_ingresos_empleados;

		if(ide_gtemp == null || ide_gtemp.equals(""))
		{
			System.out.println("Entro null");
			tab_tot_ingresos_empleados =utilitario.consultar("SELECT empleado.ide_geedp as ide_geedp,rmu_geedp, empleado.ide_gtemp as ide_gtemp "
		+" FROM public.gen_empleados_departamento_par  empleado inner join (SELECT max(ide_geedp) as ide_geedp,ide_gtemp FROM public.gen_empleados_departamento_par "
			+"  group by ide_gtemp) max_empleado on empleado.ide_gtemp = max_empleado.ide_gtemp and empleado.ide_geedp = max_empleado.ide_geedp inner join gth_empleado emp on  empleado.ide_gtemp = emp.ide_gtemp"
			+" where activo_gtemp = true order by empleado.ide_gtemp");
		}
		else
		{
			System.out.println("Entro "+ide_gtemp);
			tab_tot_ingresos_empleados =utilitario.consultar("SELECT empleado.ide_geedp as ide_geedp,rmu_geedp, empleado.ide_gtemp as ide_gtemp,empleado.fecha_geedp,empleado.ide_geded "
			+" FROM public.gen_empleados_departamento_par  empleado inner join (SELECT max(ide_geedp) as ide_geedp,ide_gtemp FROM public.gen_empleados_departamento_par "
			+"  group by ide_gtemp) max_empleado on empleado.ide_gtemp = max_empleado.ide_gtemp and empleado.ide_geedp = max_empleado.ide_geedp inner join gth_empleado emp on  empleado.ide_gtemp = emp.ide_gtemp"
			+" where  activo_gtemp = true and empleado.ide_gtemp  = "+ide_gtemp+" order by empleado.ide_gtemp");
		}


		String rubroContratacion="24";
		int mesRol=0,anioRol=0;
		String cambioContratoPorEmpleado="";
        ///int mesRol=0,anioRol=0;
		TablaGenerica tab_empleado_departamento_par=utilitario.consultar("select ide_geedp,fecha_geedp,ide_geded from gen_empleados_departamento_par "
				+ "where ide_gtemp in("+ide_gtemp+")"
				+ " order by ide_geedp desc");  
		TablaGenerica tab_accion_motivo_empleado=null;
		for (int i = 0; i < tab_empleado_departamento_par.getTotalFilas(); i++) {
		cambioContratoPorEmpleado=tab_empleado_departamento_par.getValor(i,"IDE_GEDED");
				//Los ide_geame   que son los motivos de la tabla gen_accion_motivo_empleado  
		tab_accion_motivo_empleado=utilitario.consultar("SELECT ide_geded,ide_geame "
						+ "FROM gen_detalle_empleado_departame "
						+ "where ide_geded  in("+cambioContratoPorEmpleado+") ");

				//Validacion de que tenga terminacion de contrato
				String motivoContratoPorEmpleado="";
				boolean estadoTerminacionContrato=false;
			//	for (int j = 0; j < tab_accion_motivo_empleado.getTotalFilas()-1; j++) {
					motivoContratoPorEmpleado=tab_accion_motivo_empleado.getValor("IDE_GEAME");
					System.out.println("IDE_GEAME"+motivoContratoPorEmpleado);
					if (motivoContratoPorEmpleado.equals(""+rubroContratacion+"")) {
						estadoTerminacionContrato=true;
					mesRol=	utilitario.getMes(tab_empleado_departamento_par.getValor(i,"FECHA_GEEDP"));
					anioRol=utilitario.getAnio(tab_empleado_departamento_par.getValor(i,"FECHA_GEEDP"));
					i=tab_empleado_departamento_par.getTotalFilas();
					}else {
						estadoTerminacionContrato=true;
					}
		}

		
		System.out.println("sql proyeccion ingresos "+tab_tot_ingresos_empleados.getSql());
		
		String IDE_SRPRI="";
		
		
		int int_mes_rol = utilitario.getMes(utilitario.getFechaActual());
		int anio_rol = utilitario.getAnio(utilitario.getFechaActual());
		TablaGenerica tab_sri_imp_ren=getSriImpuestoRenta(utilitario.getFechaActual());

		if (tab_sri_imp_ren!=null && tab_sri_imp_ren.getTotalFilas()>0){

			String IDE_SRIMR=tab_sri_imp_ren.getValor("IDE_SRIMR");
			TablaGenerica tab_cab_proy_ing=new TablaGenerica();	
			tab_cab_proy_ing.setTabla("SRI_PROYECCION_INGRES", "IDE_SRPRI", -1);
			tab_cab_proy_ing.setCondicion("IDE_SRPRI=-1");
			tab_cab_proy_ing.ejecutarSql();

			TablaGenerica tab_det_proy_ing=new TablaGenerica();
			tab_det_proy_ing.setTabla("SRI_DETALLE_PROYECCCION_INGRES", "IDE_SRDPI", -1);
			tab_det_proy_ing.setCondicion("IDE_SRDPI=-1");
			tab_det_proy_ing.ejecutarSql();

			if (tab_tot_ingresos_empleados.getTotalFilas()>0) {
			for (int i = 0; i < tab_tot_ingresos_empleados.getTotalFilas(); i++) {
				//Busco si tiene creada la tabla de proyeccion de ingresos
				TablaGenerica tab_proy_ing=getSriProyeccionIngresos(IDE_SRIMR, tab_tot_ingresos_empleados.getValor(i, "IDE_GTEMP"));
				
				tab_cab_proy_ing.setCondicion("IDE_SRPRI=-1");
				tab_cab_proy_ing.ejecutarSql();

				tab_det_proy_ing.setCondicion("IDE_SRDPI=-1");
				tab_det_proy_ing.ejecutarSql();

				if (tab_proy_ing==null || tab_proy_ing.getTotalFilas()==0){
					tab_cab_proy_ing.insertar();
					tab_cab_proy_ing.setValor("IDE_SRIMR", IDE_SRIMR);
					tab_cab_proy_ing.setValor("IDE_GEEDP", tab_tot_ingresos_empleados.getValor(i,"IDE_GEEDP"));
					tab_cab_proy_ing.setValor("IDE_GTEMP", tab_tot_ingresos_empleados.getValor(i, "IDE_GTEMP"));
					tab_cab_proy_ing.setValor("ACTIVO_SRPRI", "true");
					tab_cab_proy_ing.guardar();
					IDE_SRPRI=tab_cab_proy_ing.getValor("IDE_SRPRI");
					//true si es ingreso
					banderaActualizarIngresos=false;
				}else{
					IDE_SRPRI=tab_proy_ing.getValor("IDE_SRPRI");
					//true si es actualizacion
					banderaActualizarIngresos=true;
				}
				TablaGenerica tab_det_sri_proy=getSriDetalleProyeccionIngresos(IDE_SRPRI);
				//variable guarda las acciones de personal creadas para ese empleado
				String accionXEmpleado="",rubrosImponible=utilitario.getVariable("p_valor_base_imponible"),rubrosRmu=utilitario.getVariable("p_valor_rmu");
				//metodo retorna las acciones de personal para ese empleado(ide_geedp)
				accionXEmpleado=accionPersonalEmpleado(ide_gtemp);
				//guardo las base imponible generada por el empleado de ese año fiscal
				TablaGenerica getBaseImponible=getBaseImponibleEmpleado (tab_sri_imp_ren.getValor("DETALLE_SRIMR"),accionXEmpleado,rubrosImponible);				
				TablaGenerica getRmu=getBaseImponibleEmpleado (tab_sri_imp_ren.getValor("DETALLE_SRIMR"),accionXEmpleado,rubrosRmu);				
				
				//Si es actualizacion de la proyeccion de ingreso del empleado
				if (banderaActualizarIngresos==true) {
					//si contiene al menos una base imponible y contiene tabla de proyeccion 
					if (getBaseImponible.getTotalFilas()>0 && tab_det_sri_proy.getTotalFilas()>0) {
						int mesInicio=0,mesFin=0;
						//Obtengo la el mes del rubro base imponible generada por el empleado 
						//lista de meses en q el empleado genero el  base imponible 
						ArrayList<Integer> listaBaseImponible = new ArrayList<Integer>();
						for (int j = 0; j < getBaseImponible.getTotalFilas(); j++) {
							listaBaseImponible.add(Integer.parseInt(getBaseImponible.getValor(j, "IDE_GEMES")));
						}
						
						
						ArrayList<Integer> listaNueva = new ArrayList<Integer>();
						for (int j = 1; j <= 12; j++) {
							listaNueva.add(j);
						}
											
						ArrayList<Integer> listaRmu = new ArrayList<Integer>();
						for (int j = 0; j < getRmu.getTotalFilas(); j++) {
							listaRmu.add(Integer.parseInt(getRmu.getValor(j,"IDE_GEMES")));
						}
							
						
						
						ArrayList<Integer> listaSinValor = new ArrayList<Integer>();
						for (int j = 1; j <= mesRol; j++) {
							for (int j1 = 0; j1 < listaRmu.size(); j1++) {
								if (j==listaRmu.get(j1).intValue()) {
									listaRmu.remove(j1);
									j1=listaRmu.size();
								}else {
								}
							}
						}    
				
						for (int j = 1; j < mesRol; j++) {
						utilitario.getConexion().ejecutarSql("UPDATE SRI_DETALLE_PROYECCCION_INGRES " +
						"SET VALOR_SRDPI=0.0  " +
						"WHERE IDE_SRPRI="+IDE_SRPRI+" AND IDE_GEMES="+(j));
						}	
					int valorNuevo=0;			
			   if ((mesRol>Integer.parseInt(getBaseImponible.getValor((0),"ide_gemes"))&&(anio_rol==anioRol))) {
				valorNuevo=mesRol;
				
				}else {
					for (int j = 0; j < getBaseImponible.getTotalFilas(); j++) {
						utilitario.getConexion().ejecutarSql("UPDATE SRI_DETALLE_PROYECCCION_INGRES " +
								"SET VALOR_SRDPI="+utilitario.getFormatoNumero(getBaseImponible.getValor(j, "valor_nrdro"),2)+" " +
								"WHERE IDE_SRPRI="+IDE_SRPRI+" AND IDE_GEMES="+getBaseImponible.getValor(j, "ide_gemes"));
				valorNuevo=getBaseImponible.getTotalFilas()+1;
					}
				}
				
				if (anio_rol==anioRol) {
					for (int j = 0; j < listaRmu.size(); j++) {
					utilitario.getConexion().ejecutarSql("UPDATE SRI_DETALLE_PROYECCCION_INGRES " +
							"SET VALOR_SRDPI="+utilitario.getFormatoNumero(tab_tot_ingresos_empleados.getValor("rmu_geedp"),2)+" " +
							"WHERE IDE_SRPRI="+IDE_SRPRI+" AND IDE_GEMES="+listaRmu.get(j).toString());
					}	
				}
				else {
					for (int j = valorNuevo; j <=12; j++) {
						utilitario.getConexion().ejecutarSql("UPDATE SRI_DETALLE_PROYECCCION_INGRES " +
								"SET VALOR_SRDPI="+utilitario.getFormatoNumero(tab_tot_ingresos_empleados.getValor("rmu_geedp"),2)+" " +
								"WHERE IDE_SRPRI="+IDE_SRPRI+" AND IDE_GEMES="+j);
				}
					}
				utilitario.addUpdate("tab_det_proy_ing");
				
			}
			//Si no contiene base imponible pero si tabla de proyeccion 		
			else if (getBaseImponible.getTotalFilas()==0 && tab_det_sri_proy.getTotalFilas()>0){
						//actualizacion apartir del ultimo mes contenido en la tabla proyeccion de ingresos 						
						for (int j = 0; j <mesRol ; j++) {
							utilitario.getConexion().ejecutarSql("UPDATE SRI_DETALLE_PROYECCCION_INGRES " +
									"SET VALOR_SRDPI=0.0 " +
									"WHERE IDE_SRPRI="+IDE_SRPRI+" AND IDE_GEMES="+j+" ");
							}
				
						//A partir del utimo mes generado susttituyo los valores del sueldo para proyectar
						for (int j = (mesRol+1); j <= 12 ; j++) {
							utilitario.getConexion().ejecutarSql("UPDATE SRI_DETALLE_PROYECCCION_INGRES " +
									"SET VALOR_SRDPI="+utilitario.getFormatoNumero(tab_tot_ingresos_empleados.getValor("rmu_geedp"),2)+" " +
									"WHERE IDE_SRPRI="+IDE_SRPRI+" AND IDE_GEMES="+j+" and VALOR_SRDPI > 0.0");
					
						}
					} 
					//Si no contiene base imponible pero ni tabla de proyeccion		
					else if (getBaseImponible.getTotalFilas()==0 && tab_det_sri_proy.getTotalFilas()==0) {
						//Si no tiene base imponible ni tabla generada 
					for (int j = 1; j <= 12 ; j++) {
							utilitario.getConexion().ejecutarSql("UPDATE SRI_DETALLE_PROYECCCION_INGRES " +
									"SET VALOR_SRDPI="+utilitario.getFormatoNumero(tab_tot_ingresos_empleados.getValor("rmu_geedp"),2)+" " +
									"WHERE IDE_SRPRI="+IDE_SRPRI+" AND IDE_GEMES="+j+" and VALOR_SRDPI > 0.0");
						
					}
					
					}
					//Si contiene base imponible pero no tabla de proyeccion
					else if (getBaseImponible.getTotalFilas()>0 && tab_det_sri_proy.getTotalFilas()==0) {
						//Si no tiene base imponible ni tabla generada 
						for (int j = 0; j <= 12; j++) {
							utilitario.getConexion().ejecutarSql("UPDATE SRI_DETALLE_PROYECCCION_INGRES " +
									"SET VALOR_SRDPI="+utilitario.getFormatoNumero(getBaseImponible.getValor(j, "valor_nrdro"),2)+" " +
									"WHERE IDE_SRPRI="+IDE_SRPRI+" AND IDE_GEMES="+j);
							
						}	
					
					}
					utilitario.addUpdate("tab_det_proy_ing");
					
				}else {
				//Si es ingreso y no actualizacion de datos
				if (getBaseImponible.getTotalFilas()>0) {
					
					int totalFilas=0;
					totalFilas=getBaseImponible.getTotalFilas();
					int contador=1;
					int valorNuevo=0;
					int mesInicio=0,mesFin=0;
					if (anioRol==utilitario.getAnio(utilitario.getFechaActual())) {
					//Anio Rol es igual al fiscal	
					mesInicio=mesRol;
					for (int j = 1; j < mesInicio ; j++) {
						if (mesInicio==j) {
						}else if(mesInicio>j){
						tab_det_proy_ing.insertar();
						tab_det_proy_ing.setValor("IDE_SRPRI", IDE_SRPRI);
						tab_det_proy_ing.setValor("VALOR_SRDPI","0.00");
						tab_det_proy_ing.setValor("IDE_GEMES", (j)+"");
					}
					}
						
					ArrayList<Integer> listaBaseImponible = new ArrayList<Integer>();
					for (int j = 0; j < getBaseImponible.getTotalFilas(); j++) {
						listaBaseImponible.add(Integer.parseInt(getBaseImponible.getValor(j, "IDE_GEMES")));
					}
					int cont=0;
					for (int j = mesInicio; j <= listaBaseImponible.get(listaBaseImponible.size()-1).intValue(); j++) {
							if (cont<getBaseImponible.getTotalFilas()) {
							
							tab_det_proy_ing.insertar();
							tab_det_proy_ing.setValor("IDE_SRPRI", IDE_SRPRI);
							tab_det_proy_ing.setValor("VALOR_SRDPI",utilitario.getFormatoNumero(getBaseImponible.getValor(cont, "valor_nrdro"),2));
							tab_det_proy_ing.setValor("IDE_GEMES", getBaseImponible.getValor(cont, "ide_gemes")+"");
							}
							cont++;
					}
						
						for (int j = (listaBaseImponible.get(listaBaseImponible.size()-1).intValue()+1); j <=12; j++) {
							tab_det_proy_ing.insertar();
							tab_det_proy_ing.setValor("IDE_SRPRI", IDE_SRPRI);
							tab_det_proy_ing.setValor("VALOR_SRDPI",utilitario.getFormatoNumero(tab_tot_ingresos_empleados.getValor("rmu_geedp"),2));
							tab_det_proy_ing.setValor("IDE_GEMES", j+"");		
						}		
						
					
					}else if (anioRol<utilitario.getAnio(utilitario.getFechaActual())){
						for (int j = 0; j <getBaseImponible.getTotalFilas(); j++) {
							tab_det_proy_ing.insertar();
							tab_det_proy_ing.setValor("IDE_SRPRI", IDE_SRPRI);
							tab_det_proy_ing.setValor("VALOR_SRDPI",utilitario.getFormatoNumero(getBaseImponible.getValor(j, "valor_nrdro"),2));
							tab_det_proy_ing.setValor("IDE_GEMES", getBaseImponible.getValor(j, "ide_gemes")+"");		
					}
						for (int j = getBaseImponible.getTotalFilas()+1; j <=12; j++) {
							tab_det_proy_ing.insertar();
							tab_det_proy_ing.setValor("IDE_SRPRI", IDE_SRPRI);
							tab_det_proy_ing.setValor("VALOR_SRDPI",utilitario.getFormatoNumero(tab_tot_ingresos_empleados.getValor("rmu_geedp"),2));
							tab_det_proy_ing.setValor("IDE_GEMES", j+"");		
						}	
						
						
					}
					tab_det_proy_ing.guardar();
					utilitario.getConexion().ejecutarListaSql();
					utilitario.addUpdate("tab_cab_proy_ing,tab_det_proy_ing");
				
					}else {
					//Si  no contengo base imponible al ingresar un empleado
					
						for (int j = 1; j <  mesRol ; j++) {
							tab_det_proy_ing.insertar();
							tab_det_proy_ing.setValor("IDE_SRPRI", IDE_SRPRI);
							tab_det_proy_ing.setValor("VALOR_SRDPI","0.00");
							tab_det_proy_ing.setValor("IDE_GEMES", (j)+"");
						}
						
						
						
						for (int j = mesRol; j <=12 ; j++) {
							tab_det_proy_ing.insertar();
							tab_det_proy_ing.setValor("IDE_SRPRI", IDE_SRPRI);
							tab_det_proy_ing.setValor("VALOR_SRDPI",utilitario.getFormatoNumero(tab_tot_ingresos_empleados.getValor("rmu_geedp"),2));
							tab_det_proy_ing.setValor("IDE_GEMES", (j)+"");
						}
						
						tab_det_proy_ing.guardar();
						utilitario.getConexion().ejecutarListaSql();
						utilitario.addUpdate("tab_cab_proy_ing,tab_det_proy_ing");

					}
					
					
					
					
					

				/*	utilitario.getConexion().agregarSql("UPDATE SRI_DETALLE_PROYECCCION_INGRES SET VALOR_SRDPI="+utilitario.getFormatoNumero(tab_tot_ingresos_empleados.getValor(i,"rmu_geedp"),2)+" " +
							"WHERE IDE_SRPRI="+IDE_SRPRI+" AND IDE_GEMES="+int_mes_rol);

					if (int_mes_rol<12){
						for (int j = int_mes_rol+1; j <= 12; j++) {
							utilitario.getConexion().agregarSql("UPDATE SRI_DETALLE_PROYECCCION_INGRES " +
									"SET VALOR_SRDPI="+utilitario.getFormatoNumero(tab_tot_ingresos_empleados.getValor(i, "rmu_geedp"),2)+" " +
									"WHERE IDE_SRPRI="+IDE_SRPRI+" AND IDE_GEMES="+j);
						}
					}
					utilitario.getConexion().ejecutarListaSql();*/
					//utilitario.getConexion().ejecutarListaSql();
					//utilitario.agregarMensaje("Se guardó correctamente ","Proyección de Ingresos calculada con éxito");
					
					
					utilitario.agregarMensaje("Se guardó correctamente ","Proyección de Ingresos calculada con éxito");
				}
				
				System.out.println("RMU--" + tab_tot_ingresos_empleados.getValor(i, "rmu_geedp")+ " IDE_SRPRI----" +IDE_SRPRI);
				
			/*	if (tab_det_sri_proy.getTotalFilas() == 0){
					
		
				}
				else
				{
					utilitario.agregarMensaje("Ya existe una Proyección de Ingresos ","El empleado ya registra una Proyección de ingresos inicial");
				}*/
				
				
			}//termina el for
			}else {
				utilitario.agregarMensajeInfo("No se puede calcular la proyección de ingresos ","no contiene acción de personal asignada");
				 return;			
			}
			
			//			utilitario.getConexion().guardarPantalla();


		} else{
			utilitario.agregarMensajeInfo("No se puede calcular la proyección de ingresos ","no se encuentra configurado en el modulo SRI");
		}

	}


	public TablaGenerica getSriProyeccionIngresos(String IDE_SRIMR, String IDE_GTEMP){
		TablaGenerica tab_proy_ing=utilitario.consultar("SELECT * from SRI_PROYECCION_INGRES where IDE_SRIMR="+IDE_SRIMR+" and IDE_GTEMP="+IDE_GTEMP);
		return tab_proy_ing;
	}

	public TablaGenerica getSriDetalleProyeccionIngresos(String IDE_SRPRI){
		TablaGenerica tab_det_proy_ing=utilitario.consultar("SELECT * FROM SRI_DETALLE_PROYECCCION_INGRES WHERE IDE_SRPRI="+IDE_SRPRI);
		return tab_det_proy_ing;
	}



	//	public void modificarRol1(TablaGenerica tab_det_rol,String ide_gtemp,String ide_geedp,String ide_nrder,String ide_nrdtn,String valor,String ide_gepro,int nro_comercial_nrtit){
	//
	//
	//		tab_deta_rubros_rol=getDetalleRubrosRolVacia();
	//
	//		String fecha_final_gepro=getPeriodoRol(ide_gepro).getValor("FECHA_FINAL_GEPRO");
	//		String fecha_inicial_gepro=getPeriodoRol(ide_gepro).getValor("FECHA_INICIAL_GEPRO");
	//
	//
	//		String sql_emp=getSqlEmpleadosTipoNomina(ide_nrdtn, ide_geedp);
	//
	//		TablaGenerica tab_emp=utilitario.consultar(sql_emp);
	//
	//		String fecha_contrato=tab_emp.getValor("FECHA_GEEDP");
	//		String RMU=tab_emp.getValor("RMU_GEEDP");
	//		String IDE_GEREG=tab_emp.getValor("IDE_GEREG");
	//		String acumula_fondos=tab_emp.getValor("ACUMULA_FONDOS_GEEDP");
	//		String fecha_subroga=tab_emp.getValor("FECHA_ENCARGO_GEEDP");
	//		String RMU_CARGO_SUBROGA=tab_emp.getValor("SUELDO_SUBROGA_GEEDP");
	//		String ajuste_sueldo=tab_emp.getValor("AJUSTE_SUELDO_GEEDP");
	//		String fecha_ajuste=tab_emp.getValor("FECHA_AJUSTE_GEEDP");
	//		String fecha_fin_subrogacion=tab_emp.getValor("FECHA_ENCARGO_FIN_GEEDP");
	//		String dias_pend_vacacion=tab_emp.getValor("DIAS_VACACION");
	//
	//
	//		P_NRH_RUBRO_DESC_VALORES_LIQUIDAR=utilitario.getVariable("p_nrh_rubro_desc_valores_liquidar"); 
	//		P_NRH_RUBRO_REGION=utilitario.getVariable("p_nrh_rubro_region");
	//		P_NRH_RUBRO_REMUNERACION_UNIFICADA=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada");
	//		P_NRH_RUBRO_RMU_HONORARIOS=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada_honorarios");
	//
	//		P_NRH_RUBRO_ACUMULA_FONDOS=utilitario.getVariable("p_nrh_rubro_acumula_fondos_reserva");
	//		P_NRH_RUBRO_DIAS_TRABAJADOS=utilitario.getVariable("p_nrh_rubro_dias_trabajados");
	//		P_NRH_RUBRO_DIAS_ANTIGUEDAD=utilitario.getVariable("p_nrh_rubro_dias_antiguedad");
	//		P_NRH_RUBRO_DIAS_FONDOS_RESERVA=utilitario.getVariable("p_nrh_rubro_dias_fondos_reserva");
	//		P_NRH_RUBRO_DIAS_PERIODO=utilitario.getVariable("p_nrh_rubro_dias_periodo_nomina");
	//		P_NRH_RUBRO_RMU_CARGO_SUBROGANTE=utilitario.getVariable("p_nrh_rubro_rmu_cargo_subrogante");
	//		P_NRH_RUBRO_DIAS_SUBROGADOS=utilitario.getVariable("p_nrh_rubro_dias_subrogados");
	//		P_NRH_RUBRO_DIAS_AJUSTE_SUELDO=utilitario.getVariable("p_nrh_rubro_dias_ajuste_sueldo");
	//		P_NRH_RUBRO_AJUSTE_SUELDO=utilitario.getVariable("p_nrh_rubro_ajuste_sueldo");
	//
	//		P_NRH_RUBRO_DIAS_PEND_VACACION=utilitario.getVariable("p_nrh_rubro_dias_pendientes_vacacion");
	//
	//		String fecha_ingreso=serv_empleado.getEmpleado(tab_emp.getValor("IDE_GTEMP")).getValor("FECHA_INGRESO_GTEMP");
	//
	//
	//		boolean boo_tiene_anticipos=false;
	//		TablaGenerica tab_cuotas=serv_anticipo.getCuotasAnticipoPorPagarEmpleado(ide_gtemp, ide_gepro);
	//		if (tab_cuotas.getTotalFilas()>0){
	//			boo_tiene_anticipos=true;
	//		}
	//
	//		boolean boo_tiene_beneficio_guarderia=false;
	//		TablaGenerica tab_det_fac_gua=getDetalleFacturaGuarderiaEmpleado(ide_gtemp);
	//		if (tab_det_fac_gua.getTotalFilas()>0){
	//			boo_tiene_beneficio_guarderia=true;
	//		}
	//
	//
	//		int dias_antiguedad=pckUtilidades.CConversion.CInt(getTotalDiasLaborados(fecha_ingreso,fecha_final_gepro));
	//
	//
	//		for (int i = 0; i < tab_det_rol.getTotalFilas(); i++) {
	//			tab_deta_rubros_rol.insertar();
	//			tab_deta_rubros_rol.setValor("IDE_GEEDP",tab_det_rol.getValor(i, "IDE_GEEDP"));
	//			tab_deta_rubros_rol.setValor("IDE_NRDER",tab_det_rol.getValor(i, "IDE_NRDER"));
	//			tab_deta_rubros_rol.setValor("VALOR_NRDRO",tab_det_rol.getValor(i, "VALOR_NRDRO"));				
	//
	//
	//			if(tab_det_rol.getValor(i, "IDE_NRFOC").equalsIgnoreCase(P_NRH_RUBRO_IMPORTADO)){
	//
	//				String valor_rub_importado=importarRubro(ide_geedp, tab_det_rol.getValor(i, "IDE_NRRUB"), boo_tiene_beneficio_guarderia, boo_tiene_anticipos,IDE_GEREG, RMU, acumula_fondos, fecha_ingreso, fecha_contrato, ide_gepro, fecha_inicial_gepro, fecha_final_gepro, dias_antiguedad+"",fecha_subroga,fecha_fin_subrogacion,RMU_CARGO_SUBROGA,ajuste_sueldo,fecha_ajuste,dias_pend_vacacion,nro_comercial_nrtit,false);
	//				if (valor_rub_importado!=null && !valor_rub_importado.isEmpty()){
	//					tab_deta_rubros_rol.setValor("VALOR_NRDRO",valor_rub_importado);
	//					utilitario.getConexion().ejecutarSql("UPDATE NRH_DETALLE_ROL set VALOR_NRDRO="+valor_rub_importado+" where IDE_NRROL="+tab_det_rol.getValor(0,"IDE_NRROL")+" and IDE_NRDER="+tab_det_rol.getValor(i, "IDE_NRDER")+" and IDE_GEEDP="+tab_det_rol.getValor(0,"IDE_GEEDP"));
	//				}
	//
	//			}
	//
	//		}
	//
	//
	//		TablaGenerica tab_det_rub=getRubrosTipoFormula(ide_nrdtn,"-1");
	//
	//		for (int i = 0; i < tab_det_rub.getTotalFilas(); i++) {
	//
	//
	//			String formula_reemplazada = tab_det_rub.getValor(i, "FORMULA_NRDER");
	//			String str_fecha_inicio_nrder=tab_det_rub.getValor(i,"FECHA_INICIAL_NRDER");
	//			String str_fecha_fin_nrder=tab_det_rub.getValor(i,"FECHA_FINAL_NRDER");
	//			String str_fecha_pago_nrder=tab_det_rub.getValor("FECHA_PAGO_NRDER");
	//
	//			double dou_valor=0;	
	//			if (formula_reemplazada!=null && !formula_reemplazada.isEmpty()){
	//				formula_reemplazada = getValorFormula(tab_deta_rubros_rol.getTotalFilas(),fecha_final_gepro,ide_geedp,tab_det_rub.getValor(i, "IDE_NRDER"),tab_det_rub.getValor(i, "FORMULA_NRDER"),str_fecha_inicio_nrder,str_fecha_fin_nrder,str_fecha_pago_nrder);		
	//				dou_valor=utilitario.evaluarExpresion(formula_reemplazada);
	//			}
	//
	//			utilitario.getConexion().ejecutarSql("UPDATE NRH_DETALLE_ROL " +
	//					"set VALOR_NRDRO="+dou_valor+" " +
	//					"where IDE_NRROL="+tab_det_rol.getValor(0,"IDE_NRROL")+" " +
	//					"and IDE_NRDER="+tab_det_rub.getValor(i, "IDE_NRDER")+" " +
	//					"and IDE_GEEDP="+ide_geedp);
	//
	//
	//			int indice=getIndiceValorRubro(tab_det_rub.getValor(i, "IDE_NRDER"), ide_geedp);
	//			if (indice!=-1){
	//				//				tab_deta_rubros_rol.modificar(indice);
	//				tab_deta_rubros_rol.modificar(indice);
	//				tab_deta_rubros_rol.setValor(indice, "VALOR_NRDRO", dou_valor+"");
	//			}
	//
	//		}
	//
	//		if (valor!=null && !valor.isEmpty() && ide_nrder!=null && !ide_nrder.isEmpty()){
	//			if (!valor.startsWith("=")){
	//				utilitario.getConexion().ejecutarSql("UPDATE NRH_DETALLE_ROL set VALOR_NRDRO="+valor+" where IDE_NRROL="+tab_det_rol.getValor(0,"IDE_NRROL")+" and IDE_NRDER="+ide_nrder+" and IDE_GEEDP="+tab_det_rol.getValor(0,"IDE_GEEDP"));
	//			}
	//		}
	//
	//		//		utilitario.getConexion().ejecutarSql("DELETE FROM NRH_DETALLE_ROL where ide_nrdro in ( " +
	//		//				"select IDE_NRDRO " +
	//		//				"from NRH_DETALLE_ROL DRO " +
	//		//				"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER " +
	//		//				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
	//		//				"INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
	//		//				"where DRO.IDE_NRROL="+tab_det_rol.getValor(0,"IDE_NRROL")+" and DRO.IDE_GEEDP="+tab_det_rol.getValor(0,"IDE_GEEDP")+" and DER.ACTIVO_NRDER!=1 )");
	//		//		
	//
	//
	//	}


	public boolean isAcumulaFondos(String ide_geedp){
		TablaGenerica tab_emp_dep=getEmpleadoDepartamento(ide_geedp);
		if (tab_emp_dep.getValor("ACUMULA_FONDOS_GEEDP")!=null && !tab_emp_dep.getValor("ACUMULA_FONDOS_GEEDP").isEmpty()){
			if (tab_emp_dep.getValor("ACUMULA_FONDOS_GEEDP").equalsIgnoreCase("1")){
				// si acumula fondos
				return true;
			}
		}
		return false;
	}


	public void cargarRubrosRolVacia(){

		P_NRH_RUBRO_REGION=utilitario.getVariable("p_nrh_rubro_region");
		P_NRH_RUBRO_DESC_VALORES_LIQUIDAR=utilitario.getVariable("p_nrh_rubro_desc_valores_liquidar");
		P_NRH_RUBRO_REMUNERACION_UNIFICADA=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada");
		P_NRH_RUBRO_RMU_HONORARIOS=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada_honorarios");
		P_NRH_RUBRO_ACUMULA_FONDOS=utilitario.getVariable("p_nrh_rubro_acumula_fondos_reserva");
		P_NRH_RUBRO_DIAS_TRABAJADOS=utilitario.getVariable("p_nrh_rubro_dias_trabajados");
		P_NRH_RUBRO_DIAS_ANTIGUEDAD=utilitario.getVariable("p_nrh_rubro_dias_antiguedad");
		P_NRH_RUBRO_DIAS_FONDOS_RESERVA=utilitario.getVariable("p_nrh_rubro_dias_fondos_reserva");
		P_NRH_RUBRO_DIAS_PERIODO=utilitario.getVariable("p_nrh_rubro_dias_periodo_nomina");
		P_NRH_RUBRO_RMU_CARGO_SUBROGANTE=utilitario.getVariable("p_nrh_rubro_rmu_cargo_subrogante");
		P_NRH_RUBRO_DIAS_SUBROGADOS=utilitario.getVariable("p_nrh_rubro_dias_subrogados");
		P_NRH_RUBRO_DIAS_AJUSTE_SUELDO=utilitario.getVariable("p_nrh_rubro_dias_ajuste_sueldo");
		P_NRH_RUBRO_AJUSTE_SUELDO=utilitario.getVariable("p_nrh_rubro_ajuste_sueldo");		
		P_NRH_RUBRO_DIAS_PEND_VACACION=utilitario.getVariable("p_nrh_rubro_dias_pendientes_vacacion");
		P_NRH_ACUMULA_DECIMOS=utilitario.getVariable("p_nrh_rubro_acumula_decimos");		
		P_NRH_BASE_IMPONIBLE_MES_ANTERIOR=utilitario.getVariable("p_nrh_rubro_base_imponible_mes_anterior");
		P_NRH_RUBRO_IMPONIBLE_MES_ANTERIOR=utilitario.getVariable("p_nrh_rubro_imponible_mes_anterior");
		P_NRH_RUBRO_FONRESERV_ACUM_PAGO=utilitario.getVariable("p_nrh_rubro_fondreser_acum_pago_liquidacion");
		P_NRH_RUBRO_FONRESERV_NOM_PAGO=utilitario.getVariable("p_nrh_rubro_fondreser_nomi_pago");	
		
		tab_deta_rubros_rol=getDetalleRubrosRolVacia();
	}


	public TablaGenerica generarRol(TablaGenerica tab_empleados_departamento,TablaGenerica tab_rubros,int nro_dias_comercial,String ide_nrrol,Long ide_inicial){
		long inicio = System.currentTimeMillis();		

		try {

			TablaGenerica tab_cab_rol=utilitario.consultar("SELECT * FROM NRH_ROL where IDE_NRROL="+ide_nrrol);					
			String ide_nrdtn=tab_cab_rol.getValor("IDE_NRDTN");
			String ide_gepro=tab_cab_rol.getValor("IDE_GEPRO");

			System.out.println("ide_nrrol "+ide_nrrol);
			System.out.println("total rubros "+tab_rubros.getTotalFilas()+" rubros "+tab_rubros.getSql());
			System.out.println("numero de empleados "+tab_empleados_departamento.getTotalFilas());
			tab_deta_rubros_rol=getDetalleRubrosRolVacia();
			int indice=0;
			String fecha_final_gepro=getPeriodoRol(ide_gepro).getValor("FECHA_FINAL_GEPRO");
			String fecha_inicial_gepro=getPeriodoRol(ide_gepro).getValor("FECHA_INICIAL_GEPRO");

			P_NRH_RUBRO_REGION=utilitario.getVariable("p_nrh_rubro_region");
			P_NRH_RUBRO_DESC_VALORES_LIQUIDAR=utilitario.getVariable("p_nrh_rubro_desc_valores_liquidar");
			P_NRH_RUBRO_REMUNERACION_UNIFICADA=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada");
			P_NRH_RUBRO_RMU_HONORARIOS=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada_honorarios");
			P_NRH_RUBRO_ACUMULA_FONDOS=utilitario.getVariable("p_nrh_rubro_acumula_fondos_reserva");
			P_NRH_RUBRO_DIAS_TRABAJADOS=utilitario.getVariable("p_nrh_rubro_dias_trabajados");
			P_NRH_RUBRO_DIAS_ANTIGUEDAD=utilitario.getVariable("p_nrh_rubro_dias_antiguedad");
			P_NRH_RUBRO_DIAS_FONDOS_RESERVA=utilitario.getVariable("p_nrh_rubro_dias_fondos_reserva");
			P_NRH_RUBRO_DIAS_PERIODO=utilitario.getVariable("p_nrh_rubro_dias_periodo_nomina");
			P_NRH_RUBRO_RMU_CARGO_SUBROGANTE=utilitario.getVariable("p_nrh_rubro_rmu_cargo_subrogante");
			P_NRH_RUBRO_DIAS_SUBROGADOS=utilitario.getVariable("p_nrh_rubro_dias_subrogados");
			P_NRH_RUBRO_DIAS_AJUSTE_SUELDO=utilitario.getVariable("p_nrh_rubro_dias_ajuste_sueldo");
			P_NRH_RUBRO_AJUSTE_SUELDO=utilitario.getVariable("p_nrh_rubro_ajuste_sueldo");		
			P_NRH_RUBRO_DIAS_PEND_VACACION=utilitario.getVariable("p_nrh_rubro_dias_pendientes_vacacion");
			P_NRH_ACUMULA_DECIMOS=utilitario.getVariable("p_nrh_rubro_acumula_decimos");		
			P_NRH_BASE_IMPONIBLE_MES_ANTERIOR=utilitario.getVariable("p_nrh_rubro_base_imponible_mes_anterior");
			P_NRH_RUBRO_FONRESERV_ACUM_PAGO=utilitario.getVariable("p_nrh_rubro_fondreser_acum_pago");
			P_NRH_RUBRO_FONRESERV_NOM_PAGO=utilitario.getVariable("p_nrh_rubro_fondreser_nomi_pago");	

			String ide_geedp="";
			String ide_gtemp="";
			String fecha_ingreso="";
			String fecha_contrato="";
			String RMU="";
			String acumula_fondos="";
			String acumula_decimos="";
			String IDE_GEREG="";
			String fecha_subroga="";
			String RMU_CARGO_SUBROGA="";
			String fecha_ajuste="";
			String ajuste_sueldo="";
			String fecha_fin_subrogacion="";
			String base_imponible_mes_anterior="";
			String dias_pendientes_vacacion;
			String fondo_reserva_acum_pago="";
			String fondo_reserva_nom_pago="";
			String fecha_ingreso_emgirs="";
			String ide_gttem="";
			String continuidad_liquidacion_geedp="";
			//System.out.println("sql emp "+tab_empleados_departamento.getSql());

			String IDE_NRTIN="";
			try {
				IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");	
			} catch (Exception e) {
				// TODO: handle exception
			}

			str_ide_nramo_descontar="";

			for (int i = 0; i < tab_empleados_departamento.getTotalFilas(); i++) {

				ide_geedp=tab_empleados_departamento.getValor(i, "ide_geedp");
				ide_gtemp=tab_empleados_departamento.getValor(i, "ide_gtemp");
				fecha_ingreso=tab_empleados_departamento.getValor(i, "FECHA_INGRESO_GTEMP");
				fecha_ingreso_emgirs=tab_empleados_departamento.getValor(i, "fecha_ingreso_grupo_gtemp");
				fecha_contrato=tab_empleados_departamento.getValor(i, "FECHA_GEEDP");

				RMU=tab_empleados_departamento.getValor(i, "RMU_GEEDP");
				acumula_fondos=tab_empleados_departamento.getValor(i, "ACUMULA_FONDOS_GEEDP");
				IDE_GEREG=tab_empleados_departamento.getValor(i, "IDE_GEREG");
				fecha_subroga=tab_empleados_departamento.getValor(i,"FECHA_ENCARGO_GEEDP");
				RMU_CARGO_SUBROGA=tab_empleados_departamento.getValor(i,"SUELDO_SUBROGA_GEEDP");
				ajuste_sueldo=tab_empleados_departamento.getValor(i,"AJUSTE_SUELDO_GEEDP");
				fecha_ajuste=tab_empleados_departamento.getValor(i,"FECHA_AJUSTE_GEEDP");
				fecha_fin_subrogacion=tab_empleados_departamento.getValor(i,"FECHA_ENCARGO_FIN_GEEDP");
				dias_pendientes_vacacion=""+0;
				acumula_decimos=tab_empleados_departamento.getValor(i,"ACUMULA_DECIMO_GTEMP");
				ide_gttem=tab_empleados_departamento.getValor(i,"IDE_GTTEM");
				continuidad_liquidacion_geedp=tab_empleados_departamento.getValor(i,"continuidad_liquidacion_geedp");

				//++++pilas aqui enviar parametro para calculo del decimo y ruro anterior
				// calculamos el rol del empleado
				calcularRolIndividual(tab_rubros,ide_gtemp, ide_geedp, ide_nrrol, fecha_ingreso, fecha_contrato,IDE_GEREG,RMU, acumula_fondos, ide_gepro, fecha_inicial_gepro, fecha_final_gepro, fecha_subroga,fecha_fin_subrogacion,RMU_CARGO_SUBROGA,ajuste_sueldo,fecha_ajuste,dias_pendientes_vacacion,ide_inicial,nro_dias_comercial,IDE_NRTIN,acumula_decimos,base_imponible_mes_anterior,fondo_reserva_acum_pago,fondo_reserva_nom_pago,fecha_ingreso_emgirs,ide_gttem,continuidad_liquidacion_geedp);
				ide_inicial=ide_inicial+tab_rubros.getTotalFilas();

			}

			try {
				str_ide_nramo_descontar=str_ide_nramo_descontar.substring(0, str_ide_nramo_descontar.length()-1);
			} catch (Exception e) {
				// TODO: handle exception
			}


			System.out.println("IDE NRAMO A DESCONTAR "+str_ide_nramo_descontar);
			String strSql="update NRH_AMORTIZACION set IDE_NRROL="+ide_nrrol+" where IDE_NRAMO in ("+str_ide_nramo_descontar+") ";

			if (!str_ide_nramo_descontar.isEmpty()){
				System.out.println("generarRol strSql: "+strSql);
				utilitario.getConexion().ejecutarSql(strSql);
			}
			utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_RENTA_NRROL=0 where IDE_NRROL="+ide_nrrol);
			utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_CALCULADO_NRROL=1 where IDE_NRROL="+ide_nrrol);




		} catch (Exception e) {
			utilitario.crearError("EN EL METODO GENERAR ROL", "NO SE PUDO GENERAR LA NOMINA", e);
			// TODO: handle exception
		}

		long fin = System.currentTimeMillis();

		System.out.println("======== Tiempo total (TODO EL PROCESO) guardar pantalla : " + (fin - inicio));
		return tab_deta_rubros_rol;
	}

	public TablaGenerica generarRolLiquidacion(TablaGenerica tab_empleados_departamento,TablaGenerica tab_rubros,int nro_dias_comercial,String ide_nrrol,Long ide_inicial){
		long inicio = System.currentTimeMillis();		

		try {

			TablaGenerica tab_cab_rol=utilitario.consultar("SELECT * FROM NRH_ROL where IDE_NRROL="+ide_nrrol);					
			String ide_nrdtn=tab_cab_rol.getValor("IDE_NRDTN");
			String ide_gepro=tab_cab_rol.getValor("IDE_GEPRO");
			
			System.out.println("LIQUIDACION ide_nrrol "+ide_nrrol);
			System.out.println("total rubros "+tab_rubros.getTotalFilas()+" rubros "+tab_rubros.getSql());
			System.out.println("numero de empleados "+tab_empleados_departamento.getTotalFilas());
			tab_deta_rubros_rol=getDetalleRubrosRolVacia();

			int indice=0;
			String fecha_final_gepro=getPeriodoRol(ide_gepro).getValor("FECHA_FINAL_GEPRO");
			String fecha_inicial_gepro=getPeriodoRol(ide_gepro).getValor("FECHA_INICIAL_GEPRO");


			if (getDetalleTipoNomina(ide_nrdtn).getValor("IDE_GTTEM").equalsIgnoreCase(utilitario.getVariable("p_gth_tipo_empleado_codigo"))){
				fecha_final_gepro=getPeriodoRol(ide_gepro).getValor("FECHA_INICIAL_GEPRO");
				fecha_inicial_gepro=utilitario.getAnio(fecha_final_gepro)+"-"+utilitario.getMes(fecha_final_gepro)+"-1";
			}else{
				String fecha_ini=getPeriodoRol(ide_gepro).getValor("FECHA_INICIAL_GEPRO");
				fecha_final_gepro=utilitario.getAnio(fecha_ini)+"-"+utilitario.getMes(fecha_ini)+"-30";
				fecha_inicial_gepro=utilitario.getAnio(fecha_ini)+"-"+utilitario.getMes(fecha_ini)+"-1";
			}


			P_NRH_RUBRO_REGION=utilitario.getVariable("p_nrh_rubro_region");
			P_NRH_RUBRO_DESC_VALORES_LIQUIDAR=utilitario.getVariable("p_nrh_rubro_desc_valores_liquidar");
			P_NRH_RUBRO_REMUNERACION_UNIFICADA=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada");
			P_NRH_RUBRO_RMU_HONORARIOS=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada_honorarios");
			P_NRH_RUBRO_ACUMULA_FONDOS=utilitario.getVariable("p_nrh_rubro_acumula_fondos_reserva");
			P_NRH_RUBRO_DIAS_TRABAJADOS=utilitario.getVariable("p_nrh_rubro_dias_trabajados");
			P_NRH_RUBRO_DIAS_ANTIGUEDAD=utilitario.getVariable("p_nrh_rubro_dias_antiguedad");
			P_NRH_RUBRO_DIAS_FONDOS_RESERVA=utilitario.getVariable("p_nrh_rubro_dias_fondos_reserva");
			P_NRH_RUBRO_DIAS_PERIODO=utilitario.getVariable("p_nrh_rubro_dias_periodo_nomina");
			P_NRH_RUBRO_RMU_CARGO_SUBROGANTE=utilitario.getVariable("p_nrh_rubro_rmu_cargo_subrogante");
			P_NRH_RUBRO_DIAS_SUBROGADOS=utilitario.getVariable("p_nrh_rubro_dias_subrogados");
			P_NRH_RUBRO_DIAS_AJUSTE_SUELDO=utilitario.getVariable("p_nrh_rubro_dias_ajuste_sueldo");
			P_NRH_RUBRO_AJUSTE_SUELDO=utilitario.getVariable("p_nrh_rubro_ajuste_sueldo");		
			P_NRH_RUBRO_DIAS_PEND_VACACION=utilitario.getVariable("p_nrh_rubro_dias_pendientes_vacacion");
			P_NRH_ACUMULA_DECIMOS=utilitario.getVariable("p_nrh_rubro_acumula_decimos");		
			P_NRH_BASE_IMPONIBLE_MES_ANTERIOR=utilitario.getVariable("p_nrh_rubro_base_imponible_mes_anterior");
			P_NRH_RUBRO_FONRESERV_ACUM_PAGO=utilitario.getVariable("p_nrh_rubro_fondreser_acum_pago_liquidacion");
			P_NRH_RUBRO_FONRESERV_NOM_PAGO=utilitario.getVariable("p_nrh_rubro_fondreser_nomi_pago");	

			String ide_geedp="";
			String ide_gtemp="";
			String fecha_ingreso="";
			String fecha_contrato="";
			String RMU="";
			String acumula_fondos="";
			String acumula_decimos="";
			String IDE_GEREG="";
			String fecha_subroga="";
			String RMU_CARGO_SUBROGA="";
			String fecha_ajuste="";
			String ajuste_sueldo="";
			String fecha_fin_subrogacion="";
			String dias_pendientes_vacacion;
			String base_imponible_mes_anterior="";
			String fondo_reserva_acum_pago="";
			String fondo_reserva_nom_pago="";
			String fecha_ingreso_emgirs="";
			String ide_gttem="";
			String continuidad_liquidacion_geedp="";
			
			
			int num_dias_exceso_liquidacion=0;

			boolean banderaExcesoDias=false;
			//System.out.println("sql emp "+tab_empleados_departamento.getSql());

			String IDE_NRTIN="";
			try {
				IDE_NRTIN=getDetalleTipoNomina(getRol(ide_nrrol).getValor("ide_nrdtn")).getValor("IDE_NRTIN");	
			} catch (Exception e) {
				// TODO: handle exception
			}

			str_ide_nramo_descontar="";

			for (int i = 0; i < tab_empleados_departamento.getTotalFilas(); i++) {

				ide_geedp=tab_empleados_departamento.getValor(i, "ide_geedp");
				System.out.println("EMPLEADO"+ide_geedp);
				num_dias_exceso_liquidacion=getValorDiasExceso(Integer.parseInt(ide_geedp));
				ide_gtemp=tab_empleados_departamento.getValor(i, "ide_gtemp");
				fecha_ingreso=tab_empleados_departamento.getValor(i, "FECHA_INGRESO_GTEMP");
				fecha_ingreso_emgirs=tab_empleados_departamento.getValor(i, "fecha_ingreso_grupo_gtemp");
				fecha_contrato=tab_empleados_departamento.getValor(i, "FECHA_GEEDP");

				RMU=tab_empleados_departamento.getValor(i, "RMU_GEEDP");
				acumula_fondos=tab_empleados_departamento.getValor(i, "ACUMULA_FONDOS_GEEDP");
				acumula_decimos=tab_empleados_departamento.getValor(i, "ACUMULA_DECIMO_GTEMP");
				IDE_GEREG=tab_empleados_departamento.getValor(i, "IDE_GEREG");
				fecha_subroga=tab_empleados_departamento.getValor(i,"FECHA_ENCARGO_GEEDP");
				RMU_CARGO_SUBROGA=tab_empleados_departamento.getValor(i,"SUELDO_SUBROGA_GEEDP");
				ajuste_sueldo=tab_empleados_departamento.getValor(i,"AJUSTE_SUELDO_GEEDP");
				fecha_ajuste=tab_empleados_departamento.getValor(i,"FECHA_AJUSTE_GEEDP");
				fecha_fin_subrogacion=tab_empleados_departamento.getValor(i,"FECHA_ENCARGO_FIN_GEEDP");
				dias_pendientes_vacacion="0";//tab_empleados_departamento.getValor(i,"DIAS_VACACION");
				ide_gttem=tab_empleados_departamento.getValor(i,"IDE_GTTEM");
				continuidad_liquidacion_geedp=tab_empleados_departamento.getValor(i,"continuidad_liquidacion_geedp");
				if (continuidad_liquidacion_geedp==null || continuidad_liquidacion_geedp.isEmpty()) {
					continuidad_liquidacion_geedp="false";
				}
				
				
				
				banderaExcesoDias=diasExcesoLiquidacion(ide_geedp, ide_gepro);
				
				
				
				// calculamos el rol del empleado
				calcularRolIndividualLiquidacion(tab_rubros,ide_gtemp, ide_geedp, ide_nrrol, fecha_ingreso, fecha_contrato,IDE_GEREG,RMU, acumula_fondos, ide_gepro, fecha_inicial_gepro, fecha_final_gepro, fecha_subroga,fecha_fin_subrogacion,RMU_CARGO_SUBROGA,ajuste_sueldo,fecha_ajuste,dias_pendientes_vacacion,ide_inicial,nro_dias_comercial,IDE_NRTIN,acumula_decimos,base_imponible_mes_anterior,fondo_reserva_acum_pago,fondo_reserva_nom_pago,fecha_ingreso_emgirs,ide_gttem,num_dias_exceso_liquidacion,continuidad_liquidacion_geedp);
				ide_inicial=ide_inicial+tab_rubros.getTotalFilas();

			}

			try {
				//str_ide_nramo_descontar=str_ide_nramo_descontar.substring(0, str_ide_nramo_descontar.length()-1);
				//str_ide_nramo_descontar;
				
			} catch (Exception e) {
				// TODO: handle exception
			}
	

			utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_RENTA_NRROL=1 where IDE_NRROL="+ide_nrrol);
			utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_CALCULADO_NRROL=1 where IDE_NRROL="+ide_nrrol);

		} catch (Exception e) {
			utilitario.crearError("EN EL METODO GENERAR ROL", "NO SE PUDO GENERAR LA NOMINA", e);
			// TODO: handle exception
		}

		long fin = System.currentTimeMillis();

		System.out.println("======== Tiempo total (TODO EL PROCESO) guardar pantalla : " + (fin - inicio));
		return tab_deta_rubros_rol;
	}


	public String getIdeNrderDecimos(String ide_nrdtn,String ide_gepro){
		String str_ide_nrder_decimos="";
		int anio_periodo=utilitario.getAnio(getPeriodoRol(ide_gepro).getValor("fecha_inicial_gepro"));
		TablaGenerica tab_decimos=utilitario.consultar("select * from NRH_DETALLE_RUBRO " +
				"where IDE_NRDTN="+ide_nrdtn+" " +
				"and ide_nrrub in (select IDE_NRRUB from NRH_RUBRO where DECIMO_NRRUB=true) ");

		for (int j = 0; j < tab_decimos.getTotalFilas(); j++) {

			String fecha_pago_nrder=tab_decimos.getValor(j,"FECHA_PAGO_NRDER");
			try {
				fecha_pago_nrder=fecha_pago_nrder.substring(0, fecha_pago_nrder.indexOf("/"));
			} catch (Exception e) {
				// TODO: handle exception
			}

			String fecha_pago_rubro=""+anio_periodo+"-"+fecha_pago_nrder;


			TablaGenerica tab_per=utilitario.consultar("select * from GEN_PERIDO_ROL where IDE_GEPRO="+ide_gepro+" " +
					"and to_date('"+fecha_pago_rubro+"','yyyy-mm-dd') BETWEEN FECHA_INICIAL_GEPRO and FECHA_FINAL_GEPRO");
			if (tab_per.getTotalFilas()>0){
				str_ide_nrder_decimos+=tab_decimos.getValor(j,"IDE_NRDER")+",";
			}
		}
		try {
			str_ide_nrder_decimos=str_ide_nrder_decimos.substring(0,str_ide_nrder_decimos.length()-1);
		} catch (Exception e) {

			// TODO: handle exception
		}
		return str_ide_nrder_decimos;
	}


	/**Genera el rol de los empleados enviados segun el tipo de nomina de la Cabecera del Rol
	 * @param tab_empleados_departamento
	 * @param ide_nrrol
	 * @param ide_inicial
	 * @return
	 */
	public synchronized TablaGenerica calcularRol(TablaGenerica tab_empleados_departamento,String ide_nrrol,Long ide_inicial){	
		// obtengo el ide de detalle de tipo de nomina y el ide del periodo de rol de la cabecera de rol (nrh_rol)
		TablaGenerica tab_cab_rol=utilitario.consultar("SELECT * FROM NRH_ROL where IDE_NRROL="+ide_nrrol);					
		String ide_nrdtn=tab_cab_rol.getValor("IDE_NRDTN");// detalle de tipo de nomina
		String ide_gepro=tab_cab_rol.getValor("IDE_GEPRO");// periodo de rol

		// obtengo el Tipo de nomina de la tabla detalle de tipo de nomina (Normal,Liquidacion,Pago de Decimos)
		String IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");
		// obtengo del tipo de rol (mensual, quincenal)
		String ide_nrtit=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIT");
		int nro_dias_comercial_nrtit=0;
		try {
			nro_dias_comercial_nrtit=pckUtilidades.CConversion.CInt(getTipoRol(ide_nrtit).getValor("NRO_DIAS_COMERCIAL_NRTIT"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		//if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos")))
		if (utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos").contains(IDE_NRTIN))
		{
			// si es tipo de nomina pago de decimos
			return generarDecimos(tab_empleados_departamento,ide_nrrol,ide_inicial,getIdeNrderDecimos(ide_nrdtn, ide_gepro),nro_dias_comercial_nrtit);
		}else if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			// si es tipo de nomina liquidacion 
			// obtengo los rubros configurados para el tipo de nomina que se va a generar
			TablaGenerica tab_rubros = getRubrosTipoNomina(ide_nrdtn);
			return generarRolLiquidacion(tab_empleados_departamento, tab_rubros,nro_dias_comercial_nrtit ,ide_nrrol, ide_inicial);
		}else {
			// si es tipo de nomina normal 
			// obtengo los rubros configurados para el tipo de nomina que se va a generar
			TablaGenerica tab_rubros = getRubrosTipoNomina(ide_nrdtn);
			return generarRol(tab_empleados_departamento, tab_rubros,nro_dias_comercial_nrtit ,ide_nrrol, ide_inicial);
		}
	}


	public TablaGenerica generarDecimos(TablaGenerica tab_empleados_departamento,String ide_nrrol,Long ide_inicial,String ide_nrder_decimos,int nro_dia_comercial){	
		long inicio = System.currentTimeMillis();
		System.out.println("pago de nomina ingrso luis");
		TablaGenerica tab_cab_rol=utilitario.consultar("SELECT * FROM NRH_ROL where IDE_NRROL="+ide_nrrol);					
		String ide_nrdtn=tab_cab_rol.getValor("IDE_NRDTN");
		String ide_gepro=tab_cab_rol.getValor("IDE_GEPRO");

		tab_deta_rubros_rol=getDetalleRubrosRolVacia();

		String fecha_final_gepro=getPeriodoRol(ide_gepro).getValor("FECHA_FINAL_GEPRO");
		String fecha_inicial_gepro=getPeriodoRol(ide_gepro).getValor("FECHA_INICIAL_GEPRO");

		// obtengo los rubros configurados para el tipo de nomina PAGO DE DECIMOS
		TablaGenerica tab_rubros = getRubrosTipoNominaDecimos(ide_nrdtn,ide_nrder_decimos);


		String str_sql_emp_reg_pago="select * from ( ";
		str_sql_emp_reg_pago+=tab_empleados_departamento.getSql();
		str_sql_emp_reg_pago+=" )a ";
		str_sql_emp_reg_pago+="where a.ide_gereg in (select coalesce(c.ide_gereg,0)  from ("+tab_rubros.getSql()+")c group by c.ide_gereg) " +
				"order by nombres";

		TablaGenerica tab_empleados_region_pago=utilitario.consultar(str_sql_emp_reg_pago);

		P_NRH_RUBRO_REGION=utilitario.getVariable("p_nrh_rubro_region");
		P_NRH_RUBRO_DESC_VALORES_LIQUIDAR=utilitario.getVariable("p_nrh_rubro_desc_valores_liquidar");
		P_NRH_RUBRO_REMUNERACION_UNIFICADA=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada");
		P_NRH_RUBRO_RMU_HONORARIOS=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada_honorarios");
		P_NRH_RUBRO_ACUMULA_FONDOS=utilitario.getVariable("p_nrh_rubro_acumula_fondos_reserva");
		P_NRH_RUBRO_DIAS_TRABAJADOS=utilitario.getVariable("p_nrh_rubro_dias_trabajados");
		P_NRH_RUBRO_DIAS_ANTIGUEDAD=utilitario.getVariable("p_nrh_rubro_dias_antiguedad");
		P_NRH_RUBRO_DIAS_FONDOS_RESERVA=utilitario.getVariable("p_nrh_rubro_dias_fondos_reserva");
		P_NRH_RUBRO_DIAS_PERIODO=utilitario.getVariable("p_nrh_rubro_dias_periodo_nomina");
		P_NRH_RUBRO_RMU_CARGO_SUBROGANTE=utilitario.getVariable("p_nrh_rubro_rmu_cargo_subrogante");
		P_NRH_RUBRO_DIAS_SUBROGADOS=utilitario.getVariable("p_nrh_rubro_dias_subrogados");
		P_NRH_RUBRO_DIAS_AJUSTE_SUELDO=utilitario.getVariable("p_nrh_rubro_dias_ajuste_sueldo");
		P_NRH_RUBRO_AJUSTE_SUELDO=utilitario.getVariable("p_nrh_rubro_ajuste_sueldo");		
		P_NRH_RUBRO_DIAS_PEND_VACACION=utilitario.getVariable("p_nrh_rubro_dias_pendientes_vacacion");
		P_NRH_ACUMULA_DECIMOS=utilitario.getVariable("p_nrh_rubro_acumula_decimos");		
		P_NRH_BASE_IMPONIBLE_MES_ANTERIOR=utilitario.getVariable("p_nrh_rubro_base_imponible_mes_anterior");
		P_NRH_RUBRO_FONRESERV_ACUM_PAGO=utilitario.getVariable("p_nrh_rubro_fondreser_acum_pago");
		P_NRH_RUBRO_FONRESERV_NOM_PAGO=utilitario.getVariable("p_nrh_rubro_fondreser_nomi_pago");	

		System.out.println("generar decimos");
		System.out.println("sql rubros "+tab_rubros.getSql());
		System.out.println("sql emp "+tab_empleados_region_pago.getSql());
		str_ide_nramo_descontar="";

		for (int i = 0; i < tab_empleados_region_pago.getTotalFilas(); i++) {
			String ide_geedp=tab_empleados_region_pago.getValor(i, "ide_geedp");
			String ide_gtemp=tab_empleados_region_pago.getValor(i, "ide_gtemp");
			String fecha_ingreso=tab_empleados_region_pago.getValor(i, "FECHA_INGRESO_GTEMP");
			//String fecha_ingreso_emgirs=tab_empleados_region_pago.getValor(i, "fecha_ingreso_grupo_gtemp");
			String fecha_contrato=tab_empleados_region_pago.getValor(i, "FECHA_GEEDP");
			String RMU=tab_empleados_region_pago.getValor(i, "RMU_GEEDP");
			String acumula_fondos=tab_empleados_region_pago.getValor(i, "ACUMULA_FONDOS_GEEDP");
			String IDE_GEREG=tab_empleados_region_pago.getValor(i, "IDE_GEREG");
			//String dias_pend_vacacion=tab_empleados_region_pago.getValor(i, "DIAS_VACACION");
			String dias_pend_vacacion=""+0;
			// calculamos el rol del empleado
			String fecha_ingreso_emgirs="";
			String ide_gttem=tab_empleados_region_pago.getValor(i, "IDE_GTTEM");;

			System.out.println("Empleaaaaaado"+ide_gtemp);

			TablaGenerica tab_empleado_departamento_par=null;
			tab_empleado_departamento_par=utilitario.consultar("select ide_geedp,ide_geded,fecha_geedp  "
					+ "from gen_empleados_departamento_par  "
					+ "where ide_gtemp="+ide_gtemp+" "
							+ "order by ide_geedp DESC  ");
							//+ "limit 1");
			
			String cambioContratoPorEmpleado="";
			//tabla gen_accion_motivo_empleado  
			TablaGenerica tab_accion_motivo_empleado=null;
			//for (int i = tab_empleado_departamento_par.getTotal
			for (int x = 0; x < tab_empleado_departamento_par.getTotalFilas(); x++) {

		
			cambioContratoPorEmpleado=tab_empleado_departamento_par.getValor(x,"IDE_GEDED");
			//Los ide_geame   que son los motivos de la tabla gen_accion_motivo_empleado  
			tab_accion_motivo_empleado=utilitario.consultar("SELECT ide_geded,ide_geame "
					+ "FROM gen_detalle_empleado_departame "
					+ "where ide_geded  in("+cambioContratoPorEmpleado+") ORDER BY IDE_GEDED DESC ");
			String motivoContratoPorEmpleado="";
			boolean estadoContratacion=false;
		//	for (int j = 0; j < tab_accion_motivo_empleado.getTotalFilas()-1; j++) {
				motivoContratoPorEmpleado=tab_accion_motivo_empleado.getValor("IDE_GEAME");
				
				
			TablaGenerica tabTerminacionContrato=null;
			tabTerminacionContrato=utilitario.consultar("SELECT geame.ide_geame, geame.ide_geaed, geame.ide_gemed,  "
					+ "geame.activo_geame,geame.anterior_geame,geaed.detalle_geaed "
					+ "FROM  "
					+ "gen_accion_motivo_empleado geame "
					+ "left join gen_accion_empleado_depa  geaed on geaed.ide_geaed=geame.ide_geaed "
					+ "where geame.ide_geame="+motivoContratoPorEmpleado);
			
			
			
				
				if (tabTerminacionContrato.getValor("IDE_GEMED").equals("23")) {
					estadoContratacion=true;
					System.out.println("Empleado: "+ide_gtemp);
					fecha_ingreso_emgirs=tab_empleado_departamento_par.getValor(x,"fecha_geedp");
					x=tab_empleado_departamento_par.getTotalFilas();
				}else {
					estadoContratacion=false;
					fecha_ingreso_emgirs=tab_empleados_region_pago.getValor(i, "fecha_ingreso_grupo_gtemp");
					
				}
			
			
			}
			
			
			
			
			System.out.println("EMPLEADO:  "+ide_gtemp+" ide_geedp:  "+ide_geedp+" ide_nrrol:  "+ide_nrrol+" fecha_ingreso:  "+fecha_ingreso+" fecha_contrato:  "+fecha_contrato+" IDE_GEREG: "+IDE_GEREG+" acumula fondos:  "+acumula_fondos+" ide_gepro:  "+ide_gepro+" fecha_contrato:  "+fecha_contrato);

			
			
			

			calcularDecimoRolIndividual(tab_rubros,ide_gtemp, ide_geedp, ide_nrrol, fecha_ingreso, fecha_contrato,IDE_GEREG,RMU, acumula_fondos, ide_gepro, 
					fecha_inicial_gepro, fecha_final_gepro,dias_pend_vacacion,ide_inicial,nro_dia_comercial,fecha_ingreso_emgirs,ide_gttem);
			ide_inicial=ide_inicial+tab_rubros.getTotalFilas();

		}
		try {
			str_ide_nramo_descontar=str_ide_nramo_descontar.substring(0, str_ide_nramo_descontar.length()-1);
		} catch (Exception e) {
			// TODO: handle exception
		}

		System.out.println("nramo a descontar "+str_ide_nramo_descontar);
		String strSql="update NRH_AMORTIZACION set IDE_NRROL="+ide_nrrol+" where IDE_NRAMO in ("+str_ide_nramo_descontar+") ";

		if (!str_ide_nramo_descontar.isEmpty()){
			System.out.println("generarDecimos strSql: "+strSql);
			utilitario.getConexion().ejecutarSql(strSql);
		}

		utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_RENTA_NRROL=1 where IDE_NRROL="+ide_nrrol);
		utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_CALCULADO_NRROL=1 where IDE_NRROL="+ide_nrrol);
		long fin = System.currentTimeMillis();
		System.out.println("======== Tiempo total (TODO EL PROCESO) guardar pantalla : " + (fin - inicio));
		return tab_deta_rubros_rol;
	}

	public void calcularDecimoRolIndividual(TablaGenerica tab_rubros,String ide_gtemp,String ide_geedp,String ide_nrrol,String fecha_ingreso,String fecha_contrato,
			String IDE_GEREG,String RMU,String acumula_fondos,String ide_gepro,String fecha_inicial_gepro,String fecha_final_gepro,String dias_pend_vacacion,Long ide_inicial,
			int nro_dias_comercial,String fecha_ingreso_emgirs,String ide_gttem){

		boolean boo_tiene_anticipos=false;
		tab_cuotas=serv_anticipo.getCuotasAnticipoPorPagarEmpleado(ide_gtemp, ide_gepro,"");

		if (tab_cuotas.getTotalFilas()>0){
			boo_tiene_anticipos=true;
		}

		
		// insertarmos todos los rubros del empleado e importamos los valores unicamente de los rubros de forma de calculo (IMPORTADO)
		insertarDetallesRolEmpleado(ide_geedp, ide_nrrol, tab_rubros,false, boo_tiene_anticipos, IDE_GEREG,RMU, acumula_fondos, fecha_ingreso, fecha_contrato, ide_gepro, 
				fecha_inicial_gepro, fecha_final_gepro,"","","","","",dias_pend_vacacion,nro_dias_comercial,false,"","","","",fecha_ingreso_emgirs,ide_gttem,0,"","");

		int indice=tab_rubros.getTotalFilas();

		despejarFormulasRol(indice, ide_geedp, fecha_final_gepro);


		String str_valor="";
		for (int l = 0; l < tab_rubros.getTotalFilas(); l++) {
			ide_inicial=ide_inicial+1;
			if (tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO")!=null && !tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO").isEmpty()){

				try {
					BigDecimal big_valor=new BigDecimal(pckUtilidades.CConversion.CDbl_2(tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO")));
					big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
					str_valor=big_valor+"";
				} catch (Exception e) {
					str_valor="0.00";
				}

			}else{
				str_valor="0.00";
			}
			String orden=tab_deta_rubros_rol.getValor(l,"ORDEN_CALCULO_NRDRO");
			if (orden.isEmpty()){
				orden=null;
			}

			String str_insert="INSERT INTO NRH_DETALLE_ROL (IDE_NRDRO, IDE_NRROL, IDE_GEEDP, IDE_NRDER, VALOR_NRDRO, USUARIO_INGRE, FECHA_INGRE, HORA_INGRE, USUARIO_ACTUA, FECHA_ACTUA, HORA_ACTUA,ORDEN_CALCULO_NRDRO) " +
					"VALUES ("+ide_inicial+", " +
					""+tab_deta_rubros_rol.getValor(l,"IDE_NRROL")+", " +
					""+tab_deta_rubros_rol.getValor(l,"IDE_GEEDP")+", " +
					""+tab_deta_rubros_rol.getValor(l,"IDE_NRDER")+", " +
					""+str_valor+", NULL, NULL, NULL, NULL, NULL, NULL, " +
					""+orden+")";
			
			utilitario.getConexion().ejecutarSql(str_insert);
		}
	}


	public void validarEmpleadosInvalidos(String ide_nrrol,String ide_nrdtn,String fecha_fin_gepro){

		TablaGenerica tab_empleados_generados=utilitario.consultar(getSqlEmpleadosRol(ide_nrrol));

		TablaGenerica tab_empleados_por_generar=utilitario.consultar(getSqlEmpleadosTipoNomina(ide_nrdtn,fecha_fin_gepro));


		String IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");

		//if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos")))
		if (utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos").contains(IDE_NRTIN))
		{			
			TablaGenerica tab_drol=getDetalleRol(ide_nrrol);// coje los detalles de rol de todos los empleados

			String str_sql_emp_reg_pago="select * from ( ";
			str_sql_emp_reg_pago+=tab_empleados_por_generar.getSql();
			str_sql_emp_reg_pago+=" )a ";
			str_sql_emp_reg_pago+="where a.ide_gereg in (select c.ide_gereg from ("+tab_drol.getSql()+")c group by c.ide_gereg) " +
					"order by nombres";

			// lleno los empleados solo si es decimos  
			tab_empleados_por_generar=utilitario.consultar(str_sql_emp_reg_pago);
		}


		TablaGenerica tab_emp_demas=utilitario.consultar("select * from ( " +
				"SELECT a.ide_geedp,b.ide_geedp as ide_geedp_nulo FROM ( " +
				""+tab_empleados_generados.getSql()+" " +
				")a " +
				"left join ( " +
				""+tab_empleados_por_generar.getSql()+" " +
				")b " +
				"on a.ide_geedp=b.ide_geedp " +
				")a where a.ide_geedp_nulo is NULL ");


		for (int i = 0; i < tab_emp_demas.getTotalFilas(); i++) {
			if (tab_emp_demas.getValor(i,"IDE_GEEDP")!=null && !tab_emp_demas.getValor(i,"IDE_GEEDP").isEmpty()){
				utilitario.getConexion().ejecutarSql("delete from nrh_detalle_rol where ide_nrrol="+ide_nrrol+" and ide_geedp="+tab_emp_demas.getValor(i,"IDE_GEEDP"));
				System.out.println("elimina empleado demas "+tab_emp_demas.getValor(i,"IDE_GEEDP"));
			}
		}	



	}


	public void reCalcularNomina(String ide_nrdtn,String ide_gepro,String ide_nrtit,boolean decimos){
		long inicio = System.currentTimeMillis();
		System.out.println("RECALCULAR "+ide_nrdtn);

		TablaGenerica tab_rol=getRol(ide_nrdtn,ide_gepro);

		String str_ide_nrrol=tab_rol.getValor("IDE_NRROL");

		TablaGenerica tab_per_rol=getPeriodoRol(tab_rol.getValor("IDE_GEPRO"));

		String fecha_ini_gepro="";
		String fecha_fin_gepro="";

 
		// obtengo el Tipo de nomina de la tabla detalle de tipo de nomina (Normal,Liquidacion,Pago de Decimos)
		String IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			if (getDetalleTipoNomina(ide_nrdtn).getValor("IDE_GTTEM").equalsIgnoreCase(utilitario.getVariable("p_gth_tipo_empleado_codigo"))){
				fecha_fin_gepro=getPeriodoRol(ide_gepro).getValor("FECHA_INICIAL_GEPRO");
				fecha_ini_gepro=utilitario.getAnio(fecha_fin_gepro)+"-"+utilitario.getMes(fecha_fin_gepro)+"-1";
			}else{
				String fecha_ini=getPeriodoRol(ide_gepro).getValor("FECHA_INICIAL_GEPRO");
				fecha_fin_gepro=utilitario.getAnio(fecha_ini)+"-"+utilitario.getMes(fecha_ini)+"-30";
				fecha_ini_gepro=utilitario.getAnio(fecha_ini)+"-"+utilitario.getMes(fecha_ini)+"-1";
			}		
		}else{
			fecha_ini_gepro=tab_per_rol.getValor("FECHA_INICIAL_GEPRO");
			fecha_fin_gepro=tab_per_rol.getValor("FECHA_FINAL_GEPRO");
		}

		utilitario.getConexion().ejecutarSql("update  NRH_AMORTIZACION set ACTIVO_NRAMO=false " +
				"where FECHA_VENCIMIENTO_NRAMO " +
				"BETWEEN to_date ('"+fecha_ini_gepro+"','yyyy-mm-dd') and to_date ('"+fecha_fin_gepro+"','yyyy-mm-dd') " +
				"and IDE_NRANI in (select ide_nrani from NRH_ANTICIPO_INTERES " +
				"where IDE_NRANT in (select ide_nrant from NRH_ANTICIPO " +
				"where IDE_GTEMP in (select emp.ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM " +
				//				"and EDP.IDE_SUCU=DTN.IDE_SUCU " +
				"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP " +
				"WHERE DTN.IDE_NRDTN IN ("+ide_nrdtn+")) " +
				"AND ACTIVO_NRANT =true ))");


		// encera el rubro impuesto a la renta mensual de todos los empleados del rol
		utilitario.getConexion().ejecutarSql("update NRH_DETALLE_ROL set valor_nrdro=0 where IDE_NRROL="+str_ide_nrrol+" " +
				"and IDE_NRDER =  (select IDE_NRDER from NRH_DETALLE_RUBRO where IDE_NRDTN="+ide_nrdtn+" and IDE_NRRUB="+utilitario.getVariable("p_nrh_rubro_impuesto_renta_mensual")+")");


		TablaGenerica tab_empleados_departamento=utilitario.consultar(getSqlEmpleadosRolLiquidacionRecalcular(str_ide_nrrol));
				//utilitario.consultar(getSqlEmpleadosNomina(str_ide_nrrol));


		// coje los detalles de rol de todos los empleados
		TablaGenerica tab_drol=getDetalleRol(str_ide_nrrol);

		if (decimos){
			// cojo los empleados solo de la region que corresponde el pago de decimo  

			String str_sql_emp_reg_pago="select * from ( ";
			str_sql_emp_reg_pago+=tab_empleados_departamento.getSql();
			str_sql_emp_reg_pago+=" )a ";
			str_sql_emp_reg_pago+="where a.ide_gereg in (select c.ide_gereg from ("+tab_drol.getSql()+")c group by c.ide_gereg) " +
					"order by nombres";
			tab_empleados_departamento=utilitario.consultar(str_sql_emp_reg_pago);
		}

		System.out.println("emp "+tab_empleados_departamento.getSql());

		System.out.println("tab drol "+tab_drol.getSql());

		P_NRH_RUBRO_REGION=utilitario.getVariable("p_nrh_rubro_region");
		P_NRH_RUBRO_DESC_VALORES_LIQUIDAR=utilitario.getVariable("p_nrh_rubro_desc_valores_liquidar");
		P_NRH_RUBRO_REMUNERACION_UNIFICADA=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada");
		P_NRH_RUBRO_RMU_HONORARIOS=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada_honorarios");
		P_NRH_RUBRO_ACUMULA_FONDOS=utilitario.getVariable("p_nrh_rubro_acumula_fondos_reserva");
		P_NRH_RUBRO_DIAS_TRABAJADOS=utilitario.getVariable("p_nrh_rubro_dias_trabajados");
		P_NRH_RUBRO_DIAS_ANTIGUEDAD=utilitario.getVariable("p_nrh_rubro_dias_antiguedad");
		P_NRH_RUBRO_DIAS_FONDOS_RESERVA=utilitario.getVariable("p_nrh_rubro_dias_fondos_reserva");
		P_NRH_RUBRO_DIAS_PERIODO=utilitario.getVariable("p_nrh_rubro_dias_periodo_nomina");
		P_NRH_RUBRO_RMU_CARGO_SUBROGANTE=utilitario.getVariable("p_nrh_rubro_rmu_cargo_subrogante");
		P_NRH_RUBRO_DIAS_SUBROGADOS=utilitario.getVariable("p_nrh_rubro_dias_subrogados");
		P_NRH_RUBRO_DIAS_AJUSTE_SUELDO=utilitario.getVariable("p_nrh_rubro_dias_ajuste_sueldo");
		P_NRH_RUBRO_AJUSTE_SUELDO=utilitario.getVariable("p_nrh_rubro_ajuste_sueldo");		
		P_NRH_RUBRO_DIAS_PEND_VACACION=utilitario.getVariable("p_nrh_rubro_dias_pendientes_vacacion");
		P_NRH_ACUMULA_DECIMOS=utilitario.getVariable("p_nrh_rubro_acumula_decimos");		
		P_NRH_BASE_IMPONIBLE_MES_ANTERIOR=utilitario.getVariable("p_nrh_rubro_base_imponible_mes_anterior");
		P_NRH_RUBRO_FONRESERV_ACUM_PAGO=utilitario.getVariable("p_nrh_rubro_fondreser_acum_pago");
		P_NRH_RUBRO_FONRESERV_NOM_PAGO=utilitario.getVariable("p_nrh_rubro_fondreser_nomi_pago");	


		String ide_geedp="";
		String ide_gtemp="";
		String fecha_ingreso="";
		String fecha_contrato="";
		String RMU="";
		String IDE_GEREG="";
		String acumula_fondos="";
		String acumula_decimos="";
		String fecha_subroga="";
		String RMU_CARGO_SUBROGA="";
		String ajuste_sueldo="";
		String fecha_ajuste="";
		String fecha_fin_subrogacion="";
		String dias_pendientes_vacacion="";
		String base_imponible_mes_anterior="";
		String fondo_reserva_acum_pago="";
		String fondo_reserva_nom_pago="";
		String fecha_ingreso_emgirs="";
		String ide_gttem="";
		String continuidad_liquidacion_geedp="";

		TablaGenerica tab_rub_faltantes=getRubrosFaltantes(str_ide_nrrol);
		if (tab_rub_faltantes.getTotalFilas()>0){
			utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'NRH_DETALLE_ROL'");
			ide_inicial=utilitario.getConexion().getMaximo("NRH_DETALLE_ROL", "IDE_NRDRO", 1);
		}

		System.out.println("tab rub_faltantes "+tab_rub_faltantes.getSql());

		for (int j = 0; j < tab_empleados_departamento.getTotalFilas(); j++) {


			ide_geedp=tab_empleados_departamento.getValor(j, "ide_geedp");
			for (int i = 0; i < tab_rub_faltantes.getTotalFilas(); i++) {
				String str_insert="INSERT INTO NRH_DETALLE_ROL (IDE_NRDRO, IDE_NRROL, IDE_GEEDP, IDE_NRDER, VALOR_NRDRO, USUARIO_INGRE, FECHA_INGRE, HORA_INGRE, USUARIO_ACTUA, FECHA_ACTUA, HORA_ACTUA,ORDEN_CALCULO_NRDRO) " +
						"VALUES ("+ide_inicial+", " +
						""+str_ide_nrrol+", " +
						""+ide_geedp+", " +
						""+tab_rub_faltantes.getValor(i,"IDE_NRDER")+", " +
						"0.00, NULL, NULL, NULL, NULL, NULL, NULL,NULL)";
				utilitario.getConexion().ejecutarSql(str_insert);
				ide_inicial++;
			}
		}



		indice_detalle=0;
		indice_ultimo_detalle=0;
		int nro_dias_comercial_nrtit=0;
		try {
			nro_dias_comercial_nrtit=pckUtilidades.CConversion.CInt(getTipoRol(ide_nrtit).getValor("NRO_DIAS_COMERCIAL_NRTIT"));
		} catch (Exception e) {
			// TODO: handle exception
		}




		// limpio la tabla nrh_detalle del rol para recalcular
		tab_deta_rubros_rol=getDetalleRubrosRolVacia();

		str_ide_nramo_descontar="";
		for (int j = 0; j < tab_empleados_departamento.getTotalFilas(); j++) {

			//			if (j==15){
			//				break;
			//			}
			ide_geedp=tab_empleados_departamento.getValor(j, "ide_geedp");
			ide_gtemp=tab_empleados_departamento.getValor(j, "ide_gtemp");

			fecha_ingreso=tab_empleados_departamento.getValor(j, "FECHA_INGRESO_GTEMP");
			fecha_ingreso_emgirs=tab_empleados_departamento.getValor(j, "fecha_ingreso_grupo_gtemp");
			fecha_contrato=tab_empleados_departamento.getValor(j, "FECHA_GEEDP");
			RMU=tab_empleados_departamento.getValor(j, "RMU_GEEDP");
			IDE_GEREG=tab_empleados_departamento.getValor(j, "IDE_GEREG");
			acumula_fondos=tab_empleados_departamento.getValor(j, "ACUMULA_FONDOS_GEEDP");
			acumula_decimos=tab_empleados_departamento.getValor(j, "ACUMULA_DECIMO_GTEMP");
			fecha_subroga=tab_empleados_departamento.getValor(j,"FECHA_ENCARGO_GEEDP");
			RMU_CARGO_SUBROGA=tab_empleados_departamento.getValor(j,"SUELDO_SUBROGA_GEEDP");
			ajuste_sueldo=tab_empleados_departamento.getValor(j,"AJUSTE_SUELDO_GEEDP");
			fecha_ajuste=tab_empleados_departamento.getValor(j,"FECHA_AJUSTE_GEEDP");
			fecha_fin_subrogacion=tab_empleados_departamento.getValor(j,"FECHA_ENCARGO_FIN_GEEDP");
			//dias_pendientes_vacacion=tab_empleados_departamento.getValor(j,"DIAS_VACACION");
			ide_gttem=tab_empleados_departamento.getValor(j,"ide_gttem");
			continuidad_liquidacion_geedp=tab_empleados_departamento.getValor(j,"continuidad_liquidacion_geedp");
			if (continuidad_liquidacion_geedp==null || continuidad_liquidacion_geedp.isEmpty()) {
				continuidad_liquidacion_geedp="false";
			}
			System.out.println("valor tomado de la tabla acumula decimo "+acumula_decimos);
			System.out.println("valor tomado de la tabla acumula fodnos "+acumula_fondos);

			// calculamos el rol del empleado 
			long ini=System.currentTimeMillis();
			//			System.out.println("ide geedp "+ide_geedp+" indice ultimo detalle "+indice_ultimo_detalle+" det rol "+tab_drol.getSql());
			calcularRolIndividualLiquidacion(tab_drol, ide_gtemp,ide_geedp, str_ide_nrrol, fecha_ingreso, fecha_contrato, IDE_GEREG,RMU, acumula_fondos, ide_gepro, fecha_ini_gepro, fecha_fin_gepro,fecha_subroga,fecha_fin_subrogacion,RMU_CARGO_SUBROGA,ajuste_sueldo,fecha_ajuste,dias_pendientes_vacacion,nro_dias_comercial_nrtit,IDE_NRTIN,acumula_decimos,base_imponible_mes_anterior,fondo_reserva_acum_pago,fondo_reserva_nom_pago,fecha_ingreso_emgirs,ide_gttem,continuidad_liquidacion_geedp);
			long fin=System.currentTimeMillis();
		}

		utilitario.getConexion().ejecutarSql("DELETE FROM NRH_DETALLE_ROL where ide_nrdro in ( " +
				"select IDE_NRDRO " +
				"from NRH_DETALLE_ROL DRO " +
				"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
				"where DRO.IDE_NRROL="+str_ide_nrrol+" and DER.ACTIVO_NRDER!=true )");


		//if (!IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos")) 
		if (!utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos").contains(IDE_NRTIN)
				&& !IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_RENTA_NRROL=0 where IDE_NRROL="+str_ide_nrrol);
		}else{
			utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_RENTA_NRROL=1 where IDE_NRROL="+str_ide_nrrol);		
		}

		utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_CALCULADO_NRROL=1 where IDE_NRROL="+str_ide_nrrol);

		try {
			str_ide_nramo_descontar=str_ide_nramo_descontar.substring(0, str_ide_nramo_descontar.length()-1);
		} catch (Exception e) {
			// TODO: handle exception
		}

		System.out.println("nramo a descontar: "+str_ide_nramo_descontar);
		String strSql="update NRH_AMORTIZACION set IDE_NRROL="+str_ide_nrrol+" where IDE_NRAMO in ("+str_ide_nramo_descontar+") ";
		if (!str_ide_nramo_descontar.isEmpty()){
			System.out.println("reCalcularNomina boolean strSql: "+strSql);
			utilitario.getConexion().ejecutarSql(strSql);
		}


		long fin = System.currentTimeMillis();
		System.out.println("======== Tiempo total RECALCULAR (TODO EL PROCESO) guardar pantalla : " + (fin - inicio));
	}

	long ide_inicial=0;
	public void reCalcularNomina(String ide_nrdtn,String ide_gepro,String ide_nrtit,String str_ide_geedp){

		long inicio = System.currentTimeMillis();
		System.out.println("RECALCULAR EMPLEADO "+ide_nrdtn);
		TablaGenerica tab_rol=getRol(ide_nrdtn,ide_gepro);
		String str_ide_nrrol=tab_rol.getValor("IDE_NRROL");
		TablaGenerica tab_per_rol=getPeriodoRol(tab_rol.getValor("IDE_GEPRO"));

		String fecha_ini_gepro="";
		String fecha_fin_gepro="";
		String sql_NRH_AMORTIZACION="";

		
	
		

		// obtengo el Tipo de nomina de la tabla detalle de tipo de nomina (Normal,Liquidacion,Pago de Decimos)
		String IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			if (getDetalleTipoNomina(ide_nrdtn).getValor("IDE_GTTEM").equalsIgnoreCase(utilitario.getVariable("p_gth_tipo_empleado_codigo"))){
				fecha_fin_gepro=getPeriodoRol(ide_gepro).getValor("FECHA_INICIAL_GEPRO");
				fecha_ini_gepro=utilitario.getAnio(fecha_fin_gepro)+"-"+utilitario.getMes(fecha_fin_gepro)+"-1";
				//fecha_ini_gepro=utilitario.getAnio(fecha_fin_gepro)+"-"+utilitario.getMes(fecha_fin_gepro)+"-1";
			}else{
				String fecha_ini=getPeriodoRol(ide_gepro).getValor("FECHA_INICIAL_GEPRO");
				fecha_fin_gepro=utilitario.getAnio(fecha_ini)+"-"+utilitario.getMes(fecha_ini)+"-30";
				fecha_ini_gepro=utilitario.getAnio(fecha_ini)+"-"+utilitario.getMes(fecha_ini)+"-1";
			}
		}else{
			fecha_ini_gepro=tab_per_rol.getValor("FECHA_INICIAL_GEPRO");
			fecha_fin_gepro=tab_per_rol.getValor("FECHA_FINAL_GEPRO");
		}
		
		
		
		
		
		
		
	
		
		
	/*	sql_NRH_AMORTIZACION="update  NRH_AMORTIZACION set ACTIVO_NRAMO=false " +
				"where FECHA_VENCIMIENTO_NRAMO " +
				"BETWEEN to_date ('"+fecha_ini_gepro+"','yyyy-mm-dd') and to_date ('"+fecha_fin_gepro+"','yyyy-mm-dd') " +
				"and IDE_NRANI in (select ide_nrani from NRH_ANTICIPO_INTERES " +
				"where IDE_NRANT in (select ide_nrant from NRH_ANTICIPO " +
				"where IDE_GTEMP in (select emp.ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM " +
				//				"and EDP.IDE_SUCU=DTN.IDE_SUCU " +
				"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP " +
				"WHERE DTN.IDE_NRDTN IN ("+ide_nrdtn+")) " +
				"AND ACTIVO_NRANT =true ))";

		utilitario.getConexion().ejecutarSql(sql_NRH_AMORTIZACION);

		System.out.println("sql_NRH_AMORTIZACION: "+sql_NRH_AMORTIZACION);
*/
		
		
		TablaGenerica tab_empleados_departamento=utilitario.consultar(getSqlEmpleadosRolLiquidacionFecha(ide_nrdtn));
		String sql="select * from ( ";
		sql+=tab_empleados_departamento.getSql();
		sql+=" )a where a.ide_geedp in ("+str_ide_geedp+") ";

		
		
		
		tab_empleados_departamento=utilitario.consultar(sql);

		System.out.println("emp "+tab_empleados_departamento.getSql());

		P_NRH_RUBRO_REGION=utilitario.getVariable("p_nrh_rubro_region");
		P_NRH_RUBRO_DESC_VALORES_LIQUIDAR=utilitario.getVariable("p_nrh_rubro_desc_valores_liquidar");
		P_NRH_RUBRO_REMUNERACION_UNIFICADA=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada");
		P_NRH_RUBRO_RMU_HONORARIOS=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada_honorarios");
		P_NRH_RUBRO_ACUMULA_FONDOS=utilitario.getVariable("p_nrh_rubro_acumula_fondos_reserva");
		P_NRH_RUBRO_DIAS_TRABAJADOS=utilitario.getVariable("p_nrh_rubro_dias_trabajados");
		P_NRH_RUBRO_DIAS_ANTIGUEDAD=utilitario.getVariable("p_nrh_rubro_dias_antiguedad");
		P_NRH_RUBRO_DIAS_FONDOS_RESERVA=utilitario.getVariable("p_nrh_rubro_dias_fondos_reserva");
		P_NRH_RUBRO_DIAS_PERIODO=utilitario.getVariable("p_nrh_rubro_dias_periodo_nomina");
		P_NRH_RUBRO_RMU_CARGO_SUBROGANTE=utilitario.getVariable("p_nrh_rubro_rmu_cargo_subrogante");
		P_NRH_RUBRO_DIAS_SUBROGADOS=utilitario.getVariable("p_nrh_rubro_dias_subrogados");
		P_NRH_RUBRO_DIAS_AJUSTE_SUELDO=utilitario.getVariable("p_nrh_rubro_dias_ajuste_sueldo");
		P_NRH_RUBRO_AJUSTE_SUELDO=utilitario.getVariable("p_nrh_rubro_ajuste_sueldo");		
		P_NRH_RUBRO_DIAS_PEND_VACACION=utilitario.getVariable("p_nrh_rubro_dias_pendientes_vacacion");
		P_NRH_ACUMULA_DECIMOS=utilitario.getVariable("p_nrh_rubro_acumula_decimos");		
		P_NRH_BASE_IMPONIBLE_MES_ANTERIOR=utilitario.getVariable("p_nrh_rubro_base_imponible_mes_anterior");
		P_NRH_RUBRO_FONRESERV_ACUM_PAGO=utilitario.getVariable("p_nrh_rubro_fondreser_acum_pago");
		P_NRH_RUBRO_FONRESERV_NOM_PAGO=utilitario.getVariable("p_nrh_rubro_fondreser_nomi_pago");	
		

		tab_deta_rubros_rol=getDetalleRubrosRolVacia();

		String ide_geedp="";
		String ide_gtemp="";
		String fecha_ingreso="";
		String fecha_contrato="";
		String RMU="";
		String IDE_GEREG="";
		String acumula_fondos="";
		String acumula_decimos="";
		String fecha_subroga="";
		String RMU_CARGO_SUBROGA="";
		String ajuste_sueldo="";
		String fecha_ajuste="";
		String fecha_fin_subrogacion="";
		String dias_pendientes_vacacion="";
		String base_imponible_mes_anterior="";
		String fondo_reserva_acum_pago="";
		String fondo_reserva_nom_pago="";
		String fecha_ingreso_emgirs="";
		String ide_gttem="";
		String continuidad_liquidacion_geedp="";
		TablaGenerica tab_rub_faltantes=getRubrosFaltantes(str_ide_nrrol,str_ide_geedp);
		if (tab_rub_faltantes.getTotalFilas()>0){
			utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'NRH_DETALLE_ROL'");
			ide_inicial=utilitario.getConexion().getMaximo("NRH_DETALLE_ROL", "IDE_NRDRO", 1);
		}


		for (int j = 0; j < tab_empleados_departamento.getTotalFilas(); j++) {

			ide_geedp=tab_empleados_departamento.getValor(j, "ide_geedp");
			for (int i = 0; i < tab_rub_faltantes.getTotalFilas(); i++) {
				String str_insert="INSERT INTO NRH_DETALLE_ROL (IDE_NRDRO, IDE_NRROL, IDE_GEEDP, IDE_NRDER, VALOR_NRDRO, USUARIO_INGRE, FECHA_INGRE, HORA_INGRE, USUARIO_ACTUA, FECHA_ACTUA, HORA_ACTUA,ORDEN_CALCULO_NRDRO) " +
						"VALUES ("+ide_inicial+", " +
						""+str_ide_nrrol+", " +
						""+ide_geedp+", " +
						""+tab_rub_faltantes.getValor(i,"IDE_NRDER")+", " +
						"0.00, NULL, NULL, NULL, NULL, NULL, NULL,NULL)";
				utilitario.getConexion().ejecutarSql(str_insert);
				ide_inicial++;
			}
		}


		TablaGenerica tab_drol=getDetalleRol(str_ide_nrrol,str_ide_geedp);// coje los detalles de rol

		System.out.println("tab drol "+tab_drol.getSql());

		indice_detalle=0;
		indice_ultimo_detalle=0;

		int nro_dias_comercial_nrtit=0;
		try {
			nro_dias_comercial_nrtit=pckUtilidades.CConversion.CInt(getTipoRol(ide_nrtit).getValor("NRO_DIAS_COMERCIAL_NRTIT"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		str_ide_nramo_descontar="";

		for (int j = 0; j < tab_empleados_departamento.getTotalFilas(); j++) {
			ide_geedp=tab_empleados_departamento.getValor(j, "ide_geedp");
			ide_gtemp=tab_empleados_departamento.getValor(j, "ide_gtemp");
			fecha_ingreso=tab_empleados_departamento.getValor(j, "FECHA_INGRESO_GTEMP");
			fecha_ingreso_emgirs=tab_empleados_departamento.getValor(j, "fecha_ingreso_grupo_gtemp");
			fecha_contrato=tab_empleados_departamento.getValor(j, "FECHA_GEEDP");
			RMU=tab_empleados_departamento.getValor(j, "RMU_GEEDP");
			IDE_GEREG=tab_empleados_departamento.getValor(j, "IDE_GEREG");
			acumula_fondos=tab_empleados_departamento.getValor(j, "ACUMULA_FONDOS_GEEDP");
			acumula_decimos=tab_empleados_departamento.getValor(j, "ACUMULA_DECIMO_GTEMP");			
			fecha_subroga=tab_empleados_departamento.getValor(j,"FECHA_ENCARGO_GEEDP");
			RMU_CARGO_SUBROGA=tab_empleados_departamento.getValor(j,"SUELDO_SUBROGA_GEEDP");
			ajuste_sueldo=tab_empleados_departamento.getValor(j,"AJUSTE_SUELDO_GEEDP");
			fecha_ajuste=tab_empleados_departamento.getValor(j,"FECHA_AJUSTE_GEEDP");
			fecha_fin_subrogacion=tab_empleados_departamento.getValor(j,"FECHA_ENCARGO_FIN_GEEDP");
			//dias_pendientes_vacacion=tab_empleados_departamento.getValor(j,"DIAS_VACACION");
			ide_gttem=tab_empleados_departamento.getValor(j,"IDE_GTTEM");
			continuidad_liquidacion_geedp=tab_empleados_departamento.getValor(j,"continuidad_liquidacion_geedp");
			if (continuidad_liquidacion_geedp==null || continuidad_liquidacion_geedp.isEmpty()) {
				continuidad_liquidacion_geedp="false";
			}
			System.out.println("valor tomado de la tabla acumula decimoxx "+acumula_decimos);
			System.out.println("valor tomado de la tabla acumula fodnosxx "+acumula_fondos);

			// calculamos el rol del empleado 
			long ini=System.currentTimeMillis();
			
	//		calcularRolIndividualLiquidacion(tab_rubros, ide_gtemp, ide_geedp, ide_nrrol, fecha_ingreso, fecha_contrato, IDE_GEREG, RMU, acumula_fondos, ide_gepro, fecha_inicial_gepro, fecha_final_gepro, fecha_subroga, fecha_fin_subroga, RMU_CARGO_SUBROGA, ajuste_sueldo, fecha_ajuste_sueldo, dias_pendientes_vacacion, ide_inicial, nro_dias_comercial, ide_nrtin, acumula_decimos, base_imponible_mes_anterior, fondo_reserva_acum_pago, fondo_reserva_nom_pago, fecha_ingreso_emgirs, ide_gttem);
			calcularRolIndividualLiquidacion(tab_drol, ide_gtemp,ide_geedp, str_ide_nrrol, fecha_ingreso, fecha_contrato, IDE_GEREG,RMU, acumula_fondos, ide_gepro, fecha_ini_gepro, fecha_fin_gepro,fecha_subroga,fecha_fin_subrogacion,RMU_CARGO_SUBROGA,ajuste_sueldo,fecha_ajuste,dias_pendientes_vacacion,nro_dias_comercial_nrtit,IDE_NRTIN,acumula_decimos,base_imponible_mes_anterior,fondo_reserva_acum_pago,fondo_reserva_nom_pago,fecha_ingreso_emgirs,ide_gttem,continuidad_liquidacion_geedp);
			long fin=System.currentTimeMillis();

		}

		utilitario.getConexion().ejecutarSql("DELETE FROM NRH_DETALLE_ROL where ide_nrdro in ( " +
				"select IDE_NRDRO " +
				"from NRH_DETALLE_ROL DRO " +
				"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
				"where DRO.IDE_NRROL="+str_ide_nrrol+" and DER.ACTIVO_NRDER!=true )");

		//if (!IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos")) 
		  if (!utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos").contains(IDE_NRTIN)
				&& !IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_RENTA_NRROL=0 where IDE_NRROL="+str_ide_nrrol);
		}else{
			utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_RENTA_NRROL=1 where IDE_NRROL="+str_ide_nrrol);		
		}
		utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_CALCULADO_NRROL=1 where IDE_NRROL="+str_ide_nrrol);

		try {
			str_ide_nramo_descontar=str_ide_nramo_descontar.substring(0, str_ide_nramo_descontar.length()-1);
		} catch (Exception e) {
			// TODO: handle exception
		}

		System.out.println("nramo a descontar  "+str_ide_nramo_descontar);
		String strSql="update NRH_AMORTIZACION set IDE_NRROL="+str_ide_nrrol+" where IDE_NRAMO in ("+str_ide_nramo_descontar+") ";
		
		if (!str_ide_nramo_descontar.isEmpty()){
			System.out.println("reCalcularNomina strSql: "+strSql);
	        utilitario.getConexion().ejecutarSql(strSql);
		}

		long fin = System.currentTimeMillis();
		System.out.println("======== Tiempo total RECALCULAR (TODO EL PROCESO) guardar pantalla : " + (fin - inicio));
		
		utilitario.agregarNotificacionInfo("Importante.!!", "No se olvide de 're-calcular los rubros' en la pantalla Generar Rol y posterior 'Calcular Renta'...");
	}


	int indice_detalle=0;

	public void calcularRolIndividual(TablaGenerica tab_det_rol,String ide_gtemp,String ide_geedp,String ide_nrrol,String fecha_ingreso,String fecha_contrato,String IDE_GEREG,String RMU,
			String acumula_fondos,String ide_gepro,String fecha_inicial_gepro,String fecha_final_gepro,String fecha_subroga,String fecha_fin_subroga,String RMU_CARGO_SUBROGA,String ajuste_sueldo,
			String fecha_ajuste_sueldo,String dias_pend_vacacion,int nro_comercial_nrtit,String ide_nrtin,String acumula_decimo,String base_imponible_mes_anterior,String fondo_reserva_acum_pago, 
			String fondo_reserva_nom_pago,String fecha_ingreso_emgirs,String ide_gttem){

		boolean boo_tiene_anticipos=false;
		tab_cuotas=serv_anticipo.getCuotasAnticipoPorPagarEmpleado(ide_gtemp, ide_gepro,"");
		if (tab_cuotas.getTotalFilas()>0){
			boo_tiene_anticipos=true;
		}

		boolean boo_tiene_beneficio_guarderia=false;
		tab_det_fac_gua=getDetalleFacturaGuarderiaEmpleado(ide_gtemp);
		if (tab_det_fac_gua.getTotalFilas()>0){
			boo_tiene_beneficio_guarderia=true;
		}

		if (ide_nrtin.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			int num_dias_exceso_liquidacion=0;
			num_dias_exceso_liquidacion=getValorDiasExceso(Integer.parseInt(ide_geedp));
			// insertarmos todos los rubros del empleado e importamos los valores unicamente de los rubros de forma de calculo (IMPORTADO)
			actualizarDetallesRolEmpleado(ide_geedp, ide_nrrol, tab_det_rol, boo_tiene_beneficio_guarderia, boo_tiene_anticipos, IDE_GEREG,RMU, acumula_fondos, fecha_ingreso, fecha_contrato, 
					ide_gepro, fecha_inicial_gepro, fecha_final_gepro,fecha_subroga,fecha_fin_subroga,RMU_CARGO_SUBROGA,ajuste_sueldo,fecha_ajuste_sueldo,dias_pend_vacacion,nro_comercial_nrtit,
					true,acumula_decimo,base_imponible_mes_anterior,fondo_reserva_acum_pago,fondo_reserva_nom_pago,fecha_ingreso_emgirs,ide_gttem,num_dias_exceso_liquidacion,"");
		}else{
			// insertarmos todos los rubros del empleado e importamos los valores unicamente de los rubros de forma de calculo (IMPORTADO)
			int num_dias_exceso_liquidacion=0;
			num_dias_exceso_liquidacion=getValorDiasExceso(Integer.parseInt(ide_geedp));
		
			actualizarDetallesRolEmpleado(ide_geedp, ide_nrrol, tab_det_rol, boo_tiene_beneficio_guarderia, boo_tiene_anticipos, IDE_GEREG,RMU, acumula_fondos, fecha_ingreso, fecha_contrato, 
					ide_gepro, fecha_inicial_gepro, fecha_final_gepro,fecha_subroga,fecha_fin_subroga,RMU_CARGO_SUBROGA,ajuste_sueldo,fecha_ajuste_sueldo,dias_pend_vacacion,nro_comercial_nrtit,
					false,acumula_decimo,base_imponible_mes_anterior,fondo_reserva_acum_pago,fondo_reserva_nom_pago,fecha_ingreso_emgirs,ide_gttem,num_dias_exceso_liquidacion,"");
		}


		int indice=total_filas_insertadas;

		despejarFormulasRol(indice, ide_geedp, fecha_final_gepro);


		String str_valor="";
		for (int l = 0; l < total_filas_insertadas; l++) {

			if (tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO")!=null && !tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO").isEmpty()){

				try {
					BigDecimal big_valor=new BigDecimal(pckUtilidades.CConversion.CDbl_2(tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO")));
					big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
					str_valor=big_valor+"";
				} catch (Exception e) {
					str_valor="0.00";
				}
			}else{
				str_valor="0.00";
			}

			String orden=tab_deta_rubros_rol.getValor(l,"ORDEN_CALCULO_NRDRO");
			if (orden.isEmpty()){
				orden=null;
			}

			String str_update="update NRH_DETALLE_ROL set IDE_NRDER="+tab_deta_rubros_rol.getValor(l,"IDE_NRDER")+"," +
					"VALOR_NRDRO="+str_valor+", " +
					"IDE_NRROL="+tab_deta_rubros_rol.getValor(l,"IDE_NRROL")+", " +
					"IDE_GEEDP="+tab_deta_rubros_rol.getValor(l,"IDE_GEEDP")+", " +
					"ORDEN_CALCULO_NRDRO="+orden+" " +
					"where IDE_NRDRO="+tab_det_rol.getValor(indice_detalle,"IDE_NRDRO");
			utilitario.getConexion().ejecutarSql(str_update);



			indice_detalle++;
		}

		TablaGenerica tab_rub_repetido=utilitario.consultar("select * from ( " +
				"SELECT ide_nrder,COUNT(ide_nrder) as num_rub FROM ( " +
				"select IDE_NRDRO,IDE_NRROL,IDE_GEEDP,DRO.IDE_NRDER,VALOR_NRDRO,DER.IDE_NRRUB,RUB.IDE_NRFOC,ORDEN_NRDER,DER.FORMULA_NRDER,DER.FECHA_INICIAL_NRDER,DER.FECHA_FINAL_NRDER,DER.FECHA_PAGO_NRDER,decimo_nrrub from NRH_DETALLE_ROL DRO INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
				"where DER.ACTIVO_NRDER=true and DRO.IDE_NRROL="+ide_nrrol+" and DRO.IDE_GEEDP="+ide_geedp+" " +
				"order by der.ide_nrder " +
				")a GROUP BY ide_nrder " +
				")a where num_rub >1"); 

		for (int i = 0; i < tab_rub_repetido.getTotalFilas(); i++) {
			TablaGenerica tab_det_rol1=utilitario.consultar("select * from NRH_DETALLE_ROL where IDE_NRDER="+tab_rub_repetido.getValor(i, "IDE_NRDER")+" and IDE_GEEDP="+ide_geedp+" and ide_nrrol="+ide_nrrol);
			if (tab_det_rol1.getTotalFilas()>0){
				utilitario.getConexion().ejecutarSql("delete from nrh_detalle_rol where ide_nrdro="+tab_det_rol1.getValor(0,"ide_nrdro"));
			}
		}

	}

	


	public void calcularRolIndividualLiquidacion(TablaGenerica tab_det_rol,String ide_gtemp,String ide_geedp,String ide_nrrol,String fecha_ingreso,String fecha_contrato,String IDE_GEREG,String RMU,
			String acumula_fondos,String ide_gepro,String fecha_inicial_gepro,String fecha_final_gepro,String fecha_subroga,String fecha_fin_subroga,String RMU_CARGO_SUBROGA,String ajuste_sueldo,
			String fecha_ajuste_sueldo,String dias_pend_vacacion,int nro_comercial_nrtit,String ide_nrtin,String acumula_decimo,String base_imponible_mes_anterior,String fondo_reserva_acum_pago, 
			String fondo_reserva_nom_pago,String fecha_ingreso_emgirs,String ide_gttem, String continuidad_liquidacion_geedp){
		indice_detalle=0;
		boolean boo_tiene_anticipos=false;
		tab_cuotas=serv_anticipo.getCuotasAnticipoPorPagarEmpleadoLiquidacion(ide_gtemp, ide_gepro,("2017-12-31"));
	   
    	int valor=0;
      	int valorEmp=0;
      	int valorJefe=0;
       boolean bandSinArea=false;
       int bandArea=0;
   	StringBuilder str_ide_empleado=new StringBuilder();
   String ide_nramo_empleado="";
		if (tab_cuotas.getTotalFilas()>0) {
			boo_tiene_anticipos=true;
			
			//System.out.println("nramo a descontar liquidacion "+str_ide_nramo_descontar);
			int valorAnticipo=0;
			StringBuilder str_ide_empleado_anticipo= new StringBuilder();
			//Recorro la tabla con todas las acciones de personal efectuadas al empleado
		         		for (int i = 0; i < tab_cuotas.getTotalFilas(); i++) 
		         		{
		         		//Voy anidando los ides de la accion
		          	    str_ide_empleado_anticipo.append(tab_cuotas.getValor(i, "IDE_NRAMO"));
		               // valor++;
		                if (tab_cuotas.getTotalFilas()==1) {
		   			}else if (valorAnticipo<=tab_cuotas.getTotalFilas()) {
		   				valorAnticipo++;
		   					if(valorAnticipo<(tab_cuotas.getTotalFilas())){
		                    str_ide_empleado_anticipo.append(",");
		                   // System.out.println("str_ide:  "+str_ide_empleado);
		   					}
		    			}
		                
		         		}
		    //System.out.println("DETALLE TABLA AMORTIZACION"+str_ide_empleado_anticipo.toString());
			String strSql="update NRH_AMORTIZACION set IDE_NRROL="+ide_nrrol+" where IDE_NRAMO in ("+str_ide_empleado_anticipo.toString()+") ";
			if (str_ide_empleado_anticipo.length()>0){
			//	System.out.println("generarRolLiquidacion strSql: "+strSql);
				ide_nramo_empleado=str_ide_empleado_anticipo.toString();
				utilitario.getConexion().ejecutarSql(strSql);
			}
	  		 }
		
		
		
		//tab_cuotas=serv_anticipo.getCuotasAnticipoPorPagarEmpleado(ide_gtemp, ide_gepro,("2017-12-31"));
		if (tab_cuotas.getTotalFilas()>0){
			boo_tiene_anticipos=true;
		}

		boolean boo_tiene_beneficio_guarderia=false;
		tab_det_fac_gua=getDetalleFacturaGuarderiaEmpleado(ide_gtemp);
		if (tab_det_fac_gua.getTotalFilas()>0){
			boo_tiene_beneficio_guarderia=true;
		}

		if (ide_nrtin.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			int num_dias_exceso_liquidacion=0;
			num_dias_exceso_liquidacion=getValorDiasExceso(Integer.parseInt(ide_geedp));
		
			// insertarmos todos los rubros del empleado e importamos los valores unicamente de los rubros de forma de calculo (IMPORTADO)
			actualizarDetallesRolEmpleado(ide_geedp, ide_nrrol, tab_det_rol, boo_tiene_beneficio_guarderia, boo_tiene_anticipos, IDE_GEREG,RMU, acumula_fondos, fecha_ingreso, fecha_contrato, 
					ide_gepro, fecha_inicial_gepro, fecha_final_gepro,fecha_subroga,fecha_fin_subroga,RMU_CARGO_SUBROGA,ajuste_sueldo,fecha_ajuste_sueldo,dias_pend_vacacion,nro_comercial_nrtit,
					true,acumula_decimo,base_imponible_mes_anterior,fondo_reserva_acum_pago,fondo_reserva_nom_pago,fecha_ingreso_emgirs,ide_gttem,num_dias_exceso_liquidacion,ide_nramo_empleado);
		}else{
			// insertarmos todos los rubros del empleado e importamos los valores unicamente de los rubros de forma de calculo (IMPORTADO)
			int num_dias_exceso_liquidacion=0;
			num_dias_exceso_liquidacion=getValorDiasExceso(Integer.parseInt(ide_geedp));
		
			actualizarDetallesRolEmpleado(ide_geedp, ide_nrrol, tab_det_rol, boo_tiene_beneficio_guarderia, boo_tiene_anticipos, IDE_GEREG,RMU, acumula_fondos, fecha_ingreso, fecha_contrato, 
					ide_gepro, fecha_inicial_gepro, fecha_final_gepro,fecha_subroga,fecha_fin_subroga,RMU_CARGO_SUBROGA,ajuste_sueldo,fecha_ajuste_sueldo,dias_pend_vacacion,nro_comercial_nrtit,
					false,acumula_decimo,base_imponible_mes_anterior,fondo_reserva_acum_pago,fondo_reserva_nom_pago,fecha_ingreso_emgirs,ide_gttem,num_dias_exceso_liquidacion,ide_nramo_empleado);
		}

 
		int indice=total_filas_insertadas;

		despejarFormulasRol(indice, ide_geedp, fecha_final_gepro);


		String str_valor="";
		for (int l = 0; l < total_filas_insertadas; l++) {

			if (tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO")!=null && !tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO").isEmpty()){

				try {
					BigDecimal big_valor=new BigDecimal(pckUtilidades.CConversion.CDbl_2(tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO")));
					big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
					str_valor=big_valor+"";
				} catch (Exception e) {
					str_valor="0.00";
				}
			}else{
				str_valor="0.00";
			}

			String orden=tab_deta_rubros_rol.getValor(l,"ORDEN_CALCULO_NRDRO");
			if (orden.isEmpty()){
				orden=null;
			}

			String str_update="update NRH_DETALLE_ROL set IDE_NRDER="+tab_deta_rubros_rol.getValor(l,"IDE_NRDER")+"," +
					"VALOR_NRDRO="+str_valor+", " +
					"IDE_NRROL="+tab_deta_rubros_rol.getValor(l,"IDE_NRROL")+", " +
					"IDE_GEEDP="+tab_deta_rubros_rol.getValor(l,"IDE_GEEDP")+", " +
					"ORDEN_CALCULO_NRDRO="+orden+" " +
					"where IDE_NRDRO="+tab_det_rol.getValor(indice_detalle,"IDE_NRDRO");
			utilitario.getConexion().ejecutarSql(str_update);



			indice_detalle++;
		}

		TablaGenerica tab_rub_repetido=utilitario.consultar("select * from ( " +
				"SELECT ide_nrder,COUNT(ide_nrder) as num_rub FROM ( " +
				"select IDE_NRDRO,IDE_NRROL,IDE_GEEDP,DRO.IDE_NRDER,VALOR_NRDRO,DER.IDE_NRRUB,RUB.IDE_NRFOC,ORDEN_NRDER,DER.FORMULA_NRDER,DER.FECHA_INICIAL_NRDER,DER.FECHA_FINAL_NRDER,DER.FECHA_PAGO_NRDER,decimo_nrrub from NRH_DETALLE_ROL DRO INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
				"where DER.ACTIVO_NRDER=true and DRO.IDE_NRROL="+ide_nrrol+" and DRO.IDE_GEEDP="+ide_geedp+" " +
				"order by der.ide_nrder " +
				")a GROUP BY ide_nrder " +
				")a where num_rub >1"); 

		for (int i = 0; i < tab_rub_repetido.getTotalFilas(); i++) {
			TablaGenerica tab_det_rol1=utilitario.consultar("select * from NRH_DETALLE_ROL where IDE_NRDER="+tab_rub_repetido.getValor(i, "IDE_NRDER")+" and IDE_GEEDP="+ide_geedp+" and ide_nrrol="+ide_nrrol);
			if (tab_det_rol1.getTotalFilas()>0){
				utilitario.getConexion().ejecutarSql("delete from nrh_detalle_rol where ide_nrdro="+tab_det_rol1.getValor(0,"ide_nrdro"));
			}
		}

	}
	
	
	
	String str_ide_nramo_descontar="";
	public String importarRubro(String ide_geedp,String ide_nrrub,boolean boo_tiene_beneficio_guarderia,boolean boo_tiene_anticipos,String IDE_GEREG,String RMU,
			String acumula_fondos,String fecha_ingreso,String fecha_contrato,String ide_gepro,String fecha_inicial_gepro,String fecha_final_gepro,String dias_antiguedad,
			String fecha_subrogacion,String fecha_fin_subrogacion,String RMU_CARGO_SUBROGA,String ajuste_sueldo,String fecha_ajuste_sueldo,String dias_pendientes_vacacion,
			int NRO_DIAS_COMERCIAL_NRTIT,boolean es_liquidacion,String acumula_decimo,String base_imponible_mes_anterior,String fondo_reserva_acum_pago,String fondo_reserva_nom_pago,
			String fecha_ingreso_emgirs,String ide_gttem, int dias_exceso_liquidacion,String ide_nramo_empleado,String continuidad_liquidacion_geedp){

		// importacion beneficios de guarderia
		if (boo_tiene_beneficio_guarderia){
			String valor=getValorRubroBeneficioGuarderia(tab_det_fac_gua, fecha_final_gepro,ide_nrrub, ide_geedp);
			if (valor !=null){
				return valor;
			}
		}


		if (!es_liquidacion){
			// importacion anticipos
			if (boo_tiene_anticipos){
				
	
		for (int k = 0; k < tab_cuotas.getTotalFilas(); k++) {
					if (tab_cuotas.getValor(k, "IDE_NRRUB").equalsIgnoreCase(ide_nrrub)){
						//					utilitario.getConexion().agregarSqlPantalla("update NRH_AMORTIZACION set ACTIVO_NRAMO=true where IDE_NRAMO="+tab_cuotas.getValor(k,"IDE_NRAMO"));
						str_ide_nramo_descontar+=tab_cuotas.getValor(k,"IDE_NRAMO")+",";
						return tab_cuotas.getValor(k,"CUOTA_NRAMO");
					}
				}
			}
		}else {
			if (boo_tiene_anticipos){
				int valor=0;
				StringBuilder str_ide_empleado = new StringBuilder();
				double cuota_nramo=0;
				for (int k = 0; k < tab_cuotas.getTotalFilas(); k++) {
					if (tab_cuotas.getValor(k, "IDE_NRRUB").equalsIgnoreCase(ide_nrrub)){
						//					utilitario.getConexion().agregarSqlPantalla("update NRH_AMORTIZACION set ACTIVO_NRAMO=true where IDE_NRAMO="+tab_cuotas.getValor(k,"IDE_NRAMO"));
						valor=0;
						//Recorro la tabla con todas las acciones de personal efectuadas al empleado
						TablaGenerica tab_valorTotal=utilitario.consultar("SELECT sum(cuota_nramo) as total,IDE_NRRUB  "
								+ "FROM NRH_AMORTIZACION where ide_nramo in ("+ide_nramo_empleado+") GROUP BY IDE_NRRUB");
						
						return tab_valorTotal.getValor("total");
					         	/*	for (int i = 0; i < tab_cuotas.getTotalFilas(); i++) 
					         		{
					         		//Voy anidando los ides de la accion
					          	    cuota_nramo=Double.parseDouble((tab_cuotas.getValor(k, "CUOTA_NRAMO")));
					          	    k=tab_cuotas.getTotalFilas(); 
					         		}*/
					    						
						//return ""+cuota_nramo;
					}
				}
			}
		}


		// importa REGION a la que pertenece el empleado				
		if(ide_nrrub.equals(P_NRH_RUBRO_REGION)){

			if (IDE_GEREG!=null && !IDE_GEREG.isEmpty()){
				return IDE_GEREG;
			}else{
				return "-1";
			}
		}
		// importar el total de valores que adeuda el empleado
		else if (ide_nrrub.equalsIgnoreCase(P_NRH_RUBRO_DESC_VALORES_LIQUIDAR)){
			TablaGenerica tab_ant=utilitario.consultar("select ANT.IDE_NRANT,sum (cuota_nramo) as tot_desc_val_liq from NRH_ANTICIPO ant " +
					"inner join NRH_ANTICIPO_INTERES ani on ANI.IDE_NRANT=ANT.IDE_NRANT " +
					"inner join NRH_AMORTIZACION amo on amo.ide_nrani=ani.ide_nrani and activo_nramo=false " +
					"where ANT.IDE_GEEDP in ( " +
					"select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in ( " +
					"select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp+")) " +
					"and ACTIVO_NRANT=true " +
					"GROUP BY ANT.IDE_NRANT");
			TablaGenerica tab_amor=utilitario.consultar("select ANT.IDE_NRANT,AMO.IDE_NRAMO from NRH_ANTICIPO ant " +
					"inner join NRH_ANTICIPO_INTERES ani on ANI.IDE_NRANT=ANT.IDE_NRANT " +
					"inner join NRH_AMORTIZACION amo on amo.ide_nrani=ani.ide_nrani and activo_nramo=false " +
					"where ANT.IDE_GEEDP in ( " +
					"select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in ( " +
					"select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp+")) " +
					"and ACTIVO_NRANT=true " +
					"");

			for (int i = 0; i < tab_amor.getTotalFilas(); i++) {
				str_ide_nramo_descontar+=tab_amor.getValor(i, "IDE_NRAMO")+",";
			}
			return tab_ant.getSumaColumna("tot_desc_val_liq")+""; 
		}
		// importacion de dias pendientes de vacacion
		else if(ide_nrrub.equals(P_NRH_RUBRO_DIAS_PEND_VACACION)){
			return dias_pendientes_vacacion;
		}
		// importacion RMU del empleado				
		else if(ide_nrrub.equals(P_NRH_RUBRO_REMUNERACION_UNIFICADA)){		

			int int_dias_laborados=0;


			/*if (es_liquidacion){
				if (!getEmpleadoDepartamento(ide_geedp).getValor("IDE_GTTEM").equalsIgnoreCase(utilitario.getVariable("p_gth_tipo_empleado_codigo"))){
					return RMU;
				}
			}*/
///////// me inciada que si paso del primer dia la persona cobra honorarios.
					/*
			if (fecha_contrato!=null && !fecha_contrato.isEmpty()){
				int int_mes_rol=utilitario.getMes(fecha_final_gepro);
				int int_mes_contrato=utilitario.getMes(fecha_contrato);
				int int_anio_rol=utilitario.getAnio(fecha_final_gepro);
				int int_anio_contrato=utilitario.getAnio(fecha_contrato);
				int int_dia_rol=utilitario.getDia(fecha_inicial_gepro);
				int int_dia_contrato=utilitario.getDia(fecha_contrato);

				if (int_dia_contrato>int_dia_rol && int_mes_rol==int_mes_contrato && int_anio_rol==int_anio_contrato){
					return "0";
				}
			}
			*/
			return RMU;
		}						
		else if(ide_nrrub.equals(P_NRH_RUBRO_RMU_HONORARIOS)){					

			int int_dias_laborados=0;
			if (es_liquidacion){
				fecha_final_gepro=utilitario.getAnio(fecha_contrato)+"-"+utilitario.getMes(fecha_contrato)+"-"+utilitario.getDia(fecha_final_gepro);
			}

			if (fecha_contrato!=null && !fecha_contrato.isEmpty()){
				int int_mes_rol=utilitario.getMes(fecha_final_gepro);
				int int_mes_contrato=utilitario.getMes(fecha_contrato);
				int int_anio_rol=utilitario.getAnio(fecha_final_gepro);
				int int_anio_contrato=utilitario.getAnio(fecha_contrato);
				int int_dia_rol=utilitario.getDia(fecha_inicial_gepro);
				int int_dia_contrato=utilitario.getDia(fecha_contrato);

				if (int_dia_contrato>int_dia_rol && int_mes_rol==int_mes_contrato && int_anio_rol==int_anio_contrato){
					return RMU;
				}
			}
			return "0";
		}

		// 				
		else if(ide_nrrub.equals(P_NRH_RUBRO_AJUSTE_SUELDO)){					
			return ajuste_sueldo;
		}

		// importacion RMU del cargo subrogado				
		else if(ide_nrrub.equals(P_NRH_RUBRO_RMU_CARGO_SUBROGANTE)){					
			return RMU_CARGO_SUBROGA;
		}

		// importa si acumula o no fondos de reserva
		else if (ide_nrrub.equalsIgnoreCase(P_NRH_RUBRO_ACUMULA_FONDOS)){
			//System.out.println("acumlo fondos reserva"+acumula_fondos);

			if (acumula_fondos==null
					|| acumula_fondos.isEmpty()){
				return "0";
			}
			else if (acumula_fondos.equals("true")){
				return "1";
			}
			else if (acumula_fondos.equals("false")){
				return "0";
			}
			//System.out.println("retorna acumlo fondos reserva"+acumula_fondos);

			return acumula_fondos;
		}
		
		// importa si acumula decimo tercero
		else if (ide_nrrub.equalsIgnoreCase(P_NRH_ACUMULA_DECIMOS)){
			//System.out.println("acumlo decimo"+acumula_decimo);
			if (acumula_decimo==null
					|| acumula_decimo.isEmpty()){
				return "0";
			}
			else if (acumula_decimo.equals("true")){
				return "1";
			}
			return acumula_decimo;
		}
		
		// importa si contiene o no exceso de dias pagados en liquidacion
		else if (ide_nrrub.equalsIgnoreCase(utilitario.getVariable("p_nrh_ajuste_dias_exceso"))){
			//System.out.println("acumlo decimo"+acumula_decimo);
			boolean dias_exceso=false;
			int valorBandera=0;
			dias_exceso=diasExcesoLiquidacion(ide_geedp, ide_gepro);
			if (dias_exceso==false){
				valorBandera=0;
				return ""+valorBandera;
			}
			else if (dias_exceso==true){
				valorBandera=1;

				return ""+valorBandera;
			}
			return ""+valorBandera;
			
			
		}
		// importa si la base imponible del mes anterior para calculo del iess
		else if (ide_nrrub.equalsIgnoreCase(P_NRH_BASE_IMPONIBLE_MES_ANTERIOR)){
			
			
			TablaGenerica tab_empleado=utilitario.consultar("select ide_geedp,ide_gtemp,fecha_finctr_geedp  "
					+ "from gen_empleados_departamento_par  "
					+ "where ide_geedp="+ide_geedp+"");
			
			String fecha_iniTemp="";
			//retornaAccionpersonal(tab_empleado.getValor("IDE_GTEMP"), "23");
			String a="";
			TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+ide_geedp);
			//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
			int mesLiquidacion=0;
			TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
					+ "order by ide_geedp asc");
			String ide_geedp_empleado="";
			//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
			//variable guarda la fecha de salida
			String fecha_finTemp="";
			fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
			//variable cuenta el numero de acciones
			int contAccionInvalida=0,contForAccionInvalida=0;
			StringBuilder str_ide_geedp = new StringBuilder();

			for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
				if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(ide_geedp)==true) {
					String valorUltimoElemento="";
					valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
					if(valorUltimoElemento.equals(",")==true){
						str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
					}else {
					}

				i=tabEmpleados.getTotalFilas();
				}else{
						//Voy anidando los ides de la accion
						str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
		               // valor++;
		                if (tabEmpleados.getTotalFilas()==1) {
		   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
		   				contForAccionInvalida++;
		   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
		   					str_ide_geedp.append(",");
		                   // System.out.println("str_ide:  "+str_ide_empleado);
		   					}
		    			}	 	
				}
			}
			
			
			//Si no contiene acciones de personal luego de la liquidacion
			if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
				str_ide_geedp.append(ide_geedp);
			}

			
			//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
			a=utilitario.getVariable("p_nrh_acciones_personal");
			String[] listaAccionesEmpleado;
			listaAccionesEmpleado=a.split(",");
			for (int i = 0; i < listaAccionesEmpleado.length; i++) {
				fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
				if (!fecha_iniTemp.equals("")) {
					i=listaAccionesEmpleado.length;
				}
			}
			
			
	
			TablaGenerica tabAnio=utilitario.consultar("SELECT * FROM GEN_ANIO WHERE DETALLE_GEANI LIKE '%"+utilitario.getAnio(fecha_finTemp)+"%'");
			boolean bandRolGenerado=false;
			TablaGenerica valor_base_mes_anterior=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
					+" where a.ide_nrrol=b.ide_nrrol and ide_gepro=(select gepro.ide_gepro from gen_perido_rol gepro "
									+ "left join nrh_rol rol on rol.ide_gepro=gepro.ide_gepro "
									+ "where rol.ide_nrdtn in(2,4) and gepro.ide_gemes="+utilitario.getMes(fecha_finTemp)+" and gepro.ide_geani="+tabAnio.getValor("IDE_GEANI")+"  "
									+ "group by gepro.ide_gepro) "
								    + "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in (select  ide_gtemp from gen_empleados_departamento_par where ide_geedp="+ide_geedp+" ))  "
									+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_nrh_rubro_imponible_mes_anterior")+") and "
									+ "a.ide_nrdtn in(2,4)");
	   
			
			
			if(valor_base_mes_anterior.getTotalFilas()>0){
				bandRolGenerado=true;
						base_imponible_mes_anterior=valor_base_mes_anterior.getValor("valor_nrdro");
						//System.out.println("valor vbase "+base_imponible_mes_anterior);

						return base_imponible_mes_anterior;
					}
				else {
					int mes=utilitario.getMes(fecha_finTemp);
					String anio="";
					//Consulto el siguiente mes
					int sumaMes=0,restaMes=0;
					//Metodo para busqueda de remuneracion en rol de siguiente mes
					if (mes==1) {
						sumaMes=12;
						restaMes=0;
						tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+(utilitario.getAnio(fecha_finTemp)-1)+"%'");
						anio=tabAnio.getValor("IDE_GEANI");
						}else {
							sumaMes=utilitario.getMes(fecha_finTemp);
							restaMes=1;
							tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+(utilitario.getAnio(fecha_finTemp))+"%'");
							anio=tabAnio.getValor("IDE_GEANI");
						
						}
			
					TablaGenerica valor_base_mes_anterior1=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
							+" where a.ide_nrrol=b.ide_nrrol and ide_gepro=(select gepro.ide_gepro from gen_perido_rol gepro "
									+ "left join nrh_rol rol on rol.ide_gepro=gepro.ide_gepro "
									+ "where rol.ide_nrdtn in(2,4) and gepro.ide_gemes="+(sumaMes-restaMes)+" and gepro.ide_geani="+anio+"  "
									+ "group by gepro.ide_gepro) "
									+ "and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in (select  ide_gtemp from gen_empleados_departamento_par where ide_geedp="+ide_geedp+" ))  "
											+ "and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_nrh_rubro_imponible_mes_anterior")+") and "
											+ "a.ide_nrdtn in(2,4)");
					
				if(valor_base_mes_anterior1.getTotalFilas()>0){
						bandRolGenerado=true;
								base_imponible_mes_anterior=valor_base_mes_anterior.getValor("valor_nrdro");
								//System.out.println("valor vbase "+base_imponible_mes_anterior);

								return base_imponible_mes_anterior;
							}else {
								
								base_imponible_mes_anterior="0";

									return base_imponible_mes_anterior;
							}
				
					}
					

					
					
		}
		// importa los fondos de reserva acumuados del mes anterior
		else if (ide_nrrub.equalsIgnoreCase(utilitario.getVariable("p_nrh_rubro_fondreser_acum_pago"))){
			if (es_liquidacion){
				
				//Validacion de ajuste dias exceso
				boolean dias_exceso=false;
				int valorBandera=0;
				dias_exceso=diasExcesoLiquidacion(ide_geedp, ide_gepro);
				if (dias_exceso==false){
					valorBandera=0;
				}
				else if (dias_exceso==true){
					valorBandera=1;
				}			
				
				if (continuidad_liquidacion_geedp.equals("true") || valorBandera>0) {
					fondo_reserva_nom_pago="0.0";
					return fondo_reserva_nom_pago;
				}else{
				
				TablaGenerica tabEmpleadosDepartamento=utilitario.consultar("select * from gen_empleados_departamento_par where ide_geedp in("+ide_geedp+")");
				TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tabEmpleadosDepartamento.getValor("ide_gtemp")+") "
						+ "order by ide_geedp desc");
				String ide_geedp_empleado="";
				//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
				//variable guarda la fecha de salida
				String fecha_finTemp="";
				fecha_finTemp=tabEmpleadosDepartamento.getValor("fecha_finctr_geedp");
				//variable cuenta el numero de acciones
				int contAccionInvalida=0,contForAccionInvalida=0;
				StringBuilder str_ide_geedp = new StringBuilder();

				for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
					if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(ide_geedp)) {
						contAccionInvalida++;
					}
								
						if (contAccionInvalida>0) {
							
							if (contAccionInvalida>1) {
								
							
							//Voy anidando los ides de la accion
							str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
			               // valor++;
			                if (tabEmpleados.getTotalFilas()==1) {
			   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
			   				contForAccionInvalida++;
			   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
			   					str_ide_geedp.append(",");
			                   // System.out.println("str_ide:  "+str_ide_empleado);
			   					}
			    			}
			   			 	
						}
						contAccionInvalida++;
					}
						if (i==(tabEmpleados.getTotalFilas()-1)) {
							String valorUltimoElemento="";
							valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
							if(valorUltimoElemento.equals(",")==true){
								str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));						}				 
				}
				
				}
				
				//Si no contiene acciones de personal luego de la liquidacion
				if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
					str_ide_geedp.append(ide_geedp);
				}
				
				/*TablaGenerica tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
						+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
						+ "from gen_empleados_departamento_par epar "
						+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
						+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
						+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
						+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
						//+ "where epar.ide_gtemp="+tabEmpleadosDepartamento.getValor("ide_gtemp")+" and gaed.ide_geaed in(3) "
						+ "where epar.ide_gtemp="+tabEmpleadosDepartamento.getValor("ide_gtemp")+" and gaed.ide_geaed in(3) "
						+ "order by epar.ide_geedp desc");*/
				
				TablaGenerica tabAccionesEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_geedp not in("+str_ide_geedp.toString()+")  "
						+ "and ide_gtemp in("+tabEmpleadosDepartamento.getValor("ide_gtemp")+")");

				
				
				
				
				int cont=0;
				StringBuilder str = new StringBuilder();
				for (int i = 0; i < tabAccionesEmpleados.getTotalFilas(); i++) {
				  
				  		//Voy anidando los ides de la accion
				          	    str.append(tabAccionesEmpleados.getValor(i, "IDE_GEEDP"));
				               // valor++;
				                if (tabAccionesEmpleados.getTotalFilas()==1) {
				   			}else if (cont<=tabAccionesEmpleados.getTotalFilas()) {
				   					cont++;
				   					if(cont<(tabAccionesEmpleados.getTotalFilas())){
				                    str.append(",");
				                   // System.out.println("str_ide:  "+str_ide_empleado);
				   					}
				    			}
				   			 		         		
				}
				int mes=utilitario.getMes(fecha_finTemp);
				TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_finTemp)+"%' ");
				String anio=tabAnio.getValor("IDE_GEANI");
				//TablaGenerica rol=utilitario.consultar("select * from gen_perido_rol where ide_gepro="+ide_gepro);
				//String ide_mes="",ide_geani="";
				//ide_mes=rol.getValor("IDE_GEMES");
				//ide_geani=rol.getValor("IDE_GEANI");
				TablaGenerica tab_detalle_rol=null;
				int fondo=0;
				
				TablaGenerica tabLiquidacion=utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
						+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
						+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
						+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
						+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
						+ "where DRO.IDE_GEEDP in ("+str.toString()+") "
						+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+mes+" "
						//+ "and RUB.ide_nrrub in("+utilitario.getVariable("p_nrh_rubro_fondreser_acum_ante")+") "
						//+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) "
						+ "order by RUB.ide_nrrub asc");
				boolean ejecutoRol=false;
				if (tabLiquidacion.getTotalFilas()>0) {
					ejecutoRol=true;
				}else {
					ejecutoRol=false;
				}
				
				
				tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
						+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
						+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
						+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
						+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
						+ "where DRO.IDE_GEEDP in ( "
						+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
						+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str_ide_geedp.toString()+")) and ide_gttem !=3  "
						+ ")  "
						+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+mes+" "
						+ "and RUB.ide_nrrub in("+utilitario.getVariable("p_nrh_rubro_fondreser_acum_ante")+") "
						+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) "
						+ "order by RUB.ide_nrrub asc");

				if (tab_detalle_rol.getTotalFilas()>0) {
				fondo=0;					
				}else {
					//Consulto el siguiente mes
					int sumaMes=0,restaMes=0;
					//Metodo para busqueda de remuneracion en rol de siguiente mes
					if (mes==1) {
						mes=12;
						restaMes=0;
						tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+(utilitario.getAnio(fecha_finTemp)-1)+"%'");
						anio=tabAnio.getValor("IDE_GEANI");
						}else {
							mes=utilitario.getMes(fecha_finTemp);
							restaMes=1;
							tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+(utilitario.getAnio(fecha_finTemp))+"%'");
							anio=tabAnio.getValor("IDE_GEANI");
							mes=mes-restaMes;
						}
					
				 tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
							+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
							+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
							+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
							+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
							+ "where DRO.IDE_GEEDP in ( "
							+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
							+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str_ide_geedp.toString()+")) and ide_gttem !=3  "
							+ ")  "
							+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+(mes)+" "
							+ "and RUB.ide_nrrub in("+utilitario.getVariable("p_nrh_rubro_fondreser_acum_ante")+") "
							+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) "
							+ "order by RUB.ide_nrrub asc");

					if (tab_detalle_rol.getTotalFilas()>0) {
						fondo=2;	
						}else {
							fondo=0;
						}
				}	
	
				if(tab_detalle_rol.getTotalFilas()>0)
				{
					if (fondo==1) {
						if (tab_detalle_rol.getValor("ide_nrrub").equals("136")) {
							fondo_reserva_nom_pago=tab_detalle_rol.getValor("valor_nrdro");			
						}else {
						tab_detalle_rol=null;
						tab_detalle_rol = utilitario.consultar("select DRO.IDE_GEEDP,sum(dro.valor_nrdro) as total  "
								+ "from NRH_DETALLE_ROL DRO  "
								+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
								+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
								+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
								+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
								+ "where DRO.IDE_GEEDP in ( "
								+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
								+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str_ide_geedp.toString()+")) and ide_gttem !=3  "
								+ ")  "
								+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+(mes)+" "
								+ "and RUB.ide_nrrub in(288,331,336,382,17) "
								+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) "
								+ "group by DRO.IDE_GEEDP");
						
						
						
						double valorIngresos=0.0;
						//valorIngresos=Double.parseDouble(utilitario.getFormatoNumero((Double.parseDouble(tab_detalle_rol.getValor("total"))/30),2));
						if (tab_detalle_rol.getValor("total")==null || tab_detalle_rol.getValor("total").equals("") || tab_detalle_rol.getValor("total").isEmpty()) {
							valorIngresos=0.0;
							tab_detalle_rol=null;
							tab_detalle_rol = utilitario.consultar("select DRO.IDE_GEEDP,dro.valor_nrdro from NRH_DETALLE_ROL DRO  "
									+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
									+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
									+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
									+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
									+ "where DRO.IDE_GEEDP in ( "
									+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
									+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str_ide_geedp.toString()+")) and ide_gttem !=3  "
									+ ")  "
									+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+(mes)+" "
									+ "and RUB.ide_nrrub in(69) "
									+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) ");
								
							if (tab_detalle_rol.getValor("total")==null || tab_detalle_rol.getValor("total").equals("") || tab_detalle_rol.getValor("total").isEmpty()) {
								valorIngresos=0.0;
							}else{
								valorIngresos=(Double.parseDouble(tab_detalle_rol.getValor("total"))/30);	
							}	
							
							
						}else{
							valorIngresos=(Double.parseDouble(tab_detalle_rol.getValor("total"))/30);	
						}
				
						
						
						double valorFondo=0.0;
						//valorFondo=Double.parseDouble(utilitario.getFormatoNumero((valorIngresos*0.0833),2))* Double.parseDouble(tab_detalle_rol.getValor("valor_nrdro"));
						valorFondo=(valorIngresos*0.0833) * Double.parseDouble(tab_detalle_rol.getValor("valor_nrdro"));
						fondo_reserva_nom_pago=utilitario.getFormatoNumero(valorFondo,2);
						}
					}else if (fondo==2) {
						if (tab_detalle_rol.getValor("ide_nrrub").equals("136")) {
							fondo_reserva_nom_pago=tab_detalle_rol.getValor("valor_nrdro");			
						}else {
							tab_detalle_rol=null;
							tab_detalle_rol = utilitario.consultar("select DRO.IDE_GEEDP,sum(dro.valor_nrdro) as total  "
									+ "from NRH_DETALLE_ROL DRO  "
									+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
									+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
									+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
									+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
									+ "where DRO.IDE_GEEDP in ( "
									+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
									+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str_ide_geedp.toString()+")) and ide_gttem !=3  "
									+ ")  "
									+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+(mes)+" "
									+ "and RUB.ide_nrrub in(288,331,336,382,17) "
									+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) "
									+ "group by DRO.IDE_GEEDP");
							
							double valorIngresos=0.0;
							
							if (tab_detalle_rol.getValor("total")==null || tab_detalle_rol.getValor("total").equals("") || tab_detalle_rol.getValor("total").isEmpty()) {
								valorIngresos=0.0;
								//valorIngresos=Double.parseDouble(utilitario.getFormatoNumero((Double.parseDouble(tab_detalle_rol.getValor("total"))/30),2));
								tab_detalle_rol=null;
								tab_detalle_rol = utilitario.consultar("select DRO.IDE_GEEDP,dro.valor_nrdro from NRH_DETALLE_ROL DRO  "
										+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
										+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
										+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
										+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
										+ "where DRO.IDE_GEEDP in ( "
										+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
										+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str_ide_geedp.toString()+")) and ide_gttem !=3  "
										+ ")  "
										+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+(mes)+" "
										+ "and RUB.ide_nrrub in(69) "
										+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) ");
							
								if (tab_detalle_rol.getValor("total")==null || tab_detalle_rol.getValor("total").equals("") || tab_detalle_rol.getValor("total").isEmpty()) {
									valorIngresos=0.0;
								}else{
						 			   valorIngresos=(Double.parseDouble(tab_detalle_rol.getValor("total"))/30);}	
								
								
							}else{
			 			   valorIngresos=(Double.parseDouble(tab_detalle_rol.getValor("total"))/30);}
							
							
							double valorFondo=0.0;
							valorFondo=(valorIngresos*0.0833) * Double.parseDouble(tab_detalle_rol.getValor("valor_nrdro"));
							//valorFondo=Double.parseDouble(utilitario.getFormatoNumero((valorIngresos*0.0833),2))* Double.parseDouble(tab_detalle_rol.getValor("valor_nrdro"));
							fondo_reserva_nom_pago=utilitario.getFormatoNumero(valorFondo,2);
							}
						}else {
							fondo_reserva_nom_pago=""+0.0;
						return fondo_reserva_nom_pago;		
							
						}
					//System.out.println("valor vbase "+base_imponible_mes_anterior);
					return fondo_reserva_nom_pago;
				}
				else {
					fondo_reserva_nom_pago="0.0";
					return fondo_reserva_nom_pago;
				}
		
				}
				
				
			}else {
			
					TablaGenerica rol_anterior=utilitario.consultar("select * from gen_perido_rol where ide_gepro="+ide_gepro);
					TablaGenerica valor_base_mes_anterior=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
							+" where a.ide_nrrol=b.ide_nrrol and ide_gepro="+rol_anterior.getValor("gen_ide_gepro")+" and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in (select  ide_gtemp from gen_empleados_departamento_par where ide_geedp="+ide_geedp+" ))  and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_nrh_rubro_fondreser_acum_ante")+")");
					
					//rol_anterior.imprimirSql();
					
					if(valor_base_mes_anterior.getTotalFilas()>0){
						fondo_reserva_acum_pago=valor_base_mes_anterior.getValor("valor_nrdro");
						//System.out.println("valor vbase "+base_imponible_mes_anterior);

						return fondo_reserva_acum_pago;
					}
					else {
						fondo_reserva_acum_pago="0.00";

						return fondo_reserva_acum_pago;
					}
			}
		}
//Auqi		
//importa fondos de reserva nomina del mes anterior
else if (ide_nrrub.equalsIgnoreCase(P_NRH_RUBRO_FONRESERV_NOM_PAGO)){
	if (es_liquidacion){
		//Validacion de ajuste dias exceso
		boolean dias_exceso=false;
		int valorBandera=0;
		dias_exceso=diasExcesoLiquidacion(ide_geedp, ide_gepro);
		if (dias_exceso==false){
			valorBandera=0;
		}
		else if (dias_exceso==true){
			valorBandera=1;
		}			
	
		if (continuidad_liquidacion_geedp.equals("false") || valorBandera>0) {
			fondo_reserva_nom_pago="0.00";
			return fondo_reserva_nom_pago;
		}else{		
		String variable="";
		variable=utilitario.getVariable("p_nrh_rubro_fondreser_nomi_ante");
		TablaGenerica tabEmpleadosDepartamento=utilitario.consultar("select * from gen_empleados_departamento_par where ide_geedp in("+ide_geedp+") "
				+ "order by ide_geedp asc");
		TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tabEmpleadosDepartamento.getValor("ide_gtemp")+") "
				+ "order by ide_geedp desc");

		String fecha_finTemp="";
		fecha_finTemp=tabEmpleadosDepartamento.getValor("fecha_finctr_geedp");
		int contAccionInvalida=0,contForAccionInvalida=0;
		StringBuilder str_ide_geedp = new StringBuilder();

		for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
			if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(ide_geedp)) {
					contAccionInvalida++;
			}
			
			
			if (contAccionInvalida>0) {
				if (contAccionInvalida>1) {
				//Voy anidando los ides de la accion
				str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
               // valor++;
                if (tabEmpleados.getTotalFilas()==1) {
   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
   				contForAccionInvalida++;
   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
   					str_ide_geedp.append(",");
                   // System.out.println("str_ide:  "+str_ide_empleado);
   					}
    			}
   			 	
			}
				contAccionInvalida++;	
		}
			
			if (i==(tabEmpleados.getTotalFilas()-1)) {
				String valorUltimoElemento="";
				valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
				if(valorUltimoElemento.equals(",")==true){
					str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));						}				 
			}
			
			
		}
		
		//Si no contiene acciones de personal luego de la liquidacion
		if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
			str_ide_geedp.append(ide_geedp);
		}
		
		
		
		
		
		
		
		
		
		/*String ide_geedp_empleado="";
	//	String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
		TablaGenerica tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
				+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
				+ "from gen_empleados_departamento_par epar "
				+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
				+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
				+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
				+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
				+ "where epar.ide_gtemp="+tabEmpleadosDepartamento.getValor("ide_gtemp")+" and gaed.ide_geaed in(3)");
		*/
		TablaGenerica tabAccionesEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_geedp not in("+str_ide_geedp.toString()+")  "
				+ "and ide_gtemp in("+tabEmpleadosDepartamento.getValor("ide_gtemp")+")");

		int cont=0;
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < tabAccionesEmpleados.getTotalFilas(); i++) {
		  
		  		//Voy anidando los ides de la accion
		          	    str.append(tabAccionesEmpleados.getValor(i, "IDE_GEEDP"));
		               // valor++;
		                if (tabAccionesEmpleados.getTotalFilas()==1) {
		   			}else if (cont<=tabAccionesEmpleados.getTotalFilas()) {
		   					cont++;
		   					if(cont<(tabAccionesEmpleados.getTotalFilas())){
		                    str.append(",");
		                   // System.out.println("str_ide:  "+str_ide_empleado);
		   					}
		    			}
		   			 		         		
		}
		int mes=utilitario.getMes(fecha_finTemp);
		TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_finTemp)+"%' ");
		String anio=tabAnio.getValor("IDE_GEANI");
		//TablaGenerica rol=utilitario.consultar("select * from gen_perido_rol where ide_gepro="+ide_gepro);
		//String ide_mes="",ide_geani="";
		//ide_mes=rol.getValor("IDE_GEMES");
		//ide_geani=rol.getValor("IDE_GEANI");
		TablaGenerica tab_detalle_rol=null;
		int fondo=0;

		
		TablaGenerica tabLiquidacion=utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
				+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
				+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
				+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
				+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
				+ "where DRO.IDE_GEEDP in ("+str.toString()+") "
				+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+mes+" "
				//+ "and RUB.ide_nrrub in("+utilitario.getVariable("p_nrh_rubro_fondreser_acum_ante")+") "
				//+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) "
				+ "order by RUB.ide_nrrub asc");
		boolean ejecutoRol=false;
		if (tabLiquidacion.getTotalFilas()>0) {
			ejecutoRol=true;
		}else {
			ejecutoRol=false;
		}
	
		tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
				+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
				+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
				+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
				+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
				+ "where DRO.IDE_GEEDP in ( "
				+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
				+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str_ide_geedp.toString()+")) and ide_gttem !=3  "
				+ ")  "
				+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+mes+" "
				+ "and RUB.ide_nrrub in("+utilitario.getVariable("p_nrh_rubro_fondreser_nomi_ante_liquidacion")+") "
				+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) "
				+ "order by RUB.ide_nrrub asc");

		if (tab_detalle_rol.getTotalFilas()>0) {
			fondo=0;	
		}else {
			
			//Consulto el siguiente mes
			int sumaMes=0,restaMes=0;
			//Metodo para busqueda de remuneracion en rol de siguiente mes
			if (mes==1) {
				mes=12;
				restaMes=0;
				tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+(utilitario.getAnio(fecha_finTemp)-1)+"%'");
				anio=tabAnio.getValor("IDE_GEANI");
				}else {
					mes=utilitario.getMes(fecha_finTemp);
					mes=mes-1;
					tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+(utilitario.getAnio(fecha_finTemp))+"%'");
					anio=tabAnio.getValor("IDE_GEANI");	
				}
			
			tab_detalle_rol=null;
			int valorRestaMes=0;
			valorRestaMes=mes;
		 tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
					+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
					+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
					+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
					+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
					+ "where DRO.IDE_GEEDP in ( "
					+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
					+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str_ide_geedp.toString()+")) and ide_gttem !=3  "
					+ ")  "
					+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+(mes)+" "
					+ "and RUB.ide_nrrub in("+utilitario.getVariable("p_nrh_rubro_fondreser_nomi_ante_liquidacion")+") "
					+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) "
					+ "order by RUB.ide_nrrub asc");

		 if (tab_detalle_rol.getTotalFilas()>0) {
				fondo=2;	
				}else {
					fondo=0;
				}
		}

		/*TablaGenerica valor_base_mes_anterior=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
				+" where a.ide_nrrol=b.ide_nrrol and ide_gepro="+tab_detalle_rol.getValor("ide_gepro")+" and ide_geedp = "+tab_detalle_rol.getValor("IDE_GEEDP")+" and "
						+ "ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_nrh_rubro_fondreser_nomi_ante")+")");*/
		
		
		if(tab_detalle_rol.getTotalFilas()>0){
			
			
			if (fondo==1) {
				if (tab_detalle_rol.getValor("ide_nrrub").equals("29")) {
					fondo_reserva_nom_pago=tab_detalle_rol.getValor("valor_nrdro");			
				}else {
		
				tab_detalle_rol=null;
				tab_detalle_rol = utilitario.consultar("select DRO.IDE_GEEDP,sum(dro.valor_nrdro) as total from NRH_DETALLE_ROL DRO  "
						+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
						+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
						+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
						+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
						+ "where DRO.IDE_GEEDP in ( "
						+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
						+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str_ide_geedp.toString()+")) and ide_gttem !=3  "
						+ ")  "
						+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+(mes)+" "
						+ "and RUB.ide_nrrub in(288,331,336,382,17) "
						+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) "
						+ "group by DRO.IDE_GEEDP");
				
				double valorIngresos=0.0;
				//valorIngresos=Double.parseDouble(utilitario.getFormatoNumero((Double.parseDouble(tab_detalle_rol.getValor("total"))/30),2));
				
				if (tab_detalle_rol.getValor("total")==null || tab_detalle_rol.getValor("total").equals("") || tab_detalle_rol.getValor("total").isEmpty()) {
					valorIngresos=0.0;
					tab_detalle_rol=null;
					tab_detalle_rol = utilitario.consultar("select DRO.IDE_GEEDP,dro.valor_nrdro from NRH_DETALLE_ROL DRO  "
							+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
							+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
							+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
							+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
							+ "where DRO.IDE_GEEDP in ( "
							+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
							+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str_ide_geedp.toString()+")) and ide_gttem !=3  "
							+ ")  "
							+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+(mes)+" "
							+ "and RUB.ide_nrrub in(69) "
							+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) ");

					if (tab_detalle_rol.getValor("total")==null || tab_detalle_rol.getValor("total").equals("") || tab_detalle_rol.getValor("total").isEmpty()) {
						valorIngresos=0.0;
					}else{
						valorIngresos=Double.parseDouble(tab_detalle_rol.getValor("total"))/30;}
				
				
				}else{
				valorIngresos=Double.parseDouble(tab_detalle_rol.getValor("total"))/30;}
								
				double valorFondo=0.0;
				//valorFondo=Double.parseDouble(utilitario.getFormatoNumero((valorIngresos*0.0833),2))* Double.parseDouble(tab_detalle_rol.getValor("valor_nrdro"));
				valorFondo=(valorIngresos*0.0833) * (Double.parseDouble(tab_detalle_rol.getValor("valor_nrdro")));
				fondo_reserva_nom_pago=utilitario.getFormatoNumero(valorFondo,2);
					
				}
				}else if (fondo==2) {
					if (tab_detalle_rol.getValor("ide_nrrub").equals("29")) {
						return fondo_reserva_nom_pago=tab_detalle_rol.getValor("valor_nrdro");			
					}else {
						
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						if (Double.parseDouble(tab_detalle_rol.getValor("VALOR_NRDRO"))<0.0 || Double.parseDouble(tab_detalle_rol.getValor("VALOR_NRDRO"))<0.00) {
							return fondo_reserva_nom_pago=""+0.0;			
						}else {
							fondo_reserva_nom_pago=""+Double.parseDouble(tab_detalle_rol.getValor("VALOR_NRDRO"));
							
						/*tab_detalle_rol=null;
						tab_detalle_rol = utilitario.consultar("select DRO.IDE_GEEDP,sum(dro.valor_nrdro) as total "
								+ "from NRH_DETALLE_ROL DRO  "
								+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
								+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
								+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
								+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
								+ "where DRO.IDE_GEEDP in ( "
								+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
								+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str_ide_geedp.toString()+")) and ide_gttem !=3  "
								+ ")  "
								+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+(mes)+" "
								+ "and RUB.ide_nrrub in(288,331,336,382,17) "
								+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) "
								+ "group by DRO.IDE_GEEDP");
						
						double valorIngresos=0.0;
						if (tab_detalle_rol.getTotalFilas()>0) {
								
						
						if (tab_detalle_rol.getValor("total")==null || tab_detalle_rol.getValor("total").equals("") || tab_detalle_rol.getValor("total").isEmpty()) {
							valorIngresos=0.0;
							tab_detalle_rol=null;
							tab_detalle_rol = utilitario.consultar("select DRO.IDE_GEEDP,dro.valor_nrdro from NRH_DETALLE_ROL DRO  "
									+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
									+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
									+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
									+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
									+ "where DRO.IDE_GEEDP in ( "
									+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
									+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str_ide_geedp.toString()+")) and ide_gttem !=3  "
									+ ")  "
									+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+(mes)+" "
									+ "and RUB.ide_nrrub in(69) "
									+ "and dro.valor_nrdro!=0 AND DER.IDE_NRDTN IN(4,2) ");
			
							if (tab_detalle_rol.getValor("total")==null || tab_detalle_rol.getValor("total").equals("") || tab_detalle_rol.getValor("total").isEmpty()) {
								valorIngresos=0.0;
							}else{
								valorIngresos=Double.parseDouble(tab_detalle_rol.getValor("total"))/30;}
								
						}else {
							valorIngresos=Double.parseDouble(tab_detalle_rol.getValor("total"))/30;	
						}	
						
						
						
						}else{
							if (tab_detalle_rol.getValor("total")==null || tab_detalle_rol.getValor("total").equals("") || tab_detalle_rol.getValor("total").isEmpty()) {
								valorIngresos=0.0;}else{
						
						valorIngresos=Double.parseDouble(tab_detalle_rol.getValor("total"))/30;}
						}*/
						//valorIngresos=Double.parseDouble(utilitario.getFormatoNumero((Double.parseDouble(tab_detalle_rol.getValor("total"))/30),2));
						
						
						//double valorFondo=0.0;
						//valorFondo=Double.parseDouble(utilitario.getFormatoNumero((valorIngresos*0.0833),2))* Double.parseDouble(tab_detalle_rol.getValor("valor_nrdro"));
						
						//valorFondo=(valorIngresos*0.0833)* Double.parseDouble(tab_detalle_rol.getValor("valor_nrdro"));
						//fondo_reserva_nom_pago=utilitario.getFormatoNumero(valorFondo,2);
						}
				}//si el valor es en negativo
			}else {
				return "0.0";
			}
			//fondo_reserva_nom_pago=tab_detalle_rol.getValor("valor_nrdro");
			//System.out.println("valor vbase "+base_imponible_mes_anterior);

			return fondo_reserva_nom_pago;
		}
		else {
			fondo_reserva_nom_pago="0.0";

			return fondo_reserva_nom_pago;
		}
		
		
	}//if de continuidad
	}else {
		
		TablaGenerica rol_anterior=utilitario.consultar("select * from gen_perido_rol where ide_gepro="+ide_gepro);
		TablaGenerica valor_base_mes_anterior=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
				+" where a.ide_nrrol=b.ide_nrrol and ide_gepro="+rol_anterior.getValor("gen_ide_gepro")+" and ide_geedp in (select ide_geedp from gen_empleados_departamento_par where ide_gtemp in (select  ide_gtemp from gen_empleados_departamento_par where ide_geedp="+ide_geedp+" ))  and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_nrh_rubro_fondreser_nomi_ante")+")");
		
		//rol_anterior.imprimirSql();
		
		if(valor_base_mes_anterior.getTotalFilas()>0){
			fondo_reserva_acum_pago=valor_base_mes_anterior.getValor("valor_nrdro");
			//System.out.println("valor vbase "+base_imponible_mes_anterior);

			return fondo_reserva_acum_pago;
		}
		else {
			fondo_reserva_acum_pago="0";

			return fondo_reserva_acum_pago;
		}
		
		
	}
	
}	// importa numero dias ajuste sueldo
		else if(ide_nrrub.equals(P_NRH_RUBRO_DIAS_AJUSTE_SUELDO)){	 
			int int_dias_ajuste=0;
			if (fecha_ajuste_sueldo!=null && !fecha_ajuste_sueldo.isEmpty()){
				int int_mes_rol=utilitario.getMes(fecha_final_gepro);
				int int_mes_ajuste=utilitario.getMes(fecha_ajuste_sueldo);
				int int_anio_rol=utilitario.getAnio(fecha_final_gepro);
				int int_anio_ajuste=utilitario.getAnio(fecha_ajuste_sueldo);
				if (int_mes_rol==int_mes_ajuste && int_anio_rol==int_anio_ajuste){
					int_dias_ajuste=utilitario.getDiferenciasDeFechas(utilitario.getFecha(fecha_inicial_gepro), utilitario.getFecha(fecha_ajuste_sueldo));
					return int_dias_ajuste+"";
				}
			}
			return int_dias_ajuste+"";
		}

		// importa numero dias trabajados al mes 
		else if(ide_nrrub.equals(P_NRH_RUBRO_DIAS_TRABAJADOS)){	 
			int int_dias_laborados=0;
			/*if (es_liquidacion){
				if (!getEmpleadoDepartamento(ide_geedp).getValor("IDE_GTTEM").equalsIgnoreCase(utilitario.getVariable("p_gth_tipo_empleado_codigo"))){
					
					
					return NRO_DIAS_COMERCIAL_NRTIT+"";
				}
			}*/

			if (fecha_contrato!=null && !fecha_contrato.isEmpty()){
				int int_mes_rol=0;
				int int_mes_contrato=0;
				int int_anio_rol=0;
				int int_anio_contrato=0;

				if (es_liquidacion){
					 fecha_inicial_gepro=utilitario.getAnio(fecha_contrato)+"-"+utilitario.getMes(fecha_contrato)+"-01";
					 fecha_final_gepro=fecha_contrato;
					 
					 int_mes_rol=utilitario.getMes(fecha_final_gepro);
					 int_mes_contrato=utilitario.getMes(fecha_contrato);
					 int_anio_rol=utilitario.getAnio(fecha_final_gepro);
					 int_anio_contrato=utilitario.getAnio(fecha_contrato);
				
				}else {
					 int_mes_rol=utilitario.getMes(fecha_final_gepro);
					 int_mes_contrato=utilitario.getMes(fecha_contrato);
					 int_anio_rol=utilitario.getAnio(fecha_final_gepro);
					 int_anio_contrato=utilitario.getAnio(fecha_contrato);

				}
					
	
				String fecha_fin_calculo=utilitario.getAnio(fecha_final_gepro)+"-"+utilitario.getMes(fecha_final_gepro)+"-30";
				if (utilitario.getDia(fecha_contrato)==31) {
					fecha_contrato=utilitario.getAnio(fecha_contrato)+"-"+utilitario.getMes(fecha_contrato)+"-"+30;
				}
				
				if (int_mes_rol==int_mes_contrato && int_anio_rol==int_anio_contrato){
					if (es_liquidacion){
						int_dias_laborados=utilitario.getDiferenciasDeFechas(utilitario.getFecha(fecha_inicial_gepro), utilitario.getFecha(fecha_contrato))+1;
					}else{
						int_dias_laborados=utilitario.getDiferenciasDeFechas(utilitario.getFecha(fecha_contrato), utilitario.getFecha(fecha_fin_calculo))+1;
					}
					if (int_dias_laborados>NRO_DIAS_COMERCIAL_NRTIT){
						try {
							int_dias_laborados=NRO_DIAS_COMERCIAL_NRTIT;
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}else{
					try {
						int_dias_laborados=NRO_DIAS_COMERCIAL_NRTIT;
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			return int_dias_laborados+"";
		}
		// importa numero dias subrogados 
		else if(ide_nrrub.equals(P_NRH_RUBRO_DIAS_SUBROGADOS)){	 
			int int_dias_subrogados=0;
//			System.out.println("fecha ini subrogacion "+fecha_subrogacion);
//			System.out.println("fecha fin subrogacion "+fecha_fin_subrogacion);
			if (fecha_subrogacion!=null && !fecha_subrogacion.isEmpty()
					&& fecha_fin_subrogacion!=null && !fecha_fin_subrogacion.isEmpty()){
				
				int mes_rol=utilitario.getMes(fecha_inicial_gepro);
				int anio_rol=utilitario.getAnio(fecha_inicial_gepro);
				String fecha_ini_rol=anio_rol+"-"+mes_rol+"-1";
				String fecha_fin_rol=anio_rol+"-"+mes_rol+"-30";
				int mes_fin_sub=utilitario.getMes(fecha_fin_subrogacion);
				int anio_fin_sub=utilitario.getAnio(fecha_fin_subrogacion);
				int mes_ini_sub=utilitario.getMes(fecha_subrogacion);
				int anio_ini_sub=utilitario.getAnio(fecha_subrogacion);
				
				if (mes_rol==mes_ini_sub && anio_rol==anio_ini_sub){
					if (mes_rol==mes_fin_sub && anio_rol==anio_fin_sub){
						return (utilitario.getDiferenciasDeFechas(utilitario.getFecha(fecha_subrogacion), utilitario.getFecha(fecha_fin_subrogacion))+1)+"";					
					}else{
						return (utilitario.getDiferenciasDeFechas(utilitario.getFecha(fecha_subrogacion), utilitario.getFecha(fecha_fin_rol))+1)+"";
					}
				}else{
					if (mes_rol==mes_fin_sub && anio_rol==anio_fin_sub){
						return (utilitario.getDiferenciasDeFechas(utilitario.getFecha(fecha_ini_rol), utilitario.getFecha(fecha_fin_subrogacion))+1)+"";
					}else{
						if (mes_rol<mes_fin_sub && anio_rol<=anio_fin_sub){
							return NRO_DIAS_COMERCIAL_NRTIT+"";
						}
					}
				}
				
				
					
				
				
				
				
//				if (utilitario.isFechaMenor(utilitario.getFecha(fecha_fin_subrogacion), utilitario.getFecha(fecha_final_gepro))){
//
//					int int_mes_rol=utilitario.getMes(fecha_fin_subrogacion);
//					int int_mes_subrogacion=utilitario.getMes(fecha_subrogacion);
//					int int_anio_rol=utilitario.getAnio(fecha_fin_subrogacion);
//					int int_anio_subrogacion=utilitario.getAnio(fecha_subrogacion);
//
//					if (int_mes_rol==int_mes_subrogacion && int_anio_rol==int_anio_subrogacion){
//						int_dias_subrogados=utilitario.getDiferenciasDeFechas(utilitario.getFecha(fecha_subrogacion), utilitario.getFecha(fecha_fin_subrogacion))+1;
//						if (int_dias_subrogados>30){
//							int_dias_subrogados=30;
//						}
//					}else{
//						int_dias_subrogados=NRO_DIAS_COMERCIAL_NRTIT;
//					}
//				}dd
			}
			return int_dias_subrogados+"";
		}


		// importa los dias antiguedad
		else if(ide_nrrub.equals(P_NRH_RUBRO_DIAS_ANTIGUEDAD)){
			return dias_antiguedad;
		}
		
		// importa los dias trabajados por rango de fechas
		else if(ide_nrrub.equals(utilitario.getVariable("p_nrh_rubro_dias_acumulados"))){
			System.out.println("getDiasAcumulados "+ fecha_inicial_gepro+" - "+ fecha_final_gepro + " fecha_ingreso_emgirs: "+fecha_ingreso_emgirs);
			return getDiasAcumulados(ide_geedp,fecha_inicial_gepro,fecha_final_gepro,fecha_ingreso_emgirs); 
		}
		
		// importa el decimo cuarto suelo
		else if(ide_nrrub.equals(utilitario.getVariable("p_nrh_rubro_total_proviciones_d4"))){
			System.out.println("getDecimoCuartoSueldoOK "+ fecha_inicial_gepro+" - "+ fecha_final_gepro);
			return getDecimoCuartoSueldoOK(ide_geedp,fecha_inicial_gepro,fecha_final_gepro); 
		}
		
		// importa el decimo cuarto suelo pagado
		else if(ide_nrrub.equals(utilitario.getVariable("p_nrh_rubro_decimo_cuarto_rol_p"))){
			System.out.println("getDecimoCuartoSueldoPagado "+ fecha_inicial_gepro+" - "+ fecha_final_gepro);
			return getDecimoCuartoSueldoPagado(ide_geedp,fecha_inicial_gepro,fecha_final_gepro); 
		}

        // importa el decimo tercer suelo 
		else if(ide_nrrub.equals(utilitario.getVariable("p_nrh_rubro_total_proviciones_d3"))){
			System.out.println("getDecimoTercerSueldoOK "+ fecha_inicial_gepro+" - "+ fecha_final_gepro);
			return getDecimoTercerSueldoOK(ide_geedp,fecha_inicial_gepro,fecha_final_gepro); 
		}
		
		// importa el decimo tercer suelo pagado
		else if(ide_nrrub.equals(utilitario.getVariable("p_nrh_rubro_decimo_tercer_rol_p"))){
			System.out.println("getDecimoTercerSueldoPagado "+ fecha_inicial_gepro+" - "+ fecha_final_gepro);
			return getDecimoTercerSueldoPagado(ide_geedp,fecha_inicial_gepro,fecha_final_gepro); 
		}
	
		//LIQUIDACIONES 
		
		
		// importa el decimo tercer suelo pagado
		else if(ide_nrrub.equals(utilitario.getVariable("p_nrh_total_decimot_liquidacion"))){
			System.out.println("getDecimoTercerAcumulado "+ fecha_inicial_gepro+" - "+ fecha_final_gepro);
			return getDecimoTercerSueldoPagadoLiquidacion(ide_geedp,fecha_inicial_gepro,fecha_final_gepro,3); 
		}

		
		
		
		// importa el decimo CUARTO suelo pagado
		else if(ide_nrrub.equals(utilitario.getVariable("p_nrh_total_decimoc_liquidacion"))){
			System.out.println("getDecimoCuartoAcumulado "+ fecha_inicial_gepro+" - "+ fecha_final_gepro);
			return getDecimoCuartoSueldoPagadoLiquidacion(ide_geedp,fecha_inicial_gepro,fecha_final_gepro,4); 
		}
	
		
		// importa el dias acumulados sueldo pagado
		else if(ide_nrrub.equals(utilitario.getVariable("p_nrh_nro_dias_vacaciones"))){
			
			boolean ide_geame=false;			
			ide_geame=retornaAccionpersonalComisionServicios(ide_geedp);
			System.out.println("nro_dias_vacaciones "+ fecha_inicial_gepro+" - "+ fecha_final_gepro);
			return getDiasVacacionesAcumuladas(ide_geedp,fecha_inicial_gepro,fecha_final_gepro,4,ide_gttem,ide_geedp); 
		}

		// importa dias vacacines
		else if(ide_nrrub.equals(utilitario.getVariable("p_nrh_total_valor_vacaciones_liquidacion"))){
			boolean ide_geame=false;			
			ide_geame=retornaAccionpersonalComisionServicios(ide_geedp);
			if (ide_gttem.equals("2") && ide_geame==false ){
				System.out.println("valor a pagar por nro_dias_vacaciones acumulados "+ fecha_inicial_gepro+" - "+ fecha_final_gepro);
				return getDiasVacacionesAcumuladasValorCancelar(ide_geedp,fecha_inicial_gepro,fecha_final_gepro,4,ide_gttem,ide_geedp,dias_exceso_liquidacion,true,ide_geame); 
			}else if (ide_gttem.equals("1") && ide_geame==false){
				System.out.println("valor a pagar por nro_dias_vacaciones acumulados "+ fecha_inicial_gepro+" - "+ fecha_final_gepro);
				return getDiasVacacionesAcumuladasCT(ide_geedp,fecha_inicial_gepro,fecha_final_gepro,4,ide_gttem,ide_geedp,dias_exceso_liquidacion,false); 
			}else {
				return ""+0.0;
			}
 
					
		}else if(ide_nrrub.equals(utilitario.getVariable("p_valor_vacaciones_negativo"))){
			System.out.println("Ingresa a valor_vacaciones_negativo");
			if (vacacionesNegativoValor<0.0 || vacacionesNegativoValor<0) {
				double valor=0.0;
				valor=vacacionesNegativoValor*(-1);
				return ""+valor;
			}else {
				return "0";
			}
			
	
		}else if(ide_nrrub.equals(utilitario.getVariable("p_nrh_total_valor_vacaciones_liquidacionCT"))){
			System.out.println("valor a pagar por nro_dias_vacaciones acumulados "+ fecha_inicial_gepro+" - "+ fecha_final_gepro);
			return "0"; 
		

}
		///para el caso de termincación laboral por desahucio
		else if (ide_nrrub.equalsIgnoreCase(utilitario.getVariable("p_valor_desahucio"))){

 		    String valor="";
			boolean dias_exceso=false;
			//Obtengo el ide de la tabla detalle gen_detalle_empleado_departame 
			TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp,ide_geded  from gen_empleados_departamento_par where ide_geedp="+ide_geedp);
			
			//Busco el motivo de la accion sea DESAHUCIO
			TablaGenerica tab_desahucio=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
					+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
					+ "from gen_empleados_departamento_par epar "
					+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
					+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
					+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
					+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
					+ "where epar.ide_geded="+tab_emp.getValor("ide_geded"));

			
			
			if (tab_desahucio.getTotalFilas()>0 && tab_desahucio.getValor("ide_geame").equals("60")) {
				// SI CONTIENE DESAHUCIO 
				
				
			String fecha_iniTemp="";
			//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
			String a="";
			TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
					+ "order by ide_geedp asc");
			String ide_geedp_empleado="";
			//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
			//variable guarda la fecha de salida
			String fecha_finTemp="";
			fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
			//variable cuenta el numero de acciones
			int contAccionInvalida=0,contForAccionInvalida=0;
			StringBuilder str_ide_geedp = new StringBuilder();

			for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
				if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(ide_geedp)==true) {
					String valorUltimoElemento="";
					valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
					if(valorUltimoElemento.equals(",")==true){
						str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
					}else {
					}
				i=tabEmpleados.getTotalFilas();
				}else{
						//Voy anidando los ides de la accion
						str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
		               // valor++;
		                if (tabEmpleados.getTotalFilas()==1) {
		   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
		   				contForAccionInvalida++;
		   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
		   					str_ide_geedp.append(",");
		                   // System.out.println("str_ide:  "+str_ide_empleado);
		   					}
		    			}	 	
				}
			}
			
			
			//Si no contiene acciones de personal luego de la liquidacion
			if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
				str_ide_geedp.append(ide_geedp);
			}

			
			//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
			
			a=utilitario.getVariable("p_nrh_acciones_personal");
			String[] listaAccionesEmpleado;
			listaAccionesEmpleado=a.split(",");
			for (int i = 0; i < listaAccionesEmpleado.length; i++) {
				fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
				if (!fecha_iniTemp.equals("")) {
					i=listaAccionesEmpleado.length;
				}
			}

			//Calculo de años trabajados por el empleado
			   int diferenciaFechas=0,anios=0; 
			   diferenciaFechas=getDias360(fecha_iniTemp,fecha_finTemp);					
			   double valor1=0.0,valorAnios=0.0;
			   valor1=diferenciaFechas/360;
			   anios= (int) valor1;
			   
			   
			
				//Validacion de ajuste dias exceso
				dias_exceso=false;
				int valorBandera=0;
				dias_exceso=diasExcesoLiquidacion(ide_geedp, ide_gepro);
				if (dias_exceso==false){
					valorBandera=0;
				}
				else if (dias_exceso==true){
					valorBandera=1;
				}
			   
			   //variable dias de exceso
			   
				
				 TablaGenerica tabAccionesEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_geedp not in("+str_ide_geedp.toString()+")  "
							+ "and ide_gtemp in("+tab_emp.getValor("ide_gtemp")+")");
					int cont=0;
					StringBuilder str = new StringBuilder();
					for (int i = 0; i < tabAccionesEmpleados.getTotalFilas(); i++) {
					  		//Voy anidando los ides de la accion
					          	    str.append(tabAccionesEmpleados.getValor(i, "IDE_GEEDP"));
					               // valor++;
					                if (tabAccionesEmpleados.getTotalFilas()==1) {
					   			}else if (cont<=tabAccionesEmpleados.getTotalFilas()) {
					   					cont++;
					   					if(cont<(tabAccionesEmpleados.getTotalFilas())){
					                    str.append(",");
					                   // System.out.println("str_ide:  "+str_ide_empleado);
					   					}
					    			}
					}



					int mes=utilitario.getMes(fecha_finTemp);
					int anioTemp=utilitario.getAnio(fecha_finTemp);
					if (mes==1) {
						mes=12;
						anioTemp=anioTemp-1;
					}else {
						mes=mes-1;
					}
					
					
					
					TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+anioTemp+"%' ");
					String anio=tabAnio.getValor("IDE_GEANI");
					//TablaGenerica rol=utilitario.consultar("select * from gen_perido_rol where ide_gepro="+ide_gepro);
					//String ide_mes="",ide_geani="";
					//ide_mes=rol.getValor("IDE_GEMES");
					//ide_geani=rol.getValor("IDE_GEANI");
					TablaGenerica tab_detalle_rol=null;
					int fondo=0;

				
				
				
				
				
			   if (ide_gttem.equals("1")){
			   // Si es el caso de un empleado de codigo de trabajo debo seleccionar la base imponible
					TablaGenerica tabLiquidacion=utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
							+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
							+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
							+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
							+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
							+ "where DRO.IDE_GEEDP in ("+str_ide_geedp.toString()+") "
							+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+mes+" "
							+ "and RUB.ide_nrrub in("+utilitario.getVariable("p_valor_base_imponible")+") "
							+ "AND DER.IDE_NRDTN IN(4,2) "
							+ "order by RUB.ide_nrrub asc");
					boolean ejecutoRol=false;
					if (tabLiquidacion.getTotalFilas()>0) {
						double valorTemp=0.0,valorTotal=0.0;
						valorTemp= Double.parseDouble(utilitario.getFormatoNumero((Double.parseDouble(tabLiquidacion.getValor("VALOR_NRDRO"))*anios),2));
					    valorTotal=Double.parseDouble(utilitario.getFormatoNumero((valorTemp*0.25),2)); 
						return ""+valorTotal;
					}else {
						return ""+0.0;
					}
			
					
					
			   }//Si tiene liquidacion por desahucio
			   /*else{
				// Si es el caso de un empleado de codigo de trabajo debo seleccionar la base imponible
					TablaGenerica tabLiquidacion=utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
							+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
							+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
							+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
							+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
							+ "where DRO.IDE_GEEDP in ("+str.toString()+") "
							+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+mes+" "
							+ "and RUB.ide_nrrub in("+utilitario.getVariable("p_valor_rmu")+") "
							+ "AND DER.IDE_NRDTN IN(4,2) "
							+ "order by RUB.ide_nrrub asc");
					boolean ejecutoRol=false;
					if (tabLiquidacion.getTotalFilas()>0) {
					     return tabLiquidacion.getValor("VALOR_NRDRO");
					}else {
						return ""+0.0;
					}
				}*/	
					
					
					
		}else {
			return ""+0.0;
		}
	

			   
			   
			   
			   
			   
	}
		
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////AJUSTE CODIGO DE TRABAJO/////////////////////////////////////////////////////////////////////////////////
		//	Dias A vacaciones pendientes a liquidar CT
		else if(ide_nrrub.equals(utilitario.getVariable("p_valor_dias_pendientes_liquidar_ct"))){
			System.out.println("p_valor_dias_pendientes_liquidar_ct");
			return 	getDatosCodigoTrabajo(ide_geedp,fecha_inicial_gepro,fecha_final_gepro,4,ide_gttem,ide_geedp,2);
		}

		//	Dias laborados base imponible pendientes a liquidar CT
		else if(ide_nrrub.equals(utilitario.getVariable("p_valor_dias_laborados_liquidar_ct"))){
			System.out.println("p_valor_dias_laborados_liquidar_ct");
			return 	getDatosCodigoTrabajo(ide_geedp,fecha_inicial_gepro,fecha_final_gepro,4,ide_gttem,ide_geedp,1);
		}

		
	
		
		
		//	Base Imponible generada para codigo de trabajo
		else if(ide_nrrub.equals(utilitario.getVariable("p_valor_variable_total_base_imponible_liquidacion"))){
			System.out.println("getDecimoBaseImponibleGenerada "+ fecha_inicial_gepro+" - "+ fecha_final_gepro);
			return getBaseImponibleGenerada(ide_geedp,fecha_inicial_gepro,fecha_final_gepro,11); 
		}


		else if(ide_nrrub.equals(utilitario.getVariable("p_valor_decimoc_sin_rol"))){
			//Rubro utilizado para calcular el ajuste decimo tercero si acumula
			
			if (acumula_decimo.equals("true")) {
			//Si acumula decimo cuarto
			String valor="";
			boolean dias_exceso=false;
			TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+ide_geedp);
			String fecha_iniTemp="";
			//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
			String a="";
			TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
					+ "order by ide_geedp asc");
			String ide_geedp_empleado="";
			//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
			//variable guarda la fecha de salida
			String fecha_finTemp="";
			fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
			//variable cuenta el numero de acciones
			int contAccionInvalida=0,contForAccionInvalida=0;
			StringBuilder str_ide_geedp = new StringBuilder();

			for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
				if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(ide_geedp)==true) {
					String valorUltimoElemento="";
					valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
					if(valorUltimoElemento.equals(",")==true){
						str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
					}else {
					}
				i=tabEmpleados.getTotalFilas();
				}else{
						//Voy anidando los ides de la accion
						str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
		               // valor++;
		                if (tabEmpleados.getTotalFilas()==1) {
		   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
		   				contForAccionInvalida++;
		   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
		   					str_ide_geedp.append(",");
		                   // System.out.println("str_ide:  "+str_ide_empleado);
		   					}
		    			}	 	
				}
			}
			
			
			//Si no contiene acciones de personal luego de la liquidacion
			if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
				str_ide_geedp.append(ide_geedp);
			}

			
			//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
			
			a=utilitario.getVariable("p_nrh_acciones_personal");
			String[] listaAccionesEmpleado;
			listaAccionesEmpleado=a.split(",");
			for (int i = 0; i < listaAccionesEmpleado.length; i++) {
				fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
				if (!fecha_iniTemp.equals("")) {
					i=listaAccionesEmpleado.length;
				}
			}
			

			
			String fecha_comaracion=utilitario.getAnio(fecha_finTemp)+"-01-01";
			String fecha_ini="",fecha_fin="";
			System.out.println("DECIMOS AJUSTE SIN ROL");
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			String fecha_comparacionCuarto=(utilitario.getAnio(fecha_finTemp)-1)+"-08-01";
			String fecha_comparacionCuartoRango=utilitario.getAnio(fecha_finTemp)+"-08"+"-01";
		
				System.out.println("DECIMO CUARTO");
				int mesTemp=0;
				boolean bandDecimoCuarto=false;			
			if (fecha_iniTemp.compareTo(fecha_comparacionCuarto)>=0) {
				//Si la fecha inicio de contrato es mayor a la del decimo cuarto
				if (fecha_iniTemp.compareTo(fecha_comparacionCuarto)>=0 && fecha_iniTemp.compareTo(fecha_comparacionCuartoRango)<=0) {
					//variable estado 
					bandDecimoCuarto=true;	
				}else {
					bandDecimoCuarto=false;
				}
			}else{
				//Si la fecha no cumple con los parametros
				bandDecimoCuarto=false;}
			
				
				if (bandDecimoCuarto==false) {
					//Si no cumple retorna 0.0
					return ""+0.0;
				}else {	
			TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_iniTemp)+"%' ");
			String anio=tabAnio.getValor("IDE_GEANI");
			TablaGenerica tab_detalle_rol=null;
			 tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
						+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
						+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
						+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
						+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
						+ "where DRO.IDE_GEEDP in ( "
						+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
						+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+ide_geedp+")) and ide_gttem !=3  "
						+ ")  "
						+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+(utilitario.getMes(fecha_iniTemp))+" "
						+ "AND DER.IDE_NRDTN IN(4,2)");
				
			 fecha_fin=utilitario.getUltimaFechaMes(fecha_iniTemp);
			    int diferenciaFechas=0;
				int mesRol=0,anioRol=0,acum=0;
			 if (tab_detalle_rol.getTotalFilas()>0) {
				 //Si contiene rol en el mes
				return ""+0.0;
			}else {
			//Si no contiene rol generado
			    if (utilitario.getDia(fecha_fin)==31) {
					acum=0;
				}
				else if (utilitario.getDia(fecha_fin)==29) {
					acum=1;
				} else if (utilitario.getDia(fecha_fin)==28) {
					acum=2;
				}else {
					acum=0;
				}
			    		
			    if (utilitario.getMes(fecha_iniTemp)==utilitario.getMes(fecha_finTemp)) {
					   diferenciaFechas=getDias360(fecha_iniTemp,fecha_finTemp)+acum;					
				}else {
					 diferenciaFechas=getDias360(fecha_iniTemp,fecha_fin)+acum;
				}

//			   diferenciaFechas=getDias360(fecha_iniTemp,fecha_fin)+acum;
			   //Variable almacena el valor de remuneracion
			   
			   int sumaMes=0,sumaAnio=0,anioRolAnterior;
			   String anioTemp="";
			   if (utilitario.getMes(fecha_iniTemp)==12) {
				sumaMes=-11;
				TablaGenerica tabAnioTemp=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+(utilitario.getAnio(fecha_iniTemp)+1)+"%' ");
				anioTemp=tabAnioTemp.getValor("IDE_GEANI");
				anioRolAnterior=Integer.parseInt(tabAnioTemp.getValor("IDE_GEANI"));
				
				}else {
					TablaGenerica tabAnioTemp=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+(utilitario.getAnio(fecha_iniTemp))+"%' ");
					anioTemp=tabAnioTemp.getValor("IDE_GEANI");
					sumaMes=1;
					anioRolAnterior=Integer.parseInt(tabAnioTemp.getValor("IDE_GEANI"));	
				}
			   
			   TablaGenerica tab_detalle_rol_remuneracion = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
						+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
						+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
						+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
						+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
						+ "where DRO.IDE_GEEDP in ( "
						+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
						+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+ide_geedp+")) and ide_gttem !=3  "
						+ ")  "
						+ "and pro.ide_geani="+anioTemp+" and pro.ide_gemes="+(utilitario.getMes(fecha_iniTemp)+sumaMes)+" "
						+ "AND RUB.IDE_NRRUB IN("+utilitario.getVariable("p_nrh_rubro_salario_basico_unificado_vig")+") "
						+ "AND DER.IDE_NRDTN IN(4,2)");
			   
			   double sueldo=0.0;
				 if (tab_detalle_rol_remuneracion.getTotalFilas()>0) {
					 if (utilitario.getAnio(fecha_iniTemp)==(utilitario.getAnio(fecha_finTemp)-1)) {
						 TablaGenerica tab_sueldo=utilitario.consultar("SELECT ide_nrder, ide_nrrub, ide_nrdtn, formula_nrder, "
						 		+ "imprime_nrder, ver_rol_nrder "
						 		+ "FROM nrh_detalle_rubro "
						 		+ "where ide_nrrub=276 "
						 		+ "and ide_nrdtn in(4)");
								 
						 
						sueldo=Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(tab_sueldo.getValor("formula_nrder")),2));
					}else {
						sueldo=Double.parseDouble(tab_detalle_rol_remuneracion.getValor("VALOR_NRDRO"));		
					}
					 
				}else {
					
					 TablaGenerica tab_sueldo=utilitario.consultar("SELECT ide_nrder, ide_nrrub, ide_nrdtn, formula_nrder, "
						 		+ "imprime_nrder, ver_rol_nrder "
						 		+ "FROM nrh_detalle_rubro "
						 		+ "where ide_nrrub=276 "
						 		+ "and ide_nrdtn in(4)");
						 sueldo=Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(tab_sueldo.getValor("formula_nrder")),2));
				}
			  
			   double valorFinal=0.0,valorFinalTempDecimales=0.0,valor_30_dias=0.0;
			   
			   valorFinalTempDecimales=Double.parseDouble(utilitario.getFormatoNumero(((sueldo/12)/30),2));
			   valorFinal=Double.parseDouble(utilitario.getFormatoNumero(valorFinalTempDecimales*diferenciaFechas));
			   //valorFinal=Double.parseDouble(utilitario.getFormatoNumero(((valor_30_dias)/30),2));
			   return ""+valorFinal;
		
			}  
		
			}  
			
		}else {//Si acumula decimo cuarto
			 return ""+0.0;}
		}
		//Dias perndientes a vacacion 
		
		else if(ide_nrrub.equals(utilitario.getVariable("p_valor_variable_total_base_imponible_liquidacion"))){
			
		///////////////////////////////H GBGGBBBBFF
			
		}
		
		
		  
		
		else if(ide_nrrub.equals(utilitario.getVariable("p_valor_decimot_sin_rol"))){
			//Rubro utilizado para calcular el ajuste decimo tercero si acumula
			
			if (acumula_decimo.equals("true")) {
			String valor="";
			boolean dias_exceso=false;


			TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+ide_geedp);
			String fecha_iniTemp="";
			//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
			String a="";

			
			
			
			
			
			TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
					+ "order by ide_geedp asc");
			String ide_geedp_empleado="";
			//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
			//variable guarda la fecha de salida
			String fecha_finTemp="";
			fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
			//variable cuenta el numero de acciones
			int contAccionInvalida=0,contForAccionInvalida=0;
			StringBuilder str_ide_geedp = new StringBuilder();

			for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
				if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(ide_geedp)==true) {
					String valorUltimoElemento="";
					valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
					if(valorUltimoElemento.equals(",")==true){
						str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
					}else {
					} 
				i=tabEmpleados.getTotalFilas();
				}else{
						//Voy anidando los ides de la accion
						str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
		               // valor++;
		                if (tabEmpleados.getTotalFilas()==1) {
		   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
		   				contForAccionInvalida++;
		   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
		   					str_ide_geedp.append(",");
		                   // System.out.println("str_ide:  "+str_ide_empleado);
		   					}
		    			}	 	
				}
			}
			
			
			//Si no contiene acciones de personal luego de la liquidacion
			if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
				str_ide_geedp.append(ide_geedp);
			}

			
			//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
			
			a=utilitario.getVariable("p_nrh_acciones_personal");
			String[] listaAccionesEmpleado;
			listaAccionesEmpleado=a.split(",");
			for (int i = 0; i < listaAccionesEmpleado.length; i++) {
				fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
				if (!fecha_iniTemp.equals("")) {
					i=listaAccionesEmpleado.length;
				}
			}
			
			
		
			String fecha_comparacionTercero=utilitario.getAnio(fecha_finTemp)+"-01-01";
				System.out.println("DECIMO TERCERO");
				int mesTemp=0;
				boolean bandDecimoTercero=false;			
			if (fecha_iniTemp.compareTo(fecha_comparacionTercero)>=0) {
				//Si la fecha inicio de contrato es mayor a la del decimo cuarto
				    bandDecimoTercero=true;	
				}else {
					bandDecimoTercero=false;
				}
			
			if (bandDecimoTercero==false) {
				return ""+0.0;
			}else {
		
			TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_iniTemp)+"%' ");
			String anio=tabAnio.getValor("IDE_GEANI");
			TablaGenerica tab_detalle_rol=null;
			 tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
						+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
						+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
						+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
						+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
						+ "where DRO.IDE_GEEDP in ( "
						+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
						+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+ide_geedp+")) and ide_gttem !=3  "
						+ ")  "
						+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+(utilitario.getMes(fecha_iniTemp))+" "
						+ "AND DER.IDE_NRDTN IN(4,2)");
				
			 String fecha_fin=utilitario.getUltimaFechaMes(fecha_iniTemp);
			    int diferenciaFechas=0;
				int mesRol=0,anioRol=0,acum=0;
			 if (tab_detalle_rol.getTotalFilas()>0) {
				return ""+0.0;
			}else {
			
			
			    if (utilitario.getDia(fecha_fin)==31) {
					acum=0;
				}
				else if (utilitario.getDia(fecha_fin)==29) {
					acum=1;
				} else if (utilitario.getDia(fecha_fin)==28) {
					acum=2;
				}else {
					acum=0;
				}
			    
			    
			}
			    if (utilitario.getMes(fecha_iniTemp)==utilitario.getMes(fecha_finTemp)) {
					   diferenciaFechas=getDias360(fecha_iniTemp,fecha_finTemp)+acum;					
				}else {
					 diferenciaFechas=getDias360(fecha_iniTemp,fecha_fin)+acum;
				}

			   int sumaMes=0,sumaAnio=0,anioRolAnterior;
			   if (utilitario.getMes(fecha_iniTemp)==12) {
				sumaMes=-11;
				TablaGenerica tabAnioTemp=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+(utilitario.getAnio(fecha_iniTemp)+1)+"%' ");
				String anioTemp=tabAnioTemp.getValor("IDE_GEANI");
				anioRolAnterior=Integer.parseInt(tabAnioTemp.getValor("IDE_GEANI"));
				
				}else {
					TablaGenerica tabAnioTemp=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+(utilitario.getAnio(fecha_iniTemp))+"%' ");
					String anioTemp=tabAnioTemp.getValor("IDE_GEANI");
					sumaMes=1;
					anioRolAnterior=Integer.parseInt(tabAnioTemp.getValor("IDE_GEANI"));	
				}
			   
				
				 TablaGenerica tab_detalle_rolTemp = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
							+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
							+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
							+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
							+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
							+ "where DRO.IDE_GEEDP in ( "
							+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
							+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+ide_geedp+")) and ide_gttem !=3  "
							+ ")  "
							+ "and pro.ide_geani="+anioRolAnterior+" and pro.ide_gemes="+((utilitario.getMes(fecha_iniTemp))+sumaMes)+" "
							+ "AND DER.IDE_NRDTN IN(4,2) "
							+ "AND RUB.IDE_NRRUB IN("+utilitario.getVariable("p_valor_rmu")+")");
				 double sueldo=0.0;
				 
				 if (tab_detalle_rolTemp.getTotalFilas()>0) {
					
					 sueldo= Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(tab_detalle_rolTemp.getValor("VALOR_NRDRO")),2));	
				}else {
					TablaGenerica tab_detalle_empleados =utilitario.consultar("select ide_geedp,rmu_geedp from gen_empleados_departamento_par "
							+ "where ide_geedp="+ide_geedp);
					sueldo=Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(tab_detalle_empleados.getValor("RMU_GEEDP")),2));
					sueldo=Double.parseDouble(utilitario.getFormatoNumero((sueldo/30),2))*diferenciaFechas;
				}
			  			 		  			  
						   double valorFinal=0.0,valorFinalTempDecimales=0.0;
						   valorFinalTempDecimales=Double.parseDouble(utilitario.getFormatoNumero(((sueldo/12)/30),2));
						   valorFinal=Double.parseDouble(utilitario.getFormatoNumero(((valorFinalTempDecimales)*diferenciaFechas),2));
						   return ""+valorFinal;
			}
			}
			else{
				return ""+0.0;
			}
			   
			   
		}
		//Dias perndientes a vacacion 
		
		else if(ide_nrrub.equals(utilitario.getVariable("p_valor_variable_total_base_imponible_liquidacion"))){
			
		///////////////////////////////H GBGGBBBBFF
			
		}
		
		
		
		
		 
		else if(ide_nrrub.equals(utilitario.getVariable("p_nrh_dias_exceso_liquidacion"))){
			System.out.println("p_nrh_dias_exceso_liquidacion");
			String valor="";
			System.out.println("valor a pagar por nro_dias_vacaciones acumulados "+ fecha_inicial_gepro+" - "+ fecha_final_gepro);
			//return getDiasVacacionesAcumuladas(ide_geedp,fecha_inicial_gepro,fecha_final_gepro,4);
			TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+ide_geedp);
			String fecha_iniTemp="";
			//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
			String a="";
			int mesLiquidacion=0;
			TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
					+ "order by ide_geedp asc");
			String ide_geedp_empleado="";
			//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
			//variable guarda la fecha de salida
			String fecha_finTemp="";
			fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
			//variable cuenta el numero de acciones
			int contAccionInvalida=0,contForAccionInvalida=0;
			StringBuilder str_ide_geedp = new StringBuilder();

			for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
				if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(ide_geedp)==true) {
					String valorUltimoElemento="";
					valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
					if(valorUltimoElemento.equals(",")==true){
						str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
					}else {
					}
				i=tabEmpleados.getTotalFilas();
				}else{
						//Voy anidando los ides de la accion
						str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
		               // valor++;
		                if (tabEmpleados.getTotalFilas()==1) {
		   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
		   				contForAccionInvalida++;
		   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
		   					str_ide_geedp.append(",");
		                   // System.out.println("str_ide:  "+str_ide_empleado);
		   					}
		    			}	 	
				}
			}
			
			
			//Si no contiene acciones de personal luego de la liquidacion
			if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
				str_ide_geedp.append(ide_geedp);
			}

			
			//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
			
			a=utilitario.getVariable("p_nrh_acciones_personal");
			String[] listaAccionesEmpleado;
			listaAccionesEmpleado=a.split(",");
			for (int i = 0; i < listaAccionesEmpleado.length; i++) {
				fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
				if (!fecha_iniTemp.equals("")) {
					i=listaAccionesEmpleado.length;
				}
			}
			
			
			ide_geedp_empleado=ide_geedp;
			mesLiquidacion=utilitario.getMes(fecha_finTemp);
			System.out.println("fecha_finTemp"+fecha_finTemp);
			String fecha_fin_validacion="";
		/*	if (utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp))>=31) {
				fecha_finTemp=utilitario.getAnio(fecha_finTemp)+"-"+mesLiquidacion+"-31";
				fecha_fin_validacion=utilitario.getAnio(fecha_finTemp)+"-"+mesLiquidacion+"-31";
			}else if(utilitario.getMes(fecha_finTemp)==2 && (utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp))==28)){
				fecha_finTemp=utilitario.getAnio(fecha_finTemp)+"-02"+"-28";
				fecha_fin_validacion=utilitario.getAnio(fecha_finTemp)+"-02"+"-28";

		}else 	if (utilitario.getMes(fecha_finTemp)==2 && (utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp))==29)){	
			fecha_finTemp=utilitario.getAnio(fecha_finTemp)+"-02"+"-29";
			fecha_fin_validacion=utilitario.getAnio(fecha_finTemp)+"-02"+"-29";

			}else {*/
				fecha_fin_validacion=utilitario.getAnio(fecha_finTemp)+"-"+mesLiquidacion+"-"+utilitario.getDia(fecha_finTemp);
			//}
				
			System.out.println("fecha_finTemp"+fecha_fin_validacion);
			
			
			TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_finTemp)+"%' ");
			
			
			TablaGenerica tab_liquidacion = utilitario.consultar("SELECT ide_gepro, ide_nrtit, ide_gemes, ide_geani, fecha_inicial_gepro,  "
					+ "fecha_final_gepro, detalle_periodo_gepro,  "
					+ "activo_gepro,gen_ide_gepro  "
					+ "FROM gen_perido_rol "
					+ "where ide_geani="+tabAnio.getValor("IDE_GEANI")+" and ide_gemes="+mesLiquidacion
					+ " order by ide_gepro desc ");


			TablaGenerica tab_periodo_rol = utilitario.consultar("select * from nrh_rol where ide_gepro="+tab_liquidacion.getValor("IDE_GEPRO")+" "
					+ "and ide_nrdtn IN(2,4) AND ide_nresr=1 "
					+ " order by ide_nrrol asc");
			if (tab_periodo_rol.getTotalFilas()==0) {
				valor=""+0;
			}else {
				System.out.println("INGRESO A EXCESO");
			int cont=0;
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < tab_periodo_rol.getTotalFilas(); i++) {
			  
			  		//Voy anidando los ides de la accion
			          	    str.append(tab_periodo_rol.getValor(i, "IDE_NRROL"));
			               // valor++;
			                if (tab_periodo_rol.getTotalFilas()==1) {
			   			}else if (cont<=tab_periodo_rol.getTotalFilas()) {
			   					cont++;
			   					if(cont<(tab_periodo_rol.getTotalFilas())){
			                    str.append(",");
			                   // System.out.println("str_ide:  "+str_ide_empleado);
			   					}
			    			}
			   			 		         		
			}
			
			
			TablaGenerica tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
					+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
					+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
					+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
					+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
					+ "where DRO.IDE_GEEDP in ( "
					+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
					+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp+") and ide_gttem !=3  "
					+ ")  "
					+ "and rol.ide_nrrol IN ("+str.toString()+")");  
			
			/*TablaGenerica tab_detalle_rol = utilitario.consultar("select * from nrh_detalle_rol where ide_nrrol in("+str.toString()+") "
					+ "and ide_geedp in("+ide_geedp_empleado+")"); */ 
		    double valorTemp=0;
		 
		    if (tab_detalle_rol.getTotalFilas()==0) {
		    	valor=""+0;
			}else {
				valorTemp=(utilitario.getDiferenciaFechas360(utilitario.getFecha(fecha_finTemp), utilitario.getFecha(fecha_fin_validacion))-1);
				if (utilitario.getMes(fecha_finTemp)==2 && (utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp))==28)) {
					valorTemp=valorTemp-2;
				}else if (utilitario.getMes(fecha_finTemp)==2 && (utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp))==29)) {
					valorTemp=valorTemp-1;
				}
				 if (valorTemp<=0.0 || valorTemp<=0) {
					valor=""+0;
				}else {
					int x = (int) valorTemp;
					//String valorNuevo=""+valorTemp;
					valor=""+x;
				}
					
					

			}
		    
		 /*   valorTemp=utilitario.getDiferenciaFechas360(utilitario.getFecha(fecha_finTemp), utilitario.getFecha(fecha_fin_validacion))-1;
			if (valorTemp<=0.0) {
				valor=""+0.0;
			}else {
			}
			*/
			
			}
		    
		  System.out.println("dias exceso"+valor);
			return valor; 	
					
		}

	//	else if(ide_nrrub.equals(utilitario.getVariable("p_nrh_total_valor_vacaciones_liquidacion"))){
		//	System.out.println("valor a pagar por nro_dias_vacaciones acumulados "+ fecha_inicial_gepro+" - "+ fecha_final_gepro);
			
			//Obtengo valor total de pago r concepto de vacaciones para los siguientes casos:
			//1. Se le pago el sueldo del mes al empleado 30 dias pero se le liquida el 25 se crea el ajuste en la base imponible al generar liquidacion
			//2.-Se le pago el sueldo del mes al empleado 30 dias y se le liquida el 30 no se crea el ajuste en la base imponible al generar liquidacion 
			//3.- Todos los valores son generados como una nomina 
			
			//return getValorPeriodoVacacionesCodigoTrabajo(ide_geedp,ide_gepro); 
			
			
	
			
		
			
			
			
			
		//	p_nrh_rubro_iess_personal_ajuste,p_nrh_rubro_iess_patronal_ajuste,p_nrh_rubro_iece_ajuste			
	//	}
	else if (ide_nrrub.equals(utilitario.getVariable("p_nrh_rubro_iess_personal_ajuste"))) {
	
/////////////////////////////////////////////////////////////////AJUSTE IESS PERSONAL /////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
		
		
		boolean dias_exceso=false;
		int valorBandera=0;
		dias_exceso=diasExcesoLiquidacion(ide_geedp, ide_gepro);
		if (dias_exceso==false){
			valorBandera=0;
		}
		else if (dias_exceso==true){
			valorBandera=1;
		}

		
		

		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7		
			//AJUSTE IESS PERSONAL
			String valor="";

			
			if (dias_exceso==true) {
				
		
			
				TablaGenerica tabEmpleadosDepartamento=utilitario.consultar("select * from gen_empleados_departamento_par where ide_geedp in("+ide_geedp+")");
				TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tabEmpleadosDepartamento.getValor("ide_gtemp")+") "
						+ "order by ide_geedp desc");
				String ide_geedp_empleado="";
				String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
				TablaGenerica tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
						+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
						+ "from gen_empleados_departamento_par epar "
						+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
						+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
						+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
						+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
						+ "where epar.ide_gtemp="+tabEmpleadosDepartamento.getValor("ide_gtemp")+" and gaed.ide_geaed in(3) "
						+ "order by epar.ide_geedp desc");
				
				TablaGenerica tabAccionesEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_geedp not in("+tab_empleado_departamento_par.getValor("ide_geedp")+")  "
						+ "and ide_gtemp in("+tabEmpleadosDepartamento.getValor("ide_gtemp")+")");

				int cont=0;
				StringBuilder str = new StringBuilder();
				for (int i = 0; i < tabAccionesEmpleados.getTotalFilas(); i++) {
				  
				  		//Voy anidando los ides de la accion
				          	    str.append(tabAccionesEmpleados.getValor(i, "IDE_GEEDP"));
				               // valor++;
				                if (tabAccionesEmpleados.getTotalFilas()==1) {
				   			}else if (cont<=tabAccionesEmpleados.getTotalFilas()) {
				   					cont++;
				   					if(cont<(tabAccionesEmpleados.getTotalFilas())){
				                    str.append(",");
				                   // System.out.println("str_ide:  "+str_ide_empleado);
				   					}
				    			}
				   			 		         		
				}
				int mes=utilitario.getMes(fecha_finTemp);
				TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_finTemp)+"%' ");
				String anio=tabAnio.getValor("IDE_GEANI");
				//TablaGenerica rol=utilitario.consultar("select * from gen_perido_rol where ide_gepro="+ide_gepro);
				//String ide_mes="",ide_geani="";
				//ide_mes=rol.getValor("IDE_GEMES");
				//ide_geani=rol.getValor("IDE_GEANI");
				TablaGenerica tab_detalle_rol=null;
			
				
				
				tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
						+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
						+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
						+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
						+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
						+ "where DRO.IDE_GEEDP in ( "
						+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
						+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str.toString()+")) and ide_gttem !=3  "
						+ ")  "
						+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+mes);
				if (tab_detalle_rol.getTotalFilas()>0) {
					
				}else {
				 tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
							+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
							+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
							+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
							+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
							+ "where DRO.IDE_GEEDP in ( "
							+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
							+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str.toString()+")) and ide_gttem !=3  "
							+ ")  "
							+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+(mes-1));
						
				}
				//p_nrh_rubro_iess_personal,p_nrh_rubro_iess_patronal,p_nrh_rubro_iess_iece
				TablaGenerica valor_base_mes_anterior=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
						+" where a.ide_nrrol=b.ide_nrrol and ide_gepro="+tab_detalle_rol.getValor("ide_gepro")+" and ide_geedp = "+tab_detalle_rol.getValor("IDE_GEEDP")+" and  "
								+ "ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_nrh_rubro_iess_personal")+")");
				 
				if(valor_base_mes_anterior.getTotalFilas()>0){
					valor=valor_base_mes_anterior.getValor("valor_nrdro");
					//System.out.println("valor vbase "+base_imponible_mes_anterior);
					return valor;
				}
				else {
					valor="0.0";
					return valor;
				}
			}else {
				valor="0.0";
				return valor;
			}
				
		
		
		//	p_nrh_rubro_iess_personal_ajuste,p_nrh_rubro_iess_patronal_ajuste,p_nrh_rubro_iece_ajuste			
		}else if (ide_nrrub.equals(utilitario.getVariable("p_nrh_rubro_iess_patronal_ajuste"))) {
			//AJUSTE IESS PATRONAL

			
			boolean dias_exceso=false;
			int valorBandera=0;
			dias_exceso=diasExcesoLiquidacion(ide_geedp, ide_gepro);
			
			if (dias_exceso==false){
				valorBandera=0;
			}
			else if (dias_exceso==true){
				valorBandera=1;
			}

			
			

			
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7		
				//AJUSTE IESS PERSONAL
				String valor="";

				
				if (dias_exceso==true) {
			//ARREGLO  DIA - MES- ANIO DE LA FECHA DE 
					
					TablaGenerica tabEmpleadosDepartamento=utilitario.consultar("select * from gen_empleados_departamento_par where ide_geedp in("+ide_geedp+")");
				TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tabEmpleadosDepartamento.getValor("ide_gtemp")+") "
						+ "order by ide_geedp desc");
				String ide_geedp_empleado="";
				String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
				TablaGenerica tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
						+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
						+ "from gen_empleados_departamento_par epar "
						+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
						+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
						+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
						+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
						+ "where epar.ide_gtemp="+tabEmpleadosDepartamento.getValor("ide_gtemp")+" and gaed.ide_geaed in(3) "
						+ "order by epar.ide_geedp desc");
				
				TablaGenerica tabAccionesEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_geedp not in("+tab_empleado_departamento_par.getValor("ide_geedp")+")  "
						+ "and ide_gtemp in("+tabEmpleadosDepartamento.getValor("ide_gtemp")+")");

				int cont=0;
				StringBuilder str = new StringBuilder();
				for (int i = 0; i < tabAccionesEmpleados.getTotalFilas(); i++) {
				  
				  		//Voy anidando los ides de la accion
				          	    str.append(tabAccionesEmpleados.getValor(i, "IDE_GEEDP"));
				               // valor++;
				                if (tabAccionesEmpleados.getTotalFilas()==1) {
				   			}else if (cont<=tabAccionesEmpleados.getTotalFilas()) {
				   					cont++;
				   					if(cont<(tabAccionesEmpleados.getTotalFilas())){
				                    str.append(",");
				                   // System.out.println("str_ide:  "+str_ide_empleado);
				   					}
				    			}
				   			 		         		
				}
				int mes=utilitario.getMes(fecha_finTemp);
				TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_finTemp)+"%' ");
				String anio=tabAnio.getValor("IDE_GEANI");
				//TablaGenerica rol=utilitario.consultar("select * from gen_perido_rol where ide_gepro="+ide_gepro);
				//String ide_mes="",ide_geani="";
				//ide_mes=rol.getValor("IDE_GEMES");
				//ide_geani=rol.getValor("IDE_GEANI");
				TablaGenerica tab_detalle_rol=null;
			
				tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
						+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
						+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
						+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
						+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
						+ "where DRO.IDE_GEEDP in ( "
						+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
						+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str.toString()+")) and ide_gttem !=3  "
						+ ")  "
						+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+mes);
				if (tab_detalle_rol.getTotalFilas()>0) {
					
				}else {
				 tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
							+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
							+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
							+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
							+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
							+ "where DRO.IDE_GEEDP in ( "
							+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
							+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str.toString()+")) and ide_gttem !=3  "
							+ ")  "
							+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+(mes-1));
						
				}
				//p_nrh_rubro_iess_personal,p_nrh_rubro_iess_patronal,p_nrh_rubro_iess_iece
				TablaGenerica valor_base_mes_anterior=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
						+" where a.ide_nrrol=b.ide_nrrol and ide_gepro="+tab_detalle_rol.getValor("ide_gepro")+" and ide_geedp = "+tab_detalle_rol.getValor("IDE_GEEDP")+" and  "
								+ "ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_nrh_rubro_iess_patronal")+")");
				 
				if(valor_base_mes_anterior.getTotalFilas()>0){
					valor=valor_base_mes_anterior.getValor("valor_nrdro");
					//System.out.println("valor vbase "+base_imponible_mes_anterior);
					return valor;
				}
				else {
					valor="0.0";
					return valor;
				}
				}else {
					valor="0.0";
					return valor;
				}
		
		
		
		//	p_nrh_rubro_iess_personal_ajuste,p_nrh_rubro_iess_patronal_ajuste,p_nrh_rubro_iece_ajuste			
	}else if (ide_nrrub.equals(utilitario.getVariable("p_nrh_rubro_iece_ajuste"))) {
		//AJUSTE IECE
		
		
		boolean dias_exceso=false;
		int valorBandera=0;
		dias_exceso=diasExcesoLiquidacion(ide_geedp, ide_gepro);
		if (dias_exceso==false){
			valorBandera=0;
		}
		else if (dias_exceso==true){
			valorBandera=1;
		}

		
		

		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7		
			//AJUSTE IESS PERSONAL
			String valor="";

			
			if (dias_exceso==true) {

		
			TablaGenerica tabEmpleadosDepartamento=utilitario.consultar("select * from gen_empleados_departamento_par where ide_geedp in("+ide_geedp+")");
			TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tabEmpleadosDepartamento.getValor("ide_gtemp")+") "
					+ "order by ide_geedp desc");
			String ide_geedp_empleado="";
			String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
			TablaGenerica tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
					+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
					+ "from gen_empleados_departamento_par epar "
					+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
					+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
					+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
					+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
					+ "where epar.ide_gtemp="+tabEmpleadosDepartamento.getValor("ide_gtemp")+" and gaed.ide_geaed in(3) "
					+ "order by epar.ide_geedp desc");
			
			TablaGenerica tabAccionesEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_geedp not in("+tab_empleado_departamento_par.getValor("ide_geedp")+")  "
					+ "and ide_gtemp in("+tabEmpleadosDepartamento.getValor("ide_gtemp")+")");

			int cont=0;
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < tabAccionesEmpleados.getTotalFilas(); i++) {
			  
			  		//Voy anidando los ides de la accion
			          	    str.append(tabAccionesEmpleados.getValor(i, "IDE_GEEDP"));
			               // valor++;
			                if (tabAccionesEmpleados.getTotalFilas()==1) {
			   			}else if (cont<=tabAccionesEmpleados.getTotalFilas()) {
			   					cont++;
			   					if(cont<(tabAccionesEmpleados.getTotalFilas())){
			                    str.append(",");
			                   // System.out.println("str_ide:  "+str_ide_empleado);
			   					}
			    			}
			   			 		         		
			}
			int mes=utilitario.getMes(fecha_finTemp);
			TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_finTemp)+"%' ");
			String anio=tabAnio.getValor("IDE_GEANI");
			//TablaGenerica rol=utilitario.consultar("select * from gen_perido_rol where ide_gepro="+ide_gepro);
			//String ide_mes="",ide_geani="";
			//ide_mes=rol.getValor("IDE_GEMES");
			//ide_geani=rol.getValor("IDE_GEANI");
			TablaGenerica tab_detalle_rol=null;
		
			
			
			tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
					+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
					+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
					+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
					+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
					+ "where DRO.IDE_GEEDP in ( "
					+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
					+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str.toString()+")) and ide_gttem !=3  "
					+ ")  "
					+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+mes);
			if (tab_detalle_rol.getTotalFilas()>0) {
				
			}else {
			 tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
						+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
						+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
						+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
						+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
						+ "where DRO.IDE_GEEDP in ( "
						+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
						+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP in("+str.toString()+")) and ide_gttem !=3  "
						+ ")  "
						+ "and pro.ide_geani="+anio+" and pro.ide_gemes="+(mes-1));
					
			}
			//p_nrh_rubro_iess_personal,p_nrh_rubro_iess_patronal,p_nrh_rubro_iess_iece
			TablaGenerica valor_base_mes_anterior=utilitario.consultar("select a.ide_nrrol,a.ide_gepro,valor_nrdro from nrh_rol a, nrh_detalle_rol b"
					+" where a.ide_nrrol=b.ide_nrrol and ide_gepro="+tab_detalle_rol.getValor("ide_gepro")+" and ide_geedp = "+tab_detalle_rol.getValor("IDE_GEEDP")+" and  "
							+ "ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub="+utilitario.getVariable("p_nrh_rubro_iece")+")");
			 
			if(valor_base_mes_anterior.getTotalFilas()>0){
				valor=valor_base_mes_anterior.getValor("valor_nrdro");
				//System.out.println("valor vbase "+base_imponible_mes_anterior);
				return valor;
			}
			else {
				valor="0.0";
				return valor;
			}
			}else {
				valor="0.0";
				return valor;
			}
			
			
	}
		
		// importa los dias fondos reserva
		else if(ide_nrrub.equals(P_NRH_RUBRO_DIAS_FONDOS_RESERVA)){
			int int_dias_laborados=0;
			if (fecha_contrato!=null && !fecha_contrato.isEmpty()){
				int int_mes_rol=utilitario.getMes(fecha_final_gepro);
				int int_mes_contrato=utilitario.getMes(fecha_contrato);
				
				if (pckUtilidades.CConversion.CDbl_2(dias_antiguedad)>360 && pckUtilidades.CConversion.CDbl_2(dias_antiguedad)<=395
						&& int_mes_rol==int_mes_contrato ){
					String fecha_ini_calculo=utilitario.getAnio(fecha_final_gepro)+"-"+utilitario.getMes(fecha_contrato)+"-"+utilitario.getDia(fecha_contrato);
					String fecha_fin_calculo;
					//if (utilitario.getDia(fecha_final_gepro)>30){
					fecha_fin_calculo=utilitario.getAnio(fecha_final_gepro)+"-"+utilitario.getMes(fecha_final_gepro)+"-30";
					//}else{
					//	fecha_fin_calculo=fecha_final_gepro;
					//}

					int_dias_laborados=(utilitario.getDiferenciasDeFechas(utilitario.getFecha(fecha_ini_calculo), utilitario.getFecha(fecha_fin_calculo)))+1;

					if (int_dias_laborados>30){
						int_dias_laborados=int_dias_laborados-(int_dias_laborados-30);
					}

				}else{
					try {
						int_dias_laborados=NRO_DIAS_COMERCIAL_NRTIT;
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			return int_dias_laborados+"";
		}

		// importar  dias periodo nomina (mesual , quincenal o semestral)
		else if(ide_nrrub.equals(P_NRH_RUBRO_DIAS_PERIODO)){	 
			return NRO_DIAS_COMERCIAL_NRTIT+"";
		}
		return null;
	}


	public String getFormatoFechaCalculoRubros(String str_fecha_nrder,int anio){

		String fecha="";
		if (str_fecha_nrder!=null && !str_fecha_nrder.isEmpty()){
			if (str_fecha_nrder.indexOf("/")!=-1){
				String num_anios=str_fecha_nrder.substring(str_fecha_nrder.indexOf("/")+1, str_fecha_nrder.length());
				fecha=str_fecha_nrder.substring(0,str_fecha_nrder.indexOf("/"));								
				int int_num_anios_suma=0;	
				try {
					int_num_anios_suma=pckUtilidades.CConversion.CInt(num_anios);
				} catch (Exception e) {
					// TODO: handle exceptionO
				}
				fecha=(anio+int_num_anios_suma)+"-"+fecha;
			}else{
				fecha=str_fecha_nrder;
				fecha=anio+"-"+fecha;
			}
		}
		return fecha;
	}

	public void insertarDetallesRolEmpleado(String ide_geedp,String ide_nrrol,TablaGenerica tab_rubros,boolean boo_tiene_beneficio_guarderia,boolean boo_tiene_anticipos,
			String IDE_GEREG,String RMU,String acumula_fondos,String fecha_ingreso,String fecha_contrato,String ide_gepro,String fecha_inicial_gepro,
			String fecha_final_gepro,String fecha_subroga,String fecha_fin_subroga,String RMU_CARGO_SUBROGA,String ajuste_sueldo,String fecha_ajuste_sueldo,
			String dias_pendientes_vacacion,int nro_dias_comercial_nrtit,boolean es_liquidacion,String acumula_decimos,String base_imponible_mes_anterior,
			String fondo_reserva_acum_pago,String fondo_reserva_nom_pago,String fecha_ingreso_emgirs,String ide_gttem,int num_dias_exceso_liquidacion,String ide_nramo_empleado,String continuidad_liquidacion_geedp){
		
//Cambio dias antiguedad para generacion de nomina tipo liquidacion
		int dias_antiguedad=0;
		

		//Si es liquidacion saco los nuevos dias de antiguedad para calculo de vacaciones
		if (es_liquidacion==true){
			String str_sql="";

			TablaGenerica tab_empleado=utilitario.consultar("select ide_geedp,ide_gtemp,fecha_finctr_geedp  "
					+ "from gen_empleados_departamento_par  "
					+ "where ide_geedp="+ide_geedp+"");
			
			String fecha_iniTemp="";
					//retornaAccionpersonal(tab_empleado.getValor("IDE_GTEMP"), "23");
			//if (fecha_iniTemp==null || fecha_iniTemp.equals("") || fecha_iniTemp.isEmpty()) {
				String a="";
				TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+ide_geedp);
				//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
				int mesLiquidacion=0;
				TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
						+ "order by ide_geedp asc");
				String ide_geedp_empleado="";
				//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
				//variable guarda la fecha de salida
				String fecha_finTemp="";
				fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
				//variable cuenta el numero de acciones
				int contAccionInvalida=0,contForAccionInvalida=0;
				StringBuilder str_ide_geedp = new StringBuilder();

				for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
					if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(ide_geedp)==true) {
						String valorUltimoElemento="";
						valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
						if(valorUltimoElemento.equals(",")==true){
							str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
						}else {
						}
					i=tabEmpleados.getTotalFilas();
					}else{
							//Voy anidando los ides de la accion
							str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
			               // valor++;
			                if (tabEmpleados.getTotalFilas()==1) {
			   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
			   				contForAccionInvalida++;
			   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
			   					str_ide_geedp.append(",");
			                   // System.out.println("str_ide:  "+str_ide_empleado);
			   					}
			    			}	 	
					}
				}
				
				
				//Si no contiene acciones de personal luego de la liquidacion
				if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
					str_ide_geedp.append(ide_geedp);
				}

				
				//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
				
				a=utilitario.getVariable("p_nrh_acciones_personal");
				String[] listaAccionesEmpleado;
				listaAccionesEmpleado=a.split(",");
				for (int i = 0; i < listaAccionesEmpleado.length; i++) {
					fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
					if (!fecha_iniTemp.equals("")) {
						i=listaAccionesEmpleado.length;
					}
				}

				
			
			//double valorTemp=(utilitario.getDiferenciaFechas360(utilitario.getFecha(fecha_iniTemp), utilitario.getFecha(fecha_finTemp))-1);
			//int x = (int) valorTemp;
			//String valorNuevo=""+valorTemp;
			
			dias_antiguedad=pckUtilidades.CConversion.CInt(getTotalDiasLaborados(fecha_ingreso,fecha_finTemp));
			

		}else {
			dias_antiguedad=pckUtilidades.CConversion.CInt(getTotalDiasLaborados(fecha_ingreso,fecha_final_gepro));

		}
		
		int anio=utilitario.getAnio(fecha_inicial_gepro);
		for (int j = 0; j < tab_rubros.getTotalFilas(); j++) {
			System.out.println("calcularRolIndividualLiquidacion : "+tab_rubros.getValor(j, "DETALLE_NRRUB"));
			System.out.println("Rubro : "+tab_rubros.getValor(j, "DETALLE_NRRUB"));

			
		//	 calcularRolIndividual(tab_det_rol, ide_gtemp, ide_geedp, ide_nrrol, fecha_ingreso, fecha_contrato, IDE_GEREG, RMU, acumula_fondos, ide_gepro, fecha_inicial_gepro, fecha_final_gepro, fecha_subroga, fecha_fin_subroga, RMU_CARGO_SUBROGA, ajuste_sueldo, fecha_ajuste_sueldo, dias_pend_vacacion, nro_comercial_nrtit, ide_nrtin, acumula_decimo, base_imponible_mes_anterior, fondo_reserva_acum_pago, fondo_reserva_nom_pago, fecha_ingreso_emgirs, ide_gttem);
			
			
			
			tab_deta_rubros_rol.insertar();

			/*if (es_liquidacion){

				if (tab_rubros.getValor(j, "IDE_NRRUB").equalsIgnoreCase(utilitario.getVariable("p_nrh_rubro_vacaciones_liquidacion"))){
					if (tab_rubros.getValor(j, "FECHA_INICIAL_NRDER")!=null && !tab_rubros.getValor(j, "FECHA_INICIAL_NRDER").isEmpty()){

						String str_fecha_inicio_nrder=utilitario.getMes(fecha_ingreso)+"-"+utilitario.getDia(fecha_ingreso)+"/0";
						String str_fecha_fin_nrder=utilitario.getMes(fecha_final_gepro)+"-"+utilitario.getDia(fecha_final_gepro)+"/0";

						tab_deta_rubros_rol.setValor("FECHA_INICIAL_NRDER",str_fecha_inicio_nrder); 
						tab_deta_rubros_rol.setValor("FECHA_FINAL_NRDER",str_fecha_fin_nrder);
 
					}
				}else{


					if (tab_rubros.getValor(j, "DECIMO_NRRUB") != null && !tab_rubros.getValor(j, "DECIMO_NRRUB").isEmpty()
							&& (tab_rubros.getValor(j, "DECIMO_NRRUB").equalsIgnoreCase("1") || tab_rubros.getValor(j, "DECIMO_NRRUB").equalsIgnoreCase("true"))){

						String str_fecha_inicio_nrder=getFormatoFechaCalculoRubros(tab_rubros.getValor(j, "FECHA_INICIAL_NRDER"), anio);
						String str_fecha_fin_nrder=getFormatoFechaCalculoRubros(tab_rubros.getValor(j, "FECHA_FINAL_NRDER"), anio);
						String str_fecha_pago_nrder=getFormatoFechaCalculoRubros(tab_rubros.getValor(j, "FECHA_PAGO_NRDER"), anio);


						String num_anios_fec_ini_nrder="0";						
						String num_anios_fec_fin_nrder="0";						
						String num_anios_fec_pag_nrder="0";						

						try {
							num_anios_fec_ini_nrder=tab_rubros.getValor(j, "FECHA_INICIAL_NRDER").substring(tab_rubros.getValor(j, "FECHA_INICIAL_NRDER").indexOf("/")+1, tab_rubros.getValor(j, "FECHA_INICIAL_NRDER").length());						
						} catch (Exception e) {
							// TODO: handle exception
						}						
						try{							
							num_anios_fec_fin_nrder=tab_rubros.getValor(j, "FECHA_FINAL_NRDER").substring(tab_rubros.getValor(j, "FECHA_FINAL_NRDER").indexOf("/")+1, tab_rubros.getValor(j, "FECHA_FINAL_NRDER").length());

						} catch (Exception e) {
							// TODO: handle exception
						}						
						try{
							num_anios_fec_pag_nrder=tab_rubros.getValor(j, "FECHA_PAGO_NRDER").substring(tab_rubros.getValor(j, "FECHA_PAGO_NRDER").indexOf("/")+1, tab_rubros.getValor(j, "FECHA_PAGO_NRDER").length());						
						} catch (Exception e) {
							// TODO: handle exception
						}						


						// configuracion fechas de calculo de acumulados de decimos 
						String fecha_ini=utilitario.getMes(str_fecha_pago_nrder)+"-1";// mes y dia 
						String fecha_fin=utilitario.getMes(fecha_contrato)+"-"+utilitario.getDia(fecha_contrato);// mes y dia

						System.out.println("fecha contrato "+fecha_contrato);
						System.out.println("fecha fin nrder "+str_fecha_fin_nrder);
						if (utilitario.isFechaMenor(utilitario.getFecha(fecha_contrato),utilitario.getFecha(str_fecha_fin_nrder))){
							// si la fecha de liquidacion es menor que la fecha fin nrder
							tab_deta_rubros_rol.setValor("FECHA_INICIAL_NRDER",fecha_ini+"/"+num_anios_fec_ini_nrder+"");// (anio menos 1) - mes y dia de pago de rubro 
							tab_deta_rubros_rol.setValor("FECHA_FINAL_NRDER",fecha_fin+"/"+num_anios_fec_fin_nrder+"");// (anio) - mes y dia de liquidacion
							tab_deta_rubros_rol.setValor("FECHA_PAGO_NRDER",fecha_fin+"/"+num_anios_fec_pag_nrder+"");// (anio) - mes y dia de liquidacion
						}else{


							if (utilitario.getMes(str_fecha_pago_nrder)==utilitario.getMes(fecha_contrato)){
								tab_deta_rubros_rol.setValor("FECHA_INICIAL_NRDER",fecha_ini+"/-1");// (anio-1) - mes y dia de pago de rubro 
							}else{
								tab_deta_rubros_rol.setValor("FECHA_INICIAL_NRDER",fecha_ini+"/0");// (anio) - mes y dia de pago de rubro	
							}
							// si la fecha de liquidacion es mayor que la fecha fin nrder

							tab_deta_rubros_rol.setValor("FECHA_FINAL_NRDER",fecha_fin+"/0");// (anio) - mes y dia de liquidacion
							tab_deta_rubros_rol.setValor("FECHA_PAGO_NRDER",fecha_fin+"/0");// (anio) - mes y dia de liquidacion
						}

					}else{
						tab_deta_rubros_rol.setValor("FECHA_INICIAL_NRDER",tab_rubros.getValor(j, "FECHA_INICIAL_NRDER"));
						tab_deta_rubros_rol.setValor("FECHA_FINAL_NRDER",tab_rubros.getValor(j, "FECHA_FINAL_NRDER"));
						tab_deta_rubros_rol.setValor("FECHA_PAGO_NRDER",tab_rubros.getValor(j, "FECHA_PAGO_NRDER"));
					}
				}
			}else{
				*/
				tab_deta_rubros_rol.setValor("FECHA_INICIAL_NRDER",tab_rubros.getValor(j, "FECHA_INICIAL_NRDER"));
				tab_deta_rubros_rol.setValor("FECHA_FINAL_NRDER",tab_rubros.getValor(j, "FECHA_FINAL_NRDER"));
				tab_deta_rubros_rol.setValor("FECHA_PAGO_NRDER",tab_rubros.getValor(j, "FECHA_PAGO_NRDER"));
			//}

			//Cambio para personas que registran jubilacion
				/*	TablaGenerica  tab_empleado_depar=utilitario.consultar("SELECT IDE_GEEDP,IDE_GTEMP FROM "
					+ "GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_GEEDP="+ide_geedp);
			String empleado=tab_empleado_depar.getValor("IDE_GTEMP");
			TablaGenerica  tab_empleado=utilitario.consultar("SELECT IDE_GTEMP,JUBILADO_GTEMP FROM "
					+ "GTH_EMPLEADO WHERE IDE_GTEMP="+empleado);
			
			boolean jubiladoEmpleado=false;
			
			if (tab_empleado.getValor("JUBILADO_GTEMP")== null || tab_empleado.getValor("JUBILADO_GTEMP").equals("false") || tab_empleado.getValor("JUBILADO_GTEMP").isEmpty() || tab_empleado.getValor("JUBILADO_GTEMP").equals("")) {
				jubiladoEmpleado=false;
			}else {
				jubiladoEmpleado=true;
			}
			 
			if (jubiladoEmpleado==true && (tab_rubros.getValor(j, "IDE_NRRUB").equals("119"))) {
				System.out.println("Ingreso a calcular por jubilacion IESS PATRONAL");
				String textRubroPatronal = tab_rubros.getValor(j, "FORMULA_NRDER");
				String sSubCadenaPatronal = textRubroPatronal.substring(0,7);
				String part3Patronal=utilitario.getVariable("p_valor_jubilado_retorna_iess_patronal");
				//String part3Patronal="0.1015";
				String valorNuevoPatronal=sSubCadenaPatronal+"*"+part3Patronal;
				System.out.println("Valor nuevo Patronal"+valorNuevoPatronal);
				tab_deta_rubros_rol.setValor("VALOR_NRDRO",valorNuevoPatronal);
					 
				
				
			}
			else if (jubiladoEmpleado==true && (tab_rubros.getValor(j, "IDE_NRRUB").equals("44"))) {
				System.out.println("Ingreso a calcular por jubilacion IESS PERSONAL");
				String textRubroPersonal = tab_rubros.getValor(j, "FORMULA_NRDER");
				String sSubCadenaPersonal = textRubroPersonal.substring(0,7);
				String part3Personal=utilitario.getVariable("p_valor_jubilado_retorna_iess_personal");
				//String part3Personal="0.0945";
				String valorNuevoPersonal=sSubCadenaPersonal+"*"+part3Personal;
				System.out.println("Valor nuevo personal"+valorNuevoPersonal);
				tab_deta_rubros_rol.setValor("VALOR_NRDRO",valorNuevoPersonal);
									
			}

			else {
			//cont++;
			tab_deta_rubros_rol.setValor("VALOR_NRDRO",tab_rubros.getValor(j, "FORMULA_NRDER"));
			System.out.println("formula:  "+tab_rubros.getValor(j, "FORMULA_NRDER"));

			}
*/
			
			

			System.out.println(""+tab_deta_rubros_rol.getValor("IDE_NRDER"));
			
			//tab_deta_rubros_rol.setValor("VALOR_NRDRO",tab_rubros.getValor(j, "FORMULA_NRDER"));
			tab_deta_rubros_rol.setValor("IDE_NRDER",tab_rubros.getValor(j, "IDE_NRDER"));		
			tab_deta_rubros_rol.setValor("ide_geedp",ide_geedp);
			tab_deta_rubros_rol.setValor("IDE_NRROL",ide_nrrol);
			
			if (tab_rubros.getValor(j, "IDE_NRRUB").equals("29")) {
				System.out.println("VALOR");
			}
			tab_deta_rubros_rol.setValor("ORDEN_CALCULO_NRDRO",tab_rubros.getValor(j, "ORDEN_NRDER"));
			//System.out.println("oRDEN:  "+tab_rubros.getValor(j, "ORDEN_NRDER"));
			if(tab_rubros.getValor(j, "IDE_NRFOC").equalsIgnoreCase(P_NRH_RUBRO_IMPORTADO)){
				String str_valor=importarRubro(ide_geedp, tab_rubros.getValor(j, "IDE_NRRUB"), boo_tiene_beneficio_guarderia, boo_tiene_anticipos, IDE_GEREG,RMU, 
						acumula_fondos, fecha_ingreso, fecha_contrato, ide_gepro, fecha_inicial_gepro, fecha_final_gepro, dias_antiguedad+"",fecha_subroga,
						fecha_fin_subroga,RMU_CARGO_SUBROGA,ajuste_sueldo,fecha_ajuste_sueldo,dias_pendientes_vacacion,nro_dias_comercial_nrtit,es_liquidacion,
						acumula_decimos,base_imponible_mes_anterior,fondo_reserva_acum_pago,fondo_reserva_nom_pago,fecha_ingreso_emgirs,ide_gttem,num_dias_exceso_liquidacion,ide_nramo_empleado,continuidad_liquidacion_geedp);
				tab_deta_rubros_rol.setValor("VALOR_NRDRO",str_valor);				
				System.out.println("Importado:  "+str_valor);

			}else{tab_deta_rubros_rol.setValor("VALOR_NRDRO",tab_rubros.getValor(j, "FORMULA_NRDER"));}
		}
	}	

	int indice_ultimo_detalle=0;
	int total_filas_insertadas=0;
	public void actualizarDetallesRolEmpleado(String ide_geedp,String ide_nrrol,TablaGenerica tab_det_rol,boolean boo_tiene_beneficio_guarderia,boolean boo_tiene_anticipos,String IDE_GEREG,
			String RMU,String acumula_fondos,String fecha_ingreso,String fecha_contrato,String ide_gepro,String fecha_inicial_gepro,String fecha_final_gepro,String fecha_subroga,
			String fecha_fin_subroga,String RMU_cARGO_SUBROGA,String ajuste_sueldo,String fecha_ajuste_sueldo,String dias_pend_vacacion,int nro_comercial_nrtit,boolean es_liquidacion,
			String acumula_decimo,String base_imponible_mes_anterior,String fondo_reserva_acum_pago, String fondo_reserva_nom_pago, String fecha_ingreso_emgirs,String ide_gttem, int num_dias_exceso_liquidacion,
			String ide_nramo_empleado){
		//int dias_antiguedad=pckUtilidades.CConversion.CInt(getTotalDiasLaborados(fecha_ingreso,fecha_final_gepro));
		//int dias_antiguedad=0;
		//int anio=utilitario.getAnio(fecha_inicial_gepro);
		
		
		
int dias_antiguedad=0;
		

		//Si es liquidacion saco los nuevos dias de antiguedad para calculo de vacaciones

			String str_sql="";

			TablaGenerica tab_empleadoTemp=utilitario.consultar("select ide_geedp,ide_gtemp,fecha_finctr_geedp  "
					+ "from gen_empleados_departamento_par  "
					+ "where ide_geedp="+ide_geedp+"");
			
		
			
			TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+ide_geedp);
			String fecha_iniTemp="";
			//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
			String a="";
			int mesLiquidacion=0;
			TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
					+ "order by ide_geedp asc");
			String ide_geedp_empleado="";
			//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
			//variable guarda la fecha de salida
			String fecha_finTemp="";
			fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
			//variable cuenta el numero de acciones
			int contAccionInvalida=0,contForAccionInvalida=0;
			StringBuilder str_ide_geedp = new StringBuilder();

			for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
				if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(ide_geedp)==true) {
					String valorUltimoElemento="";
					valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
					if(valorUltimoElemento.equals(",")==true){
						str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
					}else {
					}
				i=tabEmpleados.getTotalFilas();
				}else{
						//Voy anidando los ides de la accion
						str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
		               // valor++;
		                if (tabEmpleados.getTotalFilas()==1) {
		   			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
		   				contForAccionInvalida++;
		   					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
		   					str_ide_geedp.append(",");
		                   // System.out.println("str_ide:  "+str_ide_empleado);
		   					}
		    			}	 	
				}
			}
			
			
			//Si no contiene acciones de personal luego de la liquidacion
			if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
				str_ide_geedp.append(ide_geedp);
			}

			
			//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
			
			a=utilitario.getVariable("p_nrh_acciones_personal");
			String[] listaAccionesEmpleado;
			listaAccionesEmpleado=a.split(",");
			for (int i = 0; i < listaAccionesEmpleado.length; i++) {
				fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
				if (!fecha_iniTemp.equals("")) {
					i=listaAccionesEmpleado.length;
				}
			}

			//double valorTemp=(utilitario.getDiferenciaFechas360(utilitario.getFecha(fecha_iniTemp), utilitario.getFecha(fecha_finTemp))-1);
			//int x = (int) valorTemp;
			//String valorNuevo=""+valorTemp;
			dias_antiguedad=pckUtilidades.CConversion.CInt(getTotalDiasLaborados(fecha_ingreso,fecha_finTemp));
			

	
		
		
		total_filas_insertadas=0;

		for (int j = indice_ultimo_detalle; j < tab_det_rol.getTotalFilas(); j++) {
			if (tab_det_rol.getValor(j, "IDE_GEEDP").equalsIgnoreCase(ide_geedp)){
				tab_deta_rubros_rol.insertar();
				total_filas_insertadas++;
				/*if (es_liquidacion){
					if (tab_det_rol.getValor(j, "IDE_NRRUB").equalsIgnoreCase(utilitario.getVariable("p_nrh_rubro_vacaciones_liquidacion"))){
						if (tab_det_rol.getValor(j, "FECHA_INICIAL_NRDER")!=null && !tab_det_rol.getValor(j, "FECHA_INICIAL_NRDER").isEmpty()){

							String str_fecha_inicio_nrder=utilitario.getMes(fecha_ingreso)+"-"+utilitario.getDia(fecha_ingreso)+"/0";
							String str_fecha_fin_nrder=utilitario.getMes(fecha_final_gepro)+"-"+utilitario.getDia(fecha_final_gepro)+"/0";

							tab_deta_rubros_rol.setValor("FECHA_INICIAL_NRDER",str_fecha_inicio_nrder); 
							tab_deta_rubros_rol.setValor("FECHA_FINAL_NRDER",str_fecha_fin_nrder);

						}
					}else{


						if (tab_det_rol.getValor(j, "DECIMO_NRRUB") != null && !tab_det_rol.getValor(j, "DECIMO_NRRUB").isEmpty()
								&& (tab_det_rol.getValor(j, "DECIMO_NRRUB").equalsIgnoreCase("1") || tab_det_rol.getValor(j, "DECIMO_NRRUB").equalsIgnoreCase("true"))){

							String str_fecha_inicio_nrder=getFormatoFechaCalculoRubros(tab_det_rol.getValor(j, "FECHA_INICIAL_NRDER"), anio);
							String str_fecha_fin_nrder=getFormatoFechaCalculoRubros(tab_det_rol.getValor(j, "FECHA_FINAL_NRDER"), anio);
							String str_fecha_pago_nrder=getFormatoFechaCalculoRubros(tab_det_rol.getValor(j, "FECHA_PAGO_NRDER"), anio);


							String num_anios_fec_ini_nrder="0";						
							String num_anios_fec_fin_nrder="0";						
							String num_anios_fec_pag_nrder="0";						

							try {
								num_anios_fec_ini_nrder=tab_det_rol.getValor(j, "FECHA_INICIAL_NRDER").substring(tab_det_rol.getValor(j, "FECHA_INICIAL_NRDER").indexOf("/")+1, tab_det_rol.getValor(j, "FECHA_INICIAL_NRDER").length());						
							} catch (Exception e) {
								// TODO: handle exception
							}						
							try{							
								num_anios_fec_fin_nrder=tab_det_rol.getValor(j, "FECHA_FINAL_NRDER").substring(tab_det_rol.getValor(j, "FECHA_FINAL_NRDER").indexOf("/")+1, tab_det_rol.getValor(j, "FECHA_FINAL_NRDER").length());

							} catch (Exception e) {
								// TODO: handle exception
							}						
							try{
								num_anios_fec_pag_nrder=tab_det_rol.getValor(j, "FECHA_PAGO_NRDER").substring(tab_det_rol.getValor(j, "FECHA_PAGO_NRDER").indexOf("/")+1, tab_det_rol.getValor(j, "FECHA_PAGO_NRDER").length());						
							} catch (Exception e) {
								// TODO: handle exception
							}						


							// configuracion fechas de calculo de acumulados de decimos 
							String fecha_ini=utilitario.getMes(str_fecha_pago_nrder)+"-1";// mes y dia 
							String fecha_fin=utilitario.getMes(fecha_contrato)+"-"+utilitario.getDia(fecha_contrato);// mes y dia

							if (utilitario.isFechaMenor(utilitario.getFecha(fecha_contrato),utilitario.getFecha(str_fecha_fin_nrder))){
								// si la fecha de liquidacion es menor que la fecha fin nrder
								tab_deta_rubros_rol.setValor("FECHA_INICIAL_NRDER",fecha_ini+"/"+num_anios_fec_ini_nrder+"");// (anio menos 1) - mes y dia de pago de rubro 
								tab_deta_rubros_rol.setValor("FECHA_FINAL_NRDER",fecha_fin+"/"+num_anios_fec_fin_nrder+"");// (anio) - mes y dia de liquidacion
								tab_deta_rubros_rol.setValor("FECHA_PAGO_NRDER",fecha_fin+"/"+num_anios_fec_pag_nrder+"");// (anio) - mes y dia de liquidacion
							}else{
									tab_deta_rubros_rol.setValor("FECHA_INICIAL_NRDER",fecha_ini+"/-1");// (anio-1) - mes y dia de pago de rubro 
								if (utilitario.getMes(str_fecha_pago_nrder)==utilitario.getMes(fecha_contrato)){
								}else{
									tab_deta_rubros_rol.setValor("FECHA_INICIAL_NRDER",fecha_ini+"/0");// (anio) - mes y dia de pago de rubro	
								}
								// si la fecha de liquidacion es mayor que la fecha fin nrder

								tab_deta_rubros_rol.setValor("FECHA_FINAL_NRDER",fecha_fin+"/0");// (anio) - mes y dia de liquidacion
								tab_deta_rubros_rol.setValor("FECHA_PAGO_NRDER",fecha_fin+"/0");// (anio) - mes y dia de liquidacion
							}

						}else{
 							tab_deta_rubros_rol.setValor("FECHA_INICIAL_NRDER",tab_det_rol.getValor(j, "FECHA_INICIAL_NRDER"));
							tab_deta_rubros_rol.setValor("FECHA_FINAL_NRDER",tab_det_rol.getValor(j, "FECHA_FINAL_NRDER"));
							tab_deta_rubros_rol.setValor("FECHA_PAGO_NRDER",tab_det_rol.getValor(j, "FECHA_PAGO_NRDER"));
						}
					}
				}else{				

					tab_deta_rubros_rol.setValor("FECHA_INICIAL_NRDER",tab_det_rol.getValor(j, "FECHA_INICIAL_NRDER"));
					tab_deta_rubros_rol.setValor("FECHA_FINAL_NRDER",tab_det_rol.getValor(j, "FECHA_FINAL_NRDER"));
					tab_deta_rubros_rol.setValor("FECHA_PAGO_NRDER",tab_det_rol.getValor(j, "FECHA_PAGO_NRDER"));
				}
*/
				tab_deta_rubros_rol.setValor("IDE_NRDER",tab_det_rol.getValor(j, "IDE_NRDER"));
				tab_deta_rubros_rol.setValor("ide_geedp",ide_geedp);
				tab_deta_rubros_rol.setValor("IDE_NRROL",ide_nrrol);
				tab_deta_rubros_rol.setValor("ORDEN_CALCULO_NRDRO",tab_det_rol.getValor(j, "ORDEN_NRDER"));
				if(tab_det_rol.getValor(j, "IDE_NRFOC").equalsIgnoreCase(P_NRH_RUBRO_IMPORTADO)){
					String str_valor=importarRubro(ide_geedp, tab_det_rol.getValor(j, "IDE_NRRUB"), boo_tiene_beneficio_guarderia, boo_tiene_anticipos, IDE_GEREG,RMU, acumula_fondos, 
							fecha_ingreso, fecha_contrato, ide_gepro, fecha_inicial_gepro, fecha_final_gepro, dias_antiguedad+"",fecha_subroga,fecha_fin_subroga,RMU_cARGO_SUBROGA,
							ajuste_sueldo,fecha_ajuste_sueldo,dias_pend_vacacion,nro_comercial_nrtit,es_liquidacion,acumula_decimo,base_imponible_mes_anterior,fondo_reserva_acum_pago,
							fondo_reserva_nom_pago,fecha_ingreso_emgirs,ide_gttem,num_dias_exceso_liquidacion,ide_nramo_empleado,"");
					tab_deta_rubros_rol.setValor("VALOR_NRDRO",str_valor);

				}else if(tab_det_rol.getValor(j, "IDE_NRFOC").equalsIgnoreCase(P_NRH_RUBRO_FORMULA)
						|| tab_det_rol.getValor(j, "IDE_NRFOC").equalsIgnoreCase(P_NRH_RUBRO_CONSTANTE)){
					
							
					//Obtnego los empleados de la tabla gen_empleados_departamento_par
					TablaGenerica  tab_empleado_depar=utilitario.consultar("SELECT IDE_GEEDP,IDE_GTEMP FROM "
							+ "GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE ACTIVO_geedp=TRUE AND IDE_GEEDP="+ide_geedp);
					String empleado=tab_empleado_depar.getValor("IDE_GTEMP");
					//obtengo el estado de jubilacion del empleado 
					TablaGenerica  tab_empleado=utilitario.consultar("SELECT IDE_GTEMP,JUBILADO_GTEMP FROM "
							+ "GTH_EMPLEADO WHERE IDE_GTEMP="+empleado);
					
					boolean jubiladoEmpleado=false;
					//Validacion de estado JUBILADO_GTEMP
					if (tab_empleado.getValor("JUBILADO_GTEMP")== null || tab_empleado.getValor("JUBILADO_GTEMP").equals("false") || tab_empleado.getValor("JUBILADO_GTEMP").isEmpty() || tab_empleado.getValor("JUBILADO_GTEMP").equals("")) {
						jubiladoEmpleado=false;
					}else {
						jubiladoEmpleado=true;
					}
					//Estado jubilado y rubro de tipo IESS PATRONAL
					
					String textRubroPatronal="",part3Patronal="",valorNuevoPatronal="",sSubCadenaPatronal="";
					String textRubroPersonal="",part3Personal="",valorNuevoPersonal="",sSubCadenaPersonal="";

					if (jubiladoEmpleado==true && (tab_det_rol.getValor(j, "IDE_NRRUB").equals("119"))) {
						System.out.println("Ingreso a calcular por jubilacion IESS PATRONAL");
						textRubroPatronal = tab_det_rol.getValor(j, "FORMULA_NRDER");
						System.out.println("string"+textRubroPatronal);
						sSubCadenaPatronal = textRubroPatronal.substring(0,7);
						//part3Patronal="0.1015";
						 part3Patronal=utilitario.getVariable("p_valor_jubilado_retorna_iess_patronal");
						valorNuevoPatronal=sSubCadenaPatronal+"*"+part3Patronal;
						System.out.println("Valor nuevo patronal"+valorNuevoPatronal);
						tab_deta_rubros_rol.setValor("VALOR_NRDRO",valorNuevoPatronal);

						
					//Estado jubilado y rubro de tipo IESS PATRONAL	
					}else if (jubiladoEmpleado==true && (tab_det_rol.getValor(j, "IDE_NRRUB").equals("44"))) {
						System.out.println("Ingreso a calcular por jubilacion IESS PERSONAL");
						textRubroPersonal = tab_det_rol.getValor(j, "FORMULA_NRDER");
						sSubCadenaPersonal = textRubroPersonal.substring(0,7);
						//part3Personal="0.0945";
						 part3Personal=utilitario.getVariable("p_valor_jubilado_retorna_iess_personal");
						valorNuevoPersonal=sSubCadenaPersonal+"*"+part3Personal;
						System.out.println("Valor nuevo personal"+valorNuevoPersonal);
						tab_deta_rubros_rol.setValor("VALOR_NRDRO",valorNuevoPersonal);
						//System.out.println("");
						//tab_deta_rubros_rol.setValor("VALOR_NRDRO",tab_det_rol.getValor(j, "FORMULA_NRDER"));			
						
					}
					
					else{
					//	cont++;
						tab_deta_rubros_rol.setValor("VALOR_NRDRO",tab_det_rol.getValor(j, "FORMULA_NRDER"));
					//	System.out.println("Contador de Empleados: "+cont);	
					}			
				
				}else{
					tab_deta_rubros_rol.setValor("VALOR_NRDRO",tab_det_rol.getValor(j, "VALOR_NRDRO"));
				}
				indice_ultimo_detalle=j;
			}else{
				indice_ultimo_detalle=j;
				break;
			}
		}

	}


	public String getRubrosCalculoDeIngresos(String ide_nrdtn){
		TablaGenerica tab_detalle_rubros=utilitario.consultar("SELECT * FROM nrh_detalle_rubro " +
				"where IDE_NRDTN="+ide_nrdtn+" and ide_nrrub in("+utilitario.getVariable("p_nrh_rubro_total_ingresos")+")");
		tab_detalle_rubros.imprimirSql();
		String ide_formula1="";
		String formula="";												
		if(tab_detalle_rubros.getTotalFilas()==1){
			for (int i = 0; i <tab_detalle_rubros.getTotalFilas(); i++) {
				ide_formula1="";								
				ide_formula1+=tab_detalle_rubros.getValor(0, "formula_nrder");
			}			
			ide_formula1=ide_formula1.replace("=", "");							
			ide_formula1=ide_formula1.replace("[", "");
			ide_formula1=ide_formula1.replace("]", "");
			ide_formula1=ide_formula1.replace("+", ",");							
			ide_formula1=ide_formula1.replace("-", ",");
			ide_formula1=ide_formula1.replace("*", ",");
			ide_formula1=ide_formula1.replace("/", ",");

			if(!ide_formula1.isEmpty() ){
				formula=ide_formula1;								
				return formula;
			}							
		}else{
			utilitario.agregarMensajeInfo("No se puede generar la Boleta de Pago", "No se encuentra configurado los rubros de: Total de Ingresos y Total de Egresos");							
		}					
		return null;
	}

	public String getRubrosCalculoDeEgresos(String ide_nrdtn){
		TablaGenerica tab_detalle_rubros=utilitario.consultar("SELECT * FROM nrh_detalle_rubro " +
				"where IDE_NRDTN="+ide_nrdtn+" and ide_nrrub in("+utilitario.getVariable("p_nrh_rubro_total_egresos")+")");
		tab_detalle_rubros.imprimirSql();
		String ide_formula1="";
		String formula="";												
		if(tab_detalle_rubros.getTotalFilas()==2){
			for (int i = 0; i <tab_detalle_rubros.getTotalFilas(); i++) {
				ide_formula1="";								
				ide_formula1+=tab_detalle_rubros.getValor(0, "formula_nrder");
			}			
			ide_formula1=ide_formula1.replace("=", "");							
			ide_formula1=ide_formula1.replace("[", "");
			ide_formula1=ide_formula1.replace("]", "");
			ide_formula1=ide_formula1.replace("+", ",");							
			ide_formula1=ide_formula1.replace("-", ",");
			ide_formula1=ide_formula1.replace("*", ",");
			ide_formula1=ide_formula1.replace("/", ",");

			if(!ide_formula1.isEmpty()){
				formula=ide_formula1;								
				return formula;
			}							
		}else{
			utilitario.agregarMensajeInfo("No se puede generar la Boleta de Pago", "No se encuentra configurado los rubros de: Total de Ingresos y Total de Egresos");							
		}					
		return null;
	}

	TablaGenerica tab_cuotas=new TablaGenerica();
	TablaGenerica tab_det_fac_gua=new TablaGenerica();

	public void calcularRolIndividual(TablaGenerica tab_rubros,String ide_gtemp,String ide_geedp,String ide_nrrol,String fecha_ingreso,String fecha_contrato,String IDE_GEREG,
			String RMU,String acumula_fondos,String ide_gepro,String fecha_inicial_gepro,String fecha_final_gepro,String fecha_subroga,String fecha_fin_subroga,String RMU_CARGO_SUBROGA,
			String ajuste_sueldo,String fecha_ajuste_sueldo,String dias_pendientes_vacacion,Long ide_inicial,int nro_dias_comercial,String IDE_NRTIN,String acumula_decimo,
			String base_imponible_mes_anterior,String fondo_reserva_acum_pago, String fondo_reserva_nom_pago,String fecha_ingreso_emgirs,String ide_gttem,String continuidad_liquidacion_geedp){

		boolean boo_tiene_anticipos=false;
		tab_cuotas=serv_anticipo.getCuotasAnticipoPorPagarEmpleado(ide_gtemp, ide_gepro,"");
		if (tab_cuotas.getTotalFilas()>0){
			boo_tiene_anticipos=true;
		}

		boolean boo_tiene_beneficio_guarderia=false;
		tab_det_fac_gua=getDetalleFacturaGuarderiaEmpleado(ide_gtemp);
		if (tab_det_fac_gua.getTotalFilas()>0){
			boo_tiene_beneficio_guarderia=true;
		}

		// insertarmos todos los rubros del empleado e importamos los valores unicamente de los rubros de forma de calculo (IMPORTADO)
		insertarDetallesRolEmpleado(ide_geedp, ide_nrrol, tab_rubros, boo_tiene_beneficio_guarderia, boo_tiene_anticipos, IDE_GEREG,RMU, acumula_fondos, fecha_ingreso, fecha_contrato, 
				ide_gepro, fecha_inicial_gepro, fecha_final_gepro,fecha_subroga,fecha_fin_subroga,RMU_CARGO_SUBROGA,ajuste_sueldo,fecha_ajuste_sueldo,dias_pendientes_vacacion,
				nro_dias_comercial,false,acumula_decimo,base_imponible_mes_anterior,fondo_reserva_acum_pago,fondo_reserva_nom_pago,fecha_ingreso_emgirs,ide_gttem,0,"","");



		int indice=tab_rubros.getTotalFilas();

		despejarFormulasRol(indice, ide_geedp, fecha_final_gepro);



		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			utilitario.getConexion().ejecutarSql("UPDATE GEN_EMPLEADOS_DEPARTAMENTO_PAR set EJECUTO_LIQUIDACION_GEEDP=1 where IDE_GEEDP="+ide_geedp);
		}




		String str_valor="";
		for (int l = 0; l < tab_rubros.getTotalFilas(); l++) {
			ide_inicial=ide_inicial+1;
			if (tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO")!=null && !tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO").isEmpty()){

				try {
					BigDecimal big_valor=new BigDecimal(pckUtilidades.CConversion.CDbl_2(tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO")));
					big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
					str_valor=big_valor+"";
				} catch (Exception e) {
					str_valor="0.00";
				}
			}else{
				str_valor="0.00";
			}

			String orden=tab_deta_rubros_rol.getValor(l,"ORDEN_CALCULO_NRDRO");
			if (orden.isEmpty()){
				orden=null;
			}
			String str_insert="INSERT INTO NRH_DETALLE_ROL (IDE_NRDRO, IDE_NRROL, IDE_GEEDP, IDE_NRDER, VALOR_NRDRO, USUARIO_INGRE, FECHA_INGRE, HORA_INGRE, USUARIO_ACTUA, FECHA_ACTUA, HORA_ACTUA,ORDEN_CALCULO_NRDRO) " +
					"VALUES ("+ide_inicial+", " +
					""+tab_deta_rubros_rol.getValor(l,"IDE_NRROL")+", " +
					""+tab_deta_rubros_rol.getValor(l,"IDE_GEEDP")+", " +
					""+tab_deta_rubros_rol.getValor(l,"IDE_NRDER")+", " +
					""+str_valor+", NULL, NULL, NULL, NULL, NULL, NULL, " +
					""+orden+")";
			utilitario.getConexion().ejecutarSql(str_insert);

		}

		TablaGenerica tab_rub_repetido=utilitario.consultar("select * from ( " +
				"SELECT ide_nrder,COUNT(ide_nrder) as num_rub FROM ( " +
				"select IDE_NRDRO,IDE_NRROL,IDE_GEEDP,DRO.IDE_NRDER,VALOR_NRDRO,DER.IDE_NRRUB,RUB.IDE_NRFOC,ORDEN_NRDER,DER.FORMULA_NRDER,DER.FECHA_INICIAL_NRDER,DER.FECHA_FINAL_NRDER,DER.FECHA_PAGO_NRDER,decimo_nrrub from NRH_DETALLE_ROL DRO INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
				"where DER.ACTIVO_NRDER=true and DRO.IDE_NRROL="+ide_nrrol+" and DRO.IDE_GEEDP="+ide_geedp+" " +
				"order by der.ide_nrder " +
				")a GROUP BY ide_nrder " +
				")a where num_rub >1"); 

		for (int i = 0; i < tab_rub_repetido.getTotalFilas(); i++) {
			TablaGenerica tab_det_rol=utilitario.consultar("select * from NRH_DETALLE_ROL where IDE_NRDER="+tab_rub_repetido.getValor(i, "IDE_NRDER")+" and IDE_GEEDP="+ide_geedp+" and ide_nrrol="+ide_nrrol);
			if (tab_det_rol.getTotalFilas()>0){
				utilitario.getConexion().ejecutarSql("delete from nrh_detalle_rol where ide_nrdro="+tab_det_rol.getValor(0,"ide_nrdro"));
			}
		}

	}

	public void calcularRolIndividualLiquidacion(TablaGenerica tab_rubros,String ide_gtemp,String ide_geedp,String ide_nrrol,String fecha_ingreso,String fecha_contrato,String IDE_GEREG,
			String RMU,String acumula_fondos,String ide_gepro,String fecha_inicial_gepro,String fecha_final_gepro,String fecha_subroga,String fecha_fin_subroga,String RMU_CARGO_SUBROGA,
			String ajuste_sueldo,String fecha_ajuste_sueldo,String dias_pendientes_vacacion,Long ide_inicial,int nro_dias_comercial,String ide_nrtin,String acumula_decimos,
			String base_imponible_mes_anterior,String fondo_reserva_acum_pago, String fondo_reserva_nom_pago, String fecha_ingreso_emgirs,String ide_gttem,int num_dias_exceso_liquidacion,String continuidad_liquidacion_geedp){

		boolean boo_tiene_anticipos=false;
		tab_cuotas=serv_anticipo.getCuotasAnticipoPorPagarEmpleadoLiquidacion(ide_gtemp, ide_gepro,("2017-12-31"));
	   
    	int valor=0;
      	int valorEmp=0;
      	int valorJefe=0;
       boolean bandSinArea=false;
       int bandArea=0;
   	StringBuilder str_ide_empleado=new StringBuilder();
   String ide_nramo_empleado="";
		if (tab_cuotas.getTotalFilas()>0) {
			boo_tiene_anticipos=true;
			
			//System.out.println("nramo a descontar liquidacion "+str_ide_nramo_descontar);
			int valorAnticipo=0;
			StringBuilder str_ide_empleado_anticipo= new StringBuilder();
			//Recorro la tabla con todas las acciones de personal efectuadas al empleado
		         		for (int i = 0; i < tab_cuotas.getTotalFilas(); i++) 
		         		{
		         		//Voy anidando los ides de la accion
		          	    str_ide_empleado_anticipo.append(tab_cuotas.getValor(i, "IDE_NRAMO"));
		               // valor++;
		                if (tab_cuotas.getTotalFilas()==1) {
		   			}else if (valorAnticipo<=tab_cuotas.getTotalFilas()) {
		   				valorAnticipo++;
		   					if(valorAnticipo<(tab_cuotas.getTotalFilas())){
		                    str_ide_empleado_anticipo.append(",");
		                   // System.out.println("str_ide:  "+str_ide_empleado);
		   					}
		    			}
		                
		         		}
		    //System.out.println("DETALLE TABLA AMORTIZACION"+str_ide_empleado_anticipo.toString());
			String strSql="update NRH_AMORTIZACION set IDE_NRROL="+ide_nrrol+" where IDE_NRAMO in ("+str_ide_empleado_anticipo.toString()+") ";
			if (str_ide_empleado_anticipo.length()>0){
			//	System.out.println("generarRolLiquidacion strSql: "+strSql);
				ide_nramo_empleado=str_ide_empleado_anticipo.toString();
				utilitario.getConexion().ejecutarSql(strSql);
			}
	  		 }
	
		boolean boo_tiene_beneficio_guarderia=false;
		tab_det_fac_gua=getDetalleFacturaGuarderiaEmpleado(ide_gtemp);
		if (tab_det_fac_gua.getTotalFilas()>0){
			boo_tiene_beneficio_guarderia=true;
		}

		// insertarmos todos los rubros del empleado e importamos los valores unicamente de los rubros de forma de calculo (IMPORTADO)
		insertarDetallesRolEmpleado(ide_geedp, ide_nrrol, tab_rubros, boo_tiene_beneficio_guarderia, boo_tiene_anticipos, IDE_GEREG,RMU, acumula_fondos, fecha_ingreso, fecha_contrato, 
				ide_gepro, fecha_inicial_gepro, fecha_final_gepro,fecha_subroga,fecha_fin_subroga,RMU_CARGO_SUBROGA,ajuste_sueldo,fecha_ajuste_sueldo,dias_pendientes_vacacion,
				nro_dias_comercial,true,acumula_decimos,base_imponible_mes_anterior,fondo_reserva_acum_pago,fondo_reserva_nom_pago,fecha_ingreso_emgirs,ide_gttem,num_dias_exceso_liquidacion,ide_nramo_empleado,continuidad_liquidacion_geedp);

		int indice=tab_rubros.getTotalFilas();

		despejarFormulasRol(indice, ide_geedp, fecha_final_gepro);


		if (ide_nrtin.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			utilitario.getConexion().ejecutarSql("UPDATE GEN_EMPLEADOS_DEPARTAMENTO_PAR set EJECUTO_LIQUIDACION_GEEDP=1 where IDE_GEEDP="+ide_geedp);
		}
  
		String str_valor="";
		for (int l = 0; l < tab_rubros.getTotalFilas(); l++) {
			ide_inicial=ide_inicial+1;
			if (tab_deta_rubros_rol.getValor(l, "IDE_NRDER").equals("2121")) {
				System.out.println("fondos nomina:   "+l+"   "+tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO")+""+tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO"));
			}

			
			if (tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO")!=null && !tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO").isEmpty()){
				try {
					//System.out.println("VALOR FINAL: "+tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO"));
					BigDecimal big_valor=new BigDecimal(pckUtilidades.CConversion.CDbl_2(tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO")));
					big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
					str_valor=big_valor+"";
				} catch (Exception e) {
					str_valor="0.00";
				}
			}else{
				str_valor="0.00";
			}
			String orden=tab_deta_rubros_rol.getValor(l,"ORDEN_CALCULO_NRDRO");
			if (orden.isEmpty()){
				orden=null;
			}

			String str_insert="INSERT INTO NRH_DETALLE_ROL (IDE_NRDRO, IDE_NRROL, IDE_GEEDP, IDE_NRDER, VALOR_NRDRO, USUARIO_INGRE, FECHA_INGRE, HORA_INGRE, USUARIO_ACTUA, FECHA_ACTUA, HORA_ACTUA,ORDEN_CALCULO_NRDRO) " +
					"VALUES ("+ide_inicial+", " +
					""+tab_deta_rubros_rol.getValor(l,"IDE_NRROL")+", " +
					""+tab_deta_rubros_rol.getValor(l,"IDE_GEEDP")+", " +
					""+tab_deta_rubros_rol.getValor(l,"IDE_NRDER")+", " +
					""+str_valor+", NULL, NULL, NULL, NULL, NULL, NULL, " +
					""+orden+")";
			System.out.println("sql_insert_juanca"+str_insert);
			utilitario.getConexion().ejecutarSql(str_insert);

		}

	TablaGenerica tab_rub_repetido=utilitario.consultar("select * from ( " +
				"SELECT ide_nrder,COUNT(ide_nrder) as num_rub FROM ( " +
				"select IDE_NRDRO,IDE_NRROL,IDE_GEEDP,DRO.IDE_NRDER,VALOR_NRDRO,DER.IDE_NRRUB,RUB.IDE_NRFOC,ORDEN_NRDER,DER.FORMULA_NRDER,DER.FECHA_INICIAL_NRDER,DER.FECHA_FINAL_NRDER,DER.FECHA_PAGO_NRDER,decimo_nrrub from NRH_DETALLE_ROL DRO INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
				"where DER.ACTIVO_NRDER=true and DRO.IDE_NRROL="+ide_nrrol+" and DRO.IDE_GEEDP="+ide_geedp+" " +
				"order by der.ide_nrder " +
				")a GROUP BY ide_nrder " +
				")a where num_rub >1"); 

		for (int i = 0; i < tab_rub_repetido.getTotalFilas(); i++) {
			TablaGenerica tab_det_rol1=utilitario.consultar("select * from NRH_DETALLE_ROL where IDE_NRDER="+tab_rub_repetido.getValor(i, "IDE_NRDER")+" and IDE_GEEDP="+ide_geedp+" and ide_nrrol="+ide_nrrol);
			if (tab_det_rol1.getTotalFilas()>0){
				utilitario.getConexion().ejecutarSql("delete from nrh_detalle_rol where ide_nrdro="+tab_det_rol1.getValor(0,"ide_nrdro"));
			}
		}

	}


	public String getIdeDetalleTipoNomina(String IDE_GEEDP){
		TablaGenerica tab_det_tip_nom=utilitario.consultar("select DTN.IDE_NRDTN,EDP.IDE_GEEDP " +
				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM " +
				"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP " +
				//				"and EDP.IDE_SUCU=DTN.IDE_SUCU and EDP.IDE_GTTCO=DTN.IDE_GTTCO " +
				"WHERE EDP.IDE_GEEDP="+IDE_GEEDP+"");

		if (tab_det_tip_nom.getTotalFilas()>0){
			return tab_det_tip_nom.getValor(0,"IDE_NRDTN");
		}
		return null;
	}

	/**
	 * Busca un detalle de tipo de nï¿½mina
	 * @param IDE_NRTIN   Tipo de Nomina
	 * @param IDE_GTTEM   Tipo de Empleado
	 * @param IDE_GTTCO   Tipo de Contrato	 
	 * @param IDE_SUCU    Sucursal
	 * @param IDE_NRTIR   Tipo Rol 
	 * @return TablaGenerica  resultado
	 */
	public TablaGenerica getDetalleTipoNomina(String IDE_NRTIN ,String IDE_GTTEM, String IDE_GTTCO , String IDE_SUCU,String IDE_NRTIT){

		TablaGenerica tab_dtn=utilitario.consultar("SELECT * FROM NRH_DETALLE_TIPO_NOMINA " +
				"WHERE IDE_NRTIN="+IDE_NRTIN+" " +
				"AND IDE_GTTEM="+IDE_GTTEM+" " +
				"AND IDE_GTTCO="+IDE_GTTCO+" " +
				"AND IDE_SUCU="+IDE_SUCU+" " +
				"AND IDE_NRTIT="+IDE_NRTIT+" " +
				"AND ACTIVO_NRDTN=true");
		return tab_dtn;
	}

	/**Busca todos los rubros de un tipo de Nomina en orden desendente por que despues se insertaran en otra tabla
	 * @param IDE_NRDTN
	 * @return TablaGenerica  resultado
	 */
	private TablaGenerica getRubrosTipoNomina(String IDE_NRDTN){
		//		TablaGenerica tab_rubros=utilitario.consultar("SELECT * FROM NRH_DETALLE_RUBRO WHERE IDE_NRDTN="+IDE_NRDTN+" AND ACTIVO_NRDER=true ORDER BY ORDEN_NRDER DESC");
		TablaGenerica tab_rubros=utilitario.consultar("SELECT ide_nrder,RUB.ide_nrrub,RUB.detalle_nrrub,IDE_NRFOC,IDE_NRDTN,FORMULA_NRDER,FECHA_INICIAL_NRDER, " +
				"FECHA_FINAL_NRDER,FECHA_PAGO_NRDER,ORDEN_NRDER,DECIMO_NRRUB " +
				"FROM NRH_DETALLE_RUBRO DER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"WHERE DER.IDE_NRDTN="+IDE_NRDTN+" " +
				"AND DER.ACTIVO_NRDER=TRUE " +
				"ORDER BY DER.ORDEN_NRDER DESC ");
		return tab_rubros;
	}


	/**Busca todos los rubros de un tipo de Nomina en orden desendente por que despues se insertaran en otra tabla
	 * @param IDE_NRDTN
	 * @return TablaGenerica  resultado
	 */
	private TablaGenerica getRubrosTipoNominaDecimos(String IDE_NRDTN,String IDE_NRDER_DECIMOS){
		//		TablaGenerica tab_rubros=utilitario.consultar("SELECT ide_nrder,RUB.ide_nrrub,IDE_NRFOC,IDE_NRDTN,FORMULA_NRDER,FECHA_INICIAL_NRDER, " +
		//				"FECHA_FINAL_NRDER,FECHA_PAGO_NRDER,ORDEN_NRDER " +
		//				"FROM NRH_DETALLE_RUBRO DER " +
		//				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
		//				"WHERE DER.IDE_NRDTN="+IDE_NRDTN+" " +
		//				"AND DER.ACTIVO_NRDER=TRUE " +
		//				"AND DER.IDE_NRDER IN ("+IDE_NRDER_DECIMOS+") "+
		//"AND DER.IDE_NRRUB IN (select IDE_NRRUB from NRH_RUBRO where DECIMO_NRRUB=1) "+
		//				"ORDER BY DER.ORDEN_NRDER DESC ");
		
		TablaGenerica tab_rubros=utilitario.consultar("SELECT ide_nrder,der.ide_gereg,RUB.ide_nrrub,IDE_NRFOC,IDE_NRDTN,FORMULA_NRDER,FECHA_INICIAL_NRDER, FECHA_FINAL_NRDER,FECHA_PAGO_NRDER,ORDEN_NRDER,DECIMO_NRRUB " +
				"FROM NRH_DETALLE_RUBRO DER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"WHERE DER.IDE_NRDTN="+IDE_NRDTN+" " +
				"AND DER.ACTIVO_NRDER=true " +
				"and der.IDE_NRRUB IN (select IDE_NRRUB from NRH_RUBRO where DECIMO_NRRUB is null or DECIMO_NRRUB != true) " +
				"or IDE_NRDER in ("+IDE_NRDER_DECIMOS+") " +
				"ORDER BY DER.ORDEN_NRDER DESC ");
		//System.out.print("rub deci ");tab_rubros.imprimirSql();
		return tab_rubros;
	}

	/**Busca todos los rubros de un tipo de Nomina en orden desendente por que despues se insertaran en otra tabla
	 * @param IDE_NRDTN
	 * @return TablaGenerica  resultado
	 */
	public TablaGenerica getRubrosTipoTeclado(String IDE_NRDTN){
		//		TablaGenerica tab_rubros=utilitario.consultar("SELECT * FROM NRH_DETALLE_RUBRO WHERE IDE_NRDTN="+IDE_NRDTN+" AND ACTIVO_NRDER=true ORDER BY ORDEN_NRDER DESC");
		TablaGenerica tab_rubros=utilitario.consultar("SELECT ide_nrder,RUB.ide_nrrub,IDE_NRFOC,IDE_NRDTN,FORMULA_NRDER,FECHA_INICIAL_NRDER, " +
				"FECHA_FINAL_NRDER,FECHA_PAGO_NRDER,ORDEN_NRDER " +
				"FROM NRH_DETALLE_RUBRO DER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"WHERE DER.IDE_NRDTN="+IDE_NRDTN+" " +
				"AND DER.ACTIVO_NRDER=TRUE " +
				"AND RUB.IDE_NRFOC="+utilitario.getVariable("p_nrh_forma_calculo_teclado")+" "+
				"ORDER BY DER.ORDEN_NRDER DESC ");
		return tab_rubros;
	}

	/**Busca todos los rubros de un tipo de Nomina en orden desendente por que despues se insertaran en otra tabla
	 * @param IDE_NRDTN
	 * @return TablaGenerica  resultado
	 */
	public TablaGenerica getRubrosTipoFormula(String IDE_NRDTN,String IDE_NRDER_EXCPECION){
		//		TablaGenerica tab_rubros=utilitario.consultar("SELECT * FROM NRH_DETALLE_RUBRO WHERE IDE_NRDTN="+IDE_NRDTN+" AND ACTIVO_NRDER=true ORDER BY ORDEN_NRDER DESC");
		TablaGenerica tab_rubros=utilitario.consultar("SELECT ide_nrder,RUB.ide_nrrub,IDE_NRFOC,IDE_NRDTN,FORMULA_NRDER,FECHA_INICIAL_NRDER, " +
				"FECHA_FINAL_NRDER,FECHA_PAGO_NRDER,ORDEN_NRDER " +
				"FROM NRH_DETALLE_RUBRO DER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"WHERE DER.IDE_NRDTN="+IDE_NRDTN+" " +
				"AND DER.ACTIVO_NRDER=TRUE " +
				"AND DER.IDE_NRDER NOT IN ("+IDE_NRDER_EXCPECION+") "+
				"AND RUB.IDE_NRFOC="+utilitario.getVariable("p_nrh_forma_calculo_formula")+" "+
				"ORDER BY DER.ORDEN_NRDER ASC ");
		return tab_rubros;
	}


	/**Busca todos los empleados que pretenecen a ese detalle de tipo de nomina 
	 * @param ide_nrdtn Detalle de tipo de nomina
	 * @return TablaGenerica con los empleados de ese tipo de nomina
	 */
	public TablaGenerica getTablaEmpleadosTipoNomina(String ide_nrdtn,String fecha_fin_gepro){

		TablaGenerica tab_empleados_tipo_nomina=utilitario.consultar(getSqlEmpleadosTipoNomina(ide_nrdtn,fecha_fin_gepro)); 
		return tab_empleados_tipo_nomina;
	}


	/**Busca todos los empleados que pretenecen a ese detalle de tipo de nomina 
	 * @param ide_nrdtn Detalle de tipo de nomina
	 * @return TablaGenerica con los empleados de ese tipo de nomina
	 */
	public String getSqlEmpleadosRol(String IDE_NRROL){

		String sql="SELECT " +
				"EDP.IDE_GEEDP, " +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP ||' '|| " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) ||' '|| " +
				"EMP.PRIMER_NOMBRE_GTEMP ||' '|| " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES, " + //02
				"EDP.IDE_SUCU, "+
				"EDP.IDE_GTEMP, " + //04
				"EDP.IDE_GTTEM, " +
				"(case when EMP.PRIMER_NOMBRE_GTEMP is null then EMP.SEGUNDO_NOMBRE_GTEMP else EMP.PRIMER_NOMBRE_GTEMP end) as nombre, " + //06
				"(case when EMP.APELLIDO_PATERNO_GTEMP is null then EMP.APELLIDO_MATERNO_GTEMP else APELLIDO_PATERNO_GTEMP end) as apellido " + //07
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP " +
				"INNER JOIN NRH_DETALLE_ROL DROL ON DROL.IDE_GEEDP=EDP.IDE_GEEDP AND IDE_NRROL="+IDE_NRROL+" " +
				"INNER JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EDP.IDE_GTEMP " +
				//				"WHERE EDP.ACTIVO_GEEDP=TRUE " +
				"GROUP BY EDP.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP , " +
				"EMP.APELLIDO_MATERNO_GTEMP , " +
				"EMP.PRIMER_NOMBRE_GTEMP , " +
				"EMP.SEGUNDO_NOMBRE_GTEMP,EDP.IDE_SUCU,EDP.IDE_GTEMP, " +
				"EDP.IDE_GTTEM " +
				"ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC";
//System.out.println("sql getSqlEmpleadosRol..."+sql);
		return sql;
	}
	
	
	

	public String getSqlEmpleadosRolLiquidacionFecha(String ide_nrdtn){
		 
				String IDE_NRTIN="";
				try {
					IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");	
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (ide_nrdtn==null || ide_nrdtn.isEmpty()){
					ide_nrdtn="-1";
				}

				String sql="select ";

				sql+="a.IDE_GEEDP," +
						"a.DOCUMENTO_IDENTIDAD_GTEMP," +
						"a.NOMBRES, " +
						"a.IDE_SUCU, " +
						"a.IDE_GTEMP, " +
						"a.IDE_GTTEM, "+
						"a.FECHA_GEEDP, "+	
						"a.RMU_GEEDP, "+
						"a.AJUSTE_SUELDO_GEEDP, " +
						"a.FECHA_AJUSTE_GEEDP, "+
						"a.fecha_ingreso_grupo_gtemp, "+//awbecerra
						"a.FECHA_INGRESO_GTEMP, "+
						"a.ACUMULA_FONDOS_GEEDP, "+
						"a.IDE_GEREG, "+
						"a.FECHA_ENCARGO_GEEDP,a.SUELDO_SUBROGA_GEEDP,a.FECHA_ENCARGO_FIN_GEEDP," +
						//"b.dias_vacacion," +
						"a.DISCAPACITADO_GTEMP, "+
						"c.PORCENTAJE_GTDIE, "+  
						"a.acumula_decimo_gtemp, "+
						"a.continuidad_liquidacion_geedp  "; 
				sql+="from " +
						"( ";
				sql+="select " +
						"EDP.IDE_GEEDP," +
						"EMP.DOCUMENTO_IDENTIDAD_GTEMP," +
						"EMP.APELLIDO_PATERNO_GTEMP ||' '|| " +
						"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) ||' '|| " +
						"EMP.PRIMER_NOMBRE_GTEMP ||' '|| " +
						"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES, " +
						"EDP.IDE_SUCU, " +
						"EMP.IDE_GTEMP, " +
						"EMP.DISCAPACITADO_GTEMP, "+
						"EDP.IDE_GTTEM, emp.fecha_ingreso_grupo_gtemp,continuidad_liquidacion_geedp, ";

				if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
					sql+="EDP.FECHA_LIQUIDACION_GEEDP AS FECHA_GEEDP, ";	
				}else{
					sql+="EDP.FECHA_GEEDP, ";
				}

				sql+="EDP.RMU_GEEDP, "+
						"EDP.AJUSTE_SUELDO_GEEDP, " +
						"EDP.FECHA_AJUSTE_GEEDP, "+
						"EMP.FECHA_INGRESO_GTEMP, "+
						"EDP.ACUMULA_FONDOS_GEEDP, "+
						"EMP.ACUMULA_DECIMO_GTEMP,"+
						"DIP.IDE_GEREG, "+
						"EDP.FECHA_ENCARGO_GEEDP,EDP.SUELDO_SUBROGA_GEEDP,EDP.FECHA_ENCARGO_FIN_GEEDP "+
						"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
						"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM " +
						"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP "+
						"inner join SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU " +
						"LEFT JOIN GEN_DIVISION_POLITICA DIP ON DIP.IDE_GEDIP=SUC.IDE_GEDIP "+
						"WHERE DTN.IDE_NRDTN="+ide_nrdtn+" " +
						"and fecha_liquidacion_geedp between '"+utilitario.getVariable("p_fecha_inicio_liquidar")+"' and '"+utilitario.getVariable("p_fecha_fin_liquidar")+"' "+
						//"and fecha_geedp <= to_date('"+fecha_fin_gepro+"','yyyy-mm-dd') "+ 
						" ";
				if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
					sql+="AND EDP.LIQUIDACION_GEEDP=1 "+
							"and EDP.EJECUTO_LIQUIDACION_GEEDP=1 ";

				}else{
					sql+="AND (EDP.LIQUIDACION_GEEDP=1) ";
				}

				sql+="ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC,EMP.APELLIDO_MATERNO_GTEMP ASC ";
				sql+=" )a  "
						+ "left join ( "
						+ "select IDE_GTEMP,PORCENTAJE_GTDIE from GTH_DISCAPACIDAD_EMPLEADO where ide_gtemp in ( "
						+ "select IDE_GTEMP from GTH_EMPLEADO  where DISCAPACITADO_GTEMP=TRUE)"
						+ ")c on c.ide_gtemp=a.ide_gtemp "
						+ "order by nombres";
		
				return sql;
			}
	
	
	public String getSqlEmpleadosRolLiquidacionImpuestoRenta(String ide_nrrol){
		 
		String ide_nrdtn=getRol(ide_nrrol).getValor("ide_nrdtn");
		String IDE_NRTIN="";
		try {
			IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");	
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (ide_nrdtn==null || ide_nrdtn.isEmpty()){
			ide_nrdtn="-1";
		}

		String sql="select ";

		sql+="a.IDE_GEEDP," +
				"a.DOCUMENTO_IDENTIDAD_GTEMP," +
				"a.NOMBRES, " +
				"a.IDE_SUCU, " +
				"a.IDE_GTEMP, " +
				"a.IDE_GTTEM, "+
				"a.FECHA_GEEDP, "+	
				"a.RMU_GEEDP, "+
				"a.AJUSTE_SUELDO_GEEDP, " +
				"a.FECHA_AJUSTE_GEEDP, "+
				"a.fecha_ingreso_grupo_gtemp, "+//awbecerra
				"a.FECHA_INGRESO_GTEMP, "+
				"a.ACUMULA_FONDOS_GEEDP, "+
				"a.IDE_GEREG, "+
				"a.FECHA_ENCARGO_GEEDP, " +
				"a.SUELDO_SUBROGA_GEEDP, " +
				"a.FECHA_ENCARGO_FIN_GEEDP, " +
				"a.DISCAPACITADO_GTEMP, "
				+ "acumula_decimo_gtemp, "+
				"c.PORCENTAJE_GTDIE,a.continuidad_liquidacion_geedp,a.fecha_finctr_geedp ";
		sql+="from " +
				"( ";


		sql+="select " +
				"EDP.IDE_GEEDP," +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP," +
				"EMP.APELLIDO_PATERNO_GTEMP ||' '|| " +
				"( case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)||' '|| " +
				"EMP.PRIMER_NOMBRE_GTEMP ||' '|| " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES, " +
				"EDP.IDE_SUCU, " +
				"EMP.IDE_GTEMP, " +
				"EMP.DISCAPACITADO_GTEMP, acumula_decimo_gtemp,"+
				"EDP.IDE_GTTEM, emp.fecha_ingreso_grupo_gtemp,edp.continuidad_liquidacion_geedp,edp.fecha_finctr_geedp, ";

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="EDP.FECHA_LIQUIDACION_GEEDP AS FECHA_GEEDP, ";	
		}else{
	
		}

		sql+="EDP.RMU_GEEDP, "+
				"EDP.AJUSTE_SUELDO_GEEDP, " +
				"EDP.FECHA_AJUSTE_GEEDP, "+
				"EMP.FECHA_INGRESO_GTEMP, "+
				"EDP.ACUMULA_FONDOS_GEEDP, "+
				"DIP.IDE_GEREG, "+
				"EDP.FECHA_ENCARGO_GEEDP,EDP.SUELDO_SUBROGA_GEEDP,EDP.FECHA_ENCARGO_FIN_GEEDP "+

				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP " +
				"inner join SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU " +
				"LEFT JOIN GEN_DIVISION_POLITICA DIP ON DIP.IDE_GEDIP=SUC.IDE_GEDIP " +
				"where EDP.IDE_GEEDP in ( " +
				"select EDP.IDE_GEEDP " +
				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join NRH_DETALLE_ROL detrol on edp.IDE_GEEDP=DETROL.IDE_GEEDP " +
				"inner join NRH_ROL rol on rol.ide_nrrol=DETROL.IDE_nrrol " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM and dtn.ide_nrdtn=rol.ide_nrdtn " +
				"WHERE detrol.IDE_NRROL="+ide_nrrol+" AND EDP.LIQUIDACION_GEEDP=1 "+
				"and EDP.EJECUTO_LIQUIDACION_GEEDP=1 GROUP BY edp.IDE_GEEDP ) " +
				"ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC,EMP.APELLIDO_MATERNO_GTEMP ASC) a "
				+ "left join ( select IDE_GTEMP,PORCENTAJE_GTDIE from GTH_DISCAPACIDAD_EMPLEADO  "
				+ "where ide_gtemp in ( select IDE_GTEMP from GTH_EMPLEADO  where DISCAPACITADO_GTEMP=TRUE) )c on c.ide_gtemp=a.ide_gtemp  order by nombres";

		System.out.println(sql);
		return sql;
	}


	/**Busca todos los empleados que pretenecen a ese detalle de tipo de nomina 
	 * @param ide_nrdtn Detalle de tipo de nomina
	 * @return TablaGenerica con los empleados de ese tipo de nomina
	 */
	public String getSqlEmpleadosTipoNomina(String ide_nrdtn,String ide_geedp,String fecha_fin_gepro){
		String sql="select * from ( ";
		sql+=getSqlEmpleadosTipoNomina(ide_nrdtn, fecha_fin_gepro);
		//sql+=" )a where a.ide_geedp in ("+ide_geedp+") ";
		return sql;
	}

	/**Busca todos los empleados que pretenecen a ese detalle de tipo de nomina 
	 * @param ide_nrdtn Detalle de tipo de nomina
	 * @return TablaGenerica con los empleados de ese tipo de nomina
	 */
	public String getSqlEmpleadosTipoNominaLiquidacion(String ide_nrdtn,String ide_geedp,String fecha_fin_gepro){
		String sql="select * from ( ";
		sql+=getSqlEmpleadosTipoNominaLiquidacion(ide_nrdtn, fecha_fin_gepro);
		
		if (utilitario.getVariable("p_nrh_empleados_liquidacion").toString().equalsIgnoreCase("VACIO") || utilitario.getVariable("p_nrh_empleados_liquidacion").isEmpty()) {
			sql+=" )a ";
		}else {
			   sql+= ")a where a.ide_geedp in ("+utilitario.getVariable("p_nrh_empleados_liquidacion")+") ";
		}
		
	   return sql;
	}



	
	
	
	
	
	public String getSqlEmpleadosRolLiquidacionRecalcular(String ide_nrrol){
		 
		
		
		
		String ide_nrdtn=getRol(ide_nrrol).getValor("ide_nrdtn");
		String IDE_NRTIN="";
		try {
			IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");	
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (ide_nrdtn==null || ide_nrdtn.isEmpty()){
			ide_nrdtn="-1";
		}
		
		
		
		String sql="select ";

		sql+="a.IDE_GEEDP," +
				"a.DOCUMENTO_IDENTIDAD_GTEMP," +
				"a.NOMBRES, " +
				"a.IDE_SUCU, " +
				"a.IDE_GTEMP, " +
				"a.IDE_GTTEM, "+
				"a.FECHA_GEEDP, "+	
				"a.RMU_GEEDP, "+
				"a.AJUSTE_SUELDO_GEEDP, " +
				"a.FECHA_AJUSTE_GEEDP, "+
				"a.fecha_ingreso_grupo_gtemp, "+//awbecerra
				"a.FECHA_INGRESO_GTEMP, "+
				"a.ACUMULA_FONDOS_GEEDP, "+
				"a.IDE_GEREG, "+
				"a.FECHA_ENCARGO_GEEDP,a.SUELDO_SUBROGA_GEEDP,a.FECHA_ENCARGO_FIN_GEEDP," +
				//"b.dias_vacacion," +
				"a.DISCAPACITADO_GTEMP, "+
				"c.PORCENTAJE_GTDIE, "+  
				"a.acumula_decimo_gtemp, "+
				"a.continuidad_liquidacion_geedp  "; 
		sql+="from " +
				"( ";
		sql+="select " +
				"EDP.IDE_GEEDP," +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP," +
				"EMP.APELLIDO_PATERNO_GTEMP ||' '|| " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) ||' '|| " +
				"EMP.PRIMER_NOMBRE_GTEMP ||' '|| " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES, " +
				"EDP.IDE_SUCU, " +
				"EMP.IDE_GTEMP, " +
				"EMP.DISCAPACITADO_GTEMP, "+
				"EDP.IDE_GTTEM, emp.fecha_ingreso_grupo_gtemp,continuidad_liquidacion_geedp, ";

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="EDP.FECHA_LIQUIDACION_GEEDP AS FECHA_GEEDP, ";	
		}else{
			sql+="EDP.FECHA_GEEDP, ";
		}

		sql+="EDP.RMU_GEEDP, "+
				"EDP.AJUSTE_SUELDO_GEEDP, " +
				"EDP.FECHA_AJUSTE_GEEDP, "+
				"EMP.FECHA_INGRESO_GTEMP, "+
				"EDP.ACUMULA_FONDOS_GEEDP, "+
				"EMP.ACUMULA_DECIMO_GTEMP,"+
				"DIP.IDE_GEREG, "+
				"EDP.FECHA_ENCARGO_GEEDP,EDP.SUELDO_SUBROGA_GEEDP,EDP.FECHA_ENCARGO_FIN_GEEDP "+
				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM " +
				"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP "+
				"inner join SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU " +
				"LEFT JOIN GEN_DIVISION_POLITICA DIP ON DIP.IDE_GEDIP=SUC.IDE_GEDIP "+
				"WHERE DTN.IDE_NRDTN="+ide_nrdtn+" " +
				"and fecha_liquidacion_geedp between '"+utilitario.getVariable("p_fecha_inicio_liquidar")+"' and '"+utilitario.getFechaActual()+"' "+
				//"and fecha_geedp <= to_date('"+fecha_fin_gepro+"','yyyy-mm-dd') "+ 
				" ";
		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="AND EDP.LIQUIDACION_GEEDP=1 "+
					"and EDP.EJECUTO_LIQUIDACION_GEEDP=1 ";

		}else{
			sql+="AND (EDP.LIQUIDACION_GEEDP=1) ";
		}

		sql+="ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC,EMP.APELLIDO_MATERNO_GTEMP ASC ";
		sql+=" )a  "
				+ "left join ( "
				+ "select IDE_GTEMP,PORCENTAJE_GTDIE from GTH_DISCAPACIDAD_EMPLEADO where ide_gtemp in ( "
				+ "select IDE_GTEMP from GTH_EMPLEADO  where DISCAPACITADO_GTEMP=TRUE)"
				+ ")c on c.ide_gtemp=a.ide_gtemp "
				+ "order by nombres";

		return sql;
	}

	
	
	
	
	
	
	
	
	
	
	

	/**Busca todos los empleados que pretenecen a ese detalle de tipo de nomina 
	 * @param ide_nrdtn Detalle de tipo de nomina
	 * @return TablaGenerica con los empleados de ese tipo de nomina
	 */
	public String getSqlEmpleadosNomina(String ide_nrrol){
		String ide_nrdtn=getRol(ide_nrrol).getValor("ide_nrdtn");
		String IDE_NRTIN="";
		try {
			IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");	
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (ide_nrdtn==null || ide_nrdtn.isEmpty()){
			ide_nrdtn="-1";
		}

		String sql="select ";

		sql+="a.IDE_GEEDP," +
				"a.DOCUMENTO_IDENTIDAD_GTEMP," +
				"a.NOMBRES, " +
				"a.IDE_SUCU, " +
				"a.IDE_GTEMP, " +
				"a.IDE_GTTEM, "+
				"a.FECHA_GEEDP, "+	
				"a.RMU_GEEDP, "+
				"a.AJUSTE_SUELDO_GEEDP, " +
				"a.FECHA_AJUSTE_GEEDP, "+
				"a.fecha_ingreso_grupo_gtemp, "+//awbecerra
				"a.FECHA_INGRESO_GTEMP, "+
				"a.ACUMULA_FONDOS_GEEDP, "+
				"a.IDE_GEREG, "+
				"a.FECHA_ENCARGO_GEEDP, " +
				"a.SUELDO_SUBROGA_GEEDP, " +
				"a.FECHA_ENCARGO_FIN_GEEDP, " +
				"b.dias_vacacion, " +
				"a.DISCAPACITADO_GTEMP, acumula_decimo_gtemp, "+
				"c.PORCENTAJE_GTDIE ";
		sql+="from " +
				"( ";


		sql+="select " +
				"EDP.IDE_GEEDP," +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP," +
				"EMP.APELLIDO_PATERNO_GTEMP ||' '|| " +
				"( case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)||' '|| " +
				"EMP.PRIMER_NOMBRE_GTEMP ||' '|| " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES, " +
				"EDP.IDE_SUCU, " +
				"EMP.IDE_GTEMP, " +
				"EMP.DISCAPACITADO_GTEMP, acumula_decimo_gtemp,"+
				"EDP.IDE_GTTEM, emp.fecha_ingreso_grupo_gtemp, ";

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="EDP.FECHA_LIQUIDACION_GEEDP AS FECHA_GEEDP, ";	
		}else{
			sql+="EDP.FECHA_GEEDP, ";
		}

		sql+="EDP.RMU_GEEDP, "+
				"EDP.AJUSTE_SUELDO_GEEDP, " +
				"EDP.FECHA_AJUSTE_GEEDP, "+
				"EMP.FECHA_INGRESO_GTEMP, "+
				"EDP.ACUMULA_FONDOS_GEEDP, "+
				"DIP.IDE_GEREG, "+
				"EDP.FECHA_ENCARGO_GEEDP,EDP.SUELDO_SUBROGA_GEEDP,EDP.FECHA_ENCARGO_FIN_GEEDP "+

				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP " +
				"inner join SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU " +
				"LEFT JOIN GEN_DIVISION_POLITICA DIP ON DIP.IDE_GEDIP=SUC.IDE_GEDIP " +
				"where EDP.IDE_GEEDP in ( " +
				"select EDP.IDE_GEEDP " +
				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join NRH_DETALLE_ROL detrol on edp.IDE_GEEDP=DETROL.IDE_GEEDP " +
				"inner join NRH_ROL rol on rol.ide_nrrol=DETROL.IDE_nrrol " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM and dtn.ide_nrdtn=rol.ide_nrdtn " +
				"WHERE detrol.IDE_NRROL="+ide_nrrol+" ";

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="AND EDP.LIQUIDACION_GEEDP=1 "+
					"and (EDP.EJECUTO_LIQUIDACION_GEEDP IS NULL OR EDP.EJECUTO_LIQUIDACION_GEEDP!=1) ";

		}else{
			sql+="AND (EDP.LIQUIDACION_GEEDP IS NULL OR EDP.LIQUIDACION_GEEDP!=1) ";
		}

		sql+="GROUP BY edp.IDE_GEEDP ) " +
				"ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC,EMP.APELLIDO_MATERNO_GTEMP ASC  ";

		sql+=")a " +
				"left join " +
				"( " +
				"SELECT IDE_ASVAC,IDE_GTEMP,DIAS_VACACION FROM ( " +
				"SELECT a.IDE_ASVAC,edp.IDE_GTEMP, " +
				"(DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_VACACION " +
				"FROM ( " +
				"SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, " +
				"(case when SUM(DIA_ADICIONAL_ASDEV) is null then 0 else SUM(DIA_ADICIONAL_ASDEV) end) as NRO_DIAS_ADICIONAL, " +
				"(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO, " +
				"(case when SUM(DIA_SOLICITADO_ASDEV) is null then 0 else SUM(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO " +
				"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = TRUE GROUP BY IDE_ASVAC " +
				") a " +
				"inner join ASI_VACACION vac on VAC.IDE_ASVAC=a.IDE_ASVAC " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR edp on edp.ide_gtemp=vac.ide_gtemp  " +
				"and edp.ide_geedp in ( " +
				"select EDP.IDE_GEEDP " +
				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join NRH_DETALLE_ROL detrol on edp.IDE_GEEDP=DETROL.IDE_GEEDP " +
				"inner join NRH_ROL rol on rol.ide_nrrol=DETROL.IDE_nrrol " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM and dtn.ide_nrdtn=rol.ide_nrdtn " +
				"WHERE detrol.IDE_NRROL="+ide_nrrol+" ";

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="AND EDP.LIQUIDACION_GEEDP=1 "+
					"and (EDP.EJECUTO_LIQUIDACION_GEEDP IS NULL OR EDP.EJECUTO_LIQUIDACION_GEEDP!=1) ";

		}else{
			sql+="AND (EDP.LIQUIDACION_GEEDP IS NULL OR EDP.LIQUIDACION_GEEDP!=1) ";
		}

		sql+="GROUP BY edp.IDE_GEEDP ) " +

				") a " +
				"GROUP BY IDE_ASVAC,IDE_GTEMP,DIAS_VACACION  " +

				")b  " +
				"on a.ide_gtemp=b.ide_gtemp "+
				"left join ( " +
				"select IDE_GTEMP,PORCENTAJE_GTDIE from GTH_DISCAPACIDAD_EMPLEADO where ide_gtemp in ( " +
				"select IDE_GTEMP from GTH_EMPLEADO  where DISCAPACITADO_GTEMP=TRUE) " +
				")c on c.ide_gtemp=a.ide_gtemp and c.ide_gtemp=b.ide_gtemp " +
				"order by nombres ";

		System.out.println(sql);
		return sql;
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	public String getSqlEmpleadosNominaLiquidacion(String ide_nrrol){
		
		
		String ide_nrdtn=getRol(ide_nrrol).getValor("ide_nrdtn");
		String IDE_NRTIN="";
		try {
			IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");	
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (ide_nrdtn==null || ide_nrdtn.isEmpty()){
			ide_nrdtn="-1";
		}

		String sql="select ";

		sql+="a.IDE_GEEDP," +
				"a.DOCUMENTO_IDENTIDAD_GTEMP," +
				"a.NOMBRES, " +
				"a.IDE_SUCU, " +
				"a.IDE_GTEMP, " +
				"a.IDE_GTTEM, "+
				"a.FECHA_GEEDP, "+	
				"a.RMU_GEEDP, "+
				"a.AJUSTE_SUELDO_GEEDP, " +
				"a.FECHA_AJUSTE_GEEDP, "+
				"a.fecha_ingreso_grupo_gtemp, "+//awbecerra
				"a.FECHA_INGRESO_GTEMP, "+
				"a.ACUMULA_FONDOS_GEEDP, "+
				"a.IDE_GEREG, "+
				"a.FECHA_ENCARGO_GEEDP, " +
				"a.SUELDO_SUBROGA_GEEDP, " +
				"a.FECHA_ENCARGO_FIN_GEEDP, " +
				"a.DISCAPACITADO_GTEMP, "
				+ "acumula_decimo_gtemp, "+
				"c.PORCENTAJE_GTDIE,a.continuidad_liquidacion_geedp ";
		sql+="from " +
				"( ";


		sql+="select " +
				"EDP.IDE_GEEDP," +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP," +
				"EMP.APELLIDO_PATERNO_GTEMP ||' '|| " +
				"( case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)||' '|| " +
				"EMP.PRIMER_NOMBRE_GTEMP ||' '|| " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES, " +
				"EDP.IDE_SUCU, " +
				"EMP.IDE_GTEMP, " +
				"EMP.DISCAPACITADO_GTEMP, acumula_decimo_gtemp,"+
				"EDP.IDE_GTTEM, emp.fecha_ingreso_grupo_gtemp,edp.continuidad_liquidacion_geedp, ";

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="EDP.FECHA_LIQUIDACION_GEEDP AS FECHA_GEEDP, ";	
		}else{
			sql+="EDP.FECHA_GEEDP, ";
		}

		sql+="EDP.RMU_GEEDP, "+
				"EDP.AJUSTE_SUELDO_GEEDP, " +
				"EDP.FECHA_AJUSTE_GEEDP, "+
				"EMP.FECHA_INGRESO_GTEMP, "+
				"EDP.ACUMULA_FONDOS_GEEDP, "+
				"DIP.IDE_GEREG, "+
				"EDP.FECHA_ENCARGO_GEEDP,EDP.SUELDO_SUBROGA_GEEDP,EDP.FECHA_ENCARGO_FIN_GEEDP "+

				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP " +
				"inner join SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU " +
				"LEFT JOIN GEN_DIVISION_POLITICA DIP ON DIP.IDE_GEDIP=SUC.IDE_GEDIP " +
				"where EDP.IDE_GEEDP in ( " +
				"select EDP.IDE_GEEDP " +
				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join NRH_DETALLE_ROL detrol on edp.IDE_GEEDP=DETROL.IDE_GEEDP " +
				"inner join NRH_ROL rol on rol.ide_nrrol=DETROL.IDE_nrrol " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM and dtn.ide_nrdtn=rol.ide_nrdtn " +
				"WHERE detrol.IDE_NRROL="+ide_nrrol+" ";

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="AND EDP.LIQUIDACION_GEEDP=1 "+
					"and (EDP.EJECUTO_LIQUIDACION_GEEDP IS NULL OR EDP.EJECUTO_LIQUIDACION_GEEDP!=1) ";

		}else{
			sql+="AND (EDP.LIQUIDACION_GEEDP IS NULL OR EDP.LIQUIDACION_GEEDP!=1) ";
		}

		sql+="GROUP BY edp.IDE_GEEDP ) " +
				"ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC,EMP.APELLIDO_MATERNO_GTEMP ASC) a "
				+ "left join ( select IDE_GTEMP,PORCENTAJE_GTDIE from GTH_DISCAPACIDAD_EMPLEADO  "
				+ "where ide_gtemp in ( select IDE_GTEMP from GTH_EMPLEADO  where DISCAPACITADO_GTEMP=TRUE) )c on c.ide_gtemp=a.ide_gtemp  order by nombres";

		/*sql+=")a " +
				"left join " +
				"( " +
				"SELECT IDE_ASVAC,IDE_GTEMP,DIAS_VACACION FROM ( " +
				"SELECT a.IDE_ASVAC,edp.IDE_GTEMP, " +
				"(DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_VACACION " +
				"FROM ( " +
				"SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, " +
				"(case when SUM(DIA_ADICIONAL_ASDEV) is null then 0 else SUM(DIA_ADICIONAL_ASDEV) end) as NRO_DIAS_ADICIONAL, " +
				"(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO, " +
				"(case when SUM(DIA_SOLICITADO_ASDEV) is null then 0 else SUM(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO " +
				"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = TRUE GROUP BY IDE_ASVAC " +
				") a " +
				"inner join ASI_VACACION vac on VAC.IDE_ASVAC=a.IDE_ASVAC " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR edp on edp.ide_gtemp=vac.ide_gtemp  " +
				"and edp.ide_geedp in ( " +
				"select EDP.IDE_GEEDP " +
				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join NRH_DETALLE_ROL detrol on edp.IDE_GEEDP=DETROL.IDE_GEEDP " +
				"inner join NRH_ROL rol on rol.ide_nrrol=DETROL.IDE_nrrol " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM and dtn.ide_nrdtn=rol.ide_nrdtn " +
				"WHERE detrol.IDE_NRROL="+ide_nrrol+" ";

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="AND EDP.LIQUIDACION_GEEDP=1 "+
					"and (EDP.EJECUTO_LIQUIDACION_GEEDP IS NULL OR EDP.EJECUTO_LIQUIDACION_GEEDP!=1) ";

		}else{
			sql+="AND (EDP.LIQUIDACION_GEEDP IS NULL OR EDP.LIQUIDACION_GEEDP!=1) ";
		}

		sql+="GROUP BY edp.IDE_GEEDP ) " +

				") a " +
				"GROUP BY IDE_ASVAC,IDE_GTEMP,DIAS_VACACION  " +

				")b  " +
				"on a.ide_gtemp=b.ide_gtemp "+
				"left join ( " +
				"select IDE_GTEMP,PORCENTAJE_GTDIE from GTH_DISCAPACIDAD_EMPLEADO where ide_gtemp in ( " +
				"select IDE_GTEMP from GTH_EMPLEADO  where DISCAPACITADO_GTEMP=TRUE) " +
				")c on c.ide_gtemp=a.ide_gtemp and c.ide_gtemp=b.ide_gtemp " +
				"order by nombres ";
*/
		System.out.println(sql);
		return sql;
		
	}
	
	
	
	/**Busca todos los empleados que pretenecen a ese detalle de tipo de nomina con contrato activo 
	 * @param ide_nrdtn Detalle de tipo de nomina
	 * @return TablaGenerica con los empleados de ese tipo de nomina
	 */
	
	
	
	public String getSqlEmpleadosTipoNomina(String ide_nrdtn,String fecha_fin_gepro){
		String IDE_NRTIN="";
		try {
			IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");	
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (ide_nrdtn==null || ide_nrdtn.isEmpty()){
			ide_nrdtn="-1";
		}

		String sql="select ";

		sql+="a.IDE_GEEDP," +
				"a.DOCUMENTO_IDENTIDAD_GTEMP," +
				"a.NOMBRES, " +
				"a.IDE_SUCU, " +
				"a.IDE_GTEMP, " +
				"a.IDE_GTTEM, "+
				"a.FECHA_GEEDP, "+	
				"a.RMU_GEEDP, "+
				"a.AJUSTE_SUELDO_GEEDP, " +
				"a.FECHA_AJUSTE_GEEDP, "+
				"a.fecha_ingreso_grupo_gtemp, "+//awbecerra
				"a.FECHA_INGRESO_GTEMP, "+
				"a.ACUMULA_FONDOS_GEEDP, "+
				"a.IDE_GEREG, "+
				"a.FECHA_ENCARGO_GEEDP, " +
				"a.SUELDO_SUBROGA_GEEDP, " +
				"a.FECHA_ENCARGO_FIN_GEEDP, " +
				"b.dias_vacacion, " +
				"a.DISCAPACITADO_GTEMP,  "+ 
				"a.ACUMULA_DECIMO_GTEMP,  "+ 
				"c.PORCENTAJE_GTDIE ";
		sql+="from " +
				"( ";
		sql+="select " +
				"EDP.IDE_GEEDP," +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP," +
				"EMP.APELLIDO_PATERNO_GTEMP ||' '|| " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) ||' '|| " +
				"EMP.PRIMER_NOMBRE_GTEMP ||' '|| " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end)  AS NOMBRES, " +
				"EDP.IDE_SUCU, " +
				"EMP.IDE_GTEMP, " +
				"EMP.DISCAPACITADO_GTEMP, "+
				"EDP.IDE_GTTEM, emp.fecha_ingreso_grupo_gtemp,  ";

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="EDP.FECHA_LIQUIDACION_GEEDP AS FECHA_GEEDP, ";	
		}else{
			sql+="EDP.FECHA_GEEDP, ";
		}

		sql+="EDP.RMU_GEEDP, "+
				"EDP.AJUSTE_SUELDO_GEEDP, " +
				"EDP.FECHA_AJUSTE_GEEDP, "+
				"EMP.FECHA_INGRESO_GTEMP, EMP.ACUMULA_DECIMO_GTEMP, "+
				"EDP.ACUMULA_FONDOS_GEEDP, "+
				"DIP.IDE_GEREG, "+
				"EDP.FECHA_ENCARGO_GEEDP,EDP.SUELDO_SUBROGA_GEEDP,EDP.FECHA_ENCARGO_FIN_GEEDP "+
				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM " +
				"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP "+
				"inner join SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU " +
				"LEFT JOIN GEN_DIVISION_POLITICA DIP ON DIP.IDE_GEDIP=SUC.IDE_GEDIP "+
				"WHERE DTN.IDE_NRDTN="+ide_nrdtn+" " +
				"and fecha_geedp <= to_date('"+fecha_fin_gepro+"','yyyy-mm-dd') ";
				//"AND EDP.ACTIVO_GEEDP=TRUE ";
		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="AND EDP.LIQUIDACION_GEEDP=1 "+
					"and (EDP.EJECUTO_LIQUIDACION_GEEDP IS NULL OR EDP.EJECUTO_LIQUIDACION_GEEDP!=1) ";

		}else{
			sql+="AND (EDP.LIQUIDACION_GEEDP IS NULL OR EDP.LIQUIDACION_GEEDP!=1) ";
		}

		sql+="ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC,EMP.APELLIDO_MATERNO_GTEMP ASC ";

		sql+=")a " +
				"left join " +
				"( " +
				"SELECT IDE_ASVAC,IDE_GTEMP,DIAS_VACACION FROM ( " +
				"SELECT a.IDE_ASVAC,edp.IDE_GTEMP, " +
				"(DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_VACACION " +
				"FROM ( " +
				"SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, " +
				"(case when SUM(DIA_ADICIONAL_ASDEV) is null then 0 else SUM(DIA_ADICIONAL_ASDEV) end) as NRO_DIAS_ADICIONAL, " +
				"(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO, " +
				"(case when SUM(DIA_SOLICITADO_ASDEV) is null then 0 else SUM(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO " +
				"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = TRUE GROUP BY IDE_ASVAC " +
				") a " +
				"inner join ASI_VACACION vac on VAC.IDE_ASVAC=a.IDE_ASVAC " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR edp on edp.ide_gtemp=vac.ide_gtemp  and edp.activo_geedp=TRUE and vac.ACTIVO_ASVAC=TRUE " +
				") a " +
				"GROUP BY IDE_ASVAC,IDE_GTEMP,DIAS_VACACION  " +
				")b  " +
				"on a.ide_gtemp=b.ide_gtemp "+
				"left join ( " +
				"select IDE_GTEMP,PORCENTAJE_GTDIE from GTH_DISCAPACIDAD_EMPLEADO where ide_gtemp in ( " +
				"select IDE_GTEMP from GTH_EMPLEADO  where DISCAPACITADO_GTEMP=TRUE) " +
				")c on c.ide_gtemp=a.ide_gtemp and c.ide_gtemp=b.ide_gtemp " +
				"order by nombres ";

		System.out.println(sql);
		
		return sql;
	}


	
	
	
	


	/**Busca todos los empleados que pretenecen a ese detalle de tipo de nomina 
	 * @param ide_nrdtn Detalle de tipo de nomina
	 * @return TablaGenerica con los empleados de ese tipo de nomina
	 */
	public String getSqlEmpleadosTipoNominaLiquidacion(String ide_nrdtn,String fecha_fin_gepro){
		String IDE_NRTIN="";
		try {
			IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");	
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (ide_nrdtn==null || ide_nrdtn.isEmpty()){
			ide_nrdtn="-1";
		}

		String sql="select ";

		sql+="a.IDE_GEEDP," +
				"a.DOCUMENTO_IDENTIDAD_GTEMP," +
				"a.NOMBRES, " +
				"a.IDE_SUCU, " +
				"a.IDE_GTEMP, " +
				"a.IDE_GTTEM, "+
				"a.FECHA_GEEDP, "+	
				"a.RMU_GEEDP, "+
				"a.AJUSTE_SUELDO_GEEDP, " +
				"a.FECHA_AJUSTE_GEEDP, "+
				"a.fecha_ingreso_grupo_gtemp, "+//awbecerra
				"a.FECHA_INGRESO_GTEMP, "+
				"a.ACUMULA_FONDOS_GEEDP, "+
				"a.IDE_GEREG, "+
				"a.FECHA_ENCARGO_GEEDP,a.SUELDO_SUBROGA_GEEDP,a.FECHA_ENCARGO_FIN_GEEDP," +
				//"b.dias_vacacion," +
				"a.DISCAPACITADO_GTEMP, "+
				"c.PORCENTAJE_GTDIE, "+  
				"a.acumula_decimo_gtemp,a.continuidad_liquidacion_geedp "; 
		sql+="from " +
				"( ";
		sql+="select " +
				"EDP.IDE_GEEDP," +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP," +
				"EMP.APELLIDO_PATERNO_GTEMP ||' '|| " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) ||' '|| " +
				"EMP.PRIMER_NOMBRE_GTEMP ||' '|| " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES, " +
				"EDP.IDE_SUCU, " +
				"EMP.IDE_GTEMP, " +
				"EMP.DISCAPACITADO_GTEMP, "+
				"EDP.IDE_GTTEM, emp.fecha_ingreso_grupo_gtemp,edp.continuidad_liquidacion_geedp, ";

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="EDP.FECHA_LIQUIDACION_GEEDP AS FECHA_GEEDP, ";	
		}else{
			sql+="EDP.FECHA_GEEDP, ";
		}

		sql+="EDP.RMU_GEEDP, "+
				"EDP.AJUSTE_SUELDO_GEEDP, " +
				"EDP.FECHA_AJUSTE_GEEDP, "+
				"EMP.FECHA_INGRESO_GTEMP, "+
				"EDP.ACUMULA_FONDOS_GEEDP, "+
				"EMP.ACUMULA_DECIMO_GTEMP,"+
				"DIP.IDE_GEREG, "+
				"EDP.FECHA_ENCARGO_GEEDP,EDP.SUELDO_SUBROGA_GEEDP,EDP.FECHA_ENCARGO_FIN_GEEDP "+
				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM " +
				"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP "+
				"inner join SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU " +
				"LEFT JOIN GEN_DIVISION_POLITICA DIP ON DIP.IDE_GEDIP=SUC.IDE_GEDIP "+
				"WHERE DTN.IDE_NRDTN="+ide_nrdtn+" " +
				"and fecha_liquidacion_geedp between '"+utilitario.getVariable("p_fecha_inicio_liquidar")+"' and '"+utilitario.getVariable("p_fecha_fin_liquidar")+"' "+
				//"and fecha_geedp <= to_date('"+fecha_fin_gepro+"','yyyy-mm-dd') "+ 
				" ";
		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="AND EDP.LIQUIDACION_GEEDP=1 "+
					"and (EDP.EJECUTO_LIQUIDACION_GEEDP IS NULL OR EDP.EJECUTO_LIQUIDACION_GEEDP!=1) ";

		}else{
			sql+="AND (EDP.LIQUIDACION_GEEDP IS NULL OR EDP.LIQUIDACION_GEEDP!=1) ";
		}

		sql+="ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC,EMP.APELLIDO_MATERNO_GTEMP ASC ";
		sql+=" )a  "
				+ "left join ( "
				+ "select IDE_GTEMP,PORCENTAJE_GTDIE from GTH_DISCAPACIDAD_EMPLEADO where ide_gtemp in ( "
				+ "select IDE_GTEMP from GTH_EMPLEADO  where DISCAPACITADO_GTEMP=TRUE)"
				+ ")c on c.ide_gtemp=a.ide_gtemp "
				+ "order by nombres";
		/*sql+=")a " +
				"left join " +
				"( " +
				"SELECT IDE_ASVAC,IDE_GTEMP,DIAS_VACACION FROM ( " +
				"SELECT a.IDE_ASVAC,edp.IDE_GTEMP, " +
				"(DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_VACACION " +
				"FROM ( " +
				"SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, " +
				"(case when SUM(DIA_ADICIONAL_ASDEV) is null then 0 else SUM(DIA_ADICIONAL_ASDEV) end) as NRO_DIAS_ADICIONAL, " +
				"(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO, " +
				"(case when SUM(DIA_SOLICITADO_ASDEV) is null then 0 else SUM(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO " +
				"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = TRUE GROUP BY IDE_ASVAC " +
				") a " +
				"inner join ASI_VACACION vac on VAC.IDE_ASVAC=a.IDE_ASVAC " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR edp on edp.ide_gtemp=vac.ide_gtemp  and vac.ACTIVO_ASVAC=TRUE " +
				") a " +
				"GROUP BY IDE_ASVAC,IDE_GTEMP,DIAS_VACACION  " +
				")b  " +
				"on a.ide_gtemp=b.ide_gtemp "+
				"left join ( " +
				"select IDE_GTEMP,PORCENTAJE_GTDIE from GTH_DISCAPACIDAD_EMPLEADO where ide_gtemp in ( " +
				"select IDE_GTEMP from GTH_EMPLEADO  where DISCAPACITADO_GTEMP=TRUE) " +
				")c on c.ide_gtemp=a.ide_gtemp and c.ide_gtemp=b.ide_gtemp " +
				"order by nombres ";*/

		return sql;
	}
	
	/**Busca todos los empleados que pretenecen a ese detalle de tipo de nomina 
	 * @param ide_nrdtn Detalle de tipo de nomina
	 * @return TablaGenerica con los empleados de ese tipo de nomina
	 */
	public String getSqlEmpleadosTipoNominaLiquidacionCodigo(String ide_nrdtn,String fecha_fin_gepro){
		String IDE_NRTIN="";
		try {
			IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");	
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (ide_nrdtn==null || ide_nrdtn.isEmpty()){
			ide_nrdtn="-1";
		}

		String sql="select ";

		sql+="a.IDE_GEEDP," +
				"a.DOCUMENTO_IDENTIDAD_GTEMP," +
				"a.NOMBRES, " +
				"a.IDE_SUCU, " +
				"a.IDE_GTEMP, " +
				"a.IDE_GTTEM, "+
				"a.FECHA_GEEDP, "+	
				"a.RMU_GEEDP, "+
				"a.AJUSTE_SUELDO_GEEDP, " +
				"a.FECHA_AJUSTE_GEEDP, "+
				"a.fecha_ingreso_grupo_gtemp, "+//awbecerra
				"a.FECHA_INGRESO_GTEMP, "+
				"a.ACUMULA_FONDOS_GEEDP, "+
				"a.IDE_GEREG, "+
				"a.FECHA_ENCARGO_GEEDP,a.SUELDO_SUBROGA_GEEDP,a.FECHA_ENCARGO_FIN_GEEDP," +
				//"b.dias_vacacion," +
				"a.DISCAPACITADO_GTEMP, "+
				"c.PORCENTAJE_GTDIE, "+  
				"a.acumula_decimo_gtemp "; 
		sql+="from " +
				"( ";
		sql+="select " +
				"EDP.IDE_GEEDP," +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP," +
				"EMP.APELLIDO_PATERNO_GTEMP ||' '|| " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) ||' '|| " +
				"EMP.PRIMER_NOMBRE_GTEMP ||' '|| " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES, " +
				"EDP.IDE_SUCU, " +
				"EMP.IDE_GTEMP, " +
				"EMP.DISCAPACITADO_GTEMP, "+
				"EDP.IDE_GTTEM, emp.fecha_ingreso_grupo_gtemp, ";

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="EDP.FECHA_LIQUIDACION_GEEDP AS FECHA_GEEDP, ";	
		}else{
			sql+="EDP.FECHA_GEEDP, ";
		}

		sql+="EDP.RMU_GEEDP, "+
				"EDP.AJUSTE_SUELDO_GEEDP, " +
				"EDP.FECHA_AJUSTE_GEEDP, "+
				"EMP.FECHA_INGRESO_GTEMP, "+
				"EDP.ACUMULA_FONDOS_GEEDP, "+
				"EMP.ACUMULA_DECIMO_GTEMP,"+
				"DIP.IDE_GEREG, "+
				"EDP.FECHA_ENCARGO_GEEDP,EDP.SUELDO_SUBROGA_GEEDP,EDP.FECHA_ENCARGO_FIN_GEEDP "+
				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM " +
				"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP "+
				"inner join SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU " +
				"LEFT JOIN GEN_DIVISION_POLITICA DIP ON DIP.IDE_GEDIP=SUC.IDE_GEDIP "+
				"WHERE DTN.IDE_NRDTN="+ide_nrdtn+" " +
				"AND EDP.IDE_GTTEM=1" +
				"and fecha_liquidacion_geedp between '"+utilitario.getVariable("p_fecha_inicio_liquidar")+"' and '"+utilitario.getVariable("p_fecha_fin_liquidar")+"' "+
				//"and fecha_geedp <= to_date('"+fecha_fin_gepro+"','yyyy-mm-dd') "+ 
				" ";
		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="AND EDP.LIQUIDACION_GEEDP=1 "+
					"and (EDP.EJECUTO_LIQUIDACION_GEEDP IS NULL OR EDP.EJECUTO_LIQUIDACION_GEEDP!=1) ";

		}else{
			sql+="AND (EDP.LIQUIDACION_GEEDP IS NULL OR EDP.LIQUIDACION_GEEDP!=1) ";
		}

		sql+="ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC,EMP.APELLIDO_MATERNO_GTEMP ASC ";
		sql+=" )a  "
				+ "left join ( "
				+ "select IDE_GTEMP,PORCENTAJE_GTDIE from GTH_DISCAPACIDAD_EMPLEADO where ide_gtemp in ( "
				+ "select IDE_GTEMP from GTH_EMPLEADO  where DISCAPACITADO_GTEMP=TRUE)"
				+ ")c on c.ide_gtemp=a.ide_gtemp "
				+ "order by nombres";
		/*sql+=")a " +
				"left join " +
				"( " +
				"SELECT IDE_ASVAC,IDE_GTEMP,DIAS_VACACION FROM ( " +
				"SELECT a.IDE_ASVAC,edp.IDE_GTEMP, " +
				"(DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_VACACION " +
				"FROM ( " +
				"SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, " +
				"(case when SUM(DIA_ADICIONAL_ASDEV) is null then 0 else SUM(DIA_ADICIONAL_ASDEV) end) as NRO_DIAS_ADICIONAL, " +
				"(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO, " +
				"(case when SUM(DIA_SOLICITADO_ASDEV) is null then 0 else SUM(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO " +
				"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = TRUE GROUP BY IDE_ASVAC " +
				") a " +
				"inner join ASI_VACACION vac on VAC.IDE_ASVAC=a.IDE_ASVAC " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR edp on edp.ide_gtemp=vac.ide_gtemp  and vac.ACTIVO_ASVAC=TRUE " +
				") a " +
				"GROUP BY IDE_ASVAC,IDE_GTEMP,DIAS_VACACION  " +
				")b  " +
				"on a.ide_gtemp=b.ide_gtemp "+
				"left join ( " +
				"select IDE_GTEMP,PORCENTAJE_GTDIE from GTH_DISCAPACIDAD_EMPLEADO where ide_gtemp in ( " +
				"select IDE_GTEMP from GTH_EMPLEADO  where DISCAPACITADO_GTEMP=TRUE) " +
				")c on c.ide_gtemp=a.ide_gtemp and c.ide_gtemp=b.ide_gtemp " +
				"order by nombres ";*/

		return sql;
	}
	
	
	/**Busca todos los empleados que pretenecen a ese detalle de tipo de nomina 
	 * @param ide_nrdtn Detalle de tipo de nomina
	 * @return TablaGenerica con los empleados de ese tipo de nomina
	 */
	public String getSqlEmpleadosTipoNominaLiquidacionLOEP(String ide_nrdtn,String fecha_fin_gepro){
		String IDE_NRTIN="";
		try {
			IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");	
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (ide_nrdtn==null || ide_nrdtn.isEmpty()){
			ide_nrdtn="-1";
		}

		String sql="select ";

		sql+="a.IDE_GEEDP," +
				"a.DOCUMENTO_IDENTIDAD_GTEMP," +
				"a.NOMBRES, " +
				"a.IDE_SUCU, " +
				"a.IDE_GTEMP, " +
				"a.IDE_GTTEM, "+
				"a.FECHA_GEEDP, "+	
				"a.RMU_GEEDP, "+
				"a.AJUSTE_SUELDO_GEEDP, " +
				"a.FECHA_AJUSTE_GEEDP, "+
				"a.fecha_ingreso_grupo_gtemp, "+//awbecerra
				"a.FECHA_INGRESO_GTEMP, "+
				"a.ACUMULA_FONDOS_GEEDP, "+
				"a.IDE_GEREG, "+
				"a.FECHA_ENCARGO_GEEDP,a.SUELDO_SUBROGA_GEEDP,a.FECHA_ENCARGO_FIN_GEEDP," +
				//"b.dias_vacacion," +
				"a.DISCAPACITADO_GTEMP, "+
				"c.PORCENTAJE_GTDIE, "+  
				"a.acumula_decimo_gtemp "; 
		sql+="from " +
				"( ";
		sql+="select " +
				"EDP.IDE_GEEDP," +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP," +
				"EMP.APELLIDO_PATERNO_GTEMP ||' '|| " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) ||' '|| " +
				"EMP.PRIMER_NOMBRE_GTEMP ||' '|| " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES, " +
				"EDP.IDE_SUCU, " +
				"EMP.IDE_GTEMP, " +
				"EMP.DISCAPACITADO_GTEMP, "+
				"EDP.IDE_GTTEM, emp.fecha_ingreso_grupo_gtemp, ";

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="EDP.FECHA_LIQUIDACION_GEEDP AS FECHA_GEEDP, ";	
		}else{
			sql+="EDP.FECHA_GEEDP, ";
		}

		sql+="EDP.RMU_GEEDP, "+
				"EDP.AJUSTE_SUELDO_GEEDP, " +
				"EDP.FECHA_AJUSTE_GEEDP, "+
				"EMP.FECHA_INGRESO_GTEMP, "+
				"EDP.ACUMULA_FONDOS_GEEDP, "+
				"EMP.ACUMULA_DECIMO_GTEMP,"+
				"DIP.IDE_GEREG, "+
				"EDP.FECHA_ENCARGO_GEEDP,EDP.SUELDO_SUBROGA_GEEDP,EDP.FECHA_ENCARGO_FIN_GEEDP "+
				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM " +
				"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP "+
				"inner join SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU " +
				"LEFT JOIN GEN_DIVISION_POLITICA DIP ON DIP.IDE_GEDIP=SUC.IDE_GEDIP "+
				"WHERE DTN.IDE_NRDTN="+ide_nrdtn+" "
				+ "AND EDP.IDE_GTTEM=2" +
				"and fecha_liquidacion_geedp between '"+utilitario.getVariable("p_fecha_inicio_liquidar")+"' and '"+utilitario.getVariable("p_fecha_fin_liquidar")+"' "+
				//"and fecha_geedp <= to_date('"+fecha_fin_gepro+"','yyyy-mm-dd') "+ 
				" ";
		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			sql+="AND EDP.LIQUIDACION_GEEDP=1 "+
					"and (EDP.EJECUTO_LIQUIDACION_GEEDP IS NULL OR EDP.EJECUTO_LIQUIDACION_GEEDP!=1) ";

		}else{
			sql+="AND (EDP.LIQUIDACION_GEEDP IS NULL OR EDP.LIQUIDACION_GEEDP!=1) ";
		}

		sql+="ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC,EMP.APELLIDO_MATERNO_GTEMP ASC ";
		sql+=" )a  "
				+ "left join ( "
				+ "select IDE_GTEMP,PORCENTAJE_GTDIE from GTH_DISCAPACIDAD_EMPLEADO where ide_gtemp in ( "
				+ "select IDE_GTEMP from GTH_EMPLEADO  where DISCAPACITADO_GTEMP=TRUE)"
				+ ")c on c.ide_gtemp=a.ide_gtemp "
				+ "order by nombres";
		/*sql+=")a " +
				"left join " +
				"( " +
				"SELECT IDE_ASVAC,IDE_GTEMP,DIAS_VACACION FROM ( " +
				"SELECT a.IDE_ASVAC,edp.IDE_GTEMP, " +
				"(DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_VACACION " +
				"FROM ( " +
				"SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, " +
				"(case when SUM(DIA_ADICIONAL_ASDEV) is null then 0 else SUM(DIA_ADICIONAL_ASDEV) end) as NRO_DIAS_ADICIONAL, " +
				"(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO, " +
				"(case when SUM(DIA_SOLICITADO_ASDEV) is null then 0 else SUM(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO " +
				"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = TRUE GROUP BY IDE_ASVAC " +
				") a " +
				"inner join ASI_VACACION vac on VAC.IDE_ASVAC=a.IDE_ASVAC " +
				"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR edp on edp.ide_gtemp=vac.ide_gtemp  and vac.ACTIVO_ASVAC=TRUE " +
				") a " +
				"GROUP BY IDE_ASVAC,IDE_GTEMP,DIAS_VACACION  " +
				")b  " +
				"on a.ide_gtemp=b.ide_gtemp "+
				"left join ( " +
				"select IDE_GTEMP,PORCENTAJE_GTDIE from GTH_DISCAPACIDAD_EMPLEADO where ide_gtemp in ( " +
				"select IDE_GTEMP from GTH_EMPLEADO  where DISCAPACITADO_GTEMP=TRUE) " +
				")c on c.ide_gtemp=a.ide_gtemp and c.ide_gtemp=b.ide_gtemp " +
				"order by nombres ";*/

		return sql;
	}

	public TablaGenerica getDetalleTipoNomina(String ide_nrdtn){
		TablaGenerica tab_dtn=utilitario.consultar("SELECT * FROM NRH_DETALLE_TIPO_NOMINA WHERE IDE_NRDTN in ("+ide_nrdtn+") ");
		if (tab_dtn.getTotalFilas()>0){			
			return tab_dtn;	
		}
		return null;
	}
 

	public TablaGenerica getDetalleTipoNominaNombres(String ide_nrdtn){
		TablaGenerica tab_dtn=utilitario.consultar("SELECT DTN.IDE_NRDTN,DETALLE_NRTIN,DETALLE_GTTEM FROM NRH_DETALLE_TIPO_NOMINA DTN " +
				"INNER JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
				"INNER JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
				"where IDE_NRDTN in ("+ide_nrdtn+")");
		return tab_dtn;
	}

	/**
	 * @param parametro :recibe el parametro de retorno (nombre de la columna) de la tabla NRH_RUBROS que requerimos
	 * @param ide_nrrub :recibe el ide del rubro (ide_nrrub) a buscar en la tabla nrh_rubros   
	 * @return : retorna el valor que contiene la columna de parametro que enviamos
	 * 
	 */
	public TablaGenerica getRubro(String ide_nrrub) {
		if (ide_nrrub != null && !ide_nrrub.isEmpty()) {
			TablaGenerica tab_rubros = utilitario.consultar("SELECT DISTINCT RUB.IDE_NRRUB,IDE_NRFOC,IDE_NRTIR,DETALLE_NRRUB,ANTICIPO_NRRUB,ACTIVO_NRRUB ,ORDEN_NRDER " +
					"FROM NRH_RUBRO rub " +
					"INNER join NRH_DETALLE_RUBRO der on DER.ide_nrrub=RUB.IDE_NRRUB " +
					"WHERE der.IDE_NRRUB IN ("+ide_nrrub+") " +
					"ORDER BY ORDEN_NRDER");

			if (tab_rubros.getTotalFilas() > 0) {				
				return tab_rubros;				 
			}        
		}
		return null;
	}

	public TablaGenerica getRubrosImpresion(String ide_nrrub,String ide_nrdtn) {
		if (ide_nrrub != null && !ide_nrrub.isEmpty()) {
			TablaGenerica tab_rubros = utilitario.consultar("select a.ide_nrrub,a.detalle_nrrub from (select RUB.IDE_NRRUB,DETALLE_NRRUB,ORDEN_IMPRIME_NRDER from NRH_DETALLE_RUBRO der " +
					"INNER join NRH_RUBRO rub on RUB.ide_nrrub=DER.IDE_NRRUB " +
					"where DER.IDE_NRRUB in ("+ide_nrrub+") " +
					"and IMPRIME_NRDER=true " +
					"and ide_nrdtn in ("+ide_nrdtn+") "+
					"GROUP BY RUB.IDE_NRRUB,DETALLE_NRRUB,ORDEN_IMPRIME_NRDER " +
					"ORDER BY ORDEN_IMPRIME_NRDER " +
					")a " +
					"group by a.ide_nrrub,a.detalle_nrrub,a.ORDEN_IMPRIME_NRDER " +
					"ORDER BY a.ORDEN_IMPRIME_NRDER  ");
			if (tab_rubros.getTotalFilas() > 0) {				
				return tab_rubros;				 
			}        
		}
		return null;
	}


	/**'
	 * Retorna una tabla vacia del Detalle de Rubros Rol
	 * @return
	 */
	private TablaGenerica getDetalleRubrosRolVacia(){

		TablaGenerica tab_deta_rol_vacia=new TablaGenerica();
		//		tab_deta_rol_vacia.setTabla("NRH_DETALLE_ROL", "IDE_NRDRO", -1);
		tab_deta_rol_vacia.setSql("select IDE_NRDRO,IDE_NRROL,IDE_GEEDP,IDE_NRDER,VALOR_NRDRO,'' AS FECHA_INICIAL_NRDER, " +
				"'' AS FECHA_FINAL_NRDER,'' AS FECHA_PAGO_NRDER,0 AS ORDEN_CALCULO_NRDRO " +
				"from NRH_DETALLE_ROL WHERE IDE_NRROL=-1");
		tab_deta_rol_vacia.setCampoPrimaria("IDE_NRDRO");
		tab_deta_rol_vacia.setNumeroTabla(-1);

		//		tab_deta_rol_vacia.setCondicion("IDE_NRDRO=-1");
		tab_deta_rol_vacia.ejecutarSql();
		return tab_deta_rol_vacia;
	}


	public TablaGenerica getDetalleFacturaGuarderiaEmpleado(String IDE_GTEMP){
		//		TablaGenerica tab_det_fac_gua=utilitario.consultar("select * from NRH_DETALLE_FACTURA_GUARDERIA where PAGADO_NRDFG != 1 AND IDE_NRHIG in ( " +
		//				"select IDE_NRHIG from NRH_HIJO_GUARDERIA where IDE_NRBEE in ( " +
		//				"select IDE_NRBEE from NRH_BENEFICIO_EMPLEADO where GUARDERIA_NRBEE=1 " +
		//				"AND IDE_GEEDP="+IDE_GEEDP+"))");
		TablaGenerica tab_det_fac_gua=utilitario.consultar("select * from NRH_DETALLE_FACTURA_GUARDERIA where PAGADO_NRDFG != 1 AND IDE_NRHIG in ( " +
				"select IDE_NRHIG from NRH_HIJO_GUARDERIA where IDE_NRBEE in ( " +
				"select IDE_NRBEE from NRH_BENEFICIO_EMPLEADO where GUARDERIA_NRBEE=1 " +
				"AND IDE_GTEMP="+IDE_GTEMP+"))");

		return tab_det_fac_gua;
	}


	public String getValorRubroBeneficioGuarderia(TablaGenerica tab_det_fac_gua,String fecha_final_gepro,String IDE_NRRUB,String ide_geedp){
		int int_mes_rol=utilitario.getMes(fecha_final_gepro);
		int int_mes_fact=0;
		try {
			int_mes_fact=pckUtilidades.CConversion.CInt(tab_det_fac_gua.getValor("IDE_GEMES"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (int_mes_rol==int_mes_fact){
			if (tab_det_fac_gua.getValor("IDE_NRRUB").equalsIgnoreCase(IDE_NRRUB)){
				utilitario.getConexion().agregarSql("UPDATE NRH_DETALLE_FACTURA_GUARDERIA SET PAGADO_NRDFG=0 WHERE IDE_NRDFG in (select IDE_NRDFG from NRH_DETALLE_FACTURA_GUARDERIA where ACTIVO_NRDFG=TRUE " +
						"AND IDE_NRHIG in (select IDE_NRHIG from NRH_HIJO_GUARDERIA where IDE_NRBEE in ( " +
						"select IDE_NRBEE from NRH_BENEFICIO_EMPLEADO where GUARDERIA_NRBEE=1 AND IDE_GEEDP="+ide_geedp+")))");
				return tab_det_fac_gua.getSumaColumna("VALOR_PAGAR_NRDFG")+"";
			}
		}

		return null;
	}

	/**
	 * Despeja las formulas del detalle del rol
	 */

	public void despejarFormulasRol(int indice_final,String ide_geedp,String fecha_rol){		
		String formula_reemplazada="";
		//		System.out.println("ide geedp "+ide_geedp+" indice final "+indice_final);
		for (int i = 0; i < indice_final; i++) {
			if (tab_deta_rubros_rol.getValor(i, "IDE_NRDER").equals("2121")) {
				System.out.println("fondos nomina despejarFormula: "+i);
			}
			System.out.println("RUBRO FONDOS ");
			if (tab_deta_rubros_rol.getValor(i, "VALOR_NRDRO")!=null && !tab_deta_rubros_rol.getValor(i, "VALOR_NRDRO").isEmpty()){
				if (tab_deta_rubros_rol.getValor(i, "VALOR_NRDRO").startsWith("=")){

					//					if (ide_geedp.equalsIgnoreCase("349")){
					//						System.out.println("envia "+tab_deta_rubros_rol.getValor(i, "VALOR_NRDRO"));	
					//					}


					formula_reemplazada = getValorFormula(indice_final,fecha_rol,ide_geedp,tab_deta_rubros_rol.getValor(i, "IDE_NRDER"),tab_deta_rubros_rol.getValor(i, "VALOR_NRDRO"),tab_deta_rubros_rol.getValor(i, "FECHA_INICIAL_NRDER"),tab_deta_rubros_rol.getValor(i, "FECHA_FINAL_NRDER"),tab_deta_rubros_rol.getValor(i, "FECHA_PAGO_NRDER"));
					//					if (ide_geedp.equalsIgnoreCase("349")){
					//					System.out.println("formula reemplada "+formula_reemplazada);
					//					}
					try {
						tab_deta_rubros_rol.modificar(i);

						BigDecimal big_valor=new BigDecimal(utilitario.evaluarExpresion(formula_reemplazada));
						big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
						//						if (ide_geedp.equalsIgnoreCase("349")){
						//						System.out.println("valor calculado "+big_valor);
						//						}

						tab_deta_rubros_rol.setValor(i,"VALOR_NRDRO",big_valor+"");

					} catch (Exception e) {
						// TODO: handle exception
						tab_deta_rubros_rol.modificar(i);
						tab_deta_rubros_rol.setValor(i,"VALOR_NRDRO","0.0");
					}
				}		
			}
		}
	}

	/**
	 * Retorna el TablaGenerica EmpleadoDepartamento
	 * @param ide_geedp
	 * @return
	 */
	public TablaGenerica getEmpleadoDepartamento(String ide_geedp) {
		if ( ide_geedp != null && !ide_geedp.isEmpty()) {
			TablaGenerica tab_empl = utilitario.consultar("select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP=" + ide_geedp);
			if (tab_empl.getTotalFilas() > 0) {				
				return tab_empl;				 
			} 
		} 
		return null;
	}



	public String getFormulaEnLetras(int indice,String fecha_rol,String ide_geedp,String ide_nrder_formula,String formula_rhcru,boolean con_reemplazo_de_1){
		if (formula_rhcru!=null && !formula_rhcru.isEmpty()){
			return getFormulaReemplazada(indice,fecha_rol,ide_geedp,ide_nrder_formula,formula_rhcru,"","","",true,con_reemplazo_de_1);
		}
		return null;
	}

	private String getValorFormula(int total_filas_detalle_rol,String fecha_rol,String ide_geedp,String ide_nrder_formula,String formula_rhcru,String fecha_inicial_nrder,String fecha_final_nrder,String fecha_pago_nrder) {
		if (formula_rhcru!=null && !formula_rhcru.isEmpty()){
			return getFormulaReemplazada(total_filas_detalle_rol,fecha_rol,ide_geedp,ide_nrder_formula,formula_rhcru,fecha_inicial_nrder,fecha_final_nrder,fecha_pago_nrder,false,false);
		}
		return "0";
	}

	private String getRecuperarNumeroFormula(String formula,boolean en_letras) {
		if (formula.length()>=3){	
			if (formula.substring(0, 3).equalsIgnoreCase("sum")){
				if (!en_letras){
					return formula.substring(0, (formula.indexOf("]")+1));
				}else{
					return formula.substring(formula.indexOf("[")+1, formula.indexOf("]")); 
				}
			}
		}
		return formula.substring(0, formula.indexOf("]"));

	}

	private String getBuscarValorRubro(int total_filas_detalle,String ide_nrder) {
		for (int i = 0; i < total_filas_detalle; i++) {
			if (tab_deta_rubros_rol.getValor(i, "ide_nrder").equals(ide_nrder)) {
				return tab_deta_rubros_rol.getValor(i, "valor_nrdro");
			}
		}

		return null;
	}

	private String getBuscarValorRubro(String formula,String ide_nrder_formula,String ide_geedp,String fecha_rol,String str_fecha_inicio_nrder,String str_fecha_fin_nrder,String str_fecha_pago_nrder) {

		if (formula !=null && !formula.isEmpty() && formula.length()>=3){
			if (formula.substring(0, 3).equalsIgnoreCase("sum")){

				if (str_fecha_inicio_nrder!=null && !str_fecha_inicio_nrder.isEmpty()
						&& str_fecha_fin_nrder!=null && !str_fecha_fin_nrder.isEmpty()){

					int indice=formula.indexOf("[");
					String ide_nrder_suma=formula.substring(indice+1, formula.indexOf("]"));
					int anio_actual=utilitario.getAnio(fecha_rol);

					str_fecha_inicio_nrder=getFormatoFechaCalculoRubros(str_fecha_inicio_nrder, anio_actual);
					str_fecha_fin_nrder=getFormatoFechaCalculoRubros(str_fecha_fin_nrder, anio_actual);
					str_fecha_pago_nrder=getFormatoFechaCalculoRubros(str_fecha_pago_nrder, anio_actual);

					if (str_fecha_inicio_nrder!=null && !str_fecha_inicio_nrder.isEmpty()
							&& str_fecha_fin_nrder!=null && !str_fecha_fin_nrder.isEmpty()){
						if (str_fecha_pago_nrder!=null && !str_fecha_pago_nrder.isEmpty()){

							int mes_rol=utilitario.getMes(fecha_rol);
							int mes_pago_rubro=utilitario.getMes(str_fecha_pago_nrder);

							if (mes_rol==mes_pago_rubro){
								if (utilitario.isFechaMenor(utilitario.getFecha(str_fecha_pago_nrder), utilitario.getFecha(fecha_rol))
										|| !utilitario.isFechaMayor(utilitario.getFecha(str_fecha_pago_nrder), utilitario.getFecha(fecha_rol))){

									return serv_nomina.getSumatoriaRubro(ide_geedp, ide_nrder_suma,str_fecha_inicio_nrder,str_fecha_fin_nrder)+"";
								}
							}else{
								return "0";
							}
						}else{
							return serv_nomina.getSumatoriaRubro(ide_geedp, ide_nrder_suma,str_fecha_inicio_nrder,str_fecha_fin_nrder)+"";
						}
					}else{
						return "0";
					}


					//					if (str_fecha_inicio_nrder.indexOf("/")!=-1){
					//						String num_anios=str_fecha_inicio_nrder.substring(str_fecha_inicio_nrder.indexOf("/")+1, str_fecha_inicio_nrder.length());
					//						String fecha=str_fecha_inicio_nrder.substring(0,str_fecha_inicio_nrder.indexOf("/"));								
					//						int int_num_anios_fec_ini=0;	
					//						try {
					//							int_num_anios_fec_ini=pckUtilidades.CConversion.CInt(num_anios);
					//						} catch (Exception e) {
					//							// TODO: handle exception
					//						}
					//						fecha=(anio_actual+int_num_anios_fec_ini)+"-"+fecha;
					//						str_fecha_inicio_nrder=fecha;
					//					}
					//					if (str_fecha_fin_nrder.indexOf("/")!=-1){
					//						String num_anios=str_fecha_fin_nrder.substring(str_fecha_fin_nrder.indexOf("/")+1, str_fecha_fin_nrder.length());
					//						String fecha=str_fecha_fin_nrder.substring(0,str_fecha_fin_nrder.indexOf("/"));								
					//						int int_num_anios_fec_fin=0;	
					//						try {
					//							int_num_anios_fec_fin=pckUtilidades.CConversion.CInt(num_anios);
					//						} catch (Exception e) {
					//							// TODO: handle exception
					//						}
					//						fecha=(anio_actual+int_num_anios_fec_fin)+"-"+fecha;
					//						str_fecha_fin_nrder=fecha;
					//					}
					//					if (str_fecha_pago_nrder!=null && !str_fecha_pago_nrder.isEmpty()){
					//						if (str_fecha_pago_nrder.indexOf("/")!=-1){
					//							String num_anios=str_fecha_pago_nrder.substring(str_fecha_pago_nrder.indexOf("/")+1, str_fecha_pago_nrder.length());
					//							String fecha=str_fecha_pago_nrder.substring(0,str_fecha_pago_nrder.indexOf("/"));								
					//							int int_num_anios_fec_pago=0;	
					//							try {
					//								int_num_anios_fec_pago=pckUtilidades.CConversion.CInt(num_anios);
					//							} catch (Exception e) {
					//								// TODO: handle exception
					//							}
					//							fecha=(anio_actual+int_num_anios_fec_pago)+"-"+fecha;
					//							str_fecha_pago_nrder=fecha;
					//						}else{
					//							String fecha=str_fecha_pago_nrder;
					//							fecha=anio_actual+"-"+fecha;
					//							str_fecha_pago_nrder=fecha;
					//						}
					//					}
					//


				}else{
					return "0";
				}
			}
		}


		return null;
	}

	private int getIndiceValorRubro(String ide_nrder,String ide_geedp) {
		for (int i = 0; i < tab_deta_rubros_rol.getTotalFilas(); i++) {
			if (tab_deta_rubros_rol.getValor(i, "ide_nrder").equals(ide_nrder)
					&& tab_deta_rubros_rol.getValor(i, "ide_geedp").equals(ide_geedp)) {
				return i;
			}
		}

		return -1;
	}


	/** busca el Detalle de tipo nomina y retorna una tabla
	 * @param ide_nrdtn_a_buscar : ide a buscar en la tabla 
	 * 
	 * @return si no existe datos retorna null 
	 */
	public TablaGenerica getDetalleRubro(String ide_nrder){
		//System.out.println("SN C_PARAMETRO getDetalleRubro ide_nrder... "+ide_nrder);
		TablaGenerica tab_dru=utilitario.consultar("select * from NRH_DETALLE_RUBRO where IDE_NRDER="+ide_nrder+" ");
		//System.out.println(" ");
		
		//System.out.println("SN sql getDetalleRubro tab_dru ... "+tab_dru.getSql());
		
		if (tab_dru.getTotalFilas()>0){			
			return tab_dru;			
		}
		return null;
	}

	private String getFormulaReemplazada(int total_filas_detalle,String fecha_rol,String ide_geedp,String ide_nrder_formula,String formula_nrder,String fecha_inicial_nrder,String fecha_final_nrder,String fecha_pago_nrder ,boolean con_nombre_formula,boolean reemplazar_con_1) {
		String formula = formula_nrder;
		String formula_reemplazada = "";
		String nueva_formula = "";
		int band=0;
		int indice = 0;
		int indicador_parentesis = 0;
		String parentesis;

		formula=formula.trim();

		formula=formula.replaceAll(" ", "");

		if (reemplazar_con_1){
			formula=formula.replaceAll("sum", "");
		}

		do {
			indice = formula.indexOf("[");
			if (indice != -1) {
				do {
					if (band==0){
						nueva_formula = formula.substring(1, indice + 1);
						band=1;
					}else{
						nueva_formula = formula.substring(indice, indice + 1);	
					}
					formula = formula.substring(indice + 1);
					parentesis = formula.substring(0, 1);
					if (parentesis.equals("[")) {
						indicador_parentesis = 1;
						indice = formula.indexOf("[");
						formula_reemplazada = formula_reemplazada.concat(nueva_formula);
					} else {
						indicador_parentesis = 0;
					}
				} while (indicador_parentesis == 1);

				String ide_nrder = getRecuperarNumeroFormula(formula,con_nombre_formula);


				if (!con_nombre_formula) {
					String valor_ide="";
					if (ide_nrder.length()>=3){
						if (ide_nrder.substring(0, 3).equalsIgnoreCase("sum")){
							formula=formula.substring(formula.indexOf("[")+1, formula.length());
							formula=formula.replaceFirst("]", "");
							valor_ide=getBuscarValorRubro(ide_nrder, ide_nrder_formula, ide_geedp, fecha_rol,fecha_inicial_nrder,fecha_final_nrder,fecha_pago_nrder);
						}
					}
					if (valor_ide==null || valor_ide.isEmpty()){	
						valor_ide = getBuscarValorRubro(total_filas_detalle,ide_nrder);
						//System.out.println("valor_ide: "+valor_ide);
					}
					if (valor_ide == null || valor_ide.isEmpty()) {
						valor_ide = "0";
					}
					nueva_formula = nueva_formula.concat(valor_ide);

				} else {
					String nombre_ide="";

					if (!reemplazar_con_1){
						if (formula.length()>=3){	
							if (formula.substring(0, 3).equalsIgnoreCase("sum")){
								nueva_formula=nueva_formula.concat(formula.substring(0, formula.indexOf("[")+1));
								formula=formula.substring(formula.indexOf("[")+1, formula.length());
							}
						}

						try {
							nombre_ide =getRubro(getDetalleRubro(ide_nrder).getValor("IDE_NRRUB")).getValor("DETALLE_NRRUB");
						} catch (Exception e) {
							// TODO: handle exception
						}
					}else{

						nombre_ide="1";
					}
					if (nombre_ide == null || nombre_ide.isEmpty()) {
						nombre_ide = "";
					}
					nueva_formula = nueva_formula.concat(nombre_ide);

				}

				int ind = formula.indexOf("[");
				if (ind != -1) {

					nueva_formula = nueva_formula.concat(formula.substring(formula.indexOf("]"), ind));

				} else {
					nueva_formula = nueva_formula.concat(formula.substring(formula.indexOf("]")));
				}
				formula_reemplazada = formula_reemplazada.concat(nueva_formula);
				//System.out.println("formula_reemplazada: "+formula_reemplazada);
			}else{
				if (reemplazar_con_1){
					formula_reemplazada="1";
				}
			}
		} while (indice != -1);
		return formula_reemplazada;
	}


	/**Busca un rubro dentro de un tipo de Nomina 
	 * @param IDE_NRDTN  Tipo de Nomina
	 * @param IDE_NRRUB   Rubro
	 * @return TablaGenerica del detalle del rubro
	 */
	public TablaGenerica getDetalleRubro(String IDE_NRDTN,String IDE_NRRUB){
		//System.out.println("SN getDetalleRubro C_PARAMETRO  IDE_NRRUB...  "+IDE_NRRUB);
		TablaGenerica tab_detalle_rubro=utilitario.consultar("select * from NRH_DETALLE_RUBRO " +
				"WHERE IDE_NRDTN="+IDE_NRDTN+" AND IDE_NRRUB="+IDE_NRRUB);
		//System.out.println("SN SQL getDetalleRubro tab_detalle_rubro...  "+tab_detalle_rubro.getSql());
		return tab_detalle_rubro;

	}

	/**
	 * Metodo que devuelve datos de empleados activos e inactivos
	 * @param ide_gtemp  : CODIGO DEL EMPLEADO
	 * @return String   resultado (CODIGO EMP, APELLIDO PAT, APELLIDO MAT, NOMBRES, NUM CEDULA)
	 */
	//servicio empleadosActivos
		public String servicioEmpleadosActivos(String estado){
			String sql_empleadosActivos="select ide_gtemp, apellido_paterno_gtemp, " +
					"(case when apellido_materno_gtemp is null then '' else apellido_materno_gtemp end) as apellido_materno_gtemp,primer_nombre_gtemp,  " +
					"(case when segundo_nombre_gtemp is null then '' else segundo_nombre_gtemp end) as segundo_nombre_gtemp,documento_identidad_gtemp from gth_empleado where activo_gtemp in ("+estado+")  " +
					"order by apellido_paterno_gtemp, apellido_materno_gtemp ";
			return sql_empleadosActivos;
		}
		
	//servicio empleadoContrato 
	
	public String servicioEmpleadoContrato(String estado){
		String sql_empleadoContrato="SELECT EPAR.IDE_GEEDP, EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' || (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)  || ' ' ||EMP.PRIMER_NOMBRE_GTEMP || ' ' || (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) " +
				" AS NOMBRES_APELLIDOS, DEPA.DETALLE_GEDEP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				" LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				" LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				" WHERE EPAR.ACTIVO_GEEDP in ("+estado+")";
		return sql_empleadoContrato;
	}
	//fin empleadoContrato	
	public String servicioEmpleadoContratoCodigo(String estado,String ide_gtemp){
		String sql_empleadoContrato="SELECT EPAR.IDE_GEEDP, EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' || (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||EMP.PRIMER_NOMBRE_GTEMP || ' ' || (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) " +
				" AS NOMBRES_APELLIDOS, SUCU.NOM_SUCU, AREA.DETALLE_GEARE,AREA.IDE_GEARE, DEPA.DETALLE_GEDEP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				" LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				" LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				" WHERE EPAR.ACTIVO_GEEDP in ("+estado+") and EMP.IDE_GTEMP in ("+ide_gtemp+") order by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP ";
		return sql_empleadoContrato;
	}
	//servicio empleadoContrato  string
	public TablaGenerica empleadoContrato(String estado){
		TablaGenerica tab_empleadoContrato=utilitario.consultar("SELECT EPAR.IDE_GEEDP, EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' || (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||EMP.PRIMER_NOMBRE_GTEMP || ' ' || (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) " +
				" AS NOMBRES_APELLIDOS, SUCU.NOM_SUCU, AREA.DETALLE_GEARE, DEPA.DETALLE_GEDEP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				" LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				" LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				" WHERE EPAR.ACTIVO_GEEDP in ("+estado+")");
		return tab_empleadoContrato;
	}
	//fin empleadoContrato	
	
	public TablaGenerica ideEmpleadoContrato(String empleado,String estado){
		TablaGenerica tab_empleadoContrato=utilitario.consultar("SELECT EPAR.IDE_GEEDP, EMP.IDE_GTEMP, EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP, EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP ,EMP.SEGUNDO_NOMBRE_GTEMP, SUCU.NOM_SUCU, AREA.IDE_GEARE,AREA.DETALLE_GEARE, DEPA.DETALLE_GEDEP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				" LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				" LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				" WHERE EPAR.IDE_GEEDP in ("+empleado+") and activo_geedp in ("+estado+") ");
		return tab_empleadoContrato;
	}	
	
	public String ideEmpleadoSubrogacion(String sql_adicional){
		String tab_empleadoContrato="select b.ide_geedp,documento_identidad_gtemp,"
+" apellido_paterno_gtemp||' '||(case when apellido_materno_gtemp is null then '' else apellido_materno_gtemp end)||' '||primer_nombre_gtemp"
+" ||' '||(case when segundo_nombre_gtemp is null then '' else segundo_nombre_gtemp end) as empleado,codigo_partida_gepap,"
+" fecha_ingreso_gtemp, titulo_cargo_gepgc,detalle_geare as proceso,detalle_gedep as sub_proceso,"
+" detalle_gegro as grupo_ocupacional,rmu_geedp,detalle_gttem,detalle_gttco,nom_sucu as lugar_trabajo,codigo_sbs_gtgen as genero,"
+" (case when discapacitado_gtemp = true then 'SI' else 'NO' end) as discapacitado,fecha_nacimiento_gtemp,observacion_geedp"
+" from gth_empleado a,gen_empleados_departamento_par b, gen_partida_presupuestaria c,gen_partida_grupo_cargo d,gen_area e,"
+" gen_departamento f,gen_grupo_ocupacional g,gth_tipo_empleado h,gth_tipo_contrato i,sis_sucursal j,gth_genero k"
+" where a.ide_gtemp = b.ide_gtemp"
+" and activo_geedp = true"
+" and b.ide_gepgc = d.ide_gepgc"
+" and c.ide_gepap = d.ide_gepap"
+" and d.ide_geare = e.ide_geare"
+" and d.ide_gedep = f.ide_gedep"
+" and d.ide_gegro = g.ide_gegro"
+" and d.ide_gttem =h.ide_gttem"
+" and b.ide_gttco = i.ide_gttco"
+" and a.ide_gtgen = k.ide_gtgen "+sql_adicional
+" and d.ide_sucu = j.ide_sucu"
+" order by empleado";
		return tab_empleadoContrato;
	}
	/**Busca un rubro dentro de un tipo de Nomina 
	 * @param IDE_NRDTN  Tipo de Nomina
	 * @param IDE_NRRUB   Rubro
	 * @return TablaGenerica del detalle del rubro
	 */
	public TablaGenerica getDetalleRubro(String IDE_NRDTN,String IDE_NRRUB,String IDE_GEREG){
		/*
		System.out.println("SN C_PARAMETROS getDetalleRubro IDE_NRDTN..."+IDE_NRDTN);
		System.out.println("SN C_PARAMETROS getDetalleRubro IDE_NRRUB..."+IDE_NRRUB);
		System.out.println("SN C_PARAMETROS getDetalleRubro IDE_GEREG..."+IDE_GEREG);
		*/
		
		if (IDE_NRDTN==null || IDE_NRDTN.isEmpty()){
			//System.out.println("SN C_PARAMETROS getDetalleRubro IDE_NRDTN == A NULL..."+IDE_NRDTN);
			
			IDE_NRDTN="-1";
			//System.out.println("SN C_PARAMETROS getDetalleRubro IDE_NRDTN -1..."+IDE_NRDTN);
			
		}
		if (IDE_NRRUB==null || IDE_NRRUB.isEmpty()){
			//System.out.println("SN C_PARAMETROS getDetalleRubro IDE_NRRUB == A NULL..."+IDE_NRRUB);
			
			IDE_NRRUB="-1";
			//System.out.println("SN C_PARAMETROS getDetalleRubro IDE_NRRUB -1..."+IDE_NRRUB);
			
		}
		if (IDE_GEREG==null || IDE_GEREG.isEmpty()){
			//System.out.println("SN C_PARAMETROS getDetalleRubro IDE_GEREG == A NULL..."+IDE_GEREG);
			
			IDE_GEREG="-1";
			//System.out.println("SN C_PARAMETROS getDetalleRubro IDE_GEREG -1..."+IDE_GEREG);
			
		}


		TablaGenerica tab_detalle_rubro=utilitario.consultar("select * from NRH_DETALLE_RUBRO " +
				"WHERE IDE_NRDTN="+IDE_NRDTN+" AND IDE_NRRUB="+IDE_NRRUB+" AND IDE_GEREG="+IDE_GEREG );
		//System.out.println("SN SQL getDetalleRubro tab_detalle_rubro DESPUES DE LOS NULL ..."+tab_detalle_rubro.getSql());
		

		return tab_detalle_rubro;


	}

	/**
	 * Sirve cuando se copia un rubro, ya que la formula permanece con el ide de la nomina q se copio
	 * @param IDE_NRDER  ide de la nomina original
	 * @param IDE_NRDTN  Tipo de nomina actual
	 * @return  ide del detalle de la nomina actual si existe
	 */
	public String getNuevoIdeTipoNomina(String IDE_NRDER, String  IDE_NRDTN ){
		
		
		TablaGenerica tab_dtn=getDetalleRubro(IDE_NRDER);
			
		if(tab_dtn!=null){
			String str_ide_nrrub=tab_dtn.getValor("IDE_NRRUB");
						
			if(str_ide_nrrub!=null){
				return getDetalleRubro(IDE_NRDTN, str_ide_nrrub).getValor("IDE_NRDER");
			}			
		}
		return null;
	}


	public TablaGenerica getTab_deta_rubros_rol() {
		return tab_deta_rubros_rol;
	}


	public void setTab_deta_rubros_rol(TablaGenerica tab_deta_rubros_rol) {
		this.tab_deta_rubros_rol = tab_deta_rubros_rol;
	}

	
	
	/*
	 * Metodo encerra los valores generados en la tabla nrh_detalle_rol
	 */
	public void encerrarValoresRol (String ide_nrrol,String ide_geedp){
	utilitario.getConexion().ejecutarSql("UPDATE nrh_detalle_rol SET  valor_nrdro=0.0 where ide_nrdro in( "
			+ "select ide_nrdro from nrh_detalle_rol where ide_nrder in( "
			+ "select ide_nrder from nrh_detalle_rubro where ide_nrder in(select ide_nrder  "
			+ "from  "
			+ "nrh_detalle_rol  "
			+ "where ide_nrrol="+ide_nrrol+" "  
			+ "and ide_geedp IN ("+ide_geedp+"))"
			+ "AND IDE_NRRUB NOT IN(122,342,44,119) ORDER BY IDE_NRDER ASC) "
			+ " and ide_nrrol ="+ide_nrrol+" and ide_geedp="+ide_geedp+") ");
	}
	/*
	 * Suma los rubros correspondientes a jubilacion por invalidez (IESS PERSONAL +IESS PATRONAL)
	 */
	public void sumarRubrosJubilacionXInvalidacion(String ide_nrrol,String ide_geedp){
	utilitario.getConexion().ejecutarSql("UPDATE nrh_detalle_rol SET  valor_nrdro=("
			+ " select sum(valor_nrdro) as suma from  nrh_detalle_rol where ide_nrdro in( "
			+ "select ide_nrdro from nrh_detalle_rol where ide_nrder in( "
			+ "select ide_nrder from nrh_detalle_rubro where ide_nrder in(select ide_nrder  "
			+ "from  "
			+ "nrh_detalle_rol  "
			+ "where ide_nrrol="+ide_nrrol+"  "
			+ "and ide_geedp IN ("+ide_geedp+")) "
			+ "AND IDE_NRRUB IN(44,119) ORDER BY IDE_NRDER ASC) "
			+ "and ide_nrrol = "+ide_nrrol+"  and ide_geedp="+ide_geedp+")"
			+ ") "
			+ "where ide_nrdro in( "
			+ "select ide_nrdro from nrh_detalle_rol where ide_nrder in( "
			+ "select ide_nrder from nrh_detalle_rubro where ide_nrder in(select ide_nrder  "
			+ "from  "
			+ "nrh_detalle_rol  "
			+ "where ide_nrrol="+ide_nrrol+" "  
			+ "and ide_geedp IN ("+ide_geedp+"))"
			+ "AND IDE_NRRUB IN(119) ORDER BY IDE_NRDER ASC) "
			+ " and ide_nrrol ="+ide_nrrol+" and ide_geedp="+ide_geedp+") ");
	
	}
	
	/*
	 * Actualiza el rubro iess personal a 0.0
	 */
	public void actualizarRubrosIessPersonalXJubilacionXInvalidacion(String ide_nrrol,String ide_geedp){
	utilitario.getConexion().ejecutarSql("UPDATE nrh_detalle_rol SET  valor_nrdro=0.0 where ide_nrdro in( "
			+ "select ide_nrdro from nrh_detalle_rol where ide_nrder in( "
			+ "select ide_nrder from nrh_detalle_rubro where ide_nrder in(select ide_nrder  "
			+ "from  "
			+ "nrh_detalle_rol  "
			+ "where ide_nrrol="+ide_nrrol+" "
			+ "and ide_geedp IN ("+ide_geedp+")) "
			+ "AND IDE_NRRUB IN(44) ORDER BY IDE_NRDER ASC "
			+ ") and ide_nrrol = "+ide_nrrol+" and ide_geedp="+ide_geedp+")");
	
	}
	
	
	
	
	public String decimoCuartoMotivoAccion(String IDE_GEEDP,String fecha_ingreso_emgirs){
		double dias=360;
		double valor_dias=0;
		double valor_dias_trabajados=0;
		int mes=0,anio=0;
		mes=utilitario.getMes(fecha_ingreso_emgirs);
		anio=utilitario.getAnio(fecha_ingreso_emgirs);
		String fecha_comparacion="";
		boolean estadoContratacion=false;
		TablaGenerica tab_empleado= utilitario.consultar("select IDE_GEEDP,IDE_GTEMP from gen_empleados_departamento_par where ide_geedp="+IDE_GEEDP);
		
		
		TablaGenerica tab_empleado_departamento_par=null;
		tab_empleado_departamento_par=utilitario.consultar("select ide_geedp,ide_geded,fecha_geedp,fecha_liquidacion_geedp  "
				+ "from gen_empleados_departamento_par  "
				+ "where ide_gtemp="+tab_empleado.getValor("IDE_GTEMP")+" "
						+ " and liquidacion_geedp=1 and ide_gttem !=3"
						+ "order by ide_geedp DESC ");
						//+ "limit 1");
		
		if (tab_empleado_departamento_par.getTotalFilas()>0) {
			
	
		String cambioContratoPorEmpleado="";
		//tabla gen_accion_motivo_empleado  
		TablaGenerica tab_accion_motivo_empleado=null;
		//for (int i = tab_empleado_departamento_par.getTotal
		for (int x = 0; x < tab_empleado_departamento_par.getTotalFilas(); x++) {

	
		cambioContratoPorEmpleado=tab_empleado_departamento_par.getValor(x,"IDE_GEDED");
		//Los ide_geame   que son los motivos de la tabla gen_accion_motivo_empleado  
		tab_accion_motivo_empleado=utilitario.consultar("SELECT ide_geded,ide_geame "
				+ "FROM gen_detalle_empleado_departame "
				+ "where ide_geded  in("+cambioContratoPorEmpleado+") ORDER BY IDE_GEDED DESC ");
		String motivoContratoPorEmpleado="";
	
	//	for (int j = 0; j < tab_accion_motivo_empleado.getTotalFilas()-1; j++) {
			motivoContratoPorEmpleado=tab_accion_motivo_empleado.getValor("IDE_GEAME");
			
			
		TablaGenerica tabTerminacionContrato=null;
		tabTerminacionContrato=utilitario.consultar("SELECT geame.ide_geame, geame.ide_geaed, geame.ide_gemed,  "
				+ "geame.activo_geame,geame.anterior_geame,geaed.detalle_geaed "
				+ "FROM  "
				+ "gen_accion_motivo_empleado geame "
				+ "left join gen_accion_empleado_depa  geaed on geaed.ide_geaed=geame.ide_geaed "
				+ "where geame.ide_geame="+motivoContratoPorEmpleado);
		
	
			
			if (tabTerminacionContrato.getValor("IDE_GEMED").equals("19")) {
				estadoContratacion=true;
				System.out.println("Empleado: "+tab_empleado.getValor("IDE_GTEMP"));
				fecha_comparacion=tab_empleado_departamento_par.getValor(x,"fecha_liquidacion_geedp");
				x=tab_empleado_departamento_par.getTotalFilas();
			}
		
		
		}
		
 
		
		//Si tiene motivo-accion como terminacion de contrato antes de una nueva contratacion 
		if (estadoContratacion==true) {
			System.out.println("fecha_comparacion"+fecha_comparacion);
		if (utilitario.getMes(utilitario.getFormatoFecha(fecha_comparacion))==utilitario.getMes(fecha_ingreso_emgirs)) {
			//consulto si tiene generado rol para el mes de liquidacion
			TablaGenerica  tab_detalle_rol_anterior= utilitario.consultar("SELECT * FROM NRH_DETALLE_ROL DROL  "
				+ "LEFT JOIN NRH_ROL ROL ON ROL.IDE_NRROL=DROL.IDE_NRROL "
				+ "LEFT JOIN GEN_PERIDO_ROL PER ON PER.IDE_GEPRO=ROL.IDE_GEPRO "
				+ "WHERE IDE_GEEDP IN (SELECT IDE_GEEDP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_GTEMP="+tab_empleado.getValor("IDE_GTEMP")+" "
				+ ") AND PER.IDE_GEPRO IN(select IDE_GEPRO from GEN_PERIDO_ROL where ((IDE_GEMES BETWEEN "+mes+" and "+mes+") "
				+ "and IDE_GEANI=(SELECT IDE_GEANI FROM GEN_ANIO WHERE lower (DETALLE_GEANI) like '"+anio+"')))");
			//Si tiene generado el mes anterior sumo 1 al mes
		if (tab_detalle_rol_anterior.getTotalFilas()>0) {
			fecha_ingreso_emgirs=anio+"-"+(mes+1)+"-01";
		}
		}
		}//fin bloque terminacion de contrato
		}//
		System.out.println("fecha_ingreso_mijin "+fecha_ingreso_emgirs);
		return fecha_ingreso_emgirs;
}

	/*
	 * Metodo getSueldoUltimoRol recibe el empleado y retorna el sueldo generado del ultimo mes
	 */
	public double getSueldoUltimoRol(String IDE_GEEDP){
	     double valor_nrdro=0.0;
		TablaGenerica tabEmpleado=utilitario.consultar("select ide_geedp,ide_gtemp from gen_empleados_departamento_par where ide_geedp="+IDE_GEEDP);
		
		TablaGenerica tabAccionesEmpleado=utilitario.consultar("select ide_geedp,ide_gtemp from gen_empleados_departamento_par "
				+ "where ide_gtemp="+tabEmpleado.getValor("IDE_GTEMP")+" ORDER BY IDE_GEEDP DESC");
			
		for (int i = 0; i < tabAccionesEmpleado.getTotalFilas(); i++) {
			System.out.println("select ide_nrdro,ide_nrrol,ide_nrder,valor_nrdro "
					+ "from nrh_detalle_rol drol "
					+ "where drol.ide_geedp="+tabAccionesEmpleado.getValor(i,"IDE_GEEDP")+" and ide_nrrol in( "
					+ "select ide_nrrol from nrh_rol "
					+ "where ide_gepro=(SELECT ide_gepro FROM gen_perido_rol where  ide_nrtit="+utilitario.getVariable("p_nrh_tipo_nomina_normal")+" order by ide_gepro desc limit 1) "
					+ "	) "
					+ "and ide_nrder in(select ide_nrder from nrh_detalle_rubro where ide_nrrub=288)");
			
			TablaGenerica tabsueldoRol = utilitario.consultar("select ide_nrdro,ide_nrrol,ide_nrder,valor_nrdro "
					+ "from nrh_detalle_rol drol "
					+ "where drol.ide_geedp="+tabAccionesEmpleado.getValor(i,"IDE_GEEDP")+" and ide_nrrol in( "
					+ "select ide_nrrol from nrh_rol "
					+ "where ide_gepro=(SELECT ide_gepro FROM gen_perido_rol where  ide_nrtit="+utilitario.getVariable("p_nrh_tipo_nomina_normal")+" order by ide_gepro desc limit 1) "
					+ "	) "
					+ "and ide_nrder in(select ide_nrder from nrh_detalle_rubro where ide_nrrub=288)"); 
		if (tabsueldoRol.getTotalFilas()>0) {
			valor_nrdro=Double.parseDouble(tabsueldoRol.getValor("VALOR_NRDRO"));
			i=tabAccionesEmpleado.getTotalFilas();
		}else {
			valor_nrdro=0.00;
		}			
		
		
		}
	return valor_nrdro;
		
	}
	







	
	public int diasTrabajadosEmpleado(String IDE_GEEDP){
		double dias=360;
		double valor_dias=0;
		double valor_dias_trabajados=0;
		int diferenciaDias=0;

		String fecha_comparacion="";
		boolean estadoContratacion=false;
		TablaGenerica tab_empleado= utilitario.consultar("select IDE_GEEDP,IDE_GTEMP from gen_empleados_departamento_par where ide_geedp="+IDE_GEEDP);
		
		
		TablaGenerica tab_empleado_departamento_par=null;
		tab_empleado_departamento_par=utilitario.consultar("select ide_geedp,ide_geded,fecha_geedp,fecha_liquidacion_geedp  "
				+ "from gen_empleados_departamento_par  "
				+ "where ide_gtemp="+tab_empleado.getValor("IDE_GTEMP")+" "
						+ " and ide_gttem !=3"
						+ "order by ide_geedp DESC ");
						//+ "limit 1");
		
		if (tab_empleado_departamento_par.getTotalFilas()>0) {
			
	
		String cambioContratoPorEmpleado="";
		//tabla gen_accion_motivo_empleado  
		TablaGenerica tab_accion_motivo_empleado=null;
		//for (int i = tab_empleado_departamento_par.getTotal
		for (int x = 0; x < tab_empleado_departamento_par.getTotalFilas(); x++) {

	
		cambioContratoPorEmpleado=tab_empleado_departamento_par.getValor(x,"IDE_GEDED");
		//Los ide_geame   que son los motivos de la tabla gen_accion_motivo_empleado  
		tab_accion_motivo_empleado=utilitario.consultar("SELECT ide_geded,ide_geame "
				+ "FROM gen_detalle_empleado_departame "
				+ "where ide_geded  in("+cambioContratoPorEmpleado+") ORDER BY IDE_GEDED DESC ");
		String motivoContratoPorEmpleado="";
	
	//	for (int j = 0; j < tab_accion_motivo_empleado.getTotalFilas()-1; j++) {
			motivoContratoPorEmpleado=tab_accion_motivo_empleado.getValor("IDE_GEAME");
			
			
		TablaGenerica tabTerminacionContrato=null;
		tabTerminacionContrato=utilitario.consultar("SELECT geame.ide_geame, geame.ide_geaed, geame.ide_gemed,  "
				+ "geame.activo_geame,geame.anterior_geame,geaed.detalle_geaed "
				+ "FROM  "
				+ "gen_accion_motivo_empleado geame "
				+ "left join gen_accion_empleado_depa  geaed on geaed.ide_geaed=geame.ide_geaed "
				+ "where geame.ide_geame="+motivoContratoPorEmpleado);
		
	
			
			if (tabTerminacionContrato.getValor("detalle_geaed").equals("CONTRATACION")) {
				estadoContratacion=true;
				System.out.println("Empleado: "+tab_empleado.getValor("IDE_GTEMP"));
				fecha_comparacion=tab_empleado_departamento_par.getValor(x,"fecha_geedp");
				x=tab_empleado_departamento_par.getTotalFilas();
			}
		
		
		}
		
 
		//Si tiene motivo-accion como terminacion de contrato antes de una nueva contratacion 
		if (estadoContratacion==true) {
		
		diferenciaDias=utilitario.getDiferenciasDeFechas(utilitario.DeStringADate(fecha_comparacion), utilitario.DeStringADate("2018-12-20"));
		if (diferenciaDias>Integer.parseInt(utilitario.getVariable("p_modulo_anticipos_dias_trabajados"))) {
			return diferenciaDias;
		}else {
			return diferenciaDias;
		}
		}//fin bloque terminacion de contrato
		
}
		return diferenciaDias;


	}



/**
 * 
 * @param ide_gtemp:Recibe el empleado o funcionario
 * @Retorna las diferentes acciones de personal(ide_geedp) creada para ese empleado y si no contiene ninguna accion retorna (-1)
 */
public String accionPersonalEmpleado(String ide_gtemp){

	StringBuilder 	str_ide= new StringBuilder();
	//variable contador
	int valorEmp=0;
	    //tabla generica con las acciones de personal de empleado
	    TablaGenerica tabEmpleado = utilitario.consultar("select  ide_gtemp,ide_geedp from gen_empleados_departamento_par where ide_gtemp in("+ide_gtemp+")");

      	if (tabEmpleado.getTotalFilas()>0) {
      	// Si contiene al menos una accion de personal
		for (int i = 0; i < tabEmpleado.getTotalFilas(); i++) 
 		{
 		//Voy anidando los ides de la accion
  	    str_ide.append(tabEmpleado.getValor(i, "IDE_GEEDP"));
        if (tabEmpleado.getTotalFilas()==1) {
        //si solo tiene una accion no agrego la (,)	
		}else if (valorEmp<=tabEmpleado.getTotalFilas()) {
			//Si tiene mas de una accion sumo 
			valorEmp++;
				if(valorEmp<(tabEmpleado.getTotalFilas())){
				//Si el contador es menor al numero de acciones de personal	
					//guardos los ide_geedp del empleado
					str_ide.append(",");
					}
			}

		}
      	//retorno los ide_geedp
		return str_ide.toString();
      	}else {
      		//si no contiene accion de personal retorno (-1)
			return "-1";
		}
}


/**
 * 
 * @param DETALLE_SRIMR : el detalle del año fiscal en curso
 * @param str_ide: Recibe las acciones de personal del empleado seleccionado
 * @return  retorna los valores generados en la base imponible hasta la fecha
 */
public TablaGenerica getBaseImponibleEmpleado (String DETALLE_SRIMR,String str_ide,String rubros){
    //Retona el ide_geani y el detalle del año fiscal en curso 
	TablaGenerica tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where detalle_geani like '%"+DETALLE_SRIMR+"%'");
	TablaGenerica tab_tot_ingresos_empleados_proyeccion=null;
	//Retorno los valores generados por concepto de base imponible del empleado
	return tab_tot_ingresos_empleados_proyeccion =utilitario.consultar("SELECT rol.ide_nrrol,rol.ide_gepro,ndrol.ide_geedp,ndrol.ide_nrder,rub.ide_nrrub,rub.detalle_nrrub, "
		+ "ndrol.valor_nrdro, "
		+ "gprol.ide_gemes,gprol.ide_geani,gprol.detalle_periodo_gepro  "
		+ "FROM  nrh_rol rol  "
		+ "left join NRH_DETALLE_ROL ndrol on ndrol.ide_nrrol=rol.ide_nrrol "
		+ "left join nrh_detalle_rubro ndrub on ndrub.ide_nrder=ndrol.ide_nrder "
		+ "left join nrh_rubro rub on rub.ide_nrrub=ndrub.ide_nrrub   "
		+ "left join gen_perido_rol gprol on gprol.ide_gepro= rol.ide_gepro  "
		+ "WHERE ndrol.IDE_NRROL IN(  "
		+ "SELECT IDE_NRROL FROM nrh_rol where ide_gepro in(select ide_gepro from GEN_PERIDO_ROL WHERE IDE_GEANI="+tabAnio.getValor("IDE_GEANI")+") order by ide_nrrol asc) "
		+ "AND   "
		+ "ndrol.IDE_NRDER IN (SELECT IDE_NRDER FROM NRH_DETALLE_RUBRO WHERE IDE_NRRUB IN("+rubros+")) AND "
		+ "ndrol.IDE_GEEDP IN("+str_ide.toString()+")  "
		+ "order by ndrol.ide_geedp asc ,ndrol.ide_nrrol asc");
	
}


public String retornaAccionpersonal(String IDE_GTEMP, String IDE_GEMED,String IDE_GEEDP){
TablaGenerica tab_empleado_departamento_par=null;
String fecha="";

if (IDE_GEEDP==null || IDE_GEEDP.equals("") || IDE_GEEDP.isEmpty()) {
	


tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
		+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
		+ "from gen_empleados_departamento_par epar "
		+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
		+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
		+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
		+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
		+ "where epar.ide_gtemp="+IDE_GTEMP+" and gmed.ide_gemed in("+IDE_GEMED+") and gaed.ide_geaed !=(3) "
		+ "and epar.activo_geedp=false "
		+ "order by ide_geedp desc");

/*if (tab_empleado_departamento_par.getTotalFilas()==0) {
	return fecha;
}else{
if (IDE_GEMED.equals("23")) {
		fecha=tab_empleado_departamento_par.getValor("FECHA_GEEDP");

}else if (IDE_GEMED.equals("19")) {
	fecha=tab_empleado_departamento_par.getValor("FECHA_LIQUIDACION_GEEDP");
	}*/
fecha=tab_empleado_departamento_par.getValor("FECHA_GEEDP");
if (fecha==null || fecha.equals("") || fecha.isEmpty()) {
	fecha="";
}
}else{
	
	tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
			+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
			+ "from gen_empleados_departamento_par epar "
			+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
			+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
			+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
			+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
			+ "where epar.ide_geedp IN("+IDE_GEEDP+") and gmed.ide_gemed in("+IDE_GEMED+") and gaed.ide_geaed !=(3) "
			+ "and epar.activo_geedp=false "
			+ "order by ide_geedp desc "
			+ "limit 1");	
 
	if (tab_empleado_departamento_par.getValor("fecha_geedp")==null || tab_empleado_departamento_par.getValor("fecha_geedp").equals("") || tab_empleado_departamento_par.getValor("fecha_geedp").isEmpty()) {
		fecha="";
	}else {
		fecha=tab_empleado_departamento_par.getValor("fecha_geedp");
	}

	
}
return fecha;		
//}

}





public String retornaAccionpersonalIngreso(String IDE_GTEMP, String IDE_GEMED,String IDE_GEEDP){
	TablaGenerica tab_empleado_departamento_par=null;
	String fecha="";

		tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
				+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
				+ "from gen_empleados_departamento_par epar "
				+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
				+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
				+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
				+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
				+ "where epar.ide_geedp IN("+IDE_GEEDP+") and gmed.ide_gemed in("+IDE_GEMED+") and gaed.ide_geaed !=(3) "
				+ "and epar.activo_geedp=false "
				+ "order by ide_geedp desc "
				+ "limit 1");


		fecha=tab_empleado_departamento_par.getValor("IDE_GEEDP");
			if (fecha==null || fecha.equals("") || fecha.isEmpty()) {
				fecha="";
			}else {
				fecha=tab_empleado_departamento_par.getValor("IDE_GEEDP");
				TablaGenerica tab_accion=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
						+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
						+ "from gen_empleados_departamento_par epar "
						+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
						+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
						+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
						+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
						+ "where epar.ide_geedp IN("+fecha+") and gmed.ide_gemed in(7,8) and gaed.ide_geaed !=(3) "
						+ "and epar.activo_geedp=false "
						+ "order by ide_geedp desc "
						+ "limit 1");
				
				if (tab_accion.getTotalFilas()>0) {
					
				}else {
					fecha="";
				}			
			}			
			return fecha;		

}





public String retornaAccionpersonalLiquidacion(String IDE_GTEMP){
TablaGenerica tab_empleado_departamento_par=null;
String fecha="";

tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
		+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
		+ "from gen_empleados_departamento_par epar "
		+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
		+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
		+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
		+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
		+ "where epar.ide_gtemp="+IDE_GTEMP+" and gaed.ide_geaed in(3) "
		+ "order by ide_geedp desc");
 	fecha=tab_empleado_departamento_par.getValor("FECHA_LIQUIDACION_GEEDP");
	return fecha;		

}

public static String getIde_rol_amortizacion() {
	return ide_rol_amortizacion;
}

public static void setIde_rol_amortizacion(String ide_rol_amortizacion) {
	ServicioLiquidacion.ide_rol_amortizacion = ide_rol_amortizacion;
}

			
public String getSqlEmpleados(String empleado){
		String sql_empleadosActivos="SELECT  "
				+ "epar.ide_geedp, "
				+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
				+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
				+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
				+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  "
				+ "SUCU.NOM_SUCU, AREA.DETALLE_GEARE,  "
				+ "DEPA.DETALLE_GEDEP,  "
				+ "GTT.DETALLE_GTTEM "
				+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP   "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU  "
				+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP  "
				+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
				+ "left join gth_tipo_empleado gtt on gtt.ide_gttem=epar.ide_gttem "
				+ "WHERE EPAR.ide_geedp in("+empleado+") "
				+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP ";
		
		return sql_empleadosActivos;
	
	}


/**
 * Metodo devuelve true si contiene generado rol normal el empleado
 * @param ide_geedp
 * @param fecha_inicial_gepro
 * @param fecha_final_gepro
 * @return
 */


public Boolean accionPersonalAnterior(String ide_geedp,String IDE_GEPRO){
String valor="";
boolean dias_exceso=false;


TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem ,fecha_finctr_geedp from gen_empleados_departamento_par where ide_geedp="+ide_geedp);
String fecha_iniTemp="";
//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
String a="";
int mesLiquidacion=0;
TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
		+ "order by ide_geedp asc");
String ide_geedp_empleado="";
//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
//variable guarda la fecha de salida
String fecha_finTemp="";
fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
//variable cuenta el numero de acciones
int contAccionInvalida=0,contForAccionInvalida=0;
StringBuilder str_ide_geedp = new StringBuilder();

for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
	if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(ide_geedp)==true) {
		String valorUltimoElemento="";
		valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
		if(valorUltimoElemento.equals(",")==true){
			str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
		}else {
		}
		i=tabEmpleados.getTotalFilas();
	}else{
			//Voy anidando los ides de la accion
			str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
           // valor++;
            if (tabEmpleados.getTotalFilas()==1) {
			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
				contForAccionInvalida++;
					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
					str_ide_geedp.append(",");
               // System.out.println("str_ide:  "+str_ide_empleado);
					}
			}	 	
	}
}


//Si no contiene acciones de personal luego de la liquidacion
if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
	str_ide_geedp.append(ide_geedp);
}


//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");

a=utilitario.getVariable("p_nrh_acciones_personal");
String[] listaAccionesEmpleado;
listaAccionesEmpleado=a.split(",");
for (int i = 0; i < listaAccionesEmpleado.length; i++) {
	fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
	if (!fecha_iniTemp.equals("")) {
		i=listaAccionesEmpleado.length;
	}
}

ide_geedp_empleado=ide_geedp;
System.out.println("fecha_finTemp"+fecha_finTemp);
String fecha_fin_validacion="";
if (utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp))>=31) {
	fecha_fin_validacion=utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-30";
}else {
	fecha_fin_validacion=utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-"+utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp));
}
	
System.out.println("fecha_finTemp"+fecha_fin_validacion);


TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_finTemp)+"%' ");


TablaGenerica tab_liquidacion = utilitario.consultar("SELECT ide_gepro, ide_nrtit, ide_gemes, ide_geani, fecha_inicial_gepro,  "
		+ "fecha_final_gepro, detalle_periodo_gepro,  "
		+ "activo_gepro,gen_ide_gepro  "
		+ "FROM gen_perido_rol "
		+ "where ide_geani="+tabAnio.getValor("IDE_GEANI")+" and ide_gemes="+utilitario.getMes(fecha_finTemp)
		+ " order by ide_gepro desc ");


TablaGenerica tab_periodo_rol = utilitario.consultar("select * from nrh_rol where ide_gepro="+tab_liquidacion.getValor("IDE_GEPRO")+" "
		+ "and ide_nrdtn IN(2,4) AND ide_nresr=1 "
		+ " order by ide_nrrol asc");
if (tab_periodo_rol.getTotalFilas()<=0) {
	dias_exceso=false;
}else {
	System.out.println("INGRESO A EXCESO");
int cont=0;
StringBuilder str = new StringBuilder();
for (int i = 0; i < tab_periodo_rol.getTotalFilas(); i++) {
  
  		//Voy anidando los ides de la accion
          	    str.append(tab_periodo_rol.getValor(i, "IDE_NRROL"));
               // valor++;
                if (tab_periodo_rol.getTotalFilas()==1) {
   			}else if (cont<=tab_periodo_rol.getTotalFilas()) {
   					cont++;
   					if(cont<(tab_periodo_rol.getTotalFilas())){
                    str.append(",");
                   // System.out.println("str_ide:  "+str_ide_empleado);
   					}
    			}
   			 		         		
}

TablaGenerica tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
		+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
		+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
		+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
		+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
		+ "where DRO.IDE_GEEDP in ( "
		+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
		+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp+") and ide_gttem !=3  "
		+ ")  "
		+ "and rol.ide_nrrol IN ("+str.toString()+")");  

/*+ "select * from nrh_detalle_rol where ide_nrrol in("+str.toString()+") "
		+ "and ide_geedp in("+ide_geedp_empleado+")"); */ 
if(tab_detalle_rol.getTotalFilas()>0){
		dias_exceso=true;
	}else {
		dias_exceso=false;
	}
}

System.out.println("Estado dias exceso"+dias_exceso);
return dias_exceso; 
}
			
public double retornaValorRubro(String ide_nrrub){
	double valorRubro=0;
	TablaGenerica tabRubro=utilitario.consultar("select  ide_nrder, ide_nrrub, ide_nrdtn, ide_gereg, formula_nrder, orden_nrder, "
			+ "orden_imprime_nrder, fecha_inicial_nrder, fecha_final_nrder  "
			+ "from nrh_detalle_rubro  "
			+ "where ide_nrrub in("+ide_nrrub+") "
			+ "and ide_nrdtn in(4,2) "
			+ "order by ide_nrder desc");	
	if (tabRubro.getTotalFilas()>0) {
		valorRubro=Double.parseDouble(tabRubro.getValor("formula_nrder"));
	}else {
		valorRubro=0.0;
	}
	return valorRubro;
}



public double getSumatoriaRubro1AjusteLiquidacionDecimoC(String IDE_GEEDP,String IDE_NRRUB,String fecha_ini,String fecha_fin, int tipoDecimo){
	double dou_sumatoria=0;
	if (fecha_ini!=null && !fecha_ini.isEmpty()
			&& fecha_fin!=null && !fecha_fin.isEmpty()
			&& IDE_GEEDP!=null && !IDE_GEEDP.isEmpty()
			&& IDE_NRRUB!=null && !IDE_NRRUB.isEmpty()){

		String str_sql="";

		TablaGenerica tab_empleado=utilitario.consultar("select ide_geedp,ide_gtemp,fecha_finctr_geedp  "
				+ "from gen_empleados_departamento_par  "
				+ "where ide_geedp="+IDE_GEEDP+"");
		
		String fecha_iniTemp="";
		//retornaAccionpersonal(tab_empleado.getValor("IDE_GTEMP"), "23");
		String a="";
		
		TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_empleado.getValor("ide_gtemp")+") "
				+ "order by ide_geedp asc");
		String ide_geedp_empleado="";
		//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
		//variable guarda la fecha de salida
		String fecha_finTemp="";
		fecha_finTemp=tab_empleado.getValor("fecha_finctr_geedp");
		//variable cuenta el numero de acciones
		int contAccionInvalida=0,contForAccionInvalida=0;
		StringBuilder str_ide_geedp = new StringBuilder();

		for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
			if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(IDE_GEEDP)==true) {
				String valorUltimoElemento="";
				valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
				if(valorUltimoElemento.equals(",")==true){
					str_ide_geedp.deleteCharAt(str_ide_geedp.length() - 1);
				}else {
				}
				i=tabEmpleados.getTotalFilas();
			}else{
					//Voy anidando los ides de la accion
					str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
		           // valor++;
		            if (tabEmpleados.getTotalFilas()==1) {
					}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
						contForAccionInvalida++;
							if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
							str_ide_geedp.append(",");
		               // System.out.println("str_ide:  "+str_ide_empleado);
							} 
					}	 	
			}
		}


		//Si no contiene acciones de personal luego de la liquidacion
		if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
			str_ide_geedp.append(IDE_GEEDP);
		}


		//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");

		a=utilitario.getVariable("p_nrh_acciones_personal");
		String[] listaAccionesEmpleado;
		listaAccionesEmpleado=a.split(",");
		for (int i = 0; i < listaAccionesEmpleado.length; i++) {
			fecha_iniTemp=retornaAccionpersonal(tab_empleado.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
			if (!fecha_iniTemp.equals("")) {
				i=listaAccionesEmpleado.length;
			}
		}

	
		
		
		String fecha_comaracion=utilitario.getAnio(fecha_finTemp)+"-01-01";

		String fecha_comparacionCuarto="2017-08-01";
		String fecha_comparacionCuartoRango="2018-08-01";
		String fecha_calculoFinInicio="2018-"+(utilitario.getMes(fecha_fin)-1)+"-01";
		String mes=utilitario.getUltimoDiaMesFecha(fecha_calculoFinInicio);
		String fecha=utilitario.getAnio(fecha_finTemp)+"-"+(utilitario.getMes(fecha_finTemp)-1)+"-"+utilitario.getDia(mes);
		
		fecha_fin=fecha;
		System.out.println("DECIMOS AJUSTE");
		System.out.println("FECHA_INICIO"+fecha_iniTemp);
		System.out.println("FECHA_FIN"+fecha_finTemp);
		if (tipoDecimo==3) {
			if (fecha_iniTemp.compareTo(fecha_comaracion)<=0) {
				fecha_ini=fecha_comaracion;
				fecha_fin="2018-"+(utilitario.getMes(fecha_finTemp)-1)+"-"+utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp));

		}else {
				fecha_ini=fecha_iniTemp;
				//fecha_ini=fecha_finTemp;
				fecha_fin=utilitario.getAnio(fecha_finTemp)+"-"+(utilitario.getMes(fecha_finTemp)-1)+"-"+utilitario.consultar(utilitario.getUltimoDiaMesFecha(fecha_finTemp));

			}		
		}else if(tipoDecimo==4){
			System.out.println("DECIMO CUARTO");
			int mesTemp=0;
						
		if (fecha_iniTemp.compareTo(fecha_comparacionCuarto)<=0) {
			fecha_ini=fecha_comparacionCuarto;
			}else if(fecha_iniTemp.compareTo(fecha_comparacionCuarto)>0) {
				fecha_ini=fecha_iniTemp;
			}
		
		if (fecha_finTemp.compareTo(fecha_comparacionCuartoRango)<=0) {
		fecha_fin=fecha_finTemp;
		}else {
			fecha_fin=fecha_finTemp;
			fecha_ini=fecha_comparacionCuartoRango;
		}
		
		}
			
			else {
			fecha_ini=fecha_iniTemp;
			fecha_fin=fecha_finTemp;
		}
	
		  
	//Metodo devuelve el total de decimo cuarto	
		String ide_gepro=getPeriodosRol(fecha_ini, fecha_fin);
		   String str_ide_gepro="";

			TablaGenerica tab_acciones =utilitario.consultar("select "
					+ "pro.ide_gepro,drol.ide_nrdro,drol.valor_nrdro,pro.ide_gemes,pro.ide_geani,DROL.IDE_NRDER  from nrh_rol rol "
					+ "left join nrh_detalle_rol drol on drol.ide_nrrol=rol.ide_nrrol "
					+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO "
					+ "where drol.ide_nrder in(select ide_nrder from nrh_detalle_rubro where ide_nrrub in(121) "
					+ "and pro.ide_gepro in ("+ide_gepro+") and drol.ide_geedp in("+str_ide_geedp.toString()+") and drol.valor_nrdro>0.0) "
							//+ "and drol.ide_nrdtn in(2,4)) "
					+ "order by pro.ide_geani asc ,pro.ide_gemes asc");
			
			if (tab_acciones.getTotalFilas()>0) {
				for (int i = 0; i < tab_acciones.getTotalFilas(); i++) {
				if ((tab_acciones.getTotalFilas()-1)==i) {
						
					str_ide_gepro+=tab_acciones.getValor(i, "IDE_GEPRO");
						}else {
							str_ide_gepro+=tab_acciones.getValor(i, "IDE_GEPRO")+",";	
						}
				}
			
			
			if (str_ide_gepro==null || str_ide_gepro.equals("") || str_ide_gepro.equals("")) {
				ide_gepro="-1";		
			}else{
			ide_gepro=str_ide_gepro;
			}
		
			}
	
		   
		   
		double valor_prov=0.0,rmuAnterior=0.0,rmuActual=0.0,valorAnteriorDecimo=0.0,valorActualDecimo=0.0,valorNuevo=0.0;
		
		rmuActual=retornaValorRubro("276");
		valorActualDecimo=rmuActual/12;
		valorActualDecimo=Double.parseDouble(utilitario.getFormatoNumero((Math.rint(valorActualDecimo*100)/100),2));
		rmuAnterior=retornaValorRubro("284");
		valorAnteriorDecimo=rmuAnterior/12;
		valorAnteriorDecimo=Double.parseDouble(utilitario.getFormatoNumero((Math.rint(valorAnteriorDecimo*100)/100),2));
		if (ide_gepro!=null && !ide_gepro.equals("-1")){
			try{
				str_sql="select pro.ide_gemes,pro.ide_geani,dro.ide_geedp,dro.VALOR_NRDRO  from NRH_DETALLE_ROL DRO   "
						+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
						+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
						+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
						+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
						+ "where DRO.IDE_GEEDP in (   "
						+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (   "
						+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+IDE_GEEDP+") and ide_gttem !=3   "
						+ ")   "
						+ "AND RUB.IDE_NRRUB in (274)   "
						+ "and PRO.IDE_GEPRO IN ("+ide_gepro+") AND DER.IDE_NRDTN IN(4,2)	 "
						+ "order by pro.ide_geani asc, pro.ide_gemes asc	";

				//System.out.println("QUERY luis "+str_sql);
				//System.out.println("QUERY luis IDE_NRRUB "+IDE_NRRUB);
				double valorTotal=0.0;
				if(tipoDecimo==4){
					TablaGenerica tab_det_rub2=utilitario.consultar(str_sql);
					if (tab_det_rub2.getTotalFilas()>0){
						for (int i = 0; i < tab_det_rub2.getTotalFilas(); i++){
							if(tab_det_rub2.getValor(i,"VALOR_NRDRO")==null || tab_det_rub2.getValor(i,"VALOR_NRDRO").isEmpty() || tab_det_rub2.getValor(i,"VALOR_NRDRO").equals("")){
						valorTotal=valorTotal+0.0;
						}else {
							double valorTemp=0.0,valorMes=0.0,valorMesTemp=0.0;
							int valorDia=0;
							valorTemp=32.83;
							valorDia=(int)Double.parseDouble(tab_det_rub2.getValor(i,"VALOR_NRDRO"));
							if (valorDia==31 || valorDia==30) {
								valorMesTemp=valorMes;
							}else if (valorDia==0){
								valorMesTemp=0.0;
							}else {
								valorTemp=Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(utilitario.getFormatoNumero((valorDia*32.83),2))/30,2));
							}
							 valorTotal=Double.parseDouble(utilitario.getFormatoNumero((valorTotal+valorTemp),2));
						}
						}
					}
					dou_sumatoria=valorTotal;
					return dou_sumatoria;
				
				}
				else{
					dou_sumatoria=0;
				}
					/*
				TablaGenerica tab_det_rub2=utilitario.consultar(str_sql);
				if (tab_det_rub2.getTotalFilas()>0)
				{

						for (int i = 0; i < tab_det_rub2.getTotalFilas(); i++) 
						{
							if (tab_det_rub2.getValor(i,"sumatoria_rubro")!=null && !tab_det_rub2.getValor(i,"sumatoria_rubro").isEmpty()){
								try {
									if(tipoDecimo==4){
									valor_prov=pckUtilidades.CConversion.CDbl_2(tab_det_rub2.getValor(i,"sumatoria_rubro"));
									if (valor_prov<valorAnteriorDecimo) {
										valorNuevo=valor_prov;
									}else if (valor_prov==valorAnteriorDecimo) {
										valorNuevo=valorActualDecimo;
									}else {
										if (valor_prov==valorActualDecimo) {
											valorNuevo=valorActualDecimo;
										}
									}	
									dou_sumatoria+=valorNuevo;					
				
									}else {
										dou_sumatoria+=pckUtilidades.CConversion.CDbl_2(tab_det_rub2.getValor(i,"sumatoria_rubro"));					
									}
									
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						}
					
				}
				
				}*///else de si es tipo decimo cuarto

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("error al ejecutar sql "+str_sql);
			}
		}else{
			dou_sumatoria=0.0;
		}
	}
		
	
	
	//System.out.println("QUERY luis dou_sumatoria "+dou_sumatoria);

	return dou_sumatoria;
}


public Boolean diasExcesoLiquidacion(String ide_geedp,String IDE_GEPRO){
String valor="";
boolean dias_exceso=false;


TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+ide_geedp);
String fecha_iniTemp="";
//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
String a="";
TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
		+ "order by ide_geedp asc");
String ide_geedp_empleado="";
//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
//variable guarda la fecha de salida
String fecha_finTemp="";
fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
//variable cuenta el numero de acciones
int contAccionInvalida=0,contForAccionInvalida=0;
StringBuilder str_ide_geedp = new StringBuilder();

for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
	if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(ide_geedp)==true) {
		String valorUltimoElemento="";
		valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
		if(valorUltimoElemento.equals(",")==true){
			str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
		}else {
		}

	i=tabEmpleados.getTotalFilas();
	}else{
			//Voy anidando los ides de la accion
			str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
           // valor++;
            if (tabEmpleados.getTotalFilas()==1) {
			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
				contForAccionInvalida++;
					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
					str_ide_geedp.append(",");
               // System.out.println("str_ide:  "+str_ide_empleado);
					}
			}	 	
	}
}


//Si no contiene acciones de personal luego de la liquidacion
if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
	str_ide_geedp.append(ide_geedp);
}


//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");

a=utilitario.getVariable("p_nrh_acciones_personal");
String[] listaAccionesEmpleado;
listaAccionesEmpleado=a.split(",");
for (int i = 0; i < listaAccionesEmpleado.length; i++) {
	fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
	if (!fecha_iniTemp.equals("")) {
		i=listaAccionesEmpleado.length;
	}
}



 ide_geedp_empleado="";
ide_geedp_empleado=ide_geedp;
System.out.println("fecha_finTemp"+fecha_finTemp);
String fecha_fin_validacion="";
if (utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp))>=31) {
	fecha_fin_validacion=utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-30";
}else {
	fecha_fin_validacion=utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-"+utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp));
}
	
System.out.println("fecha_finTemp"+fecha_fin_validacion);


TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_finTemp)+"%' ");


TablaGenerica tab_liquidacion = utilitario.consultar("SELECT ide_gepro, ide_nrtit, ide_gemes, ide_geani, fecha_inicial_gepro,  "
		+ "fecha_final_gepro, detalle_periodo_gepro,  "
		+ "activo_gepro,gen_ide_gepro  "
		+ "FROM gen_perido_rol "
		+ "where ide_geani="+tabAnio.getValor("IDE_GEANI")+" and ide_gemes="+utilitario.getMes(fecha_finTemp)
		+ " order by ide_gepro desc ");


TablaGenerica tab_periodo_rol = utilitario.consultar("select * from nrh_rol where ide_gepro="+tab_liquidacion.getValor("IDE_GEPRO")+" "
		+ "and ide_nrdtn IN(2,4) AND ide_nresr=1 "
		+ " order by ide_nrrol asc");
if (tab_periodo_rol.getTotalFilas()<=0) {
	dias_exceso=false;
}else {
	System.out.println("INGRESO A EXCESO");
int cont=0;
StringBuilder str = new StringBuilder();
for (int i = 0; i < tab_periodo_rol.getTotalFilas(); i++) {
  
  		//Voy anidando los ides de la accion
          	    str.append(tab_periodo_rol.getValor(i, "IDE_NRROL"));
               // valor++;
                if (tab_periodo_rol.getTotalFilas()==1) {
   			}else if (cont<=tab_periodo_rol.getTotalFilas()) {
   					cont++;
   					if(cont<(tab_periodo_rol.getTotalFilas())){
                    str.append(",");
                   // System.out.println("str_ide:  "+str_ide_empleado);
   					}
    			}
   			 		         		
}

TablaGenerica tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
		+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
		+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
		+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
		+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
		+ "where DRO.IDE_GEEDP in ( "
		+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
		+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp+") and ide_gttem !=3  "
		+ ")  "
		+ "and rol.ide_nrrol IN ("+str.toString()+")");  

/*+ "select * from nrh_detalle_rol where ide_nrrol in("+str.toString()+") "
		+ "and ide_geedp in("+ide_geedp_empleado+")"); */ 
if(tab_detalle_rol.getTotalFilas()>0){
		dias_exceso=true;
	}else {
		dias_exceso=false;
	}
}

System.out.println("Estado dias exceso"+dias_exceso);
return dias_exceso; 
}






//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////METODO RETORNA DIAS PENDIENTES DE VACACIONES Y LABORADOS //////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public String getDatosCodigoTrabajo(String IDE_GEEDP, String fecha_ini_dec, String fecha_fin_dec,int tipoDecimo,String ide_gttem,String ide_geedp,int valorDiasPendientesLaborados){
	
	String valor="";
	TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,rmu_geedp,fecha_finctr_geedp  "
			+ "from gen_empleados_departamento_par where ide_geedp="+IDE_GEEDP);
	
	TablaGenerica tab_periodo_vacacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
			+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
			+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac "
			+ " FROM asi_vacacion "
			+ "where ide_gtemp="+tab_emp.getValor("IDE_GTEMP")+" AND ACTIVO_ASVAC=false");

	if (tab_periodo_vacacion.getTotalFilas()>0) {
		

	 
	String fecha_inicio="",fecha_fin="";
	String fecha_iniTemp="";
	//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
	String a="";
	
	TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
			+ "order by ide_geedp asc");
	String ide_geedp_empleado="";
	//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
	//variable guarda la fecha de salida
	String fecha_finTemp="";
	fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
	//variable cuenta el numero de acciones
	int contAccionInvalida=0,contForAccionInvalida=0;
	StringBuilder str_ide_geedp = new StringBuilder();

	for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
		if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(ide_geedp)==true) {
			String valorUltimoElemento="";
			valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
			if(valorUltimoElemento.equals(",")==true){
				str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
			}else {
			}

			i=tabEmpleados.getTotalFilas();
		}else{
				//Voy anidando los ides de la accion
				str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
	           // valor++;
	            if (tabEmpleados.getTotalFilas()==1) {
				}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
					contForAccionInvalida++;
						if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
						str_ide_geedp.append(",");
	               // System.out.println("str_ide:  "+str_ide_empleado);
						}
				}	 	
		}
	}


	//Si no contiene acciones de personal luego de la liquidacion
	if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
		str_ide_geedp.append(ide_geedp);
	}


	//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");

	a=utilitario.getVariable("p_nrh_acciones_personal");
	String[] listaAccionesEmpleado;
	listaAccionesEmpleado=a.split(",");
	for (int i = 0; i < listaAccionesEmpleado.length; i++) {
		fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
		if (!fecha_iniTemp.equals("")) {
			i=listaAccionesEmpleado.length;
		}
	}

	
	
	
	String ide_asvac="";
	if (tab_periodo_vacacion.getTotalFilas()>0) {
		for (int i = 0; i < tab_periodo_vacacion.getTotalFilas(); i++) {
			fecha_inicio=tab_periodo_vacacion.getValor(i,"fecha_ingreso_asvac");
			fecha_fin=tab_periodo_vacacion.getValor(i,"fecha_finiquito_asvac");
			if (fecha_iniTemp.compareTo(fecha_inicio)==0) {
				//if (fecha_finTemp.compareTo(fecha_fin)==0) {
					ide_asvac=tab_periodo_vacacion.getValor(i,"ide_asvac");
					i=tab_periodo_vacacion.getTotalFilas();

				//}
			}
		}
		
		
		
		if (ide_asvac==null || ide_asvac.equals("") || ide_asvac.isEmpty()) {
			tab_periodo_vacacion= new TablaGenerica();
			tab_periodo_vacacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
					+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
					+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac,fecha_liquidacion_asvac,ide_geedp_liquidacion "
					+ " FROM asi_vacacion "
					+ "where ide_gtemp="+tab_emp.getValor("IDE_GTEMP")+" AND fecha_liquidacion_asvac is not null");	
			
			boolean band=false;
			if (tab_periodo_vacacion.getTotalFilas()>0) {
			for (int i = 0; i < tab_periodo_vacacion.getTotalFilas(); i++) {
				for (int j = 0; j < tabEmpleados.getTotalFilas(); j++) {
					if (tabEmpleados.getValor(j, "IDE_GEEDP").compareTo(tab_periodo_vacacion.getValor(i,"ide_geedp_liquidacion"))==0) {		
					band=true;
					ide_asvac=tab_periodo_vacacion.getValor(i,"ide_asvac");
					i=tab_periodo_vacacion.getTotalFilas();
					j=tabEmpleados.getTotalFilas();
					}else {
						band=false;
					}
					}
				}
			}else {
				
			} 
				
			
		}else {
			
			tab_periodo_vacacion= new TablaGenerica();
			tab_periodo_vacacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
					+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
					+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac,fecha_liquidacion_asvac,ide_geedp_liquidacion "
					+ " FROM asi_vacacion "
					+ "where ide_gtemp="+tab_emp.getValor("IDE_GTEMP")+" AND fecha_liquidacion_asvac is not null");	
			
			boolean band=false;
			if (tab_periodo_vacacion.getTotalFilas()>0) {
			for (int i = 0; i < tab_periodo_vacacion.getTotalFilas(); i++) {
				for (int j = 0; j < tabEmpleados.getTotalFilas(); j++) {
					if (tabEmpleados.getValor(j, "IDE_GEEDP").compareTo(tab_periodo_vacacion.getValor(i,"ide_geedp_liquidacion"))==0) {		
					band=true;
					ide_asvac=tab_periodo_vacacion.getValor(i,"ide_asvac");
					i=tab_periodo_vacacion.getTotalFilas();
					j=tabEmpleados.getTotalFilas();
					}else {
						band=false;
					}
					}
				}
			}else{}
			
		}
		
		
		
		
		
		
		
	}


	TablaGenerica tab_periodo_vacacion_liquidacion=utilitario.consultar("SELECT ide_asvac, ide_gtemp, fecha_ingreso_asvac, fecha_finiquito_asvac, "
			+ "obervacion_asvac, activo_asvac,dias_pendientes_asvac, dias_tomados_asvac, "
			+ "nro_dias_ajuste_asvac, nro_dias_ajuste_periodo_asvac "
			+ " FROM asi_vacacion "
			+ "where  ide_gtemp="+tab_emp.getValor("IDE_GTEMP") 
			+ " and ide_asvac="+ide_asvac
			+ "order by ide_asvac desc "
			+ "limit 1");
			//+ "activo_asvac=false );
	
	try {
		
			
		    int valorTemp=0;
			
		//Anterior	
		//valor=serv_asistencia.getDatosEmpleadoCodigoTrabajo(tab_emp.getValor("IDE_GTEMP"),Integer.parseInt(tab_emp.getValor("IDE_GTTEM")),tab_periodo_vacacion_liquidacion.getValor("IDE_ASVAC"),tab_periodo_vacacion_liquidacion.getValor("fecha_ingreso_asvac"),tab_periodo_vacacion_liquidacion.getValor("fecha_finiquito_asvac"));

			//Nuevo
			
			int bandSalidaAjuste=0;
			System.out.println("p_nrh_dias_exceso_liquidacion");
			//return getDiasVacacionesAcumuladas(ide_geedp,fecha_inicial_gepro,fecha_final_gepro,4);
			TablaGenerica tab_emp1=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+ide_geedp);
			fecha_iniTemp="";
			//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
			 a="";
			 
			 
			 
			 
			 
			 
				tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
						+ "order by ide_geedp asc");
				ide_geedp_empleado="";
				//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
				//variable guarda la fecha de salida
				fecha_finTemp="";
				fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
				//variable cuenta el numero de acciones
				contAccionInvalida=0;contForAccionInvalida=0;
				str_ide_geedp = new StringBuilder();

				for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
					if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(ide_geedp)==true) {
						String valorUltimoElemento="";
						valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
						if(valorUltimoElemento.equals(",")==true){
							str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
						}else {
						}
					i=tabEmpleados.getTotalFilas();
					}else{
							//Voy anidando los ides de la accion
							str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
				           // valor++;
				            if (tabEmpleados.getTotalFilas()==1) {
							}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
								contForAccionInvalida++;
									if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
									str_ide_geedp.append(",");
				               // System.out.println("str_ide:  "+str_ide_empleado);
									}
							}	 	
					}
				}


				//Si no contiene acciones de personal luego de la liquidacion
				if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
					str_ide_geedp.append(ide_geedp);
				}


				//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");

				a=utilitario.getVariable("p_nrh_acciones_personal");
				String[] listaAccionesEmpleado1;
				listaAccionesEmpleado1=a.split(",");
				for (int i = 0; i < listaAccionesEmpleado1.length; i++) {
					fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado1[i].toString(),str_ide_geedp.toString());
					if (!fecha_iniTemp.equals("")) {
						i=listaAccionesEmpleado.length;
					}
				}
			 
			 
			System.out.println("fecha_finTemp"+fecha_finTemp);
			String fecha_fin_validacion="";
			if (utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp))>=31) {
				fecha_fin_validacion=utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-30";
				fecha_finTemp=utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-30";
			}else {
				fecha_fin_validacion=utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-"+utilitario.getDia(fecha_finTemp);
			}
				
			System.out.println("fecha_finTemp"+fecha_fin_validacion);
			
			
			TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_finTemp)+"%' ");
			
			
			TablaGenerica tab_liquidacion = utilitario.consultar("SELECT ide_gepro, ide_nrtit, ide_gemes, ide_geani, fecha_inicial_gepro,  "
					+ "fecha_final_gepro, detalle_periodo_gepro,  "
					+ "activo_gepro,gen_ide_gepro  "
					+ "FROM gen_perido_rol "
					+ "where ide_geani="+tabAnio.getValor("IDE_GEANI")+" and ide_gemes="+utilitario.getMes(fecha_finTemp)
					+ " order by ide_gepro desc ");


			TablaGenerica tab_periodo_rol = utilitario.consultar("select * from nrh_rol where ide_gepro="+tab_liquidacion.getValor("IDE_GEPRO")+" "
					+ "and ide_nrdtn IN(2,4) AND ide_nresr=1 "
					+ " order by ide_nrrol asc");
			if (tab_periodo_rol.getTotalFilas()==0) {
				valor=""+0;
			}else {
			
				System.out.println("INGRESO A EXCESO");
			int cont=0;
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < tab_periodo_rol.getTotalFilas(); i++) {
			  
			  		//Voy anidando los ides de la accion
			          	    str.append(tab_periodo_rol.getValor(i, "IDE_NRROL"));
			               // valor++;
			                if (tab_periodo_rol.getTotalFilas()==1) {
			   			}else if (cont<=tab_periodo_rol.getTotalFilas()) {
			   					cont++;
			   					if(cont<(tab_periodo_rol.getTotalFilas())){
			                    str.append(",");
			                   // System.out.println("str_ide:  "+str_ide_empleado);
			   					}
			    			}
			   			 		         		
			}
			
			
			TablaGenerica tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
					+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
					+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
					+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
					+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
					+ "where DRO.IDE_GEEDP in ( "
					+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
					+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp+") and ide_gttem !=3  "
					+ ")  "
					+ "and rol.ide_nrrol IN ("+str.toString()+")");  
			
			/*TablaGenerica tab_detalle_rol = utilitario.consultar("select * from nrh_detalle_rol where ide_nrrol in("+str.toString()+") "
					+ "and ide_geedp in("+ide_geedp_empleado+")"); */ 
	
		 
		    if (tab_detalle_rol.getTotalFilas()==0) {
		    	valor=""+0;
		    	bandSalidaAjuste=0;
			}else {
				valorTemp=(int)(utilitario.getDiferenciaFechas360(utilitario.getFecha(fecha_finTemp), utilitario.getFecha(fecha_fin_validacion))-1);
				 if (valorTemp<=0.0 || valorTemp<=0) {
					valor=""+0;
					bandSalidaAjuste=1;
				}else {
					valorTemp=(int)(utilitario.getDiferenciaFechas360(utilitario.getFecha(fecha_finTemp), utilitario.getFecha(fecha_fin_validacion))-1);
					//String valorNuevo=""+valorTemp;
					bandSalidaAjuste=1;
				}
					
					

			}
		    
		 /*   valorTemp=utilitario.getDiferenciaFechas360(utilitario.getFecha(fecha_finTemp), utilitario.getFecha(fecha_fin_validacion))-1;
			if (valorTemp<=0.0) {
				valor=""+0.0;
			}else {
			}
			*/
			
			}
			
		if (bandSalidaAjuste==0 ) {
		return	serv_asistencia.getDiasLaboradosPendientesCT(tab_emp.getValor("IDE_GTEMP"),Integer.parseInt(tab_emp.getValor("IDE_GTTEM")),ide_asvac,tab_periodo_vacacion_liquidacion.getValor("fecha_ingreso_asvac"),tab_periodo_vacacion_liquidacion.getValor("fecha_finiquito_asvac"),bandSalidaAjuste,valorTemp,valorDiasPendientesLaborados);
			
		}		
		else {
			return "0.0";
		}
			
		
		

		
		
		} catch (Exception e) {
			System.out.println("error verVacaciones()");					}
	}else {
		valor="0";
	}
	return utilitario.getFormatoNumero(valor);

}


public int getValorDiasExceso(int ide_geedp){
TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+ide_geedp);
String fecha_iniTemp="";
String valor="";
//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");
String a="";


TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
		+ "order by ide_geedp asc");
String ide_geedp_empleado="";
//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
//variable guarda la fecha de salida
String fecha_finTemp="";
fecha_finTemp=tab_emp.getValor("fecha_finctr_geedp");
//variable cuenta el numero de acciones
int contAccionInvalida=0,contForAccionInvalida=0;
StringBuilder str_ide_geedp = new StringBuilder();

for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
	if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(""+ide_geedp)==true) {
		String valorUltimoElemento="";
		valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
		if(valorUltimoElemento.equals(",")==true){
			str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
		}else {
		}

	i=tabEmpleados.getTotalFilas();
	}else{
			//Voy anidando los ides de la accion
			str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
           // valor++;
            if (tabEmpleados.getTotalFilas()==1) {
			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
				contForAccionInvalida++;
					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
					str_ide_geedp.append(",");
               // System.out.println("str_ide:  "+str_ide_empleado);
					}
			}	 	
	}
}


//Si no contiene acciones de personal luego de la liquidacion
if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
	str_ide_geedp.append(ide_geedp);
}


//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");

a=utilitario.getVariable("p_nrh_acciones_personal");
String[] listaAccionesEmpleado;
listaAccionesEmpleado=a.split(",");
for (int i = 0; i < listaAccionesEmpleado.length; i++) {
	fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
	if (!fecha_iniTemp.equals("")) {
		i=listaAccionesEmpleado.length;
	}
}



System.out.println("fecha_finTemp"+fecha_finTemp);
String fecha_fin_validacion="";
if (utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp))>=31) {
	fecha_fin_validacion=utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-30";
}else {
	fecha_fin_validacion=utilitario.getAnio(fecha_finTemp)+"-"+utilitario.getMes(fecha_finTemp)+"-"+utilitario.getDia(fecha_finTemp);
}
	
System.out.println("fecha_finTemp"+fecha_fin_validacion);


TablaGenerica tabAnio=utilitario.consultar("select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_finTemp)+"%' ");


TablaGenerica tab_liquidacion = utilitario.consultar("SELECT ide_gepro, ide_nrtit, ide_gemes, ide_geani, fecha_inicial_gepro,  "
		+ "fecha_final_gepro, detalle_periodo_gepro,  "
		+ "activo_gepro,gen_ide_gepro  "
		+ "FROM gen_perido_rol "
		+ "where ide_geani="+tabAnio.getValor("IDE_GEANI")+" and ide_gemes="+utilitario.getMes(fecha_finTemp)
		+ " order by ide_gepro desc ");


TablaGenerica tab_periodo_rol = utilitario.consultar("select * from nrh_rol where ide_gepro="+tab_liquidacion.getValor("IDE_GEPRO")+" "
		+ "and ide_nrdtn IN(2,4) AND ide_nresr=1 "
		+ " order by ide_nrrol asc");
if (tab_periodo_rol.getTotalFilas()==0) {
	valor=""+0;
}else {
	System.out.println("INGRESO A EXCESO");
int cont=0;
StringBuilder str = new StringBuilder();
for (int i = 0; i < tab_periodo_rol.getTotalFilas(); i++) {
  
  		//Voy anidando los ides de la accion
          	    str.append(tab_periodo_rol.getValor(i, "IDE_NRROL"));
               // valor++;
                if (tab_periodo_rol.getTotalFilas()==1) {
   			}else if (cont<=tab_periodo_rol.getTotalFilas()) {
   					cont++;
   					if(cont<(tab_periodo_rol.getTotalFilas())){
                    str.append(",");
                   // System.out.println("str_ide:  "+str_ide_empleado);
   					}
    			}
   			 		         		
}


TablaGenerica tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
		+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
		+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
		+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
		+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
		+ "where DRO.IDE_GEEDP in ( "
		+ "select IDE_GEEDP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP in (  "
		+ "select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp+") and ide_gttem !=3  "
		+ ")  "
		+ "and rol.ide_nrrol IN ("+str.toString()+")");  

/*TablaGenerica tab_detalle_rol = utilitario.consultar("select * from nrh_detalle_rol where ide_nrrol in("+str.toString()+") "
		+ "and ide_geedp in("+ide_geedp_empleado+")"); */ 
double valorTemp=0;

if (tab_detalle_rol.getTotalFilas()==0) {
	valor=""+0;
}else {
	valorTemp=(utilitario.getDiferenciaFechas360(utilitario.getFecha(fecha_finTemp), utilitario.getFecha(fecha_fin_validacion))-1);
	 if (valorTemp<=0.0 || valorTemp<=0) {
		valor=""+0;
	}else {
		int x = (int) valorTemp;
		//String valorNuevo=""+valorTemp;
		valor=""+x;
	}
		
		

}

/*   valorTemp=utilitario.getDiferenciaFechas360(utilitario.getFecha(fecha_finTemp), utilitario.getFecha(fecha_fin_validacion))-1;
if (valorTemp<=0.0) {
	valor=""+0.0;
}else {
}
*/

}

System.out.println("dias exceso"+valor);
return Integer.parseInt(valor); 	
		
}



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public String getDecimales(double valor){
	String string ="";
	string= String.valueOf(valor);
	String[] parts = string.split(Pattern.quote("."));
    String part1 = parts[0]; // 004
	String part2 = parts[1]; // 034556
	int decimales= 0;
	boolean bandera=false;
	//String[] parts = string.split(Pattern.quote("."));
	//String part1 = parts[0]; // 123
	//String part2 = parts[1]; // 654321
	
	if (parts[1].length()>1) {
		String sCadena = part2;
		String sSubCadena = sCadena.substring(2,3);
		if (Integer.parseInt(sSubCadena)>=5) {
			String sSubCadenaTemp = sCadena.substring(0,2);
			if (sCadena.substring(0,1).equals("0")) {
				decimales=(Integer.parseInt(sSubCadenaTemp))+1;
				bandera=true;
			}else {
				decimales=(Integer.parseInt(sSubCadenaTemp))+1;	
				bandera=false;
			}
			
		}else {
		String sSubCadenaTemp = sCadena.substring(0,2);
		decimales=Integer.parseInt(sSubCadenaTemp);
		bandera=false;

		}
		}else {
			decimales=Integer.parseInt(part2);
			bandera=false;

	}
	if (bandera==true) {
		return "0"+decimales;
	}else {
		return ""+decimales;
	}
		
	
}




public boolean retornaAccionpersonalComisionServicios(String ide_geedp){
	TablaGenerica tab_empleado_departamento_par=null;
	String fecha="",a="",ide_geedp_ingreso="";
	boolean ide_geame=false;
	int contAccionInvalida=0,contForAccionInvalida=0;
	StringBuilder str_ide_geedp = new StringBuilder();

	TablaGenerica tab_emp=utilitario.consultar("select ide_gtemp,ide_geedp,ide_gttem,fecha_finctr_geedp  from gen_empleados_departamento_par where ide_geedp="+ide_geedp);
	TablaGenerica tabEmpleados=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_emp.getValor("ide_gtemp")+") "
		+ "order by ide_geedp asc");
	String ide_geedp_empleado="";
 

	for (int i = 0; i < tabEmpleados.getTotalFilas(); i++) {
		if (tabEmpleados.getValor(i,"IDE_GEEDP").equals(ide_geedp)==true) {
			String valorUltimoElemento="";
			valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
		if(valorUltimoElemento.equals(",")==true){
			str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
		}else {
		}

	i=tabEmpleados.getTotalFilas();
	}else{
			//Voy anidando los ides de la accion
			str_ide_geedp.append(tabEmpleados.getValor(i, "IDE_GEEDP"));
           // valor++;
            if (tabEmpleados.getTotalFilas()==1) {
			}else if (contForAccionInvalida<=tabEmpleados.getTotalFilas()) {
				contForAccionInvalida++;
					if(contForAccionInvalida<(tabEmpleados.getTotalFilas())){
					str_ide_geedp.append(",");
               // System.out.println("str_ide:  "+str_ide_empleado);
					}
			}	 	
	}
}

 
//Si no contiene acciones de personal luego de la liquidacion
if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
	str_ide_geedp.append(ide_geedp);
}


String fecha_iniTemp="";
a=utilitario.getVariable("p_nrh_acciones_personal");
String[] listaAccionesEmpleado;
listaAccionesEmpleado=a.split(",");
for (int i = 0; i < listaAccionesEmpleado.length; i++) {
	fecha_iniTemp=retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
	if (!fecha_iniTemp.equals("")) {
		if (listaAccionesEmpleado[i].toString().equals("7")) {
			ide_geame=true;
			i=listaAccionesEmpleado.length;
		}else {
			ide_geame=false;
		}
		
	}
}



return ide_geame;
	

}


public String getRolesDecimoCuarto(String IDE_GEEDP,String ide_nrrol){
	TablaGenerica tab_periodo_rol=utilitario.consultar("select ide_nrrol,ide_gepro from nrh_rol where ide_nrrol="+ide_nrrol);
	String ide_gepro_temp=tab_periodo_rol.getValor("ide_gepro");
	TablaGenerica tabPeriodoRol=utilitario.consultar("select ide_gemes ,ide_geani  from gen_perido_rol where ide_gepro="+ide_gepro_temp);
	int mes_rol=Integer.parseInt(tabPeriodoRol.getValor("IDE_GEMES"));
	
	String ide_gepro="",str_ide_gepro="";
	double dou_sumatoria=0.0;

		String str_sql="",fecha_ini="",fecha_fin="";

		TablaGenerica tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
				+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
				+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
				+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
				+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
				+ "where DRO.IDE_GEEDP in ("+IDE_GEEDP+")  "
				+ "and RUB.ide_nrrub in(394,389) "
				+ " ORDER BY RUB.IDE_NRRUB DESC");
		
			String ajuste_dias="",dias_exceso="";
		int  banderaDecimo=0;
			ajuste_dias=tab_detalle_rol.getValor(0,"VALOR_NRDRO");
			dias_exceso=tab_detalle_rol.getValor(1,"VALOR_NRDRO");			
		
			if (ajuste_dias.equals("1") || (ajuste_dias.equals("1.00"))){
				if (dias_exceso.equals("0") || (dias_exceso.equals("0.00"))){
					//ya ejecuto rol
					banderaDecimo=1;	
				}else {
					//excedio dias
					banderaDecimo=2;
				}
			}else {
				//debe aparecer el valor
				banderaDecimo=3;		
			}
		
			
		
		
		TablaGenerica tab_empleado=utilitario.consultar("select ide_geedp,ide_gtemp,fecha_finctr_geedp  "
				+ "from gen_empleados_departamento_par  "
				+ "where ide_geedp="+IDE_GEEDP+"");
		
		String fecha_iniTemp="";
		String a="";
		
		
		
		TablaGenerica tabEmpleados1=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_empleado.getValor("ide_gtemp")+") "
				+ "order by ide_geedp asc");
		String ide_geedp_empleado="";
		//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
		//variable guarda la fecha de salida
		String fecha_finTemp="";
		fecha_finTemp=tab_empleado.getValor("fecha_finctr_geedp");
		//variable cuenta el numero de acciones
		int contAccionInvalida=0,contForAccionInvalida=0;
		StringBuilder str_ide_geedp = new StringBuilder();
		for (int i = 0; i < tabEmpleados1.getTotalFilas(); i++) {
			if (tabEmpleados1.getValor(i,"IDE_GEEDP").equals(IDE_GEEDP)==true) {
				String valorUltimoElemento="";
				valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
				if(valorUltimoElemento.equals(",")==true){
					str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
				}else {
				}

			i=tabEmpleados1.getTotalFilas();
			}else{
					//Voy anidando los ides de la accion
					str_ide_geedp.append(tabEmpleados1.getValor(i, "IDE_GEEDP"));
		           // valor++;
		            if (tabEmpleados1.getTotalFilas()==1) {
					}else if (contForAccionInvalida<=tabEmpleados1.getTotalFilas()) {
						contForAccionInvalida++;
							if(contForAccionInvalida<(tabEmpleados1.getTotalFilas())){
							str_ide_geedp.append(",");
		               // System.out.println("str_ide:  "+str_ide_empleado);
							}
					}	 	
			}
		}


		//Si no contiene acciones de personal luego de la liquidacion
		if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
			str_ide_geedp.append(IDE_GEEDP);
		}


		//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");

		a=utilitario.getVariable("p_nrh_acciones_personal");
		String[] listaAccionesEmpleado;
		listaAccionesEmpleado=a.split(",");
		for (int i = 0; i < listaAccionesEmpleado.length; i++) {
			fecha_iniTemp=retornaAccionpersonal(tab_empleado.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
			if (!fecha_iniTemp.equals("")) {
				i=listaAccionesEmpleado.length;
			}
		}

		
		
		
		TablaGenerica tab_acumula_decimo=utilitario.consultar("select ide_gtemp,acumula_decimo_gtemp from gth_empleado "
				+ "where ide_gtemp="+tab_empleado.getValor("IDE_GTEMP")
				+ " limit 1");
		boolean acumula_decimo=false;
		acumula_decimo=Boolean.parseBoolean(tab_acumula_decimo.getValor("ACUMULA_DECIMO_GTEMP"));
		String fecha_comaracion=(utilitario.getAnio(fecha_finTemp)-1)+"-01-01";
		int anio=0,mesTemp1=0,mesEntero=0,anioTemp;
		anio=utilitario.getAnio(fecha_finTemp)-1;
		mesTemp1=utilitario.getMes(fecha_finTemp);
		if (mesTemp1==1) {
			mesEntero=12;
			anioTemp=utilitario.getAnio(fecha_finTemp)-1;
		}else {
			mesEntero=mesTemp1;
			anioTemp=utilitario.getAnio(fecha_finTemp);
		}
		
		
		String fecha_calculoFinInicio=anioTemp+"-"+(mesEntero)+"-01";
		
		TablaGenerica  tabEmpleados=utilitario.consultar("select ide_geedp,ide_gtemp from gen_empleados_departamento_par where ide_gtemp="+tab_empleado.getValor("IDE_GTEMP")+" and "
				+ "ACTIVO_GEEDP=TRUE  ORDER BY IDE_GEEDP DESC "
				+ "LIMIT 1");
		
		
		String fecha_comparacionCuartoRango=(utilitario.getAnio(fecha_finTemp))+"-08-01";
		String fecha_comparacionCuarto=(utilitario.getAnio(fecha_finTemp)-1)+"-08-01";

		if (fecha_iniTemp.compareTo(fecha_comparacionCuarto)<=0) {
			fecha_ini=fecha_comparacionCuartoRango;
			}else if(fecha_iniTemp.compareTo(fecha_comparacionCuarto)>0) {
				fecha_ini=fecha_iniTemp;
			}
		
		if (acumula_decimo==false) {
			//ejecuto rol
			if (banderaDecimo==3) {
				if (fecha_iniTemp.compareTo(fecha_comparacionCuarto)<=0) {
					fecha_ini=fecha_comparacionCuarto;
					}else if(fecha_iniTemp.compareTo(fecha_comparacionCuarto)>0) {
						fecha_ini=fecha_iniTemp;
					}
				
				if (fecha_finTemp.compareTo(fecha_comparacionCuartoRango)<=0) {
				fecha_fin=fecha_finTemp;
				}else {
					fecha_fin=fecha_finTemp;
					fecha_ini=fecha_comparacionCuartoRango;
				}
				
				
				if (mes_rol==utilitario.getMes(fecha_finTemp)) {
					int anioTercero=0,mesTempTercero=0,mesEnteroTercero=0,anioTempTercero;
					anioTercero=utilitario.getAnio(fecha_finTemp);
					mesTempTercero=utilitario.getMes(fecha_finTemp);
					if (mesTempTercero==1) {
						mesEnteroTercero=12;
						anioTempTercero=utilitario.getAnio(fecha_finTemp);
					}else {
						mesEnteroTercero=mesTempTercero-1;
						anioTempTercero=utilitario.getAnio(fecha_finTemp);
					}
					fecha_fin=anioTemp+"-"+mesEnteroTercero+"-01";
					ide_gepro=getPeriodosRolLiquidacion(fecha_ini, fecha_fin,tabEmpleados.getValor("ide_geedp"))+","+ide_gepro_temp;

					System.out.println(ide_gepro);					
					
					
					TablaGenerica tab_acciones =utilitario.consultar("select "
							+ "pro.ide_gepro,drol.ide_nrdro,drol.valor_nrdro,pro.ide_gemes,pro.ide_geani,DROL.IDE_NRDER  from nrh_rol rol "
							+ "left join nrh_detalle_rol drol on drol.ide_nrrol=rol.ide_nrrol "
							+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO "
							+ "where drol.ide_nrder in(select ide_nrder from nrh_detalle_rubro where ide_nrrub in(121) "
							+ "and pro.ide_gepro in ("+ide_gepro+") and drol.ide_geedp in("+str_ide_geedp.toString()+") and drol.valor_nrdro>0.0) "
									//+ "and drol.ide_nrdtn in(2,4)) "
							+ "order by pro.ide_geani asc ,pro.ide_gemes asc");
					
					if (tab_acciones.getTotalFilas()>0) {
						for (int i = 0; i < tab_acciones.getTotalFilas(); i++) {
						if ((tab_acciones.getTotalFilas()-1)==i) {
								
							str_ide_gepro+=tab_acciones.getValor(i, "IDE_GEPRO");
								}else {
									str_ide_gepro+=tab_acciones.getValor(i, "IDE_GEPRO")+",";	
								}
								
							
						}
					}
					
					if (str_ide_gepro==null || str_ide_gepro.equals("") || str_ide_gepro.equals("")) {
						ide_gepro=ide_gepro_temp;	
					}else{
					ide_gepro=ide_gepro_temp+","+str_ide_gepro;
					}
				}else {
					ide_gepro=getPeriodosRolLiquidacion(fecha_ini, fecha_finTemp,tabEmpleados.getValor("ide_geedp"))+","+ide_gepro_temp;
					}
			}
			
			
			if (banderaDecimo==1){
			ide_gepro="-1";
			//excedio 
			}else if (banderaDecimo==2){
				ide_gepro="-1";
			}else{
				//aparece en el rol
			//ide_gepro=ide_gepro_temp;
			}
		}else{
			
			if (banderaDecimo==1){
				//fecha_ini=fecha_comaracion;
				//fecha_fin=fecha_finTemp;	
				
				if (fecha_iniTemp.compareTo(fecha_comparacionCuarto)<=0) {
					fecha_ini=fecha_comparacionCuarto;
					}else if(fecha_iniTemp.compareTo(fecha_comparacionCuarto)>0) {
						fecha_ini=fecha_iniTemp;
					}
				
				if (fecha_finTemp.compareTo(fecha_comparacionCuartoRango)<=0) {
				fecha_fin=fecha_finTemp;
				}else {
					fecha_fin=fecha_finTemp;
					fecha_ini=fecha_comparacionCuartoRango;
				}
				
				
				
				if (mes_rol==utilitario.getMes(fecha_finTemp)) {
					int anioTercero=0,mesTempTercero=0,mesEnteroTercero=0,anioTempTercero;
					anioTercero=utilitario.getAnio(fecha_finTemp);
					mesTempTercero=utilitario.getMes(fecha_finTemp);
					if (mesTempTercero==1) {
						mesEnteroTercero=12;
						anioTempTercero=utilitario.getAnio(fecha_finTemp);
					}else {
						mesEnteroTercero=mesTempTercero-1;
						anioTempTercero=utilitario.getAnio(fecha_finTemp);
					}
					fecha_fin=anioTemp+"-"+mesEnteroTercero+"-01";
					ide_gepro=getPeriodosRolLiquidacion(fecha_ini, fecha_fin,tabEmpleados.getValor("ide_geedp"));

				}else {
					ide_gepro=getPeriodosRolLiquidacion(fecha_ini, fecha_finTemp,tabEmpleados.getValor("ide_geedp"));

				}
				
		
		
				}else if (banderaDecimo==2){
					ide_gepro="-1";
				}else{
					
					
					if (fecha_iniTemp.compareTo(fecha_comparacionCuarto)<=0) {
						fecha_ini=fecha_comparacionCuarto;
						}else if(fecha_iniTemp.compareTo(fecha_comparacionCuarto)>0) {
							fecha_ini=fecha_iniTemp;
						}
					
					if (fecha_finTemp.compareTo(fecha_comparacionCuartoRango)<=0) {
					fecha_fin=fecha_finTemp;
					}else {
						fecha_fin=fecha_finTemp;
						fecha_ini=fecha_comparacionCuartoRango;
					}
					
					
					if (mes_rol==utilitario.getMes(fecha_finTemp)) {
						int anioTercero=0,mesTempTercero=0,mesEnteroTercero=0,anioTempTercero;
						anioTercero=utilitario.getAnio(fecha_finTemp);
						mesTempTercero=utilitario.getMes(fecha_finTemp);
						if (mesTempTercero==1) {
							mesEnteroTercero=12;
							anioTempTercero=utilitario.getAnio(fecha_finTemp);
						}else {
							mesEnteroTercero=mesTempTercero-1;
							anioTempTercero=utilitario.getAnio(fecha_finTemp);
						}
						fecha_fin=anioTemp+"-"+mesEnteroTercero+"-01";
						ide_gepro=getPeriodosRolLiquidacion(fecha_ini, fecha_fin,tabEmpleados.getValor("ide_geedp"))+","+ide_gepro_temp;

					}else {
						ide_gepro=getPeriodosRolLiquidacion(fecha_ini, fecha_finTemp,tabEmpleados.getValor("ide_geedp"))+","+ide_gepro_temp;
						}

				}
			
		}
		
		

		
		return ide_gepro;
}



public String getRolesDecimoTercer(String IDE_GEEDP,String ide_nrrol){
	TablaGenerica tab_periodo_rol=utilitario.consultar("select ide_nrrol,ide_gepro from nrh_rol where ide_nrrol="+ide_nrrol);
	String ide_gepro_temp=tab_periodo_rol.getValor("ide_gepro");
	String ide_gepro="";
	TablaGenerica tab_detalle_rol = utilitario.consultar("select * from NRH_DETALLE_ROL DRO  "
			+ "inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL   "
			+ "INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO   "
			+ "inner join NRH_DETALLE_RUBRO DER on DER.IDE_NRDER=DRO.IDE_NRDER   "
			+ "INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB   "
			+ "where DRO.IDE_GEEDP in ("+IDE_GEEDP+")  "
			+ "and RUB.ide_nrrub in(394,389) "
			+ " ORDER BY RUB.IDE_NRRUB DESC");
	
	String ajuste_dias="",dias_exceso="";
	int banderaDecimo=0;
		ajuste_dias=tab_detalle_rol.getValor(0,"VALOR_NRDRO");
		//ajuste_dias=ajuste_dias_temp;
		dias_exceso=tab_detalle_rol.getValor(1,"VALOR_NRDRO");			
	
		if (ajuste_dias.equals("1") || (ajuste_dias.equals("1.00"))) {
			if (dias_exceso.equals("0") || (dias_exceso.equals("0.00"))) {
				//ya ejecuto rol
				banderaDecimo=1;	
			}else {
				//excedio dias
				banderaDecimo=2;
			}
		}else {
			//debe aparecer el valor
			banderaDecimo=3;		
		}
	
	
		String str_sql="",fecha_ini,fecha_fin;
		TablaGenerica tab_empleado=utilitario.consultar("select ide_geedp,ide_gtemp,fecha_finctr_geedp  "
				+ "from gen_empleados_departamento_par  "
				+ "where ide_geedp="+IDE_GEEDP+"");
				//retornaAccionpersonal(tab_empleado.getValor("IDE_GTEMP"), "23");
		TablaGenerica  tabEmpleados=utilitario.consultar("select ide_geedp,ide_gtemp from gen_empleados_departamento_par where ide_gtemp="+tab_empleado.getValor("IDE_GTEMP")+" and "
				+ "ACTIVO_GEEDP=TRUE  ORDER BY IDE_GEEDP DESC "
				+ "LIMIT 1");

		
		
		
		String fecha_iniTemp="";
		//if (fecha_iniTemp==null || fecha_iniTemp.equals("") || fecha_iniTemp.isEmpty()) {
			String a="";
			
			
			
			TablaGenerica tabEmpleados1=utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp in("+tab_empleado.getValor("ide_gtemp")+") "
					+ "order by ide_geedp asc");
			String ide_geedp_empleado="";
			//String fecha_finTemp=retornaAccionpersonalLiquidacion(tabEmpleados.getValor("IDE_GTEMP"));
			//variable guarda la fecha de salida
			String fecha_finTemp="";
			fecha_finTemp=tab_empleado.getValor("fecha_finctr_geedp");
			String fecha_comaracion=utilitario.getAnio(fecha_finTemp)+"-01-01";

			//variable cuenta el numero de acciones
			int contAccionInvalida=0,contForAccionInvalida=0;
			StringBuilder str_ide_geedp = new StringBuilder();

			for (int i = 0; i < tabEmpleados1.getTotalFilas(); i++) {
				if (tabEmpleados1.getValor(i,"IDE_GEEDP").equals(IDE_GEEDP)==true) {
					String valorUltimoElemento="";
					valorUltimoElemento=Character.toString(str_ide_geedp.charAt((str_ide_geedp.length() - 1)));
					if(valorUltimoElemento.equals(",")==true){
						str_ide_geedp.deleteCharAt((str_ide_geedp.length() - 1));
					}else {
					}

				i=tabEmpleados1.getTotalFilas();
				}else{
						//Voy anidando los ides de la accion
						str_ide_geedp.append(tabEmpleados1.getValor(i, "IDE_GEEDP"));
			           // valor++;
			            if (tabEmpleados1.getTotalFilas()==1) {
						}else if (contForAccionInvalida<=tabEmpleados1.getTotalFilas()) {
							contForAccionInvalida++;
								if(contForAccionInvalida<(tabEmpleados1.getTotalFilas())){
								str_ide_geedp.append(",");
			               // System.out.println("str_ide:  "+str_ide_empleado);
								}
						}	 	
				}
			}


			//Si no contiene acciones de personal luego de la liquidacion
			if (str_ide_geedp.length()==0 || str_ide_geedp==null || str_ide_geedp.equals("")) {
				str_ide_geedp.append(IDE_GEEDP);
			}
			//retornaAccionpersonal(tab_emp.getValor("IDE_GTEMP"), "23");

			a=utilitario.getVariable("p_nrh_acciones_personal");
			String[] listaAccionesEmpleado;
			listaAccionesEmpleado=a.split(",");
			for (int i = 0; i < listaAccionesEmpleado.length; i++) {
				fecha_iniTemp=retornaAccionpersonal(tab_empleado.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
				if (!fecha_iniTemp.equals("")) {
					i=listaAccionesEmpleado.length;
				}
			}

			
			//Accion de personal de ingreso
			String ide_geedp_ingreso="";
			for (int i = 0; i < listaAccionesEmpleado.length; i++) {
				
				//Obtengo el ide_geedp de ingreso del funcionario
				ide_geedp_ingreso=retornaAccionpersonalContratacion(tab_empleado.getValor("IDE_GTEMP"), listaAccionesEmpleado[i].toString(),str_ide_geedp.toString());
				if (!ide_geedp_ingreso.equals("")) {
					//Si contiene accion de entrada sale del bucle for
					i=listaAccionesEmpleado.length;
				}
			}
			
	
			int validaIngreso=0;
			StringBuilder str_ide_geedp_decimot = new StringBuilder();
			boolean band=false;			
			for (int j = 0; j < tabEmpleados1.getTotalFilas(); j++) {
						
					if (tabEmpleados1.getValor(j, "IDE_GEEDP").compareTo(ide_geedp_ingreso)==0) {
							validaIngreso=1;
							}
						
					if (tabEmpleados1.getValor(j, "IDE_GEEDP").compareTo(IDE_GEEDP)==0) {
								String valorUltimoElemento="";
								valorUltimoElemento=Character.toString(str_ide_geedp_decimot.charAt((str_ide_geedp_decimot.length() - 1)));
								if(valorUltimoElemento.equals(",")==true){
									str_ide_geedp_decimot.deleteCharAt((str_ide_geedp_decimot.length() - 1));
									j=tabEmpleados1.getTotalFilas();
									validaIngreso=2;
									}else {}
						}	
								
					   
					if (validaIngreso==1) {
								str_ide_geedp_decimot.append(tabEmpleados1.getValor(j, "IDE_GEEDP"));
								str_ide_geedp_decimot.append(",");
							}else {
						}
						
					}
			
			
					
			TablaGenerica tab_acumula_decimo=utilitario.consultar("select ide_gtemp,acumula_decimo_gtemp from gth_empleado "
					+ "where ide_gtemp="+tab_empleado.getValor("IDE_GTEMP")
					+ " limit 1");
			boolean acumula_decimo=false;
			acumula_decimo=Boolean.parseBoolean(tab_acumula_decimo.getValor("ACUMULA_DECIMO_GTEMP"));
			
			
			
			
		int anio=0,mesTemp=0,mesEntero=0,anioTemp=0;
		anio=utilitario.getAnio(fecha_finTemp)-1;
			
		mesTemp=utilitario.getMes(fecha_finTemp);
		if (mesTemp==1) {
			mesEntero=12;
			anioTemp=utilitario.getAnio(fecha_finTemp)-1;
		}else {
			mesEntero=mesTemp;
			anioTemp=utilitario.getAnio(fecha_finTemp);
		}
		String fecha=anioTemp+"-"+(mesEntero)+"-"+utilitario.getDia(fecha_finTemp);
		
	
		if (acumula_decimo==false) {
			//ejecuto rol
			if (banderaDecimo==1){
			ide_gepro="-1";
			//excedio 
			}else if (banderaDecimo==2){
				ide_gepro="-1";
			}else{
				//aparece en el rol
			ide_gepro=ide_gepro_temp;
			}
		}else{
			
			if (banderaDecimo==1){
				if (fecha_iniTemp.compareTo(fecha_comaracion)<=0) {
					fecha_ini=fecha_comaracion;
					fecha_fin=utilitario.getAnio(fecha_ini)+"-"+(utilitario.getMes(fecha_finTemp))+"-"+utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp));

			}else {
					fecha_ini=fecha_iniTemp;
					//fecha_ini=fecha_finTemp;
					fecha_fin=utilitario.getAnio(fecha_finTemp)+"-"+(utilitario.getMes(fecha_finTemp))+"-"+utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp));
				}		
				
				if (ide_gepro_temp==null || ide_gepro_temp.equals("") || ide_gepro_temp.isEmpty()){
					ide_gepro="-1";
				}else {
					ide_gepro=getPeriodosRolLiquidacion(fecha_ini, fecha_fin,tabEmpleados.getValor("ide_geedp"));
									
				}

				
				}else if (banderaDecimo==2){
					ide_gepro="-1";
				}else{
					//aparece en el rol
						if (fecha_iniTemp.compareTo(fecha_comaracion)<=0) {
							fecha_ini=fecha_comaracion;
							fecha_fin=utilitario.getAnio(fecha_ini)+"-"+(utilitario.getMes(fecha_finTemp))+"-"+utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp));

					}else {
							fecha_ini=fecha_iniTemp;
							//fecha_ini=fecha_finTemp;
							fecha_fin=utilitario.getAnio(fecha_finTemp)+"-"+(utilitario.getMes(fecha_finTemp))+"-"+utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha_finTemp));
						}	
					//fecha_ini=fecha_comaracion;
					//fecha_fin=fecha_finTemp;	
					ide_gepro=getPeriodosRolLiquidacion(fecha_ini, fecha_fin,tabEmpleados.getValor("ide_geedp"));
					if (ide_gepro==null || ide_gepro.equals("") || ide_gepro.isEmpty()) {
						if (ide_gepro_temp==null || ide_gepro_temp.equals("") || ide_gepro_temp.isEmpty()) {
							ide_gepro="-1";
						}else {
							ide_gepro=ide_gepro_temp;
						}

						
						
					}else{
						if (ide_gepro_temp==null || ide_gepro_temp.equals("") || ide_gepro_temp.isEmpty()) {
							ide_gepro=getPeriodosRolLiquidacion(fecha_ini, fecha_fin,tabEmpleados.getValor("ide_geedp"));
						}else{
							ide_gepro=getPeriodosRolLiquidacion(fecha_ini, fecha_fin,tabEmpleados.getValor("ide_geedp"))+","+ide_gepro_temp;
						}
					}
				}
			
		}
		

		return ide_gepro;
	}



public String getPeriodosRolLiquidacion(String fecha_inicial,String fecha_final,String ide_geedp){
	int ide_gepro=0;
	boolean band=false;
	int mes_ini=0;
	int mes_fin=0;
	int anio_ini=0;
	int anio_fin=0;
	try {
		mes_ini=utilitario.getMes(fecha_inicial);
		mes_fin=utilitario.getMes(fecha_final);
		anio_ini=utilitario.getAnio(fecha_inicial);
		anio_fin=utilitario.getAnio(fecha_final);
	} catch (Exception e) {
		// TODO: handle exception
	}
	
	
	/*TablaGenerica tabDetalle_rol= utilitario.consultar("select grol.ide_gepro,rol.ide_nrrol from GEN_PERIDO_ROL   grol "
			+ "left join nrh_rol rol on rol.ide_gepro=grol.ide_gepro "
			+ "left join nrh_detalle_rol drol on drol.ide_nrrol=rol.ide_nrrol "
			+ "where drol.ide_geedp  in("+ide_geedp+") limit 1");
	
	if (tabDetalle_rol.getTotalFilas()>0) {
		ide_gepro=Integer.parseInt(tabDetalle_rol.getValor("IDE_GEPRO"));
		band=true;
	}*/
	
	int ide_gemes_fin_aux=mes_fin;
	int ide_gemes_ini_aux=1;
	int ide_geani_aux=anio_fin;
	if (validarFechas(fecha_inicial, fecha_final)){
		int diferencia_anios=anio_fin-anio_ini;
		TablaGenerica tab_gen_per=new TablaGenerica();
		String str_ide_gepro="";
		if (diferencia_anios==0){
			str_ide_gepro="";
			tab_gen_per=utilitario.consultar("select * from GEN_PERIDO_ROL " +
					"where ((IDE_GEMES BETWEEN "+mes_ini+" and "+mes_fin+") " +
					"and IDE_GEANI=(SELECT IDE_GEANI FROM GEN_ANIO WHERE lower (DETALLE_GEANI) like '"+anio_ini+"'))");
			tab_gen_per.imprimirSql();
			for (int j = 0; j < tab_gen_per.getTotalFilas(); j++) {
				if (band==true) {
					if (Integer.parseInt(tab_gen_per.getValor(j, "IDE_GEPRO"))==ide_gepro) {
					}else {
						str_ide_gepro+=tab_gen_per.getValor(j, "IDE_GEPRO")+",";	
					}
				}else {
					str_ide_gepro+=tab_gen_per.getValor(j, "IDE_GEPRO")+",";	
				}
				
			}
		}else{
			str_ide_gepro="";
			for (int i = 0; i <= diferencia_anios; i++) {
				if (i==0){
					ide_gemes_ini_aux=mes_ini;
				}
				if (diferencia_anios==i){
					ide_gemes_fin_aux=mes_fin;
					ide_geani_aux=anio_fin;
					ide_gemes_ini_aux=1;
				}else{
					if (i==0){
						ide_geani_aux=ide_geani_aux-diferencia_anios;
					}else if (i>0){
						ide_gemes_ini_aux=1;
						ide_geani_aux+=1;
					}
					ide_gemes_fin_aux=12;
				}

				tab_gen_per=utilitario.consultar("select * from GEN_PERIDO_ROL " +
						"where ((IDE_GEMES BETWEEN "+ide_gemes_ini_aux+" and "+ide_gemes_fin_aux+") " +
						"and IDE_GEANI=(SELECT IDE_GEANI FROM GEN_ANIO WHERE lower (DETALLE_GEANI) like '"+ide_geani_aux+"'))");
				tab_gen_per.imprimirSql();
				for (int j = 0; j < tab_gen_per.getTotalFilas(); j++) {
					if (band==true) {
						if (Integer.parseInt(tab_gen_per.getValor(j, "IDE_GEPRO"))==ide_gepro) {
						}else {
							str_ide_gepro+=tab_gen_per.getValor(j, "IDE_GEPRO")+",";	
						}
					}else {
						str_ide_gepro+=tab_gen_per.getValor(j, "IDE_GEPRO")+",";	
					}
					
				}
			}
		}
		
		
		if (!str_ide_gepro.isEmpty()){
			str_ide_gepro=str_ide_gepro.substring(0, str_ide_gepro.length()-1);
		}
//		System.out.println("fecha ini "+fecha_inicial+" fecha fin "+fecha_final+" ide_gepro resultado "+str_ide_gepro);
		return str_ide_gepro;
	}

	return null;
}

		public int getDias360(String periodoNuevoEntrada, String periodoNuevoSalida){

			int mesIniLaborados,mesFinLaborados,diaInicioLaborados,diaFinLaborados,anioInicioLaborados,anioFinLaborados;
			diaFinLaborados=utilitario.getDia(periodoNuevoSalida);
			diaInicioLaborados=utilitario.getDia(periodoNuevoEntrada);
			mesFinLaborados=utilitario.getMes(periodoNuevoSalida);
			mesIniLaborados=utilitario.getMes(periodoNuevoEntrada);
			anioFinLaborados=utilitario.getAnio(periodoNuevoSalida);
			anioInicioLaborados=utilitario.getAnio(periodoNuevoEntrada);

			int valorAnio,valorMes,valorDia;
			valorAnio=anioFinLaborados-anioInicioLaborados;
			valorMes=mesFinLaborados-mesIniLaborados;
			valorDia=diaFinLaborados-diaInicioLaborados;
			int formula=((valorAnio*360)+((valorMes)*30)+(valorDia))+1;
			return formula;
}

		
		
		
	
		
		
		
//////////////////////////////////////////////////PARA CUANDO SE TIENE DOS LIQUIDACIONES EL MISMO ANIO
		
		
		
		

public String retornaAccionpersonalLiquidacion(String IDE_GTEMP, String IDE_GEMED,String IDE_GEEDP){
TablaGenerica tab_empleado_departamento_par=null;
String fecha="",ide_geedpLiquidacion="";

if (IDE_GEEDP==null || IDE_GEEDP.equals("") || IDE_GEEDP.isEmpty()) {
	


tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
		+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
		+ "from gen_empleados_departamento_par epar "
		+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
		+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
		+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
		+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
		+ "where epar.ide_gtemp="+IDE_GTEMP+" and gmed.ide_gemed in("+IDE_GEMED+") and gaed.ide_geaed !=(3) "
		+ "and epar.activo_geedp=false "
		+ "order by ide_geedp desc");

/*if (tab_empleado_departamento_par.getTotalFilas()==0) {
	return fecha;
}else{
if (IDE_GEMED.equals("23")) {
		fecha=tab_empleado_departamento_par.getValor("FECHA_GEEDP");

}else if (IDE_GEMED.equals("19")) {
	fecha=tab_empleado_departamento_par.getValor("FECHA_LIQUIDACION_GEEDP");
	}*/
fecha=tab_empleado_departamento_par.getValor("FECHA_GEEDP");
if (fecha==null || fecha.equals("") || fecha.isEmpty()) {
	fecha="";
}
}else{
	
	tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
			+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
			+ "from gen_empleados_departamento_par epar "
			+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
			+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
			+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
			+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
			+ "where epar.ide_geedp IN("+IDE_GEEDP+") and gmed.ide_gemed in("+IDE_GEMED+") and gaed.ide_geaed !=(3) "
			+ "and epar.activo_geedp=false "
			+ "order by ide_geedp desc "
			+ "limit 1");	

	if (tab_empleado_departamento_par.getValor("ide_geedp")==null || tab_empleado_departamento_par.getValor("ide_geedp").equals("") || tab_empleado_departamento_par.getValor("ide_geedp").isEmpty()) {
		ide_geedpLiquidacion="-1";
	}else {
		ide_geedpLiquidacion=tab_empleado_departamento_par.getValor("ide_geedp");
	}
	
}
return ide_geedpLiquidacion;		
//}

}
		
		
		
		
		
public String retornaAccionpersonalContratacion(String IDE_GTEMP, String IDE_GEMED,String IDE_GEEDP){
TablaGenerica tab_empleado_departamento_par=null;
String ide_geedp="";

if (IDE_GEEDP==null || IDE_GEEDP.equals("") || IDE_GEEDP.isEmpty()) {
	


tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
		+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
		+ "from gen_empleados_departamento_par epar "
		+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
		+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
		+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
		+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
		+ "where epar.ide_gtemp="+IDE_GTEMP+" and gmed.ide_gemed in("+IDE_GEMED+") and gaed.ide_geaed !=(3) "
		+ "and epar.activo_geedp=false "
		+ "order by ide_geedp desc");

/*if (tab_empleado_departamento_par.getTotalFilas()==0) {
	return fecha;
}else{
if (IDE_GEMED.equals("23")) {
		fecha=tab_empleado_departamento_par.getValor("FECHA_GEEDP");

}else if (IDE_GEMED.equals("19")) {
	fecha=tab_empleado_departamento_par.getValor("FECHA_LIQUIDACION_GEEDP");
	}*/
ide_geedp=tab_empleado_departamento_par.getValor("IDE_GEEDP");
if (ide_geedp==null || ide_geedp.equals("") || ide_geedp.isEmpty()) {
	ide_geedp="";
}
}else{
	
	tab_empleado_departamento_par=utilitario.consultar("select epar.ide_geedp,epar.fecha_geedp,epar.rmu_geedp,fecha_liquidacion_geedp, "
			+ "gded.fecha_ingreso_geded,game.ide_geame,game.ide_gemed,gaed.ide_geaed,gaed.detalle_geaed,gmed.ide_gemed,gmed.detalle_gemed  "
			+ "from gen_empleados_departamento_par epar "
			+ "left join  gen_detalle_empleado_departame gded on gded.ide_geded=epar.ide_geded "
			+ "left join gen_accion_motivo_empleado  game on game.ide_geame=gded.ide_geame "
			+ "left join gen_accion_empleado_depa gaed on gaed.ide_geaed=game.ide_geaed "
			+ "left join gen_motivo_empleado_depa gmed on gmed.ide_gemed=game.ide_gemed "
			+ "where epar.ide_geedp IN("+IDE_GEEDP+") and gmed.ide_gemed in("+IDE_GEMED+") and gaed.ide_geaed !=(3) "
			+ "and epar.activo_geedp=false "
			+ "order by ide_geedp desc "
			+ "limit 1");	
 
	if (tab_empleado_departamento_par.getValor("ide_geedp")==null || tab_empleado_departamento_par.getValor("ide_geedp").equals("") || tab_empleado_departamento_par.getValor("ide_geedp").isEmpty()) {
		ide_geedp="";
	}else {
		ide_geedp=tab_empleado_departamento_par.getValor("ide_geedp");
	}

	
}
return ide_geedp;		
//}

}

		
	

public void reCalcularNominaLiquidacion(String ide_nrdtn,String ide_gepro1,String ide_nrtit,boolean decimos,String empleado,String rol){
	long inicio = System.currentTimeMillis();
	System.out.println("RECALCULAR "+ide_nrdtn);

	TablaGenerica tab_rol=getRol(ide_nrdtn,ide_gepro1);

	String str_ide_nrrol=rol;

	TablaGenerica tab_per_rol=getPeriodoRol(tab_rol.getValor("IDE_GEPRO"));

	String fecha_ini_gepro="";
	String fecha_fin_gepro="";


	// obtengo el Tipo de nomina de la tabla detalle de tipo de nomina (Normal,Liquidacion,Pago de Decimos)
	String IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");

	if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
		if (getDetalleTipoNomina(ide_nrdtn).getValor("IDE_GTTEM").equalsIgnoreCase(utilitario.getVariable("p_gth_tipo_empleado_codigo"))){
			fecha_fin_gepro=getPeriodoRol(ide_gepro1).getValor("FECHA_INICIAL_GEPRO");
			fecha_ini_gepro=utilitario.getAnio(fecha_fin_gepro)+"-"+utilitario.getMes(fecha_fin_gepro)+"-1";
		}else{
			String fecha_ini=getPeriodoRol(ide_gepro1).getValor("FECHA_INICIAL_GEPRO");
			fecha_fin_gepro=utilitario.getAnio(fecha_ini)+"-"+utilitario.getMes(fecha_ini)+"-30";
			fecha_ini_gepro=utilitario.getAnio(fecha_ini)+"-"+utilitario.getMes(fecha_ini)+"-1";
		}		
	}else{
		fecha_ini_gepro=tab_per_rol.getValor("FECHA_INICIAL_GEPRO");
		fecha_fin_gepro=tab_per_rol.getValor("FECHA_FINAL_GEPRO");
	}

	utilitario.getConexion().ejecutarSql("update  NRH_AMORTIZACION set ACTIVO_NRAMO=false " +
			"where FECHA_VENCIMIENTO_NRAMO " +
			"BETWEEN to_date ('"+fecha_ini_gepro+"','yyyy-mm-dd') and to_date ('"+fecha_fin_gepro+"','yyyy-mm-dd') " +
			"and IDE_NRANI in (select ide_nrani from NRH_ANTICIPO_INTERES " +
			"where IDE_NRANT in (select ide_nrant from NRH_ANTICIPO " +
			"where IDE_GTEMP in (select emp.ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
			"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM " +
			//				"and EDP.IDE_SUCU=DTN.IDE_SUCU " +
			"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP " +
			"WHERE DTN.IDE_NRDTN IN ("+ide_nrdtn+")) " +
			"AND ACTIVO_NRANT =true ))");


	// encera el rubro impuesto a la renta mensual de todos los empleados del rol
	utilitario.getConexion().ejecutarSql("update NRH_DETALLE_ROL set valor_nrdro=0 where IDE_NRROL="+str_ide_nrrol+" " +
			"and IDE_NRDER =  (select IDE_NRDER from NRH_DETALLE_RUBRO where IDE_NRDTN="+ide_nrdtn+" and IDE_NRRUB="+utilitario.getVariable("p_nrh_rubro_impuesto_renta_mensual")+")");



	TablaGenerica tab_empleados_departamento=utilitario.consultar(getSqlEmpleadosNominaLiquidacionEmpleado(str_ide_nrrol,empleado));


	// coje los detalles de rol de todos los empleados
	//TablaGenerica tab_drol=getDetalleRol(str_ide_nrrol);
	TablaGenerica tab_drol=getDetalleRol(str_ide_nrrol,tab_empleados_departamento.getValor("IDE_GEEDP"));// coje los detalles de rol

	if (decimos){
		// cojo los empleados solo de la region que corresponde el pago de decimo  

		String str_sql_emp_reg_pago="select * from ( ";
		str_sql_emp_reg_pago+=tab_empleados_departamento.getSql();
		str_sql_emp_reg_pago+=" )a ";
		str_sql_emp_reg_pago+="where a.ide_gereg in (select c.ide_gereg from ("+tab_drol.getSql()+")c group by c.ide_gereg) " +
				"order by nombres";
		tab_empleados_departamento=utilitario.consultar(str_sql_emp_reg_pago);
	}

	System.out.println("emp "+tab_empleados_departamento.getSql());

	System.out.println("tab drol "+tab_drol.getSql());

	P_NRH_RUBRO_REGION=utilitario.getVariable("p_nrh_rubro_region");
	P_NRH_RUBRO_DESC_VALORES_LIQUIDAR=utilitario.getVariable("p_nrh_rubro_desc_valores_liquidar");
	P_NRH_RUBRO_REMUNERACION_UNIFICADA=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada");
	P_NRH_RUBRO_RMU_HONORARIOS=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada_honorarios");
	P_NRH_RUBRO_ACUMULA_FONDOS=utilitario.getVariable("p_nrh_rubro_acumula_fondos_reserva");
	P_NRH_RUBRO_DIAS_TRABAJADOS=utilitario.getVariable("p_nrh_rubro_dias_trabajados");
	P_NRH_RUBRO_DIAS_ANTIGUEDAD=utilitario.getVariable("p_nrh_rubro_dias_antiguedad");
	P_NRH_RUBRO_DIAS_FONDOS_RESERVA=utilitario.getVariable("p_nrh_rubro_dias_fondos_reserva");
	P_NRH_RUBRO_DIAS_PERIODO=utilitario.getVariable("p_nrh_rubro_dias_periodo_nomina");
	P_NRH_RUBRO_RMU_CARGO_SUBROGANTE=utilitario.getVariable("p_nrh_rubro_rmu_cargo_subrogante");
	P_NRH_RUBRO_DIAS_SUBROGADOS=utilitario.getVariable("p_nrh_rubro_dias_subrogados");
	P_NRH_RUBRO_DIAS_AJUSTE_SUELDO=utilitario.getVariable("p_nrh_rubro_dias_ajuste_sueldo");
	P_NRH_RUBRO_AJUSTE_SUELDO=utilitario.getVariable("p_nrh_rubro_ajuste_sueldo");		
	P_NRH_RUBRO_DIAS_PEND_VACACION=utilitario.getVariable("p_nrh_rubro_dias_pendientes_vacacion");
	P_NRH_ACUMULA_DECIMOS=utilitario.getVariable("p_nrh_rubro_acumula_decimos");		
	P_NRH_BASE_IMPONIBLE_MES_ANTERIOR=utilitario.getVariable("p_nrh_rubro_base_imponible_mes_anterior");
	P_NRH_RUBRO_FONRESERV_ACUM_PAGO=utilitario.getVariable("p_nrh_rubro_fondreser_acum_pago");
	P_NRH_RUBRO_FONRESERV_NOM_PAGO=utilitario.getVariable("p_nrh_rubro_fondreser_nomi_pago");	


	String ide_geedp="";
	String ide_gtemp="";
	String fecha_ingreso="";
	String fecha_contrato="";
	String RMU="";
	String IDE_GEREG="";
	String acumula_fondos="";
	String acumula_decimos="";
	String fecha_subroga="";
	String RMU_CARGO_SUBROGA="";
	String ajuste_sueldo="";
	String fecha_ajuste="";
	String fecha_fin_subrogacion="";
	String dias_pendientes_vacacion="";
	String base_imponible_mes_anterior="";
	String fondo_reserva_acum_pago="";
	String fondo_reserva_nom_pago="";
	String fecha_ingreso_emgirs="";
	String ide_gttem="";
	String continuidad_liquidacion_geedp="";
	TablaGenerica tab_rub_faltantes=getRubrosFaltantes(str_ide_nrrol);
	if (tab_rub_faltantes.getTotalFilas()>0){
		utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'NRH_DETALLE_ROL'");
		ide_inicial=utilitario.getConexion().getMaximo("NRH_DETALLE_ROL", "IDE_NRDRO", 1);
	}

	System.out.println("tab rub_faltantes "+tab_rub_faltantes.getSql());

	for (int j = 0; j < tab_empleados_departamento.getTotalFilas(); j++) {


		ide_geedp=tab_empleados_departamento.getValor(j, "ide_geedp");
		for (int i = 0; i < tab_rub_faltantes.getTotalFilas(); i++) {
			String str_insert="INSERT INTO NRH_DETALLE_ROL (IDE_NRDRO, IDE_NRROL, IDE_GEEDP, IDE_NRDER, VALOR_NRDRO, USUARIO_INGRE, FECHA_INGRE, HORA_INGRE, USUARIO_ACTUA, FECHA_ACTUA, HORA_ACTUA,ORDEN_CALCULO_NRDRO) " +
					"VALUES ("+ide_inicial+", " +
					""+str_ide_nrrol+", " +
					""+ide_geedp+", " +
					""+tab_rub_faltantes.getValor(i,"IDE_NRDER")+", " +
					"0.00, NULL, NULL, NULL, NULL, NULL, NULL,NULL)";
			utilitario.getConexion().ejecutarSql(str_insert);
			ide_inicial++;
		}
	}



	indice_detalle=0;
	indice_ultimo_detalle=0;
	int nro_dias_comercial_nrtit=0;
	try {
		nro_dias_comercial_nrtit=pckUtilidades.CConversion.CInt(getTipoRol(ide_nrtit).getValor("NRO_DIAS_COMERCIAL_NRTIT"));
	} catch (Exception e) {
		// TODO: handle exception
	}




	// limpio la tabla nrh_detalle del rol para recalcular
	tab_deta_rubros_rol=getDetalleRubrosRolVacia();

	str_ide_nramo_descontar="";
	for (int j = 0; j < tab_empleados_departamento.getTotalFilas(); j++) {

		//			if (j==15){
		//				break;
		//			}
		ide_geedp=tab_empleados_departamento.getValor(j, "ide_geedp");
		ide_gtemp=tab_empleados_departamento.getValor(j, "ide_gtemp");
 
		fecha_ingreso=tab_empleados_departamento.getValor(j, "FECHA_INGRESO_GTEMP");
		fecha_ingreso_emgirs=tab_empleados_departamento.getValor(j, "fecha_ingreso_grupo_gtemp");
		fecha_contrato=tab_empleados_departamento.getValor(j, "FECHA_GEEDP");
		RMU=tab_empleados_departamento.getValor(j, "RMU_GEEDP");
		IDE_GEREG=tab_empleados_departamento.getValor(j, "IDE_GEREG");
		acumula_fondos=tab_empleados_departamento.getValor(j, "ACUMULA_FONDOS_GEEDP");
		acumula_decimos=tab_empleados_departamento.getValor(j, "ACUMULA_DECIMO_GTEMP");
		fecha_subroga=tab_empleados_departamento.getValor(j,"FECHA_ENCARGO_GEEDP");
		RMU_CARGO_SUBROGA=tab_empleados_departamento.getValor(j,"SUELDO_SUBROGA_GEEDP");
		ajuste_sueldo=tab_empleados_departamento.getValor(j,"AJUSTE_SUELDO_GEEDP");
		fecha_ajuste=tab_empleados_departamento.getValor(j,"FECHA_AJUSTE_GEEDP");
		fecha_fin_subrogacion=tab_empleados_departamento.getValor(j,"FECHA_ENCARGO_FIN_GEEDP");
		//dias_pendientes_vacacion=tab_empleados_departamento.getValor(j,"DIAS_VACACION");
		ide_gttem=tab_empleados_departamento.getValor(j,"ide_gttem");
		continuidad_liquidacion_geedp=tab_empleados_departamento.getValor(j,"ide_gttem");
		System.out.println("valor tomado de la tabla acumula decimo "+acumula_decimos);
		System.out.println("valor tomado de la tabla acumula fodnos "+acumula_fondos);
 
		// calculamos el rol del empleado 
		long ini=System.currentTimeMillis();
		//			System.out.println("ide geedp "+ide_geedp+" indice ultimo detalle "+indice_ultimo_detalle+" det rol "+tab_drol.getSql());
		calcularRolIndividualLiquidacion(tab_drol, ide_gtemp,ide_geedp, str_ide_nrrol, fecha_ingreso, fecha_contrato, IDE_GEREG,RMU, acumula_fondos, ide_gepro1, fecha_ini_gepro, fecha_fin_gepro,fecha_subroga,fecha_fin_subrogacion,RMU_CARGO_SUBROGA,ajuste_sueldo,fecha_ajuste,dias_pendientes_vacacion,nro_dias_comercial_nrtit,IDE_NRTIN,acumula_decimos,base_imponible_mes_anterior,fondo_reserva_acum_pago,fondo_reserva_nom_pago,fecha_ingreso_emgirs,ide_gttem,continuidad_liquidacion_geedp);
		long fin=System.currentTimeMillis();
	}

	utilitario.getConexion().ejecutarSql("DELETE FROM NRH_DETALLE_ROL where ide_nrdro in ( " +
			"select IDE_NRDRO " +
			"from NRH_DETALLE_ROL DRO " +
			"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER " +
			"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
			"INNER JOIN NRH_FORMA_CALCULO FOC ON FOC.IDE_NRFOC=RUB.IDE_NRFOC " +
			"where DRO.IDE_NRROL="+str_ide_nrrol+" and DER.ACTIVO_NRDER!=true )");


	//if (!IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos")) 
	if (!utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos").contains(IDE_NRTIN)
			&& !IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
		utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_RENTA_NRROL=0 where IDE_NRROL="+str_ide_nrrol);
	}else{
		utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_RENTA_NRROL=1 where IDE_NRROL="+str_ide_nrrol);		
	}

	utilitario.getConexion().ejecutarSql("update NRH_ROL set ESTADO_CALCULADO_NRROL=1 where IDE_NRROL="+str_ide_nrrol);

	try {
		str_ide_nramo_descontar=str_ide_nramo_descontar.substring(0, str_ide_nramo_descontar.length()-1);
	} catch (Exception e) {
		// TODO: handle exception
	}

	System.out.println("nramo a descontar: "+str_ide_nramo_descontar);
	String strSql="update NRH_AMORTIZACION set IDE_NRROL="+str_ide_nrrol+" where IDE_NRAMO in ("+str_ide_nramo_descontar+") ";
	if (!str_ide_nramo_descontar.isEmpty()){
		System.out.println("reCalcularNomina boolean strSql: "+strSql);
		utilitario.getConexion().ejecutarSql(strSql);
	}


	long fin = System.currentTimeMillis();
	System.out.println("======== Tiempo total RECALCULAR (TODO EL PROCESO) guardar pantalla : " + (fin - inicio));
}










/**Busca todos los empleados que pretenecen a ese detalle de tipo de nomina 
 * @param ide_nrdtn Detalle de tipo de nomina
 * @return TablaGenerica con los empleados de ese tipo de nomina
 */
public String getSqlEmpleadosNominaLiquidacionEmpleado(String ide_nrrol, String empleado){
	String ide_nrdtn=getRol(ide_nrrol).getValor("ide_nrdtn");
	String IDE_NRTIN="";
	try {
		IDE_NRTIN=getDetalleTipoNomina(ide_nrdtn).getValor("IDE_NRTIN");	
	} catch (Exception e) {
		// TODO: handle exception
	}

	if (ide_nrdtn==null || ide_nrdtn.isEmpty()){
		ide_nrdtn="-1";
	}
	


	String sql="select ";

	sql+="a.IDE_GEEDP," +
			"a.DOCUMENTO_IDENTIDAD_GTEMP," +
			"a.NOMBRES, " +
			"a.IDE_SUCU, " +
			"a.IDE_GTEMP, " +
			"a.IDE_GTTEM, "+
			"a.FECHA_GEEDP, "+	
			"a.RMU_GEEDP, "+
			"a.AJUSTE_SUELDO_GEEDP, " +
			"a.FECHA_AJUSTE_GEEDP, "+
			"a.fecha_ingreso_grupo_gtemp, "+//awbecerra
			"a.FECHA_INGRESO_GTEMP, "+
			"a.ACUMULA_FONDOS_GEEDP, "+
			"a.IDE_GEREG, "+
			"a.FECHA_ENCARGO_GEEDP, " +
			"a.SUELDO_SUBROGA_GEEDP, " +
			"a.FECHA_ENCARGO_FIN_GEEDP, " +
			"b.dias_vacacion, " +
			"a.DISCAPACITADO_GTEMP, acumula_decimo_gtemp, "+
			"c.PORCENTAJE_GTDIE,"
			+ "a.continuidad_liquidacion_geedp ";
	sql+="from " +
			"( ";


	sql+="select " +
			"EDP.IDE_GEEDP," +
			"EMP.DOCUMENTO_IDENTIDAD_GTEMP," +
			"EMP.APELLIDO_PATERNO_GTEMP ||' '|| " +
			"( case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)||' '|| " +
			"EMP.PRIMER_NOMBRE_GTEMP ||' '|| " +
			"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES, " +
			"EDP.IDE_SUCU, " +
			"EMP.IDE_GTEMP, " +
			"EMP.DISCAPACITADO_GTEMP, acumula_decimo_gtemp,"+
			"EDP.IDE_GTTEM, emp.fecha_ingreso_grupo_gtemp,edp.continuidad_liquidacion_geedp, ";

	if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
		sql+="EDP.FECHA_LIQUIDACION_GEEDP AS FECHA_GEEDP, ";	
	}else{
		sql+="EDP.FECHA_GEEDP, ";
	}

	sql+="EDP.RMU_GEEDP, "+
			"EDP.AJUSTE_SUELDO_GEEDP, " +
			"EDP.FECHA_AJUSTE_GEEDP, "+
			"EMP.FECHA_INGRESO_GTEMP, "+
			"EDP.ACUMULA_FONDOS_GEEDP, "+
			"DIP.IDE_GEREG, "+
			"EDP.FECHA_ENCARGO_GEEDP,EDP.SUELDO_SUBROGA_GEEDP,EDP.FECHA_ENCARGO_FIN_GEEDP "+

			"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
			"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP " +
			"inner join SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU " +
			"LEFT JOIN GEN_DIVISION_POLITICA DIP ON DIP.IDE_GEDIP=SUC.IDE_GEDIP " +
			"where EDP.IDE_GEEDP in ( " +
			"select EDP.IDE_GEEDP " +
			"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
			"inner join NRH_DETALLE_ROL detrol on edp.IDE_GEEDP=DETROL.IDE_GEEDP " +
			"inner join NRH_ROL rol on rol.ide_nrrol=DETROL.IDE_nrrol " +
			"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM and dtn.ide_nrdtn=rol.ide_nrdtn " +
			"WHERE detrol.IDE_NRROL="+ide_nrrol+"   ";
			if (empleado==null || empleado.equals("") || empleado.isEmpty() ) {
				//sql+=" AND EDP.IDE_GEEDP="+empleado+" ";
			}else {
				sql+=" AND EDP.IDE_GEEDP="+empleado+" ";
			}
			

	if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
		sql+="AND EDP.LIQUIDACION_GEEDP=1 "+
				"and (EDP.EJECUTO_LIQUIDACION_GEEDP IS NULL OR EDP.EJECUTO_LIQUIDACION_GEEDP!=1) ";

	}else{
		sql+="AND (EDP.LIQUIDACION_GEEDP IS NULL OR EDP.LIQUIDACION_GEEDP!=1) ";
	}

	sql+="GROUP BY edp.IDE_GEEDP ) " +
			"ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC,EMP.APELLIDO_MATERNO_GTEMP ASC  ";

	sql+=")a " +
			"left join " +
			"( " +
			"SELECT IDE_ASVAC,IDE_GTEMP,DIAS_VACACION FROM ( " +
			"SELECT a.IDE_ASVAC,edp.IDE_GTEMP, " +
			"(DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_VACACION " +
			"FROM ( " +
			"SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, " +
			"(case when SUM(DIA_ADICIONAL_ASDEV) is null then 0 else SUM(DIA_ADICIONAL_ASDEV) end) as NRO_DIAS_ADICIONAL, " +
			"(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO, " +
			"(case when SUM(DIA_SOLICITADO_ASDEV) is null then 0 else SUM(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO " +
			"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = TRUE GROUP BY IDE_ASVAC " +
			") a " +
			"inner join ASI_VACACION vac on VAC.IDE_ASVAC=a.IDE_ASVAC " +
			"inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR edp on edp.ide_gtemp=vac.ide_gtemp  " +
			"and edp.ide_geedp in ( " +
			"select EDP.IDE_GEEDP " +
			"from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
			"inner join NRH_DETALLE_ROL detrol on edp.IDE_GEEDP=DETROL.IDE_GEEDP " +
			"inner join NRH_ROL rol on rol.ide_nrrol=DETROL.IDE_nrrol " +
			"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM and dtn.ide_nrdtn=rol.ide_nrdtn " +
			"WHERE detrol.IDE_NRROL="+ide_nrrol+" ";

	if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
		sql+="AND EDP.LIQUIDACION_GEEDP=1 "+
				"and (EDP.EJECUTO_LIQUIDACION_GEEDP IS NULL OR EDP.EJECUTO_LIQUIDACION_GEEDP!=1) ";

	}else{
		sql+="AND (EDP.LIQUIDACION_GEEDP IS NULL OR EDP.LIQUIDACION_GEEDP!=1) ";
	}

	sql+="GROUP BY edp.IDE_GEEDP ) " +

			") a " +
			"GROUP BY IDE_ASVAC,IDE_GTEMP,DIAS_VACACION  " +

			")b  " +
			"on a.ide_gtemp=b.ide_gtemp "+
			"left join ( " +
			"select IDE_GTEMP,PORCENTAJE_GTDIE from GTH_DISCAPACIDAD_EMPLEADO where ide_gtemp in ( " +
			"select IDE_GTEMP from GTH_EMPLEADO  where DISCAPACITADO_GTEMP=TRUE) " +
			")c on c.ide_gtemp=a.ide_gtemp and c.ide_gtemp=b.ide_gtemp " +
			"order by nombres ";

	System.out.println(sql);
	return sql;
}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////77





}
