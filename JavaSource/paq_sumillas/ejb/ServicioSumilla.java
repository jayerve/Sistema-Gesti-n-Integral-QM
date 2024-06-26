package paq_sumillas.ejb;


import javax.ejb.EJB;
import javax.ejb.Stateless;

import paq_sistema.aplicacion.Utilitario;
//import com.sun.xml.internal.bind.marshaller.NioEscapeHandler;
import framework.aplicacion.TablaGenerica;


/**
 * @author 
 *
 */
@Stateless
public class ServicioSumilla {

	private Utilitario utilitario = new Utilitario();

	@EJB
	private ServicioSumilla ser_sumilla;

	public String getSumillaPorPardreYEtapa(Integer padre, Integer idEtapa){
		String sql_etapa="SELECT ide_smtre " +
				"FROM SUM_TRAMITE_RESPUESTA " +
				"WHERE  ide_smtra = " + padre + " " +
				"and ide_sumet = " + idEtapa;
		
		System.out.println("getSumillaPorPardreYEtapa "+sql_etapa);
		return sql_etapa;
	}
	
	public String getSumillaPorPardre(Integer padre){
		String sql_etapa="SELECT ide_smtre " +
				"FROM SUM_TRAMITE_RESPUESTA " +
				"WHERE  ide_smtra = " + padre;
		System.out.println("getSumillaPorPardre "+sql_etapa);
		return sql_etapa;
	}
	
	public TablaGenerica getSumillaPorPadreCmp(Integer padre){
		String sql="SELECT ide_smtre, ide_smtra, "
				   //SUMILLAS
			       +"fecha_sumilla_smtre, disposicion_smtre, ide_geareg, ide_gearec, recibido_smtre, ide_suesu, "
			       +"ide_sumpr, fecha_entrega_smtre, fecha_vencimiento_smtre, adjunto_smtre, cod_bpm_smtre, ide_sumet, "
			       //PRORROGA
			       +"dias1_smtre, fecha_vencimiento1_smtre, observacion1_smtre, dias2_smtre, fecha_vencimiento2_smtre, observacion2_smtre, "
			       +"dias3_smtre, fecha_vencimiento3_smtre, observacion3_smtre, dias4_smtre, fecha_vencimiento4_smtre, observacion4_smtre, "
			       //DATOS GENERALES RESPUESTA
			       +"fecha_documento_smtre, num_documento_smtre, destinatario_regdes, cargo_destinatario_smtre, empresa_destinatario_smtre, "
			       +"asunto_smtre, fecha_entrega_res_smtre, fecha_limite_smtre, fecha_limite_prog_smtre, gabinete_smtre, adjunto_res_smtre "
			       +"FROM sum_tramite_respuesta where ide_smtra="+padre;
		 TablaGenerica tab_tramiteRes = utilitario.consultar(sql);
		 return tab_tramiteRes;
	}
	
	public String getSumillaPorPadreYEstadoSumilla(Integer padre, Integer idEstadoSumilla){
		String sql_etapa="SELECT ide_smtre " +
				"FROM SUM_TRAMITE_RESPUESTA " +
				"WHERE  ide_smtra = " + padre + " " +
				"and ide_suesu = " + idEstadoSumilla;
		return sql_etapa;
	}
	
	public TablaGenerica getTramiteRespuestaPorId(Integer id){
		String sql="SELECT ide_smtre, ide_smtra, "
				   //SUMILLAS
			       +"fecha_sumilla_smtre, disposicion_smtre, ide_geareg, ide_gearec, recibido_smtre, ide_suesu, "
			       +"ide_sumpr, fecha_entrega_smtre, fecha_vencimiento_smtre, adjunto_smtre, cod_bpm_smtre, ide_sumet, "
			       //PRORROGA
			       +"dias1_smtre, fecha_vencimiento1_smtre, observacion1_smtre, dias2_smtre, fecha_vencimiento2_smtre, observacion2_smtre, "
			       +"dias3_smtre, fecha_vencimiento3_smtre, observacion3_smtre, dias4_smtre, fecha_vencimiento4_smtre, observacion4_smtre, "
			       //DATOS GENERALES RESPUESTA
			       +"fecha_documento_smtre, num_documento_smtre, destinatario_regdes, cargo_destinatario_smtre, empresa_destinatario_smtre, "
			       +"asunto_smtre, fecha_entrega_res_smtre, fecha_limite_smtre, fecha_limite_prog_smtre, gabinete_smtre, adjunto_res_smtre "
			       +"FROM sum_tramite_respuesta where ide_smtre="+id;
		 TablaGenerica tab_tramiteResB = utilitario.consultar(sql);
		 return tab_tramiteResB;
	}
}
