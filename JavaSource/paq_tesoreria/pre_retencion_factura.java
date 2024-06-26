package paq_tesoreria;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.primefaces.event.FileUploadEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Upload;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;

public class pre_retencion_factura extends Pantalla {


	private Tabla tab_retencion =new Tabla();
    private Tabla tab_detalle_retencion=new Tabla();
    
    private Combo com_anio=new Combo();
    private Dialogo dialogo = new Dialogo();
	private Upload upl_archivo = new Upload();
	
    private String carpeta="RetencionesClientes";
    
    
    @EJB
    private ServicioTesoreria ser_Tesoreria = (ServicioTesoreria) utilitario.instanciarEJB(ServicioTesoreria.class);
    @EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
    @EJB
	private ServicioFacturacion ser_Facturacion = (ServicioFacturacion) utilitario.instanciarEJB(ServicioFacturacion.class);

    public pre_retencion_factura(){
    	
    	bar_botones.getBot_insertar().setRendered(false);
		bar_botones.getBot_eliminar().setRendered(false);
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("AÑO:"));
		bar_botones.agregarComponente(com_anio);
		
		Boton bot_cargar_xml = new Boton();
		bot_cargar_xml.setValue("Importar XML");
		bot_cargar_xml.setTitle("IMPORTAR XML");
		bot_cargar_xml.setIcon("ui-icon-person");
		bot_cargar_xml.setMetodo("importarArchivo");
		bar_botones.agregarBoton(bot_cargar_xml);
		
		
		///RETENCION
        tab_retencion.setId("tab_retencion");
        tab_retencion.setTabla("fac_retencion", "ide_faret", 1);
        tab_retencion.getColumna("xml_faret").setUpload(carpeta);
        tab_retencion.getColumna("pdf_faret").setUpload(carpeta);
        tab_retencion.getColumna("total_ret_faret").setEtiqueta();
        tab_retencion.getColumna("total_ret_faret").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
        tab_retencion.getColumna("total_ret_faret").setValorDefecto("0.00");
        tab_retencion.getColumna("activo_faret").setValorDefecto("true");
        tab_retencion.getColumna("activo_faret").setLectura(true);
        tab_retencion.getColumna("ide_fafac").setCombo(ser_Facturacion.getClientesFactura("2,24,30"));
        tab_retencion.getColumna("ide_fafac").setAutoCompletar();
        tab_retencion.getColumna("ide_fafac").setLectura(true);
        tab_retencion.getColumna("fecha_faret").setValorDefecto(utilitario.getFechaActual());
        tab_retencion.getColumna("ide_coest").setValorDefecto("2");
        tab_retencion.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
        tab_retencion.setCondicion("ide_faret=-1");
        
        tab_retencion.agregarRelacion(tab_detalle_retencion);
        tab_retencion.setTipoFormulario(true);
        tab_retencion.getGrid().setColumns(4);
        tab_retencion.dibujar();
        
        PanelTabla pat_retencion =new PanelTabla();
        pat_retencion.setPanelTabla(tab_retencion);

        Etiqueta eti_retencion=new Etiqueta(); 
        eti_retencion.setValue("RETENCION ELECTRÓNICA - CLIENTE");
        eti_retencion.setStyle("font-size: 13px;color: red;font-weight: bold");
        pat_retencion.setHeader(eti_retencion);
        
        ///DETALLE RETENCION
        tab_detalle_retencion.setId("tab_detalle_retencion");
        tab_detalle_retencion.setTabla("fac_detalle_retencion", "ide_fader", 2);
        tab_detalle_retencion.getColumna("ide_teimp").setCombo("tes_impuesto", "ide_teimp", "codigo_teimp,porcentaje_teimp,detalle_teimp", "");
        tab_detalle_retencion.getColumna("ide_teimp").setLectura(true);
        tab_detalle_retencion.getColumna("ide_teimp").setAutoCompletar();
        tab_detalle_retencion.getColumna("base_imponible_fader").setValorDefecto("0.00");
        tab_detalle_retencion.getColumna("base_imponible_fader").setMetodoChange("recalcular");
        tab_detalle_retencion.getColumna("valor_retenido_fader").setEtiqueta();
        tab_detalle_retencion.getColumna("valor_retenido_fader").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
        tab_detalle_retencion.getColumna("valor_retenido_fader").setValorDefecto("0.00");
        tab_detalle_retencion.getColumna("activo_fader").setValorDefecto("true");
        tab_detalle_retencion.getColumna("activo_fader").setLectura(true);
        tab_detalle_retencion.setTipoFormulario(true);
        tab_detalle_retencion.getGrid().setColumns(4);
        tab_detalle_retencion.dibujar();
        PanelTabla pat_detalle_retencion=new PanelTabla();
        pat_detalle_retencion.setPanelTabla(tab_detalle_retencion);
        
