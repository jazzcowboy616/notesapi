package org.speer.assessment.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;

@Data
@ResponseBody
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse implements Serializable {
    private String code;
    private String msg;
}
