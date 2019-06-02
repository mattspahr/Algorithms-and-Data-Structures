package csi403;


// Import required java libraries
import java.io.*;
import java.awt.geom.Line2D;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;

// Extend HttpServlet class
public class PointCheck extends HttpServlet {

    // Standard servlet method
    public void init() throws ServletException {
        // Do any required initialization here - likely none
    }

    // Standard servlet method - we will handle a POST operation
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            doService(request, response);
        } catch (Exception e) {
            out.println("{ 'message' : 'Error'}");
        }
    }

    // Standard Servlet method
    public void destroy() {
        // Do any required tear - down here, likely nothing.
    }

    // Standard servlet method - we will not respond to GET
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type and return an error message
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        // We can always create JSON by hand just concating a string.
        out.println("{ 'message' : 'Use POST!'}");
    }

    /**
     *  containtsPoint(Point test, Point[] polygon) method implements the Ray Casting Algorithm.
     *
     * @param test       Point that is in the polygon or not.
     * @param polygon    Verticies of the Polygon
     * @return           returns true if the point is within the polygon or on an edge.
     *                   returns false if the point is outside the polygon.
     */
    public boolean containsPoint(Point test, Point[] polygon) {
        boolean result = false;

        int i,j;
        for (i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
            if ((polygon[i].getY() > test.getY()) != (polygon[j].getY() > test.getY()) && (test.getX() < (polygon[j].getX() - polygon[i].getX()) * (test.getY() - polygon[i].getY()) / (polygon[j].getY()-polygon[i].getY()) + polygon[i].getX())) {
                result = !result;
            }
        }
        return result;
    }


    /**
     *  onEdge(Point test, Point[] polygon) method checks to see if a point is on the line or not.
     *
     * @param test       Point that is on the edge of the polygon or not.
     * @param polygon    Verticies of the Polygon
     * @return           returns true if the point is on the edge.
     *                   returns false if the point is only inside.
     */
    public boolean onEdge(Point test, Point[] polygon) {
        for (int i = 0; i < polygon.length; i++) {
            if (i != polygon.length - 1) {
                Point point1 = polygon[i];
                Point point2 = polygon[i + 1];
                Line2D.Double segment = new Line2D.Double((double)point1.getX(), (double)point1.getY(),
                        (double)point2.getX(), (double)point2.getY());
                if (segment.ptLineDist(test.getX(), test.getY()) == 0) {
                    return true;
                }
            } else {
                Point point1 = polygon[i];
                Point point2 = polygon[0];
                Line2D.Double segment = new Line2D.Double((double)point1.getX(), (double)point1.getY(),
                        (double)point2.getX(), (double)point2.getY());
                if (segment.ptLineDist(test.getX(), test.getY()) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    // Our main worker method
    private void doService(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

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

        // Set response content type to be JSON
        response.setContentType("application/json");
        // Send back the name of the class as a JSON message
        PrintWriter out = response.getWriter();

        Point[] polygon = new Point[inArray.size()];

        for (int i = 0; i < inArray.size(); i++) {

            obj = inArray.getJsonObject(i);

            polygon[i] = new Point();

            polygon[i].setX(obj.getInt("x"));
            polygon[i].setY(obj.getInt("y"));

        }

        int xMin = polygon[0].getX(),
                yMin = polygon[0].getY(),
                xMax = polygon[0].getX(),
                yMax = polygon[0].getY();

        // Creates a bounding rectangle of x and y values for the polygon
        // Will be used for quick check if the point is contained in
        for (int i = 0; i < polygon.length; i++) {
            // Calculates the minimum x-Value
            if (polygon[i].getX() < xMin) {
                xMin = polygon[i].getX();
                if (xMin > 18 || xMin < 0) {
                    out.println("{ 'message' : 'Point out of 19x19 grid'}");
                    return;
                }

            }

            // Calculates the maximum x-Value
            if (polygon[i].getX() > xMax) {
                xMax = polygon[i].getX();
                if (xMax > 18 || xMax < 0) {
                    out.println("{ 'message' : 'Point out of 19x19 grid'}");
                    return;
                }

            }

            // Calculates the minimum y-Value
            if (polygon[i].getY() < yMin) {
                yMin = polygon[i].getY();
                if (yMin > 18 || yMin < 0) {
                    out.println("{ 'message' : 'Point out of 19x19 grid'}");
                    return;
                }

            }

            // Calculates the maximum y-Value
            if (polygon[i].getY() > yMax) {
                yMax = polygon[i].getY();
                if (yMax > 18 || yMax < 0) {
                    out.println("{ 'message' : 'Point out of 19x19 grid'}");
                    return;
                }
            }
        }

        Point[] inside = new Point[(xMax - xMin) * (yMax - yMin)];

        int count = 0;
        int iterate = 0;
        for (int x = xMin; x < xMax; x++) {
            for (int y = yMin; y < yMax; y++) {
                Point temp = new Point(x, y);
                if (containsPoint(temp, polygon)) {
                    inside[iterate] = temp;
                    iterate++;
                }
            }
        }

        for (int i = 0; i < inside.length; i++) {
            if (inside[i] == null) {
            } else {
                //System.out.println(inside[i].getX() + ", " + inside[i].getY());
                Point temp = inside[i];
                if (!onEdge(temp, polygon)) {
                    count++;

                }
            }
        }

        out.println("{ \"count\" : " + count + "}");
    }
}