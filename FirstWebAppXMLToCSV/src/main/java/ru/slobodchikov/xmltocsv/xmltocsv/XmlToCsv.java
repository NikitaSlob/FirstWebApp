package ru.slobodchikov.xmltocsv.xmltocsv;
import java.io.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;

import org.supercsv.prefs.CsvPreference;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlToCsv {
    public void setMetals(Metals metals) {
        this.metals = metals;
    }
    Metals metals;
    public void xmlReader(InputStream inputStream) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Metals.class);
        Unmarshaller um=context.createUnmarshaller();
        Metals metals=(Metals)um.unmarshal(inputStream);
        this.metals=metals;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
    public void Filter()
    {
        int minMT=800;
        for (int i = 0; i <metals.getMetals().size() ; i++) {
            if(metals.getMetals().get(i).getMelting_temperature()<minMT) {
                metals.getMetals().remove(i);
                i--;
            }
        }
    }
    public void MetalSort() {
        for (int i = metals.getMetals().size() - 1; i >= 1; i--) {
            for (int j = 0; j < i; j++) {
                if (metals.getMetals().get(j).getDensity() > metals.getMetals().get(j + 1).getDensity()) {
                    Metal temp;
                    temp=metals.getMetals().get(j).Copy();
                    metals.getMetals().set(j,metals.getMetals().get(j+1).Copy());
                    metals.getMetals().set(j+1,temp.Copy());
                }
            }
        }
    }
    public void writeToCsvFile( OutputStream outputStream){
        try {
            StringWriter stringWriter=new StringWriter();
            ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(stringWriter, CsvPreference.TAB_PREFERENCE);
            String[] header = new String[]{"Название", "Пс","Температура_плавления","Температура_кипения","Теплоемкость","Плотность","Атомная_масса"};
            csvBeanWriter.writeHeader(header);
            header = new String[]{"name", "ps","melting_temperature","boiling_temperature","heat_capacity","density","atomic_mass"};
            for (Metal metal:metals.getMetals()) {
                csvBeanWriter.write(metal,header);
            }
            csvBeanWriter.close();
            byte[] bytes=stringWriter.toString().getBytes();
            outputStream.write(bytes);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
