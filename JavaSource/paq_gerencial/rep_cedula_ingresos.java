package paq_gerencial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;

import org.primefaces.component.chart.bar.BarChart;
import org.primefaces.component.chart.pie.PieChart;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.outputpanel.OutputPanel;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.PieChartModel;

import framework.aplicacion.Columna;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Ajax;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.Radio;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gerencial.ejb.ServicioReportesGerencial;
import paq_sistema.aplicacion.Pantalla;

public class rep_cedula_ingresos extends Pantalla {

	// ComboBox del anio
	private Combo com_anio = new Combo();

	// ComboBox de la opcion
	private Combo com_opcion = new Combo();

	// Crea un grupo de radio buttons
	private Radio tipo_grafico = new Radio();

	// Componentes de la pantalla
	private Grid grid_grafico = new Grid();
	private Division div_division = new Division();
	private Tabla tab_datos = new Tabla();
	private Dialogo dia_tipo_rep = new Dialogo();
	private Panel panel_info = new Panel();
	private Grid grid_tabla_info = new Grid();

	// Graficos de barra y pastel
	public BarChart barchart = new BarChart();
	private PieChart piechart = new PieChart();
	public CartesianChartModel categoryModel;
	private PieChartModel pieModel;

	// Tipo de grafico a realizar
	String id_tipo_grafico = "0";

	// Botones
	Boton bot_graficar;
	Boton bot_exportar;

	// Etiquetas
	String titulo_grafico;
	Etiqueta eti_tabla_info = new Etiqueta();

	// Tablas
	TablaGenerica tab_consulta;

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario
			.instanciarEJB(ServicioContabilidad.class);

	public rep_cedula_ingresos() {
		// Vaciando la barra de botones
		bar_botones.limpiar();

		// ComboBox para escoger el anio
		com_anio.setId("com_anio");
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false",
				"true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		try {
			// ComboBox para escoger la opcion
			com_opcion.setCombo(ServicioReportesGerencial
					.obtenerNombresCedulasIngresos(
							pckUtilidades.CConversion.CInt(com_anio.getValue().toString()),
							1, 1));
		} catch (Exception ex) {
			System.out
					.println("Error en el Combo Opciones: " + ex.getMessage());
		}

		com_opcion.setMetodo("seleccionaLaOpcion");
		com_opcion.setId("com_opcion");
		bar_botones.agregarComponente(new Etiqueta("Seleccione la opcion:"));
		bar_botones.agregarComponente(com_opcion);

		// Boton para exportar el reporte
		bot_exportar = new Boton();
		bot_exportar.setId("bot_exportar");
		bot_exportar.setValue("Exportar como imagen");
		bot_exportar.setType("button");
		bot_exportar.setIcon("ui-icon-image");
		bar_botones.agregarBoton(bot_exportar);

		// Dialogo donde se exportar el grafico
		Dialog dia_expor = new Dialog();
		dia_expor.setModal(true);
		dia_expor.setHeader("");
		dia_expor.setShowEffect("fade");
		dia_expor.setWidgetVar("dlg");
		dia_expor.setAppendToBody(true);

		// Panel para mostrar el grafico
		OutputPanel out_panel = new OutputPanel();
		out_panel.setId("output");
		out_panel.setLayout("block");
		out_panel.setStyle("width:100%; height:100%");
		dia_expor.getChildren().add(out_panel);

		// Agregando el componente
		agregarComponente(dia_expor);

		// Division de la pantalla
		div_division.setId("div_division");
		dibujarTabla();
		agregarComponente(div_division);

		// Grafico de pastel
		piechart.setId("pastel");
		piechart.setWidgetVar("pastel");
		piechart.setFill(true);
		piechart.setShowDataLabels(true);
		piechart.setDiameter(300);
		piechart.setSliceMargin(5);
		piechart.setLegendPosition("e");
		piechart.setStyle("width:500px; height:400px");
		piechart.setTitle(titulo_grafico);

		Ajax evt1 = new Ajax();
		evt1.setMetodo("itemSelectPastel");
		piechart.addClientBehavior("itemSelect", evt1);

		// Lista con las opciones para los radio buttons
		List<Object[]> lista = new ArrayList<Object[]>();
		Object fila1[] = { "0", "DEVENGADO" };
		Object fila2[] = { "1", "COBRADO" };
		lista.add(fila1);
		lista.add(fila2);

