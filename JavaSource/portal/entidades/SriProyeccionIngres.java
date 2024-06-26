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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "sri_proyeccion_ingres", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SriProyeccionIngres.findAll", query = "SELECT s FROM SriProyeccionIngres s"),
    @NamedQuery(name = "SriProyeccionIngres.findByIdeSrpri", query = "SELECT s FROM SriProyeccionIngres s WHERE s.ideSrpri = :ideSrpri"),
    @NamedQuery(name = "SriProyeccionIngres.findByIdeGemes", query = "SELECT s FROM SriProyeccionIngres s WHERE s.ideGemes = :ideGemes"),
    @NamedQuery(name = "SriProyeccionIngres.findByActivoSrpri", query = "SELECT s FROM SriProyeccionIngres s WHERE s.activoSrpri = :activoSrpri"),
    @NamedQuery(name = "SriProyeccionIngres.findByUsuarioIngre", query = "SELECT s FROM SriProyeccionIngres s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SriProyeccionIngres.findByFechaIngre", query = "SELECT s FROM SriProyeccionIngres s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SriProyeccionIngres.findByUsuarioActua", query = "SELECT s FROM SriProyeccionIngres s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SriProyeccionIngres.findByFechaActua", query = "SELECT s FROM SriProyeccionIngres s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SriProyeccionIngres.findByHoraIngre", query = "SELECT s FROM SriProyeccionIngres s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SriProyeccionIngres.findByHoraActua", query = "SELECT s FROM SriProyeccionIngres s WHERE s.horaActua = :horaActua")})
public class SriProyeccionIngres implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_srpri", nullable = false)
    private Integer ideSrpri;
    @Column(name = "ide_gemes")
    private Integer ideGemes;
    @Column(name = "activo_srpri")
    private Boolean activoSrpri;
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
    @JoinColumn(name = "ide_srimr", referencedColumnName = "ide_srimr")
    @ManyToOne
    private SriImpuestoRenta ideSrimr;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @OneToMany(mappedBy = "ideSrpri")
    private List<SriDetalleProyecccionIngres> sriDetalleProyecccionIngresList;

    public SriProyeccionIngres() {
    }

    public SriProyeccionIngres(Integer ideSrpri) {
        this.ideSrpri = ideSrpri;
    }

    public Integer getIdeSrpri() {
        return ideSrpri;
    }

    public void setIdeSrpri(Integer ideSrpri) {
        this.ideSrpri = ideSrpri;
    }

    public Integer getIdeGemes() {
        return ideGemes;
    }

    public void setIdeGemes(Integer ideGemes) {
        this.ideGemes = ideGemes;
    }

    public Boolean getActivoSrpri() {
        return activoSrpri;
    }

    public void setActivoSrpri(Boolean activoSrpri) {
        this.activoSrpri = activoSrpri;
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

    public SriImpuestoRenta getIdeSrimr() {
        return ideSrimr;
    }

    public void setIdeSrimr(SriImpuestoRenta ideSrimr) {
        this.ideSrimr = ideSrimr;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public List<SriDetalleProyecccionIngres> getSriDetalleProyecccionIngresList() {
        return sriDetalleProyecccionIngresList;
    }

    public void setSriDetalleProyecccionIngresList(List<SriDetalleProyecccionIngres> sriDetalleProyecccionIngresList) {
        this.sriDetalleProyecccionIngresList = sriDetalleProyecccionIngresList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSrpri != null ? ideSrpri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SriProyeccionIngres)) {
            return false;
        }
        SriProyeccionIngres other = (SriProyeccionIngres) object;
        if ((this.ideSrpri == null && other.ideSrpri != null) || (this.ideSrpri != null && !this.ideSrpri.equals(other.ideSrpri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SriProyeccionIngres[ ideSrpri=" + ideSrpri + " ]";
    }
    
}
