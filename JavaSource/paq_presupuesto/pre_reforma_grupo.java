package paq_presupuesto;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import paq_sistema.aplicacion.Utilitario;


public class pre_reforma_grupo extends Pantalla {
	private SeleccionTabla set_poa=new SeleccionTabla();
	private Combo com_anio=new Combo();
	private String empleado;
	private Tabla tab_reforma_grupo=new Tabla();
	private Etiqueta eti_val_municipio = new Etiqueta();
	private Etiqueta eti_val_hospitalario = new Etiqueta();
	private Etiqueta eti_val_tasa_recoleccion = new Etiqueta();
	private Etiqueta eti_val_ruminahi = new Etiqueta();
	private Etiqueta eti_val_escombreras = new Etiqueta();
	private Etiqueta eti_val_metro_quito = new Etiqueta();
	private Etiqueta eti_val_convenios = new Etiqueta();
	private Etiqueta eti_val_otros_gestores = new Etiqueta();
	private Etiqueta eti_val_saldo_bancos = new Etiqueta();
	private Etiqueta eti_val_com_rsu_aprov = new Etiqueta();
	private Etiqueta eti_val_tas_mu_ru_dic = new Etiqueta();
	private Etiqueta eti_val_tas_rec_dic = new Etiqueta();
	private Etiqueta eti_val_cue_x_cob_hosp = new Etiqueta();
	private Etiqueta eti_val_anticipo_contractual = new Etiqueta();
	//private Etiqueta eti_val_hospitalario = new Etiqueta();
	private Etiqueta eti_mensaje = new Etiqueta();
	private Dialogo dia_datos_reforma=new Dialogo();
	private Radio rad_aprobado= new Radio();
	private Texto txt_num_resolucion_guarda=new Texto();
	private Calendario cal_fecha_reforma=new Calendario();	
	private Etiqueta eti_total = new Etiqueta();
	private Radio rad_imprimir= new Radio();
	private Check che_aprobado=new Check();
	
	public static String par_modulo_municipio;
	public static String par_modulo_hospitalarios;
	public static String par_modulo_tasas_recoleccion;
	public static String par_modulo_ruminihaui;
	public static String par_modulo_escombreras;
	public static String par_modulo_metro_quito;
	public static String par_modulo_convenios;
	public static String par_modulo_otros_gestores;
	public static String par_modulo_tasas_anticpipos_prov_anteriores;
	public static String par_modulo_saldos_bancos;
	public static String par_modulo_comercializacion_rsu;
	public static String par_modulo_tasas_ruminahui_diciembre;
	public static String par_modulo_tasas_recoleccion_diciembre;
	public static String par_modulo_cuentas_por_cobrar_hosp;
	public static String par_modulo_anticipo_contractual;


