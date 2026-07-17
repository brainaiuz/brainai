/**
 * OmtypeTestCase.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.edatasite.workforce.gwt.core.server.target;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.ServiceFactory;
import java.net.URL;

public class OmtypeTestCase extends junit.framework.TestCase {
    public OmtypeTestCase(String name) {
        super(name);
    }

    public void testeventiPortWSDL() throws Exception {
        ServiceFactory serviceFactory = ServiceFactory.newInstance();
        URL url = new URL(new OmtypeLocator().getPortAddress() + "?WSDL");
        Service service = serviceFactory.createService(url, new OmtypeLocator().getServiceName());
        assertNotNull(service);
    }

    public void test1eventiPortAggiornaRecord() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        String value = null;
        value = binding.updateRecord("", "", "", "");
        // TBD - validate results
    }

    public void test2eventiPortAllegaFile() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        String value = null;
        value = binding.attachFile("", "", "", new byte[0]);
        // TBD - validate results
    }

    public void test3eventiPortCancellaRecord() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        String value = null;
        value = binding.deleteRecord("", "", "");
        // TBD - validate results
    }

    public void test4eventiPortConnessioniAttive() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        int value = -3;
        value = binding.activeConnections();
        // TBD - validate results
    }

    public void test5eventiPortControlloOperazione() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        String value = null;
        value = binding.controlloOperazione("", "", 0, "");
        // TBD - validate results
    }

    public void test6eventiPortEseguiOperazione() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        String value = null;
        value = binding.eseguiOperazione("", "", "", "", "", "", "", "");
        // TBD - validate results
    }

    public void test7eventiPortGestisciLibreria() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        int value = -3;
        value = binding.libraryManagers("", 0, "");
        // TBD - validate results
    }

    public void test8eventiPortInserisciRecord() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        String value = null;
        value = binding.insertRecord("", "", "");
        // TBD - validate results
    }

    public void test9eventiPortLeggiAllegato() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        byte[] value = null;
        value = binding.getAttachment("", "", "", "", "");
        // TBD - validate results
    }

    public void test10eventiPortLeggiLista() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        LoginResponse value = null;
        value = binding.login("connet", "connet", "INDE", "");

        assertEquals("CONNESSIONE AVVENUTA", value.get_return());

        String connectionid = value.get_connectionid();

        // Test operation
        String val = null;
        val = binding.getListRecords(connectionid, "CF_CNT_CRM", "", 0, "", "", 10, "", "", "");

        binding.logout(connectionid);
        // TBD - validate results
    }

    public void test11eventiPortLeggiSingolo() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        String value = null;
        value = binding.getSingleRecord("", "", "");
        // TBD - validate results
    }

    public void test12eventiPortLeggiStrutturaBase() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        TableStructure[] value = null;
        value = binding.getTableStructure("", "");
        // TBD - validate results
    }

    public void test13eventiPortLeggiStrutturaLista() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        String value = null;
        value = binding.getListStructure("", "");
        // TBD - validate results
    }

    public void test14eventiPortLeggiStrutturaSingolo() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        String value = null;
        value = binding.getSingleRecordStructure("", "");
        // TBD - validate results
    }

    public void test15eventiPortListaMinings() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        String value = null;
        value = binding.listaMinings("", "");
        // TBD - validate results
    }

    public void test16eventiPortListaStampe() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        String value = null;
        value = binding.getReports("", "");
        // TBD - validate results
    }

    public void test17eventiPortLogin() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        LoginResponse value = null;
        value = binding.login("connet", "connet", "INDE", "");

        assertEquals("CONNESSIONE AVVENUTA", value.get_return());

        String connectionid = value.get_connectionid();

        binding.logout(connectionid);
        // TBD - validate results
    }

    public void test18eventiPortLogout() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        int value = -3;
        value = binding.logout("");
        // TBD - validate results
    }

    public void test19eventiPortRichiesta() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        String value = null;
        value = binding.request("", "");
        // TBD - validate results
    }

    public void test20eventiPortStampaLista() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        byte[] value = null;
        value = binding.stampaLista("", "", "", "");
        // TBD - validate results
    }

    public void test21eventiPortTest() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        String value = null;
        value = binding.test();
        // TBD - validate results
    }

    public void test22eventiPortUtentiConnessi() throws Exception {
        BindingStub binding;
        try {
            binding = (BindingStub)
                    new OmtypeLocator().getPort();
        } catch (ServiceException jre) {
            if (jre.getLinkedCause() != null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        String value = null;
        value = binding.onlineUsers("");
        // TBD - validate results
    }

}
