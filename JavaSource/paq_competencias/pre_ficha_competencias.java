package paq_competencias;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import paq_anticipos.ejb.ServicioAnticipo;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.Fila;
import framework.componentes.Arbol;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;


public class pre_ficha_competencias extends Pantalla {
  
	private Tabla tab_grupo_cargo_area=new Tabla();
	private Tabla tab_factor_competencia=new Tabla();
	private Tabla tab_detalle_competenca=new Tabla();
	private Arbol arb_arbol = new Arbol();
	private SeleccionTabla sel_tab_grupo_ocupacional=new SeleccionTabla();
	private SeleccionTabla sel_tab_cargo_funcional=new SeleccionTabla();


	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();	

	public pre_ficha_competencias() {
		
		sel_tab_grupo_ocupacional.setId("sel_tab_grupo_ocupacional");
		sel_tab_grupo_ocupacional.setSeleccionTabla("SELECT IDE_GEGRO,DETALLE_GEGRO FROM  GEN_GRUPO_OCUPACIONAL WHERE IDE_GEGRO=-1", "IDE_GEGRO");
		sel_tab_grupo_ocupacional.getTab_seleccion().getColumna("DETALLE_GEGRO").setFiltro(true);
		sel_tab_grupo_ocupacional.setTitle("SELECCION DE GRUPO OCUPACIONAL");
		sel_tab_grupo_ocupacional.getBot_aceptar().setMetodo("aceptarReporte");		
		agregarComponente(sel_tab_grupo_ocupacional);

		sel_tab_cargo_funcional.setId("sel_tab_cargo_funcional");
		sel_tab_cargo_funcional.setSeleccionTabla("SELECT IDE_GECAF,DETALLE_GECAF FROM GEN_CARGO_FUNCIONAL WHERE IDE_GECAF=-1", "IDE_GECAF");
		sel_tab_cargo_funcional.getTab_seleccion().getColumna("DETALLE_GECAF").setFiltro(true);
		sel_tab_cargo_funcional.setTitle("SELECCION DE CARGO FUNCIONAL");
		sel_tab_cargo_funcional.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tab_cargo_funcional);

