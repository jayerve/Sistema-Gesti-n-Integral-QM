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
@Table(name = "cont_movimiento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContMovimiento.findAll", query = "SELECT c FROM ContMovimiento c"),
    @NamedQuery(name = "ContMovimiento.findByIdeComov", query = "SELECT c FROM ContMovimiento c WHERE c.ideComov = :ideComov"),
    @NamedQuery(name = "ContMovimiento.findByMovFechaComov", query = "SELECT c FROM ContMovimiento c WHERE c.movFechaComov = :movFechaComov"),
    @NamedQuery(name = "ContMovimiento.findByDetalleComov", query = "SELECT c FROM ContMovimiento c WHERE c.detalleComov = :detalleComov"),
    @NamedQuery(name = "ContMovimiento.findByNroComprobanteComov", query = "SELECT c FROM ContMovimiento c WHERE c.nroComprobanteComov = :nroComprobanteComov"),
    @NamedQuery(name = "ContMovimiento.findByActivoComov", query = "SELECT c FROM ContMovimiento c WHERE c.activoComov = :activoComov"),
    @NamedQuery(name = "ContMovimiento.findByUsuarioIngre", query = "SELECT c FROM ContMovimiento c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContMovimiento.findByFechaIngre", query = "SELECT c FROM ContMovimiento c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContMovimiento.findByHoraIngre", query = "SELECT c FROM ContMovimiento c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContMovimiento.findByUsuarioActua", query = "SELECT c FROM ContMovimiento c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContMovimiento.findByFechaActua", query = "SELECT c FROM ContMovimiento c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContMovimiento.findByHoraActua", query = "SELECT c FROM ContMovimiento c WHERE c.horaActua = :horaActua")})
public class ContMovimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_comov", nullable = false)
    private Long ideComov;
    @Column(name = "mov_fecha_comov")
    @Temporal(TemporalType.DATE)
    private Date movFechaComov;
    @Size(max = 2147483647)
    @Column(name = "detalle_comov", length = 2147483647)
    private String detalleComov;
    @Size(max = 20)
    @Column(name = "nro_comprobante_comov", length = 20)
    private String nroComprobanteComov;
    @Column(name = "activo_comov")
    private Boolean activoComov;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideComov")
    private List<ContDetalleMovimiento> contDetalleMovimientoList;
    @JoinColumn(name = "ide_tecpo", referencedColumnName = "ide_tecpo")
    @ManyToOne
    private TesComprobantePago ideTecpo;
    @JoinColumn(name = "ide_gemes", referencedColumnName = "ide_gemes")
    @ManyToOne
    private GenMes ideGemes;
    @JoinColumn(name = "ide_geare", referencedColumnName = "ide_geare")
    @ManyToOne
    private GenArea ideGeare;
    @JoinColumn(name = "ide_cotim", referencedColumnName = "ide_cotim")
    @ManyToOne
    private ContTipoMovimiento ideCotim;
    @JoinColumn(name = "ide_cotia", referencedColumnName = "ide_cotia")
    @ManyToOne
    private ContTipoAsiento ideCotia;

    public ContMovimiento() {
    }

    public ContMovimiento(Long ideComov) {
        this.ideComov = ideComov;
    }

    public Long getIdeComov() {
        return ideComov;
    }

    public void setIdeComov(Long ideComov) {
        this.ideComov = ideComov;
    }

    public Date getMovFechaComov() {
        return movFechaComov;
    }

    public void setMovFechaComov(Date movFechaComov) {
        this.movFechaComov = movFechaComov;
    }

    public String getDetalleComov() {
        return detalleComov;
    }

    public void setDetalleComov(String detalleComov) {
        this.detalleComov = detalleComov;
    }

    public String getNroComprobanteComov() {
        return nroComprobanteComov;
    }

    public void setNroComprobanteComov(String nroComprobanteComov) {
        this.nroComprobanteComov = nroComprobanteComov;
    }

    public Boolean getActivoComov() {
        return activoComov;
    }

    public void setActivoComov(Boolean activoComov) {
        this.activoComov = activoComov;
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

    public List<ContDetalleMovimiento> getContDetalleMovimientoList() {
        return contDetalleMovimientoList;
    }

    public void setContDetalleMovimientoList(List<ContDetalleMovimiento> contDetalleMovimientoList) {
        this.contDetalleMovimientoList = contDetalleMovimientoList;
    }

    public TesComprobantePago getIdeTecpo() {
        return ideTecpo;
    }

    public void setIdeTecpo(TesComprobantePago ideTecpo) {
        this.ideTecpo = ideTecpo;
    }

    public GenMes getIdeGemes() {
        return ideGemes;
    }

    public void setIdeGemes(GenMes ideGemes) {
        this.ideGemes = ideGemes;
    }

    public GenArea getIdeGeare() {
        return ideGeare;
    }

    public void setIdeGeare(GenArea ideGeare) {
        this.ideGeare = ideGeare;
    }

    public ContTipoMovimiento getIdeCotim() {
        return ideCotim;
    }

    public void setIdeCotim(ContTipoMovimiento ideCotim) {
        this.ideCotim = ideCotim;
    }

    public ContTipoAsiento getIdeCotia() {
        return ideCotia;
    }

    public void setIdeCotia(ContTipoAsiento ideCotia) {
        this.ideCotia = ideCotia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideComov != null ? ideComov.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContMovimiento)) {
            return false;
        }
        ContMovimiento other = (ContMovimiento) object;
        if ((this.ideComov == null && other.ideComov != null) || (this.ideComov != null && !this.ideComov.equals(other.ideComov))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContMovimiento[ ideComov=" + ideComov + " ]";
    }
    
}
