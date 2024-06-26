package paq_precontractual.ejb;

import javax.ejb.Stateless;

@Stateless
public class ServicioRequisito {
	public ServicioRequisito(){
		
	}
	
	public String getRequisito(){
	    String tab_requisito="select ide_prreq,descripcion_prreq from precon_requisito where activo_prreq=true;";
	    return tab_requisito;
	}
	
	public String getRequisitoActividad(int ide_prfas){
	    String tab_requisito="select distinct ide_prreq,descripcion_preta,descripcion_prfas "
							+ "	from precon_requisito r "
							+ "	join precon_etapa e on e.ide_preta=r.ide_preta "
							+ "	join precon_etapa_procedimiento ep on ep.ide_preta=e.ide_preta "
							+ "	join precon_procedimiento p on p.ide_prpro=ep.ide_prpro"
							+ "	join precon_fase f on f.ide_prfas=p.ide_prfas "
							+ "	where activo_prreq=true and activo_preta=true ";	
		if(ide_prfas>0)			
			tab_requisito += " and f.ide_prfas="+ide_prfas;								
		tab_requisito += "	order by 2 ";
		
		System.out.println("getRequisitoActividad "+tab_requisito);
	    return tab_requisito;
	}
	
	public String getRequisitoPorEtapa(Integer ide_preta){
	    String tab_contar_requisito="select ide_prreq,descripcion_prreq from precon_requisito where ide_preta="+ide_preta+" and activo_prreq=true;";
	    return tab_contar_requisito;
	}
	public String getRequisitoUpdateParalelo(Integer ide_prpre_seleccionado,Integer ide_prpre_actual){
		String tab_contar_requisito="update precon_documento_requisito set ide_prpre = "+ide_prpre_actual+" where ide_prpre="+ide_prpre_seleccionado+";";
	    return tab_contar_requisito;
	}
}
