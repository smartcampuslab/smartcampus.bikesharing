
package c.inetub.wwwroot.webservice.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RicercaTesseraUtenteResult" type="{c://inetub/wwwroot/webservice/Service.asmx}ReturnMSG"/>
 *         &lt;element name="DatiAnagrafici" type="{c://inetub/wwwroot/webservice/Service.asmx}AnagraficaUtente" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "ricercaTesseraUtenteResult",
    "datiAnagrafici"
})
@XmlRootElement(name = "RicercaTesseraUtenteResponse")
public class RicercaTesseraUtenteResponse {

    @XmlElement(name = "RicercaTesseraUtenteResult", required = true)
    protected ReturnMSG ricercaTesseraUtenteResult;
    @XmlElement(name = "DatiAnagrafici")
    protected AnagraficaUtente datiAnagrafici;

    /**
     * Gets the value of the ricercaTesseraUtenteResult property.
     * 
     * @return
     *     possible object is
     *     {@link ReturnMSG }
     *     
     */
    public ReturnMSG getRicercaTesseraUtenteResult() {
        return ricercaTesseraUtenteResult;
    }

    /**
     * Sets the value of the ricercaTesseraUtenteResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReturnMSG }
     *     
     */
    public void setRicercaTesseraUtenteResult(ReturnMSG value) {
        this.ricercaTesseraUtenteResult = value;
    }

    /**
     * Gets the value of the datiAnagrafici property.
     * 
     * @return
     *     possible object is
     *     {@link AnagraficaUtente }
     *     
     */
    public AnagraficaUtente getDatiAnagrafici() {
        return datiAnagrafici;
    }

    /**
     * Sets the value of the datiAnagrafici property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnagraficaUtente }
     *     
     */
    public void setDatiAnagrafici(AnagraficaUtente value) {
        this.datiAnagrafici = value;
    }

}
