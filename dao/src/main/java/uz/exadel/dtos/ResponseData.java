package uz.exadel.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {
    private Object data;
    private String detail;
    private Integer statusCode = 200;

    public ResponseData(Object data) {
        this.data = data;
    }

    public ResponseData(Object data, String detail) {
        this.data = data;
        this.detail = detail;
    }
}
