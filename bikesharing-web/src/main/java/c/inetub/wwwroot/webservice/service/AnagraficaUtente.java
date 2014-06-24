
package c.inetub.wwwroot.webservice.service;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AnagraficaUtente complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AnagraficaUtente">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IdUtente" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Nome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Cognome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ProvinciaNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DataNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Sesso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CodiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IndirizzoResidenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ComuneResidenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ProvinciaResidenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Telefono" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Cellulare" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Fax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Assicurazione" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="IDProvinciaNascita" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IDProvinciaResidenza" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NumeroTessera" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CreditoTessera" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ImportoRicarica" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ImportoCauzione" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ImportoAssicurazione" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Abbonamento" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TipologiaTessera" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="NumeroTesseraSpecifico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnagraficaUtente", propOrder = {
    "idUtente",
    "nome",
    "cognome",
    "comuneNascita",
    "provinciaNascita",
    "dataNascita",
    "sesso",
    "codiceFiscale",
    "indirizzoResidenza",
    "comuneResidenza",
    "provinciaResidenza",
    "cap",
    "telefono",
    "cellulare",
    "fax",
    "email",
    "assicurazione",
    "idProvinciaNascita",
    "idProvinciaResidenza",
    "password",
    "numeroTessera",
    "creditoTessera",
    "importoRicarica",
    "importoCauzione",
    "importoAssicurazione",
    "abbonamento",
    "tipologiaTessera",
    "numeroTesseraSpecifico"
})
public class AnagraficaUtente {

    @XmlElement(name = "IdUtente")
    protected int idUtente;
    @XmlElement(name = "Nome")
    protected String nome;
    @XmlElement(name = "Cognome")
    protected String cognome;
    @XmlElement(name = "ComuneNascita")
    protected String comuneNascita;
    @XmlElement(name = "ProvinciaNascita")
    protected String provinciaNascita;
    @XmlElement(name = "DataNascita")
    protected String dataNascita;
    @XmlElement(name = "Sesso")
    protected String sesso;
    @XmlElement(name = "CodiceFiscale")
    protected String codiceFiscale;
    @XmlElement(name = "IndirizzoResidenza")
    protected String indirizzoResidenza;
    @XmlElement(name = "ComuneResidenza")
    protected String comuneResidenza;
    @XmlElement(name = "ProvinciaResidenza")
    protected String provinciaResidenza;
    @XmlElement(name = "CAP")
    protected String cap;
    @XmlElement(name = "Telefono")
    protected String telefono;
    @XmlElement(name = "Cellulare")
    protected String cellulare;
    @XmlElement(name = "Fax")
    protected String fax;
    @XmlElement(name = "Email")
    protected String email;
    @XmlElement(name = "Assicurazione")
    protected boolean assicurazione;
    @XmlElement(name = "IDProvinciaNascita")
    protected int idProvinciaNascita;
    @XmlElement(name = "IDProvinciaResidenza")
    protected int idProvinciaResidenza;
    @XmlElement(name = "Password")
    protected String password;
    @XmlElement(name = "NumeroTessera")
    protected String numeroTessera;
    @XmlElement(name = "CreditoTessera", required = true)
    protected BigDecimal creditoTessera;
    @XmlElement(name = "ImportoRicarica", required = true)
    protected BigDecimal importoRicarica;
    @XmlElement(name = "ImportoCauzione", required = true)
    protected BigDecimal importoCauzione;
    @XmlElement(name = "ImportoAssicurazione", required = true)
    protected BigDecimal importoAssicurazione;
    @XmlElement(name = "Abbonamento")
    protected int abbonamento;
    @XmlElement(name = "TipologiaTessera")
    protected int tipologiaTessera;
    @XmlElement(name = "NumeroTesseraSpecifico")
    protected String numeroTesseraSpecifico;

    /**
     * Gets the value of the idUtente property.
     * 
     */
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Sets the value of the idUtente property.
     * 
     */
    public void setIdUtente(int value) {
        this.idUtente = value;
    }

