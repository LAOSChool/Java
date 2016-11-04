package com.itpro.sendmail;

import java.io.File;
import java.util.Properties;

public class Config {
	private Properties symbolmap;
    public Config(File file) {
        symbolmap = new Properties();
        try {
            //Populate the symbol map from the XML file
            symbolmap.loadFromXML( file.toURI().toURL().openStream() );
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    }
    //variable length arguments are packed into an array
    //which can be accessed and passed just like any array
    public String lookupSymbol(String symbol, String... variables) {
        //Retrieve the value of the associated key
        String message = symbolmap.getProperty(symbol);
        if(message == null)
            return "";
        //Interpolate parameters if necessary
        //and return the message
        return String.format(message, variables);
    }
    
    public String[] fetchArray(String symbol) {
    	  //get array split up by the semicolin
    	  String[] a =  symbolmap.getProperty(symbol).split(",");
    	  return a;
    	}
    
//    example-symbols.xml
//
//    <?xml version="1.0" encoding="UTF-8" standalone="no"?>
//    <!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
//    <properties>
//    <comment>example pairs</comment>
//    <entry key="MOTD">Hello, world!</entry>
//    <entry key="FAVOURITE_FRUIT">Mango</entry>
//    <entry key="THE_COW_SAYS">The cow says %s</entry>
//    </properties>
    
//    Example.java
//
//    import java.io.File;
//    public class Example {
//    public static void main(String[] args) throws Exception {
//        SymbolMap symbolmap = new SymbolMap(new File("example-symbols.xml"));
//        print("> " + symbolmap.lookupSymbol("MOTD"));
//        print("> " + symbolmap.lookupSymbol("NOT_FOUND"));
//        print("> " + symbolmap.lookupSymbol("THE_COW_SAYS", "mooooo"));
//    }
//    public static void print(String s) {
//        System.out.println(s);
//    }
//    }
}
