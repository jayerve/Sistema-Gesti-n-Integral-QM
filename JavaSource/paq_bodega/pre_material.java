package paq_bodega;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;


import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;

import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;

public class pre_material extends Pantalla{
	private Tabla tab_material=new Tabla();
	private Tabla tab_material_tarifa = new Tabla();
	private Tabla tab_punto_venta=new Tabla();
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);

	public pre_material() {
		tab_material.setHeader("MATERIALES DE BODEGA");
		tab_material.setId("tab_material");  
		tab_material.setTabla("bodt_material","ide_bomat", 1);	
		tab_material.setCampoOrden("ide_bomat desc");
		tab_material.getColumna("ide_bounm").setCombo("bodt_unidad_medida", "ide_bounm", "detalle_bounm,abreviatura_bounm", "");
		tab_material.getColumna("ide_botip").setCombo("bodt_tipo_producto", "ide_botip", "detalle_botip", "");
		tab_material.getColumna("ide_bogrm").setCombo("bodt_grupo_material", "ide_bogrm", "detalle_bogrm", "");
		tab_material.getColumna("ide_cocac").setCombo(ser_contabilidad.getCuentaContable("true"));
		tab_material.getColumna("ide_cocac").setAutoCompletar();
		tab_material.getColumna("ide_cocac").setRequerida(true);
		tab_material.getColumna("ide_cocac").setMetodoChange("codigoMaterial");
		tab_material.setCondicion("ide_botip in(2)");
		tab_material.getColumna("codigo_bomat").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:black");
		tab_material.getColumna("codigo_bomat").setEtiqueta();
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
        
    	tab_material.agregarRelacion(tab_material_tarifa); //CON ESTO LE DECIMOS Q TIENE RELACION
		tab_material.setTipoFormulario(true);
		tab_material.getGrid().setColumns(6);
		tab_material.dibujar();
		PanelTabla pat_material=new PanelTabla();
		pat_material.setPanelTabla(tab_material);
		
		tab_material_tarifa.setHeader("TARIFAS");
		tab_material_tarifa.setId("tab_material_tarifa");
		tab_material_tarifa.setTabla("tes_material_tarifa", "ide_temat", 2);
		tab_material_tarifa.getGrid().setColumns(4);
		tab_material_tarifa.getColumna("ide_tetar").setCombo("tes_tarifas", "ide_tetar", "detalle_tetar", "");
		tab_material_tarifa.getColumna("ide_tetar").setUnico(true);
		tab_material_tarifa.getColumna("ide_bomat").setUnico(true);
		tab_material_tarifa.getColumna("activo_temat").setValorDefecto("true");
		
		tab_material_tarifa.setCampoForanea("ide_bomat");
		tab_material_tarifa.dibujar();
		PanelTabla pat_material_tarifa= new PanelTabla();
		pat_material_tarifa.setPanelTabla(tab_material_tarifa);
			
			
		Division div_division = new Division();
		div_division.dividir2(pat_material,pat_material_tarifa , "50%", "H");
		agregarComponente(div_division);
		
	}
	public void codigoMaterial(AjaxBehaviorEvent evt) {
		tab_material.modificar(evt); //Siempre es la primera linea
		if(tab_material.getValor("ide_bomat")==null||tab_material.getValor("ide_bomat").isEmpty() ){
			
		}
		else {
			TablaGenerica codigo_material= utilitario.consultar(ser_bodega.getMaterialBodega(tab_material.getValor("ide_bomat")));
			TablaGenerica codigo_cuenta =utilitario.consultar(ser_contabilidad.getCuentaContableCodigo("true,false", tab_material.getValor("ide_cocac")));
			String codigo_generado=codigo_cuenta.getValor("cue_codigo_cocac")+"."+codigo_material.getValor("ide_bomat");
			tab_material.setValor("codigo_bomat", codigo_generado);
			utilitario.addUpdateTabla(tab_material, "codigo_bomat", "");
		}
 
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
				
		if (tab_material.isFocus()) {
			tab_material.insertar();
			//tab_poa.setValor("ide_geani", com_anio.getValue()+"");

		}
		else if (tab_material_tarifa.isFocus()) {
			tab_material_tarifa.insertar();

		}
				
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_material.guardar()) {
			
			if (tab_material_tarifa.guardar()) {
									}
					
				}
			
		guardarPantalla();
		tab_material.ejecutarSql();

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
	public Tabla getTab_material_tarifa() {
		return tab_material_tarifa;
	}
	public void setTab_material_tarifa(Tabla tab_material_tarifa) {
		this.tab_material_tarifa = tab_material_tarifa;
	}
	
	
	}
