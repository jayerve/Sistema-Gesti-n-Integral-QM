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
@Table(name = "asi_valida_nomina", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AsiValidaNomina.findAll", query = "SELECT a FROM AsiValidaNomina a"),
    @NamedQuery(name = "AsiValidaNomina.findByIdeAsvno", query = "SELECT a FROM AsiValidaNomina a WHERE a.ideAsvno = :ideAsvno"),
    @NamedQuery(name = "AsiValidaNomina.findByFechaActualAsvno", query = "SELECT a FROM AsiValidaNomina a WHERE a.fechaActualAsvno = :fechaActualAsvno"),
    @NamedQuery(name = "AsiValidaNomina.findByUsuarioIngre", query = "SELECT a FROM AsiValidaNomina a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiValidaNomina.findByFechaIngre", query = "SELECT a FROM AsiValidaNomina a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiValidaNomina.findByUsuarioActua", query = "SELECT a FROM AsiValidaNomina a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiValidaNomina.findByFechaActua", query = "SELECT a FROM AsiValidaNomina a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiValidaNomina.findByHoraIngre", query = "SELECT a FROM AsiValidaNomina a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiValidaNomina.findByHoraActua", query = "SELECT a FROM AsiValidaNomina a WHERE a.horaActua = :horaActua")})
public class AsiValidaNomina implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_asvno", nullable = false)
    private Integer ideAsvno;
    @Column(name = "fecha_actual_asvno")
    @Temporal(TemporalType.DATE)
    private Date fechaActualAsvno;
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
    @JoinColumn(name = "ide_nrrol", referencedColumnName = "ide_nrrol")
    @ManyToOne
    private NrhRol ideNrrol;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "ide_asdhe", referencedColumnName = "ide_asdhe")
    @ManyToOne
    private AsiDetalleHorasExtras ideAsdhe;

    public AsiValidaNomina() {
    }

    public AsiValidaNomina(Integer ideAsvno) {
        this.ideAsvno = ideAsvno;
    }

    public Integer getIdeAsvno() {
        return ideAsvno;
    }

    public void setIdeAsvno(Integer ideAsvno) {
        this.ideAsvno = ideAsvno;
    }

    public Date getFechaActualAsvno() {
        return fechaActualAsvno;
    }

    public void setFechaActualAsvno(Date fechaActualAsvno) {
        this.fechaActualAsvno = fechaActualAsvno;
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

    public NrhRol getIdeNrrol() {
        return ideNrrol;
    }

    public void setIdeNrrol(NrhRol ideNrrol) {
        this.ideNrrol = ideNrrol;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public AsiDetalleHorasExtras getIdeAsdhe() {
        return ideAsdhe;
    }

    public void setIdeAsdhe(AsiDetalleHorasExtras ideAsdhe) {
        this.ideAsdhe = ideAsdhe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsvno != null ? ideAsvno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiValidaNomina)) {
            return false;
        }
        AsiValidaNomina other = (AsiValidaNomina) object;
        if ((this.ideAsvno == null && other.ideAsvno != null) || (this.ideAsvno != null && !this.ideAsvno.equals(other.ideAsvno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiValidaNomina[ ideAsvno=" + ideAsvno + " ]";
    }
    
}
