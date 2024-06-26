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
@Table(name = "gen_categoria_estatus", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenCategoriaEstatus.findAll", query = "SELECT g FROM GenCategoriaEstatus g"),
    @NamedQuery(name = "GenCategoriaEstatus.findByIdeGecae", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.ideGecae = :ideGecae"),
    @NamedQuery(name = "GenCategoriaEstatus.findByDetalleGecae", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.detalleGecae = :detalleGecae"),
    @NamedQuery(name = "GenCategoriaEstatus.findByActivoGecae", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.activoGecae = :activoGecae"),
    @NamedQuery(name = "GenCategoriaEstatus.findByUsuarioIngre", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenCategoriaEstatus.findByFechaIngre", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenCategoriaEstatus.findByUsuarioActua", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenCategoriaEstatus.findByFechaActua", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenCategoriaEstatus.findByHoraIngre", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenCategoriaEstatus.findByHoraActua", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.horaActua = :horaActua")})
public class GenCategoriaEstatus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gecae", nullable = false)
    private Integer ideGecae;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "detalle_gecae", nullable = false, length = 100)
    private String detalleGecae;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_gecae", nullable = false)
    private boolean activoGecae;
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
    @OneToMany(mappedBy = "ideGecae")
    private List<GthTipoContrato> gthTipoContratoList;
    @OneToMany(mappedBy = "ideGecae")
    private List<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParList;
    @OneToMany(mappedBy = "ideGecae")
    private List<GthDocumentacionEmpleado> gthDocumentacionEmpleadoList;

    public GenCategoriaEstatus() {
    }

    public GenCategoriaEstatus(Integer ideGecae) {
        this.ideGecae = ideGecae;
    }

    public GenCategoriaEstatus(Integer ideGecae, String detalleGecae, boolean activoGecae) {
        this.ideGecae = ideGecae;
        this.detalleGecae = detalleGecae;
        this.activoGecae = activoGecae;
    }

    public Integer getIdeGecae() {
        return ideGecae;
    }

    public void setIdeGecae(Integer ideGecae) {
        this.ideGecae = ideGecae;
    }

    public String getDetalleGecae() {
        return detalleGecae;
    }

    public void setDetalleGecae(String detalleGecae) {
        this.detalleGecae = detalleGecae;
    }

    public boolean getActivoGecae() {
        return activoGecae;
    }

    public void setActivoGecae(boolean activoGecae) {
        this.activoGecae = activoGecae;
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

    public List<GthTipoContrato> getGthTipoContratoList() {
        return gthTipoContratoList;
    }

    public void setGthTipoContratoList(List<GthTipoContrato> gthTipoContratoList) {
        this.gthTipoContratoList = gthTipoContratoList;
    }

    public List<GenEmpleadosDepartamentoPar> getGenEmpleadosDepartamentoParList() {
        return genEmpleadosDepartamentoParList;
    }

    public void setGenEmpleadosDepartamentoParList(List<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParList) {
        this.genEmpleadosDepartamentoParList = genEmpleadosDepartamentoParList;
    }

    public List<GthDocumentacionEmpleado> getGthDocumentacionEmpleadoList() {
        return gthDocumentacionEmpleadoList;
    }

    public void setGthDocumentacionEmpleadoList(List<GthDocumentacionEmpleado> gthDocumentacionEmpleadoList) {
        this.gthDocumentacionEmpleadoList = gthDocumentacionEmpleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGecae != null ? ideGecae.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenCategoriaEstatus)) {
            return false;
        }
        GenCategoriaEstatus other = (GenCategoriaEstatus) object;
        if ((this.ideGecae == null && other.ideGecae != null) || (this.ideGecae != null && !this.ideGecae.equals(other.ideGecae))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenCategoriaEstatus[ ideGecae=" + ideGecae + " ]";
    }
    
}
