package control;

import model.Edge;
import model.Graph;
import model.List;
import model.Vertex;

import java.util.Objects;

/**
 * Created by Jean-Pierre on 12.01.2017.
 */
public class MainController {

    //Attribute

    //Referenzen
    private Graph allUsers;

    public MainController(){
        allUsers = new Graph();
        createSomeUsers();
        if(testIfConnected()) System.out.println("The graph is connected");
        else System.out.println("The graph is not connected");
    }

    /**
     * Fügt Personen dem sozialen Netzwerk hinzu.
     */
    private void createSomeUsers(){
        insertUser("Ulf");
        insertUser("Silent Bob");
        insertUser("Dörte");
        insertUser("Ralle");
        befriend("Silent Bob", "Ralle");
        befriend("Dörte", "Ralle");
        //befriend("Ulf","Dörte");
    }

    /**
     * Fügt einen Nutzer hinzu, falls dieser noch nicht existiert.
     * @param name
     * @return true, falls ein neuer Nutzer hinzugefügt wurde, sonst false.
     */
    public boolean insertUser(String name){
        //TODO 05: Nutzer dem sozialen Netzwerk hinzufügen. ✓
        if(name != null && allUsers.getVertex(name) == null) {
            allUsers.addVertex(new Vertex(name));
            return true;
        }
        return false;
    }

    /**
     * Löscht einen Nutzer, falls dieser existiert. Alle Verbindungen zu anderen Nutzern werden ebenfalls gelöscht.
     * @param name
     * @return true, falls ein Nutzer gelöscht wurde, sonst false.
     */
    public boolean deleteUser(String name){
        //TODO 07: Nutzer aus dem sozialen Netzwerk entfernen. ✓
        if(name != null && allUsers.getVertex(name) != null) {
            allUsers.removeVertex(allUsers.getVertex(name));
            return true;
        }
        return false;
    }

    /**
     * Falls Nutzer vorhanden sind, so werden ihre Namen in einem String-Array gespeichert und zurückgegeben. Ansonsten wird null zurückgegeben.
     * @return
     */
    public String[] getAllUsers(){
        //TODO 06: String-Array mit allen Nutzernamen erstellen. ✓
        List<Vertex> vertices = allUsers.getVertices();
        int i = 0;
        vertices.toFirst();
        while(vertices.hasAccess()) {
            i++;
            vertices.next();
        }
        String[] result = new String[i];
        vertices.toFirst();
        i = 0;
        while(vertices.hasAccess()) {
            result[i] = vertices.getContent().getID();
            i++;
            vertices.next();
        }
        return result;
    }

    /**
     * Falls der Nutzer vorhanden ist und Freunde hat, so werden deren Namen in einem String-Array gespeichert und zurückgegeben. Ansonsten wird null zurückgegeben.
     * @param name
     * @return
     */
    public String[] getAllFriendsFromUser(String name){
        //TODO 09: Freundesliste eines Nutzers als String-Array erstellen. ✓
        List<Edge> edges = allUsers.getEdges();
        int i = 0;
        if(allUsers.getVertex(name) != null) {
            edges.toFirst();
            while(edges.hasAccess()) {
                if(edges.getContent().getVertices()[0].getID().equals(name) || edges.getContent().getVertices()[1].getID().equals(name)) {
                    i++;
                }
                edges.next();
            }
            String[] s = new String[i];
            edges.toFirst();
            i = 0;
            while(edges.hasAccess()) {
                if(edges.getContent().getVertices()[0].getID().equals(name)) {
                    s[i] = edges.getContent().getVertices()[1].getID();
                    i++;
                }
                if(edges.getContent().getVertices()[1].getID().equals(name)) {
                    s[i] = edges.getContent().getVertices()[0].getID();
                    i++;
                }
                edges.next();
            }
            return s;
        }
        return new String[0];
    }

    /**
     * Bestimmt den Zentralitätsgrad einer Person im sozialen Netzwerk, falls sie vorhanden ist. Sonst wird -1.0 zurückgegeben.
     * Der Zentralitätsgrad ist der Quotient aus der Anzahl der Freunde der Person und der um die Person selbst verminderten Anzahl an Nutzern im Netzwerk.
     * Gibt also den Prozentwert an Personen im sozialen Netzwerk an, mit der die Person befreundet ist.
     * @param name
     * @return
     */
    public double centralityDegreeOfUser(String name){
        //TODO 10: Prozentsatz der vorhandenen Freundschaften eines Nutzers von allen möglichen Freundschaften des Nutzers. ✓
        if(allUsers.getVertex(name) != null) {
            int friendsNumber = getAllFriendsFromUser(name).length;
            int i = getAllUsers().length - 1;
            return (double) friendsNumber / i;
        }
        return -1.0;
    }

