package ru.slobodchikov.xmltocsv.xmltocsv.sevice;

import java.io.*;

import org.slf4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Value;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;

import org.supercsv.prefs.CsvPreference;
import ru.slobodchikov.xmltocsv.xmltocsv.structclasses.Metal;
import ru.slobodchikov.xmltocsv.xmltocsv.structclasses.Metals;

public class XmlToCsv {
    public void setMetals(Metals metals) {
        this.metals = metals;
    }

    @Value("${metals.filter.minmt}")
    int minMt;
    Metals metals;

    public void xmlReader(InputStream inputStream) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Metals.class);
        Unmarshaller um = context.createUnmarshaller();
        this.metals = (Metals) um.unmarshal(inputStream);
    }

    public void filter() {
        for (int indexi = 0; indexi < metals.getMetals().size(); indexi++) {
            if (metals.getMetals().get(indexi).getMelting_temperature() < minMt) {
                metals.getMetals().remove(indexi);
                indexi--;
            }
        }
    }

    public void metalSort() {
        for (int indexi = metals.getMetals().size() - 1; indexi >= 1; indexi--) {
            for (int indexj = 0; indexj < indexi; indexj++) {
                if (metals.getMetals().get(indexj).getDensity() > metals.getMetals().get(indexj + 1).getDensity()) {
                    Metal temp;
                    temp = metals.getMetals().get(indexj).copy();
                    metals.getMetals().set(indexj, metals.getMetals().get(indexj + 1).copy());
                    metals.getMetals().set(indexj + 1, temp.copy());
                }
            }
        }
    }

    public void writeToCsvFile(OutputStream outputStream, Logger logger) {
        try {
            StringWriter stringWriter = new StringWriter();
            ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(stringWriter, CsvPreference.TAB_PREFERENCE);
            String[] header = new String[]{"Название", "Пс", "Температура_плавления", "Температура_кипения", "Теплоемкость", "Плотность", "Атомная_масса"};
            csvBeanWriter.writeHeader(header);
            header = new String[]{"name", "ps", "melting_temperature", "boiling_temperature", "heat_capacity", "density", "atomic_mass"};
            for (Metal metal : metals.getMetals()) {
                csvBeanWriter.write(metal, header);
            }
            csvBeanWriter.close();
            byte[] bytes = stringWriter.toString().getBytes();
            outputStream.write(bytes);
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
    }
}
