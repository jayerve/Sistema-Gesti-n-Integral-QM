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
@Table(name = "sao_receta_medica", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoRecetaMedica.findAll", query = "SELECT s FROM SaoRecetaMedica s"),
    @NamedQuery(name = "SaoRecetaMedica.findByIdeSarem", query = "SELECT s FROM SaoRecetaMedica s WHERE s.ideSarem = :ideSarem"),
    @NamedQuery(name = "SaoRecetaMedica.findByCantidadSarem", query = "SELECT s FROM SaoRecetaMedica s WHERE s.cantidadSarem = :cantidadSarem"),
    @NamedQuery(name = "SaoRecetaMedica.findByIndicacionSarem", query = "SELECT s FROM SaoRecetaMedica s WHERE s.indicacionSarem = :indicacionSarem"),
    @NamedQuery(name = "SaoRecetaMedica.findByActivoSarem", query = "SELECT s FROM SaoRecetaMedica s WHERE s.activoSarem = :activoSarem"),
    @NamedQuery(name = "SaoRecetaMedica.findByUsuarioIngre", query = "SELECT s FROM SaoRecetaMedica s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoRecetaMedica.findByFechaIngre", query = "SELECT s FROM SaoRecetaMedica s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoRecetaMedica.findByUsuarioActua", query = "SELECT s FROM SaoRecetaMedica s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoRecetaMedica.findByFechaActua", query = "SELECT s FROM SaoRecetaMedica s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoRecetaMedica.findByHoraIngre", query = "SELECT s FROM SaoRecetaMedica s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoRecetaMedica.findByHoraActua", query = "SELECT s FROM SaoRecetaMedica s WHERE s.horaActua = :horaActua")})
public class SaoRecetaMedica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sarem", nullable = false)
    private Integer ideSarem;
    @Column(name = "cantidad_sarem")
    private Integer cantidadSarem;
    @Size(max = 1000)
    @Column(name = "indicacion_sarem", length = 1000)
    private String indicacionSarem;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sarem", nullable = false)
    private boolean activoSarem;
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
    @JoinColumn(name = "ide_samed", referencedColumnName = "ide_samed")
    @ManyToOne
    private SaoMedicacion ideSamed;
    @JoinColumn(name = "ide_safim", referencedColumnName = "ide_safim")
    @ManyToOne
    private SaoFichaMedica ideSafim;

    public SaoRecetaMedica() {
    }

    public SaoRecetaMedica(Integer ideSarem) {
        this.ideSarem = ideSarem;
    }

    public SaoRecetaMedica(Integer ideSarem, boolean activoSarem) {
        this.ideSarem = ideSarem;
        this.activoSarem = activoSarem;
    }

    public Integer getIdeSarem() {
        return ideSarem;
    }

    public void setIdeSarem(Integer ideSarem) {
        this.ideSarem = ideSarem;
    }

    public Integer getCantidadSarem() {
        return cantidadSarem;
    }

    public void setCantidadSarem(Integer cantidadSarem) {
        this.cantidadSarem = cantidadSarem;
    }

    public String getIndicacionSarem() {
        return indicacionSarem;
    }

    public void setIndicacionSarem(String indicacionSarem) {
        this.indicacionSarem = indicacionSarem;
    }

    public boolean getActivoSarem() {
        return activoSarem;
    }

    public void setActivoSarem(boolean activoSarem) {
        this.activoSarem = activoSarem;
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

    public SaoMedicacion getIdeSamed() {
        return ideSamed;
    }

    public void setIdeSamed(SaoMedicacion ideSamed) {
        this.ideSamed = ideSamed;
    }

    public SaoFichaMedica getIdeSafim() {
        return ideSafim;
    }

    public void setIdeSafim(SaoFichaMedica ideSafim) {
        this.ideSafim = ideSafim;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSarem != null ? ideSarem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoRecetaMedica)) {
            return false;
        }
        SaoRecetaMedica other = (SaoRecetaMedica) object;
        if ((this.ideSarem == null && other.ideSarem != null) || (this.ideSarem != null && !this.ideSarem.equals(other.ideSarem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoRecetaMedica[ ideSarem=" + ideSarem + " ]";
    }
    
}
