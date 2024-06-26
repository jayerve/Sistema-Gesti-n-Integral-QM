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
@Table(name = "asi_dia_horario", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AsiDiaHorario.findAll", query = "SELECT a FROM AsiDiaHorario a"),
    @NamedQuery(name = "AsiDiaHorario.findByIdeAsdih", query = "SELECT a FROM AsiDiaHorario a WHERE a.ideAsdih = :ideAsdih"),
    @NamedQuery(name = "AsiDiaHorario.findByActivoAsdih", query = "SELECT a FROM AsiDiaHorario a WHERE a.activoAsdih = :activoAsdih"),
    @NamedQuery(name = "AsiDiaHorario.findByUsuarioIngre", query = "SELECT a FROM AsiDiaHorario a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiDiaHorario.findByFechaIngre", query = "SELECT a FROM AsiDiaHorario a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiDiaHorario.findByUsuarioActua", query = "SELECT a FROM AsiDiaHorario a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiDiaHorario.findByFechaActua", query = "SELECT a FROM AsiDiaHorario a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiDiaHorario.findByHoraActua", query = "SELECT a FROM AsiDiaHorario a WHERE a.horaActua = :horaActua"),
    @NamedQuery(name = "AsiDiaHorario.findByHoraIngre", query = "SELECT a FROM AsiDiaHorario a WHERE a.horaIngre = :horaIngre")})
public class AsiDiaHorario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_asdih", nullable = false)
    private Integer ideAsdih;
    @Column(name = "activo_asdih")
    private Boolean activoAsdih;
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
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @JoinColumn(name = "ide_gedia", referencedColumnName = "ide_gedia")
    @ManyToOne
    private GenDias ideGedia;
    @JoinColumn(name = "ide_ashor", referencedColumnName = "ide_ashor")
    @ManyToOne
    private AsiHorario ideAshor;

    public AsiDiaHorario() {
    }

    public AsiDiaHorario(Integer ideAsdih) {
        this.ideAsdih = ideAsdih;
    }

    public Integer getIdeAsdih() {
        return ideAsdih;
    }

    public void setIdeAsdih(Integer ideAsdih) {
        this.ideAsdih = ideAsdih;
    }

    public Boolean getActivoAsdih() {
        return activoAsdih;
    }

    public void setActivoAsdih(Boolean activoAsdih) {
        this.activoAsdih = activoAsdih;
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

    public GenDias getIdeGedia() {
        return ideGedia;
    }

    public void setIdeGedia(GenDias ideGedia) {
        this.ideGedia = ideGedia;
    }

    public AsiHorario getIdeAshor() {
        return ideAshor;
    }

    public void setIdeAshor(AsiHorario ideAshor) {
        this.ideAshor = ideAshor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsdih != null ? ideAsdih.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiDiaHorario)) {
            return false;
        }
        AsiDiaHorario other = (AsiDiaHorario) object;
        if ((this.ideAsdih == null && other.ideAsdih != null) || (this.ideAsdih != null && !this.ideAsdih.equals(other.ideAsdih))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiDiaHorario[ ideAsdih=" + ideAsdih + " ]";
    }
    
}
