package gov.nist.toolkit.toolkitFramework.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import gov.nist.toolkit.toolkitFramework.client.toolSupport.command.CommandContext;

import java.util.List;

/**
 *
 */
public interface FrameworkServiceAsync {
    void getDefaultEnvironment(CommandContext context, AsyncCallback<String> callback);
    void getEnvironmentNames(CommandContext context, AsyncCallback<List<String>> callback);
    void setEnvironment(CommandContext context, AsyncCallback<String> callback);
    void getMesaTestSessionNames(CommandContext request, AsyncCallback<List<String>> callback);
    void addMesaTestSession(CommandContext context, AsyncCallback<Boolean> callback);
    void delMesaTestSession(CommandContext context, AsyncCallback<Boolean> callback);

}