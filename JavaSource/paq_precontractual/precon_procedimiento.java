package paq_precontractual;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class precon_procedimiento extends Pantalla{
	private Tabla tab_procedimiento=new Tabla();
	
	public precon_procedimiento(){
		tab_procedimiento.setId("tab_procedimiento");
		tab_procedimiento.setHeader("CONTRATACIÓN PROCEDIMIENTO ");
		tab_procedimiento.setTabla("precon_procedimiento","ide_prpro",1);
		
		//tab_procedimiento.setTipoFormulario(true);  //formulario 
		//tab_procedimiento.getGrid().setColumns(2); //hacer  columnas
		tab_procedimiento.getColumna("ide_prpro");
		tab_procedimiento.getColumna("ide_prpro").setNombreVisual("Código");
		tab_procedimiento.getColumna("ide_prcon").setCombo("precon_contratacion", "ide_prcon","descripcion_prcon", "activo_prcon=true");
		tab_procedimiento.getColumna("ide_prcon").setNombreVisual("Contratación");
		tab_procedimiento.getColumna("ide_prcon").setRequerida(true);
		tab_procedimiento.getColumna("ide_prfas").setCombo("precon_fase", "ide_prfas","descripcion_prfas", "activo_prfas=true");
		tab_procedimiento.getColumna("ide_prfas").setNombreVisual("Fase");
		tab_procedimiento.getColumna("ide_prfas").setRequerida(true);
		tab_procedimiento.getColumna("ide_adtic").setCombo("adq_tipo_contratacion","ide_adtic", "detalle_adtic","");
		tab_procedimiento.getColumna("codigo_secuencia_prpro");
		tab_procedimiento.getColumna("codigo_secuencia_prpro").setNombreVisual("Código Secuencia");
		tab_procedimiento.getColumna("descripcion_prpro");
		tab_procedimiento.getColumna("descripcion_prpro").setNombreVisual("Nombre");
		tab_procedimiento.getColumna("monto_inferior_prpro");
		tab_procedimiento.getColumna("monto_inferior_prpro").setNombreVisual("Monto Inferior");
		tab_procedimiento.getColumna("monto_superior_prpro");
		tab_procedimiento.getColumna("monto_superior_prpro").setNombreVisual("Monto Superior");
		tab_procedimiento.getColumna("monto_superior_prpro").setRequerida(true);
		tab_procedimiento.getColumna("activo_prpro");
		tab_procedimiento.getColumna("activo_prpro").setNombreVisual("ACTIVO");
		tab_procedimiento.getColumna("activo_prpro").setValorDefecto("true");
		
		tab_procedimiento.dibujar();
        PanelTabla pat_procedimiento=new PanelTabla();
        pat_procedimiento.setPanelTabla(tab_procedimiento);
		

		Division div_division = new Division();
		div_division.dividir1(pat_procedimiento);
		agregarComponente(div_division);
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
	}
	
	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_procedimiento.guardar()){
			guardarPantalla();
		}
	}
	
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_procedimiento() {
		return tab_procedimiento;
	}

	public void setTab_procedimiento(Tabla tab_procedimiento) {
		this.tab_procedimiento = tab_procedimiento;
	}
}
