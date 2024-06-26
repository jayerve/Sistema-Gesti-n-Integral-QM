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
@Table(name = "sis_campo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisCampo.findAll", query = "SELECT s FROM SisCampo s"),
    @NamedQuery(name = "SisCampo.findByIdeCamp", query = "SELECT s FROM SisCampo s WHERE s.ideCamp = :ideCamp"),
    @NamedQuery(name = "SisCampo.findByNomCamp", query = "SELECT s FROM SisCampo s WHERE s.nomCamp = :nomCamp"),
    @NamedQuery(name = "SisCampo.findByNomVisualCamp", query = "SELECT s FROM SisCampo s WHERE s.nomVisualCamp = :nomVisualCamp"),
    @NamedQuery(name = "SisCampo.findByOrdenCamp", query = "SELECT s FROM SisCampo s WHERE s.ordenCamp = :ordenCamp"),
    @NamedQuery(name = "SisCampo.findByDefectoCamp", query = "SELECT s FROM SisCampo s WHERE s.defectoCamp = :defectoCamp"),
    @NamedQuery(name = "SisCampo.findByMascaraCamp", query = "SELECT s FROM SisCampo s WHERE s.mascaraCamp = :mascaraCamp"),
    @NamedQuery(name = "SisCampo.findByComentarioCamp", query = "SELECT s FROM SisCampo s WHERE s.comentarioCamp = :comentarioCamp"),
    @NamedQuery(name = "SisCampo.findByUsuarioIngre", query = "SELECT s FROM SisCampo s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisCampo.findByFechaIngre", query = "SELECT s FROM SisCampo s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisCampo.findByUsuarioActua", query = "SELECT s FROM SisCampo s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisCampo.findByFechaActua", query = "SELECT s FROM SisCampo s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisCampo.findByHoraIngre", query = "SELECT s FROM SisCampo s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisCampo.findByHoraActua", query = "SELECT s FROM SisCampo s WHERE s.horaActua = :horaActua"),
    @NamedQuery(name = "SisCampo.findByVisibleCamp", query = "SELECT s FROM SisCampo s WHERE s.visibleCamp = :visibleCamp"),
    @NamedQuery(name = "SisCampo.findByLecturaCamp", query = "SELECT s FROM SisCampo s WHERE s.lecturaCamp = :lecturaCamp"),
    @NamedQuery(name = "SisCampo.findByFiltroCamp", query = "SELECT s FROM SisCampo s WHERE s.filtroCamp = :filtroCamp"),
    @NamedQuery(name = "SisCampo.findByMayusculaCamp", query = "SELECT s FROM SisCampo s WHERE s.mayusculaCamp = :mayusculaCamp"),
    @NamedQuery(name = "SisCampo.findByRequeridoCamp", query = "SELECT s FROM SisCampo s WHERE s.requeridoCamp = :requeridoCamp"),
    @NamedQuery(name = "SisCampo.findByUnicoCamp", query = "SELECT s FROM SisCampo s WHERE s.unicoCamp = :unicoCamp")})
