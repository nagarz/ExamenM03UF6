/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dani.java.examenuf6.controller;

import dani.java.examenuf6.model.Event;
import dani.java.examenuf6.view.AppViews;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author dani
 */
public class EventDAO implements DAO<Event> {
    
    /**
     * SessionFactory que el programa utilitzara per generar sessions per operar a la base de dades
     */
    private final SessionFactory sessionFactory;
    
    /**
     * Objecte que administra els serveis de hibernate utilitzats pel programa
     */
    private ServiceRegistry serviceRegistry;
    
    /**
     * Objecte per realitzar operacions a la base de dades
     */
    private final Session session;
    
    /**
     * Logger que registra les operacions realitzades a la base de dades
     */
    public static final Logger logger = Logger.getLogger(Main.class.getName());
    
    /**
     * Handler que el logger utilitza per guardar dades en un fitxer
     */
    private FileHandler fh;
    
    /**
     * Formatter per convertir dates a cadenes utilitzables pel logger
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    /**
     * Data per enregistrar amb el logger
     */
    private Date date = new Date();
    
    /**
     * Mètode per inicialitzar el servei per administrar les dades a la taula i inicialitzar el logger
     * i l'arxiu on guardarà la informació
     * @param sessionFactory Objecte per crear la conexió a la base de dades.
     */
    public EventDAO (SessionFactory sessionFactory) {
        if (sessionFactory == null) {
            //loads default hibernate.cfg.xml from classpath
            Configuration configuration = new Configuration().configure();
            serviceRegistry =
                    new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            //builds a session factory from the service registry
            this.sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } else {
            this.sessionFactory = sessionFactory;
        }
        session = this.sessionFactory.openSession();
        try {
            fh = new FileHandler("log.txt", true);
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException | SecurityException ex) {
            EventDAO.logger.log(Level.SEVERE, ex.getMessage());
        }
    }

    /**
     * Mètode per obtenir un registre de la taula
     * @param n Id del registre a obtenir
     * @return Event amb ID "n".
     */
    @Override
    public Event get(Integer n) {
        Transaction tx = session.beginTransaction();
        Event event = (Event) session.get(Event.class, n);
        logger.log(Level.INFO, "in get(Integer) method{}", event.toString());
        tx.commit();
        return event;
    }

    /**
     * Mètode per obtenir una llista amb tots els registres de la taula
     * @return Llista d'events
     */
    @Override
    public List<Event> getAll() {
        Transaction tx = session.beginTransaction();
        List<Event> events = session.createQuery("from Event").list();
        logger.log(Level.INFO, "in getAll() method{}", events.size());
        //AppViews.area.appendText(Level.INFO + "in getAll() method{}" + events.size() + "\n");
        tx.commit();
        return events;
    }

    /**
     * Mètode per inserir un registre nou a la taula
     * @param event Event per inserir a la taula
     */
    @Override
    public void insert(Event event) {
        logger.log(Level.INFO, "in insert(Event) method{}", event.toString());
        AppViews.area.appendText(sdf.format(date) + " " +Event.class.getName() + "\n"+ Level.INFO + ": in insert(Event) method{}" + event.toString() + "\n");
        Transaction tx = session.beginTransaction();
        session.save(event);
        tx.commit();
    }

    /**
     * Mètode per modificar un registre de la taula
     * @param event Event a modificar
     */
    @Override
    public void update(Event event) {
        logger.log(Level.INFO, "in update(Event) method{}", event.toString());
        AppViews.area.appendText(sdf.format(date) + " " + Event.class.getName() + "\n"+ Level.INFO + ": in update(Event) method{}" + event.toString() + "\n");
        Transaction tx = session.beginTransaction();
        session.update(event);
        tx.commit();
    }

    /**
     * Mètode per eliminar un registre de la taula
     * @param event Event a eliminar
     */
    @Override
    public void delete(Event event) {
        logger.log(Level.INFO, "in delete(Event) method{}", event.toString());
        AppViews.area.appendText(sdf.format(date) + " " + Event.class.getName() + "\n"+ Level.INFO + ": in delete(Event) method{}" + event.toString() + "\n");
        Transaction tx = session.beginTransaction();
        session.delete(event);
        tx.commit();
    }
    
    /**
     * Mètode per tancar el servei de conexió a la base de dades
     */
    void closeRegistry() {
        if (serviceRegistry != null ) {
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
        }
    }
    
    /**
     * Mètode per obtenir una sessió per realitzar operacions a la base de dades
     * @return una sessió
     */
    public Session getSession() {
        return this.session;
    }
    
}
