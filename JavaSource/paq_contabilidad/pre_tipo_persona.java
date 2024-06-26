package paq_contabilidad;
import paq_sistema.aplicacion.Pantalla;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


 
public class pre_tipo_persona extends Pantalla{
	
	private Tabla tab_tipo_persona=new Tabla();
	
	
	public pre_tipo_persona() {
		tab_tipo_persona.setId("tab_tipo_persona");  
		tab_tipo_persona.setTabla("gen_tipo_persona", "ide_getip", 1);	
		tab_tipo_persona.dibujar();
		PanelTabla pat_tipo_persona=new PanelTabla();
		pat_tipo_persona.setPanelTabla(tab_tipo_persona);
		
		agregarComponente(pat_tipo_persona);
		
	}	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_persona.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_persona.guardar();
		guardarPantalla();			
	}
 
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_persona.eliminar();		
		}
	public Tabla gettab_tipo_persona() {
		return tab_tipo_persona;
	}

	public void settab_tipo_persona(Tabla tab_tipo_persona) {
		this.tab_tipo_persona = tab_tipo_persona;
	}

}