	  @EJB
	  private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	   @EJB
	  private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);
	   @EJB
	 private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	   @EJB
	 private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	 			
	public pre_reforma_grupo(){
		
		
		bar_botones.getBot_insertar().setRendered(false);
		bar_botones.getBot_eliminar().setRendered(false);
		///BOTON COMBO
		empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		System.out.println("empleado"+empleado);
		if(empleado==null ||empleado.isEmpty()){
			utilitario.agregarNotificacionInfo("Mensaje", "No exixte usuario registrado para el registro de compras");
			return;
		}

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		List lista = new ArrayList();
	    Object fila1[] = {
	       "0", "Reforma Techos"
	    };
	    Object fila2[] = {
	       "1", "Reforma por Partida"
	    };
	   
	    lista.add(fila1);
	    lista.add(fila2);
	    rad_imprimir.setId("rad_imprimir");
	    rad_imprimir.setRadio(lista);
	    rad_imprimir.setValue(fila2);
	    //rad_imprimir.setho;

	    //bar_botones.agregarComponente(rad_imprimir);
	    
		/////boton buscar poa
		Boton bot_buscar=new Boton();
		bot_buscar.setIcon("ui-icon-person");
		bot_buscar.setValue("Buscar POA");
		bot_buscar.setMetodo("importarPoa");
		//bar_botones.agregarBoton(bot_buscar);
		
	    /*
	    Grid gri_componenetes = new Grid();
	    gri_componenetes.setColumns(5);
	    gri_componenetes.getChildren().add(rad_imprimir);
	    */
		
	    Grid gri_cabecera = new Grid();
	    gri_cabecera.setColumns(14);
	    
	    gri_cabecera.getChildren().add(bot_buscar);
	    gri_cabecera.getChildren().add(rad_imprimir);
	    gri_cabecera.getChildren().add(new Etiqueta(""));
	    gri_cabecera.getChildren().add(new Etiqueta(""));
	    gri_cabecera.getChildren().add(new Etiqueta("Saldo Total Reforma:"));
	    eti_total.setId("eti_total");
	    eti_total.setValue("0.00");
	    gri_cabecera.getChildren().add(eti_total);
	    gri_cabecera.getChildren().add(new Etiqueta(""));
	    gri_cabecera.getChildren().add(new Etiqueta(""));
	    gri_cabecera.getChildren().add(new Etiqueta(""));
	    gri_cabecera.getChildren().add(new Etiqueta(""));
	    gri_cabecera.getChildren().add(new Etiqueta(""));
	    gri_cabecera.getChildren().add(new Etiqueta(""));
	    gri_cabecera.getChildren().add(new Etiqueta(""));
	    gri_cabecera.getChildren().add(new Etiqueta(""));



	    
	    gri_cabecera.getChildren().add(new Etiqueta("Municipio"));
	    eti_val_municipio.setId("eti_val_municipio");;
	    eti_val_municipio.setValue("0.00");
	    eti_val_municipio.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
	    gri_cabecera.getChildren().add(eti_val_municipio);

	    gri_cabecera.getChildren().add(new Etiqueta("Hospitalarios"));
	    eti_val_hospitalario.setId("eti_val_hospitalario");
	    eti_val_hospitalario.setValue("0.00");
	    eti_val_hospitalario.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
	    gri_cabecera.getChildren().add(eti_val_hospitalario);	    

	    gri_cabecera.getChildren().add(new Etiqueta("Tasa de Recolección"));
	    eti_val_tasa_recoleccion.setId("eti_val_tasa_recoleccion");
	    eti_val_tasa_recoleccion.setValue("0.00");
	    eti_val_tasa_recoleccion.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
	    gri_cabecera.getChildren().add(eti_val_tasa_recoleccion);
	    
	    gri_cabecera.getChildren().add(new Etiqueta("Rumiñahui"));
	    eti_val_ruminahi.setId("eti_val_ruminahi");
	    eti_val_ruminahi.setValue("0.00");
	    eti_val_ruminahi.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
	    gri_cabecera.getChildren().add(eti_val_ruminahi);
	    
	    gri_cabecera.getChildren().add(new Etiqueta("Escombreras"));
	    eti_val_escombreras.setId("eti_val_escombreras");
	    eti_val_escombreras.setValue("0.00");
	    eti_val_escombreras.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
	    gri_cabecera.getChildren().add(eti_val_escombreras);
	    
	    gri_cabecera.getChildren().add(new Etiqueta("Metro Quito"));
	    eti_val_metro_quito.setId("eti_val_metro_quito");
	    eti_val_metro_quito.setValue("0.00");
	    eti_val_metro_quito.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
	    gri_cabecera.getChildren().add(eti_val_metro_quito);
	    
	    gri_cabecera.getChildren().add(new Etiqueta("Convenios"));
	    eti_val_convenios.setId("eti_val_convenios");
	    eti_val_convenios.setValue("0.00");
	    eti_val_convenios.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
	    gri_cabecera.getChildren().add(eti_val_convenios);
	    
	    
	    gri_cabecera.getChildren().add(new Etiqueta("Otros Gestores"));
	    eti_val_otros_gestores.setId("eti_val_otros_gestores");
	    eti_val_otros_gestores.setValue("0.00");
	    eti_val_otros_gestores.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
	    gri_cabecera.getChildren().add(eti_val_otros_gestores);
	    
	    gri_cabecera.getChildren().add(new Etiqueta("Saldo Bancos"));
	    eti_val_saldo_bancos.setId("eti_val_saldo_bancos");
	    eti_val_saldo_bancos.setValue("0.00");
	    eti_val_saldo_bancos.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
	    gri_cabecera.getChildren().add(eti_val_saldo_bancos);
	    
	    gri_cabecera.getChildren().add(new Etiqueta("Comercialización de RSU Aprovechables"));
	    eti_val_com_rsu_aprov.setId("eti_val_com_rsu_aprov");
	    eti_val_com_rsu_aprov.setValue("0.00");
	    eti_val_com_rsu_aprov.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
	    gri_cabecera.getChildren().add(eti_val_com_rsu_aprov);
	    
	    gri_cabecera.getChildren().add(new Etiqueta("Tasas Municipio Rumiñahui Diciembre"));
	    eti_val_tas_mu_ru_dic.setId("eti_val_tas_mu_ru_dic");
	    eti_val_tas_mu_ru_dic.setValue("0.00");
	    eti_val_tas_mu_ru_dic.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
	    gri_cabecera.getChildren().add(eti_val_tas_mu_ru_dic);

	    gri_cabecera.getChildren().add(new Etiqueta("Tasa de Recolección Diciembre"));
	    eti_val_tas_rec_dic.setId("eti_val_tas_rec_dic");
	    eti_val_tas_rec_dic.setValue("0.00");
	    eti_val_tas_rec_dic.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
	    gri_cabecera.getChildren().add(eti_val_tas_rec_dic);	    
	    
	    gri_cabecera.getChildren().add(new Etiqueta("Cuentas por Cobrar Hospitalarios"));
	    eti_val_cue_x_cob_hosp.setId("eti_val_cue_x_cob_hosp");
	    eti_val_cue_x_cob_hosp.setValue("0.00");
	    eti_val_cue_x_cob_hosp.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
	    gri_cabecera.getChildren().add(eti_val_cue_x_cob_hosp);
	    
	    gri_cabecera.getChildren().add(new Etiqueta("Anticipos Contractuales"));
	    eti_val_anticipo_contractual.setId("eti_val_anticipo_contractual");
	    eti_val_anticipo_contractual.setValue("0.00");
	    eti_val_anticipo_contractual.setEstiloContenido("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
	    gri_cabecera.getChildren().add(eti_val_anticipo_contractual);	    
	    
		tab_reforma_grupo.setId("tab_reforma_grupo");
		tab_reforma_grupo.setTabla("pre_reforma_grupo", "ide_prepoa",1);
		tab_reforma_grupo.getColumna("ide_prepoa").setLectura(true);
		tab_reforma_grupo.getColumna("ide_prepoa").setVisible(false);
		
		tab_reforma_grupo.getColumna("pre_proa").setOrden(1);
		tab_reforma_grupo.getColumna("pre_proa").setCombo(ser_presupuesto.getPoaTodos());
		tab_reforma_grupo.getColumna("pre_proa").setAutoCompletar();
		tab_reforma_grupo.getColumna("pre_proa").setLectura(true);	
		

		tab_reforma_grupo.getColumna("ide_municipio").setOrden(2);
		tab_reforma_grupo.getColumna("ide_municipio").setCombo("pre_fuente_financiamiento", "ide_prfuf", "detalle_prfuf", "");
		tab_reforma_grupo.getColumna("ide_municipio").setAutoCompletar();
		tab_reforma_grupo.getColumna("ide_municipio").setLectura(true);
		
		tab_reforma_grupo.getColumna("valor_reforma1").setOrden(3);
		tab_reforma_grupo.getColumna("valor_reforma1").setLectura(true);		
		tab_reforma_grupo.getColumna("val_reformado1").setOrden(4);
		tab_reforma_grupo.getColumna("val_reformado1").setMetodoChange("calcularReforma1");
		tab_reforma_grupo.getColumna("saldo_reforma1").setOrden(5);		
		tab_reforma_grupo.getColumna("saldo_reforma1").setLectura(true);	

		tab_reforma_grupo.getColumna("ide_hospitalarios").setOrden(6);		
		tab_reforma_grupo.getColumna("ide_hospitalarios").setCombo("pre_fuente_financiamiento", "ide_prfuf", "detalle_prfuf", "");
		tab_reforma_grupo.getColumna("ide_hospitalarios").setAutoCompletar();
		tab_reforma_grupo.getColumna("ide_hospitalarios").setLectura(true);

		tab_reforma_grupo.getColumna("valor_reforma2").setOrden(7);
		tab_reforma_grupo.getColumna("valor_reforma2").setLectura(true);		
		tab_reforma_grupo.getColumna("val_reformado2").setOrden(8);
		tab_reforma_grupo.getColumna("val_reformado2").setMetodoChange("calcularReforma2");
		tab_reforma_grupo.getColumna("saldo_reforma2").setOrden(9);		
		tab_reforma_grupo.getColumna("saldo_reforma2").setLectura(true);		

		tab_reforma_grupo.getColumna("ide_tasa_recoleccion").setOrden(10);		
		tab_reforma_grupo.getColumna("ide_tasa_recoleccion").setCombo("pre_fuente_financiamiento", "ide_prfuf", "detalle_prfuf", "");
		tab_reforma_grupo.getColumna("ide_tasa_recoleccion").setAutoCompletar();
		tab_reforma_grupo.getColumna("ide_tasa_recoleccion").setLectura(true);


		tab_reforma_grupo.getColumna("valor_reforma3").setOrden(11);
		tab_reforma_grupo.getColumna("valor_reforma3").setLectura(true);		
		tab_reforma_grupo.getColumna("val_reformado3").setOrden(12);
		tab_reforma_grupo.getColumna("val_reformado3").setMetodoChange("calcularReforma3");
		tab_reforma_grupo.getColumna("saldo_reforma3").setOrden(13);		
		tab_reforma_grupo.getColumna("saldo_reforma3").setLectura(true);			
		
		tab_reforma_grupo.getColumna("ide_ruminahi").setOrden(14);		
		tab_reforma_grupo.getColumna("ide_ruminahi").setCombo("pre_fuente_financiamiento", "ide_prfuf", "detalle_prfuf", "");
		tab_reforma_grupo.getColumna("ide_ruminahi").setAutoCompletar();
		tab_reforma_grupo.getColumna("ide_ruminahi").setLectura(true);

		tab_reforma_grupo.getColumna("valor_reforma4").setOrden(15);
		tab_reforma_grupo.getColumna("valor_reforma4").setLectura(true);		
		tab_reforma_grupo.getColumna("val_reformado4").setOrden(16);
		tab_reforma_grupo.getColumna("val_reformado4").setMetodoChange("calcularReforma4");
		tab_reforma_grupo.getColumna("saldo_reforma4").setOrden(17);		
		tab_reforma_grupo.getColumna("saldo_reforma4").setLectura(true);		

		tab_reforma_grupo.getColumna("ide_escombreras").setOrden(18);		
		tab_reforma_grupo.getColumna("ide_escombreras").setCombo("pre_fuente_financiamiento", "ide_prfuf", "detalle_prfuf", "");
		tab_reforma_grupo.getColumna("ide_escombreras").setAutoCompletar();
		tab_reforma_grupo.getColumna("ide_escombreras").setLectura(true);

		tab_reforma_grupo.getColumna("valor_reforma5").setOrden(19);
		tab_reforma_grupo.getColumna("valor_reforma5").setLectura(true);		
		tab_reforma_grupo.getColumna("val_reformado5").setOrden(20);
		tab_reforma_grupo.getColumna("val_reformado5").setMetodoChange("calcularReforma5");
		tab_reforma_grupo.getColumna("saldo_reforma5").setOrden(21);		
		tab_reforma_grupo.getColumna("saldo_reforma5").setLectura(true);

		tab_reforma_grupo.getColumna("ide_metro_quito").setOrden(22);		
		tab_reforma_grupo.getColumna("ide_metro_quito").setCombo("pre_fuente_financiamiento", "ide_prfuf", "detalle_prfuf", "");
		tab_reforma_grupo.getColumna("ide_metro_quito").setAutoCompletar();
		tab_reforma_grupo.getColumna("ide_metro_quito").setLectura(true);

		tab_reforma_grupo.getColumna("valor_reforma6").setOrden(23);
		tab_reforma_grupo.getColumna("valor_reforma6").setLectura(true);		
		tab_reforma_grupo.getColumna("val_reformado6").setOrden(24);
		tab_reforma_grupo.getColumna("val_reformado6").setMetodoChange("calcularReforma6");
		tab_reforma_grupo.getColumna("saldo_reforma6").setOrden(25);		
		tab_reforma_grupo.getColumna("saldo_reforma6").setLectura(true);
		
		tab_reforma_grupo.getColumna("ide_convenios").setOrden(26);		
		tab_reforma_grupo.getColumna("ide_convenios").setCombo("pre_fuente_financiamiento", "ide_prfuf", "detalle_prfuf", "");
		tab_reforma_grupo.getColumna("ide_convenios").setAutoCompletar();
		tab_reforma_grupo.getColumna("ide_convenios").setLectura(true);

		tab_reforma_grupo.getColumna("valor_reforma7").setOrden(27);
		tab_reforma_grupo.getColumna("valor_reforma7").setLectura(true);		
		tab_reforma_grupo.getColumna("val_reformado7").setOrden(28);
		tab_reforma_grupo.getColumna("val_reformado7").setMetodoChange("calcularReforma7");
		tab_reforma_grupo.getColumna("saldo_reforma7").setOrden(29);		
		tab_reforma_grupo.getColumna("saldo_reforma7").setLectura(true);		

		tab_reforma_grupo.getColumna("ide_otros_gestores").setOrden(30);		
		tab_reforma_grupo.getColumna("ide_otros_gestores").setCombo("pre_fuente_financiamiento", "ide_prfuf", "detalle_prfuf", "");
		tab_reforma_grupo.getColumna("ide_otros_gestores").setAutoCompletar();
		tab_reforma_grupo.getColumna("ide_otros_gestores").setLectura(true);

		tab_reforma_grupo.getColumna("valor_reforma8").setOrden(31);
		tab_reforma_grupo.getColumna("valor_reforma8").setLectura(true);		
		tab_reforma_grupo.getColumna("val_reformado8").setOrden(32);
		tab_reforma_grupo.getColumna("val_reformado8").setMetodoChange("calcularReforma8");
		tab_reforma_grupo.getColumna("saldo_reforma8").setOrden(33);		
		tab_reforma_grupo.getColumna("saldo_reforma8").setLectura(true);		

		tab_reforma_grupo.getColumna("ide_saldo_bancos").setOrden(34);		
		tab_reforma_grupo.getColumna("ide_saldo_bancos").setCombo("pre_fuente_financiamiento", "ide_prfuf", "detalle_prfuf", "");
		tab_reforma_grupo.getColumna("ide_saldo_bancos").setAutoCompletar();
		tab_reforma_grupo.getColumna("ide_saldo_bancos").setLectura(true);

		
		tab_reforma_grupo.getColumna("valor_reforma9").setOrden(35);
		tab_reforma_grupo.getColumna("valor_reforma9").setLectura(true);		
		tab_reforma_grupo.getColumna("val_reformado9").setOrden(36);
		tab_reforma_grupo.getColumna("val_reformado9").setMetodoChange("calcularReforma9");
		tab_reforma_grupo.getColumna("saldo_reforma9").setOrden(37);		
		tab_reforma_grupo.getColumna("saldo_reforma9").setLectura(true);		

		tab_reforma_grupo.getColumna("ide_com_rsu_aprov").setOrden(38);		
		tab_reforma_grupo.getColumna("ide_com_rsu_aprov").setCombo("pre_fuente_financiamiento", "ide_prfuf", "detalle_prfuf", "");
		tab_reforma_grupo.getColumna("ide_com_rsu_aprov").setAutoCompletar();
		tab_reforma_grupo.getColumna("ide_com_rsu_aprov").setLectura(true);

		tab_reforma_grupo.getColumna("valor_reforma10").setOrden(39);
		tab_reforma_grupo.getColumna("valor_reforma10").setLectura(true);		
		tab_reforma_grupo.getColumna("val_reformado10").setOrden(40);
		tab_reforma_grupo.getColumna("val_reformado10").setMetodoChange("calcularReforma10");
		tab_reforma_grupo.getColumna("saldo_reforma10").setOrden(41);		
		tab_reforma_grupo.getColumna("saldo_reforma10").setLectura(true);
		
		tab_reforma_grupo.getColumna("ide_tas_mu_ru_dic").setOrden(42);		
		tab_reforma_grupo.getColumna("ide_tas_mu_ru_dic").setCombo("pre_fuente_financiamiento", "ide_prfuf", "detalle_prfuf", "");
		tab_reforma_grupo.getColumna("ide_tas_mu_ru_dic").setAutoCompletar();
		tab_reforma_grupo.getColumna("ide_tas_mu_ru_dic").setLectura(true);

		tab_reforma_grupo.getColumna("valor_reforma11").setOrden(43);
		tab_reforma_grupo.getColumna("valor_reforma11").setLectura(true);		
		tab_reforma_grupo.getColumna("val_reformado11").setOrden(44);
		tab_reforma_grupo.getColumna("val_reformado11").setMetodoChange("calcularReforma11");
		tab_reforma_grupo.getColumna("saldo_reforma11").setOrden(45);		
		tab_reforma_grupo.getColumna("saldo_reforma11").setLectura(true);		

		tab_reforma_grupo.getColumna("ide_tas_rec_dic").setOrden(46);		
		tab_reforma_grupo.getColumna("ide_tas_rec_dic").setCombo("pre_fuente_financiamiento", "ide_prfuf", "detalle_prfuf", "");
		tab_reforma_grupo.getColumna("ide_tas_rec_dic").setAutoCompletar();
		tab_reforma_grupo.getColumna("ide_tas_rec_dic").setLectura(true);

		
		tab_reforma_grupo.getColumna("valor_reforma12").setOrden(47);
		tab_reforma_grupo.getColumna("valor_reforma12").setLectura(true);		
		tab_reforma_grupo.getColumna("val_reformado12").setOrden(48);
		tab_reforma_grupo.getColumna("val_reformado12").setMetodoChange("calcularReforma12");
		tab_reforma_grupo.getColumna("saldo_reforma12").setOrden(49);		
		tab_reforma_grupo.getColumna("saldo_reforma12").setLectura(true);		

		tab_reforma_grupo.getColumna("ide_cue_x_cob_hosp").setOrden(50);		
		tab_reforma_grupo.getColumna("ide_cue_x_cob_hosp").setCombo("pre_fuente_financiamiento", "ide_prfuf", "detalle_prfuf", "");
		tab_reforma_grupo.getColumna("ide_cue_x_cob_hosp").setAutoCompletar();
		tab_reforma_grupo.getColumna("ide_cue_x_cob_hosp").setLectura(true);

		tab_reforma_grupo.getColumna("valor_reforma13").setOrden(51);
		tab_reforma_grupo.getColumna("valor_reforma13").setLectura(true);		
		tab_reforma_grupo.getColumna("val_reformado13").setOrden(52);
		tab_reforma_grupo.getColumna("val_reformado13").setMetodoChange("calcularReforma13");
		tab_reforma_grupo.getColumna("saldo_reforma13").setOrden(53);		
		tab_reforma_grupo.getColumna("saldo_reforma13").setLectura(true);

		tab_reforma_grupo.getColumna("ide_anticipo_contractual").setOrden(54);		
		tab_reforma_grupo.getColumna("ide_anticipo_contractual").setCombo("pre_fuente_financiamiento", "ide_prfuf", "detalle_prfuf", "");
		tab_reforma_grupo.getColumna("ide_anticipo_contractual").setAutoCompletar();
		tab_reforma_grupo.getColumna("ide_anticipo_contractual").setLectura(true);
		
		tab_reforma_grupo.getColumna("valor_reforma14").setOrden(55);
		tab_reforma_grupo.getColumna("valor_reforma14").setLectura(true);		
		tab_reforma_grupo.getColumna("val_reformado14").setOrden(56);
		tab_reforma_grupo.getColumna("val_reformado14").setMetodoChange("calcularReforma14");
		tab_reforma_grupo.getColumna("saldo_reforma14").setOrden(57);		
		tab_reforma_grupo.getColumna("saldo_reforma14").setLectura(true);
		
		tab_reforma_grupo.setGenerarPrimaria(false);
		

		tab_reforma_grupo.dibujar();
		PanelTabla pat_panel=new PanelTabla();
		eti_mensaje.setId("eti_mensaje");
		eti_mensaje.setStyle("font-size: 13px;color: red;font-weight: bold; align: left");
		pat_panel.setHeader(eti_mensaje);
		pat_panel.setPanelTabla(tab_reforma_grupo);
		
		Division div_division=new Division();
		div_division.setId("div_division");
		div_division.dividir2(gri_cabecera,pat_panel,"20%","H");
		//div_division.dividir1(pat_panel);
		agregarComponente(div_division);
		
		

			
			set_poa.setId("set_poa");
			set_poa.setSeleccionTabla(ser_presupuesto.getPoaNombre("-1"),"ide_prpoa");
			set_poa.setTitle("Seleccione Poa");

			set_poa.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("DETALLE_PROGRAMA").setFiltro(true);//pone filtro
			//set_poa.getTab_seleccion().getColumna("PROGRAMA").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("DETALLE_PROYECTO").setFiltro(true);
		//	set_poa.getTab_seleccion().getColumna("PROYECTO").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("DETALLE_PRODUCTO").setFiltro(true);
			//set_poa.getTab_seleccion().getColumna("PRODUCTO").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("DETALLE_ACTIVIDAD").setFiltro(true);
		//	set_poa.getTab_seleccion().getColumna("ACTIVIDAD").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("DETALLE_SUBACTIVIDAD").setFiltro(true);
		//	set_poa.getTab_seleccion().getColumna("SUBACTIVIDAD").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("CODIGO_SUBACTIVIDAD").setFiltro(true);
		//	set_poa.getTab_seleccion().getColumna("NUM_RESOLUCION_PRPOA").setFiltro(true);
			
			

			set_poa.getBot_aceptar().setMetodo("aceptarPoa");
			//set_poa.setRadio();
			agregarComponente(set_poa);
		
			
			//Grid para el dialogo
	       	Grid gri_guarda_reforma = new Grid();
	       	gri_guarda_reforma.setColumns(2);
	       	
	       	Etiqueta eti_resolcion_guarda = new Etiqueta("Nro. Resolución: ");
	       	gri_guarda_reforma.getChildren().add(eti_resolcion_guarda);
	       	txt_num_resolucion_guarda.setId("txt_num_resolucion_guarda");
	       	txt_num_resolucion_guarda.setSize(15);
	       	gri_guarda_reforma.getChildren().add(txt_num_resolucion_guarda);
	       	
	       	Etiqueta eti_fecha_guarda = new Etiqueta("Fecha Reforma");
	       	gri_guarda_reforma.getChildren().add(eti_fecha_guarda);
	       	cal_fecha_reforma.setId("cal_fecha_reforma");
	       	cal_fecha_reforma.setFechaActual();
	       	gri_guarda_reforma.getChildren().add(cal_fecha_reforma);
	       	
	       	Etiqueta eti_che_aprobado = new Etiqueta("");
	       	gri_guarda_reforma.getChildren().add(eti_che_aprobado);
	       	
	       	List listax = new ArrayList();
		    Object filax1[] = {
		       "true", "Aprobado"
		    };
		    Object filax2[] = {
		       "false", "No Aprobado"
		    };
		   
		    listax.add(filax1);
		    listax.add(filax2);
		    rad_aprobado.setId("rad_aprobado");
		    rad_aprobado.setRadio(listax);
		    rad_aprobado.setValue(filax1);
		    rad_aprobado.setVertical();

		    
			dia_datos_reforma.setId("dia_datos_reforma");
	       	dia_datos_reforma.setTitle("Ingrese los Siguientes Datos para Realizar la Reforma");
	       	dia_datos_reforma.setWidth("25%");
	       	dia_datos_reforma.setHeight("20%");
	       	dia_datos_reforma.setDialogo(gri_guarda_reforma);
	       	dia_datos_reforma.getBot_aceptar().setMetodo("guardar");
			agregarComponente(dia_datos_reforma);
       	

	}
	
////importar poa
	public void importarPoa(){
		System.out.println(" ingresar al importar");
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un año", "");
			return;
		}
		

		set_poa.getTab_seleccion().setSql(ser_presupuesto.getPoaNombre(com_anio.getValue().toString()));
		set_poa.getTab_seleccion().ejecutarSql();
		set_poa.dibujar();

	}
	
	
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			//String str_seleccionados= set_poa.getSeleccionados();

			//tab_reforma_grupo.setCondicion("ide_prepoa in ("+str_seleccionados+")");

		tab_reforma_grupo.setCondicion("ide_prepoa="+com_anio.getValue());
			tab_reforma_grupo.ejecutarSql();
			//tab_reforma_grupo.ejecutarValorForanea(tab_certificacion.getValorSeleccionado());


		}
	}
	
	
	
	public  void aceptarPoa(){
		String str_seleccionados= set_poa.getSeleccionados();
		//System.out.println(" ingresar al aceptar");
		if (str_seleccionados!=null){
			TablaGenerica tab_poa = ser_presupuesto.getTablaGenericaPoa(str_seleccionados);		
			for(int i=0;i<tab_poa.getTotalFilas();i++){
				//TablaGenerica saldo_poa=utilitario.consultar(ser_presupuesto.getSaldoPoa(tab_poa.getValor( i,"ide_prepoa")));
		
				tab_reforma_grupo.insertar();
				tab_reforma_grupo.setValor("pre_proa", tab_poa.getValor( i,"ide_prpoa"));
				TablaGenerica tab_fuente_financiamiento=utilitario.consultar("select ide_prpof, " +
						"ide_prfuf,ide_prpoa,ide_coest,valor_financiamiento_prpof,activo_prpof  " +
						"from pre_poa_financiamiento " +
						"where ide_prpoa="+tab_poa.getValor(i,"ide_prpoa"));
				
				
				
				par_modulo_municipio=utilitario.getVariable("p_modulo_municipio");
				par_modulo_hospitalarios=utilitario.getVariable("p_modulo_hospitalario");
				par_modulo_tasas_recoleccion=utilitario.getVariable("p_modulo_tasas_recoleccion");
				par_modulo_ruminihaui=utilitario.getVariable("p_modulo_ruminahui");
				par_modulo_escombreras=utilitario.getVariable("p_modulo_escombreras");
				par_modulo_metro_quito=utilitario.getVariable("p_modulo_metro_quito");
				par_modulo_convenios=utilitario.getVariable("p_modulo_convenios");
				par_modulo_otros_gestores=utilitario.getVariable("p_modulo_otros_gestores");
				par_modulo_tasas_anticpipos_prov_anteriores=utilitario.getVariable("p_modulo_tasas_anticpipos_prov_anteriores");
				par_modulo_saldos_bancos=utilitario.getVariable("p_modulo_saldos_bancos");
				par_modulo_comercializacion_rsu=utilitario.getVariable("p_modulo_comercializacion_rsu");
				par_modulo_tasas_ruminahui_diciembre=utilitario.getVariable("p_modulo_tasas_ruminahui_diciembre");
				par_modulo_tasas_recoleccion_diciembre=utilitario.getVariable("p_modulo_tasas_recoleccion_diciembre");
				par_modulo_cuentas_por_cobrar_hosp=utilitario.getVariable("p_modulo_cuentas_por_cobrar_hosp");
				par_modulo_anticipo_contractual=utilitario.getVariable("p_modulo_anticipo_contractual");
				
				
				
//validaciones para generacion de informacion consultada		
				
				for (int j = 0; j < tab_fuente_financiamiento.getTotalFilas(); j++) {
				
					if (par_modulo_municipio.equals(tab_fuente_financiamiento.getValor(j,"ide_prfuf"))) {
						tab_reforma_grupo.setValor("ide_municipio", tab_fuente_financiamiento.getValor( j,"ide_prfuf"));
						TablaGenerica tab_saldos_grupo_fuente=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(),tab_fuente_financiamiento.getValor( j,"ide_prfuf"),"0","-1","-1"));

						for (int k = 0; k < tab_saldos_grupo_fuente.getTotalFilas(); k++) {
						
							if (tab_saldos_grupo_fuente.getValor(k,"ide_prpoa").equals(tab_fuente_financiamiento.getValor(j,"ide_prpoa"))) {
								tab_reforma_grupo.setValor("valor_reforma1", tab_saldos_grupo_fuente.getValor(k,"valor_saldo_fuente"));
								tab_reforma_grupo.setValor("val_reformado1", "0");
						
						 
						 }		
							
							

						}	
						
						
						
					 }
			
				 
				
					
					
				
					
				
					if (par_modulo_hospitalarios.equals(tab_fuente_financiamiento.getValor(j,"ide_prfuf"))) {
						tab_reforma_grupo.setValor("ide_hospitalarios", tab_fuente_financiamiento.getValor( j,"ide_prfuf"));
						TablaGenerica tab_saldos_grupo_fuente=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(), tab_fuente_financiamiento.getValor(j,"ide_prfuf"),"0","-1","-1"));

						for (int k = 0; k < tab_saldos_grupo_fuente.getTotalFilas(); k++) {
							
							if (tab_saldos_grupo_fuente.getValor(k,"ide_prpoa").equals(tab_fuente_financiamiento.getValor(j,"ide_prpoa"))) {
								tab_reforma_grupo.setValor("valor_reforma2", tab_saldos_grupo_fuente.getValor(k,"valor_saldo_fuente"));
								tab_reforma_grupo.setValor("val_reformado2", "0");
						
						 
						 }					
						}	
     				}
				
