package paq_presupuesto;

import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.NodeSelectEvent;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;

public class pre_funcion_programa extends Pantalla {
	private Tabla tab_funcion_programa=new Tabla();
	private Tabla tab_vigente= new Tabla();
	private Arbol arb_funcion_programa=new Arbol();
	private SeleccionTabla set_sub_actividad=new SeleccionTabla();
	public static String par_sub_activdad;

	public static String par_sec_proyecto;
	public static String par_sec_programa;
	public static String par_sec_producto;
	public static String par_sec_fase;
	public static String par_proyecto;
	public static String par_programa;
	public static String par_producto;
	public static String par_fase;	
	
	public static String par_producto_mc;
	public static String par_sec_producto_mc;
	public static String par_actividad_mc;
	public static String par_sec_actividad_mc;

	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	public pre_funcion_programa (){
		par_sub_activdad=utilitario.getVariable("p_sub_actividad");

		par_sec_proyecto=utilitario.getVariable("p_modulo_secuencialproyecto");
		par_sec_programa=utilitario.getVariable("p_modulo_secuencialprograma");
		par_sec_producto=utilitario.getVariable("p_modulo_secuencialproducto");
		par_sec_fase=utilitario.getVariable("p_modulo_secuencialfase");
		par_sec_producto_mc=utilitario.getVariable("p_modulo_secuencialproducto_mc");
		par_sec_actividad_mc=utilitario.getVariable("p_modulo_secuencialactividad_mc");
		
		par_proyecto=utilitario.getVariable("p_proyecto");
		par_programa=utilitario.getVariable("p_programa");
		par_producto=utilitario.getVariable("p_producto");
		par_fase=utilitario.getVariable("p_fase");		
		par_producto_mc=utilitario.getVariable("p_producto_mc");
		par_actividad_mc=utilitario.getVariable("p_actividad_mc");		
		
		tab_funcion_programa.setId("tab_funcion_programa");
		tab_funcion_programa.setTipoFormulario(true);
		tab_funcion_programa.getGrid().setColumns(4);
		tab_funcion_programa.setHeader("FUNCION PROGRAMA");
		//tab_funcion_programa.setNumeroTabla(1);
		tab_funcion_programa.setTabla("pre_funcion_programa", "ide_prfup", 1);
		tab_funcion_programa.getColumna("ide_prnfp").setCombo("pre_nivel_funcion_programa", "ide_prnfp", "detalle_prnfp","");
		tab_funcion_programa.getColumna("ide_prnfp").setMetodoChange("validaSubActividad");
		tab_funcion_programa.agregarRelacion(tab_vigente);
		tab_funcion_programa.setCampoPadre( "pre_ide_prfup");
		tab_funcion_programa.setCampoNombre("codigo_prfup||' '||detalle_prfup");
		tab_funcion_programa.agregarArbol(arb_funcion_programa);
		tab_funcion_programa.getColumna("ide_prsua").setCombo("select ide_prsua,codigo_prsua,detalle_prsua from pre_sub_actividad order by codigo_prsua,detalle_prsua");
		tab_funcion_programa.getColumna("ide_prsua").setLectura(true);
		tab_funcion_programa.getColumna("ide_prsua").setAutoCompletar();
		tab_funcion_programa.getColumna("activo_prfup").setValorDefecto("true");
		tab_funcion_programa.getColumna("ide_prfup").setNombreVisual("CODIGO");
		tab_funcion_programa.getColumna("detalle_prfup").setNombreVisual("NOMBRE");
		tab_funcion_programa.getColumna("codigo_prfup").setNombreVisual("CODIGO");
		tab_funcion_programa.getColumna("activo_prfup").setNombreVisual("ACTIVO");
		tab_funcion_programa.getColumna("ide_prnfp").setNombreVisual("NIVEL DE FUNCION");
		tab_funcion_programa.getColumna("ide_prsua").setNombreVisual("SUB ACTIVIDAD");

		tab_funcion_programa.dibujar();
		PanelTabla pat_funcion_programa=new PanelTabla();
		pat_funcion_programa.setPanelTabla(tab_funcion_programa);
		agregarComponente(pat_funcion_programa);
		
		arb_funcion_programa.setId("arb_funcion_programa");
		arb_funcion_programa.onSelect("seleccionoClasificador");
		arb_funcion_programa.dibujar();

		// tabla deaÃ±os vigente
		tab_vigente.setId("tab_vigente");
		tab_vigente.setHeader("AÑO VIGENTE");
		tab_vigente.setTabla("cont_vigente", "ide_covig", 2);
		tab_vigente.setCondicion("not ide_prfup is null");
		tab_vigente.getColumna("ide_geani").setCombo("gen_anio","ide_geani","detalle_geani","");
		tab_vigente.getColumna("ide_geani").setUnico(true);
		tab_vigente.getColumna("ide_prfup").setUnico(true);
		
		tab_vigente.dibujar();
		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_vigente);
		
