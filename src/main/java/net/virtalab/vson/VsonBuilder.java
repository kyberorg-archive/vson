package net.virtalab.vson;

/**
 * Static class that allows customize options of Vson objects
 */
public class VsonBuilder {
    private boolean stopOnFirstError = true;

    //error reporting
    public VsonBuilder showAllErrors(){
        this.stopOnFirstError = false;
        return this;
    }

    public VsonBuilder stopOnFirstError(){
        this.stopOnFirstError = true;
        return this;
    }

    //triggers build
    public Vson create(){
        Vson vson = new Vson();
        vson.stopOnFirstError = this.stopOnFirstError;
        return vson;
    }

}
