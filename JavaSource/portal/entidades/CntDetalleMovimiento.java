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
@Table(name = "cnt_detalle_movimiento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CntDetalleMovimiento.findAll", query = "SELECT c FROM CntDetalleMovimiento c"),
    @NamedQuery(name = "CntDetalleMovimiento.findByIdeCndem", query = "SELECT c FROM CntDetalleMovimiento c WHERE c.ideCndem = :ideCndem"),
    @NamedQuery(name = "CntDetalleMovimiento.findByIdeSucu", query = "SELECT c FROM CntDetalleMovimiento c WHERE c.ideSucu = :ideSucu"),
    @NamedQuery(name = "CntDetalleMovimiento.findByIdeGeare", query = "SELECT c FROM CntDetalleMovimiento c WHERE c.ideGeare = :ideGeare"),
    @NamedQuery(name = "CntDetalleMovimiento.findByDebeCndem", query = "SELECT c FROM CntDetalleMovimiento c WHERE c.debeCndem = :debeCndem"),
    @NamedQuery(name = "CntDetalleMovimiento.findByHaberCndem", query = "SELECT c FROM CntDetalleMovimiento c WHERE c.haberCndem = :haberCndem"),
    @NamedQuery(name = "CntDetalleMovimiento.findByActivoCndem", query = "SELECT c FROM CntDetalleMovimiento c WHERE c.activoCndem = :activoCndem"),
    @NamedQuery(name = "CntDetalleMovimiento.findByUsuarioIngre", query = "SELECT c FROM CntDetalleMovimiento c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CntDetalleMovimiento.findByFechaIngre", query = "SELECT c FROM CntDetalleMovimiento c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CntDetalleMovimiento.findByHoraIngre", query = "SELECT c FROM CntDetalleMovimiento c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CntDetalleMovimiento.findByUsuarioActua", query = "SELECT c FROM CntDetalleMovimiento c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CntDetalleMovimiento.findByFechaActua", query = "SELECT c FROM CntDetalleMovimiento c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CntDetalleMovimiento.findByHoraActua", query = "SELECT c FROM CntDetalleMovimiento c WHERE c.horaActua = :horaActua")})
public class CntDetalleMovimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cndem", nullable = false)
    private Integer ideCndem;
    @Column(name = "ide_sucu")
    private Integer ideSucu;
    @Column(name = "ide_geare")
    private Integer ideGeare;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "debe_cndem", precision = 12, scale = 2)
    private BigDecimal debeCndem;
    @Column(name = "haber_cndem", precision = 12, scale = 2)
    private BigDecimal haberCndem;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cndem", nullable = false)
    private boolean activoCndem;
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
    @JoinColumn(name = "ide_nrdtn", referencedColumnName = "ide_nrdtn")
    @ManyToOne
    private NrhDetalleTipoNomina ideNrdtn;
    @JoinColumn(name = "ide_gelua", referencedColumnName = "ide_gelua")
    @ManyToOne
    private GenLugarAplica ideGelua;
    @JoinColumn(name = "ide_gecuc", referencedColumnName = "ide_gecuc")
    @ManyToOne
    private GenCuentaContable ideGecuc;
    @JoinColumn(name = "ide_cnmoc", referencedColumnName = "ide_cnmoc")
    @ManyToOne
    private CntMovimientoContable ideCnmoc;

    public CntDetalleMovimiento() {
    }

    public CntDetalleMovimiento(Integer ideCndem) {
        this.ideCndem = ideCndem;
    }

    public CntDetalleMovimiento(Integer ideCndem, boolean activoCndem) {
        this.ideCndem = ideCndem;
        this.activoCndem = activoCndem;
    }

    public Integer getIdeCndem() {
        return ideCndem;
    }

    public void setIdeCndem(Integer ideCndem) {
        this.ideCndem = ideCndem;
    }

    public Integer getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Integer ideSucu) {
        this.ideSucu = ideSucu;
    }

    public Integer getIdeGeare() {
        return ideGeare;
    }

    public void setIdeGeare(Integer ideGeare) {
        this.ideGeare = ideGeare;
    }

    public BigDecimal getDebeCndem() {
        return debeCndem;
    }

    public void setDebeCndem(BigDecimal debeCndem) {
        this.debeCndem = debeCndem;
    }

    public BigDecimal getHaberCndem() {
        return haberCndem;
    }

    public void setHaberCndem(BigDecimal haberCndem) {
        this.haberCndem = haberCndem;
    }

    public boolean getActivoCndem() {
        return activoCndem;
    }

    public void setActivoCndem(boolean activoCndem) {
        this.activoCndem = activoCndem;
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

    public NrhDetalleTipoNomina getIdeNrdtn() {
        return ideNrdtn;
    }

    public void setIdeNrdtn(NrhDetalleTipoNomina ideNrdtn) {
        this.ideNrdtn = ideNrdtn;
    }

    public GenLugarAplica getIdeGelua() {
        return ideGelua;
    }

    public void setIdeGelua(GenLugarAplica ideGelua) {
        this.ideGelua = ideGelua;
    }

    public GenCuentaContable getIdeGecuc() {
        return ideGecuc;
    }

    public void setIdeGecuc(GenCuentaContable ideGecuc) {
        this.ideGecuc = ideGecuc;
    }

    public CntMovimientoContable getIdeCnmoc() {
        return ideCnmoc;
    }

    public void setIdeCnmoc(CntMovimientoContable ideCnmoc) {
        this.ideCnmoc = ideCnmoc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCndem != null ? ideCndem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CntDetalleMovimiento)) {
            return false;
        }
        CntDetalleMovimiento other = (CntDetalleMovimiento) object;
        if ((this.ideCndem == null && other.ideCndem != null) || (this.ideCndem != null && !this.ideCndem.equals(other.ideCndem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CntDetalleMovimiento[ ideCndem=" + ideCndem + " ]";
    }
    
}
