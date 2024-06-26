
package paq_contabilidad;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

 
public class pre_tipo_convenio extends Pantalla{
	
	private Tabla tab_tipo_convenio=new Tabla();
	
	public Tabla gettab_tipo_convenio() {
		return tab_tipo_convenio;
	}

	public void settab_tipo_convenio(Tabla tab_tipo_convenio) {
		this.tab_tipo_convenio = tab_tipo_convenio;
	}

	public pre_tipo_convenio() {
		tab_tipo_convenio.setId("tab_tipo_convenio");  
		tab_tipo_convenio.setTabla("cont_tipo_convenio", "ide_cotie", 1);	
		tab_tipo_convenio.dibujar();
		PanelTabla pat_tipo_convenio=new PanelTabla();
		pat_tipo_convenio.setPanelTabla(tab_tipo_convenio);
		
		agregarComponente(pat_tipo_convenio);
		
	}	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_convenio.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_convenio.guardar();
		guardarPantalla();			
	}
 
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_convenio.eliminar();		
	}

}
