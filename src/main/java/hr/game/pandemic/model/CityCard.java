package hr.game.pandemic.model;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityCard extends Card implements Serializable {

    private String color;

    public CityCard(String name, String color) {
        super(name);
        this.color = color;
    }
}
