package paq_seguimiento.ejb;

import javax.ejb.Stateless;

import paq_sistema.aplicacion.Utilitario;
import framework.aplicacion.TablaGenerica;

@Stateless
public class ServicioSeguimientoPlan {
	
	private Utilitario utilitario= new Utilitario();
	
	public String getSeguimientoPlan(String fecha_inicial,String fecha_final){
		// Fecha final para sacar los datos del periodo
		
		    	
			  
   	 TablaGenerica tab_recomendaciones_eva = utilitario.consultar("SELECT ide_serec,ide_seeva "
 			+ "FROM seg_detalle_evaluacion  "
 			+ "where ide_seeva in(4) and "
 			+ " ide_seesr not in(2)"
 			);
 	String ide_serec="",ide_serec_fal="";
		if (tab_recomendaciones_eva.getTotalFilas()>0) {
			for (int i = 0; i < tab_recomendaciones_eva.getTotalFilas(); i++) {
				if (tab_recomendaciones_eva.getTotalFilas()==1) {
					ide_serec=tab_recomendaciones_eva.getValor(i,"ide_serec");
				}else if((tab_recomendaciones_eva.getTotalFilas()-1)==i){
					ide_serec+=tab_recomendaciones_eva.getValor(i,"ide_serec");
				}else{
				ide_serec+=tab_recomendaciones_eva.getValor(i,"ide_serec")+",";
				}
			}
		}
		
		
 	TablaGenerica tab_recomendaciones_fal =utilitario.consultar("select ide_serec,ide_seinf from seg_recomendacion where ide_seinf in("+utilitario.getVariable("p_informe_seguimiento_recomendacion")+")");
 	if (tab_recomendaciones_fal.getTotalFilas()>0) {
			for (int i = 0; i < tab_recomendaciones_fal.getTotalFilas(); i++) {
				if (tab_recomendaciones_fal.getTotalFilas()==1) {
					ide_serec_fal=tab_recomendaciones_fal.getValor(i,"ide_serec");
				}else if((tab_recomendaciones_fal.getTotalFilas()-1)==i){
					ide_serec_fal+=tab_recomendaciones_fal.getValor(i,"ide_serec");
				}else{
					ide_serec_fal+=tab_recomendaciones_fal.getValor(i,"ide_serec")+",";
				}
			}
		}		    
			 
			    	
			    	String sql = "select DISTINCT inf.ide_seinf, "
			    			+ "susinf.descripcion_sesui, "
			    			+ "inf.numero_seinf, "
			    			+ "inf.fecha_aprobacion_seinf, "
			    			+ "inf.fecha_inicio_seinf, "
			    			+ "rec.numero_serec, "
			    			+ "rec.asunto_serec, "
			    			+ "upper(resp.descripcion_seres) as descripcion_seres, "
			    			+ "upper(resp.medio_verificacion_seres) as medio_verificacion_seres, "
			    			+ "plan.ide_sepla, "
			    			+ "resp.ide_seres, "
			    			+ "resp.mecanismo_reporte_seres, "
			    			+ "mecanismo_reporte_seres_enlace, "
			    			+ "resp.fecha_desde_seres, "
			    			+ "resp.fecha_hasta_seres, "
			    			+ "tiprec.descripcion_setir, "
			    			+ "tresp.descripcion_setre, "
			    			+ "CASE WHEN  (select fecha_sehmc from seg_historial_mecanismo_control where ide_seres=resp.ide_seres and activo_sehmc=true )  IS NULL "
			    			+ "then CASE WHEN resp.fecha_actua IS NULL  THEN plan.fecha_registro_sepla  ELSE resp.fecha_actua END "
			    			+ "else (select fecha_sehmc from seg_historial_mecanismo_control where ide_seres=resp.ide_seres and activo_sehmc=true)  end as fecha_medio_control, "
			    			
			    			
			    			+ "EXTRACT (MONTH FROM CASE WHEN  (select fecha_sehmc from seg_historial_mecanismo_control where ide_seres=resp.ide_seres and activo_sehmc=true )  IS NULL "
			    			+ "then CASE WHEN resp.fecha_actua IS NULL  THEN plan.fecha_registro_sepla  ELSE resp.fecha_actua END   "
			    			+ "else (select fecha_sehmc from seg_historial_mecanismo_control where ide_seres=resp.ide_seres and activo_sehmc=true)  end)::integer as mes_mecanismo_control, "
			    			
			    			
			    			+ "EXTRACT (YEAR FROM CASE WHEN  (select fecha_sehmc from seg_historial_mecanismo_control where ide_seres=resp.ide_seres and activo_sehmc=true )  IS NULL  "
			    			+ "then CASE WHEN resp.fecha_actua IS NULL  THEN plan.fecha_registro_sepla  ELSE resp.fecha_actua END "
			    			+ "else (select fecha_sehmc from seg_historial_mecanismo_control where ide_seres=resp.ide_seres and activo_sehmc=true)  end)::integer as anio_mecanismo_control,		"
			    			
			    			+ "plan.fecha_registro_sepla, "
			    			+ "CASE WHEN resp.fecha_seres <='2022-05-01' THEN emp.descripcion_seemp  "
			    			+ "WHEN  tresp.ide_setre=3 THEN 'RESPONSABLE DE SEGUIMIENTO' ELSE emp_plan.descripcion_seemp END as area, "
			    			+ "CASE WHEN resp.fecha_seres <='2022-05-01' THEN usuario_respuesta.nombre_seusu  "
			    			+ "WHEN tresp.ide_setre=3 AND resp.fecha_seres <='2023-05-31' THEN 'ARGOTI VASQUEZ DAVID SEBASTIAN'     "
			    			+ "WHEN tresp.ide_setre=3 AND resp.fecha_seres >'2023-05-31' THEN 'BURNEO DELGADO SANTIAGO ANDRES' ELSE  usuario_plan.nombre_seusu END as nombre_usuario, "
			    			+ "per.descripcion_seper, "
			    			+ "EXTRACT (YEAR FROM plan.fecha_registro_sepla)::integer as year_seres, "
			    			+ " plan.reprogramacion_total_sepla, "
			    			+ "CASE WHEN  tresp.ide_setre=3  THEN resp.fecha_seres else plan.fecha_registro_sepla end as fecha_registro "
			    			+ "from seg_plan_accion plan  "
			    			+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf   "
			    			+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec AND inf.ide_seinf=plan.ide_seinf  "
			    			+ "LEFT JOIN seg_respuesta resp on resp.ide_serec=rec.ide_serec  "
			    			+ "and resp.ide_seinf=rec.ide_seinf AND plan.ide_sepla = resp.ide_sepla  "
			    			+ "left join seg_asignacion asi on asi.ide_seinf=plan.ide_seinf  "
			    			+ "and asi.ide_serec=rec.ide_serec   "
			    			+ "left join seg_estado_plan_accion est on est.ide_seesp=plan.ide_seesp  "
			    			+ "left join seg_empresa emp on emp.ide_seemp=resp.ide_seemp  "
			    			+ "left join seg_empresa emp_plan on emp_plan.ide_seemp=plan.ide_seemp  "
			    			+ "LEFT JOIN seg_suscribe_informe susinf ON inf.ide_sesui = susinf.ide_sesui  "
			    			+ "LEFT JOIN seg_estado_recomendacion estrec ON estrec.ide_seesr = rec.ide_seesr  "
			    			+ "LEFT JOIN seg_tipo_recomendacion tiprec ON rec.ide_setir = tiprec.ide_setir  "
			    			+ "LEFT JOIN seg_tipo_respuesta tresp on tresp.ide_setre = resp.ide_setre  "
			    			+ "LEFT JOIN seg_periocidad per on per.ide_seper=plan.ide_seper  "
			    			+ "LEFT JOIN seg_detalle_evaluacion deva on deva.ide_seinf=plan.ide_seinf   "
			    			+ "and deva.ide_serec=plan.ide_serec AND deva.ide_seeva IN (4) AND deva.ide_seesr NOT IN (2)"
			    			+ "LEFT JOIN seg_estado_recomendacion estrec1 ON estrec1.ide_seesr =deva.ide_seesr  "
			    			+ "LEFT JOIN seg_usuario usuario_respuesta ON usuario_respuesta.ide_seusu = resp.ide_seusu  "
			    			+ "LEFT JOIN seg_usuario usuario_plan ON usuario_plan.ide_seemp = emp_plan.ide_seemp and usuario_plan.ide_seusu=plan.ide_seusu "//  usuario_plan.activo_seusu=true  "
			    			//+ "LEFT JOIN seg_historial_mecanismo_control sehmc ON sehmc.ide_seres = resp.ide_seres and sehmc.activo_sehmc=true "
			    			+ "where resp.ide_sepla is not null  "
			    			+ "AND plan.IDE_SEREC IN  ("+ide_serec+","+ide_serec_fal+")   "
			    			+ "AND plan.fecha_registro_sepla BETWEEN '"+fecha_inicial+"' and  '"+fecha_final+"' "
			    			+ "order by inf.numero_seinf asc, "
			    			+ "         plan.ide_sepla ASC, "
			    			+ "rec.numero_serec asc,  "
			    			+ "         resp.fecha_desde_seres asc, "
			    			+ "resp.fecha_hasta_seres asc";
			    	
			   	
		    
		System.out.println("consulta plan accion: "+ sql);
		return sql;
	}
	
	

}



