
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
 *         &lt;element name="ElencoStazioniSempliceJSONResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "elencoStazioniSempliceJSONResult"
})
@XmlRootElement(name = "ElencoStazioniSempliceJSONResponse")
public class ElencoStazioniSempliceJSONResponse {

    @XmlElement(name = "ElencoStazioniSempliceJSONResult")
    protected String elencoStazioniSempliceJSONResult;

    /**
     * Gets the value of the elencoStazioniSempliceJSONResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElencoStazioniSempliceJSONResult() {
        return elencoStazioniSempliceJSONResult;
    }

    /**
     * Sets the value of the elencoStazioniSempliceJSONResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElencoStazioniSempliceJSONResult(String value) {
        this.elencoStazioniSempliceJSONResult = value;
    }

}
