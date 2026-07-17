/**
 * PortType.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.edatasite.workforce.gwt.core.server.target;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PortType extends Remote {

    /**
     * Persistent :: Aggiorna record
     */
    String updateRecord(String _connectionid, String table, String recordXml, String key) throws RemoteException;

    /**
     * Persistent :: Allega file ad una tabella di allegati
     */
    String attachFile(String _connectionid, String parTabella, String parRecordXml, byte[] parFile) throws RemoteException;

    /**
     * Persistent :: Cancellazione record
     */
    String deleteRecord(String _connectionid, String table, String key) throws RemoteException;

    /**
     * Static :: Connessioni attive
     */
    int activeConnections() throws RemoteException;

    /**
     * Persistent :: Controllo se operazione valida
     */
    String controlloOperazione(String _connectionid, String parTabella, int parOperazione, String parRecordXml) throws RemoteException;

    /**
     * Persistent :: Esecuzione specifica operazione della tabella
     */
    String eseguiOperazione(String _connectionid, String parTabella, String parOperazione, String parParametro1, String parParametro2, String parParametro3, String parParametro4, String parParametro5) throws RemoteException;

    /**
     * Static :: Gestione libreria
     */
    int libraryManagers(String parLibreria, int parOperazione, String parControllo) throws RemoteException;

    /**
     * Persistent :: Inserimento nuovo record
     */
    String insertRecord(String _connectionid, String table, String recordXml) throws RemoteException;

    /**
     * Persistent :: Lettura allegato
     */
    byte[] getAttachment(String _connectionid, String parTabella, String parChiave, String parAlt, String parLar) throws RemoteException;

    /**
     * Persistent :: Lettura lista records
     */
    String getListRecords(String _connectionid, String table, String fieldList, int readType, String where, String sort, int numOfRecord, String field1, String field2, String field3) throws RemoteException;

    /**
     * Persistent :: Lettura singolo record
     */
    String getSingleRecord(String _connectionid, String parTabella, String parChiave) throws RemoteException;

    /**
     * Persistent :: Restituisce la struttura della tabella in parametro
     */
    TableStructure[] getTableStructure(String _connectionid, String table) throws RemoteException;

    /**
     * Persistent :: Informazioni sulla struttura della lista records
     */
    String getListStructure(String _connectionid, String parTabella) throws RemoteException;

    /**
     * Persistent :: Informazioni sulla struttura del singolo record
     */
    String getSingleRecordStructure(String _connectionid, String parTabella) throws RemoteException;

    /**
     * Persistent :: Lista dataminings disponibili per la tabella
     */
    String listaMinings(String _connectionid, String parTabella) throws RemoteException;

    /**
     * Persistent :: Lista report disponibili per la tabella
     */
    String getReports(String _connectionid, String parTabella) throws RemoteException;

    /**
     * New Persistent :: Apre la connessione al sistema e torna un
     * id con cui chiamare i metodi persistenti
     */
    LoginResponse login(String username, String password, String controller, String personal) throws RemoteException;

    /**
     * Persistent :: Uscita dal sistema
     */
    int logout(String _connectionid) throws RemoteException;

    /**
     * Static :: Inserimento nuova richiesta
     */
    String request(String parRichiestaConnessione, String parRichiestaParametri) throws RemoteException;

    /**
     * Persistent :: Stampa lista con il report indicato
     */
    byte[] stampaLista(String _connectionid, String parTabella, String parReport, String parListaXml) throws RemoteException;

    /**
     * Static :: Test
     */
    String test() throws RemoteException;

    /**
     * Static :: Elenco utenti connessi
     */
    String onlineUsers(String controller) throws RemoteException;
}
