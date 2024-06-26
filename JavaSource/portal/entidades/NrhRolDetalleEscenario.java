/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "nrh_rol_detalle_escenario", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "NrhRolDetalleEscenario.findAll", query = "SELECT n FROM NrhRolDetalleEscenario n"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByIdeNrrds", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.ideNrrds = :ideNrrds"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByIdeSucu", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.ideSucu = :ideSucu"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByDocumentoIdentidadNrroe", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.documentoIdentidadNrroe = :documentoIdentidadNrroe"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByApellidoNombreNrroe", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.apellidoNombreNrroe = :apellidoNombreNrroe"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByRmuAnteriorNrroe", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.rmuAnteriorNrroe = :rmuAnteriorNrroe"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByRmuEscenarioNrroe", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.rmuEscenarioNrroe = :rmuEscenarioNrroe"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByDecimoTerceroNrroe", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.decimoTerceroNrroe = :decimoTerceroNrroe"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByDecimoCuartoNrroe", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.decimoCuartoNrroe = :decimoCuartoNrroe"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByAportePatronalNrroe", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.aportePatronalNrroe = :aportePatronalNrroe"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByFondoResrevaNrroe", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.fondoResrevaNrroe = :fondoResrevaNrroe"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByTotalAnualNrroe", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.totalAnualNrroe = :totalAnualNrroe"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByActivoNrroe", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.activoNrroe = :activoNrroe"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByUsuarioIngre", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByFechaIngre", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByUsuarioActua", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByFechaActua", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByHoraIngre", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhRolDetalleEscenario.findByHoraActua", query = "SELECT n FROM NrhRolDetalleEscenario n WHERE n.horaActua = :horaActua")})
public class NrhRolDetalleEscenario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nrrds", nullable = false)
    private Integer ideNrrds;
    @Column(name = "ide_sucu")
    private Integer ideSucu;
    @Size(max = 15)
    @Column(name = "documento_identidad_nrroe", length = 15)
    private String documentoIdentidadNrroe;
    @Size(max = 100)
    @Column(name = "apellido_nombre_nrroe", length = 100)
    private String apellidoNombreNrroe;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "rmu_anterior_nrroe", precision = 12, scale = 2)
    private BigDecimal rmuAnteriorNrroe;
    @Column(name = "rmu_escenario_nrroe", precision = 12, scale = 2)
    private BigDecimal rmuEscenarioNrroe;
    @Column(name = "decimo_tercero_nrroe", precision = 12, scale = 2)
    private BigDecimal decimoTerceroNrroe;
    @Column(name = "decimo_cuarto_nrroe", precision = 12, scale = 2)
    private BigDecimal decimoCuartoNrroe;
    @Column(name = "aporte_patronal_nrroe", precision = 12, scale = 2)
    private BigDecimal aportePatronalNrroe;
    @Column(name = "fondo_resreva_nrroe", precision = 12, scale = 2)
    private BigDecimal fondoResrevaNrroe;
    @Column(name = "total_anual_nrroe", precision = 12, scale = 2)
    private BigDecimal totalAnualNrroe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_nrroe", nullable = false)
    private boolean activoNrroe;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @JoinColumn(name = "ide_nrroe", referencedColumnName = "ide_nrroe")
    @ManyToOne
    private NrhRolEscenario ideNrroe;
    @JoinColumn(name = "ide_nrdtn", referencedColumnName = "ide_nrdtn")
    @ManyToOne
    private NrhDetalleTipoNomina ideNrdtn;
    @JoinColumn(name = "ide_gttco", referencedColumnName = "ide_gttco")
    @ManyToOne
    private GthTipoContrato ideGttco;
    @JoinColumns({
        @JoinColumn(name = "ide_gegro", referencedColumnName = "ide_gegro"),
        @JoinColumn(name = "ide_gecaf", referencedColumnName = "ide_gecaf")})
    @ManyToOne
    private GenGrupoCargo genGrupoCargo;

    public NrhRolDetalleEscenario() {
    }

    public NrhRolDetalleEscenario(Integer ideNrrds) {
        this.ideNrrds = ideNrrds;
    }

    public NrhRolDetalleEscenario(Integer ideNrrds, boolean activoNrroe) {
        this.ideNrrds = ideNrrds;
        this.activoNrroe = activoNrroe;
    }

    public Integer getIdeNrrds() {
        return ideNrrds;
    }

    public void setIdeNrrds(Integer ideNrrds) {
        this.ideNrrds = ideNrrds;
    }

    public Integer getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Integer ideSucu) {
        this.ideSucu = ideSucu;
    }

    public String getDocumentoIdentidadNrroe() {
        return documentoIdentidadNrroe;
    }

    public void setDocumentoIdentidadNrroe(String documentoIdentidadNrroe) {
        this.documentoIdentidadNrroe = documentoIdentidadNrroe;
    }

    public String getApellidoNombreNrroe() {
        return apellidoNombreNrroe;
    }

    public void setApellidoNombreNrroe(String apellidoNombreNrroe) {
        this.apellidoNombreNrroe = apellidoNombreNrroe;
    }

    public BigDecimal getRmuAnteriorNrroe() {
        return rmuAnteriorNrroe;
    }

    public void setRmuAnteriorNrroe(BigDecimal rmuAnteriorNrroe) {
        this.rmuAnteriorNrroe = rmuAnteriorNrroe;
    }

    public BigDecimal getRmuEscenarioNrroe() {
        return rmuEscenarioNrroe;
    }

    public void setRmuEscenarioNrroe(BigDecimal rmuEscenarioNrroe) {
        this.rmuEscenarioNrroe = rmuEscenarioNrroe;
    }

    public BigDecimal getDecimoTerceroNrroe() {
        return decimoTerceroNrroe;
    }

    public void setDecimoTerceroNrroe(BigDecimal decimoTerceroNrroe) {
        this.decimoTerceroNrroe = decimoTerceroNrroe;
    }

    public BigDecimal getDecimoCuartoNrroe() {
        return decimoCuartoNrroe;
    }

    public void setDecimoCuartoNrroe(BigDecimal decimoCuartoNrroe) {
        this.decimoCuartoNrroe = decimoCuartoNrroe;
    }

    public BigDecimal getAportePatronalNrroe() {
        return aportePatronalNrroe;
    }

    public void setAportePatronalNrroe(BigDecimal aportePatronalNrroe) {
        this.aportePatronalNrroe = aportePatronalNrroe;
    }

    public BigDecimal getFondoResrevaNrroe() {
        return fondoResrevaNrroe;
    }

    public void setFondoResrevaNrroe(BigDecimal fondoResrevaNrroe) {
        this.fondoResrevaNrroe = fondoResrevaNrroe;
    }

    public BigDecimal getTotalAnualNrroe() {
        return totalAnualNrroe;
    }

    public void setTotalAnualNrroe(BigDecimal totalAnualNrroe) {
        this.totalAnualNrroe = totalAnualNrroe;
    }

    public boolean getActivoNrroe() {
        return activoNrroe;
    }

    public void setActivoNrroe(boolean activoNrroe) {
        this.activoNrroe = activoNrroe;
    }

    public String getUsuarioIngre() {
        return usuarioIngre;
    }

    public void setUsuarioIngre(String usuarioIngre) {
        this.usuarioIngre = usuarioIngre;
    }

    public Date getFechaIngre() {
        return fechaIngre;
    }

    public void setFechaIngre(Date fechaIngre) {
        this.fechaIngre = fechaIngre;
    }

    public String getUsuarioActua() {
        return usuarioActua;
    }

    public void setUsuarioActua(String usuarioActua) {
        this.usuarioActua = usuarioActua;
    }

    public Date getFechaActua() {
        return fechaActua;
    }

    public void setFechaActua(Date fechaActua) {
        this.fechaActua = fechaActua;
    }

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public NrhRolEscenario getIdeNrroe() {
        return ideNrroe;
    }

    public void setIdeNrroe(NrhRolEscenario ideNrroe) {
        this.ideNrroe = ideNrroe;
    }

    public NrhDetalleTipoNomina getIdeNrdtn() {
        return ideNrdtn;
    }

    public void setIdeNrdtn(NrhDetalleTipoNomina ideNrdtn) {
        this.ideNrdtn = ideNrdtn;
    }

    public GthTipoContrato getIdeGttco() {
        return ideGttco;
    }

    public void setIdeGttco(GthTipoContrato ideGttco) {
        this.ideGttco = ideGttco;
    }

    public GenGrupoCargo getGenGrupoCargo() {
        return genGrupoCargo;
    }

    public void setGenGrupoCargo(GenGrupoCargo genGrupoCargo) {
        this.genGrupoCargo = genGrupoCargo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrrds != null ? ideNrrds.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhRolDetalleEscenario)) {
            return false;
        }
        NrhRolDetalleEscenario other = (NrhRolDetalleEscenario) object;
        if ((this.ideNrrds == null && other.ideNrrds != null) || (this.ideNrrds != null && !this.ideNrrds.equals(other.ideNrrds))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhRolDetalleEscenario[ ideNrrds=" + ideNrrds + " ]";
    }
    
}
