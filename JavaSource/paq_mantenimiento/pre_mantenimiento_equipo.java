/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_mantenimiento;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import paq_activos.ejb.ServicioActivos;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_mantenimiento_equipo extends Pantalla {

    private Tabla tab_tabla = new Tabla();
	@EJB
	private ServicioActivos ser_activos = (ServicioActivos) utilitario.instanciarEJB(ServicioActivos.class);

    public pre_mantenimiento_equipo() {
    
    	tab_tabla.setId("tab_tabla");
    	tab_tabla.getGrid().setColumns(4); //hacer  coâ™ lumnas 
		tab_tabla.setTabla("mto_equipo", "ide_mtequ", 1);
		tab_tabla.getColumna("ide_afact").setCombo("SELECT ide_afact, ide_afact as codigo,CASE WHEN a.ide_bocam IS NULL THEN nac.detalle_afnoa ELSE b.descripcion_bocam END AS descripcion_bocam, detalle_afact,observaciones_afact,color_afact,  marca_afact, serie_afact, modelo_afact,  chasis_afact, motor_afact,cod_anterior_afact FROM afi_activo a LEFT JOIN bodt_catalogo_material b ON a.ide_bocam = b.ide_bocam LEFT JOIN afi_nombre_activo nac ON a.ide_afnoa=nac.ide_afnoa");
		tab_tabla.getColumna("ide_afact").setAutoCompletar();
		tab_tabla.getColumna("ide_afact").setMetodoChange("getDatosBien");
		tab_tabla.getColumna("ide_afact").setRequerida(true);
		tab_tabla.getColumna("ide_estado").setCombo("SELECT ide_mtest, nombre_mtest FROM mto_estado");
	    tab_tabla.getColumna("ide_tipequ").setCombo("SELECT ide_mtteq, nombre_mtteq || ' ' || descripcion_mtteq  FROM mto_tipo_equipo");
		//tab_tabla.getColumna("ide_mtman").setVisible(false);

		tab_tabla.getColumna("ACTIVO_mtequ").setCheck();
		tab_tabla.getColumna("ACTIVO_mtequ").setValorDefecto("true");
		tab_tabla.setTipoFormulario(true);  //formulario 

		tab_tabla.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);



		Division div_division = new Division();        
		div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
		
    	
       /* tab_tabla.setId("tab_tabla");
        tab_tabla.setNumeroTabla(1);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);*/
    }

    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla.guardar()) {
            guardarPantalla();
        }
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

	public ServicioActivos getSer_activos() {
		return ser_activos;
	}

	public void setSer_activos(ServicioActivos ser_activos) {
		this.ser_activos = ser_activos;
	}
    
    
	public void getDatosBien(AjaxBehaviorEvent evt) {
		tab_tabla.modificar(evt);
		if (tab_tabla.getValor("ide_afact") == null) {
			return;
		}
		TablaGenerica tab_datos_equipo=utilitario.consultar("select ide_mtequ,ide_afact from mto_equipo where ide_afact="+tab_tabla.getValor("IDE_AFACT"));
    	if (tab_datos_equipo.getTotalFilas()>0) {
    		tab_tabla.setValor("ide_afact","");
    		utilitario.addUpdate("tab_tabla");
			utilitario.agregarMensajeInfo("El equipo informático ya se encuentra ingresado", "Realizar el mantenimiento");
			return;
		}else{

		TablaGenerica tab_datos=utilitario.consultar("select marca_afact,modelo_afact,serie_afact from afi_activo where ide_afact="+tab_tabla.getValor("IDE_AFACT"));
		if (tab_datos.getTotalFilas()>0) {
		tab_tabla.setValor("marca_mtequ", tab_datos.getValor("marca_afact"));
		tab_tabla.setValor("modelo_mtequ", tab_datos.getValor("modelo_afact"));
		tab_tabla.setValor("serie_mtequ", tab_datos.getValor("serie_afact"));
		utilitario.addUpdate("tab_tabla");


		}else{
			
		}
		}
		// tab_activos_fijos.getColumna("ide_cocac").setAutoCompletar();
	} 
}
