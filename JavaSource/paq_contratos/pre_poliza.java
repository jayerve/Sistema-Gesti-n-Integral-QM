package paq_contratos;


import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_contratos.ejb.ServicioContrato;
import paq_sistema.aplicacion.Pantalla;



public class pre_poliza extends Pantalla {
	
	private Tabla tab_poliza = new Tabla();
    private Tabla tab_renovacion_poliza = new Tabla();
    private SeleccionTabla sel_contratos = new SeleccionTabla();
    private Combo com_anio=new Combo();
    
    public static String par_modulosec_polizas;
    
	@EJB
	private ServicioContrato ser_contrato = (ServicioContrato) utilitario.instanciarEJB(ServicioContrato.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega ) utilitario.instanciarEJB(ServicioBodega.class);
	
	public pre_poliza() {
		// TODO Auto-generated constructor stub
		
		par_modulosec_polizas=utilitario.getVariable("p_modulo_secuencial_polizas");
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		Boton bot_agregar = new Boton();
		bot_agregar.setValue("Enlazar Contrato");
		bot_agregar.setMetodo("enlazar_contrato");
		bar_botones.agregarBoton(bot_agregar);
		
		//AGREGAR LOS DOS COMPONENTES
		tab_poliza.setId("tab_poliza");
		tab_poliza.setHeader("POLIZAS");
		tab_poliza.setTabla("tes_poliza", "ide_tepol", 1);// 1 YA Q ES LA PRIMERA TABLA
		tab_poliza.agregarRelacion(tab_renovacion_poliza); //CON ESTO LE DECIMOS Q TIENE RELACION
		//COMBOS
		tab_poliza.setTipoFormulario(true);
		tab_poliza.getGrid().setColumns(4);
		tab_poliza.getColumna("ide_tepol").setNombreVisual("CÓDIGO");
		tab_poliza.getColumna("ide_tepol").setOrden(1);
		tab_poliza.getColumna("activo_tepol");
		tab_poliza.getColumna("activo_tepol").setNombreVisual("ACTIVO");
		tab_poliza.getColumna("activo_tepol").setOrden(2);
		tab_poliza.getColumna("activo_tepol").setValorDefecto("true");
		tab_poliza.getColumna("ide_prcon").setCombo(ser_contrato.getContratosExt());
		tab_poliza.getColumna("ide_prcon").setAutoCompletar();
//		tab_poliza.getColumna("ide_prcon").setEtiqueta();
		tab_poliza.getColumna("ide_prcon").setLectura(false);
		tab_poliza.getColumna("ide_prcon").setNombreVisual("Contrato");
		tab_poliza.getColumna("ide_prcon").setOrden(3);
		tab_poliza.getColumna("ide_tetip").setCombo("tes_tipo_poliza", "ide_tetip", "detalle_tetip", "");
		tab_poliza.getColumna("ide_tetip").setOrden(4);
		tab_poliza.getColumna("ide_tetip").setNombreVisual("Tipo de Póliza");
		tab_poliza.getColumna("ide_geins").setCombo("gen_institucion", "ide_geins", "detalle_geins", " ide_getii in (1,14) and gen_ide_geins is not null ");
		tab_poliza.getColumna("ide_geins").setOrden(5);
		tab_poliza.getColumna("ide_geins").setNombreVisual("Emisor");
		//tab_poliza.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_poliza.getColumna("ide_coest").setCombo(ser_contabilidad.getModuloEstados("true", par_modulosec_polizas));
		tab_poliza.getColumna("ide_coest").setOrden(6);
		tab_poliza.getColumna("ide_coest").setNombreVisual("Estado");
		tab_poliza.getColumna("ide_gemos").setCombo("gen_modulo_secuencial", "ide_gemos", "numero_secuencial_gemos", "");
		tab_poliza.getColumna("ide_gemos").setVisible(false);
		tab_poliza.getColumna("numero_poliza_tepol");
		tab_poliza.getColumna("numero_poliza_tepol").setNombreVisual("Número Póliza");
		tab_poliza.getColumna("numero_poliza_tepol").setOrden(7);

		tab_poliza.getColumna("fecha_emision_tepol");
		tab_poliza.getColumna("fecha_emision_tepol").setNombreVisual("Fecha de Emisión");
		tab_poliza.getColumna("fecha_emision_tepol").setOrden(8);

		tab_poliza.getColumna("valor_tepol");
		tab_poliza.getColumna("valor_tepol").setNombreVisual("Valor Póliza");
		tab_poliza.getColumna("valor_tepol").setOrden(9);

		tab_poliza.getColumna("vigencia_desde_tepol");
		tab_poliza.getColumna("vigencia_desde_tepol").setNombreVisual("Vigencia Desde");
		tab_poliza.getColumna("vigencia_desde_tepol").setOrden(10);

		tab_poliza.getColumna("vigencia_hasta_tepol");
		tab_poliza.getColumna("vigencia_hasta_tepol").setNombreVisual("Vigencia Hasta");
		tab_poliza.getColumna("vigencia_hasta_tepol").setOrden(11);

		tab_poliza.getColumna("secuencial_tepol");
		tab_poliza.getColumna("secuencial_tepol").setNombreVisual("Número Secuencia");
		tab_poliza.getColumna("secuencial_tepol").setOrden(12);
		
		tab_poliza.getColumna("afianzado_tepol").setNombreVisual("Afianzado");
		tab_poliza.getColumna("afianzado_tepol").setCombo(ser_bodega.getProveedor("true,false"));
		tab_poliza.getColumna("afianzado_tepol").setAutoCompletar();
		
		tab_poliza.getColumna("beneficiario_tepol").setNombreVisual("Beneficiario");
		tab_poliza.getColumna("beneficiario_tepol").setCombo("gen_institucion", "ide_geins", "detalle_geins", " ide_getii in (13) and gen_ide_geins is not null ");
		
		tab_poliza.getColumna("ide_geani").setCombo("gen_anio","ide_geani","detalle_geani","");
		
		tab_poliza.setCondicion("ide_geani=-1");
		
		tab_poliza.dibujar();
		PanelTabla pat_poliza= new PanelTabla();
		pat_poliza.setPanelTabla(tab_poliza);
		
		tab_renovacion_poliza.setId("tab_renovacion_poliza");
		tab_renovacion_poliza.setHeader("RENOVACION DE POLIZA");
		tab_renovacion_poliza.setTabla("tes_renovacion_poliza", "ide_terep", 2);//2 YA Q ES LA SEGUNDA TABLA
		tab_renovacion_poliza.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_renovacion_poliza.getColumna("ide_copag").setCombo("cont_parametros_general", "ide_copag", "detalle_copag", "");
		tab_renovacion_poliza.dibujar();
		PanelTabla pat_renovacion_poliza=new PanelTabla();
		pat_renovacion_poliza.setPanelTabla(tab_renovacion_poliza);
		
		Division div_division = new Division();
		div_division.dividir2(pat_poliza, pat_renovacion_poliza, "50%", "H");
		agregarComponente(div_division);
		
		inicializaContratos();
	}
	
