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
@Table(name = "sao_dotacion_uniforme", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoDotacionUniforme.findAll", query = "SELECT s FROM SaoDotacionUniforme s"),
    @NamedQuery(name = "SaoDotacionUniforme.findByIdeSadou", query = "SELECT s FROM SaoDotacionUniforme s WHERE s.ideSadou = :ideSadou"),
    @NamedQuery(name = "SaoDotacionUniforme.findByCantidadSadou", query = "SELECT s FROM SaoDotacionUniforme s WHERE s.cantidadSadou = :cantidadSadou"),
    @NamedQuery(name = "SaoDotacionUniforme.findByValorUnitarioSadou", query = "SELECT s FROM SaoDotacionUniforme s WHERE s.valorUnitarioSadou = :valorUnitarioSadou"),
    @NamedQuery(name = "SaoDotacionUniforme.findByDetalleSadou", query = "SELECT s FROM SaoDotacionUniforme s WHERE s.detalleSadou = :detalleSadou"),
    @NamedQuery(name = "SaoDotacionUniforme.findByFotoSadou", query = "SELECT s FROM SaoDotacionUniforme s WHERE s.fotoSadou = :fotoSadou"),
    @NamedQuery(name = "SaoDotacionUniforme.findByActivoSadou", query = "SELECT s FROM SaoDotacionUniforme s WHERE s.activoSadou = :activoSadou"),
    @NamedQuery(name = "SaoDotacionUniforme.findByUsuarioIngre", query = "SELECT s FROM SaoDotacionUniforme s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoDotacionUniforme.findByFechaIngre", query = "SELECT s FROM SaoDotacionUniforme s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoDotacionUniforme.findByHoraIngre", query = "SELECT s FROM SaoDotacionUniforme s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoDotacionUniforme.findByUsuarioActua", query = "SELECT s FROM SaoDotacionUniforme s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoDotacionUniforme.findByFechaActua", query = "SELECT s FROM SaoDotacionUniforme s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoDotacionUniforme.findByHoraActua", query = "SELECT s FROM SaoDotacionUniforme s WHERE s.horaActua = :horaActua")})
public class SaoDotacionUniforme implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sadou", nullable = false)
    private Integer ideSadou;
    @Column(name = "cantidad_sadou")
    private Integer cantidadSadou;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_unitario_sadou", precision = 12, scale = 2)
    private BigDecimal valorUnitarioSadou;
    @Size(max = 1000)
    @Column(name = "detalle_sadou", length = 1000)
    private String detalleSadou;
    @Size(max = 100)
    @Column(name = "foto_sadou", length = 100)
    private String fotoSadou;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sadou", nullable = false)
    private boolean activoSadou;
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
    @JoinColumn(name = "ide_sagrb", referencedColumnName = "ide_sagrb")
    @ManyToOne
    private SaoGrupoBien ideSagrb;
    @JoinColumn(name = "ide_sacus", referencedColumnName = "ide_sacus")
    @ManyToOne
    private SaoCustodio ideSacus;
    @JoinColumn(name = "ide_sacol", referencedColumnName = "ide_sacol")
    @ManyToOne
    private SaoColor ideSacol;

    public SaoDotacionUniforme() {
    }

    public SaoDotacionUniforme(Integer ideSadou) {
        this.ideSadou = ideSadou;
    }

    public SaoDotacionUniforme(Integer ideSadou, boolean activoSadou) {
        this.ideSadou = ideSadou;
        this.activoSadou = activoSadou;
    }

    public Integer getIdeSadou() {
        return ideSadou;
    }

    public void setIdeSadou(Integer ideSadou) {
        this.ideSadou = ideSadou;
    }

    public Integer getCantidadSadou() {
        return cantidadSadou;
    }

    public void setCantidadSadou(Integer cantidadSadou) {
        this.cantidadSadou = cantidadSadou;
    }

    public BigDecimal getValorUnitarioSadou() {
        return valorUnitarioSadou;
    }

    public void setValorUnitarioSadou(BigDecimal valorUnitarioSadou) {
        this.valorUnitarioSadou = valorUnitarioSadou;
    }

    public String getDetalleSadou() {
        return detalleSadou;
    }

    public void setDetalleSadou(String detalleSadou) {
        this.detalleSadou = detalleSadou;
    }

    public String getFotoSadou() {
        return fotoSadou;
    }

    public void setFotoSadou(String fotoSadou) {
        this.fotoSadou = fotoSadou;
    }

    public boolean getActivoSadou() {
        return activoSadou;
    }

    public void setActivoSadou(boolean activoSadou) {
        this.activoSadou = activoSadou;
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

    public SaoGrupoBien getIdeSagrb() {
        return ideSagrb;
    }

    public void setIdeSagrb(SaoGrupoBien ideSagrb) {
        this.ideSagrb = ideSagrb;
    }

    public SaoCustodio getIdeSacus() {
        return ideSacus;
    }

    public void setIdeSacus(SaoCustodio ideSacus) {
        this.ideSacus = ideSacus;
    }

    public SaoColor getIdeSacol() {
        return ideSacol;
    }

    public void setIdeSacol(SaoColor ideSacol) {
        this.ideSacol = ideSacol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSadou != null ? ideSadou.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoDotacionUniforme)) {
            return false;
        }
        SaoDotacionUniforme other = (SaoDotacionUniforme) object;
        if ((this.ideSadou == null && other.ideSadou != null) || (this.ideSadou != null && !this.ideSadou.equals(other.ideSadou))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoDotacionUniforme[ ideSadou=" + ideSadou + " ]";
    }
    
}
