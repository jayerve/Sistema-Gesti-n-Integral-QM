package paq_sumillas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;

import org.primefaces.component.chart.pie.PieChart;
import org.primefaces.model.chart.PieChartModel;

import paq_sistema.aplicacion.Pantalla;
import paq_sumillas.ejb.ServicioReporteSumillas;
import framework.aplicacion.Columna;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Grid;

public class pre_rep_sumillas_comparativo extends Pantalla{
	
	private Tabla tab_ejecucionP = new Tabla();
	private Grid grid_grafico = new Grid();
	private Combo com_anio = new Combo();
	private Calendario cal_fecha_ini = new Calendario();
	private Calendario cal_fecha_fin = new Calendario(); 
	private PieChartModel pieModel;
	private PieChart piechart;
	private Grid grid_tabla_info = new Grid();
	TablaGenerica tab_consulta;

    @EJB
    private ServicioReporteSumillas ser_reporte = (ServicioReporteSumillas) utilitario.instanciarEJB(ServicioReporteSumillas.class);

	public pre_rep_sumillas_comparativo(){
		bar_botones.limpiar();
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		
		//com_anio.setMetodo("actualizaMes");
		cal_fecha_ini.setValue(utilitario.obtenerFechaInicioAnio());
		bar_botones.agregarComponente(new Etiqueta(" Desde:"));
		bar_botones.agregarComponente(cal_fecha_ini);
		
	
		bar_botones.agregarComponente(new Etiqueta(" Hasta:"));
		cal_fecha_ini.setValue(utilitario.obtenerFechaInicioAnio());
		bar_botones.agregarComponente(cal_fecha_ini);
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicio :"));
		cal_fecha_fin.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_fin);
		
		
		Boton bot_reimprimir=new Boton();
		bot_reimprimir.setIcon("ui-icon-image"); //mas iconos https://api.jqueryui.com/theming/icons/
		bot_reimprimir.setValue("Visualizar");
		bot_reimprimir.setMetodo("reimprime");
		bar_botones.agregarBoton(bot_reimprimir);
		
		tab_ejecucionP.setId("tab_ejecucionP");
		//tab_ejecucionP.setTabla("sum_tramite","ide_tramite_smtra",1);	
		tab_ejecucionP.setSql(ser_reporte.getDetalleEstado(cal_fecha_ini.getFecha(), cal_fecha_fin.getFecha()));
		
		tab_ejecucionP.getColumna("fecha_sumilla_smtre").setNombreVisual("Fecha");
		tab_ejecucionP.getColumna("gerencia").setNombreVisual("Gerencia");
		tab_ejecucionP.getColumna("cordinacion").setNombreVisual("Coordinación");
		tab_ejecucionP.getColumna("cumplido").setNombreVisual("Cumplido");
		tab_ejecucionP.getColumna("incumplido").setNombreVisual("Incumplido");
		tab_ejecucionP.getColumna("en_proceso").setNombreVisual("En Proceso");
		tab_ejecucionP.getColumna("asignado").setNombreVisual("Asignado");
		tab_ejecucionP.setColumnaSuma("cumplido,incumplido,en_proceso,asignado");
		
		tab_ejecucionP.setRows(30);
		tab_ejecucionP.setLectura(true);
		tab_ejecucionP.dibujar();
		
		tab_consulta = new TablaGenerica();
		tab_consulta = utilitario.consultar(ser_reporte.getPorcentajePeriodo(cal_fecha_ini.getFecha(), cal_fecha_fin.getFecha()));
		List<Columna> columnasIniciales = new ArrayList<Columna>(
				Arrays.asList(tab_consulta.getColumnas()));
		
		
		
		List<DatoTranspuesto> datos_transpuestos = new ArrayList<DatoTranspuesto>();
		DatoTranspuesto dato;
		
		//System.out.println(tab_consulta.getTotalFilas());
		for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
			
			dato = new DatoTranspuesto();

			dato.setEtiqueta(tab_consulta.getValor(i, columnasIniciales.get(0).getNombre()));
			dato.setValor(tab_consulta.getValor(i, columnasIniciales.get(1).getNombre()));

