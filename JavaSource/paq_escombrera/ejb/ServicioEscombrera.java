package paq_escombrera.ejb;


import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import paq_sistema.aplicacion.Utilitario;
import framework.aplicacion.TablaGenerica;


/**
 * Session Bean implementation class ServicioEscombrera
 */

@Stateless
@LocalBean
public class ServicioEscombrera {

	private Utilitario utilitario = new Utilitario();
	
	public String getCapitulos(String activo) {
		String query = "SELECT hijo.ide_apcap, "
			+"  hijo.cat_codigo_apcap as codigo, "
			+"  hijo.descripcion_apcap, "
			+"  padre.descripcion_apcap, "
			+"  abuelo.descripcion_apcap "
			+" FROM apu_capitulo hijo  "
			+"  LEFT JOIN apu_capitulo padre ON hijo.con_ide_apcap=padre.ide_apcap "
			+"  LEFT JOIN apu_capitulo abuelo ON padre.con_ide_apcap=abuelo.ide_apcap "
			+"  WHERE hijo.activo_apcap = "+activo+" and padre.ide_apcap is not null "
			+"  ORDER BY hijo.ide_apcap";
	
		return query;

	}
	
	public String getRubros(String activo, String grupo) {
		String query = "SELECT hijo.ide_aprub, "
				+"  hijo.cat_codigo_aprub as codigo, "
				+"  hijo.descripcion_aprub, "
				+"  padre.descripcion_aprub, "
				+"  abuelo.descripcion_aprub "
				+" FROM apu_rubro hijo  "
				+"  LEFT JOIN apu_rubro padre ON hijo.con_ide_aprub=padre.ide_aprub "
				+"  LEFT JOIN apu_rubro abuelo ON padre.con_ide_aprub=abuelo.ide_aprub "
				+"  WHERE hijo.activo_aprub = "+activo+" and padre.ide_aprub is not null and abuelo.con_ide_aprub="+grupo
				+"  ORDER BY hijo.ide_aprub";
	
		return query;

	}
	
	public String getAPURubros(String activo) {
		String query = "SELECT ide_apitc, padre.descripcion_apcap, ch.descripcion_apcap, abreviatura_bounm, descripcion_apitc, total_costo_directo_apitc "
				+" FROM public.apu_item_construccion ic "
				+" left join apu_capitulo ch on ch.ide_apcap=ic.ide_apcap "
				+" LEFT JOIN apu_capitulo padre ON ch.con_ide_apcap=padre.ide_apcap "
				+" left join bodt_unidad_medida um on um.ide_bounm=ic.ide_bounm "
				+" where activo_apitc="+activo;
		return query;
	}
	
	public TablaGenerica getCodigoCapitulos(String ide_apcap) {
		
		String query="SELECT padre.ide_apcap, padre.cat_codigo_apcap || '.'||coalesce(total,1) as cod "
				+" FROM apu_capitulo padre  "
				+" LEFT JOIN (select con_ide_apcap, (count(*)+1) as total from apu_capitulo group by con_ide_apcap) hijo ON hijo.con_ide_apcap=padre.ide_apcap "
				+" WHERE padre.ide_apcap="+ ide_apcap;
		
		//System.out.println("getCodigoCapitulos "+query);
		
		return utilitario.consultar(query);
	}
	
	public TablaGenerica getCodigoRubro(String ide_aprub) {
		
		String query="SELECT padre.ide_aprub, padre.cat_codigo_aprub || '.'||coalesce(total,1) as cod"
				+" FROM apu_rubro padre "
				+" LEFT JOIN (select con_ide_aprub, (count(*)+1) as total from apu_rubro group by con_ide_aprub) hijo ON hijo.con_ide_aprub=padre.ide_aprub"
				+" WHERE padre.ide_aprub is not null and padre.ide_aprub="+ ide_aprub;
		
		//System.out.println("getCodigoRubro "+query);
		
		return utilitario.consultar(query);
	}

