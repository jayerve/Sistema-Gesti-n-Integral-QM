package paq_presupuesto;

import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_techo_futuras extends Pantalla 
{
	private Tabla tab_techo_futuras=new Tabla();
	private Combo com_anio=new Combo();

	 public pre_techo_futuras() {
		// TODO Auto-generated constructor stub
		 
		com_anio.setCombo("select ide_geani,detalle_geani from gen_anio order by detalle_geani");
		com_anio.setMetodo("filtrarAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		//com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
				 
		tab_techo_futuras.setId("tab_techo_futuras");
		tab_techo_futuras.setTabla("pre_techo_futuras", "ide_prtef", 1);
		tab_techo_futuras.setCondicion("ide_geani=-1");
		tab_techo_futuras.getColumna("ide_geani").setVisible(false);
		//tab_techo_futuras.getColumna("ide_prgre").setCombo("pre_grupo_economico","ide_prgre","detalle_prgre","");		
		tab_techo_futuras.setColumnaSuma("valor_prtef");
		tab_techo_futuras.dibujar();
		PanelTabla pat_fuente_financiamiento=new PanelTabla();
		pat_fuente_financiamiento.setPanelTabla(tab_techo_futuras);
		
		agregarComponente(pat_fuente_financiamiento);

	}
	 
	public void filtrarAnio(){
		System.out.println("entre 0");

		if(com_anio.getValue()!=null){
			tab_techo_futuras.setCondicion("ide_geani="+com_anio.getValue().toString());
		}
		else{
			tab_techo_futuras.setCondicion("ide_geani=-1");
		}
		tab_techo_futuras.ejecutarSql();		
		tab_techo_futuras.imprimirSql();
		utilitario.addUpdate("tab_techo_futuras");
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;
		}
		else if (tab_techo_futuras.isFocus()){
			tab_techo_futuras.insertar();
			tab_techo_futuras.setValor("ide_geani", com_anio.getValue()+"");
		}

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_techo_futuras.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_techo_futuras.eliminar();
		
	}

	public Tabla getTab_techo_futuras() {
		return tab_techo_futuras;
	}

	public void setTab_techo_futuras(Tabla tab_techo_futuras) {
		this.tab_techo_futuras = tab_techo_futuras;
	}


	

}
