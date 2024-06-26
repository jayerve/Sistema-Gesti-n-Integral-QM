package paq_contabilidad;

import java.util.ArrayList;
import java.util.List;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_grupo_cuenta extends Pantalla{
	private Tabla tab_grupo_cuenta=new Tabla();
	
	public pre_grupo_cuenta(){
		tab_grupo_cuenta.setId("tab_grupo_cuenta");
		tab_grupo_cuenta.setTabla("cont_grupo_cuenta", "ide_cogrc", 1);
		
        tab_grupo_cuenta.dibujar();
        PanelTabla pat_grupo_cuenta=new PanelTabla();
		pat_grupo_cuenta.setPanelTabla(tab_grupo_cuenta);
		
		agregarComponente(pat_grupo_cuenta);
	}
	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_grupo_cuenta.insertar();
		
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_grupo_cuenta.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_grupo_cuenta.eliminar();
		
	}


	public Tabla getTab_grupo_cuenta() {
		return tab_grupo_cuenta;
	}


	public void setTab_grupo_cuenta(Tabla tab_grupo_cuenta) {
		this.tab_grupo_cuenta = tab_grupo_cuenta;
	}

}
