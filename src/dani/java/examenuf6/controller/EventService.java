/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dani.java.examenuf6.controller;

import dani.java.examenuf6.model.Event;
import java.util.List;

/**
 * Servei per cridar als mètodes d'administració de la base de dades
 * @author dani
 */
public class EventService implements DAO<Event> {
    
    private EventDAO eventDAO;

    public EventService(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    @Override
    public Event get(Integer n) {
        return eventDAO.get(n);
    }

    @Override
    public List<Event> getAll() {
        return eventDAO.getAll();
    }

    @Override
    public void insert(Event t) {
        eventDAO.insert(t);
    }

    @Override
    public void update(Event t) {
        eventDAO.update(t);
    }

    @Override
    public void delete(Event t) {
        eventDAO.delete(t);
    }
    
    public void closeService() {
        eventDAO.closeRegistry();
    }
    
}
