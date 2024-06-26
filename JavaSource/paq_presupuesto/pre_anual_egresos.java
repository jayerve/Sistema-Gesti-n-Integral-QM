package paq_presupuesto;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;

public class pre_anual_egresos extends Pantalla {
	
	private Tabla tab_anual= new Tabla();
	private Tabla tab_mensual= new Tabla();
	private Tabla tab_reforma= new Tabla();
	private Combo com_anio =new Combo();
	private SeleccionTabla set_programa = new SeleccionTabla();
	private SeleccionTabla set_poa=new SeleccionTabla();


	@EJB
	private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	public pre_anual_egresos(){
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		
		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_anual.setId("tab_anual");
		tab_anual.setHeader("EJECUCION PRESUPUESTARIA ANUAL");
		tab_anual.setTabla("pre_anual", "ide_pranu", 1);
		tab_anual.getColumna("ide_prcla").setCombo("select ide_prcla,codigo_clasificador_prcla,descripcion_clasificador_prcla from pre_clasificador order by codigo_clasificador_prcla");
		tab_anual.getColumna("ide_prcla").setVisible(false);
		tab_anual.getColumna("ide_prfuf").setCombo("select ide_prfuf,detalle_prfuf from pre_fuente_financiamiento order by detalle_prfuf");
		//tab_anual.getColumna("ide_prfuf").setVisible(false);

		tab_anual.setCondicion("not ide_prpro is null");
		tab_anual.getColumna("ide_prpro").setCombo(ser_presupuesto.getPrograma("true,false"));
		tab_anual.getColumna("ide_prpro").setAutoCompletar();
		tab_anual.getColumna("ide_prpro").setLectura(true);
		tab_anual.getColumna("ide_geani").setCombo(ser_contabilidad.getAnio("true,false", "false,true"));
		tab_anual.getColumna("ide_geani").setVisible(false);
		tab_anual.setCondicion("ide_geani=-1");
		tab_anual.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoaNombre("select ide_geani from gen_anio"));
		tab_anual.getColumna("ide_prpoa").setAutoCompletar();
		tab_anual.getColumna("ide_prpoa").setLectura(true);
		tab_anual.getColumna("valor_reformado_h_pranu").setEtiqueta();
		tab_anual.getColumna("valor_reformado_h_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_reformado_h_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_reformado_d_pranu").setEtiqueta();
		tab_anual.getColumna("valor_reformado_d_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_reformado_d_pranu").setValorDefecto("0.00");
		//tab_anual.getColumna("ide_geani").setValorDefecto(com_anio.getValue().toString());
		//tab_anual.getColumna("ide_prfup").setCombo("pre_funcion_programa", "ide_prfup", "detalle_prfup,", "");
		tab_anual.getColumna("valor_reformado_pranu").setEtiqueta();
		tab_anual.getColumna("valor_reformado_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_reformado_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_codificado_pranu").setEtiqueta();
		tab_anual.getColumna("valor_codificado_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_codificado_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_devengado_pranu").setEtiqueta();
		tab_anual.getColumna("valor_devengado_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_devengado_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_precomprometido_pranu").setEtiqueta();
		tab_anual.getColumna("valor_precomprometido_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_precomprometido_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_eje_comprometido_pranu").setEtiqueta();
		tab_anual.getColumna("valor_eje_comprometido_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_eje_comprometido_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_recaudado_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_recaudado_pranu").setEtiqueta();
		tab_anual.getColumna("valor_recaudado_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_recaudado_efectivo_pranu").setEtiqueta();
		tab_anual.getColumna("valor_recaudado_efectivo_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_recaudado_efectivo_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_inicial_pranu").setMetodoChange("calcularValor");
		tab_anual.getColumna("valor_recaudado_pranu").setVisible(false);
		tab_anual.getColumna("valor_recaudado_efectivo_pranu").setVisible(false);
		tab_anual.getColumna("pagado_pranu").setVisible(false);

