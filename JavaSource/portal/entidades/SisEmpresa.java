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
@Table(name = "sis_empresa", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisEmpresa.findAll", query = "SELECT s FROM SisEmpresa s"),
    @NamedQuery(name = "SisEmpresa.findByIdeEmpr", query = "SELECT s FROM SisEmpresa s WHERE s.ideEmpr = :ideEmpr"),
    @NamedQuery(name = "SisEmpresa.findByNomEmpr", query = "SELECT s FROM SisEmpresa s WHERE s.nomEmpr = :nomEmpr"),
    @NamedQuery(name = "SisEmpresa.findByContactoEmpr", query = "SELECT s FROM SisEmpresa s WHERE s.contactoEmpr = :contactoEmpr"),
    @NamedQuery(name = "SisEmpresa.findByRepresentanteEmpr", query = "SELECT s FROM SisEmpresa s WHERE s.representanteEmpr = :representanteEmpr"),
    @NamedQuery(name = "SisEmpresa.findByNomCortoEmpr", query = "SELECT s FROM SisEmpresa s WHERE s.nomCortoEmpr = :nomCortoEmpr"),
    @NamedQuery(name = "SisEmpresa.findByMailEmpr", query = "SELECT s FROM SisEmpresa s WHERE s.mailEmpr = :mailEmpr"),
    @NamedQuery(name = "SisEmpresa.findByPaginaEmpr", query = "SELECT s FROM SisEmpresa s WHERE s.paginaEmpr = :paginaEmpr"),
    @NamedQuery(name = "SisEmpresa.findByIdentificacionEmpr", query = "SELECT s FROM SisEmpresa s WHERE s.identificacionEmpr = :identificacionEmpr"),
    @NamedQuery(name = "SisEmpresa.findByDireccionEmpr", query = "SELECT s FROM SisEmpresa s WHERE s.direccionEmpr = :direccionEmpr"),
    @NamedQuery(name = "SisEmpresa.findByTelefonoEmpr", query = "SELECT s FROM SisEmpresa s WHERE s.telefonoEmpr = :telefonoEmpr"),
    @NamedQuery(name = "SisEmpresa.findByLogoEmpr", query = "SELECT s FROM SisEmpresa s WHERE s.logoEmpr = :logoEmpr"),
    @NamedQuery(name = "SisEmpresa.findByUsuarioIngre", query = "SELECT s FROM SisEmpresa s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisEmpresa.findByFechaIngre", query = "SELECT s FROM SisEmpresa s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisEmpresa.findByUsuarioActua", query = "SELECT s FROM SisEmpresa s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisEmpresa.findByFechaActua", query = "SELECT s FROM SisEmpresa s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisEmpresa.findByHoraIngre", query = "SELECT s FROM SisEmpresa s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisEmpresa.findByHoraActua", query = "SELECT s FROM SisEmpresa s WHERE s.horaActua = :horaActua")})
