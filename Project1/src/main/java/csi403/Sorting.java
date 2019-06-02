package csi403;


// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;


// Extend HttpServlet class
public class Sorting extends HttpServlet {

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
        doService(request, response);
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
    // Parses messages e.g. {"inList" : [5, 32, 3, 12]}
    // Returns the list reversed.
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

        // Convert Json array to int array
        int[] arr = new int[inArray.size()];
        for(int k = 0; k < inArray.size(); k++) {
            arr[k] = inArray.getInt(k);
        }

        // Insertion Sort
        long start = System.nanoTime();
        int temp;
        for (int i = 1; i < arr.length; i++) {
            for(int j = i ; j > 0 ; j--){
                if(arr[j] < arr[j-1]){
                    temp = arr[j];
                    arr[j] = arr[j-1];
                    arr[j-1] = temp;
                }
            }
        }

        long finish = System.nanoTime();
        long timeElapsedMilli = (finish - start) / 1000;

        // Reverse the data in the list
        JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();
        for (int i = 0; i < inArray.size(); i++) {
            outArrayBuilder.add(arr[i]);
        }

        // Set response content type to be JSON
        response.setContentType("application/json");
        // Send back the response JSON message
        PrintWriter out = response.getWriter();
        out.println("{ \"outList\" : " + outArrayBuilder.build().toString());
        out.println("\"Algorithm : \" Insertion Sort");
        out.println(" \"TimeMS\" : " + timeElapsedMilli + " }");
    }


    // Standard Servlet method
    public void destroy()
    {
        // Do any required tear-down here, likely nothing.
    }
}