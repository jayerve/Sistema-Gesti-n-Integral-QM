package paq_sri.ejb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import java.util.List;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import framework.aplicacion.TablaGenerica;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gestion.ejb.ServicioEmpleado;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Utilitario;

@Stateless
public class ServicioSRI {
	private Utilitario utilitario = new Utilitario();

	@EJB
	private ServicioEmpleado serv_empleado;
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	
	private Document doc_rdep;

	//ANEXO RDEP ecevallos
	public File[] generarAnexoRDEP(String IDE_SRIMR) throws IOException{
		TablaGenerica tab_imp_renta=getImpuestoRenta(IDE_SRIMR);
		String  fecha_inicio = tab_imp_renta.getValor("fecha_inicio_srimr");
		String   fecha_fin =tab_imp_renta.getValor("fecha_fin_srimr");
		String anioHasta =utilitario.getAnio(fecha_fin)+"";
		
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
		String fileName= "RDEP"+anioHasta;
		String master = System.getProperty("user.dir");
		String path= master + "/";			
		
		File archivotxt=new File(path+fileName.concat(".csv"));
		BufferedWriter escribir=new BufferedWriter(new FileWriter(archivotxt)); 
		
		escribir.write("nuruc,anio,benGalpg,enfcatastro,tipidret,idret,apellidotrab,nombretrab,estab,residenciatrab,pasisresidencia,aplicaconvenio,"+
		"tipotrabajodiscap,porcentajediscap,tipiddiscap,iddiscap,suelsal,sobsuelcomremu,partutil,intgrabgen,imprentempl,decimotercer,"+
		"decimocuarto,fondoreserva,salariodigno,otrosingrengrav,inggravconesteempl,sissalnet,apoperiess,aporperiessconotrosempls,vivienda,"+
		"salud,educacion,alimentacion,vestimenta,exodiscap,exotered,basimp,valretasuotrosempls,valimpasuesteempl,valret,impuestocausado,remunContrEstEmpl,remunContrOtrEmpl,exonRemunContr,totRemunContr,numMesTrabContrEstEmpl,numMesTrabContrOtrEmpl,"+
		"totNumMesTrabContr,remunMenPromContr,numMesContrGenEstEmpl,numMesContrGenOtrEmpl, totNumMesContrGen,totContrGen,credTribDonContrOtrEmpl,credTribDonContrEstEmpl,credTribDonContrNOEstEmpl,"
		+"totCredTribDonContr,contrPag,contrAsuOtrEmpl,contrRetOtrEmpl,contrAsuEstEmpl,contrRetEstEmpl");
		
		///verificar 
		TablaGenerica tab_anio= utilitario.consultar("SELECT * FROM GEN_ANIO WHERE DETALLE_GEANI='"+anioHasta+"'");
		String str_ide_anio=tab_anio.getValor("IDE_GEANI");

		/*TablaGenerica tab_empleados=utilitario.consultar("select distinct a.ide_gtemp,empleado,documento_identidad_gtemp,codigo_documento_identidad, " +
				"codigo_ciudad,canton,provincia,codigo_provincia,detalle_gtdir,discapacidad,detalle_gttds,detalle_gtgrd " +
				"from ( " +
				"select a.ide_gtemp,c.ide_geedp,apellido_paterno_gtemp||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as empleado " +
				",documento_identidad_gtemp,a.ide_gttdi,a.ide_gedip,detalle_gttdi,codigo_sri_gttdi as codigo_documento_identidad, " +
				"(case when discapacitado_gtemp is null then true else discapacitado_gtemp end) as discapacidad " +
				"from gth_empleado a, gth_tipo_documento_identidad b,gen_empleados_departamento_par c " +
				"where a.ide_gttdi = b.ide_gttdi and a.ide_gtemp = c.ide_gtemp ) a " +
				"left join ( " +
				"select ide_gtemp,detalle_gtdir,ide_gtdir from gth_direccion where  activo_gtdir=true  group by ide_gtemp ,detalle_gtdir,ide_gtdir " +
				"having ide_gtdir in ( " +
				"select max(ide_gtdir) as ev from gth_direccion where  activo_gtdir=true  group by ide_gtemp " +
				") ) b on a.ide_gtemp = b.ide_gtemp " +
				"left join ( " +
				"select distinct ide_geedp from ( " +
				"select * from nrh_rol where ide_nrdtn in (  select ide_nrdtn from nrh_detalle_tipo_nomina where ide_nrtin=0) " +
				") a,( " +
				"select distinct ide_geedp,ide_nrrol from nrh_detalle_rol " +
				") b,( select ide_gepro,a.ide_gemes,a.ide_geani,detalle_gemes,detalle_geani " +
				",cast((to_char(to_date('"+fecha_inicio+"','yyyy-mm-dd'),'MM')) as Integer) as mes " +
				"from gen_perido_rol a, gen_mes b, gen_anio c where a.ide_gemes = b.ide_gemes and a.ide_geani = c.ide_geani " +
				"and a.ide_gemes between cast((to_char(to_date('"+fecha_inicio+"','yyyy-mm-dd'),'MM')) as Integer) and cast((to_char(to_date('"+fecha_fin+"','yyyy-mm-dd'),'MM')) as Integer) " +
				"and detalle_geani = to_char(to_date('2013-03-01','yyyy-mm-dd'),'yyyy') ) c " +
				"where a.ide_nrrol = b.ide_nrrol and a.ide_gepro = c.ide_gepro " +
				") c on a.ide_geedp = c.ide_geedp " +
				"left join ( select a.ide_gedip,a.detalle_gedip as ciudad,a.codigo_sri_gedip as codigo_ciudad,canton,provincia,codigo_provincia " +
				"from (select * from gen_division_politica where ide_getdp=2) a " +
				"left join ( select a.ide_gedip,a.detalle_gedip as canton,b.codigo_sri_gedip as codigo_provincia,b.detalle_gedip as provincia " +
				"from (select * from gen_division_politica where ide_getdp=3) a " +
				"left join (select * from gen_division_politica where ide_getdp=1) b on a.gen_ide_gedip = b.ide_gedip " +
				") b on a.gen_ide_gedip = b.ide_gedip order by a.detalle_gedip ) d on a.ide_gedip = d.ide_gedip " +
				"left join ( select ide_gtemp,detalle_gttds,detalle_gtgrd from gth_discapacidad_empleado a, " +
				"gth_grado_discapacidad b, gth_tipo_discapacidad c " +
				"where a.ide_gtgrd= b.ide_gtgrd and a.ide_gttds = c.ide_gttds and activo_gtdie=true " +
				"and ide_gtdie in ( select max(ide_gtdie) from gth_discapacidad_empleado where activo_gtdie=true group by ide_gtemp ) " +
				") e  on a.ide_gtemp = e.ide_gtemp order by  empleado");  */

		//////////////////////////////////////////////////////////////////////////ALEX
		
		

		TablaGenerica rmu=utilitario.consultar("select valor_nrdro,valor_nrdro from nrh_detalle_rol  drol "
				+ "left join nrh_rol rol  on rol.ide_nrrol=drol.ide_nrrol  "
				+ "where rol.ide_gepro in(select ide_gepro from gen_perido_rol where ide_geani="+str_ide_anio+") and drol.ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub in (276)) "
				+ "limit 1");

double rmu1=0,rmu2=0;
if (rmu.getValor("valor_nrdro")== null || rmu.getValor("valor_nrdro").equals("") || rmu.getValor("valor_nrdro").isEmpty()) {
	rmu1=0.00;
}else {
	rmu1=Double.parseDouble(rmu.getValor("valor_nrdro"));

	
}
		



		TablaGenerica rmuAnterior=utilitario.consultar("select valor_nrdro,valor_nrdro from nrh_detalle_rol  drol "
				+ "left join nrh_rol rol  on rol.ide_nrrol=drol.ide_nrrol  "
				+ "where rol.ide_gepro in(select ide_gepro from gen_perido_rol where ide_geani="+str_ide_anio+") and drol.ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub in (284)) "
				+ "limit 1");

		if (rmuAnterior.getValor("valor_nrdro")== null || rmuAnterior.getValor("valor_nrdro").equals("") || rmuAnterior.getValor("valor_nrdro").isEmpty()) {
			rmu2=0;
		}else {
			rmu2=Double.parseDouble(rmuAnterior.getValor("valor_nrdro"));

			
		}
			
        //Fecha inicio y fin busqueda rubros decimo cuarto sueldo generado
		String fecha1="",fecha2="";
		
		fecha1=(Integer.parseInt(anioHasta)-1)+"-08-31";
		fecha2=anioHasta+"-07-31";
		String ide_gepro=ser_nomina.getPeriodosRol(fecha1, fecha2);
				
		if (ide_gepro!=null && !ide_gepro.equalsIgnoreCase("") && !ide_gepro.isEmpty()){
		}else {
			ide_gepro="";
			ide_gepro="-1";
		}

		
		
		
		
		String query = "select * from ( "
				+ "select   *,"
				/*+ " coalesce( round(((basimp - (SELECT  case when (fraccion_basica_srdir - 1)<0 then 0 else (fraccion_basica_srdir-1) end as  fraccion_basica_srdir "+
		" FROM public.sri_detalle_impuesto_renta  where  ide_srimr = " + IDE_SRIMR + " and basimp between fraccion_basica_srdir and exceso_hasta_srdir))*     "+
		" (SELECT coalesce(imp_fraccion_excedente_srdir ,0.00)/100     FROM public.sri_detalle_impuesto_renta   "+
		" where  ide_srimr = " + IDE_SRIMR + " and basimp between fraccion_basica_srdir and exceso_hasta_srdir)) + (SELECT  imp_fraccion_srdir    "+
		"  FROM public.sri_detalle_impuesto_renta   where  ide_srimr = " + IDE_SRIMR + " and basimp between fraccion_basica_srdir and exceso_hasta_srdir),2),0)  "+
		" as impuestoCausado "*/
		
				+ " round(round(coalesce( (basimp - (SELECT fraccion_basica_srdir as  fraccion_basica_srdir "+
				" FROM public.sri_detalle_impuesto_renta  where  ide_srimr = " + IDE_SRIMR + " and basimp between fraccion_basica_srdir and exceso_hasta_srdir))*     "+
				" (SELECT coalesce(imp_fraccion_excedente_srdir ,0.00)/100     FROM public.sri_detalle_impuesto_renta   "+
				" where  ide_srimr = " + IDE_SRIMR + " and basimp between fraccion_basica_srdir and exceso_hasta_srdir) + (SELECT  imp_fraccion_srdir    "+
				"  FROM public.sri_detalle_impuesto_renta   where  ide_srimr = " + IDE_SRIMR + " and basimp between fraccion_basica_srdir and exceso_hasta_srdir),0),3),2)  "+
				" as impuestoCausado "
		
		+ "  from ( select distinct( select identificacion_empr from sis_empresa limit 1) as nuRuc,      "+
		"  substring(detalle_gttdi from 1 for 1) as tipIdRet,   documento_identidad_gtemp as idRet,    "+
		"  APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP as apellidoTrab,     "+
		"  COALESCE(coalesce(PRIMER_NOMBRE_GTEMP,' ') ||' '|| coalesce(SEGUNDO_NOMBRE_GTEMP,' '),' ') as nombreTrab,   '001' as estab,    "+
		"  '01'   as residenciaTrab,   '593' as pasisResidencia,    "+
		"  'NA' as aplicaConvenio,  case when (round(COALESCE(porcentaje_gtdie,0)) >= 40) then '02' else '01' end as tipoTrabajoDiscap, case when "+
		" round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE(porcentaje_gtdie,0)) else 0 end  as porcentajeDiscap, 'N' as tipidDiscap,   '999' as idDiscap,    "+
		"  COALESCE(sueldos,0.00)  + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) as suelSal,    COALESCE(sobresueldos,0.00) as sobSuelComRemu,    "+
		"  '0' as partUtil,   COALESCE(r301_srfor,0.00) as intGrabGen,   '0' as impRentEmpl,   COALESCE(decimoTercero,0.00)-COALESCE(decimoTercerAjuste,0.00) as decimoTercer,    ";
		if(Integer.parseInt(anioHasta)<=2017){

		query+=" COALESCE(decimoCuarto, 0.00) as decimoCuarto , ";
		}else{
			query+=" case   when COALESCE(decimoCuarto, 0.00)+COALESCE(decimoCuartoAjuste,0.00)>"+rmu2+" then "+rmu1+"  else COALESCE(decimoCuarto, 0.00)+COALESCE(decimoCuartoAjuste,0.00) end as decimoCuarto,  ";
		}
	    query+=" COALESCE(fondo, 0.00) as  fondoReserva,  '0' as salarioDigno, COALESCE(otrosIngRen,0.00) as otrosIngRenGrav,   COALESCE(sueldos,0.00) + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) + COALESCE(sobresueldos,0.00)  "+
		"   as ingGravConEsteEmpl,   '1' as sisSalNet,   COALESCE(aporteIess, 0.00) as apoPerIess,   COALESCE(r351_srfor,0.00) as aporPerIessConOtrosEmpls,    "+
		
		"  case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR) * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS "+
		"  WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)  "+
		"  from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA  "+
		"  where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end  as descuentoDiscapacidad , "+
		
		"  case when round(FLOOR(((extract(year from cast('"+anioHasta+"-12-31' as Date) )-extract(year from fecha_nacimiento_gtemp))* 372 + "+
		" (extract(month from cast('"+anioHasta+"-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 + "+  
		" (extract (day from cast('"+anioHasta+"-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS "+
		" WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA  "+
		" where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end  as descuentoTercera , "+
 
"     COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <     "+
"      (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
"      +COALESCE(ali.valor_deducible_srdee,0.00)))     "+
		"  then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when "+
		"  EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp "+
		"  else '"+anioHasta+"-01-1' end::date)) < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  "+
		"  else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(vivienda.valor_deducible_srdee,0.00))   as vivienda,       "+
"    COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <     "+
"   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
"   +COALESCE(ali.valor_deducible_srdee,0.00)))     "+
		"  then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(salud.valor_deducible_srdee,0.00))  as salud,       "+
"   COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <     "+
"   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
"   +COALESCE(ali.valor_deducible_srdee,0.00)))     "+
		"  then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(educa.valor_deducible_srdee,0.00))   as educacion,       "+
"   COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <     "+
"   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
"   +COALESCE(ali.valor_deducible_srdee,0.00)))     "+
		"  then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		" then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
"   else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(ali.valor_deducible_srdee,0.00))  as alimentacion,      "+ 
"   COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <     "+
"   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
"   +COALESCE(ali.valor_deducible_srdee,0.00)))     "+
		"  then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
"   else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(vest.valor_deducible_srdee,0.00)) as vestimenta,     "+  
		"  case when round( cast (COALESCE(COALESCE(sueldos,0.00)+ COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) + "+
		"  COALESCE(sobresueldos,0.00),0.00) + COALESCE(r301_srfor,0.00) - COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <  "+
"      (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
"      +COALESCE(ali.valor_deducible_srdee,0.00))) "+
		"  then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(vivienda.valor_deducible_srdee,0.00))       "+  
"    -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 < "+
"   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
"   +COALESCE(ali.valor_deducible_srdee,0.00))) "+
		"  then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(salud.valor_deducible_srdee,0.00))       "+
"   -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 < "+
"   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
"   +COALESCE(ali.valor_deducible_srdee,0.00))) "+
		"  then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(educa.valor_deducible_srdee,0.00))       "+
"   -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 < "+
"   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
"   +COALESCE(ali.valor_deducible_srdee,0.00))) "+
		"  then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(ali.valor_deducible_srdee,0.00))        "+	
"   -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 < "+
"   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
"   +COALESCE(ali.valor_deducible_srdee,0.00))) "+
		"  then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(vest.valor_deducible_srdee,0.00))      "+ 
		"  - COALESCE(aporteIess, 0.00) -COALESCE(r351_srfor,0.00)-(case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR) * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS "+
		"  WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)  "+
		"  from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA  "+
		"  where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end) "+
		"  -(case when round(FLOOR(((extract(year from cast('"+anioHasta+"-12-31' as Date))-extract(year from fecha_nacimiento_gtemp))* 372 + "+
		"  (extract(month from cast('"+anioHasta+"-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 + "+  
		"  (extract (day from cast('"+anioHasta+"-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS "+
		"  WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA  "+
		"  where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end)  as numeric),2) > 0 "+
"    then         round( cast (COALESCE(COALESCE(sueldos,0.00)+ COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) +    "+
		"  COALESCE(sobresueldos,0.00),0.00) + COALESCE(r301_srfor,0.00) -  COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <  "+
"     (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
"     +COALESCE(ali.valor_deducible_srdee,0.00))) "+
		"  then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
	    "  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(vivienda.valor_deducible_srdee,0.00))     "+
"   -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 < "+
"   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
		"  +COALESCE(ali.valor_deducible_srdee,0.00)))  then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , "+
		"  FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(salud.valor_deducible_srdee,0.00))      "+ 
"   -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <     "+
"   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
"   +COALESCE(ali.valor_deducible_srdee,0.00))) "+
		"  then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(educa.valor_deducible_srdee,0.00))         "+		
"   -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <     "+
"   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
"   +COALESCE(ali.valor_deducible_srdee,0.00)))     "+
		"  then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', "+
		"  case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(ali.valor_deducible_srdee,0.00))      "+
"   -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <  "+
"   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)    "+
"   +COALESCE(ali.valor_deducible_srdee,0.00)))     "+
		"  then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when "+
		"  EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +    "+
		"  (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+
		"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))    "+
		"  else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2), "+
		"  COALESCE(vest.valor_deducible_srdee,0.00))        "+
		"  - COALESCE(aporteIess, 0.00) -COALESCE(r351_srfor,0.00)-(case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS "+
		"  WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)  "+
		"  from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA  "+
		"  where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end)"+
		"  -(case when round(FLOOR(((extract(year from cast('"+anioHasta+"-12-31' as Date))-extract(year from fecha_nacimiento_gtemp))* 372 + "+
		" (extract(month from cast('"+anioHasta+"-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 + "+  
		" (extract (day from cast('"+anioHasta+"-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS "+
		"  WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA  "+
		"  where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end)  as numeric),2)  else 0.00 end as basImp, COALESCE(r405_srfor,0.00) as valRetAsuOtrosEmpls,   '0' as valImpAsuEsteEmpl,    "+
		"  COALESCE(rentaI,0.00)  as valRet, COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) as VALOR_LIQUIDACION_GEEDP, "+
		"  round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100/3.3333333,2) as baseSolidaria, "+
		"  COALESCE(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00),0.00)  as solidario, "+
		"  round(round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100*(CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0 "+
		"  then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)/3.3333333,2)"+
		"  /((CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0 then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)) *0.0333,2) "+
		"  as impuestoSolidario , "+
		"  COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),0) as numMeses ,  "+
		"  round(round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100/3.3333333,2)/((CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0 "+
		"  then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)) ,2)  as promedio  from gth_empleado as emp    "+
		"      left join sri_deducibles_empleado as ali on ali.ide_gtemp   = emp.ide_gtemp  and ali.IDE_SRDED =  "+
		"      (select  ide_srded from  SRI_DEDUCIBLES sri left join sri_impuesto_renta as imp  "+
		"      on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "   and DETALLE_SRDED like '%ALIMENTACION%')   "+  
		"      left join sri_deducibles_empleado as salud on salud.ide_gtemp   = emp.ide_gtemp   "+
		"  and salud.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri  "+
		"      left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like '%SALUD%')  "+   
		"  left join sri_deducibles_empleado as vivienda on vivienda.ide_gtemp   = emp.ide_gtemp    "+
		"  and vivienda.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri  "+
		"       left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like '%VIVIENDA%')   "+  
		"  left join sri_deducibles_empleado as educa on educa.ide_gtemp   = emp.ide_gtemp  and educa.IDE_SRDED =  "+
		"       (select  ide_srded from  SRI_DEDUCIBLES sri left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr  "+
		"       where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like '%EDUCACION%')    left join sri_deducibles_empleado as vest on vest.ide_gtemp   =  "+
		"  emp.ide_gtemp and vest.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri  "+
		"       left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like '%VESTIMENTA%') "+   
		" left join   gth_tipo_documento_identidad as tdocumento on tdocumento.ide_gttdi = emp.ide_gttdi   "+
		" left join gth_discapacidad_empleado as disca on disca.ide_gtemp = emp.ide_gtemp   "+ 
		"  left join (select empDepar.ide_gtemp, sum(VALOR_LIQUIDACION_GEEDP) as VALOR_LIQUIDACION_GEEDP, fecha_geedp, FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP, "+
		"        STRING_AGG(cast (ide_gttem as text), '-') as ide_gttem from GEN_EMPLEADOS_DEPARTAMENTO_PAR as empDepar "+
		"        left join gen_detalle_empleado_departame as empDet on empDepar.ide_geded = empDet.ide_geded "+
		"  where  activo_geded = true and (COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )  "+
		"        is null or EXTRACT(YEAR  from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )  )="+anioHasta+") "+
		"        group by empDepar.ide_gtemp,fecha_geedp,FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP) "+
		"        as depar on depar.ide_gtemp= emp.ide_gtemp    left join    (SELECT IDE_GTEMP , SUM(DETAROL.VALOR_NRDRO) AS fondo    "+ 
		"        FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP   "+
		" left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "+
		" left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB   "+
		" where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))  "+
		" and detrubro.IDE_NRRUB in (347,345) group by IDE_GTEMP) as fondos on fondos.IDE_GTEMP = emp.IDE_GTEMP  "+
		"         left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS sueldos    FROM NRH_DETALLE_ROL detarol   "+  
		" left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "+
		" left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "+
		"         left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in  "+
		"         (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))    "+
		" and detrubro.IDE_NRRUB in (288,344) group by IDE_GTEMP) sueldo on sueldo.IDE_GTEMP = emp.IDE_GTEMP  "+
		
