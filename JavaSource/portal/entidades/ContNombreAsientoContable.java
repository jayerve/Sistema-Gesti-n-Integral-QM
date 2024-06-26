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
@Table(name = "cont_nombre_asiento_contable", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContNombreAsientoContable.findAll", query = "SELECT c FROM ContNombreAsientoContable c"),
    @NamedQuery(name = "ContNombreAsientoContable.findByIdeConac", query = "SELECT c FROM ContNombreAsientoContable c WHERE c.ideConac = :ideConac"),
    @NamedQuery(name = "ContNombreAsientoContable.findByDetalleConac", query = "SELECT c FROM ContNombreAsientoContable c WHERE c.detalleConac = :detalleConac"),
    @NamedQuery(name = "ContNombreAsientoContable.findByConsolidadoConac", query = "SELECT c FROM ContNombreAsientoContable c WHERE c.consolidadoConac = :consolidadoConac"),
    @NamedQuery(name = "ContNombreAsientoContable.findByIndividualConac", query = "SELECT c FROM ContNombreAsientoContable c WHERE c.individualConac = :individualConac"),
    @NamedQuery(name = "ContNombreAsientoContable.findByActivoConac", query = "SELECT c FROM ContNombreAsientoContable c WHERE c.activoConac = :activoConac"),
    @NamedQuery(name = "ContNombreAsientoContable.findByUsuarioIngre", query = "SELECT c FROM ContNombreAsientoContable c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContNombreAsientoContable.findByFechaIngre", query = "SELECT c FROM ContNombreAsientoContable c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContNombreAsientoContable.findByHoraIngre", query = "SELECT c FROM ContNombreAsientoContable c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContNombreAsientoContable.findByUsuarioActua", query = "SELECT c FROM ContNombreAsientoContable c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContNombreAsientoContable.findByFechaActua", query = "SELECT c FROM ContNombreAsientoContable c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContNombreAsientoContable.findByHoraActua", query = "SELECT c FROM ContNombreAsientoContable c WHERE c.horaActua = :horaActua")})
public class ContNombreAsientoContable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_conac", nullable = false)
    private Long ideConac;
    @Size(max = 250)
    @Column(name = "detalle_conac", length = 250)
    private String detalleConac;
    @Column(name = "consolidado_conac")
    private Boolean consolidadoConac;
    @Column(name = "individual_conac")
    private Boolean individualConac;
    @Column(name = "activo_conac")
    private Boolean activoConac;
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
    @OneToMany(mappedBy = "ideConac")
    private List<ContReglasAsientoContable> contReglasAsientoContableList;

    public ContNombreAsientoContable() {
    }

    public ContNombreAsientoContable(Long ideConac) {
        this.ideConac = ideConac;
    }

    public Long getIdeConac() {
        return ideConac;
    }

    public void setIdeConac(Long ideConac) {
        this.ideConac = ideConac;
    }

    public String getDetalleConac() {
        return detalleConac;
    }

    public void setDetalleConac(String detalleConac) {
        this.detalleConac = detalleConac;
    }

    public Boolean getConsolidadoConac() {
        return consolidadoConac;
    }

    public void setConsolidadoConac(Boolean consolidadoConac) {
        this.consolidadoConac = consolidadoConac;
    }

    public Boolean getIndividualConac() {
        return individualConac;
    }

    public void setIndividualConac(Boolean individualConac) {
        this.individualConac = individualConac;
    }

    public Boolean getActivoConac() {
        return activoConac;
    }

    public void setActivoConac(Boolean activoConac) {
        this.activoConac = activoConac;
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

    public List<ContReglasAsientoContable> getContReglasAsientoContableList() {
        return contReglasAsientoContableList;
    }

    public void setContReglasAsientoContableList(List<ContReglasAsientoContable> contReglasAsientoContableList) {
        this.contReglasAsientoContableList = contReglasAsientoContableList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideConac != null ? ideConac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContNombreAsientoContable)) {
            return false;
        }
        ContNombreAsientoContable other = (ContNombreAsientoContable) object;
        if ((this.ideConac == null && other.ideConac != null) || (this.ideConac != null && !this.ideConac.equals(other.ideConac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContNombreAsientoContable[ ideConac=" + ideConac + " ]";
    }
    
}
