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
@Table(name = "sao_certificado_externo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoCertificadoExterno.findAll", query = "SELECT s FROM SaoCertificadoExterno s"),
    @NamedQuery(name = "SaoCertificadoExterno.findByIdeSacee", query = "SELECT s FROM SaoCertificadoExterno s WHERE s.ideSacee = :ideSacee"),
    @NamedQuery(name = "SaoCertificadoExterno.findByFechaRegistroSacee", query = "SELECT s FROM SaoCertificadoExterno s WHERE s.fechaRegistroSacee = :fechaRegistroSacee"),
    @NamedQuery(name = "SaoCertificadoExterno.findByDetalleSacee", query = "SELECT s FROM SaoCertificadoExterno s WHERE s.detalleSacee = :detalleSacee"),
    @NamedQuery(name = "SaoCertificadoExterno.findByActivoSacee", query = "SELECT s FROM SaoCertificadoExterno s WHERE s.activoSacee = :activoSacee"),
    @NamedQuery(name = "SaoCertificadoExterno.findByUsuarioIngre", query = "SELECT s FROM SaoCertificadoExterno s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoCertificadoExterno.findByFechaIngre", query = "SELECT s FROM SaoCertificadoExterno s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoCertificadoExterno.findByUsuarioActua", query = "SELECT s FROM SaoCertificadoExterno s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoCertificadoExterno.findByFechaActua", query = "SELECT s FROM SaoCertificadoExterno s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoCertificadoExterno.findByHoraActua", query = "SELECT s FROM SaoCertificadoExterno s WHERE s.horaActua = :horaActua"),
    @NamedQuery(name = "SaoCertificadoExterno.findByHoraIngre", query = "SELECT s FROM SaoCertificadoExterno s WHERE s.horaIngre = :horaIngre")})
public class SaoCertificadoExterno implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sacee", nullable = false)
    private Integer ideSacee;
    @Column(name = "fecha_registro_sacee")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistroSacee;
    @Size(max = 100)
    @Column(name = "detalle_sacee", length = 100)
    private String detalleSacee;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sacee", nullable = false)
    private boolean activoSacee;
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
    @Column(name = "hora_actua")
    @Temporal(TemporalType.DATE)
    private Date horaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @OneToMany(mappedBy = "ideSacee")
    private List<SaoDetalleCertExterno> saoDetalleCertExternoList;

    public SaoCertificadoExterno() {
    }

    public SaoCertificadoExterno(Integer ideSacee) {
        this.ideSacee = ideSacee;
    }

    public SaoCertificadoExterno(Integer ideSacee, boolean activoSacee) {
        this.ideSacee = ideSacee;
        this.activoSacee = activoSacee;
    }

    public Integer getIdeSacee() {
        return ideSacee;
    }

    public void setIdeSacee(Integer ideSacee) {
        this.ideSacee = ideSacee;
    }

    public Date getFechaRegistroSacee() {
        return fechaRegistroSacee;
    }

    public void setFechaRegistroSacee(Date fechaRegistroSacee) {
        this.fechaRegistroSacee = fechaRegistroSacee;
    }

    public String getDetalleSacee() {
        return detalleSacee;
    }

    public void setDetalleSacee(String detalleSacee) {
        this.detalleSacee = detalleSacee;
    }

    public boolean getActivoSacee() {
        return activoSacee;
    }

    public void setActivoSacee(boolean activoSacee) {
        this.activoSacee = activoSacee;
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

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public List<SaoDetalleCertExterno> getSaoDetalleCertExternoList() {
        return saoDetalleCertExternoList;
    }

    public void setSaoDetalleCertExternoList(List<SaoDetalleCertExterno> saoDetalleCertExternoList) {
        this.saoDetalleCertExternoList = saoDetalleCertExternoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSacee != null ? ideSacee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoCertificadoExterno)) {
            return false;
        }
        SaoCertificadoExterno other = (SaoCertificadoExterno) object;
        if ((this.ideSacee == null && other.ideSacee != null) || (this.ideSacee != null && !this.ideSacee.equals(other.ideSacee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoCertificadoExterno[ ideSacee=" + ideSacee + " ]";
    }
    
}
