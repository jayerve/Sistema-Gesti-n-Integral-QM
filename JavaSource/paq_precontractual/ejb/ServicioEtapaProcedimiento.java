package paq_precontractual.ejb;

import javax.ejb.Stateless;

@Stateless
public class ServicioEtapaProcedimiento {
	public ServicioEtapaProcedimiento(){
		
	}
	
	public String getProcedimientos(){
	    String tab_procedimiento="select distinct pep.ide_prpro as ide_pretp,(descripcion_prcon || ' - ' || descripcion_prpro || ' - ') as descripcion,codigo_secuencia_prpro from precon_procedimiento pp,precon_contratacion pc,precon_etapa_procedimiento pep where pp.ide_prcon=pc.ide_prcon and pep.ide_prpro=pp.ide_prpro and activo_prpro=true order by pep.ide_prpro ";
	    return tab_procedimiento;
	}
	public String getProcedimientos(Integer ide_pretp){
	    String tab_procedimiento="select distinct pep.ide_prpro as ide_pretp,(descripcion_prcon || ' - ' || descripcion_prpro) as descripcion from precon_procedimiento pp,precon_contratacion pc,precon_etapa_procedimiento pep where pp.ide_prcon=pc.ide_prcon and pep.ide_prpro=pp.ide_prpro and pep.ide_prpro="+ide_pretp+" and activo_prpro=true;";
	    return tab_procedimiento;
	}
	
	public String getEtapasProcedimientos(Integer ide_prpro ){
		String tab_etapa_procedimiento="select ide_pretp,descripcion_preta from precon_etapa_procedimiento pep,precon_etapa pet where activo_preta=true and activo_pretp=true and pep.ide_preta=pet.ide_preta and pep.ide_prpro="+ide_prpro+" order by descripcion_preta;";
	    return tab_etapa_procedimiento;
	}
	
	public String getTodasEtapasProcedimientos(){
		String tab_etapa_procedimiento="select ide_pretp,descripcion_preta from precon_etapa_procedimiento pep,precon_etapa pet where activo_pretp=true and pep.ide_preta=pet.ide_preta order by descripcion_preta;";
	    return tab_etapa_procedimiento;
	}
	
	public String getEtapaInicioSeguimiento(String inicioSeguimiento,Integer ide_preta){
		String tab_etapa_incio_seguimiento="select pet.ide_preta,descripcion_preta from precon_etapa_procedimiento pep,precon_etapa pet where pep.ide_preta=pet.ide_preta and pet.iniciosegumiento_preta='"+inicioSeguimiento+"' and pet.ide_preta="+ide_preta+" and pep.activo_pretp=true;";
		return tab_etapa_incio_seguimiento;
	}
	
	public String getEtapaInicioSeguimientoParalelo(String inicioSeguimiento){
		String tab_etapa_incio_seguimiento="select ide_pretp,descripcion_preta,descripcion_prpro,descripcion_prfas "
											+" from precon_etapa_procedimiento pep "
											+" join precon_procedimiento ppr on ppr.ide_prpro=pep.ide_prpro "
											+" join precon_fase pfa on pfa.ide_prfas=ppr.ide_prfas "
											+" join precon_etapa pet on pep.ide_preta=pet.ide_preta "
											+" where pet.iniciosegumiento_preta='"+inicioSeguimiento+"' and pep.activo_pretp=true;";
		System.out.println("getEtapaInicioSeguimientoParalelo: "+tab_etapa_incio_seguimiento);
		return tab_etapa_incio_seguimiento;
	}
	
	public String getEtapaSiguiente(Integer ide_procedimiento,Integer ide_actividad_actual, String tipoContratacion){
		String tab_etapa_siguiente="select pet.ide_preta,descripcion_preta from precon_etapa_procedimiento pep,precon_etapa pet where pep.ide_preta=pet.ide_preta and pep.activo_pretp='true' ";
				
		tab_etapa_siguiente += " and pep.ide_prpro="+ide_procedimiento+" and coalesce(pet.bien_servicio_preta,'NA') = (case when coalesce(pet.bien_servicio_preta,'NA')='NA' or coalesce(pet.bien_servicio_preta,'NA')='PG' then coalesce(pet.bien_servicio_preta,'NA') else '"+tipoContratacion+"' end) ";
				
				tab_etapa_siguiente	+= " and pep.ide_pretp in (select pre_ide_pretp2 from precon_ruta where pre_ide_pretp=(select ide_pretp from precon_etapa_procedimiento where ide_preta="+ide_actividad_actual+" and ide_prpro="+ide_procedimiento+") and  activo_prrut=true) order by pet.ide_preta desc;";
		return tab_etapa_siguiente;
	}
}