public class SisCampo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_camp", nullable = false)
    private Integer ideCamp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_camp", nullable = false, length = 50)
    private String nomCamp;
    @Size(max = 50)
    @Column(name = "nom_visual_camp", length = 50)
    private String nomVisualCamp;
    @Column(name = "orden_camp")
    private Integer ordenCamp;
    @Size(max = 100)
    @Column(name = "defecto_camp", length = 100)
    private String defectoCamp;
    @Size(max = 50)
    @Column(name = "mascara_camp", length = 50)
    private String mascaraCamp;
    @Size(max = 80)
    @Column(name = "comentario_camp", length = 80)
    private String comentarioCamp;
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
    @Column(name = "visible_camp")
    private Boolean visibleCamp;
    @Column(name = "lectura_camp")
    private Boolean lecturaCamp;
    @Column(name = "filtro_camp")
    private Boolean filtroCamp;
    @Column(name = "mayuscula_camp")
    private Boolean mayusculaCamp;
    @Column(name = "requerido_camp")
    private Boolean requeridoCamp;
    @Column(name = "unico_camp")
    private Boolean unicoCamp;
    @JoinColumn(name = "ide_tabl", referencedColumnName = "ide_tabl")
    @ManyToOne
    private SisTabla ideTabl;
    @OneToMany(mappedBy = "ideCamp")
    private List<SisPerfilCampo> sisPerfilCampoList;

    public SisCampo() {
    }

    public SisCampo(Integer ideCamp) {
        this.ideCamp = ideCamp;
    }

    public SisCampo(Integer ideCamp, String nomCamp) {
        this.ideCamp = ideCamp;
        this.nomCamp = nomCamp;
    }

    public Integer getIdeCamp() {
        return ideCamp;
    }

    public void setIdeCamp(Integer ideCamp) {
        this.ideCamp = ideCamp;
    }

    public String getNomCamp() {
        return nomCamp;
    }

    public void setNomCamp(String nomCamp) {
        this.nomCamp = nomCamp;
    }

    public String getNomVisualCamp() {
        return nomVisualCamp;
    }

    public void setNomVisualCamp(String nomVisualCamp) {
        this.nomVisualCamp = nomVisualCamp;
    }

    public Integer getOrdenCamp() {
        return ordenCamp;
    }

    public void setOrdenCamp(Integer ordenCamp) {
        this.ordenCamp = ordenCamp;
    }

    public String getDefectoCamp() {
        return defectoCamp;
    }

    public void setDefectoCamp(String defectoCamp) {
        this.defectoCamp = defectoCamp;
    }

    public String getMascaraCamp() {
        return mascaraCamp;
    }

    public void setMascaraCamp(String mascaraCamp) {
        this.mascaraCamp = mascaraCamp;
    }

    public String getComentarioCamp() {
        return comentarioCamp;
    }

    public void setComentarioCamp(String comentarioCamp) {
        this.comentarioCamp = comentarioCamp;
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

    public Boolean getVisibleCamp() {
        return visibleCamp;
    }

    public void setVisibleCamp(Boolean visibleCamp) {
        this.visibleCamp = visibleCamp;
    }

    public Boolean getLecturaCamp() {
        return lecturaCamp;
    }

    public void setLecturaCamp(Boolean lecturaCamp) {
        this.lecturaCamp = lecturaCamp;
    }

    public Boolean getFiltroCamp() {
        return filtroCamp;
    }

    public void setFiltroCamp(Boolean filtroCamp) {
        this.filtroCamp = filtroCamp;
    }

    public Boolean getMayusculaCamp() {
        return mayusculaCamp;
    }

    public void setMayusculaCamp(Boolean mayusculaCamp) {
        this.mayusculaCamp = mayusculaCamp;
    }

    public Boolean getRequeridoCamp() {
        return requeridoCamp;
    }

    public void setRequeridoCamp(Boolean requeridoCamp) {
        this.requeridoCamp = requeridoCamp;
    }

    public Boolean getUnicoCamp() {
        return unicoCamp;
    }

    public void setUnicoCamp(Boolean unicoCamp) {
        this.unicoCamp = unicoCamp;
    }

    public SisTabla getIdeTabl() {
        return ideTabl;
    }

    public void setIdeTabl(SisTabla ideTabl) {
        this.ideTabl = ideTabl;
    }

    public List<SisPerfilCampo> getSisPerfilCampoList() {
        return sisPerfilCampoList;
    }

    public void setSisPerfilCampoList(List<SisPerfilCampo> sisPerfilCampoList) {
        this.sisPerfilCampoList = sisPerfilCampoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCamp != null ? ideCamp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisCampo)) {
            return false;
        }
        SisCampo other = (SisCampo) object;
        if ((this.ideCamp == null && other.ideCamp != null) || (this.ideCamp != null && !this.ideCamp.equals(other.ideCamp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisCampo[ ideCamp=" + ideCamp + " ]";
    }
    
}
