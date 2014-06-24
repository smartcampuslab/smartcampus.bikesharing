
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
 *         &lt;element name="ElencoStazioniResult" type="{c://inetub/wwwroot/webservice/Service.asmx}ArrayOfString" minOccurs="0"/>
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
    "elencoStazioniResult"
})
@XmlRootElement(name = "ElencoStazioniResponse")
public class ElencoStazioniResponse {

    @XmlElement(name = "ElencoStazioniResult")
    protected ArrayOfString elencoStazioniResult;

    /**
     * Gets the value of the elencoStazioniResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getElencoStazioniResult() {
        return elencoStazioniResult;
    }

    /**
     * Sets the value of the elencoStazioniResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setElencoStazioniResult(ArrayOfString value) {
        this.elencoStazioniResult = value;
    }

}
