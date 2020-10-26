package ru.slobodchikov.xmltocsv.xmltocsv.sevice;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.io.*;

class XmlToCsvTest {

    @Test()
    void transform() throws IOException {
        try (InputStream inputStream = new FileInputStream("src/main/test/testresources/metals.csv")) {
            File file = new File("metals.csv");
            try (final OutputStream outputStream = new FileOutputStream(file)) {
                XmlToCsv xmlToCsv = new XmlToCsv();
                xmlToCsv.transform(inputStream, outputStream);
                Assert.fail("expected JAXBException");
            } catch (JAXBException e) {

            }
        }
    }


    @Test
    void transform1() throws FileNotFoundException {
        try (InputStream inputStream = new FileInputStream("src/main/test/testresources/metals.xml")) {
            File file = new File("metals.csv");
            try (final OutputStream outputStream = new FileOutputStream(file)) {
                XmlToCsv xmlToCsv = new XmlToCsv();
                xmlToCsv.transform(inputStream, outputStream);
                outputStream.flush();
            } catch (JAXBException e) {
                Assert.fail("unexpected JAXBException");
            }
        } catch (IOException exception) {
            Assert.fail("unexpected JAXBException");
        }
    }
}