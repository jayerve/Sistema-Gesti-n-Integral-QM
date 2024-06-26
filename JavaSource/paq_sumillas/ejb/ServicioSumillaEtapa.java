package paq_sumillas.ejb;


import javax.ejb.Stateless;



/**
 * @author 
 *
 */
@Stateless
public class ServicioSumillaEtapa {
	
	public String getEtapa(Integer idEtapa){
		String sql_etapa="SELECT ide_sumet, nombre_sumet " +
				"FROM SUM_ETAPA et " +
				"WHERE  et.ide_sumet = " + idEtapa;
		return sql_etapa;
	}
	
	public String getEtapas(){
		String sql_etapa="SELECT ide_sumet, nombre_sumet " +
				"FROM SUM_ETAPA et ";
		return sql_etapa;
	}
	
}
