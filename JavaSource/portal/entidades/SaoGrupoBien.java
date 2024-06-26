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
@Table(name = "sao_grupo_bien", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoGrupoBien.findAll", query = "SELECT s FROM SaoGrupoBien s"),
    @NamedQuery(name = "SaoGrupoBien.findByIdeSagrb", query = "SELECT s FROM SaoGrupoBien s WHERE s.ideSagrb = :ideSagrb"),
    @NamedQuery(name = "SaoGrupoBien.findByDetalleSagrb", query = "SELECT s FROM SaoGrupoBien s WHERE s.detalleSagrb = :detalleSagrb"),
    @NamedQuery(name = "SaoGrupoBien.findByActivoSagrb", query = "SELECT s FROM SaoGrupoBien s WHERE s.activoSagrb = :activoSagrb"),
    @NamedQuery(name = "SaoGrupoBien.findByUsuarioIngre", query = "SELECT s FROM SaoGrupoBien s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoGrupoBien.findByFechaIngre", query = "SELECT s FROM SaoGrupoBien s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoGrupoBien.findByHoraIngre", query = "SELECT s FROM SaoGrupoBien s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoGrupoBien.findByUsuarioActua", query = "SELECT s FROM SaoGrupoBien s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoGrupoBien.findByFechaActua", query = "SELECT s FROM SaoGrupoBien s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoGrupoBien.findByHoraActua", query = "SELECT s FROM SaoGrupoBien s WHERE s.horaActua = :horaActua")})
public class SaoGrupoBien implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sagrb", nullable = false)
    private Integer ideSagrb;
    @Size(max = 100)
    @Column(name = "detalle_sagrb", length = 100)
    private String detalleSagrb;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sagrb", nullable = false)
    private boolean activoSagrb;
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
    @OneToMany(mappedBy = "saoIdeSagrb")
    private List<SaoGrupoBien> saoGrupoBienList;
    @JoinColumn(name = "sao_ide_sagrb", referencedColumnName = "ide_sagrb")
    @ManyToOne
    private SaoGrupoBien saoIdeSagrb;
    @OneToMany(mappedBy = "ideSagrb")
    private List<SaoBienes> saoBienesList;
    @OneToMany(mappedBy = "ideSagrb")
    private List<SaoDotacionUniforme> saoDotacionUniformeList;

    public SaoGrupoBien() {
    }

    public SaoGrupoBien(Integer ideSagrb) {
        this.ideSagrb = ideSagrb;
    }

    public SaoGrupoBien(Integer ideSagrb, boolean activoSagrb) {
        this.ideSagrb = ideSagrb;
        this.activoSagrb = activoSagrb;
    }

    public Integer getIdeSagrb() {
        return ideSagrb;
    }

    public void setIdeSagrb(Integer ideSagrb) {
        this.ideSagrb = ideSagrb;
    }

    public String getDetalleSagrb() {
        return detalleSagrb;
    }

    public void setDetalleSagrb(String detalleSagrb) {
        this.detalleSagrb = detalleSagrb;
    }

    public boolean getActivoSagrb() {
        return activoSagrb;
    }

    public void setActivoSagrb(boolean activoSagrb) {
        this.activoSagrb = activoSagrb;
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

    public List<SaoGrupoBien> getSaoGrupoBienList() {
        return saoGrupoBienList;
    }

    public void setSaoGrupoBienList(List<SaoGrupoBien> saoGrupoBienList) {
        this.saoGrupoBienList = saoGrupoBienList;
    }

    public SaoGrupoBien getSaoIdeSagrb() {
        return saoIdeSagrb;
    }

    public void setSaoIdeSagrb(SaoGrupoBien saoIdeSagrb) {
        this.saoIdeSagrb = saoIdeSagrb;
    }

    public List<SaoBienes> getSaoBienesList() {
        return saoBienesList;
    }

    public void setSaoBienesList(List<SaoBienes> saoBienesList) {
        this.saoBienesList = saoBienesList;
    }

    public List<SaoDotacionUniforme> getSaoDotacionUniformeList() {
        return saoDotacionUniformeList;
    }

    public void setSaoDotacionUniformeList(List<SaoDotacionUniforme> saoDotacionUniformeList) {
        this.saoDotacionUniformeList = saoDotacionUniformeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSagrb != null ? ideSagrb.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoGrupoBien)) {
            return false;
        }
        SaoGrupoBien other = (SaoGrupoBien) object;
        if ((this.ideSagrb == null && other.ideSagrb != null) || (this.ideSagrb != null && !this.ideSagrb.equals(other.ideSagrb))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoGrupoBien[ ideSagrb=" + ideSagrb + " ]";
    }
    
}
