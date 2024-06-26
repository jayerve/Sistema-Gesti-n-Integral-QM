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
@Table(name = "crt_actividad", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CrtActividad.findAll", query = "SELECT c FROM CrtActividad c"),
    @NamedQuery(name = "CrtActividad.findByIdeCra", query = "SELECT c FROM CrtActividad c WHERE c.ideCra = :ideCra"),
    @NamedQuery(name = "CrtActividad.findByNombreCra", query = "SELECT c FROM CrtActividad c WHERE c.nombreCra = :nombreCra"),
    @NamedQuery(name = "CrtActividad.findByFechaCra", query = "SELECT c FROM CrtActividad c WHERE c.fechaCra = :fechaCra"),
    @NamedQuery(name = "CrtActividad.findByUsuarioIngre", query = "SELECT c FROM CrtActividad c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CrtActividad.findByFechaIngre", query = "SELECT c FROM CrtActividad c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CrtActividad.findByHoraIngre", query = "SELECT c FROM CrtActividad c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CrtActividad.findByUsuarioActua", query = "SELECT c FROM CrtActividad c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CrtActividad.findByFechaActua", query = "SELECT c FROM CrtActividad c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CrtActividad.findByHoraActua", query = "SELECT c FROM CrtActividad c WHERE c.horaActua = :horaActua"),
    @NamedQuery(name = "CrtActividad.findByFinalizado", query = "SELECT c FROM CrtActividad c WHERE c.finalizado = :finalizado")})
public class CrtActividad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cra", nullable = false)
    private Integer ideCra;
    @Size(max = 500)
    @Column(name = "nombre_cra", length = 500)
    private String nombreCra;
    @Column(name = "fecha_cra")
    @Temporal(TemporalType.DATE)
    private Date fechaCra;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @Column(name = "finalizado")
    private Boolean finalizado;
    @OneToMany(mappedBy = "ideCra")
    private List<CrtDetalleActividad> crtDetalleActividadList;
    @JoinColumn(name = "ide_repo", referencedColumnName = "ide_repo")
    @ManyToOne
    private SisReporte ideRepo;
    @JoinColumn(name = "ide_opci", referencedColumnName = "ide_opci")
    @ManyToOne
    private SisOpcion ideOpci;
    @JoinColumn(name = "ide_crf", referencedColumnName = "ide_crf")
    @ManyToOne
    private CrtFuncionario ideCrf;

    public CrtActividad() {
    }

    public CrtActividad(Integer ideCra) {
        this.ideCra = ideCra;
    }

    public Integer getIdeCra() {
        return ideCra;
    }

    public void setIdeCra(Integer ideCra) {
        this.ideCra = ideCra;
    }

    public String getNombreCra() {
        return nombreCra;
    }

    public void setNombreCra(String nombreCra) {
        this.nombreCra = nombreCra;
    }

    public Date getFechaCra() {
        return fechaCra;
    }

    public void setFechaCra(Date fechaCra) {
        this.fechaCra = fechaCra;
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

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public List<CrtDetalleActividad> getCrtDetalleActividadList() {
        return crtDetalleActividadList;
    }

    public void setCrtDetalleActividadList(List<CrtDetalleActividad> crtDetalleActividadList) {
        this.crtDetalleActividadList = crtDetalleActividadList;
    }

    public SisReporte getIdeRepo() {
        return ideRepo;
    }

    public void setIdeRepo(SisReporte ideRepo) {
        this.ideRepo = ideRepo;
    }

    public SisOpcion getIdeOpci() {
        return ideOpci;
    }

    public void setIdeOpci(SisOpcion ideOpci) {
        this.ideOpci = ideOpci;
    }

    public CrtFuncionario getIdeCrf() {
        return ideCrf;
    }

    public void setIdeCrf(CrtFuncionario ideCrf) {
        this.ideCrf = ideCrf;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCra != null ? ideCra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CrtActividad)) {
            return false;
        }
        CrtActividad other = (CrtActividad) object;
        if ((this.ideCra == null && other.ideCra != null) || (this.ideCra != null && !this.ideCra.equals(other.ideCra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CrtActividad[ ideCra=" + ideCra + " ]";
    }
    
}