		// Radio Buttons
		tipo_grafico.setRadio(lista);
		tipo_grafico.setVertical();
		tipo_grafico.setMetodoChange("cambiarTipoGrafico");

		// Panel
		panel_info.getChildren().add(tipo_grafico);

		// Etiqueta de espacio
		Etiqueta eti_espacio = new Etiqueta("");
		eti_espacio.setStyle("border: none;");
		panel_info.getChildren().add(eti_espacio);

		// Etiqueta de titulo
		eti_tabla_info
				.setEstiloContenido("font-size:18px; weight: bold; border: none;");
		panel_info.getChildren().add(eti_tabla_info);

		// Grid para graficar la tabla de informacion
		grid_tabla_info.getChildren().clear();
		panel_info.getChildren().add(grid_tabla_info);

		// Crea una division vertical
		div_division.getChildren().clear();

		div_division.dividir2(panel_info, grid_grafico, "20%", "V");
		div_division.getDivision1().setHeader(titulo_grafico);
		div_division.getDivision1().setCollapsible(true);
	}

	// Llama al metodo onClick del boton para graficar el pastel
	public void graficarPastel(List<DatoTranspuesto> datos_transpuestos,
			String titulo) {
		try {
			// Valor del metodo onClick para abrir un nuevo dialogo con el
			// grafico
			bot_exportar
					.setOnclick("$('#formulario\\:output').empty().append( PF('pastel').exportAsImage() );PF('dlg').show(); ");

			// $("#form\\:");

			// Grid del grafico
			grid_grafico.getChildren().clear();

			// Etiqueta
			String eti_tabla = "CEDULAS DE INGRESO";

			eti_tabla_info.setValue(titulo_grafico);

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
				double valor = Double.parseDouble(datos_transpuestos.get(i)
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
			piechart.setTitle(titulo);

			// Modelo de grafico (pastel)
			piechart.setValue(pieModel);
			grid_grafico.getChildren().add(piechart);

			utilitario.addUpdate("div_division, bot_exportar");
		} catch (Exception ex) {
			System.out.println("Cedulas de Ingresos. Error al graficar: "
					+ ex.getMessage());
		}
	}

	// Dibuja la tabla, el grafico y la division
	public void dibujarTabla() {
		Etiqueta eti = new Etiqueta("");
		eti.setStyle("font-size:18px;");
		grid_grafico.setHeader(eti);
		grid_grafico.setId("grid_grafico");

		// Limpiando los elementos hijos de la division
		div_division.getChildren().clear();

		// Agrega el grafico a la division
		div_division.dividir1(grid_grafico);
	}

	// Metodo que se ejecuta cada vez que se selecciona un valor del comboBox
	// del Anio
	public void seleccionaElAnio() {
		// Llenando el ComboBox de las opciones
		try {
			// ComboBox para escoger la opcion
			com_opcion.setCombo(ServicioReportesGerencial
					.obtenerNombresCedulasIngresos(
							pckUtilidades.CConversion.CInt(com_anio.getValue().toString()),
							1, 1));

			// Refrescando el ComboBox de las opciones
			utilitario.addUpdate("com_opcion");
		} catch (Exception ex) {
			System.out
					.println("Error en el Combo Opciones: " + ex.getMessage());
		}
	}

	// Metodo que se ejecuta cada vez que se selecciona un valor del comboBox de
	// la Opcion
	public void seleccionaLaOpcion() {
		cambiarTipoGrafico();
	}

	// Metodo que se ejecuta cada vez que se escoge el tipo de grafico
	public void cambiarTipoGrafico() {
		try {
			// Guardando el tipo de grafico escogido
			id_tipo_grafico = tipo_grafico.getValue().toString();

			// Tabla de consulta
			tab_consulta = new TablaGenerica();

			tab_consulta = utilitario.consultar(ServicioReportesGerencial
					.obtenerSQLCedulasIngresos(
							pckUtilidades.CConversion.CInt(com_anio.getValue().toString()),
							com_opcion.getValue().toString(), 1, 1));

			System.out.println("tab_consulta: " + tab_consulta.getSql());

			// Columnas (inicial)
			List<Columna> columnasIniciales = new ArrayList<Columna>(
					Arrays.asList(tab_consulta.getColumnas()));

			// Columnas (seleccionadas)
			List<Columna> columnasSeleccionadas = new ArrayList<Columna>();

			// Copiando las columnas innecesarias en una nueva lista
			Columna columnaNueva;

			for (Columna columna : columnasIniciales) {
				// Guardando el titulo de la tabla y del grafico
				if (columna.getNombre().toUpperCase()
						.contentEquals("DESCRIPCION")) {
					titulo_grafico = tab_consulta.getValor(0, "DESCRIPCION");
				}

				// Devengado
				if (id_tipo_grafico.contentEquals("0")) {
					if (columna.getNombre().toUpperCase()
							.contentEquals("DEVENGADO")
							|| columna.getNombre().toUpperCase()
									.contentEquals("SALDO_DEVENGAR")) {
						columnaNueva = columna;
						columnasSeleccionadas.add(columnaNueva);
					}
				}

				// Cobrado
				if (id_tipo_grafico.contentEquals("1")) {
					if (columna.getNombre().toUpperCase()
							.contentEquals("COBRADO")
							|| columna.getNombre().toUpperCase()
									.contentEquals("SALDO_COBRAR")) {
						columnaNueva = columna;
						columnasSeleccionadas.add(columnaNueva);
					}
				}
			}

			// ///////////////////////////////////////////////////////////////////////////
			// Transponiendo las filas y columnas

			// Lista de valores
			List<String> valores = new ArrayList<String>();

			// Valor de la celda
			String valor;

			// Copiando los valores
			for (Columna columna : columnasSeleccionadas) {
				valor = tab_consulta.getValor(0, columna.getNombre());

				valores.add(valor);
			}

			// Lista que contiene los datos transpuestos de la tabla
			List<DatoTranspuesto> datos_transpuestos = new ArrayList<rep_cedula_ingresos.DatoTranspuesto>();

			// Dato transpuesto a instanciarse
			DatoTranspuesto dato;

			// Llenando los datos transpuestos
			for (int i = 0; i < columnasSeleccionadas.size(); i++) {
				dato = new DatoTranspuesto();

				dato.setEtiqueta(columnasSeleccionadas.get(i).getNombre());
				dato.setValor(valores.get(i));

				datos_transpuestos.add(dato);
			}

			// Graficando la tabla y el pastel
			graficarPastel(datos_transpuestos, titulo_grafico);
		} catch (Exception ex) {
			System.out.println("Error al mostrar el grafico: "
					+ ex.getMessage());
		}
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

	// Metodos del framework
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

	// Acceso a las variables (Getters y Setters)
	public Grid getGrid_grafico() {
		return grid_grafico;
	}

	public void setGrid_grafico(Grid grid_grafico) {
		this.grid_grafico = grid_grafico;
	}

	public Division getDiv_division() {
		return div_division;
	}

	public void setDiv_division(Division div_division) {
		this.div_division = div_division;
	}

	public Tabla getTab_datos() {
		return tab_datos;
	}

	public void setTab_datos(Tabla tab_datos) {
		this.tab_datos = tab_datos;
	}

	public Dialogo getDia_tipo_rep() {
		return dia_tipo_rep;
	}

	public void setDia_tipo_rep(Dialogo dia_tipo_rep) {
		this.dia_tipo_rep = dia_tipo_rep;
	}

	public BarChart getBarchart() {
		return barchart;
	}

	public void setBarchart(BarChart barchart) {
		this.barchart = barchart;
	}

	public PieChart getPiechart() {
		return piechart;
	}

	public void setPiechart(PieChart piechart) {
		this.piechart = piechart;
	}

	public CartesianChartModel getCategoryModel() {
		return categoryModel;
	}

	public void setCategoryModel(CartesianChartModel categoryModel) {
		this.categoryModel = categoryModel;
	}

	public PieChartModel getPieModel() {
		return pieModel;
	}

	public void setPieModel(PieChartModel pieModel) {
		this.pieModel = pieModel;
	}

	public Boton getBot_graficar() {
		return bot_graficar;
	}

	public void setBot_graficar(Boton bot_graficar) {
		this.bot_graficar = bot_graficar;
	}

	public Boton getBot_exportar() {
		return bot_exportar;
	}

	public void setBot_exportar(Boton bot_exportar) {
		this.bot_exportar = bot_exportar;
	}

	public String getTitulo_grafico() {
		return titulo_grafico;
	}

	public void setTitulo_grafico(String titulo_grafico) {
		this.titulo_grafico = titulo_grafico;
	}

	public TablaGenerica getTab_consulta() {
		return tab_consulta;
	}

	public void setTab_consulta(TablaGenerica tab_consulta) {
		this.tab_consulta = tab_consulta;
	}

}
