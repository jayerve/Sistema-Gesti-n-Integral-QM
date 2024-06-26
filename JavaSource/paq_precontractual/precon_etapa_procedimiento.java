package paq_precontractual;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_precontractual.ejb.ServicioGeneralAdmPrecon;
import paq_precontractual.ejb.ServicioProcedimiento;
import paq_sistema.aplicacion.Pantalla;

public class precon_etapa_procedimiento extends Pantalla{
	
	@EJB
	private ServicioProcedimiento ser_procedimiento = (ServicioProcedimiento) utilitario.instanciarEJB(ServicioProcedimiento.class);
	@EJB
	private ServicioGeneralAdmPrecon ser_generalAdm = (ServicioGeneralAdmPrecon ) utilitario.instanciarEJB(ServicioGeneralAdmPrecon.class);
	
	private Tabla tab_etapa_procedimiento=new Tabla();
	private Tabla tab_etapa_procedimiento_empleado=new Tabla();
	
	private Combo com_area=new Combo();
	private Combo com_procedimiento=new Combo();
	
	private String ide_geare="-1";
	private String ide_prpro="-1";
	
	public precon_etapa_procedimiento(){
		
		com_area.setId("com_area");
		com_area.setCombo("select ide_geare,detalle_geare from  gen_area order by detalle_geare");
		com_area.setStyle("width: 200px; margin: 0 0 -8px 0;");
		com_area.setMetodo("cambiaArea");
		bar_botones.agregarComponente(new Etiqueta("Area (Opcional)"));
		bar_botones.agregarComponente(com_area);
		
		com_procedimiento.setId("com_procedimiento");
		com_procedimiento.setCombo(ser_procedimiento.getProcedimientos());
		com_procedimiento.setStyle("width: 200px; margin: 0 0 -8px 0;");
		com_procedimiento.setMetodo("cambiaProcedimiento");
		bar_botones.agregarComponente(new Etiqueta("Procedimiento (Opcional)"));
		bar_botones.agregarComponente(com_procedimiento);
		
		tab_etapa_procedimiento.setId("tab_etapa_procedimiento");
		tab_etapa_procedimiento.setHeader("PROCEDIMIENTO ACTIVIDAD ");
		tab_etapa_procedimiento.setTabla("precon_etapa_procedimiento","ide_pretp",1);
		
		//tab_etapa_procedimiento.setTipoFormulario(true);  //formulario 
		//tab_etapa_procedimiento.getGrid().setColumns(2); //hacer  columnas
		tab_etapa_procedimiento.getColumna("ide_pretp");
		tab_etapa_procedimiento.getColumna("ide_pretp").setNombreVisual("Código");
		tab_etapa_procedimiento.getColumna("ide_preta").setCombo("precon_etapa", "ide_preta","descripcion_preta", "activo_preta=true");
		tab_etapa_procedimiento.getColumna("ide_preta").setNombreVisual("Actividad");
		tab_etapa_procedimiento.getColumna("ide_geare").setCombo("gen_area", "ide_geare","detalle_geare", "activo_geare=true");
		tab_etapa_procedimiento.getColumna("ide_geare").setNombreVisual("Área");
		//tab_etapa_procedimiento.getColumna("ide_geare").setRequerida(true);
		tab_etapa_procedimiento.getColumna("ide_prpro").setCombo(ser_procedimiento.getProcedimientos());
		tab_etapa_procedimiento.getColumna("ide_prpro").setNombreVisual("Procedimiento");
		tab_etapa_procedimiento.getColumna("ide_prpro").setRequerida(true);
		List lista = new ArrayList();
	    Object fila1[] = {
	           "DIA", "DIA"
	    };
	    Object fila2[] = {
	         "HORA", "HORA"
	    };
	    lista.add(fila1);
	    lista.add(fila2);
	    tab_etapa_procedimiento.getColumna("hora_dia_pretp").setRadio(lista, "2");
	    tab_etapa_procedimiento.getColumna("hora_dia_pretp").setNombreVisual("Variable de Medición?");
		tab_etapa_procedimiento.getColumna("numero_hora_dia_pretp");
		tab_etapa_procedimiento.getColumna("numero_hora_dia_pretp").setNombreVisual("Valor Variable de Medición");
		tab_etapa_procedimiento.getColumna("numero_hora_dia_pretp").setValorDefecto("1");
		tab_etapa_procedimiento.getColumna("activo_pretp");
		tab_etapa_procedimiento.getColumna("activo_pretp").setNombreVisual("ACTIVO");
		tab_etapa_procedimiento.getColumna("activo_pretp").setValorDefecto("true");
		
		 tab_etapa_procedimiento.agregarRelacion(tab_etapa_procedimiento_empleado);
		
		tab_etapa_procedimiento.dibujar();
        PanelTabla pat_procedimiento=new PanelTabla();
        pat_procedimiento.setPanelTabla(tab_etapa_procedimiento);
        
        Tabulador tab_Tabulador=new Tabulador();
		tab_Tabulador.setId("tab_tabulador");
		
	    /////asignacion empleados
		tab_etapa_procedimiento_empleado.setId("tab_etapa_procedimiento_empleado");
		tab_etapa_procedimiento_empleado.setIdCompleto("tab_tabulador:tab_etapa_procedimiento_empleado");
		tab_etapa_procedimiento_empleado.setTabla("precon_eta_proce_empl_dep", "ide_prepe", 2);
		tab_etapa_procedimiento_empleado.getColumna("ide_prepe");
		tab_etapa_procedimiento_empleado.getColumna("ide_prepe").setNombreVisual("Código");
		tab_etapa_procedimiento_empleado.getColumna("ide_geedp").setCombo(ser_generalAdm.servicioEmpleadoContrato("true",pckUtilidades.CConversion.CInt(tab_etapa_procedimiento.getValor(("ide_geare")))));
		tab_etapa_procedimiento_empleado.getColumna("ide_geedp").setNombreVisual("Empleado");
		tab_etapa_procedimiento_empleado.getColumna("activo_prepe");
		tab_etapa_procedimiento_empleado.getColumna("activo_prepe").setNombreVisual("ACTIVO");
		
		
		tab_etapa_procedimiento_empleado.dibujar();
		PanelTabla pat_procedimiento_empleado =new PanelTabla();
		pat_procedimiento_empleado.setPanelTabla(tab_etapa_procedimiento_empleado);
		

		tab_Tabulador.agregarTab("ASIGNACIÓN ACTIVIDAD EMPLEADOS", pat_procedimiento_empleado);
		
		Division div_division = new Division();
		div_division.dividir2(pat_procedimiento,tab_Tabulador,"50%","h");
		agregarComponente(div_division);
	}
	
