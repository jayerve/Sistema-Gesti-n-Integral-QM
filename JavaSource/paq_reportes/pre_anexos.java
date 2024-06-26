package paq_reportes;

import java.util.HashMap;
import java.util.Map;

import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_anexos extends Pantalla {
	private Tabla tab_anexo=new Tabla();
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	
	public pre_anexos (){
		
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		tab_anexo.setId("tab_anexo");
		tab_anexo.setTabla("rep_anexos", "ide_rpane", 1);
		tab_anexo.dibujar();
		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_anexo);
		agregarComponente(pat_panel1);
	}
	//reporte
	   public void abrirListaReportes() {
	   	// TODO Auto-generated method stub
	   	rep_reporte.dibujar();
	   }
	   public void aceptarReporte(){
	   	if(rep_reporte.getReporteSelecionado().equals("Anexos"));{
	   		if (rep_reporte.isVisible()){
	   			p_parametros=new HashMap();		
	   			rep_reporte.cerrar();	
	   			p_parametros.put("titulo","Anexos");
	   			p_parametros.put("ide_usua",pckUtilidades.CConversion.CInt("7"));
	   			p_parametros.put("ide_empr",pckUtilidades.CConversion.CInt("0"));
	   			p_parametros.put("ide_sucu",pckUtilidades.CConversion.CInt("1"));
	   		//p_parametros.put("pide_fafac",pckUtilidades.CConversion.CInt(tab_cont_viajeros.getValor("ide_fanoc")));
	   		self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
	   		self_reporte.dibujar();
	   		
	   	}
	   }
	   }
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_anexo.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_anexo.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_anexo.eliminar();
	}

	public Tabla getTab_anexo() {
		return tab_anexo;
	}

	public void setTab_anexo(Tabla tab_anexo) {
		this.tab_anexo = tab_anexo;
	}
	public Map getP_parametros() {
		return p_parametros;
	}
	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}
	public Reporte getRep_reporte() {
		return rep_reporte;
	}
	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}
	public SeleccionFormatoReporte getSelf_reporte() {
		return self_reporte;
	}
	public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
		this.self_reporte = self_reporte;
	}
	public Map getMap_parametros() {
		return map_parametros;
	}
	public void setMap_parametros(Map map_parametros) {
		this.map_parametros = map_parametros;
	}

}
