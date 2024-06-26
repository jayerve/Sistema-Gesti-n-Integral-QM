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
@Table(name = "gen_cargo_funcional", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenCargoFuncional.findAll", query = "SELECT g FROM GenCargoFuncional g"),
    @NamedQuery(name = "GenCargoFuncional.findByIdeGecaf", query = "SELECT g FROM GenCargoFuncional g WHERE g.ideGecaf = :ideGecaf"),
    @NamedQuery(name = "GenCargoFuncional.findByDetalleGecaf", query = "SELECT g FROM GenCargoFuncional g WHERE g.detalleGecaf = :detalleGecaf"),
    @NamedQuery(name = "GenCargoFuncional.findBySiglasGecaf", query = "SELECT g FROM GenCargoFuncional g WHERE g.siglasGecaf = :siglasGecaf"),
    @NamedQuery(name = "GenCargoFuncional.findByUsuarioIngre", query = "SELECT g FROM GenCargoFuncional g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenCargoFuncional.findByFechaIngre", query = "SELECT g FROM GenCargoFuncional g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenCargoFuncional.findByUsuarioActua", query = "SELECT g FROM GenCargoFuncional g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenCargoFuncional.findByPrincipalSecundarioGecaf", query = "SELECT g FROM GenCargoFuncional g WHERE g.principalSecundarioGecaf = :principalSecundarioGecaf"),
    @NamedQuery(name = "GenCargoFuncional.findByActivoGecaf", query = "SELECT g FROM GenCargoFuncional g WHERE g.activoGecaf = :activoGecaf"),
    @NamedQuery(name = "GenCargoFuncional.findByHoraIngre", query = "SELECT g FROM GenCargoFuncional g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenCargoFuncional.findByHoraActua", query = "SELECT g FROM GenCargoFuncional g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GenCargoFuncional.findByFechaActua", query = "SELECT g FROM GenCargoFuncional g WHERE g.fechaActua = :fechaActua")})
public class GenCargoFuncional implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gecaf", nullable = false)
    private Integer ideGecaf;
    @Size(max = 100)
    @Column(name = "detalle_gecaf", length = 100)
    private String detalleGecaf;
    @Size(max = 50)
    @Column(name = "siglas_gecaf", length = 50)
    private String siglasGecaf;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "principal_secundario_gecaf")
    private Boolean principalSecundarioGecaf;
    @Column(name = "activo_gecaf")
    private Boolean activoGecaf;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.TIME)
    private Date fechaActua;
    @JoinColumn(name = "ide_sbcar", referencedColumnName = "ide_sbcar")
    @ManyToOne
    private SbsCargo ideSbcar;

    public GenCargoFuncional() {
    }

    public GenCargoFuncional(Integer ideGecaf) {
        this.ideGecaf = ideGecaf;
    }

    public Integer getIdeGecaf() {
        return ideGecaf;
    }

    public void setIdeGecaf(Integer ideGecaf) {
        this.ideGecaf = ideGecaf;
    }

    public String getDetalleGecaf() {
        return detalleGecaf;
    }

    public void setDetalleGecaf(String detalleGecaf) {
        this.detalleGecaf = detalleGecaf;
    }

    public String getSiglasGecaf() {
        return siglasGecaf;
    }

    public void setSiglasGecaf(String siglasGecaf) {
        this.siglasGecaf = siglasGecaf;
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

    public Boolean getPrincipalSecundarioGecaf() {
        return principalSecundarioGecaf;
    }

    public void setPrincipalSecundarioGecaf(Boolean principalSecundarioGecaf) {
        this.principalSecundarioGecaf = principalSecundarioGecaf;
    }

    public Boolean getActivoGecaf() {
        return activoGecaf;
    }

    public void setActivoGecaf(Boolean activoGecaf) {
        this.activoGecaf = activoGecaf;
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

    public Date getFechaActua() {
        return fechaActua;
    }

    public void setFechaActua(Date fechaActua) {
        this.fechaActua = fechaActua;
    }

    public SbsCargo getIdeSbcar() {
        return ideSbcar;
    }

    public void setIdeSbcar(SbsCargo ideSbcar) {
        this.ideSbcar = ideSbcar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGecaf != null ? ideGecaf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenCargoFuncional)) {
            return false;
        }
        GenCargoFuncional other = (GenCargoFuncional) object;
        if ((this.ideGecaf == null && other.ideGecaf != null) || (this.ideGecaf != null && !this.ideGecaf.equals(other.ideGecaf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenCargoFuncional[ ideGecaf=" + ideGecaf + " ]";
    }
    
}
