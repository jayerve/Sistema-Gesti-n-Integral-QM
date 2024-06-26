/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "gth_nacionalidad", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthNacionalidad.findAll", query = "SELECT g FROM GthNacionalidad g"),
    @NamedQuery(name = "GthNacionalidad.findByIdeGtnac", query = "SELECT g FROM GthNacionalidad g WHERE g.ideGtnac = :ideGtnac"),
    @NamedQuery(name = "GthNacionalidad.findByDetalleGtnac", query = "SELECT g FROM GthNacionalidad g WHERE g.detalleGtnac = :detalleGtnac"),
    @NamedQuery(name = "GthNacionalidad.findByCodigoSbsGtnac", query = "SELECT g FROM GthNacionalidad g WHERE g.codigoSbsGtnac = :codigoSbsGtnac"),
    @NamedQuery(name = "GthNacionalidad.findByActivoGtnac", query = "SELECT g FROM GthNacionalidad g WHERE g.activoGtnac = :activoGtnac"),
    @NamedQuery(name = "GthNacionalidad.findByUsuarioIngre", query = "SELECT g FROM GthNacionalidad g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthNacionalidad.findByFechaIngre", query = "SELECT g FROM GthNacionalidad g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthNacionalidad.findByUsuarioActua", query = "SELECT g FROM GthNacionalidad g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthNacionalidad.findByFechaActua", query = "SELECT g FROM GthNacionalidad g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthNacionalidad.findByHoraIngre", query = "SELECT g FROM GthNacionalidad g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthNacionalidad.findByHoraActua", query = "SELECT g FROM GthNacionalidad g WHERE g.horaActua = :horaActua")})
public class GthNacionalidad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtnac", nullable = false)
    private Integer ideGtnac;
    @Size(max = 50)
    @Column(name = "detalle_gtnac", length = 50)
    private String detalleGtnac;
    @Size(max = 50)
    @Column(name = "codigo_sbs_gtnac", length = 50)
    private String codigoSbsGtnac;
    @Column(name = "activo_gtnac")
    private Boolean activoGtnac;
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
    @OneToMany(mappedBy = "ideGtnac")
    private List<SbsArchivoOnce> sbsArchivoOnceList;
    @OneToMany(mappedBy = "ideGtnac")
    private List<SbsArchivoVeinteUno> sbsArchivoVeinteUnoList;
    @OneToMany(mappedBy = "ideGtnac")
    private List<GthConyuge> gthConyugeList;
    @OneToMany(mappedBy = "ideGtnac")
    private List<GthParticipaNegocioEmplea> gthParticipaNegocioEmpleaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtnac")
    private List<GthEmpleado> gthEmpleadoList;
    @OneToMany(mappedBy = "ideGtnac")
    private List<SprBasePostulante> sprBasePostulanteList;

    public GthNacionalidad() {
    }

    public GthNacionalidad(Integer ideGtnac) {
        this.ideGtnac = ideGtnac;
    }

    public Integer getIdeGtnac() {
        return ideGtnac;
    }

    public void setIdeGtnac(Integer ideGtnac) {
        this.ideGtnac = ideGtnac;
    }

    public String getDetalleGtnac() {
        return detalleGtnac;
    }

    public void setDetalleGtnac(String detalleGtnac) {
        this.detalleGtnac = detalleGtnac;
    }

    public String getCodigoSbsGtnac() {
        return codigoSbsGtnac;
    }

    public void setCodigoSbsGtnac(String codigoSbsGtnac) {
        this.codigoSbsGtnac = codigoSbsGtnac;
    }

    public Boolean getActivoGtnac() {
        return activoGtnac;
    }

    public void setActivoGtnac(Boolean activoGtnac) {
        this.activoGtnac = activoGtnac;
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

    public List<SbsArchivoOnce> getSbsArchivoOnceList() {
        return sbsArchivoOnceList;
    }

    public void setSbsArchivoOnceList(List<SbsArchivoOnce> sbsArchivoOnceList) {
        this.sbsArchivoOnceList = sbsArchivoOnceList;
    }

    public List<SbsArchivoVeinteUno> getSbsArchivoVeinteUnoList() {
        return sbsArchivoVeinteUnoList;
    }

    public void setSbsArchivoVeinteUnoList(List<SbsArchivoVeinteUno> sbsArchivoVeinteUnoList) {
        this.sbsArchivoVeinteUnoList = sbsArchivoVeinteUnoList;
    }

    public List<GthConyuge> getGthConyugeList() {
        return gthConyugeList;
    }

    public void setGthConyugeList(List<GthConyuge> gthConyugeList) {
        this.gthConyugeList = gthConyugeList;
    }

    public List<GthParticipaNegocioEmplea> getGthParticipaNegocioEmpleaList() {
        return gthParticipaNegocioEmpleaList;
    }

    public void setGthParticipaNegocioEmpleaList(List<GthParticipaNegocioEmplea> gthParticipaNegocioEmpleaList) {
        this.gthParticipaNegocioEmpleaList = gthParticipaNegocioEmpleaList;
    }

    public List<GthEmpleado> getGthEmpleadoList() {
        return gthEmpleadoList;
    }

    public void setGthEmpleadoList(List<GthEmpleado> gthEmpleadoList) {
        this.gthEmpleadoList = gthEmpleadoList;
    }

    public List<SprBasePostulante> getSprBasePostulanteList() {
        return sprBasePostulanteList;
    }

    public void setSprBasePostulanteList(List<SprBasePostulante> sprBasePostulanteList) {
        this.sprBasePostulanteList = sprBasePostulanteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtnac != null ? ideGtnac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthNacionalidad)) {
            return false;
        }
        GthNacionalidad other = (GthNacionalidad) object;
        if ((this.ideGtnac == null && other.ideGtnac != null) || (this.ideGtnac != null && !this.ideGtnac.equals(other.ideGtnac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthNacionalidad[ ideGtnac=" + ideGtnac + " ]";
    }
    
}
