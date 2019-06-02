
package csi403;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Vector;
import java.util.List; 

public class JsonSerializer {

    public JsonSerializer() {
    }

    public String serialize(Object obj) {
        String str = null; 
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(Include.NON_NULL);
            str = mapper.writeValueAsString(obj);
        } catch (Exception e) {
            str = "<error>"; 
        }
        return str;
    }


    // test app 
    public static void main(String[] args) {
        JsonSerializer serializer = new JsonSerializer();

        System.out.println("************************************");

        Point point1 = new Point();
        Point point2 = new Point();
        Point point3 = new Point();
        Point point4 = new Point();
        point1.setX(2);
        point1.setY(1);
        point2.setX(2);
        point2.setY(4);
        point3.setX(8);
        point3.setY(4);
        point4.setX(11);
        point4.setY(1);
        System.out.println(serializer.serialize(point1));
        System.out.println(serializer.serialize(point2));
        System.out.println(serializer.serialize(point3));
        System.out.println(serializer.serialize(point4));
        System.out.println("************************************");
    }
    
}