		bar_botones.agregarReporte();
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);


		//  grupo_cargo_area (division 1)

		tab_grupo_cargo_area.setId("tab_grupo_cargo_area");		
		tab_grupo_cargo_area.setTabla("GEN_GRUPO_CARGO_AREA", "IDE_GEGCA", 1);		
		tab_grupo_cargo_area.getColumna("ACTIVO_GEGCA").setCheck();
		tab_grupo_cargo_area.getColumna("ACTIVO_GEGCA").setValorDefecto("true");
		tab_grupo_cargo_area.getColumna("IDE_GEDEP").setCombo("SELECT a.ide_gedep,b.detalle_geare,a.detalle_gedep FROM gen_departamento a, gen_area b " +
				"where a.ide_geare=b.ide_geare order by b.detalle_geare,a.detalle_gedep");
		tab_grupo_cargo_area.getColumna("IDE_GEDEP").setAutoCompletar();
		tab_grupo_cargo_area.getColumna("IDE_GEGRO").setCombo("GEN_GRUPO_OCUPACIONAL", "IDE_GEGRO", "DETALLE_GEGRO", "");
		tab_grupo_cargo_area.getColumna("IDE_GEGRO").setAutoCompletar();
		tab_grupo_cargo_area.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "");
		tab_grupo_cargo_area.getColumna("IDE_GECAF").setAutoCompletar();		
		tab_grupo_cargo_area.getGrid().setColumns(4);
		tab_grupo_cargo_area.setTipoFormulario(true);		
		tab_grupo_cargo_area.setLectura(true);
		tab_grupo_cargo_area.agregarRelacion(tab_detalle_competenca);
		tab_grupo_cargo_area.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_grupo_cargo_area);
		pat_panel1.getMenuTabla().getItem_insertar().setRendered(true);
		pat_panel1.getMenuTabla().getItem_guardar().setRendered(true);
		pat_panel1.getMenuTabla().getItem_eliminar().setRendered(true);

		// factor_competencia
		arb_arbol.setId("arb_arbol");
		arb_arbol.setArbol("CMP_FACTOR_COMPETENCIA", "IDE_CMFAC", "DETALLE_CMFAC", "CMP_IDE_CMFAC");
		arb_arbol.onSelect("seleccionar_arbol");	
		arb_arbol.dibujar();
			
		tab_detalle_competenca.setId("tab_detalle_competenca");
		tab_detalle_competenca.setTabla("CMP_DETALLE_COMPETENCIA", "IDE_CMDEC", 3);
		
		tab_detalle_competenca.getColumna("ACTIVO_CMDEC").setCheck();
		tab_detalle_competenca.getColumna("ACTIVO_CMDEC").setValorDefecto("true");
		tab_detalle_competenca.getColumna("IDE_GEGCA").setValorDefecto(tab_grupo_cargo_area.getValorSeleccionado());
		tab_detalle_competenca.getColumna("IDE_CMFAC").setVisible(false);
		tab_detalle_competenca.dibujar();

		
		PanelTabla pat_panel3 =new PanelTabla();
		pat_panel3.setPanelTabla(tab_detalle_competenca);
				
		Division div_aux=new Division();
		div_aux.setId("div_aux");
		div_aux.dividir2(arb_arbol,pat_panel3, "30%", "V");

		//  DIVISION DE LA PANTALLA
		
		Division div_division=new Division();
		div_division.dividir2(pat_panel1, div_aux, "50%", "H");		
		agregarComponente(div_division);
	}

	public void seleccionar_arbol(NodeSelectEvent evt) {
		arb_arbol.seleccionarNodo(evt);
		tab_detalle_competenca.setCondicion("IDE_CMFAC="+arb_arbol.getValorSeleccionado());
		tab_detalle_competenca.ejecutarSql();	
	}
	

	
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */

	@Override
	public void insertar() {		
		if (tab_detalle_competenca.isFocus()){			
			if (tab_grupo_cargo_area.getTotalFilas()>0){				
				if(arb_arbol.getTreeNodes().size()>1){					
					tab_detalle_competenca.insertar();	
					tab_detalle_competenca.setValor("IDE_CMFAC", arb_arbol.getValorSeleccionado());
					tab_detalle_competenca.setValor("IDE_GEGCA", tab_grupo_cargo_area.getValorSeleccionado());
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe seleccionar un Factor de Competencia");
				}								
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe seleccionar un Grupo Cargo Area");				
			}		
		}
		
		
		if (tab_grupo_cargo_area.isFocus()) {
			tab_grupo_cargo_area.insertar();
		}
	}

	@Override
	public void guardar() {		
		if (tab_grupo_cargo_area.guardar()){																									
			guardarPantalla();

		}			
		
		if (tab_detalle_competenca.guardar()){																									
			guardarPantalla();

		}
		
	}


	@Override
	public void eliminar() {		
		if(tab_detalle_competenca.isFocus()){
			tab_detalle_competenca.eliminar();
		}	
		
		if (tab_grupo_cargo_area.isFocus()){																									
			tab_grupo_cargo_area.eliminar();

		}	
	}

	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}

	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}



	public Tabla getTab_factor_competencia() {
		return tab_factor_competencia;
	}

	public void setTab_factor_competencia(Tabla tab_factor_competencia) {
		this.tab_factor_competencia = tab_factor_competencia;
	}

	public Tabla getTab_detalle_competenca() {
		return tab_detalle_competenca;
	}

	public void setTab_detalle_competenca(Tabla tab_detalle_competenca) {
		this.tab_detalle_competenca = tab_detalle_competenca;
	}

	public Arbol getArb_arbol() {
		return arb_arbol;
	}

	public void setArb_arbol(Arbol arb_arbol) {
		this.arb_arbol = arb_arbol;
	}
	

	public Tabla getTab_grupo_cargo_area() {
		return tab_grupo_cargo_area;
	}

	public void setTab_grupo_cargo_area(Tabla tab_grupo_cargo_area) {
		this.tab_grupo_cargo_area = tab_grupo_cargo_area;
	}

	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
	}
	
	

	public SeleccionTabla getSel_tab_grupo_ocupacional() {
		return sel_tab_grupo_ocupacional;
	}

	public void setSel_tab_grupo_ocupacional(
			SeleccionTabla sel_tab_grupo_ocupacional) {
		this.sel_tab_grupo_ocupacional = sel_tab_grupo_ocupacional;
	}

	public SeleccionTabla getSel_tab_cargo_funcional() {
		return sel_tab_cargo_funcional;
	}

	public void setSel_tab_cargo_funcional(SeleccionTabla sel_tab_cargo_funcional) {
		this.sel_tab_cargo_funcional = sel_tab_cargo_funcional;
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {	
		if (rep_reporte.getReporteSelecionado().equals("Manual de Competencias")){			
				if (rep_reporte.isVisible()){
					p_parametros=new HashMap();					
					sel_tab_grupo_ocupacional.getTab_seleccion().setSql("SELECT IDE_GEGRO,DETALLE_GEGRO FROM  GEN_GRUPO_OCUPACIONAL");
					sel_tab_grupo_ocupacional.getTab_seleccion().ejecutarSql();
					sel_tab_grupo_ocupacional.getBot_aceptar().setMetodo("aceptarReporte");
					rep_reporte.cerrar();
					sel_tab_grupo_ocupacional.dibujar();
				}else if(sel_tab_grupo_ocupacional.isVisible()){
					if(sel_tab_grupo_ocupacional.getSeleccionados()!=null && !sel_tab_grupo_ocupacional.getSeleccionados().isEmpty()){
						p_parametros.put("ide_gegro",sel_tab_grupo_ocupacional.getSeleccionados());
						sel_tab_cargo_funcional.getTab_seleccion().setSql("select CAF.IDE_GECAF,CAF.DETALLE_GECAF from GEN_CARGO_FUNCIONAL CAF " +
								"LEFT JOIN GEN_GRUPO_CARGO GCAF ON GCAF.IDE_GECAF=CAF.IDE_GECAF " +
								"WHERE GCAF.IDE_GEGRO in ("+sel_tab_grupo_ocupacional.getSeleccionados()+") " +
								"GROUP BY CAF.IDE_GECAF,CAF.DETALLE_GECAF " +
								"ORDER BY CAF.DETALLE_GECAF ASC");
						sel_tab_cargo_funcional.getTab_seleccion().ejecutarSql();
						sel_tab_cargo_funcional.getBot_aceptar().setMetodo("aceptarReporte");
						sel_tab_grupo_ocupacional.cerrar();
						sel_tab_cargo_funcional.dibujar();						
					}else{
						utilitario.agregarMensajeInfo("No se puede continuar", "Debe selccionar al menos un Cargo Funcional");
					}					
				}else if(sel_tab_cargo_funcional.isVisible()){
					if(sel_tab_cargo_funcional.getSeleccionados()!=null && !sel_tab_cargo_funcional.getSeleccionados().isEmpty()){
						p_parametros.put("ide_gecaf",sel_tab_cargo_funcional.getSeleccionados());						
						p_parametros.put("titulo", "MANUAL DE COMPETENCIAS");
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
						sel_tab_cargo_funcional.cerrar();
						sef_reporte.dibujar();
					}else{
						utilitario.agregarMensajeInfo("No se puede continuar", "Debe selccionar al menos un Cargo Funcional");
					}							

				}		
			}
		}
}


