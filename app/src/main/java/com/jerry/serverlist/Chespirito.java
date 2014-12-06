package com.jerry.serverlist;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;
/**
 * Created by jerry on 4/12/14.
 */

@IBMDataObjectSpecialization("Chespirito")
public class Chespirito extends IBMDataObject{
    public static final String CLASS_NAME = "Chespirito";
    private static final String PERSONAJE = "personaje";
    private static final String ACTOR = "actor";

    /**
     * Setters and Getters
     * @return
     */
    public String getPersonaje() {
        //return personaje;
        return (String) getObject(PERSONAJE);
    }

    public void setPersonaje(String personaje) {
        //this.personaje = personaje;
        setObject(PERSONAJE, (personaje != null) ? personaje : "");
    }

    public String getActor() {
        //return actor;
        return (String) getObject(ACTOR);
    }

    public void setActor(String actor) {
        //this.actor = actor;
        setObject(ACTOR, (actor != null) ? actor : "");
    }

    /**
     * Constructors
     * @param personaje
     * @param actor
     */
    public Chespirito() {    }

    public Chespirito(String personaje, String actor) {
        setObject(PERSONAJE, (personaje != null) ? personaje : "");
        setObject(ACTOR, (actor != null) ? actor : "");
    }

    @Override
    public String toString() {
        String fullRecord = "";
        fullRecord = getPersonaje() + getActor();
        return fullRecord;
        //return "Chespirito{" + "personaje='" + personaje + '\'' + ", actor='" + actor + '\'' + '}';
    }
}
