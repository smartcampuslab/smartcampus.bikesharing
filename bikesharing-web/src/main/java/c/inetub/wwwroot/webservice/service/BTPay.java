
package c.inetub.wwwroot.webservice.service;

import java.math.BigDecimal;
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
 *         &lt;element name="p2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="p3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="p4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="p5" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="p6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="p7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="p8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="p9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "p2",
    "p3",
    "p4",
    "p5",
    "p6",
    "p7",
    "p8",
    "p9"
})
@XmlRootElement(name = "BTPay")
public class BTPay {

    protected String p2;
    protected String p3;
    protected String p4;
    @XmlElement(required = true)
    protected BigDecimal p5;
    protected String p6;
    protected String p7;
    protected String p8;
    protected String p9;

    /**
     * Gets the value of the p2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getP2() {
        return p2;
    }

    /**
     * Sets the value of the p2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setP2(String value) {
        this.p2 = value;
    }

    /**
     * Gets the value of the p3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getP3() {
        return p3;
    }

    /**
     * Sets the value of the p3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setP3(String value) {
        this.p3 = value;
    }

    /**
     * Gets the value of the p4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getP4() {
        return p4;
    }

    /**
     * Sets the value of the p4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setP4(String value) {
        this.p4 = value;
    }

    /**
     * Gets the value of the p5 property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getP5() {
        return p5;
    }

    /**
     * Sets the value of the p5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setP5(BigDecimal value) {
        this.p5 = value;
    }

    /**
     * Gets the value of the p6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getP6() {
        return p6;
    }

    /**
     * Sets the value of the p6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setP6(String value) {
        this.p6 = value;
    }

    /**
     * Gets the value of the p7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getP7() {
        return p7;
    }

    /**
     * Sets the value of the p7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setP7(String value) {
        this.p7 = value;
    }

    /**
     * Gets the value of the p8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getP8() {
        return p8;
    }

    /**
     * Sets the value of the p8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setP8(String value) {
        this.p8 = value;
    }

    /**
     * Gets the value of the p9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getP9() {
        return p9;
    }

    /**
     * Sets the value of the p9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setP9(String value) {
        this.p9 = value;
    }

}
