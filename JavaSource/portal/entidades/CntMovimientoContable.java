/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "cnt_movimiento_contable", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CntMovimientoContable.findAll", query = "SELECT c FROM CntMovimientoContable c"),
    @NamedQuery(name = "CntMovimientoContable.findByIdeCnmoc", query = "SELECT c FROM CntMovimientoContable c WHERE c.ideCnmoc = :ideCnmoc"),
    @NamedQuery(name = "CntMovimientoContable.findByIdeSucu", query = "SELECT c FROM CntMovimientoContable c WHERE c.ideSucu = :ideSucu"),
    @NamedQuery(name = "CntMovimientoContable.findByConceptoCnmoc", query = "SELECT c FROM CntMovimientoContable c WHERE c.conceptoCnmoc = :conceptoCnmoc"),
    @NamedQuery(name = "CntMovimientoContable.findByFechaMovimientoCnmoc", query = "SELECT c FROM CntMovimientoContable c WHERE c.fechaMovimientoCnmoc = :fechaMovimientoCnmoc"),
    @NamedQuery(name = "CntMovimientoContable.findByCmcId", query = "SELECT c FROM CntMovimientoContable c WHERE c.cmcId = :cmcId"),
    @NamedQuery(name = "CntMovimientoContable.findByObservacionCnmoc", query = "SELECT c FROM CntMovimientoContable c WHERE c.observacionCnmoc = :observacionCnmoc"),
    @NamedQuery(name = "CntMovimientoContable.findByAnuladoCnmoc", query = "SELECT c FROM CntMovimientoContable c WHERE c.anuladoCnmoc = :anuladoCnmoc"),
    @NamedQuery(name = "CntMovimientoContable.findByRazonAnulacionCnmoc", query = "SELECT c FROM CntMovimientoContable c WHERE c.razonAnulacionCnmoc = :razonAnulacionCnmoc"),
    @NamedQuery(name = "CntMovimientoContable.findByActivoCnmoc", query = "SELECT c FROM CntMovimientoContable c WHERE c.activoCnmoc = :activoCnmoc"),
    @NamedQuery(name = "CntMovimientoContable.findByUsuarioIngre", query = "SELECT c FROM CntMovimientoContable c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CntMovimientoContable.findByFechaIngre", query = "SELECT c FROM CntMovimientoContable c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CntMovimientoContable.findByHoraIngre", query = "SELECT c FROM CntMovimientoContable c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CntMovimientoContable.findByUsuarioActua", query = "SELECT c FROM CntMovimientoContable c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CntMovimientoContable.findByFechaActua", query = "SELECT c FROM CntMovimientoContable c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CntMovimientoContable.findByHoraActua", query = "SELECT c FROM CntMovimientoContable c WHERE c.horaActua = :horaActua")})
