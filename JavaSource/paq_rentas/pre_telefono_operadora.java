
package paq_rentas;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_telefono_operadora extends Pantalla {
	
	private Tabla tab_telefono_operadora = new Tabla();
	
	public pre_telefono_operadora(){
		
		tab_telefono_operadora.setId("tab_telefono_operadora");
		tab_telefono_operadora.setTabla("rec_telefono_operadora","ide_reteo",1);
		tab_telefono_operadora.dibujar();
		PanelTabla pat_telefono_operadora= new PanelTabla();
		pat_telefono_operadora.setPanelTabla(tab_telefono_operadora);
		
		agregarComponente(pat_telefono_operadora);
		
		
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_telefono_operadora.insertar();
		
		
	}

	

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_telefono_operadora.guardar();
		guardarPantalla();
		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_telefono_operadora.eliminar();
		
	}

	public Tabla getTab_telefono_operadora() {
		return tab_telefono_operadora;
	}

	public void setTab_telefono_operadora(Tabla tab_telefono_operadora) {
		this.tab_telefono_operadora = tab_telefono_operadora;
	}
	
}