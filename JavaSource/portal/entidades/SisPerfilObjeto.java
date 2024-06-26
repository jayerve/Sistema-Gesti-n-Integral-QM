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
@Table(name = "sis_perfil_objeto", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisPerfilObjeto.findAll", query = "SELECT s FROM SisPerfilObjeto s"),
    @NamedQuery(name = "SisPerfilObjeto.findByIdePeob", query = "SELECT s FROM SisPerfilObjeto s WHERE s.idePeob = :idePeob"),
    @NamedQuery(name = "SisPerfilObjeto.findByUsuarioIngre", query = "SELECT s FROM SisPerfilObjeto s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisPerfilObjeto.findByFechaIngre", query = "SELECT s FROM SisPerfilObjeto s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisPerfilObjeto.findByUsuarioActua", query = "SELECT s FROM SisPerfilObjeto s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisPerfilObjeto.findByFechaActua", query = "SELECT s FROM SisPerfilObjeto s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisPerfilObjeto.findByHoraIngre", query = "SELECT s FROM SisPerfilObjeto s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisPerfilObjeto.findByHoraActua", query = "SELECT s FROM SisPerfilObjeto s WHERE s.horaActua = :horaActua"),
    @NamedQuery(name = "SisPerfilObjeto.findByLecturaPeob", query = "SELECT s FROM SisPerfilObjeto s WHERE s.lecturaPeob = :lecturaPeob"),
    @NamedQuery(name = "SisPerfilObjeto.findByVisiblePeob", query = "SELECT s FROM SisPerfilObjeto s WHERE s.visiblePeob = :visiblePeob")})
public class SisPerfilObjeto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_peob", nullable = false)
    private Integer idePeob;
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
    @Column(name = "lectura_peob")
    private Boolean lecturaPeob;
    @Column(name = "visible_peob")
    private Boolean visiblePeob;
    @JoinColumn(name = "ide_perf", referencedColumnName = "ide_perf")
    @ManyToOne
    private SisPerfil idePerf;
    @JoinColumn(name = "ide_obop", referencedColumnName = "ide_obop")
    @ManyToOne
    private SisObjetoOpcion ideObop;

    public SisPerfilObjeto() {
    }

    public SisPerfilObjeto(Integer idePeob) {
        this.idePeob = idePeob;
    }

    public Integer getIdePeob() {
        return idePeob;
    }

    public void setIdePeob(Integer idePeob) {
        this.idePeob = idePeob;
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

    public Boolean getLecturaPeob() {
        return lecturaPeob;
    }

    public void setLecturaPeob(Boolean lecturaPeob) {
        this.lecturaPeob = lecturaPeob;
    }

    public Boolean getVisiblePeob() {
        return visiblePeob;
    }

    public void setVisiblePeob(Boolean visiblePeob) {
        this.visiblePeob = visiblePeob;
    }

    public SisPerfil getIdePerf() {
        return idePerf;
    }

    public void setIdePerf(SisPerfil idePerf) {
        this.idePerf = idePerf;
    }

    public SisObjetoOpcion getIdeObop() {
        return ideObop;
    }

    public void setIdeObop(SisObjetoOpcion ideObop) {
        this.ideObop = ideObop;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePeob != null ? idePeob.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisPerfilObjeto)) {
            return false;
        }
        SisPerfilObjeto other = (SisPerfilObjeto) object;
        if ((this.idePeob == null && other.idePeob != null) || (this.idePeob != null && !this.idePeob.equals(other.idePeob))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisPerfilObjeto[ idePeob=" + idePeob + " ]";
    }
    
}
