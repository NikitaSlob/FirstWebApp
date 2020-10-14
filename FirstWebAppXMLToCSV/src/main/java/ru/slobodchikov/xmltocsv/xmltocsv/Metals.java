package ru.slobodchikov.xmltocsv.xmltocsv;

import javax.xml.bind.annotation.*;
import java.util.List;
@XmlRootElement(name="Металлы")
public class Metals {
    private static List<Metal> metals;
    @XmlElement(name="Металл")
    public List<Metal> getMetals() {
        return this.metals;
    }
    public void setMetals(List<Metal> metals) {
        this.metals = metals;
    }
}
