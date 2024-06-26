/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_general;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.AjaxBehaviorEvent;

import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_periodo_rol extends Pantalla {
	private Combo com_tipo_rol = new Combo();

    
    private Tabla tab_tabla = new Tabla();
    private List lista = new ArrayList();
    public pre_periodo_rol() {        
     	
	       com_tipo_rol.setId("com_tipo_rol");
	       
	       com_tipo_rol.setCombo("SELECT ide_nrtin, detalle_nrtin "
	       		+ "FROM nrh_tipo_nomina "
	       		+ " where activo_nrtin=true");
		       
	           
	       
	     /*  com_tipo_rol.setCombo("SELECT ide_getir, descripcion_getir "
	       		+ "FROM gen_tipo_rol order by descripcion_getir asc");*/
	       com_tipo_rol.setMetodo("cambioTipoNomina");
	       com_tipo_rol.setStyle("width: 350px; margin: 0 0 -8px 0;");
		   bar_botones.agregarComponente(new Etiqueta("Tipo Rol:"));
		   bar_botones.agregarComponente(com_tipo_rol);
				
	       

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("GEN_PERIDO_ROL", "IDE_GEPRO", 1);
        tab_tabla.getColumna("ACTIVO_GEPRO").setCheck();
        tab_tabla.getColumna("ACTIVO_GEPRO").setValorDefecto("TRUE");
        tab_tabla.getColumna("IDE_NRTIT").setCombo("NRH_TIPO_ROL", "IDE_NRTIT", "DETALLE_NRTIT", "");
        tab_tabla.getColumna("IDE_GEANI").setCombo("GEN_ANIO", "IDE_GEANI", "DETALLE_GEANI", "");
        tab_tabla.getColumna("IDE_GEANI").setMetodoChange("cargarMes");
        tab_tabla.getColumna("IDE_GEMES").setCombo("SELECT a.IDE_GEMES,a.DETALLE_GEMES FROM GEN_MES a, GEN_PERIODO b WHERE a.IDE_GEMES=b.IDE_GEMES and b.IDE_GEANI="+tab_tabla.getValor("IDE_GEANI"));                        
        tab_tabla.getColumna("GEN_IDE_GEPRO").setCombo("select ide_gepro,fecha_inicial_gepro,fecha_final_gepro,detalle_periodo_gepro from gen_perido_rol order by fecha_inicial_gepro desc");                        
        tab_tabla.getColumna("GEN_IDE_GEPRO").setCombo("select ide_gepro,fecha_inicial_gepro,fecha_final_gepro,detalle_periodo_gepro from gen_perido_rol order by fecha_inicial_gepro desc");                        
        tab_tabla.getColumna("es_liquidacion_gepro").setVisible(false);
        tab_tabla.getColumna("TIPO_ROL").setVisible(false);                        
        tab_tabla.setCondicion("IDE_GEPRO=-1");
        tab_tabla.setTipoFormulario(true);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
        actualizarCombos();
    }
    
    public void cargarMes(AjaxBehaviorEvent evt) {
        tab_tabla.modificar(evt);
        tab_tabla.getColumna("IDE_GEMES").setCombo("SELECT a.IDE_GEMES,a.DETALLE_GEMES FROM GEN_MES a, GEN_PERIODO b WHERE a.IDE_GEMES=b.IDE_GEMES and b.IDE_GEANI="+tab_tabla.getValor("IDE_GEANI"));
        utilitario.addUpdateTabla(tab_tabla, "IDE_GEMES", "");
    }
    
    private void actualizarCombos() {
    	tab_tabla.getColumna("IDE_GEMES").setCombo("SELECT a.IDE_GEMES,a.DETALLE_GEMES FROM GEN_MES a, GEN_PERIODO b WHERE a.IDE_GEMES=b.IDE_GEMES and b.IDE_GEANI="+tab_tabla.getValor("IDE_GEANI"));
        tab_tabla.actualizarCombosFormulario();
    }

   @Override
public void buscar() {
	// TODO Auto-generated method stub
	   //Esto es para que le muestre todos los meses ya que se un  combo con filtro
	tab_tabla.getColumna("IDE_GEMES").setCombo("SELECT a.IDE_GEMES,a.DETALLE_GEMES FROM GEN_MES a");
	super.buscar();	 
} 
   
    
    @Override
    public void insertar() {
    
		if (com_tipo_rol.getValue()==null || com_tipo_rol.getValue().equals("") || com_tipo_rol.getValue().toString().isEmpty()) {
			utilitario.agregarMensajeInfo("Debe escoger un tipo rol", "No se ha seleccionado un tipo de rol");
			return;
		}else{
    	tab_tabla.getColumna("IDE_GEMES").setCombo("SELECT a.IDE_GEMES,a.DETALLE_GEMES FROM GEN_MES a, GEN_PERIODO b WHERE a.IDE_GEMES=b.IDE_GEMES and b.IDE_GEANI=-1");
        tab_tabla.insertar();
    }
    }
      

    @Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();
		actualizarCombos();
	}
    
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		actualizarCombos();
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		actualizarCombos();
	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		actualizarCombos();
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		actualizarCombos();
	}

	@Override
	public void aceptarBuscar() {
		// TODO Auto-generated method stub
		super.aceptarBuscar();
		if (utilitario.getBuscaTabla().isVisible() == false) {
            actualizarCombos();
        }
	}

	@Override
    public void guardar() {
		if (com_tipo_rol.getValue()==null || com_tipo_rol.getValue().equals("") || com_tipo_rol.getValue().toString().isEmpty()) {
		utilitario.agregarMensajeInfo("Debe escoger un tipo rol", "No se ha seleccionado un tipo de rol");
		return;
		}else{
		if (tab_tabla.getTotalFilas() > 0) {
         //   TablaGenerica tab_perido_rol = utilitario.consultar("SELECT * FROM GEN_PERIDO_ROL");
			if (com_tipo_rol.getValue().toString().equals(utilitario.getVariable("p_nrh_generar_rol_liquidacion"))) {
				tab_tabla.setValor("TIPO_ROL",com_tipo_rol.getValue().toString());
				tab_tabla.setValor("es_liquidacion_gepro","true");
        } else {
				tab_tabla.setValor("TIPO_ROL",com_tipo_rol.getValue().toString());	
				tab_tabla.setValor("es_liquidacion_gepro","false");
			}
			

        } else {
        	return;
        }
        tab_tabla.guardar();
        guardarPantalla();
    }
	}
    @Override
    public void eliminar() {
    	if (tab_tabla.eliminar()) {
            actualizarCombos();
        }
    }
    
    

    
    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

	/**
	 * Cambia de periodo
	 */
	public void cambioTipoNomina() {
		
		//guardarPantalla();
		int ide_nrrol = -1;
		if (com_tipo_rol.getValue()==null || com_tipo_rol.getValue().equals("") || com_tipo_rol.getValue().toString().isEmpty()) {
			tab_tabla.setCondicion("IDE_GEPRO=-1");
			ide_nrrol = -1;
		}else {
			ide_nrrol=Integer.parseInt(com_tipo_rol.getValue().toString());
			tab_tabla.setCondicion("tipo_rol=" + com_tipo_rol.getValue());
		}
		tab_tabla.ejecutarSql();
		utilitario.addUpdate("tab_tabla");

	}

	public List getLista() {
		return lista;
	}

	public void setLista(List lista) {
		this.lista = lista;
	}
	

	
	
}