		" left join sri_formulario_107 f107 on f107.ide_gtemp=emp.ide_gtemp and IDE_SRIMR= " + IDE_SRIMR +
		"  left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) as aporte , count(DETAROL.VALOR_NRDRO)  as numMesesAporte  "+
		"  FROM NRH_DETALLE_ROL detarol left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP    "+
		"        left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "+
		"         left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in  "+
		"         (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))    "+
		"         and detrubro.IDE_NRRUB in (356) and VALOR_NRDRO !=0  group by IDE_GTEMP) solidario on solidario.IDE_GTEMP = emp.IDE_GTEMP    "+
		"         left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) as varios , count(DETAROL.VALOR_NRDRO)  as numMesesVarios FROM NRH_DETALLE_ROL detarol   "+  
		"         left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP    "+
		"        left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "+
		"         left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in  "+
		"         (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))    "+
		"         and detrubro.IDE_NRRUB in (203) and VALOR_NRDRO !=0 and ide_nrrol in (19,20) group by IDE_GTEMP) solidarioVarios on solidarioVarios.IDE_GTEMP = emp.IDE_GTEMP    "+
		"         left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS sobresueldos    FROM NRH_DETALLE_ROL detarol "+  
		" left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "+
		" left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "+
		" left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB   "+
		"         where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO  "+
		"        in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))    ";
		if(Integer.parseInt(anioHasta)>=2020){
			query+=" and detrubro.IDE_NRRUB in (27,386,313) group by IDE_GTEMP) sobre on sobre.IDE_GTEMP = emp.IDE_GTEMP  ";

		}else{
			query+=" and detrubro.IDE_NRRUB in (27,17,354,331,336,313) group by IDE_GTEMP) sobre on sobre.IDE_GTEMP = emp.IDE_GTEMP  ";
		}
		query+="         left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS DecimoTercero    FROM NRH_DETALLE_ROL detarol   "+  
		" left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "+
		" left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "+
		"        left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL  "+
		"        in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))   ";
		
		if(Integer.parseInt(anioHasta)>=2020){
		query+="and detrubro.IDE_NRRUB in (334,125) group by IDE_GTEMP) decimoT on decimoT.IDE_GTEMP = emp.IDE_GTEMP  ";

	}else{
		query+=" and detrubro.IDE_NRRUB in (334,368,367) group by IDE_GTEMP) decimoT on decimoT.IDE_GTEMP = emp.IDE_GTEMP  ";
	}
		
		
		query+="         left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoTercerAjuste     "+
		"          FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "+  
		" left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "+
		"          left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    where  IDE_NRROL in "+
		"          (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))  "+  
		" and detrubro.IDE_NRRUB in (369) group by IDE_GTEMP) decimoTA on decimoTA.IDE_GTEMP = emp.IDE_GTEMP  "+
	
		
		"           left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoCuarto    FROM NRH_DETALLE_ROL detarol  "+   
		" left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "+
		" left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "+
		" left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB   ";
		if(Integer.parseInt(anioHasta)<=2015){
		query+=" where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))  ";}
		else {
			query+=	" where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in ("+ide_gepro+") ) ";
			
		}
		query+=" and detrubro.IDE_NRRUB in (333) group by IDE_GTEMP) decimoC on decimoC.IDE_GTEMP = emp.IDE_GTEMP  "+
			
		
		"           left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoCuartoAjuste    FROM NRH_DETALLE_ROL detarol  "+   
		" left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "+
		" left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "+
		" left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB   ";
		if(Integer.parseInt(anioHasta)<=2015){

			query+=" where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))  ";}
		else {
			query+=" where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in ("+ide_gepro+") ) ";
		}
	
		query+=" and detrubro.IDE_NRRUB in (121) group by IDE_GTEMP) decimoTA1 on decimoTA1.IDE_GTEMP = emp.IDE_GTEMP  "+

		
		"           left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS rentaI   FROM NRH_DETALLE_ROL detarol    "+ 
		" left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "+
		" left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "+
		" left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB   "+
		"          where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in  "+
		"          (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))   and detrubro.IDE_NRRUB in (42) group by IDE_GTEMP) renta  "+
		"          on renta.IDE_GTEMP = emp.IDE_GTEMP   left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS aporteIess    "+
		"          FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "+  
		" left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "+
		" left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB   "+
		"          where  IDE_NRROL in (select IDE_NRROL from NRH_ROL  "+
		"          where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + "))    "+
		" and detrubro.IDE_NRRUB in (44) group by IDE_GTEMP) iess on iess.IDE_GTEMP = emp.IDE_GTEMP  "+
		
		" left join (SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS otrosIngRen    "+
		"       FROM NRH_DETALLE_ROL detarol  "+
		"       left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP   "+
		"       left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER    "+
		"       left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB            "+
		"       where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  " + str_ide_anio + ")) and detrubro.IDE_NRRUB in (335,392,393) "+ 
		"       group by IDE_GTEMP "+
	    "    ) otrosIngresos on otrosIngresos.IDE_GTEMP = emp.IDE_GTEMP "+
		
		
		" where coalesce(depar.ide_gttem,'') not like '%3%'  "+ 
		//" where coalesce(depar.ide_gttem,'') not like '%3%' and ( COALESCE(sueldos,0.00)  + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) )>0  "+ //awbecerra No pasantes y los que hayan percibido sueldo
		" order by APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP,   "+
		" COALESCE(coalesce(PRIMER_NOMBRE_GTEMP,' ') ||' '|| coalesce(SEGUNDO_NOMBRE_GTEMP,' '),' ')) y "
		+ " ) rdep "
		+ " where ( COALESCE (suelsal,0.00) + COALESCE (sobsuelcomremu,0.00)+ COALESCE (decimotercer,0.00)+ COALESCE (decimocuarto,0.00)+ COALESCE (fondoreserva,0.00)+ COALESCE (sobsuelcomremu,0.00) )>0  ";
		
		//query += " and idret in ('1708589641')  ";
		
		
		

		
		TablaGenerica tab_empleados=utilitario.consultar(query);

		
		
		System.out.println(query);

		try {

			String linea  ="";
			List lis_sql = utilitario.getConexion().consultar("SELECT identificacion_empr from sis_empresa where ide_empr=" + utilitario.getVariable("ide_empr"));
			if (lis_sql != null && !lis_sql.isEmpty()) {

				doc_rdep = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				doc_rdep.setXmlVersion("1.0");
				doc_rdep.setXmlStandalone(true);

				Element raiz = doc_rdep.createElement("rdep");
				raiz.appendChild(crearElemento("numRuc", null, lis_sql.get(0) + ""));
				raiz.appendChild(crearElemento("anio", null, anioHasta));

				linea+= lis_sql.get(0) + ",";
				linea+= anioHasta + ",";
				doc_rdep.appendChild(raiz);

				//CABECERA
				Element retRelDep = doc_rdep.createElement("retRelDep");
				Element datRetRelDep;
				Element empleado;
				Element contribucion;
				String ideEmpleado;
				raiz.appendChild(retRelDep);
				
				//DETALLE
				for (int i = 0; i < tab_empleados.getTotalFilas(); i++) {
					//System.out.println("empleado"+i);
					datRetRelDep = doc_rdep.createElement("datRetRelDep");
					retRelDep.appendChild(datRetRelDep);
					empleado = doc_rdep.createElement("empleado");
					datRetRelDep.appendChild(empleado);
					empleado.appendChild(crearElemento("benGalpg", null, "NO"));
					empleado.appendChild(crearElemento("enfcatastro", null, "NO"));
					empleado.appendChild(crearElemento("tipIdRet", null, tab_empleados.getValor(i, "tipIdRet")));
					empleado.appendChild(crearElemento("idRet", null, tab_empleados.getValor(i, "idRet")));
					empleado.appendChild(crearElemento("apellidoTrab", null, pckUtilidades.Utilitario.quitarCaracteresSpeciales(pckUtilidades.Utilitario.quitarAcentos(tab_empleados.getValor(i, "apellidoTrab")))));
					empleado.appendChild(crearElemento("nombreTrab", null, pckUtilidades.Utilitario.quitarCaracteresSpeciales(pckUtilidades.Utilitario.quitarAcentos(tab_empleados.getValor(i, "nombreTrab")))));
					empleado.appendChild(crearElemento("estab", null, "001"));
					empleado.appendChild(crearElemento("residenciaTrab", null, "01"));
					empleado.appendChild(crearElemento("paisResidencia", null, "593"));
					empleado.appendChild(crearElemento("aplicaConvenio", null,  "NA"));
					empleado.appendChild(crearElemento("tipoTrabajDiscap", null, tab_empleados.getValor(i, "tipoTrabajoDiscap")));
					empleado.appendChild(crearElemento("porcentajeDiscap", null, tab_empleados.getValor(i, "porcentajeDiscap")));
					empleado.appendChild(crearElemento("tipIdDiscap", null, "N"));
					empleado.appendChild(crearElemento("idDiscap", null, "999"));
					
					datRetRelDep.appendChild(crearElemento("suelSal", null, tab_empleados.getValor(i, "suelSal") ));
					datRetRelDep.appendChild(crearElemento("sobSuelComRemu", null, tab_empleados.getValor(i, "sobSuelComRemu")));
					datRetRelDep.appendChild(crearElemento("partUtil", null, "0"));	
					datRetRelDep.appendChild(crearElemento("intGrabGen", null, tab_empleados.getValor(i, "intGrabGen")));
					datRetRelDep.appendChild(crearElemento("impRentEmpl", null, "0"));	
					datRetRelDep.appendChild(crearElemento("decimTer", null, tab_empleados.getValor(i, "decimoTercer")));
					datRetRelDep.appendChild(crearElemento("decimCuar", null, tab_empleados.getValor(i, "decimoCuarto")));					
					datRetRelDep.appendChild(crearElemento("fondoReserva", null, tab_empleados.getValor(i, "fondoReserva")));
					datRetRelDep.appendChild(crearElemento("salarioDigno", null, "0"));
					datRetRelDep.appendChild(crearElemento("otrosIngRenGrav", null, tab_empleados.getValor(i, "otrosIngRenGrav")));
					datRetRelDep.appendChild(crearElemento("ingGravConEsteEmpl", null, tab_empleados.getValor(i, "ingGravConEsteEmpl")));
					datRetRelDep.appendChild(crearElemento("sisSalNet", null, "1"));
					datRetRelDep.appendChild(crearElemento("apoPerIess", null, tab_empleados.getValor(i, "apoPerIess")));
					datRetRelDep.appendChild(crearElemento("aporPerIessConOtrosEmpls", null, tab_empleados.getValor(i, "aporPerIessConOtrosEmpls")));
					datRetRelDep.appendChild(crearElemento("deducVivienda", null, tab_empleados.getValor(i, "vivienda")));
					datRetRelDep.appendChild(crearElemento("deducSalud", null, tab_empleados.getValor(i, "salud")));
					datRetRelDep.appendChild(crearElemento("deducEducartcult", null, tab_empleados.getValor(i, "educacion")));
					datRetRelDep.appendChild(crearElemento("deducAliement", null, tab_empleados.getValor(i, "alimentacion")));
					datRetRelDep.appendChild(crearElemento("deducVestim", null, tab_empleados.getValor(i, "vestimenta")));			
					datRetRelDep.appendChild(crearElemento("exoDiscap", null,  tab_empleados.getValor(i, "descuentoDiscapacidad")));
					datRetRelDep.appendChild(crearElemento("exoTerEd", null,  tab_empleados.getValor(i, "descuentoTercera")));
					datRetRelDep.appendChild(crearElemento("basImp", null, tab_empleados.getValor(i, "basImp")));
					datRetRelDep.appendChild(crearElemento("impRentCaus", null,tab_empleados.getValor(i, "impuestoCausado")));			
					datRetRelDep.appendChild(crearElemento("valRetAsuOtrosEmpls", null,  tab_empleados.getValor(i, "valRetAsuOtrosEmpls")));
					datRetRelDep.appendChild(crearElemento("valImpAsuEsteEmpl", null,  "0"));
					datRetRelDep.appendChild(crearElemento("valRet", null, tab_empleados.getValor(i, "valRet")));
					
						contribucion = doc_rdep.createElement("contribucion");
						datRetRelDep.appendChild(contribucion);
						contribucion.appendChild(crearElemento("remunContrEstEmpl", null, tab_empleados.getValor(i, "baseSolidaria")));
						contribucion.appendChild(crearElemento("remunContrOtrEmpl", null, "0" ));
						contribucion.appendChild(crearElemento("exonRemunContr", null, "0" ));
						contribucion.appendChild(crearElemento("totRemunContr", null,  tab_empleados.getValor(i, "baseSolidaria") ));
						contribucion.appendChild(crearElemento("numMesTrabContrEstEmpl", null, tab_empleados.getValor(i, "numMeses")  ));
						contribucion.appendChild(crearElemento("numMesTrabContrOtrEmpl", null,  "0"));
						contribucion.appendChild(crearElemento("totNumMesTrabContr", null, tab_empleados.getValor(i, "numMeses")  ));			
						contribucion.appendChild(crearElemento("remunMenPromContr", null, tab_empleados.getValor(i, "promedio")  ));
						contribucion.appendChild(crearElemento("numMesContrGenEstEmpl", null,  tab_empleados.getValor(i, "numMeses")));
						contribucion.appendChild(crearElemento("numMesContrGenOtrEmpl", null, "0" ));
						contribucion.appendChild(crearElemento("totNumMesContrGen", null,  tab_empleados.getValor(i, "numMeses")));
						contribucion.appendChild(crearElemento("totContrGen", null,tab_empleados.getValor(i, "impuestoSolidario"))); //pckUtilidades.CConversion.CStr(pckUtilidades.CConversion.CDbl(tab_empleados.getValor(i, "promedio"))*pckUtilidades.CConversion.CDbl(tab_empleados.getValor(i, "numMeses")) *0.033)));
						contribucion.appendChild(crearElemento("credTribDonContrOtrEmpl", null, "0" ));
						contribucion.appendChild(crearElemento("credTribDonContrEstEmpl", null,  "0"));
						contribucion.appendChild(crearElemento("credTribDonContrNOEstEmpl", null, "0" ));
						contribucion.appendChild(crearElemento("totCredTribDonContr", null,  "0"));
						contribucion.appendChild(crearElemento("contrPag", null,  tab_empleados.getValor(i, "impuestoSolidario")));
						contribucion.appendChild(crearElemento("contrAsuOtrEmpl", null, "0" ));
						contribucion.appendChild(crearElemento("contrRetOtrEmpl", null,  "0"));
						contribucion.appendChild(crearElemento("contrAsuEstEmpl", null, "0" ));
						contribucion.appendChild(crearElemento("contrRetEstEmpl", null, tab_empleados.getValor(i, "solidario") ));
					linea+=  "NO,";
					linea+=  "NO,";
					linea+= tab_empleados.getValor(i, "tipidret") + ",";
					linea+= tab_empleados.getValor(i, "idret") + ",";
					linea+= tab_empleados.getValor(i, "apellidotrab") + ",";
					linea+= tab_empleados.getValor(i, "nombretrab") + ",";
					linea+= tab_empleados.getValor(i, "estab") + ",";
					linea+= tab_empleados.getValor(i, "residenciatrab") + ",";
					linea+= tab_empleados.getValor(i, "pasisresidencia") + ",";
					linea+= tab_empleados.getValor(i, "aplicaconvenio")+ ",";
					linea+= tab_empleados.getValor(i, "tipotrabajodiscap") + ",";
					linea+= tab_empleados.getValor(i, "porcentajediscap") + ",";
					linea+= tab_empleados.getValor(i, "tipiddiscap") + ",";
					linea+= tab_empleados.getValor(i, "iddiscap") + ",";
					linea+= tab_empleados.getValor(i, "suelsal") + ",";
					linea+= tab_empleados.getValor(i, "sobsuelcomremu") + ",";
					linea+= tab_empleados.getValor(i, "partutil") + ",";
					linea+= tab_empleados.getValor(i, "intGrabGen") + ",";
					linea+= tab_empleados.getValor(i, "imprentempl") + ",";
					linea+= tab_empleados.getValor(i, "decimotercer") + ",";
					linea+= tab_empleados.getValor(i, "decimocuarto") + ",";
					linea+= tab_empleados.getValor(i, "fondoreserva") + ",";
					linea+= tab_empleados.getValor(i, "salariodigno") + ",";
					linea+= tab_empleados.getValor(i, "otrosingrengrav") + ",";
					linea+= tab_empleados.getValor(i, "inggravconesteempl") + ",";
					linea+= tab_empleados.getValor(i, "sissalnet") + ",";
					linea+= tab_empleados.getValor(i, "apoperiess") + ",";
					linea+= tab_empleados.getValor(i, "aporperiessconotrosempls") + ",";
					linea+= tab_empleados.getValor(i, "vivienda") + ",";
					linea+= tab_empleados.getValor(i, "salud") + ",";
					linea+= tab_empleados.getValor(i, "educacion") + ",";
					linea+= tab_empleados.getValor(i, "alimentacion") + ",";
					linea+= tab_empleados.getValor(i, "vestimenta") + ",";
					linea+= tab_empleados.getValor(i, "descuentoDiscapacidad")+ ",";
					linea+= tab_empleados.getValor(i, "descuentoTercera") + ",";
					linea+= tab_empleados.getValor(i, "basimp") + ",";
					linea+= tab_empleados.getValor(i, "valretasuotrosempls") + ",";
					linea+= tab_empleados.getValor(i, "valimpasuesteempl") + ",";
					linea+= tab_empleados.getValor(i, "valret") + ",";
					linea+= tab_empleados.getValor(i, "impuestocausado");
						linea+= ","+ tab_empleados.getValor(i, "baseSolidaria")+",";
						linea+= "0" +",";
						linea+= "0" +",";
						linea+= tab_empleados.getValor(i, "baseSolidaria") +",";
						linea+= tab_empleados.getValor(i, "numMeses") +"," ;
						linea+= "0"+",";
						linea+= tab_empleados.getValor(i, "numMeses") +"," ;			
						linea+= tab_empleados.getValor(i, "promedio") +"," ;
						linea+=tab_empleados.getValor(i, "numMeses")+",";
						linea+= "0" +",";
						linea+=  tab_empleados.getValor(i, "numMeses")+",";
						linea+= tab_empleados.getValor(i, "impuestoSolidario")+",";
						linea+= "0" +",";
						linea+=   "0"+",";
						linea+="0" +",";
						linea+= "0"+",";
						linea+=tab_empleados.getValor(i, "impuestoSolidario")+",";
						linea+=  "0" +",";
						linea+=  "0"+",";
						linea+="0"+",";
						linea+= tab_empleados.getValor(i, "solidario") ;
					
					escribir.newLine();
					escribir.write(linea);
					
					linea="";
					linea+= lis_sql.get(0) + ",";
					linea+= anioHasta + ",";
					
				}
				
				escribir.close();
				
				///ESCRIBE EL DOCUMENTO
				Source source = new DOMSource(doc_rdep);
				
				//System.out.println(master);
				String nombre = "Rdep" + anioHasta + ".xml";
				Result result = new StreamResult(new java.io.File(master + "/" + nombre)); //nombre del archivo
				
				//Result console = new StreamResult(System.out);
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.transform(source, result);
				//transformer.transform(source, console);
				
				//String pathXml= master + "/" + nombre;
				//utilitario.crearArchivoZIP(new File[]{archivotxt}, fileName.concat(".zip"));
				//return master + "/" + nombre;
				File pathXml=new File(path+nombre);
				return new File[]{archivotxt,pathXml};
			}
		} catch (Exception e) {
			utilitario.agregarMensajeError("No se pudo generar el Anexo", "No hay información para generar el anexo :"+e.getMessage());
		}
