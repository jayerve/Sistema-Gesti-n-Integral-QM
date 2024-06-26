package paq_transporte;



import java.io.File;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.primefaces.event.SelectEvent;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_transporte.ejb.ServicioTransporte;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_consulta_veh_solicitudes extends Pantalla {
	
	private Tabla tab_vehiculos = new Tabla();
	private Tabla tab_solicitudes = new Tabla();
	
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();

	String str_fecha1=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
	String str_fecha2=utilitario.getFechaActual();
	
	
	File result; 
	WritableWorkbook archivo_xls ; 
	WritableSheet hojaPlantilla_xls; 
	WritableSheet hojaAux_xls; 
	
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioTransporte ser_transporte = (ServicioTransporte) utilitario.instanciarEJB(ServicioTransporte.class);
	
	public pre_consulta_veh_solicitudes() {
		
		bar_botones.limpiar();
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), 0));
		bar_botones.agregarComponente(cal_fecha_final);
		
		
		Boton bot_actualiza = new Boton();
		bot_actualiza.setId("bot_actualiza");
		bot_actualiza.setTitle("ACTUALIZAR");
		bot_actualiza.setValue("ACTUALIZAR");
		bot_actualiza.setMetodo("cambiaFecha");
		bar_botones.agregarBoton(bot_actualiza);
		
		Boton bot_excel=new Boton();
		bot_excel.setIcon("ui-icon-calculator");
  		bot_excel.setValue("Exportar Agendamientos");
  		bot_excel.setAjax(false);
  		bot_excel.setMetodo("exportarExcel");
  		bar_botones.agregarBoton(bot_excel); 
	
		tab_vehiculos.setId("tab_vehiculos");
		tab_vehiculos.setSql(ser_transporte.getVehiculos());
		tab_vehiculos.setLectura(true);
		tab_vehiculos.onSelect("VerAgenda");
		tab_vehiculos.dibujar();

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_vehiculos);

		tab_solicitudes.setId("tab_solicitudes");
		tab_solicitudes.setHeader("AGENDAMIENTOS");
		tab_solicitudes.setSql(ser_transporte.getAgendaVehiculo("-1",str_fecha1,str_fecha2));
		tab_solicitudes.setLectura(true);
		tab_solicitudes.dibujar();
		
		PanelTabla pat_solicitudes=new PanelTabla();
		pat_solicitudes.setPanelTabla(tab_solicitudes);
		
		Division div_tabla = new Division();
		div_tabla.dividir2(pat_panel,pat_solicitudes,"35%","H");
		agregarComponente(div_tabla);
		
		// TODO Auto-generated constructor stub
		
	}

	public void cambiaFecha()
	{
		str_fecha1=cal_fecha_inicial.getFecha();
		str_fecha2=cal_fecha_final.getFecha();
	}
	
	public void VerAgenda(SelectEvent evt)
	{
		tab_vehiculos.seleccionarFila(evt);
		tab_solicitudes.setSql(ser_transporte.getAgendaVehiculo(tab_vehiculos.getValor("ide_veveh"),str_fecha1,str_fecha2));
		tab_solicitudes.ejecutarSql();
		utilitario.addUpdate("tab_vehiculos,tab_solicitudes");
		utilitario.addUpdate("cal_fecha_inicial");
	}
	
	
	public void exportarExcel(){

		String nombre="agendamiento_vehicular.xls";
		
		try { 
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			Workbook archivo_xls2 = Workbook.getWorkbook(new File(utilitario.getPropiedad("rutaUpload")+"\\" + nombre)); 
			result = new File(extContext.getRealPath("/" + nombre)); 
			archivo_xls = Workbook.createWorkbook(result, archivo_xls2); 

			int columna=0;
			String fechaSiguiente=str_fecha1;
			int incrementoDia=1;
			String nombreHoja="";
			
			do {
				columna=0;
				nombreHoja=utilitario.getFechaLarga(fechaSiguiente);
				hojaPlantilla_xls = archivo_xls.getSheet(incrementoDia-1);
				for(int indice=0;indice<tab_vehiculos.getTotalFilas();indice++){
					//System.out.println("Hojaindice :" +indice);
					columna=columna+(columna==0?1:3);
					construyeAgendamientoCab(columna,indice, tab_vehiculos.getValor(indice,"conductor"),
							tab_vehiculos.getValor(indice,"vehiculo"),
							tab_vehiculos.getValor(indice,"placa_veveh"));
					
					//System.out.println("nombreHoja :" +nombreHoja);
					construyeAgendamiento(tab_vehiculos.getValor(indice,"ide_veveh"),columna,fechaSiguiente);
				}
				jxl.write.Label lab2 = new jxl.write.Label(0, 2,"("+nombreHoja+")", hojaPlantilla_xls.getCell(columna, 3).getCellFormat());
				hojaPlantilla_xls.addCell(lab2);
				hojaPlantilla_xls.setName(nombreHoja);
				
				System.out.println("fechaSiguiente :" +fechaSiguiente+ " - str_fecha2: "+str_fecha2);
				
				if(fechaSiguiente.equals(str_fecha2))
					break;
				
				fechaSiguiente = utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(str_fecha1), incrementoDia));
				archivo_xls.importSheet("Copia", incrementoDia, archivo_xls2.getSheet(0));
				incrementoDia++;
				
	        } while (incrementoDia<=15);
			
			if(incrementoDia>15)
				utilitario.agregarMensajeInfo("Limite Maximo superado", "Solo se permite exportar hasta maximo 15 dias");
	
			archivo_xls.write(); 
			archivo_xls.close(); 
			//archivo_xls2.close();
			
			FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() + "/" + nombre);
		} catch (Exception e) { 
			System.out.println("Error no se genero el XLS :" + e.getMessage()); 
		} 
		

	}
	
	private void construyeAgendamientoCab(int columna, int indice, String conductor, String vehiculo, String placa ) throws RowsExceededException, WriteException
	{
		jxl.write.Label lab2;
		

		lab2 = new jxl.write.Label(columna, 3,(indice+1)+"", hojaPlantilla_xls.getCell(columna, 3).getCellFormat());
		hojaPlantilla_xls.addCell(lab2);
		
		lab2 = new jxl.write.Label(columna, 4,conductor, hojaPlantilla_xls.getCell(columna, 4).getCellFormat());
		hojaPlantilla_xls.addCell(lab2);
		
		lab2 = new jxl.write.Label(columna, 5,vehiculo, hojaPlantilla_xls.getCell(columna, 5).getCellFormat());
		hojaPlantilla_xls.addCell(lab2);
		
		lab2 = new jxl.write.Label(columna, 6,placa, hojaPlantilla_xls.getCell(columna, 6).getCellFormat());
		hojaPlantilla_xls.addCell(lab2);
		
	}
	
	private void construyeAgendamiento(String ide_veveh, int columna, String fecha) throws RowsExceededException, WriteException
	{
		jxl.write.Label lab2;
		int fila=8;
		int filaFin=8;
		String ruta="";
		//tab_solicitudes.setSql(ser_transporte.getAgendaVehiculo(ide_veveh,fecha,utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecha), 1))));
		tab_solicitudes.setSql(ser_transporte.getAgendaVehiculoRangos(ide_veveh,fecha,fecha));
		tab_solicitudes.ejecutarSql();
		
		for (int i = 0; i < tab_solicitudes.getTotalFilas(); i++) 
		{
			fila=encuentraFilaHora(fila,tab_solicitudes.getValor(i, "hora_salida_vesol"));
			filaFin=encuentraFilaHora(fila,tab_solicitudes.getValor(i, "hora_retorno_vesol"));
			
			if(fila<filaFin)
			{
				try
				{
					if(UpperLowerHour(pckUtilidades.CConversion.CStr(tab_solicitudes.getValor(i, "hora_retorno_vesol"))).contains(
							UpperLowerHour(pckUtilidades.CConversion.CStr(tab_solicitudes.getValor(((i+1) >= tab_solicitudes.getTotalFilas()?i:(i+1)), "hora_salida_vesol")))))
					{
						filaFin--;
					}
				}
				catch(Exception ex)
				{
					System.out.println("Error construyeAgendamiento1 "+ex.getMessage());
				}

				hojaPlantilla_xls.mergeCells(columna, fila, columna, filaFin);
				//hojaPlantilla_xls.mergeCells(columna+1, fila, columna+1, filaFin);
			}
			
			WritableCellFormat formato_celda = new WritableCellFormat(hojaPlantilla_xls.getCell(columna, fila).getCellFormat()); 
			formato_celda.setBackground(Colour.GREY_25_PERCENT);
			
			WritableCellFormat formato_celda2 = new WritableCellFormat(hojaPlantilla_xls.getCell(columna+1, fila).getCellFormat()); 
			formato_celda2.setBackground(Colour.SEA_GREEN);
			
			ruta=pckUtilidades.CConversion.CStr(tab_solicitudes.getValor(i, "detalle_verut"));
			if(ruta.length()<=1)
			{
				String motivo=pckUtilidades.CConversion.CStr(tab_solicitudes.getValor(i, "motivo_vesol"));
				ruta=motivo.length()>=50 ? motivo.substring(0, 50) : motivo.substring(0, motivo.length());
			}
			
			lab2 = new jxl.write.Label(columna, fila,ruta, formato_celda);
			hojaPlantilla_xls.addCell(lab2);
			
			lab2 = new jxl.write.Label(columna+1, fila,tab_solicitudes.getValor(i, "FUNCIONARIO"), formato_celda2);
			hojaPlantilla_xls.addCell(lab2);
			
			//System.out.println("Fecha "+fecha);
			//System.out.println("Valor Celda "+hojaPlantilla_xls.getCell(columna+1, fila).getContents());

			
			/*lab2 = new jxl.write.Label(1, i+1,pckUtilidades.Utilitario.quitarCaracteresSpeciales(pckUtilidades.Utilitario.quitarAcentos(tab_ats_informante.getValor(i, "Razón Social")).replace(".", "")), formato_celda);
			hojaInformante_xls.addCell(lab2);*/

			fila++;
		}
		
		
	}
	
	private int encuentraFilaHora(int filaStart, String hora)
	{
		int fila=filaStart;
		String readCell="";
		String [] aux;
		hora = UpperLowerHour(hora);
		for(int i=filaStart; i<=39; i++)
		{
			aux=hojaPlantilla_xls.getCell(0, i).getContents().toString().split(":");
			readCell = pckUtilidades.Utilitario.padLeft(aux[0], 2)+":"+ pckUtilidades.Utilitario.padLeft(aux[1], 2);
			
			if(hora.contains(readCell))
			{
				fila=i;
				break;
			}
		}
		
		return fila;
	}
	
	private String UpperLowerHour(String hora)
	{
		String [] aux=hora.split(":");
		int horaAux=pckUtilidades.CConversion.CInt(aux[1]);
		
		if(horaAux > 0 && horaAux<30)
			horaAux=0;
		
		if(horaAux > 30 && horaAux<60)
			horaAux=30;
		
		hora = aux[0]+":"+ pckUtilidades.Utilitario.padLeft(horaAux+"", 2);
		
		return hora;
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


	public Tabla getTab_vehiculos() {
		return tab_vehiculos;
	}


	public void setTab_vehiculos(Tabla tab_vehiculos) {
		this.tab_vehiculos = tab_vehiculos;
	}


	public Tabla getTab_solicitudes() {
		return tab_solicitudes;
	}


	public void setTab_solicitudes(Tabla tab_solicitudes) {
		this.tab_solicitudes = tab_solicitudes;
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
