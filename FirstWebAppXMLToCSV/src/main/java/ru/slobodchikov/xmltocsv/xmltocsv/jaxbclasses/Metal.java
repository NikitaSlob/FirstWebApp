package ru.slobodchikov.xmltocsv.xmltocsv.jaxbclasses;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Класс для храненя поля
 */
@XmlRootElement(name = "Металл")
@XmlType(propOrder = {"id", "name", "ps", "melting_temperature", "boiling_temperature", "heat_capacity", "density", "atomic_mass"})
public class Metal {
    private long id;
    private String name;
    private String ps;
    private int melting_temperature;
    private int boiling_temperature;
    private int heat_capacity;
    private int density;
    private int atomic_mass;

    public Metal() {

    }

    public int getAtomic_mass() {
        return atomic_mass;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPs() {
        return ps;
    }

    public int getBoiling_temperature() {
        return boiling_temperature;
    }

    public int getMelting_temperature() {
        return melting_temperature;
    }

    public int getHeat_capacity() {
        return heat_capacity;
    }

    public int getDensity() {
        return density;
    }

    @XmlAttribute
    public void setId(long id) {
        this.id = id;
    }

    @XmlElement(name = "Название")
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "Пс")
    public void setPs(String ps) {
        this.ps = ps;
    }

    @XmlElement(name = "Температура_кипения")
    public void setBoiling_temperature(int boiling_temperature) {
        this.boiling_temperature = boiling_temperature;
    }

    @XmlElement(name = "Температура_плавления")
    public void setMelting_temperature(int melting_temperature) {
        this.melting_temperature = melting_temperature;
    }

    @XmlElement(name = "Теплоемкость")
    public void setHeat_capacity(int heat_capacity) {
        this.heat_capacity = heat_capacity;
    }

    @XmlElement(name = "Плотность")
    public void setDensity(int density) {
        this.density = density;
    }

    @XmlElement(name = "Атомная_масса")
    public void setAtomic_mass(int atomic_mass) {
        this.atomic_mass = atomic_mass;
    }

    public Metal copy() {
        Metal metal = new Metal();
        metal.setAtomic_mass(getAtomic_mass());
        metal.setDensity(getDensity());
        metal.setHeat_capacity(getHeat_capacity());
        metal.setBoiling_temperature(getBoiling_temperature());
        metal.setMelting_temperature(getMelting_temperature());
        metal.setPs(getPs());
        metal.setName(getName());
        return metal;
    }
}

