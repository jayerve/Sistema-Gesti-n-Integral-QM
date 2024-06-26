package paq_contabilidad;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

 
public class pre_clase_viaje extends Pantalla{
	
	private Tabla tab_clase_viaje=new Tabla();
	
	public Tabla gettab_clase_viaje() {
		return tab_clase_viaje;
	}

	public void settab_clase_viaje(Tabla tab_clase_viaje) {
		this.tab_clase_viaje = tab_clase_viaje;
	}

	public pre_clase_viaje() {
		tab_clase_viaje.setId("tab_clase_viaje");  
		tab_clase_viaje.setTabla("cont_clase_viaje", "ide_coclv", 1);	
		tab_clase_viaje.dibujar();
		PanelTabla pat_asunto_viaje=new PanelTabla();
		pat_asunto_viaje.setPanelTabla(tab_clase_viaje);
		
		agregarComponente(pat_asunto_viaje);
		
	}	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_clase_viaje.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_clase_viaje.guardar();
		guardarPantalla();			
	}
 
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_clase_viaje.eliminar();		
	}

}
