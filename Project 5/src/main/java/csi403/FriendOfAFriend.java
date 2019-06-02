package csi403;


// Import required java libraries
import java.io.*;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;


// Extend HttpServlet class
public class FriendOfAFriend extends HttpServlet {

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

        // Set response content type to be JSON
        response.setContentType("application/json");
        // Send back the response JSON message
        PrintWriter out = response.getWriter();

        // Create JsonReader object
        StringReader strReader = new StringReader(jsonStr);
        JsonReader reader = Json.createReader(strReader);

        // Get the singular JSON object (name:value pair) in this message.
        JsonObject obj = reader.readObject();
        // From the object get the array named "inList"
        JsonArray inArray = obj.getJsonArray("inList");

      /*
            This loop reads each inputted friendship and creates a Friendship object(edge) for each
       */
        ArrayList<Friendship> friendships = new ArrayList<>();

        for (int i = 0; i < inArray.size(); i++) {

            JsonArray friends = inArray.getJsonObject(i).getJsonArray("friends");

            String tempA = friends.get(0).toString();
            String tempB = friends.get(1).toString();

            String[] friendship = {tempA, tempB};

            Friendship f = new Friendship(friendship);

            friendships.add(f);
        }

      /*
      // Print out all friendships
      for (int i = 0; i < friendships.size(); i++) {
          out.println(friendships.get(i).toString());
      }
      */

      /*
            Traverses through the friendships, creating a Person object for each new person
       */
        ArrayList<Person> people = new ArrayList<>();

        for (Friendship search : friendships) {
            Person person1 = new Person(search.getFriends()[0]);
            Person person2 = new Person(search.getFriends()[1]);
            Person person;

            person1.getFriends().add(person2);
            person2.getFriends().add(person1);

            if ((person = contains(people, person1)) == null) {
                people.add(person1);
            } else {
                person.getFriends().add(person2);
            }

            if ((person = contains(people, person2)) == null) {
                people.add(person2);
            } else {
                person.getFriends().add(person1);
            }
        }

      /*
      // Print out all of the people
      out.println("\nList of People: \n");
      for (int i = 0; i < people.size(); i++) {
          out.println(people.get(i).getName());
      }
      */

      /*
      // Prints out every person's friend's List
      out.println("Friends Lists: \n");
      for (int i = 0; i < people.size(); i++) {

          out.print("\n" + people.get(i).getName() + "'s Friends List: \n");

          for (int j = 0; j < people.get(i).getFriends().size(); j++) {

              out.print(" " + people.get(i).getFriends().get(j).getName());

          }

      }
      */

      /*
            For each person, it goes through their friends and then their friend's friends. Stores the FOAFs in the
            outList as pairs. Checks to make sure there are no duplicate pairs, reversed.
       */
        ArrayList<ArrayList<String>> outList = new ArrayList<>();

        for (int i = 0; i < people.size(); i++) {

            for (int j = 0; j < people.get(i).getFriends().size(); j++) {

                for (int k = 0; k < people.get(i).getFriends().get(j).getFriends().size(); k++) {

                    if (!(people.get(i).equals(people.get(i).getFriends().get(j).getFriends().get(k)))) {

                        ArrayList<String> foaf = new ArrayList<>();
                        foaf.add(people.get(i).getFriends().get(j).getFriends().get(k).getName());
                        foaf.add(people.get(i).getName());

                        ArrayList<String> foafFlipped = new ArrayList<>();
                        foafFlipped.add(people.get(i).getName());
                        foafFlipped.add(people.get(i).getFriends().get(j).getFriends().get(k).getName());

                        if (!(outList.contains(foaf) || outList.contains(foafFlipped))) {

                            ArrayList<String> pair = new ArrayList<>();
                            pair.add(people.get(i).getName());
                            pair.add(people.get(i).getFriends().get(j).getFriends().get(k).getName());

                            outList.add(pair);
                        }

                    }
                }
            }
        }

      /*
            FINAL OUTPUT TO SCREEN
       */
        // Prints the friends of friends
        out.print("{\"outList\" : [");
        for (int i = 0; i < outList.size(); i++) {
            out.print("[");
            for (int j = 0; j < outList.get(i).size(); j++) {
                if (j == outList.get(i).size() - 1){
                    out.print(outList.get(i).get(j));
                } else {
                    out.print(outList.get(i).get(j) + ", ");
                }
            }
            if (i == outList.size() - 1) {
                out.print("]");
            } else {
                out.print("], ");
            }
        }
        out.print("]}");
    }

    /**
     * This method is used to check if the supplied Person is in the supplied ArrayList.
     *
     * @param friendsList
     * @param person
     * @return
     */
    public Person contains(ArrayList<Person> friendsList, Person person) {
        for (Person search : friendsList) {
            if (search.equals(person)) {
                return search;
            }
        }
        return null;
    }

    // Standard Servlet method
    public void destroy() {
        // Do any required tear-down here, likely nothing.
    }
}