	public String getAPURubrosFULL(String activo, String ide_apitc, String listar) {
		String query = "SELECT ic.ide_apitc, padre.descripcion_apcap as capitulo, "
				+" ch.descripcion_apcap as sub_capitulo, abreviatura_bounm, "
				+" materiales,"
				+" equipos,"
				+" mano_obra,"
				+" transporte,"
				+" total_costo_directo_apitc,costo_total_apitc,ic.ide_bounm "
				+" FROM public.apu_item_construccion ic "
				+" left join apu_capitulo ch on ch.ide_apcap=ic.ide_apcap "
				+" LEFT JOIN apu_capitulo padre ON ch.con_ide_apcap=padre.ide_apcap "
				+" left join bodt_unidad_medida um on um.ide_bounm=ic.ide_bounm "
				+" left join (select ide_apitc, sum(coalesce(costo_total_apmat,0)) as materiales from apu_material group by ide_apitc) mat on mat.ide_apitc=ic.ide_apitc "
				+" left join (select ide_apitc, sum(coalesce(costo_apequ,0)) as equipos from apu_equipo group by ide_apitc) eq on eq.ide_apitc=ic.ide_apitc "
				+" left join (select ide_apitc, sum(coalesce(costo_apmao,0)) as mano_obra from apu_mano_obra group by ide_apitc) mo on mo.ide_apitc=ic.ide_apitc "
				+" left join (select ide_apitc, sum(coalesce(costo_total_aptra,0)) as transporte from apu_transporte group by ide_apitc) tr on tr.ide_apitc=ic.ide_apitc "
				+" where activo_apitc in ("+activo+") ";
		
		if(!ide_apitc.equals("-1"))
			query+=" and ic.ide_apitc in ("+ide_apitc+") ";
		
		if(!listar.equals("-1"))
			query+=" and ic.ide_apitc not in (select ide_apitc from apu_proyecto_detalle where ide_appry="+listar+" ) ";
		
		return query;
	}
	
	public String consultaPresupuesto(String fechaI, String fechaF) {
		String query = "SELECT pry.ide_appry, funcionario, fecha_appry, descripcion_appry, cat_codigo_apcap, "
				+" capitulo,sub_capitulo,detalle_bounm,cantidad_apprd,precio_apprd,costo_apprd, materiales, "
				+" equipos, mano_obra, transporte,total_costo_directo_apitc, costo_indirecto_apitc,costo_total_apitc as precio_unit, "
				+" total_costo_appry, activo_appry "

				+" FROM apu_proyecto pry "

				+"   left join ( select ide_gtemp, apellido_paterno_gtemp || ' ' || (case when apellido_materno_gtemp is null then '' else apellido_materno_gtemp end)  "
				+" 	      || ' ' || primer_nombre_gtemp || ' ' || (case when segundo_nombre_gtemp is null then '' else segundo_nombre_gtemp end) as funcionario, "
				+" 	      documento_identidad_gtemp from gth_empleado ) fu on fu.ide_gtemp=pry.ide_gtemp "

				+" left join apu_proyecto_detalle pryd on pryd.ide_appry=pry.ide_appry "
				+" left join bodt_unidad_medida um on um.ide_bounm=pryd.ide_bounm "
				+" left join (SELECT ic.ide_apitc, padre.descripcion_apcap as capitulo,  "
				+" 	 ch.descripcion_apcap as sub_capitulo, abreviatura_bounm,  "
				+" 	 ch.cat_codigo_apcap, "
				+" 	 materiales, "
				+" 	 equipos, "
				+" 	 mano_obra, "
				+" 	 transporte, "
				+" 	 total_costo_directo_apitc,costo_indirecto_apitc, "
				+" 	 costo_total_apitc,ic.ide_bounm  "
				+" 	 FROM public.apu_item_construccion ic  "
				+" 	 left join apu_capitulo ch on ch.ide_apcap=ic.ide_apcap  "
				+" 	 LEFT JOIN apu_capitulo padre ON ch.con_ide_apcap=padre.ide_apcap  "
				+" 	 left join bodt_unidad_medida um on um.ide_bounm=ic.ide_bounm  "
				+" 	 left join (select ide_apitc, sum(coalesce(costo_total_apmat,0)) as materiales from apu_material group by ide_apitc) mat on mat.ide_apitc=ic.ide_apitc  "
				+" 	 left join (select ide_apitc, sum(coalesce(costo_apequ,0)) as equipos from apu_equipo group by ide_apitc) eq on eq.ide_apitc=ic.ide_apitc  "
				+" 	 left join (select ide_apitc, sum(coalesce(costo_apmao,0)) as mano_obra from apu_mano_obra group by ide_apitc) mo on mo.ide_apitc=ic.ide_apitc  "
				+" 	 left join (select ide_apitc, sum(coalesce(costo_total_aptra,0)) as transporte from apu_transporte group by ide_apitc) tr on tr.ide_apitc=ic.ide_apitc) rub on rub.ide_apitc=pryd.ide_apitc "
				+" where fecha_appry between '"+fechaI+"' and '"+fechaF+"'";
		
		System.out.println("consultaPresupuesto "+query);
		return query;
	}
}
