package paq_adquisicion;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import paq_adquisicion.ejb.ServicioAdquisicion;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

public class pre_nota_credito_compras extends Pantalla {
	
	private Tabla tab_adq_nota_credito = new Tabla();
	private Tabla tab_adq_detalle_nota_credito = new Tabla();

	private SeleccionTabla set_factura = new SeleccionTabla();

	public static double par_iva;

	private Check che_iva12 = new Check();
	private Combo com_anio=new Combo();

	@EJB
    private ServicioAdquisicion ser_adquisicion=(ServicioAdquisicion) utilitario.instanciarEJB(ServicioAdquisicion.class);
	@EJB
    private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	public pre_nota_credito_compras() {
		par_iva = pckUtilidades.CConversion.CDbl(utilitario.getVariable("p_valor_iva"));
		
		tab_adq_nota_credito.setId("tab_adq_nota_credito");
		tab_adq_nota_credito.setTabla("adq_nota_credito", "ide_adncr", 1);
		tab_adq_nota_credito.agregarRelacion(tab_adq_detalle_nota_credito);
		tab_adq_nota_credito.setTipoFormulario(true);
		tab_adq_nota_credito.getGrid().setColumns(4);
		tab_adq_nota_credito.setCampoOrden("ide_adncr desc");
		tab_adq_nota_credito.getColumna("ide_adfac").setCombo("adq_factura","ide_adfac","fecha_factura_adfac,num_factura_adfac,subtotal_adfac,valor_iva_adfac,total_adfac","");
		tab_adq_nota_credito.getColumna("ide_adfac").setLectura(true);
		tab_adq_nota_credito.getColumna("ide_adfac").setAutoCompletar();
		tab_adq_nota_credito.getColumna("fecha_adncr").setRequerida(true);
		tab_adq_nota_credito.getColumna("ide_tepro").setCombo(ser_bodega.getProveedor("true,false"));
		tab_adq_nota_credito.getColumna("ide_tepro").setLectura(true);
		tab_adq_nota_credito.getColumna("ide_tepro").setAutoCompletar();
		tab_adq_nota_credito.getColumna("num_factura_adncr").setLectura(true);
		tab_adq_nota_credito.getColumna("activo_adncr").setValorDefecto("true");
		tab_adq_nota_credito.setCondicion("ide_adncr=-1");
		tab_adq_nota_credito.dibujar();
		
		PanelTabla pat_adq_factura = new PanelTabla();
		pat_adq_factura.setPanelTabla(tab_adq_nota_credito);

		tab_adq_detalle_nota_credito.setId("tab_adq_detalle_nota_credito");
		tab_adq_detalle_nota_credito.setTabla("adq_detalle_nota_credito", "ide_addnc", 2);
		tab_adq_detalle_nota_credito.getColumna("aplica_iva_addnc").setValorDefecto("true");
		tab_adq_detalle_nota_credito.getColumna("activo_addnc").setValorDefecto("true");
		tab_adq_detalle_nota_credito.dibujar();
		
		PanelTabla pat_adq_detalle = new PanelTabla();
		pat_adq_detalle.setPanelTabla(tab_adq_detalle_nota_credito);
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("AÑO:"));
		bar_botones.agregarComponente(com_anio);

		che_iva12.setId("che_iva12");
		che_iva12.setMetodoChange("cambiaIVA");
		Etiqueta eti_iva12 = new Etiqueta("IVA 14%");
		bar_botones.agregarComponente(eti_iva12);
		bar_botones.agregarComponente(che_iva12);

		Boton bot_buscar = new Boton();
		bot_buscar.setIcon("ui-icon-person");
		bot_buscar.setValue("Buscar Factura de Compra");
		bot_buscar.setMetodo("importarFacturaCompra");
		bar_botones.agregarBoton(bot_buscar);