	public void cambiaArea()
	{
		ide_geare="-1";
		try
		{
			ide_geare=pckUtilidades.CConversion.CInt(com_area.getValue())+"";
			tab_etapa_procedimiento.setCondicion(pckUtilidades.CConversion.CInt(ide_geare)>0 ? "ide_geare="+ide_geare : "");
			tab_etapa_procedimiento.ejecutarSql();
			tab_etapa_procedimiento_empleado.ejecutarValorForanea(tab_etapa_procedimiento.getValorSeleccionado());
		}
		catch(Exception e)
		{}
	}
	
	public void cambiaProcedimiento()
	{
		ide_prpro="-1";
		try
		{
			ide_prpro=pckUtilidades.CConversion.CInt(com_procedimiento.getValue())+"";
			tab_etapa_procedimiento.setCondicion(pckUtilidades.CConversion.CInt(ide_prpro)>0 ? "ide_prpro="+ide_prpro : "");
			tab_etapa_procedimiento.ejecutarSql();
			tab_etapa_procedimiento_empleado.ejecutarValorForanea(tab_etapa_procedimiento.getValorSeleccionado());
		}
		catch(Exception e)
		{}
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_etapa_procedimiento.isFocus()){
			tab_etapa_procedimiento.insertar();
			if(pckUtilidades.CConversion.CInt(ide_geare)>0)
				tab_etapa_procedimiento.setValor("ide_geare",ide_geare);
			
			if(pckUtilidades.CConversion.CInt(ide_prpro)>0)
				tab_etapa_procedimiento.setValor("ide_prpro", ide_prpro);
			
			tab_etapa_procedimiento.setValor("hora_dia_pretp","DIA");
			
			
		}else if(tab_etapa_procedimiento_empleado.isFocus()){
			tab_etapa_procedimiento_empleado.insertar();
		}
	}
	
	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_etapa_procedimiento.guardar()){
			tab_etapa_procedimiento_empleado.guardar();
		}
		guardarPantalla();
		tab_etapa_procedimiento_empleado.getColumna("ide_geedp").setCombo(ser_generalAdm.servicioEmpleadoContrato("true",pckUtilidades.CConversion.CInt(tab_etapa_procedimiento.getValor(("ide_geare")))));
		tab_etapa_procedimiento_empleado.ejecutarSql();
		utilitario.addUpdate("tab_etapa_procedimiento_empleado");
	}
	
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}
	
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		tab_etapa_procedimiento_empleado.getColumna("ide_geedp").setCombo(ser_generalAdm.servicioEmpleadoContrato("true",pckUtilidades.CConversion.CInt(tab_etapa_procedimiento.getValor(("ide_geare")))));
		//tab_etapa_procedimiento_empleado.dibujar();
		tab_etapa_procedimiento_empleado.ejecutarSql();
		utilitario.addUpdate("tab_etapa_procedimiento_empleado");
		//utilitario.addUpdateTabla(tab_detalle_factura, "total_fadef", "tab_factura");
	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		tab_etapa_procedimiento_empleado.getColumna("ide_geedp").setCombo(ser_generalAdm.servicioEmpleadoContrato("true",pckUtilidades.CConversion.CInt(tab_etapa_procedimiento.getValor(("ide_geare")))));
		//tab_etapa_procedimiento_empleado.dibujar();
		tab_etapa_procedimiento_empleado.ejecutarSql();
		utilitario.addUpdate("tab_etapa_procedimiento_empleado");
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		tab_etapa_procedimiento_empleado.getColumna("ide_geedp").setCombo(ser_generalAdm.servicioEmpleadoContrato("true",pckUtilidades.CConversion.CInt(tab_etapa_procedimiento.getValor(("ide_geare")))));
		//tab_etapa_procedimiento_empleado.dibujar();
		tab_etapa_procedimiento_empleado.ejecutarSql();
		utilitario.addUpdate("tab_etapa_procedimiento_empleado");
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		tab_etapa_procedimiento_empleado.getColumna("ide_geedp").setCombo(ser_generalAdm.servicioEmpleadoContrato("true",pckUtilidades.CConversion.CInt(tab_etapa_procedimiento.getValor(("ide_geare")))));
		//tab_etapa_procedimiento_empleado.dibujar();
		tab_etapa_procedimiento_empleado.ejecutarSql();
		utilitario.addUpdate("tab_etapa_procedimiento_empleado");
	}
	
	public Tabla getTab_etapa_procedimiento() {
		return tab_etapa_procedimiento;
	}

	public void setTab_etapa_procedimiento(Tabla tab_etapa_procedimiento) {
		this.tab_etapa_procedimiento = tab_etapa_procedimiento;
	}

	public Tabla getTab_etapa_procedimiento_empleado() {
		return tab_etapa_procedimiento_empleado;
	}

	public void setTab_etapa_procedimiento_empleado(
			Tabla tab_etapa_procedimiento_empleado) {
		this.tab_etapa_procedimiento_empleado = tab_etapa_procedimiento_empleado;
	}

	public Combo getCom_area() {
		return com_area;
	}

	public void setCom_area(Combo com_area) {
		this.com_area = com_area;
	}

	public Combo getCom_procedimiento() {
		return com_procedimiento;
	}

	public void setCom_procedimiento(Combo com_procedimiento) {
		this.com_procedimiento = com_procedimiento;
	}
	
	
}
