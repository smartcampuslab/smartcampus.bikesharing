
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
 *         &lt;element name="ElencoStazioniSempliceResult" type="{c://inetub/wwwroot/webservice/Service.asmx}ArrayOfString" minOccurs="0"/>
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
    "elencoStazioniSempliceResult"
})
@XmlRootElement(name = "ElencoStazioniSempliceResponse")
public class ElencoStazioniSempliceResponse {

    @XmlElement(name = "ElencoStazioniSempliceResult")
    protected ArrayOfString elencoStazioniSempliceResult;

    /**
     * Gets the value of the elencoStazioniSempliceResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getElencoStazioniSempliceResult() {
        return elencoStazioniSempliceResult;
    }

    /**
     * Sets the value of the elencoStazioniSempliceResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setElencoStazioniSempliceResult(ArrayOfString value) {
        this.elencoStazioniSempliceResult = value;
    }

}