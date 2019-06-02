package csi403;

// Import required java libraries
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;


// Extend HttpServlet class
public class PQueue extends HttpServlet {

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
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            doService(request, response);
        } catch (Exception e) {
            out.println("{ 'message' : 'Error!'}");
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

        PriorityQueue<Job> pQueue = new PriorityQueue<Job>();

        for (int i = 0; i < inArray.size(); i++) {

            obj = inArray.getJsonObject(i);

            if (obj.getString("cmd").equals("enqueue")) {
                pQueue.add(new Job(obj.getString("name"), obj.getInt("pri")));
            }
            else if (obj.getString("cmd").equals("dequeue")) {
                pQueue.remove();
            }
        }

        // Set response content type to be JSON
        response.setContentType("application/json");
        // Send back the response JSON message
        PrintWriter out = response.getWriter();

        String[] strArr = new String[inArray.size()];
        boolean flag = false;
        if (pQueue.isEmpty()) {
            out.print("{ 'message' : 'Empty Queue'}");
        } else {
            int k = 0;
            while(!pQueue.isEmpty()) {
                strArr[k] = pQueue.remove().getName();
                flag = true;
                k++;
            }
        }

        if (flag) {
            out.print("{ \"outList\" : [");
            for (int i = 0; i < strArr.length; i++) {
                if (strArr[i] != null) {
                    out.print(" \"" + strArr[i] + "\", ");
                }
                else {
                    return;
                }
            }
        }
        out.print("]}");
    }

    // Standard Servlet method
    public void destroy()
    {
        // Do any required tear-down here, likely nothing.
    }
}