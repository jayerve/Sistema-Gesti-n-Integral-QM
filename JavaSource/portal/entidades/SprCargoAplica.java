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
@Table(name = "spr_cargo_aplica", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprCargoAplica.findAll", query = "SELECT s FROM SprCargoAplica s"),
    @NamedQuery(name = "SprCargoAplica.findByIdeSpcaa", query = "SELECT s FROM SprCargoAplica s WHERE s.ideSpcaa = :ideSpcaa"),
    @NamedQuery(name = "SprCargoAplica.findByIdeGeare", query = "SELECT s FROM SprCargoAplica s WHERE s.ideGeare = :ideGeare"),
    @NamedQuery(name = "SprCargoAplica.findByIdeGedep", query = "SELECT s FROM SprCargoAplica s WHERE s.ideGedep = :ideGedep"),
    @NamedQuery(name = "SprCargoAplica.findByIdeGegro", query = "SELECT s FROM SprCargoAplica s WHERE s.ideGegro = :ideGegro"),
    @NamedQuery(name = "SprCargoAplica.findByIdeGecaf", query = "SELECT s FROM SprCargoAplica s WHERE s.ideGecaf = :ideGecaf"),
    @NamedQuery(name = "SprCargoAplica.findByActivoSpcaa", query = "SELECT s FROM SprCargoAplica s WHERE s.activoSpcaa = :activoSpcaa"),
    @NamedQuery(name = "SprCargoAplica.findByUsuarioIngre", query = "SELECT s FROM SprCargoAplica s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprCargoAplica.findByFechaIngre", query = "SELECT s FROM SprCargoAplica s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprCargoAplica.findByHoraIngre", query = "SELECT s FROM SprCargoAplica s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprCargoAplica.findByUsuarioActua", query = "SELECT s FROM SprCargoAplica s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprCargoAplica.findByFechaActua", query = "SELECT s FROM SprCargoAplica s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprCargoAplica.findByHoraActua", query = "SELECT s FROM SprCargoAplica s WHERE s.horaActua = :horaActua")})
public class SprCargoAplica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spcaa", nullable = false)
    private Integer ideSpcaa;
    @Column(name = "ide_geare")
    private Integer ideGeare;
    @Column(name = "ide_gedep")
    private Integer ideGedep;
    @Column(name = "ide_gegro")
    private Integer ideGegro;
    @Column(name = "ide_gecaf")
    private Integer ideGecaf;
    @Column(name = "activo_spcaa")
    private Boolean activoSpcaa;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.DATE)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.DATE)
    private Date horaActua;
    @JoinColumn(name = "ide_spbap", referencedColumnName = "ide_spbap")
    @ManyToOne
    private SprBasePostulante ideSpbap;

    public SprCargoAplica() {
    }

    public SprCargoAplica(Integer ideSpcaa) {
        this.ideSpcaa = ideSpcaa;
    }

    public Integer getIdeSpcaa() {
        return ideSpcaa;
    }

    public void setIdeSpcaa(Integer ideSpcaa) {
        this.ideSpcaa = ideSpcaa;
    }

    public Integer getIdeGeare() {
        return ideGeare;
    }

    public void setIdeGeare(Integer ideGeare) {
        this.ideGeare = ideGeare;
    }

    public Integer getIdeGedep() {
        return ideGedep;
    }

    public void setIdeGedep(Integer ideGedep) {
        this.ideGedep = ideGedep;
    }

    public Integer getIdeGegro() {
        return ideGegro;
    }

    public void setIdeGegro(Integer ideGegro) {
        this.ideGegro = ideGegro;
    }

    public Integer getIdeGecaf() {
        return ideGecaf;
    }

    public void setIdeGecaf(Integer ideGecaf) {
        this.ideGecaf = ideGecaf;
    }

    public Boolean getActivoSpcaa() {
        return activoSpcaa;
    }

    public void setActivoSpcaa(Boolean activoSpcaa) {
        this.activoSpcaa = activoSpcaa;
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

    public SprBasePostulante getIdeSpbap() {
        return ideSpbap;
    }

    public void setIdeSpbap(SprBasePostulante ideSpbap) {
        this.ideSpbap = ideSpbap;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpcaa != null ? ideSpcaa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprCargoAplica)) {
            return false;
        }
        SprCargoAplica other = (SprCargoAplica) object;
        if ((this.ideSpcaa == null && other.ideSpcaa != null) || (this.ideSpcaa != null && !this.ideSpcaa.equals(other.ideSpcaa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprCargoAplica[ ideSpcaa=" + ideSpcaa + " ]";
    }
    
}
