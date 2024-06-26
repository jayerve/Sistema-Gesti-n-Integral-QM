/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sri;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.event.SelectEvent;

import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import pckUtilidades.CConversion;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_formulario107_manual extends Pantalla {

    private Tabla tab_tabla = new Tabla();
	private AutoCompletar aut_empleado = new AutoCompletar();
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private Confirmar con_guardar_proyeccion=new Confirmar();
	private Dialogo dia_aplica_generacion_ingresos = new Dialogo();
	private Check chk_aplica_generacion = new Check();
	private Integer band_proyeccion_ingresos=0;
	
	
    public pre_formulario107_manual() {
    	
 
	
 		con_guardar_proyeccion.setId("con_guardar_proyeccion");
		agregarComponente(con_guardar_proyeccion);  
	
	
		Etiqueta eti_aplica_proyeccion_ingresos=new Etiqueta("GENERAR PROYECCIÓN DE INGRESOS");
		chk_aplica_generacion.setId("chk_aplica_generacion");
		PanelGrid panGrianulacion = new PanelGrid();
		panGrianulacion.setColumns(2);
		panGrianulacion.getChildren().add(eti_aplica_proyeccion_ingresos);
		panGrianulacion.getChildren().add(chk_aplica_generacion);

		dia_aplica_generacion_ingresos.setId("dia_aplica_generacion_ingresos");
		dia_aplica_generacion_ingresos.setTitle("CONFIRMACION GENERACIÓN DE INGRESOS");
		dia_aplica_generacion_ingresos.getBot_aceptar().setMetodo("validarGeneracionIngresos");
		dia_aplica_generacion_ingresos.getBot_cancelar().setRendered(false);

		dia_aplica_generacion_ingresos.getGri_cuerpo().getChildren().add(panGrianulacion);
		dia_aplica_generacion_ingresos.setWidth("20%");
		dia_aplica_generacion_ingresos.setHeight("15%");
		//dia_aplica_vacion.setDynamic(false);
		agregarComponente(dia_aplica_generacion_ingresos);
    	
		aut_empleado.setId("aut_empleado");
		String str_sql_emp=ser_gestion.getSqlEmpleadosAutocompletar();
		aut_empleado.setAutoCompletar(str_sql_emp);		
		aut_empleado.setMetodoChange("filtrarAnticiposEmpleado");

		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		


		Etiqueta eti_colaborador=new Etiqueta("Empleado:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);
		bar_botones.getBot_inicio().setMetodo("inicio");
		bar_botones.getBot_fin().setMetodo("fin");
		bar_botones.getBot_siguiente().setMetodo("siguiente");
		bar_botones.getBot_atras().setMetodo("atras");

    	
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("SRI_FORMULARIO_107", "IDE_SRFOR", 1);

		tab_tabla.getColumna("ide_srfor").setNombreVisual("CODIGO");
		tab_tabla.getColumna("ide_srfor").setLongitud(30);
		tab_tabla.getColumna("ide_srfor").setOrden(1);
		
		tab_tabla.getColumna("r301_srfor").setNombreVisual("301 SUELDOS Y SALARIOS");
		tab_tabla.getColumna("r301_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r301_srfor").setLongitud(30);
		tab_tabla.getColumna("r301_srfor").setOrden(2);
	
		
		tab_tabla.getColumna("r303_srfor").setNombreVisual("303 SOBRESUELDOS,COMISIONES,BONOS Y OTROS INGRESOS GRAVADOS");
		tab_tabla.getColumna("r303_srfor").setLongitud(30);
		tab_tabla.getColumna("r303_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r303_srfor").setOrden(3);
		
		tab_tabla.getColumna("r305_srfor").setNombreVisual("305 PARTICIPACIÓN UTILIDADES");
		tab_tabla.getColumna("r305_srfor").setLongitud(30);
		tab_tabla.getColumna("r305_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r305_srfor").setOrden(4);
		
		tab_tabla.getColumna("r307_srfor").setNombreVisual("307 INGRESOS GRAVADOS GENERADOS CON OTROS EMPLEADORES");
		tab_tabla.getColumna("r307_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r307_srfor").setLongitud(30);
		tab_tabla.getColumna("r307_srfor").setOrden(5);
		tab_tabla.getColumna("r307_srfor").setMetodoChange("getIngresosGravados");;
		
		
		tab_tabla.getColumna("r311_srfor").setNombreVisual("311 DÉCIMO TERCER SUELDO");
		tab_tabla.getColumna("r311_srfor").setLongitud(30);
		tab_tabla.getColumna("r311_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r311_srfor").setOrden(6);
		
		tab_tabla.getColumna("r313_srfor").setNombreVisual("313 CUARTO DÉCIMO SUELDO");
		tab_tabla.getColumna("r313_srfor").setLongitud(30);
		tab_tabla.getColumna("r313_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r313_srfor").setOrden(7);
		
		
		tab_tabla.getColumna("r315_srfor").setNombreVisual("315 FONDO DE RESERVA");
		tab_tabla.getColumna("r315_srfor").setLongitud(30);
		tab_tabla.getColumna("r315_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r315_srfor").setOrden(8);
		
		tab_tabla.getColumna("r317_srfor").setNombreVisual("317 OTROS INGRESOS EN RELACIÓN DE DEPENDENCIA");
		tab_tabla.getColumna("r317_srfor").setLongitud(30);
		tab_tabla.getColumna("r317_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r317_srfor").setOrden(9);
		
		tab_tabla.getColumna("r351_srfor").setNombreVisual("351 APORTE PERSONAL IESS CON ESTE EMPLEADOR");
		tab_tabla.getColumna("r351_srfor").setLongitud(30);
		tab_tabla.getColumna("r351_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r351_srfor").setOrden(10);
		
		

		tab_tabla.getColumna("r353_srfor").setNombreVisual("353 APORTE PERSONAL IESS CON OTROS EMPLEADORES");
		tab_tabla.getColumna("r353_srfor").setLongitud(30);
		tab_tabla.getColumna("r353_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r353_srfor").setOrden(11);

		
		tab_tabla.getColumna("r361_srfor").setNombreVisual("361 VIVIENDA");
		tab_tabla.getColumna("r361_srfor").setLongitud(30);
		tab_tabla.getColumna("r361_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r361_srfor").setOrden(12);
		
		
		tab_tabla.getColumna("r363_srfor").setNombreVisual("363 SALUD");
		tab_tabla.getColumna("r363_srfor").setLongitud(30);
		tab_tabla.getColumna("r363_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r363_srfor").setOrden(13);
		
		
		tab_tabla.getColumna("r365_srfor").setNombreVisual("365 EDUCACIÓN");
		tab_tabla.getColumna("r365_srfor").setLongitud(30);
		tab_tabla.getColumna("r365_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r365_srfor").setOrden(14);
		
		tab_tabla.getColumna("r367_srfor").setNombreVisual("367 ALIMENTACIÓN");
		tab_tabla.getColumna("r367_srfor").setLongitud(30);
		tab_tabla.getColumna("r367_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r367_srfor").setOrden(15);	

		
		tab_tabla.getColumna("r369_srfor").setNombreVisual("369 VESTIMENTA");
		tab_tabla.getColumna("r369_srfor").setLongitud(30);
		tab_tabla.getColumna("r369_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r369_srfor").setOrden(16);
		
		
		tab_tabla.getColumna("r371_srfor").setNombreVisual("371 EXONERACIÓN POR DISCAPACIDAD");
		tab_tabla.getColumna("r371_srfor").setLongitud(30);
		tab_tabla.getColumna("r371_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r371_srfor").setOrden(17);
		
		
		tab_tabla.getColumna("r373_srfor").setNombreVisual("373 EXONERACIÓN POR TERCERA EDAD");
		tab_tabla.getColumna("r373_srfor").setLongitud(30);
		tab_tabla.getColumna("r373_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r373_srfor").setOrden(18);
		
		
		tab_tabla.getColumna("r381_srfor").setNombreVisual("381 IMPUESTO A LA RENTA ASUMIDO POR ESTE EMPLEADOR");
		tab_tabla.getColumna("r381_srfor").setLongitud(30);
		tab_tabla.getColumna("r381_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r381_srfor").setOrden(19);
		
		
		tab_tabla.getColumna("r399_srfor").setNombreVisual("399 BASE IMPONIBLE GRAVADA");
		tab_tabla.getColumna("r399_srfor").setLongitud(30);
		tab_tabla.getColumna("r399_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r399_srfor").setOrden(20);	
		
		tab_tabla.getColumna("r401_srfor").setNombreVisual("401 IMPUESTO A LA RENTA CAUSADO");
		tab_tabla.getColumna("r401_srfor").setLongitud(30);
		tab_tabla.getColumna("r401_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r401_srfor").setOrden(21);
		
		tab_tabla.getColumna("r403_srfor").setNombreVisual("403 IMPUESTO RETENIDO Y ASUMIDO POR OTROS EMPLEADORES");
		tab_tabla.getColumna("r403_srfor").setLongitud(30);
		tab_tabla.getColumna("r403_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r403_srfor").setOrden(22);
		
		
		tab_tabla.getColumna("r405_srfor").setNombreVisual("405 VALOR IMPUESTO ASUMIDO POR ESTE EMPLEADOR");
		tab_tabla.getColumna("r405_srfor").setLongitud(30);
		tab_tabla.getColumna("r405_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r405_srfor").setOrden(23);
		
		
		tab_tabla.getColumna("r407_srfor").setNombreVisual("407 VALOR IMPUESTO RETENIDO AL TRABAJADOR POR ESTE EMPLEADOR");
		tab_tabla.getColumna("r407_srfor").setLongitud(30);
		tab_tabla.getColumna("r407_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r407_srfor").setOrden(24);
		
		tab_tabla.getColumna("r349_srfor").setNombreVisual("349 INGRESOS GRAVADOS CON ESTE EMPLEADOR");
		tab_tabla.getColumna("r349_srfor").setLongitud(30);
		tab_tabla.getColumna("r349_srfor").setValorDefecto("0.0");
		tab_tabla.getColumna("r349_srfor").setOrden(25);			
		tab_tabla.getColumna("IDE_GTEMP").setVisible(false);			
		tab_tabla.getColumna("ACTIVO_SRFOR").setValorDefecto("true");		
		tab_tabla.getColumna("ACTIVO_SRFOR").setCheck();
		tab_tabla.getColumna("ACTIVO_SRFOR").setNombreVisual("ACTIVO");
		
		tab_tabla.getColumna("IDE_SRIMR").setVisible(false);	
		tab_tabla.getColumna("tipo_formulario_srfor").setCombo("SELECT ide_srtif, descripcion_srtif FROM sri_tipo_formulario107 order by descripcion_srtif asc ");
		tab_tabla.getColumna("tipo_formulario_srfor").setNombreVisual("TIPO FORMULARIO");
		
		
		
		tab_tabla.getColumna("fecha_registro_srfor").setVisible(false);	
		tab_tabla.getColumna("fecha_registro_srfor").setValorDefecto(utilitario.getFechaActual());	

		tab_tabla.setCondicion("ide_srfor=-1");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setMensajeWarn("REGISTRO MANUAL FORMULARIO 107");
        pat_panel.setBorder(5);
        pat_panel.setPanelTabla(tab_tabla);
		
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    	
    	if (tab_tabla.isFocus()){
			if (aut_empleado.getValor()!=null){
					tab_tabla.insertar();
					tab_tabla.setValor("IDE_GTEMP", aut_empleado.getValor());
					/*TablaGenerica tabEmpleadosDepartamentoPar=utilitario.consultar("select ide_geedp,ide_gtemp from gen_empleados_departamento_par where "
							+ "ide_gtemp="+aut_empleado.getValor()+" order by ide_geedp desc");
					tab_tabla.setValor("IDE_GEEDP", tabEmpleadosDepartamentoPar.getValor("IDE_GEEDP"));*/
					TablaGenerica tabImpuestoRenta=utilitario.consultar("SELECT ide_srimr, detalle_srimr, "
					+ "fecha_inicio_srimr, fecha_fin_srimr "
					+ "FROM sri_impuesto_renta  "
					+ "where detalle_srimr like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%'");
					tab_tabla.setValor("IDE_SRIMR",""+Integer.parseInt(tabImpuestoRenta.getValor("IDE_SRIMR")));
					tab_tabla.setValor("fecha_registro_srfor",utilitario.getFechaActual());			
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado que solicita el Permiso");
			}
		}    
    	
    
    }

    @Override
    public void guardar() {
        try {
        if (tab_tabla.guardar()) {
				if (validarFormulario107()) {
				//Valida que los rubros no sean null	
				}else{
					return;  		
				}

				if (Double.parseDouble(tab_tabla.getValor("r307_srfor"))>0 && tab_tabla.getValor("tipo_formulario_srfor").equals("2")){
				//Si es de tipo otra empresa y el valor en  307 es mayor a cero
					TablaGenerica tab_sri_proyec=ser_nomina.getSriProyeccionIngresos(tab_tabla.getValor("ide_srimr"), tab_tabla.getValor("ide_gtemp"));
				
					if (tab_sri_proyec.getTotalFilas()>0) {
						//Si contiene proyeccion de ingresos		
					if (tab_tabla.getValor("fecha_hasta_laburo_srfor")==null || tab_tabla.getValor("fecha_hasta_laburo_srfor").isEmpty() || tab_tabla.getValor("fecha_hasta_laburo_srfor").equals("")) {
						//No contiene fecha hasta laburo
						utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar fecha hasta que fin de gestion");
						return;
					}else {
						//Contiene fecha hasta laburo y contiene proyeccion de ingresos
						//Actualiza la tabla de acuerdo al mes de la  fecha hasta laburo
						ejecutarActualizacionFormulario107(tab_tabla.getValor("fecha_hasta_laburo_srfor"), tab_tabla.getValor("ide_gtemp"), tab_tabla.getValor("ide_srimr"), tab_tabla.getValor("r307_srfor"));			
						 guardarPantalla();
					}
				
				}else {
					//No tiene proyeccion de ingresos
					if (tab_tabla.getValor("fecha_hasta_laburo_srfor")==null || tab_tabla.getValor("fecha_hasta_laburo_srfor").isEmpty() || tab_tabla.getValor("fecha_hasta_laburo_srfor").equals("")) {
						//No tiene fecha hasta laburo 
						utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar fecha hasta que fin de gestion");
						return;
					}else {
						//Si contiene fecha hasta laburo y Desea generar proyeccion de ingresos		
					dia_aplica_generacion_ingresos.dibujar();
					band_proyeccion_ingresos=1;}
					}	
		
					/*}else if(Double.parseDouble(tab_tabla.getValor("r307_srfor"))<=0 && tab_tabla.getValor("tipo_formulario_srfor").equals("2")){
					//Si es de tipo Otra empresa y el valor es menor que cero
						if (tab_tabla.getValor("fecha_hasta_laburo_srfor")==null || tab_tabla.getValor("fecha_hasta_laburo_srfor").isEmpty() || tab_tabla.getValor("fecha_hasta_laburo_srfor").equals("")) {
							//Si no tiene fecha hasta
							utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la fecha hasta de su fin de gestion");
							return;
						}else {			
							//Si tiene fecha hata pero el valor es menor a cero
							utilitario.agregarMensajeError("RUBRO 307 INGRESOS GRAVADOS GENERADOS CON OTROS EMPLEADORES", "Debe ingresar un valor mayor a 0");
							return;			
						}
						*/	
					}else {
					//Si no es de otra empresa
				TablaGenerica tab_sri_proyec=ser_nomina.getSriProyeccionIngresos(tab_tabla.getValor("ide_srimr"), tab_tabla.getValor("ide_gtemp"));
					if (tab_sri_proyec.getTotalFilas()>0) {
						// Si contiene un detalle de proyeccion de ingresos y no es de tipo (OTRA EMPRESA).	
            guardarPantalla();
					}else {
						//Desea Ingresar proyeccion de ingresos
						band_proyeccion_ingresos=0;
						dia_aplica_generacion_ingresos.dibujar();
					}	
					
					
					}
				
			   
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR AL GUARDAR");		
        }
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }

       
	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}


	String ide_geedp_activo="";
	public void filtrarAnticiposEmpleado(SelectEvent evt){
		aut_empleado.onSelect(evt);
		tab_tabla.setCondicion("IDE_GTEMP="+aut_empleado.getValor());
		tab_tabla.ejecutarSql();
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}
	
	
	public void limpiar(){
		aut_empleado.limpiar();
		tab_tabla.limpiar();
		utilitario.addUpdate("aut_empleado,tab_tabla");
	
}
	
	public void getIngresosGravados(){}
	
	public void ejecutarActualizacionFormulario107(String fecha_fin,String ide_gtemp,String ide_srimr, String valorIngresado){
		int mesTemp=0,anioTemp=0;
		String mes="",anio="",valorNuevo="",ide_srpri="";
		double valorRegitrado=0.0,valorARegistrar=0.0;
		mesTemp=utilitario.getMes(fecha_fin);
		valorRegitrado=Double.parseDouble(valorIngresado);
		valorARegistrar=valorRegitrado/mesTemp;
		valorNuevo=utilitario.getFormatoNumero(valorARegistrar, 2);
		TablaGenerica tab_det_proy_ing=ser_nomina.getSriDetalleProyeccionIngresos(ser_nomina.getSriProyeccionIngresos(ide_srimr, ide_gtemp).getValor("IDE_SRPRI"));
		ide_srpri=tab_det_proy_ing.getValor("ide_srpri");
		for (int i = 0; i <mesTemp; i++) {
		utilitario.getConexion().ejecutarSql("update sri_detalle_proyecccion_ingres set valor_srdpi=" + valorNuevo + " where ide_gemes="+(i+1)+"and ide_srpri="+ide_srpri);
		}

		
	}
	
	///GENERAR PROYECCION eec
		public void ConfirmarProyeccion() {
			
			con_guardar_proyeccion.setMessage("Esta Seguro de Realizar la Proyección de Ingresos, Recuerde que este proceso se debe realizar una ùnica vez al iniciar su gestión");
			con_guardar_proyeccion.setTitle("CONFIRMACIÓN PROYECCIÓN");
			con_guardar_proyeccion.getBot_aceptar().setMetodo("GenererProyeccion");
			con_guardar_proyeccion.dibujar();
			utilitario.addUpdate("con_guardar_proyeccion");
			
			
		}

		
		public void GenererProyeccion() {
			
			if(aut_empleado.getValor() != null)
			{
				utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'sri_formulario_107'");
				Long ide_num_max=utilitario.getConexion().getMaximo("sri_formulario_107", "ide_srfor", 1);		
				ser_nomina.registrarProyeccionIngresosEmpleadosInicialFormulario(aut_empleado.getValor());
				TablaGenerica tab_tot_ingresos_empleados = utilitario.consultar("SELECT empleado.ide_geedp as ide_geedp,rmu_geedp, empleado.ide_gtemp as ide_gtemp  FROM public.gen_empleados_departamento_par  empleado inner join (SELECT max(ide_geedp) as ide_geedp,ide_gtemp FROM public.gen_empleados_departamento_par   group by ide_gtemp) max_empleado on empleado.ide_gtemp = max_empleado.ide_gtemp and empleado.ide_geedp = max_empleado.ide_geedp inner join gth_empleado emp on  empleado.ide_gtemp = emp.ide_gtemp where  activo_gtemp = true and empleado.ide_gtemp  = " + 
				aut_empleado.getValor() + " order by empleado.ide_gtemp");
			
				if (tab_tot_ingresos_empleados.getTotalFilas()>0) {
					//Si contiene ingresada la accion de personal
					TablaGenerica tab_sri_imp_ren = ser_nomina.getSriImpuestoRenta(utilitario.getFechaActual());
	         	    String IDE_SRIMR = tab_sri_imp_ren.getValor("IDE_SRIMR");
	         	    TablaGenerica tab=utilitario.consultar("SELECT ide_srpri, ide_srimr, ide_gtemp, ide_geedp, ide_gemes, activo_srpri "
	         	    		+ "FROM sri_proyeccion_ingres  "
	         	    		+ "WHERE ide_srimr="+IDE_SRIMR+" AND IDE_GTEMP="+aut_empleado.getValor());
	         	    
	         	    if (tab.getTotalFilas()>0) {
						//Se ha generado la cabecera de proyeccion de ingresos
	         	    	TablaGenerica tab1=utilitario.consultar("SELECT ide_srdpi, ide_srpri, ide_gemes, valor_srdpi "
	         	    			+ "FROM sri_detalle_proyecccion_ingres  where ide_srpri="+tab.getValor("ide_srpri"));
	         	    	
	         	    if (tab1.getTotalFilas()>0) {
						//Se ha generado el detalle de proyeccion de ingresos
	         	    	if (band_proyeccion_ingresos==1) {
	            			ejecutarActualizacionFormulario107(tab_tabla.getValor("fecha_hasta_laburo_srfor"), tab_tot_ingresos_empleados.getValor("ide_gtemp"), IDE_SRIMR, tab_tabla.getValor("r307_srfor"));			
							// guardarPantalla();  	
						}else {
		         	    	utilitario.agregarMensajeInfo("Se guardo correctamente", "Se generó correctamente la proyección de ingresos");
		         	    	//guardarPantalla();   				
						}
					}else{
						//No ha generado el detalle de proyeccion de ingresos
						con_guardar_proyeccion.cerrar();
						utilitario.agregarMensajeInfo("No se pudo generar la proyección de ingresos", "No contiene un detalle de proyección de ingresos");
						return;
						
					}
		         	    	
					}else {
						//No se ha generado la cabecera de proyeccion de ingresos
						con_guardar_proyeccion.cerrar();
						utilitario.agregarMensajeInfo("No se pudo generar la proyección de ingresos", "No contiene un cabecera para proyección de ingresos");
						return;
					}
						
	         	    
	         	    
	         	    
				}else {
					//No contiene ingresada la accion de personal
					con_guardar_proyeccion.cerrar();
					utilitario.agregarMensajeInfo("No se pudo generar la proyección de ingresos", "El empleado no contiene una acción de personal activa");
					return;
				}
				
			    
			}else{
				utilitario.agregarMensajeInfo("No se puede realizar la proyección", "Debe seleccionar un empleado");
				return;	
			}
			con_guardar_proyeccion.cerrar();
			guardarPantalla();
		}	
		
		
		public void validarGeneracionIngresos(){
			//Si se desea generar los ingresos del nuevo empleado
			if((Boolean)chk_aplica_generacion.getValue()){
				dia_aplica_generacion_ingresos.cerrar();
				utilitario.addUpdate("dia_aplica_generacion_ingresos");
				//Acepta generacion de ingresos
				ConfirmarProyeccion();

				
			}else {
				//No se desea generar los ingresos del nuevo empleado
					if (band_proyeccion_ingresos==1) {
						dia_aplica_generacion_ingresos.cerrar();
						utilitario.addUpdate("dia_aplica_generacion_ingresos");
						utilitario.agregarMensajeError("El funcionario no contiene una proyección de ingresos","No puede continuar");
				    	return;			
					}else {
						dia_aplica_generacion_ingresos.cerrar();
						utilitario.addUpdate("dia_aplica_generacion_ingresos");
				        utilitario.agregarMensajeInfo("El funcionario no contiene una proyección de ingresos","Recuerde debe generar la proyección de ingresos ");
				    	guardarPantalla();						
					}
				
			//	return;
			}
			

		}

		public Dialogo getDia_aplica_generacion_ingresos() {
			return dia_aplica_generacion_ingresos;
		}

		public void setDia_aplica_generacion_ingresos(
				Dialogo dia_aplica_generacion_ingresos) {
			this.dia_aplica_generacion_ingresos = dia_aplica_generacion_ingresos;
		}

		public Confirmar getCon_guardar_proyeccion() {
			return con_guardar_proyeccion;
		}

		public void setCon_guardar_proyeccion(Confirmar con_guardar_proyeccion) {
			this.con_guardar_proyeccion = con_guardar_proyeccion;
		}
		
		/**
		 * Metodo valida que los rubros ingresados sean diferente de null
		 * @return valor true si encuentra algunainconsistencia
		 */
		public boolean validarFormulario107(){
			boolean estado=true;

		
			if(tab_tabla.getValor("tipo_formulario_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe seleccionar un tipo de formulario a ingresar");
				estado=false; 		
			}	
			
			if(tab_tabla.getValor("r307_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 307 INGRESOS GRAVADOS GENERADOS CON OTROS EMPLEADORES");
				estado=false;  		
			}
			
			if(tab_tabla.getValor("r301_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 301 SUELDOS Y SALARIOS");
				estado=false;  		
			}
			
			if(tab_tabla.getValor("r303_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 303 SOBRESUELDOS,COMISIONES,BONOS Y OTROS INGRESOS GRAVADOS");
				estado=false;  		
			}
		
			
			if(tab_tabla.getValor("r305_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 305 PARTICIPACIÓN UTILIDADES");
				estado=false;  		
			}
			
			
			if(tab_tabla.getValor("r311_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 311 DÉCIMO TERCER SUELDO");
				estado=false;  		
			}

			
			if(tab_tabla.getValor("r313_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 313 CUARTO DÉCIMO SUELDO");
				estado=false; 		
			}
	
			
			
			if(tab_tabla.getValor("r315_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 315 FONDO DE RESERVA");
				estado=false;  		
			}

			
			if(tab_tabla.getValor("r317_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 317 OTROS INGRESOS EN RELACIÓN DE DEPENDENCIA");
				estado=false;  		
			}


			if(tab_tabla.getValor("r351_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 351 APORTE PERSONAL IESS CON ESTE EMPLEADOR");
				estado=false;  		
			}

			

			if(tab_tabla.getValor("r353_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 353 APORTE PERSONAL IESS CON OTROS EMPLEADORES");
				estado=false;  		
			}
			

			if(tab_tabla.getValor("r361_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 361 VIVIENDA");
				estado=false;  		
			}

			
			
			if(tab_tabla.getValor("r363_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 363 SALUD");
				estado=false;  		
			}

			

			if(tab_tabla.getValor("r365_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 365 EDUCACIÓN");
				estado=false;  		
			}
			
			
			
			if(tab_tabla.getValor("r367_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 367 ALIMENTACIÓN");
				estado=false;  		
			}
			

			if(tab_tabla.getValor("r369_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 369 VESTIMENTA");
				estado=false;  		
			}
			

			if(tab_tabla.getValor("r371_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 371 EXONERACIÓN POR DISCAPACIDAD");
				estado=false;  		
			}
			
	
			if(tab_tabla.getValor("r373_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 373 EXONERACIÓN POR TERCERA EDAD");
				estado=false;  		
			}
			
			
			if(tab_tabla.getValor("r381_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 381 IMPUESTO A LA RENTA ASUMIDO POR ESTE EMPLEADOR");
				estado=false;  		
			}
			
			
			if(tab_tabla.getValor("r399_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 399 BASE IMPONIBLE GRAVADA");
				estado=false;  		
			}
			
			if(tab_tabla.getValor("r401_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 401 IMPUESTO A LA RENTA CAUSADO");
				estado=false;  		
			}
			

			if(tab_tabla.getValor("r403_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 403 IMPUESTO RETENIDO Y ASUMIDO POR OTROS EMPLEADORES");
				estado=false; 		
			}
			

			if(tab_tabla.getValor("r405_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 405 VALOR IMPUESTO ASUMIDO POR ESTE EMPLEADOR");
				estado=false; 		
			}
			
	
			if(tab_tabla.getValor("r407_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 407 VALOR IMPUESTO RETENIDO AL TRABAJADOR POR ESTE EMPLEADOR");
				estado=false;  		
			}
			

			
			if(tab_tabla.getValor("r349_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes a 349 INGRESOS GRAVADOS CON ESTE EMPLEADOR");
				estado=false;  		
			}
			
			
			if(tab_tabla.getValor("r307_srfor")==null){
				utilitario.agregarMensajeInfo("No se puede guardar el registro", "Debe ingresar los valores correspondientes en el formulario");
				estado=false;  		
			}
			
			return estado;
		}
		
		
}