    /**
     * Zwei Nutzer des Netzwerkes gehen eine Freundschaft neu ein, falls sie sich im Netzwerk befinden und noch keine Freunde sind.
     * @param name01
     * @param name02
     * @return true, falls eine neue Freundeschaft entstanden ist, ansonsten false.
     */
    public boolean befriend(String name01, String name02){
        //TODO 08: Freundschaften schließen. ✓ (Geht beträchtlich kürzer)
        List<Vertex> vertices = allUsers.getVertices();
        List<Edge> edges = allUsers.getEdges();
        vertices.toFirst();
        edges.toFirst();
        while(edges.hasAccess()
                && ((!Objects.equals(edges.getContent().getVertices()[0].getID(), name01) && !Objects.equals(edges.getContent().getVertices()[1].getID(), name02))
                || (!Objects.equals(edges.getContent().getVertices()[1].getID(), name01) && !Objects.equals(edges.getContent().getVertices()[0].getID(), name02)))) {
            edges.next();
        }
        if(!edges.hasAccess()) {
            allUsers.addEdge(new Edge(allUsers.getVertex(name01), allUsers.getVertex(name02), 0));
            return true;
        }
        return false;
    }

    /**
     * Zwei Nutzer beenden ihre Freundschaft, falls sie sich im Netzwerk befinden und sie befreundet sind.
     * @param name01
     * @param name02
     * @return true, falls ihre Freundschaft beendet wurde, ansonsten false.
     */
    public boolean unfriend(String name01, String name02) {
        //TODO 11: Freundschaften beenden. ✓ (Geht beträchtlich kürzer)
        if (allUsers.getVertex(name01) != null && allUsers.getVertex(name02) != null) {
            List<Edge> edges = allUsers.getEdges();
            edges.toFirst();
            while(edges.hasAccess()) {
                if(edges.getContent().getVertices()[0].getID().equals(name01) && edges.getContent().getVertices()[1].getID().equals(name02)
                        || edges.getContent().getVertices()[1].getID().equals(name01) && edges.getContent().getVertices()[0].getID().equals(name02)) {
                    edges.remove();
                    return true;
                }
                edges.next();
            }
        }
        return false;
    }

    /**
     * Bestimmt die Dichte des sozialen Netzwerks und gibt diese zurück.
     * Die Dichte ist der Quotient aus der Anzahl aller vorhandenen Freundschaftsbeziehungen und der Anzahl der maximal möglichen Freundschaftsbeziehungen.
     * @return
     */
    public double dense(){
        //TODO 12: Dichte berechnen. ✓
        List<Edge> edges = allUsers.getEdges();
        double friendships = 0;
        double possibleFriendships;
        int userCount = getAllUsers().length;
        edges.toFirst();
        while(edges.hasAccess()) {
            friendships++;
            edges.next();
        }
        possibleFriendships = (userCount - 1) * userCount;
                                                            //idek
        return (friendships / possibleFriendships) * 2;
    }

    /**
     * Gibt die möglichen Verbindungen zwischen zwei Personen im sozialen Netzwerk als String-Array zurück,
     * falls die Personen vorhanden sind und sie über eine oder mehrere Ecken miteinander verbunden sind.
     * @param name01
     * @param name02
     * @return
     */
    public String[] getLinksBetween(String name01, String name02){
        Vertex user01 = allUsers.getVertex(name01);
        Vertex user02 = allUsers.getVertex(name02);
        if(user01 != null && user02 != null){
            //TODO 13: Schreibe einen Algorithmus, der mindestens eine Verbindung von einem Nutzer über Zwischennutzer zu einem anderem Nutzer bestimmt. Happy Kopfzerbrechen!

            return null;
        }
        return null;
    }

    private void getLinksBetweenRe(){

    }

