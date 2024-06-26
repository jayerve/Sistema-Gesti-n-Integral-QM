package paq_presupuesto;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_contratacion_publica extends Pantalla {
	private Tabla tab_contratacion=new Tabla();
	private Tabla tab_responsable=new Tabla();
	private Tabla tab_partida=new Tabla();
	private Tabla tab_archivo=new Tabla();
	private SeleccionTabla set_empleado=new SeleccionTabla();
	private SeleccionTabla set_actualizar = new SeleccionTabla();
	private Dialogo dia_empleado=new Dialogo();
	private Confirmar con_guardar =new Confirmar();
	
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	
	public pre_contratacion_publica(){
	
		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_contratacion.setId("tab_contratacion");
		tab_contratacion.setHeader("PROCESOS DE CONTRATACION PUBLICA");
		tab_contratacion.setTabla("pre_contratacion_publica","ide_prcop",1);
		tab_contratacion.getColumna("ide_prfop").setCombo("pre_forma_pago", "ide_prfop", "detalle_prfop", "");
		tab_contratacion.getColumna("ide_coest").setCombo("cont_estado", " ide_coest", "detalle_coest", "");
		tab_contratacion.getColumna("ide_prtpc").setCombo("pre_tipo_contratacion", "ide_prtpc", "detalle_prtpc", "");
		tab_contratacion.getColumna("ide_prpac").setCombo("pre_pac", "ide_prpac", "descripcion_prpac", "");
		tab_contratacion.getColumna("ide_cotio").setCombo("cont_tipo_compra", "ide_cotio", "detalle_cotio", "");
		tab_contratacion.setTipoFormulario(true);
		tab_contratacion.getGrid().setColumns(4);
		tab_contratacion.agregarRelacion(tab_responsable);//agraga relacion para los tabuladores
		tab_contratacion.agregarRelacion(tab_partida);
		tab_contratacion.agregarRelacion(tab_archivo);
		tab_contratacion.dibujar();
		PanelTabla pat_contratacion=new PanelTabla();
		pat_contratacion.setPanelTabla(tab_contratacion);
		
		//RESPONSABLE CONTRATO
		tab_responsable.setId("tab_responsable");
		tab_responsable.setHeader("RESPONSABLES DE CONTRATACION");
		tab_responsable.setIdCompleto("tab_tabulador:tab_responsable");
		tab_responsable.setTabla("pre_responsable_contratacion","ide_prrec",2);
		tab_responsable.getColumna("IDE_GTEMP").setVisible(false);
		tab_responsable.setCampoForanea("ide_prcop");
		tab_responsable.getColumna("IDE_GEEDP").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_responsable.getColumna("IDE_GEEDP").setAutoCompletar();
		tab_responsable.getColumna("IDE_GEEDP").setLectura(true);
		tab_responsable.getColumna("IDE_GEEDP").setUnico(true);
		tab_responsable.getColumna("ide_prrec").setUnico(true);
		tab_responsable.setCampoForanea("ide_prcop");
		tab_responsable.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_responsable);
		 
		
	   	
		// CONTRATACION PARTIDA
		tab_partida.setId("tab_partida");
		tab_partida.setHeader("CONTRATACION PARTIDA");
		tab_partida.setIdCompleto("tab_tabulador:tab_partida");
		tab_partida.setTabla("pre_contratacion_partida", "ide_prcoa",3);
		tab_partida.getColumna("ide_prpro").setCombo("pre_programa", "ide_prpro", "cod_programa_prpro", "");
		tab_partida.setCampoForanea("ide_prcop");
		tab_partida.dibujar();
		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_partida);
		
		
		//ARCHIVO
		tab_archivo.setId("tab_archivo");
		tab_archivo.setHeader("ARCHIVOS ANEXOS");
		tab_archivo.setIdCompleto("tab_tabulador:tab_archivo");
		tab_archivo.setTipoFormulario(true);
		tab_archivo.setTabla("pre_archivo","ide_prarc",5);
		tab_archivo.getColumna("foto_prarc").setUpload("presupuesto");
		tab_archivo.setCampoForanea("ide_prcop");
		//ocultar campos de las claves  foraneas
		TablaGenerica  tab_generica=ser_contabilidad.getTablaVigente("pre_archivo");
		for(int i=0;i<tab_generica.getTotalFilas();i++){
		//muestra los ides q quiere mostras.
		if(!tab_generica.getValor(i, "column_name").equals("ide_prcop")){	
		tab_archivo.getColumna(tab_generica.getValor(i, "column_name")).setVisible(false);	
		}				
		}
		tab_archivo.setCondicion("ide_prcon!=null");
		tab_archivo.dibujar();
		PanelTabla pat_panel5= new PanelTabla();
		pat_panel5.setPanelTabla(tab_archivo);
		// FONDO
		Imagen fondo= new Imagen();  
		fondo.setStyle("text-aling:center;position:absolute;top:100px;left:490px;");
		fondo.setValue("imagenes/logo.png");
		pat_panel5.setWidth("100%");
		pat_panel5.getChildren().add(fondo);

		tab_tabulador.agregarTab("RESPONSABLES DE CONTRATACION", pat_panel2);//intancia los tabuladores 
		tab_tabulador.agregarTab("CONTRATACION PARTIDA",pat_panel3);
		tab_tabulador.agregarTab("ARCHIVOS ANEXOS",pat_panel5);
		

		//division2
		
		Division div_division=new Division();
		div_division.dividir2(pat_contratacion,tab_tabulador,"50%","H");
		agregarComponente(div_division);
		//Pantalla Dialogo 
		//bara el boton empleado 
		Boton bot_empleado=new Boton();
		bot_empleado.setIcon("ui-icon-person");
		bot_empleado.setValue("Agregar Responsable");
		bot_empleado.setMetodo("importarEmpleado");
		bar_botones.agregarBoton(bot_empleado);
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);
				
		set_empleado.setId("set_empleado");
		set_empleado.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true"),"ide_geedp");
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("nombres_apellidos").setFiltro(true);
		set_empleado.setTitle("Seleccione un Empleado");
		set_empleado.getBot_aceptar().setMetodo("aceptarEmpleado");
		agregarComponente(set_empleado);
	
		// Boton Actualizar Representante
		Boton bot_actualizar=new Boton();
		bot_actualizar.setIcon("ui-icon-person");
		bot_actualizar.setValue("Actualizar Responsable");
		bot_actualizar.setMetodo("actualizarResponsable");
		bar_botones.agregarBoton(bot_actualizar);
		
		set_actualizar.setId("set_actualizar");
		set_actualizar.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true"),"ide_geedp");
		set_actualizar.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_actualizar.getTab_seleccion().getColumna("nombres_apellidos").setFiltro(true);
		set_actualizar.setRadio();
		set_actualizar.getBot_aceptar().setMetodo("modificarResponsable");
		agregarComponente(set_actualizar);	
		
	
		
		
	}
	public void actualizarResponsable(){
		if (tab_responsable.getValor("ide_prrec")==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Responsable para actualizar","");
			return;
		}
		set_actualizar.getTab_seleccion().setSql(ser_nomina.servicioEmpleadoContrato("true"));
		set_actualizar.getTab_seleccion().ejecutarSql();
		set_actualizar.dibujar();	
		
	}
	public void modificarResponsable(){
		String str_empleadoActualizado=set_actualizar.getValorSeleccionado();
	   	TablaGenerica tab_empleadoModificadoResponsable = ser_nomina.ideEmpleadoContrato(str_empleadoActualizado,"true");		
	    tab_responsable.setValor("IDE_GEEDP", tab_empleadoModificadoResponsable.getValor("IDE_GEEDP"));			
	    tab_responsable.setValor("IDE_GTEMP", tab_empleadoModificadoResponsable.getValor("IDE_GTEMP"));	
	    tab_responsable.modificar(tab_responsable.getFilaActual());
		utilitario.addUpdate("tab_responsable");	

		con_guardar.setMessage("Esta Seguro de Actualizar el Responsable");
		con_guardar.setTitle("CONFIRMCION DE ACTUALIZAR");
		con_guardar.getBot_aceptar().setMetodo("guardarActualilzarResponsable");
		con_guardar.dibujar();
		utilitario.addUpdate("con_guardar");


	}
	public void guardarActualilzarResponsable(){
		System.out.println("Entra a guardar...");
		tab_responsable.guardar();
		con_guardar.cerrar();
		set_actualizar.cerrar();


		guardarPantalla();

	}



		
	public void importarEmpleado(){
		if (tab_contratacion.isEmpty()) {
			utilitario.agregarMensajeInfo("Debe ingresar un registro en el contrato", "");
			return;
			
		}
							
		set_empleado.getTab_seleccion().setSql(ser_nomina.servicioEmpleadoContrato("true"));
		set_empleado.getTab_seleccion().ejecutarSql();
		set_empleado.dibujar();
		}
			

		public void aceptarEmpleado(){
			String str_seleccionados=set_empleado.getSeleccionados();
			if(str_seleccionados!=null){
				//Inserto los empleados seleccionados en la tabla de resposable d econtratacion 
				TablaGenerica tab_empleado_responsable = ser_nomina.ideEmpleadoContrato(str_seleccionados,"true");		
							
				
				for(int i=0;i<tab_empleado_responsable.getTotalFilas();i++){
					tab_responsable.insertar();
					tab_responsable.setValor("IDE_GEEDP", tab_empleado_responsable.getValor(i, "IDE_GEEDP"));			
					tab_responsable.setValor("IDE_GTEMP", tab_empleado_responsable.getValor(i, "IDE_GTEMP"));			
					
				}
				set_empleado.cerrar();
				utilitario.addUpdate("tab_responsable");			
			}
			else{
				utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
			}
		}

		//Dialogo Empleaado
		public void aceptarDialogo(){
				//Muestra un mensaje al dar click sobre el boton aceptar del Dialogo
				utilitario.agregarMensaje("SU NOMBRE ES","");
				dia_empleado.cerrar();//cierra el dialogo
				}
			public void abrirDialogo(){
				//Dibuja el dialogo al dar click sobre el boton abrir
				dia_empleado.dibujar();
				}	
		// Fin Dialogo	Empleado
			
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
		
		if (tab_contratacion.isFocus()) {
			tab_contratacion.insertar();
			//tab_contratacion.setValor("ide_geani", com_anio.getValue()+"");

		}
		else if (tab_responsable.isFocus()) {
			tab_responsable.insertar();

		}
		else if (tab_partida.isFocus()) {
			tab_partida.insertar();
			
		}

		
		else if (tab_archivo.isFocus()) {
			tab_archivo.insertar();

		}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_contratacion.guardar()) {
			
			if (tab_responsable.guardar()) {
				if( tab_partida.guardar()){
					tab_archivo.guardar();	
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


	public Tabla getTab_contratacion() {
		return tab_contratacion;
	}


	public void setTab_contratacion(Tabla tab_contratacion) {
		this.tab_contratacion = tab_contratacion;
	}


	public Tabla getTab_responsable() {
		return tab_responsable;
	}


	public void setTab_responsable(Tabla tab_responsable) {
		this.tab_responsable = tab_responsable;
	}


	public Tabla getTab_partida() {
		return tab_partida;
	}


	public void setTab_partida(Tabla tab_partida) {
		this.tab_partida = tab_partida;
	}


	public Tabla getTab_archivo() {
		return tab_archivo;
	}


	public void setTab_archivo(Tabla tab_archivo) {
		this.tab_archivo = tab_archivo;
	}

	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}

	public Dialogo getDia_empleado() {
		return dia_empleado;
	}

	public void setDia_empleado(Dialogo dia_empleado) {
		this.dia_empleado = dia_empleado;
	}
	public SeleccionTabla getSet_actualizar() {
		return set_actualizar;
	}
	public void setSet_actualizar(SeleccionTabla set_actualizar) {
		this.set_actualizar = set_actualizar;
	}
	public Confirmar getCon_guardar() {
		return con_guardar;
	}
	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}


}
