package paq_sumillas.ejb;


import javax.ejb.Stateless;

import paq_sistema.aplicacion.Utilitario;
import framework.aplicacion.TablaGenerica;



/**
 * @author 
 *
 */
@Stateless
public class ServicioSumTramite {
	private Utilitario utilitario = new Utilitario();

	public String getTramiteEtapa(Integer idEtapa){
		String sql_etapa="SELECT ide_smtra, doc_respuesta_smtra, doc_interno_smtra, doc_referencia_smtra, " +
				"num_tramite_smtra, num_documento_smtra, num_hojas_smtra, fecha_emision_doc_smtra, fecha_recep_doc_smtra, " +
				"remitente_smtra, destinatario_regdes, cargo_destinatario_smtra, empresa_destinatario_smtra, asunto_smtra, " +
				"activo_smtra, ide_suest, adjunto_esttra, cod_bpm_esttra, ide_sumet " +
				"FROM SUM_TRAMITE " +
				"WHERE  ide_sumet = " + idEtapa;
		return sql_etapa;
	}
	
	public TablaGenerica getTramitePorId(Integer id){
		String sql="SELECT ide_smtra, doc_respuesta_smtra, doc_interno_smtra, doc_referencia_smtra, num_tramite_smtra, "
			       +"num_documento_smtra, num_hojas_smtra, fecha_emision_doc_smtra, fecha_recep_doc_smtra, remitente_smtra, "
			       +"destinatario_regdes, cargo_destinatario_smtra, empresa_destinatario_smtra, asunto_smtra, activo_smtra, "
			       +"ide_suest, adjunto_esttra, cod_bpm_esttra, ide_sumet "
			       +"FROM sum_tramite where ide_smtra="+id;
		 TablaGenerica tab_tramiteB = utilitario.consultar(sql);
		 return tab_tramiteB;
	}
	
	/**
	  * Metodo que devuelve el numero secuencial, por modulo
	  * @param modulo recibe el modulo al cual pertenece el secuencial
	  * @return String nro secuencial por modulo
	  */
	 public String numeroSecuencial(String modulo){
	  String resultado="1";
	  
	  TablaGenerica valor_resultado=utilitario.consultar("select ide_gemod,numero_secuencial_gemos from gen_modulo_secuencial where ide_gemod="+modulo);
	  Integer nuevo_valor=pckUtilidades.CConversion.CInt(valor_resultado.getValor("numero_secuencial_gemos"))+1;
	  resultado = nuevo_valor+"";
	  //System.out.println("SECUENCIA--->>"+resultado);
	  return resultado;
	 }
	 
	 public String guardaSecuencial(String secuencial_vigente,String modulo){
	  String mensaje="Actualizado Secuencial";
	  double nuevo_valor=Double.parseDouble(secuencial_vigente);
	  utilitario.getConexion().ejecutarSql("update gen_modulo_secuencial set numero_secuencial_gemos="+nuevo_valor+" where ide_gemod="+modulo);
	  //System.out.println("update gen_modulo_secuencial set numero_secuencial_gemos="+nuevo_valor+" where ide_gemod="+modulo);
	  return mensaje;
	 }
	
}
