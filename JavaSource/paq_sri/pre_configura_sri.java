/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sri;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_configura_sri extends Pantalla {

	private Tabla tab_tabla = new Tabla();

	public pre_configura_sri() {        
		tab_tabla.setId("tab_tabla");
		tab_tabla.setTabla("SRI_CONFIGURACION_GENERAL", "IDE_SRCOG", 1);
		tab_tabla.getColumna("PATH_FIRMA_REPRE_SRCOG").setUpload("sri");
		tab_tabla.getColumna("PATH_FIRMA_REPRE_SRCOG").setImagen("60", "160");
		tab_tabla.getColumna("PATH_FIRMA_CONTA_SRCOG").setUpload("sri");
		tab_tabla.getColumna("PATH_FIRMA_CONTA_SRCOG").setImagen("60", "160");
		tab_tabla.getColumna("IDE_GEANI").setCombo("GEN_ANIO", "IDE_GEANI", "DETALLE_GEANI", "");
		tab_tabla.getColumna("ACTIVO_CONTA_SRCOG").setCheck();
		tab_tabla.getColumna("ACTIVO_CONTA_SRCOG").setValorDefecto("true");
		tab_tabla.getColumna("ACTIVO_REPRE_SRCOG").setCheck();
		tab_tabla.getColumna("ACTIVO_REPRE_SRCOG").setValorDefecto("true");
		tab_tabla.setTipoFormulario(true);
		tab_tabla.getGrid().setColumns(4);
		tab_tabla.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);	
	}


	@Override
	public void insertar() {
		tab_tabla.insertar();
	}

	@Override
	public void guardar() {
		if(tab_tabla.guardar()){	
			if(tab_tabla.getValor("DOCUMENTO_REPRE_SRCOG")!=null && !tab_tabla.getValor("DOCUMENTO_REPRE_SRCOG").isEmpty()){
				if (!utilitario.validarRUC(tab_tabla.getValor("DOCUMENTO_REPRE_SRCOG")) && !utilitario.validarCedula(tab_tabla.getValor("DOCUMENTO_REPRE_SRCOG"))){
					utilitario.agregarMensajeInfo("Atención", "El numero de RUC o Cedula ingresado  del Representante Legal no es valido");
					return;				
				}
			}

			if(tab_tabla.getValor("DOCUMENTO_CONTA_SRCOG")!=null && !tab_tabla.getValor("DOCUMENTO_CONTA_SRCOG").isEmpty()){
				if (!utilitario.validarRUC(tab_tabla.getValor("DOCUMENTO_CONTA_SRCOG")) && !utilitario.validarCedula(tab_tabla.getValor("DOCUMENTO_CONTA_SRCOG"))){
					utilitario.agregarMensajeInfo("Atención", "El numero de RUC o Cedula del Contador ingresado no es valido");
					return;
				}
			}
			
			if (utilitario.isFechaMenor(utilitario.getFecha(tab_tabla.getValor("FECHA_FIN_CONTA_SRCOG")), utilitario.getFecha(tab_tabla.getValor("FECHA_INICIO_CONTA_SRCOG")))){
				utilitario.agregarMensajeInfo("No se puede guardar", "La fecha fin contador no puede ser menor que la fecha inicio contador");
				return;
			}

			if (utilitario.isFechaMenor(utilitario.getFecha(tab_tabla.getValor("FECHA_FIN_REPRE_SRCOG")), utilitario.getFecha(tab_tabla.getValor("FECHA_INICIO_REPRE_SRCOG")))){
				utilitario.agregarMensajeInfo("No se puede guardar", "La fecha fin representante legal no puede ser menor que la fecha inicio representante legal");
				return;
			}
			guardarPantalla();
		}
	}

	public void eliminar() {
		tab_tabla.eliminar();
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}



}
