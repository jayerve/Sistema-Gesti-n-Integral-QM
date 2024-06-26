/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
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
@Table(name = "crc_asistente_evento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CrcAsistenteEvento.findAll", query = "SELECT c FROM CrcAsistenteEvento c"),
    @NamedQuery(name = "CrcAsistenteEvento.findByIdeCrase", query = "SELECT c FROM CrcAsistenteEvento c WHERE c.ideCrase = :ideCrase"),
    @NamedQuery(name = "CrcAsistenteEvento.findByApellidoNombreCrase", query = "SELECT c FROM CrcAsistenteEvento c WHERE c.apellidoNombreCrase = :apellidoNombreCrase"),
    @NamedQuery(name = "CrcAsistenteEvento.findByCorreoCrase", query = "SELECT c FROM CrcAsistenteEvento c WHERE c.correoCrase = :correoCrase"),
    @NamedQuery(name = "CrcAsistenteEvento.findByActivoCrase", query = "SELECT c FROM CrcAsistenteEvento c WHERE c.activoCrase = :activoCrase"),
    @NamedQuery(name = "CrcAsistenteEvento.findByUsuarioIngre", query = "SELECT c FROM CrcAsistenteEvento c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CrcAsistenteEvento.findByFechaIngre", query = "SELECT c FROM CrcAsistenteEvento c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CrcAsistenteEvento.findByHoraIngre", query = "SELECT c FROM CrcAsistenteEvento c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CrcAsistenteEvento.findByUsuarioActua", query = "SELECT c FROM CrcAsistenteEvento c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CrcAsistenteEvento.findByFechaActua", query = "SELECT c FROM CrcAsistenteEvento c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CrcAsistenteEvento.findByHoraActua", query = "SELECT c FROM CrcAsistenteEvento c WHERE c.horaActua = :horaActua")})
public class CrcAsistenteEvento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_crase", nullable = false)
    private Integer ideCrase;
    @Size(max = 100)
    @Column(name = "apellido_nombre_crase", length = 100)
    private String apellidoNombreCrase;
    @Size(max = 50)
    @Column(name = "correo_crase", length = 50)
    private String correoCrase;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_crase", nullable = false)
    private boolean activoCrase;
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
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_crdee", referencedColumnName = "ide_crdee")
    @ManyToOne
    private CrcDetalleEvento ideCrdee;

    public CrcAsistenteEvento() {
    }

    public CrcAsistenteEvento(Integer ideCrase) {
        this.ideCrase = ideCrase;
    }

    public CrcAsistenteEvento(Integer ideCrase, boolean activoCrase) {
        this.ideCrase = ideCrase;
        this.activoCrase = activoCrase;
    }

    public Integer getIdeCrase() {
        return ideCrase;
    }

    public void setIdeCrase(Integer ideCrase) {
        this.ideCrase = ideCrase;
    }

    public String getApellidoNombreCrase() {
        return apellidoNombreCrase;
    }

    public void setApellidoNombreCrase(String apellidoNombreCrase) {
        this.apellidoNombreCrase = apellidoNombreCrase;
    }

    public String getCorreoCrase() {
        return correoCrase;
    }

    public void setCorreoCrase(String correoCrase) {
        this.correoCrase = correoCrase;
    }

    public boolean getActivoCrase() {
        return activoCrase;
    }

    public void setActivoCrase(boolean activoCrase) {
        this.activoCrase = activoCrase;
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

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public CrcDetalleEvento getIdeCrdee() {
        return ideCrdee;
    }

    public void setIdeCrdee(CrcDetalleEvento ideCrdee) {
        this.ideCrdee = ideCrdee;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCrase != null ? ideCrase.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CrcAsistenteEvento)) {
            return false;
        }
        CrcAsistenteEvento other = (CrcAsistenteEvento) object;
        if ((this.ideCrase == null && other.ideCrase != null) || (this.ideCrase != null && !this.ideCrase.equals(other.ideCrase))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CrcAsistenteEvento[ ideCrase=" + ideCrase + " ]";
    }
    
}
