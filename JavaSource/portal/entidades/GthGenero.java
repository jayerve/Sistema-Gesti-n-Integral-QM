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
@Table(name = "gth_genero", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthGenero.findAll", query = "SELECT g FROM GthGenero g"),
    @NamedQuery(name = "GthGenero.findByIdeGtgen", query = "SELECT g FROM GthGenero g WHERE g.ideGtgen = :ideGtgen"),
    @NamedQuery(name = "GthGenero.findByDetalleGtgen", query = "SELECT g FROM GthGenero g WHERE g.detalleGtgen = :detalleGtgen"),
    @NamedQuery(name = "GthGenero.findByCodigoSbsGtgen", query = "SELECT g FROM GthGenero g WHERE g.codigoSbsGtgen = :codigoSbsGtgen"),
    @NamedQuery(name = "GthGenero.findByActivoGtgen", query = "SELECT g FROM GthGenero g WHERE g.activoGtgen = :activoGtgen"),
    @NamedQuery(name = "GthGenero.findByUsuarioIngre", query = "SELECT g FROM GthGenero g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthGenero.findByFechaIngre", query = "SELECT g FROM GthGenero g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthGenero.findByUsuarioActua", query = "SELECT g FROM GthGenero g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthGenero.findByFechaActua", query = "SELECT g FROM GthGenero g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthGenero.findByHoraIngre", query = "SELECT g FROM GthGenero g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthGenero.findByHoraActua", query = "SELECT g FROM GthGenero g WHERE g.horaActua = :horaActua")})
public class GthGenero implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtgen", nullable = false)
    private Integer ideGtgen;
    @Size(max = 50)
    @Column(name = "detalle_gtgen", length = 50)
    private String detalleGtgen;
    @Size(max = 50)
    @Column(name = "codigo_sbs_gtgen", length = 50)
    private String codigoSbsGtgen;
    @Column(name = "activo_gtgen")
    private Boolean activoGtgen;
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
    @OneToMany(mappedBy = "ideGtgen")
    private List<SbsArchivoOnce> sbsArchivoOnceList;
    @OneToMany(mappedBy = "ideGtgen")
    private List<SbsArchivoVeinteUno> sbsArchivoVeinteUnoList;
    @OneToMany(mappedBy = "ideGtgen")
    private List<GthConyuge> gthConyugeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtgen")
    private List<GthEmpleado> gthEmpleadoList;
    @OneToMany(mappedBy = "ideGtgen")
    private List<RecClientes> recClientesList;
    @OneToMany(mappedBy = "ideGtgen")
    private List<SprBasePostulante> sprBasePostulanteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtgen")
    private List<GthCargasFamiliares> gthCargasFamiliaresList;

    public GthGenero() {
    }

    public GthGenero(Integer ideGtgen) {
        this.ideGtgen = ideGtgen;
    }

    public Integer getIdeGtgen() {
        return ideGtgen;
    }

    public void setIdeGtgen(Integer ideGtgen) {
        this.ideGtgen = ideGtgen;
    }

    public String getDetalleGtgen() {
        return detalleGtgen;
    }

    public void setDetalleGtgen(String detalleGtgen) {
        this.detalleGtgen = detalleGtgen;
    }

    public String getCodigoSbsGtgen() {
        return codigoSbsGtgen;
    }

    public void setCodigoSbsGtgen(String codigoSbsGtgen) {
        this.codigoSbsGtgen = codigoSbsGtgen;
    }

    public Boolean getActivoGtgen() {
        return activoGtgen;
    }

    public void setActivoGtgen(Boolean activoGtgen) {
        this.activoGtgen = activoGtgen;
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

    public List<GthEmpleado> getGthEmpleadoList() {
        return gthEmpleadoList;
    }

    public void setGthEmpleadoList(List<GthEmpleado> gthEmpleadoList) {
        this.gthEmpleadoList = gthEmpleadoList;
    }

    public List<RecClientes> getRecClientesList() {
        return recClientesList;
    }

    public void setRecClientesList(List<RecClientes> recClientesList) {
        this.recClientesList = recClientesList;
    }

    public List<SprBasePostulante> getSprBasePostulanteList() {
        return sprBasePostulanteList;
    }

    public void setSprBasePostulanteList(List<SprBasePostulante> sprBasePostulanteList) {
        this.sprBasePostulanteList = sprBasePostulanteList;
    }

    public List<GthCargasFamiliares> getGthCargasFamiliaresList() {
        return gthCargasFamiliaresList;
    }

    public void setGthCargasFamiliaresList(List<GthCargasFamiliares> gthCargasFamiliaresList) {
        this.gthCargasFamiliaresList = gthCargasFamiliaresList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtgen != null ? ideGtgen.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthGenero)) {
            return false;
        }
        GthGenero other = (GthGenero) object;
        if ((this.ideGtgen == null && other.ideGtgen != null) || (this.ideGtgen != null && !this.ideGtgen.equals(other.ideGtgen))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthGenero[ ideGtgen=" + ideGtgen + " ]";
    }
    
}
