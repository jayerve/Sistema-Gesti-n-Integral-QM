package paq_precontractual.ejb;

import javax.ejb.Stateless;

@Stateless
public class ServicioSeguimiento {
	public ServicioSeguimiento(){
		
	}
	
	public String getSeguimiento(){
	    String tab_seguimiento="select ide_prseg,fecha_asignacion_prseg,hora_asignacion_prseg,fecha_cambio_prseg,hora_cambio_prseg,etapa_inicio_prseg,responsable_prseg,departamento_prseg,etapa_fin_prseg,responsable_asignado_prseg,departamento_asignado_prseg,observacion_prseg,estado_actividad_prseg,estado_procedimiento_prseg,activo_prseg from precon_seguimiento where activo_prseg=true;";
	    return tab_seguimiento;
	}
	
	public String getSeguimientoPorPrecontractual(Integer ide_prpre){
	    String tab_seguimiento="select ide_prseg,fecha_asignacion_prseg,hora_asignacion_prseg,fecha_cambio_prseg,hora_cambio_prseg,etapa_inicio_prseg,responsable_prseg,departamento_prseg,etapa_fin_prseg,responsable_asignado_prseg,departamento_asignado_prseg,observacion_prseg,estado_actividad_prseg,estado_procedimiento_prseg,activo_prseg,ide_preta_prseg from precon_seguimiento where ide_prpre="+ide_prpre+" and activo_prseg=true;";
	    return tab_seguimiento;
	}
	
	public String getSeguimientoUpdateParalelo(Integer ide_prpre_seleccionado,Integer ide_prpre_actual){
		String tab_seguimiento="update precon_seguimiento set ide_prpre = "+ide_prpre_actual+" where ide_prpre="+ide_prpre_seleccionado+"";
	    return tab_seguimiento;
	}
		
}
