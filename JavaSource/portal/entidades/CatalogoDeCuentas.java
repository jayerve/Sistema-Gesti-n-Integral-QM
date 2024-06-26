/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "catalogo_de_cuentas", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CatalogoDeCuentas.findAll", query = "SELECT c FROM CatalogoDeCuentas c"),
    @NamedQuery(name = "CatalogoDeCuentas.findByIdeCatalogo", query = "SELECT c FROM CatalogoDeCuentas c WHERE c.ideCatalogo = :ideCatalogo"),
    @NamedQuery(name = "CatalogoDeCuentas.findByCuenta", query = "SELECT c FROM CatalogoDeCuentas c WHERE c.cuenta = :cuenta"),
    @NamedQuery(name = "CatalogoDeCuentas.findByNombre", query = "SELECT c FROM CatalogoDeCuentas c WHERE c.nombre = :nombre")})
public class CatalogoDeCuentas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ide_catalogo", nullable = false)
    private Integer ideCatalogo;
    @Size(max = 20)
    @Column(name = "cuenta", length = 20)
    private String cuenta;
    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    public CatalogoDeCuentas() {
    }

    public CatalogoDeCuentas(Integer ideCatalogo) {
        this.ideCatalogo = ideCatalogo;
    }

    public Integer getIdeCatalogo() {
        return ideCatalogo;
    }

    public void setIdeCatalogo(Integer ideCatalogo) {
        this.ideCatalogo = ideCatalogo;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCatalogo != null ? ideCatalogo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CatalogoDeCuentas)) {
            return false;
        }
        CatalogoDeCuentas other = (CatalogoDeCuentas) object;
        if ((this.ideCatalogo == null && other.ideCatalogo != null) || (this.ideCatalogo != null && !this.ideCatalogo.equals(other.ideCatalogo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CatalogoDeCuentas[ ideCatalogo=" + ideCatalogo + " ]";
    }
    
}
