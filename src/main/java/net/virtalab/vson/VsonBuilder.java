package net.virtalab.vson;

/**
 * Static class that allows customize options of created Vson object
 */
public class VsonBuilder {
    /**
     * Error reporting property
     */
    private boolean stopOnFirstError = true;

    /**
     * Makes Parser go thru JSON all report all errors
     * @return builder instance
     */
    public VsonBuilder showAllErrors(){
        this.stopOnFirstError = false;
        return this;
    }

    /**
     * Makes Parser halt on first error without further iterating for rest of JSON
     * @return builder instance
     */
    public VsonBuilder stopOnFirstError(){
        this.stopOnFirstError = true;
        return this;
    }

    /**
     * Triggers Parser creation
     * @return Vson Parser instance
     */
    public Vson create(){
        Vson vson = new Vson();
        vson.stopOnFirstError = this.stopOnFirstError;
        return vson;
    }

}
