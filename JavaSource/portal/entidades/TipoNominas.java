/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(name = "tipo_nominas", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TipoNominas.findAll", query = "SELECT t FROM TipoNominas t"),
    @NamedQuery(name = "TipoNominas.findByIdeCotra", query = "SELECT t FROM TipoNominas t WHERE t.ideCotra = :ideCotra"),
    @NamedQuery(name = "TipoNominas.findByMes", query = "SELECT t FROM TipoNominas t WHERE t.mes = :mes"),
    @NamedQuery(name = "TipoNominas.findByDetalle", query = "SELECT t FROM TipoNominas t WHERE t.detalle = :detalle"),
    @NamedQuery(name = "TipoNominas.findByEmplcdid", query = "SELECT t FROM TipoNominas t WHERE t.emplcdid = :emplcdid"),
    @NamedQuery(name = "TipoNominas.findByGrpocdgo", query = "SELECT t FROM TipoNominas t WHERE t.grpocdgo = :grpocdgo"),
    @NamedQuery(name = "TipoNominas.findByEmplnmbr", query = "SELECT t FROM TipoNominas t WHERE t.emplnmbr = :emplnmbr"),
    @NamedQuery(name = "TipoNominas.findByEmplapll", query = "SELECT t FROM TipoNominas t WHERE t.emplapll = :emplapll"),
    @NamedQuery(name = "TipoNominas.findByEmplctgr", query = "SELECT t FROM TipoNominas t WHERE t.emplctgr = :emplctgr"),
    @NamedQuery(name = "TipoNominas.findByEmplcrgo", query = "SELECT t FROM TipoNominas t WHERE t.emplcrgo = :emplcrgo"),
    @NamedQuery(name = "TipoNominas.findByInga", query = "SELECT t FROM TipoNominas t WHERE t.inga = :inga"),
    @NamedQuery(name = "TipoNominas.findByIngb", query = "SELECT t FROM TipoNominas t WHERE t.ingb = :ingb"),
    @NamedQuery(name = "TipoNominas.findByIngc", query = "SELECT t FROM TipoNominas t WHERE t.ingc = :ingc"),
    @NamedQuery(name = "TipoNominas.findByIngd", query = "SELECT t FROM TipoNominas t WHERE t.ingd = :ingd"),
    @NamedQuery(name = "TipoNominas.findByInge", query = "SELECT t FROM TipoNominas t WHERE t.inge = :inge"),
    @NamedQuery(name = "TipoNominas.findByIngf", query = "SELECT t FROM TipoNominas t WHERE t.ingf = :ingf"),
    @NamedQuery(name = "TipoNominas.findByIngg", query = "SELECT t FROM TipoNominas t WHERE t.ingg = :ingg"),
    @NamedQuery(name = "TipoNominas.findByIngh", query = "SELECT t FROM TipoNominas t WHERE t.ingh = :ingh"),
    @NamedQuery(name = "TipoNominas.findByIngi", query = "SELECT t FROM TipoNominas t WHERE t.ingi = :ingi"),
    @NamedQuery(name = "TipoNominas.findByIngj", query = "SELECT t FROM TipoNominas t WHERE t.ingj = :ingj"),
    @NamedQuery(name = "TipoNominas.findByIngk", query = "SELECT t FROM TipoNominas t WHERE t.ingk = :ingk"),
    @NamedQuery(name = "TipoNominas.findByIngl", query = "SELECT t FROM TipoNominas t WHERE t.ingl = :ingl"),
    @NamedQuery(name = "TipoNominas.findByIngm", query = "SELECT t FROM TipoNominas t WHERE t.ingm = :ingm"),
    @NamedQuery(name = "TipoNominas.findByIngn", query = "SELECT t FROM TipoNominas t WHERE t.ingn = :ingn"),
    @NamedQuery(name = "TipoNominas.findByIngo", query = "SELECT t FROM TipoNominas t WHERE t.ingo = :ingo"),
    @NamedQuery(name = "TipoNominas.findByIngp", query = "SELECT t FROM TipoNominas t WHERE t.ingp = :ingp"),
    @NamedQuery(name = "TipoNominas.findByIngq", query = "SELECT t FROM TipoNominas t WHERE t.ingq = :ingq"),
    @NamedQuery(name = "TipoNominas.findByIngr", query = "SELECT t FROM TipoNominas t WHERE t.ingr = :ingr"),
    @NamedQuery(name = "TipoNominas.findByIngs", query = "SELECT t FROM TipoNominas t WHERE t.ings = :ings"),
    @NamedQuery(name = "TipoNominas.findByIngt", query = "SELECT t FROM TipoNominas t WHERE t.ingt = :ingt"),
    @NamedQuery(name = "TipoNominas.findByIngu", query = "SELECT t FROM TipoNominas t WHERE t.ingu = :ingu"),
    @NamedQuery(name = "TipoNominas.findByIngv", query = "SELECT t FROM TipoNominas t WHERE t.ingv = :ingv"),
    @NamedQuery(name = "TipoNominas.findByIngw", query = "SELECT t FROM TipoNominas t WHERE t.ingw = :ingw"),
    @NamedQuery(name = "TipoNominas.findByIngx", query = "SELECT t FROM TipoNominas t WHERE t.ingx = :ingx"),
    @NamedQuery(name = "TipoNominas.findByIngy", query = "SELECT t FROM TipoNominas t WHERE t.ingy = :ingy"),
    @NamedQuery(name = "TipoNominas.findByIngz", query = "SELECT t FROM TipoNominas t WHERE t.ingz = :ingz"),
    @NamedQuery(name = "TipoNominas.findByOEgreso", query = "SELECT t FROM TipoNominas t WHERE t.oEgreso = :oEgreso"),
    @NamedQuery(name = "TipoNominas.findByEgreso", query = "SELECT t FROM TipoNominas t WHERE t.egreso = :egreso"),
    @NamedQuery(name = "TipoNominas.findByMega", query = "SELECT t FROM TipoNominas t WHERE t.mega = :mega"),
    @NamedQuery(name = "TipoNominas.findByMegb", query = "SELECT t FROM TipoNominas t WHERE t.megb = :megb"),
    @NamedQuery(name = "TipoNominas.findByMegc", query = "SELECT t FROM TipoNominas t WHERE t.megc = :megc"),
    @NamedQuery(name = "TipoNominas.findByMegd", query = "SELECT t FROM TipoNominas t WHERE t.megd = :megd"),
    @NamedQuery(name = "TipoNominas.findByMege", query = "SELECT t FROM TipoNominas t WHERE t.mege = :mege"),
    @NamedQuery(name = "TipoNominas.findByMegf", query = "SELECT t FROM TipoNominas t WHERE t.megf = :megf"),
    @NamedQuery(name = "TipoNominas.findByMegg", query = "SELECT t FROM TipoNominas t WHERE t.megg = :megg"),
    @NamedQuery(name = "TipoNominas.findByMegh", query = "SELECT t FROM TipoNominas t WHERE t.megh = :megh"),
    @NamedQuery(name = "TipoNominas.findByMegi", query = "SELECT t FROM TipoNominas t WHERE t.megi = :megi"),
    @NamedQuery(name = "TipoNominas.findByMegj", query = "SELECT t FROM TipoNominas t WHERE t.megj = :megj"),
    @NamedQuery(name = "TipoNominas.findByMegk", query = "SELECT t FROM TipoNominas t WHERE t.megk = :megk"),
    @NamedQuery(name = "TipoNominas.findByMegl", query = "SELECT t FROM TipoNominas t WHERE t.megl = :megl"),
    @NamedQuery(name = "TipoNominas.findByMegm", query = "SELECT t FROM TipoNominas t WHERE t.megm = :megm"),
    @NamedQuery(name = "TipoNominas.findByMegn", query = "SELECT t FROM TipoNominas t WHERE t.megn = :megn"),
    @NamedQuery(name = "TipoNominas.findByMego", query = "SELECT t FROM TipoNominas t WHERE t.mego = :mego"),
    @NamedQuery(name = "TipoNominas.findByMegp", query = "SELECT t FROM TipoNominas t WHERE t.megp = :megp"),
    @NamedQuery(name = "TipoNominas.findByMegq", query = "SELECT t FROM TipoNominas t WHERE t.megq = :megq"),
    @NamedQuery(name = "TipoNominas.findByMegr", query = "SELECT t FROM TipoNominas t WHERE t.megr = :megr"),
    @NamedQuery(name = "TipoNominas.findByMegs", query = "SELECT t FROM TipoNominas t WHERE t.megs = :megs"),
    @NamedQuery(name = "TipoNominas.findByMegt", query = "SELECT t FROM TipoNominas t WHERE t.megt = :megt"),
    @NamedQuery(name = "TipoNominas.findByMegu", query = "SELECT t FROM TipoNominas t WHERE t.megu = :megu"),
    @NamedQuery(name = "TipoNominas.findByMegv", query = "SELECT t FROM TipoNominas t WHERE t.megv = :megv"),
    @NamedQuery(name = "TipoNominas.findByMegw", query = "SELECT t FROM TipoNominas t WHERE t.megw = :megw"),
    @NamedQuery(name = "TipoNominas.findByMegx", query = "SELECT t FROM TipoNominas t WHERE t.megx = :megx"),
    @NamedQuery(name = "TipoNominas.findByMegy", query = "SELECT t FROM TipoNominas t WHERE t.megy = :megy"),
    @NamedQuery(name = "TipoNominas.findByMegz", query = "SELECT t FROM TipoNominas t WHERE t.megz = :megz"),
    @NamedQuery(name = "TipoNominas.findByOEgreso1", query = "SELECT t FROM TipoNominas t WHERE t.oEgreso1 = :oEgreso1"),
    @NamedQuery(name = "TipoNominas.findByEgreso1", query = "SELECT t FROM TipoNominas t WHERE t.egreso1 = :egreso1"),
    @NamedQuery(name = "TipoNominas.findByTotal", query = "SELECT t FROM TipoNominas t WHERE t.total = :total"),
    @NamedQuery(name = "TipoNominas.findByPatronal", query = "SELECT t FROM TipoNominas t WHERE t.patronal = :patronal"),
    @NamedQuery(name = "TipoNominas.findByBaseiess", query = "SELECT t FROM TipoNominas t WHERE t.baseiess = :baseiess"),
    @NamedQuery(name = "TipoNominas.findByIece", query = "SELECT t FROM TipoNominas t WHERE t.iece = :iece"),
    @NamedQuery(name = "TipoNominas.findBySecap", query = "SELECT t FROM TipoNominas t WHERE t.secap = :secap")})
