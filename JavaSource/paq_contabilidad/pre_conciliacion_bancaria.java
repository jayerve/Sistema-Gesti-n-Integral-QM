/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;

import javax.ejb.EJB;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;
import framework.componentes.AutoCompletar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author HHSLOUIS
 */
public class pre_conciliacion_bancaria extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private AutoCompletar catalogo = new AutoCompletar();
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

    public pre_conciliacion_bancaria() {
    	
    	catalogo.setId("catalogo");

    	catalogo.setAutoCompletar(ser_contabilidad.getCatalogoCuentaAnio("true,false", "7"));
    	catalogo.setMetodoChange("seleccionaCuenta");
		bar_botones.agregarComponente(new Etiqueta("Cuenta Bancos:"));
		bar_botones.agregarComponente(catalogo);	
		
		
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("cont_detalle_movimiento", "ide_codem", 1);
        tab_tabla.getColumna("ide_comov").setCombo("select ide_comov,mov_fecha_comov,detalle_comov,nro_comprobante_comov from cont_movimiento");
        tab_tabla.getColumna("ide_comov").setAutoCompletar();
        tab_tabla.getColumna("ide_comov").setLectura(true);
        tab_tabla.setCondicion("ide_cocac=-1");
        tab_tabla.getColumna("fecha_concilia_codem").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.getColumna("conciliado_codem").setValorDefecto("true");
        tab_tabla.getColumna("debe_codem").setLectura(true);
        tab_tabla.getColumna("haber_codem").setLectura(true);
        tab_tabla.getColumna("ide_prcla").setVisible(false);
        tab_tabla.getColumna("ide_prpro").setVisible(false);

        tab_tabla.setRows(20);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }
    public void seleccionaCuenta(){
		if(catalogo.getValue()!=null){
			tab_tabla.setCondicion("ide_cocac="+catalogo.getValor());
			//tab_tabla.setValor("fecha_concilia_codem", utilitario.getFechaActual());
			//tab_tabla.setValor("conciliado_codem", "true");
			tab_tabla.ejecutarSql();
			
			utilitario.addUpdate("tab_tabla");
		}
		else {
			tab_tabla.setCondicion("ide_cocac=-1");
			tab_tabla.ejecutarSql();

		}
	}	
    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
	public AutoCompletar getCatalogo() {
		return catalogo;
	}
	public void setCatalogo(AutoCompletar catalogo) {
		this.catalogo = catalogo;
	}
    
}
