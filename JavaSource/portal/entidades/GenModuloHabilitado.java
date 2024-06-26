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
@Table(name = "gen_modulo_habilitado", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenModuloHabilitado.findAll", query = "SELECT g FROM GenModuloHabilitado g"),
    @NamedQuery(name = "GenModuloHabilitado.findByIdeGemoh", query = "SELECT g FROM GenModuloHabilitado g WHERE g.ideGemoh = :ideGemoh"),
    @NamedQuery(name = "GenModuloHabilitado.findByObservacionGemoh", query = "SELECT g FROM GenModuloHabilitado g WHERE g.observacionGemoh = :observacionGemoh"),
    @NamedQuery(name = "GenModuloHabilitado.findByActivoGemoh", query = "SELECT g FROM GenModuloHabilitado g WHERE g.activoGemoh = :activoGemoh"),
    @NamedQuery(name = "GenModuloHabilitado.findByUsuarioIngre", query = "SELECT g FROM GenModuloHabilitado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenModuloHabilitado.findByFechaIngre", query = "SELECT g FROM GenModuloHabilitado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenModuloHabilitado.findByHoraIngre", query = "SELECT g FROM GenModuloHabilitado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenModuloHabilitado.findByUsuarioActua", query = "SELECT g FROM GenModuloHabilitado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenModuloHabilitado.findByFechaActua", query = "SELECT g FROM GenModuloHabilitado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenModuloHabilitado.findByHoraActua", query = "SELECT g FROM GenModuloHabilitado g WHERE g.horaActua = :horaActua")})
public class GenModuloHabilitado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gemoh", nullable = false)
    private Long ideGemoh;
    @Size(max = 2147483647)
    @Column(name = "observacion_gemoh", length = 2147483647)
    private String observacionGemoh;
    @Column(name = "activo_gemoh")
    private Boolean activoGemoh;
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
    @JoinColumn(name = "ide_gemod", referencedColumnName = "ide_gemod")
    @ManyToOne
    private GenModulo ideGemod;

    public GenModuloHabilitado() {
    }

    public GenModuloHabilitado(Long ideGemoh) {
        this.ideGemoh = ideGemoh;
    }

    public Long getIdeGemoh() {
        return ideGemoh;
    }

    public void setIdeGemoh(Long ideGemoh) {
        this.ideGemoh = ideGemoh;
    }

    public String getObservacionGemoh() {
        return observacionGemoh;
    }

    public void setObservacionGemoh(String observacionGemoh) {
        this.observacionGemoh = observacionGemoh;
    }

    public Boolean getActivoGemoh() {
        return activoGemoh;
    }

    public void setActivoGemoh(Boolean activoGemoh) {
        this.activoGemoh = activoGemoh;
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

    public GenModulo getIdeGemod() {
        return ideGemod;
    }

    public void setIdeGemod(GenModulo ideGemod) {
        this.ideGemod = ideGemod;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGemoh != null ? ideGemoh.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenModuloHabilitado)) {
            return false;
        }
        GenModuloHabilitado other = (GenModuloHabilitado) object;
        if ((this.ideGemoh == null && other.ideGemoh != null) || (this.ideGemoh != null && !this.ideGemoh.equals(other.ideGemoh))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenModuloHabilitado[ ideGemoh=" + ideGemoh + " ]";
    }
    
}
