package ru.slobodchikov.xmltocsv.XmltoCsv;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
public class XmlToCsv {
    private static List<Metal> metals;
    public void xmlReader(String  filename) {
        File xmlFile = new File(filename);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("Металл");
            metals = new ArrayList<>();
            List<Integer> id=new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                metals.add(getMetal(nodeList.item(i),id));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    private static Metal getMetal(Node node, List<Integer> id) throws Exception {
        Metal metal = new Metal();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            int idx=Integer.parseInt(element.getAttribute("id"));
            if(!id.contains(idx))
                id.add(idx);
            else
                throw new Exception();
            metal.setName(getTagValue("Название", element));
            metal.setPs(getTagValue("Пс", element));
            metal.setMelting_temperature(Integer.parseInt(getTagValue("Температура_плавления", element)));
            metal.setBoiling_temperature(Integer.parseInt(getTagValue("Температура_кипения", element)));
            metal.setHeat_capacity(Integer.parseInt(getTagValue("Теплоемкость", element)));
            metal.setDensity(Integer.parseInt(getTagValue("Плотность", element)));
            metal.setAtomicMass(Integer.parseInt(getTagValue("Атомная_масса", element)));
        }

        return metal;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
    public static void Filter()
    {
        for (int i = 0; i <metals.size() ; i++) {
            if(metals.get(i).getMelting_temperature()<800) {
                metals.remove(i);
                i--;
            }
        }
    }
    public static void MetalSort() {
        for (int i = metals.size() - 1; i >= 1; i--) {
            for (int j = 0; j < i; j++) {
                if (metals.get(j).getDensity() > metals.get(j + 1).getDensity()) {
                    Metal temp;
                    temp=metals.get(j).Copy();
                    metals.set(j,metals.get(j+1).Copy());
                    metals.set(j+1,temp.Copy());
                }
            }
        }
    }
    public File writeToCsvFile(String fileName){
        try {
            Filter();
            MetalSort();
            File file=new File(fileName);
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write("Название");
            writer.append(';');
            writer.write("Пс");
            writer.append(';');
            writer.write("Температура_плавления");
            writer.append(';');
            writer.write("Температура_кипения");
            writer.append(';');
            writer.write("Теплоемкость");
            writer.append(';');
            writer.write("Плотность");
            writer.append(';');
            writer.write("Атомная_масса");
            writer.append(System.lineSeparator());
            for (Metal metal : metals) {
                writer.write(metal.toCSVString());
                writer.append(System.lineSeparator());
            }
            writer.flush();
            writer.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static class Metal {
        private String name;
        private String ps;
        private int melting_temperature;
        private int boiling_temperature;
        private int heat_capacity;
        private int density;
        private int atomic_mass;
        public Metal() {

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

        public int getAtomicMass() {
            return atomic_mass;
        }
        public void setName(String name) {
            this.name=name;
        }

        public void setPs(String ps) {
            this.ps=ps;
        }

        public void setBoiling_temperature(int boiling_temperature) {
            this.boiling_temperature=boiling_temperature;
        }

        public void setMelting_temperature(int melting_temperature) {
            this.melting_temperature=melting_temperature;
        }

        public void setHeat_capacity(int heat_capacity) {
            this.heat_capacity=heat_capacity;
        }

        public void setDensity(int density) {
            this.density=density;
        }

        public void setAtomicMass(int atomic_mass) {
            this.atomic_mass=atomic_mass;
        }
        public Metal Copy(){
            Metal metal=new Metal();
            metal.setAtomicMass(getAtomicMass());
            metal.setDensity(getDensity());
            metal.setHeat_capacity(getHeat_capacity());
            metal.setBoiling_temperature(getBoiling_temperature());
            metal.setMelting_temperature(getMelting_temperature());
            metal.setPs(getPs());
            metal.setName(getName());
            return metal;
        }
        public String toCSVString(){
            return getName()+";"+getPs()+";"+ getMelting_temperature()+";"+getBoiling_temperature()+";"+getHeat_capacity()+";"+getDensity()+";"+getAtomicMass();
        }
    }
}