		/// requerida para grabar
		tab_anual.getColumna("ide_prpro").setRequerida(true);
		tab_anual.getColumna("valor_inicial_pranu").setRequerida(true);
		tab_anual.getColumna("activo_pranu").setValorDefecto("true");
		tab_anual.setTipoFormulario(true);
		tab_anual.getGrid().setColumns(4);
		tab_anual.agregarRelacion(tab_mensual);
		tab_anual.agregarRelacion(tab_reforma);
		tab_anual.dibujar();
		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_anual);
		
		//////////////pre_mensual
		
		tab_mensual.setId("tab_mensual");
		tab_mensual.setHeader("DETALLE DE EJECUCION PRESUPUESTARIA");
		tab_mensual.setIdCompleto("tab_tabulador:tab_mensual");
		tab_mensual.setTabla("pre_mensual", "ide_prmen", 2);
		tab_mensual.getColumna("ide_prtra").setLectura(true);
		tab_mensual.getColumna("ide_comov").setLectura(true);
		//tab_mensual.setCondicion("ide_prpro!=null");
		tab_mensual.getColumna("ide_gemes").setCombo("gen_mes", "ide_gemes", "detalle_gemes", "");
		tab_mensual.getColumna("ide_codem").setLectura(true);
		//tab_anual.getColumna("ide_prfup").setCombo("pre_funcion_programa", "ide_prfup", "detalle_prfup,", "");
		tab_mensual.setCondicion("ide_pranu is not null");
		tab_mensual.setTipoFormulario(true);
		tab_mensual.getGrid().setColumns(6);
		tab_mensual.dibujar();
		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_mensual);
		
		////////REFORMA MES
		tab_reforma.setId("tab_reforma");
		tab_reforma.setHeader("REFORMA PRESUPUESTARIA MENSUAL (POA)");
		tab_reforma.setIdCompleto("tab_tabulador:tab_reforma");
		tab_reforma.setTabla("pre_poa_reforma", "ide_prpor",3);
		tab_reforma.setCampoForanea("ide_prpoa");
		tab_reforma.getColumna("ide_coest").setCombo("cont_estado","ide_coest","detalle_coest","");
		tab_reforma.getColumna("ide_gemes").setCombo("select ide_gemes,detalle_gemes from gen_mes order by ide_gemes");
		tab_reforma.getColumna("activo_prpor").setValorDefecto("true");
		tab_reforma.getColumna("fecha_prpor").setValorDefecto(utilitario.getFechaActual());
		tab_reforma.dibujar();
		
		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_reforma);
				
		
		tab_tabulador.agregarTab("DETALLE DE EJECUCION PRESUPUESTARIA", pat_panel2);//intancia los tabuladores 
		tab_tabulador.agregarTab("REFORMA PRESUPUESTARIA MENSUAL",pat_panel3);

		
		Division div_division =new Division ();
		div_division.dividir2(pat_panel1, tab_tabulador, "50%", "h");
		agregarComponente(div_division);

		Boton bot_importarpoa = new Boton();
		bot_importarpoa.setValue("Importar POA");
		bot_importarpoa.setIcon("ui-icon-person");
		bot_importarpoa.setMetodo("importarPoa");
		bar_botones.agregarBoton(bot_importarpoa);
		/*
		Boton bot_material = new Boton();
		bot_material.setValue("Agregar Programa");
		bot_material.setTitle("Solicitud Programa");
		bot_material.setIcon("ui-icon-person");
		bot_material.setMetodo("importarPrograma");
		bar_botones.agregarBoton(bot_material);
		 */
		set_programa.setId("set_programa");
		set_programa.setSeleccionTabla(ser_presupuesto.getPrograma("true,false"),"IDE_PRPRO");
		set_programa.getTab_seleccion().getColumna("cod_programa_prpro").setFiltroContenido();
		set_programa.getBot_aceptar().setMetodo("aceptarPrograma");
		set_programa.getTab_seleccion().ejecutarSql();
		set_programa.setRadio();
		agregarComponente(set_programa);

		iniciaPoa();

	}
	public void iniciaPoa(){
		set_poa.setId("set_poa");
		set_poa.setSeleccionTabla(ser_presupuesto.getPoa("-1","true","false"),"ide_prpoa");
		set_poa.setTitle("Seleccione Poa");
		set_poa.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_PROGRAMA").setFiltro(true);//pone filtro
		set_poa.getTab_seleccion().getColumna("PROGRAMA").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_PROYECTO").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("PROYECTO").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_PRODUCTO").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("PRODUCTO").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_ACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("ACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_SUBACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("SUBACTIVIDAD").setFiltroContenido();
		set_poa.getTab_seleccion().getColumna("CODIGO_SUBACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("NUM_RESOLUCION_PRPOA").setFiltro(true);
		set_poa.getBot_aceptar().setMetodo("importarPoa");
		agregarComponente(set_poa);
	}
	public void importarPoa(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
		if(set_poa.isVisible()){
			String str_seleccionados=set_poa.getSeleccionados();
			boolean insertar;
			System.out.println("str_seleccionados: "+str_seleccionados);
			if (str_seleccionados!=null){
				TablaGenerica tab_aprueba_poa =utilitario.consultar("select ide_prpoa,activo_prpoa,ide_prcla,ide_prfup,presupuesto_inicial_prpoa from pre_poa where ide_prpoa in ("+str_seleccionados+")");
				//tab_aprueba_poa.imprimirSql();
				if(tab_aprueba_poa.getTotalFilas()>0){
					
					for(int i=0;i<tab_aprueba_poa.getTotalFilas();i++)
					{
						try
						{
							TablaGenerica tab_programa=utilitario.consultar("select a.ide_prpro,a.ide_prfup,a.ide_prcla from pre_programa a, cont_vigente b where a.ide_prpro = b.ide_prpro and a.ide_prfup ="+tab_aprueba_poa.getValor(i,"ide_prfup")+"  and a.ide_prcla="+tab_aprueba_poa.getValor(i,"ide_prcla"));
							
							if(pckUtilidades.CConversion.CInt(tab_programa.getValor("ide_prpro"))>0)
							{		
								insertar=true;
								
								try
								{
									TablaGenerica tab_anual_aux = utilitario.consultar("select ide_pranu, ide_prcla, ide_prpro, ide_geani, ide_prpoa, ide_prfuf from pre_anual where ide_prpro="+tab_programa.getValor("ide_prpro")+" and ide_geani="+com_anio.getValue().toString()+" and ide_prpoa="+tab_aprueba_poa.getValor(i,"ide_prpoa"));
									if(pckUtilidades.CConversion.CInt(tab_anual_aux.getValor("ide_pranu"))>0)
										insertar=false;
								}
								catch(Exception ec)
								{
									System.out.println("Error: Importar POA msj: "+ec.getMessage());
								}
								
								if(insertar)
								{
									//tab_programa.imprimirSql();
									tab_anual.insertar();
									tab_anual.setValor("ide_prpro", tab_programa.getValor("ide_prpro"));
									tab_anual.setValor("ide_geani", com_anio.getValue().toString());
									tab_anual.setValor("ide_prpoa", tab_aprueba_poa.getValor(i,"ide_prpoa"));
									tab_anual.setValor("valor_reformado_pranu", "0");
									tab_anual.setValor("valor_inicial_pranu", tab_aprueba_poa.getValor(i,"presupuesto_inicial_prpoa"));
									tab_anual.setValor("valor_codificado_pranu", tab_aprueba_poa.getValor(i,"presupuesto_inicial_prpoa"));
									tab_anual.setValor("valor_reformado_h_pranu", "0");
									tab_anual.setValor("valor_reformado_d_pranu", "0");
									tab_anual.setValor("valor_devengado_pranu", "0");
									tab_anual.setValor("valor_precomprometido_pranu", "0");
									tab_anual.setValor("valor_eje_comprometido_pranu", "0");
									tab_anual.setValor("valor_recaudado_pranu", "0");
									tab_anual.setValor("valor_recaudado_efectivo_pranu", "0");
									tab_anual.setValor("activo_pranu", "true");
									tab_anual.guardar();
									guardarPantalla();
									System.out.println("Importar POA ok ide_prpoa: "+tab_aprueba_poa.getValor(i,"ide_prpoa"));
								}
								
								String sql="update pre_poa set ejecutado_presupuesto_prpoa=true where ide_prpoa= "+tab_aprueba_poa.getValor(i,"ide_prpoa");
								utilitario.getConexion().ejecutarSql(sql);
								
							}
						}
						catch(Exception ec)
						{
							utilitario.agregarMensajeInfo("Falta transferir el POA al Presupuesto", "");
							utilitario.agregarMensajeInfo("Falta transferir el POA a los programas presupuestarios.", "");
						}
					}
					
				}
				set_poa.cerrar();
			}

			else{
				utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
			}
		}
		else{
			set_poa.getTab_seleccion().setSql(ser_presupuesto.getPoa(com_anio.getValue().toString(),"true","false"));
			set_poa.getTab_seleccion().ejecutarSql();
			set_poa.dibujar();
			}
	}
	///metodo año
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_anual.setCondicion("not ide_prpro is null and ide_geani="+com_anio.getValue());
			tab_anual.ejecutarSql();
			tab_mensual.ejecutarValorForanea(tab_anual.getValorSeleccionado());
			tab_reforma.ejecutarValorForanea(tab_anual.getValorSeleccionado());
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");

		}
	}
	
	public void importarPrograma(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
	
		//Filtrar los clasificadores del año seleccionado
		set_programa.getTab_seleccion().setSql(ser_presupuesto.getPrograma("true,false"));
		set_programa.getTab_seleccion().ejecutarSql();
		set_programa.dibujar();

	}

	public void aceptarPrograma(){
		String str_seleccionado=set_programa.getValorSeleccionado();
		if(str_seleccionado!=null){
			//Inserto los empleados seleccionados en la tabla de participantes 
			TablaGenerica tab_programa=ser_presupuesto.getTablaGenericaPrograma(str_seleccionado);
			System.out.println(" tabla generica"+tab_programa.getSql());
			for(int i=0;i<tab_programa.getTotalFilas();i++){
				tab_anual.insertar();
				tab_anual.setValor("ide_prpro", tab_programa.getValor(i, "ide_prpro"));
				
			}
			set_programa.cerrar();
			utilitario.addUpdate("tab_anual");			
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;
		}
			if(tab_anual.isFocus()){
				tab_anual.insertar();
				tab_anual.setValor("ide_geani",com_anio.getValue()+"");

				}
				else if(tab_mensual.isFocus()){
				tab_mensual.insertar();
			}
				else if(tab_reforma.isFocus()){
					tab_reforma.insertar();
					
				}
				
		
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_anual.guardar()){
			//if(validarAnual()){
			if(tab_mensual.guardar()){
				if(tab_reforma.guardar()){
					
				}
			}
		}
		guardarPantalla();
	}
	
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}


	public Tabla getTab_anual() {
		return tab_anual;
	}


	public void setTab_anual(Tabla tab_anual) {
		this.tab_anual = tab_anual;
	}


	public Tabla getTab_mensual() {
		return tab_mensual;
	}


	public void setTab_mensual(Tabla tab_mensual) {
		this.tab_mensual = tab_mensual;
	}


	public Tabla getTab_reforma() {
		return tab_reforma;
	}


	public void setTab_reforma(Tabla tab_reforma) {
		this.tab_reforma = tab_reforma;
	}


	public SeleccionTabla getSet_programa() {
		return set_programa;
	}


	public void setSet_programa(SeleccionTabla set_programa) {
		this.set_programa = set_programa;
	}
	public SeleccionTabla getSet_poa() {
		return set_poa;
	}
	public void setSet_poa(SeleccionTabla set_poa) {
		this.set_poa = set_poa;
	}

}
