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
@Table(name = "tes_tarifas", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesTarifas.findAll", query = "SELECT t FROM TesTarifas t"),
    @NamedQuery(name = "TesTarifas.findByIdeTetar", query = "SELECT t FROM TesTarifas t WHERE t.ideTetar = :ideTetar"),
    @NamedQuery(name = "TesTarifas.findByDetalleTetar", query = "SELECT t FROM TesTarifas t WHERE t.detalleTetar = :detalleTetar"),
    @NamedQuery(name = "TesTarifas.findByFacturaTetar", query = "SELECT t FROM TesTarifas t WHERE t.facturaTetar = :facturaTetar"),
    @NamedQuery(name = "TesTarifas.findByActivoTetar", query = "SELECT t FROM TesTarifas t WHERE t.activoTetar = :activoTetar"),
    @NamedQuery(name = "TesTarifas.findByUsuarioIngre", query = "SELECT t FROM TesTarifas t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesTarifas.findByFechaIngre", query = "SELECT t FROM TesTarifas t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesTarifas.findByHoraIngre", query = "SELECT t FROM TesTarifas t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesTarifas.findByUsuarioActua", query = "SELECT t FROM TesTarifas t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesTarifas.findByFechaActua", query = "SELECT t FROM TesTarifas t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesTarifas.findByHoraActua", query = "SELECT t FROM TesTarifas t WHERE t.horaActua = :horaActua")})
public class TesTarifas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tetar", nullable = false)
    private Long ideTetar;
    @Size(max = 50)
    @Column(name = "detalle_tetar", length = 50)
    private String detalleTetar;
    @Column(name = "factura_tetar")
    private Boolean facturaTetar;
    @Column(name = "activo_tetar")
    private Boolean activoTetar;
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
    @OneToMany(mappedBy = "ideTetar")
    private List<RecClientes> recClientesList;
    @OneToMany(mappedBy = "ideTetar")
    private List<TesMaterialTarifa> tesMaterialTarifaList;

    public TesTarifas() {
    }

    public TesTarifas(Long ideTetar) {
        this.ideTetar = ideTetar;
    }

    public Long getIdeTetar() {
        return ideTetar;
    }

    public void setIdeTetar(Long ideTetar) {
        this.ideTetar = ideTetar;
    }

    public String getDetalleTetar() {
        return detalleTetar;
    }

    public void setDetalleTetar(String detalleTetar) {
        this.detalleTetar = detalleTetar;
    }

    public Boolean getFacturaTetar() {
        return facturaTetar;
    }

    public void setFacturaTetar(Boolean facturaTetar) {
        this.facturaTetar = facturaTetar;
    }

    public Boolean getActivoTetar() {
        return activoTetar;
    }

    public void setActivoTetar(Boolean activoTetar) {
        this.activoTetar = activoTetar;
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

    public List<RecClientes> getRecClientesList() {
        return recClientesList;
    }

    public void setRecClientesList(List<RecClientes> recClientesList) {
        this.recClientesList = recClientesList;
    }

    public List<TesMaterialTarifa> getTesMaterialTarifaList() {
        return tesMaterialTarifaList;
    }

    public void setTesMaterialTarifaList(List<TesMaterialTarifa> tesMaterialTarifaList) {
        this.tesMaterialTarifaList = tesMaterialTarifaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTetar != null ? ideTetar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesTarifas)) {
            return false;
        }
        TesTarifas other = (TesTarifas) object;
        if ((this.ideTetar == null && other.ideTetar != null) || (this.ideTetar != null && !this.ideTetar.equals(other.ideTetar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesTarifas[ ideTetar=" + ideTetar + " ]";
    }
    
}
