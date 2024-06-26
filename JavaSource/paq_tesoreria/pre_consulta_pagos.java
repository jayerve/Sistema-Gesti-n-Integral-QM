package paq_tesoreria;

import javax.ejb.EJB;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;

public class pre_consulta_pagos extends Pantalla{
	
	private Tabla tab_pagos = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	
	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);

	public pre_consulta_pagos(){
		
		bar_botones.limpiar();
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);
		
		
		Boton bot_actualiza = new Boton();
		bot_actualiza.setId("bot_actualiza");
		bot_actualiza.setTitle("ACTUALIZAR");
		bot_actualiza.setValue("ACTUALIZAR");
		bot_actualiza.setMetodo("cargaPagos");
		bar_botones.agregarBoton(bot_actualiza);

		tab_pagos.setId("tab_pagos");
		tab_pagos.setSql(ser_tesoreria.comprobantesPagos("1900-01-01","1900-01-01"));
		/*tab_pagos.getColumna("ide_recli").setVisible(false);
		tab_pagos.getColumna("ruc_comercial_recli").setNombreVisual("RUC");
		tab_pagos.getColumna("ruc_comercial_recli").setLongitud(30);
		tab_pagos.getColumna("detalle_bogrm").setNombreVisual("DETALLE");
		tab_pagos.getColumna("razon_social_recli").setNombreVisual("RAZON SOCIAL");
		tab_pagos.getColumna("razon_social_recli").setLongitud(150);
		tab_pagos.getColumna("secuencial_fafac").setNombreVisual("NRO. DOCUMENTO");
		tab_pagos.getColumna("detalle_bogrm").setLongitud(130);
		tab_pagos.getColumna("secuencial_fafac").setLongitud(30);
		tab_pagos.getColumna("comprobante_aplicado").setLongitud(30);
		tab_pagos.getColumna("detalle_coest").setNombreVisual("ESTADO");
		tab_pagos.getColumna("detalle_coest").setLongitud(20);
		tab_pagos.getColumna("detalle_retip").setNombreVisual("FORMA PAGO");
		tab_pagos.getColumna("detalle_retip").setLongitud(30);
		tab_pagos.setColumnaSuma("total,abono,saldo");*/
		tab_pagos.setRows(25);
		tab_pagos.setLectura(true);
		tab_pagos.dibujar();
		
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_pagos);
		
		Division div_recaudacion = new Division();
		div_recaudacion.setId("div_recaudacion");
		div_recaudacion.dividir1(pat_panel);
		
		agregarComponente(div_recaudacion);
		
	}
	
	public void cargaPagos(){
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		
		tab_pagos.setSql(ser_tesoreria.comprobantesPagos(fecha_inicial,fecha_final));
		tab_pagos.ejecutarSql();
		utilitario.addUpdate("tab_pagos");
		
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

	public Tabla getTab_pagos() {
		return tab_pagos;
	}

	public void setTab_pagos(Tabla tab_pagos) {
		this.tab_pagos = tab_pagos;
	}


}
