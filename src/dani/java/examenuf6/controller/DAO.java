/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dani.java.examenuf6.controller;

import dani.java.examenuf6.model.Event;
import java.util.List;

/**
 *  Interficié DAO amb els mètodes per administrar la base de dades
 * @author dani
 */
public interface DAO<T> {
    
    Event get(Integer n);
    List<T> getAll();
    void insert(T t);
    void update(T t);
    void delete(T t);
    
}
