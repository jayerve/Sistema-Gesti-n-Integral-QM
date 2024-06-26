package paq_precontractual.ejb;

import javax.ejb.Stateless;

@Stateless
public class ServicioEtapaRequisito { 
	public ServicioEtapaRequisito(){
		
	}
	
	public String getContarEtapaRequisitoPorPrecontractual(Integer ide_prpre,Integer ide_preta){
	    String tab_contar_requisito="select pdr.ide_prdoc,pdr.ide_prreq,presenta_prdoc from precon_documento_requisito pdr,precon_requisito pr, precon_etapa pe where pdr.ide_prreq=pr.ide_prreq and pr.ide_preta=pe.ide_preta and ide_prpre="+ide_prpre+" and pr.ide_preta="+ide_preta+" and pdr.con_ide_prdoc is null and activo_prdoc=true;";
	    return tab_contar_requisito;
	}

	public String noRegistradoEnRequisitoPrecontractualYEtapa(Integer ide_prpre,Integer ide_preta,Integer ide_prreq){
	    String tab_contar_requisito="select pr.ide_prreq,descripcion_prreq from precon_requisito pr,precon_etapa pe,precon_documento_requisito pdr where pr.ide_preta=pe.ide_preta and pdr.ide_prreq=pr.ide_prreq and pr.ide_preta="+ide_preta+" and pdr.con_ide_prdoc is null and pr.ide_prreq in (select ide_prreq from precon_documento_requisito where ide_prpre="+ide_prpre+" and con_ide_prdoc is null and ide_prreq="+ide_prreq+" and activo_prdoc=true); ";
	    return tab_contar_requisito;
	}
	public String getEtapaConRequisito(Integer ide_preta){
		String etapa_conrequisito="select re.ide_prreq,descripcion_prreq from precon_etapa et,precon_requisito re where re.ide_preta=et.ide_preta and et.ide_preta = "+ide_preta+" and et.activo_preta=true;";
		return etapa_conrequisito;
	}
}
