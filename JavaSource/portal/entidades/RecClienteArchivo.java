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
@Table(name = "rec_cliente_archivo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "RecClienteArchivo.findAll", query = "SELECT r FROM RecClienteArchivo r"),
    @NamedQuery(name = "RecClienteArchivo.findByIdeRecla", query = "SELECT r FROM RecClienteArchivo r WHERE r.ideRecla = :ideRecla"),
    @NamedQuery(name = "RecClienteArchivo.findByFechaRecla", query = "SELECT r FROM RecClienteArchivo r WHERE r.fechaRecla = :fechaRecla"),
    @NamedQuery(name = "RecClienteArchivo.findByObservacionRecla", query = "SELECT r FROM RecClienteArchivo r WHERE r.observacionRecla = :observacionRecla"),
    @NamedQuery(name = "RecClienteArchivo.findByFotoRecla", query = "SELECT r FROM RecClienteArchivo r WHERE r.fotoRecla = :fotoRecla"),
    @NamedQuery(name = "RecClienteArchivo.findByActivoRecla", query = "SELECT r FROM RecClienteArchivo r WHERE r.activoRecla = :activoRecla"),
    @NamedQuery(name = "RecClienteArchivo.findByUsuarioIngre", query = "SELECT r FROM RecClienteArchivo r WHERE r.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "RecClienteArchivo.findByFechaIngre", query = "SELECT r FROM RecClienteArchivo r WHERE r.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "RecClienteArchivo.findByHoraIngre", query = "SELECT r FROM RecClienteArchivo r WHERE r.horaIngre = :horaIngre"),
    @NamedQuery(name = "RecClienteArchivo.findByUsuarioActua", query = "SELECT r FROM RecClienteArchivo r WHERE r.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "RecClienteArchivo.findByFechaActua", query = "SELECT r FROM RecClienteArchivo r WHERE r.fechaActua = :fechaActua"),
    @NamedQuery(name = "RecClienteArchivo.findByHoraActua", query = "SELECT r FROM RecClienteArchivo r WHERE r.horaActua = :horaActua")})
public class RecClienteArchivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_recla", nullable = false)
    private Long ideRecla;
    @Column(name = "fecha_recla")
    @Temporal(TemporalType.DATE)
    private Date fechaRecla;
    @Size(max = 2147483647)
    @Column(name = "observacion_recla", length = 2147483647)
    private String observacionRecla;
    @Size(max = 250)
    @Column(name = "foto_recla", length = 250)
    private String fotoRecla;
    @Column(name = "activo_recla")
    private Boolean activoRecla;
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
    @JoinColumn(name = "ide_recli", referencedColumnName = "ide_recli")
    @ManyToOne
    private RecClientes ideRecli;

    public RecClienteArchivo() {
    }

    public RecClienteArchivo(Long ideRecla) {
        this.ideRecla = ideRecla;
    }

    public Long getIdeRecla() {
        return ideRecla;
    }

    public void setIdeRecla(Long ideRecla) {
        this.ideRecla = ideRecla;
    }

    public Date getFechaRecla() {
        return fechaRecla;
    }

    public void setFechaRecla(Date fechaRecla) {
        this.fechaRecla = fechaRecla;
    }

    public String getObservacionRecla() {
        return observacionRecla;
    }

    public void setObservacionRecla(String observacionRecla) {
        this.observacionRecla = observacionRecla;
    }

    public String getFotoRecla() {
        return fotoRecla;
    }

    public void setFotoRecla(String fotoRecla) {
        this.fotoRecla = fotoRecla;
    }

    public Boolean getActivoRecla() {
        return activoRecla;
    }

    public void setActivoRecla(Boolean activoRecla) {
        this.activoRecla = activoRecla;
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

    public RecClientes getIdeRecli() {
        return ideRecli;
    }

    public void setIdeRecli(RecClientes ideRecli) {
        this.ideRecli = ideRecli;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideRecla != null ? ideRecla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecClienteArchivo)) {
            return false;
        }
        RecClienteArchivo other = (RecClienteArchivo) object;
        if ((this.ideRecla == null && other.ideRecla != null) || (this.ideRecla != null && !this.ideRecla.equals(other.ideRecla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.RecClienteArchivo[ ideRecla=" + ideRecla + " ]";
    }
    
}
