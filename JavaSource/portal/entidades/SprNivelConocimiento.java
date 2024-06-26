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
@Table(name = "spr_nivel_conocimiento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprNivelConocimiento.findAll", query = "SELECT s FROM SprNivelConocimiento s"),
    @NamedQuery(name = "SprNivelConocimiento.findByIdeSpnic", query = "SELECT s FROM SprNivelConocimiento s WHERE s.ideSpnic = :ideSpnic"),
    @NamedQuery(name = "SprNivelConocimiento.findByDetalleSpnic", query = "SELECT s FROM SprNivelConocimiento s WHERE s.detalleSpnic = :detalleSpnic"),
    @NamedQuery(name = "SprNivelConocimiento.findByActivoSpest", query = "SELECT s FROM SprNivelConocimiento s WHERE s.activoSpest = :activoSpest"),
    @NamedQuery(name = "SprNivelConocimiento.findByUsuarioIngre", query = "SELECT s FROM SprNivelConocimiento s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprNivelConocimiento.findByFechaIngre", query = "SELECT s FROM SprNivelConocimiento s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprNivelConocimiento.findByHoraIngre", query = "SELECT s FROM SprNivelConocimiento s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprNivelConocimiento.findByUsuarioActua", query = "SELECT s FROM SprNivelConocimiento s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprNivelConocimiento.findByFechaActua", query = "SELECT s FROM SprNivelConocimiento s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprNivelConocimiento.findByHoraActua", query = "SELECT s FROM SprNivelConocimiento s WHERE s.horaActua = :horaActua")})
public class SprNivelConocimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spnic", nullable = false)
    private Integer ideSpnic;
    @Size(max = 100)
    @Column(name = "detalle_spnic", length = 100)
    private String detalleSpnic;
    @Column(name = "activo_spest")
    private Boolean activoSpest;
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
    @OneToMany(mappedBy = "ideSpnic")
    private List<SprInformatica> sprInformaticaList;

    public SprNivelConocimiento() {
    }

    public SprNivelConocimiento(Integer ideSpnic) {
        this.ideSpnic = ideSpnic;
    }

    public Integer getIdeSpnic() {
        return ideSpnic;
    }

    public void setIdeSpnic(Integer ideSpnic) {
        this.ideSpnic = ideSpnic;
    }

    public String getDetalleSpnic() {
        return detalleSpnic;
    }

    public void setDetalleSpnic(String detalleSpnic) {
        this.detalleSpnic = detalleSpnic;
    }

    public Boolean getActivoSpest() {
        return activoSpest;
    }

    public void setActivoSpest(Boolean activoSpest) {
        this.activoSpest = activoSpest;
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

    public List<SprInformatica> getSprInformaticaList() {
        return sprInformaticaList;
    }

    public void setSprInformaticaList(List<SprInformatica> sprInformaticaList) {
        this.sprInformaticaList = sprInformaticaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpnic != null ? ideSpnic.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprNivelConocimiento)) {
            return false;
        }
        SprNivelConocimiento other = (SprNivelConocimiento) object;
        if ((this.ideSpnic == null && other.ideSpnic != null) || (this.ideSpnic != null && !this.ideSpnic.equals(other.ideSpnic))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprNivelConocimiento[ ideSpnic=" + ideSpnic + " ]";
    }
    
}
