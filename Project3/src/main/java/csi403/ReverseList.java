package csi403;


// Import required java libraries
import netscape.javascript.JSObject;

import java.io.*;
import java.util.*;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;

// Extend HttpServlet class
public class ReverseList extends HttpServlet {

  // Standard servlet method 
  public void init() throws ServletException
  {
      // Do any required initialization here - likely none
  }

  // Standard servlet method - we will handle a POST operation
  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      PrintWriter out = response.getWriter();
        try {
          doService(request, response);
         } catch (ClassCastException ex) {
          out.println("{ 'message' : 'Please read line 78-85 in ReverseList.java!'}");
      } catch (Exception e) {
            out.println("{ 'message' : 'Malformed JSON, Fix and try again!");
        }
  }

  // Standard servlet method - we will not respond to GET
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Set response content type and return an error message
      response.setContentType("application/json");
      PrintWriter out = response.getWriter();
      out.println("{ 'message' : 'Use POST!'}");
  }

  // Our main worker method
  private void doService(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Get received JSON data from HTTP request
      BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
      String jsonStr = "";
      if(br != null){
          jsonStr = br.readLine();
      }
      
      // Create JsonReader object
      StringReader strReader = new StringReader(jsonStr);
      JsonReader reader = Json.createReader(strReader);

      // Get the singular JSON object (name:value pair) in this message.    
      JsonObject obj = reader.readObject();
      // From the object get the array named "inList"
      JsonArray inArray = obj.getJsonArray("inList");

      String[] myArray = new String[inArray.size()];
      ArrayList<String> myList = new ArrayList<>();

      int[] keyArray = new int[inArray.size()];
      Hashtable<Integer, ArrayList<String>> myHashTable = new Hashtable<>();
      MyHashing hashObj = new MyHashing();


      // I KNOW MY BUG IS IN THIS FOR LOOP BUT I CANNOT FIGURE OUT HOW TO GET JSON TO STRING.
      // I UNDERSTAND WHY IT IS THROWING THE EXCEPTION BUT CANNOT FIGURE OUT A SOLUTION WITH WHAT I KNOW AND HAVE FOUND.
      // I HAVE  TRIED EVERY COMBINATION OF POSSIBLE FUNCTIONS I KNOW OF.
      // FEEDBACK EXTREMELY APPRECIATED IF YOU SEE A POSSIBLE SOLUTION! THANKS
      // Fills myArray with Json Objects converted to Strings
     for (int i = 0; i < inArray.size(); i++) {
         myArray[i] = inArray.getJsonObject(i).getString("\"");
      }


      // Hashes each String using hashAlgo(String str) and inserts it into myHashTable
      // If key is already in myHashTable then add String to next index in ArrayList
      // Hashes each String using hashAlgo(String str) and inserts the key into keyArray
      for (int i = 0; i < myArray.length; i++) {
          int h = hashObj.hashAlgo(myArray[i]);
          keyArray[i] = h;
          if (!(myHashTable.containsKey(h))) {
              myHashTable.put(h, new ArrayList<String>());
          } else {
                myHashTable.get(h).add(myArray[i]);
          }
      }

      response.setContentType("application/json");
      PrintWriter out = response.getWriter();
      out.println("{ \"outList\" : [");
      while(!myHashTable.isEmpty()) {
          int i = 0;

          if (myHashTable.get(keyArray[i]).size() > 1) {
              out.print(" [ ");
              for (int k = 0; k < myHashTable.get(keyArray[i]).size(); k++) {
                  out.print(myHashTable.get(keyArray[i]).get(k) + ", ");
              }
              out.print(" ] ");
          }
          out.print(" ]");
          i++;
      }
      
      //out.println("{ \"outList\" : " + outArrayBuilder.build().toString() + "}");
      //out.println("{ \"outList\" : " + "\"hello world\"" + "}");
  }

  // Standard Servlet method
  public void destroy()
  {
      // Do any required tear-down here, likely nothing.
  }
}

