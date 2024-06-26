package paq_precontractual.ejb;

import javax.ejb.Stateless;

@Stateless
public class ServicioProcedimiento {
	public ServicioProcedimiento(){
		
	}
	
	public String getProcedimientos(){
	     String tab_procedimiento="select ide_prpro,(descripcion_prcon || ' - ' || descripcion_prpro) as descripcion from precon_procedimiento pp,precon_contratacion pc where pp.ide_prcon=pc.ide_prcon and activo_prpro=true;";
	    return tab_procedimiento;
	}
	
	public String getProcedimientoContratacion(){
	    String tab_procedimiento="select ide_prpro,(descripcion_prcon || ' - ' || descripcion_prpro) as descripcion,monto_inferior_prpro,monto_superior_prpro from precon_procedimiento pp ,precon_contratacion pc where pp.ide_prcon=pc.ide_prcon and ide_prfas=2 and activo_prpro=true order by ide_prpro;";
	    return tab_procedimiento;
	}
	public String getReporteProcedimientoContratacion(){
	    String tab_procedimiento="select ide_prpro,(descripcion_prcon || ' - ' || descripcion_prpro) as descripcion from precon_procedimiento pp ,precon_contratacion pc where pp.ide_prcon=pc.ide_prcon and activo_prpro=true order by ide_prpro;";
	    return tab_procedimiento;
	}
}
