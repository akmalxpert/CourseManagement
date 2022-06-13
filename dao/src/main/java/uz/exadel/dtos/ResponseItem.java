package uz.exadel.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseItem {
    private Object data;
    private String message;
    private Boolean success;

    public ResponseItem(Object data) {
        this.data = data;
    }
}
