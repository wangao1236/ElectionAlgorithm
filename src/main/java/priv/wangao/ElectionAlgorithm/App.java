package priv.wangao.ElectionAlgorithm;

import priv.wangao.ElectionAlgorithm.entity.Node;
import priv.wangao.ElectionAlgorithm.util.XML;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World! Ring Election Algorithm." );
        if (args != null && args.length == 1) {
        	XML.setConfigPath(args[0]);
        }
        Node.getInstance().start();
    }
}
