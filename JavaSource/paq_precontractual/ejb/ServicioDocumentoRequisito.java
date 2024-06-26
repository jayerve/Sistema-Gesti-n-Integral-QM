package paq_precontractual.ejb;

import javax.ejb.Stateless;

@Stateless
public class ServicioDocumentoRequisito {
	public ServicioDocumentoRequisito(){
		
	}
	
	public String getDocumentoRequisito(Integer ide_prpre){
	    String tab_requisito="select ide_prdoc,etapa_prdoc from precon_documento_requisito where ide_prpre="+ide_prpre+" and con_ide_prdoc is null and activo_prdoc=true;;";
	    return tab_requisito;
	}
}
