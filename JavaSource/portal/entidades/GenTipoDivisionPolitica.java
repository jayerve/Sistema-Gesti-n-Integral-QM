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
@Table(name = "gen_tipo_division_politica", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenTipoDivisionPolitica.findAll", query = "SELECT g FROM GenTipoDivisionPolitica g"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByIdeGetdp", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.ideGetdp = :ideGetdp"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByDetalleGetdp", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.detalleGetdp = :detalleGetdp"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByNivelGetdp", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.nivelGetdp = :nivelGetdp"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByUsuarioIngre", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByFechaIngre", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByUsuarioActua", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByFechaActua", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByHoraIngre", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByHoraActua", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.horaActua = :horaActua")})
public class GenTipoDivisionPolitica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_getdp", nullable = false)
    private Integer ideGetdp;
    @Size(max = 100)
    @Column(name = "detalle_getdp", length = 100)
    private String detalleGetdp;
    @Column(name = "nivel_getdp")
    private Integer nivelGetdp;
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
    @OneToMany(mappedBy = "ideGetdp")
    private List<GenDivisionPolitica> genDivisionPoliticaList;

    public GenTipoDivisionPolitica() {
    }

    public GenTipoDivisionPolitica(Integer ideGetdp) {
        this.ideGetdp = ideGetdp;
    }

    public Integer getIdeGetdp() {
        return ideGetdp;
    }

    public void setIdeGetdp(Integer ideGetdp) {
        this.ideGetdp = ideGetdp;
    }

    public String getDetalleGetdp() {
        return detalleGetdp;
    }

    public void setDetalleGetdp(String detalleGetdp) {
        this.detalleGetdp = detalleGetdp;
    }

    public Integer getNivelGetdp() {
        return nivelGetdp;
    }

    public void setNivelGetdp(Integer nivelGetdp) {
        this.nivelGetdp = nivelGetdp;
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

    public List<GenDivisionPolitica> getGenDivisionPoliticaList() {
        return genDivisionPoliticaList;
    }

    public void setGenDivisionPoliticaList(List<GenDivisionPolitica> genDivisionPoliticaList) {
        this.genDivisionPoliticaList = genDivisionPoliticaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGetdp != null ? ideGetdp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenTipoDivisionPolitica)) {
            return false;
        }
        GenTipoDivisionPolitica other = (GenTipoDivisionPolitica) object;
        if ((this.ideGetdp == null && other.ideGetdp != null) || (this.ideGetdp != null && !this.ideGetdp.equals(other.ideGetdp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenTipoDivisionPolitica[ ideGetdp=" + ideGetdp + " ]";
    }
    
}
