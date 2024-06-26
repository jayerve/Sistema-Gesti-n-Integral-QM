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
@Table(name = "sis_periodo_clave", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisPeriodoClave.findAll", query = "SELECT s FROM SisPeriodoClave s"),
    @NamedQuery(name = "SisPeriodoClave.findByIdePecl", query = "SELECT s FROM SisPeriodoClave s WHERE s.idePecl = :idePecl"),
    @NamedQuery(name = "SisPeriodoClave.findByNomPecl", query = "SELECT s FROM SisPeriodoClave s WHERE s.nomPecl = :nomPecl"),
    @NamedQuery(name = "SisPeriodoClave.findByNumDias", query = "SELECT s FROM SisPeriodoClave s WHERE s.numDias = :numDias"),
    @NamedQuery(name = "SisPeriodoClave.findByUsuarioIngre", query = "SELECT s FROM SisPeriodoClave s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisPeriodoClave.findByFechaIngre", query = "SELECT s FROM SisPeriodoClave s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisPeriodoClave.findByUsuarioActua", query = "SELECT s FROM SisPeriodoClave s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisPeriodoClave.findByFechaActua", query = "SELECT s FROM SisPeriodoClave s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisPeriodoClave.findByHoraIngre", query = "SELECT s FROM SisPeriodoClave s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisPeriodoClave.findByHoraActua", query = "SELECT s FROM SisPeriodoClave s WHERE s.horaActua = :horaActua")})
public class SisPeriodoClave implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_pecl", nullable = false)
    private Integer idePecl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_pecl", nullable = false, length = 50)
    private String nomPecl;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_dias", nullable = false)
    private int numDias;
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
    @OneToMany(mappedBy = "idePecl")
    private List<SisUsuarioClave> sisUsuarioClaveList;
    @JoinColumn(name = "ide_empr", referencedColumnName = "ide_empr")
    @ManyToOne
    private SisEmpresa ideEmpr;

    public SisPeriodoClave() {
    }

    public SisPeriodoClave(Integer idePecl) {
        this.idePecl = idePecl;
    }

    public SisPeriodoClave(Integer idePecl, String nomPecl, int numDias) {
        this.idePecl = idePecl;
        this.nomPecl = nomPecl;
        this.numDias = numDias;
    }

    public Integer getIdePecl() {
        return idePecl;
    }

    public void setIdePecl(Integer idePecl) {
        this.idePecl = idePecl;
    }

    public String getNomPecl() {
        return nomPecl;
    }

    public void setNomPecl(String nomPecl) {
        this.nomPecl = nomPecl;
    }

    public int getNumDias() {
        return numDias;
    }

    public void setNumDias(int numDias) {
        this.numDias = numDias;
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

    public List<SisUsuarioClave> getSisUsuarioClaveList() {
        return sisUsuarioClaveList;
    }

    public void setSisUsuarioClaveList(List<SisUsuarioClave> sisUsuarioClaveList) {
        this.sisUsuarioClaveList = sisUsuarioClaveList;
    }

    public SisEmpresa getIdeEmpr() {
        return ideEmpr;
    }

    public void setIdeEmpr(SisEmpresa ideEmpr) {
        this.ideEmpr = ideEmpr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePecl != null ? idePecl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisPeriodoClave)) {
            return false;
        }
        SisPeriodoClave other = (SisPeriodoClave) object;
        if ((this.idePecl == null && other.idePecl != null) || (this.idePecl != null && !this.idePecl.equals(other.idePecl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisPeriodoClave[ idePecl=" + idePecl + " ]";
    }
    
}
