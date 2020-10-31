package com.emrahyurtlu.xmltoxsd.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.wiztools.xsdgen.ParseException;
import org.wiztools.xsdgen.XsdGen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
public class ServiceController {
    @PostMapping(value = "/converter", produces = {MediaType.APPLICATION_XML_VALUE})
    public String service(@RequestParam("file") String file) throws IOException, ParseException {
        File xmlFile = new File("src/main/resources/static/input.xml");
        writeParamIntoXMLFile(xmlFile, file);

        XsdGen generator = new XsdGen();
        generator.parse(xmlFile);

        File xsdFile = new File("src/main/resources/static/output.xsd");
        generator.write(new FileOutputStream(xsdFile));

        String fileContent = FileUtils.readFileToString(xsdFile, "utf-8");

        // İşlem bittikten sonra
        // olası bir suistimali önlemek adına
        // xml dosyalarının içeriği silindi.
        writeParamIntoXMLFile(xmlFile, "<xml>reset file</xml>");
        writeParamIntoXMLFile(xsdFile, "<xml>reset file</xml>");

        return fileContent;
    }

    private void writeParamIntoXMLFile(File file, String xml) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(String.valueOf(xml));
        fileWriter.flush();
        fileWriter.close();
    }
}