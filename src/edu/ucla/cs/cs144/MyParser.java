/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {

public static class Item {
    String itemID;
    String itemName;
    String curr;
    String buyPrice;
    String first_bid;
    String num_bid;
    String latitude;
    String longitude;
    String location;
    String country;
    String start;
    String end;
    String seller;
    String desc;

    public Item (String i_itemID, String i_itemName, String i_curr, String i_buyPrice, String i_first_bid, String i_num_bid, String i_latitude, String i_longitude, String i_location, String i_country, String i_start, String i_end, String i_seller, String i_desc) {
        this.itemID = i_itemID;
        this.itemName = i_itemName;
        this.curr = i_curr;
        this.buyPrice = i_buyPrice;
        this.first_bid = i_first_bid;
        this.num_bid = i_num_bid;
        this.latitude = i_latitude;
        this.longitude = i_longitude;
        this.location = i_location;
        this.country = i_country;
        this.start = i_start;
        this.end = i_end;
        this.seller = i_seller;
        this.desc = i_desc;
    }
}

public static class Seller {
    String sellerID;
    String seller_rating;

    public Seller (String s_sellerID, String s_rating) {
        this.sellerID = s_sellerID;
        this.seller_rating = s_rating;
    }
}

public static class User {
    String userID;
    String rating;
    String location;
    String country;

    public User (String u_userID, String u_rating, String u_location, String u_country) {
        this.userID = u_userID;
        this.rating = u_rating;
        this.location = u_location;
        this.country = u_country;
    }
}

public static class Bid {
    String userID;
    String itemID;
    String amount;
    String bidtime;

    public Bid (String b_userID, String b_itemID, String b_amount, String b_bidtime) {
        this.userID = b_userID;
        this.itemID = b_itemID;
        this.amount = b_amount;
        this.bidtime = b_bidtime;
    }
}

public static class Category {
    String itemID;
    String category;

    public Category (String c_itemID, String c_category) {
        this.itemID = c_itemID;
        this.category = c_category;
    }
}

    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }

    //Function for changing the timestamp to SQL format
    static String convertToTimestamp(String time) throws Exception {
        SimpleDateFormat old = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        Date old_parse = (Date) old.parse(time);

        SimpleDateFormat new_form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new_form.format(old_parse);
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
                
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        try {
            Element root = doc.getDocumentElement();

            //open files that are written to
            PrintWriter item_csv = new PrintWriter(new FileWriter("item.csv", true));
            PrintWriter seller_csv = new PrintWriter(new FileWriter("seller.csv", true));
            PrintWriter user_csv = new PrintWriter(new FileWriter("user.csv", true));
            PrintWriter bid_csv = new PrintWriter(new FileWriter("bid.csv", true));            
            PrintWriter cate_csv = new PrintWriter(new FileWriter("cate.csv", true));

            //get all the items in the document
            Element[] all_items = getElementsByTagNameNR(root, "Item");

            //declare vectors of classes
            Vector <Item> itemVec = new Vector<Item>();
            Vector <Seller> sellerVec = new Vector<Seller>();
            Vector <User> userVec = new Vector<User>();
            Vector <Bid> bidVec = new Vector<Bid>();
            Vector <Category> cateVec = new Vector<Category>();

            //Parse out item
            for (int i=0; i<all_items.length; i++) {
                String item_id = all_items[i].getAttribute("ItemID");
                String name = getElementTextByTagNameNR(all_items[i], "Name");
                String currently = strip(getElementTextByTagNameNR(all_items[i], "Currently"));
                String buyP = strip(getElementTextByTagNameNR(all_items[i], "Buy_Price"));
                String f_bid = strip(getElementTextByTagNameNR(all_items[i], "First_Bid"));
                String n_bid = getElementTextByTagNameNR(all_items[i], "Number_of_Bids");
                
                //Location and country information
                Element loca = getElementByTagNameNR(all_items[i], "Location");
                String lat = loca.getAttribute("Latitude");
                String lon = loca.getAttribute("Longitude");
                String item_loc = getElementText(loca);
                String item_country = getElementTextByTagNameNR(all_items[i], "Country");

                //Item start and end time
                String item_start = convertToTimestamp(getElementTextByTagNameNR(all_items[i], "Started"));
                String item_end = convertToTimestamp(getElementTextByTagNameNR(all_items[i], "Ends"));

                //Item seller info
                Element sell = getElementByTagNameNR(all_items[i], "Seller");
                String seller_id = sell.getAttribute("UserID");
                String seller_rate = sell.getAttribute("Rating");

                //Item description
                String item_desc = getElementTextByTagNameNR(all_items[i], "Description");
                if (item_desc.length() > 4000)
                    item_desc = item_desc.substring(0, 4000);

                //Add new item info to item vector
                itemVec.addElement(new Item(item_id, name, currently, buyP, f_bid, n_bid, lat, lon, item_loc, item_country, item_start, item_end, seller_id, item_desc));

                //Add new seller info the seller vector
                sellerVec.addElement(new Seller(seller_id, seller_rate));

                //Parse out bidder and user info
                Element bid_root = getElementByTagNameNR(all_items[i], "Bids");
                Element[] all_bids = getElementsByTagNameNR(bid_root, "Bid");
                for (int j=0; j<all_bids.length; j++) {
                    Element bidder = getElementByTagNameNR(all_bids[j], "Bidder");
                    String user_id = bidder.getAttribute("UserID");
                    String bid_rating = bidder.getAttribute("Rating");
                    String bid_loca = getElementTextByTagNameNR(bidder, "Location");
                    String bid_country = getElementTextByTagNameNR(bidder, "Country");

                    String bid_time = convertToTimestamp(getElementTextByTagNameNR(all_bids[j], "Time"));
                    String bid_amount = strip(getElementTextByTagNameNR(all_bids[j], "Amount"));

                    //Add new bidder and user info to corresponding vectors
                    bidVec.addElement(new Bid(user_id, item_id, bid_amount, bid_time));
                    userVec.addElement(new User(user_id, bid_rating, bid_loca, bid_country));
                }

                //Parse out category info 
                Element[] all_cates = getElementsByTagNameNR(all_items[i], "Category");
                for (int j=0; j<all_cates.length; j++) {
                    String cat = getElementText(all_cates[j]);

                    //Add new category info to category vector
                    cateVec.addElement(new Category(item_id, cat));
                }
            }

String itemID;
    String itemName;
    String curr;
    String buyPrice;
    String first_bid;
    String num_bid;
    String latitude;
    String longitude;
    String location;
    String country;
    String start;
    String end;
    String seller;
    String desc;
            //Write to item csv
            for (Item it : itemVec) {
                item_csv.append(it.itemID + " || " + it.itemName + " || " + it.curr + " || " + it.buyPrice + " || " + it.first_bid + 
                " || " + it.num_bid + " || " + it.latitude + " || " + it.longitude + " || " + it.location + " || " + it.country + 
                " || " + it.start + " || " + it.end + " || " + it.seller + " || " + it.desc + "\n");
            }

            //Write to seller csv
            for (Seller sel : sellerVec) {
                seller_csv.append(sel.sellerID + " || " + sel.seller_rating + "\n");
            }

            //Write to user csv
            for (User use : userVec) {
                user_csv.append(use.userID + " || " + use.rating + " || " + use.location + " || " + use.country + "\n");
            }

            //Write to bid csv
            for (Bid bids : bidVec) {
                bid_csv.append(bids.userID + " || " + bids.itemID + " || " + bids.amount + " || " + bids.bidtime + "\n");
            }

            //Write to category csv 
            for (Category categories : cateVec) {
                cate_csv.append(categories.itemID + " || " + categories.category + "\n");
            }

            //Close all the files
            item_csv.close();
            seller_csv.close();
            user_csv.close();
            bid_csv.close();
            cate_csv.close();
        }
        catch (Exception e)
        {
            System.err.println(e.toString());
        }
        
        /**************************************************************/
        
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