//					
//					
//					
//					
					if (par_modulo_tasas_recoleccion.equals(tab_fuente_financiamiento.getValor(j,"ide_prfuf"))) {
     					tab_reforma_grupo.setValor("ide_tasa_recoleccion", tab_fuente_financiamiento.getValor( j,"ide_prfuf"));
					    TablaGenerica tab_saldos_grupo_fuente=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(), tab_fuente_financiamiento.getValor(j,"ide_prfuf"),"0","-1","-1"));

     					for (int k = 0; k < tab_saldos_grupo_fuente.getTotalFilas(); k++) {
    						
							if (tab_saldos_grupo_fuente.getValor(k,"ide_prpoa").equals(tab_fuente_financiamiento.getValor(j,"ide_prpoa"))) {
								tab_reforma_grupo.setValor("valor_reforma3", tab_saldos_grupo_fuente.getValor(k,"valor_saldo_fuente"));
								tab_reforma_grupo.setValor("val_reformado3", "0");
		
						 
						 }					
						}			
					}
				
	
					if (par_modulo_ruminihaui.equals(tab_fuente_financiamiento.getValor(j,"ide_prfuf"))) {
						tab_reforma_grupo.setValor("ide_ruminahi", tab_fuente_financiamiento.getValor( j,"ide_prfuf"));
					    TablaGenerica tab_saldos_grupo_fuente=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(), tab_fuente_financiamiento.getValor(j,"ide_prfuf"),"0","-1","-1"));

						for (int k = 0; k < tab_saldos_grupo_fuente.getTotalFilas(); k++) {
							
							if (tab_saldos_grupo_fuente.getValor(k,"ide_prpoa").equals(tab_fuente_financiamiento.getValor(j,"ide_prpoa"))) {
								tab_reforma_grupo.setValor("valor_reforma4", tab_saldos_grupo_fuente.getValor(k,"valor_saldo_fuente"));
								tab_reforma_grupo.setValor("val_reformado4", "0");
						
						 
						 }					
						}	
						
						
					}
					
					
					if (par_modulo_escombreras.equals(tab_fuente_financiamiento.getValor(j,"ide_prfuf"))) {
						tab_reforma_grupo.setValor("ide_escombreras", tab_fuente_financiamiento.getValor( j,"ide_prfuf"));
					    TablaGenerica tab_saldos_grupo_fuente=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(), tab_fuente_financiamiento.getValor(j,"ide_prfuf"),"0","-1","-1"));

						for (int k = 0; k < tab_saldos_grupo_fuente.getTotalFilas(); k++) {
							
							if (tab_saldos_grupo_fuente.getValor(k,"ide_prpoa").equals(tab_fuente_financiamiento.getValor(j,"ide_prpoa"))) {
								tab_reforma_grupo.setValor("valor_reforma5", tab_saldos_grupo_fuente.getValor(k,"valor_saldo_fuente"));
								tab_reforma_grupo.setValor("val_reformado5", "0");
						
						 
						 }					
						}	
				
					}
					
					if (par_modulo_metro_quito.equals(tab_fuente_financiamiento.getValor(j,"ide_prfuf"))) {
						tab_reforma_grupo.setValor("ide_metro_quito", tab_fuente_financiamiento.getValor( j,"ide_prfuf"));
					    TablaGenerica tab_saldos_grupo_fuente=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(), tab_fuente_financiamiento.getValor(j,"ide_prfuf"),"0","-1","-1"));
					    for (int k = 0; k < tab_saldos_grupo_fuente.getTotalFilas(); k++) {
							
							if (tab_saldos_grupo_fuente.getValor(k,"ide_prpoa").equals(tab_fuente_financiamiento.getValor(j,"ide_prpoa"))) {
								tab_reforma_grupo.setValor("valor_reforma6", tab_saldos_grupo_fuente.getValor(k,"valor_saldo_fuente"));
								tab_reforma_grupo.setValor("val_reformado6", "0");
						
						 
						 }					
						}	
					
					}
					
					
					if (par_modulo_convenios.equals(tab_fuente_financiamiento.getValor(j,"ide_prfuf"))) {
						tab_reforma_grupo.setValor("ide_convenios", tab_fuente_financiamiento.getValor( j,"ide_prfuf"));
					    TablaGenerica tab_saldos_grupo_fuente=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(), tab_fuente_financiamiento.getValor(j,"ide_prfuf"),"0","-1","-1"));
					    for (int k = 0; k < tab_saldos_grupo_fuente.getTotalFilas(); k++) {
							
							if (tab_saldos_grupo_fuente.getValor(k,"ide_prpoa").equals(tab_fuente_financiamiento.getValor(j,"ide_prpoa"))) {
								tab_reforma_grupo.setValor("valor_reforma7", tab_saldos_grupo_fuente.getValor(k,"valor_saldo_fuente"));
								tab_reforma_grupo.setValor("val_reformado7", "0");
						
						 
						 }					
						}	
					
					}
					
					
					
					if (par_modulo_otros_gestores.equals(tab_fuente_financiamiento.getValor(j,"ide_prfuf"))) {
					    TablaGenerica tab_saldos_grupo_fuente=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(), tab_fuente_financiamiento.getValor(j,"ide_prfuf"),"0","-1","-1"));
						tab_reforma_grupo.setValor("ide_otros_gestores", tab_fuente_financiamiento.getValor( j,"ide_prfuf"));
				
	for (int k = 0; k < tab_saldos_grupo_fuente.getTotalFilas(); k++) {
							
							if (tab_saldos_grupo_fuente.getValor(k,"ide_prpoa").equals(tab_fuente_financiamiento.getValor(j,"ide_prpoa"))) {
								tab_reforma_grupo.setValor("valor_reforma8", tab_saldos_grupo_fuente.getValor(k,"valor_saldo_fuente"));
								tab_reforma_grupo.setValor("val_reformado8", "0");
						
						 
						 }					
						}				
					}
					
						/*		
					if (par_modulo_tasas_anticpipos_prov_anteriores.equals(tab_fuente_financiamiento.getValor(j,"ide_prfuf"))) {
						
						tab_reforma_grupo.setValor("ide_ant_en_prov_anter", tab_fuente_financiamiento.getValor( j,"ide_prfuf"));
					    TablaGenerica tab_saldos_grupo_fuente=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(), tab_fuente_financiamiento.getValor(j,"ide_prfuf"),"0","-1"));

	                  for (int k = 0; k < tab_saldos_grupo_fuente.getTotalFilas(); k++) {
							
							if (tab_saldos_grupo_fuente.getValor(k,"ide_prpoa").equals(tab_fuente_financiamiento.getValor(j,"ide_prpoa"))) {
								tab_reforma_grupo.setValor("valor_reforma9", tab_saldos_grupo_fuente.getValor(k,"valor_saldo_fuente"));
						
						 
						 }					
						}	
					
					}

					*/
					if (par_modulo_saldos_bancos.equals(tab_fuente_financiamiento.getValor(j,"ide_prfuf"))) {
					    TablaGenerica tab_saldos_grupo_fuente=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(), tab_fuente_financiamiento.getValor(j,"ide_prfuf"),"0","-1","-1"));
						tab_reforma_grupo.setValor("ide_saldo_bancos", tab_fuente_financiamiento.getValor( j,"ide_prfuf"));
	for (int k = 0; k < tab_saldos_grupo_fuente.getTotalFilas(); k++) {
							
							if (tab_saldos_grupo_fuente.getValor(k,"ide_prpoa").equals(tab_fuente_financiamiento.getValor(j,"ide_prpoa"))) {
								tab_reforma_grupo.setValor("valor_reforma9", tab_saldos_grupo_fuente.getValor(k,"valor_saldo_fuente"));
								tab_reforma_grupo.setValor("val_reformado9", "0");
						
						 
						 }					
						}	
					}
					
					
					if (par_modulo_comercializacion_rsu.equals(tab_fuente_financiamiento.getValor(j,"ide_prfuf"))) {
					    TablaGenerica tab_saldos_grupo_fuente=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(), tab_fuente_financiamiento.getValor(j,"ide_prfuf"),"0","-1","-1"));
						tab_reforma_grupo.setValor("ide_com_rsu_aprov", tab_fuente_financiamiento.getValor( j,"ide_prfuf"));
	for (int k = 0; k < tab_saldos_grupo_fuente.getTotalFilas(); k++) {
							
							if (tab_saldos_grupo_fuente.getValor(k,"ide_prpoa").equals(tab_fuente_financiamiento.getValor(j,"ide_prpoa"))) {
								tab_reforma_grupo.setValor("valor_reforma10", tab_saldos_grupo_fuente.getValor(k,"valor_saldo_fuente"));
								tab_reforma_grupo.setValor("val_reformado10","0");
						
						 
						 }					
						}	
					
					}
					
					
					if (par_modulo_tasas_ruminahui_diciembre.equals(tab_fuente_financiamiento.getValor(j,"ide_prfuf"))) {
					    TablaGenerica tab_saldos_grupo_fuente=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(), tab_fuente_financiamiento.getValor(j,"ide_prfuf"),"0","-1","-1"));
						tab_reforma_grupo.setValor("ide_tas_mu_ru_dic", tab_fuente_financiamiento.getValor( j,"ide_prfuf"));
	for (int k = 0; k < tab_saldos_grupo_fuente.getTotalFilas(); k++) {
							
							if (tab_saldos_grupo_fuente.getValor(k,"ide_prpoa").equals(tab_fuente_financiamiento.getValor(j,"ide_prpoa"))) {
								tab_reforma_grupo.setValor("valor_reforma11", tab_saldos_grupo_fuente.getValor(k,"valor_saldo_fuente"));
								tab_reforma_grupo.setValor("val_reformado11", "0");
						
						 
						 }					
						}	
					}
					
					
					if (par_modulo_tasas_recoleccion_diciembre.equals(tab_fuente_financiamiento.getValor(j,"ide_prfuf"))) {
					    TablaGenerica tab_saldos_grupo_fuente=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(), tab_fuente_financiamiento.getValor(j,"ide_prfuf"),"0","-1","-1"));
						tab_reforma_grupo.setValor("ide_tas_rec_dic", tab_fuente_financiamiento.getValor( j,"ide_prfuf"));
	for (int k = 0; k < tab_saldos_grupo_fuente.getTotalFilas(); k++) {
							
							if (tab_saldos_grupo_fuente.getValor(k,"ide_prpoa").equals(tab_fuente_financiamiento.getValor(j,"ide_prpoa"))) {
								tab_reforma_grupo.setValor("valor_reforma12", tab_saldos_grupo_fuente.getValor(k,"valor_saldo_fuente"));
								tab_reforma_grupo.setValor("val_reformado12", "0");
						
						 
						 }					
						}	
					}
					
					if (par_modulo_cuentas_por_cobrar_hosp.equals(tab_fuente_financiamiento.getValor(j,"ide_prfuf"))) {
					    TablaGenerica tab_saldos_grupo_fuente=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(), tab_fuente_financiamiento.getValor(j,"ide_prfuf"),"0","-1","-1"));
						tab_reforma_grupo.setValor("ide_cue_X_cob_hosp", tab_fuente_financiamiento.getValor( j,"ide_prfuf"));
						for (int k = 0; k < tab_saldos_grupo_fuente.getTotalFilas(); k++) {
							
							if (tab_saldos_grupo_fuente.getValor(k,"ide_prpoa").equals(tab_fuente_financiamiento.getValor(j,"ide_prpoa"))) {
								tab_reforma_grupo.setValor("valor_reforma13", tab_saldos_grupo_fuente.getValor(k,"valor_saldo_fuente"));
								tab_reforma_grupo.setValor("val_reformado13", "0");
						
						 
						 }					
						}	
					}
					
					if (par_modulo_anticipo_contractual.equals(tab_fuente_financiamiento.getValor(j,"ide_prfuf"))) {
					    TablaGenerica tab_saldos_grupo_fuente=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(), tab_fuente_financiamiento.getValor(j,"ide_prfuf"),"0","-1","-1"));
						tab_reforma_grupo.setValor("ide_anticipo_contractual", tab_fuente_financiamiento.getValor( j,"ide_prfuf"));
						for (int k = 0; k < tab_saldos_grupo_fuente.getTotalFilas(); k++) {
							
							if (tab_saldos_grupo_fuente.getValor(k,"ide_prpoa").equals(tab_fuente_financiamiento.getValor(j,"ide_prpoa"))) {
								tab_reforma_grupo.setValor("valor_reforma14", tab_saldos_grupo_fuente.getValor(k,"valor_saldo_fuente"));
								tab_reforma_grupo.setValor("val_reformado14", "0");
						
						 
						 }					
						}	
					}
				}
