package paq_anticipos;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_anticipos.ejb.ServicioAnticipo;
import paq_sistema.aplicacion.Pantalla;

public class pre_simulador_tabla_amortizacion extends Pantalla {

	private Tabla tab_datos=new Tabla();
	private Tabla tab_amortizacion=new Tabla();
	
	private Calendario cal_fecha_rd4=new Calendario();
	private Texto tex_valor_rd4=new Texto();
	private Calendario cal_fecha_rd3=new Calendario();
	private Texto tex_valor_rd3=new Texto();

	@EJB
	private ServicioAnticipo ser_anticipo = (ServicioAnticipo) utilitario.instanciarEJB(ServicioAnticipo.class);	

	public pre_simulador_tabla_amortizacion(){


		Boton bot_generar=new Boton();
		bot_generar.setValue("Generar");
		bot_generar.setMetodo("generarTabla");
		bar_botones.agregarBoton(bot_generar);

		tab_datos.setId("tab_datos");
		tab_datos.setSql("select 1 as ide_datos , 0 as monto , 0 as plazo , 0 as tasa_interes, " +
				"30 as  amortizacion_cada,  " +
				" ('"+utilitario.getFechaActual()+"','yy-mm-dd') as fecha_inicio ");
		tab_datos.setCampoPrimaria("ide_datos");
		tab_datos.setLectura(false);
		tab_datos.getColumna("monto").setMetodoChange("cambiaDatos");
		tab_datos.getColumna("plazo").setMetodoChange("cambiaDatos");
		tab_datos.getColumna("tasa_interes").setMetodoChange("cambiaDatos");
		tab_datos.getColumna("amortizacion_cada").setMetodoChange("cambiaDatos");
		tab_datos.getColumna("amortizacion_cada").setLectura(true);
		
		//		tab_datos.getColumna("fecha_inicio").setMetodoChange("cambiaDatos");
		tab_datos.setTipoFormulario(true);
		tab_datos.getGrid().setColumns(4);
		tab_datos.getColumna("ide_datos").setVisible(false);
		tab_datos.setMostrarNumeroRegistros(false);
		tab_datos.dibujar();
		PanelTabla pat_tab=new PanelTabla();
		pat_tab.setPanelTabla(tab_datos);
		pat_tab.setMensajeWarn("DATOS AMORTIZACION");

		tex_valor_rd3.setSoloNumeros();
		tex_valor_rd3.setMetodoChange("cambiaDatos");
		tex_valor_rd4.setSoloNumeros();
		tex_valor_rd4.setMetodoChange("cambiaDatos");
		
		Grid gri_rd=new Grid();
		gri_rd.setColumns(4);
		gri_rd.getChildren().add(new Etiqueta("FECHA RUBRO D4 "));
		gri_rd.getChildren().add(cal_fecha_rd4);
		gri_rd.getChildren().add(new Etiqueta("VALOR RUBRO DESCUENTO D4 "));
		gri_rd.getChildren().add(tex_valor_rd4);
		gri_rd.getChildren().add(new Etiqueta("FECHA RUBRO D3 "));
		gri_rd.getChildren().add(cal_fecha_rd3);
		gri_rd.getChildren().add(new Etiqueta("VALOR RUBRO DESCUENTO D3 "));
		gri_rd.getChildren().add(tex_valor_rd3);
		

		Grid gri_datos=new Grid();
		gri_datos.getChildren().add(pat_tab);
		gri_datos.getChildren().add(gri_rd);

		tab_amortizacion.setId("tab_amortizacion");
		tab_amortizacion.setSql("select IDE_NRAMO,NRO_CUOTA_NRAMO,CUOTA_NRAMO,FECHA_VENCIMIENTO_NRAMO,CAPITAL_NRAMO,INTERES_NRAMO,PRINCIPAL_NRAMO from NRH_AMORTIZACION where IDE_NRAMO=-1");
		tab_amortizacion.setColumnaSuma("CUOTA_NRAMO,CAPITAL_NRAMO,INTERES_NRAMO,PRINCIPAL_NRAMO");
		tab_amortizacion.setCampoPrimaria("IDE_NRAMO");
		tab_amortizacion.setRecuperarLectura(true);
		tab_amortizacion.dibujar();

		PanelTabla pat_tab1=new PanelTabla();
		pat_tab1.setPanelTabla(tab_amortizacion);
		pat_tab1.setMensajeWarn("TABLA DE AMORTIZACION");


		Division div1=new Division();
		div1.dividir2(gri_datos, pat_tab1,"35%", "H");

		agregarComponente(div1);


	}



