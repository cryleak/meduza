/*
 * Decompiled with CFR 0.150.
 */
package javassist.util;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javassist.util.Trigger;

public class HotSwapper {
    private VirtualMachine jvm = null;
    private MethodEntryRequest request = null;
    private Map newClassFiles = null;
    private Trigger trigger = new Trigger();
    private static final String HOST_NAME = "localhost";
    private static final String TRIGGER_NAME = Trigger.class.getName();

    public HotSwapper(int port) throws IOException, IllegalConnectorArgumentsException {
        this(Integer.toString(port));
    }

    public HotSwapper(String port) throws IOException, IllegalConnectorArgumentsException {
        AttachingConnector connector = (AttachingConnector)this.findConnector("com.sun.jdi.SocketAttach");
        Map<String, Connector.Argument> arguments = connector.defaultArguments();
        arguments.get("hostname").setValue(HOST_NAME);
        arguments.get("port").setValue(port);
        this.jvm = connector.attach(arguments);
        EventRequestManager manager = this.jvm.eventRequestManager();
        this.request = HotSwapper.methodEntryRequests(manager, TRIGGER_NAME);
    }

    private Connector findConnector(String connector) throws IOException {
        List<Connector> connectors = Bootstrap.virtualMachineManager().allConnectors();
        for (Connector con : connectors) {
            if (!con.name().equals(connector)) continue;
            return con;
        }
        throw new IOException("Not found: " + connector);
    }

    private static MethodEntryRequest methodEntryRequests(EventRequestManager manager, String classpattern) {
        MethodEntryRequest mereq = manager.createMethodEntryRequest();
        mereq.addClassFilter(classpattern);
        mereq.setSuspendPolicy(1);
        return mereq;
    }

    private void deleteEventRequest(EventRequestManager manager, MethodEntryRequest request) {
        manager.deleteEventRequest(request);
    }

    public void reload(String className, byte[] classFile) {
        ReferenceType classtype = this.toRefType(className);
        HashMap<ReferenceType, byte[]> map = new HashMap<ReferenceType, byte[]>();
        map.put(classtype, classFile);
        this.reload2(map, className);
    }

    public void reload(Map classFiles) {
        Set set = classFiles.entrySet();
        Iterator it = set.iterator();
        HashMap map = new HashMap();
        String className = null;
        while (it.hasNext()) {
            Map.Entry e = it.next();
            className = (String)e.getKey();
            map.put(this.toRefType(className), e.getValue());
        }
        if (className != null) {
            this.reload2(map, className + " etc.");
        }
    }

    private ReferenceType toRefType(String className) {
        List<ReferenceType> list = this.jvm.classesByName(className);
        if (list == null || list.isEmpty()) {
            throw new RuntimeException("no such class: " + className);
        }
        return list.get(0);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void reload2(Map map, String msg) {
        Trigger trigger = this.trigger;
        synchronized (trigger) {
            this.startDaemon();
            this.newClassFiles = map;
            this.request.enable();
            this.trigger.doSwap();
            this.request.disable();
            Map ncf = this.newClassFiles;
            if (ncf != null) {
                this.newClassFiles = null;
                throw new RuntimeException("failed to reload: " + msg);
            }
        }
    }

    private void startDaemon() {
        new Thread(){

            private void errorMsg(Throwable e) {
                System.err.print("Exception in thread \"HotSwap\" ");
                e.printStackTrace(System.err);
            }

            @Override
            public void run() {
                EventSet events = null;
                try {
                    events = HotSwapper.this.waitEvent();
                    EventIterator iter = events.eventIterator();
                    while (iter.hasNext()) {
                        Event event = iter.nextEvent();
                        if (!(event instanceof MethodEntryEvent)) continue;
                        HotSwapper.this.hotswap();
                        break;
                    }
                }
                catch (Throwable e) {
                    this.errorMsg(e);
                }
                try {
                    if (events != null) {
                        events.resume();
                    }
                }
                catch (Throwable e) {
                    this.errorMsg(e);
                }
            }
        }.start();
    }

    EventSet waitEvent() throws InterruptedException {
        EventQueue queue = this.jvm.eventQueue();
        return queue.remove();
    }

    void hotswap() {
        Map map = this.newClassFiles;
        this.jvm.redefineClasses(map);
        this.newClassFiles = null;
    }
}

