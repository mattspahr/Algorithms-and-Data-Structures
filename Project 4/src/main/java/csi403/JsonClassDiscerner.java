package csi403; 

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.List; 

public class JsonClassDiscerner {

    public JsonClassDiscerner() {
    }

    public String discern(String jsonStr) {
        ObjectMapper mapper = new ObjectMapper();
        // mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try { 
            Point point = mapper.readValue(jsonStr, Point.class);
            return "point";
        }
        catch (Exception e) {
            // e.printStackTrace(); 
        }
        return "<unknown>"; 
    }

    // test app 
    public static void main(String[] args) {
        String msg;
        JsonClassDiscerner discerner = new JsonClassDiscerner();
        System.out.println("************************************");
    }
}