	public void cambiaDatos(AjaxBehaviorEvent evt){
		tab_datos.modificar(evt);
		generarTabla();
	}

	public void generarTabla(){
		double monto=0;
		try {
			monto=Double.parseDouble(tab_datos.getValor("monto"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		double tasa_int=0;
		try {
			tasa_int=Double.parseDouble(tab_datos.getValor("tasa_interes"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		int plazo=0;

		try {
			plazo=pckUtilidades.CConversion.CInt(tab_datos.getValor("plazo"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		int amort=0;

		try {
			amort=pckUtilidades.CConversion.CInt(tab_datos.getValor("amortizacion_cada"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		int gracia=0;

		
		
		String fecha_d3=cal_fecha_rd3.getFecha();
		String fecha_d4=cal_fecha_rd4.getFecha();
		
		
		if (tex_valor_rd3.getValue()!=null && !tex_valor_rd3.getValue().toString().isEmpty()
				&& Double.parseDouble(tex_valor_rd3.getValue()+"")==0){
			utilitario.agregarMensajeInfo("Atencion", "El Valor del Rubro Descuento D3 no puede ser Cero");
			return;
		}
		if (tex_valor_rd4.getValue()!=null && !tex_valor_rd4.getValue().toString().isEmpty()
				&& Double.parseDouble(tex_valor_rd4.getValue()+"")==0){
			utilitario.agregarMensajeInfo("Atencion", "El Valor del Rubro Descuento D4 no puede ser Cero");
			return;
		}

		double valor_d4=0;
		double valor_d3=0;
		try {
			valor_d3=Double.parseDouble(tex_valor_rd3.getValue()+"");
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			valor_d4=Double.parseDouble(tex_valor_rd4.getValue()+"");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		tab_amortizacion.setSql("select IDE_NRAMO,NRO_CUOTA_NRAMO,CUOTA_NRAMO,FECHA_VENCIMIENTO_NRAMO,CAPITAL_NRAMO,INTERES_NRAMO,PRINCIPAL_NRAMO from NRH_AMORTIZACION where IDE_NRAMO=-1");
		tab_amortizacion.ejecutarSql();

		TablaGenerica tab_amort=ser_anticipo.getTablaAmortizacionSimulador(monto, tasa_int, plazo, amort, gracia, tab_datos.getValor("fecha_inicio"), fecha_d4,valor_d4,fecha_d3,valor_d3);
		for (int i = 0; i < tab_amort.getTotalFilas(); i++) {
			tab_amortizacion.insertar();
			tab_amortizacion.setValor("CAPITAL_NRAMO", tab_amort.getValor(i, "CAPITAL_NRAMO"));
			tab_amortizacion.setValor("INTERES_NRAMO", tab_amort.getValor(i, "INTERES_NRAMO"));
			tab_amortizacion.setValor("CUOTA_NRAMO", tab_amort.getValor(i, "CUOTA_NRAMO"));
			tab_amortizacion.setValor("FECHA_VENCIMIENTO_NRAMO", tab_amort.getValor(i, "FECHA_VENCIMIENTO_NRAMO"));
			tab_amortizacion.setValor("PRINCIPAL_NRAMO", tab_amort.getValor(i, "PRINCIPAL_NRAMO"));
			tab_amortizacion.setValor("NRO_CUOTA_NRAMO", tab_amort.getValor(i, "NRO_CUOTA_NRAMO"));

		}

		tab_amortizacion.sumarColumnas();

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

	public Tabla getTab_datos() {
		return tab_datos;
	}

	public void setTab_datos(Tabla tab_datos) {
		this.tab_datos = tab_datos;
	}

	public Tabla getTab_amortizacion() {
		return tab_amortizacion;
	}

	public void setTab_amortizacion(Tabla tab_amortizacion) {
		this.tab_amortizacion = tab_amortizacion;
	}


}
