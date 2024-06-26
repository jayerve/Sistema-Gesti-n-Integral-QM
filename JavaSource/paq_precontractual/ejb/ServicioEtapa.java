package paq_precontractual.ejb;

import javax.ejb.Stateless;

@Stateless
public class ServicioEtapa {
	public ServicioEtapa(){
		
	}
	
	public String getEtapa(Integer ide_etapa){
	    String tab_ruta="select ide_preta,descripcion_preta,requiere_aprobacion_preta,coalesce(bien_servicio_preta,'NA') as bien_servicio_preta,opcional_preta  from precon_etapa where ide_preta = "+ide_etapa+" and activo_preta=true;";
	    return tab_ruta;
	}
	
	public String getEtapaTareas(int ide_preta){
	    String tab_etapa="select ide_preta,tarea_preta,notas_preta,plantilla_preta,descripcion_preta from precon_etapa where ide_preta = "+ide_preta+" ;";
	    return tab_etapa;
	}
	

}
