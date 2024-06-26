package paq_sumillas.ejb;


import javax.ejb.Stateless;
//import com.sun.xml.internal.bind.marshaller.NioEscapeHandler;


/**
 * @author 
 *
 */
@Stateless
public class ServicioSumTramite_bit {
	
	public String getTramiteBitPorTramiteYEtapa(Integer padre, Integer etapa){
		String sql_prioridad="SELECT ide_smtra_bit " +
				"FROM SUM_TRAMITE_BIT " +
				"WHERE  ide_smtra = " + padre + " " +
				"and ide_sumet = " + etapa;
		return sql_prioridad;
	}
	
	public String getTramiteBitPorTramiteYEtapaYEstado(Integer padre, Integer etapa, Integer estado){
		String sql_prioridad="SELECT ide_smtra_bit " +
				"FROM SUM_TRAMITE_BIT " +
				"WHERE  ide_smtra = " + padre + " " +
				"and ide_sumet = " + etapa + " " +
				"and ide_suest = " + estado;
		return sql_prioridad;
	}
	
}
