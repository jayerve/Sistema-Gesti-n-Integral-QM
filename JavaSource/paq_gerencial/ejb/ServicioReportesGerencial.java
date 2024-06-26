package paq_gerencial.ejb;

public final class ServicioReportesGerencial {
	public static String obtenerSQLCedulasIngresos(int anio,
			String codigo, int nivelInicio, int nivelFin) {
		return "select "
				+ "clasificador.codigo_clasificador_prcla as codigo, clasificador.descripcion_clasificador_prcla as descripcion,  "
				+ "sum(cedula_ingreso.inicial_prcing) as inicial, sum(cedula_ingreso.reformado_prcing) as reformado, "
				+ "sum(cedula_ingreso.codificado_prcing) as codificado, sum(cedula_ingreso.devengado_prcing) as devengado, "
				+ "sum(cedula_ingreso.cobrado_prcing) as cobrado, sum(cedula_ingreso.saldo_devengar_prcing) as saldo_devengar, "
				+ "sum(cedula_ingreso.saldo_cobrar_prcing) as saldo_cobrar, sum(cedula_ingreso.cobrado_efectivo_prcing) as cobrado_efectivo "
				+ "from pre_cedula_ingreso cedula_ingreso "
				+ "left join pre_clasificador clasificador on clasificador.ide_prcla = cedula_ingreso.ide_prcla "
				+ "where ide_geani = " + anio + " "
				+ " and clasificador.codigo_clasificador_prcla = '" + codigo
				+ "' and cedula_ingreso.ide_prcla in ("
				+ "    select ide_prcla " + "    from pre_clasificador "
				+ "    where nivel_prcla " + "    between " + nivelInicio
				+ " and " + nivelFin + " " + "    and tipo_prcla = 1" + ") "
				+ "group by clasificador.codigo_clasificador_prcla, "
				+ "    clasificador.descripcion_clasificador_prcla "
				+ "order by clasificador.codigo_clasificador_prcla;";
	}

	public static String obtenerNombresCedulasIngresos(int anio,
			int nivelInicio, int nivelFin) {
		return "select distinct clasificador.codigo_clasificador_prcla as codigo, "
				+ "clasificador.descripcion_clasificador_prcla as descripcion "
				+ "from pre_cedula_ingreso cedula_ingreso "
				+ "left join pre_clasificador clasificador on clasificador.ide_prcla = cedula_ingreso.ide_prcla "
				+ "where ide_geani = "
				+ anio
				+ " "
				+ "and cedula_ingreso.ide_prcla in ("
				+ "    select ide_prcla "
				+ "    from pre_clasificador "
				+ "    where nivel_prcla "
				+ "    between "
				+ nivelInicio
				+ " and "
				+ nivelFin
				+ " "
				+ "    and tipo_prcla = 1"
				+ ") "
				+ "order by clasificador.codigo_clasificador_prcla;";
	}

	public static String obtenerSQLCedulasGastos(int anio, String codigo, int nivelInicio, int nivelFin) {
		return "select cedula_gastos.ide_prcla as codigo, "
				+ "clasificador.descripcion_clasificador_prcla as descripcion, "
				+ "sum(cedula_gastos.inicial_prcei) as inicial, "
				+ "sum(cedula_gastos.reforma_prcei) as reformado, "
				+ "sum(cedula_gastos.codificado_prcei) as codificado, "
				+ "sum(cedula_gastos.certificado_prcei) as certificado, "
				+ "sum(cedula_gastos.comprometido_prcei) as comprometido, "
				+ "sum(cedula_gastos.devengado_prcei) as devengado, "
				+ "sum(cedula_gastos.pagado_prcei) as pagado, "
				+ "sum(cedula_gastos.saldo_comprometer_prcei) as saldo_comprometido, "
				+ "sum(cedula_gastos.saldo_devengar_prcei) as saldo_devengar, "
				+ "sum(cedula_gastos.acumulado_prcei) as acumulado, "
				+ "sum(cedula_gastos.saldo_pagado_prcei) as saldo_pagado, "
				+ "sum(cedula_gastos.liquidado_prcei) as liquidado, "
				+ "sum(cedula_gastos.saldo_certificar_prcei) as saldo_certificar "
				+ "from pre_cedula_gastos cedula_gastos "
				+ "left join pre_clasificador clasificador on clasificador.ide_prcla = cedula_gastos.ide_prcla "
				+ "where cedula_gastos.ide_geani = " + anio + " "
				+ "and clasificador.ide_prcla = " + codigo + " "
				+ "and cedula_gastos.ide_prcla in ( "
				+ "    select ide_prcla "
				+ "    from pre_clasificador "
				+ "    where nivel_prcla "
				+ "    between " + nivelInicio + " and " + nivelFin + " "
				+ "    and tipo_prcla = 0 "
				+ ")  "
				+ "group by cedula_gastos.ide_prcla, clasificador.descripcion_clasificador_prcla "
				+ "order by cedula_gastos.ide_prcla;";
	}

	public static String obtenerNombresCedulasGastos(int anio,
			int nivelInicio, int nivelFin){
		return "select distinct cedula_gastos.ide_prcla as codigo, "
                + "clasificador.descripcion_clasificador_prcla as descripcion "
                + "from pre_cedula_gastos cedula_gastos "
                + "left join pre_clasificador clasificador on clasificador.ide_prcla = cedula_gastos.ide_prcla "
                + "where cedula_gastos.ide_geani = " + anio + " "
                + "and cedula_gastos.ide_prcla in ( "
                + "    select ide_prcla "
                + "    from pre_clasificador "
                + "    where nivel_prcla "
                + "    between " + nivelInicio + " and " + nivelFin + " "
                + "    and tipo_prcla = 0 "
                + ") "
                + "order by cedula_gastos.ide_prcla;";
	}
}
