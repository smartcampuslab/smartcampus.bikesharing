
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
 *         &lt;element name="UsernamePasswordDaBadgeResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "usernamePasswordDaBadgeResult"
})
@XmlRootElement(name = "UsernamePasswordDaBadgeResponse")
public class UsernamePasswordDaBadgeResponse {

    @XmlElement(name = "UsernamePasswordDaBadgeResult")
    protected String usernamePasswordDaBadgeResult;

    /**
     * Gets the value of the usernamePasswordDaBadgeResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsernamePasswordDaBadgeResult() {
        return usernamePasswordDaBadgeResult;
    }

    /**
     * Sets the value of the usernamePasswordDaBadgeResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsernamePasswordDaBadgeResult(String value) {
        this.usernamePasswordDaBadgeResult = value;
    }

}