    /**
     * Gibt zurück, ob es keinen Knoten ohne Nachbarn gibt.
     * @return true, falls ohne einsame Knoten, sonst false.
     */
    public boolean someoneIsLonely(){
        //TODO 14: Schreibe einen Algorithmus, der explizit den von uns benutzten Aufbau der Datenstruktur Graph und ihre angebotenen Methoden so ausnutzt, dass schnell (!) iterativ geprüft werden kann, ob der Graph allUsers keine einsamen Knoten hat. Dies lässt sich mit einer einzigen Schleife prüfen. ✓
        var vertices = allUsers.getVertices();
        vertices.toFirst();
        boolean hasEdge = false;
        while(vertices.hasAccess()) {
            var tmp = vertices.getContent();
            vertices.toFirst();
            while(vertices.hasAccess()) {
                if(allUsers.getEdge(tmp,vertices.getContent()) != null)
                    hasEdge = true;
                vertices.next();
            }
            if(!hasEdge) return true;
            hasEdge = false;
            vertices.toFirst();
            while(vertices.getContent() != tmp) {
                vertices.next();
            }
            vertices.next();
        }
        return false;
    }

    /**
     * Gibt zurück, ob vom ersten Knoten in der Liste aller Knoten ausgehend alle anderen Knoten erreicht also markiert werden können.
     * Nach der Prüfung werden noch vor der Rückgabe alle Knoten demarkiert.
     * @return true, falls alle Knoten vom ersten ausgehend markiert wurden, sonst false.
     */
    public boolean testIfConnected(){
        //TODO 15: Schreibe einen Algorithmus, der ausgehend vom ersten Knoten in der Liste aller Knoten versucht, alle anderen Knoten über Kanten zu erreichen und zu markieren.
        var vertices = allUsers.getVertices();
        vertices.toFirst();
        var firstNode = vertices.getContent();
        firstNode.setMark(true);
        vertices.next();
        while(vertices.hasAccess()) {
            if(allUsers.getEdge(firstNode,vertices.getContent()) != null) {
                vertices.getContent().setMark(true);
                var tmp = vertices.getContent();
                testIfConnected(vertices.getContent());
                vertices.toFirst();
                while(tmp != vertices.getContent()) {
                    vertices.next();
                }
                vertices.next();
            }
            vertices.next();
        }
        vertices.toFirst();
        while(vertices.hasAccess() && vertices.getContent().isMarked()) {
            vertices.next();
        }
        allUsers.setAllVertexMarks(false);
        return !vertices.hasAccess();
    }

    public void testIfConnected(Vertex vertex) {
        var vertices = allUsers.getVertices();
        vertices.toFirst();
        while(vertices.hasAccess()) {
            if(allUsers.getEdge(vertex,vertices.getContent()) != null) {
                if(!vertices.getContent().isMarked()) {
                    vertices.getContent().setMark(true);
                    var tmp = vertices.getContent();
                    testIfConnected(vertices.getContent());
                    vertices.toFirst();
                    while(tmp != vertices.getContent()) {
                        vertices.next();
                    }
                    vertices.next();
                }
            }
            vertices.next();
        }
    }
    
    /**
     * Gibt einen String-Array zu allen Knoten zurück, die von einem Knoten ausgehend erreichbar sind, falls die Person vorhanden ist.
     * Im Anschluss werden vor der Rückgabe alle Knoten demarkiert.
     * @return Alle erreichbaren Knoten als String-Array, sonst null.
     */
    public String[] transitiveFriendships(String name){
        //TODO 16: Schreibe einen Algorithmus, der ausgehend von einem Knoten alle erreichbaren ausgibt.
        return null;
    }
    
    
    /**
     * Gibt eine kürzeste Verbindung zwischen zwei Personen des Sozialen Netzwerkes als String-Array zurück,
     * falls die Personen vorhanden sind und sie über eine oder mehrere Ecken miteinander verbunden sind.
     * @param name01
     * @param name02
     * @return Verbindung als String-Array oder null, falls es keine Verbindung gibt.
    */
    public String[] shortestPath(String name01, String name02){
        Vertex user01 = allUsers.getVertex(name01);
        Vertex user02 = allUsers.getVertex(name02);
        if(user01 != null && user02 != null){
            //TODO 17: Schreibe einen Algorithmus, der die kürzeste Verbindung zwischen den Nutzern name01 und name02 als String-Array zurückgibt. Versuche dabei einen möglichst effizienten Algorithmus zu schreiben.
        }
        return null;
    }

}
