package redes.routing

import redes.routing.core.Firmware;
import groovy.lang.Lazy


class Router {

    @Lazy
    public static final properties = importProperties()
	public static final String propertiesPath = "/application.properties"
    
    static void main(String[] args) {
        Firmware.run(args)
    }

    /*
     *  Private method due to import project properties
     *
     *  return properties object
     */
    def static importProperties(){
        Properties properties = new Properties();
        new Object() {}
            .getClass()
            .getResource( Router.propertiesPath )
            .withInputStream {
                properties.load(it)
            }
        
        properties
    }
    
}