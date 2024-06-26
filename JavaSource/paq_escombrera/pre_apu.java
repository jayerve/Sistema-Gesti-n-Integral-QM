package paq_escombrera;


import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import paq_escombrera.ejb.ServicioEscombrera;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;

public class pre_apu extends Pantalla {

	private Tabla tab_apu = new Tabla();
	private Tabla tab_material = new Tabla();
	private Tabla tab_equipo = new Tabla();
	private Tabla tab_mano_obra = new Tabla();
	private Tabla tab_transporte = new Tabla();

	private Division div_division = new Division();

	@EJB
	private ServicioEscombrera ser_escombrera = (ServicioEscombrera) utilitario.instanciarEJB(ServicioEscombrera.class);
	
	public pre_apu() 
	{

		apuIngreso();
		tabuladores();
		
		PanelTabla pat_apu = new PanelTabla();
		pat_apu.setPanelTabla(tab_apu);

		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		PanelTabla pat_material = new PanelTabla();
		pat_material.setPanelTabla(tab_material);
		
		PanelTabla pat_equipo = new PanelTabla();
		pat_equipo.setPanelTabla(tab_equipo);
		
		PanelTabla pat_mano_obra = new PanelTabla();
		pat_mano_obra.setPanelTabla(tab_mano_obra);
		
		PanelTabla pat_transporte = new PanelTabla();
		pat_transporte.setPanelTabla(tab_transporte);
		
		tab_tabulador.agregarTab("MATERIALES", pat_material);
		tab_tabulador.agregarTab("EQUIPOS", pat_equipo);
		tab_tabulador.agregarTab("MANO DE OBRA", pat_mano_obra);
		tab_tabulador.agregarTab("TRANSPORTE", pat_transporte);
		
		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_apu, tab_tabulador, "60%", "H");
		agregarComponente(div_division);

	}
	
	private void apuIngreso() 
	{
		tab_apu.setId("tab_apu");
		tab_apu.setTipoFormulario(true);
		tab_apu.getGrid().setColumns(4);
		tab_apu.setTabla("apu_item_construccion", "ide_apitc", 1);
		tab_apu.getColumna("activo_apitc").setValorDefecto("true");
		tab_apu.getColumna("ide_bounm").setCombo("bodt_unidad_medida", "ide_bounm", "detalle_bounm,abreviatura_bounm", "");
		tab_apu.getColumna("ide_apcap").setCombo(ser_escombrera.getCapitulos("true"));
		tab_apu.getColumna("ide_apcap").setAutoCompletar();
		tab_apu.getColumna("costo_indirecto_apitc").setMetodoChange("calcularCostoI");
		tab_apu.getColumna("costo_indirecto_apitc").setValorDefecto("0");
		tab_apu.getColumna("activo_apitc").setValorDefecto("true");
		tab_apu.getColumna("total_costo_directo_apitc").setEtiqueta();
		tab_apu.getColumna("total_costo_directo_apitc").setFormatoNumero(2);
		tab_apu.getColumna("total_costo_directo_apitc").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red;");
		tab_apu.getColumna("costo_total_apitc").setEtiqueta();
		tab_apu.getColumna("costo_total_apitc").setFormatoNumero(2);
		tab_apu.getColumna("costo_total_apitc").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red;");
		
		tab_apu.agregarRelacion(tab_material);
		tab_apu.agregarRelacion(tab_equipo);
		tab_apu.agregarRelacion(tab_mano_obra);
		tab_apu.agregarRelacion(tab_transporte);

		tab_apu.dibujar();
	}
	
	public void tabuladores()
	{
		tab_material.setId("tab_material");
		tab_material.setIdCompleto("tab_tabulador:tab_material");
		tab_material.setTabla("apu_material", "ide_apmat", 2);
		tab_material.getColumna("activo_apmat").setValorDefecto("true");
		tab_material.getColumna("ide_bounm").setCombo("bodt_unidad_medida", "ide_bounm", "detalle_bounm,abreviatura_bounm", "");
		tab_material.getColumna("ide_aprub").setCombo(ser_escombrera.getRubros("true","1"));
		tab_material.getColumna("ide_aprub").setAutoCompletar();
		tab_material.getColumna("ide_aprub").setMetodoChange("seleccionoRubro1");
		tab_material.getColumna("cantidad_apmat").setMetodoChange("calcularDetalle1");
		tab_material.getColumna("precio_apmat").setEtiqueta();
		tab_material.getColumna("precio_apmat").setFormatoNumero(2);
		tab_material.getColumna("precio_apmat").setEstilo("font-size:13px;font-weight:bold;");
		tab_material.getColumna("costo_total_apmat").setEtiqueta();
		tab_material.getColumna("costo_total_apmat").setFormatoNumero(2);
		tab_material.getColumna("costo_total_apmat").setEstilo("font-size:13px;font-weight:bold;");
		tab_material.setColumnaSuma("costo_total_apmat"); 
		tab_material.dibujar();

		tab_equipo.setId("tab_equipo");
		tab_equipo.setIdCompleto("tab_tabulador:tab_equipo");
		tab_equipo.setTabla("apu_equipo", "ide_apequ", 3);
		tab_equipo.getColumna("activo_apequ").setValorDefecto("true");
		tab_equipo.getColumna("ide_aprub").setCombo(ser_escombrera.getRubros("true","2"));
		tab_equipo.getColumna("ide_aprub").setAutoCompletar();
		//tab_equipo.getColumna("ide_aprub").setMetodoChange("seleccionoRubro2");
		tab_equipo.getColumna("cantidad_apequ").setMetodoChange("calcularDetalle2");
		//tab_equipo.getColumna("rendimiento_apequ").setMetodoChange("calcularDetalle2");
		tab_equipo.getColumna("tarifa_apequ").setEtiqueta();
		tab_equipo.getColumna("tarifa_apequ").setFormatoNumero(2);
		tab_equipo.getColumna("tarifa_apequ").setEstilo("font-size:13px;font-weight:bold;");
		tab_equipo.getColumna("costo_hora_apequ").setEtiqueta();
		tab_equipo.getColumna("costo_hora_apequ").setFormatoNumero(2);
		tab_equipo.getColumna("costo_hora_apequ").setEstilo("font-size:13px;font-weight:bold;");
		tab_equipo.getColumna("rendimiento_apequ").setEtiqueta();
		tab_equipo.getColumna("rendimiento_apequ").setFormatoNumero(2);
		tab_equipo.getColumna("rendimiento_apequ").setEstilo("font-size:13px;font-weight:bold;");
		tab_equipo.getColumna("costo_apequ").setEtiqueta();
		tab_equipo.getColumna("costo_apequ").setFormatoNumero(2);
		tab_equipo.getColumna("costo_apequ").setEstilo("font-size:13px;font-weight:bold;");
		tab_equipo.setColumnaSuma("costo_apequ"); 
		tab_equipo.dibujar();

		tab_mano_obra.setId("tab_mano_obra");
		tab_mano_obra.setIdCompleto("tab_tabulador:tab_mano_obra");
		tab_mano_obra.setTabla("apu_mano_obra", "ide_apmao", 4);
		tab_mano_obra.getColumna("activo_apmao").setValorDefecto("true");
		tab_mano_obra.getColumna("ide_aprub").setCombo(ser_escombrera.getRubros("true","3"));
		tab_mano_obra.getColumna("ide_aprub").setAutoCompletar();
		tab_mano_obra.getColumna("cantidad_apmao").setMetodoChange("calcularDetalle3");
		//tab_mano_obra.getColumna("rendimiento_apmao").setMetodoChange("calcularDetalle3");
		tab_mano_obra.getColumna("jornal_apmao").setEtiqueta();
		tab_mano_obra.getColumna("jornal_apmao").setFormatoNumero(2);
		tab_mano_obra.getColumna("jornal_apmao").setEstilo("font-size:13px;font-weight:bold;");
		tab_mano_obra.getColumna("costo_hora_apmao").setEtiqueta();
		tab_mano_obra.getColumna("costo_hora_apmao").setFormatoNumero(2);
		tab_mano_obra.getColumna("costo_hora_apmao").setEstilo("font-size:13px;font-weight:bold;");
		tab_mano_obra.getColumna("rendimiento_apmao").setEtiqueta();
		tab_mano_obra.getColumna("rendimiento_apmao").setFormatoNumero(2);
		tab_mano_obra.getColumna("rendimiento_apmao").setEstilo("font-size:13px;font-weight:bold;");
		tab_mano_obra.getColumna("costo_apmao").setEtiqueta();
		tab_mano_obra.getColumna("costo_apmao").setFormatoNumero(2);
		tab_mano_obra.getColumna("costo_apmao").setEstilo("font-size:13px;font-weight:bold;");
		tab_mano_obra.setColumnaSuma("costo_apmao"); 
		tab_mano_obra.dibujar();
		
		tab_transporte.setId("tab_transporte");
		tab_transporte.setIdCompleto("tab_tabulador:tab_transporte");
		tab_transporte.setTabla("apu_transporte", "ide_aptra", 7);
		tab_transporte.getColumna("activo_aptra").setValorDefecto("true");
		tab_transporte.getColumna("ide_bounm").setCombo("bodt_unidad_medida", "ide_bounm", "detalle_bounm,abreviatura_bounm", "");
		tab_transporte.getColumna("ide_aprub").setCombo(ser_escombrera.getRubros("true","4"));
		tab_transporte.getColumna("ide_aprub").setAutoCompletar();
		tab_transporte.getColumna("ide_aprub").setMetodoChange("seleccionoRubro4");
		//tab_transporte.getColumna("cantidad_aptra").setMetodoChange("calcularDetalle4");
		tab_transporte.getColumna("precio_aptra").setEtiqueta();
		tab_transporte.getColumna("precio_aptra").setFormatoNumero(2);
		tab_transporte.getColumna("precio_aptra").setEstilo("font-size:13px;font-weight:bold;");
		tab_transporte.getColumna("costo_total_aptra").setEtiqueta();
		tab_transporte.getColumna("costo_total_aptra").setFormatoNumero(2);
		tab_transporte.getColumna("costo_total_aptra").setEstilo("font-size:13px;font-weight:bold;");
		tab_transporte.setColumnaSuma("costo_total_aptra"); 
		tab_transporte.dibujar();
		
	}
	
	public void seleccionoRubro1(SelectEvent evt) 
	{
		tab_material.modificar(evt); 
		calcular1();
	}
	
	public void calcularDetalle1(AjaxBehaviorEvent evt) {
		tab_material.modificar(evt); // Siempre es la primera linea
		calcular1();
	}
	
	public void calcular1() 
	{
		String str_seleccionado = tab_material.getValor("ide_aprub");
		TablaGenerica validarTarifaUnica = utilitario.consultar("select ide_aprut, valor_aprut,ide_bounm from apu_rubro_tarifa where activo_aprut=true and coalesce(bloqueado_aprut,false)=false and ide_aprub=" + str_seleccionado);
		validarTarifaUnica.imprimirSql();
		double dou_valor_tarifa = 0;
		double dou_cantidad_fadef = 0;
		double dou_total_fadef = 0;

		if (!validarTarifaUnica.isEmpty()) {
			dou_valor_tarifa = pckUtilidades.CConversion.CDbl_2(validarTarifaUnica.getValor("valor_aprut"));
			tab_material.setValor("ide_bounm", validarTarifaUnica.getValor("ide_bounm"));
			System.out.println("dou_valor_tarifa "+dou_valor_tarifa);
		} else {
			// Mensaje
			utilitario.agregarMensajeInfo("No existen tarifas para el item seleccionado", "");
		}
		
		dou_cantidad_fadef=pckUtilidades.CConversion.CDbl_2(tab_material.getValor("cantidad_apmat"));
		dou_total_fadef=dou_cantidad_fadef*dou_valor_tarifa;
		
		System.out.println("dou_total_fadef "+dou_total_fadef);
		
		tab_material.setValor("precio_apmat", utilitario.getFormatoNumero(dou_valor_tarifa, 2));
		tab_material.setValor("costo_total_apmat", dou_total_fadef+"");
		tab_material.sumarColumnas();
		utilitario.addUpdate("tab_tabulador:tab_material");
		calcularTodos();
	}
	/*
	public void seleccionoRubro2(SelectEvent evt) 
	{
		tab_equipo.modificar(evt); 
		calcular2();
	}
	*/
	public void calcularDetalle2(AjaxBehaviorEvent evt) {
		tab_equipo.modificar(evt); // Siempre es la primera linea
		calcular2();
	}
	
	public void calcular2() 
	{
		String str_seleccionado = tab_equipo.getValor("ide_aprub");
		TablaGenerica validarTarifaUnica = utilitario.consultar("select ide_aprut, valor_aprut,rendimiento_aprut from apu_rubro_tarifa where activo_aprut=true and coalesce(bloqueado_aprut,false)=false and ide_aprub=" + str_seleccionado);
		validarTarifaUnica.imprimirSql();
		double dou_valor_tarifa = 0;
		double dou_cantidad = 0;
		double dou_costo = 0;
		double dou_rendimiento = 0;
		double dou_total = 0;

		if (!validarTarifaUnica.isEmpty()) {
			dou_valor_tarifa = pckUtilidades.CConversion.CDbl_2(validarTarifaUnica.getValor("valor_aprut"));
			dou_rendimiento = pckUtilidades.CConversion.CDbl_2(validarTarifaUnica.getValor("rendimiento_aprut"));
			System.out.println("dou_valor_tarifa "+dou_valor_tarifa);
		} else {
			// Mensaje
			utilitario.agregarMensajeInfo("No existen tarifas para el item seleccionado", "");
		}
		
		dou_cantidad=pckUtilidades.CConversion.CDbl_2(tab_equipo.getValor("cantidad_apequ"));
		//dou_rendimiento=pckUtilidades.CConversion.CDbl_2(tab_equipo.getValor("rendimiento_apequ"));
		
		dou_costo=dou_cantidad * dou_valor_tarifa;
		dou_total=dou_costo * dou_rendimiento;
		
		tab_equipo.setValor("tarifa_apequ", utilitario.getFormatoNumero(dou_valor_tarifa, 2));
		tab_equipo.setValor("rendimiento_apequ", utilitario.getFormatoNumero(dou_rendimiento, 2));
		tab_equipo.setValor("costo_hora_apequ", utilitario.getFormatoNumero(dou_costo, 2));
		tab_equipo.setValor("costo_apequ", utilitario.getFormatoNumero(dou_total, 2));		
		tab_equipo.sumarColumnas();
		utilitario.addUpdate("tab_tabulador:tab_equipo");
		calcularTodos();
	}
	/*
	public void seleccionoRubro3(SelectEvent evt) 
	{
		tab_mano_obra.modificar(evt); 
		calcular3();
	}
	*/
	public void calcularDetalle3(AjaxBehaviorEvent evt) {
		tab_mano_obra.modificar(evt); // Siempre es la primera linea
		calcular3();
	}
	
	public void calcular3() 
	{
		String str_seleccionado = tab_mano_obra.getValor("ide_aprub");
		TablaGenerica validarTarifaUnica = utilitario.consultar("select ide_aprut, valor_aprut,rendimiento_aprut from apu_rubro_tarifa where activo_aprut=true and coalesce(bloqueado_aprut,false)=false and ide_aprub=" + str_seleccionado);
		validarTarifaUnica.imprimirSql();
		double dou_valor_tarifa = 0;
		double dou_cantidad = 0;
		double dou_costo = 0;
		double dou_rendimiento = 0;
		double dou_total = 0;

		if (!validarTarifaUnica.isEmpty()) {
			dou_valor_tarifa = pckUtilidades.CConversion.CDbl_2(validarTarifaUnica.getValor("valor_aprut"));
			dou_rendimiento = pckUtilidades.CConversion.CDbl_2(validarTarifaUnica.getValor("rendimiento_aprut"));
			System.out.println("dou_valor_tarifa "+dou_valor_tarifa);
		} else {
			// Mensaje
			utilitario.agregarMensajeInfo("No existen tarifas para el item seleccionado", "");
		}
		
		dou_cantidad=pckUtilidades.CConversion.CDbl_2(tab_mano_obra.getValor("cantidad_apmao"));
		//dou_rendimiento=pckUtilidades.CConversion.CDbl_2(tab_mano_obra.getValor("rendimiento_apmao"));
		
		dou_costo=dou_cantidad * dou_valor_tarifa;
		dou_total=dou_costo * dou_rendimiento;
		
		tab_mano_obra.setValor("jornal_apmao", utilitario.getFormatoNumero(dou_valor_tarifa, 2));
		tab_mano_obra.setValor("rendimiento_apmao", utilitario.getFormatoNumero(dou_rendimiento, 2));
		tab_mano_obra.setValor("costo_hora_apmao", utilitario.getFormatoNumero(dou_costo, 2));
		tab_mano_obra.setValor("costo_apmao", utilitario.getFormatoNumero(dou_total, 2));		
		tab_mano_obra.sumarColumnas();
		utilitario.addUpdate("tab_tabulador:tab_mano_obra");
		calcularTodos();
	}
	/*
	public void seleccionoRubro4(SelectEvent evt) 
	{
		tab_transporte.modificar(evt); 
		calcular4();
	}
	*/
	public void calcularDetalle4(AjaxBehaviorEvent evt) {
		tab_transporte.modificar(evt); // Siempre es la primera linea
		calcular4();
	}
	
	public void calcular4() 
	{
		String str_seleccionado = tab_transporte.getValor("ide_aprub");
		TablaGenerica validarTarifaUnica = utilitario.consultar("select ide_aprut, valor_aprut from apu_rubro_tarifa where activo_aprut=true and coalesce(bloqueado_aprut,false)=false and ide_aprub=" + str_seleccionado);
		validarTarifaUnica.imprimirSql();
		double dou_valor_tarifa = 0;
		double dou_cantidad_fadef = 0;
		double dou_total_fadef = 0;

		if (!validarTarifaUnica.isEmpty()) {
			dou_valor_tarifa = pckUtilidades.CConversion.CDbl_2(validarTarifaUnica.getValor("valor_aprut"));
			System.out.println("dou_valor_tarifa "+dou_valor_tarifa);
		} else {
			// Mensaje
			utilitario.agregarMensajeInfo("No existen tarifas para el item seleccionado", "");
		}
		
		dou_cantidad_fadef=pckUtilidades.CConversion.CDbl_2(tab_transporte.getValor("cantidad_aptra"));
		dou_total_fadef=dou_cantidad_fadef*dou_valor_tarifa;
		
		System.out.println("dou_total_fadef "+dou_total_fadef);
		
		tab_transporte.setValor("precio_aptra", utilitario.getFormatoNumero(dou_valor_tarifa, 2));
		tab_transporte.setValor("costo_total_aptra", dou_total_fadef+"");
		tab_transporte.sumarColumnas();
		utilitario.addUpdate("tab_tabulador:tab_transporte");
		calcularTodos();
	}
	
	
	public void calcularTodos()
	{
		
		double valorMaterial=pckUtilidades.CConversion.CDbl_2(tab_material.getSumaColumna("costo_total_apmat"));
		double valorEquipo=pckUtilidades.CConversion.CDbl_2(tab_equipo.getSumaColumna("costo_apequ"));
		double valorManoObra=pckUtilidades.CConversion.CDbl_2(tab_mano_obra.getSumaColumna("costo_apmao"));
		double valorTransporte=pckUtilidades.CConversion.CDbl_2(tab_transporte.getSumaColumna("costo_total_aptra"));
		double valorCostoIndirecto=pckUtilidades.CConversion.CDbl_2(tab_apu.getValor("costo_indirecto_apitc"));
		
		double valorCostoDirecto=valorMaterial+valorEquipo+valorManoObra+valorTransporte;
		double valorCostoTotal= valorCostoDirecto + (valorCostoDirecto * valorCostoIndirecto);
		
		tab_apu.setValor("total_costo_directo_apitc", utilitario.getFormatoNumero(valorCostoDirecto, 2));
		tab_apu.setValor("costo_total_apitc", utilitario.getFormatoNumero(valorCostoTotal, 2));
		
		utilitario.addUpdate("tab_apu");
		tab_apu.modificar(tab_apu.getFilaActual());
		
	}
	
	public void calcularCostoI(AjaxBehaviorEvent evt) {
		tab_apu.modificar(evt); // Siempre es la primera linea
		calcularTodos();
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
		
	}


	@Override
	public void guardar() {
		
		if (tab_apu.guardar()) {
			tab_material.guardar();
			tab_equipo.guardar();
			tab_mano_obra.guardar();
			tab_transporte.guardar();
			guardarPantalla();
			//tab_material.ejecutarSql();
		}
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_apu() {
		return tab_apu;
	}

	public void setTab_apu(Tabla tab_apu) {
		this.tab_apu = tab_apu;
	}

	public Tabla getTab_material() {
		return tab_material;
	}

	public void setTab_material(Tabla tab_material) {
		this.tab_material = tab_material;
	}

	public Tabla getTab_equipo() {
		return tab_equipo;
	}

	public void setTab_equipo(Tabla tab_equipo) {
		this.tab_equipo = tab_equipo;
	}

	public Tabla getTab_mano_obra() {
		return tab_mano_obra;
	}

	public void setTab_mano_obra(Tabla tab_mano_obra) {
		this.tab_mano_obra = tab_mano_obra;
	}

	public Tabla getTab_transporte() {
		return tab_transporte;
	}

	public void setTab_transporte(Tabla tab_transporte) {
		this.tab_transporte = tab_transporte;
	}

	public Division getDiv_division() {
		return div_division;
	}

	public void setDiv_division(Division div_division) {
		this.div_division = div_division;
	}

	

}
