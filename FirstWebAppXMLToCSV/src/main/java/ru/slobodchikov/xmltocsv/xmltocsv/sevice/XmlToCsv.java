package ru.slobodchikov.xmltocsv.xmltocsv.sevice;

import java.io.*;
import java.sql.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

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
import ru.slobodchikov.xmltocsv.xmltocsv.jaxb.Metal;
import ru.slobodchikov.xmltocsv.xmltocsv.jaxb.Metals;

@Service
/**
 * Основной сервис преобразования
 */
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
     *
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
        metals.setMetals(metals.getMetals().stream().filter(x -> x.getMelting_temperature() > minMt).collect(Collectors.toList()));
    }

    /**
     * Сортировка по возростанию density
     */
    private void metalSort() {
        metals.getMetals().sort(Comparator.comparingInt(Metal::getDensity));
    }

    /**
     * Запись в csv файл
     *
     * @param outputStream поток записи в файл
     */
    private void writeToCsvFile(OutputStream outputStream) throws IOException {
        try (final StringWriter stringWriter = new StringWriter()) {
            try (final ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(stringWriter, CsvPreference.TAB_PREFERENCE)) {
                final String[] header = new String[]{"Название", "Пс", "Температура_плавления", "Температура_кипения", "Теплоемкость", "Плотность", "Атомная_масса"};
                csvBeanWriter.writeHeader(header);
                final String[] value = new String[]{"name", "ps", "melting_temperature", "boiling_temperature", "heat_capacity", "density", "atomic_mass"};
                for (Metal metal : metals.getMetals()) {
                    csvBeanWriter.write(metal, value);
                }
                outputStream.write(stringWriter.toString().getBytes());
            }
        }
    }

    /**
     * Публичный метод для чтения, измения и записи полей
     *
     * @param inputStream  поток чтения из файла
     * @param outputStream поток для записи из файла
     * @throws JAXBException ошибка чтения из метода xmlReader
     */
    public void transform(InputStream inputStream, OutputStream outputStream) throws JAXBException, IOException {
        xmlReader(inputStream);
        metalSort();
        filter();
        writeToCsvFile(outputStream);
    }
}
