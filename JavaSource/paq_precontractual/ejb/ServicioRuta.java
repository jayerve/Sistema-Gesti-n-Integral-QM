package paq_precontractual.ejb;

import javax.ejb.Stateless;

@Stateless
public class ServicioRuta {
	public ServicioRuta(){
		
	}
	
	public String getRuta(Integer ide_actividad){
	    String tab_ruta="select pre_ide_pretp2 from precon_ruta where pre_ide_pretp="+ide_actividad+" and  activo_prrut=true;";
	    return tab_ruta;
	}

}
