package org.springframework.samples.mvc.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xml.sax.SAXException;

import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntityTemplate;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceRecordset;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceSection;
import com.incadencecorp.coalesce.framework.datamodel.ECoalesceFieldDataTypes;

@Controller
@RequestMapping("/data/*")
public class ResourceController {

    @RequestMapping(value="download/{json}", method=RequestMethod.GET)
    public void editor(@PathVariable("json") String json, HttpServletResponse response) throws IOException, SAXException {
        
        
        System.out.println(json);

        CoalesceEntity entity = new CoalesceEntity();
        entity.initialize();
        
        JSONObject obj = new JSONObject(json);
        String templateName = obj.getString("templateName");
        
        entity.setName(templateName);
        
        JSONArray jsonSections = obj.getJSONArray("sections");
        
        for (int i = 0; i < jsonSections.length(); i++){
            
            JSONObject jsonSection = jsonSections.getJSONObject(i);
            String SectionName = jsonSection.getString("sectionName");
            
           CoalesceSection section = entity.createSection(SectionName);
           
           JSONArray jsonRecordSets = jsonSection.getJSONArray("recordsets");
           
           for (int j = 0; j < jsonRecordSets.length(); j++){
               
               JSONObject jsonRecordSet = jsonRecordSets.getJSONObject(j);
               String recordsetName = jsonRecordSet.getString("recordsetName");
               CoalesceRecordset recordset = section.createRecordset(recordsetName);
               
               JSONArray jsonFields = jsonRecordSet.getJSONArray("fields");
               
               for (int k = 0; k < jsonFields.length(); k++){
                   JSONObject jsonField = jsonFields.getJSONObject(k);
                   String fieldName = jsonField.getString("fieldName");
                   String fieldType = jsonField.getString("fieldType");
                   ECoalesceFieldDataTypes type = ECoalesceFieldDataTypes.getTypeForCoalesceType(fieldType);
                   
                   recordset.createFieldDefinition(fieldName, type);
               }
               
           }
        }
        
        File file = new File(templateName+".xml");
        file.createNewFile();
        
        System.out.println(file.getAbsolutePath());
        
        FileUtils.writeStringToFile(file, CoalesceEntityTemplate.create(entity).toXml());
        
        InputStream inputStream = new FileInputStream(file);
 
        // MIME type of the file
        response.setContentType("application/octet-stream");
        // Response header
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + file.getName() + "\"");
        // Read from the file and write into the response
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        
        file.delete();
    }
}