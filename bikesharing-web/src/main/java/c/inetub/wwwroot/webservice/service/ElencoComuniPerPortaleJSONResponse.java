
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
 *         &lt;element name="ElencoComuniPerPortaleJSONResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "elencoComuniPerPortaleJSONResult"
})
@XmlRootElement(name = "ElencoComuniPerPortaleJSONResponse")
public class ElencoComuniPerPortaleJSONResponse {

    @XmlElement(name = "ElencoComuniPerPortaleJSONResult")
    protected String elencoComuniPerPortaleJSONResult;

    /**
     * Gets the value of the elencoComuniPerPortaleJSONResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElencoComuniPerPortaleJSONResult() {
        return elencoComuniPerPortaleJSONResult;
    }

    /**
     * Sets the value of the elencoComuniPerPortaleJSONResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElencoComuniPerPortaleJSONResult(String value) {
        this.elencoComuniPerPortaleJSONResult = value;
    }

}
