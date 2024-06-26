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
@Table(name = "sis_perfil_opcion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisPerfilOpcion.findAll", query = "SELECT s FROM SisPerfilOpcion s"),
    @NamedQuery(name = "SisPerfilOpcion.findByIdePeop", query = "SELECT s FROM SisPerfilOpcion s WHERE s.idePeop = :idePeop"),
    @NamedQuery(name = "SisPerfilOpcion.findByUsuarioIngre", query = "SELECT s FROM SisPerfilOpcion s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisPerfilOpcion.findByFechaIngre", query = "SELECT s FROM SisPerfilOpcion s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisPerfilOpcion.findByUsuarioActua", query = "SELECT s FROM SisPerfilOpcion s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisPerfilOpcion.findByFechaActua", query = "SELECT s FROM SisPerfilOpcion s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisPerfilOpcion.findByHoraIngre", query = "SELECT s FROM SisPerfilOpcion s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisPerfilOpcion.findByHoraActua", query = "SELECT s FROM SisPerfilOpcion s WHERE s.horaActua = :horaActua"),
    @NamedQuery(name = "SisPerfilOpcion.findByLecturaPeop", query = "SELECT s FROM SisPerfilOpcion s WHERE s.lecturaPeop = :lecturaPeop")})
public class SisPerfilOpcion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_peop", nullable = false)
    private Integer idePeop;
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
    @Column(name = "lectura_peop")
    private Boolean lecturaPeop;
    @JoinColumn(name = "ide_perf", referencedColumnName = "ide_perf")
    @ManyToOne
    private SisPerfil idePerf;
    @JoinColumn(name = "ide_opci", referencedColumnName = "ide_opci")
    @ManyToOne
    private SisOpcion ideOpci;

    public SisPerfilOpcion() {
    }

    public SisPerfilOpcion(Integer idePeop) {
        this.idePeop = idePeop;
    }

    public Integer getIdePeop() {
        return idePeop;
    }

    public void setIdePeop(Integer idePeop) {
        this.idePeop = idePeop;
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

    public Boolean getLecturaPeop() {
        return lecturaPeop;
    }

    public void setLecturaPeop(Boolean lecturaPeop) {
        this.lecturaPeop = lecturaPeop;
    }

    public SisPerfil getIdePerf() {
        return idePerf;
    }

    public void setIdePerf(SisPerfil idePerf) {
        this.idePerf = idePerf;
    }

    public SisOpcion getIdeOpci() {
        return ideOpci;
    }

    public void setIdeOpci(SisOpcion ideOpci) {
        this.ideOpci = ideOpci;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePeop != null ? idePeop.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisPerfilOpcion)) {
            return false;
        }
        SisPerfilOpcion other = (SisPerfilOpcion) object;
        if ((this.idePeop == null && other.idePeop != null) || (this.idePeop != null && !this.idePeop.equals(other.idePeop))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisPerfilOpcion[ idePeop=" + idePeop + " ]";
    }
    
}