return null;
	}

	private Element crearElemento(String nombre, String[] atributos, String texto) {
		Element elemento = doc_rdep.createElement(nombre);
		if (atributos != null) {
			for (int i = 0; i < atributos.length; i++) {
				elemento.setAttribute(atributos[0], atributos[1]);
			}
		}
		if (texto == null) {
			texto = "";
		}
		elemento.appendChild(doc_rdep.createTextNode(texto));
		return elemento;
	}
	private Element crearElementoCDATA(String nombre, String[] atributos, String texto) {
		Element elemento = doc_rdep.createElement(nombre);
		if (atributos != null) {
			for (int i = 0; i < atributos.length; i++) {
				elemento.setAttribute(atributos[0], atributos[1]);
			}
		}
		elemento.appendChild(doc_rdep.createCDATASection(texto));
		return elemento;
	}

	public TablaGenerica getFormulario107(String IDE_GTEMP,String IDE_SRIMR,String fecha_entrega){
		TablaGenerica tab_formulario=utilitario.consultar("select '' as numero,	'' as p199_firma,	'' as p105_firma,	'' as p199,	'' as p349,	'' as p407,	'' as p405,	'' as p403,	'' as p401,	'' as p399,	'' as p381,	'' as p373,	'' as p371,	'' as p369,	'' as p367,	'' as p365,	'' as p363,	'' as p361,	'' as p353,	'' as p351,	'' as p317,	'' as p315,	'' as p313,	'' as p311,	'' as p307,	'' as p305,	'' as p303,	'' as p301,	'' as p202,	'' as p201,	'' as p106,	'' as p105,	'' as p103,	'' as p102 from dual");
		String empleados[]=IDE_GTEMP.split(",");					
		for (int i = 0; i < empleados.length; i++) {
			String numero=utilitario.getAnio(fecha_entrega)+"-"+utilitario.getMes(fecha_entrega)+"-"+(i+1);		
			IDE_GTEMP = empleados[i];		
			TablaGenerica tab_imp_renta=getImpuestoRenta(IDE_SRIMR);
			tab_formulario.insertar();
			tab_formulario.setValor("numero",numero);
			//102 Ejercicio Fiscal
			String str_anio= String.valueOf(utilitario.getAnio(tab_imp_renta.getValor("FECHA_INICIO_SRIMR")));
			tab_formulario.setValor("p102",str_anio);
			//Fecha Entrega
			tab_formulario.setValor("p103", fecha_entrega);
			TablaGenerica tab_datos_sri=getDatosConfiguracionSRI(str_anio);
			if(tab_datos_sri!=null || !tab_datos_sri.isEmpty()){
				//RUC agente de retencion
				tab_formulario.setValor("p105", tab_datos_sri.getValor("DOCUMENTO_REPRE_SRCOG"));
				tab_formulario.setValor("p105_firma", tab_datos_sri.getValor("PATH_FIRMA_REPRE_SRCOG"));
				//RAZON SOCIAL O NOMBRES Y APELLIDOS COMPLETOS
				tab_formulario.setValor("p106", tab_datos_sri.getValor("RAZON_SOCIAL_SRCOG"));			
				//RUC agente de retencion
				tab_formulario.setValor("p199", tab_datos_sri.getValor("DOCUMENTO_CONTA_SRCOG"));
				tab_formulario.setValor("p199_firma", tab_datos_sri.getValor("PATH_FIRMA_CONTA_SRCOG"));
				TablaGenerica tab_empleado= serv_empleado.getEmpleado(IDE_GTEMP);
				//CEDULA EMPLEADO			
				tab_formulario.setValor("p201", tab_empleado.getValor("DOCUMENTO_IDENTIDAD_GTEMP"));
				//NOMBRES EMPLEADO			
				tab_formulario.setValor("p202", new StringBuilder(tab_empleado.getValor("APELLIDO_PATERNO_GTEMP")==null?"":tab_empleado.getValor("APELLIDO_PATERNO_GTEMP")).append(" ").
						append((tab_empleado.getValor("APELLIDO_MATERNO_GTEMP")==null?"":tab_empleado.getValor("APELLIDO_MATERNO_GTEMP"))).append(" ")
						.append((tab_empleado.getValor("PRIMER_NOMBRE_GTEMP")==null?"":tab_empleado.getValor("PRIMER_NOMBRE_GTEMP"))).append(" ").
						append(((tab_empleado.getValor("SEGUNDO_NOMBRE_GTEMP")==null?"":tab_empleado.getValor("SEGUNDO_NOMBRE_GTEMP")))).toString());

				TablaGenerica tab_anio= utilitario.consultar("SELECT * FROM GEN_ANIO WHERE DETALLE_GEANI='"+str_anio+"'");
				String str_ide_anio=tab_anio.getValor("IDE_GEANI");


				//LIQUIDACIÓN IMPUESTO
				//301 SUELDOS Y SALARIOS  (RUBRO 24 ) sueldos  sri_sueldos 
				tab_formulario.setValor("p301", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_301_sueldos_y_salarios"), str_ide_anio));
				//303 SOBRESUELDOS, COMISIONES, BONOS Y OTROS INGRESOS GRAVADOS (RUBRO 27) subrogaciones
				tab_formulario.setValor("p303", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_303_subrogaciones"), str_ide_anio));
				//305 PARTICIPACIÓN UTILIDADES (RUBRO 76)
				tab_formulario.setValor("p305", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_305_participacion_utilidades"), str_ide_anio));
				//307 INGRESOS GRAVApDOS GENERADOS CON OTROS EMPLEADORES (RUBRO 77)
				tab_formulario.setValor("p307", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_307_ing_grav_otro_emp"), str_ide_anio));
				//311 DECIMO TERCER SUELDO (RUBRO 125)  provisiones
				tab_formulario.setValor("p311", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_311_provisiones_decimo_tercero"), str_ide_anio));
				//313 DECIMO CUARTO SUELDO (RUBRO 121)  provisiones
				tab_formulario.setValor("p313", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_313_provisiones_decimo_cuarto"), str_ide_anio));
				//315 FONDO DE RESERVA (RUBRO 120)  provisiones
				tab_formulario.setValor("p315", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_315_provisiones_fondo_reserva"), str_ide_anio));
				//317 OTROS INGRESOS EN RELACION DE DEPENDENCIA QUE NO CONSTITUYEN RENTA GRAVADA 
				tab_formulario.setValor("p317",getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_317_otros_ing_rel_depencia"), str_ide_anio));			
				//351 (-) APORTE PERSONAL IESS CON ESTE EMPLEADOR
				tab_formulario.setValor("p351",getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_351_aporte_personal"), str_ide_anio));
				//353 (-) APORTE PERSONAL IESS CON OTROS EMPLEADORES
				tab_formulario.setValor("p353",getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_353_aporte_personal_otros_empl"), str_ide_anio));

				TablaGenerica tab_deducibles=getDeducibles(IDE_SRIMR);
				for (int j = 0; j < tab_deducibles.getTotalFilas() ; j++) {
					if(tab_deducibles.getValor(j, "ALTERNO_SRI_SRDED")!=null){					
						tab_formulario.setValor("p"+tab_deducibles.getValor(j, "ALTERNO_SRI_SRDED"),getValorDeducibleEmpleado(IDE_GTEMP, tab_deducibles.getValor(j, "IDE_SRDED")));
					}				
				}

				//371 (-) EXONERACIÓN POR DISCAPACIDAD
				tab_formulario.setValor("p371",getExoneracionDiscapacidad(IDE_GTEMP, IDE_SRIMR));

				//373 (-) EXONERACIÓN POR TERCERA EDAD
				tab_formulario.setValor("p373",getExoneracionTerceraEdad(IDE_GTEMP, IDE_SRIMR));

				//381 IMPUESTO A LA RENTA ASUMIDO POR ESTE EMPLEADOR
				tab_formulario.setValor("p381", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_381_imp_renta_empleador"), str_ide_anio));

				//403 VALOR DEL IMPUESTO RETENIDO Y ASUMIDO POR OTROS EMPL. DURANTE EL PERIODO DECLARADO
				tab_formulario.setValor("p403", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_403_impuesto_retenido"), str_ide_anio));
				//405 VALOR DEL IMPUESTO ASUMIDO POR ESTE EMPLEADOR
				tab_formulario.setValor("p405", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_405_impuesto_asumido"), str_ide_anio));
				//407 VALOR DEL IMPUESTO RETENIDO AL TRABAJADOR POR ESTE EMPLEADOR
				tab_formulario.setValor("p407",getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_407_imp_retenido_trabajador"), str_ide_anio));
				//349 INGRESOS GRAVADOS CON ESTE EMPLEADOR (301+303+305+381)
				double v301=0,v303=0,v305=0,v307=0,v351=0,v353=0,v361=0,v363=0,v365=0,v367=0,v369=0,v371=0,v373=0,v381=0;
				try {
					v301=Double.parseDouble(tab_formulario.getValor("p301"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v303=Double.parseDouble(tab_formulario.getValor("p303"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v305=Double.parseDouble(tab_formulario.getValor("p305"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v307=Double.parseDouble(tab_formulario.getValor("p307"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v351=Double.parseDouble(tab_formulario.getValor("p351"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v353=Double.parseDouble(tab_formulario.getValor("p353"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v361=Double.parseDouble(tab_formulario.getValor("p361"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v363=Double.parseDouble(tab_formulario.getValor("p363"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v365=Double.parseDouble(tab_formulario.getValor("p365"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v367=Double.parseDouble(tab_formulario.getValor("p367"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v369=Double.parseDouble(tab_formulario.getValor("p369"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v371=Double.parseDouble(tab_formulario.getValor("p371"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v373=Double.parseDouble(tab_formulario.getValor("p373"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v381=Double.parseDouble(tab_formulario.getValor("p381"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}

				double base=v301+v303+v305+v307-v351-v353-v361-v363-v365-v367-v369-v371-v373+v381;
				String str_base="---";
				if(base>=0){
					str_base=utilitario.getFormatoNumero(base);
				}

				//399 BASE IMPONIBLE GRAVADA 301+303+305+307-351-353-361-363-365-367-369-371-373+381
				tab_formulario.setValor("p399", str_base);

				//401 IMPUESTO A LA RENTA CAUSADO
				try {
					tab_formulario.setValor("p401", String.valueOf(getValorImpuestoRentaCausado(IDE_SRIMR, Double.parseDouble(tab_formulario.getValor("p399")))));

				} catch (Exception e) {
					tab_formulario.setValor("p401", "---");

				}
				
				//349 INGRESOS GRAVADOS CON ESTE EMPLEADOR (informativo) 301+303+305+381
				tab_formulario.setValor("p349", utilitario.getFormatoNumero((v301+v303+v305+v381)));
			}
			else{
				System.out.println("NO EXISTEN CONFIGURACIONES DE SRI PARA EL PERIODO SELECCIONADO :"+str_anio);
				return null;
			}

		}
		return tab_formulario;
	}


	/**
	 * Busca la fraccion basica exenta de un impuesto a la renta
	 * @param IDE_SRIMR
	 * @return
	 */
	public double getFraccionBasicaExenta(String IDE_SRIMR){

		TablaGenerica tab_fraccion=utilitario.consultar("select * from SRI_DETALLE_IMPUESTO_RENTA WHERE IDE_SRIMR="+IDE_SRIMR+" AND FRACCION_BASICA_SRDIR=0");
		if(tab_fraccion.isEmpty()){
			try {
				return Double.parseDouble(tab_fraccion.getValor("EXCESO_HASTA_SRDIR"));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return 0;
	}


	/**
	 * Calcula la exoneracion por discapacidad >=30% 
	 * @param IDE_GTEMP
	 * @return
	 */
	public String getExoneracionDiscapacidad(String IDE_GTEMP,String IDE_SRIMR){
		double dou_porcentaje=serv_empleado.getPorcentajeDiscapacidad(IDE_GTEMP);
		if(dou_porcentaje>=30){
			//doble de la fraccion basica excenta
			return utilitario.getFormatoNumero(getFraccionBasicaExenta(IDE_SRIMR)*2);
		}		
		return "---";
	}


	/**
	 * Calcula la exoneracion por tercera edad >=65 años 
	 * @param IDE_GTEMP
	 * @return
	 */
	public String getExoneracionTerceraEdad(String IDE_GTEMP,String IDE_SRIMR){		
		if(serv_empleado.isTerceraEdad(IDE_GTEMP)){
			//doble de la fraccion basica excenta
			return utilitario.getFormatoNumero(getFraccionBasicaExenta(IDE_SRIMR)*2);
		}		
		return "---";
	}



	public String getValorImpuestoRentaCausado(String IDE_SRIMR,double base_imponible){
		TablaGenerica tab_det_imp_renta=utilitario.consultar("select * from SRI_DETALLE_IMPUESTO_RENTA where IDE_SRIMR="+IDE_SRIMR+" " +
				"and "+base_imponible+" BETWEEN FRACCION_BASICA_SRDIR and EXCESO_HASTA_SRDIR");
		double dou_imp_renta=0;
		if(!tab_det_imp_renta.isEmpty()){
			double dou_fraccion_basica=0;
			try {
				dou_fraccion_basica=Double.parseDouble(tab_det_imp_renta.getValor("FRACCION_BASICA_SRDIR"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			double dou_por_frac_exc=0;
			try {
				dou_por_frac_exc=Double.parseDouble(tab_det_imp_renta.getValor("IMP_FRACCION_EXCEDENTE_SRDIR"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			double dou_imp_fraccion=0;
			try {
				dou_imp_fraccion=Double.parseDouble(tab_det_imp_renta.getValor("IMP_FRACCION_SRDIR"));

			} catch (Exception e) {
				// TODO: handle exception
			}			
			double dou_excedente=base_imponible-dou_fraccion_basica;			
			double dou_imp_exc=(dou_excedente*dou_por_frac_exc)/100;			
			dou_imp_renta=dou_imp_exc+dou_imp_fraccion;

		}
		return utilitario.getFormatoNumero(dou_imp_renta); 
	}


	/**
	 * Retorna el valor de un deducible de un empleado
	 * @param IDE_GTEMP
	 * @param IDE_SRDED
	 * @return
	 */
	public String getValorDeducibleEmpleado(String IDE_GTEMP,String IDE_SRDED){		
		TablaGenerica tab_deduci=utilitario.consultar("SELECT * FROM SRI_DEDUCIBLES_EMPLEADO WHERE IDE_GTEMP="+IDE_GTEMP+" AND IDE_SRDED="+IDE_SRDED);
		try {
			if(!tab_deduci.isEmpty()){
				if(tab_deduci.getValor("VALOR_DEDUCIBLE_SRDEE")!=null){
					return utilitario.getFormatoNumero(Double.parseDouble(tab_deduci.getValor("VALOR_DEDUCIBLE_SRDEE")));	
				}				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "---";
	}


	/**
	 * Retorna los deducible para el impuesto a la renta de un determinado periodo
	 * @param IDE_SRIMR
	 * @return
	 */
	public TablaGenerica getDeducibles(String IDE_SRIMR){		
		return utilitario.consultar("SELECT * FROM SRI_DEDUCIBLES WHERE IDE_SRIMR="+IDE_SRIMR+" AND ACTIVO_SRDED=true");
	}


	/**
	 * Calcula la suma de un rubro de un empledo en un anio determinado
	 * @param IDE_GTEMP
	 * @param IDE_NRRUB
	 * @return
	 */
	public String getSumaRubro(String IDE_GTEMP,String IDE_NRRUB,String anio){		
		if(anio!=null){			
			List lis=utilitario.getConexion().consultar("SELECT SUM(DETAROL.VALOR_NRDRO) AS SUMA_RUBRO FROM NRH_DETALLE_ROL detarol INNER JOIN NRH_ROL rol on DETAROL.IDE_NRROL = ROL.IDE_NRROL INNER JOIN GEN_PERIDO_ROL peri ON  ROL.IDE_GEPRO=peri.IDE_GEPRO INNER JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP INNER JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER INNER JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB WHERE IDE_GEANI="+anio+" AND EMPLE.IDE_GTEMP="+IDE_GTEMP+" and detrubro.IDE_NRRUB in("+IDE_NRRUB+") GROUP BY detrubro.IDE_NRRUB");
			if(!lis.isEmpty() && lis.get(0)!=null){
				return utilitario.getFormatoNumero(Double.parseDouble(String.valueOf(lis.get(0))));
			}
		}

		return "---";
	}


	/**
	 * Retorna la cabecera del  impuesto a la renta
	 * @param IDE_SRIMR
	 * @return
	 */
	public TablaGenerica getImpuestoRenta(String IDE_SRIMR){			
		return utilitario.consultar("SELECT * FROM SRI_IMPUESTO_RENTA WHERE IDE_SRIMR=".concat(IDE_SRIMR));
	}

	/**
	 * Retorna la configuracion de datos del sri en un anio determinado
	 * @param anio
	 * @return
	 */
	public TablaGenerica getDatosConfiguracionSRI(String anio){
		TablaGenerica tab_anio= utilitario.consultar("SELECT * FROM GEN_ANIO WHERE DETALLE_GEANI='"+anio+"'");
		if(!tab_anio.isEmpty()){
			return utilitario.consultar("SELECT * FROM SRI_CONFIGURACION_GENERAL WHERE IDE_GEANI="+tab_anio.getValor("IDE_GEANI"));
		}
		return null;
	}
	
	////////GENERACION DEL ANEXO TRANSACCIONAL - ALeX BeCERrA
	public String getAtsInformante(String anio, String mes, String totalVentas, String id_emp){
	
		if(mes.length()<2)
			mes="0"+mes;
		
		String sql="select 'R' as \"Tipo Id Informante\", identificacion_empr as \"Identificacion Informante\", "
					+" nom_empr as \"Razón Social\", (select detalle_geani from gen_anio where ide_geani="+anio+") as \"Año\", '"+mes+"' as \"Mes\", '006' as \"Numero Establecimientos\", "
					+" '"+totalVentas+"' as \"Total Ventas\", 'IVA' as \"Código Operativo\""
					+" from sis_empresa where ide_empr="+id_emp+" ;";

		return sql;
	}
	

	public String getAtsDetalleCompras(String anio,String mes){
		
		/*String tab_detalle_compras="select a.ide_adfac as codigo_compra, coalesce(codigo_srsuc,'02') as sustento, tdi.codigo_sbs_gttdi as identificacion_proveedor,ruc_tepro,case when tdi.codigo_sbs_gttdi='02' then '03' else '01' end as tipo_comprobante, nombre_tepro as razon_social_prov"
				+" ,case when tdi.codigo_sbs_gttdi='03' then codigo_ats_retic else '' end as tipo_proveedor,'NO' as parte_relacionada,fecha_factura_adfac as fecha_registro, substring(num_factura_adfac from 1 for 3) as establecimiento,"
				+" substring (num_factura_adfac from 5 for 3) as punto_emision, substring(num_factura_adfac from 9 for 20) as secuencial,fecha_factura_adfac as fecha_emision,"
				+" nro_autorizacion_sri_adq,0 as base_imp_no_objeto_iva,base_noiva_adfac as base_imponible_tarifa_cero_iva,base_iva_adfac as base_imponible_gravada, 0 as base_excenta, 0 as monto_ice,"
				+" valor_iva_adfac as monto_iva,(case when retencion10 is null then 0 else retencion10 end) as retencion10"
				+" ,(case when retencion20 is null then 0 else retencion20 end) as retencion20 "
				+" ,(case when retencion30 is null then 0 else retencion30 end) as retencion30 "
				+" ,(case when retencion50 is null then 0 else retencion50 end) as retencion50 "
				+" ,(case when retencion70 is null then 0 else retencion70 end) as retencion70 "
				+" ,(case when retencion100 is null then 0 else retencion100 end) as retencion100  "
				+" ,(base_noiva_adfac+base_iva_adfac) as total_bases_imponibles,'01' as pago_local_exterior,'NA' as pais_paga "
				+" ,'NA' as regimen_fiscal,'NA' as aplica_convenio, 'NA' as pago_exterior_noramtiva,substring(num_retencion_teret from 1 for 3) as establecimiento_rte "
				+" ,substring(num_retencion_teret from 5 for 3) as punto_emision_rte,substring(num_retencion_teret from 9 for 9) as secuencial_rte,fecha_teret,nro_autorizacion_sri_ret "
				+"  from adq_factura a "
				+"  left join tes_proveedor b on a.ide_tepro=b.ide_tepro"
				+"  left join rec_tipo_contribuyente c on b.ide_retic =c.ide_retic"
				+"  left join gth_tipo_documento_identidad tdi on tdi.ide_gttdi = b.ide_gttdi"
				+"  left join sri_sustento_comprobante ssc on ssc.ide_srsuc = a.ide_srsuc"
				+"  left join (select a.ide_adfac,ide_teimp,sum(valor_retenido_teder)as retencion10 from tes_retencion a,tes_detalle_retencion b"
				+"  where a.ide_teret =b.ide_teret and ide_teimp in (select unnest(string_to_array(cast(valor_para as character varying(20)),',')::integer[]) as valor from sis_parametros where nom_para='p_retencion_bienes_10' and a.ide_coest=2) group by  a.ide_adfac,ide_teimp) d on a.ide_adfac = d.ide_adfac"
				+"  left join (select a.ide_adfac,ide_teimp,sum(valor_retenido_teder)as retencion20 from tes_retencion a,tes_detalle_retencion b"
				+"  where a.ide_teret =b.ide_teret and ide_teimp in (select unnest(string_to_array(cast(valor_para as character varying(20)),',')::integer[]) as valor from sis_parametros where nom_para='p_retencion_servicios_20' and a.ide_coest=2) group by  a.ide_adfac,ide_teimp) e on a.ide_adfac = e.ide_adfac"
				+"  left join (select a.ide_adfac,ide_teimp,sum(valor_retenido_teder)as retencion30 from tes_retencion a,tes_detalle_retencion b"
			  //+"  where a.ide_teret =b.ide_teret and ide_teimp in (select cast(valor_para as integer) as valor from sis_parametros where nom_para='p_retencion_bienes_30' and a.ide_coest=2) group by  a.ide_adfac,ide_teimp) f on a.ide_adfac = f.ide_adfac"
				+"  where a.ide_teret =b.ide_teret and ide_teimp in (select unnest(string_to_array(cast(valor_para as character varying(20)),',')::integer[]) as valor from sis_parametros where nom_para='p_retencion_bienes_30' and a.ide_coest=2) group by  a.ide_adfac,ide_teimp) f on a.ide_adfac = f.ide_adfac"
				+"  left join (select a.ide_adfac,ide_teimp,sum(valor_retenido_teder)as retencion70 from tes_retencion a,tes_detalle_retencion b"
				+"  where a.ide_teret =b.ide_teret and ide_teimp in (select unnest(string_to_array(cast(valor_para as character varying(20)),',')::integer[]) as valor from sis_parametros where nom_para='p_retencion_servicios_70' and a.ide_coest=2) group by  a.ide_adfac,ide_teimp) g on a.ide_adfac = g.ide_adfac"
				+"  left join (select a.ide_adfac,ide_teimp,sum(valor_retenido_teder)as retencion50 from tes_retencion a,tes_detalle_retencion b"
				+"  where a.ide_teret =b.ide_teret and ide_teimp in (select unnest(string_to_array(cast(valor_para as character varying(20)),',')::integer[]) as valor from sis_parametros where nom_para='p_retencion_iva_50' and a.ide_coest=2) group by  a.ide_adfac,ide_teimp) iv5 on a.ide_adfac = iv5.ide_adfac"
				+"  left join (select a.ide_adfac,ide_teimp,sum(valor_retenido_teder)as retencion100 from tes_retencion a,tes_detalle_retencion b"
				+"  where a.ide_teret =b.ide_teret and ide_teimp in (select unnest(string_to_array(cast(valor_para as character varying(20)),',')::integer[]) as valor from sis_parametros where nom_para='p_retencion_iva_100' and a.ide_coest=2) group by  a.ide_adfac,ide_teimp) h on a.ide_adfac = h.ide_adfac"
				+"  left join (select ide_adfac,fecha_teret,num_retencion_teret,nro_autorizacion_sri_ret from tes_retencion where ide_coest=2 ) i on a.ide_adfac = i.ide_adfac"
				+" where extract(year from fecha_factura_adfac)="+anio+" and extract(month from fecha_factura_adfac)= "+mes+" ;";*/
		
		String tab_detalle_compras="select a.ide_adfac as codigo_compra, coalesce(codigo_srsuc,'02') as sustento, tdi.codigo_sbs_gttdi as identificacion_proveedor,ruc_tepro,case when tdi.codigo_sbs_gttdi='02' then '03' else '01' end as tipo_comprobante, nombre_tepro as razon_social_prov " +
				" ,case when tdi.codigo_sbs_gttdi='03' then codigo_ats_retic else '' end as tipo_proveedor,'NO' as parte_relacionada,fecha_factura_adfac as fecha_registro, substring(num_factura_adfac from 1 for 3) as establecimiento, " +
				" substring (num_factura_adfac from 5 for 3) as punto_emision, substring(num_factura_adfac from 9 for 20) as secuencial,fecha_factura_adfac as fecha_emision, " +
				" nro_autorizacion_sri_adq,0 as base_imp_no_objeto_iva,base_noiva_adfac as base_imponible_tarifa_cero_iva,base_iva_adfac as base_imponible_gravada, 0 as base_excenta, 0 as monto_ice, " +
				" valor_iva_adfac as monto_iva,(case when retencion10 is null then 0 else retencion10 end) as retencion10 " +
				" ,(case when retencion20 is null then 0 else retencion20 end) as retencion20  " +
				" ,(case when retencion30 is null then 0 else retencion30 end) as retencion30  " +
				" ,(case when retencion50 is null then 0 else retencion50 end) as retencion50  " +
				" ,(case when retencion70 is null then 0 else retencion70 end) as retencion70  " +
				" ,(case when retencion100 is null then 0 else retencion100 end) as retencion100   " +
				" ,(base_noiva_adfac+base_iva_adfac) as total_bases_imponibles,'01' as pago_local_exterior,'NA' as pais_paga  " +
				" ,'NA' as regimen_fiscal,'NA' as aplica_convenio, 'NA' as pago_exterior_noramtiva,substring(num_retencion_teret from 1 for 3) as establecimiento_rte  " +
				" ,substring(num_retencion_teret from 5 for 3) as punto_emision_rte,substring(num_retencion_teret from 9 for 9) as secuencial_rte,fecha_teret,nro_autorizacion_sri_ret  " +
				",(case when ide_adncr is null then null else '01' end) as codigo_doc_nc, substring(num_nota_credito_adncr from 1 for 3) as establecimiento_nc  " +
				",substring(num_nota_credito_adncr from 5 for 3) as punto_emision_nc,substring(num_nota_credito_adncr from 9 for 9) as secuencial_nc " +
				" ,nro_autorizacion_sri_adncr " +
				"  from adq_factura a  " +
				"  left join tes_proveedor b on a.ide_tepro=b.ide_tepro " +
				"  left join rec_tipo_contribuyente c on b.ide_retic =c.ide_retic " +
				"  left join gth_tipo_documento_identidad tdi on tdi.ide_gttdi = b.ide_gttdi " +
				"  left join sri_sustento_comprobante ssc on ssc.ide_srsuc = a.ide_srsuc " +
				"  left join (select a.ide_adfac,ide_teimp,sum(valor_retenido_teder)as retencion10 from tes_retencion a,tes_detalle_retencion b " +
				"  where a.ide_teret =b.ide_teret and ide_teimp in (select unnest(string_to_array(cast(valor_para as character varying(20)),',')::integer[]) as valor from sis_parametros where nom_para='p_retencion_bienes_10' and a.ide_coest=2) group by  a.ide_adfac,ide_teimp) d on a.ide_adfac = d.ide_adfac " +
				"  left join (select a.ide_adfac,ide_teimp,sum(valor_retenido_teder)as retencion20 from tes_retencion a,tes_detalle_retencion b " +
				"  where a.ide_teret =b.ide_teret and ide_teimp in (select unnest(string_to_array(cast(valor_para as character varying(20)),',')::integer[]) as valor from sis_parametros where nom_para='p_retencion_servicios_20' and a.ide_coest=2) group by  a.ide_adfac,ide_teimp) e on a.ide_adfac = e.ide_adfac " +
				"  left join (select a.ide_adfac,ide_teimp,sum(valor_retenido_teder)as retencion30 from tes_retencion a,tes_detalle_retencion b " +
				" " +
				"  where a.ide_teret =b.ide_teret and ide_teimp in (select unnest(string_to_array(cast(valor_para as character varying(20)),',')::integer[]) as valor from sis_parametros where nom_para='p_retencion_bienes_30' and a.ide_coest=2) group by  a.ide_adfac,ide_teimp) f on a.ide_adfac = f.ide_adfac " +
				"  left join (select a.ide_adfac,ide_teimp,sum(valor_retenido_teder)as retencion70 from tes_retencion a,tes_detalle_retencion b " +
				"  where a.ide_teret =b.ide_teret and ide_teimp in (select unnest(string_to_array(cast(valor_para as character varying(20)),',')::integer[]) as valor from sis_parametros where nom_para='p_retencion_servicios_70' and a.ide_coest=2) group by  a.ide_adfac,ide_teimp) g on a.ide_adfac = g.ide_adfac " +
				"  left join (select a.ide_adfac,ide_teimp,sum(valor_retenido_teder)as retencion50 from tes_retencion a,tes_detalle_retencion b " +
				"  where a.ide_teret =b.ide_teret and ide_teimp in (select unnest(string_to_array(cast(valor_para as character varying(20)),',')::integer[]) as valor from sis_parametros where nom_para='p_retencion_iva_50' and a.ide_coest=2) group by  a.ide_adfac,ide_teimp) iv5 on a.ide_adfac = iv5.ide_adfac " +
				"  left join (select a.ide_adfac,ide_teimp,sum(valor_retenido_teder)as retencion100 from tes_retencion a,tes_detalle_retencion b " +
				"  where a.ide_teret =b.ide_teret and ide_teimp in (select unnest(string_to_array(cast(valor_para as character varying(20)),',')::integer[]) as valor from sis_parametros where nom_para='p_retencion_iva_100' and a.ide_coest=2) group by  a.ide_adfac,ide_teimp) h on a.ide_adfac = h.ide_adfac " +
				"  left join (select ide_adfac,fecha_teret,num_retencion_teret,nro_autorizacion_sri_ret from tes_retencion where ide_coest=2 ) i on a.ide_adfac = i.ide_adfac " +
				" " +
				"  left join adq_nota_credito nc on nc.ide_adfac=a.ide_adfac " +
				" " +
				" where extract(year from fecha_factura_adfac)="+anio+" and extract(month from fecha_factura_adfac)= "+mes+" ;";
		
		System.out.println("tab_detalle_compras "+tab_detalle_compras);
		return tab_detalle_compras;
	}
	
	public String getAtsComprasFormaPago(String anio,String mes){
		String sql="select ide_adfac as codigo_compra,'20' as forma_pago from adq_factura "
				+" where total_adfac>1000 and extract(year from fecha_factura_adfac)="+anio+" and extract(month from fecha_factura_adfac)= "+mes+" ;";
		return sql;
	}
	
	public String getAtsComprasRetencion(String anio,String mes){
		String sql="select adq.ide_adfac as codigo_compra, codigo_teimp as codigo_retencion, base_imponible_teder as base_imponible "
					+", porcentaje_teimp as porcentaje_retencion, valor_retenido_teder as valor_retenido  from adq_factura adq "
					+" join tes_retencion ret on ret.ide_adfac=adq.ide_adfac "
					+" join tes_detalle_retencion dret on dret.ide_teret=ret.ide_teret "
					+" join tes_impuesto imp on imp.ide_teimp=dret.ide_teimp "
				+" where ret.ide_coest=2 and ide_tetii=1 and extract(year from adq.fecha_factura_adfac)="+anio+" and extract(month from adq.fecha_factura_adfac)= "+mes+" ;";
		return sql;
	}
	
	public String getAtsVentasCliente(String anio,String mes){
        /*String sql="select 'FACTURA' as doc, tdi.codigo_sri_gttdi as tipo_identificacion, ruc_comercial_recli as identificacion_cliente, case when tdi.codigo_sri_gttdi = '07' then '' else 'NO'  end as es_parte_relacionada,  " +
			" '18' as codigo_tipo_comprobate, count(fac.ide_fafac) as total_comp_emitidos, sum(0) as base_imp_nobjeto, sum(0) as base_imp_tarf0 , sum(fac.base_aprobada_fafac) as base_imp_tarfiva,  " +
			" sum(fac.valor_iva_fafac) as valor_iva  " +
			" from rec_clientes cli left join gth_tipo_documento_identidad tdi on tdi.ide_gttdi=cli.ide_gttdi  " +
			" left join fac_factura fac on fac.ide_recli=cli.ide_recli   " +
			" where fac.ide_coest in (1,2,16,24) and ((fac.ide_tetid=4 and coalesce(autorizada_sri_fafac,false)=true) or (fac.ide_tetid=3)) and extract(year from fecha_transaccion_fafac)="+anio+" and extract(month from fecha_transaccion_fafac)= "+mes+
			" group by tipo_identificacion, identificacion_cliente, es_parte_relacionada,codigo_tipo_comprobate  " +
			" union " +
			" select 'NOTA CREDITO' as doc, tdi.codigo_sri_gttdi as tipo_identificacion, ruc_comercial_recli as identificacion_cliente, case when tdi.codigo_sri_gttdi = '07' then '' else 'NO'  end as es_parte_relacionada,  " +
			" '04' as codigo_tipo_comprobate, count(nc.ide_fanoc) as total_comp_emitidos, sum(0) as base_imp_nobjeto, sum(0) as base_imp_tarf0 , sum(nc.valor_referencial_fanoc) as base_imp_tarfiva,  " +
			" sum(nc.iva_fanoc) as valor_iva  " +
			" from rec_clientes cli left join gth_tipo_documento_identidad tdi on tdi.ide_gttdi=cli.ide_gttdi  " +
			" left join fac_factura fac on fac.ide_recli=cli.ide_recli " +
			" join fac_nota_credito nc on nc.ide_fafac=fac.ide_fafac   " +
			" where nc.ide_coest in (2) and ((substring(nro_nota_credito_fanoc from 5 for 3) like '011' and coalesce(autorizada_sri_fanoc,false)=true) "+
			" or (substring(nro_nota_credito_fanoc from 5 for 3) not like '011' and coalesce(autorizada_sri_fanoc,false)=false)) and extract(year from fecha_fanoc)="+anio+" and extract(month from fecha_fanoc)="+mes+
			" group by tipo_identificacion, identificacion_cliente, es_parte_relacionada,codigo_tipo_comprobate  " +
			" union " +
			" select 'NOTA DEBITO' as doc, tdi.codigo_sri_gttdi as tipo_identificacion, ruc_comercial_recli as identificacion_cliente, case when tdi.codigo_sri_gttdi = '07' then '' else 'NO'  end as es_parte_relacionada,  " +
			" '05' as codigo_tipo_comprobate, count(nd.ide_fanod) as total_comp_emitidos, sum(0) as base_imp_nobjeto, sum(interes_generado_fanod) as base_imp_tarf0 , sum(0) as base_imp_tarfiva,  " +
			" sum(0) as valor_iva  " +
			" from rec_clientes cli left join gth_tipo_documento_identidad tdi on tdi.ide_gttdi=cli.ide_gttdi  " +
			" left join fac_nota_debito nd on nd.ide_recli=cli.ide_recli  " +
			" where nd.ide_coest in (2,16) and autorizada_sri_fanod=true and extract(year from fecha_emision_fanod)="+anio+" and extract(month from fecha_emision_fanod)="+mes+
			" group by tipo_identificacion, identificacion_cliente, es_parte_relacionada,codigo_tipo_comprobate order by 1,3;";*/
		
		String sql="select row_number() over(order by comp.doc,comp.identificacion_cliente) as codigo, comp.* "

				+" from ( "

				+" select doc, tipo_identificacion, identificacion_cliente, razon_social_recli, tipo_cli, es_parte_relacionada, codigo_tipo_comprobate, tipo_emi, forma_pago, "
				+" sum(total_comp_emitidos) as total_comp_emitidos, sum(base_imp_nobjeto) as base_imp_nobjeto, sum(base_imp_tarf0) as base_imp_tarf0, sum(base_imp_tarfiva) as base_imp_tarfiva, sum(valor_iva) as valor_iva"
				+" from ("
				
				
				+" select cast('FACTURA' as character(25)) as doc, tdi.codigo_sri_gttdi as tipo_identificacion, ruc_comercial_recli as identificacion_cliente, "
				+" case when tdi.codigo_sri_gttdi='06' then razon_social_recli else '' end as razon_social_recli,case when tdi.codigo_sri_gttdi='06' then tipo_c.codigo_ats_retic else '' end as tipo_cli," 
				+" case when tdi.codigo_sri_gttdi = '07' then '' else 'NO'  end as es_parte_relacionada, "
				//+" '18' as codigo_tipo_comprobate, case when fac.ide_tetid=4 then 'E' else 'F' end as tipo_emi,fpag.codigo_ats_retip as forma_pago , count(fac.ide_fafac) as total_comp_emitidos, sum(0) as base_imp_nobjeto, sum(0) as base_imp_tarf0 , sum(fac.base_aprobada_fafac) as base_imp_tarfiva, sum(fac.valor_iva_fafac) as valor_iva"
				+" '18' as codigo_tipo_comprobate, case when fac.ide_tetid=4 then 'F' else 'F' end as tipo_emi,cast('01' as character(25)) as forma_pago , count(fac.ide_fafac) as total_comp_emitidos, sum(0) as base_imp_nobjeto, sum(coalesce(base_cero_fafac,0)) as base_imp_tarf0 , sum(fac.base_aprobada_fafac) as base_imp_tarfiva, sum(fac.valor_iva_fafac) as valor_iva"
				+"  from rec_clientes cli left join gth_tipo_documento_identidad tdi on tdi.ide_gttdi=cli.ide_gttdi   "
				+"  left join rec_tipo_contribuyente tipo_c on tipo_c.ide_retic=cli.ide_retic"
				+"  join fac_factura fac on fac.ide_recli=cli.ide_recli  "
				//+"  left join rec_tipo fpag on fpag.ide_retip=fac.ide_retip  "
				+"  where coalesce(activo_fafac,false) in (true,false) and fac.ide_coest in (1,2,16,24,29,30) and ((fac.ide_tetid=4 and coalesce(autorizada_sri_fafac,false)=true) or (fac.ide_tetid=3 and fac.ide_coest not in (1))) and extract(year from fecha_transaccion_fafac)="+anio+" and extract(month from fecha_transaccion_fafac)= "+mes
				+"  group by tipo_identificacion, identificacion_cliente,razon_social_recli,tipo_c.codigo_ats_retic, es_parte_relacionada,codigo_tipo_comprobate, tipo_emi "//", fpag.codigo_ats_retip "
				+"   union all "
				+"  select cast('NOTA CREDITO' as character(25)) as doc, tdi.codigo_sri_gttdi as tipo_identificacion, ruc_comercial_recli as identificacion_cliente,"
				+" case when tdi.codigo_sri_gttdi='06' then razon_social_recli else '' end as razon_social_recli,case when tdi.codigo_sri_gttdi='06' then tipo_c.codigo_ats_retic else '' end as tipo_cli, case when tdi.codigo_sri_gttdi = '07' then '' else 'NO'  end as es_parte_relacionada,"     
				//+" '04' as codigo_tipo_comprobate, 'E' as tipo_emi, '' as forma_pago, count(nc.ide_fanoc) as total_comp_emitidos, sum(0) as base_imp_nobjeto, sum(0) as base_imp_tarf0 , sum(nc.valor_referencial_fanoc) as base_imp_tarfiva, sum(nc.iva_fanoc) as valor_iva "
				+" '04' as codigo_tipo_comprobate, 'F' as tipo_emi, cast('00' as character(25)) as forma_pago , count(nc.ide_fanoc) as total_comp_emitidos, sum(0) as base_imp_nobjeto, sum(0) as base_imp_tarf0 , sum(nc.valor_referencial_fanoc) as base_imp_tarfiva, sum(nc.iva_fanoc) as valor_iva "
				+"  from rec_clientes cli left join gth_tipo_documento_identidad tdi on tdi.ide_gttdi=cli.ide_gttdi   "
				+"  left join rec_tipo_contribuyente tipo_c on tipo_c.ide_retic=cli.ide_retic"
				+"  left join fac_factura fac on fac.ide_recli=cli.ide_recli  "
				+"  join fac_nota_credito nc on nc.ide_fafac=fac.ide_fafac    "
				+"  where nc.ide_coest in (2) and ((substring(nro_nota_credito_fanoc from 5 for 3) like '011' and coalesce(autorizada_sri_fanoc,false)=true) "
				+"  or (substring(nro_nota_credito_fanoc from 5 for 3) not like '011' and coalesce(autorizada_sri_fanoc,false)=false)) and extract(year from fecha_fanoc)="+anio+" and extract(month from fecha_fanoc)="+mes
				+"  group by tipo_identificacion, identificacion_cliente,razon_social_recli,tipo_c.codigo_ats_retic, es_parte_relacionada,codigo_tipo_comprobate, tipo_emi, forma_pago  "
				+"  union all "
				+"  select cast('NOTA DEBITO' as character(25)) as doc, tdi.codigo_sri_gttdi as tipo_identificacion, ruc_comercial_recli as identificacion_cliente,"
				+" case when tdi.codigo_sri_gttdi='06' then razon_social_recli else '' end as razon_social_recli, case when tdi.codigo_sri_gttdi='06' then tipo_c.codigo_ats_retic else '' end as tipo_cli, case when tdi.codigo_sri_gttdi = '07' then '' else 'NO'  end as es_parte_relacionada,"    
				//+" '05' as codigo_tipo_comprobate, 'E' as tipo_emi,fpag.codigo_ats_retip as forma_pago , count(nd.ide_fanod) as total_comp_emitidos, sum(0) as base_imp_nobjeto, sum(interes_generado_fanod) as base_imp_tarf0 , sum(0) as base_imp_tarfiva,  sum(0) as valor_iva "
				+" '05' as codigo_tipo_comprobate, 'F' as tipo_emi,cast('01' as character(25)) as forma_pago , count(nd.ide_fanod) as total_comp_emitidos, sum(0) as base_imp_nobjeto, sum(interes_generado_fanod) as base_imp_tarf0 , sum(0) as base_imp_tarfiva,  sum(0) as valor_iva "
				+"  from rec_clientes cli left join gth_tipo_documento_identidad tdi on tdi.ide_gttdi=cli.ide_gttdi  "
				+"  left join rec_tipo_contribuyente tipo_c on tipo_c.ide_retic=cli.ide_retic "
				+"  join fac_nota_debito nd on nd.ide_recli=cli.ide_recli  "
				+"  left join fac_detalle_debito dnd on dnd.ide_fanod=nd.ide_fanod"
				+"  left join fac_factura fac on fac.ide_fafac=dnd.ide_fafac"
				//+"  left join rec_tipo fpag on fpag.ide_retip=fac.ide_retip "
				+"  where nd.ide_coest in (16) and autorizada_sri_fanod=true and extract(year from fecha_emision_fanod)="+anio+" and extract(month from fecha_emision_fanod)="+mes
				+"  group by tipo_identificacion, identificacion_cliente,razon_social_recli,tipo_c.codigo_ats_retic, es_parte_relacionada,codigo_tipo_comprobate, tipo_emi"//", fpag.codigo_ats_retip  "
				
				+" ) det"

				+" group by doc, tipo_identificacion, identificacion_cliente, razon_social_recli, tipo_cli, es_parte_relacionada, codigo_tipo_comprobate, tipo_emi, forma_pago"

				+"  ) comp"
				+"  order by comp.doc,comp.identificacion_cliente;";
			     
		System.out.println("ventas clientes "+sql);
		return sql;
	}

	public String getAtsVentasEstablecimiento(String anio,String mes){
		
        String sql="select 'FACTURA' as doc, substring(secuencial_fafac from 1 for 3) as codigo_establecimiento,sum(coalesce(base_cero_fafac,0)+base_aprobada_fafac) as ventas_establecimiento  " +
					" from fac_factura fac   " +
					" where coalesce(activo_fafac,false) in (true,false) and fac.ide_coest in (1,2,16,24,29,30) and extract(year from fecha_transaccion_fafac)="+anio+
					" and ((fac.ide_tetid=4 and coalesce(autorizada_sri_fafac,false)=true) or (fac.ide_tetid=3 and fac.ide_coest not in (1))) " +
					" and extract(month from fecha_transaccion_fafac)= "+mes+
					" group by substring(secuencial_fafac from 1 for 3)  " +
					" union  " +
					" select 'NOTA CREDITO' as doc, substring(nro_nota_credito_fanoc from 1 for 3) as codigo_establecimiento,sum(valor_referencial_fanoc) as ventas_establecimiento  " +
					" from fac_nota_credito   " +
					" where ide_coest in (2) and extract(year from fecha_fanoc)="+anio+
					" and ((substring(nro_nota_credito_fanoc from 5 for 3) like '011' and coalesce(autorizada_sri_fanoc,false)=true) "+
					" or (substring(nro_nota_credito_fanoc from 5 for 3) not like '011' and coalesce(autorizada_sri_fanoc,false)=false)) " +
					" and extract(month from fecha_fanoc)= "+mes+
					" group by substring(nro_nota_credito_fanoc from 1 for 3) " +
					" union " +
					" select 'NOTA DEBITO' as doc, substring(nro_nota_debito_elect_fanod from 1 for 3) as codigo_establecimiento,sum(interes_generado_fanod) as ventas_establecimiento  " +
					" from fac_nota_debito   " +
					" where ide_coest in (16) and extract(year from fecha_emision_fanod)="+anio+
					" and autorizada_sri_fanod=true " +
					" and extract(month from fecha_emision_fanod)= "+mes+
					" group by substring(nro_nota_debito_elect_fanod from 1 for 3) order by 1,2;";
		
//		String sql="select substring(secuencial_fafac from 1 for 3) as codigo_establecimiento,sum(base_aprobada_fafac) as ventas_establecimiento from fac_factura fac " 
//				+" where fac.ide_coest in (1,2,16,24) and autorizada_sri_fafac=true and extract(year from fecha_transaccion_fafac)="+anio+" and extract(month from fecha_transaccion_fafac)= "+mes
//				+" group by substring(secuencial_fafac from 1 for 3) order by 1;";
		return sql;
	}
	
	public String getAtsAnulados(String anio,String mes){
        String sql="select 'FACTURA' as doc, '01' as codigo_tipo_comprobate, substring(secuencial_fafac from 1 for 3) as establecimiento, substring(secuencial_fafac from 5 for 3) as punto_emision   " +
		", substring(secuencial_fafac from 9 for 9) as seciencial_inicio, substring(secuencial_fafac from 9 for 9) as seciencial_fin, nro_autorizacion_sri_fac as autorizacion  " +
		" from fac_factura fac   " +
		" where coalesce(activo_fafac,false) in (true,false) and fac.ide_coest in (1) and fac.ide_tetid=3 and coalesce(autorizada_sri_fafac,false)=false and extract(year from fecha_transaccion_fafac)="+anio+" and extract(month from fecha_transaccion_fafac)= "+mes+
		" union " +
		" select 'NOTA CREDITO' as doc, '04' as codigo_tipo_comprobate, substring(nro_nota_credito_fanoc from 1 for 3) as establecimiento, substring(nro_nota_credito_fanoc from 5 for 3) as punto_emision   " +
		" , substring(nro_nota_credito_fanoc from 9 for 9) as seciencial_inicio, substring(nro_nota_credito_fanoc from 9 for 9) as seciencial_fin, nro_autorizacion_sri_fanoc as autorizacion  " +
		" from fac_nota_credito  " +
		" where ide_coest in (1) and autorizada_sri_fanoc=true and extract(year from fecha_fanoc)="+anio+" and extract(month from fecha_fanoc)= "+mes+
		" union " +
		" select 'NOTA DEBITO' as doc, '05' as codigo_tipo_comprobate, substring(nro_nota_debito_elect_fanod from 1 for 3) as establecimiento, substring(nro_nota_debito_elect_fanod from 5 for 3) as punto_emision   " +
		" , substring(nro_nota_debito_elect_fanod from 9 for 9) as seciencial_inicio, substring(nro_nota_debito_elect_fanod from 9 for 9) as seciencial_fin, nro_autorizacion_sri_fanod as autorizacion  " +
		" from fac_nota_debito  " +
		" where ide_coest in (1) and autorizada_sri_fanod=true and extract(year from fecha_emision_fanod)="+anio+" and extract(month from fecha_emision_fanod)= "+mes+" order by 1,3;";
   
//		String sql="select '01' as codigo_tipo_comprobate, substring(secuencial_fafac from 1 for 3) as establecimiento, substring(secuencial_fafac from 5 for 3) as punto_emision " 
//				+" , substring(secuencial_fafac from 9 for 9) as seciencial_inicio, substring(secuencial_fafac from 9 for 9) as seciencial_fin, '' as autorizacion from fac_factura fac "
//				+" where fac.ide_coest in (1) and extract(year from fecha_transaccion_fafac)="+anio+" and extract(month from fecha_transaccion_fafac)= "+mes+" ;";
		return sql;
	}
	
	public String getGastosPersonales(String anio){
        String sql="select row_number() over(order by cast(extract(month from ded.fecha_ingre) as int)) as codigo,"
				+" cast(extract(month from ded.fecha_ingre) as int) as mes"
				+" ,case when activo_gtemp =true then 'Empleados' else 'Ex Empleados' end as TipoEmpleado"
				+" , sum(case when activo_gtemp =true then valor_deducible_srdee else 0 end) as totalActivos"
				+" , sum(case when activo_gtemp =false then valor_deducible_srdee else 0 end) as totalInacActivos"
				+" FROM sri_deducibles_empleado ded"
				+"  join gth_empleado emp on emp.ide_gtemp=ded.ide_gtemp"
				
				+" where extract(year from ded.fecha_ingre)=(select cast(detalle_geani as int) as anio from gen_anio where ide_geani="+anio+") "
				+" group by mes,activo_gtemp"
				+" order by mes;";

		return sql;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	public void getTablaReporte107Liquidacion(int ide_anio,String ide_geedp,String ide_gtemp,String fecIni,String fecFin ){
		StringBuilder 	str_ide= new StringBuilder();
		StringBuilder 	str_ide_actual= new StringBuilder();
		StringBuilder 	str_ide_decimotercer= new StringBuilder();
		StringBuilder 	str_ide_roles= new StringBuilder();
		int mesIni=0,mesFin=0;
		boolean bandFechaValidacion=false;
		System.out.println("EMPLEADO "+ide_gtemp);
		if (fecIni.compareTo((utilitario.getAnio(fecFin)+"-01"+"-31"))<=0) {
			mesIni=1;
		}else {
			mesIni=utilitario.getMes(fecIni);
		}

		
		
		mesFin=utilitario.getMes(fecFin);
		int valor=mesIni;
		for (int i = mesIni; i <= mesFin; i++) 	
		{
	 		//Voy anidando los ides de la accion
			str_ide_roles.append(i);
	       // valor++;
	        if (mesFin==mesIni) {
			}else if (valor<=mesFin) {
				valor++;
					if(valor<=(mesFin)){
						str_ide_roles.append(",");
	            //System.out.println("str_ide:  "+str_ide);
					}
			}

			 }
		
		
		
		

		String IDE_SRIMR="";
		int valorEmp=0;
		int valorEmpActual=0;
		int anio=0,anioResultado=0;
		double valor_sbv_anterior=0.0,valor_sbv_actual=0.0;
		double sueldo=0.0;
		TablaGenerica tabAnio =utilitario.consultar("Select * from gen_anio where ide_geani="+ide_anio);
		TablaGenerica tabSRI =utilitario.consultar("SELECT ide_srimr, detalle_srimr, fecha_inicio_srimr, fecha_fin_srimr,  "
				+ "activo_srimr "
				+ "FROM sri_impuesto_renta "
				+ "WHERE detalle_srimr LIKE '"+tabAnio.getValor("DETALLE_GEANI")+"'");
		IDE_SRIMR=tabSRI.getValor("ide_srimr");
		utilitario.getConexion().ejecutarSql("delete from sri_formulario107 where generado_liquidacion_srfor=true and ide_srimr="+IDE_SRIMR+" and "
				+ "IDE_GTEMP="+ide_gtemp);
		String roles="",str_ide_anio="";
		anio=Integer.parseInt(tabAnio.getValor("DETALLE_GEANI"));
		anioResultado=anio-1;
		String anioHasta=""+tabAnio.getValor("DETALLE_GEANI");
		str_ide_anio=tabAnio.getValor("IDE_GEANI");
		TablaGenerica tabRolDecimoCuarto1=null;
		TablaGenerica tabAnioNuevo =utilitario.consultar("Select * from gen_anio where detalle_geani like '%"+anioResultado+"%'");
		String fecha_ini="",fecha_fin="";
		StringBuilder ide_mes_decimo_cuarto= new StringBuilder();
		StringBuilder ide_mes_decimo_cuarto2= new StringBuilder();

		if (tabAnioNuevo.getTotalFilas()>0) {
			
			String str_sql="";

			String fecha_comaracion=utilitario.getAnio(fecFin)+"-01-01";

			String fecha_comparacionCuarto=(utilitario.getAnio(fecFin)-1)+"-08-01";
			String fecha_comparacionCuartoRango=utilitario.getAnio(fecFin)+"-08-01";
			//String fecha_calculoFinInicio=utilitario.getAnio(utilitario.getFechaActual())+"-"+(utilitario.getMes(fecFin)-1)+"-01";
			//String mes=utilitario.getUltimoDiaMesFecha(fecha_calculoFinInicio);
			//String fecha=utilitario.getAnio(fecFin)+"-"+(utilitario.getMes(fecha_fin)-1)+"-"+utilitario.getDia(mes);
			
			//fecFin=fecha;
			System.out.println("DECIMOS AJUSTE");
			System.out.println("FECHA_INICIO"+fecIni);
			System.out.println("FECHA_FIN"+fecFin);
			if (fecIni.compareTo(fecha_comparacionCuarto)<=0) {
				fecha_ini=fecha_comparacionCuarto;
				}else if(fecIni.compareTo(fecha_comparacionCuarto)>0) {
					fecha_ini=fecIni;
					bandFechaValidacion=true;
				}
			
			if (fecFin.compareTo(fecha_comparacionCuartoRango)<=0) {
			fecha_fin=fecFin;
			}else {
/////////////////////////////////////////////////////////////////////////AQUI CAMBIE//////////////////////////////////////////				
				if (bandFechaValidacion==false) {
					fecha_fin=fecha_comparacionCuartoRango;	
				}else {
					fecha_fin=fecFin;	
				}
				
				
			}
			
			}
				
				else {
				fecha_ini=fecIni;
				fecha_fin=fecFin;
			}
		
			
			
		//String ide_gepro=getPeriodosRol(fecha_ini, fecha_fin);
			
if (utilitario.getAnio(fecIni)==utilitario.getAnio(fecFin)) {
	
	TablaGenerica tabAnioNuevo1 =utilitario.consultar("Select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_ini)+"%'");

	int mesAuxIni=utilitario.getMes(fecha_ini);
	int mesAuxFin=utilitario.getMes(fecha_fin);
	int valorAux=mesAuxIni;
	for (int i = mesAuxIni; i <= mesAuxFin; i++) 	
	{
 		//Voy anidando los ides de la accion
		ide_mes_decimo_cuarto.append(i);
       // valor++;
        if (mesAuxFin==mesAuxIni) {
		}else if (valorAux<=mesAuxFin) {
			valorAux++;
				if(valorAux<=(mesAuxFin)){
					ide_mes_decimo_cuarto.append(",");
            //System.out.println("str_ide:  "+str_ide);
				}
		}

		 }
	

	
	TablaGenerica tabRolDecimoCuarto2=utilitario.consultar("select * from nrh_rol  rol "
			+ "left join gen_perido_rol gprol on gprol.ide_gepro= rol.ide_gepro   "
			+ "where gprol.ide_gemes in("+ide_mes_decimo_cuarto.toString()+") and gprol.ide_geani="+tabAnioNuevo1.getValor("IDE_GEANI")+"  "
				+ "and rol.ide_nrdtn in(2,4)   "
			+ "order by rol.ide_nrrol asc");
	
	if (tabRolDecimoCuarto2.getTotalFilas()>0) {
		for (int i = 0; i < tabRolDecimoCuarto2.getTotalFilas(); i++) 
	 		{
	 		//Voy anidando los ides de la accion
	  	    str_ide_actual.append(tabRolDecimoCuarto2.getValor(i, "IDE_NRROL"));
	  	    //valorEmpActual++;
	        if (tabRolDecimoCuarto2.getTotalFilas()==1) {
			}else if (valorEmpActual<=tabRolDecimoCuarto2.getTotalFilas()) {
				valorEmpActual++;
					if(valorEmpActual<(tabRolDecimoCuarto2.getTotalFilas())){
	            str_ide_actual.append(",");
	            //System.out.println("str_ide:  "+str_ide);
					}
			}

			 }

		valor_sbv_actual=consultarSueloXAnio(Integer.parseInt(tabAnioNuevo1.getValor("IDE_GEANI")), "1");

		
			}else{
				tabRolDecimoCuarto2=utilitario.consultar("select * from nrh_rol  rol "
				+ "left join gen_perido_rol gprol on gprol.ide_gepro= rol.ide_gepro   "
				+ "where gprol.ide_gemes in(1,2,3,4,5,6,7) and gprol.ide_geani=-1 and rol.ide_nrdtn in(2,4)  "
				+ "order by rol.ide_nrrol asc ");
		str_ide_actual.append("-1");
		valor_sbv_actual=0.0;
		}



}else {

	int mesAuxIni=utilitario.getMes(fecha_ini);
	int mesAuxFin=12;
	int valorAux=mesAuxIni;
	for (int i = mesAuxIni; i <= mesAuxFin; i++) 	
	{
 		//Voy anidando los ides de la accion
		ide_mes_decimo_cuarto.append(i);
       // valor++;
        if (mesAuxFin==mesAuxIni) {
		}else if (valorAux<=mesAuxFin) {
			valorAux++;
				if(valorAux<=(mesAuxFin)){
					ide_mes_decimo_cuarto.append(",");
            //System.out.println("str_ide:  "+str_ide);
				}
		}

		 }


	
	TablaGenerica tabAnioNuevo1 =utilitario.consultar("Select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_ini)+"%'");
	tabRolDecimoCuarto1=utilitario.consultar("select * from nrh_rol  rol "
			+ "left join gen_perido_rol gprol on gprol.ide_gepro= rol.ide_gepro   "
			+ "where gprol.ide_gemes in("+ide_mes_decimo_cuarto.toString()+") and gprol.ide_geani="+tabAnioNuevo1.getValor("IDE_GEANI")+" "
			+ "and rol.ide_nrdtn in(2,4)  "
			+ "order by rol.ide_nrrol asc ");



	if (tabRolDecimoCuarto1.getTotalFilas()>0) {
		for (int i = 0; i < tabRolDecimoCuarto1.getTotalFilas(); i++) 
	 		{
	 		//Voy anidando los ides de la accion
	  	    str_ide.append(tabRolDecimoCuarto1.getValor(i, "IDE_NRROL"));
	       // valor++;
	        if (tabRolDecimoCuarto1.getTotalFilas()==1) {
			}else if (valorEmp<=tabRolDecimoCuarto1.getTotalFilas()) {
				valorEmp++;
					if(valorEmp<(tabRolDecimoCuarto1.getTotalFilas())){
	            str_ide.append(",");
	            //System.out.println("str_ide:  "+str_ide);
					}
			}

			 }
		valor_sbv_anterior=consultarSueloXAnio(Integer.parseInt(tabAnioNuevo1.getValor("IDE_GEANI")), "12");

			}else{
				tabRolDecimoCuarto1=utilitario.consultar("select * from nrh_rol  rol "
				+ "left join gen_perido_rol gprol on gprol.ide_gepro= rol.ide_gepro   "
				+ "where gprol.ide_gemes in(8,9,10,11,12) and gprol.ide_geani=-1 and rol.ide_nrdtn in(2,4)  "
				+ "order by rol.ide_nrrol asc ");
		str_ide.append("-1");
		valor_sbv_anterior=0;
			}

	
	int mesAuxIni2=1;
	int mesAuxFin2=utilitario.getMes(fecha_fin);
	int valorAux2=mesAuxIni2;
	for (int i = mesAuxIni2; i <= mesAuxFin2; i++) 	
	{
 		//Voy anidando los ides de la accion
		ide_mes_decimo_cuarto2.append(i);
       // valor++;
        if (mesAuxFin2==mesAuxIni2) {
		}else if (valorAux2<=mesAuxFin2) {
			valorAux2++;
				if(valorAux2<=(mesAuxFin2)){
					ide_mes_decimo_cuarto2.append(",");
            //System.out.println("str_ide:  "+str_ide);
				}
		}

		 }

		

	TablaGenerica tabAnioNuevo2 =utilitario.consultar("Select * from gen_anio where detalle_geani like '%"+utilitario.getAnio(fecha_fin)+"%'");

	
	
	
	TablaGenerica tabRolDecimoCuarto2=utilitario.consultar("select * from nrh_rol  rol "
			+ "left join gen_perido_rol gprol on gprol.ide_gepro= rol.ide_gepro   "
			+ "where gprol.ide_gemes in("+ide_mes_decimo_cuarto2.toString()+") and gprol.ide_geani="+tabAnioNuevo2.getValor("IDE_GEANI")+"  "
				+ "and rol.ide_nrdtn in(2,4)   "
			+ "order by rol.ide_nrrol asc");
	
	
	
	
	if (tabRolDecimoCuarto2.getTotalFilas()>0) {
		for (int i = 0; i < tabRolDecimoCuarto2.getTotalFilas(); i++) 
	 		{
	 		//Voy anidando los ides de la accion
	  	    str_ide_actual.append(tabRolDecimoCuarto2.getValor(i, "IDE_NRROL"));
	  	    //valorEmpActual++;
	        if (tabRolDecimoCuarto2.getTotalFilas()==1) {
			}else if (valorEmpActual<=tabRolDecimoCuarto2.getTotalFilas()) {
				valorEmpActual++;
					if(valorEmpActual<(tabRolDecimoCuarto2.getTotalFilas())){
	            str_ide_actual.append(",");
	            //System.out.println("str_ide:  "+str_ide);
					}
			}

			 }

		valor_sbv_actual=consultarSueloXAnio(Integer.parseInt(tabAnioNuevo2.getValor("IDE_GEANI")), "1");

		
			}else{
				tabRolDecimoCuarto2=utilitario.consultar("select * from nrh_rol  rol "
				+ "left join gen_perido_rol gprol on gprol.ide_gepro= rol.ide_gepro   "
				+ "where gprol.ide_gemes in(1,2,3,4,5,6,7) and gprol.ide_geani=-1 and rol.ide_nrdtn in(2,4)  "
				+ "order by rol.ide_nrrol asc ");
		str_ide_actual.append("-1");
		valor_sbv_actual=0.0;
		}
}			
			
			
		

		
		boolean uno=false,dos=false;
		
		
		if (ide_mes_decimo_cuarto.length()==0) {
			uno=false;
		}else {
			uno=true;
		}
		
		if (ide_mes_decimo_cuarto2.length()==0) {
			dos=false;
		}else {
			dos=true;
		}	
	if (uno==true && dos==true) {
		roles=str_ide.toString()+","+str_ide_actual;
		sueldo=valor_sbv_actual;
	}
	if (uno==true && dos==false) {
		roles=str_ide_actual.toString();
		sueldo=valor_sbv_actual;
	}
		
	if (uno==false && dos==true) {
		roles=str_ide_actual.toString();
		sueldo=valor_sbv_actual;
	}
			
	if (uno==false && dos==false) {
		roles="-1";
		sueldo=0;
	}	

	TablaGenerica tabRolDecimoTercer=utilitario.consultar("select * from nrh_rol  rol "
			+ "left join gen_perido_rol gprol on gprol.ide_gepro= rol.ide_gepro   "
			+ "where gprol.ide_gemes in("+str_ide_roles.toString()+") and gprol.ide_geani="+str_ide_anio+"  "
				+ "and rol.ide_nrdtn in(2,4)   "
			+ "order by rol.ide_nrrol asc");
		
	valorEmp=0;
	if (tabRolDecimoTercer.getTotalFilas()>0) {
		for (int i = 0; i < tabRolDecimoTercer.getTotalFilas(); i++) 
	 		{
	 		//Voy anidando los ides de la accion
			str_ide_decimotercer.append(tabRolDecimoTercer.getValor(i, "IDE_NRROL"));
	       // valor++;
	        if (tabRolDecimoTercer.getTotalFilas()==1) {
			}else if (valorEmp<=tabRolDecimoTercer.getTotalFilas()) {
				valorEmp++;
					if(valorEmp<(tabRolDecimoTercer.getTotalFilas())){
						str_ide_decimotercer.append(",");
	            //System.out.println("str_ide:  "+str_ide);
					}
			}

			 }

	}




		 String sql="select   *, coalesce( round(((basimp - (SELECT  case when (fraccion_basica_srdir - 1)<0 then 0 else (fraccion_basica_srdir-1) end as "
		 		+ "fraccion_basica_srdir "
		 		+ "FROM public.sri_detalle_impuesto_renta  where  ide_srimr =  " + IDE_SRIMR + "  and "
		 		+ "basimp between fraccion_basica_srdir and exceso_hasta_srdir))* "
		 		+ "(SELECT coalesce(imp_fraccion_excedente_srdir ,0.00)/100 "
		 		+ "FROM public.sri_detalle_impuesto_renta "
		 		+ "where  ide_srimr = " + IDE_SRIMR + " and basimp between fraccion_basica_srdir and exceso_hasta_srdir)) + (SELECT  imp_fraccion_srdir "
		 		+ "FROM public.sri_detalle_impuesto_renta   where  ide_srimr = " + IDE_SRIMR + " and basimp between fraccion_basica_srdir and exceso_hasta_srdir),2),0)    as impuestoCausado  "
		 		+ "from ( select distinct( select identificacion_empr from sis_empresa limit 1) as nuRuc,ide_geedp, emp.ide_gtemp, "
		 		//+ "(select nom_empr from sis_empresa limit 1) as nomEmpresa,  "
		 		+ "substring(detalle_gttdi from 1 for 1) as tipIdRet,  "
		 		+ "documento_identidad_gtemp as idRet,  "
		 		+ "APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP as apellidoTrab,   "
		 		+ "COALESCE(coalesce(PRIMER_NOMBRE_GTEMP,' ') ||' '|| coalesce(SEGUNDO_NOMBRE_GTEMP,' '),' ') as nombreTrab,  "
		 		+ "'001' as estab,  "
		 		+ "'01'   as residenciaTrab,  "
		 		+ "'593' as pasisResidencia,  "
		 		+ "'NA' as aplicaConvenio, "
		 		+ "case when (round(COALESCE(porcentaje_gtdie,0)) >= 40) then '02' else '01' end as tipoTrabajoDiscap,  "
		 		+ "case when   round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE(porcentaje_gtdie,0)) else 0 end  as porcentajeDiscap,  "
		 		+ "'N' as tipidDiscap,  "
		 		+ "'999' as idDiscap,  "
		 		+ "COALESCE(sueldos,0.00)  + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) as suelSal,  "
		 		+ "COALESCE(sobresueldos,0.00) as sobSuelComRemu,  "
		 		+ "'0' as partUtil,  "
		 		+ "'0' as intGrabGen,  "
		 		+ "'0' as impRentEmpl,  "
		 		+ "COALESCE(DecimoTercero,0.00) as decimoTercer,   case  "
		 		+ "when COALESCE(decimoCuarto, 0.00)+COALESCE(decimoCuartoAjuste,0.00)>"+(sueldo-5)+" then "+sueldo+"  "
		 		+ "else  "
		 		+ "COALESCE(decimoCuarto, 0.00)+COALESCE(decimoCuartoAjuste,0.00) end  "
		 		+ " as decimoCuarto, "
		 		+ "COALESCE(fondo, 0.00) as  fondoReserva,  "
		 		+ "'0' as salarioDigno,  "
		 		+ "'0' as otrosIngRenGrav,  "
		 		+ "COALESCE(sueldos,0.00) + COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) + COALESCE(sobresueldos,0.00)    as ingGravConEsteEmpl,  "
		 		+ "'1' as sisSalNet,  "
		 		+ "COALESCE(aporteIess, 0.00) as apoPerIess,  "
		 		+ "'0.00' as aporPerIessConOtrosEmpls,  "
		 		+ "case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR) * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) "
		 		+ "from SRI_DETALLE_IMPUESTO_RENTA  "
		 		+ "where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end  as descuentoDiscapacidad ,   "
		 		+ "case when round(FLOOR(((extract(year from cast('"+anioHasta+"-12-31' as Date) )-extract(year from fecha_nacimiento_gtemp))* 372 +  (extract(month from cast('"+anioHasta+"-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +  (extract (day from cast('"+anioHasta+"-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS  WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)    "
		 		+ "from SRI_DETALLE_IMPUESTO_RENTA   "
		 		+ "where IDE_SRIMR= " + IDE_SRIMR + "  GROUP BY IDE_SRIMR),0),2) else 0 end  as descuentoTercera ,  "
		 		+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when   EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp   else '"+anioHasta+"-01-1' end::date)) < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12    else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(vivienda.valor_deducible_srdee,0.00))   as vivienda, "
		 		+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))  as salud,  "
		 		+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(educa.valor_deducible_srdee,0.00))   as educacion,  "
		 		+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(ali.valor_deducible_srdee,0.00))  as alimentacion,  "
		 		+ "COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <(COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),COALESCE(vest.valor_deducible_srdee,0.00)) as vestimenta,  "
		 		+ "case when round( cast (COALESCE(COALESCE(sueldos,0.00)+ COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) +   COALESCE(sobresueldos,0.00),0.00) + COALESCE(r301_srfor,0.00) - COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vivienda.valor_deducible_srdee,0.00))         -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))         -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(educa.valor_deducible_srdee,0.00))         -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(ali.valor_deducible_srdee,0.00))          -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +  "
		 		+ "(DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vest.valor_deducible_srdee,0.00))        - COALESCE(aporteIess, 0.00) -(case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR) * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= " + IDE_SRIMR + "  GROUP BY IDE_SRIMR),0),2) else 0 end)   -(case when round(FLOOR(((extract(year from cast('"+anioHasta+"-12-31' as Date))-extract(year from fecha_nacimiento_gtemp))* 372 +  "
		 		+ "(extract(month from cast('"+anioHasta+"-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +   (extract (day from cast('"+anioHasta+"-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= " + IDE_SRIMR + " GROUP BY IDE_SRIMR),0),2) else 0 end)  as numeric),2) > 0   then  round( cast (COALESCE(COALESCE(sueldos,0.00)+ COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) +      COALESCE(sobresueldos,0.00),0.00) + COALESCE(r301_srfor,0.00) -  COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(vivienda.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vivienda.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vivienda.valor_deducible_srdee,0.00))       -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <   (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))  then (COALESCE(salud.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP ,   FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(salud.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(salud.valor_deducible_srdee,0.00))        -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <       (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))   then (COALESCE(educa.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(educa.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(educa.valor_deducible_srdee,0.00))           -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <       (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(ali.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year',   case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))   < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(ali.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(ali.valor_deducible_srdee,0.00))        -COALESCE(round(cast(case when (COALESCE(sueldos,0.00)/2 <    (COALESCE(vivienda.valor_deducible_srdee,0.00)+ COALESCE(salud.valor_deducible_srdee,0.00)+ COALESCE(educa.valor_deducible_srdee,0.00)+COALESCE(vest.valor_deducible_srdee,0.00)      +COALESCE(ali.valor_deducible_srdee,0.00)))       then (COALESCE(vest.valor_deducible_srdee,0.00))*(case when (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when   EXTRACT (YEAR from fecha_geedp) = "+anioHasta+" then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      < 12 then (DATE_PART('year', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('year', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date)) * 12 +      (DATE_PART('month', COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)::date) - DATE_PART('month', case when EXTRACT (YEAR from fecha_geedp) = "+anioHasta+"  then fecha_geedp else '"+anioHasta+"-01-1' end::date))      else EXTRACT(MONTH from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP)) end)/12  else COALESCE(vest.valor_deducible_srdee,0.00) end as numeric ), 2),   COALESCE(vest.valor_deducible_srdee,0.00))          - COALESCE(aporteIess, 0.00) -(case when round(COALESCE(porcentaje_gtdie,0)) >= 40 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric) * (select round(cast(porcentaje_aplica_srbtd as numeric)/100,2)    from SRI_BENEFICIO_TRIBUTARIO_DISC where porcentaje_gtdie BETWEEN GRADO_INICIAL_SRBTD and GRADO_FINAL_SRBTD) from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= " + IDE_SRIMR + "  GROUP BY IDE_SRIMR),0),2) else 0 end)  -(case when round(FLOOR(((extract(year from cast('"+anioHasta+"-12-31' as Date))-extract(year from fecha_nacimiento_gtemp))* 372 +  (extract(month from cast('"+anioHasta+"-12-31' as Date)) - extract(month from fecha_nacimiento_gtemp))*31 +  (extract (day from cast('"+anioHasta+"-12-31' as Date))-extract(day from fecha_nacimiento_gtemp)))/372) ) >= 65 then round(COALESCE((select min(EXCESO_HASTA_SRDIR)  * cast((SELECT VALOR_PARA FROM SIS_PARAMETROS   WHERE LOWER(NOM_PARA)='p_factor_multiplicador_renta_discapacitados' ) as numeric)  from SRI_DETALLE_IMPUESTO_RENTA    where IDE_SRIMR= " + IDE_SRIMR + "  GROUP BY IDE_SRIMR),0),2) else 0 end)  as numeric),2)  else 0.00 end as basImp, '0' as valRetAsuOtrosEmpls,   '0' as valImpAsuEsteEmpl,      COALESCE(rentaI,0.00)  as valRet, COALESCE(VALOR_LIQUIDACION_GEEDP,0.00) as VALOR_LIQUIDACION_GEEDP,   round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100/3.3333333,2) as baseSolidaria,   COALESCE(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00),0.00)  as solidario,   round(round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100*(CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0   then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)/3.3333333,2)  /((CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0 then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)) *0.0333,2)   as impuestoSolidario ,   COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),0) as numMeses ,    round(round(COALESCE(COALESCE(aporte,0.00)+COALESCE(varios,0.00),0.00)*100/3.3333333,2)/((CASE when COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) >0   then COALESCE(COALESCE(numMesesAporte,0)+COALESCE(numMesesVarios,0),1) else 1 end)) ,2)  as promedio  "
		 	    + " from gth_empleado as emp      "
		 	    + "left join sri_deducibles_empleado as ali on ali.ide_gtemp   = emp.ide_gtemp  and ali.IDE_SRDED =    "
		 	    + "(select  ide_srded from  SRI_DEDUCIBLES sri left join sri_impuesto_renta as imp    on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "   and DETALLE_SRDED like '%ALIMENTACION%')     "
		 	    + "left join sri_deducibles_empleado as salud on salud.ide_gtemp   = emp.ide_gtemp     and salud.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri    left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like '%SALUD%')    "
		 	    + "left join sri_deducibles_empleado as vivienda on vivienda.ide_gtemp   = emp.ide_gtemp      and vivienda.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri    left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like '%VIVIENDA%')     "
		 	    + "left join sri_deducibles_empleado as educa on educa.ide_gtemp   = emp.ide_gtemp  and educa.IDE_SRDED =    (select  ide_srded from  SRI_DEDUCIBLES sri left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr    where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like '%EDUCACION%')    "
		 	    + "left join sri_deducibles_empleado as vest on vest.ide_gtemp   =    emp.ide_gtemp and vest.IDE_SRDED =(select  ide_srded from  SRI_DEDUCIBLES sri    left join sri_impuesto_renta as imp on sri.ide_srimr= imp.ide_srimr where sri.ide_srimr = " + IDE_SRIMR + "  and DETALLE_SRDED like '%VESTIMENTA%')   left join   gth_tipo_documento_identidad as tdocumento on tdocumento.ide_gttdi = emp.ide_gttdi      "
		 	    + "left join gth_discapacidad_empleado as disca on disca.ide_gtemp = emp.ide_gtemp      "
		 	    + "left join (select empDepar.ide_gtemp, sum(VALOR_LIQUIDACION_GEEDP) as VALOR_LIQUIDACION_GEEDP, fecha_geedp, FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP,IDE_GEEDP,   STRING_AGG(cast (ide_gttem as text), '-') as ide_gttem from GEN_EMPLEADOS_DEPARTAMENTO_PAR as empDepar   left join gen_detalle_empleado_departame as empDet on empDepar.ide_geded = empDet.ide_geded   "
		 		+ "where  activo_geded = true   "
		 		+ "and empDepar.ide_gtemp in ("+ide_gtemp+") "
		 		+ "and (COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )    is null or EXTRACT(YEAR  from COALESCE(FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP )  )="+anioHasta+")   "
		 		+ "group by empDepar.ide_gtemp,fecha_geedp,FECHA_FINCTR_GEEDP , FECHA_LIQUIDACION_GEEDP, ide_geedp)   as "
		 		+ "depar on depar.ide_gtemp= emp.ide_gtemp  "
		 		+ "left join    (SELECT IDE_GTEMP , SUM(DETAROL.VALOR_NRDRO) AS fondo      FROM NRH_DETALLE_ROL detarol    "
		 		+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP     "
		 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER      "
		 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB       "
		 		+ "where  IDE_NRROL    in ("+str_ide_decimotercer.toString()+") "
		 		//+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL "
		 		//+ "where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+")) "   
		 		+ "and detrubro.IDE_NRRUB in (347,345) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro=TRUE) AND IDE_GTEMP in ("+ide_gtemp+")  group by IDE_GTEMP) as fondos on fondos.IDE_GTEMP = emp.IDE_GTEMP     "
		 		+ "left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS sueldos    FROM NRH_DETALLE_ROL detarol     "
		 		+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "
		 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
		 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    "
		 		+ "where  IDE_NRROL    in ("+str_ide_decimotercer.toString()+") "
		 		//+ "where  IDE_NRROL in    (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))      "
		 		+ "and detrubro.IDE_NRRUB in (288,344) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro=TRUE) AND IDE_GTEMP in ("+ide_gtemp+") group by IDE_GTEMP) sueldo on sueldo.IDE_GTEMP = emp.IDE_GTEMP      "
		 		
		 		+" left join sri_formulario_107 f107 on f107.ide_gtemp=emp.ide_gtemp and IDE_SRIMR= 14 "
		 		
		 		+ "left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) as aporte , count(DETAROL.VALOR_NRDRO)  as numMesesAporte    "
		 		+ "FROM NRH_DETALLE_ROL detarol left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "
		 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
		 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    "
		 		+ "where  IDE_NRROL    in ("+str_ide_decimotercer.toString()+") "
		 		//+ "where  IDE_NRROL in    (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))      "		 		
		 		+ "and detrubro.IDE_NRRUB in (356) and VALOR_NRDRO !=0 AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro=TRUE) AND IDE_GTEMP in ("+ide_gtemp+") group by IDE_GTEMP) solidario on solidario.IDE_GTEMP = emp.IDE_GTEMP      "
		 		+ "left join (    SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) as varios , count(DETAROL.VALOR_NRDRO)  as numMesesVarios FROM NRH_DETALLE_ROL detarol     "
		 		+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP      "
		 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
		 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB    "
		 		+ "where  IDE_NRROL    in ("+str_ide_decimotercer.toString()+") "
		 		//+ "where  IDE_NRROL in    (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))      "
		 		+ "and detrubro.IDE_NRRUB in (203) and VALOR_NRDRO !=0 and ide_nrrol in (19,20) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro=TRUE) AND IDE_GTEMP in ("+ide_gtemp+") group by IDE_GTEMP) solidarioVarios on solidarioVarios.IDE_GTEMP = emp.IDE_GTEMP  "
		 		+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS sobresueldos  "
		 		+ "FROM NRH_DETALLE_ROL detarol   "
		 		+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP   "
		 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER     "
		 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
		 		+ "where  IDE_NRROL    in ("+str_ide_decimotercer.toString()+") "
		 		//+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO    in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI= "+str_ide_anio+"))  "
		 		+ "and detrubro.IDE_NRRUB in (27,17,354,331,336,313) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro=TRUE) AND IDE_GTEMP in ("+ide_gtemp+") group by IDE_GTEMP) sobre on sobre.IDE_GTEMP = emp.IDE_GTEMP  "
		 		+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS DecimoTercero  "
		 		+ "FROM NRH_DETALLE_ROL detarol     left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "
		 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER  "
		 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
//		 		+ "where  IDE_NRROL    in ("+str_ide_decimotercer.toString()+")"
				+ "where  IDE_NRROL    in ("+str_ide_decimotercer.toString()+") "
//+ "select IDE_NRROL from NRH_ROL where IDE_GEPRO in   "
		 		//+ "(select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI= "+str_ide_anio+"))     "
		 		+ "and detrubro.IDE_NRRUB in (334,125) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro =TRUE) AND IDE_GTEMP in ("+ide_gtemp+") group by IDE_GTEMP) decimoT on decimoT.IDE_GTEMP = emp.IDE_GTEMP  "
		 		+ "     left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS decimoTercerAjuste  "
		 		+ "FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "
		 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
		 		//+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in (72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111))   "
		 		//+ "where IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ("+str_ide_decimotercer.toString()+"))   "
		 		+ "where IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ("+str_ide_decimotercer.toString()+"))   "
		 		+ "and detrubro.IDE_NRRUB in (333) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro=TRUE) AND IDE_GTEMP in ("+ide_gtemp+")  group by IDE_GTEMP) decimoTA1 on decimoTA1.IDE_GTEMP = emp.IDE_GTEMP      left join (   SELECT IDE_GTEMP,  "
		 		+ "SUM(DETAROL.VALOR_NRDRO)  "
		 		+ "AS DecimoCuarto  "
		 		+ "FROM NRH_DETALLE_ROL detarol    left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "
		 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER       "
		 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
		 		//+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in (72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111))  "
		 		+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ("+roles+"))  "	
		 		
		 		+ "and detrubro.IDE_NRRUB in (121) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro=TRUE) AND IDE_GTEMP in ("+ide_gtemp+") group by IDE_GTEMP) decimoTA on decimoTA.IDE_GTEMP = emp.IDE_GTEMP      left join (   SELECT IDE_GTEMP,  "
		 		+ "SUM(DETAROL.VALOR_NRDRO)  "
		 		+ "AS decimoCuartoAjuste  "
		 		+ "FROM NRH_DETALLE_ROL detarol  "
		 		+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP   "
		 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "
		 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB   "
		 		+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ("+roles+"))  "	
		 		//+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))   "
		 		+ "and detrubro.IDE_NRRUB in (333,359) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro =TRUE) AND IDE_GTEMP in ("+ide_gtemp+") group by IDE_GTEMP) decimoC on decimoC.IDE_GTEMP = emp.IDE_GTEMP   "
		 		+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS rentaI   FROM NRH_DETALLE_ROL detarol   "
		 		+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP  "
		 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   "
		 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB  "
		 		+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ("+str_ide_decimotercer.toString()+"))  "	

		 		//+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL where IDE_GEPRO in  "
		 		//+ "(select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))   "
		 		+ "and detrubro.IDE_NRRUB in (42) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro=TRUE)  AND IDE_GTEMP in ("+ide_gtemp+") group by IDE_GTEMP) renta    on renta.IDE_GTEMP = emp.IDE_GTEMP   "
		 		+ "left join (   SELECT IDE_GTEMP, SUM(DETAROL.VALOR_NRDRO) AS aporteIess   "
		 		+ "FROM NRH_DETALLE_ROL detarol    "
		 		+ "left JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP    "
		 		+ "left JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER   " 
		 		+ "left JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB      "
		 		+ "where  IDE_NRROL in   (select IDE_NRROL from NRH_ROL where IDE_NRROL in ("+str_ide_decimotercer.toString()+"))  "	
		 		//+ "where  IDE_NRROL in (select IDE_NRROL from NRH_ROL    where IDE_GEPRO in (select IDE_GEPRO from GEN_PERIDO_ROL  WHERE IDE_GEANI=  "+str_ide_anio+"))    "
		 		+ "and detrubro.IDE_NRRUB in (44) AND DETRUBRO.ide_nrdtn in (2,4) AND (DETAROL.es_liquidacion_nrdro=TRUE) AND IDE_GTEMP in ("+ide_gtemp+") group by IDE_GTEMP) iess on iess.IDE_GTEMP = emp.IDE_GTEMP      "
		 		+ "where depar.ide_gttem not like '%3%'      order by APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP,      "
		 		+ "COALESCE(coalesce(PRIMER_NOMBRE_GTEMP,' ') ||' '|| coalesce(SEGUNDO_NOMBRE_GTEMP,' '),' ')) y ";
				 	System.out.println(sql);
				 	
				 	TablaGenerica tab_seleccion= null;
				 	tab_seleccion=utilitario.consultar(sql);
				 	tab_seleccion.ejecutarSql();
					 insertarTablaFormulario107(Double.parseDouble(tab_seleccion.getValor("suelSal")), 
			 				  Double.parseDouble(tab_seleccion.getValor("sobSuelComRemu")), 
			 				  Double.parseDouble(tab_seleccion.getValor("partUtil")), 
			 				  Double.parseDouble(tab_seleccion.getValor("intGrabGen")),
			 				  Double.parseDouble(tab_seleccion.getValor("decimoTercer")), 
			 				  Double.parseDouble(tab_seleccion.getValor("decimoCuarto")),
			 				  Double.parseDouble(tab_seleccion.getValor("fondoReserva")), 
			 				  Double.parseDouble(tab_seleccion.getValor("otrosIngRenGrav")), 
			 				  Double.parseDouble(tab_seleccion.getValor("apoPerIess")), 
			 				  Double.parseDouble(tab_seleccion.getValor("aporPerIessConOtrosEmpls")), 
			 				  Double.parseDouble(tab_seleccion.getValor("vivienda")), 
			 				  Double.parseDouble(tab_seleccion.getValor("salud")), 
			 				  Double.parseDouble(tab_seleccion.getValor("educacion")), 
			 				  Double.parseDouble(tab_seleccion.getValor("alimentacion")), 
			 				  Double.parseDouble(tab_seleccion.getValor("vestimenta")), 
			 				  Double.parseDouble(tab_seleccion.getValor("descuentoDiscapacidad")), 
			 				  Double.parseDouble(tab_seleccion.getValor("descuentoTercera")), 
			 				  Double.parseDouble(tab_seleccion.getValor("valimpasuesteempl")), 
			 				  Double.parseDouble(tab_seleccion.getValor("basimp")),
			 				  Double.parseDouble(tab_seleccion.getValor("impuestoCausado")), 
			 				  Double.parseDouble(tab_seleccion.getValor("valRetAsuOtrosEmpls")), 
			 				  Double.parseDouble(tab_seleccion.getValor("valImpAsuEsteEmpl")), 
			 				  Double.parseDouble(tab_seleccion.getValor("valRet")), 
			 				  Double.parseDouble(tab_seleccion.getValor("ingGravConEsteEmpl")), 
			 				  true, 
			 				  true,Integer.parseInt(tab_seleccion.getValor("ide_gtemp")),
			 				  Integer.parseInt(tab_seleccion.getValor("ide_geedp")),  Integer.parseInt(IDE_SRIMR),
			 				 tab_seleccion.getValor("nuRuc"));
				 	
				 	
	}
	
	
	
	
	
	
	public double consultarSueloXAnio(int anio, String mes){

		TablaGenerica tabSBU=utilitario.consultar("SELECT valor_nrdro,count(*) as contador  "
				+ "FROM nrh_detalle_rol "
				+ "WHERE  ide_nrrol in( "
				+ "select ide_nrrol from nrh_rol  rol  "
				+ "left join gen_perido_rol gprol on gprol.ide_gepro= rol.ide_gepro  "
				+ "where gprol.ide_geani="+anio+" and gprol.ide_gemes in ("+mes+") "
				+ ")  and ide_nrder in ( "
				+ "select ide_nrder from nrh_detalle_rubro nrder "
				+ "left join nrh_rubro rub on rub.ide_nrrub = nrder.ide_nrrub  "
				+ "where nrder.ide_nrrub=276) and valor_nrdro>0.0 "
				+ "GROUP BY valor_nrdro "
				+ "HAVING valor_nrdro>1 "
				+ "ORDER BY valor_nrdro ");
				double a=0,b=0;
				boolean bandera=false;
				if (tabSBU.getTotalFilas()>0) {
					a=Double.parseDouble(tabSBU.getValor("VALOR_NRDRO"));
					return a;	
				}else {
					return 0;
				}



		}
	
	
	
	
	
	public void insertarTablaFormulario107(
			 double r301_srfor,
			 double r303_srfor,
			 double r305_srfor,
			 double r307_srfor,
			 double r311_srfor,
			 double r313_srfor,
			 double r315_srfor,
			 double r317_srfor,
			 double r351_srfor,
			 double r353_srfor,
			 double r361_srfor,
			 double r363_srfor,
			 double r365_srfor,
			 double r367_srfor,
			 double r369_srfor,
			 double r371_srfor,
			 double r373_srfor,
			 double r381_srfor,
			 double r399_srfor,
			 double r401_srfor,
			 double r403_srfor,
			 double r405_srfor,
			 double r407_srfor,
			 double r349_srfor,
			 boolean activo_srfor,
			 boolean generado_liquidacion_srfor,
			 int ide_gtemp,
			 int ide_geedp,
			 int ide_srimr,
			 String ruc
			 ){


		
			TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("sri_formulario107", "ide_srfor"));
			String codigo=tab_codigo.getValor("codigo");
			
			
		
			

			utilitario.getConexion().ejecutarSql("INSERT INTO sri_formulario107("
					+ "ide_srfor, r301_srfor, r303_srfor, r305_srfor, r307_srfor, r311_srfor, "
					+ "r313_srfor, r315_srfor, r317_srfor, r351_srfor, r353_srfor, r361_srfor, "
					+ "r363_srfor, r365_srfor, r367_srfor, r369_srfor, r371_srfor, r373_srfor, "
					+ "r381_srfor, r399_srfor, r401_srfor, r403_srfor, r405_srfor, r407_srfor, "
					+ "r349_srfor, activo_srfor, generado_liquidacion_srfor, ide_gtemp,ide_geedp,ide_srimr,ruc_srfor)" + 
			  		" values( " +codigo + ", "
				  		+ ""+ r301_srfor+", "
				  		+ ""+r303_srfor+", "
				  		+ ""+r305_srfor+", "
				  		+ ""+r307_srfor+", "
				  		+ ""+r311_srfor+", "
				  		+ ""+r313_srfor+", "
				  		+ ""+r315_srfor+", "
				  	    + ""+ r317_srfor+", "
				  		+ ""+r351_srfor+", "
				  		+ ""+r353_srfor+", "
				  		+ ""+r361_srfor+", "
				  		+ ""+r363_srfor+", "
				  		+ ""+r365_srfor+", "
				  		+ ""+r367_srfor+", "
				  		+ ""+ r369_srfor+", "
				  		+ ""+r371_srfor+", "
				  		+ ""+r373_srfor+", "
				  		+ ""+r381_srfor+", "
				  		+ ""+r399_srfor+", "
				  		+ ""+r401_srfor+", "
				  		+ ""+r403_srfor+", "
				  	    + ""+ r405_srfor+", "
				  		+ ""+r407_srfor+", "
				  		+ ""+r349_srfor+", "
				  		+ ""+activo_srfor+", "
				  		+ ""+generado_liquidacion_srfor+", "
				  	    + ""+ ide_gtemp+", "
				  	    + ""+ ide_geedp+", "
				  		+ ""+ide_srimr+", "
	     		  		+ "'"+ruc+"')"); 
		 
	}


	 	public String servicioCodigoMaximo(String tabla,String ide_primario){
	 		
	 		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
	 		return maximo;
	 	}

	

}
