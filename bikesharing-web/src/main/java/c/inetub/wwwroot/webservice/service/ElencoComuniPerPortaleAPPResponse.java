
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
 *         &lt;element name="ElencoComuniPerPortaleAPPResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "elencoComuniPerPortaleAPPResult"
})
@XmlRootElement(name = "ElencoComuniPerPortaleAPPResponse")
public class ElencoComuniPerPortaleAPPResponse {

    @XmlElement(name = "ElencoComuniPerPortaleAPPResult")
    protected String elencoComuniPerPortaleAPPResult;

    /**
     * Gets the value of the elencoComuniPerPortaleAPPResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElencoComuniPerPortaleAPPResult() {
        return elencoComuniPerPortaleAPPResult;
    }

    /**
     * Sets the value of the elencoComuniPerPortaleAPPResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElencoComuniPerPortaleAPPResult(String value) {
        this.elencoComuniPerPortaleAPPResult = value;
    }

}
