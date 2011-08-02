import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;


public class LiveCodeJmDNSHelper {
 
 private final static String EXIT_COMMAND = "exit";
 private final static String LIST_COMMAND = "list";
 private final static String REGISTER_COMMAND = "register";
 private final static String UNREGISTER_COMMAND = "unregister";
 
 private JmDNS jmdns;
 private Map<String, ServiceInfo> regMap; 
 
 public LiveCodeJmDNSHelper() {
  super();
 }
 
 public void start() throws IOException {
  this.jmdns = JmDNS.create();
  this.regMap = new HashMap<String, ServiceInfo>();
  String commandLine = "";  // the last received command line
  System.out.println("LiveCodeJmDNSHelper started. Type the command you wish to execute (type 'exit' to stop):");
  final InputStreamReader isr = new InputStreamReader(System.in);
  final BufferedReader bir = new BufferedReader(isr);
  
  while(!EXIT_COMMAND.equals(commandLine)) {
   commandLine = bir.readLine();
   final String[] commandParts = commandLine.split(" ");
   final String commandName = commandParts[0];
   if (EXIT_COMMAND.equalsIgnoreCase(commandName)) {
    System.out.println("LiveCodeHelper is exiting");
    System.exit(0);
   } else if (LIST_COMMAND.equalsIgnoreCase(commandName)) {
    handleListCommand(commandParts);
   } else if (REGISTER_COMMAND.equalsIgnoreCase(commandName)) {
    handleRegisterCommand(commandParts);
   } else if (UNREGISTER_COMMAND.equalsIgnoreCase(commandName)) {
    handleUnregisterCommand(commandParts);
   } else {
    System.out.println("Unrecognized command: " + commandLine);
   }
  }
 }

 private void handleListCommand(final String[] commandParts) {
  final String svcType = commandParts[1];
  final ServiceInfo[] svcInfos = this.jmdns.list(svcType);
  for (ServiceInfo svcInfo : svcInfos) {
   System.out.println(svcInfo);
  }
  System.out.println(".");
 }

 private void handleRegisterCommand(final String[] commandParts) {
  final String svcAlias = commandParts[1];
  final String svcType = commandParts[2];
  final String svcName = commandParts[3];
  final int svcPort = Integer.parseInt(commandParts[4]);
  final String svcText = commandParts[5];
  ServiceInfo svcInfo = ServiceInfo.create(svcType, svcName, svcPort, svcText);
  try {
   this.jmdns.registerService(svcInfo);
   this.regMap.put(svcAlias, svcInfo);
   System.out.println("Registered service '" + svcAlias + "' as: " + svcInfo);
  } catch (IOException e) {
   System.out.println("Failed to register service '" + svcAlias + "'");
  }
 }

 private void handleUnregisterCommand(final String[] commandParts) {
  final String svcAlias = commandParts[1];
  final ServiceInfo svcInfo = this.regMap.remove(svcAlias);
  if (svcInfo != null) {
   this.jmdns.unregisterService(svcInfo);
   System.out.println("Unregistered service '" + svcAlias + "'");
  } else {
   System.out.println("Failed to unregister service '" + svcAlias + "'");
  }
 }
 
 public static void main(String[] args) throws IOException {
  final LiveCodeJmDNSHelper helper = new LiveCodeJmDNSHelper();
  helper.start();
 }

}