
package c.inetub.wwwroot.webservice.service;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReturnMSG.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ReturnMSG">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="RivenditoreNonValido"/>
 *     &lt;enumeration value="UtenteNonTrovato"/>
 *     &lt;enumeration value="UtenteGiaEsistente"/>
 *     &lt;enumeration value="ErroreTabellaUtente"/>
 *     &lt;enumeration value="ErroreInserimentoAnagraficaUtente"/>
 *     &lt;enumeration value="ErroreInserimentoUtente"/>
 *     &lt;enumeration value="InserimentoRiuscito"/>
 *     &lt;enumeration value="Campo_Obbligatorio"/>
 *     &lt;enumeration value="erroreCAP"/>
 *     &lt;enumeration value="erroreData"/>
 *     &lt;enumeration value="EtaInferiore14"/>
 *     &lt;enumeration value="Solo_F_M"/>
 *     &lt;enumeration value="CodiceFiscaleErrato"/>
 *     &lt;enumeration value="CampoEmailErrato"/>
 *     &lt;enumeration value="CampoCreditoErrato"/>
 *     &lt;enumeration value="TesseraNonTrovata"/>
 *     &lt;enumeration value="TesseraTrovata"/>
 *     &lt;enumeration value="ErroreNumeroTessera"/>
 *     &lt;enumeration value="ErroreTabellaTessere"/>
 *     &lt;enumeration value="NULL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ReturnMSG")
@XmlEnum
public enum ReturnMSG {

    @XmlEnumValue("RivenditoreNonValido")
    RIVENDITORE_NON_VALIDO("RivenditoreNonValido"),
    @XmlEnumValue("UtenteNonTrovato")
    UTENTE_NON_TROVATO("UtenteNonTrovato"),
    @XmlEnumValue("UtenteGiaEsistente")
    UTENTE_GIA_ESISTENTE("UtenteGiaEsistente"),
    @XmlEnumValue("ErroreTabellaUtente")
    ERRORE_TABELLA_UTENTE("ErroreTabellaUtente"),
    @XmlEnumValue("ErroreInserimentoAnagraficaUtente")
    ERRORE_INSERIMENTO_ANAGRAFICA_UTENTE("ErroreInserimentoAnagraficaUtente"),
    @XmlEnumValue("ErroreInserimentoUtente")
    ERRORE_INSERIMENTO_UTENTE("ErroreInserimentoUtente"),
    @XmlEnumValue("InserimentoRiuscito")
    INSERIMENTO_RIUSCITO("InserimentoRiuscito"),
    @XmlEnumValue("Campo_Obbligatorio")
    CAMPO_OBBLIGATORIO("Campo_Obbligatorio"),
    @XmlEnumValue("erroreCAP")
    ERRORE_CAP("erroreCAP"),
    @XmlEnumValue("erroreData")
    ERRORE_DATA("erroreData"),
    @XmlEnumValue("EtaInferiore14")
    ETA_INFERIORE_14("EtaInferiore14"),
    @XmlEnumValue("Solo_F_M")
    SOLO_F_M("Solo_F_M"),
    @XmlEnumValue("CodiceFiscaleErrato")
    CODICE_FISCALE_ERRATO("CodiceFiscaleErrato"),
    @XmlEnumValue("CampoEmailErrato")
    CAMPO_EMAIL_ERRATO("CampoEmailErrato"),
    @XmlEnumValue("CampoCreditoErrato")
    CAMPO_CREDITO_ERRATO("CampoCreditoErrato"),
    @XmlEnumValue("TesseraNonTrovata")
    TESSERA_NON_TROVATA("TesseraNonTrovata"),
    @XmlEnumValue("TesseraTrovata")
    TESSERA_TROVATA("TesseraTrovata"),
    @XmlEnumValue("ErroreNumeroTessera")
    ERRORE_NUMERO_TESSERA("ErroreNumeroTessera"),
    @XmlEnumValue("ErroreTabellaTessere")
    ERRORE_TABELLA_TESSERE("ErroreTabellaTessere"),
    NULL("NULL");
    private final String value;

    ReturnMSG(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ReturnMSG fromValue(String v) {
        for (ReturnMSG c: ReturnMSG.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