		//division2
      	Division div_vigente = new Division();
 		div_vigente.setId("div_vigente");
 		div_vigente.dividir2( pat_funcion_programa, pat_panel2,"50%","h");
 		agregarComponente(div_vigente);

				
		//arbol
		
		Division div_division=new Division();
		div_division.dividir2(arb_funcion_programa, div_vigente, "25%", "v");
      	agregarComponente(div_division);
      	
        //sub_actividad
		
		Boton bot_sub_actividad=new Boton();
		bot_sub_actividad.setValue("Agregar Sub Actividad");
		bot_sub_actividad.setMetodo("agregarSubActividad");
		bar_botones.agregarBoton(bot_sub_actividad);
		
		set_sub_actividad.setId("set_sub_actividad");
		set_sub_actividad.setTitle("SELECCIONE UNA SUB_ACTIVIDAD");
		set_sub_actividad.setRadio();
		set_sub_actividad.setSeleccionTabla("select ide_prsua,codigo_prsua,detalle_prsua from pre_sub_actividad order by codigo_prsua,detalle_prsua", "");  
		set_sub_actividad.getTab_seleccion().getColumna("detalle_prsua").setFiltroContenido();
		set_sub_actividad.getBot_aceptar().setMetodo("aceptarSubActividad");
		agregarComponente(set_sub_actividad);
      	
      		
	}
	
	/**DJ
	 * Se ejecuta cuando se selecciona algun nodo del arbol
	 */
	public void seleccionoClasificador(NodeSelectEvent evt){
	tab_funcion_programa.limpiar();	
	//Asigna evento al arbol
	arb_funcion_programa.seleccionarNodo(evt);
	//Filtra la tabla Padre
	tab_funcion_programa.ejecutarValorPadre(arb_funcion_programa.getValorSeleccionado());
	//Filtra la tabla tab_vigente
	tab_vigente.ejecutarValorForanea(tab_funcion_programa.getValorSeleccionado());
  }

	public void agregarSubActividad(){
		
		if( pckUtilidades.CConversion.CStr(tab_funcion_programa.getValor("ide_prnfp")).equals(par_sub_activdad)){
			set_sub_actividad.getTab_seleccion().setSql("select ide_prsua,codigo_prsua,detalle_prsua from pre_sub_actividad order by codigo_prsua,detalle_prsua");  
			set_sub_actividad.getTab_seleccion().ejecutarSql();
			set_sub_actividad.dibujar();
		}
		else {
			utilitario.agregarNotificacionInfo("Nivel no Valido", "El nivel debe ser Sub Actividad para poder agregar la Sub Actividad");
		}
		
	}

	public void aceptarSubActividad(){
		TablaGenerica sub_actividad= utilitario.consultar("select ide_prsua,detalle_prsua,codigo_prsua from pre_sub_actividad where ide_prsua ="+set_sub_actividad.getValorSeleccionado());
		TablaGenerica codigo_anterior=utilitario.consultar("select ide_prnfp,codigo_prfup from pre_funcion_programa where ide_prfup="+tab_funcion_programa.getValor("pre_ide_prfup"));
		if(set_sub_actividad.getValorSeleccionado()!=null){
			String nuevo_codigo=codigo_anterior.getValor("codigo_prfup")+"."+sub_actividad.getValor("codigo_prsua");
			tab_funcion_programa.setValor("ide_prsua", set_sub_actividad.getValorSeleccionado());
			tab_funcion_programa.setValor("detalle_prfup", sub_actividad.getValor("detalle_prsua"));
			tab_funcion_programa.setValor("codigo_prfup",nuevo_codigo);
			//Actualiza 
			tab_funcion_programa.modificar(tab_funcion_programa.getFilaActual());
			utilitario.addUpdate("tab_funcion_programa");//actualiza mediante ajax el objeto tab_poa
			set_sub_actividad.cerrar();
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar una Sub_Actividad", "");
		}
	}

	public void validaSubActividad(AjaxBehaviorEvent evt){
		System.out.println("ingrese al evento");
	
		tab_funcion_programa.modificar(evt);//Siempre es la primera linea
		if(tab_funcion_programa.getValor("ide_prnfp").equals(par_sub_activdad)){
			System.out.println("ingrese porel if");
	
			tab_funcion_programa.getColumna("detalle_prfup").setLectura(true);
			tab_funcion_programa.setValor("detalle_prfup", "");
			tab_funcion_programa.getColumna("codigo_prfup").setLectura(true);
			tab_funcion_programa.setValor("codigo_prfup", "");
			utilitario.agregarMensajeInfo("Agregar", "Para crear una Sub Actividad Seleccione dar clic en Agregar Sub Actividad");
	
		}
		else {
			if(tab_funcion_programa.getValor("ide_prnfp").equals(par_actividad_mc)){
				System.out.println("ingrese por el if");
				utilitario.agregarMensajeInfo("Agregar", "El codigo obra ingresado sera reflejado en la ejecución presupuestaria...");
				utilitario.agregarNotificacionInfo("Mensaje", "El codigo Obra y su Detalle seran reflejados en la ejecución presupuestaria... Favor activar el check Obra...");
			}
			actualizaCodigo();
		}
		utilitario.addUpdateTabla(tab_funcion_programa, "detalle_prfup,codigo_prfup", "");
		
	}
	
	public void actualizaCodigo(){
		TablaGenerica codigo_anterior=utilitario.consultar("select ide_prnfp,codigo_prfup from pre_funcion_programa where ide_prnfp="+tab_funcion_programa.getValor("ide_prnfp"));
		codigo_anterior.imprimirSql();
		String nuevo_codigo="";
		if(tab_funcion_programa.getValor("ide_prnfp").equals(par_programa)){
			nuevo_codigo=ser_contabilidad.numeroSecuencial(par_sec_programa);
	
		}
		if(tab_funcion_programa.getValor("ide_prnfp").equals(par_proyecto)){
			//nuevo_codigo=codigo_anterior.getValor("codigo_prfup")+""+ser_contabilidad.numeroSecuencial(par_sec_proyecto);
			nuevo_codigo=ser_contabilidad.numeroSecuencial(par_sec_proyecto);
		}
		if(tab_funcion_programa.getValor("ide_prnfp").equals(par_producto)){
			nuevo_codigo=codigo_anterior.getValor("codigo_prfup")+"."+ser_contabilidad.numeroSecuencial(par_sec_producto);
			//nuevo_codigo=ser_contabilidad.numeroSecuencial(par_sec_producto);
		}
		if(tab_funcion_programa.getValor("ide_prnfp").equals(par_fase)){
			nuevo_codigo=codigo_anterior.getValor("codigo_prfup")+"."+ser_contabilidad.numeroSecuencial(par_sec_fase);
			//nuevo_codigo=ser_contabilidad.numeroSecuencial(par_sec_fase);
		}
		
		if(tab_funcion_programa.getValor("ide_prnfp").equals(par_producto_mc)){
			//nuevo_codigo=codigo_anterior.getValor("codigo_prfup")+"."+ser_contabilidad.numeroSecuencial(par_sec_producto);
			nuevo_codigo=ser_contabilidad.numeroSecuencial(par_sec_producto_mc);
		}
		if(tab_funcion_programa.getValor("ide_prnfp").equals(par_actividad_mc)){
			//nuevo_codigo=codigo_anterior.getValor("codigo_prfup")+"."+ser_contabilidad.numeroSecuencial(par_sec_fase);
			nuevo_codigo=ser_contabilidad.numeroSecuencial(par_sec_actividad_mc);
	
		}
	
		tab_funcion_programa.setValor("codigo_prfup",nuevo_codigo);
		utilitario.addUpdateTabla(tab_funcion_programa, "codigo_prfup", "");
	
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_funcion_programa.isFocus()) {
			tab_funcion_programa.insertar();
		}
		else if (tab_vigente.isFocus()) {
			tab_vigente.insertar();
			if(pckUtilidades.CConversion.CInt(tab_funcion_programa.getValor("ide_prfup"))>0)
				tab_vigente.setValor("ide_prfup",tab_funcion_programa.getValor("ide_prfup"));
		}
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
		if(tab_funcion_programa.isEmpty()){
			if (!tab_funcion_programa.guardar()) {
				   
				   if(tab_funcion_programa.isFilaInsertada()){
					    System.out.println("es nuevo registro ");
						if(tab_funcion_programa.getValor("ide_prnfp").equals(par_programa)){
						ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_programa),par_sec_programa);
	
						}
						if(tab_funcion_programa.getValor("ide_prnfp").equals(par_proyecto)){
						ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_proyecto),par_sec_proyecto);
	
						}
						if(tab_funcion_programa.getValor("ide_prnfp").equals(par_producto)){
						ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_producto),par_sec_producto);
						
						}
						if(tab_funcion_programa.getValor("ide_prnfp").equals(par_fase)){
							ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_fase),par_sec_fase);
	
						}
				   }
				
				tab_vigente.guardar();
				
				guardarPantalla();
				//Actualizar el arbol
				arb_funcion_programa.ejecutarSql();
				utilitario.addUpdate("arb_funcion_programa");
				
			}
			return;
		}
		
		else if(tab_funcion_programa.isFocus()){
			TablaGenerica nivel_funcion=utilitario.consultar("select * from pre_nivel_funcion_programa where ide_prnfp ="+tab_funcion_programa.getValor("ide_prnfp"));
			nivel_funcion.imprimirSql();
			int nivelpadre =0;
			if(tab_funcion_programa.getValor("pre_ide_prfup")==null){
				nivelpadre=0;
			}
			else {
				
				if(tab_funcion_programa.getValor("pre_ide_prfup").isEmpty()){
					nivelpadre=0;
				}
				else {
					TablaGenerica nivel_funcion_padre=utilitario.consultar("select * from pre_nivel_funcion_programa where ide_prnfp in (select ide_prnfp from pre_funcion_programa where ide_prfup="+tab_funcion_programa.getValor("pre_ide_prfup")+")");
					nivel_funcion_padre.imprimirSql();
					//nivelpadre=pckUtilidades.CConversion.CInt(nivel_funcion_padre.getValor("ide_prnfp")); //poa 2016
					nivelpadre=pckUtilidades.CConversion.CInt(nivel_funcion_padre.getValor("nivel_prnfp")); //poa 2017
				}
			}
			
			//int nivel = pckUtilidades.CConversion.CInt(nivel_funcion.getValor("ide_prnfp"));	//poa 2016
			int nivel = pckUtilidades.CConversion.CInt(nivel_funcion.getValor("nivel_prnfp"));	//poa 2017
			
			int nivel_restado=nivel-1;
			
			if(nivel_restado==nivelpadre)
			{	
				if(tab_funcion_programa.isFilaInsertada()){
					   System.out.println("es nuevo registro ");
					if(tab_funcion_programa.getValor("ide_prnfp").equals(par_programa)){
					ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_programa),par_sec_programa);

					}
					if(tab_funcion_programa.getValor("ide_prnfp").equals(par_proyecto)){
					ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_proyecto),par_sec_proyecto);

					}
					if(tab_funcion_programa.getValor("ide_prnfp").equals(par_producto)){
					ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_producto),par_sec_producto);
					
					}
					if(tab_funcion_programa.getValor("ide_prnfp").equals(par_fase)){
						ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_fase),par_sec_fase);

					}
				   }
				

				if (tab_funcion_programa.guardar()) {
					if (tab_vigente.guardar()) {
						//System.out.println(" entre aguardar ");
						guardarPantalla();
						//Actualizar el arbol
						arb_funcion_programa.ejecutarSql();
						utilitario.addUpdate("arb_funcion_programa");
				
				
					}
			
				}
		}
		else {
			utilitario.agregarMensajeError("No se puede Guardar", "Revise el nivel jerarquico para la creación del presente registro");
			}
		}
		
		else if (tab_vigente.isFocus()){
			
			if(tab_funcion_programa.isFilaInsertada()){
				   //System.out.println("es nuevo registro ");
				if(tab_funcion_programa.getValor("ide_prnfp").equals(par_programa)){
					ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_programa),par_sec_programa);

				}
				if(tab_funcion_programa.getValor("ide_prnfp").equals(par_proyecto)){
					ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_proyecto),par_sec_proyecto);

				}
				if(tab_funcion_programa.getValor("ide_prnfp").equals(par_producto)){
					ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_producto),par_sec_producto);
				
				}
				if(tab_funcion_programa.getValor("ide_prnfp").equals(par_fase)){
					ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_fase),par_sec_fase);

				}
			   }
			
				tab_vigente.guardar();
				guardarPantalla();

		}
	}


	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
		tab_funcion_programa.guardar();
		tab_vigente.guardar();
		guardarPantalla();
	}
	
	public Tabla getTab_funcion_programa() {
		return tab_funcion_programa;
	}


	public void setTab_funcion_programa(Tabla tab_funcion_programa) {
		this.tab_funcion_programa = tab_funcion_programa;
	}


	public Tabla getTab_vigente() {
		return tab_vigente;
	}


	public void setTab_vigente(Tabla tab_vigente) {
		this.tab_vigente = tab_vigente;
	}


	public Arbol getArb_funcion_programa() {
		return arb_funcion_programa;
	}


	public void setArb_funcion_programa(Arbol arb_funcion_programa) {
		this.arb_funcion_programa = arb_funcion_programa;
	}

	public SeleccionTabla getSet_sub_actividad() {
		return set_sub_actividad;
	}

	public void setSet_sub_actividad(SeleccionTabla set_sub_actividad) {
		this.set_sub_actividad = set_sub_actividad;
	}



}
