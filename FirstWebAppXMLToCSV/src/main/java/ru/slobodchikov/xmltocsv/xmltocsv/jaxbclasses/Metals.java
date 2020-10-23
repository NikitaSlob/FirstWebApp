package ru.slobodchikov.xmltocsv.xmltocsv.jaxbclasses;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Класс для хранения массива полей
 */
@XmlRootElement(name = "Металлы")
public class Metals {
    private static List<Metal> metals;

    @XmlElement(name = "Металл")
    public List<Metal> getMetals() {
        return metals;
    }

    public void setMetals(List<Metal> metals) {
        this.metals = metals;
    }
}
