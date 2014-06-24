
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
 *         &lt;element name="ElencoStazioniPerComuneResult" type="{c://inetub/wwwroot/webservice/Service.asmx}ArrayOfString" minOccurs="0"/>
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
    "elencoStazioniPerComuneResult"
})
@XmlRootElement(name = "ElencoStazioniPerComuneResponse")
public class ElencoStazioniPerComuneResponse {

    @XmlElement(name = "ElencoStazioniPerComuneResult")
    protected ArrayOfString elencoStazioniPerComuneResult;

    /**
     * Gets the value of the elencoStazioniPerComuneResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getElencoStazioniPerComuneResult() {
        return elencoStazioniPerComuneResult;
    }

    /**
     * Sets the value of the elencoStazioniPerComuneResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setElencoStazioniPerComuneResult(ArrayOfString value) {
        this.elencoStazioniPerComuneResult = value;
    }

}
