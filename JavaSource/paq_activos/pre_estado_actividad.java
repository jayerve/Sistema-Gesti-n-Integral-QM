package paq_activos;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

 
public class pre_estado_actividad extends Pantalla{
	
	private Tabla tab_estado_actividad=new Tabla();
	
	public Tabla gettab_estado_actividad() {
		return tab_estado_actividad;
	}

	public void settab_estado_actividad(Tabla tab_estado_actividad) {
		this.tab_estado_actividad = tab_estado_actividad;
	}

	public pre_estado_actividad() {
		tab_estado_actividad.setId("tab_estado_actividad");  
		tab_estado_actividad.setTabla("afi_actividad", "ide_afacd", 1);	
		tab_estado_actividad.dibujar();
		PanelTabla pat_estado_actividad=new PanelTabla();
		pat_estado_actividad.setPanelTabla(tab_estado_actividad);
		
		agregarComponente(pat_estado_actividad);
		
	}	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_estado_actividad.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_estado_actividad.guardar();
		guardarPantalla();			
	}
 
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_estado_actividad.eliminar();		
	}

}
