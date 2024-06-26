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
@Table(name = "sis_combo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisCombo.findAll", query = "SELECT s FROM SisCombo s"),
    @NamedQuery(name = "SisCombo.findByIdeComb", query = "SELECT s FROM SisCombo s WHERE s.ideComb = :ideComb"),
    @NamedQuery(name = "SisCombo.findByCampoComb", query = "SELECT s FROM SisCombo s WHERE s.campoComb = :campoComb"),
    @NamedQuery(name = "SisCombo.findByTablaComb", query = "SELECT s FROM SisCombo s WHERE s.tablaComb = :tablaComb"),
    @NamedQuery(name = "SisCombo.findByPrimariaComb", query = "SELECT s FROM SisCombo s WHERE s.primariaComb = :primariaComb"),
    @NamedQuery(name = "SisCombo.findByNombreComb", query = "SELECT s FROM SisCombo s WHERE s.nombreComb = :nombreComb"),
    @NamedQuery(name = "SisCombo.findByCondicionComb", query = "SELECT s FROM SisCombo s WHERE s.condicionComb = :condicionComb"),
    @NamedQuery(name = "SisCombo.findByUsuarioIngre", query = "SELECT s FROM SisCombo s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisCombo.findByFechaIngre", query = "SELECT s FROM SisCombo s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisCombo.findByUsuarioActua", query = "SELECT s FROM SisCombo s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisCombo.findByFechaActua", query = "SELECT s FROM SisCombo s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisCombo.findByHoraIngre", query = "SELECT s FROM SisCombo s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisCombo.findByHoraActua", query = "SELECT s FROM SisCombo s WHERE s.horaActua = :horaActua")})
public class SisCombo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_comb", nullable = false)
    private Integer ideComb;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "campo_comb", nullable = false, length = 50)
    private String campoComb;
    @Size(max = 50)
    @Column(name = "tabla_comb", length = 50)
    private String tablaComb;
    @Size(max = 50)
    @Column(name = "primaria_comb", length = 50)
    private String primariaComb;
    @Size(max = 50)
    @Column(name = "nombre_comb", length = 50)
    private String nombreComb;
    @Size(max = 150)
    @Column(name = "condicion_comb", length = 150)
    private String condicionComb;
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
    @JoinColumn(name = "ide_tabl", referencedColumnName = "ide_tabl")
    @ManyToOne
    private SisTabla ideTabl;

    public SisCombo() {
    }

    public SisCombo(Integer ideComb) {
        this.ideComb = ideComb;
    }

    public SisCombo(Integer ideComb, String campoComb) {
        this.ideComb = ideComb;
        this.campoComb = campoComb;
    }

    public Integer getIdeComb() {
        return ideComb;
    }

    public void setIdeComb(Integer ideComb) {
        this.ideComb = ideComb;
    }

    public String getCampoComb() {
        return campoComb;
    }

    public void setCampoComb(String campoComb) {
        this.campoComb = campoComb;
    }

    public String getTablaComb() {
        return tablaComb;
    }

    public void setTablaComb(String tablaComb) {
        this.tablaComb = tablaComb;
    }

    public String getPrimariaComb() {
        return primariaComb;
    }

    public void setPrimariaComb(String primariaComb) {
        this.primariaComb = primariaComb;
    }

    public String getNombreComb() {
        return nombreComb;
    }

    public void setNombreComb(String nombreComb) {
        this.nombreComb = nombreComb;
    }

    public String getCondicionComb() {
        return condicionComb;
    }

    public void setCondicionComb(String condicionComb) {
        this.condicionComb = condicionComb;
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

    public SisTabla getIdeTabl() {
        return ideTabl;
    }

    public void setIdeTabl(SisTabla ideTabl) {
        this.ideTabl = ideTabl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideComb != null ? ideComb.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisCombo)) {
            return false;
        }
        SisCombo other = (SisCombo) object;
        if ((this.ideComb == null && other.ideComb != null) || (this.ideComb != null && !this.ideComb.equals(other.ideComb))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisCombo[ ideComb=" + ideComb + " ]";
    }
    
}
