package paq_contabilidad;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;

public class pre_asiento_apertura_cierre extends Pantalla{

	private Tabla tab_catalogo_cuentas = new Tabla();
	private Tabla tab_asociacion_cuentas = new Tabla();
	private Radio rad_imprimir= new Radio();
	
	   @EJB
	    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	public pre_asiento_apertura_cierre() {
		

		List lista = new ArrayList();
		Object fila1[] = {"0", "TODOS"};
        Object fila2[] = {"1", "APERTURA"};
        Object fila3[] = {"2", "CIERRE"};
        Object fila4[] = {"3", "APERTURA/CIERRE"};
        
        lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		lista.add(fila4);
	    rad_imprimir.setId("rad_imprimir");
	    rad_imprimir.setRadio(lista);
	    rad_imprimir.setValue(fila1);
	    rad_imprimir.setMetodoChange("seleccionaOpcion");
	    //rad_imprimir.setVertical();
	    
	    bar_botones.agregarComponente(rad_imprimir);
		
	    lista.remove(fila1);
	    		
		tab_catalogo_cuentas.setId("tab_catalogo_cuentas");
		tab_catalogo_cuentas.setHeader("CATALOGO DE CUENTAS - APERTURA/CIERRE");
		tab_catalogo_cuentas.setTabla("cont_catalogo_cuenta", "ide_cocac", 1);
		//tab_catalogo_cuentas.setCondicion(" coalesce(apertura_cierre_cocac,0) in (3) ");
		tab_catalogo_cuentas.getColumna("con_ide_cocac").setVisible(false);
		tab_catalogo_cuentas.getColumna("saldo_cocac").setVisible(false);
		tab_catalogo_cuentas.getColumna("ide_botip").setVisible(false);
		tab_catalogo_cuentas.getColumna("con_ide_cocac2").setVisible(false);
		tab_catalogo_cuentas.getColumna("ide_cogrc").setVisible(false);
		tab_catalogo_cuentas.getColumna("sigef_cocac").setVisible(false);
		tab_catalogo_cuentas.getColumna("nivel_cocac").setVisible(false);
		tab_catalogo_cuentas.getColumna("grupo_nivel_cocac").setVisible(false);
		tab_catalogo_cuentas.getColumna("apertura_cierre_cocac").setRadio(lista, "0");
		tab_catalogo_cuentas.getColumna("apertura_cierre_cocac").setLongitud_control(85);
		tab_catalogo_cuentas.getColumna("cue_codigo_cocac").setFiltroContenido();
		tab_catalogo_cuentas.agregarRelacion(tab_asociacion_cuentas);
		tab_catalogo_cuentas.setLectura(true);
		tab_catalogo_cuentas.setRows(20);
		tab_catalogo_cuentas.dibujar();
		PanelTabla pat_catalogo_cuentas= new PanelTabla();
		pat_catalogo_cuentas.setPanelTabla(tab_catalogo_cuentas);
			
		tab_asociacion_cuentas.setId("tab_asociacion_cuentas");
		tab_asociacion_cuentas.setHeader("ASOCIACIÓN DE CUENTAS - APERTURA/CIERRE");
		tab_asociacion_cuentas.setTabla("cont_asiento_apertura_cierre", "ide_asiac", 2);
		tab_asociacion_cuentas.getColumna("apertura_cierre_asiac").setRadio(lista, "0");
		tab_asociacion_cuentas.getColumna("apertura_cierre_asiac").setLongitud_control(70);
		tab_asociacion_cuentas.getColumna("activo_asiac").setValorDefecto("true");
		tab_asociacion_cuentas.getColumna("ide_gelua").setCombo("gen_lugar_aplica","ide_gelua","detalle_gelua","");
		tab_asociacion_cuentas.getColumna("con_ide_cocac").setCombo(ser_contabilidad.servicioCatalogoCuentaCombo());
		tab_asociacion_cuentas.getColumna("con_ide_cocac").setAutoCompletar();
		tab_asociacion_cuentas.dibujar();
		PanelTabla pat_asociacion_cuentas = new PanelTabla();
		pat_asociacion_cuentas.setPanelTabla(tab_asociacion_cuentas);

		Division div_division = new Division();
		div_division.dividir2(pat_catalogo_cuentas,pat_asociacion_cuentas,"50%","H");
		agregarComponente(div_division);
		
	}
	
	public void seleccionaOpcion() {
		if(pckUtilidades.CConversion.CInt(rad_imprimir.getValue())>0)
			tab_catalogo_cuentas.setCondicion(" coalesce(apertura_cierre_cocac,0) in ("+pckUtilidades.CConversion.CInt(rad_imprimir.getValue())+")");
		else
			tab_catalogo_cuentas.setCondicion("");
		tab_catalogo_cuentas.ejecutarSql();
		utilitario.addUpdate("tab_catalogo_cuentas");
		tab_asociacion_cuentas.ejecutarValorForanea(tab_catalogo_cuentas.getValorSeleccionado());
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_catalogo_cuentas.guardar()){
			if(tab_asociacion_cuentas.guardar()){
			
					guardarPantalla();		
				
			}
		}
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_catalogo_cuentas() {
		return tab_catalogo_cuentas;
	}

	public void setTab_catalogo_cuentas(Tabla tab_catalogo_cuentas) {
		this.tab_catalogo_cuentas = tab_catalogo_cuentas;
	}

	public Tabla getTab_asociacion_cuentas() {
		return tab_asociacion_cuentas;
	}

	public void setTab_asociacion_cuentas(Tabla tab_asociacion_cuentas) {
		this.tab_asociacion_cuentas = tab_asociacion_cuentas;
	}

	public Radio getRad_imprimir() {
		return rad_imprimir;
	}

	public void setRad_imprimir(Radio rad_imprimir) {
		this.rad_imprimir = rad_imprimir;
	}




}
