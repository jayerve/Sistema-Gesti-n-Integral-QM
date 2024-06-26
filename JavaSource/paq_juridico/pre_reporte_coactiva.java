package paq_juridico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_juridico.ejb.ServicioJuridico;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;

public class pre_reporte_coactiva extends Pantalla {

	private AutoCompletar aut_cliente = new AutoCompletar();
	private Check che_todos_emp=new Check();
	
	private Tabla tab_coactiva = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioJuridico ser_juridico = (ServicioJuridico ) utilitario.instanciarEJB(ServicioJuridico.class);

	public pre_reporte_coactiva() {
		// TODO Auto-generated constructor stub
		
		bar_botones.limpiar();
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		
		aut_cliente.setId("aut_cliente");
		aut_cliente.setAutoCompletar(ser_facturacion.getClientesDatosBasicos("0,1"));
		Etiqueta eti_colaborador=new Etiqueta("CLIENTE:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_cliente);
		bar_botones.agregarBoton(bot_limpiar);
		
		che_todos_emp.setId("che_todos_emp");
		Etiqueta eti_todos_emp=new Etiqueta("Todos los Clientes");
		bar_botones.agregarComponente(eti_todos_emp);
		bar_botones.agregarComponente(che_todos_emp);
		
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
		bot_actualiza.setMetodo("cargaReporte");
		bar_botones.agregarBoton(bot_actualiza);
		
		Boton bot_generar= new Boton();
  		bot_generar.setIcon("ui-icon-folder-collapsed");
		bot_generar.setValue("Exportar Expediente");
		bot_generar.setMetodo("generarArchivo");
		bot_generar.setAjax(false);
		bar_botones.agregarBoton(bot_generar);
		
		tab_coactiva.setId("tab_coactiva");
		tab_coactiva.setSql(ser_juridico.getSqlCoactivas("-1","0", "1900-01-01","1900-01-01"));
		//tab_coactiva.getColumna("activo_juepr").setValorDefecto("true");
		tab_coactiva.setRows(25);
		tab_coactiva.setLectura(true);
		tab_coactiva.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_coactiva);
        Division div_division = new Division();

        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);

	}
	
	public void limpiar(){
		aut_cliente.limpiar();
		tab_coactiva.limpiar();
		
		tab_coactiva.setSql(ser_juridico.getSqlCoactivas("-1","0", "1900-01-01","1900-01-01"));
		tab_coactiva.ejecutarSql();
		utilitario.addUpdate("tab_coactiva,aut_cliente");
	}
	
	public void cargaReporte(){
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		
		if(che_todos_emp.getValue().toString().equalsIgnoreCase("true")){
			tab_coactiva.setSql(ser_juridico.getSqlCoactivas("-1", "1",fecha_inicial,fecha_final));
			tab_coactiva.ejecutarSql();
			utilitario.addUpdate("tab_coactiva");
		}
		else{
			if(aut_cliente.getValor()!=null)
			{
				tab_coactiva.setSql(ser_juridico.getSqlCoactivas(aut_cliente.getValor(), "0",fecha_inicial,fecha_final));
				tab_coactiva.ejecutarSql();
				utilitario.addUpdate("tab_coactiva");
			}
			else{
				utilitario.agregarMensajeInfo("Selecione un cliente", "");
			}
				
		}
	}
	
	public void generarArchivo(){	
		try {
			
			if(tab_coactiva.getTotalFilas()<=0)
			{
				utilitario.agregarMensajeInfo("Selecione un Proceso", "");
				return;
			}

			TablaGenerica tab_archivos=utilitario.consultar("SELECT ide_jucar, descripcion_jucar, adjunto_jucar, activo_jucar "
					+ " FROM jur_coactiva_archivo where length(coalesce(adjunto_jucar,''))>4 and ide_jucoa= "+pckUtilidades.CConversion.CInt(tab_coactiva.getValor("codigo"))
					+ " order by 1 ");
			
			if(tab_archivos.getTotalFilas()<=0)
			{
				utilitario.agregarMensajeInfo("SIN ARCHIVOS DEL PROCESO", "");
				return;
			}
			
			String fileName = tab_coactiva.getValor("ruc_comercial_recli")+"_"+tab_coactiva.getValor("nro_proceso");
			List<File> anexosPrincipales=new ArrayList();
			int item=1;
			for(int i=0;i<tab_archivos.getTotalFilas();i++)
			{
				String ruta=utilitario.getPropiedad("rutaDownload")+(tab_archivos.getValor(i, "adjunto_jucar"));	
				String nombre=pckUtilidades.CConversion.CStr(tab_archivos.getValor(i, "descripcion_jucar"));
	
				if(nombre.length()<4)
				{
					nombre="SinNombre"+i;
				}
	
				try{
					File anexoFil=utilitario.descargarArchivo(ruta, item+"_" +pckUtilidades.Utilitario.quitarCaracteresSpeciales(nombre), ruta.substring(ruta.length()-5, ruta.length()));
					if(anexoFil!=null)
						anexosPrincipales.add(anexoFil);
					}catch(Exception e){}
				item++;
				
			}

			utilitario.crearArchivoZIP(anexosPrincipales, fileName.concat(".zip"));		
			
			
		} catch (Exception e) {
			// TODO: handle exception
			utilitario.crearError("PROCESOS", "generarArchivo", e);
		}
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

	public AutoCompletar getAut_cliente() {
		return aut_cliente;
	}

	public void setAut_cliente(AutoCompletar aut_cliente) {
		this.aut_cliente = aut_cliente;
	}

	public Check getChe_todos_emp() {
		return che_todos_emp;
	}

	public void setChe_todos_emp(Check che_todos_emp) {
		this.che_todos_emp = che_todos_emp;
	}

	public Tabla getTab_coactiva() {
		return tab_coactiva;
	}

	public void setTab_coactiva(Tabla tab_coactiva) {
		this.tab_coactiva = tab_coactiva;
	}

	public Calendario getCal_fecha_inicial() {
		return cal_fecha_inicial;
	}

	public void setCal_fecha_inicial(Calendario cal_fecha_inicial) {
		this.cal_fecha_inicial = cal_fecha_inicial;
	}

	public Calendario getCal_fecha_final() {
		return cal_fecha_final;
	}

	public void setCal_fecha_final(Calendario cal_fecha_final) {
		this.cal_fecha_final = cal_fecha_final;
	}

	
	
}
