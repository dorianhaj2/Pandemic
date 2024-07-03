package hr.game.pandemic.jndi;

import lombok.Getter;

@Getter
public enum ConfigurationKey {
    RMI_HOST("rmi.host"), RMI_PORT("rmi.port"), MULT_HOST("mult.host"), MULT_PORT("mult.port");

    private String key;

    ConfigurationKey(String key) {
        this.key = key;
    }
}
