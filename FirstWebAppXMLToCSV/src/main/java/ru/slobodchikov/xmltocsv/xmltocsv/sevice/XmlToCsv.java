package ru.slobodchikov.xmltocsv.xmltocsv.sevice;

import java.io.*;

import org.slf4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;

import org.supercsv.prefs.CsvPreference;
import ru.slobodchikov.xmltocsv.xmltocsv.jaxbclasses.Metal;
import ru.slobodchikov.xmltocsv.xmltocsv.jaxbclasses.Metals;

/**
 * Основной класс преобразования
 */
@Service
public class XmlToCsv {
    public void setMetals(Metals metals) {
        this.metals = metals;
    }

    private static final Logger logger = LoggerFactory.getLogger(XmlToCsv.class);
    /**
     * Минимальныя значение поля для фильтрации записей
     */
    @Value("${metals.filter.minmt}")
    private int minMt;
    /**
     * Класс для сериализация
     */
    private Metals metals;

    /**
     * Получения класса из файла xml
     * @param inputStream поток из файла
     * @throws JAXBException ошибка получения класса из файла
     */
    private void xmlReader(InputStream inputStream) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Metals.class);
        Unmarshaller um = context.createUnmarshaller();
        this.metals = (Metals) um.unmarshal(inputStream);
    }

    /**
     * удаление записей у которых melting_temperature меньше minMt
     */
    private void filter() {
        for (int indexi = 0; indexi < metals.getMetals().size(); indexi++) {
            if (metals.getMetals().get(indexi).getMelting_temperature() < minMt) {
                metals.getMetals().remove(indexi);
                indexi--;
            }
        }
    }

    /**
     * Сортировка по возростанию density
     */
    private void metalSort() {
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

    /**
     * Запись в csv файл
     * @param outputStream поток записи в файл
     */
    private void writeToCsvFile(OutputStream outputStream) {
        try {
            final StringWriter stringWriter = new StringWriter();
            final ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(stringWriter, CsvPreference.TAB_PREFERENCE);
            String[] header = new String[]{"Название", "Пс", "Температура_плавления", "Температура_кипения", "Теплоемкость", "Плотность", "Атомная_масса"};
            csvBeanWriter.writeHeader(header);
            header = new String[]{"name", "ps", "melting_temperature", "boiling_temperature", "heat_capacity", "density", "atomic_mass"};
            for (Metal metal : metals.getMetals()) {
                csvBeanWriter.write(metal, header);
            }
            csvBeanWriter.close();
            final byte[] bytes = stringWriter.toString().getBytes();
            outputStream.write(bytes);
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
    }

    /**
     * Публичный метод для чтения, измения и записи полей
     * @param inputStream поток чтения из файла
     * @param outputStream поток для записи из файла
     * @throws JAXBException ошибка чтения из метода xmlRearer
     */
    public void transform(InputStream inputStream, OutputStream outputStream) throws JAXBException {
        xmlReader(inputStream);
        metalSort();
        filter();
        writeToCsvFile(outputStream);
    }
}
