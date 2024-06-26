/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author awbecerra
 */
@Embeddable
public class GenPartidaGrupoCargoPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gepgc", nullable = false)
    private int ideGepgc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gegro", nullable = false)
    private int ideGegro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gecaf", nullable = false)
    private int ideGecaf;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sucu", nullable = false)
    private int ideSucu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gedep", nullable = false)
    private int ideGedep;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_geare", nullable = false)
    private int ideGeare;

    public GenPartidaGrupoCargoPK() {
    }

    public GenPartidaGrupoCargoPK(int ideGepgc, int ideGegro, int ideGecaf, int ideSucu, int ideGedep, int ideGeare) {
        this.ideGepgc = ideGepgc;
        this.ideGegro = ideGegro;
        this.ideGecaf = ideGecaf;
        this.ideSucu = ideSucu;
        this.ideGedep = ideGedep;
        this.ideGeare = ideGeare;
    }

    public int getIdeGepgc() {
        return ideGepgc;
    }

    public void setIdeGepgc(int ideGepgc) {
        this.ideGepgc = ideGepgc;
    }

    public int getIdeGegro() {
        return ideGegro;
    }

    public void setIdeGegro(int ideGegro) {
        this.ideGegro = ideGegro;
    }

    public int getIdeGecaf() {
        return ideGecaf;
    }

    public void setIdeGecaf(int ideGecaf) {
        this.ideGecaf = ideGecaf;
    }

    public int getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(int ideSucu) {
        this.ideSucu = ideSucu;
    }

    public int getIdeGedep() {
        return ideGedep;
    }

    public void setIdeGedep(int ideGedep) {
        this.ideGedep = ideGedep;
    }

    public int getIdeGeare() {
        return ideGeare;
    }

    public void setIdeGeare(int ideGeare) {
        this.ideGeare = ideGeare;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideGepgc;
        hash += (int) ideGegro;
        hash += (int) ideGecaf;
        hash += (int) ideSucu;
        hash += (int) ideGedep;
        hash += (int) ideGeare;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenPartidaGrupoCargoPK)) {
            return false;
        }
        GenPartidaGrupoCargoPK other = (GenPartidaGrupoCargoPK) object;
        if (this.ideGepgc != other.ideGepgc) {
            return false;
        }
        if (this.ideGegro != other.ideGegro) {
            return false;
        }
        if (this.ideGecaf != other.ideGecaf) {
            return false;
        }
        if (this.ideSucu != other.ideSucu) {
            return false;
        }
        if (this.ideGedep != other.ideGedep) {
            return false;
        }
        if (this.ideGeare != other.ideGeare) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenPartidaGrupoCargoPK[ ideGepgc=" + ideGepgc + ", ideGegro=" + ideGegro + ", ideGecaf=" + ideGecaf + ", ideSucu=" + ideSucu + ", ideGedep=" + ideGedep + ", ideGeare=" + ideGeare + " ]";
    }
    
}
