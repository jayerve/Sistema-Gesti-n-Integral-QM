package paq_tesoreria;

import javax.ejb.EJB;

import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;

public class pre_anticipo_escombrera extends Pantalla {
	
	private Tabla tab_anticiposEsc = new Tabla();
    private Combo com_anio=new Combo();
    
    @EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
    @EJB
    private ServicioTesoreria ser_Tesoreria = (ServicioTesoreria) utilitario.instanciarEJB(ServicioTesoreria.class);
	
	public pre_anticipo_escombrera(){
		
		bar_botones.limpiar();
		
		com_anio.setCombo(ser_contabilidad.getAnio("true,false","true,false"));		
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setTitle("Limpiar");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
		
		Boton bot_excel=new Boton();
	    bot_excel.setValue("Exportar Auxiliar Anticipos EXCEL");
	    bot_excel.setIcon("ui-icon-calculator");
	    bot_excel.setAjax(false);
	    bot_excel.setMetodo("exportarExcel");
	    bar_botones.agregarBoton(bot_excel);

		tab_anticiposEsc.setId("tab_anticiposEsc");
		tab_anticiposEsc.setSql(ser_Tesoreria.getRptAnticiposContabilizados("-1"));
		
		tab_anticiposEsc.setRows(30);
		tab_anticiposEsc.setLectura(true);
		tab_anticiposEsc.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_anticiposEsc);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);

	}
	
	public void seleccionaElAnio (){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		
		cargaAnticipos();
	}

	public void cargaAnticipos()
	{		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			
		}
		
		tab_anticiposEsc.setSql(ser_Tesoreria.getRptAnticiposContabilizados(com_anio.getValue().toString()));
		tab_anticiposEsc.ejecutarSql();
		utilitario.addUpdate("tab_anticiposEsc");

	}
	
	public void exportarExcel()
	{
	      if(com_anio.getValue()==null){
	        utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
	        return;
	      }
	      Tabla tab_tablaXls = new Tabla();
	      tab_tablaXls.setSql(ser_Tesoreria.getRptAuxiliarAnticiposContabilizados(com_anio.getValue().toString()));
	      tab_tablaXls.ejecutarSql();
	      tab_tablaXls.exportarXLS();
    }
	
	public void limpiar(){
		
		tab_anticiposEsc.limpiar();
		
		tab_anticiposEsc.setSql(ser_Tesoreria.getRptAnticiposContabilizados("-1"));
		tab_anticiposEsc.ejecutarSql();
		utilitario.addUpdate("tab_anticiposEsc");
	}
	
	
	/////botones fin,siguiente,atras,ultimo.inicio
    @Override
    public void inicio() {
        // TODO Auto-generated method stub
        super.inicio();
    }
    
    @Override
    public void siguiente() {
        // TODO Auto-generated method stub
        super.siguiente();
    }
    
    @Override
    public void atras() {
        // TODO Auto-generated method stub
        super.atras();
    }

    @Override
    public void fin() {
        // TODO Auto-generated method stub
        super.fin();
    }
    
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
	}

	public Tabla getTab_anticiposEsc() {
		return tab_anticiposEsc;
	}

	public void setTab_anticiposEsc(Tabla tab_anticiposEsc) {
		this.tab_anticiposEsc = tab_anticiposEsc;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}
	
	

}
