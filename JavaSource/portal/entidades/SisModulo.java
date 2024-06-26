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
@Table(name = "sis_modulo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisModulo.findAll", query = "SELECT s FROM SisModulo s"),
    @NamedQuery(name = "SisModulo.findByIdeModu", query = "SELECT s FROM SisModulo s WHERE s.ideModu = :ideModu"),
    @NamedQuery(name = "SisModulo.findByNomModu", query = "SELECT s FROM SisModulo s WHERE s.nomModu = :nomModu"),
    @NamedQuery(name = "SisModulo.findByUsuarioIngre", query = "SELECT s FROM SisModulo s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisModulo.findByFechaIngre", query = "SELECT s FROM SisModulo s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisModulo.findByUsuarioActua", query = "SELECT s FROM SisModulo s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisModulo.findByFechaActua", query = "SELECT s FROM SisModulo s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisModulo.findByHoraIngre", query = "SELECT s FROM SisModulo s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisModulo.findByHoraActua", query = "SELECT s FROM SisModulo s WHERE s.horaActua = :horaActua")})
public class SisModulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_modu", nullable = false)
    private Integer ideModu;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nom_modu", nullable = false, length = 60)
    private String nomModu;
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
    @OneToMany(mappedBy = "ideModu")
    private List<SisParametros> sisParametrosList;

    public SisModulo() {
    }

    public SisModulo(Integer ideModu) {
        this.ideModu = ideModu;
    }

    public SisModulo(Integer ideModu, String nomModu) {
        this.ideModu = ideModu;
        this.nomModu = nomModu;
    }

    public Integer getIdeModu() {
        return ideModu;
    }

    public void setIdeModu(Integer ideModu) {
        this.ideModu = ideModu;
    }

    public String getNomModu() {
        return nomModu;
    }

    public void setNomModu(String nomModu) {
        this.nomModu = nomModu;
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

    public List<SisParametros> getSisParametrosList() {
        return sisParametrosList;
    }

    public void setSisParametrosList(List<SisParametros> sisParametrosList) {
        this.sisParametrosList = sisParametrosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideModu != null ? ideModu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisModulo)) {
            return false;
        }
        SisModulo other = (SisModulo) object;
        if ((this.ideModu == null && other.ideModu != null) || (this.ideModu != null && !this.ideModu.equals(other.ideModu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisModulo[ ideModu=" + ideModu + " ]";
    }
    
}
