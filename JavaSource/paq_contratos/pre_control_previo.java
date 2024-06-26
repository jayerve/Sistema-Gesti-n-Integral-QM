package paq_contratos;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_contratos.ejb.ServicioContrato;
import paq_sistema.aplicacion.Pantalla;


public class pre_control_previo extends Pantalla{
	
	private Tabla tab_control_previo= new Tabla();
	private Tabla tab_control_pre_doc= new Tabla();

	List lista = new ArrayList();
	
	@EJB
	private ServicioContrato ser_contrato = (ServicioContrato) utilitario.instanciarEJB(ServicioContrato.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario
			.instanciarEJB(ServicioContabilidad.class);

	
	public pre_control_previo() {
		
		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");
		
		tab_control_previo.setId("tab_control_previo");
		tab_control_previo.setTipoFormulario(true);
		tab_control_previo.getGrid().setColumns(4);
		tab_control_previo.setTabla("pre_control_previo", "ide_precp", 1);
		tab_control_previo.getColumna("ide_precp");
		tab_control_previo.getColumna("ide_precp").setNombreVisual("CÓDIGO");
		tab_control_previo.getColumna("ide_precp").setOrden(1);
		tab_control_previo.getColumna("activo_precp").setValorDefecto("true");
		tab_control_previo.getColumna("activo_precp").setNombreVisual("ACTIVO");
		tab_control_previo.getColumna("activo_precp").setOrden(2);
		
		Object fila1[] = { "0", "Anticipo" };
		Object fila2[] = { "1", "Avance" };
		Object fila3[] = { "2", "Cierre" };

		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		tab_control_previo.getColumna("tipo_control_precp").setRadio(lista, "0");
		tab_control_previo.getColumna("tipo_control_precp").setOrden(3);
		tab_control_previo.getColumna("tipo_control_precp").setRadioVertical(true);
		tab_control_previo.getColumna("tipo_control_precp").setNombreVisual("Tipo de Control");
		tab_control_previo.getColumna("ide_prcon").setCombo(ser_contrato.getContratos());
		tab_control_previo.getColumna("ide_prcon").setAutoCompletar();
		tab_control_previo.getColumna("ide_prcon").setLectura(false);
		tab_control_previo.getColumna("ide_prcon").setNombreVisual("Contrato");
		tab_control_previo.getColumna("ide_prcon").setOrden(4);
		tab_control_previo.getColumna("fecha_registro_precp");
		tab_control_previo.getColumna("fecha_registro_precp").setNombreVisual("Firma de Control");
		tab_control_previo.getColumna("fecha_registro_precp").setOrden(5);
		tab_control_previo.getColumna("fecha_registro_precp").setValorDefecto(utilitario.getFechaActual());
		tab_control_previo.getColumna("detalle_precp");
		tab_control_previo.getColumna("detalle_precp").setNombreVisual("Observaciones");
		tab_control_previo.getColumna("detalle_precp").setOrden(4);
		
		tab_control_previo.agregarRelacion(tab_control_pre_doc);
		tab_control_previo.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_control_previo);
		
//		dibujarControlDocumento();
		
		tab_control_pre_doc.setId("tab_control_pre_doc");
		tab_control_pre_doc.setHeader("CONTROL DE DOCUMENTOS");
		tab_control_pre_doc.setIdCompleto("tab_tabulador:tab_control_pre_doc");
//		tab_control_pre_doc.setTipoFormulario(true);
//		tab_control_pre_doc.getGrid().setColumns(4);
		tab_control_pre_doc.setTabla("pre_control_pre_doc", "ide_prcpd", 2);
		
		tab_control_pre_doc.getColumna("ide_prcpd").setNombreVisual("Código");
		tab_control_pre_doc.getColumna("ide_prcpd").setOrden(1);
		tab_control_pre_doc.getColumna("detalle_prcpd").setNombreVisual("Observación");
		tab_control_pre_doc.getColumna("detalle_prcpd").setOrden(6);
		tab_control_pre_doc.getColumna("detalle_prcpd").setLongitud(30);
		tab_control_pre_doc.getColumna("activo_prcpd").setValorDefecto("true");
		tab_control_pre_doc.getColumna("activo_prcpd").setVisible(false);
		tab_control_pre_doc.getColumna("ide_predo").setCombo("pre_control_documento",
				"ide_predo", "detalle_predo", " activo_predo='true' ");
		tab_control_pre_doc.getColumna("ide_predo").setNombreVisual("Documento/Respaldo");
		tab_control_pre_doc.getColumna("ide_predo").setOrden(2);
		tab_control_pre_doc.getColumna("ide_predo").setLectura(true);
		tab_control_pre_doc.getColumna("ide_prtcd").setCombo("pre_tipo_control_documento",
				"ide_prtcd", "detalle_prtcd", "");
		tab_control_pre_doc.getColumna("ide_prtcd").setValorDefecto("1");
		tab_control_pre_doc.getColumna("ide_prtcd").setNombreVisual("Control");
		tab_control_pre_doc.getColumna("ide_prtcd").setLongitud(5);
		tab_control_pre_doc.getColumna("ide_prtcd").setOrden(3);
		tab_control_pre_doc.getColumna("archivo_prcpd").setNombreVisual("Arhivo");
		tab_control_pre_doc.getColumna("archivo_prcpd").setOrden(4);
		tab_control_pre_doc.getColumna("archivo_prcpd").setUpload("control_pevio");
		tab_control_pre_doc.getColumna("archivo_prcpd").setLongitud(20);
		tab_control_pre_doc.dibujar();

		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_control_pre_doc);
		tab_tabulador.agregarTab("CONTROL", pat_panel2);
		
		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel, tab_tabulador, "35%", "H");
		agregarComponente(div_division);

		
