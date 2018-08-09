package trainingSetFromTwitter;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class CollectionOfDataBaseFromTwitter {

    public static void main(String[] args) throws Exception {
        Connection conn = getConnection();
        Statement st = conn.createStatement();
        ResultSet resultSet = st.executeQuery("SELECT * FROM new_schema.collectionoftwitter");


        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory
                .newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element rootElement = document.createElement("add");
        document.appendChild(rootElement);
        while (resultSet.next()) {

            Element doc = document.createElement("doc");
            rootElement.appendChild(doc);

            Element fieldOfText = document.createElement("field");
            doc.appendChild(fieldOfText);

            Attr name = document.createAttribute("name");
            name.setTextContent("textClass");
            fieldOfText.setAttributeNode(name);

            String str=resultSet.getString(4).replaceAll("@[\\w:_-]*","").replaceAll("#[а-яА-Яa-zA-Z0-9:_-]*","").replaceAll("http[./а-яА-Яa-zA-Z0-9/\\:_-]*"," ").replaceAll(" amp;*"," ");
            str = SmileAndDeleteForLoadOnPlatform.prepareText(str);

            fieldOfText.setTextContent(str.replaceAll("amp;|amp;;|htt…"," ").replaceAll("[^a-zA-Zа-яА-Я]"," "));//текст

            Element fieldOfClass = document.createElement("field");
            doc.appendChild(fieldOfClass);

            Attr name2 = document.createAttribute("name");
            name2.setTextContent("class");
            fieldOfClass.setAttributeNode(name2);


            fieldOfClass.setTextContent(resultSet.getString(5)); //класс
        }
        TransformerFactory transformerFactory = TransformerFactory
                .newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File("./resources/CollectionofTwitter.xml"));
        transformer.transform(source, result);
    }


    public static Connection getConnection() throws Exception{
        try {
            String driver = "com.mysql.jdbc.Driver";
            String url= "jdbc:mysql://localhost:3306/mysql";
            String username="root";
            String password="12345";
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, username, password);

            return conn;
        } catch (Exception e){System.out.print(e);}
        return null;
    }


}
