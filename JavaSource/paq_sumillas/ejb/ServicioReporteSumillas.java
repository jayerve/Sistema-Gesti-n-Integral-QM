package paq_sumillas.ejb;



import javax.ejb.Stateless;

/**
 * @author 
 *
 */
@Stateless
public class ServicioReporteSumillas {

	
	public String getDetalleSumillas (String fechaInicial, String fechaFinal ){
		String tab_sumillas= "SELECT ste.num_tramite_smtra, ste.num_documento_smtra, ste.num_hojas_smtra, ste.fecha_emision_doc_smtra," 
		+ "ste.fecha_recep_doc_smtra, ste.remitente_smtra, ste.destinatario_regdes, ste.asunto_smtra, "
		+ "str.fecha_sumilla_smtre, str.disposicion_smtre, "
		+ "ger.detalle_geare as gerencia, cor.detalle_geare as coordinacion,spr.nombre_sumpr, str.recibido_smtre," 
		+ "str.fecha_entrega_smtre, str.fecha_vencimiento_smtre, "
		+ "str.fecha_documento_smtre, str.num_documento_smtre, str.destinatario_regdes, str.cargo_destinatario_smtre, str.asunto_smtre," 
		+ "str.fecha_entrega_res_smtre, str.fecha_limite_smtre, str.fecha_limite_prog_smtre, "
		+ "((case when str.fecha_vencimiento1_smtre is not null then 1 else 0 end)+ "
		+ "(case when str.fecha_vencimiento2_smtre is not null then 1 else 0 end)+ "
		+ "(case when str.fecha_vencimiento3_smtre is not null then 1 else 0 end)+ "
		+ "(case when str.fecha_vencimiento4_smtre is not null then 1 else 0 end)) as prorrogas, "
		+ "ses.nombre_suesu as estado_sumilla,str.gabinete_smtre "
		+ "FROM public.sum_tramite_respuesta str "
		+ "left join sum_tramite ste "
		+ "on str.ide_smtra = ste.ide_smtra "
		+ "left join gen_area ger "
		+ "on str.ide_geareg = ger.ide_geare "
		+ "left join gen_area cor "
		+ "on str.ide_gearec = cor.ide_geare "
		+ "left join sum_estado_sumilla ses "
				+ "on str.ide_suesu = ses.ide_suesu "
		+ "left join sum_prioridad spr "
		+ "on str.ide_sumpr = spr.ide_sumpr "
		+ "where ste.fecha_recep_doc_smtra between '"+ fechaInicial +"' and '"+fechaFinal+"'";
		//+ "where ste.fecha_recep_doc_smtra between to_date('"+ fechaInicial +"', 'DD/MM/YYYY') and to_date('"+fechaFinal+"', 'DD/MM/YYYY')";
				
		return tab_sumillas;

		}

	public String getDetalleAlertas(String fechaini,String fechafin){
		return "select 'FUTURA' semaforo,fecha_limite_smtre, (fecha_limite_smtre - CURRENT_DATE) faltan, num_tramite_smtra from sum_tramite_respuesta left join sum_tramite "
				+ "on sum_tramite_respuesta.ide_smtra = sum_tramite.ide_smtra "
				+ "where fecha_limite_smtre - CURRENT_DATE > 15 and ide_suesu in (1,3) and (fecha_sumilla_smtre between to_date('"+ fechaini +"', 'DD/MM/YYYY') and to_date('"+fechafin+"', 'DD/MM/YYYY')) "
				+ "union "
				+ "select 'INMEDIATA' semaforo,fecha_limite_smtre,(fecha_limite_smtre - CURRENT_DATE) faltan, num_tramite_smtra from sum_tramite_respuesta left join sum_tramite "
				+ "on sum_tramite_respuesta.ide_smtra = sum_tramite.ide_smtra "
				+ "where (fecha_limite_smtre - CURRENT_DATE > 6 and fecha_limite_smtre - CURRENT_DATE < 16) and ide_suesu in (1,3) and (fecha_sumilla_smtre between to_date('"+ fechaini +"', 'DD/MM/YYYY') and to_date('"+fechafin+"', 'DD/MM/YYYY')) "
				+ "union "
				+ "select 'URGENTE' semaforo,fecha_limite_smtre,(fecha_limite_smtre - CURRENT_DATE) faltan, num_tramite_smtra from sum_tramite_respuesta left join sum_tramite "
				+ "on sum_tramite_respuesta.ide_smtra = sum_tramite.ide_smtra "
				+ "where fecha_limite_smtre - CURRENT_DATE < 5 and ide_suesu in (1,3) and (fecha_sumilla_smtre between to_date('"+ fechaini +"', 'DD/MM/YYYY') and to_date('"+fechafin+"', 'DD/MM/YYYY'))";
	}

	public String getDetalleEstado(String fechaini,String fechafin){
		return "select fecha_sumilla_smtre,gerencia,cordinacion,cumplido,incumplido,en_proceso, (cumplido+incumplido+en_proceso) as asignado from ( "
			+"select fecha_sumilla_smtre,nombre_suesu,gerencia,cordinacion,sum(case when ide_suesu = 2 then sumillas else 0 end) as cumplido, " 
			+"sum(case when ide_suesu = 3 then sumillas else 0 end) as incumplido,  "
			+"sum(case when ide_suesu = 1 then sumillas else 0 end) as en_proceso "
			+"from "
			+"(select  distinct str.fecha_sumilla_smtre,str.ide_suesu,ses.nombre_suesu, "
				+"gag.detalle_geare as gerencia, "
			    +"COALESCE(gac.detalle_geare,'') as cordinacion, "
			    +"count(str.ide_smtre) over (partition by (to_char(str.fecha_sumilla_smtre, 'YYYY-MM-DD')||str.ide_suesu||str.ide_geareg||COALESCE(str.ide_gearec,-1))) as sumillas " 
			+"from sum_tramite_respuesta str "
			+"left join gen_area gag "
			+"on str.ide_geareg = gag.ide_geare "
			+"left join gen_area gac "
			+"on str.ide_gearec = gac.ide_geare "
			+"left join sum_estado_sumilla ses "
			+"on str.ide_suesu = ses.ide_suesu "
			+"where str.fecha_sumilla_smtre between '"+ fechaini +"' and '"+ fechafin +"') v1 "
			+"group by fecha_sumilla_smtre,nombre_suesu,gerencia,cordinacion)v2";
	}
	
	public String getPorcentajePeriodo(String fechaini, String fechafin){
		return "select nombre_suesu,sum(sumillas) from ( "
				+"select  distinct str.fecha_sumilla_smtre,str.ide_suesu,ses.nombre_suesu, "
				+"gag.detalle_geare as gerencia, "
			    +"COALESCE(gac.detalle_geare,'') as cordinacion, "
			    +"count(str.ide_smtre) over (partition by (to_char(str.fecha_sumilla_smtre, 'YYYY-MM-DD')||str.ide_suesu||str.ide_geareg||COALESCE(str.ide_gearec,-1))) as sumillas " 
				+"from sum_tramite_respuesta str "
				+"left join gen_area gag "
				+"on str.ide_geareg = gag.ide_geare "
				+"left join gen_area gac "
				+"on str.ide_gearec = gac.ide_geare "
				+"left join sum_estado_sumilla ses "
				+"on str.ide_suesu = ses.ide_suesu "
				+ "where str.fecha_sumilla_smtre between '"+ fechaini +"' and '"+ fechafin +"') v1 "
				+"group by nombre_suesu";
	}

}
