package paq_sumillas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.VisualizarPDF;

public class pre_rep_sumillas_coordinacion extends Pantalla{
	
	private Combo com_anio = new Combo();
	private Combo com_mes_ini = new Combo();
	private Combo com_gerencia = new Combo();
    private VisualizarPDF vpdf_pago = new VisualizarPDF();
    public static String par_modulosec_recaudacion;
    double valor_pendiente=0;

	/*@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	*/

	public pre_rep_sumillas_coordinacion(){
		
		//par_modulosec_recaudacion=utilitario.getVariable("p_modulo_secuencial_recaudacion");
		bar_botones.limpiar();
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		
		/*Etiqueta eti_colaborador=new Etiqueta("CLIENTE:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarBoton(bot_limpiar);*/
		
		com_anio.setCombo("select distinct anio id_anio,anio from rep_sum_tramite_mes_anio order by anio");
		com_anio.setValue(1);
		//com_anio.setMetodo("actualizaMes");
		com_anio.setStyle("width: 200px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta(" Año:"));
		bar_botones.agregarComponente(com_anio);
		
		com_mes_ini.setCombo(getListaMeses());
		com_mes_ini.setValue(1);
		com_mes_ini.setMetodo("actualizargerencias");
		com_mes_ini.setStyle("width: 200px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta(" Mes:"));
		bar_botones.agregarComponente(com_mes_ini);
		
		com_gerencia.setCombo("select distinct ide_geareg,gerencia_acr from rep_sum_tramite_mes_anio order by gerencia_acr");
		com_gerencia.setValue(1);
		com_gerencia.setId("com_gerencia");
		//com_gerencia.setMetodo("actualizaMes");
		com_gerencia.setStyle("width: 200px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta(" Gerencia:"));
		bar_botones.agregarComponente(com_gerencia);
		
		Boton bot_reimprimir=new Boton();
		bot_reimprimir.setIcon("ui-icon-image"); //mas iconos https://api.jqueryui.com/theming/icons/
		bot_reimprimir.setValue("Gráfico");
		bot_reimprimir.setMetodo("reimprime");
		bar_botones.agregarBoton(bot_reimprimir);
		
		Boton bot_reptabular=new Boton();
		bot_reptabular.setIcon("ui-icon-contact"); //mas iconos https://api.jqueryui.com/theming/icons/
		bot_reptabular.setValue("Tabla");
		bot_reptabular.setMetodo("reptabular");
		bar_botones.agregarBoton(bot_reptabular);
		
		//REPORTE
        vpdf_pago.setId("vpdf_pago");
        vpdf_pago.setTitle("Reporte");
        agregarComponente(vpdf_pago);
        
		
	}
	
	public List getListaMeses() {
		//-- Este campo me indica el tipo de flujo si es debito(1) o credito (2)
		List lista = new ArrayList();
		Object fila1[] = {"1", "Enero"};
        Object fila2[] = {"2", "Febrero"};
        Object fila3[] = {"3", "Marzo"};
        Object fila4[] = {"4", "Abril"};
        Object fila5[] = {"5", "Mayo"};
        Object fila6[] = {"6", "Junio"};
        Object fila7[] = {"7", "Julio"};
        Object fila8[] = {"8", "Agosto"};
        Object fila9[] = {"9", "Septiembre"};
        Object fila10[] = {"10", "Octubre"};
        Object fila11[] = {"11", "Noviembre"};
        Object fila12[] = {"12", "Diciembre"};
        
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		lista.add(fila4);
		lista.add(fila5);
		lista.add(fila6);
		lista.add(fila7);
		lista.add(fila8);
		lista.add(fila9);
		lista.add(fila10);
		lista.add(fila11);
		lista.add(fila12);

		return lista;
	}
	
	public void actualizargerencias(){
		com_gerencia.setCombo("select distinct ide_geareg,gerencia_acr from rep_sum_tramite_mes_anio order by gerencia_acr");
		com_gerencia.setValue(1);
		//com_gerencia.setMetodo("actualizaMes");
		com_gerencia.setStyle("width: 200px; margin: 0 0 -8px 0;");
		utilitario.addUpdate("com_gerencia");
		
	}
	
	public void reimprime()
	{
		Locale locale=new Locale("es","ES");
		//double valorPendiente=pckUtilidades.CConversion.CDbl_2(txt_valor_entregado.getValue());
		
		//AQUI ABRE EL REPORTE
        Map p_parametros = new HashMap();
        p_parametros.put("p_anio",pckUtilidades.CConversion.CInt(com_anio.getValue()));
        p_parametros.put("p_mes_ini",pckUtilidades.CConversion.CInt(com_mes_ini.getValue()));
        p_parametros.put("p_gerencia",pckUtilidades.CConversion.CInt(com_gerencia.getValue()));
        p_parametros.put("REPORT_LOCALE", locale);
        vpdf_pago.setVisualizarPDF("rep_sumillas/rep_sumillas_mes_anio_coordinacion.jasper", p_parametros);
        vpdf_pago.dibujar();
        utilitario.addUpdate("vpdf_pago");
	}
	
	public void reptabular()
	{
		Locale locale=new Locale("es","ES");
		//double valorPendiente=pckUtilidades.CConversion.CDbl_2(txt_valor_entregado.getValue());
		
		//AQUI ABRE EL REPORTE
        Map p_parametros = new HashMap();
        p_parametros.put("p_anio",pckUtilidades.CConversion.CInt(com_anio.getValue()));
        p_parametros.put("p_mes_ini",pckUtilidades.CConversion.CInt(com_mes_ini.getValue()));
        p_parametros.put("p_gerencia",pckUtilidades.CConversion.CInt(com_gerencia.getValue()));
        p_parametros.put("REPORT_LOCALE", locale);
        vpdf_pago.setVisualizarPDF("rep_sumillas/rep_sumillas_mes_anio_coordinacion_tbl.jasper", p_parametros);
        vpdf_pago.dibujar();
        utilitario.addUpdate("vpdf_pago");
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

	public VisualizarPDF getVpdf_pago() {
		return vpdf_pago;
	}

	public void setVpdf_pago(VisualizarPDF vpdf_pago) {
		this.vpdf_pago = vpdf_pago;
	}

	public static String getPar_modulosec_recaudacion() {
		return par_modulosec_recaudacion;
	}

	public static void setPar_modulosec_recaudacion(String par_modulosec_recaudacion) {
		pre_rep_sumillas_coordinacion.par_modulosec_recaudacion = par_modulosec_recaudacion;
	}

	public double getValor_pendiente() {
		return valor_pendiente;
	}

	public void setValor_pendiente(double valor_pendiente) {
		this.valor_pendiente = valor_pendiente;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public Combo getCom_mes_ini() {
		return com_mes_ini;
	}

	public void setCom_mes_ini(Combo com_mes_ini) {
		this.com_mes_ini = com_mes_ini;
	}

	public Combo getCom_gerencia() {
		return com_gerencia;
	}

	public void setCom_gerencia(Combo com_gerencia) {
		this.com_gerencia = com_gerencia;
	}
	
	
	
}
