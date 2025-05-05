package bumh3r.interfaces;

import bumh3r.model.Dispositivo;

@FunctionalInterface
public interface DeviceCallback {
    void action(Dispositivo var1, int var2);
}
