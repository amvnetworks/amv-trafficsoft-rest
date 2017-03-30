
package org.amv.trafficsoft.rest.xfcd.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * Encapsulates one parameter, either CAN or State parameter, which contains a
 * parameter name and value.
 *
 * @author <a href='mailto:elisabeth.rosemann@amv-networks.com'>Elisabeth
 *         Rosemann</a>
 * @version $Revision: 3582 $
 * @since 13.06.2016
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Encapsulates one parameter, either a CAN or State parameter, which contains a parameter name and value.")
public class ParameterDto {
    @ApiModelProperty(notes = "Required. The name of the parameter, e.g. \"kmrd\", \"speed\", etc", required = true)
    private String param;

    @ApiModelProperty(notes = "Optional. The value of the parameter. Only in string format, but can be String, Number, boolean, etc. Empty if no value available for that parameter.")
    private String value;

    @ApiModelProperty(notes = "Optional. The time the parameter was generated.")
    private Date timestamp;

    @ApiModelProperty(notes = "Optional. The longitude at which the parameter was generated.")
    private BigDecimal longitude;

    @ApiModelProperty(notes = "Optional. The latitude at which the parameter was generated.")
    private BigDecimal latitude;

    public Date getTimestamp() {
        return Optional.ofNullable(timestamp)
                .map(Date::getTime)
                .map(Date::new)
                .orElse(null);
    }

    public void setTimestamp(Date timestamp) {
        Optional.ofNullable(timestamp)
                .map(Date::getTime)
                .map(Date::new)
                .ifPresent(date -> this.timestamp = date);
    }
}