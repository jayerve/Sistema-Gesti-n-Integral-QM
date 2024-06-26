package paq_precontractual;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_precontractual.ejb.ServicioEtapaProcedimiento;
import paq_precontractual.ejb.ServicioProcedimiento;
import paq_sistema.aplicacion.Pantalla;

public class precon_ruta extends Pantalla{
	
	@EJB
	private ServicioEtapaProcedimiento ser_EtapaProcedimiento = (ServicioEtapaProcedimiento) utilitario.instanciarEJB(ServicioEtapaProcedimiento.class);
	@EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioProcedimiento ser_procedimiento = (ServicioProcedimiento) utilitario.instanciarEJB(ServicioProcedimiento.class);
	
	private Tabla tab_ruta=new Tabla();
	private Combo com_anio=new Combo();
	private Combo com_procedimiento=new Combo();
	
	private String ide_prpro="-1";
	
	public precon_ruta(){
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		com_procedimiento.setId("com_procedimiento");
		com_procedimiento.setCombo(ser_procedimiento.getProcedimientos());
		com_procedimiento.setStyle("width: 200px; margin: 0 0 -8px 0;");
		com_procedimiento.setMetodo("cambiaProcedimiento");
		bar_botones.agregarComponente(new Etiqueta("Procedimiento (Opcional)"));
		bar_botones.agregarComponente(com_procedimiento);
		
		tab_ruta.setId("tab_ruta");
		tab_ruta.setHeader("RUTA PROCESO ");
		tab_ruta.setTabla("precon_ruta","ide_prrut",1);
		
		//tab_ruta.setTipoFormulario(true);  //formulario 
		//tab_ruta.getGrid().setColumns(2); //hacer  columnas
		
		tab_ruta.getColumna("ide_prrut");
		tab_ruta.getColumna("ide_prrut").setNombreVisual("Código");
		tab_ruta.getColumna("ide_pretp").setCombo(ser_EtapaProcedimiento.getProcedimientos());
		tab_ruta.getColumna("ide_pretp").setNombreVisual("Procedimiento");
		tab_ruta.getColumna("ide_pretp").setRequerida(true);
		tab_ruta.getColumna("ide_pretp").setLectura(true);
		//tab_ruta.getColumna("ide_pretp").setMetodoChange("mostrarEtapas");
		
		tab_ruta.getColumna("pre_ide_pretp").setCombo(ser_EtapaProcedimiento.getTodasEtapasProcedimientos());
		tab_ruta.getColumna("pre_ide_pretp").setNombreVisual("Actividad Inicio");
		tab_ruta.getColumna("pre_ide_pretp").setRequerida(true);
		
		tab_ruta.getColumna("pre_ide_pretp2").setCombo(ser_EtapaProcedimiento.getTodasEtapasProcedimientos());
		tab_ruta.getColumna("pre_ide_pretp2").setNombreVisual("Actividad Fin");
		tab_ruta.getColumna("pre_ide_pretp2").setRequerida(true);
		

		List lista = new ArrayList();
	    Object fila1[] = {
	           "ENVIAR", "ENVIAR"
	    };
	    Object fila2[] = {
	         "DEVOLVER", "DEVOLVER"
	    };
	    lista.add(fila1);
	    lista.add(fila2);
	    tab_ruta.getColumna("direccion_prrut").setRadio(lista, "2");
	    tab_ruta.getColumna("direccion_prrut").setNombreVisual("Dirección Ruta");
	    tab_ruta.getColumna("descripcion_prrut");
	    tab_ruta.getColumna("descripcion_prrut").setNombreVisual("Descripción");
	    tab_ruta.getColumna("activo_prrut");
	    tab_ruta.getColumna("activo_prrut").setNombreVisual("ACTIVO");
	    tab_ruta.getColumna("activo_prrut").setValorDefecto("true");
	    tab_ruta.getColumna("ide_geani").setVisible(false);;
		
	    tab_ruta.setCondicion("ide_prrut=-1");
	    tab_ruta.setRows(20);
	    tab_ruta.dibujar();
        PanelTabla pat_ruta=new PanelTabla();
        pat_ruta.setPanelTabla(tab_ruta);
		

		Division div_division = new Division();
		div_division.dividir1(pat_ruta);
		agregarComponente(div_division);
	}
	
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_ruta.setCondicion("ide_geani="+com_anio.getValue()+(pckUtilidades.CConversion.CInt(ide_prpro)>0 ? " and ide_pretp="+ide_prpro : ""));
			tab_ruta.ejecutarSql();
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
	}
	
	public void cambiaProcedimiento()
	{
		ide_prpro="-1";
		try
		{
			ide_prpro=pckUtilidades.CConversion.CInt(com_procedimiento.getValue())+"";
			tab_ruta.setCondicion("ide_geani="+com_anio.getValue()+(pckUtilidades.CConversion.CInt(ide_prpro)>0 ? " and ide_pretp="+ide_prpro : ""));
			mostrarEtapas();
			tab_ruta.ejecutarSql();

		}
		catch(Exception e)
		{}
	}
	
	public void mostrarEtapas() {
		
		  if (pckUtilidades.CConversion.CInt(ide_prpro)>0) {
			String servicioEtapas = ser_EtapaProcedimiento.getEtapasProcedimientos(pckUtilidades.CConversion.CInt(ide_prpro));
			tab_ruta.getColumna("pre_ide_pretp").setCombo(servicioEtapas);
			utilitario.addUpdateTabla(tab_ruta, "pre_ide_pretp", "");
			tab_ruta.getColumna("pre_ide_pretp2").setCombo(servicioEtapas);
			utilitario.addUpdateTabla(tab_ruta, "pre_ide_pretp2", "");
		  } 
		 
		 }

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		//utilitario.getTablaisFocus().insertar();
		if (tab_ruta.isFocus())
		{
			if(com_anio.getValue()==null){	
				utilitario.agregarMensajeInfo("Selecione un año", "");
				return;
			}
			
			if (pckUtilidades.CConversion.CInt(ide_prpro)<1) {
				utilitario.agregarMensajeInfo("Selecione un Procedimiento", "");
				return;
			}
			tab_ruta.insertar();
			tab_ruta.setValor("ide_geani", com_anio.getValue()+"");
			tab_ruta.setValor("ide_pretp", ide_prpro);
		}
	}
	
	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_ruta.guardar()){
			guardarPantalla();
		}
	}
	
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_ruta() {
		return tab_ruta;
	}

	public void setTab_ruta(Tabla tab_ruta) {
		this.tab_ruta = tab_ruta;
	}
}
