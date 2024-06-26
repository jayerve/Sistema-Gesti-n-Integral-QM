package paq_competencias;

import org.primefaces.event.NodeSelectEvent;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_factor_competencias extends Pantalla {
	private Tabla tab_tabla = new Tabla();	
	private Arbol arb_arbol = new Arbol();
	private Tabla tab_tabla2 =new Tabla();
  
	public pre_factor_competencias() {

		tab_tabla.setId("tab_tabla");
		tab_tabla.setTabla("CMP_FACTOR_COMPETENCIA", "IDE_CMFAC", 1);
		tab_tabla.getColumna("ACTIVO_CMFAC").setCheck();
		tab_tabla.getColumna("ACTIVO_CMFAC").setValorDefecto("true");
		tab_tabla.getColumna("IDE_SPFAC").setCombo("SPR_FACTOR", "IDE_SPFAC", "DETALLE_SPFAC", "");
		tab_tabla.getColumna("IDE_SPFAC").setAutoCompletar();
		tab_tabla.getColumna("IDE_EVFAE").setCombo("EVL_FACTOR_EVALUACION","IDE_EVFAE", "DETALLE_EVFAE", "");
		
		//tab_tabla.agregarRelacion(tab_tabla2);
		tab_tabla.setCampoNombre("DETALLE_CMFAC"); // Para que se configure el arbol
		tab_tabla.setCampoPadre("CMP_IDE_CMFAC"); // Para que se configure el arbol
		tab_tabla.agregarArbol(arb_arbol); // Para que se configure el arbol
		tab_tabla.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);
		arb_arbol.setId("arb_arbol");
		arb_arbol.dibujar();
		
/*		tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setTabla("CMP_DETALLE_COMPETENCIA", "IDE_CMDEC", 1);
		tab_tabla2.getColumna("ACTIVO_CMDEC").setCheck();
		tab_tabla2.getColumna("ACTIVO_CMDEC").setValorDefecto("true");	
		tab_tabla2.getColumna("IDE_GEGCA").setCombo("SELECT a.ide_gegca,b.detalle_gegro,c.detalle_gecaf,d.detalle_gedep,e.detalle_geare FROM gen_grupo_cargo_area a, gen_grupo_ocupacional b, gen_cargo_funcional c,gen_departamento d,gen_area e " +
				"where a.ide_gegro=b.ide_gegro and a.ide_gecaf=c.ide_gecaf and a.ide_gedep=d.ide_gedep and d.ide_geare=e.ide_geare");
		tab_tabla2.getColumna("IDE_GEGCA").setAutoCompletar();
		tab_tabla2.dibujar();

		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_tabla2);
		*/
		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(arb_arbol, pat_panel, "21%", "V");
		agregarComponente(div_division);
	}

	public void seleccionar_arbol(NodeSelectEvent evt) {
		arb_arbol.seleccionarNodo(evt);
		tab_tabla.setValorPadre(arb_arbol.getValorSeleccionado());
		tab_tabla.ejecutarSql();
	}

	@Override
	public void insertar() {
		if(tab_tabla.isFocus()){
			tab_tabla.insertar();
		}else{
				utilitario.agregarMensajeInfo("No se Puede Insertar", "Ingrese un Factor de Seleccion");
			}			
				
	}

	@Override
	public void guardar() {		
		if (tab_tabla.guardar()){
																																				
				guardarPantalla();										
								
		}
	}

	@Override
	public void eliminar() {
		if (tab_tabla.isFocus()){	
			tab_tabla.eliminar();							
		}
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

	public Arbol getArb_arbol() {
		return arb_arbol;
	}

	public void setArb_arbol(Arbol arb_arbol) {
		this.arb_arbol = arb_arbol;
	}


	
}