    /**
     * Gets the value of the nome property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets the value of the nome property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Gets the value of the cognome property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Sets the value of the cognome property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCognome(String value) {
        this.cognome = value;
    }

    /**
     * Gets the value of the comuneNascita property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComuneNascita() {
        return comuneNascita;
    }

    /**
     * Sets the value of the comuneNascita property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComuneNascita(String value) {
        this.comuneNascita = value;
    }

    /**
     * Gets the value of the provinciaNascita property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvinciaNascita() {
        return provinciaNascita;
    }

    /**
     * Sets the value of the provinciaNascita property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvinciaNascita(String value) {
        this.provinciaNascita = value;
    }

    /**
     * Gets the value of the dataNascita property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataNascita() {
        return dataNascita;
    }

    /**
     * Sets the value of the dataNascita property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataNascita(String value) {
        this.dataNascita = value;
    }

    /**
     * Gets the value of the sesso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSesso() {
        return sesso;
    }

    /**
     * Sets the value of the sesso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSesso(String value) {
        this.sesso = value;
    }

    /**
     * Gets the value of the codiceFiscale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Sets the value of the codiceFiscale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscale(String value) {
        this.codiceFiscale = value;
    }

    /**
     * Gets the value of the indirizzoResidenza property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndirizzoResidenza() {
        return indirizzoResidenza;
    }

    /**
     * Sets the value of the indirizzoResidenza property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndirizzoResidenza(String value) {
        this.indirizzoResidenza = value;
    }

    /**
     * Gets the value of the comuneResidenza property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComuneResidenza() {
        return comuneResidenza;
    }

    /**
     * Sets the value of the comuneResidenza property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComuneResidenza(String value) {
        this.comuneResidenza = value;
    }

    /**
     * Gets the value of the provinciaResidenza property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvinciaResidenza() {
        return provinciaResidenza;
    }

    /**
     * Sets the value of the provinciaResidenza property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvinciaResidenza(String value) {
        this.provinciaResidenza = value;
    }

    /**
     * Gets the value of the cap property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCAP() {
        return cap;
    }

    /**
     * Sets the value of the cap property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCAP(String value) {
        this.cap = value;
    }

    /**
     * Gets the value of the telefono property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Sets the value of the telefono property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefono(String value) {
        this.telefono = value;
    }

    /**
     * Gets the value of the cellulare property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCellulare() {
        return cellulare;
    }

    /**
     * Sets the value of the cellulare property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCellulare(String value) {
        this.cellulare = value;
    }

    /**
     * Gets the value of the fax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFax() {
        return fax;
    }

    /**
     * Sets the value of the fax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFax(String value) {
        this.fax = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the assicurazione property.
     * 
     */
    public boolean isAssicurazione() {
        return assicurazione;
    }

    /**
     * Sets the value of the assicurazione property.
     * 
     */
    public void setAssicurazione(boolean value) {
        this.assicurazione = value;
    }

    /**
     * Gets the value of the idProvinciaNascita property.
     * 
     */
    public int getIDProvinciaNascita() {
        return idProvinciaNascita;
    }

    /**
     * Sets the value of the idProvinciaNascita property.
     * 
     */
    public void setIDProvinciaNascita(int value) {
        this.idProvinciaNascita = value;
    }

    /**
     * Gets the value of the idProvinciaResidenza property.
     * 
     */
    public int getIDProvinciaResidenza() {
        return idProvinciaResidenza;
    }

    /**
     * Sets the value of the idProvinciaResidenza property.
     * 
     */
    public void setIDProvinciaResidenza(int value) {
        this.idProvinciaResidenza = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the numeroTessera property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroTessera() {
        return numeroTessera;
    }

    /**
     * Sets the value of the numeroTessera property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroTessera(String value) {
        this.numeroTessera = value;
    }

    /**
     * Gets the value of the creditoTessera property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCreditoTessera() {
        return creditoTessera;
    }

    /**
     * Sets the value of the creditoTessera property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCreditoTessera(BigDecimal value) {
        this.creditoTessera = value;
    }

    /**
     * Gets the value of the importoRicarica property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImportoRicarica() {
        return importoRicarica;
    }

    /**
     * Sets the value of the importoRicarica property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImportoRicarica(BigDecimal value) {
        this.importoRicarica = value;
    }

    /**
     * Gets the value of the importoCauzione property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImportoCauzione() {
        return importoCauzione;
    }

    /**
     * Sets the value of the importoCauzione property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImportoCauzione(BigDecimal value) {
        this.importoCauzione = value;
    }

    /**
     * Gets the value of the importoAssicurazione property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImportoAssicurazione() {
        return importoAssicurazione;
    }

    /**
     * Sets the value of the importoAssicurazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImportoAssicurazione(BigDecimal value) {
        this.importoAssicurazione = value;
    }

    /**
     * Gets the value of the abbonamento property.
     * 
     */
    public int getAbbonamento() {
        return abbonamento;
    }

    /**
     * Sets the value of the abbonamento property.
     * 
     */
    public void setAbbonamento(int value) {
        this.abbonamento = value;
    }

    /**
     * Gets the value of the tipologiaTessera property.
     * 
     */
    public int getTipologiaTessera() {
        return tipologiaTessera;
    }

    /**
     * Sets the value of the tipologiaTessera property.
     * 
     */
    public void setTipologiaTessera(int value) {
        this.tipologiaTessera = value;
    }

    /**
     * Gets the value of the numeroTesseraSpecifico property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroTesseraSpecifico() {
        return numeroTesseraSpecifico;
    }

    /**
     * Sets the value of the numeroTesseraSpecifico property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroTesseraSpecifico(String value) {
        this.numeroTesseraSpecifico = value;
    }

}
