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
@Table(name = "sbs_archivo_ediez", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SbsArchivoEdiez.findAll", query = "SELECT s FROM SbsArchivoEdiez s"),
    @NamedQuery(name = "SbsArchivoEdiez.findByIdeSbare", query = "SELECT s FROM SbsArchivoEdiez s WHERE s.ideSbare = :ideSbare"),
    @NamedQuery(name = "SbsArchivoEdiez.findByFechaIngresoSbare", query = "SELECT s FROM SbsArchivoEdiez s WHERE s.fechaIngresoSbare = :fechaIngresoSbare"),
    @NamedQuery(name = "SbsArchivoEdiez.findByValorPromedioSbare", query = "SELECT s FROM SbsArchivoEdiez s WHERE s.valorPromedioSbare = :valorPromedioSbare"),
    @NamedQuery(name = "SbsArchivoEdiez.findByFechaSalidaSbare", query = "SELECT s FROM SbsArchivoEdiez s WHERE s.fechaSalidaSbare = :fechaSalidaSbare"),
    @NamedQuery(name = "SbsArchivoEdiez.findByUtilidadSbare", query = "SELECT s FROM SbsArchivoEdiez s WHERE s.utilidadSbare = :utilidadSbare"),
    @NamedQuery(name = "SbsArchivoEdiez.findByDeudaSbare", query = "SELECT s FROM SbsArchivoEdiez s WHERE s.deudaSbare = :deudaSbare"),
    @NamedQuery(name = "SbsArchivoEdiez.findByPatrimonioSbare", query = "SELECT s FROM SbsArchivoEdiez s WHERE s.patrimonioSbare = :patrimonioSbare"),
    @NamedQuery(name = "SbsArchivoEdiez.findByActivoSbare", query = "SELECT s FROM SbsArchivoEdiez s WHERE s.activoSbare = :activoSbare"),
    @NamedQuery(name = "SbsArchivoEdiez.findByUsuarioIngre", query = "SELECT s FROM SbsArchivoEdiez s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SbsArchivoEdiez.findByFechaIngre", query = "SELECT s FROM SbsArchivoEdiez s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SbsArchivoEdiez.findByHoraIngre", query = "SELECT s FROM SbsArchivoEdiez s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SbsArchivoEdiez.findByUsuarioActua", query = "SELECT s FROM SbsArchivoEdiez s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SbsArchivoEdiez.findByFechaActua", query = "SELECT s FROM SbsArchivoEdiez s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SbsArchivoEdiez.findByHoraActua", query = "SELECT s FROM SbsArchivoEdiez s WHERE s.horaActua = :horaActua")})
public class SbsArchivoEdiez implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sbare", nullable = false)
    private Integer ideSbare;
    @Column(name = "fecha_ingreso_sbare")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoSbare;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_promedio_sbare", precision = 12, scale = 2)
    private BigDecimal valorPromedioSbare;
    @Column(name = "fecha_salida_sbare")
    @Temporal(TemporalType.DATE)
    private Date fechaSalidaSbare;
    @Column(name = "utilidad_sbare", precision = 12, scale = 2)
    private BigDecimal utilidadSbare;
    @Column(name = "deuda_sbare", precision = 12, scale = 2)
    private BigDecimal deudaSbare;
    @Column(name = "patrimonio_sbare", precision = 12, scale = 2)
    private BigDecimal patrimonioSbare;
    @Column(name = "activo_sbare")
    private Boolean activoSbare;
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
    @JoinColumn(name = "ide_sbofi", referencedColumnName = "ide_sbofi")
    @ManyToOne
    private SbsOficina ideSbofi;
    @JoinColumn(name = "ide_sblaa", referencedColumnName = "ide_sblaa")
    @ManyToOne
    private SbsLavadoActivos ideSblaa;
    @JoinColumn(name = "ide_sbcar", referencedColumnName = "ide_sbcar")
    @ManyToOne
    private SbsCargo ideSbcar;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;

    public SbsArchivoEdiez() {
    }

    public SbsArchivoEdiez(Integer ideSbare) {
        this.ideSbare = ideSbare;
    }

    public Integer getIdeSbare() {
        return ideSbare;
    }

    public void setIdeSbare(Integer ideSbare) {
        this.ideSbare = ideSbare;
    }

    public Date getFechaIngresoSbare() {
        return fechaIngresoSbare;
    }

    public void setFechaIngresoSbare(Date fechaIngresoSbare) {
        this.fechaIngresoSbare = fechaIngresoSbare;
    }

    public BigDecimal getValorPromedioSbare() {
        return valorPromedioSbare;
    }

    public void setValorPromedioSbare(BigDecimal valorPromedioSbare) {
        this.valorPromedioSbare = valorPromedioSbare;
    }

    public Date getFechaSalidaSbare() {
        return fechaSalidaSbare;
    }

    public void setFechaSalidaSbare(Date fechaSalidaSbare) {
        this.fechaSalidaSbare = fechaSalidaSbare;
    }

    public BigDecimal getUtilidadSbare() {
        return utilidadSbare;
    }

    public void setUtilidadSbare(BigDecimal utilidadSbare) {
        this.utilidadSbare = utilidadSbare;
    }

    public BigDecimal getDeudaSbare() {
        return deudaSbare;
    }

    public void setDeudaSbare(BigDecimal deudaSbare) {
        this.deudaSbare = deudaSbare;
    }

    public BigDecimal getPatrimonioSbare() {
        return patrimonioSbare;
    }

    public void setPatrimonioSbare(BigDecimal patrimonioSbare) {
        this.patrimonioSbare = patrimonioSbare;
    }

    public Boolean getActivoSbare() {
        return activoSbare;
    }

    public void setActivoSbare(Boolean activoSbare) {
        this.activoSbare = activoSbare;
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

    public SbsOficina getIdeSbofi() {
        return ideSbofi;
    }

    public void setIdeSbofi(SbsOficina ideSbofi) {
        this.ideSbofi = ideSbofi;
    }

    public SbsLavadoActivos getIdeSblaa() {
        return ideSblaa;
    }

    public void setIdeSblaa(SbsLavadoActivos ideSblaa) {
        this.ideSblaa = ideSblaa;
    }

    public SbsCargo getIdeSbcar() {
        return ideSbcar;
    }

    public void setIdeSbcar(SbsCargo ideSbcar) {
        this.ideSbcar = ideSbcar;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSbare != null ? ideSbare.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SbsArchivoEdiez)) {
            return false;
        }
        SbsArchivoEdiez other = (SbsArchivoEdiez) object;
        if ((this.ideSbare == null && other.ideSbare != null) || (this.ideSbare != null && !this.ideSbare.equals(other.ideSbare))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SbsArchivoEdiez[ ideSbare=" + ideSbare + " ]";
    }
    
}
