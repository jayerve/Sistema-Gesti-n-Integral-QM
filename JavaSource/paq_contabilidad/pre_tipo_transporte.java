package paq_contabilidad;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

 
public class pre_tipo_transporte extends Pantalla{
	
	private Tabla tab_tipo_transporte=new Tabla();
	
	public Tabla gettab_tipo_transporte() {
		return tab_tipo_transporte;
	}

	public void settab_tipo_transporte(Tabla tab_tipo_transporte) {
		this.tab_tipo_transporte = tab_tipo_transporte;
	}

	public pre_tipo_transporte() {
		tab_tipo_transporte.setId("tab_tipo_transporte");  
		tab_tipo_transporte.setTabla("cont_tipo_transporte", "ide_cotit", 1);	
		tab_tipo_transporte.dibujar();
		PanelTabla pat_tipo_transporte=new PanelTabla();
		pat_tipo_transporte.setPanelTabla(tab_tipo_transporte);
		
		agregarComponente(pat_tipo_transporte);
		
	}	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_transporte.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_transporte.guardar();
		guardarPantalla();			
	}
 
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_transporte.eliminar();		
	}

}