		set_factura.setId("set_factura");
		set_factura.setSeleccionTabla(ser_adquisicion.getFacturasCompra("true,false","-1"), "ide_adfac");
		set_factura.setTitle("SELECCIONE UNA FACTURA DE COMPRA");
		set_factura.getTab_seleccion().getColumna("proveedor").setFiltroContenido();
		set_factura.getTab_seleccion().getColumna("num_factura").setFiltroContenido();
		set_factura.getBot_aceptar().setMetodo("aceptarFacturaCompra");
		set_factura.setRadio();
		agregarComponente(set_factura);

		Division div_division = new Division();
		div_division.dividir2(pat_adq_factura, pat_adq_detalle, "50%", "H");
		agregarComponente(div_division);


	}
	
	public void seleccionaElAnio (){
    	tab_adq_nota_credito.setCondicion(" extract(year from fecha_adncr)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") ");
    	tab_adq_nota_credito.ejecutarSql();
    	tab_adq_detalle_nota_credito.ejecutarValorForanea(tab_adq_nota_credito.getValorSeleccionado());
	}

	public void cambiaIVA() {
		if (che_iva12.getValue().toString().equalsIgnoreCase("true")) {
			par_iva = 0.14;
		} else {
			par_iva = pckUtilidades.CConversion.CDbl(utilitario.getVariable("p_valor_iva")); // 0.14
																								// de
																								// IVA
		}

		System.out.println("cambiaIVA FacCompras " + par_iva);
	}

	public void importarFacturaCompra() {
		set_factura.getTab_seleccion().setSql(ser_adquisicion.getFacturasCompra("true,false",com_anio.getValue()+""));
		set_factura.getTab_seleccion().ejecutarSql();
		set_factura.dibujar();
	}


	public void aceptarFacturaCompra() {

		String str_seleccionado = set_factura.getValorSeleccionado();
		TablaGenerica tab_factura = utilitario.consultar(ser_adquisicion.getFacturaComprasCodigo(str_seleccionado));
		if (str_seleccionado != null) {
			tab_adq_nota_credito.insertar();
			tab_adq_nota_credito.setValor("ide_adfac", str_seleccionado);
			tab_adq_nota_credito.setValor("num_factura_adncr", tab_factura.getValor("num_factura_adfac"));
			tab_adq_nota_credito.setValor("detalle_adncr", tab_factura.getValor("detalle_adfac"));
			tab_adq_nota_credito.setValor("ide_tepro", tab_factura.getValor("ide_tepro"));
			tab_adq_nota_credito.setValor("valor_descuento_adncr", tab_factura.getValor("valor_descuento_adfac"));
			tab_adq_nota_credito.setValor("subtotal_adncr", tab_factura.getValor("subtotal_adfac"));
			tab_adq_nota_credito.setValor("total_adncr", tab_factura.getValor("total_adfac"));
			tab_adq_nota_credito.setValor("valor_iva_adncr", tab_factura.getValor("valor_iva_adfac"));
			tab_adq_nota_credito.setValor("base_iva_adncr", tab_factura.getValor("base_iva_adfac"));
			tab_adq_nota_credito.setValor("base_noiva_adncr", tab_factura.getValor("base_noiva_adfac"));
		}
		utilitario.addUpdateTabla(tab_adq_nota_credito, "ide_adfac,detalle_adncr,ide_tepro", "");
		set_factura.cerrar();
		tab_adq_nota_credito.guardar();
		guardarPantalla();

	}

	// /CALCULAR
	public void calcular() {
		// Variables para almacenar y calcular el total del detalle
		double dou_cantidad_addef = 0;
		double dou_valor_unitario_addef = 0;
		double dou_valor_total_addef = 0;
		try {
			// Obtenemos el valor de la cantidad
			dou_cantidad_addef = pckUtilidades.CConversion.CDbl(tab_adq_detalle_nota_credito.getValor("cantidad_addef"));
		} catch (Exception e) {
		}

		try {
			// Obtenemos el valor
			dou_valor_total_addef = pckUtilidades.CConversion.CDbl(tab_adq_detalle_nota_credito.getValor("valor_total_addef"));
		} catch (Exception e) {
		}
		// Calculamos el total
		dou_valor_unitario_addef = dou_valor_total_addef / dou_cantidad_addef;
		// Asignamos el total a la tabla detalle, con 3 decimales
		tab_adq_detalle_nota_credito.setValor("valor_unitario_addef", utilitario.getFormatoNumero(dou_valor_unitario_addef, 3));
		// Actualizamos el campo de la tabla AJAX
		utilitario.addUpdateTabla(tab_adq_detalle_nota_credito, "valor_unitario_addef", "tab_adq_nota_credito");
		calcularSolicitud();
	}

	public void calcularDetallle(AjaxBehaviorEvent evt) {
		tab_adq_detalle_nota_credito.modificar(evt); // Siempre es la primera linea
		calcular();
	}

	public void calcularSolicitud() {

		double dou_subtotal_adfac = 0;
		double duo_valor_iva = par_iva;
		double dou_valor_iva_adfac = 0;
		double dou_total_adfac = 0;
		double dou_valor_total_addef = 0;

		// Aplica IVA 2016-03-29
		double subtotal_iva = 0;
		double subtotal_sin_iva = 0;

		for (int i = 0; i < tab_adq_detalle_nota_credito.getTotalFilas(); i++) {
			// Subtotal con iva
			if (tab_adq_detalle_nota_credito.getValor(i, "aplica_iva_adfac").trim().contentEquals("true")) {
				subtotal_iva += pckUtilidades.CConversion.CDbl(tab_adq_detalle_nota_credito.getValor(i, "valor_total_addef"));
				// System.out.println("> con iva: "+pckUtilidades.CConversion.CDbl(tab_adq_detalle_nota_credito.getValor(i,"valor_total_addef"))
				// );
			}

			if (tab_adq_detalle_nota_credito.getValor(i, "aplica_iva_adfac").trim().contentEquals("false")) {
				// Subtotal sin iva
				subtotal_sin_iva += pckUtilidades.CConversion.CDbl(tab_adq_detalle_nota_credito.getValor(i, "valor_total_addef"));
				// System.out.println("> sin iva: "+pckUtilidades.CConversion.CDbl(tab_adq_detalle_nota_credito.getValor(i,"valor_total_addef"))
				// );
			}
		}

		double iva_adq_factura = subtotal_iva * duo_valor_iva;
		double subtotal_adq_factura = subtotal_iva + subtotal_sin_iva;
		double total_adq_factura = subtotal_adq_factura + iva_adq_factura;

		// Asigna el valor de la base imponible Sin Iva
		tab_adq_nota_credito.setValor("base_noiva_adfac", String.valueOf(subtotal_sin_iva));

		// Asigna el valor de la base imponible Con Iva
		tab_adq_nota_credito.setValor("base_iva_adfac", String.valueOf(subtotal_iva));

		System.out.println("subtotal_sin_iva: " + subtotal_sin_iva);
		System.out.println("subtotal_iva: " + subtotal_iva);
		System.out.println("iva_adq_factura: " + iva_adq_factura);
		System.out.println("subtotal_adq_factura: " + subtotal_adq_factura);
		System.out.println("total_adq_factura: " + total_adq_factura);
		System.out.println("");
		// Aplica IVA 2016-03-29

		tab_adq_nota_credito.setValor("subtotal_adfac", String.valueOf(subtotal_adq_factura));

		tab_adq_nota_credito.setValor("valor_iva_adfac", utilitario.getFormatoNumero(iva_adq_factura, 3));
		tab_adq_nota_credito.setValor("total_adfac", utilitario.getFormatoNumero(total_adq_factura, 3));
		tab_adq_nota_credito.modificar(tab_adq_nota_credito.getFilaActual());// para que
																	// haga el
																	// update

		utilitario.addUpdateTabla(tab_adq_nota_credito, "valor_iva_adfac,total_adfac,subtotal_adfac,base_iva_adfac", "tab_adq_detalle_nota_credito");
	}

	public void calcularDescuento() {
		double duo_subtotal = 0;
		double dou_subtotal_adfac = 0;
		double duo_valor_descuento = 0;
		double duo_total_iva = 0;
		double duo_total = 0;
		double porcentaje_descuento = 0;
		double duo_iva = par_iva;
		calcularSolicitud();

		// tab_adq_nota_credito.getValor("valor_descuento_adfac");
		duo_subtotal = pckUtilidades.CConversion.CDbl(tab_adq_nota_credito.getValor("valor_descuento_adfac"));

		// tab_adq_nota_credito.getValor("total_adfac");
		dou_subtotal_adfac = pckUtilidades.CConversion.CDbl(tab_adq_nota_credito.getValor("subtotal_adfac"));
		if (duo_subtotal > dou_subtotal_adfac) {
			utilitario.agregarMensajeInfo("Excedido Valor ", "El valor del descuento no puedo superar el valor del subtotal");
			tab_adq_nota_credito.setValor("valor_descuento_adfac", "0");
			utilitario.addUpdateTabla(tab_adq_nota_credito, "valor_descuento_adfac", "");

			return;

		}
		porcentaje_descuento = (duo_subtotal * 100) / dou_subtotal_adfac;
		duo_valor_descuento = dou_subtotal_adfac - duo_subtotal;
		duo_total_iva = duo_valor_descuento * duo_iva;
		duo_total = duo_valor_descuento + duo_total_iva;
		tab_adq_nota_credito.setValor("porcent_desc_adfac", utilitario.getFormatoNumero(porcentaje_descuento, 2));
		tab_adq_nota_credito.setValor("subtotal_adfac", utilitario.getFormatoNumero(duo_valor_descuento, 2));
		tab_adq_nota_credito.setValor("base_iva_adfac", utilitario.getFormatoNumero(duo_valor_descuento, 2));

		tab_adq_nota_credito.setValor("total_adfac", utilitario.getFormatoNumero(duo_total, 2));
		tab_adq_nota_credito.setValor("VALOR_IVA_ADFAC", utilitario.getFormatoNumero(duo_total_iva, 2));

		utilitario.addUpdateTabla(tab_adq_nota_credito, "total_adfac,subtotal_adfac,VALOR_IVA_ADFAC,porcent_desc_adfac", "tab_adq_detalle_nota_credito");

	}

	public void calcularDescuentoPorce() {
		double duo_subtotal = 0;
		double dou_subtotal_adfac = 0;
		double duo_valor_descuento = 0;
		double duo_total_iva = 0;
		double duo_total = 0;
		double porcentaje_descuento = 0;
		double duo_iva = par_iva;
		calcularSolicitud();

		// tab_adq_nota_credito.getValor("valor_descuento_adfac");
		duo_subtotal = pckUtilidades.CConversion.CDbl(tab_adq_nota_credito.getValor("porcent_desc_adfac"));
		// tab_adq_nota_credito.getValor("total_adfac");
		if (duo_subtotal > 100) {
			utilitario.agregarMensajeInfo("Excedido Valor ", "El porcentaje de descuento no puede superar el 100%");
			tab_adq_nota_credito.setValor("porcent_desc_adfac", "0");
			utilitario.addUpdateTabla(tab_adq_nota_credito, "porcent_desc_adfac", "");

			return;
		}
		dou_subtotal_adfac = pckUtilidades.CConversion.CDbl(tab_adq_nota_credito.getValor("subtotal_adfac"));
		porcentaje_descuento = (dou_subtotal_adfac * duo_subtotal) / 100;
		duo_valor_descuento = dou_subtotal_adfac - porcentaje_descuento;
		duo_total_iva = duo_valor_descuento * duo_iva;
		duo_total = duo_valor_descuento + duo_total_iva;
		tab_adq_nota_credito.setValor("valor_descuento_adfac", utilitario.getFormatoNumero(porcentaje_descuento, 2));
		tab_adq_nota_credito.setValor("subtotal_adfac", utilitario.getFormatoNumero(duo_valor_descuento, 2));
		tab_adq_nota_credito.setValor("base_iva_adfac", utilitario.getFormatoNumero(duo_valor_descuento, 2));
		tab_adq_nota_credito.setValor("total_adfac", utilitario.getFormatoNumero(duo_total, 2));
		tab_adq_nota_credito.setValor("VALOR_DESCUENTO_ADFAC", utilitario.getFormatoNumero(porcentaje_descuento, 2));
		tab_adq_nota_credito.setValor("VALOR_IVA_ADFAC", utilitario.getFormatoNumero(duo_total_iva, 2));

		utilitario.addUpdateTabla(tab_adq_nota_credito, "total_adfac,subtotal_adfac,VALOR_IVA_ADFAC,porcent_desc_adfac,VALOR_DESCUENTO_ADFAC", "");

	}


	private boolean formatearNumeroFactura() {
		boolean correcto = false;
		try {
			String[] numeroFactura = tab_adq_nota_credito.getValor("num_nota_credito_adncr").split("-");
			String nuevoNumeroFactura = "";

			String strSucursal = pckUtilidades.Utilitario.padLeft(String.valueOf(pckUtilidades.CConversion.CInt(numeroFactura[0])), 3);
			String strPuntoEmision = pckUtilidades.Utilitario.padLeft(String.valueOf(pckUtilidades.CConversion.CInt(numeroFactura[1])), 3);
			String strSecuencial = pckUtilidades.Utilitario.padLeft(String.valueOf(pckUtilidades.CConversion.CInt(numeroFactura[2])), 9);

			nuevoNumeroFactura += strSucursal + "-";
			nuevoNumeroFactura += strPuntoEmision + "-";
			nuevoNumeroFactura += strSecuencial;

			tab_adq_nota_credito.setValor("num_nota_credito_adncr", nuevoNumeroFactura);

			correcto = true;

		} catch (Exception ex) {
			System.out.println("Error al padear el secuencial de la nota de credito de la factura de compra: " + tab_adq_nota_credito.getValor("num_factura_adncr") + ". " + ex.getMessage());
			tab_adq_nota_credito.setValor("num_nota_credito_adncr", null);
			utilitario.agregarMensajeInfo("No se pudo guardar", "Valide el formato del número de la nota de credito.");
		}
		return correcto;
	}


	@Override
	public void insertar() {
		//utilitario.getTablaisFocus().insertar();
		utilitario.agregarMensajeInfo("Insertar", "Favor utilice el botón BUSCAR FACTURA DE COMPRAS.");
		return;
	}

	@Override
	public void guardar() {
		// AVACA - 2016-03-21
		// Formato del número de la factura (001-001-000123456)
		if (formatearNumeroFactura()) {
			if (tab_adq_nota_credito.guardar()) {
				if (tab_adq_detalle_nota_credito.guardar()) {
					guardarPantalla();
					tab_adq_nota_credito.ejecutarSql();
					tab_adq_detalle_nota_credito.ejecutarSql();
				}
			}
		}

		// AVACA - 2016-03-21
	}

	@Override
	public void eliminar() {
		if (tab_adq_nota_credito.isFocus()) {
			if (tab_adq_detalle_nota_credito.getTotalFilas() > 0) {
				utilitario.agregarMensajeError("No se puede borrar", "El presente registro no se puede borrar existen detalles de factura");
				return;
			}
			tab_adq_nota_credito.eliminar();
		}
		if (tab_adq_detalle_nota_credito.isFocus()) {
			tab_adq_detalle_nota_credito.eliminar();
		}
	}

	
	public Tabla getTab_adq_nota_credito() {
		return tab_adq_nota_credito;
	}

	public void setTab_adq_nota_credito(Tabla tab_adq_nota_credito) {
		this.tab_adq_nota_credito = tab_adq_nota_credito;
	}

	public Tabla getTab_adq_detalle_nota_credito() {
		return tab_adq_detalle_nota_credito;
	}

	public void setTab_adq_detalle_nota_credito(Tabla tab_adq_detalle_nota_credito) {
		this.tab_adq_detalle_nota_credito = tab_adq_detalle_nota_credito;
	}

	public SeleccionTabla getSet_factura() {
		return set_factura;
	}

	public void setSet_factura(SeleccionTabla set_factura) {
		this.set_factura = set_factura;
	}



	public Check getChe_iva12() {
		return che_iva12;
	}

	public void setChe_iva12(Check che_iva12) {
		this.che_iva12 = che_iva12;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	
	
	
	
}
