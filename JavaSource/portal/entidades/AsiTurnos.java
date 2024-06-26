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
@Table(name = "asi_turnos", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AsiTurnos.findAll", query = "SELECT a FROM AsiTurnos a"),
    @NamedQuery(name = "AsiTurnos.findByIdeAstur", query = "SELECT a FROM AsiTurnos a WHERE a.ideAstur = :ideAstur"),
    @NamedQuery(name = "AsiTurnos.findByMinutoToleranciaAstur", query = "SELECT a FROM AsiTurnos a WHERE a.minutoToleranciaAstur = :minutoToleranciaAstur"),
    @NamedQuery(name = "AsiTurnos.findByActivoAstur", query = "SELECT a FROM AsiTurnos a WHERE a.activoAstur = :activoAstur"),
    @NamedQuery(name = "AsiTurnos.findByUsuarioIngre", query = "SELECT a FROM AsiTurnos a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiTurnos.findByFechaIngre", query = "SELECT a FROM AsiTurnos a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiTurnos.findByUsuarioActua", query = "SELECT a FROM AsiTurnos a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiTurnos.findByFechaActua", query = "SELECT a FROM AsiTurnos a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiTurnos.findByHoraIngre", query = "SELECT a FROM AsiTurnos a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiTurnos.findByHoraActua", query = "SELECT a FROM AsiTurnos a WHERE a.horaActua = :horaActua")})
public class AsiTurnos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_astur", nullable = false)
    private Integer ideAstur;
    @Column(name = "minuto_tolerancia_astur")
    private Integer minutoToleranciaAstur;
    @Column(name = "activo_astur")
    private Boolean activoAstur;
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
    @JoinColumn(name = "ide_gtgre", referencedColumnName = "ide_gtgre")
    @ManyToOne
    private GthGrupoEmpleado ideGtgre;
    @JoinColumn(name = "ide_ashor", referencedColumnName = "ide_ashor")
    @ManyToOne
    private AsiHorario ideAshor;

    public AsiTurnos() {
    }

    public AsiTurnos(Integer ideAstur) {
        this.ideAstur = ideAstur;
    }

    public Integer getIdeAstur() {
        return ideAstur;
    }

    public void setIdeAstur(Integer ideAstur) {
        this.ideAstur = ideAstur;
    }

    public Integer getMinutoToleranciaAstur() {
        return minutoToleranciaAstur;
    }

    public void setMinutoToleranciaAstur(Integer minutoToleranciaAstur) {
        this.minutoToleranciaAstur = minutoToleranciaAstur;
    }

    public Boolean getActivoAstur() {
        return activoAstur;
    }

    public void setActivoAstur(Boolean activoAstur) {
        this.activoAstur = activoAstur;
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

    public GthGrupoEmpleado getIdeGtgre() {
        return ideGtgre;
    }

    public void setIdeGtgre(GthGrupoEmpleado ideGtgre) {
        this.ideGtgre = ideGtgre;
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
        hash += (ideAstur != null ? ideAstur.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiTurnos)) {
            return false;
        }
        AsiTurnos other = (AsiTurnos) object;
        if ((this.ideAstur == null && other.ideAstur != null) || (this.ideAstur != null && !this.ideAstur.equals(other.ideAstur))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiTurnos[ ideAstur=" + ideAstur + " ]";
    }
    
}
