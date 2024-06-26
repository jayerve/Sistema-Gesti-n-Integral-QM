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
@Table(name = "sao_motivo_consulta", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoMotivoConsulta.findAll", query = "SELECT s FROM SaoMotivoConsulta s"),
    @NamedQuery(name = "SaoMotivoConsulta.findByIdeSamoc", query = "SELECT s FROM SaoMotivoConsulta s WHERE s.ideSamoc = :ideSamoc"),
    @NamedQuery(name = "SaoMotivoConsulta.findByDetalleSamoc", query = "SELECT s FROM SaoMotivoConsulta s WHERE s.detalleSamoc = :detalleSamoc"),
    @NamedQuery(name = "SaoMotivoConsulta.findByActivoSamoc", query = "SELECT s FROM SaoMotivoConsulta s WHERE s.activoSamoc = :activoSamoc"),
    @NamedQuery(name = "SaoMotivoConsulta.findByUsuarioIngre", query = "SELECT s FROM SaoMotivoConsulta s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoMotivoConsulta.findByFechaIngre", query = "SELECT s FROM SaoMotivoConsulta s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoMotivoConsulta.findByUsuarioActua", query = "SELECT s FROM SaoMotivoConsulta s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoMotivoConsulta.findByFechaActua", query = "SELECT s FROM SaoMotivoConsulta s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoMotivoConsulta.findByHoraIngre", query = "SELECT s FROM SaoMotivoConsulta s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoMotivoConsulta.findByHoraActua", query = "SELECT s FROM SaoMotivoConsulta s WHERE s.horaActua = :horaActua")})
public class SaoMotivoConsulta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_samoc", nullable = false)
    private Integer ideSamoc;
    @Size(max = 100)
    @Column(name = "detalle_samoc", length = 100)
    private String detalleSamoc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_samoc", nullable = false)
    private boolean activoSamoc;
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
    @OneToMany(mappedBy = "ideSamoc")
    private List<SaoFichaMotivoConsulta> saoFichaMotivoConsultaList;

    public SaoMotivoConsulta() {
    }

    public SaoMotivoConsulta(Integer ideSamoc) {
        this.ideSamoc = ideSamoc;
    }

    public SaoMotivoConsulta(Integer ideSamoc, boolean activoSamoc) {
        this.ideSamoc = ideSamoc;
        this.activoSamoc = activoSamoc;
    }

    public Integer getIdeSamoc() {
        return ideSamoc;
    }

    public void setIdeSamoc(Integer ideSamoc) {
        this.ideSamoc = ideSamoc;
    }

    public String getDetalleSamoc() {
        return detalleSamoc;
    }

    public void setDetalleSamoc(String detalleSamoc) {
        this.detalleSamoc = detalleSamoc;
    }

    public boolean getActivoSamoc() {
        return activoSamoc;
    }

    public void setActivoSamoc(boolean activoSamoc) {
        this.activoSamoc = activoSamoc;
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

    public List<SaoFichaMotivoConsulta> getSaoFichaMotivoConsultaList() {
        return saoFichaMotivoConsultaList;
    }

    public void setSaoFichaMotivoConsultaList(List<SaoFichaMotivoConsulta> saoFichaMotivoConsultaList) {
        this.saoFichaMotivoConsultaList = saoFichaMotivoConsultaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSamoc != null ? ideSamoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoMotivoConsulta)) {
            return false;
        }
        SaoMotivoConsulta other = (SaoMotivoConsulta) object;
        if ((this.ideSamoc == null && other.ideSamoc != null) || (this.ideSamoc != null && !this.ideSamoc.equals(other.ideSamoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoMotivoConsulta[ ideSamoc=" + ideSamoc + " ]";
    }
    
}