        ////para obteber las dos ventanas retencion y detalla retención
        Etiqueta eti_detalle_retencion=new Etiqueta(); 
        eti_detalle_retencion.setValue("DETALLE RETENCION");
        eti_detalle_retencion.setStyle("font-size: 13px;color: red;font-weight: bold");
        pat_detalle_retencion.setHeader(eti_detalle_retencion);
        
        Division div_division =new Division();
        div_division.dividir2( pat_retencion, pat_detalle_retencion, "45%", "H");
        agregarComponente(div_division);
        
        
		
		dialogo.setId("dialogo");
		dialogo.setTitle("Cargue el Archivo XML - Retención Electrónica");
		dialogo.setWidth("40%");
		dialogo.setHeight("30%");
		dialogo.setDialogo(upl_archivo);
		dialogo.setFooter("Este proceso creara una nueva retención");
		//dialogo.getBot_aceptar().setMetodo("aceptarImportacion");
		dialogo.getBot_aceptar().setRendered(false);
		agregarComponente(dialogo);

		upl_archivo.setId("upl_archivo");
		upl_archivo.setMetodo("validarArchivo");
		upl_archivo.setAuto(false);
		upl_archivo.setAllowTypes("/(\\.|\\/)(xml|XML)$/");
		upl_archivo.setUploadLabel("Cargar archivo");
		upl_archivo.setCancelLabel("Cancelar Seleccion");

     }
    
    public void seleccionaElAnio (){

    	tab_retencion.setCondicion(" extract(year from fecha_faret)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") ");
    	tab_retencion.ejecutarSql();
    	tab_detalle_retencion.ejecutarValorForanea(tab_retencion.getValorSeleccionado());
	}
    
    public void recalcular(AjaxBehaviorEvent evt){
   		tab_detalle_retencion.modificar(evt);
        TablaGenerica tab_rentas= utilitario.consultar(ser_Tesoreria.getImpuestoCalculo(tab_detalle_retencion.getValor("ide_teimp")));

		double dou_valor_impuesto=0;
        double dou_porcentaje_calculo=0;
        double dou_valor_resultado=0;

        dou_porcentaje_calculo=pckUtilidades.CConversion.CDbl_2(tab_rentas.getValor("porcentaje_teimp"));
        dou_valor_impuesto=pckUtilidades.CConversion.CDbl_2(tab_detalle_retencion.getValor("base_imponible_fader"));
        dou_valor_resultado=pckUtilidades.CConversion.CDbl_2((dou_porcentaje_calculo*dou_valor_impuesto)/100);
  
        tab_detalle_retencion.setValor("valor_retenido_fader",utilitario.getFormatoNumero( dou_valor_resultado,2)+"");   
        String valorx=tab_detalle_retencion.getSumaColumna("valor_retenido_fader")+"";
        tab_retencion.setValor("total_ret_faret", utilitario.getFormatoNumero(valorx,2));   
        tab_retencion.modificar(tab_retencion.getFilaActual());
        utilitario.addUpdateTabla(tab_detalle_retencion, "valor_retenido_fader,base_imponible_fader,ide_teimp","");
        utilitario.addUpdateTabla(tab_retencion, "total_ret_faret",""); 
    }  
    
    public void importarArchivo() {
		/*if (!verificarSiSePuedeEditarFactura()) {
			return;
		}*/
    	
    	if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("No se puede importar el Archivo", "Favor Seleccione un Año");
			return;
		}
    	
		dialogo.dibujar();
	}
    
    /*public void aceptarImportacion() {
		dialogo.cerrar();
	}*/
    
    public void validarArchivo(FileUploadEvent evt) {
	
		String fecha_emision = null;
		String establecimiento = null;
		String ptoEmi = null;
		String secuencial = null;
		String secuencial_full=null;
		String numeroAutorizacion=null;
		
		dialogo.cerrar();
		
		try {
			// tab_adq_factura.modificar(evt);		
			
			if(evt.getFile().getInputstream()==null)
			{
				utilitario.agregarMensaje("No se pudo cargar", "No se cargo el XML");
				System.out.println("Error No se pudo cargar: No se cargo el XML: "+evt.getFile().getFileName());
				return;
			}
			
			File fil_xml = pckUtilidades.Utilitario.array64ConvertToFile(pckUtilidades.Utilitario.inputStreamConvertToArray64(evt.getFile().getInputstream()));

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = null;
			InputStream inputStream = null;
			inputStream = evt.getFile().getInputstream();
			try {
				InputSource is = new InputSource(inputStream);
				is.setEncoding("UTF-8");
				doc = db.parse(is);
			} catch (org.xml.sax.SAXParseException e) {
				// TODO Auto-generated catch block
				System.out.println("Error al leer con UTF-8");

				InputSource is = new InputSource(inputStream);
				is.setEncoding("ISO-8859-1");
				doc = db.parse(is);
			}

			doc.getDocumentElement().normalize();

			//System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

			if (doc.getElementsByTagName("estado").getLength() > 0) {
				if (doc.getElementsByTagName("estado").item(0).getTextContent().toUpperCase().equals("AUTORIZADO")) {
					System.out.println("Comprobante estado: " + doc.getElementsByTagName("estado").item(0).getTextContent());
					if (doc.getElementsByTagName("comprobante").getLength() > 0) {
						//NodeList docElements = doc.getElementsByTagName("comprobante");
						String xml_retencion = doc.getElementsByTagName("comprobante").item(0).getTextContent();
						Document doc_retencion = null;
						if (xml_retencion.contains("<")) {
							doc_retencion = db.parse(new InputSource(new StringReader(xml_retencion)));
						} else {
							System.out.println("Utilizando la raiz");
							doc_retencion = doc;
						}

						// System.out.println(xml_factura);
						// InputStream inputStream = new ByteArrayInputStream(xml_factura.getBytes());						

						numeroAutorizacion = doc_retencion.getElementsByTagName("claveAcceso").getLength() > 0
								? doc_retencion.getElementsByTagName("claveAcceso").item(0).getTextContent()
								: null;

						establecimiento = doc_retencion.getElementsByTagName("estab").getLength() > 0
								? doc_retencion.getElementsByTagName("estab").item(0).getTextContent()
								: null;
						ptoEmi = doc_retencion.getElementsByTagName("ptoEmi").getLength() > 0
								? doc_retencion.getElementsByTagName("ptoEmi").item(0).getTextContent()
								: null;
						secuencial = doc_retencion.getElementsByTagName("secuencial").getLength() > 0
								? doc_retencion.getElementsByTagName("secuencial").item(0).getTextContent()
								: null;
						
						secuencial_full=establecimiento+'-'+ptoEmi+'-'+secuencial;
						fecha_emision = doc_retencion.getElementsByTagName("fechaEmision").getLength() > 0
								? doc_retencion.getElementsByTagName("fechaEmision").item(0).getTextContent()
								: null;

						if (fecha_emision == null) {
							fecha_emision = doc_retencion.getElementsByTagName("fechaEmisionDocSustento").getLength() > 0
									? doc_retencion.getElementsByTagName("fechaEmisionDocSustento").item(0)
											.getTextContent()
									: null;
						}

						SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
						java.util.Date date = dt.parse(fecha_emision);

						SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
						fecha_emision = dt1.format(date);
						
						System.out.println("Num Retencion: "+secuencial_full);

						NodeList docsSustento = doc_retencion.getElementsByTagName("docsSustento");
						
						if(docsSustento.getLength()>1)
						{
							utilitario.agregarMensajeInfo("No se puede importar el Archivo", "La retención cuenta con varias facturas asociadas.");
							return;
						}
						
						//System.out.println(docsSustento.item(i).getTextContent());
						
						Element docSustento = (Element) docsSustento.item(0);
						
						String numDocSustento = docSustento.getElementsByTagName("numDocSustento").getLength() > 0
								? docSustento.getElementsByTagName("numDocSustento").item(0).getTextContent()
								: null;						
						
						String secuencial_factura=numDocSustento.substring(0, 3)+'-'+numDocSustento.substring(3, 6)+'-'+numDocSustento.substring(6, 15);
						
						System.out.println("numDocSustento: "+numDocSustento);
						System.out.println("secuencial_factura: "+secuencial_factura);	
						
						TablaGenerica tab_factura = utilitario.consultar("Select * from fac_factura where secuencial_fafac like '"+secuencial_factura+"';");
						if(tab_factura.getTotalFilas()<=0)
						{						
							utilitario.agregarMensajeInfo("No se puede importar el Archivo", "Factura: "+secuencial_factura+", No encontrada..!!");
							return;
						}
						
						tab_retencion.insertar();
						tab_retencion.setValor("nro_autorizacion_sri_faret", numeroAutorizacion);
						tab_retencion.setValor("fecha_faret",fecha_emision);
						tab_retencion.setValor("num_retencion_faret",secuencial_full);
						tab_retencion.setValor("ide_fafac",tab_factura.getValor("ide_fafac"));	
						////////////////////////////////////////////////////////////////
						String nombreXML="retCli_"+secuencial_full.replaceAll("-", "_")+"_fact_"+numDocSustento+".xml";
						tab_retencion.setValor("xml_faret", "/upload/"+carpeta+"/"+nombreXML );
						pckUtilidades.Utilitario.copiarArchivo(fil_xml, utilitario.getPropiedad("rutaUpload") + "/"+carpeta+"/"+nombreXML);
						////////////////////////////////////////////////////////////////
						
						tab_retencion.guardar();
						guardarPantalla();												
						tab_retencion.actualizar();
						
						Double total_ret=0.00;
						NodeList detalles = docSustento.getElementsByTagName("retencion");
						
						for (int i = 0; i < detalles.getLength(); i++) {
							//System.out.println(detalles.item(i).getTextContent());
							Element detalle = (Element) detalles.item(i);
							
							int codigo = detalle.getElementsByTagName("codigo").getLength() > 0
									? pckUtilidades.CConversion.CInt(detalle.getElementsByTagName("codigo").item(0).getTextContent())
									: 0;
							
							String codigoRetencion = detalle.getElementsByTagName("codigoRetencion").getLength() > 0
									? detalle.getElementsByTagName("codigoRetencion").item(0).getTextContent()
									: null;
							
							Double baseImponible = detalle.getElementsByTagName("baseImponible").getLength() > 0
									? pckUtilidades.CConversion.CDbl(
											detalle.getElementsByTagName("baseImponible").item(0).getTextContent())
									: 0;
							
							Double porcentajeRetener = detalle.getElementsByTagName("porcentajeRetener").getLength() > 0
									? pckUtilidades.CConversion.CDbl(
											detalle.getElementsByTagName("porcentajeRetener").item(0).getTextContent())
									: 0;
							
							Double valorRetenido = detalle.getElementsByTagName("valorRetenido").getLength() > 0
									? pckUtilidades.CConversion.CDbl(
											detalle.getElementsByTagName("valorRetenido").item(0).getTextContent())
									: 0;
							
							String sqlImp="SELECT * FROM tes_impuesto ";
							
							if(codigo==1)
							{
								sqlImp+=" where ide_tetii=1 and codigo_teimp like '"+codigoRetencion+"';";
							}
							
							if(codigo==2)
							{
								sqlImp+=" where ide_tetii=2 and activo_teimp=true and porcentaje_teimp = "+porcentajeRetener+";";
							}

							TablaGenerica tab_impuesto = utilitario.consultar(sqlImp);
							//tab_impuesto.imprimirSql();
							
							if(tab_impuesto.getTotalFilas()>0 && codigo>0)
							{
								tab_detalle_retencion.insertar();
								tab_detalle_retencion.setValor("ide_faret",tab_retencion.getValor("ide_faret"));
								tab_detalle_retencion.setValor("ide_teimp", tab_impuesto.getValor("ide_teimp"));
								tab_detalle_retencion.setValor("base_imponible_fader", baseImponible.toString());
								tab_detalle_retencion.setValor("valor_retenido_fader", valorRetenido.toString());
								total_ret+=valorRetenido;
								tab_detalle_retencion.guardar();
								guardarPantalla();
							}
							
						}
						
						tab_retencion.setValor("total_ret_faret",total_ret.toString());						
						tab_retencion.modificar(tab_retencion.getFilaActual());
						tab_retencion.guardar();
						
						guardarPantalla();

						utilitario.agregarMensaje("OK", "Retención cargada con exito..");

				} else 
					utilitario.agregarMensajeError("Retención No Autorizada", "Retención no autorizada");
				
			  } else 
				utilitario.agregarMensajeError("Retención No Autorizada", "Retención no autorizada");
			} else 
				utilitario.agregarMensajeError("Retención No Autorizada", "Retención no autorizada");
			
			
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}

			utilitario.addUpdate("tab_retencion");
			utilitario.addUpdate("tab_detalle_retencion");

		} catch (Exception e) {
			// TODO: handle exception
			utilitario.agregarMensajeError("Error al Importar", e.getMessage());
			e.printStackTrace();
		}
	}
    
    
    @Override
	public void insertar() {
		// TODO Auto-generated method stub
		
    	//utilitario.getTablaisFocus().insertar();
	}



	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
		tab_retencion.guardar();
		tab_detalle_retencion.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub

		//utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_retencion() {
		return tab_retencion;
	}

	public void setTab_retencion(Tabla tab_retencion) {
		this.tab_retencion = tab_retencion;
	}

	public Tabla getTab_detalle_retencion() {
		return tab_detalle_retencion;
	}

	public void setTab_detalle_retencion(Tabla tab_detalle_retencion) {
		this.tab_detalle_retencion = tab_detalle_retencion;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public Upload getUpl_archivo() {
		return upl_archivo;
	}

	public void setUpl_archivo(Upload upl_archivo) {
		this.upl_archivo = upl_archivo;
	}

	
	

}
