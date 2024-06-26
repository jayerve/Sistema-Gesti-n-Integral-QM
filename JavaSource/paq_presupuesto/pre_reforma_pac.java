/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package paq_presupuesto;

import javax.ejb.EJB;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_reforma_pac extends Pantalla {

	private Tabla tab_tabla = new Tabla();
	private Combo com_cuatrimestre=new Combo();
	private Combo com_anio=new Combo();
	private Check che_aplica_portal = new Check();
	
	@EJB
	private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	public pre_reforma_pac() {  
		
		bar_botones.limpiar();

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione el Año:"));
		bar_botones.agregarComponente(com_anio);
		
		com_cuatrimestre.setCombo("select ide_copec, detalle_copec from cont_periodo_cuatrimestre where activo_copec=true");
		com_cuatrimestre.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione el Cuatrimestre:"));
		bar_botones.agregarComponente(com_cuatrimestre);
		
		che_aplica_portal.setId("che_aplica_portal");
		Etiqueta eti_aplica_portal = new Etiqueta("Aplica Portal");
		bar_botones.agregarComponente(eti_aplica_portal);
		bar_botones.agregarComponente(che_aplica_portal);
		
		Boton bot_importar = new Boton();
		bot_importar.setValue("Filtrar");
		bot_importar.setMetodo("filtrar");
		bot_importar.setIcon("ui-icon-clock");
		bar_botones.agregarComponente(bot_importar);
		
		tab_tabla.setId("tab_tabla");
		tab_tabla.setSql(ser_presupuesto.getReformasPac("-1","-1","true,false"));
		//tab_pac.getColumna("ide_geani").setVisible(false);
		//tab_pac.getColumna("ide_geani").setVisible(false);
		/*
		tab_tabla.getColumna("ide_prpoa").setVisible(true);
		tab_tabla.getColumna("ide_prpoa").setNombreVisual("POA");
		tab_tabla.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoaNombre("select ide_geani from gen_anio"));
		tab_tabla.getColumna("ide_prpoa").setAutoCompletar();
		tab_tabla.getColumna("ide_prpoa").setLongitud(300);
		tab_tabla.getColumna("ide_prpoa").setLectura(true);
		*/
		tab_tabla.getColumna("tipo_prod_prpac").setCombo(utilitario.getListaTipoProducto());	
		tab_tabla.getColumna("tregimen_prpac").setCombo(utilitario.getListaTipoRegimen());
        
		tab_tabla.setRows(8);
		tab_tabla.setLectura(true);
		tab_tabla.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		Division div_division = new Division();        
		div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);

	}
	
	public void filtrar()
	{
		String strAplicaPortal="true,false";
		if (com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("No se puede insertar", "Debe Seleccionar un Anio");
			return;
		}
		
		if(com_cuatrimestre.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un cuatrimestre", "");
			return;
		}
		
		if (che_aplica_portal.getValue().toString().equalsIgnoreCase("true")) {
			strAplicaPortal="true";
		}

		tab_tabla.setSql(ser_presupuesto.getReformasPac( com_anio.getValue().toString(), com_cuatrimestre.getValue().toString(), strAplicaPortal ));
		tab_tabla.ejecutarSql();
	}
	
	@Override
	public void insertar() {
	}

	@Override
	public void guardar() {
	}

	@Override
	public void eliminar() {
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

	public Combo getCom_cuatrimestre() {
		return com_cuatrimestre;
	}

	public void setCom_cuatrimestre(Combo com_cuatrimestre) {
		this.com_cuatrimestre = com_cuatrimestre;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public Check getChe_aplica_portal() {
		return che_aplica_portal;
	}

	public void setChe_aplica_portal(Check che_aplica_portal) {
		this.che_aplica_portal = che_aplica_portal;
	}
	
	

}