public class CntMovimientoContable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cnmoc", nullable = false)
    private Integer ideCnmoc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sucu", nullable = false)
    private int ideSucu;
    @Size(max = 100)
    @Column(name = "concepto_cnmoc", length = 100)
    private String conceptoCnmoc;
    @Column(name = "fecha_movimiento_cnmoc")
    @Temporal(TemporalType.DATE)
    private Date fechaMovimientoCnmoc;
    @Column(name = "cmc_id")
    private Integer cmcId;
    @Size(max = 4000)
    @Column(name = "observacion_cnmoc", length = 4000)
    private String observacionCnmoc;
    @Column(name = "anulado_cnmoc")
    private Integer anuladoCnmoc;
    @Size(max = 4000)
    @Column(name = "razon_anulacion_cnmoc", length = 4000)
    private String razonAnulacionCnmoc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cnmoc", nullable = false)
    private boolean activoCnmoc;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.DATE)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.DATE)
    private Date horaActua;
    @JoinColumn(name = "ide_usua", referencedColumnName = "ide_usua", nullable = false)
    @ManyToOne(optional = false)
    private SisUsuario ideUsua;
    @JoinColumn(name = "ide_getia", referencedColumnName = "ide_getia")
    @ManyToOne
    private GenTipoAsiento ideGetia;
    @JoinColumns({
        @JoinColumn(name = "ide_gemes", referencedColumnName = "ide_gemes", nullable = false),
        @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani", nullable = false)})
    @ManyToOne(optional = false)
    private GenPeriodo genPeriodo;
    @JoinColumn(name = "ide_geben", referencedColumnName = "ide_geben")
    @ManyToOne
    private GenBeneficiario ideGeben;
    @JoinColumn(name = "ide_cntic", referencedColumnName = "ide_cntic", nullable = false)
    @ManyToOne(optional = false)
    private CntTipoComprobante ideCntic;
    @OneToMany(mappedBy = "ideCnmoc")
    private List<CntDetalleMovimiento> cntDetalleMovimientoList;
    @OneToMany(mappedBy = "ideCnmoc")
    private List<NrhRol> nrhRolList;

    public CntMovimientoContable() {
    }

    public CntMovimientoContable(Integer ideCnmoc) {
        this.ideCnmoc = ideCnmoc;
    }

    public CntMovimientoContable(Integer ideCnmoc, int ideSucu, boolean activoCnmoc) {
        this.ideCnmoc = ideCnmoc;
        this.ideSucu = ideSucu;
        this.activoCnmoc = activoCnmoc;
    }

    public Integer getIdeCnmoc() {
        return ideCnmoc;
    }

    public void setIdeCnmoc(Integer ideCnmoc) {
        this.ideCnmoc = ideCnmoc;
    }

    public int getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(int ideSucu) {
        this.ideSucu = ideSucu;
    }

    public String getConceptoCnmoc() {
        return conceptoCnmoc;
    }

    public void setConceptoCnmoc(String conceptoCnmoc) {
        this.conceptoCnmoc = conceptoCnmoc;
    }

    public Date getFechaMovimientoCnmoc() {
        return fechaMovimientoCnmoc;
    }

    public void setFechaMovimientoCnmoc(Date fechaMovimientoCnmoc) {
        this.fechaMovimientoCnmoc = fechaMovimientoCnmoc;
    }

    public Integer getCmcId() {
        return cmcId;
    }

    public void setCmcId(Integer cmcId) {
        this.cmcId = cmcId;
    }

    public String getObservacionCnmoc() {
        return observacionCnmoc;
    }

    public void setObservacionCnmoc(String observacionCnmoc) {
        this.observacionCnmoc = observacionCnmoc;
    }

    public Integer getAnuladoCnmoc() {
        return anuladoCnmoc;
    }

    public void setAnuladoCnmoc(Integer anuladoCnmoc) {
        this.anuladoCnmoc = anuladoCnmoc;
    }

    public String getRazonAnulacionCnmoc() {
        return razonAnulacionCnmoc;
    }

    public void setRazonAnulacionCnmoc(String razonAnulacionCnmoc) {
        this.razonAnulacionCnmoc = razonAnulacionCnmoc;
    }

    public boolean getActivoCnmoc() {
        return activoCnmoc;
    }

    public void setActivoCnmoc(boolean activoCnmoc) {
        this.activoCnmoc = activoCnmoc;
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

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
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

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public SisUsuario getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(SisUsuario ideUsua) {
        this.ideUsua = ideUsua;
    }

    public GenTipoAsiento getIdeGetia() {
        return ideGetia;
    }

    public void setIdeGetia(GenTipoAsiento ideGetia) {
        this.ideGetia = ideGetia;
    }

    public GenPeriodo getGenPeriodo() {
        return genPeriodo;
    }

    public void setGenPeriodo(GenPeriodo genPeriodo) {
        this.genPeriodo = genPeriodo;
    }

    public GenBeneficiario getIdeGeben() {
        return ideGeben;
    }

    public void setIdeGeben(GenBeneficiario ideGeben) {
        this.ideGeben = ideGeben;
    }

    public CntTipoComprobante getIdeCntic() {
        return ideCntic;
    }

    public void setIdeCntic(CntTipoComprobante ideCntic) {
        this.ideCntic = ideCntic;
    }

    public List<CntDetalleMovimiento> getCntDetalleMovimientoList() {
        return cntDetalleMovimientoList;
    }

    public void setCntDetalleMovimientoList(List<CntDetalleMovimiento> cntDetalleMovimientoList) {
        this.cntDetalleMovimientoList = cntDetalleMovimientoList;
    }

    public List<NrhRol> getNrhRolList() {
        return nrhRolList;
    }

    public void setNrhRolList(List<NrhRol> nrhRolList) {
        this.nrhRolList = nrhRolList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCnmoc != null ? ideCnmoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CntMovimientoContable)) {
            return false;
        }
        CntMovimientoContable other = (CntMovimientoContable) object;
        if ((this.ideCnmoc == null && other.ideCnmoc != null) || (this.ideCnmoc != null && !this.ideCnmoc.equals(other.ideCnmoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CntMovimientoContable[ ideCnmoc=" + ideCnmoc + " ]";
    }
    
}
