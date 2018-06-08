package priv.wangao.ElectionAlgorithm;

import priv.wangao.ElectionAlgorithm.server.Node;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World! Ring Election Algorithm." );
        Node.getInstance().start();
    }
}