public class SisEmpresa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_empr", nullable = false)
    private Integer ideEmpr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nom_empr", nullable = false, length = 100)
    private String nomEmpr;
    @Size(max = 80)
    @Column(name = "contacto_empr", length = 80)
    private String contactoEmpr;
    @Size(max = 80)
    @Column(name = "representante_empr", length = 80)
    private String representanteEmpr;
    @Size(max = 100)
    @Column(name = "nom_corto_empr", length = 100)
    private String nomCortoEmpr;
    @Size(max = 100)
    @Column(name = "mail_empr", length = 100)
    private String mailEmpr;
    @Size(max = 100)
    @Column(name = "pagina_empr", length = 100)
    private String paginaEmpr;
    @Size(max = 20)
    @Column(name = "identificacion_empr", length = 20)
    private String identificacionEmpr;
    @Size(max = 100)
    @Column(name = "direccion_empr", length = 100)
    private String direccionEmpr;
    @Size(max = 40)
    @Column(name = "telefono_empr", length = 40)
    private String telefonoEmpr;
    @Size(max = 200)
    @Column(name = "logo_empr", length = 200)
    private String logoEmpr;
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
    @OneToMany(mappedBy = "ideEmpr")
    private List<SisParametros> sisParametrosList;
    @OneToMany(mappedBy = "ideEmpr")
    private List<SisReglasClave> sisReglasClaveList;
    @OneToMany(mappedBy = "ideEmpr")
    private List<SisAuditoria> sisAuditoriaList;
    @OneToMany(mappedBy = "ideEmpr")
    private List<SisUsuario> sisUsuarioList;
    @OneToMany(mappedBy = "ideEmpr")
    private List<SisPeriodoClave> sisPeriodoClaveList;
    @OneToMany(mappedBy = "ideEmpr")
    private List<SisSucursal> sisSucursalList;

    public SisEmpresa() {
    }

    public SisEmpresa(Integer ideEmpr) {
        this.ideEmpr = ideEmpr;
    }

    public SisEmpresa(Integer ideEmpr, String nomEmpr) {
        this.ideEmpr = ideEmpr;
        this.nomEmpr = nomEmpr;
    }

    public Integer getIdeEmpr() {
        return ideEmpr;
    }

    public void setIdeEmpr(Integer ideEmpr) {
        this.ideEmpr = ideEmpr;
    }

    public String getNomEmpr() {
        return nomEmpr;
    }

    public void setNomEmpr(String nomEmpr) {
        this.nomEmpr = nomEmpr;
    }

    public String getContactoEmpr() {
        return contactoEmpr;
    }

    public void setContactoEmpr(String contactoEmpr) {
        this.contactoEmpr = contactoEmpr;
    }

    public String getRepresentanteEmpr() {
        return representanteEmpr;
    }

    public void setRepresentanteEmpr(String representanteEmpr) {
        this.representanteEmpr = representanteEmpr;
    }

    public String getNomCortoEmpr() {
        return nomCortoEmpr;
    }

    public void setNomCortoEmpr(String nomCortoEmpr) {
        this.nomCortoEmpr = nomCortoEmpr;
    }

    public String getMailEmpr() {
        return mailEmpr;
    }

    public void setMailEmpr(String mailEmpr) {
        this.mailEmpr = mailEmpr;
    }

    public String getPaginaEmpr() {
        return paginaEmpr;
    }

    public void setPaginaEmpr(String paginaEmpr) {
        this.paginaEmpr = paginaEmpr;
    }

    public String getIdentificacionEmpr() {
        return identificacionEmpr;
    }

    public void setIdentificacionEmpr(String identificacionEmpr) {
        this.identificacionEmpr = identificacionEmpr;
    }

    public String getDireccionEmpr() {
        return direccionEmpr;
    }

    public void setDireccionEmpr(String direccionEmpr) {
        this.direccionEmpr = direccionEmpr;
    }

    public String getTelefonoEmpr() {
        return telefonoEmpr;
    }

    public void setTelefonoEmpr(String telefonoEmpr) {
        this.telefonoEmpr = telefonoEmpr;
    }

    public String getLogoEmpr() {
        return logoEmpr;
    }

    public void setLogoEmpr(String logoEmpr) {
        this.logoEmpr = logoEmpr;
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

    public List<SisReglasClave> getSisReglasClaveList() {
        return sisReglasClaveList;
    }

    public void setSisReglasClaveList(List<SisReglasClave> sisReglasClaveList) {
        this.sisReglasClaveList = sisReglasClaveList;
    }

    public List<SisAuditoria> getSisAuditoriaList() {
        return sisAuditoriaList;
    }

    public void setSisAuditoriaList(List<SisAuditoria> sisAuditoriaList) {
        this.sisAuditoriaList = sisAuditoriaList;
    }

    public List<SisUsuario> getSisUsuarioList() {
        return sisUsuarioList;
    }

    public void setSisUsuarioList(List<SisUsuario> sisUsuarioList) {
        this.sisUsuarioList = sisUsuarioList;
    }

    public List<SisPeriodoClave> getSisPeriodoClaveList() {
        return sisPeriodoClaveList;
    }

    public void setSisPeriodoClaveList(List<SisPeriodoClave> sisPeriodoClaveList) {
        this.sisPeriodoClaveList = sisPeriodoClaveList;
    }

    public List<SisSucursal> getSisSucursalList() {
        return sisSucursalList;
    }

    public void setSisSucursalList(List<SisSucursal> sisSucursalList) {
        this.sisSucursalList = sisSucursalList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideEmpr != null ? ideEmpr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisEmpresa)) {
            return false;
        }
        SisEmpresa other = (SisEmpresa) object;
        if ((this.ideEmpr == null && other.ideEmpr != null) || (this.ideEmpr != null && !this.ideEmpr.equals(other.ideEmpr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisEmpresa[ ideEmpr=" + ideEmpr + " ]";
    }
    
}
