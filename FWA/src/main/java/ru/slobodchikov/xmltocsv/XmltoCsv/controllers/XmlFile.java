package ru.slobodchikov.xmltocsv.XmltoCsv.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.slobodchikov.xmltocsv.XmltoCsv.XmlToCsv;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

@Controller
public class XmlFile {
    @GetMapping(value="/upload")
    public String Upload() {
        return "upload";
    }

    @PostMapping(value="/upload",produces = "text/plain;charset=UTF-8")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (!file.isEmpty()) {
            try {

                byte[] bytes = file.getBytes();
                //File metals = new File("src/main/resources/templates");
                String workingDir = System.getProperty("user.dir");
                File workDir = new File(workingDir + "\\tempres");
                workDir.mkdir();
                File metals = new File(workingDir + "\\tempres\\metals.xml");
                metals.createNewFile();
                FileOutputStream stream = new FileOutputStream(metals);
                stream.write(bytes);
                stream.close();
                return "redirect:download";
            } catch (Exception e) {
                String s = "Вам не удалось загрузить файл => " + e.getMessage();
                System.out.println(s);
                return "redirect:"+ referer;
            }
        } else {
            String s = "Вам не удалось загрузить файл, потому что он пустой.";
            System.out.println(s);
            return "redirect:"+ referer;
        }
    }

}
