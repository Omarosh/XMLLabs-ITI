
package finalxml;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author EGC
 */
public class FXMLDocumentController implements Initializable {
    @FXML
    private TextField name;
    @FXML
    private Text nametxt;
    @FXML
    private Text phonetxt;
    @FXML
    private Text addresstxt;
    @FXML
    private Text emailtxt;
    @FXML
    private TextField id;
    @FXML
    private TextField searchname;
    @FXML
    private TextField phone;
    @FXML
    private TextField address;
    @FXML
    private TextField email;
    @FXML
    private Button prev;
    @FXML
    private Button next;
    @FXML
    private Button insert;
    @FXML
    private Button update;
    @FXML
    private Button save;
    @FXML
    private Button delete;
    @FXML
    private Button saveinsert;
    @FXML
    private Button reload;
    @FXML
    private Button updatemode;

    public static File FILENAME;
    Stage mystage;
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db;

    {
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    Document doc;
    NodeList nodeList;

    int counterLimit;
    int counter;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FILENAME = openNewFile(mystage);
        counter = -1;
        disableTFs();
        readNparseXML();
    }

    public File openNewFile(Stage stage) {
        // showDialog(stage);
        String latestDirectory = System.getProperty("user.dir");
        String latestFile = "my.xml";
        FileChooser fp = new FileChooser();
        fp.setInitialDirectory(new File(latestDirectory));
        fp.setInitialFileName(latestFile);
        fp.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All files", "*.*"),
                new FileChooser.ExtensionFilter("TXT", "*.txt"));
        fp.setTitle("Choose a file to open");
        java.io.File file = fp.showOpenDialog(stage);
        latestDirectory = file.getParentFile().getAbsolutePath();
        latestFile = file.getAbsolutePath();
        return file;
    }

    public void disableTFs() {
        id.setEditable(false);
        name.setEditable(false);
        email.setEditable(false);
        phone.setEditable(false);
        address.setEditable(false);
    }

    public void enableTFs() {
        id.setEditable(true);
        name.setEditable(true);
        email.setEditable(true);
        phone.setEditable(true);
        address.setEditable(true);
    }

    public void readNparseXML() {
        try {
            doc = db.parse(FILENAME);
        } catch (IOException e) {
        } catch (SAXException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        doc.getDocumentElement().normalize();
        System.out.println("Document Root element is: " + doc.getDocumentElement().getNodeName());
        nodeList = doc.getElementsByTagName("employee");
        counterLimit = nodeList.getLength();
        onnext(new ActionEvent());
        onprev(new ActionEvent());
    }

    public void showNode() {

        // show all info in the node by the counter
        System.out.println("Currently viewing node: " + counter);
        Node node = nodeList.item(counter);
        Element eElement = (Element) node;

        id.setText(eElement.getElementsByTagName("Id").item(0).getTextContent());
        name.setText(eElement.getElementsByTagName("Name").item(0).getTextContent());
        address.setText(eElement.getElementsByTagName("Address").item(0).getTextContent());
        email.setText(eElement.getElementsByTagName("Email").item(0).getTextContent());
        phone.setText(eElement.getElementsByTagName("Phone").item(0).getTextContent());
    }

    public void ondelete(ActionEvent event) {

        if (!id.getText().trim().isEmpty()) {
            Node node = nodeList.item(counter);
            node.getParentNode().removeChild(node);
            emptyAllFIelds();
            counterLimit--;
            onprev(event);
        } else {
            System.out.println("Finish the Save/Update First Or get an id to delete ");
        }

    }

    public void oninsertmode(ActionEvent event) {
        System.out.println("InsertMode: ");
        emptyAllFIelds();
        enableTFs();
        toggleInsert();

    }

    public void toggleInsert() {
        saveinsert.setVisible(!saveinsert.visibleProperty().get());
        insert.setVisible(!insert.visibleProperty().get());
    }

    public boolean oninsert(ActionEvent event) {
        System.out.println("Inserting: ");

        toggleInsert();
        try {
            Element root = doc.getDocumentElement(); // employees
            Element employee = doc.createElement("employee"); // new element
            root.appendChild(employee);

            Element Id = doc.createElement("Id");
            Element Name = doc.createElement("Name");
            Element Email = doc.createElement("Email");
            Element Address = doc.createElement("Address");
            Element Phone = doc.createElement("Phone");

            Name.appendChild(doc.createTextNode(name.getText().trim()));
            Address.appendChild(doc.createTextNode(address.getText().trim()));
            Id.appendChild(doc.createTextNode(id.getText().trim()));

            Email.appendChild(doc.createTextNode(email.getText().trim()));
            Phone.appendChild(doc.createTextNode(phone.getText().trim()));

            employee.appendChild((Node) Id);
            employee.appendChild((Node) Name);

            employee.appendChild((Node) Address);
            employee.appendChild((Node) Email);
            employee.appendChild((Node) Phone);
            counterLimit++;
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("error happened !");
            return false;
        }
    }

    public void onnext(ActionEvent event) {
        if (counter < counterLimit - 1) {
            counter++;
            showNode();
        }
    }

    public void onprev(ActionEvent event) {
        if (counter > 0) {
            counter--;
            showNode();
        }

    }

    public void onsave(ActionEvent event) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(FILENAME);
            transformer.transform(domSource, streamResult);
            counter = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onreload(ActionEvent event) {
        emptyAllFIelds();
        readNparseXML();
    }

    public void onupdatemode(ActionEvent event) {
        enableTFs();
        toggleUpdate();
    }

    public void toggleUpdate() {
        update.setVisible(!update.visibleProperty().get());
        updatemode.setVisible(!updatemode.visibleProperty().get());
    }

    public void onupdate(ActionEvent event) {
        toggleUpdate();
        disableTFs();
        if (!id.getText().trim().isEmpty()) {

            Node item = nodeList.item(counter);
            Element eElement = (Element) item;

            eElement.getElementsByTagName("Name").item(0).setTextContent(name.getText().trim());

            eElement.getElementsByTagName("Id").item(0).setTextContent(id.getText().trim());

            eElement.getElementsByTagName("Address").item(0).setTextContent(address.getText().trim());

            eElement.getElementsByTagName("Email").item(0).setTextContent(email.getText().trim());
            eElement.getElementsByTagName("Phone").item(0).setTextContent(phone.getText().trim());

            // counter = 0;

        }

        else {
            System.out.println("Please Finish Save First or get an id to update ...");
        }

    }

    public void emptyAllFIelds() {

        name.clear();
        email.clear();
        phone.clear();
        address.clear();
        id.clear();
    }

    @FXML
    void onsearch(ActionEvent event) {
        System.out.println("Search");
        String nameInserted = searchname.getText();

        for (int i = 0; i < counterLimit; ++i) {
            // found = false;
            Node node = nodeList.item(i);
            Element element = (Element) node;
            String Name = element.getElementsByTagName("Name").item(0).getTextContent();

            if (nameInserted.equals(Name)) {
                counter = i;
                showNode();
                break;

            }
        }

    }
}
