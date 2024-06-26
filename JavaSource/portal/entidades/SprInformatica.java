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
@Table(name = "spr_informatica", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprInformatica.findAll", query = "SELECT s FROM SprInformatica s"),
    @NamedQuery(name = "SprInformatica.findByIdeSpinf", query = "SELECT s FROM SprInformatica s WHERE s.ideSpinf = :ideSpinf"),
    @NamedQuery(name = "SprInformatica.findByActivoSpinf", query = "SELECT s FROM SprInformatica s WHERE s.activoSpinf = :activoSpinf"),
    @NamedQuery(name = "SprInformatica.findByUsuarioIngre", query = "SELECT s FROM SprInformatica s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprInformatica.findByFechaIngre", query = "SELECT s FROM SprInformatica s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprInformatica.findByHoraIngre", query = "SELECT s FROM SprInformatica s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprInformatica.findByUsuarioActua", query = "SELECT s FROM SprInformatica s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprInformatica.findByFechaActua", query = "SELECT s FROM SprInformatica s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprInformatica.findByHoraActua", query = "SELECT s FROM SprInformatica s WHERE s.horaActua = :horaActua")})
public class SprInformatica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spinf", nullable = false)
    private Integer ideSpinf;
    @Column(name = "activo_spinf")
    private Boolean activoSpinf;
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
    @JoinColumn(name = "ide_spnic", referencedColumnName = "ide_spnic")
    @ManyToOne
    private SprNivelConocimiento ideSpnic;
    @JoinColumn(name = "ide_spbap", referencedColumnName = "ide_spbap")
    @ManyToOne
    private SprBasePostulante ideSpbap;

    public SprInformatica() {
    }

    public SprInformatica(Integer ideSpinf) {
        this.ideSpinf = ideSpinf;
    }

    public Integer getIdeSpinf() {
        return ideSpinf;
    }

    public void setIdeSpinf(Integer ideSpinf) {
        this.ideSpinf = ideSpinf;
    }

    public Boolean getActivoSpinf() {
        return activoSpinf;
    }

    public void setActivoSpinf(Boolean activoSpinf) {
        this.activoSpinf = activoSpinf;
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

    public SprNivelConocimiento getIdeSpnic() {
        return ideSpnic;
    }

    public void setIdeSpnic(SprNivelConocimiento ideSpnic) {
        this.ideSpnic = ideSpnic;
    }

    public SprBasePostulante getIdeSpbap() {
        return ideSpbap;
    }

    public void setIdeSpbap(SprBasePostulante ideSpbap) {
        this.ideSpbap = ideSpbap;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpinf != null ? ideSpinf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprInformatica)) {
            return false;
        }
        SprInformatica other = (SprInformatica) object;
        if ((this.ideSpinf == null && other.ideSpinf != null) || (this.ideSpinf != null && !this.ideSpinf.equals(other.ideSpinf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprInformatica[ ideSpinf=" + ideSpinf + " ]";
    }
    
}
