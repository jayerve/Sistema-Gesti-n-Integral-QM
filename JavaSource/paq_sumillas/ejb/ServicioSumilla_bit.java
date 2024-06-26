package paq_sumillas.ejb;


import javax.ejb.Stateless;
//import com.sun.xml.internal.bind.marshaller.NioEscapeHandler;


/**
 * @author 
 *
 */
@Stateless
public class ServicioSumilla_bit {
	
	public String getTramiteRespuestaBitPorTramiteRespuestaYEtapa(Integer padre, Integer etapa){
		String sql_prioridad="SELECT ide_smtre_bit " +
				"FROM sum_tramite_respuesta_bit " +
				"WHERE  ide_smtre = " + padre + " " +
				"and ide_sumet = " + etapa;
		return sql_prioridad;
	}
	
	public String getTramiteRespuestaBitPorTramiteRespuestaYEtapaYEstado(Integer padre, Integer etapa, Integer estado){
		String sql_prioridad="SELECT ide_smtre_bit " +
				"FROM sum_tramite_respuesta_bit " +
				"WHERE  ide_smtre = " + padre + " " +
				"and ide_sumet = " + etapa + " " +
				"and ide_suesu = " + estado;
		return sql_prioridad;
	}
	
}
