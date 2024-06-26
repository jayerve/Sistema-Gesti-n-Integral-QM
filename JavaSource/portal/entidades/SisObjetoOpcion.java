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
@Table(name = "sis_objeto_opcion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisObjetoOpcion.findAll", query = "SELECT s FROM SisObjetoOpcion s"),
    @NamedQuery(name = "SisObjetoOpcion.findByIdeObop", query = "SELECT s FROM SisObjetoOpcion s WHERE s.ideObop = :ideObop"),
    @NamedQuery(name = "SisObjetoOpcion.findByNomObop", query = "SELECT s FROM SisObjetoOpcion s WHERE s.nomObop = :nomObop"),
    @NamedQuery(name = "SisObjetoOpcion.findByIdObop", query = "SELECT s FROM SisObjetoOpcion s WHERE s.idObop = :idObop"),
    @NamedQuery(name = "SisObjetoOpcion.findByDescripcionObop", query = "SELECT s FROM SisObjetoOpcion s WHERE s.descripcionObop = :descripcionObop"),
    @NamedQuery(name = "SisObjetoOpcion.findByUsuarioIngre", query = "SELECT s FROM SisObjetoOpcion s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisObjetoOpcion.findByFechaIngre", query = "SELECT s FROM SisObjetoOpcion s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisObjetoOpcion.findByUsuarioActua", query = "SELECT s FROM SisObjetoOpcion s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisObjetoOpcion.findByFechaActua", query = "SELECT s FROM SisObjetoOpcion s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisObjetoOpcion.findByHoraIngre", query = "SELECT s FROM SisObjetoOpcion s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisObjetoOpcion.findByHoraActua", query = "SELECT s FROM SisObjetoOpcion s WHERE s.horaActua = :horaActua")})
public class SisObjetoOpcion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_obop", nullable = false)
    private Integer ideObop;
    @Size(max = 80)
    @Column(name = "nom_obop", length = 80)
    private String nomObop;
    @Size(max = 50)
    @Column(name = "id_obop", length = 50)
    private String idObop;
    @Size(max = 150)
    @Column(name = "descripcion_obop", length = 150)
    private String descripcionObop;
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
    @OneToMany(mappedBy = "ideObop")
    private List<SisPerfilObjeto> sisPerfilObjetoList;
    @JoinColumn(name = "ide_opci", referencedColumnName = "ide_opci")
    @ManyToOne
    private SisOpcion ideOpci;

    public SisObjetoOpcion() {
    }

    public SisObjetoOpcion(Integer ideObop) {
        this.ideObop = ideObop;
    }

    public Integer getIdeObop() {
        return ideObop;
    }

    public void setIdeObop(Integer ideObop) {
        this.ideObop = ideObop;
    }

    public String getNomObop() {
        return nomObop;
    }

    public void setNomObop(String nomObop) {
        this.nomObop = nomObop;
    }

    public String getIdObop() {
        return idObop;
    }

    public void setIdObop(String idObop) {
        this.idObop = idObop;
    }

    public String getDescripcionObop() {
        return descripcionObop;
    }

    public void setDescripcionObop(String descripcionObop) {
        this.descripcionObop = descripcionObop;
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

    public List<SisPerfilObjeto> getSisPerfilObjetoList() {
        return sisPerfilObjetoList;
    }

    public void setSisPerfilObjetoList(List<SisPerfilObjeto> sisPerfilObjetoList) {
        this.sisPerfilObjetoList = sisPerfilObjetoList;
    }

    public SisOpcion getIdeOpci() {
        return ideOpci;
    }

    public void setIdeOpci(SisOpcion ideOpci) {
        this.ideOpci = ideOpci;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideObop != null ? ideObop.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisObjetoOpcion)) {
            return false;
        }
        SisObjetoOpcion other = (SisObjetoOpcion) object;
        if ((this.ideObop == null && other.ideObop != null) || (this.ideObop != null && !this.ideObop.equals(other.ideObop))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisObjetoOpcion[ ideObop=" + ideObop + " ]";
    }
    
}
