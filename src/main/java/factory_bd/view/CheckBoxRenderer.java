package factory_bd.view;

import com.vaadin.server.*;
import com.vaadin.shared.communication.SharedState;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.Renderer;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import factory_bd.entity.UserRole;

import java.io.IOException;
import java.util.Collection;
import java.util.List;


/**
 * Created by sereo_000 on 03.08.2016.
 */
public class CheckBoxRenderer implements Renderer<UserRole> {
    @Override
    public Class<UserRole> getPresentationType() {
        return null;
    }

    @Override
    public JsonValue encode(UserRole userRole) {
        return null;
    }

    @Override
    public void remove() {

    }

    @Override
    public void setParent(ClientConnector clientConnector) {

    }

    @Override
    public void addAttachListener(AttachListener attachListener) {

    }

    @Override
    public void removeAttachListener(AttachListener attachListener) {

    }

    @Override
    public void addDetachListener(DetachListener detachListener) {

    }

    @Override
    public void removeDetachListener(DetachListener detachListener) {

    }

    @Override
    public List<ClientMethodInvocation> retrievePendingRpcCalls() {
        return null;
    }

    @Override
    public boolean isConnectorEnabled() {
        return false;
    }

    @Override
    public Class<? extends SharedState> getStateType() {
        return null;
    }

    @Override
    public String getConnectorId() {
        return null;
    }

    @Override
    public ClientConnector getParent() {
        return null;
    }

    @Override
    public void requestRepaint() {

    }

    @Override
    public void markAsDirty() {

    }

    @Override
    public void requestRepaintAll() {

    }

    @Override
    public void markAsDirtyRecursive() {

    }

    @Override
    public boolean isAttached() {
        return false;
    }

    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }

    @Override
    public Collection<Extension> getExtensions() {
        return null;
    }

    @Override
    public void removeExtension(Extension extension) {

    }

    @Override
    public UI getUI() {
        return null;
    }

    @Override
    public void beforeClientResponse(boolean b) {

    }

    @Override
    public JsonObject encodeState() {
        return null;
    }

    @Override
    public boolean handleConnectorRequest(VaadinRequest vaadinRequest, VaadinResponse vaadinResponse, String s) throws IOException {
        return false;
    }

    @Override
    public ServerRpcManager<?> getRpcManager(String s) {
        return null;
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return null;
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {

    }
}
