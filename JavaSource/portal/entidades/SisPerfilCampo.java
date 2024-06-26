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
@Table(name = "sis_perfil_campo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisPerfilCampo.findAll", query = "SELECT s FROM SisPerfilCampo s"),
    @NamedQuery(name = "SisPerfilCampo.findByIdePeca", query = "SELECT s FROM SisPerfilCampo s WHERE s.idePeca = :idePeca"),
    @NamedQuery(name = "SisPerfilCampo.findByLecturaPeca", query = "SELECT s FROM SisPerfilCampo s WHERE s.lecturaPeca = :lecturaPeca"),
    @NamedQuery(name = "SisPerfilCampo.findByVisiblePeca", query = "SELECT s FROM SisPerfilCampo s WHERE s.visiblePeca = :visiblePeca"),
    @NamedQuery(name = "SisPerfilCampo.findByDefectoPeca", query = "SELECT s FROM SisPerfilCampo s WHERE s.defectoPeca = :defectoPeca"),
    @NamedQuery(name = "SisPerfilCampo.findByUsuarioIngre", query = "SELECT s FROM SisPerfilCampo s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisPerfilCampo.findByFechaIngre", query = "SELECT s FROM SisPerfilCampo s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisPerfilCampo.findByUsuarioActua", query = "SELECT s FROM SisPerfilCampo s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisPerfilCampo.findByFechaActua", query = "SELECT s FROM SisPerfilCampo s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisPerfilCampo.findByHoraIngre", query = "SELECT s FROM SisPerfilCampo s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisPerfilCampo.findByHoraActua", query = "SELECT s FROM SisPerfilCampo s WHERE s.horaActua = :horaActua")})
public class SisPerfilCampo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_peca", nullable = false)
    private Integer idePeca;
    @Column(name = "lectura_peca")
    private Short lecturaPeca;
    @Column(name = "visible_peca")
    private Short visiblePeca;
    @Size(max = 100)
    @Column(name = "defecto_peca", length = 100)
    private String defectoPeca;
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
    @JoinColumn(name = "ide_perf", referencedColumnName = "ide_perf")
    @ManyToOne
    private SisPerfil idePerf;
    @JoinColumn(name = "ide_camp", referencedColumnName = "ide_camp")
    @ManyToOne
    private SisCampo ideCamp;

    public SisPerfilCampo() {
    }

    public SisPerfilCampo(Integer idePeca) {
        this.idePeca = idePeca;
    }

    public Integer getIdePeca() {
        return idePeca;
    }

    public void setIdePeca(Integer idePeca) {
        this.idePeca = idePeca;
    }

    public Short getLecturaPeca() {
        return lecturaPeca;
    }

    public void setLecturaPeca(Short lecturaPeca) {
        this.lecturaPeca = lecturaPeca;
    }

    public Short getVisiblePeca() {
        return visiblePeca;
    }

    public void setVisiblePeca(Short visiblePeca) {
        this.visiblePeca = visiblePeca;
    }

    public String getDefectoPeca() {
        return defectoPeca;
    }

    public void setDefectoPeca(String defectoPeca) {
        this.defectoPeca = defectoPeca;
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

    public SisPerfil getIdePerf() {
        return idePerf;
    }

    public void setIdePerf(SisPerfil idePerf) {
        this.idePerf = idePerf;
    }

    public SisCampo getIdeCamp() {
        return ideCamp;
    }

    public void setIdeCamp(SisCampo ideCamp) {
        this.ideCamp = ideCamp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePeca != null ? idePeca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisPerfilCampo)) {
            return false;
        }
        SisPerfilCampo other = (SisPerfilCampo) object;
        if ((this.idePeca == null && other.idePeca != null) || (this.idePeca != null && !this.idePeca.equals(other.idePeca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisPerfilCampo[ idePeca=" + idePeca + " ]";
    }
    
}
