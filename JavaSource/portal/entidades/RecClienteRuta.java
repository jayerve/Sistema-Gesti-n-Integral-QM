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
@Table(name = "rec_cliente_ruta", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "RecClienteRuta.findAll", query = "SELECT r FROM RecClienteRuta r"),
    @NamedQuery(name = "RecClienteRuta.findByIdeReclr", query = "SELECT r FROM RecClienteRuta r WHERE r.ideReclr = :ideReclr"),
    @NamedQuery(name = "RecClienteRuta.findByDetalleReclr", query = "SELECT r FROM RecClienteRuta r WHERE r.detalleReclr = :detalleReclr"),
    @NamedQuery(name = "RecClienteRuta.findByAbreviaturaReclr", query = "SELECT r FROM RecClienteRuta r WHERE r.abreviaturaReclr = :abreviaturaReclr"),
    @NamedQuery(name = "RecClienteRuta.findByActivoReclr", query = "SELECT r FROM RecClienteRuta r WHERE r.activoReclr = :activoReclr"),
    @NamedQuery(name = "RecClienteRuta.findByUsuarioIngre", query = "SELECT r FROM RecClienteRuta r WHERE r.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "RecClienteRuta.findByFechaIngre", query = "SELECT r FROM RecClienteRuta r WHERE r.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "RecClienteRuta.findByHoraIngre", query = "SELECT r FROM RecClienteRuta r WHERE r.horaIngre = :horaIngre"),
    @NamedQuery(name = "RecClienteRuta.findByUsuarioActua", query = "SELECT r FROM RecClienteRuta r WHERE r.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "RecClienteRuta.findByFechaActua", query = "SELECT r FROM RecClienteRuta r WHERE r.fechaActua = :fechaActua"),
    @NamedQuery(name = "RecClienteRuta.findByHoraActua", query = "SELECT r FROM RecClienteRuta r WHERE r.horaActua = :horaActua"),
    @NamedQuery(name = "RecClienteRuta.findByCodigoRutaReclr", query = "SELECT r FROM RecClienteRuta r WHERE r.codigoRutaReclr = :codigoRutaReclr")})
public class RecClienteRuta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_reclr", nullable = false)
    private Long ideReclr;
    @Size(max = 50)
    @Column(name = "detalle_reclr", length = 50)
    private String detalleReclr;
    @Size(max = 20)
    @Column(name = "abreviatura_reclr", length = 20)
    private String abreviaturaReclr;
    @Column(name = "activo_reclr")
    private Boolean activoReclr;
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
    @Column(name = "codigo_ruta_reclr")
    private Integer codigoRutaReclr;
    @OneToMany(mappedBy = "ideReclr")
    private List<RecClientes> recClientesList;

    public RecClienteRuta() {
    }

    public RecClienteRuta(Long ideReclr) {
        this.ideReclr = ideReclr;
    }

    public Long getIdeReclr() {
        return ideReclr;
    }

    public void setIdeReclr(Long ideReclr) {
        this.ideReclr = ideReclr;
    }

    public String getDetalleReclr() {
        return detalleReclr;
    }

    public void setDetalleReclr(String detalleReclr) {
        this.detalleReclr = detalleReclr;
    }

    public String getAbreviaturaReclr() {
        return abreviaturaReclr;
    }

    public void setAbreviaturaReclr(String abreviaturaReclr) {
        this.abreviaturaReclr = abreviaturaReclr;
    }

    public Boolean getActivoReclr() {
        return activoReclr;
    }

    public void setActivoReclr(Boolean activoReclr) {
        this.activoReclr = activoReclr;
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

    public Integer getCodigoRutaReclr() {
        return codigoRutaReclr;
    }

    public void setCodigoRutaReclr(Integer codigoRutaReclr) {
        this.codigoRutaReclr = codigoRutaReclr;
    }

    public List<RecClientes> getRecClientesList() {
        return recClientesList;
    }

    public void setRecClientesList(List<RecClientes> recClientesList) {
        this.recClientesList = recClientesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideReclr != null ? ideReclr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecClienteRuta)) {
            return false;
        }
        RecClienteRuta other = (RecClienteRuta) object;
        if ((this.ideReclr == null && other.ideReclr != null) || (this.ideReclr != null && !this.ideReclr.equals(other.ideReclr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.RecClienteRuta[ ideReclr=" + ideReclr + " ]";
    }
    
}