	public void inicializaContratos(){
		sel_contratos.setId("sel_contratos");
		sel_contratos.setTitle("LISTA DE CONTRATOS");
		sel_contratos.setSeleccionTabla(ser_contrato.getContratosExt(), "ide_prcon");

		sel_contratos.setRadio();
		sel_contratos.getTab_seleccion().ejecutarSql();
		sel_contratos.getTab_seleccion().getColumna("numero_contrato_prcon").setFiltroContenido();
		
		sel_contratos.getBot_aceptar().setMetodo("enlazar_contrato");
		sel_contratos.setFooter("Los contratos deben de ser ingresados previamente y/o deben de ser de tipo externo...");
		agregarComponente(sel_contratos);
	}
	
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_poliza.setCondicion("ide_geani="+com_anio.getValue());
			tab_poliza.ejecutarSql();
			tab_renovacion_poliza.ejecutarValorForanea(tab_poliza.getValorSeleccionado());
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
	}

	public void enlazar_contrato(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
		if(sel_contratos.isVisible()){
			if(sel_contratos.getValorSeleccionado()!=null){
				sel_contratos.cerrar();	
				
				if(tab_poliza.getTotalFilas()<=0)
				{
					tab_poliza.insertar();
					tab_poliza.setValor("secuencial_tepol", ser_contabilidad.numeroSecuencial(par_modulosec_polizas));
					tab_poliza.setValor("ide_geani", com_anio.getValue()+"");
				}
				
				tab_poliza.setValor("ide_prcon", sel_contratos.getValorSeleccionado());
				tab_poliza.modificar(tab_poliza.getFilaActual());
				tab_poliza.guardar();
				guardarPantalla();
				utilitario.addUpdate("tab_poliza");
			}
			else {
				utilitario.agregarMensajeError("Seleccion", "Seleccione un registro para continuar");
			}
		}
		else
		{
			sel_contratos.dibujar();
		}
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
		utilitario.getTablaisFocus().insertar();
		if(tab_poliza.isFilaInsertada())
		{
			tab_poliza.setValor("secuencial_tepol", ser_contabilidad.numeroSecuencial(par_modulosec_polizas));
			tab_poliza.setValor("ide_geani", com_anio.getValue()+"");
		}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
		if(tab_poliza.guardar()){
			if(tab_renovacion_poliza.guardar()){
				guardarPantalla();
			}
		}
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
		utilitario.getTablaisFocus().eliminar();
	}


	public Tabla getTab_poliza() {
		return tab_poliza;
	}


	public void setTab_poliza(Tabla tab_poliza) {
		this.tab_poliza = tab_poliza;
	}


	public Tabla getTab_renovacion_poliza() {
		return tab_renovacion_poliza;
	}


	public void setTab_renovacion_poliza(Tabla tab_renovacion_poliza) {
		this.tab_renovacion_poliza = tab_renovacion_poliza;
	}

	public SeleccionTabla getSel_contratos() {
		return sel_contratos;
	}

	public void setSel_contratos(SeleccionTabla sel_contratos) {
		this.sel_contratos = sel_contratos;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}
	

}
