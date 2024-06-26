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
@Table(name = "sis_tabla", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisTabla.findAll", query = "SELECT s FROM SisTabla s"),
    @NamedQuery(name = "SisTabla.findByIdeTabl", query = "SELECT s FROM SisTabla s WHERE s.ideTabl = :ideTabl"),
    @NamedQuery(name = "SisTabla.findByNumeroTabl", query = "SELECT s FROM SisTabla s WHERE s.numeroTabl = :numeroTabl"),
    @NamedQuery(name = "SisTabla.findByTablaTabl", query = "SELECT s FROM SisTabla s WHERE s.tablaTabl = :tablaTabl"),
    @NamedQuery(name = "SisTabla.findByPrimariaTabl", query = "SELECT s FROM SisTabla s WHERE s.primariaTabl = :primariaTabl"),
    @NamedQuery(name = "SisTabla.findByNombreTabl", query = "SELECT s FROM SisTabla s WHERE s.nombreTabl = :nombreTabl"),
    @NamedQuery(name = "SisTabla.findByForaneaTabl", query = "SELECT s FROM SisTabla s WHERE s.foraneaTabl = :foraneaTabl"),
    @NamedQuery(name = "SisTabla.findByPadreTabl", query = "SELECT s FROM SisTabla s WHERE s.padreTabl = :padreTabl"),
    @NamedQuery(name = "SisTabla.findByOrdenTabl", query = "SELECT s FROM SisTabla s WHERE s.ordenTabl = :ordenTabl"),
    @NamedQuery(name = "SisTabla.findByFilasTabl", query = "SELECT s FROM SisTabla s WHERE s.filasTabl = :filasTabl"),
    @NamedQuery(name = "SisTabla.findByUsuarioIngre", query = "SELECT s FROM SisTabla s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisTabla.findByFechaIngre", query = "SELECT s FROM SisTabla s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisTabla.findByUsuarioActua", query = "SELECT s FROM SisTabla s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisTabla.findByFechaActua", query = "SELECT s FROM SisTabla s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisTabla.findByHoraIngre", query = "SELECT s FROM SisTabla s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisTabla.findByHoraActua", query = "SELECT s FROM SisTabla s WHERE s.horaActua = :horaActua"),
    @NamedQuery(name = "SisTabla.findByFormularioTabl", query = "SELECT s FROM SisTabla s WHERE s.formularioTabl = :formularioTabl"),
    @NamedQuery(name = "SisTabla.findByGeneraPrimariaTabl", query = "SELECT s FROM SisTabla s WHERE s.generaPrimariaTabl = :generaPrimariaTabl")})
public class SisTabla implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tabl", nullable = false)
    private Integer ideTabl;
    @Column(name = "numero_tabl")
    private Integer numeroTabl;
    @Size(max = 50)
    @Column(name = "tabla_tabl", length = 50)
    private String tablaTabl;
    @Size(max = 50)
    @Column(name = "primaria_tabl", length = 50)
    private String primariaTabl;
    @Size(max = 50)
    @Column(name = "nombre_tabl", length = 50)
    private String nombreTabl;
    @Size(max = 50)
    @Column(name = "foranea_tabl", length = 50)
    private String foraneaTabl;
    @Size(max = 50)
    @Column(name = "padre_tabl", length = 50)
    private String padreTabl;
    @Size(max = 50)
    @Column(name = "orden_tabl", length = 50)
    private String ordenTabl;
    @Column(name = "filas_tabl")
    private Integer filasTabl;
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
    @Column(name = "formulario_tabl")
    private Boolean formularioTabl;
    @Column(name = "genera_primaria_tabl")
    private Boolean generaPrimariaTabl;
    @OneToMany(mappedBy = "ideTabl")
    private List<SisCampo> sisCampoList;
    @OneToMany(mappedBy = "ideTabl")
    private List<SisCombo> sisComboList;
    @JoinColumn(name = "ide_opci", referencedColumnName = "ide_opci")
    @ManyToOne
    private SisOpcion ideOpci;

    public SisTabla() {
    }

    public SisTabla(Integer ideTabl) {
        this.ideTabl = ideTabl;
    }

    public Integer getIdeTabl() {
        return ideTabl;
    }

    public void setIdeTabl(Integer ideTabl) {
        this.ideTabl = ideTabl;
    }

    public Integer getNumeroTabl() {
        return numeroTabl;
    }

    public void setNumeroTabl(Integer numeroTabl) {
        this.numeroTabl = numeroTabl;
    }

    public String getTablaTabl() {
        return tablaTabl;
    }

    public void setTablaTabl(String tablaTabl) {
        this.tablaTabl = tablaTabl;
    }

    public String getPrimariaTabl() {
        return primariaTabl;
    }

    public void setPrimariaTabl(String primariaTabl) {
        this.primariaTabl = primariaTabl;
    }

    public String getNombreTabl() {
        return nombreTabl;
    }

    public void setNombreTabl(String nombreTabl) {
        this.nombreTabl = nombreTabl;
    }

    public String getForaneaTabl() {
        return foraneaTabl;
    }

    public void setForaneaTabl(String foraneaTabl) {
        this.foraneaTabl = foraneaTabl;
    }

    public String getPadreTabl() {
        return padreTabl;
    }

    public void setPadreTabl(String padreTabl) {
        this.padreTabl = padreTabl;
    }

    public String getOrdenTabl() {
        return ordenTabl;
    }

    public void setOrdenTabl(String ordenTabl) {
        this.ordenTabl = ordenTabl;
    }

    public Integer getFilasTabl() {
        return filasTabl;
    }

    public void setFilasTabl(Integer filasTabl) {
        this.filasTabl = filasTabl;
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

    public Boolean getFormularioTabl() {
        return formularioTabl;
    }

    public void setFormularioTabl(Boolean formularioTabl) {
        this.formularioTabl = formularioTabl;
    }

    public Boolean getGeneraPrimariaTabl() {
        return generaPrimariaTabl;
    }

    public void setGeneraPrimariaTabl(Boolean generaPrimariaTabl) {
        this.generaPrimariaTabl = generaPrimariaTabl;
    }

    public List<SisCampo> getSisCampoList() {
        return sisCampoList;
    }

    public void setSisCampoList(List<SisCampo> sisCampoList) {
        this.sisCampoList = sisCampoList;
    }

    public List<SisCombo> getSisComboList() {
        return sisComboList;
    }

    public void setSisComboList(List<SisCombo> sisComboList) {
        this.sisComboList = sisComboList;
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
        hash += (ideTabl != null ? ideTabl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisTabla)) {
            return false;
        }
        SisTabla other = (SisTabla) object;
        if ((this.ideTabl == null && other.ideTabl != null) || (this.ideTabl != null && !this.ideTabl.equals(other.ideTabl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisTabla[ ideTabl=" + ideTabl + " ]";
    }
    
}