public class TipoNominas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ide_cotra", nullable = false)
    private Integer ideCotra;
    @Size(max = 20)
    @Column(name = "mes", length = 20)
    private String mes;
    @Size(max = 50)
    @Column(name = "detalle", length = 50)
    private String detalle;
    @Size(max = 10)
    @Column(name = "emplcdid", length = 10)
    private String emplcdid;
    @Size(max = 20)
    @Column(name = "grpocdgo", length = 20)
    private String grpocdgo;
    @Size(max = 50)
    @Column(name = "emplnmbr", length = 50)
    private String emplnmbr;
    @Size(max = 50)
    @Column(name = "emplapll", length = 50)
    private String emplapll;
    @Column(name = "emplctgr")
    private BigInteger emplctgr;
    @Size(max = 20)
    @Column(name = "emplcrgo", length = 20)
    private String emplcrgo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "inga", precision = 10, scale = 2)
    private BigDecimal inga;
    @Column(name = "ingb", precision = 10, scale = 2)
    private BigDecimal ingb;
    @Column(name = "ingc", precision = 10, scale = 2)
    private BigDecimal ingc;
    @Column(name = "ingd", precision = 10, scale = 2)
    private BigDecimal ingd;
    @Column(name = "inge", precision = 10, scale = 2)
    private BigDecimal inge;
    @Column(name = "ingf", precision = 10, scale = 2)
    private BigDecimal ingf;
    @Column(name = "ingg", precision = 10, scale = 2)
    private BigDecimal ingg;
    @Column(name = "ingh", precision = 10, scale = 2)
    private BigDecimal ingh;
    @Column(name = "ingi", precision = 10, scale = 2)
    private BigDecimal ingi;
    @Column(name = "ingj", precision = 10, scale = 2)
    private BigDecimal ingj;
    @Column(name = "ingk", precision = 10, scale = 2)
    private BigDecimal ingk;
    @Column(name = "ingl", precision = 10, scale = 2)
    private BigDecimal ingl;
    @Column(name = "ingm", precision = 10, scale = 2)
    private BigDecimal ingm;
    @Column(name = "ingn", precision = 10, scale = 2)
    private BigDecimal ingn;
    @Column(name = "ingo", precision = 10, scale = 2)
    private BigDecimal ingo;
    @Column(name = "ingp", precision = 10, scale = 2)
    private BigDecimal ingp;
    @Column(name = "ingq", precision = 10, scale = 2)
    private BigDecimal ingq;
    @Column(name = "ingr", precision = 10, scale = 2)
    private BigDecimal ingr;
    @Column(name = "ings", precision = 10, scale = 2)
    private BigDecimal ings;
    @Column(name = "ingt", precision = 10, scale = 2)
    private BigDecimal ingt;
    @Column(name = "ingu", precision = 10, scale = 2)
    private BigDecimal ingu;
    @Column(name = "ingv", precision = 10, scale = 2)
    private BigDecimal ingv;
    @Column(name = "ingw", precision = 10, scale = 2)
    private BigDecimal ingw;
    @Column(name = "ingx", precision = 10, scale = 2)
    private BigDecimal ingx;
    @Column(name = "ingy", precision = 10, scale = 2)
    private BigDecimal ingy;
    @Column(name = "ingz", precision = 10, scale = 2)
    private BigDecimal ingz;
    @Column(name = "o_egreso", precision = 10, scale = 2)
    private BigDecimal oEgreso;
    @Column(name = "egreso", precision = 10, scale = 2)
    private BigDecimal egreso;
    @Column(name = "mega", precision = 10, scale = 2)
    private BigDecimal mega;
    @Column(name = "megb", precision = 10, scale = 2)
    private BigDecimal megb;
    @Column(name = "megc", precision = 10, scale = 2)
    private BigDecimal megc;
    @Column(name = "megd", precision = 10, scale = 2)
    private BigDecimal megd;
    @Column(name = "mege", precision = 10, scale = 2)
    private BigDecimal mege;
    @Column(name = "megf", precision = 10, scale = 2)
    private BigDecimal megf;
    @Column(name = "megg", precision = 10, scale = 2)
    private BigDecimal megg;
    @Column(name = "megh", precision = 10, scale = 2)
    private BigDecimal megh;
    @Column(name = "megi", precision = 10, scale = 2)
    private BigDecimal megi;
    @Column(name = "megj", precision = 10, scale = 2)
    private BigDecimal megj;
    @Column(name = "megk", precision = 10, scale = 2)
    private BigDecimal megk;
    @Column(name = "megl", precision = 10, scale = 2)
    private BigDecimal megl;
    @Column(name = "megm", precision = 10, scale = 2)
    private BigDecimal megm;
    @Column(name = "megn", precision = 10, scale = 2)
    private BigDecimal megn;
    @Column(name = "mego", precision = 10, scale = 2)
    private BigDecimal mego;
    @Column(name = "megp", precision = 10, scale = 2)
    private BigDecimal megp;
    @Column(name = "megq", precision = 10, scale = 2)
    private BigDecimal megq;
    @Column(name = "megr", precision = 10, scale = 2)
    private BigDecimal megr;
    @Column(name = "megs", precision = 10, scale = 2)
    private BigDecimal megs;
    @Column(name = "megt", precision = 10, scale = 2)
    private BigDecimal megt;
    @Column(name = "megu", precision = 10, scale = 2)
    private BigDecimal megu;
    @Column(name = "megv", precision = 10, scale = 2)
    private BigDecimal megv;
    @Column(name = "megw", precision = 10, scale = 2)
    private BigDecimal megw;
    @Column(name = "megx", precision = 10, scale = 2)
    private BigDecimal megx;
    @Column(name = "megy", precision = 10, scale = 2)
    private BigDecimal megy;
    @Column(name = "megz", precision = 10, scale = 2)
    private BigDecimal megz;
    @Column(name = "o_egreso1", precision = 10, scale = 2)
    private BigDecimal oEgreso1;
    @Column(name = "egreso_1", precision = 10, scale = 2)
    private BigDecimal egreso1;
    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;
    @Column(name = "patronal", precision = 10, scale = 2)
    private BigDecimal patronal;
    @Column(name = "baseiess", precision = 10, scale = 2)
    private BigDecimal baseiess;
    @Column(name = "iece", precision = 10, scale = 2)
    private BigDecimal iece;
    @Column(name = "secap", precision = 10, scale = 2)
    private BigDecimal secap;

    public TipoNominas() {
    }

    public TipoNominas(Integer ideCotra) {
        this.ideCotra = ideCotra;
    }

    public Integer getIdeCotra() {
        return ideCotra;
    }

    public void setIdeCotra(Integer ideCotra) {
        this.ideCotra = ideCotra;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getEmplcdid() {
        return emplcdid;
    }

    public void setEmplcdid(String emplcdid) {
        this.emplcdid = emplcdid;
    }

    public String getGrpocdgo() {
        return grpocdgo;
    }

    public void setGrpocdgo(String grpocdgo) {
        this.grpocdgo = grpocdgo;
    }

    public String getEmplnmbr() {
        return emplnmbr;
    }

    public void setEmplnmbr(String emplnmbr) {
        this.emplnmbr = emplnmbr;
    }

    public String getEmplapll() {
        return emplapll;
    }

    public void setEmplapll(String emplapll) {
        this.emplapll = emplapll;
    }

    public BigInteger getEmplctgr() {
        return emplctgr;
    }

    public void setEmplctgr(BigInteger emplctgr) {
        this.emplctgr = emplctgr;
    }

    public String getEmplcrgo() {
        return emplcrgo;
    }

    public void setEmplcrgo(String emplcrgo) {
        this.emplcrgo = emplcrgo;
    }

    public BigDecimal getInga() {
        return inga;
    }

    public void setInga(BigDecimal inga) {
        this.inga = inga;
    }

    public BigDecimal getIngb() {
        return ingb;
    }

    public void setIngb(BigDecimal ingb) {
        this.ingb = ingb;
    }

    public BigDecimal getIngc() {
        return ingc;
    }

    public void setIngc(BigDecimal ingc) {
        this.ingc = ingc;
    }

    public BigDecimal getIngd() {
        return ingd;
    }

    public void setIngd(BigDecimal ingd) {
        this.ingd = ingd;
    }

    public BigDecimal getInge() {
        return inge;
    }

    public void setInge(BigDecimal inge) {
        this.inge = inge;
    }

    public BigDecimal getIngf() {
        return ingf;
    }

    public void setIngf(BigDecimal ingf) {
        this.ingf = ingf;
    }

    public BigDecimal getIngg() {
        return ingg;
    }

    public void setIngg(BigDecimal ingg) {
        this.ingg = ingg;
    }

    public BigDecimal getIngh() {
        return ingh;
    }

    public void setIngh(BigDecimal ingh) {
        this.ingh = ingh;
    }

    public BigDecimal getIngi() {
        return ingi;
    }

    public void setIngi(BigDecimal ingi) {
        this.ingi = ingi;
    }

    public BigDecimal getIngj() {
        return ingj;
    }

    public void setIngj(BigDecimal ingj) {
        this.ingj = ingj;
    }

    public BigDecimal getIngk() {
        return ingk;
    }

    public void setIngk(BigDecimal ingk) {
        this.ingk = ingk;
    }

    public BigDecimal getIngl() {
        return ingl;
    }

    public void setIngl(BigDecimal ingl) {
        this.ingl = ingl;
    }

    public BigDecimal getIngm() {
        return ingm;
    }

    public void setIngm(BigDecimal ingm) {
        this.ingm = ingm;
    }

    public BigDecimal getIngn() {
        return ingn;
    }

    public void setIngn(BigDecimal ingn) {
        this.ingn = ingn;
    }

    public BigDecimal getIngo() {
        return ingo;
    }

    public void setIngo(BigDecimal ingo) {
        this.ingo = ingo;
    }

    public BigDecimal getIngp() {
        return ingp;
    }

    public void setIngp(BigDecimal ingp) {
        this.ingp = ingp;
    }

    public BigDecimal getIngq() {
        return ingq;
    }

    public void setIngq(BigDecimal ingq) {
        this.ingq = ingq;
    }

    public BigDecimal getIngr() {
        return ingr;
    }

    public void setIngr(BigDecimal ingr) {
        this.ingr = ingr;
    }

    public BigDecimal getIngs() {
        return ings;
    }

    public void setIngs(BigDecimal ings) {
        this.ings = ings;
    }

    public BigDecimal getIngt() {
        return ingt;
    }

    public void setIngt(BigDecimal ingt) {
        this.ingt = ingt;
    }

    public BigDecimal getIngu() {
        return ingu;
    }

    public void setIngu(BigDecimal ingu) {
        this.ingu = ingu;
    }

    public BigDecimal getIngv() {
        return ingv;
    }

    public void setIngv(BigDecimal ingv) {
        this.ingv = ingv;
    }

    public BigDecimal getIngw() {
        return ingw;
    }

    public void setIngw(BigDecimal ingw) {
        this.ingw = ingw;
    }

    public BigDecimal getIngx() {
        return ingx;
    }

    public void setIngx(BigDecimal ingx) {
        this.ingx = ingx;
    }

    public BigDecimal getIngy() {
        return ingy;
    }

    public void setIngy(BigDecimal ingy) {
        this.ingy = ingy;
    }

    public BigDecimal getIngz() {
        return ingz;
    }

    public void setIngz(BigDecimal ingz) {
        this.ingz = ingz;
    }

    public BigDecimal getOEgreso() {
        return oEgreso;
    }

    public void setOEgreso(BigDecimal oEgreso) {
        this.oEgreso = oEgreso;
    }

    public BigDecimal getEgreso() {
        return egreso;
    }

    public void setEgreso(BigDecimal egreso) {
        this.egreso = egreso;
    }

    public BigDecimal getMega() {
        return mega;
    }

    public void setMega(BigDecimal mega) {
        this.mega = mega;
    }

    public BigDecimal getMegb() {
        return megb;
    }

    public void setMegb(BigDecimal megb) {
        this.megb = megb;
    }

    public BigDecimal getMegc() {
        return megc;
    }

    public void setMegc(BigDecimal megc) {
        this.megc = megc;
    }

    public BigDecimal getMegd() {
        return megd;
    }

    public void setMegd(BigDecimal megd) {
        this.megd = megd;
    }

    public BigDecimal getMege() {
        return mege;
    }

    public void setMege(BigDecimal mege) {
        this.mege = mege;
    }

    public BigDecimal getMegf() {
        return megf;
    }

    public void setMegf(BigDecimal megf) {
        this.megf = megf;
    }

    public BigDecimal getMegg() {
        return megg;
    }

    public void setMegg(BigDecimal megg) {
        this.megg = megg;
    }

    public BigDecimal getMegh() {
        return megh;
    }

    public void setMegh(BigDecimal megh) {
        this.megh = megh;
    }

    public BigDecimal getMegi() {
        return megi;
    }

    public void setMegi(BigDecimal megi) {
        this.megi = megi;
    }

    public BigDecimal getMegj() {
        return megj;
    }

    public void setMegj(BigDecimal megj) {
        this.megj = megj;
    }

    public BigDecimal getMegk() {
        return megk;
    }

    public void setMegk(BigDecimal megk) {
        this.megk = megk;
    }

    public BigDecimal getMegl() {
        return megl;
    }

    public void setMegl(BigDecimal megl) {
        this.megl = megl;
    }

    public BigDecimal getMegm() {
        return megm;
    }

    public void setMegm(BigDecimal megm) {
        this.megm = megm;
    }

    public BigDecimal getMegn() {
        return megn;
    }

    public void setMegn(BigDecimal megn) {
        this.megn = megn;
    }

    public BigDecimal getMego() {
        return mego;
    }

    public void setMego(BigDecimal mego) {
        this.mego = mego;
    }

    public BigDecimal getMegp() {
        return megp;
    }

    public void setMegp(BigDecimal megp) {
        this.megp = megp;
    }

    public BigDecimal getMegq() {
        return megq;
    }

    public void setMegq(BigDecimal megq) {
        this.megq = megq;
    }

    public BigDecimal getMegr() {
        return megr;
    }

    public void setMegr(BigDecimal megr) {
        this.megr = megr;
    }

    public BigDecimal getMegs() {
        return megs;
    }

    public void setMegs(BigDecimal megs) {
        this.megs = megs;
    }

    public BigDecimal getMegt() {
        return megt;
    }

    public void setMegt(BigDecimal megt) {
        this.megt = megt;
    }

    public BigDecimal getMegu() {
        return megu;
    }

    public void setMegu(BigDecimal megu) {
        this.megu = megu;
    }

    public BigDecimal getMegv() {
        return megv;
    }

    public void setMegv(BigDecimal megv) {
        this.megv = megv;
    }

    public BigDecimal getMegw() {
        return megw;
    }

    public void setMegw(BigDecimal megw) {
        this.megw = megw;
    }

    public BigDecimal getMegx() {
        return megx;
    }

    public void setMegx(BigDecimal megx) {
        this.megx = megx;
    }

    public BigDecimal getMegy() {
        return megy;
    }

    public void setMegy(BigDecimal megy) {
        this.megy = megy;
    }

    public BigDecimal getMegz() {
        return megz;
    }

    public void setMegz(BigDecimal megz) {
        this.megz = megz;
    }

    public BigDecimal getOEgreso1() {
        return oEgreso1;
    }

    public void setOEgreso1(BigDecimal oEgreso1) {
        this.oEgreso1 = oEgreso1;
    }

    public BigDecimal getEgreso1() {
        return egreso1;
    }

    public void setEgreso1(BigDecimal egreso1) {
        this.egreso1 = egreso1;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getPatronal() {
        return patronal;
    }

    public void setPatronal(BigDecimal patronal) {
        this.patronal = patronal;
    }

    public BigDecimal getBaseiess() {
        return baseiess;
    }

    public void setBaseiess(BigDecimal baseiess) {
        this.baseiess = baseiess;
    }

    public BigDecimal getIece() {
        return iece;
    }

    public void setIece(BigDecimal iece) {
        this.iece = iece;
    }

    public BigDecimal getSecap() {
        return secap;
    }

    public void setSecap(BigDecimal secap) {
        this.secap = secap;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCotra != null ? ideCotra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoNominas)) {
            return false;
        }
        TipoNominas other = (TipoNominas) object;
        if ((this.ideCotra == null && other.ideCotra != null) || (this.ideCotra != null && !this.ideCotra.equals(other.ideCotra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TipoNominas[ ideCotra=" + ideCotra + " ]";
    }
    
}