//		agregarComponente(pat_seguro);
			
	}
	
	public void dibujarControlDocumento (){
		
		int num=10;
		
//		TablaGenerica tab=utilitario.consultar(ser_contrato.getNumRegistros());
//		num= Long.parseLong(tab.getValor("num"));
		
		for (int i = 1; i <= num; i++) {
			
			tab_control_pre_doc.insertar();
			tab_control_pre_doc.setValor("ide_predo",Integer.toString(i));
			tab_control_pre_doc.getColumna("ide_prtcd").setValorDefecto("1");

			tab_control_pre_doc.getColumna("ide_prtcd").setLongitud(10);

//			tab_control_pre_doc.setValor("ide_prcpd",tab_control_pre_doc.getValor(i));
//			tab_control_pre_doc.getColumna("detalle_prcpd").setNombreVisual("Observación");
//			tab_control_pre_doc.getColumna("activo_prcpd").setValorDefecto("true");
//			tab_control_pre_doc.getColumna("activo_prcpd").setVisible(false);
//			tab_control_pre_doc.getColumna("ide_predo").setCombo("pre_control_documento",
//					"ide_predo", "detalle_predo", " activo_predo='true' ");
//			tab_control_pre_doc.getColumna("ide_predo").setNombreVisual("Documento/Respaldo");
//			tab_control_pre_doc.getColumna("ide_prtcd").setCombo("pre_tipo_control_documento",
//					"ide_prtcd", "detalle_prtcd", "");
//			tab_control_pre_doc.getColumna("ide_prtcd").setNombreVisual("Control");
//			tab_control_pre_doc.getColumna("archivo_prcpd").setUpload("control_pevio");
//			tab_control_pre_doc.dibujar();
//			
//			tab_control_pre_doc.setValor("predo", tab_control_pre_doc.getValor("ide_predo"));
			
		}
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
//		utilitario.getTablaisFocus().insertar();

		tab_control_previo.insertar();
		dibujarControlDocumento();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		ser_contabilidad.limpiarAcceso("pre_control_previo");
		ser_contabilidad.limpiarAcceso("pre_control_pre_doc");

		
		if (tab_control_previo.guardar()) {
			if (tab_control_pre_doc.guardar()) {
			}
		}			
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_control_previo.eliminar();
	
	}

	public Tabla getTab_control_previo() {
		return tab_control_previo;
	}

	public void setTab_control_previo(Tabla tab_control_previo) {
		this.tab_control_previo = tab_control_previo;
	}

	public Tabla getTab_control_pre_doc() {
		return tab_control_pre_doc;
	}

	public void setTab_control_pre_doc(Tabla tab_control_pre_doc) {
		this.tab_control_pre_doc = tab_control_pre_doc;
	}

	public ServicioContrato getSer_contrato() {
		return ser_contrato;
	}

	public void setSer_contrato(ServicioContrato ser_contrato) {
		this.ser_contrato = ser_contrato;
	}

}