			datos_transpuestos.add(dato);
		}
		graficarPastel(datos_transpuestos, "Desde: " + cal_fecha_ini.getFecha() + " Hasta: " + cal_fecha_fin.getFecha());
        
		PanelTabla pat_dat_gen = new PanelTabla();  
		pat_dat_gen.setPanelTabla(tab_ejecucionP);    
		Division div_division = new Division();  
		//div_division.dividir1(pat_dat_gen);  
		div_division.dividir2(pat_dat_gen, grid_grafico, "50%", "V");
		agregarComponente(div_division); 
	}
	
	// Clase interna para representar los datos de la tabla transpuesta
		class DatoTranspuesto {
			// Atributos
			private String etiqueta;
			private String valor;

			// Constructores
			public DatoTranspuesto() {
				etiqueta = "";
				valor = "";
			}

			public DatoTranspuesto(String etiqueta, String valor) {
				this.etiqueta = etiqueta;
				this.valor = valor;
			}

			// Getters & Setters
			public String getEtiqueta() {
				return etiqueta;
			}

			public void setEtiqueta(String etiqueta) {
				this.etiqueta = etiqueta;
			}

			public String getValor() {
				return valor;
			}

			public void setValor(String valor) {
				this.valor = valor;
			}
		}
	
	public void graficarPastel(List<DatoTranspuesto> datos_transpuestos,
			String titulo) {
		try {
			
			// Grid del grafico
			grid_grafico.getChildren().clear();

			// Etiqueta
			String eti_tabla = "Estado trámites asignados";

			Etiqueta eti = new Etiqueta(eti_tabla);
			eti.setStyle("font-size:18px;");
			grid_grafico.setHeader(eti);
			grid_grafico.setId("grid_grafico");
			

			// Grid para graficar la tabla de informacion
			grid_tabla_info.getChildren().clear();
			grid_tabla_info.setColumns(2);

			// Etiquetas para llenar la tabla de informacion
			Etiqueta eti_descripcion;
			Etiqueta eti_valor;

			// Grafico de pastel
			pieModel = new PieChartModel();

			// Sumatoria de los valores
			double total = 0;

			// Barrido de todas las filas
			for (int i = 0; i < datos_transpuestos.size(); i++) {
				// Obteniendo el valor de la etiqueta
				String etiqueta = datos_transpuestos.get(i).getEtiqueta()
						.replace("_", " ");

				// Obteniendo el valor de la columna
				double valor = pckUtilidades.CConversion.CDbl_2(datos_transpuestos.get(i)
						.getValor());

				// Llenando los datos del pastel
				pieModel.set(etiqueta, valor);

				// Sumatoria de los valores
				total = total + valor;

				// Agregando las etiquetas a la tabla info
				eti_descripcion = new Etiqueta();
				eti_descripcion.setValue(etiqueta + ":");
				eti_descripcion
						.setStyle("font-weight: bold; padding-right: 20px;");
				grid_tabla_info.getChildren().add(eti_descripcion);

				eti_valor = new Etiqueta();
				eti_valor.setValue(utilitario.getFormatoNumero(valor, 2));
				eti_valor.setStyle("float: right;");
				grid_tabla_info.getChildren().add(eti_valor);
			}

			// Titulo del grafico
			piechart = new PieChart();
			piechart.setTitle(titulo);
			piechart.setId("piechart");

			// Modelo de grafico (pastel)
			//piechart.setId("pastel");
			piechart.setValue(pieModel);
			piechart.setFill(true);
			piechart.setShowDataLabels(true);
			piechart.setDiameter(300);
			piechart.setSliceMargin(5);
			piechart.setLegendPosition("e");
			piechart.setStyle("width:600px;height:600px");
			grid_grafico.getChildren().add(piechart);

			//utilitario.addUpdate("div_division, bot_exportar");
		} catch (Exception ex) {
			System.out.println("Error al graficar: "
					+ ex.getMessage());
		}
	}
	
	public void reimprime()
	{
		tab_ejecucionP.setId("tab_ejecucionP");
		//tab_ejecucionP.setTabla("sum_tramite","ide_tramite_smtra",1);	
		tab_ejecucionP.setSql(ser_reporte.getDetalleEstado(cal_fecha_ini.getFecha(), cal_fecha_fin.getFecha()));
		
		tab_ejecucionP.getColumna("fecha_sumilla_smtre").setNombreVisual("Fecha");
		tab_ejecucionP.getColumna("gerencia").setNombreVisual("Gerencia");
		tab_ejecucionP.getColumna("cordinacion").setNombreVisual("Coordinación");
		tab_ejecucionP.getColumna("cumplido").setNombreVisual("Cumplido");
		tab_ejecucionP.getColumna("incumplido").setNombreVisual("Incumplido");
		tab_ejecucionP.getColumna("en_proceso").setNombreVisual("En Proceso");
		tab_ejecucionP.getColumna("asignado").setNombreVisual("Asignado");
		
		tab_ejecucionP.setRows(30);
		tab_ejecucionP.setLectura(true);
		tab_ejecucionP.dibujar();
        utilitario.addUpdate("tab_sumillas");
        
        tab_consulta = new TablaGenerica();
		tab_consulta = utilitario.consultar(ser_reporte.getPorcentajePeriodo(cal_fecha_ini.getFecha(), cal_fecha_fin.getFecha()));
		List<Columna> columnasIniciales = new ArrayList<Columna>(
				Arrays.asList(tab_consulta.getColumnas()));
		
		
		
		List<DatoTranspuesto> datos_transpuestos = new ArrayList<DatoTranspuesto>();
		DatoTranspuesto dato;
		
		System.out.println(tab_consulta.getTotalFilas());
		if(tab_consulta.getTotalFilas()>0){
		for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
			
			dato = new DatoTranspuesto();

			dato.setEtiqueta(tab_consulta.getValor(i, columnasIniciales.get(0).getNombre()));
			dato.setValor(tab_consulta.getValor(i, columnasIniciales.get(1).getNombre()));

			datos_transpuestos.add(dato);
		}
		
		graficarPastel(datos_transpuestos, "Desde: " + cal_fecha_ini.getFecha() + " Hasta: " + cal_fecha_fin.getFecha());
		//utilitario.addUpdate("div_division");
			
		}
		else{
			grid_grafico.getChildren().clear();
		}
		//utilitario.addUpdate("piechart");
		utilitario.addUpdate("grid_grafico");
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

	public Tabla getTab_ejecucionP() {
		return tab_ejecucionP;
	}

	public void setTab_ejecucionP(Tabla tab_ejecucionP) {
		this.tab_ejecucionP = tab_ejecucionP;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public ServicioReporteSumillas getSer_reporte() {
		return ser_reporte;
	}

	public void setSer_reporte(ServicioReporteSumillas ser_reporte) {
		this.ser_reporte = ser_reporte;
	}

	public Calendario getCal_fecha_ini() {
		return cal_fecha_ini;
	}

	public void setCal_fecha_ini(Calendario cal_fecha_ini) {
		this.cal_fecha_ini = cal_fecha_ini;
	}

	public Calendario getCal_fecha_fin() {
		return cal_fecha_fin;
	}

	public void setCal_fecha_fin(Calendario cal_fecha_fin) {
		this.cal_fecha_fin = cal_fecha_fin;
	}
	
}
