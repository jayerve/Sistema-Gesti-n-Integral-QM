package paq_bodega;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;

public class pre_material_bodega extends Pantalla{

	private Tabla tab_material=new Tabla();
	private Tabla tab_inventario = new Tabla();

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);

	public pre_material_bodega() {
		
		tab_material.setHeader("MATERIALES DE BODEGA");
		tab_material.setId("tab_material");  
		tab_material.setTabla("bodt_material","ide_bomat", 1);	
		tab_material.setCampoOrden("ide_bomat desc");
		tab_material.getColumna("ide_bounm").setCombo("bodt_unidad_medida", "ide_bounm", "detalle_bounm,abreviatura_bounm", "");
		tab_material.getColumna("ide_botip").setCombo("bodt_tipo_producto", "ide_botip", "detalle_botip", "");
		tab_material.getColumna("ide_bocam").setCombo(ser_bodega.getCatalagoBodega("true"));
		tab_material.getColumna("ide_bocam").setAutoCompletar();
		//tab_material.getColumna("ide_bocam").setRequerida(true);
		tab_material.setCondicion("ide_botip not in(2)");
	
		tab_material.getColumna("foto_bomat").setUpload("fotos");
    	tab_material.getColumna("foto_bomat").setImagen("128", "128");
		List lista = new ArrayList();
        Object fila1[] = {
            "1", "SI"
        };
        Object fila2[] = {
            "0", "NO"
        };
   
        lista.add(fila1);
        lista.add(fila2);
       
        tab_material.getColumna("iva_bomat").setRadio(lista, "1");
        tab_material.getColumna("iva_bomat").setRadioVertical(true);
        tab_material.getColumna("activo_bomat").setValorDefecto("true");
        
    	tab_material.agregarRelacion(tab_inventario); //CON ESTO LE DECIMOS Q TIENE RELACION
		tab_material.setTipoFormulario(true);
		tab_material.getGrid().setColumns(6);
		tab_material.dibujar();
		PanelTabla pat_material=new PanelTabla();
		pat_material.setPanelTabla(tab_material);
		
		tab_inventario.setHeader("INVENTARIO");
		tab_inventario.setId("tab_inventario");  
		tab_inventario.setTabla("bodt_inventario","ide_boinv", 2);
		tab_inventario.getColumna("ide_bomat").setUnico(true);
		tab_inventario.getColumna("ide_geani").setUnico(true);
		tab_inventario.getColumna("ingreso_material_boinv").setValorDefecto("0.0");
		tab_inventario.getColumna("egreso_material_boinv").setValorDefecto("0.0");
		tab_inventario.getColumna("existencia_inicial_boinv").setValorDefecto("0.0");
		tab_inventario.getColumna("costo_anterior_boinv").setValorDefecto("0.0");
		tab_inventario.getColumna("costo_actual_boinv").setValorDefecto("0.0");
		tab_inventario.getColumna("costo_inicial_boinv").setValorDefecto("0.0");
		tab_inventario.getColumna("activo_boinv").setValorDefecto("true");
		tab_inventario.getColumna("ide_geani").setCombo(ser_contabilidad.getAnio("true,false","true,false"));
		tab_inventario.setLectura(true);
		tab_inventario.setCampoForanea("ide_bomat");
		tab_inventario.dibujar();
		PanelTabla pat_material_tarifa= new PanelTabla();
		pat_material_tarifa.setPanelTabla(tab_inventario);
			
			
		Division div_division = new Division();
		div_division.dividir2(pat_material,pat_material_tarifa , "50%", "H");
		agregarComponente(div_division);
		
	}


	@Override
	public void insertar() {
		// TODO Auto-generated method stub
				
		if (tab_material.isFocus()) {
			tab_material.insertar();
		}
				
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_material.guardar()) {
			guardarPantalla();
			tab_material.ejecutarSql();
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();

	}
	public Tabla getTab_material() {
		return tab_material;
	}
	public void setTab_material(Tabla tab_material) {
		this.tab_material = tab_material;
	}
	public Tabla getTab_inventario() {
		return tab_inventario;
	}
	public void setTab_inventario(Tabla tab_inventario) {
		this.tab_inventario = tab_inventario;
	}
	
	
}
