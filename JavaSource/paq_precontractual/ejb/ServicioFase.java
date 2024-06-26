package paq_precontractual.ejb;

import javax.ejb.Stateless;

@Stateless
public class ServicioFase {
	public ServicioFase(){
		
	}
	
	public String getFase(Integer ide_prpre){
	    String tab_fase="select ide_prfas,descripcion_prfas from precon_fase where ide_prfas=(select ide_prfas from precon_procedimiento where ide_prpro=(select ide_prpro from precon_etapa_procedimiento where ide_pretp=(select ide_pretp from precon_precontractual where ide_prpre="+ide_prpre+")));";
	    return tab_fase;
	}

}