//					
				
			
			   
//				//	ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(), tab_fuente_financiamiento.getValor(j,"ide_prfuf") ,"0","-1");
//			
				
//				
		}
			

			for(int i=0;i<tab_poa.getTotalFilas();i++){

		
			String valor_nuevo="0";
			if (tab_reforma_grupo.getValor(i,"valor_reforma1")==null) {
						valor_nuevo="0";
			tab_reforma_grupo.setValor(i,"valor_reforma1", valor_nuevo);
			tab_reforma_grupo.setValor(i,"saldo_reforma1", valor_nuevo);
			tab_reforma_grupo.setValor(i,"val_reformado1", valor_nuevo);
				
			}
			
			if (tab_reforma_grupo.getValor(i,"valor_reforma2")==null) {
				valor_nuevo="0";
				tab_reforma_grupo.setValor(i,"valor_reforma2", valor_nuevo);
				tab_reforma_grupo.setValor(i,"saldo_reforma2", valor_nuevo);
				tab_reforma_grupo.setValor(i,"val_reformado2", valor_nuevo);
		
			}
			
			if (tab_reforma_grupo.getValor(i,"valor_reforma3")==null) {
				valor_nuevo="0";
				tab_reforma_grupo.setValor(i,"valor_reforma3", valor_nuevo);
				tab_reforma_grupo.setValor(i,"saldo_reforma3", valor_nuevo);
				tab_reforma_grupo.setValor(i,"val_reformado3", valor_nuevo);
				
		
			}
			if (tab_reforma_grupo.getValor(i,"valor_reforma4")==null) {
				valor_nuevo="0";
				tab_reforma_grupo.setValor(i,"valor_reforma4", valor_nuevo);
				tab_reforma_grupo.setValor(i,"saldo_reforma4", valor_nuevo);
				tab_reforma_grupo.setValor(i,"val_reformado4", valor_nuevo);
				
		
			}
			if (tab_reforma_grupo.getValor(i,"valor_reforma5")==null) {
				valor_nuevo="0";
				tab_reforma_grupo.setValor(i,"valor_reforma5", valor_nuevo);
				tab_reforma_grupo.setValor(i,"saldo_reforma5", valor_nuevo);
				tab_reforma_grupo.setValor(i,"val_reformado5", valor_nuevo);
				
		
			}
			if (tab_reforma_grupo.getValor(i,"valor_reforma6")==null) {
				valor_nuevo="0";
				tab_reforma_grupo.setValor(i,"valor_reforma6", valor_nuevo);
				tab_reforma_grupo.setValor(i,"saldo_reforma6", valor_nuevo);
				tab_reforma_grupo.setValor(i,"val_reformado6", valor_nuevo);
				
		
			}
			if (tab_reforma_grupo.getValor(i,"valor_reforma7")==null) {
				valor_nuevo="0";
				tab_reforma_grupo.setValor(i,"valor_reforma7", valor_nuevo);
				tab_reforma_grupo.setValor(i,"saldo_reforma7", valor_nuevo);
				tab_reforma_grupo.setValor(i,"val_reformado7", valor_nuevo);
				
		
			}
			if (tab_reforma_grupo.getValor(i,"valor_reforma8")==null) {
				valor_nuevo="0";
				tab_reforma_grupo.setValor(i,"valor_reforma8", valor_nuevo);
				tab_reforma_grupo.setValor(i,"saldo_reforma8", valor_nuevo);
				tab_reforma_grupo.setValor(i,"val_reformado8", valor_nuevo);
				
		
			}
			if (tab_reforma_grupo.getValor(i,"valor_reforma9")==null) {
				valor_nuevo="0";
				tab_reforma_grupo.setValor(i,"valor_reforma9", valor_nuevo);
				tab_reforma_grupo.setValor(i,"saldo_reforma9", valor_nuevo);
				tab_reforma_grupo.setValor(i,"val_reformado9", valor_nuevo);
				
		
			}
			if (tab_reforma_grupo.getValor(i,"valor_reforma10")==null) {
				valor_nuevo="0";
				tab_reforma_grupo.setValor(i,"valor_reforma10", valor_nuevo);
				tab_reforma_grupo.setValor(i,"saldo_reforma10", valor_nuevo);
				tab_reforma_grupo.setValor(i,"val_reformado10", valor_nuevo);
				
		
			}
			if (tab_reforma_grupo.getValor(i,"valor_reforma11")==null) {
				valor_nuevo="0";
				tab_reforma_grupo.setValor(i,"valor_reforma11", valor_nuevo);
				tab_reforma_grupo.setValor(i,"saldo_reforma11", valor_nuevo);
				tab_reforma_grupo.setValor(i,"val_reformado11", valor_nuevo);
				
		
			}
			if (tab_reforma_grupo.getValor(i,"valor_reforma12")==null) {
				valor_nuevo="0";
				tab_reforma_grupo.setValor(i,"valor_reforma12", valor_nuevo);
				tab_reforma_grupo.setValor(i,"saldo_reforma12", valor_nuevo);
				tab_reforma_grupo.setValor(i,"val_reformado12", valor_nuevo);
				
		
			}
			if (tab_reforma_grupo.getValor(i,"valor_reforma13")==null) {
				valor_nuevo="0";
				tab_reforma_grupo.setValor(i,"valor_reforma13", valor_nuevo);
				tab_reforma_grupo.setValor(i,"saldo_reforma13", valor_nuevo);
				tab_reforma_grupo.setValor(i,"val_reformado13", valor_nuevo);

		
			}
			if (tab_reforma_grupo.getValor(i,"valor_reforma14")==null) {
				valor_nuevo="0";
				tab_reforma_grupo.setValor(i,"valor_reforma14", valor_nuevo);
				tab_reforma_grupo.setValor(i,"saldo_reforma14", valor_nuevo);
				tab_reforma_grupo.setValor(i,"val_reformado14", valor_nuevo);

		
			}

			//else{
			//	valor_nuevo= tab_reforma_grupo.getValor(i,"valor_reforma1");

			//}
			}
			
			
			set_poa.cerrar();
			utilitario.addUpdate("tab_reforma_grupo");
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
		}
	
	}
	
	public void sumaTotales(){
		
		Double val_ide_municipio =tab_reforma_grupo.getSumaColumna("val_reformado1");
		Double val_ide_hospitalarios =tab_reforma_grupo.getSumaColumna("val_reformado2");
		Double val_ide_tasa_recoleccion =tab_reforma_grupo.getSumaColumna("val_reformado3");
		Double val_ide_ruminahi =tab_reforma_grupo.getSumaColumna("val_reformado4");
		Double val_ide_escombreras =tab_reforma_grupo.getSumaColumna("val_reformado5");
		Double val_ide_metro_quito =tab_reforma_grupo.getSumaColumna("val_reformado6");
		Double val_ide_convenios =tab_reforma_grupo.getSumaColumna("val_reformado7");
		Double val_ide_otros_gestores =tab_reforma_grupo.getSumaColumna("val_reformado8");
		Double val_ide_saldo_bancos =tab_reforma_grupo.getSumaColumna("val_reformado9");
		Double val_ide_com_rsu_aprov =tab_reforma_grupo.getSumaColumna("val_reformado10");
		Double val_ide_tas_mu_ru_dic =tab_reforma_grupo.getSumaColumna("val_reformado11");
		Double val_ide_tas_rec_dic =tab_reforma_grupo.getSumaColumna("val_reformado12");
		Double val_ide_cue_x_cob_hosp =tab_reforma_grupo.getSumaColumna("val_reformado13");
		Double val_ide_anticipo_contractual=tab_reforma_grupo.getSumaColumna("val_reformado14");
		
		Double val_total=val_ide_municipio + val_ide_hospitalarios + val_ide_tasa_recoleccion + val_ide_ruminahi + val_ide_escombreras + val_ide_metro_quito+
				val_ide_convenios + val_ide_otros_gestores+ val_ide_saldo_bancos + val_ide_com_rsu_aprov + val_ide_tas_mu_ru_dic + val_ide_tas_rec_dic + val_ide_cue_x_cob_hosp + val_ide_anticipo_contractual; 
		
		eti_total.setValue(val_total);
		utilitario.addUpdate("eti_total");
		
	}
	public void calcularReforma1(){
		
		eti_mensaje.setValue("");
		utilitario.addUpdate("eti_mensaje");		
//		System.out.println("valor del campo "+tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(), "ide_municipio"));
		if(tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(), "ide_municipio")==null){
		
			eti_mensaje.setValue("<img src='imagenes/stop.png' /> No existe Fuente de Finaciamiento");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma1", "0");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"val_reformado1", "0");

			eti_val_municipio.setValue(utilitario.getFormatoNumero("0", 2));
			utilitario.addUpdate("tab_reforma_grupo,eti_val_municipio,eti_mensaje");
		}
		
			
		
		String valor_reforma = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"valor_reforma1");
		String valor_a_reformar = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"val_reformado1");
		double dou_reforma = pckUtilidades.CConversion.CDbl_2(valor_reforma);
		double dou_a_reformar = pckUtilidades.CConversion.CDbl_2(valor_a_reformar);
		double resultado=0;
		resultado = dou_reforma+dou_a_reformar;
		//System.out.println("valores a frefomrar "+dou_reforma+" cuanto reformo "+dou_a_reformar+" resultado "+resultado);
		tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma1", resultado+"");
		eti_val_municipio.setValue(utilitario.getFormatoNumero(tab_reforma_grupo.getSumaColumna("val_reformado1"), 2));
		sumaTotales();
		utilitario.addUpdate("tab_reforma_grupo,eti_val_municipio");
		
		
	}	
	public void calcularReforma2(){
		eti_mensaje.setValue("");
		utilitario.addUpdate("eti_mensaje");
		
		if(tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(), "ide_hospitalarios")==null){
			
			eti_mensaje.setValue("<img src='imagenes/stop.png' /> No existe Fuente de Finaciamiento");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma2", "0");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"val_reformado2", "0");

			eti_val_hospitalario.setValue(utilitario.getFormatoNumero("0", 2));
			utilitario.addUpdate("tab_reforma_grupo,eti_val_hospitalario,eti_mensaje");
		}
		
		String valor_reforma = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"valor_reforma2");
		String valor_a_reformar = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"val_reformado2");
		double dou_reforma = pckUtilidades.CConversion.CDbl_2(valor_reforma);
		double dou_a_reformar = pckUtilidades.CConversion.CDbl_2(valor_a_reformar);
		double resultado=0;
		resultado = dou_reforma+dou_a_reformar;
		//System.out.println("valores a frefomrar "+dou_reforma+" cuanto reformo "+dou_a_reformar+" resultado "+resultado);
		tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma2", resultado+"");
		eti_val_hospitalario.setValue(utilitario.getFormatoNumero(tab_reforma_grupo.getSumaColumna("val_reformado2"), 2));
		sumaTotales();
		utilitario.addUpdate("tab_reforma_grupo,eti_val_hospitalario");
		
		
	}

	public void calcularReforma3(){
		
		eti_mensaje.setValue("");
		utilitario.addUpdate("eti_mensaje");
		
		if(tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(), "ide_tasa_recoleccion")==null){
			
			eti_mensaje.setValue("<img src='imagenes/stop.png' /> No existe Fuente de Finaciamiento");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma3", "0");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"val_reformado3", "0");

			eti_val_tasa_recoleccion.setValue(utilitario.getFormatoNumero("0", 2));
			utilitario.addUpdate("tab_reforma_grupo,eti_val_tasa_recoleccion,eti_mensaje");
		}
		
		String valor_reforma = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"valor_reforma3");
		String valor_a_reformar = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"val_reformado3");
		double dou_reforma = pckUtilidades.CConversion.CDbl_2(valor_reforma);
		double dou_a_reformar = pckUtilidades.CConversion.CDbl_2(valor_a_reformar);
		double resultado=0;
		resultado = dou_reforma+dou_a_reformar;
		//System.out.println("valores a frefomrar "+dou_reforma+" cuanto reformo "+dou_a_reformar+" resultado "+resultado);
		tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma3", resultado+"");
		eti_val_tasa_recoleccion.setValue(utilitario.getFormatoNumero(tab_reforma_grupo.getSumaColumna("val_reformado3"), 2));
		sumaTotales();
		utilitario.addUpdate("tab_reforma_grupo,eti_val_tasa_recoleccion");
		
		
	}
	public void calcularReforma4(){
		
		eti_mensaje.setValue("");
		utilitario.addUpdate("eti_mensaje");
		
		if(tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(), "ide_ruminahi")==null){
			
			eti_mensaje.setValue("<img src='imagenes/stop.png' /> No existe Fuente de Finaciamiento");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma4", "0");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"val_reformado4", "0");

			eti_val_ruminahi.setValue(utilitario.getFormatoNumero("0", 2));
			utilitario.addUpdate("tab_reforma_grupo,eti_val_ruminahi,eti_mensaje");
		}
		
		String valor_reforma = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"valor_reforma4");
		String valor_a_reformar = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"val_reformado4");
		double dou_reforma = pckUtilidades.CConversion.CDbl_2(valor_reforma);
		double dou_a_reformar = pckUtilidades.CConversion.CDbl_2(valor_a_reformar);
		double resultado=0;
		resultado = dou_reforma+dou_a_reformar;
		System.out.println("valores a frefomrar "+dou_reforma+" cuanto reformo "+dou_a_reformar+" resultado "+resultado);
		tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma4", resultado+"");
		eti_val_ruminahi.setValue(utilitario.getFormatoNumero(tab_reforma_grupo.getSumaColumna("val_reformado4"), 2));
		sumaTotales();
		utilitario.addUpdate("tab_reforma_grupo,eti_val_ruminahi");
		
		
	}
	public void calcularReforma5(){
		
		eti_mensaje.setValue("");
		utilitario.addUpdate("eti_mensaje");
		
		if(tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(), "ide_escombreras")==null){
			
			eti_mensaje.setValue("<img src='imagenes/stop.png' /> No existe Fuente de Finaciamiento");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma5", "0");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"val_reformado5", "0");

			eti_val_escombreras.setValue(utilitario.getFormatoNumero("0", 2));
			utilitario.addUpdate("tab_reforma_grupo,eti_val_escombreras,eti_mensaje");
		}
		
		String valor_reforma = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"valor_reforma5");
		String valor_a_reformar = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"val_reformado5");
		double dou_reforma = pckUtilidades.CConversion.CDbl_2(valor_reforma);
		double dou_a_reformar = pckUtilidades.CConversion.CDbl_2(valor_a_reformar);
		double resultado=0;
		resultado = dou_reforma+dou_a_reformar;
		System.out.println("valores a frefomrar "+dou_reforma+" cuanto reformo "+dou_a_reformar+" resultado "+resultado);
		tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma5", resultado+"");
		eti_val_escombreras.setValue(utilitario.getFormatoNumero(tab_reforma_grupo.getSumaColumna("val_reformado5"), 2));
		sumaTotales();
		utilitario.addUpdate("tab_reforma_grupo,eti_val_escombreras");
		
		
	}
	public void calcularReforma6(){
		
		eti_mensaje.setValue("");
		utilitario.addUpdate("eti_mensaje");
		
		if(tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(), "ide_metro_quito")==null){
			
			eti_mensaje.setValue("<img src='imagenes/stop.png' /> No existe Fuente de Finaciamiento");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma6", "0");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"val_reformado6", "0");

			eti_val_metro_quito.setValue(utilitario.getFormatoNumero("0", 2));
			utilitario.addUpdate("tab_reforma_grupo,eti_val_metro_quito,eti_mensaje");
		}
		
		String valor_reforma = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"valor_reforma6");
		String valor_a_reformar = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"val_reformado6");
		double dou_reforma = pckUtilidades.CConversion.CDbl_2(valor_reforma);
		double dou_a_reformar = pckUtilidades.CConversion.CDbl_2(valor_a_reformar);
		double resultado=0;
		resultado = dou_reforma+dou_a_reformar;
		//System.out.println("valores a frefomrar "+dou_reforma+" cuanto reformo "+dou_a_reformar+" resultado "+resultado);
		tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma6", resultado+"");
		eti_val_metro_quito.setValue(utilitario.getFormatoNumero(tab_reforma_grupo.getSumaColumna("val_reformado6"), 2));
		sumaTotales();
		utilitario.addUpdate("tab_reforma_grupo,eti_val_metro_quito");
		
		
	}
	public void calcularReforma7(){
		
		eti_mensaje.setValue("");
		utilitario.addUpdate("eti_mensaje");
		
		if(tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(), "ide_convenios")==null){
			
			eti_mensaje.setValue("<img src='imagenes/stop.png' /> No existe Fuente de Finaciamiento");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma7", "0");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"val_reformado7", "0");

			eti_val_convenios.setValue(utilitario.getFormatoNumero("0", 2));
			utilitario.addUpdate("tab_reforma_grupo,eti_val_convenios,eti_mensaje");
		}
		
		String valor_reforma = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"valor_reforma7");
		String valor_a_reformar = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"val_reformado7");
		double dou_reforma = pckUtilidades.CConversion.CDbl_2(valor_reforma);
		double dou_a_reformar = pckUtilidades.CConversion.CDbl_2(valor_a_reformar);
		double resultado=0;
		resultado = dou_reforma+dou_a_reformar;
		//System.out.println("valores a frefomrar "+dou_reforma+" cuanto reformo "+dou_a_reformar+" resultado "+resultado);
		tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma7", resultado+"");
		eti_val_convenios.setValue(utilitario.getFormatoNumero(tab_reforma_grupo.getSumaColumna("val_reformado7"), 2));
		sumaTotales();
		utilitario.addUpdate("tab_reforma_grupo,eti_val_convenios");
		
		
	}
	
	public void calcularReforma8(){
		
		eti_mensaje.setValue("");
		utilitario.addUpdate("eti_mensaje");
		
		if(tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(), "ide_otros_gestores")==null){
			
			eti_mensaje.setValue("<img src='imagenes/stop.png' /> No existe Fuente de Finaciamiento");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma8", "0");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"val_reformado8", "0");

			eti_val_otros_gestores.setValue(utilitario.getFormatoNumero("0", 2));
			utilitario.addUpdate("tab_reforma_grupo,eti_val_otros_gestores,eti_mensaje");
		}
		
		String valor_reforma = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"valor_reforma8");
		String valor_a_reformar = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"val_reformado8");
		double dou_reforma = pckUtilidades.CConversion.CDbl_2(valor_reforma);
		double dou_a_reformar = pckUtilidades.CConversion.CDbl_2(valor_a_reformar);
		double resultado=0;
		resultado = dou_reforma+dou_a_reformar;
		//System.out.println("valores a frefomrar "+dou_reforma+" cuanto reformo "+dou_a_reformar+" resultado "+resultado);
		tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma8", resultado+"");
		eti_val_otros_gestores.setValue(utilitario.getFormatoNumero(tab_reforma_grupo.getSumaColumna("val_reformado8"), 2));
		sumaTotales();
		utilitario.addUpdate("tab_reforma_grupo,eti_val_otros_gestores");
		
		
	}
	public void calcularReforma9(){
		
		eti_mensaje.setValue("");
		utilitario.addUpdate("eti_mensaje");
		
		if(tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(), "ide_saldo_bancos")==null){
			
			eti_mensaje.setValue("<img src='imagenes/stop.png' /> No existe Fuente de Finaciamiento");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma9", "0");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"val_reformado9", "0");

			eti_val_saldo_bancos.setValue(utilitario.getFormatoNumero("0", 2));
			utilitario.addUpdate("tab_reforma_grupo,eti_val_saldo_bancos,eti_mensaje");
		}
		
		
		String valor_reforma = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"valor_reforma9");
		String valor_a_reformar = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"val_reformado9");
		double dou_reforma = pckUtilidades.CConversion.CDbl_2(valor_reforma);
		double dou_a_reformar = pckUtilidades.CConversion.CDbl_2(valor_a_reformar);
		double resultado=0;
		resultado = dou_reforma+dou_a_reformar;
		//System.out.println("valores a frefomrar "+dou_reforma+" cuanto reformo "+dou_a_reformar+" resultado "+resultado);
		tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma9", resultado+"");
		eti_val_saldo_bancos.setValue(utilitario.getFormatoNumero(tab_reforma_grupo.getSumaColumna("val_reformado9"), 2));
		sumaTotales();
		utilitario.addUpdate("tab_reforma_grupo,eti_val_saldo_bancos");
		
		
	}
	public void calcularReforma10(){
		
		eti_mensaje.setValue("");
		utilitario.addUpdate("eti_mensaje");
		
		if(tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(), "ide_com_rsu_aprov")==null){
			
			eti_mensaje.setValue("<img src='imagenes/stop.png' /> No existe Fuente de Finaciamiento");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma10", "0");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"val_reformado10", "0");

			eti_val_com_rsu_aprov.setValue(utilitario.getFormatoNumero("0", 2));
			utilitario.addUpdate("tab_reforma_grupo,eti_val_com_rsu_aprov,eti_mensaje");
		}
		
		
		String valor_reforma = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"valor_reforma10");
		String valor_a_reformar = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"val_reformado10");
		double dou_reforma = pckUtilidades.CConversion.CDbl_2(valor_reforma);
		double dou_a_reformar = pckUtilidades.CConversion.CDbl_2(valor_a_reformar);
		double resultado=0;
		resultado = dou_reforma+dou_a_reformar;
		//System.out.println("valores a frefomrar "+dou_reforma+" cuanto reformo "+dou_a_reformar+" resultado "+resultado);
		tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma10", resultado+"");
		eti_val_com_rsu_aprov.setValue(utilitario.getFormatoNumero(tab_reforma_grupo.getSumaColumna("val_reformado10"), 2));
		sumaTotales();
		utilitario.addUpdate("tab_reforma_grupo,eti_val_com_rsu_aprov");
		
		
	}
	public void calcularReforma11(){
		
		eti_mensaje.setValue("");
		utilitario.addUpdate("eti_mensaje");
		
		if(tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(), "ide_tas_mu_ru_dic")==null){
			
			eti_mensaje.setValue("<img src='imagenes/stop.png' /> No existe Fuente de Finaciamiento");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma11", "0");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"val_reformado11", "0");

			eti_val_tas_mu_ru_dic.setValue(utilitario.getFormatoNumero("0", 2));
			utilitario.addUpdate("tab_reforma_grupo,eti_val_tas_mu_ru_dic,eti_mensaje");
		}
		
		String valor_reforma = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"valor_reforma11");
		String valor_a_reformar = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"val_reformado11");
		double dou_reforma = pckUtilidades.CConversion.CDbl_2(valor_reforma);
		double dou_a_reformar = pckUtilidades.CConversion.CDbl_2(valor_a_reformar);
		double resultado=0;
		resultado = dou_reforma+dou_a_reformar;
		//System.out.println("valores a frefomrar "+dou_reforma+" cuanto reformo "+dou_a_reformar+" resultado "+resultado);
		tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma11", resultado+"");
		eti_val_tas_mu_ru_dic.setValue(utilitario.getFormatoNumero(tab_reforma_grupo.getSumaColumna("val_reformado11"), 2));
		sumaTotales();
		utilitario.addUpdate("tab_reforma_grupo,eti_val_tas_mu_ru_dic");
		
		
	}
	public void calcularReforma12(){
		
		eti_mensaje.setValue("");
		utilitario.addUpdate("eti_mensaje");
		
		if(tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(), "ide_tas_rec_dic")==null){
			
			eti_mensaje.setValue("<img src='imagenes/stop.png' /> No existe Fuente de Finaciamiento");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma12", "0");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"val_reformado12", "0");

			eti_val_tas_rec_dic.setValue(utilitario.getFormatoNumero("0", 2));
			utilitario.addUpdate("tab_reforma_grupo,eti_val_tas_rec_dic,eti_mensaje");
		}
		
		
		String valor_reforma = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"valor_reforma12");
		String valor_a_reformar = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"val_reformado12");
		double dou_reforma = pckUtilidades.CConversion.CDbl_2(valor_reforma);
		double dou_a_reformar = pckUtilidades.CConversion.CDbl_2(valor_a_reformar);
		double resultado=0;
		resultado = dou_reforma+dou_a_reformar;
		//System.out.println("valores a frefomrar "+dou_reforma+" cuanto reformo "+dou_a_reformar+" resultado "+resultado);
		tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma12", resultado+"");
		eti_val_tas_rec_dic.setValue(utilitario.getFormatoNumero(tab_reforma_grupo.getSumaColumna("val_reformado12"), 2));
		sumaTotales();
		utilitario.addUpdate("tab_reforma_grupo,eti_val_tas_rec_dic");
		
		
	}
	public void calcularReforma13(){
		
		eti_mensaje.setValue("");
		utilitario.addUpdate("eti_mensaje");
		
		if(tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(), "ide_cue_x_cob_hosp")==null){
			
			eti_mensaje.setValue("<img src='imagenes/stop.png' /> No existe Fuente de Finaciamiento");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma13", "0");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"val_reformado13", "0");

			eti_val_cue_x_cob_hosp.setValue(utilitario.getFormatoNumero("0", 2));
			utilitario.addUpdate("tab_reforma_grupo,eti_val_cue_x_cob_hosp,eti_mensaje");
		}
		
		
		String valor_reforma = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"valor_reforma13");
		String valor_a_reformar = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"val_reformado13");
		double dou_reforma = pckUtilidades.CConversion.CDbl_2(valor_reforma);
		double dou_a_reformar = pckUtilidades.CConversion.CDbl_2(valor_a_reformar);
		double resultado=0;
		resultado = dou_reforma+dou_a_reformar;
		//System.out.println("valores a frefomrar "+dou_reforma+" cuanto reformo "+dou_a_reformar+" resultado "+resultado);
		tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma13", resultado+"");
		eti_val_cue_x_cob_hosp.setValue(utilitario.getFormatoNumero(tab_reforma_grupo.getSumaColumna("val_reformado13"), 2));
		sumaTotales();
		utilitario.addUpdate("tab_reforma_grupo,eti_val_cue_x_cob_hosp");
		
		
	}
	public void calcularReforma14(){
		
		eti_mensaje.setValue("");
		utilitario.addUpdate("eti_mensaje");
		
		if(tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(), "ide_anticipo_contractual")==null){
			
			eti_mensaje.setValue("<img src='imagenes/stop.png' /> No existe Fuente de Finaciamiento");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma14", "0");
			tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"val_reformado14", "0");

			eti_val_anticipo_contractual.setValue(utilitario.getFormatoNumero("0", 2));
			utilitario.addUpdate("tab_reforma_grupo,eti_val_anticipo_contractual,eti_mensaje");
		}
		
		
		String valor_reforma = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"valor_reforma14");
		String valor_a_reformar = tab_reforma_grupo.getValor(tab_reforma_grupo.getFilaActual(),"val_reformado14");
		double dou_reforma = pckUtilidades.CConversion.CDbl_2(valor_reforma);
		double dou_a_reformar = pckUtilidades.CConversion.CDbl_2(valor_a_reformar);
		double resultado=0;
		resultado = dou_reforma+dou_a_reformar;
		//System.out.println("valores a frefomrar "+dou_reforma+" cuanto reformo "+dou_a_reformar+" resultado "+resultado);
		tab_reforma_grupo.setValor(tab_reforma_grupo.getFilaActual(),"saldo_reforma14", resultado+"");
		eti_val_anticipo_contractual.setValue(utilitario.getFormatoNumero(tab_reforma_grupo.getSumaColumna("val_reformado14"), 2));
		sumaTotales();
		utilitario.addUpdate("tab_reforma_grupo,eti_val_anticipo_contractual");
		
		
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_reforma_grupo.insertar();
	}

	@Override
	public void guardar() {
		
		String ide_prpoa="";
		
		Double val_reforma_municipio;
		Double val_reforma_hospitalarios;
		Double val_reforma_tasa_recoleccion;
		Double val_reforma_ruminahi;
		Double val_reforma_escombreras;
		Double val_reforma_metro_quito;
		Double val_reforma_convenios;
		Double val_reforma_otros_gestores;
		Double val_reforma_saldo_bancos;
		Double val_reforma_com_rsu_aprov;
		Double val_reforma_tas_mu_ru_dic;
		Double val_reforma_tas_rec_dic;
		Double val_reforma_cue_x_cob_hosp;
		Double val_reforma_anticipo_contractual;
		
		// TODO Auto-generated method stub
		Double val_ide_municipio =tab_reforma_grupo.getSumaColumna("val_reformado1");
		Double val_ide_hospitalarios =tab_reforma_grupo.getSumaColumna("val_reformado2");
		Double val_ide_tasa_recoleccion =tab_reforma_grupo.getSumaColumna("val_reformado3");
		Double val_ide_ruminahi =tab_reforma_grupo.getSumaColumna("val_reformado4");
		Double val_ide_escombreras =tab_reforma_grupo.getSumaColumna("val_reformado5");
		Double val_ide_metro_quito =tab_reforma_grupo.getSumaColumna("val_reformado6");
		Double val_ide_convenios =tab_reforma_grupo.getSumaColumna("val_reformado7");
		Double val_ide_otros_gestores =tab_reforma_grupo.getSumaColumna("val_reformado8");
		Double val_ide_saldo_bancos =tab_reforma_grupo.getSumaColumna("val_reformado9");
		Double val_ide_com_rsu_aprov =tab_reforma_grupo.getSumaColumna("val_reformado10");
		Double val_ide_tas_mu_ru_dic =tab_reforma_grupo.getSumaColumna("val_reformado11");
		Double val_ide_tas_rec_dic =tab_reforma_grupo.getSumaColumna("val_reformado12");
		Double val_ide_cue_x_cob_hosp =tab_reforma_grupo.getSumaColumna("val_reformado13");
		Double val_ide_anticipo_contractual=tab_reforma_grupo.getSumaColumna("val_reformado14");
		
		Double val_total=val_ide_municipio + val_ide_hospitalarios + val_ide_tasa_recoleccion + val_ide_ruminahi + val_ide_escombreras + val_ide_metro_quito+
				val_ide_convenios + val_ide_otros_gestores+ val_ide_saldo_bancos + val_ide_com_rsu_aprov + val_ide_tas_mu_ru_dic + val_ide_tas_rec_dic + val_ide_cue_x_cob_hosp + val_ide_anticipo_contractual; 
		
		if(dia_datos_reforma.isVisible()){
			   // Inserto validando reforma por partida
				if(rad_imprimir.getValue().equals("1") ) {
					
					if(val_ide_municipio !=0) {
						utilitario.agregarMensajeError("No se puede Guardar", "Revizar la Reforma Municipio");
					}
					if(val_ide_hospitalarios !=0) {
						utilitario.agregarMensajeError("No se puede Guardar", "Revizar la Reforma Hospitalarios");
					}
					if(val_ide_tasa_recoleccion !=0) {
						utilitario.agregarMensajeError("No se puede Guardar", "Revizar la Reforma Tasa de Recolección");
					}
					if(val_ide_ruminahi !=0) {
						utilitario.agregarMensajeError("No se puede Guardar", "Revizar la Reforma Rumiñahui");
					}
					if(val_ide_escombreras !=0) {
						utilitario.agregarMensajeError("No se puede Guardar", "Revizar la Reforma Escombreras");
					}
					if(val_ide_metro_quito !=0) {
						utilitario.agregarMensajeError("No se puede Guardar", "Revizar la Reforma Metro de Quito");
					}
					if(val_ide_convenios !=0) {
						utilitario.agregarMensajeError("No se puede Guardar", "Revizar la Reforma Convenios");
					}
					if(val_ide_otros_gestores !=0) {
						utilitario.agregarMensajeError("No se puede Guardar", "Revizar la Reforma Otros Gestores");
					}
					if(val_ide_saldo_bancos !=0) {
						utilitario.agregarMensajeError("No se puede Guardar", "Revizar la Reforma Saldo Bancos");
					}
					if(val_ide_com_rsu_aprov !=0) {
						utilitario.agregarMensajeError("No se puede Guardar", "Revizar la Reforma Comercialización de RSU Aprovechables");
					}
					if(val_ide_tas_mu_ru_dic !=0) {
						utilitario.agregarMensajeError("No se puede Guardar", "Revizar la Reforma Municipio Rumiñahui Diciembre");
					}
					if(val_ide_tas_rec_dic !=0) {
						utilitario.agregarMensajeError("No se puede Guardar", "Revizar la Reforma Tasa de Recolección Diciembre");
					}
					if(val_ide_cue_x_cob_hosp !=0) {
						utilitario.agregarMensajeError("No se puede Guardar", "Revizar la Reforma Cuentas por cobrar Hospitalarios");
					}
					if(val_ide_anticipo_contractual !=0) {
						utilitario.agregarMensajeError("No se puede Guardar", "Revizar la Reforma Anticipo Contractual");
					}
					// GUARDO EL MUNICIPIO
					for(int i=0;i < tab_reforma_grupo.getTotalFilas();i++){
						
						ide_prpoa=tab_reforma_grupo.getValor(i, "pre_proa");
						val_reforma_municipio=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado1"));
						val_reforma_hospitalarios=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado2"));
						val_ide_tasa_recoleccion=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado3"));
						val_ide_ruminahi=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado4"));
						val_ide_escombreras=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado5"));
						val_ide_metro_quito=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado6"));
						val_ide_convenios=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado7"));
						val_ide_otros_gestores=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado8"));
						val_ide_saldo_bancos=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado9"));
						val_ide_com_rsu_aprov=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado10"));
						val_ide_tas_mu_ru_dic=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado11"));
						val_ide_tas_rec_dic=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado12"));
						val_ide_cue_x_cob_hosp=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado13"));
						val_ide_anticipo_contractual=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado14"));

						//System.out.println("ide_hospitalarios "+tab_reforma_grupo.getValor(i, "ide_hospitalarios" )+" valor hospitarios "+val_reforma_hospitalarios);

							if(tab_reforma_grupo.getValor(i, "ide_municipio") !=null && val_reforma_municipio !=0){			

								ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_reforma_municipio+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma1"), "true");	
							}
							else if(tab_reforma_grupo.getValor(i, "ide_hospitalarios" ) !=null && val_reforma_hospitalarios !=0){								

								ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_reforma_hospitalarios+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma2"), "true");	
							}
							else if(tab_reforma_grupo.getValor(i, "ide_tasa_recoleccion") !=null && val_ide_tasa_recoleccion !=0){								

								ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_tasa_recoleccion+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma3"), "true");	
							}
							else if(tab_reforma_grupo.getValor(i, "ide_ruminahi") !=null && val_ide_ruminahi !=0){								

								ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_ruminahi+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma4"), "true");	
							}
							else if(tab_reforma_grupo.getValor(i, "ide_escombreras") !=null && val_ide_escombreras !=0){								

								ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_escombreras+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma5"), "true");	
							}
							else if(tab_reforma_grupo.getValor(i, "ide_metro_quito") !=null && val_ide_metro_quito !=0){								

								ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_metro_quito+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma6"), "true");	
							}
							else if(tab_reforma_grupo.getValor(i, "ide_convenios") !=null && val_ide_convenios !=0){								

								ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_convenios+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma7"), "true");	
							}
							else if(tab_reforma_grupo.getValor(i, "ide_otros_gestores") !=null && val_ide_otros_gestores !=0){								

								ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_otros_gestores+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma8"), "true");	
							}
							else if(tab_reforma_grupo.getValor(i, "ide_saldo_bancos") !=null && val_ide_saldo_bancos !=0){								

								ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_saldo_bancos+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma9"), "true");	
							}
							else if(tab_reforma_grupo.getValor(i, "ide_com_rsu_aprov") !=null && val_ide_com_rsu_aprov !=0){								
								ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_com_rsu_aprov+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma10"), "true");	
							}
							else if(tab_reforma_grupo.getValor(i, "ide_tas_mu_ru_dic") !=null && val_ide_tas_mu_ru_dic !=0){								
								ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_tas_mu_ru_dic+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma11"), "true");	
							}
							else if(tab_reforma_grupo.getValor(i, "ide_tas_rec_dic") !=null && val_ide_tas_rec_dic !=0){								
								ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_tas_rec_dic+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma12"), "true");	
							}
							else if(tab_reforma_grupo.getValor(i, "ide_cue_x_cob_hosp") !=null && val_ide_cue_x_cob_hosp !=0){								
								ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_cue_x_cob_hosp+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma13"), "true");	
							}
							else if(tab_reforma_grupo.getValor(i, "ide_anticipo_contractual") !=null && val_ide_anticipo_contractual !=0){								
								ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_anticipo_contractual+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma14"), "true");	
							}
					}							
					
					
				}
			   // Inserto validando reforma por techos
				else if(rad_imprimir.getValue().equals("0")){
					
					if(val_total !=0){
						utilitario.agregarMensajeError("No se puede Realizar Reforma", "El saldo techo debe poseer una reforma de Cero");
					}
					else {
						 
						// GUARDO EL MUNICIPIO
						for(int i=0;i < tab_reforma_grupo.getTotalFilas();i++){
							
							ide_prpoa=tab_reforma_grupo.getValor(i, "pre_proa");
							val_reforma_municipio=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado1"));
							val_reforma_hospitalarios=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado2"));
							val_ide_tasa_recoleccion=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado3"));
							val_ide_ruminahi=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado4"));
							val_ide_escombreras=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado5"));
							val_ide_metro_quito=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado6"));
							val_ide_convenios=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado7"));
							val_ide_otros_gestores=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado8"));
							val_ide_saldo_bancos=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado9"));
							val_ide_com_rsu_aprov=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado10"));
							val_ide_tas_mu_ru_dic=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado11"));
							val_ide_tas_rec_dic=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado12"));
							val_ide_cue_x_cob_hosp=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado13"));
							val_ide_anticipo_contractual=pckUtilidades.CConversion.CDbl_2(tab_reforma_grupo.getValor(i, "val_reformado14"));

							//System.out.println("ide_hospitalarios "+tab_reforma_grupo.getValor(i, "ide_hospitalarios" )+" valor hospitarios "+val_reforma_hospitalarios);

								if(tab_reforma_grupo.getValor(i, "ide_municipio") !=null && val_reforma_municipio !=0){			

									ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_reforma_municipio+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma1"), "true");	
								}
								else if(tab_reforma_grupo.getValor(i, "ide_hospitalarios" ) !=null && val_reforma_hospitalarios !=0){								

									ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_reforma_hospitalarios+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma2"), "true");	
								}
								else if(tab_reforma_grupo.getValor(i, "ide_tasa_recoleccion") !=null && val_ide_tasa_recoleccion !=0){								

									ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_tasa_recoleccion+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma3"), "true");	
								}
								else if(tab_reforma_grupo.getValor(i, "ide_ruminahi") !=null && val_ide_ruminahi !=0){								

									ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_ruminahi+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma4"), "true");	
								}
								else if(tab_reforma_grupo.getValor(i, "ide_escombreras") !=null && val_ide_escombreras !=0){								

									ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_escombreras+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma5"), "true");	
								}
								else if(tab_reforma_grupo.getValor(i, "ide_metro_quito") !=null && val_ide_metro_quito !=0){								

									ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_metro_quito+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma6"), "true");	
								}
								else if(tab_reforma_grupo.getValor(i, "ide_convenios") !=null && val_ide_convenios !=0){								

									ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_convenios+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma7"), "true");	
								}
								else if(tab_reforma_grupo.getValor(i, "ide_otros_gestores") !=null && val_ide_otros_gestores !=0){								

									ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_otros_gestores+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma8"), "true");	
								}
								else if(tab_reforma_grupo.getValor(i, "ide_saldo_bancos") !=null && val_ide_saldo_bancos !=0){								

									ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_saldo_bancos+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma9"), "true");	
								}
								else if(tab_reforma_grupo.getValor(i, "ide_com_rsu_aprov") !=null && val_ide_com_rsu_aprov !=0){								
									ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_com_rsu_aprov+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma10"), "true");	
								}
								else if(tab_reforma_grupo.getValor(i, "ide_tas_mu_ru_dic") !=null && val_ide_tas_mu_ru_dic !=0){								
									ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_tas_mu_ru_dic+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma11"), "true");	
								}
								else if(tab_reforma_grupo.getValor(i, "ide_tas_rec_dic") !=null && val_ide_tas_rec_dic !=0){								
									ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_tas_rec_dic+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma12"), "true");	
								}
								else if(tab_reforma_grupo.getValor(i, "ide_cue_x_cob_hosp") !=null && val_ide_cue_x_cob_hosp !=0){								
									ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_cue_x_cob_hosp+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma13"), "true");	
								}
								else if(tab_reforma_grupo.getValor(i, "ide_anticipo_contractual") !=null && val_ide_anticipo_contractual !=0){								
									ser_presupuesto.actualizarSaldosReforma(ide_prpoa, val_ide_anticipo_contractual+"",  txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_reforma_grupo.getValor(i, "saldo_reforma14"), "true");	
								}
						}
					}
				}
				else {
					utilitario.agregarMensajeError("No se puede ejecutar Reforma", "La Reforma por Partida debe Generar un Valor 0.00");
					return;
					
				}

				dia_datos_reforma.cerrar();
				utilitario.agregarMensaje("REFORMADO", "Realizado correctamente la Reforma");
				actualizar();
			}
			else{
				dia_datos_reforma.dibujar();
			}
	       
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
	      tab_reforma_grupo.eliminar();
	}


	@Override
	public void actualizar() {
		eti_val_municipio.setValue("0.00");
	    eti_val_hospitalario.setValue("0.00");
	    eti_val_tasa_recoleccion.setValue("0.00");
	    eti_val_ruminahi.setValue("0.00");
	    eti_val_escombreras.setValue("0.00");
	    eti_val_metro_quito.setValue("0.00");
	    eti_val_convenios.setValue("0.00");
	    eti_val_otros_gestores.setValue("0.00");
	    eti_val_saldo_bancos.setValue("0.00");
	    eti_val_com_rsu_aprov.setValue("0.00");
	    eti_val_tas_mu_ru_dic.setValue("0.00");
	    eti_val_tas_rec_dic.setValue("0.00");
	    eti_val_cue_x_cob_hosp.setValue("0.00");
	    eti_val_anticipo_contractual.setValue("0.00");
		// TODO Auto-generated method stub
		utilitario.addUpdate("eti_val_municipio,eti_val_hospitalario,eti_val_tasa_recoleccion,eti_val_ruminahi,eti_val_escombreras,eti_val_metro_quito"
				+ ",eti_val_convenios,eti_val_otros_gestores,eti_val_saldo_bancos,eti_val_com_rsu_aprov,eti_val_tas_mu_ru_dic,eti_val_tas_rec_dic,eti_val_cue_x_cob_hosp,eti_val_anticipo_contractual");

		super.actualizar();
	}

	public SeleccionTabla getSet_poa() {
		return set_poa;
	}

	public void setSet_poa(SeleccionTabla set_poa) {
		this.set_poa = set_poa;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public Tabla getTab_reforma_grupo() {
		return tab_reforma_grupo;
	}

	public void setTab_reforma_grupo(Tabla tab_reforma_grupo) {
		this.tab_reforma_grupo = tab_reforma_grupo;
	}

	public Dialogo getDia_datos_reforma() {
		return dia_datos_reforma;
	}

	public void setDia_datos_reforma(Dialogo dia_datos_reforma) {
		this.dia_datos_reforma = dia_datos_reforma;
	}

	
}
